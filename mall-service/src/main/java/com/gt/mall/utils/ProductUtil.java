package com.gt.mall.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.mall.constant.Constants;
import com.gt.mall.entity.product.MallProduct;
import org.apache.log4j.Logger;

import java.util.Map;

public class ProductUtil {

    private static final Logger log = Logger.getLogger( ProductUtil.class );

    /**
     * 判断商品信息
     */
    public static String isProductError( MallProduct product ) {
        if ( CommonUtil.isEmpty( product ) ) {
            return "商品不存在";
        }
        if ( CommonUtil.isNotEmpty( product.getIsDelete() ) && product.getIsDelete() == 1 ) {
            return "商品被删除";
        }
        if ( CommonUtil.isNotEmpty( product.getCheckStatus() ) && product.getCheckStatus() != 1 ) {
            return "商品未审核或审核中";
        }
        if ( CommonUtil.isNotEmpty( product.getIsPublish() ) && product.getIsPublish() != 1 ) {
            return "商品还未发布或未上架";
        }
        if ( CommonUtil.isNotEmpty( product.getProTypeId() ) && product.getProTypeId() == 3 ) {
            return "商品已下架";
        }
        return null;
    }

    public static int getGuanzhuNum( MallProduct product ) {
        String key = Constants.REDIS_KEY + "proViewNum";
        int viewNum = 0;
        String viewNums = "";
        if ( JedisUtil.hExists( key, product.getId().toString() ) ) {
            viewNums = JedisUtil.maoget( key, product.getId().toString() );
        }
        if ( viewNums == null || viewNums.equals( "" ) ) {
            if ( CommonUtil.isNotEmpty( product.getViewsNum() ) ) {
                viewNums = product.getViewsNum().toString();
            }
        }
        if ( viewNums != null && !viewNums.equals( "" ) ) {
            viewNum = CommonUtil.toInteger( viewNums );
        }
        if ( viewNum + 1 < 1000000000 ) {
            JedisUtil.map( key, product.getId().toString(), ( viewNum + 1 ) + "" );
        }
        return viewNum;
    }

    public static JSONObject getCardReceive( Map< String,Object > cardMap ) {
        JSONObject obj = new JSONObject();
        JSONArray cardList = new JSONArray();
        JSONArray cardmessage = new JSONArray();
        double cardMoney = 0;
        if ( CommonUtil.isNotEmpty( cardMap.get( "recevieMap" ) ) ) {
            JSONObject cardObj = JSONObject.parseObject( cardMap.get( "recevieMap" ).toString() );
            cardMoney = cardObj.getDouble( "buyMoney" );
            if ( CommonUtil.isNotEmpty( cardObj.get( "cardMessage" ) ) ) {
                cardmessage = JSONArray.parseArray( cardObj.get( "cardMessage" ).toString() );
            }
            obj.put( "cardRecevieId", cardObj.getInteger( "id" ) );
        }
        if ( CommonUtil.isNotEmpty( cardMap.get( "returnDuofencardList" ) ) ) {
            cardList = JSONArray.parseArray( cardMap.get( "returnDuofencardList" ).toString() );
        }
        if ( cardmessage != null && cardmessage.size() > 0 && cardList != null && cardList.size() > 0 ) {
            for ( Object object : cardmessage ) {
                int isGuoqi = 0;
                JSONObject card = JSONObject.parseObject( object.toString() );
                for ( Object object2 : cardList ) {
                    JSONObject cardObj = JSONObject.parseObject( object2.toString() );
                    if ( card.getString( "cardId" ).equals( cardObj.getString( "id" ) ) ) {
                        isGuoqi = cardObj.getInteger( "guoqi" );
                    }
                    if ( cardObj.getInteger( "cardType" ) == 4 && cardObj.containsKey( "money" ) && CommonUtil.isNotEmpty( cardObj.get( "money" ) ) ) {
                        cardMoney = cardObj.getDouble( "money" );
                    }
                }
                card.put( "isGuoqi", isGuoqi );
            }
            obj.put( "cardmessage", cardmessage );
            //	    return cardmessage;
        }
        if ( cardMoney > 0 ) {
            obj.put( "cardMoney", cardMoney );
        }
        return obj;
    }

}
