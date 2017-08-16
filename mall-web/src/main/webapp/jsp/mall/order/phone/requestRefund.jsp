<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>申请退款</title>
	<%
		String path = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"
			+request.getServerPort()+path+"/";
	%>
	<base href="<%=basePath%>">
	<meta http-equiv="X-UA-Compatible"content="IE=edge,chrome=1" />
	    <meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
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
	    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
	    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>" />
   <!--  <link rel="stylesheet" href="/css/mall/reset.css"/> -->
<style type="text/css">
html { font-size: 50px; }
body { font-size: 24px; }
@media screen and (max-width: 359px) {
 html { font-size: 25px;}
body { font-size: 12px; }
}
@media screen and (min-width: 360px) {
    html { font-size: 28.13px;}
    body { font-size: 12px; }
}
@media screen and (min-width: 375px) {
    html { font-size: 29.30px;}
    body { font-size: 14px; }
}
@media screen and (min-width: 384px) {
    html { font-size: 30px;}
    body { font-size: 14px; }
}
@media screen and (min-width: 400px) {
    html { font-size: 31.25px;}
    body { font-size: 15px; }
}
@media screen and (min-width: 414px) {
    html { font-size: 32.34px;}
    body { font-size: 16px; }
}
@media screen and (min-width: 424px) {
    html { font-size: 33.13px;}
    body { font-size: 16px; }
}
@media screen and (min-width: 480px) {
    html { font-size: 37.50px;}
    body { font-size: 18px; }
}
@media screen and (min-width: 540px) {
    html { font-size: 42.19px;}
    body { font-size: 20px; }
}
@media screen and (min-width: 640px) {
    html { font-size: 50px;}
    body { font-size: 24px;}
}

.order-info,.refund-info2{
  		width: 12.8rem;
  		margin: 0 auto;
  		border-bottom: 1px solid #D2D2D2;
  	}
  	.order-info-tb tr td{
  		height: 0.85rem;
  		font-size: 0.48rem;
  	}
  	.order-info-tb tr td:nth-of-type(1){
  		color: #ABABAB;
  	}
  	.order-info-tb tr td:nth-of-type(2){
  		text-align: right;
  	}
  	.refund-info2 ul{
  		width: 95%;
  		margin: 0 auto;
  		font-size: 0.52rem;
  		padding:0px;
  	}
  	.refund-info2 ul li{
  		height: 1.5rem;
  		line-height: 1.5rem;
  		border-bottom: 1px solid #EEEEEE;
  		list-style : none;
  	}
  	.refund-info2 ul li:last-child{
  		border-bottom: 0;
  	}
  	.refund-info2 ul li label{
  		float: left;
  		color: #888787;
  	}
  	.refund-info2 ul li div{
  		margin-left: 2.5rem;
  	}
  	.refund-info2 select,.refund-info2 input{
  		border: none;
  		width: 90%;
  		height: 100%;
  		background: transparent;
  		appearance:none;
  		-moz-appearance:none; 
  		-webkit-appearance:none;
  	}
  	.refund-info2 select{
  		background: url(img/grey-arrow.png) no-repeat right center;
  	}
  	.refund-info2 select,.refund-info2 input{
  		font-size: 0.52rem;
  	}
  	.inp-submit{
  		display: block;
  		border: none;
  		width: 90%;
  		margin: 0.5rem auto;
  		height: 1.5rem;
  		text-align: center;
  		color: #FFFFFF;
  		background-color: #F20000;
  		font-size: 0.5rem;
  		border-radius: 8px;
  		appearance:none;
  		-moz-appearance:none; 
  		-webkit-appearance:none;
  	}
  	em {color:red;}
  	td.c3{width:18%;}
  	label{width:22%;}
  	label,div.li_div{height:1.5rem;}
  </style>
</head>
<body>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>

		<section class="order-info">
			<table border="0" cellspacing="0" class="order-info-tb" cellpadding="0" width="95%" style="margin: 0 auto;">
				<tr>
					<td class="c3">商品名称</td>
					<td><c:if test="${!empty map }">${map.proName }</c:if></td>
				</tr>
				<tr>
					<td class="c3">订单金额</td>
					<td>￥<c:if test="${!empty map }">${map.orderMoney }</c:if></td>
				</tr>
				<tr>
					<td class="c3">订单编号</td>
					<td><c:if test="${!empty map }">${map.orderNo }</c:if></td>
				</tr>
				<tr>
					<td class="c3">交易时间</td>
					<td><c:if test="${!empty map }"><fmt:formatDate value="${map.payTime}" type="both" /></c:if></td>
				</tr>
				<c:if test="${type == 1 }">
				<tr>
					<td class="c3">退货时间</td>
					<td><c:if test="${!empty orderReturn }"><fmt:formatDate value="${orderReturn.updateTime}" type="both" /></c:if></td>
				</tr>
				<tr>
					<td class="c3">退货地址</td>
					<td><c:if test="${!empty orderReturn }">${orderReturn.returnAddress}</c:if></td>
				</tr>
				</c:if>
			</table>
		</section>
		<section class="refund-info2">
			<c:set var="price" value="${map.proPrice }"></c:set>
			<c:if test="${map.total_price > 0 }">
				<c:set var="price" value="${map.total_price }"></c:set>
			</c:if>
			<input type="hidden" id="id" value='<c:if test="${!empty orderReturn }">${orderReturn.id }</c:if>'/>
			<input type="hidden" id="detailId" value="<c:if test='${!empty map }'>${map.dId}</c:if>"/>
			<input type="hidden" id="orderId" value="<c:if test='${!empty map }'>${map.oId}</c:if>"/>
			<input type="hidden" id="returnMoney" value="<c:if test="${!empty map.returnMoney }">${map.returnMoney }</c:if><c:if test="${empty map.returnMoney }">${price }</c:if>"/>
			<input type="hidden" id="shopId" value="<c:if test="${!empty map }">${map.shopId }</c:if>"/>
			<input type="hidden" id="type" value="<c:if test="${!empty type }">${type }</c:if>"/>
			<input type="hidden" id="fenbi" value="<c:if test="${!empty map }">${map.use_fenbi }</c:if>"/>
			<input type="hidden" id="jifen" value="<c:if test="${!empty map }">${map.use_jifen }</c:if>"/>
			<input type="hidden" id="isWallet" value="${map.is_wallet }"/>
			<c:set value="0" var="way"/>
			<c:set value="0" var="reason"/>
			<c:set value="0" var="companyId"/>
			<c:if test="${!empty orderReturn }">
				<c:if test="${orderReturn.retHandlingWay != null}"><c:set value="${orderReturn.retHandlingWay }" var="way"/></c:if>
				<c:if test="${orderReturn.retReasonId != null}"><c:set value="${orderReturn.retReasonId }" var="reason"/></c:if>
				<c:if test="${orderReturn.wlCompanyId != null}"><c:set value="${orderReturn.wlCompanyId }" var="companyId"/></c:if>
			</c:if>
			<ul>
				<c:if test="${type == 0 }">
				<li>
					<label for=""><em>*</em>处理方式</label>
					<div class="li_div">
						<select name="" class="handlingWay">
							<option value="">请选择处理方式</option>
							<c:if test="${map.order_pay_way != 2 && map.order_pay_way != 6}">
								<option value="1" <c:if test="${way == 1}">selected='selected'</c:if>>我要退款，但不退货</option>
								<c:if test="${!empty map }">
									<c:if test="${map.orderStatus == 3 || map.orderStatus == 4}">
										<option value="2" <c:if test="${way == 2}">selected='selected'</c:if>>我要退款退货</option>
									</c:if>
								</c:if>
							</c:if>
							<c:if test="${map.order_pay_way == 2 || map.order_pay_way == 6}">
								<c:if test="${map.is_wallet == 0 && map.orderStatus == 2}">
									<option value="2" <c:if test="${way == 1}">selected='selected'</c:if>>我要退货</option>
								</c:if>
								<c:if test="${map.is_wallet == 1 && map.orderStatus == 2}">
									<option value="1" <c:if test="${way == 2}">selected='selected'</c:if>>我要退款</option>
								</c:if>
								<c:if test="${!empty map }">
									<c:if test="${map.orderStatus == 3 || map.orderStatus == 4}">
										<c:if test="${map.is_wallet == 0 }">
											<option value="2" <c:if test="${way == 2}">selected='selected'</c:if>>我要退货</option>
										</c:if>
										<c:if test="${map.is_wallet == 1 }">
											<option value="1" <c:if test="${way == 1}">selected='selected'</c:if>>我要退款，但不退货</option>
										</c:if>
									</c:if>
								</c:if>
							</c:if>
						</select>
					</div>
				</li>
				<li>
					<label for=""><em>*</em>退款原因</label>
					<div class="li_div">
						<select name="" class="reasonId">
							<option value="">请选择退款原因</option>
							<c:if test="${!empty dictMap }">
								<c:forEach var="dic" items="${dictMap }">
									<option value="${dic.item_key }" <c:if test="${reason == dic.item_key}">selected='selected'</c:if>>${dic.item_value }</option>
								</c:forEach>
							</c:if>
						</select>
					</div>
				</li>
				<li>
					<label for="">&nbsp;退款金额</label>
					<div class="li_div" style="margin-left:2.6rem;font-size: 0.52rem;"><%-- <c:if test="${!empty map.returnMoney }">${map.returnMoney }</c:if><c:if test="${empty map.returnMoney }">${map.proPrice }</c:if> --%>
					<c:if test="${(map.order_pay_way != 2 && map.order_pay_way != 6) || map.is_wallet == 1}">${price }</c:if>
					<c:if test="${(map.order_pay_way == 2 || map.order_pay_way == 6) && map.is_wallet == 0}">0.00</c:if>
					</div>
				</li>
				<li>
					<label for=""><em>*</em>手机号码</label>
					<div class="li_div"><input type="tel" name="" id="telephone" value="<c:if test="${!empty orderReturn }">${orderReturn.retTelephone }</c:if>" placeholder="填写手机号码便于卖家联系您"/></div>
				</li>
				<li>
					<label for="">&nbsp;备注信息</label>
					<div class="li_div"><input type="text" name="" id="remark" maxlength="200" value="<c:if test="${!empty orderReturn }">${orderReturn.retRemark }</c:if>" placeholder="最多可填200字"/></div>
				</li>
				</c:if>
				<c:if test="${type == 1 }">
				<li>
					<label for=""><em>*</em>物流方式</label>
					<div class="li_div">
						<select name="" class="companyId">
							<option value="">请选择物流公司</option>
							<c:if test="${!empty comMap }">
								<c:forEach var="com" items="${comMap }">
									<option value="${com.item_key }" <c:if test="${companyId == com.item_key}">selected='selected'</c:if>>${com.item_value }</option>
								</c:forEach>
							</c:if>
						</select>
					</div>
				</li>
				<li>
					<label for=""><em>*</em>物流单号</label>
					<div class="li_div">
						<input type="text" name="" id="wlNo" maxlength="" value="<c:if test="${!empty orderReturn }">${orderReturn.wlNo }</c:if>" placeholder="请填写物流单号"/>
					</div>
				</li>
				<li>
					<label for=""><em>*</em>手机号码</label>
					<div class="li_div"><input type="tel" name="" id="wlTelephone" value="<c:if test="${!empty orderReturn }">${orderReturn.wlTelephone }</c:if>" placeholder="请填写手机号码便于卖家联系您"/></div>
				</li>
				<li>
					<label for="">&nbsp;备注信息</label>
					<div class="li_div"><input type="text" name="" id="wlRemark" maxlength="200" value="<c:if test="${!empty orderReturn }">${orderReturn.wlRemark }</c:if>" placeholder="最多可填200字"/></div>
				</li>
				</c:if>
			</ul>
		</section>
		<input type="hidden" value="${map.order_pay_way }" class="payWay" />
		<input type="button" name="" id="" value="提交" class="inp-submit"/>
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<script src="/js/plugin/layer/layer.js"></script>
		<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
		<script type="text/javascript">
		$(window).load(function() {
	    	setTimeout(function() {
	    		$(".loading").hide();
	    	}, 1000);
	    });
		
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
			
			//提交申请退款信息
			$(".inp-submit").click(function(){
				var id = $("#id").val();
				var phoneTest = /^1[3|4|5|8][0-9]\d{8}$/;
				var orderId= $("#orderId").val();
				var detailId= $("#detailId").val();
				var type = $("#type").val();
				if(type == 0){//退款申请
					var handlingWay = $(".handlingWay option:selected").val();//处理方式
					var reasonId = $(".reasonId option:selected").val();//退款原因ID
					var reason =$(".reasonId option:selected").text();//退款原因
					var returnMoney = $("#returnMoney").val();//退款金额
					var telephone = $("#telephone").val();//退款联系人
					var remark = $("#remark").val();//退款备注
					var shopId = $("#shopId").val();
					var jifen = $("#jifen").val();//退还粉币
					var fenbi = $("#fenbi").val();//退还积分
					var isWallet = $("#isWallet").val();//是否使用了钱包支付
					var params = {
		    				orderId : orderId,
		    				orderDetailId: detailId,
		    				retHandlingWay:handlingWay,
		    				retReasonId:reasonId,
		    				retReason:reason,
		    				retMoney:returnMoney,
		    				retTelephone:telephone,
		    				retRemark:remark,
		    				shopId:shopId,
		    				returnFenbi:fenbi,
		    				returnJifen:jifen
		    			};
					if(handlingWay == null || $.trim(handlingWay) == ""){
						alert("请选择处理方式");
					}else if(reasonId == null || $.trim(reasonId) == ""){
						alert("请选择退款原因");
					}else if(telephone == null || $.trim(telephone) == ""){
						alert("请填写正确的手机号码");
						$("#telephone").focus();
					}else  if(!phoneTest.test(telephone)){
						alert("请填写正确的手机号码");
						$("#telephone").focus();
				    }else if(remark.length > 200){
				    	 alert("备注信息的长度不能大于200");
				    }else{
				    	 if(id != null && id != ""){
				    		 params.id = id;
				    	 }
				    	 params.status = 0;
				    	 var payWay = $("input.payWay").val();
				    	 if(payWay != null && payWay != "" && ((payWay == 2 || payWay == 6) && isWallet == 0)){
				    		 params.retMoney = 0;
				    	 }
				    	 //console.log(params)
				    	returnOrder(params,"申请退款");
				     }
				}else{
					var companyId = $(".companyId option:selected").val();
					var company = $(".companyId option:selected").text();
					var wlNo = $("#wlNo").val();//物流
					var wlTelephone = $("#wlTelephone").val();//手机号码
					var wlRemark = $("#wlRemark").val();//备注
					var params ={
							orderId : orderId,
		    				orderDetailId: detailId,
							wlCompanyId:companyId,
							wlCompany:company,
							wlNo:wlNo,
							wlTelephone:wlTelephone,
							wlRemark:wlRemark
					};
					if(companyId == null || $.trim(companyId)==""){
						alert("请选择物流公司");
					}else if(wlNo == null || $.trim(wlNo) == ""){
						alert("请输入物流单号");
						 $("#wlNo").focus();
					}else if(wlNo.length > 50){
						alert("物流单号的长度必须要小于50");
						 $("#wlNo").focus();
					}else if(wlTelephone == null || $.trim(wlTelephone) ==""){
						alert("请输入手机号码");
						$("#wlTelephone").focus();
					}else  if(!phoneTest.test(wlTelephone)){
						alert("请填写正确的手机号码");
						$("#wlTelephone").focus();
				    }else if(wlRemark.length > 200){
				    	 alert("备注信息的长度不能大于200");
				     }else{
				    	 if(id != null && id != ""){
				    		 params.id = id;
				    	 }
				    	 params.status = 3;
				    	 //console.log(params)
				    	 returnOrder(params,"填写物流信息");
				     }
				}
				
			});
			function returnOrder(params,tips){
				 var layerLoad = layer.load(1, {
	    			shade : [ 0.3, '#000' ],
	    			offset : "30%"
	    		 });
				 $.ajax({
		    			type : "post",
		    			url : "/phoneOrder/79B4DE7C/addReturnOrder.do",
		    			data : params,
		    			dataType : "json",
		    			success : function(data) {
		    				layer.closeAll();
		    				if (data.flag == true) {// 重新登录
		    					alert(tips+"成功");
		    					//location.href = "/phoneOrder/79B4DE7C/orderList.do";
		    					//window.history.go(-1);//返回上一页面，并重新加载js
		    					location.href=document.referrer;//返回上一页面，并重新加载页面
		    				} else {// 编辑失败
		    					alert(tips+"失败");
		    				}

		    			},
		    			error : function(XMLHttpRequest, textStatus, errorThrown) {
		    				layer.closeAll();
		    				alert(tips+"失败");
		    				return;
		    			}
		    		});
			}
		</script>
	</body>
</html>
