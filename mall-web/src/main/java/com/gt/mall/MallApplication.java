package com.gt.mall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 商城行业版 程序入口
 */
@ServletComponentScan
@SpringBootApplication
public class MallApplication extends SpringBootServletInitializer {

    public static void main( String[] args ) {
	SpringApplication.run( MallApplication.class, args );
    }

    @Override
    protected SpringApplicationBuilder configure( SpringApplicationBuilder application ) {
	return application.sources( MallApplication.class );
    }

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
	return new DataSourceTransactionManager(dataSource);
    }
}
