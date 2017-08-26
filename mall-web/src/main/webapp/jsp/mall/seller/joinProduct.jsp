<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商品佣金设置</title>
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
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/seller/back/main.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/Fan-index.css">
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/common/copy.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>

    <script type="text/javascript">
        var error = '${error}';
        if (error != undefined && error != "") {
            layer.alert("参数错误，将调回前一个页面");
            window.history.back(-1);
        }
        jQuery().ready(function () {
            /**
             * 查看二维码
             * @param id
             */
            $(".qrcode").click(function () {
                SonScrollTop(0);
                setTimeout(function () {
                    layer.open({
                        type: 1,
                        title: "商品预览",
                        shade: [0.1, "#fff"],
                        skin: 'layui-layer-rim', //加上边框
                        area: ['208px', '251px'], //宽高
                        offset: scrollHeight + "px",
                        content: "<img src ='/mallSellers/getTwoCode.do?code=" + $(this).attr("tit") + "' style='width:200px;height:200px;'/>"
                    });
                }, timeout);
            });
        });
    </script>
</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div id="con-box">
    <div class="page-body">
        <ul class="page-tab">
            <li>
                <a href="/mallSellers/sellerSet.do">功能设置</a>
            </li>
            <li>
                <a href="/mallSellers/joinProduct.do" class="tab-active">商品佣金设置</a>
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
            <span class="page-topic-name">商品佣金</span>
        </p>
    </div>
    <div class="con-box1">
        <div class="txt-btn">
            <div class="blue-btn fl box1Btn" style="padding:0px 10px;width:auto;">
                <a id="layShow" href="/mallSellers/to_edit_join.do">新建商品佣金</a>
            </div>
        </div>
    </div>
    <table class="page-table">
        <thead>
        <tr>
            <td class="td-column-10">商品名称</td>
            <td class="td-column-12">所属店铺</td>
            <td class="td-column-10">活动状态</td>
            <td class="td-column-10">创建时间</td>
            <td class="td-column-10">操作</td>
        </tr>
        </thead>
        <tbody>

        <c:if test="${!empty page }">
            <c:if test="${!empty page.subList }">
                <c:forEach var="joinProduct" items="${page.subList }">
                    <tr>
                        <td style="line-height:20px;">${joinProduct.pro_name }</td>
                        <td style="line-height:20px;">${joinProduct.shopName }</td>
                        <td>
                            <c:if test="${joinProduct.is_use == 0 }">已禁用</c:if>
                            <c:if test="${joinProduct.is_use == 1 }">已启用</c:if>
                        </td>
                        <td><c:if test="${!empty joinProduct.create_time}"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss" value="${joinProduct.create_time }"/></c:if></td>
                        <td>
						<span class="operate" style="position:relative;display:inline-block;">
							<a href="/mallSellers/to_edit_join.do?id=${joinProduct.id }" class="edit" title="编辑"></a>
							<a href="javascript:void(0);" id="${joinProduct.id}" class="delete" onclick="deleteJoinProduct(this,-3);" title="删除"></a>
							<c:if test="${joinProduct.is_use == 1 }">
                                <a href="javascript:void(0);" id="${joinProduct.id}" class="shiGroup" onclick="deleteJoinProduct(this,-1);" title="禁用"></a>
                            </c:if>
							<c:if test="${joinProduct.is_use == 0 }">
                                <a href="javascript:void(0);" id="${joinProduct.id}" class="shiGroup" onclick="deleteJoinProduct(this,-2);" title="启用"></a>
                            </c:if>
							<c:if test="${joinProduct.two_code_path != null && joinProduct.two_code_path != '' }">
                                <a href="javascript:;" class="bj-a qrcode" title="预览" tit="${joinProduct.product_id}/${joinProduct.shop_id}"></a>
                            </c:if>
							<a href="javascript:;" class="bj-a copy copy_public" title="复制链接"
                               data-clipboard-text="${path }/mallPage/${joinProduct.product_id}/${joinProduct.shop_id }/79B4DE7C/phoneProduct.do?view=show" aria-label="复制成功！"></a>
						</span>
                            <input type="hidden" class="link" value="${path }/mallPage/${joinProduct.product_id}/${joinProduct.shop_id }/79B4DE7C/phoneProduct.do?view=show"/>
                        </td>
                    </tr>
                </c:forEach>
            </c:if>
        </c:if>
        </tbody>
    </table>
    <c:if test="${!empty page }">
        <jsp:include page="/jsp/common/page/page.jsp"></jsp:include>
    </c:if>
</div>
<script type="text/javascript" src="/js/mall/seller/editJoinProduct.js"></script>
<script type="text/javascript" src="/js/mall/seller/sellerPublic.js"></script>
</body>
</html>