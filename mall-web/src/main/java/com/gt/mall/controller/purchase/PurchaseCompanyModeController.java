package com.gt.mall.controller.purchase;

import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.entity.purchase.PurchaseCompanyMode;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.service.web.purchase.PurchaseCompanyModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Controller
@RequestMapping( "purchaseCompany" )
public class PurchaseCompanyModeController extends BaseController {

    @Autowired
    PurchaseCompanyModeService companyService;

    /**
     * 公司首页
     *
     * @param request
     * @param parms
     *
     * @return
     */
    @RequestMapping( value = "/companyIndex" )
    public String orderIndex( HttpServletRequest request, @RequestParam Map< String,Object > parms ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( parms.get( "searchType" ) ) && parms.get( "searchType" ).toString().equals( "0" ) ) {
		parms.put( "companyName", parms.get( "search" ) );
	    } else if ( CommonUtil.isNotEmpty( parms.get( "searchType" ) ) && parms.get( "searchType" ).toString().equals( "1" ) ) {
		parms.put( "companyTel", parms.get( "search" ) );
	    } else if ( CommonUtil.isNotEmpty( parms.get( "searchType" ) ) && parms.get( "searchType" ).toString().equals( "2" ) ) {
		parms.put( "companyInternet", parms.get( "search" ) );
	    }
	    parms.put( "busId", user.getId() );
	    PageUtil page = companyService.findList( parms );
	    request.setAttribute( "page", page );
	    request.setAttribute( "parms", parms );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/moban";
    }

    /**
     * 公司新增修改页
     *
     * @param request
     * @param companyId
     * @param company
     *
     * @return
     */
    @RequestMapping( value = "/companyForm" )
    public String form( HttpServletRequest request, Integer companyId, PurchaseCompanyMode company ) {
	String title = "新增公司模板";
	if ( companyId != null && companyId > 0 ) {
	    company = companyService.selectById( companyId );
	    if ( company != null ) {
		title = "修改公司模板";
	    }
	}
	request.setAttribute( "title", title );
	request.setAttribute( "company", company );
	return "mall/purchase/moban-add";
    }

    @ResponseBody
    @RequestMapping( value = "/saveCompany" )
    public Map< String,Object > saveContract( HttpServletRequest request, @RequestParam Map< String,Object > parms ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    PurchaseCompanyMode company = new PurchaseCompanyMode();
	    company.setBusId( user.getId() );
	    company.setCompanyAddress( parms.get( "companyAddress" ).toString() );
	    company.setCompanyInternet( parms.get( "companyInternet" ).toString() );
	    company.setCompanyName( parms.get( "companyName" ).toString() );
	    company.setLatitude( parms.get( "latitude" ) != null ? parms.get( "latitude" ).toString() : null );
	    company.setLongitude( parms.get( "longitude" ) != null ? parms.get( "longitude" ).toString() : null );
	    company.setCompanyTel( parms.get( "companyTel" ).toString() );
	    if ( parms.get( "id" ) != null && CommonUtil.isNotEmpty( parms.get( "id" ).toString() ) ) {
		company.setId( Integer.parseInt( parms.get( "id" ).toString() ) );
		companyService.updateById( company );
	    } else {
		companyService.insert( company );
	    }
	    map.put( "result", true );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	}
	return map;
    }

    @ResponseBody
    @RequestMapping( value = "/deleteCompany" )
    public Map< String,Object > deleteCompanyIds( @RequestParam Integer companyId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    companyService.deleteById( companyId );
	    map.put( "result", true );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	}
	return map;
    }

}
