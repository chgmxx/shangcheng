package com.gt.mall.service.web.common.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.util.DateTimeKitUtils;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.order.PhoneOrderMemberAddressDTO;
import com.gt.mall.param.phone.order.PhoneOrderPifaSpecDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderBusDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderProductDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderShopDTO;
import com.gt.mall.result.phone.order.PhoneToOrderBusResult;
import com.gt.mall.service.inter.member.CardService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.common.MallMemberAddressService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import com.gt.mall.utils.AddOrderUtil;
import com.gt.mall.utils.CommonUtil;
import com.gt.util.entity.param.fenbiFlow.BusFlowInfo;
import com.gt.util.entity.param.fenbiFlow.ReqGetMobileInfo;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.result.fenbi.GetMobileInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 公共服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCommonServiceImpl implements MallCommonService {

    private Logger logger = Logger.getLogger( MallCommonServiceImpl.class );

    @Autowired
    private SmsService               smsService;
    @Autowired
    private BusUserService           busUserService;
    @Autowired
    private WxPublicUserService      wxPublicUserService;
    @Autowired
    private MallProductService       mallProductService;
    @Autowired
    private CardService              cardService;
    @Autowired
    private MallAuctionService       mallAuctionService;
    @Autowired
    private MallPresaleService       mallPresaleService;
    @Autowired
    private MallSeckillService       mallSeckillService;
    @Autowired
    private FenBiFlowService         fenBiFlowService;
    @Autowired
    private MallSellerMallsetService mallSellerMallSetService;
    @Autowired
    private MallTakeTheirService     mallTakeTheirService;
    @Autowired
    private MemberAddressService     memberAddressService;
    @Autowired
    private MallMemberAddressService mallMemberAddressService;
    @Autowired
    private MallOrderDAO             mallOrderDAO;

    @Override
    public boolean getValCode( String mobile, Integer busId, String content, String authorizerInfo ) {
	OldApiSms apiSms = new OldApiSms();
	apiSms.setBusId( busId );
	if ( CommonUtil.isNotEmpty( authorizerInfo ) ) {
	    apiSms.setCompany( authorizerInfo );
	}
	apiSms.setContent( content );
	apiSms.setMobiles( mobile );
	apiSms.setModel( CommonUtil.toInteger( Constants.SMS_MODEL ) );

	return smsService.sendSmsOld( apiSms );
    }

    @Override
    public boolean busIsAdvert( int busId ) {
	BusUser user = busUserService.selectById( busId );//根据商家id查询商家信息
	if ( CommonUtil.isNotEmpty( user ) ) {
	    if ( CommonUtil.isNotEmpty( user.getAdvert() ) ) {
		if ( user.getAdvert() == 0 ) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public PhoneToOrderBusResult getBusUserNameOrImage( int busId ) {
	PhoneToOrderBusResult busResult = new PhoneToOrderBusResult();
	WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( busId );//查询公众号信息
	if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
	    busResult.setPublicId( wxPublicUsers.getId() );//公众号id
	    busResult.setBusName( wxPublicUsers.getAuthorizerInfo() );//公众号名称
	    busResult.setBusImageUrl( wxPublicUsers.getHeadImg() );//公众号头像
	}
	if ( CommonUtil.isEmpty( busResult.getBusName() ) ) {
	    BusUser user = busUserService.selectById( busId );//查询商家信息
	    if ( CommonUtil.isNotEmpty( user ) ) {
		busResult.setBusName( user.getName() );
	    }
	}
	busResult.setBusId( busId );
	return busResult;
    }

    /**
     * 判断订单传参是否完整
     */
    @Override
    public PhoneAddOrderDTO isOrderParams( PhoneAddOrderDTO params, Member member ) {
	if ( CommonUtil.isNotEmpty( params.getOrderId() ) && params.getOrderId() > 0 ) {
	    MallOrder mallOrder = mallOrderDAO.selectById( params.getOrderId() );
	    if ( mallOrder.getOrderStatus() > 1 ) {
		throw new BusinessException( ResponseEnums.ORDER_PAY_ERROR.getCode(), ResponseEnums.ORDER_PAY_ERROR.getDesc() );
	    }
	}
	if ( CommonUtil.isEmpty( params.getBusResultList() ) ) {
	    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
	}
	if ( CommonUtil.isEmpty( params.getSelectPayWayId() ) || params.getSelectPayWayId() == 0 ) {
	    throw new BusinessException( ResponseEnums.NO_SELECT_PAY_WAY_ERROR.getCode(), ResponseEnums.NO_SELECT_PAY_WAY_ERROR.getDesc() );
	}
	int selectPayWayId = params.getSelectPayWayId();//支付方式
	int addressId = 0;
	if ( CommonUtil.isNotEmpty( params.getSelectMemberAddressId() ) ) {
	    addressId = params.getSelectMemberAddressId();
	}
	int orderType = 0; // 订单类型 1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品
	if ( CommonUtil.isNotEmpty( params.getOrderType() ) ) {
	    orderType = params.getOrderType();
	}

	StringBuilder wxShopIds = new StringBuilder( "," );
	String busIds;
	Map< Integer,Object > productMap = params.getProductMap();
	StringBuilder busIdsBuilder = new StringBuilder( "," );
	for ( PhoneAddOrderBusDTO busDTO : params.getBusResultList() ) {//循环商家集合
	    if ( CommonUtil.isEmpty( busDTO.getShopResultList() ) ) {
		throw new BusinessException( ResponseEnums.ORDER_PARAMS_ERROR.getCode(), ResponseEnums.ORDER_PARAMS_ERROR.getDesc() );
	    }
	    if ( CommonUtil.isEmpty( busDTO.getSelectDeliveryWayId() ) || busDTO.getSelectDeliveryWayId() == 0 ) {
		throw new BusinessException( ResponseEnums.NO_SELECT_DELIVERY_WAY.getCode(), ResponseEnums.NO_SELECT_DELIVERY_WAY.getDesc() );
	    }
	    AddOrderUtil.isJiFenOrFenbi( selectPayWayId, member, busDTO.getProductTotalMoney() );//判断粉币积分是否足够  ，不够 抛异常
	    int deliverWayId = busDTO.getSelectDeliveryWayId();
	    //上门自提，判断是否传了提货人信息
	    if ( deliverWayId == 2 && ( CommonUtil.isEmpty( busDTO.getAppointmentId() ) || CommonUtil.isEmpty( busDTO.getAppointmentUserName() ) || CommonUtil
			    .isEmpty( busDTO.getAppointmentStartTime() ) || CommonUtil.isEmpty( busDTO.getAppointmentEndTime() ) || CommonUtil
			    .isEmpty( busDTO.getAppointmentUserPhone() ) ) ) {
		throw new BusinessException( ResponseEnums.NO_TIHUO_ERROR.getCode(), ResponseEnums.NO_TIHUO_ERROR.getDesc() );
	    }
	    if ( busDTO.getIsSelectCoupons() == 1 || busDTO.getIsSelectDiscount() == 1 || busDTO.getIsSelectFenbi() == 1 || busDTO.getIsSelectJifen() == 1
			    || busDTO.getIsSelectUnion() == 1 ) {
		params.setCalculation( true );
	    }
	    busDTO.setTotalNewMoney( busDTO.getTotalMoney() );
	    if ( CommonUtil.isNotEmpty( busDTO.getBusId() ) && !wxShopIds.toString().contains( "," + busDTO.getBusId() + "," ) ) {
		busIdsBuilder.append( busDTO.getBusId() ).append( "," );
	    }
	    for ( PhoneAddOrderShopDTO shopDTO : busDTO.getShopResultList() ) {//循环店铺集合
		if ( CommonUtil.isEmpty( shopDTO.getSelectCouponsId() ) ) {
		    throw new BusinessException( ResponseEnums.ORDER_PARAMS_ERROR.getCode(), ResponseEnums.ORDER_PARAMS_ERROR.getDesc() );
		}
		if ( CommonUtil.isNotEmpty( shopDTO.getWxShopId() ) && !wxShopIds.toString().contains( "," + shopDTO.getWxShopId() + "," ) ) {
		    wxShopIds.append( shopDTO.getWxShopId() ).append( "," );
		}
		shopDTO.setTotalNewMoney( shopDTO.getTotalMoney() );
		for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		    MallProduct product;
		    if ( CommonUtil.isEmpty( params.getProductMap() ) || !productMap.containsKey( productDTO.getProductId() ) ) {
			product = mallProductService.selectById( productDTO.getProductId() );
			productMap.put( product.getId(), JSONObject.toJSONString( product ) );
		    } else {
			product = JSONObject.parseObject( productMap.get( productDTO.getProductId() ).toString(), MallProduct.class );
		    }
		    productDTO.setIsCanUseDiscount( CommonUtil.toInteger( product.getIsMemberDiscount() ) );//是否能使用会员折扣
		    productDTO.setIsCanUseYhq( CommonUtil.toInteger( product.getIsCoupons() ) );//是否能使用优惠券
		    productDTO.setIsCanUseJifen( CommonUtil.toInteger( product.getIsIntegralDeduction() ) );//是否能使用积分抵扣
		    productDTO.setIsCanUseFenbi( CommonUtil.toInteger( product.getIsFenbiDeduction() ) );//是否能使用粉币抵扣
		    productDTO.setCardReceiveId( product.getCardType() );
		    productDTO.setProduct( product );
		    productDTO.setProductNewTotalPrice( productDTO.getTotalPrice() );
		    productDTO.setProductWeight( CommonUtil.toDouble( product.getProWeight() ) );
		    //		    productDTO.setProductNewOnePrice( productDTO.getProductPrice() );
		    int proTypeId = 0;//商品类型  0,实物商品 1,虚拟商品非会员卡 2虚拟商品（会员卡） 3 虚拟商品（卡券包） 4虚拟商品（流量包）
		    if ( CommonUtil.isNotEmpty( productDTO.getProTypeId() ) ) {
			proTypeId = productDTO.getProTypeId();
		    }
		    productDTO.setProTypeId( proTypeId );
		    //选择快递配送，商品是实物 且  没传地址
		    if ( deliverWayId == 1 && addressId == 0 && ( CommonUtil.isEmpty( productDTO.getProTypeId() ) || proTypeId == 0 ) ) {//1 快递配送
			throw new BusinessException( ResponseEnums.NO_SELECT_SHOUHUO_ADDRESS.getCode(), ResponseEnums.NO_SELECT_SHOUHUO_ADDRESS.getDesc() );
		    }
		    //以下是判断商品库存的
		    if ( orderType == 7 && null != productDTO.getPfSpecResultList()
				    && productDTO.getPfSpecResultList().size() > 0 ) { //判断批发商品的库存是否足够，不够抛异常
			for ( PhoneOrderPifaSpecDTO pifaSpecDTO : productDTO.getPfSpecResultList() ) {
			    mallProductService.calculateInventory( productDTO.getProductId(), pifaSpecDTO.getSpecificaIds(), pifaSpecDTO.getTotalNum(), member.getId() );
			}
		    } else if ( orderType == 4 ) {//拍卖判断库存  ，超过了库存会抛异常
			mallAuctionService.isMaxNum( productDTO.getActivityId(), member.getId().toString() );
		    } else if ( orderType == 6 ) {//商品预售  ，超过了库存会抛异常
			mallPresaleService.isMaxNum( productDTO.getActivityId(), member.getId().toString(), productDTO.getProductSpecificaValue(), productDTO.getProductNum() );
		    } else if ( orderType == 3 ) {//秒杀  ，超过了库存会抛异常
			mallSeckillService.isInvNum( productDTO.getActivityId(), productDTO.getProductSpecificaValue(), productDTO.getProductNum() );
		    } else {//判断普通商品的库存是否足够，不够抛异常
			mallProductService.calculateInventory( productDTO.getProductId(), productDTO.getProductSpecificaValue(), productDTO.getProductNum(), member.getId() );
		    }

		    //卡全包购买判断是否已经过期
		    if ( proTypeId == 3 && CommonUtil.isNotEmpty( productDTO.getCardReceiveId() ) ) {
			Map< String,Object > cardMap = cardService.findDuofenCardByReceiveId( product.getCardType() );
			if ( CommonUtil.isNotEmpty( cardMap ) ) {
			    if ( CommonUtil.isNotEmpty( cardMap.get( "recevieMap" ) ) ) {
				JSONObject cardObj = JSONObject.parseObject( cardMap.get( "recevieMap" ).toString() );
				if ( CommonUtil.isNotEmpty( cardObj.get( "guoqi" ) ) && "1".equals( cardObj.getString( "guoqi" ) ) ) {
				    throw new BusinessException( ResponseEnums.CAR_RECEVIE_GUOQI_ERROR.getCode(), ResponseEnums.CAR_RECEVIE_GUOQI_ERROR.getDesc() );
				}
			    }
			}
		    }

		    if ( "4".equals( product.getProTypeId().toString() ) && CommonUtil.isEmpty( params.getFlowPhone() ) ) {
			throw new BusinessException( ResponseEnums.FLOW_NO_PHONE_ERROR.getCode(), ResponseEnums.FLOW_NO_PHONE_ERROR.getDesc() );
		    }
		    if ( "4".equals( product.getProTypeId().toString() ) && CommonUtil.isNotEmpty( params.getFlowPhone() ) ) {//流量充值，判断手机号码
			ReqGetMobileInfo reqGetMobileInfo = new ReqGetMobileInfo();
			reqGetMobileInfo.setPhone( params.getFlowPhone() );
			Map map = fenBiFlowService.getMobileInfo( reqGetMobileInfo );
			if ( "-1".equals( map.get( "code" ).toString() ) ) {
			    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), map.get( "errorMsg" ).toString() );
			} else if ( "1".equals( map.get( "code" ).toString() ) ) {
			    GetMobileInfo mobileInfo = JSONObject.toJavaObject( JSONObject.parseObject( map.get( "data" ).toString() ), GetMobileInfo.class );

			    BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );

			    if ( "中国联通".equals( mobileInfo.getSupplier() ) && flow.getType() == 10 ) {
				throw new BusinessException( ResponseEnums.FLOW_LIANTONG_ERROR.getCode(), ResponseEnums.FLOW_LIANTONG_ERROR.getDesc() );
			    }
			    if ( flow.getCount() == 0 ) {
				throw new BusinessException( ResponseEnums.FLOW_ERROR.getCode(), ResponseEnums.FLOW_ERROR.getDesc() );
			    }
			}
		    }
		}
	    }
	}
	busIds = busIdsBuilder.toString();
	if ( CommonUtil.isNotEmpty( productMap ) && productMap.size() > 0 ) {
	    params.setProductMap( productMap );
	}
	if ( CommonUtil.isNotEmpty( wxShopIds ) && wxShopIds.length() > 1 ) {
	    wxShopIds = new StringBuilder( wxShopIds.substring( 1, wxShopIds.length() - 1 ) );
	    params.setWxShopIds( wxShopIds.toString() );
	}
	if ( CommonUtil.isNotEmpty( busIds ) && busIds.length() > 1 ) {
	    busIds = busIds.substring( 1, busIds.length() - 1 );
	    params.setBusIds( busIds );
	}
	return params;
    }

    @Override
    public MallOrder getOrderParams( PhoneAddOrderShopDTO shopDTO, PhoneAddOrderBusDTO busDTO, PhoneAddOrderDTO params, Member member ) {
	MallOrder order = new MallOrder();
	if ( busDTO.getSelectDeliveryWayId() == 1 && CommonUtil.isNotEmpty( params.getSelectMemberAddressId() ) ) {//快递配送
	    MemberAddress memberAddress = memberAddressService.addreSelectId( params.getSelectMemberAddressId() );
	    if ( CommonUtil.isNotEmpty( memberAddress ) ) {
		PhoneOrderMemberAddressDTO memberAddressResult = mallMemberAddressService.getMemberAddressResult( memberAddress );
		order.setReceiveId( memberAddressResult.getId() );
		order.setReceiveName( memberAddressResult.getMemberName() );
		order.setReceiveAddress( memberAddressResult.getMemberAddress() );
		order.setReceivePhone( memberAddressResult.getMemberPhone() );
	    }
	} else if ( busDTO.getSelectDeliveryWayId() == 2 ) {
	    order.setAppointmentName( busDTO.getAppointmentUserName() );
	    order.setAppointmentTelephone( busDTO.getAppointmentUserPhone() );
	    order.setAppointmentTime( DateTimeKitUtils.parseDate( busDTO.getAppointmentDate(), DateTimeKitUtils.DEFAULT_DATE_FORMAT ) );
	    order.setAppointmentStartTime( busDTO.getAppointmentStartTime() );
	    order.setAppointmentEndTime( busDTO.getAppointmentEndTime() );
	    order.setTakeTheirId( busDTO.getAppointmentId() );
	    MallTakeTheir takeTheir = mallTakeTheirService.selectById( busDTO.getAppointmentId() );
	    if ( CommonUtil.isNotEmpty( takeTheir ) ) {
		order.setAppointmentAddress( takeTheir.getVisitAddressDetail() );
	    }
	}
	order.setOrderMoney( CommonUtil.toBigDecimal( CommonUtil.add( shopDTO.getTotalNewMoney(), shopDTO.getTotalFreightMoney() ) ) );
	order.setOrderFreightMoney( CommonUtil.toBigDecimal( shopDTO.getTotalFreightMoney() ) );
	order.setOrderOldMoney( CommonUtil.toBigDecimal( shopDTO.getTotalMoney() ) );
	order.setOrderBuyerMessage( shopDTO.getBuyerMessage() );
	order.setOrderPayWay( params.getSelectPayWayId() );
	order.setDeliveryMethod( busDTO.getSelectDeliveryWayId() );
	order.setBuyerUserId( member.getId() );//买家id
	order.setBusUserId( member.getBusid() );//商家id
	if ( CommonUtil.isNotEmpty( member.getPublicId() ) ) {
	    order.setSellerUserId( member.getPublicId() );//公众号id
	}
	order.setShopId( shopDTO.getShopId() );
	order.setCreateTime( new Date() );
	order.setFlowRechargeStatus( -1 );//不用充值流量
	if ( CommonUtil.isNotEmpty( params.getFlowPhone() ) ) {
	    order.setFlowPhone( params.getFlowPhone() );
	    order.setFlowRechargeStatus( 0 );
	}
	if ( CommonUtil.isNotEmpty( member.getNickname() ) ) {
	    order.setMemberName( member.getNickname() );
	}
	order.setUseFenbi( shopDTO.getUseFenbi() );
	order.setUseJifen( shopDTO.getUseJifen() );
	order.setCouponId( shopDTO.getSelectCouponsId() );
	order.setCouponType( shopDTO.getSelectCouponsType() );
	order.setCouponUseNum( shopDTO.getSelectCouponsNum() );
	order.setDiscountMoney( shopDTO.getTotalYouhuiMoney() );
	order.setFenbiDiscountMoney( shopDTO.getFenbiYouhuiMoney() );
	order.setJifenDiscountMoney( shopDTO.getJifenYouhuiMoney() );
	if ( busDTO.getIsSelectUnion() == 1 ) {
	    //使用了联盟折扣
	    order.setUnionCardId( busDTO.getUnionCardId() );
	}
	return order;
    }

    /**
     * 获取订单详情的参数
     */
    @Override
    public MallOrderDetail getOrderDetailParams( PhoneAddOrderProductDTO productDTO, PhoneAddOrderDTO params, Double memberDiscount, Integer couponId ) {
	MallProduct product = null;
	if ( CommonUtil.isNotEmpty( params.getProductMap() ) ) {
	    Object obj = params.getProductMap().get( productDTO.getProductId() );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		product = JSONObject.parseObject( obj.toString(), MallProduct.class );
	    }
	}
	MallOrderDetail detail = new MallOrderDetail();
	detail.setProductId( productDTO.getProductId() );//产品id 关联t_mall_product表的id
	detail.setShopId( product.getShopId() );//店铺id 关联t_mall_store表的id
	if ( CommonUtil.isNotEmpty( productDTO.getProductSpecificaValue() ) ) {
	    detail.setProductSpeciname( productDTO.getProductSpecificaValue() );//商品规格值
	    detail.setProductSpecificas( productDTO.getProductSpecificaValueId() );//商品规格Id
	}
	if ( CommonUtil.isNotEmpty( productDTO.getProductImageUrl() ) ) {
	    detail.setProductImageUrl( productDTO.getProductImageUrl() );//商品图片
	}
	detail.setDetProNum( productDTO.getProductNum() );//商品数量
	double price = CommonUtil.div( productDTO.getProductNewTotalPrice(), CommonUtil.toDouble( productDTO.getProductNum() ), 2 );
	detail.setDetProPrice( CommonUtil.toBigDecimal( price ) );//商品实付单价
	detail.setDetProName( product.getProName() );//商品名称
	detail.setDetProCode( product.getProCode() );//商品编码
	double oldPrice = CommonUtil.div( productDTO.getTotalPrice(), CommonUtil.toDouble( productDTO.getProductNum() ), 2 );
	detail.setDetPrivivilege( CommonUtil.toBigDecimal( oldPrice ) );//商品原价
	detail.setReturnDay( product.getReturnDay() );//完成订单后在有效天数内退款
	if ( CommonUtil.isNotEmpty( memberDiscount ) ) {
	    int discount = CommonUtil.toIntegerByDouble( CommonUtil.multiply( memberDiscount, 100 ) );
	    detail.setDiscount( discount );//折扣数
	}
	double youhuiPrice = CommonUtil.getDecimal(
			productDTO.getUseUnionDiscountYouhuiPrice() + productDTO.getUseCouponYouhuiPrice() + productDTO.getUseDiscountYouhuiPrice() + productDTO
					.getUseFenbiYouhuiPrice() + productDTO.getUseJifenYouhuiPrice() );
	detail.setDiscountedPrices( CommonUtil.toBigDecimal( youhuiPrice ) );//商品优惠的总价
	detail.setCreateTime( new Date() );//创建时间
	detail.setProTypeId( product.getProTypeId() );//实否是实物商品（0实物 1虚拟商品非会员卡 2虚拟商品（会员卡））...
	detail.setUseFenbi( productDTO.getUseFenbiNum() );//使用粉币数量
	detail.setUseJifen( productDTO.getUseJifenNum() );//使用积分数量
	detail.setTotalPrice( productDTO.getProductNewTotalPrice() );//商品实付总价
	JSONObject pfSpecObj = new JSONObject();
	//批发规格集合
	if ( productDTO.getPfSpecResultList() != null && productDTO.getPfSpecResultList().size() > 0 ) {
	    for ( PhoneOrderPifaSpecDTO pifaSpecDto : productDTO.getPfSpecResultList() ) {
		JSONObject specObj = new JSONObject();
		specObj.put( "num", pifaSpecDto.getTotalNum() );
		specObj.put( "value", pifaSpecDto.getSpecificaValues() );
		specObj.put( "name", pifaSpecDto.getSpecificaName() );
		specObj.put( "price", pifaSpecDto.getPfPrice() );
		pfSpecObj.put( pifaSpecDto.getSpecificaIds(), specObj );
	    }
	    detail.setProSpecStr( JSON.toJSONString( pfSpecObj ) );//批发规格
	}
	if ( CommonUtil.isNotEmpty( product.getCardType() ) ) {
	    detail.setCardReceiveId( product.getCardType() );//购买卡券包id  关联 t_duofen_card_receive 表的id
	}
	detail.setUseCardId( couponId );
	if ( CommonUtil.isNotEmpty( productDTO.getSaleMemberId() ) && CommonUtil.isNotEmpty( productDTO.getCommission() ) ) {
	    if ( productDTO.getSaleMemberId() > 0 ) {
		//判断销售员是否拥有该商品
		boolean isSeller = mallSellerMallSetService.isSellerProduct( productDTO.getProductId(), productDTO.getSaleMemberId() );
		if ( isSeller ) {
		    detail.setSaleMemberId( productDTO.getSaleMemberId() );
		    detail.setCommission( BigDecimal.valueOf( productDTO.getCommission() ) );
		}
	    }

	}
	if ( CommonUtil.isNotEmpty( productDTO.getUseJifenYouhuiPrice() ) ) {
	    detail.setIntegralYouhui( CommonUtil.toBigDecimal( productDTO.getUseJifenYouhuiPrice() ) );//积分优惠的金额
	}
	if ( CommonUtil.isNotEmpty( productDTO.getUseFenbiYouhuiPrice() ) ) {
	    detail.setFenbiYouhui( CommonUtil.toBigDecimal( productDTO.getUseFenbiYouhuiPrice() ) );//粉币优惠的金额
	}
	if ( CommonUtil.isNotEmpty( productDTO.getUseCouponYouhuiPrice() ) ) {
	    detail.setCouponType( productDTO.getSelectCouponsType() );
	    detail.setUseCardId( productDTO.getSelectCouponsId() );
	}
	if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
	    detail.setFlowId( product.getFlowId() );//流量id
	}
	if ( CommonUtil.isNotEmpty( product.getFlowRecordId() ) ) {
	    detail.setFlowRecordId( product.getFlowRecordId() );//流量冻结id
	}
	return detail;
    }

}
