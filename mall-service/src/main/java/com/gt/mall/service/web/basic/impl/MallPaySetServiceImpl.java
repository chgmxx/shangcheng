package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.dao.seller.MallSellerDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.utils.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城交易支付设置 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPaySetServiceImpl extends BaseServiceImpl< MallPaySetDAO,MallPaySet > implements MallPaySetService {

    private Logger logger = Logger.getLogger( MallPaySetServiceImpl.class );

    @Autowired
    private MallPaySetDAO paySetDAO;

    @Autowired
    private MallSellerDAO sellerDAO;

    @Override
    public MallPaySet selectByUserId( MallPaySet set ) {
	return paySetDAO.selectOne( set );
    }

    @Override
    public int editPaySet( Map< String,Object > params ) {
	int count = 0;
	MallPaySet set = (MallPaySet) JSONObject.toJavaObject( JSONObject.parseObject( JSON.toJSONString( params ) ), MallPaySet.class );
	//	set.setSmsMessage( CommonUtil.urlEncode( set.getSmsMessage() ) );
	//	set.setPfRemark( CommonUtil.urlEncode( set.getPfRemark() ) );
	//	set.setPfApplyRemark( CommonUtil.urlEncode( set.getPfApplyRemark() ) );
	//	set.setBusMessageJson( CommonUtil.urlEncode( set.getBusMessageJson() ) );
	//	set.setMessageJson( CommonUtil.urlEncode( set.getMessageJson() ) );
	MallPaySet paySet = paySetDAO.selectOne( set );
	if ( CommonUtil.isNotEmpty( set ) && CommonUtil.isNotEmpty( paySet ) ) {
	    if ( CommonUtil.isEmpty( set.getId() ) && CommonUtil.isNotEmpty( paySet.getId() ) ) {
		set.setId( paySet.getId() );
	    }
	}
	if ( CommonUtil.isNotEmpty( set.getId() ) ) {
	    count = paySetDAO.updateById( set );
	} else {
	    set.setCreateTime( new Date() );
	    count = paySetDAO.insert( set );
	}
	if ( count > 0 ) {
	    MallPaySet mallPaySet = paySetDAO.selectOne( set );
	    if ( CommonUtil.isNotEmpty( set.getIsCheckSeller() ) ) {
		if ( set.getIsCheckSeller().toString().equals( "0" ) ) {
		    if ( CommonUtil.isNotEmpty( mallPaySet.getIsSeller() ) ) {
			if ( mallPaySet.getIsSeller().toString().equals( "1" ) ) {
			    sellerDAO.updateStatusByUserId( 1, set.getUserId() );
			}
		    }
		}
	    }
	    //修改updateById无法修改的数据
	    mallPaySet.setOrderCancel( set.getOrderCancel() );
	    mallPaySet.setCheckSellerPhone( set.getCheckSellerPhone() );
	    paySetDAO.updateAllColumnById( mallPaySet );
	}
	if ( count < 0 ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return count;
    }

    @Override
    public void isHuoDaoByUserId( int userId, HttpServletRequest request ) {
	String isHuoDao = "0";//不允许货到付款
	String isDaifu = "0";//不允许代付
	MallPaySet set = new MallPaySet();
	set.setUserId( userId );
	MallPaySet paySet = paySetDAO.selectOne( set );
	if ( CommonUtil.isNotEmpty( paySet ) ) {
	    if ( CommonUtil.isNotEmpty( paySet.getIsDeliveryPay() ) ) {
		if ( paySet.getIsDeliveryPay().toString().equals( "1" ) ) {
		    isHuoDao = "1";//允许货到付款
		}
	    }
	    if ( CommonUtil.isNotEmpty( paySet.getIsDaifu() ) ) {
		if ( paySet.getIsDaifu().toString().equals( "1" ) ) {
		    isDaifu = "1";//允许代付
		}
	    }
	}
	request.setAttribute( "isHuoDao", isHuoDao );
	request.setAttribute( "isDaifu", isDaifu );
    }

    @Override
    public Map< String,Object > isHuoDaoByUserId( int userId ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	String isHuoDao = "0";//不允许货到付款
	String isDaifu = "0";//不允许代付
	MallPaySet set = new MallPaySet();
	set.setUserId( userId );
	MallPaySet paySet = paySetDAO.selectOne( set );
	if ( CommonUtil.isNotEmpty( paySet ) ) {
	    if ( CommonUtil.isNotEmpty( paySet.getIsDeliveryPay() ) ) {
		if ( paySet.getIsDeliveryPay().toString().equals( "1" ) ) {
		    isHuoDao = "1";//允许货到付款
		}
	    }
	    if ( CommonUtil.isNotEmpty( paySet.getIsDaifu() ) ) {
		if ( paySet.getIsDaifu().toString().equals( "1" ) ) {
		    isDaifu = "1";//允许代付
		}
	    }
	}
	resultMap.put( "isHuoDao", isHuoDao );
	resultMap.put( "isDaifu", isDaifu );
	return resultMap;
    }

    @Override
    public MallPaySet selectByMember( Member member ) {
	if ( CommonUtil.isEmpty( member ) ) {
	    return null;
	}
	MallPaySet paySet = new MallPaySet();
	paySet.setUserId( member.getBusid() );
	return paySetDAO.selectOne( paySet );
    }

    @Override
    public Map< String,Object > getFooterMenu( int busUserId ) {
	Map< String,Object > footerMap = new HashMap< String,Object >();
	footerMap.put( "home", 1 );
	footerMap.put( "group", 1 );
	footerMap.put( "cart", 1 );
	footerMap.put( "my", 1 );
	MallPaySet paySet = new MallPaySet();
	paySet.setUserId( busUserId );
	paySet = paySetDAO.selectOne( paySet );
	if ( CommonUtil.isNotEmpty( paySet ) && paySet.getIsFooter() == 1 ) {
	    if ( CommonUtil.isNotEmpty( paySet.getFooterJson() ) ) {
		footerMap = (Map< String,Object >) JSONObject.toJavaObject( JSONObject.parseObject( paySet.getFooterJson() ), Map.class );
		if ( CommonUtil.toString( footerMap.get( "home" ) ).equals( "0" ) && CommonUtil.toString( footerMap.get( "group" ) ).equals( "0" ) && CommonUtil
				.toString( footerMap.get( "cart" ) ).equals( "0" ) && CommonUtil.toString( footerMap.get( "my" ) ).equals( "0" ) ) {
		    return null;
		}
	    }
	} else {
	    return null;
	}
	return footerMap;
    }

    @Override
    public MallPaySet selectByUserId( int busId ) {
	MallPaySet set = new MallPaySet();
	set.setUserId( busId );
	return paySetDAO.selectOne( set );
    }

    @Override
    public List< MallPaySet > selectByUserIdList( List< Integer > busIdList ) {
	Wrapper< MallPaySet > wrapper = new EntityWrapper<>();
	wrapper.in( "user_id", busIdList );

	return paySetDAO.selectList( wrapper );
    }
}
