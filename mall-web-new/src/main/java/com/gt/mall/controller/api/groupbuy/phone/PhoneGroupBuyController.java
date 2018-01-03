package com.gt.mall.controller.api.groupbuy.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.service.inter.core.CoreService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.groupbuy.MallGroupJoinService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallRedisUtils;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    private MallGroupBuyService  groupBuyService;
    @Autowired
    private MallGroupJoinService groupJoinService;
    @Autowired
    private CoreService          coreService;

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
	    coreService.payModel( loginDTO.getBusId(), CommonUtil.getAddedStyle( "1" ) );////判断活动是否已经过期
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );

	    MallGroupBuy groupBuy = groupBuyService.selectBuyByProductId( id );
	    if ( CommonUtil.isEmpty( groupBuy ) ) {
		throw new BusinessException( ResponseEnums.URL_GUOQI_ERROR.getCode(), ResponseEnums.URL_GUOQI_ERROR.getDesc() );
	    }
	    result.put( "status", groupBuy.getStatus() );

	    if ( groupBuy.getStatus() == -1 ) {
		result.put( "statusMsg", "活动已结束" );
	    } else if ( groupBuy.getStatus() == -2 ) {
		result.put( "statusMsg", "活动已失效" );
	    }

	    //	    if(groupBuy.getGStartTime())

	    if ( buyerUserId == null && CommonUtil.isNotEmpty( member ) ) {
		buyerUserId = member.getId();
	    }
	    Map< String,Object > productMap = groupBuyService.getGroupBuyById( groupBuy, groupBuy.getProductId(), joinId );// 通过团购id查询团购信息

	    Map< String,Object > maps = new HashMap<>();
	    maps.put( "groupBuyId", id );
	    maps.put( "joinId", joinId );
	    List< Map< String,Object > > joinList = groupJoinService.selectJoinByjoinId( maps );//查询参团人

	    //公众号分享
	    int chaPeopleNum = 0;
	    if ( CommonUtil.isNotEmpty( productMap ) && CommonUtil.isNotEmpty( groupBuy.getGPeopleNum() ) ) {
		chaPeopleNum = groupBuy.getGPeopleNum() - joinList.size();
	    }
	    String title = "还差" + chaPeopleNum + "人、我" + productMap.get( "price" ) + "元团了" + productMap.get( "proName" ) + "商品";
	    double old_price = 0;
	    double price = CommonUtil.toDouble( productMap.get( "price" ) );
	    if ( CommonUtil.isNotEmpty( productMap.get( "oldPrice" ) ) ) {
		old_price = CommonUtil.toDouble( productMap.get( "oldPrice" ) ) - price;
	    }
	    if ( old_price > 0 ) {
		title += "、立省" + old_price + "元";
	    }
	    title += "、快来参团";
	    String describe = "我参加了" + productMap.get( "proName" );

	    if ( CommonUtil.isNotEmpty( productMap ) ) {
		MallRedisUtils.getMallShopId( productMap.get( "shopId" ) );//从session获取店铺id  或  把店铺id存入session
	    }
	    result.put( "chaPeopleNum", chaPeopleNum );

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
	    //	    List< Map< String,Object > > productList = groupBuyService.getGroupBuyAll( member, params );// 查询店铺下所有加入团购的商品

	    //	    PhoneSearchProductDTO searchProductDTO = new PhoneSearchProductDTO();
	    //	    searchProductDTO.setShopId( groupBuy.getShopId() );
	    //	    searchProductDTO.setBusId( groupBuy.getUserId() );
	    //	    searchProductDTO.setType( 1 );
	    //	    PageUtil pageUtil = groupBuyService.searchGroupBuyProduct( searchProductDTO, member );

	    result.put( "title", title );//分享标题
	    result.put( "describe", describe );//分享描述
	    result.put( "isMember", isMember );//0用户还没参团或开团  1用户已经参团或开团
	    result.put( "orderId", orderId );//订单ID
	    result.put( "productMap", productMap );//团购商品信息
	    result.put( "joinList", joinList );//参团人列表
	    //	    result.put( "productList", productList );//其它团购列表
	    //	    result.put( "productPage", pageUtil );
	    result.put( "groupId", groupBuy.getId() );

	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "获取团购详情信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取团购详情信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

}


