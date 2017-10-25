package com.gt.mall.controller.api.product;

import com.alibaba.fastjson.JSON;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.entity.product.MallSpecifica;
import com.gt.mall.entity.product.MallSpecificaValue;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.SessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>
 * 商品规格前端控制器
 * </p>
 *
 * @author maoyl
 * @since 2017-09-21
 */
@Api( value = "mallProductSpec", description = "商品规格", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallProduct/spec" )
public class MallSpecificaNewController extends BaseController {

    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;

    /**
     * 获取规格名称和值
     */
    @ApiOperation( value = "获取规格名称和值", notes = "获取规格名称和值" )
    @ResponseBody
    @RequestMapping( value = "/getSpecificaList", method = RequestMethod.POST )
    public ServerResponse getSpecificaList( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "map", value = "id:不为空 则查询值 ，type:  1 规格 2参数，  shopId: 店铺ID", required = true ) @RequestParam Map< String,Object > params ) {
	SortedMap< String,Object > map;
	try {
	    //id:334   不为空 则查询值
	    //type:2  1 规格， 2 参数
	    //shopId:177 店铺ID
	    Integer userId = SessionUtils.getLoginUser( request ).getId();

	    if ( CommonUtil.isEmpty( params.get( "id" ) ) ) {
		params.put( "userId", userId );
		// 查询自定义规格名称
		map = mallProductSpecificaService.getSpecificaByUser( params );// 查询自定义规格名称
	    } else {
		map = mallProductSpecificaService.getSpecificaValueById( params );
	    }

	} catch ( Exception e ) {
	    logger.error( "获取规格名称和值异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取规格名称和值异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), map );
    }

    /**
     * 新增自定义规格
     */
    @ApiOperation( value = "新增自定义规格", notes = "新增自定义规格" )
    @ResponseBody
    @RequestMapping( value = "/addSpecifica", method = RequestMethod.POST )
    public ServerResponse addSpecifica( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "map", value = "specName:名称， specId: 值， type:  1 规格 2参数 ， shopId: 店铺ID", required = true ) @RequestParam Map< String,Object > map ) {
	Integer id = null;
	try {
	    //specName:334   名称
	    //specId:5057  值
	    //type:2  1 规格， 2 参数
	    //shopId:177 店铺ID
	    Integer userId = SessionUtils.getLoginUser( request ).getId();

	    if ( !CommonUtil.isEmpty( map.get( "specId" ) ) ) {// 添加规格值
		MallSpecificaValue value = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( map ), MallSpecificaValue.class );
		value.setSpecValue( CommonUtil.urlEncode( value.getSpecValue() ) );
		value.setUserId( userId );
		mallProductSpecificaService.insertSpecificaValue( value );
		id = value.getId();
	    } else {// 添加规格名称
		MallSpecifica spe = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( map ), MallSpecifica.class );
		spe.setSpecName( CommonUtil.urlEncode( spe.getSpecName() ) );
		spe.setUserId( userId );
		spe.setCreateTime( new Date() );
		mallProductSpecificaService.insertSpecifica( spe );
		id = spe.getId();
	    }
	} catch ( Exception e ) {
	    logger.error( "新增自定义规格异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "新增自定义规格异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), id );
    }

}
