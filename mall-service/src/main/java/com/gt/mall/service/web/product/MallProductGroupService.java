package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallProductGroup;

/**
 * <p>
 * 商品分组中间表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductGroupService extends BaseService< MallProductGroup > {

    /**
     * 批量保存商品分组
     */
    void saveOrUpdate( Object obj, int proId );

}
