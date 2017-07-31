package com.gt.mall.dao.groupbuy;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.groupbuy.MallGroupBuyPrice;

import java.util.List;

/**
 * <p>
 * 团购价格表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupBuyPriceDAO extends BaseMapper<MallGroupBuyPrice> {


    /**
     * 通过团购id查询团购价格列表
     *
     * @param groupBuyId 团购id
     * @return  团购价格列表
     */
//    List<MallGroupBuyPrice> selectPriceByGroupId(Integer groupBuyId);

    /**
     * 通过团购id 修改信息
     *
     * @param record 团购价格信息
     * @return 是否成功
     */
    int updateByGroupBuyId(MallGroupBuyPrice record);
}