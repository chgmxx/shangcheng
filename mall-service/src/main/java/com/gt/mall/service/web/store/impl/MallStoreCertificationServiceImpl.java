package com.gt.mall.service.web.store.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.DictBean;
import com.gt.mall.dao.store.MallStoreCertificationDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.store.MallStoreCertification;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.MemberAuthService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.store.MallStoreCertificationService;
import com.gt.mall.utils.CommonUtil;
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
    @Autowired
    private MemberAuthService         memberAuthService;

    @Override
    public MallStoreCertification selectByStoreId( Integer storeId ) {
	MallStoreCertification storeCert = mallStoreCertificationDAO.selectByStoreId( storeId );
	if ( storeCert != null && storeCert.getStoType() == 1 ) {
	    List< DictBean > categoryMap = dictService.getDict( "K002" );
	    if ( categoryMap != null && categoryMap.size() > 0 ) {
		for ( DictBean dictBean : categoryMap ) {
		    if ( dictBean.getItem_key().toString().equals( storeCert.getStoCategory().toString() ) ) {
			String value = dictBean.getItem_value();
			net.sf.json.JSONObject foorerObj = net.sf.json.JSONObject.fromObject( value );
			dictBean.setValue( foorerObj.get( "title" ).toString() );

			storeCert.setStoCategoryName( foorerObj.get( "title" ).toString() );
			break;
		    }
		}
	    }
	}
	return storeCert;
    }

    @Override
    public Map< String,Object > getStoreServiceByShopId( Integer shopId, Integer userId ) {
	Map< String,Object > result = new HashMap<>();
	Integer auth = memberAuthService.getMemberAuth( userId );
	if ( CommonUtil.isNotEmpty( auth ) ) {
	    if ( auth == 2 ) {
		result.put( "stoType", "企业认证" );
	    } else if ( auth == 3 ) {
		result.put( "stoType", "个人认证" );
	    }
	}
	/*MallStoreCertification certification = mallStoreCertificationDAO.selectByStoreId( shopId );
	if ( certification != null ) {
	    result.put( "stoType", certification.getStoType() == 0 ? "个人认证" : "企业认证" );
	    List< DictBean > categoryMap = dictService.getDict( "K002" );
	    for ( DictBean dictBean : categoryMap ) {
		Integer key = dictBean.getItem_key();
		String value = dictBean.getItem_value();
		JSONObject foorerObj = JSONObject.fromObject( value );

		if ( CommonUtil.isNotEmpty( certification.getStoCategory() ) && certification.getStoCategory().toString().equals( key.toString() ) ) {
		    result.put( "categoryName", foorerObj.get( "title" ).toString() );
		    break;
		}
	    }
	}*/
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
