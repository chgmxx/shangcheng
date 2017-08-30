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
    <title><c:if test="${empty type || type == 1 }">累计佣金</c:if><c:if test="${type == 2 }">累计积分</c:if><c:if test="${type == 3 }">销售总额</c:if></title>
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
    <header class="arrow-header" onclick="javascript:location.href='/phoneSellers/79B4DE7C/sellerIndex.do?uId=${userid}'">
        <c:if test="${empty type || type == 1 }">累计佣金</c:if><c:if test="${type == 2 }">累计积分</c:if><c:if test="${type == 3 }">销售总额</c:if>
        (<span class="totalMoney">0</span>)
    </header>
    <c:set var="count" value="0"></c:set>
    <section class="s-body">
        <c:if test="${!empty incomeList }">
            <ul class="yj-list">
                <c:forEach var="income" items="${incomeList }">
                    <li>
                        <c:set var="headImages" value="/images/mall/img/pt-detail2.jpg"></c:set>
                        <c:if test="${!empty income.headimgurl }">
                            <c:set var="headImages" value="${income.headimgurl }"></c:set>
                        </c:if>
                        <div class="bg-yj-er" style="background-image: url('${headImages}'); background-size: cover;"></div>
                        <div class="float-desc">
                            <p>
                                <span>${income.nickname }</span>
                                <span class="yj-num">
                           <c:if test="${empty type || type == 1 }">&yen;${income.income_commission }</c:if>
                           <c:if test="${type == 2}">${income.income_integral }积分</c:if>
                           <c:if test="${type == 3}">&yen;${income.income_money }</c:if>
                           </span>
                            </p>
                            <p class="yj-text"><c:if test="${!empty income.income_time}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${income.income_time }"/></c:if></p>
                            <p class="yj-text oneOf">${income.det_pro_name }</p>
                        </div>
                        <c:if test="${empty type || type == 1 }">
                            <c:set var="count" value="${count+income.income_commission }"></c:set>
                        </c:if>
                        <c:if test="${type == 2}">
                            <c:set var="count" value="${count+income.income_integral }"></c:set>
                        </c:if>
                        <c:if test="${type == 3}">
                            <c:set var="count" value="${count+income.income_money }"></c:set>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </section>
</div>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
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
    var count = "${count}";
    if (count != null && count != "" && typeof(count) != "indefined") {
        count = count * 1;
        $(".totalMoney").html(count.toFixed(2));
    }


</script>
</body>
</html>