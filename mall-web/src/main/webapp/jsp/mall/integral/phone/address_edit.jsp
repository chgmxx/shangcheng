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
    <title>编辑地址</title>
    <script src="/js/mall/integral/camel.js"></script>
    <script src="/js/mall/integral/swiper-3.3.1.min.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <link rel="stylesheet" href="/css/mall/integral/swiper-3.3.1.min.css">
    <link rel="stylesheet" href="/css/mall/integral/global.css">
    <link rel="stylesheet" href="/css/mall/integral/public.css">
    <style type="text/css">
        .form-item .item-label {
            padding-left: 10px;
            text-align: left;
        }

        .form-item {
            background: white;
            align-items: flex-start;
        }

        textarea {
            resize: none !important;
        }

        select {
            width: 100%;
            appearance: none;
            -webkit-appearance: none;
            height: 44px;
            border: none;
        }
    </style>
</head>
<body>


<form class="form" method="post" id="addressForm">
    <input type="hidden" name="id" id="id" value="${mem.id }"/>
    <input type="hidden" name="memDefault" id="memDefault" value="${mem.memDefault }"/>
    <div class="form-item">
        <label class="item-label">收货人</label>
        <div class="item-field">
            <input type="text" class="f-text f-text--right-icon" placeholder="请输入姓名" name="memName" id="memName" value="${mem.memName }">
        </div>
    </div>
    <div class="form-item">
        <label class="item-label">联系电话</label>
        <div class="item-field">
            <input type="tel" class="f-text" placeholder="请输入联系电话" name="memPhone" id="memPhone" maxlength="11" value="${mem.memPhone }">
        </div>
    </div>
    <div class="form-item">
        <label class="item-label">选择省份</label>
        <div class="item-field">
            <!-- <input type="text" class="f-text" readonly="readonly" placeholder="请选择地区"> -->
            <select id="province" name="memProvince" onchange="clearAddress();">
                <option value="0">请选择省份</option>
                <c:forEach var="map" items="${maps }">
                    <option <c:if test="${mem.memProvince==map.id}"> selected="selected"</c:if> value="${map.id }">${map.city_name}</option>
                </c:forEach>
            </select>
        </div>
        <icon class="icon-v-right"></icon>
    </div>
    <div class="form-item">
        <label class="item-label">选择市</label>
        <div class="item-field">
            <select name="memCity" id="city" cityid="${mem.memCity }" onchange="clearAddress();"></select>
        </div>
        <icon class="icon-v-right"></icon>
    </div>
    <div class="form-item">
        <label class="item-label">选择区/县</label>
        <div class="item-field">
            <select name="memArea" id="area" disid="${mem.memArea }" onchange="getLng(1);clearAddress();"></select>
        </div>
        <icon class="icon-v-right"></icon>
    </div>

    <div class="form-item">
        <label class="item-label">详细地址</label>
        <div class="item-field">
            <c:if test="${!empty isJuliFreight }">
                <textarea type="text" class="f-text integral-text-area" placeholder="请输入地址" name="memAddress" id="memAddress" placeholder="点击选择详细地址" onclick="openMap();"
                          readonly="readonly">${mem.memAddress }</textarea>
            </c:if>
            <c:if test="${empty isJuliFreight }">
                <textarea type="text" class="f-text integral-text-area" placeholder="请输入地址" name="memAddress" id="memAddress" placeholder="点击选择详细地址">${mem.memAddress }</textarea>
            </c:if>
        </div>
    </div>


</form>
<div class="integral-bottom integral-address">
    <div class="integral-bottom-btn" id="save">确定</div>
</div>
<input type="hidden" class="shopId" value="${shopId }"/>
<input type="hidden" class="userid" value="${userid }"/>

<c:if test="${!empty orders }">
    <c:forEach var="i" items="${orders }">
        <input type="hidden" name="${i.key }" class="formCla" value="${i.value }"/>
    </c:forEach>
</c:if>

<form action="/phoneIntegral/79B4DE7C/addressList.do?uId=${userid }&shopId=${shopId }" id="toAddList" method="post">
    <input type="hidden" class="orders" name="orders"/>
</form>

<iframe id="geoPage" width=0 height=0 frameborder=0 style="display:none;" scrolling="no"
        src="https://apis.map.qq.com/tools/geolocation?key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp">
</iframe>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=ZpmmFnpf7ZalTwV2Urf3Q4N2"></script>
<script type="text/javascript" src="/js/mall/integral/phone/address_edit.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
</body>
</html>