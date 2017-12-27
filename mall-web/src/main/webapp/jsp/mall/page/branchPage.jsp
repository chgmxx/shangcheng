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

    <title>选择分类页</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/edit1.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/table.js"></script>

    <script type="text/javascript">
        /**点击确认**/
        function fnOk() {
            var jsonArry = [];
            $("input[name='genre']").each(function () {
                if ($(this).is(":checked")) {
                    var json = {};
                    $(this).parents("li").find("div").each(function () {
                        if ($(this).attr("name") != undefined) {
                            json[$(this).attr("name")] = $(this).text();
                        }
                    });
                    jsonArry.push(json);
                }
            });
            parent.returnBranch(jsonArry);//方法回调
            closeWindow();
        }


        function fnCancel() {
            closeWindow();
        }

        function closeWindow() {
            //当你在iframe页面关闭自身时
//            var index = layer.getFrameIndex(window.name); //先得到当前iframe层的索引
//            layer.close(index); //再执行关闭
            parent.layer.closeAll();
        }
    </script>
</head>

<body style="margin: 10px">
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div style="padding-bottom: 50px">
    <div class="box-btm30">

        <div class="txt-tle">
            <div class="t1">&nbsp;</div>
            <div class="t2" style="width: 30%;">分类名称</div>
            <div class="t3" style="width: 30%;">创建时间</div>
            <div class="t3" style="width: 30%;">分类描述</div>
        </div>

        <div class="msg-list">
            <div class="txt-tle2" id="list">
                <c:if test="${typeList != null }">
                    <c:forEach var="types" items="${typeList }">
                        <!-- 积分商品 -->
                        <li>
                            <div class="lista list-checkbox">
                                <input type="radio" value="-1"
                                       name="genre" onclick="event.stopPropagation()">
                            </div>
                            <div name="title" class="listb" style="width: 30%;">${types.name }</div>
                            <div class="listb" style="width: 30%;"></div>
                            <div class="listb" style="width: 30%;">${types.name }</div>
                            <div name="selecttype" class="listb" style="display: none">2</div><!--属性，1代表的是商品，2代表的是分类id-->
                            <div name="id" class="listb" style="display: none">-1</div>
                            <div name="url" class="listb" style="display: none">${ym}${types.url }</div>
                        </li>
                    </c:forEach>
                </c:if>
                <c:if test="${!empty page && !empty page.subList}">
                    <c:forEach items="${page.subList}" var="pro">
                        <li>
                            <div class="lista list-checkbox">
                                <input type="radio" value="${pro.id}"
                                       name="genre" onclick="event.stopPropagation()">
                            </div>
                            <div name="title" class="listb" style="width: 30%;">${pro.pag_name }</div>
                            <div class="listb" style="width: 30%;">${pro.pag_create_time }</div>
                            <div class="listb" style="width: 30%;">${pro.pag_descript }</div>
                            <div name="selecttype" class="listb" style="display: none">2</div><!--属性，1代表的是商品，2代表的是分类id  -->
                            <div name="id" class="listb" style="display: none">${pro.id }</div>
                            <div name="url" class="listb" style="display: none">${ym}/mallPage/${pro.id}/79B4DE7C/pageIndex.do</div>
                        </li>
                    </c:forEach>
                </c:if>
            </div>
        </div>
    </div>

</div>
<form id="queryForm" method="post" action="/mallPage/choosePro.do">
    <input type="hidden" name="stoId" value="${shopId }" class="srh"/>
</form>
<div style="width: 100%;height: 50px;text-align: center; position: fixed; bottom: 0">
    <jsp:include page="/jsp/common/page/pageView.jsp"></jsp:include>
    <div style="position: absolute;top:10; left:50;">
        <input type="button" value="确认" onclick="fnOk()" style="width:70px;  cursor: pointer;background-color: #1aa1e7;border-radius: 3px;color: #fff;border: none;"/>
        <input type="button" value="返回" onclick="fnCancel()" style="width:70px;  cursor: pointer;background-color: #8cc717;border-radius: 3px;color: #fff;border: none;"/>
    </div>
</div>
</body>
</html>
