package com.gt.mall.entity.product;

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
 * 购物车
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_shop_cart" )
public class MallShopCart extends Model< MallShopCart > {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 店铺id 关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 商品规格id，多个规格，用逗号分开
     */
    @TableField( "product_specificas" )
    private String     productSpecificas;
    /**
     * 购买商品数量 单价
     */
    @TableField( "product_num" )
    private Integer    productNum;
    /**
     * 买家留言
     */
    @TableField( "product_message" )
    private String     productMessage;
    /**
     * 买家id 关联t_wx_bus_member表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 产品规格,多个规格，用逗号分开
     */
    @TableField( "product_speciname" )
    private String     productSpeciname;
    /**
     * 折后单价格
     */
    private BigDecimal price;
    /**
     * 商品原价
     */
    @TableField( "primary_price" )
    private BigDecimal primaryPrice;
    /**
     * 商品编码
     */
    @TableField( "pro_code" )
    private String     proCode;
    /**
     * 买家类型 1 微信端买家  2 PC端买家
     */
    @TableField( "user_type" )
    private Integer    userType;
    /**
     * 折扣数
     */
    private String     discount;
    /**
     * 商品的规格（批发商品），以json方式存储{规格id:{num:数量,value:规格值,price:价格},规格id:{num:数量,value:规格值,price:价格}}
     */
    @TableField( "pro_spec_str" )
    private String     proSpecStr;
    /**
     * 商品类型 0普通商品  1 批发商品(手批) 2 批发商品(混批)
     */
    @TableField( "pro_type" )
    private Integer    proType;
    /**
     * 销售员id  关联t_wx_bus_member表的id
     */
    @TableField( "sale_member_id" )
    private Integer    saleMemberId;
    /**
     * 销售佣金
     */
    private BigDecimal commission;
    /**
     * 选中状态 0未选中  1选中
     */
    @TableField( "is_check" )
    private Integer    isCheck;
    /**
     * session标识
     */
    @TableField( "session_id" )
    private String     sessionId;
    /**
     * 商家用户id
     */
    @TableField( "bus_user_id" )
    private Integer    busUserId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallShopCart{" +
			"id=" + id +
			", productId=" + productId +
			", shopId=" + shopId +
			", productSpecificas=" + productSpecificas +
			", productNum=" + productNum +
			", productMessage=" + productMessage +
			", userId=" + userId +
			", createTime=" + createTime +
			", productSpeciname=" + productSpeciname +
			", price=" + price +
			", primaryPrice=" + primaryPrice +
			", proCode=" + proCode +
			", userType=" + userType +
			", discount=" + discount +
			", proSpecStr=" + proSpecStr +
			", proType=" + proType +
			", saleMemberId=" + saleMemberId +
			", commission=" + commission +
			", isCheck=" + isCheck +
			", sessionId=" + sessionId +
			", busUserId=" + busUserId +
			"}";
    }
}
