package com.gt.mall.service.web.common.impl;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.mall.bean.member.Coupons;
import com.gt.mall.bean.member.JifenAndFenbBean;
import com.gt.mall.bean.member.JifenAndFenbiRule;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.param.applet.AppletSubmitOrderDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.service.web.common.MallAppletCalculateService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.ToOrderUtil;
import com.gt.union.api.entity.param.UnionCardDiscountParam;
import com.gt.union.api.entity.result.UnionDiscountResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 会员计算实现类
 * User : yangqian
 * Date : 2017/10/27 0027
 * Time : 17:31
 */
@Service
public class MallAppletCalculateServiceImpl implements MallAppletCalculateService {

    private static Logger logger = LoggerFactory.getLogger( MallAppletCalculateServiceImpl.class );

    @Autowired
    private MemberService    memberService;
    @Autowired
    private UnionCardService unionCardService;

    /**
     * 手机端 提交订单计算
     */
    @Override
    public AppletSubmitOrderDTO calculateOrder( AppletSubmitOrderDTO params, Member member ) {
	boolean isCalculate = true;
	Map cardMap = memberService.findCardAndShopIdsByMembeId( member.getId(), params.getWxShopIds() );
	JifenAndFenbiRule jifenFenbiRule = null;//通过商家id查询积分和粉币规则
	Integer isSelectUnion = params.getIsUseUnion();//是否选中联盟  1选中
	Integer isSelectDiscount = params.getIsUseDiscount();//是否选中会员折扣 1 选中
	Integer isSelectCoupon = params.getIsUseYhq();//是否选中优惠券 1选中
	Integer isSelectJifen = params.getIsUseJifen();//是否选中积分抵扣 1选中
	Integer isSelectFenbi = params.getIsUseFenbi();//是否选中粉币抵扣 1选中
	boolean unionFlag = CommonUtil.isNotEmpty( isSelectUnion ) && "1".equals( isSelectUnion.toString() );
	boolean discountFlag = CommonUtil.isNotEmpty( isSelectDiscount ) && "1".equals( isSelectDiscount.toString() );
	boolean couponFlag = CommonUtil.isNotEmpty( isSelectCoupon ) && "1".equals( isSelectCoupon.toString() );
	boolean jifenFlag = CommonUtil.isNotEmpty( isSelectJifen ) && "1".equals( isSelectJifen.toString() );
	boolean fenbiFlag = CommonUtil.isNotEmpty( isSelectFenbi ) && "1".equals( isSelectFenbi.toString() );
	if ( !unionFlag && !discountFlag && !couponFlag && !jifenFlag && !fenbiFlag ) {
	    return params;
	}
	if ( jifenFlag || fenbiFlag ) {
	    if ( CommonUtil.isNotEmpty( member.getBusid() ) ) {
		jifenFenbiRule = memberService.jifenAndFenbiRule( member.getBusid() );
	    }
	}
	//使用了优惠券查询优惠券信息
	if ( couponFlag ) {
	    for ( MallOrder order : params.getOrderList() ) {
		List< Coupons > couponsList = new ArrayList<>();
		//多粉优惠券
		if ( cardMap.containsKey( "duofenCards" + order.getWxShopId() ) ) {
		    Object obj = cardMap.get( "duofenCards" + order.getWxShopId() );
		    couponsList = ToOrderUtil.getDuofenCouponsResult( obj, couponsList, order.getCouponId() );
		}
		//微信优惠券
		if ( cardMap.containsKey( "cardList" + order.getWxShopId() ) ) {
		    Object obj = cardMap.get( "cardList" + order.getWxShopId() );
		    couponsList = ToOrderUtil.getWxCouponsResult( obj, couponsList, order.getCouponId() );
		}
		if ( couponsList != null && couponsList.size() > 0 ) {
		    for ( int i = 0; i < couponsList.size(); i++ ) {
			Coupons coupons = couponsList.get( i );
			if ( coupons.getId().toString().equals( order.getCouponId().toString() ) && order.getCouponId() > 0 ) {
			    order.setSelectCoupon( coupons );
			    break;
			}
		    }
		}
	    }
	}
	int ctId = 0;
	if ( CommonUtil.isNotEmpty( cardMap ) ) {
	    if ( CommonUtil.isNotEmpty( cardMap.get( "ctId" ) ) ) {
		ctId = CommonUtil.toInteger( cardMap.get( "ctId" ) );
	    }
	}
	if ( ctId != 2 ) {
	    discountFlag = false;
	}
	double memberDiscount = 0;//会员折扣
	if ( CommonUtil.isNotEmpty( cardMap ) && discountFlag ) {
	    if ( CommonUtil.isNotEmpty( cardMap.get( "discount" ) ) ) {
		memberDiscount = CommonUtil.toDouble( cardMap.get( "discount" ) );
	    }
	}
	params.setMemberDiscount( memberDiscount );
	Double memberUnionDiscount = null;//联盟的折扣
	Double busFenbiYouhui = params.getFenbi_money();//保存商家下 粉币优惠的总额
	Double busJifenYouhui = params.getIntegral_money();//保存商家下 积分优惠的总额
	Integer unionCardId = 0;
	if ( unionFlag ) {//使用了联盟 查询联盟信息
	    if ( CommonUtil.isNotEmpty( member.getPhone() ) ) {
		UnionCardDiscountParam unionParams = new UnionCardDiscountParam();
		unionParams.setBusId( member.getBusid() );
		//		unionParams.setMemberId( member.getId() );
		unionParams.setPhone( member.getPhone() );
		UnionDiscountResult unionResult = unionCardService.consumeUnionDiscount( unionParams );//获取联盟折扣
		if ( CommonUtil.isNotEmpty( unionResult ) ) {
		    if ( unionResult.getCode() == 1 ) {
			memberUnionDiscount = unionResult.getDiscount();
			unionCardId = unionResult.getCardId();
		    }
		}
	    }
	}

	//	List< PhoneAddOrderShopDTO > shopDTOList = busDTO.getShopResultList();
	List< MallOrder > orderList = params.getOrderList();

	if ( unionFlag ) {//计算联盟折扣
	    orderList = calculateMemberUnion( orderList, memberUnionDiscount, unionCardId );
	}
	if ( discountFlag ) {//计算会员折扣
	    orderList = calculateMemberDiscount( orderList, memberDiscount, cardMap );
	}
	if ( couponFlag ) {//计算优惠券优惠
	    orderList = calculateMemberCoupon( orderList, discountFlag );
	}
 	if ( fenbiFlag && CommonUtil.isNotEmpty( jifenFenbiRule ) ) {//计算粉币优惠
	    orderList = calculateMemberFenbi( orderList, busFenbiYouhui, jifenFenbiRule, cardMap );
	}
	if ( jifenFlag && CommonUtil.isNotEmpty( jifenFenbiRule ) ) {//计算积分优惠
	    orderList = calculateMemberJifen( orderList, busJifenYouhui, jifenFenbiRule, cardMap );
	}
	//以下参数用于避免 四舍五入 后数值 多0.1或小0.1
	double youhuiTotalMoney = 0;

  	for ( MallOrder order : orderList ) {
	    if ( CommonUtil.isNotEmpty( order.getDiscountMoney() ) ) {
		youhuiTotalMoney = CommonUtil.add( youhuiTotalMoney, order.getDiscountMoney() );
	    }
	}
 	youhuiTotalMoney = CommonUtil.getDecimal( youhuiTotalMoney );
	double youhuiMoney = 0;
	for ( int i = 0; i < orderList.size(); i++ ) {
	    MallOrder order = orderList.get( i );
	    double newOrderMoney = order.getTotalNewMoney();
	    if ( CommonUtil.isNotEmpty( order.getOrderFreightMoney() ) ) {
		newOrderMoney = CommonUtil.add( order.getTotalNewMoney(), CommonUtil.toDouble( order.getOrderFreightMoney() ) );
	    }
	    if ( CommonUtil.isNotEmpty( order.getFenbiDiscountMoney() ) && order.getFenbiDiscountMoney() > 0 ) {
		order.setFenbiDiscountMoney( CommonUtil.getDecimal( order.getFenbiDiscountMoney() ) );
	    }
	    if ( CommonUtil.isNotEmpty( order.getJifenDiscountMoney() ) && order.getJifenDiscountMoney() > 0 ) {
		order.setJifenDiscountMoney( CommonUtil.getDecimal( order.getJifenDiscountMoney() ) );
	    }
	    if ( CommonUtil.isNotEmpty( order.getUseJifen() ) && order.getUseJifen() > 0 ) {
		order.setUseJifen( CommonUtil.getDecimal( order.getUseJifen() ) );
	    }
	    if ( CommonUtil.isNotEmpty( order.getUseFenbi() ) && order.getUseFenbi() > 0 ) {
		order.setUseFenbi( CommonUtil.getDecimal( order.getUseFenbi() ) );
	    }
	    order.setOrderMoney( CommonUtil.toBigDecimal( newOrderMoney ) );
	    if ( CommonUtil.isNotEmpty( order.getDiscountMoney() ) ) {
		logger.error( "总优惠：：：：：：：：：：" + order.getDiscountMoney() );
		if ( i + 1 == orderList.size() ) {
		    order.setDiscountMoney( CommonUtil.subtract( youhuiTotalMoney, youhuiMoney ) );
		}
		order.setDiscountMoney( CommonUtil.getDecimal( order.getDiscountMoney() ) );
		youhuiMoney = CommonUtil.add( youhuiMoney, order.getDiscountMoney() );
	    }
	    logger.error( "商品总数：" + order.getTotalNewMoney() + "使用积分总数：" + order.getUseJifen() + "----使用粉币总数：" + order.getUseFenbi() );
	    double totalFenbiNum = 0, totalJifenNum = 0, fenbiYouhuiTotal = 0, jifenYouhuiTotal = 0, totalProductMoney = 0;
	    for ( int j = 0; j < order.getMallOrderDetail().size(); j++ ) {
		MallOrderDetail detail = order.getMallOrderDetail().get( j );

		double newTotalPrice = detail.getNewTotalPrice();
		if ( j + 1 == order.getMallOrderDetail().size() ) {
		    newTotalPrice = CommonUtil.subtract( order.getTotalNewMoney(), totalProductMoney );
		}
		detail.setTotalPrice( CommonUtil.getDecimal( newTotalPrice ) );
		totalProductMoney = CommonUtil.add( totalProductMoney, detail.getTotalPrice() );

		detail.setDetPrivivilege( detail.getDetProPrice() );
		detail.setDetProPrice( CommonUtil.toBigDecimal( CommonUtil.div( detail.getTotalPrice(), detail.getDetProNum(), 2 ) ) );
		if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
		    double discountPrice = CommonUtil.toDouble( detail.getDiscountedPrices() );
		    detail.setDiscountedPrices( CommonUtil.toBigDecimal( CommonUtil.getDecimal( discountPrice ) ) );
		}
		if ( CommonUtil.isNotEmpty( detail.getUseJifen() ) && detail.getUseJifen() > 0 ) {
		    if ( j + 1 == order.getMallOrderDetail().size() ) {
			detail.setUseJifen( CommonUtil.subtract( order.getUseJifen(), totalJifenNum ) );
		    }
		    detail.setUseJifen( CommonUtil.getDecimal( detail.getUseJifen() ) );
		    totalJifenNum = CommonUtil.add( totalJifenNum, detail.getUseJifen() );
		}
		if ( CommonUtil.isNotEmpty( detail.getUseFenbi() ) && detail.getUseFenbi() > 0 ) {
		    if ( j + 1 == order.getMallOrderDetail().size() ) {
			detail.setUseFenbi( CommonUtil.subtract( order.getUseFenbi(), totalFenbiNum ) );
		    }
		    detail.setUseFenbi( CommonUtil.getDecimal( detail.getUseFenbi() ) );
		    totalFenbiNum = CommonUtil.add( totalFenbiNum, detail.getUseFenbi() );
		}
		if ( CommonUtil.isNotEmpty( detail.getFenbiYouhui() ) && CommonUtil.toDouble( detail.getFenbiYouhui() ) > 0 ) {
		    double fenbiYouhui = CommonUtil.toDouble( detail.getFenbiYouhui() );
		    if ( j + 1 == order.getMallOrderDetail().size() ) {
			fenbiYouhui = CommonUtil.subtract( order.getFenbiDiscountMoney(), fenbiYouhuiTotal );
		    }
		    detail.setFenbiYouhui( CommonUtil.toBigDecimal( CommonUtil.getDecimal( fenbiYouhui ) ) );
		    fenbiYouhuiTotal = CommonUtil.add( fenbiYouhuiTotal, CommonUtil.toDouble( detail.getFenbiYouhui() ) );
		}
		if ( CommonUtil.isNotEmpty( detail.getIntegralYouhui() ) && CommonUtil.toDouble( detail.getIntegralYouhui() ) > 0 ) {
		    double jifenYouhui = CommonUtil.toDouble( detail.getIntegralYouhui() );
		    if ( j + 1 == order.getMallOrderDetail().size() ) {
			jifenYouhui = CommonUtil.subtract( order.getJifenDiscountMoney(), jifenYouhuiTotal );
		    }
		    detail.setIntegralYouhui( CommonUtil.toBigDecimal( CommonUtil.getDecimal( jifenYouhui ) ) );
		    jifenYouhuiTotal = CommonUtil.add( jifenYouhuiTotal, CommonUtil.toDouble( detail.getIntegralYouhui() ) );
		}
		//		logger.error( "原价：" + detail.getDetPrivivilege() + "---优惠了：" + detail.getDiscountedPrices() + "----剩余金额：" + CommonUtil.getDecimal( detail.getNewTotalPrice() ) );
		logger.error( "商品总价：" + detail.getTotalPrice() + "使用积分数量：" + detail.getUseJifen() + "---积分总优惠：" + detail.getIntegralYouhui() + "----使用粉币数量：" + detail.getUseFenbi()
				+ "----粉币总优惠：" + detail
				.getFenbiYouhui() );
	    }
	}

	if ( !isCalculate ) {
	    return params;
	}
	logger.info( "优惠后的参数：" + JSON.toJSONString( params ) );
	//	if ( CommonUtil.isNotEmpty( params ) ) {
	//	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	//	}
	return params;
    }

    /**
     * 计算商家下的联盟折扣  的优惠
     *
     * @param orderList           订单集合
     * @param memberUnionDiscount 商家下联盟的折扣
     *
     * @return 店铺集合
     */
    private List< MallOrder > calculateMemberUnion( List< MallOrder > orderList, Double memberUnionDiscount, Integer unionCardId ) {
	//	double busCanUseUnionProductPrice = 0;//保存能使用联盟折扣的商品总额
	//	//保存能使用会员折扣的商品价格
	//	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	//	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
	//		if ( CommonUtil.isNotEmpty( productDTO.getIsCanUseDiscount() ) && "1".equals( productDTO.getIsCanUseDiscount().toString() ) ) {
	//		    //把能使用用联盟折扣的商品 总价 累计起来
	//		    busCanUseUnionProductPrice = CommonUtil.add( busCanUseUnionProductPrice, productDTO.getProductNewTotalPrice() );
	//		}
	//	    }
	//	}
	//	Double busUnionYouhui = null;//保存商家下 联盟优惠的总额
	//	if ( CommonUtil.isNotEmpty( memberUnionDiscount ) && memberUnionDiscount > 0 && memberUnionDiscount < 1 && busCanUseUnionProductPrice > 0 ) {
	//	    double unionYouhuiHouPrice = CommonUtil.multiply( memberUnionDiscount, memberUnionDiscount );
	//	    busUnionYouhui = CommonUtil.subtract( memberUnionDiscount, unionYouhuiHouPrice );
	//	} else {
	//	    return shopDTOList;
	//	}
	//	busCanUseUnionProductPrice = CommonUtil.formatDoubleNumber( busCanUseUnionProductPrice );
	//	double proUnionYouhuiTotal = 0;//已经优惠的联盟总额
	for ( MallOrder order : orderList ) {//循环店铺集合
	    double shopProductNewTotal = 0;//保存订单优惠后的商品总额
	    double totalYouhuiMoney = 0;//保存订单优惠的金额
	    //平摊联盟优惠
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		double productNewTotal = detail.getNewTotalPrice();//商品优惠前的价格
		double discountHouPrice = CommonUtil.multiply( productNewTotal, memberUnionDiscount );//计算商品联盟折扣后的金额
		double productYouhuiPrice = CommonUtil.subtract( productNewTotal, discountHouPrice );//计算联盟折扣优惠的金额 （商品优惠前的总价 -  优惠后的金额）
		/*if ( proUnionYouhuiTotal + productYouhuiPrice <= busUnionYouhuiTotal ) {
		    // 已经优惠的金额 + 商品优惠的金额 小于  联盟总共能优惠的金额   才能把联盟优惠的金额累计起来
		    proUnionYouhuiTotal += productYouhuiPrice;
		} else if ( proUnionYouhuiTotal + productYouhuiPrice > busUnionYouhuiTotal && proUnionYouhuiTotal < busUnionYouhuiTotal ) {
		    //商品优惠的总额 小于 商家优惠的总额
		    productYouhuiPrice = CommonUtil.subtract( busUnionYouhuiTotal, proUnionYouhuiTotal );//商家优惠的总额 - 商品优惠的总额
		    discountHouPrice = CommonUtil.subtract( productNewTotal, productYouhuiPrice );
		    proUnionYouhuiTotal += productYouhuiPrice;
		} else {
		    discountHouPrice = productNewTotal;
		    productYouhuiPrice = 0;
		}*/
		//		if ( productYouhuiPrice > 0 ) {
		//		    productDTO.setUseUnionDiscount( true );
		//		}
		double discount = 0;
		if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
		    discount = CommonUtil.toDouble( detail.getDiscountedPrices() );
		}
		discount = CommonUtil.add( discount, productYouhuiPrice );
		detail.setDiscountedPrices( CommonUtil.toBigDecimal( discount ) );
		detail.setNewTotalPrice( discountHouPrice );
		//		productDTO.setUseUnionDiscountYouhuiPrice( productYouhuiPrice );
		//		productDTO.setProductNewTotalPrice( discountHouPrice );

		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, detail.getNewTotalPrice() );//累加店铺优惠后的金额
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, productYouhuiPrice );
	    }
	    logger.error( "联盟折扣优惠后的价格：" + shopProductNewTotal + "--优惠了：" + totalYouhuiMoney + "元 " );
	    order.setUnionDiscountMoney( totalYouhuiMoney );
	    order.setUnionCardId( unionCardId );
	    order.setDiscountMoney( CommonUtil.add( order.getDiscountMoney(), totalYouhuiMoney ) );
	    order.setTotalNewMoney( shopProductNewTotal );
	    logger.error( "totalYouhuiMoney====联盟优惠========" + totalYouhuiMoney );
	}
	return orderList;
    }

    /**
     * 计算商家下的会员折扣  的优惠
     *
     * @param orderList      店铺集合
     * @param memberDiscount 商家下会员折扣优惠的总额
     *
     * @return 店铺集合
     */
    private List< MallOrder > calculateMemberDiscount( List< MallOrder > orderList, Double memberDiscount, Map cardMap ) {
	double busCanUseDiscountProductPrice = 0;//保存能使用会员折扣的商品总额
	//	List< Coupons > couponsList = new ArrayList<>();
	//保存能使用会员折扣的商品价格
	for ( MallOrder order : orderList ) {//循环店铺集合
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( detail.getIsCanUseDiscount() ) && "1".equals( detail.getIsCanUseDiscount().toString() )
				&& detail.getNewTotalPrice() > 0 ) {
		    //把能使用会员折扣的商品 总价 累计起来
		    busCanUseDiscountProductPrice = CommonUtil.add( busCanUseDiscountProductPrice, detail.getNewTotalPrice() );
		}
	    }
	}
	//	busCanUseDiscountProductPrice = CommonUtil.getDecimalStr( busCanUseDiscountProductPrice, 2 );
	/*Double busDiscountYouhui = null;//保存商家下  折扣卡优惠的总额
	if ( busCanUseDiscountProductPrice > 0 && memberDiscount > 0 && memberDiscount < 1 ) {
	    double discountYouhuiHouPrice = CommonUtil.multiply( busCanUseDiscountProductPrice, memberDiscount );
	    busDiscountYouhui = CommonUtil.subtract( busCanUseDiscountProductPrice, discountYouhuiHouPrice );
	} else {
	    return orderList;
	}*/
	if ( !( busCanUseDiscountProductPrice > 0 && memberDiscount > 0 && memberDiscount < 1 ) ) {
	    return orderList;
	}
	//	double proUnionYouhuiTotal = 0;//已经优惠的联盟总额
	for ( MallOrder order : orderList ) {//循环店铺集合
	    double shopProductNewTotal = 0;//保存订单优惠后的商品总额
	    double totalYouhuiMoney = 0;
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//商品集合
		if ( CommonUtil.isEmpty( detail.getIsCanUseDiscount() ) && !"1".equals( detail.getIsCanUseDiscount().toString() ) ) {
		    shopProductNewTotal = CommonUtil.add( shopProductNewTotal, detail.getNewTotalPrice() );
		    continue;
		}
		double productNewTotal = detail.getNewTotalPrice();//商品优惠前的价格
		double discountHouPrice = CommonUtil.multiply( productNewTotal, memberDiscount );//计算商品会员折扣后的金额
		double productYouhuiPrice = CommonUtil.subtract( productNewTotal, discountHouPrice );//计算会员折扣 优惠的金额  （商品优惠前的总价 -  优惠后的金额）
		   /* if ( proUnionYouhuiTotal + productYouhuiPrice <= busDiscountYouhui ) {
			// 已经优惠的金额 + 商品优惠的金额 小于  联盟总共能优惠的金额   才能把联盟优惠的金额累计起来
			proUnionYouhuiTotal += productYouhuiPrice;
		    } else if ( proUnionYouhuiTotal + productYouhuiPrice > busDiscountYouhui && proUnionYouhuiTotal < busDiscountYouhui ) {
			//商品优惠的总额 小于 商家优惠的总额
			productYouhuiPrice = CommonUtil.subtract( busDiscountYouhui, proUnionYouhuiTotal );//商家优惠的总额 - 商品优惠的总额
			discountHouPrice = CommonUtil.subtract( productNewTotal, productYouhuiPrice );
			proUnionYouhuiTotal += productYouhuiPrice;
		    } else {
			discountHouPrice = productNewTotal;
			productYouhuiPrice = 0;
		    }*/
		double discount = 0;
		if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
		    discount = CommonUtil.toDouble( detail.getDiscountedPrices() );
		}
		discount = CommonUtil.add( discount, productYouhuiPrice );
		detail.setDiscountedPrices( CommonUtil.toBigDecimal( discount ) );
		detail.setNewTotalPrice( discountHouPrice );
		//		detail.setUseDiscountYouhuiPrice( productYouhuiPrice );
		//		detail.setProductNewTotalPrice( discountHouPrice );
		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, detail.getNewTotalPrice() );
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, productYouhuiPrice );

		//		logger.error( "会员折扣优惠后的价格：" + detail.getNewTotalPrice() );
	    }
	    logger.error( "会员折扣优惠后的价格：" + shopProductNewTotal + "--优惠了：" + totalYouhuiMoney + "元 " );
	    order.setDiscountMoney( CommonUtil.add( order.getDiscountMoney(), totalYouhuiMoney ) );
	    order.setTotalNewMoney( shopProductNewTotal );

	    logger.error( "totalYouhuiMoney====会员折扣优惠========" + totalYouhuiMoney );
	}
	return orderList;
    }

    /**
     * 计算商家下的优惠券 的优惠
     *
     * @param orderList    订单集合
     * @param discountFlag 是否计算的会员折扣
     *
     * @return 店铺集合
     */
    private List< MallOrder > calculateMemberCoupon( List< MallOrder > orderList, boolean discountFlag ) {
	for ( MallOrder order : orderList ) {//循环店铺集合
	    Coupons coupons = order.getSelectCoupon();//选中优惠券对象
	    if ( CommonUtil.isEmpty( order.getSelectCoupon() ) ) {
		continue;
	    }
	    double canUseCouponProductPrice = 0;//能使用优惠券的商品金额
	    int canUseCouponProductNum = 0;//能使用优惠券的商品数量
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( detail.getIsCanUseCoupons() ) && "1".equals( detail.getIsCanUseCoupons().toString() ) && detail.getNewTotalPrice() > 0 ) {
		    canUseCouponProductPrice = CommonUtil.add( canUseCouponProductPrice, detail.getNewTotalPrice() );
		    canUseCouponProductNum++;
		}
	    }
	    if ( canUseCouponProductPrice == 0 || canUseCouponProductNum == 0 ) {//能使用优惠券的商品总价和商品总数 = 0  则跳出当前循环
		continue;
	    }
	    //	    canUseCouponProductPrice = CommonUtil.getDecimalStr( canUseCouponProductPrice, 2 );
	    int cardFrom = coupons.getCouponsFrom();//优惠券来源（ 1 微信优惠券  2多粉优惠券 ）
	    int cardType = coupons.getCardType();//卡券类型 0折扣券 1减免券
	    int addUse = coupons.getAddUser();//是否允许叠加 0不允许 1已允许 (多粉优惠券也可以)
	    int couponNum = coupons.getCouponNum();//叠加的数量
	    double shopYouhuiHouTotalPrice = 0;//保存 店铺下 商品优惠后的总额
	    //计算店铺下使用商品优惠券的 优惠总额
	    if ( cardType == 0 && !discountFlag ) {
		//计算店铺下 折扣券 优惠的总额
		double youhuiHouPrice = CommonUtil.multiply( canUseCouponProductPrice, coupons.getDiscount() );//折扣券优惠后的 商品金额
		shopYouhuiHouTotalPrice = CommonUtil.subtract( canUseCouponProductPrice, youhuiHouPrice );//店铺下商品使用优惠券 的优惠总额 = 能使用折扣券的商品总额 - 折扣优惠后的商品金额
	    } else if ( cardType == 1 ) {
		//计算店铺下 减免券 优惠的总额
		shopYouhuiHouTotalPrice = coupons.getReduceCost();//店铺下商品使用优惠券 的优惠总额  = 减免券优惠的总额
		//判断是否多粉优惠券 且可以叠加
		if ( cardFrom == 2 && addUse == 1 && couponNum > 1 ) {
		    if ( coupons.getCashLeastCost() > 0 ) {//使用优惠券的条件
			couponNum = CommonUtil.divInteger( canUseCouponProductPrice, coupons.getCashLeastCost() ); // 优惠券的叠加数量 =  店铺下 能使用优惠券的商品总额 /  使用优惠券的条件
		    } else {
			//优惠的金额  乘以 叠加数量 （优惠的总额） 大于 能使用优惠券的商品数量
			if ( coupons.getReduceCost() * couponNum > canUseCouponProductPrice ) {
			    Double aa = Math.ceil( canUseCouponProductPrice / coupons.getReduceCost() );//优惠券叠加的数量 =  店铺下能使用优惠券的商品总额 / 减免券优惠的金额
			    couponNum = aa.intValue();
			}
		    }
		    shopYouhuiHouTotalPrice = CommonUtil.multiply( coupons.getReduceCost(), couponNum );
		}
		if ( coupons.getCashLeastCost() > canUseCouponProductPrice ) {//满减条件  大于能 使用优惠券的商品金额
		    continue;
		}
		if ( shopYouhuiHouTotalPrice > canUseCouponProductPrice ) {//优惠的总额  大于 能使用优惠券的商品金额，则 优惠券的金额 = 能使用优惠券的商品金额
		    shopYouhuiHouTotalPrice = canUseCouponProductPrice;
		}
	    }
	    if ( shopYouhuiHouTotalPrice == 0 ) {//优惠的金额 = 0 没必要计算
		continue;
	    }
	    double useCouponTotalPrice = 0;//已使用优惠券的商品金额
	    double useCouponTotalNum = 0;//已使用优惠券的商品数量
	    double shopProductNewTotal = 0;//保存订单优惠后的商品总额
	    double totalYouhuiMoney = 0;//保存使用优惠券优惠的金额
	    //平摊 每个商品使用优惠券的 优惠金额
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		boolean isCanUseYhq = CommonUtil.isEmpty( detail.getIsCanUseCoupons() ) || !"1".equals( detail.getIsCanUseCoupons().toString() );
		//已使用优惠券的数量 = 店铺下优惠券优惠的数量  && 已使用优惠券的金额 == 店铺下优惠券优惠的总额
		boolean isEndCanUseYhq = useCouponTotalNum == canUseCouponProductNum && useCouponTotalPrice == shopYouhuiHouTotalPrice;  //店铺下使用优惠的金额 已经分摊完毕
		if ( isCanUseYhq && isEndCanUseYhq ) {
		    shopProductNewTotal = CommonUtil.add( shopProductNewTotal, detail.getNewTotalPrice() );
		    continue;
		}
		//		detail.setCouponCode( coupons.get );
		//		detail.setSelectCouponsType( cardFrom );
		detail.setCouponType( coupons.getCouponsFrom() );
		detail.setUseCardId( coupons.getId() );
		double productTotalPrice = detail.getNewTotalPrice();//商品优惠前的总价
		double productYouHuiHouTotalPrice = productTotalPrice;//商品优惠后的总价
		double couponYouhuiPrice = 0;//优惠券优惠的金额
		//能使用优惠券的商品
		if ( cardType == 0 && !discountFlag ) {
			/* 折扣券的计算 */
		    //计算单个商品优惠的价格（ 折扣券）
		    productYouHuiHouTotalPrice = CommonUtil.multiply( productTotalPrice, coupons.getDiscount() );// 折扣券优惠后的 商品金额
		    couponYouhuiPrice = CommonUtil.subtract( productTotalPrice, productYouHuiHouTotalPrice );//优惠的金额
		} else if ( cardType == 1 ) {
			/* 减免券的计算 */
		    //单个商品的总价/ 店铺下能使用优惠券的商品总价  * 能够优惠的价格 （减免券）
		    couponYouhuiPrice = CommonUtil.multiply( CommonUtil.div( productTotalPrice, canUseCouponProductPrice, 10 ), shopYouhuiHouTotalPrice );
		    productYouHuiHouTotalPrice = CommonUtil.subtract( productTotalPrice, couponYouhuiPrice );
		}
		if ( productYouHuiHouTotalPrice < 0 ) {//商品优惠后的商品总额 小于 0
		    productYouHuiHouTotalPrice = 0;//优惠后的价格  = 0
		    couponYouhuiPrice = productTotalPrice;//商品优惠的金额  = 优惠前的总额
		}
		if ( useCouponTotalNum + 1 == canUseCouponProductNum ) {//最后一个商品
		    couponYouhuiPrice = CommonUtil.subtract( shopYouhuiHouTotalPrice, useCouponTotalPrice );//商品优惠的金额 =  店铺下商品使用优惠券 的优惠总额
		}
		if ( couponYouhuiPrice + useCouponTotalPrice > shopYouhuiHouTotalPrice ) {//商品优惠的金额 + 已经优惠的商品总价 > 店铺下优惠的总额
		    couponYouhuiPrice = CommonUtil.subtract( shopYouhuiHouTotalPrice, useCouponTotalPrice );//商品优惠的金额 = 店铺下优惠的总额 - 已经优惠的总价
		    productYouHuiHouTotalPrice = CommonUtil.subtract( productTotalPrice, couponYouhuiPrice );//商品优惠后的总价 = 商品优惠前的总价 - 商品优惠的金额
		}
		//		productDTO.setUseCouponYouhuiPrice( couponYouhuiPrice );
		//		productDTO.setProductNewTotalPrice( productYouHuiHouTotalPrice );

		double discount = 0;
		if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
		    discount = CommonUtil.toDouble( detail.getDiscountedPrices() );
		}
		discount = CommonUtil.add( discount, couponYouhuiPrice );
		detail.setDiscountedPrices( CommonUtil.toBigDecimal( discount ) );
		detail.setNewTotalPrice( productYouHuiHouTotalPrice );

		useCouponTotalPrice += couponYouhuiPrice;
		useCouponTotalNum++;

		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, detail.getNewTotalPrice() );
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, couponYouhuiPrice );
		logger.error( "优惠券优惠后的价格：" + detail.getNewTotalPrice() + "优惠了：" + couponYouhuiPrice );
	    }
	    logger.error( "优惠券优惠后的价格orders：" + shopProductNewTotal + "--优惠了：" + totalYouhuiMoney + "元 " );
	    order.setCouponId( coupons.getId() );
	    order.setCouponType( coupons.getCouponsFrom() );
	    order.setCouponUseNum( coupons.getCouponNum() > 0 ? coupons.getCouponNum() : 0 );
	    //	    order.setSelectCouponsId( couponsId );
	    //	    order.setSelectCouponsType( cardFrom );
	    //	    order.setSelectCouponsNum( couponNum > 0 ? couponNum : 0 );
	    logger.error( "totalYouhuiMoney====优惠券优惠========" + totalYouhuiMoney );
	    order.setDiscountMoney( CommonUtil.add( order.getDiscountMoney(), totalYouhuiMoney ) );
	    order.setTotalNewMoney( shopProductNewTotal );
	}
	return orderList;
    }

    /**
     * 计算商家下的粉币 的优惠
     *
     * @param orderList      店铺集合
     * @param busFenbiYouhui 商家下 粉币优惠的总额
     *
     * @return 店铺集合
     */
    private List< MallOrder > calculateMemberFenbi( List< MallOrder > orderList, Double busFenbiYouhui, JifenAndFenbiRule jifenFenbiRule, Map cardMap ) {
	double busCanUseFenbiProductPrice = 0;//保存能使用粉币的商品总额
	int busCanUseFenbiProductNum = 0;//保存能使用粉币的商品数量
	//保存能使用会员折扣的商品价格
	for ( MallOrder order : orderList ) {//循环店铺集合
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( detail.getIsCanUseFenbi() ) && "1".equals( detail.getIsCanUseFenbi().toString() )
				&& detail.getNewTotalPrice() > 0 ) {
		    //		    logger.error( "能使用粉币的商品金额-------------" + detail.getNewTotalPrice() );
		    //把能使用粉币的商品 总价 累计起来
		    busCanUseFenbiProductPrice = CommonUtil.add( busCanUseFenbiProductPrice, detail.getNewTotalPrice() );
		    busCanUseFenbiProductNum++;
		}
	    }
	}
	if ( busCanUseFenbiProductPrice == 0 || busCanUseFenbiProductNum == 0 ) {//能使用粉币的商品总价和商品总数 = 0
	    return orderList;
	}
	//	busCanUseFenbiProductPrice = CommonUtil.getDecimalStr( busCanUseFenbiProductPrice, 2 );
	JifenAndFenbBean bean = ToOrderUtil.getJifenFenbiParams( jifenFenbiRule, 0, busCanUseFenbiProductPrice, cardMap );
	if ( bean == null || bean.getFenbiMoney() == 0 || bean.getFenbiNum() == 0 ) {
	    return orderList;
	}
	double useFenbiTotalPrice = 0;//已使用粉币的优惠金额
	double useFenbiTotalNum = 0;//已使用粉币的商品数量
	for ( MallOrder order : orderList ) {//循环店铺集合
	    double useFenbiNum = 0;
	    double shopYouhuiHouTotalPrice = 0;//保存 店铺下 商品优惠后的总额
	    double totalYouhuiMoney = 0;//保存订单下优惠的总额
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		boolean isCanUseCoupon = CommonUtil.isEmpty( detail.getIsCanUseFenbi() ) || !"1".equals( detail.getIsCanUseFenbi().toString() );//是否能使用粉币
		//已优惠粉币的商品数量 = 店铺下能使用粉币优惠的商品数量  && 已使用粉币优惠的 == 已使用粉币的优惠金额
		boolean isEndCanUseFenbi = useFenbiTotalNum == busCanUseFenbiProductNum && useFenbiTotalPrice == busCanUseFenbiProductPrice;
		if ( isCanUseCoupon || isEndCanUseFenbi ) {
		    shopYouhuiHouTotalPrice = CommonUtil.add( shopYouhuiHouTotalPrice, detail.getNewTotalPrice() );
		    continue;
		}

		double productTotalPrice = detail.getNewTotalPrice();//商品优惠前的总价
		double productYouHuiHouTotalPrice;//商品优惠后的总价
		double fenbiYouhuiPrice = 0;//粉币优惠的金额
		if ( useFenbiTotalNum + 1 == busCanUseFenbiProductNum ) {//最后一个能使用粉币的商品
		    fenbiYouhuiPrice = CommonUtil.subtract( bean.getFenbiMoney(), useFenbiTotalPrice );//单个商品使用粉币优惠的金额 =  粉币优惠的总额 - 已使用粉币的优惠金额
		} else {
		    //单个商品使用粉币优惠的金额 = （（商品的总价 / 能使用粉币的商品总价） * 粉币总共能优惠的金额）
		    fenbiYouhuiPrice = CommonUtil.multiply( CommonUtil.div( detail.getNewTotalPrice(), busCanUseFenbiProductPrice, 10 ), bean.getFenbiMoney() );
		    //		    logger.error( "计算：" + detail.getNewTotalPrice() + "/" + busCanUseFenbiProductPrice + "*" + bean.getFenbiMoney() );
		}
		//		if ( productTotalPrice < fenbiYouhuiPrice ) {
		//		    fenbiYouhuiPrice = productTotalPrice;
		//		}
		productYouHuiHouTotalPrice = CommonUtil.subtract( productTotalPrice, fenbiYouhuiPrice );
		if ( productYouHuiHouTotalPrice < 0 ) {
		    productYouHuiHouTotalPrice = 0;
		}

		//		detail.setUseFenbiNum( CommonUtil.multiply( jifenFenbiRule.getFenbiRatio(), fenbiYouhuiPrice ) );
		//		detail.setUseFenbiYouhuiPrice( fenbiYouhuiPrice );
		//		detail.setProductNewTotalPrice( productYouHuiHouTotalPrice );

		double discount = 0;
		if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
		    discount = CommonUtil.toDouble( detail.getDiscountedPrices() );
		}
		discount = CommonUtil.add( discount, fenbiYouhuiPrice );
		detail.setDiscountedPrices( CommonUtil.toBigDecimal( discount ) );
		detail.setNewTotalPrice( productYouHuiHouTotalPrice );
		detail.setUseFenbi( CommonUtil.multiply( jifenFenbiRule.getFenbiRatio(), fenbiYouhuiPrice ) );
		detail.setFenbiYouhui( CommonUtil.toBigDecimal( fenbiYouhuiPrice ) );

		useFenbiTotalPrice += fenbiYouhuiPrice;
		useFenbiTotalNum++;

		shopYouhuiHouTotalPrice = CommonUtil.add( shopYouhuiHouTotalPrice, detail.getNewTotalPrice() );
		useFenbiNum += detail.getUseFenbi();
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, fenbiYouhuiPrice );
		//		logger.error( "粉币优惠后的价格：" + detail.getNewTotalPrice() + "--优惠了：" + fenbiYouhuiPrice);
	    }
	    logger.error( "粉币优惠后的价格：" + shopYouhuiHouTotalPrice + "--优惠了：" + totalYouhuiMoney + "元 -- 使用了：" + useFenbiNum + "粉币" );
	    order.setTotalNewMoney( shopYouhuiHouTotalPrice );
	    order.setUseFenbi( useFenbiNum );
	    //	    order.setFenbiYouhuiMoney( totalYouhuiMoney );
	    order.setFenbiDiscountMoney( totalYouhuiMoney );
	    order.setDiscountMoney( CommonUtil.add( order.getDiscountMoney(), totalYouhuiMoney ) );
	    logger.error( "totalYouhuiMoney====粉币优惠========" + totalYouhuiMoney );
	}
	return orderList;
    }

    /**
     * 计算商家下的积分 的优惠
     *
     * @param orderList      店铺集合
     * @param busJifenYouhui 商家下积分优惠的总额
     *
     * @return 店铺集合
     */
    private List< MallOrder > calculateMemberJifen( List< MallOrder > orderList, Double busJifenYouhui, JifenAndFenbiRule jifenFenbiRule, Map cardMap ) {
	double busCanUseJifenProductPrice = 0;//保存能使用积分的商品总额
	int busCanUseJifenProductNum = 0;//保存能使用积分的商品数量
	//保存能使用会员折扣的商品价格
	for ( MallOrder order : orderList ) {//循环店铺集合
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( detail.getIsCanUseDiscount() ) && "1".equals( detail.getIsCanUseDiscount().toString() )
				&& detail.getNewTotalPrice() > 0 ) {
		    //把能使用积分的 商品总价 累计起来
		    busCanUseJifenProductPrice = CommonUtil.add( busCanUseJifenProductPrice, detail.getNewTotalPrice() );
		    busCanUseJifenProductNum++;
		}
	    }
	}
	if ( busCanUseJifenProductPrice == 0 || busCanUseJifenProductNum == 0 ) {//能使用积分的商品总价和商品总数 = 0
	    return orderList;
	}
	//	busCanUseJifenProductPrice = CommonUtil.getDecimalStr( busCanUseJifenProductPrice, 2 );
	JifenAndFenbBean bean = ToOrderUtil.getJifenFenbiParams( jifenFenbiRule, busCanUseJifenProductPrice, 0, cardMap );
	if ( bean == null || bean.getJifenMoney() == 0 || bean.getJifenNum() == 0 ) {
	    return orderList;
	}
	double useJifenTotalPrice = 0;//已使用积分的优惠金额
	double useJifenTotalNum = 0;//已使用积分的商品数量
	for ( MallOrder order : orderList ) {//循环店铺集合
	    double useJifenNum = 0;
	    double shopProductNewTotal = 0;//保存 店铺下 商品优惠后的总额
	    double totalYouhuiMoney = 0;//保存 订单下 商品优惠的金额
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {//循环商品集合
		boolean isCanUseJifen = CommonUtil.isEmpty( detail.getIsCanUseJifen() ) || !"1".equals( detail.getIsCanUseJifen().toString() );
		//已优惠积分的商品数量 = 店铺下能使用积分优惠的商品数量  && 已使用积分优惠的总额 == 已使用积分的优惠金额
		boolean isEndCanUseJifen = useJifenTotalNum == busCanUseJifenProductNum && useJifenTotalPrice == busCanUseJifenProductPrice;
		if ( isCanUseJifen || isEndCanUseJifen ) {
		    shopProductNewTotal = CommonUtil.add( shopProductNewTotal, detail.getNewTotalPrice() );
		    continue;
		}
		double productTotalPrice = detail.getNewTotalPrice();//商品优惠前的总价
		double productYouHuiHouTotalPrice;//商品优惠后的总价
		double jifenYouhuiPrice = 0;//积分优惠的金额
		if ( useJifenTotalNum + 1 == busCanUseJifenProductNum ) {
		    /*    最后一个能使用积分的商品   */
		    //单个商品使用积分优惠的金额 =  积分优惠的总额 - 已使用积分的优惠金额
		    jifenYouhuiPrice = CommonUtil.subtract( bean.getJifenMoney(), useJifenTotalPrice );
		} else {
		    //单个商品使用积分优惠的金额 = （（商品的总价 / 能使用积分的商品总价） * 积分总共能优惠的金额）
		    jifenYouhuiPrice = CommonUtil.multiply( CommonUtil.div( detail.getNewTotalPrice(), busCanUseJifenProductPrice, 10 ), bean.getJifenMoney() );
		}
		//		if ( productTotalPrice < jifenYouhuiPrice ) {
		//		    jifenYouhuiPrice = productTotalPrice;
		//		}
		productYouHuiHouTotalPrice = CommonUtil.subtract( productTotalPrice, jifenYouhuiPrice );
		if ( productYouHuiHouTotalPrice < 0 ) {
		    productYouHuiHouTotalPrice = 0;
		}

		//		detail.setUseJifenNum( CommonUtil.multiply( jifenFenbiRule.getJifenRatio(), jifenYouhuiPrice ) );
		//		detail.setUseJifenYouhuiPrice( jifenYouhuiPrice );
		//		detail.setProductNewTotalPrice( productYouHuiHouTotalPrice );

		double discount = 0;
		if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
		    discount = CommonUtil.toDouble( detail.getDiscountedPrices() );
		}
		discount = CommonUtil.add( discount, jifenYouhuiPrice );
		detail.setDiscountedPrices( CommonUtil.toBigDecimal( discount ) );
		detail.setNewTotalPrice( productYouHuiHouTotalPrice );
		detail.setUseJifen( CommonUtil.multiply( jifenFenbiRule.getJifenRatio(), jifenYouhuiPrice ) );
		detail.setIntegralYouhui( CommonUtil.toBigDecimal( jifenYouhuiPrice ) );

		useJifenTotalPrice += jifenYouhuiPrice;
		useJifenTotalNum++;

		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, detail.getNewTotalPrice() );
		useJifenNum += detail.getUseJifen();
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, jifenYouhuiPrice );
	    }
	    logger.error( "积分优惠后的价格：" + shopProductNewTotal + "--优惠了：" + totalYouhuiMoney + "元 -- 使用了：" + useJifenNum + "积分" );
	    order.setTotalNewMoney( shopProductNewTotal );
	    order.setUseJifen( useJifenNum );
	    //	    order.setJifenYouhuiMoney( totalYouhuiMoney );
	    order.setJifenDiscountMoney( totalYouhuiMoney );
	    order.setDiscountMoney( CommonUtil.add( order.getDiscountMoney(), totalYouhuiMoney ) );
	    logger.error( "totalYouhuiMoney====积分优惠========" + totalYouhuiMoney );
	}

	return orderList;
    }
}
