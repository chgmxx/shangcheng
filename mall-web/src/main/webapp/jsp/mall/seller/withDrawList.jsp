<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html lang="en">
<head>
<title>销售员管理-提现列表</title>
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
<script type="text/javascript" src="/js/plugin/laydate/laydate.js"></script>
<script type="text/javascript" src="/js/public.js"></script>
<script type="text/javascript" src="/js/util.js"></script>
<script type="text/javascript" src="/js/mall/mall_public.js"></script>

<script type="text/javascript">
	var error = '${error}';
	if (error != undefined && error != "") {
		parent.layer.alert("参数错误，将调回前一个页面");
		window.history.back(-1);
	}
	if(top==self){
		 window.location.href="/mallSellers/start.do";
	}
</script>
</head>
<body>
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
                <a href="/mallSellers/sellerList.do">销售员管理</a>
            </li>
            <li>
                <a href="/mallSellers/withDrawList.do" class="tab-active">提现列表</a>
            </li>
        </ul>
        <p class="page-topic">
            <span class="page-topic-bor"></span>
            <c:if test="${empty saleMemId || empty seller}">
            	<span class="page-topic-name">提现记录</span>
            </c:if>
            <c:if test="${!empty saleMemId && !empty seller}">
            	<span class="page-topic-name">销售员${seller.userName }的提现记录</span>
            </c:if>
        </p>
        <form id="searchForms">
        <div class="page-oper">
            <div class="oper-srh">
                <input type="text" class="srh-input keyWord" name="keyWord"  placeholder="请输入销售员名字或手机" value="<c:if test="${!empty keyWord }">${keyWord }</c:if>" onkeypress="pressSearch(event)">
                <span class="srh-icon"><i class="iconfont icon-search" onclick="searchWithDraw();"></i></span>
                <c:if test="${!empty saleMemId }">
                	<input type="hidden" class="saleMemId" value="${saleMemId }"/>
                </c:if>
            </div>
            <div class="page-date">
                <label>提现时间：</label>
                <input type="text" name="startTime" id="startTime" name="startTime" placeholder="请选择日期" class="laydate-icon date-input" value="${startTime }"/>
                <span> - </span>
                <input type="text" name="endTime" id="endTime" name="endTime" placeholder="请选择日期" class="laydate-icon date-input" value="${endTime }"/>
            </div>
        </div>
         <input type="hidden" class="curPage" name="curPage" value="1"/>
        </form>
        <table class="page-table">
            <thead>
                <tr>
                    <!-- <td class="td-column-5">&nbsp;</td> -->
                    <td class="td-column-12">提现时间</td>
                    <td class="td-column-10">姓名</td>
                    <td class="td-column-12">手机</td>
                    <td class="td-column-10">金额</td>
                    <td class="td-column-12">订单号</td>
                    <td class="td-column-10">状态</td>
                    <!-- <td class="td-column-10">操作</td> -->
                </tr>
            </thead>
            <c:if test="${!empty page }">
            <tbody>
            	<c:if test="${!empty page.subList }">
            		<c:forEach var="withDraw" items="${page.subList }">
                <tr>
                   <!--  <td class="td-column-5"><input type="checkbox"></td> -->
                    <td><c:if test="${!empty withDraw.applay_time}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${withDraw.applay_time }" /></c:if></td>
                    <td>${withDraw.user_name }</td>
                    <td>${withDraw.telephone }</td>
                    <td>${withDraw.withdraw_money }</td>
                    <td>${withDraw.withdraw_order_no }</td>
                    <td>
                    	<c:if test="${withDraw.withdraw_status == 1}">待打款</c:if>
                    	<c:if test="${withDraw.withdraw_status == 2}">已打款</c:if>
                    </td>
                    <!-- <td>
                        <a href="javascript:void(0)" class="iconfont icon-ok icon-edit" title="打款"></a>
                    </td> -->
                </tr>
            		</c:forEach>
            	</c:if>
            </tbody>
            </c:if>
        </table>
        <c:if test="${!empty page }">
        <jsp:include page="/jsp/mall/seller/page/page2.jsp"></jsp:include>
        </c:if>
    </div>
	<script type="text/javascript" src="/js/mall/seller/sellerPublic.js"></script>
	<script>
		$(function () {
	        loadLaydate();
	    });
		 /**初始化两个日期控件*/
	    function loadLaydate(){
	        var datebox_1 = {
	            elem: '#startTime',
	            event: 'focus',
	            festival: true,
	            format: 'YYYY-MM-DD hh:mm:ss',
	            istime : true,
	            max: laydate.now(),
	            choose: function(datas){
	            	datebox_2.min = datas; // 开始日选好后，重置结束日的最小日期
	            	datebox_2.start = datas; // 将结束日的初始值设定为开始日
	            	searchWithDraw();
	            }
	        };
	        var datebox_2 = {
	            elem: '#endTime',
	            event: 'focus',
	            festival: true,
	            max: laydate.now(),
	            format: 'YYYY-MM-DD hh:mm:ss',
	            istime : true,
	            choose: function(datas){
	            	datebox_1.max = datas; // 开始日选好后，重置结束日的最小日期
	            	searchWithDraw();
	            }
	        };
	        laydate(datebox_1);
	    	laydate(datebox_2);
	    	
	    	var startTime = $("#startTime").val();
	    	if(startTime != null && startTime != ""){
	    		datebox_2.min =startTime;
	    		datebox_2.start =startTime;
	    	}
	    }
		 
		function searchWithDraw(){
			var keyWord = $(".keyWord").val();
        	var url = window.location.pathname;
        	if(keyWord != null && keyWord != ""){
        		if(url.split("?").length == 1){
        			url += "?";
        		}else{
        			url += "&";
        		}
        		//keyWord = encodeURIComponent(encodeURIComponent(keyWord));
        		url += "keyWord="+keyWord;
        	}
        	if($("#startTime").length > 0){
        		var startTime = $("#startTime").val();
        		if(startTime != null && startTime != ""){
        			if(url.split("?").length == 1){
        				url += "?";
        			}else{
        				url += "&";
        			}
        			url += "startTime="+startTime;
        		}
        	}
        	if($("#endTime").length > 0){
        		var endTime = $("#endTime").val();
        		if(endTime != null && endTime != ""){
        			if(url.split("?").length == 1){
        				url += "?";
        			}else{
        				url += "&";
        			}
        			url += "endTime="+endTime;
        		}
        	}
        	location.href = url;
		}
    </script>
</body>
</html>