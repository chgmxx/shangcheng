package com.gt.mall.service.web.presale.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.presale.MallPresaleDAO;
import com.gt.mall.dao.presale.MallPresaleDepositDAO;
import com.gt.mall.dao.presale.MallPresaleGiveDAO;
import com.gt.mall.dao.presale.MallPresaleRankDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.presale.*;
import com.gt.mall.entity.product.MallProductInventory;
import com.gt.mall.entity.product.MallProductSpecifica;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.param.phone.PhoneSearchProductDTO;
import com.gt.mall.result.phone.product.PhonePresaleProductDetailResult;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.presale.MallPresaleTimeService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.fenbiFlow.FenbiFlowRecord;
import com.gt.util.entity.param.fenbiFlow.FenbiSurplus;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import com.gt.util.entity.result.fenbi.FenBiCount;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 商品预售表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPresaleServiceImpl extends BaseServiceImpl< MallPresaleDAO,MallPresale > implements MallPresaleService {

    private Logger logger = Logger.getLogger( MallPresaleServiceImpl.class );

    @Autowired
    private MallPresaleDAO              mallPresaleDAO;
    @Autowired
    private MallPresaleDepositDAO       mallPresaleDepositDAO;
    @Autowired
    private MallPresaleTimeService      mallPresaleTimeService;
    @Autowired
    private MallPresaleGiveDAO          mallPresaleGiveDAO;
    @Autowired
    private MallPageService             mallPageService;
    @Autowired
    private MallPresaleRankDAO          mallPresaleRankDAO;
    @Autowired
    private MallProductInventoryService mallProductInventoryService;
    @Autowired
    private MallOrderDAO                mallOrderDAO;
    @Autowired
    private MallPaySetService           mallPaySetService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;
    @Autowired
    private FenBiFlowService            fenBiFlowService;
    @Autowired
    private MallSearchKeywordService    mallSearchKeywordService;
    @Autowired
    private MallProductService          mallProductService;

    /**
     * 通过店铺id来查询预售
     *
     * @Title: selectFreightByShopId
     */
    @Override
    public PageUtil selectPresaleByShopId( Map< String,Object > params, int userId, List< Map< String,Object > > shoplist ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallPresaleDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mPresale/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量
	if ( count > 0 ) {// 判断预售是否有数据
	    List< MallPresale > presaleList = mallPresaleDAO.selectByPage( params );
	    if ( presaleList != null ) {
		for ( MallPresale presale : presaleList ) {
		    int status = isJoinPresale( presale );
		    presale.setJoinId( status );
		    if ( shoplist != null && shoplist.size() > 0 ) {
			for ( Map< String,Object > shopMaps : shoplist ) {
			    int shop_id = CommonUtil.toInteger( shopMaps.get( "id" ) );
			    if ( presale.getShopId() == shop_id ) {
				presale.setShopName( shopMaps.get( "sto_name" ).toString() );
				break;
			    }
			}
		    }
		}
	    }
	    page.setSubList( presaleList );
	}

	return page;
    }

    private int isJoinPresale( MallPresale presale ) {
	int status = 0;
	if ( presale.getIsDeposit() == 1 ) {//需要交纳定金的预售，是否已经有人加入了预售
	    MallPresaleDeposit deposit = new MallPresaleDeposit();
	    deposit.setPresaleId( presale.getId() );
	    List< MallPresaleDeposit > depositList = mallPresaleDepositDAO.selectListByDeposit( deposit );
	    if ( depositList != null && depositList.size() > 0 ) {
		status = 1;
	    }
	} else {
	    //是否已经有人购买了预售的商品
	    Wrapper< MallOrder > orderWrapper = new EntityWrapper<>();
	    orderWrapper.where( "order_type = 5 and order_status > 1 and p_join_id ={0} and bus_user_id={1}", presale.getId(), presale.getUserId() );
	    List< MallOrder > mapList = mallOrderDAO.selectList( orderWrapper );
	    if ( mapList != null && mapList.size() > 0 ) {
		status = 1;
	    }
	}
	return status;
    }

    /**
     * 通过预售id查询预售信息
     */
    @Override
    public Map< String,Object > selectPresaleById( Integer id ) {
	Map< String,Object > map = mallPresaleDAO.selectByPresaleId( id );
	if ( map != null ) {
	    int preId = CommonUtil.toInteger( map.get( "id" ) );
	    List< MallPresaleTime > timeList = mallPresaleTimeService.getPresaleTimeByPreId( preId );
	    map.put( "timeList", timeList );
	    /*Date startTime = DateTimeKit.parse(map.get("sale_start_time").toString(), "yyyy-MM-dd HH:mm:ss");*/
	    /*Date endTime = DateTimeKit.parse(map.get("sale_end_time").toString(), "yyyy-MM-dd HH:mm:ss");
	    Date nowTime = DateTimeKit.parse(DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss");
	    map.put("times", (endTime.getTime() - nowTime.getTime())/1000);*/
	}
	return map;
    }

    /**
     * 编辑预售
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int editPresale( Map< String,Object > presaleMap, int userId ) {
	int code = -1;
	if ( CommonUtil.isNotEmpty( presaleMap.get( "presale" ) ) ) {
	    MallPresale presale = (MallPresale) JSONObject.toJavaObject( JSONObject.parseObject( presaleMap.get( "presale" ).toString() ), MallPresale.class );
	    // 判断选择的商品是否已经存在未开始和进行中的预售中
	    /*List<MallPresale> presaleList = mallPresaleDAO.selectDepositByProId(presale);
	    if (presaleList == null || presaleList.size() == 0) {*/
	    if ( CommonUtil.isNotEmpty( presale.getId() ) ) {
		// 判断本商品是否正在预售中
		MallPresale pre = mallPresaleDAO.selectDepositByIds( presale.getId() );

		int status = isJoinPresale( pre );
		if ( pre.getStatus() == 1 && status > 0 ) {// 正在进行预售的信息不能修改
		    code = -2;
		} else if ( pre.getIsUse().toString().equals( "-1" ) ) {
		    code = -3;
		} else {
		    code = mallPresaleDAO.updateById( presale );
		}
	    } else {
		presale.setUserId( userId );
		presale.setCreateTime( new Date() );
		code = mallPresaleDAO.insert( presale );
	    }
	    if ( CommonUtil.isNotEmpty( presale.getId() ) && code > 0 ) {
		code = 1;
		if ( CommonUtil.isNotEmpty( presaleMap.get( "presaleTimes" ) ) || CommonUtil.isNotEmpty( presaleMap.get( "delPresaleTimes" ) ) ) {
		    mallPresaleTimeService.insertUpdBatchTime( presaleMap, presale.getId() );
		}
		//loadPresaleInv(presaleMap, presale.getId(),presale);
		loadPresaleByJedis( presale );
	    }
	    /*} else {
		    code = 0;
	    }*/
	}
	return code;
    }

    /**
     * 编辑预售
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int newEditPresale( Map< String,Object > presaleMap, int userId ) {
	int code = -1;
	if ( CommonUtil.isNotEmpty( presaleMap.get( "presale" ) ) ) {
	    MallPresale presale = (MallPresale) JSONObject.toJavaObject( JSONObject.parseObject( presaleMap.get( "presale" ).toString() ), MallPresale.class );
	    // 判断选择的商品是否已经存在未开始和进行中的预售中
	    if ( CommonUtil.isNotEmpty( presale.getId() ) ) {
		// 判断本商品是否正在预售中
		MallPresale pre = mallPresaleDAO.selectDepositByIds( presale.getId() );

		int status = isJoinPresale( pre );
		if ( pre.getStatus() == 1 && status > 0 ) {// 正在进行预售的信息不能修改
		    code = -2;
		} else if ( pre.getIsUse().toString().equals( "-1" ) ) {
		    code = -3;
		} else {
		    code = mallPresaleDAO.updateById( presale );
		}
	    } else {
		presale.setUserId( userId );
		presale.setCreateTime( new Date() );
		code = mallPresaleDAO.insert( presale );
	    }
	    if ( CommonUtil.isNotEmpty( presale.getId() ) && code > 0 ) {
		code = 1;
		if ( CommonUtil.isNotEmpty( presaleMap.get( "presaleTimes" ) ) ) {
		    mallPresaleTimeService.newInsertUpdBatchTime( presaleMap, presale.getId() );
		}

		loadPresaleByJedis( presale );
	    }
	}
	return code;
    }

    /**
     * 删除预售
     */
    @Override
    public boolean deletePresale( MallPresale presale ) {
	int num = mallPresaleDAO.updateById( presale );
	return num > 0;
    }

    /**
     * 获取店铺下所有的预售
     */
    @Override
    public List< Map< String,Object > > getPresaleAll( Member member, Map< String,Object > maps ) {
	int shopid = 0;
	if ( CommonUtil.isNotEmpty( maps.get( "shopId" ) ) ) {
	    shopid = CommonUtil.toInteger( maps.get( "shopId" ) );
	}
	if ( CommonUtil.isNotEmpty( member ) ) {
	    mallPageService.saveOrUpdateKeyWord( maps, shopid, member.getId() );
	}

	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();// 存放店铺下的商品

	/*double discount = 1;// 商品折扣
	Map<String, Object> map = memberpayService.findCardType(Integer
			.valueOf(memberId));
	String result = map.get("result").toString();
	if (result == "true" || result.equals("true")) {
		discount = Double.parseDouble(map.get("discount").toString());
	}*/
	maps.put( "status", 1 );
	List< Map< String,Object > > productList = mallPresaleDAO.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		//				int id = Integer.valueOf(map2.get("preId").toString());
		String is_specifica = map2.get( "is_specifica" ).toString();// 是否有规格，1有规格，有规格取规格里面的值
		if ( is_specifica.equals( "1" ) ) {
		    map2.put( "price", map2.get( "inv_price" ) );
		    if ( CommonUtil.isEmpty( map2.get( "specifica_img_url" ) ) ) {
			map2.put( "image_url", map2.get( "image_url" ) );
		    } else {
			map2.put( "image_url", map2.get( "specifica_img_url" ) );
		    }
		} else {
		    map2.put( "price", map2.get( "pro_price" ) );
		    map2.put( "image_url", map2.get( "image_url" ) );
		}

		String startTimes = map2.get( "sale_start_time" ).toString();

		Date endTime = DateTimeKit.parse( map2.get( "sale_end_time" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date startTime = DateTimeKit.parse( startTimes, "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );

		long nowTimes = new Date().getTime();
		int isEnd = 1;
		int status = -1;
		if ( startTime.getTime() < nowTimes && endTime.getTime() <= nowTimes ) {//预售已经结束
		    isEnd = -1;
		}
		map2.put( "isEnd", isEnd );
		if ( startTime.getTime() > nowTimes && endTime.getTime() > nowTimes ) {//预售还未开始
		    status = 0;
		    map2.put( "startTimes", ( startTime.getTime() - nowTime.getTime() ) / 1000 );
		} else if ( startTime.getTime() <= nowTimes && nowTimes < endTime.getTime() ) {//预售正在进行
		    status = 1;
		}
		map2.put( "status", status );

		long times = ( endTime.getTime() - nowTime.getTime() ) / 1000;
		map2.put( "times", times );

		DecimalFormat df = new DecimalFormat( "######0.00" );
		map2.put( "old_price", df.format( CommonUtil.toDouble( map2.get( "pro_cost_price" ) ) ) );
		map2.put( "price", df.format( CommonUtil.toDouble( map2.get( "price" ) ) ) );

		String is_member_discount = map2.get( "is_member_discount" ).toString();// 商品是否参加折扣,1参加折扣
		if ( is_member_discount.equals( "1" ) ) {
		/*map2.put("price", Math.ceil((Double.parseDouble(map2.get(
				"price").toString()) * discount) * 100) / 100);*/
		}
		list.add( map2 );
	    }
	}
	return list;
    }

    /**
     * 根据商品id查询预售信息和预售价格
     */
    @SuppressWarnings( "unchecked" )
    @Override
    public MallPresale getPresaleByProId( Integer proId, Integer shopId, Integer presaleId ) {
	MallPresale presale = new MallPresale();
	if ( CommonUtil.isNotEmpty( presaleId ) && presaleId > 0 ) {
	    presale.setId( presaleId );
	}
	presale.setProductId( proId );
	presale.setShopId( shopId );
	presale = mallPresaleDAO.selectBuyByProductId( presale );
	if ( presale != null && CommonUtil.isNotEmpty( presale.getId() ) ) {

	    String key = "presale_num";
	    String field = presale.getId().toString();
	    if ( JedisUtil.hExists( key, field ) ) {
		String str = JedisUtil.maoget( key, field );
		if ( CommonUtil.isNotEmpty( str ) ) {
		    JSONObject obj = JSONObject.parseObject( str );
		    if ( CommonUtil.isNotEmpty( obj.get( "invNum" ) ) ) {
			presale.setInvNum( CommonUtil.toInteger( obj.get( "invNum" ) ) );
		    }
		    if ( CommonUtil.isNotEmpty( obj.get( "specArr" ) ) ) {
			List< Map< String,Object > > preSpecArr = (List< Map< String,Object > >) net.sf.json.JSONArray
					.toList( net.sf.json.JSONArray.fromObject( obj.get( "specArr" ) ), Map.class );
			if ( preSpecArr != null && preSpecArr.size() > 0 ) {
			    presale.setList( preSpecArr );
			}
		    }
		}
	    }

	    Date endTime = DateTimeKit.parse( presale.getSaleEndTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date startTime = DateTimeKit.parse( presale.getSaleStartTime(), "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
	    presale.setTimes( ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	    if ( presale.getStatus() == 0 ) {
		presale.setStartTimes( ( startTime.getTime() - nowTime.getTime() ) / 1000 );
	    }
	    return presale;
	}
	return null;
    }

    @Override
    public PhoneProductDetailResult getPresaleProductDetail( int proId, int shopId, int activityId, PhoneProductDetailResult result, Member member, MallPaySet mallPaySet ) {
	/*if ( activityId == 0 ) {
	    return result;
	}*/
	boolean isOpenPresale = false;
	if ( CommonUtil.isNotEmpty( mallPaySet ) ) {
	    if ( CommonUtil.isNotEmpty( mallPaySet.getIsPresale() ) ) {//是否开启预售
		if ( mallPaySet.getIsPresale().toString().equals( "1" ) ) {
		    isOpenPresale = true;
		}
	    }
	}
	if ( !isOpenPresale ) {
	    return result;
	}
	MallPresaleDeposit deposit = null;
	MallPresale presale = getPresaleByProId( proId, shopId, activityId );
	if ( CommonUtil.isEmpty( presale ) ) {
	    return result;
	}
	result.setActivityTimes( presale.getTimes() );
	PhonePresaleProductDetailResult presaleResult = new PhonePresaleProductDetailResult();
	result.setActivityId( presale.getId() );
	result.setActivityStatus( presale.getStatus() );
	if ( presale.getIsDeposit() == 1 ) {
	    presaleResult.setDingMoney( CommonUtil.toDouble( presale.getDepositPercent() ) );
	}

	Map< String,Object > presaleMap = new HashMap<>();
	presaleMap.put( "presaleId", presale.getId() );
	boolean isBuyFlag = false;
	if ( CommonUtil.isNotEmpty( member ) ) {
	    presaleMap.put( "joinUserId", member.getId() );
	    deposit = mallPresaleDepositDAO.selectCountByPresaleId( presaleMap );//用户是否已经交纳定金  》0   已经交纳了定金
	    if ( deposit != null ) {
		if ( deposit.getDepositStatus().toString().equals( "1" ) ) {
		    isBuyFlag = true;

		    presaleResult.setWeiMoney( CommonUtil.subtract( CommonUtil.toDouble( deposit.getOrderMoney() ), CommonUtil.toDouble( deposit.getDepositMoney() ) ) );
		}
	    }
	    if ( CommonUtil.isNotEmpty( deposit ) ) {
		Map< String,Object > invMap = mallProductService.getProInvIdBySpecId( deposit.getProSpecificaIds(), deposit.getProductId() );
		if ( CommonUtil.isNotEmpty( invMap ) ) {
		    if ( CommonUtil.isNotEmpty( invMap.get( "id" ) ) ) {
			result.setInvId( CommonUtil.toInteger( invMap.get( "id" ) ) );
		    }
		}

	    }
	}
	result.setIsShowLiJiBuyButton( 0 );//隐藏立即购买
	if ( isBuyFlag ) {
	    if ( presale.getStatus() == 1 ) {
		presaleResult.setIsShowPresaleButton( 1 );//显示预定按钮
	    } else if ( presale.getStatus() == 0 ) {
		presaleResult.setIsShowStartButton( 1 );//显示即将开售按钮
	    }
	} else {
	    if ( presale.getStatus() == 1 ) {
		result.setIsShowLiJiBuyButton( 1 );//显示立即购买
	    } else if ( presale.getStatus() == 0 ) {
		presaleResult.setIsShowPresaleButton( 1 );//显示预定按钮
	    }
	}
	if ( CommonUtil.isNotEmpty( deposit ) ) {
	    if ( isBuyFlag && presale.getStatus() == 0 ) {
		presaleResult.setPayDespositStatus( 1 );//已缴纳定金
	    }
	}

	//查询预售商品订购数量
	int buyCout = mallPresaleDepositDAO.selectBuyCountByPreId( presaleMap );
	if ( CommonUtil.isNotEmpty( presale.getOrderNum() ) ) {
	    buyCout = buyCout + presale.getOrderNum();//订购量
	}
	presaleResult.setBuyCount( buyCout );

	List< MallPresaleTime > timeList = mallPresaleTimeService.getPresaleTimeByPreId( presale.getId() );
	double proPrice = result.getProductPrice();
	proPrice = getPresalePrice( proPrice, timeList );
	DecimalFormat df = new DecimalFormat( "######0.00" );
	result.setProductPrice( CommonUtil.toDouble( df.format( proPrice ) ) );
	result.setPresaleResult( presaleResult );
	return result;
    }

    @Override
    public double getPresalePrice( double proPrice, List< MallPresaleTime > timeList ) {
	double presaleDiscount;
	if ( timeList != null && timeList.size() > 0 ) {
	    for ( MallPresaleTime mallPresaleTime : timeList ) {
		Date endTime = DateTimeKit.parse( mallPresaleTime.getEndTime(), "yyyy-MM-dd HH:mm:ss" );
		Date startTime = DateTimeKit.parse( mallPresaleTime.getStartTime(), "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
		if ( startTime.getTime() <= nowTime.getTime() && nowTime.getTime() < endTime.getTime() ) {
		    int saleType = mallPresaleTime.getSaleType();
		    int priceType = mallPresaleTime.getPriceType();
		    if ( priceType == 2 ) {//加减金额
			presaleDiscount = CommonUtil.toDouble( mallPresaleTime.getPrice() );
			if ( saleType == 1 ) {//上调价格
			    proPrice = proPrice + presaleDiscount;
			} else {//下调价格
			    proPrice = proPrice - presaleDiscount;
			}
		    } else {//百分比
			presaleDiscount = CommonUtil.toDouble( mallPresaleTime.getPrice() ) / 100;
			if ( saleType == 1 ) {//上调价格
			    proPrice = proPrice + ( proPrice * ( presaleDiscount ) );
			} else {//下调价格
			    proPrice = proPrice - ( proPrice * ( presaleDiscount ) );
			}
		    }
		    break;
		}
	    }
	}
	return proPrice;
    }

    /**
     * 查询用户参加预售的数量
     */
    @Override
    public int selectCountByBuyId( Map< String,Object > params ) {
	return mallPresaleDAO.selectCountByPresaleId( params );
    }

    /**
     * 根据店铺id查询预售信息
     */
    @Override
    public List< Map< String,Object > > selectgbPresaleByShopId( Map< String,Object > maps ) {
	return mallPresaleDAO.selectgbProductByShopId( maps );
    }

    /**
     * 判断是否超过了限购
     *
     * @param map
     * @param memberId
     *
     * @return
     */
    @Override
    public Map< String,Object > isMaxNum( Map< String,Object > map, String memberId, MallOrderDetail mallOrderDetail ) {
	Map< String,Object > result = new HashMap<>();
	int presaleId = Integer.parseInt( map.get( "groupBuyId" ).toString() );

	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "joinUserId", memberId );
	params.put( "presaleId", presaleId );
	//判断是否已经加入到预售竞拍中
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectCountByPresaleId( params );//判断用户是否已经交纳定金
	if ( deposit == null ) {//没有交纳定金的用户来判断库存

	    result = isInvNum( map, mallOrderDetail );
	} else {
	    result.put( "result", true );
	}
	return result;
    }

    @Override
    public List< MallPresaleGive > selectGiveByUserId( BusUser user ) {
	return mallPresaleGiveDAO.selectByUserId( user.getId() );
    }

    @Override
    public PageUtil selectPageGiveByUserId( Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallPresaleGiveDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mPresale/give/index.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量
	if ( count > 0 ) {// 判断预售送礼是否有数据
	    List< MallPresaleGive > presaleList = mallPresaleGiveDAO.selectByPage( params );
	    page.setSubList( presaleList );
	}

	return page;
    }

    @SuppressWarnings( { "unchecked", "deprecation" } )
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int editPresaleSet( Map< String,Object > params, int userId ) {
	int num = 0;
	int code = -1;
	boolean fenbiFlag = false;
	// 逻辑删除预售送礼
	if ( !CommonUtil.isEmpty( params.get( "delPresaleSet" ) ) ) {
	    List< MallPresaleGive > giveList = JSONArray.parseArray( params.get( "delPresaleSet" ).toString(), MallPresaleGive.class );
	    if ( giveList != null && giveList.size() > 0 ) {
		for ( MallPresaleGive give : giveList ) {
		    num = mallPresaleGiveDAO.updateById( give );
		    if ( give.getGiveType() == 2 ) {
			fenbiFlag = true;
		    }
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( params.get( "presaleSet" ) ) ) {
	    List< MallPresaleGive > setList = JSONArray.parseArray( params.get( "presaleSet" ).toString(), MallPresaleGive.class );
	    if ( setList != null && setList.size() > 0 ) {
		for ( MallPresaleGive give : setList ) {
		    if ( CommonUtil.isNotEmpty( give.getId() ) ) {
			num = mallPresaleGiveDAO.updateById( give );
		    } else {
			give.setUserId( userId );
			num = mallPresaleGiveDAO.insert( give );
		    }
		    if ( give.getGiveType() == 2 ) {
			fenbiFlag = true;
		    }
		}
	    }
	    if ( num > 0 ) {
		code = 1;
		if ( fenbiFlag ) {
		    saveRenbiFlowRecord( userId );
		}

	    }
	}
	return code;
    }

    @SuppressWarnings( { "unchecked", "deprecation" } )
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int newEditPresaleSet( Map< String,Object > params, int userId ) {
	int num = 0;
	int code = -1;
	boolean fenbiFlag = false;

	Wrapper< MallPresaleGive > prowrapper = new EntityWrapper<>();
	prowrapper.where( "user_id={0} and is_delete = 0 ", userId );
	List< MallPresaleGive > presaleGives = mallPresaleGiveDAO.selectList( prowrapper );

	if ( CommonUtil.isNotEmpty( params.get( "presaleSet" ) ) ) {
	    List< MallPresaleGive > setList = JSONArray.parseArray( params.get( "presaleSet" ).toString(), MallPresaleGive.class );
	    if ( setList != null && setList.size() > 0 ) {
		for ( MallPresaleGive give : setList ) {
		    if ( CommonUtil.isNotEmpty( give.getId() ) ) {
			for ( MallPresaleGive presaleGive : presaleGives ) {
			    if ( presaleGive.getId() == give.getId() ) {
				presaleGives.remove( presaleGive );// 移除已经存在预售送礼
				break;
			    }
			}
			num = mallPresaleGiveDAO.updateById( give );
		    } else {
			give.setUserId( userId );
			num = mallPresaleGiveDAO.insert( give );
		    }
		    if ( give.getGiveType() == 2 ) {
			fenbiFlag = true;
		    }
		}
	    }

	    if ( num > 0 ) {
		code = 1;
		if ( fenbiFlag ) {
		    saveRenbiFlowRecord( userId );
		}

	    }
	}

	//删除预售送礼
	if ( presaleGives != null && presaleGives.size() > 0 ) {
	    for ( MallPresaleGive presaleGive : presaleGives ) {
		presaleGive.setIsDelete( 1 );
		mallPresaleGiveDAO.updateById( presaleGive );
	    }
	}
	return code;
    }

    @SuppressWarnings( { "unchecked", "deprecation" } )
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int newEditOnePresaleSet( Map< String,Object > params, int userId ) {
	int num = 0;
	int code = -1;
	boolean fenbiFlag = false;

	if ( CommonUtil.isNotEmpty( params.get( "presaleSet" ) ) ) {
	    MallPresaleGive give = JSONObject.parseObject( params.get( "presaleSet" ).toString(), MallPresaleGive.class );
	    if ( CommonUtil.isNotEmpty( give.getId() ) ) {
		num = mallPresaleGiveDAO.updateById( give );
	    } else {
		give.setUserId( userId );
		num = mallPresaleGiveDAO.insert( give );
	    }
	    if ( give.getGiveType() == 2 ) {
		fenbiFlag = true;
	    }

	    if ( num > 0 ) {
		code = 1;
		if ( fenbiFlag ) {
		    saveRenbiFlowRecord( userId );
		}

	    }
	}
	return code;
    }

    private void saveRenbiFlowRecord( int userId ) {
	//查询资产分配
	FenbiFlowRecord fenbi = new FenbiFlowRecord();
	fenbi.setBusUserId( userId );
	fenbi.setRecType( 1 );
	/*fenbi.setRecCreatetime( DateTimeKit.getDateTime() );*/
	fenbi.setRecDesc( "商城预售送粉币" );
	fenbi.setRecFreezeType( 34 );
	fenbi.setRecFkId( 0 );
	fenbi.setRecCount( Double.valueOf( "0" ) );

	//查询粉币数量 的参数
	FenbiSurplus fenbiSurplus = new FenbiSurplus();
	fenbiSurplus.setBusId( userId );
	fenbiSurplus.setFkId( fenbi.getRecFkId() );
	fenbiSurplus.setFre_type( fenbi.getRecFreezeType() );
	fenbiSurplus.setRec_type( fenbi.getRecType() );
	//判断用户是否已经资产分配过了
	FenBiCount fenBiCount = fenBiFlowService.getFenbiSurplus( fenbiSurplus );
	if ( CommonUtil.isEmpty( fenBiCount ) ) {
	    fenBiFlowService.saveFenbiFlowRecord( fenbi );
	}
    }

    /**
     * 发货时赠送实体物品
     */
    @Override
    public void deliveryRank( MallOrder order ) {
	if ( order.getOrderType() == 6 ) {//预售商品购买的
	    if ( CommonUtil.isNotEmpty( order.getGroupBuyId() ) ) {
		MallPresaleRank rank = new MallPresaleRank();
		rank.setOrderId( order.getId() );
		rank.setPresaleId( order.getGroupBuyId() );
		MallPresaleRank presaleRank = mallPresaleRankDAO.selectByPresale( rank );
		if ( presaleRank != null ) {
		    if ( presaleRank.getType() == 3 ) {//实体物品发货时赠送
			MallPresaleRank mallRank = new MallPresaleRank();
			mallRank.setId( presaleRank.getId() );
			mallRank.setIsGive( "1" );
			mallPresaleRankDAO.updateById( mallRank );
		    }
		}
	    }
	}
    }

    @Override
    public void loadPresaleByJedis( MallPresale pre ) {

	String key = "presale_num";
	Map< String,Object > maps = new HashMap<>();
	maps.put( "status", 1 );
	if ( CommonUtil.isNotEmpty( pre ) ) {
	    maps.put( "id", pre.getId() );
	}
	List< Map< String,Object > > productList = mallPresaleDAO.selectgbProductByShopId( maps );

	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		String proId = map2.get( "id" ).toString();
		String is_specifica = map2.get( "is_specifica" ).toString();// 是否有规格，1有规格，有规格取规格里面的值
		String stockNum = map2.get( "stockTotal" ).toString();//商品总库存

		String preId = map2.get( "preId" ).toString();
		Map< String,Object > map = new HashMap<>();
		map.put( "invNum", stockNum );

		List< Map< String,Object > > specList = new ArrayList<>();
		if ( is_specifica.equals( "1" ) ) {
		    //规格库存
		    List< MallProductInventory > invenList = mallProductInventoryService.selectInvenByProductId( Integer.parseInt( proId ) );
		    if ( invenList != null && invenList.size() > 0 ) {
			for ( MallProductInventory inven : invenList ) {
			    Map< String,Object > invMap = new HashMap<>();
			    StringBuilder specIds = new StringBuilder();
			    for ( MallProductSpecifica spec : inven.getSpecList() ) {
				if ( !specIds.toString().equals( "" ) ) {
				    specIds.append( "," );
				}
				specIds.append( spec.getSpecificaValueId() );
			    }
			    //							String field =preId+"_"+specIds;

			    //JedisUtil.map(key, field, inven.getInvNum().toString());
			    invMap.put( "specId", specIds.toString() );
			    invMap.put( "invId", inven.getId() );
			    invMap.put( "invNum", inven.getInvNum() );
			    specList.add( invMap );
			}
		    }
		}
		if ( specList == null || specList.size() == 0 ) {
		    is_specifica = "0";
		} else {
		    map.put( "specArr", specList );
		}
		map.put( "is_specifica", is_specifica );
		JedisUtil.map( key, preId, JSONObject.parseObject( JSON.toJSONString( map ) ).toString() );
	    }
	}

    }

    /**
     * 判断秒杀的库存是否能够秒杀
     */
    private Map< String,Object > isInvNum( Map< String,Object > params, MallOrderDetail mallOrderDetail ) {
	Map< String,Object > result = new HashMap<>();
	String preId = params.get( "groupBuyId" ).toString();
	String invKey = "presale_num";//秒杀库存的key
	String specificas = "";
	//判断商品是否有规格
	if ( CommonUtil.isNotEmpty( mallOrderDetail.getProductSpecificas() ) ) {
	    specificas = mallOrderDetail.getProductSpecificas();
	}
	//查询秒杀商品的库存
	Integer invNum = 0;
	String value = JedisUtil.maoget( invKey, preId + "" );
	if ( CommonUtil.isNotEmpty( value ) ) {
	    JSONObject specObj = JSONObject.parseObject( value );
	    if ( !specificas.equals( "" ) ) {
		//有规格，取规格的库存
		if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
		    JSONArray preSpecArr = JSONArray.parseArray( specObj.get( "specArr" ).toString() );
		    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
			for ( Object obj : preSpecArr ) {
			    JSONObject preObj = JSONObject.parseObject( obj.toString() );
			    if ( preObj.get( "specId" ).toString().equals( specificas ) ) {
				invNum = CommonUtil.toInteger( preObj.get( "invNum" ) );
				break;
			    }
			}
		    }
		}
	    }
	    if ( CommonUtil.isEmpty( specificas ) || invNum == 0 ) {
		//无规格，则取商品库存
		if ( CommonUtil.isNotEmpty( specObj.get( "invNum" ) ) ) {
		    invNum = CommonUtil.toInteger( specObj.get( "invNum" ) );
		}
	    }
	}

	int proNum = mallOrderDetail.getDetProNum();//购买商品的用户
	//判断库存是否够
	if ( invNum >= proNum && invNum > 0 ) {
	    result.put( "result", true );
	} else {
	    result.put( "msg", "预售商品的库存不够" );
	    result.put( "result", false );
	}
	return result;
    }

    /**
     * 减去预售商品的库存（redis）
     */
    @Override
    public void diffInvNum( MallOrder order ) {
	String invKey = "presale_num";//秒杀库存的key

	String field = order.getGroupBuyId().toString();
	String specificas = "";
	//判断用户是否已经交了定金
	Map< String,Object > params = new HashMap<>();
	params.put( "presaleId", order.getGroupBuyId() );
	params.put( "userId", order.getBuyerUserId() );
	int count = mallPresaleDepositDAO.selectCountDeposit( params );
	if ( order.getMallOrderDetail() != null && order.getMallOrderDetail().size() > 0 && count == 0 ) {
	    for ( MallOrderDetail detail : order.getMallOrderDetail() ) {
		//判断商品是否有规格
		if ( CommonUtil.isNotEmpty( detail.getProductSpecificas() ) ) {
		    specificas = detail.getProductSpecificas();
		}
		//查询预售商品的库存
		Integer invNum = 0;
		String value = JedisUtil.maoget( invKey, field );
		if ( CommonUtil.isNotEmpty( value ) ) {
		    JSONObject specObj = JSONObject.parseObject( value );
		    JSONArray arr = new JSONArray();
		    if ( !specificas.equals( "" ) ) {
			//有规格，取规格的库存
			if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
			    JSONArray preSpecArr = JSONArray.parseArray( specObj.get( "specArr" ).toString() );
			    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
				for ( Object obj : preSpecArr ) {
				    JSONObject preObj = JSONObject.parseObject( obj.toString() );
				    if ( preObj.get( "specId" ).toString().equals( specificas ) ) {
					invNum = CommonUtil.toInteger( preObj.get( "invNum" ) );
					//break;
					preObj.put( "invNum", invNum - 1 );
				    }
				    arr.add( preObj );
				}
			    }
			}
		    }
		    if ( arr != null && arr.size() > 0 ) {
			specObj.put( "specArr", arr );
		    }
		    if ( CommonUtil.isNotEmpty( specObj.get( "invNum" ) ) ) {
			invNum = CommonUtil.toInteger( specObj.get( "invNum" ) );
			specObj.put( "invNum", invNum - 1 );
		    }
		    JedisUtil.map( invKey, field, specObj.toString() );
		}
	    }
	}
    }

    @Override
    public void paySucessPresale( MallOrder order ) {
	MallPresaleDeposit preDeposit = new MallPresaleDeposit();
	preDeposit.setPresaleId( order.getGroupBuyId() );
	preDeposit.setIsSubmit( 0 );
	preDeposit.setDepositStatus( 1 );
	preDeposit.setUserId( order.getBuyerUserId() );
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectByDeposit( preDeposit );
	if ( deposit != null ) {
	    MallPresaleDeposit presaleDeposit = new MallPresaleDeposit();
	    presaleDeposit.setId( deposit.getId() );
	    presaleDeposit.setIsSubmit( 1 );
	    if ( CommonUtil.isNotEmpty( order.getOrderPayNo() ) ) {
		presaleDeposit.setPayNo( order.getOrderPayNo() );
	    }
	    //修改预售定金的状态
	    mallPresaleDepositDAO.updateById( presaleDeposit );

	    MallPresaleRank rank = new MallPresaleRank();
	    rank.setDepositId( deposit.getId() );
	    rank.setPresaleId( deposit.getPresaleId() );
	    rank.setMemberId( order.getBuyerUserId() );
	    MallPresaleRank presaleRank = mallPresaleRankDAO.selectByPresale( rank );
	    if ( CommonUtil.isNotEmpty( presaleRank ) ) {
		rank.setId( presaleRank.getId() );
		rank.setOrderId( order.getId() );
		//修改排名信息
		mallPresaleRankDAO.updateById( rank );
	    }
	}
    }

    @Override
    public void presaleStartRemain() {
	String key = Constants.REDIS_KEY + "presale_message";
	Map< String,String > map = JedisUtil.mapGetAll( key );
	if ( CommonUtil.isNotEmpty( map ) ) {
	    Set< String > set = map.keySet();
	    for ( String str : set ) {
		if ( str != null && !str.equals( "" ) ) {
		    int id = CommonUtil.toInteger( str );
		    Map< String,Object > preMap = mallPresaleDAO.selectByPresaleId( id );
		    if ( CommonUtil.isNotEmpty( preMap ) ) {
			if ( preMap.get( "is_use" ).toString().equals( "1" ) && preMap.get( "is_delete" ).toString().equals( "0" ) ) {

			    String endTimes = preMap.get( "sale_end_time" ).toString();
			    String startTimes = preMap.get( "sale_start_time" ).toString();
			    String format = DateTimeKit.DEFAULT_DATETIME_FORMAT;
			    String nowDate = DateTimeKit.format( new Date(), format );
			    long endHour = DateTimeKit.minsBetween( nowDate, endTimes, 3600000, format );
			    String msg = "";
			    String url = PropertiesUtil.getHomeUrl() + "/mallPage/" + preMap.get( "product_id" ) + "/" + preMap.get( "shop_id" ) + "/79B4DE7C/phoneProduct.do";
			    long startHour = DateTimeKit.minsBetween( nowDate, startTimes, 3600000, format );
			    if ( CommonUtil.isNotEmpty( map.get( str ) ) ) {
				JSONArray arr = JSONArray.parseArray( map.get( str ) );
				JSONArray msgArr = new JSONArray();
				if ( arr != null && arr.size() > 0 ) {
				    for ( Object object : arr ) {
					boolean flag = false;
					JSONObject obj = JSONObject.parseObject( object.toString() );
					int isStartRemain = 0;
					int isEndRemain = 0;
					if ( CommonUtil.isNotEmpty( obj ) ) {

					    if ( CommonUtil.isNotEmpty( obj.get( "isStartRemain" ) ) ) {
						isStartRemain = CommonUtil.toInteger( obj.get( "isStartRemain" ) );
						if ( startHour <= 24 && obj.get( "isStartRemain" ).toString().equals( "0" ) ) {
						    msg = "商品：" + preMap.get( "proName" ) + "将在" + startTimes + "开售";
						    isStartRemain = 1;
						}
					    }
					    if ( CommonUtil.isNotEmpty( obj.get( "isEndRemain" ) ) ) {
						isEndRemain = CommonUtil.toInteger( obj.get( "isEndRemain" ) );
						if ( endHour <= 1 && obj.get( "isEndRemain" ).toString().equals( "0" ) ) {
						    msg = "商品：" + preMap.get( "proName" ) + "将在" + endTimes + "结束预售，请尽快购买";
						    isEndRemain = 1;
						}
					    }
					    String time = startTimes + " 至 " + endTimes;
					    if ( CommonUtil.isNotEmpty( msg ) ) {
						flag = sendMsg( obj, msg, url, time );
					    }
					}
					if ( flag ) {//修改提醒状态
					    if ( isStartRemain == 1 ) {
						obj.put( "isStartRemain", isStartRemain );
					    }
					    if ( isEndRemain == 1 ) {
						obj.put( "isEndRemain", isEndRemain );
					    }
					    if ( isStartRemain != 1 || isEndRemain != 1 ) {
						msgArr.add( obj );
					    }
					} else {
					    msgArr.add( obj );
					}

				    }
				    if ( msgArr.size() > 0 ) {
					JedisUtil.map( key, str, msgArr.toString() );
				    } else {
					JedisUtil.mapdel( key, str );
				    }
				}
			    }
			}
		    }
		}
	    }
	}
    }

    /**
     * 发送消息模板
     */
    private boolean sendMsg( JSONObject msgObj, String msg, String url, String time ) {
	boolean result = false;
	int id = 0;
	String title = "活动时间变更通知";

	//		int busUserId = CommonUtil.toInteger(msgObj.get("busUserId"));
	int memberId = CommonUtil.toInteger( msgObj.get( "wxMemberId" ) );
	WxPublicUsers publicUser = wxPublicUserService.selectByMemberId( memberId );
	MallPaySet paySet = new MallPaySet();
	paySet.setUserId( publicUser.getBusUserId() );
	MallPaySet set = mallPaySetService.selectByUserId( paySet );
	if ( CommonUtil.isNotEmpty( set ) && CommonUtil.isNotEmpty( title ) ) {
	    if ( set.getIsMessage().toString().equals( "1" ) ) {//判断是否开启了消息模板的功能
		if ( CommonUtil.isNotEmpty( set.getMessageJson() ) ) {
		    JSONArray arr = JSONArray.parseArray( set.getMessageJson() );
		    if ( arr != null && arr.size() > 0 ) {
			for ( Object object : arr ) {
			    JSONObject obj = JSONObject.parseObject( object.toString() );
			    if ( obj.getString( "title" ).equals( title ) ) {
				if ( CommonUtil.isNotEmpty( obj.get( "id" ) ) ) {
				    id = CommonUtil.toInteger( obj.get( "id" ) );
				    break;
				}
			    }
			}
		    }
		}
	    }
	}

	if ( id > 0 ) {
	    List< Object > objs = new ArrayList<>();
	    objs.add( msg );
	    objs.add( "商品预售" );
	    objs.add( time );
	    objs.add( "商品预售提醒" );

	    SendWxMsgTemplate template = new SendWxMsgTemplate();
	    template.setId( id );
	    template.setUrl( url );
	    template.setMemberId( memberId );
	    template.setPublicId( publicUser.getId() );
	    template.setObjs( objs );
	    logger.info( "调用发送消息模板的接口===================" );
	    return wxPublicUserService.sendWxMsgTemplate( template );
	}
	return result;
    }

    @Override
    public void presaleProEnd() {
	List< MallPresale > presaleList = mallPresaleDAO.selectByPresaleEnd();
	if ( presaleList != null && presaleList.size() > 0 ) {
	    for ( MallPresale mallPresale : presaleList ) {
		String invKey = "presale_num";//秒杀库存的key
		JedisUtil.mapdel( invKey, mallPresale.getId().toString() );
		String key = Constants.REDIS_KEY + "presale_message";
		JedisUtil.mapdel( key, mallPresale.getId().toString() );
	    }
	}

    }

    /**
     * 搜索预售商品信息
     */
    @Override
    public PageUtil searchPresaleAll( PhoneSearchProductDTO searchProductDTO, Member member ) {
	if ( CommonUtil.isNotEmpty( member ) && CommonUtil.isNotEmpty( searchProductDTO.getSearchContent() ) ) {
	    //新增搜索关键词
	    mallSearchKeywordService.insertSeachKeyWord( member.getId(), searchProductDTO.getShopId(), searchProductDTO.getSearchContent() );
	}
	PageUtil page = new PageUtil();
	if ( !searchProductDTO.getSort().equals( "price" ) ) {
	    int pageSize = 10;
	    int curPage = CommonUtil.isEmpty( searchProductDTO.getCurPage() ) ? 1 : searchProductDTO.getCurPage();
	    int rowCount = mallPresaleDAO.selectCountGoingPresaleProduct( searchProductDTO );
	    page = new PageUtil( curPage, pageSize, rowCount, "" );
	    searchProductDTO.setFirstNum( pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	    searchProductDTO.setMaxNum( pageSize );
	}

	List< Map< String,Object > > list = new ArrayList<>();// 存放店铺下的商品

	List< Map< String,Object > > productList = mallPresaleDAO.selectGoingPresaleProduct( searchProductDTO );

	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map : productList ) {
		double presaleDiscount = 100;

		double proPrice;
		if ( map.get( "is_specifica" ).toString().equals( "1" ) && CommonUtil.isNotEmpty( map.get( "inv_price" ) ) && CommonUtil.toDouble( map.get( "inv_price" ) ) > 0 ) {
		    proPrice = CommonUtil.toDouble( map.get( "inv_price" ) );
		} else {
		    proPrice = CommonUtil.toDouble( map.get( "pro_price" ) );
		}
		List< MallPresaleTime > timeList = mallPresaleTimeService.getPresaleTimeByPreId( CommonUtil.toInteger( map.get( "activityId" ) ) );
		if ( timeList != null && timeList.size() > 0 ) {
		    for ( MallPresaleTime mallPresaleTime : timeList ) {
			Date startTime = DateTimeKit.parse( mallPresaleTime.getStartTime(), "yyyy-MM-dd HH:mm:ss" );
			Date endTime = DateTimeKit.parse( mallPresaleTime.getEndTime(), "yyyy-MM-dd HH:mm:ss" );
			Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss" );
			if ( startTime.getTime() <= nowTime.getTime() && nowTime.getTime() < endTime.getTime() ) {
			    if ( mallPresaleTime.getPriceType() == 2 ) {
				presaleDiscount = CommonUtil.toDouble( mallPresaleTime.getPrice() );
			    } else {
				presaleDiscount = CommonUtil.toDouble( mallPresaleTime.getPrice() ) / 100;
			    }
			    double d = CommonUtil.multiply( proPrice, presaleDiscount );
			    if ( mallPresaleTime.getSaleType() == 1 ) {//上涨价格
				if ( mallPresaleTime.getPriceType() == 2 ) {//按价格
				    proPrice = CommonUtil.add( proPrice, presaleDiscount );//proPrice = proPrice + presaleDiscount;
				} else {//按百分比
				    proPrice = CommonUtil.add( proPrice, d );
				    //proPrice = proPrice + ( proPrice * ( presaleDiscount ) );
				}
			    } else if ( mallPresaleTime.getSaleType() == 2 ) {//下调金额
				if ( mallPresaleTime.getPriceType() == 2 ) {//按价格
				    proPrice = CommonUtil.subtract( proPrice, presaleDiscount );// proPrice = proPrice - presaleDiscount;
				} else {//按百分比
				    proPrice = CommonUtil.subtract( proPrice, d );
				    //proPrice = proPrice - ( proPrice * ( presaleDiscount ) );
				}
			    }
			    map.put( "price", proPrice );
			    break;
			}

		    }
		}
		list.add( map );
	    }
	    list = mallPageService.getSearchProductParam( list, 1, searchProductDTO );
	}
	if ( CommonUtil.isNotEmpty( searchProductDTO.getSort() ) && searchProductDTO.getSort().equals( "price" ) ) {
	    if ( CommonUtil.isNotEmpty( searchProductDTO.getIsDesc() ) ) {
		if ( searchProductDTO.getIsDesc() == 1 ) {
		    Collections.sort( productList, new MallComparatorUtil( "price" ) );
		}
	    }
	}

	page.setSubList( list );
	return page;
    }

    @Override
    public boolean presaleProductCanBuy( int presaleId, int invId, int productNum, int memberId, int memberBuyNum ) {
	if ( CommonUtil.isEmpty( presaleId ) || presaleId == 0 ) {
	    return false;
	}
	MallPresale presale = new MallPresale();
	presale.setId( presaleId );
	presale = mallPresaleDAO.selectBuyByProductId( presale );
	if ( CommonUtil.isEmpty( presale ) ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的预售商品被删除或已失效" );
	}
	if ( presale.getStatus() == 0 ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的预售商品活动还未开始" );
	} else if ( presale.getStatus() == -1 ) {
	    throw new BusinessException( ResponseEnums.ACTIVITY_ERROR.getCode(), "您购买的预售商品活动已结束" );
	}
	/*int maxBuyNum = presale.getGMaxBuyNum();
	if ( maxBuyNum > 0 && memberBuyNum > -1 ) {
	    if ( memberBuyNum + productNum > maxBuyNum ) {
		throw new BusinessException( ResponseEnums.MAX_BUY_ERROR.getCode(), "每人限购" + maxBuyNum + "件" + ResponseEnums.MAX_BUY_ERROR.getDesc() );
	    }
	}*/
	if ( presale.getIsDeposit() == 1 && memberId > 0 ) {//需缴纳定金
	    MallPresaleDeposit deposit = new MallPresaleDeposit();
	    deposit.setPresaleId( presaleId );
	    deposit.setUserId( memberId );
	    MallPresaleDeposit presaleDeposit = mallPresaleDepositDAO.selectOne( deposit );
	    if ( CommonUtil.isEmpty( presaleDeposit ) ) {
		throw new BusinessException( ResponseEnums.ACTIVITY_MONEY_ERROR.getCode(), "您未缴纳定金，不能购买该预售商品" );
	    } else {
		if ( presaleDeposit.getDepositStatus() != 1 ) {
		    throw new BusinessException( ResponseEnums.ACTIVITY_MONEY_ERROR.getCode(), "您未缴纳定金，不能购买该预售商品" );
		}
	    }
	}
	return true;
    }

}
