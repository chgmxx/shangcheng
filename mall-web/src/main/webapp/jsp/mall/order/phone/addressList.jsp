<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的地址</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>">
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
    <link rel="stylesheet" href="/css/common/init.css"/>
    <style type="text/css">
        body {
            min-height: 100%;
            position: relative;
            font-family: SimHei, "微软雅黑";
            font-size: 28px;
            color: #000;
        }

        .main {
            width: 870px;
            margin: 0 auto 100px auto;
        }

        .header {
            height: 90px;
            line-height: 90px;
            font-size: 33px;
            text-align: center;
        }

        .header .back {
            width: 25px;
            height: 100%;
            display: inline-block;
            float: right;
            position: relative;
            right: 40px;
            background: url(images/mall/delete.png) no-repeat 50% 50%;
        }

        .address-box {
            padding: 38px 0px;
            border-bottom: 1px solid #dfdfdf;
            border-top: 1px solid #dfdfdf;
            position: relative;
            margin-bottom: 20px;
        }

        .address-box .main-con {
            width: 718px;
            margin: 0 auto;
        }

        .address-box .box {
            height: 50px;
            line-height: 50px;
            display: flex;
            display: -webkit-flex;
        }

        .address-box .box label {
            color: #9d9d9d;
        }

        .address-box .box p {
            margin-left: 90px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
            max-width: calc(100% - 200px);
        }

        .address-box .check {
            display: inline-block;
            width: 47px;
            height: 56px;
            float: left;
            position: absolute;
            left: 15px;
            top: 50%;
            margin-top: -28px;
            background: url(images/mall/check.jpg) no-repeat center center;
        }

        .address-box .noCheck {
            display: inline-block;
            width: 47px;
            height: 56px;
            float: left;
            position: absolute;
            left: 15px;
            top: 50%;
            margin-top: -28px;
            background: url(images/mall/noCheck.png) no-repeat center center;
        }

        .address-box .edit {
            display: inline-block;
            width: 57px;
            height: 51px;
            float: left;
            position: absolute;
            right: 20px;
            top: 50%;
            margin-top: -25px;
            background: url(images/mall/edit.jpg) no-repeat center center;
        }

        .footer {
            position: fixed;
            bottom: 20px;
            width: 870px;
        }

        .footer .new-address {
            width: 828px;
            height: 83px;
            margin: 0 auto;
            display: block;
            text-align: center;
            color: #FFFFFF;
            font-size: 35px;
            background-color: #f23030;
            border: none;
            border-radius: 12px;
            -webkit-border-radius: 12px;
            -moz-border-radius: 12px;
            text-decoration: none;
            line-height: 83px;
        }

        .isAdvert {
            position: fixed;
            bottom: 80px;
            width: 870px;
            height: auto;
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
<link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<div class="main">
    <header class="header">
        <span>我的地址</span>
        <!-- <i class="back"></i> -->
    </header>
    <input type="hidden" id="addressManage" value="${addressManage }"/>
    <input type="hidden" id="addType" value="${addType }"/>
    <div style="padding-bottom: 150px;">
        <c:forEach var="address" items="${addressList }">
            <div class="address-box">
                <c:if test="${empty addressManage}">
                    <c:if test="${address.memDefault == 1 }">
                        <i class="check" onclick="checkAddress(this);" id="check_address">
                            <input type="hidden" name="id" id="id" value="${address.id }"/>
                        </i>
                    </c:if>
                    <c:if test="${address.memDefault == 2 }">
                        <i class="noCheck" onclick="checkAddress(this);" id="check_address">
                            <input type="hidden" name="id" id="id" value="${address.id }"/>
                        </i>
                    </c:if>
                </c:if>
                <a class="edit" href="javascript:void(0);" onclick="toEdit(${address.id },${userid });"></a>
                <div class="main-con">
                    <div class="box">
                        <label>姓名</label>
                        <p>${address.memName }</p>
                    </div>
                    <div class="box">
                        <label>电话</label>
                        <p>${address.memPhone }</p>
                    </div>
                    <div class="box">
                        <label>地址</label>
                        <p>${address.provincename }${address.cityname }${address.areaname}${address.memAddress }
                            <c:if test="${address.memZipCode != null && address.memZipCode != ''}">（${address.memZipCode}）</c:if>
                        </p>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <c:if test="${!empty isAdvert }">
        <div class="isAdvert" style="">
            <jsp:include page="/jsp/common/technicalSupport.jsp"></jsp:include>
        </div>
    </c:if>
    <footer class="footer">
        <a class="new-address" href="javascript:void(0);" onclick="toEdit(0,${userid });">新增我的地址</a>
    </footer>
</div>
<input type="hidden" class="userid" value="${userid }"/>


<form action="" method="post" id="toOrder" name="toOrder">
    <input name="addressId" type="hidden" class="addressId"/>
</form>

<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script>
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

    $(window).load(function () {
        var a = $(window).width(),
            b = $(window).height(),
            d = 870,
            meta = $("#meta");
        meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
        setTimeout(function () {
            $(".loading").hide();
        }, 1000);
    });

    function checkAddress(obj) {
        $('.address-box').find("#check_address").each(function (index) {
            $(this).removeClass("check");
            $(this).addClass("noCheck");
        });
        $(obj).removeClass("noCheck");
        $(obj).addClass("check");
        var id = $(obj).find("input").val();
        $.post("phoneOrder/79B4DE7C/updateDefault.do", {
            id: id
        }, function (result) {
            if (result.code == 1) {
                $(".addressId").val(id);
                document.toOrder.action="/phoneOrder/79B4DE7C/toOrder.do?uId=${userid }";
                document.toOrder.submit();
            } else {
                alert("设置默认地址失败，请稍后再试。");
            }
        }, "json");
    }
    function toEdit(addressId, userid) {
        var url = "/phoneOrder/79B4DE7C/toAddress.do?uId=" + userid;
        var addressManage = $("#addressManage").val();
        var addType = $("#addType").val();
        if (addressId != null && addressId != "" && typeof(addressId) != "undefined" && addressId != "0") {
            url += "&id=" + addressId;
        }
        if (addressManage != null && addressManage != "" && typeof(addressManage) != "undefined") {
            url += "&addressManage=" + addressManage;
        }

        if (addType != null && addType != "" && typeof(addType) != "undefined") {
            url += "&addType=" + addType;
        }
        location.href = url;
    }
</script>

</body>
</html>
