package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSellerWithdraw;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员提现记录表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerWithdrawDAO extends BaseMapper< MallSellerWithdraw > {

    /**
     * 查询销售员的提现记录
     * @param params saleMemberId 销售员id
     * @return 提现记录
     */
    List<MallSellerWithdraw> selectBySaleMemberId(Map<String, Object> params);

    /**
     * 统计提现列表
     * @param params 参数
     * @return 提现列表数量
     */
    int selectCountWithdraw(Map<String, Object> params);

    /**
     * 查询提现列表
     * @param params 参数
     * @return 提现列表
     */
    List<Map<String, Object> > selectWithdrawList(Map<String, Object> params);
}