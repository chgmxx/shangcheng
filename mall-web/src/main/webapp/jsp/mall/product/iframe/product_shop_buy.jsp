<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>到店购买</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>
    <style type="text/css">
        .downsQrs {
            color: #fff;
            font-size: 15px;
            height: 26px;
            background: #8FC41A;
            padding: 0 23px;
            cursor: pointer;
            display: inline-block;
            border-radius: 15px;
            line-height: 26px;
            text-decoration: none;
        }

        .blueQr {
            background: #48B9EF;
        }
    </style>
</head>
<body>
<div class="previewShopDiv">
    <div class="toShopDiv" style="text-align: center;">
        <img src='/mPro/79B4DE7C/getTwoCode.do?id=${id }&shopId=${shopId}'/>
    </div>
    <div style="width: 100%;margin-top:10px;height:29px;text-align:center;position:relative;display:inline-block;">
        <a class="downsQrs" href="/common/downQr.do?url=${html }">下载二维码</a>
        <a class="downsQrs blueQr copy copy_public" href="javascript:void(0);" data-clipboard-text="${html }" aria-label="复制成功！">复制链接</a>
        <input type="hidden" value="${html }"/>
    </div>
</div>
</body>
</html>