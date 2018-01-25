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
	Integer count = 0;
	try {
	    if ( ( tradePrice != null && tradePrice.doubleValue() > 0 ) || ( refundPrice != null && refundPrice.doubleValue() > 0 ) ) {
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
		    count = mallCountIncomeDAO.updateById( countIncome );
		} else {
		    MallStore store = mallStoreService.selectById( shopId );
		    countIncome = new MallCountIncome();
		    if ( store != null ) {
			countIncome.setBusId( store.getStoUserId() );
		    }
		    countIncome.setShopId( CommonUtil.toInteger( shopId ) );
		    countIncome.setCountDate( new Date() );
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
    public String getCountByTimes( Map< String,Object > params ) {

	return mallCountIncomeDAO.getCountByTimes( params );
    }

    @Override
    public List< Map< String,Object > > getCountListByTimes( Map< String,Object > params ) {

	return mallCountIncomeDAO.getCountListByTimes( params );
    }
}
