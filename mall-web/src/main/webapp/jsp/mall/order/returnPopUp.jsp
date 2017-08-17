<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";

    %>

    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>订单弹出框</title>

    <link rel="stylesheet" type="text/css" href="/css/mall/order.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="returnLayer">
    <form id="returnForm">
        <input type="hidden" name="returnId" class="returnId" value="<c:if test="${!empty oReturn }">${oReturn.id }</c:if>"/>
        <input type="hidden" name="orderNo" value="${oNo }"/>
        <input type="hidden" name="orderPayNo" value="${orderPayNo }"/>
        <input type="hidden" name="totalFee" value="<c:if test="${!empty oReturn }">${oReturn.retMoney }</c:if>"/>
        <input type="hidden" name="orderId" class="orderId" value="<c:if test="${!empty oReturn }">${oReturn.orderId }</c:if>"/>
        <input type="hidden" class="detailId" value="<c:if test="${!empty oReturn }">${oReturn.orderDetailId }</c:if>"/>
        <input type="hidden" class="type" value="${type }"/>
        <div class="r_tip">
            <c:set var="payName" value="微信安全支付"></c:set>
            <c:if test="${order.orderPayWay == 9 }">
                <c:set var="payName" value="支付宝安全支付"></c:set>
            </c:if>
            <c:if test="${type == -1 }">
                建议您与买家协商后，再确定是否拒绝退款。如您拒绝退款后，买家可修改退款申请协议重新发起退款。 也可直接发起维权申请。
            </c:if>
            <c:if test="${type == 1 }">
                该笔订单通过 “${payName }－代销” 付款， 需您同意退款申请，买家才能退货给您； 买家退货后您需再次确认收货后，退款将自动原路退回至买家付款账户；
            </c:if>
            <c:if test="${type == 2 || type == 5}">
                该笔订单通过 “${payName }－代销” 付款， 需您同意退款申请，买家才能退货给您； 买家退货后您需再次确认收货后，退款将自动原路退回至买家付款账户；
            </c:if>
            <c:if test="${type == 3 }">
                该笔订单通过 “${payName }－代销” 付款， 需您同意退款申请，买家才能退货给您； 买家退货后您需再次确认收货后，退款将自动原路退回至买家付款账户；
            </c:if>
            <c:if test="${type == 4 }">
                该笔订单通过 “${payName }－代销” 付款， 需您同意退款申请，买家才能退货给您； 买家退货后您需再次确认收货后，退款将自动原路退回至买家付款账户；
            </c:if>
        </div>
        <div class="r_div">
            <span class="tip">处理方式：</span>
            <span class="tip_right r_way">
		<c:if test="${!empty oReturn }">
            <c:if test="${order.orderPayWay != 2 && order.orderPayWay != 6}">
                <c:if test="${oReturn.retHandlingWay == 1 }">我要退款，但不退货</c:if>
                <c:if test="${oReturn.retHandlingWay == 2 }">我要退款退货</c:if>
            </c:if>
            <c:if test="${order.orderPayWay == 2 || order.orderPayWay == 6}">
                <c:if test="${oReturn.retHandlingWay == 1 }">我要退货</c:if>
                <c:if test="${oReturn.retHandlingWay == 2 }">我要退货</c:if>
            </c:if>
        </c:if></span>
        </div>
        <c:set var="money" value="0.00"></c:set>
        <c:if test="${!empty oReturn }">
            <c:set var="money" value="${oReturn.retMoney }"></c:set>
        </c:if>
        <c:if test="${!empty order }">
            <c:if test="${(order.orderPayWay == 2 || order.orderPayWay == 6 ) && order.isWallet == 0}">
                <c:set var="money" value="0.00"></c:set>
            </c:if>
        </c:if>
        <div class="r_div">
            <span class="tip">退款金额：</span>
            <span class="tip_right r_money">¥ ${money }</span>
        </div>
        <c:if test="${type == -1 }">
            <div class="r_div">
                <span class="tip">拒绝理由：</span>
                <span class="tip_right r_money"><textarea class="noReturnReason" rows="5" cols="30" placeholder="请填写您的拒绝理由"></textarea></span>
            </div>
        </c:if>
        <c:if test="${type == 2  || type == 5}">
            <div class="r_div">
                <span class="tip">退货地址：</span>
                <span class="tip_right r_money"><textarea class="returnAddress" rows="6" cols="30"
                                                          placeholder="填写您的完整收货地址信息，以便买家可退货给您!如，浙江省杭州市有赞区致富路888号，张三，13588888888 ，由买家承担寄回运费。"><c:if
                        test="${!empty oReturn }">${oReturn.returnAddress }</c:if></textarea></span>
            </div>
        </c:if>
        <c:if test="${type == 3 || type == 4 }">
            <div class="r_div">
                <span class="tip">退货地址：</span>
                <span class="tip_right r_money"><c:if test="${!empty oReturn }">${oReturn.returnAddress }</c:if></span>
            </div>
            <div class="r_div">
                <span class="tip">物流公司：</span>
                <span class="tip_right r_money"><c:if test="${!empty oReturn }">${oReturn.wlCompany }</c:if></span>
            </div>
            <div class="r_div">
                <span class="tip">物流单号：</span>
                <span class="tip_right r_money"><c:if test="${!empty oReturn }">${oReturn.wlNo}</c:if></span>
            </div>
            <div class="r_div">
                <span class="tip">备注信息：</span>
                <span class="tip_right r_money"><c:if test="${!empty oReturn }">${oReturn.wlRemark}</c:if></span>
            </div>
        </c:if>
        <div class="modal-footer clearfix" style="position:relative;display:inline-block;">
            <c:set var="isZhifubao" value="0"></c:set>
            <c:if test="${order.orderPayWay == 7 && !empty daifu }">
                <c:if test="${daifu.dfPayWay == 2 }">
                    <c:set var="isZhifubao" value="1"></c:set>
                </c:if>
            </c:if>
            <c:if test="${(type == 1 || type == 3 ) && order.orderPayWay == 9 }">
                <c:set var="isZhifubao" value="1"></c:set>
            </c:if>
            <c:if test="${isZhifubao == 1 }">
                <div style="color:red;margin-left: 38px">*建议您复制退款链接并用IE浏览器打开进行退款</div>
                <a href="${http }/alipay/79B4DE7C/refund.do?out_trade_no=${oNo }&busId=${busUserId}&desc=订单退款&fee=${money}" target="_blank"
                   class="ui-btn ui-btn-primary" style="padding:5px 10px;border-radius:2px;margin-left: 38px">
                    <c:if test="${type == -1 }">拒绝退款</c:if>
                    <c:if test="${type == 1 }">确认退款</c:if>
                    <c:if test="${type == 2 }">同意退款退货</c:if>
                    <c:if test="${type == 3 }">确认收货并退款</c:if>
                    <c:if test="${type == 4 }">未收到货，拒绝退款</c:if>
                    <c:if test="${type == 5 }">修改退货信息</c:if>
                </a>
                <a href="javascript:void(0);" class="ui-btn ui-btn-primary copy_a copy_public"
                   data-clipboard-text="${http }/alipay/79B4DE7C/refund.do?out_trade_no=${oNo }&busId=${busUserId}&desc=订单退款&fee=${money}" aria-label="复制成功！"
                   style="padding:5px 10px;border-radius:2px;margin-left: 10px">复制退款链接</a>
            </c:if>
            <c:if test="${isZhifubao == 0 }">
                <a href="javascript:;" class="ui-btn ui-btn-primary" id="submit"
                   style="padding:5px 10px;border-radius:2px;margin-left: 38px">

                    <c:if test="${type == -1 }">拒绝退款</c:if>
                    <c:if test="${type == 1 }">确认退款</c:if>
                    <c:if test="${type == 2 }">同意退款退货</c:if>
                    <c:if test="${type == 3 }">确认收货并退款</c:if>
                    <c:if test="${type == 4 }">未收到货，拒绝退款</c:if>
                    <c:if test="${type == 5 }">修改退货信息</c:if>
                </a>
            </c:if>
        </div>
        <input type="hidden" class="orderPayWay" value="${order.orderPayWay }"/>
    </form>
</div>
</body>
<script type="text/javascript" src="/js/mall/order/orderIndex.js"></script>
<script type="text/javascript">
    $(function () {

    });
</script>
</html>