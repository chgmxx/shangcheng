<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--按手批下单-->
<div class="item-type" type="1">
	<div class="item-box clearfix">
		<span class="lh-1">按一手货下单</span>
		<div class="r">
			<a href="javascript:void(0)" class="hand-btn toMinus noDelay" onclick="minusByHand(this)">减一手</a>
			<a href="javascript:void(0)" class="hand-btn toAdd noDelay" onclick="addByHand(this)">加一手</a>
		</div>
	</div>
	<c:if test="${!empty specificaList }">
	<div class="item-box">
		<c:if test="${specificaList.size() > 1 }">
		<c:set var="specIndex" value="${specificaList.get(0) }"></c:set>
		<p class="specIndex"  option="${specIndex.specNameId}">${specIndex.specName }</p>
		<ul class="box-ul clearfix">
			<c:if test="${!empty specIndex.specValues }">
			<c:forEach items="${specIndex.specValues }" var="specValues" varStatus="i">
			<li class="box-li specValIndex"  option="${specValues.specValueId}" optionvalue="${specValues.specValue}">
				<a href="javascript:void(0)" class="color-btn <c:if test='${i.index == 0 }'>cActive</c:if>" onclick="switchBtn(this)">${specValues.specValue}</a>
				<span class="radius-num">0</span>
			</li>
			</c:forEach>
			</c:if>
		</ul>
		</c:if>
	</div>
	<div class="item-box item-thead clearfix">
		<!-- <span class="item-td-4">尺寸</span>
		<span class="item-td-4">大小</span>
		<span class="item-td-4">库存</span>
		<span class="item-td-4">价格</span>
		<span class="item-td-4">数量</span> -->
	</div>
	<div class="item-box item-tables tab_div handDiv"></div>
	</c:if>
	<div class="item-end">
		还差<span class="least-num pfNumSpan">${pfSet.spHand }</span>手可达批发条件， <span class="cr-1">&yen;<span class="zj-price split-ele">0.00</span></span> <small>共<span class="zj-num">0</span>件</small>
	</div>
</div>

<div class="handTabDiv"  style="display: none;">
	<table class="item-tab createTable" over=0>
		<tbody>
		</tbody>
	</table>
</div>
