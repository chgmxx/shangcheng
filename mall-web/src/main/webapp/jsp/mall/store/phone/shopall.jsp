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
    <title>全部店铺</title>
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

<link id="link" rel="stylesheet" type="text/css" href="/css/mall/shoppingall/index.css"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<!-- 头部分区结束 -->
<header class="w header">全部店铺</header>
<div class="main w">
    <c:forEach items="${shopList }" var="shop">
        <section class="brand" style="min-height: 4.2rem;height:auto;">
            <a href="javascript:void(0);" class="shop_a" pId="${shop.pageId}">
                <div class="pic-l l">
                    <c:set var="img" value="${shop.shopPicture }"></c:set>
                    <c:if test="${shop.shopPicture == null }">
                        <c:set var="img" value="${shop.stoPicture }"></c:set>
                    </c:if>
                    <span class="img-container" style="background: url(${http}${img}) no-repeat center center;background-size: contain;"></span>
                </div>
                <div class="text l" style="width: 7.92rem;">
                    <p style="height: auto">${shop.shopName}</p>
                    <p style="width: 7.92rem;">${shop.sto_address}</p>
                </div>
                <div class="clearfix"></div>
            </a>
        </section>
    </c:forEach>
</div>
<!-- 列表标题结束 -->

<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
</body>
<script>

    $(window).load(function () {
        setTimeout(function () {
            $(".loading").hide();
        }, 1000);
    });
    $(function () {
        $("div.fixRig").css({
            "right": "0.5rem"
        });
        $("div.fixRig a").css({
            "width": "1.12rem",
            "height": "1.12rem",
            "background-size": "auto 0.6rem"
        });
        $("div.fixRig a.top").css({
            "margin-bottom": "0.5rem"
        });
        $("a.shop_a").click(function () {
            var pageId = $(this).attr("pId");
            if (pageId != null && pageId != "") {
                location.href = "/mallPage/" + pageId + "/79B4DE7C/pageIndex.do";
            } else {
                alert("该店铺还没创建首页，不能点击");
            }
        });
    });
</script>
</html>