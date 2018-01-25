package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallCountIncome;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日收入统计表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-16
 */
public interface MallCountIncomeService extends BaseService< MallCountIncome > {

    /**
     * 保存当天营业额
     *
     * @param shopId
     * @param tradePrice
     * @param refundPrice
     */
    Integer saveTurnover( Integer shopId, BigDecimal tradePrice, BigDecimal refundPrice );

    /**
     * 获取今天的营业额
     *
     * @return
     */
    Double getTodayCount( List< Map< String,Object > > shoplist, Integer shopId );

    /**
     * 获取时间段内的统计金额
     *
     * @param params type 类型 1收入金额 2营业额  （shoplist 店铺列表 或 shopId 店铺ID）   （endDate 结束时间 startDate 开始时间 或  date：日期）
     *
     * @return
     */
    String getCountByTimes( Map< String,Object > params );

    /**
     * 获取时间段内的收入金额列表
     *
     * @param params shoplist 店铺列表 shopId 店铺ID endDate 结束时间 startDate 开始时间
     *
     * @return
     */
    List< Map< String,Object > > getCountListByTimes( Map< String,Object > params );
}
