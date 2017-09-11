package com.gt.mall.controller;

import com.gt.mall.base.BaseController;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.util.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * API 参考类
 *
 * @author zhangmz
 * @create 2017/7/8
 */
@Controller
@Api(description="Test", value = "TestController")
public class TestController extends BaseController {

    @Test
    public void test() {
	Map< String,Integer > params = new HashMap<>();
	params.put( "id", 2 );
	params.put( "userId", 42 );
	//	MallPaySet set = (MallPaySet) JSON.parseArray(params.toString(), MallPaySet.class);
	MallPaySet set2 = (MallPaySet) JSONObject.toBean( JSONObject.fromObject( params ), MallPaySet.class );
	System.out.println( "开始" );
	System.out.println( set2.getId() );
    }

    /**
     * 跳转index 页面
     *
     * @param map
     *
     * @return
     */
    @ApiOperation( value = "首页", notes = "首页" )
    @RequestMapping( value = "/", method = RequestMethod.POST )
    public ModelAndView index( ModelAndView map ) {
	try {

	/*WsWxShopInfo wxShopInfo = wxShopService.getShopById( 21 );
	map.addObject( "wxShop", JSONObject.toJSONString( wxShopInfo ));*/

	    map.setViewName( "index" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return map;
    }

    @GetMapping( "/79B4DE7C/ss" )
    public ModelAndView test( ModelAndView map ) {
	try {
	    System.out.println( "MyConfigUtil.getHomeUrl() = " + PropertiesUtil.getHomeUrl() );
	    map.addObject( "homeUrl", 22 );

	    map.addObject( "test", "hello sssssddss!" );

	/*WsWxShopInfo wxShopInfo = wxShopService.getShopById( 21 );
	map.addObject( "wxShop", JSONObject.toJSONString( wxShopInfo ));*/

	    map.setViewName( "index" );
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return map;
    }

}
