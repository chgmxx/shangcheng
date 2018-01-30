package com.gt.mall.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.DictBean;
import com.gt.mall.dao.store.MallStoreCertificationDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private MallStoreService              mallStoreService;

    @ApiOperation( value = "待审核店铺认证列表", notes = "待审核店铺认证列表" )
    //    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
    //		    @ApiImplicitParam( name = "pageSize", value = "显示数量 默认15条", paramType = "query", required = false, dataType = "int" ),
    //		    @ApiImplicitParam( name = "userIds", value = "用户Id集合", paramType = "query", required = false, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "/certList", method = RequestMethod.POST )
    public ServerResponse certList( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );

	    List< Map< String,Object > > certList = null;
	    Integer curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	    Integer pageSize = CommonUtil.isEmpty( params.get( "pageSize" ) ) ? 15 : CommonUtil.toInteger( params.get( "pageSize" ) );

	    //	    params.put( "checkStatus", "0" );
	    if ( CommonUtil.isNotEmpty( params.get( "checkStatus" ) ) ) {
		params.put( "checkStatus", params.get( "checkStatus" ).toString() );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "userIds" ) ) ) {
		params.put( "userIds", params.get( "userIds" ).toString().split( "," ) );
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
			if ( CommonUtil.toInteger( map.get( "stoType" ) ) == 1 ) {
			    if ( categoryMap != null && categoryMap.size() > 0 ) {
				for ( DictBean dictBean : categoryMap ) {
				    if ( dictBean.getItem_key().toString().equals( map.get( "stoCategory" ).toString() ) ) {
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
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 获取店铺认证信息
     */
    @ApiOperation( value = "获取店铺认证信息", notes = "获取店铺认证信息" )
    @ResponseBody
    @RequestMapping( value = "/certInfo", method = RequestMethod.POST )
    public ServerResponse certInfo( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	MallStoreCertification storeCert = null;
	try {
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );

	    storeCert = mallStoreCertService.selectById( CommonUtil.toInteger( params.get( "id" ) ) );
	    if ( storeCert != null && storeCert.getIsDelete() == 1 ) {
		storeCert = null;
	    }
	    if ( storeCert != null ) {
		MallStore store = mallStoreService.selectById( storeCert.getStoId() );
		storeCert.setStoName( store.getStoName() );
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

		if ( storeCert.getIsCertDoc() == 1 ) {
		    Map< String,Object > assParams = new HashMap<>();
		    assParams.put( "assType", 6 );
		    assParams.put( "assId", storeCert.getId() );
		    List< MallImageAssociative > imageAssociativeList = mallImageAssociativeService.selectByAssId( assParams );
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
    public ServerResponse certCheck( HttpServletRequest request, HttpServletResponse response, @RequestBody String param ) {
	//		    @ApiParam( name = "status", value = "类型 1通过 -1不通过", required = true ) @RequestParam Integer status
	try {
	    logger.info( "接收到的参数：" + param );
	    Map< String,Object > params = JSONObject.parseObject( param );

	    MallStoreCertification certification = mallStoreCertService.selectById( CommonUtil.toInteger( params.get( "id" ) ) );
	    certification.setCheckStatus( CommonUtil.toInteger( params.get( "status" ) ) );
	    if ( CommonUtil.isNotEmpty( params.get( "reason" ) ) ) {
		certification.setRefuseReason( params.get( "reason" ).toString() );
	    }
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
