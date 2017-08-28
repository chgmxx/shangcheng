<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    /* String path=application.getRealPath(request.getRequestURI());   */
    String basePath = "http://" + request.getServerName() + ":" +
            request.getServerPort() +
            request.getContextPath() +
            request.getServletPath().substring( 0, request.getServletPath().lastIndexOf( "/" ) + 1 );
/* String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; */
%>
<!DOCTYPE>
<html ng-app="app">

<head>
    <base href="<%=basePath%>"/>
    <meta charset="utf-8">
    <title>${obj.pagName}</title>
    <meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="HandheldFriendly" content="true">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
    <style type="text/css">
        * {
            -webkit-text-size-adjust: 100% !important;
        }
    </style>
</head>

<body>
<!--加载动画-->
<!-- <section class="loading">
	<div class="load3">
		<div class="double-bounce1"></div>
		<div class="double-bounce2"></div>
	</div>
</section> -->

<link rel="stylesheet" href="css/base.css"/>

<link rel="stylesheet" href="css/swiper.min.css"/>
<link rel="stylesheet" href="css/main.css"/>
<link rel="stylesheet" href="css/reservation.css"/>

<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script src="js/angular.min.js"></script>
<script src="js/app.js"></script>
<script src="js/swiper.min.js"></script>

<link rel="stylesheet" href="js/admin/admin.css?<%=System.currentTimeMillis()%>"/>
<script src="js/admin/admin.js?<%=System.currentTimeMillis()%>"></script>

<script src="js/directive/countdown.js"></script>

<script src="js/directive/swiper.js"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>

<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>

<%-- <div class="tpl-shop">
    <div class="tpl-shop-header" style="background-color:#ffffff; background-image: url('${http}${mapbranch.sto_picture}');">
        <div class="tpl-shop-title">${name}</div>
        <div class="tpl-shop-avatar">
            <img src="${headImg}" alt="头像">
        </div>
    </div>
    <div class="tpl-shop-content">
        <ul class="clearfix">
            <li class="js-all-goods">
                <a href="/mallPage/${shopid}/79B4DE7C/shoppingall.do">
                    <span class="count">${countproduct}</span>
                    <span class="text">全部商品</span>
                </a>
            </li>
             <li class="js-new-goods">
                <a href="/mallPage/${shopid}/79B4DE7C/shoppingall.do">
                    <span class="count">${xincound}</span>
                    <span class="text">新上商品</span>
                </a>
            </li> 
            <li class="js-order">
                <a href="javascript:void(0);" onclick="toOrder();">
                    <span class="count user"></span>
                    <span class="text">我的订单</span>
                </a>
            </li>
        </ul>
    </div>
</div> --%>

<div class="pfj">
    <div style="height:51px;"></div>
    <div class="pfj-title">批发商</div>
    <div style="height:33px;"></div>
    <div class="pfj-main">1、申请成为批发商.
        1、申请成为批发商.
    </div>
    <div><i class="pfj-close" onclick="pfclose()"></i></div>
</div>

<admindraggable></admindraggable>

<div style="height:40px"></div>

<input type="hidden" class="shopid" value="${shopid }"/>
<jsp:include page="/jsp/mall/phoneFooterMenu.jsp"></jsp:include>

<input type="hidden" class="memberId" value="${memberId }"/>

<div class="fade" id="fade"></div>
<div class="attr-lay" style="display:none">
    <div class="lay-header">
        <div class="img-box">
            <img src="" id="image_srcs">
        </div>
        <div class="header-box">
            <div class="info">
                <p class="sum">￥<span id="sum-num">4800.27</span></p>
                <p class="inv-num">库存<span id="inventory">888</span>件</p>
            </div>
            <div class="gw_num">
                <em class="jian">-</em>
                <input type="tel" value="1" class="num" id="product_num">
                <em class="add">+</em>
            </div>
        </div>
        <span class="delete" onclick="hideDiv()">×</span>
    </div>
    <div class="lay-content">

    </div>
    <div class="lay-bottom">
        <a href="javascript:void(0)" onclick="addshopping()" class="add-shop shop-btn">加入购物车</a>
        <a href="javascript:void(0)" class="now-shop shop-btn" onclick="product_Buy()">立即购买</a>
    </div>
</div>
<input type="hidden" id="http" value="${http }">
<input type="hidden" id="shopid" value="${obj.pagStoId}"><!-- 店铺id  -->
<input type="hidden" id="shopname" value="${name}"><!-- 店铺id  -->
<input type="hidden" id="product_id"><!-- 商品id  -->
<input type="hidden" id="product_name"><!-- 商品名称  -->
<input type="hidden" id="yuan_image_url"><!-- 商品员图片  -->
<input type="hidden" id="primary_price"><!-- 商品原价钱  -->
<input type="hidden" id="pro_code"><!-- 商品编码  -->
<input type="hidden" id="return_day"><!-- 商品有效天数 -->
<input type="hidden" id="discount"><!-- 折扣数-->
<input type="hidden" id="isCoupons">
<input type="hidden" id="is_member_discount" value=""/>
<input type="hidden" id="pro_type_id" value=""/>
<input type="hidden" id="member_type" value=""/>
<input type="hidden" id="is_integral_deduction" value=""/>
<input type="hidden" id="is_fenbi_deduction" value=""/>

<input type="hidden" class="userid" value="${obj.pagUserId}"/>

<input type="hidden" class="img" value="${headImg }"/>
<input type="hidden" class="pageName" value="${obj.pagName}"/>
<form id="queryForm" method="post" action="/phoneOrder/79B4DE7C/toOrder.do">
    <input type="hidden" id="json" name="data">
    <input type="hidden" id="type" name="type" value="0">
</form>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
<script type='text/javascript' src="/js/plugin/lazyload/jquery.lazyload.min.js"></script>
<script type="text/javascript">
    var pageName = $("input.pageName").val();
    var title = $("input#shopname").val() + "-" + pageName;
    var imgUrls = $("input.img").val();
    var url = window.location.href;
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${record.get('appid')}", // 必填，公众号的唯一标识
        timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
        nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
        signature: "${record.get('signature')}",// 必填，签名，见附录1
        jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage", "showAllNonBaseMenuItem"] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });
    wx.ready(function () {
        //显示所有功能按钮接口
        wx.showAllNonBaseMenuItem();
        //分享到朋友圈
        wx.onMenuShareTimeline({
            title: title, // 分享标题
            link: url, // 分享链接
            imgUrl: imgUrls, // 分享图标
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
        //分享给朋友
        wx.onMenuShareAppMessage({
            title: title, // 分享标题
            desc: title, // 分享描述
            link: url, // 分享链接
            imgUrl: imgUrls, // 分享图标
            type: '', // 分享类型,music、video或link，不填默认为link
            dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
            success: function () {
                // 用户确认分享后执行的回调函数
            },
            cancel: function () {
                // 用户取消分享后执行的回调函数
            }
        });
    });
    $("div.fixRig").css({
        "right": "1.12rem"
    });
    $("div.fixRig a").css({
        "width": "2.12rem",
        "height": "2.12rem",
        "background-size": "auto 1rem"
    });
    $("div.fixRig a.top").css({
        "margin-bottom": "0.5rem"
    });

    var shopid = $("input#shopid").val();
    var userid = $("input.userid").val();
    var memberId = $("input.memberId").val();

    var guigePrice;//定义全局变量，规格价
    var discount = 1;//定义全局变量，规格价
    function hideDiv() {
        var fade = $("#fade");
        fade.hide();
        $(".attr-lay").hide();
        ;
    }
    /**
     * 购买
     */
    function buy_fuc(param) {
        if (memberId == null || memberId == "") {
            toLogin();
            return false;
        }
        event.preventDefault();
        var id = $(param).attr("data-id");
        $("#product_id").val(id);
        var url = "/mallPage/" + id + "/79B4DE7C/mainpageShopping.do";
        var fade = $("#fade");
        fade.show();
        $.ajax({
            type: "post",
            url: url,
            data: {},
            async: false,
            dataType: "json",
            success: function (data) {
                var error = data.error;
                if (error == 0) {
                    $("#product_num").val(1);
                    discount = data.discount;//折扣价格
                    var html = "";
                    var specificaList = data.specificaList;
                    var message = data.mapmessage;
                    $("#return_day").val(message.return_day);
                    $("#primary_price").val(message.pro_price);//商品原价
                    $("#pro_code").val(message.pro_code);//商品编码
                    $("#sum-num").text(message.pro_price * discount);//商品折扣价
                    $("#discount").val(discount);
                    $("#inventory").text(message.pro_stock_total);
                    $("#image_srcs").attr("src", $("#http").val() + message.image_url);
                    $("#yuan_image_url").val($("#http").val() + message.image_url);
                    $("#product_name").val(message.pro_name);
                    $("#isCoupons").val(message.is_coupons);
                    $("#is_member_discount").val(message.is_member_discount);
                    $("#pro_type_id").val(message.pro_type_id);
                    $("#member_type").val(message.member_type);
                    $("#is_integral_deduction").val(message.is_integral_deduction);
                    $("#is_fenbi_deduction").val(message.is_fenbi_deduction);
                    if (specificaList.length > 0) {
                        for (var i = 0; i < specificaList.length; i++) {
                            html += "<div class='content-box'>";
                            html += "<h2 class='title'>" + specificaList[i].specName + "</h2>";
                            html += "<ul class='inv-list'>";
                            var specvalues = specificaList[i].specValues;
                            console.log(specvalues);
                            for (var j = 0; j < specvalues.length; j++) {
                                html += "<li class='inv-item' option='" + specvalues[j].specValueId + "' optionvalue='" + specvalues[j].specValue + "'>" + specvalues[j].specValue + "</li>"
                            }
                            html += "</ul>";
                            html += "</div>";
                        }

                        var guige = data.guige;
                        $(".lay-content").html(html);
                        var xids = guige.xids;
                        var xid = xids.split(",");
                        for (var k = 0; k < xid.length; k++) {
                            $(".inv-item").each(function () {
                                if ($(this).attr("option") == xid[k]) {
                                    $(this).addClass("inv-item-active");
                                }
                            })
                        }
                        guigePrice = data.guigePrice;

                        guigeqh(xid);
                    } else {
                        $(".lay-content").html(html);
                    }

                    $(".attr-lay").show();
                } else {
                    alert(data.message);
                    var fade1 = $("#fade");
                    fade1.hide();
                }
            }
        });

    }
    function guigeqh(xid) {
        for (var i = 0; i < guigePrice.length; i++) {
            if (xid == guigePrice[i].xsid) {
                $("#primary_price").val(guigePrice[i].inv_price);//规格原价
                $("#sum-num").text(Math.ceil((guigePrice[i].inv_price * discount) * 100) / 100);//折扣价
                $("#inventory").text(guigePrice[i].inv_num);
                $("#pro_code").val(guigePrice[i].inv_code);
                if (guigePrice[i].specifica_img_id == 0) {
                    $("#image_srcs").attr("src", $("#yuan_image_url").val());
                } else {
                    $("#image_srcs").attr("src", $("#http").val() + guigePrice[i].specifica_img_url);
                }
            }
        }
    }
    $(function () {
        $(".attr-lay").on("click", ".jian", function () {
            var n = $(this).next().val();
            var num = parseInt(n) - 1;
            if (num == 0) {
                return
            }
            $(this).next().val(num);
        }).on("click", ".add", function () {
            var n = $(this).prev().val();
            var num = parseInt(n) + 1;
            var stock = parseInt($("#inventory").text());
            if (num > stock) {
                return;
            }
            if (num == $("#inventory").val()) {
                return;
            }
            $(this).prev().val(num);
        })
        $(".attr-lay").on("click", ".inv-item", function () {
            $(this).addClass("inv-item-active").siblings().removeClass("inv-item-active");
            var product_specificas = "";
            $(".inv-item-active").each(function () {
                product_specificas += $(this).attr("option") + ",";
            })
            if (product_specificas.length > 0) {
                product_specificas = product_specificas.substr(0, product_specificas.length - 1);
            }
            guigeqh(product_specificas);
        });

    })


    function addshopping() {
        if (memberId == null || memberId == "") {
            toLogin();
            return false;
        }
        var shopid = $("#shopid").val();//获取商铺id
        var product_id = $("#product_id").val();//获取商品id
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
        var product_num = $("#product_num").val();
        if (isNaN(product_num)) {
            alert("购买数量请输入合法数字");
            return;
        }
        if (product_num == 0) {
            alert("购买数量不能为0");
            return;
        }
        var price = $("#sum-num").text();
        var primary_price = $("#primary_price").val();
        var pro_code = $("#pro_code").val();//商品编码
        var discount = $("#discount").val();
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
                var error = data.error;
                if (error == 0) {
                    alert("商品已添加到购物车里");
                } else {
                    alert("商品添加到购物车失败");
                }
            }
        })
    }
    //立即购买
    function product_Buy() {
        if (memberId == null || memberId == "") {
            toLogin();
            return false;
        }
        var shopid = $("#shopid").val();//获取商铺id
        var product_id = $("#product_id").val();//获取商品id
        var product_num = $("#product_num").val();
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
        var price = $("#sum-num").text();
        var product_name = $("#product_name").val();//获取商品名
        var shop_namemessg = $("#shopname").val();
        var totalprice = parseInt(product_num) * price;
        var image_url = $("#image_srcs").attr("src");
        var primary_price = $("#primary_price").val();
        var pro_code = $("#pro_code").val();//商品编码
        var return_day = $("#return_day").val();
        var discount = $("#discount").val();
        var isCoupons = $("#isCoupons").val();
        var hs = {
            product_id: product_id,
            shop_id: shopid,
            product_specificas: product_specificas,
            product_speciname: product_speciname,
            totalnum: product_num,
            totalprice: totalprice,
            price: price,
            shop_name: shop_namemessg,
            product_name: product_name,
            image_url: image_url,
            primary_price: primary_price,
            pro_code: pro_code,
            return_day: return_day,
            discount: discount,
            groupType: 0,
            groupBuyId: 0,
            pJoinId: 0,
            isCoupons: isCoupons
        };
        hs["is_member_discount"] = $("#is_member_discount").val();
        hs["pro_type_id"] = $("#pro_type_id").val();
        hs["member_type"] = $("#member_type").val();
        hs["is_integral_deduction"] = $("#is_integral_deduction").val();
        hs["is_fenbi_deduction"] = $("#is_fenbi_deduction").val();
        $("#json").val(JSON.stringify(hs));
        sumbit();
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

    function searchFind() {
        var search = $(".search input").val();
        if (search != null && search != "" && (typeof search ) != "undefined") {
            location.href = "/mallPage/${shopid}/79B4DE7C/shoppingall.do?proName=" + search;
        } else {
            location.href = "/mallPage/${shopid}/79B4DE7C/shoppingall.do";
        }
    }
    /**
     * 消息提醒
     */
    function messageCli(param) {
        if (memberId == null || memberId == "") {
            toLogin();
            return false;
        }
        $.ajax({
            type: "post",
            url: "/phonePresale/79B4DE7C/messageRemind.do",
            data: {
                preId: param
            },
            async: false,
            dataType: "json",
            success: function (data) {
                var result = data.result;
                if (result) {
                    alert("消息提醒成功");
                    location.href = window.location.href;
                } else {
                    alert("消息提醒失败");
                }
            }
        });
    }
    function toOrder() {
        if (memberId != null && memberId != "") {
            location.href = "/phoneOrder/79B4DE7C/orderList.do?uId=" + userid;
        } else {
            toLogin();
        }
    }
    /**
     * 关闭批发价弹框
     */
    function pfclose() {
        $(".pfj").hide();
    }
    var dataJson = ${dataJson}; //样式数据
    picJson = ${picJson};	//图片id数据
    stoName = "${stoName}" || "";//店铺名称
    stoPicture = "${stoPicture}" || "";//店铺图片
    countproduct = "${countproduct}" || 0;//全部商品数量
    headImg = "${headImg}" || "";
    var verson = 0;
    if (dataJson.length > 0) {
        dataJson.forEach(function (e, i) {
            if (e.type == 7) {
                verson = 1;
                picJson.splice(i, 0, {type: 7, stoName: stoName, stoPicture: stoPicture, countproduct: countproduct, headImg: headImg})
            }
        })
    }
    if (verson == 0) {
        dataJson.unshift({type: 7, radio: true})
        picJson.unshift({type: 7, stoName: stoName, stoPicture: stoPicture, countproduct: countproduct, headImg: headImg})
    }


    var imgIdList = [];
    var imgIds = ",";
    picJson.forEach(function(e){
        if(e.type==1){
            e.imgID.forEach(function(e){
                if(imgIds.indexOf(","+e.id+",")<0){
                    imgIds += e.id+",";
                }
            })
        }
    })
    function picJsonEach(data){
        picJson.forEach(function(e){
            if(e.type==1){
                e.imgID.forEach(function(e){
                    data.forEach(function(data){
                        if(e.id == data.id){
                            e.price = data.price;
                            e.src = data.src;
                            e.title = data.title;
                            if(data.url != null && data.url != ""){
                                e.url = data.url;
                            }
                        }
                    })
                })
            }
        })
    }

</script>
</body>
</html>