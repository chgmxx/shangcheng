package com.gt.mall.service.web.presale.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.bean.wx.SendWxMsgTemplate;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallPaySetDAO;
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
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.page.MallPageService;
import com.gt.mall.service.web.presale.MallPresaleService;
import com.gt.mall.service.web.presale.MallPresaleTimeService;
import com.gt.mall.service.web.product.MallProductInventoryService;
import com.gt.mall.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
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
    private MallPaySetDAO               mallPaySetDAO;
    @Autowired
    private MallOrderDAO                mallOrderDAO;
    @Autowired
    private MallPaySetService           mallPaySetService;
    @Autowired
    private WxPublicUserService         wxPublicUserService;

    /**
     * 通过店铺id来查询预售
     *
     * @Title: selectFreightByShopId
     */
    @Override
    public PageUtil selectPresaleByShopId( Map< String,Object > params ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1
			: CommonUtil.toInteger( params.get( "curPage" ) );
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
	    orderWrapper.where( "order_type = 5 and order_status > 1 and p_join_id ={0}", presale.getId() );
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
	    MallPresale presale = (MallPresale) JSONObject.toBean( JSONObject.fromObject( presaleMap.get( "presale" ) ),
			    MallPresale.class );
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
    public List< Map< String,Object > > getPresaleAll( Member member,
		    Map< String,Object > maps ) {
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
	List< Map< String,Object > > productList = mallPresaleDAO
			.selectgbProductByShopId( maps );
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
	if ( CommonUtil.isNotEmpty( presaleId ) ) {
	    presale.setId( presaleId );
	}
	presale.setProductId( proId );
	presale.setShopId( shopId );
	presale = mallPresaleDAO.selectBuyByProductId( presale );
	if ( presale != null && CommonUtil.isNotEmpty( presale.getId() ) ) {

	    String key = Constants.REDIS_KEY + "presale_num";
	    String field = presale.getId().toString();
	    if ( JedisUtil.hExists( key, field ) ) {
		String str = JedisUtil.maoget( key, field );
		if ( CommonUtil.isNotEmpty( str ) ) {
		    JSONObject obj = JSONObject.fromObject( str );
		    if ( CommonUtil.isNotEmpty( obj.get( "invNum" ) ) ) {
			presale.setInvNum( CommonUtil.toInteger( obj.get( "invNum" ) ) );
		    }
		    if ( CommonUtil.isNotEmpty( obj.get( "specArr" ) ) ) {
			JSONArray preSpecArr = JSONArray.fromObject( obj.get( "specArr" ) );
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
    public List< Map< String,Object > > selectgbPresaleByShopId(
		    Map< String,Object > maps ) {
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
    public Map< String,Object > isMaxNum( Map< String,Object > map, String memberId ) {
	Map< String,Object > result = new HashMap< String,Object >();
	int presaleId = Integer.parseInt( map.get( "groupBuyId" ).toString() );

	Map< String,Object > params = new HashMap< String,Object >();
	params.put( "joinUserId", memberId );
	params.put( "presaleId", presaleId );
	MallPresaleDeposit deposit = mallPresaleDepositDAO.selectCountByPresaleId( params );//判断用户是否已经交纳定金
	//判断是否已经加入到预售竞拍中
	//		MallPresale presale = mallPresaleDAO.selectByPrimaryKey(presaleId);
	if ( deposit == null ) {//没有交纳定金的用户来判断库存

	    result = isInvNum( map );
	} else {
	    result.put( "result", true );
	}
	return result;
    }

    @Override
    public List< MallPresaleGive > selectGiveByUserId( BusUser user ) {
	return mallPresaleGiveDAO.selectByUserId( user.getId() );
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
	    List< MallPresaleGive > giveList = (List< MallPresaleGive >)
			    JSONArray.toList( JSONArray.fromObject( params.get( "delPresaleSet" ) ),
					    MallPresaleGive.class );
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
	    List< MallPresaleGive > setList = (List< MallPresaleGive >)
			    JSONArray.toList( JSONArray.fromObject( params.get( "presaleSet" ) ),
					    MallPresaleGive.class );
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

    private void saveRenbiFlowRecord( int userId ) {
	//todo 调用小屁孩  粉币资产分配接口
	//查询资产分配
	/*FenbiFlowRecord fenbi = new FenbiFlowRecord();
	fenbi.setBusUserId( userId );
	fenbi.setRecType( 1 );
	fenbi.setRecCreatetime( new Date() );
	fenbi.setRecDesc( "商城预售送粉币" );
	fenbi.setRecFreezeType( 34 );
	fenbi.setRecFkId( 0 );
	fenbi.setRecCount( Double.valueOf( "0" ) );
	//判断用户是否已经资产分配过了
	FenbiFlowRecord fenbis = fenbiMapper.getFenbi( fenbi.getBusUserId(), fenbi.getRecType(), fenbi.getRecFreezeType(), fenbi.getRecFkId() );
	if ( CommonUtil.isEmpty( fenbis ) ) {
	    fenbiMapper.insertSelective( fenbi );
	}*/
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

	String key = Constants.REDIS_KEY + "presale_num";
	/*List<MallSeckill> seckillList = seckillMapper.selectByPage(null);
	if(seckillList != null && seckillList.size() > 0){
		for (MallSeckill mallSeckill : seckillList) {
			String field = mallSeckill.getId().toString();

			JedisUtil.map(key, field, mallSeckill.getsNum()+"");

			List<MallSeckillPrice> priceList = priceService.selectPriceByGroupId(mallSeckill.getId());
			if(priceList != null && priceList.size() > 0){
				for (MallSeckillPrice mallSeckillPrice : priceList) {
					field = mallSeckill.getId()+"_"+mallSeckillPrice.getSpecificaIds();
					JedisUtil.map(key, field, mallSeckillPrice.getSeckillNum()+"");
				}
			}
		}
	}*/

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
		    List< MallProductInventory > invenList = mallProductInventoryService
				    .selectInvenByProductId( Integer.parseInt( proId ) );
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
		JedisUtil.map( key, preId, JSONObject.fromObject( map ).toString() );
	    }
	}

    }

    /**
     * 判断秒杀的库存是否能够秒杀
     */
    @Override
    public Map< String,Object > isInvNum( Map< String,Object > params ) {
	Map< String,Object > result = new HashMap<>();
	String preId = params.get( "groupBuyId" ).toString();
	String invKey = Constants.REDIS_KEY + "presale_num";//秒杀库存的key
	String specificas = "";
	JSONArray dobj = JSONArray.fromObject( params.get( "detail" ).toString() );
	JSONArray arr = JSONArray.fromObject( dobj );
	if ( arr != null && arr.size() > 0 ) {
	    for ( Object object : arr ) {
		JSONObject detailObj = JSONObject.fromObject( object );
		//判断商品是否有规格
		if ( CommonUtil.isNotEmpty( detailObj.get( "product_specificas" ) ) ) {
		    specificas = detailObj.get( "product_specificas" ).toString();
		}
		//查询秒杀商品的库存
		Integer invNum = 0;
		String value = JedisUtil.maoget( invKey, preId + "" );
		if ( CommonUtil.isNotEmpty( value ) ) {
		    JSONObject specObj = JSONObject.fromObject( value );
		    if ( !specificas.equals( "" ) ) {
			//有规格，取规格的库存
			if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
			    JSONArray preSpecArr = JSONArray.fromObject( specObj.get( "specArr" ) );
			    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
				for ( Object obj : preSpecArr ) {
				    JSONObject preObj = JSONObject.fromObject( obj );
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

		int proNum = detailObj.getInt( "totalnum" );//购买商品的用户
		//判断库存是否够
		if ( invNum >= proNum && invNum > 0 ) {
		    result.put( "result", true );
		} else {
		    result.put( "msg", "预售商品的库存不够" );
		    result.put( "result", false );
		    break;
		}
	    }
	}
	return result;
    }

    /**
     * 减去预售商品的库存（redis）
     */
    @Override
    public void diffInvNum( MallOrder order ) {
	String invKey = Constants.REDIS_KEY + "presale_num";//秒杀库存的key

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
		    JSONObject specObj = JSONObject.fromObject( value );
		    JSONArray arr = new JSONArray();
		    if ( !specificas.equals( "" ) ) {
			//有规格，取规格的库存
			if ( CommonUtil.isNotEmpty( specObj.get( "specArr" ) ) ) {
			    JSONArray preSpecArr = JSONArray.fromObject( specObj.get( "specArr" ) );
			    if ( preSpecArr != null && preSpecArr.size() > 0 ) {
				for ( Object obj : preSpecArr ) {
				    JSONObject preObj = JSONObject.fromObject( obj );
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
				JSONArray arr = JSONArray.fromObject( map.get( str ) );
				JSONArray msgArr = new JSONArray();
				if ( arr != null && arr.size() > 0 ) {
				    for ( Object object : arr ) {
					boolean flag = false;
					JSONObject obj = JSONObject.fromObject( object );
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
		    JSONArray arr = JSONArray.fromObject( set.getMessageJson() );
		    if ( arr != null && arr.size() > 0 ) {
			for ( Object object : arr ) {
			    JSONObject obj = JSONObject.fromObject( object );
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
		String invKey = Constants.REDIS_KEY + "presale_num";//秒杀库存的key
		JedisUtil.mapdel( invKey, mallPresale.getId().toString() );
		String key = Constants.REDIS_KEY + "presale_message";
		JedisUtil.mapdel( key, mallPresale.getId().toString() );
	    }
	}

    }

}
