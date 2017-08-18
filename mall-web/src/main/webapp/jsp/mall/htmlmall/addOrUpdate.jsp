<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>页面管理</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css?<%= System.currentTimeMillis()%>"/>
    <script charset="utf-8" type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="/js/plugin/jquery-form.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="/js/public.js?<%= System.currentTimeMillis()%>"></script>
</head>
<body>
<div class="fansTitle">
    <span class="i-con fl"></span><span class="title-p">${pageTitle}</span>
</div>
<form action="" method="post" id="textmsg">
    <input type="hidden" value="${id }" name="id" id="id"/>
    <table class="table2" id="tab">

        <tr>
            <td class="table-td1">页面名称：</td>
            <td class="table-td2"><input type="text" class="text1 abc"
                                         id="htmlname" name="htmlname" placeholder="请输入页面名称,不能超过20字"
                                         maxlength="20" required="required" value="${map.htmlname }"/></td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>
        <tr>
            <td class="table-td1">页面介绍：</td>
            <td class="table-td2"><input type="text" class="text1 abc"
                                         id="introduce" name="introduce" placeholder="用于微信分享朋友圈"
                                         maxlength="20" value="${map.introduce }" maxlength=25/></td>
            <td></td>
        </tr>
        <!-- <tr>
            <td class="table-td1">是否开启：</td>
            <td class="table-td2"><input type="checkbox" id="isstate"
                 checked></td>
            <td></td>
        </tr> -->
    </table>
</form>
<div class="btn_box_div" style="margin-top: 30px;">
    <div class="btn_save_div">
        <input type="button" id="save" value="保存" class="blue-btn"
               onclick="save()"/>
    </div>
    <div class="btn_cancel_div"
         style="background-color: #8cc717; border-radius: 3px; font-size: 14px;">
        <a href="/mallhtml/htmllist.do" style="cursor: pointer; color: white;">返回</a>
    </div>
</div>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
</body>
<script type="text/javascript">
    $(function () {
        var id = '${id}';
        if (id != '' && id != null || id != undefined) {
            var state = '${map.state}';
            if (state == 1) {
                $("#state").val(1);
                $('#isstate').removeAttr("checked");
            }
        }
    });
    //保存
    function save() {
        var htmlname = $("#htmlname").val();
        if (htmlname == null || htmlname == "" || htmlname == undefined) {
            $("#picCodePrompt").html("商城名称不能为空");
            return false;
        }

        showAll();//加载
        $("#textmsg").ajaxSubmit({
            type: "post",
            dataType: "json",
            url: "/mallhtml/addorUpdateSave.do",
            success: function (data) {
                closeWindow(); //加载完毕
                if (data.error == "0") {
                    layer.alert("操作成功", {
                        offset: "30%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                    window.location.href = "/mallhtml/htmllist.do";
                } else {
                    layer.alert(data.message, {
                        offset: "30%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                }
            }
        });
    }
</script>
</html>
