<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>上门自提管理</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/common/edit1.css?<%= System.currentTimeMillis()%>" />
	<link rel="stylesheet" type="text/css" href="/css/common/comm.css" />
	<link rel="stylesheet" type="text/css" href="/css/mall/take.css" />
	<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="/js/public.js"></script>
	<script type="text/javascript" src="/js/util.js"></script>
	<script type="text/javascript" src="/js/table.js"></script>
	<script src="/js/plugin/layer/layer.js"></script>
	<script type="text/javascript" src="/js/mall/mall_public.js"></script>
  </head>
  
  <body>
  <jsp:include page="/jsp/common/headerCommon.jsp"/>
 <div class="con_div">
    <!-- <div class="fansTitle">
		<span class="i-con fl"></span><span class="title-p">店铺管理</span>
	</div> -->
	<div class="con-head">
		<a class="" href="/mFreight/index.do">物流管理</a> 
		<a class="navColor" href="/mFreight/takeindex.do">上门自提</a>
	</div>

	<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
		<c:set var="payCla" value="ui-switcher-off"></c:set>
		<c:if test="${!empty set }">
			<c:if test="${set.isTakeTheir == 1}">
				<c:set var="payCla" value="ui-switcher-on"></c:set>
			</c:if>
		</c:if>
		<input type="hidden" class="id" value="${set.id }" />
		<div>
			<div class="payment-block">
			    <div class="payment-block-header">
			        <h3>上门自提功能</h3>
			        <label class="ui-switcher ui-switcher-small js-switch pull-right ${payCla }" id="0"></label>
			    </div>
			    <div class="payment-block-body">
			        <h4>启用上门自提功能后，买家可以就近选择你预设的自提点，下单后你需要尽快将商品配送至指定自提点。</h4>
			    </div>
			</div>

		</div>
	</div>

	<div class="txt-btn pd-bottom-15 clearfix">
		<div class="blue-btn fl right-15">
			<a href="/mFreight/take_edit.do">新增</a>
		</div>
	</div>


	<div class="box-btm30">

		<div class="txt-tle">
			<div class="t2" style="width: 20%;">自提点名称</div>
			<div class="t3" style="width: 50%;">自提点地址</div>
			<div class="t3">联系电话</div>
			<div class="t3">操作</div>
		</div>

		<div class="msg-list">
			<div class="txt-tle2" id="list">
				<c:forEach items="${page.subList}" var="take">
					<li>
						<div class="listb" style="width: 20%;">${take.visitName }</div>
						<div class="listb" style="width: 50%;">${take.visitAddressDetail }</div>
						<div class="listb">${take.visitContactNumber }</div>
						<div class="listd">
							<div class="bianji" style="width: 120px;">
								<a href="mFreight/take_edit.do?id=${take.id}" class="bj-a textEdit"></a> 
								<a href="javascript:void(0)" class="bj-a textDelete" onclick="del(${take.id})"></a>
							</div>
						</div>
					</li>
				</c:forEach>
			</div>
		</div>
	</div>

	<!-- 分页 -->
	<c:if test="${! empty page.subList }">
		<input type="hidden" id="taskId" value="0"/>
		<jsp:include page="../../common/page/pageTwo.jsp"></jsp:include>
	</c:if>
</div>
	<script type="text/javascript" src="/js/mall/take/take_index.js"></script>
  </body>
</html>
