package com.gt.mall.dao.store;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.store.MallStore;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城店铺 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallStoreDAO extends BaseMapper< MallStore > {

    /**
     * 分页查询商城店铺
     *
     * @param params userId 商家id
     * @param params stoName 店铺名称 （用在搜索，不传值则不搜索）
     *
     * @return
     */
    List< Map< String,Object > > findByPage( Map< String,Object > params );

    /**
     * 统计商家的店铺数量
     *
     * @param params userId 商家id
     * @param params stoName 店铺名称 （用在搜索，不传值则不搜索）
     *
     * @return
     */
    int count( Map< String,Object > params );

    /**
     * 根据店铺id 逻辑删除店铺
     *
     * @param ids
     *
     * @return
     */
    int updateByIds( @Param( "ids" ) String[] ids );

    List< MallStore > findByUserIds( @Param( "ids" ) List< Integer > ids );

    /**
     * 查询门店是否开通商城并拥有页面
     *
     * @param wxShopId 门店id
     *
     * @return
     */
    Integer shopIsOpenMall( @Param( "wxShopId" ) Integer wxShopId );
}