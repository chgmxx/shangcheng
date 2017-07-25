package com.gt.mall.dao.order;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.order.MallOrder;

import java.util.Map;

/**
 * <p>
 * 商城订单 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderDAO extends BaseMapper< MallOrder > {


    /**
     * 获取拍卖商品的 订单信息
     * @param auctId
     * @return
     */
    Map<String, Object> selectOrderByAuctId(Integer auctId);
}