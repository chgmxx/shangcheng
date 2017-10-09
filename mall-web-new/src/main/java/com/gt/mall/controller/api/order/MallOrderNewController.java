package com.gt.mall.controller.api.order;

import com.google.common.collect.Maps;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.param.GroupDTO;
import com.gt.mall.param.OrderDTO;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallDaifuService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-26
 */

@Api( value = "mallOrder", description = "商城订单", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallOrder/new" )
public class MallOrderNewController extends BaseController {

    @Autowired
    private MallOrderService    mallOrderService;
    @Autowired
    private MallStoreService    mallStoreService;
    @Autowired
    private MallGroupBuyService mallGroupBuyService;
    @Autowired
    private MallOrderDAO        mallOrderDAO;
    @Autowired
    private MallDaifuService    mallDaifuService;
    @Autowired
    private DictService         dictService;
    @Autowired
    private BusUserService      busUserService;

    /*
    *
    * 订单列表
    * 批量导出
    * 发货
    *订单详情（订单信息，商品信息，维权信息）
    *
    * 同意退款申请
    * 拒绝退款申请
    * 申请多粉介入
    * 确认收货
    *
    * 维权订单列表
    * 维权订单批量导出
    *
    *
    * */

    @ApiOperation( value = "订单列表(分页)", notes = "订单列表(分页)" )
    @ResponseBody
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, @RequestParam OrderDTO orderDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {

	    Map< String,Object > params = new HashMap<>();
	    if ( orderDTO != null ) {
		BeanMap beanMap = BeanMap.create( orderDTO );
		for ( Object key : beanMap.keySet() ) {
		    if ( CommonUtil.isNotEmpty( beanMap.get( key ) ) ) {
			params.put( key + "", beanMap.get( key ) );
		    }
		}
	    }
	    BusUser user = SessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    if ( CommonUtil.isEmpty( orderDTO.getShopId() ) ) {
		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		params.put( "shoplist", shoplist );
	    }

	    PageUtil page = mallOrderService.findByPage( params );
	    result.put( "page", page );
	    result.put( "urlPath", PropertiesUtil.getDomain() );
	    result.put( "videourl", busUserService.getVoiceUrl( "79" ) );
	} catch ( Exception e ) {
	    logger.error( "订单列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "订单列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 查看订单详情
     */
    @ApiOperation( value = "查看订单详情", notes = "查看订单详情" )
    @ResponseBody
    @RequestMapping( value = "/orderInfo", method = RequestMethod.POST )
    public ServerResponse orderInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "订单ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = null;
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderId", id );
	    result = mallOrderService.selectOrderList( params );
	} catch ( Exception e ) {
	    logger.error( "查看订单详情异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查看订单详情异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 查询退款信息
     */
    @ApiOperation( value = "查询退款信息", notes = "查询退款信息" )
    @ResponseBody
    @RequestMapping( value = "/returnPopUp", method = RequestMethod.POST )
    public ServerResponse returnPopUp( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = null;
	try {
	    //rId=" + rId + "&type=-1" + "&oNo=" + oNo + "&orderPayNo=" + orderPayNo
	    if ( !CommonUtil.isEmpty( params.get( "rId" ) ) ) {
		Integer returnId = CommonUtil.toInteger( params.get( "rId" ).toString() );
		MallOrderReturn oReturn = mallOrderService.selectByDId( returnId );
		if ( oReturn != null ) {
		    MallOrder order = mallOrderService.selectById( oReturn.getOrderId() );
		    if ( order.getOrderPayWay() == 7 ) {
			MallDaifu daifu = mallDaifuService.selectByDfOrderNo( order.getOrderNo() );
			result.put( "daifu", daifu );
		    }
		    result.put( "order", order );
		}
		result.put( "oReturn", oReturn );
	    }
	} catch ( Exception e ) {
	    logger.error( "查询退款信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查询退款信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 打印订单
     */
    @ApiOperation( value = "打印订单", notes = "打印订单" )
    @ResponseBody
    @RequestMapping( value = "/toPrintMallOrder", method = RequestMethod.POST )
    public ServerResponse toPrintMallOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = null;
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( params ) ) {
		result = mallOrderService.printOrder( params, user );
	    }
	} catch ( Exception e ) {
	    logger.error( "打印订单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "打印订单异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

}
