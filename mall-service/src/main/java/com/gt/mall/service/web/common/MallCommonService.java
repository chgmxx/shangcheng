package com.gt.mall.service.web.common;

import com.gt.api.bean.session.Member;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.param.phone.order.add.PhoneAddOrderBusDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderProductDTO;
import com.gt.mall.param.phone.order.add.PhoneAddOrderShopDTO;
import com.gt.mall.result.phone.order.PhoneToOrderBusResult;

/**
 * <p>
 * 公共服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCommonService {

    /**
     * 获取手机验证码
     *
     * @param mobile         手机号码
     * @param busId          商家ID
     * @param content        短信内容
     * @param authorizerInfo 公司名称
     *
     * @return
     */
    boolean getValCode( String mobile, Integer busId, String content, String authorizerInfo );

    /**
     * 判断商家是否有技术支持
     *
     * @param busId 商家id
     *
     * @return true 有
     */
    boolean busIsAdvert( int busId );

    /**
     * 获取商家名称或商家头像
     */
    PhoneToOrderBusResult getBusUserNameOrImage( int busId );

    /**
     * 判断订单传参是否完整(提交订单时判断)
     */
    PhoneAddOrderDTO isOrderParams( PhoneAddOrderDTO params, Member member );

    /**
     * 重组订单详情的参数
     *
     * @param memberDiscount 会员优惠
     * @param couponId       选择优惠券id
     *
     * @return 订单详情
     */
    MallOrderDetail getOrderDetailParams( PhoneAddOrderProductDTO productDTO, PhoneAddOrderDTO params, Double memberDiscount, Integer couponId );

    /**
     * 重组订单参数
     */
    MallOrder getOrderParams( PhoneAddOrderShopDTO shopDTO, PhoneAddOrderBusDTO busDTO, PhoneAddOrderDTO params, Member member );

}
