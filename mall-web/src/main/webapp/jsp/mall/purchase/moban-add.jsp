<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/laydate/skins/lan/laydate.css"/>

    <title>${title}</title>
</head>
<body>
<div class="warp">
    <div class="gt-bread-crumb">
        <span class="gt-bread-crumb-title">${title}</span>
    </div>
    <form class="add-form" id="companyForm">
        <input type="hidden" name="id" value="${company.id}">
        <div class="gt-form-row">
            <div class="gt-form-left hetong-add-list">公司名称：</div>
            <div class="gt-form-right">
                <input type="text" name="companyName" maxlength="20" class="gt-form-input middle add-form-input" placeholder="请输入公司名称" value="${company.companyName}">
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left hetong-add-list">公司电话：</div>
            <div class="gt-form-right">
                <input type="text" name="companyTel" class="gt-form-input middle add-form-input" placeholder="请输入公司电话" value="${company.companyTel}">
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left hetong-add-list">公司官网：</div>
            <div class="gt-form-right">
                <input type="text" name="companyInternet" class="gt-form-input middle add-form-input" placeholder="请输入官网" value="${company.companyInternet}">
            </div>
        </div>
        <div class="gt-form-row">
            <div class="gt-form-left hetong-add-list">公司地址：</div>
            <div class="gt-form-right ver-top">
                <input type="hidden" id="longitude" name="longitude" value="${company.longitude }"/>
                <input type="hidden" id="latitude" name="latitude" value="${company.latitude }"/>
                <textarea class="add-form-input" id="address" name="companyAddress" placeholder="请输入公司详细地址">${company.companyAddress}</textarea>
                <img id="selectAddress" title="点击选择地址" style="cursor: pointer;" alt="" src="/images/address.png" width="20" height="20">
            </div>

        </div>

    </form>
    <div class="gt-margin-left50">
        <input type="button" class="gt-btn blue middle mar-r20" value="保存" onclick="save()"/>
        <input type="button" class="gt-btn default middle" name="" id="" value="返回" onclick="javascript:history.go(-1)"/>
    </div>
</div>
<script charset="utf-8" type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
<script src="/js/plugin/layer/layer.js"></script>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<script type="text/javascript">
    function save() {
        var companyName = $("input[name='companyName']").val();
        var companyTel = $("input[name='companyTel']").val();
        var companyInternet = $("input[name='companyInternet']").val();
        var companyAddress = $("#address").val();
        if ($.trim(companyName) == "") {
            parentAlertMsg("公司名称不可以为空!")
            return;
        }
        if ($.trim(companyTel) == "") {
            parentAlertMsg("公司电话不可以为空!")
            return;
        }
        if (!checkTel($.trim(companyTel)) && !checkPhone($.trim(companyTel))) {
            parentAlertMsg("公司联系电话有误!")
            return;
        }
        if ($.trim(companyInternet) == "") {
            parentAlertMsg("公司官网不可以为空!")
            return;
        }
        if (!checkUrl($.trim(companyInternet))) {
            parentAlertMsg("公司官网链接有误!")
            return;
        }
        if ($.trim(companyAddress) == "") {
            parentAlertMsg("公司地址不可以为空!")
            return;
        }
        $.ajax({
            url: "/purchaseCompany/saveCompany.do",
            data: $('#companyForm').serialize(),
            type: "POST",
            dataType: "JSON",
            success: function (data) {
                if (data.result == true || data.result == "true") {
                    parentAlertMsg("公司信息保存成功!")
                    location.href = "/purchaseCompany/companyIndex.do";
                } else {
                    parentAlertMsg("公司信息保存失败!")
                }
            }
        });

    }
    function checkUrl(urlString) {
        var reg = /(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&:/~\+#]*[\w\-\@?^=%&/~\+#])?/;
        if (urlString == "" || !reg.test(urlString)) {
            return false;
        }
        return true;
    }

    function checkTel(tel) {
        if (!/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(tel)) {
            return false;
        }
        return true;
    }

    function checkPhone(phone) {
        if (!(/^1[34578]\d{9}$/.test(phone))) {
            return false;
        }
        return true;
    }

    $(function () {
        $("#selectAddress").click(function () {
            var latitude = $("#latitude").val();//纬度
            var longitude = $("#longitude").val();//经度
            var address = "";
            var url = "https://apis.map.qq.com/tools/locpicker?search=1&type=1&key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU&referer=test";
            address = "&coord=" + latitude + "," + longitude;
            url += "&coordtype=5";
            if (latitude != "" && longitude != "") {
                url += address;
            }
            window.layer.open({
                area: ['800px', '600px'],
                title: [
                    '消息',
                    'background-color:#5FBFE7; color:#fff;'
                ],
                shade:[0.1,"#fff"],
                offset: "5%",
                type: 2,
                btn: ["确定", "取消"],
                content: [url, "no"], //这里content是一个URL，如果你不想让iframe出现滚动条，你还可以content: ['http://sentsin.com', 'no']
                yes: function (index) {
                    window.layer.close(index);
                }
            });
        });
    });

    function setAddress(loc) {
        $("#address").val(loc.poiname);
        $("#longitude").val(loc.latlng.lng);
        $("#latitude").val(loc.latlng.lat);
    }

</script>
</body>
</html>
