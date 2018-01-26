package com.gt.mall.param.phone.order.add;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 商家（提交订单接口的参数）
 * User : yangqian
 * Date : 2017/10/12 0012
 * Time : 20:11
 */
@ApiModel( value = "PhoneAddOrderBusResult", description = "商家（提交订单接口的参数）" )
@Getter
@Setter
public class PhoneAddOrderBusDTO {

    @ApiModelProperty( name = "busId", value = "商家id" )
    private Integer busId;

    //    @ApiModelProperty( name = "busName", value = "商家名称" )
    //    private String busName;
    //
    //    @ApiModelProperty( name = "publicId", value = "公众号id" )
    //    private Integer publicId;
    //
    //    @ApiModelProperty( name = "busImageUrl", value = "商家头像" )
    //    private String busImageUrl;

    @ApiModelProperty( name = "productTotalMoney", value = "商品总价" )
    private double productTotalMoney;

    @ApiModelProperty( name = "productFreightMoney", value = "商品运费" )
    private double productFreightMoney;

    @ApiModelProperty( name = "totalNum", value = "商品总数" )
    private int totalNum;

    @ApiModelProperty( name = "totalMoney", value = "订单总价（包含运费）" )
    private double totalMoney;

    //    @ApiModelProperty( name = "deliveryWayList", value = "配送方式集合" )
    //    private List< PhoneOrderWayDTO > deliveryWayList;

    @ApiModelProperty( name = "toOrderShopResultList", value = "店铺集合" )
    private List< PhoneAddOrderShopDTO > shopResultList;

    @ApiModelProperty( name = "jifenNum", value = "能抵扣的积分数量" )
    private Double jifenNum;

    @ApiModelProperty( name = "jifenMoney", value = "积分数量抵扣的积分金额" )
    private Double jifenMoney;

    @ApiModelProperty( name = "fenbiNum", value = "能抵扣的粉币数量" )
    private Double fenbiNum;

    @ApiModelProperty( name = "fenbiMoney", value = "粉币数量抵扣的粉币金额" )
    private Double fenbiMoney;

    //    @ApiModelProperty( name = "jifenFenbiRule", value = "积分粉币兑换规则" )
    //    private JifenAndFenbiRule jifenFenbiRule;

    @ApiModelProperty( name = "memberPhone", value = "粉丝手机号码" )
    private String memberPhone;

    /*********************************联盟卡返回的接口***************************************/

    @ApiModelProperty( name = "unionStatus", value = "联盟状态 -1：没有联盟信息，不显示 0：没有关联联盟卡，需绑定联盟卡 1：返回联盟折扣" )
    private Integer unionStatus;

    @ApiModelProperty( name = "unionDiscount", value = "联盟折扣" )
    private Double unionDiscount;

    @ApiModelProperty( name = "unionId", value = "联盟id" )
    private Integer unionId;

    @ApiModelProperty( name = "unionCardId", value = "联盟卡id" )
    private Integer unionCardId;

    /*********************************提交订单接口，传值***************************************/

    @ApiModelProperty( name = "selectDeliveryWayId", value = "选中配送方式的id  1, 快递配送  2,上门自提  3到店购买" )
    private Integer selectDeliveryWayId = 0;

    @ApiModelProperty( name = "isSelectJifen", value = "是否选中积分  1选中" )
    private Integer isSelectJifen = 0;

    @ApiModelProperty( name = "isSelectFenbi", value = "是否选中粉币  1选中" )
    private Integer isSelectFenbi = 0;

    @ApiModelProperty( name = "isSelectDiscount", value = "是否选中会员卡折扣 1选中" )
    private Integer isSelectDiscount = 0;

    @ApiModelProperty( name = "isSelectCoupons", value = "是否选中了优惠券  1选中" )
    private Integer isSelectCoupons = 0;

    @ApiModelProperty( name = "isSelectUnion", value = "是否选中联盟 1选中" )
    private Integer isSelectUnion = 0;

    /*********************************上门自提，传值***************************************/

    @ApiModelProperty( name = "takeAddress", value = "上门自提地址" )
    private String takeAddress;

    @ApiModelProperty( name = "appointmentUserName", value = "提货人姓名" )
    private String appointmentUserName;

    @ApiModelProperty( name = "appointmentUserPhone", value = "提货人手机号码" )
    private String appointmentUserPhone;

    @ApiModelProperty( name = "appointmentId", value = "提货id" )
    private Integer appointmentId;

    @ApiModelProperty( name = "appointmentDate", value = "提货日期" )
    private String appointmentDate;

    @ApiModelProperty( name = "appointmentStartTime", value = "提货开始时间" )
    private String appointmentStartTime;

    @ApiModelProperty( name = "appointmentEndTime", value = "提货结束时间" )
    private String appointmentEndTime;

    /*************************************   以下参数不做传值，只做后续计算和判断    ******************************************/

    @ApiModelProperty( name = "totalNewMoney", value = "商品优惠后的总价（没包含运费）", hidden = true )
    private double totalNewMoney = 0;

    @ApiModelProperty( name = "memberDiscount", value = "会员折扣数", hidden = true )
    private double memberDiscount = 0;

}
