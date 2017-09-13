package com.gt.mall.controller.product;

import com.gt.mall.base.BaseController;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.service.web.product.MallProductInventoryService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品库存 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mPro/inven" )
public class MallProductInventoryController extends BaseController {

    @Autowired
    private MallProductInventoryService mallProductInventoryService;

    /**
     * 获取默认库存
     */
    @RequestMapping( "getInvenDefault" )
    public void getInvenDefault( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > map ) throws IOException {
	logger.info( "获取默认库存controller" );
	response.setCharacterEncoding( "utf-8" );
	MallProductInventory inven = null;
	try {
	    if ( !CommonUtil.isEmpty( map.get( "proId" ) ) ) {
		Integer productId = CommonUtil.toInteger( map.get( "proId" ) );
		inven = mallProductInventoryService.selectByIsDefault( productId );

	    }

	} catch ( Exception e ) {
	    e.printStackTrace();
	} finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "inven", inven );
	    CommonUtil.write( response, obj );
	}
    }

    /**
     * 获取库存
     */
    @RequestMapping( "getInvenByProId" )
    public void getInvenByProId( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > map ) throws IOException {
	logger.info( "获取默认库存controller" );
	response.setCharacterEncoding( "utf-8" );
	List< MallProductInventory > invenList = null;
	try {
	    if ( !CommonUtil.isEmpty( map.get( "proId" ) ) ) {
		Integer productId = CommonUtil.toInteger( map.get( "proId" ) );
		invenList = mallProductInventoryService.selectInvenByProductId( productId );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	}finally {
	    JSONObject obj = new JSONObject();
	    obj.put( "invenList", invenList );
	    CommonUtil.write( response,obj );
	}
    }
}
