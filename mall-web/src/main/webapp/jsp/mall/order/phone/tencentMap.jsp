<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!doctype html>
<html class="no-js">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>地址</title>
<meta name="robots" content="index,follow" />
<meta name="apple-mobile-web-app-capable" content="yes">
<!-- 添加到主屏后的标题（iOS 6 新增） -->
<meta name="apple-mobile-web-app-capable" content="yes" />
<!-- 是否启用 WebApp 全屏模式，删除苹果默认的工具栏和菜单栏 -->
<meta name="apple-mobile-web-app-status-bar-style" content="black" />
<!-- 设置苹果工具栏颜色 -->
<meta name="format-detection" content="telphone=no, email=no" />
<!-- 忽略页面中的数字识别为电话，忽略email识别 -->
<!-- 启用360浏览器的极速模式(webkit) -->
<meta name="renderer" content="webkit">
<!-- 避免IE使用兼容模式 -->
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- 针对手持设备优化，主要是针对一些老的不识别viewport的浏览器，比如黑莓 -->
<meta name="HandheldFriendly" content="true">
<!-- 微软的老式浏览器 -->
<meta name="MobileOptimized" content="320">
<!-- uc强制竖屏 -->
<meta name="screen-orientation" content="portrait">
<!-- QQ强制竖屏 -->
<meta name="x5-orientation" content="portrait">
<!-- UC强制全屏 -->
<meta name="full-screen" content="yes">
<!-- QQ强制全屏 -->
<meta name="x5-fullscreen" content="true">
<!-- UC应用模式 -->
<meta name="browsermode" content="application">
<!-- QQ应用模式 -->
<meta name="x5-page-mode" content="app">
<!-- windows phone 点击无高光 -->
<meta name="msapplication-tap-highlight" content="no">
<!-- iOS 图标 begin -->
<link rel="alternate" type="application/rss+xml" title="RSS"
	href="/rss.xml" />
<meta name="format-detection" content="telephone=no">
<meta id="meta" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0, user-scalable=no" name="viewport">
<meta content="black" name="apple-mobile-web-app-status-bar-style">
<meta content="telephone=no" name="format-detection">
<link rel="stylesheet" type="text/css" href="/css/common/init.css"/>
<style type="text/css">
	html,body{
		width:100%;
		height:100%;
		position: relative;
		overflow-x: hidden
	}
	
	.btn{
		background-color: #ff7b00;
		width: 100px;
		border-radius : 10px;
		border: 0;
		height: 30px;
		color :white;
		font-size : 14px;
		line-height: 30px;
		-webkit-appearance:none;
	    appearance:none;
	}
	.fade{
	    display: none; 
		position: fixed;
		top: 0%;
		left: 0%;
		width: 100%;
		background-color: black;
		z-index:1001;
		-moz-opacity: 0.4;
		opacity:.40;
		filter: alpha(opacity=40);
		min-height: 100%
	}
	.lay{
	   position:absolute; 
	   width: 95%;  
	   background-color: #fff;
	   border-radius:5px;
	   z-index:1002;
	   padding:15px 0px;
	   display: none;
	}
</style>
	<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
	
	<script type="text/javascript">
	var address = '${address}';
	var userid = '${uId}';
	address = JSON.parse(address);
	var addressManage = "${addressManage}"
	var addType = "${addType}"
	var type = "${type}";
	//接收地图返回的信息
	var loc;
	/**腾讯地图返回信息**/
	window.addEventListener('message', function(event) {
	    // 接收位置信息，用户选择确认位置点后选点组件会触发该事件，回传用户的位置信息
	    loc = event.data;
	    address.memAddress = loc.poiname;
	    address.memLatitude = loc.latlng.lat;
	    address.memLongitude = loc.latlng.lng;
	}, false);	
	
	/**确认**/
	function fnOk(){
		var url = "/phoneOrder/79B4DE7C/toAddress.do?uId="+userid+"&params="+JSON.stringify(address);
		if(addressManage != null && addressManage != "" && typeof(addressManage) != "undefined"){
			url += "&addressManage="+addressManage;
		}
		if(addType != null && addType != "" && typeof(addType) != "undefined"){
			url += "&addType="+addType;
		}
		location.href=url;
	}
		
	
	/**返回**/
	function fnCancel(){
		var url = "/phoneOrder/79B4DE7C/toAddress.do?uId="+userid+"&params="+JSON.stringify(address);
		if(addressManage != null && addressManage != "" && typeof(addressManage) != "undefined"){
			url += "&addressManage="+addressManage;
		}
		if(addType != null && addType != "" && typeof(addType) != "undefined"){
			url += "&addType="+addType;
		}
		location.href=url;
	}
	
	$(function(){
		var url ="http://apis.map.qq.com/tools/locpicker?search=1&type=1&key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU&referer=test&coordtype=5&coord=";
		//url += "25.22,114.66";
		url += address.memLatitude+","+address.memLongitude;
		$("#ifr").attr("src",url);
	})
	
	//让指定的div始终显示在屏幕中间
	function divCenter(divname){
		var top=($(window).height()-$(divname).height())/2;
		var left=($(window).width()-$(divname).width())/2;
		var scrollTop=$(document).scrollTop();
		var scrollLeft=$(document).scrollLeft();
		$(divname).css(
			{
			position:'absolute',
			top:20+"%",
			left:scrollLeft
		}).show();
	}
	
	function fadeShow(){
		$(".fade").show();
	}
	 
	function showAll(divname){
		fadeShow();
		divCenter(divname);
	}
	
	function hideAll(){
		$(".fade").hide();
		$(".lay").hide();
	}
	
	function hander(){
		if($("#hander").val() == ""){
			alert("请输入地址！");
		}else{
			address.memAddress = $("#hander").val();
			address.memLatitude = '';
			address.memLongitude = '';
			fnOk();
		}
		
	}
	</script>
  </head>
  
  <body style="width: 100%;height:100%; ">
    <iframe id="ifr" src="" 
    	frameborder="0" style="height: 90%;width: 99%;">
    	
    </iframe>
    <div style="width: 100%;height: 10%;text-align: center;">
    	<input type="button" value="确认" class="btn" onclick="fnOk()"/>
    	<input type="button" value="返回" class="btn" onclick="fnCancel()"/>
    	<input type="button" value="搜索不到?" class="btn" onclick="showAll('.lay')"/> 
    </div>
    <div class="fade" onclick="hideAll()"></div>
    <div class="lay" style=" ">
       <input type="text" id="hander" placeholder="若搜索不到地址，请在此填写" style="height: 30px;  margin: 0 auto; display: block; width: 95%; border-radius:5px; border:1px solid #ccc" />
       <input type="button" value="确定" class="btn" onclick="hander()" style="display: block; margin:10px auto;"/>
    </div>
  </body>
</html>
