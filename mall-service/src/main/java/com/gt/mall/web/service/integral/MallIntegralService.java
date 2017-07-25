package com.gt.mall.web.service.integral;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.integral.MallIntegral;
import com.gt.mall.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 积分商品表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallIntegralService extends BaseService<MallIntegral> {

    /**
     * 对积分商品进行分页
     *
     * @param params
     * @return
     */
    PageUtil selectIntegralByUserId(Map<String, Object> params);


    /**
     * 查询积分明细
     *
     * @param params
     * @return
     */
    PageUtil selectIntegralDetail(Member member, Map<String, Object> params);

    /**
     * 查询积分商品信息
     *
     * @param member
     * @param params
     * @return
     */
    Map<String, Object> selectProductDetail(Member member, Map<String, Object> params);

    /**
     * 兑换积分
     *
     * @param params
     * @return
     */
    Map<String, Object> recordIntegral(Map<String, Object> params, Member member, Integer browser, HttpServletRequest request);

    /**
     * 查询用户下面所有的积分商品
     *
     * @param params
     * @return
     */
    PageUtil selectIntegralByPage(Map<String, Object> params);

    /**
     * 查询单个的积分商品信息
     *
     * @param id
     * @return
     */
    Map<String, Object> selectByIds(int id);

    /**
     * 保存积分商品信息
     *
     * @param busUserId
     * @param params
     * @return
     */
    Map<String, Object> saveIntegral(int busUserId, Map<String, Object> params);

    /**
     * 删除或使失效 积分商品
     *
     * @param params
     * @return
     */
    boolean removeIntegral(Map<String, Object> params);


}
