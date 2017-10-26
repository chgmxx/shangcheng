package com.gt.mall.result.phone.order;

import com.gt.mall.bean.member.JifenAndFenbiRule;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 商家（进入提交订单页面返回结果）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneToOrderBusResult", description = "商家（进入提交订单页面返回结果）" )
@Getter
@Setter
public class PhoneToOrderBusResult implements Serializable {

    private static final long serialVersionUID = 1650253331649286295L;

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    @ApiModelProperty( name = "busName", value = "商家名称" )
    private String busName;

    @ApiModelProperty( name = "publicId", value = "公众号id" )
    private Integer publicId;

    @ApiModelProperty( name = "busImageUrl", value = "商家头像" )
    private String busImageUrl;

    @ApiModelProperty( name = "productTotalMoney", value = "商品总价" )
    private double productTotalMoney;

    @ApiModelProperty( name = "productFreightMoney", value = "商品运费" )
    private double productFreightMoney;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private int totalNum;

    @ApiModelProperty( name = "totalMoney", value = "订单总价（包含运费）" )
    private double totalMoney;

    @ApiModelProperty( name = "payWay", value = "支付方式集合" )
    private List< PhoneToOrderWayResult > payWayList;

    @ApiModelProperty( name = "deliveryWayList", value = "配送方式集合" )
    private List< PhoneToOrderWayResult > deliveryWayList;

    @ApiModelProperty( name = "toOrderShopResultList", value = "店铺集合" )
    private List< PhoneToOrderShopResult > shopResultList;

    @ApiModelProperty( name = "takeId", value = "上门自提id" )
    private Integer takeId;

    @ApiModelProperty( name = "takeAddress", value = "上门自提地址" )
    private String takeAddress;

    @ApiModelProperty( name = "memberDiscount", value = "会员折扣数" )
    private double memberDiscount = 0;

    @ApiModelProperty( name = "jifenNum", value = "会员拥有的积分数量" )
    private double jifenNum = 0;

    @ApiModelProperty( name = "jifenMoney", value = "积分数量抵扣的积分金额" )
    private double jifenMoney = 0;

    @ApiModelProperty( name = "fenbiNum", value = "粉币数量" )
    private double fenbiNum = 0;

    @ApiModelProperty( name = "fenbiMoney", value = "粉币数量抵扣的粉币金额" )
    private double fenbiMoney = 0;

    @ApiModelProperty( name = "jifenFenbiRule", value = "积分粉币兑换规则" )
    private JifenAndFenbiRule jifenFenbiRule;

    @ApiModelProperty( name = "memberPhone", value = "粉丝手机号码" )
    private String memberPhone;

    /*********************************提交订单接口，传值***************************************/

    @ApiModelProperty( name = "selectPayWayId", value = "选中支付方式id" )
    private int selectPayWayId = 0;

    @ApiModelProperty( name = "selectDeliveryWayId", value = "选中配送方式的id" )
    private int selectDeliveryWayId = 0;

    @ApiModelProperty( name = "isSelectJifen", value = "是否选中积分  1选中" )
    private Integer isSelectJifen = 0;

    @ApiModelProperty( name = "isSelectFenbi", value = "是否选中粉币  1选中" )
    private Integer isSelectFenbi = 0;

    @ApiModelProperty( name = "tihuoUserName", value = "提货人姓名" )
    private String appointmentUserName;

    @ApiModelProperty( name = "tihuoUserPhone", value = "提货人手机号码" )
    private String appointmentUserPhone;

    @ApiModelProperty( name = "appointmentId", value = "提货id" )
    private Integer appointmentId;

    @ApiModelProperty( name = "appointmentStartTime", value = "提货开始时间" )
    private String appointmentStartTime;

    @ApiModelProperty( name = "appointmentEndTime", value = "提货结束时间" )
    private String appointmentEndTime;

}
