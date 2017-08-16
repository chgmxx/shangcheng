package com.gt.mall.web.service.auction.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPayOrder;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.dao.auction.MallAuctionMarginDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.auction.MallAuctionMargin;
import com.gt.mall.inter.service.MemberService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.DateTimeKit;
import com.gt.mall.util.PageUtil;
import com.gt.mall.web.service.auction.MallAuctionMarginService;
import net.sf.json.JSONObject;
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

    @Override
    public PageUtil selectMarginByShopId( Map< String,Object > params ) {
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
		for ( MallAuctionMargin margin : auctionList ) {
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
		//添加总的消费记录  暂不加记录
		//addUserConsume(margin);
	    }
	}

	return num;
    }

    @Override
    public Map< String,Object > addMargin( Map< String,Object > params, String memberId ) {
	Map< String,Object > result = new HashMap< String,Object >();

	MallAuctionMargin margin = (MallAuctionMargin) JSONObject.toBean( JSONObject.fromObject( params.get( "margin" ) ), MallAuctionMargin.class );
	margin.setUserId( CommonUtil.toInteger( memberId ) );
	MallAuctionMargin aucMargin = auctionMarginDAO.selectByMargin( margin );//查询该拍卖是否已经加入了保证金
	String aucNo = "PM" + System.currentTimeMillis();
	if ( aucMargin == null ) {
	    margin.setCreateTime( new Date() );
	    margin.setAucNo( aucNo );
	    margin.setUserId( Integer.parseInt( memberId ) );
	    auctionMarginDAO.insert( margin );
	} else {//已经交纳了保证金，无需再次交纳
	    if ( aucMargin.getMarginStatus().toString().equals( "1" ) ) {
		result.put( "msg", "您已经交纳了保证金，无需再次交纳" );
		result.put( "code", 1 );
		result.put( "result", false );
	    } else {
		margin.setId( aucMargin.getId() );
		auctionMarginDAO.updateById( margin );
		result.put( "result", true );
		margin.setAucNo( aucMargin.getAucNo() );
	    }
	}
	if ( CommonUtil.isNotEmpty( margin.getId() ) ) {
	    if ( margin.getId() > 0 ) {
		result.put( "result", true );
		result.put( "msg", "交纳保证金成功" );
		result.put( "id", margin.getId() );
		result.put( "no", margin.getAucNo() );
		result.put( "payWay", margin.getPayWay() );

		Member member = memberService.findMemberById( CommonUtil.toInteger( memberId ), null );
		result.put( "busId", member.getBusid() );
	    } else {
		result.put( "result", false );
		result.put( "msg", "交纳保证金失败" );
	    }
	}
	return result;
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
	//TODO 需关连 WxPublicUsers 数据
	WxPublicUsers pUser = new WxPublicUsers();
	//        WxPublicUsers pUser = mOrderMapper.getWpUser(memberId);
	String returnNo = "PMTK" + System.currentTimeMillis();
	map.put( "return_no", returnNo );

	if ( payWay.toString().equals( "1" ) && CommonUtil.isNotEmpty( pUser ) ) {//微信退款
	    //TODO 需关连 WxPayOrder 数据
	    WxPayOrder wxPayOrder = new WxPayOrder();
	    //            WxPayOrder wxPayOrder=wxPayOrderMapper.selectByOutTradeNo(aucNo);
	    if ( wxPayOrder.getTradeState().equals( "SUCCESS" ) ) {
		map.put( "appid", pUser.getAppid() );// 公众号
		map.put( "mchid", pUser.getMchId() );// 商户号
		map.put( "sysOrderNo", wxPayOrder.getOutTradeNo() );// 系统订单号
		map.put( "wx_order_no", wxPayOrder.getTransactionId() );// 微信订单号
		map.put( "outRefundNo", returnNo );// 商户退款单号(系统生成)
		map.put( "totalFee", wxPayOrder.getTotalFee() );// 总金额
		map.put( "refundFee", money );// 退款金额
		map.put( "key", pUser.getApiKey() );// 商户支付密钥
		map.put( "wxOrdId", wxPayOrder.getId() );// 微信订单表主键
		log.info( "JSONObject.fromObject(resultmap).toString()" + JSONObject.fromObject( map ).toString() );

		//TODO 需关连 WxPayService.memberPayRefund(map)方法
		Map< String,Object > resultmap = new HashMap<>();
		//                Map<String, Object> resultmap = payService.memberPayRefund(map);
		log.info( "JSONObject.fromObject(resultmap).toString()" + JSONObject.fromObject( resultmap ).toString() );
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
	//TODO 需关连 WxPublicUsers 数据
	WxPublicUsers wx = null;
	//        if(CommonUtil.isNotEmpty(member.getPublicId())){
	//            wx = wxPublicUsersMapper.selectByPrimaryKey(member.getPublicId());
	//        }
	updateReturnStatus( wx, params, returnNo );
    }
}
