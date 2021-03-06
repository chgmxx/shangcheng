<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <title>销售规则</title>
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
<link rel="stylesheet" type="text/css" href="/css/mall/seller/main.css?<%=System.currentTimeMillis()%>"/>
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<div class="sWrapper">
    <header class="s-header">
        <div class="s-saler">
            <c:set var="headImages" value="/images/mall/img/pt-detail2.jpg"></c:set>
            <c:set var="userName" value="${seller.userName }"></c:set>
            <c:if test="${!empty member }">
                <c:if test="${!empty member.headimgurl }">
                    <c:set var="headImages" value="${member.headimgurl }"></c:set>
                </c:if>
                <c:if test="${empty userName }">
                    <c:set var="userName" value="${member.nickname }"></c:set>
                </c:if>
            </c:if>
            <p class="bg-saler" style="background-image: url('${headImages}');background-size: cover;"></p>
            <p class="s-name">${userName }</p>
            <p class="s-join-time">加入时间：<c:if test="${!empty seller.addTime}"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${seller.addTime }"/></c:if></p>
        </div>
        <ul class="s-effect">
            <li>
                <a href="/phoneSellers/1/79B4DE7C/totalIncome.do?uId=${userid}">
                    <p class="effect-text">累计佣金(元)</p>
                    <p class="bfz-1">${seller.totalCommission }</p>
                </a>
            </li>
            <li>
                <a href="/phoneSellers/2/79B4DE7C/totalIncome.do?uId=${userid}">
                    <p class="effect-text">积分</p>
                    <p class="bfz-1">${seller.incomeIntegral }</p>
                </a>
            </li>
            <li>
                <a href="/phoneSellers/3/79B4DE7C/totalIncome.do?uId=${userid}">
                    <p class="effect-text">销售总额</p>
                    <p class="bfz-1">${seller.saleMoney }</p>
                </a>
            </li>
        </ul>
    </header>
    <section class="ru-body">
        <p>经纪人说明</p>
        <c:if test="${!empty sellerSet }">
            <c:if test="${!empty sellerSet.sellerRemark }">
                <c:set var="sellerRemark" value="${sellerSet.sellerRemark }"></c:set>
            </c:if>
        </c:if>
        <c:if test="${!empty sellerRemark }">
            ${sellerRemark }
        </c:if>
        <c:if test="${empty sellerRemark }">
            <ul class="rule-list">
                <li>
                    当消费金额满2000元可申请成为全民经纪人。
                </li>
                <li>
                    当A消费达到规定金额成为全民经纪人后，B通过A的推荐并进行消费，则A
                    可获得相对应的佣金提成，而B消费达到规定金额后亦可申请成为全民经纪人，；
                    C通过B的推荐且进行消费，则B可获得相对应的佣金提成，而A无任何收入，以此类推，此过程为经纪人代理。
                </li>
                <li>
                    所获佣金达到最低提现要求且满7天，将自动提现到个人微信钱包。
                </li>
            </ul>
        </c:if>
    </section>
</div>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/applySeller.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript">

    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${record.get('appid')}", // 必填，公众号的唯一标识
        timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
        nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
        signature: "${record.get('signature')}",// 必填，签名，见附录1
        jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.ready(function () {
        wx.hideOptionMenu();
    });


</script>
</body>
</html>