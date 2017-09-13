package com.gt.mall.service.web.seckill;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.seckill.MallSeckill;
import com.gt.mall.utils.PageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSeckillService extends BaseService< MallSeckill > {

    /**
     * 通过店铺id来查询秒杀
     */
    PageUtil selectSeckillByShopId( Map< String,Object > param );

    /**
     * 通过秒杀id查询秒杀信息
     */
    Map< String,Object > selectSeckillById( Integer id );

    /**
     * 编辑秒杀
     */
    int editSeckill( Map< String,Object > params, BusUser busUser, HttpServletRequest request );

    /**
     * 删除秒杀
     */
    boolean deleteSeckill( MallSeckill Seckill );

    /**
     * 根据店铺id查询商品，并分析商品是否加入秒杀
     */
    PageUtil selectProByGroup( Map< String,Object > params );

    /**
     * 查询所有的秒杀
     */
    List< Map< String,Object > > getSeckillAll( Member member, Map< String,Object > maps );

    /**
     * 根据商品id查询秒杀信息和秒杀价格
     */
    MallSeckill getSeckillByProId( Integer proId, Integer shopId );

    /**
     * 查询用户参加秒杀的数量
     */
    int selectCountByBuyId( Map< String,Object > params );

    /**
     * 判断秒杀的库存是否能够秒杀
     */
    Map< String,Object > isInvNum( MallOrder mallOrder, MallOrderDetail mallOrderDetail );

    /**
     * 减商品的库存（在redis中）
     *
     * @param mallOrderDetail 订单详情
     * @param memberId        下单用户id
     * @param orderId         订单id
     */
    void invNum( MallOrder mallOrder, MallOrderDetail mallOrderDetail, String memberId, String orderId );

    /**
     * 把要修改的库存丢到redis里
     */
    void addInvNumRedis( MallOrder order, List< MallOrderDetail > detailList );

    List< Map< String,Object > > selectgbSeckillByShopId( Map< String,Object > maps );

    /**
     * 更新秒杀
     */
    void loadSeckill();

    /**
     * 通过秒杀id查询秒杀信息
     */
    MallSeckill selectSeckillBySeckillId( int id );
}
