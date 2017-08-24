<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<title>商品管理-商品编辑</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>" />
<link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
<link rel="stylesheet" type="text/css" href="/css/mall/init.css"/>
<link rel="stylesheet" type="text/css" href="/css/mall/editNextTwo.css"/>

</head>
<body>
	<!--加载动画-->
		<section class="loading">
	        <div class="load3">
	            <div class="double-bounce1"></div>
	            <div class="double-bounce2"></div>
	        </div>
	    </section>
	    
	    <div class="main">
	    	<h2 class="detail-title">商品详情</h2>
	    	<article class="detail-info" id="detail-info"> 
	    		<h3>售后服务说明：已激活的iPhone不支持七天无理由退换货，请确认需求后在激活使用。</h3>
	    		<p>声明：下单一小时内未支付订单，京东有权取消订单；如系统判定为经销商订单，系统将自动删除订单；由于货源紧张，所有订单将根据各地实际到货时间陆续自动发货，京东有权晚发货或者单方面取消订单。</p>
	    	</article> 
	    </div>
	    
	    <footer class="footer">
			<ul>
				<li class="foot-item">
					<img src="/images/icon/mall-person.png"/>
					<p>我的</p>
				</li>
				<li class="foot-item">
					<img src="/images/icon/mall-shop.png"/>
					<p>购物车</p>
				</li>
				<li class="foot-item2">
					<a href="javascrpt:void(0);" class="add-shop">加入购物车</a>
				</li>
				<li class="foot-item2">
					<a href="javascrpt:void(0);" class="now-shop">立即购买</a>
				</li>
			</ul>
		</footer>
		
		<script src="/js/plugin/jquery-1.8.3.min.js"></script>
	    <script> 
	    var parentWin = window.parent.document;
		    $(window).load(function(){
					var a=$(window).width(),
					b=$(window).height(),
					d=870,
					meta=$("#meta");
					setTimeout(function(){
						meta.attr("content","width=870,initial-scale="+a/d+", minimum-scale="+a/d+", maximum-scale="+a/d+", user-scalable=no");
						$(".loading").hide();
					},300);
				});
		    var detail = window.parent.editor.html();// 店铺名称
		    $(".detail-info").html(detail)
		    
		    
		</script>

</body>
</html>