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
    <title>订单弹出框</title>

    <link rel="stylesheet" type="text/css" href="/css/mall/order.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>
</head>
<body>
<div class="returnLayer">
    <form id="returnForm">
        <div class="r_div">
            <span class="tip">退款金额：</span>
            <span class="tip_right r_money">¥ ${deposit.depositMoney }</span>
        </div>
        <div class="modal-footer clearfix" style="position:relative;display:inline-block;">
            <c:if test="${deposit.payWay == 3 }">
                <div style="color:red;margin-left: 38px">*建议您复制退款链接并用IE浏览器打开进行退款</div>
                <a href="${http }/alipay/79B4DE7C/refund.do?out_trade_no=${deposit.depositNo }&busId=${busUserId}&desc=退保证金" target="_blank"
                   class="ui-btn ui-btn-primary" style="padding:5px 10px;border-radius:2px;margin-left: 38px">退保证金 </a>
                <a href="javascript:void(0);" class="ui-btn ui-btn-primary copy_a copy_public"
                   data-clipboard-text="${http }/alipay/79B4DE7C/refund.do?out_trade_no=${deposit.depositNo }&busId=${busUserId}&desc=退保证金" aria-label="复制成功！"
                   style="padding:5px 10px;border-radius:2px;margin-left: 10px">复制退款链接</a>
            </c:if>
            <c:if test="${deposit.payWay != 3 }">
                <a href="javascript:;" class="ui-btn ui-btn-primary" id="submit"
                   style="padding:5px 10px;border-radius:2px;margin-left: 38px">退保证金</a>
            </c:if>
        </div>
    </form>

    <input type="hidden" class="id" value="${deposit.id }"/>
</div>
</body>
<script type="text/javascript">
    $("#submit").click(function () {
        var layerLoad = parent.layer.load(1, {
            shade: [0.3, '#000'],
            offset: "30%"
        });
        var id = $(".id").val();
        $.ajax({
            type: "post",
            url: "/mPresale/agreedReturnDeposit.do",
            data: {
                depositId: id
            },
            dataType: "json",
            success: function (data) {
                parent.layer.close(layerLoad);
                if (data.result == true) {
                    parent.location.href = "/mPresale/to_deposit.do";
                } else {// 编辑失败
                    if (data.msg == null || data.msg == "") {
                        alert("退定金失败，请稍后重试");
                    } else {
                        alert(data.msg);
                    }
                }

            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                parent.layer.close(layerLoad);
                alert("退定金金失败，请稍后重试");
                return;
            }
        });
    });
</script>
</html>