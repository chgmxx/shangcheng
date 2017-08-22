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

    <title>编辑上门自提</title>

    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/take.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/DateTime/css/lq.datetimepick.css"/>
    <script charset="utf-8" type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script src="/js/plugin/DateTime/js/lq.datetimepick.js"></script>
    <script src="/js/plugin/DateTime/js/selectUi.js"></script>
    <script src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        var imagDefaultObj = new Object();
        var daysObjs = new Object();
        var daysDefaultObj = new Object();
    </script>
</head>

<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="con_div">
    <div class="con-head">
        <a class="" href="/mFreight/index.do">物流管理</a>
        <a class="navColor" href="/mFreight/takeindex.do">上门自提</a>
    </div>
    <table class="table2" id="tab">
        <tr>
            <td class="table-td1"><em>*</em> 自提点名称：</td>
            <td class="table-td3" style="width:auto;">
                <input type="hidden" value="${take.id }" name="id" id="id"/>
                <input type="text" class="text1 abc" id="visitName" name="visitName" maxlength="50"
                       placeholder="请输入自提点名称" required="required" value="${take.visitName }"/>
            </td>
            <td><span id="picCodePrompt" class="tColor" msg="自提点名称"></span></td>
        </tr>
        <tr>
            <td colspan="1" class="table-td1" style="width: 50px;"><em>*</em> 自提点地址：
            </td>
            <td class="table-td3">
                <input type="hidden" id="province" value="${take.visitProvinceId }"/>
                <input type="hidden" id="city" value="${take.visitCityId }"/>
                <input type="hidden" id="area" value="${take.visitAreaId }"/>
                <select class="abc" style="width: 98px;height: 25px;" name="visitProvinceId" id="visitProvinceId"
                        onchange="areaChange(this.value,1,2)">
                    <option value="0">请选择省份</option>
                    <c:forEach items="${areaLs }" var="area">
                        <option ${area.id eq take.visitProvinceId?'selected':'' } value="${area.id }">${area.city_name }</option>
                    </c:forEach>
                </select>
                <select class="abc" style="width: 97px;height: 25px;" name="visitCityId" id="visitCityId"
                        onchange="areaChange(this.value,2,2)">
                    <option value="0">请选择市</option>
                </select>
                <select class="abc" style="width: 97px;height: 25px;" name="visitAreaId" id="visitAreaId"
                        onchange="areaChange(this.value,3,2)">
                    <option value="0">请选择区</option>
                </select>
            </td>
            <td><span id="picCodePrompt_city" class="tColor"></span></td>
        </tr>

        <tr>
            <td class="table-td1"></td>
            <td class="table-td3" style="width:auto;">
                <input type="hidden" id="longitude" name="visitLongitude" value="${take.visitLongitude}"/>
                <input type="hidden" id="latitude" name="visitLatitude" value="${take.visitLatitude}"/>
                <input type="text" class="text1 abc" id="visitAddress" name="visitAddress" placeholder="请点击自提地址" readonly="readonly"
                       required="required" value="${take.visitAddress }" onclick="openMap()"/>
            </td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>
        <tr>
            <td class="table-td1"><em>*</em> 联系电话：</td>
            <td class="table-td3" style="width:auto;">
                <input type="text" class="text1 abc vali"
                       id="phone" name="visitContactNumber" placeholder="请输入联系电话" maxlength="30"
                       datatype="^\d+\-?\d*$" errormsg="请输入正确的联系电话" isnotnull="1"
                       required="required" value="${take.visitContactNumber }"/>
            </td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>

        <tr>
            <td class="table-td1 vtop"><em>*</em> 接待时间：</td>
            <td class="table-td3 timeTd" style="width:auto;">
                <c:set var="isTime" value="0"></c:set>
                <c:if test="${!empty take }">
                    <c:forEach var="time" items="${take.timeList }">
                        <c:set var="isTime" value="1"></c:set>
                        <div class="timeDiv" id="${time.id }">
                            <input type="text" class="text1 abc right visitStartTime" style="width:60px;" readonly="readonly"
                                   id="visitStartTime" value="${time.startTime }" disabled="disabled"/>
                            <input type="text" class="text1 abc right visitEndTime" style="width:60px;" readonly="readonly"
                                   id="visitEndTime" value="${time.endTime }" disabled="disabled"/>
                            <ul class="js-weeks-selector weeks-selector ">
                                <li class="js-week-item" did="1">周一</li>
                                <li class="js-week-item" did="2">周二</li>
                                <li class="js-week-item" did="3">周三</li>
                                <li class="js-week-item" did="4">周四</li>
                                <li class="js-week-item" did="5">周五</li>
                                <li class="js-week-item" did="6">周六</li>
                                <li class="js-week-item" did="7">周日</li>
                            </ul>
                            <span class="times_span"></span>
                            <span class="ok_span" style="display:none;">
					        	<a href="javascript:void(0)" class="ok blue">确认</a>
					        	<a href="javascript:void(0)" class="cancel blue">取消</a>
					        </span>
                            <span class="edit_span">
					        	<a href="javascript:void(0)" class="edit blue">编辑</a>
					        	<a href="javascript:void(0)" class="dele blue">删除</a>
					        </span>
                            <input type="hidden" class="days" value="${time.visitDays }"/>
                            <input type="hidden" class="timeId" value="${time.id }"/>
                        </div>
                        <script type="text/javascript">
                            daysDefaultObj["${time.id}"] = "${time.id}";
                        </script>
                    </c:forEach>
                </c:if>
                <c:if test="${isTime == 0}">
                    <div class="timeDiv" id="">
                        <input type="text" class="text1 abc right visitStartTime" style="width:60px;" readonly="readonly"
                               id="visitStartTime" value="9:00"/>
                        <input type="text" class="text1 abc right visitEndTime" style="width:60px;" readonly="readonly"
                               id="visitEndTime" value="22:00"/>
                        <ul class="js-weeks-selector weeks-selector ">
                            <li class="js-week-item" did="1">周一</li>
                            <li class="js-week-item" did="2">周二</li>
                            <li class="js-week-item" did="3">周三</li>
                            <li class="js-week-item" did="4">周四</li>
                            <li class="js-week-item" did="5">周五</li>
                            <li class="js-week-item" did="6">周六</li>
                            <li class="js-week-item" did="7">周日</li>
                        </ul>
                        <span class="times_span"></span>
                        <span class="ok_span">
				        	<a href="javascript:void(0)" class="ok blue">确认</a>
				        	<a href="javascript:void(0)" class="cancel blue">取消</a>
				        </span>
                        <span class="edit_span" style="display:none;">
				        	<a href="javascript:void(0)" class="edit blue">编辑</a>
				        	<a href="javascript:void(0)" class="dele blue">删除</a>
				        </span>
                        <input type="hidden" class="days" value=""/>
                        <input type="hidden" class="timeId" value=""/>
                    </div>
                </c:if>
                <a href="javascript:void(0)" class="add-times blue" style="<c:if test='${isTime == 0}'>display:none;</c:if>">新增时间段</a>
            </td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>
        <tr>
            <td class="table-td1"><em>*</em> 自提点图片：</td>
            <td class="table-td3 " style="width:auto;">
                <img onclick="choosePicture()" class="abc imgURL" src="/images/add_Image.png" width="50" style="cursor: pointer;"/>
                <ul class="ztImageUrl picture-list app-image-list clearfix ui-sortable">
                    <c:if test="${take.imageList != null }">
                        <c:forEach var="images" items="${take.imageList }">
                            <li class="sort delParent">
                                <img src="${http}${images.imageUrl }" aId="${images.id }" class="img1" width="50"/>
                                <a class="js-delete-picture close-modal small hide" onclick="delImg(this)">×</a>
                            </li>
                            <script type="text/javascript">
                                imagDefaultObj["${images.id }"] = "${images.id}";
                            </script>
                        </c:forEach>
                    </c:if>
                </ul>
                <input type="hidden" value="${take.imagesUrl }" name="imagesUrl" id="imagesUrl"/>
                <input type="hidden" value="${http }" class="imgHttp"/>
            </td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>
        <tr>
            <td class="table-td1" style="vertical-align: top;padding:10px 0px;">自提点备注：</td>
            <td class="table-td3" style="width:auto;">
				<textarea rows="10" cols="5" name="visitRemark" id="visitRemark" class="vali" maxlength="200"
                          style="width:300px;" placeholder="请输入自提点备注" errormsg="请输入自提点备注">${take.visitRemark }</textarea>
            </td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>
        <tr>
            <td class="table-td1">是否同时作为线下门店接待：</td>
            <td class="table-td3" style="width:auto;">
                <input type="checkbox" name="isStoreReception" id="isStoreReception" style="vertical-align: middle;"
                       <c:if test="${take.isStoreReception==1}">checked="checked"</c:if>/>
                同时作为线下门店接待
            </td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>
        <tr>
            <td class="table-td1">是否允许到店支付：</td>
            <td class="table-td3" style="width:auto;">
                <input type="checkbox" name="isStorePay" id="isStorePay" style="vertical-align: middle;"
                       <c:if test="${take.isStorePay==1}">checked="checked"</c:if>/>
                允许到店支付(如不勾选到店支付就只能微信支付，勾选到店支付既能到店支付也能微信支付)
            </td>
            <td><span id="picCodePrompt" class="tColor"></span></td>
        </tr>
    </table>


    <div class="btn_box_div" style="margin-top: 30px;">
        <div class="btn_save_div">
            <input type="button" id="save" value="保存" class="blue-btn" onclick="save()"/>
        </div>
        <div class="btn_cancel_div" style="background-color: #8cc717;border-radius: 3px;font-size: 14px;">
            <a href="/mFreight/takeindex.do" style="cursor: pointer;color:white;">返回</a>
        </div>
    </div>


    <div class="timeDivHide" id="" style="display: none;">
        <input type="text" class="text1 abc right visitStartTime" style="width:60px;" readonly="readonly"
               id="visitStartTime" value=""/>
        <input type="text" class="text1 abc right visitEndTime" style="width:60px;" readonly="readonly"
               id="visitEndTime" value=""/>
        <ul class="js-weeks-selector weeks-selector ">
            <li class="js-week-item" did="1">周一</li>
            <li class="js-week-item" did="2">周二</li>
            <li class="js-week-item" did="3">周三</li>
            <li class="js-week-item" did="4">周四</li>
            <li class="js-week-item" did="5">周五</li>
            <li class="js-week-item" did="6">周六</li>
            <li class="js-week-item" did="7">周日</li>
        </ul>
        <span class="times_span"></span>
        <span class="ok_span">
        	<a href="javascript:void(0)" class="ok blue">确认</a>
        	<a href="javascript:void(0)" class="cancel blue">取消</a>
        </span>
        <span class="edit_span" style="display:none;">
        	<a href="javascript:void(0)" class="edit blue">编辑</a>
        	<a href="javascript:void(0)" class="dele blue">删除</a>
        </span>
        <input type="hidden" class="days" value=""/>
        <input type="hidden" class="timeId" value=""/>
    </div>
</div>
<script type="text/javascript" src="/js/mall/take/take_edit.js"></script>
</body>
</html>
