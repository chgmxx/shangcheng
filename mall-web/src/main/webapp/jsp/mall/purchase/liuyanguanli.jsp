<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
	    <meta http-equiv="Pragma" content="no-cache" />
	    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
	    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
	    <link rel="stylesheet" type="text/css" href="/css/trade/mall/purchase/index.css"/>
		<title>留言管理</title>
	</head>
	<body>
		<div class="warp">
			<div class="top-back"><a class="top-back-icon" href="#" onClick='javascript :history.back(-1);'></a></div>
			<div class="gt-bread-crumb">
		        <span class="gt-bread-crumb-title">留言管理</span>
		    </div>
		    <table class="gt-table">
		        <thead class="gt-table-thead">
		            <tr>
		              	<th>编号</th>
		                <th>用户头像</th>
		                <th>用户名称</th>
		                <th>操作</th>
		            </tr>
		        </thead>
		        <tbody class="gt-table-tbody">
		        <c:forEach items="${languageList}" var="language">
		            <tr>
		            	<td>${language.id}</td>
		                <td><img src="${language.headimgurl}" class="table-image-circle"/></td>
		                <td>${language.nickname}</td>
		                <td><i class="iconfont" title="留言详情" onclick="location.href='/purchaseOrder/languageDetails.do?orderId=${language.order_id}&memberId=${language.member_id}'">&#xe60c;</i></td>
		            </tr>
		            </c:forEach>
		        </tbody>
		    </table>
		</div>
	</body>
</html>
