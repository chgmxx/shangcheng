package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallCountIncomeDAO;
import com.gt.mall.entity.basic.MallCountIncome;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.service.web.basic.MallCountIncomeService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public Integer getTodayCount( List< Map< String,Object > > shoplist, Integer shopId ) {
	Integer incomeCount = 0;

	if ( shopId != null ) {
	    incomeCount += todayCount( shopId );
	} else {
	    for ( Map< String,Object > shopMaps : shoplist ) {
		int id = CommonUtil.toInteger( shopMaps.get( "id" ) );
		incomeCount += todayCount( id );
	    }
	}
	return incomeCount;
    }

    /**
     * 当天店铺的营业额
     *
     * @param shopId
     *
     * @return
     */
    public Integer todayCount( Integer shopId ) {
	String tradeKey = Constants.REDIS_KEY + "todayTradeCount-" + shopId;//当天交易金额
	String refundKey = Constants.REDIS_KEY + "todayRefundCount-" + shopId;//当天退款金额
	Integer tradePrice = 0;
	Integer refundPrice = 0;
	if ( JedisUtil.exists( tradeKey ) ) {
	    Object obj = JedisUtil.get( tradeKey );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		tradePrice = CommonUtil.toInteger( obj );
	    }
	}
	if ( JedisUtil.exists( refundKey ) ) {
	    Object obj = JedisUtil.get( refundKey );
	    if ( CommonUtil.isNotEmpty( obj ) ) {
		refundPrice = CommonUtil.toInteger( obj );
	    }
	}

	return tradePrice - refundPrice;
    }

    @Override
    public Integer getCountByTimes( Map< String,Object > params ) {

	return mallCountIncomeDAO.getCountByTimes( params );
    }

    @Override
    public List< Map< String,Object > > getCountListByTimes( Map< String,Object > params ) {

	return mallCountIncomeDAO.getCountListByTimes( params );
    }
}
