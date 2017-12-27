package com.gt.mall.service.web.order.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.order.MallOrderReturnLogDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.order.MallOrderReturnLog;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallOrderReturnLogService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
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
    private MallOrderReturnLogDAO     mallOrderReturnLogDAO;
    @Autowired
    private MallOrderReturnDAO        mallOrderReturnDAO;
    @Autowired
    private MallOrderDAO              mallOrderDAO;
    @Autowired
    private MallStoreDAO              mallStoreDAO;
    @Autowired
    private MallOrderReturnLogService mallOrderReturnLogService;
    @Autowired
    private MallGroupBuyService       mallGroupBuyService;
    @Autowired
    private MallOrderDetailDAO        mallOrderDetailDAO;

    @Override
    public List< Map< String,Object > > selectReturnLogList( Integer returnId ) {
	Wrapper< MallOrderReturnLog > wrapper = new EntityWrapper<>();
	wrapper.where( "return_id = {0}", returnId );
	wrapper.orderBy( "create_time,id" );
	List< Map< String,Object > > logList = mallOrderReturnLogDAO.selectMaps( wrapper );
	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );
	if ( logList != null && logList.size() > 0 ) {
	    int i = 1;
	    for ( Map< String,Object > map : logList ) {
		Integer getData = CommonUtil.toInteger( map.get( "getData" ) );
		map.put( "orderDetailId", orderReturn.getOrderDetailId() );
		map.put( "nowReturnStatus", orderReturn.getStatus() );
		if ( getData > 0 ) {
		    net.sf.json.JSONObject foorerObj = net.sf.json.JSONObject.fromObject( map.get( "remark" ) );
		    map.put( "remark", foorerObj );
		}

		//截止时间
		if ( CommonUtil.isNotEmpty( map.get( "deadlineTime" ) ) ) {
		    try {
			int cont = DateTimeKit.dateCompare( map.get( "deadlineTime" ).toString(), DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
			if ( cont == 1 ) {
			    long[] times = DateTimeKit.getDistanceTimes( map.get( "deadlineTime" ).toString(), DateTimeKit.getDateTime() );
			    map.put( "times", times );
			    map.put( "msg", Constants.WAIT_SELLER_DISPOSE_REMARK1 );
			}
		    } catch ( Exception e ) {
			e.printStackTrace();
		    }
		}

		if ( i == logList.size() ) {
		    if ( orderReturn.getStatus() == -1 || orderReturn.getStatus() == 2 ) {
			MallOrder order = mallOrderDAO.selectById( orderReturn.getOrderId() );
			MallOrderDetail orderDetail = mallOrderDetailDAO.selectById( orderReturn.getOrderDetailId() );
			long updateDay = 0;//计算修改时间到今天的天数
			if ( CommonUtil.isNotEmpty( order.getUpdateTime() ) ) {
			    updateDay = DateTimeKit.diffDays( new Date(), order.getUpdateTime() );
			}
			boolean isNowReturn = false;//定义订单是否退款  false 没退款
			isNowReturn = OrderUtil.getOrderIsNowReturn( orderReturn.getStatus().toString() );
			boolean isGoupOrderCanReturn = true;
			if ( order.getOrderType() == 1 ) {
			    //团购订单是否能退款

			    isGoupOrderCanReturn = mallGroupBuyService.orderIsCanRenturn( order.getId(), orderDetail.getId(), order.getGroupBuyId() );
			    if ( !isGoupOrderCanReturn ) {
				//团购订单不能退款
				isNowReturn = false;
			    }
			}
			//判断订单详情是否能显示申请退款按钮 1显示
			int isShowAppllyReturn = 0;
			if ( isGoupOrderCanReturn ) {
			    isShowAppllyReturn = OrderUtil.getOrderIsShowReturnButton( order, orderDetail, updateDay );
			}

			if ( orderReturn.getStatus() == 2 ) {//同意申请
			    //是否显示退款物流的按钮 1显示
			    map.put( "isShowReturnWuLiuButton", OrderUtil.getOrderIsShowReturnWuliuButton( isNowReturn, orderReturn.getStatus().toString(), orderReturn ) );
			} else if ( orderReturn.getStatus() == -1 ) {//拒绝申请
			    //显示申请退款按钮 1显示
			    if ( isShowAppllyReturn == 1 && "4".equals( order.getOrderStatus() ) ) {
				//显示申请退款按钮
				map.put( "isShowApplyReturnButton", 1 );
			    }
			}
		    }
		}
		i++;

	    }
	}
	return logList;
    }

    /*1买家发起退款申请*/
    @Override
    public boolean addBuyerRetutnApply( Integer returnId, Integer userId, Integer way ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 0 );
	log.setReturnStatus( -3 );

	String msg = Constants.RETURN_APPLY;
	if ( way == 1 ) {
	    msg = msg.replace( "{type}", "退货" );
	} else {
	    msg = msg.replace( "{type}", "退货并退款" );
	}
	log.setStatusContent( msg );
	log.setGetData( 1 );

	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );

	MallOrder order = mallOrderDAO.selectById( orderReturn.getOrderId() );
	Map< String,Object > map = new HashMap<>();
	map.put( "退款类型：", orderReturn.getRetHandlingWay() == 1 ? "退款" : "退货并退款" );
	String cargo = "";
	if ( order.getOrderStatus() == 1 ) {
	    cargo = "未发货";
	} else if ( order.getOrderStatus() == 2 ) {
	    cargo = "待发货";
	} else if ( order.getOrderStatus() >= 3 ) {
	    cargo = "已发货";
	}
	map.put( "货物状态：", cargo );
	map.put( "退款原因：", orderReturn.getRetReason() );
	map.put( "退款金额：", orderReturn.getRetMoney() + "元" );
	if ( CommonUtil.isNotEmpty( orderReturn.getRetRemark() ) ) {
	    map.put( "退款说明：", orderReturn.getRetRemark() );
	}

	log.setRemark( JSONUtils.toJSONString( map ) );
	return mallOrderReturnLogService.insert( log );
    }

    @Override
    public boolean againRetutnApply( Integer returnId, Integer userId, Integer way ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 0 );
	log.setReturnStatus( -1 );

	String msg = Constants.RETURN_AGAIN_APPLY;
	if ( way == 1 ) {
	    msg = msg.replace( "{type}", "退货" );
	} else {
	    msg = msg.replace( "{type}", "退货并退款" );
	}
	log.setStatusContent( msg );
	log.setGetData( 1 );

	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );
	MallOrder order = mallOrderDAO.selectById( orderReturn.getOrderId() );
	Map< String,Object > map = new HashMap<>();
	map.put( "退款类型：", orderReturn.getRetHandlingWay() == 1 ? "退款" : "退货并退款" );
	String cargo = "";
	if ( order.getOrderStatus() == 1 ) {
	    cargo = "未发货";
	} else if ( order.getOrderStatus() == 2 ) {
	    cargo = "待发货";
	} else if ( order.getOrderStatus() >= 3 ) {
	    cargo = "已发货";
	}
	map.put( "货物状态：", cargo );
	map.put( "退款原因：", orderReturn.getRetReason() );
	map.put( "退款金额：", orderReturn.getRetMoney() + "元" );
	map.put( "退款说明：", orderReturn.getRetRemark() );
	log.setRemark( JSONUtils.toJSONString( map ) );

	return mallOrderReturnLogService.insert( log );
    }

    /*2 等待卖家处理*/
    @Override
    public boolean waitSellerDispose( Integer returnId, Date deadlineTime ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	//	log.setUserId(  );
	log.setOperator( 2 );
	log.setReturnStatus( -3 );
	log.setStatusContent( Constants.WAIT_SELLER_DISPOSE );
	log.setRemark( Constants.WAIT_SELLER_DISPOSE_REMARK );
	log.setDeadlineTime( deadlineTime );
	return mallOrderReturnLogService.insert( log );
    }

    /*3卖家同意申请*/
    @Override
    public boolean sellerAgreeApply( Integer returnId, Integer userId ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 1 );
	log.setReturnStatus( 2 );
	log.setStatusContent( Constants.SELLER_AGREE_APPLY );

	log.setGetData( 1 );

	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );
	MallStore store = mallStoreDAO.selectById( orderReturn.getShopId() );
	Map< String,Object > map = new HashMap<>();
	map.put( "退货地址：", store.getStoAddress() );
	map.put( "退款说明：", orderReturn.getRetRemark() );
	map.put( "商家电话：", store.getStoPhone() );
	log.setRemark( JSONUtils.toJSONString( map ) );

	return mallOrderReturnLogService.insert( log );
    }

    /*4买家已经退货  */
    public boolean buyerReturnGoods( Integer returnId, Integer userId ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 0 );
	log.setReturnStatus( 3 );
	log.setStatusContent( Constants.BUYER_RETURN_GOODS );
	log.setGetData( 1 );

	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );
	Map< String,Object > map = new HashMap<>();
	map.put( "物流公司：", orderReturn.getWlCompany() );
	map.put( "退货单号：", orderReturn.getWlNo() );
	map.put( "退货地址：", orderReturn.getReturnAddress() );
	log.setRemark( JSONUtils.toJSONString( map ) );

	return mallOrderReturnLogService.insert( log );
    }

    /* 5卖家已收到货，并退款*/
    public boolean sellerRefund( Integer returnId, Integer userId ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 1 );
	log.setReturnStatus( 0 );//退款中
	log.setStatusContent( Constants.SELLER_REFUND );
	log.setRemark( Constants.SELLER_REFUND_REMARK );
	return mallOrderReturnLogService.insert( log );
    }

    /*6退款成功*/
    public boolean refundSuccess( Integer returnId, String payWay, String returnPrice ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	/*log.setUserId( userId );*/
	log.setOperator( 2 );
	log.setReturnStatus( 5 );
	log.setStatusContent( Constants.REFUND_SUCCESS );
	String msg = Constants.REFUND_SUCCESS_REMARK;
	msg = msg.replace( "{price}", returnPrice );
	if ( CommonUtil.isNotEmpty( payWay ) ) {
	    msg = msg.replace( "{payWay}", "(" + payWay + ")" );
	} else {
	    msg = msg.replace( "{payWay}", "" );
	}
	log.setRemark( msg );
	return mallOrderReturnLogService.insert( log );
    }

    /*7卖家拒绝退款申请*/
    public boolean sellerRefuseRefund( Integer returnId, Integer userId ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 1 );
	log.setReturnStatus( -1 );
	log.setStatusContent( Constants.SELLER_REFUSE_REFUND );
	log.setRemark( Constants.SELLER_REFUSE_REFUND_REMARK );
	return mallOrderReturnLogService.insert( log );
    }

    /*8申请维权介入*/
    public boolean platformIntervention( Integer returnId, Integer userId, Integer type ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( type );
	//	log.setReturnStatus( -1 );
	String name = type == 1 ? "买家" : "卖家";
	log.setStatusContent( name + Constants.PLATFORM_INTERVENTION );

	return mallOrderReturnLogService.insert( log );
    }

    /*买家撤销退款*/
    public boolean buyerRevokeRefund( Integer returnId, Integer userId ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 0 );
	log.setReturnStatus( -2 );
	log.setStatusContent( Constants.BUYER_REVOKE_REFUND );
	log.setRemark( Constants.BUYER_REVOKE_REFUND_REMARK );
	return mallOrderReturnLogService.insert( log );
    }

    @Override
    public boolean buyerUpdateLogistics( Integer returnId, Integer userId ) {
	MallOrderReturnLog log = new MallOrderReturnLog();
	log.setReturnId( returnId );
	log.setCreateTime( new Date() );
	log.setUserId( userId );
	log.setOperator( 0 );
	log.setReturnStatus( 4 );
	log.setStatusContent( Constants.BUYER_UPDATE_LOGISTICS );
	log.setGetData( 1 );

	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );
	Map< String,Object > map = new HashMap<>();
	map.put( "物流公司：", orderReturn.getWlCompany() );
	map.put( "退货单号：", orderReturn.getWlNo() );
	map.put( "退货地址：", orderReturn.getReturnAddress() );
	log.setRemark( JSONUtils.toJSONString( map ) );

	return mallOrderReturnLogService.insert( log );
    }
}
