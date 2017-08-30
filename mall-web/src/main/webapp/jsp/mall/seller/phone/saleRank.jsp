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
<title>销售排行榜</title>
<link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>" />
</head>

<body>
<!--加载动画-->
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/mall/seller/main.css" />
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/html5shiv.min.js"></script>

<div class="sWrapper">
   <header class="text-header">
       销售员排行榜
   </header>
   <nav class="r-nav">
       <ul class="r-nav-ul">
           <li onclick="showNav(1)" class="<c:if test="${empty type || type == 1 }">nav-active</c:if>" type="rank-1">销售榜</li>
           <li onclick="showNav(2)" class="<c:if test="${type == 2 }">nav-active</c:if>" type="rank-2">佣金榜</li>
           <li onclick="showNav(3)" class="<c:if test="${type == 3 }">nav-active</c:if>" type="rank-3">积分榜</li>
       </ul>
   </nav>
   <input type="hidden" class="type" value="${type }"/>
   <section class="r-body" id="rank-1">
       <div class="r-mine">
           <p>我的名次</p>
           <p class="r-text">
               <strong class="r-text-num">第&nbsp;${myRank }&nbsp;名</strong>
               <span class="r">
               <c:if test="${empty type || type == 1 }">总销售：${seller.saleMoney }元</c:if>
               <c:if test="${type == 2 }">总佣金：${seller.commission }元</c:if>
               <c:if test="${type == 3 }">总积分：${seller.incomeIntegral }</c:if>
               	</span>
           </p>
           <c:set var="headImages" value="/images/mall/img/pt-detail2.jpg"></c:set>
        	<c:if test="${!empty member }">
        		<c:if test="${!empty member.headimgurl }">
        			<c:set var="headImages" value="${member.headimgurl }"></c:set>
        		</c:if>
        	</c:if>
           <div class="r-rank-l memberHeadImg" style="background-image: url('${headImages}'); background-size: cover;"></div>
       </div>
       <c:if test="${!empty page }">
       <ul class="r-ranklist">
       	<c:if test="${!empty page.subList }">
       		<c:forEach var="rank" items="${page.subList }" varStatus="i">
           <li>
           	   <c:if test="${i.index < 3 }">
               		<div class="r-rank-${i.index+1 }"></div>
               </c:if>
               <c:if test="${i.index >= 3 }">
               		<div class="r-rank-num">${i.index+1 }</div>
               </c:if>
                <c:set var="headImagesUrl" value="/images/mall/img/pt-detail2.jpg"></c:set>
        		<c:if test="${!empty rank.headimgurl }">
        			<c:set var="headImagesUrl" value="${rank.headimgurl }"></c:set>
        		</c:if>
              	<div class="r-rank-s"  style="background: url('${headImagesUrl}');background-size: cover;"></div>
              	<c:set var="userName" value="${rank.user_name }"></c:set>
              	<c:if test="${empty rank.user_name }">
              		<c:set var="userName" value="${rank.nickname }"></c:set>
              	</c:if>
               <div class="rank_name">${userName }</div>
               <c:if test="${empty type || type == 1 }">
               		<p class="r-sale-text">销售：<span>${rank.sale_money }元</span></p>
               </c:if>
               <c:if test="${type == 2 }">
               		<p class="r-sale-text">佣金：<span>${rank.commission }元</span></p>
               </c:if>
               <c:if test="${type == 3 }">
               		<p class="r-sale-text">积分：<span>${rank.income_integral }</span></p>
               </c:if>
           </li>
           </c:forEach>
     	</c:if>
       </ul>
       </c:if>
       <input type="hidden" class="curPage" value="${page.curPage+1  }"/>
       <c:if test="${page.curPage+1 <= page.pageCount}">
       <div class="r-more" onclick="loadMore(this)">
            <p>加载更多</p>
            <div class="spinner">
                <div class="bounce1"></div>
                <div class="bounce2"></div>
                <div class="bounce3"></div>
            </div>
        </div>
        </c:if>
        <div class="r-more r-no-more" style='<c:if test="${page.curPage+1 <= page.pageCount}">display: none;</c:if>'>
         <p>没有更多了</p>
        </div>
   </section>
</div>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript">

wx.config({
	debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: "${record.get('appid')}", // 必填，公众号的唯一标识
    timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
    nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
    signature: "${record.get('signature')}",// 必填，签名，见附录1
    jsApiList: ['hideOptionMenu'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

wx.ready(function(){
 wx.hideOptionMenu();
});

function showNav(type){
	location.href = "/phoneSellers/"+type+"/79B4DE7C/saleRank.do?uId=${userid}";
}


function loadMore(obj) {
    $(obj).find("p").hide().next(".spinner").show().find("div").addClass("loadings");

    /* var t = setTimeout(function () {
        $(obj).find("p").slideDown().next(".spinner").hide().find("div").removeClass("loadings");
    },5000); */
    var type = $("input.type").val();
    var curPage = $("input.curPage").val();
    $.ajax({
		type : "post",
		url : "/phoneSellers/"+type+"/79B4DE7C/loadSaleRank.do",
		data : {
			curPage:curPage
		},
		dataType : "json",
		success : function(data) {
			$(obj).find("p").slideDown().next(".spinner").hide().find("div").removeClass("loadings");
			if (data.flag ) {
				var index = $(".r-ranklist li").length;
				if(data.data != null){
					var page = data.data;
					if(data.data.subList != null){
						var html = "";
						var list = data.data.subList;
						for(var i = 0 ;i < list.length; i++){
							var rank = list[i];
							html +=" <li>";
							if(i+1+index <= 3){
								html += "<div class='r-rank-"+(i+1+index)+"'></div>";
							}else{
								html += "<div class='r-rank-num'>"+(i+1+index)+"</div>";
							}
							var headImages = "/images/mall/img/pt-detail2.jpg";
							if(rank.headimgurl != null && rank.headimgurl != ""){
								headImages = rank.headimgurl;
							}
							html += "<div class='r-rank-s' style='background: url("+headImages+");background-size: contain;'></div>";
			              	var username = rank.user_name;
			              	if(username == null || username == ""){
			              		username = rank.nickname;
			              	}
			              	if(username == null || username == ""){
			              		username = "未知用户";
			              	}else{
			              		username = username.replace(/&/g, "&amp;");
			              		username = username.replace(/</g, "&lt;");
			              		username = username.replace(/>/g, "&gt;");
			              		username = username.replace(/'/g, "&acute;");
			              		username = username.replace(/"/g, "&quot;");
			              		username = username.replace(/\|/g, "&brvbar;");
			              	}
							html += "<div class='rank_name'>"+username+"</div>";
							
							if(type == 1 || type == ""){
								html += "<p class='r-sale-text'>销售：<span>"+rank.sale_money+"元</span></p>";
							}else if(type == 2){
								html += "<p class='r-sale-text'>佣金：<span>"+rank.commission+"元</span></p>";
							}else if(type == 3){
								html += "<p class='r-sale-text'>积分：<span>"+rank.income_integral+"</span></p>";
							}
							html += "</li>";
						}
						 $(".r-ranklist").append(html);
					}
					if(page.curPage+1 < page.pageCount){
						$("input.curPage").val(page.curPage+1);
					}else{
						$(".r-more").hide();	
						$(".r-no-more").show();
					}
				}
			} else {// 编辑失败
				parent.layer.alert("加载失败，请稍后重试", {
					offset : "30%"
				});
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			$(obj).find("p").slideDown().next(".spinner").hide().find("div").removeClass("loadings");
			parent.layer.alert("加载失败，请稍后重试", {
				offset : "30%"
			});
			return;
		}
	});
    
    
}



</script>
</body>
</html>