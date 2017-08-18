$(function () {

});


/**
 * 保存
 */
function save() {
    var pagStoId = $("#pagStoId").val();
    if (pagStoId == "" || pagStoId <= 0) {
        $("#pagStoId").parent().next().find("span").text("不能为空");
        return;
    }
    if (requiredValidate()) {
        var index = layer.load(3, {
            offset: '10%',
            shade: [0.4, '#fff']
        });
        var obj = $("#tab").serializeObject();
        var params = {
            obj: JSON.stringify(obj)
        };
        $.ajax({
            type: "post",
            url: "/mallPage/saveOrUpdate.do",
            data: params,
            dataType: "json",
            success: function (data) {
                layer.close(index);
                alertMsg(data.message);
                if (data.result) {
                    location.href = $(".urls").val();
                }
            }
        });
    }
}