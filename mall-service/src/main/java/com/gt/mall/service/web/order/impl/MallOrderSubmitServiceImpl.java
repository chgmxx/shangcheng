package com.gt.mall.service.web.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.util.KeysUtil;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.bean.member.Coupons;
import com.gt.mall.bean.member.JifenAndFenbBean;
import com.gt.mall.bean.member.JifenAndFenbiRule;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheirTime;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneBuyNowDTO;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.freight.PhoneFreightProductDTO;
import com.gt.mall.param.phone.order.*;
import com.gt.mall.param.phone.order.add.PhoneAddOrderBusDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderProductDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderShopDTO;
import com.gt.mall.result.phone.order.PhoneToOrderBusResult;
import com.gt.mall.result.phone.order.PhoneToOrderProductResult;
import com.gt.mall.result.phone.order.PhoneToOrderResult;
import com.gt.mall.result.phone.order.PhoneToOrderShopResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.common.MallCalculateService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.common.MallMemberAddressService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.order.MallOrderSubmitService;
import com.gt.mall.service.web.order.MallOrderTaskService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.union.api.entity.param.UnionCardDiscountParam;
import com.gt.union.api.entity.result.UnionDiscountResult;
import com.gt.util.entity.param.pay.PayWay;
import com.gt.util.entity.param.pay.SubQrPayParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 商城提交订单 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderSubmitServiceImpl extends BaseServiceImpl< MallOrderDAO,MallOrder > implements MallOrderSubmitService {

    private static Logger logger = LoggerFactory.getLogger( MallOrderSubmitServiceImpl.class );

    @Autowired
    private MallOrderDAO              mallOrderDAO;
    @Autowired
    private MallProductService        mallProductService;
    @Autowired
    private MallOrderDetailDAO        mallOrderDetailDAO;
    @Autowired
    private MallOrderService          mallOrderService;
    @Autowired
    private MallShopCartDAO           mallShopCartDAO;
    @Autowired
    private MallAuctionBiddingService mallAuctionBiddingService;
    @Autowired
    private MemberAddressService      memberAddressService;
    @Autowired
    private MemberService             memberService;
    @Autowired
    private MallStoreService          mallStoreService;
    @Autowired
    private MallFreightService        mallFreightService;
    @Autowired
    private MallPageService           mallPageService;
    @Autowired
    private MallCommonService         mallCommonService;
    @Autowired
    private MallImageAssociativeDAO   mallImageAssociativeDAO;
    @Autowired
    private MallPifaService           mallPifaService;
    @Autowired
    private MallPaySetService         mallPaySetService;
    @Autowired
    private MallTakeTheirService      mallTakeTheirService;
    @Autowired
    private MallCalculateService      mallCalculateService;
    @Autowired
    private MallMemberAddressService  mallMemberAddressService;
    @Autowired
    private UnionCardService          UnionCardService;
    @Autowired
    private PayService                payService;
    @Autowired
    private MallOrderTaskService      mallOrderTaskService;

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > submitOrder( PhoneAddOrderDTO params, Member member, Integer browser ) throws Exception {
	Map< String,Object > result = new HashMap<>();

	if ( CommonUtil.isEmpty( params ) ) {
	    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
	}

	logger.info( "提交订单参数：" + JSONArray.toJSON( params ) );

	//判断参数传值是否完整
	params = mallCommonService.isOrderParams( params, member );

	if ( params.isCalculation() && ( CommonUtil.isEmpty( params.getOrderType() ) || params.getOrderType() <= 0 ) ) {
	    //会员计算
	    params = mallCalculateService.calculateOrder( params, member );
	    logger.info( "计算后的参数：" + JSONArray.toJSON( params ) );
	}
	if ( CommonUtil.isEmpty( browser ) || browser == 99 ) {
	    browser = 0;
	}

	List< MallOrder > orderList = new ArrayList<>();
	MallOrder parentOrder = null;
	MallOrder firstOrder = new MallOrder();
	double totalOrderMoney = 0;//订单的实付价格(包含运费)
	double totalOrderFreightMoney = 0;//订单的总运费
	double totalOrderOldMoney = 0;//订单应付价格（不包含运费）
	double totalUseFenbiNum = 0;//总共使用粉币的数量
	double totalUseJifenNum = 0;//总共使用积分的数量
	double totalYouhuiMoney = 0;//总订单优惠的金额
	double totalFenbiMoney = 0;//粉币优惠金额
	double totalJifenMoney = 0;//积分优惠的金额
	//组装需要保存的参数 并保存订单
	for ( PhoneAddOrderBusDTO busDTO : params.getBusResultList() ) {//循环商家集合
	    for ( PhoneAddOrderShopDTO shopDTO : busDTO.getShopResultList() ) {//循环店铺集合
		MallOrder order = mallCommonService.getOrderParams( shopDTO, busDTO, params, member );
		List< MallOrderDetail > detailList = new ArrayList<>();
		for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		    MallOrderDetail detail = mallCommonService.getOrderDetailParams( productDTO, params, busDTO.getMemberDiscount(), shopDTO.getSelectCouponsId() );
		    if ( CommonUtil.isNotEmpty( productDTO.getProduct() ) ) {
			order.setMemCardType( productDTO.getProduct().getMemberType() );//会员卡类型
		    }
		    if ( CommonUtil.isNotEmpty( params.getOrderId() ) && params.getOrderId() > 0 ) {
			order.setId( params.getOrderId() );
			detail.setOrderId( params.getOrderId() );
		    }
		    if ( CommonUtil.isNotEmpty( productDTO.getOrderDetailId() ) && productDTO.getOrderDetailId() > 0 ) {
			detail.setId( productDTO.getOrderDetailId() );
		    }
		    order.setOrderType( productDTO.getActivityType() );//活动类型 1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品
		    order.setGroupBuyId( productDTO.getActivityId() );//活动id
		    order.setPJoinId( productDTO.getGroupJoinId() );//参团id
		    order.setBuyerUserType( browser );//数据来源 0:pc端 1:微信 2:uc端 3:小程序
		    totalUseFenbiNum = CommonUtil.add( totalUseFenbiNum, detail.getUseFenbi() );//商家下面使用粉币数量
		    totalUseJifenNum = CommonUtil.add( totalUseJifenNum, detail.getUseJifen() );//商家下面使用积分数量
		    detailList.add( detail );
		}
		if ( CommonUtil.isEmpty( parentOrder ) && busDTO.getShopResultList().size() > 1 ) {
		    parentOrder = order.clone();
		    //		    parentOrder.setShopId( null );
		}
		order.setMallOrderDetail( detailList );
		orderList.add( order );

		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, shopDTO.getTotalYouhuiMoney() );
		totalFenbiMoney = CommonUtil.add( totalFenbiMoney, shopDTO.getFenbiYouhuiMoney() );
		totalJifenMoney = CommonUtil.add( totalJifenMoney, shopDTO.getJifenYouhuiMoney() );
		totalOrderMoney = CommonUtil.add( totalOrderMoney, CommonUtil.toDouble( order.getOrderMoney() ) );
	    }
	    totalOrderFreightMoney = CommonUtil.add( totalOrderFreightMoney, busDTO.getProductFreightMoney() );
	    totalOrderOldMoney = CommonUtil.add( totalOrderOldMoney, busDTO.getTotalMoney() );

	}
	if ( params.getTotalPayMoney() != totalOrderMoney ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "订单优惠计算异常" );
	}
	if ( CommonUtil.isNotEmpty( parentOrder ) ) {
	    parentOrder.setOrderMoney( CommonUtil.toBigDecimal( totalOrderMoney ) );//订单实付金额（包含运费）
	    parentOrder.setOrderFreightMoney( CommonUtil.toBigDecimal( totalOrderFreightMoney ) );//订单总运费
	    parentOrder.setOrderOldMoney( CommonUtil.toBigDecimal( totalOrderOldMoney ) );//订单应付金额
	    parentOrder.setUseJifen( totalUseJifenNum );//使用积分的数量
	    parentOrder.setUseFenbi( totalUseFenbiNum );//使用粉币的数量
	    parentOrder.setDiscountMoney( totalYouhuiMoney );
	    parentOrder.setJifenDiscountMoney( totalJifenMoney );
	    parentOrder.setFenbiDiscountMoney( totalFenbiMoney );
	    orderList.add( 0, parentOrder );
	}
	//	if ( orderList != null ) {
	//	    //		    logger.error( JSONArray.toJSONString( orderList ) );
	//	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	//	}
	String orderPNo = "";
	int orderPId = 0;
	//新增订单和订单详情
	if ( orderList != null && orderList.size() > 0 ) {
	    for ( int i = 0; i < orderList.size(); i++ ) {
		MallOrder mallOrder = orderList.get( i );

		if ( orderPId > 0 ) {
		    mallOrder.setOrderPid( orderPId );
		}
		int count;
		if ( CommonUtil.isNotEmpty( mallOrder.getId() ) && mallOrder.getId() > 0 ) {
		    mallOrder.setUpdateTime( new Date() );
		    count = mallOrderDAO.updateById( mallOrder );
		} else {
		    mallOrder.setOrderNo( "SC" + System.currentTimeMillis() );
		    mallOrder.setCreateTime( new Date() );
		    count = mallOrderDAO.insert( mallOrder );
		}
		if ( count < 0 ) {
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "新增订单失败，请稍后重试" );
		}
		if ( i == 0 ) {
		    orderPId = mallOrder.getId();
		    orderPNo = mallOrder.getOrderNo();
		    firstOrder = mallOrder;
		}
		if ( mallOrder.getMallOrderDetail() != null && mallOrder.getMallOrderDetail().size() > 0 ) {
		    for ( MallOrderDetail orderDetail : mallOrder.getMallOrderDetail() ) {
			orderDetail.setOrderId( mallOrder.getId() );
			if ( CommonUtil.isNotEmpty( orderDetail.getId() ) && orderDetail.getId() > 0 ) {
			    count = mallOrderDetailDAO.updateById( orderDetail );
			} else {
			    orderDetail.setCreateTime( new Date() );
			    count = mallOrderDetailDAO.insert( orderDetail );
			}
			if ( count < 0 ) {
			    throw new BusinessException( ResponseEnums.ERROR.getCode(), "新增订单详情失败，请稍后重试" );
			}
		    }
		}
	    }
	}

	//订单生成成功，把订单加入到未支付的队列中（秒杀商品除外）
	if ( firstOrder.getOrderType() != 3 ) {
	    String key = Constants.REDIS_KEY + "hOrder_nopay";
	    net.sf.json.JSONObject objs = new net.sf.json.JSONObject();
	    String times = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    objs.put( "times", times );
	    MallPaySet paySet = mallPaySetService.selectByUserId( orderList.get( 0 ).getBusUserId() );
	    Integer orderCancel = 1440 * 3;
	    if ( paySet != null ) {
		if ( CommonUtil.isNotEmpty( paySet.getOrderCancel() ) ) {
		    objs.put( "orderCancel", paySet.getOrderCancel() );
		    orderCancel = paySet.getOrderCancel();
		}
	    }
	    objs.put( "orderId", orderPId );
	    JedisUtil.map( key, orderPId + "", objs.toString() );
	    //添加任务
	    mallOrderTaskService.saveOrUpdate( 1, orderPId, orderPNo, null, orderCancel );//1关闭订单
	}
	//拍卖，添加拍卖竞拍
	if ( firstOrder.getOrderType() == 4 ) {
	    //加入拍卖竞拍
	    mallAuctionBiddingService.addBidding( firstOrder, orderList.get( 0 ).getMallOrderDetail() );
	}
	//删除购物车的商品
	if ( CommonUtil.isNotEmpty( params.getShopCartIds() ) ) {
	    String[] cartIds = params.getShopCartIds().split( "," );
	    if ( cartIds != null && cartIds.length > 0 ) {
		for ( Object str : cartIds ) {
		    if ( CommonUtil.isNotEmpty( str ) ) {
			mallShopCartDAO.deleteById( CommonUtil.toInteger( str ) );
		    }
		}
	    }
	}
	//货到付款或支付金额为0的订单，直接修改订单状态为已支付，且修改商品库存和销量
	if ( totalOrderMoney == 0 || ( firstOrder.getOrderPayWay() != 1 && firstOrder.getOrderPayWay() != 9 && firstOrder.getOrderPayWay() != 11 ) ) {
	    Map< String,Object > payParams = new HashMap<>();
	    payParams.put( "status", 2 );
	    payParams.put( "out_trade_no", orderPNo );
	    mallOrderService.paySuccessModified( payParams, member );//修改库存和订单状态
	}

	int returnUrlType = 1;//跳转类型  1 跳转至订单列表 2 跳转至代付页面 3 跳转至积分商城兑换页面  4跳转至 微信或支付包支付的页面（具体会给链接）
	if ( firstOrder.getOrderPayWay() == 7 ) {
	    returnUrlType = 2;
	} else if ( firstOrder.getOrderPayWay() == 4 ) {
	    returnUrlType = 3;
	}
	if ( totalOrderMoney > 0 && ( firstOrder.getOrderPayWay() == 1 || firstOrder.getOrderPayWay() == 9 || firstOrder.getOrderPayWay() == 11 ) ) {
	    returnUrlType = 4;
	    String url = wxPayWay( totalOrderMoney, orderPNo, firstOrder, firstOrder.getOrderPayWay() );
	    result.put( "url", url );
	}

	result.put( "orderPayWay", firstOrder.getOrderPayWay() );
	result.put( "orderId", orderPId );
	result.put( "returnUrlType", returnUrlType );
	return result;
    }

    @Override
    public String wxPayWay( double orderAllMoney, String orderNo, MallOrder order, int orderPayWay ) throws Exception {
	if ( orderAllMoney == 0 ) {
	    orderAllMoney = CommonUtil.toDouble( order.getOrderMoney() );
	}
	if ( CommonUtil.isEmpty( orderNo ) ) {
	    orderNo = order.getOrderNo();
	}
	SubQrPayParams subQrPayParams = new SubQrPayParams();
	subQrPayParams.setTotalFee( orderAllMoney );//支付金额
	subQrPayParams.setModel( Constants.PAY_MODEL );//支付模块ID(字典:1200)
	subQrPayParams.setBusId( order.getBusUserId() );//商家id
	subQrPayParams.setAppidType( 0 );//appid类型(后期可能会有小程序支付),可为空默认是0：公众号支付,1:小程序支付
	    /*subQrPayParams.setAppid( 0 );*///微信支付和支付宝支付可以不传
	subQrPayParams.setOrderNum( orderNo );//订单号
	subQrPayParams.setMemberId( order.getBuyerUserId() );//会员id
	subQrPayParams.setDesc( "商城下单" );//描述
	subQrPayParams.setIsreturn( 1 );//是否需要同步回调(支付成功后页面跳转),1:需要(returnUrl比传),0:不需要(为0时returnUrl不用传)
	String returnUrl = PropertiesUtil.getPhoneWebHomeUrl() + "/order/list/" + order.getBusUserId() + "/0";
	String sucessUrl = PropertiesUtil.getHomeUrl() + "phoneOrder/L6tgXlBFeK/paySuccessModified.do";
	if ( order.getOrderPayWay() == 7 ) {
	    sucessUrl = PropertiesUtil.getHomeUrl() + "phoneOrder/L6tgXlBFeK/daifuSuccess.do";
	    returnUrl = PropertiesUtil.getPhoneWebHomeUrl() + "/daifu/" + order.getBusUserId() + "/" + order.getId();
	}
	subQrPayParams.setReturnUrl( returnUrl );//同步返回url(支付成功后页面跳转)，注：跳转链接时，支付接口会拼接busId跟memberId这两个参数传过去，因此
	subQrPayParams.setNotifyUrl( sucessUrl );//异步回调，注：1、会传out_trade_no--订单号,payType--支付类型(0:微信，1：支付宝2：多粉钱包),2接收到请求处理完成后，必须返回回调结果：code(0:成功,-1:失败),msg(处理结果,如:成功)
	subQrPayParams.setIsSendMessage( 1 );//是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传)
	subQrPayParams.setSendUrl( PropertiesUtil.getHomeUrl() + "html/back/views/order/index.html#/allOrder" );//推送路径(尽量不要带参数)
	if ( orderPayWay >= 0 ) {
	    orderPayWay = 1;
	    if ( order.getOrderPayWay() == 9 ) {
		orderPayWay = 2;
	    }
	    if ( order.getOrderPayWay() == 11 ) {//多粉钱包支付
		orderPayWay = 3;
	    }
	    /*if ( CommonUtil.isNotEmpty( order.getIsWallet() ) && order.getIsWallet() == 1 ) {
		orderPayWay = 3;
	    }*/
	}

	subQrPayParams.setPayWay( orderPayWay );//支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付
	subQrPayParams.setSourceType( Constants.PAY_SOURCE_TYPE);//墨盒默认0即啊祥不用填,其他人调用填1
	subQrPayParams.setTakeState( 2 );//此订单是否可立即提现(1:是 2:否,不填默认为1)，不可立即提现表示此订单有担保期；注：如传值为2,各erp系统需各自写定时器将超过担保期的订单发送到指定接口
	KeysUtil keyUtil = new KeysUtil();
	String params = keyUtil.getEncString( JSONObject.toJSONString( subQrPayParams ) );
	return PropertiesUtil.getWxmpDomain() + "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do?obj=" + params;
    }

    @Override
    public PhoneToOrderResult toOrder( PhoneToOrderDTO params, Member member, PhoneLoginDTO loginDTO, HttpServletRequest request ) {
	long startTime = System.currentTimeMillis();
	PhoneToOrderResult result = new PhoneToOrderResult();
	Integer provincesId = null;//省份id
	Double memberLongitude = params.getLongitude();//会员经度
	Double memberLangitude = params.getLangitude();//会员纬度
	MallOrder mallOrder = null;
	if ( CommonUtil.isNotEmpty( member ) ) {
	    if ( CommonUtil.isEmpty( member.getPhone() ) ) {
		member = memberService.findMemberById( member.getId(), member );
	    }
	    if ( CommonUtil.isNotEmpty( member.getPhone() ) ) {
		result.setMemberPhone( member.getPhone() );
	    }
	}
	if ( CommonUtil.isNotEmpty( params.getOrderId() ) && params.getOrderId() > 0 ) {
	    mallOrder = mallOrderDAO.selectById( params.getOrderId() );
	    if ( CommonUtil.isNotEmpty( mallOrder ) ) {
		List< MallOrderDetail > detailList = mallOrderDetailDAO.selectByOrderId( params.getOrderId() );
		if ( detailList != null && detailList.size() > 0 ) {
		    mallOrder.setMallOrderDetail( detailList );
		}
		params.setFrom( 3 );
		if ( mallOrder.getDeliveryMethod() == 1 && CommonUtil.isNotEmpty( mallOrder.getReceiveId() ) && mallOrder.getReceiveId() > 0 ) {
		    params.setMemberAddressId( mallOrder.getReceiveId() );
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( params.getMemberAddressId() ) && params.getMemberAddressId() > 0 ) {
	    MemberAddress memberAddress = memberAddressService.addreSelectId( params.getMemberAddressId() );
	    if ( CommonUtil.isNotEmpty( memberAddress ) ) {
		PhoneOrderMemberAddressDTO memberAddressResult = mallMemberAddressService.getMemberAddressResult( memberAddress );

		result.setMemberAddressDTO( memberAddressResult );
		provincesId = memberAddress.getMemProvince();
		if ( CommonUtil.isNotEmpty( memberAddress.getMemLongitude() ) ) {
		    memberLongitude = CommonUtil.toDouble( memberAddress.getMemLongitude() );
		}
		if ( CommonUtil.isNotEmpty( memberAddress.getMemLatitude() ) ) {
		    memberLangitude = CommonUtil.toDouble( memberAddress.getMemLatitude() );
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isEmpty( result.getMemberAddressDTO() ) ) {
	    if ( CommonUtil.isNotEmpty( member ) ) {
		List< Integer > memberList = memberService.findMemberListByIds( member.getId() );
		//获取会员的默认地址
		Map addressMap = memberAddressService.addressDefault( CommonUtil.getMememberIds( memberList, member.getId() ) );
		PhoneOrderMemberAddressDTO memberAddress = mallMemberAddressService.getMemberAddressResult( addressMap );
		if ( CommonUtil.isNotEmpty( memberAddress ) ) {
		    result.setMemberAddressDTO( memberAddress );
		    provincesId = CommonUtil.toInteger( addressMap.get( "memProvince" ) );
		    if ( CommonUtil.isNotEmpty( addressMap.get( "memLongitude" ) ) ) {
			memberLongitude = CommonUtil.toDouble( addressMap.get( "memLongitude" ) );
		    }
		    if ( CommonUtil.isNotEmpty( addressMap.get( "memLatitude" ) ) ) {
			memberLangitude = CommonUtil.toDouble( addressMap.get( "memLatitude" ) );
		    }
		}
	    }
	}
	if ( CommonUtil.isEmpty( provincesId ) && CommonUtil.isNotEmpty( params.getIp() ) ) {
	    String province = mallPageService.getProvince( params.getIp() );
	    if ( CommonUtil.isNotEmpty( province ) ) {
		provincesId = CommonUtil.toInteger( province );
	    }
	}
	int proTypeId = 0;//商品类型 0 实体物品 > 0 虚拟物品
	List< Map< String,Object > > mallShopList = new ArrayList<>();//商城店铺集合
	List< Integer > busUserList = new ArrayList<>();//保存商家id集合
	List< Integer > shopList = new ArrayList<>();//保存店铺id集合
	Integer toShop = 0;
	Integer type = 0;//订单类型
	long endTime = System.currentTimeMillis();
	long endTime3 = 0;
	logger.error( "访问的执行时间 : " + ( endTime - startTime ) + "ms----1111" );
	if ( params.getFrom() == 1 && CommonUtil.isNotEmpty( params.getCartIds() ) ) {//购物车
	    Map< String,Object > shopcartParams = new HashMap<>();
	    shopcartParams.put( "checkIds", params.getCartIds().split( "," ) );
	    List< Map< String,Object > > shopCartList = mallShopCartDAO.selectShopCartByCheckIds( shopcartParams );
	    long endTime2 = System.currentTimeMillis();
	    logger.error( "访问的执行时间 : " + ( endTime2 - endTime ) + "ms----2222" );
	    if ( shopCartList != null && shopCartList.size() > 0 ) {
		List< PhoneToOrderProductResult > productResultList = new ArrayList<>();
		List< Integer > freightIds = new ArrayList<>();
		for ( Map< String,Object > cartMap : shopCartList ) {
		    PhoneToOrderProductResult productResult = getToOrderProductParamsByShopCart( cartMap );//重组商品对象
		    proTypeId = productResult.getProTypeId();

		    if ( !shopList.contains( productResult.getShopId() ) ) {
			shopList.add( productResult.getShopId() );
		    }
		    if ( !busUserList.contains( productResult.getBusUserId() ) ) {
			busUserList.add( productResult.getBusUserId() );
		    }
		    int freightId = 0;
		    if ( CommonUtil.isNotEmpty( cartMap.get( "freightId" ) ) ) {
			freightId = CommonUtil.toInteger( cartMap.get( "freightId" ) );
			if ( freightId > 0 ) {
			    freightIds.add( freightId );
			    productResult.setFreightId( freightId );
			}
		    }
		    if ( CommonUtil.isNotEmpty( cartMap.get( "freightPrice" ) ) && freightId == 0 ) {
			double freightPrice = CommonUtil.toDouble( cartMap.get( "freightPrice" ) );
			if ( freightPrice > 0 ) {
			    productResult.setProductFreightPrice( freightPrice );
			}
		    }
		    productResultList.add( productResult );

		}
		List< MallFreight > freightList = null;
		if ( freightIds != null && freightIds.size() > 0 ) {
		    freightList = mallFreightService.selectFreightByFreightIds( freightIds );
		}

		if ( busUserList != null && busUserList.size() > 0 ) {
		    int busId = busUserList.get( 0 );
		    mallShopList = mallStoreService.findShopByUserIdAndShops( busId, shopList );//查询店铺信息
		}

		result = getToOrderParams( productResultList, busUserList, freightList, mallShopList, params, result, provincesId, memberLongitude, memberLangitude );
	    }
	    endTime3 = System.currentTimeMillis();
	    logger.error( "访问的执行时间 : " + ( endTime3 - endTime2 ) + "ms----3333" );
	} else if ( params.getFrom() == 2 ) {//立即购买
	    String cookieValue = CookieUtil.findCookieByName( request, CookieUtil.TO_ORDER_KEY );
	    PhoneBuyNowDTO buyNowDTO = null;
	    if ( CommonUtil.isNotEmpty( cookieValue ) ) {
		buyNowDTO = JSONObject.parseObject( cookieValue, PhoneBuyNowDTO.class );
	    }
	    if ( CommonUtil.isEmpty( buyNowDTO ) ) {
		throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "提交订单数据为空" );
	    }
	    DecimalFormat df = new DecimalFormat( "######0.00" );
	    //查询商品信息
	    MallProduct product = mallProductService.selectById( buyNowDTO.getProductId() );
	    busUserList.add( product.getUserId() );
	    shopList.add( product.getShopId() );
	    proTypeId = product.getProTypeId();

	    //查询公众号名称或商家名称以及图片
	    List< PhoneToOrderBusResult > busResultList = new ArrayList<>();//返回给页面的商家集合
	    PhoneToOrderBusResult busResult = mallCommonService.getBusUserNameOrImage( product.getUserId() );//返回给页面的商家对象

	    List< PhoneToOrderShopResult > shopResultList = new ArrayList<>();//返回给页面的店铺对象
	    PhoneToOrderShopResult shopResult = new PhoneToOrderShopResult();
	    Map< String,Object > storeMap = mallStoreService.findShopByStoreId( product.getShopId() );//查询店铺信息
	    if ( CommonUtil.isNotEmpty( storeMap ) ) {
		shopResult.setShopId( CommonUtil.toInteger( storeMap.get( "id" ) ) );
		shopResult.setShopName( CommonUtil.toString( storeMap.get( "stoName" ) ) );
		shopResult.setWxShopId( CommonUtil.toInteger( storeMap.get( "wxShopId" ) ) );
		mallShopList.add( storeMap );
	    }
	    List< PhoneToOrderProductResult > newProductResultList = new ArrayList<>();
	    PhoneToOrderProductResult productResult = getToOrderProductParamsByShopCart( product, params, buyNowDTO );//重组商品参数
	    newProductResultList.add( productResult );
	    double totalPrice = productResult.getTotalPrice();

	    //	    PhoneFreightDTO paramsDto = new PhoneFreightDTO();//运费传参
	    //	    paramsDto.setProvinceId( CommonUtil.toInteger( provincesId ) );
	    //	    paramsDto.setToshop( buyNowDTO.getToShop() );
	    //	    paramsDto.setJuli( CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude ) );
	    //	    PhoneFreightShopDTO freightShopDTO = new PhoneFreightShopDTO();//运费店铺传参
	    //	    freightShopDTO.setProTypeId( productResult.getProTypeId() );
	    //	    freightShopDTO.setShopId( shopResult.getShopId() );
	    //	    freightShopDTO.setTotalProductNum( productResult.getProductNum() );
	    //	    freightShopDTO.setTotalProductPrice( totalPrice );
	    //	    freightShopDTO.setTotalProductWeight( productResult.getProductWeight() );
	    //	    double freightMoney = mallFreightService.getFreightMoneyByShopList( null, paramsDto, freightShopDTO ); //计算运费
	    //	    if ( freightMoney > 0 ) {
	    //		shopResult.setTotalFreightMoney( freightMoney );//店铺下的运费
	    //	    }

	    //计算运费
	    Integer freightId = 0;
	    Double freightPrice = 0d;
	    double productWeight = 0;
	    if ( CommonUtil.isNotEmpty( productResult.getProductWeight() ) ) {
		productWeight = CommonUtil.toDouble( productResult.getProductWeight() );
	    }
	    if ( CommonUtil.isNotEmpty( product.getProFreightPrice() ) ) {
		freightPrice = CommonUtil.toDouble( product.getProFreightPrice() );
	    }
	    if ( CommonUtil.isNotEmpty( product.getProFreightTempId() ) && product.getProFreightTempId() > 0 ) {
		freightId = product.getProFreightTempId();
	    } else {
		freightPrice = 0d;
	    }
	    //到店购买不用计算运费
	    if ( CommonUtil.isNotEmpty( buyNowDTO.getToShop() ) && buyNowDTO.getToShop() == 1 && product.getProTypeId() > 0 ) {
		freightPrice = 0d;
	    } else {
		if ( freightId > 0 ) {
		    MallFreight mallFreight = mallFreightService.selectById( product.getProFreightTempId() );
		    List< PhoneFreightProductDTO > freightDTOList = new ArrayList<>();
		    PhoneFreightProductDTO freightProductDTO = new PhoneFreightProductDTO();
		    freightProductDTO.setProductId( product.getId() );
		    freightProductDTO.setFreightId( freightId );
		    freightProductDTO.setTotalProductNum( productResult.getProductNum() );
		    freightProductDTO.setTotalProductPrice( totalPrice );
		    freightProductDTO.setTotalProductWeight( productWeight );
		    freightProductDTO.setMallFreight( mallFreight );
		    freightDTOList.add( freightProductDTO );

		    Double juli = CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude );
		    freightPrice = mallFreightService.getFreightMoneyByProductList( freightDTOList, juli, provincesId );
		}
	    }

	    if ( freightPrice > 0 ) {
		shopResult.setTotalFreightMoney( freightPrice );//店铺下的运费
	    }

	    shopResult.setTotalNum( productResult.getProductNum() );//店铺下的商品总数
	    shopResult.setTotalMoney( totalPrice );//店铺下的商品总金额
	    shopResult.setProductResultList( newProductResultList );
	    shopResultList.add( shopResult );
	    double orderMoney = CommonUtil.toDouble( df.format( freightPrice + totalPrice ) );
	    busResult.setTotalNum( productResult.getProductNum() );//商家下的商品总数
	    busResult.setProductTotalMoney( totalPrice );//商家下的商品总额
	    busResult.setProductFreightMoney( freightPrice );//总运费
	    busResult.setTotalMoney( orderMoney );//商家下的商品总额
	    busResult.setShopResultList( shopResultList );
	    busResultList.add( busResult );

	    result.setTotalPayMoney( orderMoney );//订单支付总价（包含运费）
	    result.setTotalMoney( orderMoney );//订单优惠前的总价（包含运费）
	    result.setBusResultList( busResultList );
	    result.setType( buyNowDTO.getType() );
	    result.setActivityId( buyNowDTO.getActivityId() );
	    result.setJoinActivityId( buyNowDTO.getJoinActivityId() );
	    result.setProTypeId( product.getProTypeId() );
	    type = buyNowDTO.getType();
	    toShop = buyNowDTO.getToShop();
	} else if ( params.getFrom() == 3 && CommonUtil.isNotEmpty( mallOrder ) ) {//去支付进来的

	    busUserList.add( mallOrder.getBusUserId() );
	    shopList.add( mallOrder.getShopId() );

	    result = getToOrderFromOrder( result, mallOrder, provincesId, memberLangitude, memberLongitude );
	    proTypeId = result.getProTypeId();
	    mallShopList = result.getMallShopList();
	    type = mallOrder.getOrderType();
	    result.setType( mallOrder.getOrderType() );
	    result.setActivityId( mallOrder.getGroupBuyId() );
	    if ( CommonUtil.isNotEmpty( mallOrder.getDeliveryMethod() ) && mallOrder.getDeliveryMethod() == 3 ) {
		toShop = 1;
	    }
	}
	if ( CommonUtil.isNotEmpty( toShop ) && toShop == 1 ) {
	    result.setMemberAddressDTO( null );
	}
	result.setToShop( toShop );
	result = getToOrderResult( mallShopList, member, busUserList, result, loginDTO.getBrowerType(), params, proTypeId, provincesId, toShop, type );

	long endTime4 = System.currentTimeMillis();
	logger.error( "访问的执行时间 : " + ( endTime4 - endTime3 ) + "ms----444" );
	logger.info( "result=======" + JSON.toJSONString( result ) );
	return result;
    }

    private PhoneToOrderResult getToOrderResult( List< Map< String,Object > > mallShopList, Member member, List< Integer > busUserList, PhoneToOrderResult result,
		    Integer browerType, PhoneToOrderDTO params, int proTypeId, Integer provincesId, Integer toShop, Integer type ) {
	Map cardMap = null;
	JifenAndFenbiRule jifenFenbiRule = null;//积分粉币归责
	double discount = 0;
	StringBuilder wxShopIds = new StringBuilder( "," );
	//只有实体物品才去查询信息
	if ( mallShopList != null && mallShopList.size() > 0 ) {
	    for ( Map< String,Object > maps : mallShopList ) {
		if ( CommonUtil.isNotEmpty( maps.get( "wxShopId" ) ) && !wxShopIds.toString().contains( "," + maps.get( "wxShopId" ) + "," ) ) {
		    wxShopIds.append( maps.get( "wxShopId" ).toString() ).append( "," );
		}
	    }
	    wxShopIds = new StringBuilder( wxShopIds.substring( 1, wxShopIds.length() - 1 ) );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		cardMap = memberService.findCardAndShopIdsByMembeId( member.getId(), wxShopIds.toString() );
	    }
	    if ( proTypeId == 0 ) {
		if ( CommonUtil.isNotEmpty( member ) && ( CommonUtil.isEmpty( type ) || type <= 0 ) ) {
		    jifenFenbiRule = memberService.jifenAndFenbiRule( busUserList.get( 0 ) );//通过商家id查询积分和粉币规则
		}
		if ( CommonUtil.isNotEmpty( cardMap ) ) {
		    if ( CommonUtil.isNotEmpty( cardMap.get( "ctId" ) ) && "2".equals( cardMap.get( "ctId" ).toString() ) ) {
			discount = CommonUtil.toDouble( cardMap.get( "discount" ) );
		    }
		}
	    }

	    logger.info( "cardMap:" + cardMap );
	}

	UnionDiscountResult unionResult = null;
	if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( member.getPhone() ) && proTypeId == 0 && ( CommonUtil.isEmpty( type ) || type <= 0 ) ) {
	    UnionCardDiscountParam unionParams = new UnionCardDiscountParam();
	    unionParams.setBusId( member.getBusid() );
	    //	    unionParams.setMemberId( member.getId() );
	    unionParams.setPhone( member.getPhone() );
	    unionResult = UnionCardService.consumeUnionDiscount( unionParams );//获取联盟折扣
	}
	List< MallPaySet > mallPaySetList = mallPaySetService.selectByUserIdList( busUserList );//通过商家集合查询商城设置
	List< Map< String,Object > > isShowTake = mallTakeTheirService.isTakeTheirByUserIdList( mallPaySetList, provincesId + "" );//查询是否开启到店自提
	int isDaodianPay = 0;//是否显示到店支付
	List< PhoneOrderUserDTO > orderUserDTOList = new ArrayList<>();//集合用来算支付方式的
	//循环结果，查询商家的支付方式和配送方式
	if ( CommonUtil.isNotEmpty( result ) && CommonUtil.isNotEmpty( result.getBusResultList() ) ) {
	    List< PayWay > payWayList = new ArrayList<>();
	    for ( PhoneToOrderBusResult busResult : result.getBusResultList() ) {
		//是否能使用会员折扣1 能
		Integer isCanUseMemberDiscount = 0;
		//是否能使用联盟折扣 1 能
		Integer isCanUseUnionDiscount = 1;
		//是否能使用积分抵扣 1 能
		Integer isCanUseJifenDiscount = 0;
		//是否能使用粉币抵扣 1 能
		Integer isCanUseFenbiDiscount = 0;
		//是否能使用优惠券抵扣 1能
		Integer isCanUseCouponDiscount = 0;
		busResult.setJifenFenbiRule( jifenFenbiRule );

		if ( CommonUtil.isNotEmpty( unionResult ) ) {
		    busResult.setUnionDiscount( unionResult.getDiscount() );
		    busResult.setUnionCardId( unionResult.getCardId() );
		    busResult.setUnionStatus( unionResult.getCode() );
		    //		    busResult.setUnionId( unionResult.getUnionId() );
		}

		int takeId = 0;//上门自提id
		String takeAddress = "";//上门自提地址
		String takeTimes = "";//提货时间
		int isStorePay = 0;
		if ( isShowTake != null && isShowTake.size() > 0 ) {
		    for ( Map< String,Object > isShowMap : isShowTake ) {
			boolean isFlag = false;
			if ( CommonUtil.isNotEmpty( busResult.getTakeId() ) && busResult.getTakeId() > 0 ) {
			    if ( busResult.getTakeId().toString().equals( isShowMap.get( "takeId" ).toString() ) ) {
				isFlag = true;
			    }
			} else if ( CommonUtil.toString( busResult.getBusId() ).equals( isShowMap.get( "user_id" ).toString() ) ) {
			    isFlag = true;
			}
			if ( isFlag ) {
			    takeId = CommonUtil.toInteger( isShowMap.get( "takeId" ) );
			    takeAddress = CommonUtil.toString( isShowMap.get( "visitAddressDetail" ) );
			    isStorePay = CommonUtil.toInteger( isShowMap.get( "isStorePay" ) );
			    break;
			}
		    }
		}
		isDaodianPay = isStorePay;
		busResult.setTakeId( takeId );
		busResult.setTakeAddress( takeAddress );

		//获取商家的配送方式
		List< PhoneOrderWayDTO > deliveryResultList = ToOrderUtil.getDeliveryWay( params, proTypeId, takeId, toShop );
		busResult.setDeliveryWayList( deliveryResultList );
		//默认选中配送方式
		if ( CommonUtil.isNotEmpty( busResult.getSelectDeliveryWayId() ) && busResult.getSelectDeliveryWayId() > 0 ) {
		    for ( PhoneOrderWayDTO phoneOrderWayDTO : deliveryResultList ) {
			if ( phoneOrderWayDTO.getId() == CommonUtil.toInteger( busResult.getSelectDeliveryWayId() ) ) {
			    busResult.setSelectDelivery( phoneOrderWayDTO );
			    break;
			}
		    }
		}

		busResult.setMemberDiscount( discount );
		double fenbiProductMoney = 0;//能够粉币抵扣的商品金额
		double jifenProductMoney = 0;//能够积分抵扣的商品金额
		for ( PhoneToOrderShopResult shopResult : busResult.getShopResultList() ) {
		    //是否能使用优惠券抵扣
		    int isCanUseYhqDiscount = 0;

		    List< Coupons > couponsList = new ArrayList<>();
		    if ( CommonUtil.isNotEmpty( cardMap ) && proTypeId == 0 ) {
			//多粉优惠券
			if ( cardMap.containsKey( "duofenCards" + shopResult.getWxShopId() ) ) {
			    Object obj = cardMap.get( "duofenCards" + shopResult.getWxShopId() );
			    couponsList = ToOrderUtil.getDuofenCouponsResult( obj, couponsList, shopResult.getSelectCouponId() );
			}
			//微信优惠券
			if ( cardMap.containsKey( "cardList" + shopResult.getWxShopId() ) ) {
			    Object obj = cardMap.get( "cardList" + shopResult.getWxShopId() );
			    couponsList = ToOrderUtil.getWxCouponsResult( obj, couponsList, shopResult.getSelectCouponId() );
			}
		    }
		    if ( couponsList != null && couponsList.size() > 0 ) {
			Coupons selectCoupon = couponsList.get( 0 );
			if ( selectCoupon.getIsDefaultSelect() == 1 ) {
			    shopResult.setSelectCoupon( selectCoupon );
			}
		    }
		    shopResult.setCouponList( couponsList );

		    for ( PhoneToOrderProductResult productResult : shopResult.getProductResultList() ) {
			if ( productResult.getIsCanUseJifen() == 1 ) {//是否能使用积分
			    jifenProductMoney += productResult.getTotalPrice(); //把能使用积分的商品金额保存起来
			}
			if ( productResult.getIsCanUseFenbi() == 1 ) {
			    fenbiProductMoney += productResult.getTotalPrice();//把能使用粉币的商品金额保存起来
			}
			if ( productResult.getIsCanUseDiscount() == 1 && discount > 0 && discount < 1 ) {
			    isCanUseMemberDiscount = 1;  //能使用会员卡抵扣
			}
			if ( productResult.getIsCanUseJifen() == 1 ) {
			    isCanUseJifenDiscount = 1; //能使用积分抵扣
			}
			if ( productResult.getIsCanUseFenbi() == 1 ) {
			    isCanUseFenbiDiscount = 1;//能使用粉币抵扣
			}
			if ( productResult.getIsCanUseYhq() == 1 ) {
			    isCanUseYhqDiscount = 1;
			    isCanUseCouponDiscount = 1;
			}
		    }
		    if ( proTypeId == 0 && type == 0 ) {
			shopResult.setIsCanUseYhqDiscount( isCanUseYhqDiscount );
		    }
		}
		PhoneOrderUserDTO phoneOrderUserDTO = new PhoneOrderUserDTO();
		if ( CommonUtil.isNotEmpty( cardMap ) ) {
		    JifenAndFenbBean bean = ToOrderUtil.getJifenFenbiParams( jifenFenbiRule, jifenProductMoney, fenbiProductMoney, cardMap );
		    if ( CommonUtil.isNotEmpty( bean ) ) {
			busResult.setJifenNum( bean.getJifenNum() );
			busResult.setJifenMoney( bean.getJifenMoney() );
			busResult.setFenbiNum( bean.getFenbiNum() );
			busResult.setFenbiMoney( bean.getFenbiMoney() );
		    }
		    if ( CommonUtil.isNotEmpty( cardMap.get( "ctId" ) ) ) {
			phoneOrderUserDTO.setMemberCtId( CommonUtil.toInteger( cardMap.get( "ctId" ) ) );
		    }
		}
		if ( proTypeId == 0 && type == 0 ) {
		    busResult.setIsCanUseMemberDiscount( isCanUseMemberDiscount );
		    busResult.setIsCanUseJifenDiscount( isCanUseJifenDiscount );
		    busResult.setIsCanUseFenbiDiscount( isCanUseFenbiDiscount );
		    if ( CommonUtil.isNotEmpty( busResult.getUnionStatus() ) && busResult.getUnionStatus() == 1 ) {
			busResult.setIsCanUseUnionDiscount( isCanUseUnionDiscount );
		    } else {
			busResult.setIsCanUseUnionDiscount( 0 );
		    }
		    busResult.setIsCanUseYhqDiscount( isCanUseCouponDiscount );
		} else {
		    busResult.setIsCanUseUnionDiscount( 0 );
		}
		phoneOrderUserDTO.setPublicId( busResult.getPublicId() );
		phoneOrderUserDTO.setBusId( busResult.getBusId() );
		orderUserDTOList.add( phoneOrderUserDTO );

	    }
	    PayWay payWay = payService.getPayWay( busUserList.get( 0 ) );
	    payWayList.add( payWay );
	    //获取商家的支付方式
	    List< PhoneOrderWayDTO > wayResultList = ToOrderUtil
			    .getPayWay( browerType, orderUserDTOList, params, isDaodianPay, mallPaySetList, proTypeId, type, payWayList, toShop );
	    result.setPayWayList( wayResultList );
	    if ( CommonUtil.isNotEmpty( result.getSelectPayWayId() ) && result.getSelectPayWayId() > 0 ) {
		for ( PhoneOrderWayDTO phoneOrderWayDTO : wayResultList ) {
		    if ( phoneOrderWayDTO.getId() == result.getSelectPayWayId() ) {
			result.setSelectPayWay( phoneOrderWayDTO );
			break;
		    }
		}
	    }
	}
	return result;
    }

    /**
     * 重组订单参数(购物车进入提交订单页面)
     */
    private PhoneToOrderResult getToOrderParams( List< PhoneToOrderProductResult > productResultList, List< Integer > busUserList, List< MallFreight > freightList,
		    List< Map< String,Object > > mallShopList, PhoneToOrderDTO params, PhoneToOrderResult result, Integer provincesId, Double memberLongitude,
		    Double memberLangitude ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );

	//查询公众号名称或商家名称以及图片
	List< PhoneToOrderBusResult > busResultList = new ArrayList<>();//返回给页面的商家接话
	PhoneToOrderBusResult busResult = new PhoneToOrderBusResult();//返回给页面的商家对象
	if ( busUserList != null && busUserList.size() > 0 ) {
	    int busId = busUserList.get( 0 );
	    busResult = mallCommonService.getBusUserNameOrImage( busId );//获取商家名称或商家头像
	}
	int busTotalNum = 0;//商家商品总数
	double busTotalPrice = 0;//商家商品总额
	double busFreightPrice = 0;//商家运费
	double totalOrderMoney = 0;//订单总价（含运费）
	List< PhoneToOrderShopResult > shopResultList = new ArrayList<>();//返回给页面的店铺对象
	if ( mallShopList != null && mallShopList.size() > 0 && productResultList != null && productResultList.size() > 0 ) {
	    for ( Map< String,Object > storeMap : mallShopList ) {//循环店铺集合
		PhoneToOrderShopResult shopResult = new PhoneToOrderShopResult();
		shopResult.setShopId( CommonUtil.toInteger( storeMap.get( "id" ) ) );
		shopResult.setShopName( CommonUtil.toString( storeMap.get( "sto_name" ) ) );
		shopResult.setWxShopId( CommonUtil.toInteger( storeMap.get( "wxShopId" ) ) );
		int totalNum = 0;//店铺商品总数
		double totalPrice = 0;//店铺商品总额
		//		double productWeight = 0;//商品重量
		//		int proTypeId = 0;//商品类型id
		double totalFreightPrice = 0;//商品运费
		List< PhoneFreightProductDTO > freightDTOList = new ArrayList<>();

		List< PhoneToOrderProductResult > newProductResultList = new ArrayList<>();
		for ( PhoneToOrderProductResult productResult : productResultList ) {//循环商品集合
		    if ( productResult.getShopId() == shopResult.getShopId() ) {
			newProductResultList.add( productResult );
			totalNum += productResult.getProductNum();
			totalPrice += productResult.getTotalPrice();
			//			productWeight += productResult.getProductWeight();
			//			proTypeId = productResult.getProTypeId();
			if ( CommonUtil.isNotEmpty( productResult.getProductFreightPrice() ) ) {
			    totalFreightPrice += productResult.getProductFreightPrice();
			}
			if ( CommonUtil.isNotEmpty( productResult.getFreightId() ) && productResult.getFreightId() > 0 ) {
			    PhoneFreightProductDTO freightProductDTO = new PhoneFreightProductDTO();
			    freightProductDTO.setProductId( productResult.getProductId() );
			    freightProductDTO.setFreightId( productResult.getFreightId() );
			    freightProductDTO.setTotalProductNum( productResult.getProductNum() );
			    freightProductDTO.setTotalProductPrice( productResult.getTotalPrice() );
			    freightProductDTO.setTotalProductWeight( productResult.getProductWeight() );
			    for ( int i = 0; i < freightList.size(); i++ ) {
				MallFreight mallFreight = freightList.get( i );
				if ( mallFreight.getId().toString().equals( productResult.getFreightId().toString() ) ) {
				    freightProductDTO.setMallFreight( mallFreight );
				    break;
				}
			    }
			    freightDTOList.add( freightProductDTO );
			}
		    }
		}
		if ( newProductResultList == null || newProductResultList.size() == 0 ) {
		    continue;
		}
		if ( freightDTOList != null && freightDTOList.size() > 0 ) {
		    Double juli = CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude );
		    double freightPrice = mallFreightService.getFreightMoneyByProductList( freightDTOList, juli, provincesId );
		    if ( freightPrice > 0 ) {
			totalFreightPrice += freightPrice;
		    }
		}

		//		PhoneFreightDTO paramsDto = new PhoneFreightDTO();//运费传参
		//		paramsDto.setProvinceId( CommonUtil.toInteger( provincesId ) );
		//		//		paramsDto.setToshop( params.getToShop() );
		//		paramsDto.setJuli( CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude ) );
		//		PhoneFreightShopDTO freightShopDTO = new PhoneFreightShopDTO();//运费店铺传参
		//		freightShopDTO.setProTypeId( proTypeId );
		//		freightShopDTO.setShopId( shopResult.getShopId() );
		//		freightShopDTO.setTotalProductNum( totalNum );
		//		freightShopDTO.setTotalProductPrice( totalPrice );
		//		freightShopDTO.setTotalProductWeight( productWeight );
		//		double freightMoney = mallFreightService.getFreightMoneyByShopList( freightList, paramsDto, freightShopDTO );

		if ( totalFreightPrice > 0 ) {
		    shopResult.setTotalFreightMoney( totalFreightPrice );
		    busFreightPrice += totalFreightPrice;
		}
		//计算运费
		busTotalNum += totalNum;
		busTotalPrice += totalPrice;
		if ( newProductResultList != null && newProductResultList.size() > 0 ) {
		    totalPrice = CommonUtil.toDouble( df.format( totalPrice ) );
		    shopResult.setTotalNum( totalNum );
		    shopResult.setTotalMoney( totalPrice );
		    shopResult.setProductResultList( newProductResultList );
		    productResultList.removeAll( newProductResultList );//移除商品信息
		}
		shopResultList.add( shopResult );
	    }
	    totalOrderMoney = CommonUtil.toDouble( df.format( busTotalPrice + busFreightPrice ) );//支付金额
	    busTotalPrice = CommonUtil.toDouble( df.format( busTotalPrice ) );//商品总额
	    busFreightPrice = CommonUtil.toDouble( df.format( busFreightPrice ) );//运费
	    busResult.setTotalNum( busTotalNum );
	    busResult.setProductTotalMoney( busTotalPrice );
	    busResult.setProductFreightMoney( busFreightPrice );
	    busResult.setTotalMoney( totalOrderMoney );
	    busResult.setShopResultList( shopResultList );
	}
	busResultList.add( busResult );

	result.setBusResultList( busResultList );
	result.setTotalMoney( totalOrderMoney );
	result.setTotalPayMoney( totalOrderMoney );

	return result;
    }

    /**
     * 重组订单商品参数
     */
    private PhoneToOrderProductResult getToOrderProductParamsByShopCart( Map< String,Object > cartMap ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	int productId = CommonUtil.toInteger( cartMap.get( "product_id" ) );
	int shopId = CommonUtil.toInteger( cartMap.get( "shop_id" ) );
	double price = CommonUtil.toDouble( cartMap.get( "price" ) );
	double oldPrice = price;
	PhoneToOrderProductResult result = new PhoneToOrderProductResult();
	result.setProductId( productId );//商品id
	result.setShopId( shopId );//店铺id
	result.setBusUserId( CommonUtil.toInteger( cartMap.get( "bus_user_id" ) ) );//商家id
	result.setProductName( CommonUtil.toString( cartMap.get( "pro_name" ) ) );//商品名称
	result.setProductImageUrl( CommonUtil.toString( cartMap.get( "image_url" ) ) );//商品图片
	if ( CommonUtil.isNotEmpty( cartMap.get( "pro_weight" ) ) ) {
	    result.setProductWeight( CommonUtil.toDouble( cartMap.get( "pro_weight" ) ) );//商品重量
	}
	if ( CommonUtil.isNotEmpty( cartMap.get( "is_specifica" ) ) && "1".equals( cartMap.get( "is_specifica" ).toString() ) && CommonUtil
			.isNotEmpty( cartMap.get( "product_specificas" ) ) ) {
	    //获取商品规格和规格价
	    Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( cartMap.get( "product_specificas" ).toString(), productId );
	    if ( CommonUtil.isNotEmpty( invMap ) ) {
		result.setProductSpecificaValueId( cartMap.get( "product_specificas" ).toString() );
		result.setProductSpecificaValue( CommonUtil.toString( invMap.get( "specifica_values" ) ).replace( ",", "/" ) );

		price = CommonUtil.toDouble( invMap.get( "inv_price" ) );//商品规格价
		if ( CommonUtil.isNotEmpty( invMap.get( "weight" ) ) ) {
		    double weight = CommonUtil.toDouble( invMap.get( "weight" ) );
		    if ( weight > 0 ) {
			result.setProductWeight( weight );
		    }
		}
	    }
	}
	int productNum = CommonUtil.toInteger( cartMap.get( "product_num" ) );
	double totalPrice = price * productNum;
	if ( !"0".equals( cartMap.get( "pro_type" ).toString() ) ) {//查询批发商品
	    //	    MallPifa pifa = mallPifaService.selectBuyByProductId( productId, shopId, 0 );
	    String proSpecStr = cartMap.get( "pro_spec_str" ).toString();
	    double pfTotalPrice = 0;
	    if ( CommonUtil.isNotEmpty( proSpecStr ) ) {
		JSONObject specObj = JSONObject.parseObject( proSpecStr );
		List< PhoneOrderPifaSpecDTO > pfSpecResultsList = new ArrayList<>();
		Set< String > set = specObj.keySet();
		productNum = 0;
		for ( String str : set ) {
		    if ( CommonUtil.isNotEmpty( specObj.get( str ) ) ) {
			JSONObject obj = specObj.getJSONObject( str );
			if ( "1".equals( obj.getString( "isCheck" ) ) ) {
			    PhoneOrderPifaSpecDTO pfSpecResult = new PhoneOrderPifaSpecDTO();
			    pfSpecResult.setTotalNum( obj.getInteger( "num" ) );
			    pfSpecResult.setPfPrice( obj.getDouble( "price" ) );
			    pfSpecResult.setSpecificaName( obj.getString( "specName" ) );
			    pfSpecResult.setSpecificaIds( str );
			    pfSpecResult.setSpecificaValues( obj.getString( "value" ) );
			    pfSpecResultsList.add( pfSpecResult );

			    productNum += pfSpecResult.getTotalNum();
			    pfTotalPrice += pfSpecResult.getPfPrice() * pfSpecResult.getTotalNum();
			}
		    }
		}
		result.setPfSpecResultList( pfSpecResultsList );
		if ( pfTotalPrice > 0 ) {
		    totalPrice = CommonUtil.toDouble( df.format( pfTotalPrice ) );
		}
	    }
	}
	result.setTotalPrice( CommonUtil.toDouble( df.format( totalPrice ) ) );
	result.setProductPrice( CommonUtil.div( totalPrice, productNum, 2 ) );//商品价
	result.setProductOldPrice( oldPrice );//商品原价
	result.setProductNum( productNum );//商品数量
	result.setIsCanUseDiscount( CommonUtil.toInteger( cartMap.get( "is_member_discount" ) ) );//是否能使用会员折扣
	result.setIsCanUseYhq( CommonUtil.toInteger( cartMap.get( "is_coupons" ) ) );//是否能使用优惠券
	result.setIsCanUseJifen( CommonUtil.toInteger( cartMap.get( "is_integral_deduction" ) ) );//是否能使用积分抵扣
	result.setIsCanUseFenbi( CommonUtil.toInteger( cartMap.get( "is_fenbi_deduction" ) ) );//是否能使用粉币抵扣
	if ( CommonUtil.isNotEmpty( cartMap.get( "pro_type_id" ) ) ) {
	    result.setProTypeId( CommonUtil.toInteger( cartMap.get( "pro_type_id" ) ) );
	}
	if ( CommonUtil.isNotEmpty( cartMap.get( "sale_member_id" ) ) ) {
	    result.setSaleMemberId( CommonUtil.toInteger( cartMap.get( "sale_member_id" ) ) );
	}
	if ( CommonUtil.isNotEmpty( cartMap.get( "commission" ) ) ) {
	    result.setCommission( CommonUtil.toDouble( cartMap.get( "commission" ) ) );
	}
	return result;
    }

    /**
     * 重组订单商品参数
     */
    private PhoneToOrderProductResult getToOrderProductParamsByShopCart( MallProduct product, PhoneToOrderDTO params, PhoneBuyNowDTO buyNowDTO ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	int productNum = buyNowDTO.getProductNum();
	int productId = product.getId();
	double price = CommonUtil.toDouble( buyNowDTO.getPrice() );
	double oldPrice = price;
	PhoneToOrderProductResult result = new PhoneToOrderProductResult();
	result.setProductId( productId );//商品id
	result.setShopId( product.getShopId() );//店铺id
	result.setBusUserId( product.getUserId() );//商家id
	result.setProductName( product.getProName() );//商品名称
	if ( CommonUtil.isNotEmpty( product.getProWeight() ) ) {
	    result.setProductWeight( CommonUtil.toDouble( product.getProWeight() ) );//商品重量
	}
	Map< String,Object > imageMap = new HashMap<>();
	imageMap.put( "assId", product.getId() );
	imageMap.put( "isMainImages", 1 );
	imageMap.put( "assType", 1 );
	List< Map< String,Object > > imageList = mallImageAssociativeDAO.selectByAssId( imageMap );
	if ( imageList != null && imageList.size() > 0 ) {
	    result.setProductImageUrl( CommonUtil.toString( imageList.get( 0 ).get( "image_url" ) ) );//商品图片
	}
	if ( CommonUtil.isNotEmpty( product.getIsSpecifica() ) && "1".equals( product.getIsSpecifica().toString() ) && CommonUtil.isNotEmpty( buyNowDTO.getProductSpecificas() ) ) {
	    //获取商品规格和规格价
	    Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( buyNowDTO.getProductSpecificas(), productId );
	    if ( CommonUtil.isNotEmpty( invMap ) ) {
		result.setProductSpecificaValueId( buyNowDTO.getProductSpecificas() );
		result.setProductSpecificaValue( CommonUtil.toString( invMap.get( "specifica_values" ) ).replace( ",", "/" ) );
		double invPrice = CommonUtil.toDouble( invMap.get( "inv_price" ) );//商品规格价
		if ( buyNowDTO.getType() == 0 ) {
		    price = invPrice;//商品规格价
		} else {
		    oldPrice = invPrice;
		}
		if ( CommonUtil.isNotEmpty( invMap.get( "weight" ) ) ) {
		    double weight = CommonUtil.toDouble( invMap.get( "weight" ) );
		    if ( weight > 0 ) {
			result.setProductWeight( weight );
		    }
		}
	    }
	} else {
	    if ( buyNowDTO.getType() > 0 ) {
		oldPrice = CommonUtil.toDouble( product.getProPrice() );
	    }
	}
	double totalPrice = price * productNum;
	if ( CommonUtil.isNotEmpty( buyNowDTO.getType() ) ) {
	    if ( buyNowDTO.getType() == 7 ) {//查询批发商品
		double pfTotalPrice = 0;
		MallPifa pifa = mallPifaService.selectBuyByProductId( productId, product.getShopId(), 0 );
		if ( CommonUtil.isNotEmpty( pifa ) ) {
		    pfTotalPrice = CommonUtil.toDouble( pifa.getPfPrice() );
		    if ( CommonUtil.isNotEmpty( buyNowDTO.getPifaSpecificaDTOList() ) ) {
			List< PhoneToOrderPifatSpecificaDTO > pifaSpecificaDTOList = JSONArray
					.parseArray( buyNowDTO.getPifaSpecificaDTOList(), PhoneToOrderPifatSpecificaDTO.class );
			List< PhoneOrderPifaSpecDTO > pfSpecResultsList = mallPifaService.getPifaPrice( product.getId(), product.getShopId(), pifa.getId(), pifaSpecificaDTOList );
			result.setPfSpecResultList( pfSpecResultsList );
			productNum = 0;

			if ( pfSpecResultsList == null || pfSpecResultsList.size() == 0 ) {
			    productNum = pifaSpecificaDTOList.get( 0 ).getProductNum();
			    pfTotalPrice = pifa.getPfPrice().doubleValue();
			} else {
			    for ( PhoneOrderPifaSpecDTO pfSpecResult : pfSpecResultsList ) {
				productNum += pfSpecResult.getTotalNum();
				pfTotalPrice += pfSpecResult.getPfPrice() * pfSpecResult.getTotalNum();
			    }
			}
			if ( pfTotalPrice > 0 ) {
			    totalPrice = CommonUtil.toDouble( df.format( pfTotalPrice ) );
			}
		    } else {
			pfTotalPrice = CommonUtil.toDouble( df.format( pfTotalPrice * productNum ) );
			totalPrice = pfTotalPrice;
		    }
		}

	    }
	}
	result.setTotalPrice( CommonUtil.toDouble( df.format( totalPrice ) ) );
	result.setProductPrice( CommonUtil.div( totalPrice, productNum, 2 ) );//商品价
	result.setProductOldPrice( oldPrice );//商品原价
	result.setProductNum( productNum );//商品数量
	result.setIsCanUseDiscount( CommonUtil.toInteger( product.getIsMemberDiscount() ) );//是否能使用会员折扣
	result.setIsCanUseYhq( CommonUtil.toInteger( product.getIsCoupons() ) );//是否能使用优惠券
	result.setIsCanUseJifen( CommonUtil.toInteger( product.getIsIntegralDeduction() ) );//是否能使用积分抵扣
	result.setIsCanUseFenbi( CommonUtil.toInteger( product.getIsFenbiDeduction() ) );//是否能使用粉币抵扣
	if ( CommonUtil.isNotEmpty( product.getProTypeId() ) ) {
	    result.setProTypeId( CommonUtil.toInteger( product.getProTypeId() ) );
	}
	result.setSaleMemberId( buyNowDTO.getSaleMemberId() );
	result.setCommission( buyNowDTO.getCommission() );
	return result;
    }

    /**
     * 从去支付进来的
     */
    private PhoneToOrderResult getToOrderFromOrder( PhoneToOrderResult result, MallOrder mallOrder, Integer provincesId, double memberLangitude, double memberLongitude ) {
	//查询公众号名称或商家名称以及图片
	List< PhoneToOrderBusResult > busResultList = new ArrayList<>();//返回给页面的商家接话
	PhoneToOrderBusResult busResult = mallCommonService.getBusUserNameOrImage( mallOrder.getBusUserId() );//返回给页面的商家对象

	List< PhoneToOrderShopResult > shopResultList = new ArrayList<>();//返回给页面的店铺对象
	PhoneToOrderShopResult shopResult = new PhoneToOrderShopResult();
	Map< String,Object > storeMap = mallStoreService.findShopByStoreId( mallOrder.getShopId() );//查询店铺信息
	if ( CommonUtil.isNotEmpty( storeMap ) ) {
	    shopResult.setShopId( CommonUtil.toInteger( storeMap.get( "id" ) ) );
	    shopResult.setShopName( CommonUtil.toString( storeMap.get( "stoName" ) ) );
	    shopResult.setWxShopId( CommonUtil.toInteger( storeMap.get( "wxShopId" ) ) );
	    List< Map< String,Object > > mallShopList = new ArrayList<>();
	    mallShopList.add( storeMap );
	    result.setMallShopList( mallShopList );
	}
	int proTypeId = 0;
	int totalNum = 0;
	double totalPrice = 0;
	double totalWeight = 0;
	int toShop = 0;
	if ( mallOrder.getDeliveryMethod() == 3 ) {
	    toShop = 1;
	}
	double totalFreightPrice = 0;
	List< PhoneFreightProductDTO > freightDTOList = new ArrayList<>();//计算运费参数
	boolean isSelectDiscount = true;//是否选中了折扣
	List< PhoneToOrderProductResult > newProductResultList = new ArrayList<>();
	for ( MallOrderDetail detail : mallOrder.getMallOrderDetail() ) {
	    MallProduct product = mallProductService.selectById( detail.getProductId() );
	    proTypeId = detail.getProTypeId();
	    PhoneToOrderProductResult productResult = getToOrderProductParamsByOrder( mallOrder, detail, product );//重组商品参数
	    newProductResultList.add( productResult );
	    totalPrice = CommonUtil.add( totalPrice, productResult.getTotalPrice() );
	    totalNum += detail.getDetProNum();
	    if ( CommonUtil.isNotEmpty( product.getProWeight() ) ) {
		totalWeight = CommonUtil.add( totalWeight, CommonUtil.toDouble( product.getProWeight() ) );
	    }
	    if ( CommonUtil.isNotEmpty( detail.getDiscount() ) && detail.getDiscount() >= 10 && detail.getDiscount() < 100 ) {
		isSelectDiscount = true;
	    } else {
		isSelectDiscount = false;
	    }
	    //计算运费
	    Integer freightId = 0;
	    Double freightPrice = 0d;
	    if ( CommonUtil.isNotEmpty( product.getProFreightPrice() ) ) {
		freightPrice = CommonUtil.toDouble( product.getProFreightPrice() );

	    }
	    if ( CommonUtil.isNotEmpty( product.getProFreightTempId() ) && product.getProFreightTempId() > 0 ) {
		freightId = product.getProFreightTempId();
	    }
	    if ( freightId > 0 && toShop == 0 ) {
		MallFreight mallFreight = mallFreightService.selectById( freightId );
		PhoneFreightProductDTO freightProductDTO = new PhoneFreightProductDTO();
		freightProductDTO.setProductId( product.getId() );
		freightProductDTO.setFreightId( freightId );
		freightProductDTO.setTotalProductNum( productResult.getProductNum() );
		freightProductDTO.setTotalProductPrice( productResult.getTotalPrice() );
		freightProductDTO.setTotalProductWeight( productResult.getProductWeight() );
		freightProductDTO.setMallFreight( mallFreight );
		freightDTOList.add( freightProductDTO );
	    } else if ( freightPrice > 0 ) {
		totalFreightPrice += freightPrice;
	    }

	}
	if ( freightDTOList != null && freightDTOList.size() > 0 && toShop == 0 ) {

	    Double juli = CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude );
	    double freightPrice = mallFreightService.getFreightMoneyByProductList( freightDTOList, juli, provincesId );
	    totalFreightPrice += freightPrice;
	}
	//	PhoneFreightDTO paramsDto = new PhoneFreightDTO();//运费传参
	//	paramsDto.setProvinceId( CommonUtil.toInteger( provincesId ) );
	//	paramsDto.setToshop( toShop );
	//	paramsDto.setJuli( CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude ) );
	//	PhoneFreightShopDTO freightShopDTO = new PhoneFreightShopDTO();//运费店铺传参
	//	freightShopDTO.setProTypeId( proTypeId );
	//	freightShopDTO.setShopId( shopResult.getShopId() );
	//	freightShopDTO.setTotalProductNum( totalNum );
	//	freightShopDTO.setTotalProductPrice( totalPrice );
	//	freightShopDTO.setTotalProductWeight( totalWeight );
	//	double freightMoney = mallFreightService.getFreightMoneyByShopList( null, paramsDto, freightShopDTO ); //计算运费
	//到店购买不用计算运费
	if ( toShop == 1 ) {
	    totalFreightPrice = 0;
	}
	if ( totalFreightPrice > 0 ) {

	    shopResult.setTotalFreightMoney( CommonUtil.formatDoubleNumber( totalFreightPrice ) );//店铺下的运费
	}
	shopResult.setTotalNum( totalNum );//店铺下的商品总数
	shopResult.setTotalMoney( totalPrice );//店铺下的商品总金额
	shopResult.setProductResultList( newProductResultList );
	if ( CommonUtil.isNotEmpty( mallOrder.getCouponId() ) && mallOrder.getCouponId() > 0 ) {
	    shopResult.setSelectCouponId( mallOrder.getCouponId() );
	}
	shopResultList.add( shopResult );
	double orderMoney = CommonUtil.formatDoubleNumber( totalFreightPrice + totalPrice );
	busResult.setTotalNum( totalNum );//商家下的商品总数
	busResult.setProductTotalMoney( totalPrice );//商家下的商品总额
	busResult.setProductFreightMoney( totalFreightPrice );//总运费
	busResult.setTotalMoney( orderMoney );//商家下的商品总额
	if ( mallOrder.getDeliveryMethod() == 2 ) {
	    busResult.setTakeId( mallOrder.getTakeTheirId() );
	    busResult.setTakeAddress( mallOrder.getAppointmentAddress() );
	    busResult.setAppointmentUserName( mallOrder.getAppointmentName() );
	    busResult.setAppointmentUserPhone( mallOrder.getAppointmentTelephone() );
	    MallTakeTheirTime takeTheirTime = new MallTakeTheirTime();
	    takeTheirTime.setTimes( DateTimeKit.format( mallOrder.getAppointmentTime(), DateTimeKit.DEFAULT_DATE_FORMAT ) );
	    takeTheirTime.setStartTime( mallOrder.getAppointmentStartTime() );
	    takeTheirTime.setEndTime( mallOrder.getAppointmentEndTime() );
	    busResult.setSelectTakeTime( takeTheirTime );
	}
	busResult.setSelectDeliveryWayId( mallOrder.getDeliveryMethod() );

	busResult.setShopResultList( shopResultList );
	if ( CommonUtil.isNotEmpty( mallOrder.getUnionCardId() ) && mallOrder.getUnionCardId() > 0 ) {
	    busResult.setIsSelectUnion( true );
	}
	if ( CommonUtil.isNotEmpty( mallOrder.getFenbiDiscountMoney() ) && mallOrder.getFenbiDiscountMoney() > 0 ) {
	    busResult.setIsSelectFenbi( true );
	}
	if ( CommonUtil.isNotEmpty( mallOrder.getJifenDiscountMoney() ) && mallOrder.getJifenDiscountMoney() > 0 ) {
	    busResult.setIsSelectJifen( true );
	}
	busResult.setIsSelectDiscount( isSelectDiscount );
	busResultList.add( busResult );

	result.setTotalPayMoney( orderMoney );//订单支付总价（包含运费）
	result.setTotalMoney( orderMoney );//订单优惠前的总价（包含运费）
	result.setBusResultList( busResultList );
	result.setProTypeId( proTypeId );
	result.setSelectPayWayId( mallOrder.getOrderPayWay() );
	return result;
    }

    /**
     * 重组订单商品参数
     */
    private PhoneToOrderProductResult getToOrderProductParamsByOrder( MallOrder order, MallOrderDetail detail, MallProduct product ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	int productNum = detail.getDetProNum();
	int productId = detail.getProductId();
	double price = CommonUtil.toDouble( detail.getDetPrivivilege() );
	double oldPrice = price;
	PhoneToOrderProductResult result = new PhoneToOrderProductResult();
	result.setProductId( productId );//商品id
	result.setShopId( detail.getShopId() );//店铺id
	result.setBusUserId( order.getBusUserId() );//商家id
	result.setProductName( detail.getDetProName() );//商品名称
	Map< String,Object > imageMap = new HashMap<>();
	imageMap.put( "assId", productId );
	imageMap.put( "isMainImages", 1 );
	imageMap.put( "assType", 1 );
	List< Map< String,Object > > imageList = mallImageAssociativeDAO.selectByAssId( imageMap );
	if ( imageList != null && imageList.size() > 0 ) {
	    result.setProductImageUrl( CommonUtil.toString( imageList.get( 0 ).get( "image_url" ) ) );//商品图片
	}
	if ( CommonUtil.isNotEmpty( product.getProWeight() ) ) {
	    result.setProductWeight( CommonUtil.toDouble( product.getProWeight() ) );
	}
	if ( CommonUtil.isNotEmpty( detail.getProductSpecificas() ) ) {
	    //获取商品规格和规格价
	    Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( detail.getProductSpecificas(), productId );
	    if ( CommonUtil.isNotEmpty( invMap ) ) {
		result.setProductSpecificaValueId( detail.getProductSpecificas() );
		result.setProductSpecificaValue( CommonUtil.toString( invMap.get( "specifica_values" ) ).replace( ",", "/" ) );
		price = CommonUtil.toDouble( invMap.get( "inv_price" ) );//商品规格价
		if ( CommonUtil.isNotEmpty( invMap.get( "weight" ) ) ) {
		    if ( CommonUtil.toDouble( invMap.get( "weight" ) ) > 0 ) {
			result.setProductWeight( CommonUtil.toDouble( invMap.get( "weight" ) ) );
		    }
		}
	    }
	}
	double totalPrice = price * productNum;
	if ( CommonUtil.isNotEmpty( order.getOrderType() ) ) {
	    if ( order.getOrderType() == 7 ) {//查询批发商品
		double pfTotalPrice = 0;
		MallPifa pifa = mallPifaService.selectBuyByProductId( productId, order.getShopId(), 0 );
		if ( CommonUtil.isNotEmpty( pifa ) ) {
		    pfTotalPrice = CommonUtil.toDouble( pifa.getPfPrice() );
		    if ( CommonUtil.isNotEmpty( detail.getProSpecStr() ) ) {
			JSONObject specObj = JSONObject.parseObject( detail.getProSpecStr() );

			List< PhoneOrderPifaSpecDTO > pfSpecResultsList = new ArrayList<>();
			Set< String > set = specObj.keySet();
			productNum = 0;
			for ( String str : set ) {
			    if ( CommonUtil.isNotEmpty( specObj.get( str ) ) ) {
				JSONObject obj = specObj.getJSONObject( str );
				if ( "1".equals( obj.getString( "isCheck" ) ) ) {
				    PhoneOrderPifaSpecDTO pfSpecResult = new PhoneOrderPifaSpecDTO();
				    pfSpecResult.setTotalNum( obj.getInteger( "num" ) );
				    pfSpecResult.setPfPrice( obj.getDouble( "price" ) );
				    pfSpecResult.setSpecificaName( obj.getString( "name" ) );
				    pfSpecResult.setSpecificaIds( str );
				    pfSpecResult.setSpecificaValues( obj.getString( "value" ) );
				    pfSpecResultsList.add( pfSpecResult );

				    productNum += pfSpecResult.getTotalNum();
				    pfTotalPrice += pfSpecResult.getPfPrice() * pfSpecResult.getTotalNum();
				}
			    }
			}
			result.setPfSpecResultList( pfSpecResultsList );
			if ( pfTotalPrice > 0 ) {
			    totalPrice = CommonUtil.toDouble( df.format( pfTotalPrice ) );
			}
		    } else {
			pfTotalPrice = CommonUtil.toDouble( df.format( pfTotalPrice * productNum ) );
			totalPrice = pfTotalPrice;
		    }
		}

	    }
	}
	result.setTotalPrice( CommonUtil.toDouble( df.format( totalPrice ) ) );
	result.setProductPrice( CommonUtil.div( totalPrice, productNum, 2 ) );//商品价
	result.setProductOldPrice( oldPrice );//商品原价
	result.setProductNum( productNum );//商品数量
	result.setIsCanUseDiscount( CommonUtil.toInteger( product.getIsMemberDiscount() ) );//是否能使用会员折扣
	result.setIsCanUseYhq( CommonUtil.toInteger( product.getIsCoupons() ) );//是否能使用优惠券
	result.setIsCanUseJifen( CommonUtil.toInteger( product.getIsIntegralDeduction() ) );//是否能使用积分抵扣
	result.setIsCanUseFenbi( CommonUtil.toInteger( product.getIsFenbiDeduction() ) );//是否能使用粉币抵扣
	if ( CommonUtil.isNotEmpty( product.getProTypeId() ) ) {
	    result.setProTypeId( CommonUtil.toInteger( product.getProTypeId() ) );
	}
	result.setOrderDetailId( detail.getId() );
	result.setSaleMemberId( detail.getSaleMemberId() );
	result.setCommission( CommonUtil.toDouble( detail.getCommission() ) );
	return result;
    }

}
