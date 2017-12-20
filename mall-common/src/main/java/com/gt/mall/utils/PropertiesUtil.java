package com.gt.mall.utils;

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

    private static String marketingUrl; //商城营销路径

    private static String phoneWebHomeUrl;//手机端域名

    private static String jxcUrl;//进销存 地址

    private static String jxcAccount;//进销存token用户名

    private static String jxcPwd;//进销存token密码

    private static String staticSourceFtpIp;//图片资源Ftp IP

    private static String staticSourceFtpPort;//图片资源Ftp 端口

    private static String staticSourceFtpUser;//图片资源Ftp 用户

    private static String staticSourceFtpPwd;  //图片资源Ftp 密码

    private static String redisHost;// redis IP

    private static String redisPort;// redis 端口

    private static String redisPassword;// redis 密码

    private static Integer redisDataBase;//redis Database

    private static String resImagePath;//获取图片存放路径

    private static String resourceUrl;//访问资源URL

    private static String dbname;//电信流量订单前缀

    private static String exchange;//队列转换器

    private static String queueName;//队列名称

    private static String wxmpDomain;//主项目域名（登陆。。）

    private static String wxmpSignKey;//调用主项目接口的前面key

    private static String memberDomain;//调用会员接口的链接

    private static String memberSignKey;//调用会员接口的签名key

    private static String unionDomain;//调用联盟接口的链接

    private static String unionSignKey;//调用联盟接口的签名key

    @Value( "${web.domain}" )
    public void setDomain( String domain ) {
	PropertiesUtil.domain = domain;
    }

    @Value( "${web.homeUrl}" )
    public void setHomeUrl( String homeUrl ) {
	PropertiesUtil.homeUrl = homeUrl;
    }

    @Value( "${web.marketingUrl}" )
    public void setMarketingUrl( String marketingUrl ) {
	PropertiesUtil.marketingUrl = marketingUrl;
    }

    @Value( "${web.phoneWebHomeUrl}" )
    public void setPhoneWebHomeUrl( String phoneWebHomeUrl ) {
	PropertiesUtil.phoneWebHomeUrl = phoneWebHomeUrl;
    }

    @Value( "${http.jxc.domain}" )
    public void setJxcUrl( String jxcUrl ) {
	PropertiesUtil.jxcUrl = jxcUrl;
    }

    @Value( "${http.jxc.account}" )
    public void setJxcAccount( String jxcAccount ) {
	PropertiesUtil.jxcAccount = jxcAccount;
    }

    @Value( "${http.jxc.pwx}" )
    public void setJxcPwd( String jxcPwd ) {
	PropertiesUtil.jxcPwd = jxcPwd;
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

    @Value( "${redis.host}" )
    public void setRedisHost( String redisHost ) {
	PropertiesUtil.redisHost = redisHost;
    }

    @Value( "${redis.port}" )
    public void setRedisPort( String redisPort ) {
	PropertiesUtil.redisPort = redisPort;
    }

    @Value( "${redis.password}" )
    public void setRedisPassword( String redisPassword ) {
	PropertiesUtil.redisPassword = redisPassword;
    }

    @Value( "${redis.database}" )
    public void setRedisDataBase( Integer redisDataBase ) {
	PropertiesUtil.redisDataBase = redisDataBase;
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

    @Value( "${mq.queueName}" )
    public void setQueueName( String queueName ) {
	PropertiesUtil.queueName = queueName;
    }

    @Value( "${http.wxmp.domain}" )
    public void setWxmpDomain( String wxmpDomain ) {
	PropertiesUtil.wxmpDomain = wxmpDomain;
    }

    @Value( "${http.wxmp.key}" )
    public void setWxmpSignKey( String wxmpSignKey ) {
	PropertiesUtil.wxmpSignKey = wxmpSignKey;
    }

    @Value( "${http.member.domain}" )
    public void setMemberDomain( String memberDomain ) {
	PropertiesUtil.memberDomain = memberDomain;
    }

    @Value( "${http.member.key}" )
    public void setMemberSignKey( String memberSignKey ) {
	PropertiesUtil.memberSignKey = memberSignKey;
    }

    @Value( "${http.union.domain}" )
    public void setUnionDomain( String unionDomain ) {
	PropertiesUtil.unionDomain = unionDomain;
    }

    @Value( "${http.union.key}" )
    public void setUnionSignKey( String unionSignKey ) {
	PropertiesUtil.unionSignKey = unionSignKey;
    }

    /**
     * 获取联盟的签名key
     */
    public static String getUnionDomain() {
	return unionDomain;
    }

    /**
     * 获取联盟的链接
     */
    public static String getUnionSignKey() {
	return unionSignKey;
    }

    /**
     * 获取主项目的签名keykey
     */
    public static String getWxmpSignKey() {
	return wxmpSignKey;
    }

    /**
     * 获取主域名（登陆）
     */
    public static String getWxmpDomain() {
	return wxmpDomain;
    }

    /**
     * 获取会员域名
     */
    public static String getMemberDomain() {
	return memberDomain;
    }

    /**
     * 获取会员签名key
     */
    public static String getMemberSignKey() {
	return memberSignKey;
    }

    /**
     * 获取商城的域名
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

    public static String getMarketingUrl() {
	return marketingUrl;
    }

    public static String getPhoneWebHomeUrl() {
	return phoneWebHomeUrl;
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

    public static String getRedisHost() {
	return redisHost;
    }

    public static String getRedisPort() {
	return redisPort;
    }

    public static String getRedisPassword() {
	return redisPassword;
    }

    public static Integer getRedisDataBase() {
	return redisDataBase;
    }
}
