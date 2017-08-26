<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script src="js/jquery-1.8.3.min.js"></script>
    <title>404</title>
    <script type="text/javascript">
        $(function () {
            var height = $(window).height();
            $('.bg').css('height', height);
        })
    </script>
    <style type="text/css">
        * {
            margin: 0;
            padding: 0;
        }

        body, html {
            width: 100%;
            height: 100%;
            position: relative;
            overflow: hidden;
        }

        .bg {
            background: #b0e2fb;

        }

        .one {
            position: absolute;
            width: 410px;
            height: 350px;
            left: 50%;
            top: 50%;
            margin-top: -175px;
            margin-left: -450px;
            background: url(/images/error/404-64.png);

        }

        .two {
            position: absolute;
            width: 586px;
            height: 181px;
            left: 50%;
            top: 50%;
            margin-top: -87px;
            margin-left: -50px;
            background: url(/images/error/404-68.png);
        }

    </style>
</head>
<body>
<div class="bg">
    <div class="one">

    </div>
    <div class="two">

    </div>
</div>
</body>
</html>