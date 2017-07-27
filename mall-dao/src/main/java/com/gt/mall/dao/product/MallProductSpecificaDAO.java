package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallProductSpecifica;

import java.util.Map;

/**
 * <p>
 * 商品的规格 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductSpecificaDAO extends BaseMapper< MallProductSpecifica > {

    Map<String,Object> selectValueBySpecId(int specId);
}