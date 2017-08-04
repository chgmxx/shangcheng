<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html lang="en">
<head>

    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
        String setType = request.getParameter( "setType" );
        request.setAttribute( "setType", setType );
    %>
    <base href="<%=basePath%>"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta charset="utf-8"/>
    <title>h5商城</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <!--[if IE 8]>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <![endif]-->
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js?<%=System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="/js/public.js?<%=System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="/js/plugin/jquery-form.js?<%=System.currentTimeMillis()%>"></script>
    <script src="/js/plugin/layer/layer.js?<%=System.currentTimeMillis()%>"></script>

</head>
<body>
<!--navContainerBox-->
<div id="navContainerBox">

    <!--左边菜单栏-->
    <jsp:include page="/jsp/common/user/leftCommon.jsp"></jsp:include>
    <!--左边菜单栏结束-->
    <div id="container">
        <!--header-->
        <jsp:include page="/jsp/common/user/headerCommon.jsp"></jsp:include>
        <!--header  End-->
        <!--中间信息-->
        <div id="content" style="width: 1000px;">

            <iframe id="main" src="/mallhtml/htmllist.do" frameborder=0
                    style="width: 100%;" scrolling="no"></iframe>
            <!--中间信息结束-->
        </div>
    </div>
</div>
<!--footer-->
<jsp:include page="/jsp/common/user/footer.jsp"></jsp:include>
<!--footer  End-->
<div id="fade" class="black_overlay"></div>
<div id="moveGroupLaye"
     style="display: none; z-index: 5895456; width: 200px; height: 200px; position: absolute">
    <img src="/images/loading.gif">
</div>
<form id="xinfrom" method="post" action="/mallhtml/updateHtml.do" target="_blank">
    <input type="hidden" name="id" id="id">
</form>
</body>
<script type="text/javascript">
    //弹出遮罩层
    function showFade() {
        document.getElementById('fade').style.display = 'block';
        var bgdiv = document.getElementById('fade');
        bgdiv.style.width = document.body.scrollWidth;
        $('#fade').height($(document).height());
    }
    //弹出弹出层
    function showAll() {
        showFade();
        center($("#moveGroupLaye"));
    }
    //去掉遮蔽层和加载页面
    function closeWindow() {
        document.getElementById('moveGroupLaye').style.display = 'none';
        document.getElementById('fade').style.display = 'none';
    }

    function center(obj) {
        var screenWidth = $(window).width(), screenHeight = window.screen.height; //当前浏览器窗口的 宽和电脑屏幕的高度高
        var scrolltop = $(document).scrollTop();//获取当前窗口距离页面顶部高度
        var objLeft = (screenWidth - obj.width()) / 2;
        var objTop = (screenHeight - obj.height()) / 2 + scrolltop;
        obj.css({
            left: objLeft + 'px',
            top: objTop + 'px',
            'display': 'block'
        });
    }
    //弹出素材库
    function materiallayer() {
        layer.open({
            type: 2,
            title: '素材库',
            shadeClose: true,
            shade: 0.2,
            area: ['820px', '500px'],
            offset: "10px",
            content: '/common/material.do',
        });
    }
    //素材库里面返回信息
    function image(id, url) {
        layer.closeAll();
        $("#main")[0].contentWindow.fhmateriallayer(id, url);	//父类调用子类的方法
    }
    //子页面调用返回按钮。关闭弹出框
    function go_back() {
        layer.closeAll();
    }

    //回调方法
    function hdhtml(id) {
        parent.layer.closeAll();
        $("#main").attr("src", "/mallhtml/htmllist.do?pageNum=1");
        $("#id").val(id);
        $("#xinfrom").submit();//post 请求弹出新页面;

    }
</script>
</html>