package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallCountIncomeDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallCountIncome;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.basic.MallCountIncomeService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 每日收入统计表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-16
 */
@Service
public class MallCountIncomeServiceImpl extends BaseServiceImpl< MallCountIncomeDAO,MallCountIncome > implements MallCountIncomeService {

    @Autowired
    private MallCountIncomeDAO mallCountIncomeDAO;
    @Autowired
    private MallStoreService   mallStoreService;

    @Override
    public Integer saveTurnover( Integer shopId, BigDecimal tradePrice, BigDecimal refundPrice ) {
        logger.info( "进入保存当天营业额方法，接收参数：" + shopId + "tradePrice=" + tradePrice + "refundPrice=" + refundPrice );
        Integer count = 0;
        try {
            if ( tradePrice == null ) {
                tradePrice = CommonUtil.toBigDecimal( 0 );
            }
            if ( refundPrice == null ) {
                refundPrice = CommonUtil.toBigDecimal( 0 );
            }
            if ( tradePrice.doubleValue() > 0 || refundPrice.doubleValue() > 0 ) {
                logger.info( "提交营业额" );
                MallCountIncome income = new MallCountIncome();
                income.setShopId( shopId );
                Date d1 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( DateTimeKit.getDate() );
                income.setCountDate( d1 );
                MallCountIncome countIncome = mallCountIncomeDAO.selectOne( income );
                if ( countIncome != null ) {
                    if ( CommonUtil.isNotEmpty( tradePrice ) ) {
                        countIncome.setTradePrice( countIncome.getTradePrice().add( tradePrice ) );
                    }
                    if ( CommonUtil.isNotEmpty( refundPrice ) ) {
                        countIncome.setRefundPrice( countIncome.getRefundPrice().add( refundPrice ) );
                    }
                    countIncome.setTurnover( countIncome.getTradePrice().subtract( countIncome.getRefundPrice() ) );
                    count = mallCountIncomeDAO.updateById( countIncome );
                } else {
                    MallStore store = mallStoreService.selectById( shopId );
                    countIncome = new MallCountIncome();
                    if ( store != null ) {
                        countIncome.setBusId( store.getStoUserId() );
                    }
                    countIncome.setShopId( CommonUtil.toInteger( shopId ) );
                    countIncome.setCountDate( d1 );
                    countIncome.setTradePrice( tradePrice );
                    countIncome.setRefundPrice( refundPrice );
                    countIncome.setTurnover( tradePrice.subtract( refundPrice ) );

                    count = mallCountIncomeDAO.insert( countIncome );
                }
            }
        } catch ( Exception e ) {
            logger.error( "保存当天营业额异常：" + e.getMessage() );
            e.printStackTrace();
            throw new BusinessException( ResponseEnums.ERROR.getCode(), "保存当天营业额异常" );
        }
        return count;
    }

    /**
     * 当天店铺的营业额
     *
     * @param shopId
     *
     * @return
     */

    @Override
    public Double getTodayCount( List< Map< String,Object > > shoplist, Integer shopId ) {
        Double incomeCount = 0d;
        try {
            Map< String,Object > params = new HashMap<>();
            Date d1 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( DateTimeKit.getDate() );
            params.put( "date", d1 );
            params.put( "type", "2" );
            if ( shopId != null ) {
                params.put( "shopId", shopId );
            } else {
                params.put( "shoplist", shoplist );
            }
            String price = getCountByTimes( params );
            incomeCount = CommonUtil.toDouble( price );
        } catch ( Exception e ) {
            logger.error( "获取当天店铺的营业额异常：" + e.getMessage() );
            e.printStackTrace();
            throw new BusinessException( ResponseEnums.ERROR.getCode(), "获取当天店铺的营业额异常" );
        }
        return incomeCount;
    }

    @Override
    public String getCountByTimes( Map< String,Object > params ) {

        return mallCountIncomeDAO.getCountByTimes( params );
    }

    @Override
    public List< Map< String,Object > > getCountListByTimes( Map< String,Object > params ) {

        return mallCountIncomeDAO.getCountListByTimes( params );
    }
}
