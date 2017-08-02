package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.constant.Constants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class SessionUtils {

    private static final Logger log = Logger.getLogger( SessionUtils.class );

    /**
     * 获取用户bus_user   SESSION的值
     */
    public static BusUser getLoginUser( HttpServletRequest request ) {
	try {
	    Object obj = request.getSession().getAttribute( Constants.SESSION_BUSINESS_KEY );
	    if ( obj != null ) {
		return JSONObject.parseObject( obj.toString(), BusUser.class );
	    } else {
		return null;
	    }
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
	return null;
    }


    /*
      存入 用户bus_user  的值
     */
   /* public static void setLoginUser( HttpServletRequest request, BusUser busUser ) {
	try {
	    request.getSession().setAttribute( Constants.SESSION_BUSINESS_KEY, JSONObject.toJSON( busUser ) );
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
    }*/

    /*
      设置session中的t_wx_public_user微信信息
     */
    /*public static void setLoginPbUser( HttpServletRequest request, WxPublicUsers wxPublicUsers ) {
	try {
	    request.getSession().setAttribute( Constants.SESSION_WXPUBLICUSERS_KEY, wxPublicUsers );
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
    }*/

    /**
     * 获取session中的t_wx_public_user登录用户
     */
    public static WxPublicUsers getLoginPbUser( HttpServletRequest request ) {
	try {
	    Object obj = request.getSession().getAttribute( Constants.SESSION_WXPUBLICUSERS_KEY );
	    if ( obj != null ) {
		return (WxPublicUsers) obj;
	    } else {
		return null;
	    }

	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * 设置session中的商家member会员信息
     */
    public static void setLoginMember( HttpServletRequest request, Member member ) {
	try {
	    request.getSession().setAttribute( Constants.SESSION_MEMBER_KEY, JSONObject.toJSON( member ) );
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 获取session中的商家会员信息
     */
    public static Member getLoginMember( HttpServletRequest request ) {
	try {
	    Object obj = request.getSession().getAttribute( Constants.SESSION_MEMBER_KEY );
	    if ( obj != null ) {
		return JSONObject.parseObject( obj.toString(), Member.class );
	    } else {
		return null;
	    }

	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();

	}
	return null;
    }

}
