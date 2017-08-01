package com.gt.mall.web.service.integral.impl;

import com.gt.mall.base.BaseServiceImpl;
import com.gt.mall.bean.Member;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.basic.MallImageAssociativeDAO;
import com.gt.mall.dao.integral.MallIntegralDAO;
import com.gt.mall.dao.order.MallOrderDAO;
import com.gt.mall.dao.order.MallOrderDetailDAO;
import com.gt.mall.dao.product.MallProductDAO;
import com.gt.mall.dao.product.MallProductDetailDAO;
import com.gt.mall.entity.basic.MallImageAssociative;
import com.gt.mall.entity.integral.MallIntegral;
import com.gt.mall.entity.order.MallOrder;
import com.gt.mall.entity.order.MallOrderDetail;
import com.gt.mall.entity.product.MallProduct;
import com.gt.mall.entity.product.MallProductDetail;
import com.gt.mall.util.*;
import com.gt.mall.web.service.integral.MallIntegralService;
import com.gt.mall.web.service.order.MallOrderService;
import com.gt.mall.web.service.page.MallPageService;
import com.gt.mall.web.service.product.MallProductInventoryService;
import com.gt.mall.web.service.product.MallProductService;
import com.gt.mall.web.service.product.MallProductSpecificaService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <p>
 * 积分商品表 服务实现类
 * </p>
 *
 * @author yangqian
 * @since 2017-07-20
 */
@Service
public class MallIntegralServiceImpl extends BaseServiceImpl<MallIntegralDAO, MallIntegral> implements MallIntegralService {

    private Logger log = Logger.getLogger(MallIntegralServiceImpl.class);

    @Autowired
    private MallIntegralDAO integralDAO;
    @Autowired
    private MallImageAssociativeDAO imageAssociativeDAO;
    @Autowired
    private MallProductDAO productDAO;
    @Autowired
    private MallProductService productService;
    @Autowired
    private MallProductDetailDAO productDetailDAO;
    @Autowired
    private MallProductInventoryService productInventoryService;
    @Autowired
    private MallOrderDAO orderDAO;
    @Autowired
    private MallOrderService orderService;
    @Autowired
    private MallOrderDetailDAO orderDetailDAO;
    @Autowired
    private MallPageService pageService;
    @Autowired
    private MallProductSpecificaService productSpecificaService;

    @Override
    public PageUtil selectIntegralByUserId(Map<String, Object> params) {
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty(params.get("curPage")) ? 1 : CommonUtil.toInteger(params.get("curPage"));
        params.put("curPage", curPage);

        // 统计售罄商品
        int count = integralDAO.selectCountIntegralByShopids(params);

        PageUtil page = new PageUtil(curPage, pageSize, count, "phoneIntegral/79B4DE7C/toIndex.do");
        int firstNum = pageSize * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);
        params.put("firstNum", firstNum);// 起始页
        params.put("maxNum", pageSize);// 每页显示商品的数量

        List<Map<String, Object>> list = integralDAO.selectIntegralByShopids(params);
        if (list != null && list.size() > 0) {
            for (Map<String, Object> map : list) {
                if (CommonUtil.isNotEmpty(map.get("specifica_img_url"))) {
                    map.put("product_image", PropertiesUtil.getHomeUrl() + map.get("specifica_img_url"));
                } else {
                    map.put("product_image", PropertiesUtil.getHomeUrl() + map.get("image_url"));
                }
                productList.add(map);
            }
        }
        page.setSubList(productList);
        return page;
    }

    @Override
    public PageUtil selectIntegralDetail(Member member, Map<String, Object> params) {
        int pageSize = 20;
        //TODO 需关连卡片t_member_cardrecord 记录
//        int curPage = CommonUtil.isEmpty(params.get("curPage")) ? 1 : CommonUtil.toInteger(params.get("curPage"));
//        String countSql = "SELECT count(id) FROM t_member_cardrecord WHERE recordType = 2 AND cardId=" + member.getMcId();
//        int count = daoUtil.queryForInt(countSql);

//        PageUtil page = new PageUtil(curPage, pageSize, count, "phoneIntegral/79B4DE7C/toIndex.do");
//        int firstNum = pageSize * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);

//        String sql = "SELECT itemName,number,date_format(createDate, '%Y-%c-%d %h:%i:%s') as createDate FROM t_member_cardrecord " +
//                "WHERE recordType = 2 AND cardId=" + member.getMcId() + " order by id desc limit " + firstNum + "," + pageSize;
//        List<Map<String, Object>> recordList = daoUtil.queryForList(sql);
//        page.setSubList(recordList);
//        return page;
        return null;
    }

    @Override
    public Map<String, Object> selectProductDetail(Member member, Map<String, Object> params) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int productId = CommonUtil.toInteger(params.get("id"));
        MallProduct product = productDAO.selectById(productId);// 根据商品id查询商品的基本信息
        resultMap.put("product", product);
        // 查询商品详情
        MallProductDetail detail = new MallProductDetail();
        detail.setProductId(productId);
        detail = productDetailDAO.selectOne(detail);
        if (CommonUtil.isNotEmpty(detail)) {
            if (CommonUtil.isNotEmpty(detail.getProductDetail())) {
                detail.setProductDetail(detail.getProductDetail().replaceAll("\"", "&quot;").replaceAll("'", "&apos;").replaceAll("(\r\n|\r|\n|\n\r)", ""));
            }
        }
        //查询商品规格
        if (product.getIsSpecifica().toString().equals("1")) {
            Map<String, Object> guige = productInventoryService.productSpecifications(product.getId(), null);//查询商品的默认规格
            List<Map<String, Object>> specificaList = productSpecificaService.getSpecificaByProductId(product.getId());//获取商品规格值
            List<Map<String, Object>> guigePriceList = productInventoryService.guigePrice(product.getId());//获取商品所有规格
            if (guige == null || specificaList == null || specificaList.size() == 0) {
                product.setIsSpecifica(0);
            }
            resultMap.put("guige", guige);
            resultMap.put("specificaList", specificaList);
            resultMap.put("guigePriceList", guigePriceList);
        }

        resultMap.put("detail", detail);

        Map<String, Object> integralParams = new HashMap<String, Object>();
        integralParams.put("productId", product.getId());
        integralParams.put("isUse", 1);
        integralParams.put("shopId", product.getShopId());
        List<MallIntegral> integralList = integralDAO.selectByIntegral(integralParams);
        if (integralList != null && integralList.size() > 0) {
            MallIntegral integral = integralList.get(0);
            resultMap.put("integral", integral);
            if (CommonUtil.isNotEmpty(integral)) {

                Date endTime = DateTimeKit.parse(integral.getEndTime().toString(), "yyyy-MM-dd HH:mm:ss");
                Date startTime = DateTimeKit.parse(integral.getStartTime().toString(), "yyyy-MM-dd HH:mm:ss");
                Date nowTime = DateTimeKit.parse(DateTimeKit.getDateTime(), "yyyy-MM-dd HH:mm:ss");
                if (nowTime.getTime() < startTime.getTime()) {
                    resultMap.put("isNoStart", 1);
                }
                if (nowTime.getTime() > endTime.getTime()) {
                    resultMap.put("isEnd", 1);
                }

                integral.setEndTime(DateTimeKit.getDateTime(endTime, "yyyy-MM-dd"));
                integral.setStartTime(DateTimeKit.getDateTime(startTime, "yyyy-MM-dd"));
            }
        }

        params.put("assId", productId);
        params.put("assType", 1);
        // 查询商品图片
        List<MallImageAssociative> imageList = imageAssociativeDAO.selectImageByAssId(params);
        resultMap.put("imageList", imageList);

        params.put("productId", productId);
        List<Map<String, Object>> orderList = orderDAO.selectIntegralOrder(params);
        int recordNum = orderList.size();
        resultMap.put("recordNum", recordNum);

        if (CommonUtil.isNotEmpty(product.getFlowId())) {
            //TODO 需关连busFlowService.selectById()流量方法
//            BusFlow flow = busFlowService.selectById(product.getFlowId());
//            if(CommonUtil.isNotEmpty(flow)){
//                resultMap.put("flow_desc", flow.getType()+"M流量");
//            }
        }
        int proViewNum = 0;
        if (CommonUtil.isNotEmpty(product.getViewsNum())) {
            proViewNum = CommonUtil.toInteger(product.getViewsNum());
        }
        updateProNum(product.getId().toString(), proViewNum);
        return resultMap;
    }

    private void updateProNum(String proId, Integer proViewNum) {

        String key = Constants.REDIS_KEY + "proViewNum";
        int viewNum = 0;
        String viewNums = "";
        if (JedisUtil.hExists(key, proId)) {
            viewNums = JedisUtil.maoget(key, proId);
        }
        if (viewNums == null || viewNums.equals("")) {
            if (CommonUtil.isNotEmpty(proViewNum)) {
                viewNums = proViewNum.toString();
            }
        }
        if (viewNums != null && !viewNums.equals("")) {
            viewNum = CommonUtil.toInteger(viewNums);
        }
        if (viewNum + 1 < 1000000000) {
            JedisUtil.map(key, proId, (viewNum + 1) + "");
        }
    }

    @Override
    public Map<String, Object> recordIntegral(Map<String, Object> params, Member member, Integer browser, HttpServletRequest request) {
        DecimalFormat df = new DecimalFormat("######0.00");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        int productId = CommonUtil.toInteger(params.get("productId"));
        MallProduct product = productDAO.selectById(productId);
        int integralId = CommonUtil.toInteger(params.get("integralId"));
        MallIntegral integral = integralDAO.selectById(integralId);
        double price = CommonUtil.toDouble(integral.getMoney());
        int num = CommonUtil.toInteger(params.get("productNum"));
        double totalPrice = CommonUtil.toDouble(df.format(price * num));
        int orderPayWay = 4;
        Object proSpecificas = params.get("productSpecificas");
        String flowPhone = "";
        if (CommonUtil.isNotEmpty(params.get("flowPhone"))) {
            flowPhone = CommonUtil.toString(params.get("flowPhone"));
        }
        int proTypeId = product.getProTypeId();

        int memType = 0;
        //TODO 需关连memberPayService  isMemember()  isCardType()方法
//        if(memberPayService.isMemember(member.getId())){//是否为会员
//            memType = memberPayService.isCardType(member.getId());
//        }
        if (orderPayWay == 4) {//积分支付
            Integer mIntergral = member.getIntegral();
            if (mIntergral < totalPrice || mIntergral < 0) {
                resultMap.put("code", -1);
                resultMap.put("msg", "您的积分不够，不能用积分来兑换这件商品");
                return resultMap;
            }
        }
        /*if(orderPayWay == 8){//粉币支付
            double fenbi = member.getFansCurrency();
			if (fenbi < totalPrice || fenbi < 0) {
				resultMap.put("code", -1);
				resultMap.put("msg", "您的粉币不够，不能用粉币来兑换这件商品");
				return resultMap;
			}
		}*/
        //TODO 需调用 orderService.calculateInventory()方法
//        Map<String, Object> result = orderService.calculateInventory(productId+"", proSpecificas, num+"");
//        if(result.get("result").toString().equals("false")){
//            resultMap.put("code", -1);
//            resultMap.put("msg", result.get("msg"));
//            return resultMap;
//        }
        if (proTypeId == 0 && CommonUtil.isEmpty(params.get("receiveId"))) {
            resultMap.put("code", 1);
            resultMap.put("proTypeId", proTypeId);
            return resultMap;
        }
        if (CommonUtil.isNotEmpty(flowPhone)) {//流量充值，判断手机号码
            Map<String, String> map = MobileLocationUtil.getMobileLocation(flowPhone);
            if (map.get("code").toString().equals("-1")) {
                resultMap.put("code", -1);
                resultMap.put("msg", map.get("msg"));
                return resultMap;
            } else if (map.get("code").toString().equals("1")) {
                //TODO 需关连busFlowService 流量 方法
//                BusFlow flow = busFlowService.selectById(product.getFlowId());
//                if(map.get("supplier").equals("中国联通")&&flow.getType()==10){
//                    resultMap.put("code", -1);
//                    resultMap.put("msg", "充值失败,联通号码至少30MB");
//                    return resultMap;
//                }
            }
        }

        int code = -1;
        MallOrder order = new MallOrder();
        order.setOrderNo("SC" + System.currentTimeMillis());
        order.setOrderMoney(BigDecimal.valueOf(totalPrice));
        order.setOrderOldMoney(BigDecimal.valueOf(totalPrice));
        order.setOrderPayWay(orderPayWay);
        order.setBuyerUserId(member.getId());
        if (CommonUtil.isNotEmpty(member.getPublicId())) {
            order.setSellerUserId(member.getPublicId());
        }
        if (CommonUtil.isNotEmpty(member.getBusid())) {
            order.setBusUserId(member.getBusid());
        }
        order.setShopId(product.getShopId());
        order.setOrderStatus(1);
        order.setCreateTime(new Date());
        //order.setPayTime(new Date());
        order.setOrderType(2);
        order.setMemCardType(memType);
        if (CommonUtil.isNotEmpty(flowPhone)) {
            order.setFlowPhone(flowPhone);
            order.setFlowRechargeStatus(0);
        }
        if (CommonUtil.isNotEmpty(params.get("receiveId"))) {
            order.setReceiveId(CommonUtil.toInteger(params.get("receiveId")));
            //TODO 需调用 微餐饮IEatPhoneService.updateDefaultArea()方法
//            eatPhoneService.updateDefaultArea(order.getReceiveId(), member.getId());
        }
        if (CommonUtil.isNotEmpty(browser)) {
            order.setBuyerUserType(browser);
        }
        int count = orderDAO.insert(order);

        if (count > 0) {
            MallOrderDetail detail = new MallOrderDetail();
            detail.setOrderId(order.getId());
            detail.setProductId(productId);
            detail.setShopId(product.getShopId());
            params.put("assId", productId);
            params.put("isMainImages", 1);
            params.put("assType", 1);
            List<MallImageAssociative> imagesList = imageAssociativeDAO.selectImageByAssId(params);//获取商品图片
            if (imagesList != null && imagesList.size() > 0) {
                detail.setProductImageUrl(imagesList.get(0).getImageUrl());
            }
            if (CommonUtil.isNotEmpty(proSpecificas)) {
                detail.setProductSpecificas(CommonUtil.toString(proSpecificas));
                Map<String, Object> invMap = productService.getProInvIdBySpecId(proSpecificas.toString(), productId);
                detail.setProductSpeciname(invMap.get("specifica_values").toString());
                if (CommonUtil.isNotEmpty(invMap.get("specifica_img_url"))) {
                    detail.setProductImageUrl(invMap.get("specifica_img_url").toString());
                }
            }

            detail.setDetProNum(CommonUtil.toInteger(num));
            detail.setDetProPrice(BigDecimal.valueOf(price));
            detail.setDetProName(product.getProName());
            if (CommonUtil.isNotEmpty(product.getProCode())) {
                detail.setDetProCode(product.getProCode());
            }
            detail.setDetPrivivilege(BigDecimal.valueOf(price));
            detail.setReturnDay(product.getReturnDay());
            detail.setCreateTime(new Date());
            detail.setProTypeId(CommonUtil.toInteger(product.getProTypeId()));
            detail.setTotalPrice(totalPrice);
            if (CommonUtil.isNotEmpty(product.getCardType())) {
                detail.setCardReceiveId(product.getCardType());
            }
            if (CommonUtil.isNotEmpty(product.getFlowId())) {
                detail.setFlowId(product.getFlowId());
            }
            if (CommonUtil.isNotEmpty(product.getFlowRecordId())) {
                detail.setFlowRecordId(product.getFlowRecordId());
            }
            count = orderDetailDAO.insert(detail);
            if (count > 0) {
                params.put("status", 2);
                params.put("out_trade_no", order.getOrderNo());
                //TODO 需调用 会员memberPayService.updateIntergral()方法
                Map<String, Object> payRresult = new HashMap<>();
//                Map<String, Object> payRresult = memberPayService.updateIntergral(request, member.getId(), (int) -order.getOrderMoney());
                if (CommonUtil.isNotEmpty(payRresult.get("result"))) {
                    if (CommonUtil.toString(payRresult.get("result")).equals("2")) {
                        //TODO 需调用 orderService.paySuccessModified()方法
//                        orderService.paySuccessModified(params, member);//修改库存和订单状态
                        code = 1;
                    } else {
                        code = -1;
                    }
                } else {
                    code = -1;
                }
            }
        }

        resultMap.put("code", code);

        return resultMap;
    }

    @Override
    public PageUtil selectIntegralByPage(Map<String, Object> params) {
        int pageSize = 10;

        int curPage = CommonUtil.isEmpty(params.get("curPage")) ? 1 : CommonUtil.toInteger(params.get("curPage"));
        params.put("curPage", curPage);
        int count = integralDAO.selectByCount(params);

        PageUtil page = new PageUtil(curPage, pageSize, count, "/mallIntegral/index.do");
        int firstNum = pageSize * ((page.getCurPage() <= 0 ? 1 : page.getCurPage()) - 1);
        params.put("firstNum", firstNum);// 起始页
        params.put("maxNum", pageSize);// 每页显示商品的数量

        if (count > 0) {// 判断团购是否有数据
            List<Map<String, Object>> integralList = integralDAO.selectByPage(params);
            page.setSubList(integralList);
        }
        return page;
    }

    @Override
    public Map<String, Object> selectByIds(int id) {
        return integralDAO.selectByIds(id);
    }

    @Override
    public Map<String, Object> saveIntegral(int busUserId, Map<String, Object> params) {
        int count = 0;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (CommonUtil.isNotEmpty(params.get("integral"))) {
            MallIntegral mallIntegral = (MallIntegral) JSONObject.toBean(JSONObject.fromObject(params.get("integral")), MallIntegral.class);
            MallIntegral integral = null;
            if (CommonUtil.isNotEmpty(mallIntegral.getProductId())) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("productId", mallIntegral.getProductId());
                map.put("userId", busUserId);
                List<MallIntegral> list = integralDAO.selectByIntegral(map);
                if (list != null && list.size() > 0) {
                    integral = list.get(0);
                }
            }
            if (CommonUtil.isNotEmpty(mallIntegral.getId())) {
                if (CommonUtil.isNotEmpty(integral)) {
                    if (!integral.getId().toString().equals(mallIntegral.getId().toString())) {
                        resultMap.put("msg", "您选择的商品已经设置了积分，请重新选择");
                        resultMap.put("flag", false);
                        return resultMap;
                    }
                }
                count = integralDAO.updateById(mallIntegral);
            } else {
                if (CommonUtil.isNotEmpty(integral)) {
                    if (CommonUtil.isNotEmpty(integral.getId())) {
                        resultMap.put("msg", "您选择的商品已经设置了积分，请重新选择");
                        resultMap.put("flag", false);
                        return resultMap;
                    }
                }
                mallIntegral.setUserId(busUserId);
                mallIntegral.setCreateTime(new Date());
                count = integralDAO.insert(mallIntegral);
            }
        }
        if (count > 0) {
            resultMap.put("flag", true);
        } else {
            resultMap.put("flag", false);
        }
        return resultMap;
    }

    @Override
    public boolean removeIntegral(Map<String, Object> params) {
        int type = CommonUtil.toInteger(params.get("type"));
        int id = CommonUtil.toInteger(params.get("id"));
        MallIntegral integral = new MallIntegral();
        if (type == -1) {//删除积分商品
            integral.setIsDelete(1);
        } else if (type == -2) {//使失效积分商品
            integral.setIsUse(-1);
        } else if (type == 1) {//启用失效积分商品
            integral.setIsUse(1);
        }
        integral.setId(id);
        int count = integralDAO.updateById(integral);
        if (count > 0) {
            return true;
        }
        return false;
    }
}
