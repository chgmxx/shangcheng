package com.gt.mall.dao.pifa;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.pifa.MallPifaApply;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 批发申请表 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPifaApplyDAO extends BaseMapper< MallPifaApply > {

    /**
     * 根据memberId和BusUserId 查询批发商
     * @param pifaApply
     * @return
     */
    MallPifaApply selectByPifaApply(MallPifaApply pifaApply);

    /**
     * 批发商设置
     * @param map
     * @return
     */
    int updateSetWholesaler(Map<String,Object> map);

    /**
     * 批发商列表条数
     * @param params
     * @return
     */
    int count(Map<String,Object> params);

    /**
     * 查询批发商列表
     * @param params
     * @return
     */
    List wholesalerList(Map<String,Object> params);

    /**
     * 修改审核状态
     * @param params
     * @return
     */
    int updateStatus(Map<String,Object> params);
}