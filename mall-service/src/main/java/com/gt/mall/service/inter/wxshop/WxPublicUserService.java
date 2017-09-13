package com.gt.mall.service.inter.wxshop;

import com.gt.mall.bean.WxPublicUsers;
import com.gt.util.entity.param.wx.BusIdAndindustry;
import com.gt.util.entity.param.wx.QrcodeCreateFinal;
import com.gt.util.entity.param.wx.SendWxMsgTemplate;
import com.gt.util.entity.result.wx.ApiWxApplet;

import java.util.List;
import java.util.Map;

/**
 * 微信公众号接口
 * User : yangqian
 * Date : 2017/8/18 0018
 * Time : 17:15
 */
public interface WxPublicUserService {

    /**
     * 根据商家id查询公众号信息
     *
     * @param busUserId 商家id
     *
     * @return 公众号信息
     */
    WxPublicUsers selectByUserId( int busUserId );

    /**
     * 根据公众号id查询公众号信息
     *
     * @param id 公众号id
     *
     * @return 公众号信息
     */
    WxPublicUsers selectById( int id );

    /**
     * 根据会员id查询公众号信息
     *
     * @param memberId 会员id
     *
     * @return 公众号信息
     */
    WxPublicUsers selectByMemberId( int memberId );

    /**
     * 生成永久二维码
     *
     * @param createFinal {scene_id：场景值，publicId：公众号id}
     *
     * @return tiket 值
     */
    String qrcodeCreateFinal( QrcodeCreateFinal createFinal );

    /**
     * 发送消息模板
     *
     * @param template 模板
     *
     * @return true 发送成功  false 发送失败
     */
    boolean sendWxMsgTemplate( SendWxMsgTemplate template );

    /**
     * 根据商家id查询消息模板
     *
     * @param busUserId 商家id
     *
     * @return 消息模板
     */
    List< Map > selectTempObjByBusId( int busUserId );

    /**
     * 7、根据商家ID行业id查询小程序账号
     */
    ApiWxApplet selectBybusIdAndindustry( BusIdAndindustry busIdAndindustry );

}
