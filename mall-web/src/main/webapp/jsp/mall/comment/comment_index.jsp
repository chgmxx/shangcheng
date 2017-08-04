<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商品管理-商品列表</title>
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
    <link rel="stylesheet" href="/css/mall/comment/common.css">
    <link rel="stylesheet" href="/css/mall/comment/evalServer.css">

    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js?<%= System.currentTimeMillis()%>"></script>
    <script src="/js/plugin/layer/layer.js"></script>

    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>

    <script type="text/javascript">
        var error = '${error}';
        if (error != undefined && error != "") {
            parent.layer.alert("参数错误，将调回前一个页面");
            window.history.back(-1);
        }
    </script>
    <style type="text/css">
    </style>
</head>
<body>
<div class="div_con">
    <div class="i-title vam" style="padding:0px;">
        <div class="con-head">
            <a class="" href="/mallOrder/toIndex.do">订单管理</a>
            <a class="navColor" href="/comment/to_index.do">评价管理</a>
        </div>

        <!-- <span class="verLine vam"></span>
        <span class="t-name vam">评价管理</span> -->
    </div>
    <!-- <div class="horLine"></div> -->
    <div class="i-main">
        <input type="hidden" class="checkStatus"/>
        <input type="hidden" class="feel" value="${feel }"/>
        <ul class="i-menu clearfix">
            <li class="<c:if test='${empty checkStatus && empty feel}'>cmenu-w</c:if><c:if test='${!empty checkStatus || !empty feel}'>cmenu-g</c:if>">
                <a href="/comment/to_index.do" class="li-a">全部</a>
            </li>
            <li class="<c:if test='${feel == 1 && !empty feel}'>cmenu-w</c:if><c:if test='${feel != 1 || empty feel}'>cmenu-g</c:if>">
                <a href="/comment/to_index.do?feel=1" class="li-a">好评</a>
            </li>
            <li class="<c:if test='${feel == 0 && !empty feel}'>cmenu-w</c:if><c:if test='${feel != 0 || empty feel}'>cmenu-g</c:if>">
                <a href="/comment/to_index.do?feel=0" class="li-a">中评</a>
            </li>
            <li class="<c:if test='${feel == -1 && !empty feel}'>cmenu-w</c:if><c:if test='${feel != -1 || empty feel}'>cmenu-g</c:if>">
                <a href="/comment/to_index.do?feel=-1" class="li-a">差评</a>
            </li>
            <li class="<c:if test='${checkStatus == 1 && !empty checkStatus}'>cmenu-w</c:if><c:if test='${checkStatus != 1 || empty checkStatus}'>cmenu-g</c:if>">
                <a href="/comment/to_index.do?checkStatus=1" class="li-a">已通过</a>
            </li>
            <li class="<c:if test='${checkStatus == -1 && !empty checkStatus}'>cmenu-w</c:if><c:if test='${checkStatus != -1 || empty checkStatus}'>cmenu-g</c:if>">
                <a href="/comment/to_index.do?checkStatus=-1" class="li-a">未通过</a>
            </li>
            <li class="<c:if test='${checkStatus == 0 && !empty checkStatus}'>cmenu-w</c:if><c:if test='${checkStatus != 0 || empty checkStatus}'>cmenu-g</c:if>">
                <a href="/comment/to_index.do?checkStatus=0" class="li-a">未审核</a>
            </li>
        </ul>
        <div class="drawLine">
            <div class="sHorizontalLine"></div>
            <div class="lHorizontalLine"></div>
        </div>
        <div class="i-table">
            <ul class="ul-th clearfix">
                <li class="td-1 td-title">
                    <input type="checkbox" name="check" id="checkAll" class="allCheck" onclick="showMore(this)">
                </li>
                <li class="td-4 td-title">评价</li>
                <li class="td-3 td-title">商品</li>
                <li class="td-3 td-title">订单号</li>
                <li class="td-3 td-title">所属店铺</li>
                <li class="td-3 td-title">评论时间</li>
                <li class="td-2 td-title">状态</li>
                <li class="td-2 td-title">操作</li>
            </ul>
            <c:if test="${!empty page }">
                <c:forEach var="comment" items="${page.subList }">
                    <c:set var="feel" value=""></c:set>
                    <c:if test="${comment.feel == 1 }">
                        <c:set var="feel" value="好评"></c:set>
                    </c:if>
                    <c:if test="${comment.feel == 0 }">
                        <c:set var="feel" value="中评"></c:set>
                    </c:if>
                    <c:if test="${comment.feel == -1 }">
                        <c:set var="feel" value="差评"></c:set>
                    </c:if>
                    <div class="tr-wrap clearfix">
                        <ul class="ul-tr clearfix">
                            <li class="td-1">
                                <input type="checkbox" name="check" class="checkComment" id="${comment.id }">
                                <input type="hidden" class="checkStatus" value="${comment.check_status }"/>
                            </li>
                            <li class="td-4">
                                <p class="co-con">
                                    <span class="blank fw">[&nbsp;${feel }&nbsp;]</span>
                                        ${comment.content }</p>
                                <c:if test="${!empty comment.imageList }">
                                    <ul class="co-img-ul clearfix">
                                        <c:forEach var="imageObj" items="${comment.imageList }">
                                            <li><i class="co-img" style="background-image: url('${imgUrl}${imageObj.imageUrl}');"
                                                   onclick="showImages('${imgUrl}${imageObj.imageUrl}');"></i></li>
                                        </c:forEach>
                                    </ul>
                                </c:if>
                            </li>
                            <li class="td-3">
                                    ${comment.det_pro_name }<br/>
                            </li>
                            <li class="td-3">${comment.order_no }</li>
                            <li class="td-3">${comment.sto_name }</li>
                            <li class="td-3">${comment.createTime }</li>
                            <li class="td-2">
                                <p class="pb10">
                                    <c:if test="${comment.check_status == 0 && !empty isOpenCheck && isOpenCheck == 1}">
                                        <c:if test="${comment.check_status == 0 }">未审核</c:if>
                                        <c:if test="${comment.check_status == 1 }">通过</c:if>
                                        <c:if test="${comment.check_status == -1 }">不通过</c:if>
                                    </c:if>
                                </p>
                                <c:if test="${comment.is_rep == 0 }">
                                    <a href="javascript:void(0)" class="a-blue" onclick="replayBox(this)">回复</a>
                                </c:if>
                            </li>
                            <li class="td-2">
                                <c:if test="${comment.check_status == 0 && !empty isOpenCheck && isOpenCheck == 1}">
                                    <a href="javascript:void(0)" class="a-edit a-check" id="${comment.id }" title="审核通过"></a>
                                    <a href="javascript:void(0)" class="a-edit a-uncheck" id="${comment.id }" title="审核不通过"></a>
                                </c:if>
                                <a href="javascript:void(0)" class="a-edit a-del" id="${comment.id }" title="删除"></a>
                            </li>
                        </ul>
                        <c:if test="${comment.is_rep == 0 }">
                            <div class="co-replay" style="display: none">
                                <!-- <input type="text" class="replay-box repContent"> -->
                                <p class="inputEle repContent" contenteditable="true"></p>
                                <div class="fr ib">
                                    <input type="hidden" class="commentPId" value="${comment.id }">
                                    <input type="hidden" class="shop_id" value="${comment.shop_id }">
                                    <span class="show-emotion">
	                        <i class="co-emotion vam"></i>
	                        <i class="tri-g vam"></i>
                        </span>
                                    <a href="javascript:void(0)" class="a-blue subRepComBtn" onclick="">发表</a>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${!empty comment.chilComment }">
                            <div class="co-replayed">
                                <p class="fs2" style="width:25.2%"><span class="blank fw">回复：</span>${comment.chilComment.content }</p>
                            </div>
                        </c:if>
                    </div>
                </c:forEach>

                <div class="list-footer-region ui-box">
                    <c:if test="${!empty page.subList }">
                        <input type="hidden" id="taskId" value="0"/>
                        <jsp:include page="page_comment.jsp"></jsp:include>
                    </c:if>
                </div>
            </c:if>
        </div>
    </div>

</div>
<div class="layers" style="display: none;">
    <div class="imgDiv" style="display: -webkit-box;-webkit-box-orient: horizontal;
    -webkit-box-pack: center;-webkit-box-align: center; display: box;
    box-orient: horizontal; box-pack: center;box-align: center;min-height:400px;">
        <img src='' style="width:100%;"/>
    </div>
</div>
<script type="text/javascript" src="/js/mall/comment/comment_index.js"></script>
<script src="/js/mall/jquery.qqFace.js"></script>
<script type="text/javascript">
    /*全选 反选*/
    var _array = $("input[name='check']");
    function showMore(obj) {
        if ($(obj).is(":checked")) {
            for (var i = 0; i < _array.length; i++) {
                _array[i].checked = true;
            }
        } else {
            for (var i = 0; i < _array.length; i++) {
                _array[i].checked = false;
            }
        }
    }
    $(".checkComment").click(function () {
        if (!$(this).is(":checked")) {
            $(".allCheck").removeAttr("checked");
        } else {
            var flag = true;
            $(".checkComment").each(function () {
                if (!$(this).is(":checked")) {
                    flag = false;
                }
            });
            if (flag) {
                $(".allCheck").attr("checked", "checked");
            }
        }
    });
    //全选
    $(".js-batch-checkAll").click(function () {
        $(".allCheck").attr("checked", "checked");
        showMore($(".allCheck"));
    });


    //显示表情
    function showEmoj() {
        if ($(".show-emotion").length > 0) {
            $(".show-emotion").qqFace({
                id: "facebox",
                assign: 'inputEle',
                path: '/images/activity/miniscene/phone/face/'
            });
        }
        load();

    }
    $(".show-emotion").click(function () {
        load(200);
    });
    $(function () {
        showEmoj();
    })
    $(document).on("click", function (event) {
        if ($(event.target).closest(".qqFace").length <= 0) {
            $('.qqFace').hide();
            $('.qqFace').remove();
            $(".tri-g").removeClass("rot180");
            load();
        }
    });

    //显示回复框
    function replayBox(obj) {
        var parent = $(obj).parents("div.tr-wrap");
        $("div.co-replay", parent).show();
        load();
    }

    load();
    function load(length) {
        loadWindow();
    }

    function showImages(img) {
        $(".layers img").attr("src", img);
        var index = parent.layer.open({
            type: 1,
            content: $(".layers").html(),
            maxmin: true,
            offset: "10%",
        });
        //parent.layer.full(index);
    }

</script>

</body>
</html>