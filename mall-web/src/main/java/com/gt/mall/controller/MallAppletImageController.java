package com.gt.mall.controller;

import com.gt.mall.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * <p>
 * 小程序图片 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-17
 */
@Controller
@RequestMapping( "/mApplet" )
public class MallAppletImageController extends BaseController {

    @ResponseBody
    @GetMapping( "/" )
    public String hello(HttpSession session) {
        session.setAttribute( "name","yangqian" );
        this.logger.info( "sessionid : {}",session.getId() );
        return "你好..";
    }




}
