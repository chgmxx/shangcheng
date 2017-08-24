<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商品管理-商品编辑</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css"
          href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/reset.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/swiper.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/editNextOne.css"/>

</head>
<body>
<div class="Warp">
    <div class="main">
        <section class="pic-carousel">
            <div class="swiper-container">
                <div class="swiper-wrapper" id="wrapper">
                </div>
            </div>
            <div class="page">
                <span>1</span>/<span class="imgSize">1</span>
            </div>
        </section>
        <div class="price-box">
            <h2 class="pName">Apple iPhone 6s (A1700) 16G 玫瑰金色 移动联通电信4G手机</h2>
            <div class="price pad">
                <span class="red">￥</span><span class="pPrice" id="pPrice">4888.00</span>
            </div>
            <div class="before-price pad">
                原价：<span class="oldPrice">￥4888.00</span>
            </div>
        </div>

        <div class="select pad">选择<span class="spec"></span></div>
        <!-- <div id="" class="address pad">
            <div class="addr-box">
                送至：<em></em><span class="addr">广东惠州市惠城区东平大道49号</span>
            </div>
            <div>
                <span class="pay">免运费</span> <span>销量：<label>888</label>件
                </span>
            </div>
        </div> -->

        <div class="go-other pad">
            <div class="mall-name">
                <img src="/images/icon/logo2.png"/>
                <p class="sName">多粉官方商店</p>
            </div>
            <div class="other-mall">
                <a href="javascript:;">全部商品</a> <a href="javascript:;">进入店铺</a>
            </div>
        </div>
    </div>
</div>

<footer class="footer">
    <ul>
        <li class="foot-item"><img src="/images/icon/mall-person.png"/>
            <p>我的</p></li>
        <li class="foot-item"><img src="/images/icon/mall-shop.png"/>
            <p>购物车</p></li>
        <li class="foot-item2"><a href="javascript:;" class="add-shop">加入购物车</a>
        </li>
        <li class="foot-item2"><a href="javascript:;" class="now-shop">立即购买</a>
        </li>
    </ul>
</footer>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/mall/product/swiper.jquery.min.js" charset="utf-8"></script>
<script type="text/javascript" src="/js/mall/product/pro_preview.js"></script>
<script type="text/javascript">
    var mySwiper;
    $(window)
        .load(
            function () {
                var a = $(window).width(), b = $(window).height(), d = 870, meta = $("#meta");
                setTimeout(function () {
                    meta.attr("content", "width=870,initial-scale="
                        + a / d + ", minimum-scale=" + a / d
                        + ", maximum-scale=" + a / d
                        + ", user-scalable=no");
                    $(".loading").hide();
                }, 300);
            });
    mySwiper = new Swiper('.swiper-container', {
        autoplay: 5000,//可选选项，自动滑动
        observer: true,//修改swiper自己或子元素时，自动初始化swiper
        observeParents: true,//修改swiper的父元素时，自动初始化swiper
        onInit: function (swiper) {
            var parentWin = window.parent.document;
            // 商品图片
            var proImgHtml = "";
            var imgObj = $(parentWin).find(".picture-list li img");
            // 遍历商品图片
            imgObj.each(function () {
                var src = $(this).attr("src");
                proImgHtml += "<div class=\"swiper-slide\"><img src=\"" + src
                    + "\" /></div>";
            });
            if (proImgHtml != "") {
                $("span.imgSize").html(imgObj.length);
                $(".swiper-wrapper").html(proImgHtml);
            }
        }
    });
    loadProPreView();

</script>
</body>
</html>