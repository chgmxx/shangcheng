<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE>
<html>

<base href="<%=basePath%>"/>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="HandheldFriendly" content="true">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <meta name="full-screen" content="yes">
    <meta name="x5-fullscreen" content="true">
    <meta name="browsermode" content="application">
    <meta name="x5-page-mode" content="app">
    <meta name="msapplication-tap-highlight" content="no">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <title>${mapmessage.pro_name }</title>
    <link rel="stylesheet" type="text/css" href="/css/reset.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/phoneDetail.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/template/common/swiper.min.css?<%=System.currentTimeMillis()%>"/>
</head>
<body>
<input type="hidden" id="discount" value="${discount}">
<input type="hidden" id="return_day" name="return_day" value="${mapmessage.return_day}">
<input type="hidden" name="shop_name" value='${shopmessage.business_name }' id="shop_name">
<input type="hidden" name="prodect_price"
       value='<c:if test="${mapmessage.is_specifica eq 1 }">${((mapmessage.inv_price*100) * (discount*100))/10000}</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${((mapmessage.pro_price*100) * (discount*100))/10000}</c:if>'
       id="prodect_price"><!--商品折扣价  -->
<input type="hidden"
       value='<c:if test="${mapmessage.is_specifica eq 1 }">${mapmessage.inv_price }</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${mapmessage.pro_price }</c:if>'
       id="primary_price"><!--商品原价  -->

<input type="hidden" name="group_price" value='' id="group_price"><!--团购折扣价  -->

<input type="hidden"
       value='<c:if test="${mapmessage.is_specifica eq 1 }">${mapmessage.inv_code }</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${mapmessage.pro_code }</c:if>'
       id="pro_code"><!--商品原价  -->

<input type="hidden" class="rType" value="${rType }"/>
<input type="hidden" class="isChangeIntegral" value="${mapmessage.is_integral_change_pro }"/>
<input type="hidden" class="integral" value="${mapmessage.change_integral }"/>
<input type="hidden" class="is_member_discount" value="${mapmessage.is_member_discount }"/>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<div class="Warp">
    <div class="main">
        <section class="pic-carousel">
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <c:forEach items="${imagelist }" var="lists">
                        <div class="swiper-slide">
                            <img src="${http}${lists.image_url}"/>
                        </div>
                    </c:forEach>
                </div>

            </div>
            <div class="page">
                <span id="indexNum">1</span>/<span>${fn:length(imagelist)}</span>
            </div>
        </section>
        <div class="price-box">
            <h2 class="product_nameclass">${mapmessage.pro_name }</h2>
            <c:set var="isOffer" value="0"></c:set>
            <c:set var="isMargin" value="0"></c:set>
            <c:if test="${!empty auction && auction.aucType == 2 && (auction.isMargin == 0 or (auction.isMargin == 1 && marginSize > 0)) }">
                <c:set var="isOffer" value="1"></c:set>
            </c:if>
            <c:if test="${!empty auction && auction.isMargin == 1 && marginSize == 0}">
                <c:set var="isMargin" value="${auction.isMargin }"></c:set>
            </c:if>
            <c:if test="${auction.status != -1 }">
                <div class="price pad <c:if test="${isMargin == 1}">black</c:if>">
                    <c:if test="${!empty auction}">
                        当前价：
                    </c:if>
                    ￥<i class="priceclass"></i>
                    <c:if test="${auction.aucType == 2 }">
                        <em class="black">出价次数 ${offerList.size()} 次</em>
                    </c:if>
                    <c:if test="${auction.aucType == 1 }">
                        <em class="black">抢拍次数 ${bidList.size()} 次</em>
                    </c:if>
                </div>
                <c:if test="${isMargin == 1}">
                    <div class="price pad">
                        保证金：
                        ￥<i class="margin_i">${auction.aucMargin }</i>
                        <em class="black">不成拍结束后退款</em>
                    </div>
                </c:if>
                <c:if test="${isOffer == 1 && auction.status == 1}">
                    <div class="price pad">
                        出价：
                        ￥<input type="text" class="addOffer" value="${auction.nowPrice+auction.aucAddPrice }" maxLength="8"/>
                    </div>
                </c:if>
            </c:if>
            <c:if test="${!empty isWin && isWin == 1}">
                <div class="viewPrices">
                    <div style="padding:0px 20px;">恭喜！您已胜出！</div>
                </div>
                <div class="viewPrices">
                    <div style="padding:0px 20px;">当前价：
                        <em style="color:#F20000;">￥${winOffer.offerMoney }</em>
                    </div>
                </div>
                <c:if test="${isSubmit == 0 && isWin == 1}">
                    <div style="padding:0px 20px;font-size:20px;color:#828282;">
                        请在24小时内提交订单：若在24小时内未提交订单，系统将自动关闭交易，并扣除您的保证金用于赔付送拍机构
                    </div>
                </c:if>
            </c:if>
            <c:if test="${!empty isWin && isWin == 0}">
                <div class="viewPrices">
                    <div style="padding:0px 20px;">拍卖已成交</div>
                </div>
                <div class="viewPrices">
                    <div style="padding:0px 20px;">当前价：
                        <em style="color:#F20000;">￥${winOffer.offerMoney }</em>
                    </div>
                </div>
            </c:if>
            <div class="viewPrices">
                <div class="start-price pad" style="display:none;">
                    起拍价：￥<span></span>
                </div>
                <div class="before-price pad">
                    原价：<span>￥${mapmessage.pro_cost_price }</span>
                </div>
                <c:if test="${mapmessage.is_show_views == 1 }">
					<span class="viewSpan">
						<em>关注量</em>${viewNum }
					</span>
                </c:if>
            </div>
            <c:if test="${!empty auction && auction.status != -1}">
                <div class="flex-1 count-down <c:if test="${auction.status == 0 }">startTimes</c:if>" id="time-item" status="${auction.status}">
                    <label for="">
                        <c:if test="${auction.status == 1 }">活动剩余时间</c:if>
                        <c:if test="${auction.status == 0 }">活动开始剩余时间</c:if>
                    </label>
                    <input type="hidden" class="diffTimes" value="${auction.times }"/>
                    <input type="hidden" class="startTimes" value="${auction.startTimes }"/>
                    <input type="hidden" class="times" value="0"/>
                    <span>
      	   				<span><span id="day_show" class="red-txt day_show">0</span>天</span>
      	   				<span><span id="hour_show" class="red-txt hour_show">0</span>时</span>
      	   				<span><span id="minute_show" class="red-txt minute_show">0</span>分</span>
      	   				<span><span id="second_show" class="red-txt second_show">0</span>秒</span>
     	   			</span>
                </div>
            </c:if>
        </div>
        <div class="select pad ">
            <!--商品数量  -->
            <input type="hidden" id="product_num" name="product_num" value=1>
            <input type="hidden" id="xids" name="xids" value="${guige.xids}">
            <input type="hidden" id="specifica_name" name="specifica_name" value="${guige.specifica_name}">
            <input type="hidden" id="maxBuy" value="${mapmessage.pro_restriction_num }"/>
            <input type="hidden" id="auctionCount" value="<c:if test='${!empty auctionCount }'>${auctionCount }</c:if>"/>
            <input type="hidden" id="isCoupons" value="${mapmessage.is_coupons }"/>
            <div onclick="showDiv()">
                <i class="specidsname"> ${guige.specifica_name }</i>
                数量:1件
            </div>

        </div>
        <div id="" class="address pad">
            <div style="width:715px;float:left;">
                <c:if test="${!empty addressMap }">
                    <div class="addr-box">
                        送至：<em></em><span class="addr">${addressMap.pName }${addressMap.cName }${addressMap.aName }${addressMap.mem_address }</span>
                    </div>
                </c:if>
                <div style="<c:if test="${!empty addressMap }">line-height:30px;</c:if>">
                    <span class="pay"><c:if test="${!empty priceMap && priceMap>0 }">运费：￥${priceMap }</c:if><c:if test="${empty priceMap || priceMap == 0 }">免运费</c:if></span>
                    <span>销量：<label>${mapmessage.pro_sale_total}</label>件</span>
                </div>
            </div>
            <input type="hidden" class="fMoney" value="${priceMap }"/>
        </div>

        <div class="go-other pad">
            <div class="mall-name">
                <img src="${http}<c:if test='${shopmessage.stoPicture == \'\' || shopmessage.stoPicture == null}'>${shopmessage.sto_picture}</c:if><c:if test='${shopmessage.stoPicture != \'\' && shopmessage.stoPicture != null }'>${shopmessage.stoPicture}</c:if>"/>
                <p class="shop_namemessg"><c:if test='${shopmessage.business_name == "" }'>${shopmessage.sto_name}</c:if><c:if
                        test='${shopmessage.business_name != "" }'>${shopmessage.business_name}</c:if></p>
            </div>
            <div class="other-mall">
                <a href="mallPage/${shopid}/79B4DE7C/shoppingall.do">全部商品</a>
                <a href="javascript:void(0)" onclick="pageclick('${pageid}')">进入店铺</a>
                <a href="mallPage/${id}/79B4DE7C/shopdetails.do">商品详情</a>
            </div>

        </div>
    </div>
    <footer class="footer">
        <ul>
            <li class="foot-item" onclick="PersonalCenter()"><img src="/images/mall/img/mall-person.png"/>
                <p>我的</p></li>
            <li class="foot-item" onclick="shoppingcart()"><img src="/images/mall/img/mall-shop.png"/>
                <p>购物车</p>
                <i class="shopping-icon">0</i>
            </li>
            <c:if test="${empty auction}">
                <li class="foot-item2"><a href="javascript:void(0)" class="add-shop shop-btn" onclick="addshopping()">加入购物车</a></li>
                <li class="foot-item2"><a href="javascript:void(0)" class="now-shop shop-btn" onclick="productBuy(0)">立即购买</a></li>
            </c:if>
            <li class="foot-item2" style="float:right;">
                <c:set var="flag" value="0"></c:set>
                <c:set var="cla" value="a"></c:set>
                <c:if test="${!empty auction}">
                    <c:if test="${auction.status == 0 && auction.startTimes > 0}">
                        <c:set var="cla" value="noStart"></c:set>
                    </c:if>
                    <c:if test="${auction.isMargin == 0 }">
                        <c:if test="${auction.aucType == 1 }">
                            <a href="javascript:void(0)" onclick="productBuy(4)" class="now-shop shop-btn ${cla }">
                                <c:if test="${auction.status == 0}">即将开始拍卖</c:if>
                                <c:if test="${auction.status == 1}">立即拍卖</c:if>
                            </a>
                            <c:set var="flag" value="1"></c:set>
                        </c:if>
                        <c:if test="${auction.aucType == 2}">
                            <c:if test="${empty isWin }">
                                <a href="javascript:void(0)" onclick="addOffer(4)" class="now-shop shop-btn ${cla }">
                                    <c:if test="${auction.status == 0}">即将开始拍卖</c:if>
                                    <c:if test="${auction.status == 1}">出价</c:if>
                                </a>
                                <c:set var="flag" value="1"></c:set>
                            </c:if>
                            <c:if test="${!empty isWin && isWin == 1 && isSubmit == 0}">
                                <a href="javascript:void(0)" onclick="productBuy(4)" class="now-shop shop-btn ${cla }">
                                    提交订单
                                </a>
                                <c:set var="flag" value="1"></c:set>
                            </c:if>
                        </c:if>
                    </c:if>
                    <c:if test="${auction.isMargin == 1 }">
                        <a href="javascript:void(0)" onclick="addMargin()" class="seckill-btn now-shop shop-btn ${cla }">
                            交保证金报名
                        </a>
                        <c:set var="flag" value="1"></c:set>
                    </c:if>
                </c:if>
                <c:if test="${flag == 0 }">
                    <a href="/mallPage/${mapmessage.id }/${mapmessage.shop_id }/79B4DE7C/phoneProduct.do" class="now-shop shop-btn ${cla }">
                        前去购买
                    </a>
                </c:if>
            </li>
        </ul>
    </footer>

    <!--遮罩层-->
    <div class="fade" id="fade" onclick="hideDiv()"></div>
    <!--弹出层-->
    <div class="attr-lay" style="display: none;">
        <div class="lay-header">
            <div class="img-box">
                <img src="${http}${mapmessage.image_url}" id="attr_image"/>
            </div>
            <div class="header-box">
                <div class="info">
                    <p class="sum">
                        ￥<span id="attr_lay_price">${mapmessage.pro_price * discount}</span>
                        <span class="before-price pad" style="display:none;float:none;">
					 			原价：￥<span>${mapmessage.pro_cost_price }</span>
							</span>
                        <span class="start-price pad" style="display:none;float:none;">
								 起拍价：￥<span></span>
							</span>
                    </p>
                    <p class="inv-num">库存<span id="inventory">${mapmessage.pro_stock_total}</span>件</p>
                </div>
                <div class="gw_num" style="display: none;">
                    <em class="jian">-</em>
                    <input type="tel" value="1" class="num" id="attr_lay_num" maxlength="6"/>
                    <em class="add">+</em>
                </div>
            </div>
            <i class="delete" onclick="hideDiv()"></i>
        </div>
        <div class="lay-content">
            <c:forEach items="${specificaList }" var="specifica">
                <div class="content-box" option="${specifica.specNameId}">
                    <h2 class="title">${specifica.specName}</h2>
                    <ul class="inv-list">
                        <c:forEach items="${specifica.specValues }" var="specValues">
                            <li class="inv-item " option="${specValues.specValueId}" optionvalue="${specValues.specValue}">${specValues.specValue}</li>
                        </c:forEach>
                    </ul>
                </div>
            </c:forEach>
        </div>
    </div>
    <c:forEach items="${guigePrice }" var="guigePrice">
        <input type="hidden" class="guigePrice_value" option="${guigePrice.xsid}" optionprice="${guigePrice.inv_price * discount}" optionnum="${guigePrice.inv_num}"
               optionimage="${http}${guigePrice.specifica_img_url}" optionspecifica_img_id="${guigePrice.specifica_img_id} " optionyuanprice="${guigePrice.inv_price}"
               optioncode="${guigePrice.inv_code }">
    </c:forEach>
    <c:if test="${!empty auction }">
        <input type="hidden" class="nowPrice" value="${auction.nowPrice }"/>
        <input type="hidden" class="startPrice" value="${auction.aucStartPrice }"/>
        <input type="hidden" class="lowestPrice" value="${auction.aucLowestPrice }"/>
        <input type="hidden" class="lowerPriceTime" value="${auction.aucLowerPriceTime }"/>
        <input type="hidden" class="lowerPrice" value="${auction.aucLowerPrice }"/>
        <input type="hidden" class="aucId" value="${auction.id }"/>
        <input type="hidden" id="sNum" value="${auction.aucNum }"/>
        <input type="hidden" id="xgNum" value="${auction.aucRestrictionNum }"/>
        <input type="hidden" id="isMargin" value="${auction.isMargin }"/>
        <input type="hidden" id="margin" value="${auction.aucMargin }"/>
        <input type="hidden" id="addPrice" value="${auction.aucAddPrice }"/>
        <input type="hidden" id="aucType" value="${auction.aucType }"/>
        <input type="hidden" id="aucStartTimes" value="${auction.aucStartTime }"/>
        <input type="hidden" class="bidSize" value="${bidList.size() }"/>
        <input type="hidden" class="aStatus" value="${auction.status }">
    </c:if>
</div>
<form id="queryForm" method="post" action="/phoneOrder/79B4DE7C/toOrder.do">
    <input type="hidden" id="json" name="data">
    <input type="hidden" id="type" name="type" value="0">
</form>

<c:if test="${empty addressMap && empty loginCity}">
    <iframe id="geoPage" width="100%" height="30%" frameborder=0 scrolling="no" style="display:none;"
            src="http://apis.map.qq.com/tools/geolocation?key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp&effect=zoom"></iframe>

    <!-- 接收到位置信息后 通过 iframe 嵌入位置标注组件 -->
    <iframe id="markPage" width="100%" height="70%" frameborder=0 scrolling="no" src="" style="display:none;"></iframe>
</c:if>
<input type="hidden" id="costPrice" value="${mapmessage.pro_cost_price }"/>
<input type="hidden" id="proPrice" value="${mapmessage.pro_price }"/>
<input type="hidden" id="invPrice" value="${mapmessage.inv_price }"/>
<input type="hidden" id="proid" value="${id}">
<input type="hidden" id="shopid" value="${shopid}">
<input type="hidden" id="yuan_http" name="yuan_http" value="${http}">
<input type="hidden" id="yuan_image_url" name="yuan_image_url" value="${http}${mapmessage.image_url}">
<input type="hidden" class="stockTotal" value="${mapmessage.pro_stock_total}"/>
<script src="/js/plugin/jquery-1.8.3.min.js?<%=System.currentTimeMillis()%>"></script>

<script type="text/javascript" src="/js/plugin/swiper.min.js"></script>
<script src="/js/plugin/jquery.fly.js"></script>
<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript">
    var discount = $("#discount").val() * 1;
    var memberId = ${memberId};
    var aucLength = $(".aucId").length;
    $(function () {
        var a = $(window).width(),
            b = $(window).height(),
            d = 870,
            meta = $("#meta");
        setTimeout(function () {
            meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
            $(".loading").hide();
        }, 300);

    });
    var mySwiper = new Swiper('.swiper-container', {
        autoplay: 5000,//可选选项，自动滑动
        onSlideChangeStart: function (swiper) {
            $("#indexNum").text(mySwiper.activeIndex + 1);
        }
    });
    //立即购买,立即开团,立即拍卖
    function productBuy(types) {
        if ($("#time-item").length > 0) {
            var status = $("#time-item").attr("status");
            if (status == 0 && $("#time-item").hasClass("startTimes")) {
                alert("拍卖活动还没开始，请耐心等待。");
                return;
            }
        }
        var shopid = $("input#shopid").val();//获取商铺id
        var product_id = $("input#proid").val();//获取商品id
        var product_specificas = $("#xids").val();//产品规格,存多个规格，用;分开
        var product_speciname = $("#specifica_name").val();
        var product_num = 1;
        var price = $("#prodect_price").val();
        ;
        var product_name = $(".product_nameclass").text();//获取商品名;//获取商品名
        var shop_namemessg = $("#shop_name").val();
        var totalprice = price;
        if ($(".guigePrice_value").length > 0) {
            $(".guigePrice_value").each(function () {
                if ($(this).attr("option") == product_specificas) {
                    image_url = $(this).attr("optionimage");
                    var gPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                    /* $("#attr_lay_price").text(gPrice);
                     $("#primary_price").val($(this).attr("optionyuanprice"));
                     $("#pro_code").val($(this).attr("optioncode")); */
                    $("#inventory").text($(this).attr("optionnum"));

                    if ($(this).attr("optionspecifica_img_id") == 0) {
                        $("#attr_image").attr("src", $("#yuan_image_url").val());
                    } else {
                        $("#attr_image").attr("src", $(this).attr("optionimage"));
                    }
                }
            });
        }
        var image_url = $("#attr_image").attr("src");
        var primary_price = $("#primary_price").val();
        var pro_code = $("#pro_code").val();
        var return_day = $("#return_day").val();
        var discount = $("#discount").val();
        var groupBuyId = 0;//存放拍卖id
        var pJoinId = -1;
        var flag = true;
        var isCoupons = $("#isCoupons").val();
        var myIntegral = $(".myIntegral").val();
        if (types == 4) {//拍卖
            price = $(".nowPrice").val();
            groupBuyId = $(".aucId").val();
            if (groupBuyId == undefined || groupBuyId == null || groupBuyId == "") {
                groupBuyId = 0;
            }
            pJoinId = 0;
            var xgNum = $("input#xgNum").val();//拍卖商品限购
            var bidSize = $(".bidSize").val();//已经参加拍卖的数量
            var aucType = $("input#aucType").val();
            if (xgNum != "" && xgNum != "0" && aucType == "1") {//降价拍限购每人购买次数
                if (bidSize * 1 + product_num * 1 > xgNum * 1) {
                    alert("每人限购" + xgNum + "件，您已超过每人购买次数限制");
                    flag = false;
                }
            }
        } else {
            totalprice = price * product_num;
            /* var maxBuy = $("#maxBuy").val();
             if(maxBuy != "" && maxBuy != "0"){
             if(product_num*1 > maxBuy*1){
             alert("每人限购"+maxBuy+"件，超过每人购买次数限制");
             flag = false;
             }
             } */
        }
        if ($("#inventory").text() == "0" || $(".stockTotal").val() == "0") {
            alert("该商品的库存不够，不能拍卖");
            flag = false;
        }
        if (flag) {
            //console.log(types+"==="+price)
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
                groupType: types,
                groupBuyId: groupBuyId,
                pJoinId: pJoinId,
                isCoupons: isCoupons
            }
            hs["is_member_discount"] = $(".is_member_discount").val();
            $("#json").val(JSON.stringify(hs));
            //console.log(hs)
            sumbit();
        }
    }


</script>
<script type="text/javascript" src="/js/mall/auction/phone/auctiondetail.js"></script>
</body>
</html>