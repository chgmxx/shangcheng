$(function () {
    //判断是否是IOS，是，则调用 处理点击事件延迟 的方法
    var u = navigator.userAgent;
    if (!!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/)) {
        $(".noDelay").each(function () {
            new NoClickDelay($(this)[0]);
        });
    }
});

//取消A标签的默认行为
$(document).click(function (e) {
    if ($(e.target).is(".minus-icon") || $(e.target).is(".add-icon")
        || $(e.target).is(".toMinus") || $(e.target).is(".toAdd")) {
        e.preventDefault();
    }
});
//手机端软键盘显示与否
/*var wHeight = $(window).height();
 var scrollTop = $("body").scrollTop();
 $(window).resize(function() {
 if(wHeight != $(window).height() || scrollTop != $('body').scrollTop()) {
 $("div.cd-main").addClass("cd-top-Y").removeClass("cd-bot-Y");
 $("footer.wFooter").hide();
 }else{
 $("div.cd-main").addClass("cd-bot-Y").removeClass("cd-top-Y").find(".item-input2").blur();
 $("footer.wFooter").show();
 }
 });*/
//关闭遮罩层
function closeLayer(obj) {
    $(obj).parents("section#cd-input").hide();
    $('body').removeClass('a');
}
//显示遮罩层
var $input, $stock = 0;
function showLayer(obj) {
    $input = $(obj);
    $stock = parseInt($input.attr("stock"));
    $input.blur();
    $showObj = $("section#cd-input").show().find(".item-input2");
    $showObj.val($input.val());
    $showObj.attr("stock", $stock);
    $showObj.attr("max", $input.attr("max"));
    $showObj.attr("proid", $input.attr("proid"));
    $showObj.attr("id", $input.attr("id"));
    $('body').addClass('a');
}
//输入不得超过库存
function strictStock(obj) {
    var inputObj = $(obj);
    var $rVal = parseInt($(obj).val());
    var reg = /^(0|[1-9][0-9]*)$/;
    if (!reg.test($rVal)) {
        $(obj).val("");
    } else {
        var stock = parseInt(inputObj.attr("stock"));//商品最大库存
        var max = 0;
        if (typeof(inputObj.attr("max")) != "undefined") {
            max = parseInt(inputObj.attr("max"));//商品限购
        }
        var val = parseInt(inputObj.val());
        var proid = inputObj.attr("proid");
        var id = inputObj.attr("id");
        var maxPro = val;
        var proSum = 0;
        $("input.pro_inp[proid='" + proid + "']").each(function () {
            if (id != $(this).attr("id")) {
                var num = parseInt($(this).val());
                maxPro += num;
                proSum += num;
            }
        });
        if ($rVal == 0) {
            $(obj).val(1);
        } else if ((max == 0 || (max > 0 && $rVal + 1 <= max && maxPro <= max))) {
            return;
        } else if (max > 0 && ($rVal + 1 > max || maxPro > max)) {
            var proNum = max - proSum;
            if (proNum <= 0) {
                proNum = 1;
            }
            $(obj).val(parseInt(proNum));
        } else if ($rVal > 0 && $rVal <= $stock && maxPro < stock) {
            return;
        } else if ($rVal > $stock) {
            $(obj).val($stock);
        }
    }
}
//遮罩层  确定按钮
function numOper(obj) {
    var layer = $(obj).parents("section#cd-input");
    var inputObj = layer.find(".item-input2");
    var val = inputObj.val();
    if (val == "") {
        return;
    } else {
        var stock = parseInt(inputObj.attr("stock"));//商品最大库存
        var max = 0;
        if (typeof(inputObj.attr("max")) != "undefined") {
            max = parseInt(inputObj.attr("max"));//商品限购
        }
        var val = parseInt(inputObj.val());
        var proid = inputObj.attr("proid");
        var id = inputObj.attr("id");
        var maxPro = val;
        $("input.pro_inp[proid='" + proid + "']").each(function () {
            if (id != $(this).attr("id")) {
                maxPro += parseInt($(this).val());
            }
        });
        if ((val < stock || val < $stock) && (max == 0 || (max > 0 && val + 1 <= max && maxPro <= max)) && maxPro < stock) {//购买的数量不能大于库存和限购的数量
            layer.hide();
            $('body').removeClass('a');
            $input.val(val);
        }

    }
    setOper($input, val);
}
//点击编辑或完成按钮
function editOper(obj) {
    if ($(obj).attr("edit") == 0) {
        $(obj).hide().next().show();
    } else {
        $(obj).parent().hide().prev().show();
    }
}
//导航栏事件
function switchNav(obj) {
    /*var baseHTML = '<div class="l">' +
     '<p>总计  <span class="cr-1">&yen;<span class="zjPrice">0.00</span></span></p>' +
     '<p>(共 <span class="">0</span> 件)</p>' +
     '</div>' +
     '<div class="r"><a href="#" class="base-btn tActive">去结算</a></div>';
     var wsHTML = '<div class="l">' +
     '<p><span class="zjNum">0</span>件,还差 <span class="restNum">3</span>件可混批</p>' +
     '<p>总价： <span class="cr-1">&yen;<span class="zjPrice">0.00</span></span></p> </div>' +
     '<div class="r"> <a href="#" class="base-btn toGo">继续选购</a> ' +
     '<a href="javascript:void(0)" class="base-btn tInactive" onclick="toCount(this)">去结算</a> ' +
     '</div>';
     $(obj).toggleClass("nav-active").parent().siblings().find("a").toggleClass("nav-active");
     if($(obj).attr("item") == 2) {
     $("footer.wFooter").html(wsHTML);
     }else {
     $("footer.wFooter").html(baseHTML);
     }*/
}
//计算手批数目和混批数量
function countNumByType() {
    var cProp = {"handNum": 0, "mixNum": 0, "mixCost": 0};
    $(".box-main").each(function () {
        var _this = $(this);
        if (_this.hasClass("box-hand")) {
            if (_this.find(".ck-icon").hasClass("ck-check")) {
                cProp.handNum = cProp.handNum + parseInt(_this.find(".item-num").eq(0).html());
            }
        } else {
            var spec = _this.find("._spec");
            if (spec.length > 0) {
                spec.each(function () {
                    if ($(this).find(".ck-icon").hasClass("ck-check")) {
                        var inputVal = parseInt($(this).find(".item-input").val());
                        var itemPrice = parseFloat($(this).find(".item-price").html());
                        cProp.mixNum = cProp.mixNum + inputVal;
                        cProp.mixCost = cProp.mixCost + inputVal * itemPrice;
                    }
                })
            } else {
                if (_this.find(".ck-icon").hasClass("ck-check")) {
                    var inputVal = parseInt($(this).find(".item-input").val());
                    var itemPrice = parseFloat($(this).find(".item-price").html());
                    cProp.mixNum = cProp.mixNum + inputVal;
                    cProp.mixCost = cProp.mixCost + inputVal * itemPrice;
                }
            }
        }
    });
    return cProp;
}
//结算按钮
var leastHand = parseInt($("input.leastHand").val()), leastNum = parseInt($("input.leastNum").val()), leastCost = parseInt($("input.leastCost").val());
function toCount(obj) {
    if ($("input.proType").val() == "1") {
        var cProp = countNumByType();
        $(".item-term").html("");
        var handFlag = isHand(cProp.handNum);
        var mixFlag = isMixed(cProp.mixNum, cProp.mixCost)
        if (handFlag && mixFlag) {
            if (mixLen == 0 && handLen == 0) {
                $(".item-term").fadeIn().html("<p class='tc'>您还未选择商品！</p>").delay(3000).fadeOut("slow");
            } else {
                $(".item-term").slideUp();
                //可以提交
                go_order();
            }
        } else {
            $(".item-term").slideDown();
        }
    } else {
        //可以提交
        go_order();
    }
}
//手批是否满足批发条件
var handLen = 0, mixLen = 0;
function isHand(curHand) {
    var handHTML = "";
    handLen = $(".box-hand").find(".ck-check").length;
    if (handLen <= 0) {
        return true;
    }
    if (curHand < leastHand && leastHand > 0) {
        handHTML = '手批商品还差 <span>' + (leastHand - curHand) + '</span> 手可批发；';
        $(".item-term").append(handHTML);
        disableHand();
        return false;
    } else {
        ableHand();
        $(".item-term").append("");
        return true;
    }
}

//不满足手批条件不允许批发
function disableHand() {
    $(".box-hand").each(function () {
        var _this = $(this);
        var checkObj = _this.find(".ck-check").parents(".box-hand").find(".item-tip").slideDown();
    });
}
//满足手批条件允许批发
function ableHand() {
    $(".box-hand").each(function () {
        $(this).find(".item-tip").slideUp();
    });
}
//不满足混批条件不允许批发
function disableMix() {
    $(".box-mixed").each(function () {
        var _this = $(this);
        var checkObj = _this.find(".ck-check").parents(".box-mixed").find(".item-tip").slideDown();
    });
}
//满足混批条件允许批发
function ableMix() {
    $(".box-mixed").each(function () {
        var _this = $(this);
        var checkObj = _this.find(".item-tip").slideUp();
    });
}
/*混批是否满足批发条件*/
function isMixed(curMixNum, curMixPrice) {
    var mixHTML = "", mixFlag = false;

    mixLen = $(".box-mixed").find(".ck-check").length;
    if (mixLen <= 0) {
        return true;
    }
    var isMixNum = isMixedNum(curMixNum), isMixPrice = isMixedPrice(curMixPrice);
    if (leastCost > 0 && leastNum > 0) {
        if (!isMixNum && !isMixPrice) {
            mixHTML = '混批商品还差<span>' + (leastNum - curMixNum) + '</span>件，或<span>' + (leastCost - curMixPrice).toFixed(2) + '</span>元可混批';
            disableMix();
        } else {
            ableMix();
            mixFlag = true;
        }
    } else if (leastCost > 0 && leastNum == 0) {
        if (!isMixPrice) {
            mixHTML = '混批商品还差<span>' + (leastCost - curMixPrice).toFixed(2) + '</span>元,可混批';
            disableMix();
        } else {
            ableMix();
            mixFlag = true;
        }
    } else if (leastCost == 0 && leastNum > 0) {
        if (!isMixNum) {
            mixHTML = '混批商品还差<span>' + (leastNum - curMixNum) + '</span>件，可混批';
            disableMix();
        } else {
            ableMix();
            mixFlag = true;
        }
    }
    $(".item-term").append(mixHTML);
    return mixFlag;
}
//混批商品价格不足以批发
function isMixedPrice(curMixPrice) {
    if (curMixPrice < leastCost) {
        return false;
    } else {
        return true;
    }
}
//混批商品数量不足以批发
function isMixedNum(curMixNum) {
    if (curMixNum < leastNum) {
        return false;
    } else {
        return true;
    }
}
//减法操作(单位：件)
function minusOper(obj) {
    var inputObj = $(obj).next();
    var val = inputObj.val();
    if (val > 1) {
        val--;
        inputObj.val(val);
        if ($(obj).hasClass("iProp")) {
            setOper($(obj), val);
        }
    }
}
//加法操作(单位：件) -- 不能超过库存
function addOper(obj) {
    var inputObj = $(obj).prev();
    var stock = parseInt(inputObj.attr("stock"));//商品最大库存
    var max = 0;
    if (typeof(inputObj.attr("max")) != "undefined") {
        max = parseInt(inputObj.attr("max"));//商品限购
    }
    var val = parseInt(inputObj.val());
    var proid = inputObj.attr("proid");
    var id = inputObj.attr("id");
    var maxPro = val + 1;
    $("input.pro_inp[proid='" + proid + "']").each(function () {
        if (id != $(this).attr("id")) {
            maxPro += parseInt($(this).val());
        }
    });
    if ((val < stock || val < $stock) && (max == 0 || (max > 0 && val + 1 <= max && maxPro <= max)) && maxPro <= stock) {//购买的数量不能大于库存和限购的数量
        val++;
        inputObj.val(val);
        if ($(obj).hasClass("iProp")) {
            setOper($(obj), val);
        }
    } else {
        alert("该商品已达到限购")
    }
}
//价格、件数  赋值操作
function setOper(obj, val) {
    var scParent = obj.parents("div.sc-box");
    xjOper(scParent, obj);
    zjOper();
}
//减法操作（单位：手）
function minusByHand(obj) {
    var iParent = $(obj).parents("div.box-main");
    if (iParent.attr("over") == 1) {
        iParent.attr("over", 0);
    }
    var scParent = $(obj).parents("div.sc-box");
    /*var num = parseInt(iParent.find(".item-num")[0].innerHTML);
     if(num > 1) {
     num = num - 1;
     $(".item-num",iParent).each(function() {
     $(this).html(num);
     });
     xjOper(scParent,$(obj));
     zjOper();
     }*/
    var minNum = getMinHand(iParent);
    if (minNum > 1) {
        $(".item-num", iParent).each(function () {
            var curNum = $(this).html() * 1;
            curNum = curNum - 1;
            $(this).html(curNum);
        });
        xjOper(scParent, $(obj));
        zjOper();
    }
}
function getMinHand(parent) {
    var minNum = parent.find(".item-num").eq(0).html() * 1;
    var itemNum = 0;
    parent.find(".item-num").each(function () {
        itemNum = $(this).html() * 1;
        if (itemNum < minNum) {
            minNum = itemNum;
        }
    });
    return minNum;
}
//加法操作（单位：手）
function addByHand(obj) {
    var parent = $(obj).parents(".box-main");
    if (parent.attr("over") == 0) {
        var stockArr = parent.find(".item-stock");
        var minStock = 0, curStock = 0;
        var stockLen = stockArr.length;
        if (stockLen > 0) {
            minStock = parseInt(stockArr.eq(0).val());
            if (stockLen == 1) {
                return;
            } else {
                stockArr.each(function () {
                    curStock = parseInt($(this).val());
                    if (curStock < minStock) {
                        minStock = curStock;
                    }
                });
            }
        }
        /* var num = parseInt(parent.find(".item-num")[0].innerHTML);
         if(num < minStock) {
         num = num + 1;
         $(".item-num",parent).each(function() {
         $(this).html(num);
         });
         var scParent = $(obj).parents("div.sc-box");
         xjOper(scParent,$(obj));
         zjOper();
         }else {
         parent.attr("over",1);
         }*/
        var minNum = getMinHand(parent);
        if (minNum < minStock) {
            $(".item-num", parent).each(function () {
                var curNum = $(this).html() * 1;
                curNum = curNum + 1;
                $(this).html(curNum);
            });
            var scParent = $(obj).parents("div.sc-box");
            xjOper(scParent, $(obj));
            zjOper();
        } else {
            parent.attr("over", 1);
        }
    }
}
/*店铺全选与否*/
function xjCheck(obj, scParent) {
    /*点击 -/+ 按钮，如果原先没有选中，即选中该商品*/
    var ckObj;
    var boxMain = obj.parents(".box-main");
    if (boxMain.hasClass("box-mixed")) {
        var specLen = obj.parents("._spec");
        var infoIcon = obj.parents(".item-info").find(".ck-icon");
        if (specLen.length == 0 && infoIcon.find(".ck-icon").length == 0) {
            ckObj = obj.parents(".box-main").find(".ck-icon");
        } else {
            ckObj = specLen.length == 0 ? infoIcon : specLen.find(".ck-icon");
        }
    } else {
        ckObj = obj.parents(".box-main").find(".ck-icon");
    }
    if (!ckObj.hasClass("ck-check")) {
        ckObj.addClass("ck-check");
    }
    /*店铺下所有商品是否都选中、购物车中所有商品是否都选中*/
    isSpecChecked(obj.parents(".item-detail"));
    isStoreChecked(scParent);
    isAllChecked();
}
/*店铺选中商品合计 --modified*/
function xjOper(scParent, obj) {
    if (obj) {
        xjCheck(obj, scParent);
    }
    var xjProps = {xjPrice: "0.00", xjNum: 0};
    //混批小计计算
    var boxMain = $(".box-main", scParent);
    boxMain.each(function () {
        var _this = $(this);
        if (_this.hasClass("box-mixed")) {
            var cur = _this.find("._spec");
            if (cur.length > 0) {
                cur.each(function () {
                    if ($(this).find(".ck-icon").hasClass("ck-check")) {
                        var inputVal = parseInt($(this).find(".item-input").val());
                        var itemPrice = parseFloat($(this).find(".item-price").html());
                        xjProps.xjPrice = (parseFloat(xjProps.xjPrice) + inputVal * itemPrice).toFixed(2);
                        xjProps.xjNum = parseInt(xjProps.xjNum) + inputVal;
                        $(this).find(".item-num").html(inputVal);
                    }
                });
            } else {
                if (_this.find(".ck-icon").hasClass("ck-check")) {
                    var xjNum = parseInt(_this.find(".item-input").val());
                    var itemPrice = parseFloat(_this.find(".item-price").html());
                    xjProps.xjPrice = (parseFloat(xjProps.xjPrice) + (xjNum * itemPrice)).toFixed(2)
                    xjProps.xjNum = parseInt(xjProps.xjNum) + xjNum;
                }
            }
        } else {
            /* if(_this.find(".ck-icon").hasClass("ck-check")) {
             var itemNum = parseInt(_this.find(".item-num").eq(0).html());
             _this.find(".item-price").each(function () {
             xjProps.xjPrice = (parseFloat(xjProps.xjPrice) + parseFloat($(this).html() * itemNum)).toFixed(2);
             });
             var itemLen = _this.find(".item-num").length;
             xjProps.xjNum = parseInt(xjProps.xjNum) + parseInt(itemLen * itemNum);
             }*/
            if (_this.find(".ck-icon").hasClass("ck-check")) {
                var itemNum/* = parseInt(_this.find(".item-num").eq(0).html())*/;
                _this.find(".item-price").each(function () {
                    itemNum = $(this).prev(".item-num").html() * 1;
                    xjProps.xjNum = parseInt(xjProps.xjNum) + itemNum;
                    xjProps.xjPrice = (parseFloat(xjProps.xjPrice) + parseFloat($(this).html() * itemNum)).toFixed(2);
                });
            }
        }
    });
    $("span.xj-price", scParent).html(xjProps.xjPrice);
    $("span.xj-num", scParent).html(xjProps.xjNum);
}
/*购物车中选中商品价格总计*/
function zjOper() {
    var zjNum = 0;
    var zjPrice = "0.00";
    $("div.sc-box").each(function () {
        zjNum = parseInt(zjNum) + parseInt($(this).find("span.xj-num").html());
        zjPrice = (parseFloat(zjPrice) + parseFloat($(this).find("span.xj-price").html())).toFixed(2);
    });
    $("span.zjPrice").html(zjPrice);
    $("span.zjNum").html(zjNum);
}
/*全选按钮选中与否*/
function isAllCheck(obj) {
    if ($(obj).is(".ck-check")) {
        $(".ck-icon").removeClass("ck-check");
    } else {
        $(".ck-icon").addClass("ck-check");
    }
    $(".sc-box").each(function () {
        xjOper($(this));
    });
    zjOper();
    jsBtn();
}
/*店铺选中与否*/
function isStoreCheck(obj) {
    var parent = $(obj).parents(".sc-box");
    if ($(obj).is(".ck-check")) {
        $(".ck-icon", parent).removeClass("ck-check");
    } else {
        $(".ck-icon", parent).addClass("ck-check")
    }
    /*判断全选按钮是否应该选中*/
    isAllChecked();
    xjOper(parent);
    zjOper();


    jsBtn();
}
/*规格选中与否*/
function isSpecCheck(obj) {
    var parent = $(obj).parents(".box-main");
    if ($(obj).is(".ck-check")) {
        $(".ck-icon", parent).removeClass("ck-check");
    } else {
        $(".ck-icon", parent).addClass("ck-check")
    }
    /*判断全选按钮以及该对象所在的店铺按钮是否应该选中*/
    var parentI = $(obj).parents(".box-list");
    var parentO = $(obj).parents(".sc-box");
    isStoreChecked(parentO);
    isAllChecked();
    xjOper(parentO);
    zjOper();

    jsBtn();
}
/*商品选中与否*/
function isItemCheck(obj) {
    if ($(obj).is(".ck-check")) {
        $(obj).removeClass("ck-check");
    } else {
        $(obj).addClass("ck-check");
    }
    /*判断全选按钮以及该对象所在的店铺按钮是否应该选中*/
    var parentI = $(obj).parents(".box-list");
    var parentO = $(obj).parents(".sc-box");
    var parentD = $(obj).parents(".item-detail");
    isSpecChecked(parentD);
    isStoreChecked(parentO);
    isAllChecked();
    xjOper(parentO);
    zjOper();


    jsBtn();
}

function jsBtn() {
    var _inp = $("input.proType");
    if (_inp.length > 0) {
        if (_inp.val() == "0") {
            var isCheck = 0;
            $(".check_div").each(function () {
                if ($(this).hasClass("ck-check")) {
                    isCheck = 1;
                }
            });
            if (isCheck == 1) {
                $(".goBuy").addClass("tActive");
                $(".goBuy").removeClass("tInactive");
            } else {
                $(".goBuy").removeClass("tActive");
                $(".goBuy").addClass("tInactive");
            }
        }
    }
}

/*判断是否所有商品皆被选中，是，则选中全选按钮，反之，不选*/
function isAllChecked() {
    var len1 = $(".sc-box .ck-icon").length;
    var len2 = $(".sc-box .ck-check").length;
    if (len1 == len2) {
        $("#ck-all").addClass("ck-check");
    } else {
        $("#ck-all").removeClass("ck-check");
    }
}
/*判断购物车中该店铺下的所有商品是否被选中，是，则选中店铺按钮，反之，不选*/
function isStoreChecked(parentO) {
    var len3 = 0, len4 = 0;
    $(".box-main", parentO).each(function () {
        var _this = $(this);
        if (_this.hasClass("box-hand")) {
            len3 = len3 + _this.find(".ck-check").length;
            len4 = len4 + _this.find(".ck-icon").length;
        } else {
            if (_this.find(".item-info>.ck-icon").length == 0) {
                len3 = len3 + _this.find(".ck-check").length;
                len4 = len4 + _this.find(".ck-icon").length;
            } else {
                len3 = len3 + _this.find(".item-info>.ck-check").length;
                len4 = len4 + _this.find(".item-info>.ck-icon").length;
            }
        }
    });
    if (len3 == len4) {
        $(".box-top", parentO).find(".ck-icon").addClass("ck-check");
    } else {
        $(".box-top", parentO).find(".ck-icon").removeClass("ck-check");
    }
}
//该商品的所有规格是否都被选中
function isSpecChecked(dParent) {
    var checkedLen = $(".ck-check", dParent).length;
    var allIconLen = $(".ck-icon", dParent).length;
    var targetObj = dParent.prev(".item-info").find(".ck-icon");
    if (checkedLen == allIconLen) {
        targetObj.addClass("ck-check");
    } else {
        targetObj.removeClass("ck-check");
    }
}


//商品移除购物车
function shopdelect() {
    var delects = "0";
    var upArr = new Array();
    $(".check_pro").each(function () {
        //delects += ","+$(this).attr("id");
        var hs = {id: $(this).attr("id")};
        var _parent = $(this).parents(".box-main");
        var check = 1;//未选中
        if ($(this).hasClass("ck-check")) {
            check = 0;//选中
            delects += "," + $(this).attr("id");
        }
        var proNum = 0;
        var pType = _parent.attr("types");
        var deleteSpecIds = new Object();
        var flag = false;
        if (_parent.find(".pro_spec_div").length > 0 && check == 1) {
            _parent.find(".pro_spec_div").each(function () {
                var specIds = $(this).attr("specIds");
                var specValue = $(this).attr("values");

                var num = $(this).find(".item-num").text();
                var price = $(this).find(".item-price").text();
                var bol = false;
                var specCheck = 0;
                var _checkObj = $(this).find(".check_pro_spec");
                if (_checkObj.length > 0 && $(this).parent().find(".ck-check").length > 0) {
                    if (!_checkObj.hasClass("ck-check")) {//没有选中
                        deleteSpecIds[specIds] = {
                            "num": num,
                            "value": specValue,
                            "price": price,
                            "isCheck": specCheck
                        };
                        bol = true;
                        flag = true;
                    }
                }
                if (bol) {
                    proNum = proNum + num * 1;
                }
            });
            if (deleteSpecIds != null && flag) {
                hs.num = proNum;
                hs.proSpecStr = JSON.stringify(deleteSpecIds)
                upArr[upArr.length] = hs;
            }
        } else {
            proNum = _parent.find(".num").val();
        }
    })
    if (delects == "0" && upArr.length == 0) {
        alert("你还选择移除的商品");
        return;
    } else {
        if (confirm("确认要删除选中的商品或商品规格？")) {
            var fade = $("#fade");
            var index = layer.open({
                title: "",
                content: "",
                type: 2,
                shadeClose: false
            });
            $.ajax({
                type: "post",
                url: "/mallPage/79B4DE7C/shoppingdelect.do",
                data: {
                    delects: delects,
                    upArr: JSON.stringify(upArr)
                },
                async: false,
                dataType: "json",
                success: function (data) {
                    var error = data.error;
                    if (error == 0) {
                        location.href = window.location.href;
                    } else {
                        alert("删除购物车商品失败");
                        parentCloseAll();
                        // layer.closeAll();
                    }
                }, error: function () {
                    // layer.closeAll();
                    parentCloseAll();
                }
            })
        }
    }

}
//清空失效商品
$(".clearCart").click(function () {
    var delects = "0";
    $(".isSX_div").each(function () {
        delects += "," + $(this).attr("id");
    });
    if (delects != "") {
        if (confirm("确认清空失效商品吗？")) {
            var index = layer.open({
                title: "",
                content: "",
                type: 2,
                shadeClose: false
            });
            $.ajax({
                type: "post",
                url: "/mallPage/79B4DE7C/shoppingdelect.do",
                data: {
                    delects: delects
                },
                async: false,
                dataType: "json",
                success: function (data) {
                    var error = data.error;
                    if (error == 0) {
                        location.href = window.location.href;
                    } else {
                        alert("确认清空失效商品失败");
                        parentCloseAll();
                    }
                }, error: function () {
                    parentCloseAll();
                }
            })
        }
    }
});

/**
 * 去结算
 */
function go_order() {
    var type = $("input.proType").val();
    var array = new Array();//声明数组
    var flag = false;
    $(".check_pro").each(function () {
        var _parent = $(this).parents(".box-main");
        var check = 1;//未选中
        var hs = {id: $(this).attr("id"), num: _parent.find(".num").val()};
        var isCheckFlag = false;
        if ($(this).hasClass("ck-check")) {
            check = 0;//选中
            flag = true;
            isCheckFlag = true;
        }
        var proNum = 0;
        var pType = _parent.attr("types");
        var specObj = new Object();
        if (_parent.find(".pro_spec_div").length > 0) {
            _parent.find(".pro_spec_div").each(function () {
                var specIds = $(this).attr("specIds");
                var specValue = $(this).attr("values");
                var proType = $(this).attr("proType");

                var num = $(this).find(".item-num").text();
                var price = $(this).find(".item-price").text();
                var specCheck = 0;
                var len = $(this).find(".check_pro_spec.ck-check").length;
                if (len > 0 || (proType == 1 && len == 0 && isCheckFlag)) {
                    specCheck = 1;
                    check = 0;
                    flag = true;
                }

                specObj[specIds] = {
                    "num": num,
                    "value": specValue,
                    "price": price,
                    "specName": $(this).attr("names"),
                    "isCheck": specCheck
                };
                proNum = proNum + num * 1;
            });
            hs.specStr = JSON.stringify(specObj);
        } else {
            proNum = _parent.find(".num").val();
        }
        hs.num = proNum;
        hs.check = check;

        array[array.length] = hs;
    });
    if (array.length == 0 || !flag) {
        alert("你还未选择商品");
        return;
    } else {

        $("#arrayJson").val(JSON.stringify(array));
        $("#totalnum").val($(".zjNum").text());
        $("#totalprice").val($(".zjPrice").text());
        var index = layer.open({
            title: "",
            content: "",
            type: 2,
            shadeClose: false
        });
        $("#queryForm").ajaxSubmit({
            type: "post",
            dataType: "json",
            url: "/mallPage/79B4DE7C/shoppingorder.do",
            success: function (data) {
                if (data.error == "0") {
                    $("#json").val(data.result)
                    $("#queryText").submit();
                } else {
                    parentCloseAll();
                    alert("结算购物车失败，请稍后结算");
                }
            }, error: function () {
                parentCloseAll();
            }
        });
    }

}