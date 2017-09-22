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

<div class="warp" style="overflow-x: hidden;">
	<h2 class="title">我在商城挑选好了商品，是时候该你仗义疏财了，快帮我付个款吧~</h2>
	 <div class="payInfo">
	 	<div class="payMoney" <c:if test="${!empty times && order.orderStatus > 1 && order.orderStatus < 5}">style="background: url(/images/mall/pay3.jpg)"</c:if>>
	 		<div>代付金额</div>
	 		<div>￥<span class="price">${order.orderMoney }</span></div>
	 		<c:if test="${!empty order.receiveName }">
	 		<div>收货人：${order.receiveName }</div>
	 		</c:if>
	 	</div>
	 	<div class="timeDiv">
	 		<c:if test="${!empty times && order.orderStatus == 1}">
		 	<input type="hidden" class="times" value="${times }"/>
	 		<div class="payTime count-down" id="time-item">
				剩余支付时间：<span id="hour_show" class="time-block">0</span>:<span id="minute_show" class="time-block">0</span>:<span id="second_show" class="time-block">0</span>
	       	</div>
	       	<c:if test="${isWxPay == 1 }">
	       	<div class="payBox"><a href="javascript:;" class="payBtn wxPay">微信安全支付</a></div>
	       	</c:if>
	       	<c:if test="${isAliPay == 1 }">
	       	<div class="payBox"><a href="javascript:;" class="payBtn aliPay">支付宝安全支付</a></div>
	       	</c:if>
	       	</c:if>
	 	</div>
		<c:if test="${order.orderStatus == 1 }">
		<div class="payBox" style="margin-top:10px;"><a href="javascript:void(0);" class="payBtn sharePay">发起代付请求</a></div>
		</c:if>
	 	<c:if test="${!empty pageId}">
		<div class="payBox" style="margin-top:10px;"><a href="/mallPage/${pageId }/79B4DE7C/pageIndex.do" class="payBtn jxBtn">我也要购买</a></div>
		</c:if>
       	<div class="info pad">
       		<p>说明：</p>
       		<p>1.付款前务必和好友再次确认，避免是诈骗行为。</p>
       		<p>2.如果发生退款，钱将退还到你的<c:if test="${isWxPay == 1 }">微信</c:if><c:if test="${isWxPay == 0 && isAliPay == 1 }">支付宝</c:if>账户里。</p>
       	</div>
	 </div>
	 <c:set var="img" value=""></c:set>
	 <div class="payDetail">
	 	<div class="detailTit">代付订单信息</div>
	 	<ul>
	 		<c:if test="${!empty detailList }">
	 			<c:forEach var="detail" items="${detailList }">
	 				<c:set var="img" value="${detail.product_image_url }"></c:set>
	 		<li class="item">
	 			<div class="itemPic">
	 				<a href="mallPage/${detail.product_id }/${detail.shop_id }/79B4DE7C/phoneProduct.do"><img src="${imgUrl }${detail.product_image_url }"/></a>
	 			</div>
	 			<div class="item-detail">
	 				<div><a href="mallPage/${detail.product_id }/${detail.shop_id }/79B4DE7C/phoneProduct.do">${detail.det_pro_name }</a></div>
	 				<div>
	 					<label for="">￥${detail.det_pro_price }</label>
	 					<label for="">x${detail.det_pro_num }</label>
	 				</div>
	 			</div>
	 		</li>
	 			</c:forEach>
	 		</c:if>
	 	</ul>
	 </div>
</div>
<form method="post" id="daifuForm">
	<input type="hidden" class="orderId" name="orderId" value="${order.id}"/>
	<input type="hidden" class="dfPayMoney" name="dfPayMoney" value="${order.orderMoney }"/>
</form>
<input type="hidden" class="isWx" value="${isWxPay }"/>
<input type="hidden" class="alipaySubject" value="${alipaySubject }"/>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript">
var title = "帮我付款才是真友谊";
var desc = "你的一笔小开支，是我们关系的一大步，为我付款吧！";
var imgUrls = "${imgUrl}${img}";
var url = "${path}/phoneOrder/${order.id}/79B4DE7C/getDaiFu.do";
wx.config({
	debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.get('appid')}", // 必填，公众号的唯一标识
    timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
    nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
    signature: "${record.get('signature')}",// 必填，签名，见附录1
    jsApiList: ["onMenuShareTimeline","onMenuShareAppMessage","showAllNonBaseMenuItem"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
wx.ready(function(){
	wx.showAllNonBaseMenuItem();
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
function timer(intDiff){
	var interIndex = window.setInterval(function(){
		var intDiff = $(".times").val();
		if(intDiff != null && intDiff != ""){
			intDiff = intDiff*1;
			if(intDiff > 0){
				var day=0,
					hour=0,
					minute=0,
					second=0;		
				if(intDiff > 0){
					day = Math.floor(intDiff / (60 * 60 * 24));
					hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
					minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
					second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
				}
				if (minute <= 9) minute = '0' + minute;
				if (second <= 9) second = '0' + second;
				$('#day_show').html(day);
				$('#hour_show').html(hour);
				$('#minute_show').html(minute);
				$('#second_show').html(second);
				intDiff--;
				$(".times").val(intDiff);
			}else{
				$(".timeDiv").hide();
				clearInterval(interIndex);
			}
		}else{
			clearInterval(interIndex);
		}
	}, 1000);
} 

if($(".times").length > 0){
	timer($(".times").val());
}

/**
 * 微信在线支付,支付宝
 */
$(".wxPay,.aliPay").click(function(){
	var orderId = $(".orderId").val();
	var payWay = 1;
	if($(this).hasClass("aliPay")){
		payWay = 2;
	}
	if(orderId == null || orderId == ""){
		//该代付不存在
		return;
	}
	var index = layer.open({
	    title: "",
	    content: "",
	    type:2,
	    shadeClose:false
	});
	var data = {
		orderId : orderId,
		dfPayMoney : $(".dfPayMoney").val(),
		dfPayWay : payWay
	};
	$.ajax({
  		  url:"/phoneOrder/79B4DE7C/freindDaifu.do",
  		  type:"POST",
  		  data: {daifu:JSON.stringify(data)},
  		  timeout:300000,
  		  dataType:"json",
  		  success:function(data){
  			
  			if(data != null){
  				if(data.result == true){
                    if (data.url !== null && data.url !== "") {
                        location.href = data.url;
                    }
  				}else{
  					layer.closeAll();
  					if(data.msg != null && data.msg != ""){
  	  					alert(data.msg)
  					}else{
  						if(payWay == 2){
  							alert("支付宝支付失败，请稍后重新支付");
  						}else{
  							alert("微信代付失败，请稍后重新支付");
  						}
  						
  					}
  				}
  			}else{
  				layer.closeAll();
  			}
  		  },error:function(){
  			if(payWay == 2){
					alert("支付宝支付失败，请稍后重新支付");
				}else{
					alert("微信代付失败，请稍后重新支付");
				}
			  layer.closeAll();
		  }
	});
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
