/**
 * 优惠计算
 * @param type 1 优惠券 2 粉币 3 积分  4 联盟 0 计算会员卡
 */
function jisuan(type, couponObj, flag) {
    if (type === 2 || type === 3) {
        clickJifenFenbiHideYouhui(type);
    } else if (type === 1) { // 使用优惠券
        couponShow(couponObj, flag);
    } else if (type == 4) {
        unionDiscount(couponObj)
    }
    var memberId = $("input.memberId").val();
    var useFenbi = $("#isFenbi").val();
    var userJifen = $("#isJifen").val();
    var totalMoney = $("#proMoneyAll").val();

    var orderObj = {
        "productAllMoney": $("#proMoneyAll").val()
    };


    var couponList = [];


    $(".couponDivs").each(function () {
        var _this = $(this);
        var wxShopId = _this.attr("wxShopId");
        var shopId = _this.attr("stoId");
        var useCoupon = 0;
        if ($(this).hasClass("selected")) {
            useCoupon = 1;
        }

        var sObj = {
            "wxShopId": wxShopId, //门店id
            "shopId": shopId, //店铺id
            "useCoupon": useCoupon //是否使用优惠券  0未使用 1使用
        };
        if (useCoupon === 1) {
            var couponObj = _this.find(".couponJson").val();
            if (couponObj != null && couponObj != "") {
                couponObj = JSON.parse(couponObj);
            }
            sObj.couponType = couponObj.couponType;//优惠券类型 0微信 1多粉优惠券
            sObj.coupondId = couponObj.cardId;//卡券id
            sObj.cardType = couponObj.cardType;//卡券类型

            /* $(this).find("input[name='useCoupon']").val(1);*/


            couponList[couponList.length] = sObj;
            $("#couponList").val(JSON.stringify(couponList));
        } else {
            /* $(this).find("input[name='useCoupon']").val(0);*/
        }
    });


    var data = getSubmitParams();

    $.ajax({
        url: "/phoneOrder/79B4DE7C/calculateOrder.do",
        type: "POST",
        data: data,
        timeout: 300000,
        dataType: "json",
        success: function (data) {
            if (data.code === 1) {


                $("#hy").text(data.discountMemberMoney);
                $("#yhj").text(data.discountConponMoney);
                if (data.userJifen === 1) {
                    if (data.canUseJifen === 1) {//能使用
                        /* $(".jifen_open_span .pay-explan .num").html(data.jifenNum);
                         $(".jifen_open_span .pay-explan .money").html(data.discountjifenMoney);*/

                        $("#jf").text(data.discountjifenMoney);
                        showHideJifen(3, true);
                    } else {
                        showHideJifen(3, false);
                    }
                } else if ($(".jifen_open_span").length > 0) {
                    $("#isJifen").val(0);
                    $("#fb").text(0);

                    showHideJifen(3, true);
                }
                if (data.useFenbi === 1) {//能使用
                    if (data.canUsefenbi === 1) {//能使用
                        /*$(".fenbi_open_span .pay-explan .num").html(data.fenbiNum);
                         $(".fenbi_open_span .pay-explan .money").html(data.discountfenbiMoney);*/

                        $("#fb").text(data.discountfenbiMoney);
                        showHideJifen(2, true);
                    } else {
                        showHideJifen(2, false);
                    }
                } else if ($(".fenbi_open_span").length > 0) {
                    $("#isFenbi").val(0);
                    $("#fb").text(0);
                    showHideJifen(2, true);
                }
                if ($("#lm").length > 0) {
                    $("#lm").text(data.leagueDiscount);
                }
                var balanceMoney = 0;
                if (data.balanceMoney != null && data.balanceMoney != "") {
                    balanceMoney = data.balanceMoney * 1;
                }
                var freight = 0;
                if ($("#orderFreightMoney").val() != null && $("#orderFreightMoney").val() != "") {
                    freight = $("#orderFreightMoney").val() * 1;
                }
                var youhui = data.balanceMoney;


                $("#sum-money").text((  freight + youhui).toFixed(2));

                return true;
            } else {
                console.log("计算失败");
                return false;
            }
        }, error: function () {
            console.log("计算失败");
            return false;
        }
    });


    hideLay();

}

function showHideJifen(type, flag) {
    if (type === 2) {
        if (flag) {
            $(".fenbi_open_span").show();
            $(".fenbi_noopen_span").hide();
            var isFenbi = $("#isFenbi").val();
            if(isFenbi === "1"){
                $(".fenbiyouhui").addClass("slide_on");
            }else{
                $(".fenbiyouhui").removeClass("slide_on");
            }
        } else {
            $(".fenbi_open_span").hide();
            $(".fenbi_noopen_span").show();
            $(".fenbi_noopen_span .pay-explan").text("不可用");

            $("#fb").text(0);
            $("#isFenbi").val(0);
        }
    } else {
        if (flag) {
            $(".jifen_open_span").show();
            $(".jifen_noopen_span").hide();
            var isJifen = $("#isJifen").val();
            if(isJifen === "1"){
                $(".jifenyouhui").addClass("slide_on");
            }else{
                $(".jifenyouhui").removeClass("slide_on");
            }
        } else {
            $(".jifen_open_span").hide();
            $(".jifen_noopen_span").show();
            $(".jifen_noopen_span .pay-explan").text("不可用");

            $("#jf").text(0);
            $("#isJifen").val(0);
        }
    }
}


/**
 * 积分粉币
 * @param tag
 */
function clickJifenFenbiHideYouhui(tag) {
    if (tag === 2) {//粉币
        if ($(".fenbiyouhui").hasClass("slide_on")) {
            $(".fenbiyouhui").removeClass("slide_on");
            $("#isFenbi").val(0);
        } else {
            $(".fenbiyouhui").addClass("slide_on");
            $("#isFenbi").val(1);
        }
    } else if (tag === 3) {//积分
        if ($(".jifenyouhui").hasClass("slide_on")) {
            $(".jifenyouhui").removeClass("slide_on");
            $("#isJifen").val(0);
        } else {
            $(".jifenyouhui").addClass("slide_on");
            $("#isJifen").val(1);
        }
    }
}
/**
 * 优惠券
 */
function couponShow(obj, tag) {
    var parentObj = $(obj).parents("#couponDiv");
    var shopId = parentObj.attr("stoId");
    var wxShopId = $(obj).attr("wxShopId");


    var val = $(obj).find("#cardCode").val() || "";
    var boolen = false;
    $(".useCoupon").each(function () {
        var _wxShopId = $(this).parents("#couponDiv").attr("wxShopId");
        if ($(this).attr("dataid") === val && val !== "" && wxShopId !== _wxShopId) {
            boolen = true;
        }
    });
    if (boolen) {
        alert("该优惠券已被选用");
        return;
    }
    $(obj).parents(".couponDivs").find("#useCoupon").attr("dataid", val);


    if (tag === 0) {
        $(obj).parent().find(".selected").removeClass("selected");
        parentObj.find("#useCoupon").html("");
        parentObj.removeClass("selected");
        parentObj.find(".couponJson").val("");


        var coupon = {};
        var cArr = $("#couponList").val();
        if (cArr !== null && cArr !== "") {
            coupon = JSON.parse(cArr);
        }
        delete coupon[shopId];
        if (isEmptyObject(coupon)) {
            $("#couponList").val("");
        } else {
            $("#couponList").val(JSON.stringify(coupon));
        }
    } else {
        var code = $(obj).find("#cardCode").val();
        var type = $(obj).find("#couponType").val();
        var discount = 0;
        if (type === "CASH" || type === 1) {//满减券(cash是微信,1是多粉)
            var explain = $(obj).find("#couponExplain").html();
            $(obj).parents(".couponDivs").find("#useCoupon").html(explain);
        } else {//折扣券
            var explain = $(obj).find("#couponExplain").html();
            $(obj).parents(".couponDivs").find("#useCoupon").html(explain);
            discount = $(obj).find("#discount").val();

        }
        $(obj).parent().find(".lay-item").removeClass("selected");
        $(obj).addClass("selected");
        $(obj).parents("#couponDiv").addClass("selected");
        var couponType = $(obj).find("#couponType").val();
        var id = $(obj).find("#cardId").val();

        var couponObj = {"couponCode": code, "shopId": shopId, "wxShopId": wxShopId, "cardId": id, "couponType": couponType};
        if (type === "CASH" || type === "DISCOUNT") {//折扣
            couponObj.discountCoupon = discount;
            couponObj.cardType = 0;
        } else {
            couponObj.cardType = type;
        }
        parentObj.find(".couponJson").val(JSON.stringify(couponObj));
    }
}

/**
 * 计算联盟卡的折扣
 */
function unionDiscount(obj) {
    if (obj === "" || obj === null) {
        $("input.unionDiscountVal").val("");
        $("#unionSpan").html("您还没选择优惠");
    } else {
        var unionDiscount = $(obj).find("#unionDiscountSpan").text();
        if (unionDiscount !== null && unionDiscount !== "") {
            $("#unionSpan").html(unionDiscount + "折");
            $("input.unionDiscountVal").val(unionDiscount);
        }
    }
    hideLay();
}