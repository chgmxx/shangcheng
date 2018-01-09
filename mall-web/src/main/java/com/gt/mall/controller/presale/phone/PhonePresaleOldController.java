package com.gt.mall.controller.presale.phone;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
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
public class PhonePresaleOldController extends AuthorizeOrLoginController {

    private static final Logger logger = Logger.getLogger( PhonePresaleOldController.class );

    @Autowired
    private MallStoreService mallStoreService;

    /**
     * 获取店铺下所有的预售（手机）
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( "{shopid}/79B4DE7C/presaleall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String presaleall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) {
	int userid = 0;
	if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
	    userid = CommonUtil.toInteger( params.get( "uId" ) );
	}
	if ( userid <= 0 ) {
	    MallStore mallStore = mallStoreService.selectById( shopid );
	    userid = mallStore.getStoUserId();
	}
	return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/classify/" + shopid + "/" + userid + "/6/k=k";
    }

}
