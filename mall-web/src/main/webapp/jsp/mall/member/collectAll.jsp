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
    <meta name="MobileOptimized" content="320">
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
    <title>我的收藏</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/public2.css"/>
    <link id="link" rel="stylesheet" type="text/css" href="/css/mall/collectAll.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js?<%=System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="/js/plugin/html5shiv.min.js?<%= System.currentTimeMillis()%>"></script>
</head>

<body>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<header class="co-header flex-row-bt ai">
    <div class="pre-icon" onclick="javascript:location.href='/mMember/79B4DE7C/toUser.do?member_id=${memberId}&uId=${userid }'"></div>
    <div>我的收藏</div>
    <div class="btn_div">
        <c:if test="${!empty xlist }">
            <a href="javascript:void(0);" class="edit_a nav_a" style="<c:if test='${!empty type }'>display:none;</c:if>">编辑</a>
            <a href="javascript:void(0);" class="finish_a nav_a" style="<c:if test='${empty type }'>display:none;</c:if>">完成</a>
        </c:if>
    </div>
</header>
<div class="coWrapper f2" style="margin-bottom:2rem;">

    <div class="co-list">
        <c:if test="${!empty xlist }">
            <ul>
                <c:forEach items="${xlist }" var="shopall">
                    <li class="co-border">
                        <input type="hidden" class="urls" value="/mallPage/${shopall.id}/${shopall.shop_id}/79B4DE7C/phoneProduct.do"/>
                        <a href="javascript:void(0);" class="list-a flex-row-bt proUrl">
                            <i class="ch-icon i-uncheck finish_a" id="check" ids="${shopall.cId}" onclick="isCheck(this)"
                               style="<c:if test='${empty type }'>display:none;</c:if>-webkit-align-self:center;"></i>
                                <%-- <img src="${http}${shopall.image_url}" alt="" class="co-img"> --%>
                            <div class="img_div" style="background-image: url(${http}${shopall.image_url})"></div>
                            <div class="item-info rel">
                                <div>
                                    <p class="multi-overflow">
                                        <c:if test="${!empty shopall.pro_label && shopall.pro_label != ''}">
                                            <span class="isRec f1">${shopall.pro_label }</span>
                                        </c:if>
                                        <span>${shopall.pro_name}</span>
                                    </p>
                                    <p class="c-r">
                                        &yen;<span class="co-digit pdr1 f4 price" style="margin-right: 0.1rem;">${shopall.price}</span>
                                        <c:if test="${!empty shopall.hyPrice && discount != 1 && shopall.is_member_discount == 1}">
                                            <span>会员价：&yen;
                                                <span class="co-digit hyPrice">
                                                    <c:if test="${shopall.hyPrice*1 <= 0}">0.01</c:if>
                                                    <c:if test="${shopall.hyPrice*1 > 0}">${shopall.hyPrice }</c:if>
                                                </span>
                                            </span>
                                        </c:if>
                                    </p>
                                    <p class="c-g ltg">
                                        <c:if test="${shopall.pro_cost_price > 0 && shopall.pro_cost_price > shopall.price}">
                                            &yen;<span class="co-digit oldPrice">${shopall.pro_cost_price }</span>
                                        </c:if>
                                    </p>
                                </div>
                                <p class="c-r abs" style="bottom:0;right:0">
                        	<span onclick="deleteCollect(${shopall.cId});" class="delete finish_a" id="delete"   style="<c:if test='${empty type }'>display:none;</c:if>">删除</span>
                                </p>
                            </div>
                        </a>
                    </li>
                </c:forEach>
            </ul>
            <p class="tc c-g pdt">没有更多了</p>
        </c:if>
    </div>

</div>
<c:if test="${!empty xlist }">
    <div class="co-edit flex-row-bt ai finish_a" style="<c:if test='${empty type }'>display:none;</c:if>">
        <div class="df ai">
            <span class="ch-icon2 i-uncheck" id="allCheck" onclick="isAllCheck(this)"></span>
            <span class="lh">全选</span>
        </div>
        <a href="javascript:void(0)" class="ch-del lt1 deleteAll">删除</a>
    </div>

</c:if>
</body>
<script src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
<script src="/js/plugin/layer-mobile/layer/layer.js?<%= System.currentTimeMillis()%>"></script>
<script>
    $(function () {
        //完成操作
        $(".finish_a.nav_a").click(function () {
            $(".finish_a").hide();
            $(".edit_a").show();
        });
        //编辑操作
        $(".edit_a.nav_a").click(function () {
            $(".edit_a").hide();
            $(".finish_a").show();
        });
        $(".sort-div").click(function () {
            $(this).find("ul").toggle();
        })
        $(".item-info").each(function () {
            var price = $(this).find(".price").html();
            if (price != null && price != "") {
                var a = price.split(".");
                $(this).find(".price").html(a[0] + ".<small>" + a[1] + "</small>");
            }
            var hyprice = $(this).find(".hyPrice").html();
            if (hyprice != null && hyprice != "") {
                var a = hyprice.split(".");
                $(this).find(".hyPrice").html(a[0] + ".<small>" + a[1] + "</small>");
            }

            var old = $(this).find(".oldPrice").html();
            if (old != null && old != "") {
                var a = old.split(".");
                $(this).find(".oldPrice").html(a[0] + ".<small>" + a[1] + "</small>");
            }
        });

        $(".proUrl").click(function (event) {
            if (event.target.id != "delete" && event.target.id != "check") {
                location.href = $(this).parent().find("input.urls").val();
            }
        });

        //批量删除
        $(".deleteAll").click(function () {
            var idArr = [];
            $("i.i-check").each(function () {
                idArr.push($(this).attr("ids"));
            });
            if (idArr == null || idArr.length == 0) {
                alert("请选择你要删除的收藏");
            } else {
                if (confirm("您确定要删除收藏商品？")) {
                    var data = {
                        ids: JSON.stringify(idArr),
                        isDelete: 1
                    };
                    ajaxPost(data, "删除");
                }
            }
        });
    });

    $(window).load(function () {
        setTimeout(function () {
            $(".loading").hide();
        }, 1000);
    });
    function deleteCollect(id) {
        var idArr = [];
        if (confirm("您确定要删除收藏商品？")) {
            idArr.push(id);

            var data = {
                ids: JSON.stringify(idArr),
                isDelete: 1
            };
            ajaxPost(data, "删除");
        }
    }

    function ajaxPost(data, tip) {
        var index = layer.open({
            title: "",
            content: "",
            type: 2,
            shadeClose: false
        });
        $.ajax({
            type: "post",
            data: data,
            url: "/mMember/79B4DE7C/deleteCollect.do",
            dataType: "json",
            success: function (data) {
                layer.closeAll();
                if (data != null) {
                    if (data.result) {
                        alert(tip + "成功");
                        location.href = window.location.href;
                    } else {
                        alert(tip + "失败");
                    }
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.closeAll();
                alert(tip + "失败");
                return;
            }
        });
    }

    /* function isCheck(obj) {
     if(!$(obj).is(".i-check")){
     $(obj).addClass("i-check").removeClass("i-uncheck");
     }else{
     $(obj).addClass("i-uncheck").removeClass("i-check");
     }
     } */


    function isCheck(obj) {
        if (!$(obj).is(".i-check")) {
            $(obj).addClass("i-check").removeClass("i-uncheck");
        } else {
            $(obj).addClass("i-uncheck").removeClass("i-check");
        }
        var len1 = $("div.co-list").find(".ch-icon").length;
        var len2 = $("div.co-list").find(".i-check").length;
        if (len1 == len2) {
            $("#allCheck").removeClass("i-uncheck").addClass("i-check");
        } else {
            $("#allCheck").addClass("i-uncheck").removeClass("i-check");
        }
    }
    function isAllCheck(obj) {
        if ($(obj).is(".i-check")) {
            $(obj).removeClass("i-check").addClass("i-uncheck");
            $("div.co-list").find(".ch-icon").removeClass("i-check").addClass("i-uncheck");
        } else {
            $(obj).removeClass("i-uncheck").addClass("i-check");
            $("div.co-list").find(".ch-icon").removeClass("i-uncheck").addClass("i-check")
        }
    }

</script>
</html>