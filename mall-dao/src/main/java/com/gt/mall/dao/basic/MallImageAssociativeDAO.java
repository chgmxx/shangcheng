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
     * @param ids
     */
    void deleteAllByAssId(Integer ids);

    /**
     *  根据关联表id来查询图片
     * @param params
     * @return
     */
    List<MallImageAssociative> selectImageByAssId(Map<String, Object> params);

    /**
     * 批量添加商品图片
     * @param params
     * @return
     */
    int insertBatch(Map<String, Object> params);

    /**
     * 批量修改商品图片
     * @param imageList
     * @return
     */
    int updateBatch(List<MallImageAssociative> imageList);

    /**
     * 根据关联表id来查询图片
     * @param params
     * @return
     */
    List<Map<String, Object>> selectByAssId(Map<String, Object> params);
}