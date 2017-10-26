package com.gt.mall.controller.seckill;

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
 * 秒杀表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mSeckill" )
public class MallSeckillController extends AuthorizeOrLoginController {

    /**
     * 获取店铺下所有的秒杀（手机）
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/seckillall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String seckillall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	try {
	} catch ( Exception e ) {
	    logger.error( "进入秒杀商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/seckill/phone/seckillall";
    }

}
