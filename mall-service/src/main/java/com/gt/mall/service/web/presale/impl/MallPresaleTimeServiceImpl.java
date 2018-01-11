package com.gt.mall.service.web.presale.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.presale.MallPresaleTimeDAO;
import com.gt.mall.entity.freight.MallFreightDetail;
import com.gt.mall.entity.freight.MallFreightProvinces;
import com.gt.mall.entity.presale.MallPresaleTime;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.service.web.presale.MallPresaleTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售时间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPresaleTimeServiceImpl extends BaseServiceImpl< MallPresaleTimeDAO,MallPresaleTime > implements MallPresaleTimeService {

    @Autowired
    private MallPresaleTimeDAO mallPresaleTimeDAO;

    /**
     * 批量添加或修改预售时间
     */
    @SuppressWarnings( { "deprecation", "unchecked" } )
    @Override
    public void insertUpdBatchTime( Map< String,Object > map, Integer preId ) {

	// 逻辑删除预售时间
	if ( !CommonUtil.isEmpty( map.get( "delPresaleTimes" ) ) ) {

	    List< MallPresaleTime > timeList = JSONArray.parseArray( map.get( "delPresaleTimes" ).toString(), MallPresaleTime.class );
	    if ( timeList != null && timeList.size() > 0 ) {
		for ( MallPresaleTime time : timeList ) {
		    mallPresaleTimeDAO.updateById( time );
		}
	    }
	}
	// 添加预售时间
	if ( !CommonUtil.isEmpty( map.get( "presaleTimes" ) ) ) {
	    List< MallPresaleTime > addTimeList = JSONArray.parseArray( map.get( "presaleTimes" ).toString(), MallPresaleTime.class );
	    if ( addTimeList != null && addTimeList.size() > 0 ) {
		for ( MallPresaleTime time : addTimeList ) {
		    time.setPresaleId( preId );
		    mallPresaleTimeDAO.insert( time );
		}

	    }
	}
    }

    /**
     * 批量添加或修改预售时间
     */
    @SuppressWarnings( { "deprecation", "unchecked" } )
    @Override
    public void newInsertUpdBatchTime( Map< String,Object > map, Integer preId ) {

	Wrapper< MallPresaleTime > prowrapper = new EntityWrapper<>();
	prowrapper.where( "presale_id={0} and is_delete = 0 ", preId );
	List< MallPresaleTime > presaleTimes = mallPresaleTimeDAO.selectList( prowrapper );

	// 添加预售时间
	if ( !CommonUtil.isEmpty( map.get( "presaleTimes" ) ) ) {
	    List< MallPresaleTime > addTimeList = JSONArray.parseArray( map.get( "presaleTimes" ).toString(), MallPresaleTime.class );
	    if ( addTimeList != null && addTimeList.size() > 0 ) {
		for ( MallPresaleTime time : addTimeList ) {
		    if ( CommonUtil.isEmpty( time.getId() ) ) {// 新增预售时间
			time.setPresaleId( preId );
			mallPresaleTimeDAO.insert( time );
		    } else {
			if ( presaleTimes != null ) {
			    int timeId = time.getId();
			    for ( MallPresaleTime presaleTime : presaleTimes ) {
				if ( presaleTime.getId() == timeId ) {
				    presaleTimes.remove( presaleTime );// 移除已经存在预售时间
				    break;
				}
			    }
			}
			mallPresaleTimeDAO.updateById( time );
		    }

		}

	    }
	}
	//删除预售时间
	if ( presaleTimes != null && presaleTimes.size() > 0 ) {
	    for ( MallPresaleTime presaleTime : presaleTimes ) {
		presaleTime.setIsDelete( 1 );
		mallPresaleTimeDAO.updateById( presaleTime );
	    }
	}
    }

    @Override
    public List< MallPresaleTime > getPresaleTimeByPreId( int preId ) {
	return mallPresaleTimeDAO.selectByPreId( preId );
    }

}
