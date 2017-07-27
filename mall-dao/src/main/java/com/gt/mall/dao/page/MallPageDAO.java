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
     * @param params
     * @return
     */
    List findByPage(Map<String, Object> params);


    /**
     * 获取条数
     * @param params
     * @return
     */
    int count(Map<String, Object> params);


    /**
     * 删除多条
     * @param params
     * @return
     */
    public void deleteByIds(@Param("ids")String[] ids);

    /**
     * 修改其他的数据为非主页
     * @param id
     * @param stoId
     * @return
     */
    int updateOtherisMain(@Param("id")Integer id,@Param("stoId")Integer stoId);

    int updatecss(MallPage record);

    /**
     * 判断店铺下是否有主页
     * @param page
     * @return
     */
    int selectMainCountByShopId(MallPage page);

    /**
     * 查询店铺下的页面的个数
     * @param pagStoId
     * @return
     */
    int selectCountByShopId(int pagStoId);

    /**
     * 重新设置主页
     * @param pagStoId
     * @return
     */
    int updatePageMain(int pagStoId);

    /**
     * 查询店铺下的页面
     * @param params
     * @return
     */
    List<Map<String, Object>> selectByShopId(Map<String, Object> params);

    /**
     * 统计店铺下的页面
     * @param params
     * @return
     */
    int selectCountsByShopId(Map<String, Object> params);


}