package com.gt.mall.service.web.applet.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.entityBo.MallAllEntity;
import com.gt.entityBo.MallEntity;
import com.gt.entityBo.MallShopEntity;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.api.bean.session.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.dao.applet.MallAppletImageDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.applet.MallNewOrderAppletService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.fenbiFlow.BusFlowInfo;
import com.gt.util.entity.param.fenbiFlow.ReqGetMobileInfo;
import com.gt.util.entity.result.fenbi.GetMobileInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 小程序图片表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallNewOrderAppletServiceImpl extends BaseServiceImpl< MallAppletImageDAO,MallAppletImage > implements MallNewOrderAppletService {

    private Logger logger = Logger.getLogger( MallNewOrderAppletServiceImpl.class );

    @Autowired
    private MallOrderDAO            orderDAO;
    @Autowired
    private MallShopCartDAO         shopCartDAO;
    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;
    @Autowired
    private MallStoreDAO            storeDAO;
    @Autowired
    private MallOrderDetailDAO      orderDetailDAO;

    @Autowired
    private MallProductService   productService;
    @Autowired
    private MallFreightService   freightService;
    @Autowired
    private MallPaySetService    paySetService;
    @Autowired
    private MallOrderService     orderService;
    @Autowired
    private MallPageService      pageService;
    @Autowired
    private MemberService        memberService;
    @Autowired
    private DictService          dictService;
    @Autowired
    private WxPublicUserService  wxPublicUserService;
    @Autowired
    private MallStoreService     mallStoreService;
    @Autowired
    private FenBiFlowService     fenBiFlowService;
    @Autowired
    private WxShopService        wxShopService;
    @Autowired
    private MemberPayService     memberPayService;
    @Autowired
    private MallAuctionService   mallAuctionService;
    @Autowired
    private MallPresaleService   mallPresaleService;
    @Autowired
    private MallSeckillService   mallSeckillService;
    @Autowired
    private MemberAddressService memberAddressService;

    @Override
    public Map< String,Object > toSubmitOrder( Map< String,Object > params ) {

	Map< String,Object > resultMap = new HashMap< String,Object >();
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Member member = memberService.findMemberById( memberId, null );
	int from = 1;
	if ( CommonUtil.isNotEmpty( params.get( "from" ) ) ) {
	    from = CommonUtil.toInteger( params.get( "from" ) );
	}
	DecimalFormat df = new DecimalFormat( "######0.00" );
	//查询用户默认的地址
	List< Integer > memberList = memberService.findMemberListByIds( memberId );//查询会员信息
	Map addressMap = memberAddressService.addressDefault( CommonUtil.getMememberIds( memberList, memberId ) );
	if ( addressMap != null && addressMap.size() > 0 ) {
	    resultMap.put( "addressMap", getAddressParams( addressMap ) );
	    params.put( "mem_province", addressMap.get( "memProvince" ) );
	}
	resultMap.put( "memberPhone", member.getPhone() );
	WxPublicUsers wxPublicUsers = null;
	if ( CommonUtil.isNotEmpty( member.getPublicId() ) ) {
	    wxPublicUsers = wxPublicUserService.selectById( member.getPublicId() );
	}
	int jifenNum = 0;//统计能使用积分的商品数量
	double jifenMoney = 0;//统计能使用积分的商品总价
	int fenbiNum = 0;//统计能使用粉币的商品数量
	double fenbiMoney = 0;//统计能使用粉币的商品总价
	int yhqNum = 0;//统计能使用优惠券的商品数量
	double yhqMoney = 0;//统计能使用优惠券的商品总价
	int unionNum = 0;//能使用商家联盟的商品数量
	double unionMoney = 0;//能使用商家联盟的商品总价

	//	double totalMoney = 0;//实付金额
	double totalProMoney = 0;//	商品总价
	double totalFreightMoney = 0;//运费总价
	double totalDiscountMoney = 0; //商品会员折扣金额
	int isFlow = 0;//是否是流量充值商品  1是  0 不是

	int totalNum = 0;//商品小计
	int proTypeId = 0;//商品类型
	double discount = productService.getMemberDiscount( "1", member );//获取会员折扣
	Map memberCard = null;
	if ( from == 1 ) {//购物车结算

	    List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	    if ( CommonUtil.isNotEmpty( params.get( "cartIds" ) ) ) {
		JSONArray cartArrs = JSONArray.parseArray( params.get( "cartIds" ).toString() );
		params.put( "cartIds", cartArrs );
	    }
	    List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( member.getBusid() );
	    List< Map< String,Object > > shopList = shopCartDAO.selectCheckShopByParam( params );
	    if ( shopList != null && shopList.size() > 0 ) {
		for ( Map< String,Object > shopMap : shopList ) {
		    int shopId = CommonUtil.toInteger( shopMap.get( "shop_id" ) );
		    params.put( "shopId", shopId );
		    double pro_weight = 0;
		    double totalPrice = 0;
		    totalNum = 0;
		    boolean isYhq = false;
		    /*jifenNum = 0;//统计能使用积分的商品数量
		    jifenMoney = 0;//统计能使用积分的商品总价
					fenbiNum = 0;//统计能使用粉币的商品数量
					fenbiMoney = 0;//统计能使用粉币的商品总价
*/
		    yhqNum = 0;//统计能使用优惠券的商品数量
		    yhqMoney = 0;//统计能使用优惠券的商品总价
					/*unionNum = 0;//能使用商家联盟的商品数量
					unionMoney = 0;//能使用商家联盟的商品总价
*/
		    List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();
		    List< Map< String,Object > > productList = shopCartDAO.selectCheckCartByParams( params );
		    if ( productList != null && productList.size() > 0 ) {
			for ( Map< String,Object > map : productList ) {
			    Map< String,Object > productMap = new HashMap< String,Object >();
			    productMap.put( "product_name", map.get( "pro_name" ) );
			    productMap.put( "product_image", PropertiesUtil.getResourceUrl() + map.get( "image_url" ) );
			    productMap.put( "product_specificas", map.get( "product_specificas" ) );
			    productMap.put( "product_specificaname", map.get( "product_speciname" ) );
			    productMap.put( "product_num", map.get( "product_num" ) );
			    productMap.put( "product_price", map.get( "price" ) );//会员折扣后价格
			    productMap.put( "primary_price", map.get( "primary_price" ) );//原价
			    productMap.put( "is_member_discount", map.get( "is_member_discount" ) );
			    productMap.put( "is_coupons", map.get( "is_coupons" ) );
			    productMap.put( "is_integral_deduction", map.get( "is_integral_deduction" ) );
			    productMap.put( "is_fenbi_deduction", map.get( "is_fenbi_deduction" ) );
			    productMap.put( "product_id", map.get( "product_id" ) );
			    productMap.put( "product_type_id", map.get( "pro_type_id" ) );
			    totalNum += CommonUtil.toInteger( map.get( "product_num" ) );
			    proTypeId = CommonUtil.toInteger( map.get( "pro_type_id" ) );
			    double discountPrice = CommonUtil.toDouble( map.get( "primary_price" ) ) - CommonUtil.toDouble( map.get( "price" ) );
			    discountPrice = CommonUtil.toDouble( df.format( discountPrice ) );
			    productMap.put( "discount_price", discountPrice );//折扣金额

			    //新版本新增内容  1.1 开始    计算商品的会员价
			    /*double price = 0;
			    if(CommonUtil.isNotEmpty(productMap.get("product_price"))){
				    price = CommonUtil.toDouble(productMap.get("product_price"));
			    }
			    if(CommonUtil.isNotEmpty(map.get("is_member_discount"))){
				    if(CommonUtil.toString(map.get("is_member_discount")).equals("1")){
					    if(discount > 0 && discount < 1){
						    double memberProPrice = CommonUtil.toDouble(df.format(price*discount));

						    productMap.put("primary_price", price);
						    productMap.put("product_price", memberProPrice);
					    }
				    }
			    }*/
			    //新版本新增内容  1.1 结束

			    if ( CommonUtil.isNotEmpty( map.get( "product_specificas" ) ) ) {
				Map< String,Object > invMap = productService
						.getProInvIdBySpecId( CommonUtil.toString( map.get( "product_specificas" ) ), CommonUtil.toInteger( map.get( "product_id" ) ) );
				if ( CommonUtil.isNotEmpty( invMap ) && CommonUtil.isNotEmpty( invMap.get( "specifica_values" ) ) ) {
				    String speciname = invMap.get( "specifica_values" ).toString();
				    speciname = speciname.replaceAll( ",", "/" );
				    productMap.put( "product_specificaname", speciname );
				}
			    }

			    proList.add( productMap );
			    double proTotalPrice = CommonUtil.toDouble( productMap.get( "primary_price" ) ) * CommonUtil.toInteger( map.get( "product_num" ) );
			    totalPrice += proTotalPrice;
			    //countProPrice += proTotalPrice;

			    totalProMoney += proTotalPrice;

			    double proTotalDiscount = discountPrice * CommonUtil.toInteger( map.get( "product_num" ) );
			    totalDiscountMoney += proTotalDiscount;

			    pro_weight += CommonUtil.toDouble( map.get( "pro_weight" ) );

			    if ( CommonUtil.toString( map.get( "is_integral_deduction" ) ).equals( "1" ) ) {
				jifenMoney += proTotalPrice;
				++jifenNum;
			    }
			    if ( CommonUtil.toString( map.get( "is_fenbi_deduction" ) ).equals( "1" ) ) {
				fenbiMoney += proTotalPrice;
				++fenbiNum;
			    }
			    if ( CommonUtil.toString( map.get( "is_coupons" ) ).equals( "1" ) ) {
				isYhq = true;
				yhqMoney += proTotalPrice;
				++yhqNum;
			    }
			    unionMoney += proTotalPrice;
			    ++unionNum;
			}
		    }
		    params.put( "product_num", totalNum );
		    double freightPrice = freightService.getFreightByProvinces( params, addressMap, shopId, totalPrice, pro_weight );

		    totalFreightMoney += freightPrice;

		    shopMap.put( "freightPrice", freightPrice );
		    shopMap.put( "totalProPrice", df.format( totalPrice ) );

		    shopMap.put( "proList", proList );

		    shopMap.put( "totalNum", totalNum );

		    /*Store store = storeMapper.selectByPrimaryKey(shopId);*/
		    Map< String,Object > map = new HashMap< String,Object >();
		    map.put( "id", shopId );
		    //Map<String, Object> storeMap = storeMapper.selectByStoreId(map);
		    int wxShopId = 0;
		    if ( CommonUtil.isNotEmpty( shopMap.get( "wx_shop_id" ) ) ) {
			wxShopId = CommonUtil.toInteger( shopMap.get( "wx_shop_id" ) );
		    }
		    memberCard = memberService.findCardAndShopIdsByMembeId( member.getId(), shopMap.get( "wx_shop_id" ).toString() );
		    if ( proTypeId == 0 && isYhq ) {
			List< Map< String,Object > > coupon = (List< Map< String,Object > >) memberCard.get( "cardList" + wxShopId );
			if ( null != coupon && coupon.size() > 0 ) {
			    coupon = getCouponDuofen( coupon, yhqMoney, 1 );
			    shopMap.put( "coupon", coupon );
			}

			//查询多粉优惠券
			List< Map< String,Object > > duofenCoupon = (List< Map< String,Object > >) memberCard.get( "duofenCards" + wxShopId );
			if ( duofenCoupon != null && duofenCoupon.size() > 0 ) {
			    duofenCoupon = getCouponDuofen( duofenCoupon, yhqMoney, 0 );
			    shopMap.put( "duofenCoupon", duofenCoupon );
			}
		    }
		    String shopPicture = "";
		    for ( WsWxShopInfoExtend wxShops : shopInfoList ) {
			if ( wxShops.getId() == wxShopId ) {
			    if ( CommonUtil.isNotEmpty( wxShops.getBusinessName() ) ) {
				shopMap.put( "sto_name", wxShops.getBusinessName() );
			    }
			    shopPicture = wxShops.getImageUrl();
			    break;
			}
		    }
		    if ( CommonUtil.isNotEmpty( shopPicture ) ) {
			shopMap.put( "sto_image", shopPicture );
		    } else {
			shopMap.put( "sto_image", shopMap.get( "stoPicture" ) );
		    }

		    shopMap.remove( "stoPicture" );
		    shopMap.remove( "wx_shop_id" );

		    //版本1.1新增开始
		    /*shopMap.put("jifenProNum", jifenNum);//能使用积分的商品数量
		    shopMap.put("jifenProMoney", jifenMoney);//能使用积分的商品总价

		    shopMap.put("fenbiProNum", fenbiNum);//能使用粉币的商品数量
		    shopMap.put("fenbiProMoney", fenbiMoney);//能使用粉币的商品总价
			*/
		    shopMap.put( "yhqProNum", yhqNum );//能使用优惠券的商品数量
		    shopMap.put( "yhqProMoney", yhqMoney );//能使用优惠券的商品总价

		    /*shopMap.put("unionProNum", unionNum);//能使用商家联盟的商品数量
		     shopMap.put("unionProMoney", unionMoney);//能使用商家联盟的商品总价
		    */                    //版本1.1新增结束

		    cartList.add( shopMap );
		}
	    }
	    resultMap.put( "shopList", cartList );

	} else {//立即购买
	    int productId = CommonUtil.toInteger( params.get( "product_id" ) );
	    MallProduct product = productService.selectByPrimaryKey( productId );
	    String specificaIds = "";
	    String specificaNames = "";
	    String imageUrl = "";
	    boolean isYhq = false;
	    if ( CommonUtil.isNotEmpty( params.get( "product_specificas" ) ) ) {
		specificaIds = CommonUtil.toString( params.get( "product_specificas" ) );

		Map< String,Object > specMap = productService.getSpecNameBySPecId( specificaIds, productId );
		if ( CommonUtil.isNotEmpty( specMap.get( "product_speciname" ) ) ) {
		    specificaNames = CommonUtil.toString( specMap.get( "product_speciname" ) );
		}
		if ( CommonUtil.isNotEmpty( specMap.get( "imageUrl" ) ) ) {
		    imageUrl = CommonUtil.toString( specMap.get( "imageUrl" ) );
		}
	    }
	    if ( CommonUtil.isEmpty( imageUrl ) ) {
		Map< String,Object > imageParams = new HashMap< String,Object >();
		imageParams.put( "assId", productId );
		imageParams.put( "isMainImages", 1 );
		imageParams.put( "assType", 1 );
		List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( imageParams );
		if ( imageList != null && imageList.size() > 0 ) {
		    imageUrl = imageList.get( 0 ).getImageUrl();
		}
	    }
	    if ( CommonUtil.isNotEmpty( imageUrl ) ) {
		if ( imageUrl.indexOf( PropertiesUtil.getResourceUrl() ) < 0 ) {
		    imageUrl = PropertiesUtil.getResourceUrl() + imageUrl;
		}
	    }
	    List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();

	    Map< String,Object > productMap = new HashMap< String,Object >();
	    productMap.put( "product_id", product.getId() );
	    productMap.put( "product_type_id", product.getProTypeId() );
	    productMap.put( "product_name", product.getProName() );
	    productMap.put( "product_image", imageUrl );
	    productMap.put( "product_specificas", specificaIds );
	    productMap.put( "product_specificaname", specificaNames );
	    productMap.put( "product_num", params.get( "product_num" ) );
	    productMap.put( "product_price", params.get( "product_price" ) );
	    productMap.put( "primary_price", params.get( "primary_price" ) );
	    productMap.put( "is_member_discount", product.getIsMemberDiscount() );
	    productMap.put( "is_coupons", product.getIsCoupons() );
	    productMap.put( "is_integral_deduction", product.getIsIntegralDeduction() );
	    productMap.put( "is_fenbi_deduction", product.getIsFenbiDeduction() );
	    proTypeId = product.getProTypeId();

	    totalNum += CommonUtil.toInteger( params.get( "product_num" ) );

	    double discountPrice = 0;
	    //新版本新增内容  1.1 开始    计算商品的会员价
	    double memberProPrice = 0;
	    double price = 0;
	    if ( CommonUtil.isNotEmpty( productMap.get( "product_price" ) ) ) {
		price = CommonUtil.toDouble( productMap.get( "product_price" ) );
		memberProPrice = price;
	    }
	    if ( CommonUtil.isNotEmpty( product.getIsMemberDiscount() ) ) {
		if ( CommonUtil.toString( product.getIsMemberDiscount() ).equals( "1" ) ) {
		    if ( discount > 0 && discount < 1 ) {
			memberProPrice = CommonUtil.toDouble( df.format( price * discount ) );

			productMap.put( "primary_price", price );
			productMap.put( "product_price", memberProPrice );

			discountPrice = price - memberProPrice;
			discountPrice = CommonUtil.toDouble( df.format( discountPrice ) );
			productMap.put( "discount_price", discountPrice );//折扣金额
		    }
		}
	    }
	    //新版本新增内容  1.1 结束

	    //判断商品是否是流量充值
	    if ( CommonUtil.isNotEmpty( product.getProTypeId() ) && CommonUtil.isNotEmpty( product.getFlowId() ) ) {
		if ( product.getProTypeId().toString().equals( "4" ) && product.getFlowId() > 0 ) {
		    isFlow = 1;
		}
	    }

	    proList.add( productMap );

	    //	    double proTotalPrice = memberProPrice * CommonUtil.toInteger( params.get( "product_num" ) );
	    double proTotalPrice = CommonUtil.toDouble( productMap.get( "primary_price" ) ) * CommonUtil.toInteger( params.get( "product_num" ) );
	    totalProMoney += proTotalPrice;

	    double proTotalDiscount = discountPrice * CommonUtil.toInteger( params.get( "product_num" ) );
	    totalDiscountMoney += proTotalDiscount;

	    if ( CommonUtil.toString( product.getIsIntegralDeduction() ).equals( "1" ) ) {
		++jifenNum;
		jifenMoney += proTotalPrice;
	    }
	    if ( CommonUtil.toString( product.getIsFenbiDeduction() ).equals( "1" ) ) {
		++fenbiNum;
		fenbiMoney += proTotalPrice;
	    }
	    if ( CommonUtil.toString( product.getIsCoupons() ).equals( "1" ) ) {
		++yhqNum;
		yhqMoney += proTotalPrice;
		isYhq = true;
	    }
	    ++unionNum;
	    unionMoney += proTotalPrice;

	    double totalPrice = proTotalPrice;

	    //计算运费
	    double freightPrice = freightService.getFreightByProvinces( params, addressMap, product.getShopId(), totalPrice, CommonUtil.toDouble( product.getProWeight() ) );
	    totalFreightMoney += freightPrice;

	    //查询店铺名称
	    Map< String,Object > storeMap = null;
	    Map< String,Object > shopMap = new HashMap< String,Object >();
	    try {
		storeMap = mallStoreService.findShopByStoreId( product.getShopId() );
	    } catch ( Exception e ) {
		logger.error( "查询店铺信息异常:" + e.getMessage() );
	    }
	    shopMap.put( "shop_id", product.getShopId() );

	    shopMap.put( "sto_name", storeMap.get( "stoName" ) );
	    shopMap.put( "sto_image", storeMap.get( "stoPicture" ) );

	    shopMap.put( "totalNum", totalNum );

	    shopMap.put( "freightPrice", freightPrice );
	    shopMap.put( "proList", proList );
	    shopMap.put( "totalProPrice", df.format( totalPrice ) );
	    MallStore store = storeDAO.selectById( product.getShopId() );
	    memberCard = memberService.findCardAndShopIdsByMembeId( member.getId(), store.getWxShopId().toString() );

	    if ( proTypeId == 0 && isYhq ) {
		List< Map< String,Object > > coupon = (List< Map< String,Object > >) memberCard.get( "cardList" + store.getWxShopId() );
		if ( null != coupon && coupon.size() > 0 ) {
		    coupon = getCouponDuofen( coupon, yhqMoney, 1 );
		    shopMap.put( "coupon", coupon );
		}

		//查询多粉优惠券
		List< Map< String,Object > > duofenCoupon = (List< Map< String,Object > >) memberCard.get( "duofenCards" + store.getWxShopId() );
		if ( duofenCoupon != null && duofenCoupon.size() > 0 ) {
		    duofenCoupon = getCouponDuofen( duofenCoupon, yhqMoney, 0 );
		    shopMap.put( "duofenCoupon", duofenCoupon );
		}
	    }

	    //版本1.1新增开始
			/*shopMap.put("jifenProNum", jifenNum);//能使用积分的商品数量
			shopMap.put("jifenProMoney", jifenMoney);//能使用积分的商品总价

			shopMap.put("fenbiProNum", fenbiNum);//能使用粉币的商品数量
			shopMap.put("fenbiProMoney", fenbiMoney);//能使用粉币的商品总价
*/
	    shopMap.put( "yhqProNum", yhqNum );//能使用优惠券的商品数量
	    shopMap.put( "yhqProMoney", yhqMoney );//能使用优惠券的商品总价

			/*shopMap.put("unionProNum", unionNum);//能使用商家联盟的商品数量
			shopMap.put("unionProMoney", unionMoney);//能使用商家联盟的商品总价
*/            //版本1.1新增结束

	    List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	    cartList.add( shopMap );

	    resultMap.put( "shopList", cartList );
	}

	resultMap.put( "jifenProNum", jifenNum );//能使用积分的商品数量
	resultMap.put( "jifenProMoney", df.format( jifenMoney ) );//能使用积分的商品总价

	resultMap.put( "fenbiProNum", fenbiNum );//能使用粉币的商品数量
	resultMap.put( "fenbiProMoney", df.format( fenbiMoney ) );//能使用粉币的商品总价

	resultMap.put( "unionProNum", unionNum );//能使用商家联盟的商品数量
	resultMap.put( "unionProMoney", df.format( unionMoney ) );//能使用商家联盟的商品总价

	//查询是否能显示货到付款和找人代付的按钮
	Map< String,Object > huodaoMap = paySetService.isHuoDaoByUserId( member.getBusid() );

	resultMap.put( "isDaifu", huodaoMap.get( "isDaifu" ) );

	if ( proTypeId == 0 ) {
	    resultMap.put( "isHuoDao", huodaoMap.get( "isHuoDao" ) );
	} else {
	    resultMap.put( "isHuoDao", 0 );
	}
	if ( proTypeId == 0 ) {
	    Map< String,Object > fenbiParams = new HashMap< String,Object >();
	    fenbiParams.put( "jifenNum", memberCard.get( "integral" ) );
	    fenbiParams.put( "fenbiNum", memberCard.get( "fans_currency" ) );
	    fenbiParams.put( "jifenMoney", memberCard.get( "jifenMoeny" ) );
	    fenbiParams.put( "fenbiMoney", memberCard.get( "fenbiMoeny" ) );

	    Map< String,Object > map = countIntegralFenbi( member, fenbiParams );//获取积分、粉币抵扣金额
	    resultMap.putAll( map );
	}

	totalProMoney = CommonUtil.toDouble( df.format( totalProMoney ) );
	totalFreightMoney = CommonUtil.toDouble( df.format( totalFreightMoney ) );
	resultMap.put( "totalProMoney", totalProMoney );//商品总价
	resultMap.put( "totalFreightMoney", totalFreightMoney );//运费
	resultMap.put( "totalMoney", df.format( totalProMoney + totalFreightMoney ) );//实付金额
	resultMap.put( "totalDiscountMoney", totalDiscountMoney );//商品会员折扣总价

	resultMap.put( "isFlow", isFlow );//是否是流量充值商品   1是  0 不是

	int memType = 0;
	if ( CommonUtil.isNotEmpty( member ) ) {
	    memType = memberService.isCardType( member.getId() );
	}
	if ( memType == 3 ) {
	    resultMap.put( "isChuzhiCard", 1 );//是否是流量充值商品   1是  0 不是
	}

	return resultMap;

    }

    /**
     * 查询多粉优惠券的值
     *
     * @param duofenCoupon
     *
     * @return
     */
    public List< Map< String,Object > > getCouponDuofen( List< Map< String,Object > > duofenCoupon, double orderMoney, int type ) {
	List< Map< String,Object > > duofenList = new ArrayList< Map< String,Object > >();
	for ( Map< String,Object > map : duofenCoupon ) {
	    int card_type = -1;//卡券类型 0折扣券 1代金券
	    double cash_least_cost = 0;//满足条件
	    if ( type == 1 ) {//微信优惠券
		if ( CommonUtil.isNotEmpty( map.get( "card_type" ) ) ) {
		    String cardType = CommonUtil.toString( map.get( "card_type" ) );
		    if ( cardType.equals( "DISCOUNT" ) ) {//折扣券
			card_type = 0;
		    } else if ( cardType.equals( "CASH" ) ) {//代金券
			card_type = 1;
			cash_least_cost = CommonUtil.toDouble( map.get( "cash_least_cost" ) );
		    }
		}
	    } else {//多粉优惠券
		card_type = CommonUtil.toInteger( map.get( "card_type" ) );
		if ( card_type == 1 ) {//代金券
		    cash_least_cost = CommonUtil.toDouble( map.get( "cash_least_cost" ) );
		    //判断代金券可不可以叠加
		    if ( CommonUtil.toString( map.get( "addUser" ) ).equals( "1" ) ) {
			int least_cost = 1;
			if ( orderMoney > cash_least_cost ) {
			    least_cost = (int) ( orderMoney / cash_least_cost );
			    int countId = CommonUtil.toInteger( map.get( "countId" ) );
			    if ( least_cost >= countId ) {
				least_cost = countId;
			    }
			}
			map.put( "countId", least_cost );
		    }

		}
	    }
	    boolean flag = true;
	    if ( card_type == 1 ) {//代金券要达到满足的条件才可以使用
		if ( cash_least_cost > orderMoney ) {
		    flag = false;//不满足使用代金券的条件
		}
	    }
	    if ( flag ) {
		map.remove( "status" );
		map.remove( "outer_id" );
		map.remove( "custom_url" );
		map.remove( "fixed_term" );
		map.remove( "location_id_list" );
		map.remove( "public_id" );
		map.remove( "type" );
		map.remove( "ctime" );
		map.remove( "fixed_begin_term" );
		map.remove( "time_limit" );
		duofenList.add( map );
	    }
	}
	return duofenList;
    }

    public Map< String,Object > getIntegralFenbi( Member member ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();

	//TODO 需关连 paramSetMapper.findBybusId（）方法
	/*PublicParameterSet ps = null;
	//                paramSetMapper.findBybusId(member.getBusid());

	if ( CommonUtil.isNotEmpty( ps ) ) {
	    if ( ps.getIntegralratio() > 0 ) {
		resultMap.put( "jifen_dk_num", ps.getIntegralratio() );
	    }
	}*/
	//查询粉币抵扣的比例
	List< Map > currencyList = dictService.getDict( "1058" );
	if ( currencyList != null && currencyList.size() > 0 ) {
	    Map< String,Object > fenbiMap = currencyList.get( 0 );

	    double itemValue = Double.parseDouble( fenbiMap.get( "item_value" ).toString() );
	    resultMap.put( "fenbi_dk_num", itemValue );
	}
	return resultMap;
    }

    /**
     * 计算粉币和积分抵扣的最大值
     *
     * @param member
     * @param params
     *
     * @return
     */
    public Map< String,Object > countIntegralFenbi( Member member, Map< String,Object > params ) {
	Map< String,Object > map = new HashMap< String,Object >();
	double jifenNum = 0;
	double fenbiNum = 0;
	double jifenMoney = 0;
	double fenbiMoney = 0;
	if ( CommonUtil.isNotEmpty( params.get( "jifenNum" ) ) ) {
	    jifenNum = CommonUtil.toDouble( params.get( "jifenNum" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbiNum" ) ) ) {
	    fenbiNum = CommonUtil.toDouble( params.get( "fenbiNum" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "jifenMoney" ) ) ) {
	    jifenMoney = CommonUtil.toDouble( params.get( "jifenMoney" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbiMoney" ) ) ) {
	    fenbiMoney = CommonUtil.toDouble( params.get( "fenbiMoney" ) );
	}
	//查询积分抵扣的信息
	if ( jifenMoney > 0 ) {
	    map.put( "integral_money", jifenMoney );//积分可抵扣的金额
	    map.put( "integral", jifenNum );//积分数量
	}
	if ( fenbiMoney > 0 ) {
	    map.put( "fenbi_money", fenbiMoney );//粉币可抵扣的金额
	    map.put( "fenbi", fenbiNum );//粉币数量
	}
	int isJifen = 0;
	if ( map.containsKey( "integral_money" ) ) {
	    isJifen = 1;
	}
	int isFenbi = 0;
	if ( map.containsKey( "fenbi_money" ) ) {
	    isFenbi = 1;
	}
	map.put( "isJifen", isJifen );
	map.put( "isFenbi", isFenbi );
	return map;
    }

    private Map< String,Object > getAddressParams( Map< String,Object > map ) {
	Map< String,Object > addressMap = new HashMap<>();
	addressMap.put( "id", map.get( "id" ) );//地址id
	String address = CommonUtil.toString( map.get( "provincename" ) ) + CommonUtil.toString( map.get( "cityname" ) );
	if ( CommonUtil.isNotEmpty( map.get( "areaname" ) ) ) {
	    address += CommonUtil.toString( map.get( "areaname" ) );
	}
	address += CommonUtil.toString( map.get( "memAddress" ) );
	if ( CommonUtil.isNotEmpty( map.get( "memZipCode" ) ) ) {
	    address += CommonUtil.toInteger( map.get( "memZipCode" ) );
	}
	addressMap.put( "address_detail", address );//详细地址
	addressMap.put( "member_name", map.get( "memName" ) );//联系人姓名
	addressMap.put( "member_phone", map.get( "memPhone" ) );//联系人手机号码
	if ( CommonUtil.isNotEmpty( map.get( "memDefault" ) ) ) {
	    addressMap.put( "is_default", map.get( "memDefault" ) );//是否是默认选中地址
	}
	return addressMap;
    }

    @Override
    public Map< String,Object > calculationPreferential( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	DecimalFormat df = new DecimalFormat( "######0.00" );
	int code = 1;
	String msg = "";
	if ( CommonUtil.isEmpty( params.get( "order" ) ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整" );
	    return resultMap;
	}
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Member member = memberService.findMemberById( memberId, null );
	int shop_id = 0;
	Map memberCard = null;
	//判断库存
	List< MallOrder > orderList = new ArrayList<>();

	double jifenDiscountMoney = 0;//使用积分总共优惠的金额
	double fenbiDiscountMoney = 0;//使用粉币总共优惠的金额
	double yhqDiscountMoney = 0;//使用优惠券总共优惠的金额
	double unionDiscountMoney = 0;//使用商家联盟总共优惠的金额
	Map< String,Object > countMap = getIntegralFenbi( member );
	params.putAll( countMap );

	double orderTotalMoney = 0;//商品总价
	if ( CommonUtil.isNotEmpty( params.get( "totalAllMoney" ) ) ) {
	    orderTotalMoney = CommonUtil.toDouble( params.get( "totalAllMoney" ) );
	}
	double totalMoneys = 0;

	Map< String,Object > proMap = new HashMap< String,Object >();
	Map< String,Object > duofenCouponMap = new HashMap< String,Object >();//保存多粉优惠券的信息
	Map< String,Object > wxduofenCouponMap = new HashMap< String,Object >();//保存微信优惠券信息
	//判断库存
	JSONArray orderArray = JSONArray.parseArray( params.get( "order" ).toString() );
	if ( orderArray != null && orderArray.size() > 0 ) {
	    for ( int i = 0; i < orderArray.size(); i++ ) {
		Object object = orderArray.get( i );
		JSONObject orderObj = JSONObject.parseObject( object.toString() );
		MallOrder order = getOrderByParam( orderObj, member );
		shop_id = order.getShopId();
		memberCard = memberService.findMemberCardByMemberId( memberId, order.getShopId() );
		if ( CommonUtil.isNotEmpty( orderObj.get( "wxCoupon" ) ) ) {//已使用微信优惠券
		    wxduofenCouponMap.put( order.getShopId() + "", JSONObject.parseObject( orderObj.get( "wxCoupon" ).toString() ) );
		}
		//获取多粉优惠券的 满减优惠或折扣
		if ( CommonUtil.isNotEmpty( orderObj.get( "duofenCoupon" ) ) ) {//已使用多粉优惠券
		    duofenCouponMap.put( order.getShopId() + "", JSONObject.parseObject( orderObj.get( "duofenCoupon" ).toString() ) );
		}

		List< MallOrderDetail > detailList = new ArrayList< MallOrderDetail >();
		if ( CommonUtil.isNotEmpty( orderObj.get( "orderDetail" ) ) ) {
		    JSONArray detailArr = JSONArray.parseArray( orderObj.get( "orderDetail" ).toString() );
		    if ( detailArr != null && detailArr.size() > 0 ) {
			for ( int j = 0; j < detailArr.size(); j++ ) {
			    Object object2 = detailArr.get( j );

			    JSONObject detailObj = JSONObject.parseObject( object2.toString() );

			    int productId = CommonUtil.toInteger( detailObj.get( "productId" ) );
			    MallProduct product = null;

			    if ( CommonUtil.isEmpty( proMap ) ) {
				product = productService.selectByPrimaryKey( productId );
				proMap.put( product.getId().toString(), JSONObject.parseObject( JSON.toJSONString( product ) ) );
			    } else {
				if ( !proMap.containsKey( productId ) ) {
				    product = productService.selectByPrimaryKey( productId );
				    proMap.put( product.getId().toString(), JSONObject.parseObject( JSON.toJSONString( product ) ) );
				} else {
				    product = JSONObject.parseObject( proMap.get( productId ).toString(), MallProduct.class );
				}
			    }

			    MallOrderDetail detail = getOrderDetailByParam( detailObj, product );
			    detailList.add( detail );
			}
		    }
		}
		order.setMallOrderDetail( detailList );
		orderList.add( order );
	    }
	}
	Map< String,Object > youhuiMaps = new HashMap< String,Object >();
	youhuiMaps.put( "fenbi_money", params.get( "fenbi_money" ) );
	youhuiMaps.put( "fenbi", params.get( "fenbi" ) );
	youhuiMaps.put( "integral_money", params.get( "integral_money" ) );
	youhuiMaps.put( "integral", params.get( "integral" ) );
	params.put( "shop_id", shop_id );
	List< MallOrder > newOrderList = curYouhuiOrder( params, orderList, proMap, duofenCouponMap, wxduofenCouponMap, youhuiMaps, member );

	if ( CommonUtil.isNotEmpty( newOrderList ) ) {
	    orderList = new ArrayList< MallOrder >();
	    orderList.addAll( newOrderList );
	}
	int isUseFenbi = 0;
	int isUseJifen = 0;
	if ( CommonUtil.isNotEmpty( params.get( "isUseFenbi" ) ) ) {
	    isUseFenbi = CommonUtil.toInteger( params.get( "isUseFenbi" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "isUseJifen" ) ) ) {
	    isUseJifen = CommonUtil.toInteger( params.get( "isUseJifen" ) );
	}

	for ( MallOrder order : orderList ) {
	    totalMoneys += order.getOrderMoney().doubleValue();
	    unionDiscountMoney += order.getUnionDiscountMoney();
	    yhqDiscountMoney += order.getYhqDiscountMoney();
	    fenbiDiscountMoney += order.getFenbiDiscountMoney();
	    jifenDiscountMoney += order.getJifenDiscountMoney();
	}
	if ( isUseFenbi != 1 || isUseJifen != 1 ) {
	    Map< String,Object > fenbiParams = new HashMap< String,Object >();
	    fenbiParams.put( "jifenNum", memberCard.get( "integral" ) );
	    fenbiParams.put( "fenbiNum", memberCard.get( "fans_currency" ) );
	    fenbiParams.put( "jifenMoney", memberCard.get( "jifenMoeny" ) );
	    fenbiParams.put( "fenbiMoney", memberCard.get( "fenbiMoeny" ) );

	    Map< String,Object > map = countIntegralFenbi( member, fenbiParams );//获取积分、粉币抵扣金额
	    if ( CommonUtil.isNotEmpty( map.get( "integral_money" ) ) ) {
		resultMap.put( "integral_money", map.get( "integral_money" ) );//积分可抵扣的金额
	    } else {
		resultMap.put( "integral_money", 0 );//积分可抵扣的金额
	    }
	    if ( CommonUtil.isNotEmpty( map.get( "integral" ) ) ) {
		resultMap.put( "integral", map.get( "integral" ) );//积分数量
	    } else {
		resultMap.put( "integral", 0 );//积分数量
	    }
	    if ( CommonUtil.isNotEmpty( map.get( "fenbi_money" ) ) ) {
		resultMap.put( "fenbi_money", map.get( "fenbi_money" ) );//粉币可抵扣的金额
	    } else {
		resultMap.put( "fenbi_money", 0 );//粉币可抵扣的金额
	    }
	    if ( CommonUtil.isNotEmpty( map.get( "fenbi" ) ) ) {
		resultMap.put( "fenbi", map.get( "fenbi" ) );//粉币数量
	    } else {
		resultMap.put( "fenbi", 0 );//粉币数量
	    }
	}

	logger.info( "订单总金额：" + df.format( totalMoneys ) );

	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );

	resultMap.put( "unionDiscountMoney", df.format( unionDiscountMoney ) );
	resultMap.put( "yhqDiscountMoney", df.format( yhqDiscountMoney ) );
	resultMap.put( "fenbiDiscountMoney", df.format( fenbiDiscountMoney ) );
	resultMap.put( "jifenDiscountMoney", df.format( jifenDiscountMoney ) );

	double youhuiMoney = unionDiscountMoney + yhqDiscountMoney + fenbiDiscountMoney + jifenDiscountMoney;
	resultMap.put( "orderTotalMoney", df.format( orderTotalMoney - youhuiMoney ) );
	return resultMap;
    }

    public List< MallOrder > curYouhuiOrder( Map< String,Object > params, List< MallOrder > orderList, Map< String,Object > proMap, Map< String,Object > duofenCouponMap,
		    Map< String,Object > wxduofenCouponMap, Map< String,Object > youhuiMaps, Member member ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );

	double unionProMoney = 0;//保存能使用商家联盟的商品金额
	double yhqProMoney = 0;//保存能使用优惠券的商品总额
	double fenbiProMoney = 0;//保存能使用粉币的商品总额
	double jifenProMoney = 0;//保存能使用积分的商品总额
	int unionProNum = 0;
	int yhqProNum = 0;
	int fenbiProNum = 0;
	int jifenProNum = 0;

	List< Integer > list = new ArrayList< Integer >();
	int isUseUnion = 0;//是否已经使用商家联盟   1已经使用商家联盟     0没使用
	int isUseYhq = 0;//是否已经使用优惠券   1已经使用优惠券     0没使用
	int isUseFenbi = 0;//是否已经使用粉币  1已经使用粉币     0没使用
	int isUseJifen = 0;//是否已经使用积分 1已经使用积分   0没使用
	if ( CommonUtil.isNotEmpty( params.get( "isUseUnion" ) ) ) {
	    isUseUnion = CommonUtil.toInteger( params.get( "isUseUnion" ) );
	    if ( isUseUnion == 1 ) {
		list.add( 1 );
	    }
	}
	if ( CommonUtil.isNotEmpty( params.get( "isUseYhq" ) ) ) {
	    isUseYhq = CommonUtil.toInteger( params.get( "isUseYhq" ) );
	    if ( isUseYhq == 1 ) {
		list.add( 2 );
	    }
	}
	if ( CommonUtil.isNotEmpty( params.get( "isUseFenbi" ) ) ) {
	    isUseFenbi = CommonUtil.toInteger( params.get( "isUseFenbi" ) );
	    if ( isUseFenbi == 1 ) {
		list.add( 3 );
	    }
	}
	if ( CommonUtil.isNotEmpty( params.get( "isUseJifen" ) ) ) {
	    isUseJifen = CommonUtil.toInteger( params.get( "isUseJifen" ) );
	    if ( isUseJifen == 1 ) {
		list.add( 4 );
	    }
	}
	if ( list == null || list.size() <= 0 ) {
	    return null;
	}
	MallStore store = storeDAO.selectById( CommonUtil.toInteger( params.get( "shop_id" ) ) );
	Map memberCard = memberService.findCardAndShopIdsByMembeId( member.getId(), store.getWxShopId().toString() );
	Map< Integer,Object > yhqProductObj = new HashMap< Integer,Object >();//保存能使用优惠券的商品数量金额
	List< MallOrder > newOrderList = new ArrayList< MallOrder >();
	boolean isCur = false;
	if ( list != null && list.size() > 0 ) {
	    for ( int i = 0; i < list.size(); i++ ) {
		Integer index = list.get( i );
		int nextIndex = list.get( i );
		Map< String,Object > youhuiParams = new HashMap< String,Object >();
		youhuiParams.putAll( youhuiMaps );
		youhuiParams.put( "unionProMoney", df.format( unionProMoney ) );
		//				params.put("yhqProMoney", df.format(yhqProMoney));
		youhuiParams.put( "fenbiProMoney", df.format( fenbiProMoney ) );
		youhuiParams.put( "jifenProMoney", df.format( jifenProMoney ) );

		youhuiParams.put( "unionProNum", unionProNum );
		//				params.put("yhqProNum", yhqProNum);
		youhuiParams.put( "fenbiProNum", fenbiProNum );
		youhuiParams.put( "jifenProNum", jifenProNum );
		if ( index == 1 ) {//商家联盟
		    youhuiParams.put( "isUseUnion", 1 );
		} else if ( index == 2 ) {//优惠券
		    youhuiParams.put( "isUseYhq", 1 );
		    logger.info( "所有能使用优惠券：" + yhqProductObj );
		} else if ( index == 3 ) {//粉币
		    youhuiParams.put( "isUseFenbi", 1 );
		    youhuiParams.put( "fenbi_dk_num", params.get( "fenbi_dk_num" ) );
		    Map< String,Object > fenbiParams = new HashMap< String,Object >();
		    if ( CommonUtil.isNotEmpty( memberCard ) ) {
			fenbiParams.put( "fenbiNum", memberCard.get( "fans_currency" ) );
			fenbiParams.put( "fenbiMoney", memberCard.get( "fenbiMoeny" ) );
		    }
		    Map< String,Object > fenbi = countIntegralFenbi( member, fenbiParams );
		    youhuiParams.putAll( fenbi );
		    logger.info( fenbi );
		    logger.info( "所有能使用粉币的商品价格：" + fenbiProMoney + "---" + fenbiProNum );
		} else if ( index == 4 ) {//积分
		    youhuiParams.put( "isUseJifen", 1 );
		    youhuiParams.put( "jifen_dk_num", params.get( "jifen_dk_num" ) );
		    Map< String,Object > jifenParams = new HashMap< String,Object >();
		    if ( CommonUtil.isNotEmpty( memberCard ) ) {
			jifenParams.put( "jifenNum", memberCard.get( "integral" ) );
			jifenParams.put( "jifenMoney", memberCard.get( "jifenMoeny" ) );
		    }
		    Map< String,Object > jifen = countIntegralFenbi( member, jifenParams );
		    youhuiParams.putAll( jifen );
		    logger.info( jifen );
		    logger.info( "所有能使用积分的商品价格：" + jifenProMoney + "----" + jifenProNum );
		}
		if ( newOrderList != null && newOrderList.size() > 0 ) {
		    orderList = new ArrayList< MallOrder >();
		    orderList.addAll( newOrderList );
		    newOrderList = new ArrayList< MallOrder >();
		}

		for ( MallOrder order : orderList ) {
		    yhqProMoney = 0;
		    yhqProNum = 0;
		    double yhqYouhui = 0;//优惠券优惠的金额
		    double fenbiYouhui = 0;//粉币优惠的金额
		    double jifenYouhui = 0;//积分优惠的金额
		    double unionYouhui = 0;//商家联盟优惠的金额

		    int useJifen = 0;
		    double useFenbi = 0;
		    if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
			List< MallOrderDetail > newDetailList = new ArrayList< MallOrderDetail >();
			for ( int j = 0; j < order.getMallOrderDetail().size(); j++ ) {
			    MallOrderDetail detail = order.getMallOrderDetail().get( j );
			    MallProduct product = JSONObject.parseObject( proMap.get( detail.getProductId().toString() ).toString(), MallProduct.class );
			    if ( isCur ) {
				Map< String,Object > youhuiMap = calculateYouhui( youhuiParams, detail, product, index, duofenCouponMap, wxduofenCouponMap, i, yhqProductObj,
						member );

				if ( CommonUtil.isNotEmpty( youhuiMap ) ) {
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "unionDiscountMoney" ) ) ) {
					double money = CommonUtil.toDouble( youhuiMap.get( "unionDiscountMoney" ) );
					unionYouhui += money;
				    }
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "yhqDiscountMoney" ) ) ) {
					double money = CommonUtil.toDouble( youhuiMap.get( "yhqDiscountMoney" ) );
					yhqYouhui += money;
					youhuiMap.put( "yhqDiscountMoney", yhqYouhui );
				    }
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "fenbiDiscountMoney" ) ) ) {
					double money = CommonUtil.toDouble( youhuiMap.get( "fenbiDiscountMoney" ) );
					fenbiYouhui += money;
					youhuiMap.put( "fenbiDiscountMoney", fenbiYouhui );
				    }
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "jifenDiscountMoney" ) ) ) {
					double money = CommonUtil.toDouble( youhuiMap.get( "jifenDiscountMoney" ) );
					jifenYouhui += money;
					youhuiMap.put( "jifenDiscountMoney", jifenYouhui );
				    }
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "useJifenCount" ) ) ) {
					youhuiMap.put( "useJifen", useJifen );
				    }
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "useFenbiCount" ) ) ) {
					youhuiMap.put( "useFenbi", useFenbi );
				    }
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "coupon_code" ) ) ) {
					detail.setCouponCode( CommonUtil.toString( youhuiMap.get( "coupon_code" ) ) );
				    }
				    if ( CommonUtil.isNotEmpty( youhuiMap.get( "detail" ) ) ) {
					Object aaa = youhuiMap.get( "detail" );
					detail = JSONObject.parseObject( JSON.toJSONString( aaa ), MallOrderDetail.class );
				    }
				    if ( j + 1 == order.getMallOrderDetail().size() ) {
					youhuiMap.put( "yhqMoneyCount", 0 );
				    }
				    youhuiParams.putAll( youhuiMap );
				}
			    }
			    double price = detail.getTotalPrice();
			    if ( isCur ) {
				price = detail.getNewTotalPrice();
			    }

			    if ( i + 1 <= list.size() ) {
				if ( i > 0 || isCur ) {
				    if ( i + 1 < list.size() ) {
					nextIndex = list.get( i + 1 );
				    } else {
					nextIndex = -1;
				    }
				}
				if ( nextIndex == 1 ) {//商家联盟
				    unionProMoney += price;
				    ++unionProNum;
				} else if ( nextIndex == 2 ) {//优惠券
				    if ( CommonUtil.toString( product.getIsCoupons() ).equals( "1" ) ) {
					yhqProMoney += price;
					++yhqProNum;
				    }
				} else if ( nextIndex == 3 ) {//粉币
				    if ( CommonUtil.toString( product.getIsFenbiDeduction() ).equals( "1" ) ) {
					detail.setFenbiBeforeTotalPrice( price );
					fenbiProMoney += price;
					++fenbiProNum;
				    }
				} else if ( nextIndex == 4 ) {//积分
				    if ( CommonUtil.toString( product.getIsIntegralDeduction() ).equals( "1" ) ) {
					detail.setJifenBeforeTotalPrice( price );
					jifenProMoney += price;
					++jifenProNum;
				    }
				}
			    }
			    newDetailList.add( detail );
			}
			order.setMallOrderDetail( newDetailList );
		    }
		    if ( nextIndex == 2 ) {
			Map< String,Object > maps = new HashMap< String,Object >();
			maps.put( "yhqProMoney", df.format( yhqProMoney ) );
			maps.put( "yhqProNum", yhqProNum );
			yhqProductObj.put( order.getShopId(), maps );
		    }
		    if ( unionYouhui > 0 ) {
			order.setUnionDiscountMoney( unionYouhui );
		    }
		    if ( yhqYouhui > 0 ) {
			order.setYhqDiscountMoney( yhqYouhui );
		    }
		    if ( fenbiYouhui > 0 ) {
			order.setFenbiDiscountMoney( fenbiYouhui );
		    }
		    if ( jifenYouhui > 0 ) {
			order.setJifenDiscountMoney( jifenYouhui );
		    }
		    newOrderList.add( order );
		}
		if ( i == 0 && !isCur ) {
		    i = -1;
		    isCur = true;
		}
	    }
	    //			double totalMoneys = 0;
	    orderList = new ArrayList< MallOrder >();
	    orderList.addAll( newOrderList );
	    for ( MallOrder order : orderList ) {
		double orderMoney = 0;
		if ( order.getMallOrderDetail() != null ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			detail.setDetPrivivilege( BigDecimal.valueOf( detail.getTotalPrice() / detail.getDetProNum() ) );

			detail.setDiscountedPrices( BigDecimal.valueOf( CommonUtil.toDouble( df.format( detail.getTotalPrice() - detail.getNewTotalPrice() ) ) ) );//优惠价格

			detail.setTotalPrice( detail.getNewTotalPrice() );

			detail.setDetProPrice( BigDecimal.valueOf( detail.getNewTotalPrice() / detail.getDetProNum() ) );

			orderMoney += detail.getTotalPrice();

		    }
		}
		//				totalMoneys += orderMoney;
		order.setOrderMoney( order.getOrderFreightMoney().add( new BigDecimal( orderMoney ) ) );
		logger.info( "订单总金额：" + orderMoney );
	    }
	}

	return orderList;
    }

    /**
     * 计算优惠信息
     *
     * @param params
     * @param yhqProductObj 订单详情信息
     * @param detail        订单详情对象
     * @param product       商品
     *
     * @return
     */
    public Map< String,Object > calculateYouhui( Map< String,Object > params, MallOrderDetail detail, MallProduct product, int index, Map< String,Object > duofenCouponMap,
		    Map< String,Object > wxduofenCouponMap, int i, Map< Integer,Object > yhqProductObj, Member member ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	DecimalFormat df = new DecimalFormat( "######0.00" );

	//能使用优惠的商品数量和金额
		/*int unionProNum = 0;//能使用商家联盟的商品数量
		double unionProMoney = 0;//能使用商家联盟的商品总价
*/
	int yhqProNum = 0;//能使用优惠券的商品数量
	double yhqProMoney = 0;//能使用优惠券的商品总价
	int fenbiProNum = 0;//能使用粉币的商品数量
	double fenbiProMoney = 0;//能使用粉币的商品总价
	int jifenProNum = 0;//能使用积分的商品数量
	double jifenProMoney = 0;//能使用积分的商品总价
		/*if(CommonUtil.isNotEmpty(orderObj.get("unionProNum"))){
			unionProNum = CommonUtil.toInteger(orderObj.get("unionProNum"));
		}
		if(CommonUtil.isNotEmpty(orderObj.get("unionProMoney"))){
			unionProMoney = CommonUtil.toDouble(orderObj.get("unionProMoney"));
		}*/
		/*if(CommonUtil.isNotEmpty(params.get("yhqProNum"))){
			yhqProNum = CommonUtil.toInteger(params.get("yhqProNum"));
		}
		if(CommonUtil.isNotEmpty(params.get("yhqProMoney"))){
			yhqProMoney = CommonUtil.toDouble(params.get("yhqProMoney"));
		}*/
	if ( CommonUtil.isNotEmpty( yhqProductObj ) ) {
	    System.out.println( "优惠券信息：" + yhqProductObj );
	    System.out.println( "店铺id：" + detail.getShopId() );
	    if ( CommonUtil.isNotEmpty( yhqProductObj.get( detail.getShopId() ) ) ) {
		JSONObject obj = JSONObject.parseObject( JSON.toJSONString( yhqProductObj.get( detail.getShopId() ) ) );
		if ( CommonUtil.isNotEmpty( obj.get( "yhqProNum" ) ) ) {
		    yhqProNum = CommonUtil.toInteger( obj.get( "yhqProNum" ) );
		}
		if ( CommonUtil.isNotEmpty( obj.get( "yhqProMoney" ) ) ) {
		    yhqProMoney = CommonUtil.toDouble( obj.get( "yhqProMoney" ) );
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbiProNum" ) ) ) {
	    fenbiProNum = CommonUtil.toInteger( params.get( "fenbiProNum" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbiProMoney" ) ) ) {
	    fenbiProMoney = CommonUtil.toDouble( params.get( "fenbiProMoney" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "jifenProNum" ) ) ) {
	    jifenProNum = CommonUtil.toInteger( params.get( "jifenProNum" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "jifenProMoney" ) ) ) {
	    jifenProMoney = CommonUtil.toDouble( params.get( "jifenProMoney" ) );
	}

	//是否已经使用积分，粉币和商家联盟
	int isUseUnion = 0;//是否已经使用商家联盟   1已经使用商家联盟     0没使用
	int isUseFenbi = 0;//是否已经使用粉币  1已经使用粉币     0没使用
	int isUseJifen = 0;//是否已经使用积分 1已经使用积分   0没使用
	if ( CommonUtil.isNotEmpty( params.get( "isUseUnion" ) ) ) {
	    isUseUnion = CommonUtil.toInteger( params.get( "isUseUnion" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "isUseFenbi" ) ) ) {
	    isUseFenbi = CommonUtil.toInteger( params.get( "isUseFenbi" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "isUseJifen" ) ) ) {
	    isUseJifen = CommonUtil.toInteger( params.get( "isUseJifen" ) );
	}

	double unionDiscount = 0;//商家联盟的折扣数
	double fenbi_money = 0;//粉币可抵扣的金额
	double fenbi = 0;//粉币数量
	double integral_money = 0;//积分可抵扣的金额
	double integral = 0;//积分数量
	if ( CommonUtil.isNotEmpty( params.get( "unionDiscount" ) ) ) {
	    unionDiscount = CommonUtil.toDouble( params.get( "unionDiscount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbi_money" ) ) ) {
	    fenbi_money = CommonUtil.toDouble( params.get( "fenbi_money" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbi" ) ) ) {
	    fenbi = CommonUtil.toDouble( params.get( "fenbi" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "integral_money" ) ) ) {
	    integral_money = CommonUtil.toDouble( params.get( "integral_money" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "integral" ) ) ) {
	    integral = CommonUtil.toDouble( params.get( "integral" ) );
	}

	int yhqNumCount = 0;//累计使用优惠券的商品个数，用于算最后的差价
	int fenbiNumCount = 0;//累计使用粉币优惠的商品个数，用于算最后的差价
	int jifenNumCount = 0;//累计使用积分的商品个数，用于算最后的差价
	double yhqMoneyCount = 0;//累计使用优惠券优惠的优惠金额，用于算最后的差价
	double fenbiMoneyCount = 0;//累计使用粉币优惠的优惠金额，用于算最后的差价
	double jifenMoneyCount = 0;//累计使用积分的优惠金额，用于算最后的差价

	double useFenbiCount = 0;//累计使用粉币的数量，用于算差价
	int useJifenCount = 0;//累计使用积分的数量，用于算差价

	if ( CommonUtil.isNotEmpty( params.get( "yhqNumCount" ) ) ) {
	    yhqNumCount = CommonUtil.toInteger( params.get( "yhqNumCount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbiNumCount" ) ) ) {
	    fenbiNumCount = CommonUtil.toInteger( params.get( "fenbiNumCount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "jifenNumCount" ) ) ) {
	    jifenNumCount = CommonUtil.toInteger( params.get( "jifenNumCount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "yhqMoneyCount" ) ) ) {
	    yhqMoneyCount = CommonUtil.toDouble( params.get( "yhqMoneyCount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "fenbiMoneyCount" ) ) ) {
	    fenbiMoneyCount = CommonUtil.toDouble( params.get( "fenbiMoneyCount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "jifenMoneyCount" ) ) ) {
	    jifenMoneyCount = CommonUtil.toDouble( params.get( "jifenMoneyCount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "useFenbiCount" ) ) ) {
	    useFenbiCount = CommonUtil.toDouble( params.get( "useFenbiCount" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "useJifenCount" ) ) ) {
	    useJifenCount = CommonUtil.toInteger( params.get( "useJifenCount" ) );
	}

	double chaFenbiMoney = 0;
	double chaJifenMoney = 0;
	if ( CommonUtil.isNotEmpty( params.get( "chaFenbiMoney" ) ) ) {
	    chaFenbiMoney = CommonUtil.toDouble( params.get( "chaFenbiMoney" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "chaJifenMoney" ) ) ) {
	    chaJifenMoney = CommonUtil.toDouble( params.get( "chaJifenMoney" ) );
	}

	double totalPrice = detail.getTotalPrice();
	double price = detail.getTotalPrice();
	if ( i > 0 ) {
	    totalPrice = detail.getNewTotalPrice();
	    price = detail.getNewTotalPrice();
	}

	double unionDiscountMoney = 0;//联盟卡优惠的金额
	double yhqDiscountMoney = 0;//优惠券优惠的金额
	double fenbiDiscountMoney = 0;//粉币优惠的金额
	double jifenDiscountMoney = 0;//积分优惠的金额

	//		double useFenbiTotal = 0;//使用粉币数量
	//		int useJifenTotal = 0;//使用积分数量

	logger.info( "商品初始价格：" + price );

	//计算商家联盟的优惠
	if ( isUseUnion == 1 && unionDiscount > 0 && index == 1 ) {//已使用商家联盟
	    price = priceSubstring( ( totalPrice * unionDiscount / 10 ) + "", 3 );//计算联盟折扣后的最终价格
	    unionDiscountMoney = CommonUtil.subtract( totalPrice, price );//计算联盟折扣优惠的价格
	    unionDiscountMoney += unionDiscountMoney;
	    //++unionMoneyCount;
	}
	//计算优惠券的优惠
	if ( CommonUtil.toString( product.getIsCoupons() ).equals( "1" ) && price > 0 && index == 2 ) {//能使用优惠券
	    boolean yhqFlag = false;
	    double cashCose = 0;//使用代金券 要满足的条件
	    double reduceCost = 0;//要减免的金额
	    int cardType = 0;//卡券类型 0折扣券 1代金券
	    double discount = 0;//折扣数
	    double hyqCanYouhui = 0;//能使用优惠券的商品总共能优惠的金额
	    String couponCode = "";//微信优惠券
	    int couponType = 0;//优惠券类型(1微信 2多粉),
	    JSONObject couponObjs = null;
	    //获取微信优惠券的 满减优惠或折扣
	    if ( CommonUtil.isNotEmpty( wxduofenCouponMap ) ) {//已使用微信优惠券
		if ( CommonUtil.isNotEmpty( wxduofenCouponMap.get( detail.getShopId().toString() ) ) ) {
		    JSONObject wxCouponObj = JSONObject.parseObject( wxduofenCouponMap.get( detail.getShopId().toString() ).toString() );
		    couponObjs = wxCouponObj;
		    if ( CommonUtil.isNotEmpty( wxCouponObj.get( "card_type" ) ) ) {
			if ( wxCouponObj.getString( "card_type" ).equals( "CASH" ) ) {//代金券
			    cashCose = wxCouponObj.getDouble( "cash_least_cost" );//使用代金券 要满足的条件
			    reduceCost = wxCouponObj.getDouble( "reduce_cost" );//要减免的金额

			    cardType = 1;
			} else if ( wxCouponObj.getString( "card_type" ).equals( "DISCOUNT" ) ) {//折扣券
			    discount = wxCouponObj.getDouble( "discount" );//折扣数
			    cardType = 0;
			}
		    }
		    if ( CommonUtil.isNotEmpty( wxCouponObj.getString( "user_card_code" ) ) ) {
			couponCode = wxCouponObj.getString( "user_card_code" );
			couponType = 1;
		    }
		}
	    }
	    //获取多粉优惠券的 满减优惠或折扣
	    if ( CommonUtil.isNotEmpty( duofenCouponMap ) ) {//已使用微信优惠券
		if ( CommonUtil.isNotEmpty( duofenCouponMap.get( detail.getShopId().toString() ) ) ) {
		    System.out.println( duofenCouponMap.get( detail.getShopId().toString() ) );
		    JSONObject duofenCouponObj = JSONObject.parseObject( JSON.toJSONString( duofenCouponMap.get( detail.getShopId().toString() ) ) );
		    couponObjs = duofenCouponObj;
		    if ( CommonUtil.isNotEmpty( duofenCouponObj.get( "card_type" ) ) ) {
			cardType = CommonUtil.toInteger( duofenCouponObj.get( "card_type" ) );
			if ( CommonUtil.toString( duofenCouponObj.get( "card_type" ) ).equals( "1" ) ) {//代金券
			    cashCose = duofenCouponObj.getDouble( "cash_least_cost" );//使用代金券 要满足的条件
			    reduceCost = duofenCouponObj.getDouble( "reduce_cost" );//要减免的金额
			    if ( CommonUtil.toString( duofenCouponObj.get( "addUser" ) ).equals( "1" ) ) {//代金券可以叠加使用
				int count = CommonUtil.toInteger( duofenCouponObj.get( "countId" ) );
				reduceCost = reduceCost * count;

				int cardId = Integer.parseInt( duofenCouponObj.get( "cId" ).toString() );
				//TODO 需关连卡券方法
				//  couponCode = duofenCardService.findCardCode(cardId, member.getId(), count);

			    }
			} else {
			    discount = duofenCouponObj.getDouble( "discount" );

			}
		    }

		    if ( CommonUtil.isNotEmpty( duofenCouponObj.getString( "code" ) ) && CommonUtil.isEmpty( couponCode ) ) {
			couponCode = duofenCouponObj.getString( "code" );
			couponType = 2;
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( couponCode ) ) {
		detail.setCouponCode( couponCode );
	    }

	    double newPrice = price;
	    if ( cardType == 1 ) {//代金券
		if ( yhqProMoney > cashCose ) {//能使用优惠券的商品金额大于 满减条件
		    double f = CommonUtil.div( price, yhqProMoney, 2 );
		    yhqDiscountMoney = CommonUtil.multiply( f, reduceCost );//算出一个商品要优惠的金额

		    if ( price < yhqDiscountMoney ) {//如果优惠的金额，小于 商品的单价
			yhqDiscountMoney = price;
		    }
		    newPrice = CommonUtil.subtract( price, yhqDiscountMoney );//价格
		    yhqFlag = true;
		    hyqCanYouhui = reduceCost;
		}
		cardType = 1;
	    } else if ( cardType == 0 ) {//折扣券
		if ( discount > 0 ) {
		    discount = discount / 10;
		    newPrice = CommonUtil.multiply( price, discount );//计算商品的折扣数
		    if ( totalPrice < newPrice ) {
			newPrice = totalPrice;
		    }
		    yhqDiscountMoney = CommonUtil.subtract( price, newPrice );//算出一个商品要优惠的金额
		    yhqFlag = true;
		    double proTotal = CommonUtil.multiply( yhqProMoney, discount );//计算能优惠商品的折扣金额
		    hyqCanYouhui = CommonUtil.subtract( yhqProMoney, proTotal );//所有商品能够优惠的金额
		}
		cardType = 0;
	    }
	    if ( yhqFlag ) {
		if ( price < 1 ) {
		    yhqDiscountMoney = price;
		}
		yhqDiscountMoney = CommonUtil.toDouble( df.format( yhqDiscountMoney ) );
		newPrice = CommonUtil.toDouble( df.format( newPrice ) );
		//如果已经使用了优惠券的商品个数大于等于能使用商品的个数   ，则表示已经是最后一个能使用优惠券的商品
		if ( yhqNumCount + 1 >= yhqProNum && yhqProNum > 0 && yhqMoneyCount + yhqDiscountMoney != yhqProMoney ) {//已经是最后一个能使用优惠券的商品
		    if ( hyqCanYouhui > 0 ) {
			yhqDiscountMoney = CommonUtil.subtract( hyqCanYouhui, yhqMoneyCount );//算出一个商品要优惠的金额
			newPrice = CommonUtil.subtract( price, yhqDiscountMoney );//计算商品优惠后的价格
		    }
		}
		if ( newPrice < 0 ) {
		    //jifenDiscountMoney = price;
		    newPrice = 0;
		} else if ( yhqDiscountMoney > price ) {
		    //fenbiDiscountMoney = newPrice;
		    newPrice = 0;
		}
		if ( yhqMoneyCount + yhqDiscountMoney > hyqCanYouhui ) {//如果优惠的价格大于
		    yhqDiscountMoney = CommonUtil.subtract( hyqCanYouhui, yhqMoneyCount );
		    newPrice = CommonUtil.subtract( price, yhqDiscountMoney );
		}
		yhqDiscountMoney = CommonUtil.toDouble( df.format( yhqDiscountMoney ) );
		newPrice = CommonUtil.toDouble( df.format( newPrice ) );
		logger.info( "优惠券优惠前的价格：" + price + "；优惠后的价格：" + newPrice + "--优惠了：" + yhqDiscountMoney + "元" );
		price = newPrice;
		yhqMoneyCount += yhqDiscountMoney;
		++yhqNumCount;

		if ( couponType > 0 ) {
		    JSONObject duofenObj = new JSONObject();
		    MallStore store = mallStoreService.selectById( detail.getShopId() );
		    duofenObj.put( "wxShopId", store.getWxShopId() );
		    duofenObj.put( "couponCode", couponCode );//优惠券的code
		    duofenObj.put( "shopId", detail.getShopId() );//店铺id
		    duofenObj.put( "couponType", couponType );
		    duofenObj.put( "cardType", cardType );//卡券类型 0折扣券 1代金券
		    duofenObj.put( "useCoupon", 1 );
		    if ( couponType == 1 ) {
			duofenObj.put( "coupondId", couponObjs.get( "id" ) );//领取卡券Id
		    } else {
			duofenObj.put( "coupondId", couponObjs.get( "gId" ) );//领取卡券Id
		    }
		    detail.setUseCardId( CommonUtil.toInteger( duofenObj.get( "coupondId" ) ) );
		    detail.setDuofenCoupon( JSONObject.parse( duofenObj.toJSONString() ).toString() );
		}
	    }

	}
	double useFenbi = 0;
	//计算粉币的优惠
	if ( isUseFenbi == 1 && CommonUtil.toString( product.getIsFenbiDeduction() ).equals( "1" ) && price > 0 && index == 3 ) {//判断商品是否能使用粉币
	    if ( price > 1 ) {
		double f = CommonUtil.div( price, fenbiProMoney, 2 );
		fenbiDiscountMoney = CommonUtil.multiply( f, fenbi_money );
	    } else {
		fenbiDiscountMoney = price;
	    }

	    double newPrice = CommonUtil.subtract( price, fenbiDiscountMoney );//计算商品优惠后的价格

	    //计算使用粉币的数量
	    useFenbi = CommonUtil.div( fenbi, fenbiProNum, 2 );

	    fenbiDiscountMoney = CommonUtil.toDouble( df.format( fenbiDiscountMoney ) );
	    newPrice = CommonUtil.toDouble( df.format( newPrice ) );
	    useFenbi = CommonUtil.toDouble( df.format( useFenbi ) );

	    //最后一个能积分抵扣的商品
	    if ( fenbiNumCount + 1 >= fenbiProNum && fenbiMoneyCount + fenbiDiscountMoney != fenbiProMoney ) {
		fenbiDiscountMoney = CommonUtil.subtract( fenbi_money, fenbiMoneyCount );
		newPrice = CommonUtil.subtract( price, fenbiDiscountMoney );

		useFenbi = CommonUtil.subtract( fenbi, useFenbiCount );
	    }
	    if ( useFenbi < 0 ) {
		useFenbi = 0;
	    }
	    useFenbiCount += useFenbi;
	    if ( newPrice < 0 && fenbiDiscountMoney - price > 0 ) {
		chaFenbiMoney = fenbiDiscountMoney - price;
		fenbiDiscountMoney = price;
		newPrice = 0;
	    }
	    if ( fenbiMoneyCount + fenbiDiscountMoney > fenbi_money ) {//如果优惠的价格大于
		fenbiDiscountMoney = CommonUtil.subtract( fenbi_money, fenbiMoneyCount );
		newPrice = CommonUtil.subtract( price, fenbiDiscountMoney );
	    }
	    fenbiDiscountMoney = CommonUtil.toDouble( df.format( fenbiDiscountMoney ) );
	    newPrice = CommonUtil.toDouble( df.format( newPrice ) );

	    logger.info( "粉币优惠前的价格：" + price + "；优惠后的价格：" + newPrice + "--优惠了：" + fenbiDiscountMoney + "元" );
	    price = newPrice;
	    fenbiMoneyCount += fenbiDiscountMoney;
	    ++fenbiNumCount;

	}
	double useJifen = 0;
	//计算积分的优惠
	if ( isUseJifen == 1 && CommonUtil.toString( product.getIsIntegralDeduction() ).equals( "1" ) && price > 0 && index == 4 ) {//判断商品是否能使用积分
	    if ( price > 1 ) {
		double f = CommonUtil.div( price, jifenProMoney, 2 );
		jifenDiscountMoney = CommonUtil.multiply( f, integral_money );
	    } else {
		jifenDiscountMoney = price;
	    }

	    double newPrice = CommonUtil.subtract( price, jifenDiscountMoney );//计算商品优惠后的价格

	    //计算使用粉币的数量
	    useJifen = CommonUtil.div( integral, jifenProNum, 2 );
	    jifenDiscountMoney = CommonUtil.toDouble( df.format( jifenDiscountMoney ) );
	    newPrice = CommonUtil.toDouble( df.format( newPrice ) );
	    useJifen = CommonUtil.toDouble( df.format( useJifen ) );
	    //最后一个能积分抵扣的商品
	    if ( jifenNumCount + 1 >= jifenProNum && jifenMoneyCount + jifenDiscountMoney != jifenProMoney ) {
		jifenDiscountMoney = CommonUtil.subtract( integral_money, jifenMoneyCount );
		newPrice = CommonUtil.subtract( price, jifenDiscountMoney );

		useJifen = CommonUtil.subtract( integral, useJifenCount );
	    }
	    if ( useJifen < 0 ) {
		useJifen = 0;
	    }
	    useJifenCount += useJifen;
	    if ( newPrice < 0 && jifenDiscountMoney - price > 0 ) {
		chaJifenMoney = jifenDiscountMoney - price;
		jifenDiscountMoney = price;
		newPrice = 0;
	    }
	    if ( jifenMoneyCount + jifenDiscountMoney > integral_money ) {//如果优惠的价格大于
		jifenDiscountMoney = CommonUtil.subtract( integral_money, jifenMoneyCount );
		newPrice = CommonUtil.subtract( price, jifenDiscountMoney );
	    }
	    jifenDiscountMoney = CommonUtil.toDouble( df.format( jifenDiscountMoney ) );
	    newPrice = CommonUtil.toDouble( df.format( newPrice ) );
	    logger.info( "积分优惠前的价格：" + price + "；优惠后的价格：" + newPrice + "--优惠了：" + jifenDiscountMoney + "元" );
	    price = newPrice;
	    jifenMoneyCount += jifenDiscountMoney;
	    ++jifenNumCount;
	}

	resultMap.put( "chaFenbiMoney", chaFenbiMoney );
	resultMap.put( "chaJifenMoney", chaJifenMoney );

	resultMap.put( "yhqNumCount", yhqNumCount );//累计使用优惠券的商品个数，用于算差价
	resultMap.put( "fenbiNumCount", fenbiNumCount );//累计使用粉币的商品个数，用于算差价
	resultMap.put( "jifenNumCount", jifenNumCount );//累计使用积分的商品个数，用于算差价

	resultMap.put( "yhqMoneyCount", yhqMoneyCount );//累计使用优惠券的优惠金额，用于算差价
	resultMap.put( "fenbiMoneyCount", fenbiMoneyCount );//累计使用粉币的优惠金额，用于算差价
	resultMap.put( "jifenMoneyCount", jifenMoneyCount );//累计使用积分的优惠金额，用于算差价

	resultMap.put( "unionDiscountMoney", unionDiscountMoney );//联盟卡优惠的金额
	resultMap.put( "yhqDiscountMoney", yhqDiscountMoney );//优惠券优惠的金额
	resultMap.put( "fenbiDiscountMoney", fenbiDiscountMoney );//粉币优惠的金额
	resultMap.put( "jifenDiscountMoney", jifenDiscountMoney );//积分优惠的金额

	resultMap.put( "useFenbiCount", useFenbiCount );
	resultMap.put( "useJifenCount", useJifenCount );

	if ( jifenDiscountMoney > 0 ) {
	    detail.setIntegralYouhui( BigDecimal.valueOf( jifenDiscountMoney ) );
	    if ( useJifen > 0 ) {
		detail.setUseJifen( useJifen );
		logger.info( "使用了积分：" + detail.getUseJifen() + "，优惠了：" + jifenDiscountMoney );
	    }
	}
	if ( fenbiDiscountMoney > 0 ) {
	    detail.setFenbiYouhui( BigDecimal.valueOf( fenbiDiscountMoney ) );
	    if ( useFenbi > 0 ) {
		detail.setUseFenbi( useFenbi );
		logger.info( "使用了粉币：" + detail.getUseFenbi() + "，优惠了：" + fenbiDiscountMoney );
	    }
	}

	detail.setNewTotalPrice( price );//优惠后的总价
	resultMap.put( "price", price );

	logger.info( "商品优惠后的单价：" + price );
	resultMap.put( "detail", detail );
	return resultMap;
    }

    public double priceSubstring( String p, int num ) {
	double price = 0;
	try {
	    int n = p.indexOf( "." );
	    if ( n > 0 && n + num < p.length() ) {
		price = Double.parseDouble( p.substring( 0, n + num ) );
	    } else {
		price = Double.parseDouble( p );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    price = Double.parseDouble( p );
	}
	return price;
    }

    private MallOrder getOrderByParam( JSONObject orderObj, Member member ) {
	MallOrder order = new MallOrder();
	if ( CommonUtil.isNotEmpty( orderObj.get( "receiveId" ) ) ) {
	    order.setReceiveId( CommonUtil.toInteger( orderObj.get( "receiveId" ) ) );
	}
	if ( CommonUtil.isNotEmpty( order.getReceiveId() ) ) {
	    if ( order.getReceiveId() > 0 ) {
		MemberAddress address = memberAddressService.addreSelectId( order.getReceiveId() );
		if ( CommonUtil.isNotEmpty( address ) ) {
		    order.setReceiveName( CommonUtil.toString( address.getMemName() ) );
		    order.setReceivePhone( CommonUtil.toString( address.getMemPhone() ) );
		    String addDetail = address.getProvincename() + address.getCityname();
		    if ( CommonUtil.isNotEmpty( address.getAreaname() ) ) {
			addDetail += address.getAreaname();
		    }
		    addDetail += address.getMemAddress();
		    if ( CommonUtil.isNotEmpty( address.getMemZipCode() ) ) {
			addDetail += address.getMemZipCode();
		    }
		    order.setReceiveAddress( addDetail );
		}
	    }
	}
	order.setOrderMoney( CommonUtil.toBigDecimal( orderObj.get( "orderMoney" ) ) );
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderFreightMoney" ) ) ) {
	    order.setOrderFreightMoney( CommonUtil.toBigDecimal( orderObj.get( "orderFreightMoney" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderOldMoney" ) ) ) {
	    order.setOrderOldMoney( CommonUtil.toBigDecimal( orderObj.get( "orderOldMoney" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderBuyerMessage" ) ) ) {
	    order.setOrderBuyerMessage( CommonUtil.toString( orderObj.get( "orderBuyerMessage" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderPayWay" ) ) ) {
	    order.setOrderPayWay( CommonUtil.toInteger( orderObj.get( "orderPayWay" ) ) );
	}
		/*order.setBuyerUserId(CommonUtil.toInteger(orderObj.get("buyerUserId")));
		order.setBusUserId(CommonUtil.toInteger(orderObj.get("busUserId")));*/
	order.setOrderStatus( 1 );
	if ( CommonUtil.isNotEmpty( orderObj.get( "deliveryMethod" ) ) ) {
	    order.setDeliveryMethod( CommonUtil.toInteger( orderObj.get( "deliveryMethod" ) ) );
	    if ( order.getDeliveryMethod() == 2 ) {//上门自提
		order.setAppointmentName( CommonUtil.toString( orderObj.get( "appointmentName" ) ) );
		order.setAppointmentTelephone( CommonUtil.toString( orderObj.get( "appointmentTelephone" ) ) );
		order.setAppointmentTime( DateTimeKit.parse( CommonUtil.toString( orderObj.get( "appointmentTime" ) ), "" ) );
		order.setTakeTheirId( CommonUtil.toInteger( orderObj.get( "takeTheirId" ) ) );
		order.setAppointmentStartTime( CommonUtil.toString( orderObj.get( "appointmentStartTime" ) ) );
		order.setAppointmentEndTime( CommonUtil.toString( orderObj.get( "appointmentEndTime" ) ) );
	    }
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderType" ) ) ) {
	    order.setOrderType( CommonUtil.toInteger( orderObj.get( "orderType" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "pJoinId" ) ) ) {
	    order.setPJoinId( CommonUtil.toInteger( orderObj.get( "pJoinId" ) ) );
	}
	order.setBuyerUserId( member.getId() );
	order.setBusUserId( member.getBusid() );
	if ( CommonUtil.isNotEmpty( member.getNickname() ) ) {
	    order.setMemberName( member.getNickname() );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "flowPhone" ) ) ) {
	    order.setFlowPhone( CommonUtil.toString( orderObj.get( "flowPhone" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "shopId" ) ) ) {
	    order.setShopId( CommonUtil.toInteger( orderObj.get( "shopId" ) ) );
	}
	order.setBuyerUserType( 3 );
	return order;
    }

    private MallOrderDetail getOrderDetailByParam( JSONObject detailObj, MallProduct product ) {
	MallOrderDetail detail = new MallOrderDetail();
	detail.setProductId( CommonUtil.toInteger( detailObj.get( "productId" ) ) );
	detail.setShopId( product.getShopId() );

	String imageUrl = CommonUtil.toString( detailObj.get( "productImageUrl" ) );
	if ( imageUrl.indexOf( "/image" ) >= 0 ) {
	    imageUrl = imageUrl.substring( imageUrl.indexOf( "/image" ), imageUrl.length() );
	} else if ( imageUrl.indexOf( "/upload" ) >= 0 ) {
	    imageUrl = imageUrl.substring( imageUrl.indexOf( "/upload" ) + 7, imageUrl.length() );
	}
	detail.setProductImageUrl( imageUrl );

	detail.setDetProNum( CommonUtil.toInteger( detailObj.get( "detProNum" ) ) );
	detail.setDetProPrice( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "detProPrice" ) ) ) );

	detail.setTotalPrice( CommonUtil.toDouble( detailObj.get( "totalPrice" ) ) );
	detail.setDetPrivivilege( product.getProCostPrice() );
	if ( CommonUtil.isNotEmpty( detailObj.get( "productSpecificas" ) ) ) {
	    detail.setProductSpecificas( CommonUtil.toString( detailObj.get( "productSpecificas" ) ) );
	    detail.setDetPrivivilege( product.getProPrice() );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "productSpeciname" ) ) ) {
	    detail.setProductSpeciname( CommonUtil.toString( detailObj.get( "productSpeciname" ) ) );
	    detail.setProductSpeciname( CommonUtil.getBytes( detail.getProductSpeciname() ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "discountedPrices" ) ) ) {
	    detail.setDiscountedPrices( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "discountedPrices" ) ) ) );
	}
	detail.setDetProName( product.getProName() );
	if ( CommonUtil.isNotEmpty( product.getProCode() ) ) {
	    detail.setDetProCode( product.getProCode() );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "detProMessage" ) ) ) {
	    detail.setDetProMessage( CommonUtil.toString( detailObj.get( "detProMessage" ) ) );
	}
	detail.setReturnDay( product.getReturnDay() );
	if ( CommonUtil.isNotEmpty( detailObj.get( "discount" ) ) ) {
	    detail.setDiscount( CommonUtil.toInteger( detailObj.get( "discount" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "couponCode" ) ) ) {
	    detail.setCouponCode( CommonUtil.toString( detailObj.get( "couponCode" ) ) );
	}
	detail.setProTypeId( CommonUtil.toInteger( product.getProTypeId() ) );
	if ( CommonUtil.isNotEmpty( detailObj.get( "useFenbi" ) ) ) {
	    detail.setUseFenbi( CommonUtil.toDouble( detailObj.get( "useFenbi" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "useJifen" ) ) ) {
	    detail.setUseFenbi( CommonUtil.toDouble( detailObj.get( "useJifen" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "proSpecStr" ) ) ) {
	    detail.setProSpecStr( CommonUtil.toString( detailObj.get( "proSpecStr" ) ) );
	}
	if ( CommonUtil.isNotEmpty( product.getCardType() ) ) {
	    detail.setCardReceiveId( product.getCardType() );
	}
		/*if(CommonUtil.isNotEmpty(detailObj.get("cardReceiveId"))){
			detail.setCardReceiveId(CommonUtil.toInteger(detailObj.get("cardReceiveId")));
		}*/
	if ( CommonUtil.isNotEmpty( detailObj.get( "duofenCoupon" ) ) ) {
	    detail.setDuofenCoupon( CommonUtil.toString( detailObj.getString( "duofenCoupon" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "useCardId" ) ) ) {
	    detail.setUseCardId( CommonUtil.toInteger( detailObj.get( "useCardId" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "commission" ) ) ) {
	    detail.setCommission( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "commission" ) ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "saleMemberId" ) ) ) {
	    detail.setSaleMemberId( CommonUtil.toInteger( detailObj.get( "saleMemberId" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "jifen_youhui" ) ) ) {
	    detail.setIntegralYouhui( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "jifen_youhui" ) ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "fenbi_youhui" ) ) ) {
	    detail.setFenbiYouhui( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "fenbi_youhui" ) ) ) );
	}
	detail.setStatus( -3 );
	return detail;
    }

    @Override
    public Map< String,Object > submitOrder( Map< String,Object > params ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int code = 1;
	String msg = "";
	if ( CommonUtil.isEmpty( params.get( "order" ) ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整" );
	    return resultMap;
	}
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	int shop_id = 0;
	Member member = memberService.findMemberById( memberId, null );
	WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( member.getBusid() );
	int orderPayWay = 0;
	int orderTypeId = 0;
	//判断库存
	List< MallOrder > orderList = new ArrayList<>();
	double totalPrimary = 0;

	int memType = memberService.isCardType( member.getId() );

	Map< String,Object > countMap = getIntegralFenbi( member );
	params.putAll( countMap );
	Map< String,Object > proMap = new HashMap< String,Object >();
	Map< String,Object > duofenCouponMap = new HashMap< String,Object >();//保存多粉优惠券的信息
	Map< String,Object > wxduofenCouponMap = new HashMap< String,Object >();//保存微信优惠券信息
	//判断库存
	JSONArray orderArray = JSONArray.parseArray( params.get( "order" ).toString() );
	if ( orderArray != null && orderArray.size() > 0 ) {
	    for ( Object object : orderArray ) {
		if ( code == -1 ) {
		    break;
		}
		JSONObject orderObj = JSONObject.parseObject( object.toString() );
		MallOrder order = getOrderByParam( orderObj, member );
		order.setShopId( CommonUtil.toInteger( orderObj.get( "shopId" ) ) );
		order.setMemCardType( memType );
		orderPayWay = order.getOrderPayWay();
		orderTypeId = order.getOrderType();
		shop_id = order.getShopId();
		if ( CommonUtil.isNotEmpty( orderObj.get( "wxCoupon" ) ) ) {//已使用微信优惠券
		    wxduofenCouponMap.put( order.getShopId() + "", JSONObject.parseObject( orderObj.get( "wxCoupon" ).toString() ) );
		}
		//获取多粉优惠券的 满减优惠或折扣
		if ( CommonUtil.isNotEmpty( orderObj.get( "duofenCoupon" ) ) ) {//已使用多粉优惠券
		    duofenCouponMap.put( order.getShopId() + "", JSONObject.parseObject( orderObj.get( "duofenCoupon" ).toString() ) );
		}

		Map< String,Object > result = validateOrder( order, member );
		code = CommonUtil.toInteger( result.get( "code" ) );
		if ( code == -1 ) {
		    if ( CommonUtil.isNotEmpty( result.get( "errorMsg" ) ) ) {
			msg = result.get( "errorMsg" ).toString();
		    }
		    break;
		}

		double orderMoneys = 0;
		List< MallOrderDetail > detailList = new ArrayList< MallOrderDetail >();
		if ( CommonUtil.isNotEmpty( orderObj.get( "orderDetail" ) ) ) {
		    JSONArray detailArr = JSONArray.parseArray( orderObj.get( "orderDetail" ).toString() );
		    if ( detailArr != null && detailArr.size() > 0 ) {
			for ( Object object2 : detailArr ) {
			    if ( code == -1 ) {
				break;
			    }
			    JSONObject detailObj = JSONObject.parseObject( object2.toString() );

			    int productId = CommonUtil.toInteger( detailObj.get( "productId" ) );
			    MallProduct product = null;

			    if ( CommonUtil.isEmpty( proMap ) ) {
				product = productService.selectByPrimaryKey( productId );
				proMap.put( product.getId().toString(), JSONObject.parseObject( product.toString() ) );
			    } else {
				if ( !proMap.containsKey( productId ) ) {
				    product = productService.selectByPrimaryKey( productId );
				    proMap.put( product.getId().toString(), JSONObject.parseObject( JSON.toJSONString( product ) ) );
				} else {
				    product = JSONObject.parseObject( proMap.get( productId ).toString(), MallProduct.class );
				}
			    }

			    MallOrderDetail detail = getOrderDetailByParam( detailObj, product );

			    if ( CommonUtil.isNotEmpty( detail.getDetPrivivilege() ) ) {
				totalPrimary = CommonUtil.toDouble( detail.getDetPrivivilege() ) * detail.getDetProNum();
			    }
			    Map< String,Object > dresultMap = validateOrderDetail( order, detail );
			    code = CommonUtil.toInteger( dresultMap.get( "code" ) );
			    if ( code == -1 ) {
				if ( CommonUtil.isNotEmpty( dresultMap.get( "errorMsg" ) ) ) {
				    msg = dresultMap.get( "errorMsg" ).toString();
				}
				break;
			    }
			    detailList.add( detail );

			}
		    }
		}
		if ( orderMoneys > 0 && orderMoneys != order.getOrderMoney().doubleValue() - order.getOrderFreightMoney().doubleValue() ) {
		    double orderMoney = order.getOrderFreightMoney().doubleValue() + orderMoneys;
		    order.setOrderMoney( CommonUtil.toBigDecimal( orderMoney ) );
		}
		if ( detailList.size() == 0 && code == 1 ) {
		    code = -1;
		    msg = "参数有误";
		    break;
		}
		logger.info( "订单金额：" + order.getOrderMoney() );

		order.setMallOrderDetail( detailList );
		orderList.add( order );

	    }
	}
	Map< String,Object > youhuiMaps = new HashMap< String,Object >();
	youhuiMaps.put( "fenbi_money", params.get( "fenbi_money" ) );
	youhuiMaps.put( "fenbi", params.get( "fenbi" ) );
	youhuiMaps.put( "integral_money", params.get( "integral_money" ) );
	youhuiMaps.put( "integral", params.get( "integral" ) );
	params.put( "shop_id", shop_id );
	List< MallOrder > newOrderList = curYouhuiOrder( params, orderList, proMap, duofenCouponMap, wxduofenCouponMap, youhuiMaps, member );

	if ( CommonUtil.isNotEmpty( newOrderList ) ) {
	    orderList = new ArrayList< MallOrder >();
	    orderList.addAll( newOrderList );
	}
	double orderTotalMoney = 0;
	double orderFreightMoney = 0;
	for ( MallOrder order : orderList ) {
	    orderTotalMoney += order.getOrderMoney().doubleValue();
	    orderFreightMoney += order.getOrderFreightMoney().doubleValue();
	}
	if ( orderTotalMoney > 0 ) {
	    orderTotalMoney = CommonUtil.toDouble( df.format( orderTotalMoney ) );
	}
	if ( orderFreightMoney > 0 ) {
	    orderFreightMoney = CommonUtil.toDouble( df.format( orderFreightMoney ) );
	}
	if ( orderPayWay == 3 && code > 0 ) {//储值卡支付，判断储值卡余额是否足够
	    //	    boolean flag =  memberService.isAdequateMoney(member.getId(), orderTotalMoney);
	    //	    if ( !flag ) {
	    //		code = -1;
	    //		msg = "您的储值卡余额不足";
	    //	    }
	    Map< String,Object > moneyMap = memberService.isAdequateMoney( member.getId(), orderTotalMoney );
	    if ( !moneyMap.get( "code" ).equals( "1" ) ) {//失败
		code = -1;
		msg = "您的储值卡余额不足";
	    }

	}
	logger.info( "订单总金额：" + orderTotalMoney );
	String orderNo = "";
	MallOrderDetail orderDetail = null;
	int orderPId = 0;
	//保存订单信息
	if ( orderList != null && orderList.size() > 0 && code > 0 ) {
	    for ( int i = 0; i < orderList.size(); i++ ) {
		MallOrder order = orderList.get( i );
		if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
		    order.setSellerUserId( wxPublicUsers.getId() );
		}
		order.setOrderOldMoney( new BigDecimal( 0 ) );
		if ( totalPrimary > 0 ) {
		    order.setOrderOldMoney( CommonUtil.toBigDecimal( totalPrimary ) );
		}
		BigDecimal money = order.getOrderMoney();
		BigDecimal freightMoney = order.getOrderFreightMoney();
		if ( orderList.size() > 1 && i == 0 ) {
		    MallOrder newOrder = new MallOrder();
		    newOrder = order;
		    newOrder.setOrderNo( "SC" + System.currentTimeMillis() );
		    newOrder.setOrderMoney( CommonUtil.toBigDecimal( orderTotalMoney ) );
		    newOrder.setOrderFreightMoney( CommonUtil.toBigDecimal( orderFreightMoney ) );
		    newOrder.setCreateTime( new Date() );
		    int count = orderDAO.insert( newOrder );
		    if ( count > 0 ) {
			orderNo = newOrder.getOrderNo();
			orderPId = newOrder.getId();
		    }

		}
		if ( orderPId > 0 ) {
		    order.setOrderPid( orderPId );
		}
		order.setOrderMoney( money );
		order.setOrderFreightMoney( freightMoney );
		order.setId( null );
		order.setOrderNo( "SC" + System.currentTimeMillis() );
		order.setCreateTime( new Date() );
		int count = orderDAO.insert( order );
		if ( count < 0 ) {
		    code = -1;
		    break;
		} else if ( count > 0 && orderList.size() == 1 && i == 0 ) {
		    orderNo = order.getOrderNo();
		}
		if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			orderDetail = detail;
			detail.setOrderId( order.getId() );
			detail.setCreateTime( new Date() );
			orderDetailDAO.insert( detail );
			if ( count < 0 ) {
			    code = -1;
			    break;
			}
		    }
		}
	    }
	}
	if ( code == -1 && msg.equals( "" ) ) {
	    msg = "提交订单失败，请稍后重试";
	}
	if ( code > 0 ) {//提交订单成功，并且是货到付款，
	    //货到付款或支付金额为0的订单，直接修改订单状态为已支付，且修改商品库存和销量
	    if ( orderPayWay != 10 ) {
		params = new HashMap<>();
		params.put( "status", 2 );
		params.put( "out_trade_no", orderNo );
		orderService.paySuccessModified( params, member );//修改库存和订单状态
	    }
	    //删除购物车的商品
	    if ( CommonUtil.isNotEmpty( params.get( "cartIds" ) ) ) {
		JSONArray cartArrs = JSONArray.parseArray( params.get( "cartIds" ).toString() );
		if ( cartArrs != null && cartArrs.size() > 0 ) {
		    for ( Object obj : cartArrs ) {
			if ( CommonUtil.isNotEmpty( obj ) ) {
			    shopCartDAO.deleteById( CommonUtil.toInteger( obj ) );
			}
		    }
		}
	    }
	    resultMap.put( "orderNo", orderNo );
	}
	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );
	return resultMap;
    }

    @Override
    public Map< String,Object > validateOrder( MallOrder order, Member member ) {
	Map< String,Object > resultMap = new HashMap<>();
	String msg = "";
	int code = 1;
	if ( order.getOrderPayWay() == 4 ) {//积分支付
	    Integer mIntergral = member.getIntegral();
	    if ( mIntergral < order.getOrderMoney().intValue() || mIntergral < 0 ) {
		code = -1;
		msg = "您的积分不够，不能用积分来兑换这件商品";
	    }
	}
	if ( order.getOrderPayWay() == 8 ) {//粉币支付
	    double fenbi = member.getFansCurrency();
	    if ( fenbi < order.getOrderMoney().intValue() || fenbi < 0 ) {
		code = -1;
		msg = "您的粉币不够，不能用粉币来兑换这件商品";
	    }
	}
	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );
	return resultMap;
    }

    @Override
    public Map< String,Object > validateOrderDetail( MallOrder order, MallOrderDetail detail ) {
	Map< String,Object > resultMap = new HashMap<>();
	String msg = "";
	int code = 1;
	//判断商品的库存
	if ( CommonUtil.isNotEmpty( order.getOrderType() ) && order.getOrderType() == 7 && CommonUtil.isNotEmpty( detail.getProSpecStr() ) ) {//判断批发商品的库存
	    JSONObject map = JSONObject.parseObject( detail.getProSpecStr() );
	    for ( Object key : map.keySet() ) {
		Object proSpecificas = key;
		JSONObject p = JSONObject.parseObject( map.get( key ).toString() );
		int proNum = CommonUtil.toInteger( p.get( "num" ) );
		Map< String,Object > result = productService.calculateInventory( detail.getProductId(), proSpecificas, proNum, order.getBuyerUserId() );
		if ( result.get( "result" ).toString().equals( "false" ) ) {
		    msg = result.get( "msg" ).toString();
		    code = -1;
		}
	    }
	} else {
	    Map< String,Object > result = productService
			    .calculateInventory( detail.getProductId(), detail.getProductSpecificas(), detail.getDetProNum(), order.getBuyerUserId() );
	    if ( result.get( "result" ).toString().equals( "false" ) ) {
		msg = result.get( "msg" ).toString();
		code = -1;
	    }
	}
	if ( CommonUtil.isNotEmpty( detail.getProTypeId() ) && code > 0 ) {
	    //卡全包购买判断是否已经过期
	    if ( detail.getProTypeId().toString().equals( "3" ) && CommonUtil.isNotEmpty( detail.getCardReceiveId() ) ) {
		Map< String,Object > cardMap = pageService.getCardReceive( detail.getCardReceiveId() );
		if ( CommonUtil.isNotEmpty( cardMap ) ) {
		    if ( CommonUtil.isNotEmpty( cardMap.get( "recevieMap" ) ) ) {
			JSONObject cardObj = JSONObject.parseObject( cardMap.get( "recevieMap" ).toString() );
			if ( CommonUtil.isNotEmpty( cardObj.get( "guoqi" ) ) && cardObj.get( "guoqi" ).toString().equals( "1" ) ) {
			    msg = "卡券包已过期不能购买";
			}
		    }
		}
	    }
	}

	//拍卖商品
	if ( CommonUtil.isNotEmpty( order.getOrderType() ) && code > 0 ) {
	    if ( "4".equals( order.getOrderType().toString() ) ) {
		resultMap = mallAuctionService.isMaxNum( order.getGroupBuyId(), order.getBuyerUserId().toString() );
	    } else if ( "6".equals( order.getOrderType().toString() ) ) {//商品预售
		resultMap = mallPresaleService.isMaxNum( order.getGroupBuyId(), order.getBuyerUserId().toString(), detail.getProductSpecificas(), detail.getDetProNum() );
	    } else if ( "3".equals( order.getOrderType().toString() ) ) {
		//判断库存
		resultMap = mallSeckillService.isInvNum( order.getGroupBuyId(), detail.getProductSpecificas(), detail.getDetProNum() );
	    }
	    if ( CommonUtil.isNotEmpty( resultMap ) && resultMap.size() > 0 ) {
		if ( !resultMap.get( "result" ).toString().equals( "true" ) ) {
		    return resultMap;
		}
	    }
	}

	if ( CommonUtil.isNotEmpty( order.getFlowPhone() ) && code > 0 ) {//流量充值，判断手机号码
	    MallProduct product = productService.selectByPrimaryKey( detail.getProductId() );

	    ReqGetMobileInfo reqGetMobileInfo = new ReqGetMobileInfo();
	    reqGetMobileInfo.setPhone( order.getFlowPhone() );
	    Map map = fenBiFlowService.getMobileInfo( reqGetMobileInfo );
	    if ( map.get( "code" ).toString().equals( "-1" ) ) {
		resultMap.put( "code", ResponseEnums.ERROR.getCode() );
		resultMap.put( "errorMsg", map.get( "errorMsg" ) );
		return resultMap;
	    } else if ( map.get( "code" ).toString().equals( "1" ) ) {
		GetMobileInfo mobileInfo = JSONObject.toJavaObject( JSONObject.parseObject( map.get( "data" ).toString() ), GetMobileInfo.class );

		BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );

		if ( mobileInfo.getSupplier().equals( "中国联通" ) && flow.getType() == 10 ) {
		    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
		    resultMap.put( "errorMsg", "充值失败,联通号码至少30MB" );
		    return resultMap;
		}
		if ( flow.getCount() == 0 ) {
		    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
		    resultMap.put( "errorMsg", "流量数量不足，不能充值" );
		    return resultMap;
		}
	    }
	}
	if ( ( CommonUtil.isEmpty( detail.getProTypeId() ) || detail.getProTypeId() == 0 ) && CommonUtil.isEmpty( order.getReceiveId() ) && code > 0 ) {
	    msg = "请选择收货地址";
	    code = -1;
	}
	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );
	return resultMap;
    }

    @Override
    public Map< String,Object > newCalculationPreferential( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	if ( CommonUtil.isEmpty( params.get( "order" ) ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整" );
	    return resultMap;
	}

	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Integer shopId = null;
	double orderTotalMoney = 0;//商品总价
	if ( CommonUtil.isNotEmpty( params.get( "totalAllMoney" ) ) ) {
	    orderTotalMoney = CommonUtil.toDouble( params.get( "totalAllMoney" ) );
	}
	double totalDiscountMoney = 0;//商品会员折扣总价
	if ( CommonUtil.isNotEmpty( params.get( "totalDiscountMoney" ) ) ) {
	    totalDiscountMoney = CommonUtil.toDouble( params.get( "totalDiscountMoney" ) );
	}

	Map< Integer,MallShopEntity > mallShops = new HashMap<>();

	JSONArray orderArray = JSONArray.parseArray( params.get( "order" ).toString() );
	if ( orderArray != null && orderArray.size() > 0 ) {
	    for ( int i = 0; i < orderArray.size(); i++ ) {
		Object object = orderArray.get( i );
		JSONObject orderObj = JSONObject.parseObject( object.toString() );
		MallShopEntity mallShopEntity = new MallShopEntity();
		Map< Object,MallEntity > productMap = new HashMap<>();

		if ( CommonUtil.isNotEmpty( orderObj.get( "orderDetail" ) ) ) {
		    JSONArray detailArr = JSONArray.parseArray( orderObj.get( "orderDetail" ).toString() );
		    if ( detailArr != null && detailArr.size() > 0 ) {
			for ( int j = 0; j < detailArr.size(); j++ ) {
			    Object object2 = detailArr.get( j );
			    MallOrderDetail orderDetail = JSONObject.parseObject( object2.toString(), MallOrderDetail.class );
			    MallProduct product = productService.selectByPrimaryKey( orderDetail.getProductId() );
			    int index = CommonUtil.toInteger( ( i + 1 ) + "" + j );
			    MallEntity entity = new MallEntity();
			    entity.setMallId( index );
			    entity.setNumber( orderDetail.getDetProNum() );
			    entity.setTotalMoneyOne( CommonUtil.toDouble( orderDetail.getDetProPrice() ) );
			    entity.setTotalMoneyAll( orderDetail.getTotalPrice() );
			    entity.setUserCard( product.getIsMemberDiscount() );
			    entity.setUseCoupon( product.getIsCoupons() );
			    entity.setUseFenbi( product.getIsFenbiDeduction() );
			    entity.setUserJifen( product.getIsIntegralDeduction() );
			    productMap.put( index, entity );
			}
		    }
		}

		MallStore store = storeDAO.selectById( CommonUtil.toInteger( orderObj.get( "shopId" ) ) );
		shopId = store.getWxShopId();
		mallShopEntity.setShopId( store.getWxShopId() );
		mallShopEntity.setMalls( productMap );

		if ( CommonUtil.isNotEmpty( orderObj.get( "wxCoupon" ) ) ) {//已使用微信优惠券
		    JSONObject wxCouponObj = JSONObject.parseObject( orderObj.get( "wxCoupon" ).toString() );
		    mallShopEntity.setUseCoupon( 1 );
		    mallShopEntity.setCouponType( 0 );
		    mallShopEntity.setCanUseConpon( 1 );
		    mallShopEntity.setCoupondId( CommonUtil.toInteger( wxCouponObj.get( "id" ) ) );
		    mallShopEntity.setCodes( wxCouponObj.get( "card_id" ).toString() );

		}

		if ( CommonUtil.isNotEmpty( orderObj.get( "duofenCoupon" ) ) ) {//已使用多粉优惠券
		    JSONObject duofenCouponObj = JSONObject.parseObject( orderObj.get( "duofenCoupon" ).toString() );
		    mallShopEntity.setUseCoupon( 1 );
		    mallShopEntity.setCouponType( 1 );
		    mallShopEntity.setCoupondId( CommonUtil.toInteger( duofenCouponObj.get( "gId" ) ) );
		    mallShopEntity.setCodes( duofenCouponObj.get( "code" ).toString() );
		    mallShopEntity.setCanUseConpon( 1 );
		}

		mallShops.put( store.getWxShopId(), mallShopEntity );

	    }
	}

	MallAllEntity mallAllEntity = new MallAllEntity();
	mallAllEntity.setMemberId( memberId );
	mallAllEntity.setTotalMoney( orderTotalMoney );

	int isUseFenbi = 0;
	int isUseJifen = 0;
	if ( CommonUtil.isNotEmpty( params.get( "isUseFenbi" ) ) ) {
	    isUseFenbi = CommonUtil.toInteger( params.get( "isUseFenbi" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "isUseJifen" ) ) ) {
	    isUseJifen = CommonUtil.toInteger( params.get( "isUseJifen" ) );
	}

	if ( isUseFenbi == 1 ) {
	    mallAllEntity.setUseFenbi( isUseFenbi );

	}
	if ( isUseJifen == 1 ) {
	    mallAllEntity.setUserJifen( isUseJifen );
	}

	mallAllEntity.setMallShops( mallShops );
	MallAllEntity resultAllEntity = memberPayService.memberCountMoneyByShop( mallAllEntity );
	if ( CommonUtil.isNotEmpty( resultAllEntity ) ) {

	    Map memberCard = memberService.findMemberCardByMemberId( memberId, shopId );
	    if ( CommonUtil.isNotEmpty( memberCard.get( "jifenMoeny" ) ) ) {
		resultMap.put( "integral_money", memberCard.get( "jifenMoeny" ) );//积分可抵扣的金额
	    } else {
		resultMap.put( "integral_money", 0 );//积分可抵扣的金额
	    }
	    if ( CommonUtil.isNotEmpty( memberCard.get( "integral" ) ) ) {
		resultMap.put( "integral", memberCard.get( "integral" ) );//积分数量
	    } else {
		resultMap.put( "integral", 0 );//积分数量
	    }
	    if ( CommonUtil.isNotEmpty( memberCard.get( "fenbiMoeny" ) ) ) {
		resultMap.put( "fenbi_money", memberCard.get( "fenbiMoeny" ) );//粉币可抵扣的金额
	    } else {
		resultMap.put( "fenbi_money", 0 );//粉币可抵扣的金额
	    }
	    if ( CommonUtil.isNotEmpty( memberCard.get( "fans_currency" ) ) ) {
		resultMap.put( "fenbi", memberCard.get( "fans_currency" ) );//粉币数量
	    } else {
		resultMap.put( "fenbi", 0 );//粉币数量
	    }
	    Integer canUsefenbi = resultAllEntity.getCanUsefenbi();
	    Integer canUseJifen = resultAllEntity.getCanUseJifen();
	    double discountjifenMoney = 0;
	    double discountfenbiMoney = 0;
	    if ( canUsefenbi == 1 ) {//能用
		discountfenbiMoney = resultAllEntity.getDiscountfenbiMoney();
	    }
	    if ( canUseJifen == 1 ) {//能用
		discountjifenMoney = resultAllEntity.getDiscountjifenMoney();
	    }
	    resultMap.put( "code", 1 );
	    resultMap.put( "errorMsg", "" );

	    resultMap.put( "canUsefenbi", canUsefenbi );
	    resultMap.put( "canUseJifen", canUseJifen );
	    resultMap.put( "unionDiscountMoney", resultAllEntity.getLeagueMoney() );
	    resultMap.put( "yhqDiscountMoney", resultAllEntity.getDiscountConponMoney() );
	    resultMap.put( "fenbiDiscountMoney", discountfenbiMoney );
	    resultMap.put( "jifenDiscountMoney", discountjifenMoney );
	    DecimalFormat df = new DecimalFormat( "######0.00" );
	    double youhuiMoney = resultAllEntity.getLeagueMoney() + resultAllEntity.getDiscountConponMoney() + discountfenbiMoney + discountjifenMoney + totalDiscountMoney;
	    resultMap.put( "orderTotalMoney", df.format( orderTotalMoney - youhuiMoney ) );

	} else {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", ResponseEnums.INTER_ERROR.getDesc() );
	}

	return resultMap;
    }
}
