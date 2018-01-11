package com.gt.mall.controller.product;

import com.gt.mall.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mPro" )
public class MallProductController extends BaseController {


    /**
     * 获取二维码的图片
     */
    @RequestMapping( value = "/79B4DE7C/codeIframs" )
    public String codeIframs( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {

	} catch ( Exception e ) {
	    logger.error( "获取拍卖二维码图片失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/product/iframe/product_shop_buy";
    }



}
