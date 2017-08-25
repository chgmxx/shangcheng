package com.gt.mall.service.web.applet.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.applet.MallAppletImageDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.service.web.applet.MallAppletImageService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序图片表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallAppletImageServiceImpl extends BaseServiceImpl< MallAppletImageDAO,MallAppletImage > implements MallAppletImageService {

    @Autowired
    private MallAppletImageDAO      mallAppletImageDAO;
    @Autowired
    private MallProductDAO          mallProductDAO;
    @Autowired
    private MallImageAssociativeDAO mallImageAssociativeDAO;

    @Override
    public PageUtil selectImageByShopId( Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallAppletImageDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mApplet/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断拍卖是否有数据
	    List< MallAppletImage > AuctionList = mallAppletImageDAO.selectByPage( params );
	    page.setSubList( AuctionList );
	}

	return page;
    }

    @Override
    public Map< String,Object > selectImageById( Integer id ) {
	Map< String,Object > imageMaps = new HashMap< String,Object >();
	MallAppletImage image = mallAppletImageDAO.selectById( id );
	if ( CommonUtil.isNotEmpty( image.getProId() ) && image.getProId() > 0 ) {
	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "id", image.getProId() );
	    List< Map< String,Object > > imageList = mallProductDAO.selectProductAllByShopids( params );
	    if ( imageList != null && imageList.size() > 0 ) {
		imageMaps = imageList.get( 0 );
		Map< String,Object > imgMaps = new HashMap< String,Object >();
		imgMaps.put( "isMainImages", 1 );
		imgMaps.put( "assType", 1 );
		imgMaps.put( "assId", image.getProId() );
		List< Map< String,Object > > proImageList = mallImageAssociativeDAO.selectByAssId( imgMaps );
		if ( proImageList != null && proImageList.size() > 0 ) {
		    imageMaps.put( "image_url", proImageList.get( 0 ).get( "image_url" ) );
		}
	    }
	}
	imageMaps.put( "id", image.getId() );
	imageMaps.put( "imageUrl", image.getImageUrl() );
	imageMaps.put( "pro_id", image.getProId() );
	imageMaps.put( "shop_id", image.getShopId() );
	imageMaps.put( "type", image.getType() );
	return imageMaps;
    }

    @Override
    public boolean editImage( Map< String,Object > params, int userId ) {
	if ( CommonUtil.isNotEmpty( params ) ) {
	    MallAppletImage appletImage = JSONObject.parseObject( JSON.toJSONString( params), MallAppletImage.class );
	    if ( CommonUtil.isNotEmpty( appletImage ) ) {
		int count = 0;
		if ( CommonUtil.isNotEmpty( appletImage.getId() ) ) {
		    count = mallAppletImageDAO.updateById( appletImage );
		} else {
		    appletImage.setCreateTime( new Date() );
		    appletImage.setBusUserId( userId );
		    count = mallAppletImageDAO.insert( appletImage );
		}
		if ( count > 0 ) {
		    return true;
		}
	    }
	}
	return false;
    }

    @Override
    public boolean deleteImage( Map< String,Object > params ) {
	int id = CommonUtil.toInteger( params.get( "id" ) );
	int type = CommonUtil.toInteger( params.get( "type" ) );
	MallAppletImage images = new MallAppletImage();
	images.setId( id );
	if ( type == -1 ) {
	    images.setIsDelete( 1 );
	} else if ( type == -2 ) {
	    images.setIsShow( 0 );
	} else {
	    images.setIsShow( 1 );
	}
	int count = mallAppletImageDAO.updateById( images );
	if ( count > 0 ) {
	    return true;
	}
	return false;
    }
}
