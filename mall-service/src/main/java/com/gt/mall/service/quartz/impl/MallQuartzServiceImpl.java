package com.gt.mall.service.quartz.impl;

import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.entity.groupbuy.MallGroupJoin;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.service.quartz.MallQuartzService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.presale.MallPresaleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Component
public class MallQuartzServiceImpl implements MallQuartzService {

    private Logger logger = Logger.getLogger( MallQuartzServiceImpl.class );

    @Autowired
    private MallOrderReturnService mallOrderReturnService;

    @Autowired
    private MallOrderDAO mallOrderDAO;

    @Autowired
    private MallOrderDetailDAO mallOrderDetailDAO;

    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;

    @Autowired
    private MallPresaleService mallPresaleService;

    @Autowired
    private MallGroupBuyDAO mallGroupBuyDAO;

    @Autowired
    private MallGroupJoinDAO mallGroupJoinDAO;

    @Autowired
    private MallAuctionMarginService mallAuctionMarginService;

    /**
     * 对已结束未成团的订单进行退款
     */
    @Scheduled( cron = "0 0 3 * * ?" )//每天早上3点扫描
    //	@Scheduled(cron = "0 0 17 * * ?")//每天下午2点点扫描
    //	@Scheduled(cron = "0 0/2 * * * ?")//每隔2分钟扫描
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void returnFlow() {

	logger.info( "开始扫描充值失败的订单" );
	try {
	    List< Map< String,Object > > orderList = mallOrderDAO.selectOrderFlow();
	    if ( orderList != null && orderList.size() > 0 ) {
		for ( Map< String,Object > map : orderList ) {
		    if ( CommonUtil.isNotEmpty( map.get( "id" ) ) ) {
			List< MallOrderDetail > detailList = mallOrderDetailDAO.selectByOrderId( map );
			if ( detailList != null && detailList.size() > 0 ) {
			    MallOrderDetail mallOrderDetail = detailList.get( 0 );
			    boolean flag = mallOrderReturnService.returnEndOrder( mallOrderDetail.getOrderId(), mallOrderDetail.getId() );
			    if ( flag ) {
				if ( CommonUtil.isNotEmpty( map.get( "flow_recharge_status" ) ) ) {
				    if ( map.get( "flow_recharge_status" ).toString().equals( "2" ) ) {
					//todo 调用小屁孩的接口
					/*String sql = "UPDATE t_wx_fenbi_flow_record t SET t.rec_use_count = t.rec_use_count - " + mallOrderDetail.getDetProNum() + " WHERE id="
							+ mallOrderDetail.getFlowRecordId();
					daoUtil.update( sql );*/

				    }
				}
			    }
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "开始扫描充值失败的订单异常" + e );
	    e.printStackTrace();
	}

    }

    /**
     * 对已结束未成团的订单进行退款
     */
    @Scheduled( cron = "0 0 1 * * ?" )//每天早上1点扫描
    //	@Scheduled(cron = "0 0 17 * * ?")//每天下午2点点扫描
    //	@Scheduled(cron = "0 0/2 * * * ?")//每隔2分钟扫描
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void endGroupReturn() {
	logger.info( "开始扫描已结束未成团的订单" );
	try {
	    List< Map< String,Object > > groupList = mallGroupBuyDAO.selectEndGroupByAll();// 查询已结束未成团的团购信息
	    if ( groupList != null && groupList.size() > 0 ) {
		for ( Map< String,Object > map : groupList ) {
		    List< MallGroupJoin > joinList = mallGroupJoinDAO.selectByProJoinId( map );
		    if ( joinList != null && joinList.size() > 0 ) {
			for ( MallGroupJoin mallGroupJoin : joinList ) {
			    boolean flag = mallOrderReturnService.returnEndOrder( mallGroupJoin.getOrderId(), mallGroupJoin.getOrderDetailId() );

			    if ( flag ) {
				//修改团购状态
				MallGroupJoin join = new MallGroupJoin();
				join.setId( mallGroupJoin.getId() );
				join.setJoinStatus( -1 );
				mallGroupJoinDAO.updateById( join );
			    }
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "扫描已结束未成团的订单异常" + e );
	    e.printStackTrace();
	}

	logger.info( "开始扫描已结束的拍卖保证金" );
	try {
	    mallAuctionMarginService.returnMargin();
	} catch ( Exception e ) {
	    logger.error( "扫描已结束的拍卖保证金异常" + e );
	    e.printStackTrace();
	}

	logger.info( "start扫描已结束未付尾款的预售定金" );
	try {
	    mallPresaleDepositService.returnDeposit();
	} catch ( Exception e ) {
	    logger.error( "扫描已结束的预售定金异常" + e );
	    e.printStackTrace();
	}

	logger.info( "开始扫描已结束的预售商品" );
	try {
	    mallPresaleService.presaleProEnd();
	} catch ( Exception e ) {
	    logger.error( "开始扫描已结束的预售商品异常" + e );
	    e.printStackTrace();
	}

    }

    /**
     * 短信提醒预售开始时间和结束时间
     */
    @Scheduled( cron = "0 0 0/2 * * ?" )//两个小时扫描一次
    @Override
    public void presaleStar() {
	try {
	    mallPresaleService.presaleStartRemain();
	} catch ( Exception e ) {
	    logger.error( "短信提醒预售开始时间和结束时间异常" + e );
	    e.printStackTrace();
	}

    }
}
