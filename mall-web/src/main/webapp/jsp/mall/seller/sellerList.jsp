<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<title>销售员管理-销售员</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<base href="<%=basePath%>" />
<link rel="stylesheet" type="text/css" href="/css/mall/seller/back/iconfont/iconfont.css" />
<link rel="stylesheet" type="text/css" href="/css/mall/seller/back/main.css" />
<link rel="stylesheet" type="text/css" href="/css/common.css" />
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<script src="/js/plugin/layer/layer.js"></script>
<script type="text/javascript" src="/js/public.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/mall/mall_public.js"></script>

<script type="text/javascript">
	var error = '${error}';
	if (error != undefined && error != "") {
        parentAlertMsg("参数错误，将调回前一个页面");
//		parent.layer.alert("参数错误，将调回前一个页面");
		window.history.back(-1);
	}
	if(top==self){
		 window.location.href="mallSellers/start.do";
	}
</script>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
	 <div class="page-body">
        <ul class="page-tab">
            <li>
                <a href="/mallSellers/sellerSet.do">功能设置</a>
            </li>
            <li>
                <a href="/mallSellers/joinProduct.do">商品佣金设置</a>
            </li>
            <li>
                <a href="/mallSellers/sellerCheckList.do">推荐审核</a>
            </li>
            <li>
                <a href="/mallSellers/sellerList.do" class="tab-active">销售员管理</a>
            </li>
            <li>
                <a href="/mallSellers/withDrawList.do">提现列表</a>
            </li>
        </ul>
        <p class="page-topic">
            <span class="page-topic-bor"></span>
            <c:if test="${empty saleMemId || empty seller}">
            	<span class="page-topic-name">销售员</span>
            </c:if>
            <c:if test="${!empty saleMemId && !empty seller}">
            	<span class="page-topic-name">销售员${seller.userName }的推荐</span>
            </c:if>
        </p>
        <form id="searchForms">
        <div class="page-oper">
            <div class="oper-srh">
                <input type="text" class="srh-input keyWord" name="keyWord"  placeholder="请输入销售员名字或手机" value="<c:if test="${!empty keyWord }">${keyWord }</c:if>" onkeypress="pressSearch(event)">
                <span class="srh-icon"><i class="iconfont icon-search" onclick="searchUrl();"></i></span>
                <c:if test="${!empty saleMemId }">
                	<input type="hidden" class="saleMemId" value="${saleMemId }"/>
                </c:if>
                <input type="hidden" class="curPage" name="curPage" value="1"/>
            </div>
        </div>
        </form>
        <table class="page-table">
            <thead>
                <tr>
                    <td class="td-column-5">&nbsp;</td>
                    <td class="td-column-10">姓名</td>
                    <td class="td-column-12">手机</td>
                    <td class="td-column-10">积分</td>
                    <td class="td-column-10">销售额</td>
                    <td class="td-column-10">总佣金</td>
                    <td class="td-column-10">冻结佣金</td>
                    <td class="td-column-12">加入时间</td>
                    <td class="td-column-10">操作</td>
                </tr>
            </thead>
            <tbody>
            	
        	<c:if test="${!empty page }">
            	<c:if test="${!empty page.subList }">
            		<c:forEach var="seller" items="${page.subList }">
	            		<c:set var="userName" value="${seller.user_name }"></c:set>
	            		<c:set var="telephone" value="${seller.telephone }"></c:set>
	                   	<c:if test="${empty seller.user_name }">
	                   		<c:set var="userName" value="${seller.nickname }"></c:set>
	                   	</c:if>
	                   	<c:if test="${empty telephone }">
	                   		<c:set var="telephone" value="${seller.phone }"></c:set>
	                   	</c:if>
                <tr>
                    <td class="td-column-5"><input type="checkbox" class="check" value="${seller.id }"></td>
                    <td>${userName }</td>
                    <td>${telephone}</td>
                    <td>${seller.income_integral }</td>
                    <td>${seller.sale_money }</td>
                    <td>${seller.total_commission }</td>
                    <td>${seller.freeze_commission }</td>
                    <td><c:if test="${!empty seller.add_time}"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${seller.add_time }" /></c:if></td>
                    <td>
                    	<c:if test="${empty saleMemId }">
                        <a href="/mallSellers/sellerList.do?saleMemId=${seller.member_id }" class="iconfont icon-zan icon-edit" title="推荐列表"></a>
                        </c:if>
                        <c:if test="${seller.is_start_use == 0}">
                        	<a href="javascript:void(0)" class="iconfont icon-ok icon-edit" onclick="sellerStart(${seller.id},1);" title="启用"></a>
                        </c:if>
                        <c:if test="${seller.is_start_use == -1 }">
                        	<a href="javascript:void(0)" class="iconfont icon-start icon-edit" onclick="sellerStart(${seller.id},1);" title="启用"></a>
                        </c:if>
                        <c:if test="${seller.is_start_use == 1 }">
                        	<a href="javascript:void(0)" class="iconfont icon-stop icon-edit" onclick="sellerStart(${seller.id},-1);" title="暂停"></a>
                        </c:if>
                        <c:if test="${empty saleMemId }">
                        <a href="/mallSellers/withDrawList.do?saleMemId=${seller.member_id }" class="iconfont icon-tMoney icon-edit" title="提现记录"></a>
                        </c:if>
                    </td>
                </tr>
                	</c:forEach>
                </c:if>
        </c:if>
            </tbody>
        </table>
        <c:if test="${!empty page }">
        <jsp:include page="/jsp/mall/seller/page/page2.jsp"></jsp:include>
        </c:if>
    </div>
	<script type="text/javascript" src="/js/mall/seller/sellerPublic.js"></script>
</body>
</html>