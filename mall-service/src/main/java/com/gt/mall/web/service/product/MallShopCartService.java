package com.gt.mall.web.service.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.entity.product.MallShopCart;

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
    List< Map< String,Object > > getProductByShopCart( String shopcards, WxPublicUsers pbUser, Member member ,int userId);

    /**
     * 查询商品信息（立即购买进入提交订单页面所需的）
     */
    List< Map< String,Object > > getProductByIds( Map< String,Object > maps, WxPublicUsers pbUser, Member member ,int userId) throws Exception;
}
