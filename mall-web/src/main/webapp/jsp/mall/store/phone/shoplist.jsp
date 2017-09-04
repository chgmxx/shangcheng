<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>全部店铺</title>
    <meta charset="UTF-8">
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
    <link rel="stylesheet" type="text/css" href="/css/member/phone/base.css"/>
    <link rel="stylesheet" type="text/css" href="/css/member/phone/init.css"/>
    <link rel="stylesheet" type="text/css" href="/css/member/phone/870.css"/>
    <link rel="stylesheet" type="text/css" href="/css/member/public.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <style type="text/css">
        body {
            min-height: 100%;
            position: relative;
            background-color: #e6e6e6;
            font-family: SimHei, "微软雅黑";
            font-size: 30px;
            color: #000;
        }

        .text-right {
            text-align: right;
        }

        .Warp {
            width: 870px;
            margin: 0 auto;
        }

        .main .store-list h2 {
            font-size: 32px;
            padding-left: 42px;
            height: 96px;
            line-height: 96px;
        }

        .main .store-list .list-detail {
            margin: 0 auto;
            background-color: #FFFFFF;
            color: #717070;
        }

        .main .store-list .list-detail div {
            height: 78px;
            line-height: 78px;
            padding: 0px 22px;
            background: url(/images/member/phone/9.png) no-repeat 805px center
        }

        .main .store-list .list-detail label {
            float: left;
            font-size: 29px;
        }

        .main .store-list .list-detail p {
            margin-left: 85px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            font-size: 28px;
            width: 680px;
            color: #717070
        }

        .store-list .list-detail .address {
            border-bottom: 1px solid #d0d0d0;

        }
    </style>
</head>
<body>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<div class="Warp">
    <header class="header">
        <span class="back" onclick="window.location='${urls}'"></span>
        <span>适用门店</span>
        <span class="index" onclick="pageclick('${pageid}')">首页</span>
    </header>

    <div class="main" style="padding-bottom: 100px;">
        <section class="store-list">
            <c:forEach items="${shopList }" var="store">
                <h2>${store.shopName }</h2>
                <div class="list-detail">
                    <div class="address">
                        <c:choose>
                            <c:when test="${store.longitude==null ||store.latitude==null}">
                                <label for="">地址：</label>
                                <c:if test="${fn:length(store.sto_address )>15}">${fn:substring(store.sto_address,0,15)}...</c:if>
                                <c:if test="${fn:length(store.sto_address )<=15}"> ${store.sto_address }</c:if>
                                </p>
                            </c:when>
                            <c:otherwise>
                                <label for="">地址：</label>
                                <a href="javascript:void(0);" onClick="txMap('${store.latitude}','${store.longitude }')">
                                    <p>
                                        <c:if test="${fn:length(store.sto_address )>15}">${fn:substring(store.sto_address,0,15)}...</c:if>
                                        <c:if test="${fn:length(store.sto_address )<=15}"> ${store.sto_address }</c:if>
                                        <!--   <img src="/images/member/phone/dh.png" width="30px" height="30px" style="padding-left:30px;"/> -->
                                    </p>
                                </a> </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="phone">
                        <label for="">电话：</label>
                        <a href="tel:${store.telephone }">
                            <p>${store.telephone }
                                <!-- <img src="/images/member/phone/lx.png" width="30px" style="padding-left:30px;" width="20px" height="30px"/> -->
                            </p>
                        </a></div>
                </div>
            </c:forEach>
        </section>
    </div>
</div>

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


    //调用地图
    function txMap(latitude, longitude) {
        var address = "";
        var url = "https://apis.map.qq.com/tools/poimarker?key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU&&referer=test&type=0";
        if (latitude != "-1" && longitude != "-1") {
            address = latitude + "," + longitude;
            url += "&marker=coord:" + address;
            window.location.href = url;
        }
    }
    function pageclick(obj) {
        if (obj == '' || obj == null || obj == undefined || obj == 0 || obj == '0') {
            alert("店面未设置微商城主页面或者微商城主页面已删除");
        } else {
            window.location.href = "/mallPage/" + obj + "/79B4DE7C/pageIndex.do";
        }
    }
</script>
</body>
</html>
