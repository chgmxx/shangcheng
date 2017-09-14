function hideLay() {
    $(".fade").hide();
    $("#payLayer").hide();
    $("#coupon").hide();
}

function showLay() {
    $(".fade").show();
    $("#payLayer").show();
}
function payWay(num) {
    if (num == 1) {
        $('.orderPayWay').val(1);
        $('#onlinePayment').html("微信支付 >");
    } else if (num == 2) {
        $('.orderPayWay').val(2);
        $('#onlinePayment').html("储值卡支付 >");
    } else if (num == 3) {
        $('.orderPayWay').val(3);
        $('#onlinePayment').html("支付宝支付 >");
    }
    hideLay();
}
var userid = $("input.userid").val();
/**
 * 交纳保证金
 */
$(".recog-btn").click(function () {
    var isCheck = $("#agrea").is(":checked");
    var orderPayWay = $("input.orderPayWay").val();
    if (!isCheck) {
        alert("请同意阅读协议");
    } else {
        var index = layer.open({
            title: "",
            content: "",
            type: 2,
            shadeClose: false
        });
        var data = $("#marginForm").serializeObject();
        $.ajax({
            url: "mAuction/79B4DE7C/addMargin.do",
            type: "POST",
            data: {margin: JSON.stringify(data)},
            timeout: 300000,
            dataType: "json",
            success: function (data) {
                var userid = $(".userid").val();
                layer.closeAll();
                if (data != null) {
                    if (data.result == true) {
                        var marginMoney = $('.marginMoney').val();
                        if (data.payWay === 1 || data.payWay == 3) {
                            location.href = data.payUrl;
                        } else {
                            location.href = "/mAuction/79B4DE7C/payWay.do?orderMoney=" + marginMoney + "&orderId=" + data.id + "&payWay=" + data.payWay + "&out_trade_no=" + data.no + "&busId=" + userid;
                        }
                        /*if (data.payWay == 2) {//储值卡支付，进入支付回调

                         } else if (data.payWay == 3) {//支付宝支付
                         var path = $("input.path").val();
                         var alipaySubject = $("input.alipaySubject").val();
                         var return_url = path + "/phoneOrder/" + data.id + "/" + data.busId + "/3/79B4DE7C/orderPaySuccess.do";
                         location.href = "/alipay/79B4DE7C/alipayApi.do?out_trade_no=" + data.no + "&subject=" + alipaySubject + "&total_fee=" + marginMoney + "&busId=" + data.busId + "&model=3&businessUtilName=mallAuctionAlipayNotifyUrlBuinessService&return_url=" + return_url;
                         } else {
                         location.href = "/wxPay/79B4DE7C/wxMallAnAuction.do?id=" + data.id;
                         }*/
                    } else {
                        var tip = layer.open({
                            content: data.msg,
                            btn: ['确认'],
                            shadeClose: false,
                            yes: function () {
                                if (data.code == 1) {
                                    var proId = $(".proId").val();
                                    var aucId = $(".aucId").val();
                                    var shopId = $(".shopId").val();
                                    location.href = "mAuction/" + proId + "/" + shopId + "/" + aucId + "/79B4DE7C/auctiondetail.do?uId=" + userid;
                                }
                                layer.closeAll();
                            }
                        });
                    }
                }
            }, error: function () {
                alert("交纳保证金失败，稍后请重新交纳");
                layer.closeAll();
            }
        });
    }

});


/** 自定义一个序列化表单的方法* */
$.fn.serializeObject = function () {
    var o = {};
    $(this).find("input").each(function (index) {
        if ($(this).attr("name") != undefined) {
            if ($(this).attr("type") == "text" || $(this).attr("type") == "password" || $(this).attr("type") == "hidden") {
                o[$(this).attr("name")] = $(this).val();
            }

            if ($(this).attr("type") == "checkbox" || $(this).attr("type") == "radio") {
                if ($(this).is(":checked")) {
                    o[$(this).attr("name")] = 1;
                } else {
                    o[$(this).attr("name")] = 0;
                }
            }
        }
    });

    $(this).find("select").each(function (index) {
        if ($(this).attr("name") != undefined) {
            o[$(this).attr("name")] = $(this).val();
        }
    });

    $(this).find("textarea").each(function (index) {
        if ($(this).attr("name") != undefined) {
            o[$(this).attr("name")] = $(this).val();
        }
    });
    return o;
};

