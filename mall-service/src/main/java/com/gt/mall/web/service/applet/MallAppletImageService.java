package com.gt.mall.web.service.applet;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.util.PageUtil;

import java.util.Map;

/**
 * <p>
 * 小程序图片表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAppletImageService extends BaseService<MallAppletImage> {

    /**
     * 通过店铺id来查询小程序
     *
     * @param params userId：用户Id,curPage:当前页
     * @return 列表
     */
    PageUtil selectImageByShopId(Map<String, Object> params);

    /**
     * 通过小程序id查询小程序图片
     *
     * @param id 图片Id
     * @return 图片信息
     */
    Map<String, Object> selectImageById(Integer id);

    /**
     * 编辑小程序图片
     *
     * @param params 小程序图片信息
     * @param userId 用户Id
     * @return boolean
     */
    boolean editImage(Map<String, Object> params, int userId);

    /**
     * 删除小程序图片
     *
     * @param params id:图片id,type:类型
     * @return 是否成功
     */
    boolean deleteImage(Map<String, Object> params);
}
