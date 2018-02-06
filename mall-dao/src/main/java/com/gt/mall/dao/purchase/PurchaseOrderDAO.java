package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseOrder;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
public interface PurchaseOrderDAO extends BaseMapper< PurchaseOrder > {

    /**
     * 分页查询
     *
     * @param parms busId：商家id，startTime：开始时间，endTime：结束时间，status：状态，
     *              search：订单号，pageFirst:页数,pageLast：记录数
     *
     * @return list
     */
    List< Map< String,Object > > findList( Map< String,Object > parms );

    /**
     * 分页查询的订单条数
     *
     * @param parms busId：商家id，startTime：开始时间，endTime：结束时间，status：状态，search：订单号
     *
     * @return 数量
     */
    Integer findListCount( Map< String,Object > parms );
}
