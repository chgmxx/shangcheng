package com.gt.mall.service.web.html;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.html.MallHtmlFrom;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * h5商城里面的表单信息 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHtmlFromService extends BaseService<MallHtmlFrom> {


    /**
     * h5商城列表页
     *
     * @return map
     */
    Map<String, Object> htmlListfrom(HttpServletRequest request);

    /**
     * 表单详情
     *
     * @return map
     */
    Map<String, Object> htmlfromview(HttpServletRequest request);
}
