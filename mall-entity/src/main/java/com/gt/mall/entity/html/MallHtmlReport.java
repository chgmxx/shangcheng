package com.gt.mall.entity.html;

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
 * h5 商城举报信息
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Data
@Accessors( chain = true )
@TableName( "t_mall_html_report" )
public class MallHtmlReport extends Model< MallHtmlReport > {

    private static final long serialVersionUID = 1L;

    @TableId( value = "id", type = IdType.AUTO )
    private Integer id;
    /**
     * t_mall_html 商城id
     */
    @TableField( "html_id" )
    private Integer htmlId;
    /**
     * 1，诈骗、反社会、谣言，2：色情、赌博、毒品，3：传销、邪教、非法集会，4：侵权、抄袭；5：恶意营销、侵犯隐私、诱导分享，6：虚假广告，7，其他原因
     */
    private Integer style;
    /**
     * 被举报的次数
     */
    @TableField( "report_num" )
    private Integer reportNum;

    @Override
    protected Serializable pkVal() {
	return this.id;
    }

    @Override
    public String toString() {
	return "MallHtmlReport{" +
			"id=" + id +
			", htmlId=" + htmlId +
			", style=" + style +
			", reportNum=" + reportNum +
			"}";
    }
}
