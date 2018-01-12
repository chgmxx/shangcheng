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
        if(obj.id == null || obj.id == ""){
            delete obj.id;
        }
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
                layer.alert(data.message, {
                    offset: "10%",
                    shade:[0.1,"#fff"],
                    closeBtn: 0
                }, function (index) {
                    if (data.result) {
                        location.href = $(".urls").val();
                    }else{
                        layer.closeAll();
                    }
                });
            }
        });
    }
}