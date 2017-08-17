package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallSpecificaValue;

import java.util.Map;

/**
 * <p>
 * 用户添加规格值 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSpecificaValueService extends BaseService< MallSpecificaValue > {

    /**
     * 根据规格值或erp规格id查询规格信息
     * @param params erpValueId erp的规格值
     * @param params value  规格值
     * @return
     */
    MallSpecificaValue selectBySpecValue(Map<String,Object> params);
}
