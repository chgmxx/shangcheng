<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";

    %>

    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>商品分组弹出框</title>

    <link rel="stylesheet" type="text/css" href="/css/mall/groupPopUp.css"/>
    <style type="text/css">
        .shopDiv {
            width: 300px;
        }

        select {
            width: 200px !important;
        }

        .area-btn {
            margin-top: 20px;
        }

        .area-modal {
            margin-top: 20px;
        }
    </style>
</head>
<body>
<div class="area-modal">
    <!--  <div class="area-head">选择可配送区域</div> -->
    <!--   <div class="area-main"> -->
    <div class="shopDiv">
        <c:if test="${!empty shoplist }">
            来源店铺:
            <select class="shopSelect shopSel">
                <c:forEach var="shop" items="${shoplist }">
                    <option
                            <c:if test="${shopId == shop.id }">selected="selected"</c:if> id="${shop.id }">${shop.sto_name }</option>
                </c:forEach>
            </select>
        </c:if>
    </div>
    <div class="shopDiv">
        <c:if test="${!empty shoplist }">
            目标店铺:
            <select class="shopSelect toShopSel">
                <c:forEach var="shop" items="${shoplist }">
                    <option
                            <c:if test="${shopId == shop.id }">selected="selected"</c:if> id="${shop.id }">${shop.sto_name }</option>
                </c:forEach>
            </select>
        </c:if>
    </div>
    <div class="area-btn">
        <button class="area-confirm" onclick="subtmit();">确认同步</button>
        <button class="area-close" onclick="cancel();">取消同步</button>
    </div>
</div>
<input type="hidden" class="shopId" value="${shopId }"/>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
</body>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/layer/layer.js"></script>

<script type="text/javascript">
    function cancel() {
        parent.layer.closeAll();
    }
    function subtmit() {
        var array = new Array();
        var shopId = $(".shopSel option:selected").attr("id");
        var toShopId = $(".toShopSel option:selected").attr("id");
        if (shopId == null || shopId == "") {
            layer.alert("请选择来源店铺", {
                shade: [0.1, "#fff"],
                offset: "10%"
            });
            return false;
        }
        if (toShopId == null || toShopId.length == 0) {
            layer.alert("请选择目标店铺", {
                shade: [0.1, "#fff"],
                offset: "10%"
            });
            return false;
        }
        if (shopId == toShopId) {
            layer.alert("来源店铺和目标店铺不能相同！", {
                shade: [0.1, "#fff"],
                offset: "10%"
            });
            return false;
        }
        var data = {
            shopId: shopId,
            toShopId: toShopId
        };
        var layerLoad = layer.load(1, {
            shade: [0.3, '#fff'],
            offset: "10%"
        });
        $.ajax({
            type: "post",
            data: data,
            url: "mPro/copy_pro.do",
            dataType: "json",
            timeout: 60000 * 30,//半小时的超时时间
            success: function (data) {
                layer.close(layerLoad);
                //layer.closeAll();
                SonScrollTop(0);
                setTimeout(function () {
                    if (data.code == 0) {// 重新登录
                        layer.alert("操作失败，长时间没操作，跳转到登录页面", {
                            offset: scrollHeight + "px",
                            shade: [0.1, "#fff"],
                            closeBtn: 0
                        }, function (index) {
                            location.href = "/user/tologin.do";
                        });
                    } else if (data.code == 1) {
                        var tipLayer = layer.alert("同步商品成功", {
                            offset: scrollHeight + "px",
                            shade: [0.1, "#fff"],
                            closeBtn: 0
                        }, function (index) {
                            parent.location.href = window.parent.location.href;
                        });
                    } else {// 编辑失败
                        layer.alert("同步商品失败", {
                            shade: [0.1, "#fff"],
                            offset: scrollHeight + "px"
                        });
                    }
                }, timeout);
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.close(layerLoad);
                layer.alert("同步商品失败", {
                    shade: [0.1, "#fff"],
                    offset: "10%"
                });
                return;
            }
        });
        //parent.returnVal(array);
    }
</script>
</html>