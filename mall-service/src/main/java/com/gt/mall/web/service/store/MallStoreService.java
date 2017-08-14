package com.gt.mall.web.service.store;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.entity.store.MallStore;
import com.gt.mall.util.PageUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城店铺 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallStoreService extends BaseService< MallStore > {

    /**
     * 店铺管理分页
     *
     * @param params userId  商家id ，stoName 店铺名称
     *
     * @return 店铺信息
     */
    public PageUtil findByPage( Map< String,Object > params );

    /**
     * 获取店铺集合
     *
     * @param userId 商家id
     *
     * @return 店铺集合
     */
    public List< Map< String,Object > > findAllStore( Integer userId );

    /**
     * 查询商家是否是管理员
     *
     * @param userId 商家id
     *
     * @return 查询商家是否是管理员
     */
    public boolean isAdminUser( int userId );

    /**
     * 根据用户id获取分店数量
     *
     * @param userId 商家id
     *
     * @return 分店数量
     */
    public int countBranch( Integer userId );

    /**
     * 创建店铺多少
     *
     * @param userId 商家id
     *
     * @return 店铺数量
     */
    public int countStroe( Integer userId );

    /**
     * 查询所有的门店
     *
     * @param userId 商家id
     * @param shopId 店铺id
     *
     * @return 门店信息
     */
    public List< Map< String,Object > > findShop( int userId, int shopId );

    /**
     * 通过商家id查询所有的店铺
     *
     * @param userId 商家id
     *
     * @return map
     */
    public List< Map< String,Object > > findByUserId( Integer userId );

    /**
     * 从session获取店铺id
     *
     * @param session session
     * @param shopId  店铺id
     *
     * @return 店铺id
     */
    int getShopBySession( HttpSession session, int shopId );

    /**
     * 创建仓库  对接进销存
     *
     * @param shopId 店铺id
     * @param user   用户信息
     * @param uType  用户类型
     *
     * @return 店铺信息
     */
    public int createCangku( int shopId, BusUser user, int uType );

    /**
     * 通过门店id查询店铺信息
     *
     * @param wxShopId 门店id
     * @param isNotId  除id以外
     *
     * @return 店铺信息
     */
    List< MallStore > findByShopId( int wxShopId, int isNotId );

    /**
     * 根据商家id查询店铺信息
     *
     * @param userId 商家id
     *
     * @return 店铺信息
     */
    List< Map< String,Object > > selectStoreByUserId( int userId );

    /**
     * 根据店铺id查询店铺信息
     *
     * @param id 店铺id
     *
     * @return 店铺信息
     */
    Map< String,Object > findShopByStoreId( Integer id ) throws Exception;

    /**
     * 保存或修改店铺
     */
    Map< String,Object > saveOrUpdate( MallStore sto, BusUser user ) throws Exception;

    /**
     * 逻辑删除店铺信息
     */
    Map< String,Object > deleteShop( String[] ids ) throws Exception;

    /**
     * 获取登录人拥有的店铺集合
     */
    public List< Map< String,Object > > findAllStoByUser( BusUser user );

    /**
     * 创建所有erp仓库
     *
     * @param user 用户信息
     *
     * @return 是否成功
     */
    boolean createCangkuAll( BusUser user );

}
