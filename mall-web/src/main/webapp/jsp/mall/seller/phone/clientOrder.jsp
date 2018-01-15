<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<title>客户订单</title>
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
<link rel="stylesheet" type="text/css" href="/css/mall/seller/main.css?<%=System.currentTimeMillis()%>" />
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<div class="sWrapper">
  <header class="arrow-header" onclick="javascript:location.href='/phoneSellers/79B4DE7C/sellerIndex.do?uId=${userid}'">
      客户订单(${sellerOrderList.size() })
  </header>
  <nav class="r-nav">
      <ul class="r-nav-ul">
          <li onclick="showNav(0)" class="nav-order <c:if test="${empty status || status == 0 }">nav-active</c:if>">所有订单</li>
          <li onclick="showNav(1)" class="nav-order <c:if test="${status == 1 }">nav-active</c:if>">待付款</li>
          <li onclick="showNav(2)" class="nav-order <c:if test="${status == 2 }">nav-active</c:if>">已付款</li>
          <li onclick="showNav(4)" class="nav-order <c:if test="${status == 3 }">nav-active</c:if>">已完成</li>
        </ul>
    </nav>
    <section class="o-body">
    	<c:if test="${!empty sellerOrderList }">
        <ul class="o-order-list">
        	<c:forEach var="sellerOrder" items="${sellerOrderList }">
            <li>
                <div class="o-order-desc">
                    <p class="o-item-name">${sellerOrder.proName }</p>
                    <p class="mgb-1">买家：${sellerOrder.nickname }</p>
                    <p class="mgb-1">下单时间：<c:if test="${!empty sellerOrder.create_time}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${sellerOrder.create_time }" /></c:if></p>
                    <p>支付状态：
                    	<c:if test="${sellerOrder.order_status == 1 }">待付款</c:if>
                    	<c:if test="${sellerOrder.order_status == 2 }">待发货</c:if>
                    	<c:if test="${sellerOrder.order_status == 3 }">已发货</c:if>
                    	<c:if test="${sellerOrder.order_status == 4 }">已完成</c:if>
                    	<c:if test="${sellerOrder.order_status == 5 }">订单已关闭</c:if>
                    </p>
                </div>
                <p class="o-money">金额：&yen; ${sellerOrder.order_money }</p>
            </li>
            </c:forEach>
        </ul>
        </c:if>
    </section>
</div>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript">

wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.appid}", // 必填，公众号的唯一标识
    timestamp: "${record.timestamp}", // 必填，生成签名的时间戳
    nonceStr: "${record.nonce_str}", // 必填，生成签名的随机串
    signature: "${record.signature}",// 必填，签名，见附录1
    jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

wx.ready(function(){
 wx.hideOptionMenu();
});

/* function showNav(obj) {
    $(obj).addClass("nav-active").siblings().removeClass("nav-active");
} */

function showNav(status){
	if(status > 0){
		location.href = "/phoneSellers/79B4DE7C/clientOrder.do?status="+status+"&uId=${userid}";
	}else{
		location.href = "/phoneSellers/79B4DE7C/clientOrder.do?uId=${userid}";
	}
	
}


</script>
</body>
</html>