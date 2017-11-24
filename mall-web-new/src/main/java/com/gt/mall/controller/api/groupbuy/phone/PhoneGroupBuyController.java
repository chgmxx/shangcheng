package com.gt.mall.controller.api.groupbuy.phone;

import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.html.MallHtml;
import com.gt.mall.entity.html.MallHtmlFrom;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.html.PhoneAddHtmlFromDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.groupbuy.MallGroupJoinService;
import com.gt.mall.service.web.html.MallHtmlFromService;
import com.gt.mall.service.web.html.MallHtmlReportService;
import com.gt.mall.service.web.html.MallHtmlService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购管理 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "phoneGroupBuy", description = "团购管理页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneGroupBuy/L6tgXlBFeK/" )
public class PhoneGroupBuyController extends AuthorizeOrUcLoginController {

    @Autowired
    private MallStoreService            storeService;
    @Autowired
    private MallGroupBuyService         groupBuyService;
    @Autowired
    private MallProductService          productService;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private MallGroupJoinService        groupJoinService;
    @Autowired
    private MallPageService             pageService;
    @Autowired
    private MemberService               memberService;

    /**
     * 获取团购详情信息
     */
    @ApiOperation( value = "获取团购详情信息", notes = "获取团购详情信息" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "团购ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "joinId", value = "参团ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "buyerUserId", value = "会员id", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/groupBuyDetail", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > groupBuyDetail( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, int id, Integer joinId, Integer buyerUserId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );

	    MallGroupBuy groupBuy = groupBuyService.selectById( id );

	    if ( buyerUserId == null && CommonUtil.isNotEmpty( member ) ) {
		buyerUserId = member.getId();
	    }
	    Map< String,Object > productMap = groupBuyService.getGroupBuyById( buyerUserId.toString(), id );// 通过团购id查询团购信息

	    Map< String,Object > maps = new HashMap<>();
	    maps.put( "groupBuyId", id );
	    maps.put( "joinId", joinId );
	    List< Map< String,Object > > joinList = groupJoinService.selectJoinByjoinId( maps );//查询参团人

	    //公众号分享
	    int chaPeopleNum = 0;
	    if ( CommonUtil.isNotEmpty( productMap ) && CommonUtil.isNotEmpty( productMap.get( "peopleNum" ) ) ) {
		chaPeopleNum = CommonUtil.toInteger( productMap.get( "peopleNum" ) ) - joinList.size();
	    }
	    String title = "还差" + chaPeopleNum + "人、我" + productMap.get( "price" ) + "元团了" + productMap.get( "pro_name" ) + "商品";
	    double old_price = 0;
	    double price = CommonUtil.toDouble( productMap.get( "price" ) );
	    if ( CommonUtil.isNotEmpty( productMap.get( "old_price" ) ) ) {
		old_price = CommonUtil.toDouble( productMap.get( "old_price" ) ) - price;
	    }
	    if ( old_price > 0 ) {
		title += "、立省" + old_price + "元";
	    }
	    title += "、快来参团";
	    String describe = "我参加了" + productMap.get( "pro_name" );

	    if ( CommonUtil.isNotEmpty( productMap.get( "is_specifica" ) ) ) {//存在规格
		String isSpecifica = productMap.get( "is_specifica" ).toString();
		if ( isSpecifica.equals( "1" ) ) {
		    String inv_id = "";//存放默认库存id
		    if ( CommonUtil.isNotEmpty( productMap.get( "inv_id" ) ) ) {
			inv_id = productMap.get( "inv_id" ).toString();
		    }
		    Map< String,Object > guige = pageService.productSpecifications( CommonUtil.toInteger( productMap.get( "id" ) ), inv_id );//通过商品id和库存id查询商品规格信息
		    result.put( "guige", guige );//规格信息 xids specifica_name
		}
	    }
	    if ( CommonUtil.isNotEmpty( productMap ) ) {
		MallRedisUtils.getMallShopId( productMap.get( "shopId" ) );//从session获取店铺id  或  把店铺id存入session
	    }
	    double discount = 1;//商品折扣
	    String is_member_discount = productMap.get( "is_member_discount" ).toString();//商品是否参加折扣
	    if ( ( is_member_discount == "1" || is_member_discount.equals( "1" ) ) && CommonUtil.isNotEmpty( member ) ) {
		discount = memberService.getMemberDiscount( member.getId() );//商品折扣
	    }

	    int isMember = 0;
	    int orderId = 0;
	    if ( joinList != null && joinList.size() > 0 && CommonUtil.isNotEmpty( member ) ) {
		for ( Map< String,Object > map : joinList ) {
		    String joinUserId = map.get( "joinUserId" ).toString();
		    if ( joinUserId.equals( member.getId().toString() ) ) {
			isMember = 1;
			orderId = CommonUtil.toInteger( map.get( "orderId" ) );
			break;
		    }
		}
	    }

	    int groupBuyId = 0;
	    if ( CommonUtil.isNotEmpty( productMap.get( "gBuyId" ) ) ) {
		groupBuyId = CommonUtil.toInteger( productMap.get( "gBuyId" ) );
	    }
	    if ( groupBuyId > 0 && CommonUtil.isNotEmpty( buyerUserId ) ) {
		Map< String,Object > groupMap = new HashMap< String,Object >();
		groupMap.put( "groupBuyId", groupBuyId );
		groupMap.put( "joinUserId", buyerUserId );
		//查询用户参加团购的数量
		int groupBuyCount = groupJoinService.selectCountByBuyId( groupMap );
		result.put( "groupBuyCount", groupBuyCount );//团购的数量
	    }

	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", groupBuy.getShopId() );
	    List< Map< String,Object > > productList = groupBuyService.getGroupBuyAll( member, params );// 查询店铺下所有加入团购的商品

	    result.put( "title", title );//分享标题
	    result.put( "describe", describe );//分享描述
	    result.put( "discount", discount );//折扣价
	    result.put( "isMember", isMember );//0用户还没参团或开团  1用户已经参团或开团
	    result.put( "orderId", orderId );//订单ID
	    result.put( "productMap", productMap );//团购商品信息
	    result.put( "joinList", joinList );//参团人列表
	    result.put( "productList", productList );//其它团购列表

	} catch ( Exception e ) {
	    logger.error( "获取团购详情信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取团购详情信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

}


