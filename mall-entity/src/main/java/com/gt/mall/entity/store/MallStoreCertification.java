package com.gt.mall.entity.store;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.gt.mall.entity.basic.MallImageAssociative;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 店铺认证表
 * </p>
 *
 * @author yangqian
 * @since 2017-09-19
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_store_certification" )
public class MallStoreCertification extends Model< MallStoreCertification > {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺认证标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "sto_id" )
    private Integer stoId;
    /**
     * 主体类型 0个人 1企业
     */
    @TableField( "sto_type" )
    private Integer stoType;
    /**
     * 姓名或法人
     */
    private String  name;
    /**
     * 企业名称
     */
    @TableField( "company_name" )
    private String  companyName;
    /**
     * 身份证号码
     */
    @TableField( "id_number" )
    private String  idNumber;
    /**
     * 身份证正面
     */
    @TableField( "id_card_front" )
    private String  idCardFront;
    /**
     * 身份证反面
     */
    @TableField( "id_card_back" )
    private String  idCardBack;
    /**
     * 营业执照
     */
    @TableField( "bus_license_img" )
    private String  busLicenseImg;
    /**
     * 营业执照号
     */
    @TableField( "bus_license_no" )
    private String  busLicenseNo;
    /**
     * 店铺类型  1 普通店铺  2旗舰店  3专卖店  4直营店
     */
    @TableField( "sto_category" )
    private Integer stoCategory;
    /**
     * 认证类型   6 商标注册通知书 7商标注册证  8商标使用许可证明  9微信渠道授权书 10多粉渠道授权书  11公司总部证明函 12关系证明函
     */
    @TableField( "cert_type" )
    private Integer certType;
    /**
     * 商标注册通知书,商标注册证，商标使用许可合同，渠道授权书，证明函文件
     */
    @TableField( "cert_img_url" )
    private String  certImgUrl;
    /**
     * 商标注册证 旗舰店2 认证8使用
     */
    @TableField( "trade_mark_img" )
    private String  tradeMarkImg;
    /**
     * 是否有补充资料  0无 1有
     */
    @TableField( "is_cert_doc" )
    private Integer isCertDoc;
    /**
     * 是否已删除 0未删除 1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;
    /**
     * 审核状态 0未审核 1审核通过 -1审核不通过
     */
    @TableField( "check_status" )
    private Integer checkStatus;
    /**
     * 不通过理由
     */
    @TableField( "refuse_reason" )
    private String  refuseReason;
    /**
     * 创建时间
     */
    @TableField( "create_time" )
    private Date    createTime;

    /**
     * 认证店铺名称
     */
    @TableField( exist = false )
    private String stoCategoryName;

    /**
     * 补充资料
     */
    @TableField( exist = false )
    private List< MallImageAssociative > imageList;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

}
