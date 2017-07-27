package com.gt.mall.web.service.html;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.html.MallHtmlReport;

/**
 * <p>
 * h5 商城举报信息 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlReportService extends BaseService<MallHtmlReport> {

    /**
     * 举报方法
     *
     * @param htmlid 模板Id
     * @param style  举报id
     */
    void htmlReport(Integer htmlid, Integer style);

}
