package com.gt.mall.controller.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.utils.CommonUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品相关接口
 * User : yangqian
 * Date : 2017/9/21 0021
 * Time : 15:30
 */
@Controller
@RequestMapping( "/mallProduct/mallAPI" )
public class MallProductApiController {

    private static Logger logger = LoggerFactory.getLogger( MallProductApiController.class );

    @Autowired
    private MallProductService          mallProductService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private MallProductInventoryService productInventoryService;

    @ApiOperation( value = "待审核商品的接口", notes = "获取所有商家待审核的商品" )
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @ResponseBody
    @RequestMapping( value = "/index", method = RequestMethod.GET )
    public ServerResponse index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {

	} catch ( Exception e ) {
	    logger.error( "获取待审核商品的异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取待审核商品的异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }


    @ApiOperation( value = "修改商品库存(秒杀订单)", notes = "MQ调用-修改商品库存(秒杀订单)" )
    @ResponseBody
    @RequestMapping( value = "/upProInvNumBySeckill", method = RequestMethod.POST )
    public ServerResponse upProInvNumBySeckill( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	//params  四个参数 (db 数据源, productId , productNum, proSpecificas)
	Map< String,Object > result = new HashMap<>();
	try {
	    String dbName = CommonUtil.toString( params.get( "db" ) );
	    String proSpecificas = "";
	    String proId = params.get( "productId" ).toString();
	    Integer productNum = 0;
	    if ( params.get( "productNum" ) != null ) {
		productNum = Integer.valueOf( params.get( "productNum" ).toString() );
	    }
	    if ( params.get( "proSpecificas" ) != null ) {
		proSpecificas = params.get( "proSpecificas" ).toString();
	    }
	    //获取商品的库存和销售量
	   /* String proSql = "select pro_stock_total,pro_sale_total,is_specifica from t_mall_product where id=" + proId;*/
	    Wrapper groupWrapper = new EntityWrapper();
	    groupWrapper.setSqlSelect( "pro_stock_total,pro_sale_total,is_specifica" );
	    groupWrapper.where( "id = {0}", proId );
	   /* mallProductService.selectMap( groupWrapper );*/
	    Map< String,Object > proMap = new HashMap< String,Object >();
	    //TODO 多数据源
	    /*if(dbName.equals("df")){//多粉
		proMap = daoUtil.queryForMap(proSql);
	    }else if(dbName.equals("cs")){
		proMap = daoUtilCS.queryForMap(proSql);
	    }else{//翼粉
		proMap = daoUtilYF.queryForMap(proSql);
	    }*/
	    if ( proMap != null ) {
		int invNum = Integer.parseInt( proMap.get( "pro_stock_total" ).toString() );//库存
		int saleNum = 0;//销量
		int isSpec = 0;//是否有规格
		if ( CommonUtil.isNotEmpty( proMap.get( "pro_sale_total" ) ) ) {
		    saleNum = Integer.parseInt( proMap.get( "pro_sale_total" ).toString() );
		}
		if ( CommonUtil.isNotEmpty( proMap.get( "is_specifica" ) ) ) {
		    isSpec = Integer.parseInt( proMap.get( "is_specifica" ).toString() );
		}
		invNum = invNum - productNum;
		saleNum = saleNum + productNum;
		/*String upSql = "update t_mall_product set pro_stock_total=" + invNum + ",pro_sale_total=" + saleNum + " where id=" + proId;*/
		MallProduct mallProduct = new MallProduct();
		mallProduct.setId( CommonUtil.toInteger( proId ) );
		mallProduct.setProStockTotal( invNum );
		mallProduct.setProSaleTotal( saleNum );
		/*mallProductService.updateById( mallProduct );*/
		//TODO 多数据源
		/*if ( dbName.equals( "df" ) ) {//多粉
		    daoUtil.execute( upSql );
		} else if ( dbName.equals( "cs" ) ) {
		    daoUtilCS.execute( upSql );
		} else {//翼粉
		    daoUtilYF.execute( upSql );
		}*/
		if ( isSpec == 1 ) {//该商品存在规格
		    String specIds = "";
		    //获取规格的库存和销售额
		    for ( String str : proSpecificas.split( "," ) ) {
			if ( str != null && !str.equals( "" ) ) {
			  /*  String sql = "select id from t_mall_product_specifica where product_id = " + proId + " and is_delete = 0 and specifica_value_id=" + str;*/
			    Wrapper< MallProductSpecifica > wrapper = new EntityWrapper<>();
			    wrapper.setSqlSelect( "id" );
			    wrapper.where( "product_id={0} and is_delete = 0 and specifica_value_id= {1}", proId, str );
			   /* productSpecificaService.selectMaps( wrapper );*/

			    List< Map< String,Object > > specList = new ArrayList< Map< String,Object > >();
			    //TODO 多数据源
			    /*if ( dbName.equals( "df" ) ) {//多粉
				specList = daoUtil.queryForList( sql );
			    } else if ( dbName.equals( "cs" ) ) {
				specList = daoUtilCS.queryForList( sql );
			    } else {//翼粉
				specList = daoUtilYF.queryForList( sql );
			    }*/
			    if ( specList != null && specList.size() > 0 ) {
				for ( Map< String,Object > map : specList ) {
				    if ( !specIds.equals( "" ) ) {
					specIds += ",";
				    }
				    specIds += map.get( "id" ).toString();
				}
			    }
			}
		    }
		    /*proSql = "select id,inv_num,inv_sale_num from t_mall_product_inventory where product_id = " + proId + " and is_delete = 0 and specifica_ids = '" + specIds + "'";*/
		    Wrapper proWrapper = new EntityWrapper();
		    proWrapper.setSqlSelect( "id,inv_num,inv_sale_num " );
		    proWrapper.where( "product_id = {0} and is_delete = 0 and specifica_ids = {1}", proId, specIds );
		 /*   Map< String,Object > invMap= productInventoryService.selectMap( proWrapper );*/

		    Map< String,Object > invMap = new HashMap< String,Object >();
		    //TODO 多数据源
		    /*if ( dbName.equals( "df" ) ) {//多粉
			invMap = daoUtil.queryForMap( proSql );
		    } else if ( dbName.equals( "cs" ) ) {
			invMap = daoUtilCS.queryForMap( proSql );
		    } else {//翼粉
			invMap = daoUtilYF.queryForMap( proSql );
		    }*/
		    if ( invMap != null ) {
			int invStockNum = 0;//库存
			if ( CommonUtil.isNotEmpty( invMap.get( "inv_num" ) ) ) {
			    invStockNum = CommonUtil.toInteger( invMap.get( "inv_num" ).toString() );
			}
			int invSaleNum = 0;//销量
			if ( CommonUtil.isNotEmpty( invMap.get( "inv_sale_num" ) ) ) {
			    invSaleNum = CommonUtil.toInteger( invMap.get( "inv_sale_num" ).toString() );
			}
			int invId = CommonUtil.toInteger( invMap.get( "id" ) );
			invStockNum = invStockNum - productNum;
			invSaleNum = invSaleNum + productNum;
			/*upSql = "update t_mall_product_inventory set inv_num=" + invStockNum + ",inv_sale_num=" + invSaleNum + " where id=" + invId;*/
			MallProductInventory inventory = new MallProductInventory();
			inventory.setId( invId );
			inventory.setInvNum( invStockNum );
			inventory.setInvSaleNum( invSaleNum );
			/*productInventoryService.updateById( inventory );*/
			//TODO 多数据源
			/*if ( dbName.equals( "df" ) ) {//多粉
			    daoUtil.execute( upSql );
			} else if ( dbName.equals( "cs" ) ) {
			    daoUtilCS.execute( upSql );
			} else {//翼粉
			    daoUtilYF.execute( upSql );
			}*/
		    }
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "修改商品库存(秒杀订单)异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "修改商品库存(秒杀订单)异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }
}
