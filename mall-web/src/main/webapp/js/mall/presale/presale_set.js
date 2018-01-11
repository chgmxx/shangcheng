$(document).ready(function () {
    /**
     * 鼠标选中事件
     */
    $("input.vali").focus(function () {
        valiReg($(this));
    });
    /**
     * 鼠标失去焦点
     */
    $("input.vali").blur(function () {
        valiReg($(this));
    });

    var len = $("#J_Tbody tr").length;
    if (len == 0) {
        addTr(null);
        var _obj = $("#J_Tbody tr:last");
        _obj.find(".add").show();
        _obj.find(".del").hide();
    }
});

/**
 * 验证表单
 */
function valiForm() {
    var flag = true;
    $(".groupDiv input.vali").each(function () {
        var bol = true;
        /*if($(this).attr("name") == "sPrice" && isSpec == 1){
         bol = false;
         }*/
        if (bol) {
            resultFlag = valiReg($(this));

            if (!resultFlag) {
                flag = resultFlag;
                return;
            }
        }
    });
    return flag;
}
var presaleSet = new Array();
var delPresaleSet = new Array();
/**
 * 编辑预售
 */
function editPresale() {
    var ids = $("#ids").val();
    var flag = valiForm();

    if (flag) {

        flag = validateGive();

        var datas = {
            presaleSet: JSON.stringify(presaleSet)
        };
        if (delPresaleSet != null && delPresaleSet.length > 0) {
            datas["delPresaleSet"] = JSON.stringify(delPresaleSet);
        }
    }

    if (!flag) {
        layer.msg("请完善订购送礼信息", {
            icon: 1,
            offset: "10%",
            shade: [0.1, '#fff']
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
            url: "mPresale/edit_presale_set.do",
            data: datas,
            dataType: "json",
            success: function (data) {
                layer.close(layerLoad);
                if (data.code == 1) {
                    var tip = layer.alert("编辑成功", {
                        offset: "10%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    }, function (index) {
                        layer.close(tip);
                        location.href = "/mPresale/presale_set.do";
                    });
                } else if (data.code == -2) {
                    var tip = layer.alert("正在进行预售的活动不能修改", {
                        offset: "10%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                } else if (data.code == -3) {
                    var tip = layer.alert("已失效的预售不能进行修改", {
                        offset: "10%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                } else if (data.code == 0) {
                    var tip = layer.alert("同一个商品只能参与一个预售活动", {
                        offset: "10%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                } else {// 编辑失败
                    layer.alert("编辑失败", {
                        shade:[0.1,"#fff"],
                        offset: "10%"
                    });
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.close(layerLoad);
                layer.alert("编辑失败", {
                    shade:[0.1,"#fff"],
                    offset: "10%"
                });
                return;
            }
        });
    }
}
/**
 * 验证价格调整
 * @returns
 */
function validateGive() {

    delPresaleSet = new Array();
    var defaultObj = new Object();
    if (setDefaultObj != null) {
        defaultObj = setDefaultObj;
    }


    presaleSet = new Array();
    var flag = true;
    var msg = "";
    $("#J_Tbody tr").each(function (i) {
        var types = $(this).find(".types option:selected").val();
        var id = $(this).find(".tId").val();

        var name = $(this).find(".name").val();
        var tNum = $(this).find(".tNum").val();
        var rank = $(this).find(".rank").val();

        var _obj = $(this);

        var obj = {
            giveName: name,
            giveNum: tNum,
            giveRanking: rank,
            giveType: types,
        };
        if (id == null || $.trim(id) == "") {
            presaleSet[presaleSet.length] = obj;
        } else {

            delete defaultObj[id];

            obj.id = id;
            delPresaleSet[delPresaleSet.length] = obj;
        }
    });
    if (defaultObj != null) {
        for (var str in defaultObj) {
            var obj = new Object();
            obj.id = str;
            obj.isDelete = 1;
            delPresaleSet[delPresaleSet.length] = obj;
        }
    }
    return flag;
}

/**
 * 验证正则表达式
 *
 * @param obj
 */
function valiReg(obj) {
    var datatype = obj.attr("datatype");// 正则表达式
    var errormsg = obj.attr("errormsg");// 错误提示
    datatype = new RegExp(datatype);
    if (obj.hasClass("vali")) {
        if (!datatype.test($.trim(obj.val()))) {
            obj.css("border-color", "red");
            obj.parent().find("span.red").css("color", "red");
            return false;
        } else if (obj.attr("id") == "gPeopleNum" || obj.attr("id") == "maxNum" || obj.hasClass("gPrice")) {
            var flag = false;
            if (obj.attr("id") == "maxNum") {
                if (obj.val() != null && obj.val() != "" || obj.hasClass("gPrice")) {
                    flag = true;
                }
            } else {
                flag = true;
            }
            if (flag && obj.val() * 1 <= 0) {
                obj.css("border-color", "red");
                obj.parent().find("span.red").css("color", "red");
                return false;
            } else {
                obj.css("border-color", "#c5c5c5");
                obj.parent().find("span.red").css("color", "#000000");
                return true;
            }
        } else {
            var flag = true;
            if (obj.attr("notNull") != null) {
                if (obj.val() == null || $.trim(obj.val()) == "") {
                    obj.css("border-color", "red");
                    obj.parent().find("span.red").css("color", "red");
                    flag = false;
                }
            }
            if (flag) {
                obj.css("border-color", "#c5c5c5");
                obj.parent().find("span.red").css("color", "#000000");
            }
            return flag;
        }
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

function closewindow() {
    layer.closeAll();
}

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
/**
 * 删除价格
 * @param obj
 * @returns
 */
function delTr(obj) {
    $(obj).parents("tr").remove();
    loadWindow();
}
/**
 * 新增价格区间
 * @param obj
 * @returns
 */
function addTr(_obj) {
    var obj = $(".trObj_div table tbody").clone(true);

    $(".table tbody").append(obj.html());

    /**
     * 鼠标选中事件
     */
    $(".table tbody tr:last input.vali").focus(function () {
        valiReg($(this));
    });
    /**
     * 鼠标失去焦点
     */
    $(".table tbody tr:last input.vali").blur(function () {
        valiReg($(this));
    });

    loadWindow();
}
function typeChange(_obj) {
    var val = $(_obj).find("option:selected").text();
    $(_obj).parents("tr").find(".name").val(val);
}