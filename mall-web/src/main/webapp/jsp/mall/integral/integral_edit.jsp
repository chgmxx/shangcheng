<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>积分商城-编辑积分商品</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">

        if (top == self) {
            window.location.href = "/mallIntegral/start.do";
        }
        var dProvinceObj = new Object();
        var dDetailObj = new Object();

        var proObj = new Object();

        var proSpecArr = new Array();

        var proSpecObj = new Object();
    </script>

</head>
<body>
<div id="newGroup">
    <c:if test="${!empty shoplist }">
        <div class="con-head">
            <a class="navColor" href="/mallIntegral/index.do">积分商品管理</a>
            <a class="" href="/mallIntegral/image_index.do">积分商城图片设置</a>
        </div>
        <form id="integralForm">
            <div class="groupDiv">
                <span class="font14"><em>*</em>所属店铺：</span>
                <c:if test="${!empty shoplist }">
                    <select name="shopId" class="shopId" id="<c:if test="${!empty integralMap}">${integralMap.shop_id }</c:if>"
                            <c:if test="${!empty integralMap}">disabled="disabled"</c:if>>
                        <c:forEach var="shop" items="${shoplist }">
                            <option value="${shop.id }">${shop.sto_name }</option>
                        </c:forEach>
                    </select>
                </c:if>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em>选择商品：</span>
                <div class="goodDiv">
                    <a onclick="choosePro()" href="javascript:void(0);" class="js-add-goods add-goods"
                       <c:if test="${!empty integralMap && integralMap.imageUrl != ''}">style="background-image:url(${http }${integralMap.imageUrl });background-size:cover;background-position:50% 50%;"</c:if>>
                        <c:if test="${empty integralMap || integralMap.imageUrl == ''}">
                            <i class="icon-add"></i>
                        </c:if>
                    </a>
                </div>
                <input type="hidden" name="id" id="ids" value="<c:if test="${!empty integralMap }">${integralMap.id }</c:if>">
                <input type="hidden" name="productId" id="productId" value="<c:if test="${!empty integralMap }">${integralMap.product_id }</c:if>">
                <input type="hidden" id="defaultProId" value="<c:if test="${!empty integralMap }">${integralMap.product_id }</c:if>">
                <input type="hidden" id="isSpec" value="<c:if test="${!empty integralMap }">${integralMap.isSpecifica }</c:if>">
            </div>
            <div class="groupDiv">
                <span class="font14"></span>
                <div class="proName red" style="margin-left:100px;">如需修改商品信息，请在商品管理中更新</div>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>&nbsp;&nbsp;</em>商品名称：</span>
                <span class="proName"><c:if test="${!empty integralMap }">${integralMap.proName }</c:if></span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>&nbsp;&nbsp;</em>商品原价：</span>
                <span class="proPrice">￥
				<c:if test="${!empty integralMap && integralMap.proPrice>0}">${integralMap.proPrice }</c:if>
				<c:if test="${empty integralMap || integralMap.proPrice==0}">0.00</c:if>
				</span>
            </div>
                <%-- <div class="groupDiv">
                    <span class="font14"><em>*</em>拍卖类型：</span>
                        <input type="radio" class="aucType" name="aucType" value="1"
                        <c:if test="${isType == 1}">checked="checked"</c:if> /> 降价拍
                        <input type="radio" class="aucType" name="aucType" value="2"
                        <c:if test="${isType == 2}">checked="checked"</c:if> /> 升价拍
                </div> --%>
            <div class="groupDiv">
                <span class="font14"><em>*</em>积分： </span>
                <input type="text" class="inpt vali gPrice hidePrice" name="money" id="integralMoney"
                       maxlength="9" datatype="^[0-9]{0,6}(\.\d{1,2})?$" p="num" notNull="1" value="${integralMap.money }">
                <span class="red">积分最多只能是大于0的6位小数</span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em>开始时间： </span>
                <input type="text" class="inpt vali" name="startTime" id="sStartTime" notNull="1" datatype=""
                       value="<c:if test="${!empty integralMap}">${integralMap.start_time }</c:if>">
                <span class="red">开始时间不能为空</span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em>结束时间： </span>
                <input type="text" class="inpt vali" name="endTime" id="sEndTime" notNull="1" datatype=""
                       value="<c:if test="${!empty integralMap}">${integralMap.end_time }</c:if>">
                <span class="red">结束时间不能为空</span>
            </div>

            <div class="groupDiv">
                <a href="javascript:editIntegral();" class="addBtn">确定</a>
                <a href="/mallIntegral/index.do" style="cursor: pointer;color:white;" class="backBtn">返回</a>
            </div>
        </form>
    </c:if>
    <c:if test="${empty shoplist }">
        <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
    </c:if>
</div>
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);

</script>
<script type="text/javascript" src="/js/mall/integral/integral.js"></script>
</body>
</html>