package com.gt.mall.service.web.pifa.impl;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.pifa.MallPifaPriceDAO;
import com.gt.mall.entity.pifa.MallPifaPrice;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.service.web.pifa.MallPifaPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 批发价格表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPifaPriceServiceImpl extends BaseServiceImpl< MallPifaPriceDAO,MallPifaPrice > implements MallPifaPriceService {

    @Autowired
    private MallPifaPriceDAO mallPifaPriceDAO;

    @Override
    public void editPifaPrice( Map< String,Object > map, int pifaId, boolean flag ) {
	if ( flag ) {//已经更换了商品
	    //删除已经有的团购价
	    MallPifaPrice price = new MallPifaPrice();
	    price.setPifaId( pifaId );
	    price.setIsDelete( 1 );
	    mallPifaPriceDAO.updateByPifaId( price );
	}
	if ( CommonUtil.isNotEmpty( map.get( "specArr" ) ) ) {
	    List< MallPifaPrice > priceList = JSONArray.parseArray( map.get( "specArr" ).toString(), MallPifaPrice.class );
	    if ( priceList != null && priceList.size() > 0 ) {
		for ( MallPifaPrice price : priceList ) {
		    price.setPifaId( pifaId );
		    if ( CommonUtil.isEmpty( price.getId() ) ) {
			mallPifaPriceDAO.insert( price );
		    } else {
			mallPifaPriceDAO.updateById( price );
		    }
		    //		    String key = "hSeckill";
		    //		    String field = pifaId + "_" + price.getSpecificaIds();
		    //		    JedisUtil.map( key, field, price.getSeckillNum() + "" );
		}
	    }
	}

    }

    @Override
    public List< MallPifaPrice > selectPriceByGroupId( int groupId ) {
	return mallPifaPriceDAO.selectPriceByGroupId( groupId );
    }
}
