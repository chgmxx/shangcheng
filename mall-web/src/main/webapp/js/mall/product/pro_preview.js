var parentWin = window.parent.document;
/**
 * 初始化预览
 */
function loadProPreView() {

    var shopName = $(parentWin).find(".shop-contain option:selected").text();// 店铺名称
    $(".sName").html(shopName);
    var specifica = $(parentWin).find(".controls .spec-contain");// 商品规格
    var specText = "";
    // 遍历规格名称
    specifica.each(function () {
        if (specText != "") {
            specText += ",";
        }
        specText += $(this).find("option:selected").text();
    });
    if (specText != "") {
        $(".spec").html(specText);
    }
    // 规格对应的价格
    var invenDefault = $(parentWin).find(".js-default:checked").parents("tr");
    var price = invenDefault.find(".js-price").val();
    if (typeof (price) != "undefined") {
        if (price == null || price == "") {
            price = "0";
        }
        $(".pPrice").html(price);
    } else {
        $(".pPrice").html($(parentWin).find("input.proPrice").val());
    }
    // 原价
    var oldPrice = $(parentWin).find("input.proCostPrice").val();
    var priceFlag = false;
    if (oldPrice != "") {
        if (oldPrice * 1 > 0) {
            $("span.oldPrice").html("￥" + oldPrice);
            priceFlag = true;
        }
    }
    if (!priceFlag) {
        $("span.oldPrice").parent().hide();
    }
    // 商品名称
    var proName = $(parentWin).find("input.proName").val();
    if (proName != null && proName != "") {
        $(".pName").html(proName);
    }
    // 商品图片
    var proImgHtml = "";
    var imgObj = $(parentWin).find(".picture-list li img");
    // 遍历商品图片
    imgObj.each(function () {
        var src = $(this).attr("src");
        proImgHtml += "<div class=\"swiper-slide\"><img src=\"" + src
            + "\" /></div>";
    });
    if (proImgHtml != "") {
        $("span.imgSize").html(imgObj.length);
        //$(".swiper-wrapper").html(proImgHtml);
    }
    if (proName != null && proName != "") {
        $(".pName").html(proName);
    }


    /**
     * 绑定输入商品名称事件
     */
    $(parentWin).find("input.proName").keyup(function () {
        $(".pName").html($(this).val());
    });
    /**
     * 绑定商铺选择事件
     */
    $(parentWin).find(".shop-contain").change(function () {
        var shopName = $(this).find("option:selected").text();
        ;
        $(".sName").html(shopName);
    });

    /**
     * 绑定输入价格事件
     */
    $(parentWin).find(".proPrice").keyup(function () {
        var invenPrice = $(parentWin).find(".js-price").length;
        if (invenPrice == 0) {
            $(".pPrice").html($(this).val());
        }

    });
    /**
     * 绑定输入原价事件
     */
    $(parentWin).find(".proCostPrice").keyup(function () {
        if ($(this).val() != "") {
            $(".oldPrice").html($(this).val());
            $(".oldPrice").parent().show();
        }

    });


}


