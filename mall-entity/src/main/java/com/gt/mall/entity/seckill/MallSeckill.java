package com.gt.mall.entity.seckill;

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
 * 秒杀表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seckill" )
public class MallSeckill extends Model< MallSeckill > {

    private static final long serialVersionUID = 1L;

    /**
     * 秒杀标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 商品id 关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer    productId;
    /**
     * 秒杀活动名称
     */
    @TableField( "s_name" )
    private String     sName;
    /**
     * 秒杀价
     */
    @TableField( "s_price" )
    private BigDecimal sPrice;
    /**
     * 秒杀开始时间
     */
    @TableField( "s_start_time" )
    private String     sStartTime;
    /**
     * 秒杀结束时间
     */
    @TableField( "s_end_time" )
    private String     sEndTime;
    /**
     * 商品限购数量
     */
    @TableField( "s_max_buy_num" )
    private Integer    sMaxBuyNum;
    /**
     * 创建人  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 商品所属店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 是否删除 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;
    /**
     * 是否生效 0 未生效  1已生效
     */
    @TableField( "is_use" )
    private Integer    isUse;

    /********************************* 以下参数不是表中字段要加注解   @TableField(exist = false) *******************************************/
    /**
     * 状态 不是表中字段
     */
    @TableField( exist = false )
    private Integer status;

    /**
     * 店铺名称  不是表中字段
     */
    @TableField( exist = false )
    private String shopName;

    /**
     * 秒杀价集合  不是表中字段
     */
    @TableField( exist = false )
    private List< MallSeckillPrice > priceList;

    /**
     * 剩余时间   不是表中字段
     */
    @TableField( exist = false )
    private Long times;

    /**
     * 活动开始剩余时间  不是表中字段
     */
    @TableField( exist = false )
    private Long startTimes;

    /**
     * 数量  不是表中字段
     */
    @TableField( exist = false )
    private Integer sNum;

    /**
     * 加入秒杀id 不是表中字段
     */
    @TableField( exist = false )
    private int joinId = 0;

    @TableField( exist = false )
    private String twoCodePath;

    @TableField( exist = false )
    private int wx_shop_id; //门店ID 关联wx_shop表的id

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallSeckill{" +
            "id=" + id +
            ", productId=" + productId +
            ", sName=" + sName +
            ", sPrice=" + sPrice +
            ", sStartTime=" + sStartTime +
            ", sEndTime=" + sEndTime +
            ", sMaxBuyNum=" + sMaxBuyNum +
            ", userId=" + userId +
            ", shopId=" + shopId +
            ", createTime=" + createTime +
            ", isDelete=" + isDelete +
            ", isUse=" + isUse +
            "}";
    }
}
