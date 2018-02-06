package com.gt.mall.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.api.util.SessionUtils;
import com.gt.mall.constant.Constants;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public class MallSessionUtils {

    private static final Logger log = Logger.getLogger( MallSessionUtils.class );

    /**
     * 获取用户bus_user   SESSION的值
     */
    public static BusUser getLoginUser( HttpServletRequest request ) {
        return SessionUtils.getLoginUser( request );

    }

    /*
      存入 用户bus_user  的值
     */
    public static void setLoginUser( HttpServletRequest request, BusUser busUser ) {
	try {
	    request.getSession().setAttribute( Constants.SESSION_BUSINESS_KEY, JSONObject.toJSON( busUser ) );
	} catch ( Exception e ) {
	    log.info( e.getLocalizedMessage() );
	    e.printStackTrace();
	}
    }

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
     * 获取session中的商家会员信息
     */
    public static Member getLoginMember( HttpServletRequest request, int busId ) {
        if ( busId > 0 ) {
            MallRedisUtils.setUserId( busId );
        }
        return SessionUtils.getLoginMember( request, busId );
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
        if ( CommonUtil.isEmpty( request ) ) {
            return;
        }
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
        if ( CommonUtil.isEmpty( request ) ) {
            return 0;
        }
        String sessionKey = Constants.SESSION_KEY + "wx_shop_num" + userId;
        Object object = request.getSession().getAttribute( sessionKey );
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

    public static void setSession( Object obj, HttpServletRequest request, String key ) {
        request.getSession().setAttribute( Constants.SESSION_KEY + key, obj );
    }

    public static Object getSession( HttpServletRequest request, String key ) {
        return request.getSession().getAttribute( Constants.SESSION_KEY + key );
    }

    public static void removeSession( HttpServletRequest request, String key ) {
        request.getSession().removeAttribute( Constants.SESSION_KEY + key );
    }

}
