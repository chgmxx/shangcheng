package com.gt.mall.service.quartz.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallCountIncomeDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.page.MallPageDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.product.MallProductDaycountDAO;
import com.gt.mall.dao.product.MallProductMonthcountDAO;
import com.gt.mall.dao.seckill.MallSeckillDAO;
import com.gt.mall.dao.store.MallShopDaycountDAO;
import com.gt.mall.dao.store.MallShopMonthcountDAO;
import com.gt.mall.entity.basic.MallCountIncome;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallProductDaycount;
import com.gt.mall.entity.product.MallProductMonthcount;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.entity.store.MallShopDaycount;
import com.gt.mall.entity.store.MallShopMonthcount;
import com.gt.mall.service.quartz.MallQuartzNewService;
import com.gt.mall.service.web.order.QuartzOrderService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.MultipleJedisUtil;
import com.gt.mall.utils.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 定时任务服务类
 */
@Service
@Component
public class MallQuartzNewServiceImpl implements MallQuartzNewService {

    private Logger logger = Logger.getLogger( MallQuartzServiceImpl.class );

    @Autowired
    private MallOrderReturnDAO       mallOrderReturnDAO;
    @Autowired
    private MallSeckillDAO           mallSeckillDAO;
    @Autowired
    private QuartzOrderService       quartzOrderService;
    @Autowired
    private MallProductDAO           mallProductDAO;
    @Autowired
    private MallPageDAO              mallPageDAO;
    @Autowired
    private MallCountIncomeDAO       mallCountIncomeDAO;
    @Autowired
    private MallOrderDAO             mallOrderDAO;
    @Autowired
    private MallOrderDetailDAO       orderDetailDAO;
    @Autowired
    private MallProductDaycountDAO   mallProductDaycountDAO;
    @Autowired
    private MallProductMonthcountDAO mallProductMonthcountDAO;
    @Autowired
    private MallShopDaycountDAO      mallShopDaycountDAO;
    @Autowired
    private MallShopMonthcountDAO    mallShopMonthcountDAO;

    private static String ip  = PropertiesUtil.getRedisHost();
    private static String pwd = PropertiesUtil.getRedisPassword();

    /**
     * 订单完成赠送物品  每天早上8点扫描
     */
    //	@Scheduled(cron = "0 0/1 * * * ?")
    @Scheduled( cron = "0 0 8 * * ?" )
    @Override
    public void orderFinish() {
	System.out.println( "开始赠送物品" );
	try {
	    List< MallOrder > orderList = mallOrderDAO.selectOrderByFinish();
	    if ( orderList != null && orderList.size() > 0 ) {
		for ( MallOrder order : orderList ) {
		    if ( order != null ) {
			Map< String,Object > map = new HashMap< String,Object >();
			map.put( "id", order.getId() );
			List< Map< String,Object > > dList = mallOrderDAO.selectRealOrderDetail( map );
			boolean flag = false;
			if ( dList != null && dList.size() > 0 ) {
			    for ( int j = 0; j < dList.size(); j++ ) {
				Map< String,Object > dMap = dList.get( j );
				String status = dMap.get( "status" ).toString();
				if ( status.equals( "-3" ) || status.equals( "1" ) || status.equals( "5" ) || status.equals( "-2" ) ) {
				    flag = true;
				} else {
				    flag = false;
				    break;
				}
			    }
			    if ( flag ) {
				String orderNo = order.getOrderNo();
				//TODO 发送赠送物品给用户
				/*memberPayService.giveGood( orderNo );//赠送状态*/

				//修改赠送状态
				order.setGiveStatus( 1 );
				mallOrderDAO.upOrderNoById( order );

				//mOrderService.insertPayLog(order);//添加支付有礼的日志信息
				//TODO 联盟积分赠送
				/*unionQuartzService.updateUnionMemberCardIntegral( 1, order.getId() );*/
			    }
			}

		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.debug( "订单完成赠送物品bug：" + e.getMessage() );
	    e.printStackTrace();
	}
    }

    /**
     * 关闭30分钟内未支付的订单
     */
    @Scheduled( cron = "0 0/30 * * * ?" )//三十分钟更新一次
    @Override
    public void closeOrderNoPay() {
	logger.info( "开始扫描30分钟内未支付的订单" );
	try {
	    String key = Constants.REDIS_KEY + "hSeckill_nopay";
	    quartzOrderService.closeOrderNoPay( key, ip, pwd );

	} catch ( Exception e ) {
	    logger.error( "扫描30分钟内未支付的秒杀订单异常" + e );
	    e.printStackTrace();
	}

	try {
	    String key = Constants.REDIS_KEY + "hOrder_nopay";
	    quartzOrderService.closeOrderNoPay( key, ip, pwd );
	} catch ( Exception e ) {
	    logger.error( "扫描30分钟内未支付的订单异常" + e );
	    e.printStackTrace();
	}

	try {
	    quartzOrderService.closeOrderByDaoDian();
	} catch ( Exception e ) {
	    logger.error( "关闭到店支付订单异常" + e );
	    e.printStackTrace();
	}
    }

    /**
     * 扫描已经结束的秒杀信息
     */
    @Scheduled( cron = "0 0 2 * * ?" )//每天早上2点扫描
    //	@Scheduled(cron = "0 0/1 * * * ?")//每隔50分钟扫描
    @Override
    public void endSeckill() {
	logger.info( "开始扫描已经结束的秒杀信息" );
	try {
	    String key = Constants.REDIS_KEY + "hSeckill";
	    int seckId = 0;
	    if ( MultipleJedisUtil.exists( key, ip, pwd ) ) {
		Map< String,String > map = MultipleJedisUtil.mapGetAll( key, ip, pwd );
		if ( map != null ) {
		    Set< String > set = map.keySet();
		    for ( String str : set ) {
			if ( str != null && !str.equals( "" ) ) {
			    String[] spli = str.split( "_" );
			    if ( spli.length > 0 && !spli[0].equals( "" ) ) {
				seckId = Integer.valueOf( spli[0] );
				//跟根据秒杀id查询是否已经结束
				MallSeckill seckill = mallSeckillDAO.selectById( seckId );
				if ( seckill.getStatus() == -1 || seckill.getIsDelete().toString().equals( "1" ) ) {
				    logger.error( "订单：" + str + "，已经结束" );
				    //秒杀结束，从redis中删除对应的秒杀信息
				    MultipleJedisUtil.mapdel( key, str, ip, pwd );
				}
			    }
			}
		    }
		}
	    }

	} catch ( Exception e ) {
	    logger.error( "扫描已经结束的秒杀信息异常" + e );
	    e.printStackTrace();
	}

	logger.info( "开始扫描已发货未确认收货的订单信息" );
	try {
	    List< Map< String,Object > > list = mallOrderDAO.selectRealOrder();
	    if ( list != null && list.size() > 0 ) {
		for ( int i = 0; i < list.size(); i++ ) {
		    Map< String,Object > map = list.get( i );
		    List< Map< String,Object > > dList = mallOrderDAO.selectRealOrderDetail( map );
		    boolean flag = false;
		    if ( dList != null && dList.size() > 0 ) {
			for ( int j = 0; j < dList.size(); j++ ) {
			    Map< String,Object > dMap = dList.get( j );
			    String status = dMap.get( "status" ).toString();
			    if ( status.equals( "-3" ) || status.equals( "1" ) || status.equals( "5" ) || status.equals( "-2" ) ) {
				flag = true;
			    } else {
				flag = false;
				break;
			    }
			}
			if ( flag ) {
			    map.put( "order_status", 4 );
			    mallOrderDAO.updateByOrder( map );
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "扫描已发货未确认收货的订单信息异常" + e );
	    e.printStackTrace();
	}

	logger.info( "开始扫描完成的订单预售送礼" );
	try {
	    List< Map< String,Object > > list = mallOrderDAO.selectFinishOrder();
	    if ( list != null && list.size() > 0 ) {
		for ( Map< String,Object > map : list ) {
		    System.out.println( "orderId:" + map.get( "id" ) );
		    List< Map< String,Object > > dList = mallOrderDAO.selectRealOrderDetail( map );
		    boolean flag = false;
		    if ( dList != null && dList.size() > 0 ) {
			for ( Map< String,Object > dMap : dList ) {
			    flag = quartzOrderService.isOrderReturn( map, dMap );
			    if ( !flag ) {
				break;
			    }
			}
		    }
		    if ( flag ) {
			if ( CommonUtil.isNotEmpty( map.get( "order_type" ) ) && CommonUtil.isNotEmpty( map.get( "group_buy_id" ) ) ) {
			    int orderType = CommonUtil.toInteger( map.get( "order_type" ) );
			    int groupBuyId = CommonUtil.toInteger( map.get( "group_buy_id" ) );
			    if ( orderType == 6 && groupBuyId > 0 ) {
				quartzOrderService.presaleOrderGive( map );
			    }
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "扫描完成的订单预售送礼" + e );
	    e.printStackTrace();
	}

	logger.info( "开始扫描预售佣金发放" );
	try {
	    List< Map< String,Object > > list = mallOrderDAO.selectOrderNoCommisssion();
	    if ( list != null && list.size() > 0 ) {
		for ( Map< String,Object > map : list ) {
		    System.out.println( "orderId:" + map.get( "id" ) );
		    List< Map< String,Object > > dList = mallOrderDAO.selectRealOrderDetail( map );
		    boolean flag = false;
		    if ( dList != null && dList.size() > 0 ) {
			for ( Map< String,Object > dMap : dList ) {
			    flag = quartzOrderService.isOrderReturn( map, dMap );
			    if ( flag ) {
				quartzOrderService.sellerIncome( dMap, map );//销售佣金的发放
			    } else {
				break;
			    }
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "开始扫描预售佣金发放异常" + e );
	    e.printStackTrace();
	}

	logger.info( "开始同步商品浏览量" );
	try {
	    String key = Constants.REDIS_KEY + "proViewNum";
	    if ( MultipleJedisUtil.exists( key, ip, pwd ) ) {
		Map< String,String > map = MultipleJedisUtil.mapGetAll( key, ip, pwd );
		if ( map != null ) {
		    Set< String > set = map.keySet();
		    for ( String str : set ) {
			String numStr = map.get( str ).toString();
			if ( numStr != null && !numStr.equals( "" ) && !numStr.equals( "0" ) ) {
			    Map< String,Object > params = new HashMap< String,Object >();
			    params.put( "id", str );
			    params.put( "viewsNum", numStr );
			    mallProductDAO.updateProViewNum( params );
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "同步商品浏览量异常" + e );
	    e.printStackTrace();
	}
	logger.info( "开始扫描未支付的订单" );
	try {
	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "minuteTime", 30 );// 关闭订单的时间 （30分钟）
	    params.put( "status", -1 );
	    mallOrderDAO.updateByNoMoney( params );
	} catch ( Exception e ) {
	    logger.error( "修改未支付的订单异常" + e );
	    e.printStackTrace();
	}
    }

    @Scheduled( cron = "0 59 23 * * ?" )//每天23:59点扫描
    @Override
    public void countIncomeNum() {
	logger.info( "统计每天营业额" );
	try {
	    String key = Constants.REDIS_KEY + "todayIncomeCount";
	    //判断是否存在页面访问数量
	    if ( MultipleJedisUtil.exists( key, ip, pwd ) ) {
		Map< String,String > map = MultipleJedisUtil.mapGetAll( key, ip, pwd );//获取所有店铺营业额
		if ( map != null ) {
		    Set< String > set = map.keySet();
		    for ( String shopId : set ) {
			String oStr = map.get( shopId );//店铺营业额
			if ( CommonUtil.isNotEmpty( oStr ) ) {
			    JSONObject obj = JSONObject.parseObject( oStr );
			    Integer tradePrice = obj.getInteger( "tradePrice" ); //当天交易金额
			    Integer refundPrice = obj.getInteger( "refundPrice" );//当天退款金额
			    //查询 当前时间 大于等于 7天后的时间
			    double incomePrice = mallOrderDAO.selectOrderFinishMoneyByShopId( CommonUtil.toInteger( shopId ) );

			    //保存
			    MallCountIncome income = new MallCountIncome();
			    income.setBusId( 42 );
			    income.setShopId( CommonUtil.toInteger( shopId ) );
			    income.setCountDate( new Date() );
			    income.setTradePrice( CommonUtil.toBigDecimal( tradePrice ) );
			    income.setRefundPrice( CommonUtil.toBigDecimal( refundPrice ) );
			    income.setTurnover( CommonUtil.toBigDecimal( tradePrice ).subtract( CommonUtil.toBigDecimal( refundPrice ) ) );
			    income.setIncomePrice( CommonUtil.toBigDecimal( incomePrice ) );//统计订单完成7天后的金额
			    mallCountIncomeDAO.insert( income );

			    //清空统计
			    MultipleJedisUtil.mapdel( key, shopId, ip, pwd );
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "统计每天页面访问数量异常" + e );
	    e.printStackTrace();
	}
    }

    /**
     * 统计每天页面访问数量
     */
    @Scheduled( cron = "0 0 23 * * ?" )//每天23点扫描
    @Override
    public void countPageVisitorNum() {
	logger.info( "统计每天页面访问数量" );
	try {
	    String key = Constants.REDIS_KEY + "pageVisitor";
	    //判断是否存在页面访问数量
	    if ( MultipleJedisUtil.exists( key, ip, pwd ) ) {
		Map< String,String > map = MultipleJedisUtil.mapGetAll( key, ip, pwd );//获取所有页面访问数量
		if ( map != null ) {
		    Set< String > set = map.keySet();
		    for ( String pageId : set ) {
			String oStr = map.get( pageId );//页面访问数量
			if ( CommonUtil.isNotEmpty( oStr ) ) {
			    JSONObject obj = JSONObject.parseObject( oStr );
			    Integer visitorNum = obj.getInteger( "visitorNum" ); //访问数
			    Integer viewsNum = obj.getInteger( "viewsNum" );//浏览量
			    MallPage page = mallPageDAO.selectById( pageId );
			    //保存
			    if ( page != null ) {
				page.setViewsNum( page.getViewsNum() + viewsNum );
				page.setVisitorNum( page.getVisitorNum() + visitorNum );
				mallPageDAO.updateById( page );
			    }
			    //清空统计
			    MultipleJedisUtil.mapdel( key, pageId, ip, pwd );
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "统计每天页面访问数量异常" + e );
	    e.printStackTrace();
	}
    }

    /**
     * 关闭未付款认单 (拍下未付款订单规定时间内买家不付款，自动取消订单)
     */
    @Override
    public void closeNoPayOrder() {
	try {
	    String key = Constants.REDIS_KEY + "hOrder_nopay";
	    quartzOrderService.newCloseOrderNoPay( key, ip, pwd );
	} catch ( Exception e ) {
	    logger.error( "扫描未支付的订单异常" + e );
	    e.printStackTrace();
	}
    }

    /**
     * 自动确认收货(物流签收后超过7天未确认收货，系统自动确认收货)
     */
/*    @Scheduled( cron = "0 0 2 * * ?" )//每天早上2点扫描*/
    @Override
    public void autoConfirmTakeDelivery() {
	//TODO 签收状态 签收时间
	boolean isSign = true;//物流是否已签收
	String signTime = "";//物流签收时间

	Wrapper< MallOrder > wrapper = new EntityWrapper<>();
	//查询 当前时间 大于等于 7天后的时间
	wrapper.where( "order_status = 3 and SYSDATE() >= DATE_ADD({0}, INTERVAL 7 DAY)", signTime );
	List< MallOrder > orderList = mallOrderDAO.selectList( wrapper );
	if ( orderList != null && orderList.size() > 0 ) {
	    for ( MallOrder order : orderList ) {
		order.setOrderStatus( 4 );
		order.setUpdateTime( new Date() );
		mallOrderDAO.updateById( order );
	    }
	}
    }

    /**
     * 取消维权(维权 买家在卖家拒绝后7天内没有回应，则系统自动默认取消维权 )
     */
  /*  @Scheduled( cron = "0/10 * * * * ?" ) // 每10秒执行一次*/
    @Override
    public void cancelReturn() {
	Date date1 = DateTimeKit.addDays( -7 );
	String date3 = DateTimeKit.format( date1 );//7天后的时间日期

	Wrapper< MallOrderReturn > wrapper = new EntityWrapper<>();
	//查询 当前时间 大于等于 7天后的时间
	wrapper.where( "status = -1 and SYSDATE() >= DATE_ADD(update_time, INTERVAL 7 DAY)" );
	List< MallOrderReturn > returnList = mallOrderReturnDAO.selectList( wrapper );
	if ( returnList != null && returnList.size() > 0 ) {
	    for ( MallOrderReturn orderReturn : returnList ) {
		orderReturn.setStatus( -3 );
		orderReturn.setUpdateTime( new Date() );
		mallOrderReturnDAO.updateById( orderReturn );
	    }
	}
    }

    /**
     * 自动退款给买家 (买家申请退款，卖家没有响应，7天后系统自动退款给买家)
     */
    @Override
    public void autoRefund() {
	Wrapper< MallOrderReturn > wrapper = new EntityWrapper<>();
	//查询  当前时间 大于等于 7天后的时间
	wrapper.where( "status = 0 and ret_handling_way = 1 and SYSDATE() >= DATE_ADD(update_time, INTERVAL 7 DAY)" );
	List< MallOrderReturn > returnList = mallOrderReturnDAO.selectList( wrapper );
	if ( returnList != null && returnList.size() > 0 ) {
	    for ( MallOrderReturn orderReturn : returnList ) {
		orderReturn.setStatus( 1 );
		orderReturn.setUpdateTime( new Date() );
		mallOrderReturnDAO.updateById( orderReturn );
		// TODO 退款
	    }
	}

    }

    /**
     * 自动确认收货并退款至买家  (买家退货物流,若卖家超出10天不做操作，系统自动确认卖家
     * 收货并结算至买家账户)
     */
    @Override
    public void returnGoodsByRefund() {
	Wrapper< MallOrderReturn > wrapper = new EntityWrapper<>();
	//查询 当前时间 大于等于 10天后的时间
	wrapper.where( "status = 3 and ret_handling_way = 2 and SYSDATE() >= DATE_ADD(update_time, INTERVAL 10 DAY)" );
	List< MallOrderReturn > returnList = mallOrderReturnDAO.selectList( wrapper );
	if ( returnList != null && returnList.size() > 0 ) {
	    for ( MallOrderReturn orderReturn : returnList ) {
		orderReturn.setStatus( 5 );
		orderReturn.setUpdateTime( new Date() );
		mallOrderReturnDAO.updateById( orderReturn );
		// TODO 退款
	    }
	}
    }

    //统计商城信息
    @Scheduled( cron = "0 0 1 * * ?" )
    @Override
    public void mallcount() {
	if ( ip.equals( "183.47.242.2" ) ) {//多粉
	    try {
		Date date = DateTimeKit.getNow();//获取当前日期
		Date date1 = DateTimeKit.addDays( -1 );//昨天日期
		String date2 = DateTimeKit.format( date );//当天的时间日期
		String date3 = DateTimeKit.format( date1 );//昨天的时间日期

		//1.当天商品- 修改商品的数量和金额
		todayProduct( date3, date2 );
		//2.退款商品 （单商品的退款数量与金额）
		productReturn( date3, date2 );
		//3.店铺退款
		shopReturnCount( date3 );
		//4.店铺扫码支付
		shopScanPay( date3, date2 );

		int day = DateTimeKit.getCurrentDay();//获取当前日
		//如果当前日是1号，就开始统计商品每月的数据图和店铺的销售情况
		if ( day == 1 ) {
		    int year = DateTimeKit.getCurrentYear();//获取当前年份
		    int month = DateTimeKit.getCurrentMonth();//获取当前月份
		    String time = DateTimeKit.getFirstDayOfsMonth( year, month );//获取上一个月的第一天
		    String syear = time.split( "-" )[0];//上一个月的年份
		    String smonth = time.split( "-" )[1];//上一个月的月份

		    //5.上月销售商品
		    lastMonthSaleProduct( date2, time, syear, smonth );

		    //6.统计店铺每月销售情况
		    getMonthShopSale( date2, time, syear, smonth );
		}
	    } catch ( Exception e ) {
		System.out.println( "定时统计商城报错" );
		e.printStackTrace();
	    }
	} else {//翼粉
	    try {
		//TODO 多数据源
		Date date = DateTimeKit.getNow();//获取当前日期
		Date date1 = DateTimeKit.addDays( -1 );//昨天日期
		String date2 = DateTimeKit.format( date );//当天的时间日期
		String date3 = DateTimeKit.format( date1 );//昨天的时间日期

		//1.当天商品- 修改商品的数量和金额
		todayProduct( date3, date2 );
		//2.退款商品 （单商品的退款数量与金额）
		productReturn( date3, date2 );
		//3.店铺退款
		shopReturnCount( date3 );
		//4.店铺扫码支付
		shopScanPay( date3, date2 );

		int day = DateTimeKit.getCurrentDay();//获取当前日
		//如果当前日是1号，就开始统计商品每月的数据图和店铺的销售情况
		if ( day == 1 ) {
		    int year = DateTimeKit.getCurrentYear();//获取当前年份
		    int month = DateTimeKit.getCurrentMonth();//获取当前月份
		    String time = DateTimeKit.getFirstDayOfsMonth( year, month );//获取上一个月的第一天
		    String syear = time.split( "-" )[0];//上一个月的年份
		    String smonth = time.split( "-" )[1];//上一个月的月份

		    //5.上月销售商品
		    lastMonthSaleProduct( date2, time, syear, smonth );

		    //6.统计店铺每月销售情况
		    getMonthShopSale( date2, time, syear, smonth );
		}
	    } catch ( Exception e ) {
		System.out.println( "定时统计商城报错" );
		e.printStackTrace();
	    }
	}
	//
    }

    //1.当天商品
    void todayProduct( String startTime, String endTime ) {
	/* 获取当天所有的商品id*/
	List< Map< String,Object > > list = orderDetailDAO.selectTodayProduct( startTime, endTime );

	for ( int i = 0; i < list.size(); i++ ) {
	    MallProductDaycount obj = new MallProductDaycount();
	    Map< String,Object > map = list.get( i );
	    Integer product_id = Integer.valueOf( map.get( "product_id" ).toString() );//获取商品id
	    Integer wx_shop_id = Integer.valueOf( map.get( "wx_shop_id" ).toString() );//获取店铺id
	    obj.setProductId( product_id );
	    obj.setShopId( wx_shop_id );
	    //获取单商品微信支付的数量和金额
	    List< Map< String,Object > > wxlist = orderDetailDAO.selectProductMoneyByWay( startTime, endTime, product_id, 1 );
	    Integer saleNum = 0;//默认数量0
	    Double salePrice = 0.00;//默认价钱0.00
	    if ( wxlist.size() > 0 ) {
		Map< String,Object > wxmap = wxlist.get( 0 );
		if ( wxmap.get( "wxnum" ) != null && !wxmap.get( "wxnum" ).equals( "" ) ) {
		    saleNum = Integer.valueOf( wxmap.get( "wxnum" ).toString() );
		    salePrice = Double.parseDouble( wxmap.get( "wxprice" ).toString() );
		}
	    }
	    obj.setSaleMiprice( new BigDecimal( salePrice ) );
	    obj.setSaleNum( saleNum );
	    /*获取单商品微信支付的数量和金额*/
	    List< Map< String,Object > > milist = orderDetailDAO.selectProductMoneyByWay( startTime, endTime, product_id, 3 );
	    Integer saleMinum = 0;//默认数量0
	    Double saleMiprice = 0.00;//默认价钱0.00
	    if ( wxlist.size() > 0 ) {
		Map< String,Object > mimap = milist.get( 0 );
		if ( mimap.get( "minum" ) != null && !mimap.get( "minum" ).equals( "" ) ) {
		    saleMinum = Integer.valueOf( mimap.get( "minum" ).toString() );
		    saleMiprice = Double.parseDouble( mimap.get( "miprice" ).toString() );
		}
	    }
	    obj.setSaleMinum( saleMinum );
	    obj.setSaleMiprice( new BigDecimal( saleMiprice ) );
	    obj.setCountDate( startTime );
	    obj.setSaleTotalnum( saleMinum + saleNum );
	    obj.setSaleTotalprice( new BigDecimal( saleMiprice + salePrice ) );
	    mallProductDaycountDAO.insert( obj );
	}
    }

    //2.退款商品 （单商品的退款数量与金额）
    void productReturn( String startTime, String endTime ) {
	/*退款的所有商品*/
	List< Map< String,Object > > tklist = mallOrderReturnDAO.selectAllReturnProduct( startTime, endTime );
	for ( int k = 0; k < tklist.size(); k++ ) {
	    MallProductDaycount obj = new MallProductDaycount();
	    Map< String,Object > tkmap = tklist.get( k );//获取退款的商品
	    Integer product_id = Integer.valueOf( tkmap.get( "product_id" ).toString() );//获取商品id
	    Integer wx_shop_id = Integer.valueOf( tkmap.get( "wx_shop_id" ).toString() );//获取店铺id
	    /*获取单商品微信支付退款的数量和金额*/
	    List< Map< String,Object > > wxtklist = mallOrderReturnDAO.selectProductMoneyByWay( startTime, endTime, product_id, 1 );
	    Integer refundNum = 0;//默认数量0
	    Double refundPrice = 0.00;//默认价钱0.00
	    if ( wxtklist.size() > 0 ) {
		Map< String,Object > wxtkmap = wxtklist.get( 0 );
		if ( wxtkmap.get( "tkwxnum" ) != null && !wxtkmap.get( "tkwxnum" ).equals( "" ) ) {
		    refundNum = Integer.valueOf( wxtkmap.get( "tkwxnum" ).toString() );
		    refundPrice = Double.parseDouble( wxtkmap.get( "tkwxprice" ).toString() );
		}
	    }
	    /*获取单商品会员退款的数量和金额*/
	    List< Map< String,Object > > mitklist = mallOrderReturnDAO.selectProductMoneyByWay( startTime, endTime, product_id, 3 );
	    Integer refundMinum = 0;//默认数量0
	    Double refundMiprice = 0.00;//默认价钱0.00
	    if ( wxtklist.size() > 0 ) {
		Map< String,Object > mitkmap = mitklist.get( 0 );
		if ( mitkmap.get( "tkminum" ) != null && !mitkmap.get( "tkminum" ).equals( "" ) ) {
		    refundMinum = Integer.valueOf( mitkmap.get( "tkminum" ).toString() );
		    refundMiprice = Double.parseDouble( mitkmap.get( "tkmiprice" ).toString() );
		}
	    }
	    /*检验商品是否存在*/
	    List< Map< String,Object > > listsele = mallProductDaycountDAO.isProductByDate( startTime, product_id );//检验商品是否存在
	    obj.setProductId( product_id );
	    obj.setShopId( wx_shop_id );
	    obj.setRefundNum( refundNum );
	    obj.setRefundPrice( new BigDecimal( refundPrice ) );
	    obj.setRefundMinum( refundMinum );
	    obj.setRefundMiprice( new BigDecimal( refundMiprice ) );
	    obj.setRefundTotalnum( refundNum + refundMinum );
	    obj.setRefundTotalprice( new BigDecimal( refundMiprice + refundPrice ) );
	    obj.setCountDate( startTime );
	    //如果产品存在,修改之前的退款信息，不存在的话。直接添加新数据
	    if ( listsele.size() > 0 ) {
		Integer id = Integer.valueOf( listsele.get( 0 ).get( "id" ).toString() );
		obj.setId( id );
		mallProductDaycountDAO.updatetk( obj );//修改数据
	    } else {
		mallProductDaycountDAO.insert( obj );//添加数据
	    }
	}

    }

    //3.店铺退款
    void shopReturnCount( String startTime ) {
	//统计店铺每天的销售，退款情况
	List< Map< String,Object > > shoplist = mallProductDaycountDAO.selectShopIdByDate( startTime );
	for ( int x = 0; x < shoplist.size(); x++ ) {
	    Map< String,Object > shopmap = shoplist.get( x );
	    List< Map< String,Object > > shopcountlist = mallProductDaycountDAO.selectShopReturnListByDate( startTime, CommonUtil.toInteger( shopmap.get( "shop_id" ) ) );
	    if ( shopcountlist.size() > 0 ) {
		Map< String,Object > shopcountmap = shopcountlist.get( 0 );
		MallShopDaycount obj = (MallShopDaycount) net.sf.json.JSONObject.toBean( net.sf.json.JSONObject.fromObject( shopcountmap ), MallShopDaycount.class );
		obj.setShopId( Integer.valueOf( shopmap.get( "shop_id" ).toString() ) );
		obj.setCountDate( startTime );
		mallShopDaycountDAO.insert( obj );
	    }
	}
    }

    //4.店铺扫码支付
    void shopScanPay( String startTime, String endTime ) {

	/*获取当天店铺扫码支付情况，*/
	List< Map< String,Object > > sqldisshoplist = mallOrderDAO.selectTodayShopByTime( startTime, endTime );
	for ( int s = 0; s < sqldisshoplist.size(); s++ ) {
	    Map< String,Object > map = sqldisshoplist.get( s );//获取每天扫码支付店铺

	    Map< String,Object > mapcount = mallOrderDAO.countTodayShopByTime( startTime, endTime, CommonUtil.toInteger( map.get( "shop_id" ) ) );

	    int a = mallShopDaycountDAO.isShopByDate( startTime, CommonUtil.toInteger( map.get( "shop_id" ) ) );//检查之前是否有该数据
	    if ( a == 0 ) {
		MallShopDaycount obj = (MallShopDaycount) net.sf.json.JSONObject.toBean( net.sf.json.JSONObject.fromObject( mapcount ), MallShopDaycount.class );
		obj.setShopId( Integer.valueOf( map.get( "shop_id" ).toString() ) );
		obj.setCountDate( startTime );
		mallShopDaycountDAO.insert( obj );
	    } else {
		mallShopDaycountDAO.updateShopSalePrice( startTime, CommonUtil.toInteger( map.get( "shop_id" ) ) );
	    }
	}
    }

    //5.上月销售商品
    void lastMonthSaleProduct( String startTime, String endTime, String syear, String smonth ) {
	List< Map< String,Object > > listmonth = mallProductDaycountDAO.selectLastMonthProduct( startTime, endTime );//获取上个月的销售的所有的商品
	for ( int i = 0; i < listmonth.size(); i++ ) {
	    Map< String,Object > mapmonth = listmonth.get( i );//获取商品信息id
	    Integer product_id = Integer.valueOf( mapmonth.get( "product_id" ).toString() );//获取商品id
	    Integer wx_shop_id = Integer.valueOf( mapmonth.get( "shop_id" ).toString() );//获取店铺id
	    List< Map< String,Object > > sqlprolist = mallProductDaycountDAO.countLastMonthProduct( startTime, endTime, product_id );
	    if ( sqlprolist.size() > 0 ) {
		Map< String,Object > mappro = sqlprolist.get( 0 );//获取商品信息id
		MallProductMonthcount obj = (MallProductMonthcount) net.sf.json.JSONObject.toBean( net.sf.json.JSONObject.fromObject( mappro ), MallProductMonthcount.class );
		obj.setCountDate( startTime );
		obj.setProductId( product_id );
		obj.setYear( Integer.valueOf( syear ) );
		obj.setMonth( Integer.valueOf( smonth ) );
		obj.setShopId( wx_shop_id );
		mallProductMonthcountDAO.insert( obj );
	    }
	}

    }

    //6.店铺每月销售销售情况
    void getMonthShopSale( String startTime, String endTime, String syear, String smonth ) {
	List< Map< String,Object > > shopmonlist = mallShopDaycountDAO.selectLastMonthShop( startTime, endTime );
	for ( int w = 0; w < shopmonlist.size(); w++ ) {
	    Map< String,Object > shopmonmap = shopmonlist.get( w );
	    List< Map< String,Object > > shopmonthlist = mallShopDaycountDAO.countLastMonthShop( startTime, endTime, CommonUtil.toInteger( shopmonmap.get( "shop_id" ) ) );
	    if ( shopmonthlist.size() > 0 ) {
		Map< String,Object > shopmonthmap = shopmonthlist.get( 0 );
		MallShopMonthcount obj = (MallShopMonthcount) net.sf.json.JSONObject.toBean( net.sf.json.JSONObject.fromObject( shopmonthmap ), MallShopMonthcount.class );
		obj.setCountDate( startTime );
		obj.setYear( Integer.valueOf( syear ) );
		obj.setMonth( Integer.valueOf( smonth ) );
		obj.setShopId( Integer.valueOf( shopmonmap.get( "shop_id" ).toString() ) );
		mallShopMonthcountDAO.insert( obj );
	    }
	}
    }
}
