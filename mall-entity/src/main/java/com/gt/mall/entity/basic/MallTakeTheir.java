package com.gt.mall.entity.basic;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 到店自提表
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_take_their" )
public class MallTakeTheir extends Model< MallTakeTheir > {

    private static final long serialVersionUID = 1L;

    /**
     * 自提点标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 自提点名称
     */
    @TableField( "visit_name" )
    private String  visitName;
    /**
     * 省份id
     */
    @TableField( "visit_province_id" )
    private Integer visitProvinceId;
    /**
     * 城市id
     */
    @TableField( "visit_city_id" )
    private Integer visitCityId;
    /**
     * 区域id
     */
    @TableField( "visit_area_id" )
    private Integer visitAreaId;
    /**
     * 地址
     */
    @TableField( "visit_address" )
    private String  visitAddress;
    /**
     * 经度
     */
    @TableField( "visit_longitude" )
    private String  visitLongitude;
    /**
     * 维度
     */
    @TableField( "visit_latitude" )
    private String  visitLatitude;
    /**
     * 详细地址
     */
    @TableField( "visit_address_detail" )
    private String  visitAddressDetail;
    /**
     * 联系电话
     */
    @TableField( "visit_contact_number" )
    private String  visitContactNumber;
    /**
     * 自提点备注
     */
    @TableField( "visit_remark" )
    private String  visitRemark;
    /**
     * 是否同时作为线下门店接待 0,不接待  1,接待
     */
    @TableField( "is_store_reception" )
    private Integer isStoreReception;
    /**
     * 是否允许到店支付 0允许  1不允许
     */
    @TableField( "is_store_pay" )
    private Integer isStorePay;
    /**
     * 多张图片，用逗号隔开
     */
    @TableField( "images_url" )
    private String  imagesUrl;
    /**
     * 用户id 关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;
    /**
     * 是否删除 0未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @TableField( exist = false )
    private List< MallImageAssociative > imageList;

    @TableField( exist = false )
    private List< MallTakeTheirTime > timeList;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallTakeTheir{" +
            "id=" + id +
            ", visitName=" + visitName +
            ", visitProvinceId=" + visitProvinceId +
            ", visitCityId=" + visitCityId +
            ", visitAreaId=" + visitAreaId +
            ", visitAddress=" + visitAddress +
            ", visitLongitude=" + visitLongitude +
            ", visitLatitude=" + visitLatitude +
            ", visitAddressDetail=" + visitAddressDetail +
            ", visitContactNumber=" + visitContactNumber +
            ", visitRemark=" + visitRemark +
            ", isStoreReception=" + isStoreReception +
            ", isStorePay=" + isStorePay +
            ", imagesUrl=" + imagesUrl +
            ", userId=" + userId +
            ", createTime=" + createTime +
            ", isDelete=" + isDelete +
            "}";
    }
}
