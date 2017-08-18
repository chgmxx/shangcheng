var userid = $("input.userid").val();
/*下单 遮罩层*/
function showWrap(status) {
    var memberId = $("input.memberId").val();
    if (memberId == null || memberId == "") {
        toLogin();
        return;
    }
    var memberId = $("input#memberIds").val();
    if (status == "0") {
        alert("您的批发商申请在审核中请耐心等待1-3个工作日");
        return;
    } else if (status == null || status == "" || status == "-2") {
        if (confirm("您还没申请批发商，是否前往我的批发进行申请")) {
            location.href = "/phoneWholesaler/79B4DE7C/toApplyWholesaler.do?member_id=" + memberId + "&uId=" + userid;
        }
        return;
    } else if (status == "-1") {
        if (confirm("您的批发商申请不通过,是否前往我的批发进行重新申请")) {
            location.href = "/phoneWholesaler/79B4DE7C/toApplyWholesaler.do?member_id=" + memberId + "&uId=" + userid;
        }
        return;
    }
    $("#fade").show();
    $(".wrap-item").show();
    $("body,html").attr("style", "overflow-y:hidden;height:100%");
    var wHeight = $(window).height();
    var height = 0;
    $(".item-box:not(.item-tables)").each(function (index) {
        height = parseInt(height) + $(this).outerHeight();
    });
    height = height + parseInt($(".item-bottom").outerHeight()) + parseInt($(".item-top").outerHeight()) + parseInt($(".item-end").outerHeight());
    height = wHeight - height;
    $(".item-tables").css("max-height", height + "px");
}
//关闭 遮罩层
function closeWrap(obj) {
    $("#fade").hide();
    $(obj).parents(".wrap-item").hide();
    $("body,html").attr("style", "");
}
//减法操作(单位：件)
function minusOper(obj, type) {
    var inputObj = $(obj).next();
    var val = inputObj.val();
    if (--val >= 0) {
        if (val == 0) {
            $(obj).parent().removeClass("oper-active");
        }
        inputObj.val(val);
        activeOper($(obj));
        if ($(obj).hasClass("iProp")) {
            if (type == 0) {
                setOper2($(obj))
            } else {
                setOper($(obj));
            }
        } else {
            /*setOper2($(obj));*/
            /*$input.val(val)*/
        }
    }
}
//加法操作(单位：件)
function addOper(obj) {
    var inputObj = $(obj).prev();
    var val = parseInt(inputObj.val());

    if (!$(obj).parent().hasClass("oper-active")) {
        $(obj).parent().addClass("oper-active");
    }
    //不得大于库存量
    var stockObj = $(obj).parents("tr").find(".item-stock");
    var stockObjNoSpec = $(obj).parents(".item-type").find(".item-stock");
    var parent = $(obj).parents("item-type");
    var stockNum;
    if (stockObj.length == 0) {
        stockObj = stockObjNoSpec;
        stockNum = parseInt(stockObj.val());
    }
    if (stockObj.length != 0) {
        stockNum = parseInt(stockObj.html());
        if (val < stockNum || val < $stock) {
            val++;
            inputObj.val(val);
            activeOper($(obj));
            setOper($(obj));
        }
    } else {

        stockNum = $stock;
        if (val < stockNum || val < $stock) {
            val++;
            inputObj.val(val);
            /*$input.val(val)*/
            /*setOper2($(obj));*/
        }
    }
}
function activeOper(obj) {
    var $parent = $(obj).parents(".item-type");
    var zjNum = 0;
    $parent.find("table.tab-active").find(".item-input").each(function () {
        zjNum = parseInt(zjNum) + parseInt($(this).val());
    });
    if (zjNum == 0) {
        $parent.find("a.cActive").next().hide();
    } else {
        var $radius = $parent.find("a.cActive").next();
        if ($radius.css("display") == "none") {
            $radius.show();
        }
    }
    $("a.cActive").next("span").html(zjNum);
}
//减法操作（单位：手）
function minusByHand(obj) {
    var parent = $(obj).parents(".item-type");
    var zjNum = 0;
    parent.find("table.tab-active").attr("over", 0).find(".tr-num").each(function () {
        var curNum = $(this).html();
        if (curNum <= 0) {
            return;
        } else {
            $(this).html(--curNum);
            zjNum = parseInt(zjNum) + curNum;
        }
    });
    if (zjNum == 0) {
        parent.find("a.cActive").next().hide();
    }
    $("a.cActive").next().html(zjNum);
    setOper($(obj));
}
//加法操作（单位：手）
var over = false, $handStock = 0;
function addByHand(obj) {
    var parent = $(obj).parents(".item-type");
    var $handNum = 0;
    var table = parent.find("table.tab-active");
    if (table.attr("over") == 0) {
        $handStock = table.find(".item-stock").eq(0).html();
        table.find(".item-stock").each(function () {
            var curStock = $(this).html();
            if ($handStock > curStock) {
                $handStock = curStock;
            }
        });
        table.find(".tr-num").each(function () {
            var curNum = $(this).html();
            if (curNum * 1 < $handStock) {
                $(this).html(++curNum);
            } else {
                table.attr("over", 1);
            }
            $handNum = parseInt($handNum) + parseInt(curNum);
        });
        var $radius = parent.find("a.cActive").next();
        if ($radius.css("display") == "none") {
            $radius.show();
        }
        $("a.cActive").next().html($handNum);
        setOper($(obj));
    }
}
var $input, $stock;
//显示遮罩层
function showLayer(obj) {
    $input = $(obj);
    $input.blur();
    var initVal = $input.val();
    var $stockObj = $(obj).parents("tr").find(".item-stock");
    if ($stockObj.length == 0) {
        $stock = $(obj).parent("span").next(".item-stock").val();
    } else {
        $stock = $stockObj.html();
    }
    var layer = $("section#cd-input");
    layer.show().find(".item-input2").val($input.val());
    if (initVal <= 0) {
        layer.find(".cd-item").removeClass("oper-active");
    } else {
        layer.find(".cd-item").addClass("oper-active");
    }
    $stock = parseInt($stock);
}

//输入不得超过库存
function strictStock(obj) {
    var $rVal = parseInt($(obj).val());
    var reg = /^(0|[1-9][0-9]*)$/;
    if (!reg.test($rVal)) {
        $(obj).val("");
    } else {
        if ($rVal >= 0 && $rVal <= $stock) {
            if ($rVal == 0) {
                $(obj).val(1);
            }
            $(obj).parent().addClass("oper-active");
        } else {
            $(obj).val($stock);
        }
    }
}
//遮罩层  确定按钮
function numOper(obj) {
    var layer = $(obj).parents("section#cd-input");
    var val = layer.find(".item-input2").val();
    if (val == "") {
        return;
    } else {
        layer.hide();
        $input.val(val).parents(".item-oper").addClass("oper-active");
    }
    if ($input.attr("layer") == 0) {
        setOper2($input);
    } else {
        activeOper($input);
        setOper($input);
    }
}
//价格、件数  赋值操作
var leastNObj = $(".ws-least").find(".least-num");
var leastCObj = $(".ws-least").find(".least-cost");
var leastNum = leastNObj.length != 0 ? leastNObj.html() : 0;
var leastCost = leastCObj.length != 0 ? leastCObj.html() : 0;
leastNum = leastNum != null && leastNum != "" && leastNum != "0" ? leastNum * 1 : 0;
leastCost = leastCost != null && leastCost != "" && leastCost != "0" ? leastCost * 1 : 0;
function setOper2(obj) {
    var tabParent = obj.parents(".item-type");
    var unitPrice = $(".ws-price").val(), xjPrice = "0.00", xjNum = 0;
    var xjNum = tabParent.find(".item-input").val();
    if (typeof(xjNum) == "undefined") {
        xjNum = obj.parent().find("input").val();
    }
    xjPrice = parseFloat(unitPrice) * parseInt(xjNum);
    isSatisfy(xjNum, xjPrice);
    sDigit($(".item-end").find(".zj-price"));
}
function setOper(obj) {
    var tabParent = obj.parents(".item-type");
    var flag = tabParent.attr("type") == 1 ? true : false; //1：手批；2混批
    var trNum = 0, unitPrice, xjNum = 0, xjPrice = "0.00", handNum = 0;
    if (tabParent.find("table").length != 0) {
        tabParent.find("table").each(function () {
            var $table = $(this), curHandNum = 0;
            $table.find("tbody>tr").each(function () {
                var $tr = $(this);
                unitPrice = $tr.find(".unit-price").html();
                if (flag) {
                    trNum = parseInt($(this).find(".tr-num").html());
                    if (curHandNum == 0) {
                        curHandNum = trNum;
                    }
                } else {
                    trNum = parseInt($(this).find(".item-input").val());
                }
                xjNum = parseInt(xjNum) + trNum;
                xjPrice = parseFloat(xjPrice) + parseFloat(unitPrice) * parseInt(trNum);
            });
            handNum = parseInt(handNum) + curHandNum;
        });
    } else {
        xjNum = $(".item-input").val();
        xjPrice = parseFloat(xjPrice) + parseFloat($(".pfPriceSpan").html()) * xjNum;
    }
    //是否满足批发条件(首先判断是手批或者混批)
    if ($(".item-type").attr("type") == 1) {
        if (handNum < leastNum) {
            $(".item-end").html('还差<span>' + (leastNum - handNum) + '</span>手可达批发条件， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").addClass("wsInactive");
        } else {
            $(".item-end").html('满<span>' + leastNum + '</span>手，达到批发条件， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").removeClass("wsInactive");
        }
    } else {
        isSatisfy(xjNum, xjPrice);
    }
    sDigit($(".item-end").find(".zj-price"));
}
/*是否满足批发条件*/
function isSatisfy(xjNum, xjPrice) {
    xjNum = xjNum * 1;
    xjPrice = xjPrice * 1;
    if (leastNum != 0 && leastCost == 0) {
        if (parseInt(xjNum) >= leastNum) {
            $(".item-end").html('满<span>' + leastNum + '</span>件，达到批发条件， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").removeClass("wsInactive");
        } else {
            $(".item-end").html('还差<span>' + (leastNum - xjNum) + '</span>件，可混批， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").addClass("wsInactive");
        }
    } else if (leastNum == 0 && leastCost != 0) {
        if (xjPrice >= leastCost) {
            $(".item-end").html('满<span>' + leastCost + '</span>元，达到批发条件， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").removeClass("wsInactive");
        } else {
            $(".item-end").html('还差<span>' + (leastCost - xjPrice) + '</span>元,可混批， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").addClass("wsInactive");
        }
    } else if (leastNObj != 0 && leastCost != 0) {
        if (xjNum < leastNum && xjPrice < leastCost) {
            $(".item-end").html('还差<span>' + (leastNum - xjNum) + '</span>件，或<span>' + (leastCost - xjPrice) + '</span>元可混批， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").addClass("wsInactive");
        } else {
            $(".item-end").html('满<span>' + leastNum + '</span>件或<span>' + leastCost + '</span>元，达到批发条件， <span class="cr-1">&yen;<span class="zj-price">' + xjPrice.toFixed(2) + '</span></span> <small>共<span class="zj-num">' + xjNum + '</span>件</small>');
            $("a.toWS").removeClass("wsInactive");
        }
    }
}


window.onload = function () {
    $(".item-box>table.tab-active").show().siblings().hide();
    //判断是否是IOS，是，则调用 处理点击事件延迟 的方法

    sDigit($(".split-ele"));

    if ($(".pifaId").length > 0) {
        loadPifa();
    }

    loadNoDelay();
};

function loadNoDelay() {
    //判断是否是IOS，是，则调用 处理点击事件延迟 的方法
    if ($(".noDelay").length > 0) {
        var u = navigator.userAgent;
        if (!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
            $(".noDelay").each(function () {
                new NoClickDelay($(this)[0]);
            });
        }
    }
}
//价格小数后两位变小
function sDigit(ele) {
    ele.each(function () {
        var arr = $(this).html().split(".");
        $(this).html('<span>' + arr[0] + '</span>.<small>' + arr[1] + '</small>');
    });
}
//手机端软键盘显示与否
/*var wHeight = $(window).height();
 var scrollTop = $("body").scrollTop();
 $(window).resize(function() {
 if(wHeight != $(window).height() || scrollTop != $('body').scrollTop()) {
 $("div.cd-main").addClass("cd-top-Y").removeClass("cd-bot-Y");
 }else{
 $("div.cd-main").addClass("cd-bot-Y").removeClass("cd-top-Y").find(".item-input2").blur();
 }
 });*/
//关闭遮罩层
function closeLayer(obj) {
    $(obj).parents("section#cd-input").hide();
}
//切换不同类型的商品
function switchBtn(obj) {
    $(obj).addClass("cActive").parent().siblings().find("a").removeClass("cActive");
    var index = $(obj).parent().index();
    var parent = $(obj).parents(".item-type");
    parent.find(".item-tab").eq(index).addClass("tab-active").show().siblings("table").hide().removeClass("tab-active");
}
//去批发按钮
function toWS(obj) {
    if ($(obj).hasClass("wsInactive")) {
        return;
    } else {
        var stockTotal = $(".stockTotal").val();
        if (stockTotal == "" || stockTotal == "0") {
            alert("您购买商品的库存不够，请重新选择商品");
            return;
        }
        //$(obj).attr("href","a.html");
        pfProductBuy(obj);
    }
}
/**
 * 加载批发
 */
function loadPifa() {
    var pfType = $("input.pfType").val();
    var pfNum = 0;
    if (pfType == 1) {//手批
        pfNum = $("input.spNum").val();
        leastNum = pfNum;
        $(".tip1_span").html("本商品");
        $(".pfUnitSpan").html("手");
        $(".tip2_span").html("起批");
    } else {//混批
        pfNum = $("input.hpNum").val();
        leastNum = pfNum;
        //$(".tip1_span").html("本店满");
        $(".tip1_span").html("本商品");
        $(".pfUnitSpan").html("件");
        $(".tip2_span").html("混批");

        if ($("input.hpMoney").length > 0) {
            var hpMoney = $("input.hpMoney").val();
            if (hpMoney != null && hpMoney != "") {
                hpMoney = hpMoney * 1;
                if (hpMoney > 0) {
                    $(".hp_spans").show();

                    hpMoney = hpMoney.toFixed(2)

                    $(".hpMoneySpan").html(hpMoney);
                }
            }
        }
    }
    if (pfNum == 0) {
        $(".pfNumSpan").hide();
        $(".pfUnitSpan").hide();
    }
    $(".pfNumSpan").html(pfNum);

    //初始化批发价格
    var proPrices = 0;
    var costPrice = 0;

    var isSpec = $("input#isSpec").val();
    var discount = $("#discount").val() * 1;//会员折扣

    if (isSpec == '1') {//该商品存在规格
        var costPrice = $("input#invPrice").val();
        proPrices = costPrice * 1 * discount;
    } else {
        var costPrice = $("input#proPrice").val();
        proPrices = costPrice * 1 * discount;
    }

    $(".proPriceSpan").html((costPrice * 1).toFixed(2));

    var pifaPrice = $("input.pfPrice").val();
    pifaPrice = (pifaPrice * discount).toFixed(2);
    $(".pfPriceSpan").html(pifaPrice);

    $stock = $("input.stockTotal").val();

    $("input.ws-price").val(pifaPrice);

    //初始化批发商品的规格和库存
    if ($(".specValIndex").length > 0) {
        $(".specValIndex").each(function (i) {
            var valueId = $(this).attr("option");
//			console.log(valueId)
            eval(step.Creat_Table(valueId, i));// 初始化创建库存表格
        });
    } else if (isSpec == 1) {
        eval(step.Creat_Table(0, 0));// 初始化创建库存表格
    }
}
function loadSpecJSON(_obj, pfType) {
    var obj = null;
    var _pObj = _obj.parents("tr");
    var num = 0;
    if (pfType == 1) {
        num = _obj.text();
    } else {
        num = _obj.val();
    }
    var proSpecStr = new Object();
    if (num != null && num != "" && !isNaN(num) && typeof(num) != "undefined") {
        if (num * 1 > 0) {
            var specValIds = "";
            var names = "";
            var specValue = "";
            var specName = "";
            _pObj.find(".specCla").each(function () {
                if (specValIds != "") {
                    specValIds += ",";
                    names += " ";
                    specValue += " ";
                    specName += " ";
                }
                specValIds += $(this).attr("id");
                names += $(this).text();
                specValue += $(this).text();
                specName += $(this).attr("names");

            });
            var product_speciname = names;

            proSpecStr = {
                num: num,
                value: names,
                price: _pObj.find(".unit-price").text(),
                specName: specName,
                isCheck: "1"
            };
            obj = {
                product_speciname: product_speciname,
                specValIds: specValIds,
                num: num,
                specName: specValue,
                proSpecStr: proSpecStr
            };

        }
    }
    return obj;
}
function getJson() {
    var product_num = 0;
    var product_speciname = "";
    var pro_spec_str = {};

    var pfType = $(".pfType").val();//批发类型
    var isSpec = $("#isSpec").val();
    var specName = "";

    if (pfType == 1) {//手批
        if (isSpec == 0) {//没有规格
            product_num = $(".pfNumInp").val();
        } else if (isSpec == 1) {//有规格
            $(".handDiv tbody tr .tr-num").each(function () {
                var obj = loadSpecJSON($(this), pfType);
                if (obj != null) {
                    if (product_speciname != "") {
                        product_speciname += ",";
                    }
                    product_speciname += obj.product_speciname;
                    pro_spec_str[obj.specValIds] = obj.proSpecStr;
                    //proSpecNames[obj.specValIds] = obj.specName;

                    product_num += obj.num * 1;

                }
            });
        }
    } else if (pfType == 2) {//混批
        if (isSpec == 0) {//没有规格
            product_num = $(".pfNumInp").val();
        } else if (isSpec == 1) {//有规格
            $(".mixedDiv tbody tr .proNumInp").each(function () {
                var obj = loadSpecJSON($(this), pfType);
                if (obj != null) {
                    if (product_speciname != "") {
                        product_speciname += ",";
                    }
                    product_speciname += obj.product_speciname;
                    pro_spec_str[obj.specValIds] = obj.proSpecStr;
                    //proSpecNames[obj.specValIds] = obj.specName;
                    product_num += obj.num * 1;
                }
            });
        }
    }
    if ($("span.pro_spec").length > 0) {
        $("span.pro_spec").each(function () {
            if (specName != "") {
                specName += " ";
            }
            specName += $(this).text();
        });
    }
    var result = new Object();
    result.product_num = product_num;
    result.product_speciname = product_speciname;
    result.pro_spec_str = pro_spec_str;
    result.specName = specName;
    return result
}
/**
 * 批发添加购物车
 */
function pfAddShopCart(obj) {
    var stockTotal = $(".stockTotal").val();
    if (stockTotal == "" || stockTotal == "0") {
        alert("您购买商品的库存不够，请重新选择商品");
        return;
    }

    var pfType = $(".pfType").val();//批发类型
    var isSpec = $("#isSpec").val();

    var shopid = $("input#shopid").val();//获取商铺id
    var product_id = $("input#proid").val();//获取商品id
    var product_specificas = "";//产品规格,存多个规格，用;分开
    var product_speciname = "";//规格名称
    var product_num = 0;//商品数量
    var price = $(".pfPriceSpan").text();//价格
    var primary_price = $(".proPriceSpan").text();//商品原价
    var pro_code = $("#pro_code").val();
    var discount = $("#discount").val();//折扣
    var pro_spec_str = new Object();//商品规格
    var proSpecNames = new Object();
    var group_buy_id = $(".pifaId").val();
    var result = getJson();
    if (result != null) {
        if (result.product_num != null) {
            product_num = result.product_num;
        }
        if (result.product_speciname != null) {
            product_speciname = result.product_speciname;
        }
        if (result.pro_spec_str != null) {
            pro_spec_str = result.pro_spec_str;
        }
    }
    if (product_num == null || product_num == "" || isNaN(product_num) || product_num == "0") {
        alert("商品数量必须是大于0");
        return false;
    }
    if (isSpec == 1 && pro_spec_str == null) {
        alert("请选择规格和数量")
        return false;
    } else if (isSpec == 1 && pro_spec_str != null) {
        pro_spec_str = JSON.stringify(pro_spec_str);
    } else {
        pro_spec_str = "";
    }
    /*if(proSpecNames != null){
     proSpecNames = JSON.stringify(proSpecNames);
     }*/
    if (discount != null && discount != "" && discount != "1") {
        discount = discount * 1;
        price = price * discount;
    }
    var stockTotal = $("input.stockTotal").val();
    if ($("input.guigePrice_value").length > 0) {
        var invNum = $("input.guigePrice_value[option='" + product_specificas + "']").attr("optionnum");
        if (invNum == 0 || invNum < invNum - product_num) {
            alert("商品的库存不够，请重新选择规格");
            flag = false;
            return;
        }
    }
    if (stockTotal != null && stockTotal != "") {
        if (stockTotal == 0 || stockTotal < stockTotal - product_num) {
            alert("商品的库存不够请重新选择商品");
            flag = false;
            return;
        }
    }
    var index = layer.open({
        title: "",
        content: "",
        offset: "10%",
        type: 2,
        shadeClose: false
    });
    $.ajax({
        type: "post",
        url: "/mallPage/79B4DE7C/addshopping.do",
        data: {
            shopId: shopid, productId: product_id, productSpecificas: product_specificas, productNum: product_num,
            productSpeciname: product_speciname, price: price, primaryPrice: primary_price, proCode: pro_code,
            discount: discount, proSpecStr: pro_spec_str, proType: pfType
        },
        async: false,
        dataType: "json",
        success: function (data) {
            layer.closeAll();
            var error = data.error;
            if (error == 0) {
                var shopnum = $(".shopping-icon").text();//显示购物车该产品数量

                $(".shopping-icon").show();

                $(".shopping-icon").text(parseInt(shopnum) + parseInt(product_num));
                alert("商品添加到购物车成功");
                location.href = window.location.href;
            } else {
                alert("商品添加到购物车失败");
            }
        }, error: function (data) {
            layer.closeAll();
            alert("商品添加到购物车失败");
        }
    })
}
if ($(".pfNumInp").length > 0) {
    $(".pfNumInp").val(0)
}
function pfProductBuy() {
    var shopid = $("input#shopid").val();//获取商铺id
    var product_id = $("input#proid").val();//获取商品id
    var product_specificas = "";
    var product_speciname = "";//规格名称
    var product_num = 1;
    var price = $("#prodect_price").val();
    var product_name = $(".product_nameclass").text();//获取商品名;//获取商品名
    var shop_namemessg = $("#shop_name").val();
    var totalprice = 0;
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
    var proSpecStr = "";//存放规格名称
    var pro_spec_str = new Array();

    var pfType = $(".pfType").val();//批发类型
    var isSpec = $("#isSpec").val();

    var result = getJson();
    if (result != null) {
        if (result.product_num != null) {
            product_num = result.product_num;
        }
        if (!isEmptyObject(result.pro_spec_str)) {
            pro_spec_str = result.pro_spec_str;
            for (var str in pro_spec_str) {
                if (pro_spec_str[str] != null) {
                    var objs = pro_spec_str[str];
                    if (product_speciname != "") {
                        product_speciname += "、";
                    }
                    product_speciname += objs.value + " x " + objs.num + " ¥ " + objs.price;

                    totalprice += objs.num * objs.price;
                }
            }
        } else {
            price = $(".pfPriceSpan").text();

            totalprice = price * product_num;
        }
        if (result.specName != null && result.specName != "" && product_speciname != "") {
            product_speciname = result.specName + "：" + product_speciname;
        }
    }
    if (product_num == null || product_num == "" || isNaN(product_num)) {
        alert("商品数量必须是大于0");
        return false;
    }

    if (isSpec == 1 && pro_spec_str == null) {
        alert("请选择规格和数量")
        return false;
    } else if (isSpec == 1 && pro_spec_str != null) {
        proSpecStr = JSON.stringify(pro_spec_str);
    } else {
        proSpecStr = "";
    }
    /*if(proSpecNames != null){
     proSpecNames = JSON.stringify(proSpecNames);
     }*/
    if (discount != null && discount != "" && discount != "1") {
        //discount = discount*1;
        //price = price*discount;
    }
    var stockTotal = $("input.stockTotal").val();
    if ($("input.guigePrice_value").length > 0) {
        var invNum = $("input.guigePrice_value[option='" + product_specificas + "']").attr("optionnum");
        if (invNum == 0 || invNum < invNum - product_num) {
            alert("商品的库存不够，请重新选择规格");
            flag = false;
            return;
        }
    }
    if (stockTotal != null && stockTotal != "") {
        if (stockTotal == 0 || stockTotal < stockTotal - product_num) {
            alert("商品的库存不够请重新选择商品");
            flag = false;
            return;
        }
    }

    /*var invNum = $(".inv-num span#inventory").text();
     if(product_num*1 > $.trim(invNum)*1){
     alert("你购买的数量大于商品现有的库存，请重新输入购买数量");
     return;
     }*/

    var proTypeId = $("#pro_type_id").val();
    var memberType = $("#member_type").val();
    if (proTypeId == 2 && memberType != "") {
        if ($(".buyCode").val() == "-1") {
            alert("您已经购买了会员卡，不能再次购买");
            flag = false;
            return;
        }
    }
    var types = "7";
    if (flag) {
        //product_name = $.trim(product_name)
        var hs = {
            product_id: product_id,
            product_specificas: product_specificas,
            totalnum: product_num,
            price: price,
            primary_price: primary_price,
            discount: discount,
            isCoupons: isCoupons
        };
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
        //批发
        hs["pro_spec_str"] = proSpecStr;
        $("#json").val(JSON.stringify(hs));
        //console.log(hs)
        sumbit();
    }
}
function isEmptyObject(e) {
    var t;
    for (t in e)
        return false;
    return true
}  