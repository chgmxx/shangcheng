package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallProductTemplate;
import com.gt.mall.utils.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品页模板表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-09-20
 */
public interface MallProductTemplateService extends BaseService< MallProductTemplate > {

    /**
     * 分页查询商品页模板
     *
     * @param param
     *
     * @return
     */
    PageUtil findTemplateByPage( Map< String,Object > param );

    /**
     * 批量删除商品页模板
     *
     * @param id
     * @param userId
     *
     * @return
     */
    boolean batchDelTemplate( String[] id, Integer userId );

}
