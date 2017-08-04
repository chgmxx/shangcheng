<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/weui-master/dist/style/weui.css"/>
    <link rel="stylesheet" href="/css/mall/purchase/phone/index.css">
    <link rel="stylesheet" href="/css/mall/purchase/phone/swiper.min.css">
    <script src="/js/plugin/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="/js/mall/purchase/phone/index.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/mall/purchase/phone/swiper.min.js" type="text/javascript"></script>
    <title>商品详情</title>
</head>
<body>
<div class="warp">
    <div class="quotes-carousel">
        <div class="swiper-container">
            <div class="swiper-wrapper">
                <c:forEach items="${imgList}" var="img">
                    <div class="swiper-slide">
                        <img src="${http}${img.image_url}" class="carousel_pic"/>
                    </div>
                </c:forEach>
            </div>
        </div>
        <div class="page">
            <span id="indexNum">1</span>/<span id="imgNum">
                <c:if test="${!empty imgList}">
                    ${imgList.size()}
                </c:if>
              	</span>
        </div>
    </div>

    <div class="quotes_fee">
        <h2>[编号：${orderDetails.productId}]${orderDetails.productName}</h2>
        <div class="quotes_fee_discount">
            <div class="colorf0">优惠价：￥<fmt:formatNumber type="number" value="${orderDetails.discountMoney}" pattern="0.00" maxFractionDigits="2"/></div>
            <div>原价：￥<fmt:formatNumber type="number" value="${orderDetails.money}" pattern="0.00" maxFractionDigits="2"/></div>
        </div>
        <div class="quotes_fee_other">
            <div>人工费：<fmt:formatNumber type="number" value="${orderDetails.laborCost}" pattern="0.00" maxFractionDigits="2"/>元</div>
            <div>安装费：<fmt:formatNumber type="number" value="${orderDetails.installationFee}" pattern="0.00" maxFractionDigits="2"/>元</div>
            <div>运费：<fmt:formatNumber type="number" value="${orderDetails.freight}" pattern="0.00" maxFractionDigits="2"/>元</div>
        </div>
    </div>

    <div style="width: 100%; height: 0.2rem; background-color: #FFFFFF"></div>

    <article class="weui-article">
        ${orderDetails.productDetails}
    </article>
</div>

<script type="text/javascript">
    $(function () {
        var mySwiper = new Swiper('.swiper-container', {
            autoplay: 3000,
            onSlideChangeStart: function (swiper) {
                $("#indexNum").text(mySwiper.activeIndex + 1);
            }
        });
    });
</script>
</body>
</html>