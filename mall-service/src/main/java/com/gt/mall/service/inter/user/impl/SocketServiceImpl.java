package com.gt.mall.service.inter.user.impl;

import com.gt.mall.service.inter.user.SocketService;
import com.gt.mall.utils.HttpSignUtil;
import com.gt.mall.utils.PropertiesUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * socket操作实现类
 * User : yangqian
 * Date : 2017/8/24 0024
 * Time : 16:46
 */
@Service
public class SocketServiceImpl implements SocketService {

    private static final String SOCKET_URL = "/8A5DA52E/socket/";

    private static final String MQ_UEL = "/8A5DA52E/mq/";

    @Override
    public void getSocketApi( Map< String,Object > params ) {

	HttpSignUtil.signHttpInsertOrUpdate( params, SOCKET_URL + "getSocketApi.do", 1 );
    }

    @Override
    public Map mqSendMessage( String message ) {
	Map< String,Object > params = new HashMap<>();
	params.put( "exchange", PropertiesUtil.getExchange() );
	params.put( "queueName", PropertiesUtil.getQueueName() );
	params.put( "message", message );
	return HttpSignUtil.signHttpInsertOrUpdate( params, MQ_UEL + "getMq.do", 1 );
    }

}
