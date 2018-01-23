package com.gt.mall.service.web.applet.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.entityBo.MallEntity;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.bean.member.Coupons;
import com.gt.mall.bean.member.JifenAndFenbBean;
import com.gt.mall.bean.member.JifenAndFenbiRule;
import com.gt.mall.dao.applet.MallAppletImageDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.applet.AppletSubmitOrderDTO;
import com.gt.mall.param.applet.AppletSubmitOrderProductDTO;
import com.gt.mall.param.applet.AppletSubmitOrderShopDTO;
import com.gt.mall.param.phone.freight.PhoneFreightProductDTO;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.applet.MallNewOrderAppletService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.common.MallAppletCalculateService;
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
import com.gt.mall.utils.ToOrderUtil;
import com.gt.union.api.entity.param.UnionCardDiscountParam;
import com.gt.union.api.entity.result.UnionDiscountResult;
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
    private MallOrderDAO               orderDAO;
    @Autowired
    private MallShopCartDAO            shopCartDAO;
    @Autowired
    private MallImageAssociativeDAO    imageAssociativeDAO;
    @Autowired
    private MallStoreDAO               storeDAO;
    @Autowired
    private MallOrderDetailDAO         orderDetailDAO;
    @Autowired
    private MallProductService         productService;
    @Autowired
    private MallFreightService         freightService;
    @Autowired
    private MallPaySetService          paySetService;
    @Autowired
    private MallOrderService           orderService;
    @Autowired
    private MallPageService            pageService;
    @Autowired
    private MemberService              memberService;
    @Autowired
    private DictService                dictService;
    @Autowired
    private WxPublicUserService        wxPublicUserService;
    @Autowired
    private MallStoreService           mallStoreService;
    @Autowired
    private FenBiFlowService           fenBiFlowService;
    @Autowired
    private WxShopService              wxShopService;
    @Autowired
    private MemberPayService           memberPayService;
    @Autowired
    private MallAuctionService         mallAuctionService;
    @Autowired
    private MallPresaleService         mallPresaleService;
    @Autowired
    private MallSeckillService         mallSeckillService;
    @Autowired
    private MemberAddressService       memberAddressService;
    @Autowired
    private UnionCardService           unionCardService;
    @Autowired
    private MallAppletCalculateService mallAppletCalculateService;

    @Override
    public Map< String,Object > toSubmitOrder( Map< String,Object > params ) {

	Map< String,Object > resultMap = new HashMap<>();
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Member member = memberService.findMemberById( memberId, null );
	int from = 1;
	if ( CommonUtil.isNotEmpty( params.get( "from" ) ) ) {
	    from = CommonUtil.toInteger( params.get( "from" ) );
	}
	DecimalFormat df = new DecimalFormat( "######0.00" );
	Double memberLongitude = 0d;//会员经度
	Double memberLangitude = 0d;//会员纬度
	Integer provincesId = null;
	//查询用户默认的地址
	List< Integer > memberList = memberService.findMemberListByIds( memberId );//查询会员信息
	Map addressMap = memberAddressService.addressDefault( CommonUtil.getMememberIds( memberList, memberId ) );
	if ( addressMap != null && addressMap.size() > 0 ) {
	    resultMap.put( "addressMap", getAddressParams( addressMap ) );
	    params.put( "mem_province", addressMap.get( "memProvince" ) );
	    if ( CommonUtil.isNotEmpty( addressMap.get( "memLongitude" ) ) && CommonUtil.isNotEmpty( addressMap.get( "memLatitude" ) ) ) {
		memberLongitude = CommonUtil.toDouble( addressMap.get( "memLongitude" ) );
		memberLangitude = CommonUtil.toDouble( addressMap.get( "memLatitude" ) );
	    }
	    provincesId = CommonUtil.toInteger( addressMap.get( "memProvince" ) );
	}

	double totalProMoney = 0;//	商品总价
	double totalFreightMoney = 0;//运费总价
	int isFlow = 0;//是否是流量充值商品  1是  0 不是

	int totalNum = 0;//商品小计
	int proTypeId = 0;//商品类型

	StringBuilder wxShopIds = new StringBuilder( "," );//微信门店id
	boolean isCanJifenFenbi = false;//是否能使用积分和粉币
	if ( from == 1 ) {//购物车结算
	    List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	    if ( CommonUtil.isNotEmpty( params.get( "cartIds" ) ) ) {
		JSONArray cartArrs = JSONArray.parseArray( params.get( "cartIds" ).toString() );
		params.put( "cartIds", cartArrs );
	    }
	    double totalFreightPrice = 0;
	    List< Integer > freightIds = new ArrayList<>();
	    List< Map< String,Object > > shopList = shopCartDAO.selectCheckShopByParam( params );
	    if ( shopList != null && shopList.size() > 0 ) {
		List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( member.getBusid() );//查询商家的所有门店集合
		for ( Map< String,Object > shopMap : shopList ) {
		    int shopId = CommonUtil.toInteger( shopMap.get( "shop_id" ) );
		    params.put( "shopId", shopId );
		    double totalPrice = 0;
		    totalNum = 0;
		    List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();
		    List< Map< String,Object > > productList = shopCartDAO.selectCheckCartByParams( params );
		    List< PhoneFreightProductDTO > freightDTOList = new ArrayList<>();
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
			    double weight = CommonUtil.toDouble( map.get( "pro_weight" ) );
			    productMap.put( "total_price", CommonUtil.multiply( CommonUtil.toDouble( productMap.get( "product_price" ) ),
					    CommonUtil.toInteger( productMap.get( "product_num" ) ) ) );
			    totalNum += CommonUtil.toInteger( map.get( "product_num" ) );
			    proTypeId = CommonUtil.toInteger( map.get( "pro_type_id" ) );
			    double discountPrice = CommonUtil.toDouble( productMap.get( "product_price" ) ) - CommonUtil.toDouble( map.get( "price" ) );
			    discountPrice = CommonUtil.toDouble( df.format( discountPrice ) );
			    productMap.put( "discount_price", discountPrice );//折扣金额
			    if ( map.get( "is_integral_deduction" ).toString().equals( "1" ) || map.get( "is_fenbi_deduction" ).toString().equals( "1" ) ) {
				isCanJifenFenbi = true;
			    }
			    //获取规格名称
			    if ( CommonUtil.isNotEmpty( map.get( "product_specificas" ) ) ) {
				Map< String,Object > invMap = productService
						.getProInvIdBySpecId( CommonUtil.toString( map.get( "product_specificas" ) ), CommonUtil.toInteger( map.get( "product_id" ) ) );
				if ( CommonUtil.isNotEmpty( invMap ) && CommonUtil.isNotEmpty( invMap.get( "specifica_values" ) ) ) {
				    String speciname = invMap.get( "specifica_values" ).toString().replaceAll( ",", "/" );
				    productMap.put( "product_specificaname", speciname );
				    if ( CommonUtil.isNotEmpty( invMap.get( "weight" ) ) ) {
					if ( CommonUtil.toDouble( invMap.get( "weight" ) ) > 0 ) {
					    weight = CommonUtil.toDouble( invMap.get( "weight" ) );
					}
				    }
				}
			    }

			    proList.add( productMap );
			    double proTotalPrice = CommonUtil.multiply( CommonUtil.toDouble( productMap.get( "product_price" ) ),
					    CommonUtil.toInteger( productMap.get( "product_num" ) ) );
			    totalPrice += proTotalPrice;
			    totalProMoney += proTotalPrice;

			    double freightPrice = 0;
			    if ( CommonUtil.isNotEmpty( map.get( "pro_freight_price" ) ) ) {
				freightPrice = CommonUtil.toDouble( map.get( "pro_freight_price" ) );
			    }

			    if ( CommonUtil.isNotEmpty( map.get( "pro_freight_temp_id" ) ) ) {
				int freightId = CommonUtil.toInteger( map.get( "pro_freight_temp_id" ) );
				if ( freightId > 0 ) {
				    MallFreight freight = freightService.selectById( freightId );
				    PhoneFreightProductDTO freightProductDTO = new PhoneFreightProductDTO();
				    freightProductDTO.setProductId( CommonUtil.toInteger( map.get( "product_id" ) ) );
				    freightProductDTO.setFreightId( freightId );
				    freightProductDTO.setTotalProductNum( CommonUtil.toInteger( map.get( "product_num" ) ) );
				    freightProductDTO.setTotalProductPrice( proTotalPrice );
				    freightProductDTO.setTotalProductWeight( weight );
				    freightProductDTO.setMallFreight( freight );
				    freightDTOList.add( freightProductDTO );
				} else if ( freightPrice > 0 ) {
				    totalFreightMoney += freightPrice;
				}
			    }

			}
		    }
		    params.put( "product_num", totalNum );
		    boolean isError = false;
		    if ( shopId > 0 ) {
			boolean isJuli = orderService.isJuliByFreight( shopId + "" );
			if ( isJuli && ( CommonUtil.isEmpty( addressMap.get( "memLatitude" ) ) || CommonUtil.isEmpty( addressMap.get( "memLongitude" ) ) ) ) {
			    resultMap.put( "isJuliFreight", 1 );
			    isError = true;
			}
		    }
		    //		    if ( !isError ) {
		    //			double freightPrice = freightService.getFreightByProvinces( params, addressMap, shopId, totalPrice, pro_weight );
		    //			totalFreightMoney += freightPrice;
		    //			shopMap.put( "freightPrice", freightPrice );
		    //		    }
		    shopMap.put( "totalProPrice", df.format( totalPrice ) );
		    shopMap.put( "proList", proList );
		    shopMap.put( "totalNum", totalNum );
		    int wxShopId = 0;
		    if ( CommonUtil.isNotEmpty( shopMap.get( "wx_shop_id" ) ) ) {
			wxShopId = CommonUtil.toInteger( shopMap.get( "wx_shop_id" ) );
		    }
		    double shopLongitude = 0;//店铺经度
		    double shopLangitude = 0;//店铺纬度
		    String shopPicture = "";//店铺图片
		    for ( WsWxShopInfoExtend wxShops : shopInfoList ) {
			if ( wxShops.getId() == wxShopId ) {
			    if ( CommonUtil.isNotEmpty( wxShops.getBusinessName() ) ) {
				shopMap.put( "sto_name", wxShops.getBusinessName() );
			    }
			    shopPicture = wxShops.getImageUrl();
			    if ( CommonUtil.isNotEmpty( wxShops.getLongitude() ) ) {
				shopLongitude = CommonUtil.toDouble( wxShops.getLongitude() );
			    }
			    if ( CommonUtil.isNotEmpty( wxShops.getLatitude() ) ) {
				shopLangitude = CommonUtil.toDouble( wxShops.getLatitude() );
			    }
			    break;
			}
		    }
		    if ( freightDTOList != null && freightDTOList.size() > 0 && !isError ) {
			Double juli = 0d;
			if ( memberLongitude > 0 && memberLangitude > 0 && shopLongitude > 0 && shopLangitude > 0 ) {
			    juli = CommonUtil.getDistance( memberLongitude, memberLangitude, shopLongitude, shopLangitude ) / 100;
			}
			double freightPrice = freightService.getFreightMoneyByProductList( freightDTOList, juli, provincesId );
			totalFreightMoney += freightPrice;
		    }
		    shopMap.put( "freightPrice", totalFreightMoney );
		    if ( !wxShopIds.toString().contains( "," + wxShopId + "," ) ) {
			wxShopIds.append( wxShopId ).append( "," );
		    }
		    if ( CommonUtil.isNotEmpty( shopPicture ) ) {
			shopMap.put( "sto_image", shopPicture );
		    } else {
			shopMap.put( "sto_image", shopMap.get( "stoPicture" ) );
		    }
		    shopMap.remove( "stoPicture" );

		    cartList.add( shopMap );
		}
	    }
	    resultMap.put( "shopList", cartList );

	} else if ( from == 2 ) {//立即购买
	    int productId = CommonUtil.toInteger( params.get( "product_id" ) );
	    MallProduct product = productService.selectByPrimaryKey( productId );
	    String specificaIds = "";
	    String specificaNames = "";
	    String imageUrl = "";
	    double productWeight = 0;
	    if ( CommonUtil.isNotEmpty( product.getProWeight() ) ) {
		productWeight = CommonUtil.toDouble( product.getProWeight() );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "product_specificas" ) ) ) {
		specificaIds = CommonUtil.toString( params.get( "product_specificas" ) );
		Map< String,Object > specMap = productService.getSpecNameBySPecId( specificaIds, productId );
		if ( CommonUtil.isNotEmpty( specMap.get( "product_speciname" ) ) ) {
		    specificaNames = CommonUtil.toString( specMap.get( "product_speciname" ) );
		}
		if ( CommonUtil.isNotEmpty( specMap.get( "imageUrl" ) ) ) {
		    imageUrl = CommonUtil.toString( specMap.get( "imageUrl" ) );
		}
		Map< String,Object > invMap = productService.getProInvIdBySpecId( specificaIds, productId );
		if ( CommonUtil.isNotEmpty( invMap ) ) {
		    double invPrice = CommonUtil.toDouble( invMap.get( "inv_price" ) );//商品规格价
		    if ( CommonUtil.isNotEmpty( invMap.get( "weight" ) ) ) {
			double weight = CommonUtil.toDouble( invMap.get( "weight" ) );
			if ( weight > 0 ) {
			    productWeight = weight;
			}
		    }
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
		if ( !imageUrl.contains( PropertiesUtil.getResourceUrl() ) ) {
		    imageUrl = PropertiesUtil.getResourceUrl() + imageUrl;
		}
	    }
	    if ( product.getIsIntegralDeduction().toString().equals( "1" ) || product.getIsFenbiDeduction().toString().equals( "1" ) ) {
		isCanJifenFenbi = true;
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
	    productMap.put( "total_price",
			    CommonUtil.multiply( CommonUtil.toDouble( productMap.get( "product_price" ) ), CommonUtil.toInteger( productMap.get( "product_num" ) ) ) );
	    proTypeId = product.getProTypeId();

	    totalNum += CommonUtil.toInteger( params.get( "product_num" ) );

	    //判断商品是否是流量充值
	    if ( CommonUtil.isNotEmpty( product.getProTypeId() ) && CommonUtil.isNotEmpty( product.getFlowId() ) ) {
		if ( product.getProTypeId().toString().equals( "4" ) && product.getFlowId() > 0 ) {
		    isFlow = 1;
		}
	    }

	    proList.add( productMap );

	    double proTotalPrice = CommonUtil.toDouble( productMap.get( "primary_price" ) ) * CommonUtil.toInteger( params.get( "product_num" ) );
	    totalProMoney += proTotalPrice;

	    double totalPrice = proTotalPrice;
	    boolean isError = false;
	    double freightPrice = 0;
	    if ( CommonUtil.isNotEmpty( product.getShopId() ) ) {
		boolean isJuli = orderService.isJuliByFreight( product.getShopId().toString() );
		if ( isJuli && ( CommonUtil.isEmpty( addressMap ) || CommonUtil.isEmpty( addressMap.get( "memLatitude" ) ) || CommonUtil
				.isEmpty( addressMap.get( "memLongitude" ) ) ) ) {
		    resultMap.put( "isJuliFreight", 1 );
		    isError = true;
		}
	    }
	    Map< String,Object > storeMaps = mallStoreService.findShopByStoreId( product.getShopId() );
	    if ( !isError ) {
		//计算运费
		//		freightPrice = freightService.getFreightByProvinces( params, addressMap, product.getShopId(), totalPrice, CommonUtil.toDouble( product.getProWeight() ) );
		//		totalFreightMoney += freightPrice;
		Integer freightId = 0;
		if ( CommonUtil.isNotEmpty( product.getProFreightPrice() ) ) {
		    freightPrice = CommonUtil.toDouble( product.getProFreightPrice() );
		}
		if ( CommonUtil.isNotEmpty( product.getProFreightTempId() ) && product.getProFreightTempId() > 0 ) {
		    freightId = product.getProFreightTempId();
		}
		if ( freightId > 0 ) {
		    MallFreight mallFreight = freightService.selectById( freightId );
		    List< PhoneFreightProductDTO > freightDTOList = new ArrayList<>();
		    PhoneFreightProductDTO freightProductDTO = new PhoneFreightProductDTO();
		    freightProductDTO.setProductId( product.getId() );
		    freightProductDTO.setFreightId( freightId );
		    freightProductDTO.setTotalProductNum( CommonUtil.toInteger( params.get( "product_num" ) ) );
		    freightProductDTO.setTotalProductPrice( totalPrice );
		    freightProductDTO.setTotalProductWeight( productWeight );
		    freightProductDTO.setMallFreight( mallFreight );
		    freightDTOList.add( freightProductDTO );

		    Double juli = CommonUtil.getRaill( storeMaps, memberLangitude, memberLongitude );
		    freightPrice = freightService.getFreightMoneyByProductList( freightDTOList, juli, provincesId );
		    totalFreightMoney += freightPrice;
		}
	    }
	    Map< String,Object > shopMap = new HashMap<>();
	    shopMap.put( "shop_id", product.getShopId() );
	    shopMap.put( "totalNum", totalNum );
	    shopMap.put( "freightPrice", freightPrice );
	    shopMap.put( "proList", proList );
	    shopMap.put( "totalProPrice", df.format( totalPrice ) );

	    int wxShopId = 0;
	    if ( CommonUtil.isNotEmpty( storeMaps ) ) {
		wxShopId = CommonUtil.toInteger( storeMaps.get( "wxShopId" ) );
		shopMap.put( "sto_image", storeMaps.get( "stoPicture" ) );
		shopMap.put( "sto_name", storeMaps.get( "stoName" ) );
		shopMap.put( "wx_shop_id", wxShopId );
		wxShopIds.append( wxShopId ).append( "," );
	    }

	    List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	    cartList.add( shopMap );

	    resultMap.put( "shopList", cartList );
	} else if ( from == 3 && CommonUtil.isNotEmpty( params.get( "orderId" ) ) && CommonUtil.toInteger( params.get( "orderId" ) ) > 0 ) {//去支付
	    MallOrder mallOrder = orderDAO.selectById( CommonUtil.toInteger( params.get( "orderId" ) ) );
	    if ( CommonUtil.isNotEmpty( mallOrder ) ) {
		List< MallOrderDetail > detailList = orderDetailDAO.selectByOrderId( mallOrder.getId() );
		List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();
		double productWeight = 0;
		double totalFreightPrice = 0;
		List< PhoneFreightProductDTO > freightDTOList = new ArrayList<>();//计算运费参数
		if ( detailList != null && detailList.size() > 0 ) {
		    for ( MallOrderDetail detail : detailList ) {
			MallProduct product = productService.selectById( detail.getProductId() );
			Map< String,Object > productMap = new HashMap< String,Object >();
			productMap.put( "product_id", detail.getProductId() );
			productMap.put( "product_type_id", detail.getProTypeId() );
			productMap.put( "product_name", product.getProName() );
			productMap.put( "product_image", PropertiesUtil.getResourceUrl() + detail.getProductImageUrl() );
			productMap.put( "product_specificas", detail.getProductSpecificas() );
			productMap.put( "product_specificaname", detail.getProductSpeciname() );
			productMap.put( "product_num", detail.getDetProNum() );
			productMap.put( "product_price", detail.getDetPrivivilege() );
			productMap.put( "primary_price", detail.getDetPrivivilege() );
			productMap.put( "is_member_discount", product.getIsMemberDiscount() );
			productMap.put( "is_coupons", product.getIsCoupons() );
			productMap.put( "is_integral_deduction", product.getIsIntegralDeduction() );
			productMap.put( "is_fenbi_deduction", product.getIsFenbiDeduction() );
			productMap.put( "total_price",
					CommonUtil.multiply( CommonUtil.toDouble( detail.getDetPrivivilege() ), detail.getDetProNum() ) );
			productMap.put( "detailId", detail.getId() );
			proTypeId = product.getProTypeId();

			totalNum += CommonUtil.toInteger( detail.getDetProNum() );

			//判断商品是否是流量充值
			if ( CommonUtil.isNotEmpty( mallOrder.getFlowPhone() ) ) {
			    isFlow = 1;
			}

			proList.add( productMap );

			double proTotalPrice = CommonUtil.toDouble( productMap.get( "primary_price" ) ) * CommonUtil.toInteger( productMap.get( "product_num" ) );
			totalProMoney += proTotalPrice;
			if ( CommonUtil.isNotEmpty( product.getProWeight() ) ) {
			    productWeight = CommonUtil.add( productWeight, CommonUtil.toDouble( product.getProWeight() ) );
			}
			if ( CommonUtil.isNotEmpty( detail.getProductSpecificas() ) ) {
			    Map< String,Object > invMap = productService.getProInvIdBySpecId( detail.getProductSpecificas(), detail.getProductId() );
			    if ( CommonUtil.isNotEmpty( invMap ) ) {
				if ( CommonUtil.isNotEmpty( invMap.get( "weight" ) ) ) {
				    if ( CommonUtil.toDouble( invMap.get( "weight" ) ) > 0 ) {
					productWeight = CommonUtil.toDouble( invMap.get( "weight" ) );
				    }
				}
			    }
			}

			if ( product.getIsIntegralDeduction().toString().equals( "1" ) || product.getIsFenbiDeduction().toString().equals( "1" ) ) {
			    isCanJifenFenbi = true;
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
			if ( freightId > 0 ) {
			    MallFreight mallFreight = freightService.selectById( freightId );
			    PhoneFreightProductDTO freightProductDTO = new PhoneFreightProductDTO();
			    freightProductDTO.setProductId( product.getId() );
			    freightProductDTO.setFreightId( freightId );
			    freightProductDTO.setTotalProductNum( detail.getDetProNum() );
			    freightProductDTO.setTotalProductPrice( detail.getTotalPrice() );
			    freightProductDTO.setTotalProductWeight( productWeight );
			    freightProductDTO.setMallFreight( mallFreight );
			    freightDTOList.add( freightProductDTO );
			} else if ( freightPrice > 0 ) {
			    totalFreightPrice += freightPrice;
			}

		    }
		}

		//		double totalPrice = proTotalPrice;

		Map< String,Object > storeMaps = mallStoreService.findShopByStoreId( mallOrder.getShopId() );
		boolean isError = false;
		double freightPrice = 0;
		if ( CommonUtil.isNotEmpty( mallOrder.getShopId() ) ) {
		    boolean isJuli = orderService.isJuliByFreight( mallOrder.getShopId().toString() );
		    if ( isJuli && ( CommonUtil.isEmpty( addressMap.get( "memLatitude" ) ) || CommonUtil.isEmpty( addressMap.get( "memLongitude" ) ) ) ) {
			resultMap.put( "isJuliFreight", 1 );
			isError = true;
		    }
		}
		if ( !isError ) {
		    //计算运费
		    freightPrice = freightService.getFreightByProvinces( params, addressMap, mallOrder.getShopId(), totalProMoney, productWeight );
		    totalFreightMoney += freightPrice;

		    if ( freightDTOList != null && freightDTOList.size() > 0 ) {

			Double juli = CommonUtil.getRaill( storeMaps, memberLangitude, memberLongitude );
			freightPrice = freightService.getFreightMoneyByProductList( freightDTOList, juli, provincesId );
			totalFreightPrice += freightPrice;
			totalFreightMoney = totalFreightPrice;
		    }
		}

		Map< String,Object > shopMap = new HashMap<>();
		shopMap.put( "shop_id", mallOrder.getShopId() );
		shopMap.put( "totalNum", totalNum );
		shopMap.put( "freightPrice", freightPrice );
		shopMap.put( "proList", proList );
		shopMap.put( "totalProPrice", df.format( totalProMoney ) );
		shopMap.put( "orderId", mallOrder.getId() );

		int wxShopId = 0;
		if ( CommonUtil.isNotEmpty( storeMaps ) ) {
		    wxShopId = CommonUtil.toInteger( storeMaps.get( "wxShopId" ) );
		    shopMap.put( "sto_image", storeMaps.get( "stoPicture" ) );
		    shopMap.put( "sto_name", storeMaps.get( "stoName" ) );
		    shopMap.put( "wx_shop_id", wxShopId );
		    wxShopIds.append( wxShopId ).append( "," );
		}

		List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
		cartList.add( shopMap );

		resultMap.put( "shopList", cartList );
	    }
	}

	//查询是否能显示货到付款和找人代付的按钮
	Map< String,Object > huodaoMap = paySetService.isHuoDaoByUserId( member.getBusid() );

	resultMap.put( "isDaifu", huodaoMap.get( "isDaifu" ) );

	if ( proTypeId == 0 ) {
	    resultMap.put( "isHuoDao", huodaoMap.get( "isHuoDao" ) );
	} else {
	    resultMap.put( "isHuoDao", 0 );
	}

	totalProMoney = CommonUtil.toDouble( df.format( totalProMoney ) );
	totalFreightMoney = CommonUtil.toDouble( df.format( totalFreightMoney ) );
	resultMap.put( "totalProMoney", totalProMoney );//商品总价
	resultMap.put( "totalFreightMoney", totalFreightMoney );//运费
	resultMap.put( "totalMoney", df.format( totalProMoney + totalFreightMoney ) );//实付金额

	resultMap.put( "isFlow", isFlow );//是否是流量充值商品   1是  0 不是

	resultMap = getMemberByOrders( resultMap, proTypeId, member, wxShopIds.toString().substring( 1, wxShopIds.length() - 1 ), isCanJifenFenbi );

	return resultMap;

    }

    private Map< String,Object > getMemberByOrders( Map< String,Object > resultMap, Integer proTypeId, Member member, String wxShopIds,
		    boolean isCanJifenFenbi ) {
	UnionDiscountResult unionResult = null;//联盟折扣
	JifenAndFenbiRule jifenFenbiRule = null;//积分粉币抵扣规则
	double discount = 1;
	Map cardMap = null;//兑换对象
	if ( proTypeId == 0 ) {
	    if ( CommonUtil.isNotEmpty( member ) ) {
		if ( CommonUtil.isNotEmpty( member.getPhone() ) && proTypeId == 0 ) {
		    UnionCardDiscountParam unionParams = new UnionCardDiscountParam();
		    unionParams.setBusId( member.getBusid() );
		    //		    unionParams.setMemberId( member.getId() );
		    unionParams.setPhone( member.getPhone() );
		    unionResult = unionCardService.consumeUnionDiscount( unionParams );//获取联盟折扣
		    if ( CommonUtil.isNotEmpty( unionResult ) ) {
			resultMap.put( "unionStatus", unionResult.getCode() );
			if ( unionResult.getCode() == 1 ) {
			    resultMap.put( "unionDiscount", unionResult.getDiscount() );
			}
		    }
		}
		cardMap = memberService.findCardAndShopIdsByMembeId( member.getId(), wxShopIds );
	    }
	    if ( isCanJifenFenbi ) {
		jifenFenbiRule = memberService.jifenAndFenbiRule( member.getBusid() );//通过商家id查询积分和粉币规则
		resultMap.put( "jifenFenbiRule", jifenFenbiRule );
	    }
	    if ( CommonUtil.isNotEmpty( cardMap ) ) {
		if ( CommonUtil.isNotEmpty( cardMap.get( "ctId" ) ) && "2".equals( cardMap.get( "ctId" ).toString() ) ) {
		    discount = CommonUtil.toDouble( cardMap.get( "discount" ) );
		    resultMap.put( "discount", discount );
		}
	    }
	    logger.error( "联盟折扣====" + unionResult );
	    logger.error( "积分粉币规则====" + jifenFenbiRule );
	    logger.error( "会员信息===" + cardMap );
	}

	Integer isFenbi = 0;//是否能使用粉币
	Integer isJifen = 0;//是否能使用积分
	Integer isDiscount = 0;//是否能使用折扣
	double canUseJifenMoney = 0;//能使用积分的商品金额
	double canUseFenbiMoney = 0;//能使用粉币的商品金额
	if ( resultMap.containsKey( "shopList" ) && CommonUtil.isNotEmpty( resultMap.get( "shopList" ) ) ) {
	    JSONArray newShopArr = new JSONArray();
	    JSONArray shopArr = JSONArray.parseArray( JSONObject.toJSONString( resultMap.get( "shopList" ) ) );
	    for ( int i = 0; i < shopArr.size(); i++ ) {
		JSONObject shopObj = JSONObject.parseObject( shopArr.get( i ).toString() );
		Integer isCanUseYhq = 0;//是否能使用优惠券
		if ( CommonUtil.isNotEmpty( shopObj.get( "proList" ) ) ) {
		    JSONArray proArr = shopObj.getJSONArray( "proList" );
		    for ( Object pObj : proArr ) {
			JSONObject proObj = JSONObject.parseObject( pObj.toString() );
			//			isCanUseYhq = pObj.
			if ( proObj.getString( "is_coupons" ).equals( "1" ) ) {
			    isCanUseYhq = 1;
			}
			if ( proObj.getString( "is_member_discount" ).equals( "1" ) ) {
			    isDiscount = 1;
			}
			if ( proObj.getString( "is_fenbi_deduction" ).equals( "1" ) ) {
			    isFenbi = 1;
			    canUseFenbiMoney = CommonUtil.add( canUseFenbiMoney, proObj.getDouble( "total_price" ) );
			}
			if ( proObj.getString( "is_integral_deduction" ).equals( "1" ) ) {
			    isJifen = 1;
			    canUseJifenMoney = CommonUtil.add( canUseJifenMoney, proObj.getDouble( "total_price" ) );
			}
		    }
		}
		int wxShopId = shopObj.getInteger( "wx_shop_id" );
		List< Coupons > couponsList = new ArrayList<>();
		if ( CommonUtil.isNotEmpty( cardMap ) && isCanUseYhq == 1 ) {
		    //多粉优惠券
		    if ( cardMap.containsKey( "duofenCards" + wxShopId ) ) {
			Object obj = cardMap.get( "duofenCards" + wxShopId );
			couponsList = ToOrderUtil.getDuofenCouponsResult( obj, couponsList, null );
		    }
		    //微信优惠券
		    if ( cardMap.containsKey( "cardList" + wxShopId ) ) {
			Object obj = cardMap.get( "cardList" + wxShopId );
			couponsList = ToOrderUtil.getWxCouponsResult( obj, couponsList, null );
		    }
		    shopObj.put( "couponsList", couponsList );
		    newShopArr.add( shopObj );
		}
		resultMap.put( "shopList", newShopArr );
	    }
	}
	resultMap.put( "isDiscount", isDiscount );
	resultMap.put( "isFenbi", isFenbi );
	resultMap.put( "isJifen", isJifen );
	if ( canUseJifenMoney > 0 || canUseFenbiMoney > 0 ) {
	    JifenAndFenbBean bean = ToOrderUtil.getJifenFenbiParams( jifenFenbiRule, canUseJifenMoney, canUseFenbiMoney, cardMap );
	    if ( CommonUtil.isNotEmpty( bean ) ) {
		if ( CommonUtil.isNotEmpty( bean.getJifenMoney() ) && CommonUtil.isNotEmpty( bean.getJifenNum() ) && bean.getJifenMoney() > 0 && bean.getJifenNum() > 0 ) {
		    resultMap.put( "integral_money", bean.getJifenMoney() );
		    resultMap.put( "integral", bean.getJifenNum() );
		}
		if ( CommonUtil.isNotEmpty( bean.getFenbiMoney() ) && CommonUtil.isNotEmpty( bean.getFenbiNum() ) && bean.getFenbiMoney() > 0 && bean.getFenbiNum() > 0 ) {
		    resultMap.put( "fenbi_money", bean.getFenbiMoney() );
		    resultMap.put( "fenbi", bean.getFenbiNum() );
		}
	    }
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

    private MallOrder getOrderByParam( AppletSubmitOrderShopDTO shopObj, Member member ) {
	MallOrder order = new MallOrder();
	if ( CommonUtil.isNotEmpty( shopObj.getReceiveId() ) ) {
	    order.setReceiveId( CommonUtil.toInteger( shopObj.getReceiveId() ) );
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
	order.setOrderMoney( CommonUtil.toBigDecimal( shopObj.getOrderMoney() ) );
	double orderOldMoney = shopObj.getOrderMoney();
	if ( CommonUtil.isNotEmpty( shopObj.getOrderFreightMoney() ) ) {
	    order.setOrderFreightMoney( CommonUtil.toBigDecimal( shopObj.getOrderFreightMoney() ) );
	    orderOldMoney = CommonUtil.subtract( orderOldMoney, shopObj.getOrderFreightMoney() );
	}
	if ( CommonUtil.isNotEmpty( shopObj.getOrderMoney() ) ) {
	    order.setOrderOldMoney( CommonUtil.toBigDecimal( orderOldMoney ) );
	}
	if ( CommonUtil.isNotEmpty( shopObj.getOrderBuyerMessage() ) ) {
	    order.setOrderBuyerMessage( shopObj.getOrderBuyerMessage() );
	}
	if ( CommonUtil.isNotEmpty( shopObj.getOrderPayWay() ) ) {
	    order.setOrderPayWay( shopObj.getOrderPayWay() );
	}
	order.setOrderStatus( 1 );
	if ( CommonUtil.isNotEmpty( shopObj.getDeliveryMethod() ) ) {
	    order.setDeliveryMethod( shopObj.getDeliveryMethod() );
	    if ( order.getDeliveryMethod() == 2 ) {//上门自提
		order.setAppointmentName( shopObj.getAppointmentName() );
		order.setAppointmentTelephone( shopObj.getAppointmentTelephone() );
		order.setAppointmentTime( DateTimeKit.parse( shopObj.getAppointmentTime(), "" ) );
		order.setTakeTheirId( shopObj.getTakeTheirId() );
		order.setAppointmentStartTime( shopObj.getAppointmentStartTime() );
		order.setAppointmentEndTime( shopObj.getAppointmentEndTime() );
	    }
	}
	if ( CommonUtil.isNotEmpty( shopObj.getSelectCounpon() ) ) {
	    order.setSelectCoupon( shopObj.getSelectCounpon() );
	    order.setCouponId( shopObj.getSelectCounpon().getId() );
	}
	if ( CommonUtil.isNotEmpty( shopObj.getOrderType() ) ) {
	    order.setOrderType( shopObj.getOrderType() );
	}
	if ( CommonUtil.isNotEmpty( shopObj.getPJoinId() ) ) {
	    order.setPJoinId( shopObj.getPJoinId() );
	}
	order.setBuyerUserId( member.getId() );
	order.setBusUserId( member.getBusid() );
	if ( CommonUtil.isNotEmpty( member.getNickname() ) ) {
	    order.setMemberName( member.getNickname() );
	}
	if ( CommonUtil.isNotEmpty( shopObj.getFlowPhone() ) ) {
	    order.setFlowPhone( shopObj.getFlowPhone() );
	}
	if ( CommonUtil.isNotEmpty( shopObj.getShopId() ) ) {
	    order.setShopId( shopObj.getShopId() );
	}
	order.setBuyerUserType( 3 );
	order.setDiscountMoney( 0d );
	if ( CommonUtil.isNotEmpty( shopObj.getOrderId() ) && shopObj.getOrderId() > 0 ) {
	    order.setId( shopObj.getOrderId() );
	}
	return order;
    }

    private MallOrderDetail getOrderDetailByParam( AppletSubmitOrderProductDTO productObj, MallProduct product ) {
	MallOrderDetail detail = new MallOrderDetail();
	detail.setProductId( productObj.getProductId() );
	detail.setShopId( product.getShopId() );

	String imageUrl = productObj.getProductImageUrl();
	if ( imageUrl.contains( "/image" ) ) {
	    imageUrl = imageUrl.substring( imageUrl.indexOf( "/image" ), imageUrl.length() );
	} else if ( imageUrl.contains( "/upload" ) ) {
	    imageUrl = imageUrl.substring( imageUrl.indexOf( "/upload" ) + 7, imageUrl.length() );
	}
	detail.setProductImageUrl( imageUrl );

	detail.setDetProNum( productObj.getDetProNum() );
	detail.setDetProPrice( BigDecimal.valueOf( productObj.getDetProPrice() ) );

	detail.setTotalPrice( CommonUtil.toDouble( productObj.getTotalPrice() ) );
	detail.setNewTotalPrice( detail.getTotalPrice() );
	detail.setDetPrivivilege( product.getProCostPrice() );
	if ( CommonUtil.isNotEmpty( productObj.getProductSpecificas() ) ) {
	    detail.setProductSpecificas( productObj.getProductSpecificas() );
	    detail.setDetPrivivilege( product.getProPrice() );
	}
	if ( CommonUtil.isNotEmpty( productObj.getProductSpeciname() ) ) {
	    detail.setProductSpeciname( productObj.getProductSpeciname() );
	    //	    detail.setProductSpeciname( CommonUtil.getBytes( detail.getProductSpeciname() ) );
	}
	if ( CommonUtil.isNotEmpty( productObj.getDiscountedPrices() ) ) {
	    detail.setDiscountedPrices( BigDecimal.valueOf( productObj.getDiscountedPrices() ) );
	}
	detail.setDetProName( product.getProName() );
	if ( CommonUtil.isNotEmpty( product.getProCode() ) ) {
	    detail.setDetProCode( product.getProCode() );
	}
	if ( CommonUtil.isNotEmpty( productObj.getDetProMessage() ) ) {
	    detail.setDetProMessage( productObj.getDetProMessage() );
	}
	detail.setReturnDay( product.getReturnDay() );
	if ( CommonUtil.isNotEmpty( productObj.getDiscount() ) ) {
	    detail.setDiscount( CommonUtil.toIntegerByDouble( productObj.getDiscount() * 100 ) );
	}
	//	if ( CommonUtil.isNotEmpty( detailObj.get( "couponCode" ) ) ) {
	//	    detail.setCouponCode( CommonUtil.toString( detailObj.get( "couponCode" ) ) );
	//	}
	detail.setProTypeId( CommonUtil.toInteger( product.getProTypeId() ) );
	//	if ( CommonUtil.isNotEmpty( detailObj.get( "useFenbi" ) ) ) {
	//	    detail.setUseFenbi( CommonUtil.toDouble( detailObj.get( "useFenbi" ) ) );
	//	}
	//	if ( CommonUtil.isNotEmpty( detailObj.get( "useJifen" ) ) ) {
	//	    detail.setUseFenbi( CommonUtil.toDouble( detailObj.get( "useJifen" ) ) );
	//	}
	if ( CommonUtil.isNotEmpty( productObj.getProSpecStr() ) ) {
	    detail.setProSpecStr( productObj.getProSpecStr() );
	}
	if ( CommonUtil.isNotEmpty( product.getCardType() ) ) {
	    detail.setCardReceiveId( product.getCardType() );
	}
		/*if(CommonUtil.isNotEmpty(detailObj.get("cardReceiveId"))){
			detail.setCardReceiveId(CommonUtil.toInteger(detailObj.get("cardReceiveId")));
		}*/
	//	if ( CommonUtil.isNotEmpty( detailObj.get( "duofenCoupon" ) ) ) {
	//	    detail.setDuofenCoupon( CommonUtil.toString( detailObj.getString( "duofenCoupon" ) ) );
	//	}
	//	if ( CommonUtil.isNotEmpty( detailObj.get( "useCardId" ) ) ) {
	//	    detail.setUseCardId( CommonUtil.toInteger( detailObj.get( "useCardId" ) ) );
	//	}
	if ( CommonUtil.isNotEmpty( productObj.getCommission() ) ) {
	    detail.setCommission( BigDecimal.valueOf( productObj.getCommission() ) );
	}
	if ( CommonUtil.isNotEmpty( productObj.getSaleMemberId() ) ) {
	    detail.setSaleMemberId( productObj.getSaleMemberId() );
	}
	//	if ( CommonUtil.isNotEmpty( detailObj.get( "jifen_youhui" ) ) ) {
	//	    detail.setIntegralYouhui( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "jifen_youhui" ) ) ) );
	//	}
	//	if ( CommonUtil.isNotEmpty( detailObj.get( "fenbi_youhui" ) ) ) {
	//	    detail.setFenbiYouhui( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "fenbi_youhui" ) ) ) );
	//	}
	detail.setIsCanUseDiscount( product.getIsMemberDiscount() );
	detail.setIsCanUseCoupons( product.getIsCoupons() );
	detail.setIsCanUseJifen( product.getIsIntegralDeduction() );
	detail.setIsCanUseFenbi( product.getIsFenbiDeduction() );
	detail.setNewTotalPrice( detail.getTotalPrice() );
	detail.setStatus( -3 );
	detail.setUseJifen( 0d );
	detail.setUseFenbi( 0d );
	detail.setDiscountedPrices( CommonUtil.toBigDecimal( 0d ) );
	if ( CommonUtil.isNotEmpty( productObj.getDetailId() ) && productObj.getDetailId() > 0 ) {
	    detail.setId( productObj.getDetailId() );
	}
	return detail;
    }

    @Override
    public Map< String,Object > submitOrder( AppletSubmitOrderDTO params ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int code = 1;
	String msg = "";
	if ( CommonUtil.isEmpty( params.getOrder() ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整" );
	    return resultMap;
	}
	int memberId = CommonUtil.toInteger( params.getMemberId() );
	int shop_id = 0;
	Member member = memberService.findMemberById( memberId, null );
	WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( member.getBusid() );
	int orderPayWay = 0;
	//	int orderTypeId = 0;
	//判断库存
	List< MallOrder > orderList = new ArrayList<>();
	//	Map< String,Object > countMap = getIntegralFenbi( member );
	//	params.putAll( countMap );
	Map< Integer,MallProduct > proMap = new HashMap<>();
	Map< Integer,Coupons > selectCouponMap = new HashMap<>();//保存使用优惠券的信息
	StringBuilder wxShopIds = new StringBuilder( "," );
	logger.error( "订单集合" + params.getOrder() );
	//判断库存
	List< AppletSubmitOrderShopDTO > orderArray = JSONArray.parseArray( params.getOrder(), AppletSubmitOrderShopDTO.class );
	if ( orderArray != null && orderArray.size() > 0 ) {
	    for ( AppletSubmitOrderShopDTO shopObj : orderArray ) {
		if ( code == -1 ) {
		    break;
		}
		//		JSONObject orderObj = JSONObject.parseObject( object.toString() );
		MallOrder order = getOrderByParam( shopObj, member );
		order.setShopId( CommonUtil.toInteger( shopObj.getShopId() ) );
		orderPayWay = order.getOrderPayWay();
		//		orderTypeId = order.getOrderType();
		//		shop_id = order.getShopId();
		if ( CommonUtil.isNotEmpty( shopObj.getSelectCounpon() ) ) {//已使用优惠券
		    selectCouponMap.put( order.getShopId(), shopObj.getSelectCounpon() );
		}

		Map< String,Object > result = validateOrder( order, member );
		code = CommonUtil.toInteger( result.get( "code" ) );
		if ( code == -1 ) {
		    if ( CommonUtil.isNotEmpty( result.get( "errorMsg" ) ) ) {
			msg = result.get( "errorMsg" ).toString();
			throw new BusinessException( ResponseEnums.ERROR.getCode(), result.get( "errorMsg" ).toString() );
		    }
		    break;
		}

		//		double orderMoneys = 0;
		List< MallOrderDetail > detailList = new ArrayList< MallOrderDetail >();
		if ( CommonUtil.isNotEmpty( shopObj.getOrderDetail() ) ) {
		    List< AppletSubmitOrderProductDTO > detailArr = shopObj.getOrderDetail();
		    if ( detailArr != null && detailArr.size() > 0 ) {
			for ( AppletSubmitOrderProductDTO productObj : detailArr ) {
			    if ( code == -1 ) {
				break;
			    }
			    Integer productId = CommonUtil.toInteger( productObj.getProductId() );
			    MallProduct product = null;
			    if ( !proMap.containsKey( productId ) ) {
				product = productService.selectByPrimaryKey( productId );
				proMap.put( product.getId(), product );
			    } else {
				product = proMap.get( productId );
			    }

			    order.setMemCardType( product.getMemberType() );

			    MallOrderDetail detail = getOrderDetailByParam( productObj, product );

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
		//		if ( orderMoneys > 0 && orderMoneys != order.getOrderMoney().doubleValue() - order.getOrderFreightMoney().doubleValue() ) {
		//		    double orderMoney = order.getOrderFreightMoney().doubleValue() + orderMoneys;
		//		    order.setOrderMoney( CommonUtil.toBigDecimal( orderMoneys ) );
		//		    order.setOrderOldMoney( order.getOrderMoney() );
		//		}
		if ( detailList.size() == 0 && code == 1 ) {
		    //		    code = -1;
		    //		    msg = "参数有误";
		    //		    break;
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "参数有误" );
		}
		logger.info( "订单金额：" + order.getOrderMoney() );

		order.setMallOrderDetail( detailList );
		orderList.add( order );
		if ( !wxShopIds.toString().contains( "," + shopObj.getWxShopId() + "," ) ) {
		    wxShopIds.append( shopObj.getWxShopId() ).append( "," );
		}
	    }
	}
	params.setWxShopIds( wxShopIds.substring( 1, wxShopIds.length() - 1 ) );
	params.setOrderList( orderList );

	params = mallAppletCalculateService.calculateOrder( params, member );
	orderList = params.getOrderList();

	//	Map< String,Object > youhuiMaps = new HashMap< String,Object >();
	//	youhuiMaps.put( "fenbi_money", params.get( "fenbi_money" ) );
	//	youhuiMaps.put( "fenbi", params.get( "fenbi" ) );
	//	youhuiMaps.put( "integral_money", params.get( "integral_money" ) );
	//	youhuiMaps.put( "integral", params.get( "integral" ) );
	//	params.put( "shop_id", shop_id );
	//	List< MallOrder > newOrderList = curYouhuiOrder( params, orderList, proMap, selectCouponMap, wxduofenCouponMap, youhuiMaps, member );

	//	if ( CommonUtil.isNotEmpty( newOrderList ) ) {
	//	    orderList = new ArrayList< MallOrder >();
	//	    orderList.addAll( newOrderList );
	//	}
	double orderTotalMoney = 0;
	double orderFreightMoney = 0;
	double discountMoney = 0;
	double jifenDiscountMoney = 0;
	double fenbiDiscountMoney = 0;
	double useJifen = 0;
	double useFenbi = 0;
	double totalPrimary = 0;
	for ( MallOrder order : orderList ) {
	    logger.error( "discountMoney" + order.getDiscountMoney() + "---" + order.getOrderOldMoney() + "----" + order.getOrderMoney() );
	    orderFreightMoney += order.getOrderFreightMoney().doubleValue();
	    orderTotalMoney += order.getOrderMoney().doubleValue();
	    if ( CommonUtil.isNotEmpty( order.getOrderOldMoney() ) ) {
		totalPrimary += order.getOrderOldMoney().doubleValue();
	    }
	    if ( CommonUtil.isNotEmpty( order.getDiscountMoney() ) ) {
		discountMoney += order.getDiscountMoney();
	    }
	    if ( CommonUtil.isNotEmpty( order.getJifenDiscountMoney() ) ) {
		jifenDiscountMoney += order.getJifenDiscountMoney();
	    }
	    if ( CommonUtil.isNotEmpty( order.getFenbiDiscountMoney() ) ) {
		fenbiDiscountMoney += order.getFenbiDiscountMoney();
	    }
	    if ( CommonUtil.isNotEmpty( order.getUseJifen() ) ) {
		useJifen += order.getUseJifen();
	    }
	    if ( CommonUtil.isNotEmpty( order.getUseFenbi() ) ) {
		useFenbi += order.getUseFenbi();
	    }
	}
	logger.error( "总优惠：" + discountMoney );
	logger.error( "积分优惠金额：" + jifenDiscountMoney + "====使用积分数量:" + useJifen );
	logger.error( "粉币优惠金额：" + fenbiDiscountMoney + "====使用粉币数量：" + useFenbi );
	if ( orderTotalMoney > 0 ) {
	    orderTotalMoney = CommonUtil.toDouble( df.format( orderTotalMoney ) );
	}
	if ( orderFreightMoney > 0 ) {
	    orderFreightMoney = CommonUtil.toDouble( df.format( orderFreightMoney ) );
	}
	if ( params.getTotalPayMoney() != orderTotalMoney ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "订单优惠计算异常" );
	}
	if ( orderPayWay == 3 && code > 0 ) {//储值卡支付，判断储值卡余额是否足够
	    Map< String,Object > moneyMap = memberService.isAdequateMoney( member.getId(), orderTotalMoney );
	    if ( !moneyMap.get( "code" ).equals( "1" ) ) {//失败
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "您的储值卡余额不足" );
	    }
	}
	//	if ( orderList != null && orderList.size() > 0 ) {
	//	    logger.error( JSON.toJSON( orderList ) );
	//	    throw new BusinessException( -1, "ss" );
	//	}
	logger.info( "订单总金额：" + orderTotalMoney );
	String orderNo = "";
	//	MallOrderDetail orderDetail = null;
	int orderPId = 0;
	//保存订单信息
	if ( orderList != null && orderList.size() > 0 && code > 0 ) {
	    for ( int i = 0; i < orderList.size(); i++ ) {
		MallOrder order = orderList.get( i );
		if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
		    order.setSellerUserId( wxPublicUsers.getId() );
		}
		BigDecimal money = order.getOrderMoney();
		BigDecimal freightMoney = order.getOrderFreightMoney();
		if ( orderList.size() > 1 && i == 0 ) {
		    MallOrder newOrder = new MallOrder();
		    newOrder = order.clone();
		    newOrder.setOrderNo( "SC" + System.currentTimeMillis() );
		    newOrder.setOrderMoney( CommonUtil.toBigDecimal( orderTotalMoney ) );
		    newOrder.setOrderOldMoney( CommonUtil.toBigDecimal( totalPrimary ) );
		    newOrder.setOrderFreightMoney( CommonUtil.toBigDecimal( orderFreightMoney ) );
		    newOrder.setDiscountMoney( discountMoney );
		    newOrder.setJifenDiscountMoney( jifenDiscountMoney );
		    newOrder.setFenbiDiscountMoney( fenbiDiscountMoney );
		    newOrder.setUseJifen( useJifen );
		    newOrder.setUseFenbi( useFenbi );
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
		order.setOrderNo( "SC" + System.currentTimeMillis() );
		order.setCreateTime( new Date() );
		int count = -1;
		if ( CommonUtil.isNotEmpty( order.getId() ) && order.getId() > 0 ) {
		    count = orderDAO.updateById( order );
		} else {
		    count = orderDAO.insert( order );
		}
		if ( count < 0 ) {
		    code = -1;
		    break;
		} else if ( count > 0 && orderList.size() == 1 && i == 0 ) {
		    orderNo = order.getOrderNo();
		}
		if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			//			MallOrderDetail orderDetail = detail;
			detail.setOrderId( order.getId() );
			detail.setCreateTime( new Date() );
			if ( CommonUtil.isNotEmpty( detail.getId() ) && detail.getId() > 0 ) {
			    orderDetailDAO.updateById( detail );
			} else {
			    orderDetailDAO.insert( detail );
			}
			if ( count < 0 ) {
			    code = -1;
			    break;
			}
		    }
		}
	    }
	}
	if ( code == -1 && msg.equals( "" ) ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "提交订单失败，请稍后重试" );
	}
	if ( code > 0 ) {//提交订单成功，并且是货到付款，
	    //货到付款或支付金额为0的订单，直接修改订单状态为已支付，且修改商品库存和销量
	    if ( orderPayWay != 10 ) {
		Map< String,Object > payParams = new HashMap<>();
		payParams.put( "status", 2 );
		payParams.put( "out_trade_no", orderNo );
		orderService.paySuccessModified( payParams, member );//修改库存和订单状态
	    }
	    //删除购物车的商品
	    //	    if ( CommonUtil.isNotEmpty( params.getCartIds() ) ) {
	    //		JSONArray cartArrs = JSONArray.parseArray( params.getCartIds() );
	    //		if ( cartArrs != null && cartArrs.size() > 0 ) {
	    //		    for ( Object obj : cartArrs ) {
	    //			if ( CommonUtil.isNotEmpty( obj ) ) {
	    //			    shopCartDAO.deleteById( CommonUtil.toInteger( obj ) );
	    //			}
	    //		    }
	    //		}
	    //	    }
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
		//		code = -1;
		//		msg = "您的积分不够，不能用积分来兑换这件商品";
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "您的积分不够，不能用积分来兑换这件商品" );
	    }
	}
	if ( order.getOrderPayWay() == 8 ) {//粉币支付
	    double fenbi = member.getFansCurrency();
	    if ( fenbi < order.getOrderMoney().intValue() || fenbi < 0 ) {
		//		code = -1;
		//		msg = "您的粉币不够，不能用粉币来兑换这件商品";
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "您的粉币不够，不能用粉币来兑换这件商品" );
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
	    Map< String,Object > result = productService.calculateInventory( detail.getProductId(), detail.getProductSpecificas(), detail.getDetProNum(), order.getBuyerUserId() );
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
			    //			    msg = "卡券包已过期不能购买";
			    throw new BusinessException( ResponseEnums.ERROR.getCode(), "卡券包已过期不能购买" );
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
		//		resultMap.put( "code", ResponseEnums.ERROR.getCode() );
		//		resultMap.put( "errorMsg", map.get( "errorMsg" ) );
		//		return resultMap;
		throw new BusinessException( ResponseEnums.ERROR.getCode(), map.get( "errorMsg" ).toString() );
	    } else if ( map.get( "code" ).toString().equals( "1" ) ) {
		GetMobileInfo mobileInfo = JSONObject.toJavaObject( JSONObject.parseObject( map.get( "data" ).toString() ), GetMobileInfo.class );

		BusFlowInfo flow = fenBiFlowService.getFlowInfoById( product.getFlowId() );

		if ( mobileInfo.getSupplier().equals( "中国联通" ) && flow.getType() == 10 ) {
		    //		    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
		    //		    resultMap.put( "errorMsg", "充值失败,联通号码至少30MB" );
		    //		    return resultMap;
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "充值失败,联通号码至少30MB" );
		}
		if ( flow.getCount() == 0 ) {
		    //		    resultMap.put( "code", ResponseEnums.ERROR.getCode() );
		    //		    resultMap.put( "errorMsg", "流量数量不足，不能充值" );
		    //		    return resultMap;
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "流量数量不足，不能充值" );
		}
	    }
	}
	if ( ( CommonUtil.isEmpty( detail.getProTypeId() ) || detail.getProTypeId() == 0 ) && CommonUtil.isEmpty( order.getReceiveId() ) && code > 0 ) {
	    //	    msg = "请选择收货地址";
	    //	    code = -1;
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "请选择收货地址" );
	}
	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );
	return resultMap;
    }

    @Override
    public Map< String,Object > newCalculationPreferential( AppletSubmitOrderDTO params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	if ( CommonUtil.isEmpty( params.getOrder() ) ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	}
	List< AppletSubmitOrderShopDTO > orderList = JSONArray.parseArray( params.getOrder(), AppletSubmitOrderShopDTO.class );
	if ( orderList == null || orderList.size() == 0 ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	}

	//	int memberId = CommonUtil.toInteger( params.getMemberId() );
	//	Integer shopId = null;
	//	double orderTotalMoney = 0;//商品总价
	//	if ( CommonUtil.isNotEmpty( params.getTotalAllMoney() ) ) {
	//	    orderTotalMoney = CommonUtil.toDouble( params.getTotalAllMoney() );
	//	}
	if ( orderList != null && orderList.size() > 0 ) {
	    for ( AppletSubmitOrderShopDTO orderObj : orderList ) {
		Map< Object,MallEntity > productMap = new HashMap<>();

		if ( CommonUtil.isNotEmpty( orderObj.getOrderDetail() ) ) {
		    for ( AppletSubmitOrderProductDTO orderDetail : orderObj.getOrderDetail() ) {
			MallProduct product = productService.selectByPrimaryKey( orderDetail.getProductId() );
			orderDetail.setMallProduct( product );
		    }
		}
	    }
	}

	int isUseFenbi = 0;
	int isUseJifen = 0;
	if ( CommonUtil.isNotEmpty( params.getIsUseFenbi() ) ) {
	    isUseFenbi = params.getIsUseFenbi();
	}
	if ( CommonUtil.isNotEmpty( params.getIsUseJifen() ) ) {
	    isUseJifen = params.getIsUseJifen();
	}

	return resultMap;
    }
}
