package com.gt.mall.controller.product;

import com.alibaba.fastjson.JSON;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.entity.product.MallSpecifica;
import com.gt.mall.entity.product.MallSpecificaValue;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.SessionUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>
 * 规格 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( value = "mPro/spec" )
public class MallSpecificaController extends BaseController {
    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;

    /**
     * 获取规格名称和值
     *
     */
    @RequestMapping( "getSpec" )
    public void getSpec( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	response.setCharacterEncoding( "utf-8" );
	JSONObject obj = new JSONObject();
	try {
	    SortedMap< String,Object > map;
	    Integer userId = SessionUtils.getLoginUser( request ).getId();

	    /*int userPId = dictService.pidUserId(userId);//通过用户名查询主账号id
	    long isJxc = erpLoginOrMenusService.isjxcCount("8", userPId);//判断商家是否有进销存 0没有 1有
	    if(isJxc == 1){
		    mallProductSpecificaService.syncErpSpecifica(userPId);
	    }*/

	    if ( CommonUtil.isEmpty( params.get( "id" ) ) ) {
		params.put( "userId", userId );
		// 查询自定义规格名称
		map = mallProductSpecificaService.getSpecificaByUser( params );// 查询自定义规格名称
	    } else {
		map = mallProductSpecificaService.getSpecificaValueById( params );
	    }
	    obj.put( "map", map );

	} catch ( Exception e ) {
	    logger.error( e.getMessage() );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 自定义规格
     */
    @SysLogAnnotation( description = "商品管理-商品信息编辑，自定义规格", op_function = "2" )
    @RequestMapping( "addSpec" )
    public void addSpec( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > map ) throws IOException {
	logger.info( "进入自定义规格controller" );
	response.setCharacterEncoding( "utf-8" );
	Integer id = 0;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();

	    if ( !CommonUtil.isEmpty( map.get( "specId" ) ) ) {// 添加规格值
		MallSpecificaValue value = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( map ),MallSpecificaValue.class );
		value.setUserId( userId );
		mallProductSpecificaService.insertSpecificaValue( value );
		id = value.getId();
	    } else {// 添加规格名称
		MallSpecifica spe = com.alibaba.fastjson.JSONObject.parseObject( JSON.toJSONString( map ),MallSpecifica.class );
		spe.setUserId( userId );
		spe.setCreateTime( new Date() );
		mallProductSpecificaService.insertSpecifica( spe );
		id = spe.getId();
	    }
	} catch ( Exception e ) {
	    logger.error( "自定义规格异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "id", id );
	    CommonUtil.write( response, obj );
	}

    }
}
