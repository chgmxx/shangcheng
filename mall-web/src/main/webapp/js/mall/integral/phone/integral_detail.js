/**
 * 加载更多
 */
function loadMore() {
    var curPage = $("input.curPage").val();
    if (curPage == null || curPage == '') {
        return false;
    }
    var datas = {
        curPage: curPage * 1 + 1,
        shopId: $(".shopId").val()
    };
    $("input.isLoading").val(-1);
    $(".load-more").hide();
    $.ajax({
        type: "post",
        url: "/phoneIntegral/79B4DE7C/integerDetailPage.do",
        data: datas,
        dataType: "json",
        success: function (data) {
            var html = "";
            if (data != null) {
                if (data.integerList != null && data.integerList.length > 0) {
                    for (var i = 0; i < data.integerList.length; i++) {
                        var integral = data.integerList[i];
                        html += "<li class='line-item'>" +
                            "<div class='item-bd'> " +
                            "<h3 class='bd-tt'>" + integral.itemName + "</h3>" +
                            "<p class='bd-txt ellipsis'>" + integral.createDate + "</p> " +
                            "</div>" +
                            "<span class='item-append integral-detail-price'>" + integral.number + "</span>" +
                            "</li>";
                    }
                }
                if (data.curPage * 1 <= data.pageCount * 1) {
                    $("input.isLoading").val(0);
                } else {
                    $(".load-more").show();
                }
                $("input.curPage").val(curPage);
            }
            if (html == "") {
                $("input.isLoading").val(0);
                return false;
            } else {
                $(".order_ul").append(html);
                $("input.isLoading").val(1);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            $("input.isLoading").val(1);
        }
    });
}
