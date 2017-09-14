package com.gt.mall.service.web.auction.impl;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.util.KeysUtil;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPayOrder;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.auction.MallAuctionMarginDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.PayOrderService;
import com.gt.mall.service.inter.wxshop.PayService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.auction.MallAuctionMarginService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.PageUtil;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.pay.SubQrPayParams;
import com.gt.util.entity.param.pay.WxmemberPayRefund;
import com.gt.util.entity.result.shop.WsWxShopInfoExtend;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 竞拍保证金表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallAuctionMarginServiceImpl extends BaseServiceImpl< MallAuctionMarginDAO,MallAuctionMargin > implements MallAuctionMarginService {

    private Logger log = Logger.getLogger( MallAuctionMarginServiceImpl.class );

    @Autowired
    private MallAuctionMarginDAO auctionMarginDAO;
    @Autowired
    private MallOrderDAO         orderDAO;
    @Autowired
    private MallStoreDAO         storeDAO;
    @Autowired
    private MemberService        memberService;
    @Autowired
    private WxShopService        wxShopService;
    @Autowired
    private WxPublicUserService  wxPublicUserService;
    @Autowired
    private PayOrderService      payOrderService;
    @Autowired
    private PayService           payService;

    @Override
    public PageUtil selectMarginByShopId( Map< String,Object > params, int userId ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = auctionMarginDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mAuction/margin.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断拍卖是否有数据
	    List< MallAuctionMargin > auctionList = auctionMarginDAO.selectByPage( params );
	    List< MallAuctionMargin > marginList = new ArrayList< MallAuctionMargin >();
	    if ( auctionList != null && auctionList.size() > 0 ) {
		List< WsWxShopInfoExtend > shopInfoList = wxShopService.queryWxShopByBusId( userId );
		for ( MallAuctionMargin margin : auctionList ) {
		    for ( WsWxShopInfoExtend wxShops : shopInfoList ) {
			if ( wxShops.getId() == margin.getWx_shop_id() ) {
			    if ( CommonUtil.isNotEmpty( wxShops.getBusinessName() ) ) {
				margin.setShopName( wxShops.getBusinessName() );
			    }
			    break;
			}
		    }

		    boolean isReturn = true;
		    if ( ( margin.getAuctionStatus() == -1 || margin.getAuctionStatus() == -2 ) && margin.getMarginStatus().toString().equals( "1" ) ) {
			if ( margin.getAuctionStatus() == -1 ) {
			    String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
			    String endTimes = DateTimeKit.format( margin.getCreateTime(), format );
			    String eDate = DateTimeKit.format( new Date(), format );
			    long hour = DateTimeKit.minsBetween( endTimes, eDate, 3600000, format );
			    Map< String,Object > orderMap = orderDAO.selectOrderByAuctId( margin.getAucId() );
			    if ( CommonUtil.isNotEmpty( orderMap ) ) {
				if ( margin.getAucType().toString().equals( "1" ) ) {//降价拍
				    if ( hour >= 24 ) {
					log.info( "降价拍退款进来啦" );
					if ( CommonUtil.isNotEmpty( orderMap.get( "id" ) ) && CommonUtil.isNotEmpty( orderMap.get( "order_status" ) ) ) {
					    if ( orderMap.get( "order_status" ).toString().equals( "1" ) ) {//未付款的不退保证金
						isReturn = false;
					    }
					}
				    }
				} else {//升价拍
				    log.info( "升价拍退款进来啦" );
				    if ( hour >= 72 ) {
					if ( CommonUtil.isNotEmpty( orderMap.get( "id" ) ) && CommonUtil.isNotEmpty( orderMap.get( "order_status" ) ) ) {
					    if ( orderMap.get( "order_status" ).toString().equals( "1" ) ) {//未付款的不退保证金
						isReturn = false;
					    }
					}
				    }
				}
			    } else {
				isReturn = false;
			    }
			} else {
			    isReturn = true;
			}
			int isReturnMargin = 0;
			if ( isReturn ) {//不能退款，修改保证金的状态
			    //returnEndMargin(map);
			    isReturnMargin = 1;
			}
			margin.setIsReturn( isReturnMargin );
		    }
		    marginList.add( margin );
		}
	    }
	    page.setSubList( marginList );
	}
	return page;
    }

    @Override
    public int paySuccessAuction( Map< String,Object > params ) {
	int num = 0;
	String aucNo = params.get( "out_trade_no" ).toString();

	//根据返回的保证金单号来查询竞拍保证金
	MallAuctionMargin margin = auctionMarginDAO.selectByAucNo( aucNo );
	if ( margin != null ) {
	    MallAuctionMargin aucMargin = new MallAuctionMargin();
	    aucMargin.setId( margin.getId() );
	    if ( margin.getPayWay().toString().equals( "1" ) ) {
		if ( CommonUtil.isNotEmpty( params.get( "transaction_id" ) ) ) {
		    aucMargin.setPayNo( params.get( "transaction_id" ).toString() );
		}
	    }
	    aucMargin.setMarginStatus( 1 );
	    aucMargin.setPayTime( new Date() );
	    num = auctionMarginDAO.updateById( aucMargin );
	    if ( num > 0 && margin.getPayWay().toString().equals( "2" ) ) {
		//储值卡支付
		//todo 储值卡支付
	    }
	}

	return num;
    }

    @Override
    public Map< String,Object > addMargin( Map< String,Object > params, Member member ) throws Exception {
	Map< String,Object > result = new HashMap<>();

	MallAuctionMargin margin = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "margin" ).toString() ), MallAuctionMargin.class );
	margin.setUserId( member.getId() );
	MallAuctionMargin aucMargin = auctionMarginDAO.selectByMargin( margin );//查询该拍卖是否已经加入了保证金
	String aucNo = "PM" + System.currentTimeMillis();
	if ( aucMargin == null ) {
	    margin.setCreateTime( new Date() );
	    margin.setAucNo( aucNo );
	    margin.setUserId( member.getId() );
	    auctionMarginDAO.insert( margin );
	} else {//已经交纳了保证金，无需再次交纳
	    if ( aucMargin.getMarginStatus().toString().equals( "1" ) ) {
		result.put( "isReturn", "1" );
		result.put( "errorMsg", "您已经交纳了保证金，无需再次交纳" );
		result.put( "code", ResponseEnums.ERROR.getCode() );
	    } else {
		margin.setId( aucMargin.getId() );
		auctionMarginDAO.updateById( margin );
		result.put( "code", ResponseEnums.SUCCESS.getCode() );
		margin.setAucNo( aucMargin.getAucNo() );
	    }
	}
	if ( CommonUtil.isNotEmpty( margin.getId() ) ) {
	    if ( margin.getId() > 0 ) {
		result.put( "code", ResponseEnums.SUCCESS.getCode() );
		result.put( "id", margin.getId() );
		result.put( "no", margin.getAucNo() );
		result.put( "payWay", margin.getPayWay() );
		result.put( "busId", member.getBusid() );

		if ( margin.getPayWay() == 1 || margin.getPayWay() == 3 ) {
		    String url = getWxAlipay( margin, member );
		    result.put( "payUrl", url );
		} else if ( margin.getPayWay() == 2 ) {
		    params.put( "out_trade_no", margin.getAucNo() );
		    paySuccessAuction( params );
		    result.put( "payUrl", "/mAuction/79B4DE7C/myMargin.do?busId=" + member.getBusid() );
		}

	    } else {
		result.put( "code", ResponseEnums.ERROR.getCode() );
		result.put( "errorMsg", "交纳保证金失败" );
	    }
	}
	return result;
    }

    private String getWxAlipay( MallAuctionMargin mallAuctionMargin, Member member ) throws Exception {
	SubQrPayParams subQrPayParams = new SubQrPayParams();
	subQrPayParams.setTotalFee( CommonUtil.toDouble( mallAuctionMargin.getMarginMoney() ) );
	subQrPayParams.setModel( Constants.PAY_MODEL );
	subQrPayParams.setBusId( member.getBusid() );
	subQrPayParams.setAppidType( 0 );
	    /*subQrPayParams.setAppid( 0 );*///微信支付和支付宝支付可以不传
	subQrPayParams.setOrderNum( mallAuctionMargin.getAucNo() );//订单号
	subQrPayParams.setMemberId( member.getId() );//会员id
	subQrPayParams.setDesc( "商城缴纳拍卖定金" );//描述
	subQrPayParams.setIsreturn( 1 );//是否需要同步回调(支付成功后页面跳转),1:需要(returnUrl比传),0:不需要(为0时returnUrl不用传)
	subQrPayParams.setReturnUrl( PropertiesUtil.getHomeUrl() + "/mAuction/79B4DE7C/myMargin.do" );
	subQrPayParams.setNotifyUrl( PropertiesUtil.getHomeUrl()
			+ "/mAuction/79B4DE7C/payWay.do" );//异步回调，注：1、会传out_trade_no--订单号,payType--支付类型(0:微信，1：支付宝2：多粉钱包),2接收到请求处理完成后，必须返回回调结果：code(0:成功,-1:失败),msg(处理结果,如:成功)
	subQrPayParams.setIsSendMessage( 1 );//是否需要消息推送,1:需要(sendUrl比传),0:不需要(为0时sendUrl不用传)
	subQrPayParams.setSendUrl( PropertiesUtil.getHomeUrl() + "/mAuction/margin.do" );//推送路径(尽量不要带参数)
	int payWay = 1;
	if ( mallAuctionMargin.getPayWay() == 3 ) {
	    payWay = 2;
	}
	subQrPayParams.setPayWay( payWay );//支付方式  0----系统根据浏览器判断   1---微信支付 2---支付宝 3---多粉钱包支付

	/*Map< String,Object > resultMap = payService.payapi( subQrPayParams );
	if ( !resultMap.get( "code" ).toString().equals( "1" ) ) {
	    String errorMsg = "支付失败";
	    if ( CommonUtil.isNotEmpty( resultMap.get( "errorMsg" ) ) ) {
		errorMsg = resultMap.get( "errorMsg" ).toString();
	    }
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), errorMsg );
	}*/
	logger.info( "拍卖缴纳定金参数：" + JSONObject.toJSONString( subQrPayParams ) );
	KeysUtil keyUtil = new KeysUtil();
	String params = keyUtil.getEncString( JSONObject.toJSONString( subQrPayParams ) );
	return PropertiesUtil.getWxmpDomain() + "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do?obj=" + params;
    }

    @Override
    public List< MallAuctionMargin > getMyAuction( MallAuctionMargin margin ) {
	List< MallAuctionMargin > marginList = auctionMarginDAO.selectListByMargin( margin );
	return marginList;
    }

    @Override
    public void returnMargin() throws Exception {
	List< Map< String,Object > > marginList = auctionMarginDAO.selectMarginByEnd();
	if ( marginList != null && marginList.size() > 0 ) {
	    for ( int i = 0; i < marginList.size(); i++ ) {
		boolean isReturn = true;
		Map< String,Object > map = marginList.get( i );
		log.info( map );
		String endTimes = map.get( "create_time" ).toString();
		String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
		String eDate = DateTimeKit.format( new Date(), format );
		long hour = DateTimeKit.minsBetween( endTimes, eDate, 3600000, format );
		if ( map.get( "auc_type" ).toString().equals( "1" ) ) {//降价拍
		    if ( hour >= 24 ) {
			log.info( "降价拍退款进来啦" );
			if ( CommonUtil.isNotEmpty( map.get( "order_id" ) ) && CommonUtil.isNotEmpty( map.get( "order_status" ) ) ) {
			    if ( map.get( "order_status" ).toString().equals( "1" ) ) {//未付款的不退保证金
				isReturn = false;
			    }
			}
		    }
		} else {//升价拍
		    log.info( "升价拍退款进来啦" );
		    if ( hour >= 72 ) {
			if ( CommonUtil.isNotEmpty( map.get( "order_id" ) ) && CommonUtil.isNotEmpty( map.get( "order_status" ) ) ) {
			    if ( map.get( "order_status" ).toString().equals( "1" ) ) {//未付款的不退保证金
				isReturn = false;
			    }
			}
		    }
		}
		if ( !isReturn ) {//不能退款，修改保证金的状态
		    Integer marginId = CommonUtil.toInteger( map.get( "id" ) );
		    MallAuctionMargin margin = new MallAuctionMargin();
		    margin.setId( marginId );
		    margin.setMarginStatus( -2 );
		    margin.setNoReturnReason( "拍卖没有转换成订单" );
		    auctionMarginDAO.updateById( margin );
		} else {
		    map.put( "isAlipay", false );
		    returnEndMargin( map );
		}
	    }
	}
    }

    //    @Override
    //    public MallAuctionMargin selectByMarginId(int marginId) {
    //        return auctionMarginDAO.selectByPrimaryKey(marginId);
    //    }

    @Override
    public Map< String,Object > returnEndMargin( Map< String,Object > map ) throws Exception {
	log.info( map );
	Map< String,Object > resultMap = new HashMap< String,Object >();
	Integer memberId = CommonUtil.toInteger( map.get( "user_id" ) );
	Integer payWay = CommonUtil.toInteger( map.get( "pay_way" ) );
	Double money = CommonUtil.toDouble( map.get( "margin_money" ) );
	String aucNo = CommonUtil.toString( map.get( "auc_no" ) );
	WxPublicUsers pUser = wxPublicUserService.selectByMemberId( memberId );
	String returnNo = "PMTK" + System.currentTimeMillis();
	map.put( "return_no", returnNo );

	if ( payWay.toString().equals( "1" ) && CommonUtil.isNotEmpty( pUser ) ) {//微信退款
	    WxPayOrder wxPayOrder = payOrderService.selectWxOrdByOutTradeNo( aucNo );
	    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {

		WxmemberPayRefund refund = new WxmemberPayRefund();
		refund.setMchid( pUser.getMchId() );// 商户号
		refund.setAppid( pUser.getAppid() );// 公众号
		refund.setTotalFee( wxPayOrder.getTotalFee() );//支付总金额
		refund.setSysOrderNo( wxPayOrder.getOutTradeNo() );//系统单号

		refund.setRefundFee( money );//退款金额

		logger.error( "微信退款的参数：" + net.sf.json.JSONObject.fromObject( refund ).toString() );
		Map< String,Object > resultmap = payService.wxmemberPayRefund( refund );  //微信退款
		logger.error( "微信退款的返回值：" + net.sf.json.JSONObject.fromObject( resultmap ) );

		if ( resultmap != null ) {
		    if ( resultmap.get( "code" ).toString().equals( "1" ) ) {
			//退款成功修改退款状态
			updateReturnStatus( pUser, map, returnNo );//微信退款
		    } else {
			resultMap.put( "result", false );
			resultMap.put( "msg", resultmap.get( "msg" ) );
		    }
		}

	    }
	} else if ( payWay.toString().equals( "2" ) ) {//储值卡退款
	    Map< String,Object > returnParams = new HashMap<>();
	    Member member = memberService.findMemberById( memberId, null );
	    returnParams.put( "busId", member.getBusid() );
	    returnParams.put( "orderNo", aucNo );
	    returnParams.put( "money", money );
	    Map< String,Object > payResultMap = memberService.refundMoney( returnParams ); //memberPayService.chargeBack(memberId,money);
	    if ( payResultMap != null ) {
		if ( !CommonUtil.isEmpty( payResultMap.get( "result" ) ) ) {
		    boolean result = Boolean.valueOf( payResultMap.get( "result" ).toString() );
		    if ( result ) {//退款成功修改退款状态
			updateReturnStatus( pUser, map, returnNo );//储值卡退款退款
		    } else {
			resultMap.put( "result", false );
			resultMap.put( "msg", payResultMap.get( "message" ) );
		    }
		}
	    }
	} else if ( payWay.toString().equals( "3" ) && !map.containsKey( "isAlipay" ) ) {//支付宝退款
	    updateReturnStatus( pUser, map, returnNo );//储值卡退款退款
	}
	return resultMap;
    }

    /**
     * 修改退款保证金的状态
     *
     * @param pUser
     * @param map
     * @param returnNo
     */
    private void updateReturnStatus( WxPublicUsers pUser, Map< String,Object > map, String returnNo ) {
	Integer marginId = CommonUtil.toInteger( map.get( "id" ) );

	MallAuctionMargin margin = new MallAuctionMargin();
	margin.setId( marginId );
	margin.setMarginStatus( -1 );
	margin.setReturnNo( map.get( "return_no" ).toString() );
	margin.setReturnTime( new Date() );
	int num = auctionMarginDAO.updateById( margin );

    }

    @Override
    public void returnAlipayMargin( Map< String,Object > params ) {
	String aucNo = params.get( "outTradeNo" ).toString();//订单号
	//根据订单号查询订单信息
	MallAuctionMargin margin = auctionMarginDAO.selectByAucNo( aucNo );
	params.put( "user_id", margin.getUserId() );
	params.put( "id", margin.getId() );
	params.put( "margin_money", margin.getMarginMoney() );
	params.put( "auc_no", margin.getAucNo() );
	String returnNo = "PMTK" + System.currentTimeMillis();
	params.put( "return_no", returnNo );
	params.put( "pay_way", margin.getPayWay() );
	params.put( "shop_id", margin.getShopId() );

	Member member = memberService.findMemberById( margin.getUserId(), null );
	WxPublicUsers wx = null;
	if ( CommonUtil.isNotEmpty( member.getPublicId() ) ) {
	    wx = wxPublicUserService.selectById( member.getPublicId() );
	}
	updateReturnStatus( wx, params, returnNo );
    }
}
