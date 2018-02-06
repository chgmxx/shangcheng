package com.gt.mall.entity.store;

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
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_store" )
public class MallStore extends Model< MallStore > {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 店铺名称
     */
    @TableField( "sto_name" )
    private String  stoName;
    /**
     * 联系人手机号码
     */
    @TableField( "sto_phone" )
    private String  stoPhone;
    /**
     * 联系人名称
     */
    @TableField( "sto_linkman" )
    private String  stoLinkman;
    /**
     * 店铺状态
     */
    @TableField( "sto_status" )
    private Integer stoStatus;
    /**
     * 父级ID
     */
    @TableField( "sto_pid" )
    private Integer stoPid;
    /**
     * 所属用户ID(bus_user的id)
     */
    @TableField( "sto_user_id" )
    private Integer stoUserId;
    /**
     * 是否主店(1-主店,2-非,-1 不分主店铺子店铺)
     */
    @TableField( "sto_is_main" )
    private Integer stoIsMain;
    /**
     * 详细地址
     */
    @TableField( "sto_address" )
    private String  stoAddress;
    /**
     * 店铺二维码
     */
    @TableField( "sto_qr_code" )
    private String  stoQrCode;
    /**
     * 分店ID
     */
    @TableField( "sto_branch_id" )
    private Integer stoBranchId;
    /**
     * 是否开启会员折扣(暂时不用)
     */
    @TableField( "sto_is_memberDiscount" )
    private Integer stoIsMemberDiscount;
    /**
     * 门牌号
     */
    @TableField( "sto_house_member" )
    private String  stoHouseMember;
    /**
     * 创建时间
     */
    @TableField( "sto_create_time" )
    private Date    stoCreateTime;
    /**
     * 创建人(bus_user中的id)
     */
    @TableField( "sto_create_person" )
    private Integer stoCreatePerson;
    /**
     * 所属省份
     */
    @TableField( "sto_province" )
    private Integer stoProvince;
    /**
     * 所属城市
     */
    @TableField( "sto_city" )
    private Integer stoCity;
    /**
     * 经度
     */
    @TableField( "sto_longitude" )
    private String  stoLongitude;
    /**
     * 纬度
     */
    @TableField( "sto_latitude" )
    private String  stoLatitude;
    /**
     * 图片
     */
    @TableField( "sto_picture" )
    private String  stoPicture;
    /**
     * 是否删除 0未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 所属区域
     */
    @TableField( "sto_area" )
    private Integer stoArea;
    /**
     * 店铺id,关联t_wx_shop表的id字段
     */
    @TableField( "wx_shop_id" )
    private Integer wxShopId;
    /**
     * 是否开启短信推送  1推送 0 不推送
     */
    @TableField( "sto_is_sms" )
    private Integer stoIsSms;
    /**
     * 短信推送手机号码   保存多个手机号用分号;隔开
     */
    @TableField( "sto_sms_telephone" )
    private String  stoSmsTelephone;
    /**
     * QQ客服
     */
    @TableField( "sto_qq_customer" )
    private String  stoQqCustomer;
    /**
     * 商城店铺头像
     */
    @TableField( "sto_head_img" )
    private String  stoHeadImg;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallStore{" +
            "id=" + id +
            ", stoName=" + stoName +
            ", stoPhone=" + stoPhone +
            ", stoLinkman=" + stoLinkman +
            ", stoStatus=" + stoStatus +
            ", stoPid=" + stoPid +
            ", stoUserId=" + stoUserId +
            ", stoIsMain=" + stoIsMain +
            ", stoAddress=" + stoAddress +
            ", stoQrCode=" + stoQrCode +
            ", stoBranchId=" + stoBranchId +
            ", stoIsMemberDiscount=" + stoIsMemberDiscount +
            ", stoHouseMember=" + stoHouseMember +
            ", stoCreateTime=" + stoCreateTime +
            ", stoCreatePerson=" + stoCreatePerson +
            ", stoProvince=" + stoProvince +
            ", stoCity=" + stoCity +
            ", stoLongitude=" + stoLongitude +
            ", stoLatitude=" + stoLatitude +
            ", stoPicture=" + stoPicture +
            ", isDelete=" + isDelete +
            ", stoArea=" + stoArea +
            ", wxShopId=" + wxShopId +
            ", stoIsSms=" + stoIsSms +
            ", stoSmsTelephone=" + stoSmsTelephone +
            ", stoQqCustomer=" + stoQqCustomer +
            ", stoHeadImg=" + stoHeadImg +
            "}";
    }
}
