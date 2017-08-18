<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">

<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

        String setType = request.getParameter( "setType" );
        request.setAttribute( "setType", setType );
    %>
    <base href="<%=basePath%>"/>
    <meta charset="utf-8"/>
    <meta name="renderer" content="webkit"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <title>公共微场景预览</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <script src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript">
        //iframe直接输入URL情况下，跳转到对应的URL中

        function checkupdate(id) {
            $("#buttoncl").attr("disabled", "disabled");
             var index = layer.load(1, {
                 shade: [0.1, '#fff']
                 // 0.1透明度的白色背景
             });
            $.ajax({
                type: "post",
                url: "mallhtml/SetmallHtml.do",
                data: {id: id},
                dataType: "json",
                success: function (data) {
                    layer.close(index);
                    var error = data.error;
                    if (error == 0) {
                        hdhtml(data.xid);

                    } else if (error == 2) {
                        var ispid = data.ispid;
                        if (ispid == 0) {
                            layer.confirm("等级不够，不能在创建h5商城，请前往<a href='/trading/upGrade.do?setType=trading' style='text-decoration: none;color:red'>续费升级级别</a>", { shade:[0.1,'#fff'],offset: '25%'}, function () {
                                top.location.href = "/trading/upGrade.do?setType=trading";
                            })
                        } else {
                            layer.alert("主账户等级不足，不能在创建h5商城", {
                                offset: "30%",
                                shade:[0.1,"#fff"],
                                closeBtn: 0
                            });
                        }
                    } else {
                        layer.alert("操作失败，数据异常，请联系管理员", {
                            offset: "30%",
                            shade:[0.1,"#fff"],
                            closeBtn: 0
                        });
                    }
                }
            });
        }
        function closeWindow() {
            layer.closeAll();
        }
    </script>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div style="text-align: center">
    <img id="img" style="width: 200px;height:200px; margin: 0 auto;vertical-align: middle;" alt="扫一扫，查看效果" src="${image}${map.codeUrl}">

</div>
<div style="text-align: center">
    <font style="color:red;font-size:12px">提示：低版本浏览器不兼容h5商城效果，建议使用谷歌，火狐，百度等浏览器：</font>
</div>
<div style="text-align: center;width: 100%;margin-top: 20px;">
    <input type="button" style="border: none;padding: 0px 12px;background: #1aa1e7;color: #fff;font-size: 1.1em;cursor: pointer; height: 28px;line-height: 28px;" value="返回"
           onclick="closeWindow()">
    <input type="button" id="buttoncl" style="border: none;padding: 0px 12px;background: #1aa1e7;color: #fff;font-size: 1.1em;cursor: pointer; height: 28px;line-height: 28px;"
           value="选取" onclick="checkupdate('${map.id}')">
</div>
</body>
</html>