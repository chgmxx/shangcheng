package com.gt.mall.web.service.applet.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.applet.MallAppletImageDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.web.service.applet.MallAppletImageService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 小程序图片表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallAppletImageServiceImpl extends BaseServiceImpl<MallAppletImageDAO, MallAppletImage> implements MallAppletImageService {

    @Autowired
    private MallAppletImageDAO appletImageDAO;
    @Autowired
    private MallProductDAO productDAO;

    @Override
    public PageUtil selectImageByShopId(Map<String, Object> params) {
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty(params.get("curPage")) ? 1 : CommonUtil.toInteger(params.get("curPage"));
        params.put("curPage", curPage);
        int count = appletImageDAO.selectByCount(params);

        PageUtil page = new PageUtil(curPage, pageSize, count, "mApplet/index.do");
        int firstNum = pageSize * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);
        params.put("firstNum", firstNum);// 起始页
        params.put("maxNum", pageSize);// 每页显示商品的数量

        if (count > 0) {// 判断拍卖是否有数据
            List<MallAppletImage> AuctionList = appletImageDAO.selectByPage(params);
            page.setSubList(AuctionList);
        }

        return page;
    }

    @Override
    public Map<String, Object> selectImageById(Integer id) {
        MallAppletImage image = appletImageDAO.selectById(id);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", image.getProId());
        List<Map<String, Object>> imageList = productDAO.selectProductAllByShopids(params);
        if (imageList != null && imageList.size() > 0) {
            Map<String, Object> imageMaps = imageList.get(0);

            imageMaps.put("id", image.getId());
            imageMaps.put("imageUrl", image.getImageUrl());
            imageMaps.put("pro_id", image.getProId());
            imageMaps.put("shop_id", image.getShopId());
            imageMaps.put("type", image.getType());
            return imageMaps;
        }
        return null;
    }

    @Override
    public boolean editImage(Map<String, Object> params, int userId) {
        if (CommonUtil.isNotEmpty(params)) {
            MallAppletImage appletImage = (MallAppletImage) JSONObject.toBean(JSONObject.fromObject(params), MallAppletImage.class);
            if (CommonUtil.isNotEmpty(appletImage)) {
                int count = 0;
                if (CommonUtil.isNotEmpty(appletImage.getId())) {
                    count = appletImageDAO.updateById(appletImage);
                } else {
                    appletImage.setCreateTime(new Date());
                    appletImage.setBusUserId(userId);
                    count = appletImageDAO.insert(appletImage);
                }
                if (count > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean deleteImage(Map<String, Object> params) {
        int id = CommonUtil.toInteger(params.get("id"));
        int type = CommonUtil.toInteger(params.get("type"));
        MallAppletImage images = new MallAppletImage();
        images.setId(id);
        if (type == -1) {
            images.setIsDelete(1);
        } else if (type == -2) {
            images.setIsShow(0);
        } else {
            images.setIsShow(1);
        }
        int count = appletImageDAO.updateById(images);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
