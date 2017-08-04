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
<title>购物车</title>
<link rel="stylesheet" type="text/css" href="/css/common/init.css?<%=System.currentTimeMillis()%>" />
	   
<style type="text/css">
html, body {
	height: 100%;
	width: 100%;
	font-size: 30px !important;
	position: relative;
	margin: 0;
	padding: 0;
	background-color: #f0f2f5;
}

.Warp {
	margin: 0 auto;
	width: 870px;
	height: 100%;
}

.header {
	width: 100%;
	height: 78px;
	line-height: 78px;
	background-color: #f0f2f5;
	position: fixed;
	top: 0;
}

.check {
	width: 34px;
	height: 34px;
	border-radius: 50%;
	border: 2px solid #484848;
}

.header .all-text {
	display: inline-block;
	padding-left: 38px;
}

.main {
	margin-bottom: 110px;
	margin-top: 78px;
}

.main .main-list {
	height: auto;
	padding: 58px 0px;
	border-top: 1px solid #dfdfdf;
	border-bottom: 1px solid #dfdfdf;
	margin-bottom: 10px;
	background-color: #FFFFFF;
}

.check-box {
	float: left;
	width: 78px;
	height: 100%;
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-pack: center;
	-webkit-box-align: center;
	display: box;
	box-orient: horizontal;
	box-pack: center;
	box-align: center;
}

.main .mall-item {
	display: inline-block;
	vertical-align: middle;
}

.main .mall-img {
	display: inline-block;
	width: 165px;
}

.main .mall-img img {
	width: 123px;
	max-height: 162px;
	display: block;
	margin: 0 auto;
}

.main .mall-info {
	display: inline-block;
	width: 550px;
	vertical-align: top;
	font-size: 28px;
}

.main .mall-info .red-txt {
	color: #F20000;
	font-size: 30px;
	line-height: 1.6;
}

.main .mall-info span {
	margin-right: 40px;
}

.main .mall-info p.title {
	max-height: 74px;
	 overflow: hidden;
	font-size: 28px;
	 display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-line-clamp: 2;
	text-overflow: ellipsis;
	
}

em {
	font-style: normal
}

.gw_num {
	border: 2px solid #dfdfdf;
	width: 203px;
	line-height: 50px;
	overflow: hidden;
	margin-left: 300px;
}

.gw_num em {
	display: block;
	height: 50px;
	width: 64px;
	float: left;
	color: #7A7979;
	border-right: 2px solid #dfdfdf;
	text-align: center;
	cursor: pointer;
	font-size: 35px;
}

.gw_num .num {
	display: block;
	float: left;
	text-align: center;
	width: 70px;
	font-style: normal;
	font-size: 26px;
	line-height: 50px;
	border: 0;
}

.gw_num em.add {
	float: right;
	border-right: 0;
	border-left: 2px solid #dfdfdf;
}

img.check-mark {
	display: block;
	margin: 3px auto;
}

.footer {
	width: 830px;
	height: 70px;
	position: fixed;
	bottom: 0;
	border-top: 1px solid #dfdfdf;
	background-color: #FFFFFF;
	padding: 20px;
	color: #484848;
}

.footer .footer-left {
	display: inline-block;
	vertical-align: top;
	width: 580px;
}

.footer .footer-left .price-box {
	color: #f23030;
}

.footer .footer-left .red-txt label {
	color: #484848 !important;
}

.footer .footer-right {
	display: inline-block;
	vertical-align: top
}

.go-order {
	width: 209px;
	height: 70px;
	line-height: 70px;
	text-align: center;
	color: #FFFFFF;
	background-color: #f23030;
	display: inline-block;
	border-radius: 8px;
}

.check {
	width: 50px;
	height: 50px;
	border: none;
	margin-left: 22px;
	vertical-align: middle;
	appearance: none;
	-webkit-appearance: none;
	background: url(/images/mall/img/check.png) no-repeat center center;
	background-size: 100%;
}

.checked {
	width: 50px;
	height: 50px;
	border: none;
	margin-left: 22px;
	vertical-align: middle;
	appearance: none;
	-webkit-appearance: none;
	background: url(/images/mall/img/checked.png) no-repeat center center;
	background-size: 100%;
}

.header .operate {
	margin-left: 460px;
}

.header .operate label {
	padding: 0px 20px;
}

.header .operate .delete {
	
}
.left{
	float:left;
	}
.attributes{
		line-height: 2.5;
	}
	
.attributes	span{
   display: inline-block;
   vertical-align: top;
   width: 48%;
   margin-right: 0!important;
}
.box{
margin:5px 0px

}
.shop-name{
  color: #f23030
}
.error_tip{
    border: 1px solid #dfdfdf;
    margin: 10px 0px;
    background-color: #FFFFFF;
    line-height: 81px;
    text-align: center;
 }
 .sx_div{text-align: center;}
 .sx_a{
 	margin: 20px 0px;
    display: inline-block;
    border: 1px solid #F20000;
    color: #F20000;
    width: 214px;
    line-height: 50px;
    border-radius: 5px;
 }
.c{clear: both;}
</style>
</head>
 <script type="text/javascript">
 $(function(){
   var checkobj = $("#check-all");
   checkobj.attr("checked","");
   showMore(checkobj); 
   });
  </script>
<body>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/common/reset.css" />
<link rel="stylesheet" type="text/css" href="/css/plugin/swiper.min.css" />

<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/swiper.min.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
	    <div class="Warp">
	    	<div class="header">
	    		<input type="checkbox" name="check-all" id="check-all" class="check check-all" value="" onclick="showMore(this)"   />
	    		<label for="" class="all-text">全选</label>
	    		<span class="operate">
	    			<label for="" class="oper-edti" onclick="open_edti(this)" style="cursor: pointer;" option="0">编辑</label>
	    			<label for="" class="oper-delete" style="display:none" id="oper-delete" onclick="shopdelect()">删除</label>
	    		</span>
	    	</div>
	    	
	    	<section class="main">
	    		<c:set var="index" value="0"></c:set>
	    	    <c:forEach items="${list }" var="shop"> 
	    	      <c:if test="${shop.code == 0 && index == 0}">
	    			  <c:set var="index" value="${index+1 }"></c:set>
		    	      <div class="error_tip" >
		    	      	<label>以下商品无法一起购买</label>
		    	      </div>
	    	      </c:if>
	    	      <div class="main-list <c:if test="${shop.code == 0}">isSX</c:if>" id="${shop.id }">
	    	      <c:if test="${shop.code == 1}">
	    	      <input type="checkbox" name="check" id="check" value="" class="check"  option="${shop.id}"/>
	    	      </c:if>
	    			<div class="mall-item">
    					<div class="mall-img">
	    					  <a href="mallPage/${shop.product_id}/${shop.shop_id}/79B4DE7C/phoneProduct.do"><img src="${http }${shop.image_url}"/></a>
	    				</div>
	    				<div class="mall-info">
	    					<a href="mallPage/${shop.product_id}/${shop.shop_id}/79B4DE7C/phoneProduct.do"><p class="title">${shop.pro_name}</p></a>
	    					<div class="attributes">
	    					   <span>${shop.product_speciname} </span>
	    					  <a href="javascript:void(0)" onclick="pageclick('${shop.pageid}')"> <span class="shop-name">${shop.sto_name}</span></a>
	    					</div>
	    					<div class="box">
	    						<label class="red-txt left">
	    							<div><em>￥</em><span class="price">${shop.primary_price}</span></div>
	    							<c:if test="${discount < 1 && discount > 0 && !empty shop.hyPrice}">
	    								<div><em>会员价：￥</em><span class="price">${shop.hyPrice}</span></div>
	    							</c:if>
	    							<div></div>
	    						</label>
		    				    <div class="gw_num" code="${shop.code }" msg="${shop.msg }">
									<em class="jian">-</em>
									<input type="tel" value="${shop.product_num}" class="num" maxlength="6"/>
									<em class="add">+</em>
								</div>
	    					</div>
	    					<c:if test="${shop.code == 0 && shop.msg != ''}">
	    					<div class="box">
	    						<label class="red-txt left">${shop.msg}</label>
	    					</div>
	    					</c:if>
	    				</div>
	    			</div>
	    			<div class="c"></div>
	    		   </div>
	    	    </c:forEach>
	    		<c:if test="${index > 0}">
	    			<div class="sx_div">
	    				<a href="javascript:void(0);" class="sx_a clearCart">清空失效商品</a>
	    			</div>
	    		</c:if>
	    		<div style="height: 100px; width:100%"></div>
	    	</section>
	    	<footer class="footer" id="footer">
	    		<div class="footer-left">
	    			<div class="line-text red-txt">
	    				总计 
	    				<span class="price-box">
	    					￥<span class="" id="priceTotal">0.00</span>
	    				</span>
	    				 
	    			</div>
	    		    <div class="line-text">
	    		    	(共<span id="sum-num">0</span>件)
	    		    </div>
	    		</div>
	    		<div class="footer-right">
	    			<a href="javascript:void(0)" class="go-order" onclick="go_order()">去结算</a>
	    		</div>
	    	</footer>
	    </div>
	<!--遮罩层-->
		<div class="fade" id="fade" ></div>    
		<form id="queryForm" method="post" action="/mallPage/79B4DE7C/shoppingorder.do">
			<input type="hidden" id="arrayJson" name="arrayJson">
			<input type="hidden" id="totalnum" name="totalnum">
			<input type="hidden" id="totalprice" name="totalprice">
		</form>
		 <form id="queryText" method="post" action="/phoneOrder/79B4DE7C/toOrder.do">
			<input type="hidden" id="json" name="data">
			<input type="hidden" id="type" name="type" value="1">
		</form>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
<script> 

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
//店面跳转
function pageclick(obj){
	if(obj==''||obj==null||obj==undefined){
		alert("店面未设置微商城主页面或者微商城主页面已删除");
	}else{
		window.location.href = "mallPage/"+obj+"/79B4DE7C/pageIndex.do";
	}
}
	$(window).load(function() {
		var a = $(window).width(), b = $(window).height(), d = 870, meta = $("#meta");
		meta.attr("content", "width=870,initial-scale=" + a/ d + ", minimum-scale=" + a / d+ ", maximum-scale=" + a / d+ ", user-scalable=no");
		setTimeout(function() {
			$(".loading").hide();
		}, 1000);
	});

	$(document).ready(function() {
		//加的效果
		$(".add").click(function() {
			var code = $(this).parent().attr("code");
			if(code == 1){
				var n = $(this).prev().val();
				var num = parseInt(n) + 1;
				if (num == 0) {
					return;
				}
				$(this).prev().val(num);
				$(this).parent().parent().parent().parent().find(".check").addClass("checked");
				changeTotle();
			}else{
				var msg = $(this).parent().attr("msg");
				if(msg != null && msg != ""){
					alert(msg+",请立即清空");
					$(document).scrollTop($(document).height());
				}else{
					alert("该商品已经失效，请立即清空");
				}
			}
		});
		//减的效果
		$(".jian").click(function() {
			var code = $(this).parent().attr("code");
			if(code == 1){
				var n = $(this).next().val();
				var num = parseInt(n) - 1;
				if (num == 0) {
					return
				}
				$(this).next().val(num);
				$(this).parent().parent().parent().parent().find(".check").addClass("checked");
				changeTotle();
			}else{
				var msg = $(this).parent().attr("msg");
				if(msg != null && msg != ""){
					alert(msg+",请立即清空");
					$(document).scrollTop($(document).height());
				}else{
					alert("该商品已经失效，请立即清空");
				}
			}
		});
		$("span.price").each(function(){
			var price = $(this).html();
			if(typeof price != "undefined" && price != null && price != ""){
				price = parseFloat(price).toFixed(2);
			}
		});
	});

	function changeTotle() {
		var total_price = 0;
		var total_count = 0;
		$("input[name='check']").each(function() {
			if ($(this).hasClass("checked")) {
				var obj1 = $(this).next(".mall-item").find(".num").val();
				total_count += parseInt(obj1);
				var obj2 = $(this).next(".mall-item").find(".price").text();
				obj1 = parseFloat(obj1);
				
				total_price += parseFloat(obj2)*parseInt(obj1);
			}
		});
		$("#sum-num").text(total_count);//将计算出的总金额显示
		$("#priceTotal").text(total_price.toFixed(2));//将计算出的总数量显示
	}
	/*全选/反选*/	
	function showMore(obj){
		if($(obj).is(":checked")){
			$("input[name='check']").each(function(){
				$(".check").addClass("checked")
				$(this).attr("checked","checked");
				
			});
		}else{
			$("input[name='check']").each(function(){
				$(".check").removeClass("checked")
				$(this).removeAttr("checked");
			});
		}
		changeTotle();
		/* if (obj.checked) {  
	      for(var i=0; i<_array.length; i++){
	      	_array[i].checked = true;
	      	$(".check").addClass("checked");
	      	changeTotle();
	      }
	    }else{
	       for(var i=0; i<_array.length; i++){
	      	_array[i].checked = false;
	      	$(".check").removeClass("checked");
	      	changeTotle();
	      }
	    } */
	}
	/*单选*/
	$(function(){
	 $("input[name='check']").click(function(){
		 if($(this).hasClass("checked")){
			 $(this).removeClass("checked");
			 $("#check-all").removeClass("checked");
		 }else{
			 $(this).addClass("checked");
			 allcheck();
		 }
	      changeTotle();
	    });
	});
	//商品是否全选时，全选勾住全选按钮
   function allcheck(){
	   var shif = true;//是否全选
	   $("input[name='check']").each(function(){
		   if($(this).hasClass("checked")){
			   
		   }else{
			   shif = false;
			   return;
		   }
		   
	   })
	   if(shif){
		   $("#check-all").addClass("checked");
	   }
   }
   var idoption = "0";//定义编剧前，选中的产品 option，数据与，分开	
	//编辑前先记住那些是之前编剧的数据
	function open_edti(obj){
		var option = $(obj).attr("option");
		if(option==0){
		 $("input[name='check']").each(function(){
			 if($(this).hasClass("checked")){
				 idoption +=","+$(this).attr("option");
				 $(this).removeClass("checked");
			 }
		 })
		  $("#check-all").removeClass("checked");
		 $("#footer").css("display","none");
	
	    $("#oper-delete").show();
		 $(obj).text("完成");
		 $(obj).attr("option","1");
		}else{
			var idopt = idoption.split(",");
			 $("input[name='check']").each(function(){
			for (var i=1 ; i< idopt.length ; i++)
			 {
				if( idopt[i]==$(this).attr("option")){
					$(this).addClass("checked");
					allcheck();
					changeTotle();
				}
			 }
			})
			 $("#oper-delete").css("display","none");
			 $(obj).text("编辑");
			 $(obj).attr("option","0");
			 $("#footer").css("display","block");
		}
	}
   //商品移除购物车
	function shopdelect(){
		var delects = "0";
		 $("input[name='check']").each(function(){
			 if($(this).hasClass("checked")){
				 delects += ","+$(this).attr("option");
			 }
		 })
		 if(delects=="0"){
			 alert("你还选择移除的商品");
			return;
		 }else{
			 var fade=$("#fade");
				fade.show();
				 $.ajax({
						type : "post",
						url : "/mallPage/79B4DE7C/shoppingdelect.do",
						data : {
							delects:delects
						},
						async : false,
						dataType : "json",
						success : function(data) {
							fade.hide();
							var error = data.error;
							if(error==0){
								location.href = window.location.href;
							}else{
								alert("删除购物车商品失败");
							}
						}
				})
		 }
		 
	}
   //清空失效商品
   $(".clearCart").click(function(){
	   var delects = "0";
	   $(".main-list.isSX").each(function(){
		   delects += ","+ $(this).attr("id");
	   });
	   if(delects != ""){
		   if(confirm("确认清空失效商品吗？")){
			   var fade=$("#fade");
				fade.show();
				 $.ajax({
						type : "post",
						url : "/mallPage/79B4DE7C/shoppingdelect.do",
						data : {
							delects:delects
						},
						async : false,
						dataType : "json",
						success : function(data) {
							fade.hide();
							var error = data.error;
							if(error==0){
								location.href = window.location.href;
							}else{
								alert("确认清空失效商品失败");
							}
						},error:function(){
							fade.hide();
						}
				})
		   }
	   }
   });
	
   
   function go_order(){
	   var array = new Array();//声明数组
	   $("input[name='check']").each(function(){
			 if($(this).hasClass("checked")){
				 var hs = {id:$(this).attr("option"),num:$(this).parents().find(".num").val(),check:0};/*check==0 代表选中，1代表未选中  */
				 var length = array.length;
				 array[length]=hs;
			 }else{
				 var hs = {id:$(this).attr("option"),num:$(this).parents().find(".num").val(),check:1};
				 var length = array.length;
				 array[length]=hs;
			 }
		 })
	   if(array.length==0){
		   alert("你还未选择商品");
		   return;
	   }else{
		  
		   $("#arrayJson").val(JSON.stringify(array));
		   $("#totalnum").val($("#sum-num").text());
		   $("#totalprice").val($("#priceTotal").text());
		   $("#queryForm").ajaxSubmit({
		        type: "post",		       
		        dataType : "json",  	
		        url: "/mallPage/79B4DE7C/shoppingorder.do",
		        success: function(data) {
		        	if(data.error == "0"){
		        		$("#json").val(data.result)
		        		$("#queryText").submit();
		        	}else{
		        		alert("结算购物车失败，请稍后结算");
		        	}
		        }
			}); 
	   }
	   
   }
   var numDefault = 0;
	$(".num").focus(function(){
		numDefault = $(this).val();
   });
   $(".num").keyup(function(){
	   var val = $(this).val();
	   var numTest = /^\d{0,6}$/;
	   if(!numTest.test(val)){
		   $(this).val(numDefault);
	   }else{
		   numDefault = $(this).val();
	   }
   });
</script>
</html>