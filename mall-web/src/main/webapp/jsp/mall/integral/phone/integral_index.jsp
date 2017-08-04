<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title>积分商城</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css"/>
    <style type="text/css">
        strong {
            font-weight: bold !important;
        }
    </style>
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
    <div class="integral-shopping">
        <div class="integral-shopping-title">
            <p>我的积分</p>
            <c:if test="${!empty memberId }">
                <div>${memberIntegral }</div>
            </c:if>
            <c:if test="${empty memberId }">
                <div style="font-size: 0.5rem;" onclick="toLogin();">还未登陆，请先登陆</div>
            </c:if>
        </div>
        <div class="integral-shopping-tab flex flex-align-center ">
            <div class="flex-1 flex flex-pack-center" onclick="toRecord();">
                <img src="/images/mall/integral/event.png" alt="">
                <span>兑换记录</span>
            </div>
            <div class="flex-1 flex flex-pack-center" onclick="integralDetail();">
                <img src="/images/mall/integral/wealth.png" alt="">
                <span>积分明细</span>
            </div>
        </div>
        <c:if test="${!empty imageList }">
            <div class="swiper-container integral-swiper">
                <div class="swiper-wrapper">
                    <c:forEach var="image" items="${imageList }">
                        <div class="swiper-slide" onclick="returnUrls('${image.type}',this);">
                            <img src="${imageHttp }${image.imageUrl }" alt="">
                            <input type="hidden" class="return_url" value="${image.returnUrl }"/>
                        </div>
                    </c:forEach>
                </div>
                <!-- 如果需要分页器 -->
                <div class="swiper-pagination"></div>
            </div>
        </c:if>
    </div>
    <c:if test="${!empty productList }">
        <div class="media-list media-list--after-v integral-list product_list">
            <c:forEach var="product" items="${productList }">
                <div class="media-item" onclick="toProduct(${product.id});">
                    <div class="media-imgWrap">
                        <img src="${product.product_image }" class="item-img">
                    </div>
                    <div class="item-bd">
                        <h4 class="bd-tt">${product.pro_name }</h4>
                        <div class="bd-txt">会员积分兑换</div>
                        <div class="bd-txt integral-list-price ">${product.money }积分</div>
                    </div>
                </div>
            </c:forEach>
        </div>
        <c:if test="${pageCount > 1 }">
            <div class="load-more" onclick="loadMore();">加载更多</div>
            <input type="hidden" class="isLoading" value="1"/>
        </c:if>
    </c:if>
    <div class="empty"></div>
</div>
<input type="hidden" class="curPage" value="${curPage }"/>
<input type="hidden" class="pageCount" value="${pageCount }"/>
<input type="hidden" class="imageHttp" value="${imageHttp }"/>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="shopId" value="${shopId }"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script src="/js/mall/integral/phone/integral_index.js"></script>
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
</script>
</body>
</html>