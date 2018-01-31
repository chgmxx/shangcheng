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

}
