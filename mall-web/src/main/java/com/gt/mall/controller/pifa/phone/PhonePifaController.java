package com.gt.mall.controller.pifa.phone;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 商品批发表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/phoneWholesaler" )
public class PhonePifaController extends AuthorizeOrLoginController {

    /**
     * 获取店铺下所有的批发（手机）
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/wholesalerall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String wholesalerall( HttpServletRequest request,
		    HttpServletResponse response, @PathVariable int shopid,
		    @RequestParam Map< String,Object > params ) throws Exception {
	try {
	} catch ( Exception e ) {
	    logger.error( "进入批发商品的列表的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/wholesalers/phone/wholesalerAll";
    }
}
