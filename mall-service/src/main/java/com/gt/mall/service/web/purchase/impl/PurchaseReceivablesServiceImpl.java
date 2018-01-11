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
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.pay.SubQrPayParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    private PayService             payService;


    /**
     * 支付回调方法
     *
     * @param receivablesNumber
     *
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
	if ( order.getOrderType().equals( "1" ) ) {
	    //查询分期
	    List< Map< String,Object > > termList = purchaseTermDAO.findTermList( order.getId() );
	    for ( int i = 0; i < termList.size(); i++ ) {
		PurchaseTerm term = (PurchaseTerm) termList.get( i );
		if ( term.getTermBuy().equals( "0" ) ) {
		    break;
		}
		if ( i == termList.size() - 1 ) {
		    PurchaseOrder orderEntity = new PurchaseOrder();
		    orderEntity.setId( order.getId() );
		    orderEntity.setOrderStatus( "3" );
		    purchaseOrderDAO.updateById( orderEntity );
		}
	    }
	} else {
	    if ( !receivable.getBuyMode().equals( "4" ) ) {
		PurchaseOrder orderEntity = new PurchaseOrder();
		orderEntity.setId( order.getId() );
		orderEntity.setOrderStatus( "3" );
		purchaseOrderDAO.updateById( orderEntity );
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
	    receivables.setIntegral( integral );
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
	    updateUserConsume( receivables );
	    return receivables.getReceivablesNumber();
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * 分页查询数据
     *
     * @param parms
     *
     * @return
     */
    @Override
    public PageUtil findList( Map< String,Object > parms ) {
	try {
	    int pageSize = 10;
	    int count = 0;
	    List< Map< String,Object > > map = new ArrayList< Map< String,Object > >();
	    int curPage = CommonUtil.isEmpty( parms.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( parms.get( "curPage" ) );
	    count = purchaseReceivablesDAO.findListCount( parms );
	    PageUtil page = new PageUtil( curPage, pageSize, count, "" );
	    parms.put( "pageFirst", ( page.getCurPage() - 1 ) * 10 );
	    parms.put( "pageLast", 10 );
	    if ( count > 0 ) {
		map = purchaseReceivablesDAO.findList( parms );
	    }
	    page.setSubList( map );
	    return page;
	} catch ( Exception e ) {
	    e.printStackTrace();
	    return null;
	}
    }
}
