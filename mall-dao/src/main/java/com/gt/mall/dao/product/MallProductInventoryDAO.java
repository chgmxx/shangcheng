package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallProductInventory;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品库存 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductInventoryDAO extends BaseMapper< MallProductInventory > {

    /**
     * 根据用户id来询售完商品
     *
     * @Title: selectByUserId
     */
    List< Map< String,Object > > selectByUserId( Map< String,Object > params );

    /**
     * 根据用户id查询售完商品数量
     *
     * @Title: selectCountByUserId
     */
    int selectCountByUserId( Map< String,Object > params );

    /**
     * 批量添加商品规格
     *
     * @Title: batchInsert
     */
    int insertBatch( Map< String,Object > map );

    /**
     * 批量修改商品规格
     *
     * @Title: batchUpdate
     */
    int updateBatch( List< MallProductInventory > invenList );

    /**
     * 修改商品的库存
     *
     * @param params
     *
     * @return
     */
    int updateProductStock( Map< String,Object > params );

    /**
     * 通过商品id查询商品库存
     *
     * @param id 商品id
     *
     * @return 商品库存
     */
    List< Map< String,Object > > selectInvenByProId( int id );

    /**
     * 通过商品Ids 店铺Id 查询商品库存
     *
     * @param productIds 商品Ids
     * @param shopId     店铺Id
     *
     * @return 库存
     */
    List< MallProductInventory > selectInvenByProIdsOrShopId( String productIds, Integer shopId );

    /**
     * 通过商品id查询商品库存
     *
     * @param map
     *
     * @return
     */
    Map< String,Object > selectProductInvenById( Map< String,Object > map );

}