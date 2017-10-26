package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallCollect;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 收藏表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallCollectService extends BaseService< MallCollect > {

    /**
     * 查询商品收藏
     *
     * @param proId  商品id
     * @param userId 用户Id
     */
    void getProductCollect( HttpServletRequest request, int proId, int userId );

    /**
     * 收藏商品
     *
     * @param productId 商品id
     * @param userId    用户Id
     *
     * @return boolean
     */
    boolean collectionProduct( int productId, int userId );

    /**
     * 删除收藏 可批量
     *
     * @param params ids:收藏id集合，isDelete;是否删除，isCollect：是否收藏
     *
     * @return boolean
     */
    boolean deleteCollect( Map< String,Object > params );

    /**
     * 查询商家是否已商品收藏
     *
     * @param proId  商品id
     * @param userId 用户Id
     */
    boolean getProductCollect( int proId, int userId );
}
