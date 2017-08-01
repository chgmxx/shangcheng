package com.gt.mall.web.service.basic.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.basic.MallImageAssociativeService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片中间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallImageAssociativeServiceImpl extends BaseServiceImpl< MallImageAssociativeDAO,MallImageAssociative > implements MallImageAssociativeService {

    private Logger logger = Logger.getLogger( MallImageAssociativeServiceImpl.class );

    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;

    @Override
    public List< MallImageAssociative > selectByAssId( Map< String,Object > params ) {
	return imageAssociativeDAO.selectImageByAssId( params );
    }

    @Override
    public void insertUpdBatchImage( Map< String,Object > map, Integer proId ) {
	// 逻辑删除商品图片
	if ( !CommonUtil.isEmpty( map.get( "delimageList" ) ) ) {
	    List< MallImageAssociative > imageList = JSONArray.parseArray( map.get( "delimageList" ).toString(),MallImageAssociative.class );
	    if ( imageList != null && imageList.size() > 0 ) {
		imageAssociativeDAO.updateBatch( imageList );
	    }
	}
	// 添加商品图片
	if ( !CommonUtil.isEmpty( map.get( "imageList" ) ) ) {
	    List< MallImageAssociative > addImgList = JSONArray.parseArray( map.get( "imageList" ).toString(),MallImageAssociative.class );
	    if ( addImgList != null && addImgList.size() > 0 ) {
		for ( MallImageAssociative images : addImgList ) {
		    images.setAssId( proId );
		    imageAssociativeDAO.insert( images );
		}

	    }
	}
    }

    @Override
    public List< Map< String,Object > > selectImageByAssId( Map< String,Object > params ) {
	return imageAssociativeDAO.selectByAssId( params );
    }

    @Override
    public List< MallImageAssociative > getParamByProductId( Map< String,Object > params ) {
	Wrapper< MallImageAssociative > wrapper = new EntityWrapper<>();
	wrapper.where( "is_delete = 0" );
	if ( CommonUtil.isNotEmpty( params.get( "assId" ) ) ) {
	    wrapper.andNew( "ass_id = {0}", params.get( "assId" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "isMainImages" ) ) ) {
	    wrapper.andNew( "is_main_images = {0}", params.get( "isMainImages" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "assType" ) ) ) {
	    wrapper.andNew( "ass_type = {0}", params.get( "assType" ) );
	}
	wrapper.orderBy( "ass_sort", true );
	return imageAssociativeDAO.selectList( wrapper );
    }
}
