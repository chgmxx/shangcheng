package com.gt.mall.controller.api.product.phone;

import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.controller.api.common.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneGroupDTO;
import com.gt.mall.param.phone.PhoneProductDetailDTO;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.applet.MallHomeAppletService;
import com.gt.mall.service.web.auction.MallAuctionService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.service.web.product.MallProductNewService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品相关（手机端）
 * User : yangqian
 * Date : 2017/10/9 0009
 * Time : 14:35
 */
@Api( value = "phoneProduct", description = "商品页面相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneProduct/" )
public class PhoneProductNewController extends AuthorizeOrUcLoginController {
    private static Logger logger = LoggerFactory.getLogger( PhoneProductNewController.class );

    @Autowired
    private MallPageService       mallPageService;
    @Autowired
    private MallHomeAppletService mallHomeAppletService;
    @Autowired
    private MallStoreService      mallStoreService;
    @Autowired
    private MallProductService    mallProductService;
    @Autowired
    private MallGroupBuyService   mallGroupBuyService;
    @Autowired
    private MallSeckillService    mallSeckillService;
    @Autowired
    private MallAuctionService    mallAuctionService;
    @Autowired
    private MallPresaleService    mallPresaleService;
    @Autowired
    private MallPifaService       mallPifaService;
    @Autowired
    private MallPaySetService     mallPaySetService;
    @Autowired
    private MallProductNewService mallProductNewService;
    @Autowired
    private BusUserService        busUserService;

    @ApiOperation( value = "商品分类接口", notes = "商品分类接口" )
    @ResponseBody
    @RequestMapping( value = "79B4DE7C/classAll", method = RequestMethod.POST )
    public ServerResponse classAll( HttpServletRequest request, HttpServletResponse response, @Valid @ModelAttribute PhoneGroupDTO params ) {
	try {
	    Map< String,Object > map = new HashMap<>();
	    map.put( "shopId", params.getShopId() );
	    if ( params.getGroupId() == 0 ) {
		map.put( "isFrist", 1 );
	    } else {
		map.put( "classId", params.getGroupId() );
	    }
	    map.put( "busId", params.getBusId() );
	    List< Map< String,Object > > classList = mallHomeAppletService.selectGroupsByShopId( map );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), classList );

	} catch ( BusinessException e ) {
	    logger.error( "查询商品分类接口异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查询商品分类接口失败" );
	} catch ( Exception e ) {
	    logger.error( "查询商品分类接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "查询商品分类接口失败" );
	}

    }

    @ApiOperation( value = "商品搜索接口", notes = "搜索商品" )
    @ResponseBody
    @RequestMapping( value = "79B4DE7C/productAll", method = RequestMethod.POST )
    public ServerResponse productAll( HttpServletRequest request, HttpServletResponse response, @Valid @ModelAttribute PhoneSearchProductDTO params ) {
	Map< String,Object > result = new HashMap<>();
	Member member = SessionUtils.getLoginMember( request );
	try {
	    if ( params.getCurPage() == 1 ) {
		//判断店铺和门店是否已经被删除
		boolean isShop = mallPageService.wxShopIsDelete( params.getShopId(), null );
		if ( !isShop ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.SHOP_ERROR.getCode(), ResponseEnums.SHOP_ERROR.getDesc() );
		}
		//封装登陆参数
		Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, params.getBusId(), request );
		loginMap.put( "uclogin", 1 );//不需要登陆
		userLogin( request, response, loginMap );//授权或登陆，以及商家是否已过期的判断

		mallStoreService.getShopBySession( request.getSession(), params.getShopId() );//从session获取店铺id  或  把店铺id存入session

		if ( params.getType() == 5 ) {
		    mallProductService.setJifenByRedis( member, request, params.getType(), params.getBusId() );
		} else {
		    int isJifen = mallProductService.getJifenByRedis( member, request, params.getType(), params.getBusId() );
		    if ( isJifen == 5 ) {
			params.setType( 5 );
		    }
		}

	    }
	    PageUtil page = null;
	    if ( params.getType() == 0 || params.getType() == 5 ) {//0 查询普通商品 5 查询粉币商品
		//		double discount = mallProductService.getMemberDiscount( "1", member );
		page = mallPageService.productAllListNew( params, 1, member );
	    } else if ( params.getType() == 1 ) {//查询团购商品
		page = mallGroupBuyService.searchGroupBuyProduct( params, member );
	    } else if ( params.getType() == 3 ) {//查询秒杀商品
		page = mallSeckillService.searchSeckillAll( params, member );
	    } else if ( params.getType() == 4 ) {//查询拍卖商品
		page = mallAuctionService.searchAuctionAll( params, member );
	    } else if ( params.getType() == 6 ) {//查询预售商品
		page = mallPresaleService.searchPresaleAll( params, member );
	    } else if ( params.getType() == 7 ) {//查询批发商品
		page = mallPifaService.searchPifaAll( params, member );
	    }
	    result.put( "productList", page );
	} catch ( BusinessException e ) {
	    logger.error( "商品搜索接口异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品搜索接口失败" );
	} catch ( Exception e ) {
	    logger.error( "商品搜索接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品搜索接口失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    @ApiOperation( value = "商品详情接口", notes = "查看商品详情" )
    @ResponseBody
    @RequestMapping( value = "79B4DE7C/productDetail", method = RequestMethod.POST )
    public ServerResponse productDetail( HttpServletRequest request, HttpServletResponse response, @Valid @ModelAttribute PhoneProductDetailDTO params ) {
	Map< String,Object > result = new HashMap<>();
	Member member = SessionUtils.getLoginMember( request );
	try {
	    //判断店铺和门店是否已经被删除
	    boolean isShop = mallPageService.wxShopIsDelete( params.getShopId(), null );
	    if ( !isShop ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.SHOP_ERROR.getCode(), ResponseEnums.SHOP_ERROR.getDesc() );
	    }
	    //封装登陆参数
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, params.getBusId(), request );
	    loginMap.put( "uclogin", 1 );//不需要登陆
	    userLogin( request, response, loginMap );//授权或登陆，以及商家是否已过期的判断

	    mallStoreService.getShopBySession( request.getSession(), params.getShopId() );//从session获取店铺id  或  把店铺id存入session

	    MallPaySet set = new MallPaySet();
	    set.setUserId( params.getBusId() );
	    MallPaySet mallPaySet = mallPaySetService.selectByUserId( set );
	    //查询商品详情数据
	    result = mallProductNewService.selectProductDetail( params, member, mallPaySet );

	    BusUser user = busUserService.selectById( params.getBusId() );//根据商家id获取商家信息
	    if ( CommonUtil.isNotEmpty( user ) ) {
		if ( CommonUtil.isNotEmpty( user.getAdvert() ) && user.getAdvert().toString().equals( "0" ) ) {
		    result.put( "isAdvert", 1 );//是否显示技术支持  1显示
		}
	    }

	} catch ( BusinessException e ) {
	    logger.error( "商品搜索接口异常：" + e.getMessage() );
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "商品搜索接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品搜索接口失败" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

}
