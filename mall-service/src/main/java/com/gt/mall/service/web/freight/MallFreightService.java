package com.gt.mall.service.web.freight;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.base.BaseService;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.utils.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallFreightService extends BaseService< MallFreight > {

    /**
     * 通过店铺id来查询物流
     *
     * @param shopList 店铺id集合
     * @param param    curPage：当前页
     *
     * @return
     */
    PageUtil selectFreightByShopId( List< Map< String,Object > > shopList, Map< String,Object > param );

    /**
     * 通过物流id查询物流信息
     *
     * @param id 物流Id
     *
     * @return 物流信息
     */
    MallFreight selectFreightById( Integer id );

    /**
     * 编辑物流
     *
     * @param params freight:物流信息， detail：物流详情
     *               delDetail：删除的物流详情，delPro：删除的物流配送区域
     * @param userId 用户Id
     *
     * @return 是否成功
     */
    boolean editFreight( Map< String,Object > params, int userId );

    /**
     * 编辑物流
     *
     * @param params freight:物流信息， detail：物流详情
     * @param userId 用户Id
     *
     * @return 是否成功
     */
    boolean newEditFreight( Map< String,Object > params, int userId );

    /**
     * 删除物流
     *
     * @param ids 物流id集合
     *
     * @return 是否成功
     */
    boolean deleteFreight( Map< String,Object > ids );

    /**
     * 通过店铺id来查询物流
     *
     * @param shopList 店铺id集合
     *
     * @return 物流列表
     */
    List< MallFreight > getByShopId( List< Map< String,Object > > shopList );

    /**
     * 通过店铺id来查询物流
     *
     * @param shopId 店铺id
     *
     * @return 物流列表
     */
    MallFreight selectFreightByShopId( Integer shopId );

    /**
     * 手机端获取运费
     *
     * @param ip         粉丝所在的ip
     * @param provinceId 省份id
     * @param toshop     是否是到店 1 到店 0不是到店
     * @param productArr 商品对象 [{shop_id:店铺id,price_total:商品价格,proNum:商品数量,proTypeId:用户类型,juli:收货地址跟门店的距离}]
     *
     * @return 价格 [{店铺id：运费}]
     */
    Map< String,Object > getFreightByParams( String ip, String provinceId, int toshop, JSONArray productArr, double juli );

    /**
     * 获取运费
     *
     * @param map ...
     *
     * @return map
     */
    Map< String,Object > getFreightMoney( Map< String,Object > map );

    /**
     * 通过省份名称获取省份id
     *
     * @param province 省份名称
     *
     * @return id
     */
    int getProvinceId( String province );

    /**
     * 根据省份获取运费
     *
     * @param params     信息
     * @param addressMap 省份id
     * @param shopId     店铺Id
     * @param totalPrice 总价格
     * @param pro_weight 重置
     *
     * @return 运费
     */
    double getFreightByProvinces( Map< String,Object > params, Map< String,Object > addressMap, int shopId, double totalPrice, double pro_weight );

}