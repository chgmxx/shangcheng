package com.gt.mall.web.service.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 到店自提表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallTakeTheirService extends BaseService< MallTakeTheir > {

    /**
     * 根据用户id来查询商品
     *
     * @Title: selectByUserId
     */
    PageUtil selectByUserId(Map<String, Object> params);

    /**
     * 删除上门自提
     *
     * @Title: batchUpdateTake
     */
    boolean deleteTake(Map<String, Object> params);


    /**
     * 编辑上门自提
     *
     * @Title: editTake
     */
    boolean editTake(Map<String, Object> params, BusUser user);

    /**
     * 查询上门自提集合
     * @param param
     * @return
     */
    List<MallTakeTheir> selectListByUserId(Map<String, Object> param);

    /**
     * 根据上门自提id查询上门自提信息
     * @param params
     * @return
     */
    MallTakeTheir selectById(Map<String, Object> params);

    /**
     * 查询用户是否允许使用上门自提
     * @param userId
     * @return true 允许  false 不允许
     */
    boolean isTakeTheirByUserId(int userId);

    /**
     * 根据公众号id查询上门自提信息
     * @param map
     * @return
     */
    List<MallTakeTheir> selectByBusUserId(Map<String, Object> map);
}
