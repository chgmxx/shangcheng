<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="/css/mall/purchase/phone/index.css">
    <link rel="stylesheet" type="text/css" href="/css/weui-master/dist/style/weui.css"/>
    <title>确认订单</title>
</head>
<body>
<div class="warp">
    <div class="weui-cells weui-cells_checkbox">
        <c:if test="${payType==1}">
            <label class="weui-cell weui-check__label" for="s11">
                <div class="weui-cell__hd">
                    <input type="radio" class="weui-check quotes_check" name="checkbox1" id="s11" value="1" ${card!=null && card.ctId==3?"":"checked='checked'"}>
                    <i class="weui-icon-checked"></i>
                </div>
                <div class="weui-cell__bd">
                    <p>微信支付</p>
                </div>
            </label>
        </c:if>
        <c:if test="${payType==0}">
            <label class="weui-cell weui-check__label" for="s11">
                <div class="weui-cell__hd">
                    <input type="radio" name="checkbox1" class="weui-check quotes_check" value="0"  ${card!=null && card.ctId==3?"":"checked='checked'"} id="s11">
                    <i class="weui-icon-checked"></i>
                </div>
                <div class="weui-cell__bd">
                    <p>支付宝</p>
                </div>
            </label>
        </c:if>
        <c:if test="${card!=null && card.ctId==3}">
            <label class="weui-cell weui-check__label" for="s13">
                <div class="weui-cell__hd">
                    <input type="radio" class="weui-check quotes_check" name="checkbox1" id="s13" value="5" checked="checked">
                    <i class="weui-icon-checked"></i>
                </div>
                <div class="weui-cell__bd">
                    <p>储值卡支付</p>
                </div>
            </label>
        </c:if>
        <c:if test="${order.orderType==0}">
            <label class="weui-cell weui-check__label" for="s12">
                <div class="weui-cell__hd">
                    <input type="radio" class="weui-check quotes_check" name="checkbox1" id="s12" value="4">
                    <i class="weui-icon-checked"></i>
                </div>
                <div class="weui-cell__bd">
                    <p>货到付款</p>
                </div>
            </label>
        </c:if>
    </div>
    <c:if test="${!empty member.mcId}">
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell weui-cell_switch">
                <div class="weui-cell__bd">
                    <p>积分</p>
                    <p class="quotes_tips">${!empty member.integral && member.integral>0?"":"无"}可用</p>
                </div>
                <div class="weui-cell__ft">
                    <input class="weui-switch quotes_switch" type="checkbox"
                           id="jifenCheckBox" onclick="jisuan()" name="checkbox2"
                        ${!empty member.integral && member.integral>0?"":"disabled='true'"}
                           value=""/>
                </div>
            </div>
            <div class="weui-cell weui-cell_switch">
                <div class="weui-cell__bd">
                    <p>粉币</p>
                    <p class="quotes_tips">${!empty member.fansCurrency && member.fansCurrency>0?"":"无"}可用</p>
                </div>
                <div class="weui-cell__ft">
                    <input class="weui-switch quotes_switch" type="checkbox"
                           id="fenbiCheckBox" onclick="jisuan()" name="checkbox2"
                        ${!empty member.fansCurrency && member.fansCurrency>0?"":"disabled='true'"}
                           value=""/>
                </div>
            </div>
        </div>
    </c:if>

    <div class="weui-cells">
        <c:if test="${!empty member.mcId}">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>积分</p>
                </div>
                <div class="weui-cell__ft" id="jifen">-￥0.00元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>粉币</p>
                </div>
                <div class="weui-cell__ft" id="fenbi">-￥0.00元</div>
            </div>
        </c:if>
        <c:if test="${card.ctId==2}">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>折扣数</p>
                </div>
                <div class="weui-cell__ft">${grDiscount}折</div>
            </div>
        </c:if>
        <c:if test="${order.orderType==0}">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>运费</p>
                </div>
                <div class="weui-cell__ft">￥<fmt:formatNumber type="number" value="${order.freight}" pattern="0.00" maxFractionDigits="2"/>元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>报价单总额</p>
                </div>
                <div class="weui-cell__ft">￥<fmt:formatNumber type="number" value="${order.allMoney}" pattern="0.00" maxFractionDigits="2"/>元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>应付金额</p>
                </div>
                <div class="weui-cell__ft" id="actualMoney" style="color: red; font-weight: bold;">
                    ￥<fmt:formatNumber type="number" value="${actualMoney}" pattern="0.00" maxFractionDigits="2"/>元
                </div>
            </div>
        </c:if>
        <c:if test="${order.orderType==1}">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>运费</p>
                </div>
                <div class="weui-cell__ft">￥<fmt:formatNumber type="number" value="${order.freight}" pattern="0.00" maxFractionDigits="2"/>元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>本期金额</p>
                </div>
                <div class="weui-cell__ft">￥<fmt:formatNumber type="number" value="${nowMoney}" pattern="0.00" maxFractionDigits="2"/>元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>下期金额</p>
                </div>
                <div class="weui-cell__ft">￥<fmt:formatNumber type="number" value="${nextMoney}" pattern="0.00" maxFractionDigits="2"/>元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>剩余尾款</p>
                </div>
                <div class="weui-cell__ft">￥<fmt:formatNumber type="number" value="${retainage}" pattern="0.00" maxFractionDigits="2"/>元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>报价单总额</p>
                </div>
                <div class="weui-cell__ft">￥<fmt:formatNumber type="number" value="${order.allMoney}" pattern="0.00" maxFractionDigits="2"/>元</div>
            </div>
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <p>应付金额</p>
                </div>
                <div class="weui-cell__ft" id="actualMoney"
                     style="color: red; font-weight: bold;">￥<fmt:formatNumber type="number" value="${actualMoney}" pattern="0.00" maxFractionDigits="2"/>元
                </div>
            </div>
        </c:if>
    </div>
    <div class="weui-btn-area">
        <c:if test="${order.orderStatus==0}">
            <a class="weui-btn quotes_btn_primary" href="javascript:void(0)">已关闭</a>
        </c:if>
        <c:if test="${order.orderStatus==1}">
            <a class="weui-btn quotes_btn_primary" href="javascript:void(0)">待审核</a>
        </c:if>
        <c:if test="${order.orderStatus==2}">
            <c:if test="${!empty nowMoney && nowMoney!=0 && nowMoney!='0'}">
                <a class="weui-btn quotes_btn_primary" href="javascript:void(0)"
                   id="showTooltips" onclick="cgPay()">确认付款</a>
            </c:if>
            <c:if test="${empty nowMoney || nowMoney==0 || nowMoney=='0'}">
                <a class="weui-btn quotes_btn_primary" href="javascript:void(0)">已付款</a>
            </c:if>
        </c:if>
        <c:if test="${order.orderStatus==3}">
            <a class="weui-btn quotes_btn_primary" href="javascript:void(0)">已付款</a>
        </c:if>
        <c:if test="${order.orderStatus==4}">
            <a class="weui-btn quotes_btn_primary" href="javascript:void(0)">已完成</a>
        </c:if>
    </div>

</div>

<div class="error" id="error"></div>
<script src="/js/plugin/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="/js/mall/purchase/phone/index.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    //支付方法
    var jifen = 0.0; //使用的积分
    var fenbi = 0.0; //使用的粉币
    function cgPay() {
        if (ok0()) {
            var money = "${nowMoney}";
            var memberId = "${member.id}";
            var discountmoney = jisuan();
            var discount = "${grDiscount}";
            var paymentType = "";
            var termId = "${termId}";
            var orderId = "${order.id}";
            var busId = "${order.busId}";
            var payType = $('input[name="checkbox1"]:checked').val();
            if ((payType == 1 || payType == "1") && discountmoney > 0) {//微信支付
                window.location.href = "/wxPay/79B4DE7C/cgPay.do?memberId=" + memberId + "&money=" + money + "&discountmoney=" + discountmoney + "&orderId=" + orderId + "&fenbi=" + fenbi + "&jifen=" + jifen + "&discount=" + discount + "&paymentType=" + payType + "&busId=" + busId + "&termId=" + termId;
            } else if ((payType == 0 || payType == "0") && discountmoney > 0) {//支付宝支付
                $.ajax({
                    url: "/purchasePhone/79B4DE7C/aliCgPay.do",
                    data: {
                        "memberId": memberId,
                        "discountmoney": discountmoney,
                        "orderId": orderId,
                        "money": money,
                        "fenbi": fenbi,
                        "jifen": jifen,
                        "discount": discount,
                        "paymentType": payType,
                        "busId": busId,
                        "termId": termId
                    },
                    type: "POST",
                    dataType: "JSON",
                    success: function (data) {
                        if (data.result == true) {
                            window.location.href = data.red_url;
                        } else {
                            alert(data.message);
                        }
                    }
                })
            } else if ((payType == 5 || payType == "5") && discountmoney > 0) {//余额支付
                var yue = "${card.money}";
                if (discountmoney > yue) {
                    alert("储值卡余额不足!");
                    return;
                }
                $.ajax({
                    url: "/purchasePhone/79B4DE7C/addReceivables.do",
                    data: {
                        "memberId": memberId,
                        "orderId": orderId,
                        "money": discountmoney,
                        "fansCurrency": fenbi,
                        "integral": jifen,
                        "discount": discount,
                        "buyMode": payType,
                        "busId": busId,
                        "termId": termId
                    },
                    type: "POST",
                    dataType: "JSON",
                    success: function (data) {
                        if (data == true || data == "true") {
                            alert("支付成功!");
                            window.location.href = "/purchasePhone/79B4DE7C/findOrder.do?orderId=" + orderId;
                        } else {
                            alert("支付失败!");
                        }
                    }
                })
            } else if ((payType == 4 || payType == "4") && discountmoney > 0) {//货到付款
                $.ajax({
                    url: "/purchasePhone/79B4DE7C/addReceivables.do",
                    data: {
                        "memberId": memberId,
                        "orderId": orderId,
                        "money": 0,
                        "buyMode": payType,
                        "busId": busId,
                        "termId": termId
                    },
                    type: "POST",
                    dataType: "JSON",
                    success: function (data) {
                        if (data == true || data == "true") {
                            alert("支付成功!");
                            window.location.href = "/purchasePhone/79B4DE7C/findOrder.do?orderId=" + orderId;
                        } else {
                            alert("支付失败!");
                        }
                    }
                })
            } else {
                $.ajax({
                    url: "/purchasePhone/79B4DE7C/addReceivables.do",
                    data: {
                        "memberId": memberId,
                        "orderId": orderId,
                        "money": discountmoney,
                        "fansCurrency": fenbi,
                        "integral": jifen,
                        "discount": discount,
                        "buyMode": payType,
                        "busId": busId,
                        "termId": termId
                    },
                    type: "POST",
                    dataType: "JSON",
                    success: function (data) {
                        if (data == true || data == "true") {
                            alert("支付成功!");
                            window.location.href = "/purchasePhone/79B4DE7C/findOrder.do?orderId=" + orderId;
                        } else {
                            alert("支付失败!");
                        }
                    }
                })
            }
        }
    }

    <!-- 减法函数 -->
    function accClaer(arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2))
        n = (r1 >= r2) ? r1 : r2;
        return ((arg1 * m - arg2 * m) / m).toFixed(n);
    }

    <!-- 加法函数升级 -->
    function accSub(arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        n = (r1 >= r2) ? r1 : r2;
        return ((arg1 * m + arg2 * m) / m).toFixed(n);
    }

    function ok0() {
        var payType = $('input[name="checkbox1"]:checked').val();
        if (payType == null) {
            alert("请选择支付方式!");
            return false;
        }
        return true;
    }


    function jisuan() {
        var actualMoney = "${actualMoney}";
        var obj = document.getElementsByName('checkbox2');
        for (var i = 0; i < obj.length; i++) {
            if (obj[i].checked) {
                if (obj[i].id == "fenbiCheckBox") {
                    if (actualMoney != 0) {
                        $.ajax({
                            url: "/purchasePhone/79B4DE7C/currencyCount.do",
                            data: {
                                "actualMoney": actualMoney,
                                "fansCurrency": "${member.fansCurrency}"
                            },
                            async: false,
                            type: "POST",
                            dataType: "JSON",
                            success: function (data) {
                                fenbi = data;
                                actualMoney = accClaer(actualMoney, fenbi);
                                if (actualMoney == 0) {
                                    $("#jifenCheckBox").attr("disabled", true);
                                }
                            }
                        });
                    } else {
                        fenbi = 0.0;
                    }
                }
                if (obj[i].id == "jifenCheckBox") {
                    if (actualMoney != 0) {
                        $.ajax({
                            url: "/purchasePhone/79B4DE7C/integralCount.do",
                            data: {
                                "actualMoney": accClaer(actualMoney, fenbi),
                                "integral": "${member.integral}",
                                "busId": "${member.busid}"
                            },
                            async: false,
                            type: "POST",
                            dataType: "JSON",
                            success: function (data) {
                                jifen = data;
                                actualMoney = accClaer(actualMoney, jifen);
                                if (actualMoney == 0) {
                                    $("#fenbiCheckBox").attr("disabled", true);
                                }
                            }
                        });
                    } else {
                        jifen = 0.0;
                    }
                }

                if (obj[i].id == "jifenCheckBox") {
                    $("#jifen").html("-￥" + jifen + "元");
                }
                if (obj[i].id == "fenbiCheckBox") {
                    $("#fenbi").html("-￥" + fenbi + "元");
                }
            } else {
                if (actualMoney != 0 && ${member.integral} > 0) {
                    $("#jifenCheckBox").attr("disabled", false);
                }
                if (actualMoney != 0 && ${member.fansCurrency} > 0) {
                    $("#fenbiCheckBox").attr("disabled", false);
                }
                if (obj[i].id == "jifenCheckBox") {
                    $("#jifen").html("-￥0.00元");
                    jifen = 0.0;
                }
                if (obj[i].id == "fenbiCheckBox") {
                    fenbi = 0.0;
                    $("#fenbi").html("-￥0.00元");
                }
            }
        }
        $("#actualMoney").html("￥" + actualMoney + "元");
        return actualMoney;
    }


</script>
</body>
</html>