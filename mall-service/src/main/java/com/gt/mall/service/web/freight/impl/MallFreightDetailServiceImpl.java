package com.gt.mall.service.web.freight.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.freight.MallFreightDetailDAO;
import com.gt.mall.dao.freight.MallFreightProvincesDAO;
import com.gt.mall.entity.freight.MallFreightDetail;
import com.gt.mall.entity.freight.MallFreightProvinces;
import com.gt.mall.service.web.freight.MallFreightDetailService;
import com.gt.mall.util.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流表详情 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallFreightDetailServiceImpl extends BaseServiceImpl< MallFreightDetailDAO,MallFreightDetail > implements MallFreightDetailService {

    private Logger log = Logger.getLogger( MallFreightDetailServiceImpl.class );

    @Autowired
    private MallFreightDetailDAO    freightDetailDAO;
    @Autowired
    private MallFreightProvincesDAO freightProvincesDAO;

    @Override
    public void editFreightDetail( Map< String,Object > params, int freightId ) {
	Map< String,Object > delDetailMap = new HashMap< String,Object >();
	Map< String,Object > delProMap = new HashMap< String,Object >();
	// 需要删除的物流详情
	if ( !CommonUtil.isEmpty( params.get( "delDetail" ) ) ) {
	    delDetailMap = (Map< String,Object >) JSONObject.toJavaObject( JSONObject.parseObject( params.get( "delDetail" ).toString() ), Map.class );
	}
	// 需要删除的物流配送区域
	if ( !CommonUtil.isEmpty( params.get( "delPro" ) ) ) {
	    delProMap = (Map< String,Object >) JSONObject.toJavaObject( JSONObject.parseObject( params.get( "delPro" ).toString() ), Map.class );
	}

	if ( !CommonUtil.isEmpty( params.get( "detail" ) ) ) {
	    // 物流详情
	    List< MallFreightDetail > detailList =  JSONArray.parseArray( params.get( "detail" ).toString() , MallFreightDetail.class );
	    if ( detailList != null && detailList.size() > 0 ) {
		// 根据物流id查询物流详情
		for ( MallFreightDetail detail : detailList ) {
		    if ( CommonUtil.isEmpty( detail.getId() ) ) {// 新增物流详情
			detail.setFreightId( freightId );
			freightDetailDAO.insert( detail );
		    } else {
			if ( delDetailMap != null ) {
			    String detailId = detail.getId().toString();
			    if ( !CommonUtil.isEmpty( delDetailMap.get( detailId ) ) ) {
				delDetailMap.remove( detailId );// 移除已经存在物流详情
			    }
			}
			freightDetailDAO.updateById( detail );
		    }
		    updateFreightProvince( detail, freightId, delProMap );
		}
	    }
	}
	deleteFreightDetail( delDetailMap, 1 );
	deleteFreightDetail( delProMap, 2 );
    }

    /**
     * 修改物流配送区域
     *
     * @Title: updateFreightProvince
     */
    @SuppressWarnings( { "deprecation", "unchecked" } )
    private void updateFreightProvince( MallFreightDetail detail, int freightId, Map< String,Object > delProMap ) {
	if ( detail.getProvinceList() != null && detail.getId() != null ) {
	    List< MallFreightProvinces > proList =  JSONArray.parseArray( detail.getProvinceList().toString(), MallFreightProvinces.class );
	    for ( MallFreightProvinces fProvince : proList ) {
		if ( CommonUtil.isEmpty( fProvince.getId() ) ) {// 新增物流配送区域
		    fProvince.setFreightId( freightId );
		    fProvince.setFreightDetailId( detail.getId() );
		    freightProvincesDAO.insert( fProvince );
		} else {
		    String proId = fProvince.getId().toString();
		    if ( delProMap != null ) {
			if ( !CommonUtil.isEmpty( delProMap.get( proId ) ) ) {
			    delProMap.remove( proId );
			}
		    }
		    freightProvincesDAO.updateById( fProvince );
		}
	    }
	}
    }

    /**
     * 逻辑删除物流详情和物流配送区域
     *
     * @Title: deleteFreightDetail
     */
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    private void deleteFreightDetail( Map< String,Object > delMap, int type ) {
	if ( delMap != null && CommonUtil.isNotEmpty( delMap ) ) {
	    Iterator it = delMap.entrySet().iterator();
	    while ( it.hasNext() ) {
		Map.Entry< String,Integer > entry = (Map.Entry< String,Integer >) it.next();
		if ( type == 1 ) {
		    MallFreightDetail detail = new MallFreightDetail();
		    detail.setId( CommonUtil.toInteger( entry.getKey() ) );
		    detail.setIsDelete( 1 );
		    // 逻辑删除物流详情
		    freightDetailDAO.updateById( detail );
		} else {
		    MallFreightProvinces province = new MallFreightProvinces();
		    province.setId( CommonUtil.toInteger( entry.getKey() ) );
		    province.setIsDelete( 1 );
		    // 逻辑删除物流配送区域
		    freightProvincesDAO.updateById( province );
		}
	    }
	}
    }

}
