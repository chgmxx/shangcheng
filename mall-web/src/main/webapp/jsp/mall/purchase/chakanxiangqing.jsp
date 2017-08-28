<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/laydate/skins/lan/laydate.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/plugin/layer/layer.js" type="text/javascript" charset="utf-8"></script>
    <title>查看详情</title>
</head>
<body>
<div class="warp">
    <div class="top-back">
        <a class="top-back-icon" href="#" onClick="location.href='/purchaseOrder/orderIndex.do'"></a>
    </div>
    <h4 class="second-title">报价单状态</h4>
    <div class="stepflex" id="sflex03">
        <dl class="first" id="one">
            <dt class="s-num">1</dt>
            <dd class="s-text">
                1.待发布<s></s><b></b>
            </dd>
        </dl>
        <dl class="normal" id="two">
            <dt class="s-num">2</dt>
            <dd class="s-text">
                2.待付款<s></s><b></b>
            </dd>
        </dl>
        <dl class="normal" id="three">
            <dt class="s-num">3</dt>
            <dd class="s-text">
                3.已付款<s></s><b></b>
            </dd>
        </dl>
        <dl class="normal" id="four">
            <dt class="s-num">4</dt>
            <dd class="s-text">
                4.已完成<s></s><b></b>
            </dd>
        </dl>
    </div>
    <h4 class="second-title">报价单状态</h4>

    <table class="deatil-table">
        <tr>
            <th colspan="10" style="font-size: 16px; padding: 15px 0;">${company.companyName}报价单</th>
        </tr>
        <tr style="background-color: #f9f9f9;">
            <th colspan="10">报价信息</th>
        </tr>
        <tbody>
        <tr>
            <td colspan="1">报价单号</td>
            <td colspan="4">${order.orderNumber}</td>
            <td colspan="1">公司信息</td>
            <td colspan="4">${company.companyName}</td>
        </tr>
        <tr>
            <td colspan="1">标题</td>
            <td colspan="4">${order.orderTitle}</td>
            <td colspan="1">付款方式</td>
            <td colspan="4">${order.orderType==0?"全款":"分期"}</td>
        </tr>
        <tr>
            <td colspan="1">是否在线签合同</td>
            <td colspan="4">${order.haveContract==0?"签":"不签"}</td>
            <td colspan="1">对外报价是否含税</td>
            <td colspan="4">${order.haveTax==0?"含":"不含"}</td>
        </tr>
        <tr>
            <td colspan="1">报价单描述</td>
            <td colspan="9" style="max-width: 800px; padding: 15px 0;">${order.orderDescribe}</td>
        </tr>
        <tr>
            <td colspan="1">报价单说明</td>
            <td colspan="9" style="max-width: 800px; padding: 15px 0;">${order.orderExplain}</td>
        </tr>
        <tr>
            <td colspan="1">报价单备注</td>
            <td colspan="9" style="max-width: 800px; padding: 15px 0;">${order.orderRemarks}</td>
        </tr>
        <tr>
            <td colspan="1">创建时间</td>
            <td colspan="9">
                <fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
        </tr>
        <tr>
            <td>商品图片</td>
            <td>商品名称</td>
            <td>原价(￥)</td>
            <td>优惠价(￥)</td>
            <td>数量</td>
            <td>人工费(￥)</td>
            <td>安装费(￥)</td>
            <td>小计</td>
            <td>运费</td>
            <td>状态</td>
        </tr>
        <c:forEach var="details" items="${orderdetails}"
                   varStatus="orderIndex">
            <tr>
                <td><img src="${details.productImg}" class="detail-table-img"/></td>
                <td>${details.productName}</td>
                <td><fmt:formatNumber type="number" value="${details.money}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber type="number" value="${details.discountMoney}" pattern="0.00" maxFractionDigits="2"/></td>
                <td>${details.count}</td>
                <td><fmt:formatNumber type="number" value="${details.laborCost}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber type="number" value="${details.installationFee}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber type="number" value="${details.allMoney}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber type="number" value="${details.freight}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><c:if test="${order.orderStatus==0}">已关闭</c:if> <c:if
                        test="${order.orderStatus==1}">待发布</c:if> <c:if
                        test="${order.orderStatus==2}">待付款</c:if> <c:if
                        test="${order.orderStatus==3}">已付款</c:if> <c:if
                        test="${order.orderStatus==4}">已完成</c:if></td>
            </tr>
            <c:set value="${details.laborCost+laborCosts}" var="laborCosts"></c:set>
            <c:set value="${details.installationFee+installationFees}" var="installationFees"></c:set>
            <c:set value="${details.allMoney+allMoneys}" var="allMoneys"></c:set>
        </c:forEach>
        <tr>
            <td>合计</td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><fmt:formatNumber type="number" value="${laborCosts}" pattern="0.00" maxFractionDigits="2"/></td>
            <td><fmt:formatNumber type="number" value="${installationFees}" pattern="0.00" maxFractionDigits="2"/></td>
            <td><fmt:formatNumber type="number" value="${allMoneys}" pattern="0.00" maxFractionDigits="2"/></td>
            <td></td>
            <td></td>
        </tr>
        <tr class="text-right">
            <td colspan="10" style="padding-right: 20px; border-bottom: none;">总运费：
                ￥<fmt:formatNumber type="number" value="${order.freight}" pattern="0.00" maxFractionDigits="2"/>
                &nbsp;&nbsp;&nbsp;合计： ￥<fmt:formatNumber type="number" value="${order.allMoney}" pattern="0.00" maxFractionDigits="2"/>
            </td>
        </tr>
        <c:if test="${order.orderType==1}">
            <tr>
                <td
                        style="font-weight: bold; text-align: center; border-bottom: 2px solid #000000; border-top: 2px solid #000000; background-color: #F9F9F9;"
                        colspan="10">分期信息
                </td>
            </tr>
            <tr>
                <td colspan="2">期数</td>
                <td colspan="3">付款金额</td>
                <td colspan="3">付款时间</td>
                <td colspan="2">状态</td>
            </tr>
            <c:forEach items="${termList}" var="term" varStatus="termIndex">
                <tr>
                    <td colspan="2">第${termIndex.index+1}期</td>
                    <td colspan="3">￥<fmt:formatNumber type="number" value="${term.termMoney}" pattern="0.00" maxFractionDigits="2"/></td>
                    <td colspan="3"><fmt:formatDate value="${term.termTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td colspan="2">${term.termBuy==1?"已付款":"未付款"}</td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
    <div class="text-center mar-t20">

        <c:if test="${order.orderStatus==1}">
            <input type="button" class="gt-btn blue middle mar-r20" onclick="openConfirm(2)" value="发布报价单"/>
        </c:if>
        <c:if test="${order.orderStatus==2}">
            <input type="button" class="gt-btn blue middle mar-r20" onclick="openConfirm(3)" value="确认收款"/>
        </c:if>
        <c:if test="${order.orderStatus==3}">
            <input type="button" class="gt-btn blue middle mar-r20" onclick="openConfirm(4)" value="完成订单"/>
        </c:if>
        <c:if test="${order.orderStatus==1}">
            <input type="button" class="gt-btn default middle" onclick="openConfirm(0)" value="关闭订单"/>
        </c:if>


    </div>
    <jsp:include page="/jsp/common/headerCommon.jsp"/>
    <script type="text/javascript">
        $(function () {
            var status = "${order.orderStatus}";
            if (status == 0) {
                $('#sflex03').find("#one").addClass("last");
                $('#sflex03').find("#two").addClass("last");
                $('#sflex03').find("#three").addClass("last");
                $('#sflex03').find("#four").addClass("last");
            } else if (status == 1) {
                $('#sflex03').find("#one").addClass("doing");
                $('#sflex03').find("#two").addClass("last");
                $('#sflex03').find("#three").addClass("last");
                $('#sflex03').find("#four").addClass("last");
            } else if (status == 2) {
                $('#sflex03').find("#one").addClass("done");
                $('#sflex03').find("#two").addClass("doing");
                $('#sflex03').find("#three").addClass("last");
                $('#sflex03').find("#four").addClass("last");
            } else if (status == 3) {
                $('#sflex03').find("#one").addClass("done");
                $('#sflex03').find("#two").addClass("done");
                $('#sflex03').find("#three").addClass("doing");
                $('#sflex03').find("#four").addClass("last");
            } else if (status == 4) {
                $('#sflex03').find("#one").addClass("done");
                $('#sflex03').find("#two").addClass("done");
                $('#sflex03').find("#three").addClass("done");
                $('#sflex03').find("#four").addClass("doing");
            }
            loadWindow();
        });

        var winParents = window.parent;
        var winScreen = window.screen;
        var leftNav = $(window.parent.document.getElementById('dao-hang0')).height();

        function loadWindow() {
            //清空内容高度
            var rightHeight = document.body.scrollHeight;//网页正文全文高，包括有滚动条时的未见区域
            if (rightHeight > leftNav) {
                leftHeight = rightHeight;
            } else {
                leftHeight = leftNav;
            }
            var screenHeight = winScreen.availHeight;//屏幕可用工作区的高
            if (leftHeight < screenHeight) {
                leftHeight = screenHeight;
            }
            //改变内容高度
            $(winParents.document.getElementById('ifr')).height(leftHeight);
            $(winParents.document.getElementById('content')).height(leftHeight);
            //改变菜单栏的高度
            $(winParents.document.getElementById('nav')).height(leftHeight + 167);
        }


        /**打开一个询问框**/
        function openConfirm(status) {
            //询问框
            SonScrollTop(0);
            setTimeout(function () {
                window.layer.confirm("确定要修改报价单状态吗?", {
                    shade: [0.1, '#fff'],
                    btn: ['确定', '取消'],
                    offset: scrollHeight + "px",
                }, function () {
                    $.ajax({
                        url: "/purchaseOrder/editStatus.do?orderId=${order.id}&status=" + status,
                        type: "POST",
                        async: false,
                        dataType: "JSON",
                        success: function (data) {
                        }
                    });
                    window.layer.closeAll();
                    location.href = "/purchaseOrder/orderIndexDetail.do?orderId=${order.id}";
                }, function () {
                });
            }, timeout);
        }
    </script>
</div>
</body>
</html>
