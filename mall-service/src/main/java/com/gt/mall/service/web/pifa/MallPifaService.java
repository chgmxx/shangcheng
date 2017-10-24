package com.gt.mall.service.web.pifa;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.param.phone.order.PhoneToOrderPifatSpecificaDTO;
import com.gt.mall.result.phone.order.PhoneToOrderPfSpecResult;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.utils.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品批发表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallPifaService extends BaseService< MallPifa > {

    /**
     * 添加批发商
     *
     * @param pifaApply
     *
     * @return
     */
    public int addWholesaler( MallPifaApply pifaApply );

    /**
     * 根据memberId和busUserId 查询批发商
     */
    public MallPifaApply selectByPifaApply( MallPifaApply pifaApply );

    /**
     * 批发商设置
     */
    public int updateSetWholesaler( Map< String,Object > map );

    /**
     * 查询批发商列表
     */
    public PageUtil wholesalerList( Map< String,Object > params );

    /**
     * 修改审核状态
     */
    public int updateStatus( Map< String,Object > params );

    /**
     * 批发商品列表
     */
    PageUtil pifaProductList( Map< String,Object > params, List< Map< String,Object > > shoplist );

    /**
     * 通过批发id查询批发信息
     */
    public Map< String,Object > selectPifaById( Integer id );

    /**
     * 编辑批发
     */
    public int editPifa( Map< String,Object > pifaMap, int userId );

    /**
     * 删除批发
     */
    public boolean deletePifa( MallPifa pifa );

    /**
     * 根据店铺id查询拍卖信息
     */
    public List< Map< String,Object > > selectgbPifaByShopId( Map< String,Object > maps );

    /**
     * 获取店铺下所有的批发
     */
    public PageUtil getPifaAll( Member member, Map< String,Object > maps );

    /**
     * 根据商品id查询批发信息和批发价格
     */
    public MallPifa getPifaByProId( Integer proId, Integer shopId, int activityId );

    /**
     * 根据商品id查询批发信息和批发价格
     */
    public MallPifa selectBuyByProductId( Integer proId, Integer shopId, int activityId );

    /**
     * 修改批发申请
     */
    public int updateWholesaleApplay( MallPifaApply applay );

    /**
     * 根据商品id查询批发价
     *
     * @param isPifa    是否开启批发 true 开启  false不开启
     * @param productId 商品id
     *
     * @return 批发价
     */
    public double getPifaPriceByProIds( boolean isPifa, int productId );

    /**
     * 搜索店铺下所有的批发商品
     */
    PageUtil searchPifaAll( PhoneSearchProductDTO searchProductDTO, Member member );

    /**
     * 获取商品的批发信息
     *
     * @param proId  商品id
     * @param shopId 店铺id
     * @param result 返回商品详细页面的结果
     * @param member 会员
     *
     * @return 批发信息
     */
    PhoneProductDetailResult getPifaProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result, Member member, MallPaySet mallPaySet );

    /**
     * 判断批发商品是否能购买
     * 1 判断批发商品是否正在进行
     * 2 判断购买的规格是否允许参团
     * 3 判断限购
     *
     * @param pifaId       批发id
     * @param invId        库存id
     * @param productNum   商品数量
     * @param memberId     粉丝id
     * @param memberBuyNum 粉丝已购买商品数量
     */
    boolean pifaProductCanBuy( int pifaId, int invId, int productNum, int memberId, int memberBuyNum, MallPaySet mallPaySet );

    /**
     * 获取批发设置
     *
     * @param busId 商家id
     *
     * @return 批发设置
     */
    Map< String,Object > getPifaSet( int busId );

    /**
     * 提交订单页面获取批发价
     *
     * @param proId            商品id
     * @param shopId           店铺id
     * @param specificaIdsList 规格集合
     *
     * @return
     */
    public List< PhoneToOrderPfSpecResult > getPifaPrice( int proId, int shopId, int activityId, List< PhoneToOrderPifatSpecificaDTO > specificaIdsList );
}
