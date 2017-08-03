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
import java.util.List;

/**
 * <p>
 * 物流表详情
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_freight_detail" )
public class MallFreightDetail extends Model< MallFreightDetail > {

    private static final long serialVersionUID = 1L;

    /**
     * 物流详情标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 物流id  关联t_mall_freight表的id
     */
    @TableField( "freight_id" )
    private Integer    freightId;
    /**
     * 配送省Id
     */
    @TableField( "provinces_id" )
    private String     provincesId;
    /**
     * 配送省
     */
    private String     provinces;
    /**
     * 运费
     */
    private BigDecimal money;
    /**
     * 默认快递id
     */
    @TableField( "express_id" )
    private Integer    expressId;
    /**
     * 默认快递
     */
    private String     express;
    /**
     * 是否删除 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;
    /**
     * 免邮数量
     */
    @TableField( "no_money_num" )
    private Integer    noMoneyNum;
    /**
     * 免邮价格
     */
    @TableField( "no_money" )
    private BigDecimal noMoney;
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
    private List<MallFreightProvinces> provinceList;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallFreightDetail{" +
			"id=" + id +
			", freightId=" + freightId +
			", provincesId=" + provincesId +
			", provinces=" + provinces +
			", money=" + money +
			", expressId=" + expressId +
			", express=" + express +
			", isDelete=" + isDelete +
			", noMoneyNum=" + noMoneyNum +
			", noMoney=" + noMoney +
			", firstNums=" + firstNums +
			", addNums=" + addNums +
			", addMoney=" + addMoney +
			"}";
    }
}
