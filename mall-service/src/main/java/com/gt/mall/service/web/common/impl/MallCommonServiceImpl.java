package com.gt.mall.service.web.common.impl;

import com.gt.mall.bean.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.web.common.MallCommonService;
import com.gt.mall.utils.CommonUtil;
import com.gt.util.entity.param.sms.OldApiSms;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 公共服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCommonServiceImpl implements MallCommonService {

    private Logger logger = Logger.getLogger( MallCommonServiceImpl.class );

    @Autowired
    private SmsService     smsService;
    @Autowired
    private BusUserService busUserService;

    @Override
    public boolean getValCode( String mobile, Integer busId, String content, String authorizerInfo ) {
	OldApiSms apiSms = new OldApiSms();
	apiSms.setBusId( busId );
	apiSms.setCompany( authorizerInfo );
	apiSms.setContent( content );
	apiSms.setMobiles( mobile );
	apiSms.setModel( CommonUtil.toInteger( Constants.SMS_MODEL ) );

	return smsService.sendSmsOld( apiSms );
    }

    @Override
    public boolean busIsAdvert( int busId ) {
	BusUser user = busUserService.selectById( busId );//根据商家id查询商家信息
	if ( CommonUtil.isNotEmpty( user ) ) {
	    if ( CommonUtil.isNotEmpty( user.getAdvert() ) ) {
		if ( user.getAdvert() == 0 ) {
		    return true;
		}
	    }
	}
	return false;
    }
}
