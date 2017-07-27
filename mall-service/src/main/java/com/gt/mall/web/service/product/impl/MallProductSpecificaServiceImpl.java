package com.gt.mall.web.service.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductSpecificaDAO;
import com.gt.mall.dao.product.MallSpecificaDAO;
import com.gt.mall.dao.product.MallSpecificaValueDAO;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.entity.product.MallSpecifica;
import com.gt.mall.entity.product.MallSpecificaValue;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.MallJxcHttpClientUtil;
import com.gt.mall.web.service.product.MallProductSpecificaService;
import com.gt.mall.web.service.product.MallSpecificaService;
import com.gt.mall.web.service.product.MallSpecificaValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 商品的规格 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductSpecificaServiceImpl extends BaseServiceImpl< MallProductSpecificaDAO,MallProductSpecifica > implements MallProductSpecificaService {

    @Autowired
    private MallProductSpecificaDAO mallProductSpecificaDAO;//商品规格dao

    @Autowired
    private MallSpecificaService mallSpecificaService;//父类规格业务处理类

    @Autowired
    private MallSpecificaValueService mallSpecificaValueService;

    @Autowired
    private MallSpecificaDAO mallSpecificaDAO;//父类规格dao

    @Autowired
    private MallSpecificaValueDAO mallSpecificaValueDAO;//子类规格dao

    @Override
    public List< MallProductSpecifica > selectBySpecIds( String[] ids ) {
	Wrapper< MallProductSpecifica > specificaWrapper = new EntityWrapper<>();
	specificaWrapper.where( "is_delete = 0" ).in( "id", ids ).orderBy( "sort", true );//生成的sql  where is_delete = 0 and id in (1,2) order by sort asc
	return mallProductSpecificaDAO.selectList( specificaWrapper );//查询规格集合
    }

    @Override
    public Map< String,Object > saveOrUpdateBatch( Object obj, int proId, Map< String,Object > defaultMap, boolean isUpdate ) {
	Map< String,Object > specMap = new HashMap< String,Object >();
	List< MallProductSpecifica > speList = JSONArray.parseArray( obj.toString(), MallProductSpecifica.class );
	if ( speList != null && speList.size() > 0 ) {
	    for ( MallProductSpecifica spe : speList ) {
		if ( spe != null ) {
		    spe.setProductId( proId );

		    String str = spe.getSpecificaNameId() + "_" + spe.getSpecificaValueId();
		    boolean flag = true;
		    if ( CommonUtil.isNotEmpty( defaultMap ) ) {
			if ( CommonUtil.isNotEmpty( defaultMap.get( str ) ) ) {
			    String idStr = defaultMap.get( str ).toString();
			    if ( CommonUtil.isInteger( idStr ) ) {
				spe.setId( CommonUtil.toInteger( idStr ) );
				defaultMap.remove( str );
				flag = false;
			    }
			}
		    }
		    if ( isUpdate ) {//没有参加团购的商品才可以修改商品规格
			if ( flag ) {
			    mallProductSpecificaDAO.insert( spe );//添加商品规格
			} else {
			    mallProductSpecificaDAO.updateById( spe );//修改商品规格
			}
		    }
		    if ( spe.getId() > 0 ) {
			JSONObject jObj = new JSONObject();
			if ( !CommonUtil.isEmpty( spe.getSpecificaImgUrl() ) ) {
			    jObj.put( "specId", spe.getId() );
			}
			jObj.put( "id", spe.getId() );
			specMap.put( str, jObj );
		    }
		}
	    }
	    if ( defaultMap != null && isUpdate ) {//没有参加团购的商品才可以修改商品规格
		Iterator it = defaultMap.entrySet().iterator();
		while ( it.hasNext() ) {
		    Map.Entry< String,Integer > entry = (Map.Entry< String,Integer >) it.next();
		    MallProductSpecifica pSpe = new MallProductSpecifica();
		    pSpe.setId( CommonUtil.toInteger( entry.getValue() ) );
		    pSpe.setIsDelete( 1 );
		    // 逻辑删除规格
		    mallProductSpecificaDAO.updateById( pSpe );
		}
	    }
	}
	return specMap;
    }

    @Override
    public Integer insertSpecifica( MallSpecifica spec ) {
	spec.setCreateTime( new Date() );
	return mallSpecificaDAO.insert( spec );
    }

    @Override
    public Integer insertSpecificaValue( MallSpecificaValue value ) {
	return mallSpecificaValueDAO.insert( value );
    }

    @Override
    public SortedMap< String,Object > getSpecificaByUser( Map< String,Object > maps ) {
	Wrapper< MallSpecifica > specificaWrapper = new EntityWrapper< MallSpecifica >();
	specificaWrapper.where( " is_delete=0 and (user_id = {0} or is_back_end=1) and type={1} " ).orderBy( "id", true );
	List< MallSpecifica > specificaList = mallSpecificaDAO.selectList( specificaWrapper );
	SortedMap< String,Object > map = new TreeMap< String,Object >();
	for ( MallSpecifica specifica : specificaList ) {
	    map.put( CommonUtil.toString( specifica.getId() ), specifica.getSpecName() );
	}
	return map;
    }

    @Override
    public SortedMap< String,Object > getSpecificaValueById( Map< String,Object > params ) {

	Wrapper< MallSpecificaValue > specificaWrapper = new EntityWrapper< MallSpecificaValue >();
	specificaWrapper.where( " spec_id = {0} and type={1} and is_delete = 0 " );
	List< MallSpecificaValue > list = mallSpecificaValueDAO.selectList( specificaWrapper );

	SortedMap< String,Object > map = new TreeMap< String,Object >();
	for ( MallSpecificaValue value : list ) {
	    map.put( CommonUtil.toString( value.getId() ), value.getSpecValue() );
	}
	return map;
    }

    @Override
    public List< Map< String,Object > > getSpecificaByProductId( Integer proId ) {
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();

	Wrapper< MallProductSpecifica > productSpecificaWrapper = new EntityWrapper<>();
	productSpecificaWrapper.where( "product_id = {0} and is_delete = 0", proId );
	List< MallProductSpecifica > specList = mallProductSpecificaDAO.selectList( productSpecificaWrapper );
	if ( specList != null && specList.size() > 0 ) {
	    // 遍历商品规格
	    for ( MallProductSpecifica spec : specList ) {
		if ( CommonUtil.isEmpty( spec.getSpecificaNameId() ) || CommonUtil.isEmpty( spec.getSpecificaValueId() ) ) {
		    break;
		}
		int specId = spec.getSpecificaNameId();// 规格id
		int valueId = spec.getSpecificaValueId();// 规格值id

		Map< String,Object > specValueMap = new HashMap< String,Object >();// 存放规格值

		specValueMap.put( "specValueId", valueId );
		specValueMap.put( "specValue", spec.getSpecificaValue() );
		specValueMap.put( "specImage", spec.getSpecificaImgUrl() );
		specValueMap.put( "id", spec.getId() );
		specValueMap.put( "erpValueId", spec.getErpSpecvalueId() );

		boolean flag = true;

		if ( list.size() > 0 ) {
		    for ( Map< String,Object > specMap : list ) {
			int nameId = CommonUtil.toInteger( specMap.get( "specNameId" ).toString() );
			// 有相同的规格值id
			if ( nameId == specId ) {
			    List< Map > mapList = JSONArray.parseArray( specMap.get( "specValues" ).toString(), Map.class );
			    mapList.add( specValueMap );
			    flag = false;
			}
		    }
		}
		if ( flag ) {
		    List< Map< String,Object > > specValueList = new ArrayList< Map< String,Object > >();
		    specValueList.add( specValueMap );

		    Map< String,Object > map = new HashMap< String,Object >();// 存放规格名称
		    map.put( "specNameId", specId );
		    map.put( "specName", spec.getSpecificaName() );
		    map.put( "productId", spec.getProductId() );
		    map.put( "specValues", specValueList );
		    map.put( "erpNameId", spec.getErpSpecnameId() );
		    list.add( map );
		}

	    }
	}
	return list;
    }

    @Override
    public Map< String,Object > copyProductSpecifica( List< MallProductSpecifica > specList, int proId, int shopId, int userId ) {
	Map< String,Object > specMap = new HashMap< String,Object >();

	if ( specList != null && specList.size() > 0 ) {
	    for ( MallProductSpecifica proSpecifica : specList ) {
		proSpecifica.setProductId( proId );
		int oldId = proSpecifica.getId();
		proSpecifica.setId( null );
		//		mallProductSpecificaMapper.insertSelective(proSpecifica);//同步商品规格
		mallProductSpecificaDAO.insert( proSpecifica );
		if ( CommonUtil.isNotEmpty( proSpecifica.getId() ) ) {
		    JSONObject obj = new JSONObject();
		    obj.put( "newId", proSpecifica.getId() );
		    if ( CommonUtil.isNotEmpty( proSpecifica.getSpecificaImgUrl() ) ) {
			obj.put( "isImage", 1 );
		    }
		    specMap.put( oldId + "", obj );
		}
	    }
	}
	return specMap;
    }

    @Override
    public String saveOrUpdateBatchErp( List< Map > specList, int userId, int proId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	map.put( "userId", userId );
	String specIds = "";
	int code = 1;
	if ( specList != null && specList.size() > 0 ) {
	    for ( int i = 0; i < specList.size(); i++ ) {
		Map< String,Object > specMaps = specList.get( i );
		if ( CommonUtil.isEmpty( specMaps.get( "erpSpecnameId" ) ) || CommonUtil.isEmpty( specMaps.get( "specificaName" ) )
				|| CommonUtil.isEmpty( specMaps.get( "erpSpecvalueId" ) ) || CommonUtil.isEmpty( specMaps.get( "specificaValue" ) ) || code < 1 ) {
		    code = -1;
		    break;
		}
		int nameId = 0;//商城的规格名称id
		String specName = specMaps.get( "specificaName" ).toString();

		int erpNameId = CommonUtil.toInteger( specMaps.get( "erpSpecnameId" ) );//erp的规格id
		map.put( "erpNameId", erpNameId );
		map.put( "name", specName );
		MallSpecifica specifica = mallSpecificaService.selectBySpecName( map );//根据erp规格名称id查询规格信息
		if ( specifica == null ) {
		    map.remove( "erpNameId" );
		    specifica = mallSpecificaService.selectBySpecName( map );//根据erp规格名称查询规格信息
		}
		boolean isInsert = true;
		if ( CommonUtil.isNotEmpty( specifica ) ) {
		    if ( CommonUtil.isNotEmpty( specifica.getId() ) ) {
			isInsert = false;
			nameId = specifica.getId();
		    }
		}
		if ( isInsert ) {//规格不存在，则新增规格
		    specifica = new MallSpecifica();
		    specifica.setSpecName( specName );
		    specifica.setUserId( userId );
		    specifica.setCreateTime( new Date() );
		    specifica.setErpNameId( erpNameId );
		    mallSpecificaDAO.insert( specifica );
		    nameId = specifica.getId();
		}
		isInsert = true;
		int valueId = 0;//商城的规格值id
		int erpValueId = CommonUtil.toInteger( specMaps.get( "erpSpecvalueId" ) );//erp的规格id
		String specValue = CommonUtil.toString( specMaps.get( "specificaValue" ) );//规格值
		map.put( "erpValueId", erpValueId );
		map.put( "value", specValue );
		MallSpecificaValue value = mallSpecificaValueService.selectBySpecValue( map );
		if ( CommonUtil.isEmpty( value ) ) {
		    map.remove( "erpValueId" );
		    value = mallSpecificaValueService.selectBySpecValue( map );
		}
		if ( CommonUtil.isNotEmpty( value ) ) {
		    if ( CommonUtil.isNotEmpty( value.getId() ) ) {
			isInsert = false;
			valueId = value.getId();
		    }
		}
		if ( isInsert ) {//规格值不存在，则新增规格值
		    value = new MallSpecificaValue();
		    value.setSpecValue( specValue );
		    value.setUserId( userId );
		    value.setSpecId( nameId );
		    value.setErpValueId( erpValueId );
		    mallSpecificaValueDAO.insert( value );
		    valueId = value.getId();

		}
		MallProductSpecifica productSpecifica = new MallProductSpecifica();
		productSpecifica.setProductId( proId );
		Map< String,Object > params = new HashMap< String,Object >();
		params.put( "nameId", nameId );
		params.put( "valueId", valueId );
		params.put( "proId", proId );
		MallProductSpecifica proSpec = selectByNameValueId( params );
		if ( CommonUtil.isNotEmpty( proSpec ) ) {
		    if ( CommonUtil.isNotEmpty( proSpec.getId() ) ) {
			productSpecifica.setId( proSpec.getId() );
		    }
		}
		productSpecifica.setSort( i );
		productSpecifica.setSpecificaName( specName );
		productSpecifica.setSpecificaNameId( nameId );
		productSpecifica.setSpecificaValue( specValue );
		productSpecifica.setSpecificaValueId( valueId );
		productSpecifica.setErpSpecnameId( erpNameId );
		productSpecifica.setErpSpecvalueId( erpValueId );
		if ( CommonUtil.isNotEmpty( productSpecifica.getId() ) ) {
		    mallProductSpecificaDAO.updateById( productSpecifica );
		} else {
		    mallProductSpecificaDAO.insert( productSpecifica );//同步商品规格
		}
		if ( CommonUtil.isNotEmpty( specIds ) ) {
		    specIds += ",";
		}
		specIds += productSpecifica.getId();
	    }
	}
	if ( code == 1 ) {
	    return specIds;
	}
	return null;
    }

    @Override
    public int addErpSpecificas( int parentId, String specName, int userId, int userPId, int uType, String userName ) {
	int id = 0;
	JSONObject specParams = new JSONObject();
	specParams.put( "uId", userId );
	specParams.put( "uType", uType );
	specParams.put( "uName", userName );
	specParams.put( "rootUid", userPId );
	Map< String,Object > specParams2 = new HashMap< String,Object >();
	specParams2.put( "id", "" );
	specParams2.put( "name", specName );
	if ( parentId > 0 ) {
	    specParams2.put( "parentId", parentId );
	}
	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();
	list.add( specParams2 );
	specParams.put( "norms", list );

	Map< String,Object > attrParams = new HashMap< String,Object >();
	attrParams.put( "attrs", specParams );

	com.alibaba.fastjson.JSONArray arr = MallJxcHttpClientUtil.batchAttrSave( attrParams, true );
	if ( arr != null && arr.size() > 0 ) {
	    com.alibaba.fastjson.JSONObject jObj = com.alibaba.fastjson.JSONObject.parseObject( arr.get( 0 ).toString() );
	    id = jObj.getInteger( "id" );
	}
	return id;
    }

    @Override
    public boolean saveOrUpdateProSpecifica( MallProductSpecifica specifica ) {
	int code = -1;
	if ( CommonUtil.isNotEmpty( specifica.getId() ) ) {
	    code = mallProductSpecificaDAO.updateById( specifica );
	} else {
	    code = mallProductSpecificaDAO.insert( specifica );
	}
	if ( code > 0 ) {
	    return true;
	}
	return false;
    }

    @Override
    public void saveOrUpSpecifica( int nameId, int valueId, int erpNameId, int erpValueId ) {
	MallSpecifica name = new MallSpecifica();
	name.setId( nameId );
	name.setErpNameId( erpNameId );
	mallSpecificaDAO.updateById( name );

	MallSpecificaValue value = new MallSpecificaValue();
	value.setId( valueId );
	value.setErpValueId( erpValueId );
	mallSpecificaValueDAO.updateById( value );
    }

    @Override
    public void syncErpSpecifica( int userPId ) {
	Map< String,Object > productAttrMap = new HashMap< String,Object >();
	productAttrMap.put( "rootUid", userPId );
	com.alibaba.fastjson.JSONArray arr = MallJxcHttpClientUtil.getProductAttrs( productAttrMap, true );
	if ( arr != null && arr.size() > 0 ) {
	    for ( Object object : arr ) {
		JSONObject specObj = JSONObject.parseObject( object.toString() );
		int erpNameId = specObj.getInteger( "id" );
		String erpName = specObj.getString( "name" );
		System.out.println( "父类规格：" + erpNameId + "---" + erpName );

		if ( CommonUtil.isNotEmpty( specObj.get( "childs" ) ) ) {
		    JSONArray childArr = specObj.getJSONArray( "childs" );
		    if ( childArr != null && childArr.size() > 0 ) {
			for ( Object object2 : childArr ) {
			    JSONObject childObj = JSONObject.parseObject( object2.toString() );
			    int erpValueId = childObj.getInteger( "id" );
			    String erpValue = childObj.getString( "name" );

			    System.out.println( "子类规格：" + erpValueId + "---" + erpValue );
			}
		    }
		}

	    }
	}
    }

    @Override
    public List< MallProductSpecifica > selectBySpecId( String[] ids ) {
	Wrapper< MallProductSpecifica > productSpecificaWrapper = new EntityWrapper<>();
	productSpecificaWrapper.where( "is_delete = 0" ).in( "id", ids ).orderBy( "sort", true );
	return mallProductSpecificaDAO.selectList( productSpecificaWrapper );
    }

    @Override
    public String saveOrUpdateBatchErpSpec( JSONArray specArr, int userId, int proId ) {
	Map< String,Object > map = new HashMap< String,Object >();
	map.put( "userId", userId );
	String specIds = "";
	int code = 1;
	if ( specArr != null && specArr.size() > 0 ) {
	    for ( int i = 0; i < specArr.size(); i++ ) {
		JSONObject specObj = JSONObject.parseObject( specArr.get( i ).toString() );
		int erpNameId = specObj.getInteger( "id" );
		String specName = specObj.getString( "name" );
		System.out.print( "父类规格id:" + specObj.getInteger( "id" ) );
		System.out.print( "--父类规格：" + specObj.getString( "name" ) );

		JSONObject childSpecObj = specObj.getJSONObject( "child" );//子类规格
		int erpValueId = childSpecObj.getInteger( "id" );
		String specValue = childSpecObj.getString( "name" );
		System.out.print( "----子类规格id:" + childSpecObj.getInteger( "id" ) );
		System.out.println( "--子类规格：" + childSpecObj.getString( "name" ) );

		int nameId = 0;//商城的规格名称id

		map.put( "erpNameId", erpNameId );
		map.put( "name", specName );
		MallSpecifica specifica = mallSpecificaService.selectBySpecName( map );//根据erp规格名称id查询规格信息
		if ( CommonUtil.isEmpty( specifica ) ) {
		    map.remove( "erpNameId" );
		    specifica = mallSpecificaService.selectBySpecName( map );//根据erp规格名称查询规格信息
		}
		boolean isInsert = true;
		if ( CommonUtil.isNotEmpty( specifica ) ) {
		    if ( CommonUtil.isNotEmpty( specifica.getId() ) ) {
			isInsert = false;
			nameId = specifica.getId();
		    }
		}
		if ( isInsert ) {//规格不存在，则新增规格
		    specifica = new MallSpecifica();
		    specifica.setSpecName( specName );
		    specifica.setUserId( userId );
		    specifica.setCreateTime( new Date() );
		    specifica.setErpNameId( erpNameId );
		    mallSpecificaDAO.insert( specifica );
		    nameId = specifica.getId();
		}
		isInsert = true;
		int valueId = 0;//商城的规格值id
		map.put( "erpValueId", erpValueId );
		map.put( "value", specValue );
		MallSpecificaValue value = mallSpecificaValueService.selectBySpecValue( map );
		if ( CommonUtil.isEmpty( value ) ) {
		    map.remove( "erpValueId" );
		    value = mallSpecificaValueService.selectBySpecValue( map );
		}
		if ( CommonUtil.isNotEmpty( value ) ) {
		    if ( CommonUtil.isNotEmpty( value.getId() ) ) {
			isInsert = false;
			valueId = value.getId();
		    }
		}
		if ( isInsert ) {//规格值不存在，则新增规格值
		    value = new MallSpecificaValue();
		    value.setSpecValue( specValue );
		    value.setUserId( userId );
		    value.setSpecId( nameId );
		    value.setErpValueId( erpValueId );
		    mallSpecificaValueDAO.insert( value );
		    valueId = value.getId();
		}
		MallProductSpecifica productSpecifica = new MallProductSpecifica();
		productSpecifica.setProductId( proId );
		Map< String,Object > params = new HashMap< String,Object >();
		params.put( "nameId", nameId );
		params.put( "valueId", valueId );
		params.put( "proId", proId );
		MallProductSpecifica proSpec = selectByNameValueId( params );
		if ( CommonUtil.isNotEmpty( proSpec ) ) {
		    if ( CommonUtil.isNotEmpty( proSpec.getId() ) ) {
			productSpecifica.setId( proSpec.getId() );
		    }
		}
		productSpecifica.setSort( i );
		productSpecifica.setSpecificaName( specName );
		productSpecifica.setSpecificaNameId( nameId );
		productSpecifica.setSpecificaValue( specValue );
		productSpecifica.setSpecificaValueId( valueId );
		productSpecifica.setErpSpecnameId( erpNameId );
		productSpecifica.setErpSpecvalueId( erpValueId );
		if ( CommonUtil.isNotEmpty( productSpecifica.getId() ) ) {
		    mallProductSpecificaDAO.updateById( productSpecifica );
		} else {
		    mallProductSpecificaDAO.insert( productSpecifica );//同步商品规格
		}
		if ( CommonUtil.isNotEmpty( specIds ) ) {
		    specIds += ",";
		}
		specIds += productSpecifica.getId();
	    }
	}
	if ( code == 1 ) {
	    return specIds;
	}
	return null;
    }

    @Override
    public MallProductSpecifica selectByNameValueId( Map< String,Object > params ) {
	Wrapper< MallProductSpecifica > productSpecificaWrapper = new EntityWrapper<>();
	productSpecificaWrapper.where( "is_delete = 0" );
	if ( CommonUtil.isNotEmpty( params.get( "nameId" ) ) ) {
	    productSpecificaWrapper.where( "specifica_name_id = {0}", params.get( "nameId" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "valueId" ) ) ) {
	    productSpecificaWrapper.where( "specifica_value_id = {0}", params.get( "valueId" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "proId" ) ) ) {
	    productSpecificaWrapper.where( "product_id = {0}", params.get( "proId" ) );
	}
	List< MallProductSpecifica > productSpecificaList = mallProductSpecificaDAO.selectList( productSpecificaWrapper );
	if ( CommonUtil.isNotEmpty( productSpecificaList ) && productSpecificaList.size() > 0 ) {
	    return productSpecificaList.get( 0 );
	}
	return null;
    }

    @Override
    public List< MallProductSpecifica > selectByProductId( Integer id ) {
	Wrapper< MallProductSpecifica > productSpecificaWrapper = new EntityWrapper<>();
	productSpecificaWrapper.where( "is_delete = 0 and product_id = {0}", id ).orderBy( "sort, id", true );
	return mallProductSpecificaDAO.selectList( productSpecificaWrapper );
    }

    @Override
    public List< MallProductSpecifica > selectByValueIds( int productIds, String[] valueIds ) {
	Wrapper< MallProductSpecifica > productSpecificaWrapper = new EntityWrapper<>();
	productSpecificaWrapper.where( "is_delete = 0 and product_id = {0}", productIds ).in( "specifica_value_id", valueIds ).orderBy( "sort", true );
	return mallProductSpecificaDAO.selectList( productSpecificaWrapper );
    }

}
