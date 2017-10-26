package com.gt.mall.service.web.groupbuy.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.api.bean.session.Member;
import com.gt.mall.dao.groupbuy.MallGroupJoinDAO;
import com.gt.mall.entity.groupbuy.MallGroupJoin;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.groupbuy.MallGroupJoinService;
import com.gt.mall.utils.CommonUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参团表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallGroupJoinServiceImpl extends BaseServiceImpl< MallGroupJoinDAO,MallGroupJoin > implements MallGroupJoinService {

    private Logger log = Logger.getLogger( MallGroupJoinServiceImpl.class );

    @Autowired
    private MallGroupJoinDAO groupJoinDAO;
    @Autowired
    private MemberService    memberService;

    @Override
    public List< Map< String,Object > > getJoinGroup( Map< String,Object > params, Member member ) {
	List< Map< String,Object > > joinList = new ArrayList<>();
	try {
	    //获取开团信息
	    List< Map< String,Object > > list = groupJoinDAO.selectJoinGroupByProId( params );
	    if ( list != null && list.size() > 0 ) {
		for ( Map< String,Object > map : list ) {
		    boolean flag = true;
		    //获取开团参团人数
		    List< MallGroupJoin > joinGroupList = groupJoinDAO.selectByProJoinId( map );
		    if ( joinGroupList != null && joinGroupList.size() > 0 ) {
			if ( CommonUtil.isNotEmpty( member ) ) {
			    for ( MallGroupJoin mallGroupJoin : joinGroupList ) {
				if ( mallGroupJoin.getJoinUserId().equals( member.getId() ) ) {
				    flag = false;
				    break;
				}
			    }
			}
		    } else {
			flag = false;
		    }
		    map.put( "count", joinGroupList.size() );
		    int num = CommonUtil.toInteger( map.get( "pelpleNum" ) );
		    map.put( "joinNum", num - joinGroupList.size() );

		    if ( flag && num - joinGroupList.size() > 0 ) {
			Member member1 = memberService.findMemberById( CommonUtil.toInteger( map.get( "joinUserId" ) ), null );
			map.put( "nickname", member1.getNickname() );
			map.put( "headimgurl", member1.getHeadimgurl() );
			joinList.add( map );
		    }
		}
	    }
	    return joinList;
	} catch ( Exception e ) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public List< Map< String,Object > > selectJoinByjoinId( Map< String,Object > params ) {
	List< Map< String,Object > > list = groupJoinDAO.selectJoinByJoinId( params );
	if ( list != null && list.size() > 0 ) {
	    for ( Map< String,Object > map : list ) {
		Member member1 = memberService.findMemberById( CommonUtil.toInteger( map.get( "joinUserId" ) ), null );
		map.put( "nickname", member1.getNickname() );
		map.put( "headimgurl", member1.getHeadimgurl() );
	    }
	}

	return list;
    }

    @Override
    public int selectCountByBuyId( Map< String,Object > params ) {
	return groupJoinDAO.selectCountByBuyId( params );
    }
}
