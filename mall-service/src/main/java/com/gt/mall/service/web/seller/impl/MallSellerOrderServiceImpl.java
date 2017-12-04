package com.gt.mall.service.web.seller.impl;

import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.seller.MallSellerDAO;
import com.gt.mall.dao.seller.MallSellerOrderDAO;
import com.gt.mall.entity.seller.MallSellerOrder;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.seller.MallSellerOrderService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员订单中间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSellerOrderServiceImpl extends BaseServiceImpl< MallSellerOrderDAO,MallSellerOrder > implements MallSellerOrderService {

    @Autowired
    private MallSellerOrderDAO mallSellerOrderDAO;
    @Autowired
    private MemberService      memberService;
    @Autowired
    private MallSellerDAO      mallSellerDAO;

    /**
     * 查询销售员的订单信息
     */
    @Override
    public Map< String,Object > selectOrderByMemberId( Map< String,Object > params ) {
	return mallSellerOrderDAO.selectOrderByMemberId( params );
    }

    /**
     * 查询客户的订单
     */
    @Override
    public List< Map< String,Object > > selectOrderByClientId( Map< String,Object > params ) {
	List< Map< String,Object > > mapList = mallSellerOrderDAO.selectOrderByClientId( params );
	if ( mapList != null && mapList.size() > 0 ) {
	    for ( Map< String,Object > map : mapList ) {
		Member member = memberService.findMemberById( CommonUtil.toInteger( map.get( "buyer_user_id" ) ), null );
		if ( member != null ) {
		    map.put( "nickname", member.getNickname() );
		}
	    }
	}

	return mapList;
    }

    @Override
    public PageUtil selectSellerByBusUserId( Map< String,Object > params ) {

	List< Map< String,Object > > countList = mallSellerDAO.selectSellerByBusUserId( params );

	int pageSize = 15;
	int count = countList.size();

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );

	PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	List< Map< String,Object > > rankList = mallSellerOrderDAO.selectSalePricerByUserId( params );
	if ( rankList != null && rankList.size() > 0 ) {
	    for ( Map< String,Object > rankMap : rankList ) {
		if ( CommonUtil.isNotEmpty( rankMap.get( "member_id" ) ) ) {
		    Member member = memberService.findMemberById( CommonUtil.toInteger( rankMap.get( "member_id" ) ), null );
		    if ( member != null ) {
			rankMap.put( "headimgurl", member.getHeadimgurl() );
			rankMap.put( "nickname", member.getNickname() );
		    }
		}
	    }
	}

	page.setSubList( rankList );

	return page;
    }
}
