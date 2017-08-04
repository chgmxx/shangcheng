<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title></title>
    <meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/wholesalers/pcReset.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/wholesalers/pcWholesale.css?<%=System.currentTimeMillis()%>"/>
    <script src="/js/plugin/jquery-1.8.3.min.js?<%=System.currentTimeMillis()%>"></script>
</head>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<script type="text/javascript" src="/js/mall/mall_public.js"></script>
<body>
<div class="contentWarp">
    <div class="con-head">
        <a href="/mallWholesalers/index.do">批发管理</a>
        <a href="/mallWholesalers/wholesaleList.do">批发商管理</a>
        <a class="navColor" href="/mallWholesalers/toSetWholesale.do">批发设置</a>
    </div>

    <div class="title">
        <span class="i-con"></span>
        <p class="title-p">基础设置</p>
    </div>


    <table border="0" cellspacing="0" cellpadding="0" width="100%" class="saleTab2">
        <c:set var="isHpMoney" value="0"></c:set>
        <c:set var="hpMoney" value="0"></c:set>
        <c:set var="isHpNum" value="0"></c:set>
        <c:set var="hpNum" value="0"></c:set>
        <c:set var="isSpHand" value="0"></c:set>
        <c:set var="spHand" value="0"></c:set>
        <c:if test="${!empty pfSet }">
            <c:if test="${!empty pfSet.hpMoney }">
                <c:set var="hpMoney" value="${pfSet.hpMoney }"></c:set>
            </c:if>
            <c:if test="${!empty pfSet.hpNum }">
                <c:set var="hpNum" value="${pfSet.hpNum }"></c:set>
            </c:if>
            <c:if test="${!empty pfSet.spHand }">
                <c:set var="spHand" value="${pfSet.spHand }"></c:set>
            </c:if>
            <c:if test="${!empty pfSet.isHpMoney }">
                <c:set var="isHpMoney" value="${pfSet.isHpMoney }"></c:set>
            </c:if>
            <c:if test="${!empty pfSet.isHpNum }">
                <c:set var="isHpNum" value="${pfSet.isHpNum }"></c:set>
            </c:if>
            <c:if test="${!empty pfSet.isSpHand }">
                <c:set var="isSpHand" value="${pfSet.isSpHand }"></c:set>
            </c:if>
        </c:if>
        <tr>
            <td class="td1">混批条件：</td>
            <td class="td2">
                <p class="">
                    <label><input type="checkbox" name="condition" id="condition" value="1"
                                  <c:if test="${isHpMoney == 1 }">checked="checked"</c:if> />一次性购买商品金额达
                        <input type="text" name="" id="money" value="<c:if test="${hpMoney > 0 && isHpMoney == 1}">${hpMoney }</c:if>" class="iptTxt"/> 元(￥)
                    </label>
                </p>
                <p class="list">
                    <label><input type="checkbox" name="conditionNum" id="conditionNum" value="1"
                                  <c:if test="${isHpNum == 1 }">checked="checked"</c:if> />一次性购买数量达
                        <input type="text" name="" id="num" value="<c:if test="${hpNum > 0 && isHpNum == 1}">${hpNum }</c:if>" class="iptTxt"/> 件
                    </label>
                </p>
            </td>
        </tr>
        <tr>
            <td class="td1">手批条件：</td>
            <td class="td2">
                <label><input type="checkbox" name="shoupi" id="shoupi" value="1"
                              <c:if test="${isSpHand == 1 }">checked="checked"</c:if> />一次性购买商品达
                    <input type="text" class="iptTxt" name="" id="shou" value="<c:if test="${spHand > 0 && isSpHand == 1}">${spHand }</c:if>"/>手
                </label>
            </td>
        </tr>
        <tr>
            <td class="td1"></td>
            <td class="td2">
                <p style="color:red;">混批条件和手批条件必须得设置一种</p>
                <p style="color:red;">如果没有选择混批条件，我们会为您默认购买混批商品必须达到一件才能批发</p>
                <p style="color:red;">如果没有选择手批条件，我们会为您默认购买手批商品必须达到一手才能批发</p>
            </td>
        </tr>
        <tr height="30px"></tr>
        <tr>
            <td class="td1">批发商说明：</td>
            <td class="td2">
                <textarea name="pfRemark" id="pfRemark" rows="" cols=""><c:if test="${!empty paySet }">${paySet.pfRemark }</c:if></textarea>
            </td>
        </tr>
        <tr>
            <td class="td1">批发商申请说明：</td>
            <td class="td2">
                <textarea name="pfApplyRemark" id="pfApplyRemark" rows="" cols=""><c:if test="${!empty paySet }">${paySet.pfApplyRemark }</c:if></textarea>
            </td>
        </tr>
    </table>
    <div class="saleBot">
        <input type="button" id="blue-btn" value="保存" class="blue-btn"/>
        <input type="button" id="returnBtn" value="返回" class="green-btn"/>
    </div>
</div>
</body>
<script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/wholesalers/setWholesaler.js?<%=System.currentTimeMillis()%>"></script>
</html>