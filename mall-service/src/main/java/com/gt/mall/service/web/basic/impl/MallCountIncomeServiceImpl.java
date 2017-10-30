package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallCountIncomeDAO;
import com.gt.mall.entity.basic.MallCountIncome;
import com.gt.mall.service.web.basic.MallCountIncomeService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void incomeJedis( Integer shopId, Integer tradePrice, Integer refundPrice ) {
	Integer totalTradePrice = 0;
	Integer totalRefundPrice = 0;
	String key = Constants.REDIS_KEY + Constants.INCOME_COUNT_KEY;

	if ( JedisUtil.hExists( key, shopId.toString() ) ) {
	    List< String > maps = JedisUtil.hmgetByKeys( key, shopId.toString() );
	    if ( CommonUtil.isNotEmpty( maps ) ) {
		JSONObject obj = JSONObject.parseObject( maps.get( 0 ) );
		totalTradePrice = obj.getInteger( "tradePrice" );
		totalRefundPrice = obj.getInteger( "refundPrice" );
	    }
	}

	net.sf.json.JSONObject objs = new net.sf.json.JSONObject();
	objs.put( "tradePrice", totalTradePrice + tradePrice );
	objs.put( "refundPrice", totalRefundPrice + refundPrice );
	JedisUtil.map( key, shopId + "", objs.toString() );
    }

    /**
     * 当天店铺的营业额
     *
     * @param shopId
     *
     * @return
     */

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

    public Integer todayCount( Integer shopId ) {
	String key = Constants.REDIS_KEY + Constants.INCOME_COUNT_KEY;
	Integer tradePrice = 0;
	Integer refundPrice = 0;
	if ( JedisUtil.hExists( key, shopId.toString() ) ) {
	    List< String > maps = JedisUtil.hmgetByKeys( key, shopId.toString() );
	    if ( CommonUtil.isNotEmpty( maps ) ) {
		JSONObject obj = JSONObject.parseObject( maps.get( 0 ) );
		if ( CommonUtil.isNotEmpty( obj ) ) {
		    refundPrice = obj.getInteger( "refundPrice" );
		    tradePrice = obj.getInteger( "tradePrice" );
		}
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
