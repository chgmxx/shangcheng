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
     */
    List< Map< String,Object > > selectgroupsByProductId( Map< String,Object > map );

    /**
     * 根据店铺id查询商品分组信息
     */
    List< Map< String,Object > > selectProductGroupByShopId( Map< String,Object > params );

    /**
     * 根据父类的分组id查询子类的分组信息
     */
    List< Map< String,Object > > selectGroupByParentId( Map< String,Object > params );

    /**
     * 查询店铺下所有的商品分组和图片
     */
    List< Map< String,Object > > selectGroupsByShopId( Map< String,Object > params );
}
