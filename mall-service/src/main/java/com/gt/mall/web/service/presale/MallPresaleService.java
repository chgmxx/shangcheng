package com.gt.mall.web.service.presale;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.presale.MallPresale;
import com.gt.mall.entity.presale.MallPresaleGive;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品预售表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPresaleService extends BaseService< MallPresale > {

    /**
     * 通过店铺id来查询预售
     *
     */
    PageUtil selectPresaleByShopId( Map< String,Object > param );

    /**
     * 通过预售id查询预售信息
     */
    Map< String,Object > selectPresaleById( Integer id );

    /**
     * 编辑预售
     */
    int editPresale( Map< String,Object > params, int userId );

    /**
     * 删除预售
     */
    boolean deletePresale( MallPresale presale );

    /**
     * 查询所有的预售
     */
    List< Map< String,Object > > getPresaleAll( Member member, Map< String,Object > maps );

    /**
     * 根据商品id查询预售信息和预售价格
     */
    MallPresale getPresaleByProId( Integer proId, Integer shopId, Integer presaleId );

    /**
     * 查询用户参加预售的数量
     */
    int selectCountByBuyId( Map< String,Object > params );

    /**
     * 根据店铺id查询预售信息
     */
    List< Map< String,Object > > selectgbPresaleByShopId( Map< String,Object > maps );

    /**
     * 判断是否超过了限购
     */
    Map< String,Object > isMaxNum( Map< String,Object > map, String memberId );

    /**
     * 获取送礼设置的信息
     */
    List< MallPresaleGive > selectGiveByUserId( BusUser user );

    /**
     * 编辑预售设置
     */
    int editPresaleSet( Map< String,Object > params, int userId );

    /**
     * 发货实体物品
     */
    void deliveryRank( MallOrder order );

    /**
     * 初始化redis
     */
    void loadPresaleByJedis( MallPresale pre );

    /**
     * 判断预售商品的库存
     */
    Map< String,Object > isInvNum( Map< String,Object > params );

    /**
     * 预售商品开售提醒
     */
    void presaleStartRemain();

    /**
     * 扫描已经结束的预售
     */
    void presaleProEnd();

    /**
     * 扣库存
     */
    void diffInvNum( MallOrder order );
}
