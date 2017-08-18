<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>拍卖管理-编辑拍卖</title>
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

        var dProvinceObj = new Object();
        var dDetailObj = new Object();

        var proObj = new Object();

        var proSpecArr = new Array();

        var proSpecObj = new Object();
    </script>

</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp" />
<c:if test="${!empty auction }">
    <%-- <c:if test="${!empty auction.priceList }">
        <c:forEach var="obj" items="${auction.priceList }">
            <script type="text/javascript">
                var obj = {
                    "id":"${obj.id}",
                    "auctionPrice" : "${obj.auctionPrice}",
                    "isJoinGroup" : "${obj.isJoinGroup}"
                };
                proSpecObj["${obj.specificaIds}"] = obj;
            </script>
        </c:forEach>
    </c:if> --%>
</c:if>
<div id="newGroup">
    <c:if test="${!empty shoplist }">
        <div class="con-head">
            <a class="navColor" href="/mAuction/index.do">拍卖管理</a>
            <a class="" href="/mAuction/margin.do">保证金管理</a>
        </div>
        <form id="groupForm">
            <div class="groupDiv">
                <span class="font14"><em>*</em>所属店铺：</span>
                <c:if test="${!empty shoplist }">
                    <select name="shopId" class="shopId" id="<c:if test="${!empty auction}">${auction.shopId }</c:if>" <c:if test="${!empty auction}">disabled="disabled"</c:if>>
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
                       <c:if test="${!empty auction && auction.imgUrl != ''}">style="background-image:url(${http }${auction.imgUrl });background-size:cover;background-position:50% 50%;"</c:if>>
                        <c:if test="${empty auction || auction.imgUrl == ''}">
                            <i class="icon-add"></i>
                        </c:if>
                    </a>
                </div>
                <input type="hidden" name="id" id="ids" value="<c:if test="${!empty auction }">${auction.id }</c:if>">
                <input type="hidden" name="productId" id="productId" value="<c:if test="${!empty auction }">${auction.productId }</c:if>">
                <input type="hidden" id="defaultProId" value="<c:if test="${!empty auction }">${auction.productId }</c:if>">
                <input type="hidden" id="isSpec" value="<c:if test="${!empty auction }">${auction.isSpecifica }</c:if>">
            </div>
            <div class="groupDiv">
                <span class="font14"></span>
                <div class="proName red" style="margin-left:100px;">如需修改商品信息，请在商品管理中更新</div>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>&nbsp;&nbsp;</em>商品名称：</span>
                <span class="proName"><c:if test="${!empty auction }">${auction.proName }</c:if></span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>&nbsp;&nbsp;</em>商品原价：</span>
                <span class="proPrice">￥
				<c:if test="${!empty auction && auction.proPrice>0}">${auction.proPrice }</c:if>
				<c:if test="${empty auction || auction.proPrice==0}">0.00</c:if>
				</span>
            </div>
            <c:set var="isType" value="1"></c:set>
            <c:set var="isMargin" value="0"></c:set>
            <c:if test="${auction.aucType == 2}">
                <c:set var="isType" value="2"></c:set>
            </c:if>
            <c:if test="${!empty auction }">
                <c:set var="isMargin" value="${auction.isMargin }"></c:set>
            </c:if>
            <div class="groupDiv">
                <span class="font14"><em>*</em>拍卖类型：</span>
                <input type="radio" class="aucType" name="aucType" value="1"
                       <c:if test="${isType == 1}">checked="checked"</c:if> /> 降价拍
                <input type="radio" class="aucType" name="aucType" value="2"
                       <c:if test="${isType == 2}">checked="checked"</c:if> /> 升价拍
            </div>
            <div class="groupDiv">
                <span class="font14"><em></em>是否需要交纳保证金：</span>
                <input type="checkbox" class="isMargin" name="isMargin" value="1"
                       <c:if test="${isMargin == 1}">checked="checked"</c:if> /> 拍卖前需要交纳保证金
            </div>
            <div class="groupDiv aucMarginDiv" <c:if test="${isMargin == 0}">style="display:none;"</c:if>>
                <span class="font14"><em>*</em>交纳保证金： </span>
                <input type="text" class="inpt vali gPrice hidePrice" name="aucMargin" id="aucMargin"
                       maxlength="9" datatype="^[0-9]{0,6}(\.\d{1,2})?$" p="num" notNull="1"
                       value="<c:if test="${!empty auction && !empty auction.aucMargin && auction.aucMargin > 0}">${auction.aucMargin }</c:if>">
                <span class="red">交纳保证金最多只能是大于0的6位小数</span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em>起拍价格： </span>
                <input type="text" class="inpt vali gPrice" name="aucStartPrice" id="aucStartPrice" notNull="1"
                       maxlength="9" datatype="^[0-9]{1,6}(\.\d{1,2})?$"
                       value="<c:if test="${!empty auction && !empty auction.aucStartPrice && auction.aucStartPrice > 0}">${auction.aucStartPrice }</c:if>">
                <span class="red">起拍价格最多只能是大于0的6位小数</span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em>开始时间： </span>
                <input type="text" class="inpt vali" name="aucStartTime" id="sStartTime" notNull="1" datatype=""
                       value="<c:if test="${!empty auction}">${auction.aucStartTime }</c:if>">
                <span class="red">开始时间不能为空</span>
            </div>
            <div class="addDiv" id="hideDiv"
                 <c:if test="${isType == 2}">style="display:block"</c:if> >
                <div class="groupDiv">
                    <span class="font14"><em>*</em>结束时间： </span>
                    <input type="text" class="inpt vali" name="aucEndTime" id="sEndTime" notNull="1" datatype=""
                           value="<c:if test="${!empty auction}">${auction.aucEndTime }</c:if>">
                    <span class="red">结束时间不能为空</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>*</em>加价幅度：</span>
                    <input type="text" class="inpt vali gPrice" name="aucAddPrice" id="addPrice"
                           maxlength="9" datatype="^[0-9]{1,6}(\.\d{1,2})?$" p="num" notNull="1"
                           placeholder="0.00"
                           value="<c:if test="${!empty auction && !empty auction.aucAddPrice && auction.aucAddPrice > 0}">${auction.aucAddPrice }</c:if>">
                    <span class="red">加价幅度必须为6位数的数字</span>
                </div>
            </div>
            <div class="diffDiv" id="hideDiv" <c:if test="${isType == 1}">style="display:block"</c:if>>
                <div class="groupDiv">
                    <span class="font14"><em>*</em>最低价格：</span>
                    <input type="text" class="inpt vali gPrice" name="aucLowestPrice" id="aucLowestPrice" notNull="1"
                           maxlength="9" datatype="^[0-9]{1,6}(\.\d{1,2})?$"
                           value="<c:if test="${!empty auction && !empty auction.aucLowestPrice && auction.aucLowestPrice > 0}">${auction.aucLowestPrice }</c:if>">
                    <span class="red">最低价格最多只能是大于0的6小位数</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>*</em>降价幅度：</span> 每
                    <input type="text" class="inpt vali gPrice" name="aucLowerPriceTime" id="aucLowerPriceTime" notNull="1"
                           maxlength="6" datatype="^[0-9]{1,6}$" p="num"
                           value="<c:if test="${!empty auction && !empty auction.aucLowerPriceTime && auction.aucLowerPriceTime > 0}">${auction.aucLowerPriceTime }</c:if>">
                    分钟下降 ￥
                    <input type="text" class="inpt vali gPrice" name="aucLowerPrice" id="aucLowerPrice" notNull="1"
                           maxlength="9" datatype="^[0-9]{1,6}(\.\d{1,2})?$" p="num"
                           placeholder="0.00" value="<c:if test="${!empty auction && !empty auction.aucLowerPrice && auction.aucLowerPrice > 0}">${auction.aucLowerPrice }</c:if>">
                    <span class="red">降价幅度必须为6位数的数字</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>&nbsp;</em>持续时间：</span>
                    <span class="cxTimes"></span>
                    <span class="times_span">计算持续时间</span>
                </div>
                <div class="groupDiv">
                    <span class="font14">&nbsp;</span>
                    <span class="tips_span">下降到结束价格后还会持续一个时间周期</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>&nbsp;</em>结束时间：</span>
                    <span class="endTimes_span">这里显示结束时间</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>&nbsp;</em>商品限购：</span>
                    开启限购&nbsp;&nbsp;<input type="text" class="inpt vali" name="aucRestrictionNum" id="maxNum"
                                           style="width:142px;" maxlength="6"
                                           datatype="^[0-9]{0,6}$"
                                           value="<c:if test="${!empty auction && !empty auction.aucRestrictionNum && auction.aucRestrictionNum > 0}">${auction.aucRestrictionNum }</c:if>">
                    件/人，
                    <span class="red" style="color:#000;">商品限购最多只能是大于0的6位数字，不填写代表此拍卖的商品不限购</span>
                </div>
            </div>
            <div class="groupDiv" style="display:none;">
                <span class="font14"><em>*</em>拍卖件数：</span>
                <!--  maxlength="6" datatype="^[0-9]{1,6}$" notNull="1" -->
                <input type="text" class="inpt" name="aucNum" id="aucNum"
                       placeholder="0.00" value="<c:if test="${!empty auction && !empty auction.aucNum && auction.aucNum > 0}">${auction.aucNum }</c:if>">
                <span class="red">降价幅度必须为6位数的数字</span>
            </div>

            <!-- <div class="packageDiv" id="createTable">
                <div class="groupDiv">
                <span class="font14"> </span>
                <table class="table" style="width:auto;display:none;">
                        <thead>
                            <tr>
                                <th>是否参与</th>
                                <th>颜色</th>
                                <th>原价</th>
                                <th>拍卖价</th>
                                <th>库存</th>
                            </tr>
                        </thead>
                        <tbody id="J_Tbody">
                    </tbody>
                    <tfoot>
                        <tr height="44px">
                            <td colspan="5">
                                <div class="batch-opts">
                                    批量设置 <span class="js-batch-type" style="display: inline;">
                                        <a class="js-batch-price" href="javascript:;"
                                        errormsg="价格最多只能输入6位小数">拍卖价</a>
                                    </span>
                                    <span class="js-batch-form" style="display:none;"> <input type="text"
                                        class="js-batch-txt input-mini" placeholder="" vali=""
                                        maxlength="10"> <a href="javascript:;"
                                        class="js-batch-save">保存</a> <a href="javascript:;"
                                        class="js-batch-cancel">取消</a>
                                    </span>
                                </div>
                            </td>
                        </tr>
                    </tfoot>
                    </table>
                    </div>
            </div> -->
            <div class="groupDiv">
                <a href="javascript:editAuction();" class="addBtn">确定</a>
                <a href="mAuction/index.do" style="cursor: pointer;color:white;" class="backBtn">返回</a>
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
<script type="text/javascript" src="/js/mall/auction/auction.js"></script>
</body>
</html>