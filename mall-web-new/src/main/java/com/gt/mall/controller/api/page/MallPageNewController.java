package com.gt.mall.controller.api.page;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面管理前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-19
 */
@Api( value = "mallPage", description = "页面管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallPage/new/" )
public class MallPageNewController extends BaseController {

    @Autowired
    private MallPageService  mallPageService;
    @Autowired
    private MallProductDAO   mallProductDAO;
    @Autowired
    private DictService      dictService;
    @Autowired
    private BusUserService   busUserService;

    @ApiOperation( value = "商家的页面列表(分页)", notes = "商家的页面列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    params.put( "curPage", curPage );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    PageUtil page = mallPageService.findByPage( params, user, request );

	    result.put( "page", page );
	    result.put( "shopId", shopId );
	    result.put( "videourl", busUserService.getVoiceUrl( "78" ) );

	} catch ( Exception e ) {
	    logger.error( "获取商家的页面列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商家的页面列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取页面信息
     */
    @ApiOperation( value = "获取页面信息", notes = "获取页面信息" )
    @ResponseBody
    @RequestMapping( value = "/pageInfo", method = RequestMethod.POST )
    public ServerResponse pageInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "页面ID", required = true ) @RequestParam Integer id ) {
	MallPage page = null;
	try {
	    page = mallPageService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取页面信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), page );
    }

    /**
     * 获取页面类型
     */
    @ApiOperation( value = "获取页面类型", notes = "获取页面类型" )
    @ResponseBody
    @RequestMapping( value = "/typeMap", method = RequestMethod.POST )
    public ServerResponse typeMap( HttpServletRequest request, HttpServletResponse response ) {
	List< Map > typeMap = null;
	try {
	    //获取页面类型
	    typeMap = dictService.getDict( "1073" );
	} catch ( Exception e ) {
	    logger.error( "获取页面类型异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取页面类型异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), typeMap );
    }

    /**
     * 保存或修改页面信息
     */
    @ApiOperation( value = "保存页面信息", notes = "保存页面信息" )
    @ResponseBody
    @SysLogAnnotation( description = "页面管理-保存页面信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    MallPage page = com.alibaba.fastjson.JSONObject.parseObject( params.get( "page" ).toString(), MallPage.class );
	    page.setPagUserId( MallSessionUtils.getLoginUser( request ).getId() );
	    page.setPagCreateTime( new Date() );
	    mallPageService.saveOrUpdate( page, user );
	} catch ( BusinessException e ) {
	    logger.error( "保存页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除页面信息
     */
    @ApiOperation( value = "删除页面", notes = "删除页面" )
    @ResponseBody
    @SysLogAnnotation( description = "页面管理-删除页面信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "ids", value = "页面ID集合,用逗号隔开", required = true ) @RequestParam String ids ) throws IOException {
	Map< String,Object > result = new HashMap<>();
	try {
	    String id[] = ids.toString().split( "," );
	    mallPageService.delete( id );
	} catch ( BusinessException e ) {
	    logger.error( "删除页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除页面信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除页面信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取店铺链接
     */
    @ApiOperation( value = "获取链接", notes = "获取链接" )
    @ResponseBody
    @RequestMapping( value = "/link", method = RequestMethod.POST )
    public ServerResponse link( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "页面ID", required = true ) @RequestParam Integer id )
		    throws IOException {
	Map< String,Object > result = new HashMap<>();
	try {
	    String url = PropertiesUtil.getHomeUrl() + "mallPage/" + id + "/79B4DE7C/viewHomepage.do";
	    result.put( "link", url );//店铺链接
	   /* result.put( "smsLink", url );//短信链接*/
	} catch ( Exception e ) {
	    logger.error( "获取链接：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 进入页面设计
     */
    //    @ApiOperation( value = "进入页面设计", notes = "进入页面设计" )
    //    @ResponseBody
    //    @RequestMapping( value = "/designPage", method = RequestMethod.POST )
    public ServerResponse designPage( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "页面ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    //获取轮播选择类型
	    Map< String,Object > lbMap = new HashMap<>();
	    lbMap.put( "1", "商品" );
	    lbMap.put( "2", "分类" );
	    result.put( "lbMap", JSONObject.fromObject( lbMap ).toString() );
	    String dataJson = "[]";
	    String picJson = "[]";
	    BusUser obj = MallSessionUtils.getLoginUser( request );//获取登录人信息
	    Map< String,Object > map = mallPageService.select( id, obj.getId() );
	    if ( map.get( "pag_css" ) != null ) {
		dataJson = map.get( "pag_css" ).toString();

	    }
	    //根据商品id获取商品最新的数据
	    if ( map.get( "pag_data" ) != null ) {
		JSONArray jsonobj = JSONArray.fromObject( map.get( "pag_data" ).toString() );//转换成JSON数据
		JSONArray XinJson = new JSONArray();//获取新的数组对象
		for ( int i = 0; i < jsonobj.size(); i++ ) {
		    JSONArray XinidJSon = new JSONArray();
		    if ( CommonUtil.isEmpty( jsonobj.get( i ) ) ) {
			XinJson.add( "null" );
			continue;
		    } else {
			if ( jsonobj.get( i ).equals( "null" ) ) {
			    XinJson.add( "null" );
			    continue;
			}
		    }
		    Map< String,Object > map1 = (Map< String,Object >) jsonobj.get( i );
		    if ( CommonUtil.isEmpty( map1.get( "imgID" ) ) ) {
			continue;
		    }
		    String imaid = map1.get( "imgID" ).toString();
		    String type = map1.get( "type" ).toString();//如果type==1 代表来自与页面商品信息，2代表轮播图里面的信息
		    JSONArray jsonobjX = JSONArray.fromObject( imaid );//转换成JSON数据
		    for ( int j = 0; j < jsonobjX.size(); j++ ) {
			if ( CommonUtil.isNotEmpty( jsonobjX.get( j ) ) ) {
			    if ( jsonobjX.get( j ).toString().equals( "null" ) ) {
				continue;
			    }
			    Map< String,Object > map2 = (Map< String,Object >) jsonobjX.get( j );
			    Object selecttype = map2.get( "selecttype" );
			    Boolean res = true;
			    if ( selecttype != null ) {
				if ( selecttype == "2" || selecttype.equals( "2" ) ) {
				    Object mapid = map2.get( "id" );
				    if ( mapid != null && !mapid.equals( null ) && !mapid.equals( "" ) ) {
					Integer mapid1 = Integer.valueOf( mapid.toString() );
					if ( !mapid1.toString().equals( "-1" ) && !mapid1.toString().equals( "-2" ) ) {
					    try {
						Map map3 = mallPageService.selectBranch( mapid1 );
						map2.put( "title", map3.get( "pag_name" ) );
					    } catch ( Exception e ) {
						e.printStackTrace();
						map2.remove( "url" );//发现分类页被删除时，移除url
					    }
					}

				    }
				} else if ( selecttype == "6" || selecttype.equals( "6" ) ) {//预售商品
				    map2 = mallPageService.getProductPresale( map2, null );
				}
			    }
			    //res == true 时，代表商品正常，res == false 代表商品已删除
			    if ( res ) {
				XinidJSon.add( map2 );
			    }
			}
		    }
		    map1.put( "imgID", XinidJSon );
		    XinJson.add( map1 );
		}
		picJson = XinJson.toString();
	    }
	    result.put( "dataJson", dataJson );
	    result.put( "picJson", picJson );
	    result.put( "stoId", id );
	    result.put( "shopid", map.get( "pag_sto_id" ).toString() );

	    String headImg = "";
	    Map wxPubMap = mallPageService.wxpublicid( id );//根据页面id查询商家公众号
	    if ( CommonUtil.isNotEmpty( wxPubMap ) ) {
		if ( CommonUtil.isNotEmpty( wxPubMap.get( "head_img" ) ) ) {
		    headImg = wxPubMap.get( "head_img" ).toString();
		}
	    }

	    if ( CommonUtil.isNotEmpty( map.get( "pag_sto_id" ) ) ) {
		Map storeMap = mallPageService.shopmessage( CommonUtil.toInteger( map.get( "pag_sto_id" ) ), null );//获取店铺信息

		String name = "";//店铺名称
		if ( CommonUtil.isEmpty( storeMap.get( "business_name" ) ) ) {
		    name = storeMap.get( "sto_name" ).toString();
		} else {
		    name = storeMap.get( "business_name" ).toString();
		}
		//店铺图片
		if ( CommonUtil.isEmpty( storeMap.get( "stoPicture" ) ) ) {
		    storeMap.put( "stoPicture", storeMap.get( "sto_qr_code" ).toString() );
		}

		if ( CommonUtil.isEmpty( headImg ) ) {
		    if ( CommonUtil.isNotEmpty( storeMap ) ) {
			if ( CommonUtil.isNotEmpty( storeMap.get( "sto_head_img" ) ) ) {
			    headImg = PropertiesUtil.getResourceUrl() + storeMap.get( "sto_head_img" ).toString();
			}
		    }
		}

		result.put( "stoName", name );
		result.put( "stoPicture", PropertiesUtil.getResourceUrl() + storeMap.get( "stoPicture" ) );
	    }
	    Map< String,Object > params = new HashMap<>();
	    params.put( "id", id );
	    params.put( "shopId", map.get( "pag_sto_id" ) );
	    int countproduct = mallProductDAO.selectCountAllByShopids( params );
	    result.put( "countproduct", countproduct );

	    result.put( "headImg", headImg );
	    result.put( "userid", obj.getId() );
	    result.put( "codeUrl", map.get( "codeUrl" ) );//预览二维码
	} catch ( Exception e ) {
	    logger.error( "进入页面设计异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "进入页面设计异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 页面保存
     */
   /* @ApiOperation( value = "页面设计-页面保存", notes = "页面设计-页面保存" )
    @ResponseBody
    @SysLogAnnotation( description = "页面设计-页面保存", op_function = "2" )
    @RequestMapping( value = "/savepage", method = RequestMethod.POST )*/
    public ServerResponse savepage( HttpServletRequest request, HttpServletResponse response, @RequestParam MallPage obj ) {
	try {
	    mallPageService.updateById( obj );
	} catch ( BusinessException e ) {
	    logger.error( "页面设计-页面保存异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "页面设计-页面保存异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 弹出选择商品
     */
    @ApiOperation( value = "页面设计-选择商品", notes = "页面设计-选择商品" )
    @ResponseBody
    @RequestMapping( value = "/choosePro", method = RequestMethod.POST )
    public ServerResponse choosePro( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    //获取店铺下的分类信息
	    Integer stoId = CommonUtil.toInteger( params.get( "stoId" ) );//stoid代表商铺id
	    List groLs = mallPageService.storeList( stoId, 0, 0 );
	    //	    String check = request.getParameter( "check" ).toString();
	    //获取店铺下的商品
	    PageUtil page = mallPageService.product( params );
	    String url = PropertiesUtil.getHomeUrl();

	    result.put( "page", page );
	    result.put( "groLs", groLs );
	    result.put( "proName", params.get( "proName" ) );
	    result.put( "groupId", params.get( "groupId" ) );
	    //	    result.put( "check", check );
	    result.put( "stoId", stoId );
	    result.put( "url", url );
	} catch ( Exception e ) {
	    logger.error( "页面设计-选择商品异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "页面设计-选择商品异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 弹出该店铺分类页
     */
    @ApiOperation( value = "页面设计-店铺分类", notes = "页面设计-店铺分类" )
    @ResponseBody
    @RequestMapping( value = "/branchPage", method = RequestMethod.POST )
    public ServerResponse branchPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Integer stoId = Integer.valueOf( params.get( "stoId" ).toString() );
	    PageUtil page = mallPageService.selectListBranch( stoId, params );
	    result.put( "page", page );
	    String ym = PropertiesUtil.getHomeUrl();//域名
	    result.put( "ym", ym );
	    int pageindex = 0;
	    if ( CommonUtil.isNotEmpty( page ) ) {
		pageindex = page.getCurPage();
	    }
	    if ( pageindex == 1 ) {
		List< Map< String,Object > > typeList = mallPageService.typePage( stoId, user );
		result.put( "typeList", typeList );
	    }
	    result.put( "shopId", stoId );
	} catch ( Exception e ) {
	    logger.error( "商城店铺分类弹出框异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商城店铺分类弹出框异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 弹出该店铺预售信息
     */
    @ApiOperation( value = "页面设计-店铺预售信息", notes = "页面设计-店铺预售信息" )
    @ResponseBody
    @RequestMapping( value = "/choosePresalePro", method = RequestMethod.POST )
    public ServerResponse choosePresalePro( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Integer stoId = Integer.valueOf( params.get( "stoId" ).toString() );
	    String url = PropertiesUtil.getHomeUrl();//域名
	    result.put( "url", url );
	    List< Map< String,Object > > presaleList = mallPageService.productPresale( stoId, params );
	    result.put( "presaleList", presaleList );
	    result.put( "proName", params.get( "proName" ) );
	    result.put( "groupId", params.get( "groupId" ) );
/*	    result.put( "check", params.get( "check" ) );*/
	    result.put( "stoId", params.get( "stoId" ) );

	} catch ( Exception e ) {
	    logger.error( "选择预售商品出错：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "选择预售商品异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );

    }

}
