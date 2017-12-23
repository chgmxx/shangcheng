package com.gt.mall.param.applet;

import com.gt.mall.entity.product.MallProduct;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 提交订单参数
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Getter
@Setter
public class AppletSubmitOrderProductDTO implements Serializable {

    private static final long serialVersionUID = 4521974190766891350L;
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 商品规格id
     */
    private String  productSpecificas;
    /**
     * 商品规格名称
     */
    private String  productSpeciname;

    /**
     * 商品图片 必传
     */
    private String productImageUrl;

    /**
     * 商品数量 必传
     */
    private Integer detProNum;

    /**
     * 商品实付单价，折扣优惠后的单价 必传
     */
    private Double detProPrice;
    /**
     * 实付的总价 必传
     */
    private Double totalPrice;

    /**
     * 商品原价 折扣优惠前的单价
     */
    private Double detPrivivilege;
    /**
     * 优惠的价格（优惠前的价格-优惠后的价格）
     */
    private Double discountedPrices;
    /**
     * 买家留言
     */
    private String detProMessage;
    /**
     * 折扣值
     */
    private Double discount;

    /**
     * 购买卡券包id   卡券包购买时所需
     */
    private String cardReceiveId;
    /**
     * 卡券包Id
     */
    private String useCardId;
    //以下参数 超级销售员 所需

    /**
     * 销售佣金
     */
    private Double commission;

    /**
     * 销售员id
     */
    private Integer saleMemberId;

    /**
     * 批发商品的规格
     */
    private String proSpecStr;

    /**
     * 订单详情id
     */
    private Integer detailId;

    @ApiModelProperty( name = "mallProduct", value = "商品对象", hidden = true )
    private MallProduct mallProduct;

}
