package com.gt.mall.service.web.applet.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.api.bean.session.Member;
import com.gt.mall.dao.applet.MallAppletImageDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.result.applet.AppletShopCartResult;
import com.gt.mall.result.applet.param.AppletShopCartProductDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.applet.MallProductAppletService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.result.shop.WsShopPhoto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序图片表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductAppletServiceImpl extends BaseServiceImpl< MallAppletImageDAO,MallAppletImage > implements MallProductAppletService {

    @Autowired
    private MallShopCartDAO shopCartDAO;
    @Autowired
    private MallPifaDAO     pifaDAO;

    @Autowired
    private MallProductService          productService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private WxShopService               wxShopService;
    @Autowired
    private MallStoreService            mallStoreService;

    @Override
    public Map< String,Object > shoppingcare( Map< String,Object > params, HttpServletRequest request ) {
        Map< String,Object > resultMap = new HashMap< String,Object >();
        int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
        Member member = memberService.findMemberById( memberId, null );
        List< Integer > memberList = memberService.findMemberListByIds( memberId );//查询会员信息
        params.put( "memberList", memberList );
        double discount = productService.getMemberDiscount( "1", member );//获取会员折扣
        //	List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( member.getBusid() );
        List< Map< String,Object > > list = shopCartDAO.selectAppletByParams( params );
    /*int type = 0;
        if(CommonUtil.isNotEmpty(params.get("type"))){
			type = CommonUtil.toInteger(params.get("type"));
		}*/
        List< Map< String,Object > > storeList = mallStoreService.findShopByUserId( member.getBusid(), request );
        List< AppletShopCartProductDTO > shopList = new ArrayList< AppletShopCartProductDTO >();//商品列表
        List< AppletShopCartResult > shopCartList = new ArrayList< AppletShopCartResult >();//店铺列表

        //保存失效商品
        List< AppletShopCartProductDTO > sxCartList = new ArrayList< AppletShopCartProductDTO >();//商品列表
        List< AppletShopCartResult > sxshopCartList = new ArrayList< AppletShopCartResult >();//店铺列表
        int shopId = 0;
        AppletShopCartResult shopMap = new AppletShopCartResult();
        AppletShopCartResult sxShopMap = new AppletShopCartResult();
	/*Map< String,Object > productMap = new HashMap< String,Object >();*/
        if ( list != null && list.size() > 0 ) {
            int j = 0;
            for ( Map< String,Object > map : list ) {
                AppletShopCartProductDTO cartMap = new AppletShopCartProductDTO();
                String proId = map.get( "product_id" ).toString();
                int productNum = CommonUtil.toInteger( map.get( "product_num" ) );//商品数量
                int stockNum = CommonUtil.toInteger( map.get( "pro_stock_num" ) );//库存数量
                double price = CommonUtil.toDouble( map.get( "price" ) );//商品价
                int pro_type = 0;
                if ( CommonUtil.isNotEmpty( map.get( "pro_type" ) ) ) {
                    pro_type = CommonUtil.toInteger( map.get( "pro_type" ) );
                }
                DecimalFormat df = new DecimalFormat( "######0.00" );
                MallShopCart cart = new MallShopCart();
                cart.setId( CommonUtil.toInteger( map.get( "id" ) ) );
                cartMap.setId( CommonUtil.toInteger( map.get( "id" ) ) );
                cartMap.setProduct_id( CommonUtil.isEmpty( map.get( "proId" ) ) ? null : CommonUtil.toInteger( map.get( "proId" ) ) );
                cartMap.setProduct_name( map.get( "pro_name" ).toString() );
		/*String jump_url = PropertiesUtil.getWebHomeUrl()+"/mallApplet/79B4DE7C/phoneProduct.do?memberId="+memberId+"&shopId="+map.get("shop_id")+"&productId="+proId;
		int is_return = 1;
		if(CommonUtil.isNotEmpty(map.get("pro_type_id")) && CommonUtil.isNotEmpty(map.get("member_type"))){
			if(CommonUtil.toInteger(map.get("pro_type_id")) == 2 && CommonUtil.toInteger(map.get("member_type")) > 0){
				jump_url = "";
				is_return = 0;
				cartMap.put("return_msg", "会员卡购买正在开发。。。请耐心等待");
			}
		}
		cartMap.put("jump_url", jump_url);
		cartMap.put("is_return", is_return);*/
                cartMap.setPro_type_id( CommonUtil.toInteger( map.get( "pro_type_id" ) ) );
                cartMap.setImage_url( PropertiesUtil.getResourceUrl() + map.get( "image_url" ) );
                if ( map.containsKey( "product_speciname" ) ) {
                    cartMap.setProduct_speciname( map.get( "product_speciname" ) + "" );
                }
                cartMap.setPro_stock_num( CommonUtil.toInteger( map.get( "pro_stock_num" ) ) );
                cartMap.setProduct_num( CommonUtil.toInteger( map.get( "product_num" ) ) );
                cartMap.setShop_id( shopId );
                String msg = "";
                int code = 1;
                //判断限购和商品是否正在售卖
                Map< String,Object > xgMap = productService.isshoppingCart( map, productNum, storeList );
                if ( xgMap.get( "code" ).toString().equals( "1" ) ) {
                    if ( xgMap.containsKey( "product_num" ) ) {
                        cartMap.setProduct_num( CommonUtil.toInteger( xgMap.get( "product_num" ) ) );
                    }
                    if ( xgMap.containsKey( "maxBuy" ) ) {
                        cartMap.setMaxBuy( CommonUtil.toInteger( xgMap.get( "maxBuy" ) ) );
                    }
		    /*if ( xgMap.containsKey( "productMap" ) ) {
			productMap.putAll( JSONObject.parseObject( xgMap.get( "productMap" ).toString() ) );
		    }*/
                    if ( xgMap.containsKey( "sto_name" ) ) {
                        cartMap.setSto_name( xgMap.get( "sto_name" ).toString() );
                    }
                } else {
                    code = CommonUtil.toInteger( xgMap.get( "code" ) );
                    msg = CommonUtil.toString( xgMap.get( "msg" ) );
                }
                String proSpec = CommonUtil.isEmpty( map.get( "product_specificas" ) ) ? "" : CommonUtil.toString( map.get( "product_specificas" ) );
                if ( map.get( "isSpec" ).toString().equals( "1" ) && code == 1 && pro_type == 0 ) {//商品存在规格
                    if ( proSpec == null || proSpec.equals( "" ) ) {
                        code = 0;
                        msg = "商品存在规格";
                    } else {
                        //判断商品规格是否异常
                        Map< String,Object > invPriceMap = productService.getProInvIdBySpecId( proSpec, CommonUtil.toInteger( proId ) );
                        boolean flag = false;
                        if ( CommonUtil.isNotEmpty( invPriceMap ) ) {
                            cartMap.setPro_stock_num( CommonUtil.toInteger( invPriceMap.get( "inv_num" ) ) );
                            if ( CommonUtil.isNotEmpty( invPriceMap.get( "specifica_img_url" ) ) ) {
                                cartMap.setImage_url( PropertiesUtil.getResourceUrl() + invPriceMap.get( "specifica_img_url" ) );
                            }
                            if ( CommonUtil.isNotEmpty( invPriceMap.get( "specifica_values" ) ) ) {
                                String speciname = invPriceMap.get( "specifica_values" ).toString();
                                speciname = speciname.replaceAll( ",", "/" );
                                cartMap.setProduct_speciname( speciname );
                            }
                            double invPrice = CommonUtil.toDouble( invPriceMap.get( "inv_price" ) );
                            double yhPrice = invPrice;
                            if ( CommonUtil.isNotEmpty( map.get( "is_member_discount" ) ) ) {
                                if ( map.get( "is_member_discount" ).toString().equals( "1" ) ) {//开启会员折扣
                                    yhPrice = invPrice * discount;
                                }
                            }
                            cartMap.setPrice( df.format( invPrice ) );
                            if(Double.compare( price, invPrice ) != 0 || Double.compare( price, yhPrice ) != 0 ){// 后面的写法findbug报错  if ( price != invPrice || yhPrice != price ) {//同步价格
                                yhPrice = CommonUtil.toDouble( df.format( yhPrice ) );
                                if ( yhPrice <= 0 ) {
                                    yhPrice = 0.01;
                                }
                                cart.setPrice( BigDecimal.valueOf( yhPrice ) );
                                cart.setPrimaryPrice( BigDecimal.valueOf( invPrice ) );
                                shopCartDAO.updateById( cart );
                                cartMap.setHyprice( df.format( yhPrice ) );
                                //								cartMap.put("primary_price",df.format(invPrice));
                                cartMap.setPrice( df.format( yhPrice ) );
                            }
                            if ( CommonUtil.isEmpty( invPriceMap.get( "inv_num" ) ) ) {
                                flag = true;
                            } else {
                                int invNum = CommonUtil.toInteger( invPriceMap.get( "inv_num" ) );
                                if ( invNum <= 0 ) {
                                    flag = true;
                                } else if ( productNum > invNum ) {
                                    flag = true;
                                }
                            }

                        } else {
                            code = 0;
                            msg = "商品规格不存在";
                        }
                        if ( flag ) {
                            code = 0;
                            msg = "商品已售罄";
                        }

                    }
                } else if ( map.get( "isSpec" ).toString().equals( "0" ) && code == 1 && pro_type == 0 ) {//商品部存在规格
                    if ( proSpec != null && !proSpec.equals( "" ) ) {
                        code = 0;
                        msg = "商品不存在规格";
                    }
                    double proPrice = CommonUtil.toDouble( map.get( "pro_price" ) );
                    cartMap.setPrice( df.format( proPrice ) );
                    double yhPrice = proPrice;
                    if ( CommonUtil.isNotEmpty( map.get( "is_member_discount" ) ) ) {
                        if ( map.get( "is_member_discount" ).toString().equals( "1" ) ) {//开启会员折扣
                            yhPrice = proPrice * discount;
                        }
                    }

                    if ( Double.compare( price, proPrice ) != 0 || Double.compare( price, yhPrice ) != 0 ) {//findbug 会报错if ( price != proPrice || yhPrice != price ) {//同步价格
                        yhPrice = CommonUtil.toDouble( df.format( yhPrice ) );
                        if ( yhPrice <= 0 ) {
                            yhPrice = 0.01;
                        }
			/*boolean isUpPro = true;
			if(CommonUtil.isNotEmpty(map.get("sale_member_id"))){
				if(CommonUtil.toInteger(map.get("sale_member_id")) > 0){
					isUpPro = false;
				}
			}
			if(isUpPro){*/
                        cart.setPrice( BigDecimal.valueOf( yhPrice ) );
                        cart.setPrimaryPrice( BigDecimal.valueOf( proPrice ) );
                        shopCartDAO.updateById( cart );
                        cartMap.setPrice( df.format( yhPrice ) );
                        cartMap.setHyprice( df.format( yhPrice ) );
                        // cartMap.put("primary_price",df.format(proPrice));
			/*}*/

			/*code = 0;
			msg = "商品价格异常";*/
                    }
                    if ( productNum > stockNum ) {
                        code = 0;
                        msg = "商品已售罄";
                    }
                }
                if ( pro_type == 1 || pro_type == 2 ) {//批发商品
                    cartMap.setPfType( 1 );
                    List< MallPifa > pfList = pifaDAO.selectStartPiFaByProductId( map );
                    if ( pfList == null || pfList.size() == 0 ) {
                        code = 0;
                        msg = "批发商品已结束或已删除";
                    } else {
                        MallPifa pifa = pfList.get( 0 );
                        if ( !pifa.getPfType().toString().equals( map.get( "pro_type" ).toString() ) ) {
                            code = 0;
                            msg = "商家已更改批发类型";
                        }
                    }
                }
                //批发
		/*if(CommonUtil.isNotEmpty(map.get("pro_spec_str"))){
			Map<String, Object> specStr = JSONObject.fromObject(map.get("pro_spec_str"));
			Set<String> set = specStr.keySet();
			if(set != null && set.size() > 0){
				for (String str : set) {
					if(CommonUtil.isNotEmpty(specStr.get(str))){
						Map<String, Object> valMap = JSONObject.fromObject(specStr.get(str));
						Map<String, Object> invPrice = mallProductService.getProInvIdBySpecId(str, CommonUtil.toInteger(proId));
						if(CommonUtil.isNotEmpty(invPrice)){
							valMap.put("invNum", invPrice.get("inv_num"));
							specStr.put(str, valMap);
						}
					}
				}
			}
			map.put("proSpecStr", specStr);
		}*/
                cartMap.setMsg( msg );
                cartMap.setCode( code );

                if ( code == 1 ) {
                    shopList.add( cartMap );
                } else {
                    sxCartList.add( cartMap );
                }
                if ( !map.get( "shop_id" ).toString().equals( CommonUtil.toString( shopId ) ) || j == 0 ) {
                    shopId = CommonUtil.toInteger( map.get( "shop_id" ) );
                }
                String next_shop_id = "";
                if ( j + 1 < list.size() ) {
                    Map< String,Object > nextMap = list.get( j + 1 );
                    next_shop_id = nextMap.get( "shop_id" ).toString();
                }
                if ( ( !next_shop_id.equals( CommonUtil.toString( shopId ) ) && CommonUtil.isNotEmpty( next_shop_id ) ) || j + 1 == list.size() ) {
                    if ( null != shopList && shopList.size() > 0 ) {
                        shopMap = getShopMaps( shopMap, map, pro_type, storeList );
                        shopMap.setProList( shopList );
                        shopCartList.add( shopMap );

                        shopList = new ArrayList<>();
                        shopMap = new AppletShopCartResult();
                    }
                    if ( null != sxCartList && sxCartList.size() > 0 ) {
                        sxShopMap = getShopMaps( sxShopMap, map, pro_type, storeList );
                        sxShopMap.setProList( sxCartList );
                        sxshopCartList.add( sxShopMap );

                        sxCartList = new ArrayList<>();
                        sxShopMap = new AppletShopCartResult();
                    }
                }
                j++;
            }
        }
        if ( shopCartList != null && shopCartList.size() > 0 ) {
            resultMap.put( "productList", shopCartList );
        }
        if ( sxshopCartList != null && sxshopCartList.size() > 0 ) {
            resultMap.put( "sxProductList", sxshopCartList );
        }

        //混批
	/*if(type == 1){//批发购物车
		//通过商品id查询预售信息
		MallPaySet set = mallPaySetService.selectByMember(member);
		double hpMoney = 0;
		int hpNum = 1;//混批件数
		int spHand = 1;
		if(CommonUtil.isNotEmpty(set)){
			if(CommonUtil.isNotEmpty(set.getIsPf())){//是否开启批发
				if(CommonUtil.isNotEmpty(set.getPfSet())){
					JSONObject obj = JSONObject.fromObject(set.getPfSet());
					if(CommonUtil.isNotEmpty(obj.get("isHpMoney"))){
						if(obj.get("isHpMoney").toString().equals("1") && CommonUtil.isNotEmpty(obj.get("hpMoney"))){
							hpMoney = CommonUtil.toDouble(obj.get("hpMoney"));
						}
					}
					if(CommonUtil.isNotEmpty(obj.get("isHpNum"))){
						if(obj.get("isHpNum").toString().equals("1") && CommonUtil.isNotEmpty(obj.get("hpNum"))){
							hpNum = CommonUtil.toInteger(obj.get("hpNum"));
						}
					}
					if(CommonUtil.isNotEmpty(obj.get("isSpHand"))){
						if(obj.get("isSpHand").toString().equals("1") && CommonUtil.isNotEmpty(obj.get("spHand"))){
							spHand = CommonUtil.toInteger(obj.get("spHand"));
						}
					}
				}
			}
		}
		resultMap.put("hpMoney", hpMoney);
		resultMap.put("hpNum", hpNum);
		resultMap.put("spHand", spHand);
	}*/
        return resultMap;
    }

    private AppletShopCartResult getShopMaps( AppletShopCartResult shopMap, Map< String,Object > map, int pro_type, List< Map< String,Object > > storeList ) {
        String wxShopId = CommonUtil.toString( map.get( "wx_shop_id" ) );
        String shopPicture = "";
        for ( Map< String,Object > storeMap : storeList ) {
            if ( storeMap.get( "wxShopId" ).toString().equals( wxShopId ) ) {
                if ( CommonUtil.isNotEmpty( storeMap.get( "sto_name" ) ) ) {
                    shopMap.setSto_name( storeMap.get( "sto_name" ).toString() );
                }
                List< WsShopPhoto > photoList = wxShopService.getShopPhotoByShopId( CommonUtil.toInteger( map.get( "wx_shop_id" ) ) );
                if ( photoList != null && photoList.size() > 0 ) {
                    shopPicture = photoList.get( 0 ).getLocalAddress();
                }
                break;
            }
        }
        shopMap.setShop_id( CommonUtil.toInteger( map.get( "shop_id" ) ) );
        shopMap.setPageid( CommonUtil.isEmpty( map.get( "pageid" ) ) ? null : CommonUtil.toInteger( map.get( "pageid" ) ) );
        shopMap.setPro_type( pro_type );
        if ( CommonUtil.isNotEmpty( shopPicture ) ) {
            shopMap.setSto_image( PropertiesUtil.getResourceUrl() + shopPicture );
        } else if ( CommonUtil.isNotEmpty( map.get( "stoPicture" ) ) ) {
            shopMap.setSto_image( PropertiesUtil.getResourceUrl() + map.get( "stoPicture" ) );
        }
        return shopMap;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public void shoppingdelete( List< Integer > ids ) {
        if ( CommonUtil.isNotEmpty( ids ) && ids.size() > 0 ) {
            for ( int id : ids ) {
                shopCartDAO.deleteById( id );
            }
        }
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public void shoppingorder( Map< String,Object > params ) {
        if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
            int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
            List< Integer > memberList = memberService.findMemberListByIds( memberId );//查询会员信息
            params.put( "memberList", memberList );
            shopCartDAO.updateCheckByShopCart( params );
        }
        JSONArray jsonArray = JSONArray.parseArray( params.get( "cart" ).toString() );
        for ( int i = 0; i < jsonArray.size(); i++ ) {
            JSONObject obj = JSONObject.parseObject( jsonArray.get( i ).toString() );
            MallShopCart cart = new MallShopCart();
            cart.setId( CommonUtil.toInteger( obj.get( "id" ) ) );
            cart.setIsCheck( CommonUtil.toInteger( obj.get( "check" ) ) );
            cart.setProductNum( CommonUtil.toInteger( obj.get( "num" ) ) );
            if ( CommonUtil.isNotEmpty( obj.get( "specStr" ) ) ) {
                cart.setProSpecStr( CommonUtil.toString( obj.get( "specStr" ) ) );
            }
            shopCartDAO.updateById( cart );
        }
    }

    @Override
    public Map< String,Object > getProductSpecifica( Map< String,Object > params ) {
        if ( CommonUtil.isEmpty( params.get( "id" ) ) ) {
            return null;
        }
        int productId = CommonUtil.toInteger( params.get( "id" ) );
        Map< String,Object > resultMap = new HashMap< String,Object >();
        //查询商品的规格价
        Map< String,Object > defaultInvMap = productInventoryService.productSpecifications( productId, "" );//查询商品默认的规格

        List< Map< String,Object > > specificaList = productSpecificaService.getSpecificaByProductId( productId );//获取商品规格值
        List< Map< String,Object > > guigePriceList = productInventoryService.guigePrice( productId );//根据商品id获取所有规格对应的规格、价格、图片

        resultMap.put( "specificaNum", guigePriceList.size() );

        if ( CommonUtil.isNotEmpty( defaultInvMap ) ) {
            if ( CommonUtil.isNotEmpty( defaultInvMap.get( "specifica_name" ) ) ) {
                String names = defaultInvMap.get( "specifica_name" ).toString();
                resultMap.put( "productSpeciname", names.replaceAll( "&nbsp;&nbsp;", "," ) );
            }
            if ( CommonUtil.isNotEmpty( defaultInvMap.get( "xids" ) ) ) {//规格id
                resultMap.put( "productSpecificas", defaultInvMap.get( "xids" ) );
            }
        }

        resultMap.put( "specificaList", specificaList );
        resultMap.put( "guigePriceList", guigePriceList );
        return resultMap;
    }
}
