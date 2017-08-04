<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--输入框 遮罩层-->
<section class="cd-popup" id="cd-input">
	<div class="cd-main">
		<div class="cd-content">
			<p class="cd-title">修改购买数量</p>
			<div class="cd-item">
				<i class="bProp minus-icon noDelay" onclick="minusOper(this)"></i>
				<input type="tel" class="item-input2" value=2  oninput="strictStock(this)">
				<i class="bProp add-icon noDelay" onclick="addOper(this)"></i>
			</div>
		</div>
		<div class="cd-bottom">
			<a href="javascript:void(0)" class="cd-btn btnCancel" onclick="closeLayer(this)">取消</a>
			<a href="javascript:void(0)" class="cd-btn btnOkay" onclick="numOper(this)">确定</a>
		</div>
	</div>
</section>
<!-- 批发遮罩层 -->
<div class="dWrapper">
	<div class="wrap-item">
		<c:if test="${!empty pifa }">
		<c:set var="hpMoney" value="0"></c:set>
		<c:set var="hpNum" value="0"></c:set>
		<c:set var="spNum" value="1"></c:set>
		<c:if test="${!empty pfSet.isHpMoney }">
			<c:if test="${pfSet.isHpMoney == 1 }">
				<c:set var="hpMoney" value="${pfSet.hpMoney }"></c:set>
			</c:if>
		</c:if>
		<c:if test="${!empty pfSet.isHpNum }">
			<c:if test="${pfSet.isHpNum == 1 }">
				<c:set var="hpNum" value="${pfSet.hpNum }"></c:set>
			</c:if>
		</c:if>
		<c:if test="${hpNum == 0 && hpMoney == 0 }">
			<c:set var="hpNum" value="1"></c:set>
		</c:if>
		<c:if test="${!empty pfSet.isSpHand }">
			<c:if test="${pfSet.isSpHand == 1 }">
				<c:set var="spNum" value="${pfSet.spHand }"></c:set>
			</c:if>
		</c:if>
		<div class="item-detail">
			<div class="item-top">
				<span class="item-text">${mapmessage.pro_name }</span>
				<i class="icon-close" onclick="closeWrap(this)"></i>
			</div>
			<div class="item-box clearfix">
				<i class="small-bg" style="background-image: url('${http}${mapmessage.image_url}');"></i>
				<div class="l">
					<p class="cr-1">&yen; <span class="split-ele proPriceSpan">${mapmessage.pro_price * discount}</span></p>
					<p class="small-p1" style="margin-bottom:15px;">批发价：&yen;<span class="split-ele pfPriceSpan">${pifa.pfPrice }</span></p>
					<p class="cr-1" style="margin-bottom:20px;">库存<span id="inventory2">${mapmessage.pro_stock_total}</span>件</p>
					
					<input type="hidden" class="ws-price" value="${pifa.pfPrice }"/>
					
					<p class="small-p2 ws-least">
						<span class="tip1_span">本商品</span>
						<c:if test="${pifa.pfType == 1 && spNum > 0}">
						<span class="least-num pfNumSpan">${spNum }</span><span class="pfUnitSpan">手</span>
						</c:if>
						<c:if test="${pifa.pfType == 2 }">
						<c:if test="${hpNum > 0 }">
						<span class="least-num pfNumSpan" >${hpNum }</span><span class="pfUnitSpan">件</span>
						</c:if>
						<c:if test="${hpMoney > 0 }">
						<span class="hp_spans" style="display: none;">
							<c:if test="${hpNum > 0 }">或</c:if><span class="hpMoneySpan least-cost">${hpMoney }</span>元
						</span>
						</c:if>
						</c:if>
						<span class="tip2_span">起批</span>
						
						
					</p>
				</div>
			</div>
			<!--按手批下单-->
			<c:if test="${mapmessage.is_specifica == 1 }">
			
				<c:if test="${pifa.pfType == 1 }">
				<jsp:include page="hand_batch.jsp"></jsp:include>
				</c:if>
				<!--混批下单-->
				<c:if test="${pifa.pfType == 2 }">
				<jsp:include page="mixed_batch.jsp"></jsp:include>
				</c:if>
			</c:if>
			<c:if test="${mapmessage.is_specifica == 0 }">
				<div class="item-type" type="3">
					<div class="item-box">
						<p class="clearfix">
							<span>数量</span>
							<span class="item-oper clearfix" style="float: right;">
								<i class="iProp minus-icon noDelay" onclick="minusOper(this,0)"></i>
								<input type="text" class="item-input pfNumInp" value=0  layer=0 onclick="showLayer(this)">
								<i class="iProp add-icon noDelay" onclick="addOper(this,0)"></i>
							</span>
							<!--库存量-->
							<input type="hidden" value="${mapmessage.pro_stock_total }" class="item-stock">
						</p>
					</div>
					<div class="item-end">
						<c:if test="${pifa.pfType == 1 && spNum > 0}">
						<span class="least-num">${spNum }</span>手 
						</c:if>
						<c:if test="${pifa.pfType == 2 }">
							<c:if test="${hpNum > 0 }">
								<span class="least-num">${hpNum }</span>件 
							</c:if>
							<c:if test="${hpMoney > 0 }">
								<c:if test="${hpNum > 0 }">或</c:if> <span class="least-cost">${hpMoney }</span>元
							</c:if>
							
						</c:if>
						起批， 
						<span class="cr-1">&yen;<span class="zj-price split-ele">0.00</span></span> 
						<small>共<span class="zj-num">0</span>件</small>
					</div>
				</div>
			</c:if>
		</div>
		</c:if>
		<div class="item-bottom">
			<a href="javascript:void(0)" onclick="pfAddShopCart(this);" class="item-btn toSC">加入购物车</a>
			<a href="javascript:void(0)" class="item-btn toWS wsInactive" onclick="toWS(this);">立即批发</a>
		</div>
	</div>
</div>