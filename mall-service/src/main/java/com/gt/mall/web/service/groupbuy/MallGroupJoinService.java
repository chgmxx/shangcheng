package com.gt.mall.web.service.groupbuy;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.groupbuy.MallGroupJoin;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参团表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupJoinService extends BaseService<MallGroupJoin> {

    /**
     * 获取开团人员
     *
     * @param params groupBuyId：团购id，joinUserId：用户id,buyerUserId:用户Id
     * @param member 用户
     * @return List
     */
    List<Map<String, Object>> getJoinGroup(Map<String, Object> params, Member member);

    /**
     * 查询团购参与人
     *
     * @param params groupBuyId：团购id，joinId：参团id
     * @return 参与人
     */
    List<Map<String, Object>> selectJoinByjoinId(Map<String, Object> params);

    /**
     * 查询用户参加团购的数量
     *
     * @param params groupBuyId：团购id，joinUserId：用户id
     * @return 数量
     */
    int selectCountByBuyId(Map<String, Object> params);

}
