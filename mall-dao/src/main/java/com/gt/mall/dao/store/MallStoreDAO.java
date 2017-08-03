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
     * @param ids 店铺id
     *
     * @return
     */
    int updateByIds( @Param( "ids" ) String[] ids );

    /**
     * 根据门店id查询商品店铺信息
     *
     * @param wxShopIds 门店id
     *
     * @return
     */
    List< Map< String,Object > > findByShopIds( @Param( "wxShopIds" ) List< Integer > wxShopIds );

    /**
     * 根据商家idid查询  店铺信息，门店信息和  店铺页面id   oldParams findByPublicId
     *
     * @param userId
     *
     * @return 店铺信息，门店信息  和店铺页面
     */
    List< Map< String,Object > > findWxStorePageByUser( @Param( "userId" ) Integer userId );

    /**
     * 查询门店是否开通商城并拥有页面
     *
     * @param wxShopId 门店id
     *
     * @return
     */
    Integer shopIsOpenMall( @Param( "wxShopId" ) Integer wxShopId );

    /**
     * 根据商家id查询店铺信息
     *
     * @param userId 商家id
     *
     * @return
     */
    List< Map< String,Object > > findByUserId( @Param( "userId" ) Integer userId );

    /**
     * 根据店铺id查询店铺信息
     * @param id 店铺id
     * @return 店铺信息   字段
     */
    Map<String,Object> selectMapById(int id);

}