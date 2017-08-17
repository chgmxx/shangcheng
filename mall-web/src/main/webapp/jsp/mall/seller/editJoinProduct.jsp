<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>超级销售员——编辑商品佣金设置</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/seller/back/main.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css?<%= System.currentTimeMillis()%>"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-ui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>

</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div id="newGroup">
    <ul class="page-tab">
        <li>
            <a href="/mallSellers/sellerSet.do">功能设置</a>
        </li>
        <li>
            <a href="/mallSellers/joinProduct.do" class="tab-active">商品佣金设置</a>
        </li>
        <li>
            <a href="/mallSellers/sellerCheckList.do">推荐审核</a>
        </li>
        <li>
            <a href="/mallSellers/sellerList.do">销售员管理</a>
        </li>
        <li>
            <a href="/mallSellers/withDrawList.do">提现列表</a>
        </li>
    </ul>
    <c:if test="${!empty shoplist }">
        <form id="joinProductForm">
            <div class="groupDiv">
                <span class="font14"><em>*</em> 所属店铺：</span>
                <c:if test="${!empty shoplist }">
                    <select name="shopId" class="shopId" id="<c:if test="${!empty joinProductMap}">${joinProductMap.shop_id }</c:if>"
                            <c:if test="${!empty joinProductMap}">disabled="disabled"</c:if>>
                        <c:forEach var="shop" items="${shoplist }">
                            <option value="${shop.id }">${shop.sto_name }</option>
                        </c:forEach>
                    </select>
                </c:if>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em> 选择商品：</span>
                <div class="goodDiv">
                    <a onclick="choosePro()" href="javascript:void(0);" class="js-add-goods add-goods"
                       <c:if test="${!empty joinProductMap && joinProductMap.imgUrl != ''}">style="background-image:url(${http }${joinProductMap.imgUrl });background-size:cover;background-position:50% 50%;"</c:if>>
                        <c:if test="${empty joinProductMap || joinProductMap.imgUrl == ''}">
                            <i class="icon-add"></i>
                        </c:if>
                    </a>
                </div>
                <input type="hidden" id="ids" value="<c:if test="${!empty joinProductMap }">${joinProductMap.id }</c:if>">
                <input type="hidden" name="productId" id="productId" value="<c:if test="${!empty joinProductMap }">${joinProductMap.product_id }</c:if>">
                <input type="hidden" id="proPrice"
                       value="<c:if test="${!empty joinProductMap && joinProductMap.proPrice>0}">${joinProductMap.proPrice }</c:if><c:if test="${empty joinProductMap || joinProductMap.proPrice==0}">0.00</c:if>">
                <input type="hidden" id="defaultProId" value="<c:if test="${!empty joinProductMap }">${joinProductMap.product_id }</c:if>">
                <input type="hidden" id="isSpec" value="<c:if test="${!empty joinProductMap }">${joinProductMap.is_specifica }</c:if>">
            </div>
            <div class="groupDiv">
                <span class="font14"></span>
                <div class="proName red" style="margin-left:100px;">如需修改商品信息，请在商品管理中更新</div>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em> 商品名称：</span>
                <span class="proName"><c:if test="${!empty joinProductMap }">${joinProductMap.proName }</c:if></span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em> 商品原价：</span>
                <span class="proPrice">￥
				<c:if test="${!empty joinProductMap && joinProductMap.proPrice>0}">${joinProductMap.proPrice }</c:if>
				<c:if test="${empty joinProductMap || joinProductMap.proPrice==0}">0.00</c:if>
				</span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em> 佣金类型：</span>
                <c:if test="${!empty shoplist }">
                    <select name="commissionType" class="commissionType" id="<c:if test="${!empty joinProductMap}">${joinProductMap.commission_type }</c:if>">
                        <option value="1" <c:if test="${joinProductMap.commission_type == 1 }">selected="selected"</c:if>>按百分比</option>
                        <option value="2" <c:if test="${joinProductMap.commission_type == 2 }">selected="selected"</c:if>>按固定金额</option>
                    </select>
                </c:if>
            </div>
            <div class="groupDiv proPrices">
                <span class="font14"><em>*</em> 商品佣金： </span>
                <input type="text" class="inpt" name="commissionRate" id="commissionRate" notNull="1" maxlength="8"
                       datatype="^[0-9]{1,5}(\.\d{1,2})?$" yj="1"
                       value="<c:if test="${!empty joinProductMap && !empty joinProductMap.commission_rate && joinProductMap.commission_rate > 0}">${joinProductMap.commission_rate }</c:if>">
                <em class="unit">%</em>
                <span class="mRed red"
                      <c:if test="${joinProductMap.commission_type == 1 }">style="display:none;"</c:if> >商品佣金最多只能是大于0的5位数,且不能超过商品的70%</span>
                <span class="baiRed red"
                      <c:if test="${joinProductMap.commission_type == 2 }">style="display:none;"</c:if> >根据微信规则，佣金比例最高不可超过70%</span>
                <div class="commissionTip" style="margin-left:100px;">商品佣金按百分比的计算公式：商品价*（佣金商品佣金/100）</div>
            </div>
            <div class="groupDiv">
                <a href="javascript:editJoinProduct();" class="addBtn">确定</a>
                <a href="/mallSellers/joinProduct.do" style="cursor: pointer;color:white;" class="backBtn">返回</a>
            </div>
        </form>
    </c:if>
    <c:if test="${empty shoplist }">
        <a href="javascript:void(0);" onclick="shopIndex();">请前往店铺管理添加新的店铺</a>
    </c:if>
</div>
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);
</script>
<script type="text/javascript" src="/js/mall/seller/editJoinProduct.js"></script>
</body>
</html>