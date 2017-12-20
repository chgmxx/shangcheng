package com.gt.mall.service.web.common.impl;

import com.alibaba.fastjson.JSON;
import com.gt.api.bean.session.Member;
import com.gt.mall.bean.member.Coupons;
import com.gt.mall.bean.member.JifenAndFenbBean;
import com.gt.mall.bean.member.JifenAndFenbiRule;
import com.gt.mall.param.phone.order.add.PhoneAddOrderBusDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderProductDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderShopDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.common.MallCalculateService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.ToOrderUtil;
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
public class MallCalculateServiceImpl implements MallCalculateService {

    private static Logger logger = LoggerFactory.getLogger( MallCalculateServiceImpl.class );

    @Autowired
    private MemberService memberService;

    /**
     * 手机端 提交订单计算
     */
    @Override
    public PhoneAddOrderDTO calculateOrder( PhoneAddOrderDTO params, Member member ) {
	boolean isCalculate = true;
	Map cardMap = null;
	JifenAndFenbiRule jifenFenbiRule = null;//通过商家id查询积分和粉币规则
	for ( PhoneAddOrderBusDTO busDTO : params.getBusResultList() ) {//循环商家集合
	    Integer isSelectUnion = busDTO.getIsSelectUnion();//是否选中联盟  1选中
	    Integer isSelectDiscount = busDTO.getIsSelectDiscount();//是否选中会员折扣 1 选中
	    Integer isSelectCoupon = busDTO.getIsSelectCoupons();//是否选中优惠券 1选中
	    Integer isSelectJifen = busDTO.getIsSelectJifen();//是否选中积分抵扣 1选中
	    Integer isSelectFenbi = busDTO.getIsSelectFenbi();//是否选中粉币抵扣 1选中
	    boolean unionFlag = CommonUtil.isNotEmpty( isSelectUnion ) && "1".equals( isSelectUnion.toString() );
	    boolean discountFlag = CommonUtil.isNotEmpty( isSelectDiscount ) && "1".equals( isSelectDiscount.toString() );
	    boolean couponFlag = CommonUtil.isNotEmpty( isSelectCoupon ) && "1".equals( isSelectCoupon.toString() );
	    boolean jifenFlag = CommonUtil.isNotEmpty( isSelectJifen ) && "1".equals( isSelectJifen.toString() );
	    boolean fenbiFlag = CommonUtil.isNotEmpty( isSelectFenbi ) && "1".equals( isSelectFenbi.toString() );
	    if ( !unionFlag && !discountFlag && !couponFlag && !jifenFlag && !fenbiFlag ) {
		isCalculate = false;
		break;
	    }
	    if ( CommonUtil.isEmpty( cardMap ) ) {
		cardMap = memberService.findCardAndShopIdsByMembeId( member.getId(), params.getWxShopIds() );
	    }
	    if ( couponFlag ) {
		for ( PhoneAddOrderShopDTO shopDTO : busDTO.getShopResultList() ) {
		    List< Coupons > couponsList = new ArrayList<>();
		    //多粉优惠券
		    if ( cardMap.containsKey( "duofenCards" + shopDTO.getWxShopId() ) ) {
			Object obj = cardMap.get( "duofenCards" + shopDTO.getWxShopId() );
			couponsList = ToOrderUtil.getDuofenCouponsResult( obj, couponsList, shopDTO.getSelectCouponsId() );
		    }
		    //微信优惠券
		    if ( cardMap.containsKey( "cardList" + shopDTO.getWxShopId() ) ) {
			Object obj = cardMap.get( "cardList" + shopDTO.getWxShopId() );
			couponsList = ToOrderUtil.getWxCouponsResult( obj, couponsList, shopDTO.getSelectCouponsId() );
		    }
		    shopDTO.setCouponList( couponsList );
		}
	    }
	    if ( jifenFlag || fenbiFlag ) {
		if ( CommonUtil.isNotEmpty( params.getBusIds() ) && CommonUtil.isEmpty( jifenFenbiRule ) ) {
		    jifenFenbiRule = memberService.jifenAndFenbiRule( CommonUtil.toInteger( params.getBusIds() ) );
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
	    busDTO.setMemberDiscount( memberDiscount );
	    Double memberUnionDiscount = busDTO.getUnionDiscount();//联盟折扣
	    Double busFenbiYouhui = busDTO.getFenbiMoney();//保存商家下 粉币优惠的总额
	    Double busJifenYouhui = busDTO.getJifenMoney();//保存商家下 积分优惠的总额

	    List< PhoneAddOrderShopDTO > shopDTOList = busDTO.getShopResultList();

	    if ( unionFlag ) {//计算联盟折扣
		shopDTOList = calculateMemberUnion( shopDTOList, memberUnionDiscount );
	    }
	    if ( discountFlag ) {//计算会员折扣
		shopDTOList = calculateMemberDiscount( shopDTOList, memberDiscount, cardMap );
	    }
	    if ( couponFlag ) {//计算优惠券优惠
		shopDTOList = calculateMemberCoupon( shopDTOList, discountFlag );
	    }
	    if ( fenbiFlag && CommonUtil.isNotEmpty( jifenFenbiRule ) ) {//计算粉币优惠
		shopDTOList = calculateMemberFenbi( shopDTOList, busFenbiYouhui, jifenFenbiRule, cardMap );
	    }
	    if ( jifenFlag && CommonUtil.isNotEmpty( jifenFenbiRule ) ) {//计算积分优惠
		shopDTOList = calculateMemberJifen( shopDTOList, busJifenYouhui, jifenFenbiRule, cardMap );
	    }
	    double busTotalNew = 0;//商家实付的金额（折扣前）
	    double busTotalOld = 0;//商家应付的金额（折扣后）
	    double busTotalFreightMoney = 0;//运费
	    for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {
		if ( CommonUtil.isNotEmpty( shopDTO.getTotalFreightMoney() ) && shopDTO.getTotalFreightMoney() > 0 ) {
		    busTotalFreightMoney = CommonUtil.add( busTotalFreightMoney, shopDTO.getTotalFreightMoney() );
		}
		busTotalNew = CommonUtil.add( busTotalNew, shopDTO.getTotalNewMoney() );
		busTotalOld = CommonUtil.add( busTotalOld, shopDTO.getTotalMoney() );
	    }
	    busDTO.setProductTotalMoney( busTotalFreightMoney );
	    busDTO.setTotalNewMoney( busTotalNew );
	    busDTO.setTotalMoney( busTotalOld );
	}
	if ( !isCalculate ) {
	    return params;
	}
	logger.info( "优惠后的参数：" + JSON.toJSONString( params ) );
	return params;
    }

    /**
     * 计算商家下的联盟折扣  的优惠
     *
     * @param shopDTOList         店铺集合
     * @param memberUnionDiscount 商家下联盟的折扣
     *
     * @return 店铺集合
     */
    private List< PhoneAddOrderShopDTO > calculateMemberUnion( List< PhoneAddOrderShopDTO > shopDTOList, Double memberUnionDiscount ) {
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
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    double shopProductNewTotal = 0;//保存订单优惠后的商品总额
	    double totalYouhuiMoney = 0;//保存订单优惠的金额
	    //平摊联盟优惠
	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		double productNewTotal = productDTO.getProductNewTotalPrice();//商品优惠前的价格
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
		if ( productYouhuiPrice > 0 ) {
		    productDTO.setUseUnionDiscount( true );
		}
		productDTO.setUseUnionDiscountYouhuiPrice( productYouhuiPrice );
		productDTO.setProductNewTotalPrice( discountHouPrice );

		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, productDTO.getProductNewTotalPrice() );//累加店铺优惠后的金额
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, productYouhuiPrice );
	    }
	    shopDTO.setTotalYouhuiMoney( CommonUtil.add( shopDTO.getTotalYouhuiMoney(), totalYouhuiMoney ) );
	    shopDTO.setTotalNewMoney( CommonUtil.formatDoubleNumber( shopProductNewTotal ) );
	}
	return shopDTOList;
    }

    /**
     * 计算商家下的会员折扣  的优惠
     *
     * @param shopDTOList    店铺集合
     * @param memberDiscount 商家下会员折扣优惠的总额
     *
     * @return 店铺集合
     */
    private List< PhoneAddOrderShopDTO > calculateMemberDiscount( List< PhoneAddOrderShopDTO > shopDTOList, Double memberDiscount, Map cardMap ) {
	double busCanUseDiscountProductPrice = 0;//保存能使用会员折扣的商品总额
	//	List< Coupons > couponsList = new ArrayList<>();
	//保存能使用会员折扣的商品价格
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( productDTO.getIsCanUseDiscount() ) && "1".equals( productDTO.getIsCanUseDiscount().toString() )
				&& productDTO.getProductNewTotalPrice() > 0 ) {
		    //把能使用会员折扣的商品 总价 累计起来
		    busCanUseDiscountProductPrice = CommonUtil.add( busCanUseDiscountProductPrice, productDTO.getProductNewTotalPrice() );

		}
	    }
	   /* //多粉优惠券
	    if ( cardMap.containsKey( "duofenCards" + shopDTO.getWxShopId() ) ) {
		Object obj = cardMap.get( "duofenCards" + shopDTO.getWxShopId() );
		couponsList = ToOrderUtil.getDuofenCouponsResult( obj, couponsList );
	    }
	    //微信优惠券
	    if ( cardMap.containsKey( "cardList" + shopDTO.getWxShopId() ) ) {
		Object obj = cardMap.get( "cardList" + shopDTO.getWxShopId() );
		couponsList = ToOrderUtil.getWxCouponsResult( obj, couponsList );
	    }*/
	}
	busCanUseDiscountProductPrice = CommonUtil.formatDoubleNumber( busCanUseDiscountProductPrice );
	Double busDiscountYouhui = null;//保存商家下  折扣卡优惠的总额
	if ( busCanUseDiscountProductPrice > 0 && memberDiscount > 0 && memberDiscount < 1 ) {
	    double discountYouhuiHouPrice = CommonUtil.multiply( busCanUseDiscountProductPrice, memberDiscount );
	    busDiscountYouhui = CommonUtil.subtract( busCanUseDiscountProductPrice, discountYouhuiHouPrice );
	} else {
	    return shopDTOList;
	}
	//	double proUnionYouhuiTotal = 0;//已经优惠的联盟总额
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    double shopProductNewTotal = 0;//保存订单优惠后的商品总额
	    double totalYouhuiMoney = 0;
	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//商品集合
		if ( CommonUtil.isEmpty( productDTO.getIsCanUseDiscount() ) && !"1".equals( productDTO.getIsCanUseDiscount().toString() ) ) {
		    shopProductNewTotal = CommonUtil.add( shopProductNewTotal, productDTO.getProductNewTotalPrice() );
		    continue;
		}
		double productNewTotal = productDTO.getProductNewTotalPrice();//商品优惠前的价格
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
		productDTO.setUseDiscountYouhuiPrice( productYouhuiPrice );
		productDTO.setProductNewTotalPrice( discountHouPrice );
		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, productDTO.getProductNewTotalPrice() );
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, productYouhuiPrice );
	    }
	    shopDTO.setTotalYouhuiMoney( CommonUtil.add( shopDTO.getTotalYouhuiMoney(), totalYouhuiMoney ) );
	    shopDTO.setTotalNewMoney( shopProductNewTotal );

	}
	return shopDTOList;
    }

    /**
     * 计算商家下的优惠券 的优惠
     *
     * @param shopDTOList  店铺集合
     * @param discountFlag 是否计算的会员折扣
     *
     * @return 店铺集合
     */
    private List< PhoneAddOrderShopDTO > calculateMemberCoupon( List< PhoneAddOrderShopDTO > shopDTOList, boolean discountFlag ) {
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    List< Coupons > couponsList = shopDTO.getCouponList();//优惠券集合
	    List< PhoneAddOrderProductDTO > productDTOList = shopDTO.getProductResultList();//商品集合
	    Integer couponsId = shopDTO.getSelectCouponsId();//选中优惠券id
	    if ( CommonUtil.isEmpty( couponsId ) || couponsId == 0 || couponsList == null || couponsList.size() == 0 ) {
		continue;
	    }
	    Coupons coupons = null;//选中优惠券对象
	    for ( Coupons coupon : couponsList ) {
		if ( couponsId.toString().equals( coupon.getId().toString() ) ) {
		    coupons = coupon;
		}
	    }
	    if ( CommonUtil.isEmpty( coupons ) || CommonUtil.isEmpty( coupons.getCardType() ) ) {
		continue;
	    }
	    double canUseCouponProductPrice = 0;//能使用优惠券的商品金额
	    int canUseCouponProductNum = 0;//能使用优惠券的商品数量
	    for ( PhoneAddOrderProductDTO productDTO : productDTOList ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( productDTO.getIsCanUseYhq() ) && "1".equals( productDTO.getIsCanUseYhq().toString() ) && productDTO.getProductNewTotalPrice() > 0 ) {
		    canUseCouponProductPrice = CommonUtil.add( canUseCouponProductPrice, productDTO.getProductNewTotalPrice() );
		    canUseCouponProductNum++;
		}
	    }
	    if ( canUseCouponProductPrice == 0 || canUseCouponProductNum == 0 ) {//能使用优惠券的商品总价和商品总数 = 0  则跳出当前循环
		continue;
	    }
	    canUseCouponProductPrice = CommonUtil.formatDoubleNumber( canUseCouponProductPrice );
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
	    for ( PhoneAddOrderProductDTO productDTO : productDTOList ) {//循环商品集合
		boolean isCanUseYhq = CommonUtil.isEmpty( productDTO.getIsCanUseYhq() ) || !"1".equals( productDTO.getIsCanUseYhq().toString() );
		//已使用优惠券的数量 = 店铺下优惠券优惠的数量  && 已使用优惠券的金额 == 店铺下优惠券优惠的总额
		boolean isEndCanUseYhq = useCouponTotalNum == canUseCouponProductNum && useCouponTotalPrice == shopYouhuiHouTotalPrice;  //店铺下使用优惠的金额 已经分摊完毕
		if ( isCanUseYhq && isEndCanUseYhq ) {
		    shopProductNewTotal = CommonUtil.add( shopProductNewTotal, productDTO.getProductNewTotalPrice() );
		    continue;
		}
		productDTO.setSelectCouponsId( couponsId );
		productDTO.setSelectCouponsType( cardFrom );
		double productTotalPrice = productDTO.getProductNewTotalPrice();//商品优惠前的总价
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
		productDTO.setUseCouponYouhuiPrice( couponYouhuiPrice );
		productDTO.setProductNewTotalPrice( productYouHuiHouTotalPrice );
		useCouponTotalPrice += couponYouhuiPrice;
		useCouponTotalNum++;

		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, productDTO.getProductNewTotalPrice() );
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, couponYouhuiPrice );
	    }
	    shopDTO.setSelectCouponsId( couponsId );
	    shopDTO.setSelectCouponsType( cardFrom );
	    shopDTO.setSelectCouponsNum( couponNum > 0 ? couponNum : 0 );
	    shopDTO.setTotalYouhuiMoney( CommonUtil.add( shopDTO.getTotalYouhuiMoney(), totalYouhuiMoney ) );
	    shopDTO.setTotalNewMoney( CommonUtil.formatDoubleNumber( shopProductNewTotal ) );
	}
	return shopDTOList;
    }

    /**
     * 计算商家下的粉币 的优惠
     *
     * @param shopDTOList    店铺集合
     * @param busFenbiYouhui 商家下 粉币优惠的总额
     *
     * @return 店铺集合
     */
    private List< PhoneAddOrderShopDTO > calculateMemberFenbi( List< PhoneAddOrderShopDTO > shopDTOList, Double busFenbiYouhui, JifenAndFenbiRule jifenFenbiRule, Map cardMap ) {
	double busCanUseFenbiProductPrice = 0;//保存能使用粉币的商品总额
	int busCanUseFenbiProductNum = 0;//保存能使用粉币的商品数量
	//保存能使用会员折扣的商品价格
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( productDTO.getIsCanUseFenbi() ) && "1".equals( productDTO.getIsCanUseFenbi().toString() )
				&& productDTO.getProductNewTotalPrice() > 0 ) {
		    //把能使用粉币的商品 总价 累计起来
		    busCanUseFenbiProductPrice = CommonUtil.add( busCanUseFenbiProductPrice, productDTO.getProductNewTotalPrice() );
		    busCanUseFenbiProductNum++;
		}
	    }
	}
	if ( busCanUseFenbiProductPrice == 0 || busCanUseFenbiProductNum == 0 ) {//能使用粉币的商品总价和商品总数 = 0
	    return shopDTOList;
	}
	busCanUseFenbiProductPrice = CommonUtil.formatDoubleNumber( busCanUseFenbiProductPrice );
	JifenAndFenbBean bean = ToOrderUtil.getJifenFenbiParams( jifenFenbiRule, 0, busCanUseFenbiProductPrice, cardMap );
	if ( bean == null || bean.getFenbiMoney() == 0 || bean.getFenbiNum() == 0 ) {
	    return shopDTOList;
	}
	double useFenbiTotalPrice = 0;//已使用粉币的优惠金额
	double useFenbiTotalNum = 0;//已使用粉币的商品数量
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    double useFenbiNum = 0;
	    double shopYouhuiHouTotalPrice = 0;//保存 店铺下 商品优惠后的总额
	    double totalYouhuiMoney = 0;//保存订单下优惠的总额
	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		boolean isCanUseCoupon = CommonUtil.isEmpty( productDTO.getIsCanUseFenbi() ) || !"1".equals( productDTO.getIsCanUseFenbi().toString() );//是否能使用粉币
		//已优惠粉币的商品数量 = 店铺下能使用粉币优惠的商品数量  && 已使用粉币优惠的 == 已使用粉币的优惠金额
		boolean isEndCanUseFenbi = useFenbiTotalNum == busCanUseFenbiProductNum && useFenbiTotalPrice == busCanUseFenbiProductPrice;
		if ( isCanUseCoupon || isEndCanUseFenbi ) {
		    shopYouhuiHouTotalPrice = CommonUtil.add( shopYouhuiHouTotalPrice, productDTO.getProductNewTotalPrice() );
		    continue;
		}

		double productTotalPrice = productDTO.getProductNewTotalPrice();//商品优惠前的总价
		double productYouHuiHouTotalPrice;//商品优惠后的总价
		double fenbiYouhuiPrice = 0;//粉币优惠的金额
		if ( useFenbiTotalNum + 1 == busCanUseFenbiProductNum ) {//最后一个能使用粉币的商品
		    fenbiYouhuiPrice = CommonUtil.subtract( bean.getFenbiMoney(), useFenbiTotalPrice );//单个商品使用粉币优惠的金额 =  粉币优惠的总额 - 已使用粉币的优惠金额
		} else {
		    //单个商品使用粉币优惠的金额 = （（商品的总价 / 能使用粉币的商品总价） * 粉币总共能优惠的金额）
		    fenbiYouhuiPrice = CommonUtil.multiply( CommonUtil.div( productDTO.getProductNewTotalPrice(), busCanUseFenbiProductPrice, 10 ), bean.getFenbiMoney() );
		}
		productYouHuiHouTotalPrice = CommonUtil.subtract( productTotalPrice, fenbiYouhuiPrice );
		productDTO.setUseFenbiNum( CommonUtil.multiply( jifenFenbiRule.getFenbiRatio(), fenbiYouhuiPrice ) );
		productDTO.setUseFenbiYouhuiPrice( fenbiYouhuiPrice );
		productDTO.setProductNewTotalPrice( productYouHuiHouTotalPrice );

		useFenbiTotalPrice += fenbiYouhuiPrice;
		useFenbiTotalNum++;

		shopYouhuiHouTotalPrice = CommonUtil.add( shopYouhuiHouTotalPrice, productDTO.getProductNewTotalPrice() );
		useFenbiNum += productDTO.getUseFenbiNum();
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, fenbiYouhuiPrice );
	    }
	    shopDTO.setTotalNewMoney( CommonUtil.formatDoubleNumber( shopYouhuiHouTotalPrice ) );
	    shopDTO.setUseFenbi( useFenbiNum );
	    shopDTO.setFenbiYouhuiMoney( totalYouhuiMoney );
	    shopDTO.setTotalYouhuiMoney( CommonUtil.add( shopDTO.getTotalYouhuiMoney(), totalYouhuiMoney ) );
	}
	return shopDTOList;
    }

    /**
     * 计算商家下的积分 的优惠
     *
     * @param shopDTOList    店铺集合
     * @param busJifenYouhui 商家下积分优惠的总额
     *
     * @return 店铺集合
     */
    private List< PhoneAddOrderShopDTO > calculateMemberJifen( List< PhoneAddOrderShopDTO > shopDTOList, Double busJifenYouhui, JifenAndFenbiRule jifenFenbiRule, Map cardMap ) {
	double busCanUseJifenProductPrice = 0;//保存能使用积分的商品总额
	int busCanUseJifenProductNum = 0;//保存能使用积分的商品数量
	//保存能使用会员折扣的商品价格
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		if ( CommonUtil.isNotEmpty( productDTO.getIsCanUseDiscount() ) && "1".equals( productDTO.getIsCanUseDiscount().toString() )
				&& productDTO.getProductNewTotalPrice() > 0 ) {
		    //把能使用积分的 商品总价 累计起来
		    busCanUseJifenProductPrice = CommonUtil.add( busCanUseJifenProductPrice, productDTO.getProductNewTotalPrice() );
		    busCanUseJifenProductNum++;
		}
	    }
	}
	if ( busCanUseJifenProductPrice == 0 || busCanUseJifenProductNum == 0 ) {//能使用积分的商品总价和商品总数 = 0
	    return shopDTOList;
	}
	busCanUseJifenProductPrice = CommonUtil.formatDoubleNumber( busCanUseJifenProductPrice );
	JifenAndFenbBean bean = ToOrderUtil.getJifenFenbiParams( jifenFenbiRule, busCanUseJifenProductPrice, 0, cardMap );
	if ( bean == null || bean.getJifenMoney() == 0 || bean.getJifenNum() == 0 ) {
	    return shopDTOList;
	}
	double useJifenTotalPrice = 0;//已使用积分的优惠金额
	double useJifenTotalNum = 0;//已使用积分的商品数量
	for ( PhoneAddOrderShopDTO shopDTO : shopDTOList ) {//循环店铺集合
	    double useJifenNum = 0;
	    double shopProductNewTotal = 0;//保存 店铺下 商品优惠后的总额
	    double totalYouhuiMoney = 0;//保存 订单下 商品优惠的金额
	    for ( PhoneAddOrderProductDTO productDTO : shopDTO.getProductResultList() ) {//循环商品集合
		boolean isCanUseJifen = CommonUtil.isEmpty( productDTO.getIsCanUseJifen() ) || !"1".equals( productDTO.getIsCanUseJifen().toString() );
		//已优惠积分的商品数量 = 店铺下能使用积分优惠的商品数量  && 已使用积分优惠的总额 == 已使用积分的优惠金额
		boolean isEndCanUseJifen = useJifenTotalNum == busCanUseJifenProductNum && useJifenTotalPrice == busCanUseJifenProductPrice;
		if ( isCanUseJifen || isEndCanUseJifen ) {
		    shopProductNewTotal = CommonUtil.add( shopProductNewTotal, productDTO.getProductNewTotalPrice() );
		    continue;
		}
		double productTotalPrice = productDTO.getProductNewTotalPrice();//商品优惠前的总价
		double productYouHuiHouTotalPrice;//商品优惠后的总价
		double jifenYouhuiPrice = 0;//积分优惠的金额
		if ( useJifenTotalNum + 1 == busCanUseJifenProductNum ) {
		    /*    最后一个能使用积分的商品   */

		    //单个商品使用积分优惠的金额 =  积分优惠的总额 - 已使用积分的优惠金额
		    jifenYouhuiPrice = CommonUtil.subtract( bean.getJifenMoney(), useJifenTotalPrice );
		} else {
		    //单个商品使用积分优惠的金额 = （（商品的总价 / 能使用积分的商品总价） * 积分总共能优惠的金额）
		    jifenYouhuiPrice = CommonUtil.multiply( CommonUtil.div( productDTO.getProductNewTotalPrice(), busCanUseJifenProductPrice, 10 ), bean.getJifenMoney() );
		}
		productYouHuiHouTotalPrice = CommonUtil.subtract( productTotalPrice, jifenYouhuiPrice );
		productDTO.setUseJifenNum( CommonUtil.multiply( jifenFenbiRule.getJifenRatio(), jifenYouhuiPrice ) );
		productDTO.setUseJifenYouhuiPrice( jifenYouhuiPrice );
		productDTO.setProductNewTotalPrice( productYouHuiHouTotalPrice );

		useJifenTotalPrice += jifenYouhuiPrice;
		useJifenTotalNum++;

		shopProductNewTotal = CommonUtil.add( shopProductNewTotal, productDTO.getProductNewTotalPrice() );
		useJifenNum += productDTO.getUseJifenNum();
		totalYouhuiMoney = CommonUtil.add( totalYouhuiMoney, jifenYouhuiPrice );
	    }
	    shopDTO.setTotalNewMoney( CommonUtil.formatDoubleNumber( shopProductNewTotal ) );
	    shopDTO.setUseJifen( useJifenNum );
	    shopDTO.setJifenYouhuiMoney( totalYouhuiMoney );
	    shopDTO.setTotalYouhuiMoney( CommonUtil.add( shopDTO.getTotalYouhuiMoney(), totalYouhuiMoney ) );
	}

	return shopDTOList;
    }
}
