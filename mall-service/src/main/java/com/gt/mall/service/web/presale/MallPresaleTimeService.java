package com.gt.mall.service.web.presale;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.presale.MallPresaleTime;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售时间表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleTimeService extends BaseService< MallPresaleTime > {

    /**
     * 批量添加或修改预售时间
     */
    public void insertUpdBatchTime(Map<String, Object> map, Integer preId);


    /**
     * 根据预售id查询预售时间
     */
    public List<MallPresaleTime> getPresaleTimeByPreId(int preId);
}
