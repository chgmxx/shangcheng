<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--混批下单-->
<div class="item-type" type="2">
	<c:if test="${!empty specificaList }">
	<div class="item-box">
		<c:if test="${specificaList.size() > 1 }">
			<c:set var="specIndex" value="${specificaList.get(0) }"></c:set>
		<p class="specIndex"  option="${specIndex.specNameId}">${specIndex.specName }</p>
		<ul class="box-ul clearfix">
			<c:if test="${!empty specIndex.specValues }">
				<c:forEach items="${specIndex.specValues }" var="specValues" varStatus="i">
			<li class="box-li specValIndex" option="${specValues.specValueId}" optionvalue="${specValues.specValue}">
				<a href="javascript:void(0)" class="color-btn <c:if test='${i.index == 0 }'>cActive</c:if>" onclick="switchBtn(this)">${specValues.specValue}</a>
				<span class="radius-num">0</span>
			</li>
				</c:forEach>
			
			</c:if>
		</ul>
		</c:if>
	</div>
	</c:if>
	<div class="item-box item-thead clearfix">
	</div>
	<div class="item-box item-tables tab_div mixedDiv"></div>
	<div class="item-end">
		还差<span class="least-num pfNumSpan">${pfSet.hpNum }</span>件或 <span class="least-cost">${pfSet.hpMoney }</span>元可混批， <span class="cr-1">&yen;<span class="zj-price split-ele">0.00</span></span> <small>共<span class="zj-num">0</span>件</small>
	</div>
</div>

<div class="mixedTabDiv"  style="display: none;">
	<table class="item-tab createTable" over=0>
		<tbody>
		</tbody>
	</table>
</div>