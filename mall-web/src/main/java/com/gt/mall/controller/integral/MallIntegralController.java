package com.gt.mall.controller.integral;

import com.gt.mall.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 积分商品表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mallIntegral" )
public class MallIntegralController extends BaseController {

    /**
     * 获取二维码的图片
     *
     * @param params
     */
    @RequestMapping( value = "/79B4DE7C/integralMallCodeIframs" )
    public String integralMallCodeIframs( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
        try {

        } catch ( Exception e ) {
            logger.error( "获取积分商城二维码图片失败：" + e.getMessage() );
            e.printStackTrace();
        }
        return "mall/integral/iframe/integral_mall";
    }

}
