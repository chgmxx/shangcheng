$(function () {
    $.fn.serializeObject = function () {
        var o = {};
        $(this).find("input").each(function (index) {
            if ($(this).attr("name") != undefined) {
                if ($(this).attr("type") == "text" || $(this).attr("type") == "password" || $(this).attr("type") == "hidden" ||
                    $(this).attr("type") == "tel") {
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

    $("#name").blur(function () {
        var isName = $("#isName").val();
        if (isName == 1) {
            var name = $("#name").val();
            if (name == "") {
                promMsg("姓名不能为空");
                return false;
            }
        }
    });

    $("#companyName").blur(function () {
        var isCompany = $("#isCompany").val();
        if (isCompany == 1) {
            var companyName = $("#companyName").val();
            if (companyName == "") {
                promMsg("公司名称不能为空");
                return false;
            }
        }
    });

    $("#remark").blur(function () {
        var isRemark = $("#isRemark").val();
        if (isRemark == 1) {
            var remark = $("#remark").val();
            if (remark == "") {
                promMsg("备注不能为空");
                return false;
            }
        }
    });

    $("#tel").blur(function () {
        var tel = $("#tel").val();
        if (tel == "") {
            promMsg("手机号码不能为空");
            $("#tel").val("");
            return false;
        } else if (tel.length < 11) {
            promMsg("手机号码长度为11位数字");
            $("#tel").val("");
            return false;
        } else if (!Mobilephone(tel)) {
            promMsg("手机号码格式错误");
            $("#tel").val("");
            return false;
        }
    });

    $("#code").blur(function () {
        checkCode();
    });

    $("#applyBut").click(function () {
        var name = $("#name").val();
        var companyName = $("#companyName").val();
        var tel = $("#tel").val();
        var code = $("#code").val();
        var remark = $("#remark").val();
        var isName = $("#isName").val();
        var isCompany = $("#isCompany").val();
        var isRemark = $("#isRemark").val();
        if (isName == 1) {
            if (name == "") {
                promMsg("请填写姓名");
                return false;
            }
        }
        if (isCompany == 1) {
            if (companyName == "") {
                promMsg("请填写公司名称");
                return false;
            }
        }
        if (isRemark == 1) {
            if (remark == "") {
                promMsg("请填写备注");
                return false;
            }
        }
        if (tel == '') {
            promMsg("请填写手机号码");
            return false;
        }
        if (code == '') {
            promMsg("请填写验证码");
            return false;
        }
        var result = checkCode();
        if (result == true) {
            var seller = $(".sWrapper").serializeObject();
            var obj = JSON.stringify(seller);

            var layerindex = layer.open({
                type: 2,
                content: '兑换中',
                shadeClose: false
            });

            $.ajax({
                url: "/phoneSellers/79B4DE7C/addSellers.do",
                type: "post",
                data: {obj: obj},
                dataType: "json",
                success: function (data) {
                    layer.closeAll();
                    if (data.result) {
                        parent.layer.alert("已申请，请等待审核", {
                            offset: "30%",
                            end: function () {
                                var userid = $("input.userid").val();
                                location.href = "/mMember/79B4DE7C/toUser.do?member_id=" + $("input.memberId").val() + "&uId=" + userid;
                            }
                        });
                    } else {
                        if (data.msg != null && data.msg != "" && typeof(data.msg) != "undefined") {
                            promMsg(data.msg);
                        } else {
                            promMsg("超级销售员申请失败");
                        }
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.closeAll();
                    promMsg("超级销售员申请失败");
                    return;
                }
            });
        }
    });

});

/**
 * 60秒后重新获取验证码
 */
var wait = 60;
function time(btn) {
    if (wait == 0) {
        $('#codeBtn').html("获取验证码");
        wait = 60;
    } else {
        btn.html(wait + "秒后重发");
        wait--;
        setTimeout(function () {
            time(btn);
        }, 1000)
    }
}

// 发送验证码
function sendMsg(type, mType, voiceOrMsg, self) {
    var tel = $("#tel").val();
    var phoneFlag = Mobilephone(tel);
    if (phoneFlag) {
        time($(self));
        $.post("/phoneSellers/79B4DE7C/sendMsg.do", {
            telNo: tel,
            sType: type,
            mType: mType
        }, function (data) {
            if (data.code == "1") {
                /*$("#obtainCode").html("发送成功");*/
                return true;
            } else {
                $("#obtainCode").html("发送失败，重新发送");
                return false;
            }
        }, "json");
    } else {
        promMsg("请填写手机号码。")
        return false;
    }

}

function checkCode() {
    var code = $("#code").val();
    if (code == "") {
        promMsg("验证码不能为空。");
        return false;
    } else if (!/^[0-9]*$/.test(code)) {
        promMsg("验证码只能为数字。");
        $("#code").val("");
        return false;
    } else if (code.length != 6) {
        promMsg("验证码的长度为6位数。");
        $("#code").val("");
        return false;
    } else {
        var codeFlag = false;
        $.ajaxSetup({
            async: false
        });
        $.ajax({
            url: "/phoneSellers/79B4DE7C/checkCode.do",
            type: "post",
            data: {applyCode: code},
            dataType: "json",
            success: function (data) {
                if (data.result == 1) {
                    codeFlag = true;
                } else {
                    promMsg(data.message);
                }
            }
        });
        return codeFlag;
    }
}

function promMsg(content) {
    parent.layer.alert(content, {
        offset: "30%"
    });
}
