package com.gt.mall.cxf.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.param.fenbiFlow.FenbiSurplus;
import com.gt.mall.bean.param.fenbiFlow.UpdateFenbiReduce;
import com.gt.mall.bean.result.fenbi.FenBiCount;
import com.gt.mall.cxf.service.FenbiFlowService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.CxfFactoryBeanUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.webservice.client.WxmpApiSerivce;
import com.gt.webservice.entity.param.BaseParam;
import com.gt.webservice.util.ParamsSignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 粉币流量实现类  调用CXF
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:19
 */
@Service
public class FenbiFlowServiceImpl implements FenbiFlowService {

    private static Logger logger = LoggerFactory.getLogger( FenbiFlowServiceImpl.class );


    @Override
    public Boolean updateFenbiReduce( UpdateFenbiReduce fenbiReduce ) throws Exception {

	WxmpApiSerivce wxmpApiSerivce = (WxmpApiSerivce) CxfFactoryBeanUtil.crateCxfFactoryBean( WxmpApiSerivce.class, PropertiesUtil.getShopUrl() );

	BaseParam< UpdateFenbiReduce > baseParam = new BaseParam<>();
	baseParam.setAction( "updateFenbiReduce" );
	baseParam.setReqdata( fenbiReduce );
	baseParam.setNonceStr(ParamsSignUtil.CreateNoncestr());
	baseParam.setTimestamp(ParamsSignUtil.create_timestamp());
	String sign = ParamsSignUtil.sign( baseParam,JSONObject.toJSONString( fenbiReduce ) );
	baseParam.setRequestToken( sign );

	String json = wxmpApiSerivce.reInvoke( JSONObject.toJSONString( baseParam ) );
	logger.info( "更改粉币冻结额度的接口 updateFenbiReduce 返回的结果：" + json );

	return CxfFactoryBeanUtil.getBooleanResultByJson( json );
    }

    @Override
    public Double getFenbiSurplus( FenbiSurplus fenbiSurplus ) throws Exception {
	WxmpApiSerivce wxmpApiSerivce = (WxmpApiSerivce) CxfFactoryBeanUtil.crateCxfFactoryBean( WxmpApiSerivce.class, PropertiesUtil.getShopUrl() );
	BaseParam< FenbiSurplus > baseParam = new BaseParam<>();
	baseParam.setAction( "getFenbiSurplus" );
	baseParam.setRequestToken( PropertiesUtil.getWxmpToken() );
	baseParam.setReqdata( fenbiSurplus );

	String json = wxmpApiSerivce.reInvoke( JSONObject.toJSONString( baseParam ) );
	logger.info( "查询剩余粉币数量的接口 getFenbiSurplus 返回的结果：" + json );
	String data = CxfFactoryBeanUtil.getStringResultByJson( json );
	if ( CommonUtil.isNotEmpty( data ) ) {
	    FenBiCount fenBiCount = JSONObject.parseObject( data, FenBiCount.class );
	    if(CommonUtil.isNotEmpty( fenBiCount )){
	        if(fenBiCount.getCount() > 0){
	            return fenBiCount.getCount();
		}
	    }
	}
	return null;
    }

}
