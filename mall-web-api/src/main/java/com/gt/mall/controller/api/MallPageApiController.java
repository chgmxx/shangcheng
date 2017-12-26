package com.gt.mall.controller.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.bean.DictBean;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.utils.PropertiesUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallPage/mallAPI" )
public class MallPageApiController {

    private static Logger logger = LoggerFactory.getLogger( MallStoreApiController.class );

    @Autowired
    private MallPageService mallPageService;

    /**
     * 获取页面列表
     */
    @ApiOperation( value = "获取页面列表", notes = "获取页面列表" )
    @ResponseBody
    @RequestMapping( value = "/pageList", method = RequestMethod.POST )
    public ServerResponse pageList( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "userId", value = "商家Id", required = true ) @RequestParam Integer userId ) {
	List< Map > pageList = null;
	try {
	    Wrapper wrapper = new EntityWrapper();
	    wrapper.where( "pag_user_id = {0}  ", userId );
	    List< MallPage > list = mallPageService.selectList( wrapper );
	    if ( list != null && list.size() > 0 ) {
		pageList = new ArrayList<>();
		for ( MallPage page : list ) {
		    Map map = new HashMap();
		    map.put( "id", page.getId() );
		    map.put( "name", page.getPagName() );
		    map.put( "url", PropertiesUtil.getPhoneWebHomeUrl() + "/index/" + page.getId() );
		    pageList.add( map );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "获取页面列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取页面列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), pageList, false );
    }

}
