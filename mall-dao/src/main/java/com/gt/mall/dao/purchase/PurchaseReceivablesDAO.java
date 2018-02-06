package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseReceivables;
import org.apache.ibatis.annotations.Param;

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
public interface PurchaseReceivablesDAO extends BaseMapper< PurchaseReceivables > {

    /**
     * 查询该订单的收款记录
     *
     * @param orderId 订单Id
     *
     * @return list
     */
    List< Map< String,Object > > findReceivablesList( int orderId );

    /**
     * 分页查询的订单的收款条数
     *
     * @param parms
     *
     * @return 数量
     */
    Integer findListCount( Map< String,Object > parms );

    /**
     * 分页查询
     *
     * @param parms
     *
     * @return list
     */
    List< Map< String,Object > > findList( Map< String,Object > parms );

    /**
     * 根据订单号查询收款记录
     *
     * @param receivablesNumber 收款订单号
     *
     * @return 收款记录
     */
    PurchaseReceivables selectReceivable( @Param( "receivablesNumber" ) String receivablesNumber );
}
