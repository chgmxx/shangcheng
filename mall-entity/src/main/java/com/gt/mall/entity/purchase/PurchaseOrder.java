package com.gt.mall.entity.purchase;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Data
@Accessors( chain = true )
@TableName( "purchase_order" )
public class PurchaseOrder extends Model< PurchaseOrder > {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 订单号
     */
    @TableField( "order_number" )
    private String  orderNumber;
    /**
     * 公司id
     */
    @TableField( "company_id" )
    private Integer companyId;
    /**
     * 订单标题
     */
    @TableField( "order_title" )
    private String  orderTitle;
    /**
     * 创建时间
     */
    @TableField( "create_date" )
    private Date    createDate;
    /**
     * 订单描述
     */
    @TableField( "order_describe" )
    private String  orderDescribe;
    /**
     * 订单说明
     */
    @TableField( "order_explain" )
    private String  orderExplain;
    /**
     * 订单二维码
     */
    @TableField( "order_qrcode" )
    private String  orderQrcode;
    /**
     * 0全款1分期
     */
    @TableField( "order_type" )
    private String  orderType;
    /**
     * 0签1不签
     */
    @TableField( "have_contract" )
    private String  haveContract;
    /**
     * 0含1不含
     */
    @TableField( "have_tax" )
    private String  haveTax;
    /**
     * 订单运费
     */
    private Double  freight;
    /**
     * 订单总额
     */
    @TableField( "all_money" )
    private Double  allMoney;
    /**
     * 0已关闭1待发布2待付款3已付款4已完成
     */
    @TableField( "order_status" )
    private String  orderStatus;
    /**
     * 订单备注
     */
    @TableField( "order_remarks" )
    private String  orderRemarks;
    /**
     * 商家id
     */
    @TableField( "bus_id" )
    private Integer busId;
    /**
     * 用户id
     */
    @TableField( "member_id" )
    private Integer memberId;

    @TableField( exist = false )
    private String contractId;//合同Id

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
            "id=" + id +
            ", orderNumber=" + orderNumber +
            ", companyId=" + companyId +
            ", orderTitle=" + orderTitle +
            ", createDate=" + createDate +
            ", orderDescribe=" + orderDescribe +
            ", orderExplain=" + orderExplain +
            ", orderQrcode=" + orderQrcode +
            ", orderType=" + orderType +
            ", haveContract=" + haveContract +
            ", haveTax=" + haveTax +
            ", freight=" + freight +
            ", allMoney=" + allMoney +
            ", orderStatus=" + orderStatus +
            ", orderRemarks=" + orderRemarks +
            ", busId=" + busId +
            ", memberId=" + memberId +
            "}";
    }
}
