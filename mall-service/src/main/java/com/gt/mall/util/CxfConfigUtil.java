package com.gt.mall.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 注解方式获取application.yml文件的配置参数
 * User : yangqian
 * Date : 2017/7/19 0019
 * Time : 15:11
 */
@Component      //不加这个注解的话, 使用@Autowired 就不能注入进去了
@Configuration
@Setter
@Getter
public class CxfConfigUtil {

    @Value( "${project.shop.cxf-url}" )
    private String shopUrl;

    @Value( "${wxmp.token}" )
    private String wxmpToken;

}
