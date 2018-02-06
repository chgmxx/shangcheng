package com.gt.mall.service.web.integral.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.integral.MallIntegralImageDAO;
import com.gt.mall.entity.integral.MallIntegralImage;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.integral.MallIntegralImageService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分商城图片循环 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallIntegralImageServiceImpl extends BaseServiceImpl< MallIntegralImageDAO,MallIntegralImage > implements MallIntegralImageService {

    private Logger log = Logger.getLogger( MallIntegralImageServiceImpl.class );

    @Autowired
    private MallIntegralImageDAO integralImageDAO;
    @Autowired
    private WxShopService        wxShopService;

    @Override
    public PageUtil selectImageByShopId( Map< String,Object > params, int userId, List< Map< String,Object > > shoplist ) {
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
        params.put( "curPage", curPage );
        int count = integralImageDAO.selectByCount( params );

        PageUtil page = new PageUtil( curPage, pageSize, count, "mallIntegral/image_index.do" );
        int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
        params.put( "firstNum", firstNum );// 起始页
        params.put( "maxNum", pageSize );// 每页显示商品的数量

        if ( count > 0 ) {// 判断拍卖是否有数据
            List< Map< String,Object > > imageList = integralImageDAO.selectByPage( params );
            if ( imageList != null && imageList.size() > 0 ) {
                for ( Map< String,Object > image : imageList ) {
                    int shopId = CommonUtil.toInteger( image.get( "shopId" ) );
                    for ( Map< String,Object > shopMap : shoplist ) {
                        int shop_id = CommonUtil.toInteger( shopMap.get( "id" ) );
                        if ( shop_id == shopId ) {
                            image.put( "shopName", shopMap.get( "sto_name" ) );
                            break;
                        }
                    }
                }
            }
            page.setSubList( imageList );
        }

        return page;
    }

    @Override
    public boolean editImage( Map< String,Object > params, int userId ) {
        if ( CommonUtil.isNotEmpty( params ) ) {
            MallIntegralImage appletImage = (MallIntegralImage) JSONObject.toJavaObject( JSONObject.parseObject( JSON.toJSONString( params ) ), MallIntegralImage.class );
            if ( CommonUtil.isEmpty( appletImage.getShopId() ) ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), "店铺不能为空" );
            }
            if ( CommonUtil.isEmpty( appletImage.getImageUrl() ) ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), "图片不能为空" );
            }
            if ( CommonUtil.isNotEmpty( appletImage ) ) {
                int count = 0;
                if ( CommonUtil.isNotEmpty( appletImage.getId() ) ) {
                    count = integralImageDAO.updateById( appletImage );
                } else {
                    appletImage.setCreateTime( new Date() );
                    appletImage.setBusUserId( userId );
                    count = integralImageDAO.insert( appletImage );
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
        MallIntegralImage images = new MallIntegralImage();
        images.setId( id );
        if ( type == -1 ) {
            images.setIsDelete( 1 );
        } else if ( type == -2 ) {
            images.setIsShow( 0 );
        } else {
            images.setIsShow( 1 );
        }
        int count = integralImageDAO.updateById( images );
        if ( count > 0 ) {
            return true;
        }
        return false;
    }

    @Override
    public List< MallIntegralImage > getIntegralImageByUser( Map< String,Object > params ) {
        Wrapper< MallIntegralImage > groupWrapper = new EntityWrapper<>();
        String sql = "";
        if ( CommonUtil.isNotEmpty( params.get( "userId" ) ) ) {
            sql += " and bus_user_id = " + params.get( "userId" );
        }
        if ( CommonUtil.isNotEmpty( params.get( "shopId" ) ) ) {
            sql += " and shop_id = " + params.get( "shopId" );
        }
        groupWrapper.where( "is_delete = 0 and is_show = 1 " + sql );
        groupWrapper.orderBy( "create_time desc" );
        return integralImageDAO.selectList( groupWrapper );
    }
}
