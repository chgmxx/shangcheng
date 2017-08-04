<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">

<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

        String setType = request.getParameter( "setType" );
        request.setAttribute( "setType", setType );
    %>
    <base href="<%=basePath%>"/>
    <meta charset="utf-8"/>
    <meta name="renderer" content="webkit"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <title>h5商城预览</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <!--[if IE 8]>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <script src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript">
    </script>
</head>
<body>
<div style="text-align: center">
    <img id="img" style="width: 200px;height:200px; margin: 0 auto;vertical-align: middle;" alt="扫一扫，查看效果" src="${url}">
</div>
</body>
</html>