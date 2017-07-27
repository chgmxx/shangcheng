package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallSpecificaValue;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户添加规格值 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSpecificaValueDAO extends BaseMapper< MallSpecificaValue > {

    /**
     * 获取规格值
     * @Title: selectBySpecId
     */
    List<Map<String, Object> > selectBySpecId(Map<String, Object> map);
}