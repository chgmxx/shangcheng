package com.gt.mall.web.service.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductService extends BaseService< MallProduct > {

    /**
     * 根据用户id来查询商品
     *
     * @Title: selectByUserId
     */
    PageUtil selectByUserId( Map< String,Object > params );

    /**
     * 批量修改商品信息
     *
     * @Title: batchUpdateProduct
     */
    boolean batchUpdateProduct( Map< String,Object > params, String[] ids );

    /**
     * 根据商品id来查询商品基本信息
     *
     * @Title: findProductById
     */
    Map< String,Object > selectProductById( Integer id, BusUser user, long isJxc ) throws Exception;

    /**
     * 添加商品信息，详细，图片，规格，库存
     *
     * @Title: addProduct
     */
    Map< String,Object > addProduct( Map< String,Object > params, BusUser user ) throws Exception;

    /**
     * 修改商品信息，详情，图片，规格，库存
     *
     * @Title: updateProduct
     */
    Map< String,Object > updateProduct( Map< String,Object > params, BusUser user ) throws Exception;

    /**
     * 根据商品Id查询商品的基本信息
     *
     * @param proId
     *
     * @return
     */
    MallProduct selectByPrimaryKey( Integer proId );

    /**
     * 根据商家id查询会员卡
     */
    List< Map< String,Object > > selectMemberType( int userId );

    /**
     * 同步门店下的商品信息
     */
    Map< String,Object > copyProductByShopId( Map< String,Object > params, BusUser user ) throws Exception;

    /**
     * 同步商品信息
     */
    Map< String,Object > copyProduct( Map< String,Object > params, BusUser user ) throws Exception;

    /**
     * 根据门店id查询商品列表
     */
    List< Map< String,Object > > selectProductByWxShop( Map< String,Object > params ) throws Exception;

    /**
     * 统计门店id里面的商品数量
     */
    int selectCountProductByWxShop( Map< String,Object > params ) throws Exception;

    /**
     * 根据商品id查询商品库存
     *
     * @param params productId  商品id  ，memberId   用户id   取自t_wx_bus_member表的id  如果需要折扣价就传至，否则就不传
     *
     * @return 商品库存
     */
    Map< String,Object > selectProStockByProId( Map< String,Object > params ) throws Exception;

    /**
     * 修改商品的库存
     *
     * @param params type 类型 1 增加商品库存  2 减商品库存
     * @param params product_id  商品id
     * @param params inventory_id  库存id
     * @param params pro_num  购买商品的数量
     *
     * @return
     * @throws Exception
     */
    Map< String,Object > updateProductStock( Map< String,Object > params ) throws Exception;

    /**
     * 查询商品的规格信息
     *
     * @param params productId 商品id
     *
     * @return
     * @throws Exception
     */
    Map< String,Object > selectProductSpec( Map< String,Object > params ) throws Exception;

    /**
     * 判断用户是否在积分商城
     *
     * @param member
     * @param request
     *
     * @return
     */
    int getJifenByRedis( Member member, HttpServletRequest request, int isJifen, int userid );

    /**
     * 把积分商城的标示存入redis
     *
     * @param member
     * @param request
     * @param isJifen
     */
    void setJifenByRedis( Member member, HttpServletRequest request, int isJifen, int userid );

    /**
     * 把积分的标示在redis里清空
     *
     * @param member
     * @param request
     */
    void clearJifenByRedis( Member member, HttpServletRequest request, int userid );

    /**
     * 通过商品的规格id来获取商品的库存id
     *
     * @return
     */
    Map< String,Object > getProInvIdBySpecId( String specId, int proId );

    /**
     * 查询会员折扣
     */
    double getMemberDiscount( String isMemberDiscount, Member member );

    /**
     * 计算库存是否足够
     *
     * @param proId         商品id
     * @param proSpecificas 商品规格
     * @param proNum        购买数量
     * @param memberId      购买人id
     *
     * @return
     */
    Map< String,Object > calculateInventory( int proId, Object proSpecificas, int proNum, int memberId );

    /**
     * 通过商品id来获取商品的规格值和图片
     *
     * @param specId
     * @param productId
     *
     * @return
     */
    Map< String,Object > getSpecNameBySPecId( String specId, int productId );

    /**
     * 判断购物车的商品限购
     *
     * @param map
     * @param productNum
     *
     * @return
     */
    Map< String,Object > isshoppingCart( Map< String,Object > map, int productNum );

    /**
     * 查询流量充值的商品个数
     *
     * @param flowIds
     *
     * @return
     */
    int selectCountByFlowIds( int flowIds );

    int setIsShopBySession( int toshop, int shopid, int userid, HttpServletRequest request );

    int getIsShopBySession( int shopid, int userid, HttpServletRequest request );

    void clearIsShopSession( int shopid, int userid, HttpServletRequest request );

    /**
     * 修改商品信息 ，规格，库存
     *
     * @Title: updateProduct
     */
    Map< String,Object > saveOrUpdateProductByErp( Map< String,Object > params ) throws Exception;

    /**
     * 通过门店同步商品信息
     *
     * @param params
     *
     * @return
     * @throws Exception
     */
    Map< String,Object > syncErpProductByWxShop( Map< String,Object > params ) throws Exception;

    /**
     * 同步erp的商品
     *
     * @param product
     * @param user
     *
     * @return
     */
    boolean saveProductByErp( MallProduct product, BusUser user );

    /**
     * 根据商品id查询进销存规格库存
     *
     * @param erpProId erp商品id
     * @param shopId   店铺id
     *
     * @return
     */
    List< Map< String,Object > > getErpInvByProId( int erpProId, int shopId ) throws Exception;

    /**
     * 从list中获取库存
     *
     * @param specList
     * @param invIds
     *
     * @return
     */
    int getInvNumsBySpecs( List< Map< String,Object > > specList, String invIds );

    /**
     * 把未同步的erp商品进行同步
     *
     * @param userId
     */
    void syncErpPro( int userId );

    /**
     * 根据流量id查询商品数量
     *
     * @param flowId
     *
     * @return
     */
    int selectCountByFlowIds( @Param( "flowId" ) Integer flowId );

    /**
     * 根据erp的商品id查询
     *
     * @param params
     *
     * @return
     */
    List< MallProduct > selectByERPId( Map< String,Object > params );

    /**
     * 同步所有实体商品 至erp
     *
     * @param user 用户信息
     */
    void syncAllProduct( BusUser user );
}
