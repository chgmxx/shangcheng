package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneProductDetailDTO;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.product.*;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.product.MallProductDetailService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductNewService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 新商品 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductNewServiceImpl extends BaseServiceImpl< MallProductDAO,MallProduct > implements MallProductNewService {

    private static Logger logger = LoggerFactory.getLogger( MallProductNewServiceImpl.class );

    @Autowired
    private MallProductDAO                mallProductDAO;//商品dao
    @Autowired
    private MallProductInventoryService   mallProductInventoryService;//商品库存业务处理类
    @Autowired
    private MallProductSpecificaService   mallProductSpecificaService;//商品规格业务处理类
    @Autowired
    private MallProductDetailService      mallProductDetailService;//商品详情业务处理
    @Autowired
    private MallImageAssociativeService   mallImageAssociativeService;//图片业务处理类
    @Autowired
    private MallProductParamService       mallProductParamService;//商品参数业务处理类
    @Autowired
    private MallStoreService              mallStoreService;//店铺业务处理类
    @Autowired
    private MallStoreCertificationService mallStoreCertificationService;//店铺认证业务处理类
    @Autowired
    private MallPaySetService             mallPaySetService;
    private MallCollectService            mallCollectService;//收藏业务处理类
    @Autowired
    private MallProductService            mallProductService;//商品业务处理类
    @Autowired
    private MallFreightService            mallFreightService;
    @Autowired
    private BusUserService                busUserService;//商家业务处理类

    @Override
    public Map< String,Object > selectProductDetail( PhoneProductDetailDTO params, Member member, MallPaySet mallPaySet ) {
	Map< String,Object > resultMap = new HashMap<>();
	DecimalFormat df = new DecimalFormat( "0.00" );

	//查询商品信息
	MallProduct product = mallProductDAO.selectById( params.getProductId() );
	if ( CommonUtil.isEmpty( product ) ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "该商品不存在" );
	}
	//查询商品详情
	MallProductDetail detail = mallProductDetailService.selectByProductId( params.getProductId() );
	Map< String,Object > imageParams = new HashMap<>();
	imageParams.put( "assId", params.getProductId() );
	imageParams.put( "assType", 1 );
	//查询商品图片
	List< MallImageAssociative > imageList = mallImageAssociativeService.selectByAssId( imageParams );

	//查询店铺信息
	Map< String,Object > storeMap = mallStoreService.findShopByStoreId( params.getShopId() );

	//查询店铺的认证信息
	MallStoreCertification certification = mallStoreCertificationService.selectByStoreId( params.getShopId() );

	//查询商品是否已收藏
	boolean isCollect = mallCollectService.getProductCollect( params.getProductId(), params.getBusId() );

	//获取粉丝的折扣
	double discount = mallProductService.getMemberDiscount( product.getIsMemberDiscount().toString(), member );

	double productPrice = CommonUtil.toDouble( product.getProPrice() );//商品价格
	double productCostPrice = CommonUtil.toDouble( product.getProCostPrice() );//商品原价

	if ( product.getIsSpecifica().toString().equals( "1" ) ) {//商品有规格
	    //查询规格价
	    MallProductInventory inven = mallProductInventoryService.selectByIsDefault( params.getProductId() );
	    if ( inven != null ) {
		productPrice = CommonUtil.toDouble( inven.getInvPrice() );
	    } else {
		product.setIsSpecifica( 0 );
	    }
	}
	//查询店铺信息
	Map< String,Object > storeMap = mallStoreService.findShopByStoreId( params.getShopId() );

	//查询店铺的认证信息
	MallStoreCertification certification = mallStoreCertificationService.selectByStoreId( params.getShopId() );
	if ( discount > 0 && discount < 1 ) {
	    double hyPrice = CommonUtil.toDouble( df.format( productPrice * discount ) );
	    resultMap.put( "hyPrice", hyPrice );//会员价
	}

	resultMap.put( "productName", product.getProName() );//商品名称
	resultMap.put( "productLabel", product.getProLabel() );//商品标签
	resultMap.put( "productPrice", productPrice );//商品价格
	if ( productPrice < productCostPrice && productCostPrice > 0 ) {
	    resultMap.put( "productCostPrice", productCostPrice );//商品原价
	}
	resultMap.put( "productStockTotal", product.getProStockTotal() );//商品库存
	resultMap.put( "productSaleTotal", product.getProSaleTotal() );//商品销量
	resultMap.put( "isSpecifica", product.getIsSpecifica() );//是否有规格  1 有规格 0 无规格

	if ( CommonUtil.isNotEmpty( detail ) ) {
	    resultMap.put( "product_detail", detail.getProductDetail() );//商品详情
	    resultMap.put( "product_message", detail.getProductMessage() );//商品商品信息栏
	}
	resultMap.put( "imageList", imageList );//商品图片集合

	String provinces = "";//省份id
	if ( CommonUtil.isNotEmpty( storeMap ) ) {
	    resultMap.put( "shopName", storeMap.get( "stoName" ) );//店铺名称
	    resultMap.put( "shopImageUrl", storeMap.get( "stoPicture" ) );//店铺图片
	    resultMap.put( "shopAddress", storeMap.get( "adder" ) );//店铺地址
	    /*double stoLongitude = CommonUtil.toDouble( storeMap.get( "stoLongitude" ) );
	    double stoLatitude = CommonUtil.toDouble( storeMap.get( "stoLatitude" ) );
	    if ( stoLatitude > 0 && stoLongitude > 0 && params.getLatitude() > 0 && params.getLongitude() > 0 ) {
		Double raill = CommonUtil.getDistance( params.getLongitude(), params.getLatitude(), stoLongitude, stoLatitude );
		raill = raill / 1000;
		resultMap.put( "raill", df.format( raill ) );//距离 单位km
	    }*/
	    if ( CommonUtil.isNotEmpty( storeMap.get( "" ) ) ) {
		provinces = storeMap.get( "" ).toString();
	    }
	}
	if ( CommonUtil.isNotEmpty( certification ) ) {
	    resultMap.put( "isOfflineStores", 1 );//是否开通了线下门店

	}
	//查询是否开通了担保交易
	if ( CommonUtil.isNotEmpty( mallPaySet ) ) {

	}
	if ( isCollect ) {
	    resultMap.put( "isCollect", 1 );//是否已收藏
	}

	//计算运费
	JSONArray productArr = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put( "shop_id", params.getShopId() );
	obj.put( "price_total", productPrice );
	obj.put( "proNum", 1 );
	productArr.add( obj );
	Map< String,Object > freightMap = mallFreightService.getFreightByParams( params.getIp(), provinces, params.getToShop(), productArr );
	if ( CommonUtil.isNotEmpty( freightMap ) ) {
	    resultMap.put( "freightMoney", freightMap.get( params.getShopId().toString() ) );
	}


	return resultMap;
    }
}
