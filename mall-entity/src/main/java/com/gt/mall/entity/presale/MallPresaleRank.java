package com.gt.mall.entity.presale;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 预售送礼名次
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_presale_rank" )
public class MallPresaleRank extends Model< MallPresaleRank > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 预售id 关联t_mall_presale表的id
     */
    @TableField( "presale_id" )
    private Integer presaleId;
    /**
     * 定金id 关联t_mall_presale_deposit表的id
     */
    @TableField( "deposit_id" )
    private Integer depositId;
    /**
     * 订单id 关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer orderId;
    /**
     * 排名
     */
    private Integer rank;
    /**
     * 礼品类型 1,积分  2,粉币  3实体物品
     */
    private Integer type;
    /**
     * 中奖人 关联t_wx_bus_member表的id
     */
    @TableField( "member_id" )
    private Integer memberId;
    /**
     * 奖品名称
     */
    @TableField( "give_name" )
    private String  giveName;
    /**
     * 奖品数量
     */
    @TableField( "give_num" )
    private Integer giveNum;
    /**
     * 是否已经赠送出去了   0还未赠送   1已经赠送
     */
    @TableField( "is_give" )
    private String  isGive;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallPresaleRank{" +
			"id=" + id +
			", presaleId=" + presaleId +
			", depositId=" + depositId +
			", orderId=" + orderId +
			", rank=" + rank +
			", type=" + type +
			", memberId=" + memberId +
			", giveName=" + giveName +
			", giveNum=" + giveNum +
			", isGive=" + isGive +
			"}";
    }
}
