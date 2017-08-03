package com.gt.mall.web.service.pifa;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifaApply;

/**
 * <p>
 * 批发申请表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPifaApplyService extends BaseService< MallPifaApply > {

    /**
     * 获取批发审核的状态
     */
    public int getPifaApplay(Member member,MallPaySet set);

    /**
     * 是否可以选择批发商品
     */
    public boolean isPifa(Member member);

    /**
     * 商家是否已经开启批发商品
     * @param member 会员对象
     * @param set 设置
     * @return true 已设置
     */
    public boolean isPifaPublic(Member member,MallPaySet set);

}
