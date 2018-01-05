package com.gt.mall.service.web.purchase.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.purchase.PurchaseOrderDAO;
import com.gt.mall.dao.purchase.PurchaseReceivablesDAO;
import com.gt.mall.dao.purchase.PurchaseTermDAO;
import com.gt.mall.entity.purchase.PurchaseOrder;
import com.gt.mall.entity.purchase.PurchaseReceivables;
import com.gt.mall.entity.purchase.PurchaseTerm;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.purchase.PurchaseReceivablesService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.pay.SubQrPayParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Service
public class PurchaseReceivablesServiceImpl extends BaseServiceImpl< PurchaseReceivablesDAO,PurchaseReceivables > implements PurchaseReceivablesService {

    @Autowired
    private PurchaseReceivablesDAO purchaseReceivablesDAO;
    @Autowired
    private PurchaseOrderDAO       purchaseOrderDAO;
    @Autowired
    private PurchaseTermDAO        purchaseTermDAO;
    @Autowired
    private MemberService          memberService;
    @Autowired
    private WxPublicUserService    wxPublicUserService;
    @Autowired
    private PayOrderService        payOrderService;
    @Autowired
    private PayService        payService;


    /**
     * 使用手机端支付调用方法
     * @param memberId
     * @param busId
     * @param termId
     * @param money
     * @param discountmoney
     * @param fenbi
     * @param jifen
     * @param discount
     * @param paymentType
     * @param orderId
     * @param dvId
     * @param disCountdepict
     * @param judgeBrowser
     * @return
     * @throws Exception
     */
    @Override
    public void aliCgPay( Integer memberId, Integer busId, String termId, Double money, Double discountmoney, Double fenbi, Integer jifen, Double discount,
		    String paymentType, Integer orderId, Integer dvId, String disCountdepict,Integer judgeBrowser) throws Exception {
	if ( CommonUtil.isEmpty( memberId ) || CommonUtil.isEmpty( money ) ) {
	    throw new Exception();
	}
	try {
	    //新增收款记录
	    PurchaseReceivables receivables = new PurchaseReceivables();
	    receivables.setBusId( busId );
	    receivables.setBuyMode( paymentType );
	    receivables.setBuyTime( new Date() );
	    receivables.setCoupon( dvId != null ? dvId.toString() : null );
	    receivables.setDiscount( discount );
	    receivables.setFansCorrency( fenbi );
	    receivables.setHaveTerm( termId == null || termId.equals( "" ) ? "0" : "1" );
	    receivables.setIntegral( Double.parseDouble( jifen.toString() ) );
	    receivables.setMemberId( memberId );
	    receivables.setMoney( discountmoney );
	    String receivablesNumber = "CG" + System.currentTimeMillis();
	    receivables.setReceivablesNumber( receivablesNumber );
	    receivables.setOrderId( orderId );
	    receivables.setBuyStatus( 0 );
	    receivables.setTermId( termId == null || termId.equals( "" ) ? null : Integer.parseInt( termId ) );
	    purchaseReceivablesDAO.insert( receivables );

	    //Member member = memberService.findMemberById( memberId, null );
	    // WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo(receivablesNumber);
	    // if ( wxPayOrder != null ) {
	    //receivablesNumber = "CG" + System.currentTimeMillis();
	    //receivables.setReceivablesNumber( receivablesNumber );
	    //purchaseReceivablesDAO.updateById( receivables );
	    //}
	    SubQrPayParams subQrPayParams=new SubQrPayParams();
	    subQrPayParams.setAppidType(0);
	    subQrPayParams.setBusId(busId);
	    subQrPayParams.setDesc("对外报价订单支付");
	    subQrPayParams.setIsreturn(1);
	    subQrPayParams.setIsSendMessage(0);
	    subQrPayParams.setMemberId(memberId);
	    subQrPayParams.setModel(35);
	    subQrPayParams.setNotifyUrl(PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/payBackMethod.do");
	    subQrPayParams.setOrderNum(receivablesNumber);
	    subQrPayParams.setPayWay(judgeBrowser);
	    subQrPayParams.setReturnUrl(PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/findOrder.do?orderId=" + orderId);
	    subQrPayParams.setSourceType(1);
	    subQrPayParams.setTotalFee(discountmoney);
	    payService.payapi(subQrPayParams);

	} catch ( Exception e ) {
	    throw new Exception();
	}
    }

    /**
     * 支付回调方法
     * @param receivablesNumber
     * @throws Exception
     */
    @Override
    public void addReceivables( String receivablesNumber ) throws Exception {
	try {
	    //得到收款信息
	    PurchaseReceivables receivable = purchaseReceivablesDAO.selectReceivable( receivablesNumber );
	    //如果收款的订单涉及到分期支付  修改分期状态
	    if ( receivable.getHaveTerm() != null && receivable.getHaveTerm().equals( "1" ) && receivable.getTermId() != null ) {
		PurchaseTerm term = new PurchaseTerm();
		term.setId( receivable.getTermId() );
		term.setTermBuy( "1" );
		purchaseTermDAO.updateById( term );
	    }
	    //把收款状态改为已收款
	    receivable.setBuyStatus( 1 );
	    purchaseReceivablesDAO.updateById( receivable );
	    //修改订单记录
	    updateUserConsume( receivable );

	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    public void updateUserConsume( PurchaseReceivables receivable ) {
	PurchaseOrder order = purchaseOrderDAO.selectById( receivable.getOrderId() );
	if ( CommonUtil.isEmpty( order.getMemberId() ) ) {
	    order.setMemberId( receivable.getMemberId() );
	    purchaseOrderDAO.updateById( order );
	}
	if (order.getOrderType().equals("1")) {
	    //查询分期
	    List<Map<String, Object>> termList = purchaseTermDAO.findTermList(order.getId());
	    for (int i = 0; i < termList.size(); i++) {
		PurchaseTerm term = (PurchaseTerm) termList.get(i);
		if (term.getTermBuy().equals("0")) {
		    break;
		}
		if(i==termList.size()-1){
		    PurchaseOrder orderEntity=new PurchaseOrder();
		    orderEntity.setId(order.getId());
		    orderEntity.setOrderStatus("3");
		    purchaseOrderDAO.updateById(orderEntity);
		}
	    }
	}
	else {
	    if(!receivable.getBuyMode().equals("4")){
		PurchaseOrder orderEntity=new PurchaseOrder();
		orderEntity.setId(order.getId());
		orderEntity.setOrderStatus("3");
		purchaseOrderDAO.updateById(orderEntity);
	    }

	}
    }

    /**
     * 使用储值卡或者货到付款等支付方式时进入
     *
     * @param request
     * @param memberId
     * @param orderId
     * @param busId
     * @param buyMode
     * @param money
     * @param fansCurrency
     * @param integral
     * @param coupon
     * @param termId
     * @param discount
     */
    @Transactional( rollbackFor = Exception.class )
    public String addReceivables( HttpServletRequest request, Integer memberId, Integer orderId, Integer busId, String buyMode, Double money, Double fansCurrency, Double integral,
		    String coupon, String termId, Double discount ) {
	try {
	    //新增收款记录
	    PurchaseReceivables receivables = new PurchaseReceivables();
	    receivables.setBusId( busId );
	    receivables.setBuyMode( buyMode );
	    receivables.setBuyTime( new Date() );
	    receivables.setCoupon( coupon );
	    receivables.setDiscount( discount );
	    receivables.setFansCorrency( fansCurrency );
	    receivables.setHaveTerm( termId == null || termId.equals( "" ) ? "0" : "1" );
	    receivables.setIntegral(integral);
	    receivables.setMemberId( memberId );
	    receivables.setMoney( money );
	    receivables.setReceivablesNumber( "CG" + System.currentTimeMillis() );
	    receivables.setOrderId( orderId );
	    receivables.setTermId( termId == null || termId.equals( "" ) ? null : Integer.parseInt( termId ) );
	    receivables.setBuyStatus( 1 );
	    purchaseReceivablesDAO.insert( receivables );

	    if ( CommonUtil.isNotEmpty( termId ) ) {
		//修改分期状态
		PurchaseTerm term = new PurchaseTerm();
		term.setId( Integer.parseInt( termId ) );
		term.setTermBuy( "1" );
		purchaseTermDAO.updateById( term );
	    }
	    updateUserConsume(receivables);
	    return receivables.getReceivablesNumber();
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return null;
	}
    }
}
