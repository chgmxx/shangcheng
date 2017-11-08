package com.gt.mall.service.web.presale;

import com.gt.mall.base.BaseService;
import com.gt.api.bean.session.Member;
import com.gt.mall.entity.presale.MallPresaleDeposit;
import com.gt.mall.param.phone.presale.PhoneAddDepositDTO;
import com.gt.mall.utils.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户缴纳定金表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleDepositService extends BaseService< MallPresaleDeposit > {

    /**
     * 通过店铺id来查询保证金
     */
    PageUtil selectPresaleByShopId( Map< String,Object > param, List< Map< String,Object > > shoplist );

    /**
     * 交纳保证金成功返回
     */
    int paySuccessPresale( Map< String,Object > params );

    /**
     * 交纳保证金
     */
    Map< String,Object > addDeposit( Map< String,Object > params, Member member, Integer browser ) throws Exception;

    /**
     * 交纳保证金
     */
    Map< String,Object > addDeposit( PhoneAddDepositDTO depositDTO, Member member, Integer browser ) throws Exception;

    /**
     * 根据用户id查询我的所有的保证金
     *
     * @param deposit id
     *
     * @return 保证金信息
     */
    List< Map< String,Object > > getMyPresale( MallPresaleDeposit deposit );

    /**
     * 退还保证金
     */
    void returnDeposit() throws Exception;

    /**
     * 通过id查询交纳保证金的情况
     *
     * @param id id
     *
     * @return 交纳保证金的情况
     */
    MallPresaleDeposit getPresaleDepositById( int id );

    /**
     * 修改交纳保证金的情况
     */
    int updatePresaleDepositById( MallPresaleDeposit deposit );

    /**
     * 查询交纳定金的信息
     */
    MallPresaleDeposit selectCountByPresaleId( Map< String,Object > maps );

    /**
     * 添加消息提醒记录
     */
    Map< String,Object > addMessage( Map< String,Object > params, String memberId );

    /**
     * 根据定金id查询定金信息
     */
    MallPresaleDeposit selectByDeposit( int deposit );

    /**
     * 退定金
     */
    void returnAlipayDeposit( Map< String,Object > params );

    /**
     * 退定金
     */
    Map< String,Object > returnEndPresale( Map< String,Object > map ) throws Exception;

}
