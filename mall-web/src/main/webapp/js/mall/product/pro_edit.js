var invenArray = new Array();
var specificaArray = new Array();
var params = new Object();
var shopId = 0;
var productObj = new Object();
var paramsArray = new Array();

/**
 * 下一步
 */
$(".subBtn").click(function () {
    SonScrollTop(0);
    var flag = true;
    // 验证信息
    productObj = $("#productForm").serializeObject();
    shopId = $(".shop-contain option:selected").val();
    if (shopId == null || $.trim(shopId) == "") {
        parentScrollTops(100);
        SonScrollTop(0);
        setTimeout(function () {
            layer.alert("请选择所属商铺", {
                offset: scrollHeight + "px",
                shade: [0.1, "#fff"],
                closeBtn: 0
            });
        }, timeout);
        return;
    }
    productObj.shopId = shopId;

    var noUpInvNum = 0;//是否能修改库存 1不能修改 0能修改
    if ($("input.noUpInvNum").length > 0) {
        noUpInvNum = $("input.noUpInvNum").val();
    }
    if (noUpInvNum == 1) {
        delete productObj.proStockTotal;
    }

    //var groupList = eachGroup();// 遍历产品分组
    var groupList = eachProGroup();// 遍历产品分组
    if (groupList.length == 0 && updGroupList.length == 0 && flag) {
        parentScrollTops(400);
        SonScrollTop(0);
        setTimeout(function () {
            layer.alert("请选择商品分组", {
                shade: [0.1, "#fff"],
                offset: scrollHeight + "px",
                closeBtn: 0
            });
        }, timeout);
        /*$(window.parent).scrollTop(400);*/
        $('.group-contain input').focus();
        flag = false;
    }
    //return false;
    var proTypeId = $(".proTypeId:checked").val();
    productObj.proTypeId = proTypeId;
    if (proTypeId == 2) {
        var checkId = $(".memberType:checked").val();
        if (checkId == null || checkId == "") {
            alert("请选择会员卡类型");
            return false;
        } else {
            productObj.proPrice = $(".memberType:checked").attr("jq");
        }
        productObj.memberType = checkId;
    } else if (proTypeId == 3) {
        var checkId = $(".cardType:checked").val();
        if (checkId == null || checkId == "") {
            alert("请选择卡券包");
            return false;
        } else {
            //productObj.proPrice = $(".memberType:checked").attr("jq");
        }
        productObj.cardType = checkId;
    } else if (proTypeId == 4) {
        var checkId = $(".flowId:checked").val();
        if (checkId == null || checkId == "") {
            alert("请选择流量包");
            return false;
        } else {
            //productObj.proPrice = $(".memberType:checked").attr("jq");
        }
        productObj.flowId = checkId;
    }
    if (proTypeId != 2) {
        productObj.memberType = 0;
    }
    if (proTypeId != 3) {
        productObj.cardType = 0;
    }
    if (proTypeId != 4) {
        delete productObj.flowId;
        delete productObj.flowRecordId;
    }

    //预售商品是否选中
    if ($("input.isPresell").is(':checked') == true && flag) {
        //预售结束时间不能为空
        var time = $(".proPresellEnd");
        if (time.val() == null || $.trim(time.val()) == "") {
            time.next().html("请选择预售结束时间");
            time.css("border-color", "red");
            /*$(window.parent).scrollTop(450);*/
            parentScrollTops(450);
            flag = false;
        }
        //预售发货开始时间不能为空
        var starttime = $(".proDeliveryStart");
        if (starttime.val() == null || $.trim(starttime.val()) == "") {
            starttime.parent().find("p.red").html("请选择预计发货时间");
            starttime.css("border-color", "red");
            /*$(window.parent).scrollTop(450);*/
            parentScrollTops(450);
            flag = false;
        }
//		//预售发货结束时间不能为空
//		var endtime = $(".proDeliveryEnd");
//		if(endtime.val() == null || $.trim(endtime.val()) == ""){
//			endtime.parent().find("p.red").html("请选择预计发货时间");
//			endtime.css("border-color", "red");
//			$(window.parent).scrollTop(450); 
//			flag = false;
//		}
    }
    if (flag) {
        tip = eachSpecifica();// 遍历规格
        if (tip != null) {
            flag = false;
            parentScrollTops(400);
            SonScrollTop(0);
            setTimeout(function () {
                layer.alert(tip, {
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    closeBtn: 0
                });
            }, timeout);
        } else {
            flag = eachInven();// 遍历库存
            if (!flag) {
                parentScrollTops(400);
                SonScrollTop(0);
                setTimeout(function () {
                    layer.alert("请完善商品库存", {
                        offset: scrollHeight + "px",
                        shade: [0.1, "#fff"],
                        closeBtn: 0
                    });
                }, timeout);
               /* $(window.parent).scrollTop(400);*/
            }

        }
        tip = eachParams();//遍历参数
        if (tip != null && tip != "") {
            flag = false;
            parentScrollTops(400);
            SonScrollTop(0);
            setTimeout(function () {
                layer.alert(tip, {
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    closeBtn: 0
                });
            }, timeout);
           /* $(window.parent).scrollTop(400);*/
        }

        var isSpecifica = 0;
        if ($(".controls .group-list").length > 0) {
            isSpecifica = 1;
        }
        productObj.isSpecifica = isSpecifica;

        var imageObj = eachImage();// 获取图片
        var imageList = imageObj.imageList;// 添加的图片
        if (delimageList == null || delimageList.length == 0) {
            delimageList = imageObj.delimageList;// 删除的图片
        } else {
            delimageList.concat(imageObj.delimageList);
        }

        if (imageList.length == 0 && delimageList.length == 0 && flag) {
            setTimeout(function () {
                layer.alert("商品图至少上传一张", {
                    shade: [0.1, "#fff"],
                    offset: scrollHeight + "px",
                    closeBtn: 0
                });
            }, timeout);
            $(".imageVali").html("商品图至少上传一张");
            flag = false;
        }
        /*var isIntegralChangePro = $(".isIntegralChangePro").is(":checked");
         if(isIntegralChangePro){
         var changeIntegral = $(".changeIntegral").val();
         if(changeIntegral == null || changeIntegral == ""){
         parent.layer.alert("请填写兑换积分值", {
         offset : "30%",
         closeBtn : 0
         });
         flag = false;
         }
         }else{
         productObj.changeIntegral = 0;
         }*/
        var isFenbiChangePro = $(".isFenbiChangePro").is(":checked");
        if (isFenbiChangePro) {
            var changeFenbi = $(".changeFenbi").val();
            if (changeFenbi == null || changeFenbi == "") {
                setTimeout(function () {
                    layer.alert("请填写兑换粉币值", {
                        offset: scrollHeight + "px",
                        shade: [0.1, "#fff"],
                        closeBtn: 0
                    });
                }, timeout);
                flag = false;
            }
        } else {
            productObj.changeFenbi = 0;
        }
        var isShowViews = $(".isShowViews").is(":checked");
        if (isShowViews) {
            var viewsNum = $(".viewsNum").val();
            if (viewsNum == null || viewsNum == "") {
                setTimeout(function () {
                    layer.alert("请填写浏览量", {
                        offset: scrollHeight + "px",
                        shade: [0.1, "#fff"],
                        closeBtn: 0
                    });
                }, timeout);
                flag = false;
            }
        } else {
            productObj.viewsNum = 0;
        }
        /*var isReturn = $(".isReturn").is(":checked");
         if(isReturn){
         productObj.isReturn = 1; 
         }else{
         productObj.isReturn = 0;
         }*/
        if (flag) {
            if (!validateForm()) {
                setTimeout(function () {
                    layer.alert("请完善商品信息", {
                        offset: scrollHeight + "px",
                        shade: [0.1, "#fff"],
                        closeBtn: 0
                    });
                }, timeout);
            } else {
                params = {
                    imageList: JSON.stringify(imageList),
                    groupList: JSON.stringify(groupList)
                };
                if (delimageList != null && delimageList.length > 0) {
                    params.delimageList = JSON.stringify(delimageList);
                }
                if (updGroupList != null && updGroupList.length > 0) {
                    params.updGroupList = JSON.stringify(updGroupList);
                }
                if (specificaArray != null && specificaArray.length > 0) {
                    params.speList = JSON.stringify(specificaArray);
                }
                if (invenArray != null && invenArray.length > 0) {
                    params.invenList = JSON.stringify(invenArray);
                }
                if (inveDefaultObj != null && inveDefaultObj.length > 0) {// 库存与原本的数据
                    params.inveDefaultObj = JSON.stringify(inveDefaultObj);
                }
                if (specDefaultObj != null && specDefaultObj.length > 0) {// 规格原本的数据
                    params.specDefaultObj = JSON.stringify(specDefaultObj);
                }
                if (paramsArray != null && paramsArray.length > 0) {
                    params.paramsList = JSON.stringify(paramsArray);
                }
                if (paramDefaultObj != null) {// 参数原本的数据
                    params.paramDefaultObj = JSON.stringify(paramDefaultObj);
                }
                var isInvoice = $("input[name='isInvoice']:checked").val();
                productObj.isInvoice = isInvoice;
                var isWarranty = $("input[name='isWarranty']:checked").val();
                productObj.isWarranty = isWarranty;

                $(".onePro").hide();
                $(".twoPro").show();

                $(window.parent).scrollTop(270);

                $(".twoDl").removeClass("doing");
                $(".twoDl").addClass("done");
                $(".threeDl").removeClass("last");
                $(".threeDl").addClass("doing");
            }
        }

        loadWindow();
    } else {
        loadWindow();
    }
});
/**
 * 上一步
 */
$(".prePage").click(function () {
    $(".onePro").show();
    $(".twoPro").hide();

    $(".twoDl").addClass("doing");
    $(".twoDl").removeClass("done");
    $(".threeDl").addClass("last");
    $(".threeDl").removeClass("doing");

    $(window.parent).scrollTop(270);
    loadWindow();
});
/**
 * 保存并预览
 */
$(".preview").click(function () {
    saveUpd(0);
});
/**
 * 保存
 */
$(".savePage").click(function () {
    saveUpd(1);
});
/**
 * 保存并送审
 */
$(".saveStatusPage").click(function () {
    SonScrollTop(0);
    setTimeout(function () {
        layer.confirm('保存并送审后商品不允许修改，您确定要保存并送审商品？', {
            btn: ['确定', '取消'],
            shade: [0.1, '#fff'],
            offset: scrollHeight + "px"
        }, function () {
            layer.closeAll();
            saveUpd(2);
        });
    }, timeout);
});
/**
 * 保存信息
 * @param type
 */
function saveUpd(type) {
    var productDetail = editor.html();
    var detail = new Object();
    var detailId = $(".detailId").val();
    if (detailId != null && detailId != "") {
        detail.id = detailId;
    }
    if (productDetail != null) {
        detail.productDetail = productDetail;
    }
    var productIntrodu = $(".productIntrodu").val();
    if (productIntrodu != null) {
        if (productIntrodu.length > 150) {
            alert("商品简介的文字长度不能超过150");
            return false;
        }
        detail.productIntrodu = productIntrodu;
    }
    var productMessage = $(".productMessage").val();
    if (productMessage != null && productMessage != "") {
        if (productMessage.length > 200) {
            alert("商品信息的文字长度不能超过200");
            return false;
        }
        detail.productMessage = productMessage;
    }
    params.detail = JSON.stringify(detail);
    if (productObj != null) {
        if (type == 2) {
            productObj.checkStatus = 0;
        } else {
            productObj.checkStatus = -2;
        }
        params.product = JSON.stringify(productObj);
    }

    /*console.log(JSON.stringify(params))*/
    // loading层
    var layerLoad = layer.load(1, {
        shade: [0.3, '#fff'],
        offset: "10%"
    });
    var proId = $(".proId").val();
    var url = "mPro/add_pro.do";
    if (proId != null && $.trim(proId) != "") {
        url = "mPro/upd_pro.do";
    }
    $.ajax({
        type: "post",
        url: url,
        data: params,
        dataType: "json",
        timeout: 60000 * 60,//一小时的超时时间
        success: function (data) {
            layer.closeAll();
            SonScrollTop(0);
            setTimeout(function () {
                if (data.code == 0) {// 重新登录

                    layer.alert("操作失败，长时间没操作，跳转到登录页面", {
                        offset: scrollHeight + "px",
                        shade: [0.1, "#fff"],
                        closeBtn: 0
                    }, function (index) {
                        location.href = "/user/tologin.do";
                    });

                } else if (data.code == 1) {

                    var tip = layer.alert("编辑成功", {
                        offset: scrollHeight + "px",
                        shade: [0.1, "#fff"],
                        closeBtn: 0
                    }, function (index) {
                        layer.close(tip);
                        if (type == 0) {
                            var pId = data.id;
                            window.location.href = "mallPage/" + pId + "/" + shopId + "/79B4DE7C/phoneProduct.do";
                        } else {
                            var urls = $("input.urls").val();
                            if (urls == null || urls == "") {
                                location.href = "/mPro/index.do";
                            } else {
                                location.href = urls;
                            }
                        }

                    });
                } else {// 编辑失败
                    var msg = "编辑失败";
                    if (data.msg != "") {
                        msg = data.msg;
                    }
                    SonScrollTop(0);
                    setTimeout(function () {
                        layer.alert(msg, {
                            shade: [0.1, "#fff"],
                            offset: scrollHeight + "px"
                        });
                    }, timeout);
                }
            }, timeout);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            SonScrollTop(0);
            setTimeout(function () {
                layer.alert("编辑失败", {
                    shade: [0.1, "#fff"],
                    offset: scrollHeight + "px"
                });
                return;
            }, timeout);
        }
    });
//	parent.layer.closeAll();
}

var specNameObj = new Object();
var specValuObj = new Object();

/**
 * 遍历规格
 */
function eachSpecifica() {
    specificaArray = new Array();
    var tip = null;
    var sort = 0;
    // 遍历规格名称
    $(".controls .group-list").each(function () {
        var selObj = $(this).find(".sku-group-title select option:selected");
        var id = selObj.val();// 规格名id
        var val = selObj.text();// 规格名

        if (id != null && val != null) {
            specNameObj[val] = id;

            if ($(this).find(".sku-atom").length > 0) {
                // 遍历规格值
                $(this).find(".sku-atom").each(function () {
                    var vId = $(this).find("span").attr("id");// 规格值id
                    var vVal = $(this).find("span").text();// 规格值
                    var specObj = new Object();
                    specObj.specificaNameId = id;
                    specObj.specificaName = val;
                    specObj.specificaValueId = vId;
                    specObj.specificaValue = vVal;
                    specObj.sort = sort;
                    specValuObj[vVal] = vId;
                    sort++;

//					console.log($("#addPicture").is(':checked'))
                    // 规格图片
                    var check = $(this).parents(".sku-sub-group").find("#addPicture");
                    if (check.is(':checked') == true) {
                        var imgUrl = $(this).find(".add-image input").val();
                        if ($.trim(imgUrl) != "" && $.trim(imgUrl) != "+") {
                            specObj.specificaImgUrl = imgUrl;
                        } else {
                            tip = "请上传商品规格图片";
                        }
                    } else {
                        specObj.specificaImgUrl = "";
                    }
                    specificaArray[specificaArray.length] = specObj;
                });
            } else {
                tip = "请完善商品规格信息";
            }

        } else {
            tip = "请完善商品规格信息";
        }

    });
    return tip;
}
/**
 * 遍历参数
 */
function eachParams() {
    paramsArray = new Array();
    var tip = null;
    var sort = 0;
    // 遍历规格名称
    $(".controls .sku-param-title").each(function () {
        var nameObj = $(this).find(".param-contain option:selected");
        var valueObj = $(this).find(".param-value-contain option:selected");

        var nameId = nameObj.val();// 参数名id
        var nameVal = nameObj.text();// 参数名
        var valueId = valueObj.val();// 参数值id
        var valueVal = valueObj.text();// 参数值

        if (typeof(nameId) == "undefined" || nameId == "") {
            nameId = $(this).find(".param-contain").attr("nameId");
        }
        if (typeof(nameVal) == "undefined" || nameVal == "") {
            nameVal = $(this).find(".param-contain").attr("nameVal");
        }
        if (typeof(valueId) == "undefined" || valueId == "") {
            valueId = $(this).find(".param-value-contain").attr("nameId");
        }
        if (typeof(valueVal) == "undefined" || valueVal == "") {
            valueVal = $(this).find(".param-value-contain").attr("nameVal");
        }
        if (nameId != "" && nameVal != "") {
            if (valueId == "" || valueVal == "") {
                tip = "请完善商品参数值";
                return;
            }
        }
        //console.log(nameId+"-----"+nameVal+"------"+valueId+"-----"+valueVal)
        if (nameId != "" && nameVal != "" && valueId != "" && valueVal != "") {
            var paramsObj = new Object();
            paramsObj.paramsNameId = nameId;
            paramsObj.paramsName = nameVal;
            paramsObj.paramsValueId = valueId;
            paramsObj.paramsValue = valueVal;
            paramsObj.sort = sort;
            sort++;

            paramsArray[paramsArray.length] = paramsObj;
        }
    });
    return tip;
}
/**
 * 遍历库存
 */
function eachInven() {
    invenArray = new Array();
    var table = $("#createTable");
    var thead = table.find("thead");

    var flag = true;
//	console.log("trLen" + table.find("tbody tr").length);
    // 遍历规格值
    table.find("tbody tr").each(function () {
        var len = $(this).find("td").length;
        var array = new Array();
        for (var i = 0; i < (len - 5); i++) {
            var th = thead.find("th:eq(" + i + ")");

            var specStrId = th.attr("id");// 规格名称id
            var specStr = th.text();// 规格名称

            var td = $(this).find("td:eq(" + i + ")");
            var specValId = td.attr("id");// 规格值id
            var specValStr = td.text();// 规格值

            var obj = new Object();
            obj.specificaNameId = specStrId;
            obj.specificaName = specStr;
            obj.specificaValueId = specValId;
            obj.specificaValue = specValStr;

            array[array.length] = obj;
        }
        var price = $(this).find(".js-price");
        var num = $(this).find(".js-stock-num");
        var code = $(this).find(".js-code");
        var isDefaults = $(this).find(".js-default");

        var priceTest = /^[0-9]{1,6}(\.\d{1,2})?$/;
        var numTest = /^\d{1,6}$/;
        var noUpInvNum = 0;//是否能修改库存 1不能修改 0能修改
        if ($("input.noUpInvNum").length > 0) {
            noUpInvNum = $("input.noUpInvNum").val();
        }
        if (!priceTest.test(price.val())) {
            price.next().html("价格必须为小数");
            price.next().hide();
            price.focus();
            flag = false;
        } else if (price.val() * 1 <= 0) {
            price.next().html("价格必须为小数");
            price.next().hide();
            price.focus();
            flag = false;
        }
        if (noUpInvNum == 0) {
            if (!numTest.test(num.val())) {
                num.next().html("库存必须是6位数字");
                num.next().hide();
                num.focus();
                flag = false;
            }
        }

        var invenObj = new Object();
        invenObj.specificas = array;
        invenObj.invPrice = price.val();
        if (noUpInvNum == 0) {
            invenObj.invNum = num.val();
        }
        invenObj.invCode = code.val();
        invenObj.isDefault = 0;
        if (isDefaults.is(":checked")) {
            invenObj.isDefault = 1;
        }
        invenArray[invenArray.length] = invenObj;
    });
    return flag;
}
/**
 * 遍历商品分组  new
 * @returns
 */
function eachProGroup() {
    var groupArray = new Array();
    var deleGroupObj = new Object();
    if (groupShopObj != null) {
        deleGroupObj = groupShopObj;
    }
    //console.log(groupShopObj)

    //console.log(updGroupObj)
    var index = 0;
    if (updGroupObj != null) {
        for (var groupId in updGroupObj) {
            var group = updGroupObj[groupId];

            var maps = getGroups(group, groupArray, index, deleGroupObj, groupShopObj, 0);
            if (maps != null) {
                groupArray = maps.groupArray;
                deleGroupObj = maps.deleGroupObj;
                index = maps.index;
            }
        }
    }
    if (deleGroupObj != null) {
        for (var str in deleGroupObj) {
            var obj = new Object();
            obj.id = deleGroupObj[str];
            obj.isDelete = 1;

            updGroupList[updGroupList.length] = obj;
        }
    }
//	console.log(deleGroupObj);
//	console.log(updGroupList);
//	console.log(groupArray);
    return groupArray;
}

function getGroups(group, groupArray, index, deleGroupObj, groupShopObj, groupPid) {
    //console.log(group);
    var maps = new Object();
    var obj = new Object();
    obj.groupId = group.groupId;
    obj.shopId = group.shopId;
    obj.groupPId = groupPid;
    obj.sort = index;
    var key = obj.groupId + "_" + obj.shopId;
    //console.log(group);
    //console.log(key)
    var id = "";
    if (groupShopObj != null) {
        id = groupShopObj[key];
    }
    if (jQuery.trim(id) == "") {
//		console.log("1-----------");
//		console.log(obj);
//		console.log("2-----------");
        groupArray[groupArray.length] = obj;
    } else {
        obj.id = id;
        delete deleGroupObj[key];
        updGroupList[updGroupList.length] = obj;
    }
    index++;
    maps["groupArray"] = groupArray;
    maps["deleGroupObj"] = deleGroupObj;
    maps["index"] = index;

    if (group.childGroup != null) {
        //console.log(group.childGroup)
        for (var j = 0; j < group.childGroup.length; j++) {
            var child = group.childGroup[j];

            maps = getGroups(child, groupArray, index, deleGroupObj, groupShopObj, group.groupId);
            if (maps != null) {
                groupArray = maps.groupArray;
                deleGroupObj = maps.deleGroupObj;
                index = maps.index;
            }

        }

    }

    return maps;
}
/**
 * 遍历分组 old
 *
 * @returns {Array}
 */
function eachGroup() {
    var groupArray = new Array();
    var deleGroupObj = new Object();
    if (groupShopObj != null) {
        deleGroupObj = groupShopObj;
    }
    var groupStr = $('.group-contain').val();
    var index = 0;
    if (groupStr != null && groupStr != "") {
        for (var i = 0; i < groupStr.length; i++) {
            var group = groupStr[i];
            var obj = new Object();
            obj.groupId = group;
            obj.shopId = groups[obj.groupId];
            var key = obj.groupId + "_" + obj.shopId;
            var id = "";
            if (groupShopObj != null) {
                id = groupShopObj[key];
            }
            if (jQuery.trim(id) == "") {
                groupArray[index] = obj;
                index++;
            } else {
                obj.id = id;

                delete deleGroupObj[key];

                updGroupList[updGroupList.length] = obj;
            }
        }
    }
    if (deleGroupObj != null) {
        for (var str in deleGroupObj) {
            var obj = new Object();
            obj.id = deleGroupObj[str];
            obj.isDelete = 1;
            updGroupList[updGroupList.length] = obj;
        }
    }
    // console.log(groupArray)
    return groupArray;
}
/**
 * 遍历图片
 *
 * @returns {Array}
 */
function eachImage() {
    var imageInp = $(".picture-list input.imageInp");
    var imageList = new Array();
    var delimageList = new Array();
    var defaultObj = new Object();
    if (imagDefaultObj != null) {
        defaultObj = imagDefaultObj;
    }
    // 遍历上传图片
    imageInp.each(function (i) {
        var list = new Object();
        list.imageUrl = $(this).val();
        list.id = $(this).next().val();
        if (i == 0) {
            list.isMainImages = 1;
        } else {
            list.isMainImages = 0;
        }
        list.assType = 1;
        list.assSort = i;
        if (list.id == null || $.trim(list.id) == "") {
            imageList[imageList.length] = list;
        } else {

            delete defaultObj[list.id];

            delimageList[delimageList.length] = list;
        }

    });
    var imageObj = {
        imageList: imageList,
        delimageList: delimageList
    };
    if (defaultObj != null) {
        for (var str in defaultObj) {
            var obj = new Object();
            obj.id = str;
            obj.isDelete = 1;
            delimageList[delimageList.length] = obj;
        }
    }
    return imageObj;
}
/**
 * 验证表单
 */
function validateForm() {
    var flag = true;
    $("input[datatype!='']").each(function () {
        if (!valiReg($(this)) && flag) {
            $(this).focus();
            flag = false;
        }
    });
    return flag;
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
        $("." + valiName).html(errormsg);
        $("." + valiName).show();
        if (obj.hasClass("changeIntegral")) {
            if ($(".isIntegralChangePro").is(":checked")) {
                return false;
            } else {
                return true;
            }
        } else if (obj.hasClass("changeFenbi")) {
            if ($(".isFenbiChangePro").is(":checked")) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    } else if (obj.hasClass("price")) {
        var flag = false;
        if (obj.hasClass("proCostPrice") || obj.hasClass("changeIntegral") || obj.hasClass("changeFenbi")) {
            if (obj.val() != null && obj.val() != "") {
                flag = true;
            }
        } else {
            flag = true;
        }
        if (flag && obj.val() * 1 <= 0) {
            obj.css("border-color", "red");
            $("." + valiName).html(errormsg);
            $("." + valiName).show();
            return false;
        } else {
            obj.css("border-color", "#c5c5c5");
            $("." + valiName).hide();
            return true;
        }
    } else {
        var flag = true;
        if (obj.attr("not-null") != null) {
            if (obj.val() == null || $.trim(obj.val()) == "") {
                obj.css("border-color", "red");
                $("." + valiName).html(errormsg);
                $("." + valiName).show();
                flag = false;
            }
        }
        var maxNum = obj.attr("max_num");
        if (maxNum != null && maxNum != "" && typeof(maxNum) != "undefined") {
            if (!isNaN(obj.val())) {
                var vals = obj.val();
                if (parseInt(vals) > maxNum) {
                    obj.css("border-color", "red");
                    $("." + valiName).html(errormsg);
                    $("." + valiName).show();
                    flag = false;
                }
            }
        }
        if (flag) {
            obj.css("border-color", "#c5c5c5");
            $("." + valiName).hide();
        }
        return flag;
    }
}
//^(\d{1,6}(\.\d{1,2})?)(0(\.\[1-9]{1,2}))$
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
            return false;
        } else if (obj.val() * 1 <= 0) {
            obj.next().html("价格最多只能是6位小数或整数");
            obj.next().show();
            return false;
        } else {
            obj.next().hide();
        }
    }
    if (obj.hasClass("js-stock-num")) {
        if (!numTest.test($.trim(obj.val()))) {
            obj.next().html("库存最多是6位数字");
            obj.next().show();
            return false;
        } else {
            obj.next().hide();
        }
    }
    return true;
}

$(".proLabel").focus(function () {
});
/**
 * 鼠标失去焦点
 */
$(".proLabel").blur(function () {
});