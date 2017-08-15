<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>

</head>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<body>
<div>
    <div>
        <input type="hidden" name="parentId" value="${languageId}">
        <input type="hidden" name="orderId" value="${orderId}">
        <textarea rows="6" cols="50" style="width: 260px;" placeholder="请填写回复内容" id="languageContent" name="languageContent"></textarea>
        <input type="button" value="回复" onclick="replyLanguage()" class="blue-btn" style="margin-left: 105px;margin-top: 5px"/>
    </div>
</div>
<script>
    function replyLanguage() {
        $.ajax({
            url: "/purchaseOrder/replyLanguage.do",
            data: {"languageId": "${languageId}", "orderId": "${orderId}", "languageContent": $("#languageContent").val()},
            type: "POST",
            dataType: "JSON",
            success: function (data) {
                if (data == true || data == "true") {
                    top.frames[0].location.reload();
                    //TODO parent.layer.getFrameIndex
                    var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                    parentCloseAll();
//                    parent.layer.close(index);
                } else {
                    alert("回复失败!");
                }
            }
        });
    }
</script>
</body>
</html>