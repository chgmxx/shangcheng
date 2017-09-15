package com.gt.mall.service.web.applet.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.applet.MallAppletImageDAO;
import com.gt.mall.dao.auction.MallAuctionDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.presale.MallPresaleDAO;
import com.gt.mall.dao.product.*;
import com.gt.mall.dao.seckill.MallSeckillDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.product.*;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.applet.MallHomeAppletService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductParamService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.result.shop.WsShopPhoto;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 商城分组 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallHomeAppletServiceImpl extends BaseServiceImpl< MallAppletImageDAO,MallAppletImage > implements MallHomeAppletService {

    @Autowired
    private MallAppletImageDAO      appletImageDAO;
    @Autowired
    private MallProductDAO          productDAO;
    @Autowired
    private MallProductGroupDAO     productGroupDAO;
    @Autowired
    private MallProductDetailDAO    productDetailDAO;
    @Autowired
    private MallGroupDAO            groupDAO;
    @Autowired
    private MallShopCartDAO         shopCartDAO;
    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;
    @Autowired
    private MallStoreDAO            storeDAO;

    @Autowired
    private MallGroupBuyDAO groupBuyDAO;
    @Autowired
    private MallSeckillDAO  seckillDAO;
    @Autowired
    private MallAuctionDAO  auctionDAO;
    @Autowired
    private MallPresaleDAO  presaleDAO;

    @Autowired
    private MallProductService          productService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MallProductParamService     productParamService;
    @Autowired
    private MallFreightService          freightService;
    @Autowired
    private MallPifaService             pifaService;
    @Autowired
    private MallPifaApplyService        pifaApplyService;
    @Autowired
    private MallImageAssociativeService imageAssociativeService;
    @Autowired
    private MallPageService             pageService;
    @Autowired
    private MallOrderService            orderService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private SmsService                  smsService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private WxShopService               wxShopService;
    @Autowired
    private MemberAddressService        memberAddressService;

    @Override
    public List< Map< String,Object > > selectGroupsByShopId( Map< String,Object > params ) {
	List< Map< String,Object > > productGroupList = new ArrayList< Map< String,Object > >();
	List< Map< String,Object > > groupList = productGroupDAO.selectGroupsByShopId( params );
	if ( groupList != null && groupList.size() > 0 ) {
	    for ( Map< String,Object > map : groupList ) {
		int isChild = 0;
		if ( CommonUtil.isNotEmpty( map.get( "is_child" ) ) ) {
		    isChild = CommonUtil.toInteger( map.get( "is_child" ) );
		    if ( isChild == 1 ) {
			//查询分类是否有子类
			Wrapper< MallGroup > groupWrapper = new EntityWrapper<>();
			groupWrapper.where( "group_p_id = {0}", map.get( "group_id" ) );
			List< MallGroup > list = groupDAO.selectList( groupWrapper );
			if ( list != null && list.size() > 0 ) {
			    isChild = 1;
			} else {
			    isChild = 0;
			}
		    }
		}
		map.put( "is_child", isChild );
		/*String jump_url = PropertiesUtil.getWebHomeUrl()+"/mallApplet/79B4DE7C/";
		if(isChild == 1){//有子类 跳转到分类页面
					jump_url += "clssAll.do?shopId="+map.get("shop_id")+"&memberId="+params.get("memberId")+"&classId="+map.get("group_id");
				}else{//没有子类  直接跳转到全部商品页面
					jump_url += "productAll.do?shopId="+map.get("shop_id")+"&memberId="+params.get("memberId")+"&classId="+map.get("group_id");
				}
				map.put("jump_url", jump_url);*/
		productGroupList.add( map );
	    }
	}
	return productGroupList;
    }

    @Override
    public PageUtil productAllList( Map< String,Object > params ) {
	String searchName = "";//排序
	String desc = "1";//搜索关键词
	int type = 1;//类型
	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = params.get( "desc" ).toString();
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "searchName" ) ) ) {
		searchName = params.get( "searchName" ).toString();
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = CommonUtil.toInteger( params.get( "type" ) );
	    }
	    params.put( "proName", searchName );
	    params.put( "desc", desc );
	    params.put( "type", type );
	}
	/*int memberId = 0;*/
	double discount = 1;//商品折扣
	/*if(CommonUtil.isNotEmpty(params.get("memberId"))){
	    memberId = CommonUtil.toInteger(params.get("memberId"));
		}
		//计算会员价
		if(memberId > 0){
			Member member = memberService.findById(memberId);
			discount = memberService.getMemberDiscount("1", member);//获取会员折扣
		}*/
	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	int rowCount = productDAO.selectCountAllByShopids( params );
	PageUtil page = new PageUtil( curPage, pageSize, rowCount, "" );
	params.put( "firstNum", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxNum", pageSize );

	List< Map< String,Object > > xlist = new ArrayList< Map< String,Object > >();
	List< Map< String,Object > > list = productDAO.selectProductAllByShopids( params );

	List< Integer > proIds = new ArrayList< Integer >();
	String specImgIds = "";

	if ( list != null && list.size() > 0 ) {
	    for ( int i = 0; i < list.size(); i++ ) {
		Map< String,Object > map1 = list.get( i );
		map1 = productGetPrice( map1, params, discount );
		xlist.add( map1 );

		if ( CommonUtil.isNotEmpty( map1.get( "specifica_img_id" ) ) ) {
		    if ( !map1.get( "specifica_img_id" ).toString().equals( "0" ) ) {
			if ( CommonUtil.isNotEmpty( specImgIds ) ) {
			    specImgIds += ",";
			}
			specImgIds += map1.get( "specifica_img_id" ).toString();
		    }
		}
		if ( CommonUtil.isNotEmpty( map1.get( "id" ) ) ) {
		    proIds.add( CommonUtil.toInteger( map1.get( "id" ) ) );
		}
	    }
	    String[] split = null;
	    if ( CommonUtil.isNotEmpty( specImgIds ) ) {
		split = specImgIds.split( "," );
	    }
	    xlist = pageService.getProductImages( xlist, proIds, split );
	}
	page.setSubList( xlist );
	return page;
    }

    private Map< String,Object > productGetPrice( Map< String,Object > map1, Map< String,Object > params, double discount ) {
	String is_specifica = map1.get( "is_specifica" ).toString();//是否有规格，1有规格，有规格取规格里面的值
	double price = 0;
	String imageUrl = "";
	if ( is_specifica == "1" || is_specifica.equals( "1" ) ) {
	    if ( CommonUtil.isNotEmpty( map1.get( "inv_price" ) ) ) {
		price = CommonUtil.toDouble( map1.get( "inv_price" ) );
	    }
	    if ( CommonUtil.isNotEmpty( map1.get( "specifica_img_url" ) ) ) {
		imageUrl = CommonUtil.toString( map1.get( "specifica_img_url" ) );
	    }
	}
	if ( CommonUtil.isEmpty( price ) || price <= 0 ) {
	    price = CommonUtil.toDouble( map1.get( "pro_price" ) );
	}
	if ( CommonUtil.isEmpty( imageUrl ) && CommonUtil.isNotEmpty( map1.get( "image_url" ) ) ) {
	    imageUrl = CommonUtil.toString( map1.get( "image_url" ) );
	}

	Map< String,Object > proMap = new HashMap< String,Object >();
	if ( CommonUtil.isNotEmpty( imageUrl ) ) {
	    proMap.put( "image_url", PropertiesUtil.getResourceUrl() + imageUrl );
	}
	DecimalFormat df = new DecimalFormat( "######0.00" );
	if ( price > 0 ) {
	    String proPrice = df.format( price );
	    /*if(discount > 0){
		double memberProPrice = CommonUtil.toDouble(df.format(price*discount));
			}*/
	    proMap.put( "pro_price", proPrice );

	    String[] priceStr = proPrice.split( "\\." );
	    if ( priceStr != null && priceStr.length > 1 ) {
		proMap.put( "price_first", priceStr[0] );
		proMap.put( "price_next", priceStr[1] );
	    }
	}
	int saleTotal = 0;
	if ( CommonUtil.isNotEmpty( map1.get( "pro_sale_total" ) ) ) {
	    saleTotal += CommonUtil.toInteger( map1.get( "pro_sale_total" ) );
	}
	if ( CommonUtil.isNotEmpty( map1.get( "sales_base" ) ) ) {
	    saleTotal += CommonUtil.toInteger( map1.get( "sales_base" ) );
	}
	//		String jump_url = PropertiesUtil.getWebHomeUrl()+"/mallApplet/79B4DE7C/phoneProduct.do?memberId="+params.get("memberId")+"&shopId="+map1.get("shopId")+"&productId="+map1.get("id");
	/*int is_return = 1;
		if(CommonUtil.isNotEmpty(map1.get("pro_type_id")) && CommonUtil.isNotEmpty(map1.get("member_type"))){
			if(CommonUtil.toInteger(map1.get("pro_type_id")) == 2 && CommonUtil.toInteger(map1.get("member_type")) > 0){
//				jump_url = "";
				is_return = 0;
				proMap.put("return_msg", "会员卡购买正在开发。。。请耐心等待");
			}
		}
//		proMap.put("jump_url", jump_url);
		proMap.put("is_return", is_return);*/
	proMap.put( "pro_name", map1.get( "pro_name" ) );
	if ( CommonUtil.isNotEmpty( map1.get( "pro_label" ) ) ) {
	    proMap.put( "pro_label", map1.get( "pro_label" ) );
	}
	proMap.put( "sale_total", saleTotal );
	proMap.put( "pro_type_id", map1.get( "pro_type_id" ) );
	proMap.put( "shop_id", map1.get( "shop_id" ) );
	proMap.put( "id", map1.get( "id" ) );
	return proMap;
    }

    @Override
    public Map< String,Object > getActivityList( Map< String,Object > params ) {
	int isShowActivity = 0;//是否有活动  0没有活动  1有活动
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int isGroup = 0;
	int isSeckill = 0;
	int isAuction = 0;
	int isPresale = 0;
	params.put( "status", 1 );
	params.put( "isPublic", 0 );
	//查询是否有正在进行的团购商品
	int groupCount = groupBuyDAO.selectCountByShopId( params );
	if ( groupCount > 0 ) {
	    isGroup = 1;
	    isShowActivity = 1;
	}
	//查询是否有正在进行的秒杀商品
	int seckillCount = seckillDAO.selectCountByShopId( params );
	if ( seckillCount > 0 ) {
	    isSeckill = 1;
	    isShowActivity = 1;
	}
	//查询是否有正在进行的拍卖商品
	int auctionCount = auctionDAO.selectCountByShopId( params );
	if ( auctionCount > 0 ) {
	    isAuction = 1;
	    isShowActivity = 1;
	}
	//查询是否有正在进行的预售商品
	int presaleCount = presaleDAO.selectCountByShopId( params );
	if ( presaleCount > 0 ) {
	    isPresale = 1;
	    isShowActivity = 1;
	}
	resultMap.put( "is_group", isGroup );
	resultMap.put( "is_seckill", isSeckill );
	resultMap.put( "is_auction", isAuction );
	resultMap.put( "is_presale", isPresale );
	resultMap.put( "isShowActivity", isShowActivity );
	return resultMap;
    }

    @Override
    public Map< String,Object > selectProductDetailById( Map< String,Object > params ) {
	DecimalFormat df = new DecimalFormat( "######0.00" );
	Map< String,Object > productMap = new HashMap< String,Object >();
	int productId = CommonUtil.toInteger( params.get( "productId" ) );
	MallProduct product = productDAO.selectById( productId );//查询商品信息
	productMap.put( "product_name", product.getProName() );//商品名称
	productMap.put( "product_sale", product.getProSaleTotal() + product.getSalesBase() );//商品销量
	productMap.put( "product_code", product.getProCode() );
	if ( CommonUtil.isNotEmpty( product.getProLabel() ) ) {
	    productMap.put( "product_label", product.getProLabel() );//商品标签
	}
	if ( CommonUtil.isNotEmpty( product.getIsDelete() ) && CommonUtil.isNotEmpty( product.getCheckStatus() ) && CommonUtil.isNotEmpty( product.getIsPublish() ) ) {
	    if ( !product.getIsDelete().toString().equals( "0" ) || !product.getCheckStatus().toString().equals( "1" ) || !product.getIsPublish().toString().equals( "1" ) ) {
		productMap.put( "isNoShow", 1 );//不显示商品详细页面
	    }
	}
	productMap.put( "product_type_id", product.getProTypeId() );
	double proPrice = CommonUtil.toDouble( product.getProPrice() );
	double proCostPrice = 0;
	if ( CommonUtil.isNotEmpty( product.getProCostPrice() ) ) {
	    proCostPrice = CommonUtil.toDouble( product.getProCostPrice() );
	}
	Member member = memberService.findMemberById( CommonUtil.toInteger( params.get( "memberId" ) ), null );
	int userPId = busUserService.getMainBusId( member.getBusid() );//通过用户名查询主账号id
	long isJxc = 0;
	busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
	//计算会员价
	double discount = 1;//商品折扣
	if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) && CommonUtil.isNotEmpty( product.getIsMemberDiscount() ) ) {
	    discount = productService.getMemberDiscount( CommonUtil.toString( product.getIsMemberDiscount() ), member );//获取会员折扣
	}
	//查询商品价格
	if ( CommonUtil.isNotEmpty( product.getIsSpecifica() ) ) {
	    if ( product.getIsSpecifica().toString().equals( "1" ) ) {//存在商品规格
		//查询商品的规格价
		Map< String,Object > defaultInvMap = productInventoryService.productSpecifications( productId, "" );//查询商品默认的

		List< Map< String,Object > > specificaList = productSpecificaService.getSpecificaByProductId( productId );//获取商品规格值
		List< Map< String,Object > > guigePriceList = productInventoryService.guigePrice( productId );//根据商品id获取所有规格信息
		if ( defaultInvMap == null || specificaList == null || specificaList.size() == 0 ) {
		    product.setIsSpecifica( 0 );
		}
		if ( CommonUtil.isNotEmpty( defaultInvMap ) ) {
		    proCostPrice = proPrice;
		    proPrice = CommonUtil.toDouble( defaultInvMap.get( "inv_price" ) );
		    if ( CommonUtil.isNotEmpty( defaultInvMap.get( "specifica_name" ) ) ) {
			String names = defaultInvMap.get( "specifica_name" ).toString();
			defaultInvMap.put( "specifica_name", names.replaceAll( "&nbsp;&nbsp;", "," ) );
		    }
		}
		//获取进销存库存
		if ( isJxc == 1 && CommonUtil.isNotEmpty( product.getErpProId() ) && product.getErpProId() > 0 && guigePriceList != null && guigePriceList.size() > 0 ) {
		    List< Map< String,Object > > erpInvList = null;
		    try {
			erpInvList = productService.getErpInvByProId( product.getErpProId(), product.getShopId() );
		    } catch ( Exception e ) {
			e.printStackTrace();
		    }
		    if ( erpInvList != null && erpInvList.size() > 0 ) {
			int totalInvNum = 0;
			for ( Map< String,Object > erpMap : erpInvList ) {
			    int erpInvId = CommonUtil.toInteger( erpMap.get( "erpInvId" ) );
			    int invNum = CommonUtil.toInteger( erpMap.get( "invNum" ) );
			    if ( guigePriceList != null && guigePriceList.size() > 0 ) {
				for ( Map< String,Object > map : guigePriceList ) {
				    int erp_inv_id = CommonUtil.toInteger( product.getErpInvId() );
				    if ( erp_inv_id == erpInvId ) {
					totalInvNum += invNum;
					map.put( "inv_num", invNum );
				    }
				}
			    } else {
				int erp_inv_id = CommonUtil.toInteger( product.getErpInvId() );
				if ( erp_inv_id > 0 && erp_inv_id == erpInvId ) {
				    product.setProStockTotal( invNum );
				}
			    }
			}
			if ( totalInvNum > 0 ) {
			    product.setProStockTotal( totalInvNum );
			}
		    }

		}
		if ( guigePriceList != null && guigePriceList.size() > 0 && discount > 0 && discount < 1 ) {
		    List< Map< String,Object > > newGuigeList = new ArrayList< Map< String,Object > >();
		    for ( Map< String,Object > guigeMap : guigePriceList ) {
			if ( CommonUtil.isNotEmpty( guigeMap.get( "inv_price" ) ) ) {
			    double inv_price = CommonUtil.toDouble( guigeMap.get( "inv_price" ) );
			    double memberProPrice = CommonUtil.toDouble( df.format( inv_price * discount ) );
			    guigeMap.put( "member_price", memberProPrice );
			}
			newGuigeList.add( guigeMap );
		    }
		    if ( newGuigeList != null && newGuigeList.size() > 0 ) {
			guigePriceList = new ArrayList< Map< String,Object > >();
			guigePriceList = newGuigeList;
		    }
		}
		productMap.put( "specificaList", specificaList );
		productMap.put( "guigePriceList", guigePriceList );
		productMap.put( "defaultInvMap", defaultInvMap );

	    }
	}
	productMap.put( "product_price", proPrice );//商品价
	if ( proCostPrice > proPrice ) {
	    productMap.put( "product_cost_price", proCostPrice );//商品原价
	}
	if ( discount > 0 && discount < 1 ) {
	    double memberProPrice = CommonUtil.toDouble( df.format( proPrice * discount ) );
	    productMap.put( "member_price", memberProPrice );
	}

	//计算批发价
	boolean isPifa = pifaApplyService.isPifa( member );
	if ( isPifa ) {
	    MallPifa pifa = pifaService.getPifaByProId( product.getId(), product.getShopId() );
	    if ( CommonUtil.isNotEmpty( pifa ) ) {
		productMap.put( "wholesale_price", pifa.getPfPrice() );
	    }
	}

	Map addressMap = null;
	//获取收货地址
	if ( CommonUtil.isNotEmpty( member ) ) {
	    List< Integer > memberList = memberService.findMemberListByIds( member.getId() );//查询会员信息
	    addressMap = memberAddressService.addressDefault( CommonUtil.getMememberIds( memberList, member.getId() ) );
	}
	if ( addressMap != null && addressMap.size() > 0 ) {
	    if ( CommonUtil.isNotEmpty( addressMap ) ) {
		String address = CommonUtil.toString( addressMap.get( "provincename" ) ) + CommonUtil.toString( addressMap.get( "cityname" ) ) + CommonUtil
				.toString( addressMap.get( "areaname" ) ) + CommonUtil.toString( addressMap.get( "memAddress" ) );
		productMap.put( "address", address );
		productMap.put( "address_id", addressMap.get( "id" ) );
	    }

	}
	productMap.put( "freightPrice",
			freightService.getFreightByProvinces( params, addressMap, product.getShopId(), proPrice * discount, CommonUtil.toDouble( product.getProWeight() ) ) );

	//查询店铺信息
	Map< String,Object > shopMap = pageService.shopmessage( product.getShopId(), null );
	if ( CommonUtil.isNotEmpty( shopMap ) ) {
	    //店铺图片
	    if ( CommonUtil.isNotEmpty( shopMap.get( "stoPicture" ) ) ) {
		productMap.put( "shopImage", PropertiesUtil.getResourceUrl() + shopMap.get( "stoPicture" ) );
	    } else {
		productMap.put( "shopImage", PropertiesUtil.getResourceUrl() + shopMap.get( "sto_picture" ) );
	    }
	    //店铺名称
	    if ( CommonUtil.isNotEmpty( shopMap.get( "business_name" ) ) ) {
		productMap.put( "shopName", shopMap.get( "business_name" ) );
	    } else {
		productMap.put( "shopName", shopMap.get( "sto_name" ) );
	    }
	}

	//查询商品详情
	MallProductDetail productDetail = new MallProductDetail();
	productDetail.setProductId( productId );
	productDetail = productDetailDAO.selectOne( productDetail );
	productMap.put( "product_detail", productDetail.getProductDetail() );//商品详情
	if ( CommonUtil.isNotEmpty( productDetail.getProductMessage() ) ) {
	    productMap.put( "product_message", productDetail.getProductMessage() );//商品信息
	    String msg = productDetail.getProductMessage();
	    if ( msg.length() > 20 ) {
		msg = msg.substring( 0, 20 ) + "...";
	    }
	    productMap.put( "product_message_sub", msg );
	}

	//查询商品图片
	params.put( "assId", productId );
	List< Map< String,Object > > imageList = imageAssociativeDAO.selectByAssId( params );
	List< Map< String,Object > > newImageList = new ArrayList< Map< String,Object > >();
	if ( imageList != null && imageList.size() > 0 ) {
	    for ( Map< String,Object > map : imageList ) {
		map.put( "image_url", PropertiesUtil.getResourceUrl() + map.get( "image_url" ) );
		newImageList.add( map );
	    }
	}
	productMap.put( "imagelist", newImageList );
	//查询商品规格参数
	List< MallProductParam > paramList = productParamService.getParamByProductId( productId );
	productMap.put( "paramList", paramList );

	//查询用户id
	List< Integer > memberList = memberService.findMemberListByIds( member.getId() );//查询会员信息
	params.put( "memberList", memberList );

	//查询购物车的数量
	params.put( "type", 0 );
	params.put( "busUserId", product.getUserId() );
	int shopCartNum = shopCartDAO.selectShopCartNum( params );
	productMap.put( "shopCartNum", shopCartNum );
	return productMap;
    }

    @Override
    public Map< String,Object > addshopping( Map< String,Object > params ) {
	MallShopCart cart = new MallShopCart();
	Map< String,Object > resultMap = new HashMap< String,Object >();
	if ( CommonUtil.isNotEmpty( params.get( "shopCart" ) ) ) {
	    cart = (MallShopCart) JSONObject.toJavaObject( JSONObject.parseObject( params.get( "shopCart" ).toString() ), MallShopCart.class );
	} else {
	    resultMap.put( "errorMsg", "缺少参数" );
	    resultMap.put( "code", -1 );
	    return resultMap;
	}
	int count = 0;
	Map< String,Object > result = new HashMap< String,Object >();

	if ( CommonUtil.isNotEmpty( cart.getProductId() ) ) {
	    MallProduct product = productService.selectByPrimaryKey( cart.getProductId() );
	    if ( CommonUtil.isEmpty( cart.getProductSpecificas() ) && product.getIsSpecifica().toString().equals( "1" ) ) {
		Map< String,Object > specificaMap = productInventoryService.productSpecifications( cart.getProductId(), null );//查询规格集合
		if ( CommonUtil.isNotEmpty( specificaMap ) ) {
		    if ( CommonUtil.isNotEmpty( specificaMap.get( "xids" ) ) ) {
			cart.setProductSpecificas( CommonUtil.toString( specificaMap.get( "xids" ) ) );
		    }
		    if ( CommonUtil.isNotEmpty( specificaMap.get( "specifica_name" ) ) ) {
			cart.setProductSpeciname( CommonUtil.toString( specificaMap.get( "specifica_name" ) ) );
		    }
		    if ( CommonUtil.isNotEmpty( specificaMap.get( "inv_price" ) ) ) {
			cart.setPrimaryPrice( cart.getPrice() );
			cart.setPrice( BigDecimal.valueOf( CommonUtil.toDouble( specificaMap.get( "inv_price" ) ) ) );
		    }
		}
	    }
	}

	//判断商品库存,限购
	if ( CommonUtil.isNotEmpty( cart.getProSpecStr() ) ) {
	    Map map = JSONObject.parseObject( cart.getProSpecStr() );
	    for ( Object key : map.keySet() ) {
		String proSpecificas = key.toString();
		Map p = JSONObject.parseObject( map.get( key ).toString() );
		int proNum = CommonUtil.toInteger( p.get( "num" ) );
		result = productService.calculateInventory( cart.getProductId(), proSpecificas, proNum, cart.getUserId() );
	    }
	} else {
	    result = productService.calculateInventory( cart.getProductId(), cart.getProductSpecificas(), cart.getProductNum(), cart.getUserId() );
	}
	if ( CommonUtil.isNotEmpty( result ) ) {
	    if ( result.get( "result" ).toString().equals( "false" ) ) {
		resultMap.put( "errorMsg", result.get( "msg" ) );
		resultMap.put( "code", -1 );
		return resultMap;
	    }
	}

	List< Map< String,Object > > cartList = shopCartDAO.selectByShopCart( cart );
	if ( cartList.size() > 0 ) {

	    Map< String,Object > map = cartList.get( 0 );

	    if ( CommonUtil.isNotEmpty( cart.getProSpecStr() ) ) {
		cart = getProSpecNum( map, cart );
	    }
	    cart.setId( CommonUtil.toInteger( map.get( "id" ) ) );
	    count = shopCartDAO.updateByShopCart( cart );
	} else {
	    cart.setCreateTime( new Date() );
	    count = shopCartDAO.insert( cart );
	}
	if ( count > 0 ) {
	    resultMap.put( "code", 1 );
	} else {
	    resultMap.put( "code", -1 );
	}
	return resultMap;
    }

    /**
     * 判断批发购物车的规格
     *
     * @param map
     * @param cart
     *
     * @return
     */
    @SuppressWarnings( "rawtypes" )
    private MallShopCart getProSpecNum( Map< String,Object > map, MallShopCart cart ) {
	if ( CommonUtil.isNotEmpty( map.get( "pro_spec_str" ) ) ) {
	    JSONObject specIdObj = new JSONObject();

	    JSONObject speObj = JSONObject.parseObject( map.get( "pro_spec_str" ).toString() );//商品规格和数量

	    if ( CommonUtil.isNotEmpty( cart.getProSpecStr() ) ) {
		JSONObject speObj2 = JSONObject.parseObject( cart.getProSpecStr() );

		Iterator it = (Iterator) speObj.keySet();

		while ( it.hasNext() ) {
		    String str = it.next().toString();
		    if ( CommonUtil.isNotEmpty( speObj2.get( str ) ) ) {
			JSONObject proObj = JSONObject.parseObject( speObj.get( str ).toString() );
			JSONObject proObj2 = JSONObject.parseObject( speObj2.get( str ).toString() );

			int num = CommonUtil.toInteger( proObj.get( "num" ) );
			int num2 = CommonUtil.toInteger( proObj2.get( "num" ) );
			num += num2;

			proObj2.put( "num", num + "" );

			specIdObj.put( str, proObj2.toString() );

			speObj2.remove( str );
		    } else {
			specIdObj.put( str, speObj.get( str ) );

			speObj2.remove( str );
		    }
		}
		if ( speObj2 != null && speObj2.size() > 0 ) {
		    Iterator it2 = (Iterator) speObj2.keySet();

		    while ( it2.hasNext() ) {
			String str = it2.next().toString();
			specIdObj.put( str, speObj2.get( str ) );
		    }
		}
		cart.setProSpecStr( specIdObj.toString() );
	    }
	}
	return cart;
    }

    @Override
    public Map< String,Object > getMemberPage( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Member member = memberService.findMemberById( memberId, null );
	resultMap.put( "member_name", member.getNickname() );
	resultMap.put( "head_image", member.getHeadimgurl() );
	resultMap.put( "telephone", member.getPhone() );

	int isAdvert = 0;
	BusUser user = busUserService.selectById( member.getBusid() );
	if ( CommonUtil.isNotEmpty( user ) ) {
	    if ( CommonUtil.isNotEmpty( user.getAdvert() ) ) {
		if ( user.getAdvert() == 0 ) {
		    isAdvert = 1;
		}
	    }
	}
	resultMap.put( "isAdvert", isAdvert );


		/*double minCosumeMoney = 0;
		double consumeMoney = 0;
		MallSellerSet sellerSet = mallSellerService.selectByBusUserId(member.getBusid());
		if(CommonUtil.isNotEmpty(sellerSet)){
			if(CommonUtil.isNotEmpty(sellerSet.getConsumeMoney())){
				minCosumeMoney = CommonUtil.toDouble(sellerSet.getConsumeMoney());
				if(minCosumeMoney > 0){
					if(CommonUtil.isNotEmpty(member)){
						//判断消费金额
						Map<String, Object> consumeMap = orderService.selectSumSaleMoney(member.getId());
						if(CommonUtil.isNotEmpty(consumeMap)){
							if(CommonUtil.isNotEmpty(consumeMap.get("orderMoney"))){
								consumeMoney = CommonUtil.toDouble(consumeMap.get("orderMoney"));
							}
						}
					}
					if(minCosumeMoney > consumeMoney){
						resultMap.put("minCosumeMoney", minCosumeMoney);
						resultMap.put("consumeMoney", consumeMoney);
					}
				}
			}
		}

		MallPaySet set = mallPaySetService.selectByMember(member);
		if(CommonUtil.isNotEmpty(set)){
			if(set.getIsPf().toString().equals("1")){
				int status = mallPifaApplayService.getPifaApplay(member,set);
				String msg = "";
				if(status == 0){
					msg = "您的批发商申请在审核中请耐心等待1-3个工作日";
				}else if(status == -1){
					msg = "您的批发商申请不通过";
				}
				resultMap.put("pfMsg", msg);
				resultMap.put("isOpenPf", 1);
			}
			if(set.getIsSeller().toString().equals("1")){
				int status = mallSellerService.selectSellerStatusByMemberId(member,set);
				String msg = "";
				if(status == 0){
					msg = "您的超级销售员申请在审核中请耐心等待1-3个工作日";
				}else if(status == -1){
					msg = "您的超级销售员申请不通过";
				}else if(status == -3){
					msg = "您的超级销售员已经被暂停了，不能继续使用";
				}else if(minCosumeMoney > 0 && consumeMoney > 0){
					msg = "加入超级销售员消费额必须要达到"+minCosumeMoney+"元，您的消费额只有"+consumeMoney+"元";
				}
				resultMap.put("sellerMsg", msg);
				resultMap.put("isOpenSeller", 1);
			}
		}*/

	return resultMap;
    }

    @Override
    public int getAdvert( Map< String,Object > params ) {
	int isAdvert = 0;
	BusUser user = busUserService.selectById( CommonUtil.toInteger( params.get( "busUserId" ) ) );
	if ( CommonUtil.isNotEmpty( user ) ) {
	    if ( CommonUtil.isNotEmpty( user.getAdvert() ) ) {
		if ( user.getAdvert() == 0 ) {
		    isAdvert = 1;
		}
	    }
	}
	return isAdvert;
    }

    @Override
    public List< Map< String,Object > > getShopList( Map< String,Object > params ) {
	Double longitude = 0.00;
	Double latitude = 0.00;
	if ( CommonUtil.isNotEmpty( params.get( "longitude" ) ) ) {
	    longitude = CommonUtil.toDouble( params.get( "longitude" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "latitude" ) ) ) {
	    latitude = CommonUtil.toDouble( params.get( "latitude" ) );
	}
	int userId = CommonUtil.toInteger( params.get( "userId" ) );
	List< Map< String,Object > > list = storeDAO.findByUserId( userId );
	DecimalFormat df = new DecimalFormat( "0.00" );
	List< Map< String,Object > > shopList = new ArrayList< Map< String,Object > >();
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > map : list ) {
		WsWxShopInfo wxShopInfo = wxShopService.getShopById( CommonUtil.toInteger( map.get( "wx_shop_id" ) ) );
		List< WsShopPhoto > shopPhotoList = wxShopService.getShopPhotoByShopId( wxShopInfo.getId() );
		Map< String,Object > shopMap = new HashMap< String,Object >();
		if ( longitude > 0 && latitude > 0 ) {
		    Double raill = CommonUtil.getDistance( longitude, latitude, CommonUtil.toDouble( wxShopInfo.getLongitude() ), CommonUtil.toDouble( wxShopInfo.getLatitude() ) );
		    raill = raill / 1000;
		    shopMap.put( "raill", df.format( raill ) );
		} else {
		    shopMap.put( "raill", "-1" );
		}
		if ( shopPhotoList != null && shopPhotoList.size() > 0 ) {
		    if ( CommonUtil.isNotEmpty( shopPhotoList.get( 0 ).getLocalAddress() ) ) {
			shopMap.put( "shopImage", PropertiesUtil.getResourceUrl() + shopPhotoList.get( 0 ).getLocalAddress() );
		    } else {
			shopMap.put( "shopImage", PropertiesUtil.getResourceUrl() + map.get( "stoPicture" ) );
		    }
		}

		String cityids = wxShopInfo.getProvince() + "," + wxShopInfo.getCity() + "," + wxShopInfo.getDistrict();
		List< Map > cityList = wxShopService.queryBasisCityIds( cityids );
		String address = wxShopInfo.getAddress() + wxShopInfo.getDetail();
		StringBuilder shopAddress = new StringBuilder();
		if ( cityList != null && cityList.size() > 0 ) {
		    for ( Map map1 : cityList ) {
			shopAddress.append( map1.get( "city_name" ) );
		    }
		}
		shopAddress.append( address );

		shopMap.put( "shopName", wxShopInfo.getBusinessName() );
		shopMap.put( "shopAddress", shopAddress.toString() );
		shopMap.put( "telephone", wxShopInfo.getTelephone() );
		shopMap.put( "id", map.get( "id" ) );
		shopList.add( shopMap );
	    }
	    Collections.sort( shopList, new MallComparatorUtil( "raill" ) );
	}

	return shopList;
    }

    @Override
    public List< Map< String,Object > > getAppletImageByBusUser( Map< String,Object > params ) {
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	List< MallAppletImage > imageList = appletImageDAO.selectByImage( params );
	if ( imageList != null && imageList.size() > 0 ) {
	    for ( MallAppletImage mallAppletImage : imageList ) {
		Map< String,Object > maps = new HashMap< String,Object >();
		maps.put( "imageUrl", mallAppletImage.getImageUrl() );
		maps.put( "shopId", mallAppletImage.getShopId() );
		maps.put( "type", mallAppletImage.getType() );
		maps.put( "proId", mallAppletImage.getProId() );
		list.add( maps );
	    }
	}
	return list;
    }

    @Override
    public Map< String,Object > getValCode( Map< String,Object > params ) throws Exception {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int busId = CommonUtil.toInteger( params.get( "busId" ) );
	WxPublicUsers pbUser = wxPublicUserService.selectByUserId( busId );
	String no = Constants.REDIS_KEY + CommonUtil.getPhoneCode();
	JedisUtil.set( no, no, 10 * 60 );
	System.out.println( no );

	OldApiSms apiSms = new OldApiSms();
	apiSms.setBusId( busId );
	apiSms.setCompany( pbUser.getAuthorizerInfo() );
	apiSms.setContent( "" + pbUser.getAuthorizerInfo() + "  提醒您，您的验证码为：(" + no + ")" + "，验证码10分钟内有效，请尽快完成验证。" );
	apiSms.setMobiles( params.get( "phoneNo" ).toString() );
	apiSms.setModel( CommonUtil.toInteger( Constants.SMS_MODEL ) );

	boolean result = smsService.sendSmsOld( apiSms );
	if ( result ) {
	    resultMap.put( "code", 1 );
	    resultMap.put( "msg", "发送成功" );
	} else {
	    resultMap.put( "code", -1 );
	    resultMap.put( "msg", "发送失败" );
	}
	return resultMap;
    }

    @Override
    public Map< String,Object > bindPhones( Map< String,Object > params ) throws Exception {
	Map< String,Object > resultMap = new HashMap<>();
	Member member = memberService.findMemberById( CommonUtil.toInteger( params.get( "memberId" ) ), null );
	member = memberService.bingdingPhone( params, member );
	if ( !"".equals( member.getPhone() ) ) {
	    resultMap.put( "result", true );
	    resultMap.put( "message", "绑定成功" );
	} else {
	    resultMap.put( "result", false );
	    resultMap.put( "message", "绑定失败" );
	}
	return resultMap;
    }
}
