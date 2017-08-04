<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

    %>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
    <title>城市弹出框</title>
    <link rel="stylesheet" type="text/css" href="/css/mall/order.css"/>
    <script src="/js/plugin/jquery-1.8.0.min.js"></script>
    <script src="/js/plugin/jquery-ui.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <style>
        span {
            display: inline-block;
            margin: 5px 0;
        }

    </style>
</head>
<body>
<input type="hidden" class="index" value="${index }"/>
<input type="hidden" class="selectPro" value="${selectPro }"/>
<input type="hidden" class="hidePro" value="${hidePro }"/>
<div class="modal" style="width:400px;padding:0px 20px;">
    <c:if test="${!empty provinceList }">
        <c:forEach var="province" items="${provinceList }">
			<span class="">
				<input type="checkbox" class="province" value="${province.id }"
                       tip="${province.city_name }"/>${province.city_name }
			</span>
        </c:forEach>
    </c:if>
    <div><a id="subRemark" class="ui-btn ui-btn-primary" style="width: 120px;">确认</a></div>
</div>
<script type="text/javascript">
    function onload() {
        var selectPro = $(".selectPro").val();
        var hidePro = $(".hidePro").val();

        $("input.province").each(function () {
            var name = $(this).attr("tip");

            if (selectPro.indexOf(name) > 0) {
                $(this).attr("checked", "checked");
            }
            if (hidePro.indexOf(name) > 0) {
                $(this).parent().hide();
            } else {
                $(this).parent().show();
            }
        });
    }

    $(function () {
        onload();

        $('#subRemark').click(function () {
            var orderId = $('#orderId').val();
            var orderMoney = $('#orderMoney').val();
            var index = $(".index").val();
            var id = "";
            var province = "";
            $("input.province").each(function () {
                if ($(this).is(":checked")) {
                    if (id != "") {
                        id += ",";
                        province += ",";
                    }
                    id += $(this).val();
                    province += $(this).attr("tip");
                }
            });
            parent.getProvinces(id, province, index);

            parent.layer.closeAll();
        });

        //取消按钮
        $('#cancelOrder').click(function () {
            parent.layer.closeAll();
        });
    });
</script>
</body>
</html>