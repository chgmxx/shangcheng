package com.gt.mall.controller.api.basic;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.base.BaseController;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.entity.basic.MallIncomeList;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.service.web.basic.MallIncomeListService;
import com.gt.mall.service.web.order.MallOrderDetailService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 收入记录表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2018-01-30
 */
@Api( value = "mallIncomeList", description = "收入记录", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallIncomeList/E9lM9uM4ct" )
public class MallIncomeListController extends BaseController {

    @Autowired
    private MallIncomeListService     mallIncomeListService;
    @Autowired
    private MallOrderDAO              mallOrderDAO;
    @Autowired
    private MallOrderReturnService    mallOrderReturnService;
    @Autowired
    private MallStoreService          mallStoreService;
    @Autowired
    private MallOrderDetailService    mallOrderDetailService;
    @Autowired
    private MemberService             memberService;
    @Autowired
    private MallAuctionMarginService  mallAuctionMarginService;
    @Autowired
    private MallPresaleDepositService mallPresaleDepositService;
    @Autowired
    private MallProductService        mallProductService;
    @Autowired
    private MallOrderService          mallOrderService;

    @ApiOperation( value = "生成交易记录数据", notes = "生成交易记录数据" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "type", value = "生成数据 1订单 2退款 3预售定金 4拍卖保证金", paramType = "query", required = true, dataType = "int" ) } )
    @ResponseBody
    @RequestMapping( value = "/test", method = RequestMethod.POST )
    public ServerResponse test( HttpServletRequest request, HttpServletResponse response, Integer type ) {
	try {
	    if ( type == 1 ) {
		List< MallOrder > orderList = mallOrderDAO.getPayOrderById();
		if ( orderList != null && orderList.size() > 0 ) {
		    for ( MallOrder order : orderList ) {
			//添加交易记录
			MallIncomeList incomeList = new MallIncomeList();
			incomeList.setBusId( order.getBusUserId() );
			incomeList.setIncomeType( 1 );
			incomeList.setIncomeCategory( 1 );
			incomeList.setIncomeMoney( order.getOrderMoney() );
			incomeList.setShopId( order.getShopId() );
			incomeList.setBuyerId( order.getBuyerUserId() );
			incomeList.setBuyerName( order.getMemberName() );
			incomeList.setTradeId( order.getId() );
			incomeList.setTradeType( 1 );
			if ( order.getMallOrderDetail().size() > 0 ) {
			    incomeList.setProName( order.getMallOrderDetail().get( 0 ).getDetProName() );
			} else if ( order.getOrderPayWay() == 5 ) {
			    incomeList.setProName( "扫码支付" );
			}
			incomeList.setProNo( order.getOrderNo() );
			incomeList.setCreateTime( order.getPayTime() );
			mallIncomeListService.insert( incomeList );
		    }
		}
	    } else if ( type == 11 ) {
		Wrapper wrapper = new EntityWrapper();
		wrapper.where( "order_status = 4 AND SYSDATE() >= DATE_ADD(update_time, INTERVAL 7 DAY) " );
		List< MallOrder > orderList = mallOrderDAO.selectList( wrapper );
		if ( orderList != null && orderList.size() > 0 ) {
		    for ( MallOrder order1 : orderList ) {
			MallOrder order = mallOrderDAO.getOrderById( order1.getId() );
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
			if ( order.getMallOrderDetail().size() > 0 ) {
			    incomeList.setProName( order.getMallOrderDetail().get( 0 ).getDetProName() );
			}
			incomeList.setProNo( order.getOrderNo() );
			//			Calendar cal = Calendar.getInstance();
			//			cal.add( Calendar.DATE, 7 );
			//			String sevenday = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );
			incomeList.setCreateTime( DateTimeKit.addDate( order.getUpdateTime(), 7 ) );
			mallIncomeListService.insert( incomeList );
		    }
		}

	    } else if ( type == 2 ) {
		Wrapper groupWrapper = new EntityWrapper();
		groupWrapper.where( "(status=1 OR status=5)" );
		List< MallOrderReturn > returnList = mallOrderReturnService.selectList( groupWrapper );
		if ( returnList != null && returnList.size() > 0 ) {
		    for ( MallOrderReturn orderReturn : returnList ) {
			MallOrder order = mallOrderDAO.getOrderById( orderReturn.getOrderId() );
			MallOrderDetail orderDetails = mallOrderDetailService.selectById( orderReturn.getOrderDetailId() );
			if ( order == null ) {
			    continue;
			}
			//添加交易记录
			MallIncomeList incomeList = new MallIncomeList();
			incomeList.setBusId( order.getBusUserId() );
			incomeList.setIncomeType( 2 );
			incomeList.setIncomeCategory( 1 );
			incomeList.setIncomeMoney( order.getOrderMoney() );
			incomeList.setShopId( order.getShopId() );
			incomeList.setBuyerId( order.getBuyerUserId() );
			incomeList.setBuyerName( order.getMemberName() );
			incomeList.setTradeId( orderDetails.getId() );
			incomeList.setTradeType( 2 );
			incomeList.setProName( orderDetails.getDetProName() );
			incomeList.setProNo( order.getOrderNo() );
			incomeList.setCreateTime( orderReturn.getUpdateTime() == null ? orderReturn.getCreateTime() : orderReturn.getUpdateTime() );
			mallIncomeListService.insert( incomeList );
		    }
		}
	    } else if ( type == 4 ) {
		Wrapper groupWrapper = new EntityWrapper();
		groupWrapper.where( "margin_status != 0" );
		List< MallAuctionMargin > marginList = mallAuctionMarginService.selectList( groupWrapper );
		if ( marginList != null && marginList.size() > 0 ) {
		    for ( MallAuctionMargin margin : marginList ) {
			Member member = memberService.findMemberById( margin.getUserId(), null );
			MallProduct product = mallProductService.selectById( margin.getProId() );
			//添加交易记录
			MallIncomeList incomeList = new MallIncomeList();
			incomeList.setBusId( product == null ? null : product.getUserId() );
			incomeList.setIncomeType( 1 );
			incomeList.setIncomeCategory( 1 );
			incomeList.setIncomeMoney( margin.getMarginMoney() );
			incomeList.setShopId( product == null ? null : product.getShopId() );
			incomeList.setBuyerId( margin.getUserId() );
			incomeList.setBuyerName( member == null ? null : member.getNickname() );
			incomeList.setTradeId( margin.getId() );
			incomeList.setTradeType( 4 );
			incomeList.setProName( margin.getProName() );
			incomeList.setProNo( margin.getAucNo() );
			incomeList.setCreateTime( margin.getPayTime() );
			mallIncomeListService.insert( incomeList );
			if ( margin.getMarginStatus() == -1 && margin.getReturnTime() != null ) {
			    incomeList.setId( null );
			    incomeList.setIncomeType( 2 );
			    incomeList.setCreateTime( margin.getReturnTime() );
			    mallIncomeListService.insert( incomeList );
			}
		    }
		}
	    } else if ( type == 3 ) {
		Wrapper groupWrapper = new EntityWrapper();
		groupWrapper.where( "deposit_status != 0" );
		List< MallPresaleDeposit > depositList = mallPresaleDepositService.selectList( groupWrapper );
		if ( depositList != null && depositList.size() > 0 ) {
		    for ( MallPresaleDeposit deposit : depositList ) {
			Member member = memberService.findMemberById( deposit.getUserId(), null );
			MallProduct product = mallProductService.selectById( deposit.getProductId() );
			//添加交易记录
			MallIncomeList incomeList = new MallIncomeList();
			incomeList.setBusId( product == null ? null : product.getUserId() );
			incomeList.setIncomeType( 1 );
			incomeList.setIncomeCategory( 1 );
			incomeList.setIncomeMoney( deposit.getDepositMoney() );
			incomeList.setShopId( product == null ? null : product.getShopId() );
			incomeList.setBuyerId( deposit.getUserId() );
			incomeList.setBuyerName( member == null ? null : member.getNickname() );
			incomeList.setTradeId( deposit.getId() );
			incomeList.setTradeType( 3 );
			incomeList.setProName( deposit.getProName() );
			incomeList.setProNo( deposit.getDepositNo() );
			incomeList.setCreateTime( deposit.getPayTime() );
			mallIncomeListService.insert( incomeList );

			if ( deposit.getDepositStatus() == -1 && deposit.getReturnTime() != null ) {
			    incomeList.setId( null );
			    incomeList.setIncomeType( 2 );
			    incomeList.setCreateTime( deposit.getReturnTime() );
			    mallIncomeListService.insert( incomeList );
			}
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "生成交易记录数据异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "生成交易记录数据异常" );
	}
	return ServerResponse.createBySuccess();
    }

    @ApiOperation( value = "交易记录列表(分页)", notes = "交易记录列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "orderNo", value = "订单号", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "status", value = "订单状态", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "startTime", value = "下单开始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endTime", value = "下单结束时间", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, String orderNo, Integer status, String startTime, String endTime ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "orderNo", orderNo );
	    params.put( "status", status );
	    params.put( "startTime", startTime );
	    params.put( "endTime", endTime );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );
	    params.put( "incomeCategory", "1" );
	    PageUtil page = mallIncomeListService.findByTradePage( params );
	    result.put( "page", page );

	} catch ( Exception e ) {
	    logger.error( "交易记录列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "交易记录列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 导出交易记录订单
     */
    @ApiOperation( value = "导出交易记录订单", notes = "导出交易记录订单" )
    @ApiImplicitParams( { @ApiImplicitParam( name = "orderNo", value = "订单号", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "status", value = "订单状态", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "startTime", value = "下单开始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endTime", value = "下单结束时间", paramType = "query", required = false, dataType = "String" ) } )
    @RequestMapping( value = "/exportOrder", method = RequestMethod.GET )
    public void exportOrder( HttpServletRequest request, HttpServletResponse response, String orderNo, Integer status, String startTime, String endTime ) {
	OutputStream out = null;
	HSSFWorkbook workbook = null;
	try {
	    Map< String,Object > params = new HashMap<>();

	    params.put( "orderNo", orderNo );
	    params.put( "status", status );
	    params.put( "startTime", startTime );
	    params.put( "endTime", endTime );
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    params.put( "userId", user.getId() );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    params.put( "shoplist", shoplist );
	    params.put( "incomeCategory", "1" );
	    String[] titles = new String[] { "时间", "订单编号", "商品名称", "买方", "支付金额", "状态" };
	    workbook = mallIncomeListService.exportTradeExcel( params, titles, 1, shoplist );

	    String filename = "交易记录" + DateTimeKit.getDateIsLink() + ".xls";//设置下载时客户端Excel的名称
	    filename = URLEncoder.encode( filename, "UTF-8" );

	    response.setHeader( "Content-Disposition", "attachment;filename=\"" + filename + "\"" );
	    response.setContentType( "application/vnd.ms-excel" );

	    out = new BufferedOutputStream( response.getOutputStream() );
	    workbook.write( out );

	    out.flush();
	} catch ( UnsupportedEncodingException e ) {
	    e.printStackTrace();
	    logger.error( "导出交易记录订单：中文转换异常！" + e.getMessage() );
	} catch ( IOException e ) {
	    e.printStackTrace();
	    logger.error( "导出交易记录订单：IO流输出异常！" + e.getMessage() );
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "导出交易记录订单失败" + e.getMessage() );
	} finally {
	    try {
		if ( out != null ) {
		    out.close();
		}
		if ( workbook != null ) {
		    workbook.close();
		}
	    } catch ( IOException e ) {
		logger.error( "导出交易记录订单：关闭输出流异常" + e );
		e.printStackTrace();
	    }
	}
    }

    /**
     * 获取交易记录的营业额统计
     */
    @ApiOperation( value = "获取交易记录的营业额统计", notes = "获取交易记录的营业额统计" )
    @ResponseBody
    @RequestMapping( value = "/getTurnoverCount", method = RequestMethod.POST )
    public ServerResponse getTurnoverCount( HttpServletRequest request, HttpServletResponse response ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺

	    Calendar cal = Calendar.getInstance();
	    cal.add( Calendar.DATE, -1 );
	    String yesterday = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );
	    cal.add( Calendar.DATE, -6 );
	    String sevenday = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );
	    //昨天营业额
	    Map< String,Object > countParams = new HashMap<>();
	    countParams.put( "date", yesterday );
	    countParams.put( "shoplist", shoplist );
	    countParams.put( "userId", user.getId() );
	    countParams.put( "category", "1" );
	    String yesterCount = mallIncomeListService.getCountByTimes( countParams );
	    result.put( "yesterCount", yesterCount );//昨天营业额
	    //7日营业额（前7天-昨天）
	    countParams.remove( "date" );
	    countParams.put( "startDate", sevenday );
	    countParams.put( "endDate", yesterday );
	    String sevenCount = mallIncomeListService.getCountByTimes( countParams );
	    result.put( "sevenCount", sevenCount );//7天营业额
	} catch ( Exception e ) {
	    logger.error( "获取交易记录的营业额统计异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取交易记录的营业额统计异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }

    /**
     * 获取收入金额列表
     */
    @ApiOperation( value = "获取收入金额列表", notes = "获取收入金额列表" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "startDate", value = "开始时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "endDate", value = "结束时间", paramType = "query", required = false, dataType = "String" ),
		    @ApiImplicitParam( name = "shopId", value = "店铺ID", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/getCountListByDate", method = RequestMethod.POST )
    public ServerResponse getCountListByDate( HttpServletRequest request, HttpServletResponse response, String startDate, String endDate, Integer shopId ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = mallStoreService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    List< Integer > shopIds = new ArrayList<>();
	    for ( Map map : shoplist ) {
		shopIds.add( CommonUtil.toInteger( map.get( "id" ) ) );
	    }
	    Map< String,Object > params = new HashMap<>();
	    params.put( "shopId", shopId );
	    if ( CommonUtil.isEmpty( shopId ) ) {
		params.put( "shoplist", shoplist );
	    }
	    if ( CommonUtil.isEmpty( startDate ) ) {
		Calendar cal = Calendar.getInstance();
		cal.add( Calendar.DATE, -1 );
		endDate = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );
		cal.add( Calendar.DATE, -29 );
		startDate = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );
	    } else {
		startDate = DateTimeKit.format( DateTimeKit.parseDate( startDate ) );
		endDate = DateTimeKit.format( DateTimeKit.parseDate( endDate ) );
	    }

	    Integer date = DateTimeKit.daysBetween( startDate, endDate );

	    params.put( "startDate", startDate );
	    params.put( "endDate", endDate );
	    List< Map< String,Object > > countList = mallIncomeListService.getCountListByTimes( params );
	    String[] data = new String[date + 1];

	    int i = 0;
	    Calendar end = Calendar.getInstance();//定义日期实例
	    Calendar start = Calendar.getInstance();//定义日期实例
	    Date d1 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( endDate );//结束日期
	    Date d2 = new SimpleDateFormat( "yyyy-MM-dd" ).parse( startDate );//起始日期
	    end.setTime( d1 );
	    start.setTime( d2 );

	    while ( start.getTimeInMillis() <= end.getTimeInMillis() ) {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		String str = sdf.format( start.getTime() );
		String count = "";
		for ( Map map : countList ) {
		    if ( str.equals( map.get( "createTime" ).toString() ) ) {
			count = map.get( "incomeMoney" ).toString();
			break;
		    }
		}
		if ( "".equals( count ) ) {
		    count = "0";
		}
		data[i] = count;
		i++;
		start.add( Calendar.DATE, 1 );//进行当前日期加1
	    }
	    result.put( "data", data );//数据列表

	    Calendar cal = Calendar.getInstance();
	    String day = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );
	    cal.add( Calendar.DATE, -1 );
	    String yesterday = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );
	    cal.add( Calendar.DATE, -6 );
	    String sevenday = new SimpleDateFormat( "yyyy-MM-dd " ).format( cal.getTime() );

	    Wrapper groupWrapper = new EntityWrapper();
	    groupWrapper.where( "TO_DAYS(pay_time) = TO_DAYS({0}) and order_status>1 and order_status!=5", day );
	    if ( CommonUtil.isEmpty( shopId ) ) {
		groupWrapper.in( "shop_id", shopIds );
	    } else {
		groupWrapper.and( "shop_id ={0}", shopId );
	    }
	    Integer todayPayOrderNum = mallOrderService.selectCount( groupWrapper );

	    Map< String,Object > params1 = new HashMap<>();
	    params1.put( "userId", user.getId() );
	    if ( CommonUtil.isNotEmpty( shopId ) ) {
		params1.put( "shopId", shopId );
	    } else {
		params1.put( "shoplist", shoplist );
	    }
	    params1.put( "status", "1" );
	    Integer waitPayOrderNum = mallOrderService.count( params1 );

	    params1.put( "status", "2" );
	    Integer waitDeliveryOrderNum = mallOrderService.count( params1 );

	    result.put( "todayPayOrderNum", todayPayOrderNum );//今日付款订单数
	    result.put( "waitPayOrderNum", waitPayOrderNum );//待付款订单数
	    result.put( "waitDeliveryOrderNum", waitDeliveryOrderNum );//待发货订单数

	    Map< String,Object > countParams = new HashMap<>();
	    countParams.put( "category", "2" );
	    if ( CommonUtil.isEmpty( shopId ) ) {
		countParams.put( "shoplist", shoplist );
	    } else {
		countParams.put( "shopId", shopId );
	    }
	    countParams.put( "date", yesterday );
	    String yesterPrice = mallIncomeListService.getCountByTimes( countParams );

	    countParams.remove( "date" );
	    countParams.put( "startDate", sevenday );
	    countParams.put( "endDate", yesterday );
	    String sevenCount = mallIncomeListService.getCountByTimes( countParams );
	    result.put( "yesterIncomeCount", yesterPrice );//昨天总收入
	    result.put( "sevenIncomeCount", sevenCount );//7天总收入

	} catch ( Exception e ) {
	    logger.error( "获取收入金额列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取收入金额列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result, false );
    }
}
