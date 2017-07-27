package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallShopCart;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallShopCartDAO extends BaseMapper< MallShopCart > {
    /**
     * 查询购物车的信息
     * @param shopCart
     * @return
     */
    List<Map<String, Object> > selectByShopCart(MallShopCart shopCart);

    /**
     * 修改购物车信息
     * @param shopCart
     * @return
     */
    int updateByShopCart(MallShopCart shopCart);

    /**
     * 查询购物车信息
     * @param params
     * @return
     */
    List<Map<String, Object>> selectAppletByParams(Map<String, Object> params);

    /**
     * 查询已选中购物车的商品信息
     * @param params
     * @return
     */
    List<Map<String, Object>> selectCheckCartByParams(Map<String, Object> params);

    /**
     * 查询已选中的店铺信息
     * @param params
     * @return
     */
    List<Map<String, Object>> selectCheckShopByParam(Map<String, Object> params);

    /**
     * 取消已选中的商品
     * @param params
     */
    void updateCheckByShopCart(Map<String, Object> params);

    /**
     * 获取购物车的数量
     * @param params
     * @return
     */
    int selectShopCartNum(Map<String, Object> params);
}