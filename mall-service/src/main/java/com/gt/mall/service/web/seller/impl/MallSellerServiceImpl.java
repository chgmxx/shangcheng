package com.gt.mall.service.web.seller.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.seller.*;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.seller.*;
import com.gt.mall.result.phone.product.PhoneProductDetailResult;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallPaySetService;
import com.gt.mall.service.web.seller.MallSellerMallsetService;
import com.gt.mall.service.web.seller.MallSellerOrderService;
import com.gt.mall.service.web.seller.MallSellerService;
import com.gt.mall.utils.*;
import com.gt.util.entity.param.wx.QrcodeCreateFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 超级销售员 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallSellerServiceImpl extends BaseServiceImpl< MallSellerDAO,MallSeller > implements MallSellerService {

    @Autowired
    private MallSellerDAO            mallSellerDAO;
    @Autowired
    private MallSellerOrderDAO       mallSellerOrderDAO;
    @Autowired
    private MallSellerIncomeDAO      mallSellerIncomeDAO;
    @Autowired
    private MallSellerOrderService   sellerOrderService;
    @Autowired
    private MallSellerSetDAO         mallSellerSetDAO;
    @Autowired
    private MallSellerJoinProductDAO mallSellerJoinProductDAO;
    @Autowired
    private MallPaySetService        mallPaySetService;
    @Autowired
    private MemberService            memberService;
    @Autowired
    private WxPublicUserService      wxPublicUserService;
    @Autowired
    private MallSellerMallsetService mallSellerMallsetService;

    /**
     * 查询客户订单的个数
     */
    @Override
    public int selectCountClientOrder( Map< String,Object > params ) {
	return mallSellerOrderDAO.selectCountClientOrder( params );
    }

    /**
     * 查询客户的个数
     */
    @Override
    public int selectCountMyClient( Map< String,Object > params ) {
	return mallSellerDAO.selectCountMyClient( params );
    }

    /**
     * 查询销售员的信息
     *
     * @param saleId 销售员id
     */
    @Override
    public Map< String,Object > selectSellerBySaleId( int saleId ) {
	return mallSellerDAO.selectSellerBySaleId( saleId );
    }

    /**
     * 查询累计收益
     */
    @Override
    public List< Map< String,Object > > selectTotalIncome( Map< String,Object > params ) {
	List< Map< String,Object > > mapList = mallSellerIncomeDAO.selectTotalIncome( params );
	if ( mapList != null && mapList.size() > 0 ) {
	    String memberIds = "";
	    for ( Map< String,Object > map : mapList ) {
		if ( CommonUtil.isNotEmpty( map.get( "buyer_user_id" ) ) ) {
		    memberIds += map.get( "buyer_user_id" ) + ",";
		}
	    }
	    if ( CommonUtil.isNotEmpty( memberIds ) ) {
		List< Map > memberList = memberService.findMemberByIds( memberIds, CommonUtil.toInteger( params.get( "busId" ) ) );
		if ( memberList != null && memberList.size() > 0 ) {
		    for ( Map< String,Object > map : mapList ) {
			String rMemberId = CommonUtil.toString( map.get( "buyer_user_id" ) );
			for ( Map< String,Object > member1 : memberList ) {
			    String memberId = CommonUtil.toString( member1.get( "id" ) );
			    if ( rMemberId.equals( memberId ) ) {
				map.put( "headimgurl", member1.get( "headimgurl" ) );
				map.put( "nickname", member1.get( "nickname" ) );
				break;
			    }

			}
		    }
		}
	    }
	}

	return mapList;
    }

    /**
     * 查询超级销售员的审核状态
     */
    @Override
    public int selectSellerStatusByMemberId( Member member, MallPaySet set ) {
	MallSeller seller = mallSellerDAO.selectSellerByMemberId( member.getId() );
	if ( CommonUtil.isNotEmpty( seller ) ) {
	    int status = seller.getCheckStatus();
	    if ( CommonUtil.isNotEmpty( set ) ) {
		if ( CommonUtil.isNotEmpty( set.getIsCheckSeller() ) ) {
		    if ( set.getIsCheckSeller().toString().equals( "0" ) && status != -2 && status != -1 ) {//不开启审核所有人都能看到批发价
			status = 1;
		    }
		}
	    }
	    if ( seller.getIsStartUse() == -1 ) {
		status = -3;
	    }
	    return status;
	}
	return -4;
    }

    /**
     * 查询销售员的信息
     */
    @Override
    public MallSeller selectSellerByMemberId( int memberId ) {
	return mallSellerDAO.selectSellerByMemberId( memberId );
    }

    /**
     * 查询销售员收益积分姐呢排行榜
     */
    @Override
    public Map< String,Object > selectSellerByBusUserId( Map< String,Object > params, int type, Member member ) {
	Map< String,Object > resultMap = new HashMap<>();
	if ( params.containsKey( "memberId" ) ) {
	    params.put( "refereesMemberId", params.get( "memberId" ) );
	}
	List< Map< String,Object > > countList = mallSellerDAO.selectSellerByBusUserId( params );

	int pageSize = 10;
	int count = countList.size();

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );

	PageUtil page = new PageUtil( curPage, pageSize, count, params.get( "url" ).toString() );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	List< Map< String,Object > > rankList = mallSellerDAO.selectSellerByBusUserId( params );

	String memberIds = "";
	for ( Map< String,Object > rankMap : rankList ) {
	    if ( CommonUtil.isNotEmpty( rankMap.get( "member_id" ) ) ) {
		memberIds += rankMap.get( "member_id" ) + ",";
	    }
	}
	if ( CommonUtil.isNotEmpty( memberIds ) ) {
	    List< Map > memberList = memberService.findMemberByIds( memberIds, member.getBusid() );
	    if ( memberList != null && memberList.size() > 0 ) {
		for ( Map< String,Object > rankMap : rankList ) {
		    String rMemberId = CommonUtil.toString( rankMap.get( "member_id" ) );
		    for ( Map< String,Object > member1 : memberList ) {
			String memberId = CommonUtil.toString( member1.get( "id" ) );
			if ( rMemberId.equals( memberId ) ) {
			    rankMap.put( "headimgurl", member1.get( "headimgurl" ) );
			    rankMap.put( "nickname", member1.get( "nickname" ) );
			    break;
			}

		    }
		}
	    }
	}

	if ( type == 2 ) {
	    if ( rankList != null && rankList.size() > 0 ) {
		for ( Map< String,Object > rankMap : rankList ) {
		    if ( CommonUtil.isNotEmpty( rankMap.get( "member_id" ) ) ) {
			Map< String,Object > map = new HashMap<>();
			Map< String,Object > orderMap = sellerOrderService.selectOrderByMemberId( map );
			if ( CommonUtil.isNotEmpty( orderMap ) ) {
			    rankMap.put( "det_pro_name", orderMap.get( "det_pro_name" ) );
			    rankMap.put( "product_speciname", orderMap.get( "product_speciname" ) );
			}
		    }
		}
	    }
	}
	int rank = 1;
	if ( type == 1 ) {
	    int i = 1;
	    if ( countList != null && countList.size() > 0 ) {
		for ( Map< String,Object > rankMap : rankList ) {
		    if ( CommonUtil.isNotEmpty( rankMap.get( "member_id" ) ) ) {
			if ( rankMap.get( "member_id" ).toString().equals( CommonUtil.toString( member.getId() ) ) ) {
			    rank = i;
			}
		    }
		    i++;
		}
	    }
	    resultMap.put( "rank", rank );
	}

	page.setSubList( rankList );

	resultMap.put( "page", page );
	return resultMap;
    }

    /**
     * 通过商家id查询销售规则
     */
    @Override
    public MallSellerSet selectByBusUserId( int busUserId ) {
	return mallSellerSetDAO.selectByBusUserId( busUserId );
    }

    /**
     * 从redis里获取销售员id
     */
    @Override
    public int getSaleMemberIdByRedis( Member member, int saleMemberId, HttpServletRequest request, int userid ) {
	String key = "mall_mallSaleMemberId_" + userid;
	if ( saleMemberId > 0 ) {
	    boolean isSellers = isSeller( saleMemberId );
	    if ( !isSellers ) {
		saleMemberId = 0;
	    } else {
		if ( CommonUtil.isNotEmpty( request ) ) {
		    MallSessionUtils.setSession( saleMemberId, request, key );
		} else {
		    JedisUtil.set( Constants.REDIS_KEY + key, saleMemberId + "", Constants.REDIS_SECONDS );
		}
	    }
	}
	if ( CommonUtil.isNotEmpty( request ) ) {
	    if ( CommonUtil.isNotEmpty( MallSessionUtils.getSession( request, "mall_mallSaleMemberId_" + userid ) ) ) {
		return CommonUtil.toInteger( MallSessionUtils.getSession( request, "mall_mallSaleMemberId_" + userid ) );
	    }
	} else {
	    if ( JedisUtil.exists( Constants.REDIS_KEY + key ) ) {
		return CommonUtil.toInteger( JedisUtil.get( Constants.REDIS_KEY + key ) );
	    }
	}
	return saleMemberId;
    }

    @Override
    public void setSaleMemberIdByRedis( Member member, int saleMemberId, HttpServletRequest request, int userid ) {
	if ( saleMemberId > 0 ) {
	    String key = "mall_mallSaleMemberId_" + userid;
	    if ( CommonUtil.isNotEmpty( request ) ) {
		MallSessionUtils.setSession( saleMemberId, request, key );
	    } else {
		JedisUtil.set( Constants.REDIS_KEY + key, saleMemberId + "", Constants.REDIS_SECONDS );
	    }
	}
    }

    @Override
    public void clearSaleMemberIdByRedis( Member member, HttpServletRequest request, int userid ) {
	String key = "mall_mallSaleMemberId_" + userid;
	if ( CommonUtil.isNotEmpty( request ) ) {
	    MallSessionUtils.removeSession( request, key );
	} else {
	    JedisUtil.del( Constants.REDIS_KEY + key );
	}
    }

    @Override
    public boolean isSeller( int saleMemberId ) {
	if ( saleMemberId == 0 ) {
	    return false;
	}
	Member member = memberService.findMemberById( saleMemberId, null );//查询销售员的用户信息
	MallPaySet set = mallPaySetService.selectByMember( member );
	int state = getSellerApplay( member, set );
	boolean isSeller = false;
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getIsSeller() ) ) {
		if ( set.getIsSeller().toString().equals( "1" ) ) {
		    if ( CommonUtil.isNotEmpty( set.getIsCheckSeller() ) ) {
			if ( set.getIsCheckSeller().toString().equals( "1" ) ) {
			    if ( state == 1 ) {
				isSeller = true;
			    }
			} else {
			    isSeller = true;
			}
		    } else {
			isSeller = true;
		    }
		    if ( state != 1 ) {
			isSeller = false;
		    }
		}
	    }
	}
	return isSeller;
    }

    @Override
    public int getSellerApplay( Member member, MallPaySet set ) {
	int status = -2;
	MallSeller s = new MallSeller();
	s.setMemberId( member.getId() );
	s.setBusUserId( member.getBusid() );
	MallSeller seller = mallSellerDAO.selectMallSeller( s );//查询是否已经参加销售员审核
	if ( CommonUtil.isNotEmpty( seller ) ) {
	    if ( CommonUtil.isNotEmpty( seller.getCheckStatus() ) ) {
		status = seller.getCheckStatus();
		if ( CommonUtil.isNotEmpty( set.getIsCheckSeller() ) ) {
		    if ( set.getIsCheckSeller().toString().equals( "0" ) ) {//不开启审核所有人都能看到商品佣金
			status = 1;
		    }
		} else {
		    status = 1;
		}
	    }
	    if ( seller.getIsStartUse() != 1 ) {
		status = -1;
	    }
	}
	return status;
    }

    /**
     * 保存或修改功能设置
     */
    @Override
    public Map< String,Object > saveOrUpdSellerSet( int busUserId, Map< String,Object > params ) {
	int count = 0;
	Map< String,Object > resultMap = new HashMap<>();
	if ( CommonUtil.isNotEmpty( params.get( "sellerSet" ) ) ) {
	    MallSellerSet sellerSet = (MallSellerSet) JSONObject.toJavaObject( JSONObject.parseObject( params.get( "sellerSet" ).toString() ), MallSellerSet.class );
	    sellerSet.setSellerRemark( CommonUtil.urlEncode( sellerSet.getSellerRemark() ) );
	    if ( CommonUtil.isEmpty( sellerSet.getId() ) ) {
		//判断用户是否已经保存了功能设置
		MallSellerSet set = mallSellerSetDAO.selectByBusUserId( busUserId );
		if ( CommonUtil.isNotEmpty( set ) ) {
		    if ( CommonUtil.isNotEmpty( set.getId() ) ) {
			sellerSet.setId( set.getId() );
		    }
		}
	    }
	    if ( CommonUtil.isNotEmpty( sellerSet.getId() ) ) {
		MallSellerSet mallSellerSet = mallSellerSetDAO.selectById( sellerSet.getId() );
		mallSellerSet.setIntegralReward( sellerSet.getIntegralReward() );
		mallSellerSet.setConsumeMoney( sellerSet.getConsumeMoney() );
		mallSellerSet.setWithdrawalType( sellerSet.getWithdrawalType() );
		mallSellerSet.setWithdrawalLowestMoney( sellerSet.getWithdrawalLowestMoney() );
		mallSellerSet.setWithdrawalMultiple( sellerSet.getWithdrawalMultiple() );
		mallSellerSet.setSellerRemark( sellerSet.getSellerRemark() );

		count = mallSellerSetDAO.updateAllColumnById( mallSellerSet );
	    } else {
		sellerSet.setBusUserId( busUserId );
		count = mallSellerSetDAO.insert( sellerSet );
	    }
	}
	if ( count > 0 ) {
	    resultMap.put( "flag", true );
	} else {
	    resultMap.put( "flag", false );
	    resultMap.put( "msg", "保存功能设置失败，请稍后重试" );
	}
	return resultMap;
    }

    @Override
    public PageUtil selectCheckSeller( int busUserId, Map< String,Object > params ) {
	params.put( "status", "0" );
	params.put( "busUserId", busUserId );
	int count = mallSellerDAO.selectCountByBusUserId( params );

	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );

	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallSellers/sellerCheckList.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	List< Map< String,Object > > sellerList = mallSellerDAO.selectPageCheckByBusUserId( params );

	//	sellerList = getSellerMemberNick( sellerList, busUserId, memberList );

	page.setSubList( sellerList );
	return page;
    }

    private List< Map< String,Object > > getSellerMemberNick( List< Map< String,Object > > sellerList, int busUserId, List< Map > memberList ) {
	if ( sellerList == null || sellerList.size() == 0 ) {
	    return null;
	}
	if ( memberList == null || memberList.size() == 0 ) {
	    StringBuilder memberIds = new StringBuilder();
	    for ( Map< String,Object > sellerMap : sellerList ) {
		Object obj = sellerMap.get( "member_id" );
		if ( memberIds == null || ( CommonUtil.isNotEmpty( obj ) && !memberIds.toString().contains( obj + "," ) ) ) {
		    memberIds.append( obj ).append( "," );
		}
	    }
	    memberIds = new StringBuilder( memberIds.substring( 0, memberIds.length() - 1 ) );

	    memberList = memberService.findMemberByIds( memberIds.toString(), busUserId );
	}

	if ( memberList != null && memberList.size() > 0 ) {
	    for ( Map< String,Object > sellerMap : sellerList ) {
		Object obj = sellerMap.get( "member_id" );
		for ( Map memberMap : memberList ) {
		    Object id = memberMap.get( "id" );
		    if ( obj.toString().equals( id.toString() ) ) {
			sellerMap.put( "nickname", memberMap.get( "nickname" ) );
			sellerMap.put( "phone", memberMap.get( "phone" ) );
			break;
		    }
		}
	    }
	}
	return sellerList;
    }

    @Override
    public boolean checkSeller( int busUserId, Map< String,Object > params, WxPublicUsers wxPublicUsers ) {
	int count = 0;
	if ( CommonUtil.isNotEmpty( params.get( "seller" ) ) ) {
	    MallSeller seller = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "seller" ).toString() ), MallSeller.class );
	    seller.setCheckTime( new Date() );
	    if ( CommonUtil.isNotEmpty( seller.getCheckStatus() ) ) {
		if ( seller.getCheckStatus() == 1 ) {
		    seller.setAddTime( new Date() );
		}
	    }
	    count = mallSellerDAO.updateById( seller );
	}
	if ( CommonUtil.isNotEmpty( params.get( "ids" ) ) ) {
	    String[] id = (String[]) JSONArray.toJSON( JSONArray.parseObject( params.get( "ids" ).toString() ) );
			/*params.put("ids", id);*/
	    if ( id != null && id.length > 0 ) {
		for ( String ids : id ) {
		    if ( CommonUtil.isNotEmpty( ids ) ) {
			MallSeller seller = new MallSeller();
			if ( CommonUtil.isNotEmpty( params.get( "checkStatus" ) ) ) {
			    if ( params.get( "checkStatus" ).toString().equals( "1" ) ) {
				seller.setAddTime( new Date() );
			    }
			    seller.setCheckTime( new Date() );
			    seller.setCheckStatus( CommonUtil.toInteger( params.get( "checkStatus" ) ) );
			}
			count = mallSellerDAO.updateById( seller );

		    }
		}
	    }
	    /*if(CommonUtil.isNotEmpty(params.get("checkStatus"))){
		    if(params.get("checkStatus").toString().equals("1")){
			    params.put("applyTime", new Date());
			    String ticket = insertTwoCode(scene_id, wxPublicUsers);
		    }
		    params.put("addTime", new Date());
	    }
	    count = mallSellerDao.batchUpdateSeller(params);*/
	}
	return count > 0;
    }

    public String insertTwoCode( String scene_id, WxPublicUsers wxPublicUsers ) {
	QrcodeCreateFinal createFinal = new QrcodeCreateFinal();
	createFinal.setScene_id( scene_id );
	createFinal.setPublicId( wxPublicUsers.getId() );
	return wxPublicUserService.qrcodeCreateFinal( createFinal );
    }

    @Override
    public PageUtil selectSellerPage( int busUserId, Map< String,Object > params ) {
	params.put( "status", "1" );
	params.put( "busUserId", busUserId );
	List< Map< String,Object > > countList = mallSellerDAO.selectPageSellerByBusUserId( params );
	int count = countList.size();

	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );

	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallSellers/sellerList.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	List< Map< String,Object > > sellerList = mallSellerDAO.selectPageSellerByBusUserId( params );

	page.setSubList( sellerList );
	return page;
    }

    @Override
    public int insertSelective( MallSeller seller, Member member ) {
	WxPublicUsers wxPublicUsers = wxPublicUserService.selectById( member.getPublicId() );
	if ( CommonUtil.isNotEmpty( wxPublicUsers ) ) {
	    String scene_id = member.getBusid() + "_" + System.currentTimeMillis() + "_3";//3代表商城
	    String ticket = insertTwoCode( scene_id, wxPublicUsers );
	    seller.setQrCodeTicket( ticket );
	    seller.setSceneKey( scene_id );
	    seller.setQrCodePath( "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket );
	}
	return mallSellerDAO.insert( seller );
    }

    @Override
    public MallSeller selectMallSeller( MallSeller seller ) {
	return mallSellerDAO.selectMallSeller( seller );
    }

    @Override
    public int updateSeller( MallSeller seller ) {
	return mallSellerDAO.updateById( seller );
    }

    @Override
    public PageUtil selectProductByShopId( Map< String,Object > params, List< Map< String,Object > > shoplist ) {
	int pageSize = 10;

	int curPage = CommonUtil.isEmpty( params.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( params.get( "curPage" ) );
	params.put( "curPage", curPage );
	int count = mallSellerJoinProductDAO.selectCountByPage( params );

	PageUtil page = new PageUtil( curPage, pageSize, count, "/mallSellers/joinProduct.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	params.put( "firstNum", firstNum );// 起始页
	params.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断团购是否有数据
	    List< Map< String,Object > > joinProductlist = mallSellerJoinProductDAO.selectByPage( params );
	    if ( shoplist != null && shoplist.size() > 0 && joinProductlist != null && joinProductlist.size() > 0 ) {
		for ( Map< String,Object > joinMap : joinProductlist ) {
		    int shopId = CommonUtil.toInteger( joinMap.get( "shop_id" ) );
		    for ( Map< String,Object > shopMap : shoplist ) {
			int shop_id = CommonUtil.toInteger( shopMap.get( "id" ) );
			if ( shop_id == shopId ) {
			    joinMap.put( "shopName", shopMap.get( "sto_name" ) );
			    break;
			}
		    }
		}
	    }
	    page.setSubList( joinProductlist );
	}
	return page;
    }

    /**
     * 保存或修改商品佣金设置
     */
    @Override
    public Map< String,Object > saveOrUpdSellerJoinProduct( int busUserId, Map< String,Object > params ) {
	int count = 0;
	Map< String,Object > resultMap = new HashMap<>();
	if ( CommonUtil.isNotEmpty( params.get( "joinProduct" ) ) ) {
	    MallSellerJoinProduct joinProduct = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "joinProduct" ).toString() ), MallSellerJoinProduct.class );
	    MallSellerJoinProduct jProduct = null;
	    if ( CommonUtil.isNotEmpty( joinProduct.getProductId() ) ) {
		jProduct = mallSellerJoinProductDAO.selectByProId( joinProduct.getProductId() );
	    }
	    if ( CommonUtil.isNotEmpty( joinProduct.getId() ) ) {
		if ( CommonUtil.isNotEmpty( jProduct ) ) {
		    if ( !jProduct.getId().toString().equals( joinProduct.getId().toString() ) ) {
			resultMap.put( "msg", "您选择的商品已经设置了商品佣金，请重新选择" );
			resultMap.put( "flag", false );
			return resultMap;
		    }
		}
		count = mallSellerJoinProductDAO.updateById( joinProduct );
	    } else {
		if ( CommonUtil.isNotEmpty( jProduct ) ) {
		    if ( CommonUtil.isNotEmpty( jProduct.getId() ) ) {
			resultMap.put( "msg", "您选择的商品已经设置了商品佣金，请重新选择" );
			resultMap.put( "flag", false );
			return resultMap;
		    }
		}
		joinProduct.setUserId( busUserId );
		joinProduct.setCreateTime( new Date() );
		count = mallSellerJoinProductDAO.insert( joinProduct );
	    }
	}
	if ( count > 0 ) {
	    resultMap.put( "flag", true );
	} else {
	    resultMap.put( "flag", false );
	}
	return resultMap;
    }

    @Override
    public Map< String,Object > selectJoinProductById( int id ) {
	return mallSellerJoinProductDAO.selectByIds( id );
    }

    @Override
    public Map< String,Object > updSellerJoinProduct( int busUserId, Map< String,Object > params ) {
	Map< String,Object > resultMap = new HashMap<>();
	int count = 0;
	if ( CommonUtil.isNotEmpty( params ) ) {
	    MallSellerJoinProduct joinProduct = JSONObject.toJavaObject( JSONObject.parseObject( params.toString() ), MallSellerJoinProduct.class );
	    count = mallSellerJoinProductDAO.updateById( joinProduct );
	}
	if ( count > 0 ) {
	    resultMap.put( "flag", true );
	} else {
	    resultMap.put( "flag", false );
	}
	return resultMap;
    }

    @Override
    public void delSaleMemberIdByRedis( Member member ) {
	String key = Constants.REDIS_KEY + "mallSaleMemberId";
	String field = member.getId().toString();
	if ( JedisUtil.hExists( key, field ) ) {
	    JedisUtil.hdel( key, field );
	}
    }

    /**
     * 添加销售员的销售情况
     */
    @Override
    public void insertSellerIncome( double commission, MallOrder order, MallOrderDetail detail, double totalPrice ) {
	MallSellerIncome income = new MallSellerIncome();
	income.setSaleMemberId( detail.getSaleMemberId() );
	if ( commission > 0 ) {
	    income.setIncomeCommission( BigDecimal.valueOf( commission ) );
	    income.setNoReceiveCommission( BigDecimal.valueOf( commission ) );
	    int incomeType = 1;
	    if ( order.getOrderPayWay() == 3 ) {
		incomeType = 2;
	    }
	    income.setIncomeType( incomeType );
	}
	income.setIncomeMoney( BigDecimal.valueOf( totalPrice ) );
	income.setIncomeTime( new Date() );
	income.setBuyerUserId( order.getBuyerUserId() );
	income.setOrderId( order.getId() );
	income.setOrderDetailId( detail.getId() );
	income.setIncomeType( 2 );
	income.setIsGet( -1 );
	List< MallSellerIncome > incomeList = mallSellerIncomeDAO.selectByIncome( income );
	if ( incomeList == null || incomeList.size() == 0 ) {
	    mallSellerIncomeDAO.insert( income );

	    //修改佣金
	    Map< String,Object > params = new HashMap<>();
	    if ( commission > 0 ) {
		params.put( "commission", commission );
	    }
	    params.put( "saleMoney", totalPrice );
	    params.put( "saleMemberId", detail.getSaleMemberId() );
	    params.put( "type", 1 );
	    mallSellerDAO.updateBySellerIncome( params );

	    MallSellerOrder sellerOrder = new MallSellerOrder();
	    sellerOrder.setBuyerUserId( order.getBuyerUserId() );
	    sellerOrder.setSellerUserId( order.getSellerUserId() );
	    sellerOrder.setOrderMoney( order.getOrderMoney() );
	    MallSeller seller = selectSellerByMemberId( detail.getSaleMemberId() );
	    if ( CommonUtil.isNotEmpty( seller ) ) {
		sellerOrder.setRefereesMemberId( seller.getRefereesMemberId() );
	    }
	    if ( CommonUtil.toString( detail.getSaleMemberId() ).equals( order.getBuyerUserId().toString() ) ) {
		sellerOrder.setRefereesMemberId( detail.getSaleMemberId() );
	    }
	    sellerOrder.setSaleMemberId( detail.getSaleMemberId() );
	    sellerOrder.setOrderId( order.getId() );
	    sellerOrder.setOrderDetailId( detail.getId() );
	    mallSellerOrderDAO.insert( sellerOrder );//添加销售订单
	}
    }

    @Transactional( rollbackFor = Exception.class, propagation = Propagation.SUPPORTS )
    @Override
    public Map< String,Object > sellerSendIntegral( Map< String,Object > params ) {

	Map< String,Object > resultMap = new HashMap<>();
	int memberId = 0;//关注会员id
	int refMemberId = 0;//推荐人id
	String scene_id = "";
	if ( CommonUtil.isNotEmpty( params ) ) {
	    if ( CommonUtil.isNotEmpty( params.get( "memberId" ) ) ) {
		memberId = CommonUtil.toInteger( params.get( "memberId" ) );
	    }
	    if ( CommonUtil.isNotEmpty( params.get( "scene_id" ) ) ) {
		scene_id = params.get( "scene_id" ).toString();
	    }
	}
	if ( memberId == 0 || scene_id.equals( "" ) ) {
	    resultMap.put( "flag", false );
	    resultMap.put( "errorMsg", "缺少参数" );
	    return resultMap;
	}
	Member member = memberService.findMemberById( memberId, null );
	//查询关注人信息
	MallSeller seller = mallSellerDAO.selectSellerBySecenId( scene_id );
	if ( CommonUtil.isNotEmpty( seller ) ) {
	    if ( CommonUtil.isNotEmpty( seller.getMemberId() ) ) {
		refMemberId = seller.getMemberId();//推荐人id
		//判断关注人是否已经是客户的粉丝
		MallSeller mallseller = mallSellerDAO.selectSellerByMemberId( memberId );
		MallSeller saleSelelr = new MallSeller();
		saleSelelr.setRefereesMemberId( seller.getMemberId() );
		saleSelelr.setIsFocusPublic( 1 );
		if ( CommonUtil.isEmpty( mallseller ) ) {
		    saleSelelr.setUserType( 0 );
		    saleSelelr.setBusUserId( seller.getBusUserId() );
		    saleSelelr.setMemberId( memberId );
		    saleSelelr.setCheckStatus( -2 );
		    saleSelelr.setAddTime( new Date() );
		    if ( CommonUtil.isEmpty( member.getNickname() ) ) {
			seller.setUserName( member.getNickname() );
		    }
		    if ( CommonUtil.isEmpty( member.getPhone() ) ) {
			seller.setTelephone( member.getPhone() );
		    }
		    mallSellerDAO.insert( saleSelelr );
		} else {
		    saleSelelr.setId( mallseller.getId() );
		    mallSellerDAO.updateById( saleSelelr );

		    if ( mallseller.getIsFocusPublic() == 1 || mallseller.getIsSendFocusIntegral() == 1 ) {
			resultMap.put( "flag", false );
			resultMap.put( "errorMsg", "已经关注过公众号，不能再送积分" );
			return resultMap;
		    }
		}
				/*if(seller.getCheckStatus() != -2){
					resultMap.put("flag", false);
					resultMap.put("errorMsg", "已经申请过销售员，不能再申请了");
					return resultMap;
				}*/
	    }
	}
	if ( refMemberId == 0 ) {
	    resultMap.put( "flag", false );
	    resultMap.put( "errorMsg", "没有推荐人，不用送积分" );
	    return resultMap;
	}
	double integral = 0;
	//判断推荐人是否是销售员，并且商家开启超级销售员
	boolean isSeller = isSeller( refMemberId );
	if ( !isSeller ) {
	    resultMap.put( "flag", false );
	    resultMap.put( "errorMsg", "推荐人还不是商家的销售员，或商家还未开启超级销售员" );
	    return resultMap;
	}
	//查询商家设置的关注送积分
	MallSellerSet sellerSet = mallSellerSetDAO.selectByBusUserId( seller.getBusUserId() );
	if ( CommonUtil.isNotEmpty( sellerSet ) ) {
	    if ( CommonUtil.isNotEmpty( sellerSet.getIntegralReward() ) ) {
		integral = CommonUtil.toDouble( sellerSet.getIntegralReward() );
	    }
	}
	if ( integral == 0 ) {
	    resultMap.put( "flag", false );
	    resultMap.put( "errorMsg", "商家还未设置关注送积分" );
	    return resultMap;
	}
	Map< String,Object > sellerMap = new HashMap<>();
	sellerMap.put( "incomeIntegral", integral );
	sellerMap.put( "type", 1 );
	sellerMap.put( "saleMemberId", refMemberId );
	int count = mallSellerDAO.updateBySellerIncome( sellerMap );//修改销售员的收益积分
	if ( count > 0 ) {
	    Map< String,Object > focusMap = new HashMap<>();
	    focusMap.put( "isSendFocusIntegral", 1 );
	    focusMap.put( "isFocusPublic", 1 );
	    focusMap.put( "saleMemberId", memberId );
	    mallSellerDAO.updateBySellerIncome( focusMap );//修改销售员的关注信息

	    count = mallSellerDAO.updateMember( sellerMap );//修改会员积分
	    //添加销售员的收益
	    MallSellerIncome income = new MallSellerIncome();
	    income.setSaleMemberId( refMemberId );
	    income.setIncomeIntegral( BigDecimal.valueOf( integral ) );
	    income.setIncomeTime( new Date() );
	    income.setBuyerUserId( memberId );
	    income.setIncomeType( 1 );
	    income.setIsGet( 1 );
	    mallSellerIncomeDAO.insert( income );

	    boolean isMember = memberService.isMember( refMemberId );//判断是否是会员
	    if ( isMember ) {
		Map< String,Object > jifenParams = new HashMap<>();
		jifenParams.put( "memberId", refMemberId );
		jifenParams.put( "jifen", integral );
		memberService.updateJifen( jifenParams );//新增积分记录
	    }
	}
	if ( count > 0 ) {
	    resultMap.put( "flag", true );
	}
	return resultMap;
    }

    @Override
    public void updateSellerIncome( MallOrderDetail detail ) {
	MallSellerIncome income = new MallSellerIncome();
	income.setOrderId( detail.getOrderId() );
	income.setOrderDetailId( detail.getId() );
	income.setSaleMemberId( detail.getSaleMemberId() );
	income.setIncomeType( 2 );
	List< MallSellerIncome > incomeList = mallSellerIncomeDAO.selectByIncome( income );
	if ( incomeList != null && incomeList.size() > 0 ) {
	    for ( MallSellerIncome sellerIncome : incomeList ) {
		income = new MallSellerIncome();
		income.setId( sellerIncome.getId() );
		income.setIsGet( -2 );
		mallSellerIncomeDAO.updateById( income );//修改收益状态
		double commission = 0;
		if ( CommonUtil.isNotEmpty( sellerIncome.getIncomeCommission() ) ) {
		    commission = CommonUtil.toDouble( sellerIncome.getIncomeCommission() );
		}
		//修改佣金
		Map< String,Object > params = new HashMap<>();
		if ( commission > 0 ) {
		    params.put( "commission", commission );
		}
		if ( CommonUtil.isNotEmpty( sellerIncome.getIncomeMoney() ) ) {
		    params.put( "saleMoney", sellerIncome.getIncomeMoney() );
		}
		params.put( "saleMemberId", detail.getSaleMemberId() );
		params.put( "type", 2 );
		mallSellerDAO.updateBySellerIncome( params );//修改销售额和佣金

		MallSellerOrder order = new MallSellerOrder();
		order.setOrderId( detail.getOrderId() );
		order.setOrderDetailId( detail.getId() );
		order.setSaleMemberId( detail.getSaleMemberId() );
		MallSellerOrder sellerOrder = mallSellerOrderDAO.selectBySellerOrder( order );
		if ( CommonUtil.isNotEmpty( sellerOrder ) ) {
		    MallSellerOrder updateOrder = new MallSellerOrder();
		    updateOrder.setId( sellerOrder.getId() );
		    updateOrder.setIsDelete( 1 );
		    mallSellerOrderDAO.updateById( updateOrder );//删除销售订单
		}
	    }
	}
    }

    @Override
    public void shareSellerIsSale( Member member, int saleMemberId, MallSeller seller ) {
	if ( CommonUtil.isNotEmpty( member ) ) {
	    MallSeller mallSeller = mallSellerDAO.selectSellerByMemberId( member.getId() );
	    if ( CommonUtil.isEmpty( mallSeller ) ) {
		MallSeller saleSelelr = new MallSeller();
		saleSelelr.setRefereesMemberId( saleMemberId );
		saleSelelr.setUserType( 0 );
		saleSelelr.setBusUserId( seller.getBusUserId() );
		saleSelelr.setMemberId( member.getId() );
		saleSelelr.setCheckStatus( -2 );
		saleSelelr.setAddTime( new Date() );
		if ( CommonUtil.isEmpty( member.getNickname() ) ) {
		    seller.setUserName( member.getNickname() );
		}
		if ( CommonUtil.isEmpty( member.getPhone() ) ) {
		    seller.setTelephone( member.getPhone() );
		}
		mallSellerDAO.insert( saleSelelr );
	    }
	}
    }

    /**
     * 从redis获取图片
     */
    /*public String getRedisSellerImage(String saleMemberId) {
	    String key = Constants.REDIS_KEY+"seller_promotion_img_"+saleMemberId;
	    if(JedisUtil.exists(key)){
		    String image = JedisUtil.get(key);

		    if(CommonUtil.isNotEmpty(image)){
			    String newimage = PropertiesUtil.getResourceUrl()+image;
			    newimage = URLConnectionDownloader.isConnect(newimage);//判断
			    if(CommonUtil.isEmpty(newimage)){
				    JedisUtil.del(key);
				    return null;
			    }
		    }
		    return image;
	    }
	    return null;
    }*/
    /*public void setRedisSellerImage(String saleMemberId,String imagePath) {
	    String key = Constants.REDIS_KEY+"seller_promotion_img_"+saleMemberId;
	    JedisUtil.set(key, imagePath, 60*30);
    }*/
    @Override
    public String createTempImage( Member member, MallSeller seller, int browerType ) {
	try {
	    MallSeller newSeller = new MallSeller();
	    //String imagesPaths = getRedisSellerImage(seller.getMemberId().toString());
	    String imagesPaths = seller.getPromotionPosterPath();
	    if ( browerType != 1 || CommonUtil.isEmpty( member.getPublicId() ) ) {//UC版
		imagesPaths = seller.getUcpromotionPosterPath();
	    }
	    if ( CommonUtil.isNotEmpty( imagesPaths ) ) {
		String path = URLConnectionDownloader.isConnect( PropertiesUtil.getResourceUrl() + imagesPaths );//判断
		if ( CommonUtil.isEmpty( path ) ) {
		    imagesPaths = null;
		}
	    }
	    boolean isUpHead = true;//修改用户头像
	    //判断销售员表保存的头像跟用户表保存的头像地址是否一致
	    String headPaths = "";
	    if ( CommonUtil.isNotEmpty( member.getHeadimgurl() ) ) {
		headPaths = URLConnectionDownloader.isConnect( member.getHeadimgurl() );//判断
	    }
	    if ( CommonUtil.isEmpty( headPaths ) ) {
		headPaths = PropertiesUtil.getHomeUrl() + "/images/mall/img/pt-detail2.jpg";
		String isConnet = URLConnectionDownloader.isConnect( headPaths );//判断
		if ( CommonUtil.isNotEmpty( isConnet ) ) {
		    member.setHeadimgurl( isConnet );
		}
	    }
	    if ( CommonUtil.isNotEmpty( seller.getHeadImagePath() ) ) {
		if ( seller.getHeadImagePath().equals( member.getHeadimgurl() ) ) {
		    isUpHead = false;
		}
	    }
	    MallSeller mallSeller = getSellerTwoCode( seller, member, browerType );
	    boolean isUpProPath = true;//修改二维码
	    if ( CommonUtil.isNotEmpty( mallSeller.getQrCodePath() ) ) {
		if ( CommonUtil.isNotEmpty( seller.getQrCodePath() ) ) {
		    if ( mallSeller.getQrCodePath().equals( seller.getQrCodePath() ) ) {
			isUpProPath = false;
		    }
		}
	    }

	    if ( CommonUtil.isNotEmpty( imagesPaths ) && !isUpHead && !isUpProPath ) {
		return imagesPaths;
	    }

	    String nowDate = DateTimeKit.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD );

	    String twoCodePath = mallSeller.getQrCodePath();
	    //String newPaths = PropertiesUtil.getResImagePath()+"/"+wx.getAppid()+"/"+PropertiesUtil.IMAGE_FOLDER_TYPE_28+"/"+nowDate;
	    String newPath = "/" + member.getPhone() + "/" + Constants.IMAGE_FOLDER_TYPE_15 + "/" + nowDate + "/";
	    twoCodePath = URLConnectionDownloader.downloadRqcode( twoCodePath, PropertiesUtil.getResImagePath() + newPath, 240, 240 );//下载二维码

	    String headPath = member.getHeadimgurl();
	    headPath = URLConnectionDownloader.downloadRqcode( headPath, PropertiesUtil.getResImagePath() + newPath, 90, 90 );//下载用户头像
	    if ( CommonUtil.isEmpty( headPath ) && CommonUtil.isNotEmpty( member.getHeadimgurl() ) ) {
		headPath = member.getHeadimgurl();
		headPath = URLConnectionDownloader.downloadRqcode( headPath, PropertiesUtil.getResImagePath() + newPath, 90, 90 );//下载用户头像
	    }

	    JSONArray arr = new JSONArray();
	    arr.add( twoCodePath );//存放二维码
	    if ( CommonUtil.isNotEmpty( headPath ) ) {
		arr.add( headPath );//存放用户头像
	    }
	    String[] logoPathStr = null;
	    List< String > arrList = (List< String >) JSONArray.toJSON( arr );
	    if ( arrList != null && arrList.size() > 0 ) {
		logoPathStr = new String[arrList.size()];
		for ( int i = 0; i < arrList.size(); i++ ) {
		    logoPathStr[i] = arrList.get( i );
		}
	    }

	    String path = PropertiesUtil.getHomeUrl() + "/images/mall/seller/tg-code.png";
	    path = URLConnectionDownloader.downloadRqcode( path, PropertiesUtil.getResImagePath() + newPath, 750, 1218 );//下载背景图片

	    String suffix = path.substring( path.lastIndexOf( "." ) + 1 );

	    //			String newPath = path.substring(0,path.lastIndexOf("/"));

	    newPath += System.currentTimeMillis() + "." + suffix;
	    //			String newPath=PropertiesUtil.getResImagePath()+"/temp/mall";//生成的图片存放到新的地址
	    //			newPath += "/"+nowDate+"/"+System.currentTimeMillis()+"."+suffix;

	    File file = new File( path );
	    path = file.getCanonicalPath();
	    ImageWaterMarkUtil iwm = new ImageWaterMarkUtil( path );

	    int[] w = new int[logoPathStr.length];//水印图片的宽度
	    int[] h = new int[logoPathStr.length];//水印图片的高度
	    int[] x = new int[2];//水印的x坐标
	    int[] y = new int[2];//水印的y左边
	    for ( int i = 0; i < logoPathStr.length; i++ ) {
		String logo = logoPathStr[i];

		ImageWaterMarkUtil iwmIcon = new ImageWaterMarkUtil( logo );

		w[i] = iwmIcon.getImgWidth();
		h[i] = iwmIcon.getImgHeight();
	    }

	    //设置二维码的坐标
	    x[0] = (int) ( iwm.getImgWidth() * 0.15 );
	    y[0] = (int) ( iwm.getImgHeight() * 0.59 );
	    //设置用户头像的坐标
	    x[1] = (int) ( iwm.getImgWidth() * 0.5 );
	    y[1] = (int) ( iwm.getImgHeight() * 0.59 );

	    Float myfloat;
	    try {
		myfloat = (float) ( 100 / 100 );
	    } catch ( Exception e ) {
		myfloat = null;
	    }
	    iwm.markImageByIconMoreImage( logoPathStr, path, 0, myfloat, x, y, w, h, PropertiesUtil.getResImagePath() + newPath );//生成新的图片

	    //删除下载的二维码、用户头像和背景图片
	    if ( logoPathStr != null && logoPathStr.length > 0 ) {
		for ( String aLogoPathStr : logoPathStr ) {
		    File files = new File( aLogoPathStr );
		    if ( files.exists() ) {
			boolean flag = files.delete();//删除水印图片
			logger.info( "删除水印图片：" + flag );
		    }
		}
	    }
	    File pathFile = new File( path );
	    if ( pathFile.exists() ) {
		boolean flag = pathFile.delete();//删除背景图片
		logger.info( "删除背景图片：" + flag );
	    }
	    newPath = "/image/" + newPath;
	    //setRedisSellerImage(seller.getMemberId().toString(), newPath);
	    newSeller.setId( seller.getId() );
	    if ( isUpHead ) {
		newSeller.setHeadImagePath( member.getHeadimgurl() );
	    }
	    if ( browerType != 1 || CommonUtil.isEmpty( member.getPublicId() ) ) {//UC版
		newSeller.setUcpromotionPosterPath( newPath );
	    } else {//微信版
		newSeller.setPromotionPosterPath( newPath );
	    }
	    mallSellerDAO.updateById( newSeller );
	    return newPath;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public MallSeller getSellerTwoCode( MallSeller seller, Member member, int browerType ) {

	if ( browerType != 1 || CommonUtil.isEmpty( member.getPublicId() ) ) {//UC版
	    if ( CommonUtil.isEmpty( seller.getUcqrCodePath() ) ) {
		String url = PropertiesUtil.getHomeUrl() + "/phoneSellers/" + member.getId() + "/79B4DE7C/mallIndex.do?uId=" + member.getBusid();
		String nowDate = DateTimeKit.getDateTime( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD );
		String codePath = QRcodeKit
				.buildQRcode( url, PropertiesUtil.getResImagePath() + "/" + member.getPhone() + "/" + Constants.IMAGE_FOLDER_TYPE_15 + "/" + nowDate + "/", 200,
						200 );
		codePath = PropertiesUtil.getResourceUrl() + codePath.split( "upload/" )[1];
		MallSeller mallSeller = new MallSeller();
		//mallSeller.setQrCodePath(codePath);
		mallSeller.setUcqrCodePath( codePath );
		mallSeller.setId( seller.getId() );
		int count = mallSellerDAO.updateById( mallSeller );
		if ( count > 0 ) {
		    seller.setUcqrCodePath( codePath );
		}
	    }
	    seller.setQrCodePath( seller.getUcqrCodePath() );
	} else if ( CommonUtil.isEmpty( seller.getQrCodeTicket() ) && browerType == 1 && CommonUtil.isNotEmpty( member.getPublicId() ) ) {//微信版
	    WxPublicUsers wxPublicUsers = wxPublicUserService.selectById( member.getPublicId() );
	    String newimage = seller.getQrCodePath();
	    newimage = URLConnectionDownloader.isConnect( newimage );//判断
	    if ( CommonUtil.isNotEmpty( wxPublicUsers ) && CommonUtil.isEmpty( newimage ) ) {
		String scene_id = member.getBusid() + "_" + System.currentTimeMillis() + "_3";//3代表商城
		String ticket = insertTwoCode( scene_id, wxPublicUsers );
		MallSeller mallSeller = new MallSeller();
		mallSeller.setQrCodeTicket( ticket );
		mallSeller.setSceneKey( scene_id );
		mallSeller.setQrCodePath( "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket );
		mallSeller.setId( seller.getId() );
		int count = mallSellerDAO.updateById( mallSeller );
		if ( count > 0 ) {
		    seller.setQrCodeTicket( ticket );
		    seller.setSceneKey( scene_id );
		    seller.setQrCodePath( "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + ticket );
		}
	    }
	}
	return seller;
    }

    public static void main( String[] args ) throws Exception {

	/*try {
	      URL url = new URL("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHT8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyZERDdUJHdEhmZzIxMDAwMHcwN3MAAgQ5g1xYAwQAAAAA");
	      // 返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
              URLConnection uc = url.openConnection();
              // 打开的连接读取的输入流。
              InputStream in = uc.getInputStream();
              System.out.println(in.);


        	  String state = URLConnectionDownloader.isConnect("https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHT8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyZERDdUJHdEhmZzIxMDAwMHcwN3MAAgQ5g1xYAwQAAAAA");
		  System.out.println(state);

	} catch ( Exception e ) {
	    System.out.println( "没有" );
	}*/

		/*try {
    		String nowDate = DateTimeKit.getDateTime(new Date(), DateTimeKit.DEFAULT_DATE_FORMAT_YYYYMMDD);
    		String twoCodePath = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=gQHT8DwAAAAAAAAAAS5odHRwOi8vd2VpeGluLnFxLmNvbS9xLzAyZERDdUJHdEhmZzIxMDAwMHcwN3MAAgQ5g1xYAwQAAAAA";
    		String newPaths = PropertiesUtil.getResImagePath()+"/temp/mall/"+nowDate;
    		twoCodePath = URLConnectionDownloader.downloadRqcode(twoCodePath,newPaths,240,240);

    		String headPath = "http://wx.qlogo.cn/mmopen/ajNVdqHZLLCCf4dqcCCe7dIRIOpVZvgQ5NZySGgPfsibxNiaQhunnusJMGfT7V5OZW0hvWCd6BdsE2ubLHibgVcOg/0";
    		headPath = URLConnectionDownloader.downloadRqcode(headPath,newPaths,90,90);
    		System.out.println("headPath:"+headPath);

    		String path = "E:/images/bg-poster.png";

    		String suffix = path.substring(path.lastIndexOf(".") + 1);

//    		String newPath = path.substring(0,path.lastIndexOf("/"));
    		String newPath=PropertiesUtil.getResImagePath()+"/temp/mall/"+nowDate;
    		newPath += "/"+System.currentTimeMillis()+"."+suffix;

    		System.out.println(newPath);

    		//System.out.println(newPath);

    		String[] logoPaths = new String[]{
    				twoCodePath,headPath
    		};
    		//String logoPath = "E:/images/showqrcode.png";
			ImageWaterMarkUtil iwm = new ImageWaterMarkUtil(path);

    		int[] w = new int[logoPaths.length];//水印图片的宽度
			int[] h = new int[logoPaths.length];//水印图片的高度
			int[] x = new int[2];//水印的x坐标
			int[] y = new int[2];//水印的y左边
    		for (int i = 0; i < logoPaths.length; i++) {
				String logo = logoPaths[i];
				System.out.println("logo:"+logo);

				ImageWaterMarkUtil iwmIcon = new ImageWaterMarkUtil(logo);
				double a = (iwm.getImgWidth()*iwm.getImgHeight())/10;

				double mianji = iwmIcon.getImgWidth()*iwmIcon.getImgHeight();
				w[i] = Integer.valueOf((int) (a * (Double.valueOf(iwmIcon.getImgWidth())/mianji)))/2;
				h[i] =  Integer.valueOf((int) (a * (Double.valueOf(iwmIcon.getImgHeight())/mianji)))/2;

				w[i] = iwmIcon.getImgWidth();
				h[i] = iwmIcon.getImgHeight();

//				if(i == 0){
//					w[i] = w[i]/2;
//					h[i] = h[i]/2;
//				}
				System.out.println("w:"+w[i]);
				System.out.println("h:"+h[i]);

				//w[i] = Integer.valueOf((int) (a * (Double.valueOf(iwmIcon.getImgWidth())/mianji)))/2;
				//h[1] =  Integer.valueOf((int) (a * (Double.valueOf(iwmIcon.getImgHeight())/mianji)))/2;
//				int w = (int) iwm.getImgWidth()/6;
//				int h = (int) ((int) w*((double)iwmIcon.getImgHeight()/iwmIcon.getImgWidth()));


			}
    		//System.out.println(w);
			//System.out.println(h);

    		x[0] = (int) (iwm.getImgWidth()*0.13);
			y[0] = (int) (iwm.getImgHeight()*0.54);

			x[1] = (int) (iwm.getImgWidth()*0.5);
			y[1] = (int) (iwm.getImgHeight()*0.54);


			Float myfloat = null;
			try {
				myfloat = new Float(100/100);
			} catch (Exception e) {
				myfloat = null;
			}
			System.out.println(x[0]);
			System.out.println(y[0]);
			iwm.markImageByIconMoreImage(logoPaths, path, 0, myfloat, x, y, w, h,newPath);

//			int[] x = new int[1];
//			int[] y = new int[1];
//			x[0] = (int) (iwm.getImgWidth()*0.3);
//		   	y[0] = (int) (iwm.getImgHeight()*0.5);
//			iwm.markImageByTextMore("F:/1110104P8-4-22.jpg", "多粉 http://www.duofriend.com", Color.RED, new Font("宋体", Font.PLAIN, 20), 0, (float) 0.7, x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public MallSeller mergeData( MallSeller seller, Member member ) {
	Map< String,Object > params = new HashMap<>();
	if ( CommonUtil.isNotEmpty( member.getOldid() ) ) {
	    List< String > lists = new ArrayList<>();
	    for ( String oldMemberId : member.getOldid().split( "," ) ) {
		if ( CommonUtil.isNotEmpty( oldMemberId ) && !oldMemberId.equals( "0" ) ) {
		    lists.add( oldMemberId );
		}
	    }
	    params.put( "oldMemberIds", lists );
	    if ( lists != null && lists.size() > 0 ) {
		MallSeller newSeller = mallSellerDAO.selectDataByOldMemberId( params );
		if ( CommonUtil.isNotEmpty( newSeller ) ) {
		    newSeller.setId( seller.getId() );
		    newSeller.setIsMergeData( 1 );
		    int count = mallSellerDAO.updateById( newSeller );
		    if ( count > 0 ) {
			seller = mallSellerDAO.selectById( seller.getId() );
			params.put( "noUpd", member.getId() );
			mallSellerDAO.updateDataByOldMemberId( params );
		    }
		}
	    }

	}
	return seller;
    }

    public PhoneProductDetailResult getSeller( PhoneProductDetailResult result, int saleMemberId, int busId, int productId, String view, Member member ) {
	if ( saleMemberId > 0 && busId > 0 ) {
	    setSaleMemberIdByRedis( member, saleMemberId, null, busId );
	}
	saleMemberId = getSaleMemberIdByRedis( member, saleMemberId, null, busId );
	if ( saleMemberId > 0 ) {
	    MallSeller mallSeller = selectSellerByMemberId( saleMemberId );//查询销售员的信息

	    if ( saleMemberId > 0 && CommonUtil.isNotEmpty( mallSeller ) ) {//分享的用户 判断是否是销售员
		shareSellerIsSale( member, saleMemberId, mallSeller );
	    }
	}
	if ( saleMemberId > 0 || view.equals( "show" ) ) {
	    //查询销售商品信息
	    result = mallSellerMallsetService.selectSellerProduct( productId, saleMemberId, result, view, member );
	}
	return result;
    }

}
