
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="UTF-8">
<title>提交订单</title>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"
			+request.getServerPort()+path+"/";
	%>
	<base href="<%=basePath%>">
   <meta id="meta" name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no">
   <meta name="apple-mobile-web-app-capable" content="yes"/>
   <meta name="format-detection" content="telephone=no"/>
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
</head>
<body>
<div class="pay-layer" id="unionPhoneDiv">
	<div class="alliance-dialog">
		<div class="alliance-dialog__hd"><strong class="alliance-dialog__title">联盟优惠</strong></div>
		<div class="alliance-dialog__bd">
			<p class="alliance-dialog__info">该商家已加入商家联盟，可享受联盟折扣优惠。</p>
			<div class="alliance-dialog__item bordered">
				<label for="">手机号：<input type="tel" name="phone" id="phone" value="" placeholder="请输入手机号" class="alliance-ipt" /></label>
			</div>
			<div class="alliance-dialog__outer flex">
				<div class="alliance-dialog__item bordered">
					<label for="">验证码：<input type="number" name="" id="unionCode" value="" placeholder="请输入验证码" class="alliance-ipt alliance-ipt2"/></label>
				</div>
				<div class="alliance-code__box">
					<button name="" id="" class="alliance-code" onclick="getCode(this)">获取验证码</button>
				</div>
			</div>
		</div>
		<div class="alliance-dialog__ft alliance-f">
            <button class="alliance-dialog__btn alliance-dialog__btn_default alliance-ok-btn" onclick="valUnionMobile(this);">验证</button>
        </div>
	</div>
</div>

<div class="pay-layer" id="unionDiscountDiv" style="overflow: scroll;max-height: 800px;">
   	<ul>
   	    <li class="lay-item">
   			联盟优惠
       		<i class="delete" onclick="hideLay()"></i>
   		</li>
   		<c:set var="unionDiscount" value=""></c:set>
   		<c:if test="${!empty unionMap && unionMap.status == 1}">
   		<li class="flex lay-item unionItem" onclick="unionDiscount(this);">
   			<div class="flex-1" id="unionDiscounts">
   				<span id="unionDiscountSpan">${unionMap.discount }</span>折
   				<c:set var="unionDiscount" value="${unionMap.discount }"></c:set>
   			</div>
   		</li>
   		</c:if>
	   	<li class="flex lay-item unionItem" onclick="unionDiscount('');">
   			<div class="flex-1">取消联盟优惠</div>
   		</li>
	</ul>
</div>
<input type="hidden" class="unionDiscountVal" value="${unionDiscount }"/><!-- 联盟折扣 -->
<input type="hidden" class="union" value="${unionDiscount }"/>
<input type="hidden" class="countUnion" value=""/>

<script type="text/javascript">
/**
 * 计算联盟卡的折扣
 */
function unionDiscount(obj){
	if(obj == "" || obj == null){
		$("input.unionDiscountVal").val("");
		$("#unionSpan").html("您还没选择优惠");
	}else{
		var unionDiscount = $(obj).find("#unionDiscountSpan").text();
		if(unionDiscount != null && unionDiscount != ""){
			$("#unionSpan").html(unionDiscount+"折");
			$("input.unionDiscountVal").val(unionDiscount);
		}
	}
	youhui(3,obj);
	hideLay();
}
</script>
</body>
</html>
