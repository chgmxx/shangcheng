package com.gt.mall.service.web.seller.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.seller.MallSellerOrderDAO;
import com.gt.mall.entity.seller.MallSellerOrder;
import com.gt.mall.service.web.seller.MallSellerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员订单中间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSellerOrderServiceImpl extends BaseServiceImpl< MallSellerOrderDAO,MallSellerOrder > implements MallSellerOrderService {

    @Autowired
    private MallSellerOrderDAO mallSellerOrderDAO;

    /**
     * 查询销售员的订单信息
     */
    @Override
    public Map< String,Object > selectOrderByMemberId( Map< String,Object > params ) {
	return mallSellerOrderDAO.selectOrderByMemberId( params );
    }

    /**
     * 查询客户的订单
     */
    @Override
    public List< Map< String,Object > > selectOrderByClientId( Map< String,Object > params ) {
	return mallSellerOrderDAO.selectOrderByClientId( params );
    }
}
