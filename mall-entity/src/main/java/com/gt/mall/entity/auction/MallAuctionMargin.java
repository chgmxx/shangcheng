package com.gt.mall.entity.auction;

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
 * 竞拍保证金表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_auction_margin" )
public class MallAuctionMargin extends Model< MallAuctionMargin > {

    private static final long serialVersionUID = 1L;

    /**
     * 拍卖保证金标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 拍卖id
     */
    @TableField( "auc_id" )
    private Integer    aucId;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "pro_id" )
    private Integer    proId;
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
     * 保证金编号
     */
    @TableField( "auc_no" )
    private String     aucNo;
    /**
     * 保证金金额
     */
    @TableField( "margin_money" )
    private BigDecimal marginMoney;
    /**
     * 保证金状态 0 未支付  1已支付保证金 -1 已返还保证金 -2 不返还保证金
     */
    @TableField( "margin_status" )
    private Integer    marginStatus;
    /**
     * 支付方式 1 微信支付 2 储值卡支付  3 支付宝支付
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
     * 保证金返还单号
     */
    @TableField( "return_no" )
    private String     returnNo;
    /**
     * 保证金返还时间
     */
    @TableField( "return_time" )
    private Date       returnTime;
    /**
     * 是否需要返还保证金 0 不需要返还 1需要返还
     */
    @TableField( "is_return" )
    private Integer    isReturn;
    /**
     * 不返还保证金的原因
     */
    @TableField( "no_return_reason" )
    private String     noReturnReason;
    /**
     * 交纳人 关联t_wx_bus_member表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;

    @TableField(exist = false)
    private Integer aucType;//拍卖类型

    @TableField(exist = false)
    private Integer shopId; //店铺Id

    @TableField(exist = false)
    private int auctionStatus; //拍卖状态

    @TableField(exist = false)
    private List<String> oldUserIdList;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallAuctionMargin{" +
			"id=" + id +
			", aucId=" + aucId +
			", proId=" + proId +
			", proSpecificaIds=" + proSpecificaIds +
			", proName=" + proName +
			", proImgUrl=" + proImgUrl +
			", aucNo=" + aucNo +
			", marginMoney=" + marginMoney +
			", marginStatus=" + marginStatus +
			", payWay=" + payWay +
			", payTime=" + payTime +
			", payNo=" + payNo +
			", returnNo=" + returnNo +
			", returnTime=" + returnTime +
			", isReturn=" + isReturn +
			", noReturnReason=" + noReturnReason +
			", userId=" + userId +
			", createTime=" + createTime +
			"}";
    }
}
