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
    layer.confirm('确认要删除此数据吗?', {icon: 3, title: '提示', shade:[0.1,'#fff'], offset: "10%"}, function (index) {
        layer.closeAll();
        var params = {
            ids: ids
        };
        var index2 = layer.load(1, {
            offset: "10%",
            shade: [0.1, '#fff']
            // 0.1透明度的白色背景
        });
        $.post("store/delete.do", params, function (data) {
            layer.msg(data.message);
            if (data.result) {
                layer.close(index2);
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
        layer.msg("请至少选择一条数据！", {
            shade:[0.1,"#fff"],
            offset: "10%"
        });
    }
}
