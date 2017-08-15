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
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/html/htmlSelect.css?<%= System.currentTimeMillis()%>"/>
    <script src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <script src="/js/plugin/layer/layer.js?<%= System.currentTimeMillis()%>"></script>
</head>
<body>
<!--container-->
<div>
    <!--中间信息-->
    <div id="content">
        <div class="webTitle">
            <span class="i-con fl"></span><span class="title-p">h5商城模板列表</span>
        </div>
        <div class="webSelect">
            <span class="font14 fl">请选择你需要的模板进行编辑</span>
        </div>
        <div class="microSite">
            <ul class="microSiteUl">
                <c:forEach items="${map.list}" var="hlist">
                    <li class="microSiteListwcj">
                        <input type="hidden" class="pid" value="${hlist.id}"/>
                        <a>
                            <img src="${image}${hlist.bakurl}" alt="" width="146px" height="240px"
                                 style="display: inline;margin-left:16px;margin-top: 60px;">
                        </a>
                        <div class="microSiteUlTitle">${hlist.htmlname}</div>
                    </li>
                </c:forEach>
            </ul>
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
    </div>
    <!--中间信息结束-->
</div>
<form id="xinfrom" method="post" action="/mallhtml/updateHtml.do" target="_blank">
    <input type="hidden" name="id" id="id">
</form>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<script type="text/javascript">
    $(function () {
        var body = $("body").height() + 89;
        //TODO parent.$("#nav").height();
        var candan = parent.$("#nav").height();
        if (body < candan) {
            $("body").height(candan - 89);
        }
    })
    $(function () {

        $('.microSiteListwcj').mouseover(function () {
            $(this).removeClass("temBackImgOut");
            $(this).addClass("temBackImgOver");
        });
        //鼠标离开在li上的效果
        $('.microSiteListwcj').mouseout(function () {
            $(this).removeClass("temBackImgOver");
            $(this).addClass("temBackImgOut");
        });
        $(".microSiteListwcj").live("click", function () {
            var pid = $(this).find(".pid").val();
            htmllayer(pid);//选中的方法
        });
    });
    function htmllayer(pid) {
        parentOpenIframe('h5商城预览','820px', '500px','/mallhtml/ylmodel.do?id=' + pid);
//        parent.layer.open({
//            type: 2,
//            title: 'h5商城预览',
//            shadeClose: true,
//            shade: 0.2,
//            area: ['820px', '500px'],
//            offset: "10px",
//            content: '/mallhtml/ylmodel.do?id=' + pid, //iframe的url
//        });
    }

    function first(obj) {
        var currentPage = obj;
        if (1 < currentPage) {
            window.location.href = "/mallhtml/modelList.do?pageNum=1";
        } else {

        }
    }
    function previous(obj) {
        var currentPage = obj;
        if (1 < currentPage) {
            window.location.href = "/mallhtml/modelList.do?pageNum=${map.pageNum-1}";
        } else {
            parentAlertMsg("已经是最前一页");
//            parent.layer.alert("已经是最前一页", {
//                offset: "30%",
//                closeBtn: 0
//            });
        }
    }
    function next(obj, totalPages) {
        var currentPage = obj;
        if (currentPage < totalPages) {
            window.location.href = "/mallhtml/modelList.do?pageNum=${map.pageNum+1} ";
        } else {
            parentAlertMsg("已经是最后一页");
//            parent.layer.alert("已经是最后一页", {
//                offset: "30%",
//                closeBtn: 0
//            });
        }
    }
    function last(obj, totalPages) {
        var currentPage = obj;
        if (currentPage < totalPages) {
            window.location.href = "/mallhtml/modelList.do?pageNum=${map.pagetotal}";
        } else {

        }
    }
</script>
</body>
</html>