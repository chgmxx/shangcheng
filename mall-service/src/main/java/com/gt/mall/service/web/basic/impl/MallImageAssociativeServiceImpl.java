package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.param.basic.ImageAssociativeDTO;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.EntityDtoConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
	    List< MallImageAssociative > imageList = JSONArray.parseArray( map.get( "delimageList" ).toString(), MallImageAssociative.class );
	    if ( imageList != null && imageList.size() > 0 ) {
		for ( MallImageAssociative mallImageAssociative : imageList ) {
		    imageAssociativeDAO.updateById( mallImageAssociative );
		}
		/*imageAssociativeDAO.updateBatchImage( imageList );*/
	    }
	}
	// 添加商品图片
	if ( !CommonUtil.isEmpty( map.get( "imageList" ) ) ) {
	    List< MallImageAssociative > addImgList = JSONArray.parseArray( map.get( "imageList" ).toString(), MallImageAssociative.class );
	    if ( addImgList != null && addImgList.size() > 0 ) {
		for ( MallImageAssociative images : addImgList ) {
		    images.setAssId( proId );
		    imageAssociativeDAO.insert( images );
		}

	    }
	}
    }

    @Override
    public void newInsertUpdBatchImage( Map< String,Object > map, Integer proId, Integer assType ) {
	if ( !CommonUtil.isEmpty( map.get( "imageList" ) ) ) {
	    Map< String,Object > imageMap = new HashMap< String,Object >();
	    imageMap.put( "assId", proId );
	    imageMap.put( "assType", assType );
	    List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( imageMap );

	    List< MallImageAssociative > addImgList = JSONArray.parseArray( map.get( "imageList" ).toString(), MallImageAssociative.class );

	    if ( addImgList != null && addImgList.size() > 0 ) {
		for ( MallImageAssociative images : addImgList ) {
		    if ( CommonUtil.isEmpty( images.getId() ) ) {
			images.setAssId( proId );
			imageAssociativeDAO.insert( images );
		    } else {
			imageAssociativeDAO.updateById( images );
			//1.remove 存在的数据
			for ( MallImageAssociative image : imageList ) {
			    if ( image.getId() == images.getId() ) {
				imageList.remove( image );
			    }
			}
		    }
		}
	    }
	    //2.还存在的数据，进行删除
	    if ( imageList != null && imageList.size() > 0 ) {
		for ( MallImageAssociative image : imageList ) {
		    image.setIsDelete( 1 );
		    imageAssociativeDAO.updateById( image );
		}
	    }
	}
    }

    @Override
    public void newSaveImage( List< ImageAssociativeDTO > imageAssociativeDTOS, Integer proId, Integer assType ) {

	Map< String,Object > imageMap = new HashMap< String,Object >();
	imageMap.put( "assId", proId );
	imageMap.put( "assType", assType );
	List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( imageMap );
	EntityDtoConverter converter = new EntityDtoConverter();

	if ( imageAssociativeDTOS != null && imageAssociativeDTOS.size() > 0 ) {
	    for ( ImageAssociativeDTO images : imageAssociativeDTOS ) {
		if ( CommonUtil.isEmpty( images.getId() ) ) {
		    MallImageAssociative imageAssociative = new MallImageAssociative();
		    converter.entityConvertDto( images, imageAssociative );
		    imageAssociative.setAssId( proId );
		    imageAssociative.setAssType( assType );
		    imageAssociativeDAO.insert( imageAssociative );
		} else {
		    MallImageAssociative imageAssociative = new MallImageAssociative();
		    converter.entityConvertDto( images, imageAssociative );
		    imageAssociativeDAO.updateById( imageAssociative );
		    //1.remove 存在的数据
		    for ( MallImageAssociative image : imageList ) {
			if ( image.getId() == images.getId() ) {
			    imageList.remove( image );
			}
		    }
		}
	    }
	}
	//2.还存在的数据，进行删除
	if ( imageList != null && imageList.size() > 0 ) {
	    for ( MallImageAssociative image : imageList ) {
		image.setIsDelete( 1 );
		imageAssociativeDAO.updateById( image );
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

    public List< MallImageAssociative > selectImageByAssId( Integer isMainImages, Integer assType, Integer assId ) {
	Wrapper< MallImageAssociative > wrapper = new EntityWrapper<>();
	wrapper.where( "is_delete = 0" );
	if ( CommonUtil.isNotEmpty( isMainImages ) ) {
	    wrapper.andNew( "is_main_images = {0}", isMainImages );
	}
	if ( CommonUtil.isNotEmpty( assType ) ) {
	    wrapper.andNew( "ass_type = {0}", assType );
	}
	if ( CommonUtil.isNotEmpty( assId ) ) {
	    wrapper.andNew( "ass_id = {0}", assId );
	}
	wrapper.orderBy( "ass_sort", true );
	return imageAssociativeDAO.selectList( wrapper );
    }

    public List< MallImageAssociative > selectImageByAssIds( Integer isMainImages, Integer assType, List< Integer > assIds ) {
	Wrapper< MallImageAssociative > wrapper = new EntityWrapper<>();
	wrapper.where( "is_delete = 0" );
	if ( CommonUtil.isNotEmpty( isMainImages ) ) {
	    wrapper.andNew( "is_main_images = {0}", isMainImages );
	}
	if ( CommonUtil.isNotEmpty( assType ) ) {
	    wrapper.andNew( "ass_type = {0}", assType );
	}
	if ( CommonUtil.isNotEmpty( assIds ) && assIds.size() > 0 ) {
	    wrapper.in( "ass_id", assIds );
	}
	wrapper.orderBy( "ass_sort", true );
	return imageAssociativeDAO.selectList( wrapper );
    }
}
