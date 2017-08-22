<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>编辑上门自提</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" href="/css/common/init.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/take.css"/>
    <script charset="utf-8" type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
</head>

<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>

<div class="main" style="width:100%;">
    <header class="header flex" style="height:80px;line-height:80px;">
        <div class="flex-1">
            <a href="javascript:void(0);" onclick="closes(this);" id="${checkId}">取消</a>
        </div>
        <!-- <div class="flex-4">更换提货地址</div> -->
    </header>

    <div class="address-box">
        <ul>
            <c:forEach var="take" items="${takeList }">
                <c:set var="checkCla" value=""></c:set>
                <c:if test="${take.id == checkId && !empty checkId }">
                    <c:set var="checkCla" value="checked"></c:set>
                </c:if>
                <li class="flex" id="${take.id }">
                    <div class="inp">
                        <input type="checkbox" class="check ${checkCla }" name="check" id="${take.id }" value="${take.id }"
                        />
                    </div>
                    <div class="address">
                        <div>${take.visitName }</div>
                        <div class="text-overflow">收货地址：${take.visitAddressDetail }</div>
                    </div>
                </li>
            </c:forEach>
        </ul>
    </div>
    <input type="hidden" class="userid" value="${userid }"/>
    <jsp:include page="/jsp/mall/customer.jsp"></jsp:include>

    <script src="/js/jquery.min.js"></script>
    <script type="text/javascript" src="/js/mall/take/take_phone.js"></script>
    <script type="text/javascript">
        $(window).load(function () {
            var a = $(window).width(),
                b = $(window).height(),
                d = 870,
                meta = $("#meta");
            setTimeout(function () {
                meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
                $(".loading").hide();
            }, 300);
        });
    </script>
</body>
</html>
