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
    <meta charset="utf-8">
    <meta content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=no" name="viewport">
    <meta content="yes" name="apple-mobile-web-app-capable">
    <meta content="black" name="apple-mobile-web-app-status-bar-style">
    <meta content="telephone=no" name="format-detection">
    <meta content="email=no" name="format-detection">

    <script src="/js/mall/integral/camel.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <link rel="stylesheet" href="/css/mall/integral/swiper-3.3.1.min.css">
    <link rel="stylesheet" href="/css/mall/integral/global.css">
    <link rel="stylesheet" href="/css/mall/integral/public.css">
    <script src="/js/mall/integral/swiper-3.3.1.min.js"></script>
    <script src="/js/mall/integral/iconfont.js"></script>
    <script src="/js/mall/integral/public.js"></script>

    <script src="/js/plugin/layer-mobile/layer/layer.js"></script>
    <link rel="stylesheet" type="text/css" href="/js/plugin/layer-mobile/layer/need/layer.css"/>

    <title>我的地址</title>
    <style>
        .line-list--flex .line-item .bd-txt {
            line-height: 1.5;
            color: #333;
            font-size: 0.4rem;
        }

        .line-list--flex .line-item .bd-txt {
            line-height: 1.5;
            color: #333;
            font-size: 0.4rem;
            margin-bottom: 5px;
        }

        .layermbox2 .layermcont i {
            width: 15px !important;
            height: 15px !important;
        }
    </style>
</head>
<body>
<div>
    <ul class="line-list line-list--indent line-list--flex my-address">
        <c:if test="${!empty addressList }">
            <c:forEach var="address" items="${addressList }">
                <li class="line-item">
                    <c:set var="cla" value=""></c:set>
                    <c:if test="${address.memDefault == 1 }">
                        <c:set var="cla" value="on-check"></c:set>
                    </c:if>
                    <div class="item-gou ${cla } isChecks" id="${address.id }">
                        <svg class="icon" aria-hidden="true">
                            <use xlink:href="/phoneIntegral/79B4DE7C/addressList.do?uId=${userid }&shopId=${shopId }#icon-duigou"></use>
                        </svg>
                    </div>
                    <div class="item-bd">
                        <p class="bd-txt ">
		                    <span>
		                        ${address.provincename }${address.cityname }${address.areaname }
		    					<c:if test="${address.memZipCode != null && address.memZipCode != ''}">（${address.memZipCode}）</c:if>
		                    </span>
                        </p>
                        <div class="bd-tt flex flex-pack-justify">
		                       <span>
		                        ${address.memName }　${address.memPhone }
		                      </span>
                            <div class="clear">
                                <!-- <p class="  fr operation-wrap flex flex-align-center">
                                    <img src="/images/mall/integral/dele.png" alt="">
                                    <span>删除</span>
                                </p> -->
                                <p class="  fr operation-wrap flex flex-align-center" onclick="toEdit(${address.id },${userid });">
                                    <img src="/images/mall/integral/edit.png" alt="">
                                    <span>编辑</span>
                                </p>

                            </div>
                        </div>
                    </div>
                </li>
            </c:forEach>
        </c:if>
        <li class="line-item" onclick="toEdit(0,${userid });">
            <div class="item-gou">
                <img src="/images/mall/integral/add.png" alt="">
            </div>
            <div class="item-bd">
                <p class="bd-txt ">
                    <span> 新增收货地址</span>
                </p>
            </div>
        </li>
    </ul>
</div>

<section class="dialog-wrap register-box active vali_section" style="display: none;">
    <div class="overlay"></div>
    <div class="dialog">
        <div class="dialog-bd">
            <h3 class="bd-tt">立即兑换</h3>
            <p class="bd-txt txt--left errorMsg">
                您确认要立即兑换商品
            </p>
        </div>
        <footer class="dialog-ft">
            <span class="ft-btn cancel">取消</span>
            <span class="ft-btn register-btn-ok" onclick="integralChange(1);">立即兑换</span>
        </footer>
    </div>
</section>

<section class="dialog-wrap register-box active failedSection" style="display: none;">
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

<c:if test="${!empty orders }">
    <c:forEach var="i" items="${orders }">
        <input type="hidden" name="${i.key }" class="formCla" value="${i.value }"/>
    </c:forEach>
</c:if>
<div class="integral-bottom integral-address">
    <div class="integral-bottom-btn" id="save" onclick="integralChange(0);">立即兑换</div>
</div>
<input type="hidden" class="shopId" value="${shopId }"/>
<input type="hidden" class="userid" value="${userid }"/>

<script src="/js/mall/integral/phone/integral_product.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>

<form action="/phoneIntegral/79B4DE7C/toAddress.do?uId=${userid }&shopId=${shopId }" id="toAddList" method="post">
    <input type="hidden" class="orders" name="orders"/>
</form>

<script type="text/javascript">
    function toEdit(addressId, userid) {
        /* var shopId = $(".shopId").val();
         var url = "/phoneIntegral/79B4DE7C/toAddress.do?uId="+userid+"&shopId="+shopId;
         if(addressId != null && addressId != "" && addressId != "0" && typeof(addressId) != "undefined"){
         url += "&id="+addressId;
         }
         location.href = url; */
        var json = getJson();
        $(".orders").val(JSON.stringify(json));
        if (addressId != null && addressId != "" && addressId != "0" && typeof(addressId) != "undefined") {
            var actions = $('#toAddList').attr("action");
            $('#toAddList').attr("action", actions + "&id=" + addressId)
        }
        $('#toAddList').submit();
    }
    $(".item-gou").click(function () {
        var check = $(this).attr("id");
        var isChecks = $(".isChecks.on-check").attr("id");
        if (check == isChecks) {
            $(this).addClass("on-check");
            return false;
        }
        if (!$(this).hasClass("on-check")) {//选中
            $(".item-gou").removeClass("on-check");
        }
        $(this).toggleClass('on-check')
    });
    function getJson() {
        var json = {};
        $(".formCla").each(function () {
            json[$(this).attr("name")] = $(this).val();
        });
        return json;
    }
    /**
     * 立即兑换
     */
    function integralChange(types) {
        var json = getJson();
        var isChecks = $(".isChecks.on-check").attr("id");
        if (isChecks == null || isChecks == "" || typeof(isChecks) == "undefined") {
            showDialog(1, "您还未选择收货地址")
            return false;
        }
        json["receiveId"] = isChecks;
        json["uId"] = $(".userid").val();
        if (types == 0) {
            showDialog(0, "您确认要立即兑换商品");
            return false;
        } else {
            $(".vali_section").hide();
        }
        console.log(json);
        ajaxSubmit(json, 0);
    }
    function showDialog(isHide, errorMsg) {
        $(".vali_section").show();
        if (isHide == 1) {
            $(".vali_section .register-btn-ok").hide();
        } else {
            $(".vali_section .register-btn-ok").show();
        }
        if (errorMsg != null && errorMsg != "") {
            $(".vali_section .errorMsg").html(errorMsg);
        }
    }
</script>
</body>
</html>