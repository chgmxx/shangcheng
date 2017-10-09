package com.gt.mall.controller.api.product;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallSearchLabel;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.CardService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.fenbiFlow.BusFlow;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 商品分组前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-20
 */
@Api( value = "mallProduct", description = "商品管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallProduct" )
public class MallProductNewController extends BaseController {

    @Autowired
    private MallGroupService            mallGroupService;
    @Autowired
    private MallStoreService            mallStoreService;
    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;
    @Autowired
    private CardService                 cardService;
    @Autowired
    private MallFreightService          mallFreightService;

    @ApiOperation( value = "商品列表(分页)", notes = "商品列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    //isPublish  checkStatus proName  proType(页面标识)  type(传1 售罄商品)
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( user != null ) {
		int userPId = SessionUtils.getAdminUserId( user.getId(), request );//通过用户名查询主账号id
		List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		long isJxc = mallStoreService.getIsErpCount( userPId, request );//判断商家是否有进销存 0没有 1有(从session获取)
		params.put( "isJxc", isJxc );
		if ( shoplist != null && shoplist.size() > 0 ) {
		    if ( CommonUtil.isNotEmpty( params.get( "proType" ) ) ) {
			int proType = CommonUtil.toInteger( params.get( "proType" ) );
			if ( proType == 1 ) {//上架
			    params.put( "is_publish", 1 );
			    params.put( "check_status", 1 );
			} else if ( proType == 2 ) {//未上架
			    params.put( "is_publish", 0 );
			} else if ( proType == 3 ) {//审核不通过
			    params.put( "check_status", -1 );
			}
		    }
		    if ( CommonUtil.isEmpty( params.get( "shopId" ) ) ) {
			params.put( "shoplist", shoplist );
		    }
		    PageUtil page = mallProductService.selectByUserId( params, shoplist );
		    result.put( "page", page );
		}
		   /* result.put( "proMaxNum", dictService.getDiBserNum( userPId, 5, "1094" ) );//可新增商品总数*/
		result.put( "proType", params.get( "proType" ) );
		result.put( "proName", params.get( "proName" ) );

		if ( isJxc == 1 ) {
		    mallProductService.syncErpPro( user.getId(), request );//把未同步的商品进行同步
		}
	    }
	    result.put( "videourl", busUserService.getVoiceUrl( "77" ) );
	} catch ( Exception e ) {
	    logger.error( "商品列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取链接
     */
    @ApiOperation( value = "获取链接", notes = "获取链接" )
    @ResponseBody
    @RequestMapping( value = "/link", method = RequestMethod.POST )
    public ServerResponse link( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "商品ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    MallProduct product = mallProductService.selectById( id );
	    String url = PropertiesUtil.getHomeUrl() + "mallPage/" + product.getId() + "/" + product.getShopId() + "/79B4DE7C/phoneProduct.do?toshop=0";
	    result.put( "link", url );//链接

	} catch ( Exception e ) {
	    logger.error( "获取链接：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 批量处理商品(删除，送审，上架，下架)
     */
    @ApiOperation( value = "批量处理商品（删除，送审，上架，下架）", notes = "批量处理商品（删除，送审，上架，下架）" )
    @ResponseBody
    @SysLogAnnotation( description = "商品管理-商品列表：批量处理商品（删除，送审，上架，下架）", op_function = "3" )
    @RequestMapping( value = "/batchProduct", method = RequestMethod.POST )
    public ServerResponse batchProduct( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "ids", value = "商品Id集合，用逗号隔开", required = true ) @RequestParam( "ids" ) String ids, @RequestParam Map< String,Object > params ) {

	logger.info( "进入批量处理controller" );
	response.setCharacterEncoding( "utf-8" );

	try {
	   /* String[] id = (String[]) net.sf.json.JSONArray.toArray( net.sf.json.JSONArray.fromObject( ids ), String.class );*/
	    String[] id = ids.split( "," );
	    boolean flag = mallProductService.batchUpdateProduct( params, id );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "操作失败！" );
	    }
	} catch ( Exception e ) {
	    logger.error( "批量处理商品信息：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 生成二维码的图片
     */
/*    @ApiOperation( value = "生成二维码的图片", notes = "生成二维码的图片" )
    @ResponseBody
    @RequestMapping( value = "/generateQRCode", method = RequestMethod.GET )*/
  /*  public void generateQRCode( @RequestBody Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    String id = params.get( "id" ).toString();
	    String shopId = params.get( "shopId" ).toString();
	    BusUser user = SessionUtils.getLoginUser( request );
	    String content = PropertiesUtil.getHomeUrl() + "/mallPage/" + id + "/" + shopId + "/79B4DE7C/phoneProduct.do?toshop=1";
	    QRcodeKit.buildQRcode( content, 200, 200, response );
	} catch ( Exception e ) {
	    logger.error( "获取到店支付二维码的图片失败：" + e.getMessage() );
	    e.printStackTrace();
	}
    }*/

    /**
     * 到店购买
     */
    @ApiOperation( value = "到店购买", notes = "到店购买" )
    @ResponseBody
    @RequestMapping( value = "/codeIframs", method = RequestMethod.POST )
    public ServerResponse codeIframs( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > resulst = new HashMap< String,Object >();
	try {
	    String id = params.get( "id" ).toString();
	    String shopId = params.get( "shopId" ).toString();
	    BusUser user = SessionUtils.getLoginUser( request );
	    //下载二维码地址
	    String html = PropertiesUtil.getHomeUrl() + "/mallPage/" + id + "/" + shopId + "/79B4DE7C/phoneProduct.do?uId=" + user.getId() + "&toshop=1";
	    resulst.put( "html", html );
	} catch ( Exception e ) {
	    logger.error( "获取到店购买信息失败：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resulst );
    }

    @ApiOperation( value = "获取各状态下商品总数", notes = "获取各状态下商品总数" )
    @ResponseBody
    @RequestMapping( value = "/countStatus", method = RequestMethod.POST )
    public ServerResponse countStatus( HttpServletRequest request, HttpServletResponse response ) {
	BusUser user = SessionUtils.getLoginUser( request );
	Map< String,Object > result = new HashMap<>();
	try {
	    //全部商品
	    Wrapper groupWrapper = new EntityWrapper();
	    groupWrapper.where( "user_id = {0} and is_delete = 0 ", user.getId() );
	    int status_total = mallProductService.selectCount( groupWrapper );
	    //已上架商品  （审核成功，上架）
	    groupWrapper = new EntityWrapper();
	    groupWrapper.where( "user_id = {0} and is_delete = 0 and is_publish =1 and check_status =1 ", user.getId() );
	    int status1 = mallProductService.selectCount( groupWrapper );
	    //未上架商品
	    groupWrapper = new EntityWrapper();
	    groupWrapper.where( "user_id = {0} and is_delete = 0 and is_publish = 0", user.getId() );
	    int status2 = mallProductService.selectCount( groupWrapper );
	    //审核不通过
	    groupWrapper = new EntityWrapper();
	    groupWrapper.where( "user_id = {0} and is_delete = 0 and check_status = -1 ", user.getId() );
	    int status3 = mallProductService.selectCount( groupWrapper );

	    result.put( "status_total", status_total );
	    result.put( "status1", status1 );
	    result.put( "status2", status2 );
	    result.put( "status3", status3 );
	} catch ( Exception e ) {
	    logger.error( "商城店铺管理异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商城的统计概况异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取编辑商品页面数据
     */
    @ApiOperation( value = "进入编辑商品", notes = "进入编辑商品" )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "id", value = "商品Id", paramType = "query", required = true, dataType = "int" ) )
    @RequestMapping( value = "/to_edit", method = RequestMethod.POST )
    public ServerResponse to_edit( HttpServletRequest request, HttpServletResponse response, Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );

	    int userPId = SessionUtils.getAdminUserId( user.getId(), request );//通过用户名查询主账号id
	    int isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
	    if ( isJxc == 1 ) {
		result.put( "noShowSt", 1 );//不显示实体物品
		result.put( "noUpSpec", 1 );//不修改规格 1 修改 0 不修改
	    }
	    result.put( "isJxc", isJxc );//是否有进销存 0没有 1有

	    // 查询商品信息
	    if ( CommonUtil.isNotEmpty( id ) ) {
		Map< String,Object > map = mallProductService.selectProductById( id, user, isJxc );
		if ( map != null && map.size() > 0 ) {
		    for ( Map.Entry< String,Object > e : map.entrySet() ) {
			result.put( e.getKey(), e.getValue() );
		    }
		}
	    }

	} catch ( Exception e ) {
	    logger.error( "进入编辑商品功能异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "进入编辑商品异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取商品信息
     */
    @ApiOperation( value = "获取商品信息", notes = "获取商品信息" )
    @ResponseBody
    @RequestMapping( value = "/productInfo", method = RequestMethod.POST )
    public ServerResponse productInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "商品Id", required = true ) @RequestParam Integer id ) {
	MallProduct product = null;
	try {
	    product = mallProductService.selectById( id );
	    if ( product != null && product.getIsDelete() > 0 ) {
		product = null;
	    }
	} catch ( Exception e ) {
	    logger.error( "获取商品分组信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商品信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), product );
    }

    /**
     * 新增商品
     */
    @ApiOperation( value = "新增商品信息", notes = "新增商品信息" )
    @ResponseBody
    @SysLogAnnotation( description = "新增商品", op_function = "2" )
    @RequestMapping( value = "/add", method = RequestMethod.POST )
    public ServerResponse add( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int code = 1;
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( user != null ) {
		Map< String,Object > resultMap = mallProductService.addProduct( params, user, request );
		if ( CommonUtil.isNotEmpty( resultMap.get( "code" ) ) ) {
		    code = CommonUtil.toInteger( resultMap.get( "code" ) );
		}
		if ( code != 1 ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存商品失败" );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc() );
	    }
	} catch ( Exception e ) {
	    logger.error( "新增商品信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 修改商品
     */
    @ApiOperation( value = "修改商品信息", notes = "修改商品信息" )
    @ResponseBody
    @SysLogAnnotation( description = "修改商品", op_function = "2" )
    @RequestMapping( value = "/updete", method = RequestMethod.POST )
    public ServerResponse updete( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {

	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( user != null ) {
		boolean flag = mallProductService.newUpdateProduct( params, user, request );
		if ( !flag ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改商品失败" );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc() );
	    }
	} catch ( Exception e ) {
	    logger.error( "修改商品信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
}
