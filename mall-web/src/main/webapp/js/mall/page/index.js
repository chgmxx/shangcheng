$(function () {
    $("#go").click(function () {
        $("#queryForm").submit();
    });
})


/**
 * 删除
 * @param obj
 */
function del(ids) {
    //event.stopPropagation();
    parent.layer.confirm('确认要删除此数据吗?删除之后，引用该页面的地方将会因为找不到该链接跳转到404页面', {icon: 3, title: '提示', offset: "40%"}, function (index) {
        parent.layer.close(index);
        var params = {
            ids: ids
        };
        var index2 = parent.layer.load(3, {
            offset: '40%',
            shade: [0.4, '#8E8E8E']
        });
        $.post("mallPage/delete.do", params, function (data) {
            parent.alertMsg(data.message);
            parent.layer.close(index2);
            if (data.result) {
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
        parent.alertMsg("请至少选择一条数据！");
    }
}


/**
 * 设为主页
 */
function setMian(id, shopid) {
    parent.layer.confirm('确认要设为主页吗?', {icon: 3, title: '提示', offset: "40%"}, function (index) {
        parent.layer.close(index);
        var params = {
            id: id,
            shopid: shopid
        };
        var index2 = parent.layer.load(3, {
            offset: '40%',
            shade: [0.4, '#8E8E8E']
        });
        $.post("mallPage/setMain.do", params, function (data) {
            parent.alertMsg(data.message);
            if (data.result) {
                parent.layer.close(index2);
                location.reload();
            }
        }, "json");

    });

}

/**
 * 设计页面
 * @param id
 */
function designPage(id) {

    $("#id").val(id);
    $("#xinfrom").submit();//post 请求弹出新页面
}