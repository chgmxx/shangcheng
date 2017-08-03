package com.gt.mall.base;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpSession;

/**
 * BaseController
 *
 * @author zhangmz
 * @create 2017/7/10
 */
public abstract class BaseController {
    /**
     * 日志
     */
    protected Logger logger = Logger.getLogger(BaseController.class);

    /**
     * 获取Sessionid
     *
     * @param session HttpSession
     *
     */
    public String getSessionId( HttpSession session ) {
	return session.getId();
    }


}
