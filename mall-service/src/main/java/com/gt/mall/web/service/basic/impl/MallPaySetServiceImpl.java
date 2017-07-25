package com.gt.mall.web.service.basic.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.basic.MallPaySetDAO;
import com.gt.mall.dao.seller.MallSellerDAO;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.util.CommonUtil;
import com.gt.mall.util.DateTimeKit;
import com.gt.mall.web.service.basic.MallPaySetService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 商城交易支付设置 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallPaySetServiceImpl extends BaseServiceImpl< MallPaySetDAO,MallPaySet > implements MallPaySetService {

    private Logger logger = Logger.getLogger(MallPaySetServiceImpl.class);

    @Autowired
    private MallPaySetDAO paySetDAO;

    @Autowired
    private MallSellerDAO sellerDAO;

    @Override
    public MallPaySet selectByUserId(MallPaySet set) {
        return paySetDAO.selectByUserId(set);
    }

    @Override
    public int editPaySet(Map<String, Object> params) {
        int count = 0;
        MallPaySet set = (MallPaySet) JSONObject.toBean(JSONObject.fromObject(params),MallPaySet.class);

        MallPaySet paySet = paySetDAO.selectByUserId(set);
        if(CommonUtil.isNotEmpty(set) && CommonUtil.isNotEmpty(paySet)){
            if(CommonUtil.isEmpty(set.getId()) && CommonUtil.isNotEmpty(paySet.getId())){
                set.setId(paySet.getId());
            }
        }
        if(CommonUtil.isNotEmpty(set.getId())){
            count = paySetDAO.updateAllColumnById(set);
        }else{
            set.setCreateTime(new Date());
            count = paySetDAO.insert(set);
        }
        if(count > 0){
            MallPaySet mallPaySet = paySetDAO.selectByUserId(set);
            if(CommonUtil.isNotEmpty(set.getIsCheckSeller())){
                if(set.getIsCheckSeller().toString().equals("0")){
                    if(CommonUtil.isNotEmpty(mallPaySet.getIsSeller())){
                        if(mallPaySet.getIsSeller().toString().equals("1")){
                            sellerDAO.updateStatusByUserId(1,set.getUserId());
                        }
                    }
                }
            }
        }
        return count;
    }

    @Override
    public void isHuoDaoByUserId(int userId, HttpServletRequest request) {
        String isHuoDao = "0";//不允许货到付款
        String isDaifu = "0";//不允许代付
        MallPaySet set = new MallPaySet();
        set.setUserId(userId);
        MallPaySet paySet = paySetDAO.selectByUserId(set);
        if(CommonUtil.isNotEmpty(paySet)){
            if(CommonUtil.isNotEmpty(paySet.getIsDeliveryPay())){
                if(paySet.getIsDeliveryPay().toString().equals("1")){
                    isHuoDao = "1";//允许货到付款
                }
            }
            if(CommonUtil.isNotEmpty(paySet.getIsDaifu())){
                if(paySet.getIsDaifu().toString().equals("1")){
                    isDaifu = "1";//允许代付
                }
            }
        }
        request.setAttribute("isHuoDao", isHuoDao);
        request.setAttribute("isDaifu", isDaifu);
    }

    @Override
    public Map<String, Object> isHuoDaoByUserId(int userId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        String isHuoDao = "0";//不允许货到付款
        String isDaifu = "0";//不允许代付
        MallPaySet set = new MallPaySet();
        set.setUserId(userId);
        MallPaySet paySet = paySetDAO.selectByUserId(set);
        if(CommonUtil.isNotEmpty(paySet)){
            if(CommonUtil.isNotEmpty(paySet.getIsDeliveryPay())){
                if(paySet.getIsDeliveryPay().toString().equals("1")){
                    isHuoDao = "1";//允许货到付款
                }
            }
            if(CommonUtil.isNotEmpty(paySet.getIsDaifu())){
                if(paySet.getIsDaifu().toString().equals("1")){
                    isDaifu = "1";//允许代付
                }
            }
        }
        resultMap.put("isHuoDao", isHuoDao);
        resultMap.put("isDaifu", isDaifu);
        return resultMap;
    }
}
