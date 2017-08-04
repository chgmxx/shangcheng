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
<title>推广二维码</title>
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
<link rel="stylesheet" type="text/css" href="/css/mall/seller/main.css" />
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
 <div class="sWrapper">
      <header class="arrow-header" onclick="javascript:history.back(-1);">
          推广二维码
      </header>
      <div class="qr-body">
          <div class="qr-main">
              <p>我的二维码</p>
              <img src="${seller.qrCodePath }" class="qr-code">
          </div>
          <p class="qr-bottom">您可将此二维码图片发送给您的好友</p><!-- 您可将此二维码链接或二维码图片发送给您的好友 -->
      </div>
</div>
<input type="hidden" class="userName" value="${seller.userName }"/>
<input type="hidden" class="title" value="${mallSet.mallName }"/>
<input type="hidden" class="desc" value="${mallSet.mallIntroducation }"/>
<img class="mallHeadPath" src="<c:if test="${!empty mallSet }">${http }${mallSet.mallHeadPath }</c:if>" style="display: none;"/>
<img class="headimgurl" src="${member.headimgurl }" style="display: none;"/>

<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript">
var userName = $("input.userName").val();
var title = $("input.title").val();
var desc = $("input.desc").val();
if(title == ""){
	title = userName;
}
if(desc == "" && title != ""){
	desc = title+"的商城";
}
if(title == ""){
	title = title+"的商城";
}
var imgUrls = $("img.mallHeadPath").attr("src");
if(imgUrls == null || imgUrls == ""){
	imgUrls = $("img.headimgurl").attr("src");
}
var url = "${url}/phoneSellers/${member.id}/79B4DE7C/mallIndex.do";
wx.config({
	debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.get('appid')}", // 必填，公众号的唯一标识
    timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
    nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
    signature: "${record.get('signature')}",// 必填，签名，见附录1
    jsApiList: ["onMenuShareTimeline","onMenuShareAppMessage","showAllNonBaseMenuItem","hideMenuItems"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
wx.ready(function(){
	//显示所有功能按钮接口
	wx.showAllNonBaseMenuItem();
	wx.hideMenuItems({
	    menuList: ["menuItem:copyUrl"] // 要隐藏的菜单项，只能隐藏“传播类”和“保护类”按钮，所有menu项见附录3
	});
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

function showNav(obj) {
    $(obj).addClass("nav-active").siblings().removeClass("nav-active");
}

</script>
</body>
</html>