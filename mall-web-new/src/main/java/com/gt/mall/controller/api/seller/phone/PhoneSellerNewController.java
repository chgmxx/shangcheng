package com.gt.mall.controller.api.seller.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerMallset;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.entity.seller.MallSellerWithdraw;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.sellers.PhoneAddMallSetDTO;
import com.gt.mall.param.phone.sellers.PhoneAddSellersDTO;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.pifa.MallPifaApplyService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import com.gt.mall.service.web.seller.MallSellerOrderService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.seller.MallSellerWithdrawService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;

/**
 * 超级销售员页面相关接口
 * User : yangqian
 * Date : 2017/10/19 0019
 * Time : 17:10
 */
@Api( value = "phoneSellers", description = "超级销售员页面相关接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneSellers/L6tgXlBFeK/" )
public class PhoneSellerNewController extends AuthorizeOrUcLoginController {

    @Autowired
    private WxPublicUserService       wxPublicUserService;
    @Autowired
    private MemberService             memberService;
    @Autowired
    private MallSellerService         mallSellerService;
    @Autowired
    private MallPageService           mallPageService;
    @Autowired
    private MallPaySetService         mallPaySetService;
    @Autowired
    private MallCommonService         mallCommonService;
    @Autowired
    private MallSellerMallsetService  mallSellerMallSetService;
    @Autowired
    private MallProductService        mallProductService;
    @Autowired
    private MallPifaApplyService      mallPifaApplyService;
    @Autowired
    private MallSellerOrderService    mallSellerOrderService;
    @Autowired
    private MallOrderService          mallOrderService;
    @Autowired
    private MallSellerWithdrawService mallSellerWithdrawService;

    @ApiOperation( value = "判断是否可以申请超级销售员", notes = "判断是否可以申请超级销售员", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "isApplySeller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > isApplySeller( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	try {
	    Map< String,Object > resultMap = new HashMap<>();
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    boolean isError = false;
	    boolean isIndex = false;
	    String errorMsg = "";
	    MallPaySet set = mallPaySetService.selectByMember( member );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( set.getIsSeller().toString().equals( "1" ) ) {//开启销售员
		    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );

		    if ( set.getIsCheckSeller().toString().equals( "1" ) ) {//开启销售员审核
			if ( CommonUtil.isNotEmpty( seller ) ) {
			    if ( seller.getCheckStatus() == 1 && seller.getIsStartUse() == 1 ) {//审核通过并且已经开启
				isIndex = true;
			    } else if ( seller.getCheckStatus() == 0 ) {//审核中
				isError = true;
				errorMsg = "您的申请已经提交，请等候审核！";
			    } else if ( seller.getCheckStatus() == -1 ) {//审核中
				isError = true;
				errorMsg = "您的申请审核不通过！";
			    } else if ( seller.getIsStartUse() != 1 ) {
				isError = true;
				errorMsg = "您的销售员暂不能使用";
			    }
			}
		    } else {
			if ( CommonUtil.isNotEmpty( seller ) ) {
			    if ( seller.getIsStartUse() == 1 ) {//开启销售员
				isIndex = true;

			    } else {//暂停销售员
				isError = true;
				errorMsg = "您的销售员暂不能使用";
			    }
			    if ( seller.getCheckStatus() != 1 ) {
				MallSeller mallSeller = new MallSeller();
				mallSeller.setCheckStatus( 1 );
				mallSeller.setId( seller.getId() );
				mallSellerService.updateSeller( mallSeller );
			    }
			} else {
			    seller = new MallSeller();
			    seller.setMemberId( member.getId() );
			    seller.setBusUserId( loginDTO.getBusId() );
			    seller.setApplyTime( new Date() );
			    seller.setAddTime( new Date() );
			    seller.setCheckStatus( 1 );
			    if ( CommonUtil.isEmpty( member.getNickname() ) ) {
				seller.setUserName( member.getNickname() );
			    }
			    if ( CommonUtil.isEmpty( member.getPhone() ) ) {
				seller.setTelephone( member.getPhone() );
			    }
			    int count = mallSellerService.insertSelective( seller, member );//添加超级销售员
			    if ( count > 0 ) {
				isIndex = true;
			    }
			}
		    }
		} else {//没有开启销售员的页面
		    isError = true;
		    errorMsg = "商家还没开启销售员的功能，敬请期待";
		}
	    } else {//没有开启销售员的页面
		isError = true;
		errorMsg = "商家还没开启销售员的功能，敬请期待";
	    }

	    if ( isError ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), errorMsg );
	    }
	    if ( isIndex ) {
		resultMap.put( "index", 1 );//跳转至主页
		return ServerResponse.createBySuccessCodeData( ResponseEnums.ERROR.getCode(), resultMap, false );
	    }
	    resultMap.put( "isApply", 1 );//可以申请
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), resultMap, false );
	} catch ( Exception e ) {
	    logger.error( "判断是否可以申请超级销售员异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "判断是否可以申请超级销售员失败" );
	}
    }

    @ApiOperation( value = "添加超级销售员接口", notes = "添加超级销售员", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "code", value = "验证码,必传", paramType = "query", required = true, dataType = "String" ) )
    @ResponseBody
    @PostMapping( value = "addSellers", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > addSellers( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneAddSellersDTO params, PhoneLoginDTO loginDTO, String code ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    String msg = "";
	    boolean result = false;
	    int count = 0;
	    userLogin( request, response, loginDTO );
	    if ( CommonUtil.isEmpty( params.getUserName() ) ) {
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "姓名不能为空" );
	    } else if ( CommonUtil.isEmpty( params.getTelephone() ) ) {
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "手机号码不能为空" );
	    } else if ( CommonUtil.isEmpty( code ) ) {
		throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "验证码不能为空" );
	    }

	    String jedisCode = JedisUtil.get( Constants.REDIS_KEY + code );
	    if ( CommonUtil.isEmpty( jedisCode ) || !jedisCode.equals( code ) ) {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "验证码错误或超时" );
	    }

	    MallSeller seller = new MallSeller();
	    seller.setUserName( CommonUtil.urlEncode( params.getUserName() ) );
	    seller.setCompanyName( CommonUtil.urlEncode( params.getCompanyName() ) );
	    seller.setTelephone( params.getTelephone() );
	    seller.setRemark( CommonUtil.urlEncode( params.getRemark() ) );
	    seller.setMemberId( member.getId() );
	    seller.setBusUserId( member.getBusid() );
	    seller.setApplyTime( new Date() );
	    seller.setAddTime( new Date() );
	    seller.setMemberId( params.getMemberId() );

	    //查询是否已申请超级销售员
	    MallSeller isMallSeller = mallSellerService.selectMallSeller( seller );
	    if ( CommonUtil.isEmpty( isMallSeller ) ) {
		if ( CommonUtil.isEmpty( member.getNickname() ) ) {
		    seller.setUserName( member.getNickname() );
		}
		if ( CommonUtil.isEmpty( member.getPhone() ) ) {
		    seller.setTelephone( member.getPhone() );
		}
		count = mallSellerService.insertSelective( seller, member );//添加超级销售员
	    } else {
		if ( !isMallSeller.getCheckStatus().toString().equals( "0" ) ) {
		    seller.setId( isMallSeller.getId() );
		    seller.setCheckStatus( 0 );
		    count = mallSellerService.updateSeller( seller );
		} else {
		    msg = "您申请的超级销售员正在审核中，无需再次提交申请";
		}
	    }
	    if ( count > 0 ) {
		result = true;
		JedisUtil.del( code );//申请超级销售员成功，删除验证码
	    }

	    if ( !result ) {
		return ServerResponse.createByErrorMessage( msg );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "添加超级销售员接口异常：" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "添加超级销售员接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "添加超级销售员失败" );
	}
	return ServerResponse.createBySuccessCode();
    }

    /**
     * 获取短信验证码
     */
    @ApiOperation( value = "获取短信验证码", notes = "获取短信验证码" )
    @ResponseBody
    @RequestMapping( value = "getValCode", method = RequestMethod.POST )
    public ServerResponse getValCode( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "mobile", value = "手机号码", required = true ) @RequestParam String mobile,
		    @ApiParam( name = "busId", value = "商家ID", required = true ) @RequestParam Integer busId ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, busId );
	    String no = CommonUtil.getPhoneCode();
	    JedisUtil.set( Constants.REDIS_KEY + no, no, 5 * 60 );

	    String content = "您申请成为超级销售员的验证码为:" + no + "，5分钟内有效。";

	    boolean result = mallCommonService.getValCode( mobile, member.getBusid(), content, null );
	    if ( !result ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "发送短信验证码异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取短信验证码异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取短信验证码异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 超级销售员主页
     */
    @ApiOperation( value = "获取超级销售员主页接口", notes = "获取超级销售员主页信息" )
    @ResponseBody
    @RequestMapping( value = "sellerIndex", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > sellerIndex( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    boolean isSeller = mallSellerService.isSeller( member.getId() );//判断商户是否是销售员
	    if ( !isSeller ) {
		return ServerResponse.createByErrorMessage( "该用户不是销售员！" );
	    }

	    //判断用户是否已经对商城进行设置
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    if ( CommonUtil.isNotEmpty( mallSet ) ) {
		result.put( "isMallSet", 1 );//已设置
	    } else {
		result.put( "isMallSet", 0 );
	    }

	    MallPaySet set = new MallPaySet();
	    set.setUserId( member.getBusid() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }
	    Map< String,Object > params = new HashMap<>();
	    params.put( "refereesMemberId", member.getId() );
	    params.put( "isCheck", isCheck );
	    int sellerCount = mallSellerService.selectCountMyClient( params );//查询客户的个数
	    int sellerOrderCount = mallSellerService.selectCountClientOrder( params );//查询客户订单的个数

	    //查询销售员的信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    seller = mallSellerService.getSellerTwoCode( seller, member, CommonUtil.judgeBrowser( request ) );//获取二维码
	    seller = mallSellerService.mergeData( seller, member );
	    result.put( "seller", seller );
	    result.put( "sellerCount", sellerCount );//查询客户的个数
	    result.put( "sellerOrderCount", sellerOrderCount );//查询客户订单的个数
	    result.put( "member", member );

//	    mallPageService.getCustomer( request, member.getBusid() );
	} catch ( Exception e ) {
	    logger.error( "获取超级销售员主页信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取超级销售员主页信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 进入统计收益的页面
     *
     * @param type 1 统计销售佣金  2统计销售积分 3统计销售额
     */
    @ApiOperation( value = "统计收益", notes = "统计收益（1销售佣金、2销售积分、3销售额）" )
    @ResponseBody
    @RequestMapping( value = "{type}/totalIncome", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > totalIncome( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    @PathVariable int type ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    params.put( "type", type );
	    params = mallOrderService.getMemberParams( member, params );
	    if ( params.containsKey( "memberId" ) ) {
		params.put( "saleMemeberId", params.get( "memberId" ) );
	    }
	    //查询佣金，积分和销售金额排行榜
	    List< Map< String,Object > > incomeList = mallSellerService.selectTotalIncome( params );

	    result.put( "incomeList", incomeList );

	} catch ( Exception e ) {
	    logger.error( "获取自选商品列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取自选商品列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /**
     * 获取自选商品列表
     */
    @ApiOperation( value = "获取自选商品列表", notes = "获取自选商品列表" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "proName", value = "商品名称", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "groupId", value = "分类ID", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "findProduct", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > findProduct( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    String proName, Integer groupId, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    userLogin( request, response, loginDTO );
	    params.put( "uId", member.getBusid() );
	    params.put( "groupId", groupId );
	    params.put( "curPage", curPage );
	    if ( CommonUtil.isNotEmpty( proName ) ) {
		proName = new String( proName.getBytes( "iso8859-1" ), "utf-8" );
		params.put( "proName", proName );
	    }
	    double discount = mallProductService.getMemberDiscount( "1", member );//商品折扣
	    result.put( "discount", discount );

	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    boolean isPifa = mallPifaApplyService.isPifa( member );
	    params.put( "isFindSeller", 1 );
	    //查询已设置佣金的商品
	    PageUtil page = mallSellerMallSetService.selectProductBySaleMember( mallSet, params, "1", 0, discount, isPifa );
	    result.put( "page", page );
	   /* //获取店铺所有的分类
	    List classList = mallPageService.storeList( null, 1, loginDTO.getBusId() );
	    result.put( "classList", classList );*/

	} catch ( Exception e ) {
	    logger.error( "获取自选商品列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取自选商品列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
    }

    /*
    * 自选商品列表（返回选中商品列表）
    * */
    @ApiOperation( value = "获取选择的商品列表", notes = "获取选择的商品列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "findIds", value = "选中商品id集合", paramType = "query", required = true, dataType = "String" ) } )
    @ResponseBody
    @PostMapping( value = "selectedProducts", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse getProductDetail( HttpServletRequest request, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, String findIds ) {
	List< Map< String,Object > > selectProList = null;
	try {
	    Map< String,Object > params = new HashMap<>();
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    if ( CommonUtil.isNotEmpty( findIds ) ) {
		params.put( "mallSetId", mallSet.getId() );
		params.put( "saleMemberId", mallSet.getSaleMemberId() );
		//查询销售员已选择的商品
		List< Map< String,Object > > sellerProList = mallSellerMallSetService.selectProductBySaleMember( params );

		String[] spli = findIds.split( "," );
		params.put( "findIds", spli );
		//查询销售员当前选择的商品
		selectProList = mallSellerMallSetService.selectProductByBusUserId( params );
		List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();
		if ( sellerProList != null && sellerProList.size() > 0 && selectProList != null && selectProList.size() > 0 ) {
		    for ( Map< String,Object > selectMap : selectProList ) {
			boolean isSave = true;
			for ( Map< String,Object > sellerMap : sellerProList ) {
			    if ( selectMap.get( "id" ).toString().equals( sellerMap.get( "id" ).toString() ) ) {
				isSave = false;
			    }
			}
			if ( isSave ) {
			    proList.add( selectMap );
			}
		    }
		    selectProList = proList;
		}
	    }
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), selectProList, true );

	} catch ( Exception e ) {
	    logger.error( "获取选择的商品列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取选择的商品列表失败" );
	}

    }

    @ApiOperation( value = "获取商城设置信息", notes = "获取商城设置信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "type", value = "1基本信息 2自选商品", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "mallSetInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse mallSetInfo( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer type ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    if ( CommonUtil.isEmpty( type ) ) {
		type = 1;
	    }
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    if ( type == 2 && CommonUtil.isNotEmpty( mallSet ) ) {
		Map< String,Object > params = new HashMap<>();
		params.put( "mallSetId", mallSet.getId() );
		params.put( "saleMemberId", mallSet.getSaleMemberId() );
		//查询销售员已选择的商品
		List< Map< String,Object > > sellerProList = mallSellerMallSetService.selectProductBySaleMember( params );
		result.put( "sellerProList", sellerProList );

	    }
	    result.put( "mallSet", mallSet );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( Exception e ) {
	    logger.error( "获取商城设置信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取商城设置信息失败" );
	}

    }

    @ApiOperation( value = "保存商城设置", notes = "保存商城设置", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "type", value = "1基本信息 2自选商品", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "addMallSet", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse addMallSet( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    PhoneAddMallSetDTO mallSetDTO, Integer type ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    result = mallSellerMallSetService.newSaveSeller( member, mallSetDTO, type );
	    boolean flag = (boolean) result.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorMessage( result.get( "msg" ).toString() );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "保存商城设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存商城设置异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "保存商城设置失败" );
	}
    }

    @ApiOperation( value = "是否开启自选", notes = "是否开启自选", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "id", value = "商城设置ID", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "status", value = "0关闭 1开始", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "openOptional", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse openOptional( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer id,
		    Integer status ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );

	    MallSellerMallset sellerMallset = new MallSellerMallset();
	    sellerMallset.setId( id );
	    sellerMallset.setIsOpenOptional( status );
	    boolean flag = mallSellerMallSetService.updateById( sellerMallset );
	    if ( !flag ) {
		return ServerResponse.createByErrorMessage( "开启自选设置失败" );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( Exception e ) {
	    logger.error( "开启自选设置失败异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "开启自选设置失败" );
	}
    }

    @ApiOperation( value = "删除已添加商品", notes = "删除已添加商品", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "id", value = "商品ID", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "deleteMallPro", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse deleteMallPro( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    userLogin( request, response, loginDTO );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "id", id );
	    result = mallSellerMallSetService.deleteSellerProduct( params );
	    boolean flag = (boolean) result.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorMessage( result.get( "msg" ).toString() );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( Exception e ) {
	    logger.error( "删除已添加商品异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "删除已添加商品失败" );
	}

    }

    @ApiOperation( value = "获取我的商城信息", notes = "获取我的商城信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "{saleMemberId}/mallIndex", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse mallIndex( HttpServletRequest request, HttpServletResponse response, @PathVariable int saleMemberId,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );

	    //查询商城设置
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( saleMemberId );
	    result.put( "mallSet", mallSet );

	    //分享出来的链接，保存销售员在缓存
	    mallSellerService.setSaleMemberIdByRedis( member, saleMemberId, request, mallSet.getBusUserId() );

	    saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, saleMemberId, request, mallSet.getBusUserId() );

	    //查询销售员信息
	    MallSeller mallSeller = mallSellerService.selectSellerByMemberId( saleMemberId );
	    if ( CommonUtil.isNotEmpty( mallSeller ) ) {
		result.put( "mallSeller", mallSeller );
	    }
	    if ( saleMemberId > 0 && CommonUtil.isNotEmpty( member ) ) {//分享的用户 判断是否是销售员
		mallSellerService.shareSellerIsSale( member, saleMemberId, mallSeller );
	    }

	    boolean isSeller = mallSellerService.isSeller( saleMemberId );
	    if ( !isSeller ) {
		return ServerResponse.createByErrorMessage( "该用户不是销售员！" );
	    }

	    //查询销售员选择的商品
	    Map< String,Object > params = new HashMap<>();
	    List< Map< String,Object > > productList = mallSellerMallSetService.selectProductByMallIndex( member, params, mallSet );
	    result.put( "productList", productList );

	    Map< String,Object > footerMenuMap = mallPaySetService.getFooterMenu( mallSet.getBusUserId() );//查询商城底部菜单
	    result.put( "footerMenuMap", footerMenuMap );

	   /* mallPageService.getCustomer( request, mallSet.getBusUserId() );*/

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( Exception e ) {
	    logger.error( "获取我的商城信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取我的商城信息失败" );
	}

    }

    @ApiOperation( value = "获取销售员排行榜接口", notes = "获取销售员排行榜", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "type", value = "1 周榜 2月榜 3年榜 4总榜", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "saleRank", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > saleRank( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer type, Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );
	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    if ( CommonUtil.isNotEmpty( seller ) && CommonUtil.isNotEmpty( member ) ) {
		Map< String,Object > params = new HashMap<>();
		params.put( "curPage", curPage );
		if ( type == 1 ) {
		    params.put( "startTime", DateTimeKit.getDateTime() );
		    params.put( "endTime", DateTimeKit.getDateTime( DateTimeKit.addDays( -7 ) ) );
		} else if ( type == 2 ) {
		    params.put( "startTime", DateTimeKit.getDateTime() );
		    params.put( "endTime", DateTimeKit.getDateTime( DateTimeKit.addMonths( -1 ) ) );
		} else if ( type == 3 ) {
		    params.put( "startTime", DateTimeKit.getDateTime() );
		    params.put( "endTime", DateTimeKit.getDateTime( DateTimeKit.addMonths( -12 ) ) );

		}
		PageUtil rankList = mallSellerOrderService.selectSellerByBusUserId( params );
		result.put( "page", rankList );
	    }
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
	} catch ( Exception e ) {
	    logger.error( "获取销售员排行榜异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取销售员排行榜列表失败" );
	}

    }

    @ApiOperation( value = "获取我的客户列表接口", notes = "获取我的客户列表接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "clientList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > clientList( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer curPage ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );
	    member = memberService.findMemberById( member.getId(), member );
	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );

	    //查询我的客户列表
	    Map< String,Object > param = new HashMap< String,Object >();
	    param.put( "url", "/phoneSellers/L6tgXlBFeK/clientList.do" );
	    param.put( "fromType", 1 );

	    MallPaySet set = new MallPaySet();
	    set.setUserId( member.getBusid() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }
	    param.put( "curPage", curPage );
	    param.put( "isCheck", isCheck );
	    param = mallOrderService.getMemberParams( member, param );

	    //通过商家id查询销售员排名
	    Map< String,Object > resultMap = mallSellerService.selectSellerByBusUserId( param, 2, member );
	    if ( CommonUtil.isNotEmpty( resultMap.get( "page" ) ) ) {
		result.put( "page", resultMap.get( "page" ) );
	    }
	    result.put( "seller", seller );//销售员信息
	    result.put( "member", member );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
	} catch ( Exception e ) {
	    logger.error( "获取我的客户列表接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取我的客户列表接口失败" );
	}

    }

    @ApiOperation( value = "获取客户订单接口", notes = "获取客户订单接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "status", value = "为空查询全部 1待付款 2已付款 4已完成", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "custId", value = "客户ID", paramType = "query", required = false, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "clientOrder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > clientOrder( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    Integer status, Integer custId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    MallPaySet set = new MallPaySet();
	    set.setUserId( member.getBusid() );
	    MallPaySet payset = mallPaySetService.selectByUserId( set );
	    int isCheck = -1;
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( CommonUtil.isNotEmpty( payset.getIsCheckSeller() ) ) {
		    if ( payset.getIsCheckSeller().toString().equals( "1" ) ) {
			isCheck = 1;
		    }
		}
	    }

	    Map< String,Object > params = new HashMap<>();
	    params.put( "status", status );
	    params.put( "custId", custId );
	    params.put( "isCheck", isCheck );
	    params.put( "fromType", 1 );
	    params = mallOrderService.getMemberParams( member, params );

	    //查询客户订单
	    List< Map< String,Object > > sellerOrderList = mallSellerOrderService.selectOrderByClientId( params );
	    result.put( "sellerOrderList", sellerOrderList );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
	} catch ( Exception e ) {
	    logger.error( "获取客户订单接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取客户订单接口列表失败" );
	}

    }

    @ApiOperation( value = "获取我的二维码接口", notes = "获取我的二维码", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "myTwoCode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > myTwoCode( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );
	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    seller = mallSellerService.getSellerTwoCode( seller, member, CommonUtil.judgeBrowser( request ) );//获取二维码

	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    if ( CommonUtil.isNotEmpty( seller ) ) {
		result.put( "qrCodePath", seller.getQrCodePath() );//二维码
		result.put( "userName", seller.getUserName() );//销售员姓名
	    }
	    if ( CommonUtil.isNotEmpty( mallSet ) ) {
		result.put( "mallName", mallSet.getMallName() );//商城名称
		result.put( "mallIntroducation", mallSet.getMallIntroducation() );//商城简介
		result.put( "mallHeadPath", mallSet.getMallHeadPath() );//商城头像地址
	    }
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( Exception e ) {
	    logger.error( "获取我的二维码接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取我的二维码接口失败" );
	}
    }

    @ApiOperation( value = "获取推广海报信息", notes = "获取推广海报信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "promotion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > promotion( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    //查询销售员信息
	   /* Map< String,Object > sellerMap = mallSellerService.selectSellerBySaleId( member.getId() );*/
	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( member.getId() );
	    if ( CommonUtil.isNotEmpty( seller ) ) {
		result.put( "qrCodePath", seller.getQrCodePath() );//二维码
		result.put( "headImagePath", seller.getHeadImagePath() );//销售员的用户头像地址
		result.put( "userName", seller.getUserName() );//销售员姓名
	    }
	    if ( CommonUtil.isNotEmpty( mallSet ) ) {
		result.put( "mallName", mallSet.getMallName() );//商城名称
		result.put( "mallIntroducation", mallSet.getMallIntroducation() );//商城简介
		result.put( "mallHeadPath", mallSet.getMallHeadPath() );//商城头像地址
	    }

	    String imageUrl = mallSellerService.createTempImage( member, seller, CommonUtil.judgeBrowser( request ) );
	    if ( CommonUtil.isNotEmpty( imageUrl ) ) {
		imageUrl = PropertiesUtil.getResourceUrl() + imageUrl;
		imageUrl = URLConnectionDownloader.isConnect( imageUrl );
		if ( CommonUtil.isNotEmpty( imageUrl ) ) {
		    result.put( "imageUrl", imageUrl );
		}
	    }
	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( Exception e ) {
	    logger.error( "获取推广海报信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取推广海报信息失败" );
	}
    }

    @ApiOperation( value = "获取我的提现信息", notes = "获取推广海报信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "myWithdrawal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > myWithdrawal( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    seller = mallSellerService.mergeData( seller, member );
	    result.put( "seller", seller );

	    //查询提现条件
	    MallSellerSet sellerSet = mallSellerService.selectByBusUserId( seller.getBusUserId() );
	    if ( CommonUtil.isNotEmpty( sellerSet ) ) {
		result.put( "sellerSet", sellerSet );
	    }

	    //查询销售员的提现记录
	    List< MallSellerWithdraw > withdrawList = mallSellerWithdrawService.selectBySaleMemberId( member );
	    result.put( "withdrawList", withdrawList );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( Exception e ) {
	    logger.error( "获取我的提现信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取我的提现信息失败" );
	}
    }

    @ApiOperation( value = "申请提现接口", notes = "申请提现接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "withdrawMoney", value = "提现金额", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "addWithdrawal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Map< String,Object > > addWithdrawal( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer withdrawMoney ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "withdraw", "{withdrawMoney: " + withdrawMoney + "}" );
	    result = mallSellerWithdrawService.saveWithdraw( member.getId(), params, 0 );
	    boolean flag = (boolean) result.get( "flag" );
	    if ( !flag ) {
		return ServerResponse.createByErrorMessage( result.get( "msg" ).toString() );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( Exception e ) {
	    logger.error( "申请提现接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "申请提现接口失败" );
	}
    }

    @ApiOperation( value = "获取佣金明细接口", notes = "获取佣金明细接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "type", value = "为空查询全部 1待完成 2已完成 3无效", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "withdrawalDetail", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse withdrawalDetail( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer type ) {
	List< Map< String,Object > > incomeList = null;
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "types", type );
	    params.put( "type", 1 );
	    params = mallOrderService.getMemberParams( member, params );
	    if ( params.containsKey( "memberId" ) ) {
		params.put( "saleMemeberId", params.get( "memberId" ) );
	    }
	    //查询用户的提现明细
	    incomeList = mallSellerService.selectTotalIncome( params );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), incomeList, false );
	} catch ( Exception e ) {
	    logger.error( "获取佣金明细接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取佣金明细接口失败" );
	}
    }

    @ApiOperation( value = "获取销售规则信息接口", notes = "获取销售规则信息接口", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ResponseBody
    @PostMapping( value = "saleRules", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse saleRules( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    userLogin( request, response, loginDTO );

	    //查询销售员信息
	    MallSeller seller = mallSellerService.selectSellerByMemberId( member.getId() );
	    result.put( "seller", seller );

	    MallSellerSet sellerSet = mallSellerService.selectByBusUserId( seller.getBusUserId() );
	    result.put( "sellerSet", sellerSet );

	    result.put( "member", member );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
	} catch ( Exception e ) {
	    logger.error( "获取销售规则信息接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取销售规则信息接口失败" );
	}
    }

    @ApiOperation( value = "获取商品分享信息", notes = "获取商品分享信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( { @ApiImplicitParam( name = "saleMemberId", value = "销售员ID", paramType = "query", required = true, dataType = "int" ),
		    @ApiImplicitParam( name = "productId", value = "商品ID", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @PostMapping( value = "shareSeller", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse shareSeller( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer saleMemberId,
		    Integer productId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );

	    MallSellerMallset mallSet = mallSellerMallSetService.selectByMemberId( saleMemberId );

	    //分享出来的链接，保存销售员在缓存
	    mallSellerService.setSaleMemberIdByRedis( member, saleMemberId, request, loginDTO.getBusId() );

	    saleMemberId = mallSellerService.getSaleMemberIdByRedis( member, saleMemberId, request, loginDTO.getBusId() );

	    //查询销售员信息
	    MallSeller mallSeller = mallSellerService.selectSellerByMemberId( saleMemberId );
	    Member sellerMember = memberService.findMemberById( mallSeller.getMemberId(), member );//查询销售员的用户信息
	    request.setAttribute( "sellerMember", sellerMember );

	    if ( saleMemberId > 0 && CommonUtil.isNotEmpty( mallSeller ) ) {//分享的用户 判断是否是销售员
		mallSellerService.shareSellerIsSale( member, saleMemberId, mallSeller );
	    }

	    boolean isSeller = mallSellerService.isSeller( saleMemberId );
	    if ( !isSeller ) {
		return ServerResponse.createByErrorMessage( "该用户不是销售员！" );
	    }

	    //查询商品信息
	    Map< String,Object > proMap = mallSellerMallSetService.selectSellerByProId( productId, mallSet );
	    if ( CommonUtil.isNotEmpty( proMap ) ) {
		double price = CommonUtil.toDouble( proMap.get( "pro_price" ) );
		if ( CommonUtil.isNotEmpty( proMap.get( "inv_price" ) ) ) {
		    double invPrice = CommonUtil.toDouble( proMap.get( "inv_price" ) );
		    if ( invPrice > 0 ) {
			price = invPrice;
		    }
		}
		proMap.put( "price", price );
		proMap = mallSellerMallSetService.getSellerProductPrice( proMap );
	    }
	    result.put( "productMap", proMap );

	    result.put( "mallSeller", mallSeller );
	    result.put( "member", member );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, true );
	} catch ( Exception e ) {
	    logger.error( "获取商品分享信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "获取商品分享信息失败" );
	}
    }
}