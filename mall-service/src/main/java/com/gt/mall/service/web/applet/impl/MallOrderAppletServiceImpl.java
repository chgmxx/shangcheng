package com.gt.mall.service.web.applet.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusFlow;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.applet.MallAppletImageDAO;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.groupbuy.MallGroupBuyDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.order.MallOrderReturnDAO;
import com.gt.mall.dao.product.MallShopCartDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.service.inter.DictService;
import com.gt.mall.service.inter.MemberService;
import com.gt.mall.service.web.applet.MallOrderAppletService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.util.*;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.order.MallOrderService;
import com.gt.mall.service.web.seckill.MallSeckillService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 小程序图片表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallOrderAppletServiceImpl extends BaseServiceImpl< MallAppletImageDAO,MallAppletImage > implements MallOrderAppletService {

    @Autowired
    private MallOrderDAO            orderDAO;
    @Autowired
    private MallGroupBuyDAO         groupBuyDAO;
    @Autowired
    private MallStoreDAO            storeDAO;
    @Autowired
    private MallOrderDetailDAO      orderDetailDAO;
    @Autowired
    private MallOrderReturnDAO      orderReturnDAO;
    @Autowired
    private MallShopCartDAO         shopCartDAO;
    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;

    @Autowired
    private MallGroupBuyService         groupBuyService;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MallProductService          productService;
    @Autowired
    private MallSeckillService          seckillService;
    @Autowired
    private MallOrderService            orderService;
    @Autowired
    private MallFreightService          freightService;
    @Autowired
    private MallPaySetService           paySetService;
    @Autowired
    private MemberService               memberService;
    @Autowired
    private MallPageService             pageService;
    @Autowired
    private DictService                 dictService;

    @Override
    public PageUtil getOrderList( Map< String,Object > params ) {
	List< Integer > memberList = memberService.findMemberListByIds( CommonUtil.toInteger( params.get( "memberId" ) ) );//查询会员信息
	if ( memberList != null && memberList.size() > 0 ) {
	    params.put( "oldMemberIds", memberList );
	}
	params.put( "memberId", params.get( "memberId" ) );

	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
	    if ( CommonUtil.toString( params.get( "type" ) ).equals( "0" ) ) {
		params.remove( "type" );
	    }
	}
	int countOrder = orderDAO.countMobileOrderList( params );
	PageUtil page = new PageUtil( curPage, pageSize, countOrder, "" );
	params.put( "firstNum", pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxNum", pageSize );

	List< Map< String,Object > > orderList = new ArrayList< Map< String,Object > >();
	List< MallOrder > list = orderDAO.mobileOrderList( params );
	if ( list != null && list.size() > 0 ) {
	    for ( MallOrder order : list ) {

		Map< String,Object > map = getOrderParam( order );
		if ( CommonUtil.isNotEmpty( map ) ) {
		    orderList.add( map );
		}
	    }
	}
	page.setSubList( orderList );
	return page;
    }

    private Map< String,Object > getOrderParam( MallOrder order ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();

	SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	String orderType = CommonUtil.toString( order.getOrderType() );
	String orderPayWay = CommonUtil.toString( order.getOrderPayWay() );
	String orderStatus = CommonUtil.toString( order.getOrderStatus() );
	String deliveryMethod = CommonUtil.toString( order.getDeliveryMethod() );
	JSONArray detailArr = new JSONArray();
	JSONArray arr = JSONArray.fromObject( order.getMallOrderDetail() );
	int isShouHuo = 0;//是否能显示收货按钮
	int groupBuyId = CommonUtil.toInteger( order.getGroupBuyId() );
	int shopId = 0;
	MallGroupBuy buy = null;
	if ( arr != null && orderType.equals( "1" ) ) {
	    buy = groupBuyDAO.selectById( CommonUtil.toInteger( order.getGroupBuyId() ) );
	}
	if ( order.getOrderStatus() == 3 ) {
	    isShouHuo = 1;
	}
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
	for ( Object dObject2 : arr ) {
	    JSONObject dObj = JSONObject.fromObject( dObject2 );
	    Map< String,Object > detailMap = new HashMap< String,Object >();
	    int productId = dObj.getInt( "productId" );
	    shopId = dObj.getInt( "shopId" );
	    String proTypeId = CommonUtil.toString( dObj.get( "proTypeId" ) );
	    int cardReceiveId = 0;
	    int groupIsReturn = groupBuyService.groupIsReturn( groupBuyId, orderType, order.getId(), dObj.get( "id" ), buy );
	    if ( CommonUtil.isNotEmpty( dObj.get( "cardReceiveId" ) ) ) {
		cardReceiveId = CommonUtil.toInteger( dObj.get( "cardReceiveId" ) );
	    }
	    /*if(buy != null){
		Map<String, Object> params = new HashMap<String, Object>();
				params.put("orderId", orderMap.get("id"));
				params.put("orderDetailId", dObj.get("id"));
				params.put("groupBuyId", buy.getId());
				//查询是否已成团
				Map<String, Object> joinMap = mOrderMapper.groupJoinPeopleNum(params);
				if(joinMap != null){
					int count = CommonUtil.toInteger(joinMap.get("num"));
					//团购凑齐人允许退款
					if(count >= buy.getgPeopleNum()){
						groupIsReturn = 0;
					}else{//拼团人数没达到不允许退款
						groupIsReturn = 1;
					}
				}
			}*/
	    //String url = "/mallApplet/79B4DE7C/phoneProduct.do?productId="+productId+"&shopId="+shopId;
	    String proUnit = "";
	    if ( orderPayWay.equals( "4" ) ) {//积分兑换商品
		//url = "/mallApplet/79B4DE7C/phoneProduct.do?rType=1&productId="+productId+"&shopId="+shopId;
		proUnit = "积分";
	    } else if ( orderPayWay.equals( "8" ) ) {//粉币兑换商品
		//url = "/mallApplet/79B4DE7C/phoneProduct.do?rType=2&productId="+productId+"&shopId="+shopId;
		proUnit = "粉币";
	    }
	    /*if(orderType.equals("4")){// 拍卖的商品，直接跳转到拍卖详情
				url = "/mallApplet/79B4DE7C/auctiondetail.do?productId="+productId+"&shopId="+shopId+"&auctionId="+orderMap.get("groupBuyId");
			}else if(CommonUtil.isNotEmpty(dObj.get("saleMemberId")) && !orderType.equals("4")){
				int saleMemberId = CommonUtil.toInteger(dObj.get("saleMemberId"));
				if(saleMemberId > 0){
					url += "&saleMemberId="+saleMemberId;
				}
			}*/
	    int status = -3;
			/*String statusMsg = "";*/
	    int isReturn = 1;//是否可以退款  1可以退款  0 不可以退款
	    if ( CommonUtil.isNotEmpty( dObj.get( "status" ) ) ) {
		status = CommonUtil.toInteger( dObj.get( "status" ) );
	    }

			/*if(status == 0){
				statusMsg = "退款中";
			}else if(status == 1 || status == 5){
			}else if(status == -1){
				statusMsg = "卖家不同意退款";
			}else if(status == -2){
				statusMsg = "退款已撤销";
			}else if(status == 2){
				statusMsg = "商家已同意退款退货,请退货给商家";
			}else if(status == 3){
				statusMsg = "已退货等待商家确认收货";
			}else if(status == 4){
				statusMsg = "商家未收到货，不同意退款申请";
			}else if(status == 0){
				statusMsg = "退款中";
			}*/

	    if ( status != 1 && status != 5 && status != -2 && status != -3 ) {
		isShouHuo = 0;
		statusName = "退款中";
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
	    if ( ( proTypeId.equals( "3" ) && cardReceiveId > 0 ) || proTypeId.equals( "2" ) || proTypeId.equals( "4" ) ) {//卡券购买,会员卡支付 不可以退款
		isReturn = 0;
	    }
	    int updateDay = 0;
	    if ( CommonUtil.isNotEmpty( order.getUpdateDay() ) ) {
		updateDay = order.getUpdateDay();
	    }
	    int isReturnButton = 0;
	    int isUpdateButton = 0;
	    int isCancelButton = 0;
	    int isWuliuButton = 0;
	    int returnDay = CommonUtil.toInteger( dObj.get( "returnDay" ) );
	    if ( isReturn == 1 && groupIsReturn == 0 ) {
		if ( ( ( orderStatus.equals( "2" ) || orderStatus.equals( "3" ) ) && status == -3 ) || ( updateDay > 0 && updateDay < returnDay && orderStatus.equals( "4" )
				&& status == -3 ) ) {
		    isReturnButton = 1;//展示申请退款的按钮
		}
		if ( status == -1 ) {
		    isUpdateButton = 1;//展示修改退款的按钮
		}
		if ( ( status == 0 || status == 2 || status == 3 || status == 4 || status == -1 ) && CommonUtil.isNotEmpty( dObj.get( "orderReturn" ) ) ) {
		    isCancelButton = 1;//展示撤销退款的按钮
		    if ( status == 2 || status == 4 ) {
			isWuliuButton = 1;//展示填写物流的按钮
		    }
		}
	    }
	    detailMap.put( "isReturnButton", isReturnButton );
	    detailMap.put( "isUpdateButton", isUpdateButton );
	    detailMap.put( "isCancelButton", isCancelButton );
	    detailMap.put( "isWuliuButton", isWuliuButton );

	    detailMap.put( "detail_id", dObj.get( "id" ) );
	    detailMap.put( "product_id", productId );
	    detailMap.put( "shop_id", shopId );
	    detailMap.put( "product_name", dObj.get( "detProName" ) );
	    detailMap.put( "product_image", PropertiesUtil.getResourceUrl() + dObj.get( "productImageUrl" ) );
	    detailMap.put( "product_price", dObj.get( "detProPrice" ) );
	    detailMap.put( "product_num", dObj.get( "detProNum" ) );
	    if ( CommonUtil.isNotEmpty( dObj.get( "productSpecificas" ) ) ) {
		Map< String,Object > invPriceMap = productService.getProInvIdBySpecId( CommonUtil.toString( dObj.get( "productSpecificas" ) ), productId );
		if ( CommonUtil.isNotEmpty( invPriceMap ) ) {
		    if ( CommonUtil.isNotEmpty( invPriceMap.get( "specifica_values" ) ) ) {
			String speciname = invPriceMap.get( "specifica_values" ).toString();
			speciname = speciname.replaceAll( ",", "/" );
			detailMap.put( "product_specifica_name", speciname );
		    }
		}
	    }

	    //detailMap.put("product_url", url);
	    detailMap.put( "product_unit", proUnit );

	    detailMap.put( "isReturn", isReturn );
			/*detailMap.put("statusMsg", statusMsg);*/
	    detailMap.put( "proUnit", proUnit );
	    if ( orderPayWay.equals( "5" ) ) {
		detailMap.put( "product_name", "扫码支付" );
		detailMap.put( "product_num", 1 );
		detailMap.put( "product_price", order.getOrderMoney() );
	    }
	    if ( dObj.getInt( "id" ) > 0 || orderPayWay.equals( "5" ) ) {
		detailArr.add( detailMap );
	    }
	}
		/*if(detailArr == null || detailArr.size() == 0){
			detailArr.addAll(arr);
		}*/

	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "id", shopId );
	//TODO 需调用 storeDAO.selectByStoreId(map);方法
	Map< String,Object > storeMap = null;
	//                storeDAO.selectByStoreId(params);
	if ( CommonUtil.isNotEmpty( storeMap ) ) {
	    resultMap.put( "store_name", storeMap.get( "business_name" ) );
	    resultMap.put( "store_image", storeMap.get( "shopPicture" ) );
	}
	resultMap.put( "order_id", order.getId() );
	resultMap.put( "order_no", order.getOrderNo() );
	resultMap.put( "status_name", statusName );
	resultMap.put( "order_money", order.getOrderMoney() );
	resultMap.put( "order_freight_money", order.getOrderFreightMoney() );

	int isGoPay = 0;
	if ( order.getOrderStatus() == 1 && order.getOrderPayWay() == 10 ) {
	    isGoPay = 1;
	}
	resultMap.put( "isGoPay", isGoPay );
	resultMap.put( "isShouHuo", isShouHuo );
	resultMap.put( "mallOrderDetail", detailArr );

	Date date = order.getCreateTime();
	resultMap.put( "create_time", format.format( date ) );
	resultMap.put( "order_pay_way", orderPayWay );
	if ( ( detailArr == null || detailArr.size() == 0 ) && !orderPayWay.equals( "5" ) ) {
	    return null;
	}

	return resultMap;
    }

    @Override
    public Map< String,Object > getOrderDetail( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();

	SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );//时间格式化
	DecimalFormat df = new DecimalFormat( "######0.00" );//价钱格式化

	int orderId = CommonUtil.toInteger( params.get( "orderId" ) );

	MallOrder order = orderDAO.selectById( orderId );
	if ( CommonUtil.isNotEmpty( order ) ) {
	    String orderStatus = order.getOrderStatus().toString();
	    String deliveryMethod = order.getDeliveryMethod().toString();
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

	    resultMap.put( "order_no", order.getOrderNo() );
	    resultMap.put( "order_time", dateFormat.format( new Date() ) );

	    if ( CommonUtil.isNotEmpty( order.getReceiveId() ) ) {
		params.put( "id", order.getReceiveId() );
		List< Map< String,Object > > list = orderService.selectShipAddress( params );
		if ( list != null && list.size() > 0 ) {
		    Map< String,Object > addressMap = getAddressParams( list.get( 0 ) );
		    resultMap.putAll( addressMap );
		}
	    }
	    if ( CommonUtil.isNotEmpty( order.getExpressId() ) ) {
		String expressName = dictService.getDictRuturnValue( "1092", order.getExpressId() );
		resultMap.put( "expressName", expressName );

	    }
	    if ( CommonUtil.isNotEmpty( order.getExpressNumber() ) ) {
		resultMap.put( "expressNumber", order.getExpressNumber() );
	    }

	    //查询店铺信息
	    params.put( "id", order.getShopId() );
	    //TODO 需调用 storeDAO.selectByStoreId(map);方法
	    Map< String,Object > storeMap = null;
	    //                    storeMapper.selectByStoreId(params);
	    if ( CommonUtil.isNotEmpty( storeMap ) ) {
		resultMap.put( "shop_name", storeMap.get( "business_name" ) );
		if ( CommonUtil.isNotEmpty( storeMap.get( "shopPicture" ) ) ) {
		    resultMap.put( "shop_image", PropertiesUtil.getResourceUrl() + storeMap.get( "shopPicture" ) );
		} else {
		    resultMap.put( "shop_image", PropertiesUtil.getResourceUrl() + storeMap.get( "stoPicture" ) );
		}
	    }
	    String orderPayWay = order.getOrderPayWay().toString();
	    MallGroupBuy buy = null;
	    if ( order.getOrderType() == 1 ) {
		buy = groupBuyDAO.selectById( order.getGroupBuyId() );
	    }
	    int updateDay = 0;
	    if ( CommonUtil.isNotEmpty( order.getUpdateDay() ) ) {
		updateDay = order.getUpdateDay();
	    }
	    int isShouHuo = 0;

	    //查询订单详情信息
	    params.put( "id", order.getId() );
	    if ( order.getOrderStatus() == 3 ) {
		isShouHuo = 1;
	    }
	    List< Map< String,Object > > detailList = new ArrayList< Map< String,Object > >();
	    List< MallOrderDetail > list = orderDetailDAO.selectByOrderId( params );
	    if ( list != null && list.size() > 0 ) {
		for ( MallOrderDetail detail : list ) {
		    Map< String,Object > detailMap = new HashMap< String,Object >();
		    detailMap.put( "id", detail.getId() );
		    detailMap.put( "product_id", detail.getProductId() );
		    detailMap.put( "shop_id", detail.getShopId() );
		    detailMap.put( "product_image", PropertiesUtil.getResourceUrl() + detail.getProductImageUrl() );
		    detailMap.put( "product_name", detail.getDetProName() );
		    if ( detail.getTotalPrice() > 0 ) {
			detailMap.put( "product_price", detail.getTotalPrice() );
		    } else {
			double totalPrice = detail.getDetProNum() * CommonUtil.toDouble( detail.getDetProPrice() );
			detailMap.put( "product_price", df.format( totalPrice ) );
		    }
					/*MallProduct product = mallProductService.selectByPrimaryKey(detail.getProductId());
					if(CommonUtil.isNotEmpty(product.getIsDelete()) && CommonUtil.isNotEmpty(product.getCheckStatus()) && CommonUtil.isNotEmpty(product.getIsPublish())){
						if(!product.getIsDelete().toString().equals("0") || !product.getCheckStatus().toString().equals("1")
								|| !product.getIsPublish().toString().equals("1")){
							detailMap.put("noShow", 1);//不显示商品详细页面
						}
					}*/
		    //resultMap.put("id", detail.getId());
		    detailMap.put( "product_num", detail.getDetProNum() );
		    int groupIsReturn = 0;
		    if ( buy != null ) {
			Map< String,Object > joinParams = new HashMap< String,Object >();
			joinParams.put( "orderId", order.getId() );
			joinParams.put( "orderDetailId", detail.getId() );
			joinParams.put( "groupBuyId", buy.getId() );
			//查询是否已成团
			Map< String,Object > joinMap = orderDAO.groupJoinPeopleNum( joinParams );
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
		    int status = detail.getStatus();
					/*String statusMsg = "";*/
		    int isReturn = 1;//是否可以退款  1可以退款  0 不可以退款
		    String proTypeId = detail.getProTypeId().toString();

		    if ( status != 1 && status != 5 && status != -2 && status != -3 ) {
			isShouHuo = 0;
			statusName = "退款中";
		    }

		    if ( orderStatus.equals( "5" ) || orderPayWay.equals( "5" ) || orderPayWay.equals( "4" ) || orderPayWay.equals( "8" ) ) {//扫码支付、积分支付、粉币支付  不可以退款
			isReturn = 0;
		    }

		    if ( ( proTypeId.equals( "3" ) && detail.getCardReceiveId() > 0 ) || proTypeId.equals( "2" ) ) {//卡券购买,会员卡支付 不可以退款
			isReturn = 0;
		    }
		    MallOrderReturn returns = null;
		    if ( status != 3 ) {
			MallOrderReturn orderReturn = new MallOrderReturn();
			orderReturn.setOrderId( order.getId() );
			orderReturn.setOrderDetailId( detail.getId() );
			returns = orderReturnDAO.selectByOrderDetailId( orderReturn );
			if ( CommonUtil.isNotEmpty( returns ) ) {
			    detailMap.put( "return_id", returns.getId() );
			}
		    }
		    int returnDay = 0;
		    if ( CommonUtil.isNotEmpty( detail.getReturnDay() ) ) {
			returnDay = detail.getReturnDay();
		    }
		    int isReturnButton = 0;
		    int isUpdateButton = 0;
		    int isCancelButton = 0;
		    int isWuliuButton = 0;
		    if ( isReturn == 1 && groupIsReturn == 0 ) {
			if ( ( ( orderStatus.equals( "2" ) || orderStatus.equals( "3" ) ) && status == -3 ) || ( updateDay > 0 && updateDay < returnDay && orderStatus.equals( "4" )
					&& status == -3 ) ) {
			    isReturnButton = 1;//展示申请退款的按钮
			}
			if ( status == -1 ) {
			    isUpdateButton = 1;//展示修改退款的按钮
			}
			if ( ( status == 0 || status == 2 || status == 3 || status == 4 || status == -1 ) && CommonUtil.isNotEmpty( returns ) ) {
			    isCancelButton = 1;//展示撤销退款的按钮
			    if ( status == 2 || status == 4 ) {
				isWuliuButton = 1;//展示填写物流的按钮
			    }
			}

		    }
		    detailMap.put( "isReturnButton", isReturnButton );
		    detailMap.put( "isUpdateButton", isUpdateButton );
		    detailMap.put( "isCancelButton", isCancelButton );
		    detailMap.put( "isWuliuButton", isWuliuButton );

		    if ( CommonUtil.isNotEmpty( detail.getProductSpecificas() ) ) {
			Map< String,Object > invPriceMap = productService.getProInvIdBySpecId( detail.getProductSpecificas(), detail.getProductId() );
			if ( CommonUtil.isNotEmpty( invPriceMap ) ) {
			    if ( CommonUtil.isNotEmpty( invPriceMap.get( "specifica_values" ) ) ) {
				String speciname = invPriceMap.get( "specifica_values" ).toString();
				speciname = speciname.replaceAll( ",", "/" );
				detailMap.put( "product_speciname", speciname );
			    }
			}
		    }
		    detailList.add( detailMap );
		}
	    }
	    resultMap.put( "status_name", statusName );
	    resultMap.put( "pro_total_num", detailList.size() );
	    BigDecimal freightMoney = order.getOrderFreightMoney();
	    resultMap.put( "product_totalprice", df.format( order.getOrderMoney().subtract( freightMoney ) ) );
	    resultMap.put( "freight_money", freightMoney );
	    resultMap.put( "order_money", order.getOrderMoney() );
	    resultMap.put( "detailList", detailList );

	    int isGoPay = 0;
	    if ( order.getOrderStatus() == 1 && order.getOrderPayWay() == 10 ) {
		isGoPay = 1;
	    }
	    resultMap.put( "isGoPay", isGoPay );
	    resultMap.put( "isShouHuo", isShouHuo );
	    resultMap.put( "id", order.getId() );
	}

	return resultMap;
    }

    @Override
    public Map< Object,Object > orderGoPay( Map< String,Object > params, String url ) throws Exception {
	Map< Object,Object > resultMap = new HashMap< Object,Object >();
	int error = 1;
	String msg = "";
	if ( CommonUtil.isEmpty( params.get( "order_id" ) ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整：缺少订单id" );
	    return resultMap;
	}
	int orderId = CommonUtil.toInteger( params.get( "order_id" ) );

	MallOrder order = orderDAO.getOrderById( orderId );
	if ( CommonUtil.isNotEmpty( order ) ) {
	    MallOrder newOrder = new MallOrder();
	    newOrder.setId( order.getId() );
	    String orderNo = "SC" + System.currentTimeMillis();
	    newOrder.setOrderNo( orderNo );
	    if ( order.getOrderPayWay() == 1 ) {
		newOrder.setOrderPayWay( 10 );
	    }

	    orderDAO.upOrderNoById( newOrder );
	    order.setOrderNo( orderNo );

	    if ( order.getOrderType() == 3 ) {//秒杀订单
		JSONObject detailObj = new JSONObject();
		String key = Constants.REDIS_KEY + "hSeckill_nopay";//秒杀用户(用于没有支付，恢复库存用)
		if ( JedisUtil.hExists( key, order.getId().toString() ) ) {
		    detailObj.put( "groupBuyId", order.getGroupBuyId() );
		    //判断秒杀订单是否正在进行
		    MallSeckill seckill = seckillService.selectById( order.getGroupBuyId() );
		    if ( CommonUtil.isNotEmpty( seckill ) ) {
			if ( seckill.getIsDelete().toString().equals( "1" ) ) {
			    error = -1;
			    msg = "您购买的秒杀商品已经被删除，请重新下单";
			} else if ( seckill.getIsUse().toString().equals( "-1" ) ) {
			    error = -1;
			    msg = "您购买的秒杀商品已经被失效，请重新下单";
			} else if ( seckill.getStatus() == 0 ) {
			    error = -1;
			    msg = "您购买的秒杀商品还没开始，请耐心等待";
			} else if ( seckill.getStatus() == -1 ) {
			    error = -1;
			    msg = "您购买的秒杀商品已经结束，请重新下单";
			}
		    } else {
			error = -1;
			msg = "您购买的秒杀商品已经被删除，请重新下单";
		    }
		} else {
		    error = -1;
		    msg = "您的订单已关闭，请重新下单";
		}

	    } else {
		if ( CommonUtil.isNotEmpty( order.getMallOrderDetail() ) ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			if ( CommonUtil.isNotEmpty( order.getOrderType() ) && order.getOrderType().toString().equals( "7" ) && CommonUtil
					.isNotEmpty( detail.getProSpecStr() ) ) {//批发商品判断库存
			    JSONObject map = JSONObject.fromObject( detail.getProSpecStr() );
			    for ( Object key : map.keySet() ) {
				String proSpecificas = key.toString();
				JSONObject p = JSONObject.fromObject( map.get( key ) );
				int proNum = CommonUtil.toInteger( p.get( "num" ) );
				Map< String,Object > result = productService.calculateInventory( detail.getProductId(), proSpecificas, proNum, order.getBuyerUserId() );
				if ( CommonUtil.isNotEmpty( result.get( "msg" ) ) ) {
				    msg = result.get( "msg" ).toString();
				}
				if ( CommonUtil.toString( result.get( "result" ) ).equals( "false" ) ) {
				    error = -1;
				    break;
				}
			    }
			} else {
			    Map< String,Object > result = productService
					    .calculateInventory( detail.getProductId(), detail.getProductSpecificas(), detail.getDetProNum(), order.getBuyerUserId() );
			    if ( CommonUtil.isNotEmpty( result.get( "msg" ) ) ) {
				msg = result.get( "msg" ).toString();
			    }
			    if ( CommonUtil.toString( result.get( "result" ) ).equals( "false" ) ) {
				error = -1;
				break;
			    }
			}

		    }
		}
	    }
	    resultMap.put( "out_trade_no", order.getOrderNo() );
	}
	resultMap.put( "code", error );
	if ( error < 0 && msg.equals( "" ) ) {
	    msg = "去支付失败";
	} else {
	    if ( order.getOrderPayWay() == 10 ) {
		Map< String,Object > appletParam = new HashMap<>();
		appletParam.put( "memberId", params.get( "memberId" ) );
		appletParam.put( "orderNo", order.getOrderNo() );
		appletParam.put( "appid", params.get( "appid" ) );
		Map< Object,Object > parameters = appletWxOrder( appletParam, url );
		resultMap.putAll( parameters );
	    }

	}

	resultMap.put( "code", error );
	resultMap.put( "errorMsg", msg );
	return resultMap;
    }

    @Override
    public Map< String,Object > confirmReceipt( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	if ( CommonUtil.isEmpty( params.get( "order_id" ) ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整：缺少订单id" );
	    return resultMap;
	}
	params.put( "status", 4 );//已完成订单
	params.put( "orderId", params.get( "order_id" ) );
	int count = orderService.upOrderNoOrRemark( params );
	if ( count > 0 ) {
	    resultMap.put( "code", 1 );
	} else {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "确认收货失败" );
	}
	return resultMap;
    }

    @Override
    public Map< String,Object > toReturnOrder( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int orderId = CommonUtil.toInteger( params.get( "orderId" ) );
	int orderDetailId = CommonUtil.toInteger( params.get( "orderDetailId" ) );
	//查询订单信息
	MallOrder order = orderDAO.selectById( orderId );
	//查询订单详情信息
	MallOrderDetail detail = orderDetailDAO.selectById( orderDetailId );
	SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );//时间格式化
	DecimalFormat df = new DecimalFormat( "######0.00" );//价钱格式化
	double returnMoney = 0;//保存退款金额
	int orderPayWay = 0;//保存支付方式
	int orderStatus = 0;//保存订单状态
	int isWallet = 0;//保存是否是钱包支付
	if ( CommonUtil.isNotEmpty( detail ) ) {
	    resultMap.put( "product_name", detail.getDetProName() );
	    if ( detail.getTotalPrice() > 0 ) {
		returnMoney = detail.getTotalPrice();
	    } else {
		returnMoney = detail.getDetProNum() * CommonUtil.toDouble( detail.getDetProPrice() );
	    }
	}
	if ( CommonUtil.isNotEmpty( order ) ) {
	    orderPayWay = order.getOrderPayWay();
	    orderStatus = order.getOrderStatus();
	    isWallet = order.getIsWallet();
	    resultMap.put( "order_money", order.getOrderMoney() );
	    resultMap.put( "order_no", order.getOrderNo() );
	    if ( CommonUtil.isNotEmpty( order.getPayTime() ) ) {
		resultMap.put( "pay_time", dateFormat.format( order.getPayTime() ) );
	    } else {
		resultMap.put( "pay_time", dateFormat.format( order.getCreateTime() ) );
	    }
	    if ( ( orderPayWay == 2 || orderPayWay == 6 ) && order.getIsWallet() == 0 ) {
		returnMoney = 0;
	    }
	}
	resultMap.put( "return_money", df.format( returnMoney ) );
	Integer type = 0;
	if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
	    type = CommonUtil.toInteger( params.get( "type" ) );
	}
	if ( type == 0 ) {//申请退款
	    // 查询退款原因
	    List< Map > reasonList = dictService.getDict( "1091" );
	    resultMap.put( "reasonList", reasonList );
	    List< Map< String,Object > > handWayList = new ArrayList< Map< String,Object > >();//处理方式
	    Map< String,Object > handWayMap = new HashMap< String,Object >();
	    if ( orderPayWay != 2 && orderPayWay != 6 ) {//支付方式不是货到付款和到点支付
		handWayMap.put( "id", 1 );
		handWayMap.put( "value", "我要退款，但不退货" );
		handWayList.add( handWayMap );
		if ( orderStatus == 3 || orderStatus == 4 ) {
		    handWayMap = new HashMap< String,Object >();
		    handWayMap.put( "id", 2 );
		    handWayMap.put( "value", "我要退款退货" );
		    handWayList.add( handWayMap );
		}
	    } else {
		if ( isWallet == 0 && orderStatus == 2 ) {
		    handWayMap = new HashMap< String,Object >();
		    handWayMap.put( "id", 2 );
		    handWayMap.put( "value", "我要退货" );
		    handWayList.add( handWayMap );
		} else if ( isWallet == 1 && orderStatus == 2 ) {
		    handWayMap = new HashMap< String,Object >();
		    handWayMap.put( "id", 1 );
		    handWayMap.put( "value", "我要退款" );
		    handWayList.add( handWayMap );
		}
		if ( orderStatus == 3 || orderStatus == 4 ) {
		    if ( isWallet == 0 ) {
			handWayMap = new HashMap< String,Object >();
			handWayMap.put( "id", 2 );
			handWayMap.put( "value", "我要退货" );
			handWayList.add( handWayMap );
		    } else {
			handWayMap.put( "id", 1 );
			handWayMap = new HashMap< String,Object >();
			handWayMap.put( "value", "我要退款，但不退货" );
			handWayList.add( handWayMap );
		    }
		}
	    }
	    //handWayList.add(handWayMap);
	    resultMap.put( "handWayList", handWayList );
	} else if ( type == 1 ) {//填写物流公司
	    //查询物流公司
	    List< Map > comList = dictService.getDict( "1092" );
	    resultMap.put( "comList", comList );
	}
	//退款id
	if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
	    //查询退款信息
	    MallOrderReturn orderReturn = orderReturnDAO.selectById( CommonUtil.toInteger( params.get( "id" ) ) );
	    if ( type == 1 ) {
		if ( CommonUtil.isNotEmpty( orderReturn.getUpdateTime() ) ) {
		    resultMap.put( "return_time", dateFormat.format( orderReturn.getUpdateTime() ) );
		} else {
		    resultMap.put( "return_time", dateFormat.format( orderReturn.getCreateTime() ) );
		}
		resultMap.put( "return_address", orderReturn.getReturnAddress() );
		resultMap.put( "companyId", orderReturn.getWlCompanyId() );
		resultMap.put( "wlNo", orderReturn.getWlNo() );
		resultMap.put( "wlTelephone", orderReturn.getWlTelephone() );
		resultMap.put( "wlRemark", orderReturn.getWlRemark() );
	    } else {
		resultMap.put( "handWayId", orderReturn.getRetHandlingWay() );//处理方式id
		resultMap.put( "reasonId", orderReturn.getRetReasonId() );//退款原因id
		resultMap.put( "telephone", orderReturn.getRetTelephone() );
		resultMap.put( "remark", orderReturn.getRetRemark() );
	    }
	}

	return resultMap;
    }

    @Override
    public Map< String,Object > submitReturnOrder( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();

	MallOrderReturn orderReturn = (MallOrderReturn) JSONObject.toBean( JSONObject.fromObject( params ), MallOrderReturn.class );
	if ( CommonUtil.isNotEmpty( orderReturn.getWlCompanyId() ) ) {
	    orderReturn.setStatus( 3 );
	} else {
	    orderReturn.setStatus( 0 );
	}
	boolean flag = orderService.addOrderReturn( orderReturn );
	if ( flag ) {
	    resultMap.put( "code", 1 );
	} else {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "提交退款失败" );
	}

	return resultMap;
    }

    @Override
    public Map< String,Object > closeReturnOrder( Map< String,Object > params ) {
	int count = 0;
	Map< String,Object > resultMap = new HashMap< String,Object >();
	MallOrderReturn returns = new MallOrderReturn();
	returns.setOrderId( CommonUtil.toInteger( params.get( "order_id" ) ) );
	returns.setOrderDetailId( CommonUtil.toInteger( params.get( "order_detail_id" ) ) );
	MallOrderReturn orderReturn = orderReturnDAO.selectByOrderDetailId( returns );
	if ( CommonUtil.isNotEmpty( orderReturn ) ) {
	    MallOrderReturn mallOrderReturn = new MallOrderReturn();
	    mallOrderReturn.setStatus( -2 );
	    mallOrderReturn.setId( orderReturn.getId() );
	    count = orderReturnDAO.updateById( mallOrderReturn );
	}
	if ( count > 0 ) {
	    resultMap.put( "code", 1 );

	    MallOrderDetail detail = new MallOrderDetail();
	    detail.setId( orderReturn.getOrderDetailId() );// 修改订单详情的状态
	    detail.setStatus( orderReturn.getStatus() );
	    //orderMapper.updateOrderDetailStatus(detail);
	    orderDetailDAO.updateById( detail );

	} else {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "撤销退款失败" );
	}

	return resultMap;
    }

    @Override
    public Map< String,Object > addressList( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	//TODO 会员信息，地址方法 t_eat_member_address
	//        int memberId = CommonUtil.toInteger(params.get("memberId"));
	//        List<Integer> memberList = memberPayService.findMemberIds(memberId);//查询会员信息
	//        if (memberList != null && memberList.size() > 0) {
	//            params.put("oldMemberIds", memberList);
	//        }
	//        List<Map<String, Object>> addressList = new ArrayList<Map<String, Object>>();
	//        List<Map<String, Object>> list = orderDAO.selectShipAddress(params);
	//        int is_default = 2;
	//        if (list != null && list.size() > 0) {
	//            for (Map<String, Object> map : list) {
	//                Map<String, Object> addressMap = getAddressParams(map);
	//                if (is_default == 2) {
	//                    is_default = CommonUtil.toInteger(addressMap.get("is_default"));
	//                } else if (is_default == 1 && addressMap.get("is_default").toString().equals("1")) {
	//                    addressMap.put("is_default", "2");
	//                    daoUtil.update("UPDATE t_eat_member_address SET mem_default = 2 WHERE id =" + addressMap.get("id"));
	//                }
	//                addressList.add(addressMap);
	//            }
	//        }
	//        resultMap.put("addressList", addressList);
	return resultMap;
    }

    @Override
    public Map< String,Object > addressDefault( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int id = CommonUtil.toInteger( params.get( "id" ) );
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	//TODO 会员信息，地址方法 t_eat_member_address
	//        List<Integer> memberList = memberPayService.findMemberIds(memberId);//查询会员信息
	//        String memberIds = "";
	//        if (memberList != null && memberList.size() > 0) {
	//            for (Integer dfMemberId : memberList) {
	//                if (CommonUtil.isNotEmpty(memberIds)) {
	//                    memberIds += ",";
	//                }
	//                memberIds += dfMemberId;
	//            }
	//        }
	//        daoUtil.update("UPDATE t_eat_member_address SET mem_default = 2 WHERE df_member_id in (" + memberIds + ")");
	//        int count = daoUtil.update("UPDATE t_eat_member_address SET mem_default = 1 WHERE id=?", id);
	//
	//        if (count > 0) {
	//            resultMap.put("code", 1);
	//        } else {
	//            resultMap.put("code", -1);
	//            resultMap.put("errorMsg", "设置默认地址失败");
	//        }

	return resultMap;
    }

    @Override
    public Map< String,Object > addressSubmit( Map< String,Object > params ) {
	//TODO 需关连会员地址方法 basis_city，t_eat_member_address
	//        Map<String, Object> resultMap = new HashMap<String, Object>();
	//
	//        MemberAddress memAddress = (MemberAddress) JSONObject.toBean(JSONObject.fromObject(params), MemberAddress.class);
	//        int count = daoUtil.queryForInt("select count(id) from t_eat_member_address where mem_default = 1 and df_member_id=?", memAddress.getDfMemberId());
	//        if(count == 0){
	//            memAddress.setMemDefault(1);
	//        }
	//        if(CommonUtil.isEmpty(memAddress.getMemLatitude()) || CommonUtil.isEmpty(memAddress.getMemLongitude())){
	//            Map<String, Object> provinceMaps = daoUtil.queryForMap("SELECT id,city_name FROM basis_city WHERE id=?", memAddress.getMemProvince());
	//            Map<String, Object> cityMap = daoUtil.queryForMap("SELECT id,city_name FROM basis_city WHERE id=?", memAddress.getMemCity());
	//            Map<String, Object> areaMap = daoUtil.queryForMap("SELECT id,city_name FROM basis_city WHERE id=?", memAddress.getMemArea());
	//            String address = "";
	//            String city = "";
	//            if(CommonUtil.isNotEmpty(provinceMaps)){
	//                address += provinceMaps.get("city_name").toString();
	//            }
	//            if(CommonUtil.isNotEmpty(cityMap)){
	//                city = cityMap.get("city_name").toString();
	//                address += city;
	//            }
	//            if(CommonUtil.isNotEmpty(areaMap)){
	//                address += areaMap.get("city_name").toString();
	//            }
	//            Map<String, Object> addressMap = storeService.getlnglatByAddress(address+memAddress.getMemAddress(), city);
	//            if(CommonUtil.isNotEmpty(addressMap)){
	//                memAddress.setMemLatitude(addressMap.get("lat").toString());
	//                memAddress.setMemLongitude(addressMap.get("lng").toString());
	//            }
	//        }
	//        Map<String, Object> msg = eatPhoneService.saveOrderAddress(memAddress);
	//
	//        if(msg.get("result").toString().equals("true")){
	//            resultMap.put("code", 1);
	//        }else{
	//            resultMap.put("code", -1);
	//            resultMap.put("errorMsg", "保存地址信息失败");
	//        }
	//        return resultMap;
	return null;
    }

    @Override
    public Map< String,Object > toSubmitOrder( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Member member = memberService.findMemberById( memberId, null );
	int from = 1;
	if ( CommonUtil.isNotEmpty( params.get( "from" ) ) ) {
	    from = CommonUtil.toInteger( params.get( "from" ) );
	}
	DecimalFormat df = new DecimalFormat( "######0.00" );
	//查询用户默认的地址
	Map< String,Object > addressParams = new HashMap< String,Object >();
	Map< String,Object > addressMap = new HashMap< String,Object >();//保存默认地址信息
	List< Integer > memberList = memberService.findMemberListByIds( memberId );//查询会员信息
	if ( memberList != null && memberList.size() > 0 ) {
	    addressParams.put( "oldMemberIds", memberList );
	} else {
	    addressParams.put( "memberId", memberId );
	}
	addressParams.put( "memDefault", 1 );
	List< Map< String,Object > > addressList = orderService.selectShipAddress( addressParams );
	if ( addressList != null && addressList.size() > 0 ) {
	    addressMap = addressList.get( 0 );
	    resultMap.put( "addressMap", getAddressParams( addressMap ) );

	    params.put( "mem_province", addressMap.get( "mem_province" ) );
	}
	resultMap.put( "memberPhone", member.getPhone() );
	double totalProMoney = 0;//	商品总价
	double totalFreightMoney = 0;//运费总价
	int isFlow = 0;//是否是流量充值商品  1是  0 不是

	int totalNum = 0;//商品小计
	int proTypeId = 0;//商品类型

	if ( from == 1 ) {//购物车结算

	    List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	    if ( CommonUtil.isNotEmpty( params.get( "cartIds" ) ) ) {
		JSONArray cartArrs = JSONArray.fromObject( params.get( "cartIds" ) );
		params.put( "cartIds", cartArrs );
	    }
	    List< Map< String,Object > > shopList = shopCartDAO.selectCheckShopByParam( params );
	    if ( shopList != null && shopList.size() > 0 ) {
		for ( Map< String,Object > shopMap : shopList ) {
		    int shopId = CommonUtil.toInteger( shopMap.get( "shop_id" ) );
		    params.put( "shopId", shopId );
		    double pro_weight = 0;
		    double totalPrice = 0;
		    totalNum = 0;
		    List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();
		    List< Map< String,Object > > productList = shopCartDAO.selectCheckCartByParams( params );
		    if ( productList != null && productList.size() > 0 ) {
			for ( Map< String,Object > map : productList ) {
			    Map< String,Object > productMap = new HashMap< String,Object >();
			    productMap.put( "product_name", map.get( "pro_name" ) );
			    productMap.put( "product_image", PropertiesUtil.getResourceUrl() + map.get( "image_url" ) );
			    productMap.put( "product_specificas", map.get( "product_specificas" ) );
			    productMap.put( "product_specificaname", map.get( "product_speciname" ) );
			    productMap.put( "product_num", map.get( "product_num" ) );
			    productMap.put( "product_price", map.get( "price" ) );
			    productMap.put( "primary_price", map.get( "primary_price" ) );
			    productMap.put( "is_member_discount", map.get( "is_member_discount" ) );
			    productMap.put( "is_coupons", map.get( "is_coupons" ) );
			    productMap.put( "is_integral_deduction", map.get( "is_integral_deduction" ) );
			    productMap.put( "is_fenbi_deduction", map.get( "is_fenbi_deduction" ) );
			    productMap.put( "product_id", map.get( "product_id" ) );
			    productMap.put( "product_type_id", map.get( "pro_type_id" ) );
			    totalNum += CommonUtil.toInteger( map.get( "product_num" ) );
			    proTypeId = CommonUtil.toInteger( map.get( "pro_type_id" ) );

			    if ( CommonUtil.isNotEmpty( map.get( "product_specificas" ) ) ) {
				Map< String,Object > invMap = productService
						.getProInvIdBySpecId( CommonUtil.toString( map.get( "product_specificas" ) ), CommonUtil.toInteger( map.get( "product_id" ) ) );
				if ( CommonUtil.isNotEmpty( invMap ) && CommonUtil.isNotEmpty( invMap.get( "specifica_values" ) ) ) {
				    String speciname = invMap.get( "specifica_values" ).toString();
				    speciname = speciname.replaceAll( ",", "/" );
				    productMap.put( "product_specificaname", speciname );
				}
			    }

			    proList.add( productMap );
			    double proTotalPrice = CommonUtil.toDouble( map.get( "price" ) ) * CommonUtil.toInteger( map.get( "product_num" ) );
			    totalPrice += proTotalPrice;
			    //countProPrice += proTotalPrice;

			    totalProMoney += proTotalPrice;

			    pro_weight += CommonUtil.toDouble( map.get( "pro_weight" ) );

			}
		    }
		    params.put( "product_num", totalNum );
		    double freightPrice = freightService.getFreightByProvinces( params, addressMap, shopId, totalPrice, pro_weight );

		    totalFreightMoney += freightPrice;

		    shopMap.put( "freightPrice", freightPrice );
		    shopMap.put( "totalProPrice", df.format( totalPrice ) );

		    shopMap.put( "proList", proList );

		    shopMap.put( "totalNum", totalNum );

		    if ( CommonUtil.isNotEmpty( shopMap.get( "shopPicture" ) ) ) {
			shopMap.put( "sto_image", shopMap.get( "shopPicture" ) );
		    } else {
			shopMap.put( "sto_image", shopMap.get( "stoPicture" ) );
		    }
		    shopMap.put( "sto_name", shopMap.get( "business_name" ) );

		    shopMap.remove( "stoPicture" );
		    shopMap.remove( "shopPicture" );
		    shopMap.remove( "wx_shop_id" );
		    cartList.add( shopMap );
		}
	    }
	    resultMap.put( "shopList", cartList );

	} else {//立即购买
	    int productId = CommonUtil.toInteger( params.get( "product_id" ) );
	    MallProduct product = productService.selectByPrimaryKey( productId );
	    String specificaIds = "";
	    String specificaNames = "";
	    String imageUrl = "";
	    if ( CommonUtil.isNotEmpty( params.get( "product_specificas" ) ) ) {
		specificaIds = CommonUtil.toString( params.get( "product_specificas" ) );

		Map< String,Object > specMap = productService.getSpecNameBySPecId( specificaIds, productId );
		if ( CommonUtil.isNotEmpty( specMap.get( "product_speciname" ) ) ) {
		    specificaNames = CommonUtil.toString( specMap.get( "product_speciname" ) );
		}
		if ( CommonUtil.isNotEmpty( specMap.get( "imageUrl" ) ) ) {
		    imageUrl = CommonUtil.toString( specMap.get( "imageUrl" ) );
		}
	    }
	    if ( CommonUtil.isEmpty( imageUrl ) ) {
		Map< String,Object > imageParams = new HashMap< String,Object >();
		imageParams.put( "assId", productId );
		imageParams.put( "isMainImages", 1 );
		imageParams.put( "assType", 1 );
		List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( imageParams );
		if ( imageList != null && imageList.size() > 0 ) {
		    imageUrl = imageList.get( 0 ).getImageUrl();
		}
	    }
	    if ( CommonUtil.isNotEmpty( imageUrl ) ) {
		if ( imageUrl.indexOf( PropertiesUtil.getResourceUrl() ) < 0 ) {
		    imageUrl = PropertiesUtil.getResourceUrl() + imageUrl;
		}
	    }
	    List< Map< String,Object > > proList = new ArrayList< Map< String,Object > >();

	    Map< String,Object > productMap = new HashMap< String,Object >();
	    productMap.put( "product_id", product.getId() );
	    productMap.put( "product_type_id", product.getProTypeId() );
	    productMap.put( "product_name", product.getProName() );
	    productMap.put( "product_image", imageUrl );
	    productMap.put( "product_specificas", specificaIds );
	    productMap.put( "product_specificaname", specificaNames );
	    productMap.put( "product_num", params.get( "product_num" ) );
	    productMap.put( "product_price", params.get( "product_price" ) );
	    productMap.put( "primary_price", params.get( "primary_price" ) );
	    productMap.put( "is_member_discount", product.getIsMemberDiscount() );
	    productMap.put( "is_coupons", product.getIsCoupons() );
	    productMap.put( "is_integral_deduction", product.getIsIntegralDeduction() );
	    productMap.put( "is_fenbi_deduction", product.getIsFenbiDeduction() );
	    proTypeId = product.getProTypeId();

	    totalNum += CommonUtil.toInteger( params.get( "product_num" ) );

	    //判断商品是否是流量充值
	    if ( CommonUtil.isNotEmpty( product.getProTypeId() ) && CommonUtil.isNotEmpty( product.getFlowId() ) ) {
		if ( product.getProTypeId().toString().equals( "4" ) && product.getFlowId() > 0 ) {
		    isFlow = 1;
		}
	    }

	    proList.add( productMap );

	    double proTotalPrice = CommonUtil.toDouble( params.get( "product_price" ) ) * CommonUtil.toInteger( params.get( "product_num" ) );
	    totalProMoney += proTotalPrice;

	    double totalPrice = proTotalPrice;

	    //计算运费
	    double freightPrice = freightService.getFreightByProvinces( params, addressMap, product.getShopId(), totalPrice, CommonUtil.toDouble( product.getProWeight() ) );
	    totalFreightMoney += freightPrice;

	    //查询店铺名称
	    Map< String,Object > map = new HashMap< String,Object >();
	    map.put( "id", product.getShopId() );
	    //TODO  storeMapper.selectByStoreId(map);
	    Map< String,Object > storeMap = null;
	    //                    storeMapper.selectByStoreId(map);

	    Map< String,Object > shopMap = new HashMap< String,Object >();
	    shopMap.put( "shop_id", product.getShopId() );
	    shopMap.put( "sto_name", storeMap.get( "business_name" ) );
	    if ( CommonUtil.isNotEmpty( storeMap.get( "shopPicture" ) ) ) {
		shopMap.put( "sto_image", storeMap.get( "shopPicture" ) );
	    } else {
		shopMap.put( "sto_image", storeMap.get( "stoPicture" ) );
	    }

	    shopMap.put( "totalNum", totalNum );

	    shopMap.put( "freightPrice", freightPrice );
	    shopMap.put( "proList", proList );
	    shopMap.put( "totalProPrice", df.format( totalPrice ) );

	    List< Map< String,Object > > cartList = new ArrayList< Map< String,Object > >();
	    cartList.add( shopMap );

	    resultMap.put( "shopList", cartList );
	}
	//查询是否能显示货到付款和找人代付的按钮
	Map< String,Object > huodaoMap = paySetService.isHuoDaoByUserId( member.getBusid() );

	resultMap.put( "isDaifu", huodaoMap.get( "isDaifu" ) );

	if ( proTypeId == 0 ) {
	    resultMap.put( "isHuoDao", huodaoMap.get( "isHuoDao" ) );
	} else {
	    resultMap.put( "isHuoDao", 0 );
	}

	totalProMoney = CommonUtil.toDouble( df.format( totalProMoney ) );
	totalFreightMoney = CommonUtil.toDouble( df.format( totalFreightMoney ) );
	resultMap.put( "totalProMoney", totalProMoney );//商品总价
	resultMap.put( "totalFreightMoney", totalFreightMoney );//运费
	resultMap.put( "totalMoney", df.format( totalProMoney + totalFreightMoney ) );//实付金额

	resultMap.put( "isFlow", isFlow );//是否是流量充值商品   1是  0 不是

	return resultMap;
    }

    @Override
    public Map< String,Object > submitOrder( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int code = 1;
	String msg = "";
	if ( CommonUtil.isEmpty( params.get( "order" ) ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整" );
	    return resultMap;
	}
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );

	Member member = memberService.findMemberById( memberId, null );
	//TODO 用户 wxPublicUsersMapper.selectByUserId
	WxPublicUsers wxPublicUsers = null;
	//                wxPublicUsersMapper.selectByUserId(member.getBusid());

	int orderPayWay = 0;
	//判断库存
	List< MallOrder > orderList = new ArrayList<>();
	BigDecimal totalPrimary = new BigDecimal( 0 );

	int memType = memberService.isCardType( member.getId() );
	double orderTotalMoneys = 0;
	double orderTotalFreightMoney = 0;
	//判断库存
	JSONArray orderArray = JSONArray.fromObject( params.get( "order" ) );
	if ( orderArray != null && orderArray.size() > 0 ) {
	    for ( Object object : orderArray ) {
		if ( code == -1 ) {
		    break;
		}
		JSONObject orderObj = JSONObject.fromObject( object );
		MallOrder order = getOrderByParam( orderObj, member );
		order.setShopId( CommonUtil.toInteger( orderObj.get( "shopId" ) ) );
		order.setMemCardType( memType );
		orderPayWay = order.getOrderPayWay();

		if ( order.getOrderPayWay() == 4 ) {//积分支付
		    Integer mIntergral = member.getIntegral();
		    if ( mIntergral < order.getOrderMoney().intValue() || mIntergral < 0 ) {
			code = -1;
			msg = "您的积分不够，不能用积分来兑换这件商品";
			break;
		    }
		}
		if ( order.getOrderPayWay() == 8 ) {//粉币支付
		    double fenbi = member.getFansCurrency();
		    if ( fenbi < order.getOrderMoney().doubleValue() || fenbi < 0 ) {
			code = -1;
			msg = "您的粉币不够，不能用粉币来兑换这件商品";
			break;
		    }
		}
		List< MallOrderDetail > detailList = new ArrayList< MallOrderDetail >();
		if ( CommonUtil.isNotEmpty( orderObj.get( "orderDetail" ) ) ) {
		    JSONArray detailArr = JSONArray.fromObject( orderObj.get( "orderDetail" ) );
		    if ( detailArr != null && detailArr.size() > 0 ) {
			for ( Object object2 : detailArr ) {
			    if ( code == -1 ) {
				break;
			    }
			    JSONObject detailObj = JSONObject.fromObject( object2 );

			    MallOrderDetail detail = getOrderDetailByParam( detailObj );

			    if ( CommonUtil.isNotEmpty( detail.getDetPrivivilege() ) ) {
				totalPrimary = detail.getDetPrivivilege().multiply( CommonUtil.toBigDecimal( detail.getDetProNum() ) );
			    }
			    Map< String,Object > dresultMap = orderDetailIsGo( order, detail );
			    code = CommonUtil.toInteger( dresultMap.get( "code" ) );
			    if ( code == -1 ) {
				if ( CommonUtil.isNotEmpty( dresultMap.get( "errorMsg" ) ) ) {
				    msg = dresultMap.get( "errorMsg" ).toString();
				}
				break;
			    }
			    detailList.add( detail );

			}
		    }
		}
		if ( detailList.size() == 0 && code == 1 ) {
		    code = -1;
		    msg = "参数有误";
		    break;
		}
		orderTotalMoneys += order.getOrderMoney().doubleValue();
		orderTotalFreightMoney += order.getOrderFreightMoney().doubleValue();
		order.setMallOrderDetail( detailList );
		orderList.add( order );

	    }
	}

	String orderNo = "";
	BigDecimal orderMoney = new BigDecimal( 0 );
	//保存订单信息
	if ( orderList != null && orderList.size() > 0 && code > 0 ) {
	    int orderPId = 0;
	    for ( int i = 0; i < orderList.size(); i++ ) {
		MallOrder order = orderList.get( i );
		if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
		    order.setSellerUserId( wxPublicUsers.getId() );
		}
		if ( totalPrimary.compareTo( BigDecimal.ZERO ) == 1 ) {//大于0
		    order.setOrderOldMoney( totalPrimary );
		}
		BigDecimal money = order.getOrderMoney();
		BigDecimal freightMoney = order.getOrderFreightMoney();
		if ( orderList.size() > 1 && i == 0 ) {
		    MallOrder newOrder = new MallOrder();
		    newOrder = order;
		    newOrder.setOrderNo( "SC" + System.currentTimeMillis() );
		    newOrder.setOrderMoney( CommonUtil.toBigDecimal( orderTotalMoneys ) );
		    newOrder.setOrderFreightMoney( CommonUtil.toBigDecimal( orderTotalFreightMoney ) );
		    newOrder.setCreateTime( new Date() );
		    int count = orderDAO.insert( newOrder );
		    if ( count > 0 ) {
			orderNo = newOrder.getOrderNo();
			orderPId = newOrder.getId();
		    }

		}
		if ( orderPId > 0 ) {
		    order.setOrderPid( orderPId );
		}
		order.setOrderMoney( money );
		order.setOrderFreightMoney( freightMoney );
		order.setId( null );
		order.setOrderNo( "SC" + System.currentTimeMillis() );
		order.setCreateTime( new Date() );
		int count = orderDAO.insert( order );
		if ( count < 0 ) {
		    code = -1;
		    break;
		} else if ( count > 0 && orderList.size() == 1 && i == 0 ) {
		    orderNo = order.getOrderNo();
		}
		orderMoney = orderMoney.add( order.getOrderMoney() );
		if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 ) {
		    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
			detail.setOrderId( order.getId() );
			orderDetailDAO.insert( detail );
			if ( count < 0 ) {
			    code = -1;
			    break;
			}
		    }
		}
	    }
	}
	if ( code == -1 && msg.equals( "" ) ) {
	    msg = "提交订单失败，请稍后重试";
	}
	if ( code > 0 ) {//提交订单成功，并且是货到付款，
	    if ( orderPayWay == 4 ) {
		//TODO 修改积分  memberPayService.updateMemberIntergral()
		Map< String,Object > payRresult = null;
		//                        memberPayService.updateMemberIntergral(null, member.getId(), CommonUtil.toInteger(-orderMoney));
		if ( CommonUtil.isNotEmpty( payRresult.get( "result" ) ) ) {
		    if ( CommonUtil.toString( payRresult.get( "result" ) ).equals( "2" ) ) {
			orderService.paySuccessModified( params, member );//修改库存和订单状态
		    }
		}
	    }
	    if ( orderPayWay == 2 ) {
		params.put( "status", 2 );
		params.put( "out_trade_no", orderNo );
		orderService.paySuccessModified( params, member );//修改库存和订单状态
	    }

	    if ( CommonUtil.isNotEmpty( params.get( "cartIds" ) ) ) {
		JSONArray cartArrs = JSONArray.fromObject( params.get( "cartIds" ) );
				/*params.put("cartIds", cartArrs);*/
		if ( cartArrs != null && cartArrs.size() > 0 ) {
		    for ( Object obj : cartArrs ) {
			if ( CommonUtil.isNotEmpty( obj ) ) {
			    shopCartDAO.deleteById( CommonUtil.toInteger( obj ) );
			}
		    }
		}
	    }

	    resultMap.put( "orderNo", orderNo );
	}
	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );
	return resultMap;
    }

    @Override
    public Map< Object,Object > appletWxOrder( Map< String,Object > params, String url ) throws Exception {
	Member member = memberService.findMemberById( CommonUtil.toInteger( params.get( "memberId" ) ), null );

	//TODO  t_wx_member_applet_openid
	//        String sql = "select openid from t_wx_member_applet_openid where style=4 and member_id=" + member.getId();
	//        Map<String, Object> openMap = daoUtil.queryForMap(sql);

	MallOrder order = orderDAO.selectOrderByOrderNo( CommonUtil.toString( params.get( "orderNo" ) ) );
	Map< String,Object > appletParams = new HashMap< String,Object >();
	appletParams.put( "paySource", 1 );//支付来源
	appletParams.put( "busId", member.getBusid() );//商家ID
	appletParams.put( "sysOrderNo", order.getOrderNo() );//订单号
	appletParams.put( "productId", order.getOrderNo() );
	appletParams.put( "desc", "商城下单" );
	appletParams.put( "totalFee", order.getOrderMoney() );
	appletParams.put( "ip", "127.0.0.1" );
	//        appletParams.put("openid", openMap.get("openid"));
	appletParams.put( "model", 3 );
	appletParams.put( "url", url );
	appletParams.put( "appid", CommonUtil.toString( params.get( "appid" ) ) );
	//TODO 下单 wxPayService.memberPayByWxApplet()
	//        Map<Object, Object> parameters = wxPayService.memberPayByWxApplet(appletParams);
	//        return parameters;
	return null;
    }

    @Override
    public Map< String,Object > productBuyNow( Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap< String,Object >();
	int code = 1;
	String msg = "";
	if ( CommonUtil.isEmpty( params.get( "order" ) ) ) {
	    resultMap.put( "code", -1 );
	    resultMap.put( "errorMsg", "参数不完整" );
	    return resultMap;
	}
	int memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	Member member = memberService.findMemberById( memberId, null );
	int memType = memberService.isCardType( member.getId() );
	//判断库存
	List< MallOrder > orderList = new ArrayList<>();
	JSONArray orderArray = JSONArray.fromObject( params.get( "order" ) );
	double totalPrimary = 0;
	if ( orderArray != null && orderArray.size() > 0 ) {
	    for ( Object object : orderArray ) {
		if ( code == -1 ) {
		    break;
		}
		JSONObject orderObj = JSONObject.fromObject( object );
		MallOrder order = getOrderByParam( orderObj, member );
		order.setShopId( CommonUtil.toInteger( orderObj.get( "shopId" ) ) );
		order.setMemCardType( memType );

		if ( order.getOrderPayWay() == 4 ) {//积分支付
		    Integer mIntergral = member.getIntegral();
		    if ( mIntergral < order.getOrderMoney().intValue() || mIntergral < 0 ) {
			code = -1;
			msg = "您的积分不够，不能用积分来兑换这件商品";
			break;
		    }
		}
		if ( order.getOrderPayWay() == 8 ) {//粉币支付
		    double fenbi = member.getFansCurrency();
		    if ( fenbi < order.getOrderMoney().doubleValue() || fenbi < 0 ) {
			code = -1;
			msg = "您的粉币不够，不能用粉币来兑换这件商品";
			break;
		    }
		}

		List< MallOrderDetail > detailList = new ArrayList< MallOrderDetail >();
		if ( CommonUtil.isNotEmpty( orderObj.get( "orderDetail" ) ) ) {
		    JSONArray detailArr = JSONArray.fromObject( orderObj.get( "orderDetail" ) );
		    if ( detailArr != null && detailArr.size() > 0 ) {
			for ( Object object2 : detailArr ) {

			    if ( code == -1 ) {
				break;
			    }
			    JSONObject detailObj = JSONObject.fromObject( object2 );

			    MallOrderDetail detail = getOrderDetailByParam( detailObj );

			    if ( CommonUtil.isNotEmpty( detail.getDetPrivivilege() ) ) {
				totalPrimary = CommonUtil.toDouble( detail.getDetPrivivilege() ) * detail.getDetProNum();
			    }
			    Map< String,Object > dresultMap = orderDetailIsGo( order, detail );
			    code = CommonUtil.toInteger( dresultMap.get( "code" ) );
			    if ( code == -1 ) {
				if ( CommonUtil.isNotEmpty( dresultMap.get( "errorMsg" ) ) ) {
				    msg = dresultMap.get( "errorMsg" ).toString();
				}
				break;
			    }
			    detailList.add( detail );

			}
		    }
		}
		if ( detailList.size() == 0 && code == 1 ) {
		    code = -1;
		    msg = "参数有误";
		    break;
		}
		order.setMallOrderDetail( detailList );
		orderList.add( order );

	    }
	}
	if ( code == 1 ) {
	    System.out.println( orderList );
	    resultMap.put( "orderList", JSONArray.fromObject( orderList ) );
	    resultMap.put( "totalPrimary", totalPrimary );
	}
	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );
	System.out.println( resultMap );
	return resultMap;
    }

    private Map< String,Object > orderDetailIsGo( MallOrder order, MallOrderDetail detail ) {
	Map< String,Object > resultMap = new HashMap<>();
	String msg = "";
	int code = 1;
	//判断商品的库存
	if ( CommonUtil.isNotEmpty( order.getOrderType() ) && order.getOrderType() == 7 && CommonUtil.isNotEmpty( detail.getProSpecStr() ) ) {//判断批发商品的库存
	    JSONObject map = JSONObject.fromObject( detail.getProSpecStr() );
	    for ( Object key : map.keySet() ) {
		Object proSpecificas = key;
		JSONObject p = JSONObject.fromObject( map.get( key ) );
		int proNum = CommonUtil.toInteger( p.get( "num" ) );
		Map< String,Object > result = productService.calculateInventory( detail.getProductId(), proSpecificas, proNum, order.getBuyerUserId() );
		if ( result.get( "result" ).toString().equals( "false" ) ) {
		    msg = result.get( "msg" ).toString();
		    code = -1;
		}
	    }
	} else {
	    Map< String,Object > result = productService.calculateInventory( detail.getProductId(), detail.getProductSpecificas(), detail.getDetProNum(), order.getBuyerUserId() );
	    if ( result.get( "result" ).toString().equals( "false" ) ) {
		msg = result.get( "msg" ).toString();
		code = -1;
	    }
	}
	if ( CommonUtil.isNotEmpty( detail.getProTypeId() ) && code > 0 ) {
	    //卡全包购买判断是否已经过期
	    if ( detail.getProTypeId().toString().equals( "3" ) && CommonUtil.isNotEmpty( detail.getCardReceiveId() ) ) {
		Map< String,Object > cardMap = pageService.getCardReceive( detail.getCardReceiveId() );
		if ( CommonUtil.isNotEmpty( cardMap ) ) {
		    if ( CommonUtil.isNotEmpty( cardMap.get( "recevieMap" ) ) ) {
			JSONObject cardObj = JSONObject.fromObject( cardMap.get( "recevieMap" ) );
			if ( CommonUtil.isNotEmpty( cardObj.get( "guoqi" ) ) && cardObj.get( "guoqi" ).toString().equals( "1" ) ) {
			    msg = "卡券包已过期不能购买";
			}
		    }
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( order.getFlowPhone() ) && code > 0 ) {//流量充值，判断手机号码
	    MallProduct product = productService.selectByPrimaryKey( detail.getProductId() );
	    Map< String,String > map = MobileLocationUtil.getMobileLocation( order.getFlowPhone() );
	    if ( map.get( "code" ).toString().equals( "-1" ) ) {
		resultMap.put( "code", -1 );
		resultMap.put( "msg", map.get( "msg" ) );
		return resultMap;
	    } else if ( map.get( "code" ).toString().equals( "1" ) ) {
		//TODO 充值  busFlowService.selectById(product.getFlowId());
		BusFlow flow = null;
		//                        busFlowService.selectById(product.getFlowId());
		if ( map.get( "supplier" ).equals( "中国联通" ) && flow.getType() == 10 ) {
		    resultMap.put( "code", -1 );
		    resultMap.put( "msg", "充值失败,联通号码至少30MB" );
		    return resultMap;
		}
	    }
	}
	if ( detail.getProTypeId() == 0 && CommonUtil.isEmpty( order.getReceiveId() ) && code > 0 ) {
	    msg = "请选择收货地址";
	    code = -1;
	}
	resultMap.put( "code", code );
	resultMap.put( "errorMsg", msg );
	return resultMap;
    }

    private MallOrder getOrderByParam( JSONObject orderObj, Member member ) {
	MallOrder order = new MallOrder();
	order.setCreateTime( new Date() );
	if ( CommonUtil.isNotEmpty( orderObj.get( "receiveId" ) ) ) {
	    order.setReceiveId( CommonUtil.toInteger( orderObj.get( "receiveId" ) ) );
	}
	if ( CommonUtil.isNotEmpty( order.getReceiveId() ) ) {
	    if ( order.getReceiveId() > 0 ) {
		//TODO 地址方法 orderDAO.selectAddressByReceiveId()
		Map< String,Object > addressMap = null;
		//                        orderDAO.selectAddressByReceiveId(order.getReceiveId());
		if ( CommonUtil.isNotEmpty( addressMap ) ) {
		    addressMap = getAddressParams( addressMap );
		    order.setReceiveName( CommonUtil.toString( addressMap.get( "member_name" ) ) );
		    order.setReceivePhone( CommonUtil.toString( addressMap.get( "member_phone" ) ) );
		    order.setReceiveAddress( CommonUtil.toString( addressMap.get( "address_detail" ) ) );
		}
	    }
	}
	order.setOrderMoney( CommonUtil.toBigDecimal( orderObj.get( "orderMoney" ) ) );
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderFreightMoney" ) ) ) {
	    order.setOrderFreightMoney( CommonUtil.toBigDecimal( orderObj.get( "orderFreightMoney" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderOldMoney" ) ) ) {
	    order.setOrderOldMoney( CommonUtil.toBigDecimal( orderObj.get( "orderOldMoney" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderBuyerMessage" ) ) ) {
	    order.setOrderBuyerMessage( CommonUtil.toString( orderObj.get( "orderBuyerMessage" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderPayWay" ) ) ) {
	    order.setOrderPayWay( CommonUtil.toInteger( orderObj.get( "orderPayWay" ) ) );
	}
		/*order.setBuyerUserId(CommonUtil.toInteger(orderObj.get("buyerUserId")));
		order.setBusUserId(CommonUtil.toInteger(orderObj.get("busUserId")));*/
	order.setOrderStatus( 1 );
	if ( CommonUtil.isNotEmpty( orderObj.get( "deliveryMethod" ) ) ) {
	    order.setDeliveryMethod( CommonUtil.toInteger( orderObj.get( "deliveryMethod" ) ) );
	    if ( order.getDeliveryMethod() == 2 ) {//上门自提
		order.setAppointmentName( CommonUtil.toString( orderObj.get( "appointmentName" ) ) );
		order.setAppointmentTelephone( CommonUtil.toString( orderObj.get( "appointmentTelephone" ) ) );
		order.setAppointmentTime( DateTimeKit.parse( CommonUtil.toString( orderObj.get( "appointmentTime" ) ), "" ) );
		order.setTakeTheirId( CommonUtil.toInteger( orderObj.get( "takeTheirId" ) ) );
		order.setAppointmentStartTime( CommonUtil.toString( orderObj.get( "appointmentStartTime" ) ) );
		order.setAppointmentEndTime( CommonUtil.toString( orderObj.get( "appointmentEndTime" ) ) );
	    }
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "orderType" ) ) ) {
	    order.setOrderType( CommonUtil.toInteger( orderObj.get( "orderType" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "pJoinId" ) ) ) {
	    order.setPJoinId( CommonUtil.toInteger( orderObj.get( "pJoinId" ) ) );
	}
	order.setBuyerUserId( member.getId() );
	order.setBusUserId( member.getBusid() );
	if ( CommonUtil.isNotEmpty( member.getNickname() ) ) {
	    order.setMemberName( member.getNickname() );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "flowPhone" ) ) ) {
	    order.setFlowPhone( CommonUtil.toString( orderObj.get( "flowPhone" ) ) );
	}
	if ( CommonUtil.isNotEmpty( orderObj.get( "shopId" ) ) ) {
	    order.setShopId( CommonUtil.toInteger( orderObj.get( "shopId" ) ) );
	}
	order.setBuyerUserType( 3 );
	return order;
    }

    private MallOrderDetail getOrderDetailByParam( JSONObject detailObj ) {
	MallOrderDetail detail = new MallOrderDetail();
	detail.setProductId( CommonUtil.toInteger( detailObj.get( "productId" ) ) );
	MallProduct product = productService.selectByPrimaryKey( detail.getProductId() );
	detail.setShopId( product.getShopId() );

	String imageUrl = CommonUtil.toString( detailObj.get( "productImageUrl" ) );
	if ( imageUrl.indexOf( "/image" ) >= 0 ) {
	    imageUrl = imageUrl.substring( imageUrl.indexOf( "/image" ), imageUrl.length() );
	} else if ( imageUrl.indexOf( "/upload" ) >= 0 ) {
	    imageUrl = imageUrl.substring( imageUrl.indexOf( "/upload" ) + 7, imageUrl.length() );
	}
	detail.setProductImageUrl( imageUrl );

	detail.setDetProNum( CommonUtil.toInteger( detailObj.get( "detProNum" ) ) );
	detail.setDetProPrice( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "detProPrice" ) ) ) );

	detail.setTotalPrice( CommonUtil.toDouble( detailObj.get( "totalPrice" ) ) );
	detail.setDetPrivivilege( product.getProCostPrice() );
	if ( CommonUtil.isNotEmpty( detailObj.get( "productSpecificas" ) ) ) {
	    detail.setProductSpecificas( CommonUtil.toString( detailObj.get( "productSpecificas" ) ) );
	    detail.setDetPrivivilege( product.getProPrice() );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "productSpeciname" ) ) ) {
	    detail.setProductSpeciname( CommonUtil.toString( detailObj.get( "productSpeciname" ) ) );
	    detail.setProductSpeciname( CommonUtil.getBytes( detail.getProductSpeciname() ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "discountedPrices" ) ) ) {
	    detail.setDiscountedPrices( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "discountedPrices" ) ) ) );
	}
	detail.setDetProName( product.getProName() );
	if ( CommonUtil.isNotEmpty( product.getProCode() ) ) {
	    detail.setDetProCode( product.getProCode() );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "detProMessage" ) ) ) {
	    detail.setDetProMessage( CommonUtil.toString( detailObj.get( "detProMessage" ) ) );
	}
	detail.setReturnDay( product.getReturnDay() );
	if ( CommonUtil.isNotEmpty( detailObj.get( "discount" ) ) ) {
	    detail.setDiscount( CommonUtil.toInteger( detailObj.get( "discount" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "couponCode" ) ) ) {
	    detail.setCouponCode( CommonUtil.toString( detailObj.get( "couponCode" ) ) );
	}
	detail.setCreateTime( new Date() );
	detail.setProTypeId( CommonUtil.toInteger( product.getProTypeId() ) );
	if ( CommonUtil.isNotEmpty( detailObj.get( "useFenbi" ) ) ) {
	    detail.setUseFenbi( CommonUtil.toDouble( detailObj.get( "useFenbi" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "useJifen" ) ) ) {
	    detail.setUseFenbi( CommonUtil.toDouble( detailObj.get( "useJifen" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "proSpecStr" ) ) ) {
	    detail.setProSpecStr( CommonUtil.toString( detailObj.get( "proSpecStr" ) ) );
	}
	if ( CommonUtil.isNotEmpty( product.getCardType() ) ) {
	    detail.setCardReceiveId( product.getCardType() );
	}
		/*if(CommonUtil.isNotEmpty(detailObj.get("cardReceiveId"))){
			detail.setCardReceiveId(CommonUtil.toInteger(detailObj.get("cardReceiveId")));
		}*/
	if ( CommonUtil.isNotEmpty( detailObj.get( "duofenCoupon" ) ) ) {
	    detail.setDuofenCoupon( CommonUtil.toString( detailObj.getString( "duofenCoupon" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "useCardId" ) ) ) {
	    detail.setUseCardId( CommonUtil.toInteger( detailObj.get( "useCardId" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "commission" ) ) ) {
	    detail.setCommission( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "commission" ) ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "saleMemberId" ) ) ) {
	    detail.setSaleMemberId( CommonUtil.toInteger( detailObj.get( "saleMemberId" ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "jifen_youhui" ) ) ) {
	    detail.setIntegralYouhui( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "jifen_youhui" ) ) ) );
	}
	if ( CommonUtil.isNotEmpty( detailObj.get( "fenbi_youhui" ) ) ) {
	    detail.setFenbiYouhui( BigDecimal.valueOf( CommonUtil.toDouble( detailObj.get( "fenbi_youhui" ) ) ) );
	}
	detail.setStatus( -3 );
	return detail;
    }

    private Map< String,Object > getAddressParams( Map< String,Object > map ) {
	Map< String,Object > addressMap = new HashMap< String,Object >();
	addressMap.put( "id", map.get( "id" ) );//地址id
	String address = CommonUtil.toString( map.get( "pName" ) ) + CommonUtil.toString( map.get( "cName" ) );
	if ( CommonUtil.isNotEmpty( map.get( "aName" ) ) ) {
	    address += CommonUtil.toString( map.get( "aName" ) );
	}
	address += CommonUtil.toString( map.get( "mem_address" ) );
	if ( CommonUtil.isNotEmpty( map.get( "mem_zip_code" ) ) ) {
	    address += CommonUtil.toInteger( map.get( "mem_zip_code" ) );
	}
	addressMap.put( "address_detail", address );//详细地址
	addressMap.put( "member_name", map.get( "mem_name" ) );//联系人姓名
	addressMap.put( "member_phone", map.get( "mem_phone" ) );//联系人手机号码
	if ( CommonUtil.isNotEmpty( map.get( "mem_default" ) ) ) {
	    addressMap.put( "is_default", map.get( "mem_default" ) );//是否是默认选中地址
	}
	return addressMap;
    }
}
