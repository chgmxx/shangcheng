package com.gt.mall.entity.seller;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 超级销售员商城设置
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_seller_mallset" )
public class MallSellerMallset extends Model< MallSellerMallset > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 店铺id 关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 商城名称
     */
    @TableField( "mall_name" )
    private String  mallName;
    /**
     * 联系电话
     */
    @TableField( "contact_number" )
    private String  contactNumber;
    /**
     * qq
     */
    private String  qq;
    /**
     * 商城头像地址
     */
    @TableField( "mall_head_path" )
    private String  mallHeadPath;
    /**
     * banner地址
     */
    @TableField( "banner_path" )
    private String  bannerPath;
    /**
     * 商城简介
     */
    @TableField( "mall_introducation" )
    private String  mallIntroducation;
    /**
     * 是否开启自选  0没有开启自选   1开启自选
     */
    @TableField( "is_open_optional" )
    private Integer isOpenOptional;
    /**
     * 销售员id  关联t_wx_bus_member表的id
     */
    @TableField( "sale_member_id" )
    private Integer saleMemberId;
    /**
     * 商家id 关联bus_user表的id
     */
    @TableField( "bus_user_id" )
    private Integer busUserId;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallSellerMallset{" +
			"id=" + id +
			", shopId=" + shopId +
			", mallName=" + mallName +
			", contactNumber=" + contactNumber +
			", qq=" + qq +
			", mallHeadPath=" + mallHeadPath +
			", bannerPath=" + bannerPath +
			", mallIntroducation=" + mallIntroducation +
			", isOpenOptional=" + isOpenOptional +
			", saleMemberId=" + saleMemberId +
			", busUserId=" + busUserId +
			"}";
    }
}
