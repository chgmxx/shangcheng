package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallGroup;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分组 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupDAO extends BaseMapper< MallGroup > {

    /**
     * 统计商品分组的个数
     */
    Integer selectGroupByCount( Map< String,Object > param );

    /**
     * 分页查找商品分组
     */
    List< Map< String,Object > > selectGroupByPage( Map< String,Object > param );

    /**
     * 分页查找商品分组 (设计页面时调用，其中返回值不能动)
     */
    List< Map< String,Object > > selectGroupDialogByPage( Map< String,Object > param );

    /**
     * 通过父类分组查询子类列表
     */
    List< Map< String,Object > > selectGroupByParent( Map< String,Object > param );

    /**
     * 批量修改
     */
    void updateByGroupId( List< MallGroup > groupList );

    /**
     * 根据店铺id查询分组信息
     */
    List< Map< String,Object > > selectGroupsByShopId( Map< String,Object > param );

}
