package com.gt.mall.service.web.basic.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallLogisticsInfoDAO;
import com.gt.mall.entity.basic.MallLogisticsInfo;
import com.gt.mall.service.web.basic.MallLogisticsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 物流信息表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-17
 */
@Service
public class MallLogisticsInfoServiceImpl extends BaseServiceImpl< MallLogisticsInfoDAO,MallLogisticsInfo > implements MallLogisticsInfoService {

    @Autowired
    private MallLogisticsInfoDAO mallLogisticsInfoDAO;

    @Override
    public List< MallLogisticsInfo > selectByOrderId( Integer orderId, String sn ) {

        Wrapper< MallLogisticsInfo > wrapper = new EntityWrapper<>();
        wrapper.where( "order_id = {0} and sn = {1}", orderId, sn );

        return mallLogisticsInfoDAO.selectList( wrapper );
    }
}
