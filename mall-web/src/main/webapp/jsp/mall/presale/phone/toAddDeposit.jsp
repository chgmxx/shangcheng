<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<!DOCTYPE>
<html>
<base href="<%=basePath%>"/>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="HandheldFriendly" content="true">
    <meta name="MobileOptimized" content="320">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <meta name="full-screen" content="yes">
    <meta name="x5-fullscreen" content="true">
    <meta name="browsermode" content="application">
    <meta name="x5-page-mode" content="app">
    <meta name="msapplication-tap-highlight" content="no">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>${mapmessage.pro_name }-交纳定金</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
</head>

<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/mall/public.css?<%=System.currentTimeMillis()%>"/>
<link rel="stylesheet" type="text/css" href="/css/mall/auction/margin.css?<%=System.currentTimeMillis()%>"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<div class="Warp">
    <!-- <h1>用户预售服务协议</h1>
    <article>1.0.0用户应按照竞拍规则及商家设置的定金金额进行缴纳，且在竞拍成功后应在规定时间内完成付款（用户付款前应下载并查看成交确认书，并于提取成交拍品前与商家签署成交确认书）。用户逾期不付清成交款、佣金或其他费用的，自逾期之日起，商家每日按逾期支付金额的千分之五计收取滞纳金；同时，自用户未按约定期限支付任一期款项之日的次日起，商家有权即时单方面解除双方签订的《拍卖成交确认书》，不予退还用户缴纳的定金，追究用户的违约责任并要求用户赔偿商家及委托人因此所遭受的实际经济损失，包括但不限于：如竞拍标的再行组织竞拍活动，原获拍用户应当支付第一次竞拍活动中商家及委托人应当向商家支付的拍卖佣金；再行组织竞拍的成交价款低于原竞拍价款的，原获拍用户应当补足差额；并原获拍用户应当支付再次竞拍所产生的相关费用。
用户逾期不付清款项而造成违约的，系统将直接扣除用户缴纳的定金作为违约金用于赔付商家；商家应向用户出示相应凭据，但无需为扣除的定金给予用户开具发票或收据等凭证。至于其他的费用，如滞纳金、经济损失、佣金、差额费用等金额的追缴及法律责任的追究，由商家线下与用户自行联系完成。所涉款项不通过平台支付，平台因此不对此承担任何义务和法律责任。
1.1用户在付清全部款项后应按照商家要求的时间到存放地提取成交拍品；用户逾期未提取的，应向商家支付保管费，保管费的收费标准以标的物所在仓库公示的仓储费用为准。超过保管期限又不宜保存的物品，商家可依法再行组织竞拍，所得款项在扣除支出的费用后，剩余款项将退回原获拍用户。上述保管费的收取、剩余款项的退回，以及法律责任的追究，由商家线下与用户自行联系完成。商家应向用户出具所收取相应费用的凭据。
1.1.1 竞拍定金(简称“定金”或“佣金”)须知：
1.1.2 定金的缴纳：定金是竞拍人参加竞拍的凭证，如竞拍人有意参加相关商品竞拍活动，则须缴纳商家设置的参与竞拍活动的定金。每个商品的竞拍只需缴纳一次定金。
1.1.3 定金的处理：
1.1.4定金的返还以下三种情况下，将原路退还用户定金：
1.1.4.1用户未成功竞拍商品；
1.1.4.2用户竞拍成功后并在竞拍规则规定的时间内付款；
1.1.4.3用户竞拍成功后，商家主动关闭交易或未履约发货或因商家其他原因导致交易未完成的。
    </article>  -->
    <div class="agree" style="display: none;"><input type="checkbox" name="" id="agrea" value="" checked="checked"/>我已阅读并同意协议</div>
    <div class="info_div">
        <div class="info">
            <h2>订单信息</h2>
            <div class="info-item flex">
                <div class="flex-1">预定商品：</div>
                <div class="flex-3">${mapmessage.pro_name }</div>
            </div>
            <div class="info-item flex">
                <div class="flex-1">支付定金：</div>
                <div class="flex-3">${presale.depositPercent }</div>
            </div>
            <div class="info-item flex" onclick="showLay()">
                <div class="flex-1">支付方式：</div>
                <div class="flex-3" id="onlinePayment"><c:if test="${isWxPay == 1}">微信支付</c:if><c:if test="${isWxPay == 0 && isAliPay == 1}">支付宝支付</c:if> ></div>
            </div>
        </div>
        <form method="post" id="marginForm">
            <input type="hidden" class="specIds" name="proSpecificaIds" value="${specId }"/>
            <input type="hidden" class="proName" name="proName" value="${mapmessage.pro_name }"/>
            <input type="hidden" class="proImgUrl" name="proImgUrl" value="${imagelist.image_url }"/>
            <input type="hidden" class="proId" name="productId" value="${mapmessage.id }"/>
            <input type="hidden" class="aucId" name="presaleId" value="${presale.id }"/>
            <input type="hidden" class="marginMoney" name="depositMoney" value="${presale.depositPercent }"/>
            <input type="hidden" class="orderMoney" name="orderMoney" value="${orderMoney*proNum }"/>
            <input type="hidden" class="orderPayWay" name="payWay" value="<c:if test="${isWxPay == 1}">1</c:if><c:if test="${isWxPay == 0 && isAliPay == 1}">3</c:if>"/>
            <input type="hidden" class="proNum" name="proNum" value="${proNum }"/>
            <div class="text-right">
                <input type="button" class="recog-btn" name="" id="" value="交定金"/>
            </div>
        </form>
        <input type="hidden" class="shopId" value="${mapmessage.shop_id }"/>
    </div>
</div>
<!--遮罩层-->
<div class="fade" id="fade" onclick="hideLay()"></div>
<!--弹出层-->
<div class="pay-layer" id="payLayer">
    <div class="lay-item">支付方式
        <i class="delete" onclick="hideLay()"></i>
    </div>
    <c:if test="${isWxPay == 1}">
        <div class="lay-item" onclick="payWay(1);">
            <img src="/images/mall/pay-wx.png"/>
            <label for="">微信支付 </label>
        </div>
    </c:if>
    <c:if test="${isAliPay == 1}">
        <div class="lay-item" onclick="payWay(3);">
            <img src="/images/mall/pay-ali.png"/>
            <label for="">支付宝支付 </label>
        </div>
    </c:if>
    <c:if test="${memType == 3 }">
        <div class="lay-item" onclick="payWay(2);">
            <img src="images/mall/giftCard.png"/>
            <label for="">储值卡支付 </label>
        </div>
    </c:if>
</div>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="path" value="${path}"/>
<input type="hidden" class="alipaySubject" value="${alipaySubject }"/>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script type="text/javascript" src="/js/mall/presale/phone/deposit.js"></script>
<script type="text/javascript">
    $(window).load(function () {
        setTimeout(function () {
            $(".loading").hide();
        }, 1000);
    });
</script>
</body>
</html>