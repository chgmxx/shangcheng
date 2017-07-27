package com.gt.mall.entity.presale;

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
import java.util.List;

/**
 * <p>
 * 用户缴纳定金表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_presale_deposit" )
public class MallPresaleDeposit extends Model< MallPresaleDeposit > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 预售id
     */
    @TableField( "presale_id" )
    private Integer    presaleId;
    /**
     * 预售商品id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 商品规格id 关联t_mall_product_specifica表的specifica_value_id 存放多个规格，用逗号(,)分隔
     */
    @TableField( "pro_specifica_ids" )
    private String     proSpecificaIds;
    /**
     * 商品名称
     */
    @TableField( "pro_name" )
    private String     proName;
    /**
     * 商品图片
     */
    @TableField( "pro_img_url" )
    private String     proImgUrl;
    /**
     * 定金编号
     */
    @TableField( "deposit_no" )
    private String     depositNo;
    /**
     * 定金金额
     */
    @TableField( "deposit_money" )
    private BigDecimal depositMoney;
    /**
     * 订购价  预定时的价格
     */
    @TableField( "order_money" )
    private BigDecimal orderMoney;
    /**
     * 缴纳定金状态 0 未支付  1已支付保证金 -1 已返还保证金 -2 不返还保证金
     */
    @TableField( "deposit_status" )
    private Integer    depositStatus;
    /**
     * 支付方式  1微信支付  2储值卡支付   3支付宝支付
     */
    @TableField( "pay_way" )
    private Integer    payWay;
    /**
     * 支付时间
     */
    @TableField( "pay_time" )
    private Date       payTime;
    /**
     * 支付单号
     */
    @TableField( "pay_no" )
    private String     payNo;
    /**
     * 定金返还单号
     */
    @TableField( "return_no" )
    private String     returnNo;
    /**
     * 定金返还时间
     */
    @TableField( "return_time" )
    private Date       returnTime;
    /**
     * 创建人 关联t_wx_bus_member表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 是否提交订单 0未提交 1 已提交
     */
    @TableField( "is_submit" )
    private Integer    isSubmit;
    /**
     * 购买数量
     */
    @TableField( "pro_num" )
    private Integer    proNum;

    @TableField(exist = false)
    private String shopName;

    @TableField(exist = false)
    private Integer shopId;

    @TableField(exist = false)
    private int invId;

    @TableField(exist = false)
    private String orderNo;

    @TableField(exist = false)
    private List<String> oldUserIdList;

    @TableField(exist = false)
    private int presaleStatus;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {

	return "MallPresaleDeposit{" +
			"id=" + id +
			", presaleId=" + presaleId +
			", productId=" + productId +
			", proSpecificaIds=" + proSpecificaIds +
			", proName=" + proName +
			", proImgUrl=" + proImgUrl +
			", depositNo=" + depositNo +
			", depositMoney=" + depositMoney +
			", orderMoney=" + orderMoney +
			", depositStatus=" + depositStatus +
			", payWay=" + payWay +
			", payTime=" + payTime +
			", payNo=" + payNo +
			", returnNo=" + returnNo +
			", returnTime=" + returnTime +
			", userId=" + userId +
			", createTime=" + createTime +
			", isSubmit=" + isSubmit +
			", proNum=" + proNum +
			"}";
    }
}
