package com.gt.mall.controller.api.basic.phone;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.Member;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallBusMessageMember;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.service.web.basic.MallBusMessageMemberService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * <p>
 * 商家消息模板提醒用户
 * </p>
 *
 * @author yangqian
 * @since 2017-12-28
 */

@Api( value = "phoneBusMessageMember", description = " 商家消息模板提醒接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "phoneBusMessageMember/L6tgXlBFeK/" )
public class PhoneBusMessageMemberController extends AuthorizeOrUcLoginController {

    @Autowired
    private MallBusMessageMemberService mallBusMessageMemberService;

    @ApiOperation( value = "商家是否授权", notes = "商家是否授权" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "busId", value = "商家id,必传", paramType = "query", required = true, dataType = "Integer" ) } )
    @ResponseBody
    @RequestMapping( value = "grant/{busId}", method = RequestMethod.GET )
    public ServerResponse grant( HttpServletRequest request, HttpServletResponse response, @PathVariable Integer busId ) {
	try {
	    Integer browser = CommonUtil.judgeBrowser( request );
	    if ( browser != 1 ) {//微信
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.GRAND_ERROR.getCode(), ResponseEnums.GRAND_ERROR.getDesc() );
	    }
	    PhoneLoginDTO loginDTO = new PhoneLoginDTO();
	    loginDTO.setBusId( busId );
	    loginDTO.setUrl( PropertiesUtil.getHomeUrl() + "phoneBusMessageMember/L6tgXlBFeK/grant/" + busId );
	    loginDTO.setBrowerType( CommonUtil.judgeBrowser( request ) );
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    //新增
	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );
	    Wrapper wrapper = new EntityWrapper<>();
	    wrapper.where( "is_delete =0 and member_id= {0}", member.getId() );
	    MallBusMessageMember mallBusMessageMember = mallBusMessageMemberService.selectOne( wrapper );
	    if ( mallBusMessageMember != null ) {
		mallBusMessageMember.setCreateTime( new Date() );
		mallBusMessageMember.setMemberId( member.getId() );
		mallBusMessageMember.setPublicId( member.getPublicId() );
		mallBusMessageMember.setNickName( member.getNickname() );
		mallBusMessageMember.setHeadImgUrl( member.getHeadimgurl() );
		mallBusMessageMemberService.updateById( mallBusMessageMember );
	    } else {
		mallBusMessageMember = new MallBusMessageMember();
		mallBusMessageMember.setCreateTime( new Date() );
		mallBusMessageMember.setMemberId( member.getId() );
		mallBusMessageMember.setPublicId( member.getPublicId() );
		mallBusMessageMember.setNickName( member.getNickname() );
		mallBusMessageMember.setHeadImgUrl( member.getHeadimgurl() );
		mallBusMessageMember.setBusId( member.getBusid() );
		mallBusMessageMemberService.insert( mallBusMessageMember );
	    }
	    return ServerResponse.createBySuccessCode();
	} catch ( BusinessException e ) {
	    logger.error( "商家授权异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "商家授权异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "商家授权失败" );
	}
    }
}
