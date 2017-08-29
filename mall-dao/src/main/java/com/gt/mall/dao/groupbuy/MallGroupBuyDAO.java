package com.gt.mall.dao.groupbuy;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupBuyDAO extends BaseMapper< MallGroupBuy > {

    /**
     * 统计团购信息
     *
     * @param params type：状态，shoplist：店铺Id集合
     *
     * @return 数量
     */
    int selectByCount( Map< String,Object > params );

    /**
     * 分页查询团购信息
     *
     * @param params type：状态，shoplist：店铺Id集合，firstNum：页数，maxNum 数量
     *
     * @return 团购信息列表
     */
    List< MallGroupBuy > selectByPage( Map< String,Object > params );

    /**
     * 查询未开团商品
     *
     * @param params shopId：店铺id，shoplist：店铺Id集合，proName：商品名称，groupId：分组Id，firstNum：页数，maxNum 数量
     *
     * @return 未开团商品
     */
    List< Map< String,Object > > selectProByGroup( Map< String,Object > params );

    /**
     * 统计未开团商品
     *
     * @param params shopId：店铺id，shoplist：店铺Id集合，proName：商品名称，groupId：分组Id
     *
     * @return 数量
     */
    int selectCountProByGroup( Map< String,Object > params );

    /**
     * 通过id查询团购信息商品信息
     *
     * @param id 团购id
     *
     * @return 团购商品信息
     */
    Map< String,Object > selectByGroupBuyId( Integer id );

    /**
     * 查询是否存在未开始和进行中的商品
     *
     * @param buy productId：商品Id，status：状态，id：团购id
     *
     * @return 团购商品列表
     */
    List< MallGroupBuy > selectGroupByProId( MallGroupBuy buy );

    /**
     * 查询店铺下所有的团购商品
     *
     * @param params isPublic：是否发布，status：状态，id：团购Id，shopId:店铺id,productId:商品Id,
     *               proName：商品名称，groupId：分组Id,type:排序,desc：降序
     *
     * @return 商品列表
     */
    List< Map< String,Object > > selectgbProductByShopId( Map< String,Object > params );

    /**
     * 根据id查询团购
     *
     * @param id 团购id
     *
     * @return 团购信息
     */
    MallGroupBuy selectGroupByIds( Integer id );

    /**
     * 根据商品id查询团购信息
     *
     * @param buy id：团购Id
     *
     * @return
     */
    MallGroupBuy selectBuyByProductId( MallGroupBuy buy );

    /**
     * 查询已结束未成团的团购信息
     *
     * @return 团购信息
     */
    List< Map< String,Object > > selectEndGroupByAll();

    /**
     * 统计团购
     *
     * @param params isPublic：是否发布，status：状态，shopId:店铺id,productId:商品Id,
     *
     * @return 数量
     */
    int selectCountByShopId( Map< String,Object > params );

    /**
     * 通过商品id查询团购信息
     *
     * @param productId 商品Id
     *
     * @return 团购信息
     */
    Map< String,Object > selectGroupByProId( @Param( "productId" ) String productId );

    /**
     * 统计团购
     *
     * @param productList 商品集合
     *
     * @return 数量
     */
    List< Map< String,Object > > selectCountByProList( @Param( "productList" ) List< Integer > productList );

    /**
     * 查询商品 在当前时间是否参与团购、秒杀、拍卖、预售、批发
     *
     * @param products 商品id集合
     *
     * @return
     */
    List< Map< String,Object > > selectGroupsByProId( @Param( "products" ) List< Integer > products );

    List< Map< String,Object > > selectSeckillByProId( @Param( "products" ) List< Integer > products );

    List< Map< String,Object > > selectAuctionByProId( @Param( "products" ) List< Integer > products );

    List< Map< String,Object > > selectPresaleByProId( @Param( "products" ) List< Integer > products );

    List< Map< String,Object > > selectpifaByProId( @Param( "products" ) List< Integer > products );
}