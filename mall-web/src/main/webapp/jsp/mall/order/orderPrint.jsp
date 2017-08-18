<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>订单打印</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>

    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/plugin/printer/lodopDriver.js"></script>
    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
            list-style: none;
        }

        .clearfix:after {
            content: "";
            display: block;
            visibility: hidden;
            height: 0;
            clear: both;
        }

        .order_layer {
            width: 700px;
            margin: 10px auto;
            border: 1px solid #dadada;
            font-size: 13px;
            background-color: #FFFFFF;
            z-index: 10005;
        }

        .order_list li {
            width: 50%;
            float: left;
            font-size: 13px;
        }

        .order_layer h2 {
            font-size: 18px;
            text-align: center;
            width: 100px;
            border-bottom: 2px solid #000;
            margin: 0 auto;
        }

        .order_layer h3 {
            font-size: 13px;
            text-align: center;
            font-weight: normal;
        }

        .order_tab {
            table-layout: fixed;
        }

        .order_layer_title {
            position: relative;
        }

        .order_layer_title .page_num {
            position: absolute;
            right: 5px;
            top: 2px;
            font-size: 12px;
        }

        .fade {
            position: fixed;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.4);
            z-index: 10002;
        }
    </style>
</head>
<body>
<div class="order_layer">
    <div class="order_layer_title">
        <h2>商城订单</h2>
        <c:if test="${result.totalPage > 1 }">
            <div class="page_num">共<span>${result.totalPage }</span>页 第<span>${result.curPage }</span>页</div>
        </c:if>
    </div>

    <h3>联系电话： ${result.phone }</h3>
    <div class="order_layer_main">
        <ul class="clearfix order_list">
            <li>客户名称：${result.memberName }</li>
            <li>所属店铺：${result.store }</li>
            <li>客户电话：${result.memberPhone }</li>
            <li>订单编号：${result.orderNum }</li>
            <li>客户地址：${result.memberAddress }</li>
            <li>下单时间：${result.orderTime }</li>
        </ul>
        <table border="1" cellspacing="0" cellpadding="0" width="100%" class="order_tab">
            <tr>
                <th width="130">商品条形码</th>
                <th width="265">商品名称</th>
                <th width="80">原价</th>
                <th width="46">数量</th>
                <th width="80">优惠</th>
                <th width="80">小计</th>
            </tr>
            <c:if test="${!empty result.lists }">
                <c:forEach var="list" items="${result.lists }">
                    <tr>

                        <td><c:if test="${!empty list.barCode }">${list.barCode }</c:if></td>
                        <td>${list.name }</td>
                        <td>${list.amount }</td>
                        <td>${list.num }</td>
                        <td>${list.disount }</td>
                        <td>${list.subtotal }</td>
                    </tr>
                </c:forEach>
            </c:if>
            <tr>
                <td colspan="3" style="border-right: none;">买家留言：${result.message }</td>
                <td colspan="3" style="border-left: none;">应收总额： ￥${result.totalAmount }</td>
            </tr>
        </table>
        <c:if test="${!empty result.deliveryType }">
            <div>配送方式：${result.deliveryType }</div>
        </c:if>
        <div>商家备注：<c:if test="${!empty result.remark}">${result.remark }</c:if></div>

    </div>
    <div class="page">
        <ul>
            <c:if test="${result.totalPage > 1 }">
                <li class="pgfocus current">第
                        ${result.curPage }页
                </li>
                <li class="pgfocus"><a href="javascript:void(0);" onclick="prevPage(${orderId},${result.prevPage});">上一页</a></li>
                <li class="pgfocus"><a href="javascript:void(0);" onclick="prevPage(${orderId},${result.nextPage});">下一页</a></li>
                <li class="pgfocus">共
                        ${result.totalPage }页
                </li>
            </c:if>
            <li class="pgfocus">
                <a href="javascript:void(0);" onclick="prints();" style="color: #48b9ef;">打印</a>
            </li>
            <li class="pgfocus">
                <a href="javascript:void(0);" onclick="printView();" style="color: #48b9ef;">打印预览</a>
            </li>
        </ul>
    </div>
</div>
<input type="hidden" class="urlPath" value="${urlPath }"/>
<input type="hidden" class="busId" value="${user.id }"/>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
</body>

<script type="text/javascript">
    var results = ${result};
    //results = JSON.parse(results);
    delete results.nextPage;
    delete results.prevPage;
    if (results.totalPage == 1) {
        delete results.totalPage;
        delete results.curPage;
    }

    var urlPath = $("input.urlPath").val();

    var busId = $("input.busId").val(); // 测试商家ID
    var moduleId = 3; // 测试板块
    // 测试地址
    var url = urlPath + "printerOpt/79B4DE7C/" + moduleId + "/" + busId + "/getPrinterTemplate.do";

    //打印
    function prints() {
        lodop_print(url, JSON.stringify(results));
    }
    //打印预览
    function printView() {
        lodop_prview(url, JSON.stringify(results));
    }
    function prevPage(orderId, page) {
		layer.open({
			type: 2,
		    title: "订单打印",
		    shadeClose: true,
            shade:[0.3,"#fff"],
		    offset: "20%",
		    shadeClose : false,
		    area: ["730px","360px"],
		    content: "mallOrder/toPrintMallOrder.do?orderId="+orderId+"&curPage="+page,
		    success: function(layero, index){
		    	var index = layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		    	layer.close(index);
		     }
		});
    }
</script>
</html>