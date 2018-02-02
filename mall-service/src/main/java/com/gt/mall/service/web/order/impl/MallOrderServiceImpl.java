package com.gt.mall.service.web.order.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.entityBo.ErpRefundBo;
import com.gt.entityBo.NewErpPaySuccessBo;
import com.gt.entityBo.PayTypeBo;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.member.UserConsumeParams;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.freight.MallFreightDAO;
import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.order.*;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallIncomeList;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.groupbuy.MallGroupJoin;
import com.gt.mall.entity.order.*;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.result.order.OrderDetailResult;
import com.gt.mall.result.order.OrderResult;
import com.gt.mall.result.order.OrderReturnLogResult;
import com.gt.mall.result.order.OrderReturnResult;
import com.gt.mall.service.inter.member.CardService;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.union.UnionConsumeService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.user.MemberAddressService;
import com.gt.mall.service.inter.user.SocketService;
import com.gt.mall.service.inter.wxshop.*;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.basic.*;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallDaifuService;
import com.gt.mall.service.web.order.MallOrderReturnLogService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.order.MallOrderSubmitService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import com.gt.union.api.entity.param.UnionConsumeParam;
import com.gt.union.api.entity.param.UnionRefundParam;
import com.gt.util.entity.param.fenbiFlow.AdcServicesInfo;
import com.gt.util.entity.param.fenbiFlow.BusFlowInfo;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wallet.TRefundOrder;
import com.gt.util.entity.param.wx.BusIdAndindustry;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import com.gt.util.entity.result.pay.WxPayOrder;
import com.gt.util.entity.result.shop.WsWxShopInfo;
import com.gt.util.entity.result.wx.ApiWxApplet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 商城订单 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderServiceImpl extends BaseServiceImpl< MallOrderDAO,MallOrder > implements MallOrderService {

    private static Logger logger = LoggerFactory.getLogger( MallOrderServiceImpl.class );

    @Autowired
    private MallOrderDAO mallOrderDAO;

    @Autowired
    private MallProductDAO mallProductDAO;

    @Autowired
    private MallOrderReturnDAO mallOrderReturnDAO;

    @Autowired
    private MallStoreDAO mallStoreDAO;

    @Autowired
    private MallGroupJoinDAO mallGroupJoinDAO;

    @Autowired
    private MallGroupBuyDAO mallGroupBuyDAO;

    @Autowired
    private MallSeckillService mallSeckillService;

    @Autowired
    private MallTakeTheirService mallTakeTheirService;

    @Autowired
    private MallDaifuDAO mallDaifuDAO;

    @Autowired
    private MallPresaleService mallPresaleService;

    @Autowired
    private MallOrderDetailDAO mallOrderDetailDAO;

    @Autowired
    private MallSellerService mallSellerService;

    @Autowired
    private MallProductService mallProductService;

    @Autowired
    private MallProductInventoryService mallProductInventoryService;

    @Autowired
    private MallAuctionBiddingService mallAuctionBiddingService;

    @Autowired
    private MallPaySetService mallPaySetService;

    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;

    @Autowired
    private MallFreightDAO mallFreightDAO;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CardService cardService;

    @Autowired
    private DictService dictService;

    @Autowired
    private MemberPayService memberPayService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Autowired
    private BusUserService busUserService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private PayService payService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private SocketService socketService;

    @Autowired
    private FenBiFlowService fenBiFlowService;

    @Autowired
    private WxShopService wxShopService;

    @Autowired
    private MemberAddressService memberAddressService;

    @Autowired
    private UnionConsumeService unionConsumeService;

    @Autowired
    private MallOrderSubmitService  mallOrderSubmitService;
    @Autowired
    private MallDaifuService        mallDaifuService;
    @Autowired
    private MallLogOrderCallbackDAO mallLogOrderCallbackDAO;

    @Autowired
    private MallStoreService mallStoreService;

    @Autowired
    private MallGroupBuyService mallGroupBuyService;

    @Autowired
    private MallOrderReturnLogService   mallOrderReturnLogService;
    @Autowired
    private MallBusMessageMemberService mallBusMessageMemberService;
    @Autowired
    private MallCountIncomeService      mallCountIncomeService;
    @Autowired
    private MallIncomeListService       mallIncomeListService;

    @Override
    public Integer count( Map< String,Object > params ) {
	int status = 0;
	if ( CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
	    status = CommonUtil.toInteger( params.get( "status" ) );
	}
	int count = 0;
	if ( status != 6 && status != 7 && status != 8 && status != 9 ) {
	    count = mallOrderDAO.count( params );
	} else {
	    count = mallOrderDAO.countReturn( params );
	}

	return count;
    }

    @Override
    public Map< String,Object > countStatus( Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	params.remove( "status" );
	if ( CommonUtil.toInteger( params.get( "orderType" ) ) == -1 ) {
	    //退款全部
	    params.put( "status", "7" );
	    Integer status7 = mallOrderDAO.countReturn( params );
	    //8退款处理中
	    params.put( "status", "8" );
	    Integer status8 = mallOrderDAO.countReturn( params );
	    //退款结束
	    params.put( "status", "9" );
	    Integer status9 = mallOrderDAO.countReturn( params );

	    result.put( "status7", status7 );
	    result.put( "status8", status8 );
	    result.put( "status9", status9 );
	} else {
	    //全部
	    Integer status_total = mallOrderDAO.count( params );
	    //待付款
	    params.put( "status", "1" );
	    Integer status1 = mallOrderDAO.count( params );
	    //待发货
	    params.put( "status", "2" );
	    Integer status2 = mallOrderDAO.count( params );
	    //已发货
	    params.put( "status", "3" );
	    Integer status3 = mallOrderDAO.count( params );
	    //已完成
	    params.put( "status", "4" );
	    Integer status4 = mallOrderDAO.count( params );
	    //已关闭
	    params.put( "status", "5" );
	    Integer status5 = mallOrderDAO.count( params );
	    //退款中
	    params.put( "status", "6" );
	    Integer status6 = mallOrderDAO.countReturn( params );

	    result.put( "status_total", status_total );
	    result.put( "status1", status1 );
	    result.put( "status2", status2 );
	    result.put( "status3", status3 );
	    result.put( "status4", status4 );
	    result.put( "status5", status5 );
	    result.put( "status6", status6 );
	}

	return result;
    }

    @Override
    public PageUtil findByPage( Map< String,Object > params, List< Map< String,Object > > shoplist ) {
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	int status = 0;
	if ( CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
	    status = CommonUtil.toInteger( params.get( "status" ) );
	    if ( status == 0 ) {
		params.remove( "status" );
	    }
	}
	int rowCount = 0;
	if ( status != 6 && status != 7 && status != 8 && status != 9 ) {
	    rowCount = mallOrderDAO.count( params );
	} else {
	    rowCount = mallOrderDAO.countReturn( params );
	}
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "mallOrder/toIndex.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	List< MallOrder > list;
	if ( status != 6 && status != 7 && status != 8 && status != 9 ) {
	    list = mallOrderDAO.findByPage( params );
	} else {
	    list = mallOrderDAO.findReturnByPage( params );
	}

	list = getOrderListParams( list );
	/*if ( list == null || list.size() == 0 ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), "您还没有相关的订单" );
	}*/

	List< OrderResult > orderResultList = new ArrayList< OrderResult >();
	for ( MallOrder mallOrder : list ) {
	    OrderResult orderResult = converterOrder( mallOrder, shoplist );
	    orderResultList.add( orderResult );
	}
	page.setSubList( orderResultList );
	return page;
    }

    public List< MallOrder > getOrderListParams( List< MallOrder > list ) {
	List< Integer > orderIdList = new ArrayList<>();
	List< Integer > detailIdList = new ArrayList<>();
	List< MallOrder > orderList = new ArrayList<>();
	boolean isHaveDetail = true;
	if ( list != null && list.size() > 0 ) {
	    for ( MallOrder order : list ) {
		String orderPNo = order.getOrderNo();
		if ( CommonUtil.isNotEmpty( order.getOrderPid() ) ) {
		    if ( order.getOrderPid() > 0 ) {
			MallOrder pOrder = mallOrderDAO.selectById( order.getOrderPid() );
			if ( CommonUtil.isNotEmpty( pOrder ) ) {
			    orderPNo = pOrder.getOrderNo();
			}
		    }
		}
		boolean flag = false;//没有查询出订单详情
		if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			if ( CommonUtil.isNotEmpty( detail.getId() ) ) {
			    if ( detail.getStatus() != -3 ) {
				detailIdList.add( detail.getId() );
			    }
			    isHaveDetail = true;
			    flag = true;
			} else {
			    flag = false;
			    isHaveDetail = false;
			}
		    }

		}
		if ( !flag ) {
		    if ( !orderIdList.contains( order.getId() ) ) {
			orderIdList.add( order.getId() );
		    }
		}
		order.setOrderPNo( orderPNo );
	    }
	    List< MallOrderDetail > orderdetailList = new ArrayList< MallOrderDetail >();
	    //查询订单详情信息
	    if ( orderIdList != null && orderIdList.size() > 0 && detailIdList.size() == 0 ) {
		Map< String,Object > orderParams = new HashMap< String,Object >();
		orderParams.put( "orderIdList", orderIdList );
		orderdetailList = mallOrderDetailDAO.selectDetailByOrderIds( orderParams );
		if ( orderdetailList != null && orderdetailList.size() > 0 ) {
		    for ( MallOrderDetail mallOrderDetail : orderdetailList ) {
			if ( mallOrderDetail.getStatus() != -3 ) {
			    detailIdList.add( mallOrderDetail.getId() );
			}
		    }
		}
	    }
	    List< MallOrderReturn > orderReturnList = new ArrayList<>();
	    //查询订单退款信息
	    if ( detailIdList != null && detailIdList.size() > 0 ) {
		Map< String,Object > detailParams = new HashMap<>();
		detailParams.put( "detailIdList", detailIdList );
		orderReturnList = mallOrderReturnDAO.selectByDetailIds( detailParams );
	    }
	    for ( MallOrder order : list ) {
		List< MallOrderDetail > detailList = new ArrayList<>();
		List< MallOrderDetail > detailIds = new ArrayList<>();
		boolean isDetail = false;
		if ( !isHaveDetail ) {
		    if ( orderdetailList != null && orderdetailList.size() > 0 ) {
			for ( MallOrderDetail detail : orderdetailList ) {
			    if ( detail.getOrderId().toString().equals( order.getId().toString() ) ) {
				detailList.add( detail );
				detailIds.add( detail );
				isDetail = true;
			    }
			}
			if ( detailIds != null && detailIds.size() > 0 ) {
			    order.setMallOrderDetail( detailList );
			    orderdetailList.removeAll( detailIds );
			}
		    }
		}
		if ( ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) || isDetail ) {
		    isDetail = true;
		    if ( orderReturnList != null && orderReturnList.size() > 0 ) {
			for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			    if ( CommonUtil.isNotEmpty( detail.getId() ) ) {
				isDetail = true;
				if ( detail.getStatus() != -3 ) {
				    for ( int i = 0; i < orderReturnList.size(); i++ ) {
					MallOrderReturn mallOrderReturn = orderReturnList.get( i );
					if ( CommonUtil.toString( mallOrderReturn.getOrderDetailId() ).equals( CommonUtil.toString( detail.getId() ) ) && CommonUtil
							.toString( mallOrderReturn.getOrderId() ).equals( CommonUtil.toString( detail.getOrderId() ) ) ) {
					    detail.setOrderReturn( mallOrderReturn );
					    orderReturnList.remove( i );
					    break;
					}
				    }
				}
			    } else {
				isDetail = false;
			    }
			}
		    }
		}

		for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
		    if ( CommonUtil.isEmpty( detail.getId() ) || detail.getId() == 0 ) {
			isDetail = false;
			break;
		    }
		}
		if ( isDetail ) {
		    if ( order.getMallOrderDetail().size() > 0 || ( order.getMallOrderDetail().size() == 0 && order.getOrderPayWay() == 5 ) ) {
			orderList.add( order );
		    }
		}
	    }
	}
	return orderList;
    }

    @Override
    public PageUtil findByTradePage( Map< String,Object > params ) {
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	int rowCount = mallOrderDAO.tradeCount( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "mallOrder/toIndex.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	if ( rowCount > 0 ) {
	    List< Map< String,Object > > list = mallOrderDAO.findByTradePage( params );
	    if ( list != null && list.size() > 0 ) {
		for ( Map< String,Object > order : list ) {
		    Integer orderStatus = CommonUtil.toInteger( order.get( "orderStatus" ) );
		    Integer returnStatus = null;
		    order.put( "memberName", CommonUtil.blob2String( order.get( "memberName" ) ) );

		    if ( CommonUtil.isNotEmpty( order.get( "return_status" ) ) ) {
			returnStatus = CommonUtil.toInteger( order.get( "return_status" ) );
		    }
		    if ( returnStatus != null && ( returnStatus == 1 || returnStatus == 5 ) ) {
			order.put( "status", "2" );
		    } else {
			order.put( "status", "1" );
		    }
		}
	    }
	    page.setSubList( list );
	}

	return page;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public int upOrderNoOrRemark( Map< String,Object > params ) {
	Member member = null;
	if ( CommonUtil.isNotEmpty( params.get( "noReturnReason" ) ) ) {
	    params.put( "noReturnReason", CommonUtil.urlEncode( params.get( "noReturnReason" ).toString() ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "returnAddress" ) ) ) {
	    params.put( "returnAddress", CommonUtil.urlEncode( params.get( "returnAddress" ).toString() ) );
	}
	int count = mallOrderDAO.upOrderNoOrRemark( params );
	Object type = params.get( "type" );
	Object status = params.get( "status" );
	Object express = params.get( "express" );
	if ( null != type && type.toString().equals( "2" ) ) {//取消订单
	    if ( count == 1 ) {
		params.put( "status", 5 );
		count = mallOrderDAO.upOrderNoOrRemark( params );
	    }
	}
	if ( null != status && status.toString().equals( "4" ) ) {//确认收货
	    if ( count == 1 ) {
		try {
		    mallBusMessageMemberService.buyerConfirmReceipt( CommonUtil.toInteger( params.get( "orderId" ) ), 0 );
		} catch ( Exception e ) {
		    e.printStackTrace();
		    logger.error( "提醒商家消息失败异常" + e.getMessage() );
		}
	    }
	}

	if ( null != express && express.equals( "1" ) ) {
	    if ( count == 1 ) {
		params.put( "status", 3 );
		count = mallOrderDAO.upOrderNoOrRemark( params );
	    }
	}

	if ( CommonUtil.isNotEmpty( type ) && count > 0 ) {
	    if ( type.equals( "4" ) ) {
		MallOrder order = mallOrderDAO.getOrderById( CommonUtil.toInteger( params.get( "orderId" ) ) );
		//商家确认买家已经提货 修改交易记录表
		UserConsumeParams consumeParams = new UserConsumeParams();
		consumeParams.setOrderNo( order.getOrderNo() );
		consumeParams.setPayStatus( 1 );
		consumeParams.setPayType( CommonUtil.getMemberPayType( order.getOrderPayWay(), order.getIsWallet() ) );
		memberService.updateJifen( consumeParams );

	    }
	}
	if ( count > 0 ) {
	    if ( CommonUtil.isNotEmpty( params.get( "status" ) ) && CommonUtil.isNotEmpty( params.get( "orderId" ) ) ) {
		MallOrder order = mallOrderDAO.getOrderById( CommonUtil.toInteger( params.get( "orderId" ) ) );
		if ( params.get( "status" ).toString().equals( "3" ) ) {//发货时赠送实体物品
		    mallPresaleService.deliveryRank( order );//发货时赠送实体物品
		}
		/*if ( params.get( "status" ).toString().equals( "4" ) ) {//确认收货订单已经完成
		    if ( CommonUtil.isNotEmpty( order ) ) {
			if ( CommonUtil.isNotEmpty( order.getMallOrderDetail() ) ) {
			    if ( CommonUtil.isEmpty( member ) ) {
				member = memberService.findMemberById( order.getBuyerUserId() ,null);
			    }
			    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
				int dStatus = detail.getStatus();
				if ( dStatus == -3 || dStatus == -2 ) {
				    //todo 调用彭江丽接口   支付有礼相关  暂没有
				    PaySuccessLog log = new PaySuccessLog();
				    log.setTotalmoney( detail.getTotalPrice() );
				    log.setMemberid( member.getId() );
				    log.setModel( 2 );
				    Date date = new Date();
				    if ( CommonUtil.isNotEmpty( detail.getReturnDay() ) ) {
					if ( detail.getReturnDay() > 0 ) {
					    date = DateTimeKit.addDays( date, detail.getReturnDay() );
					}
				    }
				    log.setDate( date );
				    log.setOrderno( order.getOrderNo() );
				    logMapper.insert( log );
				}
			    }
			}
		    }
		}*/
	    }
	    if ( params.containsKey( "detailObj" ) ) {
		JSONArray arr = JSONArray.fromObject( params.get( "detailObj" ) );
		if ( arr != null && arr.size() > 0 ) {
		    for ( Object object : arr ) {
			JSONObject obj = JSONObject.fromObject( object );
			MallOrderDetail detail = new MallOrderDetail();
			double totalMoney = CommonUtil.toDouble( obj.get( "proMoney" ) );
			int num = CommonUtil.toInteger( obj.get( "num" ) );
			detail.setId( obj.getInt( "id" ) );
			detail.setTotalPrice( totalMoney );
			double proPrice = totalMoney / num;
			detail.setDetProPrice( BigDecimal.valueOf( proPrice ) );
			mallOrderDetailDAO.updateById( detail );
		    }
		}
	    }
	}
	return count;
    }

    @Override
    public OrderResult selectOrderList( Integer orderId, List< Map< String,Object > > shoplist ) {
	OrderResult result = null;
	MallOrder order = mallOrderDAO.getOrderById( orderId );
	if ( order != null ) {
	    result = converterOrder( order, shoplist );
	}
	return result;
    }

    @Override
    public int paySuccessModified( Map< String,Object > params, Member member ) {
	logger.info( "商城支付完成后进入回调方法，接收参数：" + params );
	String orderNo = params.get( "out_trade_no" ).toString();
	MallOrder order = mallOrderDAO.selectOrderByOrderNo( orderNo );
	if ( CommonUtil.isEmpty( member ) ) {
	    member = memberService.findMemberById( order.getBuyerUserId(), null );
	}
	WxPublicUsers pbUser = wxPublicUserService.selectByMemberId( order.getBuyerUserId() );
	BusUser busUser = busUserService.selectById( member.getBusid() );
	//	String log_message = "";
	List< MallOrder > mallOrderList = mallOrderDAO.getOrderByOrderPId( order.getId() );
	try {
	    if ( mallOrderList.size() > 0 ) {
		//		mallOrderList = mallOrderDAO.getOrderByOrderPId( order.getId() );
		for ( MallOrder mallOrder : mallOrderList ) {
		    updateStatusStock( mallOrder, params, member, pbUser );
		}
	    } else {
		MallOrder mallOrder = mallOrderDAO.getOrderById( order.getId() );
		updateStatusStock( mallOrder, params, member, pbUser );
		mallOrderList.add( mallOrder );
	    }
	    List< MallOrderDetail > orderDetails = mallOrderDetailDAO.selectByOrderId( order.getId() );
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
	    if ( orderDetails.size() > 0 ) {
		incomeList.setProName( orderDetails.get( 0 ).getDetProName() );
	    } else if ( order.getOrderPayWay() == 5 ) {
		incomeList.setProName( "扫码支付" );
	    }
	    if ( order.getOrderPayWay() == 4 ) {
		incomeList.setIncomeUnit( 3 );
	    } else if ( order.getOrderPayWay() == 8 ) {
		incomeList.setIncomeUnit( 2 );
	    }
	    incomeList.setProNo( orderNo );
	    incomeList.setCreateTime( new Date() );
	    mallIncomeListService.insert( incomeList );
	    //退款成功，添加当天营业额记录
	    mallCountIncomeService.saveTurnover( order.getShopId(), order.getOrderMoney(), null );

	} catch ( BusinessException e ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), e.getMessage() );//订单状态和库存未执行，抛异常
	} catch ( Exception e ) {
	    logger.error( "支付成功回调——修改订单状态和库存失败：" + e.getMessage() );
	    e.printStackTrace();
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), e.getMessage() );//订单状态和库存未执行，抛异常
	}
	if ( mallOrderList == null || mallOrderList.size() == 0 ) {
	    MallOrder mallOrder = mallOrderDAO.getOrderById( order.getId() );
	    mallOrderList.add( mallOrder );
	}

	if ( ( mallOrderList != null && mallOrderList.size() > 0 && order.getOrderPayWay() != 7 ) ) {
	    try {
		paySuccess( mallOrderList, pbUser, null, orderNo );//支付成功回调储值卡支付，积分支付，粉币支付
	    } catch ( BusinessException e ) {
		System.out.println( "e = " + e );
		logger.error( "支付成功回调——" + e.getMessage() );
	    } catch ( Exception e ) {
		logger.error( "支付成功回调——会员回调异常：" + e.getMessage() );
		e.printStackTrace();
	    }
	}

	//微信支付，支付宝支付，小程序支付  在支付时已经消息推送了
	if ( CommonUtil.toDouble( order.getOrderMoney() ) > 0 && ( order.getOrderPayWay() != 1 && order.getOrderPayWay() != 9 && order.getOrderPayWay() != 10 ) ) {
	    String url = PropertiesUtil.getHomeUrl() + "html/back/views/order/index.html#/allOrder";
	    if ( CommonUtil.isNotEmpty( member.getBusid() ) ) {
		try {
		    Map< String,Object > socketParams = new HashMap<>();
		    socketParams.put( "pushName", member.getBusid() );
		    socketParams.put( "pushMsg", url );
		    socketService.getSocketApi( socketParams );
		} catch ( Exception e ) {
		    e.printStackTrace();
		    logger.error( "消息推送异常：" + e.getMessage() );
		}
	    }
	}

	//商家订单短信推送
	params.remove( "shopIds" );
	StringBuffer telePhone = new StringBuffer();
	//PC 端订单推送
	String shopIds = "";
	if ( mallOrderList != null && mallOrderList.size() > 0 ) {
	    shopIds = ",";
	    for ( MallOrder mallOrder : mallOrderList ) {
		if ( !shopIds.contains( "," + mallOrder.getShopId() + "," ) ) {

		    shopIds += mallOrder.getShopId() + ",";
		}
	    }
	    shopIds = shopIds.substring( 1, shopIds.length() - 1 );
	}
	if ( CommonUtil.isNotEmpty( shopIds ) ) {
	    String[] shopId = shopIds.split( "," );

	    Wrapper< MallStore > wrapper = new EntityWrapper<>();
	    wrapper.in( "id", shopId );
	    List< MallStore > storeList = mallStoreDAO.selectList( wrapper );

	    for ( MallStore store : storeList ) {
		if ( store.getStoIsSms() == 1 ) {//1是推送
		    if ( store.getStoSmsTelephone() != null ) {
			if ( CommonUtil.isNotEmpty( telePhone ) ) {
			    telePhone.append( "," );
			}
			telePhone.append( store.getStoSmsTelephone() );
		    }
		}
	    }
	    //	    if ( CommonUtil.isNotEmpty( telePhone ) ) {
	    //		OldApiSms oldApiSms = new OldApiSms();
	    //		oldApiSms.setMobiles( telePhone.toString() );
	    //		oldApiSms.setCompany( busUser.getMerchant_name() );
	    //		oldApiSms.setBusId( member.getBusid() );
	    //		oldApiSms.setModel( Constants.SMS_MODEL );
	    //		oldApiSms.setContent( CommonUtil.format( "你的商城有新的订单，请登录" + Constants.doMainName + "网站查看详情。" ) );
	    //		try {
	    //		    smsService.sendSmsOld( oldApiSms );
	    //		} catch ( Exception e ) {
	    //		    e.printStackTrace();
	    //		    logger.error( "短信推送消息异常：" + e.getMessage() );
	    //		}
	    //	    }
	}

	if ( CommonUtil.isNotEmpty( pbUser ) ) {
	    try {
		sendMsg( order, 4, pbUser, member, busUser );//发送消息模板
	    } catch ( Exception e ) {
		e.printStackTrace();
		logger.error( "购买成功消息模板发送异常" + e.getMessage() );
	    }
	    try {
		mallBusMessageMemberService.buyerPaySuccess( order, member, 0, telePhone.toString() );
	    } catch ( Exception e ) {
		e.printStackTrace();
		logger.error( "提醒商家消息失败异常" + e.getMessage() );
	    }

	}
	return 1;
    }

    private void fenCard( MallOrder order ) {
	if ( CommonUtil.isNotEmpty( order.getMallOrderDetail() ) ) {
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
		if ( detail.getProTypeId() == 3 && CommonUtil.isNotEmpty( detail.getCardReceiveId() ) ) {
		    if ( detail.getCardReceiveId() > 0 ) {
			Map< String,Object > cardParams = new HashMap<>();
			cardParams.put( "receiveId", detail.getCardReceiveId() );//卡包id
			cardParams.put( "num", detail.getDetProNum() );//数量
			cardParams.put( "memberId", order.getBuyerUserId() );//粉丝id
			cardService.successPayBack( cardParams );//商场支付成功回调 分配卡券
		    }
		}
	    }
	}
    }

    /**
     * 支付成功回调核销（微信联盟核销、流量充值、erp修改库存异常、会员回调）
     *
     * @param mallOrderList 订单集合
     */
    @Override
    public boolean paySuccess( List< MallOrder > mallOrderList, WxPublicUsers pbUser, String returnLogStatus, String orderNo ) {
	MallOrder order = mallOrderList.get( 0 );
	double discountMoney = 0;//折扣后的金额
	String codes = "";//优惠券code
	int couponType = -1;//优惠券类型  0 微信  1多粉
	boolean isFenbi = false;//是否使用粉币
	boolean isJifen = false;//是否使用积分
	double fenbiNum = 0;//使用粉币数量
	double jifenNum = 0;//使用积分数量
	int memberId = 0;//粉丝id
	Integer flowId = 0;//流量id
	Integer flowRecordId = 0;//流量冻结id
	int uType = 1;//用户类型 1总账号  0子账号
	int userPId = busUserService.getMainBusId( order.getBusUserId() );//查询商家总账号
	long isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
	List< Map< String,Object > > erpList = new ArrayList<>();
	BusUser user = null;

	List< NewErpPaySuccessBo > successBoList = new ArrayList<>();
	MallStore store = null;
	for ( MallOrder mallOrder : mallOrderList ) {
	    memberId = mallOrder.getBuyerUserId();
	    if ( !mallOrder.getBusUserId().toString().equals( CommonUtil.toString( userPId ) ) ) {
		uType = 0;
	    }
	    //会员回调
	    NewErpPaySuccessBo successBo = new NewErpPaySuccessBo();
	    successBo.setMemberId( mallOrder.getBuyerUserId() );//会员id
	    if ( mallOrder.getMallOrderDetail() != null ) {
		for ( MallOrderDetail orderDetail : mallOrder.getMallOrderDetail() ) {
		    if ( CommonUtil.isEmpty( store ) || store.getId().toString().equals( orderDetail.getShopId() ) ) {
			store = mallStoreDAO.selectById( orderDetail.getShopId() );
			if ( CommonUtil.isNotEmpty( store ) && CommonUtil.isNotEmpty( store.getWxShopId() ) ) {
			    successBo.setStoreId( store.getWxShopId() );//门店id
			}
		    }
		    if ( isJxc == 1 && orderDetail.getProTypeId() == 0 ) {//商家开通进销存，并且还是 实物商品
			if ( CommonUtil.isEmpty( user ) ) {
			    user = busUserService.selectById( mallOrder.getBusUserId() );
			}
			Map< String,Object > erpMap = new HashMap<>();
			erpMap.put( "uId", user.getId() );
			erpMap.put( "uType", uType );//用户类型
			erpMap.put( "uName", user.getName() );
			erpMap.put( "rootUid", userPId );
			erpMap.put( "type", 0 );//下单
			erpMap.put( "remark", "商城下单" );
			erpMap.put( "shopId", store.getWxShopId() );
			erpList.add( erpMap );
		    }

		    flowId = orderDetail.getFlowId();
		    flowRecordId = orderDetail.getFlowRecordId();

		    if ( CommonUtil.isNotEmpty( orderDetail.getDiscountedPrices() ) ) {
			discountMoney += CommonUtil.toDouble( orderDetail.getDiscountedPrices() );
		    }
		    //优惠券code
		    if ( CommonUtil.isNotEmpty( orderDetail.getCouponCode() ) ) {
			if ( CommonUtil.isNotEmpty( codes ) ) {
			    codes += ",";
			}
			codes += orderDetail.getCouponCode();
		    }
		    //优惠券json
		    String coupon = orderDetail.getDuofenCoupon();
		    if ( CommonUtil.isNotEmpty( coupon ) ) {
			JSONObject couponObj = JSONObject.fromObject( coupon );
			int type = couponObj.getInt( "couponType" );
			if ( type == 1 ) {//微信
			    couponType = 0;
			} else if ( type == 2 ) {//多粉
			    couponType = 1;
			}
		    }
		    if ( orderDetail.getUseFenbi() > 0 ) {
			isFenbi = true;
			fenbiNum += orderDetail.getUseFenbi();
		    }
		    if ( orderDetail.getUseJifen() > 0 ) {
			isJifen = true;
			jifenNum += orderDetail.getUseJifen();
		    }
		}
	    }
	    int orderPayWay = mallOrder.getOrderPayWay();
	    if ( orderPayWay == 4 ) {//积分支付
		isJifen = true;
		jifenNum += CommonUtil.toDouble( mallOrder.getOrderMoney() );
	    } else if ( orderPayWay == 8 ) {//粉币支付
		isFenbi = true;
		fenbiNum += CommonUtil.toDouble( mallOrder.getOrderMoney() );
	    }

	    successBo.setOrderCode( mallOrder.getOrderNo() );//订单号
	    successBo.setTotalMoney( CommonUtil.add( CommonUtil.toDouble( mallOrder.getOrderMoney() ), discountMoney ) );//应付金额
	    successBo.setDiscountMoney( discountMoney );//优惠金额
	    successBo.setDiscountAfterMoney( CommonUtil.toDouble( mallOrder.getOrderMoney() ) );//优惠后金额
	    successBo.setUcType( CommonUtil.getMemberUcType( mallOrder.getOrderType() ) );//消费类型 字典1197
	    if ( CommonUtil.isNotEmpty( mallOrder.getCouponId() ) && mallOrder.getCouponId() > 0 ) {
		successBo.setUseCoupon( 1 );//是否使用优惠券  0不使用 1使用
		successBo.setCouponType( couponType );//优惠券类型 0微信 1多粉优惠券
		successBo.setCardId( mallOrder.getCouponId() ); //卡券id
		successBo.setNumber( mallOrder.getCouponUseNum() );//卡券使用数量
	    }
	    if ( isFenbi ) {
		successBo.setUserFenbi( 1 );
		successBo.setFenbiNum( fenbiNum );
	    }
	    if ( isJifen ) {
		successBo.setUserJifen( 1 );
		successBo.setJifenNum( CommonUtil.toIntegerByDouble( jifenNum ) );
	    }
	    if ( orderPayWay == 4 || orderPayWay == 8 ) {
		successBo.setTotalMoney( 0d );////应付金额
		successBo.setDiscountMoney( 0d );//优惠金额
		successBo.setDiscountAfterMoney( 0d );//优惠后金额
	    }
	    PayTypeBo payTypeBo = new PayTypeBo();
	    int isWallet = 0;
	    if ( CommonUtil.isNotEmpty( mallOrder.getIsWallet() ) ) {
		isWallet = mallOrder.getIsWallet();
	    }
	    payTypeBo.setPayType( CommonUtil.getMemberPayType( mallOrder.getOrderPayWay(), isWallet ) );//支付方式 查询看字典1198
	    payTypeBo.setPayMoney( CommonUtil.toDouble( mallOrder.getOrderMoney() ) );
	    List< PayTypeBo > typeBoList = new ArrayList<>();
	    typeBoList.add( payTypeBo );
	    successBo.setPayTypeBos( typeBoList );

	    successBo.setDataSource( mallOrder.getBuyerUserType() );//数据来源 0:pc端 1:微信 2:uc端 3:小程序 4魔盒 5:ERP
	    successBo.setIsDiatelyGive( 1 );////是否立即赠送 0延迟送 1立即送
	    successBoList.add( successBo );
	}
	if ( CommonUtil.isEmpty( returnLogStatus ) || returnLogStatus.equals( "1" ) ) {
	    //流量充值
	    if ( CommonUtil.isNotEmpty( flowId ) && CommonUtil.isNotEmpty( flowRecordId ) && flowId > 0 && flowRecordId > 0 ) {
		try {
		    flowPhoneChong( flowId, flowRecordId, memberId, pbUser, order );//流量充值
		} catch ( BusinessException e ) {
		    logger.error( "流量充值异常：" + e.getMessage() );
		    MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "流量充值异常：" + e.getMessage(), 1, new Date() );
		    mallLogOrderCallbackDAO.insert( callback );
		    throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "流量充值异常：" + e.getMessage(), "1" );
		} catch ( Exception e ) {
		    e.printStackTrace();
		    logger.error( "流量充值异常：" + e.getMessage() );
		    MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "流量充值异常：" + e.getMessage(), 1, new Date() );
		    mallLogOrderCallbackDAO.insert( callback );
		    throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "流量充值异常：" + e.getMessage(), "1" );
		}
	    }
	}
	if ( CommonUtil.isEmpty( returnLogStatus ) || returnLogStatus.equals( "2" ) ) {
	    //微信联盟核销
	    if ( CommonUtil.isNotEmpty( order.getUnionCardId() ) && order.getUnionCardId() > 0 ) {
		UnionConsumeParam unionConsumeParam = new UnionConsumeParam();
		unionConsumeParam.setBusId( order.getBusUserId() );//消费的商家id
		unionConsumeParam.setModel( Constants.UNION_MODEL );//行业模型，商城：1
		unionConsumeParam.setModelDesc( "mallxiadan" );//消费的订单描述
		unionConsumeParam.setOrderNo( order.getOrderNo() );//订单号
		unionConsumeParam.setGiveIntegralNow( false );//是否可以立刻赠送积分，默认立刻赠送，否则请填0
		unionConsumeParam.setType( 2 );//线上或线下使用联盟卡 1：线下 2：线上
		unionConsumeParam.setTotalMoney( CommonUtil.toDouble( order.getOrderOldMoney() ) );//使用联盟卡打折前的价格
		unionConsumeParam.setPayMoney( CommonUtil
				.subtract( CommonUtil.toDouble( order.getOrderMoney() ), CommonUtil.toDouble( order.getOrderFreightMoney() ) ) );//使用联盟卡打折后的价格
		unionConsumeParam.setOrderType( CommonUtil.getUnionType( order.getOrderPayWay() ) );//支付方式：0：现金 1：微信 2：支付宝， 若有其他支付方式，请告知
		//	    unionConsumeParam.setUnionId( 0 );
		unionConsumeParam.setUnionCardId( order.getUnionCardId() );
		unionConsumeParam.setStatus( 1 );//支付状态：1：未支付 2：已支付 3：已退款
		try {
		    boolean flag = unionConsumeService.unionConsume( unionConsumeParam );
		    if ( !flag ) {
			logger.error( "商家联盟线上核销异常" );
			MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "商家联盟线上核销异常", 2, new Date() );
			mallLogOrderCallbackDAO.insert( callback );
			throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "商家联盟线上核销异常", "2" );
		    }
		} catch ( Exception e ) {
		    e.printStackTrace();
		    logger.error( "商家联盟线上核销异常：" + e.getMessage() );
		    MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "商家联盟线上核销异常：" + e.getMessage(), 2, new Date() );
		    mallLogOrderCallbackDAO.insert( callback );
		    throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "商家联盟线上核销异常：" + e.getMessage(), "2" );
		}
	    }
	}
	if ( CommonUtil.isEmpty( returnLogStatus ) || returnLogStatus.equals( "3" ) ) {
	    if ( erpList != null && erpList.size() > 0 && isJxc == 1 ) {
		try {
		    Map< String,Object > erpMap = new HashMap<>();
		    erpMap.put( "orders", JSONArray.fromObject( erpList ) );
		    boolean flag = MallJxcHttpClientUtil.inventoryOperation( erpMap, true );
		    if ( !flag ) {
			//同步失败，信息放到redis
			logger.error( "erp修改库存失败" );
			MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "erp修改库存失败", 3, new Date() );
			mallLogOrderCallbackDAO.insert( callback );
			throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "erp修改库存失败", "3" );
		    }
		    logger.info( "同步库存：" + flag );
		} catch ( BusinessException e ) {
		    logger.error( "erp修改库存异常：" + e.getMessage() );
		    MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "erp修改库存异常：" + e.getMessage(), 3, new Date() );
		    mallLogOrderCallbackDAO.insert( callback );
		    throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "erp修改库存异常：" + e.getMessage(), "3" );
		} catch ( Exception e ) {
		    e.printStackTrace();
		    logger.error( "erp修改库存异常：" + e.getMessage() );
		    MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "erp修改库存异常：" + e.getMessage(), 3, new Date() );
		    mallLogOrderCallbackDAO.insert( callback );
		    throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "erp修改库存异常：" + e.getMessage(), "3" );
		}
	    }
	}
	if ( CommonUtil.isEmpty( returnLogStatus ) || returnLogStatus.equals( "4" ) ) {
	    if ( memberService.isMember( order.getBuyerUserId() ) || isFenbi || isJifen || CommonUtil.isNotEmpty( order.getCouponId() ) ) {
		Map< String,Object > payMap = memberPayService.paySuccessNew( successBoList );
		if ( CommonUtil.isNotEmpty( payMap ) ) {
		    if ( CommonUtil.toInteger( payMap.get( "code" ) ) != 1 ) {
			String msg = ResponseEnums.INTER_ERROR.getDesc();
			if ( CommonUtil.isNotEmpty( payMap.get( "errorMsg" ) ) ) {
			    msg = payMap.get( "errorMsg" ).toString();
			}
			logger.error( "会员回调失败" + payMap.get( "errorMsg" ) );
			MallLogOrderCallback callback = new MallLogOrderCallback( orderNo, "会员回调异常：" + msg, 4, new Date() );
			mallLogOrderCallbackDAO.insert( callback );
			throw new BusinessException( CommonUtil.toInteger( payMap.get( "code" ) ), msg, "4" );
		    }
		}
	    }
	}
	return true;
    }

    /**
     * 短信提醒买家
     */
    private void smsMessageTel( MallOrder order, Member member, BusUser busUser ) {
	String messages = "支付成功，请您耐心等待，我们将稍后为您发货";
	if ( CommonUtil.isNotEmpty( order.getReceiveId() ) ) {
	    if ( CommonUtil.isNotEmpty( order.getReceivePhone() ) ) {
		String telephone = order.getReceivePhone();
		MallPaySet paySet = new MallPaySet();
		paySet.setUserId( member.getBusid() );
		MallPaySet set = mallPaySetService.selectByUserId( paySet );
		if ( CommonUtil.isNotEmpty( set ) ) {
		    if ( set.getIsSmsMember().toString().equals( "1" ) ) {
			if ( CommonUtil.isNotEmpty( set.getSmsMessage() ) ) {
			    JSONObject obj = JSONObject.fromObject( set.getSmsMessage() );
			    if ( obj != null ) {
				if ( CommonUtil.isNotEmpty( obj.get( "1" ) ) ) {
				    messages = obj.getString( "1" );
				}
			    }
			}
			OldApiSms oldApiSms = new OldApiSms();
			oldApiSms.setMobiles( telephone );
			oldApiSms.setCompany( busUser.getMerchant_name() );
			oldApiSms.setBusId( member.getBusid() );
			oldApiSms.setModel( Constants.SMS_MODEL );
			oldApiSms.setContent( CommonUtil.format( messages ) );
			try {
			    smsService.sendSmsOld( oldApiSms );
			} catch ( Exception e ) {
			    e.printStackTrace();
			    logger.error( "短信推送消息异常：" + e.getMessage() );
			}

		    }
		}

	    }
	}

    }

    /**
     * 修改库存
     */
    private Map< String,Object > updateStatusStock( MallOrder order, Map< String,Object > params, Member member, WxPublicUsers wxPublicUser ) {

	if ( order != null && order.getMallOrderDetail() == null ) {
	    order = mallOrderDAO.getOrderById( order.getId() );
	}
	Map< String,Object > map = new HashMap<>();
	if ( order.getOrderType() == 6 ) {
	    mallPresaleService.diffInvNum( order );
	}
	int userPId = busUserService.getMainBusId( order.getBusUserId() );//查询商家总账号
	long isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
	int count = 0;
	//扫码支付和找人代付不修改库存
	if ( ( CommonUtil.isNotEmpty( order.getMallOrderDetail() ) && order.getOrderPayWay() != 5 && order.getOrderPayWay() != 7 ) || params.containsKey( "isPay" ) ) {
	    List< MallOrderDetail > orderDetail = order.getMallOrderDetail();
	    if ( order.getOrderType() == 1 ) {//拼团
		addGroupBuyJoin( order, orderDetail.get( 0 ) );//添加数据到参团表
	    }
	    SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );//设置日期格式
	    String newDate = df.format( new Date() );// 当前系统时间

	    params.put( "payTime", newDate );
	    params.put( "out_trade_no", order.getOrderNo() );
	    if ( orderDetail.size() == 1 ) {
		if ( CommonUtil.isNotEmpty( orderDetail.get( 0 ).getProTypeId() ) ) {
		    if ( orderDetail.get( 0 ).getProTypeId() > 0 ) {
			params.put( "status", 4 );//虚拟物品无需发货，状态改为4  订单完成
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( order.getDeliveryMethod() ) ) {
		if ( order.getDeliveryMethod() == 3 ) {
		    params.put( "status", 4 );
		}
	    }
	    if ( CommonUtil.isNotEmpty( order ) ) {//找人代付不更改状态
		if ( order.getOrderStatus() == 1 ) {
		    if ( CommonUtil.isEmpty( params.get( "status" ) ) ) {
			params.put( "status", 2 );
		    }
		    count = mallOrderDAO.upOrderByorderNo( params );
		    //判断订单是否有父类订单
		    if ( CommonUtil.isNotEmpty( order.getOrderPid() ) && params.get( "status" ).toString().equals( "2" ) ) {
			if ( order.getOrderPid() > 0 ) {
			    //查询父类订单下的子订单是否都已支付
			    int num = mallOrderDAO.selectNoPayByPID( order.getOrderPid() );
			    if ( num == 0 ) {
				Map< String,Object > maps = new HashMap<>();
				maps.put( "orderId", order.getOrderPid() );
				maps.put( "status", params.get( "status" ) );
				mallOrderDAO.upOrderNoOrRemark( maps );
			    }
			}
		    }
		} else if ( order.getOrderPayWay() == 6 && order.getDeliveryMethod() == 2 && order.getOrderStatus() == 2 ) {
		    count = 1;
		}
	    }
	    int discount = 100;
	    if ( count > 0 && null != orderDetail && CommonUtil.isNotEmpty( orderDetail ) ) {//秒杀不立马修改库存，而是经过redis
		count = 0;
		if ( order.getOrderType() == 4 ) {
		    mallAuctionBiddingService.upStateBidding( order, 1, orderDetail );
		}
		for ( MallOrderDetail detail : orderDetail ) {
		    MallProduct pro = mallProductDAO.selectById( detail.getProductId() );
		    if ( isJxc == 0 || !pro.getProTypeId().toString().equals( "0" ) ) {
			//秒杀商品修改redis 的
			if ( order.getOrderType() == 3 ) {
			    //修改库存
			    mallSeckillService.invNum( order, detail, member.getId().toString(), order.getId().toString() );
			} else {//其他商品，则修改数据库的库存
			    mallProductService.diffProductStock( pro, detail, order );
			}
		    }
		    if ( detail.getProTypeId() == 3 && order.getOrderPayWay() != 7 ) { // 卡券购买发布卡券

			fenCard( order );//分配卡券

			String key = Constants.REDIS_KEY + "card_receive_num";
			JedisUtil.map( key, order.getId().toString(), detail.getCardReceiveId().toString() );

		    }
		}
	    }
	    if ( order.getOrderType() == 3 ) {
		//把要修改的库存丢到redis里
		mallSeckillService.addInvNumRedis( order, orderDetail );
	    } else {
		//订单生成成功，把订单加入到未支付的队列中
		String key = Constants.REDIS_KEY + "hOrder_nopay";
		if ( JedisUtil.hExists( key, order.getId().toString() ) ) {
		    JedisUtil.mapdel( key, order.getId().toString() );
		}
	    }
	    if ( order.getOrderType() == 6 && count > 0 ) {//预售商品
		mallPresaleService.paySucessPresale( order );
	    }
	    map.put( "count", count );

	} else if ( order.getOrderPayWay() == 5 ) {//扫码支付直接来修改订单信息
	    params.put( "payTime", new Date() );
	    params.put( "out_trade_no", order.getOrderNo() );
	    params.put( "status", 2 );
	    if ( CommonUtil.isNotEmpty( order ) ) {
		count = mallOrderDAO.upOrderByorderNo( params );
	    }
	    map.put( "count", count );
	} else if ( order.getOrderPayWay() == 7 ) {//找人代付
	    map.put( "count", 1 );
	}
	MallOrder orders = mallOrderDAO.selectById( order.getId() );

	//	MallStore store = mallStoreDAO.selectById( orders.getShopId() );

	List< Map< String,Object > > productList = new ArrayList<>();

	if ( CommonUtil.isNotEmpty( orders ) ) {
	    if ( orders.getOrderStatus() != 1 ) {
		double freightMoney = 0;
		if ( CommonUtil.isNotEmpty( order.getOrderFreightMoney() ) ) {
		    freightMoney = CommonUtil.toDouble( order.getOrderFreightMoney() );
		}
		DecimalFormat df = new DecimalFormat( "######0.00" );
		if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			double commission = 0;
			if ( CommonUtil.isNotEmpty( detail.getCommission() ) && CommonUtil.isNotEmpty( detail.getSaleMemberId() ) ) {
			    commission = CommonUtil.toDouble( detail.getCommission() ) * detail.getDetProNum();
			    commission = CommonUtil.toDouble( df.format( commission ) );
			}
			if ( CommonUtil.isNotEmpty( detail.getSaleMemberId() ) ) {
			    //saleMemberId = detail.getSaleMemberId();
			    double proPrice = CommonUtil.toDouble( detail.getDetProPrice() );
			    double totalPrice = proPrice * detail.getDetProNum();
			    if ( freightMoney > 0 ) {
				totalPrice += freightMoney / order.getMallOrderDetail().size();
			    }
			    totalPrice = CommonUtil.toDouble( df.format( totalPrice ) );

			    mallSellerService.insertSellerIncome( commission, order, detail, totalPrice );
			}
			int erpInvId = 0;
			if ( CommonUtil.isNotEmpty( detail.getProductSpecificas() ) ) {
			    if ( order.getOrderType() != 7 ) {
				Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( detail.getProductSpecificas(), detail.getProductId() );
				if ( CommonUtil.isNotEmpty( invMap ) ) {
				    if ( CommonUtil.isNotEmpty( invMap.get( "erp_inv_id" ) ) ) {
					erpInvId = CommonUtil.toInteger( invMap.get( "erp_inv_id" ) );
				    }
				}
			    } else if ( CommonUtil.isNotEmpty( detail.getProSpecStr() ) ) {
				com.alibaba.fastjson.JSONObject specObj = com.alibaba.fastjson.JSONObject.parseObject( detail.getProSpecStr() );
				for ( String key : specObj.keySet() ) {
				    com.alibaba.fastjson.JSONObject valueObj = specObj.getJSONObject( key );

				    Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( key, detail.getProductId() );
				    if ( CommonUtil.isNotEmpty( invMap ) ) {
					if ( CommonUtil.isNotEmpty( invMap.get( "erp_inv_id" ) ) ) {
					    erpInvId = CommonUtil.toInteger( invMap.get( "erp_inv_id" ) );

					    Map< String,Object > productMap = new HashMap<>();
					    productMap.put( "id", erpInvId );
					    productMap.put( "amount", valueObj.getInteger( "num" ) );//数量
					    productMap.put( "price", valueObj.getDouble( "price" ) );
					    productList.add( productMap );
					}
				    }
				}
			    }

			} else {
			    MallProduct product = mallProductDAO.selectById( detail.getProductId() );
			    if ( CommonUtil.isNotEmpty( product.getErpInvId() ) ) {
				erpInvId = product.getErpInvId();
			    }
			}
			//调用进销存下单时用
			if ( erpInvId > 0 && detail.getProTypeId() == 0 ) {
			    Map< String,Object > productMap = new HashMap<>();
			    productMap.put( "id", erpInvId );
			    productMap.put( "amount", detail.getDetProNum() );//数量
			    productMap.put( "price", detail.getDetProPrice() );
			    productList.add( productMap );
			}

		    }
		}
	    }
	}
	map.put( "count", count );
	return map;
    }

    private String flowPhoneChong( int flowId, int flowRecordId, Integer memberId, WxPublicUsers pbUser, MallOrder orders ) {
	if ( CommonUtil.isNotEmpty( orders.getFlowPhone() ) && flowId > 0 ) {
	    BusFlowInfo flow = fenBiFlowService.getFlowInfoById( flowId );
	    try {
		AdcServicesInfo adcServicesInfo = new AdcServicesInfo();
		adcServicesInfo.setModel( 101 );//模块ID
		adcServicesInfo.setMobile( orders.getFlowPhone() );//充值手机号
		adcServicesInfo.setPrizeCount( flow.getType() );//流量数
		adcServicesInfo.setMemberId( memberId );//用户Id
		adcServicesInfo.setPublicId( pbUser.getId() );//商家公众号Id
		adcServicesInfo.setBusId( orders.getBusUserId() ); //根据平台用户Id获取微信订阅号用户信息
		adcServicesInfo.setId( orders.getId() );//订单id
		adcServicesInfo.setNotifyUrl( PropertiesUtil.getHomeUrl() + "phoneOrder/L6tgXlBFeK/flowSuccess.do" );
		boolean isFlow = fenBiFlowService.adcServices( adcServicesInfo );//流量充值
		if ( isFlow ) {//充值成功
		    /*MallOrder order = new MallOrder();
		    order.setId( orders.getId() );
		    order.setFlowRechargeStatus( 1 );
		    mallOrderDAO.upOrderNoById( order );*/
		} else {
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "流量充值异常" );
		}
	    } catch ( Exception e ) {
		logger.error( "流量充值异常" + e.getMessage() );
		e.printStackTrace();
		throw new BusinessException( ResponseEnums.ERROR.getCode(), "流量充值异常" );
	    }
	} else {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "流量充值请输入要充值的手机号码" );
	}
	return null;
    }

    /**
     * 订单成功添加数据到参团表
     */
    private void addGroupBuyJoin( MallOrder order, MallOrderDetail orderDetail ) {
	Member member = memberService.findMemberById( order.getBuyerUserId(), null );
	MallGroupJoin groupJoin = new MallGroupJoin();
	groupJoin.setGroupBuyId( order.getGroupBuyId() );
	groupJoin.setJoinTime( new Date() );
	groupJoin.setJoinUserId( order.getBuyerUserId() );
	groupJoin.setJoinUserName( member.getNickname() );
	groupJoin.setJoinUserHeadimgurl( member.getHeadimgurl() );
	groupJoin.setOrderId( order.getId() );
	groupJoin.setSpecificaIds( orderDetail.getProductSpecificas() );
	groupJoin.setProductId( orderDetail.getProductId() );
	groupJoin.setOrderDetailId( orderDetail.getId() );
	groupJoin.setJoinStatus( 1 );
	groupJoin.setPJoinId( order.getPJoinId() );
	groupJoin.setJoinPrice( order.getOrderMoney() );
	if ( order.getPJoinId() == 0 ) {
	    groupJoin.setJoinType( 1 );
	} else {
	    groupJoin.setJoinType( 0 );
	}
	mallGroupJoinDAO.insert( groupJoin );
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public void agreanOrderReturn( Map< String,Object > params ) {
	try {
	    String returnNo = params.get( "outTradeNo" ).toString();//订单号
	    MallOrderReturn orderReturn = mallOrderReturnDAO.selectByReturnNo( returnNo );
	    MallOrder order = mallOrderDAO.getOrderById( orderReturn.getOrderId() );
	    int status = 1;
	    if ( orderReturn.getStatus().toString().equals( "3" ) ) {
		status = 5;
	    }
	    orderReturn.setStatus( status );
	    Member member = memberService.findMemberById( orderReturn.getUserId(), null );
	    WxPublicUsers wx = wxPublicUserService.selectByMemberId( member.getBusid() );
	    updateReturnStatus( wx, orderReturn, orderReturn, order );//微信退款

	    //查询订单详情是否已经全部退款
	    if ( !CommonUtil.isEmpty( orderReturn.getOrderId() ) ) {
		boolean flag = isReturnSuccess( order );
		if ( flag ) {
		    //修改父类订单的状态
		    updateOrderStatus( order );
		}
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "支付宝退款失败" );
	}
    }

    /**
     * 修改申请退款
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > updateOrderReturn( MallOrderReturn orderReturn, Object oObj, WxPublicUsers pUser ) throws Exception {
	logger.error( "13131313133" );
	Map< String,Object > resultMap = new HashMap<>();
	boolean rFlag = false;
	String msg = "申请退款失败";
	if ( orderReturn != null ) {
	    if ( !CommonUtil.isEmpty( orderReturn.getStatus() ) ) {

		if ( orderReturn.getStatus() != 1 && orderReturn.getStatus() != 5 ) {
		    updateOrderDetailStatus( orderReturn );
		}

		if ( oObj != null && ( orderReturn.getStatus() == 1 || orderReturn.getStatus() == 5 ) ) {// 同意退款
		    // 获取商户id
		    if ( !CommonUtil.isEmpty( oObj ) ) {
			Map< String,Object > payMap = com.alibaba.fastjson.JSONObject.parseObject( oObj.toString() );
			MallOrderReturn oReturn = mallOrderReturnDAO.selectById( CommonUtil.toInteger( payMap.get( "returnId" ) ) );
			MallOrder order = mallOrderDAO.getOrderById( oReturn.getOrderId() );

			boolean flags = true;

			if ( CommonUtil.isNotEmpty( oReturn.getReturnFenbi() ) && oReturn.getReturnFenbi() > 0 ) {
			    BusUser busUser = busUserService.selectById( order.getBusUserId() );//根据商家id查询商家信息
			    if ( busUser.getFansCurrency() - oReturn.getReturnFenbi() < 0 ) {
				throw new BusinessException( ResponseEnums.FENBI_NULL_ERROR.getCode(), ResponseEnums.FENBI_NULL_ERROR.getDesc() );
			    }
			}
			if ( order != null && flags ) {
			    if ( CommonUtil.isEmpty( pUser ) ) {
				pUser = wxPublicUserService.selectByUserId( order.getBusUserId() );
			    }
			    if ( ( order.getOrderPayWay() == 1 || order.getIsWallet() == 1 ) && CommonUtil.isNotEmpty( pUser ) ) {//微信退款
				if ( Double.parseDouble( oReturn.getRetMoney().toString() ) > 0 ) {//退款金额大于0，则进行微信退款
				    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( order.getOrderNo() );
				    if ( CommonUtil.isNotEmpty( wxPayOrder ) && wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
					WxmemberPayRefund refund = new WxmemberPayRefund();
					refund.setMchid( pUser.getMchId() );// 商户号
					refund.setAppid( pUser.getAppid() );// 公众号
					refund.setTotalFee( wxPayOrder.getTotalFee() );//支付总金额
					refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );//系统单号
					refund.setRefundFee( CommonUtil.toDouble( oReturn.getRetMoney() ) );//退款金额

					logger.error( "微信退款的参数：" + JSONObject.fromObject( refund ).toString() );
					Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );  //微信退款
					logger.error( "微信退款的返回值：" + JSONObject.fromObject( resultmap ) );

					if ( CommonUtil.isNotEmpty( resultmap ) ) {
					    String code = resultmap.get( "code" ).toString();
					    if ( code.equals( "1" ) || code.equals( Constants.FINISH_REFUND_STATUS ) ) {
						//退款成功修改退款状态
						updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

						rFlag = true;
						msg = "退款成功";
					    }
					}
				    } else if ( wxPayOrder.getTradeState().equals( "NOTPAY" ) ) {
					msg = "订单：" + wxPayOrder.getOutTradeNo() + "未支付";
					throw new BusinessException( ResponseEnums.ERROR.getCode(), msg );
				    } else if ( wxPayOrder.getTradeState().equals( "REVOKED" ) ) {
					rFlag = true;
					msg = "退款成功";
					//退款成功修改退款状态
					updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款
				    }
				} else {//退款金额等于0，则直接修改退款状态
				    //退款成功修改退款状态
				    updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款
				    rFlag = true;
				    msg = "退款成功";
				}
			    } else if ( order.getOrderPayWay() == 3 ) {//储值卡退款
				Double retMoney = CommonUtil.toDouble( oReturn.getRetMoney() );
				String orderNo = order.getOrderNo();
				if ( CommonUtil.isNotEmpty( order.getOrderPid() ) ) {
				    MallOrder pOrder = mallOrderDAO.selectById( order.getOrderPid() );
				    if ( CommonUtil.isNotEmpty( pOrder ) ) {
					orderNo = pOrder.getOrderNo();
				    }
				}
				ErpRefundBo erpRefundBo = new ErpRefundBo();
				erpRefundBo.setBusId( order.getBusUserId() );//商家id
				erpRefundBo.setOrderCode( orderNo );////订单号
				erpRefundBo.setRefundPayType( CommonUtil.getMemberPayType( order.getOrderPayWay(), order.getIsWallet() ) );////退款方式 字典1198
				erpRefundBo.setRefundMoney( retMoney ); //退款金额
				erpRefundBo.setRefundDate( new Date().getTime() );
				Map< String,Object > memberResultMap = memberService.refundMoney( erpRefundBo );
				if ( CommonUtil.toInteger( memberResultMap.get( "code" ) ) != 1 ) {
				    //同步失败，存入redis
				    JedisUtil.rPush( Constants.REDIS_KEY + "member_return_jifen", com.alibaba.fastjson.JSONObject.toJSONString( erpRefundBo ) );

				    resultMap.put( "result", false );
				    if ( CommonUtil.isNotEmpty( memberResultMap.get( "errorMsg" ) ) ) {
					resultMap.put( "msg", memberResultMap.get( "errorMsg" ) );
				    }
				} else {
				    updateReturnStatus( pUser, oReturn, orderReturn, order );//退款成功修改退款状态
				}

			    } else if ( order.getOrderPayWay() == 2 || order.getOrderPayWay() == 6 ) {//货到付款和到店支付都不用退钱
				rFlag = true;
				MallOrderDetail detail = new MallOrderDetail();
				detail.setId( orderReturn.getOrderDetailId() );// 修改订单详情的状态
				detail.setStatus( orderReturn.getStatus() );
				int num = mallOrderDetailDAO.updateById( detail );
				if ( num > 0 ) {
				    //退款成功修改商品的库存和销量
				    updateInvenNum( orderReturn, oReturn, order, null );
				}
			    } else if ( order.getOrderPayWay() == 7 ) {//找人代付
				Double retMoney = CommonUtil.toDouble( oReturn.getRetMoney() );
				MallDaifu df = new MallDaifu();
				int orderId = order.getId();
				if ( CommonUtil.isNotEmpty( order.getOrderPid() ) ) {
				    if ( order.getOrderPid() > 0 ) {
					orderId = order.getOrderPid();
				    }
				}
				df.setOrderId( orderId );
				//查询找人代付的信息
				MallDaifu daifu = mallDaifuDAO.selectBydf( df );
				double returnMoneys = 0;
				boolean flag = false;
				if ( CommonUtil.isNotEmpty( daifu.getDfPayMoney() ) ) {
				    double payMoney = CommonUtil.toDouble( daifu.getDfPayMoney() );
				    double dfReturnMoney = 0;
				    if ( CommonUtil.isNotEmpty( daifu.getDfReturnMoney() ) ) {
					dfReturnMoney = CommonUtil.toDouble( daifu.getDfReturnMoney() );
				    }
				    returnMoneys = dfReturnMoney + retMoney;
				    if ( returnMoneys <= payMoney && retMoney <= payMoney ) {
					flag = true;
				    }
				}
				if ( retMoney > 0 && flag ) {//退款金额大于0，则进行微信退款
				    if ( daifu.getDfPayWay().toString().equals( "1" ) && CommonUtil.isNotEmpty( pUser ) ) {
					// 根据订单号查询支付订单信息
					WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( daifu.getDfOrderNo() );
					if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {

					    WxmemberPayRefund refund = new WxmemberPayRefund();
					    refund.setMchid( pUser.getMchId() );// 商户号
					    refund.setAppid( pUser.getAppid() );// 公众号
					    refund.setTotalFee( wxPayOrder.getTotalFee() );//支付总金额
					    refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );//系统单号
					    refund.setRefundFee( CommonUtil.toDouble( oReturn.getRetMoney() ) );//退款金额
					    logger.info( "appletWxReturnParams:" + JSONObject.fromObject( refund ).toString() );

					    logger.error( "微信退款的参数" + JSONObject.fromObject( refund ).toString() );
					    Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );  //微信退款
					    logger.error( "微信退款的返回值：" + JSONObject.fromObject( resultmap ) );
					    if ( resultmap != null ) {
						String code = resultmap.get( "code" ).toString();
						if ( code.equals( "1" ) || code.equals( Constants.FINISH_REFUND_STATUS ) ) {
						    //退款成功修改退款状态
						    updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款
						    if ( order.getOrderPayWay() == 7 ) {//找人代付
							MallDaifu daifus = new MallDaifu();
							daifus.setId( daifu.getId() );
							daifus.setDfReturnNo( oReturn.getReturnNo() );
							daifus.setDfReturnMoney( BigDecimal.valueOf( returnMoneys ) );
							daifus.setDfReturnTime( new Date() );
							if ( returnMoneys == CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
							    daifus.setDfReturnStatus( 2 );
							} else if ( returnMoneys < CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
							    daifus.setDfReturnStatus( 1 );
							}
							mallDaifuDAO.updateById( daifus );
						    }
						    rFlag = true;
						    msg = "退款成功";
						}
					    }
					} else if ( wxPayOrder.getTradeState().equals( "NOTPAY" ) ) {
					    msg = "订单：" + wxPayOrder.getOutTradeNo() + "未支付";
					    throw new BusinessException( ResponseEnums.ERROR.getCode(), msg );
					} else if ( wxPayOrder.getTradeState().equals( "REVOKED" ) ) {
					    //退款成功修改退款状态
					    updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款
					    if ( order.getOrderPayWay() == 7 ) {//找人代付
						MallDaifu daifus = new MallDaifu();
						daifus.setId( daifu.getId() );
						daifus.setDfReturnNo( oReturn.getReturnNo() );
						daifus.setDfReturnMoney( BigDecimal.valueOf( returnMoneys ) );
						daifus.setDfReturnTime( new Date() );
						if ( returnMoneys == CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
						    daifus.setDfReturnStatus( 2 );
						} else if ( returnMoneys < CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
						    daifus.setDfReturnStatus( 1 );
						}
						mallDaifuDAO.updateById( daifus );
					    }
					    rFlag = true;
					    msg = "退款成功";
					}
				    } else if ( daifu.getDfPayWay().toString().equals( "2" ) ) {//支付宝退款
					rFlag = false;
					resultMap.put( "code", 1 );
				    } else if ( daifu.getDfPayWay().toString().equals( "3" ) ) {//微信小程序退款
					if ( Double.parseDouble( oReturn.getRetMoney().toString() ) > 0 ) {//退款金额大于0，则进行微信退款
					    //根据订单号查询支付订单信息
					    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( daifu.getDfOrderNo() );
					    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
						BusIdAndindustry busIdAndindustry = new BusIdAndindustry();
						busIdAndindustry.setBusId( order.getBusUserId() );
						busIdAndindustry.setIndustry( Constants.APPLET_STYLE );
						ApiWxApplet applet = wxPublicUserService.selectBybusIdAndindustry( busIdAndindustry );
						WxmemberPayRefund refund = new WxmemberPayRefund();
						refund.setMchid( applet.getMchId() );// 商户号
						refund.setAppid( applet.getAppid() );// 公众号
						refund.setTotalFee( wxPayOrder.getTotalFee() );//支付总金额
						refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );//系统单号
						refund.setRefundFee( CommonUtil.toDouble( oReturn.getRetMoney() ) );//退款金额
						logger.info( "小程序退款参数：" + JSONObject.fromObject( refund ).toString() );
						Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );//小程序退款
						logger.info( "小程序退款返回值：" + JSONObject.fromObject( resultmap ) );
						if ( CommonUtil.isNotEmpty( resultmap ) ) {
						    String code = resultmap.get( "code" ).toString();
						    if ( code.equals( "1" ) || code.equals( Constants.FINISH_REFUND_STATUS ) ) {
							//退款成功修改退款状态
							updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款
							if ( order.getOrderPayWay() == 7 ) {//找人代付
							    MallDaifu daifus = new MallDaifu();
							    daifus.setId( daifu.getId() );
							    daifus.setDfReturnNo( oReturn.getReturnNo() );
							    daifus.setDfReturnMoney( BigDecimal.valueOf( returnMoneys ) );
							    daifus.setDfReturnTime( new Date() );
							    if ( returnMoneys == CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
								daifus.setDfReturnStatus( 2 );
							    } else if ( returnMoneys < CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
								daifus.setDfReturnStatus( 0 );
							    }
							    mallDaifuDAO.updateById( daifus );
							}
							rFlag = true;
							msg = "退款成功";
						    }
						}
					    } else if ( wxPayOrder.getTradeState().equals( "REVOKED" ) ) {
						//退款成功修改退款状态
						updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款
						if ( order.getOrderPayWay() == 7 ) {//找人代付
						    MallDaifu daifus = new MallDaifu();
						    daifus.setId( daifu.getId() );
						    daifus.setDfReturnNo( oReturn.getReturnNo() );
						    daifus.setDfReturnMoney( BigDecimal.valueOf( returnMoneys ) );
						    daifus.setDfReturnTime( new Date() );
						    if ( returnMoneys == CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
							daifus.setDfReturnStatus( 2 );
						    } else if ( returnMoneys < CommonUtil.toDouble( daifu.getDfPayMoney() ) ) {
							daifus.setDfReturnStatus( 0 );
						    }
						    mallDaifuDAO.updateById( daifus );
						}
						rFlag = true;
						msg = "退款成功";
					    }

					} else {//退款金额等于0，则直接修改退款状态
					    //退款成功修改退款状态
					    updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

					    rFlag = true;
					    msg = "退款成功";
					}

				    }
				}
			    } else if ( order.getOrderPayWay() == 9 ) {//支付宝退款
				rFlag = false;
				resultMap.put( "code", 1 );
			    } else if ( order.getOrderPayWay() == 10 ) {//微信小程序退款
				if ( Double.parseDouble( oReturn.getRetMoney().toString() ) > 0 ) {//退款金额大于0，则进行微信退款
				    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( payMap.get( "orderNo" ).toString() );
				    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
					BusIdAndindustry busIdAndindustry = new BusIdAndindustry();
					busIdAndindustry.setBusId( order.getBusUserId() );
					busIdAndindustry.setIndustry( Constants.APPLET_STYLE );
					ApiWxApplet applet = wxPublicUserService.selectBybusIdAndindustry( busIdAndindustry );
					WxmemberPayRefund refund = new WxmemberPayRefund();
					refund.setMchid( applet.getMchId() );// 商户号
					refund.setAppid( applet.getAppid() );// 公众号
					refund.setTotalFee( wxPayOrder.getTotalFee() );//支付总金额
					refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );//系统单号
					refund.setRefundFee( CommonUtil.toDouble( oReturn.getRetMoney() ) );//退款金额
					logger.info( "小程序退款参数：" + JSONObject.fromObject( refund ).toString() );
					Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );//小程序退款
					logger.info( "小程序退款返回值：" + JSONObject.fromObject( resultmap ) );
					if ( resultmap != null ) {
					    String code = resultmap.get( "code" ).toString();
					    if ( code.equals( "1" ) || code.equals( Constants.FINISH_REFUND_STATUS ) ) {
						//退款成功修改退款状态
						updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

						rFlag = true;
						msg = "退款成功";
					    }
					}
				    } else if ( wxPayOrder.getTradeState().equals( "NOTPAY" ) ) {
					msg = "订单：" + wxPayOrder.getOutTradeNo() + "未支付";
					throw new BusinessException( ResponseEnums.ERROR.getCode(), msg );
				    } else if ( wxPayOrder.getTradeState().equals( "REVOKED" ) ) {
					//退款成功修改退款状态
					updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

					rFlag = true;
					msg = "退款成功";
				    }
				} else {//退款金额等于0，则直接修改退款状态
				    //退款成功修改退款状态
				    updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

				    rFlag = true;
				    msg = "退款成功";
				}
			    } else if ( order.getOrderPayWay() == 11 ) {
				TRefundOrder refundOrder = new TRefundOrder();
				refundOrder.setBizOrderNo( oReturn.getReturnNo() );//商户退款订单号
				refundOrder.setOriBizOrderNo( payMap.get( "orderNo" ).toString() );//商户原订单号(支付单号)
				refundOrder.setAmount( CommonUtil.toDouble( oReturn.getRetMoney() ) );//订单金额
				refundOrder.setBackUrl( Constants.PRESALE_REFUND_URL );//异步回调通知
				refundOrder.setBusId( order.getBusUserId() );//商家id
				Map< String,Object > refundResultMap = payService.walletRefund( refundOrder );
				if ( CommonUtil.toInteger( refundResultMap.get( "code" ) ) == 1 ) {
				    rFlag = true;
				} else {
				    rFlag = false;
				    if ( CommonUtil.isNotEmpty( refundResultMap.get( "errorMsg" ) ) ) {
					msg = refundResultMap.get( "errorMsg" ).toString();
				    }
				}
			    }
			}

		    }

		} else {
		    rFlag = true;
		}
		//查询订单详情是否已经全部退款
		if ( !CommonUtil.isEmpty( orderReturn.getOrderId() ) && rFlag ) {
		    MallOrder order = mallOrderDAO.getOrderById( orderReturn.getOrderId() );

		    boolean flag = isReturnSuccess( order );
		    if ( flag ) {
			if ( order.getUnionCardId() > 0 ) {
			    UnionRefundParam unionRefundParam = new UnionRefundParam();
			    unionRefundParam.setOrderNo( order.getOrderNo() );
			    unionRefundParam.setModel( Constants.UNION_MODEL );
			    unionConsumeService.unionRefund( unionRefundParam );
			}
			//修改父类订单的状态
			updateOrderStatus( order );
		    }
		}
	    } else {
		orderReturn.setUpdateTime( new Date() );
		mallOrderReturnDAO.updateById( orderReturn );// 修改退款订单的状态
	    }
	}
	resultMap.put( "flag", rFlag );
	resultMap.put( "msg", msg );
	return resultMap;
    }

    /**
     * 退款成功修改订单状态
     */
    private void updateOrderStatus( MallOrder order ) {
	boolean flags = false;
	if ( CommonUtil.isNotEmpty( order.getOrderPid() ) ) {
	    if ( order.getOrderPid() > 0 ) {
		//List< Map< String,Object > > list = mallOrderDAO.selectOrderPid( order.getOrderPid() );
		Wrapper< MallOrder > wrapper = new EntityWrapper<>();
		wrapper.setSqlSelect( "id,shop_id,order_status,order_no" );
		wrapper.where( "order_pid = {0}", order.getOrderPid() );
		List< Map< String,Object > > list = mallOrderDAO.selectMaps( wrapper );
		if ( list != null && list.size() > 0 ) {
		    for ( Object object : list ) {
			JSONObject obj = JSONObject.fromObject( object );
			if ( obj.get( "order_status" ).toString().equals( "5" ) ) {
			    flags = true;
			} else {
			    flags = false;
			    break;
			}
		    }
		}
		if ( flags ) {
		    //修改父类订单的状态
		    MallOrder mallOrder = new MallOrder();
		    mallOrder.setOrderStatus( 5 );
		    mallOrder.setUpdateTime( new Date() );
		    mallOrder.setId( order.getOrderPid() );
		    mallOrderDAO.updateById( mallOrder );
		}
	    }
	}
    }

    /**
     * 退款成功，添加退款记录
     */
    private void updateReturnStatus( WxPublicUsers pUser, MallOrderReturn oReturn, MallOrderReturn orderReturn, MallOrder order ) throws Exception {
	//退款成功修改商品的库存和销量
	updateInvenNum( orderReturn, oReturn, order, null );
	//添加交易记录
	MallIncomeList incomeList = new MallIncomeList();
	incomeList.setBusId( order.getBusUserId() );
	incomeList.setIncomeType( 2 );
	incomeList.setIncomeCategory( 1 );
	incomeList.setIncomeMoney( order.getOrderMoney() );
	incomeList.setShopId( order.getShopId() );
	incomeList.setBuyerId( order.getBuyerUserId() );
	incomeList.setBuyerName( order.getMemberName() );
	incomeList.setTradeType( 2 );
	if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
	    incomeList.setTradeId( order.getMallOrderDetail().get( 0 ).getId() );
	    incomeList.setProName( order.getMallOrderDetail().get( 0 ).getDetProName() );
	}
	if ( order.getOrderPayWay() == 4 ) {
	    incomeList.setIncomeUnit( 3 );
	} else if ( order.getOrderPayWay() == 8 ) {
	    incomeList.setIncomeUnit( 2 );
	}
	incomeList.setProNo( order.getOrderNo() );
	incomeList.setCreateTime( new Date() );
	mallIncomeListService.insert( incomeList );

	//退款成功，添加当天营业额记录
	mallCountIncomeService.saveTurnover( order.getShopId(), null, oReturn.getRetMoney() );
    }

    @Override
    public void walletReturnOrder( String orderNo ) throws Exception {
	MallOrder order = mallOrderDAO.selectOrderByOrderNo( orderNo );
	if ( CommonUtil.isNotEmpty( order ) ) {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "id", order.getId() );
	    List idList = mallOrderDAO.selectOrderPid( order.getId() );
	    if ( idList.size() > 0 ) {
		for ( Object object : idList ) {
		    JSONObject obj = JSONObject.fromObject( object.toString() );
		    params.put( "id", obj.get( "id" ) );

		    List< MallOrderDetail > detailList = mallOrderDetailDAO.selectByOrderId( obj.getInt( "id" ) );
		    if ( detailList != null && detailList.size() > 0 ) {
			for ( MallOrderDetail mallOrderDetail : detailList ) {
			    MallOrderReturn refund = new MallOrderReturn();
			    refund.setOrderId( order.getId() );
			    refund.setOrderDetailId( mallOrderDetail.getId() );
			    MallOrderReturn orderReturn = mallOrderReturnDAO.selectByOrderDetailId( refund );
			    orderReturn.setStatus( 1 );
			    updateInvenNum( orderReturn, null, order, mallOrderDetail );
			}
		    }
		}
	    } else {
		List< MallOrderDetail > detailList = mallOrderDetailDAO.selectByOrderId( order.getId() );
		if ( detailList != null && detailList.size() > 0 ) {
		    for ( MallOrderDetail mallOrderDetail : detailList ) {
			MallOrderReturn refund = new MallOrderReturn();
			refund.setOrderId( order.getId() );
			refund.setOrderDetailId( mallOrderDetail.getId() );
			MallOrderReturn orderReturn = mallOrderReturnDAO.selectByOrderDetailId( refund );
			orderReturn.setStatus( 1 );
			updateInvenNum( orderReturn, null, order, mallOrderDetail );
		    }
		}
	    }
	    params.put( "orderId", order.getId() );
	    params.put( "status", 5 );
	    mallOrderDAO.upOrderNoOrRemark( params );
	}
    }

    /**
     * 修改退款表的状态，并退款成功修改库存和销量
     */
    private void updateInvenNum( MallOrderReturn orderReturn, MallOrderReturn oReturn, MallOrder order, MallOrderDetail detail ) {
	if ( orderReturn != null ) {
	    updateOrderDetailStatus( orderReturn );
	}

	if ( CommonUtil.isNotEmpty( orderReturn ) && CommonUtil.isNotEmpty( oReturn ) ) {
	    updateOrderInvNum( order, orderReturn.getOrderDetailId(), oReturn.getReturnFenbi(), oReturn.getReturnJifen(), CommonUtil.toDouble( oReturn.getRetMoney() ) );
	} else {
	    updateOrderInvNum( order, detail.getId(), detail.getUseFenbi(), detail.getUseJifen(), CommonUtil.toDouble( order.getOrderMoney() ) );
	}

    }

    private void updateOrderDetailStatus( MallOrderReturn orderReturn ) {
	if ( orderReturn != null ) {
	    orderReturn.setUpdateTime( new Date() );
	    mallOrderReturnDAO.updateById( orderReturn );// 修改退款订单的状态

	    MallOrderDetail mallOrderDetail = new MallOrderDetail();
	    mallOrderDetail.setId( orderReturn.getOrderDetailId() );// 修改订单详情的状态
	    mallOrderDetail.setStatus( orderReturn.getStatus() );

	    mallOrderDetailDAO.updateById( mallOrderDetail );
	}
    }

    private void updateOrderInvNum( MallOrder order, int orderDetailId, double returnFenbi, double returnJifen, double returnMoney ) {
	Map< String,Object > detailMap = mallOrderDAO.selectByDIdOrder( orderDetailId );
	Integer productId = 0;
	Integer productNum = 0;
	int orderType = 0;
	int seckillId = 0;
	double total_price = 0;
	if ( detailMap != null ) {
	    productId = CommonUtil.toInteger( detailMap.get( "productId" ) );
	    productNum = CommonUtil.toInteger( detailMap.get( "proNum" ) );
	    if ( CommonUtil.isNotEmpty( detailMap.get( "order_type" ) ) ) {
		orderType = CommonUtil.toInteger( detailMap.get( "order_type" ) );
		seckillId = CommonUtil.toInteger( detailMap.get( "group_buy_id" ) );
	    }
	    if ( CommonUtil.isNotEmpty( detailMap.get( "total_price" ) ) ) {
		total_price = CommonUtil.toDouble( detailMap.get( "total_price" ) );
	    } else {
		total_price = CommonUtil.toDouble( detailMap.get( "proPrice" ) );
	    }
	}
	if ( productId > 0 ) {
	    //修改商品总库存
	    MallProduct product = mallProductDAO.selectById( productId );
	    if ( product != null ) {
		int userPId = busUserService.getMainBusId( product.getUserId() );//通过用户名查询主账号id
		long isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
		int erpInvenId = product.getErpInvId();
		String invKey = Constants.REDIS_SECKILL_NAME;//秒杀库存的key
		if ( isJxc == 0 || !product.getProTypeId().toString().equals( "0" ) ) {
		    //		    MallProduct p = new MallProduct();
		    //		    p.setId( product.getId() );
		    //		    p.setProStockTotal( product.getProStockTotal() + productNum );//商品库存
		    //		    if ( product.getProSaleTotal() - productNum > 0 ) {
		    //			p.setProSaleTotal( product.getProSaleTotal() - productNum );//商品销量
		    //		    }
		    //		    mallProductDAO.updateById( p );
		    Map< String,Object > productParams = new HashMap<>();
		    productParams.put( "type", 1 );
		    productParams.put( "product_id", product.getId() );
		    productParams.put( "pro_num", productNum );
		    mallProductDAO.updateProductStock( productParams );
		}

		if ( orderType == 3 && seckillId > 0 ) {
		    String field = seckillId + "";
		    if ( JedisUtil.hExists( invKey, field ) ) {
			int invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, field ) );
			JedisUtil.map( invKey, field, ( invNum + 1 ) + "" );
		    }
		}
		//修改商品规格库存
		if ( product.getIsSpecifica() == 1 ) {
		    String specIds = mallOrderDAO.selectSpecByDetailId( orderDetailId );
		    Map< String,Object > proMap = new HashMap<>();
		    proMap.put( "specificaIds", specIds );
		    proMap.put( "proId", productId );
		    MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( proMap );
		    erpInvenId = proInv.getErpInvId();
		    /*int total = proInv.getInvNum() + productNum;//库存
		    if ( total < 0 ) {
			total = 0;
		    }
		    int invSaleNum;
		    if ( !CommonUtil.isEmpty( proInv.getInvSaleNum() ) ) {
			invSaleNum = proInv.getInvSaleNum() - productNum;//销量
		    } else {
			invSaleNum = productNum;//销量
		    }
		    proMap.put( "total", total );
		    proMap.put( "saleNum", invSaleNum );*/
		    if ( isJxc == 0 || !product.getProTypeId().toString().equals( "0" ) ) {
			mallProductInventoryService.updateProductInventory( proInv, productNum, 1 );//修改规格的库存
		    }

		    String field = seckillId + "_" + detailMap.get( "product_specificas" ).toString();
		    if ( orderType == 3 && seckillId > 0 && JedisUtil.hExists( invKey, field ) ) {
			int invNum = CommonUtil.toInteger( JedisUtil.maoget( invKey, field ) );
			//有规格，取规格的库存
			JedisUtil.map( invKey, field, ( invNum + 1 ) + "" );
		    }
		}

		//同步进销存的库存
		if ( erpInvenId > 0 && isJxc == 1 && product.getProTypeId().toString().equals( "0" ) ) {
		    BusUser user = busUserService.selectById( order.getBusUserId() );//查询商家信息
		    int uType = 1;//用户类型 1总账号  0子账号
		    if ( !order.getBusUserId().toString().equals( CommonUtil.toString( userPId ) ) ) {
			uType = 0;
		    }
		    List< Map< String,Object > > erpList = new ArrayList<>();
		    MallStore store = mallStoreDAO.selectById( product.getShopId() );

		    Map< String,Object > erpMap = new HashMap<>();
		    erpMap.put( "uId", order.getBusUserId() );
		    erpMap.put( "uType", uType );//用户类型
		    erpMap.put( "uName", user.getName() );
		    erpMap.put( "rootUid", userPId );
		    erpMap.put( "type", 1 );//退单入库
		    erpMap.put( "remark", "商城退款" );
		    erpMap.put( "shopId", store.getWxShopId() );

		    List< Map< String,Object > > productList = new ArrayList<>();
		    Map< String,Object > productMap = new HashMap<>();
		    productMap.put( "id", erpInvenId );
		    productMap.put( "amount", productNum );//数量
		    productMap.put( "price", total_price );
		    productList.add( productMap );

		    erpMap.put( "products", productList );

		    erpList.add( erpMap );

		    Map< String,Object > erpMaps = new HashMap<>();
		    erpMaps.put( "orders", JSONArray.fromObject( erpList ) );
		    boolean flag = MallJxcHttpClientUtil.inventoryOperation( erpMaps, true );
		    if ( !flag ) {
			//同步失败，信息放到redis
			JedisUtil.rPush( Constants.REDIS_KEY + "erp_inven", JSONObject.fromObject( erpMaps ).toString() );
		    }
		    logger.info( "同步库存：" + flag );
		}
	    }

	    if ( memberService.isMember( order.getBuyerUserId() ) ) {

		ErpRefundBo erpRefundBo = new ErpRefundBo();
		erpRefundBo.setBusId( order.getBusUserId() );//商家id
		erpRefundBo.setOrderCode( detailMap.get( "order_no" ).toString() );////订单号
		erpRefundBo.setRefundPayType( CommonUtil.getMemberPayType( order.getOrderPayWay(), order.getIsWallet() ) );////退款方式 字典1198
		erpRefundBo.setRefundMoney( returnMoney ); //退款金额
		erpRefundBo.setRefundJifen( CommonUtil.toIntegerByDouble( returnJifen ) );//退款积分
		erpRefundBo.setRefundFenbi( returnFenbi ); //退款粉币
		erpRefundBo.setRefundDate( new Date().getTime() );
		Map< String,Object > resultMap = memberService.refundMoney( erpRefundBo );
		if ( CommonUtil.toInteger( resultMap.get( "code" ) ) != 1 ) {
		    //同步失败，存入redis
		    JedisUtil.rPush( Constants.REDIS_KEY + "member_return_jifen", com.alibaba.fastjson.JSONObject.toJSONString( erpRefundBo ) );
		}
	    }
	}

	MallOrderDetail orderDetail = mallOrderDetailDAO.selectById( orderDetailId );
	int saleMemberId = 0;//销售员id
	if ( CommonUtil.isNotEmpty( orderDetail ) ) {
	    if ( CommonUtil.isNotEmpty( orderDetail.getSaleMemberId() ) ) {
		saleMemberId = CommonUtil.toInteger( orderDetail.getSaleMemberId() );
	    }
	}
	if ( saleMemberId > 0 ) {
	    mallSellerService.updateSellerIncome( orderDetail );
	}
    }

    @Override
    public Integer selectSpeBySpeValueId( Map< String,Object > params ) {
	MallProductSpecifica spe = mallProductSpecificaService.selectByNameValueId( params );
	return spe.getId();
    }

    private MallOrder getAdressOrder( MallOrder order, Map< String,Object > addressMap ) {
	if ( CommonUtil.isNotEmpty( addressMap ) ) {
	    String address = CommonUtil.toString( addressMap.get( "pName" ) ) + CommonUtil.toString( addressMap.get( "cName" ) );
	    address += CommonUtil.toString( addressMap.get( "mem_address" ) );
	    if ( CommonUtil.isNotEmpty( addressMap.get( "mem_zip_code" ) ) ) {
		address += CommonUtil.toInteger( addressMap.get( "mem_zip_code" ) );
	    }
	    order.setReceiveName( CommonUtil.toString( addressMap.get( "mem_name" ) ) );
	    order.setReceivePhone( CommonUtil.toString( addressMap.get( "mem_phone" ) ) );
	    order.setReceiveAddress( address );
	}
	return order;
    }

    /**
     * 好友代付成功回调函数
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int paySuccessDaifu( Map< String,Object > params ) {
	String orderNo = params.get( "out_trade_no" ).toString();

	MallDaifu daifu = mallDaifuDAO.selectByDfOrderNo( orderNo );
	MallDaifu df = new MallDaifu();
	df.setId( daifu.getId() );
	df.setDfPayStatus( 1 );
	df.setDfPayTime( new Date() );
	params.put( "isPay", 1 );//已经支付成功
	int code = mallDaifuDAO.updateById( df );//修改代付的状态
	if ( code > 0 ) {
	    List idList = mallOrderDAO.selectOrderPid( daifu.getOrderId() );
	    if ( idList.size() > 0 ) {
		for ( Object anIdList : idList ) {
		    JSONObject obj = JSONObject.fromObject( anIdList.toString() );
		    Integer orderId = Integer.parseInt( obj.get( "id" ).toString() );
		    updateOrderStatus( orderId, params );
		}
		params.put( "status", 2 );
		params.put( "orderId", daifu.getOrderId() );
		//修改父类订单的状态
		mallOrderDAO.upOrderNoOrRemark( params );
	    } else {
		updateOrderStatus( daifu.getOrderId(), params );
	    }
	}
	return code;
    }

    /**
     * 订单代付修改订单状态
     */
    private void updateOrderStatus( int orderId, Map< String,Object > params ) {
	MallOrder order = mallOrderDAO.getOrderById( orderId );
	if ( CommonUtil.isNotEmpty( order ) ) {
	    params.put( "status", 2 );

	    params.put( "payTime", new Date() );
	    params.put( "out_trade_no", order.getOrderNo() );
	    mallOrderDAO.upOrderByorderNo( params );

	    WxPublicUsers pbUser = wxPublicUserService.selectByMemberId( order.getBuyerUserId() );
	    updateStatusStock( order, params, null, pbUser );

	    List< MallOrder > mallOrderList = new ArrayList<>();
	    mallOrderList.add( order );
	    paySuccess( mallOrderList, pbUser, null, order.getOrderNo() );//支付成功回调储值卡支付，积分支付，粉币支付

	    fenCard( order );

	}
    }

    /**
     * 代付判断商品库存
     */
    private Map< String,Object > isDaifuProInvNum( Integer orderId ) {
	Map< String,Object > resultMap = new HashMap<>();
	//判断商品的库存是否已经卖完了
	MallOrder order = mallOrderDAO.getOrderById( orderId );
	if ( CommonUtil.isNotEmpty( order.getMallOrderDetail() ) ) {//扫码支付不修改库存
	    List< MallOrderDetail > orderDetailList = order.getMallOrderDetail();
	    for ( MallOrderDetail orderDetail : orderDetailList ) {
		boolean flag = false;
		Map< String,Object > proMap = new HashMap<>();
		MallProduct pro = mallProductDAO.selectById( orderDetail.getProductId() );
		int proNum = orderDetail.getDetProNum();
		Integer total = ( pro.getProStockTotal() - proNum );
		proMap.put( "proId", pro.getId() );
		if ( pro.getIsSpecifica() == 0 ) {
		    if ( pro.getIsSpecifica() == 1 ) {//该商品存在规格
			String[] specifica = ( orderDetail.getProductSpecificas() ).split( "," );
			StringBuilder ids = new StringBuilder( "0" );
			for ( String aSpecifica : specifica ) {
			    proMap.put( "valueId", aSpecifica );
			    MallProductSpecifica specifica1 = mallProductSpecificaService.selectByNameValueId( proMap );
			    if ( CommonUtil.isNotEmpty( ids ) ) {
				ids.append( "," );
			    }
			    ids.append( specifica1.getId() );
			}
			proMap.put( "specificaIds", ids );
			MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( proMap );//根据商品规格id查询商品库存
			total = ( proInv.getInvNum() - orderDetail.getDetProNum() );
			if ( CommonUtil.isNotEmpty( proInv ) ) {
			    if ( total <= proNum ) {
				resultMap.put( "result", false );
				resultMap.put( "msg", "库存不够" );
				break;
			    } else {
				flag = true;
				resultMap.put( "result", true );
			    }
			} else {
			    resultMap.put( "result", false );
			    resultMap.put( "msg", "库存不够" );
			    break;
			}
		    }
		}
		if ( total <= 0 ) {
		    resultMap.put( "result", false );
		    resultMap.put( "msg", "库存不够" );
		} else {
		    flag = true;
		}
		resultMap.put( "result", flag );
		if ( !flag ) {
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "库存不够" );
		    //		    break;
		}
	    }
	    resultMap.put( "proTypeId", orderDetailList.get( 0 ).getProTypeId() );
	}
	resultMap.put( "busId", order.getBusUserId() );
	return resultMap;
    }

    @Override
    public Map< String,Object > addMallDaifu( MallDaifu daifu ) throws Exception {
	Map< String,Object > resultMap = new HashMap<>();
	MallOrder mallOrder = mallOrderDAO.selectById( daifu.getOrderId() );
	daifu.setDfPayMoney( mallOrder.getOrderMoney() );
	List idList = mallOrderDAO.selectOrderPid( daifu.getOrderId() );
	if ( idList.size() > 0 ) {
	    for ( Object anIdList : idList ) {
		JSONObject obj = JSONObject.fromObject( anIdList.toString() );
		Integer orderId = Integer.parseInt( obj.get( "id" ).toString() );
		//判断代付的库存
		resultMap = isDaifuProInvNum( orderId );
		if ( CommonUtil.isNotEmpty( resultMap ) ) {
		    if ( resultMap.get( "result" ).toString().equals( "false" ) ) {
			break;
		    }
		}
	    }
	} else {
	    //判断代付的库存
	    resultMap = isDaifuProInvNum( daifu.getOrderId() );
	}

	if ( resultMap.get( "result" ).toString().equals( "true" ) ) {
	    boolean flag = true;
	    int id = 0;
	    int code = 0;
	    String msg = "";
	    List< MallDaifu > dfList = mallDaifuDAO.selectByPayDaifu( daifu );
	    if ( dfList != null && dfList.size() > 0 ) {
		if ( dfList.size() == 1 ) {
		    MallDaifu daifus = dfList.get( 0 );
		    flag = daifus.getDfUserId().toString().equals( daifu.getDfUserId().toString() );
		} else {
		    flag = false;
		}
		if ( !flag ) {
		    resultMap.put( "result", false );
		    resultMap.put( "msg", "已经有其他好友帮忙代付了" );
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), "已经有其他好友帮忙代付了" );
		}
	    }
	    MallDaifu df = mallDaifuDAO.selectBydf( daifu );
	    if ( df == null ) {
		daifu.setDfCreateTime( new Date() );
		daifu.setDfOrderNo( "DF" + System.currentTimeMillis() );
		code = mallDaifuDAO.insert( daifu );
	    } else {
		daifu.setDfOrderNo( df.getDfOrderNo() );
		daifu.setId( df.getId() );
		if ( CommonUtil.isNotEmpty( daifu.getDfPayStatus() ) ) {
		    switch ( daifu.getDfPayStatus().toString() ) {
			case "0":
			    code = mallDaifuDAO.updateById( daifu );
			    break;
			case "1":
			    msg = "您已经帮您的朋友付过钱，不用再付钱了";
			    flag = false;
			    break;
			case "-1":
			    msg = "您的朋友已经退款了，不用再付钱了";
			    flag = false;
			    break;
			default:
			    break;
		    }
		    if ( !flag ) {
			throw new BusinessException( ResponseEnums.ERROR.getCode(), msg );
		    }
		} else {
		    code = mallDaifuDAO.updateById( daifu );
		}

	    }
	    if ( code > 0 && CommonUtil.isNotEmpty( daifu.getId() ) ) {
		id = daifu.getId();
	    }
	    if ( daifu.getDfPayWay() == 1 || daifu.getDfPayWay() == 2 || daifu.getDfPayWay() == 4 ) {
		MallOrder order = mallOrderDAO.selectById( daifu.getOrderId() );
		int payWay = 1;
		if ( daifu.getDfPayWay() == 2 ) {
		    payWay = 9;
		} else if ( daifu.getDfPayWay() == 4 ) {
		    payWay = 11;
		}
		order.setBuyerUserId( daifu.getDfUserId() );
		String url = mallOrderSubmitService.wxPayWay( CommonUtil.toDouble( daifu.getDfPayMoney() ), daifu.getDfOrderNo(), order, payWay );
		resultMap.put( "url", url );
	    }
	    resultMap.put( "result", flag );
	    resultMap.put( "msg", msg );
	    resultMap.put( "id", id );
	    resultMap.put( "orderId", daifu.getOrderId() );
	    resultMap.put( "no", daifu.getDfOrderNo() );
	    resultMap.put( "orderMoney", daifu.getDfPayMoney() );
	}
	return resultMap;
    }

    @Override
    public boolean getDaiFu( MallOrder order, int orderId, int memberId, HttpServletRequest request ) {
	boolean falg = true;

	if ( CommonUtil.isNotEmpty( order ) ) {
	    Date endTime = order.getCreateTime();
	    Date endHourTime = DateTimeKit.addHours( endTime, 24 );
	    Date nowTime = new Date();
	    long times = ( endHourTime.getTime() - nowTime.getTime() ) / 1000;
	    if ( times > 0 ) {
		request.setAttribute( "times", times );
	    }
	}
	MallDaifu daifu = new MallDaifu();
	daifu.setOrderId( orderId );
	daifu.setDfUserId( memberId );
	MallDaifu df = mallDaifuDAO.selectBydf( daifu );
	request.setAttribute( "daifu", df );
	return falg;

    }

    private double priceSubstring( String p, int num ) {
	double price;
	try {
	    int n = p.indexOf( "." );
	    if ( n > 0 && n + num < p.length() ) {
		price = Double.parseDouble( p.substring( 0, n + num ) );
	    } else {
		price = Double.parseDouble( p );
	    }
	} catch ( Exception e ) {
	    e.printStackTrace();
	    price = Double.parseDouble( p );
	}
	return price;
    }

    /**
     * 商城导出订单
     * type 1 商城订单导出
     */
    @Override
    public HSSFWorkbook exportExcel( Map< String,Object > params, String[] titles, int type, List< Map< String,Object > > shoplist ) {
	HSSFWorkbook workbook = new HSSFWorkbook();//创建一个webbook，对应一个Excel文件
	HSSFSheet sheet = workbook.createSheet( "商城订单" );//在webbook中添加一个sheet,对应Excel文件中的sheet
	HSSFRow rowTitle = sheet.createRow( 0 );//在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	HSSFCellStyle styleTitle = workbook.createCellStyle();//创建单元格，并设置值表头 设置表头居中
	styleTitle.setAlignment( HSSFCellStyle.ALIGN_CENTER );// 设置单元格左右居中
	HSSFFont fontTitle = workbook.createFont();//创建字体
	fontTitle.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );//加粗
	fontTitle.setFontName( "宋体" );//设置自提
	fontTitle.setFontHeight( (short) 200 );// 设置字体大小
	styleTitle.setFont( fontTitle );//给文字加样式

	HSSFCell cellTitle;
	//循环标题
	for ( int i = 0; i < titles.length; i++ ) {
	    cellTitle = rowTitle.createCell( i );//创建标题
	    cellTitle.setCellValue( titles[i] );
	    cellTitle.setCellStyle( styleTitle );//给标题加样式
	}
	sheet.setDefaultColumnWidth( 20 );
	sheet.setColumnWidth( 14, 100 * 256 );
	HSSFCellStyle centerStyle = workbook.createCellStyle();
	centerStyle.setAlignment( HSSFCellStyle.ALIGN_CENTER );// 设置单元格左右居中

	HSSFCellStyle leftStyle = workbook.createCellStyle();
	leftStyle.setAlignment( HSSFCellStyle.ALIGN_LEFT );// 设置单元格左右居中
	//循环数据
	if ( type == 1 ) {
	    //			getOrderList(params,sheet,valueStyle);
	    List< MallOrder > data = null;
	    //	    mallOrderDAO.explodOrder( params );
	    int status = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
		status = CommonUtil.toInteger( params.get( "status" ) );
		if ( status == 0 ) {
		    params.remove( "status" );
		}
	    }
	    if ( status != 6 && status != 7 && status != 8 && status != 9 ) {
		data = mallOrderDAO.findByPage( params );
	    } else {
		data = mallOrderDAO.findReturnByPage( params );
	    }

	    int j = 1;
	    for ( MallOrder order : data ) {
		j = getOrderList( sheet, centerStyle, leftStyle, order, j, shoplist );
	    }
	}

	return workbook;
    }

    private int getOrderList( HSSFSheet sheet, HSSFCellStyle valueStyle, HSSFCellStyle leftStyle, MallOrder order, int i, List< Map< String,Object > > shopList ) {
	String unit = "元";//单位
	String state = "";//订单状态
	String method = "快递配送";//配送方式
	String payWay = "微信支付";//付款方式
	int orderPayWay = order.getOrderPayWay();
	int orderStatus = order.getOrderStatus();
	String address = "";

	if ( orderPayWay == 2 ) {
	    payWay = "货到付款";
	} else if ( orderPayWay == 3 ) {
	    payWay = "储值卡支付";
	} else if ( orderPayWay == 4 ) {
	    payWay = "积分支付";
	    unit = "积分";
	} else if ( orderPayWay == 5 ) {
	    payWay = "扫码支付";
	} else if ( orderPayWay == 6 ) {
	    payWay = "到店支付";
	} else if ( orderPayWay == 7 ) {
	    payWay = "找人代付";
	} else if ( orderPayWay == 8 ) {
	    payWay = "粉币支付";
	    unit = "粉币";
	}
	if ( order.getDeliveryMethod() == 2 ) {
	    method = "上门自提";
	}
	if ( orderStatus == 1 ) {
	    state = "待付款";
	} else if ( orderStatus == 2 ) {
	    if ( order.getDeliveryMethod() == 1 ) {
		state = "待发货";
	    } else if ( order.getDeliveryMethod() == 2 ) {
		state = "待提货";
	    }
	    if ( orderPayWay == 5 ) {
		state = "已付款";
	    }
	} else if ( orderStatus == 3 ) {
	    state = "已发货";
	} else if ( orderStatus == 4 ) {
	    state = "交易完成";
	} else if ( orderStatus == 5 ) {
	    state = "订单关闭";
	}
	if ( CommonUtil.isNotEmpty( order.getReceiveId() ) ) {
	    if ( CommonUtil.isNotEmpty( order.getReceiveName() ) ) {
		address += order.getReceiveName() + " ";
	    }
	    if ( CommonUtil.isNotEmpty( order.getReceivePhone() ) ) {
		address += order.getReceivePhone() + " ";
	    }
	    if ( CommonUtil.isNotEmpty( order.getReceiveAddress() ) ) {
		address += order.getReceiveAddress() + " ";
	    }
	}
	String shopname = "";
	if ( shopList != null && shopList.size() > 0 ) {
	    for ( Map< String,Object > shopMap : shopList ) {
		if ( CommonUtil.toInteger( shopMap.get( "id" ) ) == order.getShopId() ) {
		    shopname = shopMap.get( "sto_name" ).toString();
		    break;
		}
	    }
	}
	/*int start = i;*/
	String createTime = DateTimeKit.format( order.getCreateTime(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	if ( orderPayWay != 5 ) {
	    /*if(order.getMallOrderDetail().size() > 1){
		    int j = order.getMallOrderDetail().size();
		    //合并单元格   起始行号，终止行号， 起始列号，终止列号
		    CellRangeAddress range = new CellRangeAddress(start, start+j, 0, 0);
		    sheet.addMergedRegion(range);
		    for (int k = 6; k <= 15; k++) {
			    CellRangeAddress range2 = new CellRangeAddress(start, start+j, k, k);
			    sheet.addMergedRegion(range2);
		    }
	    }*/
	    if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
		    if ( CommonUtil.isEmpty( detail.getId() ) || detail.getId() == 0 ) {
			continue;
		    }
		    String sale = "";
		    if ( CommonUtil.isNotEmpty( detail.getStatus() ) ) {
			Integer dStatus = detail.getStatus();
			if ( orderStatus != 5 ) {
			    if ( dStatus == 0 ) {
				sale = "退款中";
			    } else if ( dStatus == 1 ) {
				sale = "退款成功";
			    } else if ( dStatus == 2 ) {
				sale = "商家已同意退款退货，等待买家退货";
			    } else if ( dStatus == 3 ) {
				sale = "已退货等待商家确认收货";
			    } else if ( dStatus == 4 ) {
				sale = "商家未收到货，不同意退款申请";
			    } else if ( dStatus == 5 ) {
				sale = "退款退货成功";
			    } else if ( dStatus == -1 ) {
				sale = "退款失败(商家不同意退款)";
			    } else if ( dStatus == -2 ) {
				sale = "买家撤销退款";
			    }
			}
		    }
		    HSSFRow row = sheet.createRow( i );
		    createCell( row, 0, order.getOrderNo(), valueStyle );
		    createCell( row, 1, detail.getDetProName(), valueStyle );
		    String price = "0元";
		    if ( CommonUtil.isNotEmpty( detail.getDetPrivivilege() ) ) {
			price = detail.getDetProPrice() + "元";
		    }
		    createCell( row, 2, price, valueStyle );
		    createCell( row, 3, detail.getDetProNum().toString(), valueStyle );
		    double proMoney = CommonUtil.toDouble( detail.getDetProPrice() ) * detail.getDetProNum();
		    if ( proMoney > 0 ) {
			DecimalFormat df = new DecimalFormat( "######0.00" );
			proMoney = CommonUtil.toDouble( df.format( proMoney ) );
		    }
		    createCell( row, 4, proMoney + unit, valueStyle );
		    String discountPrice = "0";
		    if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
			discountPrice = detail.getDiscountedPrices().toString();
		    }
		    createCell( row, 5, discountPrice + unit, valueStyle );
		    createCell( row, 6, order.getOrderFreightMoney() + "", valueStyle );
		    createCell( row, 7, order.getMemberName(), valueStyle );
		    createCell( row, 8, createTime, valueStyle );

		    createCell( row, 9, state, valueStyle );

		    createCell( row, 10, method, valueStyle );
		    createCell( row, 11, sale, valueStyle );

		    createCell( row, 12, shopname, valueStyle );
		    createCell( row, 13, payWay, valueStyle );
		    createCell( row, 14, address, leftStyle );
		    createCell( row, 15, order.getOrderBuyerMessage(), valueStyle );
		    createCell( row, 16, order.getOrderSellerRemark(), valueStyle );
		    i++;
		}
	    }
	} else {//扫码支付
	    HSSFRow row = sheet.createRow( i );
	    createCell( row, 0, order.getOrderNo(), valueStyle );
	    createCell( row, 1, "扫码支付", valueStyle );
	    createCell( row, 2, order.getOrderMoney() + "元", valueStyle );
	    createCell( row, 3, "1", valueStyle );
	    createCell( row, 4, order.getOrderMoney() + "元", valueStyle );
	    createCell( row, 5, "0元", valueStyle );
	    createCell( row, 6, order.getOrderFreightMoney() + "", valueStyle );
	    createCell( row, 7, order.getMemberName(), valueStyle );
	    createCell( row, 8, createTime, valueStyle );

	    createCell( row, 9, state, valueStyle );

	    createCell( row, 10, method, valueStyle );
	    createCell( row, 11, "", valueStyle );
	    createCell( row, 12, shopname, valueStyle );
	    createCell( row, 13, payWay, valueStyle );
	    createCell( row, 14, address, leftStyle );
	    createCell( row, 15, order.getOrderBuyerMessage(), valueStyle );
	    createCell( row, 16, order.getOrderSellerRemark(), valueStyle );
	    i++;
	}
	if ( order.getMallOrderDetail().size() < 1 && orderPayWay != 5 ) {
	    i++;
	}
	return i;
    }

    private void createCell( HSSFRow row, int column, String value, HSSFCellStyle valueStyle ) {
	HSSFCell cell = row.createCell( column );
	cell.setCellValue( value );
	cell.setCellStyle( valueStyle );
    }

    /**
     * 商城导出订单
     * type 1 商城订单导出
     */
    @Override
    public HSSFWorkbook exportTradeExcel( Map< String,Object > params, String[] titles, int type, List< Map< String,Object > > shoplist ) {
	HSSFWorkbook workbook = new HSSFWorkbook();//创建一个webbook，对应一个Excel文件
	HSSFSheet sheet = workbook.createSheet( "交易记录" );//在webbook中添加一个sheet,对应Excel文件中的sheet
	HSSFRow rowTitle = sheet.createRow( 0 );//在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
	HSSFCellStyle styleTitle = workbook.createCellStyle();//创建单元格，并设置值表头 设置表头居中
	styleTitle.setAlignment( HSSFCellStyle.ALIGN_CENTER );// 设置单元格左右居中
	HSSFFont fontTitle = workbook.createFont();//创建字体
	fontTitle.setBoldweight( HSSFFont.BOLDWEIGHT_BOLD );//加粗
	fontTitle.setFontName( "宋体" );//设置自提
	fontTitle.setFontHeight( (short) 200 );// 设置字体大小
	styleTitle.setFont( fontTitle );//给文字加样式

	HSSFCell cellTitle;
	//循环标题
	for ( int i = 0; i < titles.length; i++ ) {
	    cellTitle = rowTitle.createCell( i );//创建标题
	    cellTitle.setCellValue( titles[i] );
	    cellTitle.setCellStyle( styleTitle );//给标题加样式
	}
	sheet.setDefaultColumnWidth( 20 );
	sheet.setColumnWidth( 2, 60 * 256 );
	HSSFCellStyle centerStyle = workbook.createCellStyle();
	centerStyle.setAlignment( HSSFCellStyle.ALIGN_CENTER );// 设置单元格左右居中

	HSSFCellStyle leftStyle = workbook.createCellStyle();
	leftStyle.setAlignment( HSSFCellStyle.ALIGN_LEFT );// 设置单元格左右居中
	//循环数据
	if ( type == 1 ) {
	    List< Map< String,Object > > data = mallOrderDAO.findByTradePage( params );
	    int j = 1;
	    for ( Map< String,Object > order : data ) {
		j = getTradeOrderList( sheet, centerStyle, leftStyle, order, j, shoplist );
	    }
	}

	return workbook;
    }

    private int getTradeOrderList( HSSFSheet sheet, HSSFCellStyle valueStyle, HSSFCellStyle leftStyle, Map< String,Object > order, int i, List< Map< String,Object > > shopList ) {
	String state = "";//订单状态
	Integer orderStatus = CommonUtil.toInteger( order.get( "orderStatus" ) );
	Integer returnStatus = null;

	if ( CommonUtil.isNotEmpty( order.get( "return_status" ) ) ) {
	    returnStatus = CommonUtil.toInteger( order.get( "return_status" ) );
	}
	String date = "";
	if ( returnStatus != null && ( returnStatus == 1 || returnStatus == 5 ) ) {
	    state = "已退款";
	    if ( CommonUtil.isNotEmpty( order.get( "updateTime" ) ) ) {
		Date date1 = DateTimeKit.parseDate( order.get( "updateTime" ).toString(), "yyyy/MM/dd HH:mm" );
		date = DateTimeKit.format( date1, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    }
	} else {
	    state = "已支付";
	    if ( CommonUtil.isNotEmpty( order.get( "payTime" ) ) ) {
		Date date1 = DateTimeKit.parseDate( order.get( "payTime" ).toString(), "yyyy/MM/dd HH:mm" );
		date = DateTimeKit.format( date1, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	    }
	}
	/*int start = i;*/
	String name = "";
	String price = "";
	if ( CommonUtil.isNotEmpty( order.get( "proName" ) ) ) {
	    name = order.get( "proName" ).toString();
	}
	if ( CommonUtil.isNotEmpty( order.get( "orderMoney" ) ) ) {
	    price = order.get( "orderMoney" ).toString();
	}
	/*Date date = DateTimeKit.parseDate( order.get( "createTime" ).toString(), "yyyy/MM/dd HH:mm" );
	String createTime = DateTimeKit.format( date, DateTimeKit.DEFAULT_DATETIME_FORMAT );*/
	HSSFRow row = sheet.createRow( i );
	createCell( row, 0, date, valueStyle );
	createCell( row, 1, order.get( "orderNo" ).toString(), valueStyle );
	createCell( row, 2, name, valueStyle );
	createCell( row, 3, CommonUtil.blob2String( order.get( "memberName" ) ), valueStyle );
	createCell( row, 4, price, valueStyle );
	createCell( row, 5, state, valueStyle );
	i++;
	return i;
    }

    @Override
    public String payGive( String pageId, Map< String,Object > params, Member member ) {
	String url = null;
	//判断是否从支付页面跳转过来？
	if ( CommonUtil.isNotEmpty( params.get( "orderId" ) ) && CommonUtil.isNotEmpty( params.get( "isPayGive" ) ) ) {
	    int orderId = CommonUtil.toInteger( params.get( "orderId" ) );
	    int isPayGive = CommonUtil.toInteger( params.get( "isPayGive" ) );
	    if ( isPayGive == 1 ) {//跳转到支付有礼的页面
		MallOrder order = mallOrderDAO.selectById( orderId );
		if ( CommonUtil.isNotEmpty( order ) ) {
		    int payWay = order.getOrderPayWay();
		    boolean falg = true;
		    if ( payWay == 6 ) {//到店支付
			if ( order.getDeliveryMethod() == 2 ) {//（线下付款不跳转到支付有礼页面）
			    falg = false;
			}
		    }
		    if ( payWay == 2 || payWay == 4 || payWay == 8 ) {//payWay 支付方式 2货到付款  4积分支付   8粉币支付    payWay == 2 ||
			falg = false;
		    }
		    //积分，粉币兑换商品,货到付款不跳转到支付有礼的页面
		    if ( falg ) {
			int model = 2;//模块id   2商城
			//todo 调用彭江丽  支付有礼 暂没有
			/*Map< String,Object > resultMap = null;//paySuccessService.findPaySuccess( model, member.getPublicId(), member.getId(), order.getOrderNo(), null, order.getOrderMoney(), 1, 1 );
			if ( CommonUtil.isNotEmpty( resultMap.get( "code" ) ) ) {
			    int code = CommonUtil.toInteger( resultMap.get( "code" ) );
			    if ( code > -1 ) {
				String urlDetail = "/phoneOrder/79B4DE7C/orderDetail.do?orderId=" + orderId;//订单详情跳转地址
				String returnUrl = "/phoneOrder/79B4DE7C/orderList.do?uId=" + order.getBusUserId();//未设置支付有礼 跳转地址
				int payType = 2;// 支付方式     2：微信支付
				if ( payWay == 3 ) {//储值卡支付
				    payType = 1;
				}

				String continueUrl = "/mallPage/" + pageId + "/79B4DE7C/pageIndex.do";//继续订购地址

				resultMap.put( "publicId", member.getPublicId() );
				resultMap.put( "model", model );
				resultMap.put( "urlDetail", urlDetail );
				resultMap.put( "returnUrl", returnUrl );
				resultMap.put( "continueUrl", continueUrl );
				resultMap.put( "payType", payType );
				resultMap.put( "memberId", member.getId() );
				resultMap.put( "orderNo", order.getOrderNo() );
				resultMap.put( "isGive", 0 );

				return "/phonePaySuccessController/79B4DE7C/paysuccessIndex.do?str=" + JSONObject.fromObject( resultMap );
			    }
			}*/
		    }
		}
	    }
	}
	return url;
    }

    /**
     * 发送消息模板
     */
    @Override
    public void sendMsg( MallOrder order, int type, WxPublicUsers publicUser, Member member, BusUser busUser ) {

	int id = 0;
	String title = "";
	if ( type == 4 ) {//购买成功通知
	    title = "购买成功通知";
	}
	//	WxPublicUsers publicUser = wxPublicUserService.selectById( order.getSellerUserId() );//通过商家id查询商家公众号id
	MallPaySet paySet = new MallPaySet();
	if ( CommonUtil.isNotEmpty( publicUser ) ) {
	    paySet.setUserId( publicUser.getBusUserId() );
	    MallPaySet set = mallPaySetService.selectByUserId( paySet );
	    if ( CommonUtil.isNotEmpty( set ) && CommonUtil.isNotEmpty( title ) ) {
		if ( set.getIsMessage().toString().equals( "1" ) ) {//判断是否开启了消息模板的功能
		    if ( CommonUtil.isNotEmpty( set.getMessageJson() ) ) {
			JSONArray arr = JSONArray.fromObject( set.getMessageJson() );
			if ( arr != null && arr.size() > 0 ) {
			    for ( Object object : arr ) {
				JSONObject obj = JSONObject.fromObject( object );
				if ( obj.getString( "title" ).equals( title ) ) {
				    if ( CommonUtil.isNotEmpty( obj.get( "id" ) ) ) {
					id = CommonUtil.toInteger( obj.get( "id" ) );
				    }
				}
			    }
			}
		    }
		}
	    }
	}

	if ( id > 0 ) {
	    List< Object > objs = new ArrayList<>();
	    objs.add( "您好，您已购买成功" );
	    objs.add( "您的商品已经购买成功，请耐心等待商家发货" );
	    String url = PropertiesUtil.getPhoneWebHomeUrl() + "/order/detail/" + publicUser.getBusUserId() + "/" + order.getId();

	    SendWxMsgTemplate template = new SendWxMsgTemplate();
	    template.setId( id );
	    template.setUrl( url );
	    template.setMemberId( order.getBuyerUserId() );
	    template.setPublicId( order.getSellerUserId() );
	    template.setObjs( objs );

	    logger.info( "发送消息模板参数：" + objs );
	    wxPublicUserService.sendWxMsgTemplate( template );

	    try {
		smsMessageTel( order, member, busUser );//短信提醒买家
	    } catch ( Exception e ) {
		e.printStackTrace();
		logger.error( "短信提醒买家失败异常" + e.getMessage() );
	    }
	}
    }

    /**
     * 同步订单
     */
    @Override
    public Map< String,Object > syncOrderbyPifa( Map< String,Object > params ) {
	boolean flag = false;
	Map< String,Object > resultMap = new HashMap<>();
	List< Map< String,Object > > orderList = mallOrderDAO.selectOrderByShopId( params );
	Map< String,Object > userMap = new HashMap<>();
	if ( orderList != null && orderList.size() > 0 ) {
	    for ( Map< String,Object > map : orderList ) {
		String buyerUserId = "";
		if ( CommonUtil.isNotEmpty( map.get( "buyer_user_id" ) ) ) {
		    buyerUserId = map.get( "buyer_user_id" ).toString();
		}
		int num = 0;
		double proPrice = 0;
		List< MallOrderDetail > orderDetailList = mallOrderDetailDAO.selectByOrderId( CommonUtil.toInteger( map.get( "id" ) ) );
		if ( orderDetailList != null && orderDetailList.size() > 0 ) {
		    for ( MallOrderDetail mallOrderDetail : orderDetailList ) {
			boolean isFinishflag = false;
			int status = mallOrderDetail.getStatus();
			if ( status == -3 || status == 1 || status == 5 || status == -2 ) {//没有在退款的订单
			    int returnDay = mallOrderDetail.getReturnDay();
			    if ( returnDay > 0 && CommonUtil.isNotEmpty( map.get( "update_time" ) ) ) {
				String times = map.get( "update_time" ).toString();

				String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
				String eDate = DateTimeKit.format( new Date(), format );
				long day = DateTimeKit.minsBetween( times, eDate, 3600000 * 7, format );
				if ( day > returnDay ) {// 订单完成时间大于已支付时间
				    isFinishflag = true;
				}
			    }
			    if ( returnDay == 0 ) {
				isFinishflag = true;
			    }
			}
			if ( isFinishflag ) {
			    double price = mallOrderDetail.getDetProNum() * CommonUtil.toDouble( mallOrderDetail.getDetProPrice() );
			    proPrice += price;
			    num += mallOrderDetail.getDetProNum();
			}
		    }
		} else {
		    num += 1;
		    if ( CommonUtil.isNotEmpty( map.get( "order_money" ) ) ) {
			proPrice += CommonUtil.toDouble( map.get( "order_money" ) );
		    }
		}
		if ( num > 0 && CommonUtil.isNotEmpty( buyerUserId ) ) {
		    JSONObject orderObj = new JSONObject();
		    if ( CommonUtil.isNotEmpty( userMap.get( buyerUserId ) ) ) {
			orderObj = JSONObject.fromObject( userMap.get( buyerUserId ) );
			num += orderObj.getInt( "num" );
			proPrice += orderObj.getDouble( "proPrice" );
		    }
		    orderObj.put( "num", num );
		    orderObj.put( "proPrice", proPrice );
		    userMap.put( buyerUserId, orderObj );

		    flag = true;
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( userMap ) ) {
	    String key = Constants.REDIS_KEY + "syncOrderCount";
	    for ( Map.Entry< String,Object > e : userMap.entrySet() ) {
		JedisUtil.map( key, e.getKey(), e.getValue().toString() );
	    }
	}
	resultMap.put( "flag", flag );
	return resultMap;
    }

    @Override
    public int updateOrderDetail( MallOrderDetail detail ) {
	return mallOrderDetailDAO.updateById( detail );
    }

    @Override
    public int updateOrderMoney( Map< String,Object > params ) {
	return mallOrderDAO.updateOrderMoney( params );
    }

    @Override
    public Map< String,Object > selectSumSaleMoney( int buyerUserId ) {
	return mallOrderDAO.selectSumSaleMoney( buyerUserId );
    }

    /**
     * 重新生成订单号
     */
    @Override
    public MallOrder againOrderNo( int orderId ) {
	MallOrder order = new MallOrder();
	order.setId( orderId );
	order.setOrderNo( "SC" + System.currentTimeMillis() );
	int count = mallOrderDAO.upOrderNoById( order );
	if ( count > 0 ) {
	    //	    String sql = "update t_wx_user_consume set orderCode='" + order.getOrderNo() + "' where orderId=" + orderId;
	    //	    daoUtil.update( sql );
	    return mallOrderDAO.selectById( orderId );
	}
	return null;
    }

    @Override
    public Map< String,Object > printOrder( Map< String,Object > params, BusUser user ) {
	MallOrder order = mallOrderDAO.getOrderById( CommonUtil.toInteger( params.get( "orderId" ) ) );//查询订单信息
	Member member = memberService.findMemberById( order.getBuyerUserId(), null );//根据粉丝id查询粉丝信息
	String memberName = "";
	String memberPhone = "";
	String address = "";
	String shopName = "";
	Map< String,Object > resultMap = new HashMap<>();
	//查询店铺名称
	if ( CommonUtil.isNotEmpty( order.getShopId() ) ) {
	    MallStore store = mallStoreDAO.selectById( order.getShopId() );
	    if ( CommonUtil.isNotEmpty( store ) ) {
		try {
		    WsWxShopInfo shopInfo = wxShopService.getShopById( store.getWxShopId() );
		    if ( CommonUtil.isNotEmpty( shopInfo ) ) {
			shopName = shopInfo.getBusinessName();
		    }
		    resultMap.put( "wxShopId", store.getWxShopId().toString() );
		} catch ( Exception e ) {
		    logger.error( "获取微信门店 方法异常：" + e.getMessage() );
		    e.printStackTrace();
		}
		if ( CommonUtil.isEmpty( shopName ) ) {
		    shopName = store.getStoName();
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( order.getReceiveName() ) ) {
	    memberName = order.getReceiveName();
	}
	if ( CommonUtil.isNotEmpty( order.getReceivePhone() ) ) {
	    memberPhone = order.getReceivePhone();
	}
	if ( CommonUtil.isNotEmpty( order.getReceiveAddress() ) ) {
	    address = order.getReceiveAddress();
	}
	if ( CommonUtil.isNotEmpty( order.getMemberName() ) ) {
	    memberName = order.getMemberName();
	}
	if ( CommonUtil.isNotEmpty( member.getPhone() ) ) {
	    memberPhone = member.getPhone();
	}
	if ( CommonUtil.isNotEmpty( order.getAppointmentName() ) && CommonUtil.isEmpty( memberName ) ) {
	    memberName = order.getAppointmentName();
	}
	if ( CommonUtil.isNotEmpty( order.getAppointmentTelephone() ) && CommonUtil.isEmpty( memberPhone ) ) {
	    memberPhone = order.getAppointmentTelephone();
	}

	//对订单详情进行分页
	/*params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = CommonUtil.isEmpty( params.get( "pageSize" ) ) ? 5 : CommonUtil.toInteger( params.get( "pageSize" ) );
	int rowCount = mallOrderDetailDAO.countByOrderId( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "mallOrder/toIndex.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );*/
	List< MallOrderDetail > list = mallOrderDetailDAO.selectPageByOrderId( params );

	//	resultMap.put( "totalPage", page.getPageCount() );//总页数
	//	resultMap.put( "curPage", page.getCurPage() );//当前页数

	List< Map< String,Object > > plist = new ArrayList<>();
	if ( list != null && list.size() > 0 ) {
	    for ( MallOrderDetail detail : list ) {
		Map< String,Object > map = new HashMap<>();
		map.put( "productBarCode", "" );//条形码
		map.put( "productName", detail.getDetProName() );//商品名称
		map.put( "originalPrice", detail.getDetPrivivilege() );//商品原价
		map.put( "quantity", detail.getDetProNum() );//商品数量
		map.put( "discount", detail.getDiscountedPrices() );//优惠
		double totalPrice = detail.getDetProNum() * CommonUtil.toDouble( detail.getDetProPrice() );
		DecimalFormat df = new DecimalFormat( "######0.00" );
		map.put( "subtotal", df.format( totalPrice ) );//小计
		plist.add( map );
	    }
	    //	    resultMap.put( "lists", plist );
	}
	String buyserMessage = "";
	if ( CommonUtil.isNotEmpty( order.getOrderBuyerMessage() ) ) {
	    buyserMessage = order.getOrderBuyerMessage();
	}
	//页面用到的参数

	//	resultMap.put( "nextPage", page.getCurPage() + 1 > page.getPageCount() ? page.getPageCount() : page.getCurPage() + 1 );
	//	resultMap.put( "prevPage", page.getCurPage() - 1 < 1 ? 1 : page.getCurPage() - 1 );

	Map< String,Object > printMap = new HashMap<>();
	Map< String,Object > header = new HashMap<>();
	header.put( "title", "商城订单" );//订单名
	header.put( "phone", user.getPhone() );//手机号/商家联系方式
	header.put( "customerName", memberName );//客户名称
	header.put( "customerPhone", CommonUtil.isNotEmpty( memberPhone ) ? memberPhone : "" );//客户电话
	header.put( "customerAddr", address );//客户地址
	header.put( "shop", shopName );//所属店铺
	header.put( "orderNumber", order.getOrderNo() );//订单编号
	header.put( "orderTime", DateTimeKit.formatDateByFormat( order.getCreateTime(), "yyyy-MM-dd HH:mm:ss" ) );//下单时间

	Map< String,Object > content = new HashMap<>();
	content.put( "plist", plist );//集合
	content.put( "buyerMessage", buyserMessage );//买家留言
	content.put( "totalReceivable", order.getOrderMoney() );//应收总额
	Map< String,Object > footer = new HashMap<>();
	footer.put( "deliveryMethod", order.getDeliveryMethod() == 1 ? "快递配送" : "上门自提" );//配送方式
	footer.put( "paymentMethod", order.getOrderStatus() == 1 || order.getOrderStatus() == 5 ? "未支付" : "已支付" );//支付状态
	footer.put( "businessNotes", CommonUtil.isNotEmpty( order.getOrderSellerRemark() ) ? order.getOrderSellerRemark() : "" );//商家备注
	printMap.put( "header", header );
	printMap.put( "content", content );
	printMap.put( "footer", footer );

	resultMap.put( "print", printMap );
	resultMap.put( "busId", order.getBusUserId() );
	resultMap.put( "hardwareDomain", PropertiesUtil.getHardwareDomain() );
	return resultMap;
    }

    @Override
    public Map< String,Object > getMemberParams( Member member, Map< String,Object > params ) {
	if ( CommonUtil.isEmpty( member ) ) {
	    return params;
	}
	List< Integer > memberList = memberService.findMemberListByIds( member.getId() );
	if ( memberList != null && memberList.size() > 1 ) {
	    params.put( "oldMemberIds", memberList );
	} else {
	    params.put( "memberId", member.getId() );
	}
	return params;
    }

    @Override
    public boolean isJuliByFreight( String shopIds ) {
	boolean isJuli = false;
	String[] shopStr = shopIds.split( "," );
	if ( CommonUtil.isNotEmpty( shopStr ) && shopStr.length > 0 ) {
	    Wrapper< MallFreight > wrapper = new EntityWrapper<>();
	    wrapper.setSqlSelect( "is_no_money,price_type" );
	    wrapper.where( "is_delete = 0 " );
	    wrapper.in( "shop_id", shopIds );
	    List< Map< String,Object > > lists = mallFreightDAO.selectMaps( wrapper );
	    if ( lists != null && lists.size() > 0 ) {
		for ( Map< String,Object > map : lists ) {
		    if ( CommonUtil.isNotEmpty( map.get( "is_no_money" ) ) && CommonUtil.isNotEmpty( map.get( "is_no_money" ) ) ) {
			if ( map.get( "is_no_money" ).toString().equals( "1" ) && map.get( "price_type" ).toString().equals( "3" ) ) {
			    isJuli = true;
			}
		    }
		}
	    }
	}
	return isJuli;
    }

    @Override
    public PageUtil selectIntegralOrder( Map< String,Object > params ) {
	List< Map< String,Object > > orderList = new ArrayList<>();
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	Wrapper< MallOrder > wrapper = new EntityWrapper<>();
	wrapper.where( "bus_user_id={0} and buyer_user_id={1}", params.get( "busUserId" ), params.get( "buyerUserId" ) );
	int count;
	if ( params.containsKey( "totalOrderNum" ) && CommonUtil.isNotEmpty( params.get( "totalOrderNum" ) ) ) {
	    count = CommonUtil.toInteger( params.get( "totalOrderNum" ) );
	} else {
	    count = mallOrderDAO.selectCount( wrapper );
	}
	if ( CommonUtil.isEmpty( count ) ) {
	    return null;
	}
	PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量
	List< Map< String,Object > > list = mallOrderDAO.selectIntegralOrderList( params );
		/*DecimalFormat df = new DecimalFormat("######0.00");*/
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > map : list ) {
		/*double num = CommonUtil.toDouble(map.get("det_pro_num"));
		double price = CommonUtil.toDouble(map.get("det_pro_price"));
		map.put("price", df.format(num*price));*/
		if ( CommonUtil.isNotEmpty( map.get( "pay_time" ) ) ) {
		    map.put( "times", map.get( "pay_time" ) );
		} else {
		    map.put( "times", map.get( "create_time" ) );
		}
		orderList.add( map );
	    }
	}
	page.setSubList( orderList );
	return page;
    }

    @Override
    public List< MallOrder > selectIntegeralOrder( Map< String,Object > params ) {
	Wrapper< MallOrder > wrapper = new EntityWrapper<>();
	wrapper.setSqlSelect( "order_money" );
	wrapper.where( "bus_user_id={0} and buyer_user_id={1} and order_type = 2", params.get( "busUserId" ), params.get( "buyerUserId" ) );
	return mallOrderDAO.selectList( wrapper );

    }

    /**
     * 计算库存是否足够
     *
     * @param proId         商品id
     * @param proSpecificas 商品规格
     * @param proNum        购买数量
     *
     * @return 判断库存
     */
    @Override
    public Map< String,Object > calculateInventory( String proId, Object proSpecificas, String proNum ) {
	Map< String,Object > result = new HashMap<>();
	MallProduct pro = mallProductService.selectByPrimaryKey( Integer.parseInt( proId ) );

	int isSpe = pro.getIsSpecifica();
	if ( isSpe == 1 ) {//是否有规格（0没有 1有）
	    Map< String,Object > invParams = new HashMap<>();
	    invParams.put( "proId", proId );
	    String[] specifica = ( proSpecificas.toString() ).split( "," );
	    StringBuilder ids = new StringBuilder();
	    for ( String aSpecifica : specifica ) {
		if ( CommonUtil.isNotEmpty( aSpecifica ) ) {
		    invParams.put( "specificaValueId", aSpecifica );
		    int num = selectSpeBySpeValueId( invParams );
		    if ( CommonUtil.isNotEmpty( ids ) && ids.length() > 0 ) {
			ids.append( "," );
		    }
		    ids.append( num );
		}
	    }
	    if ( CommonUtil.isNotEmpty( ids.toString() ) ) {
		if ( CommonUtil.isNotEmpty( ids ) ) {
		    invParams.put( "specificaIds", ids );
		    MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( invParams );
		    if ( CommonUtil.isNotEmpty( proInv ) ) {
			if ( proInv.getInvNum() < Integer.parseInt( proNum ) ) {
			    result.put( "result", false );
			    result.put( "msg", ResponseEnums.STOCK_NULL_ERROR.getDesc() );
			} else {
			    result.put( "result", true );
			}
		    } else {
			result.put( "result", false );
			result.put( "msg", ResponseEnums.STOCK_NULL_ERROR.getDesc() );
		    }
		} else {
		    isSpe = 0;
		}
	    } else {
		isSpe = 0;
	    }
	}
	if ( isSpe == 0 ) {
	    if ( pro.getProStockTotal() < Integer.parseInt( proNum ) ) {
		result.put( "result", false );
		result.put( "msg", ResponseEnums.STOCK_NULL_ERROR.getDesc() );
	    } else {
		result.put( "result", true );
	    }
	}
	return result;
    }

    @Override
    public boolean isReturnSuccess( MallOrder order ) {
	boolean flag = false;
	if ( order != null ) {
	    if ( order.getMallOrderDetail() != null ) {
		for ( MallOrderDetail mDetail : order.getMallOrderDetail() ) {
		    if ( mDetail.getStatus() != null ) {
			if ( mDetail.getStatus() == 1 || mDetail.getStatus() == 5 ) {//退款成功
			    flag = true;
			} else {
			    flag = false;
			    break;
			}
		    } else {
			flag = false;
			break;
		    }
		}
	    }
	}
	if ( flag ) {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "status", 5 );
	    params.put( "orderId", order.getId() );
	    boolean flags = false;
	    //修改订单状态为订单关闭
	    mallOrderDAO.upOrderNoOrRemark( params );
	}
	return flag;
    }

    /**
     * 订单转换
     *
     * @param order
     * @param shoplist
     *
     * @return
     */
    public OrderResult converterOrder( MallOrder order, List< Map< String,Object > > shoplist ) {
	OrderResult result = new OrderResult();
	EntityDtoConverter converter = new EntityDtoConverter();
	if ( order != null ) {
	    converter.entityConvertDto( order, result );
	    for ( Map< String,Object > shopMap : shoplist ) {
		if ( shopMap.get( "id" ).toString().equals( order.getShopId().toString() ) ) {
		    result.setShopName( shopMap.get( "sto_name" ).toString() );//店铺名称
		    break;
		}
	    }
	    result.setOrderStatusName( OrderUtil.getOrderStatusMsgByOrder( order.getOrderStatus().toString(), order.getDeliveryMethod().toString() ) );
	    result.setTypeName( OrderUtil.getOrderTypeMsgByOrder( order.getBuyerUserType() ) );
	    result.setOrderTypeName( OrderUtil.getOrderTypeByOrder( order.getOrderType() ) );
	    if ( order.getOrderPayWay() == 7 ) {
		MallDaifu daifu = mallDaifuService.selectByDfOrderNo( order.getOrderNo().toString() );
		result.setMallDaifu( daifu );
	    }
	    if ( order.getDeliveryMethod() == 2 && CommonUtil.isNotEmpty( order.getTakeTheirId() ) ) {//配送方式是到店自提
		MallTakeTheir take = mallTakeTheirService.selectById( order.getTakeTheirId() );
		result.setTakeTheir( take );
	    }
	    if ( CommonUtil.isNotEmpty( order.getExpressId() ) ) {
		String expressName = dictService.getDictRuturnValue( "1092", order.getExpressId() );
		if ( CommonUtil.isNotEmpty( expressName ) ) {
		    result.setExpressName( expressName );
		}
	    }
	    if ( order.getOrderStatus() == 1 ) {
		//是否显示取消订单按钮  1显示
		result.setIsShowCancelOrderButton( 1 );
		//是否显示修改价格按钮 1显示
		result.setIsShowUpdatePriceButton( 1 );
	    } else if ( order.getOrderStatus() == 2 && order.getDeliveryMethod() == 1 ) {
		if ( order.getGroupBuyId() != null && order.getGroupBuyId() != 0 && order.getOrderType() == 1 ) {
		    //查询团购需要参与的人数
		    Map< String,Object > map = mallGroupBuyService.selectGroupBuyById( order.getGroupBuyId() );
		    //查询已参加团购人数
		    Map< String,Object > params1 = new HashMap<>();
		    params1.put( "orderId", order.getId() );
		    params1.put( "groupBuyId", order.getGroupBuyId() );
		    Map< String,Object > map1 = mallGroupJoinDAO.groupJoinPeopleNum( params1 );
		    if ( Integer.parseInt( map.get( "gPeopleNum" ).toString() ) == Integer.parseInt( map1.get( "num" ).toString() ) ) {
			//是否显示发货按钮  1显示
			result.setIsShowDeliveryButton( 1 );
		    }
		} else {
		    //是否显示发货按钮  1显示
		    result.setIsShowDeliveryButton( 1 );
		}

	    } else if ( order.getOrderStatus() == 2 && order.getDeliveryMethod() == 2 ) {
		//是否显示确认已提货按钮  1显示
		result.setIsShowPickUpGoodsButton( 1 );
	    }

	    Integer returnStatus = null;
	    List< MallOrderDetail > details = null;
	    if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		details = order.getMallOrderDetail();
	    } else {
		details = mallOrderDetailDAO.selectByOrderId( order.getId() );
	    }
	    List< OrderDetailResult > detailResults = new ArrayList<>();
	    //扫码支付 没有商品
	    if ( details != null && details.size() > 0 && order.getOrderPayWay() != 5 ) {
		for ( MallOrderDetail detail : details ) {
		    if ( detail.getId() == null ) {
			continue;
		    }
		    OrderDetailResult detailResult = new OrderDetailResult();
		    converter.entityConvertDto( detail, detailResult );

		    MallOrderReturn returns = new MallOrderReturn();
		    returns.setOrderId( order.getId() );
		    returns.setOrderDetailId( detail.getId() );
		    MallOrderReturn orderReturn = mallOrderReturnDAO.selectByOrderDetailId( returns );
		    if ( orderReturn != null ) {
			OrderReturnResult returnResult = new OrderReturnResult();
			converter.entityConvertDto( orderReturn, returnResult );
			detailResult.setStatusName( OrderUtil.getReturnStatusName( orderReturn ) );
			returnStatus = orderReturn.getStatus();
			if ( orderReturn.getStatus() == 0 ) {
			    //是否显示拒绝退款申请按钮 1显示
			    returnResult.setIsShowRefuseApplyButton( 1 );
			    if ( orderReturn.getRetHandlingWay() == 1 ) {
				//是否显示同意退款申请按钮 1显示
				returnResult.setIsShowAgreeApplyButton( 1 );
			    } else if ( orderReturn.getRetHandlingWay() == 2 ) {
				//是否显示同意退货退款按钮 1显示
				returnResult.setIsShowAgreeRetGoodsApplyButton( 1 );
			    }
			    //默认7天无回应，则自动同意
			    try {
				Date date = DateTimeKit.addDate( orderReturn.getCreateTime(), Constants.WAIT_APPLY_RETURN_DAY );
				int cont = DateTimeKit.dateCompare( DateTimeKit.getDateTime(), DateTimeKit.getDateTime( date, "yyyy-MM-dd HH:mm:ss" ), "yyyy-MM-dd HH:mm:ss" );
				if ( cont == -1 ) {
				    returnResult.setApplyTimes( ( date.getTime() - new Date().getTime() ) / 1000 );
				}
			    } catch ( Exception e ) {
				e.printStackTrace();
				throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "获取退款申请申请倒计时异常：" + e.getMessage() );
			    }
			} else if ( orderReturn.getStatus() == 3 ) {
			    //是否显示确认收货并退款按钮 1显示
			    returnResult.setIsShowConfirmTakeButton( 1 );
			    //是否显示拒绝确认收货按钮 1显示
			    returnResult.setIsShowRefuseConfirmTakeButton( 1 );
			    //默认7天无回应，则自动收货
			    try {
				Date date = DateTimeKit.addDate( orderReturn.getUpdateTime(), Constants.RETURN_AUTO_CONFIRM_TAKE_DAY );//倒计时最终时间
				int cont = DateTimeKit.dateCompare( DateTimeKit.getDateTime(), DateTimeKit.getDateTime( date, "yyyy-MM-dd HH:mm:ss" ), "yyyy-MM-dd HH:mm:ss" );
				if ( cont == -1 ) {
				    returnResult.setTakeTimes( ( date.getTime() - new Date().getTime() ) / 1000 );
				}
			    } catch ( Exception e ) {
				e.printStackTrace();
				throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "获取收货确认倒计时异常：" + e.getMessage() );
			    }
			} else if ( orderReturn.getStatus() == 4 ) {
			    //是否显示修改退货地址按钮 1显示
			    returnResult.setIsShowUpdateAddressButton( 1 );
			}

			//正在退款的订单不能发货
			if ( returnStatus != null ) {
			    if ( returnStatus != -2 && returnStatus != -3 && returnStatus != 1 && returnStatus != 5 ) {
				//是否显示发货按钮  1显示
				result.setIsShowDeliveryButton( 0 );
				result.setIsShowPickUpGoodsButton( 0 );
			    }
			}
			//维权日志列表
			Wrapper wrapper = new EntityWrapper();
			wrapper.where( "return_id = {0}  ", orderReturn.getId() );
			wrapper.orderBy( "create_time,id" );
			List< MallOrderReturnLog > returnLogList = mallOrderReturnLogService.selectList( wrapper );
			List< OrderReturnLogResult > returnLogResults = new ArrayList<>();
			if ( returnLogList != null && returnLogList.size() > 0 ) {
			    for ( MallOrderReturnLog returnLog : returnLogList ) {
				OrderReturnLogResult logResult = new OrderReturnLogResult();
				converter.entityConvertDto( returnLog, logResult );
				returnLogResults.add( logResult );
			    }
			    returnResult.setOrderReturnLogList( returnLogResults );
			}
			detailResult.setReturnResult( returnResult );
		    }
		    //封装支付宝退款地址
		    if ( result.getOrderPayWay() == 9 && orderReturn != null ) {
			try {
			    double money = detailResult.getReturnResult().getRetMoney().doubleValue();
			    if ( ( result.getOrderPayWay() == 2 || result.getOrderPayWay() == 6 ) && result.getIsWallet() == 0 ) {
				money = 0d;
			    }
			    //			    KeysUtil keysUtil = new KeysUtil();
			    //			    String notifyUrl = keysUtil.getEncString( PropertiesUtil.getHomeUrl() + "mallOrder/E9lM9uM4ct/agreanOrderReturn" );
			    //			    String url = PropertiesUtil.getHomeUrl() + "alipay/79B4DE7C/refundVer2.do?out_trade_no=" + result.getOrderNo() + "&busId=" + order.getBusUserId()
			    //					    + "&desc=订单退款&fee=" + money + "&notifyUrl=" + notifyUrl;
			    String url = CommonUtil.getAliReturnUrl( result.getOrderNo(), order.getBusUserId(), "订单退款", money, Constants.ORDER_REFUND_URL );
			    detailResult.getReturnResult().setRefundUrl( url );
			} catch ( Exception e ) {
			    e.printStackTrace();
			    throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), "封装支付宝退款地址异常：" + e.getMessage() );
			}
		    }
		    detailResults.add( detailResult );
		}
		result.setMallOrderDetail( detailResults );
	    }

	}
	return result;
    }
}
