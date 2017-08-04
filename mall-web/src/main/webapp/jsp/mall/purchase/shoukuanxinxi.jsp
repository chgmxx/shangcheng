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
    <title>收款信息</title>
</head>
<body>
<div class="warp">
    <div class="top-back"><a class="top-back-icon" href="#" onClick='javascript :history.back(-1);'></a></div>
    <div class="gt-bread-crumb">
        <span class="gt-bread-crumb-title">收款信息</span>
    </div>
    <h4 class="second-title cell-top-border">客户信息</h4>
    <ul class="shoukuan-info">
        <li>会员卡号： ${card.cardno}</li>
        <li>客人姓名： ${member.name}</li>
        <li>联系电话： ${member.phone}</li>
    </ul>
    <h4 class="second-title cell-top-border">客户付款信息</h4>
    <table class="gt-table">
        <thead class="gt-table-thead">
        <tr>
            <th>粉币</th>
            <th>积分</th>
            <th>折扣</th>
            <th>付款方式</th>
            <th>实际付款</th>
            <th>应付金额</th>
            <th>付款时间</th>
        </tr>
        </thead>
        <tbody class="gt-table-tbody">
        <c:forEach items="${receivablesList}" var="receivable">
            <tr>
                <td><fmt:formatNumber type="number" value="${receivable.fansCorrency}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber type="number" value="${receivable.integral}" pattern="0.00" maxFractionDigits="2"/></td>
                <td>${empty receivable.discount || receivable.discount==0?"无":receivable.discount}${empty receivable.discount || receivable.discount==0?"":"折"}</td>
                <td><c:if test="${receivable.buyMode==0}">
                    支付宝支付
                </c:if>
                    <c:if test="${receivable.buyMode==1}">
                        微信支付
                    </c:if>
                    <c:if test="${receivable.buyMode==4}">
                        货到付款(线下)
                    </c:if>
                    <c:if test="${receivable.buyMode==5}">
                        储值卡支付
                    </c:if></td>
                <td><fmt:formatNumber type="number" value="${receivable.money}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><fmt:formatNumber type="number" value="${order.orderType==0?order.allMoney:receivable.termMoney}" pattern="0.00" maxFractionDigits="2"/></td>
                <td><fmt:formatDate value="${receivable.buyTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
