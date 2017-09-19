<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
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
    <style type="text/css">
        strong {
            font-weight: bold !important;
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
<link rel="stylesheet" type="text/css" href="/css/common/reset.css"/>
<link rel="stylesheet" type="text/css" href="/css/mall/phoneDetail.css?<%=System.currentTimeMillis()%>"/>
<link rel="stylesheet" type="text/css" href="/css/plugin/swiper.min.css"/>
<link rel="stylesheet" type="text/css" href="/css/mall/product/detaiLayer.css"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>

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
<input type="hidden" class="myIntegral" value="${integral }"/>
<input type="hidden" class="is_member_discount" value="${mapmessage.is_member_discount }"/>
<input type="hidden" class="buyCode" value="${buyCode }">
<input type="hidden" class="is_integral_deduction" value="${mapmessage.is_integral_deduction }"/>
<input type="hidden" class="is_fenbi_deduction" value="${mapmessage.is_fenbi_deduction }"/>
<input type="hidden" class="isChangeFenbi" value="${mapmessage.is_fenbi_change_pro }"/>
<input type="hidden" class="changeFenbi" value="${mapmessage.change_fenbi }"/>
<input type="hidden" class="myFenbi" value="${fenbi }"/>


<div class="Warp">
    <div class="main">
        <section class="pic-carousel">
            <div class="swiper-container ">
                <div class="swiper-wrapper">
                    <c:forEach items="${imagelist }" var="lists">
                        <div class="swiper-slide">
                            <img data-src="${http}${lists.image_url}" class="swiper-lazy" img="${http}${lists.image_url}"/>
                            <div class="swiper-lazy-preloader"></div>
                        </div>
                    </c:forEach>
                </div>

            </div>
            <div class="page">
                <span id="indexNum">1</span>/<span>${fn:length(imagelist)}</span>
            </div>
        </section>
        <div class="price-box">
            <div class="product_nameclass"><c:if test="${!empty mapmessage.pro_label}"><i class="label_i">${mapmessage.pro_label }</i></c:if>${mapmessage.pro_name }</div>
            <div class="price_div">
                <div class="left_div">
                    <div class="span_price redColor newPriceDiv">
                        <c:if test="${!empty groupBuy  }">
                            ${groupBuy.GPeopleNum }人团:
                        </c:if>
                        <c:if test="${!empty seckill}">
                            秒杀价：
                        </c:if>
                        <c:if test="${!empty presale}">
                            预售价：
                        </c:if>
                        <span class="group_price_spans">
							￥<i class="priceclass">
							</i>
							</span>
                    </div>
                    <div class="before-price pad">
                        <em>￥<span>${mapmessage.pro_cost_price }</span></em>
                    </div>

                </div>
                <div class="middle_div">
                    <c:if test="${!empty presale}">
                        <c:set var="depositMoney" value="${presale.depositPercent }"></c:set>
                        <c:if test="${!empty deposit }">
                            <c:if test="${!empty  deposit.depositMoney}">
                                <c:set var="depositMoney" value="${deposit.depositMoney }"></c:set>
                            </c:if>
                        </c:if>
                        <c:if test="${!isBuyFlag && presale.status == 0}">
                            <div class="deposit_span navDiv">
                                <em>定金</em><span>￥<i class="depositPrice redColor">${depositMoney }</i></span>
                                <span class="c"></span>
                            </div>
                        </c:if>
                        <c:if test="${isBuyFlag && !empty deposit}">
                            <c:if test="${presale.status == 0 }">
                                <div class="deposit_span navDiv">
                                    <em style="width: 110px;">定金已付</em><span>￥<i class="depositPrice redColor">${deposit.depositMoney }</i></span>
                                    <span class="c"></span>
                                </div>
                            </c:if>
                        </c:if>
                    </c:if>
                    <c:if test="${!empty deposit && !empty presale}">
                        <input type="hidden" class="isDeposit" value="1">
                        <input type="hidden" class="orderMoney" value="${deposit.orderMoney }"/>
                        <input type="hidden" class="depositMoney" value="${deposit.depositMoney }"/>
                        <c:if test="${deposit.depositStatus == 1 }">
                            <div class="deposit_span navDiv">
                                <em style="width: 110px;">尾款</em><span>￥<i class="depositShFPrice redColor">${deposit.orderMoney-deposit.depositMoney }</i></span>
                                <span class="c"></span>
                            </div>
                        </c:if>
                    </c:if>
                    <div class="commission_span navDiv" style="<c:if test="${empty isCommission || isCommission == 0 }">display: none;</c:if>">
                        <em>佣金</em><span>￥<i class="commission redColor"></i></span>
                        <span class="c"></span>
                    </div>
                    <div class="huiyuan_span navDiv" style="display: none;">
                        <em>会员价</em><span>￥<i class="huiyuan redColor">306.00</i></span>
                        <span class="c"></span>
                    </div>
                    <c:if test="${!empty pifa }">
                        <c:if test="${!empty pifa.pfPrice && status == 1}">
                            <div class="pfj_span navDiv" style="line-height:64px !important; ">
                                <em>批发价</em><span>￥<i class="pfjyuan redColor">${pifa.pfPrice }</i></span>
                                <span class="c"></span>
                            </div>
                        </c:if>
                    </c:if>
                </div>
                <div class="right_div">
                    <c:set var="isCollectPro" value="0"></c:set>
                    <c:if test="${!empty isCollect }">
                        <c:set var="isCollectPro" value="${isCollect }"></c:set>
                    </c:if>
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
                    <c:if test="${!empty presale }">
                        <div class="viewSpan">
                            <em>订购量</em>${buyCout }
                        </div>
                    </c:if>
                    <div class="viewSpan">
                        <em>关注量</em>${viewNum }
                    </div>
                </div>
                <div class="c"></div>
            </div>
            <%-- <div class="price pad">
                <span class="span_price">
                    <c:if test="${!empty groupBuy }">
                    ${groupBuy.GPeopleNum }人团:
                    </c:if>
                    <c:if test="${!empty seckill}">
                    秒杀价：
                    </c:if>
                    ￥<i class="priceclass">
                    </i>
                </span>
                <span class="huiyuan_span" style="display: none;">
                    <em>会员价</em><i class="huiyuan"></i>
                </span>
            </div>
            <div class="viewPrices">
                <div class="before-price pad">
                     原价：<span>￥${mapmessage.pro_cost_price }</span>
                </div>
                <c:if test="${mapmessage.is_show_views == 1 }">
                <span class="viewSpan">
                    <em>关注量</em>${viewNum }
                </span>
                </c:if>
            </div> --%>
            <c:if test="${!empty groupBuy }">
                <div class="flex-1 count-down" id="time-item">
                    <label for="">活动剩余时间</label>
                    <input type="hidden" class="diffTimes" value="${groupBuy.times }"/>
                    <span>
      	   				<span><span id="day_show" class="red-txt day_show">0</span>天</span>
      	   				<span><span id="hour_show" class="red-txt hour_show">0</span>时</span>
      	   				<span><span id="minute_show" class="red-txt minute_show">0</span>分</span>
      	   				<span><span id="second_show" class="red-txt second_show">0</span>秒</span>
     	   			</span>
                </div>
            </c:if>
            <c:if test="${!empty seckill }">
                <div class="flex-1 count-down <c:if test="${seckill.status == 0 }">startTimes</c:if>" id="time-item" status="${seckill.status}">
                    <label for="">
                        <c:if test="${seckill.status == 1 }">活动剩余时间</c:if>
                        <c:if test="${seckill.status == 0 }">活动开始剩余时间</c:if>
                    </label>
                    <input type="hidden" class="diffTimes" value="${seckill.times }"/>
                    <input type="hidden" class="startTimes" value="${seckill.startTimes }"/>
                    <span>
      	   				<span><span id="day_show" class="red-txt day_show">0</span>天</span>
      	   				<span><span id="hour_show" class="red-txt hour_show">0</span>时</span>
      	   				<span><span id="minute_show" class="red-txt minute_show">0</span>分</span>
      	   				<span><span id="second_show" class="red-txt second_show">0</span>秒</span>
     	   			</span>
                </div>
            </c:if>
            <c:if test="${!empty presale }">
                <div class="flex-1 count-down <c:if test="${presale.status == 0 }">startTimes</c:if>" id="time-item" status="${presale.status}">
                    <label for="">
                        <c:if test="${presale.status == 1 }">活动剩余时间</c:if>
                        <c:if test="${presale.status == 0 }">活动开始剩余时间</c:if>
                    </label>
                    <input type="hidden" class="diffTimes" value="${presale.times }"/>
                    <input type="hidden" class="startTimes" value="${presale.startTimes }"/>
                    <span>
      	   				<span><span id="day_show" class="red-txt day_show">0</span>天</span>
      	   				<span><span id="hour_show" class="red-txt hour_show">0</span>时</span>
      	   				<span><span id="minute_show" class="red-txt minute_show">0</span>分</span>
      	   				<span><span id="second_show" class="red-txt second_show">0</span>秒</span>
     	   			</span>
                </div>
                <div class="c"></div>
            </c:if>
        </div>
        <c:if test="${!empty saleMemberId }">
            <div class="select pad ">
                <div onclick="showShares('${shareState}','${shareMsg }','${saleMemberId }','${mapmessage.id }');">
                    我要分享
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
        <c:if test="${!empty pifa }">
            <div class="select pad " onclick="showWrap(${status},'${pifa.status  }')">
                <input type="hidden" class="pfType" value="${pifa.pfType }"/>
                <c:set var="hpMoney" value="0"></c:set>
                <c:set var="hpNum" value="0"></c:set>
                <c:set var="spNum" value="1"></c:set>
                <c:if test="${!empty pfSet }">
                    <c:if test="${!empty pfSet.isHpMoney }">
                        <c:if test="${pfSet.isHpMoney == 1 }">
                            <c:set var="hpMoney" value="${pfSet.hpMoney }"></c:set>
                        </c:if>
                    </c:if>
                    <c:if test="${!empty pfSet.isHpNum }">
                        <c:if test="${pfSet.isHpNum == 1 }">
                            <c:set var="hpNum" value="${pfSet.hpNum }"></c:set>
                        </c:if>
                    </c:if>
                    <c:if test="${!empty pfSet.isSpHand }">
                        <c:if test="${pfSet.isSpHand == 1 }">
                            <c:set var="spNum" value="${pfSet.spHand }"></c:set>
                        </c:if>
                    </c:if>
                    <c:if test="${hpNum == 0 && hpMoney == 0 }">
                        <c:set var="hpNum" value="1"></c:set>
                    </c:if>
                </c:if>
                <input type="hidden" class="hpMoney" value="${hpMoney }"/>
                <input type="hidden" class="hpNum" value="${hpNum }"/>
                <input type="hidden" class="spNum" value="${spNum }"/>

                <input type="hidden" class="pifaType" value="${pifa.pfType }"/>
                <input type="hidden" class="pfPrice" value="${pifa.pfPrice }"/>
                <input type="hidden" class="pifaId" value="${pifa.id }"/>
                我要批发
                ，<span class="least-num pfNumSpan">0</span>
                <span class="pfUnitSpan">手</span>
                <span class="hp_spans" style="display: none;">
				<c:if test="${hpNum > 0 }">或</c:if><span class="hpMoneySpan ">2</span>元
			</span>
                <span class="tip2_span">起批</span>
                <c:if test="${!empty pifa.priceList }">
                    <c:forEach var="price" items="${pifa.priceList }">
                        <input type="hidden" class="pifaSpecInp" specIds="${price.specificaIds }"
                               invenId="${price.invenId }"
                               value="${price.seckillPrice }"/>
                    </c:forEach>
                </c:if>
            </div>
        </c:if>
        <c:if test="${!empty flow_desc }">
            <div class="select pad" style="background-image: none;">
                    ${flow_desc }
            </div>
        </c:if>
        <c:if test="${!empty cardList && !empty recevieMap && mapmessage.pro_type_id == 3 && mapmessage.card_type > 0 }">
            <div class="select pad" onclick="showCardWrap();">
                查看所含优惠券
            </div>
        </c:if>
        <div class="select pad ">
            <!--商品数量  -->
            <input type="hidden" id="product_num" name="product_num" value=1>
            <input type="hidden" id="xids" name="xids" value="${guige.xids}">
            <input type="hidden" id="specifica_name" name="specifica_name" value="${guige.specifica_name}">
            <input type="hidden" id="maxBuy" value="${mapmessage.pro_restriction_num }"/>
            <input type="hidden" id="groupBuyCount" value="<c:if test='${!empty groupBuyCount }'>${groupBuyCount }</c:if>"/>
            <input type="hidden" id="seckillCount" value="<c:if test='${!empty seckillCount }'>${seckillCount }</c:if>"/>
            <input type="hidden" id="presaleCount" value="<c:if test='${!empty presaleCount }'>${presaleCount }</c:if>"/>
            <input type="hidden" id="isCoupons" value="${mapmessage.is_coupons }"/>
            <input type="hidden" id="pro_type_id" value="${mapmessage.pro_type_id }"/>
            <input type="hidden" id="member_type" value="${mapmessage.member_type }"/>
            <input type="hidden" id="card_type" value="${mapmessage.card_type }"/>
            <div onclick="showDiv()">
                <i class="specidsname"> 选择：规格</i>
                <!-- 规格与数量:1件 -->
            </div>

        </div>
        <div id="" class="address pad">
            <div style="width:100%;float:left;">
                <c:if test="${!empty addressMap }">
                    <div class="addr-box">
                        <label>送至：<em style="vertical-align: middle;"></em></label> <span
                            class="addr">${addressMap.provincename }${addressMap.cityname }${addressMap.areaname }${addressMap.memAddress }</span>
                    </div>
                </c:if>
                <div style="<c:if test="${!empty addressMap }">line-height:30px;</c:if>">
                    <c:set var="freight" value="0"></c:set>
                    <c:if test="${!empty priceMap}">
                        <c:if test="${priceMap*1 > 0}">
                            <c:set var="freight" value="${priceMap*1 }"></c:set>
                        </c:if>
                    </c:if>
                    <span class="pay"><c:if test="${freight > 0 }">运费：￥${priceMap }</c:if><c:if test="${freight == 0 }">免运费</c:if></span>
                    <span>销量：<label>${mapmessage.pro_sale_total+mapmessage.sales_base}</label>件</span>
                    <span style="float: right;">${storeAddress}</span>
                </div>
            </div>
            <input type="hidden" class="fMoney" value="${priceMap }"/>
        </div>
        <!-- 参团人 -->
        <c:if test="${!empty joinList }">
            <div class="group-title pad">
                以下小伙伴正在发起团购，您可以直接参与
            </div>
            <div class="group pad">
                <!--<div class="">-->
                <c:forEach var="join" items="${joinList }">
                    <div class="group-item">
                        <div class="flex group-list">
                            <div class="flex-one">

                            </div>
                            <div class="flex-two">
                                <div class="two-info flex">
                                    <div class="flex-1 font-bold">
                                            ${join.nickname }
                                    </div>
                                    <div class="red-txt flex-1 rig-txt">
                                        还差${join.joinNum }人成团
                                    </div>
                                </div>
                                <div class="two-info flex">
                                    <!-- <div class="flex-1">汕头
                                    </div> -->
                                    <div class="flex-1 rig-txt">
                                        剩余
                                        <span>
		           	   				<span><span id="day_show" class="red-txt day_show">0</span>天</span>
		           	   				<span><span id="hour_show" class="red-txt hour_show">0</span>时</span>
		           	   				<span><span id="minute_show" class="red-txt minute_show">0</span>分</span>
		           	   				<span><span id="second_show" class="red-txt second_show">0</span>秒</span>
	           	   				</span>结束
                                    </div>
                                </div>
                            </div>
                            <div class="flex-thr">
                                <a href="javascript:void(0);" onclick="goGroup(${join.groupBuyId},${join.id },${join.joinUserId });">去参团
                                    <div class="arrow"></div>
                                </a>
                            </div>
                        </div>
                        <img src="${join.headimgurl}" class="head-img"/>
                    </div>
                </c:forEach>
                <!--</div>-->
            </div>
        </c:if>
        <!-- 参团人 -->

        <div class="go-other pad">
            <div class="mall-name">
                <img src="${http}<c:if test='${shopmessage.stoPicture == \'\' || shopmessage.stoPicture == null}'>${shopmessage.sto_picture}</c:if><c:if test='${shopmessage.stoPicture != \'\' && shopmessage.stoPicture != null }'>${shopmessage.stoPicture}</c:if>"/>
                <p class="shop_namemessg"><c:if test='${shopmessage.business_name == "" }'>${shopmessage.sto_name}</c:if><c:if
                        test='${shopmessage.business_name != "" }'>${shopmessage.business_name}</c:if></p>
            </div>
            <div class="other-mall">
                <a href="javascript:void(0)" onclick="shoppingalls();">全部商品</a>
                <a href="javascript:void(0)" onclick="location.href='/mallPage/${mapmessage.shop_id}/79B4DE7C/toMallIndex'">进入店铺</a>
                <%-- <a href="mallPage/${id}/79B4DE7C/shopdetails.do">商品详情</a> --%>
            </div>

        </div>
        <c:if test="${!empty groupBuy }">
            <div class="group-rule pad">
                <div class="rule-title flex">
                    <div class="flex-1">
                        拼团玩法
                    </div>
                    <div class="flex-1">
                        <a href="/mGroupBuy/${shopid}/79B4DE7C/playDetail.do?uId=${userid}">玩法详情</a>
                    </div>
                </div>
                <div class="flex">
                    <div class="flex-1 step">
                        <span class="step-num"> <label for="">1</label> </span>
                        <span class="step-txt">选择商品<br/>付款开团/参团</span>
                    </div>
                    <div class="flex-1 step">
                        <span class="step-num"> <label for="">2</label> </span>
                        <span class="step-txt">邀请并等待好友<br/>支付参团</span>
                    </div>
                    <div class="flex-1 step">
                        <span class="step-num"> <label for="">3</label> </span>
                        <span class="step-txt">到达人数<br/>拼团成功</span>
                    </div>
                </div>
            </div>

        </c:if>

        <jsp:include page="/jsp/mall/product/phone/product_detail_new.jsp"></jsp:include>
    </div>
    <c:set var="cla" value="uls"></c:set>
    <c:set var="fCla" value=""></c:set>
    <c:if test="${!empty groupBuy  && empty seckill}">
        <c:set var="cla" value="group_url"></c:set>
        <c:set var="fCla" value="group_footer"></c:set>
    </c:if>
    <footer class="footer ${fCla }">
        <ul class="${cla }">
            <li class="foot-item" onclick="myCenter()"><img src="images/mall/img/mall-person.png"/></li>
            <li class="foot-item" onclick="shoppingcart()"><img src="images/mall/img/mall-shop.png"/>
                <c:if test="${empty shopCartNum || shopCartNum == 0 }">
                    <i class="shopping-icon" style="display:none">0</i>
                </c:if>
                <c:if test="${!empty shopCartNum && shopCartNum > 0 }">
                    <i class="shopping-icon" style="display:block">${shopCartNum }</i>
                </c:if>
            </li>
            <c:set var="isActivity" value="false"></c:set>
            <c:if test="${rType == 0 }">
                <c:if test="${!empty groupBuy  && empty seckill}">
                    <c:set var="isActivity" value="true"></c:set>
                    <li class="foot-item2 group_li">
                        <a href="javascript:void(0)" class="add-shop shop-btn" onclick="productBuy(0)">
                            <p class="groupDanPrice">￥<em>123.00</em></p>
                            <p>单独购买</p>
                        </a>
                    </li>
                    <li class="foot-item2 group_li">
                        <a href="javascript:void(0)" class="now-shop shop-btn" onclick="productBuy(1)">
                            <p class="groupLiPrice">￥<em>123.00</em></p>
                            <p>${groupBuy.GPeopleNum } 人拼团价</p>
                        </a>
                    </li>
                </c:if>
                <c:if test="${empty groupBuy && !empty seckill }">
                    <c:set var="isActivity" value="true"></c:set>
                    <li class="foot-item2" style="float:right;"><a href="javascript:void(0)" class="now-shop shop-btn <c:if test="${seckill.status == 0}">noStart</c:if>"
                                                                   onclick="productBuy(3)">
                        <c:if test="${seckill.status == 0}">即将开始秒杀</c:if>
                        <c:if test="${seckill.status == 1}">立即秒杀</c:if>
                    </a></li>
                </c:if>
                <c:if test="${!empty presale }">
                    <c:if test="${presale.status == 0 }">
                        <c:set var="isActivity" value="true"></c:set>

                        <c:if test="${!isBuyFlag }">
                            <li class="foot-item2" style="float:right;">
                                <a href="javascript:void(0)" class="now-shop shop-btn" onclick="presale(1);">立即预定</a>
                            </li>
                        </c:if>

                    </c:if>

                    <c:if test="${isBuyFlag}">
                        <c:set var="isActivity" value="true"></c:set>
                        <li class="foot-item2" style="float:right;">
                            <a href="javascript:void(0)" class="now-shop shop-btn  <c:if test="${presale.status == 0}">noStart</c:if>" onclick="productBuy(6)">
                                <c:if test="${presale.status == 0}">即将开售</c:if>
                                <c:if test="${presale.status == 1}">支付尾款</c:if>
                            </a></li>
                    </c:if>
                    <c:if test="${presale.status == 1 && !isBuyFlag}">
                        <c:set var="isActivity" value="true"></c:set>
                        <li class="foot-item2" style="float:right;>"><a href="javascript:void(0)" class="now-shop shop-btn" onclick="productBuy(6)">立即购买</a></li>
                    </c:if>
                </c:if>
                <c:if test="${!isActivity}">
                    <c:if test="${mapmessage.pro_type_id == 0  && toshop != 1}">
                        <li class="foot-item2"><a href="javascript:void(0)" class="add-shop shop-btn" onclick="addshopping()">加入购物车</a></li>
                    </c:if>
                    <li class="foot-item2" style="<c:if test="${mapmessage.pro_type_id != 0 || toshop == 1}">float:right;</c:if>"><a href="javascript:void(0)"
                                                                                                                                     class="now-shop shop-btn"
                                                                                                                                     onclick="productBuy(0)">立即购买</a></li>
                </c:if>
            </c:if>
            <c:if test="${rType == 1}">
                <a href="javascript:void(0)" class="now-shop shop-btn" onclick="productBuy(2)" style="float:right;">立即兑换</a>
            </c:if>
            <c:if test="${rType == 2}">
                <a href="javascript:void(0)" class="now-shop shop-btn" onclick="productBuy(5)" style="float:right;">立即兑换</a>
            </c:if>
        </ul>
    </footer>
    <jsp:include page="dialog/pf_dialog.jsp"></jsp:include>
    <!--遮罩层-->
    <div class="fade" id="fade" onclick="hideDiv()"></div>
    <!--弹出层-->
    <div class="attr-lay" style="display: none;">
        <div class="lay-header">
            <div class="img-box spec-img imageDiv">
                <img class="img-container" src="${http}${mapmessage.image_url}" img="${http}${mapmessage.image_url}" id="attr_image"/>
            </div>
            <div class="header-box">
                <div class="info">
                    <%-- <p class="sum">
                        ￥<span id="attr_lay_price">${mapmessage.pro_price * discount}</span>
                        <span class="before-price pad" style="display:none;float:none;">
                             原价：￥<span>${mapmessage.pro_cost_price }</span>
                        </span>
                    </p>
                    <p class="inv-num">库存<span id="inventory">${mapmessage.pro_stock_total}</span>件</p> --%>
                    <div class="price_div c">
                        <div class="left_div">
                            <div class="sum">
                                <div>
                                    <c:if test="${!empty presale}">预售价：</c:if>
                                    ￥<span id="attr_lay_price">${mapmessage.pro_price * discount}</span>
                                </div>
                                <div>
										<span class="before-price pad" style="display:none;float:none;padding:0px;">
								 			<em>￥<span>${mapmessage.pro_cost_price }</span></em>
										</span>
                                </div>
                            </div>
                            <div class="inv-num">库存<span id="inventory">${mapmessage.pro_stock_total}</span>件</div>
                        </div>
                        <div class="middle_div">
                            <div class="huiyuan_span em_span c" style="display: none;">
                                <em>会员价</em><i class="huiyuan redColor"></i>
                                <span class="c"></span>
                            </div>
                            <div class="commission_div em_span c" style="<c:if test="${empty isCommission || isCommission == 0 }">display: none;</c:if>">
                                <em>佣金</em><i class="commission_lay redColor"></i>
                                <span class="c"></span>
                            </div>
                        </div>
                        <div class="c"></div>
                    </div>

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
            <div class="box-num clearfix">
                <h2 class="title">数量</h2>
                <c:if test="${!empty mapmessage.flow_id }">
                    <c:if test="${mapmessage.flow_id > 0 }">
                        <c:set var="isFlow" value="1"></c:set>
                    </c:if>
                </c:if>
                <c:if test="${empty isFlow }">
                    <div class="gw_num" id="gw_num">
                        <em class="jian">-</em>
                        <input type="tel" value="1" class="num" id="attr_lay_num" maxlength="6"/>
                        <em class="add">+</em>
                    </div>
                </c:if>
                <c:if test="${!empty isFlow }">
                    <div class="gw_num" id="gw_num" style="border:0px;width: 75px;">1
                        <input type="hidden" value="1" class="num" id="attr_lay_num" maxlength="6" readonly="readonly"/>
                    </div>
                </c:if>
            </div>

        </div>
        <c:set var="cla" value="lay-bottom"></c:set>
        <c:if test="${!empty groupBuy && empty seckill }">
            <c:set var="cla" value="group_lay-bottom"></c:set>
        </c:if>
        <div class="lay-bottom ${cla }">
            <c:if test="${rType == 0 }">
                <c:if test="${!empty groupBuy && empty seckill }">
                    <ul>
                        <li>
                            <a href="javascript:void(0)" onclick="product_Buy(0)" class="add-shop shop-btn">
                                <p class="groupDanPrice">￥<em>123.00</em></p>
                                <p>单独购买</p>
                            </a>
                        </li>
                        <li>
                            <a href="javascript:void(0)" class="now-shop shop-btn" onclick="product_Buy(1)">
                                <p class="groupLiPrice">￥<em>123.00</em></p>
                                <p>${groupBuy.GPeopleNum } 人拼团价</p>
                            </a>
                        </li>
                    </ul>
                </c:if>

                <c:if test="${empty groupBuy && !empty seckill }">
                    <a href="javascript:void(0)" class="seckill-btn now-shop shop-btn <c:if test="${seckill.status == 0}">noStart</c:if>" onclick="product_Buy(3)">
                        <c:if test="${seckill.status == 0}">即将开始秒杀</c:if>
                        <c:if test="${seckill.status == 1}">立即秒杀</c:if>
                    </a>
                </c:if>
                <c:if test="${!empty presale}">
                    <%-- <c:if test="${isBuyFlag }">
                <a href="javascript:void(0)" class="seckill-btn now-shop shop-btn <c:if test="${presale.status == 0}">noStart</c:if>" onclick="product_Buy(6)">
                    <c:if test="${presale.status == 0}">即将开售</c:if>
                    <c:if test="${presale.status == 1}">立即预定</c:if>
                </a>
                    </c:if> --%>
                    <c:if test="${presale.status == 0 }">
                        <c:if test="${!isBuyFlag }">
                            <a href="javascript:void(0)" class="now-shop shop-btn" onclick="presale(2);">立即预定</a>
                        </c:if>
                    </c:if>
                    <%-- <c:if test="${isBuyFlag}">
                        <a href="javascript:void(0)" class="now-shop shop-btn  <c:if test="${presale.status == 0}">noStart</c:if>" onclick="product_Buy(6)">
                            <c:if test="${presale.status == 0}">即将开始预售</c:if>
                            <c:if test="${presale.status == 1}">支付尾款</c:if>
                        </a>
                    </c:if> --%>
                    <c:if test="${presale.status == 1  && !isBuyFlag}">
                        <a href="javascript:void(0)" class="now-shop shop-btn" onclick="product_Buy(6)">立即购买</a>
                    </c:if>
                </c:if>

                <c:if test="${!isActivity}">
                    <c:if test="${mapmessage.pro_type_id == 0 && toshop != 1}">
                        <a href="javascript:void(0)" onclick="addshop_ping()" class="add-shop shop-btn">加入购物车</a>
                    </c:if>
                    <a href="javascript:void(0)" class="now-shop shop-btn" onclick="product_Buy(0)">立即购买</a>
                </c:if>

            </c:if>
            <c:if test="${rType == 1}">
                <a href="javascript:void(0)" class="now-shop shop-btn" onclick="product_Buy(2)">立即兑换</a>
            </c:if>
            <c:if test="${rType == 2}">
                <a href="javascript:void(0)" class="now-shop shop-btn" onclick="product_Buy(5)">立即兑换</a>
            </c:if>
        </div>
    </div>

    <c:forEach items="${guigePrice }" var="guigePrice">
        <input type="hidden" class="guigePrice_value" option="${guigePrice.xsid}" optionInvId="${guigePrice.id }" optionprice="${guigePrice.inv_price * discount}"
               optionnum="${guigePrice.inv_num}"
               optionimage="<c:if test='${!empty guigePrice.specifica_img_url && guigePrice.specifica_img_url !="" }'>${http}${guigePrice.specifica_img_url}</c:if>"
               optionspecifica_img_id="${guigePrice.specifica_img_id} " optionyuanprice="${guigePrice.inv_price}" optioncode="${guigePrice.inv_code }">
    </c:forEach>
    <c:if test="${!empty groupBuy }">
        <c:if test="${!empty groupBuy.priceList }">
            <c:forEach var="buy" items="${groupBuy.priceList }">
                <input type="hidden" class="groupPrice_arr" spec="${buy.specificaIds }" specIds=",${buy.specificaIds }," inven="${buy.invenId }" isjoin="${buy.isJoinGroup }"
                       value="${buy.groupPrice }"/>
            </c:forEach>
        </c:if>
        <input type="hidden" class="groupPrice" value="${groupBuy.GPrice }"/>
        <input type="hidden" class="groupBuyId" value="${groupBuy.id }"/>
        <input type="hidden" id="groupMaxBuy" value="${groupBuy.GMaxBuyNum }"/>
    </c:if>
    <c:if test="${!empty seckill }">
        <input type="hidden" class="seckillPrice" value="${seckill.SPrice }"/>
        <input type="hidden" class="seckillId" value="${seckill.id }"/>
        <input type="hidden" id="seckillMaxBuy" value="${seckill.SMaxBuyNum }"/>
        <input type="hidden" id="sNum" value="${seckill.SNum }"/>
        <c:if test="${!empty seckill.priceList }">
            <c:forEach var="buy" items="${seckill.priceList }">
                <input type="hidden" class="seckillPrice_arr" spec="${buy.specificaIds }" specIds=",${buy.specificaIds }," inven="${buy.invenId }" isjoin="${buy.isJoinGroup }"
                       seckillNum="${buy.seckillNum }" value="${buy.seckillPrice }"/>
            </c:forEach>
        </c:if>
    </c:if>
    <!-- 预售 -->
    <c:if test="${!empty presale }">
        <input type="hidden" class="presaleDiscount" value="${presaleDiscount }"/>
        <input type="hidden" class="presaleId" value="${presale.id }"/>
        <c:if test="${!empty presaleTime }">
            <input type="hidden" class="presaleSaleType" value="${presaleTime.saleType }"/>
            <input type="hidden" class="presalePriceType" value="${presaleTime.priceType }"/>
        </c:if>
        <input type="hidden" class="presaleOrderPrice" value=""/>
        <c:if test="${!empty deposit }">
            <input type="hidden" class="depositId" value="${deposit.id }"/>
        </c:if>

        <input type="hidden" class="preInvNum" value="${presale.invNum }"/>
        <c:if test="${!empty presale.list }">
            <c:forEach var="spec" items="${presale.list }">
                <input type="hidden" class="prePrice_arr" spec="${spec.specId }" specIds=",${spec.specId }," inven="${spec.invId }" invNum="${spec.invNum }"/>
            </c:forEach>
        </c:if>
        <c:if test="${isBuyFlag }">
            <input type="hidden" class="isBuyFlag" value="1"/>
        </c:if>
    </c:if>
    <c:if test="${!empty recevieMap }">
        <c:set var="money" value="${recevieMap.buymoney }"></c:set>
        <c:if test="${!empty cardList && recevieMap.cardtype == 1}">
            <c:forEach var="card" items="${cardList }">
                <c:if test="${card.cardType == 4 }">
                    <c:set var="money" value="${card.money }"></c:set>
                </c:if>
            </c:forEach>
        </c:if>
        <input type="hidden" class="recevieGuoqi" value="${recevieMap.guoqi }"/>
        <input type="hidden" class="recevieId" value="${recevieMap.id }"/>
        <c:if test="${money > 0 }">
            <input type="hidden" class="recevieMoney" value="${money }"/>
        </c:if>
    </c:if>
    <div class="cardDivAttr attr">
        <div class="msgsDiv">
            <div class="cl">
                <div class="titleDiv">
                    <i class="delete" onclick="hideImgDiv()"></i>
                </div>
                <div class="msgDiv">
                    <c:if test="${!empty cardmessage }">
                        <c:forEach var="card" items="${cardmessage }">
                            <c:set var="guoqi" value="0"></c:set>
                            <c:if test="${!empty cardList }">
                                <c:forEach var="cardObj" items="${cardList }">
                                    <c:if test="${card.cardId ==  cardObj.id}">
                                        <c:set var="guoqi" value="${cardObj.guoqi }"></c:set>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            <div class="cardDiv">
                                <span class="left_span">${card.cardName }<c:if test="${guoqi == 1 }">（已过期）</c:if></span><span class="rigth_span">${card.num }</span>
                                <c:if test="${guoqi == 1 }">
                                    <input type="hidden" class="cardGuoqi" value="${guoqi }"/>
                                </c:if>
                            </div>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>
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
<div class="addShopCartDiv">
    <div class="msgsDiv">
        <div class="cl"><i></i></div>
    </div>
</div>
<c:if test="${!empty productCommission }">
    <input type="hidden" class="commissionType" value="${productCommission.commissionType }"/>
    <input type="hidden" class="commissionRate" value="${productCommission.commissionRate }"/>
</c:if>
<input type="hidden" class="view" value="${view }"/>
<c:if test="${!empty saleMemberId }">
    <input type="hidden" class="saleMemberId" value="${saleMemberId }"/>
</c:if>
<form id="queryForm" method="post" action="/phoneOrder/79B4DE7C/toOrder.do?uId=${userid }">
    <input type="hidden" id="json" name="data">
    <input type="hidden" id="type" name="type" value="0">
    <c:if test="${!empty toshop }">
        <input type="hidden" name="toshop" class="toshop" value="${toshop }"/>
    </c:if>
    <input type="hidden" id="proPrice" value="${mapmessage.pro_price }"></form>
<c:if test="${!empty buyNums && !empty maxNum }">
    <input type="hidden" class="buyNums" value="${buyNums }"/>
    <input type="hidden" class="maxNum" value="${maxNum }"/>
</c:if>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>

<c:if test="${empty addressMap && empty loginCity}">
    <iframe id="geoPage" width="100%" height="30%" frameborder=0 scrolling="no" style="display:none;"
            src="https://apis.map.qq.com/tools/geolocation?key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77&referer=myapp&effect=zoom"></iframe>

    <!-- 接收到位置信息后 通过 iframe 嵌入位置标注组件 -->
    <iframe id="markPage" width="100%" height="70%" frameborder=0 scrolling="no" src="" style="display:none;"></iframe>
</c:if>

<input type="hidden" id="costPrice" value="${mapmessage.pro_cost_price }"/>

<input type="hidden" id="invPrice" value="${mapmessage.inv_price }"/>
<input type="hidden" id="proid" value="${id}">
<input type="hidden" id="shopid" value="${shopid}">
<input type="hidden" id="yuan_http" name="yuan_http" value="${http}">
<input type="hidden" id="yuan_image_url" name="yuan_image_url" value="${http}${mapmessage.image_url}">
<input type="hidden" class="stockTotal" value="${mapmessage.pro_stock_total}"/>
<input type="hidden" id="isSpec" value="${mapmessage.is_specifica}"/>
<input type="hidden" class="memberId" id="memberIds" value="${memberId}"/>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="inpPrice"
       value="<c:if test="${mapmessage.is_specifica eq 1 }">${((mapmessage.inv_price*100) * (discount*100))/10000}</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${((mapmessage.pro_price*100) * (discount*100))/10000}</c:if>"/>
<input type="hidden" class="pricePro"
       value="<c:if test="${mapmessage.is_specifica eq 1 }">${mapmessage.inv_price}</c:if><c:if test="${mapmessage.is_specifica ne 1 }">${mapmessage.pro_price}</c:if>"/>

<script type="text/javascript" src="/js/plugin/swiper.min.js"></script>
<script type='text/javascript' src="/js/plugin/lazyload/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/plugin/jquery.fly.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script type="text/javascript" src="/js/mall/product/phoneProductDetail.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="/js/mall/product/productDetail.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="/js/mall/product/piFaProductDetail.js?<%=System.currentTimeMillis()%>"></script>
<c:if test="${!empty pifa }">
    <c:if test="${pifa.pfType == 1 }">
        <script type="text/javascript" src="/js/mall/product/phone/hand_detail_new_inven.js"></script>
    </c:if>
    <c:if test="${pifa.pfType == 2 }">
        <script type="text/javascript" src="/js/mall/product/phone/mixed_detail_new_inven.js"></script>
    </c:if>
</c:if>
<input type="hidden" class="saleMemberId" value="${saleMemberId}"/>
<script type="text/javascript">
    var proSpecArr = '${invenList}';
    if (proSpecArr != null && proSpecArr != "" && typeof(proSpecArr) != "undefined") {
        proSpecArr = JSON.parse(proSpecArr);
    }
    var isAddShop = "${isAddShop}";
    if (isAddShop != null && isAddShop != "" && typeof(isAddShop) != "undefined" && isAddShop == 1) {
        alert("商品已加入购物车");
    }

    jQuery(document).ready(function ($) {
        if ($(".img-container").length > 0) {
            if ($(".img-container").attr("data-original") != null) {
                $(".img-container").show().lazyload({
                    effect: "fadeIn",
                    container: $(".imageDiv"),
                    threshold: 200
                });
            }
        }
        var a = $(window).width(),
            b = $(window).height(),
            d = 870,
            meta = $("#meta");
        meta.attr("content", "width=870,initial-scale=" + a / d + ", minimum-scale=" + a / d + ", maximum-scale=" + a / d + ", user-scalable=no");
        //$(".loading").hide();

    });
    var saleMemberId = $("input.saleMemberId").val();
    var shopid = $("input#shopid").val();
    var groupLen = $(".groupPrice_arr").length;
    var discount = $("#discount").val();//会员折扣
    var rType = $(".rType").val();
    var isChange = $(".isChangeIntegral").val();//是否允许积分兑换
    var integral = $(".integral").val();//兑换积分值
    var seckillLen = $(".seckillPrice_arr").length;
    var isChangeFenbi = $(".isChangeFenbi").val();//是否允许粉币兑换
    var changeFenbi = $(".changeFenbi").val();//兑换粉币值
    var presaleLen = $(".presaleId").length;
    var proTypeId = $("#pro_type_id").val();

    var presaleSaleType = 0;//预售调价类型  0不调价  1上调金额   2下调金额
    var presaleDiscount = 1;//调价折扣
    var presalePriceType = 1;//调价类型 1 按百分比调价   2按价格调价
    var isCardBuy = false;
    var saleLen = $(".commissionRate").length;
    var memberId = $("input.memberId").val();
    $("#prodect_price").val($(".pricePro").val());
    var speSelect = "";//保存选择的规格id
    $(function () {
        var yjMoney = 0;//佣金价


        $("article img").each(function () {
            var width = $(this).width();
            if (width > a) {
                $(this).css("width", a);
            } else {
                $(this).css({"width": "auto", "max-width": "100%"});
            }
        });
        /* new NoClickDelay($(".add")[0]);
         new NoClickDelay($(".jian")[0]); */

        var u = navigator.userAgent;
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
        if (isiOS) {
            if ($(".add").length > 0) {
                new NoClickDelay($(".add")[0]);
            }
            if ($(".jian").length > 0) {
                new NoClickDelay($(".jian")[0]);
            }
        }
        var attrPrice = $("#attr_lay_price").text();
        if (attrPrice != null && attrPrice != "") {
            $("#attr_lay_price").text((attrPrice * 1).toFixed(2));
        }

        var is_specifica = $("input#isSpec").val();
        var proPrice = 0;
        var costPrice = 0;
        var yuanjiaPrice = $("input#costPrice").val() * 1;//商品原价
        var discount = $("#discount").val() * 1;//会员折扣

        if (is_specifica == '1') {//该商品存在规格
            costPrice = $("input#invPrice").val();
            proPrice = costPrice * 1 * discount;
            //proPrice = '${mapmessage.inv_price * discount}';
            //costPrice = "${mapmessage.inv_price}";
        } else {
            costPrice = $("input#proPrice").val();
            proPrice = costPrice * 1 * discount;
            //proPrice = '${mapmessage.pro_price * discount}';
            //costPrice = "${mapmessage.pro_price}";
        }

        //销售佣金
        if (saleLen > 0) {
            var pPrice = costPrice * 1;
            yjMoney = getSalePrice(pPrice);//计算商品佣金
            if (yjMoney != null && yjMoney != "") {
                yjMoney = yjMoney * 1;
            }
            $(".commission").text(yjMoney);
        }

        $(".priceclass").text((costPrice * 1).toFixed(2));
        if (discount != 1 && discount > 0) {
            //var p = ((proPrice+yjMoney)*1).toFixed(2);
            var p = ((proPrice) * 1).toFixed(2);
            if (p * 1 <= 0) {
                p = 0.01;
            }
            $(".huiyuan").html(p);
            $(".huiyuan_span").show();
        }
        var oldPrice = $("input#costPrice").val() * 1;
        if (oldPrice <= 0 || oldPrice < proPrice * 1) {
            $(".before-price").hide();
        } else {
            $(".before-price").show();
        }
        /* if(discount > 0 && discount < 1){
         $(".price-box .before-price").show();
         costPrice = (costPrice*1).toFixed(2);
         $(".price-box .before-price span").html(yuanjiaPrice);
         } */
        //$(".shopping-icon").hide();
        var fMoney = $(".fMoney").val();
        if (fMoney != null && fMoney != "" && fMoney != undefined && fMoney * 1 > 0) {
            fMoney = (fMoney * 1).toFixed(2);
            $("span.pay").html("运费：￥" + fMoney);
        } else {
            $("span.pay").html("免运费");
        }
        var xids = $("#xids").val();
        var price = "";
        if (seckillLen > 0 || $("input.seckillPrice").length > 0) {//此商品是秒杀商品
            price = $(".seckillPrice_arr[spec='" + xids + "']").val();
            if (price == null || price == "") {
                price = $(".seckillPrice").val();
            }
        }
        if (groupLen > 0 || $("input.groupPrice").length > 0) {//此商品是团购商品
            price = $(".groupPrice_arr[spec='" + xids + "']").val();
            if (price == null || price == "") {
                price = $(".groupPrice").val();
            }
        }
        if (presaleLen > 0) {
            //判断预售是否有价格调整
            if ($("input.presalePriceType").length > 0) {
                presalePriceType = $("input.presalePriceType").val();
            }
            if ($("input.presaleDiscount").length > 0 && $("input.presaleSaleType").length > 0) {
                var proPrice = $(".priceclass").text() * 1;
                proPrice = getPresalePrice(proPrice);

                $(".priceclass").text((proPrice).toFixed(2));

                $("#prodect_price").val((proPrice).toFixed(2));

                $(".before-price span").html((costPrice * 1).toFixed(2));
                if (costPrice > proPrice) {
                    $(".before-price").show();
                } else {
                    $(".before-price").hide();
                }
            }
            if ($("input.isDeposit").length > 0) {
                var orderMoney = $("input.orderMoney").val() * 1;
                var depositMoney = $("input.depositMoney").val() * 1;
                var pPrice = orderMoney - depositMoney;
                pPrice = Math.round(pPrice * 100) / 100;
                $(".depositShFPrice").text(pPrice);
                $("input.presaleOrderPrice").val(pPrice);
            }
        }
        //卡券包
        if ($("input.recevieId").length > 0 && proTypeId == 3) {
            isCardBuy = true;
            var recevieGuoqi = $("input.recevieGuoqi").val();
            if (recevieGuoqi == 1) {
                $(".shop-btn.now-shop").css("background-color", "#666");
                $(".shop-btn.add-shop").css("background-color", "#999");
            }
            if ($(".recevieMoney").length > 0) {
                var recevieMoney = $(".recevieMoney").val();
                if (recevieMoney != null && recevieMoney != "") {
                    costPrice = $(".priceclass").text() * 1;
                    price = recevieMoney * 1;
                }
            }
        }
        if (price != "") {
            //var priceDiscount = ((price*discount)+yjMoney).toFixed(2);
            //var oldPriceDiscount = ((proPrice*discount)+yjMoney).toFixed(2);
            var priceDiscount = ((price * discount)).toFixed(2);
            var oldPriceDiscount = ((proPrice * discount)).toFixed(2);
            if (proPrice * 1 <= 0) {
                priceDiscount = price;
            }
            /* $(".priceclass").text(priceDiscount); */
            $(".huiyuan").html(priceDiscount);
            $(".huiyuan_span").hide();

            $(".priceclass").text(price);
            /*  $("#group_price").val(priceDiscount); */
            $("#group_price").val(price);
            $(".before-price span").html(costPrice);
            /* $(".before-price span").html(oldPriceDiscount); */
            if (costPrice <= price) {
                $(".before-price").hide();
            } else {
                $(".before-price").show();
            }
            var diffTimes = $(".diffTimes").val();
            timer(diffTimes);//倒计时

            if (groupLen > 0 || $("input.groupPrice").length > 0) {//此商品是团购商品
                $(".groupLiPrice em").html(price);
                var groupPrices = $("#prodect_price").val();
                $(".groupDanPrice em").html(groupPrices);
            }
        }
        if (yjMoney > 0) {
            var price = $(".priceclass").text() * 1;
            var price2 = $("#prodect_price").val() * 1;
            /* var yjPrice = (price+yjMoney).toFixed(2);
             var yjPrice2 = (price2+yjMoney).toFixed(2); */
            var yjPrice = (price).toFixed(2);
            var yjPrice2 = (price2).toFixed(2);
            $(".priceclass").text(yjPrice);
            $("#prodect_price").val(yjPrice2);
        }
        var view = $(".view").val();
        if (view == "show") {
            $(".shop-btn.now-shop").css("background-color", "#666");
            $(".shop-btn.add-shop").css("background-color", "#999");
        }
        if (rType * 1 > 0) {
            var bol = false;
            if (rType == 1 && isChange == 1 && integral != "") {//积分商品（从积分商城进来的）
                $(".price-box div.span_price").html("<i class='priceclass'>" + integral + "</i>积分");
                bol = true;
            }
            if (rType == 2 && isChangeFenbi == 1 && changeFenbi != "") {//积分商品（从积分商城进来的）
                $(".price-box div.span_price").html("<i class='priceclass'>" + changeFenbi + "</i>粉币");
                bol = true;
            }
            if (bol) {
                $("span.pay").html("免运费");
                var oldPriceDiscount = (proPrice * discount).toFixed(2);
                $(".before-price span").html(oldPriceDiscount);
                $(".before-price").show();
                $(".huiyuan_span").hide();
            }
        }

        var p = $("input#prodect_price").val();
        if (p != null && p != "") {
            p = (p * 1).toFixed(2);
            if (p * 1 <= 0) {
                $("input#prodect_price").val(0.01);
            } else {
                $("input#prodect_price").val(p);
            }
        }
        //判断库存
        var stockTotal = $(".stockTotal").val();
        if (stockTotal == "0") {
            $(".shop-btn.now-shop").css("background-color", "#666");
            $(".shop-btn.add-shop").css("background-color", "#999");

            $(".item-btn.toWS").css("background-color", "#666");
            $(".item-btn.toSC").css("background-color", "#999");
        }
        //限购判断
        if ($("input.buyNums").length > 0 && $("input.maxNum").length > 0) {
            var buyNums = $("input.buyNums").val() * 1;
            var maxNum = $("input.maxNum").val() * 1;
            if (buyNums > 0 && maxNum > 0 && maxNum <= buyNums) {
                $(".shop-btn.now-shop").css("background-color", "#666");
                $(".shop-btn.add-shop").css("background-color", "#999");

                $(".item-btn.toWS").css("background-color", "#666");
                $(".item-btn.toSC").css("background-color", "#999");
            }
        }

        var mySwiper = new Swiper('.swiper-container', {
            autoplay: 5000,//可选选项，自动滑动
            onSlideChangeStart: function (swiper) {
                $("#indexNum").text(mySwiper.activeIndex + 1);
            },
            lazyLoading: true
        });
        //团购倒计时
        var timeIndex = window.setInterval(function () {
            var intDiff = $(".diffTimes").val() * 1;
            var status = $("#time-item").attr("status");
            if (status == 0) {
                if ($("input.startTimes").val() * 1 > 0) {
                    intDiff = $("input.startTimes").val() * 1;
                } else {
                    $("#time-item").removeClass("startTimes");
                    $("a.now-shop").removeClass("noStart");
                    $("a.now-shop").html("立即秒杀");
                }
            }
            var times = timer(intDiff);

            $('.day_show').html(times[0]);
            $('.hour_show').html(times[1]);
            $('.minute_show').html(times[2]);
            $('.second_show').html(times[3]);

            if (status == 0 && $("input.startTimes").val() * 1 > 0) {
                $("input.startTimes").val(intDiff - 1);
            } else {
                $(".diffTimes").val(intDiff - 1);
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
            if ($("input.isBuyFlag").length > 0) {
                alert("您已经交纳了定金，不能更改规格");
                return;
            }
            $(this).addClass("inv-item-active").siblings().removeClass("inv-item-active");
            var product_specificas = "";
            $(".inv-item-active").each(function () {
                product_specificas += $(this).attr("option") + ",";
            })
            if (product_specificas.length > 0) {
                product_specificas = product_specificas.substr(0, product_specificas.length - 1);
            }
            speSelect = product_specificas;
            var _parents = $(this).parents(".attr-lay");
            $(".guigePrice_value").each(function () {
                if ($(this).attr("option") == product_specificas) {
                    var iPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                    var yuanjiaprice = $(this).attr("optionyuanprice");
                    $("#attr_lay_price").text(iPrice);//自动向上加1
                    var invNum = $(this).attr("optionnum");
                    $("#inventory").text(invNum);
                    $("#primary_price").val(yuanjiaprice);
                    $("#pro_code").val($(this).attr("optioncode"));
                    if ($(this).attr("optionimage") == null || $(this).attr("optionimage") == "") {
                        $("#attr_image").attr("src", $("#yuan_image_url").val());
                    } else {
                        $("#attr_image").attr("src", $(this).attr("optionimage"));
                    }
                    var oPrice = $(".sum .before-price span").text() * 1;
                    if ($("input.recevieId").length > 0 && proTypeId == 3) {
                        if ($(".recevieMoney").length > 0) {
                            var recevieMoney = $(".recevieMoney").val();
                            if (recevieMoney != null && recevieMoney != "") {
                                oPrice = $(".priceclass").text() * 1;
                                iPrice = recevieMoney * 1;
                                $("#attr_lay_price").text(iPrice);//自动向上加1
                            }
                        }
                    }
                    if (oPrice > iPrice && oPrice > 0) {
                        $(".attr-lay .before-price.pad").show();
                    }
                    //库存不够
                    if (invNum == "0") {
                        _parents.find(".shop-btn.now-shop").css("background-color", "#666");
                        _parents.find(".shop-btn.add-shop").css("background-color", "#999");
                    } else {
                        _parents.find(".shop-btn.now-shop").css("background-color", "#f23030");
                        _parents.find(".shop-btn.add-shop").css("background-color", "#ff8522");
                    }
                    if ($("input.buyNums").length > 0 && $("input.maxNum").length > 0) {
                        var buyNums = $("input.buyNums").val() * 1;
                        var maxNum = $("input.maxNum").val() * 1;
                        if (buyNums > 0 && maxNum > 0 && maxNum <= buyNums) {
                            $(".shop-btn.now-shop").css("background-color", "#666");
                            $(".shop-btn.add-shop").css("background-color", "#999");

                            $(".item-btn.toWS").css("background-color", "#666");
                            $(".item-btn.toSC").css("background-color", "#999");
                        }
                    }
                    var yjMoney = 0;
                    //销售佣金
                    if (saleLen > 0) {
                        iPrice = ($(this).attr("optionyuanprice") * 1).toFixed(2) * 1;
                        yjMoney = getSalePrice(iPrice);//计算商品佣金
                        $(".commission_lay").html(yjMoney);
                        // var totalMoneys = (iPrice+yjMoney).toFixed(2);
                        //$("#attr_lay_price").text(totalMoneys);
                    }


                    var oldPrice = $(this).attr("optionyuanprice");
                    if (discount > 0 && discount < 1) {
                        $(".attr-lay .before-price.pad").show();
                        var oPrice = (oldPrice * 1).toFixed(2);
                        /* $(".before-price.pad span").html(oPrice); */
                        $(".attr-lay .before-price.pad span").html(yuanjiaprice);
                        $("#attr_lay_price").text((oldPrice * 1).toFixed(2));

                        $(".attr-lay .huiyuan").html((oPrice * discount).toFixed(2));
                    }
                    var price = "";
                    var intNum = "";
                    var specIds = $(this).attr("option");
                    if (groupLen > 0) {
                        price = $(".groupPrice_arr[spec='" + specIds + "']").val();
                    }
                    if (seckillLen > 0) {//秒杀商品信息
                        price = $(".seckillPrice_arr[spec='" + specIds + "']").val();
                        intNum = $(".seckillPrice_arr[spec='" + specIds + "']").attr("seckillnum");
                        var isjoin = $(".seckillPrice_arr[spec='" + specIds + "']").attr("isjoin");
                        if (isjoin != "" && isjoin == "0") {
                            $(".seckill-btn").html("不能参加秒杀");
                            $(".seckill-btn").addClass("noStart");
                        } else {
                            $(".seckill-btn").html("立即秒杀");
                            $(".seckill-btn").removeClass("noStart");
                        }
                    }
                    if (presaleLen > 0) {
                        //判断预售是否有价格调整
                        if ($("input.presaleDiscount").length > 0 && $("input.presaleSaleType").length > 0) {
                            var proPrices = getPresalePrice(iPrice);
                            $("#attr_lay_price").text(proPrices);
                            $(".attr-lay .before-price span").html((iPrice * 1).toFixed(2));
                            if (iPrice * 1 > proPrices * 1) {
                                $(".attr-lay .before-price").show();
                            } else {
                                $(".attr-lay .before-price").hide();
                            }
                        }
                        if ($(".prePrice_arr").length > 0) {
                            intNum = $(".prePrice_arr[spec='" + specIds + "']").attr("invNum");
                        }
                    }
                    if (intNum != null && intNum != "") {
                        $("#inventory").html(intNum);
                    }
                    if (price != null && price != "") {
                        $(".sum span.before-price").show();
                        var gPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                        var invPrice = (price * discount).toFixed(2);
                        if (invPrice <= 0) {
                            invPrice = 0.01;
                        }
                        /* $(".sum span.before-price span").text(gPrice);
                         $("#attr_lay_price").text(invPrice); */
                        $(".attr-lay #attr_lay_price").text(price);
                        $(".attr-lay .before-price.pad span").html(yuanjiaprice);
                        if (yuanjiaprice * 1 <= price * 1) {
                            $(".sum span.before-price").hide();
                        }
                        if (discount != null && discount != "" && discount != "1") {
                            $(".attr-lay .huiyuan").html((price * discount).toFixed(2));
                            /* $(".attr-lay .huiyuan_span").show(); */
                        }

                        if (groupLen > 0 || $("input.groupPrice").length > 0) {//此商品是团购商品
                            //团购
                            $(".group_lay-bottom .groupLiPrice em").html(price);
                            var groupPrices = $("#prodect_price").val();
                            $(".group_lay-bottom .groupDanPrice em").html(yuanjiaprice);
                        }
                    }
                    if (rType * 1 > 0) {
                        var bol = false;
                        var nPrice = (integral * 1).toFixed(2);
                        if (rType == 1 && isChange == 1 && integral != "") {//积分商品（从积分商城进来的）
                            $(".attr-lay #attr_lay_price").parent().html("<span id='attr_lay_price'>" + nPrice + "</span>积分");
                            bol = true;
                        }
                        if (rType == 2 && isChangeFenbi == 1 && changeFenbi != "") {//粉币商品（从粉币商城进来的）
                            $(".attr-lay #attr_lay_price").parent().html("<span id='attr_lay_price'>" + changeFenbi + "</span>粉币");
                            bol = true;
                        }
                        if (bol) {
                            $(".sum span.before-price").show();
                            var oPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                            //$("p.sum").html("<span id='attr_lay_price'>"+nPrice+"</span>积分<span class='before-price pad'>原价：￥<span>"+oPrice+"</span></span>");
                            $(".attr-lay .before-price em").html("原价：￥<span>" + oPrice + "</span>");
                        }
                    }
                    if (yjMoney > 0) {
                        var price = $("#attr_lay_price").text() * 1;
                        console.log(price + "====" + yjMoney)
                        //$("#attr_lay_price").text((yjMoney*1+price).toFixed(2));
                        $("#attr_lay_price").text((price).toFixed(2));

                        if (discount > 0 && discount < 1) {
                            var huiyuan = $(".attr-lay .huiyuan").text();
                            //$(".attr-lay .huiyuan").html((huiyuan*1+yjMoney*1).toFixed(2));
                            $(".attr-lay .huiyuan").html((huiyuan * 1).toFixed(2));
                        }
                    }
                }
            })

        });
    });
    //显示商品前，完善展现的信息
    function showDiv() {
        if ($("input.isBuyFlag").length > 0) {
            alert("您已经交纳了定金，不能更改规格");
            return;
        }
        var specifica_ids = $("#xids").val();
        $(".inv-item").removeClass("inv-item-active");
        if (speSelect != null && speSelect != "") {
            specifica_ids = speSelect;
        }
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
                    var iPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                    var yuanjiaprice = $(this).attr("optionyuanprice");
                    $("#attr_lay_price").text(iPrice);
                    $("#inventory").text($(this).attr("optionnum"));
                    $("#primary_price").val(yuanjiaprice);
                    $("#pro_code").val($(this).attr("optioncode"));
                    if ($(this).attr("optionimage") == null || $(this).attr("optionimage") == "") {
                        $("#attr_image").attr("src", $("#yuan_image_url").val());
                    } else {
                        $("#attr_image").attr("src", $(this).attr("optionimage"));
                    }
                    var oPrice = $(".sum .before-price span").text() * 1;

                    if ($("input.recevieId").length > 0 && proTypeId == 3) {
                        if ($(".recevieMoney").length > 0) {
                            var recevieMoney = $(".recevieMoney").val();
                            if (recevieMoney != null && recevieMoney != "") {
                                oPrice = iPrice;
                                iPrice = recevieMoney * 1;
                                $("#attr_lay_price").text(iPrice);//自动向上加1
                            }
                        }
                    }

                    if (oPrice * 1 > iPrice) {
                        $(".attr-lay .before-price.pad").show();
                    }
                    var oldPrice = $(this).attr("optionyuanprice");
                    if (discount > 0 && discount < 1) {
                        $(".attr-lay .before-price.pad").show();
                        /* $(".before-price.pad span").html((oldPrice*1).toFixed(2)); */
                        $(".attr-lay #attr_lay_price").text(oldPrice);
                        $(".attr-lay .before-price.pad span").html((yuanjiaprice * 1).toFixed(2));
                    }
                    var price = "";
                    var intNum = "";
                    var specIds = $(this).attr("option");
                    if (groupLen > 0) {
                        price = $(".groupPrice_arr[spec='" + specIds + "']").val();
                    }
                    if (seckillLen > 0) {//秒杀商品信息
                        price = $(".seckillPrice_arr[spec='" + specIds + "']").val();
                        intNum = $(".seckillPrice_arr[spec='" + specIds + "']").attr("seckillnum");
                        var isjoin = $(".seckillPrice_arr[spec='" + specIds + "']").attr("isjoin");
                        if (isjoin != "" && isjoin == "0") {
                            $(".seckill-btn").html("不能参加秒杀");
                            $(".seckill-btn").addClass("noStart");
                        } else {
                            $(".seckill-btn").html("立即秒杀");
                            $(".seckill-btn").removeClass("noStart");
                        }
                    }
                    if (presaleLen > 0) {
                        if ($(".prePrice_arr").length > 0) {
                            intNum = $(".prePrice_arr[spec='" + specIds + "']").attr("invNum");
                        }
                        //判断预售是否有价格调整
                        if ($("input.presaleDiscount").length > 0 && $("input.presaleSaleType").length > 0) {
                            var proPrices = getPresalePrice(iPrice);
                            $("#attr_lay_price").text(proPrices);
                            $(".attr-lay .before-price span").html((iPrice * 1).toFixed(2));
                            if (iPrice * 1 > proPrices * 1) {
                                $(".attr-lay .before-price").show();
                            } else {
                                $(".attr-lay .before-price").hide();
                            }
                        }
                    }
                    if (intNum != null && intNum != "") {
                        $("#inventory").html(intNum);
                    }
                    if (price != null && price != "") {
                        $(".sum span.before-price").show();
                        var iPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                        /* $(".sum span.before-price span").text(""+iPrice);
                         $("#attr_lay_price").text((price*discount).toFixed(2)); */
                        $(".attr-lay #attr_lay_price").text(price);
                        $(".attr-lay .before-price.pad span").html(iPrice);
                        if (discount != null && discount != "" && discount != "1") {
                            var p = (price * discount).toFixed(2);
                            if (p <= 0) {
                                p = 0.01;
                            }
                            $(".attr-lay .huiyuan").html(p);
                            /* $(".attr-lay .huiyuan_span").show(); */
                        }

                        if (groupLen > 0 || $("input.groupPrice").length > 0) {//此商品是团购商品
                            //团购
                            $(".group_lay-bottom .groupLiPrice em").html(price);
                            var groupPrices = $("#prodect_price").val();
                            $(".group_lay-bottom .groupDanPrice em").html(iPrice);
                        }
                    }
                    if (rType * 1 > 0) {
                        var bol = false;
                        var nPrice = (integral * 1).toFixed(2);
                        if (rType == 1 && isChange == 1 && integral != "") {//积分商品（从积分商城进来的）
                            bol = true;
                            $(".attr-lay #attr_lay_price").parent().html("<span id='attr_lay_price'>" + nPrice + "</span>积分");
                        }
                        if (rType == 2 && isChangeFenbi == 1 && changeFenbi != "") {//粉币商品（从粉币商城进来的）
                            bol = true;
                            $(".attr-lay #attr_lay_price").parent().html("<span id='attr_lay_price'>" + changeFenbi + "</span>粉币");
                        }
                        if (bol) {
                            $(".sum span.before-price").show();
                            var oPrice = ($(this).attr("optionprice") * 1).toFixed(2);
                            $(".attr-lay .before-price em").html("原价：￥<span>" + oPrice + "</span>");
                            //$("p.sum").html("<span id='attr_lay_price'>"+nPrice+"</span>积分<span class='before-price pad'>原价：￥<span>"+oPrice+"</span></span>");
                        }
                    }
                }
            });
        } else if (groupLen == 0 && $("input.groupPrice").length > 0) {
            price = $("input.groupPrice").val();
            var gPrice = (price * discount).toFixed(2);
            if (gPrice <= 0) {
                gPrice = 0.01;
            }
            /* $("#attr_lay_price").text(gPrice); */

            $("#attr_lay_price").text(price);
            if (discount != null && discount != "" && discount != "1") {
                $(".attr-lay .huiyuan").html(gPrice);
                /* $(".attr-lay .huiyuan_span").show(); */
            }
        } else if (seckillLen == 0 && $("input.seckillPrice").length > 0) {
            price = $("input.seckillPrice").val();
            var gPrice = (price * discount).toFixed(2);
            if (gPrice <= 0) {
                gPrice = 0.01;
            }
            /* $("#attr_lay_price").text(gPrice); */

            $("#attr_lay_price").text(price);
            if (discount != null && discount != "" && discount != "1") {
                $(".attr-lay .huiyuan").html(gPrice);
                /* $(".attr-lay .huiyuan_span").show(); */
            }
        } else if (presaleLen > 0) {
            if ($(".prePrice_arr").length > 0) {
                intNum = $(".prePrice_arr[spec='" + specIds + "']").attr("invNum");
            }
            if ($(".preInvNum").length > 0) {
                $("#inventory").html($(".preInvNum").val());
            }

            //判断预售是否有价格调整
            if ($("input.presaleDiscount").length > 0 && $("input.presaleSaleType").length > 0) {

                var iPrice = $("#attr_lay_price").html() * 1;

                var proPrices = getPresalePrice(iPrice);
                $("#attr_lay_price").text(proPrices);
                $(".attr-lay .before-price span").html((iPrice * 1).toFixed(2));
                $(".attr-lay .before-price").show();
            }
        } else {
            var price = $("#primary_price").val() * 1;
            //console.log(price);
//			var price = $("i.priceclass").html()*1;
            /* $("#attr_lay_price").text(gPrice); */
            $("#attr_lay_price").text(price);
            if (rType == 1 && isChange == 1 && integral != "") {//积分商品（从积分商城进来的）
                $("#attr_lay_price").parent().html("<span id='attr_lay_price'>" + price + "</span>积分");
            }
            if (rType == 2 && isChangeFenbi == 1 && changeFenbi != "") {//粉币商品（从粉币商城进来的）
                $("#attr_lay_price").parent().html("<span id='attr_lay_price'>" + price + "</span>粉币");
            }
            if (discount != null && discount != "" && discount != "1") {
                var gPrice = (price * discount).toFixed(2);
                if (gPrice <= 0) {
                    gPrice = 0.01;
                }
                $(".attr-lay .huiyuan").html(gPrice);
                /* $(".attr-lay .huiyuan_span").show(); */
            }
        }
        //销售佣金
        if (saleLen > 0) {
            var iPrice = $(".attr-lay #attr_lay_price").text() * 1;
            var yjMoney = getSalePrice(iPrice);//计算商品佣金
            yjMoney = yjMoney * 1;
            $(".commission_lay").html(yjMoney);
            // var totalMoneys = (iPrice+yjMoney).toFixed(2);
            var totalMoneys = (iPrice).toFixed(2);
            $(".attr-lay #attr_lay_price").text(totalMoneys);
            // $(".attr-lay #attr_lay_price").text(totalMoneys);

            var hyPrice = $(".attr-lay .huiyuan").text();
            if (hyPrice != null && hyPrice != "" && typeof(hyPrice) != "undefined") {
                hyPrice = hyPrice * 1;
                //hyPrice = hyPrice+yjMoney;
                $(".attr-lay .huiyuan").text((hyPrice * 1).toFixed(2));
            }
        }
        $("#fade").show();
        $(".attr-lay").show();
    }
    function hideDiv() {
        $("#fade").hide();
        $(".attr-lay").hide();

        $("body,html").attr("style", "");
        if ($(".wrap-item").css("display") == "block") {
            $(".wrap-item").hide();
        }

        if ($(".wrap-item").css("display") == "block") {
            $(".wrap-item").hide();
        }
        if ($(".cd-code").css("display") == "block") {
            $(".cd-code").hide();
        }
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

    var loc = null;
    var isMapInit = false;
    if ($("#geoPage").length > 0) {
        //监听定位组件的message事件
        window.addEventListener('message', function (event) {
            loc = event.data; // 接收位置信息
            //console.log('location', loc);

            if (loc && loc.module == 'geolocation' && !isMapInit) { //定位成功,防止其他应用也会向该页面post信息，需判断module是否为'geolocation'
                isMapInit = true;
                getFreight(loc.province);
            } else { //定位组件在定位失败后，也会触发message, event.data为null
                //alert('定位失败');
            }

        }, false);
        //为防止定位组件在message事件监听前已经触发定位成功事件，在此处显示请求一次位置信息
        document.getElementById("geoPage").contentWindow.postMessage('getLocation', '*');

        //设置5s超时，防止定位组件长时间获取位置信息未响应
        var timesIndex = setTimeout(function () {
            if (loc == null && !isMapInit) {
                //主动与前端定位组件通信（可选），获取粗糙的IP定位结果
                document.getElementById("geoPage").contentWindow.postMessage('getLocation.robust', '*');
            } else {
                ClearTimeout(timesIndex);
            }
        }, 5000); //5s为推荐值，业务调用方可根据自己的需求设置改时间，不建议太短
    }

    function getFreight(province) {
        var shopId = $("input#shopid").val();
        var arr = [];
        var obj = {
            shop_id: shopId,
            price_total: $(".priceclass").text(),
            proNum: 1
        };
        arr[arr.length] = obj;
        var obj = {
            province: province,
            orderArr: JSON.stringify(arr)
        };
        $.ajax({
            url: "mFreight/79B4DE7C/getFreightByProvinceId.do",
            type: "POST",
            data: obj,
            timeout: 300000,
            dataType: "json",
            success: function (data) {
                var data = data.maps;
                //console.log(data)
                if (data != null) {
                    for (sId in data) {
                        var price = data[sId];
                        if (price != null && price != "") {
                            price = parseFloat(price).toFixed(2);
                        } else {
                            price = "0.00";
                        }
                        $("span.pay").html("运费：￥" + price);
                    }
                }
            }, error: function () {
            }
        });
    }
    function hideImgDiv() {
        $("#fade1").hide();
        $(".imgDivAttr").hide();
        $(".msgDivAttr").hide();
        $(".attr").hide();
    }
    function showImgDiv() {
        $("#fade1").show();
        $(".imgDivAttr").show();
    }
    function showCardWrap() {
        $("#fade1").show();
        $(".cardDivAttr").show();
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

    function NoClickDelay(el) {
        this.element = typeof el == 'object' ? el : document.getElementById(el);
        if (window.Touch) this.element.addEventListener('touchstart', this, false);
    }
    NoClickDelay.prototype = {
        handleEvent: function (e) {
            switch (e.type) {
                case 'touchstart':
                    this.onTouchStart(e);
                    break;
                case 'touchmove':
                    this.onTouchMove(e);
                    break;
                case 'touchend':
                    this.onTouchEnd(e);
                    break;
            }
        },
        onTouchStart: function (e) {
            e.preventDefault();
            this.moved = false;
            this.theTarget = document.elementFromPoint(e.targetTouches[0].clientX, e.targetTouches[0].clientY);
            if (this.theTarget.nodeType == 3) this.theTarget = theTarget.parentNode;
            this.theTarget.className += ' pressed';
            this.element.addEventListener('touchmove', this, false);
            this.element.addEventListener('touchend', this, false);
        },
        onTouchMove: function (e) {
            this.moved = true;
            this.theTarget.className = this.theTarget.className.replace(/ ?pressed/gi, '');
        },
        onTouchEnd: function (e) {
            this.element.removeEventListener('touchmove', this, false);
            this.element.removeEventListener('touchend', this, false);
            if (!this.moved && this.theTarget) {
                this.theTarget.className = this.theTarget.className.replace(/ ?pressed/gi, '');
                var theEvent = document.createEvent('MouseEvents');
                theEvent.initEvent('click', true, true);
                this.theTarget.dispatchEvent(theEvent);
            }
            this.theTarget = undefined;
        }
    };
    $("svg").css("width", "38%");
    function showCode() {
        $(".cd-code").show();
        $("#fade").show();
    }
    function closeCode(obj) {
        $(obj).parents(".cd-code").hide();
        $("#fade").hide();
    }

</script>
</body>
</html>