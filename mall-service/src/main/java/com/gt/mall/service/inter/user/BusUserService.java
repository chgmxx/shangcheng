package com.gt.mall.service.inter.user;

import com.alibaba.fastjson.JSONObject;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.wx.shop.WsWxShopInfoExtend;

import java.util.List;

/**
 * 商家接口
 * User : yangqian
 * Date : 2017/8/19 0019
 * Time : 11:44
 */
public interface BusUserService {
    /**
     * 根据商家id查询商家信息
     *
     * @param busUserId 商家id
     *
     * @return 商家信息
     */
    BusUser selectById( int busUserId );

    /**
     * 判断商家是否已经开通了ERP的某个功能
     *
     * @param modelstyle erp模板 ，取自字典1180    8是进销存
     * @param busUserId  商家id
     *
     * @return 0代表商家未开通该erp功能，1开通了
     */
    int getIsErpCount( int modelstyle, int busUserId );

    /**
     * 判断商家id是否是管理员
     *
     * @param busUserId 商家id
     *
     * @return true 是管理员
     */
    boolean getIsAdmin( int busUserId );

    /**
     * 根据商家id查询主账号id
     *
     * @param userId 商家id
     *
     * @return 主账号id
     */
    int getMainBusId( int userId );

    /**
     * 获取模块视频的url
     *
     * @param courceModel 视频模块取自字典1174
     *
     * @return 视频url
     */
    String getVoiceUrl( String courceModel );

    /**
     * 判断 商家是否已过期
     *
     * @param busUserId 商家id
     *
     * @return 是否已过期
     */
    JSONObject isUserGuoQi( int busUserId );

    /**
     * 根据用户信息获取管理店铺列表
     *
     * @param userId 商家id
     *
     * @return 店铺列表
     */
    List<WsWxShopInfoExtend> getShopIdListByUserId( int userId );

}
