package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusFlow;
import com.gt.mall.bean.wx.flow.*;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", fenbiSurplus );
	String result = HttpSignUtil.SignHttpSelect( params, FLOW_URL + "getFenbiSurplus.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), FenBiCount.class );
	}
	return null;
    }

    @Override
    public WsBusFlowInfo getFlowInfoById( int flowId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", flowId );
	String result = HttpSignUtil.SignHttpSelect( params, FLOW_URL + "getFlowInfoById.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsBusFlowInfo.class );
	}
	return null;
    }

    @Override
    public List< BusFlow > getBusFlowsByUserId( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", busUserId );
	String result = HttpSignUtil.SignHttpSelect( params, FLOW_URL + "getBusFlowsByUserId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, BusFlow.class );
	}
	return null;
    }

    @Override
    public Map< String,Object > saveFenbiFlowRecord( FenbiFlowRecord fenbiFlowRecord ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", fenbiFlowRecord );
	//todo 暂时未返回冻结id给我
	return HttpSignUtil.SignHttpInsertOrUpdate( params, FLOW_URL + "saveFenbiFlowRecord.do", 1 );
    }

    @Override
    public WsFenbiFlowRecord getFenbiFlowRecordById( int recordId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", recordId );
	String result = HttpSignUtil.SignHttpSelect( params, FLOW_URL + "getFenbiFlowRecordById.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsFenbiFlowRecord.class );
	}
	return null;
    }

    @Override
    public boolean rollbackFenbiFlowRecord( int recordId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", recordId );
	Map< String,Object > resultMap = HttpSignUtil.SignHttpInsertOrUpdate( params, FLOW_URL + "rollbackFenbiFlowRecord.do", 1 );
	return CommonUtil.toInteger( resultMap.get( "code" ) ) == 1;
    }
}
