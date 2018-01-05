package com.gt.mall.controller.api.basic.phone;

import com.gt.api.bean.session.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dto.ErrorInfo;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneLoginDTO;
import com.gt.mall.param.phone.comment.PhoneMallCommentDTO;
import com.gt.mall.result.phone.comment.PhoneCommentListResult;
import com.gt.mall.result.phone.comment.PhoneCommentProductResult;
import com.gt.mall.result.phone.comment.PhoneCommentSuccessResult;
import com.gt.mall.service.web.basic.MallCommentGiveService;
import com.gt.mall.service.web.basic.MallCommentService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.EntityDtoConverter;
import com.gt.mall.utils.MallSessionUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * 评论相关接口（手机端）
 *
 * @author Administrator
 */
@Api( value = "phoneComment", description = "评论相关接口（手机端）", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
@Controller
@RequestMapping( "phoneComment/L6tgXlBFeK/" )
public class PhoneCommentController extends AuthorizeOrUcLoginController {

    public static final Logger logger = Logger.getLogger( PhoneCommentController.class );

    @Autowired
    private MallCommentService          mallCommentService;
    @Autowired
    private MallCommentGiveService      mallCommentGiveService;
    @Autowired
    private MallImageAssociativeService mallImageAssociativeService;
    @Autowired
    private MallPaySetService           mallPaySetService;
    @Autowired
    private MallOrderService            mallOrderService;

    @ApiOperation( value = "手机端查看评论列表的接口", notes = "我的评论", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "当前页数", paramType = "query", dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "commentList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneCommentListResult > collectList( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer curPage ) {
	try {
//	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    PhoneCommentListResult result = mallCommentService.myCommentList( member.getId(), loginDTO.getBusId(), curPage );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "查看评论列表的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "查看评论列表的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询我的评论失败" );
	}
    }

    @ApiOperation( value = "进入评价页面的接口", notes = "查询评论页面的商品信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "orderDetailId", value = "订单详情id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "toCommentProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneCommentProductResult > toCommentProduct( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer orderDetailId ) {
	try {
//	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    PhoneCommentProductResult result = mallCommentService.getCommentProduct( orderDetailId );

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "进入评价页面的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "进入评价页面的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询评论商品失败" );
	}
    }

    @ApiOperation( value = "保存评论接口", notes = "保存评论", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "imageUrls", value = "评论图片地址", paramType = "query", dataType = "String" ) )
    @ResponseBody
    @PostMapping( value = "saveComment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< Integer > saveComment( HttpServletRequest request, HttpServletResponse response, @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO,
		    @RequestBody @Valid @ModelAttribute PhoneMallCommentDTO mallCommentDTO, String imageUrls ) {
	try {
//	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断

	    Member member = MallSessionUtils.getLoginMember( request, loginDTO.getBusId() );

	    MallComment mallComment = new MallComment();
	    EntityDtoConverter converter = new EntityDtoConverter();
	    converter.entityConvertDto( mallCommentDTO, mallComment );

	    mallComment.setUserId( member.getId() );
	    //判断用户是否已经评论了，防止重复评论
	    MallComment isComment = mallCommentService.selectComment( mallComment );
	    if ( CommonUtil.isEmpty( isComment ) && CommonUtil.isEmpty( mallComment.getId() ) ) {

		Date giveTime = null; //可送礼时间
		Integer giveStatus = 0; //送礼状态
		//查询是否开启了评论送礼
		MallPaySet set = new MallPaySet();
		set.setUserId( loginDTO.getBusId() );
		MallPaySet payset = mallPaySetService.selectByUserId( set );
		if ( CommonUtil.isNotEmpty( payset ) ) {
		    if ( payset.getIsCommentGive().toString().equals( "1" ) ) {
			giveStatus = 1;
		    }
		}
		if ( giveStatus == 1 ) {
		    MallOrder order = mallOrderService.selectById( mallComment.getOrderId() );
		    giveTime = DateTimeKit.addDate( order.getUpdateTime(), Constants.ORDER_FINISH_RETURN_DAY );
		}
		mallComment.setGiveTime( giveTime );
		mallComment.setGiveStatus( giveStatus );

		MallComment comment = mallCommentService.addAppraise( imageUrls, mallComment, request );
		if ( CommonUtil.isNotEmpty( comment ) ) {
		    if ( giveStatus == 1 ) {
			int ret = new Date().compareTo( giveTime );
			if ( ret == 1 ) {
			    //评论送礼
			    try {
				mallCommentGiveService.commentGive( comment.getId(), request, comment.getUserId() );
				comment.setGiveStatus( 2 );
				mallCommentService.updateById( comment );
			    } catch ( Exception e ) {
				logger.error( "评论赠送商品异常：" + e.getMessage() );
				e.printStackTrace();
			    }
			}
		    }
		    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), comment.getId() );
		}
	    } else {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "您已经评论过了，无需再次评论", isComment.getId() );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "保存评论的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存评论的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "保存评论失败" );
	}
	return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存评论失败" );
    }

    @ApiOperation( value = "评论成功后的接口", notes = "评论成功后的接口查询", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    @ApiImplicitParams( @ApiImplicitParam( name = "id", value = "评论id,必传", paramType = "query", required = true, dataType = "int" ) )
    @ResponseBody
    @PostMapping( value = "commentSuccess", produces = MediaType.APPLICATION_JSON_UTF8_VALUE )
    public ServerResponse< PhoneCommentSuccessResult > commentSuccess( HttpServletRequest request, HttpServletResponse response,
		    @RequestBody @Valid @ModelAttribute PhoneLoginDTO loginDTO, Integer id ) {
	try {
	    /*loginDTO.setUcLogin( 1 );
	    userLogin( request, response, loginDTO );//授权或登陆，以及商家是否已过期的判断*/

	    PhoneCommentSuccessResult result = new PhoneCommentSuccessResult();
	    //查询商品评论
	    MallComment comment = mallCommentService.selectById( id );
	    //查询评论商品对象
	    PhoneCommentProductResult productResult = mallCommentService.getCommentProduct( comment.getOrderDetailId() );
	    result.setProductResult( productResult );
	    result.setMallComment( comment );

	    if ( CommonUtil.isNotEmpty( comment ) && comment.getIsUploadImage() == 1 ) {
		List< MallImageAssociative > imageList = mallImageAssociativeService.selectImageByAssId( null, 4, comment.getId() );
		result.setImageList( imageList );
	    }

	    return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
	} catch ( BusinessException e ) {
	    logger.error( "评论成功后的接口异常：" + e.getCode() + "---" + e.getMessage() );
	    if ( e.getCode() == ResponseEnums.NEED_LOGIN.getCode() ) {
		return ErrorInfo.createByErrorCodeMessage( e.getCode(), e.getMessage(), e.getData() );
	    }
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "评论成功后的接口异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorMessage( "查询评论信息失败" );
	}
    }

}
