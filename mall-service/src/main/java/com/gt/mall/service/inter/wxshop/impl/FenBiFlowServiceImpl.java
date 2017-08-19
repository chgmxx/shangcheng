package com.gt.mall.service.inter.wxshop.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusFlow;
import com.gt.mall.bean.wx.flow.WsBusFlowInfo;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.WxHttpSignUtil;
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

    private static final String FLOW_URL = "/8A5DA52E/fenbiflow/79B4DE7C/";

    @Override
    public WsBusFlowInfo getFlowInfoById( int flowId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", flowId );
	String result = WxHttpSignUtil.SignHttpSelect( params, FLOW_URL + "getFlowInfoById.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONObject.toJavaObject( JSONObject.parseObject( result ), WsBusFlowInfo.class );
	}
	return null;
    }

    @Override
    public List< BusFlow > getBusFlowsByUserId( int busUserId ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "reqdata", busUserId );
	String result = WxHttpSignUtil.SignHttpSelect( params, FLOW_URL + "getBusFlowsByUserId.do", 1 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    return JSONArray.parseArray( result, BusFlow.class );
	}
	return null;
    }
}
