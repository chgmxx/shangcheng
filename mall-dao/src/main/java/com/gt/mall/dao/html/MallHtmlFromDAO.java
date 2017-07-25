package com.gt.mall.dao.html;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.html.MallHtmlFrom;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * h5商城里面的表单信息 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlFromDAO extends BaseMapper< MallHtmlFrom > {

    /**
     * 得到H5模板的表单列表
     * @param html_id
     * @param firstNum
     * @param pageSize
     * @return
     */
    List<Map<String,Object>> getHtmlFromByHtmlId(Integer html_id, Integer firstNum, Integer pageSize);

    /**
     * 统计5模板的表单数量
     * @param html_id
     * @return
     */
    int countHtmlFromByHtmlId(Integer html_id);

    /**
     * 表单详情
     * @param id
     * @return
     */
    Map<String,Object> htmlFromView(Integer id);
}