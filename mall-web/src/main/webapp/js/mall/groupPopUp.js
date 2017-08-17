function ok() {
    var array = new Array();
    $(".area-editor-list-title.area-editor-list-parent").each(function () {
        var obj = new Object();
        var bol = false;
        if ($(this).hasClass("area-editor-list-select")) {
            bol = true;
        } else {
            if ($(this).next().find("div.area-editor-list-title.area-editor-list-select").length > 0) {
                bol = true;
            }
        }
        if (bol) {

            obj["groupId"] = $(this).find("input.groupId").val();
            obj["groupName"] = $(this).find("input.groupName").val();
            obj["shopId"] = $(this).find("input.shopId").val();
            var child = new Array();
            $(this).next().find("div.area-editor-list-title").each(function () {
                if ($(this).hasClass("area-editor-list-select")) {
                    var cObj = new Object();
                    cObj["groupId"] = $(this).find("input.groupId").val();
                    cObj["groupName"] = $(this).find("input.groupName").val();
                    cObj["shopId"] = $(this).find("input.shopId").val();
                    child[child.length] = cObj;
                }
            });
            if (child != null && child.length > 0) {
                obj["childGroup"] = child;
            }

            array[array.length] = obj;
        }
    });
    //TODO parent.returnVal(array);
    parent.returnVal(array);
}
function cancel() {
    parentCloseAll();
    // parent.layer.closeAll();
}

onload();
function onload() {
    $(".area-editor-list-parent").each(function () {
        var _this = $(this);
        var bol = false;

        if (_this.hasClass("area-editor-list-select")) {
            //bol = true;
        }
        _this.next().find(".area-editor-list-title").each(function () {
            if ($(this).hasClass("area-editor-list-select")) {
                bol = true;
            }
        });

        if (bol) {
            var obj = _this.find(".area-editor-ladder-toggle");
            obj.html("-");
            _this.next().show();
        }

    });

    $(".area-editor-list-title").on("click", function () {
        var _this = $(this);

        if (_this.hasClass("area-editor-list-select")) {

            _this.removeClass("area-editor-list-select");

            if (_this.hasClass("area-editor-list-parent")) {
                _this.next().find(".area-editor-list-title").removeClass("area-editor-list-select");
            } else {
                _this.parents("ul").prev().removeClass("area-editor-list-select");
            }

        } else {
            _this.addClass("area-editor-list-select");

            if (_this.hasClass("area-editor-list-parent")) {
                _this.next().find(".area-editor-list-title").addClass("area-editor-list-select");
            } else {
                if (_this.parents(".area-editor-list1").find("li").length == _this.parents(".area-editor-list1").find(".area-editor-list-select").length) {
                    _this.parents("ul").prev().addClass("area-editor-list-select");
                }
            }
        }

    });

    $(".area-editor-ladder-toggle").on("click", function () {
        if ($(this).html() == "-") {
            $(this).html("+");
            $(this).parents("li").find("ul").hide();
        } else {
            $(this).html("-");
            $(this).parents("li").find("ul").show();
        }
        event.stopPropagation();
    })
}
var parentLayer = window.parent.parent.layer;
$(".shopSelect").change(function () {
    var obj = $(this);
    var shopId = obj.find("option:selected").attr("id");
    getShop(shopId);
});
function getShop(shopId) {
    if (shopId == null || shopId == "" || shopId == "0") {
        shopId = $(".shopSelect option:selected").attr("id");
    }
    parentLayerLoad();
    // var layerLoad = parentLayer.load(1, {
    //     shade: [0.3, '#000']
    // });
    $.ajax({
        type: "post",
        data: {shopId: shopId},
        url: "mPro/group/getGroupsByShopid.do",
        dataType: "json",
        success: function (data) {
            parentCloseAll();
            // parentLayer.close(layerLoad);
            var html = "";
            if (data.code == 1) {
                if (data.list != null && data.list.length > 0) {
                    for (var i = 0; i < data.list.length; i++) {
                        var group = data.list[i];
                        html += '<li>';
                        html += '<div class="area-editor-list-title area-editor-list-parent">';
                        html += '<div class="area-editor-list-title-content" >';
                        html += '<div class="area-editor-ladder-toggle">+</div>' + group.groupName;
                        html += '<input type="hidden" class="groupId" value="' + group.id + '" />';
                        html += '<input type="hidden" class="groupName" value="' + group.groupName + '" />';
                        html += '<input type="hidden" class="shopId" value="' + group.shopId + '" />';
                        html += '</div>';
                        html += '</div>';
                        if (group.childGroupList != null && group.childGroupList.length > 0) {
                            html += '<ul class="area-editor-list area-editor-list1" style="display: none">';
                            for (var j = 0; j < group.childGroupList.length; j++) {
                                var child = group.childGroupList[j];
                                html += '<li>';
                                html += '<div class="area-editor-list-title">';
                                html += '<div class="area-editor-list-title-content">' + child.groupName;
                                html += '<input type="hidden" class="groupId" value="' + child.id + '" />';
                                html += '<input type="hidden" class="groupName" value="' + child.groupName + '" />';
                                html += '<input type="hidden" class="shopId" value="' + child.shopId + '" />';
                                html += '</div>';
                                html += '</div>';
                                html += '</li>';
                            }
                            html += '</ul>';
                        }
                        html += '</li>';
                    }
                }
                if (html == "") {
                    html = '<a href="/mPro/group/start.do" target="_blank">前去新建分组</a><br/>';
                    html += '<a href="javascript:void(0);" onclick="getShop(0);">刷新分组</a>';
                    //alert("请重新选择所属店铺");
                }
            }
            $("ul.area-editor-list").html(html);
            onload();
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}


function subtmit() {
    var array = new Array();
    $(".area-editor-list-title.area-editor-list-parent").each(function () {
        var obj = new Object();
        var bol = false;
        if ($(this).hasClass("area-editor-list-select")) {
            bol = true;
        } else {
            if ($(this).next().find("div.area-editor-list-title.area-editor-list-select").length > 0) {
                bol = true;
            }
        }
        if (bol) {
            var groupId = $(this).find("input.groupId").val();
            obj["groupId"] = groupId;
            obj["groupName"] = $(this).find("input.groupName").val();
            obj["shopId"] = $(this).find("input.shopId").val();
            obj["groupPId"] = 0;
            obj["sort"] = array.length;
            $(this).next().find("div.area-editor-list-title").each(function () {
                if ($(this).hasClass("area-editor-list-select")) {
                    var cObj = new Object();
                    cObj["groupId"] = $(this).find("input.groupId").val();
                    cObj["groupName"] = $(this).find("input.groupName").val();
                    cObj["shopId"] = $(this).find("input.shopId").val();
                    cObj["groupPId"] = groupId;
                    obj["sort"] = array.length;

                    array[array.length] = cObj;
                }
            });
            array[array.length] = obj;
        }
    });
    var shopId = $(".shopSelect option:selected").attr("id");
    if (shopId == null || shopId == "") {
        parentAlertMsg("请选择所属店铺");
        // parentLayer.alert("请选择所属店铺", {
        //     offset: "30%"
        // });
        return false;
    }
    if (array == null || array.length == 0) {
        parentAlertMsg("请选择所属分组");
        // parentLayer.alert("请选择所属分组", {
        //     offset: "30%"
        // });
        return false;
    }
    var data = {
        shopId: shopId,
        id: $("input.proId").val(),
        groupList: JSON.stringify(array)
    };
    //console.log(data);

    parentLayerLoad();
    // var layerLoad = parentLayer.load(1, {
    //     shade: [0.3, '#000'],
    //     offset: "30%"
    // });
    $.ajax({
        type: "post",
        data: data,
        url: "mPro/copy_pro.do",
        dataType: "json",
        timeout: 60000 * 30,//半小时的超时时间
        success: function (data) {
            parentCloseAll();
            // parentLayer.close(layerLoad);
            //parentLayer.closeAll();
            if (data.code == 0) {// 重新登录
                parentAlertMsg("操作失败，长时间没操作，跳转到登录页面");
                //TODO alert 跳转
                // parentLayer.alert("操作失败，长时间没操作，跳转到登录页面", {
                //     offset: "30%",
                //     closeBtn: 0
                // }, function (index) {
                //     parent.location.href = "/user/tologin.do";
                // });
            } else if (data.code == 1) {
                parentAlertMsg("同步商品成功");
                //TODO alert 跳转
                // var tipLayer = parentLayer.alert("同步商品成功", {
                //     offset: "30%",
                //     closeBtn: 0
                // }, function (index) {
                //     //parentLayer.closeAll();
                //     parent.location.href = window.parent.location.href;
                // });
            } else {// 编辑失败
                parentAlertMsg("同步商品失败");
                // parentLayer.alert("同步商品失败", {
                //     offset: "30%"
                // });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            //parentLayer.closeAll();
            parentAlertMsg("同步商品失败");
            // parentLayer.alert("同步商品失败", {
            //     offset: "30%"
            // });
            return;
        }
    });
    //parent.returnVal(array);
}
