package com.gt.mall.config;

import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.CookieHttpSessionStrategy;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.filter.DelegatingFilterProxy;
import redis.clients.jedis.JedisPoolConfig;

/**
 * RedisSession配置Config
 *
 * @author zhangmz
 * @version 1.0.0
 * @date 2017/07/16
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {

    /** 日志 */
    private static final Logger LOG                          = LoggerFactory.getLogger( RedisSessionConfig.class );
    //maxInactiveIntervalInSeconds session超时时间,单位秒
    private              int    maxInactiveIntervalInSeconds = 180;

    /**
     * 配置Cookie 作用域
     *
     * @return
     */
    @Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
	DefaultCookieSerializer cookieSerializer = new DefaultCookieSerializer();
	//        cookieSerializer.setDomainName( ".example.com" );
	cookieSerializer.setCookieName( "JSESSIONID" );
	cookieSerializer.setCookiePath( "/" );
	return cookieSerializer;
    }

}
