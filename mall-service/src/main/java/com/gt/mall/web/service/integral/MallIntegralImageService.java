package com.gt.mall.web.service.integral;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.integral.MallIntegralImage;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商城图片循环 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallIntegralImageService extends BaseService<MallIntegralImage> {

    /**
     * 通过店铺id来查询积分商城图片
     *
     * @param params userId:用户id，curPage：当前页
     * @return 积分商城图片列表
     */
    PageUtil selectImageByShopId(Map<String, Object> params);

    /**
     * 通过积分商城图片id查询积分商城图片
     */
//    MallIntegralImage selectImageById(Integer id);

    /**
     * 编辑积分商城图片
     *
     * @param params 积分商城图片信息
     * @param userId 用户id
     * @return 是否成功
     */
    boolean editImage(Map<String, Object> params, int userId);

    /**
     * 删除积分商城图片
     *
     * @param params id：图片Id,type:状态
     * @return 是否成功
     */
    boolean deleteImage(Map<String, Object> params);

    /**
     * 查询积分商城的图片
     *
     * @param params userId：用户Id，shopId：店铺id
     * @return list
     */
    List<MallIntegralImage> getIntegralImageByUser(Map<String, Object> params);
}
