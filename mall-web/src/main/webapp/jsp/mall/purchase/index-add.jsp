<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/laydate/skins/lan/laydate.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/layer/layer.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
    <title>报价管理-新增</title>

</head>
<body>
<form class="add-form" id="orderForm">
    <input type="hidden" name="orderId" value="${order.id}">
    <div class="warp">
        <div class="gt-bread-crumb">
            <span class="gt-bread-crumb-title">报价单详情</span>
        </div>
        <h4 class="second-title" style="margin-top: 0;">报价单信息</h4>


        <div class="gt-form-row">
            <div class="gt-form-left">报价单标题：</div>
            <div class="gt-form-right">
                <input type="text" name="orderTitle" maxlength="20" value="${order.orderTitle}" class="gt-form-input middle add-form-input" placeholder="请填写报价单标题"/>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left">所属公司：</div>
            <div class="gt-form-right">
                <select class="gt-form-select small add-form-input"
                        name="companyId" id="companySelect">
                    <option value="0" selected="selected">请选择公司模板</option>
                    <c:forEach items="${companyModeList}" var="companyMode">
                        <option value="${companyMode.id}"
                            ${order.companyId==companyMode.id?"selected='selected'":""}>${companyMode.company_name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left">付款方式：</div>
            <div class="gt-form-right">
                <label for="" class="add-form-label">
                    <input type="radio" class="gt-form-radio" name="orderType" value="0" id="quan"
                    ${order.orderType!=1?"checked='checked'":""}>全款</label>
                <label for="" class="add-form-label">
                    <input type="radio" class="gt-form-radio" name="orderType" value="1" id="fen"
                    ${order.orderType==1?"checked='checked'":""}>分期
                </label>
            </div>
        </div>
        <div class="gt-form-row fenqi-hide">
            <div class="gt-form-left">&nbsp;</div>
            <div class="gt-form-right">
                <input type="button" class="gt-btn blue middle mar-b10" id="fenqi-add" onclick="clone()" value="添加期数"/>
                <table class="gt-table" style="width: 700px">
                    <thead class="gt-table-thead">
                    <tr>
                        <th>期数</th>
                        <th>付款金额</th>
                        <th>付款时间</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody class="gt-table-tbody fenqi-tbody">
                    <c:if test="${empty termList || termList.size()==0}">
                        <tr class="fenqi-model">
                            <td name="termIndex">第1期</td>
                            <td>
                                <input type="text" class="gt-form-input" onKeyUp='amount(this)' onBlur='overFormat(this)' name="termMoney">
                            </td>
                            <td>
                                <input type="text" class="gt-form-input laydate-icon" name='termTime'
                                       onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" placeholder='请选择日期' readonly='readonly'>
                            </td>
                            <td>
                                <i class="iconfont fenqi-delete" title="删除" onclick="removeTerm(this)">&#xe649;</i>
                            </td>
                        </tr>
                    </c:if>
                    <c:if test="${!empty termList &&  termList.size()>0}">
                        <c:forEach items="${termList}" var="term" varStatus="termIndex">
                            <tr class="fenqi-model">
                                <td style="display: none;">
                                    <input type="hidden" name="termId" value="${term.id}">
                                </td>
                                <td name="termIndex">第${termIndex.index+1}期</td>
                                <td>
                                    <input type="text" class="gt-form-input"
                                           onKeyUp='amount(this)' onBlur='overFormat(this)'
                                           name="termMoney" value="${term.termMoney}">
                                </td>
                                <td>
                                    <input type="text" class="gt-form-input laydate-icon"
                                           name='termTime' value="<fmt:formatDate value='${term.termTime}' pattern='yyyy-MM-dd HH:mm:ss' />"
                                           onclick="laydate({istime: true, format: 'YYYY-MM-DD hh:mm:ss'})" placeholder='请选择日期' readonly="readonly">
                                </td>
                                <td>
                                    <i class="iconfont fenqi-delete" title="删除" onclick="removeTerm(this)">&#xe649;</i>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>

                    </tbody>
                </table>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left">是否在线签合同：</div>
            <div class="gt-form-right">
                <label for="" class="add-form-label">
                    <input type="radio" class="gt-form-radio" name="havaContract" value="0"
                           onclick="qianContract(this)" ${order.haveContract!=1?"checked='checked'":""}>签</label>
                <label for="" class="add-form-label">
                    <input type="radio" class="gt-form-radio" name="havaContract" ${order.haveContract==1?"checked='checked'":""}
                           value="1" onclick="qianContract(this)">不签
                </label>
            </div>
        </div>
        <div class="gt-form-row" id="contractSelect">
            <div class="gt-form-left">合同：</div>
            <div class="gt-form-right">
                <select class="gt-form-select small add-form-input" name="contractId">
                    <option value="0" selected="selected">请选择合同</option>
                    <c:forEach items="${contractList}" var="contractOption">
                        <option value="${contractOption.id}"
                            ${contractOption.id==orderContract.contract_id?"selected='selected'":""}>${contractOption.contract_title}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left">对外报价是否含税：</div>
            <div class="gt-form-right">
                <label for="" class="add-form-label"><input type="radio" class="gt-form-radio" name="haveTax" value="0" ${order.haveTax!=1?"checked='checked'":""}>含</label>
                <label for="" class="add-form-label"><input type="radio" class="gt-form-radio" name="haveTax" value="1" ${order.haveTax==1?"checked='checked'":""}>不含</label>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left ver-top">报价单描述：</div>
            <div class="gt-form-right">
                <textarea name="orderDescribe" rows="" cols="" class="add-form-input" placeholder="请输入报价单描述">${order.orderDescribe}</textarea>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left ver-top">报价单说明：</div>
            <div class="gt-form-right">
                <textarea name="orderExplain" rows="" cols="" class="add-form-input" placeholder="请输入报价单说明">${order.orderExplain}</textarea>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left ver-top">报价单备注：</div>
            <div class="gt-form-right">
                <textarea name="orderRemarks" rows="" cols="" class="add-form-input" placeholder="请输入报价单备注">${order.orderRemarks}</textarea>
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left ver-top">底部二维码：</div>
            <div class="gt-form-right">
                <div class="gt-form-image-box">
                    <div class="gt-form-image-icon">
                        <i class="iconfont"></i>
                    </div>
                    <input type="hidden" value="${order.orderQrcode}" id="hiddenqrcode" name="orderQrcode">
                    <img class="gt-form-image" src="${order.orderQrcode}" id="qrcode">
                </div>
                <div class="gt-form-combination">
                    <div class="gt-form-inputflie-box">
                        <input type="button" value="选择图片" class="gt-btn blue middle" onclick="getImageUrl('qrcode')"/>
                    </div>
                    <span class="gt-from-pic-tips">(建议尺寸300x300px【可上传公众号二维码或小程序体验码】)</span>
                </div>
            </div>
        </div>


        <h4 class="second-title">轮播图设置</h4>
        <div class="mar-tb10">
            <input type="button" value="添加图片" class="gt-btn blue middle" onclick="clone1()"/>
            <span class="warning-txt">(建议尺寸 350*150px 最多可上传三张轮播图)</span>
        </div>
        <table class="gt-table">
            <thead class="gt-table-thead">
            <tr>
                <th>序号</th>
                <th>图片</th>
                <th>链接</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="gt-table-tbody" id="lunbo">
            <c:forEach items="${carouselList}" var="carousel"
                       varStatus="carouselStatus">
                <tr>
                    <td>
                        <input type='checkbox' class='gt-form-checkbox' name='sex'/>
                        <input name="carouselId" type="hidden" value="${carousel.id}">
                        <input type='hidden' id="hidden${carouselStatus.index}" name='carouselImg' value="${carousel.carousel_img}"/>
                    </td>
                    <td>
                        <img class='table-image' src='${carousel.carousel_img}' id="${carouselStatus.index}"/>
                    </td>
                    <td>
                        <input type='text' class='gt-form-input middle width200' name='carouselUrl' placeholder='请输入跳转链接' value="${carousel.carousel_url}">
                    </td>
                    <td>
                        <i class='iconfont' title='替换' onclick="getImageUrl('${carouselStatus.index}')">&#xe61f;</i>
                        <i class='iconfont' title='删除' onclick='removeTerm(this)'>&#xe649;</i>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <h4 class="second-title">商品设置</h4>
        <div class="mar-tb10">
            <input type="button" value="添加商品" class="gt-btn blue middle" onclick="addProduct()"/>
        </div>
        <table class="gt-table">
            <thead class="gt-table-thead">
            <tr>
                <th>商品图片</th>
                <th>商品名称</th>
                <th>原价(￥)</th>
                <th>优惠价(￥)</th>
                <th>数量</th>
                <th>人工费(￥)</th>
                <th>安装费(￥)</th>
                <th>小计(￥)</th>
                <th>运费(￥)</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="gt-table-tbody" id="product">
            <c:forEach var="details" items="${orderdetails}"
                       varStatus="orderIndex">
                <tr>
                    <td>
                        <input type="hidden" name="orderDetailsId" value="${details.id}">
                        <input type="hidden" name="productId" value="${details.productId}">
                        <input type="hidden" name="productImg" value="${details.productImg}">
                        <input type='hidden' name='productDetails' value='${details.productDetails}'>
                        <img class='table-image' src='${details.productImg}'/>
                    </td>
                    <td>
                        <input type="hidden" name="productName" value="${details.productName}">${details.productName}
                    </td>
                    <td>
                        <input type="hidden" name="money" value="${details.money}">￥<fmt:formatNumber type="number" value="${details.money}" pattern="0.00" maxFractionDigits="2"/>
                    </td>
                    <td>
                        <input type='input' name='discountMoney' onchange='reckonCount()' onKeyUp='amount(this)'
                               onBlur='overFormat(this)' value='${details.discountMoney}' class='commodity-input'/>
                    </td>
                    <td>
                        <input type='input' name='count' value='${details.count}' onchange='reckonCount()'
                               onkeyup='keyup1(this)' onafterpaste='keyup1(this)' class='commodity-input'/>
                    </td>
                    <td>
                        <input type='input' name='laborCost' value='${details.laborCost}' onchange='reckonCount()' value='0'
                               onKeyUp='amount(this)' onBlur='overFormat(this)' class='commodity-input'/>
                    </td>
                    <td>
                        <input type='input' name='installationFee' value='${details.installationFee}' onchange='reckonCount()'
                               value='0' onKeyUp='amount(this)' onBlur='overFormat(this)' class='commodity-input'/>
                    </td>
                    <td>
                        <input type='input' name='allMoney' value='${details.allMoney}' readonly='readonly' class='commodity-input'/>
                    </td>
                    <td>
                        <input type='input' name='freight' value='${details.freight}' class='commodity-input'
                               onchange='reckonCount()' onKeyUp='amount(this)' placeholder='只显示'/>
                    </td>
                    <td>
                        <i class='iconfont' title='删除' onclick='removeTerm(this)'>&#xe649;</i>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="text-right width900 mar-tb5">
				<span class="warning-txt">总运费： ￥
                    <input type="text" value="${empty order.freight?0.00:order.freight}" id="orderFreight" style="width: 80px" onchange="reckonCount()"
                           name="orderFreight" onKeyUp="amount(this)" onBlur="overFormat(this)">
                </span>
            <span class="warning-txt">
                    合计：￥<input type="text" name="orderAllMoney" style="width: 80px" value="${order.allMoney}" readonly="readonly">
				</span>
        </div>
        </table>

        <div class="text-center width900">
            <input type="button" class="gt-btn blue middle mar-r20" onclick="saveOrder()" value="保存"/>
            <input type="button" onclick="history.go(-1)" class="gt-btn default middle" value="返回"/>
        </div>
    </div>
</form>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<script type="text/javascript">

    var winParents = window.parent;
    var winScreen = window.screen;
    var leftNav = $(window.parent.document.getElementById('dao-hang0')).height();

    function loadWindow() {
        //清空内容高度
        var rightHeight = document.body.scrollHeight;//网页正文全文高，包括有滚动条时的未见区域
        if (rightHeight > leftNav) {
            leftHeight = rightHeight;
        } else {
            leftHeight = leftNav;
        }
        var screenHeight = winScreen.availHeight;//屏幕可用工作区的高
        if (leftHeight < screenHeight) {
            leftHeight = screenHeight;
        }
        //改变内容高度
        $(winParents.document.getElementById('ifr')).height(leftHeight);
        $(winParents.document.getElementById('content')).height(leftHeight);
        //改变菜单栏的高度
        $(winParents.document.getElementById('nav')).height(leftHeight + 167);
    }

    function checked() {
        var _id = $('input[name="orderType"]:checked').attr('id');
        if (_id == 'fen') {
            $('.fenqi-hide').show()
        } else {
            $('.fenqi-hide').hide()
        }
        loadWindow();
    }


    $(function () {

        checked();
        loadWindow();
        // 分期
        $("input[name='orderType']").change(function () {
            checked();
        });


        $('#fenqi-add').click(function () {
            var _fenqi = $('.fenqi-model').clone();
            $('.fenqi-tbody')
        })

        if ("${order.haveContract}" == 1) {
            $("#contractSelect").hide();
        }

    });

    var imageId = "";
    //<c:if test="${empty carouselList}">
    var idIndex = 0;
    //</c:if>
    //<c:if test="${!empty carouselList}">
    var idIndex = "${carouselList.size()}";
    //</c:if>
    function getImageUrl(id) {
        imageId = id;
//        materiallayer();
        fhmater(0);
    }
    //素材库里面返回信息
    function image(id, url) {
        $("#" + imageId).attr("src", url);
        $("#hidden" + imageId).val(url);
    }


    function keyup1(_this) {
        _this.value = _this.value.replace(/\D/g, '')
    }

    function removeTerm(_this) {
        //询问框
        layer.confirm("确定要删除该条数据吗?", {
            shade: [0.1, '#fff'],
            btn: ['确定', '取消'],
            offset: '10%'
        }, function () {
            $(_this).parent().parent().remove();
            reckonCount();
            var termIndexs = document.getElementsByName('termIndex');
            for (var i = 0; i < termIndexs.length; i++) {
                $(termIndexs[i]).text("第" + accSub(i, 1) + "期");
            }
            layer.closeAll();
        }, function () {
        });
    }

    function clone() {
        var termMoney = document.getElementsByName('termMoney');
        var index = accSub(termMoney.length, 1);
        var content = " <tr class='fenqi-model'>" +
            " <td name='termIndex'>第" + index + "期</td>" +
            " <td><input type='text' class='gt-form-input' onKeyUp='amount(this)' onBlur='overFormat(this)' name='termMoney'></td>" +
            " <td><input type='text' class='gt-form-input laydate-icon'  name='termTime'  onclick='laydate({istime: true, format: \"YYYY-MM-DD hh:mm:ss\"})'  placeholder='请选择日期'  readonly='readonly' /></td>" +
            " <td>" +
            " <i class='iconfont fenqi-delete' title='删除' onclick='removeTerm(this)'>&#xe649;</i>" +
            " </td>" +
            " </tr>";
        $('.fenqi-tbody').append(content);
        loadWindow();
    }

    function clone1() {
        var sex = document.getElementsByName('sex');
        if (sex.length >= 3) {
            window.alertMsg("轮播图最多添加三张!");
            return;
        }
        if (idIndex == undefined || idIndex == null) {
            idIndex = 0;
        }

        idIndex++;
        var content = " <tr>" +
            "<td><input type='checkbox' class='gt-form-checkbox' name='sex' /><input type='hidden' name='carouselImg' id='hidden" + idIndex + "' /></td>" +
            "<td><img class='table-image' src='' id='" + idIndex + "'/></td>" +
            "<td><input type='text' class='gt-form-input middle width200' name='carouselUrl' placeholder='请输入跳转链接'></td>" +
            "<td><i class='iconfont' title='替换' onclick='getImageUrl(" + idIndex + ")'>&#xe61f;</i> <i class='iconfont' title='删除' onclick='removeTerm(this)'>&#xe649;</i></td>" +
            "</tr>";
        $("#lunbo").append(content);
        loadWindow();

    }

    function addProduct() {
        parentOpenIframe("选择商品", "600px", "480px", "/purchaseOrder/getProductByGroup.do");
//        parent.openIframe("选择商品", "600px", "480px", "/purchaseOrder/getProductByGroup.do");//check==0代表多选，check==1代表单选
    }


    function saveOrder() {
        var orderType = $('input[name="orderType"]:checked').val();
        var havaContract = $('input[name="havaContract"]:checked').val();
        var orderTitle = $("input[name='orderTitle']").val();
        var orderQrcode = $("input[name='orderQrcode']").val();
        var carouselImg = document.getElementsByName('carouselImg');
        var carouselUrl = document.getElementsByName('carouselUrl');
        var productIds = document.getElementsByName('productId');
        var discountMoney = document.getElementsByName('discountMoney');
        var termAllMoney = 0;
        var is_true = true;
        if ($.trim(orderTitle) == "") {
            parentAlertMsg("请填写报价单的标题!");
            return;
        }
        if ($("#companySelect option:selected").val() == 0 || $("#companySelect option:selected").val() == "0") {
            parentAlertMsg("请选择所属公司!");
            return;
        }
        if (havaContract == "0" || havaContract == 0) {
            if ($("#contractSelect option:selected").val() == 0 || $("#contractSelect option:selected").val() == "0") {
                parentAlertMsg("请选择合同!");
                return;
            }
        }
        if (orderType == "1" || orderType == 1) {
            var termMoney = document.getElementsByName('termMoney');
            var termTime = document.getElementsByName('termTime');
            for (var i = 0; i < termMoney.length; i++) {
                if ($(termMoney[i]).val() <= 0) {
                    is_true = false;
                    parentAlertMsg("分期金额必须大于零!");
                    return;
                }
                termAllMoney = accSub(termAllMoney, $(termMoney[i]).val());
            }
            for (var i = 0; i < termTime.length; i++) {
                if ($(termTime[i]).val() == "") {
                    is_true = false;
                    parentAlertMsg("分期时间必须填写!");
                    return;
                }
                if (i > 0) {
                    var d1 = new Date($(termTime[i - 1]).val().replace(/\-/g, "\/"));
                    var d2 = new Date($(termTime[i]).val().replace(/\-/g, "\/"));
                    if (d1 > d2) {
                        is_true = false;
                        parentAlertMsg("分期时间分配错误,后期时间必须大于前期!");
                        return;
                    }
                }
            }
            if (is_true && accClaer(termAllMoney, $("input[name='orderAllMoney']").val()) != "0.00" && accClaer(termAllMoney, $("input[name='orderAllMoney']").val()) != 0.00 && accClaer(termAllMoney, $("input[name='orderAllMoney']").val()) != 0) {
                parentAlertMsg("您的分期金额和报价单总金额不匹配!");
                return;
            }
        }

        if (orderQrcode == "") {
            parentAlertMsg("请上传报价单底部的二维码图!");
            return;
        }
        if (carouselUrl == null || carouselUrl.length == 0) {
            parentAlertMsg("请设置轮播图!");
            return;
        }
        for (var i = 0; i < carouselUrl.length; i++) {
            if (carouselImg[i].value == "") {
                is_true = false;
                parentAlertMsg("请选择轮播图片!");
                return;
            } else if (!checkUrl(carouselUrl[i].value)) {
                is_true = false;
                parentAlertMsg("轮播图链接地址不正确!");
                return;
            }

        }
        if (productIds.length <= 0) {
            parentAlertMsg("请选择商品!");
            return;
        }
        for (var i = 0; i < discountMoney.length; i++) {
            var money = discountMoney[i].value;
            if (money - 0 <= 0) {
                is_true = false;
                parentAlertMsg("商品的优惠价格不能小于或等于零!");
                return;
            }
        }
        if (is_true) {
            $.ajax({
                url: "/purchaseOrder/saveOrder.do",
                data: $('#orderForm').serialize(),
                type: "POST",
                dataType: "JSON",
                success: function (data) {
                    if (data.result == true || data.result == "true") {
                        parentAlertMsg("报价单保存成功!")
                        location.href = "/purchaseOrder/orderIndex.do";
                    } else {
                        parentAlertMsg("报价单保存失败!")
                    }
                }
            });
        }
    }

    //合同的显示和隐藏
    function qianContract(_this) {
        if (_this.value == 0 || _this.value == "0") {
            $("#contractSelect").show();
            return;
        }
        $("#contractSelect").hide();
    }

    //计算订单总额
    function reckonCount() {
        var discountMoney = document.getElementsByName('discountMoney');
        var count = document.getElementsByName('count');
        var installationFee = document.getElementsByName('installationFee');
        var laborCost = document.getElementsByName('laborCost');
        var allMoney = document.getElementsByName('allMoney');
        var orderAllMoney = 0;
        var orderFreight = $("#orderFreight").val();
        for (var i = 0; i < discountMoney.length; i++) {
            $(allMoney[i]).val(accSub(accSub(accMul(discountMoney[i].value, count[i].value), installationFee[i].value), laborCost[i].value));
            orderAllMoney = accSub(orderAllMoney, $(allMoney[i]).val());
        }
        $("input[name='orderAllMoney']").val(accSub(orderAllMoney, orderFreight));
    }


    <!-- 加法函数升级 -->
    function accSub(arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2));
        n = (r1 >= r2) ? r1 : r2;
        return ((arg1 * m + arg2 * m) / m).toFixed(n);
    }
    <!-- 去除多余空格 -->
    function trim(str) {
        return str.replace(/^(\s|\xA0)+|(\s|\xA0)+$/g, '');
    }
    <!-- 减法函数 -->
    function accClaer(arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length
        } catch (e) {
            r1 = 0
        }
        try {
            r2 = arg2.toString().split(".")[1].length
        } catch (e) {
            r2 = 0
        }
        m = Math.pow(10, Math.max(r1, r2))
        n = (r1 >= r2) ? r1 : r2;
        return ((arg1 * m - arg2 * m) / m).toFixed(n);
    }
    <!-- 乘法方法 -->
    function accMul(arg1, arg2) {
        var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length
        } catch (e) {
        }
        try {
            m += s2.split(".")[1].length
        } catch (e) {
        }
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m)
    }

    function checkUrl(urlString) {
        var reg = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
        if (urlString == "" || !reg.test(urlString)) {
            return false;
        }
        return true;
    }


    /**
     * 实时动态强制更改用户录入
     * arg1 inputObject
     **/
    function amount(th) {
        var regStrs = [['^0(\\d+)$', '$1'], //禁止录入整数部分两位以上，但首位为0
            ['[^\\d\\.]+$', ''], //禁止录入任何非数字和点
            ['\\.(\\d?)\\.+', '.$1'], //禁止录入两个以上的点
            ['^(\\d+\\.\\d{2}).+', '$1'] //禁止录入小数点后两位以上
        ];
        for (i = 0; i < regStrs.length; i++) {
            var reg = new RegExp(regStrs[i][0]);
            th.value = th.value.replace(reg, regStrs[i][1]);
        }
    }

    /**
     * 录入完成后，输入模式失去焦点后对录入进行判断并强制更改，并对小数点进行0补全
     * arg1 inputObject
     * 这个函数写得很傻，是我很早以前写的了，没有进行优化，但功能十分齐全，你尝试着使用
     * 其实有一种可以更快速的JavaScript内置函数可以提取杂乱数据中的数字：
     * parseFloat('10');
     **/
    function overFormat(th) {
        var v = th.value;
        if (v === '') {
            v = '0.00';
        } else if (v === '0') {
            v = '0.00';
        } else if (v === '0.') {
            v = '0.00';
        } else if (/^0+\d+\.?\d*.*$/.test(v)) {
            v = v.replace(/^0+(\d+\.?\d*).*$/, '$1');
            v = inp.getRightPriceFormat(v).val;
        } else if (/^0\.\d$/.test(v)) {
            v = v + '0';
        } else if (!/^\d+\.\d{2}$/.test(v)) {
            if (/^\d+\.\d{2}.+/.test(v)) {
                v = v.replace(/^(\d+\.\d{2}).*$/, '$1');
            } else if (/^\d+$/.test(v)) {
                v = v + '.00';
            } else if (/^\d+\.$/.test(v)) {
                v = v + '00';
            } else if (/^\d+\.\d$/.test(v)) {
                v = v + '0';
            } else if (/^[^\d]+\d+\.?\d*$/.test(v)) {
                v = v.replace(/^[^\d]+(\d+\.?\d*)$/, '$1');
            } else if (/\d+/.test(v)) {
                v = v.replace(/^[^\d]*(\d+\.?\d*).*$/, '$1');
                ty = false;
            } else if (/^0+\d+\.?\d*$/.test(v)) {
                v = v.replace(/^0+(\d+\.?\d*)$/, '$1');
                ty = false;
            } else {
                v = '0.00';
            }
        }
        th.value = v;
    }

</script>
</body>
</html>
