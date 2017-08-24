package com.gt.mall.service.inter.user.impl;

import com.gt.mall.service.inter.user.SocketService;
import com.gt.mall.util.HttpSignUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * socket操作实现类
 * User : yangqian
 * Date : 2017/8/24 0024
 * Time : 16:46
 */
@Service
public class SocketServiceImpl implements SocketService {

    public static final String SOCKET_URL = "/8A5DA52E/socket/";

    @Override
    public void getSocketApi( Map< String,Object > params ) {

	HttpSignUtil.SignHttpInsertOrUpdate( params, SOCKET_URL + "getSocketApi.do", 1 );
    }
}
