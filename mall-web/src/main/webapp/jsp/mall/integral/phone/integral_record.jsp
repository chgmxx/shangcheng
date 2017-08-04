<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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
    <title>兑换记录</title>
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
<script src="/js/mall/integral/camel.js"></script>
<script src="/js/mall/integral/swiper-3.3.1.min.js"></script>
<link rel="stylesheet" href="/css/mall/integral/swiper-3.3.1.min.css">
<link rel="stylesheet" href="/css/mall/integral/global.css">
<link rel="stylesheet" href="/css/mall/integral/public.css">


<div>
    <div class="back-icon" onclick="toIndex('${urls}');">
        <img src="/images/mall/integral/intdegral-back.png" alt="">
    </div>

    <div class="integral-shopping">

        <div class="integral-shopping-title">
            <p>累计兑换</p>
            <div class="totalIntegrals">0</div>
        </div>


    </div>
    <c:if test="${!empty orderList }">
        <ul class="line-list line-list--indent line-list--flex">
            <c:forEach var="order" items="${orderList }">
                <li class="line-item">
                    <div class="item-bd">
                        <h3 class="bd-tt">${order.det_pro_name }</h3>
                        <p class="bd-txt ellipsis"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${order.times }"/></p>
                    </div>
                    <span class="item-append integral-detail-price">-${order.order_money }</span>
                    <input type="hidden" class="integral" value="${order.order_money }"/>
                </li>
            </c:forEach>
        </ul>
    </c:if>

    <!-- <div class="load-more">
        加载更多
    </div> -->
    <div class="empty"></div>
</div>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="shopId" value="${shopId }"/>

<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script src="/js/mall/integral/phone/integral_detail.js"></script>
<script type="text/javascript">
    $(".loading").hide();
    function toIndex(urls) {
        var userid = $(".userid").val();
        var shopId = $(".shopId").val();
        if (urls == null || urls == "") {
            location.href = "/phoneIntegral/" + shopId + "/79B4DE7C/toIndex.do?uId=" + userid;
        } else {
            location.href = urls;
        }
    }
    var totalIntegral = 0;
    $(".integral").each(function () {
        var val = $(this).val();
        if (val != null && val != "") {
            totalIntegral += val * 1;
        }
    });
    if (totalIntegral > 0) {
        $(".totalIntegrals").html(totalIntegral.toFixed(2));
    }
</script>
</body>
</html>