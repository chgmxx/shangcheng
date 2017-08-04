$(function () {
    //表格点击行，选中勾选。
    $(".txt-tle2").find("li").click(function () {
        var checkbox = $(this).find("input[name='genre']");
        if (checkbox != undefined) {
            if (checkbox.is(":checked")) {
                checkbox.removeAttr("checked");
            } else {
                checkbox.attr("checked", "checked");
            }
        }
    });

    $("input[name='genre']").click(function (event) {
        event.stopPropagation();
    })
});


/** 分页-表格全选* */
function selectAll(obj) {
    if ($(obj).is(":checked")) {
        $("#a-allchoose").text("反选");
        $("input[name='genre']").attr("checked", "checked");
    } else {
        $("#a-allchoose").text("全选");
        $("input[name='genre']").removeAttr("checked");
    }
}