package com.gt.mall.web.service.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallProductDetail;

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
     * @param productId 商品id
     * @return
     */
    MallProductDetail selectByProductId(int productId);
}
