package com.gt.mall.service.web.presale.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPayOrder;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.bean.member.PaySuccessBo;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.presale.*;
import com.gt.mall.dao.store.MallStoreDAO;
import com.gt.mall.entity.presale.*;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.MemberPayService;
import com.gt.mall.service.inter.MemberService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.util.PageUtil;
import com.gt.mall.service.web.presale.MallPresaleDepositService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户缴纳定金表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPresaleDepositServiceImpl extends BaseServiceImpl< MallPresaleDepositDAO,MallPresaleDeposit > implements MallPresaleDepositService {

    private Logger log = Logger.getLogger( MallPresaleDepositServiceImpl.class );

    @Autowired
    private MallPresaleDepositDAO mallPresaleDepositDAO;

    @Autowired
    private MallOrderDAO mallOrderDAO;

    @Autowired
    private MallStoreDAO mallStoreDAO;

    @Autowired
    private MallPresaleRankDAO mallPresaleRankDAO;

    @Autowired
    private MallPresaleGiveDAO mallPresaleGiveDAO;

    @Autowired
    private MallPresaleMessageRemindDAO mallPresaleMessageRemindDAO;

    @Autowired
    private MallPresaleDAO mallPresaleDAO;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberPayService memberPayService;

    /**
     * 通过店铺id来查询拍定金
     *
     * @Title: selectFreightByShopId
     */
    @Override
    public PageUtil selectPresaleByShopId( Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1
			: CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallPresaleDepositDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mPresale/deposit.do" );
	int firstNum = pageSize
			* ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断预售是否有数据
	    List< MallPresaleDeposit > depositList = mallPresaleDepositDAO.selectByPage( params );
	    page.setSubList( depositList );
	}
	return page;
    }

    /**
     * 定金支付成功的回调函数
     */
    @Override
    public int paySuccessPresale( Map< String,Object > params ) {
	int num = 0;
	String aucNo = params.get( "out_trade_no" ).toString();
	//根据返回的定金单号来查询竞拍定金
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByPreNo( aucNo );
	if ( deposit != null ) {
	    MallPresaleDeposit dep = new MallPresaleDeposit();
	    dep.setId( deposit.getId() );
	    if ( deposit.getPayWay().toString().equals( "1" ) ) {
		if ( CommonUtil.isNotEmpty( params.get( "transaction_id" ) ) ) {
		    dep.setPayNo( params.get( "transaction_id" ).toString() );
		}
	    }
	    dep.setDepositStatus( 1 );
	    dep.setPayTime( new Date() );
	    num = mallPresaleDepositDAO.updateById( dep );
	    if ( num > 0 ) {
		diffInvNum( deposit );//从redis扣除商品库存
		Map< String,Object > maps = new HashMap< String,Object >();
		maps.put( "presaleId", deposit.getPresaleId() );
		int ranks = 1;//排名
		//判断用户的名次
		MallPresaleRank presaleRank = mallPresaleRankDAO.selectByPresaleId( maps );
		if ( CommonUtil.isNotEmpty( presaleRank ) ) {
		    ranks = presaleRank.getRank() + 1;
		}
		Member member = memberService.findMemberById( deposit.getUserId(), null );
		int busUserId = 0;//商家id
		if ( CommonUtil.isNotEmpty( member ) ) {
		    busUserId = member.getBusid();
		}

		if ( busUserId > 0 ) {
		    MallPresaleRank rank = new MallPresaleRank();
		    boolean isInsert = false;
		    //通过排名查询奖品
		    List< MallPresaleGive > giveList = mallPresaleGiveDAO.selectByUserId( busUserId );
		    if ( giveList != null && giveList.size() > 0 ) {
			for ( int i = 0; i < giveList.size(); i++ ) {
			    MallPresaleGive give = giveList.get( i );
			    if ( ranks < give.getGiveRanking() ) {
				rank.setType( give.getGiveType() );
				rank.setGiveName( give.getGiveName() );
				rank.setGiveNum( give.getGiveNum() );
				isInsert = true;
				break;
			    }
			}
		    }
		    if ( isInsert ) {
			rank.setDepositId( deposit.getId() );
			rank.setPresaleId( deposit.getPresaleId() );
			rank.setRank( ranks );
			rank.setMemberId( deposit.getUserId() );
			mallPresaleRankDAO.insert( rank );
		    }

		}

		//储值卡支付
		if ( deposit.getPayWay() == 2 && num > 0 ) {
		    num = petCartPay( deposit );
		}
	    }
	}
	return num;
    }

    /**
     * 储值卡支付
     */
    private int petCartPay( MallPresaleDeposit deposit ) {
	PaySuccessBo sucess = new PaySuccessBo();
	sucess.setMemberId( deposit.getUserId() );//会员id
	MallStore store = mallStoreDAO.selectById( deposit.getShopId() );
	sucess.setStoreId( store.getWxShopId() );//门店id
	sucess.setOrderCode( deposit.getOrderNo() );//订单号
	sucess.setTotalMoney( CommonUtil.toDouble( deposit.getDepositMoney() ) );//原价
	sucess.setDiscountMoney( CommonUtil.toDouble( deposit.getDepositMoney() ) );//折扣后金额
	sucess.setPay( CommonUtil.toDouble( deposit.getDepositMoney() ) );//支付金额
	int payType = 1;//微信支付
	if(deposit.getPayWay() == 2){
	    payType = 5;//储值卡支付
	}
	sucess.setPayType( payType );//支付方式
	sucess.setUcType( 101 );//消费方式 对应字典表 1197
	sucess.setDelay( -1 );//不赠送物品
	sucess.setUcTable( "t_mall_presale_deposit" );
	sucess.setDataSource( deposit.getBuyerUserType()  );
	//支付
	Map< String,Object > resultMap = memberPayService.paySuccess( sucess );
	int code = CommonUtil.toInteger( resultMap.get( "code" ) );
	if ( code == 1) {//支付成功
	    return 1;
	}else if(code == 5006){//储值卡金额不够
	    return -2;
	} else {//支付失败
	    throw new BusinessException( ResponseEnums.INTER_ERROR.getCode(), ResponseEnums.INTER_ERROR.getDesc() );
	}
    }

    /**
     * 减去预售商品的库存（redis）
     */
    private void diffInvNum( MallPresaleDeposit dep ) {
	String invKey = Constants.REDIS_KEY + "presale_num";//秒杀库存的key
	String field = dep.getPresaleId().toString();
	String specificas = "";

	//判断商品是否有规格
	if ( CommonUtil.isNotEmpty( dep.getProSpecificaIds() ) ) {
	    specificas = dep.getProSpecificaIds();
	}
	//查询预售商品的库存
	Integer invNum = 0;
	String value = JedisUtil.maoget( invKey, field );
	if ( CommonUtil.isNotEmpty( value ) ) {
	    JSONObject specObj = JSONObject.fromObject( value );
	    JSONArray arr = new JSONArray();
	    if ( !specificas.equals( "" ) ) {
		//有规格，取规格的库存
		if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
		    JSONArray preSpecArr = JSONArray.fromObject( specObj.get( "specArr" ) );
		    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
			for ( Object obj : preSpecArr ) {
			    JSONObject preObj = JSONObject.fromObject( obj );
			    if ( preObj.get( "specId" ).toString().equals( specificas ) ) {
				invNum = CommonUtil.toInteger( preObj.get( "invNum" ) );
				//break;
				preObj.put( "invNum", invNum - 1 );
			    }
			    arr.add( preObj );
			}
		    }
		}
	    }
	    if ( arr != null && arr.size() > 0 ) {
		specObj.put( "specArr", arr );
	    }
	    if ( CommonUtil.isNotEmpty( specObj.get( "invNum" ) ) ) {
		invNum = CommonUtil.toInteger( specObj.get( "invNum" ) );
		specObj.put( "invNum", invNum - 1 );
	    }
	    JedisUtil.map( invKey, field, specObj.toString() );
	}
    }

    /**
     * 交纳定金成功
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > addDeposit( Map< String,Object > params, String memberId ,Integer browser ) {
	Map< String,Object > result = new HashMap< String,Object >();

	MallPresaleDeposit deposit = (MallPresaleDeposit) JSONObject.toBean( JSONObject.fromObject( params.get( "presale" ) ), MallPresaleDeposit.class );
	deposit.setUserId( CommonUtil.toInteger( memberId ) );
	deposit.setIsSubmit( 0 );
	MallPresaleDeposit dep = mallPresaleDepositDAO.selectByDeposit( deposit );

	deposit.setBuyerUserType( browser );

	String depNo = "YSH" + System.currentTimeMillis();

	if ( dep == null ) {
	    deposit.setCreateTime( new Date() );
	    deposit.setDepositNo( depNo );
	    deposit.setUserId( Integer.parseInt( memberId ) );
	    mallPresaleDepositDAO.insert( deposit );
	} else {//已经交纳了定金，无需再次交纳
	    if ( dep.getDepositStatus().toString().equals( "1" ) ) {
		result.put( "msg", "您已经交纳了定金，无需再次交纳" );
		result.put( "code", 1 );
		result.put( "result", false );
	    } else {
		deposit.setId( dep.getId() );
		mallPresaleDepositDAO.updateById( deposit );
		result.put( "result", true );
		deposit.setDepositNo( dep.getDepositNo() );
	    }
	}
	if ( CommonUtil.isNotEmpty( deposit.getId() ) ) {

	    result = isInvNum( deposit );//判断商品的库存
	    if ( result.get( "result" ).toString().equals( "false" ) ) {
		return result;
	    }

	    if ( deposit.getId() > 0 ) {
		result.put( "result", true );
		result.put( "msg", "交纳定金成功" );
		result.put( "id", deposit.getId() );
		result.put( "no", deposit.getDepositNo() );
		result.put( "payWay", deposit.getPayWay() );
	    } else {
		result.put( "result", false );
		result.put( "msg", "交纳定金失败" );
	    }
	}
	return result;
    }

    private Map< String,Object > isInvNum( MallPresaleDeposit dep ) {
	Map< String,Object > result = new HashMap<>();
	String invKey = Constants.REDIS_KEY + "presale_num";//秒杀库存的key
	String specificas = "";

	//判断商品是否有规格
	if ( CommonUtil.isNotEmpty( dep.getProSpecificaIds() ) ) {
	    specificas = dep.getProSpecificaIds();
	}
	//查询秒杀商品的库存
	Integer invNum = 0;
	String value = JedisUtil.maoget( invKey, dep.getPresaleId().toString() );
	boolean flag = true;
	if ( CommonUtil.isNotEmpty( value ) ) {
	    JSONObject specObj = JSONObject.fromObject( value );
	    if ( !specificas.equals( "" ) ) {
		//有规格，取规格的库存
		if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
		    JSONArray preSpecArr = JSONArray.fromObject( specObj.get( "specArr" ) );
		    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
			for ( Object obj : preSpecArr ) {
			    JSONObject preObj = JSONObject.fromObject( obj );
			    if ( preObj.get( "specId" ).toString().equals( specificas ) ) {
				invNum = CommonUtil.toInteger( preObj.get( "invNum" ) );
				flag = false;
				break;
			    }
			}
		    }
		}
	    }
	    if ( CommonUtil.isEmpty( specificas ) || flag ) {
		//无规格，则取商品库存
		if ( CommonUtil.isNotEmpty( specObj.get( "invNum" ) ) ) {
		    invNum = CommonUtil.toInteger( specObj.get( "invNum" ) );
		}
	    }
	}

	int proNum = dep.getProNum();//购买商品的用户
	//判断库存是否够
	if ( invNum >= proNum && invNum > 0 ) {
	    result.put( "result", true );
	} else {
	    result.put( "msg", "预售商品的库存不够" );
	    result.put( "result", false );
	}
	return result;
    }

    /**
     * 根据用户id查询我的所有的定金
     */
    public List< Map< String,Object > > getMyPresale( MallPresaleDeposit deposit ) {
	return mallPresaleDepositDAO.selectListByDepositPro( deposit );
    }

    /**
     * 退定金
     */
    @Override
    public void returnDeposit() throws Exception {
	List< Map< String,Object > > List = mallPresaleDepositDAO.selectDepositByEnd();
	if ( List != null && List.size() > 0 ) {
	    for ( int i = 0; i < List.size(); i++ ) {
				/*boolean isReturn = true;*/
		Map< String,Object > map = List.get( i );
		log.info( map );
		map.put( "isAlipay", false );
		returnEndPresale( map );

		/*String endTimes = map.get("create_time").toString();
		String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
		String eDate = DateTimeKit.format(new Date(), format);
		long hour = DateTimeKit.minsBetween(endTimes, eDate, 3600000, format);
		if(hour >= 24){//24小时内 未付款的不退定金
			if(CommonUtil.isNotEmpty(map.get("order_id")) && CommonUtil.isNotEmpty(map.get("order_status"))){
				if(map.get("order_status").toString().equals("1")){//未付款的不退定金
					isReturn = false;
				}
			}
		}

		if(!isReturn){//不能退款，修改定金的状态
			Integer preId = CommonUtil.toInteger(map.get("id"));
			MallPresaleDeposit dep = new MallPresaleDeposit();
			dep.setId(preId);
			dep.setDepositStatus(Byte.valueOf("-2"));

			mallPresaleDepositDao.updateByPrimaryKeySelective(dep);
		}else{
			returnEndMargin(map);
		}*/
	    }
	}
    }

    @Override
    public Map< String,Object > returnEndPresale( Map< String,Object > map ) throws Exception {
	log.info( map );
	Map< String,Object > resultMap = new HashMap< String,Object >();
	Integer memberId = CommonUtil.toInteger( map.get( "user_id" ) );
	Integer payWay = CommonUtil.toInteger( map.get( "pay_way" ) );
	Double money = CommonUtil.toDouble( map.get( "deposit_money" ) );
	String depNo = CommonUtil.toString( map.get( "deposit_no" ) );
	//todo 根据粉丝id查询公众号的信息
	WxPublicUsers pUser = new WxPublicUsers();
	String returnNo = "YSHTK" + System.currentTimeMillis();
	map.put( "return_no", returnNo );

	if ( payWay.toString().equals( "1" ) && CommonUtil.isNotEmpty( pUser ) ) {//微信退款

	    //todo 调用小屁孩的接口，根据订单号查询支付订单信息
	    WxPayOrder wxPayOrder = null;//wxPayOrderMapper.selectByOutTradeNo(depNo);
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

		//todo 调用小屁孩的接口，会员退款
		Map< String,Object > resultmap = null;//payService.memberPayRefund(map);
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
	    Member member = memberService.findMemberById( memberId, null );
	    Map< String,Object > returnParams = new HashMap<>();
	    returnParams.put( "busId", member.getBusid() );
	    returnParams.put( "orderNo", depNo );
	    returnParams.put( "money", money );
	    //储值卡退款
	    Map< String,Object > payResultMap = memberService.refundMoney( returnParams );//memberPayService.chargeBack(memberId,money);
	    if ( payResultMap != null ) {
		if ( CommonUtil.isNotEmpty( payResultMap.get( "code" ) ) ) {
		    int code = CommonUtil.toInteger( payResultMap.get( "code" ) );
		    if ( code == 1 ) {//退款成功修改退款状态
			updateReturnStatus( pUser, map, returnNo );//储值卡退款退款
		    } else {
			resultMap.put( "result", false );
			resultMap.put( "msg", payResultMap.get( "errorMsg" ) );
		    }
		}
	    }
	} else if ( payWay.toString().equals( "3" ) && !map.containsKey( "isAlipay" ) ) {
	    updateReturnStatus( pUser, map, returnNo );//储值卡退款退款
	}
	return resultMap;
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public void returnAlipayDeposit( Map< String,Object > params ) {
	String aucNo = params.get( "outTradeNo" ).toString();//订单号
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByPreNo( aucNo );
	params.put( "user_id", deposit.getUserId() );
	params.put( "id", deposit.getId() );
	params.put( "deposit_money", deposit.getDepositMoney() );
	params.put( "deposit_no", deposit.getDepositNo() );
	String returnNo = "YSHTK" + System.currentTimeMillis();
	params.put( "return_no", returnNo );
	params.put( "pay_way", deposit.getPayWay() );
	params.put( "shop_id", deposit.getShopId() );
	Member member = memberService.findMemberById( deposit.getUserId(), null );//根据会员id查询会员信息
	// todo 调用小屁孩的接口，根据公众号id查询公众号信息
	WxPublicUsers wx = null;
	if ( CommonUtil.isNotEmpty( member.getPublicId() ) ) {
	    //	    wx = wxPublicUsersMapper.selectByPrimaryKey(member.getPublicId());
	}
	updateReturnStatus( wx, params, returnNo );

    }

    /**
     * 修改退款定金的状态
     */
    private void updateReturnStatus( WxPublicUsers pUser, Map< String,Object > map, String returnNo ) {
	/*Integer memberId = CommonUtil.toInteger(map.get("user_id"));*/
	Integer preId = CommonUtil.toInteger( map.get( "id" ) );
	/*Double money = CommonUtil.toDouble(map.get("deposit_money"));
	String depNo = map.get("deposit_no").toString();*/

	MallPresaleDeposit deposit = new MallPresaleDeposit();

	deposit.setId( preId );
	deposit.setDepositStatus( -1 );
	deposit.setReturnNo( map.get( "return_no" ).toString() );
	deposit.setReturnTime( new Date() );
	int num = mallPresaleDepositDAO.updateById( deposit );

	if ( num > 0 ) {
	    //储值卡添加退款记录
	    /*if(map.get("pay_way").toString().equals("2")){
		    //查询该定金的消费记录
		    UserConsume consume = consumeMapper.findByOrderCode1(depNo);
		    if(CommonUtil.isNotEmpty(map.get("shop_id"))){
			    //通过店铺id查询店铺信息
			    Store store = mallStoreDao.selectByPrimaryKey(CommonUtil.toInteger(map.get("shop_id")));
			    int shopsId = store.getWxShopId();
			    if(shopsId <= 0){
				    shopsId = store.getId();
			    }
			    consume.setStoreid(shopsId);//店铺id
		    }
		    int xiaofeiId = 0;
		    if(consume == null){
			    consume =  new UserConsume();
		    }else{
			    xiaofeiId = consume.getId();
			    consume.setId(null);
		    }
		    if(CommonUtil.isNotEmpty(pUser)){
			    consume.setPublicId(pUser.getId());//公众号id
		    }
		    Member member = memberService.findById(memberId);
		    consume.setBususerid(member.getBusid());//商户id
		    consume.setModuletype(Byte.valueOf("0"));//模块类型 0 商城

		    consume.setMemberid(memberId);//买家id
		    consume.setRecordtype(Byte.valueOf("3"));//记录类型 3退款记录
		    consume.setUctype(Byte.valueOf("12"));//消费类型
		    consume.setTotalmoney(money);//退款金额
		    consume.setDiscountmoney(null);
		    consume.setOrderid(preId);//订单id
		    int payWay = CommonUtil.toInteger(map.get("pay_way"));//支付方式
		    if(payWay == 1){//微信支付
			    payWay = 1;
		    }else if(payWay == 2){//储值卡支付
			    payWay = 5;
		    }else if(payWay == 3){//支付宝支付
			    payWay = 0;
		    }
		    consume.setUctable("t_mall_auction_margin");//详情表名
		    consume.setCreatedate(new Date());//创建时间
		    consume.setPaymenttype(Byte.valueOf(payWay+""));//支付方式
		    consume.setPaystatus(Byte.valueOf("3"));//支付状态 3 退单
		    consume.setOrdercode(returnNo);//订单号微信或支付宝或自定义
		    //添加退款记录
		    consumeMapper.insertSelective(consume);
		    if(consume.getId() > 0){//退款成功
			    if(xiaofeiId > 0){
				    UserConsumeRefundKey refund = new UserConsumeRefundKey();
				    refund.setConsumeid(xiaofeiId);//消费id
				    refund.setRefundid(consume.getId());//新添加的退款id
				    consumeRefundMapper.insertSelective(refund);
			    }
		    }
	    }*/

	}
    }

    @Override
    public MallPresaleDeposit getPresaleDepositById( int id ) {
	return mallPresaleDepositDAO.selectById( id );
    }

    @Override
    public int updatePresaleDepositById( MallPresaleDeposit deposit ) {
	return mallPresaleDepositDAO.updateById( deposit );
    }

    @Override
    public MallPresaleDeposit selectCountByPresaleId( Map< String,Object > maps ) {
	return mallPresaleDepositDAO.selectCountByPresaleId( maps );
    }

    /**
     * 交纳定金成功
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public Map< String,Object > addMessage( Map< String,Object > params, String memberId ) {
	Map< String,Object > result = new HashMap< String,Object >();
	int preId = 0;
	if ( CommonUtil.isNotEmpty( params.get( "preId" ) ) ) {
	    preId = CommonUtil.toInteger( params.get( "preId" ) );
	}
	MallPresaleMessageRemind message = new MallPresaleMessageRemind();
	message.setRemindUserId( CommonUtil.toInteger( memberId ) );
	message.setPresaleId( preId );
	message.setIsRemind( 0 );
	MallPresaleMessageRemind preMessage = mallPresaleMessageRemindDAO.selectByPresale( message );

	MallPresale presale = mallPresaleDAO.selectById( preId );

	message.setIsOpen( 1 );

	int num = 0;
	if ( preMessage == null ) {
	    message.setCreateTime( new Date() );
	    num = mallPresaleMessageRemindDAO.insert( message );
	} else {
	    message.setId( preMessage.getId() );
	    num = mallPresaleMessageRemindDAO.updateById( message );
	}
	if ( num > 0 ) {

	    String key = Constants.REDIS_KEY + "presale_message";
	    String field = presale.getId().toString();
	    JSONArray arr = new JSONArray();
	    String json = JedisUtil.maoget( key, field );
	    if ( CommonUtil.isNotEmpty( json ) ) {
		arr = JSONArray.fromObject( json );
	    }

	    JSONObject obj = new JSONObject();
	    obj.put( "busUserId", presale.getUserId() );
	    obj.put( "wxMemberId", memberId );
	    obj.put( "isStartRemain", 0 );
	    obj.put( "isEndRemain", 0 );
	    arr.add( obj );
	    JedisUtil.map( key, field, arr.toString() );

	    result.put( "result", true );
	} else {
	    result.put( "result", false );
	}
	return result;
    }

    public static void main( String[] args ) {
	int num = 0;
	System.out.println( "num = " + num );
    }

    @Override
    public MallPresaleDeposit selectByDeposit( int depositId ) {
	return mallPresaleDepositDAO.selectById( depositId );
    }

}
