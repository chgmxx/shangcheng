package com.gt.mall.dao.product;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gt.mall.entity.product.MallShopCart;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallShopCartDAO extends BaseMapper< MallShopCart > {
    /**
     * 查询购物车的信息
     */
    List<Map<String, Object> > selectByShopCart(MallShopCart shopCart);

    /**
     * 修改购物车信息
     */
    int updateByShopCart(MallShopCart shopCart);

    /**
     * 查询购物车信息
     */
    List<Map<String, Object>> selectAppletByParams(Map<String, Object> params);

    /**
     * 查询已选中购物车的商品信息
     */
    List<Map<String, Object>> selectCheckCartByParams(Map<String, Object> params);

    /**
     * 查询已选中的店铺信息
     */
    List<Map<String, Object>> selectCheckShopByParam(Map<String, Object> params);

    /**
     * 取消已选中的商品
     */
    void updateCheckByShopCart(Map<String, Object> params);

    /**
     * 获取购物车的数量
     */
    int selectShopCartNum(Map<String, Object> params);

    /**
     * 查询会员的购物车信息
     * @param params 参数
     * @return 购物车信息
     */
    List<Map<String,Object> > selectShopCartByMemberId(Map<String,Object> params);

    /**
     * 根据id查询购物车信息
     * @param id 购物车id
     * @return 购物车信息
     */
    Map<String,Object> selectMapById(int id);

    /**
     * 修改购物车信息
     * @param memberId 用户id
     * @param busUserId 商家id
     * @param ids 购物车id
     */
    void updateShopCarts(@Param( "memberId" ) int memberId,@Param( "busUserId" )int busUserId,@Param( "ids" )String[] ids);
}