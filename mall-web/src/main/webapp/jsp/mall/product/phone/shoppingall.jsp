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
<title> 商品</title>
<link rel="stylesheet" type="text/css" href="/css/common/init.css" />
</head>

<body>
<!-- <section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section> -->
<link rel="stylesheet" type="text/css" href="/css/mall/public.css" />
<link rel="stylesheet" type="text/css" href="/css/mall/shoppingall/menu.css" />
<link id="link" rel="stylesheet" type="text/css" href="/css/mall/shoppingall/indexModify2.css?<%=System.currentTimeMillis()%>" />
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
<div class="warp">	
	<!-- 头部开始 -->
   <header class="w header">
	    <c:if test="${groupId eq null}"> 
	    <c:if test="${rType == 0 }"> 全部商品</c:if>
	    <c:if test="${rType == 1 }"> 积分兑换商品</c:if>
	    <c:if test="${rType == 2 }"> 粉币兑换商品</c:if>
	    </c:if>
	      <c:forEach items="${groLs }" var="groL"> 
	         <c:if test="${groL.group_id eq groupId }"> ${groL.group_name } </c:if>
	        </c:forEach>
	   </header>
	<!-- 头部分区结束 -->
	   <input type="hidden" class="rType forms_cla" name="rType" value="${rType }"/>
	   <input type="hidden" class="type forms_cla" name="type" value="${type }"/>
		<input type="hidden" class="desc forms_cla" name="desc" value="${desc }"/>
	   <input type="hidden" class="shopid forms_cla" name="shopid" value="${shopid }"/>
	   <input type="hidden" class="groupId forms_cla" name="groupId" value="${groupId }"/>
	   <c:set var="urls" value="/mallPage/${shopid }/79B4DE7C/shoppingall.do"></c:set>
	   <input type="hidden" class="urls" value="${urls }"/>
	    <!-- 搜索框开始 -->
	<section class="searBtn w" style="margin-bottom:0px;">
	    <input type="text" value="${proName}" class="input s-input forms_cla" id="proName" name="proName"  onfocus="focusIn(this)" oninput="inputChange(this)"/>
	    <i onclick="queryurl('${type}','${desc}','${groupId}')"></i>
	</section>
	<jsp:include page="/jsp/mall/pop_up/searchPopUp.jsp"></jsp:include>
	<!-- 搜索框结束 -->
	<!-- 列表标题 -->
	<section class="list-tit w" style="margin-bottom: 0px;">
	    <ul  class="flex">
	        <li class="flex-1" onclick="queryurl(1,'${desc}','${groupId}')"><i class="<c:if test="${type eq 1 }">current </c:if>">最新</i></li>
	        <li class="flex-1" onclick="queryurl(2,'${desc}','${groupId}')"><i class="<c:if test="${type eq 2 }">current </c:if>">销量</i></li>
	        <li class="flex-1" onclick="queryurl(3,'${desc}','${groupId}')"><i class="<c:if test="${type eq 3 }">current </c:if>">价格<c:if test="${type eq 3 && desc eq 0 }"><i class="arrow-down"></i></c:if><c:if test="${type eq 3 && desc eq 1}"><i class="arrow-up"></i></c:if></i></li>
	       <li class="last-item flex-1"><a href="javascript:;"><img src="images/mall/img/more.jpg" class="more-pic" /></a></li>
	    </ul>
	</section>
	<div class="main flex">
		<div class="sort">
			<ul class="sortList">
				<li  class="<c:if test='${empty groupId && empty groupPId}'>sortCur</c:if> allPro" onclick="queryurl('${type}','${desc}','-1')">全部商品</li>
	         	<c:forEach items="${groLs }" var="groL" varStatus="i"> 
	         		<c:set var="isDefault" value="false"></c:set>
	         		<c:if test='${!empty groupId && groupId == groL.group_id}'>
	         			<c:set var="isDefault" value="true"></c:set>
	         		</c:if>
	         		<c:if test='${!empty groupPId && groupPId == groL.group_id}'>
	         			<c:set var="isDefault" value="true"></c:set>
	         		</c:if>
	         		<c:if test='${!empty proName && proName == groL.group_name}'>
	         			<c:set var="isDefault" value="true"></c:set>
	         			<script>
	         				$(".allPro").removeClass("sortCur");
	         			</script>
	         		</c:if>
	        		<li class="<c:if test='${isDefault}'>sortCur</c:if>"
	        		 onclick="<c:if test='${groL.is_child == 1}'>groupChild(this,${groL.group_id });</c:if><c:if test='${groL.is_child == 0}'>queryurl('${type}','${desc}','${groL.group_id }');</c:if>">${groL.group_name }</li>
	        	</c:forEach>
			</ul>
		</div>
		<div class="product">
			<c:if test="${!empty page && !empty page.subList}">
			<ul class="productList clearfix" id="sort1Main">
		   		<c:forEach items="${page.subList }" var="shopall">
				   <li class="">
				   		<c:set var="url" value="${shopall.return_url }"></c:set>
				   		<c:if test="${!empty saleMemberId }">
				   			<c:set var="url" value="${url }&saleMemberId=${saleMemberId}"></c:set>
				   		</c:if>
		 				<a href="${url }">
				   	    <div class="product-pic" />
				   	    	<span class="img-container"  data-original="${shopall.image_url}" ></span>
				   	    </div>
				   	    <div class="product-info">
				   	    	<div class="info-title2">
				   	    	<c:if test="${!empty shopall.pro_label && shopall.pro_label != ''}">
				   	    		<i class="label_i">${shopall.pro_label }</i>
				   	    	</c:if>
				   	    	${shopall.pro_name}
				   	    	</div>
				   	    	<div class="info-price flex">
				   	    		<div class="now-price">
				   	    			<p class="price"><c:if test="${rType == 0 }">￥</c:if>${shopall.price}<c:if test="${rType > 0 && !empty shopall.unit}">${shopall.unit }</c:if></p>
				   	    		</div>
				   	    		<c:if test="${rType == 0 && !empty shopall.hyPrice && discount != 1 && shopall.is_member_discount == 1}">
					   	    		<div class="member-price">
					   	    			<p><em>会员:</em><span class="hyPrice">￥${shopall.hyPrice }</span></p>
					   	    		</div> 
				   	    		</c:if>
				   	    	</div>
				   	    	<div class="info-price flex">
				   	    		 <c:if test="${shopall.pro_cost_price > 0 && shopall.pro_cost_price > shopall.price}">
						        	<div class="old-price">￥<span>${shopall.pro_cost_price }</span></div>
						        </c:if>
				   	    	</div>
				   	    </div>
				   	    </a>
				   </li>
		   	  </c:forEach>
		    </ul>
			    <c:if test="${page.curPage+1 <= page.pageCount}">
			    <input type="hidden" class="curPage" value="${page.curPage }"/>
			    <input type="hidden" class="pageCount" value="${page.pageCount }"/>
				<input type="hidden" class="isLoading" value="1"/>
			    </c:if>
		    </c:if>
		    <jsp:include page="/jsp/mall/pop_up/groupPopUp.jsp"></jsp:include>
		</div>
	</div>
</div>

<!-- 列表标题结束 -->
<!-- 页脚开始 -->
<jsp:include page="/jsp/mall/phoneFooterMenu.jsp"></jsp:include>
<input type="hidden" class="memberId" value="${memberId }"/>
<input type="hidden" class="userid" value="${userid }"/>
<input type="hidden" class="saleMemberId forms_cla" name="saleMemberId" value="${saleMemberId }"/>
<c:if test="${!empty isSeller }">
<input type="hidden" class="isSeller forms_cla" name="isSeller" value="${isSeller }"/>
</c:if>

<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>

<script type='text/javascript' src="/js/plugin/lazyload/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/js/mall/product/shoppingall.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js"></script>
</body>
<script>
var t1 = new Date().getTime(); 

/* $(window).load(function() {
	$(".loading").hide();
}); */
var selLen = $("input.isSeller").length;
if(saleMemberId != null && saleMemberId != "" && selLen > 0){
	$("input.urls").val("/phoneSellers/"+saleMemberId+"/79B4DE7C/shoppingall.do");
}
function queryurl(type,desc,style){
	if(type==3){
		if(desc==1){
			desc=0;
		}else{
			desc=1;
		}
	}
	if(saleMemberId == null || saleMemberId == ""){
		var url = "/mallPage/${shopid}/79B4DE7C/shoppingall.do?type="+type+"&&desc="+desc;
		var rType = $(".rType").val();
		if(rType != null && rType != "" && rType != "0"){
			url += '&&rType='+rType;
		}
	}else{
		var url = "/phoneSellers/"+saleMemberId+"/79B4DE7C/shoppingall.do?type="+type+"&&desc="+desc;
	}
	if(style!='-1' && style != ''){
		url += "&&groupId="+style;
	}
	var proName = $("#proName").val();
	if(proName!=null&&proName!=""&&proName!=undefined){
		if(style!='-1'){
			url += "&&proName="+proName;
		}
	}
	window.location.href = url;
}
var $container = $('.productList');
if($("input.curPage").length > 0 && $("input.isLoading").length > 0){
	$container.scroll(function () {
		var curPage = $("input.curPage").val();//当前页
		var pageCount = $("input.pageCount").val();//总共的页数
		var isLoading = $("input.isLoading").val();//是否继续加载
		if(isLoading == 0 || curPage+1 > pageCount){
			$container.unbind('scroll');
			return false;
		}
		var totalHeight = $container.prop('scrollHeight');
		var scrollTop = $container.scrollTop(); 
		var height = $container.height();
		var TRIGGER_SCROLL_SIZE =  $container.find("li:eq(0)").height();
		if(totalHeight - (height + scrollTop) <= TRIGGER_SCROLL_SIZE && isLoading == 1){
			loadMore();
	    }
	  });
}

function loadMore() {
    var curPage = $("input.curPage").val();
    if(curPage == null || curPage == ''){
    	return false;
    }
    var datas = {
       	curPage:curPage*1+1,
       	shopid:$(".shopid").val()
       };
    if($(".forms_cla").length > 0){
    	$(".forms_cla").each(function(){
    		var val = $(this).val();
    		if(val != null && val != ""){
	    		datas[$(this).attr("name")] = val;
    		}
    	});
    }
    
    $("input.isLoading").val(-1);
    $.ajax({
		type : "post",
		url : "/mallPage/79B4DE7C/shoppingAllPage.do",
		data : datas,
		dataType : "json",
		success : function(data) {
			var saleMemberId = $("input.saleMemberId").val();
			var rType = $("input.rType").val();
			var html = "";
			if(data != null){
				var page = data.page;
				if(page == null){
					return false;
				}
					if(page.subList != null && page.subList.length > 0){
						for(var i = 0;i < page.subList.length; i++){
							var product = page.subList[i];
							html += '<li class="">';
							var return_url = product.return_url
							if(saleMemberId!= null && saleMemberId != ""){
								return_url += "&saleMemberId="+saleMemberId;
							}
							html += '<a href="'+return_url+'">';
							html += '<div class="product-pic" ><span class="img-container" style="display: block; background-image: url('+product.image_url+');" ></span> </div>';
							html += '<div class="product-info">';
							html += '<div class="info-title2">';
							if(product.pro_label != null && product.pro_label != ''){
								html += '<i class="label_i">'+product.pro_label+'</i>';
							}
							html += product.pro_name+'</div>';
							html += '<div class="info-price flex">';
							var unit = "";
							var isYuan = "￥";
							if(product.unit != null && product.unit != ''){
								unit = product.unit;
								isYuan = "";
							}
							html += '<div class="now-price"><p class="price">'+isYuan+product.price+unit+'</p></div>';
							var hyPrice = product.hyPrice;
							if(rType == 0 && hyPrice != null && hyPrice != '' && hyPrice*1 > 0){
								html += '<div class="member-price"><p><em>会员:</em><span class="hyPrice">￥'+hyPrice+'</span></p></div> ';
							}
							html += '</div>';
							html += '<div class="info-price flex">';
							if(product.pro_cost_price != null && product.pro_cost_price != ""){
								if(product.pro_cost_price > 0){
									html += '<div class="old-price">￥<span>'+product.pro_cost_price+'</span></div>';
								}
							}
							html += '</div>';
							html += '</div>';
							html += '</a>';
							html += "</li>";
						}
					}
					if( page.curPage*1 > page.pageCount*1){
						$("input.isLoading").val(0);
						$container.unbind('scroll');
						return false;
					}
				$("input.curPage").val(page.curPage);
			}
			if(html == ""){
				$("input.isLoading").val(0);
				$container.unbind('scroll');
				return false;
			}else{
				$container.append(html);
				$("input.isLoading").val(1);
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			$("input.isLoading").val(1);
		}
	});
}


</script>
</html>