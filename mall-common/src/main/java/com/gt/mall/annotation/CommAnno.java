package com.gt.mall.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//注解会在class中存在，运行时可通过反射获取
@Retention(RetentionPolicy.RUNTIME)
//目标
@Target(ElementType.METHOD)
//文档生成时，该注解将被包含在javadoc中，可去掉
@Documented
public @interface CommAnno {

	String type() default "menu";
	
	String menu_url() default "";
}
