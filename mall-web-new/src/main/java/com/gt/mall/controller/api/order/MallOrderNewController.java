package com.gt.mall.controller.api.order;

import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.DictBean;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.OrderDTO;
import com.gt.mall.result.order.OrderResult;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallDaifuService;
import com.gt.mall.service.web.order.MallOrderReturnLogService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
@RequestMapping( "/mallOrder/E9lM9uM4ct" )
public class MallOrderNewController extends BaseController {

    @Autowired
    private MallOrderService          mallOrderService;
    @Autowired
    private MallStoreService          mallStoreService;
    @Autowired
    private MallGroupBuyService       mallGroupBuyService;
    @Autowired
    private MallOrderDAO              mallOrderDAO;
    @Autowired
    private MallDaifuService          mallDaifuService;
    @Autowired
    private DictService               dictService;
    @Autowired
    private BusUserService            busUserService;
    @Autowired
    private MallOrderReturnService    mallOrderReturnService;
    @Autowired
    private MallOrderReturnLogService mallOrderReturnLogService;

    @ApiOperation( value = "订单列表(分页)", notes = "订单列表(分页)" )
    @ResponseBody
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute OrderDTO orderQuery ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    EntityDtoConverter converter = new EntityDtoConverter();
	    Map< String,Object > params = new HashMap<>();
	    params = converter.beanToMap( orderQuery );

	    BusUser user = MallSessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isEmpty( orderQuery.getShopId() ) ) {
		params.put( "shoplist", shoplist );
	    }

	    PageUtil page = mallOrderService.findByPage( params, shoplist );
	    result.put( "page", page );
	    //	    result.put( "urlPath", PropertiesUtil.getDomain() );
	    result.put( "videourl", busUserService.getVoiceUrl( "79" ) );
	} catch ( BusinessException e ) {
	    logger.error( "订单列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "订单列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "订单列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    @ApiOperation( value = "获取各状态下订单总数", notes = "获取各状态下订单总数" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "orderType", value = "页面 0所有订单  -1维权订单 默认0", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/countStatus", method = RequestMethod.POST )
    public ServerResponse countStatus( HttpServletRequest request, HttpServletResponse response, Integer shopId, Integer orderType ) {
	BusUser user = MallSessionUtils.getLoginUser( request );
	Map< String,Object > result = new HashMap<>();
	try {
	    //订单状态（1,待付款 2,待发货 3,已发货 4,已完成 5,已关闭  6退款中 7全部 8退款处理中 9 退款结束）
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    Map< String,Object > params = new HashMap<>();
	    params.put( "userId", user.getId() );
	    params.put( "shopId", shopId );
	    if ( CommonUtil.isEmpty( shopId ) ) {
		params.put( "shoplist", shoplist );
	    }
	    if ( orderType == -1 ) {
		//退款全部
		params.put( "status", "7" );
		Integer status7 = mallOrderService.countStatus( params );
		//8退款处理中
		params.put( "status", "8" );
		Integer status8 = mallOrderService.countStatus( params );
		//退款结束
		params.put( "status", "9" );
		Integer status9 = mallOrderService.countStatus( params );

		result.put( "status7", status7 );
		result.put( "status8", status8 );
		result.put( "status9", status9 );
	    } else {
		//全部
		Integer status_total = mallOrderService.countStatus( params );
		//待付款
		params.put( "status", "1" );
		Integer status1 = mallOrderService.countStatus( params );
		//待发货
		params.put( "status", "2" );
		Integer status2 = mallOrderService.countStatus( params );
		//已发货
		params.put( "status", "3" );
		Integer status3 = mallOrderService.countStatus( params );
		//已完成
		params.put( "status", "4" );
		Integer status4 = mallOrderService.countStatus( params );
		//已关闭
		params.put( "status", "5" );
		Integer status5 = mallOrderService.countStatus( params );
		//退款中
		params.put( "status", "6" );
		Integer status6 = mallOrderService.countStatus( params );

		result.put( "status_total", status_total );
		result.put( "status1", status1 );
		result.put( "status2", status2 );
		result.put( "status3", status3 );
		result.put( "status4", status4 );
		result.put( "status5", status5 );
		result.put( "status6", status6 );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取各状态下订单总数异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取各状态下订单总数异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 查看订单详情
     */
    @ApiOperation( value = "查看订单详情", notes = "查看订单详情" )
    @ResponseBody
    @RequestMapping( value = "/orderInfo", method = RequestMethod.POST )
    public ServerResponse< OrderResult > orderInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "订单ID", required = true ) @RequestParam Integer id ) {
	OrderResult result = null;
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderId", id );
	    result = mallOrderService.selectOrderList( id );

	} catch ( Exception e ) {
	    logger.error( "查看订单详情异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查看订单详情异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 添加卖家备注、修改订单金额
     * 取消订单理由、订单发货
     */
    @ApiOperation( value = "修改状态", notes = "修改状态" )
    @ResponseBody
    @SysLogAnnotation( op_function = "3", description = "添加添加卖家备注、修改订单金额、取消订单理由、订单发货" )
    @RequestMapping( value = "/updateStatus", method = RequestMethod.POST )
    public ServerResponse updateStatus( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    //修改订单价格 type=3
	    /*
		 detail[i] = {
                    id: $(this).attr("o_id"),
                    proMoney: detailMoney,
                    num: $(this).find("#proNum").val()
                };
	    *       orderId: orderId,
                    orderMoney: orderMoney,
                    detailObj: JSON.stringify(detail)
	    * */

	    //发货确认按钮 type=4
	    /*
	    *   orderId: orderId,
		expressDelivery: expressDelivery,
		expressId: expressId,
		expressWay: expressWay,
		express: 1
	    * */

	    //提交备注 type=1
	    /*
	    *    orderId: orderId,
                 orderSellerRemark: remark
	    * */

	    //取消订单理由 type=2
	    /*
	    *   type: type,
		orderId: orderId,
		sellerReason: reason
	    * */

	    //收货type=5
	    /*
	    *    type: type,
                orderId: orderId,
                status: 4
	    * */

	    Integer count = mallOrderService.upOrderNoOrRemark( params );
	    if ( count <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改审核状态异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "修改状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "修改状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改状态异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取取消订单理由列表
     */
    @ApiOperation( value = "获取取消订单理由列表", notes = "获取取消订单理由列表" )
    @ResponseBody
    @RequestMapping( value = "/cancelReasonMap", method = RequestMethod.POST )
    public ServerResponse< List< DictBean > > cancelReasonMap( HttpServletRequest request, HttpServletResponse response ) {
	List< DictBean > cancelReason = null;
	try {
	    cancelReason = dictService.getDict( "1079" );
	} catch ( Exception e ) {
	    logger.error( "获取取消订单理由列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取取消订单理由列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), cancelReason, false );
    }

    /**
     * 判断是否可以发货 （团购商品）
     */
    @ApiOperation( value = "判断团购商品是否可以发货", notes = "判断团购商品是否可以发货", hidden = true )
    @ResponseBody
    @RequestMapping( value = "/isGroupBuyDeliver", method = RequestMethod.POST )
    public ServerResponse isGroupBuyDeliver( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "groupBuyId", value = "团购ID", required = true ) @RequestParam Integer groupBuyId,
		    @ApiParam( name = "orderId", value = "订单ID", required = true ) @RequestParam Integer orderId ) {
	Boolean flag = false;
	try {
	    //查询团购需要参与的人数
	    Map< String,Object > map = mallGroupBuyService.selectGroupBuyById( groupBuyId );
	    //查询已参加团购人数
	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderId", orderId );
	    params.put( "groupBuyId", groupBuyId );
	    Map< String,Object > map1 = mallOrderDAO.groupJoinPeopleNum( params );
	    if ( Integer.parseInt( map.get( "gPeopleNum" ).toString() ) == Integer.parseInt( map1.get( "num" ).toString() ) ) {
		flag = true;//团购商品可以发货
	    }
	} catch ( Exception e ) {
	    logger.error( "判断团购商品是否可以发货异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "判断团购商品是否可以发货异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), flag, false );
    }

    /**
     * 修改退款状态（同意退款和拒绝退款）
     */
    @ApiOperation( value = " 修改退款状态（同意退款和拒绝退款）", notes = " 修改退款状态（同意退款和拒绝退款）" )
    @ResponseBody
    @SysLogAnnotation( op_function = "3", description = " 修改退款状态（同意退款和拒绝退款）" )
    @RequestMapping( value = "/updateReturn", method = RequestMethod.POST )
    public ServerResponse updateReturn( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    WxPublicUsers pUser = MallSessionUtils.getLoginPbUser( request );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MallOrderReturn orderReturn = com.alibaba.fastjson.JSONObject.parseObject( params.get( "return" ).toString(), MallOrderReturn.class );
	    Map< String,Object > map = mallOrderService.updateOrderReturn( orderReturn, params.get( "order" ), pUser );
	    Boolean flag = (Boolean) map.get( "flag" );
	    if ( flag ) {
		if ( orderReturn.getStatus() == 1 ) {//同意退款申请
		    mallOrderReturnLogService.sellerAgreeApply( orderReturn.getId(), user.getId() );
		    MallOrderReturn orderReturn1 = mallOrderReturnService.selectById( orderReturn.getId() );
		    MallOrder mallOrder = mallOrderService.selectById( orderReturn.getOrderId() );
		    String payWay = "";
		    if ( mallOrder.getOrderPayWay() == 1 || mallOrder.getOrderPayWay() == 10 ) {
			payWay = "微信";
		    } else if ( mallOrder.getOrderPayWay() == 9 ) {
			payWay = "支付宝";
		    } else if ( mallOrder.getOrderPayWay() == 3 ) {
			payWay = "储值卡";
		    }
		    mallOrderReturnLogService.refundSuccess( orderReturn.getId(), payWay, orderReturn1.getRetMoney().toString() );

		} else if ( orderReturn.getStatus() == 2 ) {//同意退款退货申请
		    mallOrderReturnLogService.sellerAgreeApply( orderReturn.getId(), user.getId() );

		} else if ( orderReturn.getStatus() == -1 ) {//拒绝申请
		    mallOrderReturnLogService.sellerRefuseRefund( orderReturn.getId(), user.getId() );

		} else if ( orderReturn.getStatus() == 5 ) {//确认收货并退款
		    mallOrderReturnLogService.sellerRefund( orderReturn.getId(), user.getId() );

		    MallOrderReturn orderReturn1 = mallOrderReturnService.selectById( orderReturn.getId() );
		    MallOrder mallOrder = mallOrderService.selectById( orderReturn.getOrderId() );
		    String payWay = "";
		    if ( mallOrder.getOrderPayWay() == 1 || mallOrder.getOrderPayWay() == 10 ) {
			payWay = "微信";
		    } else if ( mallOrder.getOrderPayWay() == 9 ) {
			payWay = "支付宝";
		    } else if ( mallOrder.getOrderPayWay() == 3 ) {
			payWay = "储值卡";
		    }
		    mallOrderReturnLogService.refundSuccess( orderReturn.getId(), payWay, orderReturn1.getRetMoney().toString() );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改退款状态异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "修改退款状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "修改退款状态异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改退款状态异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), false );
    }

    /**
     * 打印订单
     */
    @ApiOperation( value = "打印订单", notes = "打印订单" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "orderId", value = "订单ID", paramType = "query", required = true, dataType = "int" ) } )
    @RequestMapping( value = "/toPrintMallOrder", method = RequestMethod.POST )
    public ServerResponse toPrintMallOrder( HttpServletRequest request, HttpServletResponse response, Integer orderId, Integer curPage ) {
	Map< String,Object > result = null;
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderId", orderId );
	    params.put( "curPage", curPage );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( params ) ) {
		result = mallOrderService.printOrder( params, user );
	    }
	} catch ( Exception e ) {
	    logger.error( "打印订单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "打印订单异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 商城导出订单
     */
    @ApiOperation( value = "商城导出订单", notes = "商城导出订单" )
    @RequestMapping( value = "/exportMallOrder", method = RequestMethod.GET )
    public void exportMallOrder( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute OrderDTO orderQuery ) {
	OutputStream out = null;
	HSSFWorkbook workbook = null;
	try {
	    Map< String,Object > params = new HashMap<>();
	    if ( orderQuery != null ) {
		BeanMap beanMap = BeanMap.create( orderQuery );
		for ( Object key : beanMap.keySet() ) {
		    if ( CommonUtil.isNotEmpty( beanMap.get( key ) ) ) {
			params.put( key + "", beanMap.get( key ) );
		    }
		}
	    }
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );
	    String[] titles = new String[] { "订单编号", "商品", "单价", "数量", "实付金额", "优惠", "运费", "买家", "下单时间", "订单状态", "配送方式", "售后", "所属店铺", "付款方式", "收货信息", "买家留言", "卖家备注" };
	    workbook = mallOrderService.exportExcel( params, titles, 1, shoplist );

	    String filename = "商城订单" + DateTimeKit.getDateIsLink() + ".xls";//设置下载时客户端Excel的名称
	    filename = URLEncoder.encode( filename, "UTF-8" );

	    response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
	    response.setContentType( "application/vnd.ms-excel" );

	    out = new BufferedOutputStream( response.getOutputStream() );
	    workbook.write( out );

	    out.flush();
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	    logger.error( "商城导出订单：中文转换异常！" + e.getMessage() );
	} catch ( IOException e ) {
	    e.printStackTrace();
	    logger.error( "商城导出订单：IO流输出异常！" + e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "商城导出订单失败" + e.getMessage() );
	} finally {
	    try {
		if ( out != null ) {
		    out.close();
		}
		if ( workbook != null ) {
		    workbook.close();
		}
	    } catch ( IOException e ) {
		logger.error( "商城导出订单：关闭输出流异常" + e );
		e.printStackTrace();
	    }
	}
    }

    /****************************交易记录********************************/

    @ApiOperation( value = "交易记录列表(分页)", notes = "交易记录列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "orderNo", value = "订单号", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "status", value = "订单状态", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "startTime", value = "下单开始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endTime", value = "下单结束时间", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/tradeList", method = RequestMethod.POST )
    public ServerResponse tradeList( HttpServletRequest request, HttpServletResponse response, Integer curPage, String orderNo, Integer status, String startTime, String endTime ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "orderNo", orderNo );
	    params.put( "status", status );
	    params.put( "startTime", startTime );
	    params.put( "endTime", endTime );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );

	    PageUtil page = mallOrderService.findByTradePage( params );
	    result.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "交易记录列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "交易记录列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 导出交易记录订单
     */
    @ApiOperation( value = "导出交易记录订单", notes = "导出交易记录订单" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "orderNo", value = "订单号", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "status", value = "订单状态", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "startTime", value = "下单开始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endTime", value = "下单结束时间", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/exportTradeOrder", method = RequestMethod.GET )
    public void exportTradeOrder( HttpServletRequest request, HttpServletResponse response, String orderNo, Integer status, String startTime, String endTime ) {
	OutputStream out = null;
	HSSFWorkbook workbook = null;
	try {
	    Map< String,Object > params = new HashMap<>();

	    params.put( "orderNo", orderNo );
	    params.put( "status", status );
	    params.put( "startTime", startTime );
	    params.put( "endTime", endTime );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );
	    String[] titles = new String[] { "时间", "订单编号", "商品名称", "买方", "支付金额", "状态" };
	    workbook = mallOrderService.exportTradeExcel( params, titles, 1, shoplist );

	    String filename = "交易记录" + DateTimeKit.getDateIsLink() + ".xls";//设置下载时客户端Excel的名称
	    filename = URLEncoder.encode( filename, "UTF-8" );

	    response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
	    response.setContentType( "application/vnd.ms-excel" );

	    out = new BufferedOutputStream( response.getOutputStream() );
	    workbook.write( out );

	    out.flush();
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	    logger.error( "导出交易记录订单：中文转换异常！" + e.getMessage() );
	} catch ( IOException e ) {
	    e.printStackTrace();
	    logger.error( "导出交易记录订单：IO流输出异常！" + e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "导出交易记录订单失败" + e.getMessage() );
	} finally {
	    try {
		if ( out != null ) {
		    out.close();
		}
		if ( workbook != null ) {
		    workbook.close();
		}
	    } catch ( IOException e ) {
		logger.error( "导出交易记录订单：关闭输出流异常" + e );
		e.printStackTrace();
	    }
	}
    }
}
