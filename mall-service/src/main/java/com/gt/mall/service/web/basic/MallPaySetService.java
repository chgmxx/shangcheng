package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.basic.MallPaySet;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 商城交易支付设置 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPaySetService extends BaseService<MallPaySet> {

    /**
     * 通过用户Id查询商城设置
     *
     * @param set userId：用户Id
     * @return
     */
    MallPaySet selectByUserId(MallPaySet set);

    /**
     * 编辑商城设置
     *
     * @param params 商城设置
     * @return 是否成功
     */
    int editPaySet(Map<String, Object> params);


    /**
     * 判断商户是否允许买家货到付款
     *
     * @param userId 用户id
     */
    void isHuoDaoByUserId(int userId, HttpServletRequest request);

    /**
     * 添加商城设置
     *
     * @param paySet
     * @return
     */
//    public int insertSelective(MallPaySet paySet);


    /**
     * 判断商户是否允许买家货到付款
     *
     * @param userId 用户id
     * @return map
     */
    Map<String, Object> isHuoDaoByUserId(int userId);

    /**
     * 获取商城设置
     *
     * @param member 用户
     * @return 商城设置
     */
    MallPaySet selectByMember(Member member);

    /**
     * 查询底部菜单
     *
     * @param busUserId 用户Id
     * @return Map
     */
    Map<String, Object> getFooterMenu(int busUserId);
}
