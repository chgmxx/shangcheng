package com.gt.mall.service.quartz.impl;

import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.order.MallOrderTaskDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.product.MallProductInventoryDAO;
import com.gt.mall.dao.product.MallProductSpecificaDAO;
import com.gt.mall.entity.basic.MallIncomeList;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.order.MallOrderTask;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionConsumeService;
import com.gt.mall.service.quartz.MallQuartzOrderTaskService;
import com.gt.mall.service.web.basic.MallIncomeListService;
import com.gt.mall.service.web.order.MallOrderTaskService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.JedisUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单定时任务服务类
 */
@Service
@Component
public class MallQuartzOrderTaskServiceImpl implements MallQuartzOrderTaskService {

    private Logger logger = Logger.getLogger( MallQuartzServiceImpl.class );

    @Autowired
    private MallOrderReturnDAO      mallOrderReturnDAO;
    @Autowired
    private MallProductDAO          mallProductDAO;
    @Autowired
    private MallOrderDAO            mallOrderDAO;
    @Autowired
    private MallOrderDetailDAO      orderDetailDAO;
    @Autowired
    private MemberService           memberService;
    @Autowired
    private UnionConsumeService     unionConsumeService;
    @Autowired
    private MallIncomeListService   mallIncomeListService;
    @Autowired
    private MallOrderTaskService    mallOrderTaskService;
    @Autowired
    private MallOrderTaskDAO        mallOrderTaskDAO;
    @Autowired
    private MallProductSpecificaDAO mallProductSpecificaDAO;
    @Autowired
    private MallProductInventoryDAO mallProductInventoryDAO;

    @Override
    public void newCloseOrderNoPay() {
        Map< String,Object > params = new HashMap<>();
        params.put( "type", "1" );
        List< MallOrderTask > taskList = mallOrderTaskService.findByType( params );
        if ( taskList != null && taskList.size() > 0 ) {
            for ( MallOrderTask task : taskList ) {
                try {
                    MallOrder order = mallOrderDAO.selectById( task.getOrderId() );
                    if ( order != null && order.getOrderStatus() == 1 ) {
                        String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
                        String eDate = DateTimeKit.format( new Date(), format );
                        long mins = DateTimeKit.minsBetween( DateTimeKit.format( order.getCreateTime(), format ), eDate, 60000, format );
                        if ( mins >= task.getReturnDay() ) {// 订单已经超过设定时间
                            // 恢复订单库存
                            addInvNum( order.getId().toString() );

                            task.setIsDelete( 1 );
                            mallOrderTaskService.updateById( task );
                        }
                    } else {//订单不存在，或状态已改变
                        task.setIsDelete( 1 );
                        mallOrderTaskService.updateById( task );
                    }
                } catch ( BusinessException e ) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 订单完成赠送物品  每天早上8点扫描
     */
   /* @Scheduled( cron = "0 0 8 * * ?" )*/
    @Override
    public void orderFinish() {
        System.out.println( "开始赠送物品" );
        try {
            Map< String,Object > params = new HashMap<>();
            params.put( "type", "3" );
            List< MallOrderTask > taskList = mallOrderTaskService.findByType( params );
            if ( taskList != null && taskList.size() > 0 ) {
                for ( MallOrderTask task : taskList ) {
                    try {
                        MallOrder order = mallOrderDAO.getOrderById( task.getOrderId() );
                        if ( order != null && order.getOrderStatus() == 4 && order.getGiveStatus() == 0 ) {
                            Date time = DateTimeKit.addDate( order.getUpdateTime(), task.getReturnDay() );//等待最终时间
                            Date newDate = new Date();
                            if ( newDate.getTime() >= time.getTime() ) {//说明已超过最终时间
                                boolean flag = false;
                                if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
                                    for ( int j = 0; j < order.getMallOrderDetail().size(); j++ ) {
                                        MallOrderDetail dMap = order.getMallOrderDetail().get( j );
                                        String status = dMap.getStatus().toString();
                                        if ( status.equals( "-3" ) || status.equals( "1" ) || status.equals( "5" ) || status.equals( "-2" ) ) {
                                            flag = true;
                                        } else {
                                            flag = false;
                                            break;
                                        }
                                    }
                                    if ( flag ) {
                                        try {
                                            boolean isSuccess = memberService.findGiveRuleDelay( task.getOrderNo() );
                                            if ( isSuccess ) {
                                                //修改赠送状态
                                                order.setGiveStatus( 1 );
                                                mallOrderDAO.upOrderNoById( order );
                                                task.setIsDelete( 1 );
                                                mallOrderTaskService.updateById( task );

                                                MallOrderTask orderTask = new MallOrderTask();
                                                orderTask.setTaskType( 4 );
                                                orderTask.setOrderId( order.getId() );
                                                orderTask = mallOrderTaskDAO.selectOne( orderTask );
                                                if ( orderTask != null ) {
                                                    // 联盟积分赠送
                                                    unionConsumeService.giveIntegral( 1, order.getId() );

                                                    orderTask.setIsDelete( 1 );
                                                    mallOrderTaskService.updateById( orderTask );
                                                }
                                            }
                                        } catch ( BusinessException be ) {
                                            logger.error( "赠送物品失败，原因：" + be.getMessage() );
                                        } catch ( Exception e ) {
                                            logger.error( "赠送物品失败：" + task.getOrderNo() );
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        } else {//订单不存在，或状态已改变
                            task.setIsDelete( 1 );
                            mallOrderTaskService.updateById( task );
                        }
                    } catch ( BusinessException e ) {
                        logger.error( task.getOrderNo() + "赠送物品异常" + e );
                        e.printStackTrace();
                    }
                }
            }
        } catch ( Exception e ) {
            logger.debug( "订单完成赠送物品bug：" + e.getMessage() );
            e.printStackTrace();
        }
    }

    /*@Scheduled( cron = "0 59 23 * * ?" )*///每天23:59点扫描
    @Override
    public void countIncomeNum() {
        logger.info( "统计每天的收入记录" );
        try {
            Map< String,Object > params = new HashMap<>();
            params.put( "type", "5" );
            List< MallOrderTask > taskList = mallOrderTaskService.findByType( params );
            if ( taskList != null && taskList.size() > 0 ) {
                for ( MallOrderTask task : taskList ) {
                    try {
                        MallOrder order = mallOrderDAO.getOrderById( task.getOrderId() );
                        if ( order != null && order.getOrderStatus() == 4 ) {
                            boolean flag = false;
                            Date updateTime = null;
                            String proName = "";
                            if ( order.getMallOrderDetail().size() > 0 ) {
                                proName = order.getMallOrderDetail().get( 0 ).getDetProName();
                                for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
                                    Integer status = detail.getStatus();
                                    if ( status == -3 || status == 1 || status == 5 || status == -2 ) {
                                        flag = true;
                                        if ( status == 1 || status == 5 || status == -2 ) {
                                            MallOrderReturn orderReturn = new MallOrderReturn();
                                            orderReturn.setOrderDetailId( detail.getId() );
                                            orderReturn = mallOrderReturnDAO.selectByOrderDetailId( orderReturn );

                                            if ( updateTime == null ) {
                                                updateTime = detail.getOrderReturn().getUpdateTime();
                                            } else if ( updateTime.getTime() < orderReturn.getUpdateTime().getTime() ) {
                                                updateTime = orderReturn.getUpdateTime();
                                            }
                                        }
                                    } else {
                                        flag = false;
                                        break;
                                    }
                                }
                                if ( !flag ) {
                                    break;
                                }
                                updateTime = updateTime == null ? order.getUpdateTime() : updateTime;//得到订单完成的最后时间

                                Date time = DateTimeKit.addDate( updateTime, task.getReturnDay() );//等待最终时间
                                Date newDate = new Date();//当前时间
                                if ( newDate.getTime() >= time.getTime() ) {//说明已超过最终时间
                                    //添加交易记录
                                    MallIncomeList incomeList = new MallIncomeList();
                                    incomeList.setBusId( order.getBusUserId() );
                                    incomeList.setIncomeType( 1 );
                                    incomeList.setIncomeCategory( 2 );
                                    incomeList.setIncomeMoney( order.getOrderMoney() );
                                    incomeList.setShopId( order.getShopId() );
                                    incomeList.setBuyerId( order.getBuyerUserId() );
                                    incomeList.setBuyerName( order.getMemberName() );
                                    incomeList.setTradeId( order.getId() );
                                    incomeList.setTradeType( 1 );
                                    incomeList.setProName( proName );
                                    if ( order.getOrderPayWay() == 4 ) {
                                        incomeList.setIncomeUnit( 3 );
                                    } else if ( order.getOrderPayWay() == 8 ) {
                                        incomeList.setIncomeUnit( 2 );
                                    }
                                    incomeList.setProNo( order.getOrderNo() );
                                    incomeList.setCreateTime( new Date() );
                                    mallIncomeListService.insert( incomeList );

                                    task.setIsDelete( 1 );
                                    mallOrderTaskService.updateById( task );
                                }
                            } else {//订单不存在，或状态已改变== 删除任务
                                task.setIsDelete( 1 );
                                mallOrderTaskService.updateById( task );
                            }
                        }
                    } catch ( BusinessException e ) {
                        logger.error( task.getOrderNo() + "统计收入金额异常" + e );
                        e.printStackTrace();
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error( "统计每天的收入金额异常" + e );
            e.printStackTrace();
        }
    }

    /**
     * 自动确认收货(物流签收后超过7天未确认收货，系统自动确认收货)
     */
/*    @Scheduled( cron = "0 0 2 * * ?" )//每天早上2点扫描*/
    @Override
    public void autoConfirmTakeDelivery() {
        try {
            Map< String,Object > params = new HashMap<>();
            params.put( "type", "2" );
            List< MallOrderTask > taskList = mallOrderTaskService.findByType( params );
            if ( taskList != null && taskList.size() > 0 ) {
                for ( MallOrderTask task : taskList ) {
                    try {
                        MallOrder order = mallOrderDAO.selectById( task.getOrderId() );
                        //TODO 签收状态 签收时间
                        boolean isSign = true;//物流是否已签收
                        Date signTime = null;//物流签收时间
                        if ( order != null && order.getOrderStatus() == 3 ) {
                            Date time = DateTimeKit.addDate( signTime, task.getReturnDay() );//等待最终时间
                            Date newDate = new Date();
                            if ( newDate.getTime() >= time.getTime() ) {//说明已超过最终时间
                                order.setOrderStatus( 4 );
                                order.setUpdateTime( new Date() );
                                mallOrderDAO.updateById( order );

                                task.setIsDelete( 1 );
                                mallOrderTaskService.updateById( task );
                            }
                        } else {//订单不存在，或状态已改变
                            task.setIsDelete( 1 );
                            mallOrderTaskService.updateById( task );
                        }
                    } catch ( BusinessException e ) {
                        logger.error( task.getOrderNo() + "自动确认收货异常" + e );
                        e.printStackTrace();
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error( "自动确认收货异常" + e );
            e.printStackTrace();
        }
    }

    /**
     * 取消维权(维权 买家在卖家拒绝后7天内没有回应，则系统自动默认取消维权 )
     */
  /*  @Scheduled( cron = "0/10 * * * * ?" ) // 每10秒执行一次*/
    @Override
    public void cancelReturn() {
        try {
            Map< String,Object > params = new HashMap<>();
            params.put( "type", "6" );
            List< MallOrderTask > taskList = mallOrderTaskService.findByType( params );
            if ( taskList != null && taskList.size() > 0 ) {
                for ( MallOrderTask task : taskList ) {
                    try {
                        MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( task.getOrderReturnId() );
                        if ( orderReturn != null && orderReturn.getStatus() == -1 ) {
                            Date time = DateTimeKit.addDate( orderReturn.getUpdateTime(), task.getReturnDay() );//等待最终时间
                            Date newDate = new Date();
                            if ( newDate.getTime() >= time.getTime() ) {//说明已超过最终时间
                                orderReturn.setStatus( -3 );
                                orderReturn.setUpdateTime( new Date() );
                                mallOrderReturnDAO.updateById( orderReturn );

                                MallOrderDetail detail = new MallOrderDetail();
                                detail.setId( orderReturn.getOrderDetailId() );
                                detail.setStatus( -3 );
                                orderDetailDAO.updateById( detail );

                                task.setIsDelete( 1 );
                                mallOrderTaskService.updateById( task );
                            }
                        } else {//维权单不存在，或状态已改变
                            task.setIsDelete( 1 );
                            mallOrderTaskService.updateById( task );
                        }
                    } catch ( BusinessException e ) {
                        logger.error( task.getOrderNo() + "取消维权异常" + e );
                        e.printStackTrace();
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error( "取消维权异常" + e );
            e.printStackTrace();
        }
    }

    /**
     * 自动退款给买家 (买家申请退款，卖家没有响应，7天后系统自动退款给买家)
     */
    @Override
    public void autoRefund() {
        try {
            Map< String,Object > params = new HashMap<>();
            params.put( "type", "7" );
            List< MallOrderTask > taskList = mallOrderTaskService.findByType( params );
            if ( taskList != null && taskList.size() > 0 ) {
                for ( MallOrderTask task : taskList ) {
                    try {
                        MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( task.getOrderReturnId() );
                        if ( orderReturn != null && orderReturn.getStatus() == 0 && orderReturn.getRetHandlingWay() == 1 ) {
                            Date time = DateTimeKit.addDate( orderReturn.getUpdateTime(), task.getReturnDay() );//等待最终时间
                            Date newDate = new Date();
                            if ( newDate.getTime() >= time.getTime() ) {//说明已超过最终时间
                                orderReturn.setStatus( 1 );
                                orderReturn.setUpdateTime( new Date() );
                                mallOrderReturnDAO.updateById( orderReturn );

                                MallOrderDetail detail = new MallOrderDetail();
                                detail.setId( orderReturn.getOrderDetailId() );
                                detail.setStatus( 1 );
                                orderDetailDAO.updateById( detail );

                                task.setIsDelete( 1 );
                                mallOrderTaskService.updateById( task );
                            }
                        } else {//维权单不存在，或状态已改变
                            task.setIsDelete( 1 );
                            mallOrderTaskService.updateById( task );
                        }
                    } catch ( BusinessException e ) {
                        logger.error( task.getOrderNo() + "自动退款给买家异常" + e );
                        e.printStackTrace();
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error( "自动退款给买家异常" + e );
            e.printStackTrace();
        }

    }

    /**
     * 自动确认收货并退款至买家(买家退货物流,若卖家超出10天不做操作，系统自动确认卖家
     * 收货并结算至买家账户)
     */
    @Override
    public void returnGoodsByRefund() {
        try {
            Map< String,Object > params = new HashMap<>();
            params.put( "type", "8" );
            List< MallOrderTask > taskList = mallOrderTaskService.findByType( params );
            if ( taskList != null && taskList.size() > 0 ) {
                for ( MallOrderTask task : taskList ) {
                    try {
                        MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( task.getOrderReturnId() );
                        if ( orderReturn != null && orderReturn.getStatus() == 3 && orderReturn.getRetHandlingWay() == 2 ) {
                            Date time = DateTimeKit.addDate( orderReturn.getUpdateTime(), task.getReturnDay() );//等待最终时间
                            Date newDate = new Date();
                            if ( newDate.getTime() >= time.getTime() ) {//说明已超过最终时间
                                orderReturn.setStatus( 5 );
                                orderReturn.setUpdateTime( new Date() );
                                mallOrderReturnDAO.updateById( orderReturn );

                                MallOrderDetail detail = new MallOrderDetail();
                                detail.setId( orderReturn.getOrderDetailId() );
                                detail.setStatus( 5 );
                                orderDetailDAO.updateById( detail );

                                task.setIsDelete( 1 );
                                mallOrderTaskService.updateById( task );
                            }
                        } else {//维权单不存在，或状态已改变
                            task.setIsDelete( 1 );
                            mallOrderTaskService.updateById( task );
                        }
                    } catch ( BusinessException e ) {
                        logger.error( task.getOrderNo() + "自动确认收货并退款至买家异常" + e );
                        e.printStackTrace();
                    }
                }
            }
        } catch ( Exception e ) {
            logger.error( "自动确认收货并退款至买家异常" + e );
            e.printStackTrace();
        }
    }

    public boolean addInvNum( String orderId ) {
        boolean flag = false;
        Map< String,Object > params = new HashMap< String,Object >();
        params.put( "id", orderId );
        //		params.put("status", 1);
        // 查询所有未支付的订单
        MallOrder order = mallOrderDAO.selectOrderById( params );

        String invKey = Constants.REDIS_SECKILL_NAME;// 秒杀库存的key
        if ( order != null ) {
            if ( order.getOrderType() == 3 ) {// 秒杀订单  恢复库存
                if ( order.getMallOrderDetail() != null ) {
                    String seckill = order.getGroupBuyId().toString();
                    MallOrderDetail detail = order.getMallOrderDetail().get( 0 );
                    if ( order.getOrderStatus() == 1 ) {//未支付的才恢复库存
                        if ( JedisUtil.hExists( invKey, seckill ) ) {
                            // 恢复商品库存
                            int invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, seckill ) );
                            JedisUtil.map( invKey, seckill, ( invNum + 1 ) + "" );
                        }
                        if ( CommonUtil.isNotEmpty( detail.getProductSpecificas() ) ) {
                            String specificas = detail.getProductSpecificas();
                            String field = seckill + "_" + specificas;
                            if ( JedisUtil.hExists( invKey, field ) ) {
                                // 恢复商品规格库存
                                int invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, field ) );
                                JedisUtil.map( invKey, field, ( invNum + 1 ) + "" );
                            }
                        }
                    } else {
                        //减库存
                        diffInvNum( detail );
                    }
                }
            }
            boolean result = true;
            String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
            String times = DateTimeKit.format( order.getCreateTime(), format );
            String eDate = DateTimeKit.format( new Date(), format );
            long hour = DateTimeKit.minsBetween( times, eDate, 3600000, format );
            if ( order.getOrderPayWay() == 7 && order.getOrderStatus() == 1 ) {//未付款的订单24小时后才关闭订单
                if ( hour > 24 ) {
                    result = true;
                } else {
                    result = false;
                }
            }
            if ( order.getOrderType() == 4 && order.getOrderStatus() == 1 && result ) { //拍卖订单72小时未支付成功后才关闭订单
                if ( hour > 72 ) {//拍卖72小时未付款才关闭订单
                    result = true;
                } else {
                    result = false;
                }
            }
            if ( order.getOrderStatus() == 1 && result ) {
                params = new HashMap< String,Object >();
                params.put( "id", order.getId() );// 关闭订单的时间 （30分钟）
                mallOrderDAO.updateByNoMoney( params );
            }
            flag = result;
        }
        return flag;
    }

    public void diffInvNum( MallOrderDetail detail ) {
        String proSpecificas = detail.getProductSpecificas();
        Integer proId = detail.getProductId();
        Integer productNum = detail.getDetProNum();
        //获取商品的库存和销售量
        MallProduct product = mallProductDAO.selectById( proId );
        if ( product != null ) {
            int invNum = product.getProStockTotal();//库存
            int saleNum = 0;//销量
            int isSpec = 0;//是否有规格
            if ( CommonUtil.isNotEmpty( product.getProSaleTotal() ) ) {
                saleNum = product.getProSaleTotal();
            }
            if ( CommonUtil.isNotEmpty( product.getIsSpecifica() ) ) {
                isSpec = product.getIsSpecifica();
            }

            MallProduct product1 = new MallProduct();
            product1.setId( proId );
            product1.setProStockTotal( invNum - productNum );
            product1.setProSaleTotal( saleNum + productNum );
            mallProductDAO.updateById( product1 );

            Map< String,Object > params = new HashMap< String,Object >();
            params.put( "id", proId );

            if ( isSpec == 1 ) {//该商品存在规格
                String specIds = "";
                //获取规格的库存和销售额
                for ( String str : proSpecificas.split( "," ) ) {
                    if ( str != null && !str.equals( "" ) ) {
                        params.put( "valueId", str );
                        List< Map< String,Object > > specList = mallProductSpecificaDAO.selectProductSpecById( params );
                        if ( specList != null && specList.size() > 0 ) {
                            for ( Map< String,Object > map : specList ) {
                                if ( !specIds.equals( "" ) ) {
                                    specIds += ",";
                                }
                                specIds += map.get( "id" ).toString();
                            }
                        }
                    }
                }
                params.put( "specId", specIds );
                Map< String,Object > invMap = mallProductInventoryDAO.selectProductInvenById( params );
                if ( invMap != null ) {
                    int invStockNum = 0;//库存
                    if ( CommonUtil.isNotEmpty( invMap.get( "inv_num" ) ) ) {
                        invStockNum = CommonUtil.toInteger( invMap.get( "inv_num" ).toString() );
                    }
                    int invSaleNum = 0;//销量
                    if ( CommonUtil.isNotEmpty( invMap.get( "inv_sale_num" ) ) ) {
                        invSaleNum = CommonUtil.toInteger( invMap.get( "inv_sale_num" ).toString() );
                    }
                    int invId = CommonUtil.toInteger( invMap.get( "id" ) );
                    invStockNum = invStockNum - productNum;
                    invSaleNum = invSaleNum + productNum;

                    MallProductInventory inventory = new MallProductInventory();
                    inventory.setId( invId );
                    inventory.setInvNum( invStockNum );
                    inventory.setInvSaleNum( invSaleNum );
                    mallProductInventoryDAO.updateById( inventory );
                }
            }
        }
    }
}
