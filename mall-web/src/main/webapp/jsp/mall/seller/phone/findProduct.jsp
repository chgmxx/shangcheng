<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
    <title>自选商品</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
    <style>
        html, body, .sWrapper {
            height: 100%;
            overflow: hidden;
        }
    </style>
    <script type="text/javascript">
        var findIdObj = new Object();
    </script>
</head>

<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/mall/seller/main.css?<%=System.currentTimeMillis()%>"/>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<c:if test="${!empty findIds }">
    <c:forEach var="arr" items="${findIds }">
        <script type="text/javascript">
            findIdObj["${arr}"] = "${arr}";
        </script>
    </c:forEach>
</c:if>
<div class="sWrapper"><!-- backUrl -->
    <header class="arrow-header" onclick="javascript:location.href='/phoneSellers/79B4DE7C/toMallSet.do?type=2&uId=${userid}'">
        自选商品
    </header>
    <div class="c-search-box">
        <input type="text" class="c-search-input srh-common proName" value="${proName }">
        <i class="c-search-icon srh-i-common" onclick="queryurl('${groupId}')"></i>
    </div>
    <section class="c-body">
        <ul class="c-left c-scroll">
            <li class="c-left-a oneOf <c:if test='${empty groupId && empty groupPId}'>left-active</c:if>" onclick="queryurl('-1')">全部商品</li>
            <c:if test="${!empty classList }">
                <c:forEach var="classs" items="${classList }">
                    <c:set var="isDefault" value="false"></c:set>
                    <c:if test='${!empty groupId && groupId == classs.group_id}'>
                        <c:set var="isDefault" value="true"></c:set>
                    </c:if>
                    <c:if test='${!empty groupPId && groupPId == classs.group_id}'>
                        <c:set var="isDefault" value="true"></c:set>
                    </c:if>
                    <li>
                        <a href="javascript:void(0)" class="c-left-a oneOf <c:if test='${isDefault}'>left-active</c:if>"
                           onclick="
                           <c:if test='${classs.is_child == 1}'>groupChild(this,${classs.group_id });</c:if>
                               <c:if test='${classs.is_child == 0}'>queryurl('${classs.group_id }');</c:if>">${classs.group_name }</a>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
        <ul class="c-right c-scroll childGroup" style="display: none;">
            <!-- <li class="m-menu-li">
                <a href="chooseItem.html" class="m-menu-a">
                    <p class="me-item-bg">无图片</p>
                    <p class="oneOf m-item-text">阿迪达斯dfdfdfdfdf</p>
                </a>
            </li> -->
        </ul>
        <ul class=" c-right c-scroll  product_ul">
            <c:if test="${!empty page && !empty page.subList}">
                <c:forEach var="product" items="${page.subList }">
                    <li>
                        <c:set var="url" value="/mallPage/${product.id}/${product.shop_id}/79B4DE7C/phoneProduct.do?saleMemberId=${member.id }&uId=${userid}"></c:set>
                        <c:if test="${shopall.pro_type_id == 2 && shopall.member_type > 0}">
                            <c:set var="url" value="/phoneMemberController/${userid}/79B4DE7C/findMember_1.do"></c:set>
                        </c:if>
                        <div class="c-item-a">
                            <a href="${url }">
                                <div class="c-item-bg img-container" style="background-image: url('${product.image_url}');  background-size: cover;"></div>
                            </a>
                            <div class="c-item-desc">
                                <p class="m-item-name"><a href="${url }">${product.pro_name}</a></p>
                                <span class="item-price-1 oneOf">
                            <span class="iPrice-1">&yen;${product.price}</span><br>
                            <c:if test="${product.pro_cost_price > 0 && product.pro_cost_price > product.price}">
                                <span class="m-item-gone">&yen;${product.pro_cost_price }</span>
                            </c:if>
                        </span>
                                <span class="item-price-2 oneOf">
                        	<c:if test="${!empty product.hyPrice && discount != 1 && product.is_member_discount == 1}">
                                <span>会员:&yen;${product.hyPrice }</span><br>
                            </c:if>
                            <c:if test="${!empty product.pfPrice }">
                                <span>批发:&yen;${product.pfPrice }</span>
                            </c:if>
                        </span>
                                <c:set var="cla" value=""></c:set>
                                <c:if test="${!empty findIds}">
                                    <c:forEach var="i" items="${findIds }">
                                        <c:if test="${i ==  product.id}">
                                            <c:set var="cla" value="item-checks"></c:set>
                                            <script type="text/javascript">
                                                if (findIdObj != null) {
                                                    delete findIdObj["${i}"];
                                                }
                                            </script>
                                        </c:if>
                                    </c:forEach>
                                    <c:if test="${page.curPage+1 <= page.pageCount}">
                                        <input type="hidden" class="curPage" value="${page.curPage }"/>
                                        <input type="hidden" class="pageCount" value="${page.pageCount }"/>
                                        <input type="hidden" class="isLoading" value="1"/>
                                    </c:if>
                                </c:if>
                                <span class="c-item-status checkRadio ${cla }" id="${product.id }" onclick="isCheckItem(this);"></span>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
    </section>
</div>
<input type="hidden" class="urls" value="/phoneSellers/79B4DE7C/findProduct.do?uId=${userid}"/>
<input type="hidden" class="groupId" value="${groupId }"/>
<input type="hidden" class="saleMemberId forms_cla" name="saleMemberId" value="${member.id }"/>
<input type="hidden" class="userid forms_cla" name="uId" value="${userid }"/>
<footer class="c-footer fix-common">
    <a href="javascript:void(0)" class="full-blue-btn" onclick="realChoose();">确定选择</a>
</footer>

<form action="/phoneSellers/79B4DE7C/toMallSet.do?type=2&uId=${userid}" method="post" id="findProduct" name="findProduct">
    <input type="hidden" name="findIds" id="findIds" value=""/>
</form>

<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
<script type='text/javascript' src="/js/plugin/lazyload/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript" src="/js/plugin/gt/js/gt_common.js"></script>
<script type="text/javascript">
    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${record.appid}", // 必填，公众号的唯一标识
        timestamp: "${record.timestamp}", // 必填，生成签名的时间戳
        nonceStr: "${record.nonce_str}", // 必填，生成签名的随机串
        signature: "${record.signature}",// 必填，签名，见附录1
        jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.ready(function () {
        wx.hideOptionMenu();
    });

    /* jQuery(document).ready(function($){
     if($(".img-container").length > 0){
     if($(".img-container").attr("data-original") != null){
     $(".img-container").lazyload({
     effect: "fadeIn",
     container :  $(".productList")
     });
     }
     }
     }); */

    function isCheckItem(obj) {
        $(obj).toggleClass("item-checks");
    }

    function menuCheck(obj) {
        $(obj).addClass("left-active").parent().siblings("li").find("a").removeClass("left-active");
    }
    function queryurl(groupId) {

        var url = "/phoneSellers/79B4DE7C/findProduct.do?uId=${userid}";
        if (groupId != '-1' && groupId != null && groupId != "") {
            url += getUrlSymb(url) + "groupId=" + groupId;
        } else if (groupId != '-1') {
            groupId = $(".groupId").val();
            if (groupId != null && groupId != "") {
                url += getUrlSymb(url) + "groupId=" + groupId;
            }
        }
        if (groupId != '-1') {
            var proName = $(".proName").val();
            if (proName != null && proName != "" && proName != undefined) {
                //proName = encodeURIComponent(encodeURIComponent(proName))
                url += getUrlSymb(url) + "proName=" + proName;
            }
        }
        var ids = getFind();
        if (ids != null && ids != "") {
            url += getUrlSymb(url) + "findIds=" + ids;
        }
        window.location.href = url;
    }
    function getUrlSymb(url) {
        if (url.split("?").length > 1) {
            return "&&";
        } else {
            return "?";
        }
    }
    function getFind() {
        var ids = "";
        $(".checkRadio.item-checks").each(function () {
            var id = $(this).attr("id");
            if (ids != "") {
                ids += ",";
            }
            if (findIdObj != null) {
                if (findIdObj[id] != null && findIdObj[id] != "") {
                    delete findIdObj[id];
                }
            }
            ids += id;
        });
        if (findIdObj != null) {
            for (var id in findIdObj) {
                if (ids != "") {
                    ids += ",";
                }
                ids += id;
            }
        }
        return ids;
    }
    /**
     * 确认选择
     */
    function realChoose() {
        var ids = getFind();
        /* 	$(".checkRadio.item-checks").each(function(){
         var id = $(this).attr("id");
         if(ids != ""){
         ids += ",";
         }
         if(findIdObj != null){
         if(findIdObj[id] != null && findIdObj[id] != ""){
         delete findIdObj[id];
         }
         }
         ids += id;
         }); */
        if (ids != "") {
            $("input#findIds").val(ids);
            document.findProduct.submit();
        } else {
            gtcom.dialog("请选择商品", "a");
        }
    }


    function groupChild(obj, groupId) {
        $(".product_ul").hide();
        $(".childGroup").show();

        getChildGroup(obj, groupId);
    }
    function getChildGroup(obj, groupId) {
        var urls = $("input.urls").val();
        $.ajax({
            url: "/phoneProduct/79B4DE7C/getChildGroup.do",
            type: "POST",
            data: {
                groupPId: groupId
            },
            timeout: 300000,
            dataType: "json",
            success: function (data) {
                var html = "";
                if (data != null) {
                    var imgHttp = data.http;
                    if (data.childList != null) {
                        for (var i = 0; i < data.childList.length; i++) {
                            var child = data.childList[i];
                            html += '<li class="m-menu-li">';
                            html += '<a href="' + urls + '&groupId=' + child.id + '" class="m-menu-a">';
                            //html += '<div class="itemBg">';
                            if (child.image_url == null || child.image_url == "") {
                                html += '<p class="me-item-bg" >无图片</p>';
                            } else {
                                var img = imgHttp + child.image_url;
                                html += '<p class="me-item-bg" style="background: url(' + img + ') no-repeat center center; background-size: contain;"></p>';
                            }
                            //html += '</div>';
                            html += '<p class="oneOf m-item-text">' + child.groupName + '</p>';
                            html += '</a>';
                            html += '</li>';
                        }
                    }
                    $(obj).addClass("left-active").parent().siblings("li").find("a").removeClass("left-active");
                }
                $(".childGroup").html(html);
                $(".childGroup").css("background", "none");
            },
            error: function () {
            }
        });
    }


    var $container = $('.product_ul');
    if ($("input.curPage").length > 0 && $("input.isLoading").length > 0) {
        $container.scroll(function () {
            var curPage = $("input.curPage").val();//当前页
            var pageCount = $("input.pageCount").val();//总共的页数
            var isLoading = $("input.isLoading").val();//是否继续加载
            if (isLoading == 0 || curPage + 1 > pageCount) {
                $container.unbind('scroll');
                return false;
            }
            var totalHeight = $container.prop('scrollHeight');
            var scrollTop = $container.scrollTop();
            var height = $container.height();
            var TRIGGER_SCROLL_SIZE = $container.find("li:eq(0)").height();
            if (totalHeight - (height + scrollTop) <= TRIGGER_SCROLL_SIZE && isLoading == 1) {
                loadMore();
            }
        });
    }

    function loadMore() {
        var curPage = $("input.curPage").val();
        if (curPage == null || curPage == '') {
            return false;
        }

        var datas = {
            curPage: curPage * 1 + 1,
            isSeller: 1,
            isFindSeller: 1
        };
        if ($(".forms_cla").length > 0) {
            $(".forms_cla").each(function () {
                var val = $(this).val();
                if (val != null && val != "") {
                    datas[$(this).attr("name")] = val;
                }
            });
        }
        var ids = getFind();
        if (ids != null && ids != '') {
            datas["findIds"] = ids;
        }
        $("input.isLoading").val(-1);
        $.ajax({
            type: "post",
            url: "/mallPage/79B4DE7C/shoppingAllPage.do",
            data: datas,
            dataType: "json",
            success: function (data) {
                var saleMemberId = $("input.saleMemberId").val();
                var userid = $("input.userid").val();
                var html = "";
                if (data != null) {
                    var page = data.page;
                    if (page == null) {
                        return false;
                    }
                    if (page.curPage * 1 <= page.pageCount * 1) {
                        if (page.subList != null && page.subList.length > 0) {
                            for (var i = 0; i < page.subList.length; i++) {
                                var product = page.subList[i];
                                html += '<li>';
                                var return_url = product.return_url + "&uId=" + userid;
                                if (saleMemberId != null && saleMemberId != "") {
                                    return_url += "&saleMemberId=" + saleMemberId;
                                }
                                html += '<div class="c-item-a">';
                                html += '<a href="' + return_url + '"><div class="c-item-bg img-container" style="background-image: url(' + product.image_url + ');  background-size: cover;"></div></a>';
                                html += '<div class="c-item-desc">';
                                html += ' <p class="m-item-name"><a href="' + return_url + '">' + product.pro_name + '</a></p>';
                                html += '<span class="item-price-1 oneOf">';
                                html += '<span class="iPrice-1">&yen;' + product.price + '</span><br>';
                                if (product.pro_cost_price != null && product.pro_cost_price != '') {
                                    if (product.pro_cost_price > 0) {
                                        html += '<span class="m-item-gone">&yen;' + product.pro_cost_price + '</span>';
                                    }
                                }
                                html += '</span>';
                                html += '<span class="item-price-2 oneOf">';
                                var hyPrice = product.hyPrice;
                                if (hyPrice != null && hyPrice != '' && hyPrice * 1 > 0) {
                                    html += '<span>会员:&yen;' + hyPrice + '</span><br>';
                                }
                                if (product.pfPrice != null && product.pfPrice != '') {
                                    html += '<span>批发:&yen;' + product.pfPrice + '</span>';
                                }
                                html += '</span>';
                                var cla = "";
                                if (findIdObj != null) {
                                    var pros = findIdObj[product.id];
                                    if (pros != null && pros != "" && typeof(pros) != "undefined") {
                                        if (pros == product.id) {
                                            cla = "item-checks";
                                        }
                                    }
                                }
                                html += '<span class="c-item-status checkRadio ' + cla + '" id="' + product.id + '" onclick="isCheckItem(this);"></span>';
                                html += '</div>';
                                html += '</div>';
                                html += "</li>";
                            }
                        }
                    }
                    $("input.curPage").val(page.curPage);
                }
                if (html == "") {
                    $("input.isLoading").val(0);
                    $container.unbind('scroll');
                    return false;
                } else {
                    $container.append(html);
                    $("input.isLoading").val(1);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                $("input.isLoading").val(1);
            }
        });
    }

</script>
</body>
</html>