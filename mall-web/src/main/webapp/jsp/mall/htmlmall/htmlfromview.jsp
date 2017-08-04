<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE>
<html lang="en">
<head>
    <base href="<%=basePath%>">
    <meta name="renderer" content="webkit"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="Content-Disposition" content="attachment;">
    <meta http-equiv="expires" content="0">
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <style type="text/css">
        .tab {
            width: 100%
        }

        .tab tr {
            height: 32px;
        }

        .tab td {
            border-bottom: 1px solid #ccc;
        }

        .tab .td1 {
            text-align: right;
            width: 30%;
        }
    </style>
    <script type="text/javascript">

    </script>
    <title>订单详情</title>
</head>
<body>
<table class="tab" cellspacing="0" cellpadding="0">
    <tr>
        <td class="td1">h5商城名：</td>
        <td>${map.htmlname}</td>
    </tr>
    <tr>
        <td class="td1">创建时间：</td>
        <td>${map.creattime}</td>
    </tr>
    <c:if test="${map.category ne null }">
        <tr>
            <td class="td1">${map.category}：</td>
            <td>${map.categoryname}</td>
        </tr>
    </c:if>
    <c:if test="${map.genre ne null }">
        <tr>
            <td class="td1">${map.genre}：</td>
            <td>${map.genrename}</td>
        </tr>
    </c:if>
    <c:if test="${map.family ne null }">
        <tr>
            <td class="td1">${map.family}：</td>
            <td>${map.familyname}</td>
        </tr>
    </c:if>
    <c:if test="${map.property ne null }">
        <tr>
            <td class="td1">${map.property}：</td>
            <td>${map.propertyname}</td>
        </tr>
    </c:if>
    <c:if test="${map.nature ne null }">
        <tr>
            <td class="td1">${map.nature}：</td>
            <td>${map.naturename}</td>
        </tr>
    </c:if>
    <c:if test="${map.quality ne null }">
        <tr>
            <td class="td1">${map.quality}：</td>
            <td>${map.qualityname}</td>
        </tr>
    </c:if>
    <c:if test="${map.attribute ne null }">
        <tr>
            <td class="td1">${map.attribute}：</td>
            <td>${map.attributename}</td>
        </tr>
    </c:if>

</table>

</body>
</html>
