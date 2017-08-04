<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>底部菜单</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" id="meta"/>
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
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
</head>
<body>
<c:if test="${!empty footerMenuMap }">
    <footer id="shop-nav">
        <ul class="flex">
            <c:if test="${!empty footerMenuMap.home && footerMenuMap.home == 1 }">
                <li class="flex-1 home_li">
                    <a href="javascript:void(0)" onclick="pageclick('${pageid}')">
                        <img src="/images/mall/img/footer1.png"/>
                        <label>首页</label>
                    </a>
                </li>
            </c:if>
            <c:if test="${!empty footerMenuMap.group && footerMenuMap.group == 1 }">
                <li class="flex-1 group_li">
                    <a href="javascript:void(0);" onclick="shoppingall();">
                        <img src="/images/mall/img/footer3.png"/>
                        <label>分类</label>
                    </a>
                </li>
            </c:if>
            <c:if test="${!empty footerMenuMap.cart && footerMenuMap.cart == 1 }">
                <li class="flex-1 cart_li">
                    <a href="javascript:void(0);" onclick="shopCart();">
                        <img src="/images/mall/img/footer5.png" alt=""/>
                        <label>购物车</label>
                    </a>
                </li>
            </c:if>
            <c:if test="${!empty footerMenuMap.my && footerMenuMap.my == 1 }">
                <li class="flex-1 my_li">
                    <a href="javascript:void(0);" onclick="toUsers();">
                        <img src="/images/mall/img/footer7.png" alt=""/>
                        <label>我的</label>
                    </a>
                </li>
            </c:if>
        </ul>
    </footer>
</c:if>
<input type="hidden" class="footerSaleMemberId" value="${saleMemberId }"/>
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script type="text/javascript">
    var urls = window.location.href;
    if (urls.indexOf("pageIndex") > 0 || urls.indexOf("mallIndex") > 0) {
        $(".home_li").addClass("nav")
        $(".home_li img").attr("src", "/images/mall/img/footer2.png");
    } else if (urls.indexOf("shoppingall") > 0 || urls.indexOf("groupbuyall") > 0 || urls.indexOf("groupbuyall") > 0 || urls.indexOf("groupbuyall") > 0 || urls.indexOf("groupbuyall") > 0) {
        $(".group_li").addClass("nav")
        $(".group_li img").attr("src", "/images/mall/img/footer4.png");
    } else {
        $(".my_li").addClass("nav")
        $(".my_li img").attr("src", "/images/mall/img/footer8.png");
    }
</script>
</body>
</html>