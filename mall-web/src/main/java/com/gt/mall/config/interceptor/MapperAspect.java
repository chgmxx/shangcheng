package com.gt.mall.config.interceptor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 使用aop监控Mapper执行时间
 * User : yangqian
 * Date : 2017/9/7 0007
 * Time : 17:42
 */
@Aspect
@Component
public class MapperAspect {
    private static final Logger logger = LoggerFactory.getLogger( MapperAspect.class );

    @AfterReturning( "execution(* com.gt.mall.dao..*DAO.*(..))" )
    public void logServiceAccess( JoinPoint joinPoint ) {
	logger.info( "Completed: " + joinPoint );
    }

    /**
     * 监控com.caiyi.financial.nirvana..*Mapper包及其子包的所有public方法
     */
    @Pointcut( "execution(* com.gt.mall.dao..*DAO.*(..))" )
    private void pointCutMethod() {
    }

    /**
     * 声明环绕通知
     */
    @Around( "pointCutMethod()" )
    public Object doAround( ProceedingJoinPoint pjp ) throws Throwable {
	long begin = System.currentTimeMillis();
	Object obj = pjp.proceed();
	long end = System.currentTimeMillis();

	logger.info( "调用Mapper方法：{}", pjp.getSignature().toString() );
	logger.info( "参数：{}", Arrays.toString( pjp.getArgs() ) );
	logger.info( "执行耗时：{}毫秒", ( end - begin ) );

	return obj;
    }
}
