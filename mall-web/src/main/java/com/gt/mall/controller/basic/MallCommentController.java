package com.gt.mall.controller.basic;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.basic.MallCommentGive;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.service.web.basic.MallCommentGiveService;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.mall.utils.SessionUtils;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城评论 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/comment" )
public class MallCommentController extends BaseController {

    @Autowired
    private MallCommentService     commentService;
    @Autowired
    private MallCommentGiveService commentGiveService;
    @Autowired
    private MallPaySetService      paySetService;
    @Autowired
    private MallStoreService       storeService;

    /**
     * 批量设置评论送礼
     *
     * @Title: batchCommentGive
     */
    @SuppressWarnings( { "unchecked", "deprecation" } )
    @SysLogAnnotation( description = "商城设置：批量设置评论", op_function = "2" )
    @RequestMapping( "/batchCommentGive" )
    public void batchCommentGive( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {

	logger.info( "进入评论送礼controller" );
	response.setCharacterEncoding( "utf-8" );
	boolean flag = false;// 编辑成功
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< MallCommentGive > giveList = com.alibaba.fastjson.JSONArray.parseArray( params.get( "datas" ).toString(), MallCommentGive.class );
	    flag = commentGiveService.editCommentGive( giveList, user );
	} catch ( Exception e ) {
	    flag = false;
	    logger.error( "编辑评论送礼：" + e.getMessage() );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	obj.put( "flag", flag );
	CommonUtil.write( response, obj );
    }

    /**
     * 进入评价管理列表
     */
    @RequestMapping( "/to_index" )
    public String to_index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    // 查询会员下面的评论
	    if ( shoplist != null && shoplist.size() > 0 ) {
		request.setAttribute( "shoplist", shoplist );

		params.put( "shoplist", shoplist );
		PageUtil page = commentService.selectCommentPage( params, shoplist );
		request.setAttribute( "page", page );
	    }
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = paySetService.selectByUserId( set );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsCommentCheck() ) ) {
		    if ( set.getIsCommentCheck() == 1 ) {
			request.setAttribute( "isOpenCheck", 1 );
		    }
		}
	    }

	    if ( CommonUtil.isNotEmpty( params.get( "checkStatus" ) ) ) {
		request.setAttribute( "checkStatus", params.get( "checkStatus" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "feel" ) ) ) {
		request.setAttribute( "feel", params.get( "feel" ) );
	    }
	    request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入评论管理列表异常" );
	    e.printStackTrace();
	}
	return "mall/comment/comment_index";
    }

    /**
     * 修改评论信息
     *
     * @Title: checkComment
     */
    @SysLogAnnotation( description = "评论管理——删除,审核评论", op_function = "2" )
    @RequestMapping( "/checkComment" )
    public void checkComment( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws JSONException, IOException {
	logger.info( "进入删除,审核评论的controller" );
	boolean result = false;
	String msg = "";
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    if ( CommonUtil.isNotEmpty( params.get( "ids" ) ) ) {
		result = commentService.checkComment( params );
	    }
	} catch ( Exception e ) {
	    result = false;
	    logger.error( "删除,审核评论失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	if ( !result ) {
	    map.put( "msg", "删除,审核评论失败，请稍后重试" );
	}
	map.put( "result", result );
	map.put( "msg", msg );

	CommonUtil.write( response, map );
    }

    /**
     * 回复评论
     *
     * @Title: repComment
     */
    @SysLogAnnotation( description = "评论管理——回复评论", op_function = "2" )
    @RequestMapping( "/repComment" )
    public void repComment( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws JSONException, IOException {
	logger.info( "进入回复评论的controller" );
	boolean result = false;
	String msg = "";
	Map< String,Object > map = new HashMap< String,Object >();
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( params.get( "params" ) ) && CommonUtil.isNotEmpty( user ) ) {
		result = commentService.replatComment( params, user.getId() );
	    }
	} catch ( Exception e ) {
	    result = false;
	    logger.error( "回复评论失败：" + e.getMessage() );
	    e.printStackTrace();
	}
	if ( !result ) {
	    map.put( "msg", "回复评论失败，请稍后重试" );
	}
	map.put( "result", result );
	map.put( "msg", msg );
	CommonUtil.write( response, map );
    }

}
