package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.param.phone.PhoneProductDetailDTO;
import com.gt.mall.param.phone.PhoneSpecificaDTO;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新商品 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductNewService extends BaseService< MallProduct > {

    /**
     * 查询商品详情数据
     */
    PhoneProductDetailResult selectProductDetail( PhoneProductDetailDTO params, Member member, MallPaySet mallPaySet );

    /**
     * 获取商品规格价
     *
     * @param params 参数
     * @param member 会员
     *
     * @return 商品规格价
     */
    List< Map< String,Object > > getProductSpecificaPrice( PhoneSpecificaDTO params, Member member );

    /**
     * 1 判断商品是否被删除或未上架、未审核
     * 2 判断商品库存是否足够
     * 3 判断是否已经超过了限购,如果不够会抛异常
     * 4 如果是活动商品，判断活动是否正在进行 和 是否超过活动限购的数量
     * 5 如果是卡券包购买，判断是否已过期
     *
     * @param proId         商品id
     * @param proSpecificas 商品规格
     * @param proNum        购买数量
     * @param memberId      购买人id
     * @param activityType  商品类型，1.团购商品 3.秒杀商品 4.拍卖商品 5 粉币商品 6预售商品 7批发商品
     * @param activityId    活动id
     */
    Map< String,Object > calculateInventory( int proId, Object proSpecificas, int proNum, int activityType, int activityId, int memberId );

}
