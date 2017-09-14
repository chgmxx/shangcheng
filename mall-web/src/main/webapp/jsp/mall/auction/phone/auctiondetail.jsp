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
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
</head>
<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>

<link rel="stylesheet" type="text/css" href="/css/reset.css?<%=System.currentTimeMillis()%>"/>
<link rel="stylesheet" type="text/css" href="/css/mall/auction/auction.css?<%=System.currentTimeMillis()%>"/>
<link rel="stylesheet" type="text/css" href="/css/template/common/swiper.min.css?<%=System.currentTimeMillis()%>"/>
<link rel="stylesheet" type="text/css" href="/css/mall/product/detaiLayer.css"/>

<input type="hidden" id="discount" value="${discount}">
<input type="hidden" id="return_day" name="return_day" value="${mapmessage.return_day}">
<input type="hidden" name="shop_name" value='${shopmessage.business_name }' id="shop_name">
<input type="hidden" name="prodect_price"
       value='<c:if test="${mapmessage.is_specifica eq 1 }">${((mapmessage.inv_price*100) * (discount*100))/10000}</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${((mapmessage.pro_price*100) * (discount*100))/10000}</c:if>'
       id="prodect_price"><!--商品折扣价  -->
<input type="hidden" id="primary_price"
       value='<c:if test="${mapmessage.is_specifica eq 1 }">${mapmessage.inv_price }</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${mapmessage.pro_price }</c:if>'>
<!--商品原价  -->

<input type="hidden" name="group_price" value='' id="group_price"><!--团购折扣价  -->

<input type="hidden"
       value='<c:if test="${mapmessage.is_specifica eq 1 }">${mapmessage.inv_code }</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${mapmessage.pro_code }</c:if>'
       id="pro_code"><!--商品原价  -->

<input type="hidden" class="rType" value="${rType }"/>
<input type="hidden" class="isChangeIntegral" value="${mapmessage.is_integral_change_pro }"/>
<input type="hidden" class="integral" value="${mapmessage.change_integral }"/>
<input type="hidden" class="is_member_discount" value="${mapmessage.is_member_discount }"/>
<input type="hidden" class="buyCode" value="${buyCode }">
<input type="hidden" class="is_integral_deduction" value="${mapmessage.is_integral_deduction }"/>
<input type="hidden" class="is_fenbi_deduction" value="${mapmessage.is_fenbi_deduction }"/>
<input type="hidden" class="is_specifica" value="${mapmessage.is_specifica }"/>

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
                <span id="indexNum">1</span>/<span id="imgNum">${fn:length(imagelist)}</span>
            </div>
        </section>
        <div class="price-box">
            <h2 class="product_nameclass">
                <c:if test="${!empty mapmessage.pro_label}"><i class="label_i">${mapmessage.pro_label }</i></c:if>
                ${mapmessage.pro_name }
            </h2>
            <c:set var="isOffer" value="0"></c:set>
            <c:set var="isMargin" value="0"></c:set>
            <c:if test="${!empty auction && auction.aucType == 2 && (auction.isMargin == 0 or (auction.isMargin == 1 && marginSize > 0)) }">
                <c:set var="isOffer" value="1"></c:set>
            </c:if>
            <c:if test="${!empty auction && auction.isMargin == 1 && marginSize == 0}">
                <c:set var="isMargin" value="${auction.isMargin }"></c:set>
            </c:if>
            <div class="price pad">
                <span class="price-new grey-txt">当前价：￥ <label for="" class="red-txt priceclass">${auction.nowPrice }</label></span>
                <c:if test="${auction.aucType == 2 }">
                    <span class="price-old">出价：<label>${offerList.size()}</label>次</span>
                </c:if>
                <c:if test="${auction.aucType == 1 }">
                    <span class="price-old">抢拍：<label>${bidList.size()}</label>次</span>
                </c:if>
                <c:if test="${empty auction}">
						 <span class="auction-hammer bid-hammer ${cla } now-shop"
                               onclick="javascript:location.href='/mallPage/${id}/${shopid }/79B4DE7C/phoneProduct.do'">前去购买</span>
                </c:if>
                <c:set var="isCollectPro" value="0"></c:set>
                <c:if test="${!empty isCollect }">
                    <c:set var="isCollectPro" value="${isCollect }"></c:set>
                </c:if>
                <div class="collects_nav">
                    <div class="collect" onclick="collectPro(${isCollectPro},'${collectId }');">
                        <c:if test="${isCollectPro == 0}">
                            <img src="/images/mall/new/collect.png"/>
                            <p>收藏</p>
                        </c:if>
                        <c:if test="${isCollectPro == 1}">
                            <img src="/images/mall/new/collected.png"/>
                            <p>已收藏</p>
                        </c:if>
                    </div>
                </div>
            </div>
            <div class="price pad">
                <c:if test="${isOffer == 1 && empty isWin}"><!-- && auction.status == 1 -->
                <c:set var="inpPrice" value="${auction.nowPrice+auction.aucAddPrice }"></c:set>
                <c:if test="${offerList.size() == 0}">
                    <c:set var="inpPrice" value="${auction.aucStartPrice }"></c:set>
                </c:if>
                <span class="price-new grey-txt">出&nbsp;&nbsp;&nbsp;价：￥
						     <input type="text" class="bid-price addOffer" value="${inpPrice }" min="${inpPrice }" maxLength="8"/>
						  </span>
                <input type="button" class="bid-change add" name="" id="" value="+"/>
                <input type="button" class="bid-change jian" name="" id="" value="-"/>
                <span class="auction-hammer bid-hammer ${cla } now-shop" onclick="addOffer(4)">
						  	<img src="/images/mall/hammer.png" class="hammer"/>	出价
						  </span>
                </c:if>
                <c:if test="${isMargin == 1}">
							<span class="price-new grey-txt">保证金：
							￥<label for="" class="red-txt margin_i">${auction.aucMargin }</label>
							</span>
                    <span class="price-old">不成拍结束后退款</span>
                </c:if>
            </div>
            <c:if test="${!empty isWin && isWin == 1}">
                <div class="viewPrices">
                    <div class="wins">恭喜！您已胜出！</div>
                </div>

                <c:if test="${isSubmit == 0 && isWin == 1}">
                    <div style="padding:0px 20px;font-size:20px;color:#828282; line-height: 30px;">
                        请在24小时内提交订单：若在24小时内未提交订单，系统将自动关闭交易，并扣除您的保证金用于赔付送拍机构
                    </div>
                </c:if>
                <c:if test="${isSubmit == 0}">
                    <div class="viewPrices" style="height:auto;margin-bottom:10px;">
							 <span class="auction-hammer bid-hammer ${cla }" onclick="productBuy(4,1)" style="margin-left: 20px;">
							  	转订单
							  </span>
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
            <div class="viewPrices" <c:if test="${mapmessage.is_show_views == 0}">style="display:none;"</c:if>>
                <div class="start-price pad" style="display:none;">
                    起拍价：￥<span></span>
                </div>
                <div class="before-price pad" style="display:none;">
                    原价：<span>￥${mapmessage.pro_cost_price }</span>
                </div>
                <c:if test="${mapmessage.is_show_views == 1 }">
						<span class="viewSpan">
							<em>关注量</em>${viewNum }
						</span>
                </c:if>
            </div>
            <c:if test="${!empty auction && auction.status != -1}">
                <div class="flex auction-time">
                    <div class="flex-1">
                        <div class="count-down <c:if test="${auction.status == 0 }">startTimes</c:if>" id="time-item" status="${auction.status}">
                            <label for="">
                                <c:if test="${auction.status == 1 }">距离结束时间</c:if>
                                <c:if test="${auction.status == 0 }">距离开始时间</c:if>
                            </label>
                            <input type="hidden" class="diffTimes" value="${auction.times }"/>
                            <input type="hidden" class="startTimes" value="${auction.startTimes }"/>
                            <input type="hidden" class="times" value="0"/>
                            <span id="day_show" class="red-txt day_show">0</span>天
                            <span id="hour_show" class="red-txt hour_show">0</span>时
                            <span id="minute_show" class="red-txt minute_show">0</span>分
                            <span id="second_show" class="red-txt second_show">0</span>秒
                        </div>
                        <div class="lookers">
                            <c:if test="${isMargin == 1 }">
                                <label for="">${marginSize }</label>人已报名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                        </div>
                    </div>
                    <c:if test="${isMargin == 1 && auction.isMargin == 1}">
                        <div class="flex-1">
                            <div class="auction-hammer">
                                <a href="javascript:void(0);" onclick="margins();" class="">
                                    <img src="/images/mall/hammer.png" class="hammer"/>
                                    <span>交保证金报名</span>
                                </a>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${auction.isMargin == 1 && memberId == null}">
                        <div class="flex-1">
                            <div class="auction-hammer" style="width:120px;text-align: center;">
                                <a href="javascript:void(0);" onclick="toLogin();" class="">
                                    <span>前往登陆</span>
                                </a>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${isMargin == 0 && auction.aucType == 1}">
                        <div class="flex-1">
                            <div class="auction-hammer now-shop" style="width: 200px;margin-left:0px;">
                                <a href="javascript:void(0);" onclick="productBuy(4,1)" class="">
                                    <img src="/images/mall/hammer.png" class="hammer"/>
                                    <span>立即拍下</span>
                                </a>
                            </div>
                        </div>
                    </c:if>
                </div>
            </c:if>
        </div>
        <c:if test="${!empty auction }">
            <div class="auction-info">
                <div class="flex">
                    <div class="flex-1 text-overflow">起拍价： ￥${auction.aucStartPrice }元</div>
                    <div class="flex-1 text-overflow">保留价：
                        <c:if test="${auction.aucLowestPrice !='' && auction.aucLowestPrice !='0'  }">
                            ￥${auction.aucLowestPrice }元
                        </c:if>
                        <c:if test="${auction.aucLowestPrice =='' || auction.aucLowestPrice =='0'  }">
                            元
                        </c:if>
                    </div>
                    <div class="flex-1">保证金：
                        <c:if test="${auction.isMargin == 0}">无</c:if>
                        <c:if test="${auction.isMargin == 1}">￥${auction.aucMargin }元</c:if>
                    </div>
                </div>
                <div class="flex">
                    <c:if test="${auction.aucType == 2}">
                        <div class="flex-1">加价幅度：￥${auction.aucAddPrice }元</div>
                    </c:if>
                    <c:if test="${auction.aucType == 1}">
                        <div class="flex-1">降价幅度：每${auction.aucLowerPriceTime }分钟降${auction.aucLowerPrice }元</div>
                    </c:if>
                </div>
                <div class="flex">
                    <div class="flex-1">拍卖方式：
                        <c:if test="${auction.aucType == 2}">升价拍</c:if>
                        <c:if test="${auction.aucType == 1}">降价拍</c:if>
                    </div>
                    <c:if test="${auction.aucType == 1}">
                        <div class="flex-1">
                        </div>
                    </c:if>
                </div>
                <div>服务支持：
                    <c:if test="${mapmessage.is_return == 0 }">不支持7天无理由退货</c:if>
                    <c:if test="${mapmessage.is_return == 1 }">支持${mapmessage.return_day }天无理由退货</c:if>
                </div>
            </div>
        </c:if>
        <c:if test="${!empty mapmessage.product_message}">
            <div class="select pad msgDiv" onclick="showMessage(this);">
                <c:set var="str" value="${mapmessage.product_message }"/>
                <c:choose>
                    <c:when test="${fn:length(str) > 23}">
                        <c:set var="str" value="${fn:substring(str, 0, 23)}..."/>
                    </c:when>
                    <c:otherwise>
                        <c:set var="str" value="${str}"/>
                    </c:otherwise>
                </c:choose>
                <div>${str }</div>
                <input type="hidden" class="product_message" value="${mapmessage.product_message }"/>
            </div>
        </c:if>
        <div class="select pad ">
            <!--商品数量  -->
            <input type="hidden" id="product_num" name="product_num" value=1>
            <input type="hidden" id="xids" name="xids" value="${guige.xids}">
            <input type="hidden" id="specifica_name" name="specifica_name" value="${guige.specifica_name}">
            <input type="hidden" id="maxBuy" value="${mapmessage.pro_restriction_num }"/>
            <input type="hidden" id="auctionCount" value="<c:if test='${!empty auctionCount }'>${auctionCount }</c:if>"/>
            <input type="hidden" id="isCoupons" value="${mapmessage.is_coupons }"/>
            <input type="hidden" id="pro_type_id" value="${mapmessage.pro_type_id }"/>
            <input type="hidden" id="member_type" value="${mapmessage.member_type }"/>
            <div onclick="showDiv()">
                <i class="specidsname"> ${guige.specifica_name }</i>
                规格与数量:1件
            </div>

        </div>
        <div id="" class="address pad">
            <div class="add_div" style="width:715px;float:left;">
                <c:if test="${!empty addressMap }">
                    <div class="addr-box">
                        送至：<em></em><span class="addr">${addressMap.provincename }${addressMap.cityname }${addressMap.areaname }${addressMap.memAddress }</span>
                    </div>
                </c:if>
                <div style="<c:if test="${!empty addressMap }">line-height:30px;</c:if>">
                    <c:set var="freight" value="0"></c:set>
                    <c:if test="${!empty priceMap}">
                        <c:if test="${priceMap*1 > 0}">
                            <c:set var="freight" value="${priceMap*1 }"></c:set>
                        </c:if>
                    </c:if>
                    <span class="pay"><c:if test="${freight>0 }">运费：￥${freight }</c:if><c:if test="${freight == 0 }">免运费</c:if></span>
                    <span>销量：<label>${mapmessage.pro_sale_total+mapmessage.sales_base}</label>件</span>
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
                <a href="/mallPage/${shopid}/79B4DE7C/shoppingall.do">全部商品</a>
                <a href="javascript:void(0)" onclick="pageclick('${pageid}')">进入店铺</a>
                <%-- <a href="mAuction/${id}/${shopid}/${auction.id}/79B4DE7C/shopdetails.do?type=1">商品详情</a> --%>
            </div>
        </div>
        <c:if test="${!empty auction }">
            <div class="auction-rule pad">
                <div class="rule-title flex">
                    <div class="flex-1">竞拍玩法</div>
                    <div class="flex-1 title-right"
                         onclick="javascript:location.href='mAuction/${id}/${shopid}/${auction.id}/79B4DE7C/shopdetails.do?type=1&uId=${userid }'">玩法详情 <img
                            src="/images/mall/grey-arrow.png"/></div>
                </div>
                <div class="flex rule-step">
                    <c:set var="index" value="1"></c:set>
                    <c:if test="${auction.isMargin==1}">
                        <div class="flex-1"><i>${index}</i>交保证金</div>
                        <c:set var="index" value="${index+1 }"></c:set>
                        <div class="flex-1"><i>${index}</i>参与竞拍</div>
                        <c:set var="index" value="${index+1 }"></c:set>
                    </c:if>
                    <c:if test="${auction.isMargin==0}">
                        <div class="flex-1"><i>${index}</i>参与竞拍</div>
                        <c:set var="index" value="${index+1 }"></c:set>
                    </c:if>
                    <div class="flex-1"><i>${index }</i>竞拍成功</div>
                    <div class="flex-1"><i>${index+1 }</i>支付货款</div>
                    <div class="flex-1"><i>${index+2 }</i>交易完成</div>
                </div>
            </div>
        </c:if>
        <jsp:include page="/jsp/mall/product/phone/product_detail_new.jsp"></jsp:include>
    </div>
    <!--遮罩层-->
    <!--遮罩层-->
    <div class="fade" id="fade" onclick="hideDiv()"></div>
    <!--弹出层-->
    <div class="attr-lay" style="display: none;">
        <div class="lay-header">
            <div class="img-box spec-img">
                <img src="${http}${mapmessage.image_url}" id="attr_image"/>
            </div>
            <div class="header-box">
                <div class="info">
                    <p class="sum">
                        ￥<span id="attr_lay_price">${mapmessage.pro_price * discount}</span>
                        <span class="before-price pad" style="display:none;float:none;">
						 			原价：￥<span>${mapmessage.pro_cost_price }</span>
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
</div>
<c:forEach items="${guigePrice }" var="guigePrice">
    <input type="hidden" class="guigePrice_value" option="${guigePrice.xsid}" optionInvId="${guigePrice.id }" optionprice="${guigePrice.inv_price * discount}"
           optionnum="${guigePrice.inv_num}" optionimage="${http}${guigePrice.specifica_img_url}" optionspecifica_img_id="${guigePrice.specifica_img_id} "
           optionyuanprice="${guigePrice.inv_price}" optioncode="${guigePrice.inv_code }">
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
<form id="queryForm" method="post" action="/phoneOrder/79B4DE7C/toOrder.do?uId=${userid }">
    <input type="hidden" id="json" name="data">
    <input type="hidden" id="type" name="type" value="0">
</form>


<div class="fade1" id="fade1" onclick="hideImgDiv()"></div>
<div class="imgDivAttr">
    <div class="titleDiv">
        <i class="delete" onclick="hideImgDiv()"></i>
    </div>
    <div class="imgDiv">
    </div>
</div>
<div class="msgDivAttr">
    <div class="msgsDiv">
        <div class="cl">
            <div class="titleDiv">
                <i class="delete" onclick="hideImgDiv()"></i>
            </div>
            <div class="msgDiv">
            </div>
        </div>
    </div>
</div>

<c:if test="${empty addressMap && empty loginCity}">
    <iframe id="geoPage" width="100%" height="30%" frameborder=0 scrolling="no" style="display:none;"
            src="https://apis.map.qq.com/tools/geolocation?key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp&effect=zoom"></iframe>

    <!-- 接收到位置信息后 通过 iframe 嵌入位置标注组件 -->
    <iframe id="markPage" width="100%" height="70%" frameborder=0 scrolling="no" src="" style="display:none;"></iframe>
</c:if>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
<input type="hidden" id="costPrice" value="${mapmessage.pro_cost_price }"/>
<input type="hidden" id="proPrice" value="${mapmessage.pro_price }"/>
<input type="hidden" id="invPrice" value="${mapmessage.inv_price }"/>
<input type="hidden" id="proid" value="${id}">
<input type="hidden" id="shopid" value="${shopid}">
<input type="hidden" id="yuan_http" name="yuan_http" value="${http}">
<input type="hidden" id="yuan_image_url" name="yuan_image_url" value="${http}${mapmessage.image_url}">
<input type="hidden" class="stockTotal" value="${mapmessage.pro_stock_total}"/>
<input type="hidden" class="memberId" id="memberIds" value="${memberId}"/>
<input type="hidden" class="userid" value="${userid }"/>
<script src="/js/plugin/jquery-1.8.3.min.js?<%=System.currentTimeMillis()%>"></script>

<script type="text/javascript" src="/js/plugin/swiper.min.js"></script>
<script src="/js/plugin/jquery.fly.js"></script>
<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script src="/js/mall/product/phoneProductDetail.js"></script>
<script type="text/javascript">
    var discount = $("#discount").val() * 1;
    var memberId = "${memberId}";
    var aucLength = $(".aucId").length;
    $(function () {
        var a = $(window).width(),
            b = $(window).height(),
            d = 870,
            meta = $("#meta");
        meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
        setTimeout(function () {
            $(".loading").hide();
        }, 1000);
    });
    var mySwiper = new Swiper('.swiper-container', {
        autoplay: 5000,//可选选项，自动滑动
        onSlideChangeStart: function (swiper) {
            $("#indexNum").text(mySwiper.activeIndex + 1);
        }
    });
    //立即购买,立即开团,立即拍卖
    function productBuy(types, status) {
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
        var invNum = $(".inv-num span#inventory").text();
        if (product_num * 1 > $.trim(invNum) * 1) {
            alert("你购买的数量大于商品现有的库存，请重新输入购买数量");
            return;
        }
        if (types == 4) {//拍卖
            price = $(".nowPrice").val();
            totalprice = price;
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
        var memberId = $(".memberId").val();
        if (memberId == null || memberId == "") {
            toLogin();
            return false;
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
            hs["pro_type_id"] = $("#pro_type_id").val();
            hs["member_type"] = $("#member_type").val();
            hs["is_integral_deduction"] = $(".is_integral_deduction").val();
            hs["is_fenbi_deduction"] = $(".is_fenbi_deduction").val();
            $("#json").val(JSON.stringify(hs));
            //console.log(hs)
            sumbit();
        }
    }


</script>
<script type="text/javascript" src="/js/mall/auction/phone/auctiondetail.js"></script>
</body>
</html>