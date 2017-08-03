package com.gt.mall.web.service.order.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.order.MallDaifuDAO;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.web.service.order.MallDaifuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 找人代付 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallDaifuServiceImpl extends BaseServiceImpl< MallDaifuDAO,MallDaifu > implements MallDaifuService {

    @Autowired
    private MallDaifuDAO mallDaifuDAO;


    @Override
    public MallDaifu selectByDfOrderNo( String dfOrderNo ) {
	return mallDaifuDAO.selectByDfOrderNo( dfOrderNo );
    }
}
