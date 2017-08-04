<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"  content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="/css/mall/purchase/phone/index.css">
    <link rel="stylesheet" type="text/css" href="/css/weui-master/dist/style/weui.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="/js/mall/purchase/phone/index.js" type="text/javascript" charset="utf-8"></script>
    <title>合同协议</title>
</head>
<body>
<div class="warp" style="height: 100%">
    <div class="hetong_main">
        <h1>${contract.contractTitle}</h1>
        <article class="hetong_detail">
            <div class="hetong_detail_">${contract.contractContent}</div>
        </article>
    </div>

    <div class="hetong_footer weui-btn-area">
        <a class="weui-btn quotes_btn_primary" href="/purchasePhone/79B4DE7C/buy.do?orderId=${orderId}&haveContract=1&busId=${busId}"
           id="showTooltips">阅读并同意本协议</a>
    </div>
</div>
</body>
</html>