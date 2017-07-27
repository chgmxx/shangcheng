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
     *
     * @param param
     *
     * @return
     */
    List< Map< String,Object > > selectGroupByPage( Map< String,Object > param );

    /**
     * 通过父类分组查询子类列表
     *
     * @param param
     *
     * @return
     */
    List< Map< String,Object > > selectGroupByParent( Map< String,Object > param );

    /**
     * 批量修改
     *
     * @Title: updateByGroupId
     */
    void updateByGroupId( List< MallGroup > groupList );


}