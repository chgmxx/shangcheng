package com.gt.mall.service.web.pifa;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.pifa.MallPifaPrice;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 批发价格表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPifaPriceService extends BaseService< MallPifaPrice > {

    /**
     * 编辑批发价格
     *
     * @Title: editPifaPrice
     */
    void editPifaPrice( Map< String,Object > map, int pfId, boolean flag );

    /**
     * 通过批发id查询批发信息
     *
     * @param groupId
     *
     * @return
     */
    List< MallPifaPrice > selectPriceByGroupId( int groupId );

    /**
     * 通过批发id查询批发价
     *
     * @param pifaId 批发id
     * @param invId  库存id
     *
     * @return 批发价
     */
    List< MallPifaPrice > selectPriceByInvId( int pifaId, int invId );

    /**
     * 根据规格值查询批发价
     *
     * @param specificaIds 规格值id
     * @param pifaId       批发id
     *
     * @return 批发价信息
     */
    MallPifaPrice selectPifaBySpecifica( String specificaIds, int pifaId );
}
