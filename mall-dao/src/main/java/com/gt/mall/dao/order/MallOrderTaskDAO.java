package com.gt.mall.dao.order;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.order.MallOrderTask;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 订单任务表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2018-02-02
 */
public interface MallOrderTaskDAO extends BaseMapper<MallOrderTask > {


    /**
     * 查询数据
     *
     * @param params
     *
     * @return
     */
    List< MallOrderTask > findByType( Map< String,Object > params );

}