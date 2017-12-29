package com.gt.mall.service.web.product.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductParamDAO;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductParam;
import com.gt.mall.service.web.product.MallProductParamService;
import com.gt.mall.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品参数表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductParamServiceImpl extends BaseServiceImpl< MallProductParamDAO,MallProductParam > implements MallProductParamService {

    @Autowired
    private MallProductParamDAO mallProductParamDAO;

    @Override
    public void saveOrUpdateBatch( Object obj, int proId, Map< String,Object > defaultMap, boolean isUpdate ) {
	List< MallProductParam > paramList = JSONArray.parseArray( obj.toString(), MallProductParam.class );
	if ( paramList != null && paramList.size() > 0 ) {
	    for ( MallProductParam param : paramList ) {
		if ( param != null ) {
		    param.setProductId( proId );

		    String str = param.getParamsNameId() + "_" + param.getParamsValueId();
		    boolean flag = true;
		    if ( defaultMap != null ) {
			if ( CommonUtil.isNotEmpty( defaultMap.get( str ) ) ) {
			    String idStr = defaultMap.get( str ).toString();
			    if ( CommonUtil.isInteger( idStr ) ) {
				param.setId( CommonUtil.toInteger( idStr ) );
				defaultMap.remove( str );
				flag = false;
			    }
			}
		    }
		    if ( isUpdate ) {//没有参加团购的商品才可以修改商品规格
			if ( flag ) {
			    mallProductParamDAO.insert( param );// 添加商品规格
			} else {
			    mallProductParamDAO.updateById( param );// 修改商品规格
			}
		    }
		}
	    }
	    if ( defaultMap != null && isUpdate ) {//没有参加团购的商品才可以修改商品规格
		Iterator it = defaultMap.entrySet().iterator();
		while ( it.hasNext() ) {
		    Map.Entry< String,Integer > entry = (Map.Entry< String,Integer >) it.next();
		    MallProductParam params = new MallProductParam();
		    params.setId( CommonUtil.toInteger( entry.getValue() ) );
		    params.setIsDelete( 1 );
		    // 逻辑删除规格
		    mallProductParamDAO.updateById( params );
		}
	    }
	}
    }

    @Override
    public void newSaveOrUpdateBatch( Object obj, int proId, boolean isUpdate ) {

	Wrapper< MallProductParam > paramWrapper = new EntityWrapper<>();
	paramWrapper.where( " product_id = {0} and is_delete = 0", proId );
	List< MallProductParam > defaultList = mallProductParamDAO.selectList( paramWrapper );

	List< MallProductParam > paramList = JSONArray.parseArray( obj.toString(), MallProductParam.class );
	if ( paramList != null && paramList.size() > 0 ) {
	    for ( MallProductParam param : paramList ) {
		if ( param != null ) {
		    param.setProductId( proId );
		    String str = param.getParamsNameId() + "_" + param.getParamsValueId();
		    if ( defaultList != null ) {
			for ( MallProductParam param1 : defaultList ) {
			    String defaultStr = param1.getParamsNameId() + "_" + param1.getParamsValueId();
			    if ( str.equals( defaultStr ) ) {
				param.setId( param1.getId() );
				defaultList.remove( param1 );
				break;
			    }
			}
		    }
		    if ( isUpdate ) {//没有参加团购的商品才可以修改商品规格
			if ( CommonUtil.isEmpty( param.getId() ) ) {
			    mallProductParamDAO.insert( param );// 添加商品规格
			} else {
			    mallProductParamDAO.updateById( param );// 修改商品规格
			}
		    }
		}
	    }
	    if ( defaultList != null && isUpdate ) {//没有参加团购的商品才可以修改商品规格
		for ( MallProductParam param : defaultList ) {
		    param.setIsDelete( 1 );
		    mallProductParamDAO.updateById( param );
		}
	    }
	}
    }

    @Override
    public List< MallProductParam > getParamByProductId( Integer proId ) {
	Wrapper< MallProductParam > paramWrapper = new EntityWrapper<>();
	paramWrapper.where( " product_id = {0} and is_delete = 0", proId ).orderBy( "sort, id", true );
	return mallProductParamDAO.selectList( paramWrapper );
    }

    @Override
    public void copyProductParam( List< MallProductParam > paramList, int proId, int shopId, int userId ) throws Exception {
	if ( paramList != null && paramList.size() > 0 ) {
	    for ( MallProductParam param : paramList ) {
		//判断店铺下面是否存在商品规格值
		/*MallSpecifica specifica = mallSpecificaMapper.selectByPrimaryKey(param.getParamsNameId());

		MallSpecificaValue value = mallSpecificaValueMapper.selectByPrimaryKey(param.getParamsValueId());

		if(specifica.getIsBackEnd().toString().equals("0") && specifica.getType().toString().equals("2")){
			if(!specifica.getShopId().toString().equals(String.valueOf(shopId))){
				specifica.setShopId(shopId);
				specifica.setUserId(userId);
				specifica.setCreateTime(new Date());
				specifica.setId(null);
				mallSpecificaMapper.insertSelective(specifica);//同步参数名称
				if(CommonUtil.isNotEmpty(specifica.getId())){
					param.setParamsNameId(specifica.getId());
				}
				if(value.getType().toString().equals("1")){
					value.setId(null);
					value.setUserId(userId);
					mallSpecificaValueMapper.insertSelective(value);//同步参数值
					if(CommonUtil.isNotEmpty(value.getId())){
						param.setParamsValueId(value.getId());
					}
				}
			}
		}*/
		param.setProductId( proId );
		param.setId( null );
		int count = mallProductParamDAO.insert( param );//同步商品参数
		if ( count <= 0 ) {
		    throw new Exception();
		}
	    }
	}
    }
}
