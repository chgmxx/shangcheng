package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallProductTemplate;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品页模板表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-09-20
 */
public interface MallProductTemplateDAO extends BaseMapper< MallProductTemplate > {

    /**
     * 分页查找商品页模板
     *
     * @param param
     *
     * @return
     */
    List< MallProductTemplate > selectTemplateByPage( Map< String,Object > param );

}