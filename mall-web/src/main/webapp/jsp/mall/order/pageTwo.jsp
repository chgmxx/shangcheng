<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title></title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script type="text/javascript">
function page(curPage,url) {
	//获取查询参数,queryForm为表单ID
	$(".curPage").val(curPage);
	$('#orderForm').submit();
}
</script>
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
</body>
</html>
