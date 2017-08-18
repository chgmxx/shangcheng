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

    <title>h5商城列表</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <link rel="stylesheet" type="text/css"  href="/css/mall/html/htmllist.css?<%= System.currentTimeMillis()%>"/>
    <script type="text/javascript"  src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <%--<script type="text/javascript"  src="/js/zclip/zclip.js?<%= System.currentTimeMillis()%>"></script>--%>
    <style>
        .share {
            background: url(/images/icon/exportiocn.jpg) -27px 21px;
            display: inline-block;
            width: 18px;
            height: 18px;
        }

        .share:hover {
            background: url(/images/icon/exportiocn.jpg) -2px 21px;
        }
    </style>
</head>

<body>
<c:if test='${ispid eq 2 }'>
    <h1 class="groupH1"><strong>您还不是管理员，不能管理商城</strong></h1>
</c:if>
<c:if test='${ispid ne 2 }'>
    <input type="hidden" id="changgeidimg" value="">
    <div class="fansTitle">
        <span class="titleClass fl"></span><span class=""
                                                 style="font-size:1.7em;color: #444444;padding-left: 20px;">H5商城</span>
    </div>
    <div class="con-box1">
        <div class="txt-btn">
            <div class="blue-btn fl">
                <span onclick="add('${iscreat}','${ispid }')">新增</span>
            </div>
            <c:if test="${!empty videourl }">
                <div class="blue-btn fl right-15">
                    <a href='${videourl}' class="btn" target='_blank' style="color: white;">教学视频</a>
                </div>
            </c:if>
        </div>
    </div>
    <!--内容层开始-->
    <div class="bom-btm30 con-dan">
        <ul class="sencelist top-30">
            <c:if test="${! empty map.list}">
                <c:forEach var="obj" items="${map.list}">
                    <li class="licode" style="position: relative;">
                        <p title="修改标题" class="txthidden fl">
                            标题： <span id="contentTitle"> <span
                        >${obj.htmlname}</span>
							</span>
                        </p>
                        <div class="chose" id="chose" style="overflow:hidden">
                            <div title="删除">
                                <div class="close" onclick="del(${obj.id});"></div>
                            </div>
                            <div title="设计页面">
                                <a href="javascript:void(0)"
                                   onclick="designPage(${obj.id })"> <span class="edit"></span>
                                </a>
                            </div>
                            <div title="背景图">
                                <div class="scenePic" onclick="uploadImg('${obj.id}');"></div>
                            </div>
                            <div title="复制链接">
                                <a href="javascript:;" class="sceneLink copy copy_public" title="复制链接"
                                   data-clipboard-text="${http}/mallhtml/${obj.id}/79B4DE7C/phoneHtml.do" aria-label="复制成功！"></a>
                                <%--<div class="sceneLink copyvalue">--%>
                                    <%--<input type="hidden" class="weblink" value="${http}/mallhtml/${obj.id}/79B4DE7C/phoneHtml.do"/>--%>
                                <%--</div>--%>
                            </div>

                            <div title="表单">
                                <div class="bj-a share" onclick="formlist(${obj.id })"></div>
                            </div>
                            <div title="修改信息">
                                <div class="edit" style="cursor:hand"
                                     onclick="update(${obj.id})"></div>
                            </div>

                        </div>
                        <div class="senceCont" id="imgtxt">
                            <a id="pic${obj.id}"> <img
                                    data-original="${applicationScope.resourceUrl}${obj.bakurl}"
                                    alt="图文图片"
                                    src="${applicationScope.resourceUrl}${obj.bakurl}"
                                    width="224px;" height="110px;"/>
                            </a>
                        </div>
                        <div class="divcodeimg" style="display:none">
                            <img src="${applicationScope.resourceUrl}${obj.codeUrl}"
                                 alt="h5商城二维码" class="codeimg"
                                 style="z-index:1002; height: 200px;width: 200px;position: absolute;left: 50%; margin-left: -100px;top: 50%;margin-top: -100px;">
                        </div>
                        <div class="codeMask"
                             style="display:none;position:absolute; background-color: black;z-index:1001;-moz-opacity: 0.2;opacity:.20;filter: alpha(opacity=20);width:100%;height:100%">
                        </div>
                    </li>
                </c:forEach>
            </c:if>
        </ul>
    </div>

    <hr style="border: 1px solid #dedede;"/>
    <c:if test="${! empty map.list}">
        <div class="box-btm30 clearfix">
            <div class="page">
                <ul>
                    <li class="pgfocus"><a
                            href="javascript:first(${map.pageNum});">首页</a></li>
                    <li class="pgfocus"><a
                            href="javascript:previous(${map.pageNum});">上一页</a></li>
                    <li class="pgfocus current">第${map.pageNum}页</li>
                    <li class="pgfocus"><a
                            href="javascript:next(${map.pageNum},${map.pagetotal});">下一页</a></li>
                    <li class="pgfocus"><a
                            href="javascript:last(${map.pageNum},${map.pagetotal});">尾页</a></li>
                    <li class="pgfocus">共${map.pagetotal}页</li>
                </ul>
            </div>
        </div>
    </c:if>
    <form id="xinfrom" method="post" action="/mallhtml/updateHtml.do" target="_blank">
        <input type="hidden" name="id" id="id">
    </form>
</c:if>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
</body>
<script type="text/javascript">

    $(function () {
        $(function () {
            //鼠标移动到这里的样式
            $('.licode').mouseover(function () {
                $(this).find(".divcodeimg").css("display", "block");
                $(this).find(".codeMask").css("display", "block");

            });
            //鼠标离开在li上的效果
            $('.licode').mouseout(function () {
                $(this).find(".codeMask").css("display", "none");
                $(this).find(".divcodeimg").css("display", "none");
            });

        })
//        $(".copyvalue").zclip({
//            path: 'js/zclip/ZeroClipboard.swf',
//            copy: function () {//复制内容
//                return $(this).find(".weblink").val();
//            },
//            afterCopy: function () {//复制成功
//                parent.layer.alert("复制成功！", {
//                    icon: 6,
//                    offset: "30%",
//                    closeBtn: 0
//                });
//            }
//        });
    })

    function formlist(id) {
        window.location.href = "/mallhtml/htmlfromlist.do?id=" + id;
    }
    //添加
    function add(iscreat, ispid) {
        if (iscreat == 1) {
            if (ispid == 0) {

                layer.confirm("等级不够，不能在创建h5商城，请前往<a href='/trading/upGrade.do?setType=trading' style='text-decoration: none;color:red'>续费升级级别</a>", {shade:[0.1,"#fff"],offset: '25%'}, function () {
                    top.location.href = "/trading/upGrade.do?setType=trading";
                })
            } else {
                layer.alert("主账户等级不足，不能在创建h5商城", {
                    offset: "30%",
                    shade:[0.1,"#fff"],
                    closeBtn: 0
                });
            }
        } else {
            window.location.href = "/mallhtml/modelList.do";
        }
    }
    function update(id) {
        window.location.href = "/mallhtml/addOrUpdate.do?id=" + id;
    }
    function del(id) {
        layer.confirm("确定删除吗？删除之后，引用该链接的页面，将会以找不到页面，跳转到404页面中", {shade:[0.1,"#fff"],offset: '25%'}, function () {
            $.post("mallhtml/delect.do", {
                id: id
            }, function (data) {
                if (data.error == 1 || data.error == '1') {
                    layer.alert("删除失败", {
                        offset: "30%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                } else {
                    layer.alert("删除成功", {
                        offset: "30%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                    window.location.href = "/mallhtml/htmllist.do";
                }
            });
        });
    }
    function designPage(id) {
        $("#id").val(id);
        $("#xinfrom").submit();//post 请求弹出新页面
    }


    function first(obj) {
        var currentPage = obj;
        if (1 < currentPage) {
            window.location.href = "/mallhtml/htmllist.do?pageNum=1";

        } else {
            layer.alert("已经是最前一页", {
                offset: "30%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }
    function previous(obj) {
        var currentPage = obj;
        if (1 < currentPage) {

            var url = "/mallhtml/htmllist.do?pageNum=${map.pageNum-1}";


            window.location.href = url;

        } else {
            layer.alert("已经是最前一页", {
                offset: "30%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }
    function next(obj, totalPages) {
        var currentPage = obj;
        if (currentPage < totalPages) {

            var url = "/mallhtml/htmllist.do?pageNum=${map.pageNum+1}";

            window.location.href = url;

        } else {
            layer.alert("已经是最后一页", {
                offset: "30%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }
    function last(obj, totalPages) {
        var currentPage = obj;
        if (currentPage < totalPages) {

            var url = "/mallhtml/htmllist.do?pageNum=${map.pagetotal}";


            window.location.href = url;

        } else {
            layer.alert("已经是最后一页", {
                offset: "30%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }
    }

    //背景图切换
    function uploadImg(id) {
        $("#changgeidimg").val(id);
        materiallayer();
    }

    function fhmateriallayer(imgid, imgurl) {
        var id = $("#changgeidimg").val();
        $.ajax({
            url: "/mallhtml/updateimage.do",
            type: "POST",
            dataType: "json",
            data: {id: id, imgurl: imgurl},
            success: function (data) {
                if (data.error == 0) {
                    layer.alert("替换成功", {
                        offset: "30%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                    $("#pic" + id).find("img").attr("src", (imgurl));
                    $("#pic" + id).find("img").attr("data-original", (imgurl));
                } else {
                    layer.alert("替换失败", {
                        offset: "30%",
                        shade:[0.1,"#fff"],
                        closeBtn: 0
                    });
                }
            }
        })
    }
</script>
</html>
