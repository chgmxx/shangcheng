package com.gt.mall.service.web.order.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.util.KeysUtil;
import com.gt.entityBo.MallEntity;
import com.gt.entityBo.MallShopEntity;
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
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.freight.PhoneFreightDTO;
import com.gt.mall.param.phone.freight.PhoneFreightShopDTO;
import com.gt.mall.param.phone.order.*;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;
import com.gt.mall.result.phone.order.PhoneToOrderBusResult;
import com.gt.mall.result.phone.order.PhoneToOrderProductResult;
import com.gt.mall.result.phone.order.PhoneToOrderResult;
import com.gt.mall.result.phone.order.PhoneToOrderShopResult;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.web.applet.MallNewOrderAppletService;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderNewService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.ToOrderUtil;
import com.gt.util.entity.param.pay.SubQrPayParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 商城订单 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderNewServiceImpl extends BaseServiceImpl< MallOrderDAO,MallOrder > implements MallOrderNewService {

    private static Logger logger = LoggerFactory.getLogger( MallOrderNewServiceImpl.class );

    @Autowired
    private MallOrderDAO              mallOrderDAO;
    @Autowired
    private MemberPayService          memberPayService;
    @Autowired
    private MallProductService        mallProductService;
    @Autowired
    private MallNewOrderAppletService mallNewOrderAppletService;
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

    @Transactional( rollbackFor = Exception.class )
    @Override
    public Map< String,Object > submitOrder( PhoneAddOrderDTO params, Member member, Integer browser ) throws Exception {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	Map< String,Object > result = new HashMap<>();
	logger.info( "提交订单参数：" + JSONArray.toJSON( params ) );

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
	subQrPayParams.setTotalFee( orderAllMoney );
	subQrPayParams.setModel( Constants.PAY_MODEL );
	subQrPayParams.setBusId( order.getBusUserId() );
	subQrPayParams.setAppidType( 0 );
	    /*subQrPayParams.setAppid( 0 );*///微信支付和支付宝支付可以不传
	subQrPayParams.setOrderNum( orderNo );//订单号
	subQrPayParams.setMemberId( order.getBuyerUserId() );//会员id
	subQrPayParams.setDesc( "商城下单" );//描述
	subQrPayParams.setIsreturn( 1 );//是否需要同步回调(支付成功后页面跳转),1:需要(returnUrl比传),0:不需要(为0时returnUrl不用传)
	String returnUrl = PropertiesUtil.getHomeUrl() + "/phoneOrder/79B4DE7C/orderList.do";
	String sucessUrl = PropertiesUtil.getHomeUrl() + "/phoneOrder/79B4DE7C/paySuccessModified.do";
	if ( order.getOrderPayWay() == 7 ) {
	    sucessUrl = PropertiesUtil.getHomeUrl() + "/phoneOrder/79B4DE7C/daifuSuccess.do";
	    returnUrl = PropertiesUtil.getHomeUrl() + "/phoneOrder/" + order.getId() + "/79B4DE7C/getDaiFu.do";
	}
	subQrPayParams.setReturnUrl( returnUrl );
	subQrPayParams.setNotifyUrl( sucessUrl );//异步回调，注：1、会传out_trade_no--订单号,payType--支付类型(0:微信，1：支付宝2：多粉钱包),2接收到请求处理完成后，必须返回回调结果：code(0:成功,-1:失败),msg(处理结果,如:成功)
	subQrPayParams.setIsSendMessage( 1 );//是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传)
	subQrPayParams.setSendUrl( PropertiesUtil.getHomeUrl() + "mallOrder/toIndex.do" );//推送路径(尽量不要带参数)
	if ( orderPayWay <= 0 ) {
	    orderPayWay = 1;
	    if ( order.getOrderPayWay() == 9 ) {
		orderPayWay = 2;
	    }
	    if ( CommonUtil.isNotEmpty( order.getIsWallet() ) && order.getIsWallet() == 1 ) {
		orderPayWay = 3;
	    }
	}

	subQrPayParams.setPayWay( orderPayWay );//支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付

	KeysUtil keyUtil = new KeysUtil();
	String params = keyUtil.getEncString( JSONObject.toJSONString( subQrPayParams ) );
	return PropertiesUtil.getWxmpDomain() + "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do?obj=" + params;
    }

    private MallOrder getOrderParams( MallOrder mallOrder, int browser, Member member, MallShopEntity shopEntity, MemberAddress memberAddress ) {
	mallOrder.setBuyerUserType( browser );
	mallOrder.setBuyerUserId( member.getId() );
	mallOrder.setBusUserId( member.getBusid() );
	mallOrder.setSellerUserId( member.getPublicId() );
	mallOrder.setOrderStatus( 1 );
	mallOrder.setCreateTime( new Date() );
	mallOrder.setMemberName( member.getNickname() );
	double freightMoney = 0;
	if ( CommonUtil.isNotEmpty( mallOrder.getOrderFreightMoney() ) ) {
	    freightMoney = CommonUtil.toDouble( mallOrder.getOrderFreightMoney() );
	}
	if ( CommonUtil.isNotEmpty( shopEntity ) ) {
	    mallOrder.setOrderMoney( CommonUtil.toBigDecimal( CommonUtil.add( shopEntity.getBalanceMoney(), freightMoney ) ) );
	}
	if ( CommonUtil.isEmpty( mallOrder.getOrderType() ) ) {
	    mallOrder.setOrderType( 0 );
	}
	if ( CommonUtil.isNotEmpty( memberAddress ) ) {
	    mallOrder.setReceiveName( memberAddress.getMemName() );
	    mallOrder.setReceivePhone( memberAddress.getMemPhone() );
	    String address = memberAddress.getProvincename() + memberAddress.getCityname() + memberAddress.getAreaname() + memberAddress.getMemAddress();
	    if ( CommonUtil.isNotEmpty( memberAddress.getMemZipCode() ) ) {
		address += memberAddress.getMemZipCode();
	    }
	    mallOrder.setReceiveAddress( address );

	}
	return mallOrder;
    }

    private MallOrderDetail getOrderDetailParams( MallOrder mallOrder, MallOrderDetail orderDetail, MallShopEntity shopEntity, List< Map > couponList ) {
	MallProduct product = mallProductService.selectByPrimaryKey( orderDetail.getProductId() );
	orderDetail.setReturnDay( product.getReturnDay() );
	orderDetail.setProTypeId( product.getProTypeId() );
	orderDetail.setDetProName( product.getProName() );
	if ( CommonUtil.isNotEmpty( product.getCardType() ) && product.getCardType() > 0 ) {
	    orderDetail.setCardReceiveId( product.getCardType() );
	}
	if ( CommonUtil.isNotEmpty( product.getFlowId() ) && product.getFlowId() > 0 ) {
	    orderDetail.setFlowId( product.getFlowId() );
	}
	if ( CommonUtil.isNotEmpty( product.getFlowRecordId() ) && product.getFlowRecordId() > 0 ) {
	    orderDetail.setFlowRecordId( product.getFlowRecordId() );
	}
	//查询计算后的商品价格
	if ( CommonUtil.isNotEmpty( shopEntity ) ) {
	    Map< Object,MallEntity > malls = shopEntity.getMalls();
	    MallEntity mallEntity = malls.get( orderDetail.getIndex() );
	    if ( CommonUtil.isNotEmpty( mallEntity ) ) {
		orderDetail.setDetProPrice( CommonUtil.toBigDecimal( mallEntity.getUnitPrice() ) );
		orderDetail.setDetPrivivilege( CommonUtil.toBigDecimal( mallEntity.getTotalMoneyOne() ) );
		orderDetail.setDiscountedPrices( CommonUtil.toBigDecimal( CommonUtil.subtract( orderDetail.getTotalPrice(), mallEntity.getBalanceMoney() ) ) );
		orderDetail.setTotalPrice( mallEntity.getBalanceMoney() );

		if ( mallEntity.getUseFenbi() == 1 ) {
		    orderDetail.setUseFenbi( mallEntity.getFenbiNum() );
		    orderDetail.setFenbiYouhui( CommonUtil.toBigDecimal( mallEntity.getDiscountfenbiMoney() ) );
		}
		if ( mallEntity.getUserJifen() == 1 ) {
		    orderDetail.setUseJifen( mallEntity.getJifenNum() );
		    orderDetail.setIntegralYouhui( CommonUtil.toBigDecimal( mallEntity.getDiscountjifenMoney() ) );
		}

		if ( orderDetail.getUseCoupon() == 1 && couponList != null && couponList.size() > 0 ) {
		    orderDetail.setCouponCode( shopEntity.getCodes() );
		    for ( Map couponMap : couponList ) {
			String wxShopId = couponMap.get( "wxShopId" ).toString();
			if ( wxShopId.equals( mallOrder.getWxShopId().toString() ) ) {
			    couponMap.put( "couponCode", shopEntity.getCodes() );
			    orderDetail.setDuofenCoupon( JSONObject.toJSONString( couponMap ) );
			    orderDetail.setUseCardId( CommonUtil.toInteger( couponMap.get( "coupondId" ) ) );
			    break;
			}
		    }
		}
	    }
	}
	return orderDetail;
    }

    /**
     * 拼装会员地址返回值
     *
     * @param addressMap 查询的地址
     *
     * @return 地址
     */
    private PhoneOrderMemberAddressDTO getMemberAddressResult( Map addressMap ) {
	if ( CommonUtil.isEmpty( addressMap ) ) {
	    return null;
	}
	PhoneOrderMemberAddressDTO memberAddress = new PhoneOrderMemberAddressDTO();
	memberAddress.setId( CommonUtil.toInteger( addressMap.get( "id" ) ) );
	memberAddress.setMemberName( addressMap.get( "memName" ).toString() );
	memberAddress.setMemberPhone( addressMap.get( "memPhone" ).toString() );
	String address = addressMap.get( "provincename" ).toString() + addressMap.get( "cityname" ).toString() + addressMap.get( "areaname" ).toString() + addressMap
			.get( "memAddress" ).toString();
	if ( CommonUtil.isNotEmpty( addressMap.get( "memZipCode" ) ) ) {
	    address += addressMap.get( "memZipCode" ).toString();
	}
	memberAddress.setMemberAddress( address );
	return memberAddress;
    }

    @Override
    public PhoneToOrderResult toOrder( PhoneToOrderDTO params, Member member, PhoneLoginDTO loginDTO ) {
	PhoneToOrderResult result = new PhoneToOrderResult();
	String provincesId = "";//省份id
	Double memberLongitude = params.getLongitude();//会员经度
	Double memberLangitude = params.getLangitude();//会员纬度
	if ( CommonUtil.isNotEmpty( params.getMemberAddressId() ) && params.getMemberAddressId() > 0 ) {
	    MemberAddress memberAddress = memberAddressService.addreSelectId( params.getMemberAddressId() );
	    if ( CommonUtil.isNotEmpty( memberAddress ) ) {
		PhoneOrderMemberAddressDTO memberAddressResult = new PhoneOrderMemberAddressDTO();
		memberAddressResult.setId( memberAddress.getId() );
		memberAddressResult.setMemberAddress( memberAddress.getMemAddress() );
		memberAddressResult.setMemberPhone( memberAddress.getMemPhone() );
		memberAddressResult.setMemberName( memberAddress.getMemName() );

		result.setMemberAddressDTO( memberAddressResult );
		provincesId = memberAddress.getMemProvince().toString();
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
		PhoneOrderMemberAddressDTO memberAddress = getMemberAddressResult( addressMap );
		if ( CommonUtil.isNotEmpty( memberAddress ) ) {
		    result.setMemberAddressDTO( memberAddress );
		    provincesId = addressMap.get( "memProvince" ).toString();
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
	    provincesId = mallPageService.getProvince( params.getIp() );
	}
	int proTypeId = 0;//商品类型 0 实体物品 > 0 虚拟物品
	List< Map< String,Object > > mallShopList = new ArrayList<>();//商城店铺集合
	List< Integer > busUserList = new ArrayList<>();//保存商家id集合
	List< Integer > shopList = new ArrayList<>();//保存店铺id集合
	if ( params.getFrom() == 1 && CommonUtil.isNotEmpty( params.getCartIds() ) ) {//购物车
	    Map< String,Object > shopcartParams = new HashMap<>();
	    shopcartParams.put( "checkIds", params.getCartIds().split( "," ) );
	    List< Map< String,Object > > shopCartList = mallShopCartDAO.selectShopCartByCheckIds( shopcartParams );
	    if ( shopCartList != null && shopCartList.size() > 0 ) {

		List< PhoneToOrderProductResult > productResultList = new ArrayList<>();
		for ( Map< String,Object > cartMap : shopCartList ) {
		    PhoneToOrderProductResult productResult = getToOrderProductParams( cartMap );//重组商品对象
		    proTypeId = productResult.getProTypeId();
		    productResultList.add( productResult );
		    if ( !shopList.contains( productResult.getShopId() ) ) {
			shopList.add( productResult.getShopId() );
		    }
		    if ( !busUserList.contains( productResult.getBusUserId() ) ) {
			busUserList.add( productResult.getBusUserId() );
		    }

		}
		List< MallFreight > freightList = mallFreightService.selectFreightByShopIdList( shopList );

		if ( busUserList != null && busUserList.size() > 0 ) {
		    int busId = busUserList.get( 0 );
		    mallShopList = mallStoreService.findShopByUserIdAndShops( busId, shopList );//查询店铺信息
		}

		result = getToOrderParams( productResultList, busUserList, freightList, mallShopList, params, result, provincesId, memberLongitude, memberLangitude );
	    }
	} else if ( CommonUtil.isNotEmpty( params.getProductId() ) ) {//立即购买
	    DecimalFormat df = new DecimalFormat( "######0.00" );
	    //查询商品信息
	    MallProduct product = mallProductService.selectById( params.getProductId() );
	    busUserList.add( product.getUserId() );
	    shopList.add( product.getShopId() );
	    proTypeId = product.getProTypeId();

	    //查询公众号名称或商家名称以及图片
	    List< PhoneToOrderBusResult > busResultList = new ArrayList<>();//返回给页面的商家接话
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
	    PhoneToOrderProductResult productResult = getToOrderProductParams( product, params );//重组商品参数
	    newProductResultList.add( productResult );
	    double totalPrice = productResult.getTotalPrice();

	    PhoneFreightDTO paramsDto = new PhoneFreightDTO();//运费传参
	    paramsDto.setProvinceId( CommonUtil.toInteger( provincesId ) );
	    paramsDto.setToshop( params.getToShop() );
	    paramsDto.setJuli( CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude ) );
	    PhoneFreightShopDTO freightShopDTO = new PhoneFreightShopDTO();//运费店铺传参
	    freightShopDTO.setProTypeId( productResult.getProTypeId() );
	    freightShopDTO.setShopId( shopResult.getShopId() );
	    freightShopDTO.setTotalProductNum( productResult.getProductNum() );
	    freightShopDTO.setTotalProductPrice( totalPrice );
	    freightShopDTO.setTotalProductWeight( productResult.getProductWeight() );
	    double freightMoney = mallFreightService.getFreightMoneyByShopList( null, paramsDto, freightShopDTO ); //计算运费
	    if ( freightMoney > 0 ) {
		shopResult.setTotalFreightMoney( freightMoney );//店铺下的运费
	    }
	    shopResult.setTotalNum( productResult.getProductNum() );//店铺下的商品总数
	    shopResult.setTotalMoney( totalPrice );//店铺下的商品总金额
	    shopResult.setProductResultList( newProductResultList );
	    shopResultList.add( shopResult );
	    double orderMoney = CommonUtil.toDouble( df.format( freightMoney + totalPrice ) );
	    busResult.setTotalNum( productResult.getProductNum() );//商家下的商品总数
	    busResult.setProductTotalMoney( totalPrice );//商家下的商品总额
	    busResult.setProductFreightMoney( freightMoney );//总运费
	    busResult.setTotalMoney( orderMoney );//商家下的商品总额
	    busResult.setShopResultList( shopResultList );
	    busResultList.add( busResult );

	    result.setTotalPayMoney( orderMoney );//订单支付总价（包含运费）
	    result.setTotalMoney( orderMoney );//订单优惠前的总价（包含运费）
	    result.setBusResultList( busResultList );

	}
	result = getToOrderResult( mallShopList, member, busUserList, result, loginDTO.getBrowerType(), params, proTypeId, provincesId );
	logger.info( "result=======" + JSON.toJSONString( result ) );
	return result;
    }

    private PhoneToOrderResult getToOrderResult( List< Map< String,Object > > mallShopList, Member member, List< Integer > busUserList, PhoneToOrderResult result,
		    Integer browerType, PhoneToOrderDTO params, int proTypeId, String provincesId ) {
	Map cardMap = null;
	if ( mallShopList != null && mallShopList.size() > 0 ) {
	    StringBuilder wxShopIds = new StringBuilder( "," );
	    for ( Map< String,Object > maps : mallShopList ) {
		if ( CommonUtil.isNotEmpty( maps.get( "wxShopId" ) ) && !wxShopIds.toString().contains( "," + maps.get( "wxShopId" ) + "," ) ) {
		    wxShopIds.append( maps.get( "wxShopId" ).toString() ).append( "," );
		}
	    }
	    wxShopIds = new StringBuilder( wxShopIds.substring( 1, wxShopIds.length() - 1 ) );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		cardMap = memberService.findCardAndShopIdsByMembeId( member.getId(), wxShopIds.toString() );
	    }
	    logger.info( "cardMap:" + cardMap );
	}
	double discount = 0;
	if ( CommonUtil.isNotEmpty( cardMap.get( "ctId" ) ) && "2".equals( cardMap.get( "ctId" ).toString() ) ) {
	    discount = CommonUtil.toDouble( cardMap.get( "discount" ) );
	}
	JifenAndFenbiRule jifenFenbiRule = memberService.jifenAndFenbiRule( busUserList.get( 0 ) );//通过商家id查询积分和粉币规则
	List< MallPaySet > mallPaySetList = mallPaySetService.selectByUserIdList( busUserList );//通过商家集合查询商城设置
	List< Map< String,Object > > isShowTake = mallTakeTheirService.isTakeTheirByUserIdList( mallPaySetList, provincesId );//查询是否开启到店自提
	int isDaodianPay = 0;//是否显示到店支付
	List< PhoneOrderUserDTO > orderUserDTOList = new ArrayList<>();//集合用来算支付方式的
	//循环结果，查询商家的支付方式和配送方式
	if ( CommonUtil.isNotEmpty( result ) && CommonUtil.isNotEmpty( result.getBusResultList() ) ) {
	    for ( PhoneToOrderBusResult busResult : result.getBusResultList() ) {
		busResult.setJifenFenbiRule( jifenFenbiRule );

		int takeId = 0;//上门自提id
		String takeAddress = "";//上门自提地址
		int isStorePay = 0;
		if ( isShowTake != null && isShowTake.size() > 0 ) {
		    for ( Map< String,Object > isShowMap : isShowTake ) {
			if ( CommonUtil.toString( busResult.getBusId() ).equals( isShowMap.get( "user_id" ).toString() ) ) {
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
		List< PhoneOrderWayDTO > deliveryResultList = ToOrderUtil.getDeliveryWay( params, proTypeId, takeId, busResult.getBusId() );
		busResult.setDeliveryWayList( deliveryResultList );

		busResult.setMemberDiscount( discount );
		double fenbiProductMoney = 0;//能够粉币抵扣的商品金额
		double jifenProductMoney = 0;//能够积分抵扣的商品金额
		for ( PhoneToOrderShopResult shopResult : busResult.getShopResultList() ) {
		    List< Coupons > couponsList = new ArrayList<>();
		    //多粉优惠券
		    if ( cardMap.containsKey( "duofenCards" + shopResult.getWxShopId() ) ) {
			Object obj = cardMap.get( "duofenCards" + shopResult.getWxShopId() );
			couponsList = ToOrderUtil.getDuofenCouponsResult( obj, couponsList );
		    }
		    //微信优惠券
		    if ( cardMap.containsKey( "cardList" + shopResult.getWxShopId() ) ) {
			Object obj = cardMap.get( "cardList" + shopResult.getWxShopId() );
			couponsList = ToOrderUtil.getWxCouponsResult( obj, couponsList );
		    }
		    shopResult.setCouponList( couponsList );

		    for ( PhoneToOrderProductResult productResult : shopResult.getProductResultList() ) {
			if ( productResult.getIsCanUseJifen() == 1 ) {//是否能使用积分
			    jifenProductMoney += productResult.getTotalPrice(); //把能使用积分的商品金额保存起来
			}
			if ( productResult.getIsCanUseFenbi() == 1 ) {
			    fenbiProductMoney += productResult.getTotalPrice();//把能使用粉币的商品金额保存起来
			}
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
		phoneOrderUserDTO.setPublicId( busResult.getPublicId() );
		phoneOrderUserDTO.setBusId( busResult.getBusId() );
		orderUserDTOList.add( phoneOrderUserDTO );
	    }
	    //获取商家的支付方式
	    List< PhoneOrderWayDTO > wayResultList = ToOrderUtil.getPayWay( browerType, orderUserDTOList, params, isDaodianPay, mallPaySetList, proTypeId );
	    result.setPayWayList( wayResultList );
	}
	return result;
    }

    /**
     * 重组订单参数
     */
    private PhoneToOrderResult getToOrderParams( List< PhoneToOrderProductResult > productResultList, List< Integer > busUserList, List< MallFreight > freightList,
		    List< Map< String,Object > > mallShopList,
		    PhoneToOrderDTO params, PhoneToOrderResult result, String provincesId, Double memberLongitude, Double memberLangitude ) {
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
		double productWeight = 0;//商品重量
		int proTypeId = 0;//商品类型id

		List< PhoneToOrderProductResult > newProductResultList = new ArrayList<>();
		for ( PhoneToOrderProductResult productResult : productResultList ) {//循环商品集合
		    if ( productResult.getShopId() == shopResult.getShopId() ) {
			newProductResultList.add( productResult );
			totalNum += productResult.getProductNum();
			totalPrice += productResult.getTotalPrice();
			productWeight += productResult.getProductWeight();
			proTypeId = productResult.getProTypeId();
		    }
		}
		if ( newProductResultList == null || newProductResultList.size() == 0 ) {
		    continue;
		}

		PhoneFreightDTO paramsDto = new PhoneFreightDTO();//运费传参
		paramsDto.setProvinceId( CommonUtil.toInteger( provincesId ) );
		paramsDto.setToshop( params.getToShop() );
		paramsDto.setJuli( CommonUtil.getRaill( storeMap, memberLangitude, memberLongitude ) );
		PhoneFreightShopDTO freightShopDTO = new PhoneFreightShopDTO();//运费店铺传参
		freightShopDTO.setProTypeId( proTypeId );
		freightShopDTO.setShopId( shopResult.getShopId() );
		freightShopDTO.setTotalProductNum( totalNum );
		freightShopDTO.setTotalProductPrice( totalPrice );
		freightShopDTO.setTotalProductWeight( productWeight );
		double freightMoney = mallFreightService.getFreightMoneyByShopList( freightList, paramsDto, freightShopDTO );
		if ( freightMoney > 0 ) {
		    shopResult.setTotalFreightMoney( freightMoney );
		    busFreightPrice += freightMoney;
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
    private PhoneToOrderProductResult getToOrderProductParams( Map< String,Object > cartMap ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	int product_id = CommonUtil.toInteger( cartMap.get( "product_id" ) );
	int shop_id = CommonUtil.toInteger( cartMap.get( "shop_id" ) );
	double price = CommonUtil.toDouble( cartMap.get( "price" ) );
	double oldPrice = price;
	PhoneToOrderProductResult result = new PhoneToOrderProductResult();
	result.setProductId( product_id );//商品id
	result.setShopId( shop_id );//店铺id
	result.setBusUserId( CommonUtil.toInteger( cartMap.get( "bus_user_id" ) ) );//商家id
	result.setProductName( CommonUtil.toString( cartMap.get( "pro_name" ) ) );//商品名称
	result.setProductImageUrl( CommonUtil.toString( cartMap.get( "image_url" ) ) );//商品图片
	if ( CommonUtil.isNotEmpty( cartMap.get( "is_specifica" ) ) && cartMap.get( "is_specifica" ).toString().equals( "1" ) && CommonUtil
			.isNotEmpty( cartMap.get( "product_specificas" ) ) ) {
	    //获取商品规格和规格价
	    Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( cartMap.get( "product_specificas" ).toString(), product_id );
	    if ( CommonUtil.isNotEmpty( invMap ) ) {
		result.setProductSpecificaValue( CommonUtil.toString( invMap.get( "specifica_values" ) ).replace( ",", "/" ) );

		price = CommonUtil.toDouble( invMap.get( "inv_price" ) );//商品规格价
	    }
	}
	int productNum = CommonUtil.toInteger( cartMap.get( "product_num" ) );
	double totalPrice = price * productNum;
	if ( !cartMap.get( "pro_type" ).toString().equals( "0" ) ) {//查询批发商品
	    MallPifa pifa = mallPifaService.selectBuyByProductId( product_id, shop_id, 0 );
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
			if ( obj.getString( "isCheck" ).equals( "1" ) ) {
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
	if ( CommonUtil.isNotEmpty( cartMap.get( "pro_weight" ) ) ) {
	    result.setProductWeight( CommonUtil.toDouble( cartMap.get( "pro_weight" ) ) );//商品重量
	}
	if ( CommonUtil.isNotEmpty( cartMap.get( "pro_type_id" ) ) ) {
	    result.setProTypeId( CommonUtil.toInteger( cartMap.get( "pro_type_id" ) ) );
	}
	return result;
    }

    /**
     * 重组订单商品参数
     */
    private PhoneToOrderProductResult getToOrderProductParams( MallProduct product, PhoneToOrderDTO params ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	int productNum = params.getProductNum();
	int product_id = product.getId();
	double price = CommonUtil.toDouble( product.getProPrice() );
	double oldPrice = price;
	PhoneToOrderProductResult result = new PhoneToOrderProductResult();
	result.setProductId( product_id );//商品id
	result.setShopId( product.getShopId() );//店铺id
	result.setBusUserId( product.getUserId() );//商家id
	result.setProductName( product.getProName() );//商品名称
	Map< String,Object > imageMap = new HashMap<>();
	imageMap.put( "assId", product.getId() );
	imageMap.put( "isMainImages", 1 );
	imageMap.put( "assType", 1 );
	List< Map< String,Object > > imageList = mallImageAssociativeDAO.selectByAssId( imageMap );
	if ( imageList != null && imageList.size() > 0 ) {
	    result.setProductImageUrl( CommonUtil.toString( imageList.get( 0 ).get( "image_url" ) ) );//商品图片
	}
	if ( CommonUtil.isNotEmpty( product.getIsSpecifica() ) && product.getIsSpecifica().toString().equals( "1" ) && CommonUtil.isNotEmpty( params.getProductSpecificas() ) ) {
	    //获取商品规格和规格价
	    Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( params.getProductSpecificas(), product_id );
	    if ( CommonUtil.isNotEmpty( invMap ) ) {
		result.setProductSpecificaValue( CommonUtil.toString( invMap.get( "specifica_values" ) ).replace( ",", "/" ) );
		price = CommonUtil.toDouble( invMap.get( "inv_price" ) );//商品规格价
	    }
	}
	double totalPrice = price * productNum;
	if ( CommonUtil.isNotEmpty( params.getType() ) ) {
	    if ( params.getType() == 7 ) {//查询批发商品
		double pfTotalPrice = 0;
		MallPifa pifa = mallPifaService.selectBuyByProductId( product_id, product.getShopId(), 0 );
		if ( CommonUtil.isNotEmpty( pifa ) ) {
		    pfTotalPrice = CommonUtil.toDouble( pifa.getPfPrice() );
		    if ( CommonUtil.isNotEmpty( params.getPifaSpecificaDTOList() ) && params.getPifaSpecificaDTOList().size() > 0 ) {
			List< PhoneOrderPifaSpecDTO > pfSpecResultsList = mallPifaService
					.getPifaPrice( product.getId(), product.getShopId(), pifa.getId(), params.getPifaSpecificaDTOList() );
			result.setPfSpecResultList( pfSpecResultsList );
			productNum = 0;
			for ( PhoneOrderPifaSpecDTO pfSpecResult : pfSpecResultsList ) {
			    productNum += pfSpecResult.getTotalNum();
			    pfTotalPrice += pfSpecResult.getPfPrice() * pfSpecResult.getTotalNum();
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
	if ( CommonUtil.isNotEmpty( product.getProWeight() ) ) {
	    result.setProductWeight( CommonUtil.toDouble( product.getProWeight() ) );//商品重量
	}
	if ( CommonUtil.isNotEmpty( product.getProTypeId() ) ) {
	    result.setProTypeId( CommonUtil.toInteger( product.getProTypeId() ) );
	}
	return result;
    }

}
