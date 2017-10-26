package com.gt.mall.controller.presale.phone;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.dao.presale.MallPresaleDepositDAO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.presale.MallPresaleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 商品预售表 前端控制器  (手机端)
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/phonePresale" )
public class PhonePresaleController extends AuthorizeOrLoginController {

    private static final Logger logger = Logger.getLogger( PhonePresaleController.class );

    @Autowired
    private MallPresaleService        mallPresaleService;
    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;
    @Autowired
    private MallPageService           pageService;
    @Autowired
    private MallPresaleDepositDAO     mallPresaleDepositDAO;
    @Autowired
    private MallPaySetService         mallPaySetService;
    @Autowired
    private MemberService             memberService;

    /**
     * 获取店铺下所有的预售（手机）
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( "{shopid}/79B4DE7C/presaleall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String presaleall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) {
	try {
	} catch ( Exception e ) {
	    logger.error( "进入预售商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/presale/phone/presaleall";
    }

}
