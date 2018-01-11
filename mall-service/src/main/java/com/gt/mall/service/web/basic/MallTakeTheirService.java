package com.gt.mall.service.web.basic;

import com.gt.mall.base.BaseService;
import com.gt.api.bean.session.BusUser;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.basic.MallTakeTheir;
import com.gt.mall.utils.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 到店自提表 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallTakeTheirService extends BaseService< MallTakeTheir > {

    /**
     * 根据用户id来查询商品
     *
     * @param params curPage:当前页，userId：用户Id
     *
     * @return 自提列表
     */
    PageUtil selectByUserId( Map< String,Object > params );

    /**
     * 删除上门自提
     *
     * @param params 自提信息
     *
     * @return boolean
     */
    boolean deleteTake( Map< String,Object > params );

    /**
     * 编辑上门自提
     *
     * @param params obj：自提信息，delimageList：删除列表，imageList：图片列表
     *               deltimeList:删除接待时间列表，timeList：添加接待时间列表
     * @param user   用户
     *
     * @return boolean
     */
    boolean editTake( Map< String,Object > params, BusUser user );

    /**
     * 编辑上门自提
     *
     * @param params obj：自提信息，delimageList：删除列表，imageList：图片列表
     *               deltimeList:删除接待时间列表，timeList：添加接待时间列表
     * @param user   用户
     *
     * @return boolean
     */
    boolean newEditTake( Map< String,Object > params, BusUser user );

    /**
     * 查询上门自提集合
     *
     * @param param userId：用户Id，firstNum：页数，maxNum 数量
     *
     * @return 上门自提信息列表
     */
    List< MallTakeTheir > selectListByUserId( Map< String,Object > param );

    /**
     * 根据上门自提id查询上门自提信息
     *
     * @param params id：自提id
     *
     * @return 上门自提信息
     */
    MallTakeTheir selectById( Map< String,Object > params );

    /**
     * 查询用户是否允许使用上门自提
     *
     * @param userId 用户Id
     *
     * @return true 允许  false 不允许
     */
    boolean isTakeTheirByUserId( int userId );

    /**
     * 查询用户是否允许使用上门自提
     *
     * @param setList 设置集合
     *
     * @return [isShow:是否显示上门自提,user_id：商家id]
     */
    public List< Map< String,Object > > isTakeTheirByUserIdList( List< MallPaySet > setList, String provincesId );

    /**
     * 根据公众号id查询上门自提信息
     *
     * @param map userId：用户Id，provinceId：省份id，takeId：自提id
     *
     * @return 上门自提信息列表
     */
    List< MallTakeTheir > selectByBusUserId( Map< String,Object > map );
}
