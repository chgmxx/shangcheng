package com.gt.mall.service.web.seller;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.seller.MallSellerJoinProduct;
import com.gt.mall.entity.seller.MallSellerMallset;
import com.gt.mall.entity.seller.MallSellerProduct;
import com.gt.mall.utils.PageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员商城设置 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerMallsetService extends BaseService< MallSellerMallset > {

    /**
     * 通过销售员id查询商城设置
     *
     * @param memberId 销售员id
     *
     * @return 商城设置
     */
    public MallSellerMallset selectByMemberId( int memberId );

    /**
     * 查询销售员自选的商品
     *
     * @param params 参数
     *
     * @return 自选商品
     */
    List< MallSellerProduct > selectProBySaleMemberId( Map< String,Object > params );

    /**
     * 保存或修改商城设置
     *
     * @param member 用户
     * @param params 商城设置的内容
     *
     * @return flag :{ true 修改成功  false 修改失败}
     */
    Map< String,Object > saveOrUpdateSeller( Member member, Map< String,Object > params );

    /**
     * 查询销售员首页的所有商品信息
     *
     * @param member 粉丝对象
     * @param params 参数
     *
     * @return 商品信息
     */
    List< Map< String,Object > > selectProductByMallIndex( Member member, Map< String,Object > params, MallSellerMallset mallSet );

    /**
     * 根据商品id查询销售商品
     *
     * @param proId      商品id
     * @param mallSeller 销售员对象
     *
     * @return 销售商品信息
     */
    Map< String,Object > selectSellerByProId( int proId, MallSellerMallset mallSeller );

    /**
     * 查询销售员的商品
     *
     * @param params 参数
     *
     * @return 商品信息
     */
    List< Map< String,Object > > selectProductBySaleMember( Map< String,Object > params );

    /**
     * 查询销售员选择的商品
     *
     * @param params 参数
     *
     * @return 商品信息
     */
    List< Map< String,Object > > selectProductByBusUserId( Map< String,Object > params );

    /**
     * 删除已添加商品
     *
     * @param params 参数
     *
     * @return {flag ：（true 成功，false 失败） msg：失败警告语}
     */
    Map< String,Object > deleteSellerProduct( Map< String,Object > params );

    /**
     * 查询销售员自选的商品
     */
    PageUtil selectProductBySaleMember( MallSellerMallset mallSet, Map< String,Object > params, String type, int rType, double discount, boolean isPifa );

    /**
     * 查询销售佣金
     *
     * @param proId 商品id
     *
     * @return 销售佣金
     */
    MallSellerJoinProduct getProductCommission( int proId );

    /**
     * 查询销售的商品
     *
     * @param proId        商品id
     * @param saleMemberId 销售员id
     *
     * @return 销售的商品
     */
    MallSellerProduct selectByProIdSale( int proId, int saleMemberId );

    /**
     * 查询商品的销售信息
     */
    void selectSellerProduct( HttpServletRequest request, int proId, int saleMemberId, Map< String,Object > params, Member member );

    /**
     * 判断销售员是否拥有该商品
     *
     * @param proId        商品id
     * @param saleMemberId 销售员id
     *
     * @return true 拥有 false 不拥有
     */
    boolean isSellerProduct( int proId, int saleMemberId );

    /**
     * 获取销售商品价
     *
     * @param map 参数
     *
     * @return 商品价
     */
    Map< String,Object > getSellerProductPrice( Map< String,Object > map );

}
