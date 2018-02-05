package com.gt.mall.utils;

import com.gt.api.bean.session.Member;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import org.apache.log4j.Logger;

public class AddOrderUtil {

    private static final Logger logger = Logger.getLogger( AddOrderUtil.class );

    /**
     * 判断积分和粉币是否足够
     *
     * @param selectPayWayId    4 积分支付  8 粉币支付
     * @param member            会员对象
     * @param productTotalMoney 商品总价
     */
    public static void isJiFenOrFenbi( int selectPayWayId, Member member, Double productTotalMoney ) {
        if ( selectPayWayId == 8 ) {
            boolean isFenbi = true;
            if ( CommonUtil.isNotEmpty( member.getFansCurrency() ) ) {
                double fenbi = member.getFansCurrency();
                if ( fenbi < productTotalMoney || fenbi < 0 ) {
                    isFenbi = false;
                }
            } else {
                isFenbi = false;
            }
            if ( !isFenbi ) {
                throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "您的粉币不够，不能用粉币来兑换这件商品" );
            }
        }
        if ( selectPayWayId == 4 ) {
            boolean isJifen = true;
            if ( CommonUtil.isNotEmpty( member.getIntegral() ) ) {
                double fenbi = member.getIntegral();
                if ( fenbi < productTotalMoney || fenbi < 0 ) {
                    isJifen = false;
                }
            } else {
                isJifen = false;
            }
            if ( !isJifen ) {
                throw new BusinessException( ResponseEnums.PARAMS_NULL_ERROR.getCode(), "您的粉币不够，不能用粉币来兑换这件商品" );
            }
        }
    }

}
