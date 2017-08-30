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
    <title>商城设置</title>
    <link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>"/>
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
<script src="/js/plugin/jquery-1.8.3.min.js?"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<c:if test="${!empty findIdArray }">
    <c:forEach var="arr" items="${findIdArray }">
        <script type="text/javascript">
            findIdObj["${arr}"] = "${arr}";
        </script>
    </c:forEach>
</c:if>
<!--输入框 遮罩层-->
<section class="cd-layer">
    <div class="layer-body">
        <p class="layer-topic">是否删除该商品？</p>
        <div class="layer-bottom-abs">
            <a href="javascript:void(0)" class="layer-btn btnCancel" onclick="closeLayer(this)">取消</a>
            <a href="javascript:void(0)" class="layer-btn" onclick="delItem(this)">确定</a>
        </div>
    </div>
</section>
<div class="mWrapper">
    <header class="arrow-header" onclick="javascript:location.href='${backUrl}'">
        商城设置
    </header>
    <c:set var="is_open_optional" value="0"></c:set>
    <c:set var="isNext" value="0"></c:set>
    <c:if test="${!empty mallSet }">
        <c:set var="isNext" value="1"></c:set>
        <c:set var="is_open_optional" value="${mallSet.isOpenOptional }"></c:set>
    </c:if>
    <nav class="r-nav">
        <ul class="r-nav-ul">
            <li onclick="showNav(1,${isNext});" class="nav-setting <c:if test="${empty type || type == 1}">nav-active</c:if>">基本信息</li>
            <li onclick="showNav(2,${isNext});" class="nav-setting <c:if test="${type == 2}">nav-active</c:if>">自选商品</li>
        </ul>
    </nav>
    <c:if test="${empty type || type == 1}">
        <section class="set-body" id="setting-1">
            <div class="basic-setting">
                <div class="setting-tr">
                    <label class="set-label">商城名称</label>
                    <input type="hidden" name="id" value="${mallSet.id }"/>
                    <input type="text" class="set-input mallName" name="mallName" value="${mallSet.mallName }" maxlength="50">
                </div>
                <div class="setting-tr">
                    <label class="set-label">联系电话</label>
                    <input type="text" class="set-input contactNumber" name="contactNumber" value="${mallSet.contactNumber }" maxlength="30">
                </div>
                <div class="setting-tr">
                    <label class="set-label">QQ</label>
                    <input type="text" class="set-input qq" name="qq" value="${mallSet.qq }" maxlength="30">
                </div>
                <div class="setting-tr-l">
                    <label class="set-label-l">商城头像</label>
                    <form action="/mMember/79B4DE7C/updateImage.do" id="headForm" enctype="multipart/form-data" method="post">
                 <span class="set-file headSpan" style="<c:if
                         test="${!empty mallSet.mallHeadPath }">background-image: url('${http}${mallSet.mallHeadPath }');background-size:contain; border-style: solid;</c:if>">
                 	<input type="file" class="input-file" name="file" id="file2" value="+" accept="image/*" onchange="uploadImages(this,1,'headForm');">
                 </span>
                    </form>

                    <input type="hidden" class="mallHeadPath" name="mallHeadPath" value="${mallSet.mallHeadPath }"/>
                </div>
                <div class="setting-tr-l">
                    <label class="set-label-l">banner</label>
                    <form action="/mMember/79B4DE7C/updateImage.do?uId=${userid }" id="bannerForm" enctype="multipart/form-data" method="post">
                <span class="set-file-full bannerSpan" style="<c:if
                        test="${!empty mallSet.bannerPath }">background-image: url('${http}${mallSet.bannerPath }');background-size:contain; border-style: solid;</c:if>">
                	<input type="file" class="input-file" name="file" id="file2" value="+" accept="image/*" onchange="uploadImages(this,2,'bannerForm');">
                </span>
                    </form>
                    <input type="hidden" class="bannerPath" name="bannerPath" value="${mallSet.bannerPath }"/>
                </div>
                <div>
                    <textarea name="mallIntroducation" id="" cols="30" rows="10" maxlength="250" class="set-textarea mallIntroducation"
                              placeholder="商城简介">${mallSet.mallIntroducation }</textarea>
                </div>
            </div>
        </section>
    </c:if>
    <input type="hidden" class="memberId" value="${member.id }"/>
    <c:if test="${type == 2 }">
        <section class="set-body" id="setting-2">
            <div class="self-setting">
                <input type="hidden" class="mallSetId" value="${mallSet.id }"/>
                <input type="hidden" id="findIds" value="${findIds }"/>
                <input type="hidden" class="is_open_optional" value="${is_open_optional }"/>
                <div class="setting-tr">
                    <label class="set-label">开启自选</label>
                    <span class="self-switch <c:if test="${is_open_optional == 1 }">self-on</c:if>" onclick="isOpenOptional(this);"></span>
                </div>
                <div class="setting-tr">
                    <p class="set-text">开启自选后，您的商城里只显示您选择的产品</p>
                </div>
                <div class="selfOpen" style="<c:if test="${is_open_optional == 1 }">display: block;</c:if>">
                    <div class="setting-tr-l">
                        <label class="set-label-l">添加商品</label>
                        <a href="javascript:void(0);" class="set-file-full" onclick="addPro();"></a>
                    </div>
                    <ul class="setting-items">
                        <c:if test="${!empty selectProList }">
                            <c:forEach var="selectPro" items="${selectProList }">
                                <li class="df df-sb selectProLi" id="${selectPro.id }" sId="${selectPro.shop_id }"><!-- joinProductId="${selectPro.joinProductId }"  -->
                                    <div class="set-item-bg" style="background: url('${http}${selectPro.image_url}');background-size: contain;background-repeat: no-repeat;"></div>
                                    <div class="set-item-desc">
                                        <p>${selectPro.pro_name }</p>
                                        <p>价格： &yen;
                                            <c:if test="${selectPro.is_specifica == 0 || empty selectPro.inv_price}">${selectPro.pro_price }</c:if>
                                            <c:if test="${selectPro.is_specifica == 1 && !empty selectPro.inv_price}">${selectPro.inv_price }</c:if>
                                        </p>
                                    </div>
                                    <div class="item-del-bg as" onclick="deleLayer(this,0,'${selectPro.id }')"></div>
                                </li>
                            </c:forEach>
                        </c:if>
                        <c:if test="${!empty sellerProList }">
                            <c:forEach var="selectPro" items="${sellerProList }">
                                <li class="df df-sb selectProLi" id="${selectPro.id }" sId="${selectPro.shop_id }" joinProductId=${selectPro.sellerProductId }>
                                    <div class="set-item-bg" style="background: url('${http}${selectPro.image_url}');background-size: contain;background-repeat: no-repeat;"></div>
                                    <div class="set-item-desc">
                                        <p>${selectPro.pro_name }</p>
                                        <p>价格： &yen;
                                            <c:if test="${selectPro.is_specifica == 0 || empty selectPro.inv_price}">${selectPro.pro_price }</c:if>
                                            <c:if test="${selectPro.is_specifica == 1 && !empty selectPro.inv_price}">${selectPro.inv_price }</c:if>
                                        </p>
                                    </div>
                                    <div class="item-del-bg as" onclick="deleLayer(this,${selectPro.sellerProductId},'${selectPro.id }')"></div>
                                </li>
                            </c:forEach>
                        </c:if>
                    </ul>
                </div>
            </div>
        </section>
    </c:if>
</div>
<div class="setting-bottom">
    <a href="javascript:void(0)" class="full-blue-btn" onclick="editSellerSet(${type});">确认</a>
</div>
<input type="hidden" class="delId" value=""/>

<form action="" id="formUrl" method="post">
    <input type="hidden" name="findIds" value="${findIds }"/>
    <input type="hidden" name="uId" value="${userid }"/>
</form>
<input type="hidden" class="userid" value="${userid }"/>

<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerMallSet.js"></script>
<script type="text/javascript" src="/js/plugin/gt/js/gt_common.js"></script>
<script type="text/javascript">

    wx.config({
        debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: "${record.get('appid')}", // 必填，公众号的唯一标识
        timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
        nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
        signature: "${record.get('signature')}",// 必填，签名，见附录1
        jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    wx.ready(function () {
        wx.hideOptionMenu();
    });

    /* function showNav(obj) {
     var index = $(obj).index() + 1;
     $("section#setting-"+index).show().siblings("section").hide();
     $(obj).addClass("nav-active").siblings().removeClass("nav-active");
     } */
    function showNav(types, isNext) {
        if (types == 2 && isNext == 0) {
            gtcom.dialog("请先完善基本信息", "a")
            return;
        }
        location.href = "/phoneSellers/79B4DE7C/toMallSet.do?type=" + types + "&uId=${userid}";
        //location.href = "/phoneSellers/79B4DE7C/clientOrder.do?status="+status;
    }
    function isOpen(obj) {
        $(obj).toggleClass("self-on");
        if ($(obj).hasClass("self-on")) {
            $(".selfOpen").fadeIn();
        } else {
            $(".selfOpen").fadeOut();
        }
    }
    function deleLayer(obj, id, delId) {
        //var id = $(obj).parents("li").attr("id");
        if (id > 0) {
            $(".delId").val(id);
            gtcom.dialog("是否删除该商品，删除商品后得重新添加商品", "b", "delItem()");
        } else {
            $(obj).parents("li").remove();
            //location.href = window.location.href;
            //$("#formUrl").attr("action",window.location.href);
            //$("#formUrl").submit();
        }
        if (findIdObj != null && delId != "0") {
            if (findIdObj[delId] != null && findIdObj[delId] != "") {
                delete findIdObj[delId];
            }
        }
    }
    var pObj;
    function showLayer(obj) {
        pObj = $(obj).parents("li");
        $(".cd-layer").show();
        $("body").bind("touchmove", function (e) {
            e.preventDefault();
        }, false);
    }
    function closeLayer(obj) {
        $(obj).parents(".cd-layer").hide();
        $("body").unbind("touchmove");
    }
    function delItem() {
        //$(".selectProLi#"+id).remove();
        //var id = $(".selectProLi#"+id).attr("sellerId");
        var id = $(".delId").val();

        layer.open({type: 2});
        $.ajax({
            type: "post",
            url: "/phoneSellers/79B4DE7C/deleteMallPro.do",
            data: {
                id: id
            },
            dataType: "json",
            success: function (data) {
                layer.closeAll();
                if (data.flag) {
                    location.href = window.location.href;
                    //$("#formUrl").attr("action",window.location.href);
                    //$("#formUrl").submit();
                } else {// 编辑失败
                    gtcom.dialog("删除已选商品失败，请稍后重试", "a");
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.closeAll();
                gtcom.dialog(msg + "失败，请稍后重试", "a");
                return;
            }
        });
    }

    function addPro() {
        var urls = "/phoneSellers/79B4DE7C/findProduct.do?uId=${userid}";
        var findIds = "";
        if (findIdObj != null) {
            for (var i in findIdObj) {
                if (findIds != "") {
                    findIds += ",";
                }
                findIds += i;
            }
            if (findIds != "") {
                urls += "&findIds=" + findIds;
            }
        }
        location.href = urls;
    }


</script>
</body>
</html>