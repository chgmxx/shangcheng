package com.gt.mall.controller.seller;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.seller.MallSellerWithdrawService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallSellers" )
public class MallSellerController extends BaseController {

    @Autowired
    private MallSellerService         mallSellerService;
    @Autowired
    private MallStoreService          mallStoreService;
    @Autowired
    private MallPaySetService         mallPaySetService;
    @Autowired
    private MallSellerWithdrawService mallSellerWithdrawService;
    @Autowired
    private BusUserService            busUserService;

    /**
     * 商品佣金设置
     */
    @RequestMapping( value = "/joinProduct" )
    public String joinProduct( @RequestParam Map< String,Object > params, HttpServletRequest request,
		    HttpServletResponse response ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "shoplist", shoplist );
		PageUtil page = mallSellerService.selectProductByShopId( params, shoplist );
		request.setAttribute( "page", page );
		request.setAttribute( "shoplist", shoplist );
	    }
	    request.setAttribute( "type", params.get( "type" ) );
	    request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
	    request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	} catch ( Exception e ) {
	    logger.error( "商品佣金设置异常：" + e.getMessage() );
	    e.printStackTrace();
	}
	return "mall/seller/joinProduct";
    }

    /**
     * 进入商品佣金设置
     */
    @RequestMapping( value = "/to_edit_join" )
    public String editJoinProduct( @RequestParam Map< String,Object > params, HttpServletRequest request,
		    HttpServletResponse response ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据团购id查询团购信息
		Map< String,Object > joinProductMap = mallSellerService.selectJoinProductById( id );
		if ( joinProductMap != null ) {
		    Object imageUrl = joinProductMap.get( "specImageUrl" );
		    if ( CommonUtil.isEmpty( imageUrl ) ) {
			imageUrl = joinProductMap.get( "imageUrl" );
		    }
		    joinProductMap.put( "imgUrl", imageUrl );
		}
		request.setAttribute( "joinProductMap", joinProductMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( " 进入商品佣金设置页面异常：" + e.getMessage() );
	}
	return "mall/seller/editJoinProduct";
    }

    /**
     * 保存商品佣金设置
     */
    @SysLogAnnotation( description = "销售员——商品佣金设置", op_function = "2" )
    @RequestMapping( value = "/editJoinProduct" )
    public void editJoinProduct( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入商品佣金设置Controller" );
	PrintWriter pw = null;
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    pw = response.getWriter();
	    BusUser user = MallSessionUtils.getLoginUser( request );

	    resultMap = mallSellerService.saveOrUpdSellerJoinProduct( user.getId(), params );

	} catch ( Exception e ) {
	    logger.error( "保存商品佣金设置失败：" + e.getMessage() );
	    resultMap.put( "flag", false );
	    e.printStackTrace();
	}
	pw.write( JSONObject.fromObject( resultMap ).toString() );
	pw.flush();
	pw.close();
    }

    /**
     * 商品佣金的删除或失效
     */
    @SysLogAnnotation( description = "销售员——商品佣金的删除或失效", op_function = "3" )
    @RequestMapping( value = "/upJoinProduct" )
    public void upJoinProduct( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入商品佣金的删除或失效Controller" );
	PrintWriter pw = null;
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    pw = response.getWriter();
	    BusUser user = MallSessionUtils.getLoginUser( request );

	    resultMap = mallSellerService.saveOrUpdSellerJoinProduct( user.getId(), params );

	} catch ( Exception e ) {
	    logger.error( "商品佣金的删除或失效异常：" + e.getMessage() );
	    resultMap.put( "flag", false );
	    e.printStackTrace();
	}
	pw.write( JSONObject.fromObject( resultMap ).toString() );
	pw.flush();
	pw.close();
    }

    /**
     * 超级销售员设置
     */
    @RequestMapping( value = "/sellerSet" )
    public String sellerSet( @RequestParam Map< String,Object > params, HttpServletRequest request,
		    HttpServletResponse response ) {
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    boolean isAdminFlag = mallStoreService.getIsAdminUser( user.getId(), request );//是管理员
	    if ( isAdminFlag ) {
		MallSellerSet sellerSet = mallSellerService.selectByBusUserId( user.getId() );
		request.setAttribute( "sellerSet", sellerSet );

		MallPaySet set = new MallPaySet();
		set.setUserId( user.getId() );
		set = mallPaySetService.selectByUserId( set );
		if ( CommonUtil.isNotEmpty( set ) ) {
		    if ( CommonUtil.isNotEmpty( set.getIsSeller() ) ) {
			if ( set.getIsSeller().toString().equals( "1" ) ) {
			    request.setAttribute( "isOpenSeller", true );
			}
		    }
		}
		request.setAttribute( "user", user );
		request.setAttribute( "httpUrl", PropertiesUtil.getHomeUrl() );
	    } else {
		request.setAttribute( "isNoAdminFlag", 1 );
	    }
	    request.setAttribute( "videourl", busUserService.getVoiceUrl( "85" ) );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "超级销售员设置异常：" + e.getMessage() );
	}
	return "mall/seller/sellerSet";
    }

    /**
     * 保存商城设置
     */
    @SysLogAnnotation( description = "销售员——功能设置", op_function = "2" )
    @RequestMapping( value = "/editSellerSet" )
    public void editSellerSet( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入保存商城设置Controller" );
	PrintWriter pw = null;
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    pw = response.getWriter();
	    BusUser user = MallSessionUtils.getLoginUser( request );

	    resultMap = mallSellerService.saveOrUpdSellerSet( user.getId(), params );

	} catch ( Exception e ) {
	    logger.error( "保存商城设置失败：" + e.getMessage() );
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "保存商城设置失败，请稍后重试" );
	    e.printStackTrace();
	}
	pw.write( JSONObject.fromObject( resultMap ).toString() );
	pw.flush();
	pw.close();
    }

    /**
     * 销售员审核列表
     */
    @RequestMapping( value = "/sellerCheckList" )
    public String sellerCheckList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入超级销售员列表Controller" );
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( params.get( "keyWord" ) ) ) {
		String keyWord = CommonUtil.getBytes( params.get( "keyWord" ).toString() );
		params.put( "keyWord", keyWord );
		request.setAttribute( "keyWord", keyWord );
	    }
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    set = mallPaySetService.selectByUserId( set );
	    int check = 0;
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsSeller() ) ) {
		    check = CommonUtil.toInteger( set.getIsCheckSeller() );
		}
	    }
	    if ( check == 1 ) {
		PageUtil page = mallSellerService.selectCheckSeller( user.getId(), params );
		request.setAttribute( "page", page );
	    }

	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "销售员审核列表异常：" + e.getMessage() );
	}
	return "mall/seller/sellerCheckList";
    }

    /**
     * 审核销售员信息
     */
    @SysLogAnnotation( description = "销售员——审核销售员", op_function = "3" )
    @RequestMapping( value = "/checkSeller" )
    public void checkSeller( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入保存商城设置Controller" );
	PrintWriter pw = null;
	Map< String,Object > resultMap = new HashMap< String,Object >();
	try {
	    pw = response.getWriter();
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    WxPublicUsers wxPublicUsers = MallSessionUtils.getLoginPbUser( request );

	    boolean flag = mallSellerService.checkSeller( user.getId(), params, wxPublicUsers );
	    resultMap.put( "flag", flag );

	} catch ( Exception e ) {
	    logger.error( "审核销售员失败：" + e.getMessage() );
	    resultMap.put( "flag", false );
	    e.printStackTrace();
	}
	pw.write( JSONObject.fromObject( resultMap ).toString() );
	pw.flush();
	pw.close();
    }

    /**
     * 超级销售员列表
     */
    @RequestMapping( value = "/sellerList" )
    public String sellerList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入超级销售员列表Controller" );
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( params.get( "keyWord" ) ) ) {
		String keyWord = CommonUtil.getBytes( params.get( "keyWord" ).toString() );
		params.put( "keyWord", keyWord );
		request.setAttribute( "keyWord", keyWord );
	    }
	    MallPaySet set = new MallPaySet();
	    set.setUserId( user.getId() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }
	    request.setAttribute( "isCheck", isCheck );
	    params.put( "isCheck", isCheck );
	    PageUtil page = mallSellerService.selectSellerPage( user.getId(), params );
	    request.setAttribute( "page", page );

	    if ( CommonUtil.isNotEmpty( params.get( "saleMemId" ) ) ) {
		MallSeller seller = mallSellerService.selectSellerByMemberId( CommonUtil.toInteger( params.get( "saleMemId" ) ) );
		request.setAttribute( "seller", seller );
		request.setAttribute( "saleMemId", params.get( "saleMemId" ) );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "超级销售员列表异常：" + e.getMessage() );
	}
	return "mall/seller/sellerList";
    }

    /**
     * 提现列表
     */
    @RequestMapping( value = "/withDrawList" )
    public String withDrawList( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入提现列表Controller" );
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( params.get( "keyWord" ) ) ) {
		String keyWord = CommonUtil.getBytes( params.get( "keyWord" ).toString() );
		params.put( "keyWord", keyWord );
		request.setAttribute( "keyWord", keyWord );
	    }

	    PageUtil page = mallSellerWithdrawService.withdrawPage( user.getId(), params );

	    request.setAttribute( "page", page );
	    if ( CommonUtil.isNotEmpty( params.get( "startTime" ) ) ) {
		request.setAttribute( "startTime", params.get( "startTime" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "endTime" ) ) ) {
		request.setAttribute( "endTime", params.get( "endTime" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "saleMemId" ) ) ) {
		MallSeller seller = mallSellerService.selectSellerByMemberId( CommonUtil.toInteger( params.get( "saleMemId" ) ) );
		request.setAttribute( "seller", seller );
		request.setAttribute( "saleMemId", params.get( "saleMemId" ) );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "提现异常：" + e.getMessage() );
	}
	return "mall/seller/withDrawList";
    }

    /**
     * 获取二维码的图片
     */
    @RequestMapping( value = "/getTwoCode" )
    public void getTwoCode( @RequestParam Map< String,Object > params, HttpServletRequest request, HttpServletResponse response ) {
	try {
	    String code = params.get( "code" ).toString();
	    String content = PropertiesUtil.getHomeUrl() + "/mallPage/" + code + "/79B4DE7C/phoneProduct.do?view=show";
	    QRcodeKit.buildQRcode( content, 200, 200, response );
	} catch ( Exception e ) {
	    logger.error( "获取拍卖二维码图片失败：" + e.getMessage() );
	    e.printStackTrace();
	}
    }
}
