package com.gt.mall.web.service.product.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallSpecificaDAO;
import com.gt.mall.entity.product.MallSpecifica;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.product.MallSpecificaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户添加规格 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSpecificaServiceImpl extends BaseServiceImpl< MallSpecificaDAO,MallSpecifica > implements MallSpecificaService {

    @Autowired
    private MallSpecificaDAO mallSpecificaDAO;

    @Override
    public MallSpecifica selectBySpecName( Map< String,Object > params ) {
        if(CommonUtil.isEmpty( params )){
            return null;
	}
	Wrapper< MallSpecifica > specificaWrapper = new EntityWrapper<>();
	if(CommonUtil.isNotEmpty( params.get( "name" ) )){
	    specificaWrapper.where( "spec_name = {0}",params.get( "name" ) );
	}
	if(CommonUtil.isNotEmpty( params.get( "userId" ) )){
	    specificaWrapper.where( "user_id = {0}",params.get( "userId" ) );
	}
	if ( CommonUtil.isNotEmpty( params.get( "erpNameId" ) ) ) {
	    specificaWrapper.where( "erp_name_id = {0}", params.get( "erpNameId" ) );
	}
	List<MallSpecifica> specificaList = mallSpecificaDAO.selectList( specificaWrapper );
	if(CommonUtil.isNotEmpty( specificaList ) && specificaList.size() > 0){
	    return specificaList.get( 0 );
	}
	return null;
    }

}
