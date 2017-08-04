<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!--[if IE 8]>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <![endif]-->
    <title>账号过期页面</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";

        String setType = request.getParameter( "setType" );
        request.setAttribute( "setType", setType );
    %>
    <base href="<%=basePath%>">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/merchants/login/reg.css"/>
    <link rel="stylesheet" type="text/css" href="/css/pastPage.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/public.js"></script>
    <script type="text/javascript">
        $(function () {
            var url = "/trading/upGrade.do?setType=trading";
            var time = 5;
            setInterval(function () {
                if (time == 0) {
                    location.href = url;
                } else {
                    $("#msg").html(time);
                    time--;
                }
            }, 1000);
        })
    </script>
</head>
<body>
<!--头部开始-->
<div id="header" class="clearfix">
    <div class="container">
        <div class="headerLeft fl">
            <img src="/images/logoIndex.png" alt="logoImg"/>
        </div>
        <div class="headerCent clearfix">
            <ul class="clearfix">
                <li class="items"><a href="http://www.${domain}/jsp/office/index.jsp" class="white">首页</a></li>
                <li class="items"><a href="http://www.${domain}/jsp/office/product.jsp" class=" white">产品服务</a></li>
                <li class="items"><a href="http://www.${domain}/jsp/office/cooperation.jsp" class="white">合作召集</a></li>
                <li class="items"><a href="http://www.${domain}/jsp/office/help.jsp" class="white">帮助中心</a></li>
                <li class="items"><input type="button" value="登录" class="logBtn" onclick="window.location.href='/user/tologin.do'"/></li>
                <li class="items"><input type="button" value="注册" class="regBtn" onclick="window.location.href='/user/toregister.do'"/></li>
            </ul>
        </div>
    </div>
</div>
<!--头部结束-->
<div class="expiredCon">
    <div class="expired layer9">
        <div class="warp  bounceInDown">
            <h1>非常抱歉！</h1>
            <p class="expiredText">你的账号已经过期，请充值后再使用，感谢继续使用多粉</p>
            <p class="expBlue">自动跳转充值页面</p>
            <p class="countDown">倒计时</p>
            <div class="expTop"></div>
            <div class="expleft">
                <a href="/trading/upGrade.do?setType=trading">充值</a>
            </div>
            <div class="expright">
                <span id="msg">5</span>
            </div>
        </div>
    </div>
    <div class="layer0"></div>
    <div class="layer1"></div>
    <div class="layer2"></div>
    <div class="layer3"></div>
    <div class="layer4"></div>
    <div class="layer5"></div>
    <div class="layer6"></div>
    <div class="layer7"></div>
    <div class="layer8"></div>
</div>
</body>
</html>