﻿﻿
$(function () {
    loadLaydate();

    if ($('.group-contain').length > 0) {
        // 商品分组
        $('.group-contain').chosen({
            display_selected_options: false
        }).change(function () {
            $('.chosen').find('option:selected').removeAttr('selected');

            delGroup();
        });
        delGroup();
    }

    /* jqueryUI 拖拽初始化 */
    $("#ui-sortable").sortable();
    $("#ui-sortable").disableSelection();

    /* 预售商品 */
    $("input.isPresell").click(
        function () {
            $('.pre-sale-item').toggle();

            loadWindow();
        });

    /* 添加项目规格 */
    $("#add-spe-btn").click(function () {
        var len = $(".controls").find(".group-list").length;
        var obj = $(".nextGroup");
        if (len == 0) {
            var obj = $(".firstGroup");
        }
        obj.find("select.spec-contain").attr("index", len);
        if (len < 3) {
            $(obj.html()).insertBefore($(this).parent());
        }
        // 添加到第二个规格的时候隐藏添加规格的按钮
        if (len == 2) {
            $(this).css("display", "none");
        }
        var selectChosen = $(this).parent().prev().find("select.spec-contain");
        createSpecSelect(selectChosen);// 创建规格名称下拉框
        loadSpecImg();// 初始化规格图片
    });
    loadSpecifica();// 初始化规格名称

    eval(step.Creat_Table());// 初始化创建库存表格

    loadSpecificaValue();// 初始化规格值

    loadSpecImg();// 初始化规格图片

    loadShopSelect();// 初始化店铺下拉框

    loadParams();//初始化商品参数

    loadGroups();//初始化分组信息

    var proTypeId = $(".proTypeId:checked").val();
    var memberType = $(".memberType:checked").val();
    if (proTypeId != null && proTypeId != "") {
        /*if(memberType != null && memberType != ""){
         if(proTypeId == 2 && memberType > 0){
         $("input.proPrice").attr("disabled","disabled");
         }
         }*/
        var cardType = $(".cardType:checked").val();
        if (cardType != null && cardType != "") {
            if (proTypeId == 3 && cardType > 0) {
                $("input.proPrice").attr("disabled", "disabled");
            }
        }
    }
});
/**
 * 初始化店铺下拉框
 */
function loadShopSelect() {
    $(".shop-contain").change(
        function () {
            var shopId = $(this).find("option:selected").val();
            $(".groups").val("");
            $.ajax({
                type: "post",
                url: "mPro/group/getGroupByShopId.do",
                data: {
                    shopId: shopId
                },
                dataType: "json",
                success: function (data) {
                    if (data != null) {
                        if (data.list != null) {
                            var list = data.list;
                            var op = "";
                            groups = new Object();
                            for (var i = 0; i < list.length; i++) {
                                var group = list[i];
                                op += "<option value='" + group.id + "'>"
                                    + group.groupName + "</option>";
                                groups[group.id] = group.shopId;
                            }
                            var obj = $(".group-contain");
                            obj.html(op);
                            obj.val("");
                            obj.trigger("chosen:updated");

                        }
                    }

                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        });
}

/**
 * 初始化规格名称
 */
function loadSpecifica() {
    var specObj = $(".specification .group-list");
    if (specObj.length > 0) {
        specObj.each(function () {
            createSpecSelect($(this).find(".sku-group-title .spec-contain"));
        });
    }
}

/**
 * 创建规格名称下拉框
 *
 * @param obj
 */
function createSpecSelect(selectChosen) {

    selectChosen.chosen({
        width: 100,
        search_contains: true
    });
    // 规格名称下拉框打开事件
    selectChosen.on("chosen:showing_dropdown", function (e, params) {
        $(".add-sku").popover('hide');

        getSpecifica(selectChosen, null, 1);
    });
    selectChosen.on('change', function (e, params) {
        // console.log(selectChosen.val())
        selectChosen.parents(".sku-sub-group").find(
            ".sku-group-cont div:eq(0)").css("display",
            "inline-block");
        var selectId = selectChosen.val();
        var indexId = selectChosen.attr("index");
        var flag = true;

        $(".controls select.spec-contain").each(function () {
            var index = $(this).attr("index");
            if (selectId == $(this).val() && indexId != index) {
                alert("规格名不能相同");
                flag = false;
            }
        });
        if (!flag) {
            // console.log(selectChosen.val() + "----")
            selectChosen.val("");
            selectChosen.trigger("chosen:updated");
        }
        var parentObj = selectChosen.parents(".sku-sub-group");
        parentObj.find(".sku-atom").remove();
        // 初始化规格值
        loadSpecVal(selectChosen);

    }).on('chosen:no_results', function (e, params) {
        // 规格名称输入框的值
        var str = $(this).parent().find("input[type='text']").val();
        $(this).parent().find(".chosen-results").html(
            "<li class=\"active-result highlighted result-selected\">"
            + str + "</li>");

        var firstLi = $(this).parents("ul");
        var con = firstLi.find("li").html();
        firstLi.find("li").html(con + str);

        clickSpec($(this), 1);

        // 初始化规格值
        loadSpecVal(selectChosen);
    });
    var _parentObj = selectChosen.parent();
    _parentObj.find("input").keyup(function () {
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
                _parentObj.find(".chosen-results li").removeClass("highlighted");

                $("<li class=\"active-result highlighted result-selected\">"
                    + inp + "</li>").insertBefore(_parentObj.find(".chosen-results li:eq(0)"));


                clickSpec($(this), 1);
            }

        }
    });

    //getSpecifica(selectChosen, selectChosen.attr("nameId"),1);

    selectChosen.html($(".specifaceSelects").html());
    selectChosen.val(selectChosen.attr("nameId"));
    selectChosen.trigger("chosen:updated");
}
/**
 * 点击规格名称的下拉框
 *
 * @param obj
 */
function clickSpec(obj, type) {
    var parents = obj.parents("div.sku-group-title");
    if (parents.length == 0) {
        parents = obj.parents("div.sku-param-title");
    }

    parents.find(".chosen-results li").click(function () {
        var val = $(this).text();
        // 自定义规格名称
        addSpecifica(val, $(this), type);

    });
}

/**
 * 自定义规格名称
 */
function addSpecifica(text, obj, type) {

    var shopId = $(".shop-contain option:selected").val();

    if (shopId != null && $.trim(shopId) != "") {
        $.ajax({
            type: "post",
            data: {
                specName: text,
                shopId: shopId,
                type: type
            },
            url: "mPro/spec/addSpec.do",
            dataType: "json",
            success: function (data) {
                if (data != null) {
                    if (data.id != null) {
                        var parentObj = obj.parents(".sku-sub-group");
                        var valSelect = parentObj.find(".spec-contain");
                        if (type == 2) {
                            parentObj = obj.parents(".sku-param-title");
                            valSelect = parentObj.find(".param-contain");
                        }

                        valSelect.append("<option value='" + data.id + "'>"
                            + text + "</option>")
                        valSelect.val(data.id);
                        valSelect.trigger("chosen:updated");
                        valSelect.click();
                        if (type == 1) {
                            // 初始化规格值
                            parentObj.find(".sku-atom").remove();

                            parentObj.find(".sku-group-cont div:eq(0)").css(
                                "display", "inline-block");
                        } else if (type == 2) {
                            // 初始化参数
                            parentObj.find(".sku-atom").remove();

                            var objs = parentObj.find(".param-value-contain");
                            objs.removeAttr("disabled");
                            objs.trigger('chosen:updated');
                        }

                    }
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }

    loadWindow();

}
/**
 * 获取规格名称
 */
function getSpecifica(obj, defaultVal, type) {
    var selectVal = obj.val();
    var shopId = $(".shop-contain option:selected").val();
    // console.log(obj.html())
    if (shopId == null || shopId == "") {
        parent.layer.alert("请选择所属商铺", {
            offset: "30%",
            closeBtn: 0
        });
    }
    $.ajax({
        type: "post",
        url: "mPro/spec/getSpec.do",
        data: {
            type: type,
            shopId: shopId
        },
        dataType: "json",
        success: function (data) {
            var op = "";
            if (data != null) {
                if (data.map != null) {
                    var spec = data.map;
                    op = "<option value=''></option>";
                    $.each(spec, function (key, val) {
                        op += "<option value='" + key + "'>" + val
                            + "</option>";
                    });
                    if (defaultVal == null) {
                        defaultVal = selectVal;
                    } else {
                        obj.find("option[nameId='" + defaultVal + "']").attr("selected", "selected");
                    }


                }
            }
            obj.html(op);
            obj.val(selectVal);
            obj.trigger("chosen:updated");

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
/** **************** 商品规格值自定义添加，获取 ******************** */

/**
 * 获取规格值
 */
function getSpecificaValue(id, selectObj, defaultStr, type) {
    var shopId = $(".shop-contain option:selected").val();
    if (shopId == null || shopId == "") {
        parent.layer.alert("请选择所属商铺", {
            offset: "30%",
            closeBtn: 0
        });
    }
    var op = "";
    $.ajax({
        type: "post",
        data: {
            id: id,
            type: type,
            shopId: shopId
        },
        // async : false,
        url: "mPro/spec/getSpec.do",
        dataType: "json",
        success: function (data) {
            var op = "";
            if (data != null) {
                if (data.map != null) {
                    var spec = data.map;
                    op = "<option value=''></option>";
                    $.each(spec, function (key, val) {
                        op += "<option value='" + key + "'";
                        if (type == 2) {
                            var selectId = selectObj.attr("selectId");
                            if (key == selectId) {
                                op += " selected='selected'";
                            }
                        }
                        op += ">" + val + "</option>";
                    });
                }
            }
            selectObj.html(op);
            selectObj.val(defaultStr);
            selectObj.trigger("chosen:updated");

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
/**
 * 初始化规格值
 */
function loadSpecificaValue() {
    $(".controls .sku-group-title select").each(function () {
        loadSpecVal($(this));// 初始化规格值的添加事件
    });
}
/**
 * 绑定规格值的添加事件
 */
function loadSpecVal(selectChosen) {
    var selectObj = selectChosen.find("option:selected");
    var addSKu = selectChosen.parents(".sku-sub-group").find(".add-sku");
    /* 弹出框初始化 */
    addSKu.popover({
        html: true,
        title: function () {
            return $('#popover-head')
        },
        content: "<select class='chosen-container chosen-contain specValue-contain' multiple='multiple' style='min-width:300px' data-placeholder='添加规格值'>"
        + "</select>"
        + "<input type='button' class='btn' id='okPop' value='确定' onclick='addPop(this)'>"
        + "<input type='button' class='btn' id='closePop' value='取消' onclick='hidePop(this)' >",
    });
    addSKu.on('shown.bs.popover', function () {
        var valSelect = $(this).parents(".group-list").find(
            ".specValue-contain");
        var obj = $(this);
        /* 选择搜索框初始化 */
        valSelect.chosen({
            display_selected_options: false,
            search_contains: true,
            no_results_text: "没有找到匹配项",
            enable_split_word_search: false,
        }).on('chosen:no_results', function (e, params) {
            var str = $(this).parent().find("input[type='text']").val();
            $(this).parent().find("input[type='text']")
                .val(str);
            $(this).parent().find(".chosen-results").html(
                "<li class=\"active-result highlighted\">"
                + str + "</li>");

            var firstLi = $(this).parents("ul");
            var con = firstLi.find("li").html();
            firstLi.find("li").html(con + str);

            clickspecValue($(this));
        });
        var selectParent = valSelect.parent();
        selectParent.find(".chosen-choices").click(function (e) {
            selectParent.find("li.search-field input").focus();
        });
        selectParent.find("li.search-field input").keyup(function () {
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
                    valSelect.parent().find(".chosen-results li").removeClass("highlighted");
                    $("<li class=\"active-result highlighted\">"
                        + inp + "</li>").insertBefore(valSelect.parent().find(".chosen-results li:eq(0)"));
                    clickspecValue($(this));
                }

            }
        });
        var selectVal = selectChosen.find("option:selected").val();
        getSpecificaValue(selectVal, valSelect, "", 1);// 获取规格值

        // valSelect.html(op);
        // valSelect.val("");
        // valSelect.trigger("chosen:updated");

    });
}

/**
 * 选择规格值的下拉框
 */
function clickspecValue(obj) {
    var parents = obj.parents("div.popover");
    // $(".chosen-drop li").unbind("click");

    $(".chosen-drop li").click(
        function () {
            // console.log("&&&&&&&&&&")
            var flag = true;
            var val = $(this).text();
            var selObj = obj.parents(".sku-sub-group").find(
                "li.search-choice");
            // 移除已经存在的规格值
            selObj.each(function (i) {
                if (selObj.length > (i + 1)) {
                    var speVal = $(this).find("span:eq(0)").text();
                    // console.log($.trim(speVal) + "===" + $.trim(val))
                    if ($.trim(speVal) == $.trim(val)) {
                        flag = false;
                        return;
                    }
                }

            });
            if (!flag) {
                var len = selObj.length - 1;
                // 移除已经选中的规格值
                obj.parents(".sku-sub-group").find(
                    "li.search-choice:eq(" + len + ")").remove();
            } else {
                var id = obj.parents(".group-list").find("select").val();// 获取规格id
                // console.log(id)

                // 自定义规格值
                addSpecificaValue(id, $(this).text(), $(this), 1);

            }

        });
}
/**
 * 自定义规格值
 */
function addSpecificaValue(id, text, obj, type) {
    var op = "";
    $.ajax({
        type: "post",
        data: {
            specId: id,
            specValue: text,
            type: type
        },
        url: "mPro/spec/addSpec.do",
        dataType: "json",
        success: function (data) {
            if (data != null) {
                if (data.id != null) {
                    var parentObj = obj.parents(".popover-content");
                    var valSelect = parentObj.find(".specValue-contain");
                    if (type == 2) {
                        parentObj = obj.parents(".sku-param-title");
                        valSelect = parentObj.find(".param-value-contain");
                    }
                    valSelect.append("<option value='" + data.id + "'>" + text
                        + "</option>")
                    var selVal = valSelect.val();
                    if (selVal.length > 0) {
                        selVal[selVal.length] = data.id;
                    } else {
                        selVal = data.id;
                    }
                    valSelect.val(selVal);
                    valSelect.trigger("chosen:updated");

                    if (type == 2) {
                        valSelect.click();

                        //valSelect.next().find("input").focus();
                    } else {
                        parentObj.find("ul.chosen-choices input").focus();

                    }

                }
            }

        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
    return op;
}

/**
 * 删除规格值
 *
 * @param obj
 */
function delSpec(obj) {
    $(obj).parent().remove();
    eval(step.Creat_Table());
}

/**
 * 添加规格值
 *
 * @param obj
 */
function addPop(obj) {
    var specArrs = $(obj).parents(".group-list");
    var parentObj = $(obj).parent('.popover-content');
    var pSelObj = parentObj.find('select option:selected');

    if (pSelObj != null && pSelObj.length > 0) {
        pSelObj.each(function () {
            var val = $(this).text();
            if (val != null && $.trim(val) != "") {
                // console.log(val)
                var flag = true;
                specArrs.find(".sku-atom").each(function () {
                    var speVal = $(this).find("span:eq(0)").text();
                    if ($.trim(speVal) == val) {
                        parent.layer.msg("已经添加了相同的规格值");
                        flag = false;
                        return;
                    }
                });
                if (flag) {
                    var speId = $(this).val();

                    // 判断添加到第几个规格中
                    var index = specArrs.attr("index");
                    var speValObj = $(".addGroupDivNext");
                    if (index == "0") {
                        speValObj = $(".addGroupDivFirst");
                    }
                    speValObj.find(".groupName").html(val);
                    if (speId != null && $.trim(speId) != "") {
                        speValObj.find(".groupName").attr("id", speId);
                    }
                    if ($("#addPicture").is(":checked")) {
                        speValObj.find(".upload-img-wrap").show();//显示上传图片的按钮
                    } else {
                        speValObj.find(".upload-img-wrap").hide();//隐藏上传图片的按钮
                    }

                    specArrs.find(".sku-atom-list").append(speValObj.html());
                }
            }
        });
        specArrs.find(".js-sku-atom-list").css("display", "inline-block");

        eval(step.Creat_Table());
    }
    specArrs.find(".add-sku").popover('hide');

    // eachSpec();

}
/**
 * 加载规格图片
 */
function loadSpecImg() {
    var check = $("input[name='addPicture']");
    if (check.is(":checked")) {
        check.parents(".group-list").find(".sku-atom").addClass("active");
        $(".addGroupDivFirst .sku-atom").addClass("active");
    } else {
        check.parents(".group-list").find(".sku-atom").removeClass("active");
        $(".addGroupDivFirst .sku-atom").removeClass("active");
    }
    $("input[name='addPicture']").unbind();
    /* 添加规格图片 */
    $("input[name='addPicture']").change(
        function () {
            // console.log($(".controls #addPicture").is(':checked'));
            var sku = $(this).parents(".group-list").find(".sku-atom");
            $(".sku-group-warning").toggle();
            $('.upload-img-wrap').toggle();
            if ($(this).is(":checked")) {
                sku.addClass("active");
                $(".addGroupDivFirst .sku-atom").addClass("active");
            } else {
                sku.removeClass("active");
                $(".addGroupDivFirst .sku-atom").removeClass("active");
            }
            loadWindow();
        });

    $(".upload-img-wrap").click(function () {
        parent.materiallayer();
    });

}
/**
 * 初始化日期
 */
function loadLaydate() {
    // 初始化预售结束时间
    var datebox_1 = {
        elem: '#pre-end-time',
        event: 'focus',
        festival: true,
        format: 'YYYY-MM-DD hh:mm:ss', // 日期格式
        istime: true, // 是否开启时间选择
        min: laydate.now(),
        choose: function (datas) {
            $('#pre-end-time').next().html("");
        }
    };
    // 初始化预计发货开始时间
    var datebox_2 = {
        elem: '#sale-start-time',
        event: 'focus',
        festival: true,
        min: laydate.now(),
        choose: function (datas) {
            datebox_3.min = datas; // 开始日选好后，重置结束日的最小日期
            datebox_3.start = datas; // 将结束日的初始值设定为开始日
            $('#sale-start-time').parent().find("p.red").html("");
        }
    }
    // 初始化预计发结束时间
    var datebox_3 = {
        elem: '#sale-end-time',
        event: 'focus',
        festival: true,
        min: laydate.now(),
        choose: function (datas) {
            datebox_2.max = datas;
            $('#sale-end-time').parent().find("p.red").html("");
        }
    }
    laydate(datebox_1);
    laydate(datebox_2);
    laydate(datebox_3);

}
/**
 * 删除图片
 *
 * @param obj
 */
function delImg(obj) {
    $(obj).parents('.delParent').remove();

    // var imageId = $(obj).parent().find(".imageId").val();
    // if (imageId != null && $.trim(imageId) != "") {
    // var imgObj = new Object();
    // imgObj.id = imageId;
    // imgObj.isDelete = 1;
    // delimageList[delimageList.length] = imgObj;
    // }

    if ($(".app-image-list li").length < 5) {
        $(".js-add-picture").parent().show();
    }

}
/**
 * 删除规格
 *
 * @param obj
 */
function del(obj) {
    $(obj).parents(".group-list").remove();
    eval(step.Creat_Table());

    var len = $(".controls .group-list").length;
    if (len < 3) {
        $(".add-project-spe #add-spe-btn").show();
        $(".add-project-spe #add-spe-btn").parent().show();
    } else {
        $(".add-project-spe #add-spe-btn").hide();
    }
    if (len == 0) {
        $("input#proStockTotal").removeAttr("disabled");

        var iframe = document.getElementById("oneIframe").contentWindow;
        var pPrice = iframe.document.getElementById("pPrice");
        $(pPrice).html($(".proPrice").val());
    }

}

function hidePop(obj) {
    $(obj).parents(".sku-group-cont").find('.add-sku').click();
}

function showPop() {
    $(this).next().show();
}
/**
 * 批量修改价格
 */
$(".js-batch-price").click(function () {
    $(".js-batch-form").show();// 显示批量修改框
    $(".js-batch-stock").hide();// 隐藏修改库存按钮
    $(".js-batch-txt").attr("type", "price");
    $(".js-batch-txt").attr("vali", "^[0-9]{1}\\d{0,5}(\\.\\d{1,2})?$");
    $(".js-batch-txt").val("");
});
/**
 * 批量修改库存
 */
$(".js-batch-stock").click(function () {
    $(".js-batch-form").show();// 显示批量修改框
    $(".js-batch-price").hide();// 隐藏修改价格按钮
    $(".js-batch-txt").attr("type", "stock");
    $(".js-batch-txt").attr("vali", "^\\d{1,6}$");
    $(".js-batch-txt").val("");
});
/**
 * 关闭批量修改价格或库存
 */
$(".js-batch-cancel").click(function () {
    $(".js-batch-form").hide();// 隐藏修改框
    $(".js-batch-stock").show();// 显示批量修改库存按钮
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
        var msg = $(".js-batch-stock").attr("errormsg");
        if (inp.attr("type") == "price") {
            msg = $(".js-batch-price").attr("errormsg");
        }
        layer.tips(msg, $(".js-batch-save"), {
            tips: [3, '#3595CC']
        });
    } else {
        $(".js-batch-form").hide();// 隐藏修改框
        $(".js-batch-stock").show();// 显示批量修改库存按钮
        $(".js-batch-price").show();// 显示批量修改价格按钮
        if (inp.attr("type") == "price") {
            if (inp.val() * 1 <= 0) {
                msg = $(".js-batch-price").attr("errormsg");
                layer.tips(msg, $(".js-batch-save"), {
                    tips: [3, '#3595CC']
                });
            } else {
                $(".js-price").val(inp.val());
                $(".js-price").next().hide();
            }
        } else {
            var totalNum = inp.val() * 1 * $(".js-stock-num").length;
            $(".js-stock-num").val(inp.val());
            $(".js-stock-num").next().hide();
            $("#proStockTotal").val(totalNum);// 总库存
        }
        eval(step.saveInp());
    }
});

function delGroup() {
    /**
     * 绑定点击删除按钮事件
     */
    $(".group-contain").parents(".controls").find('.search-choice-close')
        .click(function () {
            var index = $(this).attr("data-option-array-index");
            var optionSel = $('.group-contain option:eq(' + index + ')');
            var groupId = optionSel.val();
            var shopId = optionSel.attr("shopstr");
            if (groupShopObj != null) {
                var id = groupShopObj[groupId + "_" + shopId];
                var obj = {
                    id: id,
                    isDelete: 1
                };
                // delGroupList[delGroupList.length] = obj;
            }
            // console.log(delGroupList)
        });

}
/**
 * 上传图片回调函数
 *
 * @param id
 * @param url
 */
function fhmateriallayer(imageArray, url) {
    // console.log(JSON.stringify(imageArray) + "===" + url)
    var imageSize = $(".picture-list li").length;
    var html = "";
    var preHtml = "";
    if (imgType == 0) {// 上传商品图片
        if (imageArray != null && imageArray.length > 0) {
            for (var i = 0; i < imageArray.length; i++) {
                var imageUrl = imageArray[i].url;
                var url = imageUrl.split("/upload/")[1];
                html += "<li class='sort delParent'>";
                html += "<img src='"
                    + (imgResource + url)
                    + "'/><a class='js-delete-picture close-modal small hide' onclick='delImg(this)'>×</a>";
                html += "<input type='hidden' class='imageInp' value='"
                    + url
                    + "' /><input type='hidden' class='imageId' value='' />";
                html += "</li>";

                preHtml += "<div class=\"swiper-slide\"><img src=\""
                    + (imgResource + url) + "\" /></div>";
            }
        }
        var obj = $(".add-goods").parent();
        $(html).insertBefore(obj);
        $("#ui-sortable").sortable();
        $("#ui-sortable").disableSelection();

        // 预览
        var iframe = document.getElementById("oneIframe").contentWindow;
        var wrapper = iframe.document.getElementById("wrapper");
        // console.log( $(wrapper).html());
        if (preHtml != null && preHtml != "") {
            if (imageSize == 0) {
                $(wrapper).html("");
            }
            $(wrapper).append(preHtml);
            var imageSize = $(wrapper).find(".swiper-slide").length;
            $(wrapper).parents(".main").find(".imgSize").html(imageSize);
        }

    } else {// 上传商品规格图片
        // console.log(imgObj.html())
        var imgUrl = url.split("/upload/")[1];
        html = "<img src='" + (imgResource + imgUrl) + "' />"
            + "<input type='hidden' class='imageInp' value='" + imgUrl
            + "'/>";
        imgObj.html(html);
    }

    loadWindow();
}
var imgType = 0;
var imgObj;

// 弹出素材库
function materiallayer(type, obj) {
    imgType = type;
    imgObj = $(obj);
    var url = "/common/material.do";
    if (type == 0) {
        url += "?selectType=checked";
    }
    parent.layer.open({
        type: 2,
        title: '素材库',
        shadeClose: true,
        shade: 0.2,
        area: ['820px', '500px'],
        offset: "10px",
        content: url,
    });
}
// 素材库里面返回信息
function image(imageArray, url) {
    parent.layer.closeAll();
    $("#main")[0].contentWindow.fhmateriallayer(id, url); // 父类调用子类的方法
}
/**
 * 刷新商品分组
 */
$("a.refresh-tag").click(
    function () {
        // 获取商品分组原本选种值
        var chosenIdArr = $(".group-contain").val();

        var shopId = $(".shop-contain option:selected").val();
        $.ajax({
            type: "post",
            url: "mPro/group/getGroupByShopId.do",
            data: {
                shopId: shopId
            },
            dataType: "json",
            success: function (data) {
                if (data.list != null) {
                    var optionHtml = "";
                    groups = new Object();
                    for (var int = 0; int < data.list.length; int++) {
                        var group = data.list[int];
                        var id = group.id;
                        var shopId = group.shopId;
                        var groupName = group.groupName
                        groups[id] = shopId;

                        optionHtml += "<option value='" + id
                            + "' shopStr='" + shopId + "'>" + groupName
                            + "</option>";

                    }
                    if ($.trim(optionHtml) != "") {
                        var chosenObj = $(".group-contain");
                        chosenObj.html(optionHtml); // 给分组下拉框重新赋值
                        chosenObj.val(chosenIdArr);// 给分组下拉框设置选种值
                        chosenObj.find("option[selected='selected']").attr(
                            'selected', false);// 取消选中
                        chosenObj.trigger("chosen:updated");
                    }

                }

            }
        });
    });
var stockErrorMsg = $("input#proStockTotal").attr("errormsg");
var proId = $(".proId").val();
/**
 * 商品类型
 */
$(".proTypeId").click(function () {
    var val = $(this).val();
    if ($.trim(val) == 0) {
        //$(".pre-sale-warp").show();
    } else {
        $(".pre-sale-warp").hide();
    }
    if ($(".proTypeDiv").length > 0) {
        $(".proTypeDiv").hide();
        $(".flow_help").hide();
        $(".FlowDiv").hide();
        $("input.proPrice").removeAttr("disabled");
        $("input#proStockTotal").removeAttr("max_num");
        if ($.trim(val) == 2) {
            $(".memberTypeDiv").show();
            //$("input.proPrice").attr("disabled","disabled");
            var moneys = $(".memberType:checked").attr("jq");
            if (moneys != null && moneys != "") {
                $("input.proPrice").val(moneys);
            }
        } else if ($.trim(val) == 3) {
            $(".cardTypeDiv").show();
            $("input.proPrice").attr("disabled", "disabled");
            var moneys = $(".cardType:checked").attr("jq");
            if (moneys != null && moneys != "") {
                $("input.proPrice").val(moneys);
            }
        } else if ($.trim(val) == 4) {
            $(".FlowDiv").show();
            /*$("input.proPrice").attr("disabled","disabled");*/
            var count = $(".flowId:checked").attr("count");
            if (count != null && count != "" && proId == "") {
                $("input#proStockTotal").attr("max_num", count);
                $(".flow_help").show();
                $(".flow_help em").html(count);
            }
        } else {
            /*$("input.proPrice").removeAttr("disabled");*/
        }
    }

    loadWindow();
});
$(".flowId").change(function () {
    var count = $(this).attr("count");
    if (count != null && count != "" && proId == "") {
        $("input#proStockTotal").attr("max_num", count);
        $(".flow_help").show();
        $(".flow_help em").html(count);
    }
});
$(".memberType").click(function () {
    var money = $(this).attr("jq");
    if (money != null && money != "") {
        //$("input.proPrice").attr("disabled","disabled");
        $("input.proPrice").val(money);
    } else {
        //$("input.proPrice").removeAttr("disabled");
        $("input.proPrice").val("");
    }
});
$(".cardType").click(function () {
    var money = $(this).attr("jq");
    if (money != null && money != "") {
        $("input.proPrice").attr("disabled", "disabled");
        $("input.proPrice").val(money);
    } else {
        $("input.proPrice").removeAttr("disabled");
        $("input.proPrice").val("");
    }
    if (cardMessage != null) {
        var id = $(this).val();
        var message = cardMessage[id];
        if ($(".cardTab").length > 0) {
            var html = "";
            if (message.length > 0) {
                for (var i = 0; i < message.length; i++) {
                    html += "<tr><td>" + message[i].cardName + "</td><td>" + message[i].num + "</td></tr>";
                }
            }
            $(".cardTab tbody").html(html);
        }
    }
});
$(".isIntegralChangePro").change(function () {
    var isCheck = $(this).is(":checked");
    if (isCheck) {
        $(".integralDiv").show();
    } else {
        $(".integralDiv").hide();
    }
    loadWindow();
});
$(".isShowViews").change(function () {
    var isCheck = $(this).is(":checked");
    if (isCheck) {
        $(".viewDiv").show();
    } else {
        $(".viewDiv").hide();
    }
    loadWindow();
});
$(".isFenbiChangePro").change(function () {
    var isCheck = $(this).is(":checked");
    if (isCheck) {
        $(".fenbiDiv").show();
    } else {
        $(".fenbiDiv").hide();
    }
    loadWindow();
});