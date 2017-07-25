package com.gt.mall.web.service.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.auction.MallAuctionDAO;
import com.gt.mall.dao.basic.MallCollectDAO;
import com.gt.mall.entity.basic.MallCollect;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.auction.impl.MallAuctionMarginServiceImpl;
import com.gt.mall.web.service.basic.MallCollectService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 收藏表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCollectServiceImpl extends BaseServiceImpl<MallCollectDAO, MallCollect> implements MallCollectService {

    private Logger log = Logger.getLogger(MallCollectServiceImpl.class);

    @Autowired
    private MallCollectDAO collectDAO;

    @Override
    public void getProductCollect(HttpServletRequest request, int proId, int userId) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productId", proId);
        map.put("userId", userId);
        int id = 0;
        MallCollect collect = collectDAO.selectByCollect(map);
        if (CommonUtil.isNotEmpty(collect)) {
            if (CommonUtil.isNotEmpty(collect.getId())) {
                id = collect.getId();
                if (collect.getIsDelete().toString().equals("0")) {
                    request.setAttribute("isCollect", collect.getIsCollect());
                }
            }
        }
        request.setAttribute("collectId", id);
    }

    @Override
    public boolean collectionProduct(Map<String, Object> params, int userId) {
        MallCollect collect = (MallCollect) JSONObject.toBean(JSONObject.fromObject(params.get("params")), MallCollect.class);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("productId", collect.getProductId());
        map.put("userId", userId);
        MallCollect c = collectDAO.selectByCollect(map);
        if (CommonUtil.isNotEmpty(c)) {
            if (CommonUtil.isNotEmpty(c.getId())) {
                collect.setId(c.getId());
            }
        }
        int count = 0;
        if (CommonUtil.isNotEmpty(collect.getId())) {
            collect.setIsDelete(0);
            count = collectDAO.updateAllColumnById(collect);
        } else {
            collect.setUserId(userId);
            collect.setCreateTime(new Date());
            count = collectDAO.insert(collect);
        }
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteCollect(Map<String, Object> params) {
        if (CommonUtil.isNotEmpty(params.get("ids"))) {

            Integer[] ids = (Integer[]) JSONArray.toArray(JSONArray.fromObject(params.get("ids")), Integer.class);
            params.put("ids", ids);

            int count = collectDAO.batchUpdateCollect(params);
            if (count > 0) {
                return true;
            }
        }
        return false;
    }
}
