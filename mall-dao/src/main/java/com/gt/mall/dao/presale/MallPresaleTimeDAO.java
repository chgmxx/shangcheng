package com.gt.mall.dao.presale;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.presale.MallPresaleTime;

import java.util.List;

/**
 * <p>
 * 商品预售时间表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleTimeDAO extends BaseMapper< MallPresaleTime > {

    /**
     * 通过预售id查询预售时间
     *
     * @param preId
     *
     * @return
     */
    List< MallPresaleTime > selectByPreId( int preId );

}
