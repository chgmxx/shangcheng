package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallImageAssociative;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片中间表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallImageAssociativeDAO extends BaseMapper< MallImageAssociative > {

    /**
     * 根据关联id来删除商品图片
     *
     * @param id 关联id
     */
    void deleteAllByAssId( Integer id );

    /**
     * 根据关联表id来查询图片
     *
     * @param params isMainImages：是否是主图，assType：关联表类型，assId：关联id
     *
     * @return 图片列表
     */
    List< MallImageAssociative > selectImageByAssId( Map< String,Object > params );

    /**
     * 批量添加商品图片
     *
     * @param params 图片信息列表
     *
     * @return 是否成功
     */
    int insertBatch( Map< String,Object > params );

    /**
     * 批量修改商品图片
     *
     * @param imageList 图片信息列表
     */
    int updateBatchImage( List< MallImageAssociative > imageList );

    /**
     * 根据关联表id来查询图片
     *
     * @param params isMainImages：是否是主图，assType：关联表类型，assId：关联id
     *
     * @return 图片地址列表
     */
    List< Map< String,Object > > selectByAssId( Map< String,Object > params );

    /**
     * 根据关联表id来查询图片
     *
     * @param params isMainImages：是否是主图，assType：关联表类型，assIds：关联id
     *
     * @return 图片地址列表
     */
    List< Map< String,Object > > selectByAssIds( Map< String,Object > params );
}
