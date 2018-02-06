package com.gt.mall.entity.presale;

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
 * 商品预售赠送礼品设置
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_presale_give" )
public class MallPresaleGive extends Model< MallPresaleGive > {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * 礼品类型 1,积分  2,粉币 3 实体物品
     */
    @TableField( "give_type" )
    private Integer giveType;
    /**
     * 礼品名称
     */
    @TableField( "give_name" )
    private String  giveName;
    /**
     * 礼品数量
     */
    @TableField( "give_num" )
    private Integer giveNum;
    /**
     * 赠送排名
     */
    @TableField( "give_ranking" )
    private Integer giveRanking;
    /**
     * 创建用户  关联bus_user表的id字段
     */
    @TableField( "user_id" )
    private Integer userId;
    /**
     * 是否已经删除  0 未删除  1已删除
     */
    @TableField( "is_delete" )
    private Integer isDelete;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "MallPresaleGive{" +
            "id=" + id +
            ", giveType=" + giveType +
            ", giveName=" + giveName +
            ", giveNum=" + giveNum +
            ", giveRanking=" + giveRanking +
            ", userId=" + userId +
            ", isDelete=" + isDelete +
            "}";
    }
}
