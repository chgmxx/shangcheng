package com.gt.mall.dao.applet;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.applet.MallAppletImage;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序图片表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallAppletImageDAO extends BaseMapper<MallAppletImage> {

    /**
     * 统计小程序图片
     *
     * @param params userId：用户Id
     * @return 数量
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询小程序图片
     *
     * @param params userId：用户Id，firstNum：页数，maxNum 数量
     * @return 列表
     */
    List<MallAppletImage> selectByPage(Map<String, Object> params);

    /**
     * 查询小程序图片
     *
     * @param params userId：用户Id
     * @return list
     */
    List<MallAppletImage> selectByImage(Map<String, Object> params);
}