package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallCollectDAO;
import com.gt.mall.entity.basic.MallCollect;
import com.gt.mall.service.web.basic.MallCollectService;
import com.gt.mall.utils.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCollectServiceImpl extends BaseServiceImpl< MallCollectDAO,MallCollect > implements MallCollectService {

    private Logger log = Logger.getLogger( MallCollectServiceImpl.class );

    @Autowired
    private MallCollectDAO collectDAO;

    @Override
    public void getProductCollect( HttpServletRequest request, int proId, int userId ) {
	//        Map<String, Object> map = new HashMap<String, Object>();
	//        map.put("productId", proId);
	//        map.put("userId", userId);
	int id = 0;
	MallCollect mallCollect = new MallCollect();
	mallCollect.setUserId( userId );
	mallCollect.setProductId( proId );
	MallCollect collect = collectDAO.selectOne( mallCollect );
	if ( CommonUtil.isNotEmpty( collect ) ) {
	    if ( CommonUtil.isNotEmpty( collect.getId() ) ) {
		id = collect.getId();
		if ( collect.getIsDelete().toString().equals( "0" ) ) {
		    request.setAttribute( "isCollect", collect.getIsCollect() );
		}
	    }
	}
	request.setAttribute( "collectId", id );
    }

    @Override
    public boolean collectionProduct( Map< String,Object > params, int userId ) {
	MallCollect collect = (MallCollect) JSONObject.toJavaObject( JSONObject.parseObject( params.get( "params" ).toString() ), MallCollect.class );
	//        Map<String, Object> map = new HashMap<String, Object>();
	//        map.put("productId", collect.getProductId());
	//        map.put("userId", userId);
	//        MallCollect c = collectDAO.selectByCollect(map);

	MallCollect mallCollect = new MallCollect();
	mallCollect.setUserId( userId );
	mallCollect.setProductId( collect.getProductId() );
	MallCollect c = collectDAO.selectOne( mallCollect );

	if ( CommonUtil.isNotEmpty( c ) ) {
	    if ( CommonUtil.isNotEmpty( c.getId() ) ) {
		collect.setId( c.getId() );
	    }
	}
	int count = 0;
	if ( CommonUtil.isNotEmpty( collect.getId() ) ) {
	    collect.setIsDelete( 0 );
	    count = collectDAO.updateById( collect );
	} else {
	    collect.setUserId( userId );
	    collect.setCreateTime( new Date() );
	    count = collectDAO.insert( collect );
	}
	if ( count > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public boolean deleteCollect( Map< String,Object > params ) {
	if ( CommonUtil.isNotEmpty( params.get( "ids" ) ) ) {

	    //            Integer[] ids = (Integer[]) JSONArray.toArray(JSONArray.fromObject(params.get("ids")), Integer.class);
	    Integer[] ids = (Integer[]) JSONArray.toJSON( JSONArray.parseObject( params.get( "ids" ).toString() ) );
	    params.put( "ids", ids );

	    int count = collectDAO.batchUpdateCollect( params );
	    if ( count > 0 ) {
		return true;
	    }
	}
	return false;
    }

    @Override
    public boolean getProductCollect( int proId, int userId ) {
	int id = 0;
	MallCollect mallCollect = new MallCollect();
	mallCollect.setUserId( userId );
	mallCollect.setProductId( proId );
	MallCollect collect = collectDAO.selectOne( mallCollect );
	if ( CommonUtil.isNotEmpty( collect ) ) {
	    if ( CommonUtil.isNotEmpty( collect.getId() ) ) {
		if ( collect.getIsCollect().toString().equals( "1" ) ) {
		    return true;
		}
	    }
	}
	return false;
    }
}
