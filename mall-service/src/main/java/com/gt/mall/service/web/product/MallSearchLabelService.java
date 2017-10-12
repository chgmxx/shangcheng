package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallSearchLabel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城搜索标签表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSearchLabelService extends BaseService< MallSearchLabel > {

    /**
     * 查询搜索标签
     *
     * @param map
     *
     * @return
     */
    List< Map< String,Object > > selectByUser( Map< String,Object > map );

}
