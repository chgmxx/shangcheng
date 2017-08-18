/**
 * 鼠标选中事件
 */
$("input[datatype!='']").focus(function () {
    valiReg($(this));
});
/**
 * 鼠标失去焦点
 */
$("input[datatype!='']").blur(function () {
    valiReg($(this));
});
/**
 * 验证正则表达式
 *
 * @param obj
 */
function valiReg(obj) {
    var datatype = obj.attr("datatype");// 正则表达式
    var errormsg = obj.attr("errormsg");// 错误提示
    var valiName = obj.attr("name") + "Vali";
    datatype = new RegExp(datatype);
    if (!datatype.test($.trim(obj.val()))) {
        obj.css("border-color", "red");
        obj.parents(".part-box-2").find("." + valiName).css("color", "red");
        return false;
    } else if (obj.attr("id") == "gPeopleNum" || obj.attr("id") == "maxNum" || obj.attr("id") == "gPrice" || obj.attr("min") != null || obj.attr("max") != null) {
        var flag = false;
        var isMax = false;
        var isMin = false;
        if (obj.attr("id") == "maxNum") {
            if (obj.val() != null && obj.val() != "" || obj.attr("id") == "gPrice") {
                flag = true;
            }
        } else {
            flag = true;
        }
        if (obj.attr("min") != null && obj.attr("min") != "") {
            isMax = true;
            if (obj.val() * 1 < obj.attr("min") * 1 && obj.val() != null && obj.val() != "") {
                obj.css("border-color", "red");
                obj.parents(".part-box-2").find("." + valiName).css("color", "red");
                return false;
            }
        }
        if (obj.attr("max") != null && obj.attr("max") != "") {
            isMin = true;
            console.log(obj.attr("max"))
            if (obj.val() * 1 > obj.attr("max") * 1 && obj.val() != null && obj.val() != "") {
                obj.css("border-color", "red");
                obj.parents(".part-box-2").find("." + valiName).css("color", "red");
                return false;
            }
        }
        if (!isMin && !isMax) {
            if (flag && obj.val() * 1 <= 0) {
                obj.css("border-color", "red");
                obj.parents(".part-box-2").find("." + valiName).css("color", "red");
                return false;
            } else {
                obj.css("border-color", "#c5c5c5");
                obj.parents(".part-box-2").find("." + valiName).css("color", "#aaa");
                return true;
            }
        } else {
            return true;
        }
    } else {
        var flag = true;
        if (obj.attr("not-null") != null) {
            if (obj.val() == null || $.trim(obj.val()) == "") {
                obj.css("border-color", "red");
                obj.parents(".part-box-2").find("." + valiName).css("color", "red");
                flag = false;
            }
        }
        if (flag) {
            obj.css("border-color", "#c5c5c5");
            obj.parents(".part-box-2").find("." + valiName).css("color", "#aaa");
        }
        return flag;
    }
}
function getStrLen(message, MaxLenght) {
    var strlenght = 0; // 初始定义长度为0
    var txtval = $.trim(message);
    for (var i = 0; i < txtval.length; i++) {
        if (isCN(txtval.charAt(i)) == true) {
            strlenght = strlenght + 2; // 中文为2个字符
        } else {
            strlenght = strlenght + 1; // 英文一个字符
        }
    }
    return strlenght > MaxLenght ? false : true;
}
function isCN(str) { // 判断是不是中文
    var regexCh = /[u00-uff]/;
    return !regexCh.test(str);
}


/**
 * 编辑功能设置
 */
function editSellerSet() {
    var withdrawalType = $(".withdrawalType:checked").val();
    var withdrawalLowestMoney = $(".withdrawalLowestMoney").val();
    var withdrawalMultiple = $(".withdrawalMultiple").val();

    var flag = true;
    $("input[datatype!=null]").each(function () {
        var bol = true;
        if ($(this).attr("name") == "pfPrice") {
            bol = false;
        }
        if (bol && flag) {
            flag = valiReg($(this));
            if (!flag) {
                return;
            }
        }
    });
    var sellerSet = $(".set-part").serializeObject();
    sellerSet["withdrawalType"] = withdrawalType;
    if (flag) {
        if (withdrawalType == 1) {
            if (withdrawalLowestMoney == "" || withdrawalLowestMoney == 0) {
                $(".withdrawalLowestMoney").focus();
                $(".withdrawalLowestMoney").css("border-color", "red");
                $(".withdrawalLowestMoneyVali").css("color", "red");
                flag = false;
                layer.msg('最底可提现金额最多输入5位小数且大于0', {
                    icon: 1,
                    shade:[0.1,"#fff"],
                    offset: "10%"
                });
            }
        } else if (withdrawalType == 2) {
            if (withdrawalMultiple == "" || withdrawalMultiple == 0) {
                $(".withdrawalMultiple").focus();
                $(".withdrawalMultiple").css("border-color", "red");
                $(".withdrawalMultipleVali").css("color", "red");
                flag = false;
                layer.msg('提现规则最多输入5位小数且大于0', {
                    icon: 1,
                    shade:[0.1,"#fff"],
                    offset: "10%"
                });
            }
        }

    } else {
        layer.msg('请填写基本信息', {
            icon: 1,
            shade:[0.1,"#fff"],
            offset: "10%"
        });
    }
    if (flag) {

        // loading层
        var layerLoad = layer.load(1, {
            offset: "10%",
            shade: [0.1, '#fff']
            // 0.1透明度的白色背景
        });
        $.ajax({
            type: "post",
            url: "mallSellers/editSellerSet.do",
            data: {
                sellerSet: JSON.stringify(sellerSet)
            },
            dataType: "json",
            success: function (data) {
                layer.close(layerLoad);
                if (data.flag) {
                    var tip = layer.alert("编辑成功", {
                        offset: "10%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    }, function (index) {
                        layer.close(tip);
                        location.href = window.location.href;
                    });
                } else {// 编辑失败
                    layer.alert("编辑失败，请稍后重试", {
                        shade:[0.1,"#fff"],
                        offset: "10%"
                    });
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.close(layerLoad);
                layer.alert("编辑失败，请稍后重试", {
                    shade:[0.1,"#fff"],
                    offset: "10%"
                });
                return;
            }
        });
    }
}