package com.gt.mall.cxf.service;

import com.gt.mall.bean.result.shop.WsWxShopInfo;

/**
 * 微信门店调用的业务
 * User : yangqian
 * Date : 2017/7/27 0027
 * Time : 10:14
 */
public interface WxShopService {

    /**
     * 根据门店id 查询门店信息
     * @param id 门店id
     * @return 门店信息
     */
    public WsWxShopInfo getShopById(int id);

}
