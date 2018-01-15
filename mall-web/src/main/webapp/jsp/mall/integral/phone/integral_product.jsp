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
    <title>商品详情</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
    <style type="text/css">
        strong {
            font-weight: bold !important;
        }

        .layermbox2 .layermcont i {
            width: 15px !important;
            height: 15px !important;
        }
    </style>
</head>
<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<script src="/js/mall/integral/camel.js?<%=System.currentTimeMillis()%>"></script>
<script src="/js/mall/integral/swiper-3.3.1.min.js"></script>
<link rel="stylesheet" href="/css/mall/integral/swiper-3.3.1.min.css">
<link rel="stylesheet" href="/css/mall/integral/global.css?<%=System.currentTimeMillis()%>">
<link rel="stylesheet" href="/css/mall/integral/public.css?<%=System.currentTimeMillis()%>">
<script type="text/javascript" src="/js/phone/phone.js?<%=System.currentTimeMillis()%>"></script>

<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<link rel="stylesheet" type="text/css" href="/js/plugin/layer-mobile/layer/need/layer.css"/>

<div class="index">
    <div class="back-icon" onclick="toIndex();">
        <img src="/images/mall/integral/intdegral-back.png" alt="">
    </div>
    <c:if test="${!empty imageList }">
        <div class="swiper-container index-header">
            <div class="swiper-wrapper">
                <c:forEach var="image" items="${imageList }">
                    <div class="swiper-slide"><img src="${imageHttp }${image.imageUrl }" alt=""></div>
                </c:forEach>
            </div>
            <div class="swiper-pagination"></div>
        </div>
    </c:if>

    <div class="integral-title">
        <div class="integral-title-slogan name_div">${product.proName }</div>
        <div class="clear integral-title-info">
            <div class="fl">
                ${integral.money }积分
            </div>
            <div class="fr">
                <span>${recordNum }</span> 人兑换
            </div>
        </div>
    </div>
    <!--商品详情选择-->
    <div class="line-list line-list--indent line-list--flex shopping-detail-info-wrap">
        <div class="line-item shopping-detail-info-title">
            <p class="item-tt">
                <span>已选</span>
                <c:if test="${!empty guige }">
                    <input type="hidden" class="xIds" value="${guige.xids }"/>
                    <input type="hidden" class="specValue" value="${guige.specifica_name }"/>
                    <span class="chooseSpe">
	                </span>
                </c:if>
                <span class="nums_span">1</span>
                <span>个</span>
            </p>
            <i class="icon-v-right rotate-arrow"></i>
        </div>
        <!--隐藏-->
        <div class="line-item shopping-detail-info-hide">
            <ul class="shopping-detail-ul">
                <c:if test="${!empty specificaList }">
                    <c:forEach var="specifica" items="${specificaList }">
                        <li class="line-item">
                            <span class="line-item-begin">${specifica.specName }</span>
                            <p class="shopping-detail-info spe_value_p">
                                <c:if test="${!empty specifica.specValues }">
                                    <c:forEach var="values" items="${specifica.specValues  }" varStatus="j">
                                        <span class="" value_id="${values.specValueId }">${values.specValue }</span><!-- class="shopping-detail-info-checked" -->
                                    </c:forEach>
                                </c:if>
                            </p>
                        </li>
                    </c:forEach>
                </c:if>
                <li class="line-item shopping-detail-number">
                    <span class="line-item-begin">数量</span>
                    <c:if test="${!empty product.flowId }">
                        <c:if test="${product.flowId > 0 }">
                            <c:set var="isFlow" value="1"></c:set>
                        </c:if>
                    </c:if>
                    <c:if test="${empty isFlow }">
                        <div>
                            <strong class="line-item-begin shopping-reduce">-</strong>
                            <input type="number" value="1" min="1" max="-1" class="productNum">
                            <strong class="line-item-begin shopping-add">+</strong>
                        </div>
                    </c:if>
                    <c:if test="${!empty isFlow }">
                        <div style="width: 40px;text-align: center;">
                            1
                            <input type="hidden" value="1" class="productNum">
                        </div>
                    </c:if>
                </li>
            </ul>
        </div>

    </div>
    <!--商品详情选择-->
    <div class="integral-exchange">
        <p>兑换说明</p>
        <ul>
            <li>
                1、点击【立即兑换】，即可兑换成功；
            </li>
            <li>
                2、在【兑换记录】可查询已兑换的物品；
            </li>
            <c:if test="${!empty integral }">
                <li>
                    3、兑换时间${integral.startTime }至${integral.endTime }
                </li>
            </c:if>
        </ul>

    </div>
    <div class="integral-detail">
        <p>商品详情</p>
        <div class="integral-detail-content">

        </div>
    </div>
    <div class="integral-bottom">
        <div class="integral-bottom-btn  submit_btn" onclick="integral(0,0);">立即兑换</div>
    </div>

    <!--手机注册弹框-->
    <section class="dialog-wrap register-box active flowSection" style="display: none;">
        <div class="overlay"></div>
        <div class="dialog">
            <div class="dialog-bd">
                <h3 class="bd-tt">请输入要兑换的手机号码</h3>
                <p class="bd-txt txt--left">
                    <input type="text" class="register-box-input flowPhone" placeholder="请输入手机号码">
                </p>
            </div>
            <footer class="dialog-ft">
                <span class="ft-btn cancel">取消</span>
                <span class="ft-btn register-btn-ok" onclick="integral(1,1);">确定</span>
            </footer>
        </div>
    </section>
    <!--兑换成功弹框-->
    <section class="dialog-wrap register-box active virtualSection" style="display: none;">
        <div class="overlay"></div>
        <div class="dialog">
            <div class="dialog-bd">
                <h3 class="bd-tt">兑换成功</h3>
                <p class="bd-txt txt--left content">
                    恭喜你获得<em>500元代金券</em>
                </p>
            </div>
            <footer class="dialog-ft">
                <span class="ft-btn cancel">取消</span>
                <span class="ft-btn is_ok" onclick="oks();">确定</span>
                <span class="ft-btn register-btn-ok" onclick="details();">查看详情</span>
            </footer>
        </div>
    </section>

    <section class="register-btn-ok


    dialog-wrap register-box active failedSection" style="display: none;">
        <div class="overlay"></div>
        <div class="dialog">
            <div class="dialog-bd">
                <h3 class="bd-tt">兑换失败</h3>
                <p class="bd-txt txt--left errorMsg">
                    兑换失败，请稍后重试
                </p>
            </div>
            <footer class="dialog-ft">
                <span class="ft-btn cancel">关闭</span>
            </footer>
        </div>
    </section>
</div>
<c:if test="${!empty guigePriceList}">
    <c:forEach var="guigePrices" items="${guigePriceList }">
        <input type="hidden" class="guigePrices" x_ids="${guigePrices.xsid }" inv_price="${guigePrices.inv_price }" inv_num="${guigePrices.inv_num }"/>
    </c:forEach>
</c:if>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="shopId" value="${shopId }"/>
<input type="hidden" class="memberId" value="${member.id }"/>
<input type="hidden" class="memberIntegrals" value="${member.integral }"/>
<input type="hidden" class="proIntegral" value="${integral.money }"/>
<input type="hidden" class="proTypeId" value="${product.proTypeId }"/>
<input type="hidden" class="integralId" value="${integral.id }"/>
<input type="hidden" class="productId" value="${product.id }"/>
<input type="hidden" class="productInvNum" value="${product.proStockTotal }"/>
<input type="hidden" class="isNoStart" value="${isNoStart }"/>
<input type="hidden" class="isEnd" value="${isEnd }"/>
<form action="/phoneIntegral/79B4DE7C/addressList.do?uId=${userid }&shopId=${shopId }" id="toAddList" method="post">
    <input type="hidden" class="orders" name="orders"/>
</form>

<input type="hidden" class="isMember" value="${isMember }"/>

<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<script src="/js/mall/integral/phone/integral_product.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript">
    $(".loading").hide();

    var mySwiper = new Swiper('.swiper-container', {
        direction: 'horizontal',
        loop: false,

        // 如果需要分页器
        pagination: '.swiper-pagination',
        observer: true,
        observeParents: true,
        onInit: function (mySwiper) {

        },
        onSlideChangeEnd: function (mySwiper) {

        }
    });
    var detail = "${detail.productDetail}";
    console.log(detail,"detail")
    if (detail != null && detail != '' && typeof(detail) != "undefined") {
        detail = detail.replace(/&quot;/g, "\"").replace(/&apos;/g, "'");
        $(".integral-detail-content").html(detail);
    }
    function toIndex() {
        var userid = $(".userid").val();
        var shopId = $(".shopId").val();
        location.href = "/phoneIntegral/" + shopId + "/79B4DE7C/toIndex.do?uId=" + userid;
    }
    //商品详情新加js
    $('.shopping-detail-info-title ').on('click', function () {
        $(this).siblings('.shopping-detail-info-hide').slideToggle(200)
        $(this).find('.icon-v-right').toggleClass('rotate-arrow')
    });
</script>
</body>
</html>