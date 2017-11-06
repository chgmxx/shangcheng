package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallCollect;
import com.gt.mall.result.phone.product.PhoneCollectProductResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
     * @param ids:收藏id集合
     *
     * @return boolean
     */
    boolean deleteCollect( String ids );

    /**
     * 查询商家是否已商品收藏
     *
     * @param proId  商品id
     * @param userId 用户Id
     */
    boolean getProductCollect( int proId, int userId );

    /**
     * 查询收藏商品集合
     *
     * @param memberId 会员id
     *
     * @return 收藏商品集合
     */
    List< PhoneCollectProductResult > getCollectProductList( Integer memberId );

}
