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
<title>分享页面</title>
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
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<div class="sWrapper">
    <div class="fs-body">
        <div class="fs-agent">
        	<c:set var="headImages" value="/images/mall/img/pt-detail2.jpg"></c:set>
        	<c:if test="${!empty sellerMember }">
        		<c:if test="${!empty sellerMember.headimgurl }">
        			<c:set var="headImages" value="${sellerMember.headimgurl }"></c:set>
        		</c:if>
        	</c:if>
            <div class="fs-agent-bg" style="background-image: url('${headImages}');background-size: cover;"></div>
            <div class="fs-agent-text">
                <i class="to-left-wrap"><em class="to-left-inner"></em></i>
                <p class="mgb-1">我是<span class="yColor">${mallSeller.userName}</span></p>
                <p>我要为<span class="yColor" onclick="homeUrl();">${mallSet.mallName}</span>代言</p>
            </div>
        </div>
        <div class="fs-item" onclick="productUrl();">
        	<c:set var="imgUrl" value="${productMap.image_url }" />
        	<c:if test="${!empty  productMap.specifica_img_url}">
        		<c:set var="imgUrl" value="${productMap.specifica_img_url }"/>
        	</c:if>
            <div class="fs-item-bg" style="background-image: url('${http}${imgUrl}');"></div>
            <div class="fs-item-desc">
                <p class="multiOf mgb-1">${productMap.pro_name }</p>
                <p class="rColor">&yen;${productMap.price }</p>
            </div>
        </div>
        <div class="fs-code">
            <div class="fs-code-title"></div>
            <div class="fs-code-main">
                <!--以下两个img都是生成的二维码图片，第二张用于覆盖在小王子上-->
                <img class="fs-code-icon" src="${mallSeller.qrCodePath }">
                <img class="fs-code-icon-none" src="${mallSeller.qrCodePath }">
                <!--右侧小丸子-->
                <img class="fs-code-xwz" src="/images/mall/seller/xwz.png">
            </div>
            <p class="fs-code-request">关注我们&nbsp;加入有礼品&nbsp;购买有惊喜</p>
        </div>
        <div class="fs-remark">
            <p class="fs-remark-title">如何赚钱</p>
            <p class="fs-remark-text">
                <span class="fs-remark-step">第一步</span>
                <span class="fs-remark-span">转发商品链接或商品图片给微信好友；</span>
            </p>
            <p class="fs-remark-text">
                <span class="fs-remark-step">第二步</span>
                <span class="fs-remark-span">从您转发的链接或图片进入商城的好友，系统将自动锁定为您的客户，他们在微信商城中购买任何商品，您都可以获得销售佣金；</span>
            </p>
            <p class="fs-remark-text">
                <span class="fs-remark-step">第三步</span>
                <span class="fs-remark-span">您可以在销售中心查看【我的客户】和【我的订单，好友确认收货后佣金方可提现。</span>
            </p>
        </div>
    </div>
</div>
<%-- <img src="${http}${mallSet.mallHeadPath}" class="img"/>
<input type="hidden" id="shopname" value="${mallSet.mallName}"><!-- 店铺id  --> --%>

<input type="hidden" class="userName" value="${seller.userName }"/>
<input type="hidden" class="title" value="${mallSet.mallName }"/>
<input type="hidden" class="desc" value="${mallSet.mallIntroducation }"/>
<img class="mallHeadPath" src="<c:if test="${!empty mallSet }">${http }${mallSet.mallHeadPath }</c:if>" style="display: none;"/>
<img class="headimgurl" src="${member.headimgurl }" style="display: none;"/>


<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerMallSet.js"></script>
<script type="text/javascript" src="/js/plugin/gt/js/gt_common.js"></script>
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
var url = window.location.protocol+"//"+window.location.host+"/phoneSellers/${saleMemberId}/${productMap.id}/79B4DE7C/shareSeller.do?share=view";
wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.appid}", // 必填，公众号的唯一标识
    timestamp: "${record.timestamp}", // 必填，生成签名的时间戳
    nonceStr: "${record.nonce_str}", // 必填，生成签名的随机串
    signature: "${record.signature}",// 必填，签名，见附录1
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
	    desc: title, // 分享描述
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

function productUrl(){
	location.href = "/mallPage/${productMap.id}/${productMap.shop_id}/79B4DE7C/phoneProduct.do?saleMemberId=${saleMemberId}&uId=${userid}";
}
function homeUrl(){
	location.href = "/phoneSellers/${saleMemberId}/79B4DE7C/mallIndex.do?uId=${userid}";
}

$(function () {
    controlOF();
});
//控制行内元素溢出
function controlOF() {
    $(".m-item-tip").each(function () {
        $(this).next().css("text-indent",($(this).outerWidth() + 5) + "px");
    });
}


function searchFind(){
	var search  = $("input.searchWord").val();
	if(search != null && search  != "" && (typeof search ) != "undefined"){
		location.href = "/phoneSellers/${saleMemberId }/79B4DE7C/shoppingall.do?proName="+search;
	}else{
		location.href = "/phoneSellers/${saleMemberId }/79B4DE7C/shoppingall.do";
	}
}


</script>
</body>
</html>