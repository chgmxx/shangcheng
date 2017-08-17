package com.gt.mall.service.web.purchase.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.purchase.PurchaseOrderStatisticsDAO;
import com.gt.mall.entity.purchase.PurchaseOrderStatistics;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.service.web.purchase.PurchaseOrderStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class PurchaseOrderStatisticsServiceImpl extends BaseServiceImpl<PurchaseOrderStatisticsDAO, PurchaseOrderStatistics> implements PurchaseOrderStatisticsService {

    @Autowired
    private PurchaseOrderStatisticsDAO purchaseOrderStatisticsDAO;

    @Override
    public PageUtil findList(Map<String, Object> parms) {
        try {
            int pageSize = 10;
            int count = 0;
            List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
            int curPage = CommonUtil.isEmpty(parms.get("curPage")) ? 1 : CommonUtil.toInteger(parms.get("curPage"));
            ;
            count = purchaseOrderStatisticsDAO.findListCount(parms);
            PageUtil page = new PageUtil(curPage, pageSize, count, "");
            page.setUrl("statisticsForm");
            parms.put("pageFirst", (page.getCurPage() - 1) * 10);
            parms.put("pageLast", 10);
            if (count > 0) {
                map = purchaseOrderStatisticsDAO.findList(parms);
                if (map != null && map.size() > 0) {//如果返回的结果不为空
                    for (int i = 0; i < map.size(); i++) {
                        if (map.get(i).containsKey("nickname")) {
                            try {
                                byte[] bytes = (byte[]) map.get(i).get("nickname");
                                map.get(i).put("nickname", new String(bytes, "UTF-8"));
                            } catch (Exception e) {
                                map.get(i).put("nickname", null);
                            }
                        }
                    }
                }
            }
            page.setSubList(map);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
