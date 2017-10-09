package com.gt.mall.dao.html;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.html.MallHtmlReport;

/**
 * <p>
 * html 商城举报信息 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlReportDAO extends BaseMapper<MallHtmlReport> {


    /**
     * 统计商城+举报方法 的举报次数
     *
     * @param html_id 模板Id
     * @return 数量
     */
    int countReportNumByHtmlId(Integer html_id, Integer style);

    /**
     * 根据商城id,举报id, 修改举报次数
     *
     * @param num 次数
     * @param html_id 模板id
     * @param style 举报id
     * @return 是否成功
     */
    int updateReportNumByHtmlId(Integer num, Integer html_id, Integer style);
}