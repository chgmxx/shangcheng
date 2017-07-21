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

/**
 * <p>
 * 竞拍表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_auction_bidding" )
public class MallAuctionBidding extends Model< MallAuctionBidding > {

    private static final long serialVersionUID = 1L;

    /**
     * 竞拍标识
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
     * 商品名称
     */
    @TableField( "pro_name" )
    private String     proName;
    /**
     * 商品规格id 关联t_mall_product_specifica表的specifica_value_id 存放多个规格，用逗号(,)分隔
     */
    @TableField( "pro_specifica_ids" )
    private String     proSpecificaIds;
    /**
     * 商品名称
     */
    @TableField( "pro_img_url" )
    private String     proImgUrl;
    /**
     * 中拍价格
     */
    @TableField( "auc_price" )
    private BigDecimal aucPrice;
    /**
     * 竞拍状态 0 竞拍  1竞拍成功 -1 竞拍失败
     */
    @TableField( "auc_status" )
    private Integer    aucStatus;
    /**
     * 订单id 关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer    orderId;
    /**
     * 订单详情id
     */
    @TableField( "order_detail_id" )
    private Integer    orderDetailId;
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

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallAuctionBidding{" +
			"id=" + id +
			", aucId=" + aucId +
			", proId=" + proId +
			", proName=" + proName +
			", proSpecificaIds=" + proSpecificaIds +
			", proImgUrl=" + proImgUrl +
			", aucPrice=" + aucPrice +
			", aucStatus=" + aucStatus +
			", orderId=" + orderId +
			", orderDetailId=" + orderDetailId +
			", userId=" + userId +
			", createTime=" + createTime +
			"}";
    }
}
