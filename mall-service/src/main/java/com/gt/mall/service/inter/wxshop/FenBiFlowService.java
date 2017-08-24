package com.gt.mall.service.inter.wxshop;

import com.gt.mall.bean.BusFlow;
import com.gt.mall.bean.wx.flow.*;

import java.util.List;
import java.util.Map;

/**
 * 粉币流量接口
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:35
 */
public interface FenBiFlowService {

    /**
     * 查询剩余粉币数量(针对某个活动)
     *
     * @param fenbiSurplus 参数
     *
     * @return 数量
     */
    FenBiCount getFenbiSurplus( FenbiSurplus fenbiSurplus );

    /**
     * 根据ID查询流量套餐
     *
     * @param flowId 流量id
     *
     * @return 流量套餐
     */
    WsBusFlowInfo getFlowInfoById( int flowId );

    /**
     * 根据商家id获取商家流量
     *
     * @param busUserId 商家id
     *
     * @return 商家流量
     */
    List< BusFlow > getBusFlowsByUserId( int busUserId );

    /**
     * 保存粉币的冻结记录
     *
     * @param fenbiFlowRecord 记录
     *
     * @return 是否冻结成功，冻结id
     */
    Map< String,Object > saveFenbiFlowRecord( FenbiFlowRecord fenbiFlowRecord );

    /**
     * 根据id查询冻结流量记录
     *
     * @param recordId id
     *
     * @return 流量冻结记录
     */
    WsFenbiFlowRecord getFenbiFlowRecordById( int recordId );

    /**
     * 回滚冻结流量
     *
     * @param recordId jilu id
     *
     * @return true 成功
     */
    boolean rollbackFenbiFlowRecord( int recordId );
}
