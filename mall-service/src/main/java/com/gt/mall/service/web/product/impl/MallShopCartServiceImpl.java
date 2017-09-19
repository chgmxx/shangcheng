package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.freight.MallFreightDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.product.MallShopCartService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.SessionUtils;
import com.gt.union.api.entity.param.UnionCardDiscountParam;
import com.gt.union.api.entity.result.UnionDiscountResult;
import com.gt.util.entity.param.fenbiFlow.BusFlowInfo;
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
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallShopCartDAO             mallShopCartDAO;
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
    @Autowired
    private UnionCardService            unionCardService;

    @Override
    public List< Map< String,Object > > getProductByShopCart( String shopcards, WxPublicUsers pbUser, Member member, int userId, List< Map< String,Object > > shopList ) {
	List< Map< String,Object > > list = new ArrayList<>();

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
			continue;
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
		    if ( CommonUtil.isNotEmpty( map3.get( "product_speciname" ) ) ) {
			specNames.append( map3.get( "product_speciname" ) );
		    }
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
		if ( xlist == null || xlist.size() == 0 ) {
		    continue;
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
    public List< Map< String,Object > > getProductByIds( Map< String,Object > maps, WxPublicUsers pbUser, Member member, int userId, List< Map< String,Object > > shopList )
		    throws Exception {
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
	    if ( CommonUtil.isNotEmpty( maps.get( "productId" ) ) ) {
		int productId = CommonUtil.toInteger( maps.get( "productId" ) );
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
		    if ( CommonUtil.isNotEmpty( maps.get( "productSpecificas" ) ) ) {
			/*String specSql = "SELECT id,specifica_value,specifica_img_url FROM t_mall_product_specifica WHERE is_delete=0 AND specifica_value_id IN(" + maps
					.get( "productSpecificas" ) + ")  AND product_id=" + productId + " ORDER BY sort";
			List< Map< String,Object > > specMapList = daoUtil.queryForList( specSql );*/
			List< MallProductSpecifica > specificaList = mallProductSpecificaService
					.selectByValueIds( productId, maps.get( "productSpecificas" ).toString().split( "," ) );
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
		    double price = CommonUtil.toDouble( maps.get( "detProPrice" ) );
		    int num = CommonUtil.toInteger( maps.get( "detProNum" ) );
		    double totalPrice = 0;
		    double primaryPrice = Double.parseDouble( maps.get( "detPrivivilege" ).toString() );

		    Map< String,Object > specMap = null;
		    boolean flag = true;
		    JSONObject specObj = new JSONObject();
		    if ( CommonUtil.isNotEmpty( maps.get( "proSpecStr" ) ) ) {
			num = 0;
			JSONObject obj = JSONObject.fromObject( maps.get( "proSpecStr" ) );
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
					yuanjia_total += num * Double.parseDouble( maps.get( "detPrivivilege" ).toString() );
					primary_price += Double.parseDouble( maps.get( "detPrivivilege" ).toString() ) * num;
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
			yuanjia_total += num * primaryPrice;
			primary_price += primaryPrice * num;
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
		    if ( CommonUtil.isNotEmpty( maps.get( "productSpecificas" ) ) ) {
			productMap.put( "product_specificas", maps.get( "productSpecificas" ) );
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
	if ( shopList != null && shopList.size() > 0 ) {
	    for ( Map< String,Object > storeMap : shopList ) {
		if ( storeMap.get( "id" ).toString().equals( CommonUtil.toString( shopId ) ) ) {
		    shopMap.put( "wxShopId", storeMap.get( "wxShopId" ) );
		    shopMap.put( "shop_name", storeMap.get( "sto_name" ) );//店铺名称
		    break;
		}
	    }
	}

	DecimalFormat df = new DecimalFormat( "######0.00" );
	shopMap.put( "price_total", df.format( price_total ) );//该店铺的总价钱
	shopMap.put( "primary_price", df.format( primary_price ) );
	shopMap.put( "proNum", proNum );//购买的商品总数（用于计算运费）
	shopMap.put( "shop_id", shopId );//店铺id
	shopMap.put( "yuanjia_total", df.format( yuanjia_total ) );
	shopMap.put( "message", productList );//商品详情信息
	shopMap.put( "pro_weight", df.format( pro_weight ) );
	if ( maps.containsKey( "groupBuyId" ) ) {
	    shopMap.put( "groupBuyId", maps.get( "groupBuyId" ) );
	}
	if ( maps.containsKey( "pJoinId" ) ) {
	    shopMap.put( "pJoinId", maps.get( "pJoinId" ) );
	}
	if ( maps.containsKey( "groupType" ) ) {
	    shopMap.put( "groupType", maps.get( "groupType" ) );
	}

	list.add( shopMap );

	return list;
    }

    @Override
    public void getOrdersParams( HttpServletRequest request, String loginCity, int userid, List< Map< String,Object > > list, double mem_longitude, double mem_latitude,
		    Member member, List< Map< String,Object > > shopList ) {
	int shopId = SessionUtils.getMallShopId( request );
	int toshop = mallProductService.getIsShopBySession( shopId, userid, request );
	//计算运费如下
	Map< String,Object > map = new HashMap<>();
	map.put( "province_id", loginCity );
	map.put( "orderArr", list );

	String shopIds = "";
	String wxShopIds = ",";
	int proTypeId = 0;
	int groupType = 0;
	int index = 1;
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > shopMap : list ) {
		if ( CommonUtil.isNotEmpty( shopMap.get( "pro_type_id" ) ) ) {
		    proTypeId = CommonUtil.toInteger( shopMap.get( "pro_type_id" ) );
		}
		if ( CommonUtil.isNotEmpty( shopMap.get( "groupType" ) ) ) {
		    groupType = CommonUtil.toInteger( shopMap.get( "groupType" ) );
		}
		shopId = CommonUtil.toInteger( shopMap.get( "shop_id" ) );//店铺id

		if ( shopId > 0 ) {
		    MallFreight freight = mallFreightDao.selectFreightByShopId( shopId );
		    if ( CommonUtil.isNotEmpty( freight ) ) {
			if ( freight.getPriceType().toString().equals( "3" ) ) {//按距离算
			    request.setAttribute( "isKm", 1 );
			}
		    }
		}
		//计算粉丝跟店铺的距离
		if ( CommonUtil.isNotEmpty( mem_longitude ) && CommonUtil.isNotEmpty( mem_latitude ) ) {
		    if ( shopList != null && shopList.size() > 0 ) {
			for ( Map< String,Object > storeMap : shopList ) {
			    if ( storeMap.get( "id" ).toString().equals( CommonUtil.toString( shopId ) ) ) {
				if ( CommonUtil.isNotEmpty( storeMap.get( "stoLongitude" ) ) && CommonUtil.isNotEmpty( storeMap.get( "stoLatitude" ) ) ) {
				    Double raill = CommonUtil.getDistance( mem_longitude, mem_latitude, CommonUtil.toDouble( storeMap.get( "stoLongitude" ) ),
						    CommonUtil.toDouble( storeMap.get( "stoLatitude" ) ) );
				    raill = raill / 1000;
				    map.put( "juli", raill );
				    break;
				}
			    }
			}
		    }
		}
		if ( CommonUtil.isNotEmpty( shopIds ) ) {
		    shopIds += ",";
		}
		shopIds += shopMap.get( "shop_id" ).toString();
		wxShopIds += shopMap.get( "wxShopId" ).toString() + ",";

		if(CommonUtil.isNotEmpty( shopMap.get( "message" ) )){
		    JSONArray arr = JSONArray.parseArray( JSON.toJSONString( shopMap.get( "message" ) ) );
		    for ( Object o : arr ) {
			com.alibaba.fastjson.JSONObject obj = com.alibaba.fastjson.JSONObject.parseObject( o.toString() );
			if ( CommonUtil.isNotEmpty( obj.get( "flowId" ) ) ) {
			    if ( CommonUtil.toInteger( obj.get( "flowId" ) ) > 0 ) {
				request.setAttribute( "isFlow", 1 );
				BusFlowInfo flow = fenBiFlowService.getFlowInfoById( CommonUtil.toInteger( obj.get( "flowId" ) ) );
				if ( CommonUtil.isNotEmpty( flow ) ) {
				    request.setAttribute( "flowType", flow.getType() );
				}
			    }
			}
		    }

		}
	    }
	}

	map.put( "toshop", toshop );
	map.put( "proTypeId", proTypeId );
	Map< String,Object > priceMap = mallFreightService.getFreightMoney( map );//计算运费
	request.setAttribute( "priceMap", com.alibaba.fastjson.JSONObject.toJSON( priceMap ) );

	if ( CommonUtil.isNotEmpty( shopIds ) ) {
	    boolean isJuli = mallOrderService.isJuliByFreight( shopIds );
	    if ( isJuli ) {
		SessionUtils.setSession( shopIds, request, "isJuliFreight" );
		request.setAttribute( "isJuliFreight", 1 );
	    }
	}
	if ( CommonUtil.isNotEmpty( member ) && groupType == 0 ) {
	    if ( wxShopIds.length() >= 2 ) {
		wxShopIds = wxShopIds.substring( 1, wxShopIds.length() - 1 );
		Map cardMap = memberService.findCardAndShopIdsByMembeId( member.getId(), wxShopIds );

		for ( Map< String,Object > shopMap : list ) {
		    int wxShopId = CommonUtil.toInteger( shopMap.get( "wxShopId" ) );
		    if ( cardMap != null ) {
			if ( cardMap.containsKey( "cardList" + wxShopId ) ) {
			    shopMap.put( "coupon", cardMap.get( "cardList" + wxShopId ) );
			}
			if ( cardMap.containsKey( "duofenCards" + wxShopId ) ) {
			    shopMap.put( "duofenCoupon", cardMap.get( "duofenCards" + wxShopId ) );
			}
		    }
		}
		request.setAttribute( "cardMap", cardMap );
	    }

	    //查询商家是否已经开启了商家联盟
	    UnionCardDiscountParam param = new UnionCardDiscountParam();
	    param.setBusId( member.getBusid() );
	    param.setMemberId( member.getId() );
	    param.setPhone( member.getPhone() );
	    UnionDiscountResult unionDiscountResult = unionCardService.consumeUnionDiscount( param );
	    if ( CommonUtil.isNotEmpty( unionDiscountResult ) ) {
		if ( unionDiscountResult.getCode() != -1 ) {
		    request.setAttribute( "unionMap", unionDiscountResult );
		}
	    }
	}

    }
}
