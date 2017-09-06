package com.gt.mall.service.web.order.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPayOrder;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.bean.member.PaySuccessBo;
import com.gt.mall.bean.member.ReturnParams;
import com.gt.mall.bean.member.UserConsumeParams;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.freight.MallFreightDAO;
import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.dao.order.MallDaifuDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.page.MallPageDAO;
import com.gt.mall.dao.presale.MallPresaleDepositDAO;
import com.gt.mall.dao.presale.MallPresaleRankDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.groupbuy.MallGroupJoin;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.entity.presale.MallPresaleRank;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.CardService;
import com.gt.mall.service.inter.member.MemberPayService;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.user.SocketService;
import com.gt.mall.service.inter.wxshop.*;
import com.gt.mall.service.web.auction.MallAuctionBiddingService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.util.*;
import com.gt.util.entity.param.fenbiFlow.AdcServicesInfo;
import com.gt.util.entity.param.fenbiFlow.WsBusFlowInfo;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.param.sms.OldApiSms;
import com.gt.util.entity.param.wx.BusIdAndindustry;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
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
    private MallPageService mallPageService;

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
    private MallPresaleDepositDAO mallPresaleDepositDAO;

    @Autowired
    private MallPresaleRankDAO mallPresaleRankDAO;

    @Autowired
    private MallPresaleService mallPresaleService;

    @Autowired
    private MallOrderDetailDAO mallOrderDetailDAO;

    @Autowired
    private MallSellerMallsetService mallSellerMallsetService;

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
    private MallPageDAO mallPageDAO;

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
    private WxAppletService wxAppletService;

    @Autowired
    private SocketService socketService;

    @Autowired
    private FenBiFlowService fenBiFlowService;

    @Autowired
    private WxShopService wxShopService;

    @Override
    public PageUtil findByPage( Map< String,Object > params ) {
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1
			: CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	int status = 0;
	if ( CommonUtil.isNotEmpty( params.get( "status" ) ) ) {
	    status = CommonUtil.toInteger( params.get( "status" ) );
	}
	int rowCount = 0;
	if ( status != 6 && status != 7 && status != 8 && status != 9 ) {
	    rowCount = mallOrderDAO.count( params );
	} else {
	    rowCount = mallOrderDAO.countReturn( params );
	}
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ),
			pageSize, rowCount, "mallOrder/toIndex.do" );
	params.put( "firstResult", pageSize
			* ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	List< MallOrder > list = new ArrayList< MallOrder >();
	if ( status != 6 && status != 7 && status != 8 && status != 9 ) {
	    list = mallOrderDAO.findByPage( params );
	} else {
	    list = mallOrderDAO.findReturnByPage( params );
	}
	List< Integer > orderIdList = new ArrayList< Integer >();
	List< Integer > detailIdList = new ArrayList< Integer >();
	List< MallOrder > orderList = new ArrayList< MallOrder >();
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
		boolean flag = false;
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
	    List< MallOrderReturn > orderReturnList = new ArrayList< MallOrderReturn >();
	    //查询订单退款信息
	    if ( detailIdList != null && detailIdList.size() > 0 ) {
		Map< String,Object > detailParams = new HashMap< String,Object >();
		detailParams.put( "detailIdList", detailIdList );
		orderReturnList = mallOrderReturnDAO.selectByDetailIds( detailParams );
	    }
	    for ( MallOrder order : list ) {
		List< MallOrderDetail > detailList = new ArrayList< MallOrderDetail >();
		List< Integer > detailIds = new ArrayList< Integer >();
		boolean isDetail = false;
		if ( !isHaveDetail ) {
		    if ( orderdetailList != null && orderdetailList.size() > 0 ) {
			for ( int i = 0; i < orderdetailList.size(); i++ ) {
			    MallOrderDetail detail = orderdetailList.get( i );
			    if ( detail.getOrderId().toString().equals( order.getId().toString() ) ) {
				detailList.add( detail );
				detailIds.add( i );
				isDetail = true;
			    }
			}
			if ( detailIds != null && detailIds.size() > 0 ) {
			    order.setMallOrderDetail( detailList );
			    for ( Integer ids : detailIds ) {
				orderdetailList.remove( ids );
			    }
			}
		    }
		}
		if ( ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) || isDetail ) {
		    isDetail = true;
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			if ( CommonUtil.isNotEmpty( detail.getId() ) ) {
			    isDetail = true;
			    if ( detail.getStatus() != -3 ) {
				for ( int i = 0; i < orderReturnList.size(); i++ ) {
				    MallOrderReturn mallOrderReturn = orderReturnList.get( i );
				    if ( CommonUtil.toString( mallOrderReturn.getOrderDetailId() ).equals( CommonUtil.toString( detail.getId() ) ) &&
						    CommonUtil.toString( mallOrderReturn.getOrderId() ).equals( CommonUtil.toString( detail.getOrderId() ) ) ) {
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

		if ( isDetail || order.getOrderPayWay() == 5 ) {
		    orderList.add( order );
		}
	    }
	}
	page.setSubList( orderList );
	return page;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public int upOrderNoOrRemark( Map< String,Object > params ) {
	Member member = null;
	int count = mallOrderDAO.upOrderNoOrRemark( params );
	Object type = params.get( "type" );
	Object express = params.get( "express" );
	if ( null != type && type.equals( "2" ) ) {//取消订单
	    if ( count == 1 ) {
		params.put( "status", 5 );
		count = mallOrderDAO.upOrderNoOrRemark( params );
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
		memberService.updateUserConsume( consumeParams );

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
    public Map< String,Object > selectOrderList( Map< String,Object > params ) {
	Map< String,Object > orderMap = new HashMap<>();
	Map< String,Object > orders = mallOrderDAO.selectMapById( CommonUtil.toInteger( params.get( "orderId" ) ) );
	orders.put( "nickname", CommonUtil.blob2String( orders.get( "nickname" ) ) );//修改值
	List< Map< String,Object > > orderDetail = mallOrderDAO.selectOrderDetail( params );
	orderMap.put( "orderInfo", orders );
	orderMap.put( "orderDetail", orderDetail );

	if ( CommonUtil.isNotEmpty( orders ) ) {
	    if ( orders.get( "delivery_method" ).toString().equals( "2" ) && CommonUtil.isNotEmpty( orders.get( "take_their_id" ) ) ) {//配送方式是到店自提
		int takeId = CommonUtil.toInteger( orders.get( "take_their_id" ) );
		MallTakeTheir take = mallTakeTheirService.selectById( takeId );
		orderMap.put( "take", take );
	    }
	    if ( CommonUtil.isNotEmpty( orders.get( "express_id" ) ) ) {
		String expressName = dictService.getDictRuturnValue( "1092", CommonUtil.toInteger( orders.get( "express_id" ) ) );
		if ( CommonUtil.isNotEmpty( expressName ) ) {
		    orderMap.put( "expressName", expressName );
		}
	    }
	}

	return orderMap;
    }

    @Override
    public List< Map< String,Object > > selectShipAddress( Map< String,Object > params ) {
	List< Map< String,Object > > addressList = new ArrayList<>();
	//todo 调用陈丹接口，地址相关
	List< Map< String,Object > > list = null;// mallOrderDAO.selectShipAddress( params );
	int is_default = 2;
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > map : list ) {
		if ( CommonUtil.isNotEmpty( map.get( "mem_default" ) ) ) {
		    int memberDefault = CommonUtil.toInteger( map.get( "mem_default" ) );
		    if ( is_default == 2 ) {
			is_default = memberDefault;
		    } else if ( is_default == 1 && memberDefault == 1 ) {//如果用户拥有两个默认地址，修改第二个默认地址
			map.put( "mem_default", "2" );
			//			daoUtil.update( "UPDATE t_eat_member_address SET mem_default = 2 WHERE id =" + map.get( "id" ) );
		    }
		}
		addressList.add( map );
	    }
	}
	return addressList;
    }

    @Override
    public Map< String,Object > addOrder( Map< String,Object > params, HttpServletRequest request ) {
	MallOrder order = (MallOrder) JSONObject.toBean( JSONObject.fromObject( params.get( "order" ) ), MallOrder.class );
	int count = mallOrderDAO.insert( order );//添加order表的数据
	int orderId = order.getId();//获取orderId

	String obj = params.get( "detail" ).toString();
	boolean result = false;
	String message = "生成订单失败，请稍后重试。";
	StringBuilder ids = new StringBuilder();
	String type = params.get( "type" ).toString();
	double orderPrice = 0;
	if ( count > 0 && null != obj && !obj.equals( "" ) ) {
	    request.setAttribute( "couponDiscount", 0 );//清空优惠券的折扣数
	    request.setAttribute( "sumCouponNum", 0 );//清空优惠券的打折数
	    count = 0;
	    List< MallOrderDetail > orderDetail = addMallOrderDetail(/*map, */params, orderId, request );
	    /*jjj*/

	    /*if (type == "1" || type.equals("1")) {*///购物车添加订单详情
	    JSONArray msg = JSONArray.fromObject( obj );
	    for ( Object objMsg : msg ) {
		JSONObject jasonObject = JSONObject.fromObject( objMsg );
		if ( CommonUtil.isNotEmpty( ids ) ) {
		    ids.append( "," );
		}
		ids.append( jasonObject.get( "id" ) );
		/*double preferPrice = 0;
		if(i <= (msg.size()-1)){
			String coupon = d.getCouponCode();
			Integer jifen = d.getUseJifen();
			double fenbi = d.getUseFenbi();
			if((null != coupon && coupon.equals("")) || jifen > 0 || fenbi > 0){
				preferPrice += d.getDiscountedPrices().doubleValue();
			}
		}
		orderPrice += d.getTotalPrice();*/
		/*params.put("preferPrice", preferPrice);//所有商品总优惠钱的和
		orderDetail.add(d);//生成订单详情数据并添加*/
	    }
			/*}*/
	    if ( null != orderDetail && orderDetail.size() > 0 ) {
		if ( order.getOrderType() != 4 ) {
		    count = mallOrderDetailDAO.insertOrderDetail( orderDetail );
		} else {
		    count = mallOrderDetailDAO.insert( orderDetail.get( 0 ) );
		}
		for ( MallOrderDetail anOrderDetail : orderDetail ) {
		    orderPrice += anOrderDetail.getTotalPrice();
		}
	    }
	    double freightPrice = 0;
	    if ( CommonUtil.isNotEmpty( order.getOrderFreightMoney() ) ) {
		freightPrice = CommonUtil.toDouble( order.getOrderFreightMoney() );
	    }
	    //订单详情的总价不等于订单总价，修改订单价格
	    if ( orderPrice + freightPrice != CommonUtil.toDouble( order.getOrderMoney() ) ) {
		MallOrder o = new MallOrder();
		o.setId( order.getId() );
		o.setOrderMoney( CommonUtil.toBigDecimal( orderPrice + freightPrice ) );
		mallOrderDAO.upOrderNoById( o );
	    }
	    if ( count > 0 ) {
		//把商品信息从购物车里删除
		if ( type.equals( "1" ) ) {
		    mallPageService.shoppingdelect( ids.toString(), "", 0 );
		}
		result = true;
		message = "添加订单成功";
		if ( order.getOrderType() != 3 ) {
		    //订单生成成功，把订单加入到未支付的队列中
		    String key = "hOrder_nopay";
		    JSONObject objs = new JSONObject();
		    String times = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
		    objs.put( "times", times );
		    objs.put( "orderId", orderId );
		    JedisUtil.map( key, orderId + "", objs.toString() );
		}
		if ( order.getOrderType() == 4 ) {//拍卖
		    //加入拍卖竞拍
		    mallAuctionBiddingService.addBidding( order, orderDetail );
		}
		String unionKey = Constants.REDIS_KEY + "union_order_" + order.getId();
		if ( CommonUtil.isNotEmpty( params.get( "union_id" ) ) && CommonUtil.isNotEmpty( params.get( "cardId" ) ) ) {
		    JSONObject unionJson = new JSONObject();
		    unionJson.put( "union_id", params.get( "union_id" ) );
		    unionJson.put( "cardId", params.get( "cardId" ) );
		    unionJson.put( "orderId", params.get( "orderId" ) );
		    unionJson.put( "orderStatus", 1 );//未支付
		    JedisUtil.set( unionKey, unionJson.toString(), 60 * 60 );//以秒为单位，保存1个小时 60*60*1
		}
				/*try {
					Map s = new HashMap();
					s.put("id", order.getId());
					s.put("time", System.currentTimeMillis());
					String json = new Gson().toJson(s);
					MqUtil mq = new MqUtil();
					mq.MqMessage("gt.e.mall.conversion", "gt.e.mall",json);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (TimeoutException e) {
					e.printStackTrace();
				}*/

	    }
	}
	if ( null != params.get( "orderId" ) ) {
	    orderId = Integer.parseInt( params.get( "orderId" ).toString() );
	}
	Map< String,Object > resultMap = new HashMap<>();
	resultMap.put( "result", result );
	resultMap.put( "orderId", orderId );
	resultMap.put( "msg", message );
	return resultMap;
    }

    private List< MallOrderDetail > addMallOrderDetail( Map< String,Object > params, int orderId
		    , HttpServletRequest request ) {
	List< MallOrderDetail > orderDetail = new ArrayList<>();
	List< Map > detailList = com.alibaba.fastjson.JSONArray.parseArray( params.get( "detail" ).toString(), Map.class );
	if ( detailList != null && detailList.size() > 0 ) {
	    for ( Map< String,Object > detail : detailList ) {
		Object groupType = detail.get( "groupType" );
		Object proSpecStr = detail.get( "pro_spec_str" );
		String type = params.get( "type" ).toString();
		if ( null != groupType && groupType.equals( "7" ) && CommonUtil.isNotEmpty( proSpecStr ) ) {//判断是否是批发商品
		    Map str = JSONObject.fromObject( proSpecStr );
		    if ( type.equals( "1" ) ) {//购物车添加订单详情
			if ( CommonUtil.isNotEmpty( str ) ) {
			    for ( Object key : str.keySet() ) {
				Map p = JSONObject.fromObject( str.get( key ) );
				detail.put( "product_num", p.get( "num" ) );//商品的购买数量
				detail.put( "price", p.get( "price" ) );//商品购买价格
				detail.put( "primary_price", p.get( "price" ) );//商品原价
				detail.put( "product_specificas", key );//商品规格Id
				detail.put( "product_speciname", p.get( "value" ) );//商品规格对应名称
				orderDetail.add( addOrderDetails( params, request, orderId, detail ) );//批发商品添加订单详情数据
			    }
			}
		    } else {//立即批发添加订单详情
			String image_url2 = detail.get( "image_url" ).toString();
			String image_url = "";
			if ( image_url2.contains( "/image" ) ) {
			    image_url = image_url2.substring( image_url2.indexOf( "/image" ), image_url2.length() );
			} else if ( image_url2.contains( "/upload" ) ) {
			    image_url = image_url2.substring( image_url2.indexOf( "/upload" ) + 7, image_url2.length() );
			}
			/*jObj.put("product_num", jObj.get("totalnum"));*/
			detail.put( "zx_imageurl", image_url );
			if ( CommonUtil.isNotEmpty( str ) ) {
			    for ( Object key : str.keySet() ) {
				Map p = JSONObject.fromObject( str.get( key ) );
				detail.put( "product_num", p.get( "num" ) );//商品的购买数量
				detail.put( "price", p.get( "price" ) );//商品购买价格
				detail.put( "primary_price", p.get( "price" ) );//商品原价
				detail.put( "product_specificas", key );//商品规格Id
				detail.put( "product_speciname", p.get( "value" ) );//商品规格对应名称
								/*detail.put("pro_name", detail.get("product_name"));*/
				detail.put( "zx_imageurl", image_url );
				orderDetail.add( addOrderDetails( params, request, orderId, detail ) );//批发商品添加订单详情数据
			    }
			}
		    }
		} else {
		    if ( type.equals( "1" ) ) {//购物车添加订单详情
			orderDetail.add( addOrderDetails( params, request, orderId, detail ) );
		    } else {//立即购买添加订单详情
			String image_url2 = detail.get( "image_url" ).toString();
			String image_url = "";
			if ( image_url2.contains( "/image" ) ) {
			    image_url = image_url2.substring( image_url2.indexOf( "/image" ), image_url2.length() );
			} else if ( image_url2.contains( "/upload" ) ) {
			    image_url = image_url2.substring( image_url2.indexOf( "/upload" ) + 7, image_url2.length() );
			}

			detail.put( "zx_imageurl", image_url );
			orderDetail.add( addOrderDetails( params, request, orderId, detail ) );
		    }
		}
	    }
	}
	return orderDetail;
    }

    /**
     * 关闭未付款订单
     */
    @Override
    public void updateByNoMoney( Map< String,Object > params ) {
	/*if (params == null) {
		params = new HashMap<String, Object>();
	}
	params.put("minuteTime", 30);// 关闭订单的时间 （30分钟）
	params.put("orderTypes", 3);
	mallOrderDao.updateByNoMoney(params);*/
    }

    /**
     * 计算最后一件商品的优惠券，如果不是最后一件商品，则把总共优惠的价钱保存起来
     */
    private Double countLastPriceYhq( Map< String,Object > params, String shopId, double detProPrice, HttpServletRequest request, JSONObject sObj ) {
	Integer sumCouponNum = 0;//保存已经使用了优惠券的商品个数
	double price;
	price = detProPrice;
	//计算最后一个有优惠价的商品价格
	if ( null != params.get( "yhqNum" ) && !params.get( "yhqNum" ).equals( "" ) ) {
	    Object yhqNum = params.get( "yhqNum" );
	    JSONObject yhqNumList = JSONObject.fromObject( yhqNum );
	    if ( CommonUtil.isNotEmpty( yhqNumList.get( shopId ) ) ) {
		JSONObject obj = JSONObject.fromObject( yhqNumList.get( shopId ) );
		if ( obj.get( "shopId" ).toString().equals( shopId ) && CommonUtil.isNotEmpty( obj.get( "num" ) ) ) {
		    Integer yhqCount = CommonUtil.toInteger( obj.get( "num" ) );
		    if ( CommonUtil.isNotEmpty( request.getAttribute( "sumCouponNum" ) ) ) {
			//已经使用了优惠券的商品个数
			sumCouponNum = CommonUtil.toInteger( request.getAttribute( "sumCouponNum" ) );
		    }
		    //如果已经使用了优惠券的商品个数等于能使用商品的个数   ，则表示已经是最后一个能使用优惠券的商品
		    if ( sumCouponNum + 1 >= yhqCount && yhqCount > 0 ) {//已经是最后一个能使用优惠券的商品
			//						Object countYhq = params.get("countYhq");
			Object countYhq = sObj.get( "youhui" );
			Object couponDiscount = request.getAttribute( "couponDiscount" );//商品总共优惠的价钱
			if ( CommonUtil.isNotEmpty( couponDiscount ) && CommonUtil.isNotEmpty( countYhq ) ) {
			    Double preferPrice = CommonUtil.toDouble( couponDiscount );
			    Double totalYh = CommonUtil.toDouble( countYhq );
			    //							price = detProPrice - (totalYh-preferPrice);
			    Double priceCha = CommonUtil.subtract( totalYh, preferPrice );
			    price = CommonUtil.subtract( detProPrice, priceCha );
			    return price;
			}
		    } else {
			request.setAttribute( "sumCouponNum", sumCouponNum + 1 );
		    }
		}
	    }
	}
	return null;
    }

    @Override
    public MallOrder getOrderById( Integer orderId ) {
	return mallOrderDAO.getOrderById( orderId );
    }

    @Override
    public WxPublicUsers getWpUser( Integer memberId ) {
	return wxPublicUserService.selectByMemberId( memberId );
    }

    @Override
    public int paySuccessModified( Map< String,Object > params, Member member ) {
	logger.info( "商城支付完成后进入回调方法，接收参数：" + params );
	String orderNo = params.get( "out_trade_no" ).toString();
	MallOrder order = mallOrderDAO.selectOrderByOrderNo( orderNo );
	member = memberService.findMemberById( order.getBuyerUserId(), member );

	WxPublicUsers pbUser = wxPublicUserService.selectByMemberId( order.getBuyerUserId() );
	List< Map< String,Object > > erpList = new ArrayList<>();

	Map< String,Object > map = new HashMap<>();
	List< MallOrder > mallOrderList = mallOrderDAO.getOrderByOrderPId( order.getId() );
	if ( mallOrderList.size() > 0 ) {
	    for ( MallOrder mallOrder : mallOrderList ) {
		map = updateStatusStock( mallOrder, params, member, pbUser );
		params.put( "shopIds", map.get( "shopIds" ) );
		params.put( "fenbi", map.get( "fenbi" ) );
		params.put( "integral", map.get( "integral" ) );
		params.put( "wxCoupon", map.get( "wxCoupon" ) );
		params.put( "duofenCoupon", map.get( "duofenCoupon" ) );
		params.put( "wxCoupon2", map.get( "wxCoupon2" ) );
		params.put( "duofenCoupon2", map.get( "duofenCoupon2" ) );

		if ( CommonUtil.isNotEmpty( map.get( "erpMap" ) ) ) {
		    Map< String,Object > erpMap = (Map< String,Object >) JSONObject.fromObject( map.get( "erpMap" ) );
		    erpList.add( erpMap );
		}
	    }
	} else {
	    MallOrder mallOrder = mallOrderDAO.getOrderById( order.getId() );
	    map = updateStatusStock( order, params, member, pbUser );
	    if ( CommonUtil.isNotEmpty( map.get( "erpMap" ) ) ) {
		Map< String,Object > erpMap = (Map< String,Object >) JSONObject.fromObject( map.get( "erpMap" ) );
		erpList.add( erpMap );
	    }
	    mallOrderList = new ArrayList<>();
	    mallOrderList.add( mallOrder );
	}

	if ( erpList != null && erpList.size() > 0 ) {
	    Map< String,Object > erpMap = new HashMap<>();
	    erpMap.put( "orders", JSONArray.fromObject( erpList ) );
	    boolean flag = MallJxcHttpClientUtil.inventoryOperation( erpMap, true );
	    if ( !flag ) {
		//同步失败，信息放到redis
		JedisUtil.rPush( Constants.REDIS_KEY + "erp_inven", JSONObject.fromObject( erpMap ).toString() );
	    }
	    logger.info( "同步库存：" + flag );
	}
	if ( mallOrderList != null && mallOrderList.size() > 0 ) {
	    paySuccess( mallOrderList );//支付成功回调储值卡支付，积分支付，粉币支付
	}

	//PC 端订单推送
	String shopIds = map.get( "shopIds" ).toString();
	MallStore store = mallStoreDAO.selectById( shopIds );
	//todo 调用陈丹接口 根据brachId 查询商家id
	//	String sql = "SELECT distinct a.userid FROM bus_user_branch_relation a "
	//			+ "LEFT JOIN t_mall_store c ON a.branchid=c.sto_branch_id "
	//			+ "LEFT JOIN bus_user_menus d ON a.userid=d.userid "
	//			+ "WHERE c.id IN (" + shopIds + ") AND d.menus_id=49";
	List list = null;//daoUtil.queryForList( sql );
	String url = PropertiesUtil.getHomeUrl() + "mallOrder/indexstart.do";
	if ( null != list && list.size() > 0 ) {
	    for ( Object aList : list ) {
		JSONObject obj = JSONObject.fromObject( aList );
		int userId = Integer.parseInt( obj.get( "userid" ).toString() );
		try {
		    //todo 调用陈丹接口，socket相关
		    //socke.sendMessage( userId, url );   //订单成功推送消息
		} catch ( Exception e ) {
		    e.printStackTrace();
		    logger.error( "消息推送异常：" + e.getMessage() );
		}

	    }
	}
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

	//商家订单短信推送
	params.remove( "shopIds" );
	StringBuffer telePhone = new StringBuffer();
	String[] shopId = shopIds.split( "," );
	for ( int i = 0; i < shopId.length; i++ ) {
	    store = mallStoreDAO.selectById( Integer.parseInt( shopId[i] ) );
	    if ( store.getStoIsSms() == 1 ) {//1是推送
		if ( store.getStoSmsTelephone() != null ) {
		    if ( CommonUtil.isNotEmpty( telePhone ) ) {
			telePhone.append( "," );
		    }
		    telePhone.append( store.getStoSmsTelephone() );
		}
	    }
	}
	BusUser busUser = busUserService.selectById( member.getBusid() );
	if ( !telePhone.equals( "" ) ) {
	    OldApiSms oldApiSms = new OldApiSms();
	    oldApiSms.setMobiles( telePhone.toString() );
	    oldApiSms.setCompany( busUser.getMerchant_name() );
	    oldApiSms.setBusId( member.getBusid() );
	    oldApiSms.setModel( Constants.SMS_MODEL );
	    oldApiSms.setContent( CommonUtil.format( "你的商城有新的订单，请登录" + Constants.doMainName + "网站查看详情。" ) );
	    try {
		smsService.sendSmsOld( oldApiSms );
	    } catch ( Exception e ) {
		e.printStackTrace();
		logger.error( "短信推送消息异常：" + e.getMessage() );
	    }
	}

	if ( CommonUtil.isNotEmpty( pbUser ) ) {
	    try {
		sendMsg( order, 4 );//发送消息模板
	    } catch ( Exception e ) {
		e.printStackTrace();
		logger.error( "购买成功消息模板发送异常" + e.getMessage() );
	    }
	}
	try {
	    smsMessageTel( order, member, busUser );//短信提醒买家
	} catch ( Exception e ) {
	    e.printStackTrace();
	    logger.error( "短信提醒买家失败异常" + e.getMessage() );
	}

	return Integer.parseInt( map.get( "count" ).toString() );
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
     * 储值卡支付，积分支付，粉币支付
     *
     * @param mallOrderList 订单集合
     */
    private void paySuccess( List< MallOrder > mallOrderList ) {
	MallOrder order = mallOrderList.get( 0 );
	double discountMoney = 0;//折扣后的金额
	boolean useCoupon = false;//是否使用优惠券
	String codes = "";//优惠券code
	int couponType = -1;//优惠券类型  0 微信  1多粉
	boolean isFenbi = false;//是否使用粉币
	boolean isJifen = false;//是否使用积分
	double fenbiNum = 0;//使用粉币数量
	double jifenNum = 0;//使用积分数量

	for ( MallOrder mallOrder : mallOrderList ) {
	    if ( mallOrder.getMallOrderDetail() != null ) {
		for ( MallOrderDetail orderDetail : mallOrder.getMallOrderDetail() ) {
		    discountMoney += orderDetail.getTotalPrice();
		    //优惠券code
		    if ( CommonUtil.isNotEmpty( orderDetail.getCouponCode() ) ) {
			useCoupon = true;
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
	    int orderPayWay = order.getOrderPayWay();
	    if ( orderPayWay == 4 ) {//积分支付
		isJifen = true;
		jifenNum += CommonUtil.toDouble( order.getOrderMoney() );
	    } else if ( orderPayWay == 8 ) {//粉币支付
		isFenbi = true;
		fenbiNum += CommonUtil.toDouble( order.getOrderMoney() );
	    }
	}
	PaySuccessBo successBo = new PaySuccessBo();
	successBo.setMemberId( order.getBuyerUserId() );//会员id
	successBo.setOrderCode( order.getOrderNo() );//订单号
	successBo.setTotalMoney( CommonUtil.toDouble( order.getOrderOldMoney() ) );//订单原价
	successBo.setDiscountMoney( discountMoney );//折扣后的价格（不包含运费）
	successBo.setPay( CommonUtil.toDouble( order.getOrderMoney() ) );
	successBo.setPayType( CommonUtil.getMemberPayType( order.getOrderPayWay(), order.getIsWallet() ) );////支付方式 查询看字典1198
	successBo.setUcType( CommonUtil.getMemberUcType( order.getOrderType() ) );//消费类型 字典1197
	if ( useCoupon ) {
	    successBo.setUseCoupon( useCoupon );//是否使用优惠券
	    //todo couponType  优惠券类型 以后会改
	    successBo.setCouponType( couponType );//优惠券类型 0微信 1多粉优惠券
	    successBo.setCodes( codes );//使用优惠券code值 用来核销卡券
	}
	if ( isFenbi ) {
	    successBo.setUserFenbi( isFenbi );
	    successBo.setFenbiNum( fenbiNum );
	}
	if ( isJifen ) {
	    successBo.setUserJifen( isJifen );
	    successBo.setJifenNum( CommonUtil.toIntegerByDouble( jifenNum ) );
	}
	successBo.setDelay( 0 ); //会员赠送物品 0延迟送 1立即送  -1 不赠送物品
	successBo.setDataSource( order.getBuyerUserType() );
	successBo.setUcTable( "t_mall_order" );
	Map< String,Object > payMap = memberPayService.paySuccess( successBo );
	if ( CommonUtil.isNotEmpty( payMap ) ) {
	    if ( CommonUtil.toInteger( payMap.get( "code" ) ) == -1 ) {
		throw new BusinessException( CommonUtil.toInteger( payMap.get( "code" ) ), CommonUtil.toString( payMap.get( "errorMsg" ) ) );
	    }
	}
    }

    /**
     * 短信提醒买家
     *
     * @param order
     */
    public void smsMessageTel( MallOrder order, Member member, BusUser busUser ) {
	String messages = "支付成功，请您耐心等待，我们将稍后为您发货";
	if ( CommonUtil.isNotEmpty( order.getReceiveId() ) ) {
	    // String sql = "select mem_phone from t_eat_member_address where id = " + order.getReceiveId();
	    //todo 调用陈丹的接口 根据地址id查询地址信息
	    Map< String,Object > map = null;//daoUtil.queryForMap( sql );
	    if ( CommonUtil.isNotEmpty( map ) ) {
		if ( CommonUtil.isNotEmpty( map.get( "mem_phone" ) ) ) {
		    String telephone = map.get( "mem_phone" ).toString();
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

    }

    /**
     * 修改库存
     */
    private Map updateStatusStock( MallOrder order, Map< String,Object > params, Member member, WxPublicUsers wxPublicUser ) {

	Map< String,Object > map = new HashMap();
	String shopIds = order.getShopId() + ",";
	if ( null != params.get( "shopIds" ) && !params.get( "shopIds" ).equals( "" ) ) {
	    shopIds += params.get( "shopIds" ) + ",";
	}
	if ( order.getOrderType() == 6 ) {
	    mallPresaleService.diffInvNum( order );
	}
	BusUser user = busUserService.selectById( order.getBusUserId() );//根据商家id查询商家信息
	int userPId = busUserService.getMainBusId( order.getBusUserId() );//查询商家总账号
	long isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
	double fenbi = 0;//粉币
	double integral = 0;//积分
	int proTypeId = 0;
	int memberType = 0;
	int flowId = 0;//流量id
	int flowRecordId = 0;//流量冻结id
	int totalNum = 0;
	//扫码支付和找人代付不修改库存
	if ( CommonUtil.isNotEmpty( order.getMallOrderDetail() ) && order.getOrderPayWay() != 5 && order.getOrderPayWay() != 7 ) {
	    List< MallOrderDetail > orderDetail = order.getMallOrderDetail();
	    if ( order.getOrderType() == 1 ) {//拼团
		addGroupBuyJoin( order, orderDetail.get( 0 ) );//添加数据到参团表
	    }
	    params.put( "payTime", new Date() );
	    params.put( "out_trade_no", order.getOrderNo() );
	    if ( orderDetail.size() == 1 ) {
		if ( CommonUtil.isNotEmpty( orderDetail.get( 0 ).getProTypeId() ) ) {
		    if ( orderDetail.get( 0 ).getProTypeId() > 0 ) {
			params.put( "status", 4 );//虚拟物品无需发货，状态改为4  订单完成
			proTypeId = orderDetail.get( 0 ).getProTypeId();//虚拟物品会员卡
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( order.getDeliveryMethod() ) ) {
		if ( order.getDeliveryMethod() == 3 ) {
		    params.put( "status", 4 );
		}
	    }
	    int count = 0;
	    if ( CommonUtil.isNotEmpty( order ) ) {//找人代付不更改状态
		if ( order.getOrderStatus() == 1 ) {
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
	    if ( count > 0 && null != orderDetail && CommonUtil.isNotEmpty( orderDetail ) && order.getOrderType() != 3 ) {//秒杀不立马修改库存，而是经过redis
		count = 0;
		if ( order.getOrderType() == 4 ) {
		    mallAuctionBiddingService.upStateBidding( order, 1, orderDetail );
		}
		for ( MallOrderDetail detail : orderDetail ) {
		    Map< String,Object > proMap = new HashMap<>();
		    totalNum += detail.getDetProNum();
		    fenbi += detail.getUseFenbi();
		    integral += detail.getUseJifen();
		    if ( CommonUtil.isNotEmpty( detail.getDiscount() ) ) {
			if ( detail.getDiscount() < 100 && detail.getDiscount() > 0 ) {
			    discount = detail.getDiscount();
			}
		    }
		    flowId = detail.getFlowId();
		    flowRecordId = detail.getFlowRecordId();
		    //map.put( "flowId", detail.getFlowId() );
		    //map.put("flowRecordId", detail.getFlowRecordId());

		    MallProduct pro = mallProductDAO.selectById( detail.getProductId() );
		    if ( isJxc == 0 || !pro.getProTypeId().toString().equals( "0" ) ) {
			memberType = pro.getMemberType();
			Integer total = ( pro.getProStockTotal() - detail.getDetProNum() );
			Integer saleNum = ( pro.getProSaleTotal() + detail.getDetProNum() );
			proMap.put( "total", total );
			proMap.put( "saleNum", saleNum );
			proMap.put( "proId", detail.getProductId() );
			MallProduct mallProduct = new MallProduct();
			mallProduct.setId( detail.getProductId() );
			mallProduct.setProStockTotal( total );
			mallProduct.setProSaleTotal( saleNum );
			count = mallProductDAO.updateById( mallProduct );//修改商品库存
			if ( null != pro.getIsSpecifica() && CommonUtil.isNotEmpty( pro.getIsSpecifica() ) ) {
			    if ( pro.getIsSpecifica() == 1 ) {//该商品存在规格
				String[] specifica = ( detail.getProductSpecificas() ).split( "," );
				StringBuilder ids = new StringBuilder( "0" );
				for ( String aSpecifica : specifica ) {
				    if ( CommonUtil.isNotEmpty( aSpecifica ) ) {
					proMap.put( "valueId", aSpecifica );
					MallProductSpecifica proSpec = mallProductSpecificaService.selectByNameValueId( proMap );
					ids.append( "," ).append( proSpec.getId() );
				    }
				}
				proMap.put( "specificaIds", ids.substring( 2, ids.length() ) );
				MallProductInventory proInv = mallProductInventoryService.selectInvNumByProId( proMap );//根据商品规格id查询商品库存
				total = ( proInv.getInvNum() - detail.getDetProNum() );
				String invSaleNum = "";
				if ( !CommonUtil.isEmpty( proInv.getInvSaleNum() ) ) {
				    invSaleNum = ( proInv.getInvSaleNum() ).toString();
				}
				if ( null == invSaleNum || invSaleNum.equals( "" ) ) {
				    invSaleNum = "0";
				}
				saleNum = ( Integer.parseInt( invSaleNum ) + detail.getDetProNum() );
				proMap.put( "total", total );
				proMap.put( "saleNum", saleNum );
				mallProductInventoryService.updateProductInventory( proMap );//修改规格的库存
			    }
			}
		    }
		    if ( detail.getProTypeId() == 3 && order.getOrderPayWay() != 7 ) { // 卡券购买发布卡券

			fenCard( order );//分配卡券

			String key = Constants.REDIS_KEY + "card_receive_num";
			JedisUtil.map( key, order.getId().toString(), detail.getCardReceiveId().toString() );

		    }
		}
	    }
	    String newOrderNo = order.getOrderNo();
	    if ( flowId > 0 && flowRecordId > 0 ) {
		String ordernos = flowPhoneChong( flowId, flowRecordId, member, wxPublicUser, order );
		if ( CommonUtil.isNotEmpty( ordernos ) ) {
		    newOrderNo = ordernos;
		}
	    }
	    if ( order.getOrderType() == 3 ) {
		//把要修改的库存丢到redis里
		mallSeckillService.addInvNumRedis( order, orderDetail );
	    } else {
		//订单生成成功，把订单加入到未支付的队列中
		String key = "hOrder_nopay";
		if ( JedisUtil.hExists( key, order.getId().toString() ) ) {
		    JedisUtil.mapdel( key, order.getId().toString() );
		}
	    }

	    /*if ( proTypeId == 2 ) {
		memberPayService.buyCard( member, order.getOrderMoney(), memberType );//会员卡购买不做记录，暂时跳到会员购买
	    }*/
	    if ( order.getOrderType() == 6 && count > 0 ) {//预售商品
		MallPresaleDeposit preDeposit = new MallPresaleDeposit();
		preDeposit.setPresaleId( order.getGroupBuyId() );
		preDeposit.setIsSubmit( 0 );
		preDeposit.setDepositStatus( 1 );
		preDeposit.setUserId( order.getBuyerUserId() );
		MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByDeposit( preDeposit );
		if ( deposit != null ) {
		    MallPresaleDeposit presaleDeposit = new MallPresaleDeposit();
		    presaleDeposit.setId( deposit.getId() );
		    presaleDeposit.setIsSubmit( 1 );
		    if ( CommonUtil.isNotEmpty( order.getOrderPayNo() ) ) {
			presaleDeposit.setPayNo( order.getOrderPayNo() );
		    }
		    //修改预售定金的状态
		    mallPresaleDepositDAO.updateById( presaleDeposit );

		    MallPresaleRank rank = new MallPresaleRank();
		    rank.setDepositId( deposit.getId() );
		    rank.setPresaleId( deposit.getPresaleId() );
		    rank.setMemberId( order.getBuyerUserId() );
		    MallPresaleRank presaleRank = mallPresaleRankDAO.selectByPresale( rank );
		    if ( CommonUtil.isNotEmpty( presaleRank ) ) {
			rank.setId( presaleRank.getId() );
			rank.setOrderId( order.getId() );
			//修改排名信息
			mallPresaleRankDAO.updateById( rank );
		    }
		}
	    }
	    map.put( "count", count );

	} else if ( order.getOrderPayWay() == 5 ) {//扫码支付直接来修改订单信息
	    params.put( "payTime", new Date() );
	    params.put( "out_trade_no", order.getOrderNo() );
	    params.put( "status", 2 );
	    int count = 0;
	    if ( CommonUtil.isNotEmpty( order ) ) {
		count = mallOrderDAO.upOrderByorderNo( params );
	    }
	    map.put( "count", count );
	} else if ( order.getOrderPayWay() == 7 ) {//找人代付
	    map.put( "count", 1 );
	}
	if ( null != params.get( "fenbi" ) && !params.get( "fenbi" ).toString().equals( "" ) ) {//获取粉币
	    fenbi += Double.parseDouble( params.get( "fenbi" ).toString() );
	}
	if ( null != params.get( "integral" ) && !params.get( "integral" ).toString().equals( "" ) ) {//获取积分
	    integral += Double.parseDouble( params.get( "integral" ).toString() );
	}
	MallOrder orders = mallOrderDAO.selectById( order.getId() );

	int uType = 1;//用户类型 1总账号  0子账号
	if ( !orders.getBusUserId().toString().equals( CommonUtil.toString( userPId ) ) ) {
	    uType = 0;
	}
	MallStore store = mallStoreDAO.selectById( orders.getShopId() );
	Map< String,Object > erpMap = new HashMap<>();
	erpMap.put( "uId", orders.getBusUserId() );
	erpMap.put( "uType", uType );//用户类型
	erpMap.put( "uName", user.getName() );
	erpMap.put( "rootUid", userPId );
	erpMap.put( "type", 0 );//下单
	erpMap.put( "remark", "商城下单" );
	erpMap.put( "shopId", store.getWxShopId() );

	List< Map< String,Object > > productList = new ArrayList<>();

	if ( CommonUtil.isNotEmpty( orders ) ) {
	    if ( orders.getOrderStatus() != 1 ) {
		double freightMoney = 0;
		//int saleMemberId = 0;
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
			    Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( detail.getProductSpecificas(), detail.getProductId() );
			    if ( CommonUtil.isNotEmpty( invMap ) ) {
				if ( CommonUtil.isNotEmpty( invMap.get( "erp_inv_id" ) ) ) {
				    erpInvId = CommonUtil.toInteger( invMap.get( "erp_inv_id" ) );
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
	//微信联盟核销
	/*String unionKey =  Constants.REDIS_KEY+"union_order_" + order.getId();
	if ( JedisUtil.exists( unionKey ) ) {
	    String values = JedisUtil.get( unionKey );
	    if ( CommonUtil.isNotEmpty( values ) ) {
		JSONObject unionObj = JSONObject.fromObject( values );
		int orderType = order.getOrderPayWay();
		if ( order.getOrderPayWay() == 9 ) {//支付宝
		    orderType = 2;
		} else if ( order.getOrderPayWay() == 2 ) {//货到付款
		    orderType = 3;
		} else if ( order.getOrderPayWay() == 6 ) {//到店支付
		    orderType = 0;
		}
		double totalMoney = 0;//商品总价
		if ( order.getMallOrderDetail() != null ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			double totalPrice = 0;
			double discountPrice = 0;
			if ( CommonUtil.isNotEmpty( detail.getTotalPrice() ) ) {
			    totalPrice = detail.getTotalPrice();
			}
			if ( CommonUtil.isNotEmpty( detail.getDiscountedPrices() ) ) {
			    discountPrice = CommonUtil.toDouble( detail.getDiscountedPrices() );
			}
			totalMoney += totalPrice + discountPrice;
		    }
		}
		if ( totalMoney > 0 ) {
		    totalMoney = CommonUtil.toDouble( new DecimalFormat( "######0.00" ).format( totalMoney ) );
		} else {
		    totalMoney = CommonUtil.toDouble( order.getOrderOldMoney() );
		}
		Map< String,Object > unionMap = new HashMap< String,Object >();
		unionMap.put( "busId", member.getBusid() );//消费的商家id
		unionMap.put( "modelDesc", "商城下单" );//行业消费描述
		unionMap.put( "orderNo", order.getOrderNo() );//订单号
		unionMap.put( "orderType", orderType );//支付类型：（0：到店支付 1：微信 2：支付宝 3：货到付款）
		unionMap.put( "money", order.getOrderMoney() );//支付金额
		unionMap.put( "wxOrderNo", order.getOrderPayNo() );//如是微信支付 微信订单号
		unionMap.put( "status", 1 );//支付状态 （0：未支付 1：已支付 2：已退款）
		unionMap.put( "unionId", unionObj.get( "union_id" ) );//联盟id
		unionMap.put( "cardId", unionObj.get( "cardId" ) );//联盟卡id
		unionMap.put( "model", 1 );//行业模型
		unionMap.put( "totalMoney", totalMoney );//商品价格
		unionMap.put( "orderId", order.getId() );//订单id
		unionMap.put( "isGiveIntegralNow", 0 );//是否可支付后立即赠送积分 1：是 0：否
		try {
		    //todo 调用联盟卡核销
		    unionMobileService.saveUnionOnlineComsumeRecord( unionMap );
		} catch ( Exception e ) {
		    logger.error( "商家联盟线上核销异常：" + e.getMessage() );
		    e.printStackTrace();
		}
	    }
	}*/
	if ( productList != null && productList.size() > 0 && isJxc == 1 ) {
	    erpMap.put( "products", productList );
	    map.put( "erpMap", JSONObject.fromObject( erpMap ) );
	}

	map.put( "shopIds", shopIds.substring( 0, shopIds.length() - 1 ) );
	map.put( "fenbi", fenbi );
	map.put( "integral", integral );
	map.put( "totalNum", totalNum );
	return map;
    }

    private String flowPhoneChong( int flowId, int flowRecordId, Member member, WxPublicUsers pbUser, MallOrder orders ) {
	if ( CommonUtil.isNotEmpty( orders.getFlowPhone() ) && flowId > 0 ) {
	    WsBusFlowInfo flow = fenBiFlowService.getFlowInfoById( flowId );
	    try {
		AdcServicesInfo adcServicesInfo = new AdcServicesInfo();
		adcServicesInfo.setModel( 101 );//模块ID
		adcServicesInfo.setMobile( orders.getFlowPhone() );//充值手机号
		adcServicesInfo.setPrizeCount( flow.getType() );//流量数
		adcServicesInfo.setMemberId( member.getId().toString() );//用户Id
		adcServicesInfo.setPublicId( pbUser.getId() );//商家公众号Id
		adcServicesInfo.setBusId( orders.getBusUserId() ); //根据平台用户Id获取微信订阅号用户信息
		adcServicesInfo.setId( orders.getId() );//订单id
		boolean isFlow = fenBiFlowService.adcServices( adcServicesInfo );//流量充值
		if ( isFlow ) {//充值成功
		    //todo 流量充值成功后要修改
		    String sql = "UPDATE t_wx_fenbi_flow_record t SET t.rec_use_count = t.rec_use_count + 1 WHERE id=" + flowRecordId;
		    int count = 0;//daoUtil.update( sql );
		    if ( count > 0 ) {
			MallOrder order = new MallOrder();
			order.setId( orders.getId() );
			order.setFlowRechargeStatus( 1 );
			mallOrderDAO.upOrderNoById( order );

			//todo
			//修改记录
			//			if ( CommonUtil.isNotEmpty( flowMap.get( "orderNo" ) ) ) {
			//			    return flowMap.get( "orderNo" ).toString();
			//			}
		    }
		}
		//		logger.info( "流量充值返回参数：" + flowMap );
	    } catch ( Exception e ) {
		logger.error( "流量充值异常" + e.getMessage() );
		e.printStackTrace();
	    }
	}
	return null;
    }

    /**
     * 订单成功添加数据到参团表
     */
    private void addGroupBuyJoin( MallOrder order, MallOrderDetail orderDetail ) {
	MallGroupJoin groupJoin = new MallGroupJoin();
	groupJoin.setGroupBuyId( order.getGroupBuyId() );
	groupJoin.setJoinTime( new Date() );
	groupJoin.setJoinUserId( order.getBuyerUserId() );
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

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public List< Map< String,Object > > mobileOrderList( Map< String,Object > params, int busUserId ) throws Exception {
	List< Map< String,Object > > orderList = new ArrayList<>();
	/*int pageSize = 10;
	int countOrder = mallOrderDao.countMobileOrderList(params);
	int curPage = CommonUtil.isEmpty(params.get("curPage")) ? 1 : CommonUtil
			.toInteger(params.get("curPage"));
	params.put("curPage", curPage);

	Page page = new Page(curPage, pageSize, countOrder, "phoneOrder/79B4DE7C/orderList.do");
	int firstNum = pageSize
			* ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);
	params.put("firstNum", firstNum);// 起始页
	params.put("maxNum", pageSize);// 每页显示商品的数量 */
	params.put( "busUserId", busUserId );
	List list = mallOrderDAO.mobileOrderList( params );
	if ( list != null && list.size() > 0 ) {
	    for ( Object obj : list ) {
		JSONObject orderMap = JSONObject.fromObject( obj );
		SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String orderType = CommonUtil.toString( orderMap.get( "orderType" ) );
		String orderPayWay = CommonUtil.toString( orderMap.get( "orderPayWay" ) );
		String orderStatus = CommonUtil.toString( orderMap.get( "orderStatus" ) );
		String deliveryMethod = CommonUtil.toString( orderMap.get( "deliveryMethod" ) );
		JSONArray detailArr = new JSONArray();
		JSONArray arr = JSONArray.fromObject( orderMap.get( "mallOrderDetail" ) );
		int isEndReturn = 0;//是否还没退款或己结束退款
		MallGroupBuy buy = null;
		if ( arr != null && orderType.equals( "1" ) ) {
		    buy = mallGroupBuyDAO.selectById( CommonUtil.toInteger( orderMap.get( "groupBuyId" ) ) );
		}
		for ( Object dObject2 : arr ) {
		    JSONObject dObj = JSONObject.fromObject( dObject2 );
		    int productId = dObj.getInt( "productId" );
		    int shopId = dObj.getInt( "shopId" );
		    String proTypeId = CommonUtil.toString( dObj.get( "proTypeId" ) );
		    int cardReceiveId = 0;
		    int groupIsReturn = 0;
		    if ( CommonUtil.isNotEmpty( dObj.get( "cardReceiveId" ) ) ) {
			cardReceiveId = CommonUtil.toInteger( dObj.get( "cardReceiveId" ) );
		    }
		    if ( buy != null ) {
			params.put( "orderId", orderMap.get( "id" ) );
			params.put( "orderDetailId", dObj.get( "id" ) );
			params.put( "groupBuyId", buy.getId() );
			//查询是否已成团
			Map< String,Object > joinMap = mallOrderDAO.groupJoinPeopleNum( params );
			if ( joinMap != null ) {
			    int count = CommonUtil.toInteger( joinMap.get( "num" ) );
			    //团购凑齐人允许退款
			    if ( count >= buy.getGPeopleNum() ) {
				groupIsReturn = 0;
			    } else {//拼团人数没达到不允许退款
				groupIsReturn = 1;
			    }
			}
		    }
		    String url = "/mallPage/" + productId + "/" + shopId + "/79B4DE7C/phoneProduct.do?uId=" + busUserId;
		    String proUnit = "";
		    if ( orderPayWay.equals( "4" ) ) {//积分兑换商品
			url = "/phoneIntegral/79B4DE7C/integralProduct.do?id=" + productId + "&shopId=" + shopId + "&uId=" + busUserId;
			proUnit = "积分";
		    } else if ( orderPayWay.equals( "8" ) ) {//粉币兑换商品
			url = "/mallPage/" + productId + "/" + shopId + "/79B4DE7C/phoneProduct.do?rType=2&uId=" + busUserId;
			proUnit = "粉币";
		    }
		    if ( orderType.equals( "4" ) ) {// 拍卖的商品，直接跳转到拍卖详情
			url = "/mAuction/" + productId + "/" + shopId + "/" + orderMap.get( "groupBuyId" ) + "/79B4DE7C/auctiondetail.do?uId=" + busUserId;
		    } else if ( CommonUtil.isNotEmpty( dObj.get( "saleMemberId" ) ) && !orderType.equals( "4" ) ) {
			int saleMemberId = CommonUtil.toInteger( dObj.get( "saleMemberId" ) );
			if ( saleMemberId > 0 ) {
			    url += "&saleMemberId=" + saleMemberId;
			}
		    }
		    int status = -3;
		    String statusMsg = "";
		    int isReturn = 1;//是否可以退款  1可以退款  0 不可以退款
		    if ( CommonUtil.isNotEmpty( dObj.get( "status" ) ) ) {
			status = CommonUtil.toInteger( dObj.get( "status" ) );
		    }
		    if ( status == 0 ) {
			statusMsg = "退款中";
		    } else if ( status == 1 ) {
			statusMsg = "退款成功";
		    } else if ( status == 5 ) {
			statusMsg = "退款退货成功";
		    } else if ( status == -1 ) {
			statusMsg = "卖家不同意退款";
		    } else if ( status == -2 ) {
			statusMsg = "退款已撤销";
		    } else if ( status == 2 ) {
			statusMsg = "商家已同意退款退货,请退货给商家";
		    } else if ( status == 3 ) {
			statusMsg = "已退货等待商家确认收货";
		    } else if ( status == 4 ) {
			statusMsg = "商家未收到货，不同意退款申请";
		    } else if ( status == 0 ) {
			statusMsg = "退款中";
		    }
		    if ( status != 1 && status != 5 && status != -2 && status != -3 ) {
			isEndReturn = 1;
		    }
					/*if(orderStatus.equals("2") && !orderPayWay.equals("2") && !orderPayWay.equals("6")){
						isReturn = 0;
					}
					if((orderStatus.equals("2") && deliveryMethod.equals("2")) || deliveryMethod.equals("1")){
						isReturn = 1;
					}*/
		    if ( orderStatus.equals( "5" ) || orderPayWay.equals( "5" ) || orderPayWay.equals( "4" ) || orderPayWay.equals( "8" ) ) {//扫码支付、积分支付、粉币支付  不可以退款
			isReturn = 0;
		    }
		    if ( ( proTypeId.equals( "3" ) && cardReceiveId > 0 ) || proTypeId.equals( "2" ) || proTypeId.equals( "4" ) ) {//卡券购买,会员卡支付 ,流量 不可以退款
			isReturn = 0;
		    }
		    int updateDay = 0;
		    if ( CommonUtil.isEmpty( orderMap.get( "updateDay" ) ) ) {
			updateDay = CommonUtil.toInteger( orderMap.get( "updateDay" ) );
		    }
		    int returnDay = CommonUtil.toInteger( dObj.get( "returnDay" ) );
		    if ( isReturn == 1 && groupIsReturn == 0 ) {
			if ( ( ( orderStatus.equals( "2" ) || orderStatus.equals( "3" ) ) && status == -3 ) || ( updateDay > 0 && updateDay < returnDay && orderStatus.equals( "4" )
					&& status == -3 ) ) {
			    dObj.put( "isReturnButton", 1 );//展示申请退款的按钮
			}
			if ( status == -1 ) {
			    dObj.put( "isUpdateButton", 1 );//展示修改退款的按钮
			}
			if ( ( status == 0 || status == 2 || status == 3 || status == 4 || status == -1 ) && CommonUtil.isNotEmpty( dObj.get( "orderReturn" ) ) ) {
			    dObj.put( "isCancelButton", 1 );//展示撤销退款的按钮
			    if ( status == 2 || status == 4 ) {
				dObj.put( "isWuliuButton", 1 );//展示填写物流的按钮
			    }
			}
		    }
		    dObj.put( "isReturn", isReturn );
		    dObj.put( "statusMsg", statusMsg );
		    dObj.put( "proUnit", proUnit );
		    dObj.put( "proUrl", url );
		    if ( dObj.getInt( "id" ) > 0 ) {
			detailArr.add( dObj );
		    }
		}
				/*if(detailArr == null || detailArr.size() == 0){
					detailArr.addAll(arr);
				}*/
		String statusName = "";
		if ( orderStatus.equals( "1" ) ) {
		    statusName = "待支付";
		} else if ( orderStatus.equals( "2" ) && deliveryMethod.equals( "1" ) ) {
		    statusName = "待发货";
		} else if ( orderStatus.equals( "2" ) && deliveryMethod.equals( "2" ) ) {
		    statusName = "待提货";
		} else if ( orderStatus.equals( "3" ) ) {
		    statusName = "待收货";
		} else if ( orderStatus.equals( "4" ) ) {
		    statusName = "订单已完成";
		} else if ( orderStatus.equals( "5" ) ) {
		    statusName = "订单已关闭";
		}
		if ( orderPayWay.equals( "7" ) ) {
		    int orderPId = CommonUtil.toInteger( orderMap.get( "id" ) );
		    if ( CommonUtil.isNotEmpty( orderMap.get( "orderPid" ) ) ) {
			int orderPid = CommonUtil.toInteger( orderMap.get( "orderPid" ) );
			if ( orderPid > 0 ) {
			    orderPId = orderPid;
			}
		    }
		    String daifuUrl = "/phoneOrder/" + orderPId + "/79B4DE7C/getDaiFu.do?uId=" + busUserId;
		    orderMap.put( "daifuUrl", daifuUrl );
		}
		orderMap.put( "statusName", statusName );
		orderMap.put( "isShouHuo", isEndReturn );
		orderMap.put( "mallOrderDetail", detailArr );

		JSONObject dateObj = (JSONObject) orderMap.get( "createTime" );
		Date date = new Date( dateObj.getLong( "time" ) );
		orderMap.put( "createTime", format.format( date ) );
		if ( ( detailArr != null && detailArr.size() > 0 ) || orderPayWay.equals( "5" ) ) {
		    orderList.add( orderMap );
		}
	    }
	}
	return orderList;
    }

    /**
     * 申请订单退款
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public boolean addOrderReturn( MallOrderReturn orderReturn ) {
	if ( orderReturn != null ) {
	    // 新增订单退款
	    int num;
	    MallOrderReturn oReturn = mallOrderReturnDAO.selectByOrderDetailId( orderReturn );
	    if ( oReturn != null ) {
		orderReturn.setId( oReturn.getId() );
	    }
	    if ( CommonUtil.isNotEmpty( orderReturn.getOrderDetailId() ) ) {
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
	    if ( CommonUtil.isEmpty( orderReturn.getId() ) ) {
		orderReturn.setCreateTime( new Date() );
		String orderNo = "TK" + System.currentTimeMillis();
		orderReturn.setReturnNo( orderNo );
		num = mallOrderReturnDAO.insert( orderReturn );
	    } else {
		orderReturn.setUpdateTime( new Date() );
		num = mallOrderReturnDAO.updateById( orderReturn );
	    }
	    if ( num > 0 ) {
		// 修改订单详情的状态
		MallOrderDetail detail = new MallOrderDetail();
		detail.setId( orderReturn.getOrderDetailId() );
		detail.setStatus( orderReturn.getStatus() );
		num = mallOrderDetailDAO.updateById( detail );

		if ( num > 0 ) {
		    return true;
		}
	    }

	}
	return false;
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
    public Map< String,Object > updateOrderReturn( MallOrderReturn orderReturn, Object oObj, WxPublicUsers pUser )
		    throws Exception {
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
			MallOrder order = mallOrderDAO.selectById( oReturn.getOrderId() );

			boolean flags = true;

			if ( CommonUtil.isNotEmpty( oReturn.getReturnFenbi() ) ) {
			    BusUser busUser = busUserService.selectById( order.getBusUserId() );//根据商家id查询商家信息
			    if ( busUser.getFansCurrency() - oReturn.getReturnFenbi() < 0 ) {
				flags = false;
				msg = "您的粉币不足，请重新充值再退给买家";
			    }
			}

			if ( order != null && flags ) {
			    if ( ( order.getOrderPayWay() == 1 || order.getIsWallet() == 1 ) && CommonUtil.isNotEmpty( pUser ) ) {//微信退款
				if ( Double.parseDouble( oReturn.getRetMoney().toString() ) > 0 ) {//退款金额大于0，则进行微信退款
				    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( payMap.get( "orderNo" ).toString() );
				    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
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
					    if ( resultmap.get( "code" ).toString().equals( "1" ) ) {
						//退款成功修改退款状态
						updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

						rFlag = true;
						msg = "退款成功";
					    }
					}
				    } else if ( wxPayOrder.getTradeState().equals( "NOTPAY" ) ) {
					msg = "订单：" + wxPayOrder.getOutTradeNo() + "未支付";
				    }
				} else {//退款金额等于0，则直接修改退款状态
				    //退款成功修改退款状态
				    updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款
				    rFlag = true;
				    msg = "退款成功";
				}
			    } else if ( order.getOrderPayWay() == 3 ) {//储值卡退款
				//Double retMoney = CommonUtil.toDouble( oReturn.getRetMoney() );
				String orderNo = order.getOrderNo();
				if ( CommonUtil.isNotEmpty( order.getOrderPid() ) ) {
				    MallOrder pOrder = mallOrderDAO.selectById( order.getOrderPid() );
				    if ( CommonUtil.isNotEmpty( pOrder ) ) {
					orderNo = pOrder.getOrderNo();
				    }
				}
				updateReturnStatus( pUser, oReturn, orderReturn, order );//退款成功修改退款状态
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
						if ( resultmap.get( "code" ).toString().equals( "1" ) ) {
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
						busIdAndindustry.setIndustry( 4 );
						ApiWxApplet applet = wxPublicUserService.selectBybusIdAndindustry(busIdAndindustry);
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
						    if ( resultmap.get( "code" ).toString().equals( "1" ) ) {
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
					busIdAndindustry.setIndustry( 4 );
					ApiWxApplet applet = wxPublicUserService.selectBybusIdAndindustry(busIdAndindustry);
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
					    if ( resultmap.get( "code" ).toString().equals( "1" ) ) {
						//退款成功修改退款状态
						updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

						rFlag = true;
						msg = "退款成功";
					    }
					}
				    } else if ( wxPayOrder.getTradeState().equals( "NOTPAY" ) ) {
					msg = "订单：" + wxPayOrder.getOutTradeNo() + "未支付";
				    }
				} else {//退款金额等于0，则直接修改退款状态
				    //退款成功修改退款状态
				    updateReturnStatus( pUser, oReturn, orderReturn, order );//微信退款

				    rFlag = true;
				    msg = "退款成功";
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
			//todo 调用商家联盟接口  商家联盟退款
			//unionMobileService.updateComsumeRecord( 1, order.getId() );

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

		    List< MallOrderDetail > detailList = mallOrderDetailDAO.selectByOrderId( params );
		    if ( detailList != null && detailList.size() > 0 ) {
			for ( MallOrderDetail mallOrderDetail : detailList ) {
			    updateInvenNum( null, null, order, mallOrderDetail );
			}
		    }
		}
	    } else {
		List< MallOrderDetail > detailList = mallOrderDetailDAO.selectByOrderId( params );
		if ( detailList != null && detailList.size() > 0 ) {
		    for ( MallOrderDetail mallOrderDetail : detailList ) {
			updateInvenNum( null, null, order, mallOrderDetail );
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
		String invKey = "hSeckill";//秒杀库存的key
		if ( isJxc == 0 || !product.getProTypeId().toString().equals( "0" ) ) {
		    MallProduct p = new MallProduct();
		    p.setId( product.getId() );
		    p.setProStockTotal( product.getProStockTotal() + productNum );//商品库存
		    if ( product.getProSaleTotal() - productNum > 0 )
			p.setProSaleTotal( product.getProSaleTotal() - productNum );//商品销量
		    mallProductDAO.updateById( p );
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
		    if ( isJxc == 0 || !product.getProTypeId().toString().equals( "0" ) ) {
			mallProductInventoryService.updateProductInventory( proMap );
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

	    ReturnParams returnParams = new ReturnParams();
	    returnParams.setBusId( order.getBusUserId() );
	    returnParams.setOrderNo( order.getOrderNo() );
	    returnParams.setMoney( returnMoney );
	    returnParams.setFenbi( returnFenbi );
	    returnParams.setJifen( returnJifen );

	    Map< String,Object > resultMap = memberService.refundMoneyAndJifenAndFenbi( returnParams );
	    if ( CommonUtil.toInteger( resultMap.get( "code" ) ) == -1 ) {
		//同步失败，存入redis todo  定时器还未做
		JedisUtil.rPush( Constants.REDIS_KEY + "member_return_jifen", com.alibaba.fastjson.JSONObject.toJSONString( returnParams ) );
		//		throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), ResponseEnums.INTER_ERROR.getDesc() );
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
    public Map< String,Object > selectByDIdOrder( Integer detailId ) {
	/*if(map != null){
		String money = map.get("proPrice").toString();
		if(CommonUtil.isNotEmpty(map.get("orderStatus"))){
			if(map.get("orderStatus").toString().equals("2")){
				 money = map.get("orderMoney").toString();
			}
		}
		map.put("returnMoney", money);
	}*/
	return mallOrderDAO.selectByDIdOrder( detailId );
    }

    @Override
    public MallOrderReturn selectByDId( Integer id ) {
	return mallOrderReturnDAO.selectById( id );
    }

    @Override
    public Integer selectSpeBySpeValueId( Map< String,Object > params ) {
	MallProductSpecifica spe = mallProductSpecificaService.selectByNameValueId( params );
	return spe.getId();
    }

    @Override
    public Map< String,Object > upOrderNoById( MallOrder order ) {
	Map< String,Object > result = new HashMap<>();
	mallOrderDAO.upOrderNoById( order );
	result.put( "code", "1" );
	result.put( "msg", "修改成功" );
	return result;
    }

    @Override
    public List selectOrderPid( Integer orderId ) {
	return mallOrderDAO.selectOrderPid( orderId );
    }

    /**
     * 添加订单(组成订单数据)
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > addSeckillOrder( Map< String,Object > params, Member member, Map< String,Object > result, HttpServletRequest request ) {

	String data1 = params.get( "detail" ).toString();
	//String type = params.get( "type" ).toString();
	JSONArray dobj = JSONArray.fromObject( data1 );
	JSONArray arr = JSONArray.fromObject( dobj );

	WxPublicUsers pbUser = getWpUser( member.getId() );

	Map< String,Object > list2 = new HashMap<>();
	//1:微信浏览器,99:其他浏览器,-1:微信浏览器，但是没有公众号 -2:其他浏览器，但是没有支付宝
	Integer browser = CommonUtil.judgeBrowser( request );
	if ( browser != 1 ) {
	    browser = 2;
	}

	Object receiveId = params.get( "receiveId" );//收货地址的id
	MallOrder order = new MallOrder();
	if ( arr.size() > 1 ) {//跨店购买，生成一条总订单
	    order.setOrderStatus( 1 );
	    order.setOrderFreightMoney( CommonUtil.toBigDecimal( params.get( "orderFreightMoney" ) ) );
	    order.setOrderMoney( CommonUtil.toBigDecimal( params.get( "orderMoney" ) ) );
	    order.setOrderPayWay( Integer.parseInt( params.get( "orderPayWay" ).toString() ) );
	    if ( CommonUtil.isNotEmpty( receiveId ) ) {
		order.setReceiveId( Integer.parseInt( receiveId.toString() ) );

		if ( order.getReceiveId() > 0 ) {
		    //todo 调用陈丹接口  根据地址id查询地址信息
		    Map< String,Object > addressMap = null;//mallOrderDAO.selectAddressByReceiveId( order.getReceiveId() );
		    order = getAdressOrder( order, addressMap );
		   /* if ( CommonUtil.isNotEmpty( addressMap ) ) {
			String address = CommonUtil.toString( addressMap.get( "pName" ) ) +
					CommonUtil.toString( addressMap.get( "cName" ) );
						*//**//*if(CommonUtil.isNotEmpty(addressMap.get("aName"))){
							address += CommonUtil.toString(addressMap.get("aName"));
						}*//**//*
			address += CommonUtil.toString( addressMap.get( "mem_address" ) );
			if ( CommonUtil.isNotEmpty( addressMap.get( "mem_zip_code" ) ) ) {
			    address += CommonUtil.toInteger( addressMap.get( "mem_zip_code" ) );
			}
			order.setReceiveName( CommonUtil.toString( addressMap.get( "mem_name" ) ) );
			order.setReceivePhone( CommonUtil.toString( addressMap.get( "mem_phone" ) ) );
			order.setReceiveAddress( address );
		    }*/
		}
	    }
	    order.setBuyerUserId( member.getId() );
	    if ( CommonUtil.isNotEmpty( pbUser ) ) {
		order.setSellerUserId( pbUser.getId() );//商家公众号id
	    } else {
		order.setSellerUserId( 0 );//商家公众号id
	    }
	    order.setBusUserId( member.getBusid() );
	    order.setCreateTime( new Date() );
	    order.setOrderNo( "SC" + System.currentTimeMillis() );
	    order.setOrderPid( 0 );
	    order.setMemCardType( Integer.parseInt( params.get( "memCardType" ).toString() ) );
	    order.setOrderOldMoney( CommonUtil.toBigDecimal( params.get( "orderOldMoney" ) ) );
	    if ( CommonUtil.isNotEmpty( browser ) ) {
		order.setBuyerUserType( browser );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "flowPhone" ) ) ) {
		order.setFlowPhone( CommonUtil.toString( params.get( "flowPhone" ) ) );
		order.setFlowRechargeStatus( 0 );
	    } else {
		order.setFlowRechargeStatus( -1 );
	    }
	    if ( CommonUtil.isNotEmpty( member.getNickname() ) ) {
		order.setMemberName( member.getNickname() );
	    }
	    mallOrderDAO.insert( order );
	    list2.put( "orderId", order.getId() );
	}
	for ( int i = 0; i < arr.size(); i++ ) {
	    JSONObject list = JSONObject.fromObject( arr.get( i ) );
	    JSONArray list1 = JSONArray.fromObject( list.get( "message" ) );
	    JSONObject list3 = JSONObject.fromObject( list1.get( 0 ) );

	    String orderNo = "SC" + System.currentTimeMillis();

	    double freightMoney = 0;
	    String priceMap = params.get( "freightMoney" ).toString();
	    JSONObject jasonObject = JSONObject.fromObject( priceMap );
	    String shopId = "";
	    for ( Object key : jasonObject.keySet() ) {
		if ( CommonUtil.isNotEmpty( list3.get( "shop_id" ) ) ) {
		    shopId = list3.get( "shop_id" ).toString();
		} else if ( CommonUtil.isNotEmpty( list.get( "shop_id" ) ) ) {
		    shopId = list.get( "shop_id" ).toString();
		}
		if ( key.equals( shopId ) ) {//获取运费
		    freightMoney = Double.parseDouble( jasonObject.get( key ).toString() );
		}
	    }
	    double price;
	    Object couponList = null;
	    Object couponCode = null;
	    String sumCoupon = null;
	    if ( arr.size() > 1 ) {
		price = Double.parseDouble( list.get( "price_total" ).toString() );
	    } else {
		price = Double.parseDouble( params.get( "orderMoney" ).toString() );
	    }
	    double actuallyPaid = price;//实付价格
	    double discount = 0;
	    double fullCoupon = 0;
	    //使用优惠券，计算价格
	    if ( null != params.get( "couponList" ) && !params.get( "couponList" ).equals( "" ) ) {
		couponList = params.get( "couponList" );

		JSONObject couponObj = JSONObject.fromObject( couponList );
		if ( null != couponObj.get( shopId ) && !couponObj.get( shopId ).equals( "" ) ) {
		    JSONObject sObj = JSONObject.fromObject( couponObj.get( shopId ) );
		    sumCoupon = params.get( "sumCoupon" ).toString();
		    couponCode = sObj.get( "couponCode" );
		    discount = Double.valueOf( sObj.get( "discountCoupon" ).toString() );
		    fullCoupon = Double.valueOf( sObj.get( "fullCoupon" ).toString() );
		}
		if ( arr.size() > 1 ) {
		    if ( discount > 0 ) {
			actuallyPaid = price * discount / 10;//优惠券打折的价格
		    }
		    if ( fullCoupon > 0 ) {
			actuallyPaid = price - ( price / Double.valueOf( sumCoupon ) ) * fullCoupon;//满减券的价格
		    }
		}
	    }

	    Object groupBuyId = list.get( "groupBuyId" );
	    int orderType = 0;
	    if ( CommonUtil.isNotEmpty( list.get( "groupType" ) ) ) {
		orderType = Integer.parseInt( list.get( "groupType" ).toString() );
	    }
	    //团购id
	    if ( CommonUtil.isNotEmpty( groupBuyId ) ) {
		groupBuyId = list.get( "groupBuyId" );
	    } else {
		groupBuyId = "0";
	    }

	    Object p = list.get( "pJoinId" );
	    int pJoinId = 0;//参团表的父id
	    if ( CommonUtil.isNotEmpty( p ) ) {
		pJoinId = Integer.parseInt( p.toString() );
	    }
	    //买家留言
	    String orderBuyerMessage = params.get( "orderBuyerMessage" + shopId ).toString();

	    MallOrder order1 = new MallOrder();
	    String deliveryMethod = params.get( "deliveryMethod" ).toString();//到店自提（添加参数）
	    if ( CommonUtil.isNotEmpty( deliveryMethod ) ) {
		order1.setDeliveryMethod( CommonUtil.toInteger( deliveryMethod ) );
	    }
	    if ( null != deliveryMethod && deliveryMethod.equals( "2" ) ) {
		order1.setAppointmentName( params.get( "appointName" ).toString() );
		order1.setAppointmentTelephone( ( params.get( "appointTel" ).toString() ) );
		order1.setAppointmentTime( DateTimeKit.parse( params.get( "appointTime" ).toString(), "" ) );
		order1.setTakeTheirId( CommonUtil.toInteger( params.get( "takeTheirId" ) ) );
		order1.setAppointmentStartTime( params.get( "appStartTime" ).toString() );
		order1.setAppointmentEndTime( params.get( "appEndTime" ).toString() );
	    } else {
		if ( CommonUtil.isNotEmpty( receiveId ) ) {
		    order1.setReceiveId( Integer.parseInt( receiveId.toString() ) );

		    //todo 调用陈丹接口  根据地址id查询地址信息
		    Map< String,Object > addressMap = null;//mallOrderDAO.selectAddressByReceiveId( order1.getReceiveId() );
		    order = getAdressOrder( order, addressMap );
		   /* if ( CommonUtil.isNotEmpty( addressMap ) ) {
			String address = CommonUtil.toString( addressMap.get( "pName" ) ) +
					CommonUtil.toString( addressMap.get( "cName" ) );
						*//*if(CommonUtil.isNotEmpty(addressMap.get("aName"))){
							address += CommonUtil.toString(addressMap.get("aName"));
						}*//*
			address += CommonUtil.toString( addressMap.get( "mem_address" ) );
			if ( CommonUtil.isNotEmpty( addressMap.get( "mem_zip_code" ) ) ) {
			    address += CommonUtil.toInteger( addressMap.get( "mem_zip_code" ) );
			}
			order1.setReceiveName( CommonUtil.toString( addressMap.get( "mem_name" ) ) );
			order1.setReceivePhone( CommonUtil.toString( addressMap.get( "mem_phone" ) ) );
			order1.setReceiveAddress( address );
		    }*/
		}
	    }
	    order1.setOrderStatus( 1 );
	    order1.setOrderBuyerMessage( orderBuyerMessage );
	    order1.setOrderMoney( CommonUtil.toBigDecimal( actuallyPaid ) );
	    order1.setOrderFreightMoney( CommonUtil.toBigDecimal( freightMoney ) );
	    order1.setOrderPayWay( Integer.parseInt( params.get( "orderPayWay" ).toString() ) );
	    order1.setBuyerUserId( member.getId() );
	    if ( CommonUtil.isNotEmpty( pbUser ) ) {
		order1.setSellerUserId( pbUser.getId() );//商家公众号id
	    } else {
		order1.setSellerUserId( 0 );//商家公众号id
	    }
	    order1.setBusUserId( member.getBusid() );
	    order1.setOrderNo( orderNo );
	    order1.setShopId( Integer.parseInt( list.get( "shop_id" ).toString() ) );
	    order1.setCreateTime( new Date() );
	    order1.setOrderPid( order.getId() );
	    order1.setMemCardType( Integer.parseInt( params.get( "memCardType" ).toString() ) );
	    order1.setOrderOldMoney( CommonUtil.toBigDecimal( list.get( "primary_price" ) ) );
	    order1.setGroupBuyId( Integer.parseInt( groupBuyId.toString() ) );
	    order1.setOrderType( orderType );
	    order1.setPJoinId( pJoinId );
	    if ( CommonUtil.isNotEmpty( couponCode ) ) {
		order1.setCouponCode( couponCode.toString() );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "flowPhone" ) ) ) {
		order1.setFlowPhone( CommonUtil.toString( params.get( "flowPhone" ) ) );
		order1.setFlowRechargeStatus( 0 );
	    } else {
		order1.setFlowRechargeStatus( -1 );
	    }
	    if ( CommonUtil.isNotEmpty( browser ) ) {
		order1.setBuyerUserType( browser );
	    }
	    if ( CommonUtil.isNotEmpty( member.getNickname() ) ) {
		order1.setMemberName( member.getNickname() );
	    }
	    list2.put( "order", order1 );
	    list2.put( "detail", list1 );
	    /*if(type.equals("1")){//购物车的参数
		    list2.put("detail", list1);
	    }else{//立即购买的参数
		    list2.put("detail", list);
	    }*/
	    list2.put( "type", params.get( "type" ) );
	    list2.put( "couponList", couponList );
	    list2.put( "sumCoupon", sumCoupon );
	    list2.put( "proTypeId", params.get( "pro_type_id" ) );
	    list2.put( "isJifen", params.get( "isJifen" ) );//能否使用积分（1为是 ,0为否）
	    list2.put( "isFenbi", params.get( "isFenbi" ) );//能否使用粉币（1为是 ,0为否）
	    list2.put( "sumJifen", params.get( "jifenProMoney" ) );
	    list2.put( "sumFenbi", params.get( "fenbiProMoney" ) );
	    list2.put( "integral_money", params.get( "integral_money" ) );
	    list2.put( "integral", params.get( "integral" ) );
	    list2.put( "fenbi_money", params.get( "fenbi_money" ) );
	    list2.put( "fenbi", params.get( "fenbi" ) );
	    list2.put( "fenbiNum", params.get( "fenbiNum" ) );
	    list2.put( "yhqNum", params.get( "yhqNum" ) );
	    list2.put( "jifenNum", params.get( "jifenNum" ) );
	    list2.put( "countJifen", params.get( "countJifen" ) );
	    list2.put( "countFenbi", params.get( "countFenbi" ) );
	    list2.put( "countYhq", params.get( "countYhq" ) );
	    list2.put( "unionYouhuiList", params.get( "unionYouhuiList" ) );
	    list2.put( "countUnion", params.get( "countUnion" ) );
	    list2.put( "unionNum", params.get( "unionNum" ) );
	    if ( CommonUtil.isNotEmpty( params.get( "unionDiscount" ) ) )
		list2.put( "unionDiscount", params.get( "unionDiscount" ) );
	    if ( CommonUtil.isNotEmpty( params.get( "cardId" ) ) )
		list2.put( "cardId", params.get( "cardId" ) );
	    if ( CommonUtil.isNotEmpty( params.get( "union_id" ) ) )
		list2.put( "union_id", params.get( "union_id" ) );
	    //添加订单到数据库
	    result = addOrder( list2, request );
	    if ( price == 0 ) {//订单为0，设置payWays，使订单不跳转至微信付款页面
		result.put( "payWays", -1 );
	    }
	    result.put( "proTypeId", params.get( "pro_type_id" ) );
	    result.put( "out_trade_no", orderNo );
	    result.put( "orderMoney", order1.getOrderMoney() );
	    result.put( "busId", order1.getBusUserId() );
	}
	return result;
    }

    private MallOrder getAdressOrder( MallOrder order, Map< String,Object > addressMap ) {
	if ( CommonUtil.isNotEmpty( addressMap ) ) {
	    String address = CommonUtil.toString( addressMap.get( "pName" ) ) +
			    CommonUtil.toString( addressMap.get( "cName" ) );
						/*if(CommonUtil.isNotEmpty(addressMap.get("aName"))){
							address += CommonUtil.toString(addressMap.get("aName"));
						}*/
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
     * 计算积分和粉币的比例
     */
    public Map< String,Object > getCurrencyFenbi( Member member ) {
	Map< String,Object > map = new HashMap<>();
	//查询粉币抵扣的比例
	List< Map > currencyList = dictService.getDict( "1058" );
	if ( currencyList != null && currencyList.size() > 0 ) {
	    Map fenbiMap = currencyList.get( 0 );
	    if ( CommonUtil.isNotEmpty( fenbiMap ) ) {
		/*double fenbi = Double.parseDouble(fenbiMap.get("item_value").toString());
		//计算粉币抵扣的金额
		int fenbiMoney = CommonUtil.currencyCount(member.getFansCurrency(), fenbi);
		map.put("fenbiMoney", fenbiMoney);*/ //粉币抵扣的钱
		map.put( "fenbiMap", fenbiMap );//粉币的比例
	    }
	}

	//查询积分抵扣的信息
	//todo 调用彭江丽接口  查询商家的积分抵扣信息
	/*PublicParameterSet ps = paramSetMapper.findBybusId( member.getBusid() );
	if ( ps != null ) {
			*//*int jifen = CommonUtil.integralCount(member.getIntegral(), ps);
			map.put("jifen", jifen);*//*
	    map.put( "paramSet", ps );
	}*/

	return map;
    }

    /**
     * 计算粉币和积分抵扣的最大值
     */
    public Map< String,Object > countIntegralFenbi( Member member, double orderTotalMoney ) {
	Map< String,Object > map;
	DecimalFormat df = new DecimalFormat( "######0.00" );

	map = getCurrencyFenbi( member );//获取粉币和积分

	Object fenbiObj = map.get( "fenbiMap" );
	JSONObject fenbiMap = JSONObject.fromObject( fenbiObj );
	//Object paramSet = map.get( "paramSet" );
	//Integer userIntegral = member.getIntegral();//用户积分
	double userFenbi = member.getFansCurrency();//用户粉币
	//todo 等待彭江丽的实体类  （积分抵扣）  接口合并成一个
	/*PublicParameterSet ps = (PublicParameterSet) JSONObject.toBean( JSONObject.fromObject( paramSet ), PublicParameterSet.class );
	if ( CommonUtil.isNotEmpty( ps ) ) {
	    if ( ps.getIntegralratio() > 0 ) {
		String numIntegral = userIntegral / ps.getIntegralratio() + "";//
		//使用多少积分
		double integral = ps.getIntegralratio() * Double.parseDouble( ( numIntegral.substring( 0, numIntegral.indexOf( "." ) ) ) );
		//积分抵扣多少钱
		double integral_money = ( integral / ps.getIntegralratio() ) * ps.getChangemoney();
		if ( orderTotalMoney > 0 && CommonUtil.toDouble( orderTotalMoney ) < CommonUtil.toDouble( integral_money ) ) {
		    integral_money = orderTotalMoney;
		    integral = ps.getIntegralratio() * integral_money;
		}
		map.put( "integral_money", CommonUtil.toDouble( df.format( integral_money ) ) );//积分可抵扣的金额
		map.put( "integral", CommonUtil.toDouble( df.format( integral ) ) );//积分数量
	    }
	}*/
	double itemValue = Double.parseDouble( fenbiMap.get( "item_value" ).toString() );
	Integer numFenbi = (int) Double.parseDouble( userFenbi / itemValue + "" );//
	//使用多少粉币
	double m = Double.parseDouble( ( numFenbi - numFenbi % 10 ) + "" );
	double fenbi = itemValue * m;
	//粉币抵扣多少钱
	double fenbi_money = fenbi / itemValue;

	if ( orderTotalMoney > 0 && orderTotalMoney < fenbi_money && fenbi_money > 10 ) {
	    double avg = CommonUtil.div( orderTotalMoney, itemValue, 1 );
	    fenbi_money = avg * itemValue;
	    fenbi = avg;
	}
	if ( userFenbi >= itemValue * 10 && fenbi >= itemValue * 10 && fenbi > 0 && fenbi_money > 0 ) {
	    map.put( "fenbi_money", CommonUtil.toDouble( df.format( fenbi_money ) ) );//粉币可抵扣的金额
	    map.put( "fenbi", CommonUtil.toDouble( df.format( fenbi ) ) );//粉币数量
	}
		/*map.put("fenbi_money", CommonUtil.toDouble(df.format(fenbi_money)));//粉币可抵扣的金额
		map.put("fenbi", CommonUtil.toDouble(df.format(fenbi)));//粉币数量
*/
	return map;
    }

    /**
     * 好友代付成功回调函数
     */
    @Override
    public int paySuccessDaifu( Map< String,Object > params ) {
	String orderNo = params.get( "out_trade_no" ).toString();

	MallDaifu daifu = mallDaifuDAO.selectByDfOrderNo( orderNo );
	MallDaifu df = new MallDaifu();
	df.setId( daifu.getId() );
	df.setDfPayStatus( 1 );
	df.setDfPayTime( new Date() );
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
		    break;
		}
	    }
	    resultMap.put( "proTypeId", orderDetailList.get( 0 ).getProTypeId() );
	}
	resultMap.put( "busId", order.getBusUserId() );
	return resultMap;
    }

    @Override
    public Map< String,Object > addMallDaifu( MallDaifu daifu ) {
	Map< String,Object > resultMap = new HashMap<>();

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
		    return resultMap;
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
		} else {
		    code = mallDaifuDAO.updateById( daifu );
		}

	    }
	    if ( code > 0 && CommonUtil.isNotEmpty( daifu.getId() ) ) {
		id = daifu.getId();
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
	/*Map< String,Object > params = new HashMap<>();
	params.put( "id", order.getReceiveId() );
	params.put( "memberId", order.getBuyerUserId() );*/
	//todo 调用陈丹接口  查询地址列表
	List< Map< String,Object > > list = null;//mallOrderDAO.selectShipAddress( params );
	if ( list != null && list.size() > 0 ) {
	    Map< String,Object > map = list.get( 0 );
	    request.setAttribute( "expressName", map.get( "mem_name" ) );
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
	sheet.setColumnWidth( 14, 100 * 256 );
	HSSFCellStyle centerStyle = workbook.createCellStyle();
	centerStyle.setAlignment( HSSFCellStyle.ALIGN_CENTER );// 设置单元格左右居中

	HSSFCellStyle leftStyle = workbook.createCellStyle();
	leftStyle.setAlignment( HSSFCellStyle.ALIGN_LEFT );// 设置单元格左右居中
	//循环数据
	if ( type == 1 ) {
	    //			getOrderList(params,sheet,valueStyle);
	    List< MallOrder > data = mallOrderDAO.explodOrder( params );
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
	    if ( order.getDeliveryMethod() == 1 )
		state = "待发货";
	    else if ( order.getDeliveryMethod() == 2 )
		state = "待提货";
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
		    int dStatus = detail.getStatus();
		    String sale = "";
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
		    String shopname = "";
		    if ( shopList != null && shopList.size() > 0 ) {
			for ( Map< String,Object > shopMap : shopList ) {
			    if ( CommonUtil.toInteger( shopMap.get( "id" ) ) == order.getShopId() ) {
				shopname = shopMap.get( "sto_name" ).toString();
				break;
			    }
			}
		    }

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
	    createCell( row, 12, order.getShopName(), valueStyle );
	    createCell( row, 13, payWay, valueStyle );
	    createCell( row, 14, address, leftStyle );
	    createCell( row, 15, order.getOrderBuyerMessage(), valueStyle );
	    createCell( row, 16, order.getOrderSellerRemark(), valueStyle );
	    i++;
	}
	if ( order.getMallOrderDetail().size() < 1 && orderPayWay != 5 )
	    i++;
	return i;
    }

    private void createCell( HSSFRow row, int column, String value, HSSFCellStyle valueStyle ) {
	HSSFCell cell = row.createCell( column );
	cell.setCellValue( value );
	cell.setCellStyle( valueStyle );
    }

    @Override
    public MallOrderDetail selectOrderDetailById( Integer id ) {
	return mallOrderDetailDAO.selectById( id );
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
    public void sendMsg( MallOrder order, int type ) {

	int id = 0;
	String title = "";
	if ( type == 4 ) {//购买成功通知
	    title = "购买成功通知";
	}
	JSONObject msgObj = new JSONObject();
	WxPublicUsers publicUser = wxPublicUserService.selectByUserId( order.getSellerUserId() );//通过商家id查询商家公众号id
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

	if ( CommonUtil.isNotEmpty( msgObj ) && id > 0 ) {
	    List< Object > objs = new ArrayList<>();
	    objs.add( "您好，您已购买成功" );
	    objs.add( "您的商品已经购买成功，请耐心等待商家发货" );
	    String url = PropertiesUtil.getHomeUrl() + "/mMember/79B4DE7C/toUser.do?member_id=" + order.getBuyerUserId() + "&uId=" + order.getSellerUserId();

	    SendWxMsgTemplate template = new SendWxMsgTemplate();
	    template.setId( id );
	    template.setUrl( url );
	    template.setMemberId( order.getBuyerUserId() );
	    template.setPublicId( order.getSellerUserId() );
	    template.setObjs( objs );

	    logger.info( "发送消息模板参数：" + objs );
	    wxPublicUserService.sendWxMsgTemplate( template );
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
		List< MallOrderDetail > orderDetailList = mallOrderDetailDAO.selectByOrderId( map );
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

    /**
     * 组成一个订单详情数据
     */
    private MallOrderDetail addOrderDetails( Map< String,Object > params, HttpServletRequest request, int orderId,
		    Map< String,Object > map ) {
	Object proCode = map.get( "pro_code" );
	if ( null == proCode || proCode.equals( "" ) ) {
	    proCode = "";
	}
	Object proMessage = map.get( "product_message" );
	if ( null == proMessage || proMessage.equals( "" ) ) {
	    proMessage = "";
	}
	Object productSpecificas = map.get( "product_specificas" );
	if ( null == productSpecificas || productSpecificas.equals( "" ) ) {
	    productSpecificas = "";
	}
	Object productSpeciname = map.get( "product_speciname" );
	if ( null == productSpeciname || productSpeciname.equals( "" ) ) {
	    productSpeciname = "";
	}
	String couponCode = null;
	String isCoupons = map.get( "isCoupons" ).toString();
	String is_member_discount = map.get( "is_member_discount" ).toString();

	int num = Integer.parseInt( map.get( "product_num" ).toString() );//商品的购买数量
	double detProPrice = CommonUtil.multiply( Double.valueOf( map.get( "price" ).toString() ), num );
	double primaryPrice = Double.valueOf( map.get( "primary_price" ).toString() );//商品原价

	//商品的折数
	double discount1 = Double.parseDouble( map.get( "discount" ).toString() ) * 100;
	String discount2 = String.valueOf( discount1 );
	Integer discount = Integer.parseInt( discount2.substring( 0, discount2.indexOf( "." ) ) );

	double discountedPrices = 0;
	double price = detProPrice;
	String coupons;
	String shopId = map.get( "shop_id" ).toString();
	Object couponList;
	String duofenCoupon = "";//多粉优惠券
	//int cardId = 0;
	double hyqDiscountPrice = 0;

	//计算商家联盟优惠的价格
	Integer sumUnion = 0;//存放能使用积分抵扣的商品总数
	if ( CommonUtil.isNotEmpty( request.getAttribute( "sumUnion" ) ) ) {
	    sumUnion = CommonUtil.toInteger( request.getAttribute( "sumUnion" ) );
	}
	double unionPreferCount = 0;//保存订单已经优惠的价钱（联盟优惠）
	if ( CommonUtil.isNotEmpty( request.getAttribute( "unionPreferCount" ) ) ) {
	    unionPreferCount = CommonUtil.toDouble( request.getAttribute( "unionPreferCount" ) );
	}
	double unionPrefer;//计算联盟优惠了多少钱
	//计算联盟优惠
	if ( params.containsKey( "unionDiscount" ) ) {
	    int unionNum = CommonUtil.toInteger( params.get( "unionNum" ) );

	    double unionDiscount = CommonUtil.toDouble( params.get( "unionDiscount" ) );//联盟折扣

	    price = priceSubstring( ( detProPrice * unionDiscount / 10 ) + "", 3 );//计算联盟折扣后的最终价格
	    unionPrefer = CommonUtil.subtract( detProPrice, price );//计算联盟折扣优惠的价格

	    //计算最后一个联盟的价格
	    if ( unionNum == sumUnion + 1 && unionNum > 0 && params.containsKey( "countUnion" ) ) {
		double countYouhui = CommonUtil.toDouble( params.get( "countUnion" ) );
		unionPrefer = CommonUtil.subtract( countYouhui, unionPreferCount );//优惠后的价格
		price = CommonUtil.subtract( detProPrice, unionPrefer );
	    }
	    detProPrice = price;

	    request.setAttribute( "sumUnion", sumUnion + 1 );
	    request.setAttribute( "unionPreferCount", unionPrefer + unionPreferCount );

	    discountedPrices = discountedPrices + unionPrefer;

	}

	//使用优惠券计算价格、优惠
	if ( null != params.get( "couponList" ) && !params.get( "couponList" ).equals( "" ) ) {
	    couponList = params.get( "couponList" );

	    JSONObject couponObj = JSONObject.fromObject( couponList );
	    if ( null != couponObj.get( shopId ) && !couponObj.get( shopId ).equals( "" ) ) {
		JSONObject sObj = JSONObject.fromObject( couponObj.get( shopId ) );
		/*Object couponType = sObj.get( "couponType" );//获取优惠券的类型（1为微信 ，2为多粉）
		if ( couponType.equals( "2" ) ) {
		    duofenCoupon = sObj.toString();
		    Object addUser = sObj.get( "addUser" );//是否允许使用叠加（1为允许 ，0不允许）
		    Object cardType = sObj.get( "cardType" );//卡券的类型（1是代金券，0是折扣券）
		    if ( cardType.equals( "1" ) && addUser.equals( "1" ) ) {//获取多粉优惠券的code()
			cardId = Integer.parseInt( sObj.get( "cId" ).toString() );
			int couponNum = Integer.parseInt( sObj.get( "num" ).toString() );
			//todo 从session中获取会员对象
			Member member = null;//CommonUtil.getLoginMember( request );
			//todo 调用彭江丽接口 ，根据优惠券信息获取优惠券code
			couponCode = duofenCardService.findCardCode( cardId, member.getId(), couponNum );
		    } else {
			cardId = Integer.parseInt( sObj.get( "cId" ).toString() );
			couponCode = sObj.get( "couponCode" ).toString();
		    }
		} else {//获取微信优惠券的code
		    couponCode = sObj.get( "couponCode" ).toString();//使用优惠券的优惠码
		}*/

		coupons = sObj.get( "fullCoupon" ).toString();
		double fullCoupon = Double.valueOf( coupons );//满减券满足条件后需要减去的金额

		double sumCoupon = Double.valueOf( sObj.get( "proDisAll" ).toString() );//有优惠的商品总额
		boolean yhqFlag = false;
		if ( isCoupons.equals( "1" ) && is_member_discount.equals( "1" ) ) {//能使用优惠券、享受会员折扣
		    if ( fullCoupon != 0 ) {//满减券计算价格、优惠
			hyqDiscountPrice = priceSubstring( ( ( detProPrice / sumCoupon ) * fullCoupon ) + "", 3 );//优惠
			if ( detProPrice < hyqDiscountPrice ) {
			    hyqDiscountPrice = detProPrice;
			}
			price = priceSubstring( ( detProPrice - hyqDiscountPrice ) + "", 3 );//价格
			yhqFlag = true;
		    } else {//折扣计算价格、优惠
			Object d = sObj.get( "discountCoupon" );
			if ( null != d && !d.toString().equals( "" ) ) {
			    double discountCoupon = Double.parseDouble( d.toString() );
			    price = priceSubstring( ( detProPrice * discountCoupon / 10 ) + "", 3 );
			    if ( detProPrice < price ) {
				price = detProPrice;
			    }
			    hyqDiscountPrice = CommonUtil.subtract( detProPrice, price );
			    yhqFlag = true;
			}
		    }
		} else if ( isCoupons.equals( "1" ) && is_member_discount.equals( "0" ) ) {//能使用优惠券、不享受会员折扣
		    if ( fullCoupon != 0 ) {//满减券计算价格、优惠
			hyqDiscountPrice = priceSubstring( ( ( detProPrice / sumCoupon ) * fullCoupon ) + "", 3 );
			if ( detProPrice < hyqDiscountPrice ) {
			    hyqDiscountPrice = detProPrice;
			}
			price = priceSubstring( ( detProPrice - hyqDiscountPrice ) + "", 3 );
			yhqFlag = true;
		    } else {//折扣计算价格、优惠
			if ( null != sObj.get( "discountCoupon" ) && !sObj.get( "discountCoupon" ).equals( "" ) ) {
			    double discountCoupon = Double.parseDouble( sObj.get( "discountCoupon" ).toString() );
			    price = priceSubstring( ( detProPrice * discountCoupon / 10 ) + "", 3 );
			    if ( detProPrice < price ) {
				price = detProPrice;
			    }
			    hyqDiscountPrice = CommonUtil.subtract( detProPrice, price );
			    yhqFlag = true;
			}
		    }
		}
		if ( yhqFlag ) {
		    //计算最后一个有优惠价的商品价格
		    Double yhPrice = countLastPriceYhq( params, shopId, detProPrice, request, sObj );
		    if ( CommonUtil.isNotEmpty( yhPrice ) ) {
			price = priceSubstring( yhPrice + "", 3 );
			hyqDiscountPrice = CommonUtil.subtract( detProPrice, price );
		    }
		}
		discountedPrices += hyqDiscountPrice;
	    }
	}

	Object couponDiscount = request.getAttribute( "couponDiscount" );
	if ( null != couponDiscount && !couponDiscount.toString().equals( "" ) ) {
	    couponDiscount = hyqDiscountPrice + Double.parseDouble( couponDiscount.toString() );
	} else {
	    couponDiscount = hyqDiscountPrice;
	}
	request.setAttribute( "couponDiscount", couponDiscount );//保存优惠券已经优惠的金额

	Object isJifen = params.get( "isJifen" );//页面是否使用积分抵扣
	Object isFenbi = params.get( "isFenbi" );//页面是否使用粉币抵扣
	Object sumJifen = params.get( "sumJifen" );//能使用积分的商品总价
	//Object sumFenbi = params.get( "sumFenbi" );//能使用粉币的商品总价
	double priceJifen = price;
	Integer useJifen = 0;//使用的积分
	double useFenbi = 0;//使用的粉币

	//计算粉币
	double fenbiPrefer = 0;//粉币优惠多少钱、使用多少粉币兑换
	double priceFenbi = price;
	double fenbi_money = 0;
	Object fenbiNum = params.get( "fenbiNum" );//存放能使用粉币抵扣的商品总数
	Integer sumFenbiNum = 0;//存放已经使用粉币抵扣的商品个数

	if ( CommonUtil.isNotEmpty( params.get( "fenbi_money" ) ) ) {
	    fenbi_money = Double.parseDouble( params.get( "fenbi_money" ).toString() );
	}
	double fenbiPreferCount = 0;//保存订单已经优惠的价钱（粉币）
	if ( CommonUtil.isNotEmpty( request.getAttribute( "fenbiPrefer" ) ) ) {
	    fenbiPreferCount = CommonUtil.toDouble( request.getAttribute( "fenbiPrefer" ) );
	}
	if ( CommonUtil.isNotEmpty( request.getAttribute( "sumFenbiNum" ) ) ) {
	    sumFenbiNum = CommonUtil.toInteger( request.getAttribute( "sumFenbiNum" ) );
	}
	//计算抵扣粉币
	if ( null != isFenbi && isFenbi.toString().equals( "1" ) && ( map.get( "is_fenbi_deduction" ).toString() ).equals( "1" ) && fenbi_money > 0 ) {
	    double f = CommonUtil.div( priceFenbi, CommonUtil.toDouble( sumFenbiNum ), 3 );
	    fenbiPrefer = priceSubstring( ( f * fenbi_money ) + "", 3 );
	    price = CommonUtil.subtract( priceFenbi, fenbiPrefer );
	    useFenbi = ( Double.parseDouble( params.get( "fenbi" ).toString() ) );
	    if ( null != fenbiNum && !fenbiNum.toString().equals( "" ) ) {
		String numFenbi = fenbiNum.toString();
		String fenbi = ( useFenbi / Double.parseDouble( numFenbi ) ) + "";
		useFenbi = priceSubstring( fenbi, 2 );
		int fbNum = CommonUtil.toInteger( fenbiNum );
		//最后一个能积分抵扣的商品
		if ( fbNum == sumFenbiNum + 1 && fenbiPreferCount + fenbiPrefer != fenbi_money ) {
		    Double priceCha = CommonUtil.subtract( fenbi_money, fenbiPreferCount );
		    double detPrice = CommonUtil.subtract( priceFenbi, priceCha );
		    if ( detPrice != price ) {
			price = priceSubstring( detPrice + "", 3 );
			fenbiPrefer = CommonUtil.subtract( priceFenbi, price );
			Object sumUseFenbi = request.getAttribute( "useFenbi" );
			if ( CommonUtil.isNotEmpty( sumUseFenbi ) ) {
			    useFenbi = CommonUtil.subtract( Double.parseDouble( sumUseFenbi.toString() ), useFenbi );
			    if ( useFenbi < 0 ) {
				useFenbi = 0;
			    }
			}
		    }
		}
	    }
	    //当优惠的价格大于商品的价格
	    if ( fenbiPrefer > priceFenbi ) {
		fenbiPrefer = priceFenbi;
		price = 0;
	    }
	    discountedPrices = discountedPrices + fenbiPrefer;
	    request.setAttribute( "sumFenbiNum", sumFenbiNum + 1 );
	}

	//计算积分
	double jifenPrefer = 0;//积分优惠多少钱
	Object jifenNum = params.get( "jifenNum" );
	Integer sumJifenNum = 0;//存放能使用积分抵扣的商品总数
	double integral_money = 0;//积分总共能抵扣的金额

	if ( CommonUtil.isNotEmpty( params.get( "integral_money" ) ) ) {
	    integral_money = Double.parseDouble( params.get( "integral_money" ).toString() );
	}
	double jifenPreferCount = 0;//保存订单已经优惠的价钱（积分）
	if ( CommonUtil.isNotEmpty( request.getAttribute( "jifenPrefer" ) ) ) {
	    jifenPreferCount = CommonUtil.toDouble( request.getAttribute( "jifenPrefer" ) );
	}
	if ( CommonUtil.isNotEmpty( request.getAttribute( "sumJifenNum" ) ) ) {
	    sumJifenNum = CommonUtil.toInteger( request.getAttribute( "sumJifenNum" ) );
	}
	//计算抵扣积分
	if ( null != isJifen && isJifen.toString().equals( "1" ) && ( map.get( "is_integral_deduction" ).toString() ).equals( "1" ) && integral_money > 0 ) {
	    double p = CommonUtil.div( priceJifen, CommonUtil.toDouble( sumJifen ), 3 );
	    jifenPrefer = priceSubstring( ( p * integral_money ) + "", 3 );
	    price = CommonUtil.subtract( priceJifen, jifenPrefer );
	    useJifen = (int) ( Double.parseDouble( params.get( "integral" ).toString() ) );
	    if ( null != jifenNum && !jifenNum.toString().equals( "" ) ) {
		double numJifen = priceSubstring( jifenNum.toString(), 0 );
		double jifen = useJifen / numJifen;
		useJifen = (int) jifen;
		int jfNum = CommonUtil.toInteger( jifenNum );
		//最后一个能积分抵扣的商品
		if ( jfNum == sumJifenNum + 1 && jifenPreferCount + jifenPrefer != integral_money ) {
		    Double priceCha = CommonUtil.subtract( integral_money, jifenPreferCount );
		    double detPrice = CommonUtil.subtract( priceJifen, priceCha );
		    if ( detPrice != price ) {
			price = priceSubstring( detPrice + "", 3 );
			jifenPrefer = priceSubstring( ( priceJifen - price ) + "", 3 );
			Object sumUseJifen = request.getAttribute( "useJifen" );
			if ( CommonUtil.isNotEmpty( sumUseJifen ) ) {
			    useJifen = (int) CommonUtil.subtract( Double.parseDouble( sumUseJifen.toString() ), Double.parseDouble( useJifen + "" ) );
			    if ( useJifen < 0 ) {
				useJifen = 0;
			    }
			}
		    }
		}
	    }
	    //当优惠的价格大于商品的价格
	    if ( jifenPrefer > priceJifen ) {
		jifenPrefer = priceJifen;
		price = 0;
	    }
	    discountedPrices = discountedPrices + jifenPrefer;
	    request.setAttribute( "sumJifenNum", sumJifenNum + 1 );
	}

	MallOrderDetail od = new MallOrderDetail();
	od.setUseFenbi( useFenbi );
	od.setUseJifen( CommonUtil.toDouble( useJifen ) );
	od.setFenbiYouhui( BigDecimal.valueOf( fenbiPrefer ) );
	od.setIntegralYouhui( BigDecimal.valueOf( jifenPrefer ) );

	Object jifenPrefer1 = request.getAttribute( "jifenPrefer" );
	Object useJifen1 = request.getAttribute( "useJifen" );
	Object fenbiPrefer1 = request.getAttribute( "fenbiPrefer" );
	Object useFenbi1 = request.getAttribute( "useFenbi" );
	if ( null != jifenPrefer1 && !jifenPrefer1.toString().equals( "" ) ) {//积分优惠的钱
	    jifenPrefer = CommonUtil.add( Double.parseDouble( jifenPrefer1.toString() ), jifenPrefer );
	}
	if ( null != useJifen1 && !useJifen1.equals( "" ) ) {//使用的积分
	    useJifen = Integer.parseInt( useJifen1.toString() ) + useJifen;
	}
	if ( null != fenbiPrefer1 && !fenbiPrefer1.toString().equals( "" ) ) {//粉币优惠的钱
	    fenbiPrefer = CommonUtil.add( Double.parseDouble( fenbiPrefer1.toString() ), fenbiPrefer );
	}
	if ( null != jifenPrefer1 && !jifenPrefer1.toString().equals( "" ) ) {//使用的粉币
	    useFenbi = CommonUtil.add( Double.parseDouble( useFenbi1.toString() ), useFenbi );
	}
	request.setAttribute( "jifenPrefer", jifenPrefer );
	request.setAttribute( "useJifen", useJifen );
	request.setAttribute( "fenbiPrefer", fenbiPrefer );
	request.setAttribute( "useFenbi", useFenbi );
	double sumPrefer = discountedPrices + jifenPrefer + fenbiPrefer;
	request.setAttribute( "sumPrefer", sumPrefer );

	if ( price < 0 ) {
	    price = 0;
	    discountedPrices = 0;
	}
	od.setOrderId( orderId );
	od.setDetProName( map.get( "pro_name" ).toString() );
	od.setDetProCode( proCode.toString() );
	od.setDetProNum( num );
	od.setTotalPrice( price );
	od.setDetProPrice( new BigDecimal( price / num ) );
	od.setProductId( Integer.parseInt( map.get( "product_id" ).toString() ) );
	od.setProductImageUrl( map.get( "image_url" ).toString() );
	od.setProductSpecificas( productSpecificas.toString() );
	od.setShopId( Integer.parseInt( shopId ) );
	od.setDetProMessage( proMessage.toString() );
	od.setDetPrivivilege( new BigDecimal( primaryPrice ) );
	od.setProductSpeciname( productSpeciname.toString() );
	od.setReturnDay( Integer.parseInt( map.get( "return_day" ).toString() ) );
	od.setDiscount( discount );
	od.setCouponCode( couponCode );
	od.setDiscountedPrices( new BigDecimal( discountedPrices ) );
	od.setCreateTime( new Date() );
	od.setProTypeId( Integer.parseInt( map.get( "pro_type_id" ).toString() ) );

	if ( CommonUtil.isNotEmpty( map.get( "cardReceiveId" ) ) ) {
	    od.setCardReceiveId( CommonUtil.toInteger( map.get( "cardReceiveId" ) ) );
	} else {
	    od.setCardReceiveId( 0 );
	}

	if ( CommonUtil.isNotEmpty( duofenCoupon ) ) {
	    if ( couponCode.contains( "," ) ) {
		couponCode = couponCode.substring( 0, couponCode.length() - 1 );//多粉优惠券的code截掉最后的逗号，再保存
	    }
	    od.setCouponCode( couponCode );
	    od.setDuofenCoupon( duofenCoupon );
	}
	if ( CommonUtil.isNotEmpty( map.get( "saleMemberId" ) ) && CommonUtil.isNotEmpty( map.get( "commission" ) ) ) {
	    int saleMemberId = CommonUtil.toInteger( map.get( "saleMemberId" ) );
	    double commission = CommonUtil.toDouble( map.get( "commission" ) );
	    if ( saleMemberId > 0 ) {
		//判断销售员是否拥有该商品
		boolean isSeller = mallSellerMallsetService.isSellerProduct( od.getProductId(), saleMemberId );
		if ( isSeller ) {
		    od.setSaleMemberId( saleMemberId );
		    od.setCommission( BigDecimal.valueOf( commission ) );
		}
	    }

	}
	MallProduct product = mallProductDAO.selectById( od.getProductId() );
	if ( CommonUtil.isNotEmpty( product.getFlowId() ) ) {
	    od.setFlowId( product.getFlowId() );
	}
	if ( CommonUtil.isNotEmpty( product.getFlowRecordId() ) ) {
	    od.setFlowRecordId( product.getFlowRecordId() );
	}
	return od;
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
	MallOrder order = mallOrderDAO.selectById( CommonUtil.toInteger( params.get( "orderId" ) ) );//查询订单信息
	Member member = memberService.findMemberById( order.getBuyerUserId(), null );//根据粉丝id查询粉丝信息
	String memberName = "";
	String memberPhone = "";
	String address = "";
	String shopName = "";
	//查询店铺名称
	if ( CommonUtil.isNotEmpty( order.getShopId() ) ) {
	    MallStore store = mallStoreDAO.selectById( order.getShopId() );
	    if ( CommonUtil.isNotEmpty( store ) ) {
		try {
		    WsWxShopInfo shopInfo = wxShopService.getShopById( store.getWxShopId() );
		    if ( CommonUtil.isNotEmpty( shopInfo ) ) {
			shopName = shopInfo.getBusinessName();
		    }
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
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = CommonUtil.isEmpty( params.get( "pageSize" ) ) ? 5 : CommonUtil.toInteger( params.get( "pageSize" ) );
	int rowCount = mallOrderDetailDAO.countByOrderId( params );
	PageUtil page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ), pageSize, rowCount, "mallOrder/toIndex.do" );
	params.put( "firstResult", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	List< MallOrderDetail > list = mallOrderDetailDAO.selectPageByOrderId( params );

	Map< String,Object > resultMap = new HashMap<>();
	resultMap.put( "orderName", "商城订单" );//订单名
	resultMap.put( "totalPage", page.getPageCount() );//总页数
	resultMap.put( "curPage", page.getCurPage() );//当前页数
	resultMap.put( "phone", user.getPhone() );//手机号/商家联系方式
	if ( CommonUtil.isNotEmpty( memberName ) ) {
	    resultMap.put( "memberName", memberName );//客户名称
	}
	if ( CommonUtil.isNotEmpty( memberPhone ) ) {
	    resultMap.put( "memberPhone", memberPhone );//客户电话
	}
	if ( CommonUtil.isNotEmpty( address ) ) {
	    resultMap.put( "memberAddress", address );//客户地址
	}
	resultMap.put( "store", shopName );//所属店铺
	resultMap.put( "orderNum", order.getOrderNo() );//订单编号
	resultMap.put( "orderTime", DateTimeKit.formatDateByFormat( order.getCreateTime(), "yyyy-MM-dd HH:mm:ss" ) );//下单时间
	if ( CommonUtil.isNotEmpty( order.getOrderBuyerMessage() ) ) {
	    resultMap.put( "message", order.getOrderBuyerMessage() );//买家留言
	}
	resultMap.put( "totalAmount", order.getOrderMoney() );//应收金额
	resultMap.put( "deliveryType", order.getDeliveryMethod() == 1 ? "快递配送" : "上门自提" );//配送方式
	if ( CommonUtil.isNotEmpty( order.getOrderSellerRemark() ) ) {
	    resultMap.put( "remark", order.getOrderSellerRemark() );//商家备注
	}
	List< Map< String,Object > > detailList = new ArrayList<>();
	if ( list != null && list.size() > 0 ) {
	    for ( MallOrderDetail detail : list ) {
		Map< String,Object > map = new HashMap<>();
		//map.put("barCode", "");//条形码
		map.put( "name", detail.getDetProName() );//商品名称
		map.put( "amount", detail.getDetPrivivilege() );//商品原价
		map.put( "num", detail.getDetProNum() );//商品数量
		map.put( "disount", detail.getDiscountedPrices() );//优惠
		double totalPrice = detail.getDetProNum() * CommonUtil.toDouble( detail.getDetProPrice() );
		DecimalFormat df = new DecimalFormat( "######0.00" );
		map.put( "subtotal", df.format( totalPrice ) );//小计
		detailList.add( map );
	    }
	    resultMap.put( "lists", detailList );
	}
	//页面用到的参数
	resultMap.put( "nextPage", page.getNextPage() );
	resultMap.put( "prevPage", page.getPrevPage() );
	return resultMap;
    }

    @Override
    public Map< String,Object > getMemberParams( Member member, Map< String,Object > params ) {
	List< Integer > memberList = memberService.findMemberListByIds( member.getId() );
	if ( memberList != null && memberList.size() > 1 ) {
	    params.put( "oldMemberIds", memberList );
	} else {
	    params.put( "memberId", member.getId() );
	}
	return params;
    }

    @Override
    public void clearSession( HttpServletRequest request ) {
	Object orderObj = request.getSession().getAttribute( "to_order" );
	Object payWayObj = request.getSession().getAttribute( "orderpayway" );
	Object payWayNameObj = request.getSession().getAttribute( "orderpaywayname" );
	Object dataObj = request.getSession().getAttribute( "dataOrder" );
	Object addTypeObj = request.getSession().getAttribute( "addressType" );
	Object deliveryMethodObj = request.getSession().getAttribute( "deliveryMethod" );
	if ( CommonUtil.isNotEmpty( orderObj ) ) {
	    request.getSession().removeAttribute( "to_order" );
	}
	if ( CommonUtil.isNotEmpty( payWayObj ) ) {
	    request.getSession().removeAttribute( "orderpayway" );
	}
	if ( CommonUtil.isNotEmpty( payWayNameObj ) ) {
	    request.getSession().removeAttribute( "orderpaywayname" );
	}
	if ( CommonUtil.isNotEmpty( dataObj ) ) {
	    request.getSession().removeAttribute( "dataOrder" );
	}
	if ( CommonUtil.isNotEmpty( addTypeObj ) ) {
	    request.getSession().removeAttribute( "addressType" );
	}
	if ( CommonUtil.isNotEmpty( deliveryMethodObj ) ) {
	    request.getSession().removeAttribute( "deliveryMethod" );
	}

	String ip = IPKit.getRemoteIP( request );
	String key2 = Constants.REDIS_KEY + "add_order_" + ip;
	if ( CommonUtil.isNotEmpty( JedisUtil.exists( key2 ) ) ) {
	    JedisUtil.del( key2 );
	}

    }

    @Override
    public boolean isJuliByFreight( String shopIds ) {
	boolean isJuli = false;
	String[] shopStr = shopIds.split( "," );
	if ( CommonUtil.isEmpty( shopStr ) && shopStr.length > 0 ) {
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
    public List< Map< String,Object > > selectIntegralOrder( Map< String,Object > params ) {
	List< Map< String,Object > > orderList = new ArrayList<>();
	List< Map< String,Object > > list = mallOrderDAO.selectIntegralOrder( params );
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
	return orderList;
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
		    if ( CommonUtil.isNotEmpty( ids ) ) {
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
			    result.put( "msg", "库存不够" );
			} else {
			    result.put( "result", true );
			}
		    } else {
			result.put( "result", false );
			result.put( "msg", "库存不够" );
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
		result.put( "msg", "库存不够" );
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

}
