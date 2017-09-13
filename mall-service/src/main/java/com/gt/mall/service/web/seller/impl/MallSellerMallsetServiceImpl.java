package com.gt.mall.service.web.seller.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.seller.MallSellerJoinProductDAO;
import com.gt.mall.dao.seller.MallSellerMallsetDAO;
import com.gt.mall.dao.seller.MallSellerProductDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.seller.MallSellerJoinProduct;
import com.gt.mall.entity.seller.MallSellerMallset;
import com.gt.mall.entity.seller.MallSellerProduct;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员商城设置 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSellerMallsetServiceImpl extends BaseServiceImpl< MallSellerMallsetDAO,MallSellerMallset > implements MallSellerMallsetService {

    @Autowired
    private MallSellerMallsetDAO mallSellerMallsetDAO;

    @Autowired
    private MallSellerProductDAO mallSellerProductDAO;

    @Autowired
    private MallPifaDAO mallPifaDAO;

    @Autowired
    private MallPaySetDAO mallPaySetDAO;

    @Autowired
    private MallPifaApplyService mallPifaApplyService;

    @Autowired
    private MallSellerJoinProductDAO mallSellerJoinProductDAO;

    @Autowired
    private MallSellerService mallSellerService;

    @Autowired
    private MallPageService mallPageService;

    @Autowired
    private MallProductService mallProductService;

    @Autowired
    private MallPaySetService mallPaySetService;

    @Autowired
    private MallPifaService mallPifaService;

    /**
     * 通过销售员id查询商城设置
     *
     * @param memberId 销售员id
     *
     * @return 商城设置
     */
    @Override
    public MallSellerMallset selectByMemberId( int memberId ) {
	return mallSellerMallsetDAO.selectBySaleMemberId( memberId );
    }

    /**
     * 查询销售员自选的商品
     *
     * @param params 参数
     *
     * @return 自选商品
     */
    @Override
    public List< MallSellerProduct > selectProBySaleMemberId( Map< String,Object > params ) {
	return mallSellerProductDAO.selectProductByMallSet( params );
    }

    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > saveOrUpdateSeller( Member member,
		    Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	boolean flag = false;
	int count = 0;
	int type = 1;
	int busUserId = member.getBusid();
	if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
	    type = CommonUtil.toInteger( params.get( "type" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "mallSet" ) ) ) {
	    MallSellerMallset mallSet = JSONObject.parseObject( params.get( "mallSet" ).toString(), MallSellerMallset.class );

	    if ( CommonUtil.isEmpty( mallSet.getId() ) ) {
		//查询是否已经对商城进行设置
		MallSellerMallset set = mallSellerMallsetDAO.selectBySaleMemberId( member.getId() );
		if ( CommonUtil.isNotEmpty( set ) ) {
		    mallSet.setId( set.getId() );
		}
	    }
	    if ( CommonUtil.isNotEmpty( mallSet.getId() ) ) {
		//修改商城设置
		count = mallSellerMallsetDAO.updateById( mallSet );
	    } else {
		mallSet.setSaleMemberId( member.getId() );
		mallSet.setBusUserId( busUserId );
		//添加商城设置
		count = mallSellerMallsetDAO.insert( mallSet );
	    }
	}

	if ( count > 0 || CommonUtil.isNotEmpty( params.get( "sellerProList" ) ) ) {
	    if ( type == 2 ) {
		if ( CommonUtil.isNotEmpty( params.get( "sellerProList" ) ) ) {
		    //编辑自选商品信息
		    List< MallSellerProduct > sellerProList = JSONArray.parseArray( params.get( "sellerProList" ).toString(), MallSellerProduct.class );

		    if ( sellerProList != null && sellerProList.size() > 0 ) {
			for ( MallSellerProduct mallSellerProduct : sellerProList ) {
			    if ( CommonUtil.isEmpty( mallSellerProduct.getId() ) ) {
				Map< String,Object > param = new HashMap< String,Object >();
				param.put( "productId", mallSellerProduct.getProductId() );
				param.put( "saleMemberId", member.getId() );
				MallSellerProduct product = mallSellerProductDAO.selectByProIdSale( param );
				if ( CommonUtil.isNotEmpty( product ) ) {
				    mallSellerProduct.setId( product.getId() );
				}
			    }
			    if ( CommonUtil.isEmpty( mallSellerProduct.getId() ) ) {
				mallSellerProduct.setSaleMemberId( member.getId() );
				mallSellerProduct.setBusUserId( busUserId );
				count = mallSellerProductDAO.insert( mallSellerProduct );
			    } else {
				count = mallSellerProductDAO.updateById( mallSellerProduct );
			    }
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( params.get( "delSellerProList" ) ) ) {
		    //编辑自选商品信息
		    List< MallSellerProduct > sellerProList = JSONArray.parseArray( params.get( "delSellerProList" ).toString(), MallSellerProduct.class );

		    if ( sellerProList != null && sellerProList.size() > 0 ) {
			for ( MallSellerProduct mallSellerProduct : sellerProList ) {
			    count = mallSellerProductDAO.updateById( mallSellerProduct );
			}
		    }
		}
	    }
	    flag = true;
	}
	resultMap.put( "flag", flag );
	return resultMap;
    }

    @Override
    public List< Map< String,Object > > selectProductByMallIndex( Member member, Map< String,Object > params, MallSellerMallset mallSet ) {
	if ( CommonUtil.isNotEmpty( mallSet ) ) {
	    List< Map< String,Object > > productList = new ArrayList< Map< String,Object > >();
	    if ( mallSet.getIsOpenOptional() == 1 ) {//查询销售员自选的商品
		params.put( "mallSetId", mallSet.getId() );
		params.put( "saleMemberId", mallSet.getSaleMemberId() );
		productList = mallSellerProductDAO.selectProductBySaleMember( params );
	    } else {//查询商家所有加入销售的商品
		params.put( "busUserId", mallSet.getBusUserId() );
		productList = mallSellerProductDAO.selectProductByBusUserId( params );
	    }
	    if ( productList != null && productList.size() > 0 ) {
		double discount = mallProductService.getMemberDiscount( "1", member );//商品折扣

		boolean isPifa = mallPifaApplyService.isPifa( member );

		List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();
		for ( Map< String,Object > map : productList ) {
		    //int proId = CommonUtil.toInteger(map.get("id"));
		    double price = CommonUtil.toDouble( map.get( "pro_price" ) );
		    String image_url = map.get( "image_url" ).toString();
		    if ( map.get( "is_specifica" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( map.get( "inv_price" ) ) ) {
			price = CommonUtil.toDouble( map.get( "inv_price" ) );
			if ( CommonUtil.isNotEmpty( map.get( "specifica_img_url" ) ) ) {
			    image_url = map.get( "specifica_img_url" ).toString();
			}
		    }
		    /*MallSellerJoinProduct joinProduct = mallSellerJoinProductDao.selectByProId(proId);
		    double commissionMoney = 0;
		    if(CommonUtil.isNotEmpty(joinProduct)){
			    double commissionRate = CommonUtil.toDouble(joinProduct.getCommissionRate());//佣金比例
			    if(joinProduct.getCommissionType() == 1){//按百分比
				    commissionMoney = price*(commissionRate/100);
				    //price += CommonUtil.toDouble(df.format(commissionMoney));
			    }else if(joinProduct.getCommissionType() == 2){//按固定金额
				    commissionMoney = commissionRate;
				    //price += commissionRate;
			    }
		    }*/

		    DecimalFormat df = new DecimalFormat( "######0.00" );
		    String is_member_discount = map.get( "is_member_discount" ).toString();//商品是否参加折扣,1参加折扣
		    if ( is_member_discount.equals( "1" ) ) {
			if ( price > 0 && discount > 0 && discount < 1 ) {
			    double hyPrice = price * discount;
			    hyPrice = CommonUtil.toDouble( df.format( hyPrice ) );
			    //map.put("hyPrice",df.format(hyPrice+commissionMoney));
			    map.put( "hyPrice", df.format( hyPrice ) );
			}
		    }

		    double pfPrice = mallPifaService.getPifaPriceByProIds( isPifa, CommonUtil.toInteger( map.get( "id" ) ) );
		    if ( pfPrice >= 0 ) {
			map.put( "pfPrice", df.format( pfPrice ) );
		    }
		    //price = commissionMoney+price;
		    map.put( "image_url", image_url );
		    map.put( "price", df.format( price ) );
		    proList.add( map );
		}
		productList = proList;
	    }

	    return productList;
	}
	return null;
    }

    @Override
    public Map< String,Object > getSellerProductPrice( Map< String,Object > map ) {
	int proId = CommonUtil.toInteger( map.get( "id" ) );
	double price = CommonUtil.toDouble( map.get( "price" ) );
	double hyPrice = 0;
	double commissionMoney = 0;
	if ( CommonUtil.isNotEmpty( map.get( "hyPrice" ) ) ) {
	    hyPrice = CommonUtil.toDouble( map.get( "hyPrice" ) );
	}
	DecimalFormat df = new DecimalFormat( "######0.00" );

	MallSellerJoinProduct joinProduct = mallSellerJoinProductDAO.selectByProId( proId );
	if ( CommonUtil.isNotEmpty( joinProduct ) ) {

	    double commissionRate = CommonUtil.toDouble( joinProduct.getCommissionRate() );//佣金比例
	    if ( joinProduct.getCommissionType() == 1 ) {//按百分比
		commissionMoney = price * ( commissionRate / 100 );
		//price += CommonUtil.toDouble(df.format(commissionMoney));
	    } else if ( joinProduct.getCommissionType() == 2 ) {//按固定金额
		//price += commissionRate;
	    }
	}
	if ( commissionMoney > 0 ) {
	    //map.put("price", df.format(price+commissionMoney));
	    map.put( "price", df.format( price ) );
	    if ( hyPrice > 0 ) {
		//map.put("hyPrice", df.format(hyPrice+commissionMoney));
		map.put( "hyPrice", df.format( hyPrice ) );
	    }
	}
	return map;
    }

    @Override
    public Map< String,Object > selectSellerByProId( int proId,
		    MallSellerMallset mallSeller ) {
	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "proId", proId );
	params.put( "busUserId", mallSeller.getBusUserId() );
	return mallSellerJoinProductDAO.selectSellerByProId( params );
    }

    @Override
    public List< Map< String,Object > > selectProductByBusUserId(
		    Map< String,Object > params ) {
	return mallSellerProductDAO.selectProductByBusUserId( params );
    }

    @Override
    public List< Map< String,Object > > selectProductBySaleMember(
		    Map< String,Object > params ) {
	return mallSellerProductDAO.selectProductBySaleMember( params );
    }

    @Override
    public Map< String,Object > deleteSellerProduct( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int count = 0;
	if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
	    MallSellerProduct sellerProduct = new MallSellerProduct();
	    sellerProduct.setId( CommonUtil.toInteger( params.get( "id" ) ) );
	    sellerProduct.setIsDelete( 1 );
	    count = mallSellerProductDAO.updateById( sellerProduct );
	}
	if ( count > 0 ) {
	    resultMap.put( "flag", true );
	} else {
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "删除已选商品失败" );
	}
	return resultMap;
    }

    @Override
    public PageUtil selectProductBySaleMember( MallSellerMallset mallSet, Map< String,Object > params, String type, int rType, double discount, boolean isPifa ) {
	boolean isJoinProduct = true;
	List< Map< String,Object > > xlist = new ArrayList< Map< String,Object > >();
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	PageUtil page = new PageUtil();
	if ( CommonUtil.isNotEmpty( mallSet ) ) {
	    if ( CommonUtil.isEmpty( params.get( "isFindSeller" ) ) ) {
		if ( mallSet.getIsOpenOptional() == 1 ) {//查询销售员自选的商品
		    isJoinProduct = false;
		    //					Map<String, Object> map = new HashMap<String, Object>();
		    params.put( "id", mallSet.getId() );
		    params.put( "saleMemberId", mallSet.getSaleMemberId() );
		    int rowCount = mallSellerProductDAO.selectCountProductAllByMallSet( params );

		    page = new PageUtil( curPage, pageSize, rowCount, "" );
		    params.put( "firstNum", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
		    params.put( "maxNum", pageSize );

		    list = mallSellerProductDAO.selectProductAllByMallSet( params );
		} else {//查询商家所有加入销售的商品
		    isJoinProduct = true;
		}
	    }
	    if ( isJoinProduct ) {
		params.put( "busUserId", mallSet.getBusUserId() );
		int rowCount = mallSellerJoinProductDAO.selectCountProductAllByJoinSale( params );

		page = new PageUtil( curPage, pageSize, rowCount, "" );
		params.put( "firstNum", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
		params.put( "maxNum", pageSize );

		list = mallSellerJoinProductDAO.selectProductAllByJoinSale( params );

	    }
	}
	if ( list != null && list.size() > 0 ) {
	    for ( int i = 0; i < list.size(); i++ ) {
		Map< String,Object > map1 = list.get( i );
		map1.put( "rType", params.get( "rType" ) );
		map1 = mallPageService.productGetPrice( map1, discount, isPifa );
		xlist.add( map1 );
	    }
	    page.setSubList( xlist );
	}
	return page;
    }

    @Override
    public MallSellerJoinProduct getProductCommission( int proId ) {
	MallSellerJoinProduct joinProduct = mallSellerJoinProductDAO.selectByProId( proId );
	if ( CommonUtil.isNotEmpty( joinProduct ) ) {
	    return joinProduct;
	}
	return null;
    }

    @Override
    public MallSellerProduct selectByProIdSale( int proId, int saleMemberId ) {
	Map< String,Object > params = new HashMap< String,Object >();
	if ( saleMemberId > 0 ) {
	    params.put( "saleMemberId", saleMemberId );
	}
	params.put( "productId", proId );
	MallSellerProduct product = mallSellerProductDAO.selectByProIdSale( params );
	if ( CommonUtil.isNotEmpty( product ) ) {
	    return product;
	}
	return null;
    }

    @Override
    public boolean isSellerProduct( int proId, int saleMemberId ) {
	boolean isSeller = mallSellerService.isSeller( saleMemberId );//查询该销售员是否还是销售员
	if ( isSeller ) {
	    MallSellerMallset mallSet = mallSellerMallsetDAO.selectBySaleMemberId( saleMemberId );
	    if ( CommonUtil.isNotEmpty( mallSet ) ) {
		if ( mallSet.getIsOpenOptional() == 1 ) {//自选商品
		    Map< String,Object > maps = new HashMap< String,Object >();
		    maps.put( "productId", proId );
		    maps.put( "saleMemberId", saleMemberId );
		    int count = mallSellerJoinProductDAO.selectByOptionProId( maps );
		    if ( count == 0 ) {
			isSeller = false;
		    }
		} else {//没有开启自选
		    MallSellerJoinProduct joinProduct = mallSellerJoinProductDAO.selectByProId( proId );
		    if ( CommonUtil.isEmpty( joinProduct ) ) {
			isSeller = false;
		    }
		}
	    } else {
		isSeller = false;
	    }
	}
	return isSeller;
    }

    @Override
    public void selectSellerProduct( HttpServletRequest request, int proId,
		    int saleMemberId, Map< String,Object > params, Member member ) {
	boolean isSeller = mallSellerService.isSeller( saleMemberId );//查询该销售员是否还是销售员
	MallSellerJoinProduct joinProduct = mallSellerJoinProductDAO.selectByProId( proId );
	boolean isCommission = true;
	if ( isSeller ) {
	    MallSellerMallset mallSet = mallSellerMallsetDAO.selectBySaleMemberId( saleMemberId );
	    if ( CommonUtil.isNotEmpty( mallSet ) ) {
		if ( mallSet.getIsOpenOptional() == 1 ) {//自选商品
		    Map< String,Object > maps = new HashMap<>();
		    maps.put( "productId", proId );
		    maps.put( "saleMemberId", saleMemberId );
		    int count = mallSellerJoinProductDAO.selectByOptionProId( maps );
		    if ( count == 0 ) {
			saleMemberId = 0;
			isCommission = false;
		    }
		} else {//没有开启自选
		    if ( CommonUtil.isEmpty( joinProduct ) ) {
			saleMemberId = 0;
			isCommission = false;
		    }
		}
	    }
	}
	//		if((saleMemberId == member.getId() && isSeller) || CommonUtil.isNotEmpty(params.get("view"))){
	if ( isSeller || CommonUtil.isNotEmpty( params.get( "view" ) ) ) {
	    if ( CommonUtil.isNotEmpty( joinProduct ) ) {
		request.setAttribute( "productCommission", joinProduct );
	    }
	}
	if ( CommonUtil.isNotEmpty( member ) ) {
	    if ( ( saleMemberId == member.getId() && isSeller && isCommission ) ) {
		request.setAttribute( "isCommission", 1 );
	    }
	}
	if ( CommonUtil.isNotEmpty( params.get( "view" ) ) ) {
	    request.setAttribute( "isCommission", 1 );
	}
	if ( saleMemberId > 0 ) {
	    request.setAttribute( "saleMemberId", saleMemberId );
	}
	//查询会员是否是销售员
	int state = -1;
	if ( CommonUtil.isNotEmpty( member ) ) {
	    MallPaySet paySet = new MallPaySet();
	    paySet.setUserId( member.getBusid() );
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );

	    int status = mallSellerService.selectSellerStatusByMemberId( member, set );
	    String shareMsg = "您还没申请销售员,是否前往申请";
	    if ( status == 0 ) {
		state = -2;
		shareMsg = "销售员正在审核中";
	    } else if ( status == -1 ) {
		//state = -2;
		shareMsg = "您的销售员审核不通过,是否前往修改申请";
	    } else if ( status == -2 ) {
		shareMsg = "您还没申请销售员,是否前往修改申请";
	    } else if ( status == -3 ) {
		shareMsg = "您的销售员已暂停使用";
		state = -2;
	    } else if ( status == 1 ) {
		state = 1;
		//判断用户是否是销售员
	    }
	    request.setAttribute( "shareState", state );
	    request.setAttribute( "shareMsg", shareMsg );
	}
    }
}