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
    
    <title>商城设置</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="/css/common.css" />
	<link rel="stylesheet" type="text/css" href="/css/common/edit1.css?<%= System.currentTimeMillis()%>" />
	<link rel="stylesheet" type="text/css" href="/css/mall/setindex.css" />
	<script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="/js/public.js"></script>
	<script type="text/javascript" src="/js/util.js"></script>
	<script type="text/javascript" src="/js/public.js"></script>
	<script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
	<script type="text/javascript" src="/js/mall/mall_public.js"></script>
  </head>
  
  <body>
  <jsp:include page="/jsp/common/headerCommon.jsp"/>
 <div class="con_div">
	<div class="con-head">
		<a class="" href="/store/index.do">店铺管理</a> 
		<a class="navColor" href="/store/setindex.do">商城设置</a>
	</div>

	<div class="box-btm30">
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<c:set var="payCla" value="ui-switcher-off"></c:set>
				<c:if test="${!empty set }">
					<c:if test="${set.isDeliveryPay == 1}">
						<c:set var="payCla" value="ui-switcher-on"></c:set>
					</c:if>
				</c:if>
				<input type="hidden" class="id" value="${set.id }" />
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>货到付款</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${payCla }"  id="1"></label>
				    </div>
				    <div class="payment-block-body">
				        <h4>启用后买家可选择货到付款下单，您需自行通过合作快递安排配送。买家开箱验货无误后，快递公司向买家收款并与您结算费用。</h4>
				
				        <div class="ui-message-warning">
				            <p>
				                注意：本功能不参与配送服务，需您自行与快递公司合作，完成配送和货款结算。
				                同时，由于业务特殊性，请勿使用价格虚高商品。
				            </p>
				        </div>
				    </div>
				</div>

			</div>
		</div>
		
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<c:set var="daifuCla" value="ui-switcher-off"></c:set>
				<c:if test="${!empty set }">
					<c:if test="${set.isDaifu == 1}">
						<c:set var="daifuCla" value="ui-switcher-on"></c:set>
					</c:if>
				</c:if>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>找人代付</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${daifuCla }"  id="2"></label>
				    </div>
				    <div class="payment-block-body">
				        <h4>启用代付功能后，代付发起人（买家）下单后，可将订单分享给小伙伴（朋友圈、微信群、微信好友），请他帮忙付款。</h4>
				
				        <!-- <div class="ui-message-warning">
				            <p>
				            注意：代送订单有效期15天，逾期后未完成，进入全额退款流程；若有超额支付，则超付部分进入退款流程。
				            </p>
				        </div> -->
				    </div>
				</div>

			</div>
		</div>
		
		<c:set var="commentCla" value="ui-switcher-off"></c:set>
		<c:set var="checkCla" value="ui-switcher-off"></c:set>
		<c:set var="commentGiveCla" value="ui-switcher-off"></c:set>
		<c:set var="isComment" value="0"></c:set>
		<c:set var="isCommentcheck" value="0"></c:set>
		<c:set var="isCommentGive" value="0"></c:set>
		<c:if test="${!empty set }">
			<c:if test="${set.isComment == 1}">
				<c:set var="commentCla" value="ui-switcher-on"></c:set>
			</c:if>
			<c:if test="${set.isCommentCheck == 1}">
				<c:set var="checkCla" value="ui-switcher-on"></c:set>
			</c:if>
			<c:if test="${set.isCommentGive == 1}">
				<c:set var="commentGiveCla" value="ui-switcher-on"></c:set>
			</c:if>
			<c:set var="isComment" value="${set.isComment }"></c:set>
			<c:set var="isCommentcheck" value="${set.isCommentCheck }"></c:set>
			<c:set var="isCommentGive" value="${set.isCommentGive }"></c:set>
		</c:if>
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<input type="hidden" class="isComment" value="${isComment }"/>
				<input type="hidden" class="isCommentcheck" value="${isCommentcheck }"/>
				<input type="hidden" class="isCommentGive" value="${isCommentGive }"/>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>评论管理</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${commentCla }"  id="3"></label>
				    </div>
				    <c:if test="${isComment == 1 }">
				    <div class="">
				    	 <div class="payment-block-body set_padd" >
					    	<p>
					    		<span class="title">审核功能</span>
					    		<label class="ui-switcher ui-switcher-small js-switch pull-right ${checkCla }"  id="4"></label>
					    	</p>
					    </div>
					    
					    <div class="commDiv">
					    	<div class="payment-block-header set_padd">
						        <h3>评论送礼</h3>
						        <label class="ui-switcher ui-switcher-small js-switch pull-right ${commentGiveCla }"  id="5"></label>
						    </div>
						    <c:if test="${commentGiveCla ==  'ui-switcher-on'}">
						    <div class="payment-block-body" >
						    	<ul class="ul_div">
						    		<c:if test="${!empty giveList }">
						    			<c:forEach var="give" items="${giveList }">
						    				<li class="ul_li">
								    			<input type="hidden" class="id" value="${give.id }" />
								    			<span class="left">
								    			<input type="checkbox" class="status" value="${give.giveStatus }"
								    			 <c:if test="${give.isEnable == 1 }">checked="checked"</c:if>/>
								    			<c:if test="${give.giveStatus == 1 }">好评</c:if>
								    			<c:if test="${give.giveStatus == 0 }">中评</c:if>
								    			<c:if test="${give.giveStatus == -1 }">差评</c:if>
								    			</span>
								    			<span class="right">
								    				送
								    				<select class="type">
								    					<option value="1" <c:if test="${give.giveType == 1 }">selected='selected'</c:if>>积分</option>
								    					<option value="2" <c:if test="${give.giveType == 2 }">selected='selected'</c:if>>粉币</option>
								    				</select>
								    				<input type="text" class="num" value="<c:if test="${give.num > 0}">${give.num }</c:if>"  maxlength="6">个
								    			</span>
								    		</li>
						    			</c:forEach>
						    		</c:if>
						    		<c:if test="${empty giveList }">
							    		<li class="ul_li">
							    			<input type="hidden" class="id" value="" />
							    			<span class="left"><input type="checkbox" class="status" value="1" maxlength="6"/>好评</span>
							    			<span class="right">
							    				送
							    				<select class="type">
							    					<option value="1">积分</option>
							    					<option value="2">粉币</option>
							    				</select>
							    				<input type="text" class="num" value="0">个
							    			</span>
							    		</li>
							    		<li class="ul_li">
							    			<span class="left"><input type="checkbox" class="status" value="0"/>中评</span>
							    			<span class="right">
							    				送
							    				<select class="type">
							    					<option value="1">积分</option>
							    					<option value="2">粉币</option>
							    				</select>
							    				<input type="text" class="num" value="0">个
							    			</span>
							    		</li>
							    		<li class="ul_li">
							    			<span class="left"><input type="checkbox" class="status" value="-1"/>差评</span>
							    			<span class="right">
							    				送
							    				<select class="type">
							    					<option value="1">积分</option>
							    					<option value="2">粉币</option>
							    				</select>
							    				<input type="text" class="num" value="0">个
							    			</span>
							    		</li>
						    		</c:if>
						    		<li>
						    		<input type="button" class="btn saveBtn" value="保存">
						    		</li>
						    	</ul>
						    </div>
						    </c:if>
					    </div>
				    </div>
				    </c:if>
				</div>
				
			</div>
		</div>
		
		
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<c:set var="isMessage" value="ui-switcher-off"></c:set>
				<c:if test="${!empty set }">
					<c:if test="${set.isMessage == 1}">
						<c:set var="isMessage" value="ui-switcher-on"></c:set>
					</c:if>
					<input type="hidden" class="isMessage" value="${set.isMessage }"/>
				</c:if>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>消息模板</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isMessage }"  id="6"></label>
				    </div>
				    <div class="payment-block-body">
				        <!-- <h4>开启消息模板后，买家会接收到消息提醒信息。</h4> -->
				        <c:if test="${!empty messageList }">
				        	<c:forEach var="message" items="${messageList }">
				        		<c:set var="isCheck" value ="false"></c:set>
				        		<c:if test="${!empty msgArr }">
				        			<c:forEach var="item" items="${msgArr }">
					        			<c:if test="${message.id == item.id }">
					        			
					        				<c:set var="isCheck" value ="true"></c:set>
					        			</c:if>
				        			</c:forEach>
				        		</c:if>
				        		<span class="checkSpan">
				        			<input type="checkbox" class="msgId" 
				        				<c:if test="${isCheck }">checked='checked'</c:if>
				        				value="${message.id }" title="${message.title }" />
				        		${message.title }
				        		</span>
				        	</c:forEach>
					        <div>
					        	<input type="button" class="btn" value="保存" onclick="saveMsg();">
					        </div>
				        </c:if>
				        <c:if test="${empty messageList }">
				        	请前往消息模板进行同步模板
				        </c:if>
				        <!-- <div class="ui-message-warning">
				            <p>
				            注意：代送订单有效期15天，逾期后未完成，进入全额退款流程；若有超额支付，则超付部分进入退款流程。
				            </p>
				        </div> -->
				    </div>
				</div>

			</div>
		</div>
		
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<c:set var="isSmsMember" value="ui-switcher-off"></c:set>
				<c:if test="${!empty set }">
					<c:if test="${set.isSmsMember == 1}">
						<c:set var="isSmsMember" value="ui-switcher-on"></c:set>
					</c:if>
					<input type="hidden" class="isSmsMember" value="${set.isMessage }"/>
				</c:if>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>消息短信提醒粉丝</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isSmsMember }"  id="9"></label>
				    </div>
				    <div class="payment-block-body smsMessageDiv">
				        <!-- <h4>开启消息模板后，买家会接收到消息提醒信息。</h4> -->
				        <c:set var="isBuy" value=""></c:set>
				        <c:if test="${!empty smsMsgObj }">
				        	 <c:set var="isBuy" value="${smsMsgObj.get('1') }"></c:set>
				        </c:if>
		        		<div class="checkSpan">
		        			支付成功提醒内容：<input type="text" class="buySuccess saves" style="width: 300px;" maxlength="50"
		        				value="${isBuy}" ids="1" vali="支付成功提醒内容" />
		        		</div>
				        <div>
				        	<input type="button" class="btn" value="保存" onclick="saveSmsMessage();">
				        </div>
				        <!-- <div class="ui-message-warning">
				            <p>
				            注意：代送订单有效期15天，逾期后未完成，进入全额退款流程；若有超额支付，则超付部分进入退款流程。
				            </p>
				        </div> -->
				    </div>
				</div>

			</div>
		</div>
		
		<c:set var="isPresaleGive" value="ui-switcher-off"></c:set>
		<c:set var="isPresale" value="ui-switcher-off"></c:set>
		<c:if test="${!empty set }">
			<c:if test="${set.isPresale == 1}">
				<c:set var="isPresale" value="ui-switcher-on"></c:set>
			</c:if>
			<c:if test="${set.isPresaleGive == 1}">
				<c:set var="isPresaleGive" value="ui-switcher-on"></c:set>
			</c:if>
			<input type="hidden" class="isPresale" value="${set.isPresale }"/>
		</c:if>
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>商品预售</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isPresale }"  id="7"></label>
				    </div>
				    <div class="">
					    <div class="payment-block-body set_padd">
					    	<p>
						        <span class="title">预定送礼</span>
						        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isPresaleGive }"  id="8"></label>
						     </p>
					    </div>
					</div>
				</div>
			
			</div>
		</div>
		
		
		<c:set var="isPf" value="ui-switcher-off"></c:set>
		<c:set var="isPfCheck" value="ui-switcher-off"></c:set>
		<c:if test="${!empty set }">
			<c:if test="${set.isPf == 1}">
				<c:set var="isPf" value="ui-switcher-on"></c:set>
			</c:if>
			<c:if test="${set.isPfCheck == 1}">
				<c:set var="isPfCheck" value="ui-switcher-on"></c:set>
			</c:if>
			<input type="hidden" class="isPf" value="${set.isPf }"/>
		</c:if>
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>商品批发</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isPf }"  id="10"></label>
				    </div>
				    <div class="">
					    <div class="payment-block-body set_padd">
					    	<p>
						        <span class="title">批发审核</span>
						        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isPfCheck }"  id="11"></label>
						     </p>
					    </div>
					</div>
				</div>
			
			</div>
		</div>
		
		
		
		<c:set var="isSeller" value="ui-switcher-off"></c:set>
		<c:set var="isCheckSeller" value="ui-switcher-off"></c:set>
		<c:if test="${!empty set }">
			<c:if test="${set.isSeller == 1}">
				<c:set var="isSeller" value="ui-switcher-on"></c:set>
			</c:if>
			<c:if test="${set.isCheckSeller == 1}">
				<c:set var="isCheckSeller" value="ui-switcher-on"></c:set>
			</c:if>
			<input type="hidden" class="isSeller" value="${set.isSeller }"/>
		</c:if>
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open" >
			<div>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>超级销售员</h3>
				        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isSeller }"  id="12"></label>
				    </div>
				    <div class="">
					    <div class="payment-block-body set_padd">
					    	<p>
						        <span class="title">超级销售员审核</span>
						        <label class="ui-switcher ui-switcher-small js-switch pull-right ${isCheckSeller }"  id="13"></label>
						     </p>
					    </div>
					</div>
				</div>
			
			</div>
		</div>
		
		
		<div class="payment-block-wrap js-payment-block-wrap js-cod-region open">
			<div>
				<c:set var="isFooter" value="ui-switcher-off"></c:set>
				<c:set var="home" value="1"></c:set>
				<c:set var="group" value="1"></c:set>
				<c:set var="cart" value="1"></c:set>
				<c:set var="my" value="1"></c:set>
				<c:if test="${!empty foorerObj }">
					<c:if test="${!empty foorerObj.home }"><c:set var="home" value="${foorerObj.home }"></c:set></c:if>
					<c:if test="${!empty foorerObj.group }"><c:set var="group" value="${foorerObj.group }"></c:set></c:if>
					<c:if test="${!empty foorerObj.cart }"><c:set var="cart" value="${foorerObj.cart }"></c:set></c:if>
					<c:if test="${!empty foorerObj.my}"><c:set var="my" value="${foorerObj.my }"></c:set></c:if>
				</c:if>
				<div class="payment-block">
				    <div class="payment-block-header">
				        <h3>编辑手机端底部菜单</h3>
				    </div>
				    <div class="payment-block-body smsMessageDiv">
				    	<h4>选中之后才在手机端底部显示菜单</h4>
		        		<div class="checkSpan">
		        			<span class="checkSpan">
		        				首页 <input type="checkbox" class="footerInp" value="1" id="home" <c:if test="${home == 1 }"> checked="checked"</c:if> />
		        			</span>
		        			<span class="checkSpan" style="margin-left: 10px;">
		        				分类 <input type="checkbox" class="footerInp" value="1" id="group" <c:if test="${group == 1 }"> checked="checked"</c:if> />
		        			</span>
		        			<span class="checkSpan" style="margin-left: 10px;">
		        				购物车 <input type="checkbox" class="footerInp" value="1" id="cart" <c:if test="${cart == 1 }"> checked="checked"</c:if> />
		        			</span>
		        			<span class="checkSpan" style="margin-left: 10px;">
		        				我的 <input type="checkbox" class="footerInp" value="1" id="my" <c:if test="${my == 1 }"> checked="checked"</c:if> />
		        			</span>
		        		</div>
				        <div>
				        	<input type="button" class="btn" value="保存" onclick="saveFooter();">
				        </div>
				    </div>
				</div>

			</div>
		</div>
		
	</div>
</div>
	<script type="text/javascript">
		$(".payment-block-header").click(function(e){
			var obj = e.srcElement;
			if(!$(obj).hasClass("ui-switcher") && !$(this).hasClass("set_padd")){
				var parent = $(this).parents(".payment-block-wrap");
				if(parent.hasClass("open")){
					parent.removeClass("open");
				}else{
					parent.addClass("open");
				}
			}
			loadWindow();
		});
	</script>
	<script type="text/javascript" src="/js/mall/take/take_index.js"></script>
  </body>
</html>
