package com.gt.mall.service.web.store.impl;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.store.MallStoreCertificationDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.SmsService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.JedisUtil;
import com.gt.util.entity.param.sms.OldApiSms;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
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
    private DictService               dictService;
    @Autowired
    private MallPaySetService         mallPaySetService;

    @Override
    public MallStoreCertification selectByStoreId( Integer storeId ) {
	return mallStoreCertificationDAO.selectByStoreId( storeId );
    }

    @Override
    public Map< String,Object > getStoreServiceByShopId( Integer shopId, Integer userId ) {
	Map< String,Object > result = new HashMap<>();
	MallStoreCertification certification = mallStoreCertificationDAO.selectByStoreId( shopId );
	if ( certification != null ) {
	    result.put( "stoType", certification.getStoType() == 0 ? "个人认证" : "企业认证" );

	    List< Map > categoryMap = dictService.getDict( "K002" );
	    for ( Map map : categoryMap ) {
		Integer key = (Integer) map.get( "item_key" );
		String value = (String) map.get( "item_value" );
		JSONObject foorerObj = JSONObject.fromObject( value );

		if ( certification.getStoCategory() == key ) {
		    result.put( "categoryName", foorerObj.get( "title" ).toString() );
		    break;
		}
	    }
	}
	result.put( "isSecuritytrade", false );
	MallPaySet set = new MallPaySet();
	set.setUserId( userId );
	set = mallPaySetService.selectByUserId( set );
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getIsSecuritytrade() ) ) {
		if ( set.getIsSecuritytrade() == 1 ) {
		    result.put( "isSecuritytrade", true );
		}
	    }
	}
	return result;
    }

}
