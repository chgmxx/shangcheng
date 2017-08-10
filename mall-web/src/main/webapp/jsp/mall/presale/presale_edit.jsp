<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>预售管理-编辑预售</title>
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
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        var timeSelArr = new Array();

        var timeDefaultObj = new Object();//默认时间数据

        var specArray = new Array();
    </script>
    <style type="text/css">
        .table td {
            padding: 5px;
        }

        .groupDiv .font14 {
            width: 108px;
            display: inline-block;
            vertical-align: top;
            text-align: right;
        }

        .inpt3 {
            width: 140px;
        }
    </style>

</head>
<body>
<div class="con_div">
    <c:if test="${!empty auction }">
        <%-- <c:if test="${!empty presale.priceList }">
            <c:forEach var="obj" items="${presale.priceList }">
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
                <a class="navColor" href="/mPresale/index.do">预售管理</a>
                <a class="" href="/mPresale/deposit.do">定金管理</a>
                <a class="" href="/mPresale/presale_set.do">预售送礼设置</a>
            </div>
            <form id="groupForm">
                <div class="groupDiv">
                    <span class="font14"><em>*</em>所属店铺：</span>
                    <c:if test="${!empty shoplist }">
                        <select name="shopId" class="shopId" id="<c:if test="${!empty presale}">${presale.shop_id }</c:if>"
                                <c:if test="${!empty presale}">disabled="disabled"</c:if>>
                            <c:forEach var="shop" items="${shoplist }">
                                <option value="${shop.id }"
                                        <c:if test="${presale.shop_id == shop.id}">selected="selected"</c:if> >${shop.sto_name }</option>
                            </c:forEach>
                        </select>
                    </c:if>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>*</em>选择商品：</span>
                    <div class="goodDiv">
                        <a onclick="choosePro()" href="javascript:void(0);" class="js-add-goods add-goods"
                           <c:if test="${!empty presale && presale.imgUrl != ''}">style="background-image:url(${http }${presale.imgUrl });background-size:cover;background-position:50% 50%;"</c:if>>
                            <c:if test="${empty presale || presale.imgUrl == ''}">
                                <i class="icon-add"></i>
                            </c:if>
                        </a>
                    </div>
                    <input type="hidden" name="id" id="ids" value="<c:if test="${!empty presale }">${presale.id }</c:if>">
                    <input type="hidden" name="productId" id="productId" value="<c:if test="${!empty presale }">${presale.product_id }</c:if>">
                    <input type="hidden" id="defaultProId" value="<c:if test="${!empty presale }">${presale.product_id }</c:if>">
                    <input type="hidden" id="isSpec" value="<c:if test="${!empty presale }">${presale.isSpecifica }</c:if>">
                    <input type="hidden" class="invNum" value=""/>
                    <input type="hidden" class="isSpec" value=""/>
                </div>
                <div class="groupDiv">
                    <span class="font14"></span>
                    <div class="proName red" style="margin-left:100px;">如需修改商品信息，请在商品管理中更新</div>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>&nbsp;&nbsp;</em>商品名称：</span>
                    <span class="proName"><c:if test="${!empty presale }">${presale.proName }</c:if></span>
                </div>
                <div class="groupDiv">
                    <c:set var="proPrice" value="0.00"></c:set>
                    <c:if test="${!empty presale && presale.proPrice>0}">
                        <c:set var="proPrice" value="${presale.proPrice }"></c:set>
                    </c:if>
                    <span class="font14"><em>&nbsp;&nbsp;</em>商品价格：</span>
                    <input type="hidden" class="proPrice" value="${proPrice }"/>
                    <span class="proPrice">￥${proPrice }</span>
                </div>
                <div class="packageDiv" id="createTable" style="display:none;">
                    <div class="groupDiv" style="display: flex;">
                        <span class="font14"><em>&nbsp;&nbsp;</em>商品规格价：</span>
                        <table class="table" style="width:700px;display:none;margin:0px;">
                            <thead>
                            </thead>
                            <tbody id="J_Tbody">
                            </tbody>
                            <!-- <tfoot>
                                <tr height="44px">
                                    <td colspan="5">
                                        <div class="batch-opts">
                                            批量设置 <span class="js-batch-type" style="display: inline;">
                                                <a class="js-batch-price" href="javascript:;"
                                                errormsg="价格最多只能输入6位小数">预售价</a>
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
                            </tfoot> -->
                        </table>
                    </div>
                </div>
                <c:set var="isDeposit" value="1"></c:set>
                <c:if test="${!empty presale }">
                    <c:set var="isDeposit" value="${presale.is_deposit }"></c:set>
                </c:if>
                <div class="groupDiv" style="display: none;">
                    <span class="font14"><em></em>是否需要交纳定金：</span>
                    <input type="checkbox" class="isDeposit" name="isDeposit" value="1"
                           <c:if test="${isDeposit == 1}">checked="checked"</c:if> /> 预售前需要交纳定金
                </div>
                <div class="groupDiv aucDepositDiv" <c:if test="${isDeposit == 0}">style="display:none;"</c:if>>
                    <span class="font14"><em>*</em>交纳定金： </span>
                    <input type="text" class="inpt vali gPrice hidePrice" name="depositPercent" id="depositPercent"
                           maxlength="8" datatype="^[0-9]{1}\d{0,5}(\.\d{1,2})?$" p="num" notNull="1" maxPrice="0"
                           value="<c:if test="${!empty presale && !empty presale.deposit_percent && presale.deposit_percent > 0}">${presale.deposit_percent }</c:if>"
                           placeholder="交纳定金">
                    <!-- <span class="red">定金最多只能输入1到100的正整数</span> -->
                    <span class="red">价格最多只能输入六位小数，如：30.00</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>*</em>开售开始时间： </span>
                    <input type="text" class="inpt vali time_inp" name="saleStartTime" id="sStartTime" notNull="1" datatype=""
                           value="<c:if test="${!empty presale}">${presale.sale_start_time }</c:if>" placeholder="开售活动开始时间">
                    <span class="red">开售开始时间不能为空</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"></span>
                    <span style="color:red;">在活动开始时间前，为交纳定金的时间，在活动开始之后为支付尾款时间</span>
                </div>
                <div class="groupDiv">
                    <span class="font14"><em>*</em>开售结束时间： </span>
                    <input type="text" class="inpt vali time_inp" name="saleEndTime" id="sEndTime" notNull="1" datatype=""
                           value="<c:if test="${!empty presale}">${presale.sale_end_time }</c:if>" placeholder="开售活动结束时间">
                    <span class="red">开售结束时间不能为空</span>
                </div>
                <c:set var="types" value="1"></c:set>
                <c:if test="${!empty presale }">
                    <c:set var="types" value="${presale.types }"></c:set>
                </c:if>
                    <%-- <div class="groupDiv">
                        <span class="font14"><em>*</em>调整类型： </span>
                         <input type="radio" class="types" id="types1" datatype=""
                            value="1" <c:if test="${types == 1}">checked="checked"</c:if> > 上涨
                        <input type="radio" class="types" id="types2" datatype=""
                            value="2" <c:if test="${types == 2}">checked="checked"</c:if> > 下调
                    </div> --%>
                <div class="p_div">
                    <c:if test="${!empty presale && !empty presale.timeList  }">
                        <c:forEach var="time" items="${presale.timeList }" varStatus="i">
                            <div class="groupDiv timeDiv" id="${i.index }">
                                <input type="hidden" class="tId" value="${time.id }">
                                <c:set var="types" value="${time.saleType }"></c:set>
                                <span class="font14"><c:if test="${i.index == 0}"><!-- <em>*</em> -->价格调整： </c:if></span>
                                从 <input type="text" class="inpt3 vali time_inp startTime" id="startTime${i.index }"
                                         value="${time.startTime }" placeholder="开始时间"> 到
                                <input type="text" class="inpt3 vali time_inp endTime" id="endTime${i.index }"
                                       value="${time.endTime }" placeholder="结束时间">

                                <input type="radio" class="types" id="types1" datatype=""
                                       value="1"
                                       <c:if test="${types == 1}">checked="checked"</c:if> > 上涨
                                <input type="radio" class="types" id="types2" datatype=""
                                       value="2"
                                       <c:if test="${types == 2}">checked="checked"</c:if> > 下调

                                <select class="priceType" style="width:100px;" onclick="changePriceType(this);">
                                    <option value="1"
                                            <c:if test="${time.priceType == 1}">selected='selected'</c:if> >按百分比调整
                                    </option>
                                    <option value="2"
                                            <c:if test="${time.priceType == 2}">selected='selected'</c:if> >按价格调整
                                    </option>
                                </select>

                                <!-- <label class="type_label">上涨</label> -->
                                <input type="text" class="inpt3 vali tPrice" id="tPrice"
                                       value="${time.price }" placeholder="调整比例" maxlength="7" datatype="^\d{0,4}(\.\d{1,2})?$"> <em class="unit">%</em>
                                <c:if test="${i.index == 0}">
                                    <a href="javascript:void(0);" class="addNav nav_a red" onclick="addP(this);">新增</a>
                                    <a href="javascript:void(0);" class="clearNav nav_a red" onclick="clearP(this);">清空</a>
                                </c:if>
                                <c:if test="${i.index > 0}">
                                    <a href="javascript:void(0);" class="delNav nav_a red" onclick="delP(this);">删除</a>
                                </c:if>

                                <div style="margin-left:100px;margin-top:10px;">
                                    商品价格从<label class="proPrice red">￥ ${proPrice }</label>
                                    <label class="type_label">上涨</label>到
                                    <label class="price_label red">${proPrice }</label>
                                </div>
                            </div>
                            <script type="text/javascript">
                                timeDefaultObj["${time.id }"] = "${time.id }";
                            </script>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty presale || empty presale.timeList  }">
                        <div class="groupDiv timeDiv" id="0">
                            <input type="hidden" class="tId" value="">
                            <span class="font14"><!-- <em>*</em> -->价格调整： </span>
                            从 <input type="text" class="inpt3 vali time_inp startTime" id="startTime0"
                                     value="" placeholder="开始时间"> 到
                            <input type="text" class="inpt3 vali time_inp endTime" id="endTime0"
                                   value="" placeholder="结束时间">

                            <input type="radio" class="types" id="types1" datatype=""
                                   value="1" checked="checked"> 上涨
                            <input type="radio" class="types" id="types2" datatype=""
                                   value="2"> 下调
                            <select class="priceType" style="width:100px;" onclick="changePriceType(this);">
                                <option value="1">按百分比调整</option>
                                <option value="2">按价格调整</option>
                            </select>
                            <!-- <label class="type_label">上涨</label> -->
                            <input type="text" class="inpt3 vali tPrice" id="tPrice"
                                   value="" placeholder="调整比例" maxlength="7" datatype="^\d{0,4}(\.\d{1,2})?$"> <em class="unit">%</em>
                            <a href="javascript:void(0);" class="addNav nav_a red" onclick="addP(this);">新增</a>
                            <a href="javascript:void(0);" class="clearNav nav_a red" onclick="clearP(this);">清空</a>

                            <div style="margin-left:100px;margin-top:10px;">
                                商品价格从<label class="proPrice red">￥ ${proPrice }</label>
                                <label class="type_label">上涨</label>到
                                <label class="price_label red">${proPrice }</label>
                            </div>
                        </div>

                    </c:if>
                    <input type="hidden" class="saleType" value="${types }"/>
                </div>
                <div class="groupDiv">
                    <span class="font14">订购数量： </span>
                    <input type="text" class="inpt vali" name="orderNum" id="orderNum"
                           maxlength="6" datatype="^\d{0,6}?$" p="num"
                           value="<c:if test="${!empty presale && !empty presale.order_num && presale.order_num > 0}">${presale.order_num }</c:if>"
                           placeholder="订购数量">
                    <span class="red">订购数量最多只能六位正整数，如：30</span>
                </div>
                <div class="groupDiv">
                    <a href="javascript:editPresale();" class="addBtn">确定</a>
                    <a href="${returnUrl }" style="cursor: pointer;color:white;" class="backBtn">返回</a>
                </div>
            </form>
        </c:if>
        <c:if test="${empty shoplist }">
            <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
        </c:if>
    </div>
    <div class="priceObj_div" style="display: none;">
        <div class="groupDiv timeDiv">
            <input type="hidden" class="tId" value="">
            <span class="font14"></span>
            从 <input type="text" class="inpt3 time_inp startTime" id="startTime1"
                     value="" placeholder="开始时间"> 到
            <input type="text" class="inpt3 time_inp endTime" id="endTime1"
                   value="" placeholder="结束时间">

            <input type="radio" class="types" id="types1" datatype=""
                   value="1" checked="checked"> 上涨
            <input type="radio" class="types" id="types2" datatype=""
                   value="2"> 下调

            <select class="priceType" style="width:100px;" onclick="changePriceType(this);">
                <option value="1">按百分比调整</option>
                <option value="2">按价格调整</option>
            </select>
            <!-- <label class="type_label">上涨</label> -->
            <input type="text" class="inpt3 tPrice" id="tPrice"
                   value="" placeholder="调整比例" maxlength="7" datatype="^\d{0,4}(\.\d{1,2})?$"> <em class="unit">%</em>
            <a href="javascript:void(0);" class="delNav nav_a red" onclick="delP(this);">删除</a>

            <div style="margin-left:100px;margin-top:10px;">
                商品价格从<label class="proPrice red">￥ ${proPrice }</label>
                <label class="type_label">上涨</label>到
                <label class="price_label red">${proPrice }</label>
            </div>
        </div>
    </div>
</div>
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);
    var minPrice = 0;

</script>
<script type="text/javascript" src="/js/mall/presale/presale.js"></script>
<script type="text/javascript" src="/js/mall/presale/group_new_inven.js"></script>
</body>
</html>