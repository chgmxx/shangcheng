package com.gt.mall.utils;

import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.param.phone.order.PhoneToOrderDTO;
import com.gt.mall.result.phone.order.PhoneToOrderBusResult;
import com.gt.mall.result.phone.order.PhoneToOrderWayResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 进入提交订单页面的工具类
 * User : yangqian
 * Date : 2017/10/24 0024
 * Time : 14:29
 */
public class ToOrderUtil {

    /**
     * 获取商家的支付方式
     *
     * @param browerType      浏览器类型
     * @param busResult       商家返回结果
     * @param cardMap         卡券对象
     * @param phoneToOrderDTO 页面传参
     * @param isShowTake      商家是否有上门自提
     * @param mallPaySetList  商城设置
     * @param proTypeId       商品类型 参考t_mall_product表
     *
     * @return 支付方式集合
     */
    public static List< PhoneToOrderWayResult > getPayWay( int browerType, PhoneToOrderBusResult busResult, Map cardMap, PhoneToOrderDTO phoneToOrderDTO,
		    List< Map< String,Object > > isShowTake, List< MallPaySet > mallPaySetList, int proTypeId ) {
	List< PhoneToOrderWayResult > payWayList = new ArrayList<>();

	if ( browerType == 1 && CommonUtil.isNotEmpty( busResult.getPublicId() ) && busResult.getPublicId() > 0 ) {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 1, "微信支付" );
	    payWayList.add( result );
	} else {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 9, "支付宝支付" );
	    payWayList.add( result );
	}
	if ( CommonUtil.isNotEmpty( cardMap ) ) {
	    if ( CommonUtil.isNotEmpty( cardMap.get( "ctId" ) ) ) {
		if ( cardMap.get( "ctId" ).toString().equals( "3" ) ) {
		    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 3, "储值卡支付" );
		    payWayList.add( result );
		}
	    }
	}
	int isHuodao = 0;//是否显示货到付款
	int isDaifu = 0;//是否允许代付
	if ( mallPaySetList != null && mallPaySetList.size() > 0 ) {
	    for ( MallPaySet mallPaySet : mallPaySetList ) {
		if ( mallPaySet.getUserId().toString().equals( busResult.getBusId().toString() ) ) {
		    if ( CommonUtil.isNotEmpty( mallPaySet.getIsDeliveryPay() ) ) {
			if ( mallPaySet.getIsDeliveryPay().toString().equals( "1" ) ) {
			    isHuodao = 1;//允许货到付款
			}
		    }
		    if ( CommonUtil.isNotEmpty( mallPaySet.getIsDaifu() ) ) {
			if ( mallPaySet.getIsDaifu().toString().equals( "1" ) ) {
			    isDaifu = 1;//允许代付
			}
		    }
		}
	    }
	}
	if ( isHuodao == 1 && proTypeId == 0 ) {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 2, "货到付款" );
	    payWayList.add( result );
	}
	if ( phoneToOrderDTO.getType() == 2 ) {//积分支付
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 4, "积分支付" );
	    payWayList.add( result );
	} else if ( phoneToOrderDTO.getType() == 5 ) {//粉币支付
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 8, "粉币支付" );
	    payWayList.add( result );
	}
	int isShowTakeTheir = 0;//是否开启货到付款
	if ( isShowTake != null && isShowTake.size() > 0 ) {
	    for ( Map< String,Object > isShowMap : isShowTake ) {
		if ( busResult.getBusId().toString().equals( isShowMap.get( "user_id" ).toString() ) ) {
		    isShowTakeTheir = 1;
		    break;
		}
	    }
	}
	if ( isShowTakeTheir == 1 ) {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 6, "到店支付" );
	    payWayList.add( result );
	}
	if ( isDaifu == 1 ) {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 7, "找人代付" );
	    payWayList.add( result );
	}
	return payWayList;
    }

    /**
     * 获取商家的配送方式
     */
    public static List< PhoneToOrderWayResult > getDeliveryWay( PhoneToOrderDTO params, int proTypeId, List< Map< String,Object > > isShowTake, int busId ) {
	List< PhoneToOrderWayResult > wayResultList = new ArrayList<>();
	int toshop = 0;
	if ( CommonUtil.isNotEmpty( params.getToShop() ) ) {
	    toshop = params.getToShop();
	}
	if ( toshop == 1 ) {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 3, "到店购买" );
	    wayResultList.add( result );
	} else {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 1, "快递配送" );
	    wayResultList.add( result );
	}
	int isShowDaoDian = 0;
	if ( isShowTake != null && isShowTake.size() > 0 ) {
	    for ( Map< String,Object > isShowMap : isShowTake ) {
		if ( CommonUtil.toString( busId ).equals( isShowMap.get( "user_id" ).toString() ) ) {
		    isShowDaoDian = 1;
		    break;
		}
	    }
	}
	if ( proTypeId == 0 && isShowDaoDian == 1 ) {
	    PhoneToOrderWayResult result = new PhoneToOrderWayResult( 2, "到店自提" );
	    wayResultList.add( result );
	}

	return wayResultList;
    }
}
