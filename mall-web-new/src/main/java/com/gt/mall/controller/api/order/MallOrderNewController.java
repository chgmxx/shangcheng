package com.gt.mall.controller.api.order;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallSecuritytradeQuit;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.OrderDTO;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallDaifuService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private MallOrderService       mallOrderService;
    @Autowired
    private MallStoreService       mallStoreService;
    @Autowired
    private MallGroupBuyService    mallGroupBuyService;
    @Autowired
    private MallOrderDAO           mallOrderDAO;
    @Autowired
    private MallDaifuService       mallDaifuService;
    @Autowired
    private DictService            dictService;
    @Autowired
    private BusUserService         busUserService;
    @Autowired
    private MallOrderReturnService mallOrderReturnService;

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
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, OrderDTO orderQuery ) {
	Map< String,Object > result = new HashMap<>();
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
	    BusUser user = SessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    if ( CommonUtil.isEmpty( orderQuery.getShopId() ) ) {
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
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "orderId", id );
	    Map< String,Object > orderList = mallOrderService.selectOrderList( params );
	    result.putAll( orderList );

	    Wrapper< MallOrderReturn > returnWrapper = new EntityWrapper<>();
	    returnWrapper.where( "order_id ={0}", id );
	    MallOrderReturn oReturn = mallOrderReturnService.selectOne( returnWrapper );
	    result.put( "orderReturn", oReturn );

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
    @RequestMapping( value = "/typeMap", method = RequestMethod.POST )
    public ServerResponse typeMap( HttpServletRequest request, HttpServletResponse response ) {
	List< Map > cancelReason = null;
	try {
	    cancelReason = dictService.getDict( "1079" );
	} catch ( Exception e ) {
	    logger.error( "获取取消订单理由列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取取消订单理由列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), cancelReason );
    }

    /**
     * 判断是否可以发货 （团购商品）
     */
    @ApiOperation( value = "判断团购商品是否可以发货", notes = "判断团购商品是否可以发货" )
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
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), flag );
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
	    WxPublicUsers pUser = SessionUtils.getLoginPbUser( request );

	    MallOrderReturn orderReturn = com.alibaba.fastjson.JSONObject.parseObject( params.get( "return" ).toString(), MallOrderReturn.class );
	    Map< String,Object > map = mallOrderService.updateOrderReturn( orderReturn, params.get( "order" ), pUser );
	    Boolean flag = (Boolean) map.get( "flag" );
	    if ( !flag ) {
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
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
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
