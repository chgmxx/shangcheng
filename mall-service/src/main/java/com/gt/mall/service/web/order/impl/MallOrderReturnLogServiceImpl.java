package com.gt.mall.service.web.order.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.order.MallOrderReturnLogDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.order.MallOrderReturnLog;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.web.order.MallOrderReturnLogService;
import com.gt.mall.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单维权日志表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-10-17
 */
@Service
public class MallOrderReturnLogServiceImpl extends BaseServiceImpl< MallOrderReturnLogDAO,MallOrderReturnLog > implements MallOrderReturnLogService {

    @Autowired
    private MallOrderReturnLogDAO mallOrderReturnLogDAO;
    @Autowired
    private MallOrderReturnDAO    mallOrderReturnDAO;
    @Autowired
    private MallOrderDAO          mallOrderDAO;
    @Autowired
    private MallStoreDAO          mallStoreDAO;

    @Override
    public List< Map< String,Object > > selectReturnLogList( Integer returnId ) {
	Wrapper< MallOrderReturnLog > wrapper = new EntityWrapper<>();
	wrapper.where( "return_id = {0}", returnId );
	wrapper.orderBy( "create_time" );
	List< Map< String,Object > > logList = mallOrderReturnLogDAO.selectMaps( wrapper );
	if ( logList != null && logList.size() > 0 ) {
	    for ( Map< String,Object > map : logList ) {
		Integer getData = CommonUtil.toInteger( map.get( "getData" ) );
		if ( getData > 0 ) {
		    MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );
		    if ( getData == 1 ) {//1退货申请
			MallOrder order = mallOrderDAO.selectById( orderReturn.getOrderId() );
			map.put( "retHandlingWay", orderReturn.getRetHandlingWay() );
			map.put( "orderStatus", order.getOrderStatus() );
			map.put( "retReason", orderReturn.getRetReason() );
			map.put( "retMoney", orderReturn.getRetMoney() );
			map.put( "retMoney", orderReturn.getRetMoney() );
			map.put( "retRemark", orderReturn.getRetRemark() );
		    } else if ( getData == 2 ) {//2卖家退货地址
			MallStore store = mallStoreDAO.selectById( orderReturn.getShopId() );
			map.put( "stoAddress", store.getStoAddress() );
			map.put( "returnExplain", "请将配件全数寄回并保证不影响二次销售" );
			map.put( "stoPhone", store.getStoPhone() );
		    } else if ( getData == 3 ) {//3买家回寄的物流信息
			map.put( "wlCompany", orderReturn.getWlCompany() );
			map.put( "wlNo", orderReturn.getWlNo() );
			map.put( "returnAddress", orderReturn.getReturnAddress() );
		    }
		}
	    }
	}
	return logList;
    }
}
