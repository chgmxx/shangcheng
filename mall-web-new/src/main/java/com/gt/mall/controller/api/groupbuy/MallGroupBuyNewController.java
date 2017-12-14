package com.gt.mall.controller.api.groupbuy;

import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.constant.Constants;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.web.groupbuy.MallGroupBuyService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallProductSpecificaService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-09-27
 */
@Api( value = "mallGroupBuy", description = "团购管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallGroupBuy/E9lM9uM4ct" )
public class MallGroupBuyNewController extends BaseController {

    @Autowired
    private MallStoreService            storeService;
    @Autowired
    private MallGroupBuyService         groupBuyService;
    @Autowired
    private MallProductService          productService;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private BusUserService              busUserService;
    @Autowired
    private MallProductService          mallProductService;


    @ApiOperation( value = "团购列表(分页)", notes = "团购列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( { @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ),
		    @ApiImplicitParam( name = "type", value = "活动状态 -2已失效 1进行中 -1 未开始  2已结束", paramType = "query", required = false, dataType = "int" ) } )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage, Integer type ) {
	Map< String,Object > result = new HashMap<>();
	try {

	    BusUser user = MallSessionUtils.getLoginUser( request );
	    Map< String,Object > params = new HashMap<>();
	    params.put( "curPage", curPage );
	    params.put( "type", type );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
	    if ( shoplist != null && shoplist.size() > 0 ) {
		params.put( "shoplist", shoplist );
		PageUtil page = groupBuyService.selectGroupBuyByShopId( params, user.getId(), shoplist );
		result.put( "page", page );
	    }
	    result.put( "videourl", busUserService.getVoiceUrl( "81" ) );

	} catch ( Exception e ) {
	    logger.error( "获取团购列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取团购列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取团购信息
     */
    @ApiOperation( value = "获取团购信息", notes = "获取团购信息" )
    @ResponseBody
    @RequestMapping( value = "/groupBuyInfo", method = RequestMethod.POST )
    public ServerResponse groupBuyInfo( HttpServletRequest request, HttpServletResponse response,
		    @ApiParam( name = "id", value = "团购ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > groupMap = null;
	try {
	    // 根据团购id查询团购信息
	    groupMap = groupBuyService.selectGroupBuyById( id );
	} catch ( Exception e ) {
	    logger.error( "获取团购信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取团购信息异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), groupMap );
    }

    /**
     * 保存或修改团购信息
     */
    @ApiOperation( value = "保存团购信息", notes = "保存团购信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存团购信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    /*
	    * specArr:[{"groupPrice":"45","invenId":"777","specificaIds":"187","isJoinGroup":1},{"groupPrice":"76","invenId":"778","specificaIds":"188","isJoinGroup":1}]
	     "groupBuy" -> "{"productId":"265","id":"48","gName":"121","gStartTime":"2017-09-21 00:00:00","gEndTime":"2017-09-29 00:00:00","gPeopleNum":"1","gMaxBuyNum":"1","gPrice":"45","shopId":"29"}"
	    * */
	    int code = -1;// 编辑成功
	    Integer userId = MallSessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = groupBuyService.editGroupBuy( params, userId );// 编辑商品
	    }
	    if ( code == -2 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "正在进行团购的商品不能修改" );
	    } else if ( code <= 0 ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存团购失败" );
	    }

	} catch ( BusinessException e ) {
	    logger.error( "保存团购信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "保存团购信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 获取链接
     */
    @ApiOperation( value = "获取链接", notes = "获取链接" )
    @ResponseBody
    @RequestMapping( value = "/link", method = RequestMethod.POST )
    public ServerResponse link( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "商品ID", required = true ) @RequestParam Integer id ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    MallProduct product = mallProductService.selectById( id );
	    String url = PropertiesUtil.getHomeUrl() + "mallPage/" + product.getId() + "/" + product.getShopId() + "/79B4DE7C/phoneProduct.do";
	    result.put( "link", url );//链接

	} catch ( Exception e ) {
	    logger.error( "获取链接：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 删除团购信息
     */
    @ApiOperation( value = "删除团购信息", notes = "删除团购信息" )
    @ResponseBody
    @SysLogAnnotation( description = "删除团购信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "团购Id", required = true ) @RequestParam Integer id,
		    @ApiParam( name = "type", value = "类型 -1删除 -2失效", required = true ) @RequestParam Integer type ) {
	try {

	    MallGroupBuy groupBuy = new MallGroupBuy();
	    groupBuy.setId( id );
	    if ( type == -1 ) {// 删除
		groupBuy.setIsDelete( 1 );
	    } else if ( type == -2 ) {// 使失效团购
		groupBuy.setIsUse( -1 );
	    }
	    boolean flag = groupBuyService.deleteGroupBuy( groupBuy );
	    if ( !flag ) {
		return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), type == -1 ? "删除" : "失效" + "团购记录异常" );
	    }
	} catch ( BusinessException e ) {
	    logger.error( "删除团购信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
	} catch ( Exception e ) {
	    logger.error( "删除团购信息异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除团购信息异常" );
	}
	return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 根据店铺id查询商品
     */
    @ApiOperation( value = "根据店铺id查询商品(活动商品)", notes = "根据店铺id查询商品" )
    @ResponseBody
    @RequestMapping( value = "/getProductByGroup", method = RequestMethod.POST )
    public ServerResponse getProductByGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( user ) && CommonUtil.isNotEmpty( params ) ) {
		if ( CommonUtil.isEmpty( params.get( "shopId" ) ) ) {
		    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
		    if ( shoplist != null && shoplist.size() > 0 ) {
			params.put( "shoplist", shoplist );
		    }
		}
		int userPId = MallSessionUtils.getAdminUserId( user.getId(), request );//通过用户名查询主账号id
		long isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有
		params.put( "isJxc", isJxc );

		params.put( "userId", user.getId() );
		PageUtil page = groupBuyService.selectProByGroup( params );
		if ( page != null ) {
		    result.put( "page", page );
		}
		request.setAttribute( "map", params );
	    }
	} catch ( Exception e ) {
	    logger.error( "根据店铺id查询商品异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "根据店铺id查询商品异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 根据商品id获取商品的规格和库存
     */
    @ApiOperation( value = "根据商品id获取商品的规格和库存", notes = "根据商品id获取商品的规格和库存" )
    @ResponseBody
    @RequestMapping( value = "/getSpecificaByProId", method = RequestMethod.POST )
    public ServerResponse getSpecificaByProId( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	try {
	    BusUser user = MallSessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( user ) && CommonUtil.isNotEmpty( params ) ) {
		Integer proId = CommonUtil.toInteger( params.get( "proId" ) );
		int userPId = MallSessionUtils.getAdminUserId( user.getId(), request );//通过用户名查询主账号id
		long isJxc = busUserService.getIsErpCount( 8, userPId );//判断商家是否有进销存 0没有 1有

		int isSPec = 1;
		if ( CommonUtil.isNotEmpty( params.get( "isSpec" ) ) ) {
		    isSPec = CommonUtil.toInteger( params.get( "isSpec" ) );
		}
		MallProduct product = productService.selectByPrimaryKey( proId );
		List< MallProductInventory > invenList = productInventoryService.selectInvenByProductId( proId );
		int type = 0;
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    type = CommonUtil.toInteger( params.get( "type" ) );
		}

		if ( isJxc == 1 && CommonUtil.isNotEmpty( product.getErpProId() ) ) {//拥有进销存直查寻进销存的库存
		    boolean isSelect = true;
		    if ( type == 3 ) {
			int seckillId = 0;
			if ( CommonUtil.isNotEmpty( params.get( "seckillId" ) ) ) {
			    seckillId = CommonUtil.toInteger( params.get( "seckillId" ) );
			}
			if ( seckillId > 0 ) {
			    String key = Constants.REDIS_KEY + "hSeckill";
			    String numStr = JedisUtil.maoget( key, seckillId + "" );
			    if ( CommonUtil.isNotEmpty( numStr ) ) {
				isSelect = false;
				product.setProStockTotal( CommonUtil.toInteger( numStr ) );
			    }

			    if ( invenList != null && invenList.size() > 0 ) {
				//获取erp的商品库存
				for ( MallProductInventory inven : invenList ) {

				    String[] specIds = inven.getSpecificaIds().split( "," );
				    String valueIds = "";
				    List< MallProductSpecifica > specList = productSpecificaService.selectBySpecId( specIds );
				    if ( specList != null && specList.size() > 0 ) {
					for ( MallProductSpecifica spec : specList ) {
					    valueIds = spec.getSpecificaValueId() + ",";
					}
				    }
				    valueIds = valueIds.substring( 0, valueIds.length() - 1 );

				    String field = seckillId + "_" + valueIds;
				    numStr = JedisUtil.maoget( key, field );
				    if ( CommonUtil.isNotEmpty( numStr ) ) {
					isSelect = false;
					inven.setInvNum( CommonUtil.toInteger( numStr ) );
				    }
				}
			    }
			}
		    }
		    if ( isSelect ) {
			List< Map< String,Object > > specList = productService.getErpInvByProId( product.getErpProId(), product.getShopId() );
			int stockNum = 0;
			if ( invenList != null && invenList.size() > 0 && specList != null && specList.size() > 0 ) {
			    //获取erp的商品库存
			    for ( MallProductInventory inven : invenList ) {
				if ( CommonUtil.isNotEmpty( inven.getErpInvId() ) ) {
				    String invIds = inven.getErpInvId().toString();
				    int invNum = productService.getInvNumsBySpecs( specList, invIds );
				    stockNum += invNum;
				    inven.setInvNum( invNum );
				}
			    }
			} else {
			    if ( specList != null && specList.size() > 0 ) {
				int invNum = productService.getInvNumsBySpecs( specList, product.getErpInvId().toString() );
				product.setProStockTotal( invNum );
			    }
			}
			if ( stockNum > 0 && stockNum != product.getProStockTotal() ) {
			    product.setProStockTotal( stockNum );
			}
		    }
		}
		if ( isSPec == 1 ) {
		    result.put( "list", invenList );
		} else {
		    result.put( "product", product );
		}
	    }
	} catch ( Exception e ) {
	    logger.error( "根据商品id获取商品的规格和库存异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "根据商品id获取商品的规格和库存异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

}
