package com.gt.mall.utils;

import com.gt.mall.constant.Constants;
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

    public static int getGuanzhuNum( MallProduct product ) {
	String key = Constants.REDIS_KEY + "proViewNum";
	int viewNum = 0;
	String viewNums = "";
	if ( JedisUtil.hExists( key, product.getId().toString() ) ) {
	    viewNums = JedisUtil.maoget( key, product.getId().toString() );
	}
	if ( viewNums == null || viewNums.equals( "" ) ) {
	    if ( CommonUtil.isNotEmpty( product.getViewsNum() ) ) {
		viewNums = product.getViewsNum().toString();
	    }
	}
	if ( viewNums != null && !viewNums.equals( "" ) ) {
	    viewNum = CommonUtil.toInteger( viewNums );
	}
	if ( viewNum + 1 < 1000000000 ) {
	    JedisUtil.map( key, product.getId().toString(), ( viewNum + 1 ) + "" );
	}
	return viewNum;
    }

}
