package com.gt.mall.service.inter.user;

import java.util.Map;

/**
 * socket接口
 * User : yangqian
 * Date : 2017/8/24 0024
 * Time : 16:46
 */
public interface SocketService {
    /**
     * 推送消息
     *
     * @param params 参数
     */
    void getSocketApi( Map< String,Object > params );

    /**
     * MQ 消息队列发送消息
     * @param message 消息
     */
    //    Map mqSendMessage(String message);
}
