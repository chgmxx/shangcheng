package com.gt.mall.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.constant.Constants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public class SessionUtils {

    private static final Logger log = Logger.getLogger( SessionUtils.class );

    /**
     * 获取用户bus_user   SESSION的值
     */
    public static BusUser getLoginUser( HttpServletRequest request ) {
	try {
	    Object obj = request.getSession().getAttribute( Constants.SESSION_BUSINESS_KEY );

	    if ( obj != null ) {

		return JSONObject.toJavaObject( ( JSONObject.parseObject( obj.toString() ) ), BusUser.class );
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

    /**
     * 获取session中的t_wx_public_user登录用户
     */
    public static WxPublicUsers getLoginPbUser( HttpServletRequest request ) {
	try {
	    Object obj = request.getSession().getAttribute( Constants.SESSION_WXPUBLICUSERS_KEY );
	    if ( obj != null ) {
		return JSONObject.parseObject( obj.toString(), WxPublicUsers.class );
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

    /**
     * 把店铺列表保存到session
     *
     * @param userId   用户id
     * @param shopList 店铺列表
     */
    public static void setShopListBySession( int userId, List< Map< String,Object > > shopList, HttpServletRequest request ) {
	if ( CommonUtil.isNotEmpty( request ) ) {
	    String sessionKey = Constants.SESSION_KEY + "bus_shop_list_" + userId;
	    request.getSession().setAttribute( sessionKey, JSONArray.toJSONString( shopList ) );
	}

    }

    /**
     * 获取商家的店铺列表
     *
     * @param userId 商家id
     *
     * @return 店铺列表
     */
    public static List< Map > getShopListBySession( int userId, HttpServletRequest request ) {
	if ( CommonUtil.isEmpty( request ) ) {
	    return null;
	}
	String sessionKey = Constants.SESSION_KEY + "bus_shop_list_" + userId;
	Object obj = request.getSession().getAttribute( sessionKey );
	if ( CommonUtil.isEmpty( obj ) ) {
	    return null;
	}
	return JSONArray.parseArray( obj.toString(), Map.class );
    }

    /**
     * 把商家的门店数量存到session中
     *
     * @param userId 商家id
     * @param num    门店数量
     */
    public static void setWxShopNumBySession( int userId, int num, HttpServletRequest request ) {
	String sessionKey = Constants.SESSION_KEY + "wx_shop_num" + userId;
	request.getSession().setAttribute( sessionKey, num );
    }

    /**
     * 从session中获取商家门店的数量
     *
     * @param userId 商家id
     *
     * @return 门店忽略
     */
    public static int getWxShopNumBySession( int userId, HttpServletRequest request ) {
	String sessionKey = Constants.SESSION_KEY + "wx_shop_num" + userId;
	Object object = request.getSession().getAttribute( sessionKey );
	if ( CommonUtil.isNotEmpty( object ) ) {
	    return CommonUtil.toInteger( object );
	}
	return 0;
    }

    /**
     * 从session中获取主账号id
     *
     * @param userId 商家id
     *
     * @return 主账号id
     */
    public static int getAdminUserId( int userId, HttpServletRequest request ) {
	Object object = request.getSession().getAttribute( Constants.SESSION_ADMIN_KEY );
	if ( CommonUtil.isNotEmpty( object ) ) {
	    return CommonUtil.toInteger( object );
	}
	return 0;
    }

    /**
     * 从session 中获取是否已经开通了进销存
     *
     * @param userId 商家总账号id
     *
     * @return 1 开通
     */
    public static int getIsJxc( int userId, HttpServletRequest request ) {
	Object object = request.getSession().getAttribute( Constants.SESSION_KEY + "is_jxc" + userId );
	if ( CommonUtil.isNotEmpty( object ) ) {
	    return CommonUtil.toInteger( object );
	}
	return -1;
    }

    /**
     * 商家是否开通进销存存入session
     *
     * @param userId 商家id
     * @param isJxc  是否开通进销存
     */
    public static void setIsJxc( int userId, int isJxc, HttpServletRequest request ) {
	request.getSession().setAttribute( Constants.SESSION_KEY + "is_jxc" + userId, isJxc );
    }

    /**
     * 从session 中获取是否是管理员
     *
     * @param userId 商家总账号id
     *
     * @return 1 开通
     */
    public static int getIsAdminUser( int userId, HttpServletRequest request ) {
	Object object = request.getSession().getAttribute( Constants.SESSION_KEY + "is_admin_user" + userId );
	if ( CommonUtil.isNotEmpty( object ) ) {
	    return CommonUtil.toInteger( object );
	}
	return -1;
    }

    /**
     * 商家是否是管理员存入session
     *
     * @param userId  商家id
     * @param isAdmin 是否是管理员
     */
    public static void setIsAdminUser( int userId, int isAdmin, HttpServletRequest request ) {
	request.getSession().setAttribute( Constants.SESSION_KEY + "is_admin_user" + userId, isAdmin );
    }

    /**
     * 把商城的店铺id存到session
     *
     * @param shopIdObj 店铺id
     */
    public static void setMallShopId( Object shopIdObj, HttpServletRequest request ) {
	HttpSession session = request.getSession();
	if ( CommonUtil.isNotEmpty( shopIdObj ) ) {
	    if ( CommonUtil.isEmpty( session.getAttribute( Constants.SESSION_KEY + "shopId" ) ) ) {
		session.setAttribute( Constants.SESSION_KEY + "shopId", shopIdObj );
	    } else {
		if ( !session.getAttribute( Constants.SESSION_KEY + "shopId" ).toString().equals( shopIdObj ) ) {
		    session.setAttribute( Constants.SESSION_KEY + "shopId", shopIdObj );
		}
	    }
	}
    }

    /**
     * 获取店铺id
     */
    public static int getMallShopId( HttpServletRequest request ) {
	Object shopIdObj = request.getSession().getAttribute( Constants.SESSION_KEY + "shopId" );
	if ( CommonUtil.isNotEmpty( shopIdObj ) ) {
	    return CommonUtil.toInteger( shopIdObj );
	}
	return 0;
    }

    /**
     * 购物车信息存到session
     *
     * @param obj 购物车内容
     */
    public static void setShopCart( Object obj, HttpServletRequest request ) {
	request.getSession().setAttribute( Constants.SESSION_KEY + "mallShopCart", obj );
    }

    /**
     * 从session取出购物车信息
     */
    public static Object getShopCart( HttpServletRequest request ) {
	return request.getSession().getAttribute( Constants.SESSION_KEY + "mallShopCart" );
    }

}
