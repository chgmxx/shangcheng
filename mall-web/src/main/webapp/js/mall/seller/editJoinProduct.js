/**
 * 删除商品销售佣金
 */
function deleteJoinProduct(obj, type) {
    var id = $(obj).attr("id");
    if (id != null && id != "") {
        var data = {
            "id": id
        };
        var msg = "删除";
        if (type == -1) {
            msg = "失效";
            data["isUse"] = 0;
        } else if (type == -2) {
            msg = "启用";
            data["isUse"] = 1;
        } else {
            data["isDelete"] = 1;
        }
        // 询问框
        //TODO parent.layer.confirm
        parent.layer.confirm('您确定要' + msg + '商品销售佣金?', {
            offset: "30%",
            shade:[0.1,'#fff'],
            btn: ['确定', '取消']
            // 按钮
        }, function () {
            // loading层
            var layerLoad = parentLayerLoad();
            // var layerLoad = layer.load(1, {
            //     offset: "30%",
            //     shade: [0.1, '#fff']
            //     // 0.1透明度的白色背景
            // });
            $.ajax({
                type: "post",
                url: "/mallSellers/upJoinProduct.do",
                data: {
                    joinProduct: JSON.stringify(data)
                },
                dataType: "json",
                success: function (data) {
                    parentCloseAll();
                    // parent.layer.close(layerLoad);
                    if (data.flag) {
                        parentAlertMsg(msg + "成功");
                        //TODO alert 跳转
                        // var tip = parent.layer.alert(msg + "成功", {
                        //     offset: "30%",
                        //     closeBtn: 0
                        // }, function (index) {
                        //     parent.layer.close(tip);
                        //     location.href = window.location.href;
                        // });
                    } else {// 编辑失败
                        parentAlertMsg(msg + "失败");
                        // var tip = parent.layer.alert(msg + "失败", {
                        //     offset: "30%"
                        // });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    parentCloseAll();
                    parentAlertMsg(msg + "失败");
                    // parent.layer.close(layerLoad);
                    // parent.layer.alert(msg + "失败", {
                    //     offset: "30%"
                    // });
                    return;
                }
            });
            parent.layer.closeAll();
        });
    }

}
function getProductId(proId) {
    $.ajax({
        type: "post",
        url: "mGroupBuy/getSpecificaByProId.do",
        data: {
            proId: proId
        },
        dataType: "json",
        success: function (data) {
            if (data != null && data.list != null) {
                proSpecArr = data.list;
            }
            eval(step.Creat_Table());// 初始化创建库存表格
        }

    });
}
/**
 * 编辑团购
 */
function editJoinProduct() {
    var shopId = $(".shopId").find("option:selected").val();//店铺id
    var productId = $("#productId").val();//商品id
    var commissionType = $(".commissionType").find("option:selected").val();//佣金类型
    var ids = $("#ids").val();
    if (shopId == null || shopId == "") {
        parentAlertMsg('请选择店铺');
        // layer.msg('请选择店铺', {
        //     offset: "30%",
        //     icon: 1
        // });
    } else if (productId == null || productId == "") {
        parentAlertMsg('请选择商品');
        // layer.msg('请选择商品', {
        //     offset: "30%",
        //     icon: 1
        // });
    } else if (commissionType == null || commissionType == "") {
        parentAlertMsg('请选择佣金类型');
        // layer.msg('请选择佣金类型', {
        //     offset: "30%",
        //     icon: 1
        // });
    } else {
        var flag = true;
        $("input[datatype!=null]").each(function () {
            var bol = true;
            if ($(this).attr("name") == "gPrice" && isSpec == 1) {
                bol = false;
            }
            if (bol && flag) {
                flag = valiReg($(this));
                if (!flag) {
                    return;
                }
            }
        });
        var joinProduct = $("#joinProductForm").serializeObject();
        if (ids != null && ids != "") {
            joinProduct["id"] = ids;
        }
        if (!flag) {
            parentAlertMsg('请完善商品佣金');
            // layer.msg('请完善商品佣金', {
            //     icon: 1,
            //     offset: "30%"
            // });
        } else {
            // loading层
            var layerLoad = parentLayerLoad();
            // var layerLoad = parent.layer.load(1, {
            //     offset: "30%",
            //     shade: [0.1, '#fff']
            //     // 0.1透明度的白色背景
            // });
            $.ajax({
                type: "post",
                url: "/mallSellers/editJoinProduct.do",
                data: {
                    joinProduct: JSON.stringify(joinProduct)
                },
                dataType: "json",
                success: function (data) {
                    parentCloseAll();
                    // parent.layer.close(layerLoad);
                    if (data.flag) {
                        parentAlertMsg("编辑成功");
                        //TODO alert 跳转
                        // var tip = parent.layer.alert("编辑成功", {
                        //     offset: "30%",
                        //     closeBtn: 0
                        // }, function (index) {
                        //     parent.layer.close(tip);
                        //     location.href = "/mallSellers/joinProduct.do";
                        // });
                    } else {
                        if (data.msg != null && data.msg != "") {
                            parentAlertMsg(data.msg);
                            // parent.layer.alert(data.msg, {
                            //     offset: "30%"
                            // });
                        } else {
                            parentAlertMsg("编辑失败");
                            // parent.layer.alert("编辑失败", {
                            //     offset: "30%"
                            // });
                        }
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    parentCloseAll();
                    parentAlertMsg("编辑失败");
                    // parent.layer.close(layerLoad);
                    // parent.layer.alert("编辑失败", {
                    //     offset: "30%"
                    // });
                    return;
                }
            });
        }
    }
}
$(".commissionType").change(function () {
    var val = $(this).find("option:selected").val();
//	$(".baiRed").show();
//	$(".mRed").hide();
    if (val == 1) {
        $("em.unit").html("%");
        $(".commissionTip").show();
        $(".baiRed").show();
        $(".mRed").hide();
    } else {
        $("em.unit").html("元");
        $(".commissionTip").hide();
        $(".mRed").show();
        $(".baiRed").hide();
    }
});
var commissionType = $(".commissionType").find("option:selected").val();
if (commissionType == 1) {
    $("em.unit").html("%");
    $(".commissionTip").show();
    $(".baiRed").show();
    $(".mRed").hide();
} else {
    $("em.unit").html("元");
    $(".commissionTip").hide();
    //$(".baiRed").show();
    //$(".mRed").hide();

    $(".mRed").show();
    $(".baiRed").hide();
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
        if (obj.attr("max") != null && obj.attr("max") != "") {
            var max = obj.attr("max");
            if (max * 1 < obj.val() * 1) {
                obj.css("border-color", "red");
                //obj.next().css("color","red");
                return false;
            }
        }
        if (flag && obj.val() * 1 <= 0) {
            obj.css("border-color", "red");
            //obj.next().css("color","red");
            return false;
        } else {
            obj.css("border-color", "#c5c5c5");
            //obj.next().css("color","#000");
            return true;
        }
    } else if (obj.attr("yj") == "1") {
        var flag = true;
        var commissionType = $(".commissionType option:selected").val();
        var val = obj.val();
        if (val * 1 <= 0) {
            flag = false;
        }
        var cla = "mRed";
        if (commissionType == 1) {//百分百
            var cla = "baiRed";
            if (val * 1 > 70) {
                flag = false;
            }
        } else {//固定金额
            var proPrice = $("#proPrice").val();
            if (proPrice != null && proPrice != "") {
                proPrice = proPrice * 1;
                var max = (proPrice * 0.7).toFixed(2);
                if (val * 1 > max) {
                    flag = false;
                }
            }
        }
        if (!flag) {
            obj.css("border-color", "red");
        } else {
            obj.css("border-color", "#c5c5c5");
            return true;
        }
        return flag;
    } else {
        var flag = true;
        if (obj.attr("not-null") != null) {
            if (obj.val() == null || $.trim(obj.val()) == "") {
                obj.css("border-color", "red");
                //obj.next().css("color","red");
                flag = false;
            }
        }
        if (flag) {
            obj.css("border-color", "#c5c5c5");
            //obj.next().css("color","#000");
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
        parentOpenIframe("选择商品", "600px", "480px", "/mGroupBuy/getProductByGroup.do?shopId=" + shopId + "&defaultProId=" + defaultProId + "&isCommission=1");//check==0代表多选，check==1代表单选
    } else {
        parentAlertMsg("请选择商品");
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
        $("#proPrice").val(obj.price);

        if (arr != null && obj.isSpe == 1) {
            proSpecArr = arr;
//			console.log(proSpecArr)
        } else {
        }

    }
    loadWindow();
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
$(".shopId").change(function () {
    $("a.add-goods").css("background-image", "");
    $("span.proName").html("");
    $("span.proPrice").html("");
    $(".table").hide();
    $(".gPrice").show();
    $(".gPrice").val("");
});
function findGroup(obj) {
    var html = "/mGroupBuy/index.do";
    var type = $(obj).find("option:selected").val();
    if (type != null && type != "") {
        html += "?type=" + type;
    }
    location.href = html;
}