package com.gt.mall.service.web.groupbuy.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.dao.groupbuy.MallGroupBuyPriceDAO;
import com.gt.mall.entity.groupbuy.MallGroupBuyPrice;
import com.gt.mall.service.web.groupbuy.MallGroupBuyPriceService;
import com.gt.mall.util.CommonUtil;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 团购价格表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallGroupBuyPriceServiceImpl extends BaseServiceImpl<MallGroupBuyPriceDAO, MallGroupBuyPrice> implements MallGroupBuyPriceService {

    private Logger log = Logger.getLogger(MallGroupBuyPriceServiceImpl.class);

    @Autowired
    private MallGroupBuyPriceDAO groupBuyPriceDAO;

    @Override
    public void editGroupBuyPrice(Map<String, Object> map, int groupBuyId, boolean flag) {
        if (flag) {//已经更换了商品
            //删除已经有的团购价
            MallGroupBuyPrice price = new MallGroupBuyPrice();
            price.setIsDelete(1);
            price.setGroupBuyId(groupBuyId);
            groupBuyPriceDAO.updateByGroupBuyId(price);
        }
        if (CommonUtil.isNotEmpty(map.get("specArr"))) {
            List<MallGroupBuyPrice> priceList = (List<MallGroupBuyPrice>)
                    JSONArray.toList(JSONArray.fromObject(map.get("specArr")), MallGroupBuyPrice.class);
            if (priceList != null && priceList.size() > 0) {
                for (MallGroupBuyPrice mallGroupBuyPrice : priceList) {
                    mallGroupBuyPrice.setGroupBuyId(groupBuyId);
                    if (CommonUtil.isEmpty(mallGroupBuyPrice.getId())) {
                        groupBuyPriceDAO.insert(mallGroupBuyPrice);
                    } else {
                        groupBuyPriceDAO.updateById(mallGroupBuyPrice);
                    }
                }
            }
        }
    }

    @Override
    public List<MallGroupBuyPrice> selectPriceByGroupId(int groupBuyId) {

        Wrapper<MallGroupBuyPrice> groupWrapper = new EntityWrapper<>();
        groupWrapper.where("group_buy_id = {0} and is_delete = 0", groupBuyId);

        return groupBuyPriceDAO.selectList(groupWrapper);

    }
}
