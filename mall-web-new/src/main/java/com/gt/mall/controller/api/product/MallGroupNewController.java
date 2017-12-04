package com.gt.mall.controller.api.product;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dao.product.MallSearchLabelDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.entity.product.MallSearchLabel;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.GroupDTO;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * <p>
 * 商品分组前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-19
 */
@Api( value = "mallGroup", description = "商品分组" )
@Controller
@RequestMapping( "/mallProduct/E9lM9uM4ct/group" )
public class MallGroupNewController extends BaseController {

    @Autowired
    private MallStoreCertificationService mallStoreCertService;
    @Autowired
    private MallImageAssociativeService   mallImageAssociativeService;
    @Autowired
    private MallGroupService              mallGroupService;
    @Autowired
    private MallStoreService              mallStoreService;
    @Autowired
    private MallSearchLabelDAO            mallSearchLabelDAO;

    @ApiOperation( value = "商品分组列表(分页)", notes = "商品分组列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pId", value = "父级分组ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer pId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "isProNum", true );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "userId", user.getId() );
		params.put( "curPage", curPage );
		params.put( "groupPId", pId );
		params.put( "isLabel", 0 );
		params.put( "type", 1 );//显示推荐数据
		PageUtil page = mallGroupService.findGroupByPage( params, shoplist, user.getId() );// 获取分组集合
		result.put( "page", page );
		result.put( "pId", pId );
	    }
	} catch ( Exception e ) {
	    logger.error( "商品分组列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "商品分组列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 保存分组信息
     */
    @ResponseBody
    @SysLogAnnotation( description = "保存分组信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    @ApiOperation( value = "保存分组信息", notes = "保存分组信息", response = GroupDTO.class )
    public ServerResponse save( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute GroupDTO group ) {
	try {
	    Integer userId = MallSessionUtils.getLoginUser( request ).getId();
	    List< MallImageAssociative > images = new ArrayList< MallImageAssociative >();

	    if ( !CommonUtil.isEmpty( userId ) ) {
		MallGroup mallGroup = JSONObject.parseObject( JSON.toJSONString( group ), MallGroup.class );
		mallGroup.setGroupName( CommonUtil.urlEncode( mallGroup.getGroupName() ) );
		if ( !CommonUtil.isEmpty( group.getImageList() ) ) {
		    images = JSONArray.parseArray( JSON.toJSONString( group.getImageList() ), MallImageAssociative.class );
		}

		boolean flag = mallGroupService.saveOrUpdateGroup( mallGroup, images, userId );
		if ( !flag ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存失败" );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NEED_LOGIN.getCode(), ResponseEnums.NEED_LOGIN.getDesc() );
	    }
	} catch ( Exception e )

	{
	    logger.error( "保存店铺认证信息：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取商品分组列表（用于商品新增调用）
     */
    @ApiOperation( value = "获取商品分组列表", notes = "获取商品分组列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "shopId", value = "店铺Id", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "proId", value = "商品id", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "groups", value = "已选中的分组ID集合,逗号隔开", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/getGroups", method = RequestMethod.POST )
    public ServerResponse getGroups( HttpServletRequest request, HttpServletResponse response, Integer shopId, Integer proId, String groups ) {
	/*shopId:177
	proId:43957
	group:621,623*/
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( !CommonUtil.isEmpty( user ) ) {
		params.put( "userId", user.getId() );
		params.put( "shopId", shopId );
		params.put( "proId", proId );
		params.put( "groups", groups );
		if ( CommonUtil.isEmpty( shopId ) ) {
		    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		    if ( shoplist != null && shoplist.size() > 0 ) {
			params.put( "shopId", shoplist.get( 0 ).get( "id" ) );
		    }
		}
		//查询所有分组
		List< Map< String,Object > > list = mallGroupService.selectGroupByParent( params );
		result.put( "groupList", list );
	    }
	    result.putAll( params );
	} catch ( Exception e ) {
	    logger.error( "获取商品分组列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商品分组列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取商品分组信息
     */
    @ApiOperation( value = "获取商品分组信息", notes = "获取商品分组信息", response = GroupDTO.class )
    @ResponseBody
    @RequestMapping( value = "/groupInfo", method = RequestMethod.POST )
    public ServerResponse groupInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "分组ID", required = true ) @RequestParam Integer id ) {
	MallGroup group = null;
	try {
	    group = mallGroupService.selectById( id );
	} catch ( Exception e ) {
	    logger.error( "获取商品分组信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取商品分组信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), group );
    }

    /**
     * 删除分组
     */
    @ApiOperation( value = "删除分组", notes = "删除分组" )
    @ResponseBody
    @SysLogAnnotation( description = "删除分组信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "ids", value = "分组ID集合,用逗号隔开", required = true ) @RequestParam String ids ) {
	try {
	    if ( CommonUtil.isNotEmpty( ids ) ) {
		String id[] = ids.toString().split( "," );
		if ( id != null && id.length > 0 ) {
		    for ( String str : id ) {
			if ( CommonUtil.isNotEmpty( str ) ) {
			    mallGroupService.deleteGroup( CommonUtil.toInteger( str ) );
			}
		    }
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.NULL_ERROR.getCode(), "参数不能为空" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除分组信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除分组信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除分组异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 推荐分组
     */
    @ApiOperation( value = "推荐分组", notes = "推荐分组" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "groupId", value = "分组ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "status", value = "1推荐 2取消推荐", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @SysLogAnnotation( description = "推荐分组", op_function = "4" )
    @RequestMapping( value = "/recommend", method = RequestMethod.POST )
    public ServerResponse recommend( HttpServletRequest request, HttpServletResponse response, Integer groupId, Integer status ) {
	try {
	    Integer userId = MallSessionUtils.getLoginUser( request ).getId();

	    MallGroup mallGroup = mallGroupService.selectById( groupId );
	    if ( mallGroup != null ) {
		MallSearchLabel label = new MallSearchLabel();
		label.setUserId( userId );
		label.setShopId( mallGroup.getShopId() );
		label.setGroupId( groupId );

		MallSearchLabel sLabel = mallSearchLabelDAO.selectOne( label );
		if ( CommonUtil.isNotEmpty( sLabel ) ) {
		    label.setId( sLabel.getId() );
		}
		if ( status == 2 ) {
		    label.setIsDelete( 1 );
		}else{
		    label.setIsDelete( 0 );
		}
		if ( CommonUtil.isNotEmpty( label.getId() ) ) {
		    mallSearchLabelDAO.updateById( label );
		} else {
		    label.setCreateTime( new Date() );
		    mallSearchLabelDAO.insert( label );
		}
	    }
	} catch ( BusinessException e ) {
	    logger.error( "推荐分组信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "推荐分组信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "推荐分组异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), false );
    }
}
