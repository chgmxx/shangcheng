package com.gt.mall.service.web.seckill.impl;

import com.alibaba.fastjson.JSONArray;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.product.MallProductInventoryDAO;
import com.gt.mall.dao.seckill.MallSeckillPriceDAO;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.seckill.MallSeckillPrice;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.JedisUtil;
import com.gt.mall.service.web.seckill.MallSeckillPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 秒杀价格表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSeckillPriceServiceImpl extends BaseServiceImpl< MallSeckillPriceDAO,MallSeckillPrice > implements MallSeckillPriceService {

    @Autowired
    private MallSeckillPriceDAO     mallSeckillPriceDAO;
    @Autowired
    private MallProductInventoryDAO mallProductInventoryDAO;

    @Override
    public List< Map< String,Object > > editSeckillPrice( Map< String,Object > map, int SeckillId, boolean flag, long isJxc ) {
	if ( flag ) {//已经更换了商品
	    //删除已经有的团购价
	    MallSeckillPrice price = new MallSeckillPrice();
	    price.setSeckillId( SeckillId );
	    price.setIsDelete( 1 );
	    mallSeckillPriceDAO.updateBySeckillId( price );
	}
	List< Map< String,Object > > productList = new ArrayList< Map< String,Object > >();
	if ( CommonUtil.isNotEmpty( map.get( "specArr" ) ) ) {
	    List< MallSeckillPrice > priceList = JSONArray.parseArray( map.get( "specArr" ).toString(), MallSeckillPrice.class );
	    if ( priceList != null && priceList.size() > 0 ) {
		for ( MallSeckillPrice mallSeckillPrice : priceList ) {
		    mallSeckillPrice.setSeckillId( SeckillId );
		    if ( CommonUtil.isEmpty( mallSeckillPrice.getId() ) ) {
			mallSeckillPriceDAO.insert( mallSeckillPrice );
		    } else {
			mallSeckillPriceDAO.updateById( mallSeckillPrice );
		    }
		    String key = "hSeckill";
		    String field = SeckillId + "_" + mallSeckillPrice.getSpecificaIds();
		    //					JedisUtil.mapdel(key, field);
		    JedisUtil.map( key, field, mallSeckillPrice.getSeckillNum() + "" );

		    if ( isJxc == 1 ) {
			//						mallSeckillPrice.getInvenId()
			int invenId = mallSeckillPrice.getInvenId();
			MallProductInventory inven = mallProductInventoryDAO.selectById( invenId );

			int erpInvId = inven.getErpInvId();

			Map< String,Object > productMap = new HashMap< String,Object >();
			productMap.put( "id", erpInvId );
			productMap.put( "amount", mallSeckillPrice.getSeckillNum() );//数量
			productMap.put( "price", mallSeckillPrice.getSeckillPrice() );
			productList.add( productMap );
		    }

		}
	    }
	}
	return productList;
    }

    @Override
    public List< MallSeckillPrice > selectPriceByGroupId( int groupId ) {
	return mallSeckillPriceDAO.selectPriceByGroupId( groupId );
    }

}
