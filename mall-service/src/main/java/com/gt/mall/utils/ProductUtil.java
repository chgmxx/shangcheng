package com.gt.mall.utils;

import com.gt.mall.entity.product.MallProduct;
import org.apache.log4j.Logger;

public class ProductUtil {

    private static final Logger log = Logger.getLogger( ProductUtil.class );

    /**
     * 判断商品信息
     */
    public static String isProductError( MallProduct product ) {
	if ( CommonUtil.isEmpty( product ) ) {
	    return "商品不存在";
	}
	if ( CommonUtil.isNotEmpty( product.getIsDelete() ) && product.getIsDelete() == 1 ) {
	    return "商品被删除";
	}
	if ( CommonUtil.isNotEmpty( product.getCheckStatus() ) && product.getCheckStatus() != 1 ) {
	    return "商品未审核或审核中";
	}
	if ( CommonUtil.isNotEmpty( product.getIsPublish() ) && product.getIsPublish() != 1 ) {
	    return "商品还未发布或未商家";
	}
	return null;

    }

}
