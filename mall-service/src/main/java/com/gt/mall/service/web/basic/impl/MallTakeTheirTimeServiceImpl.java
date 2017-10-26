package com.gt.mall.service.web.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallTakeTheirDAO;
import com.gt.mall.dao.basic.MallTakeTheirTimeDAO;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.entity.basic.MallTakeTheirTime;
import com.gt.mall.service.web.basic.MallTakeTheirTimeService;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.DateTimeKit;
import com.gt.mall.utils.MallTakeComparatorUtil;
import com.gt.mall.utils.WeekDayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 自提点接待时间表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallTakeTheirTimeServiceImpl extends BaseServiceImpl< MallTakeTheirTimeDAO,MallTakeTheirTime > implements MallTakeTheirTimeService {

    private Logger logger = Logger.getLogger( MallTakeTheirTimeServiceImpl.class );

    @Autowired
    private MallTakeTheirDAO     mallTakeTheirDAO;
    @Autowired
    private MallTakeTheirTimeDAO takeTheirTimeDAO;

    @Override
    public MallTakeTheir selectDefaultTakeByUserId( int userId, int loginCity, int takeId ) {
	List< MallTakeTheirTime > takeTimeList = new ArrayList<>();
	MallTakeTheir take = new MallTakeTheir();
	Map< String,Object > map = new HashMap<>();
	map.put( "userId", userId );
	if ( takeId == 0 ) {
	    map.put( "provinceId", loginCity );
	} else {
	    map.put( "takeId", takeId );
	}
	// 根据当前的省市查询默认的地址
	List< MallTakeTheir > takeList = mallTakeTheirDAO.selectByUserId( map );
	if ( takeList == null || takeList.size() == 0 ) {
	    map.remove( "provinceId" );
	    takeList = mallTakeTheirDAO.selectByUserId( map );
	    if ( takeList != null && takeList.size() > 0 ) {
		take = takeList.get( 0 );
	    }
	} else {
	    take = takeList.get( 0 );
	}
	if ( take != null ) {
	    // 获取上门自提的时间
	    List< MallTakeTheirTime > timeList = takeTheirTimeDAO.selectByTakeId( take.getId() );
	    if ( timeList != null && timeList.size() > 0 ) {
		for ( MallTakeTheirTime mallTakeTheirTime : timeList ) {
		    if ( CommonUtil.isNotEmpty( mallTakeTheirTime.getVisitDays() ) ) {
			takeTimeList.addAll( getTimes( mallTakeTheirTime ) );
		    } else {
			takeTimeList.add( mallTakeTheirTime );
		    }
		}
		Collections.sort( takeTimeList, new MallTakeComparatorUtil() );
		take.setTimeList( takeTimeList );
	    }
	}
	return take;
    }

    @Override
    public List< MallTakeTheirTime > selectTakeTheirTime( int takeId ) {
	List< MallTakeTheirTime > takeTimeList = new ArrayList<>();
	// 获取上门自提的时间
	List< MallTakeTheirTime > timeList = takeTheirTimeDAO.selectByTakeId( takeId );
	if ( timeList != null && timeList.size() > 0 ) {
	    for ( MallTakeTheirTime mallTakeTheirTime : timeList ) {
		if ( CommonUtil.isNotEmpty( mallTakeTheirTime.getVisitDays() ) ) {
		    takeTimeList.addAll( getTimes( mallTakeTheirTime ) );
		} else {
		    takeTimeList.add( mallTakeTheirTime );
		}
	    }
	    Collections.sort( takeTimeList, new MallTakeComparatorUtil() );
	}
	return takeTimeList;
    }

    private List< MallTakeTheirTime > getTimes( MallTakeTheirTime mallTakeTheirTime ) {
	List< MallTakeTheirTime > list = new ArrayList<>();
	Date nowDate = new Date();

	Date date = DateTimeKit.addMonths( nowDate, 1 );// 一个月后的日期
	SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	String startTime = sdf.format( nowDate );
	String endTime = sdf.format( date );
	List< Integer > daysOfOneWeek = new ArrayList< Integer >();// 用来存放星期
	String[] weeks = mallTakeTheirTime.getVisitDays().split( "," );
	if ( CommonUtil.isNotEmpty( weeks ) ) {
	    for ( String str : weeks ) {
		if ( !str.equals( "" ) ) {
		    daysOfOneWeek.add( Integer.parseInt( str ) );
		}
	    }
	}
	List< String > daysNeedBookList = WeekDayUtil.getDates( startTime,
			endTime, daysOfOneWeek );
	if ( daysNeedBookList != null ) {
	    for ( String string : daysNeedBookList ) {
		String nows = DateTimeKit.format( new Date(), DateTimeKit.DEFAULT_DATE_FORMAT );
		Date now = DateTimeKit.parse( nows, DateTimeKit.DEFAULT_DATE_FORMAT );
		Date ends = DateTimeKit.parse( string, DateTimeKit.DEFAULT_DATE_FORMAT );
		boolean flag = true;
		if ( now.getTime() >= ends.getTime() ) {
		    String nowTimes = DateTimeKit.format( new Date(), "HH:mm" );
		    Date nowTime = DateTimeKit.parse( nowTimes, "HH:mm" );
		    Date endDate = DateTimeKit.parse( mallTakeTheirTime.getEndTime(), "HH:mm" );
		    if ( nowTime.getTime() > endDate.getTime() ) {
			flag = false;
		    }
		}
		if ( flag ) {
		    MallTakeTheirTime time = new MallTakeTheirTime();
		    time.setTimes( string );
		    time.setStartTime( mallTakeTheirTime.getStartTime() );
		    time.setEndTime( mallTakeTheirTime.getEndTime() );
		    list.add( time );
		}
	    }
	}

	return list;
    }
}
