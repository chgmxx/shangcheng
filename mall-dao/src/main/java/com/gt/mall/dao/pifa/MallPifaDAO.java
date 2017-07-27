package com.gt.mall.dao.pifa;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.pifa.MallPifa;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品批发表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPifaDAO extends BaseMapper< MallPifa > {

    /**
     * 统计批发信息
     *
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询批发信息
     *
     * @param params
     * @return
     */
    List<MallPifa> selectByPage(Map<String, Object> params);

    /**
     * 通过id查询批发信息商品信息
     * @param id
     * @return
     */
    Map<String, Object> selectByPifaId(Integer id);

    /**
     * 查询是否存在未开始和进行中的商品
     * @param pifa
     * @return
     */
    List<MallPifa> selectPifaByProId(MallPifa pifa);

    /**
     * 查询是否存在未开始和进行中的商品
     * @param pifa
     * @return
     */
    List<MallPifa> selectStartPifaByProId(MallPifa pifa);

    /**
     * 根据id查询批发
     * @param id
     * @return
     */
    MallPifa selecPifaByIds(Integer id);

    /**
     * 通过店铺id查询批发信息
     * @param maps
     * @return
     */
    List<Map<String, Object> > selectgbProductByShopId(Map<String, Object> maps);

    /**
     * 根据商品id查询批发信息
     * @param pifa
     * @return
     */
    MallPifa selectBuyByProductId(MallPifa pifa);

    /**
     * 统计购买批发商品的信息
     * @param params
     * @return
     */
    int selectCountJoinNum(Map<String, Object> params);

    /**
     * 查询正在进行中的批发
     * @param params
     * @return
     */
    List<MallPifa> selectStartPiFaByProductId(Map<String, Object> params);
}