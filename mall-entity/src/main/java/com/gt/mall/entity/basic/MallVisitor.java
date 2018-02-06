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

/**
 * <p>
 * 店铺访客表
 * </p>
 *
 * @author yangqian
 * @since 2017-10-26
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_visitor" )
public class MallVisitor extends Model< MallVisitor > {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺访客标识
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 商家id  关联bus_user表的id
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 店铺id  关联t_mall_store表的id
     */
    @TableField( "shop_id" )
    private Integer shopId;
    /**
     * 页面id  关联t_mall_page表的id
     */
    @TableField( "page_id" )
    private Integer pageId;
    /**
     * 商品id  关联t_mall_product表的id
     */
    @TableField( "product_id" )
    private Integer productId;
    /**
     * 访问IP
     */
    @TableField( "access_ip" )
    private String  accessIp;
    /**
     * 访问时间
     */
    @TableField( "access_time" )
    private Date    accessTime;
    /**
     * 访问次数
     */
    @TableField( "access_count" )
    private Integer accessCount;
    /**
     * 会员id  关联t_wx_bus_member表的id
     */
    @TableField( "member_id" )
    private Integer memberId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
