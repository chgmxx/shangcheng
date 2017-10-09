package com.gt.mall.dao.store;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.store.MallStoreCertification;

/**
 * <p>
  * 店铺认证表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-09-19
 */
public interface MallStoreCertificationDAO extends BaseMapper<MallStoreCertification > {


    MallStoreCertification selectByStoreId( Integer storeId );
}