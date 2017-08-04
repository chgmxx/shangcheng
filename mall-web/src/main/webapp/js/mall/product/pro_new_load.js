﻿
$(".productGroup").each(function () {
    groups(this);
});
/**
 * 查询子分组信息
 * @param obj
 */
function findChildGroup(obj, index) {
    var bool = true;
    var groupPId = $(obj).find("option:selected").val();
    $(".productGroup").each(function (i) {
        if (i != index) {
            var gId = $(obj).find("option:selected").val();
            if (gId == groupPId) {
                bol = false;
                break;
            }
        }
    });
    if (bol) {
        groups(obj);
    } else {
        alert("您已经选择了，请重新选择")
    }
}

function groups(obj) {
    var shopId = $(".shop-contain option:selected").val();
    var groupPId = $(obj).find("option:selected").val();
    var params = {
        shopId: shopId,
        groupPId: groupPId
    };
    $.ajax({
        type: "post",
        url: "mPro/group/getGroupByShopId.do",
        data: params,
        dataType: "json",
        success: function (data) {
            var optionHtml = "";
            if (data.list != null) {
                for (var int = 0; int < data.list.length; int++) {
                    var group = data.list[int];
                    var id = group.id;
                    var shopId = group.shopId;
                    var groupName = group.groupName
                    optionHtml += "<option value='" + id
                        + "' shopStr='" + shopId + "'>" + groupName
                        + "</option>";
                }
            }
            if ($.trim(optionHtml) != "") {
                optionHtml = "<select class='childGroup' style='width: 125px;'>" + optionHtml + "</select>";
                $(obj).parent().find("span").html(optionHtml);
            } else {
                $(obj).parent().find("span").html("");
            }
        }
    });
}


function loadParams() {
    if ($('.controls .param-contain').length > 0) {
        $(".controls .param-contain").each(function () {
            paramsSelectChosen($(this))
        });
        $(".controls .param-value-contain").each(function () {
            paramsValueSelectChosen($(this));
        });
    } else {

        var cloneObj = $(".paramsDivs").clone(true);
        cloneObj.find("a.add").show();
        cloneObj.find("a.del").hide();
        cloneObj.find(".sku-param-title").attr("index", "0");

        var parentObj = $(".param-parent .controls");

        $(cloneObj.html()).insertBefore(parentObj.find(".proCodeVali"));


        var pObjs = parentObj.find(".sku-param-title:last")
        pObjs.find("div.chosen-container").remove();
        pObjs.css("margin-top", "0px");

        paramsSelectChosen(pObjs.find(".param-contain"));

        paramsValueSelectChosen(pObjs.find(".param-value-contain"));
    }

}
/**
 * 初始化参数名称
 * @param selectChosen
 */
function paramsSelectChosen(selectChosen) {
    selectChosen.chosen({
        width: 150,
        search_contains: true,
        display_selected_options: true,
        no_results_text: "没有找到匹配项",
        enable_split_word_search: false
    });
    var selectId = selectChosen.attr("nameId");
    selectChosen.change(function () {
        //$('.chosen').find('option:selected').removeAttr('selected');

        //delGroup();
        var parentObj = selectChosen.parents(".sku-param-title");
        var objs = parentObj.find(".param-value-contain");
        objs.removeAttr("disabled");
        objs.val("");
        objs.trigger('chosen:updated');

    });
    selectChosen.on("chosen:showing_dropdown", function (e, params) {
        var txtObj = $(this).parent().find("input[type='text']");
        txtObj.attr("maxlength", "10");
        txtObj.attr("placeholder", "最多输入10个字")
        getSpecifica(selectChosen, selectId, 2);
    });

    selectChosen.on('chosen:no_results', function (e, params) {
        var txtObj = $(this).parent().find("input[type='text']");
        // 规格名称输入框的值
        var str = txtObj.val();
        $(this).parent().find(".chosen-results").html(
            "<li class=\"active-result highlighted result-selected\">"
            + str + "</li>");

        var firstLi = $(this).parents("ul");
        var con = firstLi.find("li").html();
        firstLi.find("li").html(con + str);

        clickSpec($(this), 2);

    });

    var _parentObj = selectChosen.parent();
    _parentObj.find("div.chosen-container input").keyup(function () {
        var inp = $(this).val();
        if (inp != null && $.trim(inp) != "") {
            var li = $(this).parents(".chosen-container").find(".chosen-results li");
            var flag = false;
            li.each(function () {
                var text = $(this).text();
                if ($.trim(text) == inp) {
                    flag = true;
                }
            });
            if (!flag) {
                _parentObj.find(".chosen-results li").removeClass("highlighted");

                $("<li class=\"active-result highlighted result-selected\">"
                    + inp + "</li>").insertBefore(_parentObj.find(".chosen-results li:eq(0)"));
                clickSpec($(this), 2);
            }

        }
    });

//	selectChosen.html($(".specifaceSelects").html());
    //selectChosen.val(selectChosen.attr("nameId"));
    selectChosen.trigger("chosen:updated");


}
/**
 * 初始化参数值
 * @param selectChosen
 */
function paramsValueSelectChosen(selectChosen) {
    selectChosen.chosen({
        width: 150,
        search_contains: true,
        display_selected_options: true,
        no_results_text: "没有找到匹配项",
        enable_split_word_search: false,
    });
    var selectId = selectChosen.attr("nameId");
    if (selectId != null && selectId != "") {
        selectChosen.removeAttr("disabled");
    }
    selectChosen.change(function () {
        //$('.chosen').find('option:selected').removeAttr('selected');

        //delGroup();
        var val = $(this).find("option:selected").val();
        //console.log(val);
        var index = $(this).parent().attr("index");
        //console.log(index)

        $(".controls .param-value-contain").each(function (i) {
            var val2 = $(this).find("option:selected").val();
            //console.log(index+"__"+i+"__"+val+"__"+val2)
            if (index != i && val == val2) {
                alert("您不能重复选择参数值");
                selectChosen.val("");
                selectChosen.trigger("chosen:updated");
                selectChosen.click();
            }
        });

    });

    selectChosen.on("chosen:showing_dropdown", function (e, params) {
        var txtObj = $(this).parent().find("input[type='text']");
        txtObj.attr("maxlength", "10");
        txtObj.attr("placeholder", "最多输入10个字")

        var paramNameId = selectChosen.parents(".sku-param-title").find(".param-contain");
        var selectVal = paramNameId.find("option:selected").val();
        if (selectVal == null || selectVal == "" || typeof(selectVal) == "undefined") {
            selectVal = paramNameId.attr("nameId");
        }
        if (selectVal != null && selectVal != "") {
            getSpecificaValue(selectVal, selectChosen, "", 2);// 获取规格值
        } else {
            selectChosen.click();
        }
    });

    selectChosen.on('chosen:no_results', function (e, params) {
        var valueObj = $(this).parent().find(".param-value-contain").next();
        // 规格名称输入框的值
        var str = valueObj.find("input[type='text']").val();
        valueObj.find(".chosen-results").html(
            "<li class=\"active-result highlighted result-selected\">"
            + str + "</li>");

//		var firstLi = $(this).parents("ul");
//		console.log(firstLi.html())
//		var con = firstLi.find("li").html();
//		firstLi.find("li").html(con + str);

        var id = selectChosen.parents(".sku-param-title").find(".param-contain option:selected").val();
        // 自定义规格值
        clickParamValues(id, valueObj, 2);
    });

    var _parentObj = selectChosen.parent().find(".param-value-contain");
    _parentObj.next().find("input").keyup(function () {
        var _valueObj = _parentObj.next();
        var inp = $(this).val();
        if (inp != null && $.trim(inp) != "") {
            var li = $(this).parents(".chosen-container").find(".chosen-results li");
            var flag = false;
            li.each(function () {
                var text = $(this).text();
                if ($.trim(text) == inp) {
                    flag = true;
                }
            });
            if (!flag) {
                //console.log(_valueObj.html())
                _valueObj.find(".chosen-results li").removeClass("highlighted");

                $("<li class=\"active-result highlighted result-selected\">"
                    + inp + "</li>").insertBefore(_valueObj.find(".chosen-results li:eq(0)"));

                var id = selectChosen.parents(".sku-param-title").find(".param-contain option:selected").val();
                // 自定义规格值
                clickParamValues(id, _valueObj, 2);
            }

        }
    });

    //selectChosen.html($(".specifaceSelects").html());
    //selectChosen.val(selectChosen.attr("nameId"));
    selectChosen.trigger("chosen:updated");
}

/**
 * 点击参数值的下拉框
 *
 * @param obj
 */
function clickParamValues(id, obj, type) {
    var parents = obj.parents("div.sku-group-title");
    if (parents.length == 0) {
        parents = obj;
    }
    parentObj = parents.find(".chosen-results li").parents(".sku-param-title");
    valSelect = parentObj.find(".param-value-contain");
    parents.find(".chosen-results li").click(function () {
        var val = $(this).text();
        if (val != null && val != "") {
            // 自定义参数值
            addSpecificaValue(id, val, $(this), 2);
        }

    });
}
/**
 * 新增商品参数
 * @param obj
 */
function addParam(obj) {
    var cloneObj = $(".paramsDivs").clone(true);
    cloneObj.find("a.add").hide();
    cloneObj.find("a.del").show();
    cloneObj.find(".sku-param-title").attr("index", $(".controls .sku-param-title").length);

    var parentObj = $(obj).parents(".controls");
    $(cloneObj.html()).insertBefore(parentObj.find(".proCodeVali"));

    var pObjs = parentObj.find(".sku-param-title:last")
    pObjs.find("div.chosen-container").remove();

    paramsSelectChosen(pObjs.find(".param-contain"));

    paramsValueSelectChosen(pObjs.find(".param-value-contain"));

    loadWindow();
}
/**
 * 删除商品参数
 * @param obj
 */
function delParam(obj) {
    $(obj).parents(".sku-param-title").remove();
    $(".controls .sku-param-title").each(function (i) {
        $(this).attr("index", i)
    });
    loadWindow();
}

/**
 * 初始化分组信息
 */
function loadGroups() {
    $("textarea.groups").click(function () {
        var shopId = $(".shop-contain option:selected").val();
        var proId = $("input.proId").val();

        var groups = JSON.stringify(updGroupList);
        //console.log(groups)
        var group = "";
        if (groupSelectObj != null) {
            for (var key in groupSelectObj) {
                //console.log(key)
                if (group != "") {
                    group += ",";
                }
                group += key;
            }
        }
        //console.log(group)

        parent.openIframe("选择分组", "500px", "530px", "/mPro/group/getGroups.do?shopId=" + shopId + "&proId=" + proId + "&group=" + group)
    });
}

function returnVal(arr) {
    var groups = "";
    if (arr != null && arr.length > 0) {
        updGroupObj = new Object();
        groupSelectObj = new Object();
        for (var i = 0; i < arr.length; i++) {
            var obj = arr[i];
            if (groups != "") {
                groups += "、";
            }
            groups += obj.groupName;
            if (obj.childGroup != null) {
                //groups += "(";
                for (var j = 0; j < obj.childGroup.length; j++) {
                    var child = obj.childGroup[j];
                    /*if(j > 0){
                     groups += ","
                     }*/
                    if (groups != "") {
                        groups += "、";
                    }
                    groups += child.groupName;

                    groupSelectObj[child.groupId] = child.shopId;
                }
                //groups += ")";
            }
            groupSelectObj[obj.groupId] = obj.shopId;

            //updGroupList[updGroupList.length] = obj;
            updGroupObj[obj.groupId] = obj;
        }
    }
    //console.log(updGroupObj)
    if (groups != null && groups != "") {
        $("textarea.groups").val(groups);
    }
    parent.layer.closeAll();
}