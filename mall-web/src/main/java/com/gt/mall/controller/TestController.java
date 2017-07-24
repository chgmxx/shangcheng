package com.gt.mall.controller;

import com.gt.mall.base.BaseController;
import com.gt.mall.config.MyConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * API 参考类
 *
 * @author zhangmz
 * @create 2017/7/8
 */
@Controller
public class TestController extends BaseController {

    @Autowired
    private MyConfig myConfig;

    /**
     * 跳转index 页面
     *
     * @param map
     *
     * @return
     */
    @ApiOperation( value = "首页", notes = "首页" )
    @GetMapping( { "", "/index", "/" } )
    public ModelAndView index( ModelAndView map ) {
	map.addObject( "homeUrl", myConfig.getHomeUrl() );

	map.addObject( "test", "hello zhangmz!" );
	map.setViewName( "index" );
	return map;
    }



}
