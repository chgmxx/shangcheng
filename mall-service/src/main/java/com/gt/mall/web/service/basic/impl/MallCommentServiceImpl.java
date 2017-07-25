package com.gt.mall.web.service.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallCommentGiveDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.web.service.basic.MallCommentService;
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
 * 商城评论 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCommentServiceImpl extends BaseServiceImpl<MallCommentDAO, MallComment> implements MallCommentService {

    private Logger logger = Logger.getLogger(MallCommentServiceImpl.class);

    @Autowired
    private MallCommentDAO commentDAO;
    @Autowired
    private MallCommentGiveDAO commentGiveDAO;
    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;

    @Override
    public PageUtil selectCommentPage(Map<String, Object> params) {
        List<Map<String, Object>> productList = null;
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty(params.get("curPage")) ? 1 : CommonUtil.toInteger(params.get("curPage"));
        params.put("curPage", curPage);

        int count = commentDAO.selectCommentCount(params);
        PageUtil page = new PageUtil(curPage, pageSize, count, "comment/to_index.do");
        int firstNum = pageSize * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);
        params.put("firstNum", firstNum);// 起始页
        params.put("maxNum", pageSize);// 每页显示商品的数量

        if (count > 0) {// 判断商品是否有数据
            productList = commentDAO.selectCommentList(params);
        }
        if (productList != null && productList.size() > 0) {
            for (Map<String, Object> map : productList) {
                if (CommonUtil.isNotEmpty(map.get("is_upload_image"))) {
                    if (map.get("is_upload_image").toString().equals("1")) {
                        Map<String, Object> imageMap = new HashMap<String, Object>();
                        imageMap.put("assId", map.get("id"));
                        imageMap.put("assType", 4);
                        List<MallImageAssociative> imageList = imageAssociativeDAO.selectImageByAssId(imageMap);
                        if (imageList != null && imageList.size() > 0) {
                            map.put("imageList", imageList);
                        }
                    }
                }
                if (CommonUtil.isNotEmpty(map.get("is_rep"))) {
                    int isRep = CommonUtil.toInteger(map.get("is_rep"));
                    if (isRep == 1) {
                        Map<String, Object> param = new HashMap<String, Object>();
                        param.put("appraise", map.get("id"));
                        List childList = commentDAO.ownerResponseList(param);
                        if (childList != null && childList.size() > 0) {
                            map.put("chilComment", childList.get(0));
                        }
                    }
                }
            }
        }
        page.setSubList(productList);
        return page;
    }

    @Override
    public boolean checkComment(Map<String, Object> params) {
        if (CommonUtil.isNotEmpty(params.get("ids"))) {
            String[] ids = (String[]) JSONArray.toArray(JSONArray.fromObject(params.get("ids")), String.class);
            params.put("ids", ids);
        }
        int count = commentDAO.batchUpdateComment(params);
        if (count > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean replatComment(Map<String, Object> params, int userId) {
        if (CommonUtil.isNotEmpty(params.get("params"))) {
            MallComment comment = (MallComment) JSONObject.toBean(JSONObject.fromObject(params.get("params")), MallComment.class);
            comment.setCreateTime(new Date());
            comment.setUserId(userId);
            comment.setUserType(2);
            int count = commentDAO.insert(comment);
            if (count > 0) {
                MallComment pComment = new MallComment();
                pComment.setId(comment.getRepPId());
                pComment.setIsRep("1");
                commentDAO.updateAllColumnById(pComment);
                return true;
            }
        }
        return false;
    }
}
