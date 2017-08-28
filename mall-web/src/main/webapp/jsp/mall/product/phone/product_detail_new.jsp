<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<div class="module">
	<nav>
		<ul class="flex moduleChange">
			<li class="flex-1"><a href="javascript:;" id="nav1" class="cur">商品详情</a></li>
			<li class="flex-1"><a href="javascript:;" id="nav2" class="">规格参数</a></li>
			<li class="flex-1"><a href="javascript:;" id="nav3" class="">评价</a></li>
		</ul>
	</nav>
	<!--<div class="packingList pad">
				<div class="title">
					<hr />
				    <span>包装清单</span>
				    <hr />
				</div>
			    <div class="main">
			    	休闲衣x1、包装包x1
			    </div>
			</div>-->
	<!--参数规格-->
	<div class="changeMain" id="nav1Main" style="display: block;min-height: 30px;">
		<%-- <c:if
			test="${!empty mapmessage.product_detail && mapmessage.product_detail != ''}">
				${mapmessage.product_detail }
				</c:if> --%>
	</div>
	<div class="packingList pad changeMain" id="nav2Main"
		style="display: none;">
		<div class="title">
			<hr>
			<span>商品参数</span>
			<hr>
		</div>
		<div class="main paramsDiv" style="min-height: 30px;"></div>
	</div>
	<!--参数规格结束-->
	<!--评价-->
	<div id="nav3Main" class="changeMain" style="display: none;">
		<ul class="flex assess">
			<li class="flex-1"><a href="javascript:;" id="assess1"
				class="curAssess">全部</a></li>
			<li class="flex-1 feelLi"><a href="javascript:;" id="assess2">好评(<em id="1">0</em>)</a></li>
			<li class="flex-1 feelLi"><a href="javascript:;" id="assess3">中评(<em id="0">0</em>)</a></li>
			<li class="flex-1 feelLi"><a href="javascript:;" id="assess4">差评(<em id="-1">0</em>)</a></li>
		</ul>
		<div id="assess1Main" class="assessMain commentDiv" style="display: block;">
			<ul>
				<!-- <li>
					<div class="itemInfo flex">
						<img src="img/0.jpg">
						<div>
							<p>小****多</p>
							<p class="grey-text">2016-09-06 12:00:00 颜色分类：黑色 码数：s码</p>
						</div>
					</div>
					<div class="itemTxt">很不错呀，货真价实。</div>
					<div class="itemPic">
						<img src="img/0.jpg">
					</div>
				</li>
				<li>
					<div class="itemInfo flex">
						<img src="img/0.jpg">
						<div>
							<p>小****多</p>
							<p class="grey-text">2016-09-06 12:00:00 颜色分类：黑色 码数：s码</p>
						</div>
					</div>
					<div class="itemTxt">很不错呀，货真价实。</div>
					<div class="itemPic">
						<img src="img/0.jpg">
					</div>
				</li> -->
			</ul>
		</div>
		<div id="assess2Main" class="assessMain"></div>
		<div id="assess3Main" class="assessMain"></div>
		<div id="assess4Main" class="assessMain"></div>
	</div>
	<!--评价结束-->
</div>

<div class="other-box">
	<div class="other-list">
		<a href="javascript:void(0);" onclick="pageclick('${pageid}')" class="other-a">店铺主页</a>
		<a href="javascript:void(0);" onclick="PersonalCenter()" class="other-a">会员中心</a>
		<c:if test="${!empty qrcode_url }">
		<a href="javascript:void(0);" class="other-a" onclick="showCode();">关注我们</a></li>
		</c:if>
		<a href="/mallPage/${shopid }/79B4DE7C/storesAll.do?uId=${userid}" class="other-a">线下门店</a>
	</div>
	<div class="other-duofen">
		
	<c:if test="${!empty isAdvert }">
	   	<div class="isAdvert">
	   		<jsp:include page="/jsp/common/technicalSupport.jsp"></jsp:include>
	   	</div>
	</c:if>
	</div>
</div>
<div class="cd-code" style="display: none;">
	<c:if test="${!empty qrcode_url }">
	<div class="code-bg"><img src="${http }${qrcode_url }" class="codeImg"/></div>
	</c:if>
	<div class="cd-bottom" onclick="closeCode(this)">关闭</div>
</div>
<script src="/js/plugin/jquery-1.8.3.min.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript">
$(function(){
	setTimeout(function(){
		var detail = "${proDetail}";
		if(detail != null && detail != '' && typeof(detail) != "undefined"){
			detail = detail.replace(/&quot;/g,"\"").replace(/&apos;/g,"'");
			$(".changeMain#nav1Main").html(detail);
		}
	},500) 
})
</script>