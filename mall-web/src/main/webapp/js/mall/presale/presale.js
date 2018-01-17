/**
 * 删除预售
 */
function deletePresale(obj, type) {
    var id = $(obj).attr("id");
    if (id != null && id != "") {
        parentScrollTops(100);
        SonScrollTop(0);
        setTimeout(function () {
            var msg = "删除预售";
            if (type * 1 == -2) {
                msg = "使失效";
            }
            // 询问框
            layer.confirm('您确定要' + msg + '？', {
                btn: ['确定', '取消'],
                offset: scrollHeight + "px",
                shade: [0.1, '#fff']
                // 按钮
            }, function () {
                // loading层
                var layerLoad = layer.load(1, {
                    shade: [0.1, '#fff'],
                    offset: scrollHeight + "px"
                    // 0.1透明度的白色背景
                });
                $.ajax({
                    type: "post",
                    url: "mPresale/presale_remove.do",
                    data: {
                        id: id,
                        type: type
                    },
                    dataType: "json",
                    success: function (data) {
                        layer.close(layerLoad);
                        if (data.code == 1) {
                            var tip = layer.alert(msg + "成功", {
                                offset: scrollHeight + "px",
                                closeBtn: 0
                            }, function (index) {
                                layer.close(tip);
                                location.href = window.location.href;
                            });
                        } else {// 编辑失败
                            var tip = layer.alert(msg + "失败", {
                                offset: scrollHeight + "px"
                            });
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {

                        layer.close(layerLoad);
                        layer.alert(msg + "失败", {
                            offset: scrollHeight + "px"
                        });
                        return;
                    }
                });
                layer.closeAll();
            });
        }, timeout);
    }

}

function loadLayDate() {
    //初始化预计发货开始时间
    var datebox_1 = {
        elem: '#sStartTime',
        event: 'focus',
        format: 'YYYY-MM-DD hh:mm:ss',
        festival: true,
        istime: true,
        min: laydate.now(),
        choose: function (datas) {
            if (datas < laydate.now()) {
                datas = laydate.now();
                $('#sStartTime').val(datas)
            }
            datebox_2.min = datas; // 开始日选好后，重置结束日的最小日期
            datebox_2.start = datas; // 将结束日的初始值设定为开始日
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
        min: laydate.now(),
        choose: function (datas) {
            //datebox_1.max = datas;
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

    /*var isSpec = $("#isSpec").val();
     if(isSpec == 1){
     getProductId($("#productId").val());
     }*/
    loadWindow();

}
$("input").focus(function () {
    loadWindow();
});

function loadLaydateObj(i) {
    var datebox_3 = {
        elem: "#startTime" + i,
        event: 'focus',
        festival: true,
        format: 'YYYY-MM-DD hh:mm:ss',
        min: laydate.now(),
        istime: true,
        choose: function (datas) {
            datebox_4.min = datas; // 开始日选好后，重置结束日的最小日期
            datebox_4.start = datas; // 将结束日的初始值设定为开始日
            loadWindow();
        }
    }
    var datebox_4 = {
        elem: '#endTime' + i,
        event: 'focus',
        festival: true,
        istime: true,
        format: 'YYYY-MM-DD hh:mm:ss',
        min: laydate.now(),
        choose: function (datas) {
            //datebox_4.min = datas; // 开始日选好后，重置结束日的最小日期
        }
    }
    laydate(datebox_3);
    laydate(datebox_4);
}

$(".time_inp").blur(function () {
    var index = $(this).parents(".timeDiv").index();

    if ($(this).val() != null && $(this).val() != "") {
        $(this).parent().find("span.red").html("");
        $(this).css({
            "color": "#000",
            "border-color": "#c5c5c5"
        });
    }
});

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

$(".times_span").click(function () {
    if (!valiForm()) {
        layer.msg('请完善商品信息', {
            offset: '10%',
            icon: 1
        });
        return;
    }
    endTime();

});

$(document).ready(function () {
    if (typeof $("#sStartTime").val() != "undefined")
        loadLayDate();

//	if($("#aucStartPrice").length > 0 && $("#ids").val()!='' && $(".aucType:checked").val() == 1)
//		endTime();
    if ($(".isDeposit").is(":checked")) {
        $(".aucDepositDiv").show();
        $(".hidePrice").addClass("vali");
    } else {
        $(".aucDepositDiv").hide();
        $(".hidePrice").removeClass("vali");
    }

    /*var type = $("input.types:checked").val();
     if(type == 1){
     $("label.type_label").html("上涨");
     }else{
     $("label.type_label").html("下调");
     }*/

    if ($(".aucType:checked").val() == 1) {
        $(".diffDiv").show();
        $(".addDiv").hide();

        $(".addDiv input").removeClass("vali");
        $(".diffDiv input").addClass("vali");
    } else {
        $(".diffDiv").hide();
        $(".addDiv").show();
        $(".diffDiv input").removeClass("vali");
        $(".addDiv input").addClass("vali");
    }

    /**
     * 循环加载预售时间控件
     */
    $(".p_div .timeDiv").each(function () {
        var startObj = $(this).find('.startTime');
        var endObj = $(this).find('.endTime');
        if (startObj.length > 0 && endObj.length > 0) {
            loadLaydateObj($(this).attr("id"));

        }
    });

    /*var saleType = $(".saleType").val();
     if(saleType != null && saleType != ""){
     $("input#types"+saleType).attr("checked","checked");
     }*/

    $("input.tPrice").each(function () {
        var tPrice = $(this).val();
        if (tPrice != null && tPrice != "") {
            //tPrice = parseInt(tPrice);
            //$(this).val(tPrice);
            var pObj = $(this).parents(".timeDiv");
            var types = pObj.find("input.types:checked").val();
            if (types == 1) {
                pObj.find(".type_label").html("上涨");
            } else {
                pObj.find(".type_label").html("下调");
            }
            jisuanPrice(tPrice, $(this), types)
        }
    });

    if ($("#productId").val() != null && $("#productId").val() != "") {
        getProductId($("#productId").val());
    }

    minPrice = $(".proPrice").val();

    //loadWindow();

});

/**
 * 验证表单
 */
function valiForm() {
    var flag = true;
    $("input.vali").each(function () {
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
var presaleTimesArray = new Array();
var delTimeList = new Array();
/**
 * 编辑预售
 */
function editPresale() {
    var shopId = $(".shopId").find("option:selected").val();//店铺id
    var sStartTime = $("#sStartTime").val();//活动生效开始时间
    var sEndTime = $("#sEndTime").val();//活动结束时间
    var productId = $("#productId").val();//商品id
    var aucLowestPrice = $("#aucLowestPrice").val();
    var ids = $("#ids").val();
    var flag = valiForm();

    var start = new Date(sStartTime.replace("-", "/").replace("-", "/"));
    var end = new Date(sEndTime.replace("-", "/").replace("-", "/"));
    parentScrollTops(100);
    SonScrollTop(0);
    setTimeout(function () {
        if (shopId == null || shopId == "") {
            layer.msg('请选择店铺', {
                icon: 1,
                offset: scrollHeight + "px",
                shade: [0.1, '#fff']
            });
        } else if (productId == null || productId == "") {
            layer.msg('请选择商品', {
                icon: 1,
                offset: scrollHeight + "px",
                shade: [0.1, '#fff']
            });
        } else if (sStartTime == null || $.trim(sStartTime) == "") {
            layer.msg('请选择活动开始时间', {
                icon: 1,
                offset: scrollHeight + "px",
                shade: [0.1, '#fff']
            });
        } else if (sEndTime == null || sEndTime == "") {
            layer.msg('请选择活动结束时间', {
                icon: 1,
                offset: scrollHeight + "px",
                shade: [0.1, '#fff']
            });
        } else if (start * 1 > end * 1) {
            layer.msg('活动开始时间必须要早于活动结束时间', {
                icon: 1,
                offset: scrollHeight + "px",
                shade: [0.1, '#fff']
            });
        } else if (!flag) {
            layer.msg('请完善商品信息', {
                icon: 1,
                offset: scrollHeight + "px",
                shade: [0.1, '#fff']
            });
        } else {
            var presale = $("#groupForm").serializeObject();

            flag = validatePriceTime();
            var datas = {
                presale: JSON.stringify(presale),
                presaleTimes: JSON.stringify(presaleTimesArray)
            };
            if (delTimeList != null && delTimeList.length > 0) {
                datas["delPresaleTimes"] = JSON.stringify(delTimeList);
            }
            /*var isSpec = $(".isSpec").val();
             var invNum = $(".invNum").val();
             if(invNum != null && invNum != "" && typeof(invNum) != "undefined"){
             datas["invNum"] = JSON.stringify(invNum);
             }
             if(isSpec != null && isSpec != "" && typeof(isSpec) != "undefined"){
             datas["isSpec"] = JSON.stringify(isSpec);
             if(isSpec == 1 && specArray != null && specArray.length > 0){
             datas["specArr"] = JSON.stringify(specArr);
             }
             }*/


            if (flag) {
                // loading层
                var layerLoad = layer.load(1, {
                    offset: scrollHeight + "px",
                    shade: [0.1, '#fff']
                    // 0.1透明度的白色背景
                });
                $.ajax({
                    type: "post",
                    url: "mPresale/edit_presale.do",
                    data: datas,
                    dataType: "json",
                    success: function (data) {
                        layer.close(layerLoad);
                        if (data.code == 1) {
                            var tip = layer.alert("编辑成功", {
                                offset: scrollHeight + "px",
                                shade: [0.1, "#fff"],
                                closeBtn: 0
                            }, function (index) {
                                layer.close(tip);
                                location.href = "/mPresale/index.do";
                            });
                        } else if (data.code == -2) {
                            var tip = layer.alert("正在进行预售的活动不能修改", {
                                offset: scrollHeight + "px",
                                shade: [0.1, "#fff"],
                                closeBtn: 0
                            });
                        } else if (data.code == -3) {
                            var tip = layer.alert("已失效的预售不能进行修改", {
                                offset: scrollHeight + "px",
                                shade: [0.1, "#fff"],
                                closeBtn: 0
                            });
                        } else if (data.code == 0) {
                            var tip = layer.alert("同一个商品只能参与一个预售活动", {
                                offset: scrollHeight + "px",
                                shade: [0.1, "#fff"],
                                closeBtn: 0
                            });
                        } else {// 编辑失败
                            layer.alert("编辑失败", {
                                shade: [0.1, "#fff"],
                                offset: scrollHeight + "px"
                            });
                        }

                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                        layer.close(layerLoad);
                        layer.alert("编辑失败", {
                            shade: [0.1, "#fff"],
                            offset: scrollHeight + "px"
                        });
                        return;
                    }
                });
            }
        }
    }, timeout);
}
/**
 * 验证价格调整
 * @returns
 */
function validatePriceTime() {

    delTimeList = new Array();
    var defaultObj = new Object();
    if (timeDefaultObj != null) {
        defaultObj = timeDefaultObj;
    }


    presaleTimesArray = new Array();
    var flag = true;
    var msg = "";
    var sStartTime = $("#sStartTime").val();//活动生效开始时间
    var sEndTime = $("#sEndTime").val();//活动结束时间
//	sStartTime = sStartTime.substr(0,10);
//	sEndTime = sEndTime.substr(0,10);
    var start = new Date(sStartTime.replace("-", "/").replace("-", "/"));
    var ends = new Date(sEndTime.replace("-", "/").replace("-", "/"));
    $(".p_div .timeDiv").each(function (i) {
        var startTime = $(this).find(".startTime").val();
        var endTime = $(this).find(".endTime").val();
        var types = $(this).find(".types:checked").val();
//		startTime = startTime.substr(0,10);
//		endTime = endTime.substr(0,10);
        var startDate = new Date(startTime.replace("-", "/").replace("-", "/"));
        var endDate = new Date(endTime.replace("-", "/").replace("-", "/"));
        var id = $(this).find(".tId").val();
        var priceType = $(this).find(".priceType option:selected").val();

        var price = $(this).find(".tPrice").val();
        var _obj = $(this);

        var isNull = false;

        if (startTime == null || startTime == "") {
            /*$(this).find(".startTime").css("border-color", "red");
             flag = false;
             msg = "价格调整开始时间不能为空";*/
            isNull = true;
        } else if (endTime == null || endTime == "") {
            /*$(this).find(".endTime").css("border-color", "red");
             flag = false;
             msg = "价格调整结束时间不能为空";*/
            isNull = true;
        } else if (startDate * 1 >= endDate * 1 && !isNull) {
            $(this).find(".startTime").css("border-color", "red");
            $(this).find(".endTime").css("border-color", "red");
            flag = false;
            msg = "价格调整活动开始时间必须要早于活动结束时间";
        }/*else if(startDate*1 < start*1){
         $(this).find(".startTime").css("border-color", "red");
         flag = false;
         msg = "价格调整开始时间不能早于预售活动开始时间";
         }*/ else if (endDate * 1 > ends * 1 && !isNull) {
            $(this).find(".endTime").css("border-color", "red");
            flag = false;
            msg = "价格调整结束时间不能晚于预售活动结束时间";
        } else {
            if (!isNull) {
                //判断价格是否重复
                $(".p_div .timeDiv").each(function (j) {
                    if (j < i) {
                        var startTime2 = $(this).find(".startTime").val();
                        var endTime2 = $(this).find(".endTime").val();
                        startTime2 = startTime2.substr(0, 10);
                        endTime2 = endTime2.substr(0, 10);

                        var startDate2 = new Date(startTime2.replace("-", "/").replace("-", "/"));
                        var endDate2 = new Date(endTime2.replace("-", "/").replace("-", "/"));

//						startDate=new Date(startTime.replace("-", "/").replace("-", "/"));  
//					    endDate=new Date(endTime.replace("-", "/").replace("-", "/")); 


                        /* if(startDate*1 < startDate2*1 || startDate*1 <  endDate2*1 ){
                         _obj.find(".startTime").css("border-color", "red");
                         flag = false;
                         }*/
                        if (endDate * 1 < startDate2 * 1 || endDate * 1 < endDate2 * 1) {
                            _obj.find(".endTime").css("border-color", "red");
                            flag = false;
                        }
                        if (!flag) {
                            msg = "价格调整的时间不能存在重叠";
                            return false;
                        }
                    }
                });
            }
        }

        //判断定金是否小于向下调整金额
        if (minPrice > 0 && !isNull) {
            var pPrice = $(this).find(".tPrice").val();

            minPriceVal = jisuanDepositPrice(pPrice, $(this).find("input"), types, minPrice);
            var depositPercent = $("#depositPercent").val();
            //console.log(minPriceVal+"----"+depositPercent)
            if (minPriceVal <= 0) {
                msg = "您调整的价格不能小于0，请重新输入要调整的价格";
                flag = false;
            } else if (minPriceVal <= depositPercent * 1) {
                msg = "定金价格不能大于等于商品最终价格";
                flag = false;
            }

        }
        if (!flag) {
            parentScrollTops(100);
            SonScrollTop(0);
            setTimeout(function () {
                layer.msg(msg, {
                    icon: 1,
                    offset: scrollHeight + "px",
                    shade: [0.1, '#fff']
                });
            }, timeout);
            return false;
        } else if (!isNull) {
            var obj = {
                startTime: startTime,
                endTime: endTime,
                saleType: types,
                price: price,
                priceType: priceType
            };
            if (id == null || $.trim(id) == "") {
                presaleTimesArray[presaleTimesArray.length] = obj;
            } else {

                delete defaultObj[id];

                obj.id = id;
                delTimeList[delTimeList.length] = obj;
            }
        }
    });
    if (defaultObj != null) {
        for (var str in defaultObj) {
            var obj = new Object();
            obj.id = str;
            obj.isDelete = 1;
            delTimeList[delTimeList.length] = obj;
        }
    }
    return flag;
}

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


/**
 *选择商品
 */
function choosePro() {
    var shopId = $(".shopId").find("option:selected").val();//店铺id
    var defaultProId = $("#defaultProId").val();
    if ((typeof defaultProId) == "undefined") {
        defaultProId = "";
    }
    SonScrollTop(0);
    setTimeout(function () {
        loadWindow();
        if (shopId != null && shopId != "") {
            // parentOpenIframe("选择商品", "600px", "480px", "/mGroupBuy/getProductByGroup.do?shopId=" + shopId + "&defaultProId=" + defaultProId);//check==0代表多选，check==1代表单选

            layer.open({
                type: 2,
                title: "选择商品",
                skin: 'layui-layer-rim', //加上边框
                area: ['600px', '480px'], //宽高
                offset: scrollHeight + "px",
                shade: [0.1, "#fff"],
                content: "/mGroupBuy/getProductByGroup.do?shopId=" + shopId + "&defaultProId=" + defaultProId
            });

        } else {
            layer.alert("请选择商品", {
                shade: [0.1, "#fff"],
                offset: scrollHeight + "px"
            });
        }
    }, timeout);
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
        $(".isSpec").val(obj.isSpe);
        $("span.proName").html(obj.title);
        $("span.proPrice,label.proPrice").html("￥" + obj.price);
        $("input.proPrice").val(obj.price);
        if (obj.price != null && obj.price != "") {
            obj.price = $.trim(obj.price);
        }
        $("#depositPercent").attr("maxPrice", obj.price);
        minPrice = obj.price;

        $(".invNum").val(obj.stockTotal);
        //console.log(obj.stockTotal)
        if (arr != null && obj.isSpe == 1) {
            proSpecArr = arr;

//			console.log(proSpecArr)

            eval(step.Creat_Table());// 初始化创建库存表格

            $("div.proPrices").hide();
        } else {
            $("#createTable").hide();
            $("div.proPrices").show();
        }
        eachPrice();
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
    var html = "/mPresale/index.do";
    var type = $(obj).find("option:selected").val();
    if (type != null && type != "") {
        html += "?type=" + type;
    }
    location.href = html;
}
$(".isDeposit").change(function () {
    if ($(this).is(":checked")) {
        $(".aucDepositDiv").show();
        $(".hidePrice").addClass("vali");
    } else {
        $(".aucDepositDiv").hide();
        $(".hidePrice").removeClass("vali");
    }
});
$("input.types").change(function () {
    var pObj = $(this).parents(".timeDiv");
    pObj.find("input.types").attr("checked", false);
    $(this).attr("checked", "checked");
    var type = $(this).val();
    if (type == 1) {
        pObj.find("label.type_label").html("上涨");
    } else {
        pObj.find("label.type_label").html("下调");
    }
    eachPrice();
});
function eachPrice() {
    var resultFlag = true;
    var proPrice = $("input.proPrice").val();
    var productId = $("input#productId").val();
    if (proPrice == null || proPrice == "" || productId == null || productId == "") {
        alert("请选择正确的商品");
        resultFlag = false;
    } else {

        $(".timeDiv").each(function () {
            var val = $(this).find("input.tPrice").val();
            var tests = $(this).attr("datatype");
            if (val != null && val != "") {
                tests = new RegExp(tests);
                if (tests.test(val)) {
                    var obj = $(this).find("input.tPrice");
                    var types = $(this).find("input.types:checked").val();
                    var flag = jisuanPrice(val, obj, types);
                    if (!flag) {
                        resultFlag = false;
                    }
                }
            }
        });
    }
    return resultFlag;
}
var depositPercent = "";
$("#depositPercent").focus(function () {
    depositPercent = $(this).val();
});
$("#depositPercent").blur(function () {
    var val = $(this).val();
    var tests = $(this).attr("datatype");
    if (val != null && val != "") {
        tests = new RegExp(tests);
        if (tests.test(val)) {
            var proPrice = $(".proPrice").val();
            if (proPrice != null && proPrice != "") {
                if (val * 1 >= proPrice * 1) {
                    if (proPrice * 1 - 1 > 0) {
                        $(this).val(proPrice - 1);
                    } else {
                        $(this).val("");
                    }
                }
            }
            flag = true;
        }
    }
    if (!flag) {
        $(this).val(depositPercent);
    }
});

var tPrice = "";
$(".tPrice").focus(function () {
    tPrice = $(this).val();
});
$(".tPrice").blur(function () {
    var proPrice = $("input.proPrice").val();
    var productId = $("input#productId").val();
    var val = $(this).val();
    var tests = $(this).attr("datatype");
    var flag = false;
    if (proPrice == null || proPrice == "" || productId == null || productId == "") {
        //alert("请选择正确的商品");
    }
    if (val != null && val != "") {
        tests = new RegExp(tests);
        if (tests.test(val)) {
            if (val * 1 > 0 && val * 1 <= 9999.99) {
                flag = true;
                var types = $(this).parents(".timeDiv").find("input.types:checked").val();
                jisuanPrice(val, $(this), types);
            }
        }
        if (!flag) {
            $(this).val(tPrice);
        }
    } else {
        $(this).val("");
    }
});
/**
 * 计算价格
 * @param pPrice 上涨下调幅度
 * @param obj
 * @returns
 */
function jisuanPrice(pPrice, obj, types) {
    var proPrice = $("input.proPrice").val();
    val = jisuanDepositPrice(pPrice, obj, types, proPrice);

    obj.parents(".groupDiv").find(".price_label").html("￥" + val);
    if (val < 0) {
        return false;
    }
    return true;
}
/**
 * 计算上涨下调的价格
 * @param pPrice
 * @param obj
 * @param types
 * @returns
 */
function jisuanDepositPrice(pPrice, obj, types, proPrice) {
    proPrice = proPrice * 1;
    var val = pPrice * 1
//	console.log("---------"+val)
    var priceTypes = $(obj).parents(".groupDiv").find("option:selected").val();
    if (types == 1) {
        if (priceTypes == 1) {//按百分比计算
            val = proPrice + (proPrice * (val / 100));
        } else {//按价格
            val = proPrice + val;
        }
    } else {
        if (priceTypes == 1) {//按百分比计算
            val = proPrice - (proPrice * (val / 100));
        } else {//按价格
            val = proPrice - val;
        }
    }
    val = Math.round(val * 100) / 100;
//	console.log("++++++++++++"+val)

    return val;
}
/**
 * 删除价格
 * @param obj
 * @returns
 */
function delP(obj) {
    $(obj).parents(".timeDiv").remove();
    loadWindow();
}
/**
 * 清空
 * @param obj
 * @returns
 */
function clearP(obj) {
    $(obj).parents(".timeDiv").find("input[type='text']").val("");
}
/**
 * 新增价格区间
 * @param obj
 * @returns
 */
function addP(obj) {
    var id = $(".p_div .timeDiv:last").attr("id");
    var index = (id * 1 + 1);
    var copy = $(".priceObj_div").clone(true);
    var startObj = copy.find(".startTime");
    startObj.attr("id", "startTime" + index)
    var endObj = copy.find(".endTime");
    endObj.attr("id", "endTime" + index)
    copy.find(".timeDiv").attr("id", index);
    copy.find("input").addClass("vali");

    startObj.attr("onclick", "laydate({elem: '#startTime" + index + "',min : laydate.now(),format: 'YYYY-MM-DD hh:mm:ss',istime : true,festival : true});");
    endObj.attr("onclick", "laydate({elem: '#endTime" + index + "',min : laydate.now(),format: 'YYYY-MM-DD hh:mm:ss',istime : true,festival : true});");


    $("div.p_div").append(copy.html());


    $(".p_div .timeDiv:last").find(".tPrice").focus(function () {
        tPrice = $(this).val();
    });
    $(".p_div .timeDiv:last").find(".tPrice").blur(function () {
        var proPrice = $("input.proPrice").val();
        var productId = $("input#productId").val();
        var val = $(this).val();
        var tests = $(this).attr("datatype");
        var flag = false;
        if (proPrice == null || proPrice == "" || productId == null || productId == "") {
            //alert("请选择正确的商品");
        }
        if (val != null && val != "") {
            tests = new RegExp(tests);
            if (tests.test(val)) {
                flag = true;
                var types = $(this).parents(".timeDiv").find("input.types:checked").val();
                jisuanPrice(val, $(this), types);
            }
        }
        if (!flag) {
            $(this).val(tPrice);
        }
    });

    $(".p_div .timeDiv:last").find("input.types").change(function () {
        var pObj = $(this).parents(".timeDiv");
        pObj.find("input.types").attr("checked", false);
        $(this).attr("checked", "checked");
        var type = $(this).val();
        if (type == 1) {
            pObj.find("label.type_label").html("上涨");
        } else {
            pObj.find("label.type_label").html("下调");
        }
        eachPrice();
    });


    //loadLaydateObj(index);

    loadWindow();
}
/**
 * 改变调整价格类型
 * @param obj
 * @returns
 */
function changePriceType(obj) {
    var val = $(obj).find("option:selected").val();
    var unit = "%";
    if (val == 2) {//按价格调整
        unit = "元";
    }
    var _parentObj = $(obj).parents("div.groupDiv");
    _parentObj.find(".unit").html(unit);

    eachPrice();
}