package com.gt.mall.service.inter.wxshop;

import com.gt.util.entity.param.shop.ShopSubsop;
import com.gt.util.entity.result.shop.WsShopPhoto;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;

import java.util.List;
import java.util.Map;

/**
 * 门店接口
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 10:18
 */
public interface WxShopService {

    /**
     * 根据门店id查询门店信息
     *
     * @param wxShopId 门店id
     *
     * @return 门店信息
     */
    WsWxShopInfo getShopById( int wxShopId );

    /**
     * 根据门店id查询门店图片
     *
     * @param wxShopId 门店id
     *
     * @return 门店信息
     */
    List< WsShopPhoto > getShopPhotoByShopId( int wxShopId );

    /**
     * 根据商家id获取所有门店
     *
     * @param busUserId 商家id
     *
     * @return 所有门店包括图片
     */
    List< WsWxShopInfoExtend > queryWxShopByBusId( int busUserId );

    /**
     * 根据商家id查询主门店信息
     *
     * @param busUserId 商家id
     *
     * @return 门店信息
     */
    WsWxShopInfo selectMainShopByBusId( int busUserId );

    /**
     * 新增门店中间表 （新增店铺时调用的）
     *
     * @param shopSubsop 门店中间表对象
     *
     * @return true 成功
     */
    boolean addShopSubShop( ShopSubsop shopSubsop );

    /**
     * 根据父级ID查询城市数据
     *
     * @param parentId 父类id
     *
     * @return 城市数据
     */
    List< Map > queryCityByParentId( int parentId );

    /**
     * 查询省份列表(2---表示查询省份)
     *
     * @param level 2代表省份
     *
     * @return 省份列表
     */
    List< Map > queryCityByLevel( int level );

    /**
     * 根据城市id查询城市名称
     *
     * @param cityIds 城市id 查询多个用逗号隔开
     *
     * @return 城市信息
     */
    List< Map > queryBasisCityIds( String cityIds );

    /**
     * 删除门店中间表
     *
     * @param shopSubsop 门店对象
     *
     * @return true 成功
     */
    boolean updateBySubShop( ShopSubsop shopSubsop );

    /**
     * 根据城市名称查询城市id
     *
     * @param name 城市名称
     *
     * @return 城市id
     */
    Map queryBasisByName( String name );

}
