<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>
        <c:choose>
            <c:when test="${orderType == 1 }">团购订单</c:when>
            <c:when test="${orderType == 3 }">秒杀订单</c:when>
            <c:when test="${orderType == 4 }">竞拍订单</c:when>
            <c:otherwise>我的订单</c:otherwise>
        </c:choose>
    </title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
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
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="stylesheet" href="/css/mall/reset.css"/>
    <link rel="stylesheet" href="/css/mall/init.css"/>
    <!-- <link rel="stylesheet" type="text/css" href="/js/gt/css/basic.css"> -->
    <link id="link" rel="stylesheet" type="text/css" href="/css/mall/shoppingall/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/layer-mobile/layer/need/layer.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <style type="text/css">
        .clearfix:after {
            content: ".";
            display: block;
            height: 0;
            clear: both;
            visibility: hidden;
        }

        .clearfix {
            display: inline-block;
        }

        body, html {
            height: 100%;
            width: 100%;
            font-size: 30px;
            position: relative;
        }

        .Warp {
            width: 870px;
            min-height: 100%;
            background-color: #f0f2f5;
            margin: 0 auto;
            color: #484848;
            margin-bottom: 30px;
        }

        .nav {
            display: block;
            width: 100%;
            background-color: #FFFFFF;
            border-bottom: 1px solid #e1e1e1;
            line-height: 60px;
            text-align: center; /* margin-bottom: 20px; height: 60px;*/
        }

        .nav ul {
            width: 100%;
        }

        .nav ul li {
            width: 20%;
            height: 58px;
            line-height: 58px;
            float: left;
            box-sizing: border-box;
            text-align: center;
            list-style: none;
        }

        .nav ul li a {
            display: inline-block;
            width: 100%;
        }

        .nav .active {
            color: #f20000;
            border-bottom: 2px solid #F20000;
        }

        .main ul {
            width: 100%;
        }

        .main ul li {
            background-color: #FFFFFF;
            list-style: none; /* margin-top: 40px; */
        }

        .main .mall-item {
            border-bottom: 1px solid #eeeeee;
            padding: 15px 40px 0px 40px;
        }

        .main .mall-img {
            display: inline-block;
            width: 165px;
        }

        .main .mall-img img {
            width: 123px;
            max-height: 162px;
            display: block;
            margin: 0 auto;
        }

        .main .mall-info {
            display: inline-block;
            width: 350px;
            vertical-align: top;
            font-size: 28px;
        }

        .main .mall-info .red-txt {
            color: #F20000;
            font-size: 30px;
        }

        .main .mall-info span {
            margin-right: 40px;
        }

        .main .mall-info p.title {
            height: 40px;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            width: 470px;
        }

        .mall-status {
            height: 85px;
            line-height: 85px;
            position: relative;
            border-bottom: 1px solid #eeeeee;
            background-color: #fff;
        }

        .mall-status label {
            padding-left: 20px;
        }

        .mall-status .goPay {
            float: right;
            padding: 0px 10px;
            height: 48px;
            border-radius: 10px;
            margin-top: 15px; /* position: absolute;top: 50%;right: 30px;margin-top: -24px; */
            text-align: center;
            border: 2px solid #F20000;
            font-size: 28px;
            display: inline-block;
            line-height: 48px;
            color: #F20000
        }

        .mall-item .goReturn, .btn {
            height: 48px;
            border-radius: 10px;
            text-align: center;
            border: 2px solid #F20000;
            font-size: 28px;
            line-height: 48px;
            color: #F20000;
            vertical-align: top;
            padding: 0px 10px;
        }

        .mall-item .div_a {
            display: inline-block;
            width: 250px;
            vertical-align: top;
        }

        .mall-item .div_a p {
            text-align: right;
            margin-top: 10px;
        }

        .mall_order_no {
            height: 90px;
            line-height: 90px; /* background:#f0f2f5; */
            border-bottom: 1px solid #eeeeee;
        }

        .mall_order_no span {
            margin-left: 20px;
        }

        .df_a {
            float: right;
            margin-top: 15px;
            margin-right: 10px;
            cursor: pointer;
        }

        /* 添加弹出层 */
        .gt_flex {
            display: -webkit-box;
            display: -webkit-flex;
            display: flex;
        }

        .gt_dialog .gt_bg, .gt_dialog .gt_box {
            position: fixed;
        }

        .gt_dialog .gt_bg {
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0, 0, 0, 0.6);
            z-index: 9990
        }

        .gt_dialog .gt_imgbg {
            background-color: rgba(255, 255, 255, 0.8);
        }

        .gt_dialog .gt_box {
            background: #fff;
            width: 86%;
            border-radius: 12px;
            top: 100px;
            left: 7%;
            overflow: hidden;
            z-index: 9991
        }

        .gt_dialog .gt_box .st_label {
            font-size: 34px;
            color: #1eb1f4;
            text-align: center;
            margin: 50px 0 45px 0;
        }

        .gt_dialog .st_html {
            font-size: 32px;
            text-align: center;
        }

        .gt_dialog .gt_box .st_btn {
            margin-top: 3rem;
            border-top: solid 1px #d2d2d2
        }

        .gt_dialog .gt_box .st_btn a {
            position: relative;
            display: block;
            -webkit-box-flex: 1;
            -webkit-flex: 1;
            flex: 1;
            text-align: center;
            height: 100px;
            line-height: 100px;
            text-align: center;
            font-size: 32px;
            color: #333
        }

        .gt_dialog .gt_box .st_btn a:last-child:before {
            position: absolute;
            display: block;
            content: "";
            height: 4.4rem;
            width: 1px;
            background-color: #d2d2d2;
            top: 0;
            left: -1px;
        }

        .gt_dialog .st_tips_txt {
            display: inline-block;
            width: 80%;
            line-height: 1.6;
            color: #666
        }

    </style>
</head>
<body>
<!--加载动画-->
<section class="loading" style="z-index: 999999;">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<div class="Warp">
    <nav class="nav">
        <ul class="clearfix">
            <li><a href="phoneOrder/79B4DE7C/orderList.do?uId=${userid}"
                   class="<c:if test='${(empty type || type == 0) && empty appraiseStatus}'>active</c:if>">全部</a></li>
            <li class="navItem" id="navItem1">
                <a href="phoneOrder/79B4DE7C/orderList.do?orderType=${orderType }&type=1&uId=${userid}"
                   class="<c:if test='${type == 1}'>active</c:if>">待支付</a>
            </li>
            <li class="navItem" id="navItem2">
                <a href="phoneOrder/79B4DE7C/orderList.do?orderType=${orderType }&type=2&uId=${userid}"
                   class="<c:if test='${type == 2}'>active</c:if>">待发货</a>
            </li>
            <li class="navItem" id="navItem3">
                <a href="phoneOrder/79B4DE7C/orderList.do?orderType=${orderType }&type=3&uId=${userid}"
                   class="<c:if test='${type == 3}'>active</c:if>">待收货</a>
            </li>
            <li class="navItem" id="navItem4">
                <a href="/phoneOrder/79B4DE7C/orderList.do?appraiseStatus=0&uId=${userid}"
                   class="<c:if test='${!empty appraiseStatus && appraiseStatus == 0}'>active</c:if>">待评价</a>
            </li>
        </ul>
        <c:if test="${!empty orderType  }">
            <input type="hidden" class="orderType forms_cla" name="orderType" value="${orderType }"/>
        </c:if>
        <c:if test="${!empty type  }">
            <input type="hidden" class="type forms_cla" name="type" value="${type }"/>
        </c:if>
        <c:if test="${!empty appraiseStatus  }">
            <input type="hidden" class="appraiseStatus forms_cla" name="appraiseStatus" value="${appraiseStatus }"/>
        </c:if>
        <%-- <c:choose>
            <c:when test="${orderType == 1 }">团购订单</c:when>
            <c:when test="${orderType == 3 }">秒杀订单</c:when>
            <c:when test="${orderType == 4 }">竞拍订单</c:when>
            <c:otherwise>全部订单</c:otherwise>
        </c:choose> --%>
    </nav>
    <!--待支付-->
    <section class="main" id="navItem1Main">
        <ul>
            <c:forEach var="order" items="${orderList }">
                <li>
                    <div class="mall_order_no">
                        <!-- <span><img alt="" src="" width="40px;" height="40px;" style="border: 1px solid red;" /></span> -->
                        <span>${order.shopName } ></span>
                    </div>
                    <div class="mall_order_no">
                        <span>订单号：${order.orderNo }</span>
                    </div>
                    <c:if test="${order.orderPayWay != 5}">
                        <c:forEach var="orderDetail" items="${order.mallOrderDetail }">
                            <div class="mall-item" <c:if test="${order.orderPayWay != 5}">onclick="toOrderDetail(${order.id});"</c:if>>
                                <div class="mall-img">
                                    <img src="${path}${orderDetail.productImageUrl }"/>
                                </div>
                                <div class="mall-info">
                                    <p class="title">
                                        <c:if test="${order.orderPayWay != 5}">
                                            <a href="${orderDetail.proUrl }">${orderDetail.detProName }</a>
                                        </c:if>
                                        <c:if test="${order.orderPayWay == 5}">扫码支付</c:if>
                                    </p>
                                        ${orderDetail.productSpeciname }
                                    <c:if test="${order.orderPayWay != 4 && order.orderPayWay != 8}">
                                        <p class="red-txt"><em>￥</em>${orderDetail.detPrivivilege }</p>
                                    </c:if>
                                    <c:if test="${order.orderPayWay == 4 || order.orderPayWay == 8}">
                                        <p class="red-txt">${orderDetail.detProPrice }${orderDetail.proUnit }</p>
                                    </c:if>
                                    <p>
                                        <span>数量 ${orderDetail.detProNum }</span>
                                    </p>
                                    <p>
                                        <c:if test="${!empty orderDetail.statusMsg }">
 							<span>
 							${orderDetail.statusMsg }
 							<c:if test="${orderDetail.status == -1 && orderDetail.orderReturn.noReturnReason != null}">原因：${orderDetail.orderReturn.noReturnReason}</c:if>
 							</span>
                                        </c:if>
                                    </p>
                                </div>
                                <div class="div_a"><!-- 申请订单的情况： 代发货，已发货 以及 7天内已完成的订单-->
                                        <%-- <c:set var="isReturns" value="1"></c:set> --%>
                                    <c:set var="isComment" value="0"></c:set>
                                    <c:if test="${!empty orderDetail.isReturnButton }">
                                        <p><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=${orderDetail.id }&id=${orderDetail.orderReturn.id }&type=0&uId=${userid}" class="goReturn"
                                              id="goReturn">申请退款</a></p>
                                    </c:if>
                                    <c:if test="${!empty orderDetail.isUpdateButton }">
                                        <p><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=${orderDetail.id }&id=${orderDetail.orderReturn.id }&type=0&uId=${userid}" class="goReturn"
                                              id="goReturn">修改退款申请</a></p>
                                    </c:if>
                                    <c:if test="${!empty orderDetail.isCancelButton }">
                                        <p><a href="javascript:void(0);" class="goReturn" id="closeReturn" rId="${orderDetail.orderReturn.id }" dId="${orderDetail.id }"
                                              oId="${order.id }">撤销退款</a></p>
                                        <c:set var="isComment" value="1"></c:set>
                                    </c:if>
                                    <c:if test="${!empty orderDetail.isWuliuButton }">
                                        <p><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=${orderDetail.id }&id=${orderDetail.orderReturn.id }&type=1&uId=${userid}"
                                              class="goReturn">填写退货物流信息</a></p>
                                        <c:set var="isComment" value="1"></c:set>
                                    </c:if>
                                    <c:if test="${orderDetail.status != -3 && orderDetail.status != -2}">
                                        <c:set var="isComment" value="1"></c:set>
                                    </c:if>
                                    <c:if test="${order.orderStatus  == 4 && mallPaySet.isComment == 1 && isComment == 0 && orderDetail.appraiseStatus == 0}">
                                        <p><a href="/mMember/79B4DE7C/orderAppraise.do?orDetailId=${orderDetail.id }&uId=${userid}" class="goReturn">去评价</a></p>
                                    </c:if>
                                    <!-- <p><a href="" class="goReturn" id="goReturn">评价</a></p> -->
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                    <c:if test="${order.orderPayWay == 5}">
                        <div style="width: 100%;border-bottom: 1px solid #eeeeee;">
                            <div class="mall-info" style="margin-left:20px;margin-bottom:45px;width:90%;">&nbsp;
                                <p>扫码支付&nbsp;&nbsp; 订单金额：<em>￥</em>${order.orderMoney }&nbsp;&nbsp;&nbsp;&nbsp;
                                    <c:if test="${!empty order.statusName }">${order.statusName }</c:if>
                                </p>
                            </div>
                        </div>
                    </c:if>
                </li>
                <c:if test="${order.orderPayWay != 5}">
                    <div class="mall-status">
                        <label for="">
                            <c:if test="${!empty order.statusName }">${order.statusName }</c:if>

                        </label>
                            <%-- <span>
                                <c:if test="${order.orderFreightMoney == null || order.orderFreightMoney == ''||order.orderFreightMoney==0}">包邮</c:if>
                                <c:if test="${order.orderFreightMoney != null && order.orderFreightMoney != ''&& order.orderFreightMoney>0 }">
                                    运费：<font style="color:#f20000;">${order.orderFreightMoney}</font>元
                                </c:if>
                            </span> --%>
                        <c:if test="${order.orderStatus == 1 && order.orderPayWay != 7}">
                            <a class="goPay" id="goPay" href="javascript:void(0);" onclick="goPay(${order.id},${order.orderPayWay },${order.orderMoney });">去支付</a>
                        </c:if>
                            <%-- <c:if test="${order.orderStatus == 4 }"><a href="" class="goPay" id="goPay">再次购买</a></c:if> --%>
                        <c:if test="${order.orderStatus == 3}">
                            <a onclick="confirmReceipt(${order.id },${order.isShouHuo },${order.orderPayWay });" class="goPay" id="goPay">确认收货</a>
                        </c:if>
                        <c:if test="${order.orderPayWay == 7 && !empty order.daifuUrl}">
                            <a href="${order.daifuUrl }" class="btn df_a" id="btn">代付详情</a>
                        </c:if>
                    </div>
                    <div class="mall-status">
                        <label for="">
                            下单时间： ${order.createTime}
                        </label>
                    </div>
                </c:if>
            </c:forEach>
        </ul>
        <div class="load_divs" style="display: none;">加载更多。。。</div>
        <div style="height: 110px; width:100%; background-color: #fff"></div>
        <jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
    </section>
</div>
<c:if test="${page.curPage+1 <= page.pageCount}">
    <input type="hidden" class="curPage" value="${page.curPage }"/>
    <input type="hidden" class="pageCount" value="${page.pageCount }"/>
    <input type="hidden" class="isLoading" value="1"/>
</c:if>
<input type="text" class="isNextPage" value="${page.isNextPage }"/>

<jsp:include page="/jsp/mall/phoneFooterMenu.jsp"></jsp:include>
<input type="hidden" class="shopid" value="${shopid }"/>
<input type="hidden" class="saleMemberId" value="${saleMemberId }"/>

<c:if test="${!empty cardReceiveId }">
    <input type="hidden" class="cardReceiveId" value="${cardReceiveId }"/>
</c:if>
<input type="hidden" class="memberId forms_cla" name="memberId" value="${memberId }"/>
<input type="hidden" class="userid forms_cla" name="busUserId" value="${userid }"/>
<input type="hidden" class="alipaySubject" value="${alipaySubject }"/>

<input type="hidden" class="paySetComment" value="${mallPaySet.isComment }"/>

<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<%--<script type="text/javascript" src="/js/jquery-2.2.2.js"></script>--%>
<script type="text/javascript" src="/js/plugin/gt/js/gt_common.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>

<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<!-- <script src="/js/plugin/layer/layer.js"></script> -->
<script type="text/javascript">
    $(window).load(function () {
        var a = $(window).width(), b = $(window).height(), d = 870, meta = $("#meta");

        meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
        $(".loading").hide();


        if ($("input.cardReceiveId").length > 0) {
            gtcom.dialog("购买成功，卡券包已存入会员卡卡包，请到会员卡查看！", "d", "showCardReceiveId");
        }
    });
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${record.appid}", // 必填，公众号的唯一标识
        timestamp: "${record.timestamp}", // 必填，生成签名的时间戳
        nonceStr: "${record.nonce_str}", // 必填，生成签名的随机串
        signature: "${record.signature}",// 必填，签名，见附录1
        jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.ready(function () {
        wx.hideOptionMenu();
    });
    function showCardReceiveId() {
        location.href = "/duofenCardPhoneController/79B4DE7C/memberCardList.do?memberId=" + $(".memberId").val();
    }

    //撤销退款
    $("#closeReturn").click(function () {
        var rId = $(this).attr("rId");
        var dId = $(this).attr("dId");
        var oId = $(this).attr("oId");
        if (confirm("如您主动关闭正在处理的退款后，您无法再次发起退款申请，请务必谨慎操作。")) {
            /* var layerLoad = layer.load(1, {
             shade : [ 0.3, '#000' ],
             offset : "30%"
             }); */
            var layerLoad = layer.open({
                title: "",
                content: "",
                type: 2,
                shadeClose: false
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
    var saleMemberId = $("input.saleMemberId").val();
    //店面跳转
    function confirmReceipt(orderId, isReturn, orderPayWay) {
        if (isReturn == 1 && orderPayWay != "4") {
            alert("退款中的商品不能确认收货");
        } else if (orderId != "") {
            var params = {};
            params["orderId"] = orderId;
            if (confirm("确定已收到货？")) {
                $.post("phoneOrder/79B4DE7C/confirmReceipt.do", {
                    params: JSON.stringify(params)
                }, function (result) {
                    if (result == 1) {
                        location.href = window.location.href/* "phoneOrder/79B4DE7C/orderList.do" */;
                    } else {
                        return false;
                    }
                }, "json");
            }
        }
    }

    function toOrderDetail(orderId) {
        window.location.href = "/phoneOrder/79B4DE7C/orderDetail.do?orderId=" + orderId + "&uId=${userid}";
    }

    function goPay(orderId, payWay, orderMoney) {
        var userid = $(".userid").val();
        var layerLoad = layer.open({
            title: "",
            content: "",
            type: 2,
            shadeClose: false
        });
        $.ajax({
            url: "/phoneOrder/79B4DE7C/goPay.do",
            type: "POST",
            data: {
                id: orderId,
                uId: userid
            },
            timeout: 300000,
            dataType: "json",
            success: function (data) {
                if (data.result) {
                    if (data.url !== null && data.url !== "") {
                        location.href = data.url;
                    }
                } else {
                    if (data.code == 0) {
                        alert(data.msg);
                        location.href = window.location.href
                    } else {
                        if (data.msg != null && data.msg != "") {
                            alert(data.msg);
                        } else {
                            alert("支付失败，请稍后重新支付");
                        }
                    }
                }
                setTimeout(function(){
                    layer.closeAll();
                },2000);
                layer.closeAll();
            }, error: function () {
                layer.closeAll();
            }
        });
    }


    var $container = $(window);
    if ($("input.curPage").length > 0 && $("input.isLoading").length > 0) {
        $container.scroll(function () {
            var curPage = $("input.curPage").val();//当前页
            var pageCount = $("input.pageCount").val();//总共的页数
            var isLoading = $("input.isLoading").val();//是否继续加载
            if (isLoading == 0 || curPage * 1 + 1 > pageCount * 1) {
                $container.unbind('scroll');
                return false;
            }
            var totalHeight = $("body").prop('scrollHeight');
            var scrollTop = $container.scrollTop();
            var height = $container.height();
            if (totalHeight - (height + scrollTop) <= (height - 200) && isLoading == 1) {
                loadMore();
            }
        });
    }

    function loadMore() {
        var curPage = $("input.curPage").val();
        if (curPage == null || curPage == '') {
            return false;
        }
        var datas = {
            curPage: curPage * 1 + 1
        };
        if ($(".forms_cla").length > 0) {
            $(".forms_cla").each(function () {
                var val = $(this).val();
                if (val != null && val != "") {
                    datas[$(this).attr("name")] = val;
                }
            });
        }
        var userid = $(".userid").val();
        var paySetComment = $(".paySetComment").val();

        $("input.isLoading").val(-1);
        $.ajax({
            type: "post",
            url: "/phoneOrder/79B4DE7C/mobileOrderListPage.do",
            data: datas,
            dataType: "json",
            success: function (data) {
                var html = "";
                if (data != null) {
                    var page = data.page;
                    var path = data.path;
                    if (page == null) {
                        return false;
                    }
                    if (page.subList != null && page.subList.length > 0) {
                        for (var i = 0; i < page.subList.length; i++) {
                            var order = page.subList[i];
                            html += "<li>";
                            if (order.shopName != null && order.shopName != "") {
                                html += "<div class='mall_order_no'><span>" + order.shopName + " ></span></div>";
                            }
                            html += "<div class='mall_order_no'><span>订单号：" + order.orderNo + "</span></div>";
                            if (order.orderPayWay != 5) {
                                for (var j in order.mallOrderDetail) {
                                    var orderDetail = order.mallOrderDetail[j];
                                    html += '<div class="mall-item" onclick="toOrderDetail(' + order.id + ');">';
                                    html += '<div class="mall-img"><img src="' + (path + orderDetail.productImageUrl) + '"/></div>';
                                    html += '<div class="mall-info">';
                                    html += '<p class="title"><a href="' + orderDetail.proUrl + '">' + orderDetail.detProName + '</a></p>' + orderDetail.productSpeciname;
                                    if (order.orderPayWay != 4 && order.orderPayWay != 8) {
                                        html += '<p class="red-txt"><em>￥</em>' + orderDetail.detPrivivilege + '</p>';
                                    } else {
                                        html += '<p class="red-txt">' + orderDetail.detProPrice + orderDetail.proUnit + '</p>';
                                    }
                                    html += '<p><span>数量   ' + orderDetail.detProNum + '</span></p>';
                                    html += '<p>';
                                    if (orderDetail.statusMsg != null && orderDetail.statusMsg != '') {
                                        html += '<span>' + orderDetail.statusMsg;
                                        if (orderDetail.status == -1 && orderDetail.orderReturn.noReturnReason != null) {
                                            html += '原因：' + orderDetail.orderReturn.noReturnReason;
                                        }
                                        html += '</span>';
                                    }
                                    html += '</p>';
                                    html += '</div>'
                                    html += '<div class="div_a">';//申请订单的情况： 代发货，已发货 以及 7天内已完成的订单-
                                    var isComment = 0;
                                    if (orderDetail.isReturnButton != null && orderDetail.isReturnButton != '') {
                                        html += '<p><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=' + orderDetail.id + '&id=' + orderDetail.orderReturn.id + '&type=0&uId=' + userid + '" class="goReturn" id="goReturn">申请退款</a></p>';
                                    }
                                    if (orderDetail.isUpdateButton != null && orderDetail.isUpdateButton != "") {
                                        html += '<p><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=' + orderDetail.id + '&id=' + orderDetail.orderReturn.id + '&type=0&uId=' + userid + '" class="goReturn" id="goReturn">修改退款申请</a></p>';
                                    }
                                    if (orderDetail.isCancelButton != null && orderDetail.isCancelButton != '') {
                                        isComment = 1;
                                        html += '<p><a href="javascript:void(0);" class="goReturn" id="closeReturn" rId="' + orderDetail.orderReturn.id + '" dId="' + orderDetail.id + '" oId="' + order.id + '" onclick="closeReturn(this);">撤销退款</a></p>';
                                    }
                                    if (orderDetail.isWuliuButton != null && orderDetail.isWuliuButton != '') {
                                        isComment = 1;
                                        html += '<p><a href="/phoneOrder/79B4DE7C/toReturn.do?dId=' + orderDetail.id + '&id=' + orderDetail.orderReturn.id + '&type=1&uId=' + userid + '" class="goReturn">填写退货物流信息</a></p>';
                                    }
                                    if (orderDetail.status != -3 && orderDetail.status != -2) {
                                        isComment = 1;
                                    }
                                    if (order.orderStatus == 4 && paySetComment == 1 && isComment == 0 && orderDetail.appraiseStatus == 0) {
                                        html += '<p><a href="/mMember/79B4DE7C/orderAppraise.do?orDetailId=' + orderDetail.id + '&uId=' + userid + '" class="goReturn">去评价</a></p>';
                                    }

                                    html += "</div>";
                                    html += "</div>";
                                }
                            } else if (order.orderPayWay == 5) {
                                html += '<div style="width: 100%;border-bottom: 1px solid #eeeeee;">';
                                html += '<div class="mall-info" style="margin-left:20px;margin-bottom:45px;width:90%;">&nbsp;';
                                html += '<p>扫码支付&nbsp;&nbsp; 订单金额：<em>￥</em>' + order.orderMoney + '&nbsp;&nbsp;&nbsp;&nbsp;';
                                if (order.statusName != null && order.statusName != '') {
                                    html += order.statusName;
                                }
                                html += '</p></div></div>';
                            }
                            html += "</li>";

                            if (order.orderPayWay != 5) {
                                html += '<div class="mall-status">';
                                html += '<label for="">';
                                if (order.statusName != null && order.statusName != '') {
                                    html += order.statusName;
                                }
                                html += '</label>';
                                if (order.orderStatus == 1 && order.orderPayWay != 7) {
                                    html += '<a class="goPay" id="goPay" href="javascript:void(0);" onclick="goPay(' + order.id + ',' + order.orderPayWay + ',' + order.orderMoney + ');">去支付</a>';
                                }
                                if (order.orderStatus == 3) {
                                    html += '<a onclick="confirmReceipt(' + order.id + ',' + order.isShouHuo + ',' + order.orderPayWay + ');" class="goPay" id="goPay">确认收货</a>';
                                }
                                if (order.orderPayWay == 7 && order.daifuUrl != null && order.daifuUrl != '') {
                                    html += '<a href="' + order.daifuUrl + '" class="btn df_a" id="btn">代付详情</a>';
                                }
                                html += '</div>';
                                html += '<div class="mall-status">';
                                html += '<label for="">下单时间： ' + order.createTime + '</label>';
                                html += '</div>';
                            }

                        }
                    }
                    if (page.curPage * 1 > page.pageCount * 1) {
                        $("input.isLoading").val(0);
                        $container.unbind('scroll');
                        return false;
                    }
                    $("input.curPage").val(page.curPage);
                }
                if (html == "") {
                    $("input.isLoading").val(0);
                    $container.unbind('scroll');
                    return false;
                } else {
                    $("#navItem1Main ul").append(html);
                    $("input.isLoading").val(1);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("input.isLoading").val(1);
            }
        });
    }

</script>
</body>
</html>
