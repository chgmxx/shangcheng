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
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/laydate/skins/lan/laydate.css"/>
    <style>
        .websit_url {
            max-width: 300px !important;
            word-break: break-all;
        }
    </style>
    <title>模板管理</title>
</head>
<body>
<div class="warp">
    <form id="companyForm" action="/purchaseCompany/companyIndex.do" method="POST">
        <div class="gt-three-level-menu">
            <a href="/purchaseOrder/orderIndex.do" class="gt-t-menu-box " hidefocus="true"> <span class="gt-t-menu-title">报价单管理</span> </a>
            <a href="/purchaseContract/contractIndex.do" class="gt-t-menu-box" hidefocus="true"> <span class="gt-t-menu-title">合同管理</span> </a>
            <a href="/purchaseCompany/companyIndex.do" class="gt-t-menu-box linked" hidefocus="true"> <span class="gt-t-menu-title">公司模版管理</span> </a>
        </div>
        <div class="table-operate flex">
            <div class="flex-1">
                <div class="gt-table-search-box">
                    <a href="/purchaseCompany/companyForm.do" class="gt-btn blue middle">新增</a>
                </div>
            </div>
            <div class="flex-1 text-right">
                <label for=""></label>
                <input type="hidden" id="curPage" name="curPage">
                <select class="gt-form-select small" name="searchType">
                    <option value="0" ${parms.searchType==0?"selected='selected'":""}>公司名称</option>
                    <option value="1" ${parms.searchType==1?"selected='selected'":""}>公司电话</option>
                    <option value="2" ${parms.searchType==2?"selected='selected'":""}>公司官网</option>
                </select>
                <div class="gt-form-search middle text-left">
                    <input type="text" class="search-input" name="search" value="${parms.search}"/>
                    <i class="iconfont search-btn" onclick="submitForm()">&#xe72c;</i>
                </div>
            </div>
        </div>
    </form>
    <table class="gt-table">
        <thead class="gt-table-thead">
        <tr>
            <th>编号</th>
            <th>公司名称</th>
            <th>公司电话</th>
            <th>公司地址</th>
            <th class="websit_url">公司官网</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody class="gt-table-tbody">
        <c:forEach items="${page.subList}" var="company">
            <tr>
                <td>${company.id}</td>
                <td>${company.company_name}</td>
                <td>${company.company_tel}</td>
                <td>${company.company_address}</td>
                <td class="websit_url">${company.company_internet}</td>
                <td>
                    <i class="iconfont" title="修改" onclick="location.href='/purchaseCompany/companyForm.do?companyId=${company.id}'">&#xe71c;</i>
                    <i class="iconfont" title="删除 " onclick="removeCompany('${company.id}')">&#xe649;</i>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <jsp:include page="page.jsp"></jsp:include>
</div>
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<script type="text/javascript">
    function submitForm() {
        $("#companyForm").submit();
    }

    function removeCompany(companyId) {
        layer.confirm("确定要删除该条数据吗?", {
            shade:[0.1,'#fff'],
            btn: ['确定', '取消'],
            offset: '40%'
        }, function () {
            $.ajax({
                url: "/purchaseCompany/deleteCompany.do",
                data: {
                    "companyId": companyId
                },
                type: "POST",
                dataType: "JSON",
                success: function (data) {
                    if (data.result == true || data.result == "true") {
                        window.alertMsg("删除成功!");
                        location.href = "/purchaseCompany/companyIndex.do";
                    } else {
                        window.alertMsg("删除失败!");
                    }
                }
            });
            layer.closeAll();
        }, function () {
        });
    }
</script>
</body>
</html>
