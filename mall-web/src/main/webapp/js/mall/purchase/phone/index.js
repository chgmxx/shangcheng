/**
 *计算字体大小
 */

$(function () {
    var deviceWidth = document.documentElement.clientWidth;
    if (deviceWidth > 750) deviceWidth = 750;
    document.documentElement.style.fontSize = deviceWidth / 7.5 + 'px';
});

var validcode = true;
var error = $('#error');

//获取短信验证码
function msg(obj) {
    var time = 120;
    var code = $(obj);
    if (ok1()) {
        if (validcode) {
            validcode = false;
            code.attr('disabled', 'disabled');
            $.ajax({
                url: '/purchasePhone/79B4DE7C/sendMsg.do',
                type: "GET",
                data: {
                    "mobile": $("#tel").val(),
                    "busid": $("#busId").val()
                },
                dataType: "JSON",
                success: function (data) {
                    if (data == "true" || data == true) {
                        alert("验证码发送成功请查收!");
                    } else {
                        alert("验证码发送失败,请重试!");
                    }
                }
            });
            var t;
            t = setInterval(function () {
                time--;
                code.html(time + '秒');
                if (time == 0) {
                    clearTimeout(t);
                    code.html("重新获取");
                    validcode = true;
                    code.attr('disabled', false);
                }
                ;
            }, 1000);
        }
    }

}

//手机验证
function ok1() {
    var value = $("#tel").val();
    var pattern = /^1[34578]\d{9}$/;
    if (value == '') {
        error.html("手机号不能为空！");
        error.fadeIn().delay(1000).hide(0);
        return false;
    }
    else if (!pattern.test(value)) {
        error.html("手机号不符合规则！");
        error.fadeIn().delay(1000).hide(0);
        return false;
    }
    else {
        return true;
    }
}



