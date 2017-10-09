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

    <title>选择商品</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/edit1.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/table.js"></script>
    <script type="text/javascript">

        $(function () {
            $("#a-delete").remove();
        });

        /**点击确认**/
        function fnOk(obj) {
            var obj = $(obj).parents("li").find("input");
            var json = {};
            $(obj).parents("li").find("input").each(function () {
                if ($(this).attr("name") != undefined) {
                    json[$(this).attr("name")] = $(this).val();
                }
            });
            var isSpe = json.isSpe;
            if (isSpe == 1) {
                var proId = json.id;
                getProductId(proId, json);
            } else if (isSpe == 0) {
                parent.getProductGroup(json, null);//方法回调
                closeWindow();
            }
        }


        function fnCancel() {
            closeWindow();
        }

        function closeWindow() {
            //当你在iframe页面关闭自身时
            /*var index = layer.getFrameIndex(window.name); //先得到当前iframe层的索引
             layer.close(index); //再执行关闭*/
            parent.layer.closeAll();
        }

        function getProductId(proId, json) {
            $.ajax({
                type: "post",
                url: "mGroupBuy/getSpecificaByProId.do",
                data: {
                    proId: proId
                },
                dataType: "json",
                success: function (data) {
                    var proSpecArr = null;
                    if (data != null && data.list != null) {
                        proSpecArr = data.list;
                    }
                    parent.getProductGroup(json, proSpecArr);//方法回调
                    closeWindow();
                }

            });
        }
    </script>
    <style type="text/css">
        .srh {
            display: inline;
            float: none;
        }
    </style>
</head>
<body style="margin: 10px">
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div style="padding-bottom: 50px">
    <div class="txt-btn pd-bottom-15 clearfix">
        <div>
            <form id="queryForm" method="post" action="mGroupBuy/getProductByGroup.do">
                <input type="hidden" name="shopId" id="shopId" class="srh" value='<c:if test="${!empty map }">${map.shopId }</c:if>'/>
                <input type="hidden" name="defaultProId" id="defaultProId" class="srh" value="<c:if test="${!empty map }">${map.defaultProId }</c:if>"/>
                商品名称：<input type="text" placeholder="请输商品名称(模糊匹配)" value="<c:if test="${!empty map }">${map.proName }</c:if>"
                            id="proName" name="proName" class="srh" style="float: none;display: inline-block;">
                <%--  所属分类：
                <select style="width: 162px" id="groupId" name="groupId">
                    <option value="">全部分类</option>
                    <c:forEach items="${groLs }" var="gro">
                        <option ${gro.group_id==groupId?'selected':'' } value="${gro.group_id }">${gro.group_name }</option>
                    </c:forEach>
                </select>  --%>
                <input type="submit" value="查询" style="width:50px;  cursor: pointer;background-color: #1aa1e7;border-radius: 3px;color: #fff;border: none;"/>
            </form>
        </div>
    </div>

    <div class="box-btm30">
        <c:if test="${!empty page && !empty page.subList}">
            <div class="txt-tle">
                <div class="t1">&nbsp;</div>
                <div class="t2" style="width: 40%;">商品名称</div>
                <div class="t3" style="width: 37%;">创建时间</div>
            </div>
            <c:set var="isCommission" value="0"></c:set>
            <c:set var="isIntegral" value="0"></c:set>
            <c:if test="${!empty map }">
                <c:if test="${!empty map.isCommission }">
                    <c:set var="isCommission" value="${map.isCommission }"></c:set>
                </c:if>
                <c:if test="${!empty map.isIntegral }">
                    <c:set var="isIntegral" value="${map.isIntegral }"></c:set>
                </c:if>
            </c:if>
            <input type="hidden" class="isCommission" value="${isCommission }"/>
            <input type="hidden" class="isIntegral" value="${isIntegral }"/>
            <div class="msg-list">
                <div class="txt-tle2" id="list">

                    <c:forEach items="${page.subList}" var="pro">
                        <li>
                            <div name="title" class="listb" style="width: 40%;">${pro.pro_name }</div>
                            <div class="listb" style="width: 30%;overflow: hidden;">${pro.createTime }</div>
                            <c:set var="flag" value="true"></c:set>
                            <div class="listb" style="width: 26%;">
                                <c:if test="${isIntegral== 0 && isCommission == 0}">
                                    <c:if test="${pro.groupStatus == 1}"><!-- 正在进行团购的商品不能选取 -->
                                        <c:set var="flag" value="false"></c:set>
                                        不可选,已加入团购
                                    </c:if>
                                    <c:if test="${pro.seckillStatus == 1}"><!-- 正在进行秒杀的商品不能选取 -->
                                        <c:set var="flag" value="false"></c:set>
                                        不可选,已加入秒杀
                                    </c:if>
                                    <c:if test="${pro.auctionStatus == 1}"><!-- 正在进行拍卖的商品不能选取 -->
                                        <c:set var="flag" value="false"></c:set>
                                        不可选,已加入拍卖
                                    </c:if>
                                    <c:if test="${pro.presaleStatus == 1}"><!-- 正在进行预售的商品不能选取 -->
                                        <c:set var="flag" value="false"></c:set>
                                        不可选，已加入了预售
                                    </c:if>
                                    <c:if test="${pro.pifaStatus == 1}"><!-- 正在进行批发的商品不能选取 -->
                                        <c:set var="flag" value="false"></c:set>
                                        不可选，已加入了批发
                                    </c:if>
                                    <c:if test="${pro.pro_type_id > 0}"><!-- 虚拟商品不能加入 -->
                                        <c:set var="flag" value="false"></c:set>
                                        虚拟商品不能加入
                                    </c:if>
                                </c:if>
                                <c:if test="${!empty pro.sellerStatus && pro.sellerStatus == 1}">
                                    <c:set var="flag" value="false"></c:set>
                                    不可选，已设置了佣金
                                </c:if>
                                <c:if test="${isIntegral == 1 && pro.integralStatus == 1 }">
                                    <c:set var="flag" value="false"></c:set>
                                    不可选，已加入了积分商品
                                </c:if>
                                <c:if test="${flag}">
                                    <a href="javascript:void(0);" class="choosePro" onclick="fnOk(this)">选取</a>
                                </c:if>
                            </div>
                            <input type="hidden" name="productId" class="listb" value="">
                            <input type="hidden" name="id" class="listb" value="${pro.id }">
                            <input type="hidden" name="title" class="listb" value="${pro.pro_name }">
                            <input type="hidden" name="isSpe" class="listb" value="${pro.is_specifica }">
                            <input type="hidden" name="stockTotal" class="listb" value="${pro.stockTotal }">
                                <%-- <div name="src" class="listb" style="display: none">${pro.image_url }http://192.168.2.103:8080/upload//image/2/gh_b0a77493e00a/3/20160330/26A892A1D0A9AED358DF4499EBBC99C5.jpg</div> --%>
                            <c:choose>
                                <c:when test="${pro.is_specifica eq '0'}">
                                    <input type="hidden" name="src" class="listb"
                                           value="${http}${pro.image_url}">
                                </c:when>
                                <c:when
                                        test="${pro.is_specifica ne  '0'  && (pro.specifica_img_id eq '0' || pro.specifica_img_id eq '' || pro.specifica_img_id eq null) }">
                                    <input type="hidden" name="src" class="listb"
                                           value="${http}${pro.image_url}">
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="src" class="listb"
                                           value="${http}${pro.specifica_img_url }">
                                </c:otherwise>
                            </c:choose>
                            <c:if test="${pro.is_specifica eq '0'}">
                                <input type="hidden" name="price" class="listb" value="${pro.pro_price}">
                            </c:if>
                            <c:if test="${pro.is_specifica ne  '0'}">
                                <input type="hidden" name="price" class="listb" value=" ${pro.inv_price }">
                            </c:if>

                            <input type="hidden" name="url" class="listb"
                                   value=" ${url}/mallPage/${pro.id }/<c:if test="${!empty map }">${map.shopId }</c:if>/79B4DE7C/phoneProduct.do">
                        </li>
                    </c:forEach>
                </div>
            </div>
        </c:if>
        <c:if test="${empty page || empty page.subList}">
            请重新选择有商品的店铺
        </c:if>
    </div>

</div>

<c:if test="${!empty page && !empty page.subList}">
    <div style="width: 100%;height: 50px;text-align: center; position: fixed; bottom: 0;background:#fff;">
        <jsp:include page="/jsp/mall/pageView.jsp"></jsp:include>
    </div>
</c:if>
<script type="text/javascript">
    function page(curPage, url) {
        //获取查询参数,queryForm为表单ID
        /* var objName = $("#queryForm").find(".srh").eq(0).attr("name");
         var objValue = $("#queryForm").find(".srh").eq(0).val(); */
        var shopId = $('#shopId').val();
        var proName = $("#proName").val();
        url += curPage + "&shopId=" + shopId + "&proName=" + proName;
        var isCommission = $("input.isCommission").val();
        if (isCommission != null && isCommission != "" && typeof(isCommission) != "undefined") {
            url += "&isCommission=" + isCommission;
        }
        var isIntegral = $("input.isIntegral").val();
        if (isIntegral != null && isIntegral != "" && typeof(isIntegral) != "undefined") {
            url += "&isIntegral=" + isIntegral;
        }
        location.href = url;
    }
</script>
</body>
</html>
