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

    <title>表单列表</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <link rel="stylesheet" type="text/css"  href="/css/mall/html/htmlfromlist.css?<%= System.currentTimeMillis()%>"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <script src="/js/plugin/layer/layer.js"></script>
</head>
<style>

    .share {
        background: url(/images/icon/exportiocn.jpg) -27px 21px;
        display: inline-block;
        width: 18px;
        height: 18px;
    }

    .share:hover {
        background: url(/images/icon/exportiocn.jpg) -2px 21px;
    }
</style>
<body>

<div class="fansTitle">
    <span class="i-con fl"></span><span class="title-p">表单列表</span>
</div>


<div class="box-btm30">
    <c:if test="${! empty map.list}">
        <div class="txt-tle">
            <div class="t2">商城名称</div>
            <div class="t2">创建时间</div>
            <div class="t2">属性</div>
            <div class="t2">属性值</div>
            <div class="t3">操作</div>
        </div>
    </c:if>
    <div class="msg-list">
        <div class="txt-tle2" id="list">
            <c:forEach items="${map.list}" var="obj">
                <li>
                    <div class="listb">${obj.htmlname }</div>
                    <div class="listb">${obj.creattime }</div>
                    <div class="listb">${obj.category }</div>
                    <div class="listb">${obj.categoryname }</div>
                    <div class="listc">
                        <div class="bianji" style="width: 160px;">
									
									<span onclick="preview('${obj.id}')"
                                          class="bj-a share" style="float: initial;" title="查看详情"> </span>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </div>
    </div>
</div>

<hr style="border: 1px solid #dedede;"/>
<c:if test="${! empty map.list}">
    <div class="box-btm30 clearfix">
        <div class="page">
            <ul>
                <li class="pgfocus"><a
                        href="javascript:first(${map.pageNum});">首页</a></li>
                <li class="pgfocus"><a
                        href="javascript:previous(${map.pageNum});">上一页</a></li>
                <li class="pgfocus current">第${map.pageNum}页</li>
                <li class="pgfocus"><a
                        href="javascript:next(${map.pageNum},${map.pagetotal});">下一页</a></li>
                <li class="pgfocus"><a
                        href="javascript:last(${map.pageNum},${map.pagetotal});">尾页</a></li>
                <li class="pgfocus">共${map.pagetotal}页</li>
            </ul>
        </div>
    </div>
</c:if>

<jsp:include page="/jsp/common/headerCommon.jsp"/>
</body>
<script type="text/javascript">
    $(function () {
        var body = $("body").height() + 89;
        var candan = $("#nav").height();
        if (body < candan) {
            $("body").height(candan - 89);
        }
    })
    //查看详情
    function preview(id) {
        layer.open({
            type: 2,
            title: '查看详情',
            shadeClose: true,
            shade:[0.2,"#fff"],
            area: ['500px', '500px'],
            offset: "10%",
            content: '/mallhtml/htmlfromview.do?id=' + id,
        });
    }
    //分页
    function first(obj) {
        var currentPage = obj;
        if (1 < currentPage) {
            window.location.href = "/mallhtml/htmlfromlist.do?pageNum=1&&id=${map.id}";

        } else {
            layer.alert("已经是最前一页", {
                offset: "10%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }
    function previous(obj) {
        var currentPage = obj;
        if (1 < currentPage) {

            var url = "/mallhtml/htmlfromlist.do?pageNum=${map.pageNum-1}&&id=${map.id}";


            window.location.href = url;

        } else {
            layer.alert("已经是最前一页", {
                offset: "10%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }
    function next(obj, totalPages) {
        var currentPage = obj;
        if (currentPage < totalPages) {

            var url = "/mallhtml/htmlfromlist.do?pageNum=${map.pageNum+1}&&id=${map.id}";

            window.location.href = url;

        } else {
            layer.alert("已经是最后一页", {
                offset: "10%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }
    function last(obj, totalPages) {
        var currentPage = obj;
        if (currentPage < totalPages) {

            var url = "/mallhtml/htmlfromlist.do?pageNum=${map.pagetotal}&&id=${map.id}";


            window.location.href = url;

        } else {
            layer.alert("已经是最后一页", {
                offset: "10%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }

</script>
</html>
