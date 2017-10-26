package com.gt.mall.controller.html;

import com.gt.mall.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 商城里面的H5 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mallhtml" )
public class MallHtmlController extends BaseController {

    /**
     * h5商城手机
     *
     * @param request
     * @param response
     * @param id
     *
     * @return
     */
    @RequestMapping( value = "{id}/79B4DE7C/phoneHtml" )
    public String phoneHtml( HttpServletRequest request, HttpServletResponse response, @PathVariable int id ) {

	return "/mall/htmlmall/phone/phonehtml";

    }

}
