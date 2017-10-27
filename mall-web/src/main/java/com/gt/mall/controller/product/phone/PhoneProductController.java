package com.gt.mall.controller.product.phone;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.product.MallGroupService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallRedisUtils;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.MallSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
@RequestMapping( "/phoneProduct" )
public class PhoneProductController extends BaseController {

    @Autowired
    private MallGroupService     mallGroupService;
    @Autowired
    private BusUserService       busUserService;
    @Autowired
    private MallPageService      mallPageService;
    @Autowired
    private MallPaySetService    mallPaySetService;
    @Autowired
    private MallPifaApplyService mallPifaApplyService;
    @Autowired
    private MallProductService   mallProductService;

    /**
     * 查询子分类
     */
    @RequestMapping( "79B4DE7C/getChildGroup" )
    public void getChildGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	JSONObject obj = new JSONObject();
	try {
	    if ( CommonUtil.isNotEmpty( params.get( "groupPId" ) ) ) {
		//todo 参数改变了
		List< MallGroup > childList = mallGroupService.selectChildGroupByPId( params );
		obj.put( "childList", childList );
		String http = PropertiesUtil.getResourceUrl();
		obj.put( "http", http );

	    }
	} catch ( Exception e ) {
	    logger.error( "查询子分组出错:" + e );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 清空搜索查询的标签
     */
    @RequestMapping( "79B4DE7C/clearSearchGroup" )
    public void clearSearchGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	JSONObject obj = new JSONObject();
	boolean result = false;
	try {
	    params.put( "userId", MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() ) );

	    result = mallGroupService.clearSearchKeyWord( params );

	} catch ( Exception e ) {
	    logger.error( "清空搜索查询的标签出错:" + e );
	    e.printStackTrace();
	} finally {
	    obj.put( "result", result );
	    CommonUtil.write( response, obj );
	}

    }

    /**
     * 查询商品集合
     *
     * @throws IOException
     */
    @RequestMapping( "79B4DE7C/getProductByIds" )
    public void getProductByIds( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	JSONObject obj = new JSONObject();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() );
	    if ( CommonUtil.isNotEmpty( params.get( "proIds" ) ) ) {

		String proIds = CommonUtil.toString( params.get( "proIds" ) );
		int userid = 0;
		if ( CommonUtil.isNotEmpty( params.get( "userid" ) ) ) {
		    userid = CommonUtil.toInteger( params.get( "userid" ) );
		}
		double discount = mallProductService.getMemberDiscount( "1", member );//商品折扣

		MallPaySet set = new MallPaySet();
		set.setUserId( userid );
		set = mallPaySetService.selectByUserId( set );
		boolean isPifa = mallPifaApplyService.isPifaPublic( member, set );

		List< Map< String,Object > > proList = mallPageService.getProductListByIds( proIds, member, discount, set, isPifa );

		obj.put( "data", proList );
		obj.put( "code", 1 );
	    }
	} catch ( Exception e ) {
	    obj.put( "code", -1 );
	    logger.error( "查询商品列表异常:" + e );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, obj );
	}
    }
}
