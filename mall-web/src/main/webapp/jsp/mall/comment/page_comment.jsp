<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title></title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <script type="text/javascript" src="/js/plugin/layer/layer.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript">
        function page(curPage, url, type) {
            var curPagenew = "${page.curPage}";//已经是当前页
            /* if(type==2){
             if(curPage==curPagenew){
             layer.alert("已经是首页");
             return;
             }
             }
             if(curPage<1){
             layer.alert("已经是首页");
             return ;
             }
             var count = "
           ${page.pageCount}";
             if(count==0){
             layer.alert("没有下一页");
             }else{
             if(curPage-1==count){
             layer.alert("已经是最后一页");
             return;
             }
             if(type==0){
             if(curPagenew==count){
             layer.alert("已经是最后一页");
             return;
             }
             }
             } */
            url = url + curPage;


            var checkStatus = $("input.checkStatus").val();
            var feel = $("input.feel").val();
            if (checkStatus != null && checkStatus != "") {
                url += "&checkStatus=" + checkStatus;
            }
            if (feel != null && feel != "") {
                url += "&feel=" + feel;
            }
            location.href = url;
        }
    </script>
</head>

<body>
<div class="box-btm30 clearfix">
    <div class="page">
        <div class="fl right-10" id="div-allchoose">
            <input type="checkbox" class="allCheck" name="check" id="" value="" onclick="showMore(this)"/>
            <a href="javascript:;" class="ui-btn js-batch-checkAll">全选</a>

            <!-- <a href="javascript:;" class="ui-btn js-batch-tag" >改分组</a> -->
            <a href="javascript:;" class="ui-btn js-batch-agreen">通过</a>
            <a href="javascript:;" class="ui-btn js-batch-no-agreen">不通过</a>
            <a href="javascript:;" class="ui-btn js-batch-delete">删除</a>
        </div>
        <%-- <c:if test="${page.pageCount > 1}"> --%>
        <ul>
            <li class="pgfocus"><a
                    href="javascript:page(1,'${page.url }',2);">首页</a></li>
            <c:if test="${page.curPage-1 > 0}">
                <li class="pgfocus"><a
                        href="javascript:page(${page.curPage-1},'${page.url }',1);">上一页</a></li>
            </c:if>
            <c:if test="${page.curPage-1 == 0}">
                <li class="pgfocus">上一页</li>
            </c:if>

            <li class="pgfocus current">第<c:if test="${page.curPage==0 }">${page.curPage+1 }</c:if>
                <c:if test="${page.curPage != 0 }">${page.curPage }</c:if>页
            </li>

            <c:if test="${page.curPage+1 <=page.pageCount }">
                <li class="pgfocus"><a
                        href="javascript:page(${page.curPage+1},'${page.url }',1);">下一页</a></li>
            </c:if>
            <c:if test="${page.curPage+1 >page.pageCount }">
                <li class="pgfocus">下一页</li>
            </c:if>
            <li class="pgfocus"><a
                    href="javascript:page(${page.pageCount},'${page.url }',0);">尾页</a></li>
            <li class="pgfocus">共<c:if test="${page.pageCount==0 }">${page.pageCount+1 }</c:if>
                <c:if test="${page.pageCount != 0 }">${page.pageCount }</c:if>页
            </li>
        </ul>
        <%-- </c:if> --%>
    </div>
</div>
</body>
</html>
