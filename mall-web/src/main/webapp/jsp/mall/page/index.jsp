<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">

    <title>页面管理</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/edit1.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css?<%= System.currentTimeMillis()%>"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/table.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/mall/page/index.js"></script>
    <script type="text/javascript" src="/js/common/copy.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/clipboard.min.js"></script>
    <script type="text/javascript" src="/js/plugin/copy/copypublic.js"></script>

    <style>
        .bj-record {
            background: url("/images/main_edit.png") -65px 0;
        }

        .bj-record:hover {
            background: url("/images/main_edit.png") 40px 0px;
        }

        .edit-page {
            background: url("/images/main_edit.png");
        }

        .edit-page:hover {
            background: url("/images/main_edit.png") -22px 0;
        }

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
                SonScrollTop(0);
                setTimeout(function () {
                    layer.open({
                        type: 1,
                        title: "页面预览",
                        shade: [0.1, "#fff"],
                        skin: 'layui-layer-rim', //加上边框
                        area: ['200px', '240px'], //宽高
                        offset: scrollHeight + "px",
                        content: "<img src ='/store/79B4DE7C/getTwoCode.do?url=" + url + "'/>"
                    });
                }, timeout);
            });
        });
    </script>
</head>

<body>
<input type="hidden" class="urls" value="${urls }"/>
<div class="con_body">
    <jsp:include page="/jsp/common/headerCommon.jsp"/>
    <c:if test="${empty isNoAdminFlag }">
        <!--post 请求。弹出新页面 -->
        <form id="xinfrom" method="post" action="/mallPage/designPage.do" target="_blank">
            <input type="hidden" name="id" id="id">
        </form>
        <div class="fansTitle">
            <span class="i-con fl"></span><span class="title-p">页面管理</span>
        </div>

        <div class="txt-btn pd-bottom-15 clearfix">
            <div class="srh">
                <div class="sbt" id="go"></div>
                <form id="queryForm" method="post" action="mallPage/index.do">
                    <!-- <input type="hidden" name="moduleType" id="moduleType" value="0"> -->
                    <input type="text" placeholder="请输入页面名称(模糊匹配)" value="${pagName }"
                           id="pagName" name="pagName" class="srh">
                </form>
            </div>
            <div class="blue-btn fl right-15">
                <a href="mallPage/to_edit.do">新增</a>
            </div>
            <c:if test="${!empty videourl }">
                <div class="blue-btn fl right-15">
                    <a href='${videourl}' class="btn" target='_blank'>教学视频</a>
                </div>
            </c:if>
        </div>

        <div class="box-btm30">

            <div class="txt-tle">
                <div class="t1">&nbsp;</div>
                <div class="t2">页面名称</div>
                <div class="t2">店铺名称</div>
                <div class="t3">所属分类</div>
                <div class="t3">创建时间</div>
                <div class="t3">是否主页</div>
                <div class="t3">操作</div>
            </div>

            <div class="msg-list">
                <div class="txt-tle2" id="list">
                    <c:forEach items="${page.subList}" var="pag">
                        <li>
                            <div class="lista list-checkbox">
                                    <%-- <input type="checkbox" id="checkbox-msg0" value="${pag.id}"
                                        name="genre" onclick="event.stopPropagation()"> --%>
                            </div>
                            <div class="listb">${pag.pag_name }</div>
                            <div class="listb">

                                    ${pag.business_name}

                            </div>
                            <div class="listb">${pag.item_value}</div>
                            <div class="listb">${pag.pag_create_time }</div>
                            <div class="listb">
                                <c:if test="${pag.pag_is_main==0 }">
                                    否
                                </c:if>
                                <c:if test="${pag.pag_is_main==1 }">
                                    是
                                </c:if>
                            </div>
                            <div class="listd" style="width:20%;">
                                <div class="bianji" style="width: 185px;position:relative;display:inline-block;">
                                    <a href="mallPage/to_edit.do?id=${pag.id}"
                                       class="bj-a textEdit" title="编辑"></a>
                                    <a href="javascript:void(0)"
                                       class="bj-a textDelete" onclick="del(${pag.id})" title="删除"></a>
                                    <a href="javascript:void(0);" class="bj-a bj-record"
                                       onclick="setMian('${pag.id }','${pag.pag_sto_id}')" title="设为主页">
                                    </a>
                                    <a href="javascript:void(0);" onclick="designPage(${pag.id })" target="_blank" class="bj-a  edit-page"
                                       title="设计页面">
                                    </a>
                                    <c:if test="${pag.codeUrl != null && pag.codeUrl != '' }">
                                        <a href="javascript:;" class="bj-a qrcode" title="预览" url="/mallPage/${pag.id}/79B4DE7C/viewHomepage.do"></a>
                                    </c:if>
                                    <a href="javascript:;" class="bj-a copy copy_public" title="复制链接" data-clipboard-text="${path }/mallPage/${pag.id}/79B4DE7C/viewHomepage.do"
                                       aria-label="复制成功！"></a>
                                </div>
                                <input type="hidden" class="link" value="${path }/mallPage/${pag.id}/79B4DE7C/viewHomepage.do"/>
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
<script type="text/javascript">
    $(function () {
        $("#div-allchoose").css("display", "none"); //隐藏掉全选按钮，删除按钮
    });

</script>
</html>
