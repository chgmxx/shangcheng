package com.gt.mall.service.web.product;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.base.BaseService;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.product.MallSpecifica;
import com.gt.mall.entity.product.MallSpecificaValue;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>
 * 商品的规格 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallProductSpecificaService extends BaseService< MallProductSpecifica > {

    /**
     * 根据规格id查询商品规格集合
     *
     * @param ids 规格id
     *
     * @return
     */
    List< MallProductSpecifica > selectBySpecIds( String[] ids );

    /**
     * 批量保存商品规格
     */
    Map< String,Object > saveOrUpdateBatch( Object obj, int proId, Map< String,Object > defaultMap, boolean isUpdate );

    /**
     * 批量保存商品规格
     */
    Map< String,Object > newSaveOrUpdateBatch( Object obj, int proId, boolean isUpdate );
    /**
     * 自定义商品规格
     *
     * @Title: insertSpecifica
     */
    Integer insertSpecifica( MallSpecifica spec );

    /**
     * 自定义规格值
     *
     * @Title: insertSpecificaValue
     */
    Integer insertSpecificaValue( MallSpecificaValue value );

    /**
     * 获取自定义规格名称
     *
     * @Title: getSpecificaByUser
     */
    SortedMap< String,Object > getSpecificaByUser( Map< String,Object > maps );

    /**
     * 获取自定义规格值
     *
     * @Title: getSpecificaById
     */
    SortedMap< String,Object > getSpecificaValueById( Map< String,Object > params );

    /**
     * 根据商品id来获取商品规格
     *
     * @Title: getSpecificaByProductId
     */
    List< Map< String,Object > > getSpecificaByProductId( Integer proId );

    /**
     * 同步商品规格
     *
     * @param specList
     * @param proId
     * @param shopId
     *
     * @return
     */
    Map< String,Object > copyProductSpecifica( List< MallProductSpecifica > specList, int proId, int shopId, int userId );

    /**
     * 新增或修改规格
     *
     * @param specList
     *
     * @return
     */
    String saveOrUpdateBatchErp( List< Map > specList, int userId, int proId );

    /**
     * 同步进销存的规格
     *
     * @param parentId 父规格id
     * @param specName 规格名称
     * @param userId   用户id
     * @param userPId  总账号id
     * @param uType    用户类型 1总账号  0子账号
     * @param userName 用户名
     *
     * @return
     */
    public int addErpSpecificas( int parentId, String specName, int userId, int userPId, int uType, String userName );

    /**
     * 新增或修改商品规格
     *
     * @param specifica
     *
     * @return
     */
    public boolean saveOrUpdateProSpecifica( MallProductSpecifica specifica );

    /**
     * 新增或修改规格
     *
     * @param nameId
     * @param valueId
     * @param erpNameId
     * @param erpValueId
     */
    public void saveOrUpSpecifica( int nameId, int valueId, int erpNameId, int erpValueId );

    /**
     * 同步erp规格
     *
     * @param userPId 总账号id
     */
    void syncErpSpecifica( int userPId );

    /**
     * 通过规格id获取规格值
     *
     * @param ids
     *
     * @return
     */
    List< MallProductSpecifica > selectBySpecId( String[] ids );

    /**
     * 保存erp的商品
     *
     * @param specArr
     * @param userId
     * @param proId
     *
     * @return
     */
    public String saveOrUpdateBatchErpSpec( JSONArray specArr, int userId, int proId );

    /**
     * 根据规格名称id和规格值id查询商品给
     *
     * @param params nameId     规格名称id
     * @param params valueId    规格值id
     * @param params proId      商品名称id
     *
     * @return
     */
    MallProductSpecifica selectByNameValueId( Map< String,Object > params );

    /**
     * 根据商品id查询商品规格
     *
     * @param productId 商品id
     */
    List< MallProductSpecifica > selectByProductId( Integer productId );

    /**
     * 根据商品和规格值id查询商品规格信息
     *
     * @param productIds 商品id
     * @param valueIds   规格值id
     *
     * @return 商品规格集合
     */
    List< MallProductSpecifica > selectByValueIds( int productIds, String[] valueIds );

    /**
     * 同步所有规格名称
     *
     * @param user 用户信息
     */

    void syncAllSpecifica( BusUser user );

    /**
     * 同步所有规格值
     *
     * @param user 用户信息
     */
    void syncAllSpecificaValue( BusUser user );

}
