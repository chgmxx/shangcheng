package com.gt.mall.controller.page;

import com.gt.mall.base.BaseController;
import com.gt.mall.config.MyConfig;
import com.gt.mall.dto.ServerResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

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
public class MallPageController extends BaseController {

    @Autowired
    private MyConfig myConfig;

    public static void main( String[] args ) {
	System.out.println( " = " + ServerResponse.createByError() );
	;
    }

    @ApiOperation( value = "首页", notes = "首页" )
    @GetMapping( "/" )
    public ModelAndView index( ModelAndView map ) {
	this.logger.info( "进来啦哈哈哈" );
	System.out.println( "homeUrl = " + myConfig.getHomeUrl() );
	map.addObject( "homeUrl", myConfig.getHomeUrl() );
	map.setViewName( "index" );
	return map;
    }
}
