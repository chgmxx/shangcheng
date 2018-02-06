package com.gt.mall.dao.purchase;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.purchase.PurchaseCompanyMode;
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
public interface PurchaseCompanyModeDAO extends BaseMapper< PurchaseCompanyMode > {

    /**
     * 分页查询
     *
     * @param parms companyTel：公司联系电话，companyName：公司名称，companyAddress：公司地址
     *              pageFirst:页数,pageLast：记录数
     *
     * @return list
     */
    List< Map< String,Object > > findList( Map< String,Object > parms );

    /**
     * 分页查询的条数
     *
     * @param parms companyTel：公司联系电话，companyName：公司名称，companyAddress：公司地址
     *
     * @return 数量
     */
    Integer findListCount( Map< String,Object > parms );

    /**
     * 批量删除
     *
     * @param companyModeIds id集合
     *
     * @return 是否成功
     */
    int deleteCompanyModes( @Param( "companyModeIds" ) String companyModeIds );

    /**
     * 查询所有的公司模板
     *
     * @param busId 商家id
     *
     * @return list
     */
    List< Map< String,Object > > findAllList( Integer busId );
}
