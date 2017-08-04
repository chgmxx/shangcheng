<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" href="/css/mall/purchase/phone/index.css">
    <link rel="stylesheet" type="text/css" href="/css/weui-master/dist/style/weui.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="/js/mall/purchase/phone/index.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">

        function check() {
            var languageContent = $(".weui-textarea").val();
            if (languageContent == "") {
                alert("留言内容不可以未空!");
                return false;
            }
            return true;
        }

        function languageForm() {
            if (check()) {
                $.ajax({
                    url: '/purchasePhone/79B4DE7C/writeLanguage.do',
                    type: "POST",
                    data: new FormData($('#languageForm')[0]),
                    dataType: "JSON",
                    processData: false,
                    contentType: false,
                    cache: false,
                    success: function (data) {
                        if (data == "true" || data == true) {
                            alert("留言成功!");
                            location.href = "/purchasePhone/79B4DE7C/findOrder.do?orderId=${orderId}";
                        } else {
                            alert("留言失败,验证码错误!");
                        }
                    }
                });
            }
        }
    </script>
    <title>写留言</title>
</head>
<body>
<div class="warp">
    <form action="" method="post" id="languageForm">
        <h1 class="quotes_title">${orderTitle}</h1>
        <input type="hidden" name="orderId" value="${orderId}">
        <!--手机号和验证码为uc版   非uc版去掉即可-->
        <div class="weui-cells weui-cells_form">
            <div class="weui-cell">
                <div class="weui-cell__bd">
                    <textarea class="weui-textarea" name="languageContent" placeholder="请输入留言" rows="4"></textarea>
                    <!--<div class="weui-textarea-counter"><span>0</span>/200</div>-->
                </div>
            </div>
        </div>
        <div class="weui-btn-area">
            <input type="button" value="提交" class="weui-btn quotes_btn_primary" onclick="languageForm()">
        </div>
    </form>
</div>
</body>
</html>