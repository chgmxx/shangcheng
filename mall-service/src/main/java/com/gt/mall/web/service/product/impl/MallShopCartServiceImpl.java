package com.gt.mall.web.service.product.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.bean.result.shop.WsWxShopInfo;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.basic.MallImageAssociativeService;
import com.gt.mall.web.service.product.MallProductService;
import com.gt.mall.web.service.product.MallProductSpecificaService;
import com.gt.mall.web.service.product.MallShopCartService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private MallStoreDAO                mallStoreDAO;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallShopCartDAO             mallShopCartDAO;
//    @Autowired
//    private WxShopService               wxShopService;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;

    @Override
    public List< Map< String,Object > > getProductByShopCart( String shopcards, WxPublicUsers pbUser, Member member, int userId ) {
	double discount = 1;//商品折扣
	if ( CommonUtil.isNotEmpty( member ) ) {
	    discount = mallProductService.getMemberDiscount( "1", member );//获取会员折扣
	}
	List< Map< String,Object > > list = new ArrayList<>();
	/*String sql = "SELECT distinct(a.shop_id),b.sto_name FROM t_mall_shop_cart a LEFT JOIN t_mall_store b ON a.shop_id=b.id "
			+ "left join t_wx_shop s on s.id=b.wx_shop_id WHERE a.id IN(" + shopcards + ")  and s.`status` != -1 and b.is_delete=0";*/
	Map< String,Object > params = new HashMap<>();
	params.put( "cartIds", shopcards.split( "," ) );
	params.put( "busUserId", member.getBusid() );
	//todo 传门店id进去
	int proTypeId = 0;
	List< Map< String,Object > > shoplist = mallShopCartDAO.selectCheckShopByParam( params );//获取所有的店铺
	if ( shoplist != null && shoplist.size() > 0 ) {
	    for ( Map< String,Object > shopMaps : shoplist ) {
		Map< String,Object > shopMap = new HashMap<>();
		/*String sql1 = "SELECT a.id,a.product_id,a.shop_id,a.product_specificas,a.product_num,a.product_speciname,a.price,a.primary_price,a.user_type,a.pro_spec_str,a.pro_type,a.sale_member_id as saleMemberId,a.commission,"
				+ " e.image_url,d.specifica_img_url,"
				+ " b.return_day,b.pro_name,b.is_coupons as isCoupons,b.is_member_discount,b.pro_type_id,b.is_integral_deduction ,b.is_fenbi_deduction,b.pro_weight,b.flow_id as flowId "
				+ " FROM t_mall_shop_cart a "
				+ " LEFT JOIN t_mall_product b ON a.product_id=b.id "
				+ " LEFT JOIN t_mall_product_inventory c ON a.product_specificas=c.specifica_ids  AND c.`product_id`=a.`product_id` "
				+ " LEFT JOIN  t_mall_product_specifica d ON c.specifica_img_id=d.id  AND d.`product_id`=a.`product_id` "
				+ " LEFT JOIN (SELECT image_url,ass_id FROM t_mall_image_associative WHERE ass_type=1 AND is_main_images=1 AND is_delete=0) e ON a.product_id=e.ass_id "
				+ " WHERE a.shop_id =" + shopMaps.get( "shop_id" ) + " AND a.id IN (" + shopcards + ")";*/
		Map< String,Object > shopParams = new HashMap<>();
		shopParams.put( "shopId", shopMaps.get( "shop_id" ) );
		shopParams.put( "checkIds", shopcards.split( "," ) );
		List< Map< String,Object > > listsql1 = mallShopCartDAO.selectShopCartByMemberId( shopParams );
		List< Map< String,Object > > xlist = new ArrayList< Map< String,Object > >();//重新封装数据和图片
		double price_total = 0;//对应店铺的总价钱
		double yuanjia_total = 0;
		int proNum = 0;
		double primary_price = 0;//对应店铺商品原价的总价钱
		double pro_weight = 0;
		for ( int j = 0; j < listsql1.size(); j++ ) {
		    Map< String,Object > map3 = listsql1.get( j );
		    proTypeId = CommonUtil.toInteger( map3.get( "pro_type_id" ) );
		    String price = map3.get( "price" ).toString();
		    int proCountNum = 0;
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
			Map< String,Object > invPriceMap = mallProductService
					.getProInvIdBySpecId( product_specificas.toString(), CommonUtil.toInteger( map3.get( "product_id" ) ) );
			if ( CommonUtil.isNotEmpty( invPriceMap ) ) {
			    if ( CommonUtil.isNotEmpty( invPriceMap.get( "specifica_img_url" ) ) ) {
				imageUrl = CommonUtil.toString( invPriceMap.get( "specifica_img_url" ) );
			    }
			}
		    }
		    JSONObject specObj = new JSONObject();
		    String specNames = "";
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
					if ( !specNames.equals( "" ) ) {
					    specNames += "、";
					} else {
					    specNames = valObj.getString( "specName" ) + "：";
					}
					specNames += valObj.get( "value" ) + " X " + valObj.get( "num" ) + " ¥" + valObj.get( "price" );

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
					proCountNum += num;
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
			proCountNum += num;
		    }

		    Map< String,Object > productMap = new HashMap< String,Object >();
		    productMap.put( "product_id", map3.get( "product_id" ) );
		    productMap.put( "product_num", map3.get( "product_num" ) );
		    productMap.put( "pro_name", map3.get( "pro_name" ) );
		    productMap.put( "isCoupons", map3.get( "isCoupons" ) );
		    productMap.put( "is_member_discount", map3.get( "is_member_discount" ) );
		    productMap.put( "pro_type_id", map3.get( "pro_type_id" ) );
		    productMap.put( "flowId", map3.get( "flowId" ) );
		    productMap.put( "shop_id", map3.get( "shop_id" ) );
		    if ( CommonUtil.isNotEmpty( map3.get( "sale_member_id" ) ) ) {
			productMap.put( "sale_member_id", map3.get( "sale_member_id" ) );
		    }
		    if ( CommonUtil.isNotEmpty( map3.get( "sale_member_id" ) ) ) {
			productMap.put( "commission", map3.get( "commission" ) );
		    }
		    productMap.put( "image_url", imageUrl );
		    productMap.put( "primary_price", map3.get( "primary_price" ) );
		    productMap.put( "price", price );
		    if ( CommonUtil.isNotEmpty( product_specificas ) ) {
			productMap.put( "product_specificas", product_specificas );
		    }
		    if ( CommonUtil.isNotEmpty( specNames ) ) {
			productMap.put( "product_speciname", specNames );
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
		    if ( map3.get( "is_member_discount" ).toString().equals( "1" ) ) {
			productMap.put( "discount", discount );
		    } else {
			productMap.put( "discount", 1 );
		    }
		    xlist.add( productMap );
		}
		DecimalFormat df = new DecimalFormat( "######0.00" );
		shopMap.put( "price_total", df.format( price_total ) );//该店铺的总价钱
		shopMap.put( "primary_price", df.format( primary_price ) );
		shopMap.put( "product_nums", listsql1.size() );//商品数量
		shopMap.put( "proNum", proNum );//购买的商品总数（用于计算运费）
		shopMap.put( "shop_id", shopMaps.get( "shop_id" ) );//店铺id
		shopMap.put( "shop_name", shopMaps.get( "sto_name" ) );//店铺名称
		shopMap.put( "yuanjia_total", df.format( yuanjia_total ) );
		shopMap.put( "message", xlist );//商品详情信息
		shopMap.put( "pro_weight", df.format( pro_weight ) );

		/*if ( CommonUtil.isNotEmpty( member ) && proTypeId == 0 ) {
		    //优惠劵
		    MallStore store = mallStoreDAO.selectById( Integer.parseInt( shopMaps.get( "shop_id" ).toString() ) );
		    if ( CommonUtil.isNotEmpty( pbUser ) ) {
		    	//todo wxCardService.findWxCardByShopId
			List< Map< String,Object > > coupon = wxCardService.findWxCardByShopId( store.getWxShopId(), pbUser, member );
			if ( null != coupon && coupon.size() > 0 ) {
			    shopMap.put( "coupon", coupon );
			}
		    }
		    //查询多粉优惠券
		    //todo duofenCardService.findDuofenCardByMemberId
		    List< Map< String,Object > > duofenCoupon = duofenCardService.findDuofenCardByMemberId( member.getId(), store.getWxShopId() );
		    if ( duofenCoupon != null && duofenCoupon.size() > 0 ) {
			shopMap.put( "duofenCoupon", duofenCoupon );
		    }
		}*/
		list.add( shopMap );
	    }
	}

	return list;
    }

    @Override
    public List< Map< String,Object > > getProductByIds( Map< String,Object > maps, WxPublicUsers pbUser, Member member, int userId ) throws Exception {
	double discount = 1;//商品折扣
	if ( CommonUtil.isNotEmpty( member ) ) {
	    discount = mallProductService.getMemberDiscount( "1", member );//获取会员折扣
	}
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	Map< String,Object > shopMap = new HashMap< String,Object >();

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
		    if ( CommonUtil.isNotEmpty( maps.get( "pro_spec_str" ) ) ) {
			num = 0;
			JSONObject obj = JSONObject.fromObject( maps.get( "pro_spec_str" ) );
			specMap = new HashMap< String,Object >();
			if ( obj != null && obj.size() > 0 ) {
			    for ( Object key : obj.keySet() ) {
				JSONObject valObj = JSONObject.fromObject( obj.get( key ) );
				if ( CommonUtil.isNotEmpty( valObj.get( "isCheck" ) ) ) {
				    if ( valObj.get( "isCheck" ).toString().equals( "1" ) ) {
					//specObj.put(key, valObj);
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

		    Map< String,Object > productMap = new HashMap< String,Object >();
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
		    if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
			if ( product.getFlowId() > 0 ) {
			    productMap.put( "flowId", product.getFlowId() );
			}
		    }
		    if ( CommonUtil.isNotEmpty( specificaValue ) ) {
			productMap.put( "product_speciname", specificaValue );
		    }
		    if ( specMap != null && specMap.size() > 0 ) {
			productMap.put( "specMap", specMap );
		    }
		    productMap.put( "pro_price_total", totalPrice );
		    productMap.put( "return_day", product.getReturnDay() );
		    if ( product.getIsMemberDiscount().toString().equals( "1" ) ) {
			productMap.put( "discount", discount );
		    } else {
			productMap.put( "discount", 1 );
		    }
		    productList.add( productMap );

		}
	    }
	}
	String shopName = "";
	MallStore mallStore = mallStoreDAO.selectById( shopId );
	if ( CommonUtil.isNotEmpty( mallStore.getWxShopId() ) ) {
	    //TODO  wxShopService.getShopById()
	    WsWxShopInfo shopInfo =null;// wxShopService.getShopById( mallStore.getWxShopId() );
	    if ( CommonUtil.isNotEmpty( shopInfo ) ) {
		shopName = shopInfo.getBusinessName();
	    }
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

	/*if ( CommonUtil.isNotEmpty( member ) && proTypeId == 0 ) {
	    //优惠劵
	    if ( CommonUtil.isNotEmpty( pbUser ) ) {
	    	//todo wxCardService.findWxCardByShopId
		List< Map< String,Object > > coupon = wxCardService.findWxCardByShopId( mallStore.getWxShopId(), pbUser, member );
		if ( null != coupon && coupon.size() > 0 ) {
		    shopMap.put( "coupon", coupon );
		}
	    }
	    //查询多粉优惠券
	    //todo duofenCardService.findDuofenCardByMemberId
	    List< Map< String,Object > > duofenCoupon = duofenCardService.findDuofenCardByMemberId( member.getId(), mallStore.getWxShopId() );
	    if ( duofenCoupon != null && duofenCoupon.size() > 0 ) {
		shopMap.put( "duofenCoupon", duofenCoupon );
	    }
	}*/

	list.add( shopMap );

	return list;
    }
}
