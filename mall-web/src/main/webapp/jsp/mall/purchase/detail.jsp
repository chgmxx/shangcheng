<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
    <title>留言详情</title>
    <style type="text/css">
        .top-back-icon {
            display: inline-block;
            width: 20px;
            height: 20px;
            background-image: url(/images/mall/purchase/u1019.svg);
            background-repeat: no-repeat;
            background-position: center center;
            background-color: #DEDEDE;
            background-size: contain;
            cursor: pointer;
            margin-top: 24px;
        }
    </style>
</head>
<body>
<div class="warp">
    <div class="top-back"><a class="top-back-icon" href="#" onClick='javascript :history.back(-1);'></a></div>
    <div class="gt-bread-crumb">
        <span class="gt-bread-crumb-title">查看详情</span>
    </div>
    <table class="gt-table">
        <thead class="gt-table-thead">
        <tr>
            <th>序号</th>
            <th width="250">留言内容</th>
            <th>留言时间</th>
            <th width="250">回复内容</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="gt-table-tbody">
        <c:forEach items="${languageList}" var="language">
            <tr>
                <td>${language.id}</td>
                <td>${language.language_content}</td>
                <td><fmt:formatDate value="${language.language_time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                <td>${language.admin_content}</td>
                <td>
                    <c:if test="${empty language.admin_content || language.admin_content==''}">
                        <i class="iconfont" title="回复"
                           onclick="openRemarks('purchaseOrder/orderIndexRemarks.do?languageId=${language.id}&orderId=${language.order_id}',300,200,'回复')">&#xe60c;</i>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<script type="text/javascript">
    /**打开一个IFRAME窗口**/
    function openRemarks(content, winth, height, title) {
        parentOpenIframe(title, winth, height, content);
//        window.parent.openIframe(title, winth, height, content);
    }
</script>
</body>
</html>
