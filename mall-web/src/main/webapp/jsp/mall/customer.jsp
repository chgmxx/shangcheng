<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<title>客服</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" id="meta"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="format-detection" content="telephone=no"/>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="HandheldFriendly" content="true">
<meta name="MobileOptimized" content="320">
<meta name="screen-orientation" content="portrait">
<meta name="x5-orientation" content="portrait">
<meta name="full-screen" content="yes">
<meta name="x5-fullscreen" content="true">
<meta name="browsermode" content="application">
<meta name="x5-page-mode" content="app">
<meta name="msapplication-tap-highlight" content="no">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" /> 
<style type="text/css">

.fixRig{
	position: fixed;
    right: 30px;
    bottom: 20%;
    z-index: 99998;
}
.fixRig a{
	width: 75px;
	height: 75px;
	display: block;
	border-radius: 50%;
	-webkit-border-radius: 50%;
	background-color: rgba(0,0,0,0.5);
}

.fixRig a.top{
	background-image: url(/images/mall/new/top.png);
	background-position: center center;
	background-repeat: no-repeat; 
	background-size: 28px 38px;
	margin-bottom: 25px;
}

.fixRig a.kefu{
	background-image: url(/images/mall/new/kf.png);
	background-position: center center;
	background-repeat: no-repeat; 
	background-size: 38px 35px;
}
</style>
<script type="text/javascript">
</script>
</head>
<body>

<div class="fixRig">
	<a href="javascript:;" class="top" id="top"></a>
	<c:if test="${!empty qq }">
	<a href="http://wpa.qq.com/msgrd?v=3&amp;uin=${qq }&amp;site=qq&amp;menu=yes" class="kefu" ></a>
	</c:if>
</div>


<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
/*向上按钮 刚开始隐藏  超过一定高度显示*/
$(window).scroll(function(){
	var scrollTop =  document.body.scrollTop || document.documentElement.scrollTop;
   if (scrollTop > 200) {
    	$('.top').show();
    }
    else {
    	$('.top').hide();
    }
});
/*返回顶部*/
$('.top').hide();
$('.top').click(function(){
	$(document).scrollTop(0);
    document.documentElement.scrollTop  = 0;
});
</script>

</body>
</html>