package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallVisitor;

/**
 * <p>
 * 店铺访客表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-26
 */
public interface MallVisitorService extends BaseService< MallVisitor > {

    /**
     * 保存或修改页面访问记录 并统计数量至Jedis
     *
     * @param ip
     * @param memberId 会员ID
     * @param pageId   页面ID
     *
     * @return
     */
    boolean savePageVisitor( String ip, Integer memberId, Integer pageId );

    /**
     * 保存或修改商品访问记录 并统计数量至Jedis
     *
     * @param ip
     * @param memberId  会员ID
     * @param productId 商品ID
     *
     * @return
     */
    boolean saveProductVisitor( String ip, Integer memberId, Integer productId );

}
