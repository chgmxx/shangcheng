package com.gt.mall.service.web.freight.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.freight.MallFreightDAO;
import com.gt.mall.dao.freight.MallFreightDetailDAO;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.freight.MallFreightDetail;
import com.gt.mall.param.phone.freight.PhoneFreightDTO;
import com.gt.mall.param.phone.freight.PhoneFreightProductDTO;
import com.gt.mall.param.phone.freight.PhoneFreightShopDTO;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.freight.MallFreightDetailService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.AddressUtil;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallFreightServiceImpl extends BaseServiceImpl< MallFreightDAO,MallFreight > implements MallFreightService {

    private Logger log = Logger.getLogger( MallFreightServiceImpl.class );

    @Autowired
    private MallFreightDAO           freightDAO;
    @Autowired
    private MallFreightDetailDAO     freightDetailDAO;
    @Autowired
    private MallFreightDetailService freightDetailService;
    @Autowired
    private MallStoreService         storeService;
    @Autowired
    private WxShopService            wxShopService;
    @Autowired
    private MallPageService          mallPageService;

    @Override
    public PageUtil selectFreightByShopId( List< Map< String,Object > > shopList, Map< String,Object > param ) {
	List< MallFreight > freightList = null;
	int pageSize = 10;
	int count = 0;

	int curPage = CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) );
	param.put( "curPage", curPage );
	param.put( "shopList", shopList );// 每页显示商品的数量selectFreightByShopId

	count = freightDAO.selectCountByShopId( param );
	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallFreight/E9lM9uM4ct/list.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	param.put( "firstNum", firstNum );// 起始页
	param.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断商品是否有数据
	    freightList = freightDAO.selectByShopId( param );
	}
	page.setSubList( freightList );
	return page;
    }

    @Override
    public MallFreight selectFreightById( Integer id ) {
	return freightDAO.selectFreightById( id );
    }

    @Override
    public boolean editFreight( Map< String,Object > params, int userId ) {
	if ( !CommonUtil.isEmpty( params.get( "freight" ) ) ) {
	    MallFreight freight = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "freight" ).toString() ), MallFreight.class );
	    if ( CommonUtil.isEmpty( freight.getId() ) ) {// 新增物流
		freight.setCreateTime( new Date() );
		freight.setUserId( userId );
		freightDAO.insert( freight );

	    } else {// 修改物流
		freight.setEditTime( new Date() );
		freightDAO.updateById( freight );
	    }
	    if ( freight.getId() != null ) {
		freightDetailService.editFreightDetail( params, freight.getId() );
	    }
	    return true;
	}
	return false;
    }

    @Override
    public boolean newEditFreight( Map< String,Object > params, int userId ) {
	if ( !CommonUtil.isEmpty( params.get( "freight" ) ) ) {
	    MallFreight freight = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "freight" ).toString() ), MallFreight.class );
	    if ( CommonUtil.isEmpty( freight.getId() ) ) {// 新增物流
		freight.setCreateTime( new Date() );
		freight.setUserId( userId );
		freightDAO.insert( freight );

	    } else {// 修改物流
		freight.setEditTime( new Date() );
		freightDAO.updateById( freight );
	    }
	    if ( freight.getId() != null ) {
		freightDetailService.newEditFreightDetail( params, freight.getId() );
	    }
	    return true;
	}
	return false;
    }

    @Override
    public boolean deleteFreight( Map< String,Object > ids ) {
	int num = freightDAO.deleteFreightById( ids );
	if ( num > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public List< MallFreight > getByShopId( List< Map< String,Object > > shopList ) {
	Map< String,Object > param = new HashMap< String,Object >();
	param.put( "shopList", shopList );
	return freightDAO.selectByShopId( param );
    }

    @Override
    public List< MallFreight > selectFreightByShopId( Integer shopId ) {
	return freightDAO.selectFreightByShopId( shopId );
    }

    @Override
    public List< MallFreight > selectFreightByFreightIds( List< Integer > freightIds ) {
	Wrapper< MallFreight > wrapper = new EntityWrapper<>();
	wrapper.in( "id", freightIds ).andNew( "is_delete = 0" );
	return freightDAO.selectList( wrapper );
    }

    //    @Override
    //    public Map< String,Object > getFreightByParams( String ip, String provinceId, int toshop, JSONArray productArr, double juli, double weight ) {
    //	if ( CommonUtil.isEmpty( provinceId ) ) {
    //	    provinceId = mallPageService.getProvince( ip );
    //	}
    //
    //	Map< String,Object > map = new HashMap<>();
    //	map.put( "province_id", provinceId );
    //	map.put( "orderArr", productArr );
    //	map.put( "toshop", toshop );
    //	if ( juli > 0 ) {
    //	    map.put( "juli", juli );
    //	}
    //	if ( weight > 0 ) {
    //	    map.put( "pro_weight", weight );
    //	}
    //	return getFreightMoney( map );
    //    }

    @Override
    public Map< String,Object > getFreightMoneyByShopList( List< MallFreight > freightList, PhoneFreightDTO paramsDto, List< PhoneFreightShopDTO > shopFreightList ) {

	Map< String,Object > priceMap = new HashMap<>();
	if ( paramsDto != null ) {
	    Integer provinceId = 0;//省份id
	    if ( CommonUtil.isNotEmpty( paramsDto.getProvinceId() ) ) {
		provinceId = CommonUtil.toInteger( paramsDto.getProvinceId() );
	    }
	    double juli = 0;
	    if ( CommonUtil.isNotEmpty( paramsDto.getJuli() ) ) {
		juli = paramsDto.getJuli();
	    }
	    int toshop = 0;
	    if ( CommonUtil.isNotEmpty( paramsDto.getToshop() ) ) {
		toshop = CommonUtil.toInteger( paramsDto.getToshop() );
	    }
	    int proTypeId = 0;

	    if ( CommonUtil.isNotEmpty( shopFreightList ) && shopFreightList.size() > 0 ) {
		for ( PhoneFreightShopDTO shopDto : shopFreightList ) {
		    if ( CommonUtil.isNotEmpty( shopDto.getProTypeId() ) ) {
			proTypeId = shopDto.getProTypeId();
		    }
		    Integer shopId = shopDto.getShopId();//店铺id
		    double orderPrice = shopDto.getTotalProductPrice();//订单价格
		    Integer proNum = shopDto.getTotalProductNum();//购买商品的数量

		    double freightPrice = 0;
		    double weight = 0;
		    if ( CommonUtil.isNotEmpty( shopDto.getTotalProductWeight() ) ) {
			weight = CommonUtil.toDouble( shopDto.getTotalProductWeight() );
		    }
		    //根据店铺来查询物流
		    MallFreight freight = new MallFreight();
		    if ( freightList != null && freightList.size() > 0 ) {
			for ( MallFreight mallFreight : freightList ) {
			    if ( mallFreight.getShopId().toString().equals( CommonUtil.toString( shopId ) ) ) {
				freight = mallFreight;
				break;
			    }
			}
		    }
		    if ( CommonUtil.isEmpty( freight ) ) {
			freight.setShopId( shopId );
			freight.setIsDelete( 0 );
			freight = freightDAO.selectOne( freight );
		    }
		    if ( freight != null && toshop == 0 ) {
			freightPrice = getFreightPrice( freight, orderPrice, proNum, weight, juli, provinceId );
		    }
		    if ( toshop == 1 || proTypeId > 0 ) {
			freightPrice = 0;
		    }
		    if ( freightPrice > 0 ) {
			DecimalFormat df = new DecimalFormat( "######0.00" );
			priceMap.put( shopId.toString(), df.format( freightPrice ) );
		    } else {
			priceMap.put( shopId.toString(), freightPrice );
		    }
		}
	    }
	}

	return priceMap;
    }

    @Override
    public double getFreightMoneyByShopList( List< MallFreight > freightList, PhoneFreightDTO paramsDto, PhoneFreightShopDTO shopDto ) {
	double freightPrice = 0;
	if ( paramsDto != null ) {
	    Integer provinceId = 0;//省份id
	    if ( CommonUtil.isNotEmpty( paramsDto.getProvinceId() ) ) {
		provinceId = CommonUtil.toInteger( paramsDto.getProvinceId() );
	    }
	    double juli = 0;
	    if ( CommonUtil.isNotEmpty( paramsDto.getJuli() ) ) {
		juli = paramsDto.getJuli();
	    }
	    int toshop = 0;
	    if ( CommonUtil.isNotEmpty( paramsDto.getToshop() ) ) {
		toshop = CommonUtil.toInteger( paramsDto.getToshop() );
	    }
	    int proTypeId = 0;

	    if ( CommonUtil.isNotEmpty( shopDto ) ) {
		if ( CommonUtil.isNotEmpty( shopDto.getProTypeId() ) ) {
		    proTypeId = shopDto.getProTypeId();
		}
		Integer shopId = shopDto.getShopId();//店铺id
		double orderPrice = shopDto.getTotalProductPrice();//订单价格
		Integer proNum = shopDto.getTotalProductNum();//购买商品的数量

		double weight = 0;
		if ( CommonUtil.isNotEmpty( shopDto.getTotalProductWeight() ) ) {
		    weight = CommonUtil.toDouble( shopDto.getTotalProductWeight() );
		}
		//根据店铺来查询物流
		MallFreight freight = null;
		if ( freightList != null && freightList.size() > 0 ) {
		    for ( MallFreight mallFreight : freightList ) {
			if ( mallFreight.getShopId().toString().equals( CommonUtil.toString( shopId ) ) ) {
			    freight = mallFreight;
			    break;
			}
		    }
		}
		if ( CommonUtil.isEmpty( freight ) ) {
		    freight = new MallFreight();
		    freight.setShopId( shopId );
		    freight.setIsDelete( 0 );
		    freight = freightDAO.selectOne( freight );
		}
		if ( freight != null && toshop == 0 ) {
		    freightPrice = getFreightPrice( freight, orderPrice, proNum, weight, juli, provinceId );
		}
		if ( toshop == 1 || proTypeId > 0 ) {
		    freightPrice = 0;
		}
		if ( freightPrice > 0 ) {
		    DecimalFormat df = new DecimalFormat( "######0.00" );
		    freightPrice = CommonUtil.toDouble( df.format( freightPrice ) );
		}
	    }
	}
	return freightPrice;
    }

    @Override
    public double getFreightMoneyByProductList( List< PhoneFreightProductDTO > freightDTOList, Double juli, Integer provinceId ) {
	double freightPrice = 0;
	if ( freightDTOList != null && freightDTOList.size() > 0 ) {
	    for ( PhoneFreightProductDTO freightProductDTO : freightDTOList ) {
		if ( CommonUtil.isEmpty( freightProductDTO ) ) {
		    continue;
		}
		MallFreight mallFreight = freightProductDTO.getMallFreight();
		if ( CommonUtil.isNotEmpty( mallFreight ) && mallFreight.getIsDelete() == 0 ) {
		    freightPrice += getFreightPrice( mallFreight, freightProductDTO.getTotalProductPrice(), freightProductDTO.getTotalProductNum(),
				    freightProductDTO.getTotalProductWeight(), juli, provinceId );

		}
	    }
	}
	if ( freightPrice > 0 ) {
	    DecimalFormat df = new DecimalFormat( "######0.00" );
	    freightPrice = CommonUtil.toDouble( df.format( freightPrice ) );
	}
	return freightPrice;
    }

    private double getFreightPrice( MallFreight freight, double orderPrice, Integer proNum, double weight, double juli, int provinceId ) {
	double freightPrice = 0;//物流数量
	double noMoney = 0;//免邮价格
	int priceType = freight.getPriceType();

	if ( freight.getNoMoney() != null && !CommonUtil.isNotEmpty( freight.getNoMoney() ) ) {
	    noMoney = CommonUtil.toDouble( freight.getNoMoney() );
	}
	if ( freight.getMoney() != null && CommonUtil.isNotEmpty( freight.getMoney() ) ) {
	    freightPrice = CommonUtil.toDouble( freight.getMoney() );
	}
	if ( freight.getIsNoMoney().toString().equals( "1" ) ) {
	    if ( priceType > 0 ) {
		double firstNum = CommonUtil.toDouble( freight.getFirstNums() );//首件重量
		double addNum = CommonUtil.toDouble( freight.getAddNums() );//续件重量
		double addPrice = CommonUtil.toDouble( freight.getAddMoney() );//续件价格
		freightPrice = getFreightMoney( freightPrice, priceType, proNum, weight, firstNum, addNum, addPrice, juli );
	    }

	    Integer noNum = freight.getNoMoneyNum();//免邮数量
	    if ( proNum >= noNum && noNum > 0 ) {//已达到免邮数量
		freightPrice = 0;
	    }
	    if ( orderPrice >= noMoney && noMoney > 0 ) {//已达到免邮价格
		freightPrice = 0;
	    }
	    if ( freight.getIsResultMoney() == 1 && provinceId > 0 ) {//根据指定条件设置邮费
		Map< String,Object > params = new HashMap< String,Object >();
		params.put( "provinceId", provinceId );
		params.put( "freightId", freight.getId() );
		MallFreightDetail detail = freightDetailDAO.selectFreightByPId( params );
		if ( detail != null ) {
		    freightPrice = CommonUtil.toDouble( detail.getMoney() );//指定城市的运费
		    if ( priceType > 0 ) {
			double firstNum = CommonUtil.toDouble( detail.getFirstNums() );//首件重量
			double addNum = CommonUtil.toDouble( detail.getAddNums() );//续件重量
			double addPrice = CommonUtil.toDouble( detail.getAddMoney() );//续件价格
			freightPrice = getFreightMoney( freightPrice, priceType, proNum, weight, firstNum, addNum, addPrice, juli );
		    }

		    Integer dNoMoneyNum = 0;//指定城市的免邮数量
		    double dNoMoney = 0;//指定城市的免邮价格
		    if ( !CommonUtil.isEmpty( detail.getNoMoneyNum() ) ) {
			dNoMoneyNum = detail.getNoMoneyNum();
		    }
		    if ( !CommonUtil.isEmpty( detail.getNoMoney() ) ) {
			dNoMoney = CommonUtil.toDouble( detail.getNoMoney() );
		    }
		    if ( proNum >= dNoMoneyNum && dNoMoneyNum > 0 ) {//指定城市已达到免邮数量
			freightPrice = 0;
		    }
		    if ( orderPrice >= dNoMoney && dNoMoney > 0 ) {//指定城市已达到免邮价格
			freightPrice = 0;
		    }
		}
	    }
	} else {
	    freightPrice = 0;
	}
	return freightPrice;
    }

    @Override
    public Map< String,Object > getFreightMoney( Map< String,Object > map ) {
	log.debug( "获取运费参数：" + map.toString() );
	Map< String,Object > priceMap = new HashMap<>();
	if ( map != null ) {
	    Integer provinceId = 0;//省份id
	    if ( CommonUtil.isNotEmpty( map.get( "province_id" ) ) ) {
		provinceId = CommonUtil.toInteger( map.get( "province_id" ) );
	    } else if ( CommonUtil.isNotEmpty( map.get( "province" ) ) ) {
		String province = map.get( "province" ).toString();
		provinceId = getProvinceId( province );
	    }
	    double juli = 0;
	    if ( CommonUtil.isNotEmpty( map.get( "juli" ) ) ) {
		juli = CommonUtil.toDouble( map.get( "juli" ) );
	    }
	    int toshop = 0;
	    if ( CommonUtil.isNotEmpty( map.get( "toshop" ) ) ) {
		toshop = CommonUtil.toInteger( map.get( "toshop" ) );
	    }
	    int proTypeId = 0;
	    if ( CommonUtil.isNotEmpty( map.get( "proTypeId" ) ) ) {
		proTypeId = CommonUtil.toInteger( map.get( "proTypeId" ) );
	    }
	    JSONArray orderJSON = JSONArray.parseArray( JSONArray.toJSONString( map.get( "orderArr" ) ) );

	    if ( !CommonUtil.isEmpty( orderJSON ) && orderJSON.size() > 0 ) {
		for ( Object object : orderJSON ) {
		    JSONObject orderObj = JSONObject.parseObject( object.toString() );
		    if ( CommonUtil.isNotEmpty( orderObj.get( "proTypeId" ) ) ) {
			proTypeId = CommonUtil.toInteger( orderObj.get( "proTypeId" ) );
		    }
		    Integer shopId = CommonUtil.toInteger( orderObj.get( "shop_id" ) );//店铺id

		    double orderPrice = 0;//订单价格
		    if ( !CommonUtil.isEmpty( orderObj.get( "yuanjia_total" ) ) ) {//立即购买传值
			orderPrice = CommonUtil.toDouble( orderObj.get( "yuanjia_total" ) );
		    } else if ( !CommonUtil.isEmpty( orderObj.get( "primary_price" ) ) ) {//购物车跳到订单页面传值
			int totalnum = Integer.parseInt( orderObj.get( "totalnum" ).toString() );
			double totalPrice = CommonUtil.toDouble( orderObj.get( "primary_price" ) );
			orderPrice = totalPrice * totalnum;
		    } else if ( !CommonUtil.isEmpty( orderObj.get( "price_total" ) ) ) {//商品详细页面传值
			orderPrice = CommonUtil.toDouble( orderObj.get( "price_total" ) );
		    } else {
			orderPrice = CommonUtil.toDouble( orderObj.get( "totalPrice" ) );
		    }

		    Integer proNum = 0;//购买商品的数量
		    if ( !CommonUtil.isEmpty( orderObj.get( "proNum" ) ) ) {
			proNum = CommonUtil.toInteger( orderObj.get( "proNum" ) );
		    } else {
			proNum = CommonUtil.toInteger( orderObj.get( "totalnum" ) );
		    }
		    double freightPrice = 0;
		    double weight = 0;
		    if ( CommonUtil.isNotEmpty( orderObj.get( "pro_weight" ) ) ) {
			weight = CommonUtil.toDouble( orderObj.get( "pro_weight" ) );
		    }
		    //根据店铺来查询物流
		    MallFreight freight = new MallFreight();
		    freight.setShopId( shopId );
		    freight.setIsDelete( 0 );
		    freight = freightDAO.selectOne( freight );
		    if ( freight != null && toshop == 0 ) {
			freightPrice = getFreightPrice( freight, orderPrice, proNum, weight, juli, provinceId );
		    }
		    if ( toshop == 1 || proTypeId > 0 ) {
			freightPrice = 0;
		    }
		    if ( freightPrice > 0 ) {
			DecimalFormat df = new DecimalFormat( "######0.00" );
			priceMap.put( shopId.toString(), df.format( freightPrice ) );
		    } else {
			priceMap.put( shopId.toString(), freightPrice );
		    }
		}
	    }
	}

	return priceMap;
    }

    /**
     * 计算商品的运费
     *
     * @param firstPrice 首件商品的价格
     * @param priceType  计价方式 1按照件数来算，2按照重量来算
     * @param proNum     购买商品的数量
     * @param weight     购买商品的重量
     * @param firstNum   首件商品的重量
     * @param addNum     续件商品的数量
     * @param addPrice   续件商品的价格
     *
     * @return
     */
    private double getFreightMoney( double firstPrice, int priceType, int proNum, double weight, double firstNum, double addNum, double addPrice, double juli ) {
	//		float addPrice = Float.valueOf(freight.getAddMoney().toString());//续件价格
	if ( priceType == 1 ) {//按照件数来算
	    //			int firstNum = CommonUtil.toInteger(freight.getFirstNums());//首件数量
	    //			int addNum = CommonUtil.toInteger(freight.getAddNums());//续件数量
	    if ( proNum > firstNum ) {//购买数量大于首件商品的数量
		if ( ( proNum - firstNum ) % addNum == 0 ) {//购买的数量-首件 是否能被续件数量整除
		    double addMoney = ( ( proNum - firstNum ) / addNum ) * addPrice;//计算续件商品的运费
		    firstPrice = addMoney + firstPrice;//查询首件和续件的运费总和
		} else {
		    firstPrice = ( ( proNum - firstNum - ( proNum - firstNum ) % addNum ) / addNum + 1 ) * addPrice + firstPrice;
		}
	    }
	} else if ( priceType == 2 ) {//按照重量来计算
	    weight = proNum * weight;//计算商品的总重量
	    //			float firstNum = Float.valueOf(freight.getFirstNums().toString());//首件重量
	    //			float addNum = Float.valueOf(freight.getAddNums().toString());//续件重量
	    if ( weight > firstNum ) {//购买数量大于首件商品的重量
		if ( ( weight - firstNum ) % addNum == 0 ) {//购买的商品重量-首件 是否能被续件重量整除
		    double addMoney = ( ( weight - firstNum ) / addNum ) * addPrice;//计算续件商品的运费
		    firstPrice = addMoney + firstPrice;//查询首件和续件的运费总和
		} else {
		    firstPrice = ( ( weight - firstNum - ( weight - firstNum ) % addNum ) / addNum + 1 ) * addPrice + firstPrice;
		}
	    }
	} else if ( priceType == 3 ) {//按照距离来计算
	    if ( juli > firstNum ) {//距离大于首件距离
		if ( ( juli - firstNum ) % addNum == 0 ) {//购买的商品距离-首件 是否能被续件距离整除
		    double addMoney = ( ( juli - firstNum ) / addNum ) * addPrice;//计算续件商品的运费
		    firstPrice = addMoney + firstPrice;//查询首件和续件的运费总和
		} else {
		    firstPrice = ( ( juli - firstNum - ( juli - firstNum ) % addNum ) / addNum + 1 ) * addPrice + firstPrice;
		}
	    }
	}
	return firstPrice;
    }

    @Override
    public int getProvinceId( String province ) {
	if ( CommonUtil.isNotEmpty( province ) && !province.equals( "0" ) ) {
	    Map map = wxShopService.queryBasisByName( province );
	    if ( CommonUtil.isNotEmpty( map ) ) {
		if ( CommonUtil.isNotEmpty( map.get( "id" ) ) ) {
		    return CommonUtil.toInteger( map.get( "id" ) );
		}
	    }
	}
	return 0;
    }

    @Override
    public double getFreightByProvinces( Map< String,Object > params, Map< String,Object > addressMap, int shopId, double totalPrice, double pro_weight ) {
	String loginCity = "";
	double juli = 0;
	if ( addressMap != null && addressMap.size() > 0 ) {
	    loginCity = addressMap.get( "memProvince" ).toString();
	    if ( CommonUtil.isNotEmpty( params.get( "latitude" ) ) && CommonUtil.isNotEmpty( params.get( "longitude" ) )
			    && CommonUtil.isNotEmpty( addressMap.get( "memLatitude" ) ) && CommonUtil.isNotEmpty( addressMap.get( "memLongitude" ) ) ) {
		double latitude = CommonUtil.toDouble( params.get( "latitude" ) );
		double longitude = CommonUtil.toDouble( params.get( "longitude" ) );
		double memLatitude = CommonUtil.toDouble( addressMap.get( "memLatitude" ) );
		double memLongitude = CommonUtil.toDouble( addressMap.get( "memLongitude" ) );
		if ( latitude > 0 && longitude > 0 && memLatitude > 0 && memLongitude > 0 ) {
		    double raill = CommonUtil.getDistance( memLongitude, memLatitude, longitude, latitude );
		    juli = raill / 1000;
		}
	    }
	} else if ( CommonUtil.isNotEmpty( params.get( "province" ) ) ) {
	    int city = getProvinceId( params.get( "province" ).toString() );
	    if ( city > 0 ) {
		loginCity = CommonUtil.toString( city );
	    }
	} else if ( CommonUtil.isNotEmpty( params.get( "latitude" ) ) && CommonUtil.isNotEmpty( params.get( "longitude" ) ) ) {
	    double latitude = CommonUtil.toDouble( params.get( "latitude" ) );
	    double longitude = CommonUtil.toDouble( params.get( "longitude" ) );
	    if ( latitude > 0 && longitude > 0 ) {
		try {
		    Map< String,Object > map = AddressUtil.getAddressBylnglat( latitude + "", longitude + "" );
		    if ( CommonUtil.isNotEmpty( map ) ) {
			if ( CommonUtil.isNotEmpty( map.get( "city" ) ) ) {
			    String city = map.get( "city" ).toString();
			    int cityId = getProvinceId( city );
			    if ( cityId > 0 ) {
				loginCity = cityId + "";
			    }
			}
		    }
		} catch ( Exception e ) {
		    e.printStackTrace();
		}
	    }
	}
	if ( loginCity.equals( "" ) ) {
	    loginCity = "2136";
	}
	double freightPrice = 0;
	//计算运费
	if ( loginCity != null && CommonUtil.isNotEmpty( loginCity ) ) {
	    Map< String,Object > map = new HashMap< String,Object >();
	    map.put( "province_id", loginCity );
	    JSONArray arr = new JSONArray();
	    JSONObject obj = new JSONObject();
	    obj.put( "shop_id", shopId );
	    obj.put( "price_total", totalPrice );
	    if ( CommonUtil.isEmpty( params.get( "product_num" ) ) ) {
		obj.put( "proNum", 1 );
	    } else {
		obj.put( "proNum", params.get( "product_num" ) );
	    }
	    if ( pro_weight > 0 ) {
		obj.put( "pro_weight", pro_weight );
	    }
	    arr.add( obj );
	    map.put( "orderArr", arr );
	    if ( juli > 0 ) {
		map.put( "juli", juli );
	    }
	    Map< String,Object > priceMap = getFreightMoney( map );
	    Object freightObj = priceMap.get( CommonUtil.toString( shopId ) );
	    if ( CommonUtil.isNotEmpty( freightObj ) ) {
		freightPrice = CommonUtil.toDouble( freightObj );
	    }
	}
	return freightPrice;
    }
}
