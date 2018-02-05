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
 * 商品表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_product" )
public class MallProduct extends Model< MallProduct > {

    private static final long serialVersionUID = 1L;

    /**
     * 产品标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer    id;
    /**
     * 购买方式
     * 1:在多粉购买  2:链接到外部购买
     */
    @TableField( "pro_buy_way" )
    private Integer    proBuyWay;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer    shopId;
    /**
     * 商品分组id 关联t_mall_group表的id（目前没有用到）
     */
    @TableField( "group_id" )
    private Integer    groupId;
    /**
     * 商品类型
     * 0,实物商品 1,虚拟商品非会员卡 2虚拟商品（会员卡） 3 虚拟商品（卡券包） 4虚拟商品（流量包）
     */
    @TableField( "pro_type_id" )
    private Integer    proTypeId;
    /**
     * 是否是预售商品
     * 0:不是预售商品  1:是预售商品
     */
    @TableField( "is_presell" )
    private Integer    isPresell;
    /**
     * 预售结束时间
     */
    @TableField( "pro_presell_end" )
    private String     proPresellEnd;
    /**
     * 预计发货开始时间
     */
    @TableField( "pro_delivery_start" )
    private String     proDeliveryStart;
    /**
     * 预计发货结束时间
     */
    @TableField( "pro_delivery_end" )
    private String     proDeliveryEnd;
    /**
     * 库存总数
     */
    @TableField( "pro_stock_total" )
    private Integer    proStockTotal;
    /**
     * 总销量
     */
    @TableField( "pro_sale_total" )
    private Integer    proSaleTotal;
    /**
     * 是否在页面显示库存
     * 0不在页面显示 1在页面显示
     */
    @TableField( "is_show_stock" )
    private Integer    isShowStock;
    /**
     * 商家编码
     */
    @TableField( "pro_code" )
    private String     proCode;
    /**
     * 商品名称
     */
    @TableField( "pro_name" )
    private String     proName;
    /**
     * 商品真实价格
     */
    @TableField( "pro_price" )
    private BigDecimal proPrice;
    /**
     * 商品虚拟原价（只仅限于展示）
     */
    @TableField( "pro_cost_price" )
    private BigDecimal proCostPrice;
    /**
     * 运费设置 1:统一运费  2:采用运费模板
     */
    @TableField( "pro_freight_set" )
    private Integer    proFreightSet;
    /**
     * 运费模板id
     */
    @TableField( "pro_freight_temp_id" )
    private Integer    proFreightTempId;
    /**
     * 统一运费价格
     * 运费设置=1 的时候才会有运费价格
     */
    @TableField( "pro_freight_price" )
    private BigDecimal proFreightPrice;
    /**
     * 限制购买数量
     * 当数量为0   代表不限购
     */
    @TableField( "pro_restriction_num" )
    private Integer    proRestrictionNum;
    /**
     * 是否立即开售
     * 0,立即开售   1,定时开售
     */
    @TableField( "is_timing_sale" )
    private Integer    isTimingSale;
    /**
     * 定时开售时间
     */
    @TableField( "pro_time_sale" )
    private Date       proTimeSale;
    /**
     * 是否参加会员打折
     * 0:不参加会员打折   1:参加会员打折
     */
    @TableField( "is_member_discount" )
    private Integer    isMemberDiscount;
    /**
     * 是否参加优惠券
     * 0:不参加优惠券   1:参加优惠券
     */
    @TableField( "is_coupons" )
    private Integer    isCoupons;
    /**
     * 是否开发票
     * 0 不开发票  1 开发票
     */
    @TableField( "is_invoice" )
    private Integer    isInvoice;
    /**
     * 是否保修
     * 0 不保修    1保修
     */
    @TableField( "is_warranty" )
    private Integer    isWarranty;
    /**
     * 卖家id  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer    userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date       createTime;
    /**
     * 修改时间
     */
    @TableField( "edit_time" )
    private Date       editTime;
    /**
     * 是否已经发布
     * 0 还未发布   1 已经发布（上架） -1下架
     */
    @TableField( "is_publish" )
    private Integer    isPublish;
    /**
     * 审核状态 0:审核中 1:审核成功 -1:审核失败  -2：还未审核
     */
    @TableField( "check_status" )
    private Integer    checkStatus;
    /**
     * 审核不通过的原因
     */
    @TableField( "check_reason" )
    private String     checkReason;
    /**
     * 审核时间
     */
    @TableField( "check_time" )
    private Date       checkTime;
    /**
     * 是否删除
     * 0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer    isDelete;
    /**
     * 是否有规格 0没有规格 1有规格
     */
    @TableField( "is_specifica" )
    private Integer    isSpecifica;
    /**
     * 二维码保存路径
     */
    @TableField( "two_code_path" )
    private String     twoCodePath;
    /**
     * 完成订单后在有效天数内退款
     */
    @TableField( "return_day" )
    private Integer    returnDay;
    /**
     * 是否允许积分兑换商品 0 不允许兑换  1允许兑换
     */
    @TableField( "is_integral_change_pro" )
    private Integer    isIntegralChangePro;
    /**
     * 兑换积分值
     */
    @TableField( "change_integral" )
    private Integer    changeIntegral;
    /**
     * 浏览量
     */
    @TableField( "views_num" )
    private Integer    viewsNum;
    /**
     * 是否显示浏览量 0 不显示  1显示
     */
    @TableField( "is_show_views" )
    private Integer    isShowViews;
    /**
     * 是否允许退款 0 不允许  1允许
     */
    @TableField( "is_return" )
    private Integer    isReturn;
    /**
     * 购买会员卡类型 关联 t_member_cardtype表的id
     */
    @TableField( "member_type" )
    private Integer    memberType;
    /**
     * 是否开启积分抵扣 0 不开启  1开启
     */
    @TableField( "is_integral_deduction" )
    private Integer    isIntegralDeduction;
    /**
     * 是否开启粉币抵扣 0 不开启  1开启
     */
    @TableField( "is_fenbi_deduction" )
    private Integer    isFenbiDeduction;
    /**
     * 是否允许粉币兑换商品 0 不允许兑换  1允许兑换
     */
    @TableField( "is_fenbi_change_pro" )
    private Integer    isFenbiChangePro;
    /**
     * 兑换粉币值
     */
    @TableField( "change_fenbi" )
    private BigDecimal changeFenbi;
    /**
     * 商品标签
     */
    @TableField( "pro_label" )
    private String     proLabel;
    /**
     * 商品重量
     */
    @TableField( "pro_weight" )
    private BigDecimal proWeight;
    /**
     * 销量基数
     */
    @TableField( "sales_base" )
    private Integer    salesBase;
    /**
     * 购买卡券包id  关联 t_duofen_card_receive 表的id
     */
    @TableField( "card_type" )
    private Integer    cardType;
    /**
     * 是否在商城显示 0 在商城显示  1在采购管理显示
     */
    @TableField( "is_mall_show" )
    private Integer    isMallShow;
    /**
     * 流量id，用于流量购买
     */
    @TableField( "flow_id" )
    private Integer    flowId;
    /**
     * 流量冻结id，用于流量购买
     */
    @TableField( "flow_record_id" )
    private Integer    flowRecordId;
    /**
     * 关联erp商品id 0为没有关联erp
     */
    @TableField( "erp_pro_id" )
    private Integer    erpProId;
    /**
     * 关联erp商品库存id（规格详情id） 0为没有关联erp
     */
    @TableField( "erp_inv_id" )
    private Integer    erpInvId;
    /**
     * 是否已经同步到erp 0没有同步  1已经同步
     */
    @TableField( "is_sync_erp" )
    private Integer    isSyncErp;
    /**
     * 是否需要合并商品(针对erp)  0不需要合并 1需要合并
     */
    @TableField( "is_merge_pro_erp" )
    private Integer    isMergeProErp;
    /**
     * 商品页模板id 关联`t_mall_product_template`表的id
     */
    @TableField( "template_id" )
    private Integer    templateId;
    /**
     * 平台是否已审核 0 未审核 1已审核
     */
    @TableField( "is_platform_check" )
    private Integer    isPlatformCheck;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallProduct{" +
            "id=" + id +
            ", proBuyWay=" + proBuyWay +
            ", shopId=" + shopId +
            ", groupId=" + groupId +
            ", proTypeId=" + proTypeId +
            ", isPresell=" + isPresell +
            ", proPresellEnd=" + proPresellEnd +
            ", proDeliveryStart=" + proDeliveryStart +
            ", proDeliveryEnd=" + proDeliveryEnd +
            ", proStockTotal=" + proStockTotal +
            ", proSaleTotal=" + proSaleTotal +
            ", isShowStock=" + isShowStock +
            ", proCode=" + proCode +
            ", proName=" + proName +
            ", proPrice=" + proPrice +
            ", proCostPrice=" + proCostPrice +
            ", proFreightSet=" + proFreightSet +
            ", proFreightTempId=" + proFreightTempId +
            ", proFreightPrice=" + proFreightPrice +
            ", proRestrictionNum=" + proRestrictionNum +
            ", isTimingSale=" + isTimingSale +
            ", proTimeSale=" + proTimeSale +
            ", isMemberDiscount=" + isMemberDiscount +
            ", isCoupons=" + isCoupons +
            ", isInvoice=" + isInvoice +
            ", isWarranty=" + isWarranty +
            ", userId=" + userId +
            ", createTime=" + createTime +
            ", editTime=" + editTime +
            ", isPublish=" + isPublish +
            ", checkStatus=" + checkStatus +
            ", checkReason=" + checkReason +
            ", checkTime=" + checkTime +
            ", isDelete=" + isDelete +
            ", isSpecifica=" + isSpecifica +
            ", twoCodePath=" + twoCodePath +
            ", returnDay=" + returnDay +
            ", isIntegralChangePro=" + isIntegralChangePro +
            ", changeIntegral=" + changeIntegral +
            ", viewsNum=" + viewsNum +
            ", isShowViews=" + isShowViews +
            ", isReturn=" + isReturn +
            ", memberType=" + memberType +
            ", isIntegralDeduction=" + isIntegralDeduction +
            ", isFenbiDeduction=" + isFenbiDeduction +
            ", isFenbiChangePro=" + isFenbiChangePro +
            ", changeFenbi=" + changeFenbi +
            ", proLabel=" + proLabel +
            ", proWeight=" + proWeight +
            ", salesBase=" + salesBase +
            ", cardType=" + cardType +
            ", isMallShow=" + isMallShow +
            ", flowId=" + flowId +
            ", flowRecordId=" + flowRecordId +
            ", erpProId=" + erpProId +
            ", erpInvId=" + erpInvId +
            ", isSyncErp=" + isSyncErp +
            ", isMergeProErp=" + isMergeProErp +
            ", templateId=" + templateId +
            "}";
    }
}
