package com.gt.mall.dao.page;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.page.MallPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPageDAO extends BaseMapper< MallPage > {

    /**
     * 分页
     */
    List findByPage( Map< String,Object > params );

    /**
     * 获取条数
     */
    int count( Map< String,Object > params );

    /**
     * 删除多条
     */
    void deleteByIds( @Param( "ids" ) String[] ids );

    /**
     * 修改其他的数据为非主页
     */
    int updateOtherisMain( @Param( "id" ) Integer id, @Param( "stoId" ) Integer stoId );

    /**
     * 判断店铺下是否有主页
     */
    int selectMainCountByShopId( MallPage page );

    /**
     * 查询店铺下的页面的个数
     */
    int selectCountByShopId( int pagStoId );

    /**
     * 重新设置主页
     */
    int updatePageMain( int pagStoId );

    /**
     * 查询店铺下的页面
     */
    List< Map< String,Object > > selectByShopId( Map< String,Object > params );

    /**
     * 统计店铺下的页面
     */
    int selectCountsByShopId( Map< String,Object > params );

    /**
     * 根据页面id查询页面信息
     *
     * @param id 页面id
     *
     * @return 页面信息
     */
    Map< String,Object > selectMapById( int id );

    /**
     * 通过门店id查询页面id
     *
     * @param  params  wxShopId 微信门店id  userId  商家id
     *
     * @return 页面id
     */
    List< Map< String,Object > > selectPageByWxShopId( Map<String,Object> params );

}