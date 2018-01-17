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

    $("#wsUName").blur(function () {
        var name = $("#wsUName").val();
        if (name == "") {
            promMsg("姓名不能为空。");
            return false;
        }
    });

    $("#wsCName").blur(function () {
        var companyName = $("#wsCName").val();
        if (companyName == "") {
            promMsg("公司名称不能为空。");
            return false;
        }
    });

    $("#wsTel").blur(function () {
        var tel = $("#wsTel").val();
        if (tel == "") {
            promMsg("手机号码不能为空。");
            return false;
        } else if (!Mobilephone(tel)) {
            promMsg("手机号码不存在。");
            return false;
        }
    });

    $("#wsCode").blur(function () {
        checkCode();
    });

    /*$("#wsRemark").blur(function(){
     var remark = $("#wsRemark").val();
     if(remark == ""){
     promMsg("备注不能为空。");
     return false;
     }
     });*/

    $("#btnSub").click(function () {
        var name = $("#wsUName").val();
        var companyName = $("#wsCName").val();
        var tel = $("#wsTel").val();
        var code = $("#wsCode").val();
        var result = checkCode();
        if (name != '' && companyName != '' && tel != '' && code != '') {
            if (result == true) {
                var wholesalers = $(".wWrapper").serializeObject();
                var obj = JSON.stringify(wholesalers);
                $.ajax({
                    url: "/phoneWholesaler/79B4DE7C/addWholesaler.do",
                    type: "post",
                    data: {obj: obj},
                    dataType: "json",
                    success: function (data) {
                        if (data.result) {
                            showLayer();
                        } else {
                            if (data.msg != null && data.msg != "" && typeof(data.msg) != "undefined") {
                                alert(data.msg);
                            } else {
                                alert("批发申请失败");
                            }
                        }
                    }
                });
            }
        } else {
            promMsg("请把信息填写完整。");
        }
    });

});

/**
 * 60秒后重新获取验证码
 */
var wait = 60;
function time(btn) {
    if (wait == 0) {
        $('#obtainCode').html("获取验证码");
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
    var tel = $("#wsTel").val();
    var phoneFlag = Mobilephone(tel);
    if (phoneFlag) {
        time($(self));
        $.post("/phoneWholesaler/79B4DE7C/sendMsg.do", {
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
    var code = $("#wsCode").val();
    if (code == "") {
        promMsg("验证码不能为空。");
        return false;
    } else if (!/^[0-9]*$/.test(code)) {
        promMsg("验证码只能为数字。");
        return false;
    } else if (code.length != 6) {
        promMsg("验证码的长度为6位数。");
        return false;
    } else {
        var codeFlag = false;
        $.ajaxSetup({
            async: false
        });
        $.ajax({
            url: "/phoneWholesaler/79B4DE7C/checkCode.do",
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

function closeLayer(obj) {
    $("body,html").attr("style", "");
    $(obj).parents("section#cd-apply").hide();
}
function showLayer() {
    $("body,html").attr("style", "overflow:hidden");
    $("section#cd-apply").show();
}

function promMsg(content) {
    parent.layer.alert(content, {
        offset: "30%"
    });
}
function okayBtn() {
    var userid = $("input.userid").val();
    location.href = "/mMember/79B4DE7C/toUser.do?member_id=" + $("input.memberId").val() + "&uId=" + userid;
}