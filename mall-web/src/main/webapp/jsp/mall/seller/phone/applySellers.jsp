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
<title>超级销售员申请</title>
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
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>

<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="/js/plugin/layer-mobile/layer/need/layer.css"/>
<div class="sWrapper">
    <p class="a-tip">
        基本信息的有效性决定您能否成为超级销售员。
    </p>
    <div class="a-main">
        <div class="a-box">
            <label class="a-label">
            <em><c:if test="${sellerSet.isNameRequired == 1 }">*</c:if></em>姓名</label>
            <input type="text" placeholder="请输入本人真实姓名" class="a-input" id="name" name="userName">
        </div>
        <div class="a-box">
            <label class="a-label">
            <em><c:if test="${sellerSet.isCompanyRequired == 1 }">*</c:if></em>公司名称</label>
            <input type="text" placeholder="请输入公司名称" class="a-input" id="companyName" name="companyName">
        </div>
        <div class="a-box">
            <label class="a-label"><em>*</em>手机号码</label>
            <input type="text" placeholder="请输入手机号码" class="a-input" maxlength="11" id="tel" name="telephone">
        </div>
        <div class="a-box">
            <label class="a-label"><em>*</em>验证码</label>
            <input type="text" placeholder="请输入收到的验证码" class="a-input-s" maxlength="6" id="code" name="code">
            <a id="codeBtn" class="a-code-btn" onclick="sendMsg(0,42297,'',this);"">获取验证码</a>
        </div>
    </div>
    <div class="a-main-2">
        <label class="a-label">
        <em><c:if test="${sellerSet.isRemarkRequired == 1 }">*</c:if></em>备注</label>
        <textarea name="remark" id="remark" cols="30" rows="10" placeholder="请输入" class="a-textarea"></textarea>
    </div>
    <input type="hidden" id="isName" value="${sellerSet.isNameRequired}" />
    <input type="hidden" id="isCompany" value="${sellerSet.isCompanyRequired}" />
    <input type="hidden" id="isRemark" value="${sellerSet.isRemarkRequired}" />
    <input type="hidden" class="memberId" value="${memberId }"/>
    <div class="a-bottom">
        <a href="JavaScript:void(0)" class="full-blue-btn" id="applyBut">申请</a>
        <p class="a-remark">
            您可以通过朋友圈分享，推广海报发布等多种方式，将客户引导到我们的商城或关注我们的公众号，
            只要他们购买了符合提成标准的产品，您就可以自动赚取佣金!
        </p>
    </div>
</div>
<input type="hidden" class="userid" value="${userid }"/>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="/js/common/phone.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/applySeller.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript">

wx.config({
	debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.get('appid')}", // 必填，公众号的唯一标识
    timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
    nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
    signature: "${record.get('signature')}",// 必填，签名，见附录1
    jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

wx.ready(function(){
 wx.hideOptionMenu();
});



</script>
</body>
</html>