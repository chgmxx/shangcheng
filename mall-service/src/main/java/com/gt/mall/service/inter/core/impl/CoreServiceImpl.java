package com.gt.mall.service.inter.core.impl;

import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.core.CoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * C增值服务接口调用
 * User : yangqian
 * Date : 2017/12/23 0023
 * Time : 16:52
 */
@Service
public class CoreServiceImpl implements CoreService {

    private static final String BUS_ADDER = "/8A5DA52E/busAdderApi/";

    @Override
    public Boolean payModel( int busId, String modelStyle ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "busid", busId );
	params.put( "modelStyle", modelStyle );
	//判断商家信息 1是否过期 2公众号是否变更过
	String result = HttpSignUtil.signHttpSelect( params, BUS_ADDER + "passModel", 4 );
	if ( CommonUtil.isNotEmpty( result ) ) {
	    if ( !result.equals( "0" ) ) {
		throw new BusinessException( ResponseEnums.URL_GUOQI_ERROR.getCode(), ResponseEnums.URL_GUOQI_ERROR.getDesc() );
	    }
	}
	return true;
    }
}
