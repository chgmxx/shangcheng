package com.gt.mall.service.web.seller;

import com.gt.mall.base.BaseService;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.WxPublicUsers;
import com.gt.mall.entity.basic.MallPaySet;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.seller.MallSeller;
import com.gt.mall.entity.seller.MallSellerSet;
import com.gt.mall.util.PageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 超级销售员 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallSellerService extends BaseService< MallSeller > {

    /**
     * 查询客户的个数
     *
     * @param params 参数
     *
     * @return 客户个数
     */
    int selectCountMyClient( Map< String,Object > params );

    /**
     * 查询客户订单的个数
     *
     * @param params 参数
     *
     * @return 订单个数
     */
    int selectCountClientOrder( Map< String,Object > params );

    /**
     * 查询销售员的信息
     *
     * @param saleId 销售员id
     *
     * @return 销售员的信息
     */
    Map< String,Object > selectSellerBySaleId( int saleId );

    /**
     * 查询累计收益
     *
     * @param params saleMemeberId   销售员id    type  统计类型（1统计销售佣金  2统计销售积分    3统计销售金额）
     *
     * @return 累计收益
     */
    List< Map< String,Object > > selectTotalIncome( Map< String,Object > params );

    /**
     * 查询超级销售员的审核状态
     *
     * @param member 粉丝对象
     * @param set    set对象
     *
     * @return 状态
     */
    int selectSellerStatusByMemberId( Member member, MallPaySet set );

    /**
     * 查询销售员信息
     *
     * @param memberId 销售员Id
     *
     * @return 销售员信息
     */
    MallSeller selectSellerByMemberId( int memberId );

    /**
     * 查询销售员收益积分排行榜
     *
     * @param type 1销售员排行榜   2我的客户
     *
     * @return 销售员收益积分排行榜
     */
    Map< String,Object > selectSellerByBusUserId( Map< String,Object > params, int type, Member member );

    /**
     * 通过商家id查询销售规则
     *
     * @param busUserId 商家id
     *
     * @return 销售规则
     */
    MallSellerSet selectByBusUserId( int busUserId );

    /**
     * 获取销售员的id
     *
     * @param member       粉丝对象
     * @param saleMemberId 销售员id
     * @param request      request
     * @param userid       商家id
     *
     * @return 销售员的id
     */
    int getSaleMemberIdByRedis( Member member, int saleMemberId, HttpServletRequest request, int userid );

    /**
     * 把销售员id存到redis
     *
     * @param member       粉丝对象
     * @param saleMemberId 销售员id
     * @param request      request
     */
    void setSaleMemberIdByRedis( Member member, int saleMemberId, HttpServletRequest request, int userid );

    /**
     * 在redis里清空销售员id
     *
     * @param member  粉丝对象
     * @param request request
     * @param userid  商家id
     */
    void clearSaleMemberIdByRedis( Member member, HttpServletRequest request, int userid );

    /**
     * 保存或修改功能设置
     *
     * @param busUserId 商家id
     * @param params    设置参数
     *
     * @return flag  true 成功  false 失败
     */
    Map< String,Object > saveOrUpdSellerSet( int busUserId, Map< String,Object > params );

    /**
     * 查询推荐审核信息
     *
     * @param busUserId 商家id
     * @param params    参数
     *
     * @return 推荐审核信息
     */
    PageUtil selectCheckSeller( int busUserId, Map< String,Object > params );

    /**
     * 审核销售员信息
     *
     * @param busUserId     商家id
     * @param params        参数
     * @param wxPublicUsers 公众号信息
     *
     * @return 销售员信息
     */
    boolean checkSeller( int busUserId, Map< String,Object > params, WxPublicUsers wxPublicUsers );

    /**
     * 销售员分页
     *
     * @param busUserId 商家id
     * @param params    参数
     *
     * @return 销售员
     */
    PageUtil selectSellerPage( int busUserId, Map< String,Object > params );

    /**
     * 申请添加超级销售员
     *
     * @param seller 销售员对象
     * @param member 粉丝对象
     *
     * @return 大于0 添加成功  ； 小于等于 0 添加失败
     */
    int insertSelective( MallSeller seller, Member member );

    /**
     * 修改超级销售员的信息
     *
     * @param seller 销售员
     *
     * @return 大于0 添加成功  ； 小于等于 0 添加失败
     */
    int updateSeller( MallSeller seller );

    /**
     * 查询超级销售员的信息
     *
     * @param seller 销售员
     *
     * @return 销售员
     */
    MallSeller selectMallSeller( MallSeller seller );

    /**
     * 通过店铺id来查询商品佣金设置
     */
    PageUtil selectProductByShopId( Map< String,Object > param );

    /**
     * 保存或修改商品佣金设置
     */
    Map< String,Object > saveOrUpdSellerJoinProduct( int busUserId, Map< String,Object > params );

    /**
     * 查询商品佣金设置
     */
    Map< String,Object > selectJoinProductById( int id );

    /**
     * 修改商品佣金设置
     */
    Map< String,Object > UpdSellerJoinProduct( int busUserId, Map< String,Object > params );

    /**
     * 删除redis里的销售员id
     */
    void delSaleMemberIdByRedis( Member member );

    /**
     * 查询销售员是否已经申请成功
     */
    int getSellerApplay( Member member, MallPaySet set );

    /**
     * 是否已经是销售员
     */
    boolean isSeller( int saleMemberId );

    /**
     * 添加销售员的收益
     */
    void insertSellerIncome( double commission, MallOrder order, MallOrderDetail detail, double totalPrice );

    /**
     * 关注送积分
     *
     * @param params memberId 关注人id  , scene_id 生成二维码时的key
     *
     * @return flag(true or false)     errorMsg
     */
    Map< String,Object > sellerSendIntegral( Map< String,Object > params );

    /**
     * 修改销售员的收益
     */
    void updateSellerIncome( MallOrderDetail detail );

    /**
     * 通过分享进来的粉丝，判断是否是销售员
     */
    void shareSellerIsSale( Member member, int saleMemberId, MallSeller seller );

    /**
     * 创建临时图片
     */
    String createTempImage( Member member, MallSeller seller, int browerType );

    /**
     * 获取销售员的二维码
     */
    MallSeller getSellerTwoCode( MallSeller seller, Member member, int browerType );

    /**
     * 生成商家的二维码
     */
    public String insertTwoCode( String scene_id, WxPublicUsers wxPublicUsers );

    /**
     * 合并销售员的数据
     */
    public MallSeller mergeData( MallSeller seller, Member member );
}
