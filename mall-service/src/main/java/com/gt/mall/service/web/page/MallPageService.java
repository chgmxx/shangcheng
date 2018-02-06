package com.gt.mall.service.web.page;

import com.gt.mall.base.BaseService;
import com.gt.api.bean.session.BusUser;
import com.gt.api.bean.session.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.entity.product.MallProductParam;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.utils.PageUtil;
import com.gt.util.entity.result.shop.WsWxShopInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 页面表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPageService extends BaseService< MallPage > {

    /**
     * 分页查询
     *
     * @param params 搜索删除
     * @param user   商家对象
     *
     * @return 页面信息
     */
    public PageUtil findByPage( Map< String,Object > params, BusUser user, HttpServletRequest request );

    /**
     * 保存或修改信息
     *
     * @param page 页面对象
     * @param user 商家对象
     *
     * @return result true 成功，false 失败
     * @throws Exception 异常
     */
    public Map< String,Object > saveOrUpdate( MallPage page, BusUser user ) throws Exception;

    /**
     * 删除页面信息
     *
     * @param ids 页面id数组
     *
     * @return result true 成功，false 失败
     * @throws Exception 异常
     */
    public Map< String,Object > delete( String ids[] ) throws Exception;

    /**
     * 删除页面信息
     *
     * @param id     页面id
     * @param shopid 店铺id
     *
     * @return result true 成功，false 失败
     * @throws Exception 异常
     */
    public Map< String,Object > setMain( Integer id, Integer shopid ) throws Exception;

    /**
     * 根据id 查询树页面信息
     *
     * @param id     页面id
     * @param userid 商家id
     *
     * @return 页面信息
     */
    public Map< String,Object > select( Integer id, Integer userid );

    /**
     * 根据商品id获取商品部分信息
     *
     * @param id 商品id
     *
     * @return 商品信息
     */
    public Map< String,Object > querySelct( Integer id );

    /**
     * 根据id 查询树页面信息
     *
     * @param id 页面id
     *
     * @return 页面信息
     */
    public MallPage select( Integer id );

    /**
     * 根据店铺获取该店铺下面的所有分类页
     *
     * @param stoId  店铺id
     * @param params 参数
     *
     * @return 分页信息
     */
    public PageUtil selectListBranch( Integer stoId, Map< String,Object > params, BusUser user );

    /**
     * 根据商品id获取商品信息
     *
     * @param id 商品id
     *
     * @return 商品信息
     */
    public Map< String,Object > selectBranch( Integer id );

    /**
     * 手机段获取商品上面轮播图图片
     */
    public List< Map< String,Object > > imageProductList( Integer id, int asscType );

    /**
     * 根据id获取商品信息
     *
     * @param id 商品id
     *
     * @return 商品信息
     */
    public Map< String,Object > productMessage( Integer id );

    /**
     * 根据店铺id 获取详情信息
     *
     * @param shopid 店铺id
     *
     * @return 详情信息
     */
    public Map< String,Object > shopmessage( Integer shopid, WsWxShopInfo wxShop );

    /**
     * 根据主页面id，获取商家公众号id
     *
     * @param pagesid 页面id
     *
     * @return 公众号信息
     */
    public Map< String,Object > wxpublicid( Integer pagesid );

    /**
     * 保存到服务车里
     */
    public int addshopping( MallShopCart obj, Member member, HttpServletRequest request, HttpServletResponse response );

    /**
     * 判断是否有商品需要加入购物车
     *
     * @param request request
     * @param member  粉丝对象
     */
    int getMemberShopCartNum( HttpServletRequest request, Member member, List< Integer > memberList ) throws Exception;

    /**
     * 判断批发购物车的规格
     */
    MallShopCart getProSpecNum( String proSpecStr, MallShopCart cart );

    /**
     * 把cookie中的商品，合并到购物车
     */
    void mergeShoppCart( Member member, HttpServletRequest request, HttpServletResponse response );

    /***
     * 未登陆时，查询cookie商品记录，用于判断新增/修改
     */
    List< Map< String,Object > > selectByShopCart( MallShopCart obj, String ids );

    /**
     * 删除购物车里面的信息
     */
    public void shoppingdelect( String delects, String updStr, int type );

    /**
     * 封装成Map 数据传给结算页面
     *
     * @param json       json
     * @param totalnum   总数
     * @param totalprice 总价
     * @param memberId   粉丝id
     *
     * @return 结算信息
     */
    public Map< String,Object > shoporder( String json, String totalnum, String totalprice, String memberId );

    /**
     * 根据店铺查找所有分类
     */
    public List< Map< String,Object > > storeList( Integer stoId, int type, int busUserId );

    /**
     * 返回店面信息下面的所有商品 (页面设计 商品弹出框专用)
     */
    public PageUtil product( Map< String,Object > params );

    /**
     * 根据商品获取产品默认规格
     */
    public Map< String,Object > productSpecifications( Integer id, String inv_id );

    /**
     * 根据商品id获取所有规格信息
     */
    public List< Map< String,Object > > guigePrice( Integer id );

    /**
     * 根据商品id获取商品详情
     */
    public MallProductDetail shopdetails( Integer id );

    /**
     * 根据商铺获取所有商品，在根据商品信息，进行排序
     */
    PageUtil productAllListNew( PhoneSearchProductDTO searchProductDTO, double discount, Member member );

    List< Map< String,Object > > getSearchProductParam( List< Map< String,Object > > list, double discount, PhoneSearchProductDTO searchProductDTO );

    /**
     * 获取商品图片
     */
    public List< Map< String,Object > > getProductImages( List< Map< String,Object > > xlist, List< Integer > proIds );

    /**
     * 重新组装对象
     */
    public Map< String,Object > productGetPrice( Map< String,Object > map1, double discount, boolean isPifa );

    /**
     * 根据店铺id查找到该店铺主页面
     */
    public List< Map< String,Object > > shoppage( Integer shopid );

    /**
     * 根据shopid查询bus_user信息
     */
    public Map< String,Object > selUser( Integer shopid );

    /**
     * 根据会员id 查询公众号id
     */
    public Map< String,Object > publicMapByUserId( Integer member_id );

    /**
     * 最新商品数量
     *
     * @param shopid 店铺id
     * @param time   时间起点
     *
     * @return 商品数量
     */
    public int counttime( Integer shopid, String time );

    /**
     * 根据ip获取省份id
     */
    public String getProvince( String ip );

    /**
     * 修改商品浏览量
     */
    int updateProViewPage( String proId, Map< String,Object > proMap );

    /**
     * 查询门店下是否有积分商城
     */
    List< Integer > shopIsJiFen( List< Integer > list );

    /**
     * 根据门店id查询是否有粉币商品
     */
    List< Integer > shopIsFenbi( List< Integer > list );

    public List< Map< String,Object > > typePage( int stoId, BusUser user );

    /**
     * 获取客服信息
     */
    void getCustomer( HttpServletRequest request, int userid );

    /**
     * 查询用户的搜索记录和商家的推荐标签
     */
    void getSearchLabel( HttpServletRequest request, int shopId, Member member, Map< String,Object > params );

    /**
     * 查询商品的规格参数
     */
    List< MallProductParam > getProductParam( int proId );

    /**
     * 根据商铺获取所有商品，在根据商品信息，进行排序
     *
     * @param member   粉丝对象
     * @param discount 会员折扣
     *
     * @return 商品信息
     */
    public List< Map< String,Object > > getProductCollectByMemberId( Member member, double discount );

    /**
     * 查询门店或店铺是否已经删除
     *
     * @param shopId 店铺id
     *
     * @return true 已删除   false 未删除
     */
    public boolean wxShopIsDelete( int shopId, WsWxShopInfo wsWxShopInfo ) throws Exception;

    /**
     * 添加关键词搜索记录
     */
    public void saveOrUpdateKeyWord( Map< String,Object > params, int shopid, int userId );

    /**
     * 获取所有的预售商品(页面设计时调用的，不可增加和减少返回值)
     */
    public PageUtil productPresale( Integer stoId, Map< String,Object > params );

    /**
     * 获取预售商品
     */
    Map< String,Object > getProductPresale( Map< String,Object > map, Member member );

    /**
     * 通过卡券包id查询卡券信息
     */
    Map< String,Object > getCardReceive( int receiveId );

    /**
     * 根据商品id获取商品部分信息
     */
    public List< Map< String,Object > > getProductListByIds( String ids, Member member, double discount, MallPaySet set, boolean isPifa );

    /**
     * 根据memberId查询pageId
     */
    List< Map< String,Object > > selectPageIdByUserId( Integer userId, List< Map< String,Object > > shopList );

    /**
     * 根据店铺id查询首页id
     *
     * @param shopId 店铺id
     *
     * @return 首页id
     */
    public int getPageIdByShopId( int shopId );

}

