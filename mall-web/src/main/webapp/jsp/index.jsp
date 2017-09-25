<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>

<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<body>

<script type="text/javascript">
    $.ajax({
        url: "http://mall.yifriend.net:8080/mall/count",
        type: "POST",
        dataType: "json",
        success: function (data) {
            console.log(data);
        }, error: function () {
        }
    });

</script>

<br/>

<br/>
</body>
</html>
