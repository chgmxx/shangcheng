package com.gt.mall.web.service.purchase;

import com.gt.mall.base.BaseService;
import com.gt.mall.entity.purchase.PurchaseReceivables;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.SortedMap;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
public interface PurchaseReceivablesService extends BaseService<PurchaseReceivables> {


    /**
     * 采购模块微信支付
     *
     * @param url
     * @param memberId
     * @param busId
     * @param termId
     * @param money
     * @param discountmoney
     * @param fenbi
     * @param jifen
     * @param discount
     * @param paymentType
     * @param orderId
     * @param dvId
     * @param disCountdepict
     * @return
     * @throws Exception
     */
    public SortedMap<Object, Object> cgPay(String url, Integer memberId, Integer busId, String termId, Double money,
                                           Double discountmoney, Double fenbi, Integer jifen, Integer discount,
                                           String paymentType, Integer orderId, Integer dvId, String disCountdepict) throws Exception;

    /**
     * 采购模块支付宝支付
     *
     * @param memberId
     * @param busId
     * @param termId
     * @param money
     * @param discountmoney
     * @param fenbi
     * @param jifen
     * @param discount
     * @param paymentType
     * @param orderId
     * @param dvId
     * @param disCountdepict
     * @return
     * @throws Exception
     */
    public Map<String, Object> aliCgPay(Integer memberId, Integer busId, String termId, Double money, Double discountmoney,
                                        Double fenbi, Integer jifen, Integer discount, String paymentType, Integer orderId,
                                        Integer dvId, String disCountdepict) throws Exception;


    /**
     * 订单支付后回调此处进行后续操作
     *
     * @param receivablesNumber
     */
    void addReceivables(@RequestParam String receivablesNumber) throws Exception;
}
