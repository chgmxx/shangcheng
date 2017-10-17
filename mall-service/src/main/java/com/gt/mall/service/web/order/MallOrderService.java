package com.gt.mall.service.web.order;

import com.gt.api.bean.session.WxPublicUsers;
import com.gt.mall.base.BaseService;
import com.gt.mall.bean.BusUser;
import com.gt.mall.bean.Member;
import com.gt.mall.bean.MemberAddress;
import com.gt.mall.entity.order.MallDaifu;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.order.MallOrderReturn;
import com.gt.mall.param.OrderDTO;
import com.gt.mall.utils.PageUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 商城订单 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
public interface MallOrderService extends BaseService< MallOrder > {

    /**
     * 分页管理
     */
    public PageUtil findByPage( Map< String,Object > params );

    /**
     * 交易记录分页管理
     */
    public PageUtil findByTradePage( Map< String,Object > params );

    /**
     * 添加卖家备注、修改订单金额
     */
    public int upOrderNoOrRemark( Map< String,Object > params );

    /**
     * 查询订单信息
     */
    public Map< String,Object > selectOrderList( Map< String,Object > params );

    /**
     * 根据会员id 查询收货地址
     */
    public List< MemberAddress > selectShipAddress( Map< String,Object > params );

    /**
     * 关闭未付款订单
     */
    public void updateByNoMoney( Map< String,Object > params );

    /**
     * 根据Id查询单个订单
     */
    public MallOrder getOrderById( Integer orderId );

    /**
     * 根据memberId查询公众号信息
     */
    public WxPublicUsers getWpUser( Integer memberId );

    /**
     * 支付成功后修改订单状态、库存、销量、规格
     */
    public int paySuccessModified( Map< String,Object > params, Member member );

    /**
     * 手机端订单列表
     */
    public PageUtil mobileOrderList( Map< String,Object > params, int busUserId ) throws Exception;

    /**
     * 申请退款
     */
    public boolean addOrderReturn( MallOrderReturn orderReturn );

    /**
     * 修改申请退款
     */
    public Map< String,Object > updateOrderReturn( MallOrderReturn orderReturn, Object oObj, WxPublicUsers pUser ) throws Exception;

    /**
     * 同意退款（用于支付宝退款）
     */
    public void agreanOrderReturn( Map< String,Object > params );

    /**
     * 根据订单详情id查询订单信息
     */
    public Map< String,Object > selectByDIdOrder( Integer detailId );

    /**
     * 查询退款信息
     */
    public MallOrderReturn selectByDId( Integer id );

    /**
     * 根据规格值Id查询规格Id
     */
    public Integer selectSpeBySpeValueId( Map< String,Object > params );

    /**
     * 找人代付成功的回调方法
     */
    public int paySuccessDaifu( Map< String,Object > params );

    /**
     * 找人代付
     */
    public Map< String,Object > addMallDaifu( MallDaifu daifu ) throws Exception;

    /**
     * 查询代付信息
     */
    public boolean getDaiFu( MallOrder order, int orderId, int memberId, HttpServletRequest request );

    /**
     * 导出订单
     *
     * @return
     */
    public HSSFWorkbook exportExcel( Map< String,Object > params, String[] titles, int type, List< Map< String,Object > > shoplist );

    /**
     * 导出订单
     *
     * @return
     */
    public HSSFWorkbook exportTradeExcel( Map< String,Object > params, String[] titles, int type, List< Map< String,Object > > shoplist );

    /**
     * 查询订单详情
     */
    public MallOrderDetail selectOrderDetailById( Integer id );

    /**
     * 支付有礼
     */
    public String payGive( String result, Map< String,Object > params, Member member );

    /**
     * 发送消息模板
     */
    public void sendMsg( MallOrder order, int type );

    /**
     * 同步订单
     */
    public Map< String,Object > syncOrderbyPifa( Map< String,Object > params );

    /**
     * 修改订单详情
     */
    int updateOrderDetail( MallOrderDetail detail );

    /**
     * 修改订单价
     */
    int updateOrderMoney( Map< String,Object > params );

    /**
     * 查询会员的消费金额
     *
     * @param buyerUserId 买家id
     *
     * @return 消费金额
     */
    Map< String,Object > selectSumSaleMoney( int buyerUserId );

    /**
     * 重新生成订单号
     */
    MallOrder againOrderNo( int orderId );

    /**
     * 钱包撤销订单
     */
    void walletReturnOrder( String orderNo ) throws Exception;

    /**
     * 获取打印订单的数据
     */
    Map< String,Object > printOrder( Map< String,Object > params, BusUser user );

    /**
     * 查询买家的账号参数
     */
    Map< String,Object > getMemberParams( Member member, Map< String,Object > params );

    /**
     * 清空session
     */
    public void clearSession( HttpServletRequest request );

    /**
     * 运费是否按照距离来算
     *
     * @param shopIds 店铺id
     *
     * @return 是否按照距离来算
     */
    public boolean isJuliByFreight( String shopIds );

    /**
     * 查询积分订单
     */
    List< Map< String,Object > > selectIntegralOrder( Map< String,Object > params );

    /**
     * 计算库存是否足够
     *
     * @param proId         商品id
     * @param proSpecificas 规格ids
     * @param proNum        购买数量
     *
     * @return result=true 成功
     */
    Map< String,Object > calculateInventory( String proId, Object proSpecificas, String proNum );

    boolean isReturnSuccess( MallOrder order );
}
