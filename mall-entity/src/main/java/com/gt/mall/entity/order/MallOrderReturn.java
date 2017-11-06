package com.gt.mall.entity.order;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 商品退货
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_order_return" )
public class MallOrderReturn extends Model< MallOrderReturn > {

    private static final long serialVersionUID = 1L;

    /**
     * 退货标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 订单id  关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer    orderId;
    /**
     * 订单详情id  关联t_mall_order_detail表的id
     */
    @TableField( "order_detail_id" )
    private Integer    orderDetailId;
    /**
     * 退款单号
     */
    @TableField( "return_no" )
    private String     returnNo;
    /**
     * 处理方式
     * 1,我要退款，但不退货
     * 2,我要退款退货
     */
    @TableField( "ret_handling_way" )
    private Integer    retHandlingWay;
    /**
     * 申请退款原因id,关联字典表
     */
    @TableField( "ret_reason_id" )
    private Integer    retReasonId;
    /**
     * 申请退款原因
     */
    @TableField( "ret_reason" )
    private String     retReason;
    /**
     * 退款金额
     */
    @TableField( "ret_money" )
    private BigDecimal retMoney;
    /**
     * 手机号码
     */
    @TableField( "ret_telephone" )
    private String     retTelephone;
    /**
     * 备注信息
     */
    @TableField( "ret_remark" )
    private String     retRemark;
    /**
     * 多张图片，用逗号隔开
     */
    @TableField( "images_url" )
    private String     imagesUrl;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 修改时间
     */
    @TableField( "update_time" )
    private Date       updateTime;
    /**
     * 退款状态 -3还未退款 0退款中 1退款成功 2已同意退款退货，请退货给商家 3已退货等待商家确认收货 4 商家未收到货，不同意退款申请 5退款退货成功 -1 退款失败(卖家不同意退款) -2买家撤销退款
     */
    private Integer    status;
    /**
     * 申请人id  关联t_wx_bus_member表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 拒绝退款理由
     */
    @TableField( "no_return_reason" )
    private String     noReturnReason;
    /**
     * 退货地址
     */
    @TableField( "return_address" )
    private String     returnAddress;
    /**
     * 物流公司id
     */
    @TableField( "wl_company_id" )
    private Integer    wlCompanyId;
    /**
     * 物流公司
     */
    @TableField( "wl_company" )
    private String     wlCompany;
    /**
     * 物流单号
     */
    @TableField( "wl_no" )
    private String     wlNo;
    /**
     * 填写物流信息时用到的手机号码
     */
    @TableField( "wl_telephone" )
    private String     wlTelephone;
    /**
     * 填写物流信息的备注
     */
    @TableField( "wl_remark" )
    private String     wlRemark;
    /**
     * 退还粉币
     */
    @TableField( "return_fenbi" )
    private Double     returnFenbi;
    /**
     * 退还积分
     */
    @TableField( "return_jifen" )
    private Double     returnJifen;
    /**
     * 0 未收到货 1已收到货  -1 未填写
     */
    @TableField( "cargo_status" )
    private Integer    cargoStatus;
    /**
     * 退货物流时上传的图片 用逗号隔开
     */
    @TableField( "wl_images_url" )
    private String     wlImagesUrl;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallOrderReturn{" +
			"id=" + id +
			", orderId=" + orderId +
			", orderDetailId=" + orderDetailId +
			", returnNo='" + returnNo + '\'' +
			", retHandlingWay=" + retHandlingWay +
			", retReasonId=" + retReasonId +
			", retReason='" + retReason + '\'' +
			", retMoney=" + retMoney +
			", retTelephone='" + retTelephone + '\'' +
			", retRemark='" + retRemark + '\'' +
			", imagesUrl='" + imagesUrl + '\'' +
			", shopId=" + shopId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", status=" + status +
			", userId=" + userId +
			", noReturnReason='" + noReturnReason + '\'' +
			", returnAddress='" + returnAddress + '\'' +
			", wlCompanyId=" + wlCompanyId +
			", wlCompany='" + wlCompany + '\'' +
			", wlNo='" + wlNo + '\'' +
			", wlTelephone='" + wlTelephone + '\'' +
			", wlRemark='" + wlRemark + '\'' +
			", returnFenbi=" + returnFenbi +
			", returnJifen=" + returnJifen +
			", cargoStatus=" + cargoStatus +
			'}';
    }
}
