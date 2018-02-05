package com.gt.mall.utils;

import com.gt.mall.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MallRedisUtils {
    private static Logger logger = LoggerFactory.getLogger( MallRedisUtils.class );

    /**
     * 从session中获取商家id
     */
    public static int getUserId() {
        try {
            String key = Constants.REDIS_KEY + "getbusId";
            if ( JedisUtil.exists( key ) ) {
                if ( CommonUtil.isNotEmpty( JedisUtil.get( key ) ) ) {
                    return CommonUtil.toInteger( JedisUtil.get( key ) );
                }
            }
        } catch ( Exception e ) {
            logger.info( e.getLocalizedMessage() );
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 从session中获取商家id
     */
    public static void setUserId( Integer busId ) {
        if ( CommonUtil.isNotEmpty( busId ) && busId > 0 ) {
            String redisKey = Constants.REDIS_KEY + "getbusId";
            JedisUtil.set( redisKey, busId.toString(), Constants.REDIS_SECONDS );
        }
    }

    /**
     * 把商城的店铺id存到session
     *
     * @param shopIdObj 店铺id
     */
    public static int getMallShopId( Object shopIdObj, Integer busId ) {
        String redisKey = Constants.REDIS_KEY + "shopId_" + busId;
        boolean isSave = true;
        if ( JedisUtil.exists( redisKey ) ) {
            Object idObj = JedisUtil.get( redisKey );
            if ( CommonUtil.isNotEmpty( idObj ) ) {
                if ( CommonUtil.isNotEmpty( shopIdObj ) && idObj.toString().equals( shopIdObj.toString() ) ) {
                    isSave = false;
                }
            }
        }
        if ( isSave && CommonUtil.isNotEmpty( shopIdObj ) ) {
            JedisUtil.set( redisKey, shopIdObj.toString(), Constants.REDIS_SECONDS );
        }
        return 0;
    }

    /**
     * 获取商城的店铺id存到session
     */
    public static int getShopId( Integer busId ) {
        String redisKey = Constants.REDIS_KEY + "shopId_" + busId;
        if ( JedisUtil.exists( redisKey ) ) {
            Object idObj = JedisUtil.get( redisKey );
            if ( idObj != null ) {
                //		return CommonUtil.toInteger( idObj );
            }
        }
        return 0;
    }

    public int setIsShopBySession( Integer toShop, Integer shopId, Integer busId ) {
        if ( CommonUtil.isEmpty( toShop ) || CommonUtil.isEmpty( shopId ) || CommonUtil.isEmpty( busId ) ) {
            return 0;
        }
        String redisKey = Constants.REDIS_KEY + "toshop_" + shopId + "_" + busId;
        JedisUtil.set( redisKey, toShop.toString() );
        return toShop;
    }

    public int getIsShopBySession( Integer shopId, Integer busId ) {
        if ( CommonUtil.isEmpty( shopId ) || CommonUtil.isEmpty( busId ) ) {
            return 0;
        }
        String redisKey = Constants.REDIS_KEY + "toshop_" + shopId + "_" + busId;
        if ( JedisUtil.exists( redisKey ) ) {
            if ( CommonUtil.isNotEmpty( JedisUtil.get( redisKey ) ) ) {
                return CommonUtil.toInteger( JedisUtil.get( redisKey ) );
            }
        }
        return 0;
    }

}
