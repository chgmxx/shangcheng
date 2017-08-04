﻿
var memberId = $(".memberId").val();
var userid = $(".userid").val();
/*商品详情  规格参数  评价切换*/
$('.moduleChange a').click(function () {
    var liId = $(this).attr('id');
    var mainId = liId + "Main";
    $('.moduleChange a').removeClass('cur');
    $(this).addClass('cur').siblings().removeClass('cur');
    $('.changeMain').hide();
    $('#' + mainId).show();

    if (liId == "nav2") {//查询规格参数
        if (!$(".paramsDiv").hasClass("selDiv")) {
            getProductParams();
        }
    } else if (liId == "nav3") {//查询评价
        if (!$(".commentDiv").hasClass("selDiv")) {
            getProductComment("assess1Main", null);
        }
    }

});

/* 评价切换 */
$('.assess a').click(function () {
    var liId = $(this).attr('id');
    var mainId = liId + "Main";
    $('.assess a').removeClass('curAssess');
    $(this).addClass('curAssess').siblings().removeClass('curAssess');
    $('.assessMain').hide();
    $('#' + mainId).show();
    if (!$("#" + mainId).hasClass("selDiv")) {
        getProductComment(mainId, $(this).find("em").attr("id"));
    }
});

function collectPro(status, id) {
    var memberId = $("input.memberId").val();
    if (memberId == null || memberId == "") {
        toLogin();
        return;
    }
    var isCollect = 0;
    if (status == 0) {
        isCollect = 1;
    }
    var obj = {
        productId: $("#proid").val(),
        isCollect: isCollect
    };
    if (id != null && id != "" && id != 0) {
        obj.id = id;
    }

    $.ajax({
        url: "mallPage/79B4DE7C/collection.do",
        type: "POST",
        data: {
            params: JSON.stringify(obj)
        },
        timeout: 300000,
        dataType: "json",
        success: function (data) {
            var flag = false;
            if (data != null) {
                flag = data.result;
                if (data.result) {
                    location.href = window.location.href;
                }
            }
            if (!flag) {
                alert("收藏失败，请稍后重新收藏");
            }
        },
        error: function () {
            alert("收藏失败");
        }
    });
}
/**
 * 查询商品参数
 */
function getProductParams() {
    var proId = $("#proid").val();
    $.ajax({
        url: "mallPage/79B4DE7C/getProductParams.do",
        type: "POST",
        data: {
            proId: proId
        },
        dataType: "json",
        success: function (data) {
            var html = "";
            if (data != null) {
                if (data.paramList != null) {
                    for (var i = 0; i < data.paramList.length; i++) {
                        var param = data.paramList[i];
                        html += '<li class="flex"><div>' + param.paramsName + '：</div><div>' + param.paramsValue + '</div></li>';
                    }
                }
            }
            if (html != "") {
                html = '<ul class="mainList">' + html + '</ul>';
                $(".paramsDiv").addClass("selDiv");
            }
            $(".paramsDiv").html(html);
            $(".paramsDiv").css("background", "none");
        },
        error: function () {
        }
    });

}


/**
 * 查询商品参数
 */
function getProductComment(cla, feel) {
    //console.log(cla)
    var proId = $("#proid").val();
    var obj = {
        proId: proId
    };
    if (feel != null && feel != "") {
        obj["feel"] = feel;
    }
    //console.log(obj)
    $.ajax({
        url: "mallPage/79B4DE7C/getProductComment.do",
        type: "POST",
        data: obj,
        dataType: "json",
        success: function (data) {
            //console.log(data)
            var html = "";
            var http = "";
            if (data != null) {
                //图片路径
                if (data.http != null && data.http != "") {
                    http = data.http;
                }
                if (data.maps != null) {
                    data = data.maps;
                }
                //总体评价数量
                if (data.countMap != null) {
                    var map = data.countMap;
                    $(".feelLi").each(function () {
                        var _em = $(this).find("em");
                        var id = _em.attr("id");
                        if (id != null && id != "" && typeof(id) != "undefinde") {
                            _em.html(map[id]);
                        }
                    });
                }
                //评论
                if (data.commentList != null) {
                    for (var i = 0; i < data.commentList.length; i++) {
                        var comment = data.commentList[i];
                        html += '<li>'
                            + '<div class="itemInfo flex">'
                            + '<img src="' + (comment.headimgurl) + '" onerror="this.src=\'/images/mall/img/pt-detail2.jpg\'" onclick="showCommentImg(this);">'
                            + '<div>'
                            + '<p>' + comment.nickname + '</p>'
                            + '<p class="grey-text">' + comment.createtime + ' ';
                        if (comment.spec != null && comment.spec != "") {
                            html += comment.spec;
                        }
                        html += '</p>'
                            + '</div>'
                            + '</div>'
                            + '<div class="itemTxt">' + comment.content + '</div>'
                            + '<div class="itemPic">';
                        if (comment.imageList != null) {
                            for (var j = 0; j < comment.imageList.length; j++) {
                                var images = comment.imageList[j];
                                html += '<img src="' + (http + images.imageUrl) + '" onerror="this.src=\'/images/mall/img/pt-detail2.jpg\'" onclick="showCommentImg(this);">';
                            }
                        }
                        html += '</div>'
                            + '</li>';
                    }
                }
            }
            //console.log(html)
            if (html != "") {
                html = '<ul>' + html + '</ul>';
                $("#" + cla).addClass("selDiv");
            }
            $("#" + cla).css("background", "none");
            $("#" + cla).html(html);
        },
        error: function () {
        }
    });

}
function showCommentImg(obj) {
    $(".imgDivAttr .imgDiv").html("<img class='img' src='" + $(obj).attr("src") + "'/>");
    showImgDiv();
}
$(".fixRig").css("bottom", "30%");

/**
 * 進入超級銷售員分享頁面
 */
function showShares(state, msg, saleMemberId, productId) {
    var memberId = $("input.memberId").val();
    if (memberId == null || memberId == "") {
        toLogin();
        return;
    }
    var memberIds = $("input#memberIds").val();
    if (state != "1") {
        if (state == -1) {
            if (confirm(msg)) {
                location.href = "/phoneSellers/79B4DE7C/toApplySeller.do?member_id=" + memberIds + "&uId=" + userid;
            }
        } else {
            alert(msg);
        }
    } else {
        location.href = "/phoneSellers/" + saleMemberId + "/" + productId + "/79B4DE7C/shareSeller.do?uId=" + userid;
    }
}

//立即购买,立即开团
function productBuy(types) {
    var shopid = $("input#shopid").val();//获取商铺id
    var product_id = $("input#proid").val();//获取商品id
    var product_specificas = $("#xids").val();//产品规格,存多个规格，用;分开
    var product_speciname = $("#specifica_name").val();
    var product_num = 1;

    var proTypeId = $("#pro_type_id").val();

    var is_specifica = $("input#isSpec").val();

    if ($("#time-item").length > 0) {
        var status = $("#time-item").attr("status");
        if (status == 0 && $("#time-item").hasClass("startTimes")) {
            var tip = "秒杀活动";
            if (types == 6) {
                tip = "商品预售";
            }
            alert(tip + "还没开始，请耐心等待。");
            return;
        }
    }
    //限购判断
    if ($("input.buyNums").length > 0 && $("input.maxNum").length > 0) {
        var buyNums = $("input.buyNums").val() * 1;
        var maxNum = $("input.maxNum").val() * 1;
        if (buyNums > 0 && maxNum > 0 && maxNum <= buyNums + product_num) {
            alert("每人限购" + maxNum + "件，您已超过每人购买次数限制");
            return;
        }
    }
    var recevieGuoqi = $("input.recevieGuoqi").val();
    if (recevieGuoqi == 1 && proTypeId == 3 && cardType != "") {
        if (confirm("卡券包已过期不能购买，是否跳转到全部商品页面重新选购")) {
            location.href = "/mallPage/" + shopid + "/79B4DE7C/shoppingall.do";
        }
        return;
    }
    var price = $("#prodect_price").val();
    var product_name = $(".product_nameclass").text();//获取商品名;//获取商品名
    var shop_namemessg = $("#shop_name").val();
    var totalprice = 1 * $(".priceclass").text();
    $(".guigePrice_value").each(function () {
        if ($(this).attr("option") == product_specificas) {
            image_url = $(this).attr("optionimage");
        }
    })
    $(".guigePrice_value").each(function () {
        if ($(this).attr("option") == product_specificas) {
            var gPrice = ($(this).attr("optionprice") * 1).toFixed(2);
            $("#attr_lay_price").text(gPrice);
            $("#inventory").text($(this).attr("optionnum"));
            $("#primary_price").val($(this).attr("optionyuanprice"));
            $("#pro_code").val($(this).attr("optioncode"));
            if ($(this).attr("optionimage") == null || $(this).attr("optionimage") == "") {

            } else {
                $("#attr_image").attr("src", $(this).attr("optionimage"));
            }
        }
    })
    var image_url = $("#attr_image").attr("img");
    var primary_price = $("#primary_price").val();
    var pro_code = $("#pro_code").val();
    var return_day = $("#return_day").val();
    var discount = $("#discount").val();
    var groupBuyId = 0;//存放团购id
    var pJoinId = -1;
    var flag = true;
    var isCoupons = $("#isCoupons").val();
    var myIntegral = $(".myIntegral").val();
    var invNum = $(".inv-num span#inventory").text();
    if (product_num * 1 > $.trim(invNum) * 1) {
        alert("你购买的数量大于商品现有的库存，请重新输入购买数量");
        return;
    }
    if (types == 1) {//开团
        price = $("#group_price").val();
        //primary_price = $("#primary_price").val();
        groupBuyId = $(".groupBuyId").val();
        if (groupBuyId == undefined || groupBuyId == null || groupBuyId == "") {
            groupBuyId = 0;
        }
        pJoinId = 0;
        var groupMaxBuy = $("#groupMaxBuy").val();//团购商品限购
        var groupBuyCount = $("#groupBuyCount").val();//已经参加团购的数量
        if (groupMaxBuy != "" && groupMaxBuy != "0") {
            if (product_num * 1 > groupMaxBuy * 1 || groupBuyCount * 1 + product_num * 1 > groupMaxBuy * 1) {
                alert("每人限购" + groupMaxBuy + "件，您已超过每人购买次数限制");
                flag = false;
                return;
            }
        }
        var groupObj = $(".groupPrice_arr[spec='" + product_specificas + "']");
        if (groupObj.attr("isjoin") == "0") {
            alert("该规格没有参加团购，请重新选择规格");
            //showDiv();
            flag = false;
            return;
        }
    } else if (types == 3) {//秒杀
        price = $(".seckillPrice").val();
        //primary_price = price;
        groupBuyId = $(".seckillId").val();
        if (groupBuyId == undefined || groupBuyId == null || groupBuyId == "") {
            groupBuyId = 0;
        }
        pJoinId = 0;
        var groupMaxBuy = $("#seckillMaxBuy").val();//团购商品限购
        var groupBuyCount = $("#seckillCount").val();//已经参加团购的数量
        if (groupMaxBuy != "" && groupMaxBuy != "0") {
            if (product_num * 1 > groupMaxBuy * 1 || groupBuyCount * 1 + product_num * 1 > groupMaxBuy * 1) {
                alert("每人限购" + groupMaxBuy + "件，您已超过每人购买次数限制");
                flag = false;
                return;
            }
        }
        var groupObj = $(".seckillPrice_arr[spec='" + product_specificas + "']");
        if (groupObj.attr("isjoin") == "0") {
            alert("该规格没有参加秒杀，请重新选择规格");
            //showDiv();
            flag = false;
            return;
        }
    } else if (types == 6) {
        groupBuyId = $("input.presaleId").val();
        var pObj = $("input.presaleOrderPrice");
        if (pObj.length > 0) {
            if (pObj.val() != null && pObj.val() != "" && typeof(pObj.val()) != "undefined") {
                price = $("input.presaleOrderPrice").val();
                totalprice = price * product_num;
            }
        }
    } else {
        totalprice = price * product_num;
    }
    var maxBuy = $("#maxBuy").val();
    if (maxBuy != "" && maxBuy != "0") {
        if (product_num * 1 > maxBuy * 1) {
            alert("每人限购" + maxBuy + "件，超过每人购买次数限制");
            flag = false;
            return;
        }
    }
    if ($("input.isBuyFlag").length == 0) {
        var preInvNum = $("input.preInvNum").val();
        var invNum = $("input.prePrice_arr[spec='" + product_specificas + "']").attr("invNum");
        if (invNum == 0 || invNum < product_num - invNum) {
            alert("商品的库存不够，请重新选择规格");
            flag = false;
            return;
        }
        if (preInvNum == 0 || preInvNum < product_num - preInvNum) {
            alert("商品的库存不够请重新选择商品");
            flag = false;
            return;
        }
    }

    if (is_specifica == '1') {//该商品存在规格
        showDiv();
        return;
    }
    /*if(memberId == null || memberId == ""){
     toLogin();
     return false;
     }*/
    if (rType == 1 && isChange == 1 && integral != "") {//积分商品（从积分商城进来的）
        var jfFlag = true;
        if (myIntegral == null || myIntegral == "") {
            jfFlag = false;
        } else {
            if (integral * 1 > myIntegral * 1) {
                jfFlag = false;
            }
        }
        if (jfFlag) {
            primary_price = price;
            price = integral;
            totalprice = integral * parseInt(product_num);
            discount = 100;
        } else {
            alert("您的积分不足，不能兑换该产品");
            flag = false;
            return;
        }
    }
    var myFenbi = $(".myFenbi").val();
    if (rType == 2 && isChangeFenbi == 1 && changeFenbi != "") {//积分商品（从积分商城进来的）
        var jfFlag = true;
        if (myFenbi == null || myFenbi == "") {
            jfFlag = false;
        } else {
            if (changeFenbi * 1 > myFenbi * 1) {
                jfFlag = false;
            }
        }
        if (jfFlag) {
            primary_price = price;
            price = changeFenbi;
            totalprice = changeFenbi * parseInt(product_num);
            discount = 100;
        } else {
            alert("您的粉币不足，不能兑换该产品");
            flag = false;
            return;
        }
    }
    var memberType = $("#member_type").val();
    var cardType = $("#card_type").val();
    if (proTypeId == 2 && memberType != "") {//购买会员卡
        if ($(".buyCode").val() == "-1") {
            alert("您已经购买了会员卡，不能再次购买");
            flag = false;
            return;
        }
    } else if (proTypeId == 3 && cardType != "") {//购买卡券包
        if ($(".recevieMoney").length > 0) {
            price = $(".recevieMoney").val();
        } else {
            price = $(".priceclass").text();
        }
        totalprice = price * product_num;
        if ($("input.cardGuoqi").length > 0) {
            if (!confirm("您购买的卡券包里有卡券已过期，是否继续购买")) {
                flag = false;
                return;
            }
        }
    }
    var view = $(".view").val();
    if (view == "show") {
        alert("正在预览的商品不能购买");
        return;
    }
    if (flag) {
        //console.log(types+"==="+price)
        var hs = {product_id: product_id, product_specificas: product_specificas, totalnum: product_num, price: price, primary_price: primary_price, discount: discount};
        if (groupBuyId > 0) {
            hs["groupBuyId"] = groupBuyId;
        }
        if (pJoinId >= 0) {
            hs["pJoinId"] = pJoinId;
        }
        if (types != null && types != "" && types != "0") {
            hs["groupType"] = types;
        }
        //hs["totalprice"] = totalprice;
        //hs["image_url"]=image_url;
        //hs["product_speciname"]=product_speciname;
        //hs["shop_name"] = shop_namemessg;
        //hs["product_name"] = product_name;
        //hs["shop_id"]=shopid;
        //hs["pro_code"]=pro_code;
        //hs["return_day"]=return_day;
        //hs["isCoupons"]=isCoupons;
        //hs["is_member_discount"] = $(".is_member_discount").val();
        //hs["pro_type_id"] = $("#pro_type_id").val();
        //hs["member_type"] = $("#member_type").val();
        //hs["is_integral_deduction"] = $(".is_integral_deduction").val();
        //hs["is_fenbi_deduction"] = $(".is_fenbi_deduction").val();

        if ($(".saleMemberId").length > 0 && saleLen > 0) {
            var commission = $(".commission").text();
            if (commission != null && commission != "") {
                hs["saleMemberId"] = $("input.saleMemberId").val();
                hs["commission"] = commission;
            }
        }
        $("#json").val(JSON.stringify(hs));
        //console.log(hs)
        sumbit();
    }
}
function addshopping() {
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
    var stockTotal = $(".stockTotal").val();
    var is_specifica = $("input#isSpec").val();

    if (stockTotal == "" || stockTotal == "0") {
        alert("您购买商品的库存不够，请重新选择商品");
        return;
    }
    if (product_specificas != null && product_specificas != "") {

        var specObj = $(".guigePrice_value[option='" + product_specificas + "']");
        var invNums = specObj.attr("optionnum");
        if (invNums == null || invNums == "" || invNums == "0") {
            alert("您购买商品的库存不够，请重新选择商品");
            return;
        }
    }
    if (discount != null && discount != "" && discount != "1") {
        discount = discount * 1;
        price = price * discount;
    }
    if ($("input.isBuyFlag").length == 0) {
        var preInvNum = $("input.preInvNum").val();
        var invNum = $("input.prePrice_arr[spec='" + product_specificas + "']").attr("invNum");
        if (invNum == 0 || invNum < product_num - invNum) {
            alert("商品的库存不够，请重新选择规格");
            flag = false;
            return;
        }
        if (preInvNum == 0 || preInvNum < product_num - preInvNum) {
            alert("商品的库存不够请重新选择商品");
            flag = false;
            return;
        }
    }
    //限购判断
    if ($("input.buyNums").length > 0 && $("input.maxNum").length > 0) {
        var buyNums = $("input.buyNums").val() * 1;
        var maxNum = $("input.maxNum").val() * 1;
        if (buyNums > 0 && maxNum > 0 && maxNum <= buyNums + product_num * 1) {
            alert("每人限购" + maxNum + "件，您已超过每人购买次数限制");
            return;
        }
    }
    var maxBuy = $("#maxBuy").val();
    if (maxBuy != "" && maxBuy != "0") {
        if (product_num * 1 > maxBuy * 1) {
            alert("每人限购" + maxBuy + "件，超过每人购买次数限制");
            return;
        }
    }
    var data = {
        shopId: shopid,
        productId: product_id,
        productSpecificas: product_specificas,
        productNum: product_num,
        productSpeciname: product_speciname,
        price: price,
        primaryPrice: primary_price,
        proCode: pro_code,
        discount: discount
    };
    if ($(".saleMemberId").length > 0 && saleLen > 0) {
        var commission = $(".commission").text();
        if (commission != null && commission != "") {
            data["saleMemberId"] = $("input.saleMemberId").val();
            data["commission"] = commission;
        }
    }
    var view = $(".view").val();
    if (view == "show") {
        alert("正在预览的商品不能加入购物车");
        return;
    }

    if (is_specifica == '1') {//该商品存在规格
        showDiv();
        return;
    }

//	if(memberId == null || memberId == ""){
//		toLogin();
//		return false;
//	}
    fade.show();
    $.ajax({
        type: "post",
        url: "/mallPage/79B4DE7C/addshopping.do",
        data: data,
        async: false,
        dataType: "json",
        success: function (data) {
            fade.hide();
            var error = data.error;
            if (error == 0) {
                var shopnum = $(".shopping-icon").text();//显示购物车该产品数量

                $(".shopping-icon").show();

                $(".shopping-icon").text(parseInt(shopnum) + parseInt(product_num));
//					alert("商品已添加到购物车里");
                $(".addShopCartDiv").show();
                setTimeout(function () {
                    $(".addShopCartDiv").hide();
                }, 2000);
            } else if (error == -1) {
                toLogin();
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

    if (discount != null && discount != "" && discount != "1") {
        discount = discount * 1;
        price = price * discount;
    }
    var stockTotal = $(".stockTotal").val();
    if (stockTotal == "" || stockTotal == "0") {
        alert("您购买商品的库存不够，请重新选择商品");
        return;
    }
    if (product_specificas != null && product_specificas != "") {

        var specObj = $(".guigePrice_value[option='" + product_specificas + "']");
        var invNums = specObj.attr("optionnum");
        if (invNums == null || invNums == "" || invNums == "0") {
            alert("您购买商品的库存不够，请重新选择商品");
            return;
        }
    }

    if ($("input.isBuyFlag").length == 0) {
        var preInvNum = $("input.preInvNum").val();
        var invNum = $("input.prePrice_arr[spec='" + product_specificas + "']").attr("invNum");
        if (invNum == 0 || invNum < product_num - invNum) {
            alert("商品的库存不够，请重新选择规格");
            flag = false;
            return;
        }
        if (preInvNum == 0 || preInvNum < product_num - preInvNum) {
            alert("商品的库存不够请重新选择商品");
            flag = false;
            return;
        }
    }
    //限购判断
    if ($("input.buyNums").length > 0 && $("input.maxNum").length > 0) {
        var buyNums = $("input.buyNums").val() * 1;
        var maxNum = $("input.maxNum").val() * 1;
        if (buyNums > 0 && maxNum > 0 && maxNum <= buyNums + product_num * 1) {
            alert("每人限购" + maxNum + "件，您已超过每人购买次数限制");
            return;
        }
    }
    var maxBuy = $("#maxBuy").val();
    if (maxBuy != "" && maxBuy != "0") {
        if (product_num * 1 > maxBuy * 1) {
            alert("每人限购" + maxBuy + "件，超过每人购买次数限制");
            return;
        }
    }
    var data = {
        shopId: shopid,
        productId: product_id,
        productSpecificas: product_specificas,
        productNum: product_num,
        productSpeciname: product_speciname,
        price: price,
        primaryPrice: primary_price,
        proCode: pro_code,
        discount: discount
    };
    if ($(".saleMemberId").length > 0 && saleLen > 0) {
        var commission = $(".commission_lay").text();
        if (commission != null && commission != "") {
            data["saleMemberId"] = $("input.saleMemberId").val();
            data["commission"] = commission;
        }
    }
    var view = $(".view").val();
    if (view == "show") {
        alert("正在预览的商品不能加入购物车");
        return;
    }
//	if(memberId == null || memberId == ""){
//		toLogin();
//		return false;
//	}
    hideDiv();
    $.ajax({
        type: "post",
        url: "/mallPage/79B4DE7C/addshopping.do",
        data: data,
        async: false,
        dataType: "json",
        success: function (data) {
            fade.hide();
            var error = data.error;
            if (error == 0) {
                var shopnum = $(".shopping-icon").text();
                $(".shopping-icon").show();
                $(".shopping-icon").text(parseInt(shopnum) + parseInt(product_num));
//					alert("商品已添加到购物车里");
                $(".addShopCartDiv").show();
                setTimeout(function () {
                    $(".addShopCartDiv").hide();
                }, 2000);

            } else if (error == -1) {
                toLogin();
            } else {
                alert("商品添加到购物车失败");
            }
        }
    })
}

//立即购买,立即开团
function product_Buy(types) {
    if ($("#time-item").length > 0) {
        var status = $("#time-item").attr("status");
        if (status == 0 && $("#time-item").hasClass("startTimes")) {
            var tip = "秒杀活动";
            if (type == 6) {
                tip = "商品预售";
            }
            alert(tip + "还没开始，请耐心等待。");
            return;
        }
    }
    var proTypeId = $("#pro_type_id").val();
    var shopid = $("input#shopid").val();//获取商铺id
    var recevieGuoqi = $("input.recevieGuoqi").val();
    if (recevieGuoqi == 1 && proTypeId == 3 && cardType != "") {
        if (confirm("卡券包已过期不能购买，是否跳转到全部商品页面重新选购")) {
            location.href = "/mallPage/" + shopid + "/79B4DE7C/shoppingall.do";
        }
        return;
    }
    var product_id = $("#proid").val();//获取商品id
    var product_num = $("#attr_lay_num").val();//获取数量
    if (isNaN(product_num)) {
        alert("购买数量请输入合法数字");
        return;
    }
    if (product_num == 0) {
        alert("购买数量不能为0");
        return;
    }
    //限购判断
    if ($("input.buyNums").length > 0 && $("input.maxNum").length > 0) {
        var buyNums = $("input.buyNums").val() * 1;
        var maxNum = $("input.maxNum").val() * 1;
        if (buyNums > 0 && maxNum > 0 && maxNum <= buyNums + product_num * 1) {
            alert("每人限购" + maxNum + "件，您已超过每人购买次数限制");
            return;
        }
    }
    var product_specificas = "";
    var product_speciname = "";
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
    //console.log(product_specificas)
    var price = $("#attr_lay_price").text();
    var product_name = $(".product_nameclass").text();//获取商品名
    var shop_namemessg = $("#shop_name").val();
    var totalprice = parseInt(product_num) * price;
    var image_url = $("#attr_image").attr("img");
    var primary_price = $("#primary_price").val();
    var pro_code = $("#pro_code").val();
    var return_day = $("#return_day").val();
    var discount = $("#discount").val();
    var groupBuyId = 0;
    var pJoinId = -1;
    var flag = true;
    var myIntegral = $(".myIntegral").val();
    var isCoupons = $("#isCoupons").val();
    var invNum = $(".inv-num span#inventory").text();
    if (product_num * 1 > $.trim(invNum) * 1) {
        alert("你购买的数量大于商品现有的库存，请重新输入购买数量");
        return;
    }

    if (types == 1) {//立即开团
        groupBuyId = $(".groupBuyId").val();
        if (groupBuyId == undefined || groupBuyId == null || groupBuyId == "") {
            groupBuyId = 0;
        }
        pJoinId = 0;
        var groupMaxBuy = $("#groupMaxBuy").val();//团购商品限购
        var groupBuyCount = $("#groupBuyCount").val();//已经参加团购的数量
        if (groupMaxBuy != "" && groupMaxBuy != "0") {
            if (product_num * 1 > groupMaxBuy * 1 || groupBuyCount * 1 + product_num * 1 > groupMaxBuy * 1) {
                alert("每人限购" + groupMaxBuy + "件，您已超过每人购买次数限制");
                flag = false;
                return;
            }
        }
        var groupObj = $(".groupPrice_arr[spec='" + product_specificas + "']");
        if (groupObj.attr("isjoin") == "0") {
            alert("该规格没有参加团购，请重新选择规格");
            //showDiv();
            flag = false;
            return;
        }
        //primary_price = price;
    } else if (types == 3) {//秒杀
        groupBuyId = $(".seckillId").val();
        if (groupBuyId == undefined || groupBuyId == null || groupBuyId == "") {
            groupBuyId = 0;
        }
        pJoinId = 0;
        var groupMaxBuy = $("#seckillMaxBuy").val();//团购商品限购
        var groupBuyCount = $("#seckillCount").val();//已经参加团购的数量
        if (groupMaxBuy != "" && groupMaxBuy != "0") {
            if (product_num * 1 > groupMaxBuy * 1 || groupBuyCount * 1 + product_num * 1 > groupMaxBuy * 1) {
                alert("每人限购" + groupMaxBuy + "件，您已超过每人购买次数限制");
                flag = false;
                return;
            }
        }
        var groupObj = $(".seckillPrice_arr[spec='" + product_specificas + "']");
        if (groupObj.attr("isjoin") == "0") {
            alert("该规格没有参加秒杀，请重新选择规格");
            //showDiv();
            flag = false;
            return;
        }
        //primary_price = price;
    } else if (types == 6) {
        groupBuyId = $("input.presaleId").val();
        var pObj = $("input.presaleOrderPrice");
        if (pObj.length > 0) {
            if (pObj.val() != null && pObj.val() != "" && typeof(pObj.val()) != "undefined") {
                price = $("input.presaleOrderPrice").val();
                totalprice = price * product_num;
            }
        }
    } else {//立即购买
        if ($(".sum .before-price span").length > 0 && (groupLen > 0 || seckillLen > 0)) {
            price = $(".sum .before-price span").text();
            totalprice = parseInt(product_num) * price;
        }

        if (discount != null && discount != "") {
            if (discount * 1 > 0 && discount * 1 < 1) {
                //price = $(".attr-lay .huiyuan").text();
                price = price * discount;
                totalprice = parseInt(product_num) * price;
            }
        }
    }
    var maxBuy = $("#maxBuy").val();
    if (maxBuy != "" && maxBuy != "0") {
        if (product_num * 1 > maxBuy * 1) {
            alert("每人限购" + maxBuy + "件，超过每人购买次数限制");
            flag = false;
            return;
        }
    }
    if ($("input.isBuyFlag").length == 0) {
        var preInvNum = $("input.preInvNum").val();
        var invNum = $("input.prePrice_arr[spec='" + product_specificas + "']").attr("invNum");
        if (invNum == 0 || invNum < product_num - invNum) {
            alert("商品的库存不够，请重新选择规格");
            flag = false;
            return;
        }
        if (preInvNum == 0 || preInvNum < product_num - preInvNum) {
            alert("商品的库存不够请重新选择商品");
            flag = false;
            return;
        }
    }
    /*var toshop = 0;
     if($(".toshop").length > 0){
     toshop = $(".toshop").val();
     }
     if((memberId == null || memberId == "") && toshop == 0){
     toLogin();
     return false;
     }*/
    if (rType == 1 && isChange == 1 && integral != "") {//积分商品（从积分商城进来的）
        var jfFlag = true;
        if (myIntegral == null || myIntegral == "") {
            jfFlag = false;
        } else {
            if (integral * 1 > myIntegral * 1) {
                jfFlag = false;
            }
        }
        if (jfFlag) {
            primary_price = price;
            price = integral;
            totalprice = integral * parseInt(product_num);
            discount = 100;//积分不打折
        } else {
            alert("您的积分不足，不能兑换该产品");
            flag = false;
            return;
        }
    }
    var myFenbi = $(".myFenbi").val();
    if (rType == 2 && isChangeFenbi == 1 && changeFenbi != "") {//积分商品（从积分商城进来的）
        var jfFlag = true;
        if (myFenbi == null || myFenbi == "") {
            jfFlag = false;
        } else {
            if (changeFenbi * 1 > myFenbi * 1) {
                jfFlag = false;
            }
        }
        if (jfFlag) {
            primary_price = price;
            price = changeFenbi;
            totalprice = changeFenbi * parseInt(product_num);
            discount = 100;
        } else {
            alert("您的粉币不足，不能兑换该产品");
            flag = false;
            return;
        }

    }
    var memberType = $("#member_type").val();
    var cardType = $("#card_type").val();
    if (proTypeId == 2 && memberType != "") {
        if ($(".buyCode").val() == "-1") {
            alert("您已经购买了会员卡，不能再次购买");
            flag = false;
            return;
        }
    } else if (proTypeId == 3 && cardType != "") {
        if ($(".recevieMoney").length > 0) {
            price = $(".recevieMoney").val();
        } else {
            price = $(".priceclass").text();
        }
        totalprice = price * product_num;
        if ($("input.cardGuoqi").length > 0) {
            if (!confirm("您购买的卡券包里有卡券已过期，是否继续购买")) {
                flag = false;
                return;
            }
        }
    }
    var view = $(".view").val();
    if (view == "show") {
        alert("正在预览的商品不能购买");
        return;
    }
    if (flag) {
        var hs = {product_id: product_id, product_specificas: product_specificas, totalnum: product_num, price: price, primary_price: primary_price, discount: discount};
        if (groupBuyId > 0) {
            hs["groupBuyId"] = groupBuyId;
        }
        if (pJoinId >= 0) {
            hs["pJoinId"] = pJoinId;
        }
        if (types != null && types != "" && types != "0") {
            hs["groupType"] = types;
        }
        //hs["totalprice"] = totalprice;
        //hs["image_url"]=image_url;
        //hs["product_speciname"]=product_speciname;
        //hs["shop_name"] = shop_namemessg;
        //hs["product_name"] = product_name;
        //hs["shop_id"]=shopid;
        //hs["pro_code"]=pro_code;
        //hs["return_day"]=return_day;

        //hs["isCoupons"]=isCoupons;
        //hs["is_member_discount"] = $(".is_member_discount").val();
        //hs["pro_type_id"] = $("#pro_type_id").val();
        //hs["member_type"] = $("#member_type").val();
        //hs["is_integral_deduction"] = $(".is_integral_deduction").val();
        //hs["is_fenbi_deduction"] = $(".is_fenbi_deduction").val();

        if ($(".saleMemberId").length > 0 && saleLen > 0) {
            var commission = $(".commission_lay").text();
            if (commission != null && commission != "") {
                hs["saleMemberId"] = $("input.saleMemberId").val();
                hs["commission"] = commission;
            }
        }
        $("#json").val(JSON.stringify(hs));
        //console.log(hs)
        sumbit();
    }
}


var saleMemberId = $("input.saleMemberId").val();
var shopid = $("input#shopid").val();
//店面跳转
function pageclick(obj) {
    if (saleMemberId == null || saleMemberId == "") {
        if (obj == '' || obj == null || obj == undefined || obj == 0 || obj == '0') {
            alert("店面未设置微商城主页面或者微商城主页面已删除");
        } else {
            window.location.href = "/mallPage/" + obj + "/79B4DE7C/pageIndex.do";
        }
    } else {
        window.location.href = "/phoneSellers/" + saleMemberId + "/79B4DE7C/mallIndex.do?uId=" + userid;
    }
}
//跳转到全部商品
function shoppingalls() {
    if (saleMemberId == null || saleMemberId == "") {
        window.location.href = "/mallPage/" + shopid + "/79B4DE7C/shoppingall.do";
    } else {
        window.location.href = "/phoneSellers/" + saleMemberId + "/79B4DE7C/shoppingall.do";
    }
}
//跳转到个人中心页面
function myCenter() {
    window.location.href = "/mMember/79B4DE7C/toUser.do?uId=" + $("input.userid").val();
}
//跳转到个人中心页面
function PersonalCenter() {
    var memberId = $("input#memberIds").val();
    var userid = $("input.userid").val();
    //window.location.href = "/mMember/79B4DE7C/toUser.do?uId="+$("input.userid").val();
    if (memberId != null && memberId != "") {
        window.location.href = "/phoneMemberController/" + userid + "/79B4DE7C/findMember_1.do";
    } else {
        toLogin();
    }
}
//跳转到购物车里
function shoppingcart() {
    var memberId = $("input#memberIds").val();
//	if(memberId == null || memberId == ""){
//		toLogin();
//		return false;
//	}
    if (saleMemberId == null || saleMemberId == "") {
        window.location.href = "/mallPage/79B4DE7C/shoppingcare.do?uId=" + userid;
    } else {
        window.location.href = "/mallPage/79B4DE7C/shoppingcare.do?saleMemberId=" + saleMemberId + "&uId=" + userid;
    }
}
//去参团
function goGroup(groupBuyId, joinId, joinUserId) {
    var memberId = $("input#memberIds").val();
    if (memberId == null || memberId == "") {
        toLogin();
        return false;
    }
    var userid = $(".userid").val();
    location.href = "/mGroupBuy/" + groupBuyId + "/" + joinId + "/79B4DE7C/groupBuyDetail.do?uId=" + userid + "&buyerUserId=" + joinUserId;
}