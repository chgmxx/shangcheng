package com.gt.mall.service.inter.core;

/**
 * 增值服务接口调用
 * User : yangqian
 * Date : 2017/12/23 0023
 * Time : 16:51
 */
public interface CoreService {

    /**
     * 增值服务的是否过期
     *
     * @param busId      商家id
     * @param modelStyle 模块属性
     *
     * @return 0是没过期，1是商家过期，2是模块过期,3是模块已删除
     */
    Integer payModel( int busId, String modelStyle );
}
