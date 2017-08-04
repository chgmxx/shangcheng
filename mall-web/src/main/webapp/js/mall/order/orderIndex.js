$("#submit").click(function () {
    var type = $(".type").val();
    var noReturnReason = $(".noReturnReason").val();
    var returnAddress = $(".returnAddress").val();
    var status = 1;
    var flag = true;
    var payParams = new Object();
    var tip = "";
    var returnOrder = {
        id: $(".returnId").val(),
        orderDetailId: $(".detailId").val(),
        orderId: $(".orderId").val()
    };
    if (type == -1) {// 拒绝退款
        status = -1;
        if (noReturnReason == null || $.trim(noReturnReason) == "") {
            alert("请输入拒绝退款理由")
            $(".noReturnReason").focus();
            flag = false;
        } else {
            returnOrder.noReturnReason = noReturnReason;
            returnOrder.status = -1;
        }
        tip = "拒绝退款";
    } else if (type == 1) {// 同意买家退款
        tip = "同意买家退款";
        payParams = $("#returnForm").serializeObject();
        returnOrder.status = 1;
    } else if (type == 2) {// 同意买家退款退货
        tip = "同意买家退款退货";
        if (returnAddress == null || $.trim(returnAddress) == "") {
            alert("请输入退货地址")
            $(".returnAddress").focus();
            flag = false;
        } else {
            //payParams = $("#returnForm").serializeObject();
            returnOrder.returnAddress = returnAddress;
            returnOrder.status = 2;
        }
    } else if (type == 3) {// 确认收货并退款
        tip = "确认收货并退款";
        payParams = $("#returnForm").serializeObject();
        returnOrder.status = 5;
    } else if (type == 4) {// 拒绝确认收货
        tip = "拒绝确认收货";
        returnOrder.status = 4;
    } else if (type == 5) {// 同意退货，修改退货信息
        tip = "修改退货信息";
        if (returnAddress == null || $.trim(returnAddress) == "") {
            alert("请输入退货地址")
            $(".returnAddress").focus();
            flag = false;
        } else {
            //payParams = $("#returnForm").serializeObject();
            returnOrder.returnAddress = returnAddress;
        }
    }
    if (flag) {
        var orderPayWay = $("input.orderPayWay").val();
        if ((type == 1 || type == 3) && orderPayWay == 9) {
            parent.openIframeNoScoll2("钱包支付", 700, 650, '4%', url, true);
        } else {
            updReturn(payParams, returnOrder, tip, type);
        }
    }
});

function updReturn(order, returnOrder, tips, type) {
    var layerLoad = parent.layer.load(1, {
        shade: [0.3, '#000'],
        offset: "30%"
    });
    $.ajax({
        type: "post",
        url: "/mallOrder/updateReturn.do",
        data: {
            order: JSON.stringify(order),
            "return": JSON.stringify(returnOrder)
        },
        dataType: "json",
        success: function (data) {
            parent.layer.close(layerLoad);
            if (data.flag == true) {// 重新登录
                alert(tips + "成功");
                parent.location.href = "/mallOrder/indexstart.do";
            } else {// 编辑失败
                if (typeof data.msg != "undefined" && data.msg != null && data.msg != "" && type == 1) {
                    alert(data.msg);
                } else {
                    alert(tips + "失败");
                }
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            parent.layer.close(layerLoad);
            alert(tips + "失败");
            return;
        }
    });
}
jQuery.prototype.serializeObject = function () {
    var obj = new Object();
    $.each(this.serializeArray(), function (index, param) {
        if (!(param.name in obj)) {
            obj[param.name] = param.value;
        }
    });
    return obj;
}; 