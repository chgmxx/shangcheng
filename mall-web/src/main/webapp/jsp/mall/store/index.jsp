<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>店铺管理</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/edit1.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/table.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/common/copy.js"></script>
    <%--<script type="text/javascript" src="/js/plugin/zclip/zclip.js"></script>--%>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>
    <style>
        a.copy:hover, a.copy {
            height: 18px;
            width: 18px;
        }

        a.qrcode:hover, a.qrcode {
            height: 18px;
            width: 18px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
            /**
             * 查看二维码
             * @param id
             */
            $(".qrcode").click(function () {
                var url = $(this).attr("url");
                layer.open({
                    type: 1,
                    title: "商品预览",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['200px', '240px'], //宽高
                    offset: "20%",
                    shade: [0.1, "#fff"],
                    content: "<img src ='/store/79B4DE7C/getTwoCode.do?url=" + url + "'/>"
                });
            });
        });
    </script>
</head>

<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>

<div class="body_div">
    <c:if test="${empty isNoAdminFlag }">
        <div class="con-head">
            <a class="navColor" href="/store/index.do">店铺管理</a>
            <a class="" href="/store/setindex.do">商城设置</a>
        </div>

        <div class="txt-btn pd-bottom-15 clearfix">
            <div class="srh">
                <div class="sbt" id="go"></div>
                <form id="queryForm" method="post" action="store/index.do">
                    <!-- <input type="hidden" name="moduleType" id="moduleType" value="0"> -->
                    <input type="text" placeholder="请输入店铺名称(模糊匹配)" value="${stoName }"
                           id="stoName" name=stoName class="srh">
                </form>
            </div>
            <div class="blue-btn fl right-15">
                <a href="javascript:void(0)" onclick="add('${countnum}')">新增</a>
            </div>
            <c:if test="${!empty videourl }">
                <div class="blue-btn fl right-15">
                    <a href='${videourl}' target='_blank'>教学视频</a>
                </div>
            </c:if>
        </div>


        <div class="box-btm30">

            <div class="txt-tle">
                <div class="t1">&nbsp;</div>
                <div class="t2" style="width: 18%;">店铺名称</div>
                <div class="t3" style="width: 22%;margin-right: 10px;">店铺地址</div>
                <div class="t3">联系人</div>
                <div class="t3">联系电话</div>
                <!-- <div class="t3" style="width: 8%;">是否分店</div> -->
                <div class="t3">操作</div>
            </div>

            <div class="msg-list">
                <div class="txt-tle2" id="list">
                    <c:forEach items="${page.subList}" var="sto">
                        <li>
                            <div class="lista list-checkbox">
                                <input type="checkbox" id="checkbox-msg0" value="${sto.id}"
                                       name="genre" onclick="event.stopPropagation()">
                            </div>
                            <div class="listb" style="width: 18%;">${sto.sto_name }</div>
                            <div class="listb" style="width:22%;margin-right: 10px;
                            <c:if test="${fn:length(sto.sto_address)>14}">line-height: 30px;</c:if>
                            <c:if test="${fn:length(sto.sto_address)<=14}">line-height: 60px;</c:if>">
                                <c:if test="${!empty sto.sto_address }">${sto.sto_address }</c:if>
                            </div>
                            <div class="listb">${sto.sto_linkman }</div>
                            <div class="listb">${sto.sto_phone }</div>
                                <%-- <div class="listb" style="width: 8%;">
                                    <c:if test="${sto.sto_is_main==1 }">
                                                    否
                                                </c:if>
                                    <c:if test="${sto.sto_is_main==2 }">
                                                    是
                                                </c:if>
                                </div> --%>
                            <div class="listd" style="width: 210px;">
                                <div class="bianji" style="width: 220px;position:relative;display:inline-block;">
                                    <a href="store/to_edit.do?id=${sto.id}" class="bj-a textEdit"></a>
                                    <a href="javascript:void(0)" class="bj-a textDelete" onclick="del(${sto.id})"></a>
                                    <c:if test="${!(wxPublicUsers.mchId==null||wxPublicUsers.mchId==''||wxPublicUsers.apiKey==null||wxPublicUsers.apiKey=='')}">
                                        <a target="_blank" class="bj-a download" href="${payUrl}/wxPay/3/${sto.id}/createPayQR.do?busId=${sto.sto_user_id}" title="下载支付二维码"></a>
                                    </c:if>
                                    <c:if test="${sto.pageId != null && sto.pageId != '' }">
                                        <a href="javascript:void(0);" class="bj-a qrcode" title="预览" url="/mallPage/${sto.pageId}/79B4DE7C/viewHomepage.do"></a>
                                        <a href="javascript:;" class="bj-a copy copy_public" title="复制链接"
                                           data-clipboard-text="${path }/mallPage/${sto.pageId}/79B4DE7C/viewHomepage.do" aria-label="复制成功！"></a>
                                    </c:if>
                                </div>
                                <c:if test="${sto.pageId != null && sto.pageId != '' }">
                                    <input type="hidden" class="link" value="${path }/mallPage/${sto.pageId}/79B4DE7C/viewHomepage.do"/>
                                </c:if>
                            </div>
                        </li>
                    </c:forEach>
                </div>
            </div>
        </div>
        <!-- 分页 -->
        <jsp:include page="/jsp/common/page/page.jsp"></jsp:include>

    </c:if>
    <c:if test="${!empty isNoAdminFlag }">
        <h1 class="groupH1"><strong>您还不是管理员，不能管理商城</strong></h1>
    </c:if>
</div>
</body>
<script type="text/javascript" src="/js/mall/store/index.js?<%=System.currentTimeMillis()%>"></script>
<script type="text/javascript">
    function add(obj) {
        if (obj > 0) {
            layer.msg("店铺已添加完毕，需要添加新店铺，请前往门店里面添加新的门店", {
                shade: [0.1, "#fff"],
                offset: "10%"
            });
        } else {
            window.location.href = "/store/to_edit.do";
        }
    }
</script>
</html>
