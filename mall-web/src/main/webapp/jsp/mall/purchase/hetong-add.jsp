<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/kindeditor/themes/simple/simple.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/kindeditor/kindeditor-min.js" type="text/javascript" charset="utf-8"></script>
    <title>${title}</title>

    <script type="text/javascript">
        var editor;
        KindEditor.ready(function (K) {
            editor = K.create('textarea[name="contractContent"]',
                {
                    width: '462px',
                    height: '388px',
                    minWidth: '150px',
                    resizeType: 0,
                    filterMode: false,
                    items: [
                        'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
                        'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
                        'insertunorderedlist', '|', 'emoticons', 'image', 'link', 'media'],
                    allowPreviewEmoticons: true,
                    allowImageUpload: true,
                    allowFileManager: true,
                    allowFileUpload: true,
                    uploadJson: '/common/upload.do',
                    fileManagerJson: '../linkurl/fileManagerJson',
                    formatUploadUrl: false,
                    wellFormatMode: true,
                    afterBlur: function () {
                        this.sync();
                    }
                });
        });

        function save() {
            var contractTitle = $("input[name='contractTitle']").val();
            var contractContent = $('textarea[name="contractContent"]').val();

            if ($.trim(contractTitle) == "") {
                parentAlertMsg("合同标题不可以为空!");
//                window.parent.alertMsg("合同标题不可以为空!")
                return;
            }
            if ($.trim(contractContent) == "") {
                parentAlertMsg("合同内容不可以为空!");
//                window.parent.alertMsg("合同内容不可以为空!")
                return;
            }
            $.ajax({
                url: "/purchaseContract/saveContract.do",
                data: $('#contractForm').serialize(),
                type: "POST",
                dataType: "JSON",
                success: function (data) {
                    if (data.result == true || data.result == "true") {
                        parentAlertMsg("合同保存成功!")
//                        window.parent.alertMsg("合同保存成功!")
                        location.href = "/purchaseContract/contractIndex.do";
                    } else {
                        parentAlertMsg("合同保存失败!")
//                        window.parent.alertMsg("合同保存失败!")
                    }
                }
            });

        }
    </script>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="warp">
    <div class="gt-bread-crumb">
        <span class="gt-bread-crumb-title">${title}</span>
    </div>
    <form id="contractForm">
        <div class="add-form">
            <div class="gt-form-row">
                <div class="gt-form-left hetong-add-list">合同标题：</div>
                <input type="hidden" name="id" value="${contract.id}">
                <div class="gt-form-right">
                    <input type="text" name="contractTitle" class="gt-form-input middle add-form-input" maxlength="15" value="${contract.contractTitle}">
                </div>
            </div>
            <div class="gt-form-row">
                <div class="gt-form-left hetong-add-list">合同内容：</div>
                <div class="gt-form-right ver-top">
                    <textarea name="contractContent" rows="" cols="">${contract.contractContent}</textarea>
                </div>
            </div>
        </div>
        <div class="text-center mar-t20">
            <input type="button" class="gt-btn blue middle mar-r20" value="保存" onclick="save()"/>
            <input type="button" class="gt-btn default middle" onclick="javascript:history.go(-1)" value="返回"/>
        </div>
    </form>
</div>
</body>
</html>
