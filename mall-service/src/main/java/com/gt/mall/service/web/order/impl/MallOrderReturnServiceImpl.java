package com.gt.mall.service.web.order.impl;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.order.MallOrderReturnService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.utils.CommonUtil;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.param.wx.BusIdAndindustry;
import com.gt.util.entity.result.pay.WxPayOrder;
import com.gt.util.entity.result.wx.ApiWxApplet;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    private MallOrderDAO mallOrderDAO;

    @Autowired
    private MallProductDAO mallProductDAO;

    @Autowired
    private MallProductInventoryService mallProductInventoryService;

    @Autowired
    private MallOrderService mallOrderService;

    @Autowired
    private MallOrderDetailDAO mallOrderDetailDAO;

    @Autowired
    private MemberService memberService;

    @Autowired
    private WxPublicUserService wxPublicUserService;

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private PayService payService;

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
		if ( product.getProSaleTotal() - productNum > 0 )
		    product.setProSaleTotal( product.getProSaleTotal() - productNum );//商品销量
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
}
