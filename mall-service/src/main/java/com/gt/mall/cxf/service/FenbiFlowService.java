package com.gt.mall.cxf.service;

import com.gt.mall.bean.param.fenbiFlow.FenbiSurplus;
import com.gt.mall.bean.param.fenbiFlow.UpdateFenbiReduce;

/**
 * 粉币流量接口（实现类会调用CXF接口）
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:14
 */
public interface FenbiFlowService {

    /**
     * 更改粉币冻结额度
     *
     * @param fenbiReduce 更改粉币冻结对象 (busId :商家ID ,count :冻结数量, fkId :外键id, freType :活动类型 )
     *
     * @return true 更改成功  false 更改失败
     */
    public Boolean updateFenbiReduce( UpdateFenbiReduce fenbiReduce ) throws Exception;

    /**
     * 2、查询剩余粉币数量(针对某个活动)
     * @param fenbiSurplus 活动对象 （busId：商家id，rec_type：冻结类型 粉币:1,流量:2    ，fkId：外键id  ，fre_type：活动类型）
     * @return 粉币数量
     * @throws Exception 异常
     */
    public Double getFenbiSurplus(FenbiSurplus fenbiSurplus ) throws Exception;

}
