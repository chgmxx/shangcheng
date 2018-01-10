package com.gt.mall.service.web.basic.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.dao.basic.MallTakeTheirDAO;
import com.gt.mall.dao.basic.MallTakeTheirTimeDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.basic.MallTakeTheirTime;
import com.gt.mall.service.inter.wxshop.WxShopService;
import com.gt.mall.service.web.basic.MallImageAssociativeService;
import com.gt.mall.service.web.basic.MallTakeTheirService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.PageUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 到店自提表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallTakeTheirServiceImpl extends BaseServiceImpl< MallTakeTheirDAO,MallTakeTheir > implements MallTakeTheirService {

    private Logger logger = Logger.getLogger( MallTakeTheirServiceImpl.class );

    @Autowired
    private MallTakeTheirDAO            mallTakeTheirDAO;
    @Autowired
    private MallTakeTheirTimeDAO        takeTheirTimeDAO;
    @Autowired
    private MallImageAssociativeService imageAssociativeService;
    @Autowired
    private MallImageAssociativeDAO     imageAssociativeDAO;
    @Autowired
    private MallPaySetDAO               paySetDAO;
    @Autowired
    private WxShopService               wxShopService;

    @Override
    public PageUtil selectByUserId( Map< String,Object > param ) {
	int pageSize = 10;
	int curPage = CommonUtil.isEmpty( param.get( "curPage" ) ) ? 1 : CommonUtil.toInteger( param.get( "curPage" ) );
	param.put( "curPage", curPage );

	Wrapper< MallTakeTheir > pageWrapper = new EntityWrapper<>();
	pageWrapper.where( "is_delete = 0 and user_id = {0}", param.get( "userId" ) );
	int count = mallTakeTheirDAO.selectCount( pageWrapper );

	PageUtil page = new PageUtil( curPage, pageSize, count, "mFreight/takeindex.do" );
	int firstNum = pageSize * ( ( page.getCurPage() <= 0 ? 1 : page.getCurPage() ) - 1 );
	param.put( "firstNum", firstNum );// 起始页
	param.put( "maxNum", pageSize );// 每页显示商品的数量

	if ( count > 0 ) {// 判断上门自提是否有数据
	    List< MallTakeTheir > takeList = mallTakeTheirDAO.selectTakeTheirList( param );
	    page.setSubList( takeList );
	}
	return page;
    }

    @Override
    public boolean deleteTake( Map< String,Object > params ) {
	boolean flag = false;
	if ( params != null ) {
	    MallTakeTheir take = JSONObject.toJavaObject( JSONObject.parseObject( JSON.toJSONString( params ) ), MallTakeTheir.class );
	    take.setIsDelete( 1 );
	    int count = mallTakeTheirDAO.updateById( take );
	    if ( count > 0 ) {
		flag = true;
	    }
	}
	return flag;
    }

    @Override
    public boolean editTake( Map< String,Object > params, BusUser user ) {
	Integer code = -1;
	if ( CommonUtil.isNotEmpty( params ) ) {
	    MallTakeTheir take = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "obj" ).toString() ), MallTakeTheir.class );
	    if ( CommonUtil.isNotEmpty( take ) ) {
		if ( CommonUtil.isNotEmpty( take.getId() ) ) {
		    code = mallTakeTheirDAO.updateById( take );
		} else {
		    take.setUserId( user.getId() );
		    take.setCreateTime( new Date() );
		    code = mallTakeTheirDAO.insert( take );
		}
		if ( take.getId() > 0 ) {
		    // 添加或修改图片
		    imageAssociativeService.insertUpdBatchImage( params, take.getId() );
		    editTakeTheirTime( params, take.getId() );
		}
	    }
	}
	if ( code > 0 ) {
	    return true;
	} else {
	    return false;
	}
    }

    @Override
    public boolean newEditTake( Map< String,Object > params, BusUser user ) {
	Integer code = -1;
	if ( CommonUtil.isNotEmpty( params ) ) {
	    MallTakeTheir take = JSONObject.toJavaObject( JSONObject.parseObject( params.get( "take" ).toString() ), MallTakeTheir.class );
	    if ( CommonUtil.isNotEmpty( take ) ) {
		List< Map > cityList = wxShopService.queryBasisCityIds( take.getVisitProvinceId() + "," + take.getVisitCityId() + "," + take.getVisitAreaId() );
		String visitAddressDetail = "";
		if ( cityList != null && cityList.size() > 0 ) {
		    for ( Map map : cityList ) {
			String cityName = CommonUtil.toString( map.get( "city_name" ) );
			visitAddressDetail += cityName;
		    }
		}
		if ( take.getVisitAddress().contains( visitAddressDetail ) ) {
		    String temp = take.getVisitAddress();
		    take.setVisitAddress( take.getVisitAddress().replace( visitAddressDetail, "" ) );
		    visitAddressDetail = temp;
		} else {
		    visitAddressDetail += take.getVisitAddress();
		}

		take.setVisitAddressDetail( visitAddressDetail );
		if ( CommonUtil.isNotEmpty( take.getId() ) ) {
		    code = mallTakeTheirDAO.updateById( take );
		} else {
		    take.setUserId( user.getId() );
		    take.setCreateTime( new Date() );
		    code = mallTakeTheirDAO.insert( take );
		}
		if ( take.getId() > 0 ) {
		    // 添加或修改图片
		    imageAssociativeService.newInsertUpdBatchImage( params, take.getId(), 3 );
		    newEditTakeTheirTime( params, take.getId() );
		}
	    }
	}
	if ( code > 0 ) {
	    return true;
	} else {
	    return false;
	}
    }

    private void newEditTakeTheirTime( Map< String,Object > params, int takeId ) {
	Wrapper< MallTakeTheirTime > prowrapper = new EntityWrapper<>();
	prowrapper.where( "take_id={0} and is_delete = 0 ", takeId );
	List< MallTakeTheirTime > takeTheirTimes = takeTheirTimeDAO.selectList( prowrapper );

	// 添加自提点接待时间
	if ( !CommonUtil.isEmpty( params.get( "timeList" ) ) ) {
	    List< MallTakeTheirTime > timeList = JSONArray.parseArray( params.get( "timeList" ).toString(), MallTakeTheirTime.class );
	    if ( timeList != null && timeList.size() > 0 ) {
		for ( MallTakeTheirTime time : timeList ) {
		    if ( CommonUtil.isEmpty( time.getId() ) ) {
			time.setTakeId( takeId );
			time.setCreateTime( new Date() );
			takeTheirTimeDAO.insert( time );
		    } else {
			if ( takeTheirTimes != null ) {
			    int timeId = time.getId();
			    for ( MallTakeTheirTime theirTime : takeTheirTimes ) {
				if ( theirTime.getId() == timeId ) {
				    takeTheirTimes.remove( theirTime );// 移除已经存在自提点接待时间
				    break;
				}
			    }
			}
			takeTheirTimeDAO.updateById( time );
		    }
		}

	    }
	}
	//删除自提点接待时间
	if ( takeTheirTimes != null && takeTheirTimes.size() > 0 ) {
	    for ( MallTakeTheirTime theirTime : takeTheirTimes ) {
		theirTime.setIsDelete( 1 );
		takeTheirTimeDAO.updateById( theirTime );
	    }
	}
    }

    private void editTakeTheirTime( Map< String,Object > params, int takeId ) {
	// 逻辑删除自提点接待时间
	if ( !CommonUtil.isEmpty( params.get( "deltimeList" ) ) ) {
	    List< MallTakeTheirTime > timeList = JSON.parseArray( params.get( "deltimeList" ).toString(), MallTakeTheirTime.class );
	    if ( timeList != null && timeList.size() > 0 ) {
		for ( MallTakeTheirTime time : timeList ) {
		    takeTheirTimeDAO.updateById( time );
		}
	    }
	}
	// 添加自提点接待时间
	if ( !CommonUtil.isEmpty( params.get( "timeList" ) ) ) {
	    List< MallTakeTheirTime > timeList = JSONArray.parseArray( params.get( "timeList" ).toString(), MallTakeTheirTime.class );
	    if ( timeList != null && timeList.size() > 0 ) {
		for ( MallTakeTheirTime time : timeList ) {
		    time.setTakeId( takeId );
		    time.setCreateTime( new Date() );
		    takeTheirTimeDAO.insert( time );
		}

	    }
	}
    }

    @Override
    public List< MallTakeTheir > selectListByUserId( Map< String,Object > param ) {
	return mallTakeTheirDAO.selectTakeTheirList( param );
    }

    @Override
    public MallTakeTheir selectById( Map< String,Object > params ) {
	MallTakeTheir take = mallTakeTheirDAO.selectByIds( params );

	params.put( "assType", 3 );
	params.put( "assId", params.get( "id" ) );
	// 查询商品图片
	List< MallImageAssociative > imageList = imageAssociativeDAO.selectImageByAssId( params );
	take.setImageList( imageList );

	take.setTimeList( takeTheirTimeDAO.selectByTakeId( take.getId() ) );

	return take;
    }

    @Override
    public boolean isTakeTheirByUserId( int userId ) {
	MallPaySet paySet = new MallPaySet();
	paySet.setUserId( userId );
	// 通过用户id查询商户是否允许买家上门自提
	MallPaySet set = paySetDAO.selectOne( paySet );
	if ( CommonUtil.isNotEmpty( set ) ) {
	    if ( CommonUtil.isNotEmpty( set.getIsTakeTheir() ) ) {
		if ( set.getIsTakeTheir().toString().equals( "1" ) ) {// 允许买家上门自提
		    Map< String,Object > param = new HashMap< String,Object >();
		    param.put( "userId", userId );
		    int count = mallTakeTheirDAO.selectCountByBusUserId( param );
		    return count > 0;
		}
	    }
	}
	return false;
    }

    @Override
    public List< Map< String,Object > > isTakeTheirByUserIdList( List< MallPaySet > setList, String provincesId ) {
	if ( setList == null || setList.size() == 0 ) {
	    return null;
	}
	List< Integer > userIdList = new ArrayList<>();
	for ( MallPaySet mallPaySet : setList ) {
	    if ( CommonUtil.isNotEmpty( mallPaySet.getIsTakeTheir() ) ) {
		if ( mallPaySet.getIsTakeTheir().toString().equals( "1" ) ) {// 允许买家上门自提
		    if ( !userIdList.contains( mallPaySet.getUserId() ) ) {
			userIdList.add( mallPaySet.getUserId() );
		    }
		}
	    }
	}
	if ( userIdList.size() == 0 ) {
	    return null;
	}
	Wrapper< MallTakeTheir > wrapper = new EntityWrapper<>();
	wrapper.setSqlSelect( "user_id,id as takeId,is_store_pay as isStorePay, visit_address_detail as visitAddressDetail" );
	wrapper.in( "user_id", userIdList ).andNew( "is_delete = 0" );
	if ( CommonUtil.isNotEmpty( provincesId ) && CommonUtil.toInteger( provincesId ) > 0 ) {
	    wrapper.in( "visit_province_id", provincesId );
	}
	if ( userIdList.size() > 1 ) {
	    wrapper.groupBy( "user_id" );
	}
	List< Map< String,Object > > mallTakeTheirList = mallTakeTheirDAO.selectMaps( wrapper );
	if ( mallTakeTheirList == null || mallTakeTheirList.size() == 0 ) {
	    return null;
	}
	List< Map< String,Object > > list = new ArrayList<>();
	for ( Map< String,Object > map : mallTakeTheirList ) {
	    list.add( map );
	}
	return list;

    }

    @Override
    public List< MallTakeTheir > selectByBusUserId( Map< String,Object > map ) {
	return mallTakeTheirDAO.selectByUserId( map );
    }
}
