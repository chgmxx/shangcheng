<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<base href="<%=basePath%>"/>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
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
    <link rel="stylesheet" href="/css/mall/my/mallStyle.css?<%=System.currentTimeMillis()%>">
    <!-- <link rel="stylesheet" href="/css/trade/mall/my/public.css"> -->
    <link rel="stylesheet" href="/css/mall/public.css?<%=System.currentTimeMillis()%>">
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <title>我的</title>
</head>
<body>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<div class="mWrapper">
    <div class="m-con">
        <div class="m-main" style="width:12.8rem;overflow-x:hidden;">
            <header class="mHeader">
                <div class="mh-left" style="padding-top: 1rem;box-sizing: border-box;">
                    <img style="float: left;" src="<c:if test="${member != null && member.headimgurl != ''}">${member.headimgurl }</c:if>
                    <c:if test="${member == null || member.headimgurl == null || member.headimgurl == ''}">/images/mall/img/pt-detail2.jpg</c:if>"
                         alt="" class="myImg" onerror="this.src='/images/mall/img/pt-detail2.jpg'"
                    <c:if test="${member == null || empty member }"> onclick="toLogin();"</c:if> >
                    <!-- images/a.jpg -->
                    <div class="mh-info">
                        <c:if test="${member != null && !empty member }">
                            <p class="pdb mh-name">${member.nickname }</p>
                            <c:if test="${!empty member.mcId }">
                                <p class="mh-phone"><span>${gradeType.gtName }</span><span class="pdlr">${card.cardno }</span></p>
                            </c:if>
                            <c:if test="${empty member.mcId }">
                                <p class="mh-phone"><span>非会员</span><span class="pdlr"></span></p>
                            </c:if>
                        </c:if>
                        <c:if test="${member == null || empty member }">
                            <p class="pdb mh-name" style="height:2rem;line-height:2rem;margin-left:2.5rem;width:3rem;" onclick="toLogin();">登陆/注册</p>
                        </c:if>
                    </div>
                </div>
            </header>
            <nav class="mNav">
                <ul class="navUl flex-row-b ai">
                    <li>
                        <a href="javascript:void(0);" onclick="toOrder('type=1');" class="mBgTop dzf-icon">
                            待支付
                        </a>
                    </li>
                    <li>
                        <a href="javascript:void(0);" onclick="toOrder('type=2');" class="mBgTop dfh-icon">
                            待发货
                        </a>
                    </li>
                    <li>
                        <a href="javascript:void(0);" onclick="toOrder('type=3');" class="mBgTop dsh-icon">
                            待收货
                        </a>
                    </li>
                    <li>
                        <a href="javascript:void(0);" onclick="toOrder('appraiseStatus=0');" class="mBgTop dpj-icon">
                            待评价
                        </a>
                    </li>
                    <li>
                        <a href="javascript:void(0);" onclick="toAddress();" class="mBgTop addr-icon">
                            我的地址
                        </a>
                    </li>
                </ul>
            </nav>
            <ul class="itemUl">
                <li>
                    <a href="javascript:void(0);" onclick="toOrder('');" class="mBgLeft order-icon">我的订单</a>
                </li>
            </ul>
            <ul class="itemUl">
                <li>
                    <a href="javascript:void(0);" onclick="toMember();" class="mBgLeft hyk-icon">
                        我的会员卡
                    </a>
                </li>
                <li>
                    <a href="javascript:void(0);" onclick="toComment();" class="mBgLeft eval-icon">我的评价</a>
                </li>
                <li>
                    <a href="javascript:void(0);" onclick="toCollect();" class="mBgLeft col-icon">我的收藏</a>
                </li>
                <li>
                    <a href="http://m.kuaidi100.com/#input" class="mBgLeft exp-icon">快递查询</a>
                </li>
            </ul>
            <ul class="itemUl">
                <li>
                    <a href="javascript:void(0);" onclick="toBidding();" class="mBgLeft jp-icon">我的竞拍</a>
                </li>
                <li>
                    <a href="javascript:void(0);" onclick="toOrder('orderType=3');" class="mBgLeft sk-icon">我的秒杀</a>
                </li>
                <c:if test="${!empty isOpenPf }">
                    <li>
                        <a href="javascript:void(0)" onclick="pifaApplay('${status}',${member.id},'${shopid }');" class="mBgLeft ws-icon">我的批发</a>
                    </li>
                </c:if>
                <li>
                    <a href="javascript:void(0);" onclick="toOrder('orderType=1');" class="mBgLeft gp-icon">我的团购</a>
                </li>
                <li>
                    <a href="javascript:void(0);" onclick="toPresale();" class="mBgLeft gp-icon">我的预售</a>
                </li>
                <c:if test="${!empty isOpenSeller }">
                    <li>
                        <a href="javascript:void(0);" onclick="sellerApplay('${sellerStatus}',${member.id });" class="mBgLeft gp-icon">超级销售员</a>
                    </li>
                </c:if>
            </ul>
        </div>
        <c:if test="${!empty isAdvert }">
            <div class="isAdvert">
                <jsp:include page="/jsp/common/technicalSupport.jsp"></jsp:include>
            </div>
        </c:if>
    </div>
</div>
<jsp:include page="/jsp/mall/phoneFooterMenu.jsp"></jsp:include>
<input type="hidden" class="shopid" value="${shopid }"/>

<input type="hidden" class="saleMemberId" value="${saleMemberId }"/>
<input type="hidden" class="minCosumeMoney" value="${minCosumeMoney }"/>
<input type="hidden" class="consumeMoney" value="${consumeMoney }"/>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="memberId" value="${member.id}"/>
</body>
<%--<script type="text/javascript" src="/js/jquery.min.js"></script>--%>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script type="text/javascript">
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${record.get('appid')}", // 必填，公众号的唯一标识
        timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
        nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
        signature: "${record.get('signature')}",// 必填，签名，见附录1
        jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    var saleMemberId = $("input.saleMemberId").val();
    wx.ready(function () {
        wx.hideOptionMenu();
    });
    $(window).load(function () {
        setTimeout(function () {
            $(".loading").hide();
        }, 1000);
    });
    //批发申请跳转
    function pifaApplay(status, memberId, shopid) {
        if (memberId == null || memberId == "") {
            toLogin();
            return false;
        }
        if (status == null || status == "" || status == "-2") {
            location.href = "/phoneWholesaler/79B4DE7C/toApplyWholesaler.do?member_id=" + memberId + "&uId=" + userid;
        } else if (status == "0") {
            alert("您的批发商申请在审核中请耐心等待1-3个工作日");
        } else if (status == "1") {
            if (shopid == null || shopid == "" || typeof(shopid) == "undefined") {
                alert("您可前往商城首页购买批发商品");
            } else if (confirm("您是否前往商城购买批发商品？")) {
                location.href = "/phoneWholesaler/" + shopid + "/79B4DE7C/wholesalerall.do?uId=" + userid;
            }
        } else if (status == "-1") {
            if (confirm("您的批发商申请不通过，确认要重新申请？")) {
                location.href = "/phoneWholesaler/79B4DE7C/toApplyWholesaler.do?member_id=" + memberId + "&uId=" + userid;
            }
        }
    }
    //销售员申请跳转
    function sellerApplay(status, memberId) {
        if (memberId == null || memberId == "") {
            toLogin();
            return false;
        }
        var consumeMoney = $(".consumeMoney").val();
        var minCosumeMoney = $(".minCosumeMoney").val();
        if (status == null || status == "" || status == "-2" || status == "-4") {
            if (consumeMoney != "" || minCosumeMoney != "") {
                alert("加入超级销售员消费额必须要达到" + minCosumeMoney + "元，您的消费额只有" + consumeMoney + "元");
            } else {
                location.href = "/phoneSellers/79B4DE7C/toApplySeller.do?member_id=" + memberId + "&uId=" + userid;
            }
        } else if (status == "0") {
            alert("您的超级销售员申请在审核中请耐心等待1-3个工作日");
        } else if (status == "1") {
            location.href = "/phoneSellers/79B4DE7C/sellerIndex.do?member_id=" + memberId + "&uId=" + userid;
        } else if (status == "-1") {
            if (consumeMoney != "" || minCosumeMoney != "") {
                alert("您的超级销售员申请不通过，且消费额没有达到" + minCosumeMoney + "元，不能继续申请，您的消费额只有" + consumeMoney + "元");
            } else {
                if (confirm("您的超级销售员申请不通过，确认要重新申请？")) {
                    location.href = "/phoneSellers/79B4DE7C/toApplySeller.do?member_id=" + memberId + "&uId=" + userid;
                }
            }
        } else if (status == "-3") {
            alert("您的超级销售员已经被暂停了，不能继续使用");
        }
    }
    //订单
    function toOrder(param) {
        if (memberId != null && memberId != "") {
            if (param != null && param != "") {
                location.href = "/phoneOrder/79B4DE7C/orderList.do?uId=" + userid + "&" + param;
            } else {
                location.href = "/phoneOrder/79B4DE7C/orderList.do?uId=" + userid;
            }
        } else {
            toLogin();
        }
    }
    //我的地址
    function toAddress() {
        if (memberId != null && memberId != "") {
            location.href = "/phoneOrder/79B4DE7C/addressList.do?addressManage=1&uId=" + userid;
        } else {
            toLogin();
        }
    }
    //个人中心
    function toMember() {
        if (memberId != null && memberId != "") {
            location.href = "/phoneMemberController/" + userid + "/79B4DE7C/findMember_1.do";
        } else {
            toLogin();
        }
    }
    //我的评论
    function toComment() {
        if (memberId != null && memberId != "") {
            location.href = "/mMember/79B4DE7C/myAppraise.do?type=1&uId=" + userid;
        } else {
            toLogin();
        }
    }
    //我的收藏
    function toCollect() {
        if (memberId != null && memberId != "") {
            location.href = "/mMember/79B4DE7C/collect.do?uId=" + userid;
        } else {
            toLogin();
        }
    }
    //我的竞拍
    function toBidding() {
        if (memberId != null && memberId != "") {
            location.href = "/mAuction/79B4DE7C/myBidding.do?uId=" + userid;
        } else {
            toLogin();
        }
    }
    //我的预售
    function toPresale() {
        if (memberId != null && memberId != "") {
            location.href = "/phonePresale/79B4DE7C/myDeposit.do?uId=" + userid;
        } else {
            toLogin();
        }
    }
</script>
</html>