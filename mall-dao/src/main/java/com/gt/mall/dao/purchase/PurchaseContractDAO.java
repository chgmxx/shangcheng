package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseContract;
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
public interface PurchaseContractDAO extends BaseMapper<PurchaseContract> {

    /**
     * 分页查询
     *
     * @param parms contractTitle:合同标题，busId:商家id, pageFirst:页数,pageLast：记录数
     * @return list
     */
    List<Map<String, Object>> findList(Map<String, Object> parms);

    /**
     * 分页查询的订单条数
     *
     * @param parms contractTitle:合同标题，busId:商家id
     * @return 数量
     */
    Integer findListCount(Map<String, Object> parms);

    /**
     * 批量删除合同
     *
     * @param contractIds Id集合
     * @return 是否成功
     */
    int deleteContracts(@Param("contractIds") String contractIds);

    /**
     * 所有合同
     *
     * @param busId 商家id
     * @return list
     */
    List<Map<String, Object>> findAllList(Integer busId);
}