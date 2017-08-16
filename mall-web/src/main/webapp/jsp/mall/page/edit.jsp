<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>页面管理</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/page/edit.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
</head>
<body>
<input type="hidden" class="urls" value="${urls }"/>
<div class="con_body">
    <div class="fansTitle">
        <span class="i-con fl"></span><span class="title-p">${pageTitle }</span>
    </div>

    <table class="table2" id="tab">

        <tr>
            <td class="table-td1">所属店铺：</td>
            <td class="table-td2"><select class="text1 " id="pagStoId"
                                          name="pagStoId">
                <!-- <option value="0">无</option> -->
                <c:forEach items="${allSto }" var="allSto">
                    <option ${allSto.id eq pag.pagStoId?'selected':'' } value="${allSto.id }">${allSto.sto_name}</option>
                </c:forEach>
            </select></td>
            <td><span class="tColor"></span></td>
        </tr>

        <tr>
            <td class="table-td1">页面类型：</td>
            <td class="table-td2"><select class="text1 " id="pagTypeId"
                                          name="pagTypeId">
                <!-- <option value="0">无</option> -->
                <c:forEach items="${typeMap }" var="map">
                    <option ${map.item_key eq pag.pagTypeId?'selected':'' } value="${map.item_key }">${map.item_value}</option>
                </c:forEach>
            </select></td>
            <td><span class="tColor"></span></td>
        </tr>

        <tr>
            <td class="table-td1">页面名称：</td>
            <td class="table-td2">
                <input type="hidden" value="${pag.id }" name="id" id="id"/>
                <input type="text" class="text1 abc" id="pagName" name="pagName"
                       placeholder="请输入页面名称,不能超过50字" maxlength="50" required="required" value="${pag.pagName }"/>
            </td>
            <td><span class="tColor"></span></td>
        </tr>

        <tr>
            <td class="table-td1">页面描述：</td>
            <td class="table-td2">
                <textarea class="text1 " name="pagDescript" id="pagDescript" style="height: 100px;" placeholder="请输入页面描述,不能超过500字" maxlength="500">${pag.pagDescript }</textarea>
            </td>
            <td><span class="tColor"></span></td>
        </tr>

        <tr>
            <td class="table-td1">是否主页：</td>
            <td class="table-td2"><input type="checkbox"
                                         name="pagIsMain"
                    <c:if test="${pag.pagIsMain == 1}"> checked="checked" </c:if> />

                <span id="picCodePrompt" class="tColor"
                      style="margin-left: 20px; color: orange">设为主页后，商城将已此页面为主页面。</span>
            </td>
            <td></td>
        </tr>

    </table>

    <div class="btn_box_div" style="margin-top: 30px;">
        <div class="btn_save_div">
            <input type="button" id="save" value="保存" class="blue-btn" onclick="save()"/>
        </div>
        <div class="btn_cancel_div" style="background-color: #8cc717;border-radius: 3px;font-size: 14px;">
            <a href="${urls }" style="cursor: pointer;color:white;">返回</a>
        </div>
    </div>
</div>
</body>
</html>
