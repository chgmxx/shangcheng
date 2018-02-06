package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallProductParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品参数表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductParamService extends BaseService< MallProductParam > {

    /**
     * 批量保存商品参数
     */
    void saveOrUpdateBatch( Object obj, int proId, Map< String,Object > defaultMap, boolean isUpdate );

    /**
     * 批量保存商品参数
     */
    void newSaveOrUpdateBatch( Object obj, int proId, boolean isUpdate );

    /**
     * 根据商品id来获取商品规格
     */
    List< MallProductParam > getParamByProductId( Integer proId );

    /**
     * 根据商品id来获取商品规格
     */
    List< MallProductParam > getParamByProductIds( List< Integer > proId );

    /**
     * 同步商品参数
     *
     * @return
     */
    void copyProductParam( List< MallProductParam > paramList, int proId, int shopId, int userId, int oldProId) throws Exception;

}
