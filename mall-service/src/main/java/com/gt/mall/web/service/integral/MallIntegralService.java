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
     * @param params shopId：店铺id，shoplist:店铺Id集合，curPage：当前页
     * @return 积分商品列表
     */
    PageUtil selectIntegralByUserId(Map<String, Object> params);


    /**
     * 查询积分明细
     *
     * @param member 用户
     * @param params curPage：当前页
     * @return 积分明细列表
     */
//    PageUtil selectIntegralDetail(Member member, Map<String, Object> params);

    /**
     * 查询积分商品信息
     *
     * @param member 用户
     * @param params productId：商品Id
     * @return map
     */
    Map<String, Object> selectProductDetail(Member member, Map<String, Object> params);

    /**
     * 兑换积分
     *
     * @param params  productId:商品id,integralId:积分商品Id,productNum：商品数量，
     *                productSpecificas：商品规格，flowPhone：号码， receiveId：收货id
     * @param member  用户
     * @param browser 买家 数据来源
     * @return 积分
     */
    Map<String, Object> recordIntegral(Map<String, Object> params, Member member, Integer browser, HttpServletRequest request);

    /**
     * 查询用户下面所有的积分商品
     *
     * @param params curPage：当前页，type：状态，shopId：店铺id，shoplist:店铺Id集合
     * @return 积分商品列表
     */
    PageUtil selectIntegralByPage(Map<String, Object> params);

    /**
     * 查询单个的积分商品信息
     *
     * @param id 积分商品Id
     * @return
     */
    Map<String, Object> selectByIds(int id);

    /**
     * 保存积分商品信息
     *
     * @param busUserId 用户Id
     * @param params    integral:积分商品信息
     * @return map
     */
    Map<String, Object> saveIntegral(int busUserId, Map<String, Object> params);

    /**
     * 删除或使失效 积分商品
     *
     * @param params id：图片Id,type:状态
     * @return boolean
     */
    boolean removeIntegral(Map<String, Object> params);


}
