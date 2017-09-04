function submitOrders() {
    var flag = validateSubmit();
    if (flag) {
        return false;
    }

    var data = getSubmitParams();

}

function getSubmitParams() {
    $("input[name='productAllMoney']").val($(".proMoneyAllOld").val());
    var orderArr = [];
    $(".couponDivs").each(function () {
        var obj = $(".orderDivForm").serializeObject();
        var detailArr = [];
        $(this).find(".mall-item").each(function () {
            var detailObj = $(this).next().serializeObject();
            detailArr.push(detailObj);
        });
        var orderObj = obj;
        orderObj.shopId = $(this).attr("stoId");
        orderObj.wxShopId = $(this).attr("wxShopId");
        orderObj.mallOrderDetail = detailArr;
        orderArr.push(orderObj);
    });
    var oldProductAll = $("#proMoneyAllOld").val();
    var useFenbi = $("#isFenbi").val();
    var userJifen = $("#isJifen").val();
    var couponJson = $("#couponList").val();
    return {
        "order": JSON.stringify(orderArr),
        "productAll": oldProductAll,
        "useFenbi": useFenbi,//是否使用粉币
        "useJifen": userJifen, //是否使用积分
        "couponArr": couponJson
    };
}

function validateSubmit() {
    var receiveId = $('#receiveId').val();
    var deliveryMethod = $('#deliveryMethod').val();
    var orderPayWay = $("#orderPayWay").val();

    if (orderPayWay == null || orderPayWay == "") {
        alert("请选择支付方式");
        return false;
    }
    if (deliveryMethod == 2) {
        var appointName = $("#appointName").val();
        var appointTel = $("#appointTelphone").val();
        var deliveryAddress = $("#deliveryAddress").html();
        var deliveryTime = $("#deliveryTime").html();
        var reg = /^0?(13[0-9]|15[012356789]|17[0678]|18[0123456789]|14[57])[0-9]{8}$/;
        if (appointName == "") {
            alert("提货人姓名不能为空");
            return false;
        }
        if (appointTel == "") {
            alert("手机号码不能为空");
            return false;
        } else if (!reg.test(appointTel)) {
            alert("手机号码有误");
            return false;
        } else {
            $("#appointTel").val(appointTel);
        }
        if (deliveryAddress == "") {
            alert("请选择提货地址");
            return false;
        }
        if (deliveryTime == "") {
            alert("请选择提货时间");
            return false;
        }
        $("#appointmentName").val(appointName);//提货人
        $("#appointmentTelephone").val(appointName);
        $("#appointmentTime").val(deliveryTime);
    }
    var proTypeId = $("#proTypeId").val();
    if (proTypeId > 0) {
        receiveId = "0";
    }
    var isJuliFreight = $(".isJuliFreight").val();
    var mem_latitude = $(".mem_latitude").val();
    var mem_longitude = $(".mem_longitude").val();
    var isKm = $(".isKm").val();
    if ((receiveId == undefined || receiveId == '') && deliveryMethod == 1) {
        alert("请选择收货地址");
        return false;
    } else {
        if (isJuliFreight != null && isJuliFreight != "" && typeof(isJuliFreight) != "undefined" && isJuliFreight == 1 && deliveryMethod == 1) {
            if ((mem_latitude == null || mem_latitude == "" || mem_longitude == null || mem_longitude == "" || isKm == 1) && proTypeId == 0) {
                alert("请重新编辑您的收货地址");
                return false;
            }
        }
        var flowCzPhone = $(".flowCzPhone").val();
        var isFlow = $(".isFlow").val();
        var flowPhone = $("#flowPhone").val();
        if (isFlow != null && isFlow != "" && typeof(isFlow) != "undefined" && (flowCzPhone == "" || flowCzPhone == null || typeof(flowCzPhone) == "undefined")) {
            if (flowPhone == "" || flowPhone == null || typeof(flowPhone) == "undefined") {
                showFlow();
                return false;
            } else {
                $(".flowCzPhone").val(flowPhone);
            }
        }

        if ($("#orderPayWay").val() == 7 && $("#orderMoney").val() * 1 == 0) {
            alert("您的实付金额为0元，无需找人代付，请重新选择支付方式");
            return false;
        }
    }
    $("input[name='receiveId']").val(receiveId);
    $("input[name='orderPayWay']").val(orderPayWay);
    $("input[name='deliveryMethod']").val(deliveryMethod);
    return true;
}