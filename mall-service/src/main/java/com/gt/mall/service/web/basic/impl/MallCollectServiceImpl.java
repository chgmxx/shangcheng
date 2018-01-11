package com.gt.mall.service.web.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallCollectDAO;
import com.gt.mall.entity.basic.MallCollect;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.result.phone.product.PhoneCollectProductResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.utils.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCollectServiceImpl extends BaseServiceImpl< MallCollectDAO,MallCollect > implements MallCollectService {

    private Logger log = Logger.getLogger( MallCollectServiceImpl.class );

    @Autowired
    private MallCollectDAO              collectDAO;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private MallProductInventoryService mallProductInventoryService;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;

    @Override
    public void getProductCollect( HttpServletRequest request, int proId, int userId ) {
	//        Map<String, Object> map = new HashMap<String, Object>();
	//        map.put("productId", proId);
	//        map.put("userId", userId);
	int id = 0;
	MallCollect mallCollect = new MallCollect();
	mallCollect.setUserId( userId );
	mallCollect.setProductId( proId );
	MallCollect collect = collectDAO.selectOne( mallCollect );
	if ( CommonUtil.isNotEmpty( collect ) ) {
	    if ( CommonUtil.isNotEmpty( collect.getId() ) ) {
		id = collect.getId();
		if ( collect.getIsDelete().toString().equals( "0" ) ) {
		    request.setAttribute( "isCollect", collect.getIsCollect() );
		}
	    }
	}
	request.setAttribute( "collectId", id );
    }

    @Override
    public boolean collectionProduct( int productId, int userId ) {

	MallCollect mallCollect = new MallCollect();
	mallCollect.setUserId( userId );
	mallCollect.setProductId( productId );
	MallCollect c = collectDAO.selectOne( mallCollect );

	MallCollect collect = new MallCollect();
	collect.setProductId( productId );
	if ( CommonUtil.isNotEmpty( c ) ) {
	    if ( CommonUtil.isNotEmpty( c.getId() ) ) {
		collect.setId( c.getId() );
	    }
	    //判断商品是否已收藏，如已收藏，状态修改为未收藏；反之 改为已收藏
	    if ( c.getIsCollect() == 1 ) {
		collect.setIsCollect( 0 );
	    } else {
		collect.setIsCollect( 1 );
	    }
	}
	int count = 0;
	if ( CommonUtil.isNotEmpty( collect.getId() ) ) {
	    collect.setIsDelete( 0 );
	    count = collectDAO.updateById( collect );
	} else {
	    collect.setUserId( userId );
	    collect.setCreateTime( new Date() );
	    count = collectDAO.insert( collect );
	}
	if ( count > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean deleteCollect( String ids ) {
	if ( CommonUtil.isNotEmpty( ids ) ) {

	    String[] deleteIds = ids.split( "," );
	    if ( deleteIds == null || deleteIds.length == 0 ) {
		throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "请选择要删除收藏" );
	    }
	    Map< String,Object > params = new HashMap<>();
	    params.put( "isDelete", 1 );
	    params.put( "ids", deleteIds );
	    int count = collectDAO.batchUpdateCollect( params );
	    if ( count > 0 ) {
		return true;
	    }
	} else {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "请选择要删除收藏" );
	}
	return false;
    }

    @Override
    public boolean getProductCollect( int proId, int userId ) {
	int id = 0;
	MallCollect mallCollect = new MallCollect();
	mallCollect.setUserId( userId );
	mallCollect.setProductId( proId );
	MallCollect collect = collectDAO.selectOne( mallCollect );
	if ( CommonUtil.isNotEmpty( collect ) ) {
	    if ( CommonUtil.isNotEmpty( collect.getId() ) ) {
		if ( collect.getIsCollect().toString().equals( "1" ) ) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public List< PhoneCollectProductResult > getCollectProductList( Integer memberId ) {
	if ( CommonUtil.isEmpty( memberId ) ) {
	    return null;
	}
	List< Integer > memberList = memberService.findMemberListByIds( memberId );
	Map< String,Object > params = new HashMap<>();
	if ( memberList != null && memberList.size() > 1 ) {
	    params.put( "memberIdList", memberList );
	} else {
	    params.put( "memberId", memberId );
	}
	double discount = memberService.getMemberDiscount( memberId );//会员折扣
	boolean isDiscount = false;
	if ( discount > 0 && discount < 1 ) {
	    isDiscount = true;
	}
	List< PhoneCollectProductResult > resultList = new ArrayList<>();
	List< Map< String,Object > > list = collectDAO.selectCollectByMemberId( params );

	if ( list != null && list.size() > 0 ) {
	    List< Integer > proIds = new ArrayList<>();
	    for ( Map< String,Object > map : list ) {
		proIds.add( CommonUtil.toInteger( map.get( "productId" ) ) );
	    }
	    List< MallImageAssociative > imageList = mallImageAssociativeService.selectImageByAssIds( 1, 1, proIds );
	    for ( Map< String,Object > map : list ) {
		PhoneCollectProductResult productResult = new PhoneCollectProductResult();
		productResult.setId( CommonUtil.toInteger( map.get( "id" ) ) );
		productResult.setProductId( CommonUtil.toInteger( map.get( "productId" ) ) );
		productResult.setShopId( CommonUtil.toInteger( map.get( "shop_id" ) ) );
		double price = CommonUtil.toDouble( map.get( "pro_price" ) );
		if ( "1".equals( map.get( "is_specifica" ).toString() ) ) {
		    MallProductInventory inventory = mallProductInventoryService.selectByIsDefault( productResult.getProductId() );
		    if ( CommonUtil.isNotEmpty( inventory ) ) {
			price = CommonUtil.toDouble( inventory.getInvPrice() );
		    }
		}
		if ( CommonUtil.isNotEmpty( map.get( "is_member_discount" ) ) && "1".equals( map.get( "is_member_discount" ).toString() ) && isDiscount ) {
		    productResult.setProductMemberPrice( CommonUtil.formatDoubleNumber( price * discount ) );
		}
		productResult.setProductPrice( price );
		productResult.setProductName( CommonUtil.toString( map.get( "pro_name" ) ) );
		if ( CommonUtil.isNotEmpty( map.get( "pro_label" ) ) ) {
		    productResult.setLabelName( CommonUtil.toString( map.get( "pro_label" ) ) );
		}
		if ( imageList != null && imageList.size() > 0 ) {
		    for ( int i = 0; i < imageList.size(); i++ ) {
			MallImageAssociative imageAssociative = imageList.get( i );
			if ( imageAssociative.getAssId().toString().equals( productResult.getProductId().toString() ) ) {
			    productResult.setProductImageUrl( imageAssociative.getImageUrl() );
			    imageList.remove( i );
			    break;
			}
		    }
		}

		resultList.add( productResult );
	    }
	}

	return resultList;
    }

}
