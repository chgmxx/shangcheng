package com.gt.mall.service.web.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品库存 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductInventoryService extends BaseService< MallProductInventory > {

    /**
     * 批量保存或修改商品库存
     */
    void saveOrUpdateBatch( Map< String,Object > specMap, Object obj, int proId, Map< String,Object > invenMap );

    /**
     * 批量保存或修改商品库存
     */
    void newSaveOrUpdateBatch( Map< String,Object > specMap, Object obj, int proId );

    /**
     * 通过商品id查询商品库存
     */
    List< MallProductInventory > getInventByProductId( int proId );
    /**
     * 通过商品id查询商品库存
     */
    List< MallProductInventory > getInventByProductIds( List<Integer> productId );

    /**
     * 查询默认显示的库存
     */
    MallProductInventory selectByIsDefault( Integer productId );

    /**
     * 查询商品大于0的默认库存（商品详细页面展示）
     *
     * @param productId 商品id
     * @param invenId   库存id
     *
     * @return 库存
     */
    MallProductInventory selectDefaultInvenNotNullStock( int productId, int invenId, PhoneProductDetailResult result );

    /**
     * 查询默认显示的库存
     */
    List< MallProductInventory > selectByIdListDefault( List< Integer > productList );

    /**
     * 查询库存
     */
    List< MallProductInventory > selectInvenByProductId( Integer productId );

    MallProductInventory selectInvNumByProId( Map< String,Object > params );

    /**
     * 同步规格库存
     *
     * @param invenList 库存集合
     * @param specMap   规格
     * @param proId     商品id
     */
    void copyProductInven( List< MallProductInventory > invenList, Map< String,Object > specMap, int proId ,int oldProId) throws Exception;

    /**
     * 批量保存或修改erp商品库存
     *
     * @param map   库存
     * @param proId 商品id
     */
    Map< String,Object > saleOrUpdateBatchErp( Map map, int proId, int userId );

    /**
     * 根据商品id和商品规格id查询商品库存信息
     *
     * @param productId    商品id
     * @param specificaIds 规格id
     *
     * @return 商品库存
     */
    MallProductInventory selectBySpecIds( int productId, String specificaIds );

    /**
     * 根据商品id和erp的库存id查询商城的库存id
     *
     * @param productId 商品id
     * @param erpInvId  erp库存id
     *
     * @return 商品库存
     */
    MallProductInventory selectByErpInvId( int productId, int erpInvId );

    /**
     * 根据商品ID和规格id修改库存信息
     *
     * @param inventory 库存对象
     * @param proNum    数量
     * @param type      1 加库存减销量（退款）   2减库存价销量（购买）
     *
     * @return
     */
    int updateProductInventory( MallProductInventory inventory, Integer proNum, Integer type );

    /**
     * 根据商品获取产品默认规格
     *
     * @param productId 商品id
     * @param inv_id    库存id
     *
     * @return 规格库存
     */
    Map< String,Object > productSpecifications( Integer productId, String inv_id );

    /**
     * 根据商品id获取所有规格信息
     *
     * @param productId 商品id
     *
     * @return 规格库存
     */
    List< Map< String,Object > > guigePrice( Integer productId );

    /**
     * 新增或修改商品库存
     *
     * @param inven 库存对象
     *
     * @return > 0 成功 ； <=0 失败
     */
    int insertOrUpdateInven( MallProductInventory inven );

}
