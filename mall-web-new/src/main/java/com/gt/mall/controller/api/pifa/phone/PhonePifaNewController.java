package com.gt.mall.controller.api.pifa.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.controller.api.basic.phone.AuthorizeOrUcLoginController;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.pifa.PhoneAddPifaApplyDTO;
import com.gt.mall.service.inter.core.CoreService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.EntityDtoConverter;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 批发管理 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
@Api( value = "phoneWholesaler", description = "批发管理页面接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "/phoneWholesaler/L6tgXlBFeK/" )
public class PhonePifaNewController extends AuthorizeOrUcLoginController {

    @Autowired
    private MallPifaService   mallPifaService;
    @Autowired
    private MallPaySetService mallPaySetService;
    @Autowired
    private MallCommonService mallCommonService;
    @Autowired
    private CoreService       coreService;

    @ApiOperation( value = "获取批发商申请说明信息", notes = "获取批发商申请说明信息" )
    @ResponseBody
    @RequestMapping( value = "getPfApplyRemark", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > pfApplyRemark( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    coreService.payModel( loginDTO.getBusId(), CommonUtil.getAddedStyle( "7" ) );////判断活动是否已经过期

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    //	    userLogin( request, response, loginDTO );

	    MallPaySet set = mallPaySetService.selectByMember( member );
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getPfApplyRemark() ) ) {
		    result.put( "pfApplayRemark", set.getPfApplyRemark() );
		}
	    }
	} catch ( BusinessException be ) {
	    return ErrorInfo.createByErrorCodeMessage( be.getCode(), be.getMessage(), be.getData() );
	} catch ( Exception e ) {
	    logger.error( "获取批发商申请说明信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取批发商申请说明信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 添加批发商
     */
    @ApiOperation( value = "添加批发商", notes = "添加批发商" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "code", value = "验证码", paramType = "query", required = true, dataType = "String" ) } )
    @ResponseBody
    @RequestMapping( value = "addWholesaler", method = RequestMethod.POST )
    public ServerResponse< Map< String,Object > > addWholesaler( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, PhoneAddPifaApplyDTO pifaApplyDTO, String code ) {
	try {
	    //	    userLogin( request, response, loginDTO );

	    if ( CommonUtil.isEmpty( code ) ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "请输入验证码" );
	    } else {
		String jedCode = JedisUtil.get( Constants.REDIS_KEY + code );
		if ( CommonUtil.isEmpty( jedCode ) ) {
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "验证码超时或错误" );
		}
	    }

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    MallPifaApply pifaApply = new MallPifaApply();
	    EntityDtoConverter converter = new EntityDtoConverter();
	    converter.entityConvertDto( pifaApplyDTO, pifaApply );

	    pifaApply.setMemberId( member.getId() );
	    pifaApply.setBusUserId( member.getBusid() );
	    pifaApply.setCreateTime( new Date() );
	    int count = 0;
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
		    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "您的批发商申请已经通过，无需再次提交申请" );
		}
	    }
	    if ( count > 0 ) {
		JedisUtil.del( Constants.REDIS_KEY + code );//申请批发商成功，删除验证码
	    }

	} catch ( Exception e ) {
	    logger.error( "添加批发商异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "添加批发商异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

    /**
     * 获取短信验证码
     */
    @ApiOperation( value = "获取短信验证码", notes = "获取短信验证码" )
    @ResponseBody
    @RequestMapping( value = "sendMsg", method = RequestMethod.POST )
    public ServerResponse sendMsg( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "mobile", value = "手机号码", required = true ) @RequestParam String mobile,
		    @ApiParam( name = "busId", value = "商家ID", required = true ) @RequestParam Integer busId,
		    @ApiParam( name = "areaCode", value = "手机区号" ) @RequestParam String areaCode ) {
	try {
	    Member member = MallSessionUtils.getLoginMember( request, busId );
	    String no = CommonUtil.getPhoneCode();
	    JedisUtil.set( Constants.REDIS_KEY + no, no, 5 * 60 );
	    logger.info( "批发商短信验证码：" + no );

	    boolean result = mallCommonService.getValCode( areaCode, mobile, member.getBusid(), no, Constants.APPLY_PIFA_CODE_MODEL_ID );
	    if ( !result ) {
		return ServerResponse.createBySuccessCodeMessage( ResponseEnums.ERROR.getCode(), "发送短信验证码异常" );
	    }
	} catch ( Exception e ) {
	    logger.error( "获取短信验证码异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取短信验证码异常" );
	}
	return ServerResponse.createBySuccessCode();
    }

}


