package com.gt.mall.service.web.product.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallSearchLabelDAO;
import com.gt.mall.entity.product.MallSearchLabel;
import com.gt.mall.service.web.product.MallSearchLabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城搜索标签表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSearchLabelServiceImpl extends BaseServiceImpl< MallSearchLabelDAO,MallSearchLabel > implements MallSearchLabelService {

    @Autowired
    private MallSearchLabelDAO mallSearchLabelDAO;

    @Override
    public List< Map< String,Object > > selectByUser( Map< String,Object > map ) {
	return mallSearchLabelDAO.selectByUser( map );
    }
}
