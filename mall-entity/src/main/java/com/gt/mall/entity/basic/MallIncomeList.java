package com.gt.mall.entity.basic;

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
 * 收入记录表
 * </p>
 *
 * @author yangqian
 * @since 2018-01-30
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_income_list" )
public class MallIncomeList extends Model< MallIncomeList > {

    private static final long serialVersionUID = 1L;

    /**
     * 收入记录标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 类型  1收入 2支出
     */
    @TableField( "income_type" )
    private Integer    incomeType;
    /**
     * 操作 1营业额 2收入
     */
    @TableField( "income_category" )
    private Integer    incomeCategory;
    /**
     * 金额
     */
    @TableField( "income_money" )
    private BigDecimal incomeMoney;
    /**
     * 用户id(bus_user中的id)
     */
    @TableField( "bus_id" )
    private Integer    busId;
    /**
     * 店铺ID
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 商品名称
     */
    @TableField( "pro_name" )
    private String     proName;
    /**
     * 订单号/竞拍编号
     */
    @TableField( "pro_no" )
    private String     proNo;
    /**
     * 付款人
     */
    @TableField( "buyer_id" )
    private Integer    buyerId;
    /**
     * 付款人名称
     */
    @TableField( "buyer_name" )
    private String     buyerName;
    /**
     * 交易ID
     */
    @TableField( "trade_id" )
    private Integer    tradeId;
    /**
     * 关联表类型 1 关联订单表 t_mall_order表的id  2关联订单详情表 t_mall_order_detail表的id
     * 3 关联预售定金表表 t_mall_presale_deposit表的id 4 关联拍卖保证金表 t_mall_auction_margin表的id
     */
    @TableField( "trade_type" )
    private Integer    tradeType;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

}
