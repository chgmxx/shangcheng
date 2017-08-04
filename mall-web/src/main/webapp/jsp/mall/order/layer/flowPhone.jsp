
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
<div class="pay-layer" id="flowPhoneDiv">
	<div class="alliance-dialog">
		<div class="alliance-dialog__hd"><strong class="alliance-dialog__title">流量充值</strong></div>
		<div class="alliance-dialog__bd">
			<!-- <p class="alliance-dialog__info">该商家已加入商家联盟，可享受联盟折扣优惠。</p> -->
			<div class="alliance-dialog__item bordered">
				<label for="">手机号：<input type="tel" id="flowPhone" value="" placeholder="请输入手机号" class="alliance-ipt" /></label>
			</div>
			<!-- <div class="alliance-dialog__outer flex">
				<div class="alliance-dialog__item bordered">
					<label for="">验证码：<input type="number" name="" id="unionCode" value="" placeholder="请输入验证码" class="alliance-ipt alliance-ipt2"/></label>
				</div>
				<div class="alliance-code__box">
					<button name="" id="" class="alliance-code" onclick="getCode(this)">获取验证码</button>
				</div>
			</div> -->
		</div>
		<div class="alliance-dialog__ft alliance-f">
            <button class="alliance-dialog__btn alliance-dialog__btn_default alliance-ok-btn" onclick="flowSubmitOrder(this);">提交订单</button>
        </div>
	</div>
</div>
<script type="text/javascript">
	function flowSubmitOrder(){
		var flowPhone = $("#flowPhone").val();
		if(flowPhone == null || flowPhone == "" || typeof(flowPhone) == "undefined"){
			flowDialog("请填写正确的手机号",null);
			return false;
		}
		var phoneFlag = Mobilephone(flowPhone);
		if(!phoneFlag){
			flowDialog("请填写正确的手机号",null);
			return false;
		}
		$('#submit-order').click();
		
	}
	function flowDialog(tip,cal){
		gtcom.dialog(tip,"a",cal,"流量充值提示");
	}
</script>
</body>
</html>
