package com.gt.mall.service.web.purchase.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.purchase.PurchaseOrderStatisticsDAO;
import com.gt.mall.entity.purchase.PurchaseOrderStatistics;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.service.web.purchase.PurchaseOrderStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Service
public class PurchaseOrderStatisticsServiceImpl extends BaseServiceImpl< PurchaseOrderStatisticsDAO,PurchaseOrderStatistics > implements PurchaseOrderStatisticsService {

    @Autowired
    private PurchaseOrderStatisticsDAO purchaseOrderStatisticsDAO;
    @Autowired
    private MemberService              memberService;

    @Override
    public PageUtil findList( Map< String,Object > parms ) {
	try {
	    int pageSize = 10;
	    int count = 0;
	    List< Map< String,Object > > map = new ArrayList< Map< String,Object > >();
	    int curPage = CommonUtil.isEmpty( parms.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( parms.get( "curPage" ) );
	    ;
	    count = purchaseOrderStatisticsDAO.findListCount( parms );
	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    page.setUrl( "statisticsForm" );
	    parms.put( "pageFirst", ( page.getCurPage() - 1 ) * 10 );
	    parms.put( "pageLast", 10 );
	    if ( count > 0 ) {
		map = purchaseOrderStatisticsDAO.findList( parms );
		if ( map != null && map.size() > 0 ) {//如果返回的结果不为空
		    for ( int i = 0; i < map.size(); i++ ) {
			Member member = memberService.findMemberById( CommonUtil.toInteger( map.get( i ).get( "member_id" ) ), null );
			map.get( i ).put( "headimgurl", member.getHeadimgurl() );
			if ( CommonUtil.isNotEmpty( member.getNickname() ) ) {
			    try {
				String bytes = member.getNickname();
				map.get( i ).put( "nickname", new String( bytes.getBytes(), "UTF-8" ) );
			    } catch ( Exception e ) {
				map.get( i ).put( "nickname", null );
			    }
			}
		    }
		}
	    }
	    page.setSubList( map );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return null;
	}
    }
}
