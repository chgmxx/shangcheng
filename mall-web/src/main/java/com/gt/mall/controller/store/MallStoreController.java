package com.gt.mall.controller.store;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gt.mall.base.BaseController;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.web.service.store.MallStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "/mallStore" )
public class MallStoreController extends BaseController {

    @Autowired
    private MallStoreService mallStoreService;

    public ServerResponse findByWxShopId() {
	List< MallStore > storeList = new ArrayList< MallStore >();
	try {
	    EntityWrapper< MallStore > entity = new EntityWrapper< MallStore >();
	    entity.eq( "id", 1 );
	    storeList = mallStoreService.selectList( entity );

	} catch ( Exception e ) {
	    this.logger.error( "MallStoreController方法异常：" + e.getMessage() );
	    e.printStackTrace();
	} finally {

	}
	return ServerResponse.createBySuccessCodeMessage( 1, "查询店铺id成功", storeList );
    }

}
