var memberId = $(".memberId").val();
var userid = $(".userid").val();
/**
 * 交纳预售定金
 */
function presale(type) {
    var bol = true;
    var presaleId = $(".presaleId").val();
    var id = $("#proid").val();
    var specIds = $("#xids").val();
    var obj = $(".guigePrice_value[option='" + specIds + "']");
    var invId = obj.attr("optionInvId");
    var invNum = $("input#attr_lay_num").val();
    var isSpec = $("#isSpec").val();
    if (isSpec === "1" && type === 1) {
        showDiv();
        return;
    }
    if (specIds == null || specIds == "") {
        invId = 0;
    } else {
        //invNum = obj.attr("optionnum");
        if (obj.attr("optionnum") == "" || obj.attr("optionnum") == "0") {
            bol = false;
            alert("该商品规格的库存为0，请重新选择规格");
            return;
        }
    }
    var stockTotal = $(".stockTotal").val();
    if (stockTotal == "" || stockTotal == "0") {
        bol = false;
        alert("该商品已经卖完了，暂不能交纳保证金");
        return;
    }
    //限购判断
    if ($("input.buyNums").length > 0 && $("input.maxNum").length > 0) {
        var buyNums = $("input.buyNums").val() * 1;
        var maxNum = $("input.maxNum").val() * 1;
        if (buyNums > 0 && maxNum > 0 && maxNum <= buyNums + invNum) {
            alert("每人限购" + maxNum + "件，您已超过每人购买次数限制");
            return;
        }
    }
    if (memberId == null || memberId == "") {
        toLogin();
        return false;
    }
    var product_specificas = "";
    $(".inv-item-active").each(function () {
        product_specificas += $(this).attr("option") + ",";
    })
    if (product_specificas.length > 0) {
        product_specificas = product_specificas.substr(0, product_specificas.length - 1);
    }
    if (bol) {
        var price = $("#attr_lay_price").text();
        if (type == 1 || price == null || price == "" || typeof(price) == "undefined") {
            price = $(".priceclass").text();
        }
        if (type == 2) {
            invNum = $("#attr_lay_num").val();
        }
        var userid = $("input.userid").val();
        location.href = "/phonePresale/" + id + "/" + invId + "/" + presaleId + "/79B4DE7C/toAddDeposit.do?oMoney=" + price + "&num=" + invNum + "&specId=" + product_specificas + "&uId=" + userid;
    }
}

function getSalePrice(proPrice) {
    console.log(proPrice)
    if ($("input.commissionType").length > 0) {
        var commissionType = $("input.commissionType").val();
        var commissionRate = $("input.commissionRate").val();
        var commissionMoney = 0;
        if (commissionType == 1) {//按百分比
            commissionMoney = proPrice * (commissionRate / 100);
            commissionMoney = Math.round(commissionMoney * 100) / 100;
            if (commissionMoney * 1 <= 0) {
                commissionMoney = 0.01;
            }
        } else {//按固定金额
            commissionMoney = commissionRate;
        }
        return commissionMoney * 1;
    }
    return 0;
}

function getPresalePrice(proPrice) {
    if ($("input.presalePriceType").length > 0) {
        presalePriceType = $("input.presalePriceType").val();
    }
    if (proPrice != null && proPrice != "") {
        proPrice = proPrice * 1;
    }
    var presaleSaleType = $("input.presaleSaleType").val();
    var presaleDiscount = $("input.presaleDiscount").val() * 1;
    //var proPrice =  $(".priceclass").text()*1;
    if (presaleSaleType == 1) {//上调价格
        if (presalePriceType == 2) {
            proPrice = proPrice + presaleDiscount
        } else {
            proPrice = proPrice + (proPrice * (presaleDiscount));
        }
    } else {//下调价格
        if (presalePriceType == 2) {
            proPrice = proPrice - presaleDiscount;
        } else {
            proPrice = proPrice - (proPrice * (presaleDiscount));
        }
    }
    proPrice = Math.round(proPrice * 100) / 100;
    return proPrice;
}
