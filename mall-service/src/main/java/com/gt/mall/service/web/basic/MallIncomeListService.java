package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallIncomeList;
import com.gt.mall.utils.PageUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 收入记录表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2018-01-30
 */
public interface MallIncomeListService extends BaseService< MallIncomeList > {

    /**
     * 交易记录分页管理
     */
    public PageUtil findByTradePage( Map< String,Object > params );

    /**
     * 导出订单
     *
     * @return
     */
    public HSSFWorkbook exportTradeExcel( Map< String,Object > params, String[] titles, int type, List< Map< String,Object > > shoplist );

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
