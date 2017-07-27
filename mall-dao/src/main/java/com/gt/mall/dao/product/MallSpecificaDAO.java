package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallSpecifica;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户添加规格 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSpecificaDAO extends BaseMapper< MallSpecifica > {

    /**
     * 获取规格
     *
     */
    List<Map<String, Object>> selectByUserId(Map<String, Object> maps);

}