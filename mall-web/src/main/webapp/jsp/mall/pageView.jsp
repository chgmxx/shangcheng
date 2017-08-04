<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<title>分页</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" id="meta"/>
<meta name="apple-mobile-web-app-capable" content="yes"/>
<meta name="format-detection" content="telephone=no"/>
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="HandheldFriendly" content="true">
<meta name="MobileOptimized" content="320">
<meta name="screen-orientation" content="portrait">
<meta name="x5-orientation" content="portrait">
<meta name="full-screen" content="yes">
<meta name="x5-fullscreen" content="true">
<meta name="browsermode" content="application">
<meta name="x5-page-mode" content="app">
<meta name="msapplication-tap-highlight" content="no">
<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" /> 
</head>
<body>
<div class="box-btm30 clearfix">
	<div class="page">
		<ul>
			<li class="pgfocus"><a
				href="javascript:page(1,'${page.url }');">首页</a></li>
			<li class="pgfocus"><a
				href="javascript:page(${page.curPage-1},'${page.url }');">上一页</a></li>
			<li class="pgfocus current">第<c:if test="${page.curPage==0 }">${page.curPage+1 }</c:if>
				<c:if test="${page.curPage != 0 }">${page.curPage }</c:if>页
			</li>
			<li class="pgfocus"><a
				href="javascript:page(${page.curPage+1},'${page.url }');">下一页</a></li>
			<li class="pgfocus"><a
				href="javascript:page(${page.pageCount },'${page.url }');">尾页</a></li>
			<li class="pgfocus">共<c:if test="${page.pageCount==0 }">${page.pageCount+1 }</c:if>
				<c:if test="${page.pageCount != 0 }">${page.pageCount }</c:if>页
			</li>
		</ul>
	</div>
</div>

<script type="text/javascript">
function page(curPage,url) {
	var shopId = $('#shopId').val();
	var proName = $("#proName").val();
	url = url + curPage;
	//获取查询参数,queryForm为表单ID
	$("#queryForm .srh").each(function(){
		url += "&"+$(this).attr("name")+"="+$(this).val();
	});
	var shopType = $(".shopType option:selected").val();
	if(shopType != null && shopType != "" && typeof(shopType) != "undefined"){
		if(shopType != "0" && shopType != "-1"){
			url += "&shopId="+shopType;
		}
	}
	location.href = url;
}
</script>

</body>
</html>