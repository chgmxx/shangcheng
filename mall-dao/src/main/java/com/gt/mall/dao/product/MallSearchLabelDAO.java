package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallSearchLabel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城搜索标签表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSearchLabelDAO extends BaseMapper< MallSearchLabel > {


    /**
     * 铜鼓店铺id和分组id查询搜索信息
     * @param label
     * @return
     */
    MallSearchLabel selectByGroupShop(MallSearchLabel label);

    /**
     * 通过用户信息查询搜索标签
     * @param map
     * @return
     */
    List<Map<String, Object>> selectByUser(Map<String, Object> map);
}