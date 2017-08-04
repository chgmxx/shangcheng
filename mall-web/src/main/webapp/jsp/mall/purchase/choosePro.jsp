<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>选择商品</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/edit1.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/table.js"></script>
    <style>
        .srh {
            display: block;
            float: left;
            padding-left: 5px;
            width: 195px;
            z-index: 10;
            margin-right: 10px;
        }
    </style>
    <script type="text/javascript">
        /**点击确认**/
        function fnOk(_this) {
            var id = "";
            var name = "";
            var price = "";
            var url = "";
            var details = "";
            $(_this).parents("li").find("input").each(function () {
                if ($(this).attr("name") == "id") {
                    id = $(this).val();
                }
                if ($(this).attr("name") == "proName") {
                    name = $(this).val();
                }
                if ($(this).attr("name") == "proCostPrice") {
                    price = $(this).val();
                }
                if ($(this).attr("name") == "imageUrl") {
                    url = $(this).val();
                }
            });
            $(_this).parents("li").find("textarea").each(function () {
                if ($(this).attr("name") == "productDetail") {
                    details = $(this).val();
                }
            });
            var str = "<tr>"
                + "<td><input type='hidden' name='productId' value='" + id + "'><input type='hidden' name='money' value='" + price + "'><input type='hidden' name='productName' value='" + name + "'><input type='hidden' name='productImg' value='" + url + "'>	<textarea  name='productDetails' style='display: none'>" + details + "</textarea><img class='table-image' src='" + url + "' /></td>"
                + "<td>" + name + "</td>"
                + "<td>￥" + price + "</td>"
                + "<td><input type='input' name='discountMoney' onchange='reckonCount()'  onKeyUp='amount(this)' onBlur='overFormat(this)' value='" + price + "' class='commodity-input' /></td>"
                + "<td><input type='input' name='count'  value='1'  onchange='reckonCount()'  onkeyup='keyup1(this)' onafterpaste='keyup1(this)' class='commodity-input' /></td>"
                + "<td><input type='input' name='laborCost'  value='0.00' onchange='reckonCount()' value='0' onKeyUp='amount(this)' onBlur='overFormat(this)' class='commodity-input' /></td>"
                + "<td><input type='input' name='installationFee'  value='0.00' onchange='reckonCount()' value='0' onKeyUp='amount(this)' onBlur='overFormat(this)' class='commodity-input' /></td>"
                + "<td><input type='input' name='allMoney'  value='" + price + "' readonly='readonly' class='commodity-input' /></td>"
                + "<td><input type='input' name='freight'  value='0.00' class='commodity-input'  onchange='reckonCount()'  onKeyUp='amount(this)'  placeholder='只显示'/></td>"
                + "<td><i class='iconfont' title='删除'  onclick='removeTerm(this)'>&#xe649;</i></td>"
                + "</tr>";
            top.frames[0].$("#product").append(str);
            top.frames[0].reckonCount();
            top.frames[0].loadWindow();
            fnCancel();
        }

        function fnCancel() {
            closeWindow();
        }

        function closeWindow() {
            //当你在iframe页面关闭自身时
            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
            parent.layer.close(index); //再执行关闭
        }
    </script>
</head>
<body style="margin: 10px">
<div style="padding-bottom: 50px">
    <div class="txt-btn pd-bottom-15 clearfix">
        <div>
            <form id="queryForm" method="post" action="purchaseOrder/getProductByGroup.do">
                <input type="text" placeholder="请输商品名称(模糊匹配)" value="${!empty map?map.proName:''}" id="proName" name="proName" class="srh">
                <input type="submit" value="查询" style="width: 50px; cursor: pointer; background-color: #1aa1e7; border-radius: 3px; color: #fff; border: none;"/>
            </form>
        </div>
    </div>

    <div class="box-btm30">
        <c:if test="${!empty page && !empty page.subList}">
            <div class="txt-tle">
                <div class="t1">&nbsp;</div>
                <div class="t2" style="width: 40%;">商品名称</div>
                <div class="t3" style="width: 37%;">商品价格</div>
            </div>
            <div class="msg-list">
                <div class="txt-tle2" id="list">
                    <c:forEach items="${page.subList}" var="pro">
                        <li>
                            <div name="title" class="listb" style="width: 40%;">${pro.pro_name }</div>
                            <div class="listb" style="width: 30%; overflow: hidden;">￥${pro.pro_price }</div>
                            <div class="listb" style="width: 26%;">
                                <a href="javascript:void(0);" class="choosePro" onclick="fnOk(this)">选取</a>
                            </div>
                            <textarea name="productDetail" style="display: none;">${pro.product_detail}</textarea>
                            <input type="hidden" name="id" class="listb" value="${pro.id }">
                            <input type="hidden" name="proName" class="listb" value="${pro.pro_name }">
                            <input type="hidden" name="proCostPrice" class="listb" value="${pro.pro_price }">
                            <input type="hidden" name="imageUrl" class="listb" value="${http}${pro.image_url }">
                        </li>
                    </c:forEach>
                </div>
            </div>
        </c:if>
        <c:if test="${empty page || empty page.subList}">
            无商品可选
        </c:if>
    </div>
</div>
<c:if test="${!empty page && !empty page.subList}">
    <div style="width: 100%; height: 50px; text-align: center; position: fixed; bottom: 0; background: #fff;">
        <jsp:include page="/jsp/mall/pageView.jsp"></jsp:include>
    </div>
</c:if>
<script type="text/javascript">
    function page(curPage, url) {
        //获取查询参数,queryForm为表单ID
        /* var objName = $("#queryForm").find(".srh").eq(0).attr("name");
         var objValue = $("#queryForm").find(".srh").eq(0).val(); */
        var shopId = $('#shopId').val();
        var proName = $("#proName").val();
        url += curPage + "&shopId=" + shopId + "&proName=" + proName;
        var isCommission = $("input.isCommission").val();
        if (isCommission != null && isCommission != ""
            && typeof (isCommission) != "undefined") {
            url += "&isCommission=" + isCommission;
        }
        location.href = url;
    }
</script>
</body>
</html>
