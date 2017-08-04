/**
 * 删除
 * @param obj
 */
function del(ids) {
    //event.stopPropagation();
    parent.layer.confirm('确认要删除此数据吗?', {icon: 3, title: '提示', offset: "40%"}, function (index) {
        parent.layer.close(index);
        var params = {
            id: ids
        };
        var index2 = parent.layer.load(3, {
            offset: '40%',
            shade: [0.4, '#8E8E8E']
        });

        $.ajax({
            type: "post",
            data: params,
            url: "mFreight/deleteTake.do",
            dataType: "json",
            success: function (data) {
                parent.layer.close(index2);
                if (!data.flag) {// 重新登录
                    parent.layer.alert("编辑上门自提失败", {
                        offset: "30%"
                    });
                } else {
                    location.href = window.location.href;
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                parent.layer.closeAll();
                parent.layer.alert("编辑上门自提失败", {
                    offset: "30%"
                });
                return;
            }
        });

    });
}


$(".ui-switcher").click(function () {
    var id = $(".id").val();
    var isTakeTheir = 0;
    var isDeliveryPay = 0;
    var isDaifu = 0;
    var isComment = 0;
    var isCommentCheck = 0;
    var isCommentGive = 0;
    var isCommentDefault = 0;
    var isMessage = 0;
    var isPresale = 0;
    var isPresaleGive = 0;
    var isPresaleDefault = 0;
    var isSmsMessage = 0;
    var isPf = 0;
    var isPfCheck = 0;
    var isSeller = 0;
    var isCheckSeller = 0;
    if ($("input.isComment").length > 0) {
        isCommentDefault = $("input.isComment").val();
    }
    if ($("input.isPresale").length > 0) {
        isPresaleDefault = $("input.isPresale").val();
    }
    if ($(this).hasClass("ui-switcher-off")) {
        isTakeTheir = 1;
        isDeliveryPay = 1;
        isDaifu = 1;
        isComment = 1;
        isCommentCheck = 1;
        isCommentGive = 1;
        isMessage = 1;
        isPresale = 1;
        isPresaleGive = 1;
        isSmsMessage = 1;
        isPf = 1;
        isPfCheck = 1;
        isSeller = 1;
        isCheckSeller = 1;
    }
    var data = {};
    if (id != null && id != "") {
        data.id = id;
    }
    var flag = true;
    if ($(this).attr("id") == 1) {
        data.isDeliveryPay = isDeliveryPay;
        deletePro(data, "开启货到付款", null);
    } else if ($(this).attr("id") == 2) {
        data.isDaifu = isDaifu;
        deletePro(data, "开启找人代付", null);
    } else if ($(this).attr("id") == 3) {
        data.isComment = isComment;
        deletePro(data, "开启评论管理", null);
    } else if ($(this).attr("id") == 4) {
        data.isCommentCheck = isCommentCheck;
        if (isCommentDefault == 1) {
            deletePro(data, "开启评论审核", null);
        } else {
            flag = false;
            parent.layer.alert("请先开启评论管理", {
                offset: "30%"
            });
        }
    } else if ($(this).attr("id") == 5) {
        data.isCommentGive = isCommentGive;
        if (isCommentDefault == 1) {
            deletePro(data, "开启评论送礼", null);
        } else {
            flag = false;
            parent.layer.alert("请先开启评论管理", {
                offset: "30%"
            });
        }
    } else if ($(this).attr("id") == 6) {
        data.isMessage = isMessage;
        deletePro(data, "开启消息提醒", null);
    } else if ($(this).attr("id") == 7) {
        data.isPresale = isPresale;
        deletePro(data, "开启商品预售", null);
    } else if ($(this).attr("id") == 8) {
        data.isPresaleGive = isPresaleGive;
        /*if(isPresaleDefault == 1){*/
        deletePro(data, "开启预定送礼", null);
        /*}else{
         flag = false;
         parent.layer.alert("请先开启商品预售", {
         offset : "30%"
         });
         }*/
    } else if ($(this).attr("id") == 9) {
        data.isSmsMember = isSmsMessage;
        deletePro(data, "消息短信提醒粉丝", null);
    } else if ($(this).attr("id") == 10) {
        data.isPf = isPf;
        /*if(isPresaleDefault == 1){*/
        deletePro(data, "开启商品批发", null);
        /*}else{
         flag = false;
         parent.layer.alert("请先开启商品预售", {
         offset : "30%"
         });
         }*/
    } else if ($(this).attr("id") == 11) {
        data.isPfCheck = isPfCheck;
        deletePro(data, "开启批发审核", null);
    } else if ($(this).attr("id") == 12) {
        data.isSeller = isSeller;
        /*if(isPresaleDefault == 1){*/
        deletePro(data, "开启超级销售员", null);
        /*}else{
         flag = false;
         parent.layer.alert("请先开启商品预售", {
         offset : "30%"
         });
         }*/
    } else if ($(this).attr("id") == 13) {
        data.isCheckSeller = isCheckSeller;
        deletePro(data, "开启超级销售员审核", null);
    } else {
        data.isTakeTheir = isTakeTheir;
        deletePro(data, "开启上门自提", null);
    }
    if (flag) {
        if ($(this).hasClass("ui-switcher-off")) {
            $(this).removeClass("ui-switcher-off");
            $(this).addClass("ui-switcher-on");
        } else {
            $(this).addClass("ui-switcher-off");
            $(this).removeClass("ui-switcher-on");
        }
    }
});

function deletePro(datas, tip, url) {
    if (url == null) {
        url = "store/edit_set.do";
    }
    var layerLoad = parent.layer.load(1, {
        shade: [0.3, '#000']
    });
    $.ajax({
        type: "post",
        data: datas,
        url: url,
        dataType: "json",
        success: function (data) {
            layer.close(layerLoad);
            parent.layer.closeAll();
            if (!data.flag) {// 重新登录
                parent.layer.alert(tip + "失败", {
                    offset: "30%"
                });
            } else {
                location.href = window.location.href;
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            parent.layer.closeAll();
            parent.layer.alert(tip + "失败", {
                offset: "30%"
            });
            return;
        }
    });
}
var numTest = /^[0-9]{1,6}$/;
$(".saveBtn").click(function () {
    var data = new Array();
    $("ul.ul_div li.ul_li").each(function () {
        var _this = $(this);
        var check = _this.find("input.status").is(":checked");
        var type = _this.find(".type option:selected").val();
        var text = _this.find(".type option:selected").text();
        var num = _this.find("input.num").val();
        var id = _this.find("input.id").val();
        if (check) {
            if (num == null || num == "") {
                data = [];
                alert("请重新输入大于0的六位数字")
                return false;
            } else if (!numTest.test(num)) {
                data = [];
                alert("请重新输入大于0的六位数字")
                return false;
            } else if (num * 1 == 0) {
                data = [];
                alert("请重新输入大于0的六位数字")
                return false;
            }
            check = 1;
        } else {
            num = 0;
            check = 0;
            type = 1;
        }
        var obj = {
            giveType: type,
            giveStatus: _this.find("input.status").val(),
            num: num,
            isEnable: check
        };
        if (id != null && id != "" && typeof(id) != "undefinde") {
            obj["id"] = id;
        }

        data[data.length] = obj;
    });
    if (data.length > 0) {
        data = JSON.stringify(data)
        deletePro({datas: data}, "评论送礼", "comment/batchCommentGive.do")
    }
});

/**
 * 消息短信提醒粉丝
 */
function saveSmsMessage() {
    var json = new Object();
    var bol = true;
    var id = $(".id").val();
    $(".smsMessageDiv input.saves").each(function () {
        var val = $(this).val();
        if (val == null || val == "" || typeof(val) == "undefined") {
            alert($(this).attr("vali") + "不能为空");
            bol = false;
            return;
        }
        json[$(this).attr("ids")] = val;
    });
    if (bol) {
        var data = {
            "smsMessage": JSON.stringify(json)
        };
        if (id != null && id != "") {
            data.id = id;
        }
        deletePro(data, "保存消息短信提醒", null);
    }
}
function saveFooter() {
    var json = new Object();
    var id = $(".id").val();
    $("input.footerInp").each(function () {
        var val = 0;
        if ($(this).is(":checked")) {
            val = 1;
        }
        json[$(this).attr("id")] = val;
    });
    var data = {
        "footerJson": JSON.stringify(json)
    };
    if (id != null && id != "") {
        data.id = id;
    }
    deletePro(data, "保存手机底部菜单", null);
}

/**
 * 消息模板的保存
 */
function saveMsg() {
    var msgObj = null;
    var isMessage = $("input.isMessage").val();
    var id = $(".id").val();
    if ($("input.msgId:checked").length > 0) {
        msgObj = new Array();
        $("input.msgId:checked").each(function () {
            var title = $(this).attr("title");
            var id = $(this).val();
            if (title != null && title != "") {
                msgObj[msgObj.length] = {
                    "title": title,
                    "id": id
                };
            }
        });
    }
    console.log(msgObj);
    if (msgObj != null && isMessage == 1) {
        var data = {
            "messageJson": JSON.stringify(msgObj)
        };
        if (id != null && id != "") {
            data.id = id;
        }

        deletePro(data, "保存消息模板", null);

    } else if (isMessage != 1) {
        alert("请开启消息模板的功能")
    } else {
        alert("请选择消息模板")
    }
}