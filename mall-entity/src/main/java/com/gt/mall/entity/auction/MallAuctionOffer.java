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
 * 拍卖出价表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_auction_offer" )
public class MallAuctionOffer extends Model< MallAuctionOffer > {

    private static final long serialVersionUID = 1L;

    /**
     * 出价标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 拍卖id 关联t_mall_auction表的id
     */
    @TableField( "auc_id" )
    private Integer    aucId;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "pro_id" )
    private Integer    proId;
    /**
     * 出价
     */
    @TableField( "offer_money" )
    private BigDecimal offerMoney;
    /**
     * 竞拍人 关联t_wx_bus_member表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 竞拍时间
     */
    @TableField( "create_time" )
    private Date       createTime;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallAuctionOffer{" +
			"id=" + id +
			", aucId=" + aucId +
			", proId=" + proId +
			", offerMoney=" + offerMoney +
			", userId=" + userId +
			", createTime=" + createTime +
			"}";
    }
}
