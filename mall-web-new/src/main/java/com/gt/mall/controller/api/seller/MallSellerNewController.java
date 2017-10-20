package com.gt.mall.controller.api.seller;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerJoinProduct;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.seller.MallSellerJoinProductService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.seller.MallSellerWithdrawService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-29
 */
@Api( value = "mallSellers", description = "超级销售员", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallSellers/new" )
public class MallSellerNewController extends BaseController {

    @Autowired
    private MallSellerService            mallSellerService;
    @Autowired
    private MallStoreService             mallStoreService;
    @Autowired
    private MallPaySetService            mallPaySetService;
    @Autowired
    private MallSellerWithdrawService    mallSellerWithdrawService;
    @Autowired
    private BusUserService               busUserService;
    @Autowired
    private MallSellerJoinProductService sellerJoinProductService;

    /********************基本规则设置*********************************/

    /**
     * 获取超级销售员设置
     */
    @ApiOperation( value = "获取超级销售员设置", notes = "获取超级销售员设置" )
    @ResponseBody
    @RequestMapping( value = "/getSellerSet", method = RequestMethod.POST )
    public ServerResponse getSellerSet( HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isOpenSeller = false;
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = mallPaySetService.selectByUserId( set );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsSeller() ) ) {
		    if ( set.getIsSeller().toString().equals( "1" ) ) {

			isOpenSeller = true;
		    }
		}
	    }
	    result.put( "isOpenSeller", isOpenSeller );
	    if ( isOpenSeller ) {
		MallSellerSet sellerSet = mallSellerService.selectByBusUserId( user.getId() );
		result.put( "sellerSet", sellerSet );
		result.put( "videourl", busUserService.getVoiceUrl( "85" ) );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取超级销售员设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取超级销售员设置异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 保存销售员设置
     */
    @ApiOperation( value = "保存销售员设置", notes = "保存销售员设置" )
    @ResponseBody
    @SysLogAnnotation( description = "保存销售员设置", op_function = "2" )
    @RequestMapping( value = "/saveSellerSet", method = RequestMethod.POST )
    public ServerResponse saveSellerSet( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    Map< String,Object > resultMap = mallSellerService.saveOrUpdSellerSet( userId, params );
	    boolean flag = (boolean) resultMap.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存销售员设置失败" );
	    }
	} catch ( Exception e ) {
	    logger.error( "保存销售员设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /********************商品佣金设置*********************************/

    @ApiOperation( value = "商品佣金设置列表(分页)", notes = "商品佣金设置列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "isUse", value = "活动状态", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/joinProduct", method = RequestMethod.POST )
    public ServerResponse joinProduct( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer shopId, Integer isUse ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "shopId", shopId );
	    params.put( "isUse", isUse );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		if ( CommonUtil.isEmpty( shopId ) ) {
		    params.put( "shoplist", shoplist );
		}
		PageUtil page = mallSellerService.selectProductByShopId( params, shoplist );
		result.put( "page", page );
	    }
	} catch ( Exception e ) {
	    logger.error( "商品佣金设置列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品佣金设置列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取商品佣金设置
     */
    @ApiOperation( value = "获取商品佣金设置", notes = "获取商品佣金设置" )
    @ResponseBody
    @RequestMapping( value = "/joinProductInfo", method = RequestMethod.POST )
    public ServerResponse joinProductInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "商品佣金ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > joinProductMap = null;
	try {
	    joinProductMap = mallSellerService.selectJoinProductById( id );
	} catch ( Exception e ) {
	    logger.error( "获取商品佣金设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商品佣金设置异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), joinProductMap );
    }

    /**
     * 保存商品佣金设置
     */
    @ApiOperation( value = "保存商品佣金设置", notes = "保存商品佣金设置" )
    @ResponseBody
    @SysLogAnnotation( description = "保存商品佣金设置", op_function = "2" )
    @RequestMapping( value = "/saveJoinProduct", method = RequestMethod.POST )
    public ServerResponse saveJoinProduct( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    Map< String,Object > resultMap = mallSellerService.saveOrUpdSellerJoinProduct( userId, params );
	    boolean flag = (boolean) resultMap.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存商品佣金设置失败" );
	    }
	} catch ( Exception e ) {
	    logger.error( "保存商品佣金设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 商品佣金的删除或失效
     */
    @ApiOperation( value = "商品佣金的删除或失效", notes = "商品佣金的删除或失效" )
    @ResponseBody
    @SysLogAnnotation( description = "商品佣金的删除或失效", op_function = "4" )
    @RequestMapping( value = "/setJoinProductStatus", method = RequestMethod.POST )
    public ServerResponse setJoinProductStatus( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "商品佣金Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1禁用 -2启用 -3删除", required = true ) @RequestParam Integer type ) {
	try {

	    MallSellerJoinProduct joinProduct = new MallSellerJoinProduct();
	    joinProduct.setId( id );
	    if ( type == -3 ) {// 删除
		joinProduct.setIsDelete( 1 );
	    } else if ( type == -2 ) {// 启用
		joinProduct.setIsUse( 1 );
	    } else if ( type == -1 ) {// 禁用
		joinProduct.setIsUse( 0 );
	    }
	    boolean flag = sellerJoinProductService.updateById( joinProduct );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品佣金的删除或失效异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "商品佣金的删除或失效异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "商品佣金的删除或失效异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品佣金的删除或失效异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /********************推荐审核*********************************/

    @ApiOperation( value = "销售员审核列表(分页)", notes = "销售员审核列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "keyWord", value = "销售员名字/手机", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/sellerCheckList", method = RequestMethod.POST )
    public ServerResponse sellerCheckList( HttpServletRequest request, HttpServletResponse response, Integer curPage, String keyWord ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "keyWord", keyWord );
	    if ( CommonUtil.isNotEmpty( keyWord ) ) {
		keyWord = CommonUtil.getBytes( keyWord );
		params.put( "keyWord", keyWord );
	    }
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = mallPaySetService.selectByUserId( set );
	    int check = 0;
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsSeller() ) ) {
		    check = CommonUtil.toInteger( set.getIsCheckSeller() );
		}
	    }
	    result.put( "isCheckSeller", check );
	    if ( check == 1 ) {
		PageUtil page = mallSellerService.selectCheckSeller( user.getId(), params );
		result.put( "page", page );
	    }

	} catch ( Exception e ) {
	    logger.error( "销售员审核列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "销售员审核列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 审核销售员信息
     */
    @ApiOperation( value = "审核销售员信息", notes = "审核销售员信息" )
    @ResponseBody
    @SysLogAnnotation( description = "审核销售员信息", op_function = "4" )
    @RequestMapping( value = "/checkSeller", method = RequestMethod.POST )
    public ServerResponse checkSeller( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "销售员ID", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "checkStatus", value = "审核状态 1通过 -1不通过 ", required = true ) @RequestParam Integer checkStatus ) {
	try {
	    //	    seller:{"id":22,"checkStatus":1}
	    Map< String,Object > params = new HashMap<>();
	    params.put( "id", id );
	    params.put( "checkStatus", checkStatus );

	    MallSeller seller = new MallSeller();
	    seller.setId( id );
	    seller.setCheckStatus( checkStatus );
	    seller.setCheckTime( new Date() );
	    if ( CommonUtil.isNotEmpty( seller.getCheckStatus() ) ) {
		if ( seller.getCheckStatus() == 1 ) {
		    seller.setAddTime( new Date() );
		}
	    }
	    boolean flag = mallSellerService.updateById( seller );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "审核销售员信息异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "审核销售员信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "审核销售员信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /********************销售员管理*********************************/

    @ApiOperation( value = "超级销售员列表(分页)", notes = "超级销售员列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "keyWord", value = "销售员名字/手机", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "saleMemId", value = "销售员ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/sellerList", method = RequestMethod.POST )
    public ServerResponse sellerList( HttpServletRequest request, HttpServletResponse response, Integer curPage, String keyWord, Integer saleMemId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "keyWord", keyWord );
	    params.put( "saleMemId", saleMemId );
	    if ( CommonUtil.isNotEmpty( keyWord ) ) {
		keyWord = CommonUtil.getBytes( keyWord );
		params.put( "keyWord", keyWord );
	    }

	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }
	    params.put( "isCheck", isCheck );
	    PageUtil page = mallSellerService.selectSellerPage( user.getId(), params );
	    result.put( "page", page );

	    if ( CommonUtil.isNotEmpty( saleMemId ) ) {
		MallSeller seller = mallSellerService.selectSellerByMemberId( saleMemId );
		result.put( "sellerName", seller.getUserName() );//推荐人姓名

	    }
	} catch ( Exception e ) {
	    logger.error( "超级销售员列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "超级销售员列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /********************提现列表*********************************/
    @ApiOperation( value = "提现列表(分页)", notes = "提现列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "keyWord", value = "销售员名字/手机", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "startTime", value = "提现开始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endTime", value = "提现结束时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "saleMemId", value = "销售员ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/withDrawList", method = RequestMethod.POST )
    public ServerResponse withDrawList( HttpServletRequest request, HttpServletResponse response, Integer curPage, String keyWord, String startTime, String endTime,
		    Integer saleMemId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "endTime", endTime );
	    params.put( "startTime", startTime );
	    params.put( "keyWord", keyWord );
	    params.put( "saleMemId", saleMemId );
	    if ( CommonUtil.isNotEmpty( keyWord ) ) {
		keyWord = CommonUtil.getBytes( keyWord );
		params.put( "keyWord", keyWord );
	    }
	    PageUtil page = mallSellerWithdrawService.withdrawPage( user.getId(), params );
	    result.put( "page", page );

	    if ( CommonUtil.isNotEmpty( saleMemId ) ) {
		MallSeller seller = mallSellerService.selectSellerByMemberId( saleMemId );
		result.put( "sellerName", seller.getUserName() );//推荐人姓名
	    }
	} catch ( Exception e ) {
	    logger.error( "提现列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "提现列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

}
