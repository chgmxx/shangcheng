package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSellerIncome;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员收益表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerIncomeDAO extends BaseMapper< MallSellerIncome > {
    /**
     * 统计收益
     * @param params
     * @return
     */
    List<Map<String, Object>> selectTotalIncome(Map<String, Object> params);

    /**
     * 查询收益信息
     * @param income
     * @return
     */
    List<MallSellerIncome > selectByIncome(MallSellerIncome income);

    /**
     * 查询可以领取的收益
     * @param income
     * @return
     */
    List<MallSellerIncome> selectByCanWithIncome(MallSellerIncome income);
}