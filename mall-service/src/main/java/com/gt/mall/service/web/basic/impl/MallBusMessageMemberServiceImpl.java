package com.gt.mall.service.web.basic.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallBusMessageMemberDAO;
import com.gt.mall.entity.basic.MallBusMessageMember;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.html.MallHtmlFrom;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallBusMessageMemberService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.order.MallOrderDetailService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商家消息模板提醒用户表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-12-28
 */
@Service
public class MallBusMessageMemberServiceImpl extends BaseServiceImpl< MallBusMessageMemberDAO,MallBusMessageMember > implements MallBusMessageMemberService {

    @Autowired
    private SmsService                  smsService;
    @Autowired
    private MallOrderDetailService      mallOrderDetailService;
    @Autowired
    private MallOrderService            mallOrderService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private MallPaySetService           mallPaySetService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private MallBusMessageMemberService mallBusMessageMemberService;
    @Autowired
    private MallBusMessageMemberDAO     mallBusMessageMemberDAO;
    @Autowired
    private MallStoreService            mallStoreService;
    @Autowired
    private MallPageService             mallPageService;

    /**
     * 获取选中的商家模板ID
     *
     * @param busId 商家ID
     * @param title 模板名称
     *
     * @return
     */
    public Integer isOpenPaySetByBusMessage( Integer busId, String title ) {
	Integer result = 0;
	MallPaySet paySet = new MallPaySet();
	paySet.setUserId( busId );
	MallPaySet set = mallPaySetService.selectByUserId( paySet );
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getBusMessageJson() ) ) {
		List< Map > busMsgArr = JSONArray.fromObject( set.getBusMessageJson() );
		if ( busMsgArr != null ) {
		    for ( Map obj : busMsgArr ) {
			if ( obj.get( "title" ).equals( title ) ) {
			    if ( CommonUtil.isNotEmpty( obj.get( "id" ) ) ) {
				result = CommonUtil.toInteger( obj.get( "id" ) );
			    }
			}
		    }
		}
	    }
	}
	return result;
    }

    @Override
    public List< MallBusMessageMember > selectByBusId( Integer busId ) {
	Wrapper< MallBusMessageMember > wrapper = new EntityWrapper<>();
	wrapper.where( "bus_id = {0}", busId );
	return mallBusMessageMemberDAO.selectList( wrapper );
    }

    @Override
    public void buyerPaySuccess( MallOrder order, BusUser busUser, Integer type, String telePhone ) {
	List< MallOrderDetail > orderDetails = mallOrderDetailService.getOrderDetailList( order.getId() );
	String proName = "";
	if ( orderDetails != null && orderDetails.size() > 0 ) {
	    for ( MallOrderDetail detail : orderDetails ) {
		if ( !proName.equals( "" ) ) {proName += ",";}
		proName += detail.getDetProName();
	    }
	    String messages = "用户【" + busUser.getName() + "】成功购买您的商品【" + proName + "】，请尽快登录商城后台发货";
	    if ( type == 0 || type == 1 ) {//短信
		if ( CommonUtil.isNotEmpty( telePhone ) ) {
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( telePhone );
		    oldApiSms.setCompany( busUser.getMerchant_name() );
		    oldApiSms.setBusId( busUser.getId() );
		    oldApiSms.setModel( Constants.SMS_MODEL );
		    oldApiSms.setContent( CommonUtil.format( messages ) );
		    try {
			smsService.sendSmsOld( oldApiSms );
		    } catch ( Exception e ) {
			e.printStackTrace();
			logger.error( "通知商家短信消息异常：" + e.getMessage() );
		    }
		}
	    }
	    if ( type == 0 || type == 2 ) {//模板
		Integer id = isOpenPaySetByBusMessage( busUser.getId(), Constants.BUS_TEMPLATE_LIST[0] );
		if ( id > 0 ) {
		    List< MallBusMessageMember > busMessageMemberList = selectByBusId( busUser.getId() );//商家
		    if ( busMessageMemberList != null && busMessageMemberList.size() > 0 ) {
			Member member = memberService.findMemberById( order.getBuyerUserId(), null );//买家
			int pageId = mallPageService.getPageIdByShopId( order.getShopId() );
			List< Object > objs = new ArrayList<>();
			objs.add( "您店铺有买家下单付款成功啦！" );
			objs.add( order.getOrderNo() );
			objs.add( order.getProductAllMoney() + "元" );
			objs.add( member.getNickname() );
			objs.add( member.getPhone() );
			objs.add( "请尽快登录后台处理订单！" );
			logger.info( "发送消息模板参数：" + objs );

			for ( MallBusMessageMember busMessageMember : busMessageMemberList ) {
			    String url = PropertiesUtil.getPhoneWebHomeUrl() + "/index/" + pageId;
			    SendWxMsgTemplate template = new SendWxMsgTemplate();
			    template.setId( id );
			    template.setUrl( url );
			    template.setMemberId( busMessageMember.getMemberId() );
			    template.setPublicId( CommonUtil.toInteger( PropertiesUtil.getDuofenPublicId() ) );
			    template.setObjs( objs );

			    wxPublicUserService.sendWxMsgTemplate( template );
			}
		    }
		}
	    }
	}
    }

    @Override
    public void buyerConfirmReceipt( Integer orderId, Integer type ) {
	MallOrder order = mallOrderService.selectById( orderId );
	if ( order != null ) {
	    BusUser busUser = busUserService.selectById( order.getBusUserId() );
	    String messages = "用户【" + busUser.getName() + "】成功购买您的商品，并确认收货成功，查看详情请登录商城后台。";
	    if ( type == 0 || type == 1 ) {//短信
		MallStore store = mallStoreService.selectById( order.getShopId() );
		String telPhone = "";
		if ( store != null && store.getStoIsSms() == 1 ) {//1是推送
		    if ( store.getStoSmsTelephone() != null ) {
			telPhone = store.getStoSmsTelephone();
		    }
		}
		if ( CommonUtil.isNotEmpty( telPhone ) ) {
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( telPhone );
		    oldApiSms.setCompany( busUser.getMerchant_name() );
		    oldApiSms.setBusId( busUser.getId() );
		    oldApiSms.setModel( Constants.SMS_MODEL );
		    oldApiSms.setContent( CommonUtil.format( messages ) );
		    try {
			smsService.sendSmsOld( oldApiSms );
		    } catch ( Exception e ) {
			e.printStackTrace();
			logger.error( "通知商家短信消息异常：" + e.getMessage() );
		    }
		}
	    }
	    if ( type == 0 || type == 2 ) {//模板
		Integer id = isOpenPaySetByBusMessage( busUser.getId(), Constants.BUS_TEMPLATE_LIST[1] );
		if ( id > 0 ) {
		    List< MallBusMessageMember > busMessageMemberList = selectByBusId( order.getBusUserId() );//商家
		    if ( busMessageMemberList != null && busMessageMemberList.size() > 0 ) {
			int pageId = mallPageService.getPageIdByShopId( order.getShopId() );
			List< Object > objs = new ArrayList<>();
			objs.add( "买家已经确认收货" );
			objs.add( order.getOrderNo() );
			objs.add( DateTimeKit.format( order.getExpressTime(), "yyyy-MM-dd HH:mm:ss" ) );
			objs.add( "详情请点击查看" );
			logger.info( "发送消息模板参数：" + objs );

			for ( MallBusMessageMember busMessageMember : busMessageMemberList ) {
			    String url = PropertiesUtil.getPhoneWebHomeUrl() + "/index/" + pageId;
			    SendWxMsgTemplate template = new SendWxMsgTemplate();
			    template.setId( id );
			    template.setUrl( url );
			    template.setMemberId( busMessageMember.getMemberId() );
			    template.setPublicId( CommonUtil.toInteger( PropertiesUtil.getDuofenPublicId() ) );
			    template.setObjs( objs );
			    wxPublicUserService.sendWxMsgTemplate( template );
			}
		    }
		}
	    }
	}
    }

    @Override
    public void buyerOrderReturn( MallOrderReturn orderReturn, Integer busId, Integer type ) {
	BusUser busUser = busUserService.selectById( busId );
	if ( busUser != null ) {
	    String messages = "用户【" + busUser.getName() + "】发起了【" + ( orderReturn.getRetHandlingWay() == 1 ? "退款" : "退货退款" ) + "】，请尽快登录商城后台查看并及时处理。";
	    if ( type == 0 || type == 1 ) {//短信
		MallStore store = mallStoreService.selectById( orderReturn.getShopId() );
		String telPhone = "";
		if ( store != null && store.getStoIsSms() == 1 ) {//1是推送
		    if ( store.getStoSmsTelephone() != null ) {
			telPhone = store.getStoSmsTelephone();
		    }
		}
		if ( CommonUtil.isNotEmpty( telPhone ) ) {
		    OldApiSms oldApiSms = new OldApiSms();
		    oldApiSms.setMobiles( telPhone );
		    oldApiSms.setCompany( busUser.getMerchant_name() );
		    oldApiSms.setBusId( busUser.getId() );
		    oldApiSms.setModel( Constants.SMS_MODEL );
		    oldApiSms.setContent( CommonUtil.format( messages ) );
		    try {
			smsService.sendSmsOld( oldApiSms );
		    } catch ( Exception e ) {
			e.printStackTrace();
			logger.error( "通知商家短信消息异常：" + e.getMessage() );
		    }
		}
	    }
	    if ( type == 0 || type == 2 ) {//模板
		Integer id = isOpenPaySetByBusMessage( busUser.getId(), Constants.BUS_TEMPLATE_LIST[2] );
		if ( id > 0 ) {
		    MallOrder order = mallOrderService.selectById( orderReturn.getOrderId() );
		    List< MallBusMessageMember > busMessageMemberList = selectByBusId( order.getBusUserId() );//商家
		    if ( busMessageMemberList != null && busMessageMemberList.size() > 0 ) {
			List< MallOrderDetail > orderDetails = mallOrderDetailService.getOrderDetailList( orderReturn.getOrderId() );
			String proName = "";
			if ( orderDetails != null && orderDetails.size() > 0 ) {
			    for ( MallOrderDetail detail : orderDetails ) {
				if ( !proName.equals( "" ) ) {proName += ",";}
				proName += detail.getDetProName();
			    }
			}
			int pageId = mallPageService.getPageIdByShopId( order.getShopId() );
			List< Object > objs = new ArrayList<>();
			objs.add( "您的店铺有买家申请维权。" );
			objs.add( order.getOrderNo() );
			objs.add( proName );
			objs.add( order.getReceiveName() );
			objs.add( order.getReceivePhone() );
			objs.add( orderReturn.getRetReason() );
			objs.add( "请尽快到后台处理。" );
			logger.info( "发送消息模板参数：" + objs );
			for ( MallBusMessageMember busMessageMember : busMessageMemberList ) {
			    String url = PropertiesUtil.getPhoneWebHomeUrl() + "/index/" + pageId;
			    SendWxMsgTemplate template = new SendWxMsgTemplate();
			    template.setId( id );
			    template.setUrl( url );
			    template.setMemberId( busMessageMember.getMemberId() );
			    template.setPublicId( CommonUtil.toInteger( PropertiesUtil.getDuofenPublicId() ) );
			    template.setObjs( objs );
			    wxPublicUserService.sendWxMsgTemplate( template );
			}
		    }

		}
	    }
	}
    }
}
