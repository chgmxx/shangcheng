<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>查看统计</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js?<%=System.currentTimeMillis()%>"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <%--<script type="text/javascript" src="/js/zclip/zclip.js"></script>--%>
    <script type="text/javascript" src="/js/common/copy.js"></script>

    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <style>
        .con-head a {
            color: #fff;
            text-align: center;
            font-size: 16px;
            background-color: #888;
            float: left;
            line-height: 50px;
            display: block;
            width: 140px;
            height: 50px;
            margin-right: 1px;
            cursor: pointer;
        }
    </style>
</head>

<div class="contentWarp">
    <div class="con-box">
        <div class="fl">
            <form action="/purchaseStatistics/statisticsIndex.do" method="post" id="statisticsForm">
                用户昵称：<input type="text" placeholder="请输入昵称" id="search" name="nickname" style="margin-bottom: 20px; height: 28px" value="${parms.nickname}">
                <input type="hidden" name="curPage" id="curPage">
                <input type="hidden" name="orderId" value="${parms.orderId}">
                <input type="button" class="btn" value="查询" onclick="submitForm()"/>
                <input type="button" value="返回" onclick="location.href='/purchaseOrder/orderIndex.do'" class="btn" style="margin-top: 50px"/>
            </form>
        </div>
    </div>
    <div class="ui-box">
        <table border="0" cellspacing="0" cellpadding="0" width="100%"
               class="ui-table">
            <tr>
                <th class="cell-8">头像</th>
                <th class="cell-8">昵称</th>
                <th class="cell-8">访问时间</th>
                <th class="cell-8">访问ip</th>
            </tr>
            <c:forEach items="${page.subList}" var="statistics">
                <tr>
                    <td>
                        <c:if test="${empty statistics.headimgurl ||  statistics.headimgurl ==''}">
                            <img src="/jsp/mall/purchase/baojia/image/defaulthead.png" width="60px" height="60px">
                        </c:if>
                        <c:if test="${!empty statistics.headimgurl && statistics.headimgurl !=''}">
                            <img src="${statistics.headimgurl}" width="60px" height="60px">
                        </c:if>
                    </td>
                    <td>${empty statistics.nickname?"游客":statistics.nickname}</td>
                    <td><fmt:formatDate value="${statistics.look_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>${statistics.look_ip}</td>
                </tr>
            </c:forEach>
        </table>
        <div class="list-footer-region ui-box">
            <c:if test="${!empty page.subList }">
                <jsp:include page="page.jsp"></jsp:include>
            </c:if>
        </div>
    </div>
    <c:if test="${!empty isNoAdminFlag }">
        <h1 class="groupH1">
            <strong>您还不是管理员，不能管理商城</strong>
        </h1>
    </c:if>
</div>
<script type="text/javascript" src="/js/mall/product/pro_index.js"></script>
<script type="text/javascript">
    function submitForm() {
        $("#statisticsForm").submit();
    }
</script>

</body>
</html>