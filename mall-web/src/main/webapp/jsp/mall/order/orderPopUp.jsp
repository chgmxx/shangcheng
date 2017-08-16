<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+path+"/";

%>

<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>订单弹出框</title>

<link rel="stylesheet" type="text/css" href="/css/mall/order.css" />
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<style>
  body{
    overflow-x: hidden;
  }
</style>
</head>
<body>
	<!-- 卖家备注开始 -->
	<c:if test="${type == 1 }">
		<div class="modal">
			<input type="hidden" name="orderId" id="orderId" value="${orderId}" />
			<div style="border-bottom: 1px solid #eee;">
				<h3 class="title">卖家备注</h3>
			</div>
			<div class="modal-body">
			    <textarea class="js-remark" rows="4" placeholder="最多可输入256个字符" maxlength="256" 
			    	style="width: 396px;" id="orderSellerRemark">${result.orderInfo[0].order_seller_remark}</textarea>
			    <div style="text-align: center;">
			        <a id="subRemark" class="ui-btn ui-btn-primary" style="width: 120px;">提交</a>
			    </div>
			</div>
		</div>
	</c:if>
	<!-- 卖家备注结束 -->
	
	<!-- 取消订单开始 -->
	<c:if test="${type == 2 }">
		<div class="modal" style="width: 262px;margin-left: -125px;">
		<input type="hidden" name="orderId" id="orderId" value="${orderId}" />
		<input type="hidden" name="type" id="type" value="${type}" />
			<div style="border-bottom: 1px solid #eee;">
				<h3 class="title">取消订单</h3>
			</div>
			<div class="modal-body">
			    <select id="reason">
			    	<c:if test="${result.orderInfo[0].seller_cancel_reason == null || result.orderInfo[0].seller_cancel_reason != '' }">
			    		<option value="0">请选择一个取消订单的理由</option>
			    	</c:if>
			    	<c:forEach var="orderReason" items="${cancelReason }">
			    		<option ${orderReason.item_key == result.orderInfo[0].seller_cancel_reason?'selected':''}
			    			value="${orderReason.item_key }">${orderReason.item_value }</option>
			    	</c:forEach>
			    </select>
			    <div style="text-align: center;">
			        <a id="defineOrder" class="ui-btn ui-btn-primary" style="width:60px;margin-top:30px;border-radius:2px;">确定</a>
			        <a id="cancelOrder" class="ui-btn" 
			        	style="width:60px;margin-top:30px;background:#f8f8f8;border-radius:2px;color:#333;">取消</a>
			    </div>
			</div>
		</div>
	</c:if>
	<!-- 取消订单结束 -->
	
	<!-- 修改价格开始 -->
	<c:if test="${type == 3 }">
		<div class="modal" style="width: 652px;height: 300px;margin-left: -320px; position: absolute;">
			<input type="hidden" name="orderId" id="orderId" value="${orderId}" />
			<div style="border-bottom: 1px solid #eee;">
				<h3 class="title">修改订单金额</h3>
			</div>
			<div class="modal-body">
			    <div>
			    	<table class="table order-price-table">
			    		<thead>
			    			<tr>
			    				<th style="min-width: 80px; max-width: 80px;">商品</th>
								<th style="min-width: 50px; max-width: 50px;">单价(元)</th>
								<th style="min-width: 35px; max-width: 35px;">数量</th>
								<th style="min-width: 50px; max-width: 50px;">小计(元)</th>
								<th style="min-width: 50px; max-width: 50px;">填写修改商品总价（单价*数量）</th>
			    			</tr>
			    		</thead>
			    		<tbody>
			    			<c:forEach var="order" items="${result.orderDetail }" varStatus="s">
				    			<tr class="order_tr" o_id="${order.id }">
				    				<td class="tb-name">
						                <a class="new-window">${order.det_pro_name }</a>
						                <p><span class="c-gray"></span></p>
						            </td>
						            <td class="tb-price">${order.det_pro_price }</td>
						            <td class="tb-num">${order.det_pro_num }</td>
						            <c:set var="countPrice" value="${order.det_pro_price*order.det_pro_num }"></c:set>
						            <td class="tb-total" style="border-right: 1px solid #e4e4e4;">${countPrice }
						            <c:if test="${order.total_price > countPrice && order.total_price*1-countPrice*1 > 0}">
						            	<c:set var="nowprice" value="${order.total_price*1-countPrice*1 }"></c:set>
							            <span>+<em class="nowPrice">${nowprice }</em></span>
						            </c:if>
						            </td>
						            <td class="tb-discount">
						                <input type="text" id="orderMoney" name="orderMoney" value="${order.total_price }">
						                <input type="hidden" id="proNum" value="${order.det_pro_num }"/>
						            </td>
						            <%-- <c:if test="${s.count==1}">
							            <td class="tb-discount" rowspan="${fn:length(result.orderDetail)}">
							                <input type="text" id="orderMoney" name="orderMoney" value="${result.orderInfo[0].order_money }">
							            </td>
						            </c:if> --%>
				    			</tr>
			    			</c:forEach>
			    		</tbody>
			    	</table>
			    </div>
			    <div class="final js-footer text-left">
			    	<div>
			    		<p>收货地址： ${result.orderInfo[0].pName}${result.orderInfo[0].cName}${result.orderInfo[0].aName}${result.orderInfo[0].mem_address },
							${result.orderInfo[0].mem_name }(收),${result.orderInfo[0].mem_phone }</p>
						<p>买家实付：${result.orderInfo[0].order_money }元
							<c:if test="${result.orderInfo[0].order_freight_money != null && result.orderInfo[0].order_freight_money != ''&& result.orderInfo[0].order_freight_money > 0 }">
								<span style="color: red;">(含运费${result.orderInfo[0].order_freight_money })</span>
							</c:if>						
						</p>
					</div>
				</div>
			    <div class="modal-footer clearfix">
				    <a class="ui-btn ui-btn-primary" id="updateMoney"
				    	style="width:60px;border-radius:2px;margin-left: 510px;list-style: none;">确 定</a>
				</div>
			</div>
		</div>
	</c:if>
	<!-- 修改价格结束 -->
	
	<!-- 发货开始 -->
	<c:if test="${type == 4 }">
		<div class="modal" style="width:545px;margin-left:-275px;position: absolute;">
		<c:if test="${count == 0 }">
			<p style="margin-top: 130px;text-align: center;">该用户购买的商品属拼团商品，目前拼团人数不够，暂不能发货。</p>
		</c:if>
		<c:if test="${count == 1 }">
		<input type="hidden" name="orderId" id="orderId" value="${orderId}" />
			<div style="border-bottom: 1px solid #eee;">
				<h3 class="title">商品发货</h3>
			</div>
			<div class="modal-body">
			    <div>
			    	<table class="table order-price-table">
			    		<thead>
			    			<tr>
			    				<th>商品名称</th>
			    				<th>单价（元）</th>
			    				<th>数量</th>
			    				<th>订单编号</th>
			    				<th>实付金额（元）</th>
			    			</tr>
			    		</thead>
			    		<tbody>
			    			<c:forEach var="order" items="${result.orderDetail }" varStatus="s">
				    			<tr>
				    				<td class="tb-name">
				    					<a class="new-window" >${order.det_pro_name }</a>
				    				</td>
				    				<td>${order.det_pro_price }</td>
				    				<td style="border-right: 1px solid #E4E4E4;">${order.det_pro_num }</td>
				    				<c:if test="${s.count==1}">
					    				<td rowspan="${fn:length(result.orderDetail)}" style="border-right: 1px solid #E4E4E4;">
					    					${result.orderInfo[0].order_no }
					    				</td>
					    				<td rowspan="${fn:length(result.orderDetail)}">${result.orderInfo[0].order_money }</td>
				    				</c:if>
				    			</tr>
			    			</c:forEach>
			    		</tbody>
			    	</table>
			    </div>
			    <div style="font-size: 14px;margin-top: 15px;">
			    	 <div class="order_ship" style="width:545px;height:auto;line-height: 25px;">
			    	 	<label class="fl" style="widows: 20%;">收货信息：</label>
			    	 	<div class="fl" style="width: 80%;">${result.orderInfo[0].pName}${result.orderInfo[0].cName}${result.orderInfo[0].aName}${result.orderInfo[0].mem_address },
							${result.orderInfo[0].mem_name }(收),${result.orderInfo[0].mem_phone }</div>
			    	 </div>
			    	 <div class="order_ship">
			    	 	<label class="fl">发货方式：</label>
			    	 	<div class="fl">
			    	 		<label><input type="radio" name="logistics" id="needLogistics" checked="checked" value="1" />需要物流</label>
			    	 		<label><input type="radio" name="logistics" id="noLogistics" value="0" />无需物流</label>
			    	 	</div>
			    	 </div>
			    	 <div class="order_ship">
			    	 	<div class="fl">
				    	 	<label class="fl">物流公司：</label>
				    	 	<div class="fl">
				    	 		<select id="logisticsCompany">
				    	 			<c:forEach var="company" items="${logisticsCompany }">
							    		<option ${company.item_key == result.orderInfo[0].express_id?'selected':''}
							    			value="${company.item_key }">${company.item_value }</option>
							    	</c:forEach>
				    	 		</select>
				    	 	</div>
			    	 	</div>
			    	 	<div class="fl">
			    	 		<label class="fl">快递单号：</label>
			    	 		<div class="fl"><input type="text" name="expressDelivery" id="expressDelivery" /></div>
			    	 	</div>
			    	 </div>
			    </div>
			    <div style="text-align: right;">
			        <a id="shipBtn" class="ui-btn ui-btn-primary" style="width:50px;margin-top:10px;border-radius:2px;">确定</a>
			        <a id="cancelOrder" class="ui-btn" 
			        	style="width:50px;margin-top:10px;background:#f8f8f8;border-radius:2px;color:#333;">取消</a>
			    </div>
			</div>
			</c:if>
		</div>
	</c:if>
	<!-- 发货结束 -->
	
	<!-- 确认提货 -->
	<c:if test="${type == 5 }">
	<div class="modal" style="width:243px;margin-left: -125px;">
		<input type="hidden" name="orderId" id="orderId" value="${orderId}" />
		<input type="hidden" name="type" id="type" value="${type}" />
		<p style="margin-top: 35px;text-align: center;">确认买家是否已提货并付款？</p>
		<div style="text-align: right;">
	        <a id="thBth" class="ui-btn ui-btn-primary" style="width:50px;margin-top:30px;border-radius:2px;">确定</a>
	        <a id="cancelOrder" class="ui-btn" 
	        	style="width:50px;margin-top:10px;background:#f8f8f8;border-radius:2px;color:#333;">取消</a>
	    </div>
	</div>
	</c:if>
	<jsp:include page="/jsp/common/headerCommon.jsp"/>
</body>
<script type="text/javascript">
$(".nowPrice").each(function(){
	var vals = $(this).val();
	if(vals == null || vals ==""){
		$(this).parent().hide();
	}else{
		$(this).val((vals*1).toFixed(2));
	}
});
$(function(){
	//修改订单价格
	$('#updateMoney').click(function(){
		var orderId = $('#orderId').val();
		var detail=[];
		var orderMoney = 0;
		$(".order_tr").each(function(i){
			var detailMoney = $(this).find("#orderMoney").val();
			detail[i] = {
				id:$(this).attr("o_id"),
				proMoney : detailMoney,
				num : $(this).find("#proNum").val(),
			}
			orderMoney += detailMoney*1;
		});
		orderMoney = orderMoney.toFixed(2);
		if(orderMoney != "" && orderId != "" && orderMoney > 0){
			$.post("mallOrder/upMoneyOrRemark.do", {
				orderId: orderId,
				orderMoney: orderMoney,
				detailObj : JSON.stringify(detail)
			}, function(result) {
				parent.location.href = "mallOrder/indexstart.do";
                parentCloseAll();
//				parent.layer.closeAll();
			},"json");
		}else{
			if(orderMoney < 0){
                parentAlertMsg("价格必须大于0。");
//				parent.alertMsg("价格必须大于0。");
				$('#orderMoney').val("");
			}else{
                parentAlertMsg("价格不能为空。");
//				parent.alertMsg("价格不能为空。");
				/* parent.location.href = "mallOrder/indexstart.do";
				parent.layer.closeAll(); */
			}
		}
	});
	
	//发货确认按钮
	$('#shipBtn').click(function(){
		var expressDelivery = $('#expressDelivery').val();
		var expressId = $('#logisticsCompany option:selected').val();
		var expressWay = $("input[name='logistics']:checked").val();
		var orderId = $('#orderId').val();
		if( expressId == 0 && expressWay == 1){
			alert("请选择物流公司");
		}else if(expressDelivery == "" && expressWay == 1){
			alert("请输入快递单号");
		}else{
			//parent.layer.closeAll();
			$.post("mallOrder/upMoneyOrRemark.do", {
				orderId: orderId,
				expressDelivery: expressDelivery,
				expressId: expressId,
				expressWay: expressWay,
				express: 1
			}, function(result) {
				//parent.layer.closeAll();
				parent.location.href = "mallOrder/indexstart.do";
			},"json");
		}
	});
	
	//提交备注
	$('#subRemark').click(function(){
		var remark = $('#orderSellerRemark').val();
		var orderId = $('#orderId').val();
		if(remark != ""){
			$.post("mallOrder/upMoneyOrRemark.do", {
				orderId: orderId,
				orderSellerRemark: remark
			}, function(result) {
                parentCloseAll();
//				parent.layer.closeAll();
			},"json");
		}else{
            parentCloseAll();
//			parent.layer.closeAll();
		}
	});
	
	//取消订单理由
	$('#defineOrder').click(function(){
		var reason = $('#reason option:selected').val();
		var orderId = $('#orderId').val();
		var type = $('#type').val();
		if(reason == 0){
			alert("请选择一个取消订单的理由");
		}else if(reason != 0){
			$.post("mallOrder/upMoneyOrRemark.do", {
				type: type,
				orderId: orderId,
				sellerReason: reason
			}, function(result) {
				//parent.layer.closeAll();
                //TODO  parent.location.href
				parent.location.href =  window.parent.location.href;
			},"json");
		}
	});
	
	//取消按钮
	$('#cancelOrder').click(function(){
//		parent.layer.closeAll();
        parentCloseAll();
	});
	
	$("#thBth").click(function(){
		var orderId = $('#orderId').val();
		var type = $('#type').val();
		$.post("mallOrder/upMoneyOrRemark.do", {
			type: type,
			orderId: orderId,
			status : 4
		}, function(result) {
			//parent.layer.closeAll();
			//TODO  parent.location.href
			parent.location.href =  window.parent.location.href;
		},"json");
	});
});
</script>
</html>