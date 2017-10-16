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
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneProductDetailDTO;
import com.gt.mall.result.phone.PhoneProductDetailResult;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductDetailService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductNewService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seckill.MallSeckillService;
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
    private MallProductDAO              mallProductDAO;//商品dao
    @Autowired
    private MallProductInventoryService mallProductInventoryService;//商品库存业务处理类
    @Autowired
    private MallProductDetailService    mallProductDetailService;//商品详情业务处理
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;//图片业务处理类
    @Autowired
    private MallStoreService            mallStoreService;//店铺业务处理类
    @Autowired
    private MallCollectService          mallCollectService;//收藏业务处理类
    @Autowired
    private MallProductService          mallProductService;//商品业务处理类
    @Autowired
    private MallFreightService          mallFreightService;//运费业务处理类
    @Autowired
    private MallGroupBuyService         mallGroupBuyService;//团购业务处理类
    @Autowired
    private MallSeckillService          mallSeckillService;//秒杀业务处理类
    @Autowired
    private MallPresaleService          mallPresaleService;//预售业务处理类

    @Override
    public PhoneProductDetailResult selectProductDetail( PhoneProductDetailDTO params, Member member, MallPaySet mallPaySet ) {
	PhoneProductDetailResult result = new PhoneProductDetailResult();
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
	Map< String,Object > storeMap = mallStoreService.findShopByStoreId( product.getShopId() );

	//查询商品是否已收藏
	boolean isCollect = mallCollectService.getProductCollect( params.getProductId(), member.getId() );

	//获取粉丝的折扣
	double discount = mallProductService.getMemberDiscount( product.getIsMemberDiscount().toString(), member );

	double productPrice = CommonUtil.toDouble( product.getProPrice() );//商品价格
	double productCostPrice = CommonUtil.toDouble( product.getProCostPrice() );//商品原价

	if ( product.getIsSpecifica().toString().equals( "1" ) ) {//商品有规格
	    //查询规格价
	    MallProductInventory inven = mallProductInventoryService.selectByIsDefault( params.getProductId() );
	    if ( inven != null ) {
		result.setInvId( inven.getId() );
		productPrice = CommonUtil.toDouble( inven.getInvPrice() );
	    } else {
		product.setIsSpecifica( 0 );
	    }
	}
	int isShowAddShop = 1;//是否显示“加入购物车按钮” 1显示
	if ( params.getType() == 1 ) {//查询团购商品
	    result = mallGroupBuyService.getGroupProductDetail( product.getId(), product.getShopId(), result, member );
	} else if ( params.getType() == 3 ) {//查询秒杀商品
	    result = mallSeckillService.getSeckillProductDetail( product.getId(), product.getShopId(), result );
	} else if ( params.getType() == 4 ) {//查询拍卖商品

	} else if ( params.getType() == 5 ) {//查询粉币商品
	    if ( product.getIsFenbiChangePro().toString().equals( "1" ) ) {
		productPrice = CommonUtil.toDouble( product.getChangeFenbi() );

		isShowAddShop = 0;
		result.setUnit( "粉币" );
	    }
	} else if ( params.getType() == 6 ) {//查询预售商品
	    isShowAddShop = 0;
	    result.setProductPrice( productPrice );
	    result = mallPresaleService.getPresaleProductDetail( product.getId(), product.getShopId(), result, member, mallPaySet );
	} else if ( params.getType() == 7 ) {//查询批发商品
	    
	}
	if ( params.getType() > 0 && params.getType() != 5 && result.getActivityId() == 0 ) {
	    result.setType( 0 );
	}
	if ( result.getInvId() > 0 && params.getType() > 0 ) {//商品有规格
	    //查询规格价
	    MallProductInventory inven = mallProductInventoryService.selectDefaultInvenNotNullStock( product.getId(), result.getInvId(), result );
	    if ( inven != null ) {
		result.setInvId( inven.getId() );
		productPrice = CommonUtil.toDouble( inven.getInvPrice() );
	    } else {
		product.setIsSpecifica( 0 );
	    }
	}

	if ( discount > 0 && discount < 1 ) {
	    double hyPrice = CommonUtil.toDouble( df.format( productPrice * discount ) );
	    result.setHyPrice( hyPrice );//会员价
	}
	result.setProductName( product.getProName() );//商品名称
	result.setProductLabel( product.getProLabel() );//商品标签
	if ( params.getType() == 0 ) {
	    result.setProductPrice( productPrice );//商品价格
	    if ( productPrice < productCostPrice && productCostPrice > 0 ) {
		result.setProductCostPrice( productCostPrice );//商品原价
	    }
	} else {
	    result.setProductCostPrice( productPrice );//商品原价
	}

	if ( params.getType() != 3 ) {
	    result.setProductStockTotal( product.getProStockTotal() );//商品库存
	}
	result.setProductSaleTotal( product.getProSaleTotal() );//商品销量
	result.setIsSpecifica( product.getIsSpecifica() );//是否有规格  1 有规格 0 无规格
	if ( CommonUtil.isNotEmpty( product.getProRestrictionNum() ) && product.getProRestrictionNum() > 0 && result.getMaxBuyNum() == 0 ) {
	    result.setMaxBuyNum( product.getProRestrictionNum() );//限购
	}

	if ( CommonUtil.isNotEmpty( detail ) ) {
	    result.setProductDetail( detail.getProductDetail() );//商品详情
	    result.setProductMessage( detail.getProductMessage() );//商品商品信息栏
	}

	result.setImageList( imageList );//商品图片集合

	String provinces = "";//省份id
	double longitude = params.getLongitude();
	double langitude = params.getLatitude();
	if ( CommonUtil.isNotEmpty( storeMap ) ) {
	    result.setShopName( storeMap.get( "stoName" ).toString() );//店铺名称
	    result.setShopImageUrl( storeMap.get( "stoPicture" ).toString() );//店铺图片
	    result.setShopAddress( storeMap.get( "adder" ).toString() );//店铺地址
	    longitude = CommonUtil.toDouble( storeMap.get( "stoLongitude" ) );
	    langitude = CommonUtil.toDouble( storeMap.get( "stoLatitude" ) );
	    if ( CommonUtil.isNotEmpty( storeMap.get( "memProvince" ) ) ) {
		provinces = storeMap.get( "memProvince" ).toString();
	    }
	}
	if ( isCollect ) {
	    result.setIsCollect( 1 );//是否已收藏
	}
	double raill = 0;
	if ( langitude > 0 && longitude > 0 && params.getLatitude() > 0 && params.getLongitude() > 0 ) {
	    raill = CommonUtil.getDistance( params.getLongitude(), params.getLatitude(), longitude, langitude );
	    raill = raill / 1000;
	}

	//计算运费
	JSONArray productArr = new JSONArray();
	JSONObject obj = new JSONObject();
	obj.put( "shop_id", product.getShopId() );
	obj.put( "price_total", productPrice );
	obj.put( "proNum", 1 );
	productArr.add( obj );
	Map< String,Object > freightMap = mallFreightService.getFreightByParams( params.getIp(), provinces, params.getToShop(), productArr, raill );
	if ( CommonUtil.isNotEmpty( freightMap ) ) {
	    if ( CommonUtil.isNotEmpty( freightMap.get( product.getShopId().toString() ) ) ) {
		result.setFreightMoney( CommonUtil.toDouble( freightMap.get( product.getShopId().toString() ) ) );
	    }
	}
	result.setIsShowAddShopButton( isShowAddShop );//是否显示加入购物车的按钮 1显示

	return result;
    }
}
