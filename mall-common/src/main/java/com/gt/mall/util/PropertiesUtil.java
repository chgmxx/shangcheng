package com.gt.mall.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Properties 读取工具（注解方式获取application.yml文件的配置参数）
 * User : yangqian
 * Date : 2017/7/19 0019
 * Time : 15:11
 */
@Configuration
public class PropertiesUtil {

    private static String domain;//获取域名

    private static String homeUrl;//网页地址

    private static String jxcUrl;//进销存 地址

    private static String jxcAccount;//进销存token用户名

    private static String jxcPwd;//进销存token密码

    private static String articleUrl;//访问文章资源URL

    private static String staticSourceFtpIp;//图片资源Ftp IP

    private static String staticSourceFtpPort;//图片资源Ftp 端口

    private static String staticSourceFtpUser;//图片资源Ftp 用户

    private static String staticSourceFtpPwd;  //图片资源Ftp 密码

    private static String shopUrl;//cxf 调用门店的地址

    private static String wxmpToken;//cxf 调用门店的token

    private static String redisHost;// redis IP

    private static String redisPort;// redis 端口

    private static String redisPassword;// redis 密码

    private static String resImagePath;//获取图片存放路径

    private static String resourceUrl;//访问资源URL

    private static String dbname;//电信流量订单前缀

    private static String exchange;//队列转换器

    private static String queueName;//队列名称

    @Value( "${web.domain}" )
    public void setDomain( String domain ) {
	PropertiesUtil.domain = domain;
    }

    @Value( "${web.homeUrl}" )
    public void setHomeUrl( String homeUrl ) {
	PropertiesUtil.homeUrl = homeUrl;
    }

    @Value( "${web.jxcUrl}" )
    public void setJxcUrl( String jxcUrl ) {
	PropertiesUtil.jxcUrl = jxcUrl;
    }

    @Value( "${web.jxcAccount}" )
    public void setJxcAccount( String jxcAccount ) {
	PropertiesUtil.jxcAccount = jxcAccount;
    }

    @Value( "${web.jxcPwd}" )
    public void setJxcPwd( String jxcPwd ) {
	PropertiesUtil.jxcPwd = jxcPwd;
    }

    @Value( "${article.url.prefix}" )
    public void setArticleUrl( String articleUrl ) {
	PropertiesUtil.articleUrl = articleUrl;
    }

    @Value( "${static.source.ftp.ip}" )
    public void setStaticSourceFtpIp( String staticSourceFtpIp ) {
	PropertiesUtil.staticSourceFtpIp = staticSourceFtpIp;
    }

    @Value( "${static.source.ftp.port}" )
    public void setStaticSourceFtpPort( String staticSourceFtpPort ) {
	PropertiesUtil.staticSourceFtpPort = staticSourceFtpPort;
    }

    @Value( "${static.source.ftp.user}" )
    public void setStaticSourceFtpUser( String staticSourceFtpUser ) {
	PropertiesUtil.staticSourceFtpUser = staticSourceFtpUser;
    }

    @Value( "${static.source.ftp.password}" )
    public void setStaticSourceFtpPwd( String staticSourceFtpPwd ) {
	PropertiesUtil.staticSourceFtpPwd = staticSourceFtpPwd;
    }

    @Value( "${project.shop.cxf-url}" )
    public void setShopUrl( String shopUrl ) {
	PropertiesUtil.shopUrl = shopUrl;
    }

    @Value( "${wxmp.token}" )
    public void setWxmpToken( String wxmpToken ) {
	PropertiesUtil.wxmpToken = wxmpToken;
    }

    @Value( "${spring.redis.host}" )
    public void setRedisHost( String redisHost ) {
	PropertiesUtil.redisHost = redisHost;
    }

    @Value( "${spring.redis.port}" )
    public void setRedisPort( String redisPort ) {
	PropertiesUtil.redisPort = redisPort;
    }

    @Value( "${spring.redis.password}" )
    public void setRedisPassword( String redisPassword ) {
	PropertiesUtil.redisPassword = redisPassword;
    }

    @Value( "${res.image.path}" )
    public void setResImagePath( String resImagePath ) {
	PropertiesUtil.resImagePath = resImagePath;
    }

    @Value( "${resource.url.prefix}" )
    public void setResourceUrl( String resourceUrl ) {
	PropertiesUtil.resourceUrl = resourceUrl;
    }

    @Value( "${dianxin.order}" )
    public void setDbname( String dbname ) {
	PropertiesUtil.dbname = dbname;
    }

    @Value( "${mq.exchange}" )
    public void setExchange( String exchange ) {
	PropertiesUtil.exchange = exchange;
    }

    @Value( "${mq.exchange}" )
    public void setQueueName( String queueName ) {
	PropertiesUtil.queueName = queueName;
    }

    /**
     * 获取域名
     */
    public static String getDomain() {
	return domain;
    }

    public static String getDbname() {
	return dbname;
    }

    public static String getExchange() {
	return exchange;
    }

    public static String getQueueName() {
	return queueName;
    }

    public static String getResImagePath() {
	return resImagePath;
    }

    public static String getResourceUrl() {
	return resourceUrl;
    }

    public static String getHomeUrl() {
	return homeUrl;
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
