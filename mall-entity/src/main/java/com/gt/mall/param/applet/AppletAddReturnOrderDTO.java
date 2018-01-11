package com.gt.mall.param.applet;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 订单 申请退款参数
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
public class AppletAddReturnOrderDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;
    /**
     * 退款id 修改退款（填写物流信息）时必传
     */
    private Integer id;
    /**
     * 订单id 必传
     */
    private Integer orderId;
    /**
     * 订单详情id 必传
     */
    private Integer orderDetailId;
    /**
     * 退款人id 必传
     */
    private Integer userId;
    /**
     * 处理方式  1,我要退款，但不退货 2,我要退款退货
     */
    private Integer retHandlingWay;
    /**
     * 申请退款原因id
     */
    private Integer retReasonId;
    /**
     * 退款金额
     */
    private Double  retMoney;
    /**
     * 手机号码
     */
    private String  retTelephone;
    /**
     * 备注信息
     */
    private String  retRemark;
}
