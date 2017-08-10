<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>保证金管理</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    %>
    <base href="<%=basePath%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/auction/aucMargin.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <style type="text/css">
        embed {
            position: absolute
        }
    </style>
</head>
<body>
<div id="con-box">
    <c:if test="${!empty shoplist }">
        <div class="con-head">
            <a class="" href="/mAuction/index.do">拍卖管理</a>
            <a class="navColor" href="/mAuction/margin.do">保证金管理</a>
        </div>
        <div class="msg-list">
            <ul id="list group_ul">
                <li class="txt-tle">
                    <span class="f2 fl" style="">拍卖商品</span>
                    <span class="f2 fl" style="">所属店铺</span>
                    <span class="f2 fl" style="width: 16%;">竞拍编号</span>
                    <span class="f4 fl" style="width: 10%;">保证金金额</span>
                    <span class="f4 fl" style="width: 10%;">保证金状态</span>
                    <span class="f2 fl">支付时间</span>
                    <span class="f2 fl">返还时间</span>
                    <span class="f4 fl">操作</span>
                </li>

                <c:if test="${!empty page.subList}">
                    <c:forEach var="margin" items="${page.subList }">
                        <li class="txt-tle" style="min-height:50px;height:auto;">
                            <span class="f2 fl" style="line-height:20px;padding-top:10px;">${margin.proName }</span>
                            <span class="f2 fl" style="line-height:20px;padding-top:10px;">${margin.shopName }</span>
                            <span class="f2 fl" style="line-height:20px;padding-top:10px;width: 16%">${margin.aucNo }</span>
                            <span class="f4 fl" style="line-height:20px;padding-top:10px;width: 10%;">${margin.marginMoney }</span>
                            <span class="f4 fl" style="line-height:20px;padding-top:10px;width: 10%;">
							<c:if test="${margin.marginStatus == 0}">未支付</c:if>
							<c:if test="${margin.marginStatus == 1}">已支付</c:if>
							<c:if test="${margin.marginStatus == -1}">已返还</c:if>
						</span>
                            <span class="f2 fl" style="line-height:20px;padding-top:10px;"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${margin.payTime }"/></span>
                            <span class="f2 fl" style="line-height:20px;padding-top:10px">&nbsp;<fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${margin.returnTime }"/></span>
                            <span class="f4 fl">
							<c:if test="${(margin.auctionStatus == -1 || margin.auctionStatus == -2) && margin.marginStatus == 1 && margin.isReturn == 1}"><!-- && margin.payWay == 3 -->
								<a href="javascript:void(0);" class="return" onclick="returns(${margin.id});">退保证金</a>
							</c:if>
						</span>
                            <div style="clear: both;"></div>
                        </li>
                    </c:forEach>
                </c:if>
            </ul>
            <c:if test="${! empty page.subList }">
                <input type="hidden" id="taskId" value="0"/>
                <jsp:include page="../../common/page/pageTwo.jsp"></jsp:include>
            </c:if>
        </div>
    </c:if>
    <c:if test="${empty shoplist }">
        <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
    </c:if>
    <!--内容编辑行end-->
</div>
<!--中间信息结束-->
<!--container  End-->
<!--footer  End-->
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" src="/js/mall/auction/auction.js"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);
    function returns(marginId) {
        parent.openIframeNoScoll("退保证金", "500px", "180px", "/mAuction/returnMarginPopUp.do?marginId=" + marginId);
    }

</script>
</body>
</html>