package com.gt.mall.web.service.order.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.WxPayOrder;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.inter.service.MemberService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.order.MallOrderReturnService;
import com.gt.mall.web.service.order.MallOrderService;
import com.gt.mall.web.service.product.MallProductInventoryService;
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
		/*Integer memberId = CommonUtil.toInteger( detailMap.get( "buyer_user_id" ) );*/
		Integer orderPayWay = CommonUtil.toInteger( detailMap.get( "order_pay_way" ) );
		Double orderMoney = CommonUtil.toDouble( detailMap.get( "orderMoney" ) );
		String orderNo = CommonUtil.toString( detailMap.get( "orderNo" ) );
		int busUserId = CommonUtil.toInteger( detailMap.get( "bus_user_id" ) );
		//todo 调用小屁孩接口，根据粉丝id查询公众号信息
		WxPublicUsers pUser = null;
		String returnNo = "TK" + System.currentTimeMillis();
		if ( detailMap.get( "orderStatus" ).toString().equals( "2" ) ) {

		    if ( orderPayWay == 1 && CommonUtil.isNotEmpty( pUser ) ) {//微信退款
			//todo 调用小屁孩接口    根据订单号查询微信订单信息
			WxPayOrder wxPayOrder = null;//wxPayOrderMapper.selectByOutTradeNo(orderNo);
			if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
			    map.put( "appid", pUser.getAppid() );// 公众号
			    map.put( "mchid", pUser.getMchId() );// 商户号
			    map.put( "sysOrderNo", wxPayOrder.getOutTradeNo() );// 系统订单号
			    map.put( "wx_order_no", wxPayOrder.getTransactionId() );// 微信订单号
			    map.put( "outRefundNo", returnNo );// 商户退款单号(系统生成)
			    map.put( "totalFee", wxPayOrder.getTotalFee() );// 总金额
			    map.put( "refundFee", orderMoney );// 退款金额
			    map.put( "key", pUser.getApiKey() );// 商户支付密钥
			    map.put( "wxOrdId", wxPayOrder.getId() );// 微信订单表主键
			    logger.error( "JSONObject.fromObject(resultmap).toString()" + JSONObject.fromObject( map ).toString() );
			    //todo 调用小屁孩接口   会员申请退款
			    Map< String,Object > resultmap = null;//payService.memberPayRefund(map);
			    logger.error( "JSONObject.fromObject(resultmap).toString()" + JSONObject.fromObject( resultmap ).toString() );
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
			returnParams.put( "ucType", 2 );
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
		    //todo 调用小屁孩接口    根据订单号查询微信订单信息
		    WxPayOrder wxPayOrder = null;//wxPayOrderMapper.selectByOutTradeNo(orderNo);
		    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
			Map< String,Object > appletMap = new HashMap<>();
			appletMap.put( "sysOrderNo", wxPayOrder.getOutTradeNo() );//系统单号
			appletMap.put( "outRefundNo", returnNo );//退款单号
			appletMap.put( "totalFee", wxPayOrder.getTotalFee() );//总金额
			appletMap.put( "refundFee", orderMoney );//退款金额
			appletMap.put( "busId", wxPayOrder.getBus_id() );
			appletMap.put( "mchid", pUser.getMchId() );// 商户号
			logger.info( "appletWxReturnParams:" + JSONObject.fromObject( appletMap ).toString() );
			//todo 调用小屁孩接口，小程序退款
			Map< String,Object > resultmap = null;//payService.memberPayRefund(appletMap);
			logger.info( "appletWxReturnResult:" + JSONObject.fromObject( resultmap ).toString() );
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
	/*Integer memberId = CommonUtil.toInteger( detailMap.get( "buyer_user_id" ) );*/
	Integer detailId = CommonUtil.toInteger( detailMap.get( "dId" ) );
	/*Integer orderId = CommonUtil.toInteger( detailMap.get( "oId" ) );
	Integer shopId = CommonUtil.toInteger( detailMap.get( "shopId" ) );
	Double orderMoney = CommonUtil.toDouble( detailMap.get( "orderMoney" ) );
	String orderNo = detailMap.get( "order_no" ).toString();*/

	MallOrderDetail detail = new MallOrderDetail();
	detail.setId( detailId );// 修改订单详情的状态
	detail.setStatus( 1 );
	mallOrderDetailDAO.updateById( detail );

	//todo 调用彭江丽接口，添加会员退款记录

	/*int flowId = 0;
	if ( CommonUtil.isNotEmpty( detailMap.get( "flow_id" ) ) ) {
	    flowId = CommonUtil.toInteger( detailMap.get( "flow_id" ) );
	}
	if ( flowId == 0 ) {
	    //通过店铺id查询店铺信息
	    MallStore store = mallStoreDAO.selectById( shopId );
	    //查询该订单的消费记录
	    UserConsume consume = userConsumeMapper.findByOrderCode1(orderNo);
	    int xiaofeiId = 0;
	    if(consume == null){
		consume =  new UserConsume();
	    }else{
		xiaofeiId = consume.getId();
		consume.setId(null);
	    }
	    int shopsId = store.getWxShopId();
	    if(shopsId <= 0){
		shopsId = store.getId();
	    }

	    if(CommonUtil.isNotEmpty(pUser)){
		consume.setPublicId(pUser.getId());//公众号id
	    }
	    Member member = memberService.findById(memberId);
	    consume.setBususerid(member.getBusid());//商户id
	    consume.setModuletype(Byte.valueOf("0"));//模块类型 0 商城
	    consume.setStoreid(shopsId);//店铺id
	    //			consume.setIsparent(isBrachStore);//是否是子店铺
	    consume.setMemberid(memberId);//买家id
	    consume.setRecordtype(Byte.valueOf("3"));//记录类型 3退款记录
	    consume.setUctype(Byte.valueOf("12"));//消费类型
	    consume.setTotalmoney(orderMoney);//退款金额
	    consume.setDiscountmoney(null);
	    consume.setOrderid(orderId);//订单id
	    if(CommonUtil.isNotEmpty(detailMap.get("order_pid"))){
		if(detailMap.get("order_pid").toString().equals("0")){
		    consume.setOrderid(CommonUtil.toInteger(detailMap.get("order_pid")));//订单id
		}
	    }
	    int payWay = CommonUtil.toInteger(detailMap.get("order_pay_way"));//支付方式
	    if(payWay == 2){//货到付款
		payWay = 4;
	    }else if(payWay == 3){//储值卡支付
		payWay = 5;
	    }
	    consume.setUctable("t_mall_order_detail");//详情表名
	    consume.setCreatedate(new Date());//创建时间
	    consume.setPaymenttype(Byte.valueOf(payWay+""));//支付方式
	    consume.setPaystatus(Byte.valueOf("3"));//支付状态 3 退单
	    consume.setOrdercode(returnNo);//订单号微信或支付宝或自定义
	    //添加退款记录
	    userConsumeMapper.insertSelective(consume);
	    if(consume.getId() > 0){//退款成功
		if(xiaofeiId > 0){
		    UserConsumeRefundKey refund = new UserConsumeRefundKey();
		    refund.setConsumeid(xiaofeiId);//消费id
		    refund.setRefundid(consume.getId());//新添加的退款id
		    consumeRefundMapper.insertSelective(refund);
		}

		//判断是否是会员
		if(memberPayService.isMemember(memberId)){
		    memberPayService.refundBack(returnNo);//退款成功 回调 添加card操作记录
		}
	    }
	}*/

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
