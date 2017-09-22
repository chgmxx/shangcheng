package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.RequestUtils;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
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
	RequestUtils< FenbiSurplus > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( fenbiSurplus );
	String result = HttpSignUtil.signHttpSelect( requestUtils, FLOW_URL + "getFenbiSurplus.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), FenBiCount.class );
	}
	return null;
    }

    @Override
    public FenbiFlowRecord getFenbiFlowRecord( FenbiSurplus fenbiSurplus ) {
	RequestUtils< FenbiSurplus > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( fenbiSurplus );
	String result = HttpSignUtil.signHttpSelect( requestUtils, FLOW_URL + "getFenbiFlowRecord.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), FenbiFlowRecord.class );
	}
	return null;
    }

    @Override
    public BusFlowInfo getFlowInfoById( int flowId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( flowId );
	String result = HttpSignUtil.signHttpSelect( requestUtils, FLOW_URL + "getFlowInfoById.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), BusFlowInfo.class );
	}
	return null;
    }

    @Override
    public List< BusFlow > getBusFlowsByUserId( int busUserId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( busUserId );
	String result = HttpSignUtil.signHttpSelect( requestUtils, FLOW_URL + "getBusFlowsByUserId.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, BusFlow.class );
	}
	return null;
    }

    @Override
    public Map< String,Object > saveFenbiFlowRecord( FenbiFlowRecord fenbiFlowRecord ) {
	RequestUtils< FenbiFlowRecord > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( fenbiFlowRecord );
	return HttpSignUtil.signHttpInsertOrUpdate( requestUtils, FLOW_URL + "saveFenbiFlowRecord.do", 2 );
    }

    @Override
    public FenbiFlowRecord getFenbiFlowRecordById( int recordId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( recordId );
	String result = HttpSignUtil.signHttpSelect( requestUtils, FLOW_URL + "getFenbiFlowRecordById.do", 2 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), FenbiFlowRecord.class );
	}
	return null;
    }

    @Override
    public boolean rollbackFenbiFlowRecord( int recordId ) {
	RequestUtils< Integer > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( recordId );
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, FLOW_URL + "rollbackFenbiFlowRecord.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public boolean adcServices( AdcServicesInfo adcServicesInfo ) {
	RequestUtils< AdcServicesInfo > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( adcServicesInfo );
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, FLOW_URL + "adcServices.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }

    @Override
    public Map getMobileInfo( ReqGetMobileInfo reqGetMobileInfo ) {
	RequestUtils< ReqGetMobileInfo > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( reqGetMobileInfo );
	return HttpSignUtil.signHttpInsertOrUpdate( requestUtils, FLOW_URL + "getMobileInfo.do", 2 );
    }

    @Override
    public boolean updaterecUseCountVer2( UpdateFenbiReduce updateFenbiReduce ) {
	RequestUtils< UpdateFenbiReduce > requestUtils = new RequestUtils<>();
	requestUtils.setReqdata( updateFenbiReduce );
	Map< String,Object > resultMap = HttpSignUtil.signHttpInsertOrUpdate( requestUtils, FLOW_URL + "updaterecUseCountVer2.do", 2 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }
}
