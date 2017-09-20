package com.gt.mall.config;

import com.gt.mall.config.interceptor.MyInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 添加拦截器demo
 */
@Configuration
public class Loginfilter extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors( InterceptorRegistry registry ) {
	registry.addInterceptor( new MyInterceptor() ).addPathPatterns( "/**" );
	//        registry.addInterceptor(new SysLogInterceptor()).addPathPatterns("/**");

	super.addInterceptors( registry );
    }
}
