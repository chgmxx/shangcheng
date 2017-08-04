/*=====================================搜索框===================================*/
$(".s-input").on("click", function (event) {
//	event.preventDefault();
//	event.stopPropagation();
    var tar = $(event.target);
    if (isChild(tar, $(".srh-result")) || isChild(tar, $(".s-input"))) {
        return;
    } else {
        //$(".srh-result").hide().siblings("div.m-layer").hide();
    }

    if (isChild(tar, $(".dus-icon")) || isChild(tar, $(".cd-popup-container"))) {
        return;
    } else {
        //$("div.cd-popup").hide();
    }
});
$(".m-layer").click(function () {
    $(".srh-result").hide().siblings("div.m-layer").hide();
});
/*文本框获取脚垫*/
function focusIn(obj) {
    var parent = $("div.srh-result");
    parent.show().siblings("div.m-layer").show();
    parent.find("div.srh-init").show().siblings("div.srh-res").hide();
}
/*文本框输入值变化*/
function inputChange(obj) {
    var value = $(obj).val();
    var parent = $("div.srh-result");
    if (value.trim()) {
        parent.find("div.srh-init").hide().siblings("div.srh-res").show();
    } else {
        parent.find("div.srh-init").show().siblings("div.srh-res").hide();
    }
}
/**
 * 清空历史搜索
 */
function isDust() {

    //$("div.srh-result").hide().siblings("div.m-layer").hide();
    $("div.cd-popup").show();
    var shopid = $("input.shopid").val();

    if (confirm("确认要清空历史搜索")) {
        $.ajax({
            url: "/phoneProduct/79B4DE7C/clearSearchGroup.do",
            type: "POST",
            data: {
                shopId: shopid
            },
            timeout: 300000,
            dataType: "json",
            success: function (data) {
                var html = "";
                if (data != null) {
                    if (data.result) {
                        $(".historyDiv").hide();
                    } else {
                        alert("清空历史搜索异常，请稍后重试");
                    }
                }
            },
            error: function () {
                alert("清空历史搜索异常，请稍后重试");
            }
        });
    }
}
//取消删除历史记录
function cdCancel(obj) {
    $(obj).parents(".cd-popup").hide();
}
function isChild(obj1, obj2) {
    return obj1.closest(obj2).length > 0;
}

function groupChild(obj, groupId) {
    $("#sort1Main").hide();
    $(".childGroup").show();

    getChildGroup(groupId);
}
function getChildGroup(groupId) {
    var rType = $("input.rType").val();
    var type = $("input.type").val();
    var urls = $("input.urls").val();
    $.ajax({
        url: "/phoneProduct/79B4DE7C/getChildGroup.do",
        type: "POST",
        data: {
            groupPId: groupId
        },
        timeout: 300000,
        dataType: "json",
        success: function (data) {
            var type = $("input.type").val();
            var html = "";
            if (data != null) {
                var imgHttp = data.http;
                if (data.childList != null) {
                    for (var i = 0; i < data.childList.length; i++) {
                        var child = data.childList[i];
                        if (urls == null || urls == "") {
                            urls = "/mallPage/" + child.shopId + "/79B4DE7C/shoppingall.do";
                        }
                        html += '<li>';
                        html += '<a href="' + urls + '?type=' + type + '&groupId=' + child.id + '&rType=' + rType + '">';
                        html += '<div class="itemBg">';
                        if (child.image_url == null || child.image_url == "") {
                            html += '<i class="bg-icon item-icon" >无图片</i>';
                        } else {
                            var img = imgHttp + child.image_url;
                            html += '<i class="bg-icon item-icon" style="background: url(' + img + ') no-repeat center center; background-size: contain;"></i>';
                        }
                        html += '</div>';
                        html += '	<p class="txt-overflow">' + child.groupName + '</p>';
                        html += '</a>';
                        html += '</li>';
                    }
                }
            }

            if (html == null || html == "") {
                var desc = $("input.desc").val();
                var type = $("input.type").val();
                queryurl(type, desc, groupId);
            } else {
                $(".childGroup .itemUl").html(html);
                $(".childGroup .itemUl").css("background", "none");
            }

        },
        error: function () {
        }
    });
}

/*向上按钮 刚开始隐藏  超过一定高度显示*/
$(".productList").scroll(function () {
    var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
    if (scrollTop == 0) {
        scrollTop = $(".productList").scrollTop();
    }
    if (scrollTop > 200) {
        $('.top').show();
    }
    else {
        $('.top').hide();
    }
});
/*返回顶部*/
$('.top').hide();
$('.top').click(function () {
    $(".productList").scrollTop(0);
});

function searchProduct(groupName) {
    var type = $(".type").val();
    var groupId = $(".groupId").val();
    var url = $("input.urls").val() + "?type=" + type + "&&desc=0";
    if (groupId != null && groupId != "") {
        url += "&&groupId=" + groupId;
    }
    var proName = $("#proName").val();
    if (groupName != null && groupName != "") {
        proName = groupName;
    }
    if (proName != null && proName != "" && proName != undefined) {
        url += "&&proName=" + proName;
    }
    var rType = $(".rType").val();
    if (rType != null && rType != "" && rType != "0") {
        url += '&&rType=' + rType;
    }
    window.location.href = url;
}

jQuery(document).ready(function ($) {
    if ($("span.img-container").length > 0) {
        if ($("span.img-container").attr("data-original") != null) {
            $("span.img-container").lazyload({
                effect: "fadeIn",
                container: $(".productList"),
                threshold: 200
            });
        }
    }
});

$(function () {

    $("div.fixRig").css({
        "right": "0.5rem"
    });
    $("div.fixRig a").css({
        "width": "1.12rem",
        "height": "1.12rem",
        "background-size": "auto 0.6rem"
    });
    $(".last-item").click(function () {
        var _href = $("#link").attr("href");
        if (_href === "/css/mall/shoppingall/indexModify2.css") {
            $("#link").attr("href", "/css/mall/shoppingall/indexModify1.css");
        }
        else {
            $("#link").attr("href", "/css/mall/shoppingall/indexModify2.css");
        }
    });
    /*左侧菜单切换*/
    $('.sortList li').click(function () {
        var sortId = $(this).attr('id');
        var sortMain = sortId + 'Main';
        $('.sortList li').removeClass('sortCur');
        $(this).addClass('sortCur').siblings().removeClass('sortCur');
    });

    $(".sort-div").click(function () {
        $(this).find("ul").toggle();
    })
    $(".product-info").each(function () {
        var price = $(this).find(".price").html();
        if (price != null && price != "") {
            var a = price.split(".");
            $(this).find(".price").html(a[0] + ".<em>" + a[1] + "</em>");
        }
        var hyprice = $(this).find(".hyPrice").html();
        if (hyprice != null && hyprice != "") {
            var a = hyprice.split(".");
            $(this).find(".hyPrice").html(a[0] + ".<em>" + a[1] + "</em>");
        }

        var old = $(this).find(".old").html();
        if (old != null && old != "") {
            var a = old.split(".");
            $(this).find(".old").html(a[0] + ".<em>" + a[1] + "</em>");
        }
    });

});