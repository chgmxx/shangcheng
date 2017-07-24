package com.gt.mall.web.service.store.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.web.service.store.MallStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallStoreServiceImpl extends BaseServiceImpl< MallStoreDAO,MallStore > implements MallStoreService {

    @Autowired
    private MallStoreDAO mallStoreDao;

    @Override
    public PageUtil findByPage( Map< String,Object > params ) {

        //todo 还需补充关联门店数据

        params.put("curPage", CommonUtil.isEmpty(params.get("curPage")) ? 1  : CommonUtil.toInteger(params.get("curPage")));
        int pageSize = 10;
        int rowCount = mallStoreDao.count(params);
        PageUtil page = new PageUtil(CommonUtil.toInteger(params.get("curPage")), pageSize, rowCount, "store/index.do");
        params.put("firstResult", pageSize
                        * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1));
        params.put("maxResult", pageSize);
        List<Map<String, Object>> list = mallStoreDao.findByPage(params);
        page.setSubList(list);
        //todo
        return page;
    }

}
