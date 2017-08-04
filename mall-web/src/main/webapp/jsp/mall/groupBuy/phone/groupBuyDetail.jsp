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
    <meta charset="UTF-8">
    <title>拼团详情</title>
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
<link id="link" rel="stylesheet" type="text/css" href="/css/mall/groupbuy/index.css"/>
<link id="link" rel="stylesheet" type="text/css" href="/css/mall/groupbuy/reset.css"/>
<script src="/js/plugin/jquery-1.8.3.min.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js?<%= System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

<c:set var="joinId" value="0"></c:set>
<c:set var="groupBuyId" value="0"></c:set>
<div class="warp">
    <div class="group_top">
        <div class="top_inner">
            <div class="wait_logo"></div>
            <div class="top_inner_info">
                <div class="">
                    <a href="/mallPage/${productMap.id }/${productMap.shopId }/79B4DE7C/phoneProduct.do?uId=${userid}" class="top_inner_pic">
                        <img src="${imgHttp }${productMap.image_url }" alt="团购图片"/>
                    </a>
                </div>
                <div class="top_inner_title font28"><a href="/mallPage/${productMap.id }/${productMap.shopId }/79B4DE7C/phoneProduct.do?uId=${userid}">${productMap.pro_name }</a>
                </div>
                <c:if test="${!empty productMap.specifica_values }">
                    <div class="top_inner_attr">${productMap.specifica_values }</div>
                </c:if>
                <div class="top_inner_price">团购价：￥<span class="font35 price_span">${productMap.price }</span></div>
            </div>
            <div class="top_inner_time">
                <div class="flex-1 count-down time-item" id="time-item">
                    <!--	<label for="">活动剩余时间</label>-->
                    剩余
                    <span id="day_show" class="day_show badge">59</span>天
                    <span id="hour_show" class="hour_show marl badge">0</span>时
                    <span id="minute_show" class="minute_show badge">40</span>分
                    <span id="second_show" class="second_show badge">20</span>秒
                    <input type="hidden" class="diffTimes" value="${productMap.times }"/>
                </div>
            </div>
        </div>
    </div>
    <div class="group_center bgfff">
        <input type="hidden" class="chaPeopleNum" value="${productMap.peopleNum-joinList.size() }"/>
        <h2 class="cells">还差<span class="colore43">${productMap.peopleNum-joinList.size() }</span>人就要开团啦</h2>
        <ul class="tuan_list">
            <c:if test="${!empty joinList }">
                <c:forEach var="join" items="${joinList }">
                    <li class="item flex cells">
                        <div class="flex-1">
                            <img src="<c:if test='${join.headimgurl != null && join.headimgurl != "" }'>${join.headimgurl }</c:if><c:if test='${join.headimgurl == null || join.headimgurl == "" }'>/images/mall/img/pt-detail2.jpg</c:if>"
                                 alt="头像" class="tuan_header"/>
                            <span>${join.nickname }</span>
                            <c:if test="${join.joinType == 1 }">
                                <c:set var="joinId" value="${join.id }"></c:set>
                                <c:set var="groupBuyId" value="${join.groupBuyId }"></c:set>
                                <span class="tuan-badge">团长</span>
                            </c:if>
                        </div>
                        <div class="color878"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${join.joinTime }"/>开团</div>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
    </div>
    <div class="group_other">
        <h2>其他团购</h2>
        <ul class="other_list">
            <c:if test="${!empty productList }">
                <c:forEach var="product" items="${productList }">
                    <li class="list_item" onclick="javascript:location.href='/mallPage/${product.id}/${product.shopId}/79B4DE7C/phoneProduct.do'">
                        <div class="list_item_pic box"><img src="${imgHttp}${product.image_url}" alt="团购图片" class=""/></div>
                        <div class="list_item_txt">
                            <c:if test="${!empty product.pro_label && product.pro_label != ''}">
                                <i class="bao_logo">${product.pro_label }</i>
                            </c:if>
                                ${product.pro_name}
                        </div>
                        <div class="list_item_price flex">
                            <div class="flex-1">${product.peopleNum }人团：￥<span class="font35 price_span">${product.price}</span></div>
                            <c:if test="${product.old_price > product.price }">
                                <div class="color878 line_txt">￥${product.old_price}</div>
                            </c:if>
                        </div>
                        <div class="list_item_time flex">
                            <div>距离结束</div>
                            <div class="flex-1 time-item">
                                <span id="day_show" class="day_show">0</span>天
                                <span id="hour_show" class="hour_show">0</span>时
                                <span id="minute_show" class="minute_show">0</span>分
                                <span id="second_show" class="second_show">0</span>秒
                                <input type="hidden" class="diffTimes" value="${product.times }">
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
    </div>
    <div class="footer flex">
        <c:if test="${isMember == 0}"><!-- 用户还没参团或开团 -->
        <div class="flex-1"><a href="/mGroupBuy/${shopId }/79B4DE7C/groupbuyall.do" class="footer_left">更多拼团</a></div>
        <div class="flex-1" onclick="productBuy();"><a href="javascript:;" class="footer_right">我也要参团</a></div>
        </c:if>
        <c:if test="${isMember == 1}"><!-- 用户已经参团或开团 -->
        <div class="flex-1"><a href="javascript:void(0);" onclick="details();" class="footer_left">查看订单详情</a></div>
        <c:if test="${productMap.isEnd != -1 && productMap.peopleNum-joinList.size() > 0}">
            <div class="flex-1" onclick="show();"><a href="javascript:;" class="footer_right">邀请好友参团</a></div>
        </c:if>
        </c:if>
    </div>

</div>
<form id="queryForm" method="post" action="/phoneOrder/79B4DE7C/toOrder.do?uId=${userid }">
    <input type="hidden" id="json" name="data">
    <input type="hidden" id="type" name="type" value="0">
</form>
<form id="buyForm">
    <input type="hidden" name="product_id" value="${productMap.id }"/>
    <input type="hidden" name="shop_id" value="${productMap.shopId }"/>
    <input type="hidden" name="product_specificas" value="<c:if test='${productMap.is_specifica eq 1 }'>${guige.xids }</c:if>"/>
    <input type="hidden" name="product_speciname" value="<c:if test='${productMap.is_specifica eq 1 }'>${guige.specifica_name }</c:if>"/>
    <input type="hidden" name="totalnum" value="1"/>
    <input type="hidden" name="totalprice" value="${productMap.price }"/>
    <input type="hidden" name="price" class="price" value="${productMap.price }"/>
    <input type="hidden" name="shop_name" value="${shopMap.name1 }"/>
    <input type="hidden" name="product_name" class="product_name" value="${productMap.pro_name }"/>
    <input type="hidden" name="image_url" value="${imgHttp }${productMap.image_url }"/>
    <input type="hidden" name="primary_price" class="primary_price" value="${productMap.old_price }"/>
    <input type="hidden" name="pro_code"
           value="<c:if test="${productMap.is_specifica eq 1 }">${productMap.inv_code }</c:if><c:if test="${productMap.is_specifica ne 1 }">${productMap.pro_code }</c:if>"/>
    <input type="hidden" name="return_day" value="${productMap.return_day }"/>
    <input type="hidden" name="discount" value="${discount }"/>
    <input type="hidden" name="groupType" value="1"/>
    <input type="hidden" name="groupBuyId" value="${groupBuyId }"/>
    <input type="hidden" name="pJoinId" value="${joinId }"/>
</form>
<input class="groupBuyCount" type="hidden" value="<c:if test='${!empty groupBuyCount }'>${groupBuyCount }</c:if>"/>
<input class="maxNum" type="hidden" value="${productMap.maxNum }">
<!--底部开始-->
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="memberId" value="${memberId }"/>
<!--底部结束-->
<input type="hidden" class="isWx" value="${isWx }"/>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>

<%--<script src="/js/jquery.min.js"></script>--%>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script type="text/javascript">
    var url = window.location.href;
    var proName = $("input[name='product_name']").val();
    var imgUrls = $("input[name='image_url']").val();
    var chaPeopleNum = $(".chaPeopleNum").val();
    var price = $(".price").val();
    var primary_price = $(".primary_price").val();
    var product_name = $(".product_name").val();
    if (chaPeopleNum == null || chaPeopleNum == "" || typeof(chaPeopleNum) == "undefined") {
        chaPeopleNum = 0;
    }
    var titles = '还差' + chaPeopleNum + '人、我' + price + '元团了' + product_name + '商品';
    if (price != null && price != "" && primary_price != "" && primary_price != null) {
        var sPrice = primary_price * 1 - price * 1;
        if (sPrice > 0) {
            sPrice = sPrice.toFixed(2);
            titles += '、立省' + sPrice + '元';
        }
    }
    titles += '、快来参团';
    $(".price_span").each(function () {
        var val = $(this).text();
        if (val != null && val != "" && typeof(val) != "undefined") {
            val = (val * 1).toFixed(2);

            var spli = val.split(".");
            if (spli.length == 2) {
                $(this).html(spli[0] + "<em>." + spli[1] + "</em>");
            }
        }
    });
    $(window).load(function () {
        var a = $(window).width(),
            b = $(window).height(),
            d = 870,
            meta = $("#meta");
        setTimeout(function () {
            meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
            $(".loading").hide();

            if ("${isMember}" == "1" && joinNum > 0 && "${productMap.isEnd}" != "-1") {
                show();
            }
        }, 100);
    });
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${record.get('appid')}", // 必填，公众号的唯一标识
        timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
        nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
        signature: "${record.get('signature')}",// 必填，签名，见附录1
        jsApiList: ['showAllNonBaseMenuItem', "onMenuShareTimeline", "onMenuShareAppMessage"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });


    wx.ready(function () {
        wx.showAllNonBaseMenuItem();

        //分享到朋友圈
        wx.onMenuShareTimeline({
            title: titles, // 分享标题
            link: url, // 分享链接
            imgUrl: imgUrls, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
        //分享给朋友
        wx.onMenuShareAppMessage({
            title: titles, // 分享标题
            desc: '我参加了"' + proName + '"', // 分享描述
            link: url, // 分享链接
            imgUrl: imgUrls, // 分享图标
            type: '', // 分享类型,music、video或link，不填默认为link
            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    });

    var html = "";
    var len = $("span.member-item").length;
    var joinNum = $(".chaPeopleNum").val() * 1;
    if (joinNum > 0) {
    } else {
        $(".wait_logo").css("background", "url(/images/mall/img/41.png) no-repeat -10px -10px");
    }
    var diffTimes = $(".diffTimes").val();
    if (diffTimes > 0 && "${productMap.isEnd}" != "-1") {
        //团购倒计时
        var timeIndex = window.setInterval(function () {
            $(".time-item").each(function () {
                var intDiff = $(this).find(".diffTimes").val() * 1;

                var times = timer(intDiff);

                $(this).find('#day_show').html(times[0]);
                $(this).find('#hour_show').html(times[1]);
                $(this).find('#minute_show').html(times[2]);
                $(this).find('#second_show').html(times[3]);

                $(this).find(".intDiff").val(intDiff - 1);
            });

        }, 1000);
    } else {
        $(".time-box .title").html("已结束");
        $(".time.flex").hide();
        //$(".count-time").hide();
    }
    function show() {
        //$(".fade").show();
        //$(".share").show();
        var isWx = $("input.isWx").val();
        if (isWx == "1") {
            alert("请点击右上角，把团购信息分享给朋友或朋友圈");
        } else {
            alert("请把链接分享给好友参团");
        }
    }

    function timer(intDiff) {
        var day = 0,
            hour = 0,
            minute = 0,
            second = 0;
        if (intDiff > 0) {
            day = Math.floor(intDiff / (60 * 60 * 24));
            hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
            minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
            second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
        } else {
            //$(".count-time").hide();
        }
        if (minute <= 9) minute = '0' + minute;
        if (second <= 9) second = '0' + second;

        var times = Array();
        times[0] = day;
        times[1] = hour;
        times[2] = minute;
        times[3] = second;
        return times;
    }

    //立即开团
    function productBuy() {
        var flag = true;
        var groupBuyCount = $(".groupBuyCount").val();
        var maxNum = $(".maxNum").val();
        if (maxNum != null && maxNum != "") {
            maxNum = maxNum * 1;
            groupBuyCount = groupBuyCount != null && groupBuyCount != "" ? groupBuyCount : 0;
            if (maxNum > 0) {
                if (maxNum <= groupBuyCount * 1) {
                    flag = false;
                    alert("每人限购" + maxNum + "件，您已超过每人购买次数");
                }
            }
        }
        var memberId = $(".memberId").val();
        if (memberId == null || memberId == "" || typeof(memberId) == "undefined") {
            toLogin();
            return false;
        }
        if (flag) {
            var hs = {};
            $("#buyForm input").each(function () {
                hs[$(this).attr("name")] = $(this).val();
            });
            $("#json").val(JSON.stringify(hs));
            $("#queryForm").submit();
        }
    }
    function togger() {
        var obj = $(".group-msg");
        //console.log(obj.css("display"))
        if (obj.css("display") == "none") {
            obj.show();
            $("p.detail span").attr("class", "sq");
            $("p.detail em").html("收起全部参团详情");
        } else {
            obj.hide();
            $("p.detail span").attr("class", "zk");
            $("p.detail em").html("展开全部参团详情");
        }
    }
    function details() {
        var memberId = $(".memberId").val();
        if (memberId == null || memberId == "" || typeof(memberId) == "undefined") {
            toLogin();
            return false;
        }
        location.href = "/phoneOrder/79B4DE7C/orderDetail.do?orderId=${orderId }&uId=${userid}";
    }
</script>
</body>
</html>