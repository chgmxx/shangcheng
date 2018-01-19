package com.gt.mall.service.web.basic;

import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallBusMessageMember;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;

import java.util.List;

/**
 * <p>
 * 商家消息模板提醒用户表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-12-28
 */
public interface MallBusMessageMemberService extends BaseService< MallBusMessageMember > {

    /**
     * 根据公众号ID查询数据
     *
     * @param busId
     *
     * @return
     */
    List< MallBusMessageMember > selectByBusId( Integer busId );

    /**
     * 买家付款成功通知商家
     *
     * @param order
     * @param member
     * @param type   发送类型 0全部 1短信 2模板
     */
    void buyerPaySuccess( MallOrder order, Member member, Integer type, String telePhone );

    /**
     * 买家确认收货通知商家
     *
     * @param orderId
     * @param type    发送类型 0全部 1短信 2模板
     */
    void buyerConfirmReceipt( Integer orderId, Integer type );

    /**
     * 买家订单维权通知商家
     *
     * @param orderReturn
     * @param member
     * @param type        发送类型 0全部 1短信 2模板
     */
    void buyerOrderReturn( MallOrderReturn orderReturn, Member member, Integer type );

    /**
     * 粉丝申请成为超级销售员 通知商家
     *
     * @param member   粉丝
     * @param telPhone 接收申请审核手机
     */
    void memberApplySeller( Member member, String telPhone );
}
