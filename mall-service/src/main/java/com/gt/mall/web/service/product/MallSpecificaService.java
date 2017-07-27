package com.gt.mall.web.service.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallSpecifica;

import java.util.Map;

/**
 * <p>
 * 用户添加规格 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSpecificaService extends BaseService< MallSpecifica > {

    /**
     * 根据规格名称查询规格信息
     * @param params name 规格名称
     * @param params userId 商家id
     * @param params erpNameId  erp规格名称id
     * @return
     */
    MallSpecifica selectBySpecName(Map<String, Object> params);


}
