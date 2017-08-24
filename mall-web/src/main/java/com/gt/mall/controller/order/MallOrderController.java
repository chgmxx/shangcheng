package com.gt.mall.controller.order;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.util.*;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallDaifuService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.store.MallStoreService;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallOrder" )
public class MallOrderController extends BaseController {

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

    /**
     * 订单首页
     */
    @RequestMapping( value = "/toIndex" )
    public String toIndex( HttpSession session, HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入订单首页" );
	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    boolean isAdminFlag = mallStoreService.getIsAdminUser( user.getId(), request );//是管理员
	    if ( isAdminFlag ) {
		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		params.put( "shoplist", shoplist );

		//关闭超过30分钟未付款订单
		mallOrderService.updateByNoMoney( params );

		Object status = params.get( "status" );
		if ( null == status || status.equals( "" ) ) {
		    status = "";
		}
		Object orderType = params.get( "orderType" );
		if ( null == orderType ) {
		    orderType = 0;
		}
		Object orderFilter = params.get( "orderFilter" );
		if ( null == orderFilter ) {
		    orderFilter = 0;
		}
		PageUtil page = mallOrderService.findByPage( params );
		request.setAttribute( "page", page );
		request.setAttribute( "status", status );
		request.setAttribute( "orderType", orderType );
		request.setAttribute( "orderFilter", orderFilter );
		request.setAttribute( "startTime", params.get( "startTime" ) );
		request.setAttribute( "endTime", params.get( "endTime" ) );
		request.setAttribute( "orderNo", params.get( "orderNo" ) );
		request.setAttribute( "path", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "urlPath", PropertiesUtil.getArticleUrl() );
		request.setAttribute( "user", user );
	    } else {
		request.setAttribute( "isNoAdminFlag", 1 );
	    }
	    request.setAttribute( "videourl", busUserService.getVoiceUrl( "79" ) );
	} catch ( Exception e ) {
	    e.printStackTrace();
	} finally {
	    String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( startTime + "---" + endTime );
	    long second = DateTimeKit.minsBetween( startTime, endTime, 1, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    logger.info( "访问订单管理花费：" + second + "毫秒" );
	}
	logger.info( "订单加载完成，跳转页面中。。。。" );
	return "mall/order/orderIndex";

    }

    /**
     * 添加卖家备注、修改订单金额
     * 取消订单理由、订单发货
     */
    @RequestMapping( value = "/upMoneyOrRemark" )
    @SysLogAnnotation( op_function = "3", description = "添加添加卖家备注、修改订单金额、取消订单理由、订单发货" )
    public void upOrderMoneyOrRemark( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入添加卖家备注、修改订单金额方法。" );
	int count = 0;
	try {
	    count = mallOrderService.upOrderNoOrRemark( params );
	} catch ( Exception e ) {
	    logger.error( "添加卖家备注、修改订单金额方法异常" );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, count );
	}
    }

    /**
     * 订单弹出框的显示（备注、取消订单、修改价格、发货）
     */
    @RequestMapping( value = "/orderPopUp" )
    public String orderPopUp( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	String type = params.get( "type" ).toString();
	String orderId = params.get( "orderId" ).toString();
	String count = "0";
	Map< String,Object > result = new HashMap< String,Object >();
	if ( type.equals( "1" ) ) {        //备注

	} else if ( type.equals( "2" ) ) {                //取消订单
	    List< Map > cancelReason = dictService.getDict( "1079" );
	    request.setAttribute( "cancelReason", cancelReason );
	} else if ( type.equals( "3" ) ) {                //修改价格

	} else {        //发货
	    Object groupBuyId = params.get( "groupBuyId" );
	    //团购商品要凑单购买
	    if ( null != groupBuyId && !groupBuyId.equals( "" ) && groupBuyId != "" && !groupBuyId.equals( "0" ) && groupBuyId.equals( "1" ) ) {
		//查询团购需要参与的人数
		Map< String,Object > map = mallGroupBuyService.selectGroupBuyById( Integer.parseInt( groupBuyId.toString() ) );
		//查询已参加团购人数
		Map< String,Object > map1 = mallOrderDAO.groupJoinPeopleNum( params );
		if ( Integer.parseInt( map.get( "gPeopleNum" ).toString() ) == Integer.parseInt( map1.get( "num" ).toString() ) ) {
		    count = "1";//团购商品可以发货
		}
	    } else {
		count = "1";
	    }
	    List< Map > logisticsCompany = dictService.getDict( "1092" );
	    request.setAttribute( "logisticsCompany", logisticsCompany );
	}
	request.setAttribute( "count", count );
	result = mallOrderService.selectOrderList( params );
	request.setAttribute( "result", result );
	request.setAttribute( "type", type );
	request.setAttribute( "orderId", orderId );
	return "mall/order/orderPopUp";
    }

    /**
     * 查看订单详情
     */
    @RequestMapping( value = "/orderDetail" )
    public String orderDetail( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	String orderId = params.get( "orderId" ).toString();
	Map< String,Object > result = mallOrderService.selectOrderList( params );
	request.setAttribute( "result", result );
	request.setAttribute( "orderId", orderId );
	request.setAttribute( "path", PropertiesUtil.getResourceUrl() );
	return "mall/order/orderDetail";
    }

    /**
     * 修改退款状态（同意退款和拒绝退款）
     */
    @RequestMapping( value = "/updateReturn" )
    @SysLogAnnotation( op_function = "3", description = "修改退款状态" )
    public void agreedReturn( HttpServletRequest request,
		    HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) throws IOException {
	logger.info( "进入同意/拒绝退款方法。" );
	Map< String,Object > map = new HashMap<>();
	WxPublicUsers pUser = SessionUtils.getLoginPbUser( request );
	try {
	    MallOrderReturn orderReturn = (MallOrderReturn) JSONObject.toBean( JSONObject.fromObject( params.get( "return" ) ), MallOrderReturn.class );
	    map = mallOrderService.updateOrderReturn( orderReturn, params.get( "order" ), pUser );
	} catch ( Exception e ) {
	    logger.error( "同意/拒绝退款方法异常" + e.getMessage() );
	    map.put( "flag", false );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, map );
	}
    }

    /**
     * 查询退款信息
     */
    @RequestMapping( value = "/returnPopUp" )
    public String returnPopUp( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	if ( !CommonUtil.isEmpty( params.get( "rId" ) ) ) {
	    Integer returnId = CommonUtil.toInteger( params.get( "rId" ).toString() );
	    MallOrderReturn oReturn = mallOrderService.selectByDId( returnId );
	    if ( oReturn != null ) {
		MallOrder order = mallOrderService.selectById( oReturn.getOrderId() );
		if ( order.getOrderPayWay() == 7 ) {
		    MallDaifu daifu = mallDaifuService.selectByDfOrderNo( order.getOrderNo() );
		    request.setAttribute( "daifu", daifu );
		}
		request.setAttribute( "order", order );
	    }
	    request.setAttribute( "oReturn", oReturn );
	}
	request.setAttribute( "type", params.get( "type" ) );
	request.setAttribute( "oNo", params.get( "oNo" ) );
	request.setAttribute( "orderPayNo", params.get( "orderPayNo" ) );
	BusUser user = SessionUtils.getLoginUser( request );
	request.setAttribute( "busUserId", user.getId() );
	request.setAttribute( "http", PropertiesUtil.getArticleUrl() );

	return "mall/order/returnPopUp";
    }

    /**
     * 同步订单成交数
     */
    @RequestMapping( value = "/syncOrderPifa" )
    public void syncOrderPifa( HttpServletRequest request, HttpServletResponse response ) throws IOException {
	logger.info( "进入同步订单的方法。" );
	Map< String,Object > map = new HashMap<>();
	BusUser user = SessionUtils.getLoginUser( request );
	try {
	    Map< String,Object > params = new HashMap<>();
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );

	    map = mallOrderService.syncOrderbyPifa( params );

	} catch ( Exception e ) {
	    logger.error( "同步订单的方法异常" + e.getMessage() );
	    map.put( "flag", false );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, map );
	}
    }

    /**
     * 商城导出订单
     */
    @RequestMapping( value = "/exportMallOrder" )
    public void exportMallOrder( HttpServletRequest request, HttpServletResponse response,
		    @RequestParam Map< String,Object > params ) {
	OutputStream out = null;
	HSSFWorkbook workbook = null;
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );
	    String[] titles = new String[] { "订单编号", "商品", "单价", "数量", "实付金额", "优惠", "运费", "买家", "下单时间", "订单状态", "配送方式", "售后", "所属店铺", "付款方式", "收货信息", "买家留言", "卖家备注" };
	    workbook = mallOrderService.exportExcel( params, titles, 1 );

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

    /**
     * 重新生成订单号（钱包支付）
     */
    @RequestMapping( value = "/againGenerateOrderNo" )
    public void againGenerateOrderNo( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	//logger.info("进入同步订单的方法。");
	Map< String,Object > map = new HashMap<>();
	MallOrder order = null;
	try {
	    if ( CommonUtil.isNotEmpty( params ) ) {
		order = mallOrderService.againOrderNo( CommonUtil.toInteger( params.get( "id" ) ) );
	    }

	    if ( CommonUtil.isNotEmpty( order ) ) {
		map.put( "result", true );
		map.put( "no", order.getOrderNo() );
		map.put( "money", order.getOrderMoney() );
	    } else {
		map.put( "result", false );
	    }

	} catch ( Exception e ) {
	    logger.error( "商城重新生成订单号异常" + e.getMessage() );
	    map.put( "result", false );
	    e.printStackTrace();
	}
	try {
	    PrintWriter p = response.getWriter();
	    p.write( net.sf.json.JSONObject.fromObject( map ).toString() );
	    p.flush();
	    p.close();
	} catch ( IOException e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 打印订单
     */
    @RequestMapping( value = "/toPrintMallOrder" )
    public String toPrintMallOrder( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( params ) ) {
		Map< String,Object > result = mallOrderService.printOrder( params, user );
		request.setAttribute( "result", JSONObject.fromObject( result ) );

		request.setAttribute( "orderId", params.get( "orderId" ) );
		request.setAttribute( "urlPath", PropertiesUtil.getArticleUrl() );
		request.setAttribute( "user", user );
	    }
	} catch ( Exception e ) {
	    logger.error( "商城重新生成订单号异常" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/order/orderPrint";
    }
}
