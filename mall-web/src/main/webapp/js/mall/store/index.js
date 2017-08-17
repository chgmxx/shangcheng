$(function () {
    $("#go").click(function () {
        $("#queryForm").submit();
    });
});


/**
 * 删除
 * @param obj
 */
function del(ids) {
    //event.stopPropagation();
    //TODO parent.layer.confirm
    parent.layer.confirm('确认要删除此数据吗?', {icon: 3, title: '提示', shade:[0.1,'#fff'], offset: "40%"}, function (index) {
        parentCloseAll();
        var params = {
            ids: ids
        };
        parentLayerLoad();
        $.post("store/delete.do", params, function (data) {
            parentAlertMsg(data.message);
            if (data.result) {
                parentCloseAll();
                // parent.layer.close(index2);
                location.reload();
            }
        }, "json");

    });
}

/**
 * 删除多个
 */
function batchdel() {
    var ids = "";
    $("input[name='genre']").each(function (index) {
        if ($(this).is(":checked")) {
            ids += $(this).val() + ",";
        }
    });
    ids = ids.substring(0, ids.lastIndexOf(","));
    if (ids != "" && ids != undefined) {
        del(ids);
    } else {
        parentAlertMsg("请至少选择一条数据！");
    }
}
