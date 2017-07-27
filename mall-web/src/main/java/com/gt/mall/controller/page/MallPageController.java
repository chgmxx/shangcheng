package com.gt.mall.controller.page;

import com.gt.mall.base.BaseController;
import io.swagger.annotations.ApiOperation;
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


    @ApiOperation( value = "首页", notes = "首页" )
    @GetMapping( "/" )
    public ModelAndView index( ModelAndView map ) {
	this.logger.info( "进来啦哈哈哈" );
	map.setViewName( "index" );
	return map;
    }
}
