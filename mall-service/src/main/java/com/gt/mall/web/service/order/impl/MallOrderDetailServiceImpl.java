package com.gt.mall.web.service.order.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.web.service.order.MallOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商城订单详情表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderDetailServiceImpl extends BaseServiceImpl< MallOrderDetailDAO,MallOrderDetail > implements MallOrderDetailService {

    @Autowired
    private MallOrderDetailDAO mallOrderDetailDAO;//订单详情dao

}
