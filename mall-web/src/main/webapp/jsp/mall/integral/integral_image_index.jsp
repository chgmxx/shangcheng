<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商城积分商城图片设置</title>
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
    <link rel="stylesheet" type="text/css" href="/css/mall/auction/aucMargin.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script src="/js/plugin/jquery-1.8.0.min.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/common/util.js"></script>
    <%--<script type="text/javascript" src="/js/zclip/zclip.js"></script>--%>
    <script type="text/javascript" src="/js/common/copy.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        var error = '${error}';
        if (error != undefined && error != "") {
            parent.layer.alert("参数错误，将调回前一个页面");
            window.history.back(-1);
        }
        if (top == self) {
            window.location.href = "/mallIntegral/index.do";
        }
    </script>
    <style type="text/css">
        .img_span { /* display: table-cell;  */
            width: 100%;
            height: 60px;
            text-align: center;
            vertical-align: middle;
            position: absolute;
            left: 0;
        }

        .img_span img {
            height: 100%;
            position: absolute;
            -webkit-transform: translate(-50%, -50%);
            transform: translate(-50%, -50%);
            top: 50%;
            left: 50%;
        }

        .txt-tle {
            padding-left: 20px;
        }
    </style>
</head>
<body>
<div class="con_div">
    <c:if test="${empty isNoAdminFlag }">
        <div id="con-box">
            <c:if test="${!empty shoplist }">
                <div class="con-head">
                    <a class="" href="/mallIntegral/index.do">积分商品管理</a>
                    <a class="navColor" href="/mallIntegral/image_index.do">积分商城图片设置</a>
                </div>
                <div class="con-box1">
                    <div class="txt-btn" style="margin: 0px;">
                        <div class="blue-btn fl box1Btn">
                            <a id="layShow" href="/mallIntegral/to_image_edit.do">新建积分商城图片</a>
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
                            <span class="f7 fl" style="">图片</span>
                            <span class="f7 fl" style="">所属店铺</span>
                            <span class="f8 fl" style="width: 30%">跳转页面</span>
                            <span class="f8 fl" style="">创建时间</span>
                            <span class="f1 fl" style="">是否显示</span>
                            <span class="f2 fl">操作</span>
                        </li>

                        <c:if test="${!empty page.subList}">
                            <c:forEach var="image" items="${page.subList }">
                                <li class="txt-tle" style="min-height:60px;height:auto;">
						<span class="f7 fl" style="min-height:60px;line-height:60px;padding-top:10px;position: relative; ">
							<span class="img_span"><img src="${imgUrl }${image.imageUrl}"/></span>
						</span>
                                    <span class="f7 fl" style="line-height:20px;padding-top:10px;">${image.shopName }</span>
                                    <c:if test="${!empty image.returnUrl }">
                                        <span class="f8 fl" style="line-height:20px;padding-top:10px;width: 30%">${image.returnUrl } </span>
                                    </c:if>
                                    <c:if test="${empty image.returnUrl }">
                                        <span class="f8 fl" style="line-height:60px;padding-top:10px;width: 30%">不跳转</span>
                                    </c:if>
                                    <span class="f8 fl" style="line-height:60px;padding-top:10px;"><fmt:formatDate pattern="yyyy-MM-dd hh:mm:ss"
                                                                                                                   value="${image.createTime }"/></span>
                                    <span class="f1 fl" style="line-height:60px;padding-top:10px;">
							<c:if test="${image.isShow == 0}">不显示</c:if>
							<c:if test="${image.isShow == 1}">显示</c:if>
						</span>
                                    <span class="f2 fl span_a" style="width: 130px;position:relative;display:inline-block;">
							<a href="/mallIntegral/to_image_edit.do?id=${image.id }" class="editGroup" title="编辑"></a>
							<a href="javascript:void(0);" id="${image.id}" class="deleteGroup" onclick="deleteImage(this,-1);" title="删除"></a>
							<c:if test="${image.isShow == 1}">
                                <a href="javascript:void(0);" id="${image.id}" class="shiGroup" onclick="deleteImage(this,-2);" title="不显示"></a>
                            </c:if>
							<c:if test="${image.isShow == 0}">
                                <a href="javascript:void(0);" id="${image.id}" class="shiGroup" onclick="deleteImage(this,1);" title="显示"></a>
                            </c:if>
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
                <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
            </c:if>
            <!--内容编辑行end-->
        </div>
    </c:if>
    <c:if test="${!empty isNoAdminFlag }">
        <h1 class="groupH2" style="margin: auto;"><strong>您还不是管理员，不能管理商城</strong></h1>
    </c:if>
</div>
<!--中间信息结束-->
<script type="text/javascript" src="/js/mall/integral/integral_image.js"></script>
</body>
</html>