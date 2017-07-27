package com.gt.mall.web.service.product.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductDetailDAO;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.product.MallProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品详情表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallProductDetailServiceImpl extends BaseServiceImpl< MallProductDetailDAO,MallProductDetail > implements MallProductDetailService {

    @Autowired
    private MallProductDetailDAO mallProductDetailDAO;

    @Override
    public MallProductDetail selectByProductId( int productId ) {
	Wrapper< MallProductDetail > detailWrapper = new EntityWrapper<>();
	detailWrapper.where( "product_id = {0}", productId );
	List< MallProductDetail > detailList = mallProductDetailDAO.selectList( detailWrapper );
	if ( CommonUtil.isNotEmpty( detailList ) && detailList.size() > 0 ) {
	    return detailList.get( 0 );
	}
	return null;
    }
}
