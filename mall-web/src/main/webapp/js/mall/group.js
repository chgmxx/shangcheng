/**
 * 删除分组
 */
function deleteGroup(obj) {
    var id = $(obj).attr("id");
    if (id != null && id != "") {
        // 询问框
        parent.layer.confirm('您确定要删除？', {
            btn: ['确定', '取消'],
            offset: "30%"
            // 按钮
        }, function () {
            // loading层
            var layerLoad = parent.layer.load(1, {
                shade: [0.1, '#fff'],
                offset: "30%"
                // 0.1透明度的白色背景
            });
            $.ajax({
                type: "post",
                url: "mPro/group/group_remove.do",
                data: {
                    id: id
                },
                dataType: "json",
                success: function (data) {
                    parent.layer.close(layerLoad);
                    if (data.code == 0) {// 重新登录
                        parent.layer.alert("操作失败，长时间没操作，跳转到登录页面", {
                            offset: "30%",
                            closeBtn: 0
                        }, function (index) {
                            location.href = "/user/tologin.do";
                        });
                    } else if (data.code == 1) {
                        var tip = parent.layer.alert("删除成功", {
                            offset: "30%",
                            closeBtn: 0
                        }, function (index) {
                            parent.layer.close(tip);
                            location.href = window.location.href;
                        });
                    } else {// 编辑失败
                        var tip = parent.layer.alert("删除失败", {
                            offset: "30%"
                        });
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                    parent.layer.close(layerLoad);
                    parent.layer.alert("删除失败", {
                        offset: "30%"
                    });
                    return;
                }
            });
            parent.layer.closeAll();
        });
    }

}
/**
 * 编辑分组
 */

function editGroup() {
    var groupName = $("#groupName").val();
    var firstPriority = $(".firstPriority").find("option:selected").val();
    var secondPriority = $(".secondPriority").find("option:selected").val();
    var isShowPage = $(".isShowPage:checked").val();
    var sort = $("#sort").val();
    var shopId = $(".shopId").find("option:selected").val();
    var isFirstParents = $("#isFirstParents").val();
    var isChild = $("#isChild").val();
    var pId = $("#pId").val();
    var id = $("#id").val();
    if (groupName == null || $.trim(groupName) == "") {
        $("#groupName").focus();
        layer.msg('请填写商品分组名称', {
            icon: 1
        });
    } else if (!valGroupName($("#groupName"))) {
        $("#groupName").focus();
        layer.msg('分组名称最多输入6位汉字或12位字符', {
            icon: 1
        });
    } else {
        var image = new Array();
        $(".picture-list li .imageInp").each(function () {
            if ($(this).val() != null && $(this).val() != "") {
                var obj = {
                    imageUrl: $(this).val(),
                    assType: 2,
                    assSort: image.length
                };
                if (image.length == 0) {
                    obj.isMainImages = 1;
                }
                var imageId = $(this).next().val();
                if (imageId != null && imageId != "") {
                    obj.id = imageId;
                    if (groupObj != null)
                        delete groupObj[imageId]
                }
                if (id != null) {
                    obj.assId = id;
                }
                image[image.length] = obj;
            }
        });
        if (groupObj != null) {
            for (var str in groupObj) {
                var obj = new Object();
                obj.id = str;
                obj.isDelete = 1;
                image[image.length] = obj;
            }
        }
        if ($(".parentGroups").length > 0) {
            pId = $(".parentGroups").val();
        }
        var group = {
            groupName: groupName,
            firstPriority: firstPriority,
            secondPriority: secondPriority,
            isShowPage: isShowPage,
            sort: sort,
            shopId: shopId,
            isFirstParents: isFirstParents,
            isChild: isChild,
            groupPId: pId,
            id: id
        };

        // loading层
        var layerLoad = parent.layer.load(1, {
            shade: [0.1, '#fff']
            // 0.1透明度的白色背景
        });
        $.ajax({
            type: "post",
            url: "mPro/group/group_edit.do",
            data: {
                group: JSON.stringify(group),
                imageArr: JSON.stringify(image)
            },
            dataType: "json",
            success: function (data) {
                parent.layer.close(layerLoad);
                if (data.code == 0) {// 重新登录
                    parent.layer.alert("操作失败，长时间没操作，跳转到登录页面", {
                        offset: "30%",
                        closeBtn: 0
                    }, function (index) {
                        location.href = "/user/tologin.do";
                    });
                } else if (data.code == 1) {
                    var tip = parent.layer.alert("编辑成功", {
                        offset: "30%",
                        closeBtn: 0
                    }, function (index) {
                        parent.layer.close(tip);
                        var url = $("input.urls").val();
                        if (url == null || url == "" || (typeof url) == "undefined") {
                            url = "/mPro/group/group_index.do";
                        }
                        location.href = url;
                    });
                } else {// 编辑失败
                    parent.layer.alert("编辑失败", {
                        offset: "30%"
                    });
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.close(layerLoad);
                parent.layer.alert("编辑失败", {
                    offset: "30%"
                });
                return;
            }
        });

    }
}
$("#sort").keyup(function () {
    var val = $(this).val();
    var pattern = /[^0-9]{0,4}/g;
    var parent = $(this).parent();
    if (!pattern.test(val)) {
        val = val.replace(pattern, "");
        parent.find("span.red").css("color", "red");
    } else {
        parent.find("span.red").css("color", "#000");
    }
});
$("#groupName").keyup(function () {
    var val = $(this).val();
    valGroupName($(this));
});

function valGroupName(obj) {
    var val = obj.val();
    var pattern = /^[\u4E00-\u9FA5\uf900-\ufa2d\w\.\s]{1,6}$/g;
    var parent = obj.parent();
    if (!getStrLen(val, 12)) {
        val = val.replace(pattern, "");
        parent.find("span.red").css("color", "red");
        return false;
    } else {
        parent.find("span.red").css("color", "#000");
        return true;
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
 * 上传图片回调函数
 *
 * @param id
 * @param url
 */
function fhmateriallayer(imageArray, url) {
    console.log(JSON.stringify(imageArray) + "===" + url)
    var html = "";
    var preHtml = "";
    if (imageArray != null && imageArray.length > 0) {
        for (var i = 0; i < imageArray.length; i++) {
            var imageUrl = imageArray[i].url;
            var url = imageUrl.split("/upload/")[1];
            html += "<li class='sort delParent'>";
            html += "<img src='"
                + (imgResource + url)
                + "'/><a class='js-delete-picture close-modal small hide' onclick='delImg(this)'>×</a>";
            html += "<input type='hidden' class='imageInp' value='" + url
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

}
var imgObj;

// 弹出素材库
function materiallayer(obj) {
    imgObj = $(obj);
    parent.layer.open({
        type: 2,
        title: '素材库',
        shadeClose: true,
        shade: 0.2,
        area: ['820px', '500px'],
        offset: "10px",
        content: "/common/material.do?selectType=checked",
    });
}
// 素材库里面返回信息
function image(imageArray, url) {
    parent.layer.closeAll();
    $("#main")[0].contentWindow.fhmateriallayer(id, url); // 父类调用子类的方法
}

/**
 * 删除图片
 *
 * @param obj
 */
function delImg(obj) {
    $(obj).parents('.delParent').remove();
}