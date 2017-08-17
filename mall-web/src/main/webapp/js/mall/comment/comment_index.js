var parentLayer = window.parent.layer;

/**
 * 批量删除
 */
$(".js-batch-delete").click(function () {
    var id = [];
    var flag = false
    $(".checkComment").each(function () {
        if ($(this).is(":checked")) {
            flag = true;
            id.push($(this).attr("id"));
        }
    });
    if (id.length == 0 || !flag) {
        parentAlertMsg("请选择需要删除的评论");
        // parentLayer.alert("请选择需要删除的评论", {
        //     offset: "30%",
        //     closeBtn: 0
        // }, function (index) {
        //     parentLayer.closeAll();
        // });
    } else {
        //TODO  parent.layer.confirm
        parentLayer.confirm('您确定要批量删除选中的评论？', {
            btn: ['确定', '取消'],
            shade:[0.1,'#fff'],
            offset: '100px'
        }, function () {
            parentLayer.closeAll();
            var data = {
                ids: JSON.stringify(id),
                isDelete: 1
            };
            deletePro(data, "批量删除", 1);
        });

    }

});
/**
 * 删除评论
 */
$(".a-del").click(function () {
    var id = [];
    if ($(this).attr("id") != null) {
        id.push($(this).attr("id"));
        //TODO  parent.layer.confirm
        parentLayer.confirm('您确定要删除评论评论？', {
            btn: ['确定', '取消'],
            shade:[0.1,'#fff'],
            offset: '100px'
        }, function () {
            parentLayer.closeAll();
            var data = {
                ids: JSON.stringify(id),
                isDelete: 1
            };
            deletePro(data, "删除评论", 1);
        });
    }
});
/**
 * 审核通过
 */
$(".a-check").click(function () {
    var id = [];
    if ($(this).attr("id") != null) {
        id.push($(this).attr("id"));

        //TODO  parent.layer.confirm
        parentLayer.confirm('您确定要通过评论？', {
            btn: ['确定', '取消'],
            shade:[0.1,'#fff'],
            offset: '100px'
        }, function () {
            parentLayer.closeAll();
            var data = {
                ids: JSON.stringify(id),
                checkStatus: 1
            };
            deletePro(data, "审核通过", 1);
        });
    }

});
/**
 * 审核不通过
 */
$(".a-uncheck").click(function () {
    var id = [];
    if ($(this).attr("id") != null) {
        id.push($(this).attr("id"));

        //TODO  parent.layer.confirm
        parentLayer.confirm('您确定要不通过评论？不通过评论后，不会在商品详情页面展示', {
            btn: ['确定', '取消'],
            shade:[0.1,'#fff'],
            offset: '100px'
        }, function () {
            parentLayer.closeAll();
            var data = {
                ids: JSON.stringify(id),
                checkStatus: -1
            };
            deletePro(data, "审核不通过", 1);
        });
    }
});
/**
 * 批量通过
 */
$(".js-batch-agreen").click(function () {
    var id = [];
    var flag = false
    $(".checkComment").each(function () {
        var checkStatus = $(this).parent().find(".checkStatus");
        if ($(this).is(":checked")) {
            flag = true;
            if (checkStatus == 0) {
                id.push($(this).attr("id"));
            }
        }
    });
    if (id.length == 0 && !flag) {
        parentAlertMsg("请选择需要通过的评论");
        // parentLayer.alert("请选择需要通过的评论", {
        //     offset: "30%",
        //     closeBtn: 0
        // }, function (index) {
        //     parentLayer.closeAll();
        // });
    } else if (id.length == 0 && flag) {
        parentAlertMsg("未审核的评论才能进行审核通过操作");
        // parentLayer.alert("未审核的评论才能进行审核通过操作", {
        //     offset: "30%",
        //     closeBtn: 0
        // }, function (index) {
        //     parentLayer.closeAll();
        // });
    } else {
        //TODO parent.layer.confirm
        parentLayer.confirm('您确定要批量通过选中的评论？', {
            btn: ['确定', '取消'],
            shade:[0.1,'#fff'],
            offset: '100px'
        }, function () {
            parentLayer.closeAll();
            var data = {
                ids: JSON.stringify(id),
                checkStatus: 1
            };
            deletePro(data, "批量通过", 1);
        });

    }

});

/**
 * 批量不通过
 */
$(".js-batch-no-agreen").click(function () {
    var id = [];
    var flag = false
    $(".checkComment").each(function () {
        var checkStatus = $(this).parent().find(".checkStatus");
        if ($(this).is(":checked")) {
            flag = true;
            if (checkStatus == 0) {
                id.push($(this).attr("id"));
            }
        }
    });
    if (id.length == 0 && !flag) {
        parentAlertMsg("请选择需要不通过的评论");
        // parentLayer.alert("请选择需要不通过的评论", {
        //     offset: "30%",
        //     closeBtn: 0
        // }, function (index) {
        //     parentLayer.closeAll();
        // });
    }
    if (id.length == 0 && flag) {
        parentAlertMsg("未审核的评价才能进行审核未通过操作");
        // parentLayer.alert("未审核的评价才能进行审核未通过操作", {
        //     offset: "30%",
        //     closeBtn: 0
        // }, function (index) {
        //     parentLayer.closeAll();
        // });
    } else {
        //TODO  parent.layer.confirm
        parentLayer.confirm('您确定要批量未通过选中的评论？', {
            btn: ['确定', '取消'],
            shade:[0.1,'#fff'],
            offset: '100px'
        }, function () {
            parentCloseAll();
            // parentLayer.closeAll();
            var data = {
                ids: JSON.stringify(id),
                checkStatus: -1
            };
            deletePro(data, "批量不通过", 1);
        });

    }

});

function deletePro(data, tip, index) {
    var layerLoad =parentLayerLoad();
    // var layerLoad = parentLayer.load(1, {
    //     shade: [0.3, '#000']
    // });
    $.ajax({
        type: "post",
        data: data,
        url: "/comment/checkComment.do",
        dataType: "json",
        success: function (data) {
            parentCloseAll();
            // parentLayer.close(layerLoad);
            if (data != null) {
                if (data.result) {
                    parentAlertMsg(tip + "成功");
                    //TODO alert 跳转
                    // var tipLayer = parent.layer.alert(tip + "成功", {
                    //     offset: "30%",
                    //     closeBtn: 0
                    // }, function (index) {
                    //     parent.layer.close(tipLayer);
                    //     location.href = window.location.href;
                    // });
                } else {
                    parentAlertMsg(tip + "失败");
                    // parent.layer.alert(tip + "失败", {
                    //     offset: "30%"
                    // });
                }
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            parentCloseAll();
            parentAlertMsg(tip + "失败");
            // parent.layer.closeAll();
            // parent.layer.alert(tip + "失败", {
            //     offset: "30%"
            // });
            return;
        }
    });
}
/**
 * 回车提交
 */
$(".subRepComBtn").click(function (event) {
    var parentObj = $(this).parents(".co-replay");

    var pId = parentObj.find("input.commentPId").val();
    var repContent = parentObj.find(".repContent").html();
    var shop_id = parentObj.find("input.shop_id").val();
    if (repContent == null || repContent == "" || typeof(repContent) == "undefinde") {
        parentAlertMsg("请输入要回复的内容");
        // parent.layer.alert("请输入要回复的内容", {
        //     offset: "30%"
        // });
    } else if (repContent.length >= 240) {
        parentAlertMsg("回复内容不能超过240个字");
        // parent.layer.alert("回复内容不能超过240个字", {
        //     offset: "30%"
        // });
    } else {
        var data = {
            content: $.trim(repContent),
            repPId: pId,
            shopId: shop_id
        };
        var layerLoad = parentLayerLoad();
        // var layerLoad = parentLayer.load(1, {
        //     shade: [0.3, '#000']
        // });
        $.ajax({
            type: "post",
            data: {params: JSON.stringify(data)},
            url: "/comment/repComment.do",
            dataType: "json",
            success: function (data) {
                parentCloseAll();
                // parentLayer.close(layerLoad);
                if (data != null) {
                    if (data.result) {
                        parentAlertMsg("回复评论成功");
                        //TODO alert 跳转
                        // var tipLayer = parent.layer.alert("回复评论成功", {
                        //     offset: "30%",
                        //     closeBtn: 0
                        // }, function (index) {
                        //     parent.layer.close(tipLayer);
                        //     location.href = window.location.href;
                        // });
                    } else {
                        parentAlertMsg("回复评论失败");
                        parent.layer.alert("回复评论失败", {
                            offset: "30%"
                        });
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                parentCloseAll();
                parentAlertMsg("回复评论失败");
                // parent.layer.closeAll();
                // parent.layer.alert("回复评论失败", {
                //     offset: "30%"
                // });
                return;
            }
        });
    }
});


