package com.gt.mall.controller.groupbuy;

import com.gt.mall.annotation.AfterAnno;
import com.gt.mall.annotation.CommAnno;
import com.gt.mall.annotation.SysLogAnnotation;
import com.gt.mall.base.BaseController;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.entity.groupbuy.MallGroupBuy;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.util.*;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.basic.MallTakeTheirService;
import com.gt.mall.web.service.freight.MallFreightService;
import com.gt.mall.web.service.groupbuy.MallGroupBuyService;
import com.gt.mall.web.service.groupbuy.MallGroupJoinService;
import com.gt.mall.web.service.page.MallPageService;
import com.gt.mall.web.service.product.MallProductInventoryService;
import com.gt.mall.web.service.product.MallProductService;
import com.gt.mall.web.service.product.MallProductSpecificaService;
import com.gt.mall.web.service.store.MallStoreService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购表 前端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Controller
@RequestMapping( "mallGroupBuy" )
public class MallGroupBuyController extends BaseController {

    @Autowired
    private MallStoreService            storeService;
    @Autowired
    private MallGroupBuyService         groupBuyService;
    @Autowired
    private MallGroupJoinService        groupJoinService;
    @Autowired
    private MallPaySetService           paySetService;
    @Autowired
    private MallProductService          productService;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;
    @Autowired
    private MallPageService             pageService;

    @CommAnno( menu_url = "mGroupBuy/start.do" )
    @RequestMapping( "start" )
    public String start( HttpServletRequest request, HttpServletResponse response ) {
	request.setAttribute( "iframe_url", "mGroupBuy/index.do" );
	request.setAttribute( "title", "团购管理" );
	return "iframe";
    }

    /**
     * 团购管理列表页面
     *
     * @return
     */
    @RequestMapping( "index" )
    public String index( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    boolean isAdminFlag = true;//是管理员
	    if ( CommonUtil.isNotEmpty( user.getPid() ) && user.getPid() > 0 ) {
		isAdminFlag = storeService.isAdminUser( user.getId() );//查询子账户是否是管理员

		if ( !isAdminFlag ) {
		    request.setAttribute( "isNoAdminFlag", 1 );
		}
	    }
	    if ( isAdminFlag ) {
		List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
		if ( shoplist != null && shoplist.size() > 0 ) {
		    params.put( "shoplist", shoplist );
		    PageUtil page = groupBuyService.selectGroupBuyByShopId( params );
		    request.setAttribute( "page", page );
		    request.setAttribute( "shoplist", shoplist );
		}
		request.setAttribute( "type", params.get( "type" ) );
		request.setAttribute( "imgUrl", PropertiesUtil.getResourceUrl() );
		request.setAttribute( "path", PropertiesUtil.getHomeUrl() );
	    }
	    //TODO 需关连 视频方法
	    //	    request.setAttribute("videourl", course.urlquery("81"));
	} catch ( Exception e ) {
	    logger.error( "团购列表：" + e );
	    e.printStackTrace();
	}

	return "mall/groupBuy/groupbuy_index";
    }

    @CommAnno( menu_url = "mGroupBuy/start.do" )
    @RequestMapping( "edit" )
    public String edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	String url = "mGroupBuy/to_edit.do";
	if ( !CommonUtil.isEmpty( params.get( "id" ) ) ) {
	    url += "?id=" + params.get( "id" ).toString();
	}
	request.setAttribute( "iframe_url", url );
	request.setAttribute( "title", "团购管理-编辑团购" );
	return "iframe";
    }

    /**
     * 进入团购编辑页面
     *
     * @return
     */
    @RequestMapping( "to_edit" )
    public String to_edit( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
	    if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		Integer id = CommonUtil.toInteger( params.get( "id" ) );
		// 根据团购id查询团购信息
		Map< String,Object > groupMap = groupBuyService.selectGroupBuyById( id );
		if ( groupMap != null ) {
		    Object imageUrl = groupMap.get( "specImageUrl" );
		    if ( CommonUtil.isEmpty( imageUrl ) ) {
			imageUrl = groupMap.get( "imageUrl" );
		    }
		    groupMap.put( "imgUrl", imageUrl );
		}
		request.setAttribute( "groupBuy", groupMap );
	    }
	    request.setAttribute( "shoplist", shoplist );
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "进入团购编辑页面：" + e );
	    e.printStackTrace();
	}
	return "mall/groupBuy/groupbuy_edit";
    }

    /**
     * 编辑团购
     *
     * @Title: editGroup
     */
    @SysLogAnnotation( description = "团购管理-编辑团购", op_function = "2" )
    @RequestMapping( "edit_group_buy" )
    public void editGroupBuy( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入编辑辑团购controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = -1;// 编辑成功
	PrintWriter p = null;
	try {
	    p = response.getWriter();
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( CommonUtil.isNotEmpty( userId ) && CommonUtil.isNotEmpty( params ) ) {
		code = groupBuyService.editGroupBuy( params, userId );// 编辑商品
	    }
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "编辑团购：" + e.getMessage() );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	try {
	    obj.put( "code", code );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 删除团购
     *
     * @Title: editGroup
     */
    @SysLogAnnotation( description = "团购管理-删除团购", op_function = "4" )
    @RequestMapping( "group_remove" )
    public void removeGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入删除团购controller" );
	response.setCharacterEncoding( "utf-8" );
	int code = 1;// 删除成功
	PrintWriter p = null;
	try {
	    Integer userId = SessionUtils.getLoginUser( request ).getId();
	    if ( !CommonUtil.isEmpty( userId ) && !CommonUtil.isEmpty( params ) ) {
		int id = 0;
		if ( CommonUtil.isNotEmpty( params.get( "id" ) ) ) {
		    id = CommonUtil.toInteger( params.get( "id" ) );
		}
		MallGroupBuy groupBuy = new MallGroupBuy();
		groupBuy.setId( id );
		if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		    int type = CommonUtil.toInteger( params.get( "type" ) );
		    if ( type == -1 ) {// 删除
			groupBuy.setIsDelete( 1 );
		    } else if ( type == -2 ) {// 使失效团购
			groupBuy.setIsUse( -1 );
		    }
		}
		boolean flag = groupBuyService.deleteGroupBuy( groupBuy );
		if ( !flag ) {
		    code = -1;// 删除失败
		}
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    code = -1;
	    logger.error( "删除团购：" + e );
	    e.printStackTrace();
	}
	JSONObject obj = new JSONObject();
	try {
	    obj.put( "code", code );
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 根据店铺id查询商品
     *
     * @Title: editGroup
     */
    @RequestMapping( "getProductByGroup" )
    public String getProductByGroup( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入获取加入团购的商品controller" );
	response.setCharacterEncoding( "utf-8" );
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( user ) && CommonUtil.isNotEmpty( params ) ) {
		if ( CommonUtil.isEmpty( params.get( "shopId" ) ) ) {
		    List< Map< String,Object > > shoplist = storeService.findAllStoByUser( user );// 查询登陆人拥有的店铺
		    if ( shoplist != null && shoplist.size() > 0 ) {
			params.put( "shoplist", shoplist );
		    }
		}
		//TODO 主帐号 dictService.pidUserId
		//		int userPId = dictService.pidUserId(user.getId());//通过用户名查询主账号id
		//TODO 有无进销存 erpLoginOrMenusService.isjxcCount
		long isJxc = 1;
		//			erpLoginOrMenusService.isjxcCount("8", userPId);//判断商家是否有进销存 0没有 1有

		params.put( "isJxc", isJxc );
		params.put( "userId", user.getId() );
		PageUtil page = groupBuyService.selectProByGroup( params );
		if ( page != null ) {
		    request.setAttribute( "page", page );
		}
		request.setAttribute( "map", params );
	    }
	    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
	} catch ( Exception e ) {
	    logger.error( "团购编辑-获取商品：" + e );
	    e.printStackTrace();
	}
	return "mall/groupBuy/choosePro";
    }

    /**
     * 根据商品id获取商品的规格和库存
     *
     * @Title: editGroup
     */
    @RequestMapping( "getSpecificaByProId" )
    public void getSpecificaByProId( HttpServletRequest request, HttpServletResponse response, @RequestParam Map< String,Object > params ) {
	logger.info( "进入获取规格和库存的controller" );
	response.setCharacterEncoding( "utf-8" );
	JSONObject obj = new JSONObject();
	PrintWriter p = null;
	try {
	    BusUser user = SessionUtils.getLoginUser( request );
	    if ( CommonUtil.isNotEmpty( user ) && CommonUtil.isNotEmpty( params ) ) {
		Integer proId = CommonUtil.toInteger( params.get( "proId" ) );
		//TODO 主帐号 dictService.pidUserId
		//		int userPId = dictService.pidUserId(user.getId());//通过用户名查询主账号id
		//TODO 有无进销存 erpLoginOrMenusService.isjxcCount
		long isJxc = 1;
		//			erpLoginOrMenusService.isjxcCount("8", userPId);//判断商家是否有进销存 0没有 1有

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

		if ( isJxc == 1 && CommonUtil.isNotEmpty( product.getErpProId() ) ) {//拥有进销存直接查寻进销存的库存
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
				    //specificaService.getSpecificaValueById(params);
				    //mallProductService.selectProductSpec(params);
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
		    obj.put( "list", invenList );
		} else {
		    obj.put( "product", product );
		}
	    }
	    p = response.getWriter();
	} catch ( Exception e ) {
	    logger.error( "团购编辑-获取商品库存和规格：" + e );
	    e.printStackTrace();
	}
	try {
	    p.write( obj.toString() );
	    p.flush();
	    p.close();
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
    }

    /**
     * 获取店铺下所有的团购（手机）
     *
     * @param request
     * @param response
     * @param shopid
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/groupbuyall" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String groupbuyall( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) throws Exception {
	try {
	    int userid = 0;
	    Member member = SessionUtils.getLoginMember( request );
	    Map< String,Object > mapuser = pageService.selUser( shopid );//查询商家信息
	    if ( CommonUtil.isNotEmpty( mapuser ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	    }
	    Map publicUserid = pageService.getPublicByUserMap( mapuser );//查询公众号信息
	    if ( CommonUtil.isNotEmpty( publicUserid ) ) {
		userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	    }

	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    // TODO 登录地址
	    String returnUrl = "";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    boolean isShop = pageService.wxShopIsDelete( shopid );
	    if ( !isShop ) {
		return "mall/product/phone/shopdelect";
	    }

	    String http = PropertiesUtil.getResourceUrl();// 图片url链接前缀
	    List groupList = pageService.storeList( shopid, 1, 0 );// 获取分类
	    String pageid = "0";
	    List list1 = pageService.shoppage( shopid );// 获取商品主页
	    if ( list1.size() > 0 ) {
		Map map1 = (Map) list1.get( 0 );
		pageid = map1.get( "id" ).toString();
	    }
	    String type = "1";// 查询条件
	    String desc = "1";// 排序 默认倒序
	    if ( CommonUtil.isNotEmpty( params.get( "type" ) ) ) {
		type = params.get( "type" ).toString();
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "desc" ) ) ) {
		desc = params.get( "desc" ).toString();
	    }
	    params.put( "shopId", shopid );
	    List< Map< String,Object > > productList = groupBuyService.getGroupBuyAll( member, params );// 查询店铺下所有加入团购的商品
	    String mall_shopId=Constants.SESSION_KEY + "shopId";
	    if ( CommonUtil.isEmpty( request.getSession().getAttribute( mall_shopId ) ) ) {
		request.getSession().setAttribute( mall_shopId, shopid );
	    } else {
		if ( !request.getSession().getAttribute( mall_shopId).toString().equals( String.valueOf( shopid ) ) ) {
		    request.getSession().setAttribute( mall_shopId, shopid );
		}
	    }
	    //查询搜索标签，历史记录
	    pageService.getSearchLabel( request, shopid, member, params );

	    request.setAttribute( "shopId", shopid );
	    request.setAttribute( "pageid", pageid );
	    request.setAttribute( "type", type );
	    request.setAttribute( "desc", desc );
	    request.setAttribute( "groupList", groupList );
	    request.setAttribute( "imgHttp", http );
	    request.setAttribute( "productList", productList );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    request.setAttribute( "groupId", params.get( "groupId" ) );
	    request.setAttribute( "proName", params.get( "proName" ) );
	    Map< String,Object > footerMenuMap = paySetService.getFooterMenu( userid );//查询商城底部菜单
	    request.setAttribute( "footerMenuMap", footerMenuMap );
	    pageService.getCustomer( request, userid );
	} catch ( Exception e ) {
	    logger.error( "进入团购商品的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/groupBuy/phone/groupbuyall";
    }

    /**
     * 我要参团，团购详情（手机）
     *
     * @param request
     * @param response
     * @param id
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings( { "rawtypes" } )
    @RequestMapping( "{id}/{joinId}/79B4DE7C/groupBuyDetail" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String groupBuyDetail( HttpServletRequest request, HttpServletResponse response, @PathVariable int id, @PathVariable int joinId,
		    @RequestParam Map< String,Object > params ) throws Exception {
	try {
	    Member member = SessionUtils.getLoginMember( request );
	    int userid = 0;
	    if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
		userid = CommonUtil.toInteger( params.get( "uId" ) );
		request.setAttribute( "userid", userid );
	    }
	    Map< String,Object > publicMap = pageService.publicMapByUserId( userid );
	    /*if((CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
		    boolean isLogin = pageService.isLogin(member, userid, request);
		    if(!isLogin){
			    return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
		    }
	    }*/
	    Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	    loginMap.put( "uclogin", 1 );
	    // TODO 登录地址
	    String returnUrl = "";
//	    userLogin( request, response, userid, loginMap );
	    if ( CommonUtil.isNotEmpty( returnUrl ) ) {
		return returnUrl;
	    }
	    int isWx = 0;//不能微信支付
	    if ( ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) ) ) {
		isWx = 1;//可以微信支付
	    }
	    request.setAttribute( "isWxPay", isWx );
	    MallGroupBuy groupBuy = groupBuyService.selectById( id );

	    String buyerUserId = "";
	    if ( CommonUtil.isNotEmpty( params.get( "buyerUserId" ) ) ) {
		buyerUserId = CommonUtil.toString( params.get( "buyerUserId" ) );
	    } else if ( CommonUtil.isNotEmpty( member ) ) {
		buyerUserId = member.getId().toString();
	    }

	    String http = PropertiesUtil.getResourceUrl();// 图片url链接前缀

	    Map< String,Object > productMap = groupBuyService.getGroupBuyById( buyerUserId, id );// 通过团购id查询团购信息
	    int groupBuyId = 0;
	    if ( CommonUtil.isNotEmpty( productMap.get( "gBuyId" ) ) ) {
		groupBuyId = CommonUtil.toInteger( productMap.get( "gBuyId" ) );
	    }
	    if ( CommonUtil.isNotEmpty( productMap.get( "is_specifica" ) ) ) {//存在规格
		String isSpecifica = productMap.get( "is_specifica" ).toString();
		if ( isSpecifica.equals( "1" ) ) {
		    String inv_id = "";//存放默认库存id
		    if ( CommonUtil.isNotEmpty( productMap.get( "inv_id" ) ) ) {
			inv_id = productMap.get( "inv_id" ).toString();
		    }
		    Map< String,Object > guige = pageService.productSpecifications( CommonUtil.toInteger( productMap.get( "id" ) ), inv_id );//通过商品id和库存id查询商品规格信息
		    request.setAttribute( "guige", guige );
		}
	    }
	    if ( CommonUtil.isNotEmpty( productMap ) ) {
		String mall_shopId=Constants.SESSION_KEY + "shopId";
		if ( CommonUtil.isEmpty( request.getSession().getAttribute( mall_shopId) ) && CommonUtil.isNotEmpty( productMap.get( "shopId" ) ) ) {
		    request.getSession().setAttribute( mall_shopId, productMap.get( "shopId" ) );
		} else {
		    if ( !request.getSession().getAttribute( mall_shopId ).toString().equals( productMap.get( "shopId" ).toString() ) ) {
			request.getSession().setAttribute( mall_shopId, productMap.get( "shopId" ) );
		    }
		}
	    }
	    Map shopmessage = pageService.shopmessage( CommonUtil.toInteger( productMap.get( "shopId" ) ) );//查询店铺信息
	    request.setAttribute( "shopMap", shopmessage );
	    String discount = "1";//商品折扣
	    String is_member_discount = productMap.get( "is_member_discount" ).toString();//商品是否参加折扣
	    if ( ( is_member_discount == "1" || is_member_discount.equals( "1" ) ) && CommonUtil.isNotEmpty( member ) ) {
		//TODO 折扣 memberpayService.findCardType
		//		Map map = memberpayService.findCardType(member.getId());//查询个人折扣信息
		//		String result = map.get("result").toString();
		//		if(result=="true"||result.equals("true")){
		//		    discount = map.get("discount").toString();
		//		}
	    }
	    request.setAttribute( "discount", discount );//折扣价
	    Map< String,Object > maps = new HashMap< String,Object >();
	    maps.put( "groupBuyId", id );
	    maps.put( "joinId", joinId );
	    List< Map< String,Object > > joinList = groupJoinService.selectJoinByjoinId( maps );//查询参团人
	    int isMember = 0;
	    int orderId = 0;
	    int orderDetailId = 0;
	    if ( joinList != null && joinList.size() > 0 && CommonUtil.isNotEmpty( member ) ) {
		for ( Map< String,Object > map : joinList ) {
		    String joinUserId = map.get( "joinUserId" ).toString();
		    if ( joinUserId.equals( member.getId().toString() ) ) {
			isMember = 1;
			orderId = CommonUtil.toInteger( map.get( "orderId" ) );
			orderDetailId = CommonUtil.toInteger( map.get( "orderDetailId" ) );
			break;
		    }
		}
	    }
	    if ( groupBuyId > 0 && CommonUtil.isNotEmpty( buyerUserId ) ) {
		Map< String,Object > groupMap = new HashMap< String,Object >();
		groupMap.put( "groupBuyId", groupBuyId );

		groupMap.put( "joinUserId", buyerUserId );
		//查询用户参加团购的数量
		int groupBuyCount = groupJoinService.selectCountByBuyId( groupMap );
		request.setAttribute( "groupBuyCount", groupBuyCount );
	    }
	    params.put( "shopId", groupBuy.getShopId() );
	    List< Map< String,Object > > productList = groupBuyService.getGroupBuyAll( member, params );// 查询店铺下所有加入团购的商品
	    request.setAttribute( "productList", productList );
	    request.setAttribute( "isMember", isMember );
	    request.setAttribute( "orderId", orderId );
	    request.setAttribute( "orderDetailId", orderDetailId );
	    request.setAttribute( "imgHttp", http );
	    request.setAttribute( "productMap", productMap );
	    request.setAttribute( "joinList", joinList );
	    if ( CommonUtil.isNotEmpty( member ) ) {
		request.setAttribute( "memberId", member.getId() );
	    }
	    pageService.getCustomer( request, groupBuy.getUserId() );
	    if ( CommonUtil.judgeBrowser( request ) == 1 && CommonUtil.isNotEmpty( publicMap ) && CommonUtil.isNotEmpty( member ) ) {
		//TODO 公众号信息 morderService.getWpUser
		//		CommonUtil.getWxParams(morderService.getWpUser(member.getId()),request);
	    }
	} catch ( Exception e ) {
	    logger.error( "进入我要参团/团购详情的页面出错：" + e );
	    e.printStackTrace();
	}
	return "mall/groupBuy/phone/groupBuyDetail";
    }

    /**
     * 玩法详情（手机）
     */
    @SuppressWarnings( "rawtypes" )
    @RequestMapping( "{shopid}/79B4DE7C/playDetail" )
    @AfterAnno( style = "9", remark = "微商城访问记录" )
    public String playDetail( HttpServletRequest request, HttpServletResponse response, @PathVariable int shopid, @RequestParam Map< String,Object > params ) {
	Member member = SessionUtils.getLoginMember( request );
	int userid = 0;
	if ( CommonUtil.isNotEmpty( params.get( "uId" ) ) ) {
	    userid = CommonUtil.toInteger( params.get( "uId" ) );
	    request.setAttribute( "userid", userid );
	}
	/*Map<String, Object> publicMap = pageService.memberMap(userid);
	if(CommonUtil.isEmpty(member) && (CommonUtil.judgeBrowser(request) != 1 || CommonUtil.isEmpty(publicMap))){
		boolean isLogin = pageService.isLogin(member, userid, request);
		if(!isLogin){
			return "redirect:/phoneLoginController/"+userid+"/79B4DE7C/phonelogin.do?returnKey="+Constants.UCLOGINKEY;
		}
	}*/
	Map< String,Object > loginMap = pageService.saveRedisByUrl( member, userid, request );
	// TODO 登录地址
	String returnUrl = "";
//	userLogin( request, response, userid, loginMap );
	if ( CommonUtil.isNotEmpty( returnUrl ) ) {
	    return returnUrl;
	}
	Map< String,Object > mapuser = pageService.selUser( shopid );//查询商家信息
	if ( CommonUtil.isNotEmpty( mapuser ) ) {
	    userid = CommonUtil.toInteger( mapuser.get( "id" ) );
	}
	Map publicUserid = pageService.getPublicByUserMap( mapuser );//查询公众号信息
	if ( CommonUtil.isNotEmpty( publicUserid ) ) {
	    userid = CommonUtil.toInteger( mapuser.get( "bus_user_id" ) );
	}

	String mall_shopId=Constants.SESSION_KEY + "shopId";
	if ( CommonUtil.isEmpty( request.getSession().getAttribute( mall_shopId ) ) ) {
	    request.getSession().setAttribute( mall_shopId, shopid );
	} else {
	    if ( !request.getSession().getAttribute( mall_shopId ).toString().equals( String.valueOf( shopid ) ) ) {
		request.getSession().setAttribute( mall_shopId, shopid );
	    }
	}
	pageService.getCustomer( request, userid );
	return "mall/product/phone/playDetail";
    }

}
