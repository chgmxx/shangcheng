<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE>
<html>
<base href="<%=basePath%>" />
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
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
</head>
<body>
<div class="m-layer" style="display: none;"></div>
<div class="srh-result" style="display: none;">
	<div class="srh-init">
		<c:if test="${!empty labelList }">
		<div class="srh-rec">
			<p class="srh-type">搜索推荐</p>
			<ul class="srhUl flex-row-sc wrap">
				<c:forEach var="label" items="${labelList }">
				<li>
					<a onclick="searchProduct('${label.group_name }');" href="javascript:void(0);">${label.group_name }</a>
				</li>
				</c:forEach>
			</ul>
		</div>
		</c:if>
		<c:if test="${!empty keywordList }">
		<div class="srh-his historyDiv">
			<div class="srh-type flex-row-bt">
				<span>历史搜索</span>
				<i class="dus-icon" onclick="isDust(this)"></i>
			</div>
			<ul class="srhUl flex-row-sc wrap">
				<c:forEach var="keyword" items="${keywordList }">
				<li>
					<a onclick="searchProduct('${keyword.keyword  }');" href="javascript:void(0);">${keyword.keyword }</a>
				</li>
				</c:forEach>
			</ul>
		</div>
		</c:if>
	</div>
	<!--搜索结果-->
	<div class="srh-res" style="display: none;">
		<ul class="srhUl flex-row-sc wrap">
			<!-- <li>
				<a href="javascript:void(0)">女子秋装</a>
			</li> -->
		</ul>
	</div>
</div>
</body>
</html>