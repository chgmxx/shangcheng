package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSellerProduct;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员选取商品 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerProductDAO extends BaseMapper< MallSellerProduct > {

    /**
     * 通过商城设置的信息查询商城自选商品
     *
     * @param params
     *
     * @return
     */
    List< MallSellerProduct > selectProductByMallSet( Map< String,Object > params );

    /**
     * 查询销售员选择的商品
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectProductBySaleMember( Map< String,Object > params );

    /**
     * 查询商家的所有商品
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectProductByBusUserId( Map< String,Object > params );

    /**
     * 通过商品id销售员id查询销售商品
     *
     * @param params
     *
     * @return
     */
    MallSellerProduct selectByProIdSale( Map< String,Object > params );

    /**
     * 统计商城设置的信息查询商城自选商品
     *
     * @param params
     *
     * @return
     */
    int selectCountProductAllByMallSet( Map< String,Object > params );

    /**
     * 查询商城设置的信息查询商城自选商品
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectProductAllByMallSet( Map< String,Object > params );
}
