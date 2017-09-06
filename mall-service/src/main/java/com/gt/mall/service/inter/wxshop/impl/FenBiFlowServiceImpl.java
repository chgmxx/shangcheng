package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusFlow;
import com.gt.mall.bean.wx.flow.FenbiFlowRecord;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import com.gt.util.entity.param.fenbiFlow.*;
import com.gt.util.entity.result.fenbi.FenBiCount;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 粉币流量实现类
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 10:35
 */
@Service
public class FenBiFlowServiceImpl implements FenBiFlowService {

    private static final String FLOW_URL = "/8A5DA52E/fenbiflow/6F6D9AD2/79B4DE7C/";

    @Override
    public FenBiCount getFenbiSurplus( FenbiSurplus fenbiSurplus ) {
	String result = HttpSignUtil.signHttpSelect( fenbiSurplus, FLOW_URL + "getFenbiSurplus.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), FenBiCount.class );
	}
	return null;
    }

    @Override
    public WsBusFlowInfo getFlowInfoById( int flowId ) {
	String result = HttpSignUtil.signHttpSelect( flowId, FLOW_URL + "getFlowInfoById.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsBusFlowInfo.class );
	}
	return null;
    }

    @Override
    public List< BusFlow > getBusFlowsByUserId( int busUserId ) {
	String result = HttpSignUtil.signHttpSelect( busUserId, FLOW_URL + "getBusFlowsByUserId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, BusFlow.class );
	}
	return null;
    }

    @Override
    public Map< String,Object > saveFenbiFlowRecord( FenbiFlowRecord fenbiFlowRecord ) {
	return HttpSignUtil.signHttpInsertOrUpdate( fenbiFlowRecord, FLOW_URL + "saveFenbiFlowRecord.do", 2 );
    }

    @Override
    public WsFenbiFlowRecord getFenbiFlowRecordById( int recordId ) {
	String result = HttpSignUtil.signHttpSelect( recordId, FLOW_URL + "getFenbiFlowRecordById.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsFenbiFlowRecord.class );
	}
	return null;
    }

    @Override
    public boolean rollbackFenbiFlowRecord( int recordId ) {
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( recordId, FLOW_URL + "rollbackFenbiFlowRecord.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public boolean adcServices( AdcServicesInfo adcServicesInfo ) {
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( adcServicesInfo, FLOW_URL + "adcServices.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public Map getMobileInfo( ReqGetMobileInfo reqGetMobileInfo ) {
	return HttpSignUtil.signHttpInsertOrUpdate( reqGetMobileInfo, FLOW_URL + "getMobileInfo.do", 2 );
    }
}
