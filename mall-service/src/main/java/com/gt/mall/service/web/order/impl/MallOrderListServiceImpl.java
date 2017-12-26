package com.gt.mall.service.web.order.impl;

import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.order.PhoneOrderListDTO;
import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;
import com.gt.mall.result.phone.order.PhoneToOrderBusResult;
import com.gt.mall.result.phone.order.detail.PhoneOrderDetailResult;
import com.gt.mall.result.phone.order.detail.PhoneOrderResult;
import com.gt.mall.result.phone.order.list.PhoneOrderListOrderDetailResult;
import com.gt.mall.result.phone.order.list.PhoneOrderListOrderResult;
import com.gt.mall.result.phone.order.list.PhoneOrderListResult;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallOrderListService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 商城订单列表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderListServiceImpl extends BaseServiceImpl< MallOrderDAO,MallOrder > implements MallOrderListService {

    private static Logger logger = LoggerFactory.getLogger( MallOrderListServiceImpl.class );

    @Autowired
    private MallOrderDAO         mallOrderDAO;
    @Autowired
    private MallOrderDetailDAO   mallOrderDetailDAO;
    @Autowired
    private MallStoreService     mallStoreService;
    @Autowired
    private MallOrderService     mallOrderService;
    @Autowired
    private MallCommonService    mallCommonService;
    @Autowired
    private MallPaySetService    mallPaySetService;
    @Autowired
    private MallGroupBuyService  mallGroupBuyService;
    @Autowired
    private MallOrderReturnDAO   mallOrderReturnDAO;
    @Autowired
    private MallTakeTheirService mallTakeTheirService;
    @Autowired
    private MallGroupJoinDAO     mallGroupJoinDAO;

    @Override
    public PhoneOrderListResult orderList( PhoneOrderListDTO params, PhoneLoginDTO loginDTO, Member member ) {
	long startTime = System.currentTimeMillis();
	PhoneOrderListResult result = new PhoneOrderListResult();

	if ( CommonUtil.isNotEmpty( params.getOrderId() ) && params.getOrderId() > 0 ) {
	    String key = Constants.REDIS_KEY + "card_receive_num";
	    boolean isCard = JedisUtil.hExists( key, params.getOrderId().toString() );
	    //购买了卡券包
	    if ( isCard ) {
		String ids = JedisUtil.maoget( key, params.getOrderId().toString() );
		result.setCardReceiveId( CommonUtil.toInteger( ids ) );
		JedisUtil.hdel( key, params.getOrderId().toString() );
	    }
	}
	Map< String,Object > paramsMap = new HashMap<>();
	paramsMap.put( "busUserId", loginDTO.getBusId() );
	paramsMap = mallOrderService.getMemberParams( member, paramsMap );//获取登录人的集合
	paramsMap = OrderUtil.getOrderListParams( paramsMap, params );//重组查询参数
	int pageSize = 20;
	int countOrder = mallOrderDAO.countMobileOrderList( paramsMap );
	if ( countOrder == 0 ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "您还没有相关的订单" );
	}
	Integer curPage = CommonUtil.isEmpty( params.getCurPage() ) ? 1 : params.getCurPage();
	paramsMap.put( "curPage", curPage );

	PageUtil page = new PageUtil( curPage, pageSize, countOrder, "" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	paramsMap.put( "firstNum", firstNum );// 起始页
	paramsMap.put( "maxNum", pageSize );// 每页显示商品的数量
	List< MallOrder > list;
	if ( paramsMap.containsKey( "appraiseStatus" ) || paramsMap.containsKey( "isReturn" ) ) {
	    list = mallOrderDAO.mobileOrderList( paramsMap );
	} else {
	    list = mallOrderDAO.mobileMyOrderList( paramsMap );
	}
	list = mallOrderService.getOrderListParams( list );
	if ( list == null || list.size() == 0 ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "您还没有相关的订单" );
	}

	List< Integer > shopIds = new ArrayList<>();
	List< Integer > busIds = new ArrayList<>();
	for ( MallOrder order : list ) {
	    if ( CommonUtil.isNotEmpty( order.getShopId() ) ) {
		if ( !shopIds.contains( order.getShopId() ) ) {
		    shopIds.add( order.getShopId() );
		}
	    }
	    if ( CommonUtil.isNotEmpty( order.getBusUserId() ) ) {
		if ( !busIds.contains( order.getBusUserId() ) ) {
		    busIds.add( order.getBusUserId() );
		}
	    }
	}
	//通过商家集合查询商城设置
	List< MallPaySet > mallPaySetList = mallPaySetService.selectByUserIdList( busIds );
	//返回给页面的商家对象
	PhoneToOrderBusResult busResult = mallCommonService.getBusUserNameOrImage( loginDTO.getBusId() );

	//查询店铺信息
	List< Map< String,Object > > mallShopList = mallStoreService.findShopByUserIdAndShops( loginDTO.getBusId(), shopIds );

	List< PhoneOrderListOrderResult > orderResultList = new ArrayList<>();
	for ( MallOrder order : list ) {
	    PhoneOrderListOrderResult orderResult = new PhoneOrderListOrderResult();
	    orderResult.setBusId( order.getBusUserId() );//商家id
	    if ( CommonUtil.isNotEmpty( busResult ) ) {
		orderResult.setBusName( busResult.getBusName() );//商家名称
		orderResult.setBusImageUrl( busResult.getBusImageUrl() );//商家头像
	    }
	    orderResult.setShopId( order.getId() );//店铺id
	    if ( mallShopList != null && mallShopList.size() > 0 ) {
		for ( Map< String,Object > shopMap : mallShopList ) {
		    if ( shopMap.get( "id" ).toString().equals( order.getShopId().toString() ) ) {
			orderResult.setShopName( shopMap.get( "sto_name" ).toString() );//店铺名称
			orderResult.setShopImageUrl( shopMap.get( "stoPhone" ).toString() );//店铺图片
			break;
		    }
		}
	    }
	    String orderStatus = order.getOrderStatus().toString();
	    boolean isOpenComment = OrderUtil.getIsOpenComment( mallPaySetList, order.getBusUserId() );//商家是否开启了评论
	    orderResult.setOrderId( order.getId() );//订单id
	    orderResult.setOrderNo( order.getOrderNo() );//订单号
	    orderResult.setOrderStatus( order.getOrderStatus() );//订单状态
	    orderResult.setOrderStatusName( OrderUtil.getOrderStatusMsgByOrder( orderStatus, order.getDeliveryMethod().toString() ) );//订单状态名称
	    orderResult.setOrderCreateTime( DateTimeKit.format( order.getCreateTime(), DateTimeKit.DEFAULT_DATETIME_FORMAT_2 ) );//下单时间
	    long updateDay = 0;//计算修改时间到今天的天数
	    if ( CommonUtil.isNotEmpty( order.getUpdateTime() ) ) {
		updateDay = DateTimeKit.diffDays( new Date(), order.getUpdateTime() );
	    }
	    boolean isNowReturn = false;//定义订单是否退款  false 没退款
	    int orderNum = 0;
	    List< PhoneOrderListOrderDetailResult > detailResultList = new ArrayList<>();
	    if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
		    PhoneOrderListOrderDetailResult detailResult = new PhoneOrderListOrderDetailResult();
		    if ( CommonUtil.isEmpty( detail.getId() ) ) {
			continue;
		    }
		    String detailStutas = detail.getStatus().toString();
		    detailResult.setOrderDetailId( detail.getId() );//订单详情id
		    detailResult.setShopId( detail.getShopId() );//店铺id
		    detailResult.setProductId( detail.getProductId() );//商品id
		    detailResult.setProductName( detail.getDetProName() );//商品名称
		    detailResult.setProductImageUrl( detail.getProductImageUrl() );
		    detailResult.setProductPrice( CommonUtil.toDouble( detail.getDetProPrice() ) );
		    detailResult.setProductNum( detail.getDetProNum() );
		    detailResult.setProductSpecificaValue( detail.getProductSpeciname() );
		    detailResult.setDetailStauts( detail.getStatus() );//订单状态
		    isNowReturn = OrderUtil.getOrderIsNowReturn( detailStutas );
		    boolean isGoupOrderCanReturn = true;
		    if ( order.getOrderType() == 1 ) {
			//团购订单是否能退款
			isGoupOrderCanReturn = mallGroupBuyService.orderIsCanRenturn( order.getId(), detail.getId(), order.getGroupBuyId() );
			if ( !isGoupOrderCanReturn ) {
			    //团购订单不能退款
			    isNowReturn = false;
			}
		    }
		    if ( detail.getOrderReturn() != null && CommonUtil.isNotEmpty( detail.getOrderReturn().getId() ) ) {
			MallOrderReturn orderReturn = detail.getOrderReturn();
			detailResult.setReturnId( orderReturn.getId() );
			if ( CommonUtil.isNotEmpty( orderReturn.getRetMoney() ) ) {
			    detailResult.setReturnMoney( CommonUtil.toDouble( orderReturn.getRetMoney() ) );
			}
			detailResult.setReturnType( orderReturn.getRetHandlingWay() );
			if ( orderReturn.getRetHandlingWay() == 1 ) {
			    detailResult.setReturnTypeDesc( "仅退款" );
			} else {
			    detailResult.setReturnTypeDesc( "退货并退款" );
			}
			detailResult.setReturnStatusDesc( OrderUtil.getReturnStatusName( orderReturn ) );
			detailResult.setIsShowReturnWuliuButton( OrderUtil.getOrderIsShowReturnWuliuButton( isNowReturn, detailStutas, orderReturn ) );
			detailResult.setIsShowUpdateReturnButton( OrderUtil.getOrderIsShowUpdateReturnButton( isNowReturn, detailStutas ) );
			detailResult.setIsShowCloseReturnButton( OrderUtil.getOrderIsShowCloseReturnButton( isNowReturn, detailStutas, orderReturn ) );
			detailResult.setIsShowApplySaleButton( OrderUtil.getOrderIsShowShouHuoButton( order, isNowReturn ) );
			detailResult.setIsShowKanJinduButton( OrderUtil.getOrderIsShowKanJinduButton( orderReturn ) );
			detailResult.setIsShowReturnDetailButton( OrderUtil.getOrderIsShowReturnDetailButton( orderReturn ) );

		    }
		    if ( isGoupOrderCanReturn ) {
			//判断订单详情是否能显示申请退款按钮
			detailResult.setIsShowApplyReturnButton( OrderUtil.getOrderIsShowReturnButton( order, detail, updateDay ) );
		    }
		    //判断订单是否能显示评论按钮  1 能评论
		    detailResult.setIsShowCommentButton( OrderUtil.getOrderIsShowCommentButton( orderStatus, isNowReturn, isOpenComment, detail ) );
		    detailResultList.add( detailResult );
		    orderNum += detail.getDetProNum();
		}
	    } else {
		orderNum = 1;
	    }
	    orderResult.setTotalNum( orderNum );
	    orderResult.setOrderMoney( CommonUtil.toDouble( order.getOrderMoney() ) );
	    //判断是否显示去支付按钮  （未支付 且 不是找人代付才能 显示去支付按钮）
	    orderResult.setIsShowGoPayButton( OrderUtil.getOrderIsShowGoPayButton( order ) );
	    //判断是否显示确认收货按钮 (订单状态为已发货、不能是积分支付和粉币支付  以及 未退款的订单或退款成功的订单   才能显示确认收货的按钮)
	    orderResult.setIsShowReceiveGoodButton( OrderUtil.getOrderIsShowShouHuoButton( order, isNowReturn ) );
	    //判断是否显示代付详情按钮
	    orderResult.setIsShowDaifuButton( OrderUtil.getOrderIsShowDaifuButton( order ) );
	    orderResult.setDetailResultList( detailResultList );
	    orderResult.setOrderType( order.getOrderType() );
	    orderResult.setActivityId( order.getGroupBuyId() );
	    orderResult.setOrderPayWay( order.getOrderPayWay() );
	    if ( detailResultList.size() > 0 || ( detailResultList.size() == 0 && order.getOrderPayWay() == 5 ) ) {
		orderResultList.add( orderResult );
	    }
	}
	result.setOrderResultList( orderResultList );
	result.setPageCount( page.getPageCount() );
	result.setCurPage( page.getCurPage() );
	long endTime = System.currentTimeMillis();

	long executeTime = endTime - startTime;
	logger.error( "订单列表访问的执行时间 : " + executeTime + "ms" );
	return result;
    }

    @Override
    public PhoneOrderResult getOrderDetail( Integer orderId, Integer busId ) {
	PhoneOrderResult result = new PhoneOrderResult();

	MallOrder order = mallOrderDAO.selectById( orderId );
	if ( CommonUtil.isEmpty( order ) ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	}
	if ( order.getMemberIsDelete() == 1 ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "订单已删除" );
	}
	List< Integer > busIds = new ArrayList<>();
	busIds.add( order.getBusUserId() );
	//通过商家集合查询商城设置
	List< MallPaySet > mallPaySetList = mallPaySetService.selectByUserIdList( busIds );
	//商家是否开启了评论 true 开启了
	boolean isOpenComment = OrderUtil.getIsOpenComment( mallPaySetList, order.getBusUserId() );
	String orderStatus = order.getOrderStatus().toString();
	String deliveryMethod = order.getDeliveryMethod().toString();
	long updateDay = 0;//计算修改时间到今天的天数
	if ( CommonUtil.isNotEmpty( order.getUpdateTime() ) ) {
	    updateDay = DateTimeKit.diffDays( new Date(), order.getUpdateTime() );
	}
	boolean isNowReturn = false;//定义订单是否退款  false 没退款
	int orderNum = 0;
	double orderYouhuiMoney = 0;//订单优惠金额
	List< PhoneOrderDetailResult > detailResultList = new ArrayList<>();
	List< MallOrderDetail > detailList = mallOrderDetailDAO.selectByOrderId( orderId );
	Integer proTypeId = 0;//商品类型 0 实物  大于0 虚拟物品
	if ( detailList != null && detailList.size() > 0 ) {
	    for ( MallOrderDetail detail : detailList ) {
		String detailStutas = detail.getStatus().toString();
		PhoneOrderDetailResult detailResult = new PhoneOrderDetailResult();
		detailResult.setOrderDetailId( detail.getId() );
		detailResult.setShopId( detail.getShopId() );
		detailResult.setProductId( detail.getProductId() );
		detailResult.setProductName( detail.getDetProName() );
		detailResult.setProductImageUrl( detail.getProductImageUrl() );
		detailResult.setProductNum( detail.getDetProNum() );
		detailResult.setProductPrice( CommonUtil.toDouble( detail.getDetProPrice() ) );
		detailResult.setProductSpecificaValue( detail.getProductSpeciname() );
		detailResult.setDetailStauts( detail.getStatus() );
		detailResult.setSaleMemberId( detail.getSaleMemberId() );
		isNowReturn = OrderUtil.getOrderIsNowReturn( detailStutas );
		boolean isGoupOrderCanReturn = true;
		if ( order.getOrderType() == 1 ) {
		    //团购订单是否能退款
		    isGoupOrderCanReturn = mallGroupBuyService.orderIsCanRenturn( order.getId(), detail.getId(), order.getGroupBuyId() );
		    if ( !isGoupOrderCanReturn ) {
			//团购订单不能退款
			isNowReturn = false;
		    }
		}
		MallOrderReturn mallOrderReturn = null;
		if ( !"-3".equals( detailStutas ) ) {
		    MallOrderReturn orderReturn = new MallOrderReturn();
		    orderReturn.setOrderId( order.getId() );
		    orderReturn.setOrderDetailId( detail.getId() );
		    mallOrderReturn = mallOrderReturnDAO.selectByOrderDetailId( orderReturn );
		    if ( CommonUtil.isNotEmpty( mallOrderReturn ) ) {
			detailResult.setReturnId( mallOrderReturn.getId() );
		    }
		}
		//判断订单详情是否能显示申请退款按钮 1显示
		int isShowAppllyReturn = 0;
		if ( isGoupOrderCanReturn ) {
		    isShowAppllyReturn = OrderUtil.getOrderIsShowReturnButton( order, detail, updateDay );
		    detailResult.setIsShowApplyReturnButton( isShowAppllyReturn );
		}
		//判断订单是否能显示评论按钮  1 能评论
		detailResult.setIsShowCommentButton( OrderUtil.getOrderIsShowCommentButton( orderStatus, isNowReturn, isOpenComment, detail ) );
		//是否显示退款物流的按钮 1显示
		detailResult.setIsShowReturnWuLiuButton( OrderUtil.getOrderIsShowReturnWuliuButton( isNowReturn, detailStutas, mallOrderReturn ) );
		//是否显示修改退款的按钮 1显示
		detailResult.setIsShowUpdateReturnButton( OrderUtil.getOrderIsShowUpdateReturnButton( isNowReturn, detailStutas ) );
		//是否显示撤销退款的按钮 1显示
		detailResult.setIsShowCloseReturnButton( OrderUtil.getOrderIsShowCloseReturnButton( isNowReturn, detailStutas, mallOrderReturn ) );
		//是否显示申请售后的按钮 1显示
		if ( isShowAppllyReturn == 1 && "4".equals( orderStatus ) ) {
		    //显示申请售后的按钮
		    detailResult.setIsShowApplySaleButton( 1 );
		    //不显示申请退款的按钮
		    detailResult.setIsShowApplyReturnButton( 0 );
		}
		result.setOrderStatusName( OrderUtil.getOrderStatusMsgBydetail( orderStatus, detailStutas, deliveryMethod, mallOrderReturn ) );
		detailResultList.add( detailResult );
		orderNum += detail.getDetProNum();
		if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
		    orderYouhuiMoney = CommonUtil.add( orderYouhuiMoney, CommonUtil.toDouble( detail.getDiscountedPrices() ) );
		}
		proTypeId = detail.getProTypeId();
	    }
	}
	//返回给页面的商家对象
	PhoneToOrderBusResult busResult = mallCommonService.getBusUserNameOrImage( order.getBusUserId() );
	if ( CommonUtil.isNotEmpty( busResult ) ) {
	    result.setBusId( order.getBusUserId() );
	    result.setBusName( busResult.getBusName() );
	    result.setBusImageUrl( busResult.getBusImageUrl() );
	}
	if ( CommonUtil.isNotEmpty( order.getShopId() ) ) {
	    //查询店铺信息
	    Map< String,Object > shopMap = mallStoreService.findShopByStoreId( order.getShopId() );
	    if ( CommonUtil.isNotEmpty( shopMap ) ) {
		result.setShopId( order.getShopId() );
		result.setShopName( CommonUtil.toString( shopMap.get( "stoName" ) ) );
		result.setShopImageUrl( CommonUtil.toString( shopMap.get( "stoPicture" ) ) );
	    }
	}
	result.setOrderId( order.getId() );
	result.setOrderNo( order.getOrderNo() );
	result.setOrderStatus( order.getOrderStatus() );
	result.setOrderStatusMsg( OrderUtil.getOrderStatusCountdownMsg( order ) );
	result.setTotalNum( orderNum );
	double orderMoney = CommonUtil.toDouble( order.getOrderMoney() );
	result.setOrderMoney( orderMoney );
	double freightMoney = 0;
	if ( CommonUtil.isNotEmpty( order.getOrderFreightMoney() ) ) {
	    freightMoney = CommonUtil.toDouble( order.getOrderFreightMoney() );
	}
	result.setProductTotalMoney( CommonUtil.subtract( orderMoney, freightMoney ) );
	result.setOrderFreightMoney( freightMoney );
	result.setOrderYouhuiMoney( orderYouhuiMoney );
	result.setOrderFenbiYouhuiMoney( order.getFenbiDiscountMoney() );
	result.setOrderJifenYouhuiMoney( order.getJifenDiscountMoney() );

	if ( "1".equals( order.getDeliveryMethod().toString() ) && ( CommonUtil.isEmpty( proTypeId ) || proTypeId == 0 ) && CommonUtil
			.isNotEmpty( order.getReceiveName() ) ) {//1 快递配送
	    PhoneOrderMemberAddressDTO memberAddressDTO = new PhoneOrderMemberAddressDTO();
	    memberAddressDTO.setId( order.getReceiveId() );
	    memberAddressDTO.setMemberName( order.getReceiveName() );
	    memberAddressDTO.setMemberPhone( order.getReceivePhone() );
	    memberAddressDTO.setMemberAddress( order.getReceiveAddress() );
	    result.setMemberAddressDTO( memberAddressDTO );
	} else if ( "2".equals( order.getDeliveryMethod().toString() ) && CommonUtil.isNotEmpty( order.getTakeTheirId() ) ) {
	    //上门自提
	    result.setAppointmentId( order.getTakeTheirId() );
	    result.setAppointmentUserName( order.getAppointmentName() );
	    result.setAppointmentUserPhone( order.getAppointmentTelephone() );
	    result.setAppointmentDate( DateTimeKit.format( order.getAppointmentTime(), DateTimeKit.DEFAULT_DATE_FORMAT ) );
	    result.setAppointmentStartTime( order.getAppointmentStartTime() );
	    result.setAppointmentEndTime( order.getAppointmentEndTime() );
	    if ( CommonUtil.isEmpty( order.getAppointmentAddress() ) ) {
		MallTakeTheir take = mallTakeTheirService.selectById( order.getTakeTheirId() );
		if ( CommonUtil.isNotEmpty( take ) ) {
		    order.setAppointmentAddress( take.getVisitAddressDetail() );
		}
	    }
	    result.setAppointmentAddress( order.getAppointmentAddress() );
	}
	//判断是否显示去支付按钮  （未支付 且 不是找人代付才能 显示去支付按钮）
	result.setIsShowGoPayButton( OrderUtil.getOrderIsShowGoPayButton( order ) );
	//判断是否显示确认收货按钮 (订单状态为已发货、不能是积分支付和粉币支付  以及 未退款的订单或退款成功的订单   才能显示确认收货的按钮)
	result.setIsShowReceiveGoodButton( OrderUtil.getOrderIsShowShouHuoButton( order, isNowReturn ) );
	//是否显示删除订单按钮 1显示
	result.setIsShowDeleteButton( OrderUtil.getOrderIsShowMemberDeleteButton( order ) );
	//是否查看物流按钮 1显示
	result.setIsShowKanWuLiuButton( OrderUtil.getOrderIsShowKanWuliuButton( order ) );
	result.setDetailResultList( detailResultList );
	result.setBuyerMessage( order.getOrderBuyerMessage() );
	result.setBusMessage( order.getOrderSellerRemark() );
	result.setOrderType( order.getOrderType() );
	result.setActivityId( order.getGroupBuyId() );
	result.setBuyerUserId( order.getBuyerUserId() );
	result.setJoinId( order.getPJoinId() );
	if ( CommonUtil.isNotEmpty( order.getOrderType() ) && order.getOrderType() == 1 && CommonUtil.isNotEmpty( order.getGroupBuyId() ) && order.getGroupBuyId() > 0 ) {
	    MallGroupBuy mallGroupBuy = mallGroupBuyService.selectById( order.getGroupBuyId() );

	    //查询参团情况
	    Map< String,Object > groupMap = new HashMap<>();
	    groupMap.put( "groupBuyId", order.getGroupBuyId() );
	    groupMap.put( "orderId", order.getId() );
	    Map< String,Object > countMap = mallGroupJoinDAO.groupJoinPeopleNum( groupMap );
	    if ( CommonUtil.isNotEmpty( countMap ) && CommonUtil.isNotEmpty( countMap.get( "pId" ) ) ) {
		result.setJoinId( CommonUtil.toInteger( countMap.get( "pId" ) ) );
		result.setJoinNum( CommonUtil.toInteger( countMap.get( "num" ) ) );
		if ( result.getJoinNum() < mallGroupBuy.getGPeopleNum() ) {
		    result.setOrderStatusName( "待成团," + result.getOrderStatusName() );
		    result.setOrderStatusMsg( OrderUtil.getOrderStatusByGroup( mallGroupBuy, result.getJoinNum() ) );
		} else {
		    result.setOrderStatusName( "已成团," + result.getOrderStatusName() );
		}
	    }
	}
	result.setOrderPayWay( order.getOrderPayWay() );
	return result;
    }

}
