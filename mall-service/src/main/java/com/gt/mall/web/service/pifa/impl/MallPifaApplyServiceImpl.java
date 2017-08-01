package com.gt.mall.web.service.pifa.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.dao.pifa.MallPifaApplyDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.pifa.MallPifaApply;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.web.service.basic.MallPaySetService;
import com.gt.mall.web.service.pifa.MallPifaApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 批发申请表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPifaApplyServiceImpl extends BaseServiceImpl< MallPifaApplyDAO,MallPifaApply > implements MallPifaApplyService {

    @Autowired
    private MallPifaApplyDAO  mallPifaApplyDAO;
    @Autowired
    private MallPaySetService mallPaySetService;

    @Override
    public int getPifaApplay(Member member,MallPaySet set) {
	int status = -2;
	if(CommonUtil.isNotEmpty(member)){

	    MallPifaApply applay = new MallPifaApply();
	    applay.setMemberId(member.getId());
	    applay.setBusUserId(member.getBusid());
	    MallPifaApply pifaApplay = mallPifaApplyDAO.selectByPifaApply(applay);
	    if( CommonUtil.isNotEmpty(pifaApplay)){
		if(CommonUtil.isNotEmpty(pifaApplay.getStatus())){
		    status = CommonUtil.toInteger(pifaApplay.getStatus());
		    if(CommonUtil.isNotEmpty(set.getIsPfCheck())){
			if(set.getIsPfCheck().toString().equals("0")){//不开启审核所有人都能看到批发价
			    status = 1;
			}
		    }else{
			status = 1;
		    }
		}
	    }
	}
	return status;
    }

    @Override
    public boolean isPifa(Member member) {
	MallPaySet set = mallPaySetService.selectByMember(member);
	if(CommonUtil.isEmpty(set)){
	    return false;
	}
	int state = getPifaApplay(member, set);
	boolean isPifa = false;
	if(CommonUtil.isNotEmpty(set)){
	    if(CommonUtil.isNotEmpty(set.getIsPf())){
		if(set.getIsPf().toString().equals("1")){
		    if(CommonUtil.isNotEmpty(set.getIsPfCheck())){
			if(set.getIsPfCheck().toString().equals("1")){
			    if(state == 1){
				isPifa = true;
			    }
			}else{
			    isPifa = true;
			}
		    }else{
			isPifa = true;
		    }
		}
	    }
	}
	return isPifa;
    }

}
