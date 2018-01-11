package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallProductDaycount;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductDaycountDAO extends BaseMapper< MallProductDaycount > {

    int updatetk( MallProductDaycount record );

    /**
     * 检验商品是否存在
     *
     * @param date
     * @param productId
     *
     * @return
     */
    List< Map< String,Object > > isProductByDate( @Param( "date" ) String date, @Param( "productId" ) Integer productId );

    //统计店铺
    List< Map< String,Object > > selectShopIdByDate( @Param( "date" ) String date );

    //统计店铺每天的销售，退款情况
    List< Map< String,Object > > selectShopReturnListByDate( @Param( "date" ) String date, @Param( "shopId" ) Integer shopId );

    //获取上个月的销售的所有的商品列表
    List< Map< String,Object > > selectLastMonthProduct( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime );

    //获取商品的统计
    List< Map< String,Object > > countLastMonthProduct( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime, @Param( "productId" ) Integer productId );

}