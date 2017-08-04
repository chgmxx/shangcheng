<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品详情</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"
		+request.getServerPort()+path+"/";
%>

<base href="<%=basePath%>">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="">
<link rel="stylesheet" type="text/css" href="/css/common.css" />
<link rel="stylesheet" type="text/css" href="/css/common/comm.css" />
<link rel="stylesheet" type="text/css" href="/css/mall/order.css" />
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/public.js"></script>
<script type="text/javascript" src="/js/mall/mall_public.js"></script>
</head>
<body>
<div class="fansTitle">
	<span class="i-con fl"></span><span class="title-p">商品详情</span>
</div>
<div class="box-btm30">
   	<div class="widget-list">
  			<div class="js-list-filter-region clearfix">
   			<div style="width: 100%;height: 50px; border: 1px solid #e4e4e4;margin-bottom: 15px;">
   			    <div class="stepflex" id="sflex03">
			        <dl class="first doing" id="one">
			            <dt class="s-num">1</dt>
			            <dd class="s-text">1.买家已下单<s></s><b></b></dd>
			        </dl>
			        <dl class="normal last" id="two">
			            <dt class="s-num">2</dt>
			            <dd class="s-text">2.买家已付款<s></s><b></b></dd>
			        </dl>
			        <dl class="normal last" id="three">
			            <dt class="s-num">3</dt>
			            <dd class="s-text">3.卖家已发货<s></s><b></b></dd>
			        </dl>
			        <dl class="normal last" id="four">
			            <dt class="s-num">4</dt>
			            <dd class="s-text">4.订单完成<s></s><b></b></dd>
			        </dl>
			        <c:if test="${result.orderInfo[0].order_status ==5 }">
				        <dl class="normal last" id="five">
				            <dt class="s-num">2</dt>
				            <dd class="s-text">2.订单已关闭<s></s><b></b></dd>
				        </dl>
			        </c:if>
		        </div>
   			</div>
		</div>
		<div class="order_info" style="height:auto;">
			<div class="fl"> 
				<label>订单信息</label>
				<div class="order_detail">
					<ul>
						<li>订单编号：${result.orderInfo[0].order_no }</li>
						<li>所属店铺：${result.orderInfo[0].shopName }</li>
						<li>买家：${result.orderInfo[0].nickname }</li>
						<li>付款方式：
							<c:if test="${result.orderInfo[0].order_pay_way == 1 }">微信支付</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 2 }">货到付款</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 3 }">储值卡支付</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 4 }">积分支付</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 5 }">扫码支付</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 6 }">到店支付</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 7 }">找人代付</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 8 }">粉币支付</c:if>
							<c:if test="${result.orderInfo[0].order_pay_way == 9 }">支付宝支付</c:if>
						</li>
						<li>下单时间：<fmt:formatDate value="${result.orderInfo[0].create_time}" pattern="yyyy-MM-dd HH:mm:ss" /></li>
						<li>配送方式：
							<c:if test="${result.orderInfo[0].delivery_method == 1 }">快递配送</c:if>
							<c:if test="${result.orderInfo[0].delivery_method == 2 }">上门自提</c:if>
						</li>
						<c:if test="${result.orderInfo[0].delivery_method == 1 }">
						<li>收货信息：
							${result.orderInfo[0].pName}${result.orderInfo[0].cName}${result.orderInfo[0].mem_address },
							${result.orderInfo[0].mem_name }(收),${result.orderInfo[0].mem_phone }</li>
						</c:if>
						<c:if test="${!empty result.expressName}">
							<li>快递公司：${result.expressName }</li>
						</c:if>
						<c:if test="${!empty result.orderInfo[0].express_number }">
						<li>快递单号：${result.orderInfo[0].express_number }</li>
						</c:if>
						<c:if test="${result.orderInfo[0].delivery_method == 2 }">
						<li>提货人姓名： ${result.orderInfo[0].appointment_name}  ${result.orderInfo[0].appointment_telephone}</li>
						<li>自提点地址： ${result.take.visitAddressDetail}</li>
						</c:if>
						<li>买家留言：${result.orderInfo[0].order_buyer_message }</li>
					</ul>
				</div>
			</div>
			<div class="fl">
				<div class="status_btn">
					<label>
						<c:if test="${result.orderInfo[0].order_status == 1}">等待买家付款</c:if>
						<c:if test="${result.orderInfo[0].order_status == 2 && result.orderInfo[0].delivery_method == 1 }">待发货</c:if>
						<c:if test="${result.orderInfo[0].order_status == 2 && result.orderInfo[0].delivery_method == 2}">待提货</c:if>
						<c:if test="${result.orderInfo[0].order_status == 3}">已发货</c:if>
						<c:if test="${result.orderInfo[0].order_status == 4}">订单完成</c:if>
						<c:if test="${result.orderInfo[0].order_status == 5}">订单关闭</c:if>
						<c:if test="${result.orderInfo[0].order_status == 6}">退款中</c:if>
					</label>
				</div>
				<div>卖家备注：${result.orderInfo[0].order_seller_remark}</div>
			</div>
		</div>
		<div class="ui-box">
		    <table class="ui-table-order" style="padding: 0px;">
		      <thead class="" style="position:static; top: 0px; margin-top: 0px; left: 324.5px; z-index: 1; width: 849px;">
		        <tr class="widget-list-header">
			        <th class="" colspan="2" style="min-width: 224px; max-width: 224px;">商品名称</th>
					<th class="price-cell" style="min-width: 90px; max-width: 90px;">原价</th>
					<th class="price-cell" style="min-width: 45px; max-width: 45px;">数量</th>
					<th class="aftermarket-cell" style="min-width: 85px; max-width: 85px;">优惠</th>
					<th class="customer-cell" style="min-width: 110px; max-width: 110px;">小计</th>
					<th class="time-cell" style="min-width: 80px; max-width: 80px;">状态</th>
					<th class="state-cell" style="min-width: 100px; max-width: 100px;">运费</th>
				</tr>
			 </thead>
			 <c:set var="orderDetailStatus" value="-3"></c:set>
			 <c:if test="${result.orderInfo[0].order_pay_way != 5 }">
			 <c:forEach var="order" items="${result.orderDetail }">
					<c:if test="${order.status == 1 || order.status == 5 }">
						<c:set var="orderDetailStatus" value="1"></c:set>
					</c:if>
				 <tbody class="widget-list-item">
				 <tr class="separation-row">
				    <td colspan="8"></td>
				 </tr>
			    <tr class="content-row">
			        <td class="image-cell">
			            <img src="${path }${order.product_image_url }">
			        </td>
			        <td class="title-cell">
			            <p class="goods-title">
			               <a href="javascript:void(0);" class="new-window" title="${order.det_pro_name }">
			                   ${order.det_pro_name }
			               </a>
			            </p>
			            <p>
			               <span class="goods-sku">${order.proSpec }</span>
			            </p>
			        </td>
			        <td class="price-cell">
			            <p>${order.det_privivilege }
			            	<c:if test="${order.det_privivilege != null && order.det_privivilege != '' }">
			            		元
			            	</c:if>
			            </p>
			        </td>
			        <td class="price-cell">
			            <p>${order.det_pro_num }</p>
			        </td>
			        <td class="price-cell">
			            <p>
			            	<c:if test="${order.discount == 100 }">0</c:if>
			            	<c:if test="${order.discount < 100 }">${(order.discount*order.det_pro_price*order.det_pro_num)/100}</c:if>
			            	<c:if test="${order.det_pro_price != null && order.det_pro_price != '' && order.discount < 100}">
			            		<c:if test="${result.orderInfo[0].order_pay_way != 4 && result.orderInfo[0].order_pay_way != 8}">元</c:if>
			            		<c:if test="${result.orderInfo[0].order_pay_way == 4 }">积分</c:if>
			            		<c:if test="${result.orderInfo[0].order_pay_way == 8 }">粉币</c:if>
			            	</c:if>
			            </p>
			        </td>
			        <td class="customer-cell" rowspan="1">
			            ${(order.det_pro_price*order.det_pro_num)}
			            <c:if test="${order.det_pro_price != null && order.det_pro_price != ''}">
		            		<c:if test="${result.orderInfo[0].order_pay_way != 4 && result.orderInfo[0].order_pay_way != 8}">元</c:if>
		            		<c:if test="${result.orderInfo[0].order_pay_way == 4 }">积分</c:if>
		            		<c:if test="${result.orderInfo[0].order_pay_way == 8 }">粉币</c:if>
		            	</c:if>
			        </td>
			        <td class="customer-cell" rowspan="1">
			            <c:if test="${result.orderInfo[0].order_status == 1}">等付款</c:if>
						<c:if test="${result.orderInfo[0].order_status == 2}">待发货</c:if>
						<c:if test="${result.orderInfo[0].order_status == 3}">已发货</c:if>
						<c:if test="${result.orderInfo[0].order_status == 4}">订单完成</c:if>
						<c:if test="${result.orderInfo[0].order_status == 5}">订单关闭</c:if>
						<c:if test="${result.orderInfo[0].order_status == 6}">退款中</c:if>
			        </td>
			        <td class="time-cell" rowspan="1">
			        	<c:choose>
			        		<c:when test="${result.orderInfo[0].order_freight_money > 0}">
			        			${result.orderInfo[0].order_freight_money}
			            		<c:if test="${result.orderInfo[0].order_pay_way != 4 && result.orderInfo[0].order_pay_way != 8}">元</c:if>
			            		<c:if test="${result.orderInfo[0].order_pay_way == 4 }">积分</c:if>
			            		<c:if test="${result.orderInfo[0].order_pay_way == 8 }">粉币</c:if>
			        		</c:when>
			        		<c:otherwise>
			            		免运费
			            	</c:otherwise>
			            </c:choose>
			        </td>
			    </tr>
			</tbody>
		</c:forEach>
		</c:if>
		<c:if test="${result.orderInfo[0].order_pay_way == 5 }"><!-- 扫码支付 -->
			 <tbody class="widget-list-item">
				 <tr class="separation-row">
				    <td colspan="8"></td>
				 </tr>
			    <tr class="content-row">
			        <td class="image-cell"></td>
			        <td class="title-cell">
			            <p class="goods-title">扫码支付</p>
			        </td>
			        <td class="price-cell">
			            <p> ${result.orderInfo[0].order_money }</p>
			        </td>
			        <td class="price-cell">
			            <p>1</p>
			        </td>
			        <td class="price-cell">
			            <p>
			            	<c:if test="${order.discount == 100 }">0</c:if>
			            	<c:if test="${order.discount < 100 }">${(order.discount*result.orderInfo[0].order_money)/100}</c:if>
			            </p>
			        </td>
			        <td class="customer-cell" rowspan="1">
			            ${result.orderInfo[0].order_money }
			        </td>
			        <td class="customer-cell" rowspan="1">
			            <c:if test="${result.orderInfo[0].order_status == 1}">等付款</c:if>
						<c:if test="${result.orderInfo[0].order_status == 2}">已付款</c:if>
						<c:if test="${result.orderInfo[0].order_status == 4}">订单完成</c:if>
						<c:if test="${result.orderInfo[0].order_status == 5}">订单关闭</c:if>
			        </td>
			        <td class="time-cell" rowspan="1">
			        </td>
			    </tr>
			</tbody>
		</c:if>
	</table>
	<div class="detail_footer">
		<div>应收总额：<label>￥${result.orderInfo[0].order_money }</label>
         		<c:if test="${result.orderInfo[0].order_pay_way != 4 && result.orderInfo[0].order_pay_way != 8}">元</c:if>
         		<c:if test="${result.orderInfo[0].order_pay_way == 4 }">积分</c:if>
         		<c:if test="${result.orderInfo[0].order_pay_way == 8 }">粉币</c:if>
      	</div>
	</div>
</div>
</div>
</div>
</body>
<script type="text/javascript">
$(function(){
	var status = "${result.orderInfo[0].order_status}";
	var detailStatus = "${orderDetailStatus}";
	if(status == 1){
		
	}else if(status == 2){
		$('#sflex03').find("#two").removeClass("last");
		$('#sflex03').find("#one").removeClass("doing");
		$('#sflex03').find("#one").addClass("done");
		$('#sflex03').find("#two").addClass("doing");
	}else if(status == 3){
		$('#sflex03').find("#two").removeClass("doing");
		$('#sflex03').find("#three").removeClass("last");
		$('#sflex03').find("#one").addClass("done");
		$('#sflex03').find("#two").addClass("done");
		$('#sflex03').find("#three").addClass("doing");
	}else if(status == 4){
		$('#sflex03').find("#one").addClass("done");
		$('#sflex03').find("#two").addClass("done");
		$('#sflex03').find("#three").removeClass("doing");
		$('#sflex03').find("#four").removeClass("last");
		$('#sflex03').find("#three").addClass("done");
		$('#sflex03').find("#four").addClass("doing");
	}else if(status == 5){
		$('#sflex03').find("#one").addClass("done");
		
		$('#sflex03').find("#two").hide();
		$('#sflex03').find("#three").hide();
		$('#sflex03').find("#four").hide();
		$('#sflex03').find("#five").show();
		$('#sflex03').find("#five").addClass("done");
	}else {
		$('#sflex03').find("#one").addClass("done");
		$('#sflex03').find("#two").addClass("done");
		$('#sflex03').find("#three").addClass("done");
		$('#sflex03').find("#four").removeClass("doing");
		$('#sflex03').find("#four").addClass("done");
	}
});

</script>
</html>