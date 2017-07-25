package com.gt.mall.web.service.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.dao.basic.MallTakeTheirDAO;
import com.gt.mall.dao.basic.MallTakeTheirTimeDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.basic.MallTakeTheirTime;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.web.service.basic.MallImageAssociativeService;
import com.gt.mall.web.service.basic.MallTakeTheirService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 到店自提表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallTakeTheirServiceImpl extends BaseServiceImpl<MallTakeTheirDAO, MallTakeTheir> implements MallTakeTheirService {

    private Logger logger = Logger.getLogger(MallTakeTheirServiceImpl.class);

    @Autowired
    private MallTakeTheirDAO mallTakeTheirDAO;
    @Autowired
    private MallTakeTheirTimeDAO takeTheirTimeDAO;
    @Autowired
    private MallImageAssociativeService imageAssociativeService;
    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;
    @Autowired
    private MallPaySetDAO paySetDAO;

    @Override
    public PageUtil selectByUserId(Map<String, Object> param) {
        int pageSize = 10;
        int curPage = CommonUtil.isEmpty(param.get("curPage")) ? 1 : CommonUtil
                .toInteger(param.get("curPage"));
        param.put("curPage", curPage);

        int count = mallTakeTheirDAO.selectCount(param);

        PageUtil page = new PageUtil(curPage, pageSize, count, "mFreight/takeindex.do");
        int firstNum = pageSize * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);
        param.put("firstNum", firstNum);// 起始页
        param.put("maxNum", pageSize);// 每页显示商品的数量

        if (count > 0) {// 判断上门自提是否有数据
            List<MallTakeTheir> takeList = mallTakeTheirDAO.selectList(param);
            page.setSubList(takeList);
        }
        return page;
    }

    @Override
    public boolean deleteTake(Map<String, Object> params) {
        boolean flag = false;
        if (params != null) {
            MallTakeTheir take = (MallTakeTheir) JSONObject.toBean(JSONObject.fromObject(params), MallTakeTheir.class);
            take.setIsDelete(1);
            int count = mallTakeTheirDAO.updateAllColumnById(take);
            if (count > 0) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public boolean editTake(Map<String, Object> params, BusUser user) {
        Integer code = -1;
        if (CommonUtil.isNotEmpty(params)) {
            MallTakeTheir take = (MallTakeTheir) JSONObject.toBean(
                    JSONObject.fromObject(params.get("obj")),
                    MallTakeTheir.class);
            if (CommonUtil.isNotEmpty(take)) {
                if (CommonUtil.isNotEmpty(take.getId())) {
                    code = mallTakeTheirDAO.updateAllColumnById(take);
                } else {
                    take.setUserId(user.getId());
                    take.setCreateTime(new Date());
                    code = mallTakeTheirDAO.insert(take);
                }
                if (take.getId() > 0) {
                    // 添加或修改图片
                    imageAssociativeService.insertUpdBatchImage(params, take.getId());
                    editTakeTheirTime(params, take.getId());
                }
            }
        }
        if (code > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void editTakeTheirTime(Map<String, Object> params, int takeId) {
        // 逻辑删除自提点接待时间
        if (!CommonUtil.isEmpty(params.get("deltimeList"))) {
            List<MallTakeTheirTime> timeList = (List<MallTakeTheirTime>) JSONArray
                    .toList(JSONArray.fromObject(params.get("deltimeList")), MallTakeTheirTime.class);
            if (timeList != null && timeList.size() > 0) {
                for (MallTakeTheirTime time : timeList) {
                    takeTheirTimeDAO.updateAllColumnById(time);
                }
            }
        }
        // 添加自提点接待时间
        if (!CommonUtil.isEmpty(params.get("timeList"))) {
            List<MallTakeTheirTime> timeList = (List<MallTakeTheirTime>) JSONArray
                    .toList(JSONArray.fromObject(params.get("timeList")), MallTakeTheirTime.class);
            if (timeList != null && timeList.size() > 0) {
                for (MallTakeTheirTime time : timeList) {
                    time.setTakeId(takeId);
                    time.setCreateTime(new Date());
                    takeTheirTimeDAO.insert(time);
                }

            }
        }
    }

    @Override
    public List<MallTakeTheir> selectListByUserId(Map<String, Object> param) {
        return mallTakeTheirDAO.selectList(param);
    }

    @Override
    public MallTakeTheir selectById(Map<String, Object> params) {
        MallTakeTheir take = mallTakeTheirDAO.selectByIds(params);

        params.put("assType", 3);
        params.put("assId", params.get("id"));
        // 查询商品图片
        List<MallImageAssociative> imageList = imageAssociativeDAO.selectImageByAssId(params);
        take.setImageList(imageList);

        take.setTimeList(takeTheirTimeDAO.selectByTakeId(take.getId()));

        return take;
    }

    @Override
    public boolean isTakeTheirByUserId(int userId) {
        MallPaySet paySet = new MallPaySet();
        paySet.setUserId(userId);
        // 通过用户id查询商户是否允许买家上门自提
        MallPaySet set = paySetDAO.selectByUserId(paySet);
        if (CommonUtil.isNotEmpty(set)) {
            if (CommonUtil.isNotEmpty(set.getIsTakeTheir())) {
                if (set.getIsTakeTheir().toString().equals("1")) {// 允许买家上门自提
                    Map<String, Object> param = new HashMap<String, Object>();
                    param.put("userId", userId);
                    int count = mallTakeTheirDAO.selectCountByBusUserId(param);
                    if (count > 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<MallTakeTheir> selectByBusUserId(Map<String, Object> map) {
        return mallTakeTheirDAO.selectByUserId(map);
    }
}
