<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>物流管理</title>
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
    <link rel="stylesheet" type="text/css" href="/css/fans/Fan-index.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        var error = '${error}';
        if (error != undefined && error != "") {
            parent.layer.alert("参数错误，将调回前一个页面");
            window.history.back(-1);
        }
        if (top == self) {
            window.location.href = "/mFreight/index.do";
        }
    </script>
</head>
<body>
<c:if test="${empty isNoAdminFlag }">
    <div id="con-box">
        <c:if test="${!empty shoplist }">
            <div class="con-head">
                <a class="navColor" href="/mFreight/index.do">物流管理</a>
                <a class="" href="/mFreight/takeindex.do">上门自提</a>
            </div>
            <div class="con-box1">
                <div class="txt-btn" style="margin:0px;">
                    <div class="blue-btn fl box1Btn">
                        <a id="layShow" href="/mFreight/to_edit.do">新建物流</a>
                    </div>
                    <c:if test="${!empty videourl }">
                        <div class="blue-btn fl right-15">
                            <a href='${videourl}' class="btn" target='_blank' style="color: white;">教学视频</a>
                        </div>
                    </c:if>
                </div>
            </div>
            <div class="msg-list">
                <ul id="list group_ul">
                    <li class="txt-tle">
                        <span class="f2 fl">物流名称</span>
                        <span class="f2 fl">运费</span>
                        <span class="f8 fl" style="width:30%">免邮规则(指定省份除外)</span>
                        <span class="f2 fl">所属店铺</span>
                        <span class="f2 fl">创建时间</span>
                        <span class="f2 fl">操作</span>
                    </li>

                    <c:if test="${!empty page.subList}">
                        <c:forEach var="freight" items="${page.subList }">
                            <li class="txt-tle" style="height:auto">
                                <span class="f2 fl" style="line-height: 25px;padding-top:10px;"><c:if test="${!empty freight.name}">${freight.name }</c:if>&nbsp;</span>
                                <span class="f2 fl"><c:if test="${!empty freight.money && freight.isNoMoney == 1}">${freight.money }</c:if><c:if
                                        test="${freight.isNoMoney == 2}">0.00</c:if>&nbsp;</span>
                                <span class="f8 fl" style="width:30%;line-height: 20px;padding-top:10px;">
							<c:if test="${freight.isNoMoney == 2}">卖家承担运费</c:if>
							<c:if test="${freight.isNoMoney == 1}">
                                <c:if test="${freight.noMoneyNum > 0}">
                                    <div>商品满${freight.noMoneyNum }件免邮</div>
                                </c:if>
                                <c:if test="${freight.noMoney > 0}">
                                    <div>商品满${freight.noMoney }元免邮</div>
                                </c:if>
                                <c:if test="${freight.noMoneyNum <= 0 && freight.noMoney <= 0}">无免邮规则</c:if>
                            </c:if>
						</span>
                                <span class="f2 fl" style="line-height:20px;padding-top:10px;"><c:if test="${!empty freight.stoName}">${freight.stoName }</c:if></span>
                                <span class="f2 fl" style="line-height: 20px;padding-top:10px;"><c:if test="${!empty freight.createTime}"><fmt:formatDate
                                        pattern="yyyy-MM-dd hh:mm:ss" value="${freight.createTime }"/></c:if></span>
                                <span class="f2 fl">
							<a href="/mFreight/to_edit.do?id=${freight.id }" class="editGroup" style="background: none;">编辑</a>
							<a href="javascript:void(0);" id="${freight.id}" class="deleteGroup" onclick="deleteFreight(this);" style="background: none;">删除</a>
						</span>
                                <div class="cb"></div>
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
            <h1><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
        </c:if>
        <!--内容编辑行end-->


    </div>
</c:if>
<c:if test="${!empty isNoAdminFlag }">
    <h1 class="groupH2" style="margin: auto;"><strong>您还不是管理员，不能管理商城</strong></h1>
</c:if>
<!--中间信息结束-->
<!--container  End-->
<!--footer  End-->
<script type="text/javascript" src="/js/mall/freight.js"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);

</script>
</body>
</html>