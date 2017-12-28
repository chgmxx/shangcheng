package com.gt.mall.controller.api;

import com.gt.mall.base.BaseController;
import com.gt.mall.bean.DictBean;
import com.gt.mall.dao.store.MallStoreCertificationDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 店铺认证相关接口
 * </p>
 *
 * @author maoyl
 * @since 2017-12-27
 */
@Api( value = "/mallStoreCert/mallAPI", description = "店铺认证相关API接口", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallStoreCert/mallAPI/" )
public class MallStoreCertificationApiController extends BaseController {

    @Autowired
    private MallStoreCertificationService mallStoreCertService;
    @Autowired
    private MallStoreCertificationDAO     mallStoreCertificationDAO;
    @Autowired
    private DictService                   dictService;
    @Autowired
    private MallImageAssociativeService   mallImageAssociativeService;

    @ApiOperation( value = "待审核店铺认证列表", notes = "待审核店铺认证列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认15条", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "userIds", value = "用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/waitCheckList", method = RequestMethod.POST )
    public ServerResponse waitCheckList( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer pageSize, String userIds ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    List< Map< String,Object > > certList = null;
	    curPage = CommonUtil.isEmpty( curPage ) ? 1 : curPage;
	    pageSize = CommonUtil.isEmpty( pageSize ) ? 15 : pageSize;

	    Map< String,Object > params = new HashMap<>();
	    params.put( "checkStatus", "0" );
	    if ( CommonUtil.isNotEmpty( userIds ) ) {
		params.put( "userIds", userIds.split( "," ) );
	    }
	    int count = mallStoreCertificationDAO.selectAllCount( params );

	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	    params.put( "firstNum", firstNum );// 起始页
	    params.put( "maxNum", pageSize );// 每页显示商品的数量

	    if ( count > 0 ) {// 判断是否有数据
		certList = mallStoreCertificationDAO.selectAllByPage( params );
		if ( certList != null && certList.size() > 0 ) {
		    List< DictBean > categoryMap = dictService.getDict( "K002" );
		    for ( Map< String,Object > map : certList ) {
			if ( CommonUtil.toInteger( map.get( "sto_type" ) ) == 1 ) {
			    if ( categoryMap != null && categoryMap.size() > 0 ) {
				for ( DictBean dictBean : categoryMap ) {
				    if ( dictBean.getItem_key().toString().equals( map.get( "sto_category" ).toString() ) ) {
					String value = dictBean.getItem_value();
					net.sf.json.JSONObject foorerObj = net.sf.json.JSONObject.fromObject( value );
					map.put( "stoCategoryName", foorerObj.get( "title" ).toString() );
					break;
				    }
				}
			    }
			}
		    }
		}
	    }
	    page.setSubList( certList );
	    result.put( "page", page );
	} catch ( Exception e ) {
	    logger.error( "待审核店铺认证列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "待审核店铺认证列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 获取店铺认证信息
     */
    @ApiOperation( value = "获取店铺认证信息", notes = "获取店铺认证信息" )
    @ResponseBody
    @RequestMapping( value = "/certInfo", method = RequestMethod.POST )
    public ServerResponse certInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "认证ID", required = true ) @RequestParam Integer id ) {
	MallStoreCertification storeCert = null;
	try {
	    storeCert = mallStoreCertService.selectById( id );
	    if ( storeCert != null ) {
		if ( storeCert.getStoType() == 1 ) {
		    List< DictBean > categoryMap = dictService.getDict( "K002" );
		    if ( categoryMap != null && categoryMap.size() > 0 ) {
			for ( DictBean dictBean : categoryMap ) {
			    if ( dictBean.getItem_key().toString().equals( storeCert.getStoCategory().toString() ) ) {
				String value = dictBean.getItem_value();
				net.sf.json.JSONObject foorerObj = net.sf.json.JSONObject.fromObject( value );
				storeCert.setStoCategoryName( foorerObj.get( "title" ).toString() );
				break;
			    }
			}
		    }
		}
		if ( storeCert.getIsCertDoc() == 1 ) {
		    Map< String,Object > params = new HashMap<>();
		    params.put( "assType", 6 );
		    params.put( "assId", storeCert.getId() );
		    List< MallImageAssociative > imageAssociativeList = mallImageAssociativeService.selectByAssId( params );
		    storeCert.setImageList( imageAssociativeList );
		}
	    }

	} catch ( Exception e ) {
	    logger.error( "获取店铺认证信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取店铺认证信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), storeCert );
    }

    /**
     * 修改审核状态
     */
    @ApiOperation( value = "认证审核", notes = "认证审核" )
    @ResponseBody
    @RequestMapping( value = "/certCheck", method = RequestMethod.POST )
    public ServerResponse certCheck( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "认证ID", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "status", value = "类型 1通过 -1不通过", required = true ) @RequestParam Integer status ) {
	try {
	    MallStoreCertification certification = mallStoreCertService.selectById( id );
	    certification.setCheckStatus( status );
	    boolean flag = mallStoreCertService.updateById( certification );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "认证审核异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "认证审核异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "认证审核异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

}
