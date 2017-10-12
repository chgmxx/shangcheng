package com.gt.mall.dao.presale;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.param.phone.PhoneSearchProductDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleDAO extends BaseMapper< MallPresale > {

    /**
     * 统计预售商品的数量
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询预售商品
     * @param params
     * @return
     */
    List<MallPresale> selectByPage(Map<String, Object> params);

    /**
     * 根据预售id查询预售状态
     * @param id
     * @return
     */
    MallPresale selectDepositByIds(int id);

    /**
     * 查询是否存在未开始和进行中的商品
     * @param presale
     * @return
     */
    List<MallPresale> selectDepositByProId(MallPresale presale);

    /**
     * 通过id查询预售信息商品信息
     * @param id
     * @return
     */
    Map<String, Object> selectByPresaleId(Integer id);
    /**
     * 根据商品id查询预售信息
     * @param presale
     * @return
     */
    MallPresale selectBuyByProductId(MallPresale presale);

    /**
     * 查询店铺下所有的预售商品
     * @param params
     * @return
     */
    List<Map<String, Object>> selectgbProductByShopId(Map<String, Object> params);

    /**
     * 查询用户是否购买了预售的商品
     * @param params
     * @return
     */
    int selectCountByPresaleId(Map<String, Object> params);

    /**
     * 查询已经结束的预售
     * @return
     */
    List<MallPresale> selectByPresaleEnd();

    /**
     * 统计店铺下预售商品的数量
     * @param params
     * @return
     */
    int selectCountByShopId(Map<String, Object> params);

    /**
     * 查询预售商品信息
     */
    List<Map<String,Object>> selectBySearchNames(Map<String,Object> params);

    /**
     * 查询正在进行的预售商品
     *
     * @return 商品列表
     */
    List< Map< String,Object > > selectGoingPresaleProduct( PhoneSearchProductDTO searchProductDTO );

    /**
     * 统计正在进行的预售商品
     */
    int selectCountGoingPresaleProduct( PhoneSearchProductDTO searchProductDTO );
}