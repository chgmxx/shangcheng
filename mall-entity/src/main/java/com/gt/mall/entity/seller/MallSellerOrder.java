package com.gt.mall.entity.seller;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 超级销售员订单中间表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller_order" )
public class MallSellerOrder extends Model< MallSellerOrder > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 订单id 关联t_mall_order表的id
     */
    @TableField( "order_id" )
    private Integer    orderId;
    /**
     * 订单详情id 关联t_mall_order_detail表的id
     */
    @TableField( "order_detail_id" )
    private Integer    orderDetailId;
    /**
     * 销售员id  关联 t_wx_bus_member表的id
     */
    @TableField( "sale_member_id" )
    private Integer    saleMemberId;
    /**
     * 推荐人id  关联 t_wx_bus_member表的id
     */
    @TableField( "referees_member_id" )
    private Integer    refereesMemberId;
    /**
     * 订单金额
     */
    @TableField( "order_money" )
    private BigDecimal orderMoney;
    /**
     * 买家id 关联t_mall_order表的buyer_user_id
     */
    @TableField( "buyer_user_id" )
    private Integer    buyerUserId;
    /**
     * 卖家id 关联t_mall_order表的seller_user_id
     */
    @TableField( "seller_user_id" )
    private Integer    sellerUserId;
    /**
     * 是否删除 0未删除  1已删除   会员退款时，会默认删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSellerOrder{" +
            "id=" + id +
            ", orderId=" + orderId +
            ", orderDetailId=" + orderDetailId +
            ", saleMemberId=" + saleMemberId +
            ", refereesMemberId=" + refereesMemberId +
            ", orderMoney=" + orderMoney +
            ", buyerUserId=" + buyerUserId +
            ", sellerUserId=" + sellerUserId +
            ", isDelete=" + isDelete +
            "}";
    }
}
