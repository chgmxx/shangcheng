package com.gt.mall.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * @author 李逢喜
 * @version 创建时间：2015年9月7日 下午7:14:20
 */
public class MultipleJedisUtil {
    private static JedisPool pool = null;

    public static JedisPool getPool( String ip, String pwd ) {
	if ( pool == null ) {
	    JedisPoolConfig config = new JedisPoolConfig();
	    //控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
	    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	    config.setMaxTotal( 500 );
	    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
	    config.setMaxIdle( 100 );
	    //表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
	    config.setMaxWaitMillis( 1000 * 100 );
	    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	    config.setTestOnBorrow( true );
	    //            if(pwd != null && !pwd.equals("")){
	    //                pool = new JedisPool(config,ip, 6379,60000,pwd);
	    //            }else{
	    //                pool = new JedisPool(config,ip, 6379,60000);
	    //            }

	    pool = new JedisPool( config, ip, 6379, 60000, pwd );
	}
	return pool;
    }

    /**
     * 返还到连接池
     *
     * @param pool
     * @param redis
     */
    public static void returnResource( JedisPool pool, Jedis redis ) {
	if ( redis != null ) {
	    pool.returnResource( redis );
	}
    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public static void set( String key, String value, String ip, String pwd ) {

	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    jedis.set( key, value );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}

    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param ip      NX XX
     * @param seconds 时间单位 ex:秒 px:毫秒
     * @param pwd
     */
    public static void set( String key, String value, int seconds, String ip, String pwd ) {
	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    jedis.setex( key, seconds, value );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}

    }

    /**
     * 设置缓存
     *
     * @param key
     * @param value
     * @param nxxx  NX XX
     */
    public static void set( String key, String value, String nxxx, String ip, String pwd ) {
	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    jedis.set( key, value, nxxx );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}
    }

    /**
     * 读取缓存
     *
     * @param key
     *
     * @return
     */
    public static String get( String key, String ip, String pwd ) {
	String value = null;

	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    value = jedis.get( key );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}

	return value;

    }

    /**
     * 删除缓存
     *
     * @param key
     *
     * @return
     */
    public static Long del( String key, String ip, String pwd ) {

	JedisPool pool = null;
	Jedis jedis = null;
	Long l = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    l = jedis.del( key );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}
	//		if(jedisPool==null||shardedJedisPool==null){
	//			init();
	//		}
	return l;
    }

    /**
     * 存储成map对象，对应的key中添加对应的键值对值
     *
     * @param key
     * @param field
     * @param value
     */
    public static void map( String key, String field, String value, String ip, String pwd ) {

	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    jedis.hset( key, field, value );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}

    }

    /**
     * 根据key，同时根据map当中的key删除数据
     *
     * @param key
     * @param field
     */
    public static void mapdel( String key, String field, String ip, String pwd ) {
	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    jedis.hdel( key, field );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}
    }

    /**
     * 查看map对象数据
     *
     * @param key
     * @param field
     *
     * @return
     */
    public static String maoget( String key, String field, String ip, String pwd ) {
	String value = null;
	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    value = jedis.hget( key, field );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}
	return value;
    }

    /**
     * 检查给定 key 是否存在。
     *
     * @param key
     */
    public static boolean exists( String key, String ip, String pwd ) {

	boolean flag = false;
	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    flag = jedis.exists( key );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}
	return flag;
    }

    /**
     * 检查给定 key对应的field 是否存在。
     *
     * @param key
     */
    public static boolean hExists( String key, String field, String ip, String pwd ) {

	boolean flag = false;
	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    flag = jedis.hexists( key, field );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}
	return flag;
    }

    /**
     * 查看map所有对象数据。
     *
     * @param key
     */
    public static Map< String,String > mapGetAll( String key, String ip, String pwd ) {

	Map< String,String > map = null;
	JedisPool pool = null;
	Jedis jedis = null;
	try {
	    pool = getPool( ip, pwd );
	    jedis = pool.getResource();
	    map = jedis.hgetAll( key );
	} catch ( Exception e ) {
	    //释放redis对象
	    pool.returnBrokenResource( jedis );
	    e.printStackTrace();
	} finally {
	    //返还到连接池
	    returnResource( pool, jedis );
	}
	return map;
    }

    public static void main( String[] args ) {
	System.out.println( MultipleJedisUtil.get( "", "127.0.0.1", "" ) );

    }
}
