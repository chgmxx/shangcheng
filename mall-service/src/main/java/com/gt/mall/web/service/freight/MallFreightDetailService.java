package com.gt.mall.web.service.freight;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.freight.MallFreightDetail;

import java.util.Map;

/**
 * <p>
 * 物流表详情 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallFreightDetailService extends BaseService<MallFreightDetail> {
    /**
     * 编辑物流详情
     *
     * @param params    delDetail：删除的物流详情，delPro：删除的物流配送区域
     *                  detail：物流详情
     * @param freightId 物流Id
     */
    void editFreightDetail(Map<String, Object> params, int freightId);
}
