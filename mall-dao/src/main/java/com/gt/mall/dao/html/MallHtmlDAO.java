package com.gt.mall.dao.html;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.html.MallHtml;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城里面的H5 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlDAO extends BaseMapper<MallHtml> {

    /**
     * 得到该用户 商家中的H5模板列表
     * @param user_id
     * @param pid
     * @param firstNum
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> getHtmlByUserId(Integer user_id, Integer pid, Integer firstNum, Integer pageSize);

    /**
     * 统计该用户 商家中的H5模板
     * @param user_id
     * @param pid
     * @return
     */
    int countHtmlByUserId(Integer user_id, Integer pid);


    /**
     * 后台中的H5模板列表
     *
     * @param firstNum
     * @param pageSize
     * @return
     */
    List<Map<String, Object>> getHtmlModelList(Integer firstNum, Integer pageSize);

    /**
     * 统计后台中的H5模板
     *
     * @return
     */
    int countHtmlModelList();


}