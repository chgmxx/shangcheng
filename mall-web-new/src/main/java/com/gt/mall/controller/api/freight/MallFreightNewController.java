package com.gt.mall.controller.api.freight;

import com.gt.api.bean.session.BusUser;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.DictBean;
import com.gt.mall.constant.Constants;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.freight.MallFreight;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.user.BusUserService;
import com.gt.mall.service.inter.user.DictService;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.service.web.freight.MallFreightDetailService;
import com.gt.mall.service.web.freight.MallFreightService;
import com.gt.mall.service.web.store.MallStoreService;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PageUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物流管理 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Api( value = "mallFreight", description = "物流管理", produces = MediaType.APPLICATION_JSON_VALUE )
@Controller
@RequestMapping( "/mallFreight/E9lM9uM4ct" )
public class MallFreightNewController extends BaseController {

    @Autowired
    private MallStoreService         storeService;
    @Autowired
    private MallFreightService       freightService;
    @Autowired
    private MallFreightDetailService freightDetailService;
    @Autowired
    private MallTakeTheirService     takeTheirService;
    @Autowired
    private MallPaySetService        paySetService;
    @Autowired
    private DictService              dictService;
    @Autowired
    private WxShopService            wxShopService;
    @Autowired
    private BusUserService           busUserService;
    @Autowired
    private MallPaySetService        mallPaySetService;

    @ApiOperation( value = "物流列表(分页)", notes = "物流列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @RequestMapping( value = "/list", method = RequestMethod.POST )
    public ServerResponse list( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
        Map< String,Object > result = new HashMap<>();
        try {
            BusUser user = MallSessionUtils.getLoginUser( request );
            Map< String,Object > params = new HashMap<>();
            params.put( "curPage", curPage );
            List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user, request );// 查询登陆人拥有的店铺
            if ( shoplist != null && shoplist.size() > 0 ) {
                params.put( "shoplist", shoplist );
                PageUtil freightPage = freightService.selectFreightByShopId( shoplist, params );
                result.put( "page", freightPage );
            }
            result.put( "videourl", Constants.VIDEO_URL + 80 );
        } catch ( Exception e ) {
            logger.error( "物流列表异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "物流列表异常" );
        }
        return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取物流信息
     */
    @ApiOperation( value = "获取物流信息", notes = "获取物流信息" )
    @ResponseBody
    @RequestMapping( value = "/freightInfo", method = RequestMethod.POST )
    public ServerResponse freightInfo( HttpServletRequest request, HttpServletResponse response,
        @ApiParam( name = "id", value = "物流ID", required = true ) @RequestParam Integer id ) {
        MallFreight freight = null;
        try {
            freight = freightService.selectFreightById( id );
        } catch ( Exception e ) {
            logger.error( "获取物流信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取物流信息异常" );
        }
        return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), freight );
    }

    /**
     * 根据店铺ID获取物流信息
     */
    @ApiOperation( value = "根据店铺ID获取物流信息", notes = "根据店铺ID获取物流信息" )
    @ResponseBody
    @RequestMapping( value = "/getFreightByShopId", method = RequestMethod.POST )
    public ServerResponse getFreightByShopId( HttpServletRequest request, HttpServletResponse response,
        @ApiParam( name = "shopId", value = "店铺ID", required = true ) @RequestParam Integer shopId ) {
        List< MallFreight > freight = null;
        try {
            freight = freightService.selectFreightByShopId( shopId );
        } catch ( Exception e ) {
            logger.error( "根据店铺ID获取物流信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "根据店铺ID获取物流信息异常" );
        }
        return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), freight );
    }

    /**
     * 获取物流快递公司
     */
    @ApiOperation( value = "获取物流快递公司", notes = "获取物流快递公司" )
    @ResponseBody
    @RequestMapping( value = "/expressList", method = RequestMethod.POST )
    public ServerResponse expressList( HttpServletRequest request, HttpServletResponse response ) {
        List< DictBean > list = null;
        try {
            //查询物流公司
            list = dictService.getDict( "1092" );
        /*for ( Map map2 : list ) {
		map.put( map2.get( "item_key" ).toString(), map2.get( "item_value" ) );
	    }*/
        } catch ( Exception e ) {
            logger.error( "获取物流快递公司息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取物流快递公司异常" );
        }
        return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), list );
    }

    /**
     * 获取省级区域列表
     */
/*    @ApiOperation( value = "获取省级区域列表", notes = "获取省级区域列表" )
    @ResponseBody
    @RequestMapping( value = "/provinceList", method = RequestMethod.POST )*/
    /*public ServerResponse provinceList( HttpServletRequest request, HttpServletResponse response ) {
	List< Map > provinceList = new ArrayList<>();
	try {
	    provinceList = wxShopService.queryCityByLevel( 2 );
	    request.setAttribute( "provinceList", provinceList );

	} catch ( Exception e ) {
	    logger.error( "获取省级区域列表异常：" + e.getMessage() );
	    e.printStackTrace();
	    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取省级区域列表异常" );
	}
	return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), provinceList );
    }*/

    /**
     * 保存或修改物流信息
     */
    @ApiOperation( value = "保存物流信息", notes = "保存物流信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存物流信息", op_function = "2" )
    @RequestMapping( value = "/save", method = RequestMethod.POST )
    public ServerResponse saveOrUpdate( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
        try {
            BusUser user = MallSessionUtils.getLoginUser( request );

            boolean flag = freightService.newEditFreight( params, user.getId() );
            if ( !flag ) {
                return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存物流信息异常" );
            }
        } catch ( BusinessException e ) {
            logger.error( "保存物流信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
        } catch ( Exception e ) {
            logger.error( "保存物流信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
        }
        return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除物流信息
     */
    @ApiOperation( value = "删除物流", notes = "删除物流" )
    @ResponseBody
    @SysLogAnnotation( description = "删除物流信息", op_function = "4" )
    @RequestMapping( value = "/delete", method = RequestMethod.POST )
    public ServerResponse delete( HttpServletRequest request, HttpServletResponse response,
        @ApiParam( name = "ids", value = "物流ID集合,用逗号隔开", required = true ) @RequestParam String ids ) throws IOException {
        try {
            BusUser user = MallSessionUtils.getLoginUser( request );
            String id[] = ids.toString().split( "," );

            Map< String,Object > params = new HashMap<>();
            params.put( "ids", id );
            // 删除物流信息
            boolean flag = freightService.deleteFreight( params );
            if ( !flag ) {
                return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除物流信息异常" );
            }
        } catch ( BusinessException e ) {
            logger.error( "删除物流信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
        } catch ( Exception e ) {
            logger.error( "删除物流信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除物流异常" );
        }
        return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /***************************************上门自提*************************************************/

    @ApiOperation( value = "上门自提列表(分页)", notes = "上门自提列表(分页)" )
    @ResponseBody
    @ApiImplicitParams( @ApiImplicitParam( name = "curPage", value = "页数", paramType = "query", required = false, dataType = "int" ) )
    @RequestMapping( value = "/take/list", method = RequestMethod.POST )
    public ServerResponse takeList( HttpServletRequest request, HttpServletResponse response, Integer curPage ) {
        Map< String,Object > result = new HashMap<>();
        try {
            BusUser user = MallSessionUtils.getLoginUser( request );
            MallPaySet paySet = new MallPaySet();
            paySet.setUserId( user.getId() );
            MallPaySet set = paySetService.selectByUserId( paySet );
            result.put( "isTakeTheir", set.getIsTakeTheir() );
            if ( set.getIsTakeTheir() == 1 ) {
                Map< String,Object > params = new HashMap<>();
                params.put( "userId", user.getId() );
                params.put( "curPage", curPage );
                PageUtil page = takeTheirService.selectByUserId( params );
                result.put( "page", page );
            }

        } catch ( Exception e ) {
            logger.error( "物流列表异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "物流列表异常" );
        }
        return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), result );
    }

    /**
     * 获取上门自提信息
     */
    @ApiOperation( value = "获取上门自提信息", notes = "获取上门自提信息" )
    @ResponseBody
    @RequestMapping( value = "/take/takeInfo", method = RequestMethod.POST )
    public ServerResponse takeInfo( HttpServletRequest request, HttpServletResponse response, @ApiParam( name = "id", value = "自提ID", required = true ) @RequestParam Integer id ) {
        MallTakeTheir takeTheir = null;
        try {
            BusUser user = MallSessionUtils.getLoginUser( request );
            Map< String,Object > params = new HashMap<>();
            params.put( "id", id );
            params.put( "userId", user.getId() );
            takeTheir = takeTheirService.selectById( params );
        } catch ( Exception e ) {
            logger.error( "获取物流信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "获取物流信息异常" );
        }
        return ServerResponse.createBySuccessCodeData( ResponseEnums.SUCCESS.getCode(), takeTheir );
    }

    /**
     * 保存或修改自提信息
     */
    @ApiOperation( value = "保存自提信息", notes = "保存自提信息" )
    @ResponseBody
    @SysLogAnnotation( description = "保存自提信息", op_function = "2" )
    @RequestMapping( value = "/take/save", method = RequestMethod.POST )
    public ServerResponse takeSave( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
        try {
            BusUser user = MallSessionUtils.getLoginUser( request );

            boolean flag = takeTheirService.newEditTake( params, user );
            if ( !flag ) {
                return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "保存自提信息异常" );
            }
        } catch ( BusinessException e ) {
            logger.error( "保存自提信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
        } catch ( Exception e ) {
            logger.error( "保存自提信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
        }
        return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 上门自提功能 开启/关闭
     */
    @ApiOperation( value = "上门自提开启/关闭", notes = "上门自提开启/关闭" )
    @ResponseBody
    @SysLogAnnotation( description = "上门自提开启/关闭", op_function = "2" )
    @RequestMapping( value = "/take/setTakeTheir", method = RequestMethod.POST )
    public ServerResponse fltakeSave( HttpServletRequest request, HttpServletResponse response,
        @ApiParam( name = "status", value = "状态 0关闭 1开启", required = true ) @RequestParam Integer status ) {
        try {
            BusUser user = MallSessionUtils.getLoginUser( request );
            MallPaySet querySet = new MallPaySet();
            querySet.setUserId( user.getId() );
            MallPaySet set = mallPaySetService.selectByUserId( querySet );
            if ( set != null ) {
                set.setIsTakeTheir( status );
                mallPaySetService.updateById( set );
            }
        } catch ( Exception e ) {
            logger.error( "上门自提开启/关闭异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), ResponseEnums.ERROR.getDesc() );
        }
        return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }

    /**
     * 删除自提信息
     */
    @ApiOperation( value = "删除自提", notes = "删除自提" )
    @ResponseBody
    @SysLogAnnotation( description = "删除自提信息", op_function = "4" )
    @RequestMapping( value = "/take/delete", method = RequestMethod.POST )
    public ServerResponse takeDelete( HttpServletRequest request, HttpServletResponse response,
        @ApiParam( name = "id", value = "自提ID", required = true ) @RequestParam String id ) {
        try {
            MallTakeTheir takeTheir = takeTheirService.selectById( id );
            if ( takeTheir != null ) {
                takeTheir.setIsDelete( 1 );
                boolean flag = takeTheirService.updateById( takeTheir );
                if ( !flag ) {
                    return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除自提信息异常" );
                }
            }
        } catch ( BusinessException e ) {
            logger.error( "删除自提信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( e.getCode(), e.getMessage() );
        } catch ( Exception e ) {
            logger.error( "删除自提信息异常：" + e.getMessage() );
            e.printStackTrace();
            return ServerResponse.createByErrorCodeMessage( ResponseEnums.ERROR.getCode(), "删除自提信息异常" );
        }
        return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc() );
    }
}
