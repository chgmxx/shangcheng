package com.gt.mall.controller.groupbuy;

import com.gt.api.bean.session.Member;
import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>
 * 团购表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mGroupBuy" )
public class MallGroupBuyOldController extends AuthorizeOrLoginController {

    @Autowired
    private MallStoreService mallStoreService;

    /**
     * 获取店铺下所有的团购（手机）
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/groupbuyall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String groupbuyall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        if ( userid <= 0 ) {
            MallStore mallStore = mallStoreService.selectById( shopid );
            userid = mallStore.getStoUserId();
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/classify/" + shopid + "/" + userid + "/1/k=k";
    }

    /**
     * 我要参团，团购详情（手机）
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( "{id}/{joinId}/79B4DE7C/groupBuyDetail" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String groupBuyDetail( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int joinId,
        @RequestParam Map< String,Object > params ) throws Exception {
        int userid = 0;
        if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
            userid = CommonUtil.toInteger( params.get( "uId" ) );
        }
        Member member = MallSessionUtils.getLoginMember( request, userid );
        String memberId = "";
        if ( CommonUtil.isNotEmpty( params.get( "buyerUserId" ) ) ) {
            memberId = CommonUtil.toString( params.get( "buyerUserId" ) );
        } else if ( CommonUtil.isNotEmpty( member ) ) {
            memberId = member.getId().toString();
        }
        return "redirect:" + PropertiesUtil.getPhoneWebHomeUrl() + "/groupbuy/detail/" + userid + "/" + id + "/" + joinId + "/" + memberId;
    }

}
