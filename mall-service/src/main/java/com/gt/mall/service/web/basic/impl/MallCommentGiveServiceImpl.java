package com.gt.mall.service.web.basic.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallCommentGiveDAO;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallCommentGive;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.exception.BusinessException;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.FenBiFlowService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallCommentGiveService;
import com.gt.mall.utils.CommonUtil;
import com.gt.util.entity.param.fenbiFlow.FenbiFlowRecord;
import com.gt.util.entity.param.fenbiFlow.FenbiSurplus;
import com.gt.util.entity.param.fenbiFlow.UpdateFenbiReduce;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评论送礼 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallCommentGiveServiceImpl extends BaseServiceImpl< MallCommentGiveDAO,MallCommentGive > implements MallCommentGiveService {

    private Logger logger = Logger.getLogger( MallCommentGiveServiceImpl.class );

    @Autowired
    private MallCommentDAO      commentDAO;
    @Autowired
    private MallCommentGiveDAO  commentGiveDAO;
    @Autowired
    private MallPaySetDAO       paySetDAO;
    @Autowired
    private MemberService       memberService;
    @Autowired
    private WxPublicUserService wxPublicUserService;
    @Autowired
    private FenBiFlowService    fenBiFlowService;

    @Override
    public void commentGive( int commentId, HttpServletRequest request, int memberId ) {
	int userId = 0;//商户id
	//	int publicId = 0;//公众号id
	int integral = 0;//积分
	double fenbi = 0;//粉币
	int totalIntegral = 0;//历史积分
	Member member1 = memberService.findMemberById( memberId, null );
	userId = member1.getBusid();
	integral = member1.getIntegral();
	fenbi = member1.getFansCurrency();
	totalIntegral = member1.getIntegral();

	if ( userId > 0 ) {
	    boolean isOpenGive = false;//是否开启评价送礼
	    MallPaySet set = new MallPaySet();
	    set.setUserId( userId );
	    //查询是否开启了评论送礼
	    MallPaySet payset = paySetDAO.selectOne( set );
	    if ( CommonUtil.isNotEmpty( payset ) ) {
		if ( payset.getIsCommentGive().toString().equals( "1" ) ) {
		    isOpenGive = true;
		}
	    }

	    boolean isMember = memberService.isMember( memberId );
	    if ( isOpenGive && isMember ) {
		//查询评价送礼的情况
		MallComment comment = commentDAO.selectById( commentId );

		if ( CommonUtil.isNotEmpty( comment ) ) {
		    MallCommentGive cGive = new MallCommentGive();
		    cGive.setUserId( userId );
		    cGive.setGiveStatus( comment.getFeel() );
		    cGive.setIsEnable( 1 );
		    MallCommentGive give = commentGiveDAO.selectOne( cGive );
		    Member member = new Member();
		    if ( CommonUtil.isNotEmpty( give ) ) {

			int giveType = CommonUtil.toInteger( give.getGiveType() );
			if ( giveType == 1 ) {//送积分
			    member.setIntegral( integral + give.getNum() );
			    member.setIntegraldate( new Date() );
			    member.setTotalintegral( totalIntegral + give.getNum() );
			} else if ( giveType == 2 ) {//送粉币

			    FenbiSurplus fenbiSurplus = new FenbiSurplus();
			    fenbiSurplus.setBusId( userId );//商家id
			    fenbiSurplus.setFkId( userId );//外键ID
			    fenbiSurplus.setFre_type( Constants.COMMNET_GIVE_TYPE );//冻结类型    30 评论送礼（赠送粉币）
			    fenbiSurplus.setRec_type( 1 );//1：粉币 2：流量
			    FenbiFlowRecord flowRecord = fenBiFlowService.getFenbiFlowRecord( fenbiSurplus );
			    if ( CommonUtil.isNotEmpty( flowRecord ) ) {
				UpdateFenbiReduce fenbiReduce = new UpdateFenbiReduce();
				fenbiReduce.setBusId( userId );
				fenbiReduce.setCount( CommonUtil.toDouble( give.getNum() ) );
				fenbiReduce.setFkId( userId );
				fenbiReduce.setFreType( Constants.COMMNET_GIVE_TYPE );
				boolean flag = fenBiFlowService.updaterecUseCountVer2( fenbiReduce );
				if ( !flag ) {
				    throw new BusinessException( ResponseEnums.ERROR.getCode(), "流量冻结失败" );
				}
			    } else {
				saveFenbiFlow( userId, CommonUtil.toDouble( give.getNum() ) );
			    }
			}
		    } else {
			logger.info( "评论送礼：没有设置评论送礼" );
		    }

		} else {
		    logger.info( "评论送礼：没有查出评论" );
		}
	    } else {
		logger.info( "评论送礼：没有开启评论送礼" );
	    }
	} else {
	    logger.info( "评论送礼：用户没有关注公众号" );
	}

    }

    @Transactional( rollbackFor = Exception.class )
    @Override
    public boolean editCommentGive( List< MallCommentGive > giveList, BusUser user ) {
	boolean flag = false;
	if ( giveList != null && giveList.size() > 0 ) {
	    for ( MallCommentGive mallCommentGive : giveList ) {
		int count = 0;
		mallCommentGive.setUserId( user.getId() );
		if ( CommonUtil.isEmpty( mallCommentGive.getId() ) ) {
		    MallCommentGive give = commentGiveDAO.selectByGive( mallCommentGive );
		    if ( CommonUtil.isNotEmpty( give ) ) {
			mallCommentGive.setId( give.getId() );
		    }
		}
		if ( CommonUtil.isNotEmpty( mallCommentGive.getId() ) ) {
		    count = commentGiveDAO.updateById( mallCommentGive );
		} else {
		    count = commentGiveDAO.insert( mallCommentGive );
		}
		if ( count > 0 ) {
		    if ( mallCommentGive.getGiveType().toString().equals( "2" ) ) {
			FenbiSurplus fenbiSurplus = new FenbiSurplus();
			fenbiSurplus.setBusId( user.getId() );//商家id
			fenbiSurplus.setFkId( user.getId() );//外键ID
			fenbiSurplus.setFre_type( Constants.COMMNET_GIVE_TYPE );//冻结类型    30 评论送礼（赠送粉币）
			fenbiSurplus.setRec_type( Constants.FENBI_GIEVE_TYPE );//1：粉币 2：流量
			FenbiFlowRecord flowRecord = fenBiFlowService.getFenbiFlowRecord( fenbiSurplus );
			if ( CommonUtil.isEmpty( flowRecord ) ) {
			    saveFenbiFlow( user.getId(), null );
			}
		    }
		    flag = true;
		} else {
		    flag = false;
		}
	    }
	}
	return flag;
    }

    /**
     * 粉币冻结
     */
    private void saveFenbiFlow( int userId, Double count ) {
	FenbiFlowRecord fenbiFlowRecord = new FenbiFlowRecord();
	fenbiFlowRecord.setBusUserId( userId );
	fenbiFlowRecord.setRecType( Constants.FENBI_GIEVE_TYPE );
	fenbiFlowRecord.setRecDesc( "评论赠送粉币" );
	fenbiFlowRecord.setRecFreezeType( Constants.COMMNET_GIVE_TYPE );
	fenbiFlowRecord.setRecFkId( userId );
	fenbiFlowRecord.setRecCount( count );
	Map< String,Object > resultMap = fenBiFlowService.saveFenbiFlowRecord( fenbiFlowRecord );
	if ( !resultMap.get( "code" ).toString().equals( "1" ) ) {
	    throw new BusinessException( ResponseEnums.ERROR.getCode(), "冻结流量失败" );
	}
    }

    @Override
    public List< MallCommentGive > getGiveByUserId( int userId ) {

	Wrapper< MallCommentGive > wrapper = new EntityWrapper<>();
	wrapper.where( "user_id = {0} ", userId );
	return commentGiveDAO.selectList( wrapper );
	//        return commentGiveDAO.getGiveByUserId(userId);
    }
}
