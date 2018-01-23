package com.gt.mall.service.web.order.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.presale.MallPresaleRankDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.product.MallProductInventoryDAO;
import com.gt.mall.dao.product.MallProductSpecificaDAO;
import com.gt.mall.dao.seller.MallSellerDAO;
import com.gt.mall.dao.seller.MallSellerIncomeDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.presale.MallPresaleRank;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerIncome;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.order.QuartzOrderService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service
public class QuartzOrderServiceImpl implements QuartzOrderService {

    @Autowired
    private MallOrderDAO            orderMapper;
    @Autowired
    private MallProductDAO          mallProductDAO;
    @Autowired
    private MallProductSpecificaDAO mallProductSpecificaDAO;
    @Autowired
    private MallProductInventoryDAO mallProductInventoryDAO;

    @Autowired
    private MallPaySetDAO paySetMapper;

    @Autowired
    private MallPresaleRankDAO  rankMapper;
    @Autowired
    private MallSellerIncomeDAO mallSellerIncomeDAO;
    @Autowired
    private MallSellerDAO       mallSellerDAO;
    @Autowired
    private MemberService       memberService;
    @Autowired
    private MemberPayService    memberPayService;

    @Override
    public void closeOrderNoPay( String key ) {
	//判断是否存在未支付的订单
	if ( JedisUtil.exists( key ) ) {
	    Map< String,String > map = JedisUtil.mapGetAll( key );//获取所有未支付的订单
	    if ( map != null ) {
		Set< String > set = map.keySet();
		for ( String orderId : set ) {
		    String oStr = map.get( orderId );//未支付的订单信息
		    if ( CommonUtil.isNotEmpty( oStr ) ) {
			JSONObject obj = JSONObject.parseObject( oStr );
			String times = obj.getString( "times" );//订单支付的时间
						/*System.out.println(times);*/
			String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
			String eDate = DateTimeKit.format( new Date(), format );
			long mins = DateTimeKit.minsBetween( times, eDate, 60000, format );
			if ( mins > 30 ) {// 订单已经支付了30分钟还未退款
			    // 恢复订单库存
			    boolean flag = addInvNum( orderId );
			    if ( flag ) {
				JedisUtil.mapdel( key, orderId );
			    }
			}
		    }
		}
	    }
	}
    }

    @Override
    public void newCloseOrderNoPay( String key ) {

	//判断是否存在未支付的订单
	if ( JedisUtil.exists( key ) ) {
	    Map< String,String > map = JedisUtil.mapGetAll( key );//获取所有未支付的订单
	    if ( map != null ) {
		Set< String > set = map.keySet();
		for ( String orderId : set ) {
		    String oStr = map.get( orderId );//未支付的订单信息
		    if ( CommonUtil.isNotEmpty( oStr ) ) {
			JSONObject obj = JSONObject.parseObject( oStr );
			String times = obj.getString( "times" );//订单创建的时间

			Integer orderCancel = null;//订单取消时间
			if ( CommonUtil.isEmpty( obj.getString( "orderCancel" ) ) ) {
			    //默认三天未支付取消订单
			    orderCancel = 1440 * 3;
			} else {
			    orderCancel = Integer.valueOf( obj.getString( "orderCancel" ) );
			}
			String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
			String eDate = DateTimeKit.format( new Date(), format );
			long mins = DateTimeKit.minsBetween( times, eDate, 60000, format );
			if ( mins >= orderCancel ) {// 订单已经超过设定时间还未退款
			    // 恢复订单库存
			    boolean flag = addInvNum( orderId );
			    if ( flag ) {
				JedisUtil.mapdel( key, orderId );
			    }
			}
		    }
		}
	    }
	}
    }

    public boolean addInvNum( String orderId ) {
	boolean flag = false;
	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "id", orderId );
	//		params.put("status", 1);
	// 查询所有未支付的订单
	MallOrder order = orderMapper.selectOrderById( params );

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
		orderMapper.updateByNoMoney( params );
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

    @Override
    public void insertPayLog( MallOrder order ) {
		/*if(CommonUtil.isNotEmpty(map.get("buyer_user_id"))){
			int buyerUserId = CommonUtil.toInteger(map.get("buyer_user_id"));
			
			Member	member = memberMapper.selectByPrimaryKey(buyerUserId);
			
			double totalPrice = 0;
			
			if(CommonUtil.isNotEmpty(dMap.get("totalPrice"))){
				totalPrice = Double.valueOf(dMap.get("totalPrice").toString());
			}
			if(CommonUtil.isNotEmpty(dMap.get("sumPrice")) && totalPrice == 0){
				totalPrice = Double.valueOf(dMap.get("sumPrice").toString());
			}
			if(totalPrice > 0){
				PaySuccessLog log = new PaySuccessLog();
				log.setTotalmoney(totalPrice);
				log.setMemberid(member.getId());
				log.setModel(2);
				Date date = new Date();
				if(CommonUtil.isNotEmpty(dMap.get("return_day"))){
					int returnDay = CommonUtil.toInteger(dMap.get("return_day"));
					if(returnDay > 0){
						date = DateTimeKit.addDays(date, returnDay);
					}
				}
				log.setDate(date);
				if(CommonUtil.isNotEmpty(map.get("order_no"))){
					String orderNo = map.get("order_no").toString();
					log.setOrderno(orderNo);
				}
				
				int count = logMapper.insertSelective(log);
				if(count > 0){
					System.out.println("加入PaySuccessLog成功,订单号："+log.getOrderno());
				}
			}
			
		}*/
    }

    /**
     * 预售商品，订单完成赠送虚拟物品
     *
     * @param map
     */
    @Override
    public void presaleOrderGive( Map< String,Object > map ) {
	int memberId = 0;
	if ( CommonUtil.isNotEmpty( map.get( "buyer_user_id" ) ) ) {
	    memberId = CommonUtil.toInteger( map.get( "buyer_user_id" ) );
	}
	int orderId = CommonUtil.toInteger( map.get( "id" ) );
	int userId = 0;//商户id
	//	int publicId = 0;//公众号id
	//	int integral = 0;//积分
	//	double fenbi = 0;//粉币
	//	int totalIntegral = 0;//历史积分

	Member member1 = memberService.findMemberById( memberId, null );
	if ( CommonUtil.isNotEmpty( member1 ) ) {
	    if ( CommonUtil.isNotEmpty( member1.getBusid() ) ) {
		userId = member1.getBusid();
	    }
	    //	    if ( CommonUtil.isNotEmpty( member1.getPublicId() ) ) {
	    //		publicId = member1.getPublicId();
	    //	    }
	    //	    if ( CommonUtil.isNotEmpty( member1.getIntegral() ) ) {
	    //		integral = member1.getIntegral();
	    //	    }
	    //	    if ( CommonUtil.isNotEmpty( member1.getFansCurrency() ) ) {
	    //		fenbi = member1.getFansCurrency();
	    //	    }
	    //	    if ( CommonUtil.isNotEmpty( member1.getTotalintegral() ) ) {
	    //		totalIntegral = member1.getTotalintegral();
	    //	    }
	}
	boolean isOpenGive = false;//是否开启预售送礼
	//查询是否开启了预售送礼
	MallPaySet set = new MallPaySet();
	set.setUserId( userId );
	MallPaySet payset = paySetMapper.selectOne( set );

	if ( CommonUtil.isNotEmpty( payset ) ) {
	    if ( payset.getIsPresale().toString().equals( "1" ) ) {
		if ( payset.getIsPresaleGive().toString().equals( "1" ) ) {
		    isOpenGive = true;
		}
	    }
	}
	if ( isOpenGive ) {
	    if ( userId > 0 ) {

		MallPresaleRank rank = new MallPresaleRank();
		rank.setOrderId( orderId );
		rank.setPresaleId( CommonUtil.toInteger( map.get( "group_buy_id" ) ) );
		rank.setMemberId( memberId );
		rank.setIsGive( "0" );
		//查询评价送礼的情况
		MallPresaleRank presaleRank = rankMapper.selectByPresale( rank );

		if ( CommonUtil.isNotEmpty( presaleRank ) ) {
		    int giveType = presaleRank.getType();
		    int giveNum = presaleRank.getGiveNum();
		    if ( presaleRank.getGiveNum() > 0 ) {

			Integer integral = 0;
			Double fenbi = 0d;
			if ( giveType == 1 ) {//送积分
			    integral = giveNum;
			} else if ( giveType == 2 ) {//送粉币
			    fenbi = CommonUtil.toDouble( giveNum );
			/*int rec_type = 1;//1：粉币 2：流量
			int fre_type = 34;//冻结类型  34 商城预售送粉币
			int fkId = 0;//外键ID
			FenbiSurplus surplus = new FenbiSurplus();
			surplus.setBusId( userId );
			surplus.setFkId( fkId );
			surplus.setRec_type( rec_type );
			surplus.setFre_type( fre_type );

			FenBiCount count = fenBiFlowService.getFenbiSurplus( surplus );
			Double fenbiCount = null;
			if ( count != null ) {
			    fenbiCount = count.getCount();
			}
			if ( fenbiCount >= Double.valueOf( giveNum ) ) {
			    member.setCurrencydate( new Date() );
			    member.setFansCurrency( Double.valueOf( ( fenbi + giveNum ) ) );

			    FenbiFlowRecord record = fenBiFlowService.getFenbiFlowRecord( surplus );
			    if ( CommonUtil.isNotEmpty( record ) ) {
				*//*FenbiFlowRecord fenbiFlow = new FenbiFlowRecord();
				fenbiFlow.setId( record.getId() );
				fenbiFlow.setRecUseCount( Double.valueOf( record.getRecUseCount() + giveNum ) );*//*
				UpdateFenbiReduce reduce = new UpdateFenbiReduce();
				reduce.setBusId( userId );
				reduce.setCount( Double.valueOf( record.getRecUseCount() + giveNum ) );
				reduce.setFreType( fre_type );
				reduce.setFkId( fkId );
				fenBiFlowService.updaterecUseCountVer2( reduce );
				flag = true;
			    }

			}*/
			}
			Map< String,Object > resultMap = memberPayService.updateJifenAndFenBiByPinglu( memberId, integral, fenbi );
			if ( resultMap.get( "code" ).toString().equals( "1" ) ) {
			    MallPresaleRank ranks = new MallPresaleRank();
			    ranks.setId( presaleRank.getId() );
			    ranks.setIsGive( "1" );
			    rankMapper.updateById( ranks );
			}
		    }

		}
	    }
	}
    }

    @Override
    public void closeOrderByDaoDian() {
	orderMapper.closeDaoDianOrder();
    }

    @Override
    public void sellerIncome( Map< String,Object > dMap, Map< String,Object > map ) {
	int detailId = CommonUtil.toInteger( dMap.get( "id" ) );
	int saleMemberId = 0;
	if ( CommonUtil.isNotEmpty( dMap.get( "sale_member_id" ) ) ) {
	    saleMemberId = CommonUtil.toInteger( dMap.get( "sale_member_id" ) );
	}
	if ( saleMemberId > 0 ) {
	    //detailId
	    Map< String,Object > params = new HashMap< String,Object >();
	    params.put( "orderDetailId", detailId );
	    params.put( "orderId", map.get( "id" ) );
	    params.put( "saleMemberId", saleMemberId );
	    List< Map< String,Object > > incomeList = mallSellerIncomeDAO.selectSellerIncome( params );
	    if ( CommonUtil.isNotEmpty( incomeList ) ) {
		for ( Map< String,Object > incomeMap : incomeList ) {
		    if ( CommonUtil.isNotEmpty( incomeMap ) ) {
			if ( CommonUtil.isNotEmpty( incomeMap.get( "income_commission" ) ) ) {
			    params.put( "commission", incomeMap.get( "income_commission" ) );
			}
			if ( CommonUtil.isNotEmpty( incomeMap.get( "income_integral" ) ) ) {
			    double integral = Double.valueOf( incomeMap.get( "income_integral" ).toString() );
			    if ( integral > 0 ) {
				params.put( "incomeIntegral", incomeMap.get( "income_integral" ) );
				Map< String,Object > jifenParams = new HashMap<>();
				jifenParams.put( "memberId", saleMemberId );
				jifenParams.put( "jifen", incomeMap.get( "income_integral" ) );
				memberService.updateJifen( jifenParams );//新增积分记录
			    }
			}
			if ( CommonUtil.isNotEmpty( incomeMap.get( "income_commission" ) ) || CommonUtil.isNotEmpty( incomeMap.get( "income_integral" ) ) ) {
			   /* orderMapper.updateSeller( params );*/
			    MallSeller seller = new MallSeller();
			    seller.setMemberId( saleMemberId );
			    Double freeze = CommonUtil.toDouble( incomeMap.get( "income_commission" ) );
			    Double canPresented = CommonUtil.toDouble( incomeMap.get( "income_commission" ) );
			    Double incomeIntegral = CommonUtil.toDouble( incomeMap.get( "income_integral" ) );
			    if ( seller.getFreezeCommission() != null ) {
				freeze = seller.getFreezeCommission().doubleValue() - freeze;
			    }
			    if ( seller.getCanPresentedCommission() != null ) {
				canPresented = canPresented + seller.getCanPresentedCommission().doubleValue();
			    }
			    if ( seller.getIncomeIntegral() != null ) {
				incomeIntegral = incomeIntegral + seller.getIncomeIntegral().doubleValue();
			    }
			    seller.setFreezeCommission( new BigDecimal( freeze ) );
			    seller.setCanPresentedCommission( new BigDecimal( canPresented ) );
			    seller.setIncomeIntegral( new BigDecimal( incomeIntegral ) );

			    mallSellerDAO.updateById( seller );

			    MallSellerIncome income = new MallSellerIncome();
			    income.setId( CommonUtil.toInteger( incomeMap.get( "id" ) ) );
			    income.setIsGet( 0 );
			    mallSellerIncomeDAO.updateById( income );
			}
		    }
		}
	    }
	}

    }

    /**
     * 订单是否还能退款
     */
    @Override
    public boolean isOrderReturn( Map< String,Object > map, Map< String,Object > dMap ) {
	boolean flag = true;
	String status = dMap.get( "status" ).toString();
	//订单商品并没有退款
	if ( status.equals( "-3" ) || status.equals( "1" ) || status.equals( "5" ) || status.equals( "-2" ) ) {
	    int returnDay = 0;
	    if ( CommonUtil.isNotEmpty( dMap.get( "return_day" ) ) ) {
		returnDay = CommonUtil.toInteger( dMap.get( "return_day" ) );
	    }
	    if ( returnDay > 0 && CommonUtil.isNotEmpty( map.get( "update_time" ) ) ) {
		String times = map.get( "update_time" ).toString();

		String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
		String eDate = DateTimeKit.format( new Date(), format );
		long day = DateTimeKit.minsBetween( times, eDate, 3600000 * 7, format );
		if ( day > returnDay ) {// 订单完成时间大于已支付时间
		    flag = true;
		}
	    } else {
		flag = true;
	    }
	    if ( !flag ) {
		flag = false;
	    }
	} else {
	    flag = false;
	}
	return flag;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public boolean rollbackOrderByFlow( Map< String,Object > params ) {
	int orderId = CommonUtil.toInteger( params.get( "id" ) );

	MallOrder mallOrder = new MallOrder();
	mallOrder.setId( orderId );
	if ( params.get( "status" ).toString().equals( "0" ) ) {//成功
	    mallOrder.setFlowRechargeStatus( 1 );
	} else {//失败
	    mallOrder.setFlowRechargeStatus( 2 );
	}
	//修改订单状态
	int count = orderMapper.updateById( mallOrder );
	if ( count <= 0 ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "修改订单状态失败" );
	}
	return true;
    }

}
