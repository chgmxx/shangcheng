package com.gt.mall.service.web.product.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.bean.wx.flow.WsBusFlowInfo;
import com.gt.mall.bean.wx.shop.WsWxShopInfo;
import com.gt.mall.dao.freight.MallFreightDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.product.MallShopCartService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.SessionUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallShopCartServiceImpl extends BaseServiceImpl< MallShopCartDAO,MallShopCart > implements MallShopCartService {

    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private MallStoreService            mallStoreService;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallShopCartDAO             mallShopCartDAO;
    @Autowired
    private WxShopService               wxShopService;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private MallFreightService          mallFreightService;
    @Autowired
    private MallFreightDAO              mallFreightDao;
    @Autowired
    private FenBiFlowService            fenBiFlowService;
    @Autowired
    private MallOrderService            mallOrderService;

    @Override
    public List< Map< String,Object > > getProductByShopCart( String shopcards, WxPublicUsers pbUser, Member member, int userId ) {
	List< Map< String,Object > > list = new ArrayList<>();

	BusUser user = new BusUser();
	user.setId( userId );
	List< Map< String,Object > > shopList = mallStoreService.findAllStoByUser( user, null );

	Map< String,Object > params = new HashMap<>();
	params.put( "cartIds", shopcards.split( "," ) );
	params.put( "busUserId", userId );
	int index = 1;
	List< Map< String,Object > > shoplist = mallShopCartDAO.selectCheckShopByParam( params );//获取所有的店铺
	if ( shoplist != null && shoplist.size() > 0 ) {
	    /*String shopIds = "";
	    for ( Map< String,Object > shopMap : shoplist ) {
		if ( CommonUtil.isNotEmpty( shopIds ) ) {
		    shopIds += ",";
		}
		shopIds += shopMap.get( "shop_id" ).toString();
	    }*/
	    Map< String,Object > shopcartParams = new HashMap<>();
//	    shopcartParams.put( "shopIds", shopIds.split( "," ) );
	    shopcartParams.put( "checkIds", shopcards.split( "," ) );
	    shopcartParams.put( "userId", userId );

	    List< Map< String,Object > > shopCartList = mallShopCartDAO.selectShopCartByCheckIds( shopcartParams );

	    for ( Map< String,Object > shopMaps : shoplist ) {
		Map< String,Object > shopMap = new HashMap<>();
		List< Map< String,Object > > xlist = new ArrayList<>();//重新封装数据和图片
		double price_total = 0;//对应店铺的总价钱
		double yuanjia_total = 0;
		int proNum = 0;
		double primary_price = 0;//对应店铺商品原价的总价钱
		double pro_weight = 0;
		for ( Map< String,Object > map3 : shopCartList ) {
		    if ( !map3.get( "shop_id" ).toString().equals( shopMaps.get( "shop_id" ).toString() ) ) {
			break;
		    }
		    String price = map3.get( "price" ).toString();
		    double proPriceTotal = 0;
		    int num = Integer.parseInt( map3.get( "product_num" ).toString() );

		    Object product_specificas = map3.get( "product_specificas" );
		    if ( CommonUtil.isNotEmpty( map3.get( "pro_weight" ) ) ) {
			pro_weight += CommonUtil.toDouble( map3.get( "pro_weight" ) );
		    }
		    String imageUrl = CommonUtil.toString( map3.get( "image_url" ) );

		    if ( CommonUtil.isNotEmpty( product_specificas ) ) {
			Object specifica_img_url = map3.get( "specifica_img_url" );
			if ( CommonUtil.isNotEmpty( specifica_img_url ) ) {
			    imageUrl = CommonUtil.toString( specifica_img_url );
			}
		    }
		    JSONObject specObj = new JSONObject();
		    StringBuilder specNames = new StringBuilder();
		    boolean flag = true;
		    Map< String,Object > specMap = null;
		    if ( CommonUtil.isNotEmpty( map3.get( "pro_spec_str" ) ) ) {
			JSONObject obj = JSONObject.fromObject( map3.get( "pro_spec_str" ) );
			specMap = new HashMap< String,Object >();
			if ( obj != null && obj.size() > 0 ) {
			    for ( Object key : obj.keySet() ) {
				JSONObject valObj = JSONObject.fromObject( obj.get( key ) );
				if ( CommonUtil.isNotEmpty( valObj.get( "isCheck" ) ) ) {
				    if ( valObj.get( "isCheck" ).toString().equals( "1" ) ) {
					specObj.put( key, valObj );
					if ( !specNames.toString().equals( "" ) ) {
					    specNames.append( "、" );
					} else {
					    specNames = new StringBuilder( valObj.getString( "specName" ) + "：" );
					}
					specNames.append( valObj.get( "value" ) ).append( " X " ).append( valObj.get( "num" ) ).append( " ¥" ).append( valObj.get( "price" ) );

					num = CommonUtil.toInteger( valObj.get( "num" ) );
					price = valObj.get( "price" ).toString();

					double p = Double.parseDouble( price );
					price_total = price_total + ( p * num );
					proPriceTotal = proPriceTotal + ( p * num );
					yuanjia_total += num * Double.parseDouble( map3.get( "primary_price" ).toString() );
					primary_price += Double.parseDouble( map3.get( "primary_price" ).toString() ) * num;
					proNum += num;
					flag = false;

					specMap.put( key.toString(), valObj );
				    }
				}
			    }
			}
		    }
		    if ( flag ) {
			double p = Double.parseDouble( price );
			price_total = price_total + ( p * num );
			proPriceTotal = proPriceTotal + ( p * num );
			yuanjia_total += num * Double.parseDouble( map3.get( "primary_price" ).toString() );
			primary_price += Double.parseDouble( map3.get( "primary_price" ).toString() ) * num;
			proNum += num;
		    }

		    Map< String,Object > productMap = new HashMap<>();
		    productMap.put( "product_id", map3.get( "product_id" ) );
		    productMap.put( "product_num", map3.get( "product_num" ) );
		    productMap.put( "pro_name", map3.get( "pro_name" ) );
		    productMap.put( "price", price );
		    productMap.put( "isCoupons", map3.get( "isCoupons" ) );
		    productMap.put( "is_member_discount", map3.get( "is_member_discount" ) );
		    productMap.put( "pro_type_id", map3.get( "pro_type_id" ) );
		    productMap.put( "image_url", imageUrl );
		    productMap.put( "shop_id", map3.get( "shop_id" ) );
		    productMap.put( "flowId", map3.get( "flowId" ) );
		    productMap.put( "is_integral_deduction", map3.get( "is_integral_deduction" ) );
		    productMap.put( "is_fenbi_deduction", map3.get( "is_fenbi_deduction" ) );
		    if ( CommonUtil.isNotEmpty( map3.get( "sale_member_id" ) ) ) {
			productMap.put( "sale_member_id", map3.get( "sale_member_id" ) );
		    }
		    if ( CommonUtil.isNotEmpty( map3.get( "sale_member_id" ) ) ) {
			productMap.put( "commission", map3.get( "commission" ) );
		    }
		    productMap.put( "primary_price", map3.get( "primary_price" ) );
		    if ( CommonUtil.isNotEmpty( product_specificas ) ) {
			productMap.put( "product_specificas", product_specificas );
		    }
		    if ( CommonUtil.isNotEmpty( specNames.toString() ) ) {
			productMap.put( "product_speciname", specNames.toString() );
		    }
		    if ( specObj != null && specObj.size() > 0 ) {
			productMap.put( "pro_spec_str", specObj.toString() );
		    }
		    if ( specMap != null && specMap.size() > 0 ) {
			productMap.put( "specMap", specMap );
		    }
		    String proType = map3.get( "pro_type" ).toString();
		    if ( proType.equals( "1" ) || proType.equals( "2" ) ) {
			productMap.put( "groupType", "7" );
		    }
		    productMap.put( "pro_price_total", proPriceTotal );
		    if ( CommonUtil.isNotEmpty( map3.get( "return_day" ) ) ) {
			productMap.put( "return_day", map3.get( "return_day" ) );
		    }
		    productMap.put( "index", index );
		    xlist.add( productMap );
		    index++;
		}
		DecimalFormat df = new DecimalFormat( "######0.00" );
		shopMap.put( "price_total", df.format( price_total ) );//该店铺的总价钱
		shopMap.put( "primary_price", df.format( primary_price ) );
		//shopMap.put( "product_nums", listsql1.size() );//商品数量
		shopMap.put( "proNum", proNum );//购买的商品总数（用于计算运费）
		shopMap.put( "shop_id", shopMaps.get( "shop_id" ) );//店铺id
		if ( shopList != null && shopList.size() > 0 ) {
		    for ( Map< String,Object > sMaps : shopList ) {
			if ( sMaps.get( "id" ).toString().equals( shopMaps.get( "shop_id" ).toString() ) ) {
			    shopMap.put( "wxShopId", sMaps.get( "wxShopId" ) );
			    shopMap.put( "shop_name", sMaps.get( "sto_name" ) );//店铺名称
			    break;
			}

		    }
		}
		shopMap.put( "yuanjia_total", df.format( yuanjia_total ) );
		shopMap.put( "message", xlist );//商品详情信息
		shopMap.put( "pro_weight", df.format( pro_weight ) );

		list.add( shopMap );
	    }
	}

	return list;
    }

    @Override
    public List< Map< String,Object > > getProductByIds( Map< String,Object > maps, WxPublicUsers pbUser, Member member, int userId ) throws Exception {
	List< Map< String,Object > > list = new ArrayList<>();
	Map< String,Object > shopMap = new HashMap<>();

	List< Map< String,Object > > productList = new ArrayList< Map< String,Object > >();
	double price_total = 0;//保存商品总价
	double yuanjia_total = 0;
	double primary_price = 0;//保存商品原价
	int proNum = 0;//保存商品数量
	int shopId = 0;
	double pro_weight = 0;
	int proTypeId = 0;

	if ( CommonUtil.isNotEmpty( maps ) ) {
	    if ( CommonUtil.isNotEmpty( maps.get( "product_id" ) ) ) {
		int productId = CommonUtil.toInteger( maps.get( "product_id" ) );
		MallProduct product = mallProductService.selectById( productId );
		if ( CommonUtil.isNotEmpty( product ) ) {
		    String imageUrl = "";
		    if ( CommonUtil.isNotEmpty( product.getProWeight() ) ) {
			pro_weight += CommonUtil.toDouble( product.getProWeight() );
		    }
		    shopId = product.getShopId();
		    proTypeId = CommonUtil.toInteger( product.getProTypeId() );

		    String specificaValue = "";
		    //查询规格信息
		    if ( CommonUtil.isNotEmpty( maps.get( "product_specificas" ) ) ) {
			/*String specSql = "SELECT id,specifica_value,specifica_img_url FROM t_mall_product_specifica WHERE is_delete=0 AND specifica_value_id IN(" + maps
					.get( "product_specificas" ) + ")  AND product_id=" + productId + " ORDER BY sort";
			List< Map< String,Object > > specMapList = daoUtil.queryForList( specSql );*/
			List< MallProductSpecifica > specificaList = mallProductSpecificaService
					.selectByValueIds( productId, maps.get( "product_specificas" ).toString().split( "," ) );
			if ( specificaList != null && specificaList.size() > 0 ) {
			    for ( MallProductSpecifica spec : specificaList ) {
				if ( CommonUtil.isNotEmpty( specificaValue ) ) {
				    specificaValue += " ";
				}
				specificaValue += spec.getSpecificaValue();
				if ( CommonUtil.isNotEmpty( spec.getSpecificaImgUrl() ) ) {
				    imageUrl = spec.getSpecificaImgUrl();
				}
			    }
			}
		    }
		    if ( CommonUtil.isEmpty( imageUrl ) ) {
			//查询商品图片
			Map< String,Object > params = new HashMap< String,Object >();
			params.put( "assType", 1 );
			params.put( "isMainImages", 1 );
			params.put( "assId", productId );
			List< MallImageAssociative > imageList = mallImageAssociativeService.selectByAssId( params );
			if ( imageList != null && imageList.size() > 0 ) {
			    imageUrl = imageList.get( 0 ).getImageUrl();
			}
		    }
		    double price = CommonUtil.toDouble( maps.get( "price" ) );
		    int num = CommonUtil.toInteger( maps.get( "totalnum" ) );
		    DecimalFormat df = new DecimalFormat( "######0.00" );
		    double totalPrice = CommonUtil.toDouble( df.format( price * num ) );
		    proNum += num;
		    price_total += totalPrice;
		    double primaryPrice = Double.parseDouble( maps.get( "primary_price" ).toString() );
		    primary_price += primaryPrice * num;
		    yuanjia_total += primaryPrice;

		    Map< String,Object > specMap = null;
		    boolean flag = true;
		    JSONObject specObj = new JSONObject();
		    if ( CommonUtil.isNotEmpty( maps.get( "pro_spec_str" ) ) ) {
			num = 0;
			JSONObject obj = JSONObject.fromObject( maps.get( "pro_spec_str" ) );
			specMap = new HashMap< String,Object >();
			if ( obj != null && obj.size() > 0 ) {
			    for ( Object key : obj.keySet() ) {
				JSONObject valObj = JSONObject.fromObject( obj.get( key ) );
				if ( CommonUtil.isNotEmpty( valObj.get( "isCheck" ) ) ) {
				    if ( valObj.get( "isCheck" ).toString().equals( "1" ) ) {
					specObj.put( key, valObj );
					if ( !specificaValue.equals( "" ) ) {
					    specificaValue += "、";
					} else {
					    specificaValue = valObj.getString( "specName" ) + "：";
					}
					specificaValue += valObj.get( "value" ) + " X " + valObj.get( "num" ) + " ¥" + valObj.get( "price" );

					num = CommonUtil.toInteger( valObj.get( "num" ) );
					price = CommonUtil.toDouble( valObj.get( "price" ) );

					price_total = price_total + ( price * num );
					totalPrice += ( price * num );
					yuanjia_total += num * Double.parseDouble( maps.get( "primary_price" ).toString() );
					primary_price += Double.parseDouble( maps.get( "primary_price" ).toString() ) * num;
					proNum += num;
					flag = false;

					specMap.put( key.toString(), valObj );
				    }
				}
			    }
			}
		    }
		    if ( flag ) {
			price_total = price_total + ( price * num );
			totalPrice = totalPrice + ( price * num );
			yuanjia_total += num * Double.parseDouble( maps.get( "primary_price" ).toString() );
			primary_price += Double.parseDouble( maps.get( "primary_price" ).toString() ) * num;
			proNum += num;
		    }

		    Map< String,Object > productMap = new HashMap<>();
		    productMap.putAll( maps );
		    productMap.put( "product_id", product.getId() );
		    productMap.put( "product_num", num );
		    productMap.put( "pro_name", product.getProName() );
		    productMap.put( "price", price );
		    productMap.put( "isCoupons", product.getIsCoupons() );
		    productMap.put( "is_member_discount", product.getIsMemberDiscount() );
		    productMap.put( "pro_type_id", proTypeId );
		    productMap.put( "image_url", imageUrl );
		    productMap.put( "shop_id", product.getShopId() );
		    productMap.put( "is_integral_deduction", product.getIsIntegralDeduction() );
		    productMap.put( "is_fenbi_deduction", product.getIsFenbiDeduction() );
		    if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
			if ( product.getFlowId() > 0 ) {
			    productMap.put( "flowId", product.getFlowId() );
			}
		    }
		    productMap.put( "primary_price", primary_price );
		    if ( CommonUtil.isNotEmpty( maps.get( "product_specificas" ) ) ) {
			productMap.put( "product_specificas", maps.get( "product_specificas" ) );
		    }
		    if ( CommonUtil.isNotEmpty( specificaValue ) ) {
			productMap.put( "product_speciname", specificaValue );
		    }
		    if ( specMap != null && specMap.size() > 0 ) {
			productMap.put( "specMap", specMap );
		    }
		    if ( specObj != null && specObj.size() > 0 ) {
			productMap.put( "pro_spec_str", specObj.toString() );
		    }
		    if ( specMap != null && specMap.size() > 0 ) {
			productMap.put( "specMap", specMap );
		    }
		    productMap.put( "pro_price_total", totalPrice );
		    productMap.put( "return_day", product.getReturnDay() );
		    productMap.put( "index", 1 );
		    productList.add( productMap );

		}
	    }
	}
	String shopName = "";
	MallStore mallStore = mallStoreService.selectById( shopId );
	if ( CommonUtil.isNotEmpty( mallStore.getWxShopId() ) ) {
	    WsWxShopInfo shopInfo = wxShopService.getShopById( mallStore.getWxShopId() );
	    if ( CommonUtil.isNotEmpty( shopInfo ) ) {
		shopName = shopInfo.getBusinessName();
	    }
	    shopMap.put( "wxShopId", mallStore.getWxShopId() );
	}
	if ( CommonUtil.isEmpty( shopName ) ) {
	    shopName = mallStore.getStoName();
	}

	DecimalFormat df = new DecimalFormat( "######0.00" );
	shopMap.put( "price_total", df.format( price_total ) );//该店铺的总价钱
	shopMap.put( "primary_price", df.format( primary_price ) );
	shopMap.put( "proNum", proNum );//购买的商品总数（用于计算运费）
	shopMap.put( "shop_id", shopId );//店铺id
	shopMap.put( "shop_name", shopName );//店铺名称
	shopMap.put( "yuanjia_total", df.format( yuanjia_total ) );
	shopMap.put( "message", productList );//商品详情信息
	shopMap.put( "pro_weight", df.format( pro_weight ) );

	list.add( shopMap );

	return list;
    }

    @Override
    public void getOrdersParams( HttpServletRequest request, String loginCity, int userid, List< Map< String,Object > > list, double mem_longitude, double mem_latitude,
		    Member member ) {
	int shopId = SessionUtils.getMallShopId( request );
	int toshop = mallProductService.getIsShopBySession( shopId, userid, request );
	//计算运费如下
	Map< String,Object > map = new HashMap<>();
	map.put( "province_id", loginCity );
	map.put( "orderArr", list );

	String shopIds = "";
	String wxShopIds = ",";
	int proTypeId = 0;
	int index = 1;
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > shopMap : list ) {
		if ( CommonUtil.isNotEmpty( shopMap.get( "pro_type_id" ) ) ) {
		    proTypeId = CommonUtil.toInteger( shopMap.get( "pro_type_id" ) );
		}
		shopId = CommonUtil.toInteger( shopMap.get( "shop_id" ) );//店铺id

		MallFreight freight = mallFreightDao.selectFreightByShopId( shopId );
		if ( CommonUtil.isNotEmpty( freight ) ) {
		    if ( freight.getPriceType().toString().equals( "3" ) ) {//按距离算
			request.setAttribute( "isKm", 1 );
		    }
		}
		//计算粉丝跟店铺的距离
		if ( CommonUtil.isNotEmpty( mem_longitude ) && CommonUtil.isNotEmpty( mem_latitude ) ) {
		    //获取微信门店的经度纬度
		    Map< String,Object > stores = mallStoreService.findShopByStoreId( shopId );
		    if ( CommonUtil.isNotEmpty( stores ) ) {
			if ( CommonUtil.isNotEmpty( stores.get( "stoLongitude" ) ) && CommonUtil.isNotEmpty( stores.get( "stoLatitude" ) ) ) {
			    Double raill = CommonUtil.getDistance( mem_longitude, mem_latitude, CommonUtil.toDouble( stores.get( "stoLongitude" ) ),
					    CommonUtil.toDouble( stores.get( "stoLatitude" ) ) );
			    raill = raill / 1000;
			    map.put( "juli", raill );
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( shopIds ) ) {
		    shopIds += ",";
		}
		shopIds += shopMap.get( "shop_id" ).toString();
		wxShopIds += shopMap.get( "wxShopId" ).toString() + ",";

		if ( CommonUtil.isNotEmpty( shopMap.get( "flowId" ) ) ) {
		    if ( CommonUtil.toInteger( shopMap.get( "flowId" ) ) > 0 ) {
			request.setAttribute( "isFlow", 1 );
			WsBusFlowInfo flow = fenBiFlowService.getFlowInfoById( CommonUtil.toInteger( shopMap.get( "flowId" ) ) );
			if ( CommonUtil.isNotEmpty( flow ) ) {
			    request.setAttribute( "flowType", flow.getType() );
			}
		    }
		}
	    }
	}

	map.put( "toshop", toshop );
	map.put( "proTypeId", proTypeId );
	Map< Integer,Object > priceMap = mallFreightService.getFreightMoney( map );//计算运费
	request.setAttribute( "priceMap", priceMap );

	if ( CommonUtil.isNotEmpty( shopIds ) ) {
	    boolean isJuli = mallOrderService.isJuliByFreight( shopIds );
	    if ( isJuli ) {
		SessionUtils.setSession( shopIds, request, "isJuliFreight" );
		request.setAttribute( "isJuliFreight", 1 );
	    }
	}

	if ( wxShopIds.length() >= 2 && CommonUtil.isNotEmpty( member ) ) {
	    wxShopIds = wxShopIds.substring( 1, wxShopIds.length() - 1 );
	    Map< String,Object > cardMap = memberService.findCardAndShopIdsByMembeId( member.getId(), wxShopIds );

	    for ( Map< String,Object > shopMap : list ) {
		int wxShopId = CommonUtil.toInteger( shopMap.get( "wxShopId" ) );
		if ( cardMap.containsKey( "cardList" + wxShopId ) ) {
		    shopMap.put( "coupon", cardMap.get( "cardList" + wxShopId ) );
		}
		if ( cardMap.containsKey( "duofenCards" + wxShopId ) ) {
		    shopMap.put( "duofenCoupon", cardMap.get( "duofenCards" + wxShopId ) );
		}

	    }
	    request.setAttribute( "cardMap", cardMap );
	    System.out.println( "card = " + com.alibaba.fastjson.JSONObject.toJSONString( cardMap ) );
	}

    }
}
