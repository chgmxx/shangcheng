<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<title>商品管理-搜索推荐</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>" />
<link rel="stylesheet" type="text/css" href="/css/common/Fan-index.css?<%= System.currentTimeMillis()%>" />
<link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
<link rel="stylesheet" type="text/css" href="/css/mall/group.css" />
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script src="/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="/js/public.js"></script>

<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/mall/mall_public.js"></script>
<script type="text/javascript">
	var error = '${error}';
	if (error != undefined && error != "") {
		parent.layer.alert("参数错误，将调回前一个页面");
		window.history.back(-1);
	}
	if(top==self){
		 window.location.href="/mPro/product_start.do";
	}
</script>
</head>
<body>
	<div id="con-box">
		<div class="con-head" style="margin:0px;">
			<a class="" href="/mPro/index.do" >商品管理</a>
			<a class="" href="/mPro/group/group_index.do" >分组管理</a>
			<a class="navColor" href="/mPro/group/label_index.do">搜索推荐管理</a>
		</div>
		<c:if test="${!empty shoplist }">
		<div class="con-box1">
			<div class="txt-btn">
				<c:if test="${!empty pId }">
					<div class="green-btn fl box1Btn">
						<a href="${urls }" class="backBtn">返回</a>
					</div>
				</c:if>
			</div>
		</div>
		<div class="msg-list">
			<ul id="list group_ul">
				<li class="txt-tle">
					<span class="f1 fl" style="margin-top:20px;"><input type="checkbox" name="check"
					id="checkAll" class="allCheck" value="" onclick="showMore(this)" /></span>
					<span class="f8 fl">分组名称</span>
					<span class="f7 fl">是否已推荐</span> 
					<span class="f8 fl">所属店铺</span> 
					<span class="f8 fl">创建时间</span>
					<span class="f8 fl">操作</span>
				</li>

				<c:if test="${!empty page.subList}">
					 <c:forEach var="group" items="${page.subList }">
					<li class="txt-tle" >
						<span class="f1 fl check-box" style="margin-top:20px;"><input type="checkbox" name="check"
							id="" class="checkPro" value="${group.id }" /></span>
						<span class="f8 fl"><c:if test="${!empty group.groupName}">${group.groupName }</c:if></span> 
						<span class="f7 fl">
							<c:if test="${!empty group.lDelete && group.lDelete == 0}">已推荐</c:if>
							<c:if test="${empty group.lDelete || group.lDelete == 1}">未推荐</c:if>
						</span> 
						<span class="f8 fl txt-overflow"><c:if test="${!empty group.sto_name}">${group.sto_name }</c:if></span>
						<span class="f8 fl"><c:if test="${!empty group.createTime}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${group.createTime }" /></c:if></span>
						<span class="f8 fl cz_span" sc="${group.lDelete }">
							<input type="hidden" class="groupId" value="${group.id }"/>
							<input type="hidden" class="shopId" value="${group.shopId }"/>
							<input type="hidden" class="id" value="${group.lId }"/>
							<%-- <a href="/mPro/group/to_edit.do?pId=${group.id }" class="addChild" style="display:none;">添加子分类</a> --%>
							<c:if test="${!empty group.lDelete && group.lDelete == 0}">
							<a href="javascript:void(0);" id="${group.id}" class="delete_tj_a"  style="background: none;">删除推荐</a>
							</c:if>
							<c:if test="${empty group.lDelete || group.lDelete == 1}">
							<a href="javascript:void(0);" id="${group.id}" class="tj_a"  style="background: none;">推荐</a>
							</c:if>
							<c:if test="${empty pId }">
								<a href="/mPro/group/label_index.do?pId=${group.id }" class="editGroup" style="background: none;">子类列表</a>
							</c:if>
						</span> 
					</li>
					</c:forEach> 
				</c:if>
			</ul>
			<c:if test="${! empty page.subList }">
				<input type="hidden" id="pId" value="${pId }"/>
				<jsp:include page="pageProduct.jsp"></jsp:include>
			</c:if>
		</div>
		</c:if>
		<c:if test="${empty shoplist }">
			 <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
		</c:if>
		<!--内容编辑行end-->
	</div>
	<!--中间信息结束-->
	<!--container  End-->
	<!--footer  End-->
	<script type="text/javascript">
	
	//推荐
	$(".tj_a").click(function(){
		var data = [];
		var parentObj = $(this).parent();
		var obj = {
			shopId : parentObj.find(".shopId").val(),
			groupId : parentObj.find(".groupId").val(),
			isDelete : 0
		};
		var id = parentObj.find("input.id").val();
		if(id != "" && id != null && typeof(id) != "undefined"){
			obj["id"] = id;
		}
		data[data.length] = obj;
		//data = JSON.stringify(data);
		ajax(this,"推荐",data);
	});
	//取消推荐
	$(".delete_tj_a").click(function(){
		var data = [];
		var parentObj = $(this).parent(); 
		// 询问框
		parent.layer.confirm('您确定要删除推荐？', {
			btn : [ '确定', '取消' ]
		// 按钮
		}, function() {
			var obj = {
				shopId : parentObj.find(".shopId").val(),
				groupId : parentObj.find(".groupId").val(),
				isDelete : 1
			}
			var id = parentObj.find("input.id").val();
			if(id != "" && id != null && typeof(id) != "undefined"){
				obj["id"] = id;
			};
			data[data.length] = obj;
			//data = JSON.stringify(data);
			ajax(this,"删除推荐",data);
		});
	});
	//批量推荐
	$(".js-batch-tj").click(function(){
		var bol = false;
		var data = [];
		
		$(".cz_span").each(function(){
			if($(this).parents("li").find(".checkPro").is(":checked")){
				var parentObj = $(this);
				var obj = {
					shopId : parentObj.find(".shopId").val(),
					groupId : parentObj.find(".groupId").val(),
					isDelete : 0
				};
				
				var id = parentObj.find("input.id").val();
				if(id != "" && id != null && typeof(id) != "undefined"){
					obj["id"] = id;
				}
				var sc = parentObj.attr("sc");
				if(sc == "" || sc == 1){
					data[data.length] = obj;
				}
				bol = true;
			}
		});
		if(bol){
			ajax(this,"批量推荐",data);
		}else{
			parent.layer.alert("您还没有要选择批量的推荐", {
				offset : "30%"
			});
		}
		
		
	});
	//批量删除推荐
	$(".js-batch-sctj").click(function(){
		var data = [];
		var bol = false;
		$(".cz_span").each(function(){
			if($(this).parents("li").find(".checkPro").is(":checked")){
				var parentObj = $(this);
				var obj = {
					shopId : parentObj.find(".shopId").val(),
					groupId : parentObj.find(".groupId").val(),
					isDelete : 1
				};
				
				var id = parentObj.find("input.id").val();
				if(id != "" && id != null && typeof(id) != "undefined"){
					obj["id"] = id;
				}
				var sc = parentObj.attr("sc");
				if(sc != "" && sc == 0){
					data[data.length] = obj;
				}
				bol = true;
			}
		});
		//data = JSON.stringify(data);
		if(bol){
			if (data != null && data != "") {
				// 询问框
				parent.layer.confirm('您确定要批量删除推荐？', {
					btn : [ '确定', '取消' ]
				// 按钮
				}, function() {
					ajax(this,"批量删除推荐",data);
				});
			}else{
				parent.layer.alert("没有能被批量删除的推荐", {
					offset : "30%"
				});
			}
		}else{
			parent.layer.alert("您还没有选择要批量删除的推荐", {
				offset : "30%"
			});
		}
		
		
		
	});
	
	function ajax(obj,msg,data) {
		if (data != null && data != "") {
			// loading层
			var layerLoad = parent.layer.load(1, {
				shade : [ 0.1, '#fff' ]
			// 0.1透明度的白色背景
			});
			$.ajax({
				type : "post",
				url : "mPro/group/labelEdit.do",
				data : {param : JSON.stringify(data)},
				dataType : "json",
				success : function(data) {
					parent.layer.close(layerLoad);
					if (data.code == 0) {// 重新登录
						parent.layer.alert("操作失败，长时间没操作，跳转到登录页面", {
							offset : "30%",
							closeBtn : 0
						}, function(index) {
							location.href = "/user/tologin.do";
						});
					} else if (data.code == 1) {
						var tip = parent.layer.alert(msg+"成功", {
							offset : "30%",
							closeBtn : 0
						}, function(index) {
							parent.layer.close(tip);
							location.href = window.location.href;
						});
					} else {// 编辑失败
						var tip = parent.layer.alert(msg+"失败", {
							offset : "30%"
						});
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					parent.layer.close(layerLoad);
					parent.layer.alert(msg+"失败", {
						offset : "30%"
					});
					return;
				}
			});
			parent.layer.closeAll();
		}else{
			var tip = parent.layer.alert("没有能被"+msg, {
				offset : "30%",
				closeBtn : 0
			}, function(index) {
				parent.layer.closeAll();
			});
		}

	}
	
	
	/*全选 反选*/
	var _array = $("input[name='check']");
	function showMore(obj) {
		if ($(obj).is(":checked")) {
			for (var i = 0; i < _array.length; i++) {
				_array[i].checked = true;
			}
		} else {
			for (var i = 0; i < _array.length; i++) {
				_array[i].checked = false;
			}
		}
	}
	$(".checkPro").click(function(){
		if(!$(this).is(":checked")){
			$(".allCheck").removeAttr("checked");
		}else{
			var flag = true;
			$(".checkPro").each(function(){
				if(!$(this).is(":checked")){
					flag = false;
				}
			});
			if(flag){
				$(".allCheck").attr("checked","checked");
			}
		}
	});
	//全选
	$(".js-batch-checkAll").click(function(){
		$(".allCheck").attr("checked","checked");
		showMore($(".allCheck"));
	});
	</script>
</body>
</html>