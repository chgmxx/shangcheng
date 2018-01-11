var userid = $("input.userid").val();

$(document).ready(function () {
    var is_specifica = $("input.is_specifica").val();
    var proPrice = 0;
    var costPrice = 0;
    var discount = $("input#discount").val() * 1;
    if (is_specifica == '1') {//该商品存在规格
        costPrice = $("input#invPrice").val() * 1;
    } else {
        costPrice = $("input#proPrice").val() * 1;
    }
//	 proPrice = costPrice*discount;
    proPrice = costPrice;
    var addOffer = $(".addOffer").val();
    if (addOffer != null && addOffer != "" && $(".addOffer").length > 0) {
        addOffer = addOffer * 1;
        addOffer = addOffer.toFixed(2);
        $(".addOffer").val(addOffer);
        $(".addOffer").attr("min", addOffer)
    }

    $(".priceclass").text((proPrice * 1).toFixed(2));
//	 var oldPrice = $("input#costPrice").val()* 1;
    var oldPrice = $("input#primary_price").val() * 1;
    /*if(oldPrice <= 0 || oldPrice < proPrice){
     $(".before-price").hide();
     }else{
     $(".before-price").show();
     }*/
    if (discount > 0 && discount < 1) {
        /* $(".price-box .before-price").show();*/
        costPrice = (costPrice * 1).toFixed(2);
        /*$(".price-box .before-price").html("原价：￥<span>"+costPrice+"</span>");*/
    }
    $(".shopping-icon").hide();
    var fMoney = $(".fMoney").val();
    if (fMoney != null && fMoney != "" && fMoney != undefined && fMoney * 1 > 0) {
        fMoney = (fMoney * 1).toFixed(2);
        $("span.pay").html("运费：￥" + fMoney);
    } else {
        $("span.pay").html("免运费");
    }
    var xids = $("#xids").val();
    var price = "";
    if (aucLength > 0) {//此商品是拍卖
        if (price == null || price == "") {
            price = $(".nowPrice").val();
        }
    }
    if (price != "") {
        var disPrice = (price * discount).toFixed(2);
        disPrice = price;
        $("#group_price").val(disPrice);
        /*$(".before-price").html("原价：￥<span>"+(proPrice*1).toFixed(2)+"</span>");*/
        $("#primary_price").val((proPrice * 1).toFixed(2));
        $("#prodect_price").val(disPrice);
        $("#attr_lay_price").html(disPrice);
        /*$(".start-price span").html($(".startPrice").val());
         $(".start-price span").html($(".startPrice").val());*/
        /*$(".start-price").show();*/
        if ($(".aStatus").val() != "0") {
            $(".priceclass").text(disPrice);
        } else {
            $(".priceclass").text($(".startPrice").val());
        }
        /*if(proPrice <= 0 || proPrice < disPrice){
         $(".before-price").hide();
         }else{
         $(".before-price").show();
         }*/
        var diffTimes = $(".diffTimes").val();
        timer(diffTimes);//倒计时
    }

    var status = $("#time-item").attr("status");

    var intDiff = $(".diffTimes").val() * 1;
    var status = $("#time-item").attr("status");
    if (intDiff <= 0) {
        $("#time-item").addClass("endTimes");
        $(".now-shop").addClass("isEnd");
    }
    if (status == 0 && $("input.startTimes").val() * 1 > 0) {
        $("#time-item").addClass("startTimes");
        $(".now-shop").addClass("noStart");
    }
});

function pageclick(obj) {
    if (obj == '' || obj == null || obj == undefined || obj == 0 || obj == '0') {
        alert("店面未设置微商城主页面或者微商城主页面已删除");
    } else {
        window.location.href = "mallPage/" + obj + "/79B4DE7C/pageIndex.do";
    }
}
var userid = $("input.userid").val();
//跳转到个人中心页面
function PersonalCenter() {
    window.location.href = "/mMember/79B4DE7C/toUser.do?member_id=" + memberId + "&uId=" + userid;
}
//跳转到购物车里
function shoppingcart() {
    window.location.href = "/mallPage/79B4DE7C/shoppingcare.do?member_id=" + memberId + "&uId=" + userid;
}
function addshopping() {
    var memberId = $(".memberId").val();
    if (memberId == null || memberId == "") {
        toLogin();
        return false;
    }
    var shopid = $("input#shopid").val();//获取商铺id
    var product_id = $("input#proid").val();//获取商品id
    var product_specificas = $("#xids").val();//产品规格,存多个规格，用;分开
    var product_speciname = $("#specifica_name").val();

    var product_num = $("#product_num").val();
    var price = $(".priceclass").html();
    var primary_price = $("#primary_price").val();
    var pro_code = $("#pro_code").val();
    var fade = $("#fade");
    var discount = $("#discount").val();
    fade.show();
    $.ajax({
        type: "post",
        url: "/mallPage/79B4DE7C/addshopping.do",
        data: {
            shopId: shopid,
            productId: product_id,
            productSpecificas: product_specificas,
            productNum: product_num,
            productSpeciname: product_speciname,
            price: price,
            primaryPrice: primary_price,
            proCode: pro_code,
            discount: discount
        },
        async: false,
        dataType: "json",
        success: function (data) {
            fade.hide();
            var error = data.error;
            if (error == 0) {
                var shopnum = $(".shopping-icon").text();//显示购物车该产品数量

                $(".shopping-icon").show();

                $(".shopping-icon").text(parseInt(shopnum) + parseInt(product_num));
                alert("商品已添加到购物车里");
            } else {
                alert("商品添加到购物车失败");
            }
        }
    })
}

function addshop_ping() {
    var shopid = $("input#shopid").val();//获取商铺id
    var product_id = $("input#proid").val();//获取商品id
    var product_num = $("#attr_lay_num").val();//获取数量
    if (isNaN(product_num)) {
        alert("购买数量请输入合法数字");
        return;
    }
    if (product_num == 0) {
        alert("购买数量不能为0");
        return;
    }
    var memberId = $(".memberId").val();
    if (memberId == null || memberId == "") {
        toLogin();
        return false;
    }
    var product_specificas = "";
    var product_speciname = "";
    var price = $("#attr_lay_price").text();
    $(".inv-item-active").each(function () {
        product_specificas += $(this).attr("option") + ",";
        product_speciname += $(this).attr("optionvalue") + ",";
    })
    if (product_specificas.length > 0) {
        product_specificas = product_specificas.substr(0, product_specificas.length - 1);
    }
    if (product_speciname.length > 0) {
        product_speciname = product_speciname.substr(0, product_speciname.length - 1);
    }
    var primary_price = $("#primary_price").val();
    var pro_code = $("#pro_code").val();
    var fade = $("#fade");
    var discount = $("#discount").val();
    fade.show();
    $.ajax({
        type: "post",
        url: "/mallPage/79B4DE7C/addshopping.do",
        data: {
            shopId: shopid,
            productId: product_id,
            productSpecificas: product_specificas,
            productNum: product_num,
            productSpeciname: product_speciname,
            price: price,
            primaryPrice: primary_price,
            proCode: pro_code,
            discount: discount
        },
        async: false,
        dataType: "json",
        success: function (data) {
            fade.hide();
            var error = data.error;
            if (error == 0) {
                var shopnum = $(".shopping-icon").text();
                $(".shopping-icon").show();
                $(".shopping-icon").text(parseInt(shopnum) + parseInt(product_num));
                alert("商品已添加到购物车里");
            } else {
                alert("商品添加到购物车失败");
            }
        }
    })
}


//拍卖倒计时
var timeIndex = window.setInterval(function () {
    var intDiff = $(".diffTimes").val() * 1;
    var status = $("#time-item").attr("status");
    if (status == 0) {
        var startTime = $("input.startTimes").val();
        if (startTime * 1 > 0) {
            intDiff = $("input.startTimes").val() * 1;
        } else if (startTime * 1 <= 0 && intDiff > 0) {
            if ($(".aStatus").val() == "0") {
                $(".aStatus").val(1);
            }
            $("#time-item").removeClass("startTimes");
            $(".now-shop").removeClass("noStart");
            var isMargin = $("#isMargin").val() * 1;
            if (isMargin == 1) {
                $(".now-shop a span").text("交保证金报名");
            } else {
                $(".now-shop a span").text("立即拍下");
            }
            $("div#time-item label").html("距离结束时间");
        } else {
            $(".aStatus").val(-1);
            $("#time-item").addClass("endTimes");
            $(".now-shop").addClass("isEnd");
        }
    }
    var times = timer(intDiff);

    $('.day_show').html(times[0]);
    $('.hour_show').html(times[1]);
    $('.minute_show').html(times[2]);
    $('.second_show').html(times[3]);

    var isStart = false;

    if (status == 0 && $("input.startTimes").val() * 1 > 0) {
        isStart = true;
        $("input.startTimes").val(intDiff - 1);
    } else if (intDiff > 0) {
        $(".diffTimes").val(intDiff - 1);
    }

    if (intDiff <= 0) {
        $("#time-item").addClass("endTimes");
        $(".now-shop").addClass("isEnd");
    }
    if (isStart) {
        $("#time-item").addClass("startTimes");
        $(".now-shop").addClass("noStart");
    }

    //计算降价拍的价格
    var aucType = $("#aucType").val() * 1;
    var lowerPriceTime = $(".lowerPriceTime").val() * 1;
    var nowPrice = $(".nowPrice").val() * 1;//当前价格

    if (lowerPriceTime > 0 && aucType == 1 && nowPrice > 0 && $(".aStatus").val() != "0") {
        var timeSec = $(".times").val() * 1;
        if (timeSec == 0) {
            //console.log(60+"---"+times[3]*1)
            timeSec = 60 - times[3] * 1;
        }
        var lowerPrice = $(".lowerPrice").val() * 1;//每分钟降低多少元
        var lowestPrice = $(".lowestPrice").val() * 1;//最低金额
        nowPrice = (nowPrice - lowerPrice).toFixed(2);

        if (timeSec / 60 == lowerPriceTime) {
            if (nowPrice > lowestPrice) {
                $(".nowPrice").val(nowPrice);
                $(".priceclass").html(nowPrice);
                $("#attr_lay_price").html(nowPrice);
                /*var addOffer = $(".addOffer").val();
                 var test = /^[0-9]{1,6}(\.\d{1,2})?$/;
                 if(!test.test(addOffer)){
                 addOffer = nowPrice;
                 }else if(addOffer*1 < nowPrice*1){
                 addOffer = nowPrice;
                 }
                 addOffer = (addOffer*1).toFixed(2);
                 console.log(addOffer)
                 $(".addOffer").val(addOffer);*/
                timeSec = 0;
            }
        } else {
            timeSec++;
        }
        $(".times").val(timeSec);
    }
}, 1000);

function timer(intDiff) {
    var day = 0,
        hour = 0,
        minute = 0,
        second = 0;
    if (intDiff > 0) {
        day = Math.floor(intDiff / (60 * 60 * 24));
        hour = Math.floor(intDiff / (60 * 60)) - (day * 24);
        minute = Math.floor(intDiff / 60) - (day * 24 * 60) - (hour * 60);
        second = Math.floor(intDiff) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60);
    } else {

    }
    if (minute <= 9) minute = '0' + minute;
    if (second <= 9) second = '0' + second;

    var times = Array();
    times[0] = day;
    times[1] = hour;
    times[2] = minute;
    times[3] = second;
    return times;
}

//加的效果
$(".add").click(function () {
    var n = $(this).prev().val();
    var num = parseInt(n) + 1;
    var stock = parseInt($("#inventory").text());
    if (num > stock) {
        return;
    }
    if (num == 0) {
        return;
    }
    $(this).prev().val(num);
});
//减的效果
$(".jian").click(function () {
    var n = $(this).next().val();
    var num = parseInt(n) - 1;
    if (num == 0) {
        return
    }
    $(this).next().val(num);
});
//点击规格事情，显示规格信息
$(".inv-item").click(function () {
    $(this).addClass("inv-item-active").siblings().removeClass("inv-item-active");
    var product_specificas = "";
    $(".inv-item-active").each(function () {
        product_specificas += $(this).attr("option") + ",";
    })
    if (product_specificas.length > 0) {
        product_specificas = product_specificas.substr(0, product_specificas.length - 1);
    }
    $(".guigePrice_value").each(function () {
        if ($(this).attr("option") == product_specificas) {
            var invNum = $(this).attr("optionnum");

            var gPrice = ($(this).attr("optionprice") * 1).toFixed(2);
            $("#attr_lay_price").text(gPrice);//自动向上加1
            $("#inventory").text(invNum);
            $("#primary_price").val($(this).attr("optionyuanprice"));
            $("#pro_code").val($(this).attr("optioncode"));
            if ($(this).attr("optionspecifica_img_id") == 0) {
                $("#attr_image").attr("src", $("#yuan_image_url").val());
            } else {
                $("#attr_image").attr("src", $(this).attr("optionimage"));
            }

            var oldPrice = $(this).attr("optionyuanprice");
            if (discount > 0 && discount < 1) {
                $(".before-price.pad").show();
                $(".before-price.pad span").html((oldPrice * 1).toFixed(2));
            }
            var price = "";
            var intNum = "";
            var specIds = $(this).attr("option");
            if (aucLength > 0) {
                price = $(".nowPrice").val();
            }
            oldPrice = $(this).attr("optionprice");
            if (price != null && price != "") {
                $(".sum span.before-price span").text("" + (oldPrice * 1).toFixed(2));
                $("#attr_lay_price").text((price * discount).toFixed(2));
            }
            if (oldPrice > price) {
                $(".sum span.before-price").show();
            } else {
                $(".sum span.before-price").hide();
            }
            if (invNum * 1 <= 0) {
                alert("请重新选择规格，该规格的库存为0");
            } else {
                var specName = "";
                var specIds = "";
                $(".inv-item-active").each(function () {
                    if (specIds != "") {
                        specIds += ",";
                        specName += ",";
                    }
                    specIds += $(this).attr("option");
                    specName += $(this).text();
                });
                if (specIds != "") {
                    $("#xids").val(specIds);//产品规格,存多个规格，用;分开
                    $("#specifica_name").val(specName);
                    $("i.specidsname").html(specName.replace(",", " "));
                    hideDiv();
                }
            }
        }
    })

});
//显示商品前，完善展现的信息
function showDiv() {
    var specifica_ids = $("#xids").val();
    $(".inv-item").removeClass("inv-item-active");
    if (specifica_ids != "" && specifica_ids != undefined && specifica_ids != null) {
        var specifica_id = specifica_ids.split(",");
        for (var i = 0; i < specifica_id.length; i++) {
            $(".inv-item").each(function () {
                if ($(this).attr("option") == specifica_id[i]) {
                    $(this).addClass("inv-item-active");
                }
            })
        }
        $(".guigePrice_value").each(function () {
            if ($(this).attr("option") == specifica_ids) {
                var gPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                $("#attr_lay_price").text(gPrice);
                $("#inventory").text($(this).attr("optionnum"));
                $("#primary_price").val($(this).attr("optionyuanprice"));
                $("#pro_code").val($(this).attr("optioncode"));
                if ($(this).attr("optionspecifica_img_id") == 0) {
                    $("#attr_image").attr("src", $("#yuan_image_url").val());
                } else {
                    $("#attr_image").attr("src", $(this).attr("optionimage"));
                }
                var oldPrice = $(this).attr("optionyuanprice");
                if (discount > 0 && discount < 1) {
                    $(".before-price.pad").show();
                    $(".before-price.pad span").html((oldPrice * 1).toFixed(2));
                }
                var price = "";
                var specIds = $(this).attr("option");
                if (aucLength > 0) {
                    price = $(".nowPrice").val();
                }
                oldPrice = $(this).attr("optionprice");
                if (price != null && price != "") {
                    $(".sum span.before-price span").text("" + (oldPrice * 1).toFixed(2));
                    /*$("#attr_lay_price").text((price*discount).toFixed(2));*/
                    $("#attr_lay_price").text((price * 1).toFixed(2));
                }
                if (oldPrice > price) {
                    $(".sum span.before-price").show();
                } else {
                    $(".sum span.before-price").hide();
                }
            }
        });
    }
    $("#fade").show();
    $(".attr-lay").show();
}
function hideDiv() {
    $("#fade").hide();
    $(".attr-lay").hide();
}

function sumbit() {
    $("#queryForm").submit();
}
var numDefault = 0;
$(".num").focus(function () {
    numDefault = $(this).val();
});
$(".num").keyup(function () {
    var val = $(this).val();
    var numTest = /^\d{0,6}$/;
    if (!numTest.test(val)) {
        $(this).val(numDefault);
    } else {
        numDefault = $(this).val();
    }
});

/**
 * 出价
 */
function addOffer() {
    if (status == 0 && $("input.startTimes").val() * 1 > 0) {
        alert("拍卖还未开始，请耐心等待");
    } else if ($(".shop-btn").hasClass("isEnd")) {
        alert("拍卖已结束，不能出价");
    } else {
        var addOffer = $(".addOffer").val();
        var nowPrice = $(".nowPrice").val();
        var addPrice = $("#addPrice").val();
        var test = /^[0-9]{1,6}(\.\d{1,2})?$/;
        if (!test.test(addOffer)) {
            alert("请输入大于0的6小位数");
        } else if (addOffer * 1 == 0) {
            alert("请输入大于0的6小位数");
        } else if (addOffer * 1 < nowPrice * 1) {
            alert("不能低于当前价");
        } else {
            var memberId = $(".memberId").val();
            if (memberId == null || memberId == "") {
                toLogin();
                return false;
            }
            var aucId = $("input.aucId").val();
            var proId = $("input#proid").val();
            var product_specificas = $("#xids").val();//产品规格,存多个规格，用;分开
            var image_url = $("#attr_image").attr("src");
            image_url = image_url.split("/upload/")[1];

            var obj = {
                aucId: aucId,
                proId: proId,
                offerMoney: addOffer
            };
            var bid = {
                aucId: aucId,
                proId: proId,
                proSpecificaIds: product_specificas,
                proName: $(".product_nameclass").text(),
                proImgUrl: image_url,
                aucPrice: addOffer
            };
            var data = {
                offer: JSON.stringify(obj),
                bid: JSON.stringify(bid)
            }
            var index = layer.open({
                title: "",
                content: "",
                type: 2,
                shadeClose: false
            });
            $.ajax({
                url: "/mAuction/79B4DE7C/addOffer.do",
                type: "POST",
                data: data,
                timeout: 300000,
                dataType: "json",
                success: function (data) {
                    layer.closeAll();
                    if (data != null) {
                        if (data.result == "true") {
                            msg = "出价成功";
                        } else {
                            msg = data.msg;
                        }
                        alert(msg);
                        location.href = window.location.href;
                    }
                }, error: function () {
                    alert("出价失败，稍后请重新出价");
                    layer.closeAll();
                }
            });

        }
    }
}
function margins() {
    var bol = true;
    var aucId = $(".aucId").val();
    var id = $("#proid").val();
    var specIds = $("#xids").val();
    var obj = $(".guigePrice_value[option='" + specIds + "']");
    var invId = obj.attr("optionInvId");
    if (specIds == null || specIds == "") {
        invId = 0;
    } else {
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
    var memberId = $(".memberId").val();
    if (memberId == null || memberId == "") {
        toLogin();
        return false;
    }
    if (bol) {
        location.href = "/mAuction/" + id + "/" + invId + "/" + aucId + "/79B4DE7C/toAddMargin.do?uId=" + userid;
    }
}
$(".bid-change.add").click(function () {
    var addOffer = $(".addOffer").val();
    if (addOffer != null && addOffer != "" && $(".addOffer").length > 0) {
        var addPrice = $("#addPrice").val();
        addOffer = addOffer * 1 + addPrice * 1;
        $(".addOffer").val(addOffer.toFixed(2));
    }
});
$(".bid-change.jian").click(function () {
    var addOffer = $(".addOffer").val();
    var nowPrice = $(".nowPrice").val();
    var minPrice = $(".addOffer").attr("min");
    if (addOffer != null && addOffer != "" && $(".addOffer").length > 0) {
        var addPrice = $("#addPrice").val();
        nowNewPrice = (nowPrice * 1 + addPrice * 1).toFixed(2);//当前价
        addOffer = (addOffer * 1 - addPrice * 1).toFixed(2);
        if (addOffer >= nowNewPrice || (addOffer >= nowPrice && addOffer >= minPrice)) {
            $(".addOffer").val(addOffer);
        }
    }
});

function hideImgDiv() {
    $("#fade1").hide();
    $(".imgDivAttr").hide();
    $(".msgDivAttr").hide();
}
function showImgDiv() {
    $("#fade1").show();
    $(".imgDivAttr").show();
}
function showMessage(obj) {
    $(".msgDivAttr .msgDiv").html($(obj).find(".product_message").val());
    $("#fade1").show();
    $(".msgDivAttr").show();
}
$(".spec-img img").click(function () {
    var src = $(this).attr("src");
    if (src != null && src != "") {
        $(".imgDivAttr .imgDiv").html("<img class='img' src='" + $(this).attr("src") + "'/>");
        showImgDiv();
    }
});
$(".imgDivAttr .imgDiv").click(function (e) {
    if (e.target.className != "img") {
        hideImgDiv();
    }
});