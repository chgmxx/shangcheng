package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseCarousel;
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
public interface PurchaseCarouselDAO extends BaseMapper<PurchaseCarousel> {


    /**
     * 分页查询
     *
     * @param parms busId:商家id，carouselUrl：轮播图链接地址，pageFirst:页数,pageLast：记录数
     * @return list
     */
    List<Map<String, Object>> findList(Map<String, Object> parms);

    /**
     * 分页查询的条数
     *
     * @param parms busId:商家id，carouselUrl：轮播图链接地址
     * @return 数量
     */
    Integer findListCount(Map<String, Object> parms);

    /**
     * 删除该订单下剩余的轮播图
     *
     * @param parms carouselIds：id集合，orderId:订单id
     * @return 是否成功
     */
    int deleteCarouselIds(Map<String, Object> parms);

    /**
     * 查询指定id集合的轮播图
     *
     * @param carouselIds id集合
     * @return list
     */
    List<Map<String, Object>> findCarouselList(@Param("carouselIds") String carouselIds);


    /**
     * 查询该订单下的轮播图
     *
     * @param orderId 订单id
     * @return map
     */
    List<Map<String, Object>> findByOrderId(Integer orderId);
}