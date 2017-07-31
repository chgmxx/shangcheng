package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseLanguage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
public interface PurchaseLanguageDAO extends BaseMapper<PurchaseLanguage> {

    /**
     * 查询该订单id的留言数据
     *
     * @param orderId 订单id
     * @return list
     */
    List<Map<String, Object>> findLanguangeList(int orderId);

    /**
     * 查询该订单id的留言数据
     *
     * @param record memberId：用户Id,orderId 订单id
     * @return list
     */
    List<Map<String, Object>> findLanguangeDetails(PurchaseLanguage record);

    /**
     * 查询该订单id的留言数据
     *
     * @param orderIds 订单id集合
     * @return list
     */
    List<Map<String, Object>> findLanguangeNotRead(@Param("orderIds") String orderIds);

    /**
     * 查询该订单id的所有留言数据
     *
     * @param orderId 订单id
     * @return list
     */
    List<Map<String, Object>> findAllList(int orderId);


    /**
     * 把该订单下的所有留言设为已阅
     *
     * @param orderId 订单id
     * @return 是否成功
     */
    int updateLanguangeByOrderId(int orderId);

}