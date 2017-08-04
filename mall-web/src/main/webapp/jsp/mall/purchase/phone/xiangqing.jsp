<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/phone/index.css">
    <script src="/js/plugin/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="/js/mall/purchase/phone/index.js" type="text/javascript" charset="utf-8"></script>
    <title>分期详情</title>
    <style type="text/css">
        .color898 {
            color: #898989;
        }

        .warp_fenqi {
            background-color: #f5f5f9;
        }

        .total {
            background-color: #FFFFFF;
            padding: 0.2rem 0.25rem;
        }

        .total_sum {
            font-size: 0.8rem;
        }

        .fenqi_item_title {
            background-color: transparent;
            padding: 0.2rem 0.25rem;
        }

        .fenqi_item {
            padding: 0.2rem 0.25rem;
            background-color: #FFFFFF;
            border-bottom: 1px solid #e7e7e7;
        }

        .fenqi_item:last-child {
            border-bottom: none;
        }

        .fenqi_huan {
            background-color: #f0f0f0;
        }
    </style>
</head>
<body>
<div class="warp warp_fenqi">
    <div class="total">
        <p>分期剩余应还(元)</p>
        <p class="total_sum"><fmt:formatNumber type="number" value="${retainage}" pattern="0.00" maxFractionDigits="2"/></p>
        <p>分期总额${order.allMoney}${order.haveTax==1?"":"(含税)"}</p>
    </div>
    <div class="fenqi">
        <div class="fenqi_item_title flex">
            <div class="flex-1">分期明细</div>
            <div class="flex-1 color898 text-right">已还${index}期</div>
        </div>
        <c:forEach items="${termList}" var="term" varStatus="termStatus">

            <div class="fenqi_item ${term.termBuy==0?'':fenqi_huan}">
                <div class="flex">
                    <div class="flex-1 color898">${termStatus.index+1}/${termList.size()}</div>
                    <div class="flex-1 color898 text-right">${term.termMoney}${order.haveTax==1?"":"(含税)"}</div>
                </div>
                <div class="flex">
                    <div class="flex-1 color898">还款日：
                        <fmt:formatDate value="${term.termTime}" pattern="yyyy-MM-dd"/>
                    </div>
                    <div class="flex-1 text-right">${term.termBuy==0?"未还清":"已还清"}</div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
