package com.gt.mall.web.service.page;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.page.MallPage;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.entity.product.MallProductParam;
import com.gt.mall.entity.product.MallShopCart;
import com.gt.mall.util.PageUtil;
import net.sf.json.JSONObject;

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
    public PageUtil findByPage( Map< String,Object > params, BusUser user );

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
    public PageUtil selectListBranch( Integer stoId, Map< String,Object > params );

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
    public Map< String,Object > shopmessage( Integer shopid );

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
    public void isAddShopCart( HttpServletRequest request, Member member ) throws Exception;

    /**
     * 根据会员id获取公众号，根据公众号获取所有店面，根据店面获取所有购物车订单
     */
    public void shoppingcare( Member member, double discount, int type, HttpServletRequest request, int userid );

    /**
     * 把cookie中的商品，合并到购物车
     */
    public void mergeShoppCart( Member member, HttpServletRequest request, HttpServletResponse response );

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
     * 返回店面信息下面的所有商品
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
     *
     * @param shopid 商铺id
     * @param params proName 模糊查询 , groupId 分组 ,desc    0代表正序，1代表倒序
     * @param type   属性，1代表默认id（也是根据时间），2：代表销量排序，3，代表价格排序
     * @param rType  1代表积分商城，2 粉币比商城
     * @param member 搜索用户
     * @param isPifa 是否显示批发价
     *
     * @return 商品信息
     */
    public PageUtil productAllList( Integer shopid, String type, int rType, Member member, double discount, Map< String,Object > params, boolean isPifa );

    /**
     * 获取商品图片
     */
    public List<Map<String, Object>> getProductImages(List<Map<String, Object>> xlist, List<Integer> proIds,String[] specImgIds);

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
     * 根据店铺id获取主页面编辑器
     */
    public List< Map< String,Object > > pagecountid( Integer shopid );

    /**
     * 根据用户id 查询所有店铺id，和名称
     */
    public List< Map< String,Object > > productShopList( Integer userid );

    /**
     * 根据用户id查询 主页id
     *
     * @param userid 商家id
     * @param wxShopId 门店id
     *
     * @return 主页id
     */
    public Map< String,Object > getPage( Integer userid, int wxShopId );

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

    /**
     * 根据用户查询商家信息
     */
    BusUser selUserByMember( Member member );

    public List< Map< String,Object > > typePage( int stoId, BusUser user );

    /**
     * 获取客服信息
     */
    void getCustomer( HttpServletRequest request, int userid );

    /**
     * 根据商品id查询商品信息
     */
    JSONObject getProduct( JSONObject obj ) throws Exception;

    /**
     * 查询用户的搜索记录和商家的推荐标签
     */
    void getSearchLabel( HttpServletRequest request, int shopId, Member member, Map< String,Object > params );

    /**
     * 查询商品的规格参数
     */
    List< MallProductParam > getProductParam( int proId );

    /**
     * 查询商品的评价
     */
    Map< String,Object > getProductComment( Map< String,Object > params, Member member );

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
    public boolean wxShopIsDelete( int shopId ) throws Exception;

    /**
     * 添加关键词搜索记录
     */
    public void saveOrUpdateKeyWord( Map< String,Object > params, int shopid, int userId );

    /**
     * 查询商品参加的活动
     */
    public String productActivity( HttpServletRequest request, Member member, int id, int shopid, int userid );

    /**
     * 获取所有的预售商品
     */
    public List< Map< String,Object > > productPresale( Integer stoId, Map< String,Object > params );

    /**
     * 获取预售商品
     */
    Map< String,Object > getProductPresale( Map< String,Object > map, Member member );

    /**
     * 首页查询商品信息
     */
    Map< String,Object > getProductHome( Map< String,Object > map3, Map< String,Object > map2, Member member, String http, double discount, MallPaySet set, int state );

    /**
     * 通过卡券包id查询卡券信息
     */
    Map< String,Object > getCardReceive( int receiveId );

    /**
     * 用户购买商品的数量
     */
    int memberBuyProNum( int memberId, Map< String,Object > params, int type );

    /**
     * 组商品参数
     */
    public String setProductParam( String url, Map< String,Object > params );

    /**
     * 查询商家的公众号
     */
    public Map< String,Object > getPublicByUserMap( Map< String,Object > userMap );

    /**
     * 判断用户是否已经登陆
     */
    public boolean isLogin( Member member, int userid, HttpServletRequest request );

    /**
     * 保存跳转地址到redis
     */
    public Map< String,Object > saveRedisByUrl( Member member, int userid, HttpServletRequest request );

    /**
     * 得到地区名称
     */
    public Map< String,Object > queryAreaById( Integer id );

}
