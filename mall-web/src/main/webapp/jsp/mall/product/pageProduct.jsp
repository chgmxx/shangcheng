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
<script type="text/javascript" src="/js/plugin/layer/layer.js?<%= System.currentTimeMillis()%>"></script>
<script type="text/javascript">
	function page(curPage,url,type) {
		var curPagenew = "${page.curPage}";//已经是当前页
		/* if(type==2){
			if(curPage==curPagenew){
				layer.alert("已经是首页");
				return;
			}
		}
		if(curPage<1){
			layer.alert("已经是首页");
			return ;
		}
		var count = "${page.pageCount}";
		if(count==0){
			layer.alert("没有下一页");
		}else{
			if(curPage-1==count){
				layer.alert("已经是最后一页");
				return;
			}
			if(type==0){
				if(curPagenew==count){
					layer.alert("已经是最后一页");
					return;
				}
			}
		} */
		url = url + curPage;
		//获取查询参数,queryForm为表单ID
		var params = {};
		$("#queryForm").find(".srh").find("input").each(function(index){
			if($(this).attr("name") != undefined){
				params[$(this).attr("name")] = $(this).val();	
			}
		});
		$("#queryForm").find(".srh").find("select").each(function(index){
			if($(this).attr("name") != undefined){
				params[$(this).attr("name")] = $(this).val();	
			}
		});
		for(var o in params){
			url += "&"+o+"="+params[o];
		}
		
		
		var proName = $("input.srh").val();
		var type = $(".proChange option:selected").val();
		if(type != null && type != ""){
			if (type == 1) {// 出售中的商品
				url += "&isPublish=1&checkStatus=1&proType=" + type;
			} else if (type == 2) {// 已售罄商品
				url += "&type=1&proType=" + type;
			} else if (type == 3) {// 仓库中的商品
				url += "&isPublish=-1&proType=" + type;
			} else if (type == 4) {// 待审核的商品
				url += "&checkStatus=-2&proType=" + type;
			} else if (type == 5) {// 审核中的商品
				url += "&checkStatus=0&proType=" + type;
			}
		}
		if (proName != null && proName != "") {
			url += "&proName=" + proName;
		}
		var pId = $("#pId").val();
		if(pId != null && pId != ""){
			url += "&pId=" + pId;
		}
		location.href = url;
	}
</script>
</head>

<body>
	<div class="box-btm30 clearfix">
		<div class="page">
			<div class="fl right-10" id="div-allchoose">
				<input type="checkbox" class="allCheck" name="check" id="" value="" onclick="showMore(this)"/>
				<a href="javascript:;" class="ui-btn js-batch-checkAll">全选</a>
			 
			    <!-- <a href="javascript:;" class="ui-btn js-batch-tag" >改分组</a> -->
			    <a href="javascript:;" class="ui-btn js-batch-valid" >送审</a>
			    <c:if test="${proType ==1 || proType ==2 || proType == 0}">
			    <a href="javascript:;" class="ui-btn js-batch-unload">下架</a>
			    </c:if>
			    <c:if test="${proType == 3 || proType == 0}">
			    <a href="javascript:;" class="ui-btn js-batch-load">上架</a>
			    </c:if>
			    <a href="javascript:;" class="ui-btn js-batch-viewNum" >修改浏览量</a>
			    <a href="javascript:;" class="ui-btn js-batch-sale" >修改销量</a>
			    <a href="javascript:;" class="ui-btn js-batch-delete">删除</a>
			</div>
			<c:if test="${page.pageCount > 1}">
			<ul>
				<li class="pgfocus"><a
					href="javascript:page(1,'${page.url }',2);">首页</a></li>
				<c:if test="${page.curPage-1 > 0}">
				<li class="pgfocus"><a
					href="javascript:page(${page.curPage-1},'${page.url }',1);">上一页</a></li>
				</c:if>
				<c:if test="${page.curPage-1 == 0}">
				<li class="pgfocus">上一页</li>
				</c:if>
					
				<li class="pgfocus current">第<c:if test="${page.curPage==0 }">${page.curPage+1 }</c:if>
					<c:if test="${page.curPage != 0 }">${page.curPage }</c:if>页
				</li>
				
				<c:if test="${page.curPage+1 <=page.pageCount }">
					<li class="pgfocus"><a
					href="javascript:page(${page.curPage+1},'${page.url }',1);">下一页</a></li>
				</c:if>
				<c:if test="${page.curPage+1 >page.pageCount }">
					<li class="pgfocus">下一页</li>
				</c:if>
				<li class="pgfocus"><a
					href="javascript:page(${page.pageCount},'${page.url }',0);">尾页</a></li>
				<li class="pgfocus">共<c:if test="${page.pageCount==0 }">${page.pageCount+1 }</c:if>
					<c:if test="${page.pageCount != 0 }">${page.pageCount }</c:if>页
				</li>
			</ul>
			</c:if>
		</div>
	</div>
</body>
</html>
