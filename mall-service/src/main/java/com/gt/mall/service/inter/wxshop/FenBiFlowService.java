package com.gt.mall.service.inter.wxshop;

import com.gt.mall.bean.BusFlow;
import com.gt.mall.bean.wx.flow.WsBusFlowInfo;

import java.util.List;

/**
 * 粉币流量接口
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:35
 */
public interface FenBiFlowService {

    /**
     * 根据ID查询流量套餐
     * @param flowId 流量id
     * @return 流量套餐
     */
    WsBusFlowInfo getFlowInfoById(int flowId);

    /**
     * 根据商家id获取商家流量
     * @param busUserId 商家id
     * @return 商家流量
     */
    List<BusFlow> getBusFlowsByUserId(int busUserId);
}
