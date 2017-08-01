package com.gt.mall.web.service.seller;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.seller.MallSellerWithdraw;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员提现记录表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerWithdrawService extends BaseService< MallSellerWithdraw > {

    /**
     * 查询销售员的提现记录
     * @param member 销售员
     * @return 提现记录
     */
    List<MallSellerWithdraw> selectBySaleMemberId(Member member);

    /**
     * 保存提现信息
     * @param saleMemberId 销售员信息
     * @param params  提现信息
     * @return {flag：true 成功、false 失败，msg：失败提示语}
     */
    Map<String, Object> saveWithdraw(int saleMemberId,Map<String, Object> params) throws Exception;

    /**
     * 查询提现列表
     * @param busUserId 商家id
     * @param params 参数
     * @return 提现列表
     */
    PageUtil withdrawPage(int busUserId,Map<String, Object> params);

    /**
     * 查询提现明细
     * @param params 参数
     * @return 提现明细
     */
    List<Map<String, Object>> selectWithdrawList(Map<String, Object> params);

}
