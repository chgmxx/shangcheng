package com.gt.mall.service.web.basic.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.dao.basic.MallCommentDAO;
import com.gt.mall.dao.basic.MallCommentGiveDAO;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.entity.basic.MallComment;
import com.gt.mall.entity.basic.MallCommentGive;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.inter.wxshop.WxPublicUserService;
import com.gt.mall.service.web.basic.MallCommentGiveService;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.SessionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
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

    @Override
    public void commentGive( int commentId, HttpServletRequest request, int memberId ) {
	int userId = 0;//商户id
	//	int publicId = 0;//公众号id
	int integral = 0;//积分
	double fenbi = 0;//粉币
	int totalIntegral = 0;//历史积分
	Member member1 = memberService.findMemberById( memberId, null );
	WxPublicUsers wxPublicUsers = wxPublicUserService.selectById( member1.getPublicId() );
	userId = wxPublicUsers.getBusUserId();
	integral = member1.getIntegral();
	fenbi = member1.getFansCurrency();
	totalIntegral = member1.getTotalintegral();

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
		    boolean flag = false;
		    if ( CommonUtil.isNotEmpty( give ) ) {

			int giveType = CommonUtil.toInteger( give.getGiveType() );
			if ( giveType == 1 ) {//送积分
			    member.setIntegral( integral + give.getNum() );
			    member.setIntegraldate( new Date() );
			    member.setTotalintegral( totalIntegral + give.getNum() );
			    flag = true;
			} else if ( giveType == 2 ) {//送粉币
			    int rec_type = 1;//1：粉币 2：流量
			    int fre_type = 30;//冻结类型    30 评论送礼（赠送粉币）
			    int fkId = 0;//外键ID
			    //TODO 需关连 粉币方法
			    //                            Integer fenbiCount = fenbiMapper.getFenbiSurplus(userId, rec_type, fre_type, fkId);
			    //                            if (fenbiCount >= Double.valueOf(give.getNum())) {
			    //                                member.setCurrencydate(new Date());
			    //                                member.setFansCurrency(CommonUtil.toDouble(fenbi + give.getNum()));
			    //
			    //                                FenbiFlowRecord record = fenbiMapper.getFenbi(userId, rec_type, fre_type, fkId);
			    //                                if (CommonUtil.isNotEmpty(record)) {
			    //                                    FenbiFlowRecord fenbiFlow = new FenbiFlowRecord();
			    //                                    fenbiFlow.setId(record.getId());
			    //                                    fenbiFlow.setRecUseCount(CommonUtil.toDouble(record.getRecUseCount() + give.getNum()));
			    //
			    //                                    fenbiMapper.updateByPrimaryKeySelective(fenbiFlow);
			    //                                    flag = true;
			    //                                }
			    //                            }
			}
			if ( flag ) {
			    member.setId( memberId );
			    //TODO 需关连 修改会员方法
			    //                            memberMapper.updateByPrimaryKeySelective(member);
			    member = memberService.findMemberById( memberId, null );
			    SessionUtils.setLoginMember( request, member );
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
			//TODO 需关连 粉币方法
			//查询资产分配
			//                        FenbiFlowRecord fenbi = new FenbiFlowRecord();
			//                        fenbi.setBusUserId(user.getId());
			//                        fenbi.setRecType(1);
			//                        fenbi.setRecCreatetime(new Date());
			//                        fenbi.setRecDesc("评论赠送粉币");
			//                        fenbi.setRecFreezeType(30);
			//                        fenbi.setRecFkId(0);
			//                        fenbi.setRecCount(Double.valueOf("0"));
			//                        //判断用户是否已经资产分配过了
			//                        FenbiFlowRecord fenbis = fenbiMapper.getFenbi(fenbi.getBusUserId(), fenbi.getRecType(), fenbi.getRecFreezeType(), fenbi.getRecFkId());
			//                        if(CommonUtil.isEmpty(fenbis)){
			//                            fenbiMapper.insertSelective(fenbi);
			//                        }
		    }
		    flag = true;
		} else {
		    flag = false;
		}
	    }
	}
	return flag;
    }

    @Override
    public List< MallCommentGive > getGiveByUserId( int userId ) {

	Wrapper< MallCommentGive > wrapper = new EntityWrapper<>();
	wrapper.where( "user_id = {0} ", userId );
	return commentGiveDAO.selectList( wrapper );
	//        return commentGiveDAO.getGiveByUserId(userId);
    }
}
