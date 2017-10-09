package com.gt.mall.service.web.store.impl;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.store.MallStoreCertificationDAO;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.JedisUtil;
import com.gt.util.entity.param.sms.OldApiSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 店铺认证表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-09-19
 */
@Service
public class MallStoreCertificationServiceImpl extends BaseServiceImpl< MallStoreCertificationDAO,MallStoreCertification > implements MallStoreCertificationService {

    @Autowired
    private MallStoreCertificationDAO mallStoreCertificationDAO;
    @Autowired
    private WxPublicUserService       wxPublicUserService;
    @Autowired
    private SmsService                smsService;

    @Override
    public MallStoreCertification selectByStoreId( Integer storeId ) {
	return mallStoreCertificationDAO.selectByStoreId( storeId );
    }

    @Override
    public boolean getValCode( String mobile, Integer busId ) {
	WxPublicUsers pbUser = wxPublicUserService.selectByUserId( busId );
	String no = CommonUtil.getPhoneCode();
	JedisUtil.set( Constants.REDIS_KEY + no, no, 10 * 60 );
	System.out.println( no );

	OldApiSms apiSms = new OldApiSms();
	apiSms.setBusId( busId );
	apiSms.setCompany( pbUser.getAuthorizerInfo() );
	apiSms.setContent( "" + pbUser.getAuthorizerInfo() + "  提醒您，您的验证码为：(" + no + ")" + "，验证码10分钟内有效，请尽快完成验证。" );
	apiSms.setMobiles( mobile );
	apiSms.setModel( CommonUtil.toInteger( Constants.SMS_MODEL ) );

	return smsService.sendSmsOld( apiSms );
    }
}
