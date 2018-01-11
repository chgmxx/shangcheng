package com.gt.mall.dao.basic;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.basic.MallSecuritytradeQuit;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 退出担保交易申请表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-10-09
 */
public interface MallSecuritytradeQuitDAO extends BaseMapper< MallSecuritytradeQuit > {

    /**
     * 统计数量
     *
     * @param params
     *
     * @return 数量
     */
    int selectAllCount( Map< String,Object > params );

    /**
     * 得到所有的申请列表
     *
     * @param params userIds 商家Ids，firstNum：页数，maxNum 数量
     */
    List< Map< String,Object > > selectAllByPage( Map< String,Object > params );

}