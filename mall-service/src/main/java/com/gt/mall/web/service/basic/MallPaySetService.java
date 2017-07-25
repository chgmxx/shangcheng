package com.gt.mall.web.service.basic;

import com.gt.mall.base.BaseService;
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
     * @param set
     * @return
     */
    MallPaySet selectByUserId(MallPaySet set);

    /**
     * 编辑商城设置
     *
     * @param params
     * @return
     */
    public int editPaySet(Map<String, Object> params);


    /**
     * 判断商户是否允许买家货到付款
     *
     * @param userId
     * @return
     */
    public void isHuoDaoByUserId(int userId, HttpServletRequest request);

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
     * @param userId
     * @return
     */
    public Map<String, Object> isHuoDaoByUserId(int userId);
}
