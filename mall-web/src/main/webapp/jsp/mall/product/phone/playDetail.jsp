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
<title>玩法详情</title>
<link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>" />
</head>

<body>
<!--加载动画-->
<section class="loading">
     <div class="load3">
         <div class="double-bounce1"></div>
         <div class="double-bounce2"></div>
     </div>
 </section>
   <link rel="stylesheet" type="text/css" href="/css/mall/groupbuy/public.css"/>
   <link rel="stylesheet" type="text/css" href="/css/mall/groupbuy/rule-detail.css"/>
   
<div class="Warp">
	<header>如何参加拼团？</header>
	<div class="process">
		<div class="process1 flex">
			<div class="pro-txt">
				团长发起拼团
			</div>
			<div class="line-arrow flex-1 box">
				<span></span>
				<div class="rig-arrow"></div>
			</div>
			<div class="pro-txt flex-1" >
				购买支付
			</div>
			<div class="line-arrow flex-1 box">
				<span></span>
				<div class="rig-arrow"></div>
			</div>
			<div class="pro-txt flex-1">
				好友参团
			</div>
		</div>
		<div class="process2">
			<span></span>
			<div class="down-arrow"></div>
		</div>
		<div class="process3 flex">
			<div class="pro-txt flex-1">
				 卖家发货
			</div>
			<div class="line-arrow flex-1 box">
				<span></span>
				<div class="left-arrow"></div>
			</div>
			<div class="pro-txt flex-1" >
				拼团成功
			</div>
			<div class="line-arrow flex-1 box">
				<span></span>
				<div class="left-arrow"></div>
			</div>
			<div class="pro-txt pro-txt-last">
				达到拼团人数
			</div>
		</div>
	</div>
	<article class="rule">
		<p>1、组团失败：未达到拼团人数，组团失败。系统会在1-2个工作日内提交处理，审核后自动原路退款到您的支付账户。</p>
		<p>2、周末配送：由于部分快递公司周末不上班不派送公司件，<span class="red-txt">周四到周六成团的</span>公司地址订单周日发货。<span class="red-txt">多粉建议您添加家庭地址</span>，方便及时收货哦</p>
	</article>
</div>
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript">

$(window).load(function() {
	setTimeout(function() {
		$(".loading").hide();
	}, 1000);
});
</script>
</body>
</html>