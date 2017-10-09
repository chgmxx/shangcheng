package com.gt.mall.service.web.html.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.html.MallHtmlReportDAO;
import com.gt.mall.entity.html.MallHtmlReport;
import com.gt.mall.service.web.html.MallHtmlReportService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * html 商城举报信息 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallHtmlReportServiceImpl extends BaseServiceImpl<MallHtmlReportDAO, MallHtmlReport> implements MallHtmlReportService {

    private Logger log = Logger.getLogger(MallHtmlReportServiceImpl.class);

    @Autowired
    private MallHtmlReportDAO htmlReportDAO;

    @Override
    public void htmlReport(Integer htmlid, Integer style) {
        int num = htmlReportDAO.countReportNumByHtmlId(htmlid, style);
        if (num > 0) {
            num += 1;
            htmlReportDAO.updateReportNumByHtmlId(num, htmlid, style);
        } else {
            MallHtmlReport obj = new MallHtmlReport();
            obj.setReportNum(1);
            obj.setStyle(style);
            obj.setHtmlId(htmlid);
            htmlReportDAO.insert(obj);
        }
    }
}
