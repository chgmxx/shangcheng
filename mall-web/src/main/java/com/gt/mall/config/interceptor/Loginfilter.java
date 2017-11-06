package com.gt.mall.config.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 添加拦截器demo
 */
@Configuration
public class Loginfilter extends WebMvcConfigurerAdapter {

    @Override
    public void addInterceptors( InterceptorRegistry registry ) {

	//	registry.addInterceptor( new MyInterceptor() ).addPathPatterns( "/**" );

	registry.addInterceptor( new MyInterceptor() ).addPathPatterns( "/**" ).excludePathPatterns( "/**/E9lM9uM4ct/**", "/**/L6tgXlBFeK/**" );
	registry.addInterceptor( new BackInterceptor() ).addPathPatterns( "/**/E9lM9uM4ct/**" );
	registry.addInterceptor( new PhoneInterceptor() ).addPathPatterns( "/**/L6tgXlBFeK/**" );

	     	/*registry.addInterceptor(new SysLogInterceptor()).addPathPatterns("*//**");*/

	super.addInterceptors( registry );
    }

    /**
     * 跨域配置
     * 默认设置全局跨域配置
     * TODO: 部署服务器需要注释掉。因为，nginx已配置跨域。否则会起冲突
     *
     * @param registry Corsregistry
     */
    @Override
    public void addCorsMappings( CorsRegistry registry ) {
	registry.addMapping( "/**" ).allowedHeaders( "*" ).allowedMethods( "*" ).allowedOrigins( "*" );
	super.addCorsMappings( registry );
    }
}
