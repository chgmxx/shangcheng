<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>物流管理-编辑物流</title>
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
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css?<%= System.currentTimeMillis()%>"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">

        var dProvinceObj = new Object();
        var dDetailObj = new Object();

        var proObj = new Object();
    </script>

</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="con-head">
    <a class="navColor" href="/mFreight/index.do">物流管理</a>
    <a class="" href="/mFreight/takeindex.do">上门自提</a>
</div>
<div id="newGroup">
    <c:if test="${!empty shopList }">
        <form id="freightForm">
            <div class="groupDiv">
                <span class="font14">物流名称：&nbsp;&nbsp;&nbsp; </span>
                <input type="hidden" name="ids" id="ids" value="<c:if test="${!empty freight }">${freight.id }</c:if>">
                <input type="text" class="inpt" placeholder="" name="name" id="name" maxlength="100" notNull="1" datatype=""
                       value="<c:if test="${!empty freight }">${freight.name }</c:if>">
                <span class="red">物流名称最多输入20位汉字或40位字符</span>
            </div>
            <div class="groupDiv">
                <span class="font14">所属店铺：</span>
                <c:if test="${!empty shopList }">
                    <select name="shopId" class="shopId" id="<c:if test="${!empty freight}">${freight.shopId }</c:if>" <c:if test="${!empty freight}">disabled="disabled"</c:if>>
                        <c:forEach var="shop" items="${shopList }">
                            <option value="${shop.id }">${shop.sto_name }</option>
                        </c:forEach>
                    </select>
                </c:if>
            </div>
            <c:if test="${!empty comMap }">
                <div class="groupDiv">
                    <span class="font14">默认快递：</span>
                    <select name="transType" class="expressId" id="expressId">
                        <c:forEach var="com" items="${comMap }">
                            <option value="${com.key }"
                                    <c:if test="${!empty freight && freight.expressId == com.key}">selected="selected"</c:if>>${com.value }</option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>
            <div class="groupDiv">
                <span class="font14">是否包邮：</span>
                <span class="font15">
					<input type="radio" name="isNoMoney" class="isNoMoney" value="1"
                    <c:if test="${!empty freight && freight.isNoMoney == 1}"> checked="checked"</c:if> >
					<label for="J_buyerBearFre">自定义运费</label>
					<input type="radio" name="isNoMoney" class="isNoMoney" value="2" <c:if test="${!empty freight && freight.isNoMoney == 2}"> checked="checked"</c:if>>
					<label for="J_sellerBearFre">卖家承担运费</label>
				</span>
            </div>
            <c:set var="priceType" value="0"></c:set>
            <c:if test="${!empty freight}">
                <c:set var="priceType" value="${freight.priceType }"></c:set>
            </c:if>
            <div class="noMoneyDiv" style="<c:if test='${!empty freight && freight.isNoMoney == 2}'>display:none;</c:if>">
                <div class="groupDiv">
                    <span class="font14">计价方式：</span>
                    <span class="font15">
						<input type="radio" name="priceType" class="priceType" value="0"
                        <c:if test="${priceType == 0}"> checked="checked"</c:if> >
						<label for="J_buyerBearFre">统一运费</label>
						<input type="radio" name="priceType" class="priceType" value="1" <c:if test="${priceType == 1}"> checked="checked"</c:if>>
						<label for="J_sellerBearFre">按件数</label>
						<input type="radio" name="priceType" class="priceType" value="2" <c:if test="${priceType== 2}"> checked="checked"</c:if>>
						<label for="J_sellerBearFre">按重量</label>
						<input type="radio" name="priceType" class="priceType" value="3" <c:if test="${priceType== 3}"> checked="checked"</c:if>>
						<label for="J_sellerBearFre">按公里</label>
					</span>
                </div>
                <div class="groupDiv priceType0" id="hideDiv" style="<c:if test="${priceType == 0}">display:block</c:if>">
                    <span class="font14">运&nbsp;&nbsp;&nbsp;&nbsp;费：&nbsp;&nbsp;&nbsp; </span>
                    <input type="text" class="inpt2" id="money" maxlength="9" style="width: 200px;" notNull="0"
                           value="<c:if test="${!empty freight && !empty freight.money && freight.money > 0 && priceType == 0}">${freight.money }</c:if>">
                    <span class="red">运费最多只能是大于0的6位数，如：10</span>
                </div>
                <div class="groupDiv priceType1" id="hideDiv" style="<c:if test="${priceType == 1 || priceType == 2 || priceType == 3}">display:block</c:if>">
                    <span class="font14">运&nbsp;&nbsp;&nbsp;&nbsp;费：&nbsp;&nbsp;&nbsp; </span>
                    <input type="text" class="inpt2" id="firstNums" maxlength="6" notNull="0"
                           value="<c:if test="${!empty freight && !empty freight.firstNums && freight.firstNums > 0 && priceType > 0}">${freight.firstNums }</c:if>">
                    <em class="unit">件</em>内，运费
                    <input type="text" class="inpt2" id="money" maxlength="9" notNull="1"
                           value="<c:if test="${!empty freight && !empty freight.money && priceType > 0}">${freight.money }</c:if>">
                    元，每增加
                    <input type="text" class="inpt2" id="addNums" notNull="0"
                           value="<c:if test="${!empty freight && !empty freight.addNums && freight.addNums > 0 && priceType > 0}">${freight.addNums }</c:if>">
                    <em class="unit">件</em>，增加运费
                    <input type="text" class="inpt2" id="addMoney" notNull="0"
                           value="<c:if test="${!empty freight && !empty freight.addMoney && freight.addMoney > 0 && priceType > 0}">${freight.addMoney }</c:if>">
                    元
                    <span class="red">运费最多只能是大于0的6位数，如：10</span>
                </div>
                <div class="groupDiv">
                    <span class="font14">包邮数量：&nbsp;&nbsp;&nbsp; </span>
                    商品满&nbsp;&nbsp;<input type="text" class="inpt" name="num" notNull="0" id="num" style="width:155px;" maxlength="5"
                                          datatype="^[0-9]{0,5}$"
                                          value="<c:if test="${!empty freight && !empty freight.noMoneyNum && freight.noMoneyNum > 0}">${freight.noMoneyNum }</c:if>">件包邮，
                    <span class="red">包邮数量最多只能是大于0的5位数字</span>
                </div>
                <div class="groupDiv">
                    <span class="font14">包邮价格：&nbsp;&nbsp;&nbsp; </span>
                    商品满&nbsp;&nbsp;<input type="text" class="inpt" name="noMoney" id="noMoney" notNull="0"
                                          style="width:155px;" maxlength="8"
                                          datatype="^[0-9]{0,5}(\.\d{1,2})?$"
                                          value="<c:if test="${!empty freight && !empty freight.noMoney && freight.noMoney > 0}">${freight.noMoney }</c:if>">元包邮，
                    <span class="red">包邮价格最多只能是大于0的5位数</span>
                </div>
                <div class="groupDiv">
                    <!-- <span class="font14"> </span>  -->
                    <span class="font15">
				<input type="checkbox" value="1" name="isResultMoney" class="isResultMoney"
                        <c:if test="${!empty freight && freight.isResultMoney == 1}"> checked="checked"</c:if>/> 是否指定条件设置邮费</span>
                </div>
                <div class="packageDiv">
                    <div class="groupDiv">
                        <span class="font14"> </span>
                        <table class="table" style="<c:if test='${freight.isResultMoney == 0}'>display: none;</c:if>width:auto;">
                            <colgroup>
                                <col width="18%">
                                <col width="14%">
                                <col width="28%">
                                <col width="15%">
                                <col width="15%">
                                <col width="14%">
                            </colgroup>
                            <thead>
                            <tr>
                                <th>选择省份</th>
                                <th>默认快递公司</th>
                                <th>运费</th>
                                <th>包邮数量</th>
                                <th>包邮价格</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="J_Tbody">
                            <c:if test="${!empty freight.detailList }">
                                <c:forEach var="detail" items="${freight.detailList }" varStatus="i">
                                    <tr data-index="${i.index }" name="tr">
                                        <td class="type${i.index }">
                                            <input type="hidden" class="dId" name="dId" value="${detail.id }">
                                            <a href="javascript:void(0);" class="acc_popup edit J_Edit" title="编辑运送区域" onclick="selectPro($(this))">编辑</a>
                                            <div class="area-group">
                                                <p>${detail.provinces }</p>
                                                <input type="hidden" class="provinceId" value="${detail.provincesId }"/>
                                                <input type="hidden" class="province" value="${detail.provinces }"/>
                                            </div>
                                        </td>
                                        <td>
                                            <select name="transType" class="expressId" style="width:100px;">
                                                <c:if test="${!empty comMap }">
                                                    <c:forEach var="com" items="${comMap }">
                                                        <option value="${com.key }"
                                                                <c:if test="${!empty freight && detail.expressId == com.key}">selected="selected"</c:if>>${com.value }</option>
                                                    </c:forEach>
                                                </c:if>
                                            </select>

                                        </td>
                                        <td>
                                                <%-- <input type="text" value="${detail.money}" class="input-text dMoney inp_test" name="dMoney" style="width:50px;"  maxlength="5"
                                                        dataType="^[0-9]{1}\d{0,5}(\.\d{1,2})?$" dataClass="moneySpan"
                                                        dataMsg="运费最多只能是大于0的5位数，如：12" dataNull="0">元 --%>
                                            <span id="hideDiv" class="types2" style="<c:if test='${priceType == 1 || priceType == 2 }'>display:block</c:if>">
									<input type="text" class="inpt2" id="firstNums" maxlength="6" notNull="0"
                                           value="<c:if test="${!empty detail && !empty detail.firstNums && detail.firstNums > 0 }">${detail.firstNums }</c:if>">
					<em class="unit" style="color: #000">g</em>内，运费 
					<input type="text" class="inpt2" id="money" maxlength="9" notNull="1"
                           value="<c:if test="${!empty detail && !empty detail.money }">${detail.money }</c:if>">
					元<br/>每增加
					<input type="text" class="inpt2" id="addNums" notNull="0"
                           value="<c:if test="${!empty detail && !empty detail.addNums && detail.addNums > 0 }">${detail.addNums }</c:if>">
					<em class="unit" style="color: #000">g</em>，增加运费 
					<input type="text" class="inpt2" id="addMoney" notNull="0"
                           value="<c:if test="${!empty detail && !empty detail.addMoney && detail.addMoney > 0 }">${detail.addMoney }</c:if>">
					元		
								</span>
                                            <span id="hideDiv" class="types1" style="<c:if test='${priceType == 0 }'>display:block</c:if>">
					<input type="text" class="inpt2" id="money" maxlength="9" notNull="0"
                           value="<c:if test="${!empty detail && !empty detail.money && detail.money > 0 }">${detail.money }</c:if>">
					元
								</span>
                                        </td>
                                        <td><input type="text" value="<c:if test="${!empty detail.noMoneyNum && detail.noMoneyNum > 0}">${detail.noMoneyNum}</c:if>"
                                                   class="input-text dNoMoneyNum inp_test" name="dNoMoneyNum" style="width:50px;" maxlength="5"
                                                   dataType="^[0-9]{0,5}$" dataClass="dNoMoneyNumSpan"
                                                   dataMsg="包邮数量最多只能是大于0的5位数字" dataNull="1">件
                                            <p>不填写代表此省份不免邮</p>
                                        </td>
                                        <td><input type="text" value="<c:if test="${!empty detail.noMoney && detail.noMoney > 0}">${detail.noMoney}</c:if>"
                                                   class="input-text dNoMoney inp_test" name="dNoMoney" style="width:50px;" maxlength="8"
                                                   dataType="^[0-9]{0,5}(\.\d{1,2})?$" dataClass="dNoMoneySpan"
                                                   dataMsg=" 包邮价格最多只能是大于0的5位数" dataNull="1">元
                                            <p>不填写代表此省份不免邮</p>
                                        </td>
                                        <td>
                                            <a href="javascript:void(0);" class="J_AddItem small-icon" onclick="addTr($(this))" data-spm-anchor-id="0.0.0.0" title="添加">+</a>
                                            <a href="javascript:void(0);" class="J_DelateItem small-icon" title="删除" onclick="deleteTr($(this))">x</a>
                                        </td>
                                    </tr>
                                    <c:if test="${!empty detail.provinceList}">
                                        <c:forEach var="province" items="${detail.provinceList }">
                                            <script type="text/javascript">
                                                var pId = "${province.id}";
                                                var provinceId = "${province.provinceId}";
                                                dProvinceObj[pId] = pId;
                                                proObj[provinceId] = pId;
                                            </script>
                                        </c:forEach>
                                    </c:if>
                                    <c:if test="${!empty detail.id}">
                                        <script type="text/javascript">
                                            dDetailObj["${detail.id}"] = "${detail.id}";
                                        </script>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <tr data-index="">
                                <td colspan="6" class="hui-error">
                                    <div class="J_DefaultMessage">
                                    </div>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
            <div class="groupDiv">
                <a href="javascript:editFreight();" class="addBtn">确定</a>
                <a href="mFreight/index.do" style="cursor: pointer;color:white;" class="backBtn">返回</a>
            </div>
        </form>
    </c:if>
    <c:if test="${empty shopList }">
        您的店铺已经设置了物流信息，<a href="javascript:void(0);" onclick="shopIndex();">请前往店铺管理添加新的店铺</a>
    </c:if>
</div>

<table class="tableHide" style="display:none;">
    <tbody>
    <tr name="tr">
        <td class="type0">
            <input type="hidden" class="dId" name="dId" value="">
            <a href="javascript:void(0);" class="acc_popup edit J_Edit" title="编辑运送区域" onclick="selectPro($(this))">编辑</a>
            <div class="area-group">
                <p>未添加省份</p>
                <input type="hidden" class="provinceId" value=""/>
                <input type="hidden" class="province" value=""/>
            </div>
        </td>
        <td>
            <select name="transType" class="expressId" style="width:100px;">
                <c:if test="${!empty comMap }">
                    <c:forEach var="com" items="${comMap }">
                        <option value="${com.key }"
                                <c:if test="${!empty freight}">selected="selected"</c:if>>${com.value }</option>
                    </c:forEach>
                </c:if>
            </select>

        </td>
        <td> <!-- <input type="text" value="" class="input-text dMoney inp_test" name="dMoney" style="width:50px;"  maxlength="5"
				dataType="^[0-9]{1}\d{0,5}(\.\d{1,2})?$" dataClass="moneySpan"
				dataMsg="运费最多只能是大于0的5位数，如：12" dataNull="0">元 -->

            <span id="hideDiv" class="types2">
				<input type="text" class="inpt2" id="firstNums" maxlength="6" value="" notNull="0">
				<em class="unit" style="color: #000">g</em>内，运费 
				<input type="text" class="inpt2" id="money" maxlength="9" value="" notNull="1"> 元<br/>每增加
				<input type="text" class="inpt2" id="addNums" value="" notNull="0">
				<em class="unit" style="color: #000">g</em>，增加运费 
				<input type="text" class="inpt2" id="addMoney" value="" notNull="0">
				元		
			</span>
            <span id="hideDiv" class="types1" style="<c:if test='${priceType == 0 }'>display:block</c:if>">
				<input type="text" class="inpt2" id="money" maxlength="9" value="" notNull="0"> 元
			</span>
        </td>
        <td><input type="text" value="" class="input-text dNoMoneyNum inp_test" name="dNoMoneyNum" style="width:100px;" maxlength="5"
                   dataType="^[0-9]{0,5}$" dataClass="dNoMoneyNumSpan"
                   dataMsg="包邮数量最多只能是大于0的5位数字" dataNull="1">件
            <p>不填写代表此省份不免邮</p>
        </td>
        <td><input type="text" value="" class="input-text dNoMoney inp_test" name="dNoMoney" style="width:100px;" maxlength="8"
                   dataType="^[0-9]{0,5}(\.\d{1,2})?$" dataClass="dNoMoneySpan"
                   dataMsg=" 包邮价格最多只能是大于0的5位数" dataNull="1">元
            <p>不填写代表此省份不免邮</p>
        </td>
        <td>
            <a href="javascript:void(0);" class="J_AddItem small-icon" data-spm-anchor-id="0.0.0.0" onclick="addTr($(this))" title="添加">+</a>
            <a href="javascript:void(0);" class="J_DelateItem small-icon" onclick="deleteTr($(this))" title="删除">x</a>
        </td>
    </tr>
    </tbody>
</table>
<script type="text/javascript" src="/js/mall/freight.js"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);
    if (!$(".isNoMoney").is(":checked")) {
        $(".isNoMoney[value='1']").attr("checked", "checked");
    }

    onload(1);
    function onload(type) {
        var html = $(".tableHide tbody");
        var len = $(".table tbody tr[data-index!='']").length;
        if (type == 0 || len == 0) {
            html.find("tr").attr("data-index", len);
            html.find("tr td:eq(0)").attr("class", "type" + len);
            $(html.html()).insertBefore($(".table tbody tr:last"));
            loadValTable();
            loadFrame();
        }

        var priceType = $(".priceType:checked").val();
        if (priceType != 0) {
            $(".types2").show();
            $(".types1").hide();
            if (priceType == "1") {
                $("em.unit").html("件");
            } else if (priceType == "2") {
                $("em.unit").html("g");
            } else if (priceType == "3") {
                $("em.unit").html("km");
            }
        } else {
            $(".types1").show();
            $(".types2").hide();
        }
        $(".types2").each(function () {

            var firstNums = $(this).find("#firstNums").val();
            var addNums = $(this).find("#addNums").val();
            if (priceType == "1") {
                if (firstNums != null && firstNums != "")
                    $(this).find("#firstNums").val(parseInt(firstNums))
                if (addNums != null && addNums != "")
                    $(this).find("#addNums").val(parseInt(addNums))
            }
        });

        if ($(".isResultMoney").is(":checked")) {
            $(".table").show();
        } else {
            $(".table").hide();
        }

        $(".inpt2").unbind("focus");
        $(".inpt2").unbind("blur");
        /**
         * 鼠标选中事件
         */
        $(".inpt2").focus(function () {
            blurFreight($(this));
        });
        /**
         * 鼠标失去焦点
         */
        $(".inpt2").blur(function () {
            blurFreight($(this));
        });

    }
    //删除tr
    function deleteTr(obj) {
        var len = $(".table tbody tr[data-index!='']").length;
        if (len > 1) {
            obj.parents("tr").remove();
        } else {
            layer.msg('至少要有一个配送区域', {
                offset: "10%",
                icon: 1
            });
        }
        loadFrame();

    }
    //添加tr
    function addTr(obj) {
        onload(0);
    }
    $(".isNoMoney").change(function () {
        var type = $(this).val();
        if (type == 1) {
            $(".noMoneyDiv").show();
        } else {
            $(".noMoneyDiv").hide();
        }
        loadFrame();
    });
    $(".isResultMoney").change(function () {
        var check = $(this).is(":checked");
        if (check) {
            $(".table").show();
        } else {
            $(".table").hide();
        }
        if ($(".isResultMoney").is(":checked")) {
            var val = $(".priceType:checked").val();
            if (val == "1") {
                $("em.unit").html("件");
            } else if (val == "2") {
                $("em.unit").html("g");
            } else if (val == "3") {
                $("em.unit").html("km");
            }
        }
        loadFrame();
    });
    function shopIndex() {
        location.href = "/store/start.do";
    }

    function loads() {
        loadWindow();
    }
</script>
</body>
</html>