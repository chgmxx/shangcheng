package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseOrderStatistics;

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
public interface PurchaseOrderStatisticsDAO extends BaseMapper< PurchaseOrderStatistics > {

    /**
     * 分页查询
     *
     * @param parms nickname:用户昵称，busId：商家Id,orderId：订单Id，pageFirst:页数,pageLast：记录数
     *
     * @return list
     */
    List< Map< String,Object > > findList( Map< String,Object > parms );

    /**
     * 分页查询的订单条数
     *
     * @param parms nickname:用户昵称，busId：商家Id,orderId：订单Id
     *
     * @return 数量
     */
    Integer findListCount( Map< String,Object > parms );
}
