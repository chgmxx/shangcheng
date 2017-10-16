package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.param.phone.PhoneProductDetailDTO;
import com.gt.mall.result.phone.PhoneProductDetailResult;

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
    PhoneProductDetailResult selectProductDetail( PhoneProductDetailDTO params, Member member ,MallPaySet mallPaySet);

}
