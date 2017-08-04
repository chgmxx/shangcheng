var userid = $("input.userid").val();
$(function () {
    $('.inp input[type="checkbox"]').click(function () {
        $(this).toggleClass("checked");
        location.href = "/phoneOrder/79B4DE7C/toOrder.do?takeId=" + $(this).val() + "&uId=" + userid;
    });

    $('.address-box li').click(function () {
        if ($(this).attr("id") != null && $(this).attr("id") != "") {
            location.href = "/phoneOrder/79B4DE7C/toOrder.do?takeId=" + $(this).attr("id") + "&uId=" + userid;
        }
    });

});

function closes(obj) {
    var id = $(obj).attr("id");
    var html = "/phoneOrder/79B4DE7C/toOrder.do?uId=" + userid;
    if (id != null && id != "") {
        html += "&takeId=" + id;
    }
    location.href = html;
}