package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallVisitorDAO;
import com.gt.mall.entity.basic.MallVisitor;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.service.web.basic.MallVisitorService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 店铺访客表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-26
 */
@Service
public class MallVisitorServiceImpl extends BaseServiceImpl< MallVisitorDAO,MallVisitor > implements MallVisitorService {

    @Autowired
    private MallVisitorDAO     mallVisitorDAO;
    @Autowired
    private MallPageService    mallPageService;
    @Autowired
    private MallProductService mallProductService;

    /**
     * @param ip
     * @param type 0页面  1商品
     * @param id   页面ID/商品ID
     *
     * @return
     */
    public MallVisitor isVisitorByIp( String ip, Integer type, Integer id ) {
        MallVisitor params = new MallVisitor();
        if ( type == 0 ) {//页面
            params.setPageId( id );
        } else {
            params.setProductId( id );
        }
        params.setAccessIp( ip );
        return mallVisitorDAO.selectOne( params );
    }

    /**
     * 访客数,浏览量保存至jedis
     *
     * @param type   0页面  1商品
     * @param id     页面ID/商品ID
     * @param status 修改数量+1  0全部 1浏览量 2访客数
     */
    public void visitorJedis( Integer type, Integer id, Integer status ) {
        String key = "";
        if ( type == 0 ) {
            key = Constants.REDIS_KEY + Constants.PAGE_VISITOR_KEY;
        } else {
            key = Constants.REDIS_KEY + Constants.PRODUCT_VISITOR_KEY;
        }
        Integer visitorNum = 1;//访客数
        Integer viewsNum = 1;//浏览量

        if ( JedisUtil.hExists( key, id.toString() ) ) {
            List< String > maps = JedisUtil.hmgetByKeys( key, id.toString() );
            if ( CommonUtil.isNotEmpty( maps ) ) {
                JSONObject obj = JSONObject.parseObject( maps.get( 0 ) );
                visitorNum = obj.getInteger( "visitorNum" );
                viewsNum = obj.getInteger( "viewsNum" );
                if ( status == 0 ) {
                    visitorNum += 1;
                    viewsNum += 1;
                } else if ( status == 1 ) {
                    viewsNum += 1;
                } else if ( status == 2 ) {
                    visitorNum += 1;
                }
            }
        }

        net.sf.json.JSONObject objs = new net.sf.json.JSONObject();
        objs.put( "visitorNum", visitorNum );
        objs.put( "viewsNum", viewsNum );
        JedisUtil.map( key, id + "", objs.toString() );

    }

    @Override
    public boolean savePageVisitor( String ip, Integer memberId, Integer pageId ) {
        boolean result = false;
        try {
            MallVisitor visitor = isVisitorByIp( ip, 0, pageId );
            if ( visitor != null ) {
                if ( CommonUtil.isNotEmpty( memberId ) ) {
                    visitor.setMemberId( memberId );
                }
                visitor.setAccessTime( new Date() );
                visitor.setAccessCount( visitor.getAccessCount() + 1 );
                mallVisitorDAO.updateById( visitor );
                visitorJedis( 0, pageId, 1 );
                result = true;
            } else {
                MallPage page = mallPageService.selectById( pageId );
                visitor = new MallVisitor();
                visitor.setUserId( page.getPagUserId() );
                visitor.setShopId( page.getPagStoId() );
                visitor.setPageId( pageId );
                visitor.setAccessIp( ip );
                visitor.setAccessTime( new Date() );
                visitor.setAccessCount( 1 );
                if ( CommonUtil.isNotEmpty( memberId ) ) {
                    visitor.setMemberId( memberId );
                }
                mallVisitorDAO.insert( visitor );
                visitorJedis( 0, pageId, 0 );
                result = true;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public boolean saveProductVisitor( String ip, Integer memberId, Integer productId ) {
        boolean result = false;
        try {
            MallVisitor visitor = isVisitorByIp( ip, 1, productId );
            if ( visitor != null ) {
                if ( CommonUtil.isNotEmpty( memberId ) ) {
                    visitor.setMemberId( memberId );
                }
                visitor.setAccessTime( new Date() );
                visitor.setAccessCount( visitor.getAccessCount() + 1 );
                mallVisitorDAO.updateById( visitor );
                visitorJedis( 1, productId, 1 );
                result = true;
            } else {
                MallProduct product = mallProductService.selectById( productId );
                visitor = new MallVisitor();
                visitor.setUserId( product.getUserId() );
                visitor.setShopId( product.getShopId() );
                visitor.setProductId( productId );
                visitor.setAccessIp( ip );
                visitor.setAccessTime( new Date() );
                visitor.setAccessCount( 1 );
                if ( CommonUtil.isNotEmpty( memberId ) ) {
                    visitor.setMemberId( memberId );
                }
                mallVisitorDAO.insert( visitor );
                visitorJedis( 1, productId, 0 );
                result = true;
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return result;
    }
}
