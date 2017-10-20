package com.gt.mall.result.phone.product;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 批发商品详情返回结果
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhonePifaProductDetailResult", description = "批发商品详情返回结果" )
@Getter
@Setter
public class PhonePifaProductDetailResult implements Serializable {

    private static final long serialVersionUID = -1254979092945866546L;

    /**
     * 批发设置
     */
    @ApiModelProperty( name = "pfSetObj", value = "批发设置 {isHpMoney:是否是混批金额条件， hpMoney:混批达到的金额条件， isHpNum:是否开启混批达到的件数， hpNum 混批达到的件数， isSpHand:是否开启混批达到的几手,spHand 手批达到的几手 }" )
    private JSONObject pfSetObj;

    /**
     * 批发状态
     */
    @ApiModelProperty( name = "pfStatus", value = "批发状态" )
    private int pfStatus = -2;

    /**
     * 批发错误提醒
     */
    @ApiModelProperty( name = "pfErrorMsg", value = "批发错误提醒" )
    private String pfErrorMsg = "";

    /**
     * 批发类型 1手批 2混批
     */
    @ApiModelProperty( name = "pfType", value = "批发类型 1手批 2混批" )
    private int pfType;

}
