package com.gt.mall.web.service.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallSearchKeyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城搜索表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSearchKeywordService extends BaseService< MallSearchKeyword > {

    /**
     * 根据店铺id和关键词插损搜索表
     *
     * @param shopId  店铺id
     * @param keyword 关键词
     * @param userId  商家id
     *
     * @return
     */
    MallSearchKeyword selectBykeyword( @Param( "shopId" ) int shopId, @Param( "keyword" ) String keyword, @Param( "userId" ) int userId );

    /**
     * 根据粉丝id和店铺id查询搜索内容
     * @param map shopId 店铺id
     * @param map userId 商家id
     * @return
     */
    List< MallSearchKeyword > selectByUser( Map< String,Object > map );

    /**
     * 清空搜索历史记录
     *
     * @param params userId 粉丝id
     *
     * @return
     */
    int clearSearchKeyWord( Map< String,Object > params );
}
