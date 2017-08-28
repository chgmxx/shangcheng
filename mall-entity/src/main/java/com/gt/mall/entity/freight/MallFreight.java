package com.gt.mall.entity.freight;

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
 * 物流表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_freight" )
public class MallFreight extends Model< MallFreight > {

    private static final long serialVersionUID = 1L;

    /**
     * 运费模板标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 物流名称
     */
    private String     name;
    /**
     * 用户id  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 默认快递id
     */
    @TableField( "express_id" )
    private Integer    expressId;
    /**
     * 默认快递名称
     */
    private String     express;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 编辑模板时间
     */
    @TableField( "edit_time" )
    private Date       editTime;
    /**
     * 默认运费(首件运费)
     */
    private BigDecimal money;
    /**
     * 是否免邮 1自定义运费 2卖家承担运费
     */
    @TableField( "is_no_money" )
    private Integer    isNoMoney;
    /**
     * 免邮数量
     */
    @TableField( "no_money_num" )
    private Integer    noMoneyNum;
    /**
     * 是否指定条件包邮 0 不是  1 是
     */
    @TableField( "is_result_money" )
    private Integer    isResultMoney;
    /**
     * 是否删除 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;
    /**
     * 免邮价格
     */
    @TableField( "no_money" )
    private BigDecimal noMoney;
    /**
     * 计价方式 0 统一运费 1按件数 2按重量  3按公里
     */
    @TableField( "price_type" )
    private Integer    priceType;
    /**
     * 首件数量或重量  根据计价方式来决定
     */
    @TableField( "first_nums" )
    private BigDecimal firstNums;
    /**
     * 续件数量或重量  根据计价方式来决定
     */
    @TableField( "add_nums" )
    private BigDecimal addNums;
    /**
     * 续件运费
     */
    @TableField( "add_money" )
    private BigDecimal addMoney;

    @TableField(exist = false)
    private List<MallFreightDetail> detailList;

    @TableField(exist = false)
    private String stoName;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallFreight{" +
			"id=" + id +
			", name=" + name +
			", userId=" + userId +
			", shopId=" + shopId +
			", expressId=" + expressId +
			", express=" + express +
			", createTime=" + createTime +
			", editTime=" + editTime +
			", money=" + money +
			", isNoMoney=" + isNoMoney +
			", noMoneyNum=" + noMoneyNum +
			", isResultMoney=" + isResultMoney +
			", isDelete=" + isDelete +
			", noMoney=" + noMoney +
			", priceType=" + priceType +
			", firstNums=" + firstNums +
			", addNums=" + addNums +
			", addMoney=" + addMoney +
			"}";
    }
}
