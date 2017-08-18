<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>商品管理-编辑商品</title>
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
    <link rel="stylesheet" type="text/css" href="/css/common.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-ui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        var groupObj = new Object();
        var imgResource = "${imgUrl}";

    </script>

</head>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<c:set var="isFirstParents" value="1"></c:set>
<c:set var="groupPId" value="0"></c:set>
<c:set var="pIds" value="0"></c:set>
<c:set var="isChild" value="0"></c:set>
<c:set var="selectShopId" value="0"></c:set>
<c:if test="${!empty group }">
    <c:set var="isFirstParents" value="${group.isFirstParents }"></c:set>
    <c:set var="groupPId" value="${group.groupPId }"></c:set>
    <c:set var="isChild" value="${group.isChild }"></c:set>
    <c:set var="pIds" value="${group.groupPId }"></c:set>
</c:if>
<c:if test="${empty group && !empty  parentGroup}">
    <c:set var="isFirstParents" value="0"></c:set>
</c:if>
<div id="con-box">
    <div class="con-head" style="margin:0px;">
        <a class="" href="/mPro/index.do">商品管理</a>
        <a class="navColor" href="/mPro/group/group_index.do">分组管理</a>
        <a class="" href="/mPro/group/label_index.do">搜索推荐管理</a>
    </div>
</div>
<input type="hidden" class="urls" value="${urls }"/>
<div id="newGroup" style="margin-top: 30px;">
    <div class="groupDiv">
        <span class="font14">分组名称：&nbsp;&nbsp;&nbsp; </span>
        <input type="hidden" name="id" id="id" value="<c:if test="${!empty group }">${group.id }</c:if>">
        <input type="text" class="inpt" placeholder="" name="groupName" id="groupName"
               value="<c:if test="${!empty group }">${group.groupName }</c:if>">
        <span class="red">分组名称最多输入6位汉字或12位字符</span>
        <input type="hidden" name="isFirstParents" id="isFirstParents" value="${isFirstParents }">
        <input type="hidden" name="isChild" id="isChild" value="${isChild }">
    </div>
    <c:if test="${!empty parentGroup }">
        <div class="groupDiv">
            <span class="font14">父级分类：&nbsp;&nbsp;&nbsp; </span> ${parentGroup.groupName }
            <input type="hidden" name="pId" id="pId" value="${parentGroup.id }">
            <c:set var="selectShopId" value="${parentGroup.shopId }"></c:set>
        </div>
    </c:if>
    <%-- <c:if test="${!empty pGroupList }">
    <div class="groupDiv">
        <span class="font14">父级分类：&nbsp;&nbsp;&nbsp; </span> ${parentGroup.groupName }
        <select name="parentGroups" class="parentGroups">
            <c:forEach var="parent" items="${pGroupList }">
                <option value="${parent.id }" >${parent.groupName }</option>
            </c:forEach>
        </select>
    </div>
    </c:if> --%>
    <div class="groupDiv">
        <span class="font14">第一级优先：</span>
        <select name="firstPriority" class="firstPriority">
            <option value="1" <c:if test="${!empty group && group.firstPriority == 1}"> selected="selected"</c:if>
            >序号越大越靠前
            </option>
            <option value="2" <c:if test="${!empty group && group.firstPriority == 2}"> selected="selected"</c:if>
            >最热的排在越前
            </option>
        </select>
    </div>
    <div class="groupDiv" style="display: none;">
        <span class="font14">第二级优先：</span>
        <select name="secondPriority" class="secondPriority">
            <option value="3" <c:if test="${!empty group && group.secondPriority == 3}"> selected="selected"</c:if> >创建时间越晚排在越前</option>
            <option value="4" <c:if test="${!empty group && group.secondPriority == 4}"> selected="selected"</c:if> >创建时间越早排在越前</option>
        </select>
    </div>
    <div class="groupDiv">
        <span class="font14">所属店铺：</span>
        <c:if test="${!empty shopList }">
            <select name="shopId" class="shopId" id="<c:if test="${!empty group}">${group.shopId }</c:if>"
                    <c:if test="${(!empty group && !empty group.shopId) || pId > 0}">disabled='disabled'</c:if>>
                <c:forEach var="shop" items="${shopList }">
                    <option value="${shop.id }" <c:if test="${selectShopId > 0 && selectShopId==shop.id}"> selected="selected"</c:if>>${shop.sto_name }</option>
                </c:forEach>
            </select>
        </c:if>
    </div>
    <div class="groupDiv" style="display:none;">
        <span class="font14">分组图片：</span>
        <img onclick="choosePicture()" class="abc" src="../../../../../images/add_Image.png" width="50" style="cursor: pointer;"/>
        <img id="img1" src="${res.resPicture}" width="50"/>
        <input type="hidden" value="${res.resPicture }" name="resPicture" id="resPicture"/>
    </div>
    <div class="groupDiv" style="display:none;">
        <span class="font14"> </span>
        <span class="font15">
			<input type="checkbox" value="1" name="isShowPage" class="isShowPage"
                    <c:if test="${!empty group && group.isShowPage == 1}"> checked="checked"</c:if>
			<c:if test="${empty group}"> checked="checked"</c:if>/>是否在页面显示分组名称</span>
    </div>
    <div class="groupDiv">
        <span class="font14">排&nbsp;&nbsp;&nbsp;&nbsp;序：</span>
        <input type="text" class="inpt" placeholder="" name="sort" id="sort" size="4" maxlength="4"
               value="<c:if test="${!empty group }"> ${group.sort }</c:if><c:if test="${empty group }">0</c:if>">
        <span class="red">默认升级排序，最多能输入四位数字</span>
    </div>
    <div class="groupDiv">
        <span class="font14">分类图片：</span>
        <ul class="picture-list app-image-list clearfix" id="ui-sortable">
            <c:if test="${!empty imageList}">
                <c:forEach var="image" items="${imageList }">
                    <li class="sort delParent"><img
                            src="${imgUrl}${image.imageUrl }"/> <a
                            class="js-delete-picture close-modal small hide"
                            onclick="delImg(this)">×</a> <input type='hidden'
                                                                class='imageInp' value="${image.imageUrl }"/> <input
                            type='hidden' class='imageId' value="${image.id }"/></li>
                    <script type="text/javascript">
                        groupObj["${image.id }"] = "${image.id }";
                    </script>
                </c:forEach>
            </c:if>
            <li class="sort delParent">
                <!-- <img src="/images/addPic.jpg"/> --> <a
                    href="javascript:;" class="add-goods js-add-picture"
                    onclick="materiallayer(this)">+加图</a>
            </li>
        </ul>
    </div>
    <div class="groupDiv">
        <a href="javascript:editGroup();" class="addBtn">确定</a>
        <a href="${urls }" style="cursor: pointer;color:white;" class="backBtn">返回</a>
    </div>
</div>
<script type="text/javascript" src="/js/mall/group.js"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);

    /* jqueryUI 拖拽初始化 */
    $("#ui-sortable").sortable();
    $("#ui-sortable").disableSelection();
</script>
</body>
</html>