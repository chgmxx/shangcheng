package com.gt.mall.web.service.product;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.product.MallGroup;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallSearchLabel;
import com.gt.mall.util.PageUtil;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商品分组 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallGroupService extends BaseService< MallGroup > {

    /**
     * 分页查询商品分组
     *
     * @param param
     * @param shoplist 店铺列表
     *
     * @return
     */
    PageUtil findGroupByPage( Map< String,Object > param, List< Map< String,Object > > shoplist );

    /**
     * 编辑商品分组
     *
     * @param group     商品分组信息
     * @param imageList 图片信息
     * @param userId    商家id
     *
     * @return
     */
    public boolean saveOrUpdateGroup( MallGroup group, List< MallImageAssociative > imageList, int userId );

    /**
     * 逻辑删除商品分组
     *
     * @param id 商品分组id
     *
     * @return
     */
    public boolean deleteGroup( Integer id ) throws Exception;

    /**
     * 根据主键id来查询单个商品分组
     */
    MallGroup findGroupById( Integer id );

    /**
     * 查询店铺下所有的分类
     */
    List< Map< String,Object > > findGroupByShopId( Map< String,Object > maps );

    /**
     * 通过shopId 查询商品分组信息
     * @param shopId 店铺id
     * @return 店铺信息
     */
    List< MallGroup > selectGroupByShopId( Integer shopId ,Integer groupPId);

    /**
     * 根据店铺id查询父类的分组
     * @param params shopId  店铺id
     * @return  父类分组信息
     */
    List<MallGroup> selectPGroupByShopId(Map<String, Object> params);

    /**
     * 通过父类分组查询子类列表
     * @param param shopId 店铺id
     * @param param group  分组id
     * @return
     */
    List<Map<String, Object>> selectGroupByParent(Map<String, Object> param);

    /**
     * 推荐或删除推荐分组
     *
     * @param labelList 推荐分组信息
     * @param userId    商家id
     */
    boolean saveOrUpdateGroupLabel(List<MallSearchLabel > labelList, int userId);

    /**
     * 通过父类id查询子类的分组
     * @param params
     * @return
     */
    List<MallGroup> selectChildGroupByPId(Map<String, Object> params);

    /**
     * 清空搜索历史记录
     * @param params memberId 粉丝id
     * @return
     */
    boolean clearSearchKeyWord(Map<String, Object> params);

    /**
     * 复制商品分组
     * @param params  分组信息
     * @param product 商品信息
     */
    void copyProductGroupByProduct(Map<String, Object> params,MallProduct product);

}
