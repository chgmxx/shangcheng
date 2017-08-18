<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";

    %>

    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>商品分组弹出框</title>

    <link rel="stylesheet" type="text/css" href="/css/mall/groupPopUp.css"/>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="area-modal">
    <!--  <div class="area-head">选择可配送区域</div> -->
    <!--   <div class="area-main"> -->
    <div class="shopDiv">
        <c:if test="${!empty shoplist }">
            所属店铺:
            <select class="shopSelect">
                <c:forEach var="shop" items="${shoplist }">
                    <option
                            <c:if test="${shopId == shop.id }">selected="selected"</c:if> id="${shop.id }">${shop.sto_name }</option>
                </c:forEach>
            </select>
        </c:if>
    </div>
    <div class="area-editor">
        <h4>可选分组</h4>
        <c:if test="${!empty list }">
            <ul class="area-editor-list">
                <c:forEach var="group" items="${list }">
                    <li>
                        <div class="area-editor-list-title area-editor-list-parent  <c:if test='${group.select==1 }'>area-editor-list-select</c:if>">
                            <div class="area-editor-list-title-content">
                                <div class="area-editor-ladder-toggle">+</div>
                                    ${group.groupName }
                                <input type="hidden" class="groupId" value="${group.id }"/>
                                <input type="hidden" class="groupName" value="${group.groupName }"/>
                                <input type="hidden" class="shopId" value="${group.shopId }"/>
                            </div>
                        </div>
                        <c:if test="${!empty group.childGroupList }">
                            <ul class="area-editor-list area-editor-list1" style="display: none">
                                <c:forEach var="childGroup" items="${group.childGroupList }">
                                    <li>
                                        <div class="area-editor-list-title <c:if test='${childGroup.select==1 }'>area-editor-list-select</c:if>">
                                            <div class="area-editor-list-title-content">
                                                    ${childGroup.groupName }
                                                <input type="hidden" class="groupId" value="${childGroup.id }"/>
                                                <input type="hidden" class="groupName" value="${childGroup.groupName }"/>
                                                <input type="hidden" class="shopId" value="${childGroup.shopId }"/>
                                            </div>
                                        </div>
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </div>
    <!--  </div> -->
    <div class="area-btn">
        <c:if test="${type == 0 }">
            <button class="area-confirm" onclick="ok();">确认</button>
            <button class="area-close" onclick="cancel();">取消</button>
        </c:if>
        <c:if test="${type == 1 }">
            <input type="hidden" class="proId" value="${proId}"/>
            <button class="area-confirm" onclick="subtmit();">确认同步</button>
            <button class="area-close" onclick="cancel();">取消同步</button>
        </c:if>
    </div>
</div>
<input type="hidden" class="shopId" value="${shopId }"/>
</body>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/groupPopUp.js"></script>
</html>