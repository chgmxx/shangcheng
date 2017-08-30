<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
<title>我的提现</title>
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
	<c:set var="withdrawMoney" value="0"></c:set>
	 <c:if test="${!empty sellerSet }">
     	<c:if test="${sellerSet.withdrawalType == 1 }">
     		<c:if test="${seller.canPresentedCommission >=  sellerSet.withdrawalLowestMoney}">
     			<c:set var="withdrawMoney" value="${seller.canPresentedCommission }"></c:set>
     		</c:if>
     	</c:if>
    	<c:if test="${sellerSet.withdrawalType == 2 && seller.canPresentedCommission >= sellerSet.withdrawalMultiple}">
     		<c:set var="withdrawMoney" value="${seller.canPresentedCommission-seller.canPresentedCommission%sellerSet.withdrawalMultiple }"></c:set>
     	</c:if>
     	<c:if test="${withdrawMoney < 1 }">
     		<c:set var="withdrawMoney" value="0"></c:set>
     	</c:if>
     </c:if>
     <input type="hidden" class="canPresentedCommission" value="${seller.canPresentedCommission }"/>
     <input type="hidden" class="withdrawalType" value="${sellerSet.withdrawalType }"/>
     <input type="hidden" class="withdrawalLowestMoney" value="${sellerSet.withdrawalLowestMoney }"/>
     <input type="hidden" class="withdrawalMultiple" value="${sellerSet.withdrawalMultiple }"/>
     <input type="hidden" class="withdrawMoney" value="${withdrawMoney }"/>
    <header class="c-header">
        <p class="c-header-title">可提现佣金(元)</p>
        <div class="c-header-main">
            <span class="c-cash-num"><span class="money_span" id="1">${withdrawMoney}</span> <!-- 0<span class="digit-s-1">.00</span> --></span>
            <a href="/phoneSellers/79B4DE7C/withdrawalDetail.do?uId=${userid}" class="c-btn-s">查看明细</a>
        </div>
        <p class="c-header-bottom">累计提现佣金：<span class="money_span" id="2">${seller.canPresentedCommission}</span><!--  2333. <span class="digit-s-2">00</span> --></p>
    </header>
    <section class="s-body">
        <div class="c-cash">
            <ul class="c-cash-detail">
                <li>
                    <p class="digit-s-3">累计佣金</p>
                    <p><span class="digit-l-1"><span class="money_span" id="3">${seller.totalCommission}</span></span><!-- 25299.<span class="digit-s-3">00</span> --></p>
                    <p class="c-text-1">所有佣金</p>
                </li>
                <li>
                    <p class="digit-s-3">佣金</p>
                    <p><span class="digit-l-1"><span class="money_span" id="3">${seller.canPresentedCommission+seller.freezeCommission}</span></span><!-- 25299.<span class="digit-s-3">00</span> --></p>
                    <p class="c-text-1">当前账户的佣金</p>
                </li>
                <li>
                    <p class="digit-s-3">冻结佣金</p>
                    <p><span class="digit-l-1"><span class="money_span" id="3">${seller.freezeCommission}</span></span><!-- 25299.<span class="digit-s-3">00</span> --></p>
                    <p class="c-text-1">结算期内的佣金</p>
                </li>
                <li>
                    <p class="digit-s-3">可提现</p>
                    <p><span class="digit-l-1"><span class="money_span" id="3">${seller.canPresentedCommission}</span></span><!-- 25299.<span class="digit-s-3">00</span> --></p>
                    <p class="c-text-1">结算后的佣金</p>
                </li>
            </ul>
            <div class="c-cash-remark">
                买家付款后将获得申请佣金；买家确认收货后获得代理佣金；结算期后，佣金可提现；结算期内，买家退货，佣金将自动扣除。
                <c:if test="${empty sellerSet }">
               		<p class="c-cash-require">注意：商家没有设置提现规则，暂时不能提现</p>
                </c:if>
                <c:if test="${!empty sellerSet }">
                	<c:if test="${sellerSet.withdrawalType == 1 }">
                		<p class="c-cash-require">注意：可用佣金满${sellerSet.withdrawalLowestMoney }元后方能申请提现
                		<c:if test="${withdrawMoney > 0 }">
                		,您目前可提现<span class="m_spans">${withdrawMoney }</span>
                		</c:if>
                		</p>		
                	</c:if>
               		<c:if test="${sellerSet.withdrawalType == 2 }">
                		<p class="c-cash-require">注意：您目前只能申请${sellerSet.withdrawalMultiple }的倍数提现
                		<c:if test="${withdrawMoney > 0 }">
                			,您目前可提现<span class="m_spans money_span">${withdrawMoney }</span>
                		</c:if>
                		</p>		
                	</c:if>
                </c:if>
            </div>
        </div>
        <div class="c-cash-link">
            <a href="javascript:void(0)" class="c-btn-l <c:if test="${withdrawMoney > 0 }">c-btn-active</c:if>" onclick="withdraw(this);">我要提现</a>
            <p>提现记录</p>
        </div>
        <div class="c-cash-table">
        	<c:if test="${!empty withdrawList }">
            <div class="c-cash-thead">
                <div class="c-cash-tr tr-pdlr">
                    <span>时间</span><span>金钱</span><span>状态</span>
                </div>
            </div>
            
            <div class="c-cash-tbody">
            	<c:forEach var="withdraw" items="${withdrawList }">
                <div class="c-cash-tr">
                    <span><c:if test="${!empty withdraw.applayTime}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm" value="${withdraw.applayTime }" /></c:if></span>
                    <span>&yen; ${withdraw.withdrawMoney }</span>
                    <span>
                    	<c:if test="${withdraw.withdrawStatus == 1}">已申请</c:if>
                    	<c:if test="${withdraw.withdrawStatus == 1}">待打款</c:if>
                    	<c:if test="${withdraw.withdrawStatus == 2}">已打款</c:if>
                    </span>
                </div>
                </c:forEach>
            </div>
            </c:if>
            <c:if test="${empty withdrawList }">没有提现记录</c:if>
        </div>
    </section>
</div>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/js/plugin/layer-mobile/layer/layer.js"></script>
<script type="text/javascript" src="/js/mall/seller/phone/sellerPublic.js"></script>
<script type="text/javascript" src="/js/plugin/gt/js/gt_common.js"></script>
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
$(window).load(function() {
	
 	$(".money_span").each(function(){
 		var money = $(this).text()*1;
 		money = money.toFixed(2);
 		var str = money.split(".");
 		if(str.length > 1){
 			$(this).html(str[0]+".<span class='digit-s-"+$(this).attr("id")+"'>"+str[1]+"</span>");
 		}else{
 			$(this).html(money);
 		}
 	});
 	var withdrawMoney = $("input.withdrawMoney").val();
 	if(withdrawMoney != null && withdrawMoney != ""){
 		withdrawMoney = (withdrawMoney*1).toFixed(2);
 		$("input.withdrawMoney").val(withdrawMoney);
 	}
 	$(".m_spans").each(function(){
 		var money = $(this).text()*1;
 		money = money.toFixed(2);
 		$(this).html(money);
 	});
});

function ok(){
	location.href = window.location.href;
}

function ajaxUrl(){
	var withdrawMoney = $("input.withdrawMoney").val();
	var data = {
		"withdrawMoney" : withdrawMoney
	};
	layer.open({type: 2});
	$.ajax({
		type : "post",
		url : "/phoneSellers/79B4DE7C/editWithdrawal.do",
		data : {
			withdraw : JSON.stringify(data)
		},
		dataType : "json",
		success : function(data) {
			layer.closeAll();
			if (data.flag ) {
				gtcom.dialog("提现成功","b","ok");
			} else {// 编辑失败
				if(data.msg == null || data.msg == ""){
					gtcom.dialog("提现失败，请稍后重试","a");
				}else{
					gtcom.dialog(data.msg,"a");
				}
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			layer.closeAll();
			gtcom.dialog("提现失败，请稍后重试","a");
			return;
		}
	});
}

function withdraw(obj){
	if(!$(obj).hasClass("c-btn-active")){
		gtcom.dialog("您实际能提现的佣金为0，请继续累积","a");
		return false;
	}
	var withdrawMoney = $("input.withdrawMoney").val();
	gtcom.dialog("您确认要提现"+withdrawMoney+"元","b","ajaxUrl");
}

</script>
</body>
</html>