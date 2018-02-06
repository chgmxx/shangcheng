package com.gt.mall.controller.purchase.phone;

import com.alibaba.fastjson.JSONObject;
import com.gt.api.bean.session.Member;
import com.gt.api.dto.ResponseUtils;
import com.gt.api.util.KeysUtil;
import com.gt.entityBo.NewErpPaySuccessBo;
import com.gt.entityBo.PayTypeBo;
import com.gt.mall.common.AuthorizeOrLoginController;
import com.gt.mall.constant.Constants;
import com.gt.mall.dao.purchase.*;
import com.gt.mall.dto.ServerResponse;
import com.gt.mall.entity.purchase.*;
import com.gt.mall.enums.ResponseEnums;
import com.gt.mall.service.inter.member.MemberService;
import com.gt.mall.service.web.purchase.*;
import com.gt.mall.utils.CommonUtil;
import com.gt.mall.utils.MallRedisUtils;
import com.gt.mall.utils.MallSessionUtils;
import com.gt.mall.utils.PropertiesUtil;
import com.gt.util.entity.param.pay.SubQrPayParams;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 采购 手机端控制器
 * </p>
 *
 * @author yangqian
 * @since 2017-07-31
 */
@Controller
@RequestMapping( "purchasePhone" )
public class PurchasePhoneController extends AuthorizeOrLoginController {

    private final static Logger logger                 = Logger.getLogger( PurchasePhoneController.class );
    private static final String MEMBER_FINDMEMBERBYIDS = "/memberAPI/member/findMemberByIds"; //根据ids集合查询粉丝信息返回包含数据(id,昵称，手机号码,头像)
    @Autowired
    PurchaseOrderService        orderService;
    @Autowired
    PurchaseLanguageDAO         languageDAO;
    @Autowired
    PurchaseOrderDetailsDAO     orderDetailsDAO;
    @Autowired
    PurchaseCompanyModeService  companyModeService;
    @Autowired
    PurchaseTermDAO             termDAO;
    @Autowired
    PurchaseCarouselDAO         carouselDAO;
    @Autowired
    PurchaseReceivablesDAO      receivablesDAO;
    @Autowired
    PurchaseOrderDetailsService detailsService;
    @Autowired
    PurchaseContractService     contractService;
    @Autowired
    PurchaseContractOrderDAO    contractOrderDAO;
    @Autowired
    MemberService               memberService;

    @Autowired
    private PurchaseReceivablesService receivablesService;
    @Autowired
    private PurchaseOrderStatisticsDAO orderStatisticsDAO;

    /**
     * 手机端订单详情首页
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/{orderId}/findOrder" )
    public String findOrder( HttpServletRequest request, @PathVariable( "orderId" ) Integer orderId ) {
        try {
            String memberIds = "";//粉丝id集合
            String stage = ""; //期数
            Double nowMoney = 0.0; //如果是订单类型为分期 该属性为本期需要支付的金额
            Double nextMoney = 0.0; //如果是订单类型为分期 该属性为下期需要支付的金额
            Double retainage = 0.0; //如果是订单类型为分期 该属性为分期的尾款
            //查询订单详情
            PurchaseOrder order = orderService.selectById( orderId );
            if ( order != null && order.getId() != null ) {
                //查询订单的商品详情
                List< Map< String,Object > > orderdetails = orderDetailsDAO.findOrderDetails( order.getId() );
                //查询留言
                List< Map< String,Object > > languageList = languageDAO.findAllList( order.getId() );
                for ( int i = 0; i < languageList.size(); i++ ) {
                    if ( languageList.get( i ).get( "member_id" ) != null && !languageList.get( i ).get( "member_id" ).toString().equals( "" ) ) {
                        if ( memberIds.equals( "" ) ) {
                            memberIds = languageList.get( i ).get( "member_id" ).toString();
                        } else {
                            memberIds += "," + languageList.get( i ).get( "member_id" ).toString();
                        }
                    }
                }
                Map< String,Object > map = new HashMap< String,Object >();
                map.put( "busId", order.getBusId() );
                map.put( "ids", memberIds );
                List< Map< String,Object > > memberList = memberService.findMemberByIds( map );
                for ( int i = 0; i < languageList.size(); i++ ) {
                    for ( int a = 0; a < memberList.size(); a++ ) {
                        Integer memberId = Integer.parseInt( memberList.get( a ).get( "memberId" ).toString() );
                        Integer lMemberId = Integer.parseInt( languageList.get( i ).get( "member_id" ).toString() );
                        if ( memberId.intValue() == lMemberId.intValue() ) {
                            languageList.get( i ).put( "nickname", memberList.get( a ).get( "nickname" ) );
                            languageList.get( i ).put( "headimgurl", memberList.get( a ).get( "headimgurl" ) );
                        }
                    }

                }
                for ( int i = 0; i < languageList.size(); i++ ) {
                    if ( languageList.get( i ).containsKey( "nickname" ) ) {
                        try {
                            byte[] bytes = (byte[]) languageList.get( i ).get( "nickname" );
                            languageList.get( i ).put( "nickname", new String( bytes, "UTF-8" ) );
                        } catch ( Exception e ) {
                            languageList.get( i ).put( "nickname", null );
                        }
                    }
                }
                //查询轮播图
                List< Map< String,Object > > carouselList = carouselDAO.findByOrderId( order.getId() );
                PurchaseCompanyMode companyMode = companyModeService.selectById( order.getCompanyId() );
                if ( order.getOrderType().equals( "1" ) ) {
                    //查询分期
                    List< Map< String,Object > > termList = termDAO.findTermList( order.getId() );
                    for ( int i = 0; i < termList.size(); i++ ) {
                        PurchaseTerm term = (PurchaseTerm) termList.get( i );
                        if ( term.getTermBuy().equals( "0" ) ) {
                            stage = i + 1 + "/" + termList.size();
                            nowMoney = term.getTermMoney();
                            if ( i + 1 < termList.size() && ( (PurchaseTerm) termList.get( i + 1 ) ).getTermBuy().toString().equals( "0" ) ) {
                                nextMoney = ( (PurchaseTerm) termList.get( i + 1 ) ).getTermMoney();
                            }
                            break;
                        }
                    }
                    retainage = order.getAllMoney();
                    for ( int i = 0; i < termList.size(); i++ ) {
                        if ( ( (PurchaseTerm) termList.get( i ) ).getTermBuy().equals( "1" ) ) {
                            retainage -= ( (PurchaseTerm) termList.get( i ) ).getTermMoney();
                        }
                    }
                    if ( retainage > 0 ) {
                        retainage = getDiscountMoney( retainage, 10.0 );
                    } else {
                        retainage = 0.0;
                    }
                }

                //查询收款
                List< Map< String,Object > > receivablesList = receivablesDAO.findReceivablesList( order.getId() );
                request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
                request.setAttribute( "order", order );
                request.setAttribute( "stage", stage );
                request.setAttribute( "nowMoney", nowMoney );
                request.setAttribute( "retainage", retainage );
                request.setAttribute( "nextMoney", nextMoney );
                request.setAttribute( "orderdetails", orderdetails );
                request.setAttribute( "languageList", languageList );
                request.setAttribute( "carouselList", carouselList );
                request.setAttribute( "companyMode", companyMode );
                request.setAttribute( "receivablesList", receivablesList );
            }
        } catch ( RuntimeException e ) {
            logger.error( "手机端订单详情首页异常:" + e.getMessage() );
            e.printStackTrace();
        } catch ( Exception e ) {
            logger.error( "手机端订单详情首页异常:" + e.getMessage() );
            e.printStackTrace();
        }
        return "mall/purchase/phone/index";
    }

    /**
     * 查询订单详情商品信息
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/findDetails" )
    public String findDetails( HttpServletRequest request ) {
        try {
            PurchaseOrderDetails orderDetails = orderDetailsDAO.selectById( Integer.parseInt( request.getParameter( "id" ).toString() ) );
            if ( orderDetails != null && orderDetails.getId() != null && orderDetails.getProductId() != null ) {
                List< Map< String,Object > > imgList = detailsService.productImg( orderDetails.getProductId() );
                if ( imgList != null ) {
                    request.setAttribute( "http", PropertiesUtil.getResourceUrl() );
                    request.setAttribute( "imgList", imgList );
                }
                request.setAttribute( "orderDetails", orderDetails );
            }
        } catch ( Exception e ) {
            logger.error( "查询订单详情商品信息异常:" + e.getMessage() );
            e.printStackTrace();
        }
        return "mall/purchase/phone/detail";
    }

    /**
     * 付款详情
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/buy" )
    public String buy( HttpServletRequest request, HttpServletResponse response ) {
        try {
            Double nowMoney = 0.0; //如果是订单类型为分期 该属性为本期需要支付的金额
            Double nextMoney = 0.0; //如果是订单类型为分期 该属性为下期需要支付的金额
            Double actualMoney = 0.0; //改属性为本次需要付款的金额
            Double retainage = 0.0; //如果是订单类型为分期 该属性为分期的尾款
            Double grDiscount = 0.0; //如果是折扣卡这个值代表折扣数
            Integer termId = 0; //本期分期的id
            String orderId = request.getParameter( "orderId" ).toString(); //订单id
            String busId = request.getParameter( "busId" ).toString(); //订单的商户id
            String haveContract = request.getParameter( "haveContract" ).toString(); //是否需要合同
            //授权部分
            String url = PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/buy.do?orderId=" + orderId + "&busId=" + busId + "&haveContract=" + haveContract;
            Map< String,Object > map = new HashMap<>();
            map.put( "url", url );
            map.put( "ucLogin", 0 );
            map.put( "busId", busId );
            String returnStr = userLogin( request, response, map );
            if ( CommonUtil.isNotEmpty( returnStr ) ) {
                request.setAttribute( "returnUrl", returnStr );
                return "mall/purchase/phone/authorizationBack";
            }
            //判断是否存在合同 ,如果有合同跳转合同页面待用户确认
            if ( request.getParameter( "haveContract" ) != null && request.getParameter( "haveContract" ).toString().equals( "0" ) ) {
                List< Map< String,Object > > contractListMap = contractOrderDAO.findContractOrderList( Integer.parseInt( request.getParameter( "orderId" ).toString() ) );
                if ( contractListMap.size() > 0 ) {
                    PurchaseContract contract = contractService.selectById( Integer.parseInt( contractListMap.get( 0 ).get( "contract_id" ).toString() ) );
                    request.setAttribute( "contract", contract );
                }
                request.setAttribute( "orderId", request.getParameter( "orderId" ).toString() );
                request.setAttribute( "busId", request.getParameter( "busId" ).toString() );
                return "mall/purchase/phone/hetong";
            } else { // 如果不存在合同则直接跳转到报价单详情页进行付款操作
                //浏览器类型判断
                if ( CommonUtil.judgeBrowser( request ) != 1 ) {
                    request.setAttribute( "payType", 0 );
                } else {
                    request.setAttribute( "payType", 1 );
                }
                //查询订单详情
                PurchaseOrder order = orderService.selectById( Integer.parseInt( request.getParameter( "orderId" ) ) );
                if ( order.getOrderType().equals( "1" ) ) { //如果订单的类型是分期
                    List< Map< String,Object > > termList = termDAO.findTermList( order.getId() );//查询分期
                    for ( int i = 0; i < termList.size(); i++ ) {
                        PurchaseTerm term = (PurchaseTerm) termList.get( i ); //获得一条分期数据
                        if ( term.getTermBuy().equals( "0" ) ) { //如果出现未付款的数据
                            nowMoney = term.getTermMoney(); // 保存本期应该的付款
                            actualMoney = term.getTermMoney();
                            termId = term.getId();
                            if ( i + 1 < termList.size() && ( (PurchaseTerm) termList.get( i + 1 ) ).getTermBuy().toString().equals( "0" ) ) {
                                nextMoney = ( (PurchaseTerm) termList.get( i + 1 ) ).getTermMoney();
                            }
                            break;
                        }
                    }
                    retainage = order.getAllMoney();
                    for ( int i = 0; i < termList.size(); i++ ) {
                        if ( ( (PurchaseTerm) termList.get( i ) ).getTermBuy().equals( "1" ) ) {
                            retainage -= ( (PurchaseTerm) termList.get( i ) ).getTermMoney();
                        }
                    }
                    if ( retainage > 0 ) {
                        retainage = getDiscountMoney( retainage, 10.0 );
                    } else {
                        retainage = 0.0;
                    }

                } else { //如果订单的类型是全款
                    //查询收款
                    List< Map< String,Object > > receivablesList = receivablesDAO.findReceivablesList( order.getId() );
                    if ( receivablesList == null || receivablesList.size() == 0 ) {
                        nowMoney = order.getAllMoney();
                        actualMoney = order.getAllMoney();
                    }
                }
                Member member = MallSessionUtils.getLoginMember( request, CommonUtil.toInteger( busId ) );
                if ( member.getMcId() != null && member.getMcId() > 0 ) {
                    Map< String,Object > memberMap = memberService.findMemberCardByMemberId( member.getId(), -1 );
                    if ( memberMap != null ) {
                        grDiscount = Double.parseDouble( memberMap.get( "discount" ).toString() );
                        if ( Integer.parseInt( memberMap.get( "ctId" ).toString() ) == 2 ) {
                            actualMoney = getDiscountMoney( actualMoney, grDiscount );
                        }
                        Map< String,Object > memberData = new HashMap< String,Object >();
                        memberData.put( "id", member.getId() );
                        memberData.put( "ctId", Integer.parseInt( memberMap.get( "ctId" ).toString() ) );
                        memberData.put( "jifenMoeny", Double.parseDouble( memberMap.get( "jifenMoeny" ).toString() ) );
                        memberData.put( "fenbiMoeny", Double.parseDouble( memberMap.get( "fenbiMoeny" ).toString() ) );
                        memberData.put( "fenbiRatio", Double.parseDouble( memberMap.get( "fenbiRatio" ).toString() ) );
                        memberData.put( "jifenRatio", Double.parseDouble( memberMap.get( "jifenRatio" ).toString() ) );
                        memberData.put( "fenbiStartMoney", Double.parseDouble( memberMap.get( "fenbiStartMoney" ).toString() ) );
                        memberData.put( "jifenStartMoney", Double.parseDouble( memberMap.get( "jifenStartMoney" ).toString() ) );
                        memberData.put( "money", Double.parseDouble( memberMap.get( "money" ).toString() ) );
                        request.setAttribute( "memberData", memberData );
                    }
                }
                request.setAttribute( "grDiscount", grDiscount );
                request.setAttribute( "nowMoney", nowMoney );
                request.setAttribute( "nextMoney", nextMoney );
                request.setAttribute( "actualMoney", actualMoney );
                request.setAttribute( "retainage", retainage );
                request.setAttribute( "termId", termId );
                request.setAttribute( "order", order );
            }
        } catch ( Exception e ) {
            logger.error( "付款详情异常:" + e.getMessage() );
            e.printStackTrace();
        }
        return "mall/purchase/phone/order";
    }

    public static double getDiscountMoney( double money, Double discount ) {
        money = money / 10 * discount;
        String str = String.format( "%.2f", money );
        if ( Double.parseDouble( str ) < 0.01 ) {
            return 0.01;
        }
        return Double.parseDouble( str );
    }

    /**
     * 去往写留言的页面
     *
     * @param request
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/languagePage" )
    public String languagePage( HttpServletRequest request, HttpServletResponse response ) throws Exception {
        String url = PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/languagePage.do?orderId=" + request.getParameter( "orderId" ).toString() + "&busId=" + request
            .getParameter( "busId" ).toString();
        int busId = CommonUtil.toInteger( request.getParameter( "busId" ) );
        Map< String,Object > map = new HashMap<>();
        map.put( "url", url );
        map.put( "ucLogin", 0 );
        map.put( "busId", busId );
        String returnStr = userLogin( request, response, map );
        if ( CommonUtil.isNotEmpty( returnStr ) ) {
            request.setAttribute( "returnUrl", returnStr );
            return "mall/purchase/phone/authorizationBack";
        }
        PurchaseOrder order = orderService.selectById( Integer.parseInt( request.getParameter( "orderId" ) ) );
        request.setAttribute( "orderId", order.getId() );
        try {
            Member member = MallSessionUtils.getLoginMember( request, busId );
            request.setAttribute( "member", member );
            request.setAttribute( "orderTitle", order.getOrderTitle() );
        } catch ( Exception e ) {
            logger.error( "去往写留言的页面异常:" + e.getMessage() );
            e.printStackTrace();
        }
        return "mall/purchase/phone/msg";
    }

    /**
     * 写留言
     *
     * @param request
     * @param response
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/writeLanguage" )
    public void writeLanguage( HttpServletRequest request, HttpServletResponse response ) throws IOException {
        try {
            Integer memberId = MallSessionUtils.getLoginMember( request, MallRedisUtils.getUserId() ).getId();
            PurchaseLanguage language = new PurchaseLanguage();
            language.setIsRead( "0" );
            language.setLanguageTime( new Date() );
            language.setMemberId( memberId );
            language.setLanguageContent( request.getParameter( "languageContent" ).toString() );
            language.setOrderId( Integer.parseInt( request.getParameter( "orderId" ).toString() ) );
            languageDAO.insert( language );
            response.getWriter().print( "true" );
        } catch ( Exception e ) {
            logger.error( "写留言异常:" + e.getMessage() );
            e.printStackTrace();
            response.getWriter().print( "false" );
        }
    }

    /**
     * 手机端支付订单
     *
     * @param request
     * @param response
     * @param memberId
     *
     * @throws IOException
     */
    @RequestMapping( "/79B4DE7C/aliCgPay" )
    @ResponseBody
    public ServerResponse cgPay( HttpServletRequest request, HttpServletResponse response, @RequestParam Integer memberId, @RequestParam Integer busId, String termId,
        @RequestParam double money, @RequestParam double discountmoney, Double fenbi, Integer jifen, Double discount, @RequestParam String paymentType,
        @RequestParam Integer orderId, Integer dvId, String disCountdepict ) throws IOException {
        try {
            //新增收款记录
            PurchaseReceivables receivables = new PurchaseReceivables();
            receivables.setBusId( busId );
            receivables.setBuyMode( paymentType );
            receivables.setBuyTime( new Date() );
            receivables.setCoupon( dvId != null ? dvId.toString() : null );
            receivables.setDiscount( discount );
            receivables.setFansCorrency( fenbi );
            receivables.setHaveTerm( termId == null || termId.equals( "" ) ? "0" : "1" );
            receivables.setIntegral( Double.parseDouble( jifen.toString() ) );
            receivables.setMemberId( memberId );
            receivables.setMoney( discountmoney );
            String receivablesNumber = "CG" + System.currentTimeMillis();
            receivables.setReceivablesNumber( receivablesNumber );
            receivables.setOrderId( orderId );
            receivables.setBuyStatus( 0 );
            receivables.setTermId( termId == null || termId.equals( "" ) ? null : Integer.parseInt( termId ) );
            receivablesDAO.insert( receivables );

            SubQrPayParams subQrPayParams = new SubQrPayParams();
            subQrPayParams.setAppidType( 0 );
            subQrPayParams.setBusId( busId );
            subQrPayParams.setDesc( "对外报价订单支付" );
            subQrPayParams.setIsreturn( 1 );
            subQrPayParams.setIsSendMessage( 0 );
            subQrPayParams.setMemberId( memberId );
            subQrPayParams.setModel( 35 );
            subQrPayParams.setNotifyUrl( PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/payBackMethod.do" );
            subQrPayParams.setOrderNum( receivablesNumber );
            subQrPayParams.setPayWay( CommonUtil.judgeBrowser( request ) == 1 ? 1 : 2 );
            subQrPayParams.setReturnUrl( PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/" + orderId + "/findOrder.do" );
            subQrPayParams.setSourceType( 1 );
            subQrPayParams.setTotalFee( discountmoney );
            subQrPayParams.setSourceType( Constants.PAY_SOURCE_TYPE );//墨盒默认0即啊祥不用填,其他人调用填1
            subQrPayParams.setTakeState( 2 );//此订单是否可立即提现(1:是 2:否,不填默认为1)，不可立即提现表示此订单有担保期；注：如传值为2,各erp系统需各自写定时器将超过担保期的订单发送到指定接口

            KeysUtil keysUtil = new KeysUtil();
            String parms = keysUtil.getEncString( JSONObject.toJSONString( subQrPayParams ) );
            String payUrl = PropertiesUtil.getWxmpDomain() + "/8A5DA52E/payApi/6F6D9AD2/79B4DE7C/payapi.do?obj=" + parms;
            return ServerResponse.createBySuccessCodeMessage( ResponseEnums.SUCCESS.getCode(), ResponseEnums.SUCCESS.getDesc(), payUrl );

        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 手机端支付回调
     *
     * @param request
     * @param response
     * @param map
     *
     * @throws IOException
     */
    @RequestMapping( value = "/79B4DE7C/payBackMethod" )
    public void payBackMethod( HttpServletRequest request, HttpServletResponse response, @RequestBody Map< String,Object > map ) throws IOException {
        ResponseUtils< ? > responseUtils = ResponseUtils.createBySuccess();
        try {
            receivablesService.addReceivables( map.get( "out_trade_no" ).toString() );
            //得到收款信息
            PurchaseReceivables receivable = receivablesDAO.selectReceivable( map.get( "out_trade_no" ).toString() );
            //验证会员信息是否正确
            if ( auditMemberJiFenAndFenBiAndMoney( receivable.getMemberId(), receivable.getIntegral(), receivable.getFansCorrency(), 0.0 ) ) {
                //核销会员卡使用数据 积分 粉币  储值卡 等
                destructionMemberAndJiFenAndFenBi( request, receivable.getMemberId(), receivable.getReceivablesNumber(), receivable.getMoney(), receivable.getIntegral(),
                    receivable.getMoney(), receivable.getFansCorrency(), receivable.getBuyMode() );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
            responseUtils = ResponseUtils.createByError();
        }
        CommonUtil.write( response, responseUtils );
    }

    /**
     * 新增收款记录
     *
     * @param request
     * @param memberId
     * @param orderId
     * @param busId
     * @param buyMode
     * @param money
     * @param fansCurrency
     * @param integral
     * @param coupon
     * @param termId
     *
     * @return
     */
    @RequestMapping( value = "/79B4DE7C/addReceivables" )
    @ResponseBody
    public String addReceivables( HttpServletRequest request, @RequestParam Integer memberId, @RequestParam Integer orderId, @RequestParam Integer busId,
        @RequestParam String buyMode, @RequestParam Double money, Double fansCurrency, Double integral, String coupon, String termId, Double discount,
        Double totalMoney ) {
        try {
            //新增收款信息  修改订单状态
            String receivablesNumber = receivablesService.addReceivables( request, memberId, orderId, busId, buyMode, money, fansCurrency, integral, coupon, termId, discount );
            if ( receivablesNumber != null ) {
                //当支付方式不是货到付款的时候
                if ( !buyMode.equals( "4" ) ) {
                    //验证会员信息是否正确
                    if ( auditMemberJiFenAndFenBiAndMoney( memberId, integral, fansCurrency, buyMode.equals( "5" ) ? money : 0.0 ) ) {
                        //核销会员卡使用数据 积分 粉币  储值卡 等
                        destructionMemberAndJiFenAndFenBi( request, memberId, receivablesNumber, totalMoney, integral, money, fansCurrency, buyMode );
                    } else {
                        //验证不通过直接返回
                        return "false";
                    }
                }
            } else {
                return "false";
            }
            return "true";
        } catch ( Exception e ) {
            logger.error( "新增收款记录异常:" + e.getMessage() );
            e.printStackTrace();
            return "false";
        }

    }

    /**
     * 核销优惠券 扣除用户使用的积分和粉币信息
     *
     * @param request
     * @param memberId
     * @param orderNo
     * @param totalMoney
     * @param integral
     * @param money
     * @param fansCurrency
     * @param buyMode
     */
    public void destructionMemberAndJiFenAndFenBi( HttpServletRequest request, Integer memberId, String orderNo, Double totalMoney, Double integral, Double money,
        Double fansCurrency, String buyMode ) {

        Map< String,Object > memberMap = memberService.findMemberCardByMemberId( memberId, -1 );
        Double jifenRatio2 = Double.parseDouble( memberMap.get( "jifenRatio" ).toString() );
        Double fenbiRatio2 = Double.parseDouble( memberMap.get( "fenbiRatio" ).toString() );
        NewErpPaySuccessBo erpPaySuccessBo = new NewErpPaySuccessBo();
        erpPaySuccessBo.setMemberId( memberId );
        erpPaySuccessBo.setOrderCode( orderNo );
        erpPaySuccessBo.setTotalMoney( totalMoney );
        erpPaySuccessBo.setDiscountMoney( getDiscountMoney( ( integral + fansCurrency ), 10.0 ) );
        erpPaySuccessBo.setDiscountAfterMoney( money );
        if ( fansCurrency > 0 ) {
            erpPaySuccessBo.setUserFenbi( 1 );
            erpPaySuccessBo.setFenbiNum( getDiscountMoney( ( fansCurrency * fenbiRatio2 ), 10.0 ) );
        }
        if ( integral > 0 ) {
            erpPaySuccessBo.setUserJifen( 1 );
            Double userJifen = Math.ceil( getDiscountMoney( ( integral * jifenRatio2 ), 10.0 ) );
            erpPaySuccessBo.setJifenNum( userJifen.intValue() );
        }
        erpPaySuccessBo.setDataSource( CommonUtil.judgeBrowser( request ) == 99 ? 1 : 2 );
        erpPaySuccessBo.setUcType( 115 );
        List< PayTypeBo > payTypeBoList = new ArrayList< PayTypeBo >();
        PayTypeBo payTypeBo = new PayTypeBo();
        payTypeBo.setPayMoney( money );
        payTypeBo.setPayType( Integer.parseInt( buyMode ) );
        payTypeBoList.add( payTypeBo );
        erpPaySuccessBo.setPayTypeBos( payTypeBoList );
        memberService.newPaySuccessByErpBalance( erpPaySuccessBo );
    }

    /**
     * 验证会员积分和粉币储值卡是否符合付款条件
     *
     * @param jifenMoeny
     * @param fenbiMoeny
     * @param money
     *
     * @return
     */
    public boolean auditMemberJiFenAndFenBiAndMoney( Integer memberId, Double jifenMoeny, Double fenbiMoeny, Double money ) {
        Map< String,Object > memberMap = memberService.findMemberCardByMemberId( memberId, -1 );
        if ( memberMap != null ) {
            if ( jifenMoeny > 0 ) {
                Double jifenMoney2 = Double.parseDouble( memberMap.get( "jifenStartMoney" ).toString() );
                if ( jifenMoeny < jifenMoney2 ) {
                    return false;
                }
                if ( Double.parseDouble( memberMap.get( "jifenMoeny" ).toString() ) < jifenMoeny ) {
                    return false;
                }
            }
            if ( fenbiMoeny > 0 ) {
                Double fenbiMoney2 = Double.parseDouble( memberMap.get( "fenbiStartMoney" ).toString() );
                if ( fenbiMoeny < fenbiMoney2 ) {
                    return false;
                }
                if ( Double.parseDouble( memberMap.get( "fenbiMoeny" ).toString() ) < fenbiMoeny ) {
                    return false;
                }
            }
            if ( money > 0 ) {
                Double money2 = Double.parseDouble( memberMap.get( "money" ).toString() );
                if ( money2 < money ) {
                    return false;
                }
            }
            return true;
        } else {
            return false;

        }
    }

    /**
     * 授权保存统计
     *
     * @param response
     * @param request
     */
    @RequestMapping( value = "/79B4DE7C/{busId}/{orderId}/getMemberPower" )
    public String getMemberPower( HttpServletResponse response, HttpServletRequest request, @PathVariable( "busId" ) Integer busId, @PathVariable( "orderId" ) Integer orderId ) {
        try {
            //浏览器类型判断
            if ( CommonUtil.judgeBrowser( request ) == 1 ) {
                String url = PropertiesUtil.getHomeUrl() + "/purchasePhone/79B4DE7C/" + busId + "/" + orderId + "/getMemberPower.do";
                Map< String,Object > map = new HashMap<>();
                map.put( "url", url );
                map.put( "ucLogin", 0 );
                map.put( "busId", busId );
                String returnStr = userLogin( request, response, map );
                if ( CommonUtil.isNotEmpty( returnStr ) ) {
                    request.setAttribute( "returnUrl", returnStr );
                    return "mall/purchase/phone/authorizationBack";
                }
            }
            //记录统计
            PurchaseOrderStatistics orderStatistics = new PurchaseOrderStatistics();
            Member member = MallSessionUtils.getLoginMember( request, busId );
            if ( member != null ) {
                orderStatistics.setMemberId( member.getId() );
                orderStatistics.setMemberHeadimgurl( member.getHeadimgurl() );
                orderStatistics.setMemberName( member.getNickname() );
            }
            orderStatistics.setLookDate( new Date() );
            orderStatistics.setLookIp( CommonUtil.getIpAddr( request ) );
            orderStatistics.setBusId( busId );
            orderStatistics.setOrderId( orderId );
            orderStatisticsDAO.insert( orderStatistics );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return "redirect:/purchasePhone/79B4DE7C/" + orderId + "/findOrder.do";
    }

    /**
     * 分期详情
     *
     * @return
     */
    @RequestMapping( "/79B4DE7C/termDetails" )
    public String termDetails( HttpServletRequest request, @RequestParam Integer orderId ) {
        Double retainage = 0.0; //如果是订单类型为分期 该属性为分期的尾款
        PurchaseOrder order = orderService.selectById( orderId );//查询订单
        int index = 0; //已还期数
        //查询分期
        retainage = order.getAllMoney();
        List< Map< String,Object > > termList = termDAO.findTermList( orderId );
        for ( int i = 0; i < termList.size(); i++ ) {
            if ( ( (PurchaseTerm) termList.get( i ) ).getTermBuy().equals( "1" ) ) {
                index++;
                retainage -= ( (PurchaseTerm) termList.get( i ) ).getTermMoney();
            }
        }
        if ( retainage > 0 ) {
            retainage = getDiscountMoney( retainage, 10.0 );
        } else {
            retainage = 0.0;
        }
        request.setAttribute( "order", order );
        request.setAttribute( "index", index );
        request.setAttribute( "retainage", retainage );
        request.setAttribute( "termList", termList );
        return "mall/purchase/phone/xiangqing";
    }
}
