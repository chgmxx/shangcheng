/**
 * 积分兑换
 */
function integral(isSave, type) {
    var memberId = $(".memberId").val();
    var memberIntegrals = $(".memberIntegrals").val();//用户积分
    var proIntegral = $(".proIntegral").val();//兑换商品所用积分
    var proTypeId = $(".proTypeId").val();
    var isMember = $(".isMember").val();
    var isNoStart = $("input.isNoStart").val();
    var isEnd = $("input.isEnd").val();
    var xIds = $(".xIds").val();

    var productId = $(".productId").val();
    var integralId = $(".integralId").val();
    var productSpecificas = $(".productSpecificas").val();
    var productNum = $(".productNum").val();
    var userid = $(".userid").val();

    var data = {
        productId: productId,
        integralId: integralId,
        productNum: productNum,
        uId: userid
    };
    if (integralId == null || integralId == "" || typeof(integralId) == "undefined") {
        $(".errorMsg").html("积分商品已失效或被删除");
        $(".failedSection").show();
        return false;
    }
    if (type == 1) {
        var flowPhone = $("input.flowPhone").val();
        if (flowPhone == null || flowPhone == "" || typeof(flowPhone) == "undefined") {
            $(".errorMsg").html("请输入要兑换的手机号码");
            $(".failedSection").show();
            return false;
        }
        var phoneFlag = Mobilephone(flowPhone);
        if (!phoneFlag) {
            $(".errorMsg").html("请填写正确的手机号");
            $(".failedSection").show();
            return false;
        }
        data["flowPhone"] = flowPhone;
    }
    if (isNoStart != null && isNoStart != "" && typeof(isNoStart) != "undefined") {
        if (isNoStart == 1) {
            $(".errorMsg").html("兑换时间还未开始，请耐心等待");
            $(".failedSection").show();
            return false;
        }
    }
    if (isEnd != null && isEnd != "" && typeof(isEnd) != "undefined") {
        if (isEnd == 1) {
            $(".errorMsg").html("兑换时间已结束，请重新选择商品进行兑换");
            $(".failedSection").show();
            return false;
        }
    }
    if (memberId == null || memberId == "" || typeof(memberId) == "undefined") {
        toLogin();
        return false;
    }
    if (isMember == null || isMember == "" || typeof(isMember) == "undefined") {
        $(".errorMsg").html("您还不是会员，不能兑换商品");
        $(".failedSection").show();
        return false;
    }
    if (memberIntegrals != "" && proIntegral != "") {
        if (memberIntegrals * 1 < proIntegral * 1) {
            $(".errorMsg").html("您的积分不足，不能兑换该商品");
            $(".failedSection").show();
            return false;
        }
    }
    var flag = isInvNum(0);
    if (!flag) {
        $(".errorMsg").html("您的商品库存不足");
        $(".failedSection").show();
        return false;
    }
    if (xIds != null && xIds != "" && typeof(xIds) != "undefined") {
        var invNum = getInvNum(xIds);
        if (invNum * 1 <= 0) {
            $(".errorMsg").html("您选择的规格库存不够，请重新选择");
            $(".failedSection").show();
            return false;
        }
        data["productSpecificas"] = xIds;
    }
    $(".orders").val(JSON.stringify(data));
    if (proTypeId == 4 && isSave == 0) {
        flowDia();
    } else {
        ajaxSubmit(data, proTypeId)
    }
}
function flowDia() {
    $(".flowSection").css("display", "flex");
}
function showDias(type) {
    $(".virtualSection .content em").html($(".name_div").html());
    $(".virtualSection").show();
    if (type == 3) {
        $(".virtualSection .register-btn-ok").show();
        $(".virtualSection .is_ok").hide();
    } else {
        $(".virtualSection .register-btn-ok").hide();
        $(".virtualSection .is_ok").show();
    }
}
function oks() {
    var userid = $(".userid").val();
    var shopId = $(".shopId").val();
    location.href = "/phoneIntegral/79B4DE7C/recordList.do?uId=" + userid + "&shopId=" + shopId;
}
function ajaxSubmit(data, proTypeId) {
    var layerindex = layer.open({
        type: 2,
        content: '兑换中',
        shadeClose: false
    });
    $.ajax({
        type: "post",
        url: "/phoneIntegral/79B4DE7C/recordIntegral.do",
        data: data,
        dataType: "json",
        success: function (data) {
            layer.closeAll();
            if (data.code == 1) {
                $(".dialog-wrap").hide();
                if (proTypeId != null && proTypeId != "") {
                    if (proTypeId == 1 || proTypeId == 2 || proTypeId == 3) {//虚拟物品
                        showDias(proTypeId);
                    } else if (proTypeId == 4) {//流量
                        showDias(proTypeId);
                    } else if (proTypeId == 0) {
                        $('#toAddList').submit();
                    }
                } else {
                    oks();
                }
            } else if (data.code == -2) {
                toLogin();
                return false;
            } else {// 编辑失败
                var msg = "兑换失败，请稍后重试";
                if (data.msg != null && data.msg != "") {
                    msg = data.msg;
                }
                $(".errorMsg").html(msg);
                $(".failedSection").show();
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.closeAll();
            $(".errorMsg").html("兑换失败，请稍后重试");
            $(".failedSection").show();
            return;
        }
    });
}
$(".cancel").click(function () {
    $(this).parents("section").hide();
});

function details() {
    var memberId = $(".memberId").val();
    location.href = "/duofenCardPhoneController/79B4DE7C/memberCardList.do?memberId=" + memberId;
}

var numVal = 1;
var addBtn = $('.shopping-add');
var reduceBtn = $('.shopping-reduce');
var displayBtn = $('.shopping-detail-number input');
displayBtn.blur(function () {
    var flag = isInvNum(0);
    if (!flag) {
        return false;
        displayBtn.val(numVal);
    }
    numVal = displayBtn.val();
    $(".nums_span").html(numVal);
});
//数量增加操作
addBtn.click(function () {
    var nums = parseInt(displayBtn.val());
    var flag = isInvNum(1);
    if (!flag) {
        return false;
    }
    displayBtn.val(nums + 1);

    numVal = displayBtn.val();
    $(".nums_span").html(numVal);
});
//数量减少操作
reduceBtn.click(function () {
    if (parseInt(displayBtn.val()) > 0) {
        var flag = isInvNum(-1);
        if (!flag) {
            return false;
        }
        displayBtn.val(parseInt(displayBtn.val()) - 1);

        numVal = displayBtn.val();
        $(".nums_span").html(numVal);
    }
});
function isInvNum(type) {
    var nums = parseInt(displayBtn.val());
    if (type == 1) {
        nums += 1;
    } else if (type == -1) {
        nums -= 1;
    }
    var max = displayBtn.attr("max");
    if (max != null && max != "") {
        max = max * 1;
        if (max >= 0) {
            if (max < nums) {
                displayBtn.val(numVal);
                return false;
            }
        }
    }
    var min = displayBtn.attr("min");
    if (min != null && min != "") {
        min = min * 1;
        if (min >= 0) {
            if (min > nums) {
                displayBtn.val(numVal);
                return false;
            }
        }
    }
    return true;
}
isDisable();
onloadSpec();
function onloadSpec() {
    if ($("input.xIds").length > 0 && $(".spe_value_p span").length > 0) {
        var xIds = $("input.xIds").val();
        if (xIds != null && xIds.length > 0) {
            var xIdArr = xIds.split(",");
            var speValues = "";
            var chooseSpec = "";
            $(".spe_value_p span").each(function () {
                var valueIds = $(this).attr("value_id");
                if (valueIds != null && valueIds != "") {
                    for (var str in xIdArr) {
                        if (xIdArr[str] == valueIds) {
                            $(this).addClass("shopping-detail-info-checked");
                            chooseSpec += $(this).text() + ",";
                            if (speValues != "") {
                                speValues += ",";
                            }
                            speValues += $(this).text();
                            break;
                        }
                    }
                }
            });
            if (chooseSpec != null && chooseSpec != "") {
                $(".chooseSpe").html(chooseSpec);
            }
            if (speValues != null && speValues != "") {
                $("input.specValue").val(speValues);
            }
            var invNum = getInvNum(xIds);
            if (invNum * 1 <= 0) {
                if (!$(".submit_btn").hasClass("btn_disable")) {
                    $(".submit_btn").html("库存不够，请重新选择");
                    $(".submit_btn").addClass("btn_disable");
                }
            } else {
                if ($(".submit_btn").hasClass("btn_disable") && isDisable()) {
                    $(".submit_btn").html("立即兑换");
                    $(".submit_btn").removeClass("btn_disable");
                }
            }
        }
    } else {
        var productInvNum = $(".productInvNum").val();
        if (productInvNum != null && productInvNum != "" && typeof(productInvNum) != "undefined") {
            productInvNum = productInvNum * 1;
            if (productInvNum >= 0) {
                displayBtn.attr("max", productInvNum);
            }
        }
    }
}
function isDisable() {
    var memberId = $("input.memberId").val();
    var memberIntegrals = $(".memberIntegrals").val();//用户积分
    var proIntegral = $(".proIntegral").val();//兑换商品所用积分
    var isMember = $(".isMember").val();
    var isNoStart = $("input.isNoStart").val();
    var isEnd = $("input.isEnd").val();
    var integralId = $(".integralId").val();
    if (integralId == null || integralId == "" || typeof(integralId) == "undefined") {
        $(".submit_btn").html("积分商品已失效或被删除");
        $(".submit_btn").addClass("btn_disable");
        return false;
    }
    if (isNoStart != null && isNoStart != "" && typeof(isNoStart) != "undefined") {
        if (isNoStart == 1) {
            $(".submit_btn").html("兑换时间还未开始，请耐心等待");
            $(".submit_btn").addClass("btn_disable");
            return false;
        }
    }
    if (isEnd != null && isEnd != "" && typeof(isEnd) != "undefined") {
        if (isEnd == 1) {
            $(".submit_btn").html("兑换时间已结束，请重新选择商品进行兑换");
            $(".submit_btn").addClass("btn_disable");
            return false;
        }
    }
    if (memberId == null || memberId == "" || typeof(memberId) == "undefined") {
        $(".submit_btn").html("还未登陆");
        $(".submit_btn").addClass("btn_disable");
        return false;
    }
    if (isMember == null || isMember == "" || typeof(isMember) == "undefined") {
        $(".submit_btn").html("您还不是会员，不能兑换商品");
        $(".submit_btn").addClass("btn_disable");
        return false;
    }
    if (memberIntegrals != "" && proIntegral != "") {
        if (memberIntegrals * 1 < proIntegral * 1) {
            $(".submit_btn").html("您的积分不足，不能兑换该商品");
            $(".submit_btn").addClass("btn_disable");
            return false;
        }
    }
    return true;
}

$('.shopping-detail-info span').on('click', function () {
    $(this).addClass('shopping-detail-info-checked').siblings('.shopping-detail-info span').removeClass('shopping-detail-info-checked');

    var chooseSpec = "";
    var xIds = "";
    var speValues = "";
    $objeach = $(".spe_value_p span.shopping-detail-info-checked");
    $objeach.each(function (i) {
        var valueIds = $(this).attr("value_id");
        if (valueIds != null && valueIds != "") {
            chooseSpec += $(this).text() + ",";
            if (i == $objeach.length - 1) {
                xIds += valueIds;
                speValues += $(this).text();
            } else {
                xIds += valueIds + ",";
                speValues += $(this).text() + ",";
            }
        }
    });
    if (chooseSpec != null && chooseSpec != "") {
        $(".chooseSpe").html(chooseSpec);
    }
    if (speValues != null && speValues != "") {
        $("input.specValue").val(speValues);
    }
    if (xIds != null && xIds != "") {
        $("input.xIds").val(xIds);

        var invNum = getInvNum(xIds);
        if (invNum * 1 <= 0) {
            if (!$(".submit_btn").hasClass("btn_disable")) {
                $(".submit_btn").html("库存不够，请重新选择");
                $(".submit_btn").addClass("btn_disable");
            }
        } else {
            if ($(".submit_btn").hasClass("btn_disable") && isDisable()) {
                $(".submit_btn").html("立即兑换");
                $(".submit_btn").removeClass("btn_disable");
            }
        }
        var flag = isInvNum(0);
        if (!flag) {
            displayBtn.val(invNum);
        }
    }
});

function getInvNum(xIds) {
    var obj = $("input.guigePrices[x_ids='" + xIds + "']");
    var invNum = obj.attr("inv_num");
    if (invNum != null && invNum != "" && typeof(invNum) != "undefined") {

        displayBtn.attr("max", invNum);
        return invNum * 1;
    }
    return 0;
}


