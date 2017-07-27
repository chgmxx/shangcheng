package com.gt.mall.dao.presale;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.presale.MallPresaleDeposit;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户缴纳定金表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleDepositDAO extends BaseMapper< MallPresaleDeposit > {

    /**
     * 统计拍卖信息
     *
     * @param params
     * @return
     */
    int selectByCount(Map<String, Object> params);

    /**
     * 分页查询拍卖信息
     *
     * @param params
     * @return
     */
    List<MallPresaleDeposit> selectByPage(Map<String, Object> params);

    /**
     * 根据保证金单号来查询保证金的信息
     * @param aucNo
     * @return
     */
    MallPresaleDeposit selectByPreNo(String aucNo);

    /**
     * 查询保证金信息
     * @param margin
     * @return
     */
    MallPresaleDeposit selectByDeposit(MallPresaleDeposit margin);

    /**
     * 查询定金的集合信息
     * @param deposit
     * @return
     */
    List<MallPresaleDeposit> selectListByDeposit(MallPresaleDeposit deposit);

    /**
     * 查询已结束拍卖的保证金信息
     * @return
     */
    List<Map<String, Object>> selectDepositByEnd();

    /**
     * 查询用户是否已经购买了保证金
     * @param params
     * @return
     */
    MallPresaleDeposit selectCountByPresaleId(Map<String, Object> params);

    /**
     * 查询预售商品订购的数量
     * @param params
     * @return
     */
    int selectBuyCountByPreId(Map<String, Object> params);

    /**
     * 查询定金的信息和商品的信息
     * @param deposit
     * @return
     */
    List<Map<String, Object>> selectListByDepositPro(MallPresaleDeposit deposit);

    /**
     * 查询用户是否已经缴纳了定金
     * @param params
     * @return
     */
    int selectCountDeposit(Map<String, Object> params);

}