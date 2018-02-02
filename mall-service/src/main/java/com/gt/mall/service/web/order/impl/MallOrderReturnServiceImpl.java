package com.gt.mall.service.web.order.impl;

import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.entityBo.ErpRefundBo;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.DictBean;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallIncomeList;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.result.phone.order.returns.PhoneReturnProductResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnWayResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnWuLiuResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallBusMessageMemberService;
import com.gt.mall.service.web.basic.MallCountIncomeService;
import com.gt.mall.service.web.basic.MallIncomeListService;
import com.gt.mall.service.web.order.*;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.utils.OrderUtil;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.param.wallet.TRefundOrder;
import com.gt.util.entity.param.wx.BusIdAndindustry;
import com.gt.util.entity.result.pay.WxPayOrder;
import com.gt.util.entity.result.wx.ApiWxApplet;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 商品退货 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderReturnServiceImpl extends BaseServiceImpl< MallOrderReturnDAO,MallOrderReturn > implements MallOrderReturnService {

    private Logger logger = Logger.getLogger( MallOrderReturnServiceImpl.class );

    @Autowired
    private MallOrderDAO                mallOrderDAO;
    @Autowired
    private MallProductDAO              mallProductDAO;
    @Autowired
    private MallProductInventoryService mallProductInventoryService;
    @Autowired
    private MallOrderService            mallOrderService;
    @Autowired
    private MallOrderDetailDAO          mallOrderDetailDAO;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private PayOrderService             payOrderService;
    @Autowired
    private PayService                  payService;
    @Autowired
    private MallOrderDetailService      mallOrderDetailService;
    @Autowired
    private DictService                 dictService;
    @Autowired
    private MallOrderReturnDAO          mallOrderReturnDAO;
    @Autowired
    private MallStoreDAO                mallStoreDAO;
    @Autowired
    private MallOrderReturnLogService   mallOrderReturnLogService;
    @Autowired
    private MallBusMessageMemberService mallBusMessageMemberService;
    @Autowired
    private MallCountIncomeService      mallCountIncomeService;
    @Autowired
    private MallIncomeListService       mallIncomeListService;
    @Autowired
    private MallOrderTaskService        mallOrderTaskService;

    /**
     * 申请订单退款
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public boolean addOrderReturn( MallOrderReturn orderReturn, Member member ) {
	int status = -3;
	Integer orderDetailId = orderReturn.getOrderDetailId();
	if ( orderReturn != null ) {
	    // 新增订单退款
	    int num;
	    MallOrderReturn oReturn = mallOrderReturnDAO.selectByOrderDetailId( orderReturn );
	    if ( oReturn != null ) {
		orderDetailId = oReturn.getOrderDetailId();
		orderReturn.setId( oReturn.getId() );
		status = oReturn.getStatus();
		orderReturn.setStatus( oReturn.getStatus() );
		if ( oReturn.getStatus() == -3 || oReturn.getStatus() == -1 ) {
		    //不同意退款和  商家拒绝退款后   修改未  退款中
		    orderReturn.setStatus( 0 );
		} else if ( orderReturn.getStatus() == 2 || orderReturn.getStatus() == 4 ) {
		    //商家已退货，等待买家确认收货
		    orderReturn.setStatus( 3 );
		}
	    }
	    if ( CommonUtil.isNotEmpty( orderReturn.getOrderDetailId() ) && ( CommonUtil.isEmpty( orderReturn.getId() ) || orderReturn.getId() == 0 ) ) {
		MallOrderDetail detail = mallOrderDetailDAO.selectById( orderReturn.getOrderDetailId() );
		if ( CommonUtil.isNotEmpty( detail ) ) {
		    orderReturn.setShopId( detail.getShopId() );
		    if ( detail.getUseFenbi() > 0 ) {
			orderReturn.setReturnFenbi( detail.getUseFenbi() );
		    }
		    if ( detail.getUseJifen() > 0 ) {
			orderReturn.setReturnJifen( detail.getUseJifen() );
		    }
		}
	    }
	    if ( CommonUtil.isEmpty( orderReturn.getId() ) || orderReturn.getId() == 0 ) {
		orderReturn.setCreateTime( new Date() );
		String orderNo = "TK" + System.currentTimeMillis();
		orderReturn.setReturnNo( orderNo );
		orderReturn.setStatus( 0 );
		num = mallOrderReturnDAO.insert( orderReturn );
		try {
		    mallBusMessageMemberService.buyerOrderReturn( orderReturn, member, 0 );
		} catch ( Exception e ) {
		    e.printStackTrace();
		    logger.error( "提醒商家消息失败异常" + e.getMessage() );
		}

	    } else {
		orderReturn.setUpdateTime( new Date() );
		num = mallOrderReturnDAO.updateById( orderReturn );
	    }
	    if ( num > 0 ) {
		// 修改订单详情的状态
		MallOrderDetail detail = new MallOrderDetail();
		detail.setId( orderDetailId );
		detail.setStatus( orderReturn.getStatus() );
		num = mallOrderDetailDAO.updateById( detail );

		if ( num > 0 ) {
		    if ( status == -3 ) {
			// 还未退款，添加退款日志记录
			mallOrderReturnLogService.addBuyerRetutnApply( orderReturn.getId(), member.getId(), orderReturn.getRetHandlingWay() );
			//默认7天不处理，自动退款（系统消息）
			mallOrderReturnLogService.waitSellerDispose( orderReturn.getId(), DateTimeKit.addDays( 7 ) );
			MallOrder mallOrder = mallOrderService.selectById( orderReturn.getOrderId() );
			mallOrderTaskService.saveOrUpdate( 7, mallOrder.getId(), mallOrder.getOrderNo(), orderReturn.getId(), 7 );//7自动退款给买家
		    } else if ( status == -1 ) {
			//修改申请
			mallOrderReturnLogService.againRetutnApply( orderReturn.getId(), member.getId(), orderReturn.getRetHandlingWay() );
			MallOrder mallOrder = mallOrderService.selectById( orderReturn.getOrderId() );
			mallOrderTaskService.saveOrUpdate( 7, mallOrder.getId(), mallOrder.getOrderNo(), orderReturn.getId(), 7 );//7自动退款给买家
		    } else if ( status == 3 ) {
			//买家已填写退货物流
			mallOrderReturnLogService.buyerReturnGoods( orderReturn.getId(), member.getId() );
		    } else if ( status == 4 ) {
			//修改退货物流
			mallOrderReturnLogService.buyerUpdateLogistics( orderReturn.getId(), member.getId() );

			MallOrder mallOrder = mallOrderService.selectById( orderReturn.getOrderId() );
			mallOrderTaskService.saveOrUpdate( 8, mallOrder.getId(), mallOrder.getOrderNo(), orderReturn.getId(), 10 );//8自动确认收货并退款至买家
		    } else if ( status == 2 ) {
			MallOrder mallOrder = mallOrderService.selectById( orderReturn.getOrderId() );
			mallOrderTaskService.saveOrUpdate( 8, mallOrder.getId(), mallOrder.getOrderNo(), orderReturn.getId(), 10 );//8自动确认收货并退款至买家
		    }

		    return true;
		} else {
		    throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
		}
	    } else {
		throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	    }

	}
	throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
    }

    /**
     * 系统退款（不是买家申请的）
     */
    @Override
    public boolean returnEndOrder( Integer orderId, Integer orderDetailId ) throws Exception {
	boolean resultFlag = false;
	//	Map< String,Object > map = new HashMap<>();//存放退款信息
	if ( orderId > 0 && orderDetailId > 0 ) {
	    Map< String,Object > detailMap = mallOrderDAO.selectByDIdOrder( orderDetailId );
	    if ( CommonUtil.isNotEmpty( detailMap ) ) {
		Integer memberId = CommonUtil.toInteger( detailMap.get( "buyer_user_id" ) );
		Integer orderPayWay = CommonUtil.toInteger( detailMap.get( "order_pay_way" ) );
		Double orderMoney = CommonUtil.toDouble( detailMap.get( "orderMoney" ) );
		String orderNo = CommonUtil.toString( detailMap.get( "orderNo" ) );
		int busUserId = CommonUtil.toInteger( detailMap.get( "bus_user_id" ) );
		WxPublicUsers pUser = wxPublicUserService.selectByMemberId( memberId );
		String returnNo = "TK" + System.currentTimeMillis();
		if ( detailMap.get( "orderStatus" ).toString().equals( "2" ) ) {

		    if ( orderPayWay == 1 && CommonUtil.isNotEmpty( pUser ) ) {//微信退款
			WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( orderNo );//根据订单号查询微信订单信息
			if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
			    WxmemberPayRefund refund = new WxmemberPayRefund();
			    refund.setMchid( pUser.getMchId() );// 商户号
			    refund.setAppid( pUser.getAppid() );// 公众号
			    refund.setTotalFee( wxPayOrder.getTotalFee() );//支付总金额
			    refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );//系统单号
			    refund.setRefundFee( orderMoney );//退款金额

			    logger.error( "微信退款的参数：" + JSONObject.fromObject( refund ).toString() );
			    Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );  //微信退款
			    logger.error( "微信退款的返回值：" + JSONObject.fromObject( resultmap ) );
			    if ( resultmap != null ) {
				String code = resultmap.get( "code" ).toString();
				if ( code.equals( "1" ) || code.equals( Constants.FINISH_REFUND_STATUS ) ) {
				    resultFlag = true;
				    //退款成功修改退款状态
				    updateReturnStatus( pUser, detailMap, returnNo );//微信退款

				}
			    }
			} else if ( wxPayOrder.getTradeState().equals( "NOTPAY" ) ) {
			    logger.info( "订单：" + wxPayOrder.getOutTradeNo() + "未支付" );
			} else if ( wxPayOrder.getTradeState().equals( "REVOKED" ) ) {
			    resultFlag = true;
			    //退款成功修改退款状态
			    updateReturnStatus( pUser, detailMap, returnNo );//微信退款
			}

		    } else if ( orderPayWay == 3 && CommonUtil.isNotEmpty( pUser ) ) {//储值卡退款
			//			Map< String,Object > returnParams = new HashMap<>();
			//			returnParams.put( "busId", busUserId );
			//			returnParams.put( "orderNo", orderNo );
			//			returnParams.put( "money", orderMoney );
			//储值卡退款
			//			Map< String,Object > payResultMap = memberService.refundMoney( returnParams );//memberPayService.chargeBack(memberId,money);
			//			if ( payResultMap != null ) {
			//			    if ( CommonUtil.isNotEmpty( payResultMap.get( "code" ) ) ) {
			//				int code = CommonUtil.toInteger( payResultMap.get( "code" ) );
			//				if ( code == 1 ) {//退款成功修改退款状态
			//				    resultFlag = true;
			//				    updateReturnStatus( pUser, detailMap, returnNo );//微信退款
			//				}
			//			    }
			//			}
			int is_wallet = 0;
			double use_fenbi = 0;
			double use_jifen = 0;
			if ( CommonUtil.isNotEmpty( detailMap.get( "is_wallet" ) ) ) {
			    is_wallet = CommonUtil.toInteger( detailMap.get( "is_wallet" ) );
			}
			if ( CommonUtil.isNotEmpty( detailMap.get( "use_fenbi" ) ) ) {
			    use_fenbi = CommonUtil.toDouble( detailMap.get( "use_fenbi" ) );
			}
			if ( CommonUtil.isNotEmpty( detailMap.get( "use_jifen" ) ) ) {
			    use_jifen = CommonUtil.toDouble( detailMap.get( "use_jifen" ) );
			}
			ErpRefundBo erpRefundBo = new ErpRefundBo();
			erpRefundBo.setBusId( busUserId );//商家id
			erpRefundBo.setOrderCode( detailMap.get( "order_no" ).toString() );////订单号
			erpRefundBo.setRefundPayType( CommonUtil.getMemberPayType( orderPayWay, is_wallet ) );////退款方式 字典1198
			erpRefundBo.setRefundMoney( orderMoney ); //退款金额
			erpRefundBo.setRefundJifen( CommonUtil.toIntegerByDouble( use_jifen ) );//退款积分
			erpRefundBo.setRefundFenbi( use_fenbi ); //退款粉币
			erpRefundBo.setRefundDate( new Date().getTime() );
			Map< String,Object > resultMap = memberService.refundMoney( erpRefundBo );
			if ( CommonUtil.toInteger( resultMap.get( "code" ) ) != 1 ) {
			    //同步失败，存入redis
			    JedisUtil.rPush( Constants.REDIS_KEY + "member_return_jifen", com.alibaba.fastjson.JSONObject.toJSONString( erpRefundBo ) );
			} else {
			    resultFlag = true;
			    updateReturnStatus( pUser, detailMap, returnNo );//微信退款
			}
		    }

		    boolean flag = false;
		    //查询订单详情是否已经全部退款
		    MallOrder order = mallOrderDAO.getOrderById( orderId );
		    flag = mallOrderService.isReturnSuccess( order );
		    /*if ( order != null ) {
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
			params.put( "orderId", orderId );
			//修改订单状态为订单关闭
			mallOrderDAO.upOrderNoOrRemark( params );
		    }*/
		} else if ( detailMap.get( "orderStatus" ).toString().equals( "9" ) ) {//支付宝退款

		} else if ( detailMap.get( "orderStatus" ).toString().equals( "10" ) ) {//小程序退款
		    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( orderNo );//根据订单号查询微信订单信息
		    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
			BusIdAndindustry busIdAndindustry = new BusIdAndindustry();
			busIdAndindustry.setBusId( wxPayOrder.getBus_id() );
			busIdAndindustry.setIndustry( Constants.APPLET_STYLE );
			ApiWxApplet applet = wxPublicUserService.selectBybusIdAndindustry( busIdAndindustry );

			WxmemberPayRefund refund = new WxmemberPayRefund();
			refund.setMchid( applet.getMchId() );// 商户号
			refund.setAppid( applet.getAppid() );// 公众号
			refund.setTotalFee( wxPayOrder.getTotalFee() );//支付总金额
			refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );//系统单号
			refund.setRefundFee( orderMoney );//退款金额
			logger.info( "小程序退款参数：" + JSONObject.fromObject( refund ).toString() );
			Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );//小程序退款
			logger.info( "小程序退款返回值：" + JSONObject.fromObject( resultmap ) );
			if ( resultmap != null ) {
			    String code = resultmap.get( "code" ).toString();
			    if ( code.equals( "1" ) || code.equals( Constants.FINISH_REFUND_STATUS ) ) {
				resultFlag = true;
				//退款成功修改退款状态
				updateReturnStatus( pUser, detailMap, returnNo );//微信退款
			    }
			}
		    } else if ( wxPayOrder.getTradeState().equals( "REVOKED" ) ) {
			resultFlag = true;
			//退款成功修改退款状态
			updateReturnStatus( pUser, detailMap, returnNo );//微信退款
		    }
		} else if ( detailMap.get( "orderStatus" ).toString().equals( "11" ) ) {//钱包退款
		    TRefundOrder refundOrder = new TRefundOrder();
		    refundOrder.setBizOrderNo( returnNo );//商户退款订单号
		    refundOrder.setOriBizOrderNo( orderNo );//商户原订单号(支付单号)
		    refundOrder.setAmount( orderMoney );//订单金额
		    refundOrder.setBackUrl( Constants.PRESALE_REFUND_URL );//异步回调通知
		    refundOrder.setBusId( busUserId );//商家id
		    Map< String,Object > refundResultMap = payService.walletRefund( refundOrder );
		    if ( CommonUtil.toInteger( refundResultMap.get( "code" ) ) == 1 ) {
			resultFlag = true;
		    } else {
			resultFlag = false;
		    }
		}
	    }

	}
	return resultFlag;
    }

    /**
     * 退款成功，修改退款表的状态，并添加退款记录
     */
    private void updateReturnStatus( WxPublicUsers pUser, Map< String,Object > detailMap, String returnNo ) throws Exception {
	Integer detailId = CommonUtil.toInteger( detailMap.get( "dId" ) );

	MallOrderDetail detail = new MallOrderDetail();
	detail.setId( detailId );// 修改订单详情的状态
	detail.setStatus( 1 );
	mallOrderDetailDAO.updateById( detail );
	//退款成功修改商品的库存和销量
	updateInvenNum( detailMap );

	String orderNo = CommonUtil.toString( detailMap.get( "orderNo" ) );
	MallOrder order = mallOrderDAO.selectOrderByOrderNo( orderNo );
	List< MallOrderDetail > orderDetails = mallOrderDetailDAO.selectByOrderId( order.getId() );
	//添加交易记录
	MallIncomeList incomeList = new MallIncomeList();
	incomeList.setBusId( order.getBusUserId() );
	incomeList.setIncomeType( 2 );
	incomeList.setIncomeCategory( 1 );
	incomeList.setIncomeMoney( order.getOrderMoney() );
	incomeList.setShopId( order.getShopId() );
	incomeList.setBuyerId( order.getBuyerUserId() );
	incomeList.setBuyerName( order.getMemberName() );
	incomeList.setTradeId( detailId );
	incomeList.setTradeType( 2 );
	if ( orderDetails.size() > 0 ) {
	    incomeList.setProName( orderDetails.get( 0 ).getDetProName() );
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
	mallCountIncomeService.saveTurnover( CommonUtil.toInteger( detailMap.get( "shopId" ) ), null, CommonUtil.toBigDecimal( detailMap.get( "orderMoney" ) ) );

    }

    /**
     * 退款成功修改库存和销量
     */
    private void updateInvenNum( Map< String,Object > detailMap ) {
	//		Map<String, Object> detailMap =mallOrderDao.selectByDIdOrder(orderReturn.getOrderDetailId());
	Integer productId = 0;
	Integer productNum = 0;
	Integer detailId = 0;
	if ( detailMap != null ) {
	    productId = CommonUtil.toInteger( detailMap.get( "productId" ) );
	    productNum = CommonUtil.toInteger( detailMap.get( "proNum" ) );
	    detailId = CommonUtil.toInteger( detailMap.get( "dId" ) );
	}
	if ( productId > 0 ) {
	    //修改商品总库存
	    MallProduct product = mallProductDAO.selectById( productId );
	    if ( product != null ) {

		Map< String,Object > productParams = new HashMap<>();
		productParams.put( "type", 1 );
		productParams.put( "product_id", product.getId() );
		productParams.put( "pro_num", productNum );
		mallProductDAO.updateProductStock( productParams );

		//修改商品规格库存
		if ( product.getIsSpecifica() == 1 ) {
		    String specIds = mallOrderDAO.selectSpecByDetailId( detailId );
		    Map< String,Object > proMap = new HashMap<>();
		    proMap.put( "specificaIds", specIds );
		    proMap.put( "proId", productId );
		    MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( proMap );
		    mallProductInventoryService.updateProductInventory( proInv, productNum, 1 );
		}
	    }

	}
    }

    @Override
    public List< PhoneReturnWayResult > getReturnWayList( MallOrder order ) {

	List< PhoneReturnWayResult > wayResultList = new ArrayList<>();
	int payWay = order.getOrderPayWay();//2 货到付款 6 到店支付
	int orderStatus = order.getOrderStatus();//3,已发货 4,已完成
	int orderWallet = order.getIsWallet();//是否使用钱包支付 1已使用 其他的未使用
	if ( payWay != 2 && payWay != 6 ) {
	    wayResultList.add( new PhoneReturnWayResult( 1, Constants.RETURN_WAY[0] ) );
	    if ( orderStatus == 3 || orderStatus == 4 ) {
		wayResultList.add( new PhoneReturnWayResult( 2, Constants.RETURN_WAY[1] ) );
	    }
	} else {
	    //货到付款或到店支付
	    if ( orderStatus == 2 || orderStatus == 3 || orderStatus == 4 ) {
		if ( orderWallet != 1 ) {
		    wayResultList.add( new PhoneReturnWayResult( 2, Constants.RETURN_WAY[1] ) );
		} else {
		    if ( orderStatus == 3 || orderStatus == 4 ) {
			//钱包支付
			wayResultList.add( new PhoneReturnWayResult( 2, Constants.RETURN_WAY[2] ) );
		    } else {
			wayResultList.add( new PhoneReturnWayResult( 1, Constants.RETURN_WAY[0] ) );
		    }
		}
	    }

	}
	return wayResultList;
    }

    public PhoneReturnProductResult getReturnProduct( MallOrder order, MallOrderDetail detail ) {
	PhoneReturnProductResult result = new PhoneReturnProductResult();
	result.setProductId( detail.getProductId() );
	result.setShopId( detail.getShopId() );
	result.setBusId( order.getBusUserId() );
	result.setProductName( detail.getDetProName() );
	result.setProductImageUrl( detail.getProductImageUrl() );
	result.setOrderId( detail.getOrderId() );
	result.setOrderType( order.getOrderType() );
	result.setActivityId( order.getGroupBuyId() );
	result.setReturnPrice( detail.getTotalPrice() );
	if ( CommonUtil.isNotEmpty( detail.getProductSpeciname() ) ) {
	    result.setProductSpecifica( detail.getProductSpeciname().replaceAll( ",", "/" ) );
	}
	return result;
    }

    @Override
    public PhoneReturnProductResult getReturn( Integer orderDetailId, Integer returnId ) {
	if ( CommonUtil.isEmpty( orderDetailId ) || orderDetailId == 0 ) {
	    return null;
	}
	MallOrderDetail detail = mallOrderDetailService.selectById( orderDetailId );
	if ( CommonUtil.isEmpty( detail ) ) {
	    return null;
	}
	MallOrder order = mallOrderService.selectById( detail.getOrderId() );//查询订单信息
	MallOrderReturn returns = new MallOrderReturn();
	returns.setOrderId( detail.getOrderId() );
	returns.setOrderDetailId( detail.getId() );
	MallOrderReturn orderReturn = mallOrderReturnDAO.selectByOrderDetailId( returns );//退款详情
	PhoneReturnProductResult result = getReturnProduct( order, detail );//获取退款商品
	double price = CommonUtil.toDouble( detail.getDetProPrice() ) * detail.getDetProNum();
	if ( CommonUtil.isNotEmpty( detail.getTotalPrice() ) && detail.getTotalPrice() > 0 ) {
	    price = CommonUtil.toDouble( detail.getTotalPrice() );
	}
	if ( CommonUtil.isNotEmpty( orderReturn ) && CommonUtil.isNotEmpty( orderReturn.getRetMoney() ) ) {
	    price = CommonUtil.toDouble( orderReturn.getRetMoney() );
	}
	if ( ( order.getOrderPayWay() == 2 || order.getOrderPayWay() == 6 ) && order.getIsWallet() == 0 ) {
	    price = 0;
	}
	result.setReturnPrice( price );
	if ( price > 0 && CommonUtil.isNotEmpty( order.getOrderFreightMoney() ) ) {
	    double freightMoney = CommonUtil.toDouble( order.getOrderFreightMoney() );
	    if ( freightMoney > 0 ) {
		result.setProductFreight( freightMoney );
	    }
	}
	//查询退款原因
	List< DictBean > dictBeanList = dictService.getDict( "1091" );
	result.setReturnReasonList( dictBeanList );

	if ( CommonUtil.isNotEmpty( orderReturn ) ) {
	    result.setReturnId( orderReturn.getId() );
	    result.setCargoStatus( orderReturn.getCargoStatus() );
	    if ( CommonUtil.isNotEmpty( orderReturn.getRetReasonId() ) ) {
		result.setRetReasonId( orderReturn.getRetReasonId() );
	    }
	    result.setRetRemark( orderReturn.getRetRemark() );
	    if ( CommonUtil.isNotEmpty( orderReturn.getImagesUrl() ) ) {
		result.setReturnImageUrls( orderReturn.getImagesUrl().split( "," ) );
	    }
	    result.setReturnPhone( orderReturn.getRetTelephone() );
	    result.setReturnWay( orderReturn.getRetHandlingWay() );
	}
	if ( order.getOrderStatus() == 3 || order.getOrderStatus() == 4 ) {
	    List< PhoneReturnWayResult > wayResultList = new ArrayList<>();
	    wayResultList.add( new PhoneReturnWayResult( 0, "未收到货" ) );
	    wayResultList.add( new PhoneReturnWayResult( 1, "已收到货" ) );
	    result.setCargoStatusList( wayResultList );
	    result.setIsShowCargoStatus( 1 );
	}

	return result;
    }

    @Override
    public PhoneReturnResult returnDetail( Integer returnId ) {

	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );//退款详情

	MallOrderDetail detail = mallOrderDetailService.selectById( orderReturn.getOrderDetailId() );
	MallStore store = mallStoreDAO.selectById( detail.getShopId() );
	PhoneReturnResult result = new PhoneReturnResult();
	result.setProductId( detail.getProductId() );
	result.setShopId( detail.getShopId() );
	if ( CommonUtil.isNotEmpty( store ) ) {
	    result.setShopName( store.getStoName() );
	}
	result.setBusId( orderReturn.getUserId() );
	result.setProductName( detail.getDetProName() );
	result.setOrderId( detail.getOrderId() );

	result.setReturnId( orderReturn.getId() );
	result.setCreateTime( DateTimeKit.getDateTime( orderReturn.getCreateTime() ) );
	result.setReturnPrice( orderReturn.getRetMoney().doubleValue() );
	result.setRetHandlingWay( orderReturn.getRetHandlingWay() );
	result.setReturnId( orderReturn.getId() );
	result.setRetReasonId( orderReturn.getRetReasonId() );
	result.setRetRemark( orderReturn.getRetRemark() );
	result.setStatus( orderReturn.getStatus() );
	result.setStatusName( OrderUtil.getReturnStatusName( orderReturn ) );
	result.setReturnTime( DateTimeKit.getDateTime( orderReturn.getUpdateTime() ) );
	//查询退款原因
	List< DictBean > dictBeanList = dictService.getDict( "1091" );
	if ( dictBeanList != null && dictBeanList.size() > 0 ) {
	    for ( DictBean bean : dictBeanList ) {
		if ( bean.getItem_key().equals( orderReturn.getRetReasonId().toString() ) ) {
		    result.setRetReasonName( bean.getItem_value() );
		    break;
		}
	    }
	}
	//	result.setReturnReasonList( dictBeanList );

	return result;
    }

    @Override
    public MallOrderReturn selectByOrderDetailId( Integer orderId, Integer orderDetailId ) {
	MallOrderReturn orderReturn = new MallOrderReturn();
	orderReturn.setOrderId( orderId );
	orderReturn.setOrderDetailId( orderDetailId );
	return mallOrderReturnDAO.selectByOrderDetailId( orderReturn );
    }

    @Override
    public PhoneReturnWuLiuResult getReturnWuLiu( Integer returnId ) {
	if ( returnId == 0 ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	}
	MallOrderReturn orderReturn = mallOrderReturnDAO.selectById( returnId );
	if ( CommonUtil.isEmpty( orderReturn ) ) {
	    throw new BusinessException( ResponseEnums.NULL_ERROR.getCode(), ResponseEnums.NULL_ERROR.getDesc() );
	}
	PhoneReturnWuLiuResult wuLiuResult = new PhoneReturnWuLiuResult();
	wuLiuResult.setWlCompany( orderReturn.getWlCompany() );
	wuLiuResult.setWlCompanyId( orderReturn.getWlCompanyId() );
	wuLiuResult.setWlNo( orderReturn.getWlNo() );
	wuLiuResult.setWlRemark( orderReturn.getWlRemark() );
	wuLiuResult.setWlTelephone( orderReturn.getWlTelephone() );
	if ( CommonUtil.isNotEmpty( orderReturn.getReturnAddress() ) ) {
	    wuLiuResult.setReturnAddress( orderReturn.getReturnAddress() );
	}
	if ( CommonUtil.isNotEmpty( orderReturn.getWlImagesUrl() ) ) {
	    wuLiuResult.setWlImagesUrl( orderReturn.getWlImagesUrl().split( "," ) );
	}
	return wuLiuResult;
    }
}
