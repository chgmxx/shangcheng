<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>批发管理-编辑批发</title>
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
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="/js/plugin/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/plugin/layer/layer.js" type="text/javascript"></script>
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
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<c:if test="${!empty pifa }">
    <c:if test="${!empty pifa.priceList }">
        <c:forEach var="obj" items="${pifa.priceList }">
            <script type="text/javascript">
                var obj = {
                    "id": "${obj.id}",
                    "seckillPrice": "${obj.seckillPrice}",
                    "isJoinGroup": "${obj.isJoinGroup}"
                };
                proSpecObj["${obj.specificaIds}"] = obj;
            </script>
        </c:forEach>
    </c:if>
</c:if>
<div class="con-head" style="margin-bottom: 0px;">
    <a class="navColor" href="/mallWholesalers/index.do">批发管理</a>
    <a class="" href="/mallWholesalers/wholesaleList.do">批发商管理</a>
    <a class="" href="/mallWholesalers/toSetWholesale.do">批发设置</a>
</div>
<div id="newGroup">
    <c:if test="${!empty shoplist }">
        <form id="groupForm">
            <div class="groupDiv">
                <span class="font14"><em>*</em> 所属店铺：</span>
                <c:if test="${!empty shoplist }">
                    <select name="shopId" class="shopId" id="<c:if test="${!empty pifa}">${pifa.shop_id }</c:if>" <c:if test="${!empty pifa}">disabled="disabled"</c:if>>
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
                       <c:if test="${!empty pifa && pifa.imgUrl != ''}">style="background-image:url(${http }${pifa.imgUrl });background-size:cover;background-position:50% 50%;"</c:if>>
                        <c:if test="${empty pifa || pifa.imgUrl == ''}">
                            <i class="icon-add"></i>
                        </c:if>
                    </a>
                </div>
                <input type="hidden" name="productId" id="productId" value="<c:if test="${!empty pifa }">${pifa.productId }</c:if>">
                <input type="hidden" id="defaultProId" value="<c:if test="${!empty pifa }">${pifa.productId }</c:if>">
                <input type="hidden" id="isSpec" value="<c:if test="${!empty pifa }">${pifa.isSpecifica }</c:if>">
                <input type="hidden" name="sNums" id="sNums" value="">
            </div>
            <div class="groupDiv">
                <span class="font14"></span>
                <div class="proName red" style="margin-left:100px;">如需修改商品信息，请在商品管理中更新</div>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em> 商品名称：</span>
                <input type="hidden" name="id" id="ids" value="<c:if test="${!empty pifa }">${pifa.id }</c:if>">
                <span class="proName"><c:if test="${!empty pifa }">${pifa.proName }</c:if></span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em> 商品原价：</span>
                <span class="proPrice">￥
				<c:if test="${!empty pifa && pifa.proPrice>0}">${pifa.proPrice }</c:if>
				<c:if test="${empty pifa || pifa.proPrice==0}">0.00</c:if>
				</span>
            </div>
            <div class="groupDiv">
                <span class="font14"><em>*</em> 批发类型：</span>
                <c:set var="pfType" value="1"></c:set>
                <c:if test="${!empty pifa }">
                    <c:set var="pfType" value="${pifa.pf_type }"></c:set>
                </c:if>
                <span <c:if test="${!empty pifa }">style="display:none;"</c:if>>
				<span class="spCla">
				<input type="radio" name="pfType" class="pfType" value="1" <c:if test="${pfType == 1 }">checked='checked'</c:if>/>手批
				</span>
				<input type="radio" name="pfType" class="pfType" value="2" <c:if test="${pfType == 2 }">checked='checked'</c:if>/>混批
				</span>

                <c:if test="${!empty pifa }">
                    <c:if test="${pfType == 1 }">手批</c:if>
                    <c:if test="${pfType == 2 }">混批</c:if>
                </c:if>
            </div>
                <%-- <div class="groupDiv">
                    <span class="font14"><em>*</em> 活动名称：</span>
                    <input type="hidden" name="id" id="ids" value="<c:if test="${!empty pifa }">${pifa.id }</c:if>">
                    <input type="text" class="inpt" placeholder="" name="sName" id="sName" maxlength="100" notNull="1" datatype=""
                        value="<c:if test="${!empty pifa }">${pifa.sName }</c:if>">
                     <span class="red">活动名称最多输入50位汉字或100位字符</span>
                </div>  --%>
            <div class="noMoneyDiv">
                <div class="groupDiv">
                    <span class="font14"><em>*</em> 生效时间： </span>
                    <input type="text" class="inpt" name="pfStartTime" id="pfStartTime" notNull="1"
                           value="<c:if test="${!empty pifa}">${pifa.pf_start_time }</c:if>"> 至
                    <input type="text" class="inpt" name="pfEndTime" id="pfEndTime" notNull="1"
                           value="<c:if test="${!empty pifa}">${pifa.pf_end_time }</c:if>">
                    <span class="red">生效时间不能为空</span>
                </div>
                    <%-- <div class="groupDiv">
                        <span class="font14">商品限购：&nbsp;&nbsp;&nbsp; </span>
                        开启限购&nbsp;&nbsp;<input type="text" class="inpt" name="sMaxBuyNum" id="maxNum"
                         style="width:142px;" maxlength="5"
                        datatype="^[0-9]{0,5}$"
                        value="<c:if test="${!empty pifa && !empty pifa.sMaxBuyNum && pifa.sMaxBuyNum > 0}">${pifa.sMaxBuyNum }</c:if>"> 件/人，
                        <span class="red" style="color:#000;">商品限购最多只能是大于0的5位数字</span>
                    </div> --%>
                <div class="groupDiv proPrices"
                     style="
                     <c:if test='${!empty pifa }'>
                     <c:if test='${!empty pifa.priceList }'>display:none;</c:if></c:if>">
                    <span class="font14"><em>*</em> 批发价：&nbsp;&nbsp;&nbsp; </span>
                    <input type="text" class="inpt" name="pfPrice" id="pfPrice" notNull="1"
                           maxlength="8"
                           datatype="^[0-9]{1,5}(\.\d{1,2})?$"
                           value="<c:if test="${!empty pifa && !empty pifa.pf_price && pifa.pf_price > 0}">${pifa.pf_price }</c:if>">
                    <span class="red">批发价最多只能是大于0的5位数</span>
                </div>
                <div class="packageDiv" id="createTable">
                    <div class="groupDiv">
                        <span class="font14"> </span>
                        <table class="table" style="width:auto;display:none;">
                            <thead>
                            <tr>
                                <th>是否参与</th>
                                <th>颜色</th>
                                <th>原价</th>
                                <th>批发价</th>
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
                                           errormsg="价格最多只能输入6位小数">批发价</a>
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
                </div>
            </div>
            <div class="groupDiv">
                <a href="javascript:editPifa();" class="addBtn">确定</a>
                <a href="/mallWholesalers/index.do" style="cursor: pointer;color:white;" class="backBtn">返回</a>
            </div>
        </form>
    </c:if>
    <c:if test="${empty shoplist }">
        您的店铺已经设置了物流信息，<a href="javascript:void(0);" onclick="shopIndex();">请前往店铺管理添加新的店铺</a>
    </c:if>
</div>
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);
</script>
<script type="text/javascript" src="/js/mall/pifa/pifa.js??<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="/js/mall/pifa/pifa_new_inven.js"></script>
</body>
</html>