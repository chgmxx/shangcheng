package com.gt.mall.dao.order;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.order.MallDaifu;

import java.util.List;

/**
 * <p>
 * 找人代付 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallDaifuDAO extends BaseMapper< MallDaifu > {

    /**
     * 根据代付订单号查询代付信息
     *
     * @param dfOrderNo
     *
     * @return
     */
    MallDaifu selectByDfOrderNo( String dfOrderNo );

    /**
     * 查询用户的代付信息
     *
     * @param daifu
     *
     * @return
     */
    MallDaifu selectBydf( MallDaifu daifu );

    /**
     * 查询是否已经有用户代付了
     *
     * @param daifu
     *
     * @return
     */
    List< MallDaifu > selectByPayDaifu( MallDaifu daifu );
}
