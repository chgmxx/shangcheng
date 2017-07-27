package com.gt.mall.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 *  Properties 读取工具（注解方式获取application.yml文件的配置参数）
 * User : yangqian
 * Date : 2017/7/19 0019
 * Time : 15:11
 */
@Component      //不加这个注解的话, 使用@Autowired 就不能注入进去了
@Configuration
public class MyConfigUtil {

    private static String homeUrl;//网页地址

    private static String imageUrlPrefix;//图片地址

    private static String jxcUrl;//进销存 地址

    private static String jxcAccount;//进销存token用户名

    private static String jxcPwd;//进销存token密码

    private static String articleUrl;//访问文章资源URL

    private static String staticSourceFtpIp;//图片资源Ftp IP

    private static String staticSourceFtpPort;//图片资源Ftp 端口

    private static String staticSourceFtpUser;//图片资源Ftp 用户

    private static String staticSourceFtpPwd;  //图片资源Ftp 密码

    private static String shopUrl;

    private static String wxmpToken;

    private static String redisHost;

    private static String redisPort;

    private static String redisPassword;

    @Value( "${web.homeUrl}" )
    public void setHomeUrl( String homeUrl ) {
        MyConfigUtil.homeUrl = homeUrl;
    }

    @Value( "${web.imageUrlPrefix}" )
    public void setImageUrlPrefix( String imageUrlPrefix ) {
        MyConfigUtil.imageUrlPrefix = imageUrlPrefix;
    }

    @Value( "${web.jxcUrl}" )
    public void setJxcUrl( String jxcUrl ) {
        MyConfigUtil.jxcUrl = jxcUrl;
    }

    @Value( "${web.jxcAccount}" )
    public void setJxcAccount( String jxcAccount ) {
        MyConfigUtil.jxcAccount = jxcAccount;
    }

    @Value( "${web.jxcPwd}" )
    public void setJxcPwd( String jxcPwd ) {
        MyConfigUtil.jxcPwd = jxcPwd;
    }

    @Value( "${article.url.prefix}" )
    public void setArticleUrl( String articleUrl ) {
        MyConfigUtil.articleUrl = articleUrl;
    }

    @Value( "${static.source.ftp.ip}" )
    public void setStaticSourceFtpIp( String staticSourceFtpIp ) {
        MyConfigUtil.staticSourceFtpIp = staticSourceFtpIp;
    }

    @Value( "${static.source.ftp.port}" )
    public void setStaticSourceFtpPort( String staticSourceFtpPort ) {
        MyConfigUtil.staticSourceFtpPort = staticSourceFtpPort;
    }

    @Value( "${static.source.ftp.user}" )
    public void setStaticSourceFtpUser( String staticSourceFtpUser ) {
        MyConfigUtil.staticSourceFtpUser = staticSourceFtpUser;
    }

    @Value( "${static.source.ftp.password}" )
    public void setStaticSourceFtpPwd( String staticSourceFtpPwd ) {
        MyConfigUtil.staticSourceFtpPwd = staticSourceFtpPwd;
    }

    @Value( "${project.shop.cxf-url}" )
    public void setShopUrl( String shopUrl ) {
        MyConfigUtil.shopUrl = shopUrl;
    }

    @Value( "${wxmp.token}" )
    public void setWxmpToken( String wxmpToken ) {
        MyConfigUtil.wxmpToken = wxmpToken;
    }

    @Value( "${spring.redis.host}" )
    public void setRedisHost( String redisHost ) {
        MyConfigUtil.redisHost = redisHost;
    }

    @Value( "${spring.redis.port}" )
    public void setRedisPort( String redisPort ) {
        MyConfigUtil.redisPort = redisPort;
    }

    @Value( "${spring.redis.password}" )
    public void setRedisPassword( String redisPassword ) {
        MyConfigUtil.redisPassword = redisPassword;
    }

    public static String getHomeUrl() {
        return homeUrl;
    }

    public static String getImageUrlPrefix() {
        return imageUrlPrefix;
    }

    public static String getJxcUrl() {
        return jxcUrl;
    }

    public static String getJxcAccount() {
        return jxcAccount;
    }

    public static String getJxcPwd() {
        return jxcPwd;
    }

    public static String getArticleUrl() {
        return articleUrl;
    }

    public static String getStaticSourceFtpIp() {
        return staticSourceFtpIp;
    }

    public static String getStaticSourceFtpPort() {
        return staticSourceFtpPort;
    }

    public static String getStaticSourceFtpUser() {
        return staticSourceFtpUser;
    }

    public static String getStaticSourceFtpPwd() {
        return staticSourceFtpPwd;
    }

    public static String getShopUrl() {
        return shopUrl;
    }

    public static String getWxmpToken() {
        return wxmpToken;
    }

    public static String getRedisHost() {
        return redisHost;
    }

    public static String getRedisPort() {
        return redisPort;
    }

    public static String getRedisPassword() {
        return redisPassword;
    }
}
