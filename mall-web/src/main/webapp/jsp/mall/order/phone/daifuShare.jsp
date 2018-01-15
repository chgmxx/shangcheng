<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>代付</title>
	<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<base href="<%=basePath%>" /> 
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
  <meta name="apple-mobile-web-app-capable" content="yes" />
  <meta name="apple-mobile-web-app-status-bar-style" content="black" />
  <meta name="format-detection" content="telphone=no, email=no" />
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
  <!--下面三个是清除缓存，微信浏览器缓存严重又无法刷新 这个方法 调用的时候方便-->
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>

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
   
   
<link rel="stylesheet" href="/css/mall/public.css"/>
<link rel="stylesheet" href="/css/mall/insteadPay.css"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
   
<div class="warp">
	<header>
		<img src="/images/mall/pay.jpg"/>
	</header>
	<div class="main">
		<div>通过微信将代付请求发送好友，即可让他帮你买单！</div>
		<div class="money">￥<c:if test="${!empty order}">${order.orderMoney }</c:if></div>
		<c:if test="${order.orderStatus == 1 }">
		<div><a href="javascript:void(0);" class="payBtn sharePay">发起代付请求</a></div>
		</c:if>
		<c:if test="${!empty pageId}">
		<div style="margin-top:10px;"><a href="/mallPage/${pageId }/79B4DE7C/pageIndex.do" class="payBtn jxBtn">继续购买</a></div>
		</c:if>
		<div class="info">
			<p>说明：</p>
			<p>1.对方需要开通微信支付才能帮你付款，如果未开通，请重新选择好友支付。</p>
			<p>2.如果你要将来退款了，钱将退款到好友的<c:if test="${isWxPay == 1 }">微信</c:if><c:if test="${isWxPay == 0 && isAliPay == 1 }">支付宝</c:if>账户里。</p>
		</div>
	</div>
</div>
<input type="hidden" class="isWx" value="${isWx }"/>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript">
var title = "帮我付款才是真友谊";
var desc = "你的一笔小开支，是我们关系的一大步，为我付款吧！";
var imgUrls = "${imgUrl}${proImgUrl}";
var url = "${path}/phoneOrder/${order.id}/79B4DE7C/getDaiFu.do";
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.appid}", // 必填，公众号的唯一标识
    timestamp: "${record.timestamp}", // 必填，生成签名的时间戳
    nonceStr: "${record.nonce_str}", // 必填，生成签名的随机串
    signature: "${record.signature}",// 必填，签名，见附录1
    jsApiList: ["onMenuShareTimeline","onMenuShareAppMessage","showOptionMenu"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
wx.ready(function(){
	wx.showOptionMenu();
	//分享到朋友圈
	wx.onMenuShareTimeline({
	    title: title, // 分享标题
	    link: url, // 分享链接
	    imgUrl: imgUrls, // 分享图标
	    success: function () { 
	        // 用户确认分享后执行的回调函数
	    },
	    cancel: function () { 
	        // 用户取消分享后执行的回调函数
	    }
	});
	//分享给朋友
	wx.onMenuShareAppMessage({
	    title: title, // 分享标题
	    desc: desc, // 分享描述
	    link: url, // 分享链接
	    imgUrl: imgUrls, // 分享图标
	    type: '', // 分享类型,music、video或link，不填默认为link
	    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
	    success: function () { 
	        // 用户确认分享后执行的回调函数
	    },
	    cancel: function () { 
	        // 用户取消分享后执行的回调函数
	    }
	}); 
});
$(window).load(function() {
	setTimeout(function() {
		$(".loading").hide();
	}, 1000);
});

$(".sharePay").click(function(){
	var isWx = $("input.isWx").val();
	if(isWx == "1"){
		alert("请点击右上角，把代付信息分享给朋友或朋友圈");
	}else{
		alert("请把链接分享给好友");
	}
});

</script>
</body>
</html>
