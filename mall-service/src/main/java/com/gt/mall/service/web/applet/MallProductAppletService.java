package com.gt.mall.service.web.applet;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.applet.MallAppletImage;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序商城订单
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductAppletService extends BaseService< MallAppletImage > {

    /**
     * 查询购物车信息
     *
     * @param params
     */
    Map< String,Object > shoppingcare( Map< String,Object > params, HttpServletRequest request );

    /**
     * 清空购物车内的失效商品、修改商品信息、删除商品信息
     *
     * @param ids
     */
    public void shoppingdelete( List< Integer > ids );

    /**
     * 结算购物车内的信息
     *
     * @param params
     */
    public void shoppingorder( Map< String,Object > params );

    /**
     * 查询商品的规格
     *
     * @param params
     *
     * @return
     */
    public Map< String,Object > getProductSpecifica( Map< String,Object > params );
}
