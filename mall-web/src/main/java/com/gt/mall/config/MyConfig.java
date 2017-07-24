package com.gt.mall.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 注解方式获取application.yml文件的配置参数
 * User : yangqian
 * Date : 2017/7/19 0019
 * Time : 15:11
 */
@Component      //不加这个注解的话, 使用@Autowired 就不能注入进去了
@ConfigurationProperties( prefix = "web" )  // 配置文件中的前缀
@Setter
@Getter
public class MyConfig {

    private String homeUrl;//网页地址

    private String imageUrlPrefix;//图片地址

}
