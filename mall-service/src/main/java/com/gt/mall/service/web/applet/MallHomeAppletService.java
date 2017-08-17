package com.gt.mall.service.web.applet;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.applet.MallAppletImage;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分组
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallHomeAppletService extends BaseService<MallAppletImage> {

    /**
     * 根据店铺id查询分组信息
     *
     * @param params shopId店铺id； groupPId 分类父id，用户查询子分类 ；  isFrist 是否只查询父类 1查询父类
     * @return List
     */
    List<Map<String, Object>> selectGroupsByShopId(Map<String, Object> params);

    /**
     * 查询所有商品信息
     *
     * @param params desc 排序 1倒序  0 正序； searchName 搜索名称；  type 排序  1时间   2销量   3价格 ；  shopId 店铺id
     * @return List
     */
    PageUtil productAllList(Map<String, Object> params);

    /**
     * 查询活动信息
     *
     * @param params shopId  店铺id
     * @return map
     */
    Map<String, Object> getActivityList(Map<String, Object> params);

    /**
     * 通过商品id查询商品详细信息
     *
     * @param params productId:商品Id,memberId：会员Id
     * @return map
     */
    Map<String, Object> selectProductDetailById(Map<String, Object> params);

    /**
     * 加入购物车
     *
     * @param params shopCart:商品信息
     * @return map
     */
    Map<String, Object> addshopping(Map<String, Object> params);

    /**
     * 获取我的页面需要的信息
     *
     * @param params memberId：会员Id
     * @return
     */
    Map<String, Object> getMemberPage(Map<String, Object> params);

    /**
     * 查询商家是否有技术支持
     *
     * @param params busUserId:商家id
     * @return 有无
     */
    int getAdvert(Map<String, Object> params);

    /**
     * 查询店铺列表
     *
     * @param params
     * @return list
     */
    List<Map<String, Object>> getShopList(Map<String, Object> params);

    /**
     * 查询商家上传的轮播图
     *
     * @param params
     * @return
     */
    List<Map<String, Object>> getAppletImageByBusUser(Map<String, Object> params);

    /**
     * 获取手机验证码
     *
     * @param params
     * @return
     */
    Map<String, Object> getValCode(Map<String, Object> params) throws Exception;

    /**
     * 绑定手机号码
     *
     * @param params
     * @return
     */
    Map<String, Object> bindPhones(Map<String, Object> params) throws Exception;
}
