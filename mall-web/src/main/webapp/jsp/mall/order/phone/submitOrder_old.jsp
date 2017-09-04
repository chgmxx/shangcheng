
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
   <link rel="stylesheet" href="/css/common/init.css"/>
</head>
<body>
<!--加载动画-->
	<section class="loading">
		<div class="load3">
			<div class="double-bounce1"></div>
			<div class="double-bounce2"></div>
		</div>
	</section>
   <link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
   <link rel="stylesheet" type="text/css" href="/css/mall/phoneOrder.css"/>
   <link rel="stylesheet" type="text/css" href="/js/plugin/layer-mobile/layer/need/layer.css"/>
   <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
   <script src="/js/plugin/layer-mobile/layer/layer.js"></script>
	<script type="text/javascript" src="/js/common/phone.js"></script>
	<script type="text/javascript" src="/js/plugin/gt/js/gt_common.js"></script>
	
<div class="header_back">
	<span class="btn_back" onclick="backUrl();">返回</span>
	<c:if test="${!empty pageId }">
	<span class="btn_go_home" onclick="pages(${pageId});">首页</span>
	</c:if>
</div>   
<c:set var="payWay" value=""></c:set>
<c:set var="orderPayWayName" value=""></c:set>
<c:if test="${isWxPay ==1}">
	<c:set var="payWay" value="1"></c:set>
	<c:set var="orderPayWayName" value="微信支付"></c:set>
</c:if>
<c:if test="${isWxPay ==0 && isAliPay ==1}">
	<c:set var="payWay" value="9"></c:set>
	<c:set var="orderPayWayName" value="支付宝支付"></c:set>
</c:if>
<c:if test="${!empty orderPayWays && orderPayWays != '' }">
	<c:set var="payWay" value="${orderPayWays }"></c:set>
</c:if>
<c:if test="${!empty payWayName && payWayName != '' }">
	<c:set var="orderPayWayName" value="${payWayName }"></c:set>
</c:if>
<c:if test="${empty deliveryMethod || deliveryMethod != 2 }">
	<c:set var="deliveryMethod" value="1"></c:set>
</c:if>
<c:if test="${!empty toshop }">
	<c:set var="deliveryMethod" value="3"></c:set>
</c:if>
<form action="phoneOrder/79B4DE7C/addOrder.do" method="post" id="orderForm">
	<input type="hidden" name="orderMoney" id="orderMoney" value="" />
	<input type="hidden" name="orderPayWay" id="orderPayWay" value="${payWay }" />
	<input type="hidden" name="orderOldMoney" id="orderOldMoney" value=""/>
	<input type="hidden" name="orderFreightMoney" id="orderFreightMoney" value=""/>
	<input type="hidden" name="type" id="type" value="${type }" />
	<input type="hidden" name="detail" id="detail" value="">
	<input type="hidden" name="freightMoney" id="freightMoney" value="">
	<input type="hidden" name="memCardType" id="memCardType" value="${memType }" />
	<!-- <input type="hidden" name="discountCoupon"  id="discountCoupon" />
	<input type="hidden" name="fullCoupon"  id="fullCoupon" /> -->
	<input type="hidden" name="sumCoupon" id="sumCoupon" />
	<input type="hidden" name="deliveryMethod" id="deliveryMethod" value="${deliveryMethod}" />
	<input type="hidden" name="appointTime" id="appointTime" value="${mallTakeTheir.timeList[0].times}" />
	<input type="hidden" name="appStartTime" id="appStartTime" value="${mallTakeTheir.timeList[0].startTime}" />
	<input type="hidden" name="appEndTime" id="appEndTime" value="${mallTakeTheir.timeList[0].endTime}" />
	<input type="hidden" name="appointTel" id="appointTel"/>
	<input type="hidden" name="couponList" id="couponList" />
	<input type="hidden" name="isJifen" id="isJifen" value="0">
	<input type="hidden" name="isFenbi" id="isFenbi" value="0">
	<input type="hidden" name="sumJifen" id="sumJifen"><!-- 能使用积分的商品总价 -->
	<input type="hidden" name="sumFenbi" id="sumFenbi"><!-- 能使用粉币的商品总价 -->
	<input type="hidden" name="yhqNum" id="yhqNum"><!-- 能使用优惠券的商品总数 -->
	<input type="hidden" name="countJifen" id="countJifen"><!-- 积分总共优惠的总数 -->
	<input type="hidden" name="countFenbi" id="countFenbi"><!-- 粉币总共优惠的总数 -->
	<input type="hidden" name="countYhq" id="countYhq"><!-- 优惠券总共优惠的总数 -->
	<input type="hidden" name="countUnion" id="countUnion"/><!-- 联盟优惠总共优惠的总数 -->
	<input type="hidden" name="unionYouhuiList" class="unionYouhuiList" value=""/><!-- 保存每个店铺优惠了多少钱 -->
	<input type="hidden" name="flowPhone" class="flowCzPhone" value="${flowPhone }"/><!-- 流量充值手机号 -->
	<input type="hidden" name="prizeCount" class="prizeCount" value="${flowType }"/>
	
	<c:if test="${!empty toshop }">
	<input type="hidden" name="toshop" class="toshop" value="${toshop }"/>
	</c:if>
	<c:set var="pro_type_id" value="0"></c:set>
	<c:set var="url" value=""></c:set>
	<c:if test="${type == 0}">
		<c:set var="pro_type_id" value="${orderDetail[0].pro_type_id}"></c:set>
	</c:if>
	<c:if test="${type == 1}">
		<c:set var="url" value="/mallPage/79B4DE7C/shoppingcare.do?member_id=${member.id }&uId=${userid }"></c:set>
		<c:set var="pro_type_id" value="${orderDetail[0].message[0].pro_type_id}"></c:set>
	</c:if>
	<c:set var="isKm" value="0"></c:set>
	<c:if test="${!empty isKm }">
		<c:set var="isKm" value="${isKm }"></c:set>
	</c:if>
  <div class="Warp" style="padding-bottom: 100px;">
  <c:if test="${pro_type_id == 0 && empty toshop}">
  	<section class="delivery-info" id="addressDiv">
      	<c:if test="${! empty address}">
	  		<div>
	  			<input type="hidden" class="mem_latitude" value="${address.mem_latitude }"/>
	  			<input type="hidden" class="mem_longitude" value="${address.mem_longitude }"/>
		  		<input type="hidden" name="receiveId" id="receiveId" value="${address.id }" />
	  			<div onclick="toAddress();">
			  		<div class="left_div">
			  			<div class="info info1">
				  			<div class="">
				  				收件人：<label for="" class="name">${address.mem_name }</label>
				  			</div>
				  			<div class="phone">
				  				${address.mem_phone }
				  			</div>
				  		</div>
				  		<div class="info info2">
				  			${address.pName}${address.cName}${address.aName}${address.mem_address }
							<c:if test="${address.mem_zip_code != null && address.mem_zip_code != ''}">
								(${address.mem_zip_code })
							</c:if>
				  		</div>
			  		</div>
		  			<div class="right_div"></div>
		  		</div>
	  		</div>
	  	</c:if>
	  	<c:if test="${empty address }">
  			<span onclick="selectAddress();" class="addre_span">请选择地址</span>
	  	</c:if>
 </section>
 </c:if>
 <input type="hidden" value="${isKm }" class="isKm"/>
 <c:set var="fenbiNum" value="0"></c:set>
 <c:set var="fenbiProMoney" value="0"></c:set>
 <c:set var="jifenNum" value="0"></c:set>
 <c:set var="jifenProMoney" value="0"></c:set>
  	<section class="main">
  	<c:if test="${type == 1 }">
  		<c:forEach var="order" items="${orderDetail }">
		<c:set var="yhqNum" value="0"></c:set>
		<c:set var="totalMoneys" value="0"></c:set>
  		<div id="couponDiv" class="couponDivs" stoId="${order.message[0].shop_id }">
  			<h2 class="store-name">${order.shop_name }</h2>
  			<c:forEach var="orderDetail" items="${order.message }">
				<c:set var="totalOrderMoneys" value="0"></c:set>
  				<input type="hidden" name="return_day" value="${orderDetail.return_day }"/>
		   	    <div class="mall-item" groupType="${orderDetail.groupType }">
		   	    	<c:if test="${orderDetail.groupType == 4 }">
		   	    		<c:set var="url" value="/mAuction/${orderDetail.product_id }/${orderDetail.shop_id }/${orderDetail.groupBuyId }/79B4DE7C/auctiondetail.do"></c:set>
		   	    	</c:if>
	   	    		<c:set var="price" value="${orderDetail.primary_price }"></c:set>
	   	    		<c:if test="${orderDetail.is_member_discount == 1 }"><!-- 参加会员折扣 -->
	   	    			<c:set var="price" value="${orderDetail.price }"></c:set>
	   	    		</c:if>
	   	    		<c:if test="${orderDetail.isCoupons == 1 && orderDetail.is_member_discount == 1 }"><!-- 参加会员折扣，优惠券 -->
	   	    			<c:set var="yhqNum" value="${yhqNum+1 }"></c:set>
	   	    		</c:if>
	   	    		<c:if test="${orderDetail.isCoupons == 1 && orderDetail.is_member_discount == 0}"><!-- 参加会员折扣，不参加优惠券 -->
	   	    			<c:set var="yhqNum" value="${yhqNum+1 }"></c:set>
	   	    		</c:if>
	   	    		<c:if test="${empty orderDetail.groupType }">
						<c:set var="totalMoneys" value="${totalMoneys+price*orderDetail.product_num }"></c:set>
					</c:if>
					<c:if test="${orderDetail.groupType == 7}">
						<c:if test="${!empty orderDetail.specMap }">
							<c:forEach var="spec" items="${orderDetail.specMap }">
								<c:set var="specVal" value="${spec.value }"></c:set>
								<c:set var="totalMoneys" value="${totalMoneys+specVal.price*specVal.num }"></c:set>
								<c:set var="totalOrderMoneys" value="${totalOrderMoneys+specVal.price*specVal.num }"></c:set>
								<c:if test="${orderDetail.is_integral_deduction == 1 }">
									<c:set var="jifenProMoney" value="${jifenProMoney+(specVal.price*specVal.num) }"></c:set>
								</c:if>
								<c:if test="${orderDetail.is_fenbi_deduction == 1 }">
			 						<c:set var="fenbiProMoney" value="${fenbiProMoney+(specVal.price*specVal.num) }"></c:set>
				   	    		</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${empty orderDetail.specMap }">
							<c:set var="totalMoneys" value="${totalMoneys+orderDetail.price*orderDetail.product_num }"></c:set>
							<c:set var="totalOrderMoneys" value="${totalOrderMoneys+orderDetail.price*orderDetail.product_num }"></c:set>
						</c:if>
					</c:if>
		   	    	<input type="hidden" name="isCoupons" id="isCoupons" value="${orderDetail.isCoupons }" />
		   	    	<input type="hidden" name="primary_price" id="primary_price" value="${orderDetail.primary_price }" />
		   	    	<input type="hidden" name="is_member_discount" id="is_member_discount" value="${orderDetail.is_member_discount }" />
					<input type="hidden" name="integralDeduction" id="integralDeduction" value="${orderDetail.is_integral_deduction }" />
	   	    		<input type="hidden" name="fenbiDeduction" id="fenbiDeduction" value="${orderDetail.is_fenbi_deduction }" />
	   	    		<input type="hidden" id="detailProPrice" value="${price }" /><!-- 价格不能改 -->
	   	    		<input type="hidden" id="proPrice" value="${price}" />
	   	    		<c:if test="${orderDetail.is_integral_deduction == 1 }">
	 					<c:set var="jifenNum" value="${jifenNum+1 }"></c:set>
	 					<c:if test="${orderDetail.groupType != 7 }">
 							<c:set var="jifenProMoney" value="${jifenProMoney+(price*orderDetail.product_num) }"></c:set>
 						</c:if>
	   	    		</c:if>
	   	    		<c:if test="${orderDetail.is_fenbi_deduction == 1 }">
		   	    		<c:set var="fenbiNum" value="${fenbiNum+1 }"></c:set>
		   	    		<c:if test="${orderDetail.groupType != 7 }">
 						<c:set var="fenbiProMoney" value="${fenbiProMoney+(price*orderDetail.product_num) }"></c:set>
 						</c:if>
	   	    		</c:if>
					<div class="mall-img">
		  				<img src="${path}${orderDetail.image_url }"/>
					</div>
					<div class="mall-info">
						<c:set var="urls" value="mallPage/${orderDetail.product_id }/${orderDetail.shop_id }/79B4DE7C/phoneProduct.do"></c:set>
						<c:if test="${!empty orderDetail.saleMemberId}">
							<c:if test="${orderDetail.saleMemberId > 0 }">
								<c:set var="urls" value="${urls }?saleMemberId=${orderDetail.saleMemberId }"></c:set>
							</c:if>
						</c:if>
						<p>
							<a href="${urls }">
							${orderDetail.pro_name }</a>
						</p>
						<c:if test="${orderDetail.groupType != 7}">
						${orderDetail.product_speciname }
						</c:if>
						<p class="red-txt"><em>￥</em><span id="singlePrice">${orderDetail.primary_price }</span></p>
						<c:if test="${!empty isDiscount && orderDetail.groupType != 7}">
						<p class="red-txt">会员价：<em id="proPrice">${price}</em></p>
						</c:if>
						
						<c:if test="${orderDetail.groupType == 7 && empty orderDetail.specMap}"><!-- 批发 无规格-->
							<p class="red-txt">批发价：<span id="proPrice">${orderDetail.price }</span></p>
							<c:set var="orderTotalPrice" value="${orderTotalPrice+orderDetail.price*orderDetail.product_num }"></c:set>
						</c:if>
						
						<p>
							<span>数量 <span id="singleNum">${orderDetail.product_num } </span></span>
						</p>
					</div>
					<input type="hidden" class="totalOrderMoneys" value="${totalOrderMoneys }"/>
				</div>
				<c:if test="${orderDetail.groupType == 7}">
						<c:if test="${!empty orderDetail.specMap }">
							<c:forEach var="spec" items="${orderDetail.specMap }">
								<c:set var="specVal" value="${spec.value }"></c:set>
								<div class="mall-spec clearfix">
									<div class="f_l">
										<p>
											<span class="cg_1">${specVal.specName }：</span><span>${specVal.value }</span>
										</p>
										<p>
											<span class="cg_1">数量：</span><span>X ${specVal.num }</span>
										</p>
										<p>
										</p>
									</div>
									<div class="f_r red-txt">
										<p>批发价：${specVal.price }元</p>
									</div>
								</div>
								
							</c:forEach>
						</c:if>
					</c:if>
			</c:forEach>
			<c:if test="${empty order.coupon}">
				<c:set var="yhqNum" value="0"></c:set>
			</c:if>
  	    	<input type="hidden" id="yhqNum" value="${yhqNum }">
  	    	<input type="hidden" id="yhqSumMoney" value="">
  	    	
			<input type="hidden" class="unionNum" value="${order.product_nums }" />
			<input type="hidden" class="unionMoney" name="unionMoney" value="${totalMoneys }">
		<div>
			<c:if test="${!empty order.coupon || !empty order.duofenCoupon}">
			  	<div class="pay-way couponDiv" onclick="showLay('coupon',this)">
					<lable class="text-left">使用优惠券</lable>
					<span class="text-right right_img" style="float:right;margin-left:10px;"></span>
					<span class="text-right useCoupon" id="useCoupon" style="float:right"></span>
				</div>
			</c:if>
		</div>
		
		<div class="remark">
			<label for="">备注：</label>
			<input type="text" name="orderBuyerMessage${order.message[0].shop_id }" id="remark-txt" class="remark-txt" placeholder="请填写备注信息" maxlength="100" />
		</div>
		<div class="sum-info">
			<span class="num-box">
				共<i id="num">${order.product_nums }</i>件商品
			</span>
			<span class="num-box shopTotal" style="width: 300px;padding-right: 20px;">
				合计：<span class="sum" style="color:#f20000;">￥
						<i>${totalMoneys }</i>
					 </span>
				<input type="hidden" name="primaryPrice" class="primaryPrice" value="${order.primary_price}" />
				<input type="hidden" class="orderCountMoney" value="${order.price_total }"/>
			</span>
			<span class="sum-box shopShipment" sId="${order.message[0].shop_id }">
				运费：
				<span style="color:#f20000;">
				<c:forEach var="shipment" items="${priceMap }">
					<c:if test="${order.message[0].shop_id == shipment.key }">${shipment.value}</c:if>
				</c:forEach>元
				</span>
			</span>
			<input type="hidden" class="shopId" value="${order.message[0].shop_id }"/>
			<input type="hidden" class="proNum" value="${order.product_nums }"/>
			<input type="hidden" class="priceTotal" value="${order.price_total }"/>
			<input type="hidden" id="proTypeId" value="${order.message[0].pro_type_id }" />
		</div>
		<c:if test="${!empty order.coupon || !empty order.duofenCoupon}">
		<div class="pay-layer" id="coupon">
		     <div class="lay-item">优惠券
		       <i class="delete" onclick="hideLay()"></i>
		     </div>
		     <c:forEach var="dfCoupon" items="${order.duofenCoupon}">
			 	<div class="lay-item duofenCouponDiv" onclick="coupon(this,1);">
			 		<input type="hidden" id="cardCode" value="${dfCoupon.code }" />
			 		<input type="hidden" id="couponType" value="${dfCoupon.card_type }" />
			     	<img src="${path}${dfCoupon.image }" width="100px;" height="65px;" />
			     	<input type="hidden" id="addUser" value="${dfCoupon.addUser }" />
			     	<input type="hidden" id="cId" value="${dfCoupon.cId }" />
			     	<c:if test="${dfCoupon.card_type == 1 }">
			     		<label for="" id="couponExplain">满${dfCoupon.cash_least_cost }减${dfCoupon.reduce_cost }元</label>
			     		<input type="hidden" id="cashLeastCost" value="${dfCoupon.cash_least_cost }" />
			     		<input type="hidden" id="cash" value="${dfCoupon.reduce_cost }" />
			     		<input type="hidden" id="gId" value="${dfCoupon.gId }" />
			     		<c:if test="${dfCoupon.addUser == 1}">
			     			<span style="padding-left: 10px;">
			     				x<span id="couponNum">${dfCoupon.countId }</span>
			     			</span>
			     		</c:if>
			     	</c:if>
			     	<c:if test="${dfCoupon.card_type == 0}">
			     		<label for="" id="couponExplain">${dfCoupon.discount }折</label>
			     		<input type="hidden" id="discount" value="${dfCoupon.discount }" />
			     	</c:if>
			     </div>
			 </c:forEach>
			 <c:forEach var="coupon" items="${order.coupon}">
			 	<div class="lay-item" onclick="coupon(this,1);">
			 		<input type="hidden" id="cardCode" value="${coupon.user_card_code }" />
			 		<input type="hidden" id="couponType" value="${coupon.card_type }" />
			     	<img src="${coupon.image }" width="100px;" height="65px;" />
			     	<c:if test="${coupon.card_type == 'DISCOUNT' }">
			     		<label for="" id="couponExplain">${coupon.discount }折</label>
			     		<input type="hidden" id="discount" value="${coupon.discount }" />
			     	</c:if>
			     	<c:if test="${coupon.card_type == 'CASH' }">
			     		<label for="" id="couponExplain">满${coupon.cash_least_cost }减${coupon.reduce_cost }元</label>
			     		<input type="hidden" id="cashLeastCost" value="${coupon.cash_least_cost }" />
			     		<input type="hidden" id="cash" value="${coupon.reduce_cost }" />
			     	</c:if>
			     </div>
			 </c:forEach>
			 <div class="lay-item" onclick="coupon(this,0);">
		 		<span>取消使用优惠券</span>
		     </div>
		</div>
		</c:if>
	</div>
	</c:forEach>
</c:if>
<c:set var="groupTypes" value="0"></c:set>
	<c:if test="${type == 0 }">
		<c:set var="yhqNum" value="0"></c:set>
		<c:forEach var="orderDetails" items="${orderDetail }" varStatus="i">
			<c:if test="${i.index == 0 }">
				<c:set var="url" value="/mallPage/${orderDetails.product_id }/${orderDetails.shop_id }/79B4DE7C/phoneProduct.do"></c:set>
			</c:if>
			<c:set var="orderTotalPrice" value="0"></c:set>
			<div id="couponDiv" class="couponDivs"  stoId="${orderDetails.shop_id }">
			<input type="hidden" name="return_day" value="${orderDetails.return_day }"/>
	   	    <div class="mall-item" groupType="${orderDetails.groupType  }">
	   	    	<c:if test="${orderDetails.groupType == 4 }">
	   	    		<c:set var="url" value="/mAuction/${orderDetails.product_id }/${orderDetails.shop_id }/${orderDetails.groupBuyId }/79B4DE7C/auctiondetail.do"></c:set>
	   	    	</c:if>
	   	    	<c:set var="price" value="${orderDetails.primary_price }"></c:set>
	   	    	<c:if test="${orderDetails.is_member_discount == 1 }"><!-- 参加会员折扣 -->
   	    			<c:set var="price" value="${orderDetails.price }"></c:set>
   	    		</c:if>
   	    		<c:if test="${orderDetails.isCoupons == 1 && orderDetails.is_member_discount == 1 }">
   	    			<c:set var="price" value="${orderDetails.price }"></c:set>
 					<c:set var="yhqNum" value="${yhqNum+1 }"></c:set>
   	    		</c:if>
   	    		<c:if test="${orderDetails.isCoupons == 1 && orderDetails.is_member_discount == 0 }">
   	    			<c:set var="yhqNum" value="${yhqNum+1 }"></c:set>
   	    		</c:if>
	   	    	<input type="hidden" name="isCoupons" id="isCoupons" value="${orderDetails.isCoupons }" />
	   	    	<input type="hidden" name="primary_price" id="primary_price" value="${orderDetails.primary_price }" />
	   	    	<input type="hidden" name="is_member_discount" id="is_member_discount" value="${orderDetails.is_member_discount }" />
	   	    	<input type="hidden" name="integralDeduction" id="integralDeduction" value="${orderDetails.is_integral_deduction }" />
	   	    	<input type="hidden" name="fenbiDeduction" id="fenbiDeduction" value="${orderDetails.is_fenbi_deduction }" />
	   	    	<input type="hidden" id="detailProPrice" value="${price }" />
	   	    	<input type="hidden" id="proPrice" value="${price }" />
	   	    	<c:if test="${orderDetails.is_integral_deduction == 1 }">
 					<c:set var="jifenNum" value="${jifenNum+1 }"></c:set>
 					<c:if test="${orderDetails.groupType != 7 }">
					<c:set var="jifenProMoney" value="${jifenProMoney+(price*orderDetails.totalnum) }"></c:set>
					</c:if>
   	    		</c:if>
   	    		<c:if test="${orderDetails.is_fenbi_deduction == 1 }">
	   	    		<c:set var="fenbiNum" value="${fenbiNum+1 }"></c:set>
	   	    		<c:if test="${orderDetails.groupType != 7 }">
					<c:set var="fenbiProMoney" value="${fenbiProMoney+(price*orderDetails.totalnum) }"></c:set>
					</c:if>
   	    		</c:if>
				<div class="mall-img">
	  				<img src="${path}${orderDetails.image_url }"/>
				</div>
				<div class="mall-info">
					<c:set var="urls" value="/mallPage/${orderDetails.product_id }/${orderDetails.shop_id }/79B4DE7C/phoneProduct.do"></c:set>
					<c:if test="${!empty orderDetails.saleMemberId}">
						<c:if test="${orderDetails.saleMemberId > 0 }">
							<c:set var="urls" value="${urls }?saleMemberId=${orderDetails.saleMemberId }"></c:set>
						</c:if>
					</c:if>
					<p><a href="${urls }">
					${orderDetails.product_name }</a></p>
					<c:if test="${orderDetails.groupType != 7}">
						<c:if test="${!empty orderDetails.product_speciname }">
						${orderDetails.product_speciname }
						</c:if>
					</c:if>
					<c:if test="${orderDetails.groupType != 2 && orderDetails.groupType != 5}"><!-- 普通购买 -->
						<p class="red-txt"><em>￥</em><span id="singlePrice">${orderDetails.primary_price }</span></p>
						
						<c:if test="${orderDetails.groupType == 7 && empty orderDetails.pro_spec_str}"><!-- 批发 无规格-->
							<p class="red-txt">批发价：<span id="proPrice">${orderDetails.price }</span></p>
							<c:set var="orderTotalPrice" value="${orderTotalPrice+orderDetails.price*orderDetails.totalnum }"></c:set>
						</c:if>
						<c:if test="${orderDetails.groupType == 7 && !empty orderDetails.pro_spec_str}">
							<c:forEach var="spec" items="${orderDetails.pro_spec_str }">
								<c:set var="specVal" value="${spec.value }"></c:set>
								<c:set var="orderTotalPrice" value="${orderTotalPrice+specVal.price*specVal.num }"></c:set>
								<c:if test="${orderDetails.is_integral_deduction == 1 }">
									<c:set var="jifenProMoney" value="${jifenProMoney+(specVal.price*specVal.num) }"></c:set>
								</c:if>
								<c:if test="${orderDetails.is_fenbi_deduction == 1 }">
									<c:set var="fenbiProMoney" value="${fenbiProMoney+(specVal.price*specVal.num) }"></c:set>
				   	    		</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${orderDetails.groupType != 7}">
							<c:if test="${!empty isDiscount && orderDetails.groupType == 0 && price != orderDetails.primary_price*1}">
							<p class="red-txt">会员价：<em id="proPrice">${price}</em></p>
							</c:if>
							<c:if test="${orderDetails.groupType == 1 || orderDetails.groupType == 3  || orderDetails.groupType == 4 }">
							<p class="red-txt">
								<c:if test="${orderDetails.groupType == 1}">团购价：</c:if>
								<c:if test="${orderDetails.groupType == 3}">秒杀价：</c:if>
								<c:if test="${orderDetails.groupType == 4}">拍卖价：</c:if>
								<em id="proPrice">${orderDetails.price}</em>
							</p>
							</c:if>
						</c:if>
					</c:if>
					<c:if test="${orderDetails.groupType == 2}"><!-- 积分购买 -->
					<p class="red-txt">${orderDetails.price }<em>积分</em></p>
					</c:if>
					<c:if test="${orderDetails.groupType == 5}"><!-- 粉币购买 -->
					<p class="red-txt">${orderDetails.price }<em>粉币</em></p>
					</c:if>
					<c:set var="groupTypes" value="${orderDetails.groupType }"></c:set>
					<p>
						<span>数量 <span id="singleNum">${orderDetails.totalnum }</span></span>
					</p>
				</div>
				
  	    		<input type="hidden" class="totalOrderMoneys" value="${orderTotalPrice }">
			</div>
			<c:if test="${orderDetails.groupType == 7}">
				<c:if test="${!empty orderDetails.pro_spec_str }">
					<c:forEach var="spec" items="${orderDetails.pro_spec_str }">
						<c:set var="specVal" value="${spec.value }"></c:set>
						<div class="mall-spec clearfix">
							<div class="f_l">
								<p>
									<span class="cg_1">${specVal.specName }：</span><span>${specVal.value }</span>
								</p>
								<p>
									<span class="cg_1">数量：</span><span>X ${specVal.num }</span>
								</p>
								<p>
								</p>
							</div>
							<div class="f_r red-txt">
								<p>批发价：${specVal.price }元</p>
							</div>
						</div>
						
					</c:forEach>
				</c:if>
			</c:if>
			<div>
				<c:if test="${!empty coupon || !empty duofenCoupon}">
				  	<div class="pay-way couponDiv" onclick="showLay('coupon',this)">
						<lable class="text-left">使用优惠券</lable>
						<span class="text-right right_img" style="float:right;margin-left:10px;"></span>
						<span class="text-right" id="useCoupon" style="float:right"></span>
					</div>
				</c:if>
			</div>
			<div class="remark">
				<label for="">备注：</label>
				<input type="text" name="orderBuyerMessage${orderDetails.shop_id }" id="remark-txt" class="remark-txt" placeholder="请填写备注信息" />
			</div>
			<div class="sum-info">
				<span id="" class="num-box">
					共<i id="num">${orderDetails.totalnum }</i>件商品
				</span>
				<span class="num-box shopTotal" style="width: 100px;padding-right: 20px;">
					合计:<span class="sum" style="color:#f20000;">
						<c:if test="${orderDetails.groupType != 2 && orderDetails.groupType != 5 && orderDetails.groupType != 7}"><!-- 普通购买 -->
						￥<i>${orderDetails.totalprice }</i>
						</c:if>
						<c:if test="${orderDetails.groupType == 2}"><!-- 积分购买 -->
						<i>${orderDetails.totalprice }</i>积分
						</c:if>
						<c:if test="${orderDetails.groupType == 5}"><!-- 粉币购买 -->
						<i>${orderDetails.totalprice }</i>粉币
						</c:if>
						<c:if test="${orderDetails.groupType == 7 }"><!-- 批发 -->
						￥<i>${orderTotalPrice }</i>
						</c:if>
					</span>
					<input type="hidden" name="primaryPrice" class="primaryPrice" value="${orderDetails.primary_price}" />
					<input type="hidden" class="orderCountMoney" value="${orderDetails.totalprice }"/>
				</span>
				<span class="sum-box shopShipment" sId="${orderDetails.shop_id }">
					<c:if test="${orderDetails.groupType != 2 && orderDetails.groupType != 5}"><!-- 普通购买 -->
						运费:<span style="color:#f20000;">
							<c:if test="${orderDetails.pro_type_id == 0 }">
								<c:forEach var="shipment" items="${priceMap }">
									<c:if test="${orderDetails.shop_id == shipment.key }">${shipment.value}</c:if>
								</c:forEach>元
							</c:if>
							<c:if test="${orderDetails.pro_type_id > 0 }">0</c:if><!-- 虚拟商品没有运费 -->
						</span>
					</c:if>
					<c:if test="${orderDetails.groupType == 2 || orderDetails.groupType == 5}"><!-- 积分和粉币购买没有运费 -->
						免运费
					</c:if>
				</span>
				<input type="hidden" class="groupBuyId" value="${orderDetails.groupBuyId }"/>
				<input type="hidden" class="groupType" value="${orderDetails.groupType }"/>
				<input type="hidden" class="shopId" value="${orderDetails.shop_id }"/>
				<input type="hidden" class="proNum" value="${orderDetails.totalnum }"/>
				<input type="hidden" class="priceTotal" value="${orderDetails.totalprice }"/>
			</div>
			<c:if test="${(!empty coupon && orderDetails.groupType != 2 && orderDetails.groupType != 5) || !empty duofenCoupon}">
			<div class="pay-layer" id="coupon">
			     <div class="lay-item">优惠券
			       <i class="delete" onclick="hideLay()"></i>
			     </div>
			     <c:forEach var="dfCoupon" items="${duofenCoupon}">
				 	<div class="lay-item duofenCouponDiv" onclick="coupon(this,1);">
				 		<input type="hidden" id="cardCode" value="${dfCoupon.code }" />
				 		<input type="hidden" id="couponType" value="${dfCoupon.card_type }" />
				 		<input type="hidden" id="addUser" value="${dfCoupon.addUser }" />
			     		<input type="hidden" id="cId" value="${dfCoupon.cId }" />
				     	<img src="${path}${dfCoupon.image }" width="100px;" height="65px;" />
				     	<c:if test="${dfCoupon.card_type == 1 }">
				     		<label for="" id="couponExplain">满${dfCoupon.cash_least_cost }减${dfCoupon.reduce_cost }元</label>
				     		<input type="hidden" id="cashLeastCost" value="${dfCoupon.cash_least_cost }" />
				     		<input type="hidden" id="cash" value="${dfCoupon.reduce_cost }" />
				     		<input type="hidden" id="gId" value="${dfCoupon.gId }" />
				     		<c:if test="${dfCoupon.addUser == 1}">
				     			<span style="padding-left: 10px;">
				     				x<span id="couponNum">${dfCoupon.countId }</span>
				     			</span>
				     		</c:if>
				     	</c:if>
				     	<c:if test="${dfCoupon.card_type == 0}">
				     		<label for="" id="couponExplain">${dfCoupon.discount }折</label>
				     		<input type="hidden" id="discount" value="${dfCoupon.discount }" />
				     	</c:if>
				     </div>
				 </c:forEach>
				 <c:forEach var="coupon" items="${coupon }">
				 	<div class="lay-item" onclick="coupon(this,1);">
				 		<input type="hidden" id="cardCode" value="${coupon.user_card_code }" />
				 		<input type="hidden" id="couponType" value="${coupon.card_type }" />
				     	<img src="${coupon.image }" width="100px;" height="65px;" />
				     	<c:if test="${coupon.card_type == 'DISCOUNT' }">
				     		<label for="" id="couponExplain">${coupon.discount }折</label>
				     		<input type="hidden" id="discount" value="${coupon.discount }" />
				     	</c:if>
				     	<c:if test="${coupon.card_type == 'CASH' }">
				     		<label for="" id="couponExplain">满${coupon.cash_least_cost }减${coupon.reduce_cost }元</label>
				     		<input type="hidden" id="cashLeastCost" value="${coupon.cash_least_cost }" />
				     		<input type="hidden" id="cash" value="${coupon.reduce_cost }" />
				     	</c:if>
				     </div>
				 </c:forEach>
				 <div class="lay-item" onclick="coupon(this,0);">
			 		<span>取消使用优惠券</span>
			     </div>
			</div>
			</c:if>
			<c:if test="${empty coupon || empty duofenCoupon}">
				<c:set var="yhqNum" value="0"></c:set>
			</c:if>
  	    	<input type="hidden" id="yhqNum" value="${yhqNum }">
  	    	<input type="hidden" id="yhqSumMoney" value="">
			<input type="hidden" class="unionNum"  value="${orderDetails.totalnum }" />
			<input type="hidden" class="unionMoney" name="unionMoney" value="${orderDetails.totalprice }">
			</div>
		</c:forEach>
		<input type="hidden" id="proTypeId" value="${orderDetail[0].pro_type_id }" />
	</c:if>
  	</section>
	
	<input type="hidden" class="jifenNum" name="jifenNum" value="${jifenNum }" />
	<input type="hidden" class="jifenProMoney" name="jifenProMoney" value="${jifenProMoney }">
	<input type="hidden" class="fenbiNum" name="fenbiNum" value="${fenbiNum }" />
	<input type="hidden" class="fenbiProMoney" name="fenbiProMoney" value="${fenbiProMoney }">
	<c:if test="${!empty unionMap }">
		<c:if test="${unionMap.status == 1 || unionMap.status == -2 }">
		
			<div class="pay-way" onclick="showUnionLayer()" >
				<lable class="text-left">联盟优惠</lable>
				<span class="text-right right_img" style="float:right;margin-left:10px;"></span>
				<span class="text-right" id="unionSpan" style="float:right">
					<c:if test="${unionMap.status == 1 && !empty unionMap.discount }">${unionMap.discount }折</c:if>
					<c:if test="${unionMap.status == -2 }">您还没绑定联盟卡</c:if>
				</span>
			</div>
		</c:if>
		<input type="hidden" class="unionStatus" value="${unionMap.status }"/>
		<c:if test="${unionMap.status == 1 }">
			<input type="hidden" class="cardId" value="${unionMap.cardId }"/>
			<input type="hidden" class="unionDiscount" value="${unionMap.discount }"/>
			<input type="hidden" class="union_id" value="${unionMap.union_id }"/>
			<%-- <input type="hidden" class="default" value="${unionMap.default }"/> --%>
		</c:if>
	</c:if>
	<c:if test="${isTakeTheir == 1 && pro_type_id == 0 || !empty toshop}">
		<div class="pay-way" onclick="showLay('deliveryWay',null)">
			<lable class="text-left">配送方式</lable>
			<span class="text-right right_img" style="float:right;margin-left:10px;"></span>
			
			<span class="text-right" id="delivery" style="float:right"><c:if test="${empty toshop }">快递配送</c:if><c:if test="${!empty toshop }">到店购买</c:if></span>
		</div>
	</c:if>
	<div class="pay-way" onclick="showLay('payLayer',null)">
		<lable class="text-left">支付方式</lable>
		<span class="text-right right_img" style="float:right;margin-left:10px;"></span>
		<span class="text-right paywaynames" id="onlinePayment" style="float:right">${orderPayWayName }</span>
	</div>
	<div class="pay-way pay-way-slide-btn"><!-- showLay('fenbiDiv',null) -->
			<input type="hidden" id="fenbi_money" name="fenbi_money" value="${map.fenbi_money }" />
			<input type="hidden" id="fenbi" name="fenbi" value="${map.fenbi }" />
			<c:set var="fenbiFlag" value="true"></c:set>
			<c:if test="${groupTypes == 2 || groupTypes == 5 }"><!-- 积分兑换和粉币兑换 -->
				<c:set var="fenbiFlag" value="false"></c:set>
			</c:if>
			<span>粉币</span><br/>
			<c:set var="isOpenFenbi" value="0"></c:set>
			<c:if test="${member.fansCurrency >= map.fenbiMap.item_value*10 && fenbiNum > 0 && fenbiFlag && !empty map}">
				<c:set var="isOpenFenbi" value="1"></c:set>
			</c:if>
			<c:if test="${!empty map }">
				<c:if test="${empty map.fenbi_money || empty map.fenbi}">
					<c:set var="isOpenFenbi" value="0"></c:set>
				</c:if>
			</c:if>
			<span class="fenbi_open_span" style="<c:if test="${isOpenFenbi == 0 }">display: none;</c:if>">
				<span class="pay-explan ">共${map.fenbi }粉币，可抵扣¥${map.fenbi_money }</span>
				<i onclick="selectFenbi(this);" class="fenbiyouhui"><img class="off"  src="/images/icon/off_icon.jpg" /><img class="on" src="/images/icon/on_icon.jpg" /></i>
			</span>
			<span class="fenbi_noopen_span" style="<c:if test="${isOpenFenbi == 1 }">display: none;</c:if>">
				<span class="pay-explan" style="color: #535353;">无可用</span>
				<i><img src="/images/icon/off_icon.jpg" /></i>
			</span>
			<%-- <lable class="text-left">使用${map.fenbi }粉币抵扣${map.fenbi_money }元
				<font style="margin-top: -50px;font-size: 90%;color: #ff8e33;">
					（${map.fenbiMap.item_value*10 }粉币起兑）
				</font>
			</lable>
			<!-- <span class="text-right" style="float:right;margin-left:10px;">></span>  -->
			<span class="text-right" style="float:right">
				<div class="circle1">
	   				<div class="circle2" style="display: none;" id="circle_fenbi"></div>
	   			</div>
			</span> --%>
		</div>
		<div class="pay-way pay-way-slide-btn" ><!-- showLay('jifenDiv',null) -->
			<input type="hidden" id="integral_money" name="integral_money" value="${map.integral_money }" />
			<input type="hidden" id="integral" name="integral" value="${map.integral }" />
			<input type="hidden" id="starttype" value="${map.paramSet.starttype }"/>
			<%-- <c:if test="${map.paramSet.starttype == 0}">
				<input type="hidden" id="jifenStartMoney" value="${map.paramSet.integralratio*map.paramSet.startmoney }" />
			  </c:if>
			<c:if test="${map.paramSet.starttype == 1}">
				<input type="hidden" id="orderStartMoney" value="${map.paramSet.startmoney }" />
			</c:if> --%>
			<input type="hidden" id="orderStartMoney" value="${map.paramSet.startmoney }" />
			<span>积分</span><br/>
			<c:set var="flag" value="false"></c:set>
			<!-- 积分启兑 -->
			<c:if test="${map.paramSet.starttype == 0 &&  member.integral > map.paramSet.integralratio*map.paramSet.startmoney}">
				<c:set var="flag" value="true"></c:set>
			</c:if>
			<!-- 订单启兑 -->
			<c:if test="${map.paramSet.starttype == 1 && jifenProMoney > map.paramSet.startmoney}">
				<c:set var="flag" value="true"></c:set>
			</c:if>
			<c:if test="${groupTypes == 2 || groupTypes == 5 }"><!-- 积分兑换和粉币兑换-->
				<c:set var="flag" value="false"></c:set>
			</c:if>
			<c:if test="${map.integral <= 0 }">
				<c:set var="flag" value="false"></c:set>
			</c:if>
			<c:set var="isOpenJifen" value="0"></c:set>
			<c:if test="${flag && jifenNum > 0 && !empty map}">
				<c:set var="isOpenJifen" value="1"></c:set>
			</c:if>
			<c:if test="${!empty map }">
				<c:if test="${empty map.integral || empty map.integral_money}">
					<c:set var="isOpenJifen" value="0"></c:set>
				</c:if>
			</c:if>
			<span class="jifen_open_span" style="<c:if test="${isOpenJifen == 0 }">display: none;</c:if>">
				<span class="pay-explan" style="color: #f20000;">共${map.integral }积分，可抵扣¥${map.integral_money }</span>
				<i onclick="selectJifen(${map.paramSet.starttype},this);" class="jifenyouhui">
					<img class="off" src="/images/icon/off_icon.jpg" /><img class="on" src="/images/icon/on_icon.jpg" />
				</i>
			</span>
			<span class="jifen_noopen_span" style="<c:if test="${isOpenJifen == 1 }">display: none;</c:if>">
				<span class="pay-explan" style="color: #535353;">无可用</span>
				<i><img src="/images/icon/off_icon.jpg" /></i>
			</span>
			
			<%-- <lable class="text-left">使用${map.intergral }积分抵扣${map.integral_money }元
				<font style="margin-top: -50px;font-size: 90%;color: #ff8e33;">
					（<c:if test="${map.paramSet.starttype == 0}">
						<span id="jifenStartMoney">${map.paramSet.integralratio*map.paramSet.startmoney }</span>积分起兑
					  </c:if>
					<c:if test="${map.paramSet.starttype == 1}">
						￥<span id="orderStartMoney">${map.paramSet.startmoney }</span>起兑
					</c:if>）
				</font>
			</lable> --%>
			
			<!-- <span class="text-right" style="float:right;margin-left:10px;">></span> -->
			<!-- <span class="text-right" style="float:right">
				<div class="circle1">
	   				<div class="circle2" style="display: none;" id="circle_jifen"></div>
	   			</div>
			</span> -->
		</div>
	<div class="delivery_way" style="display: none;" id="delivery_way">
		<ul>
			<li class="flex">
				<div>提货人：</div>
				<div><input type="text" name="appointName" id="appointName" placeholder="请填写提货人姓名"/></div>
			</li>
			<li class="flex">
				<div>手机号码：</div>
				<div><input type="tel" name="appointTelphone" id="appointTelphone"  placeholder="请填写提货人手机号码"
					maxlength="11" /></div>
			</li>
			<li class="flex" style="height: auto;">
				<div>提货地址：</div>
				<div style="line-height: 50px">
					<a href="javascript:void(0);" onclick="getTakeTheirs(${mallTakeTheir.id });">
						<!-- <input type="text" name="" id="" value="" placeholder="请填写商户提货地址"/> -->
						<input type="hidden" name="takeTheirId" value="${mallTakeTheir.id }" />
						<span id="deliveryAddress">${mallTakeTheir.visitAddressDetail }</span>
					</a>
				</div>
			</li>
			<li class="flex">
				<div>提货时间：</div>
				<div>
					<p class="delivery-time" id="delivery-time" onclick="showLay('timeLay',null)">
						<span id="deliveryTime">${mallTakeTheir.timeList[0].times } ${mallTakeTheir.timeList[0].startTime } - ${mallTakeTheir.timeList[0].endTime }
						</span></p>
				</div>
			</li>
		</ul>
	</div>

	<!-- <div class="other-pay">
		<label for="" class="text-left">找人买单</label>
		<div class="on-off">
			<a href="#" class="toggle toggle--off"></a>
		</div>
	</div> -->
	<div class="summary">
		<div class="money-box">
			<label for="">商品金额</label>
			<div class="red-txt">￥<span id="money"></span></div>
			<input type="hidden" id="proMoneyAll" value="0">
		</div>
		<div class="fare-box">
			<label for="">运费</label>
			<div class="red-txt">+ ￥<span id="fare"></span></div>
		</div>
		<div class="fare-box">
			<label for="">优惠券</label>
			<div class="red-txt">- ￥<span id="yhj">0.00</span></div>
		</div>
		<div class="fare-box">
			<label for="">积分</label>
			<div class="red-txt">- ￥<span id="jf">0.00</span></div>
		</div>
		<div class="fare-box">
			<label for="">粉币</label>
			<div class="red-txt">- ￥<span id="fb">0.00</span></div>
		</div>
		<c:if test="${!empty unionMap && unionMap.status == 1}">
		<div class="fare-box">
			<label for="">联盟优惠</label>
			<div class="red-txt">- ￥<span id="lm">0.00</span></div>
		</div>
		</c:if>
	</div>
	
	<footer class="footer">
	   <div class="footer-box">
	   	实付<span class="red-txt">￥<span id="sum-money"></span></span>
	   </div>
	   <div class="footer-box footer-right">
	   	 <!-- <input type="button" name="submit-order" id="submit-order" class="submit-order" value="提交订单" /> -->
	   	 <a href="javascript:void(0);" name="submit-order" id="submit-order" class="submit-order">提交订单</a>
	   </div>
	</footer>
	<input type="hidden" class="userid" value="${userid }"/>
</div>
</form>
<c:if test="${!empty unionMap }">
	<c:if test="${unionMap.status == -2 || unionMap.status == 1}">
<jsp:include page="/jsp/mall/order/layer/unionPhone.jsp"></jsp:include>
	</c:if>
</c:if>

<input type="hidden" class="isTakeTheir" value="${isTakeTheir }"/>
<input type="hidden" name="sumOldMoney" id="sumOldMoney" />
<form action="/phoneOrder/79B4DE7C/addressList.do?uId=${userid }" method="post" id="toAddList" name="toAddList">
	<input type="hidden" name="data" id="data" value="" />
	<input type="hidden" name="addressType" id="addressType" value="" />
	<input type="hidden" name="payWay" id="adresspay" />
	<input type="hidden" name="payWayName" id="adresspayName" />
</form>
<form action="/phoneOrder/79B4DE7C/getTakeTheir.do?uId=${userid }" method="post" id="getTakeTheir" name="getTakeTheir">
	<input type="hidden" name="datas" id="datas" value="" />
	<input type="hidden" name="id" id="id" value="" />
	<input type="hidden" name="deliveryType" id="deliveryType" />
	<input type="hidden" name="payWay" id="takePayWay" />
	<input type="hidden" name="payWayName" id="takePayWayName" />
	<input type="hidden" name="type" value="${type }" />
</form>
<!--遮罩层-->
<div class="fade" id="fade" onclick="hideLay()"></div>
<!--弹出层-->
<div class="pay-layer" id="payLayer">
     <div class="lay-item">支付方式
       <i class="delete" onclick="hideLay()"></i>
     </div>
     <c:if test="${isWxPay == 1 }">
     <div class="lay-item wxpay_div" onclick="payWay(1);" id="1">
     	<img src="/images/mall/pay-wx.png" width="52" height="52"/>
     	<label for="">微信支付</label>
     </div>
     </c:if>
     <c:if test="${isAliPay == 1}">
     <div class="lay-item aliPay_div" onclick="payWay(9);" id="9">
     	<img src="/images/mall/pay-ali.png" width="52" height="52"/>
     	<label for="">支付宝支付</label>
     </div>
     </c:if>
     <c:if test="${memType == 3 }">
	     <div class="lay-item czkpay_div" onclick="payWay(3);" id="3">
	     	<img src="images/mall/giftCard.jpg" width="52" height="52" />
	     	<label for="">储值卡支付</label>
	     </div>
     </c:if>
     <c:if test="${isHuoDao == 1 && pro_type_id == 0 }">
	     <div class="lay-item huodao" onclick="payWay(2);" id="2">
	     	<img src="images/mall/pay-cash.jpg" width="52" height="52"/>
	     	<label for="">货到付款</label>
	     </div>
     </c:if>
      <div class="lay-item jfDiv" onclick="payWay(4);" id="4" style="display:none;">
     	<img src="images/mall/pay-wx.jpg"/>
     	<label for="">积分支付</label>
     </div>
     <div class="lay-item fbDiv" onclick="payWay(8);" id="8" style="display:none;">
     	<img src="images/mall/pay-wx.jpg"/>
     	<label for="">粉币支付</label>
     </div>
     <c:if test="${!empty mallTakeTheir }">
     	<c:if test="${mallTakeTheir.isStorePay == 1 }">
     		<div class="lay-item storePayDiv" onclick="payWay(6);" id="6" style="display:none;">
		     	<img src="images/mall/pay-wx.jpg"/>
		     	<label for="">到店支付</label> 
		     </div>
     	</c:if>
     </c:if>
     <c:if test="${isDaifu == 1 && pro_type_id != 2 }">
	     <div class="lay-item huodao" onclick="payWay(7);" id="7">
	     	<img src="images/mall/pay-cash01.jpg" width="52" height="52"/>
	     	<label for="">找人代付</label>
	     </div>
     </c:if>
</div>
<div class="pay-layer" id="deliveryWay">
	<div class="lay-item">配送方式
	    <i class="delete" onclick="hideLay()"></i>
	</div>
	<c:if test="${!empty toshop }">
		<div class="lay-item" onclick="delivery(3);" id="daodianBuy">
			<img src="images/mall/pay-wx.jpg"/>
			<label for="">到店购买</label>
		</div>
	</c:if>
	<c:if test="${empty toshop }">
	<div class="lay-item" onclick="delivery(1);">
		<img src="images/mall/kuaidi.png"/>
		<label for="">快递配送</label>
	</div>
	</c:if>
	<c:if test="${pro_type_id == 0 && isTakeTheir == 1}">
		<div class="lay-item" onclick="delivery(2);" id="daodian">
			<img src="images/mall/dao.png"/>
			<label for="">到店自提</label>
		</div>
	</c:if>
</div>

<div class="lay timeLay" id="timeLay" style="overflow: scroll;max-height: 800px;min-height: 500px;">
   	<ul>
   		<c:forEach items="${mallTakeTheir.timeList }" var="time">
	   		<li class="flex" onclick="selectTime(this);">
	   			<div class="flex-1" id="times">${time.times }</div>
	   			<div class="flex-1"><span id="startTime">${time.startTime }</span> - <span id="endTime">${time.endTime }</span></div>
	   		</li>
   		</c:forEach>
   	</ul>
</div>
<div class="lay timeLay" id="jifenDiv" style="overflow: scroll;max-height: 800px;">
   	<ul>
   		<c:forEach var="jifen" items="${jifenList }">
   			<c:if test="${jifen > 0 }">
		   		<li class="flex" onclick="selectJifen(this);">
		   			<div class="flex-1" id="jifen">
		   				<span id="dhjf">${paramSet.integralratio*jifen }</span>积分抵扣<span id="dhjfMoney">${jifen }</span>元
		   			</div>
		   		</li>
	   		</c:if>
	   	</c:forEach>
	   	<li class="flex" onclick="fbjfCancel(1);">
   			<div class="flex-1">取消积分抵扣</div>
   		</li>
	</ul>
</div>

<div class="lay timeLay" id="fenbiDiv" style="overflow: scroll;max-height: 800px;">
   	<ul>
   		<c:forEach var="fenbi" items="${fenbiList }">
   			<c:if test="${fenbi > 0 }">
		   		<li class="flex" onclick="selectFenbi(this);">
		   			<div class="flex-1" id="fenbis">
		   				<span id="dhfb">${fenbiMap.item_value*fenbi }</span>粉币抵扣<span id="dhfbMoney">${fenbi }</span>元
		   			</div>
		   		</li>
	   		</c:if>
	   	</c:forEach>
	   	<li class="flex" onclick="fbjfCancel(2);">
   			<div class="flex-1">取消粉币抵扣</div>
   		</li>
	</ul>
</div>


<c:if test="${!empty mallTakeTheir }">
<input type="hidden" class="isStorePay" value="${mallTakeTheir.isStorePay }" />
</c:if>


<!-- <div class="fade1" id="fade1"></div>
<div class="seckill_layer">
	<h1 style="text-align: center;">正在排队提交订单，请稍候喔！</h1>
	<br/>
	<h2>抱歉，目前排队人数较多，导致服务器压力山大，请您耐心排队等待，我们将在稍后提交订单。</h2>
  	<input type="button" name="cancel-order" id="cancel-order" class="cancel-order" value="取消提交订单" onclick="cancelOrder();" />
</div> -->
<c:if test="${!empty url && url != null }">
	<input type="hidden" class="url" value="${url }"/>
</c:if>
<input type="hidden" class="alipaySubject" value="${alipaySubject }"/>
<c:if test="${empty addressList && empty loginCity}">
 <iframe id="geoPage" width="100%" height="30%" frameborder=0 scrolling="no" style="display:none;"
    src="https://apis.map.qq.com/tools/geolocation?key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp&effect=zoom"></iframe>
    
<!-- 接收到位置信息后 通过 iframe 嵌入位置标注组件 -->
<iframe id="markPage" width="100%" height="70%" frameborder=0 scrolling="no" src="" style="display:none;"></iframe> 
</c:if>
<c:if test="${!empty isJuliFreight }">
	<input type="hidden" class="isJuliFreight" value="${isJuliFreight }"/>
</c:if>
<c:if test="${!empty isFlow }">
	<input type="hidden" class="isFlow" value="${isFlow }"/>
	<jsp:include page="/jsp/mall/order/layer/flowPhone.jsp"></jsp:include>
</c:if>

<!-- <script src="/js/plugin/jquery-1.8.3.min.js"></script> -->
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script> 
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
	
	var a=$(window).width(),d=870,meta=$("#meta");
	meta.attr("content","width=870,initial-scale="+a/d+", minimum-scale="+a/d+", maximum-scale="+a/d+", user-scalable=no");
	$(".loading").hide();
	
	$('.toggle').click(function(e){
		var toggle = this;
		e.preventDefault();
		$(toggle).toggleClass('toggle--on').toggleClass('toggle--off').addClass('toggle--moving').toggleClass("red");
		setTimeout(function(){
			$(toggle).removeClass('toggle--moving');
		}, 200)
	});
	var userId = "${userid }";
	$('#submit-order').click(function() {
		var receiveId = $('#receiveId').val();
		var deliveryMethod = $('#deliveryMethod').val();
		var orderPayWay = $("#orderPayWay").val();
		if(orderPayWay == null || orderPayWay == ""){
			alert("请选择支付方式");
			return false;
		}
		if(deliveryMethod == 2){
			var appointName = $("#appointName").val();
			var appointTel = $("#appointTelphone").val();
			var deliveryAddress = $("#deliveryAddress").html();
			var deliveryTime = $("#deliveryTime").html();
			var reg =  /^0?(13[0-9]|15[012356789]|17[0678]|18[0123456789]|14[57])[0-9]{8}$/;
			if(appointName == ""){
				alert("提货人姓名不能为空");
				return false;
			}
			if(appointTel == ""){
				alert("手机号码不能为空");
				return false;
			}else if(!reg.test(appointTel)){
				alert("手机号码有误");
				return false;
			}else{
				$("#appointTel").val(appointTel);
			}
			if(deliveryAddress == ""){
				alert("请选择提货地址");
				return false;
			}
			if(deliveryTime == ""){
				alert("请选择提货时间");
				return false;
			}
		}
		var proTypeId = $("#proTypeId").val();
		if(proTypeId > 0){
			receiveId = "0";
		}
		var isJuliFreight = $(".isJuliFreight").val();
		var mem_latitude = $(".mem_latitude").val();
		var mem_longitude = $(".mem_longitude").val();
		var isKm = $(".isKm").val();
		if((receiveId == undefined || receiveId == '') && deliveryMethod == 1){
			alert("请选择收货地址");
			return false;
		}else{
			if(isJuliFreight != null && isJuliFreight != "" && typeof(isJuliFreight) != "undefined" && isJuliFreight == 1  && deliveryMethod == 1){
				if((mem_latitude == null || mem_latitude == "" || mem_longitude == null || mem_longitude == "" || isKm == 1) && proTypeId == 0){
					alert("请重新编辑您的收货地址");
					return false;
				}
			}
			var flowCzPhone = $(".flowCzPhone").val();
			var isFlow = $(".isFlow").val();
			var flowPhone = $("#flowPhone").val();
			if(isFlow != null && isFlow != "" && typeof(isFlow) !="undefined" && (flowCzPhone == "" || flowCzPhone == null || typeof(flowCzPhone) == "undefined")){
				if(flowPhone == "" || flowPhone == null || typeof(flowPhone) == "undefined"){
					showFlow();
					return false;
				}else{
					$(".flowCzPhone").val(flowPhone);
				}
			}
			
			if($("#orderPayWay").val() == 7 && $("#orderMoney").val()*1 == 0){
				alert("您的实付金额为0元，无需找人代付，请重新选择支付方式");
				return false;
			}
			var yhqNum = new Object();
			var unionNum = 0;
			$(".couponDivs").each(function(){
				var shopId = $(this).attr("stoId");
				yhqNum[shopId] = {
					num : $(this).find("#yhqNum").val(),
					money : $(this).find(".orderCountMoney").val(),
					shopId : shopId
				};
				unionNum += $(this).find(".unionNum").val()*1;
			});
			//console.log(yhqNum)
			$("#yhqNum").val(JSON.stringify(yhqNum));
			$('#submit-order').attr("disabled",true);
			var orderDetail = ${orderDetail};
			var freightMoney = "${priceMap}";
			$('#detail').val(JSON.stringify(orderDetail));
			$('#freightMoney').val(JSON.stringify(freightMoney));
			var order = $("#orderForm").serializeObject();
			var groupType = $(".groupType").val();
			if(groupType == 2 || groupType == 5){
				freightMoney = "";
			}
			if(order != null){
				order.groupType = groupType;
				order.groupBuyId = $("input.groupBuyId").val();
			}
			if($(".unionStatus").length > 0){
				var unionDiscountVal = $(".unionDiscountVal").val();
				var unionStatus = $(".unionStatus").val();
				if(unionStatus == 1 && unionDiscountVal != ""){//联盟卡
					var cardId = $(".cardId").val();
					var unionDiscount = $(".unionDiscount").val();
					var union_id = $(".union_id").val();
					
					order.unionDiscount = unionDiscount;
					order.cardId = cardId;
					order.union_id = union_id;
					order.unionNum = unionNum
					//order.unionNum = unionNum
				}
			}
			var obj = JSON.stringify(order);
			var url = "phoneOrder/79B4DE7C/addOrder.do?uId="+userId;
			if(groupType == 3){
				url = "/phoneOrder/79B4DE7C/addSeckillOrder.do?uId="+userId;
			}
			var index = layer.open({
			    title: "",
			    content: "",
			    type:2,
			    shadeClose:false
			});
			$.ajax({
				  url : url,
				  type:"POST",
				  data:{params: obj},
				  timeout:300000,
				  dataType:"json",
				  success:function(data){
						if(data.result){
							//微信支付
							if(data.payWay == 1){
								location.href="/wxPay/79B4DE7C/wxMallAnOrder.do?orderId="+data.orderId;
							}else if(data.payWay == -1 ||data.payWay == 3 || data.payWay == 4 || data.payWay == 7 || data.payWay == 2 || data.payWay == 6|| data.payWay == 8){
								var orderMoney = $('#orderMoney').val();
								//payWay: -1 订单金额为0 2 货到付款  3 储值卡支付   4积分支付  6 到店支付   7招人代付  8粉币支付
								location.href="/phoneOrder/79B4DE7C/payWay.do?orderMoney="+orderMoney+"&orderId="+data.orderId+"&payWay="+data.payWay+"&uId="+data.busId;	
							}else if(data.payWay == 9){
								var model = 3;
								if(data.proTypeId == 2){
									model = 13;
								}
								//var return_url = "${path}/phoneOrder/79B4DE7C/orderList.do?isPayGive=1&&orderId="+data.orderId+"&&uId="+data.busId;
								var alipaySubject = $("input.alipaySubject").val();
								var return_url = "${http}/phoneOrder/"+data.orderId+"/"+data.busId+"/1/79B4DE7C/orderPaySuccess.do";
								location.href="/alipay/79B4DE7C/alipayApi.do?out_trade_no="+data.out_trade_no+"&subject="+alipaySubject+"&total_fee="+data.orderMoney+"&busId="+data.busId+"&model="+model+"&businessUtilName=mallOrderAlipayNotifyUrlBuinessService&return_url="+return_url;
							}
							layer.closeAll();
						}else{
							if(data.isLogin != null && data.isLogin == 1){
								//location.reload(true);
								/* var loginData = {
									data : orderDetail,
									payWayName : $(".paywaynames").text(),
									orderPayWays: $("#orderPayWay").val()
								}; */
								toLogin(null);
							}else{
								layer.closeAll();
								$('#submit-order').attr("disabled",false);
								var txt = "";
								if(null != data.cardResult && data.cardResult != ""){
									txt = data.cardResult.msg;
									if(data.cardResult.code == -1){
										location.href = data.cardResult.url;
									}
								}else{
									txt = data.msg;
								}
								alert(txt);
							}
						}
				  },error:function(){
					  $('#submit-order').attr("disabled",false);
						layer.closeAll();
				  }
				});
		}
	});
	
	function selectAddress() {
		var data = ${data};
		$('#addressType').val($('#type').val());
		$('#data').val(JSON.stringify(data));
		$("#adresspay").val($("#orderPayWay").val());
		$("#adresspayName").val($(".paywaynames").text());
		$('#toAddList').submit(); 
	}

	function toAddress() {
		var data = ${data};
		$('#addressType').val($('#type').val());
		$('#data').val(JSON.stringify(data));
		$("#adresspay").val($("#orderPayWay").val());
		$("#adresspayName").val($(".paywaynames").text());
		document.toAddList.action="phoneOrder/79B4DE7C/addressList.do?uId=${userid}";
		document.toAddList.submit(); 

	}

	function getTakeTheirs(id) {
		var data = ${data};
		$('#id').val(id);
		$('#datas').val(JSON.stringify(data));
		$("#deliveryType").val($("#deliveryMethod").val());
		$("#takePayWay").val($("#orderPayWay").val());
		$("#takePayWayName").val($(".paywaynames").text());
		document.getTakeTheir.submit(); 
	}
	
	var orderPayWay = $("#orderPayWay").val();
	if(orderPayWay == null || orderPayWay == ""){
		if($("#payLayer .lay-item").length > 1){
			var layObj = $("#payLayer .lay-item:eq(1)");
			var ids = layObj.attr("id");
			if(ids != null && ids != "" && typeof(ids) != "undefined"){
				$("#orderPayWay").val(ids);
				$('#onlinePayment').html(layObj.find("label").text());
			}
		}
	}
	function showFlow(){
		$(".fade").show();
		$("#flowPhoneDiv").show();
	}
	function hideFlow(){
		$(".fade").hide();
		$("#flowPhoneDiv").hide();
	}
</script>
<script type="text/javascript" src="/js/mall/order/submitOrder.js"></script>
</body>
</html>
