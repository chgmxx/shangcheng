package com.gt.mall.web.service.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallImageAssociative;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片中间表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallImageAssociativeService extends BaseService< MallImageAssociative > {

    /**
     * 根据关联id来查询图片
     *
     * @param params isMainImages：是否是主图，assType：关联表类型，assId：关联id
     *
     * @return 图片列表
     */
    List< MallImageAssociative > selectByAssId( Map< String,Object > params );

    /**
     * 添加图片
     *
     * @Title: insertImage
     */
    //    Integer insertImage(MallImageAssociative image);

    /**
     * 修改图片
     *
     * @Title: updateById
     */
    //    void updateImageById(MallImageAssociative image);

    /**
     * 批量添加或修改图片
     *
     * @param map   delimageList：删除列表，imageList：图片列表
     * @param proId 关联id
     */
    void insertUpdBatchImage( Map< String,Object > map, Integer proId );

    /**
     * 根据关联id来查询图片
     *
     * @param params isMainImages：是否是主图，assType：关联表类型，assId：关联id
     *
     * @return  图片
     */
    List< Map< String,Object > > selectImageByAssId( Map< String,Object > params );

    List< MallImageAssociative > getParamByProductId( Map< String,Object > params );

}
