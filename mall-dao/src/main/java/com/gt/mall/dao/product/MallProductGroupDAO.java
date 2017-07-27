package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallProductGroup;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分组中间表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductGroupDAO extends BaseMapper< MallProductGroup > {

    /**
     * 根据商品id查询商品分组信息
     * @param map
     * @return
     */
    List<Map<String, Object>> selectgroupsByProductId(Map<String, Object> map);


}