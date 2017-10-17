package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.param.phone.PhoneProductDetailDTO;
import com.gt.mall.param.phone.PhoneSpecificaDTO;
import com.gt.mall.result.phone.PhoneProductDetailResult;

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

}
