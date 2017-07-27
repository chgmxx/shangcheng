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
public interface MallImageAssociativeService extends BaseService<MallImageAssociative> {

    /**
     * 根据关联id来查询图片
     *
     * @Title: selectByAssId
     */
    List<MallImageAssociative> selectByAssId(Map<String, Object> params);

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
     * @Title: insertImage
     */
    void insertUpdBatchImage(Map<String, Object> map, Integer proId);


    /**
     * 根据关联id来查询图片
     *
     * @Title: selectByAssId
     */
    List<MallImageAssociative> selectImageByAssId(Map<String, Object> params);

}
