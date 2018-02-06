package com.gt.mall.service.quartz.impl;

import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.entity.groupbuy.MallGroupJoin;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.quartz.MallQuartzService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.presale.MallPresaleService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FenBiFlowService fenBiFlowService;

    /**
     * 对已结束未成团的订单进行退款
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public void activityRefund() {
        logger.info( "开始扫描已结束未成团的订单" );
        try {
            List< Map< String,Object > > groupList = mallGroupBuyDAO.selectEndGroupByAll();// 查询已结束未成团的团购信息
            if ( groupList != null && groupList.size() > 0 ) {
                for ( Map< String,Object > map : groupList ) {
                    List< MallGroupJoin > joinList = mallGroupJoinDAO.selectByProJoinId( map );
                    if ( joinList != null && joinList.size() > 0 ) {
                        for ( MallGroupJoin mallGroupJoin : joinList ) {
                            try {
                                boolean flag = mallOrderReturnService.returnEndOrder( mallGroupJoin.getOrderId(), mallGroupJoin.getOrderDetailId() );

                                if ( flag ) {
                                    //修改团购状态
                                    MallGroupJoin join = new MallGroupJoin();
                                    join.setId( mallGroupJoin.getId() );
                                    join.setJoinStatus( -1 );
                                    mallGroupJoinDAO.updateById( join );
                                }
                            } catch ( Exception e ) {
                                logger.error( "扫描已结束未成团的订单异常" + e );
                                e.printStackTrace();
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
    //    @Scheduled( cron = "0 0 0/2 * * ?" )//两个小时扫描一次
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
