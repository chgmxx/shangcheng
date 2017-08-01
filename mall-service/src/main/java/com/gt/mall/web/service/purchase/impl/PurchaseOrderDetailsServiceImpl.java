package com.gt.mall.web.service.purchase.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.purchase.PurchaseOrderDetailsDAO;
import com.gt.mall.entity.purchase.PurchaseOrderDetails;
import com.gt.mall.web.service.purchase.PurchaseOrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Service
public class PurchaseOrderDetailsServiceImpl extends BaseServiceImpl<PurchaseOrderDetailsDAO, PurchaseOrderDetails> implements PurchaseOrderDetailsService {

    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;

    /**
     * 根据详情信息里的商品id查询商品的图片
     */
    @Override
    public List<Map<String, Object>> productImg(Integer productId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("assId", productId);
        params.put("assType", 1);
        return imageAssociativeDAO.selectByAssId(params);
    }

}
