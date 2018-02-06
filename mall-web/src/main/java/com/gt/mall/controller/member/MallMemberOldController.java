package com.gt.mall.controller.member;

import com.gt.api.bean.session.Member;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 商城的个人中心 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mMember" )
public class MallMemberOldController extends AuthorizeOrLoginController {

    @Autowired
    private MemberService memberService;

    /**
     * 跳转至个人中心的页面
     *
     * @param request
     * @param response
     *
     * @return
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( value = "/79B4DE7C/toUser" )
    public String toUser( HttpServletRequest request, HttpServletResponse response ) {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( request.getParameter( "uId" ) ) ) {
            userid = CommonUtil.toInteger( request.getParameter( "uId" ) );
        } else if ( CommonUtil.isNotEmpty( request.getParameter( "member_id" ) ) ) {
            int memberId = CommonUtil.toInteger( request.getParameter( "member_id" ) );
            Member member = memberService.findMemberById( memberId, null );
            userid = member.getBusid();
        }
        return "/my/center/" + userid;
    }

}
