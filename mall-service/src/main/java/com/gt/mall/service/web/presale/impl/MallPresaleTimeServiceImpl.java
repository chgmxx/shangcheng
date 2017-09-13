package com.gt.mall.service.web.presale.impl;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.presale.MallPresaleTimeDAO;
import com.gt.mall.entity.presale.MallPresaleTime;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.service.web.presale.MallPresaleTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售时间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPresaleTimeServiceImpl extends BaseServiceImpl< MallPresaleTimeDAO,MallPresaleTime > implements MallPresaleTimeService {

    @Autowired
    private MallPresaleTimeDAO mallPresaleTimeDAO;


    /**
     * 批量添加或修改预售时间
     */
    @SuppressWarnings({ "deprecation", "unchecked" })
    @Override
    public void insertUpdBatchTime(Map<String, Object> map, Integer preId){

	// 逻辑删除预售时间
	if (!CommonUtil.isEmpty(map.get("delPresaleTimes"))) {

	    List<MallPresaleTime> timeList = JSONArray.parseArray( map.get("delPresaleTimes").toString(),MallPresaleTime.class );
	    if (timeList != null && timeList.size() > 0) {
		for (MallPresaleTime time : timeList) {
		    mallPresaleTimeDAO.updateById(time);
		}
	    }
	}
	// 添加预售时间
	if (!CommonUtil.isEmpty(map.get("presaleTimes"))) {
	    List<MallPresaleTime> addTimeList = JSONArray.parseArray( map.get("presaleTimes").toString(),MallPresaleTime.class );
	    if (addTimeList != null && addTimeList.size() > 0) {
		for (MallPresaleTime time : addTimeList) {
		    time.setPresaleId(preId);
		    mallPresaleTimeDAO.insert(time);
		}

	    }
	}
    }

    @Override
    public List<MallPresaleTime> getPresaleTimeByPreId(int preId) {
	return mallPresaleTimeDAO.selectByPreId(preId);
    }

}
