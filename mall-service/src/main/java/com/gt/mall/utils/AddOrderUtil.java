package com.gt.mall.utils;

import com.gt.api.bean.session.Member;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;

/**
 * 订单工具类
 *
 * @author yangqian
 */
public class AddOrderUtil {

    private AddOrderUtil() {
    }

    /**
     * 判断积分和粉币是否足够
     *
     * @param selectPayWayId    4 积分支付  8 粉币支付
     * @param member            会员对象
     * @param productTotalMoney 商品总价
     */
    public static void isJiFenOrFenbi( Integer selectPayWayId, Member member, Double productTotalMoney ) {
        if ( selectPayWayId == null || member == null|| productTotalMoney == null ) {
            return;
        }
        if ( selectPayWayId == 8 && member.getFansCurrency() == null ) {
            throw new BusinessException( ResponseEnums.FENBI_EXCHANGE_NULL_ERROR.getCode(), ResponseEnums.FENBI_EXCHANGE_NULL_ERROR.getDesc() );
        }
        if ( selectPayWayId == 4 && member.getIntegral() == null ) {
            throw new BusinessException( ResponseEnums.JIFEN_EXCHANGE_NULL_ERROR.getCode(), ResponseEnums.JIFEN_EXCHANGE_NULL_ERROR.getDesc() );
        }
        if ( selectPayWayId == 8 ) {
            if ( member.getFansCurrency() < productTotalMoney || member.getFansCurrency() < 0 ) {
                throw new BusinessException( ResponseEnums.FENBI_EXCHANGE_NULL_ERROR.getCode(), ResponseEnums.FENBI_EXCHANGE_NULL_ERROR.getDesc() );
            }
        } else if ( selectPayWayId == 4 ) {
            if ( member.getIntegral() < productTotalMoney || member.getIntegral() < 0 ) {
                throw new BusinessException( ResponseEnums.JIFEN_EXCHANGE_NULL_ERROR.getCode(), ResponseEnums.JIFEN_EXCHANGE_NULL_ERROR.getDesc() );
            }
        }
    }

}
