<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE>
<html>
<base href="<%=basePath%>" />
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
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
<title>您访问的页面（商品）已删除（已下架）</title>
<link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>" />
</head>
<style type="text/css">
html, body {
	margin: 0 auto;
	position: relative;
	background-color: #FFFFFF;
}

img{
   display: block;
   margin: 0 auto!important;
   max-width:100%!important;
}

</style>
<body>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/reset.css?<%=System.currentTimeMillis()%>" />
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
	  
<img src="/images/mall/shopdelect.jpg" onclick="history.go(-1)" style="display: block; width:100%; height:100%">
	   
</body>
<script>


	$(window).load(function() {
						var a = $(window).width(), b = $(window).height(), d = 870, meta = $("#meta");
						setTimeout(function() {
							meta.attr("content","width=870,initial-scale="+a/d+", minimum-scale="+a/d+", maximum-scale="+a/d+", user-scalable=no");
							$(".loading").hide();
						}, 300);
					});

	
</script>
</html>