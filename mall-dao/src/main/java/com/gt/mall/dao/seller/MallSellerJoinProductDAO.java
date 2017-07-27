package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSellerJoinProduct;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 加入超级销售员的商品表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerJoinProductDAO extends BaseMapper< MallSellerJoinProduct > {

    /**
     * 查询已加入销售的商品
     * @return
     */
    List<MallSellerJoinProduct> selectProductByJoinSale(Map<String, Object> params);

    /**
     * 查询加入销售的商品的个数
     * @param params
     * @return
     */
    int selectCountByPage(Map<String, Object> params);

    /**
     * 对销售的商品进行分页
     * @param params
     * @return
     */
    List<Map<String, Object>> selectByPage(Map<String, Object> params);

    Map<String, Object> selectByIds(int id);

    /**
     * 查询商品的销售佣金
     * @param proId
     * @return
     */
    MallSellerJoinProduct selectByProId(int proId);

    /**
     * 查询商品是否已经加入商城
     * @param params
     * @return
     */
    int selectByOptionProId(Map<String,Object> params);

    /**
     * 查询信息
     * @param params
     * @return
     */
    Map<String, Object> selectSellerByProId(Map<String, Object> params);

    /**
     * 查询商品是否已经设置过佣金
     * @param params
     * @return
     */
    List<MallSellerJoinProduct> selectProductByIsJoin(Map<String, Object> params);

    /**
     *  查询已加入销售的商品
     * @param params
     * @return
     */
    List<Map<String, Object>> selectProductAllByJoinSale(Map<String, Object> params);

    /**
     *  统计已加入销售的商品
     * @param params
     * @return
     */
    int selectCountProductAllByJoinSale(Map<String, Object> params);

}