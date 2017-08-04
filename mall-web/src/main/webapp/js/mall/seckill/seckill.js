﻿
/**
 * 删除秒杀
 */
function deleteGroup(obj, type) {
    var id = $(obj).attr("id");
    if (id != null && id != "") {
        var msg = "删除秒杀";
        if (type * 1 == -2) {
            msg = "使失效";
        }
        // 询问框
        parent.layer.confirm('您确定要' + msg + '？', {
            offset: "30%",
            btn: ['确定', '取消']
            // 按钮
        }, function () {
            // loading层
            var layerLoad = layer.load(1, {
                shade: [0.1, '#fff']
                // 0.1透明度的白色背景
            });
            $.ajax({
                type: "post",
                url: "mSeckill/group_remove.do",
                data: {
                    id: id,
                    type: type
                },
                dataType: "json",
                success: function (data) {
                    parent.layer.close(layerLoad);
                    if (data.code == 1) {
                        var tip = parent.layer.alert(msg + "成功", {
                            offset: "30%",
                            closeBtn: 0
                        }, function (index) {
                            parent.layer.close(tip);
                            location.href = window.location.href;
                        });
                    } else {// 编辑失败
                        var tip = parent.layer.alert(msg + "失败", {
                            offset: "30%"
                        });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    parent.layer.close(layerLoad);
                    parent.layer.alert(msg + "失败", {
                        offset: "30%"
                    });
                    return;
                }
            });
            parent.layer.closeAll();
        });
    }

}
if (typeof $("#sStartTime").val() != "undefined" && typeof $("#sEndTime").val() != "undefined")
    loadLayDate();
function loadLayDate() {
    //初始化预计发货开始时间
    var datebox_1 = {
        elem: '#sStartTime',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        festival: true,
        istime: true,
        //min : laydate.now(),
        choose: function (datas) {
            datebox_2.min = datas; // 开始日选好后，重置结束日的最小日期
            datebox_2.start = datas; // 将结束日的初始值设定为开始日
            $('#sStartTime').parent().find("span.red").html("");
            loadWindow();
        }
    }
    // 初始化预计发结束时间
    var datebox_2 = {
        elem: '#sEndTime',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        festival: true,
        istime: true,
        //min : laydate.now(),
        choose: function (datas) {
            //datebox_1.max = datas;
            $('#sEndTime').parent().find("span.red").html("");
            loadWindow();
        }
    }

    laydate(datebox_1);
    laydate(datebox_2);
    var startTime = $("#sStartTime").val();
    if (startTime != null && startTime != "") {
        datebox_2.min = startTime;
        datebox_2.start = startTime;
    }
    getProductId($("#productId").val());

    loadWindow();
}
function getProductId(proId) {
    var isSpec = $("#isSpec").val();
    $.ajax({
        type: "post",
        url: "mGroupBuy/getSpecificaByProId.do",
        data: {
            proId: proId,
            type: 3,
            isSpec: isSpec,
            seckillId: $("#ids").val()
        },
        dataType: "json",
        success: function (data) {
            var isSpec = $("#isSpec").val();
            if (data != null && data.list != null && isSpec == 1) {
                proSpecArr = data.list;
            }
            if (data != null && data.product != null && isSpec == 0) {
                if (data.product != null) {
                    $("#sNums").val(data.product.proStockTotal);
                }
            }
            eval(step.Creat_Table());// 初始化创建库存表格
        }

    });
}
/**
 * 编辑秒杀
 */
function editGroup() {
    var name = $("#sName").val();//活动名称
    var shopId = $(".shopId").find("option:selected").val();//店铺id
    var gStartTime = $("#sStartTime").val();//活动生效开始时间
    var gEndTime = $("#sEndTime").val();//活动生效结束时间
    var maxNum = $("#maxNum").val();//商品限购
    var isSpec = $("#isSpec").val();//商品是否存在规格
    var productId = $("#productId").val();
    var ids = $("#ids").val();
    if (productId == null || productId == "") {
        layer.msg('请选择商品', {
            offset: "30%",
            icon: 1
        });
    } else if (name == null || $.trim(name) == "") {
        $("gName").focus();
        layer.msg('请填写活动名称', {
            offset: "30%",
            icon: 1
        });
    } else if (!valName($("#sName"))) {
        $("#sName").focus();
        layer.msg('活动名称最多输入50位汉字或100位字符', {
            offset: "30%",
            icon: 1
        });
    } else if (gStartTime == null || $.trim(gStartTime) == "") {
        layer.msg('请选择活动开始时间', {
            offset: "30%",
            icon: 1
        });
    } else if (gEndTime == null || $.trim(gEndTime) == "") {
        layer.msg('请选择活动结束时间', {
            offset: "30%",
            icon: 1
        });
    } else if (gStartTime >= gEndTime) {
        layer.msg('活动开始时间要小于活动结束时间', {
            offset: "30%",
            icon: 1
        });
    } else {
        var flag = true;
        $("input[datatype!=null]").each(function () {
            var bol = true;
            if ($(this).attr("name") == "sPrice" && isSpec == 1) {
                bol = false;
            }
            if (bol && flag) {
                flag = valiReg($(this));
                if (!flag) {
                    return;
                }
            }
        });
        var seckill = $("#groupForm").serializeObject();
        var checkLen = 0;
        var specArr = new Array();
        var proInvNum = 0;
        if (flag) {
            if (isSpec == 1) {//存在规格
                //判断参团规格的价格
                $("#createTable tbody tr").each(function (i) {
                    var check = $(this).find(".js-default").is(":checked");
                    var invenId = $(this).find(".js-default").attr("invenid");
                    var specId = "";
                    var id = $(this).find(".js-default").attr("id");
                    var seckillPrice = $(this).find(".js-price").val();
                    var invNum = $(this).find(".invNum").text();
                    proInvNum += $.trim(invNum) * 1;
                    $(this).find("td.specCla").each(function () {
                        if (specId != "") {
                            specId += ",";
                        }
                        specId += $(this).attr("id");
                    });
                    var obj = {
                        seckillPrice: seckillPrice,
                        invenId: invenId,
                        specificaIds: specId,
                        isJoinGroup: 1,
                        seckillNum: $.trim(invNum)
                    };
                    if (id != null && id != "") {
                        if (id * 1 > 0) {
                            obj.id = id;
                        }
                    }
                    if (check) {
                        flag = valiTable($(this).find(".js-price"));
                        if (i == 0) {
                            seckill.sPrice = seckillPrice;
                        }
                        if (!flag) {
                            return;
                        }
                        obj["isJoinGroup"] = 1;
                        checkLen++;
                    } else {
                        obj["isJoinGroup"] = 0;
                    }
                    specArr[specArr.length] = obj;
                });
            }
        }

        if (!flag) {
            layer.msg('请填写已经勾选的秒杀价', {
                offset: "30%",
                icon: 1
            });
        } else if (isSpec == 1 && checkLen == 0) {
            layer.msg('请勾选的参加秒杀的规格', {
                offset: "30%",
                icon: 1
            });
        } else {
            var isSpec = $("#isSpec").val();
            if (proInvNum == 0 && isSpec == 0) {
                proInvNum = $("#sNums").val();
            }
            seckill.sNum = proInvNum;
            // loading层
            var layerLoad = parent.layer.load(1, {
                offset: "30%",
                shade: [0.1, '#fff']
                // 0.1透明度的白色背景
            });
            $.ajax({
                type: "post",
                url: "mSeckill/edit_seckill.do",
                data: {
                    seckill: JSON.stringify(seckill),
                    specArr: JSON.stringify(specArr)
                },
                dataType: "json",
                success: function (data) {
                    parent.layer.close(layerLoad);
                    if (data.code == 1) {
                        var tip = parent.layer.alert("编辑成功", {
                            offset: "30%",
                            closeBtn: 0
                        }, function (index) {
                            parent.layer.close(tip);
                            location.href = "/mSeckill/index.do";
                        });
                    } else if (data.code == -2) {
                        var tip = parent.layer.alert("正在进行秒杀的商品不能修改", {
                            offset: "30%",
                            closeBtn: 0
                        });
                    } else if (data.code == 0) {
                        var tip = parent.layer.alert("同一个商品只能参与一个秒杀活动", {
                            offset: "30%",
                            closeBtn: 0
                        });
                    } else {// 编辑失败
                        parent.layer.alert("编辑失败", {
                            offset: "30%"
                        });
                    }

                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    parent.layer.close(layerLoad);
                    parent.layer.alert("编辑失败", {
                        offset: "30%"
                    });
                    return;
                }
            });
        }
    }
}
$("#gName").focus(function () {
    valName($(this));
});
$("#gName").blur(function () {
    valName($(this));
});
$("#gName").keyup(function () {
    valName($(this));
});

function valName(obj) {
    var val = obj.val();
    var pattern = /^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{1,20}$/g;
    var parent = obj.parent();
    if (val != null && val != "") {
        if (!getStrLen(val, 100)) {
            //val = val.replace(pattern, "");
            parent.find("span.red").css("color", "red");
            return false;
        } else {
            parent.find("span.red").css("color", "#000");
            return true;
        }
    } else {
        parent.find("span.red").css("color", "red");
        return false;
    }

}
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
        obj.next().css("color", "red");
        return false;
    } else if (obj.attr("id") == "gPeopleNum" || obj.attr("id") == "maxNum" || obj.attr("id") == "gPrice") {
        var flag = false;
        if (obj.attr("id") == "maxNum") {
            if (obj.val() != null && obj.val() != "" || obj.attr("id") == "gPrice") {
                flag = true;
            }
        } else {
            flag = true;
        }
        if (flag && obj.val() * 1 <= 0) {
            obj.css("border-color", "red");
            obj.next().css("color", "red");
            return false;
        } else {
            obj.css("border-color", "#c5c5c5");
            obj.next().css("color", "#000");
            return true;
        }
    } else {
        var flag = true;
        if (obj.attr("not-null") != null) {
            if (obj.val() == null || $.trim(obj.val()) == "") {
                obj.css("border-color", "red");
                obj.next().css("color", "red");
                flag = false;
            }
        }
        if (flag) {
            obj.css("border-color", "#c5c5c5");
            obj.next().css("color", "#000");
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

function closewindow() {
    layer.closeAll();
}


/**
 *选择商品
 */
function choosePro() {
    var shopId = $(".shopId").find("option:selected").val();//店铺id
    var defaultProId = $("#defaultProId").val();
    if ((typeof defaultProId) == "undefined") {
        defaultProId = "";
    }
    loadWindow();
    if (shopId != null && shopId != "") {
        parent.openIframe("选择商品", "600px", "480px", "/mGroupBuy/getProductByGroup.do?shopId=" + shopId + "&defaultProId=" + defaultProId);//check==0代表多选，check==1代表单选
    } else {
        parent.alertMsg("请选择商品");
    }
};
/**
 * 选择商品回调函数
 * @param obj
 */
function getProductGroup(obj, arr) {
    if (obj != null) {
        $(".goodDiv a").css({
            "background-image": "url(" + obj.src + ")",
            "background-position": "center center",
            "background-size": "cover"
        }).html("");
        $("input[name='productId']").val(obj.id);
        $("#isSpec").val(obj.isSpe);
        $("span.proName").html(obj.title);
        $(".proPrice").html("￥" + obj.price);
        $("#sNums").val(obj.stockTotal);

        if (arr != null && obj.isSpe == 1) {
            proSpecArr = arr;
//			console.log(proSpecArr)

            eval(step.Creat_Table());// 初始化创建库存表格

            $("div.proPrices").hide();
        } else {
            $("#createTable").hide();
            $("div.proPrices").show();
        }

    }
    loadWindow();
}
/**
 * 验证表格
 *
 * @param obj
 */
function valiTable(obj) {
    var priceTest = /^[0-9]{1,6}(\.\d{1,2})?$/;
    var numTest = /^\d{1,6}$/;
    if (obj.hasClass("js-price")) {
        if (!priceTest.test($.trim(obj.val()))) {
            obj.next().html("价格最多只能是6位小数或整数");
            obj.next().show();
            obj.next().css("color", "red");
            return false;
        } else if (obj.val() * 1 <= 0) {
            obj.next().html("价格最多只能是6位小数或整数");
            obj.next().show();
            obj.next().css("color", "red");
            return false;
        } else {
            obj.next().css("color", "#000");
            obj.next().hide();
        }
    }
    return true;
}

/**
 * 批量修改价格
 */
$(".js-batch-price").click(function () {
    $(".js-batch-form").show();// 显示批量修改框
    $(".js-batch-txt").attr("vali", "^[0-9]{1}\\d{0,5}(\\.\\d{1,2})?$");
    $(".js-batch-txt").val("");
});
/**
 * 关闭批量修改价格或库存
 */
$(".js-batch-cancel").click(function () {
    $(".js-batch-form").hide();// 隐藏修改框
    $(".js-batch-price").show();// 显示批量修改价格按钮
});
/**
 * 批量修改价格或库存
 */
$(".js-batch-save").click(function () {
    var inp = $(".js-batch-txt");
    var tests = inp.attr("vali")
    // console.log(tests)
    tests = new RegExp(tests);
    // console.log(tests)
    // console.log(inp.val() + "=======" + tests.test(inp.val()));
    if (!tests.test(inp.val())) {
        var msg = $(".js-batch-price").attr("errormsg");
        layer.tips(msg, $(".js-batch-save"), {
            tips: [3, '#3595CC']
        });
    } else {
        if (inp.val() * 1 <= 0) {
            msg = $(".js-batch-price").attr("errormsg");
            layer.tips(msg, $(".js-batch-save"), {
                tips: [3, '#3595CC']
            });
        } else {
            $(".js-price").val(inp.val());
            $(".js-price").next().hide();
            $(".js-batch-form").hide();// 隐藏修改框
            $(".js-batch-price").show();// 显示批量修改价格按钮
        }
        //eval(step.saveInp());
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
$(".shopId").change(function () {
    $("a.add-goods").css("background-image", "");
    $("span.proName").html("");
    $("span.proPrice").html("");
    $(".table").hide();
    $(".sPrice").show();
    $(".sPrice").val("");
});
function findGroup(obj) {
    var html = "/mSeckill/index.do";
    var type = $(obj).find("option:selected").val();
    if (type != null && type != "") {
        html += "?type=" + type;
    }
    location.href = html;
}