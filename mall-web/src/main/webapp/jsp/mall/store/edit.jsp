<%@ page contentType="text/html; charset=UTF-8" language="java" %>
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
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <script charset="utf-8" type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript" src="/js/mall/store/edit.js?<%=System.currentTimeMillis()%>"></script>
</head>

<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="con-head">
    <a class="navColor" href="/store/index.do">店铺管理</a>
    <a class="" href="/store/setindex.do">商城设置</a>
</div>

<div class="fansTitle">
    <span class="i-con fl"></span><span class="title-p">${pageTitle }</span>
</div>

<table class="table2" id="tab">
    <c:set var="isWxShopId" value="0"></c:set>
    <tr id="chooseTr">
        <td class="table-td1">选择门店：${shopsid }</td>
        <td class="table-td3" style="width:auto;">
            <!-- <input type="button" id="chooseFd" value="请选择" class="blue-btn" onclick="chooseFd()"/> -->
            <%-- <input type="hidden" id="stoBranchId" name="stoBranchId" value="${sto.stoBranchId }"/> --%>
            <c:if test="${!empty shopList }">
                <select class="text1 " id="wxShopId" name="wxShopId" onchange="shopChange(this)"
                        <c:if test="${!empty sto.wxShopId && sto.wxShopId > 0 }">disabled="disabled"</c:if>
                >
                    <option value="-1">请选择</option>
                    <c:forEach items="${shopList }" var="shops">
                        <option value="${shops.id }" <c:if test="${!empty sto.wxShopId && sto.wxShopId == shops.id }">selected='selected'</c:if>>${shops.businessName}</option>
                        <c:if test="${!empty sto.wxShopId && sto.wxShopId == shops.id }">
                            <c:set var="isWxShopId" value="1"></c:set>
                        </c:if>
                    </c:forEach>
                </select>
                <input type="hidden" class="isWxShopId" value="${isWxShopId }"/>
                <div style="display:none;">
                    <c:forEach items="${shopList }" var="shops">
						<span class="shopSpan" id="${shops.id }">
							<input type="hidden" ids="id" value="${shops.id }"/>
							<input type="hidden" ids="latitude" value="${shops.latitude }"/>
							<input type="hidden" ids="longitude" value="${shops.longitude }"/>
							<%--<input type="hidden" ids="url" value="${shops.url }"/>--%>
							<input type="hidden" ids="province" value="${shops.province }"/>
							<input type="hidden" ids="city" value="${shops.city }"/>
							<input type="hidden" ids="district" value="${shops.district }"/>
							<input type="hidden" ids="name" value="${shops.businessName }"/>
							<input type="hidden" ids="adder" value="${shops.adder }"/>
							<input type="hidden" ids="address" value="${shops.address }"/>
							<input type="hidden" ids="detail" value="${shops.detail }"/>
							<input type="hidden" ids="startTime" value="${shops.startTime }"/>
							<input type="hidden" ids="telephone" value="${shops.telephone }"/>
							<input type="hidden" ids="imageUrl" value="${http }${shops.imageUrl }"/>
						</span>
                    </c:forEach>
                </div>
            </c:if>
            <c:if test="${empty shopList }">
                <a href="${wxmpDomain}/wxShop/indexstart.do" target="_blank" class="tColor" style="font-size:18px;"><h1 class="tColor">请前往门店管理继续添加新的门店</h1></a>
            </c:if>
            <span id="picCodePrompt" class="tColor"><c:if test="${sto.wxShopId == 0 || isWxShopId == 0}">请重新选择门店</c:if></span>
            <%-- <input type="hidden" id="wxShopId" name="wxShopId" value="${sto.wxShopId }"/> --%>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr>
        <td class="table-td1">店铺名称：</td>
        <td class="table-td3" style="width:auto;">
            <input type="hidden" value="${sto.id }" name="id" id="id"/>
            <input type="hidden" value="${sto.stoQrCode }" name="stoQrCode" id="stoQrCode"/>
            <input type="text" class="text1 abc" id="stoName" name="stoName" placeholder="请输入店铺名称" required="required" value="${sto.stoName }"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr>
        <td class="table-td1">店铺图片：</td>
        <td class="table-td3" style="width:auto;">
            <c:if test="${empty sto.stoPicture }">
                <img onclick="choosePicture('stoPicture')" class="abc imgURL" src="/images/mall/add_Image.png" width="50" style="cursor: pointer;"/>
            </c:if>
            <img id="img1" class="stoPicture" src="${http}${sto.stoPicture}" width="50"
                 <c:if test="${empty sto.stoPicture}">style="display:none;"</c:if> />
            <input type="hidden" value="${sto.stoPicture }" name="stoPicture" id="stoPicture"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr>
        <td class="table-td1">店铺头像：</td>
        <td class="table-td3" style="width:auto;">
            <img onclick="choosePicture('stoHeadImg')" class="imgURL" src="/images/mall/add_Image.png" width="50" style="cursor: pointer;"/>
            <img id="img2" class="stoHeadImg" src="${http}${sto.stoHeadImg}" width="50" <c:if test="${empty sto.stoHeadImg}">style="display:none;"</c:if>/>
            <input type="hidden" value="${sto.stoHeadImg }" name="stoHeadImg" id="stoHeadImg"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>


    <tr>
        <td class="table-td1">选择地址：</td>
        <td class="table-td3" style="width:auto;">
            <input type="hidden" id="longitude" name="stoLongitude" value="${sto.stoLongitude}"/>
            <input type="hidden" id="latitude" name="stoLatitude" value="${sto.stoLatitude}"/>
            <input type="text" class="text1 abc" readonly="readonly" id="stoAddress" name="stoAddress" placeholder="请点击选择店铺地址" required="required" value="${sto.stoAddress }"
                   onclick="openMap()"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr>
        <td class="table-td1">门牌号：</td>
        <td class="table-td3" style="width:auto;">
            <input type="text" class="text1 abc" id="stoHouseMember" name="stoHouseMember" value="${sto.stoHouseMember }"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr>
        <td class="table-td1">联系人：</td>
        <td class="table-td3" style="width:auto;">
            <input type="text" class="text1" id="stoLinkman" name="stoLinkman" placeholder="请输入联系人" required="required" value="${sto.stoLinkman }"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr>
        <td class="table-td1">联系电话：</td>
        <td class="table-td3" style="width:auto;">
            <input type="text" class="text1 abc" id="stoPhone" name="stoPhone" placeholder="请输入联系电话" required="required" value="${sto.stoPhone }"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr>
        <td class="table-td1">是否推送：</td>
        <td class="table-td3" style="width:auto;">
            <input type="checkbox" class="text2 abc" id="stoIsSms" name="stoIsSms" value="1"
                   <c:if test="${sto.stoIsSms == 1}">checked='checked' </c:if>
                   <c:if test="${empty sto.stoIsSms}">checked='checked'</c:if>
            />接受短信推送商城信息
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr class="tdSMSTel" <c:if test="${sto.stoIsSms == 0}">style="display:none;"</c:if>>
        <td class="table-td1" style="vertical-align: top;">推送手机：</td>
        <td class="table-td3" style="width:auto;">
            <div class="telDiv">
                <c:if test="${empty sto.stoSmsTelephone }">
                    <div class="inpDiv">
                        <input type="text" class="text1 abc2 stoSmsTelephone" id="stoSmsTelephone" placeholder="请输入推送手机" value="${sto.stoSmsTelephone }"/>
                        <i class="addTel btn">新增</i>
                    </div>
                </c:if>
                <c:if test="${!empty sto.stoSmsTelephone }">
                    <c:forEach var="tel" items="${fn:split(sto.stoSmsTelephone,',') }" varStatus="i">
                        <div class="inpDiv">
                            <input type="text" class="text1 abc2 stoSmsTelephone" id="stoSmsTelephone" placeholder="请输入推送手机" value="${tel }"/>
                            <c:if test="${i.index == 0 }"><i class="addTel btn">新增</i></c:if>
                            <c:if test="${i.index > 0 }"><i class="delTel btn" onclick="deleteTel(this);">删除</i></c:if>
                        </div>
                    </c:forEach>
                </c:if>
            </div>

        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <tr class="tdSMSTel" style="<c:if test='${sto.stoIsSms == 0}'>display:none;</c:if>line-height: 0px;">
        <td class="table-td1"></td>
        <td class="table-td3 tColor" style="width:auto;color:red;">请填写正确的手机号码，此手机号码接受商城信息的推送；必须勾选是否推送，方可有效</td>
        <td></td>
    </tr>
    <tr>
        <td class="table-td1">QQ客服：</td>
        <td class="table-td3" style="width:auto;">
            <input type="text" class="text1 abc2" id="stoQqCustomer" name="stoQqCustomer" placeholder="请输入QQ客服" value="${sto.stoQqCustomer }"/>
        </td>
        <td><span class="tColor"></span></td>
    </tr>
    <%-- 	<tr>
            <td class="table-td1">是否营业中：</td>
            <td class="table-td3"><input type="checkbox"
                name="stoStatus"
                <c:if test="${sto.stoStatus == 1}"> checked="checked" </c:if> />

                <span id="picCodePrompt" class="tColor"
                style="margin-left: 20px; color: orange">关闭后，店铺将不再接收任何订单。</span>
            </td>
            <td></td>
        </tr> --%>
</table>
<input type="hidden" id="city_" value="${sto.stoCity }"/>
<input type="hidden" id="area_" value="${sto.stoArea }"/>


<div class="btn_box_div" style="margin-top: 30px;">
    <div class="btn_save_div">
        <input type="button" id="save" value="保存" class="blue-btn" onclick="save()"/>
    </div>
    <div class="btn_cancel_div" style="background-color: #8cc717;border-radius: 3px;font-size: 14px;">
        <a href="/store/index.do" style="cursor: pointer;color:white;">返回</a>
    </div>
</div>
</body>
</html>
