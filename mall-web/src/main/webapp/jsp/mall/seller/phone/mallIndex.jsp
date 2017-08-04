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
<title>商城首页</title>
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
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<div class="sWrapper-mgb">
    <div class="m-body">
        <div class="m-header">
            <div class="m-header-bg"  style="background-image: url('${http}${mallSet.bannerPath}');background-size: cover;">
            	<c:if test="${saleMemberId == memberId }">
                <a href="/phoneSellers/79B4DE7C/toMallSet.do?uId=${userid}" class="m-set-icon"></a>
                </c:if>
                <p class="m-header-belong">${mallSet.mallName }</p>
            </div>
            <div class="m-header-desc">
                <div class="m-header-me" style="background-image: url('${http}${mallSet.mallHeadPath}');background-size: cover;"></div>
                <ul class="m-header-about">
                    <li>
                        <a  href="/phoneSellers/${saleMemberId }/79B4DE7C/shoppingall.do?uId=${userid}">
                            <p class="m-text">${productList.size() }</p>
                            <p>全部商品</p>
                        </a>
                    </li>
                    <li class="m-divide"></li>
                    <li>
                        <a href="tel:${mallSet.contactNumber }" class="m-tel-icon">电话</a>
                    </li>
                    <li class="m-divide"></li>
                    <li>
                        <a href="http://wpa.qq.com/msgrd?v=3&amp;uin=${mallSet.qq }&amp;site=qq&amp;menu=yes" class="m-qq-icon">QQ</a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="m-search-box">
            <input type="text" placeholder="请输入关键字进行搜索" class="m-search-input srh-common searchWord">
            <i class="m-search-icon srh-i-common" onclick="searchFind();"></i>
        </div>
        <ul class="m-item-ul">
        	<c:if test="${!empty productList }">
        		<c:forEach var="product" items="${productList }">
            <li>
            	<c:set var="locationUrl" value="/mallPage/${product.id}/${product.shop_id}/79B4DE7C/phoneProduct.do?saleMemberId=${saleMemberId }&uId=${userid }"></c:set>
		   		<c:if test="${product.pro_type_id == 2 && product.member_type > 0}">
		   			<c:set var="locationUrl" value="/phoneMemberController/${member.publicId}/79B4DE7C/findMember.do?uId=${userid}"></c:set>
		   		</c:if>
                <a href="${locationUrl }" class="m-item-a">
                    <div class="m-item-bg"  style="background-image: url('${http}${product.image_url}');background-size: contain;"></div>
                    <div class="m-item-label">
                        <div>
                        	<c:if test="${!empty product.pro_label && product.pro_label != ''}">
                            <span class="m-item-tip">${product.pro_label }</span>
                            </c:if>
                            <p class="m-item-name">${product.pro_name }</p>
                        </div>
                        <div class="m-item-float">
                        	<p class="m-item-float-l">
                        		<span>&yen;${product.price}</span>
                        		<c:if test="${product.pro_cost_price > 0 && product.pro_cost_price > product.price}">
		                            <span class="m-item-gone">&yen;${product.pro_cost_price }</span>
		                         </c:if>
                        	</p>
                        	<p class="m-item-float-r">
                        		<c:if test="${!empty product.hyPrice && discount != 1 && product.is_member_discount == 1}">
	                             	<span class="r">会员:&yen;${product.hyPrice }</span><br>
	                            </c:if>
                        		<c:if test="${!empty product.pfPrice }">
	                         		<span class="r">批发：&yen;${product.pfPrice }</span>
	                         	</c:if>
                        	</p>
                        </div>
                        <%-- <p class="m-item-cur">
                            <span>&yen;${product.price}</span>
                            <c:if test="${!empty product.hyPrice && discount != 1 && product.is_member_discount == 1}">
                             <span class="r">会员:&yen;${product.hyPrice }</span><br>
                            </c:if>
                        </p>
                        <!-- <p class="m-item-gone">&yen;7888.00</p> -->
                        <p class="m-item-cur">
	                        <c:if test="${product.pro_cost_price > 0 && product.pro_cost_price > product.price}">
	                            <span class="m-item-gone">&yen;${product.pro_cost_price }</span>
	                         </c:if>
                         	<c:if test="${!empty product.pfPrice }">
                         	<span class="r">批发：&yen;${product.pfPrice }</span>
                         	</c:if>
                         </p> --%>
                    </div>
                </a>
            </li>
            	</c:forEach>
        	</c:if>
        </ul>
    </div>
</div>

<jsp:include page="/jsp/mall/phoneFooterMenu.jsp"></jsp:include>
<input type="hidden" class="shopid" value="${shopid }"/>
<input type="hidden" class="saleMemberId" value="${saleMemberId }"/>
<input type="hidden" class="userid" value="${userid }"/>

<input type="hidden" class="img" value="${http}${mallSet.mallHeadPath}"/>
<input type="hidden" id="shopname" value="${mallSet.mallName}"><!-- 店铺id  -->

<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerMallSet.js"></script>
<script type="text/javascript" src="/js/plugin/gt/js/gt_common.js"></script>
<script type="text/javascript">
var title= $("input#shopname").val();
var imgUrls= $("input.img").val();
var url = "${url}/phoneSellers/${saleMemberId }/79B4DE7C/mallIndex.do?share=show";
wx.config({
	debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.get('appid')}", // 必填，公众号的唯一标识
    timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
    nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
    signature: "${record.get('signature')}",// 必填，签名，见附录1
    jsApiList: ["onMenuShareTimeline","onMenuShareAppMessage","showAllNonBaseMenuItem"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});
wx.ready(function(){
	//显示所有功能按钮接口
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

$(window).load(function() {
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
		location.href = "/phoneSellers/${saleMemberId }/79B4DE7C/shoppingall.do?proName="+search+"&uId=${userid}";
	}else{
		location.href = "/phoneSellers/${saleMemberId }/79B4DE7C/shoppingall.do?uId=${userid}";
	}
}


</script>
</body>
</html>