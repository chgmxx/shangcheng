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
    <title>积分明细</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css"/>
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

    <div class="back-icon" onclick="toIndex();">
        <img src="/images/mall/integral/intdegral-back.png" alt="">
    </div>

    <div class="integral-shopping">

        <div class="integral-shopping-title">
            <p>当前积分</p>
            <div>${memberIntegral }</div>
        </div>
    </div>
    <c:if test="${!empty integralList }">
        <ul class="line-list line-list--indent line-list--flex order_ul">
            <c:forEach var="integral" items="${integralList }">
                <li class="line-item">
                    <div class="item-bd">
                        <h3 class="bd-tt">${integral.itemName }</h3>
                        <p class="bd-txt ellipsis">${integral.createDate }</p>
                    </div>
                    <span class="item-append integral-detail-price">${integral.number }</span>
                </li>
            </c:forEach>
        </ul>
        <c:if test="${pageCount > 1 }">
            <div class="load-more" onclick="loadMore();">加载更多</div>
            <input type="hidden" class="isLoading" value="1"/>
        </c:if>
    </c:if>
    <div class="empty"></div>
</div>
<input type="hidden" class="curPage" value="${curPage }"/>
<input type="hidden" class="pageCount" value="${pageCount }"/>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="shopId" value="${shopId }"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script src="/js/mall/integral/phone/integral_detail.js"></script>
<script type="text/javascript">
    var mySwiper = new Swiper('.swiper-container', {
        direction: 'horizontal',
        loop: false,

        // 如果需要分页器
        pagination: '.swiper-pagination',
        observer: true,
        observeParents: true,
        onInit: function (mySwiper) {

        },
        onSlideChangeEnd: function (mySwiper) {

        }
    });
    $(".loading").hide();
    function toIndex() {
        var userid = $(".userid").val();
        var shopId = $(".shopId").val();
        location.href = "/phoneIntegral/" + shopId + "/79B4DE7C/toIndex.do?uId=" + userid;
    }
</script>
</body>
</html>