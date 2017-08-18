package com.gt.mall.service.inter.wxshop;

import com.gt.mall.bean.wxshop.ShopPhoto;
import com.gt.mall.bean.wxshop.WsWxShopInfo;

import java.util.List;

/**
 * 门店接口
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 10:18
 */
public interface WxShopService {

    /**
     * 根据门店id查询门店信息
     * @param wxShopId 门店id
     * @return 门店信息
     */
    WsWxShopInfo getShopById(int wxShopId);

    /**
     * 根据门店id查询门店图片
     * @param wxShopId 门店id
     * @return 门店信息
     */
    List<ShopPhoto> getShopPhotoByShopId(int wxShopId);

}
