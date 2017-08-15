package com.gt.mall.util;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.exception.SignException;
import com.gt.api.util.sign.SignHttpUtils;
import com.gt.mall.bean.params.MallAllEntity;
import com.gt.mall.bean.params.MallEntity;
import com.gt.mall.bean.params.MallShopEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:10
 */
public class Test {

    private static String SignHttpByJSON( Object params, String url ) {
	url = "http://113.106.202.53:13885/" + url;
	String signKey = "MV8MMFQUMU1HJ6F2GNH40ZFJJ7Q8LNVM";
	try {
	    String result = SignHttpUtils.postByHttp( url, params, signKey );
	    System.out.println( "result:" + result );

	    if ( CommonUtil.isNotEmpty( result ) ) {
		JSONObject resultObj = JSONObject.parseObject( result );
		if ( resultObj.getInteger( "code" ) == 0 ) {
		    System.out.println( "data = " + resultObj.getString( "data" ) );
		    return resultObj.getString( "data" );
		} else {
		    System.out.println( "调用会员接口异常：" + resultObj.getString( "msg" ) );
		}
	    }
	} catch ( SignException e ) {
	    e.printStackTrace();
	}
	return null;
    }


    /**
     * post请求带请求参数
     */
    public static void main( String[] args ) {

	String startTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );

	System.out.println( "1111 = " );
	//	String url = "http://113.106.202.53:13885/memberAPI/member/findByMemberId";
	Map< String,Object > map = new HashMap<>();
//	map.put( "memberId", 1225352 );
//	map.put( "shopId", 17);
//	String result = SignHttpByJSON( map, "/memberAPI/member/findCardByMembeId" );


	map.put( "memberId", 1225352 );
	String result = SignHttpByJSON( map, "/memberAPI/member/findByMemberId" );

//	map.put( "busId", 42 );
//	String result = SignHttpByJSON( map, "/memberAPI/cardCouponseApi/findReceiveByBusUserId" );

//	map.put( "memberId", 1225352 );
//	String result = SignHttpByJSON( map, "/memberAPI/member/isMember" );



//	MallAllEntity mAll= getMallAllEntity();
//	String result = SignHttpByJSON( mAll, "/memberAPI/memberCountApi/memberCountMoneyByShop" );






	String endTime = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATETIME_FORMAT );
	long second = DateTimeKit.minsBetween( startTime, endTime, 1000, DateTimeKit.DEFAULT_DATETIME_FORMAT );
	System.out.println( "耗时：" + second + "秒" );
    }


    private static MallAllEntity getMallAllEntity(){
	MallAllEntity mAll=new MallAllEntity();
	mAll.setMemberId(1225636);;  //粉丝信息
	mAll.setTotalMoney(2001.78);; //订单总金额
	mAll.setUseFenbi(1);;  //是否使用粉币
	mAll.setUserJifen(1);;  //是否使用积分

	MallShopEntity mallShop=new MallShopEntity();
	mallShop.setShopId(1);;  //门店id
	mallShop.setUseCoupon(0); //是否使用优惠券
	mallShop.setCouponType(1);; //优惠券类型 0微信 1多粉优惠券
	mallShop.setCoupondId(2849);;  //卡券id 81微信优惠券





	Map<Integer, MallEntity> ms=new HashMap<>();
	//		MallEntity m=new MallEntity();
	//		m.setMallId(1);;  //订单详情id or 生成序列号
	//		m.setNumber(1);;  // 商品数量
	//		m.setTotalMoneyOne(3000.0);;  //商品应付单价格
	//		m.setTotalMoneyAll(3000.0);; //商品订单总价格  用来计算
	//		m.setUserCard(1);;  //是否能用会员卡打折
	//		m.setUseCoupon(1);;  //是否使用优惠券
	//		m.setUseFenbi(1);;  //是否使用粉币
	//		m.setUserJifen(1);  //是否使用积分
	//		ms.put(m.getMallId(), m);
	//
	//
	//		MallEntity m1=new MallEntity();
	//		m1.setMallId(2);;  //订单详情id or 生成序列号
	//		m1.setNumber(3);;  // 商品数量
	//		m1.setTotalMoneyOne(10.0);;  //商品应付单价格
	//		m1.setTotalMoneyAll(30.0);; //商品订单总价格  用来计算
	//		m1.setUserCard(1);;  //是否能用会员卡打折
	//		m1.setUseCoupon(1);;  //是否使用优惠券
	//		m1.setUseFenbi(1);;  //是否使用粉币
	//		m1.setUserJifen(1);  //是否使用积分
	//		ms.put(m1.getMallId(), m1);
	//
	//
	//		MallEntity m2=new MallEntity();
	//		m2.setMallId(3);;  //订单详情id or 生成序列号
	//		m2.setNumber(1);;  // 商品数量
	//		m2.setTotalMoneyOne(6000.0);;  //商品应付单价格
	//		m2.setTotalMoneyAll(6000.0);; //商品订单总价格  用来计算
	//		m2.setUserCard(1);;  //是否能用会员卡打折
	//		m2.setUseCoupon(1);;  //是否使用优惠券
	//		m2.setUseFenbi(1);;  //是否使用粉币
	//		m2.setUserJifen(1);  //是否使用积分
	//		ms.put(m2.getMallId(), m2);


	MallEntity m3=new MallEntity();
	m3.setMallId(3);;  //订单详情id or 生成序列号
	m3.setNumber(1);;  // 商品数量
	m3.setTotalMoneyOne(0.05);;  //商品应付单价格
	m3.setTotalMoneyAll(0.05);; //商品订单总价格  用来计算
	m3.setUserCard(1);;  //是否能用会员卡打折
	m3.setUseCoupon(0);;  //是否使用优惠券
	m3.setUseFenbi(0);;  //是否使用粉币
	m3.setUserJifen(0);  //是否使用积分
	ms.put(m3.getMallId(), m3);



	MallEntity m4=new MallEntity();
	m4.setMallId(4);;  //订单详情id or 生成序列号
	m4.setNumber(1);;  // 商品数量
	m4.setTotalMoneyOne(22.0);;  //商品应付单价格
	m4.setTotalMoneyAll(22.0);; //商品订单总价格  用来计算
	m4.setUserCard(0);;  //是否能用会员卡打折
	m4.setUseCoupon(1);;  //是否使用优惠券
	m4.setUseFenbi(1);;  //是否使用粉币
	m4.setUserJifen(1);  //是否使用积分
	ms.put(m4.getMallId(), m4);



	MallEntity m5=new MallEntity();
	m5.setMallId(5);;  //订单详情id or 生成序列号
	m5.setNumber(1);;  // 商品数量
	m5.setTotalMoneyOne(0.05);;  //商品应付单价格
	m5.setTotalMoneyAll(0.05);; //商品订单总价格  用来计算
	m5.setUserCard(1);;  //是否能用会员卡打折
	m5.setUseCoupon(1);;  //是否使用优惠券
	m5.setUseFenbi(1);;  //是否使用粉币
	m5.setUserJifen(1);  //是否使用积分
	ms.put(m5.getMallId(), m5);


	MallEntity m8=new MallEntity();
	m8.setMallId(8);;  //订单详情id or 生成序列号
	m8.setNumber(1);;  // 商品数量
	m8.setTotalMoneyOne(3000.0);;  //商品应付单价格
	m8.setTotalMoneyAll(3000.0);; //商品订单总价格  用来计算
	m8.setUserCard(1);;  //是否能用会员卡打折
	m8.setUseCoupon(1);;  //是否使用优惠券
	m8.setUseFenbi(0);;  //是否使用粉币
	m8.setUserJifen(0);  //是否使用积分
	ms.put(m8.getMallId(), m8);


	mallShop.setMalls(ms); //门店商品详情








	MallShopEntity mallShop1=new MallShopEntity();
	mallShop1.setShopId(2);;  //门店id
	mallShop1.setUseCoupon(1); //是否使用优惠券
	mallShop1.setCouponType(1);; //优惠券类型 0微信 1多粉优惠券
	mallShop1.setCoupondId(2852);;  //卡券id 81微信优惠券


	Map<Integer,MallEntity > ms1=new HashMap<>();

	MallEntity m6=new MallEntity();
	m6.setMallId(6);;  //订单详情id or 生成序列号
	m6.setNumber(1);;  // 商品数量
	m6.setTotalMoneyOne(100.0);;  //商品应付单价格
	m6.setTotalMoneyAll(100.0);; //商品订单总价格  用来计算
	m6.setUserCard(1);;  //是否能用会员卡打折
	m6.setUseCoupon(1);;  //是否使用优惠券
	m6.setUseFenbi(1);;  //是否使用粉币
	m6.setUserJifen(1);  //是否使用积分
	ms1.put(m6.getMallId(), m6);


	MallEntity m7=new MallEntity();
	m7.setMallId(7);;  //订单详情id or 生成序列号
	m7.setNumber(1);;  // 商品数量
	m7.setTotalMoneyOne(30.0);;  //商品应付单价格
	m7.setTotalMoneyAll(30.0);; //商品订单总价格  用来计算
	m7.setUserCard(1);;  //是否能用会员卡打折
	m7.setUseCoupon(1);;  //是否使用优惠券
	m7.setUseFenbi(1);;  //是否使用粉币
	m7.setUserJifen(1);  //是否使用积分
	ms1.put(m7.getMallId(), m7);

	mallShop1.setMalls(ms1); //门店商品详情
	Map<Integer , MallShopEntity> map=new HashMap<>();
	map.put(mallShop.getShopId(), mallShop);
	map.put(mallShop1.getShopId(), mallShop1);
	mAll.setMallShops(map);

	return mAll;
    }
}
