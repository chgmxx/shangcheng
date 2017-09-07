package com.gt.mall.controller.pifa.phone;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.util.entity.param.sms.OldApiSms;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private MallPifaService mallPifaService;

    @Autowired
    private MallOrderDAO mallOrderDAO;

    @Autowired
    private MallPageService mallPageService;

    @Autowired
    private MallPifaApplyService mallPifaApplyService;
    @Autowired
    private MallPaySetService    mallPaySetService;
    @Autowired
    private MemberService        memberService;
    @Autowired
    private WxPublicUserService  wxPublicUserService;
    @Autowired
    private SmsService           smsService;

    /**
     * 进入申请批发商页面
     */
    @RequestMapping( value = "/79B4DE7C/toApplyWholesaler" )
    public String toApplyWholesaler( HttpServletRequest request, HttpServletResponse response ) {
	logger.info( "进入申请批发商页面" );
	Member member = SessionUtils.getLoginMember( request );
	int userid = 0;
	WxPublicUsers wx = null;
	if ( CommonUtil.isNotEmpty( request.getParameter( "uId" ) ) ) {
	    userid = CommonUtil.toInteger( request.getParameter( "uId" ) );
	    wx = wxPublicUserService.selectByUserId( userid );
	} else if ( CommonUtil.isNotEmpty( request.getParameter( "pub_id" ) ) ) {//在公众号直接放链接
	    wx = wxPublicUserService.selectById( CommonUtil.toInteger( request.getParameter( "pub_id" ) ) );
	}
	if ( userid == 0 && CommonUtil.isNotEmpty( request.getParameter( "member_id" ) ) ) {
	    int memberid = CommonUtil.toInteger( request.getParameter( "member_id" ) );
	    wx = wxPublicUserService.selectByMemberId( memberid );
	    userid = wx.getBusUserId();
	}
	if ( userid == 0 && CommonUtil.isNotEmpty( wx ) ) {
	    userid = wx.getBusUserId();
	    request.setAttribute( "userid", userid );
	}
	try {
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }

	    MallPaySet set = mallPaySetService.selectByMember( member );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getPfApplyRemark() ) ) {
		    request.setAttribute( "pfApplayRemark", set.getPfApplyRemark() );
		}
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}

	request.setAttribute( "memberId", member.getId() );
	request.setAttribute( "userid", userid );
	return "mall/wholesalers/phone/applyWholesalers";
    }

    /**
     * 添加批发商
     */
    @RequestMapping( value = "/79B4DE7C/addWholesaler" )
    public void addWholesaler( HttpServletRequest request, @RequestParam Map< String,Object > map, HttpServletResponse response ) {
	PrintWriter pw = null;
	Map< String,Object > maps = new HashMap< String,Object >();
	try {
	    String msg = "";
	    boolean result = false;
	    pw = response.getWriter();
	    int count = 0;
	    MallPifaApply pifaApply = (MallPifaApply) JSONObject.toBean( JSONObject.fromObject( map.get( "obj" ) ), MallPifaApply.class );
	    Member member = SessionUtils.getLoginMember( request );
			/*if(CommonUtil.isEmpty(member)){
				member = new Member();
			}
			member.setId(200);*/
	    pifaApply.setMemberId( member.getId() );
	    pifaApply.setBusUserId( member.getBusid() );
	    pifaApply.setCreateTime( new Date() );

	    //查询是否添加批发商
	    MallPifaApply isMallPifaApply = mallPifaService.selectByPifaApply( pifaApply );
	    if ( CommonUtil.isEmpty( isMallPifaApply ) ) {
		count = mallPifaService.addWholesaler( pifaApply );//添加批发商
	    } else {
		if ( !isMallPifaApply.getStatus().toString().equals( "1" ) ) {
		    pifaApply.setId( isMallPifaApply.getId() );
		    pifaApply.setStatus( 0 );
		    count = mallPifaService.updateWholesaleApplay( pifaApply );
		} else {
		    msg = "您的批发商申请已经通过，无需再次提交申请";
		}
	    }
	    if ( count > 0 ) {
		result = true;
		JSONObject obj = JSONObject.fromObject( map.get( "obj" ) );
		JedisUtil.del( obj.get( "code" ).toString() );//申请批发商成功，删除验证码
	    }
	    maps.put( "msg", msg );
	    maps.put( "result", result );
	} catch ( Exception e ) {
	    maps.put( "result", false );
	    maps.put( "msg", "批发商申请异常，请稍后再申请" );
	    e.printStackTrace();
	} finally {
	    pw.write( JSONObject.fromObject( maps ).toString() );
	    pw.flush();
	    pw.close();
	}
    }

    /**
     * 发送短信
     *
     * @param sType 验证类型: 0 短信验证, 1 语音验证
     * @param telNo 电话
     */
    @RequestMapping( value = "/79B4DE7C/sendMsg" )
    @ResponseBody
    public Map< String,Object > sendMsg( HttpServletResponse response, HttpServletRequest request, @RequestParam String telNo, @RequestParam String sType,
		    @RequestParam String mType ) {
	logger.debug( "进入短信发送,手机号:" + telNo + "验证类型:" + sType );
	Map< String,Object > map = new HashMap<>();
	Member member = SessionUtils.getLoginMember( request );

	String no = CommonUtil.getPhoneCode();
	logger.info( "短信验证码：" + no );
	JedisUtil.set( no, no, 5 * 60 );

	try {
	    OldApiSms oldApiSms = new OldApiSms();
	    oldApiSms.setMobiles( telNo );
	    oldApiSms.setBusId( member.getBusid() );
	    oldApiSms.setContent( "您申请的批发商验证码:" + no + "，5分钟内有效。" );

	    boolean flag = smsService.sendSmsOld( oldApiSms );
	    if ( flag ) {
		map.put( "code", ResponseEnums.SUCCESS );
		map.put( "msg", "发送成功" );
	    }
	} catch ( Exception e ) {
	    map.put( "msg", "获取短信验证码失败" );
	    map.put( "code", ResponseEnums.ERROR.getCode() );
	}
	return map;
    }

    /**
     * 检验验证码
     */
    @RequestMapping( value = "/79B4DE7C/checkCode" )
    @ResponseBody
    public void checkCode( HttpServletRequest request, HttpServletResponse response, @RequestParam String applyCode ) {
	Map< String,Object > map = new HashMap<>();
	PrintWriter out = null;
	try {
	    out = response.getWriter();
	    if ( CommonUtil.isEmpty( applyCode ) ) {
		map.put( "result", 0 );
		map.put( "message", "请输入验证码" );
	    } else {
		String code = JedisUtil.get( applyCode );
		if ( CommonUtil.isEmpty( code ) ) {
		    map.put( "result", 0 );
		    map.put( "message", "验证码超时或错误" );
		} else if ( code.equals( applyCode ) ) {
		    map.put( "result", 1 );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "验证验证码异常：" + e.getMessage() );
	} finally {
	    out.write( JSONObject.fromObject( map ).toString() );
	    out.flush();
	    out.close();
	}

    }

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
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    Map< String,Object > mapuser = mallPageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map publicUserid = mallPageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) && userid == 0 ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }
	    Map< String,Object > loginMap = mallPageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    String returnUrl = userLogin( request, response, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    boolean isShop = mallPageService.wxShopIsDelete( shopid, null );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    String http = PropertiesUtil.getResourceUrl();// 图片url链接前缀
	    List groupList = mallPageService.storeList( shopid, 1, 0 );// 获取分类
	    String pageid = "0";
	    List list1 = mallPageService.shoppage( shopid );// 获取商品主页
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		pageid = map1.get( "id" ).toString();
	    }
	    String type = "1";// 查询条件
	    String desc = "1";// 排序 默认倒序
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = params.get( "type" ).toString();
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = params.get( "desc" ).toString();
	    }
	    params.put( "shopId", shopid );

	    //			MallPaySet set = mallPaySetService.selectByMember(member);
	    ////			int state = mallPifaApplyService.getPifaApplay(member, set);
	    //			boolean isPifa = false;
	    //			if(CommonUtil.isNotEmpty(set)){
	    //				if(CommonUtil.isNotEmpty(set.getIsPf())){
	    //					if(set.getIsPf().toString().equals("1")){
	    //						isPifa = true;
	    //						/*if(CommonUtil.isNotEmpty(set.getIsPfCheck())){
	    //							if(set.getIsPfCheck().toString().equals("1")){
	    //								if(state == 1){
	    //									isPifa = true;
	    //								}
	    //							}else{
	    //								isPifa = true;
	    //							}
	    //						}else{
	    //							isPifa = true;
	    //						}*/
	    //					}
	    //				}
	    //			}
	    //			if(isPifa){
	    List< Map< String,Object > > productList = mallPifaService
			    .getPifaAll( member, params );// 查询店铺下所有加入批发的商品
	    request.setAttribute( "productList", productList );
	    //			}

	    if ( CommonUtil.isEmpty( request.getSession().getAttribute( "shopId" ) ) ) {
		request.getSession().setAttribute( "shopId", shopid );
	    } else {
		if ( !request.getSession().getAttribute( "shopId" ).toString().equals( String.valueOf( shopid ) ) ) {
		    request.getSession().setAttribute( "shopId", shopid );
		}
	    }
	    //查询搜索标签，历史记录
	    mallPageService.getSearchLabel( request, shopid, member, params );

	    request.setAttribute( "shopId", shopid );
	    request.setAttribute( "pageid", pageid );
	    request.setAttribute( "type", type );
	    request.setAttribute( "desc", desc );
	    request.setAttribute( "groupList", groupList );
	    request.setAttribute( "imgHttp", http );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "proName", params.get( "proName" ) );
	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    mallPageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入批发商品的列表的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/wholesalers/phone/wholesalersall";
    }
}
