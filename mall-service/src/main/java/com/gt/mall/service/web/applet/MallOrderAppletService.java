package com.gt.mall.service.web.applet;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.utils.PageUtil;

import java.util.Map;

/**
 * <p>
 * 小程序商城订单
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderAppletService extends BaseService<MallAppletImage> {


    /**
     * 查询订单列表页面
     *
     * @param params
     * @return
     */
    PageUtil getOrderList(Map<String, Object> params);

    /**
     * 查询订单详情
     *
     * @param params
     * @return
     */
    Map<String, Object> getOrderDetail(Map<String, Object> params);

    /**
     * 去支付
     *
     * @param params
     * @return
     */
    Map<Object, Object> orderGoPay(Map<String, Object> params, String url) throws Exception;

    /**
     * 确认收货
     *
     * @param params
     * @return
     */
    Map<String, Object> confirmReceipt(Map<String, Object> params);

    /**
     * 查询退款信息
     *
     * @param params
     * @return
     */
    Map<String, Object> toReturnOrder(Map<String, Object> params);

    /**
     * 提交退款信息
     *
     * @param params
     * @return
     */
    Map<String, Object> submitReturnOrder(Map<String, Object> params);

    /**
     * 撤销信息
     *
     * @param params
     * @return
     */
    Map<String, Object> closeReturnOrder(Map<String, Object> params);

    /**
     * 进入我的地址页面
     *
     * @param params
     * @return
     */
    Map<String, Object> addressList(Map<String, Object> params);


    /**
     * 进入提交订单的页面
     *
     * @param params
     * @return
     */
    Map<String, Object> toSubmitOrder(Map<String, Object> params);

    /**
     * 提交订单的页面
     *
     * @param params
     * @return
     */
    Map<String, Object> submitOrder(Map<String, Object> params);

    /**
     * 微信下单的接口
     *
     * @param params
     * @return
     */
    String appletWxOrder(Map<String, Object> params) throws Exception;

    /**
     * 商品立即购买
     *
     * @param params
     * @return
     */
    Map<String, Object> productBuyNow(Map<String, Object> params);
}
