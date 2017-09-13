package com.gt.mall.controller.purchase;

import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.purchase.PurchaseContract;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.SessionUtils;
import com.gt.mall.service.web.purchase.PurchaseContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
@RequestMapping( "purchaseContract" )
public class PurchaseContractController extends BaseController {

    @Autowired
    PurchaseContractService contractService;

    @RequestMapping( value = "/contractIndex" )
    public String index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > parms ) {
	try {
	    BusUser busUser = SessionUtils.getLoginUser( request );
	    parms.put( "busId", busUser.getId() );
	    PageUtil page = contractService.findList( parms );
	    request.setAttribute( "page", page );
	    request.setAttribute( "parms", parms );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return "mall/purchase/hetongguanli";
    }

    @RequestMapping( value = "/contractForm" )
    public String form( HttpServletRequest request, Integer contractId, PurchaseContract contract ) {
	String title = "新增合同";
	if ( contractId != null && contractId > 0 ) {
	    contract = contractService.selectById( contractId );
	    if ( contract != null ) {
		title = "修改合同";
	    }
	}
	request.setAttribute( "title", title );
	request.setAttribute( "contract", contract );
	return "mall/purchase/hetong-add";
    }

    @ResponseBody
    @RequestMapping( value = "/saveContract" )
    public Map< String,Object > saveCarousel( HttpServletRequest request, @RequestParam Map< String,Object > parms ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser busUser = SessionUtils.getLoginUser( request );
	    PurchaseContract contract = new PurchaseContract();
	    contract.setBusId( busUser.getId() );
	    contract.setContractContent( parms.get( "contractContent" ).toString() );
	    contract.setContractTitle( parms.get( "contractTitle" ).toString() );
	    if ( parms.get( "id" ) != null && CommonUtil.isNotEmpty( parms.get( "id" ).toString() ) ) {
		contract.setId( Integer.parseInt( parms.get( "id" ).toString() ) );
		contractService.updateById( contract );
	    } else {
		contract.setCreateDate( new Date());
		contractService.insert( contract );
	    }
	    map.put( "result", true );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	}
	return map;
    }

    @ResponseBody
    @RequestMapping( value = "/deleteContract" )
    public Map< String,Object > deleteContract( @RequestParam Integer contractId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    contractService.deleteById( contractId );
	    map.put( "result", true );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    map.put( "result", false );
	}
	return map;
    }

}
