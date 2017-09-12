package com.gt.mall.service.web.pifa.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.pifa.MallPifaApplyDAO;
import com.gt.mall.dao.pifa.MallPifaDAO;
import com.gt.mall.dao.pifa.MallPifaPriceDAO;
import com.gt.mall.dao.product.MallSearchKeywordDAO;
import com.gt.mall.entity.pifa.MallPifa;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.entity.pifa.MallPifaPrice;
import com.gt.mall.service.web.pifa.MallPifaPriceService;
import com.gt.mall.service.web.pifa.MallPifaService;
import com.gt.mall.service.web.product.MallProductService;
import com.gt.mall.service.web.product.MallSearchKeywordService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.DateTimeKit;
import com.gt.mall.util.JedisUtil;
import com.gt.mall.util.PageUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 商品批发表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPifaServiceImpl extends BaseServiceImpl< MallPifaDAO,MallPifa > implements MallPifaService {

    private static Logger logger = LoggerFactory.getLogger( MallPifaServiceImpl.class );

    @Autowired
    private MallPifaApplyDAO mallPifaApplyDAO;

    @Autowired
    private MallPifaDAO mallPifaDAO;

    @Autowired
    private MallPifaPriceDAO mallPifaPriceDAO;

    @Autowired
    private MallPifaPriceService mallPifaPriceService;

    @Autowired
    private MallSearchKeywordDAO mallSearchKeywordDAO;

    @Autowired
    private MallSearchKeywordService mallSearchKeywordService;

    @Autowired
    private MallProductService mallProductService;

    @Override
    @Transactional( rollbackFor = Exception.class )
    public int addWholesaler( MallPifaApply pifaApply ) {
	int count = mallPifaApplyDAO.insert( pifaApply );
	return count;
    }

    @Override
    public MallPifaApply selectByPifaApply( MallPifaApply pifaApply ) {
	MallPifaApply mallPifaApply = mallPifaApplyDAO.selectByPifaApply( pifaApply );
	return mallPifaApply;
    }

    @Override
    public int updateSetWholesaler( Map< String,Object > map ) {
	int count = mallPifaApplyDAO.updateSetWholesaler( map );
	return count;
    }

    @SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
    public PageUtil wholesalerList( Map< String,Object > params ) {
	PageUtil page = null;
	params.put( "curPage", CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1
			: CommonUtil.toInteger( params.get( "curPage" ) ) );
	int pageSize = 10;
	int rowCount = mallPifaApplyDAO.count( params );//查询批发商总条数
	page = new PageUtil( CommonUtil.toInteger( params.get( "curPage" ) ),
			pageSize, rowCount, "mallWholesalers/wholesaleList.do" );
	params.put( "firstResult", pageSize
			* ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 ) );
	params.put( "maxResult", pageSize );
	//查询批发商列表
	List< Map< String,Object > > list = mallPifaApplyDAO.wholesalerList( params );
	List list1 = new ArrayList();
	for ( Map< String,Object > pifaMap : list ) {
	    //微信昵称转换
	    String nickname = CommonUtil.blob2String( pifaMap.get( "nickname" ) );
	    JSONObject obj = JSONObject.fromObject( pifaMap );
	    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
	    JSONObject jsonObj = JSONObject.fromObject( obj.get( "create_time" ) );
	    JSONObject jsonObj1 = JSONObject.fromObject( obj.get( "check_time" ) );
	    //申请时间转换
	    if ( jsonObj.containsKey( "time" ) ) {
		long time = Long.valueOf( ( jsonObj.get( "time" ).toString() ) );
		obj.put( "create_time", sdf.format( new Date( time ) ) );
	    }
	    //审核时间转换
	    if ( jsonObj1.containsKey( "time" ) ) {
		long time = Long.valueOf( ( jsonObj1.get( "time" ).toString() ) );
		obj.put( "check_time", sdf.format( new Date( time ) ) );
	    }
	    obj.put( "nickname", nickname );

	    String key = Constants.REDIS_KEY + "syncOrderCount";
	    String member_id = obj.get( "member_id" ).toString();
	    if ( JedisUtil.hExists( key, member_id ) ) {
		String str = JedisUtil.maoget( key, member_id );
		if ( CommonUtil.isNotEmpty( str ) ) {
		    JSONObject orderObj = JSONObject.fromObject( str );
		    if ( CommonUtil.isNotEmpty( orderObj.get( "num" ) ) )
			obj.put( "num", orderObj.get( "num" ) );
		    if ( CommonUtil.isNotEmpty( orderObj.get( "proPrice" ) ) )
			obj.put( "money", orderObj.get( "proPrice" ) );
		}
	    }

	    list1.add( obj );
	}
	page.setSubList( list1 );
	return page;
    }

    @Override
    public int updateStatus( Map< String,Object > params ) {
	return mallPifaApplyDAO.updateStatus( params );
    }

    @Override
    public PageUtil pifaProductList( Map< String,Object > params, List< Map< String,Object > > shoplist ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1
			: CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallPifaDAO.selectByCount( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mallWholesalers/index.do" );
	int firstNum = pageSize
			* ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断批发是否有数据
	    List< MallPifa > pifaList = mallPifaDAO.selectByPage( params );
	    if ( pifaList != null && pifaList.size() > 0 ) {
		for ( MallPifa pifa : pifaList ) {
		    //循环门店
		    for ( Map< String,Object > storeMap : shoplist ) {
			int shopId = CommonUtil.toInteger( storeMap.get( "id" ) );
			if ( shopId == pifa.getShopId() ) {
			    pifa.setShopName( storeMap.get( "sto_name" ).toString() );
			    break;
			}
		    }
		}
	    }
	    page.setSubList( pifaList );
	}

	return page;
    }

    /**
     * 通过批发id查询批发信息
     */
    @Override
    public Map< String,Object > selectPifaById( Integer id ) {
	Map< String,Object > map = mallPifaDAO.selectByPifaId( id );
	int pifaId = CommonUtil.toInteger( map.get( "id" ) );
	List< MallPifaPrice > priceList = mallPifaPriceDAO.selectPriceByGroupId( pifaId );
	map.put( "priceList", priceList );
	return map;
    }

    /**
     * 编辑批发
     *
     * @Title: editFreight
     */
    @Override
    @Transactional( rollbackFor = Exception.class )
    public int editPifa( Map< String,Object > pifaMap, int userId ) {
	int num = 0;
	boolean flag = false;
	int code = -1;
	int status = 0;
	if ( CommonUtil.isNotEmpty( pifaMap.get( "pifa" ) ) ) {
	    MallPifa pifa = com.alibaba.fastjson.JSONObject.parseObject( pifaMap.get( "pifa" ).toString(), MallPifa.class );
	    // 判断选择的商品是否已经存在未开始和进行中的批发中
	    List< MallPifa > buyList = mallPifaDAO.selectStartPifaByProId( pifa );
	    if ( buyList == null || buyList.size() == 0 ) {
		pifa.setUserId( userId );
		if ( CommonUtil.isNotEmpty( pifa.getId() ) ) {
		    // 判断本商品是否正在批发中
		    MallPifa buy = mallPifaDAO.selecPifaByIds( pifa.getId() );
		    if ( buy.getStatus() == 1 && buy.getJoinId() > 0 ) {// 正在进行批发的商品不能修改
			code = -2;
			status = buy.getStatus();
		    } else {
			MallPifa buyOld = mallPifaDAO.selectById( pifa.getId() );
			if ( !buyOld.getProductId().equals( pifa.getProductId() ) ) {// 用户更换了商品
			    flag = true;
			}
			num = mallPifaDAO.updateById( pifa );
		    }
		} else {
		    pifa.setCreateTime( new Date() );
		    num = mallPifaDAO.insert( pifa );
		}
		if ( CommonUtil.isNotEmpty( pifa.getId() ) ) {
		    //					String key = "hSeckill";
		    //					String field = pifa.getId().toString();
		    //					JedisUtil.map(key, field, pifa.getsNum()+"");
		    if ( status != 1 ) {
			mallPifaPriceService.editPifaPrice( pifaMap, pifa.getId(), flag );
		    }
		}
	    } else {
		code = 0;
	    }
	    if ( num > 0 ) {
		code = 1;
	    }
	}
	return code;
    }

    /**
     * 删除批发
     *
     * @Title: deleteFreight
     */
    @Override
    public boolean deletePifa( MallPifa pifa ) {
	int num = mallPifaDAO.updateById( pifa );
	if ( num > 0 ) {
	    return true;
	}
	return false;
    }

    /**
     * 根据店铺id查询批发信息
     */
    @Override
    public List< Map< String,Object > > selectgbPifaByShopId(
		    Map< String,Object > maps ) {
	List< Map< String,Object > > productList = mallPifaDAO
			.selectgbProductByShopId( maps );
	return productList;
    }

    /**
     * 获取店铺下所有的批发
     */
    @Override
    public List< Map< String,Object > > getPifaAll( Member member, Map< String,Object > maps ) {

	int shopid = 0;
	if ( CommonUtil.isNotEmpty( maps.get( "shopId" ) ) ) {
	    shopid = CommonUtil.toInteger( maps.get( "shopId" ) );
	}
	//新增搜索关键词
	mallSearchKeywordService.insertSeachKeyWord( member.getId(), shopid, maps.get( "proName" ) );

	List< Map< String,Object > > list = new ArrayList< Map< String,Object > >();// 存放店铺下的商品

	double discount = 1;// 商品折扣
	if ( CommonUtil.isNotEmpty( member ) ) {
	    discount = mallProductService.getMemberDiscount( "1", member );
	}
	maps.put( "status", 1 );
	List< Map< String,Object > > productList = mallPifaDAO
			.selectgbProductByShopId( maps );
	if ( productList != null && productList.size() > 0 ) {
	    for ( Map< String,Object > map2 : productList ) {
		String is_specifica = map2.get( "is_specifica" ).toString();// 是否有规格，1有规格，有规格取规格里面的值
		if ( is_specifica == "1" || is_specifica.equals( "1" ) ) {
		    //					map2.put("old_price", map2.get("inv_price"));
		    map2.put( "price", map2.get( "inv_price" ) );
		    String specifica_img_id = map2.get( "specifica_img_id" ).toString();
		    if ( specifica_img_id.equals( "0" ) ) {
			map2.put( "image_url", map2.get( "image_url" ) );
		    } else {
			map2.put( "image_url", map2.get( "specifica_img_url" ) );
		    }
		} else {
		    map2.put( "price", map2.get( "pro_price" ) );
		    //					map2.put("old_price", map2.get("pro_price"));
		    map2.put( "image_url", map2.get( "image_url" ) );
		}
		map2.put( "old_price", map2.get( "pro_cost_price" ) );
		String is_member_discount = map2.get( "is_member_discount" )
				.toString();// 商品是否参加折扣,1参加折扣
		if ( is_member_discount.equals( "1" ) ) {
					/*map2.put("price", Math.ceil((Double.parseDouble(map2.get(
							"price").toString()) * discount) * 100) / 100);*/
		}
		if ( CommonUtil.isNotEmpty( map2.get( "price" ) ) ) {
		    map2.put( "price", Math.ceil( ( Double.parseDouble( map2.get(
				    "price" ).toString() ) ) * 100 ) / 100 );
		}
		Date endTime = DateTimeKit.parse(
				map2.get( "endTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date startTime = DateTimeKit.parse(
				map2.get( "startTime" ).toString(), "yyyy-MM-dd HH:mm:ss" );
		Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(),
				"yyyy-MM-dd HH:mm:ss" );
		Date date = new Date();
		map2.put( "times", ( endTime.getTime() - nowTime.getTime() ) / 1000 );
		int isEnd = 1;
		if ( startTime.getTime() < date.getTime() && endTime.getTime() <= date.getTime() ) {
		    isEnd = -1;
		}
		map2.put( "isEnd", isEnd );
		int status = -1;
		if ( startTime.getTime() > date.getTime() && endTime.getTime() > date.getTime() ) {
		    status = 0;
		    map2.put( "startTimes", ( startTime.getTime() - nowTime.getTime() ) / 1000 );
		} else if ( startTime.getTime() <= date.getTime() && date.getTime() < endTime.getTime() ) {
		    status = 1;
		}
		map2.put( "status", status );
		list.add( map2 );
	    }
	}
	return list;
    }

    /**
     * 根据商品id查询秒杀信息和秒杀价格
     *
     * @return
     */
    @Override
    public MallPifa getPifaByProId( Integer proId, Integer shopId ) {
	MallPifa pifa = new MallPifa();
	pifa.setProductId( proId );
	pifa.setShopId( shopId );
	pifa = mallPifaDAO.selectBuyByProductId( pifa );
	if ( pifa != null && CommonUtil.isNotEmpty( pifa.getId() ) ) {
	    Date endTime = DateTimeKit.parse( pifa.getPfEndTime().toString(),
			    "yyyy-MM-dd HH:mm:ss" );
	    Date startTime = DateTimeKit.parse( pifa.getPfStartTime().toString(),
			    "yyyy-MM-dd HH:mm:ss" );
	    Date nowTime = DateTimeKit.parse( DateTimeKit.getDateTime(),
			    "yyyy-MM-dd HH:mm:ss" );
	    pifa.setTimes( ( endTime.getTime() - nowTime.getTime() ) / 1000 );
	    if ( pifa.getStatus()==null || pifa.getStatus() == 0 ) {
		pifa.setStartTimes( ( startTime.getTime() - nowTime.getTime() ) / 1000 );
	    }

	    List< MallPifaPrice > priceList = mallPifaPriceService.selectPriceByGroupId( pifa.getId() );
	    pifa.setPriceList( priceList );
	    return pifa;
	}
	return null;
    }

    @Override
    public int updateWholesaleApplay( MallPifaApply applay ) {
	return mallPifaApplyDAO.updateById( applay );
    }

    @Override
    public double getPifaPriceByProIds( boolean isPifa, int productId ) {
	if ( isPifa ) {
	    Map< String,Object > params = new HashMap<>();
	    params.put( "pfType", 1 );
	    params.put( "product_id", productId );
	    //查询商品是否已经加入批发
	    List< MallPifa > pifaList = mallPifaDAO.selectStartPiFaByProductId( params );
	    if ( pifaList != null && pifaList.size() > 0 && isPifa ) {
		MallPifa pifa = pifaList.get( 0 );
		if ( CommonUtil.isNotEmpty( pifa.getPfPrice() ) ) {
		    return CommonUtil.toDouble( pifa.getPfPrice() );
		}
	    }
	}
	return -1;
    }
}
