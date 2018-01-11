package com.gt.mall.service.web.html;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.html.MallHtmlFrom;
import com.gt.mall.utils.PageUtil;

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
public interface MallHtmlFromService extends BaseService< MallHtmlFrom > {

    /**
     * h5商城列表页
     *
     * @return map
     */
    Map< String,Object > htmlListfrom( HttpServletRequest request );

    /**
     * h5商城表单列表页
     *
     * @return map
     */
    PageUtil newHtmlListfrom( HttpServletRequest request, Map< String,Object > params );

    /**
     * 表单详情
     *
     * @return map
     */
    Map< String,Object > htmlfromview( HttpServletRequest request );
}
