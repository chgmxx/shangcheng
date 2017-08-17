package com.gt.mall.service.web.product.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallSpecificaValueDAO;
import com.gt.mall.entity.product.MallSpecificaValue;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.service.web.product.MallSpecificaValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户添加规格值 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSpecificaValueServiceImpl extends BaseServiceImpl< MallSpecificaValueDAO,MallSpecificaValue > implements MallSpecificaValueService {

    @Autowired
    private MallSpecificaValueDAO mallSpecificaValueDAO;

    @Override
    public MallSpecificaValue selectBySpecValue( Map< String,Object > params ) {
	if(CommonUtil.isEmpty( params )){
	    return null;
	}
	Wrapper<MallSpecificaValue> specificaValueWrapper = new EntityWrapper<>(  );
	if( CommonUtil.isNotEmpty( params.get( "erpValueId" ) )){
	    specificaValueWrapper.where( " erp_value_id = {0}",params.get( "erpValueId" ) );
	}
	if(CommonUtil.isNotEmpty( params.get( "value" ) )){
	    specificaValueWrapper.where( "spec_value = {0}",params.get( "value" ) );
	}
	if(CommonUtil.isNotEmpty( params.get( "userId" ) )){
	    specificaValueWrapper.where( "user_id = {0}",params.get( "userId" ) );
	}
	List<MallSpecificaValue> valueList = mallSpecificaValueDAO.selectList( specificaValueWrapper );
	if(CommonUtil.isNotEmpty( valueList ) && valueList.size() > 0){
	    return valueList.get( 0 );
	}
	return null;
    }
}
