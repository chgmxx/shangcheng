//删除物流
function deleteFreight(obj) {
    var id = $(obj).attr("id");
    if (id != null && id != "") {
        // 询问框
        layer.confirm('您确定要删除？', {
            btn: ['确定', '取消'],
            shade:[0.1,'#fff'],
            offset: "30%"
            // 按钮
        }, function () {
            var idArr = [];
            idArr.push(id);
            // loading层
            var layerLoad = layer.load(1, {
                offset: "30%",
                shade: [0.1, '#fff']
                // 0.1透明度的白色背景
            });
            $.ajax({
                type: "post",
                url: "mFreight/deleteFreight.do",
                data: {
                    id: JSON.stringify(idArr)
                },
                dataType: "json",
                success: function (data) {
                    layer.close(layerLoad);
                    if (data.flag) {// 重新登录
                        var tip = layer.alert("删除成功", {
                            offset: "30%",
                            shade:[0.1,"#fff"],
                            closeBtn: 0
                        }, function (index) {
                            layer.closeAll();
                            location.href = "/mFreight/index.do";
                        });
                    } else {// 删除失败
                        var tip = layer.alert("删除失败", {
                            shade:[0.1,"#fff"],
                            offset: "30%"
                        });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.close(layerLoad);
                    layer.alert("删除失败", {
                        shade:[0.1,"#fff"],
                        offset: "30%"
                    });
                    return;
                }
            });
        });
    }

}


function testFreight() {
    var flag = true;
    var priceType = $(".priceType:checked").val();
    var objId = priceType;
    if (objId != 0) {
        objId = 1;
    }
    var obj = $(".groupDiv.priceType" + objId);
    var money = obj.find("input#money").val();
    freight["priceType"] = priceType;
    freight["money"] = money;
    if (priceType == 0) {
        flag = valiFreight(money, moneyTest, "input#money", obj);
        freight["firstNums"] = 0;
        freight["addNums"] = 0;
        freight["addMoney"] = 0;
    }
    if (priceType == 1 || priceType == 2 || priceType == 3) {
        var firstNums = obj.find("input#firstNums").val();
        var addNums = obj.find("input#addNums").val();
        var addMoney = obj.find("input#addMoney").val();
        var flag1 = valiFreight(firstNums, moneyTest, "input#firstNums", obj);//首件 验证数字
        var flag2 = valiFreight(money, moneyTest, "input#money", obj);//首件运费 验证小数
        var flag3 = valiFreight(addNums, moneyTest, "input#addNums", obj);//续件 验证数字
        var flag4 = valiFreight(addMoney, moneyTest, "input#addMoney", obj);//续件运费 验证小数
        if (!flag1 || !flag2 || !flag3 || !flag4) {
            flag = false;
        } else {
            freight["firstNums"] = firstNums;
            freight["addNums"] = addNums;
            freight["addMoney"] = addMoney;
        }

    }
    /*if(priceType == 2){
     var firstNums = obj.find("input#firstNums").val();
     var addNums = obj.find("input#addNums").val();
     var addMoney = obj.find("input#addMoney").val();
     var flag1 = valiFreight(firstNums,moneyTest,"input#firstNums",obj);//首件 验证小数
     var flag2 = valiFreight(money,moneyTest,"input#money",obj);//首件运费 验证小数
     var flag3 = valiFreight(addNums,moneyTest,"input#addNums",obj);//续件 验证小数
     var flag4 = valiFreight(addMoney,moneyTest,"input#addMoney",obj);//续件运费 验证小数
     if(!flag1 || !flag2 || !flag3 || !flag4){
     flag = false;
     }else{
     freight["firstNums"] = firstNums;
     freight["addNums"] = addNums;
     freight["addMoney"] = addMoney;
     }
     }*/
    if (!flag)
        alert("运费信息不完整，修改标红色文本框数据");
    return flag;
}

function valiFreight(val, valTest, valiId, obj) {
    var flag = true;
    if (val == null || val == "") {
        errorMsg(obj.find(valiId));
        flag = false;
    } else if (!valTest.test(val)) {
        errorMsg(obj.find(valiId));
        flag = false;
    } else {
        var isNull = 0;
        var tObj = obj.find(valiId);
        if (typeof(tObj.attr("notNull")) != "undefined") {
            isNull = tObj.attr("notNull");
        }
        if (val * 1 <= 0 && isNull == 0) {
            errorMsg(obj.find(valiId));
            flag = false;
        } else {
            obj.find(valiId).css("border", "1px solid #a9a9a9");
            obj.parent().find(".red").css("color", "#999");
        }

    }
    return flag;
}

var detailArr = new Array();
var moneyTest = /^[0-9]{1}\d{0,5}(\.\d{1,2})?$/;
var numTest = /^[0-9]{1,6}$/;
var freight = {};

/**
 * 编辑运费
 */
function editFreight() {
    var flag = true;
    var name = $("#name").val();
    var shopId = $(".shopId").find("option:selected").val();
    var isNoMoney = $(".isNoMoney:checked").val();
    var noMoney = $("#noMoney").val();
    var num = $("#num").val();
    var isResultMoney = 0;
    var expressId = $("#expressId").find("option:selected").val();
    var express = $("#expressId").find("option:selected").text();
    var id = $("#ids").val();
    var params = {};
    if ($(".isResultMoney").is(":checked") == true) {
        isResultMoney = 1;
    }
    if (name == null || $.trim(name) == "") {
        $("#name").css("border-color", "red");
        $("#name").parent().find(".red").css("color", "red");
        flag = false;
    }
    if (noMoney == null || $.trim(noMoney) == "") {
        noMoney = 0;
    }
    if (num == null || $.trim(num) == "") {
        num = 0;
    }
    var detailFlag = true;
    if (isNoMoney == 1) {

        var flags = testFreight();
        if (!flags) {
            flag = false;
        }
        if (!numTest.test(num)) {
            $("#num").css("border-color", "red");
            $("#num").parent().find(".red").css("color", "red");
            $("#num").focus();
            flag = false;
        }

        var numFlag = valiReg($("#num"));
        if (!numFlag) {
            flag = false;
            $("#num").focus();
        }
        if (flag && numFlag) {
            flag = valiReg($("#noMoney"));
            if (!flag) {
                $("#noMoney").focus();
                return flag;
            }
        }


        detailArr = new Array();
        if (isResultMoney == 1 && isNoMoney == 1) {
            detailFlag = eachDetail();
            params["detail"] = JSON.stringify(detailArr);
        }
    } else {
        money = 0;
        num = 0;
        isResultMoney = 0;
    }
    if (num == null || $.trim(num) == "") {
        num = 0;
    }
    if (noMoney == null || $.trim(noMoney) == "") {
        noMoney = 0;
    }
    freight["name"] = name;
    freight["shopId"] = shopId;
    freight["isNoMoney"] = isNoMoney;
    freight["noMoneyNum"] = changeTwoDecimal_f(num);
    freight["noMoney"] = changeTwoDecimal_f(noMoney);
    freight["isResultMoney"] = isResultMoney;
    freight["expressId"] = expressId;
    freight["express"] = express;
    if (id != null && id != "") {
        freight.id = id;
    }
    params["freight"] = JSON.stringify(freight);
    if (dDetailObj != null) {
        params["delDetail"] = JSON.stringify(dDetailObj);
    }
    if (dProvinceObj != null) {
        params["delPro"] = JSON.stringify(dProvinceObj);
    }

    if (!flag || !detailFlag) {
        layer.msg('请完善物流信息', {
            shade:[0.1,"#fff"],
            icon: 1
        });
    } else {

//		if(isResultMoney == 1){
//			eachDetail();
//			params["detail"] = JSON.stringify(detailArr);
//		}

        // loading层
        var layerLoad = layer.load(1, {
            offset: "30%",
            shade: [0.3, '#fff']
        });
        $.ajax({
            type: "post",
            url: "mFreight/editFreight.do",
            data: params,
            dataType: "json",
            success: function (data) {
                layer.close(layerLoad);
                if (data.flag == true) {
                    var tip = layer.alert("编辑成功", {
                        shade:[0.1,"#fff"],
                        offset: "30%",
                        closeBtn: 0
                    }, function (index) {
                        layer.close(tip);
                        window.location.href = "/mFreight/start.do";
                    });
                } else {// 编辑失败
                    layer.alert("编辑失败", {
                        shade:[0.1,"#fff"],
                        offset: "30%"
                    });
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.close(layerLoad);
                layer.alert("编辑失败", {
                    shade:[0.1,"#fff"],
                    offset: "30%"
                });
                return;
            }
        });

    }
}

function valiFreightDetail(val, valTest, valiId, obj) {
    var flag = true;
    if (val == null || val == "") {
        errorMsg(obj.find(valiId));
        flag = false;
    } else if (!valTest.test(val)) {
        errorMsg(obj.find(valiId));
        flag = false;
    } else {
        var isNull = 0;
        var tObj = obj.find(valiId);
        if (typeof(tObj.attr("notNull")) != "undefined") {
            isNull = tObj.attr("notNull");
        }
        if (val * 1 <= 0 && isNull == 0) {
            errorMsg(obj.find(valiId));
            flag = false;
        } else {
            obj.find(valiId).css("border", "1px solid #a9a9a9");
            obj.parent().find(".red").css("color", "#999");
        }
    }
    return flag;
}

function eachDetail() {
    var flag = false;
    $(".table #J_Tbody tr[data-index!='']").each(function () {
        var provinceId = $(this).find(".provinceId").val();
        var province = $(this).find(".province").val();
        var expressId = $(this).find(".expressId").find("option:selected").val();
        var express = $(this).find(".expressId").find("option:selected").text();
        var dMoney = $(this).find("input.dMoney").val();
        var dNoMoneyNum = $(this).find("input.dNoMoneyNum").val();
        var dNoMoney = $(this).find("input.dNoMoney").val();
        var id = $(this).find(".dId").val();
        var tipMsg = "";
        if (provinceId == null || $.trim(provinceId) == "") {
            tipMsg += "<span class='msg J_Message proSpan'><span class='error'>请选择省份</span></span>";
        }
        if (expressId == null || $.trim(expressId) == "" || $.trim(expressId) == "-1") {
            tipMsg += "<span class='msg J_Message comSpan'><span class='error'>请选择快递公司</span></span>";

        }
        var priceType = $(".priceType:checked").val();

        var money = $(this).find(".types2 input#money").val();
        var firstNums = $(this).find("input#firstNums").val();
        var addNums = $(this).find("input#addNums").val();
        var addMoney = $(this).find("input#addMoney").val();
        var obj = $(this);
        if (priceType == 0) {
            money = $(this).find(".types1 input#money").val();
            var flags = valiFreightDetail(money, moneyTest, "input#money", obj);
            if (!flags) {
                flag = false;
            } else {
                flag = true;
                firstNums = 0;
                addNums = 0;
                addMoney = 0;
            }
        }
        if (priceType == 1 || priceType == 2 || priceType == 3) {

            var flag1 = valiFreightDetail(firstNums, moneyTest, "input#firstNums", obj);//首件 验证数字
            var flag2 = valiFreightDetail(money, moneyTest, "input#money", obj);//首件运费 验证小数
            var flag3 = valiFreightDetail(addNums, moneyTest, "input#addNums", obj);//续件 验证数字
            var flag4 = valiFreightDetail(addMoney, moneyTest, "input#addMoney", obj);//续件运费 验证小数
            if (!flag1 || !flag2 || !flag3 || !flag4) {
                flag = false;
            } else {
                flag = true;
            }
        }
        /*if(priceType == 2){
         var flag1 = valiFreightDetail(firstNums,moneyTest,"input#firstNums",obj);//首件 验证小数
         var flag2 = valiFreightDetail(money,moneyTest,"input#money",obj);//首件运费 验证小数
         var flag3 = valiFreightDetail(addNums,moneyTest,"input#addNums",obj);//续件 验证小数
         var flag4 = valiFreightDetail(addMoney,moneyTest,"input#addMoney",obj);//续件运费 验证小数
         if(!flag1 || !flag2 || !flag3 || !flag4){
         flag = false;
         }else{
         flag = true;
         }
         }*/
        if (!flag) {
            tipMsg += "<span class='msg J_Message moneySpan'><span class='error'>请完善运费信息，修改标红色文本框数据</span></span>";
        }


        /*if (!moneyTest.test(dMoney)) {
         tipMsg += "<span class='msg J_Message moneySpan'><span class='error'>运费最多只能是大于0的5位数，如：12</span></span>";
         }else if(dMoney * 1 <= 0){
         tipMsg += "<span class='msg J_Message moneySpan'><span class='error'>运费最多只能是大于0的5位数，如：12</span></span>";
         }*/

        if (tipMsg != "") {
            $(".J_DefaultMessage").html(tipMsg);
            $(".J_DefaultMessage").parent().show();
            flag = false;
        }
        $(this).find("input.input-text").each(function () {
            var flags = valTableInp(this);
            if (!flags) {
                loads();
                flag = false;
            }
        });

        if (flag) {
            $(".J_DefaultMessage").parent().hide();
            if (dNoMoneyNum == null || $.trim(dNoMoneyNum) == "") {
                dNoMoneyNum = 0;
            }
            if (dNoMoney == null || $.trim(dNoMoney) == "") {
                dNoMoney = 0;
            }

            var detailObj = new Object();
            detailObj["provincesId"] = provinceId;
            detailObj["provinces"] = province;
            detailObj["money"] = changeTwoDecimal_f(dMoney);
            detailObj["expressId"] = expressId;
            detailObj["express"] = express;
            detailObj["noMoneyNum"] = changeTwoDecimal_f(dNoMoneyNum);
            detailObj["noMoney"] = changeTwoDecimal_f(dNoMoney);
            detailObj["firstNums"] = changeTwoDecimal_f(firstNums);
            detailObj["addNums"] = changeTwoDecimal_f(addNums);
            detailObj["addMoney"] = changeTwoDecimal_f(addMoney);
            detailObj["money"] = changeTwoDecimal_f(money);
            if (id != null && $.trim(id) != "") {
                detailObj["id"] = id;
            }
            var proArr = new Array();

            var proSpli = provinceId.split(",");
            var nameSpli = province.split(",");
            for (var i = 0; i < proSpli.length; i++) {
                if (proSpli[i] != "") {
                    var provinceObj = {
                        provinceId: proSpli[i],
                        provinceName: nameSpli[i]
                    }
                    if (proObj != null) {
                        if (proObj[provinceId] != "") {
                            provinceObj["id"] = proObj[proSpli[i]];
                        }
                    }
                    proArr[proArr.length] = provinceObj;
                }
            }
            detailObj["provinceList"] = JSON.stringify(proArr);
            detailArr[detailArr.length] = detailObj;
            loadFrame();
        } else {
            loads();
            //return flag;
        }
    });
    return flag;
}
/**
 * 鼠标选中事件
 */
$("input[datatype!=''||notNull!='']").focus(function () {
    if ($(this).attr("datatype") != null && $(this).attr("notNull") != "")
        valiReg($(this));
});
/**
 * 鼠标失去焦点
 */
$("input[datatype!=''||notNull!='']").blur(function () {
    if ($(this).attr("datatype") != null && $(this).attr("notNull") != "")
        valiReg($(this));
});
loadValTable();
function loadValTable() {
    $("input.inp_test").focus(function () {
        valTableInp(this);
    });
    $("input.inp_test").blur(function () {
        valTableInp(this);
    });
    $("select.expressId").change(function () {
        var val = $(this).find("option:selected").val();
        var flag = true;
        if (val == "-1") {
            flag = false;
        }
        valTable(flag, "comSpan", "请选择快递公司");
    });
}
function valTableInp(obj) {
    var val = $.trim($(obj).val());
    var dataTest = $(obj).attr("dataType");
    dataTest = new RegExp(dataTest);
    var dataNull = $(obj).attr("dataNull");
    var flag = true;
    if (!dataTest.test(val)) {
        flag = false;
    }
    if (dataNull == 0 && val * 1 <= 0) {
        flag = false;
    } else if (dataNull == 1 && val != null && val != "" && val * 1 <= 0) {
        flag = false;
    }
    var dataClass = $(obj).attr("dataClass");
    var dataMsg = $(obj).attr("dataMsg");
    valTable(flag, dataClass, dataMsg);
    return flag;
}
function valTable(flag, cla, msg) {
    if (!flag) {
        $(".J_DefaultMessage").parent().show();
        if (typeof($(".msg." + cla).html()) == "undefined") {
            $(".J_DefaultMessage").append("<span class='msg J_Message " + cla + "'><span class='error'>" + msg + "</span></span>");
        } else {
            $(".msg." + cla).html("<span class='error'>运费最多只能是大于0的5位数，如：12</span>");
        }
    } else {
        $(".J_DefaultMessage ." + cla).remove();
    }
    loadFrame();
}
//编辑地区
function selectPro(obj) {
    var index = obj.parents("tr").attr("data-index");
    var proObj = obj.parent();
    var selectPro = proObj.find(".province").val();
    var hidePro = "";
    $(".table tr[data-index!='" + index + "']").each(function () {
        var proName = $(this).find(".province").val();
        if (proName != null && $.trim(proName) != "") {
            if (hidePro != "") {
                hidePro += ",";
            }
            hidePro += proName;
        }
    });
    var url = "mFreight/provincePopUp.do?index=" + index;
    if ($.trim(selectPro) != "" && $.trim(selectPro) != "未添加省份") {
        url += "&selectPro=," + selectPro + ",";
    }
    if ($.trim(hidePro) != "") {
        url += "&hidePro=," + hidePro + ",";
    }
    parentOpenIframe("选择可配送区域", "450px", "350px", url);
    // parent.openIframeNoScoll("选择可配送区域", "450px", "350px", url);
}


function getProvinces(id, province, index) {

    $(".table td.type" + index).find(".provinceId").val(id);
    $(".table td.type" + index).find(".province").val(province);
    if (province == "") {
        province = "未添加省份";
        valTable(false, "proSpan", "请选择省份");
    } else {
        valTable(true, "proSpan", "请选择省份");
    }
    $(".table td.type" + index).find(".area-group p").html(province);
    loadFrame();
}

/**
 * 验证正则表达式
 *
 * @param obj
 */
function valiReg(obj) {
    var datatype = obj.attr("datatype");// 正则表达式
    datatype = new RegExp(datatype);
    var flag = true;

    if (!datatype.test(obj.val())) {
        obj.css("border-color", "red");
//		obj.parent().find(".red").show();
        obj.parent().find(".red").css("color", "red");
        return false;
    } else if (obj.attr("name") == "money") {
        return validateNum(obj);
    } else {
        if (obj.attr("notNull") == "1") {
            if (obj.val() == null || $.trim(obj.val()) == "") {
                obj.css("border-color", "red");
//				obj.parent().find(".red").show();
                obj.parent().find(".red").css("color", "red");
                flag = false;
            }
        }
        if ((obj.attr("name") == "num" || obj.attr("name") == "noMoney") && obj.val() != "") {
            flag = validateNum(obj);
        }
        if (flag) {
            obj.css("border-color", "#a9a9a9");
//			obj.parent().find(".red").hide();
            obj.parent().find(".red").css("color", "");
        }
        return flag;
    }
}

function validateNum(obj) {
    if (obj.val() * 1 <= 0) {
        obj.css("border-color", "red");
//		obj.parent().find(".red").show();
        obj.parent().find(".red").css("color", "red");
        return false;
    } else {
        obj.css("border-color", "#a9a9a9");
//		obj.parent().find(".red").hide();
        obj.parent().find(".red").css("color", "");
        return true;
    }
}
function loadFrame() {
    loadWindow();
}
function changeTwoDecimal_f(x) {
    var f_x = parseFloat(x);
    if (!isNaN(f_x)) {
        var f_x = Math.round(x * 100) / 100;
        var s_x = f_x.toString();
        var pos_decimal = s_x.indexOf('.');
        if (pos_decimal < 0) {
            pos_decimal = s_x.length;
            s_x += '.';
        }
        while (s_x.length <= pos_decimal + 2) {
            s_x += '0';
        }
        return s_x;
    }
    return 0;
}
function errorMsg(obj) {
    obj.css("border", "1px solid red");
    obj.parent().find(".red").css("color", "red");
    //obj.focus();


    onload();
}
/**
 * 改变计价方式的时间
 */
$(".priceType").change(function () {
    var val = $(this).val();
    $(".groupDiv#hideDiv").hide();
    var objIds = val;
    if (objIds != 0) {
        objIds = 1;
    }
    var obj = $(".groupDiv.priceType" + objIds);
    if (val != 0) {
        obj.find("#firstNums").css("border-color", "#a9a9a9");
        obj.find("#money").css("border-color", "#a9a9a9");
        obj.find("#addNums").css("border-color", "#a9a9a9");
        obj.find("#addMoney").css("border-color", "#a9a9a9");

        $(".types2").show();
        $(".types1").hide();
        if (val == "1") {
            $("em.unit").html("件");
        } else if (val == "2") {
            $("em.unit").html("g");
        } else if (val == "3") {
            $("em.unit").html("km");
        }
    } else {
        obj.find("#money").val("");
        $(".types1").show();
        $(".types2").hide();
    }
    obj.show();
    onload();
});

/**
 * 鼠标选中事件
 */
$(".inpt2").focus(function () {
    blurFreight($(this));
});
/**
 * 鼠标失去焦点
 */
$(".inpt2").blur(function () {
    blurFreight($(this));
});

function blurFreight(obj) {
    var priceType = $(".priceType:checked").val();
    //var obj = $(".groupDiv.priceType"+priceType);
    var val = obj.val();
    var id = "#" + obj.attr("id");
    var pObj = obj.parent();
    if (priceType == 0 && obj.attr("id") == "money") {

        flag = valiFreight(val, moneyTest, id, pObj);
    }
    /*if(priceType == 1){
     var test = "";
     if(obj.attr("id") == "firstNums" || obj.attr("id") == "addNums"){
     test = numTest;
     }else{
     test = moneyTest;
     }
     flag = valiFreight(val,test,id,pObj);//首件 验证数字
     }*/
    if (priceType == 1 || priceType == 2 || priceType == 3) {
        flag = valiFreight(val, moneyTest, id, pObj);//首件 验证数字
    }
}
onload();
function onload() {
    var priceType = $(".priceType:checked").val();
    if (priceType == 1) {
        var firstNums = $(".priceType1 #firstNums").val();
        var addNums = $(".priceType1 #addNums").val();
        if (firstNums != null && firstNums != "") {
            $(".priceType1 #firstNums").val(parseInt(firstNums));
        }
        if (addNums != null && addNums != "") {
            $(".priceType1 #addNums").val(parseInt(addNums));
        }
    }
}