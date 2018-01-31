package com.gt.mall.controller.page;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallPage" )
public class MallPageOldController extends AuthorizeOrLoginController {

    @Autowired
    private MallPageService  mallPageService;
    @Autowired
    private MallStoreService mallStoreService;
    @Autowired
    private MallProductDAO   mallProductDAO;
    @Autowired
    private MallGroupService mallGroupService;

    @RequestMapping( "{id}/79B4DE7C/viewHomepage" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String viewHomepage( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) throws Exception {

	return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/index/" + id;
    }

    /**
     * 手机访问商家主页面接口
     */
    @RequestMapping( "{id}/79B4DE7C/pageIndex" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String pageIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) throws IOException {

	return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/index/" + id;
    }

    /**
     * 显示手机商品页
     */
    @RequestMapping( "{id}/{shopid}/79B4DE7C/phoneProduct" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String phoneProduct( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int shopid, @RequestParam Map< String,Object > param )
		    throws Exception {
	MallProduct product = mallProductDAO.selectById( id );
	return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/goods/details/" + shopid + "/" + product.getUserId() + "/0/" + id + "/0";
    }

    /**
     * 根据店铺id获取商家所有的商品
     */
    @RequestMapping( "{shopid}/79B4DE7C/shoppingall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String shoppingall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	int userid = 0;
	if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
	    userid = CommonUtil.toInteger( params.get( "uId" ) );
	}
	if ( userid <= 0 ) {
	    MallStore mallStore = mallStoreService.selectById( shopid );
	    userid = mallStore.getStoUserId();
	}
	if ( CommonUtil.isNotEmpty( params.get( "rType" ) ) ) {
	    String rType = params.get( "rType" ).toString();
	    if ( rType.equals( "1" ) ) {//跳转到积分商城页面

	    } else if ( rType.equals( "2" ) ) {//跳转到粉币商城
		return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/classify/" + shopid + "/" + userid + "/5/k=k";
	    }
	}
	return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/classify/" + shopid + "/" + userid + "/0/k=k";
    }

    /**
     * 进入页面设计
     */
    @ApiOperation( value = "进入页面设计", notes = "进入页面设计" )
    @RequestMapping( value = "/designPage", method = RequestMethod.GET )
    public String designPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    int id = CommonUtil.toInteger( params.get( "id" ) );
	    //获取轮播选择类型
	    Map< String,Object > lbMap = new HashMap<>();
	    lbMap.put( "1", "商品" );
	    lbMap.put( "2", "分类" );
	    request.setAttribute( "lbMap", JSONObject.fromObject( lbMap ).toString() );
	    String dataJson = "[]";
	    String picJson = "[]";
	    BusUser obj = MallSessionUtils.getLoginUser( request );//获取登录人信息
	    MallPage mallPage = mallPageService.select( id );
	    //	    Map< String,Object > map = mallPageService.select( id, obj.getId() );
	    if ( mallPage.getPagCss() != null ) {
		dataJson = mallPage.getPagCss();

	    }
	    //根据商品id获取商品最新的数据
	    if ( mallPage.getPagData() != null ) {
		JSONArray jsonobj = JSONArray.fromObject( mallPage.getPagData() );//转换成JSON数据
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
			if ( map1.get( "type" ).toString().equals( "7" ) ) {
			    XinJson.add( map1 );
			}
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
	    request.setAttribute( "dataJson", dataJson );
	    request.setAttribute( "picJson", picJson );
	    request.setAttribute( "stoId", id );
	    request.setAttribute( "shopid", mallPage.getPagStoId() );

	    String headImg = "";
	    Map wxPubMap = mallPageService.wxpublicid( id );//根据页面id查询商家公众号
	    if ( CommonUtil.isNotEmpty( wxPubMap ) ) {
		if ( CommonUtil.isNotEmpty( wxPubMap.get( "head_img" ) ) ) {
		    headImg = wxPubMap.get( "head_img" ).toString();
		}
	    }

	    if ( CommonUtil.isNotEmpty( mallPage.getPagStoId() ) ) {
		MallStore mallStore = mallStoreService.findShopByShopId( CommonUtil.toInteger( mallPage.getPagStoId() ) );

		request.setAttribute( "stoName", mallStore.getStoName() );
		if ( CommonUtil.isNotEmpty( mallStore.getStoPicture() ) ) {
		    request.setAttribute( "stoPicture", PropertiesUtil.getResourceUrl() + mallStore.getStoPicture() );
		}
	    }
	    Map< String,Object > proParams = new HashMap<>();
	    proParams.put( "id", id );
	    proParams.put( "shopId", mallPage.getPagStoId() );
	    int countproduct = mallProductDAO.selectCountAllByShopids( proParams );
	    request.setAttribute( "countproduct", countproduct );

	    request.setAttribute( "headImg", headImg );
	    request.setAttribute( "userid", obj.getId() );
	    request.setAttribute( "codeUrl", mallPage.getCodeUrl() );//预览二维码
	} catch ( Exception e ) {
	    logger.error( "进入页面设计异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/page/editor/index";
    }

    /**
     * 页面保存
     */
    @RequestMapping( value = "/E9lM9uM4ct/savepage", method = RequestMethod.POST )
    @ResponseBody
    public ServerResponse savepage( HttpServletRequest request, HttpServletResponse response, MallPage obj ) {
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
    @RequestMapping( value = "/E9lM9uM4ct/choosePro", method = RequestMethod.POST )
    public ServerResponse choosePro( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    //获取店铺下的分类信息
	    Integer stoId = CommonUtil.toInteger( params.get( "stoId" ) );//stoid代表商铺id
//	    List groLs = mallPageService.storeList( stoId, 0, 0 );
	    //	    String check = request.getParameter( "check" ).toString();
	    //获取店铺下的商品
	    PageUtil page = mallPageService.product( params );
	    /*String url = PropertiesUtil.getHomeUrl();*/

	    resultMap.put( "page", page );
//	    resultMap.put( "groLs", groLs );
//	    resultMap.put( "proName", params.get( "proName" ) );
//	    resultMap.put( "groupId", params.get( "groupId" ) );
//	    resultMap.put( "check", params.get( "check" ) );
//	    resultMap.put( "stoId", stoId );
//	    resultMap.put( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "页面设计-选择商品异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	//	return "/mall/page/choosePro";
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), resultMap );
    }

    /**
     * 弹出该店铺分类页
     */
    @ApiOperation( value = "页面设计-店铺分类", notes = "页面设计-店铺分类" )
    @ResponseBody
    @RequestMapping( value = "/E9lM9uM4ct/branchPage", method = RequestMethod.POST )
    public ServerResponse branchPage( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Integer stoId = Integer.valueOf( params.get( "stoId" ).toString() );
	    PageUtil page = mallPageService.selectListBranch( stoId, params ,user);
	    resultMap.put( "page", page );
	    resultMap.put( "shopId", stoId );
	} catch ( Exception e ) {
	    logger.error( "商城店铺分类弹出框异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	//	return "/mall/page/branchPage";
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), resultMap );
    }

    /**
     * 弹出该店铺预售信息
     */
    @ApiOperation( value = "页面设计-店铺预售信息", notes = "页面设计-店铺预售信息" )
    @ResponseBody
    @RequestMapping( value = "/E9lM9uM4ct/choosePresalePro", method = RequestMethod.POST )
    public ServerResponse choosePresalePro( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    Integer stoId = Integer.valueOf( params.get( "stoId" ).toString() );
	    PageUtil page = mallPageService.productPresale( stoId, params );
	    resultMap.put( "page", page );
	    /*resultMap.put( "proName", params.get( "proName" ) );*/
	    /*resultMap.put( "groupId", params.get( "groupId" ) );*/
	    /*resultMap.put( "stoId", params.get( "stoId" ) );
	    resultMap.put( "check", params.get( "check" ) );
	    resultMap.put( "http", PropertiesUtil.getResourceUrl() );*/

	} catch ( Exception e ) {
	    logger.error( "选择预售商品出错：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	//	return "mall/page/branchPresale";
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), resultMap );

    }

    /**
     * 选择分类
     */
    @ApiOperation( value = "页面设计-选择分类", notes = "页面设计-选择分类" )
    @ResponseBody
    @RequestMapping( value = "/E9lM9uM4ct/chooseGroup", method = RequestMethod.POST )
    public ServerResponse chooseGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap<>();
	try {
	    Integer stoId = Integer.valueOf( params.get( "stoId" ).toString() );

	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shopList = new ArrayList<>();
	    Map< String,Object > shopMap = new HashMap<>();
	    shopMap.put( "id", stoId );
	    shopList.add( shopMap );
	    params.put( "userId", user.getId() );
	    params.put( "groupPId", "0" );
	    params.put( "isLabel", 0 );
	    params.put( "type", 1 );//显示推荐数据
	    PageUtil page = mallGroupService.findGroupDialogByPage( params, shopList, user.getId() );// 获取分组集合
	    resultMap.put( "page", page );
//	    resultMap.put( "stoId", params.get( "stoId" ) );
//	    resultMap.put( "check", params.get( "check" ) );

	} catch ( Exception e ) {
	    logger.error( "选择分类出错：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	//	return "mall/page/branchGroup";
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), resultMap );
    }
}