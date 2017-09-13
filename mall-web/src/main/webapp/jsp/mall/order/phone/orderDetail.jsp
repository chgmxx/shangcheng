<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <title>订单详情</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
<link rel="stylesheet" type="text/css" href="/css/mall/phoneOrder.css"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<div class="Warp">
    <div class="Warp_main">
        <div class="order_status">
            <div class="order-box">
                <img alt="" src="images/mall/orderDetail.png" width="100px;" height="100px;" style="padding-left: 60px;">
                <span class="order-display-box">
 			    <span class="order_display">订单状态：${order.statusName }
 			    	<c:if test="${order.orderType != 0 && order.orderType != null && order.orderType != '' }">
                        <c:if test="${count.num < groupBuy.gPeopleNum }">，等待成团</c:if>
                        <c:if test="${count.num == groupBuy.gPeopleNum }">，已成团</c:if>
                    </c:if>
 			    </span>
 			    <c:if test="${order.orderType != 0 && order.orderType != null && order.orderType != '' }">
                    <c:if test="${order.orderStatus != 3 && order.orderStatus != 4 && order.orderStatus != 5 }">
                        <span class="order_display">拼团单需在<span id="time"></span>内邀请${groupBuy.gPeopleNum }位好友参团</span>
                    </c:if>
                </c:if>
 			</span>
            </div>
            <c:if test="${order.orderType != 0 && order.orderType != null && order.orderType != '' && order.orderStatus != 5}">
                <div style="overflow: hidden;">
                    <dl>
                        <dt>卖家已付款</dt>
                    </dl>
                    <dl>
                        <dt>已成团</dt>
                    </dl>
                    <dl>
                        <dt>商家已发货</dt>
                    </dl>
                    <dl>
                        <dt>交易完成</dt>
                    </dl>
                </div>
                <div class="step">
                    <span class="doing"></span>
                    <span class="do-line"></span>
                    <span class="${count.num == groupBuy.gPeopleNum?'doing':'done' }"></span>
                    <span class="do-line"></span>
                    <span class="${order.orderStatus == 3?'doing':'done' }"></span>
                    <span class="do-line"></span>
                    <span class="${order.orderStatus == 4?'doing':'done' }"></span>
                </div>
            </c:if>
        </div>
        <c:if test="${!empty orders.receiveName }">
            <section class="delivery-info">
                <div>
                    <div>
                        <div class="info info1">
                            <div class="">
                                收件人：<label class="name">${orders.receiveName }</label>
                            </div>
                            <div class="phone">
                                    ${orders.receivePhone }
                            </div>
                        </div>
                        <div class="info info2">
                            收货地址：${orders.receiveAddress }
                        </div>
                        <c:if test="${!empty expressName }">
                            <div class="info info2">快递公司： ${expressName }</div>
                        </c:if>
                        <c:if test="${!empty order.expressNumber }">
                            <div class="info info2">快递单号： ${order.expressNumber }</div>
                        </c:if>
                        <div class="info info2">
                            <a href="http://m.kuaidi100.com/#input" style="color: #000;">配送物流查询</a>
                        </div>
                    </div>
                </div>
            </section>
        </c:if>
        <c:if test="${!empty take && order.deliveryMethod == 2}">
            <section class="delivery-info">
                <div>
                    <div>
                        <div class="info info1">
                            <div class="">
                                提货人姓名：<label for="" class="name" style="width:200px;">${order.appointmentName }</label>
                            </div>
                            <div class="phone">${order.appointmentTelephone }</div>
                        </div>
                        <div class="info info2">
                            自提点电话：${take.visitContactNumber}
                        </div>
                        <div class="info info2">
                            自提点地址：${take.visitAddressDetail}
                        </div>
                        <c:if test="${take.visitRemark != '' }">
                            <div class="info info2">
                                自提点备注：${take.visitRemark}
                            </div>
                        </c:if>
                        <c:if test="${!empty orders.appointmentTime && orders.appointmentTime != null && orders.appointmentTime != 'null'}">
                            <div class="info info2">
                                提货时间：<fmt:formatDate value="${orders.appointmentTime}" pattern="yyyy-MM-dd"/>
                            </div>
                        </c:if>
                    </div>
                </div>
            </section>
        </c:if>
        <section class="main">
            <h2 class="store-name">店铺：${order.shopName }</h2>
            <c:forEach var="orderDetail" items="${order.mallOrderDetail }">
                <div class="mall-item">
                    <div class="mall-img">
                        <a href="${orderDetail.proUrl }"><img src="${path}${orderDetail.productImageUrl }"/></a>
                    </div>
                    <div class="mall-info">
                        <p>
                            <a href="${orderDetail.proUrl }">${orderDetail.detProName }</a>
                        </p>
                            ${orderDetail.productSpeciname }
                        <c:if test="${order.orderPayWay != 4 && order.orderPayWay != 8}">
                            <p class="red-txt" style="color: #3f3f3f;"><em>￥</em>
                                <c:if test="${order.orderPayWay != 5}">${orderDetail.detPrivivilege }</c:if>
                                <c:if test="${order.orderPayWay == 5}">${order.orderMoney }</c:if>
                            </p>
                        </c:if>
                        <c:if test="${order.orderPayWay == 4 || order.orderPayWay == 8}">
                            <p class="red-txt" style="color: #3f3f3f;">${orderDetail.detProPrice }${orderDetail.proUnit }</p>
                        </c:if>
                        <p class="red-txt" style="color: #3f3f3f;">x${orderDetail.detProNum }</p>
                        <c:if test="${!empty orderDetail.isReturnButton }">
                            <p class="red-txt"><a class="order_apply goReturn"
                                                  href="/phoneOrder/79B4DE7C/toReturn.do?dId=${orderDetail.id }&id=${orderDetail.orderReturn.id }&type=0&uId=${userid }">申请退款</a>
                            </p>
                        </c:if>
                        <c:if test="${!empty orderDetail.isUpdateButton }">
                            <p class="red-txt"><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=${orderDetail.id }&id=${orderDetail.orderReturn.id }&type=0&uId=${userid}"
                                                  class="goReturn" id="goReturn">修改退款申请</a></p>
                        </c:if>
                        <c:if test="${!empty orderDetail.isCancelButton }">
                            <p class="red-txt"><a href="javascript:void(0);" class="goReturn order_apply closeReturn" id="closeReturn" rId="${orderDetail.orderReturn.id }"
                                                  dId="${orderDetail.id }" oId="${order.id }">撤销退款</a></p>
                        </c:if>
                        <c:if test="${!empty orderDetail.isWuliuButton }">
                        <p class="red-txt"><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=${orderDetail.id }&id=${orderDetail.orderReturn.id }&type=1&uId=${userid }"
                                              class="goReturn order_apply" style="width:250px;">填写退货物流信息</a>
                            </c:if>
                            <c:if test="${order.orderStatus  == 4 && mallPaySet.isComment == 1 && isComment == 0 && orderDetail.appraiseStatus == 0}">
                        <p class="red-txt">
                        <p><a href="/mMember/79B4DE7C/orderAppraise.do?orDetailId=${orderDetail.id }&uId=${userid}" class="goReturn">去评价</a></p>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
            <div class="sum-info" style="text-align: left;border-bottom: 1px solid #eeeeee;">
                <span class="sum-box" style="padding-left: 20px;width: 400px;padding-right:0px;">运费</span>
                <span class="sum-box shopShipment" style="text-align: right;width: 370px;">
			<span><c:if test="${empty orderDetail.proUnit }">￥</c:if>${order.orderFreightMoney}<c:if test="${!empty orderDetail.proUnit }">${orderDetail.proUnit }</c:if></span>
		</span>
            </div>
            <div class="remark">
                <label for="">买家留言：</label>
                <span>${order.orderBuyerMessage != ""?order.orderBuyerMessage:'无' }</span>
            </div>

            <div class="sum-info" style="text-align: left;">
                <span class="sum-box" style="padding-left: 20px;width: 400px;padding-right:0px;">合计</span>
                <span class="sum-box shopShipment" style="text-align: right;width: 370px;">
			<c:if test="${empty orderDetail.proUnit }"><span style="color: #ff841a;">￥${order.orderMoney}</span></c:if>
			<c:if test="${!empty orderDetail.proUnit }"><span style="color: #ff841a;">${order.orderMoney}${orderDetail.proUnit }</span></c:if>
		</span>
            </div>
        </section>
    </div>
    <footer class="footer" style="height: auto; ">
        <div class="footer-box" style="width: 100%;text-align: center;">
   		<span style="color: #3f3f3f;display: inline-block;width: 100%;">
   			<c:if test="${empty orderDetail.proUnit }">
                ￥<em class="price_em">${(order.orderMoney-order.orderFreightMoney)}</em> + ￥${order.orderFreightMoney}运费
            </c:if>
   			<c:if test="${!empty orderDetail.proUnit }">
                ${(order.orderMoney-order.orderFreightMoney)}${orderDetail.proUnit }
            </c:if>
   		</span>
            <span style="color: #ff841a;">
   		<c:if test="${order.orderStatus == 2 && order.orderPayWay != 2  && order.orderPayWay != 6}">已付：</c:if>
   		<c:if test="${empty orderDetail.proUnit }">
            ￥<span id="sum-money">${order.orderMoney}</span>
        </c:if>
   		<c:if test="${!empty orderDetail.proUnit }">
            <span id="sum-money">${order.orderMoney}</span>${orderDetail.proUnit }
        </c:if>
   		</span>

            <span style="text-align: left;display: block;">订单编号：${order.orderNo }</span>
        </div>
        <c:if test="${order.orderType != 0 && order.orderType != null && order.orderType != '' && order.orderStatus != 5 && order.orderType == 1}">
            <div class="footer-box footer-right" style="width: 100%;">
                <a href="mGroupBuy/${order.groupBuyId }/${count.pId }/79B4DE7C/groupBuyDetail.do?uId=${userid }&buyerUserId=${openGroupUserId}">
                    <input type="button" name="submit-order" id="submit-order" class="submit-order" value="查看拼团详情" style="width: 229px;"/>
                </a>
            </div>
        </c:if>
    </footer>
</div>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
<input type="hidden" id="groupBuyTime" value="${endTime}"/>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script src="/js/plugin/layer/layer.js"></script>
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

    $(window).load(function () {
        var a = $(window).width(),
            b = $(window).height(),
            d = 870,
            meta = $("#meta");
        meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
        setTimeout(function () {
            $(".loading").hide();
        }, 1000);
    });

    $('.toggle').click(function (e) {
        var toggle = this;
        e.preventDefault();
        $(toggle).toggleClass('toggle--on').toggleClass('toggle--off').addClass('toggle--moving').toggleClass("red");
        setTimeout(function () {
            $(toggle).removeClass('toggle--moving');
        }, 200)

    });

    $(function () {
        var interval = 1000;

        function ShowCountDown(endTime, divname) {
            var leftsecond = parseInt(endTime);
            var day1 = Math.floor(leftsecond / (60 * 60 * 24));
            var hour = Math.floor((leftsecond - day1 * 24 * 60 * 60) / 3600);
            var minute = Math.floor((leftsecond - day1 * 24 * 60 * 60 - hour * 3600) / 60);
            var second = Math.floor(leftsecond - day1 * 24 * 60 * 60 - hour * 3600 - minute * 60);
            var cc = document.getElementById(divname);
            if (day1 < 0) {
                day1 = 0;
                hour = 0;
                minute = 0;
            }
            cc.innerHTML = day1 + "天" + hour + "小时" + minute + "分钟";
        }

        var groupBuyTime = $("#groupBuyTime").val();
        if (null != groupBuyTime && groupBuyTime != "") {
            window.setInterval(function () {
                ShowCountDown(groupBuyTime, 'time');
            }, interval);
        }

        //撤销退款
        $("#closeReturn").click(function () {
            var rId = $(this).attr("rId");
            var dId = $(this).attr("dId");
            var oId = $(this).attr("oId");
            if (confirm("如您主动关闭正在处理的退款后，您无法再次发起退款申请，请务必谨慎操作。")) {
                var layerLoad = layer.load(1, {
                    shade: [0.3, '#000'],
                    offset: "30%"
                });
                $.ajax({
                    type: "post",
                    url: "/phoneOrder/79B4DE7C/closeReturnOrder.do",
                    data: {
                        id: rId,
                        orderDetailId: dId,
                        status: -2,
                        orderId: oId
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.closeAll();
                        if (data.flag == true) {// 重新登录
                            alert("撤销退款成功");
                            location.href = window.location.href;
                        } else {// 编辑失败
                            alert("撤销退款失败");
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        layer.closeAll();
                        alert("撤销退款失败");
                        return;
                    }
                });
            }
        });

        if ($(".price_em").length > 0) {
            var price = $(".price_em").text();
            if (price != null && price != "" && typeof(price) != "undefined") {
                $(".price_em").text(price.toFixed(2));
            }
        }
    });


</script>
</body>
</html>