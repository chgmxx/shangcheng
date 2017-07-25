package com.gt.mall.web.service.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.basic.MallImageAssociativeService;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图片中间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallImageAssociativeServiceImpl extends BaseServiceImpl<MallImageAssociativeDAO, MallImageAssociative> implements MallImageAssociativeService {

    private Logger logger = Logger.getLogger(MallImageAssociativeServiceImpl.class);

    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;

    @Override
    public List<MallImageAssociative> selectByAssId(Map<String, Object> params) {
        return imageAssociativeDAO.selectImageByAssId(params);
    }

    @Override
    public void insertUpdBatchImage(Map<String, Object> map, Integer proId) {
        // 逻辑删除商品图片
        if (!CommonUtil.isEmpty(map.get("delimageList"))) {
            List<MallImageAssociative> imageList = (List<MallImageAssociative>) JSONArray
                    .toList(JSONArray.fromObject(map.get("delimageList")), MallImageAssociative.class);
            if (imageList != null && imageList.size() > 0) {
                imageAssociativeDAO.updateBatch(imageList);

            }
        }
        // 添加商品图片
        if (!CommonUtil.isEmpty(map.get("imageList"))) {
            List<MallImageAssociative> addImgList = (List<MallImageAssociative>) JSONArray
                    .toList(JSONArray.fromObject(map.get("imageList")), MallImageAssociative.class);
            if (addImgList != null && addImgList.size() > 0) {
                for (MallImageAssociative images : addImgList) {
                    images.setAssId(proId);
                    imageAssociativeDAO.insert(images);
                }

            }
        }
    }

    @Override
    public List<Map<String, Object>> selectImageByAssId(Map<String, Object> params) {
        return imageAssociativeDAO.selectByAssId(params);
    }
}
