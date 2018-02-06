package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductInventoryDAO;
import com.gt.mall.dao.product.MallProductSpecificaDAO;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 商品库存 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductInventoryServiceImpl extends BaseServiceImpl< MallProductInventoryDAO,MallProductInventory > implements MallProductInventoryService {

    @Autowired
    private MallProductInventoryDAO mallProductInventoryDAO;

    @Autowired
    private MallProductSpecificaService mallProductSpecificaService;

    @Autowired
    private MallProductSpecificaDAO mallProductSpecificaDAO;

    @Override
    public void saveOrUpdateBatch( Map< String,Object > specMap, Object obj, int proId, Map< String,Object > invenMap ) {
        // 解析页面传值
        List< Map > invenList = JSONArray.parseArray( (String) obj, Map.class );
        if ( invenList != null && invenList.size() > 0 ) {
            for ( Map map : invenList ) {
                MallProductInventory inventory = new MallProductInventory();
                if ( map != null ) {
                    // 解析规格
                    List< MallProductSpecifica > list = JSONArray.parseArray( map.get( "specificas" ).toString(), MallProductSpecifica.class );
                    int imgSpecId = 0;
                    boolean flag = true;
                    if ( list != null && list.size() > 0 ) {
                        StringBuilder specId = new StringBuilder();
                        for ( MallProductSpecifica speci : list ) {
                            String str = speci.getSpecificaNameId() + "_" + speci.getSpecificaValueId();
                            if ( !specId.toString().equals( "" ) ) {
                                specId.append( "," );
                            }
                            JSONObject jObj = JSONObject.parseObject( specMap.get( str ).toString() );
                            if ( !CommonUtil.isEmpty( jObj.get( "specId" ) ) ) {
                                imgSpecId = jObj.getInteger( "specId" );
                            }
                            specId.append( jObj.get( "id" ) );
                        }
                        if ( invenMap != null ) {
                            if ( !CommonUtil.isEmpty( invenMap.get( specId.toString() ) ) ) {
                                String idStr = invenMap.get( specId.toString() ).toString();
                                if ( CommonUtil.isInteger( idStr ) ) {
                                    int id = CommonUtil.toInteger( idStr );
                                    inventory.setId( id );
                                    invenMap.remove( specId.toString() );
                                    flag = false;
                                }
                            }
                        }
                        inventory.setSpecificaIds( specId.toString() );// 规格值
                    }
                    if ( CommonUtil.isNotEmpty( map.get( "invCode" ) ) ) {
                        inventory.setInvCode( CommonUtil.toString( map.get( "invCode" ) ) );
                    }
                    inventory.setInvNum( CommonUtil.toInteger( map.get( "invNum" ) ) );
                    inventory.setInvPrice( BigDecimal.valueOf( Double.valueOf( map.get( "invPrice" ).toString() ) ) );
                    inventory.setIsDefault( CommonUtil.toInteger( map.get( "isDefault" ) ) );
                    if ( imgSpecId > 0 ) {
                        inventory.setSpecificaImgId( imgSpecId );
                    } else {
                        inventory.setSpecificaImgId( 0 );
                    }
                    inventory.setProductId( proId );

                    if ( flag ) {
                        mallProductInventoryDAO.insert( inventory );
                    } else {
                        mallProductInventoryDAO.updateById( inventory );
                    }
                }
            }
        }
        if ( invenMap != null ) {
            for ( String str : invenMap.keySet() ) {
                MallProductInventory inven = new MallProductInventory();
                inven.setId( CommonUtil.toInteger( str ) );
                inven.setIsDelete( 1 );
                inven.setIsDefault( 0 );
                // 逻辑删除库存
                mallProductInventoryDAO.updateById( inven );
            }
        }
    }

    @Override
    public void newSaveOrUpdateBatch( Map< String,Object > specMap, Object obj, int proId ) {

        Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
        inventoryWrapper.where( "product_id =  {0} and is_delete = 0 ", proId );
        List< MallProductInventory > defaultList = mallProductInventoryDAO.selectList( inventoryWrapper );

        // 解析页面传值
        List< Map > invenList = JSONArray.parseArray( (String) obj, Map.class );
        if ( invenList != null && invenList.size() > 0 ) {
            for ( Map map : invenList ) {
                MallProductInventory inventory = new MallProductInventory();
                if ( map != null ) {
                    // 解析规格
                    List< MallProductSpecifica > list = JSONArray.parseArray( map.get( "specificas" ).toString(), MallProductSpecifica.class );
                    int imgSpecId = 0;
                    if ( list != null && list.size() > 0 ) {
                        StringBuilder specId = new StringBuilder();
                        for ( MallProductSpecifica speci : list ) {
                            String str = speci.getSpecificaNameId() + "_" + speci.getSpecificaValueId();
                            if ( !specId.toString().equals( "" ) ) {

                                specId.append( "," );
                            }
                            JSONObject jObj = JSONObject.parseObject( specMap.get( str ).toString() );
                            if ( !CommonUtil.isEmpty( jObj.get( "specId" ) ) ) {
                                imgSpecId = jObj.getInteger( "specId" );
                            }
                            specId.append( jObj.get( "id" ) );
                        }
                        if ( defaultList != null ) {
                            for ( MallProductInventory inventory1 : defaultList ) {
                                if ( specId.toString().equals( inventory1.getSpecificaIds() ) ) {
                                    inventory.setId( inventory1.getId() );
                                    defaultList.remove( inventory1 );
                                    break;
                                }
                            }
                        }
                        inventory.setSpecificaIds( specId.toString() );// 规格值
                    }
                    if ( CommonUtil.isNotEmpty( map.get( "invCode" ) ) ) {
                        inventory.setInvCode( map.get( "invCode" ).toString() );
                    }
                    inventory.setInvNum( CommonUtil.toInteger( map.get( "invNum" ) ) );
                    inventory.setInvPrice( BigDecimal.valueOf( Double.valueOf( map.get( "invPrice" ).toString() ) ) );
                    inventory.setIsDefault( CommonUtil.toInteger( map.get( "isDefault" ) ) );
                    if ( CommonUtil.isNotEmpty( map.get( "logisticsWeight" ) ) ) {
                        inventory.setLogisticsWeight( CommonUtil.toBigDecimal( map.get( "logisticsWeight" ) ) );
                    }
                    if ( imgSpecId > 0 ) {
                        inventory.setSpecificaImgId( imgSpecId );
                    } else {
                        inventory.setSpecificaImgId( 0 );
                    }
                    inventory.setProductId( proId );

                    if ( CommonUtil.isEmpty( inventory.getId() ) ) {
                        mallProductInventoryDAO.insert( inventory );
                    } else {
                        mallProductInventoryDAO.updateById( inventory );
                    }
                }
            }
        }
        if ( defaultList != null ) {
            for ( MallProductInventory inventory : defaultList ) {
                inventory.setIsDelete( 1 );
                inventory.setIsDefault( 0 );
                mallProductInventoryDAO.updateById( inventory );
            }
        }
    }

    @Override
    public List< MallProductInventory > getInventByProductId( int productId ) {
        Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
        inventoryWrapper.where( "product_id =  {0} and is_delete = 0 ", productId );
        return mallProductInventoryDAO.selectList( inventoryWrapper );
    }

    @Override
    public List< MallProductInventory > getInventByProductIds( List< Integer > productId ) {
        Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
        inventoryWrapper.where( "is_delete = 0" ).in( "product_id", productId );
        return mallProductInventoryDAO.selectList( inventoryWrapper );
    }

    @Override
    public MallProductInventory selectByIsDefault( Integer productId ) {
        MallProductInventory mallProductInventory = new MallProductInventory();
        mallProductInventory.setIsDelete( 0 );
        mallProductInventory.setIsDefault( 1 );
        mallProductInventory.setProductId( productId );
        MallProductInventory inventory = mallProductInventoryDAO.selectOne( mallProductInventory );
        if ( inventory != null && inventory.getSpecificaIds() != null ) {
            String[] ids = inventory.getSpecificaIds().split( "," );

            List< MallProductSpecifica > specList = mallProductSpecificaService.selectBySpecIds( ids );//查询规格集合
            if ( specList != null ) {
                inventory.setSpecList( specList );
            }
        }
        return inventory;
    }

    @Override
    public MallProductInventory selectDefaultInvenNotNullStock( int productId, int invenId, PhoneProductDetailResult result ) {
        MallProductInventory inventory;
        if ( productId > 0 ) {
            inventory = mallProductInventoryDAO.selectById( invenId );
        } else {
            MallProductInventory mallProductInventory = new MallProductInventory();
            mallProductInventory.setIsDelete( 0 );
            mallProductInventory.setIsDefault( 1 );
            mallProductInventory.setProductId( productId );
            inventory = mallProductInventoryDAO.selectOne( mallProductInventory );
        }
        if ( CommonUtil.isNotEmpty( inventory ) && CommonUtil.isNotEmpty( result.getInvIdList() ) && result.getInvIdList().size() > 0 ) {
            //库存为空 ，重新获取库存
            if ( CommonUtil.isEmpty( inventory.getInvNum() ) || inventory.getInvNum() == 0 ) {
                for ( Integer invId : result.getInvIdList() ) {
                    if ( CommonUtil.isEmpty( invenId ) ) {
                        continue;
                    }
                    inventory = mallProductInventoryDAO.selectById( invId );
                    if ( CommonUtil.isNotEmpty( inventory.getInvNum() ) && inventory.getInvNum() > 0 ) {
                        break;
                    }
                }
            }
        }
        return inventory;
    }

    @Override
    public List< MallProductInventory > selectByIdListDefault( List< Integer > productList ) {

        Wrapper< MallProductInventory > wrapper = new EntityWrapper<>();
        wrapper.where( "is_delete = 0 and is_default = 1" ).in( "product_id", productList );

        return mallProductInventoryDAO.selectList( wrapper );
    }

    @Override
    public List< MallProductInventory > selectInvenByProductId( Integer productId ) {
        Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
        inventoryWrapper.where( "product_id =  {0} and is_delete = 0 ", productId );
        List< MallProductInventory > inventoryList = mallProductInventoryDAO.selectList( inventoryWrapper );
        if ( inventoryList != null && inventoryList.size() > 0 ) {
            for ( MallProductInventory inventory : inventoryList ) {
                String specIds = inventory.getSpecificaIds();
                if ( specIds != null ) {
                    List< MallProductSpecifica > specList = mallProductSpecificaService.selectBySpecIds( specIds.split( "," ) );//查询规格集合
                    inventory.setSpecList( specList );
                }

            }
        }

        return inventoryList;
    }

    @Override
    public MallProductInventory selectInvNumByProId( Map< String,Object > params ) {
        MallProductInventory mallProductInventory = new MallProductInventory();
        mallProductInventory.setIsDelete( 0 );
        if ( CommonUtil.isNotEmpty( params.get( "proId" ) ) ) {
            mallProductInventory.setProductId( CommonUtil.toInteger( params.get( "proId" ) ) );
        }
        if ( CommonUtil.isNotEmpty( params.get( "specificaIds" ) ) ) {
            mallProductInventory.setSpecificaIds( CommonUtil.toString( params.get( "specificaIds" ) ) );
        }
        return mallProductInventoryDAO.selectOne( mallProductInventory );
    }

    @Override
    public void copyProductInven( List< MallProductInventory > invenList, Map< String,Object > specMap, int proId,int oldProId ) throws Exception {
        if ( invenList != null && invenList.size() > 0 ) {
            if ( CommonUtil.isEmpty( specMap ) ) {
                throw new BusinessException( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
            }
            for ( MallProductInventory inven : invenList ) {
                if ( !inven.getProductId().toString().equals( CommonUtil.toString( oldProId ) ) ) {
                    continue;
                }
                inven.setInvSaleNum( 0 );
                inven.setProductId( proId );
                String specId = inven.getSpecificaIds();
                if ( CommonUtil.isNotEmpty( specId ) ) {
                    String[] specStr = specId.split( "," );
                    String specIds = "";
                    for ( String oldSpecId : specStr ) {
                        if ( CommonUtil.isNotEmpty( specMap.get( oldSpecId ) ) ) {
                            JSONObject obj = JSONObject.parseObject( specMap.get( oldSpecId ).toString() );
                            if ( CommonUtil.isNotEmpty( specIds ) ) {
                                specIds += ",";
                            }
                            specIds += obj.get( "newId" ).toString();
                            if ( CommonUtil.isNotEmpty( obj.get( "isImage" ) ) ) {
                                inven.setSpecificaImgId( CommonUtil.toInteger( obj.get( "newId" ) ) );
                            }
                        }
                    }
                    if ( CommonUtil.isNotEmpty( specIds ) ) {
                        inven.setSpecificaIds( specIds );
                    }
                    inven.setId( null );
                    mallProductInventoryDAO.insert( inven );
                }
            }
        }
    }

    @Override
    public Map< String,Object > saleOrUpdateBatchErp( Map map, int proId, int userId ) {
        int code = 1;
        String msg = "";
        List< Map > invenList = JSONArray.parseArray( map.get( "invenList" ).toString(), Map.class );
        if ( invenList != null && invenList.size() > 0 ) {
            for ( int i = 0; i < invenList.size(); i++ ) {
                Map invenMap = invenList.get( i );
                MallProductInventory inven = new MallProductInventory();

                if ( CommonUtil.isNotEmpty( invenMap.get( "specList" ) ) ) {
                    List< Map > specList = JSONArray.parseArray( invenMap.get( "specList" ).toString(), Map.class );
                    String specIds = mallProductSpecificaService.saveOrUpdateBatchErp( specList, userId, proId );
                    if ( CommonUtil.isNotEmpty( specIds ) ) {
                        inven.setSpecificaIds( specIds );
                    } else {
                        code = -1;
                        msg = "商品规格不完整";
                        break;
                    }
                }

                inven.setInvPrice( BigDecimal.valueOf( CommonUtil.toDouble( invenMap.get( "invPrice" ) ) ) );
                inven.setInvCode( CommonUtil.toString( invenMap.get( "invCode" ) ) );
                inven.setErpInvId( CommonUtil.toInteger( invenMap.get( "erpInvId" ) ) );
                inven.setErpSpecvalueId( CommonUtil.toString( invenMap.get( "erpSpecvalueId" ) ) );
                inven.setProductId( proId );
                if ( i == 0 ) {
                    inven.setIsDefault( 1 );
                }
                Map< String,Object > params = new HashMap<>();
                params.put( "proId", proId );
                params.put( "specificaIds", inven.getSpecificaIds() );
                MallProductInventory proInven = selectInvNumByProId( params );
                if ( CommonUtil.isNotEmpty( proInven ) ) {
                    if ( CommonUtil.isNotEmpty( proInven.getId() ) ) {
                        inven.setId( proInven.getId() );
                    }
                }
                int count = insertOrUpdateInven( inven );//新增或修改
                if ( count <= 0 ) {
                    code = -1;
                    msg = "商品库存新增异常";
                    break;
                }
            }
        }
        Map< String,Object > resultMap = new HashMap<>();
        resultMap.put( "code", code );
        if ( code == -1 ) {
            resultMap.put( "errorMsg", msg );
        }
        return resultMap;
    }

    @Override
    public MallProductInventory selectBySpecIds( int productId, String specificaIds ) {
        Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
        inventoryWrapper.where( "specifica_ids = {0} and product_id={1} and is_delete = 0 ", specificaIds, productId );
        List< MallProductInventory > inventoryList = mallProductInventoryDAO.selectList( inventoryWrapper );
        if ( inventoryList != null && inventoryList.size() > 0 ) {
            return inventoryList.get( 0 );
        }
        return null;
    }

    @Override
    public MallProductInventory selectByErpInvId( int productId, int erpInvId ) {
        Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
        inventoryWrapper.where( "p.product_id = {0} AND p.erp_inv_id= {1} and is_delete = 0 ", productId, erpInvId );
        List< MallProductInventory > inventoryList = mallProductInventoryDAO.selectList( inventoryWrapper );
        if ( inventoryList != null && inventoryList.size() > 0 ) {
            return inventoryList.get( 0 );
        }
        return null;
    }

    @Override
    public int updateProductInventory( MallProductInventory inventory, Integer proNum, Integer type ) {
        if ( CommonUtil.isEmpty( proNum ) || proNum == 0 ) {
            return 0;
        }
        Map< String,Object > params = new HashMap<>();
        params.put( "type", type );
        params.put( "inventory_id", inventory.getId() );
        params.put( "product_id", inventory.getProductId() );
        params.put( "pro_num", proNum );
        return mallProductInventoryDAO.updateProductStock( params );
    }

    @Override
    public Map< String,Object > productSpecifications( Integer id, String inv_id ) {
        Map< String,Object > map = new HashMap<>();
        boolean defaultHasInvNum = true;
        Wrapper< MallProductInventory > inventoryWrapper = new EntityWrapper<>();
        inventoryWrapper.setSqlSelect( "id,specifica_ids,inv_price,inv_num,inv_code,logistics_weight" );
        inventoryWrapper.where( "is_delete = 0 and product_id = {0}", id );
        if ( CommonUtil.isNotEmpty( inv_id ) ) {
            inventoryWrapper.andNew( "id={0} and inv_num > 0", inv_id );
        }
        inventoryWrapper.orderBy( "is_default", false ).orderBy( "inv_num", false );
        List< Map< String,Object > > inventoryList = mallProductInventoryDAO.selectMaps( inventoryWrapper );
        if ( inventoryList == null || inventoryList.size() == 0 ) {
            inventoryWrapper = new EntityWrapper<>();
            inventoryWrapper.setSqlSelect( "id,specifica_ids,inv_price,inv_num,inv_code,logistics_weight" );
            inventoryWrapper.where( "is_delete = 0 and product_id = {0}", id );
            inventoryWrapper.orderBy( "inv_num", false );
            inventoryList = mallProductInventoryDAO.selectMaps( inventoryWrapper );
            defaultHasInvNum = false;
        }
        if ( inventoryList != null && inventoryList.size() > 0 && CommonUtil.isEmpty( inv_id ) ) {
            map = inventoryList.get( 0 );
            if ( CommonUtil.isEmpty( map.get( "inv_num" ) ) || map.get( "inv_num" ).toString().equals( "0" ) ) {
                inventoryWrapper = new EntityWrapper<>();
                inventoryWrapper.setSqlSelect( "id,specifica_ids,inv_price,inv_num,inv_code,logistics_weight" );
                inventoryWrapper.where( "is_delete = 0 and product_id = {0}", id );
                inventoryWrapper.andNew( " inv_num > 0 " );
                if ( defaultHasInvNum ) {
                    inventoryWrapper.orderBy( "is_default", false );
                } else {
                    inventoryWrapper.orderBy( "inv_num", false );
                }
                inventoryList = mallProductInventoryDAO.selectMaps( inventoryWrapper );
            }
        }

        if ( inventoryList != null && inventoryList.size() > 0 ) {
            map = inventoryList.get( 0 );
        }
        if ( CommonUtil.isNotEmpty( map ) ) {
            String specifica_ids = map.get( "specifica_ids" ).toString();
            String specifica_id[] = specifica_ids.split( "," );
            StringBuilder specifica_names = new StringBuilder();
            StringBuilder xids = new StringBuilder();

            Wrapper< MallProductSpecifica > wrapper = new EntityWrapper<>();
            wrapper.in( "id", specifica_id );
            List< MallProductSpecifica > specificaList = mallProductSpecificaDAO.selectList( wrapper );
            if ( specificaList != null && specificaList.size() > 0 ) {
                for ( int i = 0; i < specificaList.size(); i++ ) {
                    MallProductSpecifica specifica = specificaList.get( i );
                    specifica_names.append( specifica.getSpecificaValue() ).append( "&nbsp;&nbsp;" );
                    xids.append( specifica.getSpecificaValueId() ).append( "," );
                }
            }
            map.put( "specifica_name", specifica_names.toString() );
            map.put( "xids", xids.toString().substring( 0, xids.length() - 1 ) );
            map.put( "weight", map.get( "logistics_weight" ) );
        }
        return map;
    }

    @Override
    public List< Map< String,Object > > guigePrice( Integer productId ) {
        List< Map< String,Object > > xlist = new ArrayList<>();
        List< Map< String,Object > > list = mallProductInventoryDAO.selectInvenByProId( productId );
        if ( list != null && list.size() > 0 ) {
            List< Integer > speIdList = new ArrayList<>();
            for ( Map< String,Object > map : list ) {
                String specifica_ids = map.get( "specifica_ids" ).toString();
                String specifica_id[] = specifica_ids.split( "," );
                for ( String s : specifica_id ) {
                    if ( !speIdList.contains( CommonUtil.toInteger( s ) ) ) {
                        speIdList.add( CommonUtil.toInteger( s ) );
                    }
                }
            }
            List< MallProductSpecifica > specificaList = new ArrayList<>();
            if ( speIdList != null && speIdList.size() > 0 ) {
                Wrapper< MallProductSpecifica > wrapper = new EntityWrapper<>();
                wrapper.in( "id", speIdList );
                specificaList = mallProductSpecificaDAO.selectList( wrapper );
            }

            for ( Map< String,Object > map : list ) {
                String specifica_ids = map.get( "specifica_ids" ).toString();
                String specifica_id[] = specifica_ids.split( "," );
                List slist = Arrays.asList( specifica_id );
                StringBuilder xids = new StringBuilder();
                StringBuilder values = new StringBuilder();
                if ( specificaList != null && specificaList.size() > 0 ) {
                    for ( Object o : slist ) {
                        String ids = o.toString();
                        for ( int i = 0; i < specificaList.size(); i++ ) {
                            MallProductSpecifica specifica = specificaList.get( i );
                            if ( ids.equals( specifica.getId().toString() ) ) {
                                xids.append( specifica.getSpecificaValueId() ).append( "," );
                                values.append( specifica.getSpecificaValue() ).append( "," );
                                break;
                            }
                        }
                    }
                }
                map.put( "values", values.toString().substring( 0, values.length() - 1 ) );
                map.put( "xsid", xids.toString().substring( 0, xids.length() - 1 ) );
                xlist.add( map );
            }
        }
        return xlist;
    }

    @Override
    public int insertOrUpdateInven( MallProductInventory inven ) {
        int count;
        if ( CommonUtil.isEmpty( inven.getId() ) ) {
            if ( CommonUtil.isEmpty( inven.getInvNum() ) ) {
                inven.setInvNum( 0 );
            }
            count = mallProductInventoryDAO.insert( inven );
        } else {
            count = mallProductInventoryDAO.updateById( inven );
        }
        return count;
    }

}
