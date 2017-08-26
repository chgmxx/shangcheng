package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallProduct;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductDAO extends BaseMapper< MallProduct > {

    /**
     * 查询分组下的商品个数
     *
     * @param shopId
     * @param groupId
     *
     * @return
     */
    int countProductByGroup( @Param( "shopId" ) int shopId, @Param( "groupId" ) int groupId, @Param( "userId" ) int userId );

    /**
     * 统计用户下面的商品数量
     *
     * @Title: selectCountByUserId
     */
    Integer selectCountByUserId( Map< String,Object > params );

    /**
     * 根据用户id来查询商品信息
     *
     * @Title: selectByUserId
     */
    List< Map< String,Object > > selectByUserId( Map< String,Object > params );

    /**
     * 批量修改商品信息
     *
     * @Title: batchUpdateProduct
     */
    int batchUpdateProduct( Map< String,Object > params );

    /**
     * 统计门店下的商品
     *
     * @param params
     *
     * @return
     */
    int selectCountByShopId( Map< String,Object > params );

    /**
     * 查询门店下的商品信息
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectByShopId( Map< String,Object > params );

    /**
     * 根据商品id查询商品信息
     *
     * @param params
     *
     * @return
     */
    Map< String,Object > selectByProId( Map< String,Object > params );

    /**
     * 修改商品库存
     *
     * @param params
     *
     * @return
     */
    int updateProductStock( Map< String,Object > params );

    /**
     * 查询店铺下的商品信息
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectProductByShopids( Map< String,Object > params );

    /**
     * 统计店铺下的商品信息
     *
     * @param params
     *
     * @return
     */
    int selectCountByShopids( Map< String,Object > params );

    /**
     * 搜索店铺下的商品
     *
     * @param params
     *
     * @return
     */
    List< Map< String,Object > > selectProductAllByShopids( Map< String,Object > params );

    /**
     * 统计搜索店铺下的商品
     *
     * @param params
     *
     * @return
     */
    int selectCountAllByShopids( Map< String,Object > params );

    /**
     * 根据商品id查询商品信息，商品规格，商品库存和商品图片
     *
     * @param id 商品id
     *
     * @return 商品信息，商品
     */
    Map< String,Object > selectProductMapById( int id );

    /**
     * 根据id查询商品信息
     *
     * @param id 商品id
     *
     * @return 商品信息
     */
    Map< String,Object > selectMapById( int id );

    /**
     * 根据门店id查询粉币商品的信息
     *
     * @param wxShopId 门店id
     *
     * @return 粉币商品
     */
    List< Map< String,Object > > selectFenbiNumByWxShopId( @Param( "wxShopId" ) Integer wxShopId );

    /**
     * 根据店铺id查询粉币商品的数量
     *
     * @param shopId 店铺id
     *
     * @return 粉币商品的数量
     */
    int selectFenbiNumByShopId( @Param( "shopId" ) int shopId );

    /**
     * 统计店铺下商品-对外报价
     *
     * @param params shoplist 店铺id集合，proName：商品名称
     *
     * @return 数量
     */
    int countPurchaseProByShopId( Map< String,Object > params );

    /**
     * 搜索店铺下的商品-对外报价
     *
     * @param params shoplist 店铺id集合，proName：商品名称,firstNum：页数，maxNum：记录数
     *
     * @return list
     */
    List< Map< String,Object > > selectPurchaseProByShopId( Map< String,Object > params );

    /**
     * 通过商品名称进行合并商家的商品
     *
     * @param userId 商家Id
     *
     * @return
     */
    List< Map< String,Object > > selectProByUserIdGroupName( Integer userId );

}