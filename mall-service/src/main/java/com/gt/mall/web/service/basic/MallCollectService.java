package com.gt.mall.web.service.basic;

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
public interface MallCollectService extends BaseService<MallCollect> {

    /**
     * 查询商品收藏
     *
     * @param request
     * @param proId
     */
    void getProductCollect(HttpServletRequest request, int proId, int userId);

    /**
     * 收藏商品
     *
     * @param params
     * @param userId
     * @return
     */
    boolean collectionProduct(Map<String, Object> params, int userId);

    /**
     * 删除收藏 可批量
     *
     * @param params
     * @return
     */
    boolean deleteCollect(Map<String, Object> params);
}
