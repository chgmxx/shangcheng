package com.gt.mall.web.service.store;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.util.PageUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 商城店铺 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public interface MallStoreService extends BaseService< MallStore >  {

    public PageUtil findByPage(Map<String, Object> params);


}
