<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>功能设置</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://"
                + request.getServerName() + ":" + request.getServerPort()
                + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/seller/back/main.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/Fan-index.css">
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>

</head>
<body>
<div id="con-box">
    <c:if test="${empty isNoAdminFlag }">
        <div class="page-body">
            <ul class="page-tab">
                <li>
                    <a href="/mallSellers/sellerSet.do" class="tab-active">功能设置</a>
                </li>
                <li>
                    <a href="/mallSellers/joinProduct.do">商品佣金设置</a>
                </li>
                <li>
                    <a href="/mallSellers/sellerCheckList.do">推荐审核</a>
                </li>
                <li>
                    <a href="/mallSellers/sellerList.do">销售员管理</a>
                </li>
                <li>
                    <a href="/mallSellers/withDrawList.do">提现列表</a>
                </li>
            </ul>
            <p class="page-topic">
                <span class="page-topic-bor"></span>
                <span class="page-topic-name">审核设置</span>
            </p>
            <c:if test="${!empty videourl }">
                <div class="blue-btn fl right-15" style="margin-top:10px;">
                    <a href='${videourl}' class="btn" target='_blank' style="color: white;">教学视频</a>
                </div>
            </c:if>
            <c:set var="isNameRequired" value="1"></c:set>
            <c:set var="isCompanyRequired" value="0"></c:set>
            <c:set var="isTelephoneRequired" value="1"></c:set>
            <c:set var="isValidateRequired" value="1"></c:set>
            <c:set var="isRemarkRequired" value="0"></c:set>
            <c:if test="${!empty sellerSet }">
                <c:set var="isNameRequired" value="${sellerSet.isNameRequired }"></c:set>
                <c:set var="isCompanyRequired" value="${sellerSet.isCompanyRequired }"></c:set>
                <c:set var="isTelephoneRequired" value="${sellerSet.isTelephoneRequired }"></c:set>
                <c:set var="isValidateRequired" value="${sellerSet.isValidateRequired }"></c:set>
                <c:set var="isRemarkRequired" value="${sellerSet.isRemarkRequired }"></c:set>
            </c:if>
            <section class="set-part">
                <div class="set-part-1">
                    <div class="part-box">
                        <input type="hidden" name="id" value='<c:if test="${!empty sellerSet }">${sellerSet.id }</c:if>'/>
                        <label>姓名：</label>
                        <span class="part-radios">
	                        <span class="part-radio"><input type="radio" name="isNameRequired" <c:if test="${isNameRequired == 0 }">checked="checked"</c:if>>选填</span>
	                        <span class="part-radio"><input type="radio" name="isNameRequired" <c:if test="${isNameRequired == 1 }">checked="checked"</c:if>>必填</span>
	                    </span>
                    </div>
                    <div class="part-box">
                        <label>公司名称：</label>
                        <span class="part-radios">
	                        <span class="part-radio"><input type="radio" name="isCompanyRequired" <c:if test="${isCompanyRequired == 0 }">checked="checked"</c:if>>选填</span>
	                        <span class="part-radio"><input type="radio" name="isCompanyRequired" <c:if test="${isCompanyRequired == 1 }">checked="checked"</c:if>>必填</span>
	                    </span>
                    </div>
                    <div class="part-box">
                        <label>手机号码：</label>
                        <span class="part-radios">
	                        <span class="part-radio">
	                            <input type="radio" name="isTelephoneRequired" <c:if test="${isTelephoneRequired == 1 }">checked="checked"</c:if>>必填
	                        </span>
	                    </span>
                    </div>
                    <div class="part-box">
                        <label>验证码：</label>
                        <span class="part-radios">
	                        <span class="part-radio">
	                            <input type="radio" name="isValidateRequired" <c:if test="${isValidateRequired == 1 }">checked="checked"</c:if>>必填
	                        </span>
	                    </span>
                    </div>
                    <div class="part-box">
                        <label>备注：</label>
                        <span class="part-radios">
	                        <span class="part-radio"><input type="radio" name="isRemarkRequired" <c:if test="${isRemarkRequired == 0 }">checked="checked"</c:if>>选填</span>
	                        <span class="part-radio"><input type="radio" name="isRemarkRequired" <c:if test="${isRemarkRequired == 1 }">checked="checked"</c:if>>必填</span>
	                    </span>
                    </div>
                </div>
                <div class="set-phone phone-1"></div>
            </section>
            <p class="page-topic">
                <span class="page-topic-bor"></span>
                <span class="page-topic-name">基本规则</span>
            </p>
            <section class="set-part">
                <div class="set-part-2">
                    <div class="part-box-2">
                        <label class="part-label">积分奖励：</label>
                        <span class="part-rule">
	                    每推荐1人关注商城公众号，可奖励
	                    <input type="text" class="part-input-s integralReward" name="integralReward"
                               value="<c:if test="${!empty sellerSet}"><c:if test="${sellerSet.integralReward > 0}">${sellerSet.integralReward }</c:if></c:if>"
                               datatype="^(\d{0,5})(\.\d{1,2})?$" errormsg="积分奖励最多输入5位小数" maxlength="8">积分
	                     	<br/>
                        	<small class="part-desc integralRewardVali">积分奖励最多输入5位小数</small>
	                    </span>
                    </div>
                    <div class="part-box-2">
                        <label class="part-label">成为销售员：</label>
                        <span class="part-rule">
                    		当消费金额满
		                    <input type="text" class="part-input-s consumeMoney" name="consumeMoney"
                                   value="<c:if test="${!empty sellerSet}"><c:if test="${sellerSet.consumeMoney > 0}">${sellerSet.consumeMoney }</c:if></c:if>"
                                   datatype="^(\d{0,5})(\.\d{1,2})?$" errormsg="成为销售员的条件最多输入5位小数" maxlength="8">元可申请成为超级销售员
	                    	 <br/>
	                    	 <small class="part-desc consumeMoneyVali">成为销售员的条件最多输入5位小数</small>
	                   </span>
                    </div>
                    <div class="part-box-2">
                        <label class="part-label label-multi">提现规则：</label>
                        <c:set var="withdrawalType" value="1"></c:set>
                        <c:set var="withdrawalLowestMoney" value="0"></c:set>
                        <c:set var="withdrawalMultiple" value="0"></c:set>
                        <c:if test="${!empty sellerSet}">
                            <c:set var="withdrawalType" value="${sellerSet.withdrawalType }"></c:set>
                            <c:set var="withdrawalLowestMoney" value="${sellerSet.withdrawalLowestMoney }"></c:set>
                            <c:set var="withdrawalMultiple" value="${sellerSet.withdrawalMultiple }"></c:set>
                        </c:if>
                        <div class="part-rule">
	                    	<span class="part-radio part-mgb">
		                    <input type="radio" name="withdrawalType" class="withdrawalType" value="1"
                                   <c:if test="${withdrawalType == 1 }">checked="checked"</c:if>/>最低可提现
		                    	<input type="text" class="withdrawalLowestMoney part-input-s" name="withdrawalLowestMoney"
                                       value="<c:if test="${withdrawalType == 1 }">${withdrawalLowestMoney }</c:if>" min="1" max="2000"
                                       datatype="^(\d{0,5})(\.\d{1,2})?$" errormsg="提现规则最多输入5位小数" maxlength="8">元
		                    </span>
                            <br/>
                            <span class="part-radio">
		                    <input type="radio" name="withdrawalType" class="withdrawalType" value="2"
                                   <c:if test="${withdrawalType == 2 }">checked="checked"</c:if>/>按
		                    	<input type="text" class="withdrawalMultiple part-input-s" name="withdrawalMultiple" min="1" max="2000"
                                       value="<c:if test="${withdrawalType == 2 && withdrawalMultiple > 0}">${withdrawalMultiple }</c:if>"
                                       datatype="^(\d{0,5})(\.\d{1,2})?$" errormsg="提现规则最多输入5位小数" maxlength="8">元的倍数提现
		                    </span>
                            <br/>
                            <small class="part-desc withdrawalMultipleVali withdrawalLowestMoneyVali">提现规则最底1元，最高2000元</small>
                        </div>
                    </div>
                    <div class="part-box-2">
                        <label class="part-label vtp">经纪人说明：</label>
                        <textarea name="sellerRemark" id="" cols="30" rows="10" class="part-text sellerRemark"
                                  maxlength="200"><c:if test="${!empty sellerSet}"><c:if test="${!empty sellerSet.sellerRemark}">${sellerSet.sellerRemark }</c:if></c:if></textarea>
                    </div>
                </div>
            </section>
            <div class="btn-group">
                <a href="javascript:void(0)" class="blue-btn" onclick="editSellerSet();">保存</a>
                <!-- <a href="javascript:void(0)" class="green-btn">返回</a> -->
                <a href="javascript:void(0);" class="blue-btn copyUrl copy_public"
                   data-clipboard-text="${httpUrl}/phoneSellers/79B4DE7C/toApplySeller.do?uId=${user.id}" aria-label="复制成功！">复制链接</a>

            </div>
        </div>
    </c:if>
    <c:if test="${!empty isNoAdminFlag }">
        <h1 class="groupH1"><strong>您还不是管理员，不能管理商城</strong></h1>
    </c:if>
</div>
<script type="text/javascript" src="/js/mall/seller/sellerSet.js"></script>
<script type="text/javascript" src="/js/mall/seller/sellerPublic.js"></script>
</body>
</html>