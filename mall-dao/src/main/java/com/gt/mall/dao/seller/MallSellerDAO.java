package com.gt.mall.dao.seller;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.seller.MallSeller;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerDAO extends BaseMapper< MallSeller > {

    /**
     * 修改销售员状态
     */
    boolean updateStatusByUserId( Integer status, Integer user_id );

    /**
     * 查询客户的数量
     */
    int selectCountMyClient( Map< String,Object > params );

    /**
     * 通过销售员的id查询销售元的信息
     *
     * @param saleId 销售员的id
     */
    Map< String,Object > selectSellerBySaleId( int saleId );

    /**
     * 查询超级销售员信息
     */
    MallSeller selectMallSeller( MallSeller seller );

    /**
     * 查询销售员的信息
     *
     * @param memberId 用户id
     */
    MallSeller selectSellerByMemberId( int memberId );

    /**
     * 通过商家id查询销售员的信息
     */
    List< Map< String,Object > > selectSellerByBusUserId( Map< String,Object > params );

    /**
     * 修改提现金额
     */
    int updateByWithdrawSelective( Map< String,Object > params );

    /**
     * 统计销售员信息和推荐审核信息
     */
    int selectCountByBusUserId( Map< String,Object > params );

    /**
     * 分页查询推荐审核信息
     */
    List< Map< String,Object > > selectPageCheckByBusUserId( Map< String,Object > params );

    /**
     * 分页查询销售员信息
     */
    List< Map< String,Object > > selectPageSellerByBusUserId( Map< String,Object > params );

    /**
     * 批量审核销售员，批量暂停销售员
     */
    int batchUpdateSeller( Map< String,Object > params );

    /**
     * 修改销售员的额佣金，销售额以及销售积分
     */
    int updateBySellerIncome( Map< String,Object > params );

    /**
     * 修改会员积分
     */
    int updateMember( Map< String,Object > params );

    /**
     * 根据生成二维码时的key查询销售员信息
     */
    MallSeller selectSellerBySecenId( String secenKey );

    /**
     * 查询合并会员数据
     */
    MallSeller selectDataByOldMemberId( Map< String,Object > params );

    /**
     * 修改已合并的数据
     */
    int updateDataByOldMemberId( Map< String,Object > params );

}