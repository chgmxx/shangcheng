package com.gt.mall.controller.api.order.phone;

import com.alibaba.fastjson.JSONArray;
import com.gt.api.bean.session.Member;
import com.gt.mall.bean.DictBean;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.basic.MallTakeTheirTime;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneBuyNowDTO;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.order.PhoneOrderListDTO;
import com.gt.mall.param.phone.order.PhoneToOrderDTO;
import com.gt.mall.param.phone.order.PhoneToOrderPifatSpecificaDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;
import com.gt.mall.param.phone.order.returns.PhoneSubmitOrderReturnDTO;
import com.gt.mall.param.phone.order.returns.PhoneSubmitOrderReturnLogisticsDTO;
import com.gt.mall.result.phone.order.PhoneToOrderResult;
import com.gt.mall.result.phone.order.daifu.PhoneGetDaiFuResult;
import com.gt.mall.result.phone.order.detail.PhoneOrderResult;
import com.gt.mall.result.phone.order.list.PhoneOrderListResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnProductResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnWayResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnWuLiuResult;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.basic.MallTakeTheirTimeService;
import com.gt.mall.service.web.order.*;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductNewService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * 订单页面相关接口
 * User : yangqian
 * Date : 2017/10/19 0019
 * Time : 17:10
 */
@Api( value = "phoneOrder", description = "订单页面相关接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneOrder/L6tgXlBFeK/" )
public class PhoneOrderNewController extends AuthorizeOrUcLoginController {

    private static Logger logger = LoggerFactory.getLogger( PhoneOrderNewController.class );

    @Autowired
    private MallProductNewService     mallProductNewService;//新的商品业务处理类
    @Autowired
    private MallOrderSubmitService    mallOrderSubmitService;//提交订单业务处理类
    @Autowired
    private MallTakeTheirTimeService  mallTakeTheirTimeService;//上门自提时间业务处理类
    @Autowired
    private MallTakeTheirService      mallTakeTheirService;//上门自提业务处理类
    @Autowired
    private MallOrderService          mallOrderService;//订单业务处理类
    @Autowired
    private MallOrderListService      mallOrderListService;//订单列表业务处理类
    @Autowired
    private DictService               dictService;//字典接口
    @Autowired
    private MallOrderDetailService    mallOrderDetailService;//订单详情业务处理类
    @Autowired
    private MallOrderReturnService    mallOrderReturnService;//订单退款业务处理类
    @Autowired
    private MallDaifuService          mallDaifuService;//代付业务处理类
    @Autowired
    private MallOrderReturnLogService mallOrderReturnLogService;//订单退款日志处理类
    @Autowired
    private MallPageService           mallPageService;//页面业务处理类
    @Autowired
    private QuartzOrderService        quartzOrderService;//以前定时任务的业务处理类

    @ApiOperation( value = "立即购买接口", notes = "用户点击立即购买", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "buyNow", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > buyNow( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneBuyNowDTO params ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, params.getBusId() );
	    int memberId = 0;
	    if ( CommonUtil.isNotEmpty( member ) ) {
		memberId = member.getId();
	    }
	    if ( CommonUtil.isEmpty( params.getProductNum() ) || params.getProductNum() <= 0 ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "购买数量最少是1" );
	    }
	    JSONObject obj = JSONObject.fromObject( params );
	    mallProductNewService
			    .calculateInventory( params.getProductId(), params.getProductSpecificas(), params.getProductNum(), params.getType(), params.getActivityId(), memberId );
	    if ( CommonUtil.isNotEmpty( params.getPifaSpecificaDTOList() ) ) {
		List< PhoneToOrderPifatSpecificaDTO > pifaSpecificaDTOList = JSONArray.parseArray( params.getPifaSpecificaDTOList(), PhoneToOrderPifatSpecificaDTO.class );
		obj.remove( "pifaSpecificaDTOList" );
		obj.put( "pifaSpecificaDTOList", pifaSpecificaDTOList );
		for ( PhoneToOrderPifatSpecificaDTO pifatSpecificaDTO : pifaSpecificaDTOList ) {
		    mallProductNewService.calculateInventory( params.getProductId(), pifatSpecificaDTO.getSpecificaValueIds(), pifatSpecificaDTO.getProductNum(), params.getType(),
				    params.getActivityId(), memberId );
		}
	    }

	    logger.error( "cookie存值：" + obj.toString() );
	    CookieUtil.addCookie( response, CookieUtil.TO_ORDER_KEY, obj.toString(), 0 );

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "立即购买异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "立即购买失败" );
	}
    }

    @ApiOperation( value = "进入提交订单页面的接口", notes = "提交订单页面的接口，查询商品信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "toOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneToOrderResult > toOrder( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneToOrderDTO params,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    //封装登陆参数
	    /*loginDTO.setUcLogin( 1 );//不需要登陆
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断*/

	    PhoneToOrderResult result = mallOrderSubmitService.toOrder( params, member, loginDTO, request );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "进入提交订单页面的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "请求提交订单的数据失败" );
	}
    }

    @ApiOperation( value = "提交订单的接口", notes = "提交订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "params", value = "参数,必传", paramType = "query", required = true, dataType = "String" ) )
    @ResponseBody
    @PostMapping( value = "submitOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > submitOrder( HttpServletRequest request, HttpServletResponse response, String params,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    PhoneAddOrderDTO addOrderDTO = com.alibaba.fastjson.JSONObject.parseObject( params, PhoneAddOrderDTO.class );
	    Map< String,Object > resultMap = mallOrderSubmitService.submitOrder( addOrderDTO, member, loginDTO.getBrowerType() );

	    if ( CommonUtil.isNotEmpty( addOrderDTO.getShopCartIds() ) ) {
		mallPageService.mergeShoppCart( member, request, response );
	    }

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resultMap, false );
	} catch ( BusinessException e ) {
	    logger.error( "提交订单的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "提交订单的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "提交订单失败" );
	}
    }

    @ApiOperation( value = "查询上门自提时间的接口", notes = "根据上门自提id查询上门自提时间", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "takeId", value = "上门自提id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "getTakeTheirTime", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< MallTakeTheirTime > > getTakeTheirTime( HttpServletRequest request, HttpServletResponse response, Integer takeId ) {
	try {
	    List< MallTakeTheirTime > timeList = mallTakeTheirTimeService.selectTakeTheirTime( takeId );//查询到店自提的默认地址
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), timeList, false );
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "查询上门自提时间的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询上门自提时间失败" );
	}
    }

    @ApiOperation( value = "查询上门自提地址的接口", notes = "根据商家id查询上门自提地址", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "provinceIds", value = "省份id", paramType = "query", dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "getTakeTheir", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< MallTakeTheir > > getTakeTheir( HttpServletRequest request, HttpServletResponse response, Integer busId, Integer provinceIds ) {
	try {
	    Map< String,Object > map = new HashMap<>();
	    map.put( "userId", busId );
	    if ( CommonUtil.isNotEmpty( provinceIds ) && provinceIds > 0 ) {
		map.put( "provinceId", provinceIds );
	    }
	    //根据公众号id查询提取信息
	    List< MallTakeTheir > takeList = mallTakeTheirService.selectByBusUserId( map );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), takeList, false );
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "查询上门自提地址的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询上门自提地址失败" );
	}
    }

    /**
     * 支付成功回调
     */
    @ApiOperation( value = "支付成功回调的接口", notes = "支付成功回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, hidden = true )
    @ResponseBody
    @PostMapping( value = "paySuccessModified" )
    public ServerResponse paySuccessModified( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) throws IOException {
	logger.info( " 支付成功回调参数：" + JSONObject.fromObject( params ) );
	try {
	    if ( CommonUtil.isEmpty( params.get( "out_trade_no" ) ) ) {
		return ServerResponse.createByErrorMessage( "支付成功回调失败：参数=" + JSONObject.fromObject( params ) );
	    }
	    mallOrderService.paySuccessModified( params, null );
	} catch ( BusinessException e ) {
	    logger.error( "支付成功回调异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "支付成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "支付成功回调失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    @ApiOperation( value = "手机端订单列表的接口", notes = "我的订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "orderList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneOrderListResult > orderList( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneOrderListDTO params,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    //查询订单列表
	    PhoneOrderListResult result = mallOrderListService.orderList( params, loginDTO, MallSessionUtils.getLoginMember( request, loginDTO.getBusId() ) );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "手机端订单列表的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "手机端订单列表的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询我的订单失败" );
	}
    }

    @ApiOperation( value = "手机端订单详情接口", notes = "订单详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "orderId", value = "订单id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "orderDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneOrderResult > orderDetail( HttpServletRequest request, HttpServletResponse response, Integer orderId,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    //查询订单列表
	    PhoneOrderResult result = mallOrderListService.getOrderDetail( orderId, loginDTO.getBusId() );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "手机端订单详情接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "手机端订单详情接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询订单详情失败" );
	}
    }

    @ApiOperation( value = "查询退款方式接口", notes = "查询退款方式", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "orderDetailId", value = "订单详情id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "getReturnStyle", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneReturnProductResult > getReturnStyle( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer orderDetailId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    MallOrderDetail detail = mallOrderDetailService.selectById( orderDetailId );
	    if ( CommonUtil.isEmpty( detail ) ) {
		return ErrorInfo.createByNullError();
	    }
	    MallOrder order = mallOrderService.selectById( detail.getOrderId() );
	    if ( CommonUtil.isEmpty( order ) ) {
		return ErrorInfo.createByNullError();
	    }
	    PhoneReturnProductResult result = mallOrderReturnService.getReturnProduct( order, detail );

	    List< PhoneReturnWayResult > returnWayList = mallOrderReturnService.getReturnWayList( order );
	    result.setReturnWayList( returnWayList );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "查询退款方式的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询退款方式的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询退款方式失败" );
	}
    }

    @ApiOperation( value = "查询退款信息接口", notes = "查询退款信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "orderDetailId", value = "订单详情id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "returnId", value = "退款id,必传", paramType = "query", dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "getReturn", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneReturnProductResult > getReturn( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer orderDetailId, Integer returnId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    PhoneReturnProductResult result = mallOrderReturnService.getReturn( orderDetailId, returnId );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "查询退款信息的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询退款信息的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询退款信息失败" );
	}
    }

    @ApiOperation( value = "查询退款详情接口", notes = "查询退款详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "returnId", value = "退款id,必传", paramType = "query", dataType = "int", required = true ) } )
    @ResponseBody
    @PostMapping( value = "returnDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneReturnResult > returnDetail( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer returnId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    PhoneReturnResult result = mallOrderReturnService.returnDetail( returnId );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
	} catch ( BusinessException e ) {
	    logger.error( "查询退款详情的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询退款详情的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询退款详情失败" );
	}
    }

    @ApiOperation( value = "查询退款日志列表接口", notes = "查询退款日志列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "returnId", value = "退款id,必传", paramType = "query", dataType = "int", required = true ) } )
    @ResponseBody
    @PostMapping( value = "returnLogList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse returnLogList( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer returnId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    List< Map< String,Object > > resultList = mallOrderReturnLogService.selectReturnLogList( returnId );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resultList, false );
	} catch ( BusinessException e ) {
	    logger.error( "查询退款日志列表接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询退款日志列表的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询退款日志列表失败" );
	}
    }

    @ApiOperation( value = "保存退款内容接口", notes = "保存退款内容", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "saveReturnContent", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse saveReturnContent( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    @RequestBody @Valid @ModelAttribute PhoneSubmitOrderReturnDTO orderReturnDTO ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    MallOrderReturn orderReturn = new MallOrderReturn();
	    EntityDtoConverter converter = new EntityDtoConverter();
	    converter.entityConvertDto( orderReturnDTO, orderReturn );

	    mallOrderReturnService.addOrderReturn( orderReturn, member );

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "保存退款内容的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存退款内容接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "保存退款失败" );
	}
    }

    @ApiOperation( value = "查询物流公司接口", notes = "查询物流公司", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "getReturnLogistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< List< DictBean > > getReturnLogistics( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断
	    List< DictBean > dictBeanList = dictService.getDict( "1092" );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), dictBeanList, false );
	} catch ( BusinessException e ) {
	    logger.error( "查询物流公司的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询物流公司的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询物流公司失败" );
	}
    }

    @ApiOperation( value = "获取退货物流接口", notes = "获取退货物流", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "returnId", value = "退款id,必传", paramType = "query", dataType = "int", required = true ) } )
    @ResponseBody
    @PostMapping( value = "getReturnLogisticsDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneReturnWuLiuResult > getReturnLogisticsDetail( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer returnId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    PhoneReturnWuLiuResult returnWuLiuResult = mallOrderReturnService.getReturnWuLiu( returnId );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), returnWuLiuResult );
	} catch ( BusinessException e ) {
	    logger.error( "获取退货物流接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "获取退货物流接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取退货物流失败" );
	}
    }

    @ApiOperation( value = "保存退货物流接口", notes = "保存退货物流", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "saveReturnLogistics", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse saveReturnLogistics( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    @RequestBody @Valid @ModelAttribute PhoneSubmitOrderReturnLogisticsDTO orderReturnDTO ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    MallOrderReturn orderReturn = new MallOrderReturn();
	    EntityDtoConverter converter = new EntityDtoConverter();
	    converter.entityConvertDto( orderReturnDTO, orderReturn );

	    mallOrderReturnService.addOrderReturn( orderReturn, member );

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "保存退货物流的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存退货物流接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "保存退货物流失败" );
	}
    }

    @ApiOperation( value = "撤销申请退款接口", notes = "撤销申请退款", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "returnId", value = "退款id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "closeReturnOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse closeReturnOrder( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer returnId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    MallOrderReturn orderReturn = mallOrderReturnService.selectById( returnId );
	    orderReturn.setStatus( -2 );
	    mallOrderService.updateOrderReturn( orderReturn, null, null );
	    //添加退款日志记录
	    mallOrderReturnLogService.buyerRevokeRefund( orderReturn.getId(), member.getId() );
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "撤销申请退款异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "撤销申请退款异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "撤销申请退款失败" );
	}
    }

    @ApiOperation( value = "确认收货接口", notes = "确认收货", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "orderId", value = "订单id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "confirmReceipt", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse confirmReceipt( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer orderId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderId", orderId );
	    params.put( "status", 4 );
	    mallOrderService.upOrderNoOrRemark( params );

	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "确认收货异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "确认收货异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "确认收货失败" );
	}
    }

    @ApiOperation( value = "删除订单接口", notes = "删除订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "orderId", value = "订单id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "deleteOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse deleteOrder( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer orderId ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    MallOrder order = mallOrderService.selectById( orderId );
	    int isCanDelete = OrderUtil.getOrderIsShowMemberDeleteButton( order );//判断是否能删除订单 1 能
	    if ( isCanDelete != 1 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除订单失败" );
	    }
	    MallOrder mallOrder = new MallOrder();
	    mallOrder.setId( orderId );
	    mallOrder.setMemberIsDelete( 1 );
	    boolean isUp = mallOrderService.updateById( mallOrder );
	    if ( !isUp ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除订单失败" );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "删除订单异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除订单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "删除订单失败" );
	}
    }

    @ApiOperation( value = "查询代付订单的接口", notes = "查询代付订单", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "orderId", value = "订单id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "getDaiFu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneGetDaiFuResult > getDaiFu( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer orderId ) {
	try {
	    /*loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断*/

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    PhoneGetDaiFuResult result = mallDaifuService.getDaifuResult( orderId, member, loginDTO.getBrowerType() );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "查询代付订单异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查询代付订单异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询代付订单失败" );
	}
    }

    @ApiOperation( value = "好友代付的接口", notes = "好友代付", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "orderId", value = "订单id,必传", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "dfPayWay", value = "支付方式", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "freindDaifu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< String > freindDaifu( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer orderId, Integer dfPayWay ) {
	try {
	    //	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    MallDaifu daifu = new MallDaifu();
	    daifu.setDfUserId( member.getId() );
	    daifu.setOrderId( orderId );
	    daifu.setDfPayWay( dfPayWay );
	    Map< String,Object > result = mallOrderService.addMallDaifu( daifu );
	    if ( CommonUtil.isNotEmpty( result ) && result.containsKey( "url" ) ) {
		return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result.get( "url" ).toString(), false );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "好友代付异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "好友代付异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "好友代付失败" );
	}
    }

    @ApiOperation( value = "订单退款回调", notes = "订单退款回调" )
    @ResponseBody
    @PostMapping( value = "/agreanOrderReturn" )
    public ServerResponse agreanOrderReturn( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    mallOrderService.agreanOrderReturn( params );
	} catch ( Exception e ) {
	    logger.error( "订单退款回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "订单退款回调异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    /**
     * 代付支付成功的回调
     */
    @ApiOperation( value = "代付支付成功回调的接口", notes = "代付支付成功回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, hidden = true )
    @ApiModelProperty( hidden = true )
    @ResponseBody
    @PostMapping( value = "daifuSuccess" )
    public ServerResponse daifuSuccess( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    if ( CommonUtil.isEmpty( params.get( "out_trade_no" ) ) && CommonUtil.isNotEmpty( params.get( "no" ) ) ) {
		params.put( "out_trade_no", params.get( "no" ) );
	    }
	    mallOrderService.paySuccessDaifu( params );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
	} catch ( BusinessException e ) {
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "代付支付成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "代付支付成功回调异常" );
	}
    }

    /**
     * 流量充值成功回调
     */
    @ApiOperation( value = "流量充值成功回调的接口", notes = "流量充值成功回调", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, hidden = true )
    @ApiModelProperty( hidden = true )
    @ResponseBody
    @PostMapping( value = "flowSuccess" )
    public ServerResponse flowSuccess( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > params ) {
	try {
	    if ( CommonUtil.isEmpty( params.get( "id" ) ) && CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
		return ErrorInfo.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	    }
	    quartzOrderService.rollbackOrderByFlow( params );
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
	} catch ( BusinessException e ) {
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "流量充值成功回调异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ErrorInfo.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "流量充值成功回调异常" );
	}
    }

}
