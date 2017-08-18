package com.gt.mall.service.inter.member;

import java.util.List;
import java.util.Map;

/**
 * 优惠券、会员卡相关操作
 * User : yangqian
 * Date : 2017/8/14 0014
 * Time : 10:39
 */
public interface CardService {

    /**
     * 根据卡包id查询卡券信息 map中key guoqi=1标示该包或该券过期
     *
     * @param receiveId 卡券包id
     *
     * @return 卡券信息
     */
    public Map< String,Object > findDuofenCardByReceiveId( int receiveId );

    /**
     * 根据商家 查询商家拥有的卡包信息
     *
     * @param busUserId 商家id
     *
     * @return 卡包信息
     */
    public List< Map > findReceiveByBusUserId( int busUserId );

    /**
     * 商场支付成功回调 分配卡券
     * @param params {receiveId:卡包id，num：数量，memberId：粉丝id}
     * @return true 成功  false 失败
     */
    public boolean successPayBack(Map<String,Object> params);
}


