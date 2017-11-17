package com.gt.mall.dao.store;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.store.MallShopDaycount;
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
public interface MallShopDaycountDAO extends BaseMapper< MallShopDaycount > {

    /**
     * 检验商品是否存在
     *
     * @param date
     * @param shopId
     *
     * @return
     */
    int isShopByDate( @Param( "date" ) String date, @Param( "shopId" ) Integer shopId );

    /**
     * 修改店铺的销售总价
     *
     * @param date
     * @param shopId
     *
     * @return
     */
    int updateShopSalePrice( @Param( "date" ) String date, @Param( "shopId" ) Integer shopId );

    //统计店铺每月销售情况
    List< Map< String,Object > > selectLastMonthShop( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime );

    //获取店铺的统计信息
    List< Map< String,Object > > countLastMonthShop( @Param( "startTime" ) String startTime, @Param( "endTime" ) String endTime, @Param( "shopId" ) Integer shopId );

}