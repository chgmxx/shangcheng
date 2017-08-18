$(document).ready(function () {
    var type = $(".type:checked").val();
    $(".proDIv").hide();
    if (type == 0) {
        $(".proDIv").hide();
    } else if (type == 1) {
        $(".proDIv").show();
    }

});
$(".type").change(function () {
    var val = $(this).val();
    if (val == 0) {
        $(".proDIv").hide();
    } else if (val == 1) {
        $(".proDIv").show();
    }
});
/**
 * 删除小程序图片
 */
function deleteImage(obj, type) {
    var id = $(obj).attr("id");
    if (id != null && id != "") {
        var msg = "删除小程序图片";
        if (type * 1 == -2) {
            msg = "不显示小程序图片";
        } else if (type * 1 == 1) {
            msg = "显示小程序图片";
        }
        // 询问框
        layer.confirm('您确定要' + msg + '？', {
            offset: "30%",
            shade:[0.1,'#fff'],
            btn: ['确定', '取消']
            // 按钮
        }, function () {
            // loading层
            var layerLoad = layer.load(1, {
                offset: "30%",
                shade: [0.1, '#fff'] // 0.1透明度的白色背景
            });
            $.ajax({
                type: "post",
                url: "mApplet/applet_remove.do",
                data: {
                    id: id,
                    type: type
                },
                dataType: "json",
                success: function (data) {
                    layer.close(layerLoad);
                    if (data.code == 1) {
                        var tip = layer.alert(msg + "成功", {
                            shade:[0.1,"#fff"],
                            offset: "30%",
                            closeBtn: 0
                        }, function (index) {
                            layer.close(tip);
                            location.href = window.location.href;
                        });
                    } else {// 编辑失败
                        var tip = layer.alert(msg + "失败", {
                            shade:[0.1,"#fff"],
                            offset: "30%"
                        });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    layer.close(layerLoad);
                    layer.alert(msg + "失败", {
                        shade:[0.1,"#fff"],
                        offset: "30%"
                    });
                    return;
                }
            });
            layer.closeAll();
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
/**
 * 编辑小程序
 */
function editAuction() {
    var type = $(".type:checked").val();
    var imageUrl = $(".imageUrl").val();
    var productId = $("#productId").val();//商品id.
    var shopId = $("#shopId").val();
    if (imageUrl == null || imageUrl == "" || typeof(imageUrl) == "undefined") {
        layer.msg('请上传图片', {
            shade:[0.1,"#fff"],
            offset: "30%",
            icon: 1
        });
        return false;
    }
    if (type == 1) {//跳转到商品详情
        if (productId == null || productId == "") {
            layer.msg('请选择要跳转的商品', {
                shade:[0.1,"#fff"],
                offset: "30%",
                icon: 1
            });
            return false;
        }
    }
    var ids = $("#ids").val();
    var data = {
        "imageUrl": imageUrl,
        "proId": productId,
        "type": type,
        "shopId": shopId
    };
    if (ids != null && ids != "" && typeof(ids) != "undefined") {
        data["id"] = ids;
    }

    // loading层
    var layerLoad = layer.load(1, {
        offset: "30%",
        shade: [0.1, '#fff']
        // 0.1透明度的白色背景
    });
    $.ajax({
        type: "post",
        url: "mApplet/edit.do",
        data: data,
        dataType: "json",
        success: function (data) {
            layer.close(layerLoad);
            if (data.code == 1) {
                var tip = layer.alert("编辑成功", {
                    offset: "30%",
                    shade:[0.1,"#fff"],
                    closeBtn: 0
                }, function (index) {
                    layer.close(tip);
                    location.href = "/mApplet/index.do";
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
            obj.next().css("color", "red");
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
                obj.next().css("color", "red");
                return false;
            } else {
                obj.css("border-color", "#c5c5c5");
                obj.next().css("color", "#000");
                return true;
            }
        } else {
            var flag = true;
            if (obj.attr("notNull") != null) {
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
    parentOpenIframe("选择商品", "600px", "480px", "/mGroupBuy/getProductByGroup.do?defaultProId=" + defaultProId);
    // parent.openIframe("选择商品", "600px", "480px", "/mGroupBuy/getProductByGroup.do?defaultProId=" + defaultProId);//check==0代表多选，check==1代表单选
};
/**
 * 选择商品回调函数
 * @param obj
 */
function getProductGroup(obj, arr) {
    if (obj != null) {
        $(".pro_a").css({
            "background-image": "url(" + obj.src + ")",
            "background-position": "center center",
            "background-size": "cover"
        }).html("");
        $("input#productId").val(obj.id);
        $("#isSpec").val(obj.isSpe);
        $("span.proName").html(obj.title);
        $(".proPrice").html("￥" + obj.price);

        $("#aucNum").val(obj.stockTotal);
        //console.log(obj.stockTotal)
        /*if(arr != null && obj.isSpe == 1){
         proSpecArr = arr;
         //			console.log(proSpecArr)

         eval(step.Creat_Table());// 初始化创建库存表格

         $("div.proPrices").hide();
         }else{
         $("#createTable").hide();
         $("div.proPrices").show();
         }*/

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
 * 选择图片
 * @returns
 */
function chooseImage() {
    fhmater(0);
    // parent.layer.open({
    //     type: 2,
    //     title: '素材库',
    //     shadeClose: true,
    //     shade: 0.2,
    //     area: ['820px', '500px'],
    //     offset: "10px",
    //     content: "/common/material.do"
    // });
}
//素材库里面返回信息
function image(imageArray, url) {
    layer.closeAll();
    $("#main")[0].contentWindow.fhmateriallayer(id, url); // 父类调用子类的方法
}
/**
 * 上传图片回调函数
 *
 * @param id
 * @param url
 */
function fhmateriallayer(imageArray, url) {
    // console.log(JSON.stringify(imageArray) + "===" + url)

    var imageHttp = $(".imageHttp").val();

    var imgUrl = url.split("/upload/")[1];

    $(".add-products").css({
        "background-image": "url(" + ( imageHttp + imgUrl) + ")",
        "background-size": "cover",
        "background-position": "50% 50%"
    }).html("");
    $(".imageUrl").val(imgUrl);
}