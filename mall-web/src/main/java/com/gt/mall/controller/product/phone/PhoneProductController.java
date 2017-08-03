package com.gt.mall.controller.product.phone;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.base.BaseController;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.PropertiesUtil;
import com.gt.mall.util.SessionUtils;
import com.gt.mall.web.service.product.MallGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public class PhoneProductController extends BaseController {

    @Autowired
    private MallGroupService mallGroupService;

    /**
     * 查询子分类
     */
    @RequestMapping( "79B4DE7C/getChildGroup" )
    public void getChildGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	JSONObject obj = new JSONObject();
	try {
	    if ( CommonUtil.isNotEmpty( params.get( "groupPId" ) ) ) {
		//todo 参数改变了
		List< MallGroup > childList = mallGroupService.selectChildGroupByPId( params );
		obj.put( "childList", childList );
		String http = PropertiesUtil.getResourceUrl();
		obj.put( "http", http );

	    }
	} catch ( Exception e ) {
	    logger.error( "查询子分组出错:" + e );
	    e.printStackTrace();
	} finally {
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 清空搜索查询的标签
     */
    @RequestMapping( "79B4DE7C/clearSearchGroup" )
    public void clearSearchGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) throws IOException {
	JSONObject obj = new JSONObject();
	boolean result = false;
	try {
	    params.put( "userId", SessionUtils.getLoginMember( request ).getId() );

	    result = mallGroupService.clearSearchKeyWord( params );

	} catch ( Exception e ) {
	    logger.error( "清空搜索查询的标签出错:" + e );
	    e.printStackTrace();
	} finally {
	    obj.put( "result", result );
	    CommonUtil.write( response, obj );
	}

    }
}
