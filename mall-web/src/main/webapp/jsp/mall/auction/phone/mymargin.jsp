<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE>
<html>
<base href="<%=basePath%>"/>
<head>
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
    <title>我的保证金</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
</head>

<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/reset.css?<%=System.currentTimeMillis()%>"/>
<link rel="stylesheet" type="text/css" href="/css/mall/auction/mymargin.css?<%=System.currentTimeMillis()%>"/>

<div class="Warp">
    <nav class="nav">
        <ul class="clearfix">
            <li class="navItem" id="navItem1">
                <a href="mAuction/79B4DE7C/myBidding.do?uId=${userid }"
                   <c:if test="${type==1}">class="active"</c:if> >我的竞拍</a>
            </li>
            <li class="navItem" id="navItem2">
                <a href="mAuction/79B4DE7C/myMargin.do?uId=${userid }"
                   <c:if test="${type==2}">class="active"</c:if> >我的保证金</a>
            </li>
            <li class="navItem" id="navItem3">
                <a href="mAuction/79B4DE7C/myHuoPai.do?uId=${userid }"
                   <c:if test="${type==3}">class="active"</c:if> >我的获拍</a>
            </li>
        </ul>
    </nav>
    <!--我的竞拍-->
    <section class="main" id="navItem1Main" <c:if test="${type==1}">style="display: block;"</c:if>>
        <c:if test="${!empty bidList }">
            <ul>
                <c:forEach var="bid" items="${bidList }">
                    <li>
                        <div class="mall-item">
                            <div class="mall-img">
                                <a href="mAuction/${bid.pro_id}/${bid.shop_id}/${bid.auc_id}/79B4DE7C/auctiondetail.do"><img src="${http }${bid.pro_img_url }"/></a>
                            </div>
                            <div class="mall-info">
                                <p class="title"><a href="mAuction/${bid.pro_id}/${bid.shop_id}/${bid.auc_id}/79B4DE7C/auctiondetail.do">${bid.pro_name }</a></p>
                                <div class="flex order-up">
                                    <div class="flex-1">竞拍价：<span class="red-txt">￥${bid.auc_price }</span></div>
                                    <div class="flex-1"><a href="javascript:;" class="up">
                                        <c:if test="${bid.auc_type == 1 }"><img src="/images/mall/down.png" class="upimg"/>降价拍</c:if>
                                        <c:if test="${bid.auc_type == 2 }"><img src="/images/mall/up.png" class="upimg"/>升价拍</c:if>
                                    </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="order-time">
                            <span>出价时间</span> <fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${bid.create_time }"/>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </section>
    <!--我的保证金-->
    <section class="main" id="navItem2Main" <c:if test="${type==2}">style="display: block;"</c:if>>
        <c:if test="${!empty marginList }">
            <ul>
                <c:forEach var="margin" items="${marginList }">
                    <li>
                        <div class="mall-item">
                            <div class="mall-img">
                                <a href="mAuction/${margin.proId}/${margin.shopId}/${margin.aucId}/79B4DE7C/auctiondetail.do">
                                    <img src="${http }${margin.proImgUrl }"/>
                                </a>
                            </div>
                            <div class="mall-info">
                                <p class="title"><a href="mAuction/${margin.proId}/${margin.shopId}/${margin.aucId}/79B4DE7C/auctiondetail.do">${margin.proName }</a></p>
                                <p class="">保证金：<em class="red-txt">￥${margin.marginMoney }</em></p>
                                <div class="flex">
                                    <div class="flex-1">
                                        <p>竞拍编号：${margin.aucNo }</p>
                                        <p>由第三方发货</p>
                                    </div>
                                    <div class="flex-1 text-right">
                                        <a href="javascript:;" class="up upmar">
                                            <c:if test="${margin.aucType == 1 }"><img src="/images/mall/down.png" class="upimg"/>降价拍</c:if>
                                            <c:if test="${margin.aucType == 2 }"><img src="/images/mall/up.png" class="upimg"/>升价拍</c:if>
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="mall-status mall-item">
                            <label for="">
                                <c:if test="${margin.marginStatus == 0 }">未支付</c:if>
                                <c:if test="${margin.marginStatus == 1 }">已支付保证金
                                    支付时间：<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${margin.payTime }"/>
                                </c:if>
                                <c:if test="${margin.marginStatus == -1 }">已返还保证金
                                    返还时间：<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${margin.returnTime }"/>
                                </c:if>
                            </label>
                        </div>
                        <div class="order-time">
                            交纳时间：<fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${margin.createTime }"/>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </section>
    <!--我的获拍-->
    <section class="main" id="navItem3Main" <c:if test="${type==3}">style="display: block;"</c:if>>
        <c:if test="${!empty bidList }">
            <ul>
                <c:forEach var="bid" items="${bidList }">
                    <li>
                        <div class="mall-item">
                            <div class="mall-img">
                                <a href="mAuction/${bid.pro_id}/${bid.shop_id}/${bid.auc_id}/79B4DE7C/auctiondetail.do">
                                    <img src="${http }${bid.pro_img_url }"/>
                                </a>
                            </div>
                            <div class="mall-info">
                                <p class="title"><a href="mAuction/${bid.pro_id}/${bid.shop_id}/${bid.auc_id}/79B4DE7C/auctiondetail.do">${bid.pro_name }</a></p>
                                <div class="flex order-up">
                                    <div class="flex-1">竞拍价：<span class="red-txt">￥${bid.auc_price }</span></div>
                                    <div class="flex-1"><a href="javascript:;" class="up">
                                        <c:if test="${bid.auc_type == 1 }"><img src="/images/mall/down.png" class="upimg"/>降价拍</c:if>
                                        <c:if test="${bid.auc_type == 2 }"><img src="/images/mall/up.png" class="upimg"/>升价拍</c:if>
                                    </a>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="mall-status mall-item">
                            <label for="">
                                <c:if test="${empty bid.order_id }">竞拍成功，未转订单</c:if>
                                <c:if test="${bid.auc_status == 1 && !empty bid.order_id}">竞拍成功，已转订单</c:if>
                                <c:if test="${bid.auc_status == -1 }">竞拍失败</c:if>
                            </label>
                            <c:if test="${empty bid.order_id }">
                                <a href="mAuction/${bid.pro_id}/${bid.shop_id}/${bid.auc_id}/79B4DE7C/auctiondetail.do" class="goPay" id="">转订单</a>
                            </c:if>
                        </div>
                        <div class="order-time">竞拍时间：<fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${bid.create_time }"/></div>
                    </li>
                </c:forEach>
            </ul>
        </c:if>
    </section>
</div>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
<script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script type="text/javascript">
    $(window).load(function () {
        var a = $(window).width(),
            b = $(window).height(),
            d = 870,
            meta = $("#meta");
        setTimeout(function () {
            meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
            $(".loading").hide();
        }, 300);
    });

    $(function () {
        /* $('.nav ul li').click(function(){
         $('.navItem a').removeClass('active');
         $(this).find('a').addClass('active').siblings().removeClass('active')
         var li=$(this).attr('id');
         var main=li+"Main";
         $('.main').hide();
         $("#"+main).show();
         }); */
    })
</script>
</body>
</html>