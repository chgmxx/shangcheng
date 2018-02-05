package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.freight.MallFreightDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.pifa.MallPifaPrice;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.shopCart.*;
import com.gt.mall.result.phone.shopcart.*;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionCardService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaPriceService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallShopCartService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.CookieUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

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
    @Autowired
    private MallPageService             mallPageService;
    @Autowired
    private MallStoreService            mallStoreService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private MallPifaDAO                 mallPifaDAO;
    @Autowired
    private MallPifaPriceService        mallPifaPriceService;
    @Autowired
    private MallPifaService             mallPifaService;

    @Override
    public List< Map< String,Object > > getProductByShopCart( String shopcards, WxPublicUsers pbUser, Member member, int userId, List< Map< String,Object > > shopList ) {
        List< Map< String,Object > > list = new ArrayList<>();

        Map< String,Object > params = new HashMap<>();
        params.put( "cartIds", shopcards.split( "," ) );
        params.put( "busUserId", userId );
        int index = 1;
        List< Map< String,Object > > shoplist = mallShopCartDAO.selectCheckShopByParam( params );//获取所有的店铺
        if ( shoplist != null && shoplist.size() > 0 ) {
            Map< String,Object > shopcartParams = new HashMap<>();
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
                    double price = 0;
                    if ( CommonUtil.isNotEmpty( map3.get( "price" ) ) ) {
                        price = CommonUtil.toDouble( map3.get( "price" ) );
                    }
                    double proPriceTotal = 0;
                    int num = Integer.parseInt( map3.get( "product_num" ).toString() );

                    String product_specificas = "";
                    if ( CommonUtil.isNotEmpty( map3.get( "product_specificas" ) ) ) {
                        product_specificas = map3.get( "product_specificas" ).toString();
                    }
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
                        double proPrice = 0;
                        int proNums = 0;
                        specNames = new StringBuilder();
            /*product_specificas = "";*/
                        JSONObject obj = JSONObject.fromObject( map3.get( "pro_spec_str" ) );
                        specMap = new HashMap<>();
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

                                        double p = CommonUtil.toDouble( valObj.get( "price" ) );
                                        price_total = price_total + ( p * num );
                                        proPriceTotal = proPriceTotal + ( p * num );
                                        yuanjia_total += num * Double.parseDouble( map3.get( "primary_price" ).toString() );
                                        primary_price += Double.parseDouble( map3.get( "primary_price" ).toString() ) * num;
                                        proNum += num;
                                        flag = false;

					/*product_specificas += key + ",";*/
                                        specMap.put( key.toString(), valObj );
                                        proPrice += p;
                                        proNums += num;
                                    }
                                }
                            }
                        }
                        price = proPrice / proNums;
                        num = proNums;
            /*product_specificas = product_specificas.substring( 0, product_specificas.length() - 1 );*/
                    }
                    if ( flag ) {
                        price_total = price_total + ( price * num );
                        proPriceTotal = proPriceTotal + ( price * num );
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
                        productMap.put( "pro_spec_str", com.alibaba.fastjson.JSONObject.toJSONString( specObj ) );
                    }
                    if ( specMap != null && specMap.size() > 0 ) {
                        productMap.put( "specMap", specMap );
                    }
                    String proType = map3.get( "pro_type" ).toString();
                    if ( proType.equals( "1" ) || proType.equals( "2" ) ) {
                        shopMap.put( "groupType", 7 );
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

        List< Map< String,Object > > productList = new ArrayList<>();
        double price_total = 0;//保存商品总价
        double yuanjia_total = 0;
        double primary_price = 0;//保存商品原价
        int proNum = 0;//保存商品数量
        int shopId = 0;
        double pro_weight = 0;
        int proTypeId;

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

                    StringBuilder specificaValue = new StringBuilder();
                    //查询规格信息
                    if ( CommonUtil.isNotEmpty( maps.get( "productSpecificas" ) ) ) {
            /*String specSql = "SELECT id,specifica_value,specifica_img_url FROM t_mall_product_specifica WHERE is_delete=0 AND specifica_value_id IN(" + maps
					.get( "productSpecificas" ) + ")  AND product_id=" + productId + " ORDER BY sort";
			List< Map< String,Object > > specMapList = daoUtil.queryForList( specSql );*/
                        Map< String,Object > specificaMap = mallProductService.getProInvIdBySpecId( maps.get( "productSpecificas" ).toString(), productId );
                        if ( CommonUtil.isNotEmpty( specificaMap ) && specificaMap.size() > 0 ) {
                            specificaValue = new StringBuilder( specificaMap.get( "specifica_values" ).toString() );
                            if ( CommonUtil.isNotEmpty( specificaMap.get( "specifica_img_url" ) ) ) {
                                imageUrl = CommonUtil.toString( specificaMap.get( "specifica_img_url" ) );
                            }
                        }
                    }
                    if ( CommonUtil.isEmpty( imageUrl ) ) {
                        //查询商品图片
                        Map< String,Object > params = new HashMap<>();
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
                        double proPrice = 0;
                        int proTotalNum = 0;
                        JSONObject obj = JSONObject.fromObject( maps.get( "proSpecStr" ) );
                        specMap = new HashMap<>();
                        if ( obj != null && obj.size() > 0 ) {
                            for ( Object key : obj.keySet() ) {
                                JSONObject valObj = JSONObject.fromObject( obj.get( key ) );
                                if ( CommonUtil.isNotEmpty( valObj.get( "isCheck" ) ) ) {
                                    if ( valObj.get( "isCheck" ).toString().equals( "1" ) ) {
                                        specObj.put( key, valObj );
                                        if ( !specificaValue.toString().equals( "" ) ) {
                                            specificaValue.append( "、" );
                                        } else {
                                            specificaValue = new StringBuilder( valObj.getString( "specName" ) + "：" );
                                        }
                                        specificaValue.append( valObj.get( "value" ) ).append( " X " ).append( valObj.get( "num" ) ).append( " ¥" ).append( valObj.get( "price" ) );

                                        num = CommonUtil.toInteger( valObj.get( "num" ) );
                                        price = CommonUtil.toDouble( valObj.get( "price" ) );

                                        price_total = price_total + ( price * num );
                                        totalPrice += ( price * num );
                                        yuanjia_total += num * Double.parseDouble( maps.get( "detPrivivilege" ).toString() );
                                        primary_price += Double.parseDouble( maps.get( "detPrivivilege" ).toString() ) * num;
                                        proNum += num;
                                        flag = false;

                                        specMap.put( key.toString(), valObj );

                                        proPrice += price * num;
                                        proTotalNum += num;
                                    }
                                }
                            }
                        }
                        price = proPrice / proTotalNum;
                        num = proTotalNum;
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
                    if ( CommonUtil.isNotEmpty( specificaValue.toString() ) ) {
                        productMap.put( "product_speciname", specificaValue.toString() );
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
    public void addShoppingCart( Member member, PhoneAddShopCartDTO params, HttpServletRequest request, HttpServletResponse response ) {
        int memberId = 0;
        if ( CommonUtil.isNotEmpty( member ) ) {
            memberId = member.getId();
        }
        MallProduct product = mallProductService.selectById( params.getProductId() );
        //判断商品库存是否足够 和  是否已经超过了限购,如果不够会抛异常
        Map< String,Object > resultMap = mallProductService.calculateInventory( params.getProductId(), params.getProductSpecificas(), params.getProductNum(), memberId );

        MallShopCart mallShopCart = new MallShopCart();
        mallShopCart.setProductId( params.getProductId() );
        mallShopCart.setShopId( product.getShopId() );
        if ( CommonUtil.isNotEmpty( resultMap.get( "specificaValue" ) ) ) {
            mallShopCart.setProductSpeciname( "specificaValue" );
        }
        if ( CommonUtil.isNotEmpty( params.getProductSpecificas() ) ) {
            mallShopCart.setProductSpecificas( params.getProductSpecificas() );
        }
        mallShopCart.setProductNum( params.getProductNum() );
        mallShopCart.setUserId( memberId );
        mallShopCart.setCreateTime( new Date() );
        mallShopCart.setPrice( BigDecimal.valueOf( params.getPrice() ) );
        mallShopCart.setPrimaryPrice( product.getProPrice() );
        mallShopCart.setUserType( params.getUserType() );
        mallShopCart.setSaleMemberId( params.getSaleMemberId() );
        mallShopCart.setCommission( BigDecimal.valueOf( params.getCommission() ) );
        mallShopCart.setBusUserId( product.getUserId() );

        int count;
        List< Map< String,Object > > cartList = null;
        String cookieVales = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
        if ( CommonUtil.isNotEmpty( member ) ) {
            cartList = mallShopCartDAO.selectByShopCart( mallShopCart );
        } else if ( CommonUtil.isNotEmpty( cookieVales ) ) {
            cartList = mallPageService.selectByShopCart( mallShopCart, cookieVales );
        }
        if ( cartList != null && cartList.size() > 0 ) {
            Map< String,Object > map = cartList.get( 0 );
            mallShopCart.setId( CommonUtil.toInteger( map.get( "id" ) ) );
            if ( CommonUtil.isNotEmpty( params.getProSpecStr() ) ) {
                mallShopCart = mallPageService.getProSpecNum( params.getProSpecStr(), mallShopCart );
            }
            count = mallShopCartDAO.updateByShopCart( mallShopCart );
        } else {
            count = mallShopCartDAO.insert( mallShopCart );
        }
        String value = mallShopCart.getId().toString();
        if ( CommonUtil.isNotEmpty( cookieVales ) ) {
            value = cookieVales + "," + mallShopCart.getId();
        }
        CookieUtil.addCookie( response, CookieUtil.SHOP_CART_COOKIE_KEY, value, Constants.COOKIE_SHOP_CART_TIME );
        if ( count <= 0 ) {
            throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
        }
    }

    @Override
    public PhoneShopCartResult getShopCart( Member member, Integer busId, Integer type, HttpServletRequest request, HttpServletResponse response ) {
        if ( CommonUtil.isNotEmpty( member ) ) {//已登陆
            mallPageService.mergeShoppCart( member, request, response );//把cookie商品，合并到购物车
        }
        int memberId;
        List< Integer > memberList = null;
        Map< String,Object > shopParams = new HashMap<>();
        if ( member != null ) {
            memberId = CommonUtil.toInteger( member.getId() );
            memberList = memberService.findMemberListByIds( memberId );//根据粉丝id查询会员集合
        }
        if ( member != null ) {
            if ( memberList != null && memberList.size() > 0 ) {
                shopParams.put( "memberList", memberList );
            } else {
                shopParams.put( "memberId", member.getId() );
            }
        } else {
            String shopCartIds = CookieUtil.findCookieByName( request, CookieUtil.SHOP_CART_COOKIE_KEY );
            if ( CommonUtil.isNotEmpty( shopCartIds ) ) {
                shopParams.put( "checkIds", shopCartIds.split( "," ) );
            }
        }
        if ( CommonUtil.isEmpty( type ) ) {
            type = 0;
        }
        shopParams.put( "type", type );
        List< Map< String,Object > > shopList = null;
        String userName = null;
        String userHeadImage = null;
        if ( busId > 0 ) {
            shopList = mallStoreService.findShopByUserId( busId, request );//查询店铺信息
            WxPublicUsers wxPublicUsers = wxPublicUserService.selectByUserId( busId );//查询公众号信息
            if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
                userName = wxPublicUsers.getAuthorizerInfo();
                userHeadImage = wxPublicUsers.getHeadImg();
            }
            if ( CommonUtil.isEmpty( userName ) ) {
                BusUser user = busUserService.selectById( busId );//查询商家信息
                if ( CommonUtil.isNotEmpty( user ) ) {
                    userName = user.getName();
                }
            }
        }
        //获取会员折扣
        double discount = mallProductService.getMemberDiscount( "1", member );
        if ( CommonUtil.isEmpty( shopParams.get( "memberList" ) ) && CommonUtil.isEmpty( shopParams.get( "memberId" ) ) && CommonUtil.isEmpty( shopParams.get( "checkIds" ) ) ) {
            return null;
        }

        List< Map< String,Object > > list = mallShopCartDAO.selectShopCartByMemberId( shopParams );//查询购物车信息

        List< PhoneProductResult > productResultList = getShopCartParams( list, shopList, discount );//重组购物车返回参数

        //保存购物车返回值
        List< PhoneShopCartListResult > resultList = new ArrayList<>();
        PhoneShopCartListResult shopCartResult = new PhoneShopCartListResult();
        shopCartResult.setBusId( busId );
        shopCartResult.setUserName( userName );
        shopCartResult.setUserImageUrl( userHeadImage );

        //保存失效购物车值
        List< PhoneShopCartListResult > sxResultList = new ArrayList<>();
        PhoneShopCartListResult sxShopCartResult = new PhoneShopCartListResult();
        sxShopCartResult.setBusId( busId );
        sxShopCartResult.setUserName( userName );
        sxShopCartResult.setUserImageUrl( userHeadImage );

        List< PhoneShopResult > shopResultList = new ArrayList<>();
        List< PhoneShopResult > sxShopResultList = new ArrayList<>();
        StringBuilder id = new StringBuilder();//购物车id
        if ( shopList != null && shopList.size() > 0 ) {
            for ( Map< String,Object > shopMap : shopList ) {
                int shopId = CommonUtil.toInteger( shopMap.get( "id" ) );
                int errorStatus = 0;
                String errorMsg = "";
                if ( shopMap.get( "is_delete" ).toString().equals( "1" ) ) {
                    errorMsg = "店铺已删除";
                    errorStatus = 1;
                }

                List< PhoneProductResult > productList = new ArrayList<>();
                List< PhoneProductResult > sxProductList = new ArrayList<>();
                List< PhoneProductResult > removeList = new ArrayList<>();
                if ( productResultList != null && productResultList.size() > 0 ) {
                    for ( PhoneProductResult result : productResultList ) {
                        if ( result.getShopId() == shopId ) {
                            if ( errorStatus == 1 ) {
                                result.setIsError( 1 );
                                result.setErrorMsg( errorMsg );
                            }
                            if ( result.getIsError() == 1 ) {
                                sxProductList.add( result );
                            } else {
                                id.append( result.getId() ).append( "," );
                                productList.add( result );
                            }
                            removeList.add( result );
                        }
                    }
                    if ( removeList != null && removeList.size() > 0 ) {
                        productResultList.removeAll( removeList );
                    }
                }
                if ( productList != null && productList.size() > 0 ) {
                    shopResultList.add( getShopCartShopParams( shopId, shopMap.get( "sto_name" ).toString(), productList ) );
                }
                if ( sxProductList != null && sxProductList.size() > 0 ) {
                    sxShopResultList.add( getShopCartShopParams( shopId, shopMap.get( "sto_name" ).toString(), sxProductList ) );
                }
            }
            if ( id != null && id.length() > 1 ) {
                id = new StringBuilder( id.substring( 0, id.length() - 1 ) );
                CookieUtil.addCookie( response, CookieUtil.SHOP_CART_COOKIE_KEY, id.toString(), Constants.COOKIE_SHOP_CART_TIME );
            }
        }
        PhoneShopCartResult result = new PhoneShopCartResult();
        if ( shopResultList != null && shopResultList.size() > 0 ) {
            shopCartResult.setShopResultList( shopResultList );
            resultList.add( shopCartResult );
            result.setShopCartList( resultList );
            logger.info( "购物车返回数据：", resultList );
        }
        if ( sxShopResultList != null && sxShopResultList.size() > 0 ) {
            sxShopCartResult.setShopResultList( sxShopResultList );
            sxResultList.add( sxShopCartResult );
            result.setSxShopCartList( sxResultList );
            logger.info( "失效购物车返回数据：", sxResultList );
        }
        if ( CommonUtil.isNotEmpty( type ) && type == 1 ) {//批发购物车
            Map< String,Object > pfSetMap = mallPifaService.getPifaSet( busId );
            if ( CommonUtil.isNotEmpty( pfSetMap ) ) {
                result.setHpMoney( CommonUtil.toDouble( pfSetMap.get( "hpMoney" ) ) );
                result.setHpNum( CommonUtil.toInteger( pfSetMap.get( "hpNum" ) ) );
                result.setSpHand( CommonUtil.toInteger( pfSetMap.get( "spHand" ) ) );
            }
        }
        return result;
    }

    private PhoneShopResult getShopCartShopParams( int shopId, String shopName, List< PhoneProductResult > productList ) {
        PhoneShopResult shopResult = new PhoneShopResult();
        shopResult.setShopId( shopId );
        shopResult.setShopName( shopName );
        shopResult.setProductResultList( productList );
        return shopResult;
    }

    private List< PhoneProductResult > getShopCartParams( List< Map< String,Object > > list, List< Map< String,Object > > shopList, double discount ) {
        List< PhoneProductResult > productResultList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat( "######0.00" );
        if ( list != null && list.size() > 0 ) {
            for ( Map< String,Object > shopCartMap : list ) {
                String msg = "";
                int code = 1;
                String proId = shopCartMap.get( "product_id" ).toString();
                int productNum = CommonUtil.toInteger( shopCartMap.get( "product_num" ) );
                int stockNum = CommonUtil.toInteger( shopCartMap.get( "pro_stock_num" ) );
                double price = CommonUtil.toDouble( shopCartMap.get( "price" ) );
                double productPrice = price;
                double primaryPrice = price;
                double hyPrice = 0;
                String productSpecifica = "";//普通商品规格
                int maxBuy = 0;//限购数量

                int pro_type = 0;
                if ( CommonUtil.isNotEmpty( shopCartMap.get( "pro_type" ) ) ) {
                    pro_type = CommonUtil.toInteger( shopCartMap.get( "pro_type" ) );
                }
                Map< String,Object > xgMap = mallProductService.isshoppingCart( shopCartMap, productNum, shopList );
                if ( xgMap.get( "code" ).toString().equals( "1" ) ) {
                    if ( xgMap.containsKey( "product_num" ) ) {
                        productNum = CommonUtil.toInteger( xgMap.get( "product_num" ) );
                    }
                    if ( xgMap.containsKey( "maxBuy" ) ) {
                        maxBuy = CommonUtil.toInteger( xgMap.get( "maxBuy" ) );
                    }
                } else {
                    code = CommonUtil.toInteger( xgMap.get( "code" ) );
                    msg = CommonUtil.toString( xgMap.get( "msg" ) );
                }
		/*if ( code == 0 ) {
		    break;
		}*/
                MallShopCart cart = new MallShopCart();
                cart.setId( CommonUtil.toInteger( shopCartMap.get( "id" ) ) );
                if ( shopCartMap.get( "isSpec" ).toString().equals( "1" ) && code == 1 && pro_type == 0 ) {//商品存在规格
                    String proSpec = shopCartMap.get( "product_specificas" ).toString();
                    if ( proSpec.equals( "" ) ) {
                        code = 0;
                        msg = "商品存在规格";
                    } else {
                        //判断商品规格是否异常
                        Map< String,Object > invPriceMap = mallProductService.getProInvIdBySpecId( proSpec, CommonUtil.toInteger( proId ) );
                        boolean isErrorflag = false;

                        if ( CommonUtil.isNotEmpty( invPriceMap ) ) {
                            productSpecifica = CommonUtil.toString( invPriceMap.get( "specifica_values" ) );
                            if ( CommonUtil.isNotEmpty( productSpecifica ) ) {
                                productSpecifica = productSpecifica.replace( ",", "/" );
                            }
                            stockNum = CommonUtil.toInteger( invPriceMap.get( "inv_num" ) );
                            if ( CommonUtil.isNotEmpty( invPriceMap.get( "specifica_img_url" ) ) ) {
                                shopCartMap.put( "image_url", invPriceMap.get( "specifica_img_url" ) );
                            }
                            double invPrice = CommonUtil.toDouble( invPriceMap.get( "inv_price" ) );
                            if ( CommonUtil.isNotEmpty( shopCartMap.get( "is_member_discount" ) ) ) {
                                if ( shopCartMap.get( "is_member_discount" ).toString().equals( "1" ) ) {//开启会员折扣
                                    hyPrice = invPrice * discount;
                                    if ( hyPrice <= 0 ) {
                                        hyPrice = 0.01;
                                    } else {
                                        hyPrice = CommonUtil.toDouble( df.format( hyPrice ) );
                                    }
                                    primaryPrice = invPrice;
                                }
                            }
                            if ( price != invPrice ) {//同步价格
                                productPrice = invPrice;
                                cart.setPrice( BigDecimal.valueOf( productPrice ) );
                                cart.setPrimaryPrice( BigDecimal.valueOf( primaryPrice ) );
                                mallShopCartDAO.updateById( cart );
                            }
                            if ( CommonUtil.isEmpty( invPriceMap.get( "inv_num" ) ) ) {//判断库存是否为空
                                isErrorflag = true;
                            } else {
                                int invNum = CommonUtil.toInteger( invPriceMap.get( "inv_num" ) );
                                if ( invNum <= 0 ) {
                                    isErrorflag = true;
                                } else if ( productNum > invNum ) {
                                    productNum = invNum;
                                }
                            }
                        } else {
                            code = 0;
                            msg = "商品规格不存在";
                        }
                        if ( isErrorflag ) {
                            code = 0;
                            msg = "商品已售罄";
                        }
                    }
                } else if ( shopCartMap.get( "isSpec" ).toString().equals( "0" ) && code == 1 && pro_type == 0 ) {//商品部存在规格
                    if ( CommonUtil.isNotEmpty( shopCartMap.get( "product_specificas" ) ) ) {
                        code = 0;
                        msg = "商品不存在规格";
                    }
                    double proPrice = CommonUtil.toDouble( shopCartMap.get( "pro_price" ) );
                    if ( CommonUtil.isNotEmpty( shopCartMap.get( "is_member_discount" ) ) ) {
                        if ( shopCartMap.get( "is_member_discount" ).toString().equals( "1" ) ) {//开启会员折扣
                            hyPrice = proPrice * discount;
                            if ( hyPrice <= 0 ) {
                                hyPrice = 0.01;
                            } else {
                                hyPrice = CommonUtil.toDouble( df.format( hyPrice ) );
                            }
                            primaryPrice = proPrice;
                        }
                    }
                    if ( price != proPrice ) {//同步价格
                        cart.setPrice( BigDecimal.valueOf( proPrice ) );
                        cart.setPrimaryPrice( BigDecimal.valueOf( primaryPrice ) );
                        mallShopCartDAO.updateById( cart );
                    }
                    if ( productNum > stockNum && stockNum == 0 ) {
                        code = 0;
                        msg = "商品已售罄";
                    } else if ( productNum > stockNum && stockNum > 0 ) {
                        productNum = stockNum;
                    }
                }
                int pifaId = 0;
                if ( pro_type == 1 || pro_type == 2 ) {//批发商品
                    Map< String,Object > pifaMap = new HashMap<>();
                    pifaMap.put( "pfType", 1 );
                    pifaMap.put( "product_id", proId );
                    List< MallPifa > pfList = mallPifaDAO.selectStartPiFaByProductId( pifaMap );
                    if ( pfList == null || pfList.size() == 0 ) {
                        code = 0;
                        msg = "批发商品已结束或已删除";
                    } else {
                        MallPifa pifa = pfList.get( 0 );
                        pifaId = pifa.getId();
                        if ( !pifa.getPfType().toString().equals( shopCartMap.get( "pro_type" ).toString() ) ) {
                            code = 0;
                            msg = "商家已更改批发类型";
                        }
                    }
                }
                List< PhonePifaProductSpecificaResult > pifaSpecificaList = new ArrayList<>();
                if ( CommonUtil.isNotEmpty( shopCartMap.get( "pro_spec_str" ) ) ) {
                    Map specStr = com.alibaba.fastjson.JSONObject.parseObject( shopCartMap.get( "pro_spec_str" ).toString(), Map.class );
                    Set set = specStr.keySet();
                    if ( set != null && set.size() > 0 ) {
                        for ( Object str : set ) {
                            if ( CommonUtil.isNotEmpty( specStr.get( str ) ) ) {
                                Map valMap = com.alibaba.fastjson.JSONObject.parseObject( specStr.get( str ).toString(), Map.class );

                                Map< String,Object > invPrice = mallProductService.getProInvIdBySpecId( str.toString(), CommonUtil.toInteger( proId ) );
                                int inv_num = 0;
                                if ( CommonUtil.isNotEmpty( invPrice ) ) {
                                    inv_num = CommonUtil.toInteger( invPrice.get( "inv_num" ) );
                                }
                                double pfPrice = CommonUtil.toDouble( valMap.get( "price" ) );
                                MallPifaPrice pifaPrice = mallPifaPriceService.selectPifaBySpecifica( str.toString(), pifaId );
                                if ( pifaPrice != null ) {
                                    pfPrice = CommonUtil.toDouble( pifaPrice.getSeckillPrice() );
                                }
                                PhonePifaProductSpecificaResult pifaResult = new PhonePifaProductSpecificaResult();
                                pifaResult.setProductNum( CommonUtil.toInteger( valMap.get( "num" ) ) );
                                pifaResult.setSpecificaPrice( pfPrice );
                                pifaResult.setStockNum( inv_num );
                                pifaResult.setSpecificaValueIds( str.toString() );
                                pifaResult.setSpecificaValues( CommonUtil.toString( invPrice.get( "specifica_values" ) ) );
                                pifaResult.setSpecificaName( invPrice.get( "specificaName" ).toString() );
                                pifaResult.setId( CommonUtil.toInteger( shopCartMap.get( "id" ) ) );
                                pifaSpecificaList.add( pifaResult );
                            }
                        }
                    }
                }
                PhoneProductResult productResult = new PhoneProductResult();
                productResult.setId( CommonUtil.toInteger( shopCartMap.get( "id" ) ) );
                productResult.setProductId( CommonUtil.toInteger( proId ) );
                productResult.setShopId( CommonUtil.toInteger( shopCartMap.get( "shop_id" ) ) );
                productResult.setProductNum( productNum );
                productResult.setProductName( CommonUtil.toString( shopCartMap.get( "pro_name" ) ) );
                productResult.setProductImageUrl( CommonUtil.toString( shopCartMap.get( "image_url" ) ) );
                productResult.setProductPrice( productPrice );
                productResult.setProductOldPrice( primaryPrice );
                productResult.setProductHyPrice( hyPrice );
                productResult.setProductSpecifica( productSpecifica );
                productResult.setMaxBuyNum( maxBuy );
                productResult.setStockNum( stockNum );
                if ( CommonUtil.isNotEmpty( shopCartMap.get( "pro_type" ) ) ) {
                    productResult.setPfType( CommonUtil.toInteger( shopCartMap.get( "pro_type" ) ) );
                }
                if ( pifaSpecificaList != null && pifaSpecificaList.size() > 0 ) {
                    productResult.setPifaSpecificaList( pifaSpecificaList );
                }
                if ( code == 0 ) {
                    productResult.setIsError( 1 );
                    productResult.setErrorMsg( msg );
                }
                productResultList.add( productResult );
            }
        }
        return productResultList;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public void removeShopCart( PhoneRemoveShopCartDTO params, HttpServletRequest request, HttpServletResponse response ) {
        if ( CommonUtil.isNotEmpty( params.getIds() ) ) {
            String[] ids = params.getIds().split( "," );
            if ( ids != null ) {
                if ( ids.length > 1 ) {
                    List< Integer > idList = JSONArray.parseArray( JSON.toJSONString( ids ), Integer.class );
                    mallShopCartDAO.deleteBatchIds( idList );//批量删除购物车信息
                } else if ( ids.length == 1 ) {
                    mallShopCartDAO.deleteById( CommonUtil.toInteger( ids[0] ) );//单个删除购物车信息
                }
            }
        }
        if ( CommonUtil.isNotEmpty( params.getPifaSpecificaList() ) && params.getPifaSpecificaList().size() > 0 ) {
            List< PhoneRemovePifatSpecificaDTO > pifaSpecResultList = params.getPifaSpecificaList();//批发规格
            if ( pifaSpecResultList != null && pifaSpecResultList.size() > 0 ) {
                for ( PhoneRemovePifatSpecificaDTO result : pifaSpecResultList ) {//批发商品对象
                    MallShopCart shopCart = mallShopCartDAO.selectById( result.getId() );
                    if ( CommonUtil.isNotEmpty( shopCart.getProSpecStr() ) ) {
                        JSONObject upObj = JSONObject.fromObject( shopCart.getProSpecStr() );

                        if ( result.getSpecificaValueIds() != null && result.getSpecificaValueIds().length > 0 ) {
                            for ( String s : result.getSpecificaValueIds() ) {
                                upObj.remove( s );
                            }
                        }
                        MallShopCart shopcart = new MallShopCart();
                        shopcart.setProductNum( result.getProductNum() );
                        shopcart.setProSpecStr( com.alibaba.fastjson.JSONObject.toJSONString( upObj ) );
                        shopcart.setId( result.getId() );
                        mallShopCartDAO.updateById( shopcart );

                    }
                }
            }
        }
    }

    @Override
    public void shopCartOrder( List< PhoneShopCartOrderDTO > params ) {
        if ( params != null && params.size() > 0 ) {
            for ( PhoneShopCartOrderDTO cartDto : params ) {
                if ( CommonUtil.isEmpty( cartDto.getId() ) || CommonUtil.isEmpty( cartDto.getProductNum() ) ) {
                    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
                }
                MallShopCart shopCart = new MallShopCart();
                MallShopCart cart = mallShopCartDAO.selectById( cartDto.getId() );
                if ( CommonUtil.isNotEmpty( cartDto.getPifatSpecificaDTOList() ) && cartDto.getPifatSpecificaDTOList().size() > 0 && CommonUtil
                    .isNotEmpty( cart.getProSpecStr() ) ) {
                    JSONObject specObj = JSONObject.fromObject( cart.getProSpecStr() );
                    for ( PhoneShopCartOrderPifatSpecificaDTO specificaDTO : cartDto.getPifatSpecificaDTOList() ) {
                        if ( specificaDTO.getSpecificaValueIds() != null && specificaDTO.getSpecificaValueIds().length > 0 ) {
                            JSONObject newObj = new JSONObject();
                            for ( String valueId : specificaDTO.getSpecificaValueIds() ) {
                                JSONObject obj = specObj.getJSONObject( valueId );
                                obj.put( "num", specificaDTO.getProductNum() );
                                newObj.putAll( obj );
                            }
                            shopCart.setProSpecStr( JSON.toJSONString( newObj ) );
                        }
                    }
                } else if ( cart.getProType() > 0 && CommonUtil.isNotEmpty( cart.getProSpecStr() ) ) {
                    throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
                }
                shopCart.setId( cartDto.getId() );
                shopCart.setProductNum( cartDto.getProductNum() );
                mallShopCartDAO.updateById( shopCart );
                //		mallShopCartDAO.updateByShopCart( shopCart );
            }
        } else {
            throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), ResponseEnums.PARAMS_NULL_ERROR.getDesc() );
        }
    }

}
