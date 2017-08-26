package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusFlow;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.wx.shop.WsWxShopInfoExtend;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.util.PageUtil;

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
     */
    PageUtil selectByUserId( Map< String,Object > params, List< Map< String,Object > > shoplist );

    /**
     * 批量修改商品信息
     */
    boolean batchUpdateProduct( Map< String,Object > params, String[] ids );

    /**
     * 根据商品id来查询商品基本信息
     */
    Map< String,Object > selectProductById( Integer id, BusUser user, long isJxc ) throws Exception;

    /**
     * 添加商品信息，详细，图片，规格，库存
     */
    Map< String,Object > addProduct( Map< String,Object > params, BusUser user, HttpServletRequest request ) throws Exception;

    /**
     * 修改商品信息，详情，图片，规格，库存
     */
    Map< String,Object > updateProduct( Map< String,Object > params, BusUser user, HttpServletRequest request ) throws Exception;

    /**
     * 根据商品Id查询商品的基本信息
     */
    MallProduct selectByPrimaryKey( Integer proId );

    /**
     * 根据商家id查询会员卡
     */
    List< Map > selectMemberType( int userId );

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
     * @param params type 类型 1 增加商品库存  2 减商品库存 ，product_id  商品id ，inventory_id  库存id  ,pro_num  购买商品的数量
     */
    Map< String,Object > updateProductStock( Map< String,Object > params ) throws Exception;

    /**
     * 查询商品的规格信息
     *
     * @param params productId 商品id
     */
    Map< String,Object > selectProductSpec( Map< String,Object > params ) throws Exception;

    /**
     * 判断用户是否在积分商城
     */
    int getJifenByRedis( Member member, HttpServletRequest request, int isJifen, int userid );

    /**
     * 把积分商城的标示存入redis
     */
    void setJifenByRedis( Member member, HttpServletRequest request, int isJifen, int userid );

    /**
     * 把积分的标示在redis里清空
     */
    void clearJifenByRedis( Member member, HttpServletRequest request, int userid );

    /**
     * 通过商品的规格id来获取商品的库存id
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
     */
    Map< String,Object > calculateInventory( int proId, Object proSpecificas, int proNum, int memberId );

    /**
     * 通过商品id来获取商品的规格值和图片
     */
    Map< String,Object > getSpecNameBySPecId( String specId, int productId );

    /**
     * 判断购物车的商品限购
     */
    Map< String,Object > isshoppingCart( Map< String,Object > map, int productNum, List< WsWxShopInfoExtend > wxShopList );

    int setIsShopBySession( int toshop, int shopid, int userid, HttpServletRequest request );

    int getIsShopBySession( int shopid, int userid, HttpServletRequest request );

    void clearIsShopSession( int shopid, int userid, HttpServletRequest request );

    /**
     * 修改商品信息 ，规格，库存
     */
    Map< String,Object > saveOrUpdateProductByErp( Map< String,Object > params, HttpServletRequest request ) throws Exception;

    /**
     * 通过门店同步商品信息
     */
    Map< String,Object > syncErpProductByWxShop( Map< String,Object > params, HttpServletRequest request ) throws Exception;

    /**
     * 同步erp的商品
     */
    boolean saveProductByErp( MallProduct product, BusUser user, int userPId );

    /**
     * 根据商品id查询进销存规格库存
     *
     * @param erpProId erp商品id
     * @param shopId   店铺id
     */
    List< Map< String,Object > > getErpInvByProId( int erpProId, int shopId ) throws Exception;

    /**
     * 从list中获取库存
     */
    int getInvNumsBySpecs( List< Map< String,Object > > specList, String invIds );

    /**
     * 把未同步的erp商品进行同步
     */
    void syncErpPro( int userId, HttpServletRequest request );

    /**
     * 根据流量id查询商品数量
     */
    List< BusFlow > selectCountByFlowIds( int userId );

    /**
     * 根据erp的商品id查询
     */
    List< MallProduct > selectByERPId( Map< String,Object > params );

    /**
     * 同步所有实体商品 至erp
     *
     * @param user 用户信息
     */
    void syncAllProduct( BusUser user, HttpServletRequest request );
}
