<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商品管理-商品分组</title>
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
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common/Fan-index.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        var error = '${error}';
        if (error != undefined && error != "") {
            parent.layer.alert("参数错误，将调回前一个页面");
            window.history.back(-1);
        }
        if (top == self) {
            window.location.href = "/mPro/product_start.do";
        }
    </script>
    <style type="text/css">
    </style>
</head>
<body>
<div id="con-box">
    <div class="con-head" style="margin:0px;">
        <a class="" href="/mPro/index.do">商品管理</a>
        <a class="navColor" href="/mPro/group/group_index.do">分组管理</a>
        <a class="" href="/mPro/group/label_index.do">搜索推荐管理</a>
    </div>
    <c:if test="${!empty shoplist }">
        <div class="con-box1">
            <div class="txt-btn">

                <c:if test="${empty pId }">
                    <div class="blue-btn fl box1Btn">
                        <a href="/mPro/group/to_edit.do">新建分组</a>
                    </div>
                </c:if>
                <c:if test="${!empty pId }">
                    <div class="blue-btn fl box1Btn" style="width: auto;">
                        <a id="layShow" href="/mPro/group/to_edit.do?pId=${pId }">新建子类分组</a>
                    </div>
                    <div class="green-btn fl box1Btn">
                        <a href="/mPro/group/group_index.do" class="backBtn">返回</a>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="msg-list">
            <ul id="list group_ul">
                <li class="txt-tle">
                    <span class="f8 fl">分组名称</span>
                    <span class="f8 fl">商品数量</span>
                    <span class="f8 fl">所属店铺</span>
                    <span class="f8 fl">创建时间</span>
                    <span class="f8 fl">操作</span>
                </li>

                <c:if test="${!empty page.subList}">
                    <c:forEach var="group" items="${page.subList }">
                        <li class="txt-tle">
                            <span class="f8 fl"><c:if test="${!empty group.groupName}">${group.groupName }</c:if></span>
                            <span class="f8 fl"><c:if test="${!empty group.COUNT}">${group.COUNT }</c:if><c:if test="${empty group.COUNT}">0</c:if></span>
                            <span class="f8 fl" style="line-height: 25px;"><c:if test="${!empty group.sto_name}">${group.sto_name }</c:if></span>
                            <span class="f8 fl"><c:if test="${!empty group.createTime}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${group.createTime }"/></c:if></span>
                            <span class="f8 fl">
							<%-- <a href="/mPro/group/to_edit.do?pId=${group.id }" class="addChild" style="display:none;">添加子分类</a> --%>
							<a href="/mPro/group/to_edit.do?id=${group.id }" class="editGroup" style="background: none;">编辑</a>
							<a href="javascript:void(0);" id="${group.id}" class="deleteGroup" onclick="deleteGroup(this);" style="background: none;">删除</a>
							<c:if test="${empty pId }">
                                <a href="/mPro/group/group_index.do?pId=${group.id }" class="editGroup" style="background: none;">子类列表</a>
                            </c:if>
						</span>
                        </li>
                        <%--  <c:if test="${!empty group.childGroup}">
                            <c:forEach var="child" items="${group.childGroup}">
                            <li class="txt-tle" >
                                <span class="f8 fl"><c:if test="${!empty child.groupName}">${child.groupName }</c:if></span>
                                <span class="f8 fl"><c:if test="${!empty child.count}">${child.count }</c:if></span>
                                <span class="f8 fl"><c:if test="${!empty child.createTime}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${child.createTime }" /></c:if></span>
                                <span class="f8 fl">
                                    <a href="/mPro/group/to_edit.do?id=${child.id }" class="editGroup" style="background: none;">编辑</a>
                                    <a href="javascript:void(0);" id="${child.id}" class="deleteGroup" onclick="deleteGroup(this);" style="background: none;">删除</a>
                                </span>
                            </li>
                            </c:forEach>
                        </c:if> --%>

                    </c:forEach>
                </c:if>
            </ul>
            <c:if test="${! empty page.subList }">
                <input type="hidden" id="pId" value="${pId }"/>
                <jsp:include page="pageProduct.jsp"></jsp:include>
            </c:if>
        </div>
    </c:if>
    <c:if test="${empty shoplist }">
        <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
    </c:if>
    <!--内容编辑行end-->
</div>
<!--中间信息结束-->
<!--container  End-->
<!--footer  End-->
<script type="text/javascript" src="/js/mall/group.js"></script>
</body>
</html>