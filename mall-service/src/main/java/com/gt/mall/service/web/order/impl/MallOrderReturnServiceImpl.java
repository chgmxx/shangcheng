package com.gt.mall.service.web.order.impl;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.DictBean;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.result.phone.order.returns.PhoneReturnProductResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnResult;
import com.gt.mall.result.phone.order.returns.PhoneReturnWayResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.order.MallOrderDetailService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.OrderUtil;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.param.wx.BusIdAndindustry;
import com.gt.util.entity.result.pay.WxPayOrder;
import com.gt.util.entity.result.wx.ApiWxApplet;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 系统退款（不是买家申请的）
     */
    @Override
    public boolean returnEndOrder( Integer orderId, Integer orderDetailId ) throws Exception {
	boolean resultFlag = false;
	Map< String,Object > map = new HashMap<>();//存放退款信息
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
				if ( resultmap.get( "code" ).toString().equals( "1" ) ) {
				    resultFlag = true;
				    //退款成功修改退款状态
				    updateReturnStatus( pUser, detailMap, returnNo );//微信退款

				}
			    }
			} else if ( wxPayOrder.getTradeState().equals( "NOTPAY" ) ) {
			    logger.info( "订单：" + wxPayOrder.getOutTradeNo() + "未支付" );
			}

		    } else if ( orderPayWay == 3 && CommonUtil.isNotEmpty( pUser ) ) {//储值卡退款
			Map< String,Object > returnParams = new HashMap<>();
			returnParams.put( "busId", busUserId );
			returnParams.put( "orderNo", orderNo );
			returnParams.put( "money", orderMoney );
			//储值卡退款
			Map< String,Object > payResultMap = memberService.refundMoney( returnParams );//memberPayService.chargeBack(memberId,money);
			if ( payResultMap != null ) {
			    if ( CommonUtil.isNotEmpty( payResultMap.get( "code" ) ) ) {
				int code = CommonUtil.toInteger( payResultMap.get( "code" ) );
				if ( code == 1 ) {//退款成功修改退款状态
				    resultFlag = true;
				    updateReturnStatus( pUser, detailMap, returnNo );//微信退款
				}
			    }
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
			    if ( resultmap.get( "code" ).toString().equals( "1" ) ) {
				resultFlag = true;
				//退款成功修改退款状态
				updateReturnStatus( pUser, detailMap, returnNo );//微信退款
			    }
			}
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

		product.setProStockTotal( product.getProStockTotal() + productNum );//商品库存
		if ( product.getProSaleTotal() - productNum > 0 ) {
		    product.setProSaleTotal( product.getProSaleTotal() - productNum );//商品销量
		}
		mallProductDAO.updateById( product );

		//修改商品规格库存
		if ( product.getIsSpecifica() == 1 ) {
		    String specIds = mallOrderDAO.selectSpecByDetailId( detailId );
		    Map< String,Object > proMap = new HashMap<>();
		    proMap.put( "specificaIds", specIds );
		    proMap.put( "proId", productId );
		    MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( proMap );
		    int total = proInv.getInvNum() + productNum;//库存
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
		    proMap.put( "saleNum", invSaleNum );
		    mallProductInventoryService.updateProductInventory( proMap );
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
	if ( ( order.getOrderPayWay() == 2 || order.getOrderPayWay() == 6 ) || order.getIsWallet() == 0 ) {
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
	    result.setRetReasonId( orderReturn.getRetReasonId() );
	    result.setRetRemark( orderReturn.getRetRemark() );
	    if ( CommonUtil.isNotEmpty( orderReturn.getImagesUrl() ) ) {
		result.setReturnImageUrls( orderReturn.getImagesUrl().split( "," ) );
	    }
	    result.setReturnPhone( orderReturn.getRetTelephone() );
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
	result.setCreateTime( orderReturn.getCreateTime() );
	result.setReturnPrice( orderReturn.getRetMoney().doubleValue() );
	result.setRetHandlingWay( orderReturn.getRetHandlingWay() );
	result.setReturnId( orderReturn.getId() );
	result.setRetReasonId( orderReturn.getRetReasonId() );
	result.setRetRemark( orderReturn.getRetRemark() );
	result.setStatus( orderReturn.getStatus() );
	result.setStatusName( OrderUtil.getReturnStatusName( orderReturn ) );
	result.setReturnTime( orderReturn.getUpdateTime() );
	//查询退款原因
	List< DictBean > dictBeanList = dictService.getDict( "1091" );
	if ( dictBeanList != null && dictBeanList.size() > 0 ) {
	    for ( DictBean bean : dictBeanList ) {
		if ( bean.getItem_key() == orderReturn.getRetReasonId() ) {
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
}
