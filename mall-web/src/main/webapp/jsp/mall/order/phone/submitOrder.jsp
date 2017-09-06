<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="UTF-8">
    <title>提交订单</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";
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
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
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
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
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
<form action="/phoneOrder/79B4DE7C/addOrder.do" method="post" id="orderForm">
    <input type="hidden" class="buyType" value="${type }"/><!-- 类型 1购物车 -->
    <input type="hidden" id="freightMoney" value="">
    <input type="hidden" id="isJifen" value="${!empty useFenbi ? useFenbi: 0}">
    <input type="hidden" id="isFenbi" value="${!empty useJifen ? useJifen: 0}">
    <input type="hidden" id="sumCoupon"/>

    <div class="orderDivForm">
        <input type="hidden" name="receiveId"/>
        <input type="hidden" name="orderMoney" id="orderMoney" value=""/>
        <input type="hidden" name="orderFreightMoney" id="orderFreightMoney" value=""/>
        <input type="hidden" name="orderOldMoney" id="orderOldMoney" value=""/>
        <input type="hidden" name="orderPayWay" id="orderPayWay" value="${payWay }"/>
        <input type="hidden" name="deliveryMethod" id="deliveryMethod" value="${deliveryMethod}"/>
        <input type="hidden" name="appointmentName" id="appointmentName"/>
        <input type="hidden" name="appointmentTelephone" id="appointTel"/>
        <input type="hidden" name="appointmentTime" id="appointTime" value="${mallTakeTheir.timeList[0].times}"/>
        <input type="hidden" name="appointmentStartTime" id="appStartTime" value="${mallTakeTheir.timeList[0].startTime}"/>
        <input type="hidden" name="appointmentEndTime" id="appEndTime" value="${mallTakeTheir.timeList[0].endTime}"/>
        <input type="hidden" name="memCardType" id="memCardType" value="${memType }"/>
        <input type="hidden" name="flowPhone" class="flowCzPhone" value="${flowPhone }"/><!-- 流量充值手机号 -->

        <input type="hidden" name="productAllMoney" value=""/>

        <input type="hidden" name="useCoupon" value="0"/><!-- 是否使用优惠券 -->

    </div>

    <input type="hidden" name="detail" id="detail" value="">
    <input type="hidden" name="couponList" id="couponList"/>
    <input type="hidden" name="prizeCount" class="prizeCount" value="${flowType }"/>

    <c:if test="${!empty toshop }">
        <input type="hidden" name="toshop" class="toshop" value="${toshop }"/>
    </c:if>
    <c:set var="pro_type_id" value="0"></c:set>
    <c:set var="url" value=""></c:set>
    <c:set var="pro_type_id" value="${orderDetail[0].message[0].pro_type_id}"></c:set>
    <c:if test="${type == 1}">
        <c:set var="url" value="/mallPage/79B4DE7C/shoppingcare.do?member_id=${member.id }&uId=${userid }"></c:set>
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
                        <input type="hidden" id="receiveId" value="${address.id }"/>
                        <div onclick="toAddress();">
                            <div class="left_div">
                                <div class="info info1">
                                    <div class="">
                                        收件人：<label class="name">${address.mem_name }</label>
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
            <c:forEach var="order" items="${orderDetail }">
                <c:set var="yhqNum" value="0"></c:set>
                <c:set var="totalMoneys" value="0"></c:set>
                <div id="couponDiv" class="couponDivs" stoId="${order.shop_id }" wxShopId="${order.wxShopId }">
                    <input type="hidden" class="couponJson"/>
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
                                    <p class="red-txt">会员价：<em>${price}</em></p>
                                </c:if>

                                <c:if test="${orderDetail.groupType == 7 && empty orderDetail.specMap}"><!-- 批发 无规格-->
                                <p class="red-txt">批发价：<span>${orderDetail.price }</span></p>
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


                        <div class="productDivForm">
                            <input type="hidden" name="index" value="${orderDetail.index}"/>
                            <input type="hidden" name="productId" value="${orderDetail.product_id}"/>
                            <input type="hidden" name="shopId" value="${order.shop_id}"/>
                            <input type="hidden" name="productSpecificas" value="${orderDetail.product_specificas}"/>
                            <input type="hidden" name="productImageUrl" value="${orderDetail.image_url}"/>
                            <input type="hidden" name="detProNum" value="${orderDetail.product_num}"/>
                            <input type="hidden" name="detProPrice" id="proPrice" value="${price}"/>
                            <input type="hidden" name="detProName" value="${orderDetail.pro_name}"/>
                            <input type="hidden" name="detPrivivilege" id="primary_price" value="${orderDetail.primary_price  }"/>
                            <input type="hidden" name="productSpeciname" value="${orderDetail.product_speciname }"/>
                            <input type="hidden" name="totalPrice" value="${orderDetail.pro_price_total}"/>

                            <input type="hidden" name="useCoupon" id="isCoupons" value="${orderDetail.isCoupons }"/>
                            <input type="hidden" name="userCard" id="is_member_discount" value="${orderDetail.is_member_discount }"/>
                            <input type="hidden" name="userJifen" id="integralDeduction" value="${orderDetail.is_integral_deduction }"/>
                            <input type="hidden" name="userFenbi" id="fenbiDeduction" value="${orderDetail.is_fenbi_deduction }"/>

                            <input type="hidden" name="wxShopId" value="${order.wxShopId}"/>
                            <c:if test="${!empty cardMap && !empty cardMap.discount}">
                                <input type="hidden" name="discount" value="${cardMap.discount*10}"/>
                            </c:if>
                        </div>

                        <input type="hidden" id="detailProPrice" value="${price }"/><!-- 价格不能改 -->

                    </c:forEach>
                    <c:if test="${empty order.coupon}">
                        <c:set var="yhqNum" value="0"></c:set>
                    </c:if>
                    <input type="hidden" id="yhqNum" value="${yhqNum }">
                    <input type="hidden" id="yhqSumMoney" value="">

                    <input type="hidden" class="unionNum" value="${order.product_nums }"/>
                    <input type="hidden" class="unionMoney" value="${totalMoneys }">
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
                        <label>备注：</label>
                        <input type="text" name="orderBuyerMessage${order.message[0].shop_id }" id="remark-txt" class="remark-txt" placeholder="请填写备注信息" maxlength="100"/>
                    </div>
                    <div class="sum-info">
			<span class="num-box">
				共<i id="num">${order.product_nums }</i>件商品
			</span>
                        <span class="num-box shopTotal" style="width: 300px;padding-right: 20px;">
				合计：<span class="sum" style="color:#f20000;">￥
						<i>${totalMoneys }</i>
					 </span>
				<input type="hidden" name="primaryPrice" class="primaryPrice" value="${order.primary_price}"/>
				<input type="hidden" class="orderCountMoney" value="${order.price_total }"/>
			</span>
                        <span class="sum-box shopShipment" sId="${order.message[0].shop_id }">
				运费：
				<span style="color:#f20000;">
				<c:forEach var="shipment" items="${priceMap }">
                    <c:if test="${order.message[0].shop_id == shipment.key }">
                        <em class="freight_em">${shipment.value}</em>
                    </c:if>
                </c:forEach>元
				</span>
			</span>
                        <input type="hidden" class="shopId" value="${order.message[0].shop_id }"/>
                        <input type="hidden" class="proNum" value="${order.product_nums }"/>
                        <input type="hidden" class="priceTotal" value="${order.price_total }"/>
                        <input type="hidden" id="proTypeId" value="${order.message[0].pro_type_id }"/>
                    </div>
                    <c:if test="${!empty order.coupon || !empty order.duofenCoupon}">
                        <div class="pay-layer" id="coupon">
                            <div class="lay-item">优惠券
                                <i class="delete" onclick="hideLay()"></i>
                            </div>
                            <c:forEach var="dfCoupon" items="${order.duofenCoupon}">
                                <div class="lay-item duofenCouponDiv" onclick="jisuan(1,this,1);" wxShopId="${order.wxShopId}">
                                    <input type="hidden" id="cardId" value="${dfCoupon.gId}"/>
                                    <input type="hidden" id="cardCode" value="${dfCoupon.code }"/>
                                    <input type="hidden" id="couponType" value="1"/>
                                    <img src="${path}${dfCoupon.image }" width="100px;" height="65px;"/>
                                    <c:if test="${dfCoupon.card_type == 1 }">
                                        <label id="couponExplain">满${dfCoupon.cash_least_cost }减${dfCoupon.reduce_cost }元</label>
                                        <c:if test="${dfCoupon.addUser == 1}">
                                            <span style="padding-left: 10px;">
                                                x<span id="couponNum">${dfCoupon.countId }</span>
                                            </span>
                                        </c:if>
                                    </c:if>
                                    <c:if test="${dfCoupon.card_type == 0}">
                                        <label id="couponExplain">${dfCoupon.discount }折</label>
                                        <input type="hidden" id="discount" value="${dfCoupon.discount }"/>
                                    </c:if>
                                </div>
                            </c:forEach>
                            <c:forEach var="coupon" items="${order.coupon}">
                                <div class="lay-item" onclick="jisuan(1,this,1);" wxShopId="${order.wxShopId}">
                                    <input type="hidden" id="cardId" value="${coupon.id}"/>
                                    <input type="hidden" id="cardCode" value="${coupon.user_card_code }"/>
                                    <input type="hidden" id="couponType" value="0"/>
                                    <img src="${coupon.image }" width="100px;" height="65px;"/>
                                    <c:if test="${coupon.card_type == 'DISCOUNT' }">
                                        <label id="couponExplain">${coupon.discount }折</label>
                                        <input type="hidden" id="discount" value="${coupon.discount }"/>
                                    </c:if>
                                    <c:if test="${coupon.card_type == 'CASH' }">
                                        <label id="couponExplain">满${coupon.cash_least_cost }减${coupon.reduce_cost }元</label>
                                    </c:if>
                                </div>
                            </c:forEach>
                            <div class="lay-item" onclick="jisuan(1,this,0);">
                                <span>取消使用优惠券</span>
                            </div>
                        </div>
                    </c:if>
                </div>
            </c:forEach>
        </section>

        <input type="hidden" class="jifenNum" name="jifenNum" value="${jifenNum }"/>
        <input type="hidden" class="jifenProMoney" name="jifenProMoney" value="${jifenProMoney }">
        <input type="hidden" class="fenbiNum" name="fenbiNum" value="${fenbiNum }"/>
        <input type="hidden" class="fenbiProMoney" name="fenbiProMoney" value="${fenbiProMoney }">
        <c:if test="${!empty unionMap }">
            <c:if test="${unionMap.status == 1 || unionMap.status == -2 }">

                <div class="pay-way" onclick="showUnionLayer()">
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
        <c:set var="fenbiNum" value="0"/>
        <c:set var="fenbiMoney" value="0"/>
        <c:set var="isOpenFenbi" value="0"/>
        <c:set var="isOpenJifen" value="0"/>
        <c:set var="jifenNum" value="0"/>
        <c:set var="jifenMoeny" value="0"/>
        <c:if test="${!empty cardMap}">
            <c:if test="${!empty cardMap.fenbiMoeny && !empty cardMap.fans_currency}">
                <c:set var="isOpenFenbi" value="1"></c:set>
                <c:set var="fenbiNum" value="${cardMap.fans_currency }"/>
                <c:set var="fenbiMoney" value="${cardMap.fenbiMoeny }"/>
            </c:if>
            <c:if test="${!empty cardMap.jifenMoeny && !empty cardMap.integral}">
                <c:set var="isOpenJifen" value="1"/>
                <c:set var="jifenNum" value="${cardMap.integral }"/>
                <c:set var="jifenMoeny" value="${cardMap.jifenMoeny }"/>
            </c:if>
        </c:if>
        <div class="pay-way pay-way-slide-btn"><!-- showLay('fenbiDiv',null) -->
            <input type="hidden" id="fenbi_money" name="fenbi_money" value="${fenbiMoney }"/>
            <input type="hidden" id="fenbi" name="fenbi" value="${fenbiNum }"/>
            <c:set var="fenbiFlag" value="true"></c:set>
            <c:if test="${groupTypes == 2 || groupTypes == 5 }"><!-- 积分兑换和粉币兑换 -->
            <c:set var="fenbiFlag" value="false"></c:set>
            </c:if>
            <span>粉币</span><br/>

            <span class="fenbi_open_span" style="<c:if test="${isOpenFenbi == 0 }">display: none;</c:if>">
                <span class="pay-explan ">有<em class="num">${fenbiNum }</em>粉币，可抵扣<em class="money">¥${fenbiMoney }</em></span>
				<i onclick="jisuan(2);" class="fenbiyouhui"><img class="off" src="/images/icon/off_icon.jpg"/><img class="on" src="/images/icon/on_icon.jpg"/></i>
			</span>
            <span class="fenbi_noopen_span" style="<c:if test="${isOpenFenbi == 1 }">display: none;</c:if>">
				<span class="pay-explan" style="color: #535353;">无可用</span>
				<i><img src="/images/icon/off_icon.jpg"/></i>
			</span>
        </div>
        <div class="pay-way pay-way-slide-btn"><!-- showLay('jifenDiv',null) -->
            <input type="hidden" id="integral_money" name="integral_money" value="${jifenMoeny }"/>
            <input type="hidden" id="integral" name="integral" value="${jifenNum }"/>
            <input type="hidden" id="starttype" value="${map.paramSet.starttype }"/>
            <input type="hidden" id="orderStartMoney" value="${map.paramSet.startmoney }"/>
            <span>积分</span><br/>
            <span class="jifen_open_span" style="<c:if test="${isOpenJifen == 0 }">display: none;</c:if>">
                <span class="pay-explan" style="color: #f20000;">有<em class="num">${jifenNum }</em>积分，可抵扣<em class="money">¥${jifenMoeny }</em></span>
				<i onclick="jisuan(3);" class="jifenyouhui">
					<img class="off" src="/images/icon/off_icon.jpg"/><img class="on" src="/images/icon/on_icon.jpg"/>
				</i>
			</span>
            <span class="jifen_noopen_span" style="<c:if test="${isOpenJifen == 1 }">display: none;</c:if>">
				<span class="pay-explan" style="color: #535353;">无可用</span>
				<i><img src="/images/icon/off_icon.jpg"/></i>
			</span>

        </div>
        <div class="delivery_way" style="display: none;" id="delivery_way">
            <ul>
                <li class="flex">
                    <div>提货人：</div>
                    <div><input type="text" id="appointName" placeholder="请填写提货人姓名" value="${appointName}"/></div>
                </li>
                <li class="flex">
                    <div>手机号码：</div>
                    <div><input type="tel" id="appointTelphone" placeholder="请填写提货人手机号码" value="${appointTel}"
                                maxlength="11"/></div>
                </li>
                <li class="flex" style="height: auto;">
                    <div>提货地址：</div>
                    <div style="line-height: 50px">
                        <a href="javascript:void(0);" onclick="getTakeTheirs(${mallTakeTheir.id });">
                            <!-- <input type="text" name="" id="" value="" placeholder="请填写商户提货地址"/> -->
                            <input type="hidden" name="takeTheirId" value="${mallTakeTheir.id }"/>
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
            <label class="text-left">找人买单</label>
            <div class="on-off">
                <a href="#" class="toggle toggle--off"></a>
            </div>
        </div> -->
        <div class="summary">
            <div class="money-box">
                <label>商品金额</label>
                <div class="red-txt">￥<span id="money"></span></div>
                <input type="hidden" id="proMoneyAll" value="0"/>
                <input type="hidden" id="proMoneyAllOld" value="0"/>
            </div>
            <div class="fare-box">
                <label>运费</label>
                <div class="red-txt">+ ￥<span id="fare"></span></div>
            </div>
            <div class="fare-box">
                <label>会员</label>
                <div class="red-txt">- ￥<span id="hy">0.00</span></div>
            </div>
            <div class="fare-box">
                <label>优惠券</label>
                <div class="red-txt">- ￥<span id="yhj">0.00</span></div>
            </div>
            <div class="fare-box">
                <label>粉币</label>
                <div class="red-txt">- ￥<span id="fb">0.00</span></div>
            </div>
            <div class="fare-box">
                <label>积分</label>
                <div class="red-txt">- ￥<span id="jf">0.00</span></div>
            </div>
            <c:if test="${!empty unionMap && unionMap.status == 1}">
                <div class="fare-box">
                    <label>联盟优惠</label>
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
                <a href="javascript:void(0);" name="submit-order" id="submit-order" class="submit-order" onclick="submitOrders();">提交订单</a>
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
<input type="hidden" name="sumOldMoney" id="sumOldMoney"/>
<form action="/phoneOrder/79B4DE7C/addressList.do?uId=${userid }" method="post" id="toAddList" name="toAddList">
    <input type="hidden" name="data" id="data" value=""/>
    <input type="hidden" name="addressType" id="addressType" value=""/>
    <input type="hidden" name="payWay" id="adresspay"/>
    <input type="hidden" name="payWayName" id="adresspayName"/>
</form>
<form action="/phoneOrder/79B4DE7C/getTakeTheir.do?uId=${userid }" method="post" id="getTakeTheir" name="getTakeTheir">
    <input type="hidden" name="datas" id="datas" value=""/>
    <input type="hidden" name="id" id="id" value=""/>
    <input type="hidden" name="deliveryType" id="deliveryType"/>
    <input type="hidden" name="payWay" id="takePayWay"/>
    <input type="hidden" name="payWayName" id="takePayWayName"/>
    <input type="hidden" name="type" value="${type }"/>
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
            <label>微信支付</label>
        </div>
    </c:if>
    <c:if test="${isAliPay == 1}">
        <div class="lay-item aliPay_div" onclick="payWay(9);" id="9">
            <img src="/images/mall/pay-ali.png" width="52" height="52"/>
            <label>支付宝支付</label>
        </div>
    </c:if>
    <c:if test="${!empty cardMap}">
        <c:if test="${cardMap.ctId == 3 }">
            <div class="lay-item czkpay_div" onclick="payWay(3);" id="3">
                <img src="images/mall/giftCard.jpg" width="52" height="52"/>
                <label>储值卡支付</label>
            </div>
        </c:if>
    </c:if>
    <c:if test="${isHuoDao == 1 && pro_type_id == 0 }">
        <div class="lay-item huodao" onclick="payWay(2);" id="2">
            <img src="images/mall/pay-cash.jpg" width="52" height="52"/>
            <label>货到付款</label>
        </div>
    </c:if>
    <div class="lay-item jfDiv" onclick="payWay(4);" id="4" style="display:none;">
        <img src="images/mall/pay-wx.jpg"/>
        <label>积分支付</label>
    </div>
    <div class="lay-item fbDiv" onclick="payWay(8);" id="8" style="display:none;">
        <img src="images/mall/pay-wx.jpg"/>
        <label>粉币支付</label>
    </div>
    <c:if test="${!empty mallTakeTheir }">
        <c:if test="${mallTakeTheir.isStorePay == 1 }">
            <div class="lay-item storePayDiv" onclick="payWay(6);" id="6" style="display:none;">
                <img src="images/mall/pay-wx.jpg"/>
                <label>到店支付</label>
            </div>
        </c:if>
    </c:if>
    <c:if test="${isDaifu == 1 && pro_type_id != 2 }">
        <div class="lay-item huodao" onclick="payWay(7);" id="7">
            <img src="images/mall/pay-cash01.jpg" width="52" height="52"/>
            <label>找人代付</label>
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
            <label>到店购买</label>
        </div>
    </c:if>
    <c:if test="${empty toshop }">
        <div class="lay-item" onclick="delivery(1);">
            <img src="images/mall/kuaidi.png"/>
            <label>快递配送</label>
        </div>
    </c:if>
    <c:if test="${pro_type_id == 0 && isTakeTheir == 1}">
        <div class="lay-item" onclick="delivery(2);" id="daodian">
            <img src="images/mall/dao.png"/>
            <label>到店自提</label>
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
    <input type="hidden" class="isStorePay" value="${mallTakeTheir.isStorePay }"/>
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
<c:if test="${!empty isJuliFreight }">
    <input type="hidden" class="isJuliFreight" value="${isJuliFreight }"/>
</c:if>
<c:if test="${!empty isFlow }">
    <input type="hidden" class="isFlow" value="${isFlow }"/>
    <jsp:include page="/jsp/mall/order/layer/flowPhone.jsp"></jsp:include>
</c:if>

<input type="hidden" class="memberId" value="${!empty member && !empty member.id?member.id:'' }"/>
<input type="hidden" class="shopcards" value="${shopcards}"/>

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

    wx.ready(function () {
        wx.hideOptionMenu();
    });

    var a = $(window).width(), d = 870, meta = $("#meta");
    meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
    $(".loading").hide();

    $('.toggle').click(function (e) {
        var toggle = this;
        e.preventDefault();
        $(toggle).toggleClass('toggle--on').toggleClass('toggle--off').addClass('toggle--moving').toggleClass("red");
        setTimeout(function () {
            $(toggle).removeClass('toggle--moving');
        }, 200)
    });
    var userId = "${userid }";
    var freightMoney = '${priceMap}';
    if (freightMoney !== null && freightMoney !== "") {
        freightMoney = JSON.parse(freightMoney);
    }

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
        document.toAddList.action = "phoneOrder/79B4DE7C/addressList.do?uId=${userid}";
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
    if (orderPayWay == null || orderPayWay == "") {
        if ($("#payLayer .lay-item").length > 1) {
            var layObj = $("#payLayer .lay-item:eq(1)");
            var ids = layObj.attr("id");
            if (ids != null && ids != "" && typeof(ids) != "undefined") {
                $("#orderPayWay").val(ids);
                $('#onlinePayment').html(layObj.find("label").text());
            }
        }
    }
    function showFlow() {
        $(".fade").show();
        $("#flowPhoneDiv").show();
    }
    function hideFlow() {
        $(".fade").hide();
        $("#flowPhoneDiv").hide();
    }
</script>
<script type="text/javascript" src="/js/mall/order/submitOrder.js"></script>
<script type="text/javascript" src="/js/mall/order/submitOrderNew.js"></script>
<script type="text/javascript" src="/js/mall/order/calculateOrder.js"></script>
</body>
</html>
