<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>推荐审核</title>
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
    <link rel="stylesheet" type="text/css" href="/css/mall/seller/back/iconfont/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/seller/back/main.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>

    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="page-body">
    <ul class="page-tab">
        <li>
            <a href="/mallSellers/sellerSet.do">功能设置</a>
        </li>
        <li>
            <a href="/mallSellers/joinProduct.do">商品佣金设置</a>
        </li>
        <li>
            <a href="/mallSellers/sellerCheckList.do" class="tab-active">推荐审核</a>
        </li>
        <li>
            <a href="/mallSellers/sellerList.do">销售员管理</a>
        </li>
        <li>
            <a href="/mallSellers/withDrawList.do">提现列表</a>
        </li>
    </ul>
    <p class="page-topic">
        <span class="page-topic-bor"></span>
        <span class="page-topic-name">推荐审核列表</span>
    </p>
    <form id="searchForms">
        <div class="page-oper">
            <div class="oper-srh">
                <input type="text" class="srh-input keyWord" name="keyWord" placeholder="请输入推荐人姓名或手机" value="<c:if test="${!empty keyWord }">${keyWord }</c:if>"
                       onkeypress="pressSearch(event)">
                <span class="srh-icon"><i class="iconfont icon-search" onclick="searchUrl();"></i></span>
            </div>
        </div>
        <input type="hidden" class="curPage" name="curPage" value="1"/>
    </form>
    <table class="page-table">
        <thead>
        <tr>
            <td class="td-column-5">&nbsp;</td>
            <td class="td-column-10">姓名</td>
            <td class="td-column-12">手机</td>
            <td class="td-column-10">推荐人</td>
            <td class="td-column-10">推荐时间</td>
            <td class="td-column-10">审核状态</td>
            <td class="td-column-10">操作</td>
        </tr>
        </thead>
        <tbody>

        <c:if test="${!empty page }">
            <c:if test="${!empty page.subList }">
                <c:forEach var="seller" items="${page.subList }">
                    <c:set var="userName" value="${seller.user_name }"></c:set>
                    <c:set var="telephone" value="${seller.telephone }"></c:set>
                    <c:if test="${empty seller.user_name && !empty seller.nickname}">
                        <c:set var="userName" value="${seller.nickname }"></c:set>
                    </c:if>
                    <c:if test="${empty telephone && !empty seller.phone}">
                        <c:set var="telephone" value="${seller.phone }"></c:set>
                    </c:if>
                    <tr>
                        <td class="td-column-5"><input type="checkbox" class="check" value="${seller.id }" status="${seller.check_status }" /></td>
                        <td>${userName }</td>
                        <td>${telephone }</td>
                        <td>${seller.tj_user_name }</td>
                        <td><c:if test="${!empty seller.apply_time}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${seller.apply_time }"/></c:if></td>
                        <td>
                            <c:if test="${seller.check_status == 0 }">审核中</c:if>
                            <c:if test="${seller.check_status == 1 }">审核通过</c:if>
                            <c:if test="${seller.check_status == -1 }">审核不通过</c:if>
                            <c:if test="${seller.check_status == -2 }">待审核</c:if>
                        </td>
                        <td>
                            <c:if test="${seller.check_status == 0 }">
                                <a href="javascript:void(0);" class="iconfont icon-ok icon-edit" onclick="checkSeller(${seller.id},1);" title="审核通过"></a>
                                <a href="javascript:void(0);" class="iconfont icon-delete icon-edit" onclick="checkSeller(${seller.id},-1);" title="审核不通过"></a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </c:if>
        </tbody>
    </table>
    <c:if test="${!empty page }">
        <c:if test="${!empty page.subList }">
            <jsp:include page="/jsp/mall/seller/page/page2.jsp"></jsp:include>
        </c:if>
    </c:if>
</div>
<script type="text/javascript" src="/js/mall/seller/sellerPublic.js"></script>
</body>
</html>