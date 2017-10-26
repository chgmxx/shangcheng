package com.gt.mall.service.web.product;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseService;
import com.gt.api.bean.session.Member;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.param.phone.PhoneAddShopCartDTO;
import com.gt.mall.param.phone.shopCart.PhoneRemoveShopCartDTO;
import com.gt.mall.param.phone.shopCart.PhoneShopCartOrderDTO;
import com.gt.mall.result.phone.shopcart.PhoneShopCartResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallShopCartService extends BaseService< MallShopCart > {

    /**
     * 查询购物车的商品信息（立即结算进入提交订单页面所需的）
     */
    List< Map< String,Object > > getProductByShopCart( String shopcards, WxPublicUsers pbUser, Member member, int userId, List< Map< String,Object > > shopList );

    /**
     * 查询商品信息（立即购买进入提交订单页面所需的）
     */
    List< Map< String,Object > > getProductByIds( Map< String,Object > maps, WxPublicUsers pbUser, Member member, int userId, List< Map< String,Object > > shopList )
		    throws Exception;

    /**
     * 获取进入提交订单的参数
     *
     * @param request       request
     * @param loginCity     所在省份
     * @param userid        商家id
     * @param list          订单信息
     * @param mem_longitude 经度
     * @param mem_latitude  纬度
     */
    void getOrdersParams( HttpServletRequest request, String loginCity, int userid, List< Map< String,Object > > list, double mem_longitude, double mem_latitude, Member member,
		    List< Map< String,Object > > shopList );

    /**
     * 加入购物车
     */
    void addShoppingCart( Member member, PhoneAddShopCartDTO params, HttpServletRequest request, HttpServletResponse response );

    /**
     * 查询购物车的信息
     *
     * @param member   粉丝对象
     * @param busId    商家id
     * @param type     类型 1批发购物车
     * @param request  request
     * @param response response
     *
     * @return 购物车信息
     */
    PhoneShopCartResult getShopCart( Member member, Integer busId, Integer type, HttpServletRequest request, HttpServletResponse response );

    /**
     * 删除购物车信息
     *
     * @param params   购物车信息
     * @param request  request
     * @param response response
     */
    void removeShopCart( PhoneRemoveShopCartDTO params, HttpServletRequest request, HttpServletResponse response );

    /**
     * 购物车去结算 (不能去结算会抛异常)
     *
     * @param params 参数
     */
    void shopCartOrder( List< PhoneShopCartOrderDTO > params );
}
