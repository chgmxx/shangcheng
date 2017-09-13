package com.gt.mall.service.web.purchase.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.purchase.PurchaseCompanyModeDAO;
import com.gt.mall.entity.purchase.PurchaseCompanyMode;
import com.gt.mall.service.web.purchase.PurchaseCompanyModeService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
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
public class PurchaseCompanyModeServiceImpl extends BaseServiceImpl<PurchaseCompanyModeDAO, PurchaseCompanyMode> implements PurchaseCompanyModeService {

    @Autowired
    private PurchaseCompanyModeDAO purchaseCompanyModeDAO;

    @Override
    public PageUtil findList(Map<String, Object> parms) {
        try {
            int pageSize = 10;
            int count = 0;
            List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
            int curPage = CommonUtil.isEmpty(parms.get("curPage")) ? 1 : CommonUtil.toInteger(parms.get("curPage"));
            ;
            count = purchaseCompanyModeDAO.findListCount(parms);
            PageUtil page = new PageUtil(curPage, pageSize, count, "");
            page.setUrl("companyForm");
            parms.put("pageFirst", (page.getCurPage() - 1) * 10);
            parms.put("pageLast", 10);
            if (count > 0) {
                map = purchaseCompanyModeDAO.findList(parms);
            }
            page.setSubList(map);
            return page;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
