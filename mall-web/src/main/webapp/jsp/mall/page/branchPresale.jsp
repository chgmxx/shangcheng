<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>选择预售商品</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css"
		href="/css/common/edit1.css?<%= System.currentTimeMillis()%>" />
	<link rel="stylesheet" type="text/css" href="/css/common/comm.css" />
	<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="/js/public.js"></script>
	<script type="text/javascript" src="/js/util.js"></script>
	<script type="text/javascript" src="/js/table.js"></script>

	<script type="text/javascript">
	
		$(function(){
			$("#a-delete").remove();
		});
		
		/**点击确认**/
		function fnOk(){
			var jsonArry=[];
			$("input[name='genre']").each(function(){
				if($(this).is(":checked")){
					var json = {};
					$(this).parents("li").find("input.saInp").each(function(){
						if($(this).attr("name") != undefined){
							json[$(this).attr("name")] = $(this).val();
						}
					});
					jsonArry.push(json);
				}
			});
			returnProVal(jsonArry,6,${check});//方法回调
			closeWindow();
		}
	
		
		function fnCancel(){
			closeWindow();
		}
		
		function closeWindow(){
			//当你在iframe页面关闭自身时
			var index = layer.getFrameIndex(window.name); //先得到当前iframe层的索引
			layer.close(index); //再执行关闭
		}
	</script>
  </head>
  
  <body style="margin: 10px">
  <jsp:include page="/jsp/common/headerCommon.jsp"/>
  <div style="padding-bottom: 50px">
    <div class="txt-btn pd-bottom-15 clearfix">
		<div>
			<form id="queryForm" method="post" action="mallPage/choosePro.do">
				<input type="hidden" name="stoId" value="${stoId }"/>
				<input type="hidden" name="check" value="${check}"/>
				商品名称：<input type="text" placeholder="请输商品名称(模糊匹配)" value="${proName }"
					id="proName" name="proName">
				 所属分类：
				<select style="width: 162px" id="groupId" name="groupId">
					<option value="">全部分类</option>
					<c:forEach items="${groLs }" var="gro">
						<option ${gro.group_id==groupId?'selected':'' } value="${gro.group_id }">${gro.group_name }</option>
					</c:forEach>
				</select> 
				<input type="submit" value="查询" style="width:50px;  cursor: pointer;background-color: #1aa1e7;border-radius: 3px;color: #fff;border: none;"/>
			</form>
		</div>
	</div>
	
	<div class="box-btm30">

		<div class="txt-tle">
			<div class="t1">&nbsp;</div>
			<div class="t2" style="width: 40%;">商品名称</div>
			<div class="t3" style="width: 40%;">状态</div>
		</div>

		<div class="msg-list">
			<div class="txt-tle2" id="list">
				<c:forEach items="${presaleList}" var="pro">
					<li>
						<div class="lista list-checkbox">
						    <c:if test = "${check eq 0 }">
							<input type="checkbox" id="checkbox-msg0" value="${pro.id}"
								name="genre" onclick="event.stopPropagation()">
							</c:if>
							<c:if test = "${check eq 1}">
							<input type="radio" id="checkbox-msg0" value="${pro.id}"
								name="genre" onclick="event.stopPropagation()">
							</c:if>
						</div>
						<div name="title" class="listb" style="width: 40%;">${pro.proName }</div>
						<div class="listb" style="width: 40%;">已审核</div>
						
						<input type="hidden" name="selecttype" class="listb saInp" value="6"><!--属性，1代表的是商品，2代表的是分类id 6代表的是预售商品  -->
						<input type="hidden" name="id" class="listb saInp" value="${pro.id }">
						<input type="hidden" name="proName" class="listb saInp" value="${pro.proName }">
						<%-- <div name="src" class="listb" style="display: none">${pro.image_url }http://192.168.2.103:8080/upload//image/2/gh_b0a77493e00a/3/20160330/26A892A1D0A9AED358DF4499EBBC99C5.jpg</div> --%>
						<input type="hidden" name="proImgUrl" class="listb saInp" value="${http}${pro.proImgUrl}">
						<input type="hidden" name="proPrice" class="listb saInp" value="${pro.proPrice}">
						<input type="hidden" name="status" class="listb saInp" value="${pro.status}">
						<input type="hidden" name="orderNum" class="listb saInp" value="${pro.orderNum}">
						<input type="hidden" name="times" class="listb saInp" value="${pro.times}">
						<input type="hidden" name="product_introdu" class="listb saInp" value="${pro.product_introdu}">
						<input type="hidden" name="preId" class="listb saInp" value="${pro.preId}">
						
						 <input type="hidden" name="url" class="listb saInp" value="/mallPage/${pro.id }/${stoId}/79B4DE7C/phoneProduct.do">
					</li>
				</c:forEach>
			</div>
		</div>
	</div>

	</div>
	<div style="width: 100%;height: 30px;text-align: center; position: fixed; bottom: 0">
		<input type="button" value="确认" onclick="fnOk()" style="width:70px;  cursor: pointer;background-color: #1aa1e7;border-radius: 3px;color: #fff;border: none;"/>
		<input type="button" value="返回" onclick="fnCancel()" style="width:70px;  cursor: pointer;background-color: #8cc717;border-radius: 3px;color: #fff;border: none;"/>
	</div>
  </body>
</html>
