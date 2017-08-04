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
<link id="link" rel="stylesheet" type="text/css" href="/css/mall/shoppingall/index.css" />
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>
</head>

<body>
<!-- 头部开始 -->
   <!--  <header class="w">
        <div class="close l">×</div>
        <div class="center">金稻农家庄</div>
        <div class="right r">
            <div></div>
            <div></div>
            <div></div>
        </div>
    </header> -->
    <!-- 头部分区结束 -->
   <header class="w header">
    <c:if test="${groupId eq null}"> 
    <c:if test="${rType == 0 }"> 全部商品</c:if>
    <c:if test="${rType == 1 }"> 积分兑换商品</c:if>
    </c:if>
      <c:forEach items="${groLs }" var="groL"> 
         <c:if test="${groL.group_id eq groupId }"> ${groL.group_name } </c:if>
        </c:forEach>
   </header>
   <input type="hidden" class="rType" value="${rType }"/>
    <!-- 搜索框开始 -->
<section class="searBtn w" style="margin-bottom:0px;">
    <input type="text" value="${proName}" class="input" id="proName"/>
    <i onclick="queryurl('${type}','${desc}','${groupId}')"></i>
</section>
<!-- 搜索框结束 -->
<!-- 列表标题 -->
<section class="list-tit w" style="margin-bottom: 0px;">
    <ul>
        <li  onclick="queryurl(1,'${desc}','${groupId}')"><i  class="<c:if test="${type eq 1 }">current </c:if>">最新</i></li>
        <li onclick="queryurl(2,'${desc}','${groupId}')"><i     class="<c:if test="${type eq 2 }">current </c:if>">销量</i></li>
        <li onclick="queryurl(3,'${desc}','${groupId}')"><i  class="<c:if test="${type eq 3 }">current </c:if>">价格<c:if test="${type eq 3 && desc eq 0 }"><i class="arrow-down"></i></c:if><c:if test="${type eq 3 && desc eq 1}"><i class="arrow-up"></i></c:if></i></li>
       <li class="last-item"><a href="javascript:;"><img src="images/mall/img/more.jpg" class="more-pic" /></a></li>
    </ul>
</section>
<div class="main w">
<c:forEach items="${xlist }" var="shopall">
<section class="brand">
 <a href="/mallPage/${shopall.id}/${shopid}/79B4DE7C/phoneProduct.do?rType=${rType}">
    <div class="pic-l l">
            <%-- <img src="${http}${shopall.image_url}" alt="" /> --%>
             <span class="img-container" style="background: url(${http}${shopall.image_url}) no-repeat center center;background-size: contain;"></span>
    </div>
    <div class="text l">
        <p>${shopall.pro_name}</p>
        <div class="tLeft">
        	<c:if test="${rType == 0 }">
	        <p class="price">￥${shopall.price}</p>
	        </c:if>
	        <c:if test="${rType == 1 }">
	        <p>${shopall.change_integral}积分</p>
	        </c:if>
	        <c:if test="${rType == 2 }">
	        <p>${shopall.change_fenbi}粉币</p>
	        </c:if>
	        <c:if test="${shopall.pro_cost_price > 0 && shopall.pro_cost_price > shopall.price}">
	        	<p class="old">￥<span>${shopall.pro_cost_price }</span></p>
	        </c:if>
        </div>
        <div class="tRight">
        <c:if test="${rType == 0 && !empty shopall.hyPrice && discount != 1}">
	        <p><em>会员:</em><span class="hyPrice">￥${shopall.hyPrice}</span></p>
        </c:if>
        </div>
    </div>
   </a> 
</section>

</c:forEach>
</div>
<!-- 列表标题结束 -->
<!-- 页脚开始 -->
<footer class="foot row">
    <div class="col col-20">
        <a href="javascript:void(0)" onclick="pageclick('${pageid}')">
            <img src="images/mall/img/Home.jpg" />
            <span></span>
        </a>
    </div>
    <div class="col col-30 sort-div">
            <img src="images/mall/img/list-pic.jpg" alt="" />
            <span>分类</span>
         <ul class="sort-list" style="display: none;">
         <li onclick="queryurl('${type}','${desc}','')">全部商品</li>
         <c:forEach items="${groLs }" var="groL"> 
        	<li onclick="queryurl('${type}','${desc}','${groL.group_id }')">${groL.group_name }</li>
        </c:forEach>
        	
        </ul>
    </div>
    <div class="col col-30">
        <a href="/mallPage/79B4DE7C/shoppingcare.do?member_id=${memberId}">
            <img src="/images/mall/img/Shopping Cart .jpg" alt="" />
            <span>购物车</span>
        </a>
    </div>
    <div class="col col-30">
        <a href="/mMember/79B4DE7C/toUser.do?member_id=${memberId}">
            <img src="/images/mall/img/User.jpg" alt="" />
            <span>我的</span>
        </a>
    </div>
</footer>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>

</body>
<script>
//店面跳转
function pageclick(obj){
	if(obj==''||obj==null||obj==undefined||obj==0||obj=='0'){
		alert("店面未设置微商城主页面或者微商城主页面已删除");
	}else{
		window.location.href = "/mallPage/"+obj+"/79B4DE7C/pageIndex.do";
	}
}
function queryurl(type,desc,style){
	if(type==3){
		if(desc==1){
			desc=0;
		}else{
			desc=1;
		}
	}
	var url = "/mallPage/${shopid}/79B4DE7C/shoppingall.do?type="+type+"&&desc="+desc;
	if(style!=''){
		url += "&&groupId="+style;
	}
	var proName = $("#proName").val();
	if(proName!=null&&proName!=""&&proName!=undefined){
		url += "&&proName="+proName;
	}
	var rType = $(".rType").val();
	if(rType != null && rType != "" && rType != "0"){
		url += '&&rType='+rType;
	}
	window.location.href = url;
}

$(function(){
	$("div.fixRig a").css({
		"width":"1.12rem",
		"height":"1.12rem",
		"background-size":"auto 0.6rem"
	});		
	$(".last-item").click(function(){
		var _href = $("#link").attr("href");
		if(_href==="/css/mall/shoppingall/index.css"){
			$("#link").attr("href","/css/mall/shoppingall/index2.css");
		}
		else{
			$("#link").attr("href","/css/mall/shoppingall/index.css");
		}
	});
	$(".sort-div").click(function(){
		$(this).find("ul").toggle();
	})
	$(".text").each(function(){
		var price = $(this).find(".price").html();
		if(price != null && price != ""){
			var a = price.split(".");
			$(this).find(".price").html("<span class='intNum'>"+a[0]+"</span>.<small class='floatNum'>"+a[1]+"</small>");
		}
		var hyprice = $(this).find(".hyPrice").html();
		if(hyprice != null && hyprice != ""){
			var a = hyprice.split(".");
			$(this).find(".hyPrice").html("<span class='intNum'>"+a[0]+"</span>.<small class='floatNum'>"+a[1]+"</small>");
		}
        
        var old = $(this).find(".old").html();
        if(old != null && old != ""){
			var a = old.split(".");
			$(this).find(".old").html("<span class='intNum'>"+a[0]+"</span>.<small class='floatNum'>"+a[1]+"</small>");
		}
	});
});
</script>
</html>