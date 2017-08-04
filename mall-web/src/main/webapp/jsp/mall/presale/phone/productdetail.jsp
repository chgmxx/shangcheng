<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
<title>拍卖详情</title>
<link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>" />
<style type="text/css">
html,body{
width: 100%}
	.Warp{
		width: 12.8rem;
		overflow: hidden;
		margin: 0 auto;
	}
	header ul li{
		height: 1.5rem;
		line-height: 1.5rem;
		border-bottom: 1px solid #F23030;
		/*border-right: 1px solid #C7C7C7;*/
		border-top: 1px solid transparent;
		border-left: 1px solid transparent;
		box-sizing: border-box;
		text-align: center;
		/* border-bottom: 1px solid #ccc; */
	}
	article{
		padding: 0.4rem;
		/* word-break: break-all;
        white-space: pre-wrap; */
        font-size : 12px;
        overflow-x: auto; 
        width: 95%!important
	}
	article div{
	  width: 100%!important;
	  margin: 0!important;
	  line-height:28px;
	 /*  overflow: auto!important  */
	  
	}
	img{
		display: block;
		max-width: 100%;
		margin: 0 auto;
	}
	.section2-con{
		text-align: center;
		margin-top: 0.7rem;
	}
	.history-title{
		background-color: #dedede;
		height: 1rem;
		line-height: 1rem;
	}
	.history-con li{
		height: 1.2rem;
		line-height: 1.2rem;
	}
	.cur{
		 border-bottom: none;
		border-top: 1px solid #F23030;
		border-left: 1px solid #F23030;
		border-right: 1px solid #F23030!important; 
		background-color: #F1312F;
		/* border-bottom: 1px solid #F23030; */
	}
	.cur a{color:#fff;}
	.flex-1 a{display: inline-block;width:100%;height: 100%;}
	.section{display: none;}
	.section.show{display:block;}
	
	header .tip{height:1.5rem;line-height:1.5rem;background-color: #f6f6f6;border-bottom:1px solid #cdcdcd;}
	header .tip span{display:inline-block;height:100%;font-size: 24px;}
	.tip .tip_left{float:left;width:50px;text-align: center;background: url("/images/mall/img/black-up.png") no-repeat center center;background-size: 28%;}
	.tip .tip_right{float:right;width:70px;text-align: right;margin-right: 20px;}
</style>
</head>

<body>
<!--加载动画-->
<section class="loading">
       <div class="load3">
           <div class="double-bounce1"></div>
           <div class="double-bounce2"></div>
       </div>
   </section>
<link id="link" rel="stylesheet" type="text/css" href="/css/mall/public.css?<%=System.currentTimeMillis()%>" />
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<div class="Warp">
	<header>
		<div class="tip">
			<span class="tip_left" onclick="javascript:location.href='/mAuction/${id }/${shopid }/${auctionId }/79B4DE7C/auctiondetail.do?uId=${userid }';">
			</span>
			<span class="tip_right"  onclick="pageclick('${pageid}')">首页</span>
		</div>
		<ul class="flex" class="">
			<li class="flex-1 <c:if test='${type == 1 }'>cur</c:if>" id="section1">
				<a href="/mAuction/${id }/${shopid }/${auctionId }/79B4DE7C/shopdetails.do?type=1&uId=${userid }">商品详情</a>
			</li>
			<li class="flex-1 <c:if test='${type == 2 }'>cur</c:if>" id="section2">
				<a href="/mAuction/${id }/${shopid }/${auctionId }/79B4DE7C/shopdetails.do?type=2&uId=${userid }">
					<c:if test="${aucType == 1}">抢拍记录</c:if>
					<c:if test="${aucType == 2}">出价记录</c:if>
				</a>
			</li>
			<li class="flex-1 <c:if test='${type == 3 }'>cur</c:if>" id="section3">
				<a href="/mAuction/${id }/${shopid }/${auctionId }/79B4DE7C/shopdetails.do?type=3&uId=${userid }">卖家承诺</a>
			</li>
			<li class="flex-1 <c:if test='${type == 4 }'>cur</c:if>" id="section4">
				<a href="/mAuction/${id }/${shopid }/${auctionId }/79B4DE7C/shopdetails.do?type=4&uId=${userid }">保证金须知</a>
			</li>
		</ul>
	</header>
	<c:if test='${type == 1 }'>
		<section id="section1Con" class="section show"> 
			<article>${obj.productDetail }</article>
		</section>
	</c:if>
	<c:if test='${type == 2 }'>
	<section id="section2Con" class="section section2-con show">
		<div class="history-title flex">
			<div class="flex-1">状态</div>
			<div class="flex-1">价格</div>
			<div class="flex-1">竞拍人</div>
			<div class="flex-2">时间</div>
		</div>
		<c:set var="noAuction" value="0"></c:set>
		<div class="history-con">
			<c:if test="${aucType == 2 && !empty offerList }">
				<c:set var="noAuction" value="1"></c:set>
				<ul class="">
					<c:forEach var="offer" items="${offerList }" varStatus="i">
					<li class="flex">
						<div class="flex-1">
							<c:if test="${i.index == 0}">领先</c:if>
							<c:if test="${i.index > 0}">出局</c:if>
						</div>
						<div class="flex-1">￥${offer.offerMoney }</div>
						<div class="flex-1 text-overflow">${offer.userName }</div>
						<div class="flex-2"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${offer.createTime }" /></div>
					</li>
					</c:forEach>
				</ul>
			</c:if>
			<c:if test="${aucType == 1 && !empty bidList }">
				<c:set var="noAuction" value="1"></c:set>
				<ul class="">
					<c:forEach var="bid" items="${bidList }" varStatus="i">
					<li class="flex">
						<div class="flex-1">
							<c:if test="${bid.aucStatus == 0}">未支付</c:if>
							<c:if test="${bid.aucStatus == 1}">抢拍成功</c:if>
							<c:if test="${bid.aucStatus == -1}">抢拍失败</c:if>
						</div>
						<div class="flex-1">￥${bid.aucPrice }</div>
						<div class="flex-1 text-overflow">${bid.userName }</div>
						<div class="flex-2"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${bid.createTime }" /></div>
					</li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
	</section>
	</c:if>
	<c:if test='${type == 3 }'>
	<section id="section3Con" class="section show">
		<article>卖家承诺：
<div>一、服务承诺</div>
  <div>A.商家承诺</div>

   <div>1.卖家承诺所发拍品均为真品。</div>
   <div>2.卖家成交不卖，卖家应将保证金退一赔一给竞买成功人。</div>
<div>二、竞拍活动基础服务</div>

   <div>1.保证金保障：卖家入驻竞拍平台需缴纳较高的经营保证金，买家维权有保障。</div>

<div>三、拍卖特色服务</div>

  <div>A. 支持线下预览</div>

      支持线下预览是指卖家已选定场地预展拍品，竞买人可在竞拍结束前，亲自或委托他人查看竞拍品实物。
      </article>
	</section>
	</c:if>
	<c:if test='${type == 4 }'>
	<section id="section4Con" class="section show">
		<article>保证金须知：
<div><strong>一、保证金的缴纳：</strong></div>

<div>&nbsp;&nbsp;保证金是竞拍人参加竞拍的凭证，如竞拍人有意参加相关商品竞拍活动，则须缴纳卖家设置的参与竞拍活动的保证金；保证金的缴纳方式为以订单的形式通过微信进行支付。</div>
<div>&nbsp;&nbsp;<strong>缴纳次数：</strong>参加升价拍或降价拍，每个商品的竞拍只需要缴纳一次保证金；</div>
<div><strong>二、保证金的返还：</strong></div>

<div><strong>在符合以下条件时将原路退还客户保证金：</strong></div>

<div>&nbsp;&nbsp;1.竞拍人未成功竞拍商品：例如在竞拍过程中始终未出价、在竞拍过程中放弃加价等情况；</div>
<div>&nbsp;&nbsp;2.竞拍人竞拍成功后并在竞拍规则规定时间内付款；</div>
<div>竞拍结束后，系统会自动在1-15个工作日内将款项内退还到支付账号中，如您缴纳的竞拍保证金符合退还条件，但是系统未予退还，您可以联系客服，我们会尽快帮您处理。</div>
<div><strong>三、保证金的扣除：</strong></div>

&nbsp;&nbsp;1.在升价拍竞拍成功之时起的72小时内未按照成交价格支付拍品货款的；在降价拍竞拍成功之时起的24小时内未按照成交价格支付拍品货款的。
如因卖家原因导致竞拍人不付款，竞拍人可在交易关闭的15天内联系客服，逾期保证金申诉不给予受理。
		</article>
	</section>
	</c:if>
</div>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>

<script type="text/javascript">
	$(function(){
		setTimeout(function() {
			$(".loading").hide();
		}, 1000);
		$('header li').click(function(){
			var liId = $(this).attr('id');
			var conId = liId + "Con";
			$('header li').css('border-right','none');
			$(this).addClass('cur').siblings().removeClass('cur');
			$('.section').hide();
			$('#' +　conId).show();
		});
	});
	function pageclick(obj){
		if(obj==''||obj==null||obj==undefined||obj==0||obj=='0'){
			alert("店面未设置微商城主页面或者微商城主页面已删除");
		}else{
			window.location.href = "mallPage/"+obj+"/79B4DE7C/pageIndex.do";
		}
	}
</script>
</body>
</html>