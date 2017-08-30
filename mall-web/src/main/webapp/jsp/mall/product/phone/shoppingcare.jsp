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
<style>
	.a{height: 100%; overflow: hidden;}
</style>
</head>
 <script type="text/javascript">
/*  $(function(){
   var checkobj = $("#check-all");
   checkobj.attr("checked","");
   showMore(checkobj); 
   }); */
  </script>
 
<body style="-webkit-overflow-scrolling: touch;">
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>
<link rel="stylesheet" type="text/css" href="/css/mall/public2.css?<%=System.currentTimeMillis()%>" />
<link rel="stylesheet" type="text/css" href="/css/mall/product/scStyle.css" />

<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/swiper.min.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>

<!--输入框 遮罩层-->
<section class="cd-popup" id="cd-input">
    <div class="cd-main">
        <div class="cd-content">
            <p class="f3 lt1">修改购买数量</p>
            <div class="cd-item">
                    <i class="bProp minus-icon noDelay" onclick="minusOper(this)"></i>
                    <input type="text" class="item-input2" value=0 oninput="strictStock(this)">
                    <i class="bProp add-icon noDelay" onclick="addOper(this)"></i>
            </div>
        </div>
        <div class="cd-bottom">
            <a href="javascript:void(0)" class="cd-btn btnCancel" onclick="closeLayer(this)">取消</a>
            <a href="javascript:void(0)" class="cd-btn btnOkay" onclick="numOper(this)">确定</a>
        </div>
    </div>
</section>
<div class="wWrapper">
    <nav class="sc-nav">
        <ul class="nav-ul clearfix">
            <li class="nav-li">
                <a href="/mallPage/79B4DE7C/shoppingcare.do?member_id=${memberId }&uId=${userid}" class="nav-a <c:if test='${type == 0 }'>nav-active</c:if>" item="1" onclick="switchNav(this)">购物车</a>
            </li>
            <li class="nav-li">
                <a href="/mallPage/79B4DE7C/shoppingcare.do?member_id=${memberId }&type=1&uId=${userid}" class="nav-a <c:if test='${type == 1 }'>nav-active</c:if>" item="2" id="ws-nav" onclick="switchNav(this)">批发购物车</a>
            </li>
        </ul>
    </nav>
    <div class="sc-edit clearfix">
        <div class="ck-hei-1 ck-icon " id="ck-all" onclick="isAllCheck(this)">
            全选
        </div>
        <div class="r">
            <span edit="0" onclick="editOper(this)">编辑</span>
            <div class="dn">
                <span edit="1" onclick="editOper(this)">完成</span>
                <span class="ml-1" onclick="shopdelect();">删除</span>
            </div>
        </div>
    </div>
    <c:set var="index" value="0"></c:set>
    <c:set var="shopId" value="0"></c:set>
    <c:if test="${!empty list }">
 	<c:forEach items="${list }" var="shop"> 
 		<c:if test="${shopId != shop.shop_id }">
    		<c:set var="shopId" value="${shop.shop_id }"></c:set>
 		</c:if>
    <div class="sc-box">
        <div class="box-top">
            <div class="ck-hei-2 ck-icon " onclick="isStoreCheck(this)"></div>
            <a href="javascript:void(0)" class="ck-dp"  onclick="pageclick('${shop.pageid}')">${shop.sto_name}</a>
        </div>
        <div class="box-list">
        	<c:if test="${!empty shop.proList }">
        		<c:forEach var="pro" items="${shop.proList }">
            <div class="box-main df <c:if test='${pro.pro_type == 2 || pro.pro_type == 0}'>box-mixed</c:if> <c:if test='${pro.pro_type == 1}'>box-hand</c:if>" <c:if test="${pro.pro_type == 1}">over="0"</c:if>
            	types="${pro.pro_type }">
               	<c:if test="${pro.pro_type == 1 || pro.pro_type == 0}">
                   	<div class="ck-hei-3 ck-icon  as check_div check_pro" id="${pro.id }"  onclick="isSpecCheck(this)"></div>
                </c:if>
                <div class="box-item">
                    <div class="item-info df">
                    	<c:if test="${pro.pro_type == 2  }">
                    	<div class="ck-hei-3 ck-icon  as check_div check_pro" id="${pro.id }"  onclick="isSpecCheck(this)"></div>
                    	</c:if>
                    	<c:set var="urls" value="/mallPage/${pro.product_id}/${pro.shop_id}/79B4DE7C/phoneProduct.do"></c:set>
                    	<c:if test="${!empty pro.sale_member_id }">
                    		<c:if test="${pro.sale_member_id > 0 }">
                    			<c:set var="urls" value="${urls }?saleMemberId=${pro.sale_member_id }"></c:set>
                    		</c:if>
                    	</c:if>
                    	
                    	<!-- 商品图片 -->
                        <div class="item-bg <c:if test="${pro.pro_type == 0 }">as</c:if>" 
	                        style="background-image: url('${http }${pro.image_url}');"
	                        onclick="javascript:location.href='${urls}'"></div>
                        <div class="item-text clearfix">
                        	<div class="item-text-left" <c:if test="${pro.pro_type != 0 }">style="width:auto;float:none;"</c:if> >
	                             <!-- 商品名称 -->
                            	<p class="mb-1 multiLine" <c:if test="${pro.pro_type == 1 }">style="margin-bottom:0;"</c:if>>
                            		<a href="${urls}">${pro.pro_name}</a>
                            	</p>
                            	
	                            <!-- 批发提醒 -->
	                            <small class="item-tip">该商品不满足批发条件</small>
	                            <c:if test="${pro.pro_type == 0 }">
	                            	<c:if test="${discount == 1 || discount == 0 || empty pro.hyPrice}">
	                            		<p class="cr-1">&yen;<span class="split-ele item-price">${pro.price}</span></p>
	                            	</c:if>
	                            	<c:if test="${discount < 1 && discount > 0 && !empty pro.hyPrice}">
	                            		<p class="cr-1">&yen;<span class="split-ele ">${pro.primary_price}</span></p>
	                               		<p class="cr-1">会员价：&yen;<span class="split-ele item-price">${pro.hyPrice}</span></p>
	                                </c:if>
	                            </c:if>
	                            <c:if test="${pro.pro_type == 2 && empty pro.proSpecStr }">
	                            	<p class="cr-1">&yen;<span class="split-ele item-price">${pro.price}</span></p>
	                            </c:if>
	                        
                         	</div>
                         	<c:set var="invNum" value="${pro.stockNum }"></c:set>
                         	<c:if test="${!empty pro.inv_num }">
                         		<c:set var="invNum" value="${pro.inv_num }"></c:set>
                         	</c:if>
                            <c:if test="${pro.pro_type == 0 || (pro.pro_type == 2 && empty pro.proSpecStr)}">
	                            <span class="item-oper">
	                                <i class="iProp minus-icon noDelay" onclick="minusOper(this)"></i>
	                                <!--stock 为库存量-->
	                                <input type="text" class="item-input num pro_inp" value="${pro.product_num}" stock="${invNum }" proid="${pro.product_id }" id="${pro.id }"
	                                max="${!empty pro.maxBuy?pro.maxBuy:0 }" onclick="showLayer(this)">
	                                <i class="iProp add-icon noDelay" onclick="addOper(this)"></i>
	                            </span>
                            </c:if>
                            <c:if test="${pro.pro_type == 1 }">
                            	<div class="clearfix">
	                            	<span class="r">
			                            <span class="oper-btn toMinus noDelay" onclick="minusByHand(this)">减一手</span>
			                            <span class="oper-btn toAdd noDelay" onclick="addByHand(this)">加一手</span>
			                        </span>
		                        </div>
                            </c:if>
                        </div>
                       
                    </div>
                     <!-- 手批规格 -->
	                <c:if test="${pro.pro_type == 1  && pro.proSpecStr != null}">
	                	<div class="item-spec ">
		                	<c:if test="${pro.proSpecStr != null }">
		                     <c:forEach var="spec" items="${pro.proSpecStr }" varStatus="i">
			                     <c:set var="specVal" value="${spec.value }"></c:set>
			                     <c:if test="${i.index == 0 }">
		                     		<span>${specVal.specName }：</span>
		                     	 </c:if>
			                     <span class="pro_spec_div" specIds="${spec.key }" values="${specVal.value }" proType="${pro.pro_type }" names="${specVal.specName }">
			                     	${specVal.value } x <span class="item-num mgr">${specVal.num }</span>&yen;<span class="item-price">${specVal.price }</span><input type="hidden" class="item-stock" value="${specVal.invNum }">
			                     </span>
			                     <c:if test="${i.index+1 <  pro.proSpecStr.size()}">、</c:if>
		                     </c:forEach>
		                     </c:if>
	                    </div>
	                </c:if>
                    <!-- 普通商品规格 -->
                    <c:if test="${pro.pro_type == 0 && pro.product_speciname != ''}">
                    	<div class="item-spec">${pro.product_speciname }</div>
                    </c:if>
                    <!-- 混批规格 -->
                   <c:if test="${pro.pro_type == 2 && pro.proSpecStr != null}">
	                <div class="item-detail">
	                	<c:if test="${pro.proSpecStr != null }">
	                		<c:forEach var="spec" items="${pro.proSpecStr }" varStatus="i">
	                			<c:set var="specVal" value="${spec.value }"></c:set>
			                	<div class="df _spec pro_spec_div" specIds="${spec.key }" values="${specVal.value }" proType="${pro.pro_type }" names="${specVal.specName }">	
			                		<div class="ck-hei-3 ck-icon as check_pro_spec"  onclick="isItemCheck(this)"></div>
			                		<div class="item-spec df">
		                                <div class="item-text-wid">
		                                    <p class="mb-1">${specVal.specName }：${specVal.value } X <span class="item-num">${specVal.num }</span></p>
		                                    <p class="cy_1">&yen;<span class="item-price">${specVal.price }</span></p>
		                                </div>
		                                <span class="item-oper as">
		                                    <i class="iProp minus-icon noDelay" onclick="minusOper(this)"></i>
		                                    <input type="text" class="item-input num pro_inp" value="${specVal.num}" stock="${specVal.invNum }" proid="${pro.product_id }" id="${pro.id }"
		                                     max="${!empty pro.maxBuy?pro.maxBuy:0 }" onclick="showLayer(this)">
		                                    <i class="iProp add-icon noDelay" onclick="addOper(this)"></i>
		                                </span>
		                            </div>
			                    </div>
	                    	</c:forEach>
		                </c:if>
	                </div>
	                </c:if>
                </div>
             </div>
            	</c:forEach>
        	</c:if>
        </div>
        <div class="box-bottom">
            <span class="xj-num">0</span>件， 小计： <span class="cr-1">&yen; <span class="xj-price">0.00</span></span>
        </div>
    </div>
    </c:forEach>
    </c:if>
    
    <!--失效商品-->
    <c:if test="${!empty sxList}">
    <div class="box-top-2">以下商品无法一起购买</div>
	 	<c:forEach var="sxShop" items="${sxList }">
    <div class="sc-disable">
        <div class="box-top">
            <a href="javascript:void(0)" class="ck-dp"  onclick="pageclick('${sxShop.pageid}')" style="width: 86%;">${sxShop.sto_name}</a>
        </div>
        <div class="box-main" >
        	<c:if test="${!empty sxShop.proList }">
        		<c:forEach var="pro" items="${sxShop.proList }">
        			<div class="box-item isSX_div"  id="${pro.id }" style="padding: .2rem 0 .2rem 0;">
			            <div class="item-info df">
			                <div class="item-bg" style="background-image: url('${http }${pro.image_url}');"></div>
			                <div class="item-text">
			                    <span><a href="/mallPage/${pro.product_id}/${pro.shop_id}/79B4DE7C/phoneProduct.do">${pro.pro_name}</a></span>
			                    <c:if test="${pro.msg != '' }">
					            <div class="red-txt">${pro.msg}</div>
					            </c:if>
					            <c:if test="${pro.pro_type == 0 && pro.product_speciname != ''}">
				                	<div class="item-detail">${pro.product_speciname } </div>
				                </c:if>
                           		<p class="cr-1" style="line-height: 25px;">&yen;<span class="split-ele item-price">${pro.price} </span>  x ${pro.product_num }</p>
			                </div>
			            </div>
			            <c:if test="${pro.pro_type == 2 && pro.proSpecStr != null}">
		                <div class="item-detail">
		                	<c:if test="${pro.proSpecStr != null }">
		                	<c:forEach var="spec" items="${pro.proSpecStr }" varStatus="i">
		                		<c:set var="specVal" value="${spec.value }"></c:set>
		                		 <c:if test="${i.index == 0 }">
		                     		<span>${specVal.specName }：</span>
		                     	 </c:if>
			                    <span>${specVal.value }</span>
			                    x <span class="item-num">${specVal.num }</span>
			                    &nbsp;&nbsp;&yen; <span class="item-price">${specVal.price }</span>
			                    <c:if test="${i.index+1 <  pro.proSpecStr.size()}">、</c:if>
		                    </c:forEach>
			                </c:if>
		                </div>
		                </c:if>
		                <c:if test="${pro.pro_type == 1  && pro.proSpecStr != null}">
		                	<div class="item-detail">
			                	<c:if test="${pro.proSpecStr != null }">
			                     <c:forEach var="spec" items="${pro.proSpecStr }" varStatus="i">
				                     <c:set var="specVal" value="${spec.value }"></c:set>
				                     <c:if test="${i.index == 0 }">
			                     		<span>${specVal.specName }：</span>
			                     	 </c:if>
				                     <span>${specVal.value } x<span class="item-num mgr">${specVal.num }</span>&yen;<span class="item-price">${specVal.price }</span><input type="hidden" class="item-stock" value="5"></span>
				                     <c:if test="${i.index+1 <  pro.proSpecStr.size()}">、</c:if>
			                     </c:forEach>
			                     </c:if>
			                 </div>
		                </c:if>
		            </div>
            	</c:forEach>
        	</c:if>
        </div>
    </div>
    </c:forEach>
    <div class="tc">
        <a href="javascript:void(0);" class="clear-btn clearCart">清空失效商品</a>
    </div>
    </c:if>
</div>
<input type="hidden" class="leastHand" value="<c:if test='${!empty spHand }'>${spHand }</c:if><c:if test='${empty spHand }'>0</c:if>">
<input type="hidden" class="leastNum" value="<c:if test='${!empty hpNum }'>${hpNum }</c:if><c:if test='${empty hpNum }'>0</c:if>">
<input type="hidden" class="leastCost" value="<c:if test='${!empty hpMoney }'>${hpMoney }</c:if><c:if test='${empty hpMoney }'>0</c:if>">

<div class="item-term"></div>
<footer class="wFooter clearfix">
<!-- <div class="l">
            <p>总计  <span class="cr-1">&yen;<span class="zjPrice">0.00</span></span></p>
            <p>(共 <span class="zjNum">0</span> 件)</p>
        </div> -->

    <div class="l">
    	<c:set var="hpMoney" value="0"></c:set>
    	<c:set var="hpNum" value="1"></c:set>
    	<c:set var="spNum" value="1"></c:set>
    	 <p>总计  <span class="cr-1">&yen;<span class="zjPrice">0.00</span></span></p>
         <p>(共 <span class="zjNum">0</span> 件)</p>
       <%--  <p>共<span class="zjNum">0</span>件
    	<c:if test="${type == 1 }">
        ,还差 <span class="restNum"><c:if test='${!empty hpNum }'>${hpNum }</c:if></span>件可混批
        </c:if>
        </p>
        <p>总价： <span class="cr-1">&yen;<span class="zjPrice">0.00</span></span></p> --%>
    </div>
    <div class="r">
        <a href="/mallPage/${shopId }/79B4DE7C/shoppingall.do" class="base-btn toGo">继续选购</a>
        <a href="javascript:void(0)" class="base-btn tActive goBuy" onclick="toCount(this)">去结算</a>
    </div>
</footer>
<div class="fade" id="fade" ></div>    
<form id="queryForm" method="post" action="/mallPage/79B4DE7C/shoppingorder.do">
	<input type="hidden" id="arrayJson" name="arrayJson">
	<input type="hidden" id="totalnum" name="totalnum">
	<input type="hidden" id="totalprice" name="totalprice">
	<input type="hidden" class="proType" name="type" value="${type }"/>
</form>
<form id="queryText" method="post" action="/phoneOrder/79B4DE7C/toOrder.do?uId=${userid }">
	<input type="hidden" id="json" name="data">
	<input type="hidden" id="type" name="type" value="1">
</form>
<jsp:include page="/jsp/mall/customer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="/js/plugin/jquery-form.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/product/noDelay.js"></script>
<script type="text/javascript" src="/js/mall/phone/phone_public.js?<%= System.currentTimeMillis()%>"></script>
<script type="text/javascript" src="/js/mall/product/shoppingcare.js?<%= System.currentTimeMillis()%>"></script>
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
//店面跳转
function pageclick(obj){
	if(obj==''||obj==null||obj==undefined){
		alert("店面未设置微商城主页面或者微商城主页面已删除");
	}else{
		window.location.href = "mallPage/"+obj+"/79B4DE7C/pageIndex.do";
	}
}
/* $(".loading").hide(); */
$(window).load(function() {
	//var a = $(window).width(), b = $(window).height(), d = 870, meta = $("#meta");
	//meta.attr("content", "width=870,initial-scale=" + a/ d + ", minimum-scale=" + a / d+ ", maximum-scale=" + a / d+ ", user-scalable=no");
	setTimeout(function() {
		$(".loading").hide();
	}, 500);
});

$("div.fixRig").css({
	"right":"0.3rem"
});
$("div.fixRig a").css({
	"width":"1.02rem",
	"height":"1.02rem",
	"background-size":"auto 0.6rem"
});
$("div.fixRig a.top").css({
	"margin-bottom":"0.5rem"
});
</script>
</html>