package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallProductDetail;

import java.util.List;

/**
 * <p>
 * 商品详情表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductDetailService extends BaseService< MallProductDetail > {

    /**
     * 根据商品id查询商品详情信息
     *
     * @param productId 商品id
     *
     * @return 商品详情信息
     */
    MallProductDetail selectByProductId( int productId );

    /**
     * 根据商品id查询商品详情
     * @param productIds 商品id
     * @return 商品详情信息
     */
    List<MallProductDetail> selectByProductIds (List<Integer> productIds);
}
