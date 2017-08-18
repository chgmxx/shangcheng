<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/laydate/skins/lan/laydate.css"/>
    <title>合同管理</title>
</head>
<body>
<div class="warp">
    <div class="gt-three-level-menu">
        <a href="/purchaseOrder/orderIndex.do" class="gt-t-menu-box" hidefocus="true">
            <span class="gt-t-menu-title">报价单管理</span>
        </a>
        <a href="/purchaseContract/contractIndex.do" class="gt-t-menu-box linked" hidefocus="true">
            <span class="gt-t-menu-title">合同管理</span>
        </a>
        <a href="/purchaseCompany/companyIndex.do" class="gt-t-menu-box" hidefocus="true">
            <span class="gt-t-menu-title">公司模版管理</span>
        </a>
    </div>
    <div class="width900">
        <div class="gt-table-search-box flex">
            <div class="flex-1">
                <a href="/purchaseContract/contractForm.do" class="gt-btn blue middle"/>新增</a>
            </div>
            <form action="/purchaseContract/contractIndex.do" method="post" id="contractForm">
                <div class="flex-1 text-right">
                    <label for="" class="font14">合同标题：</label>
                    <div class="gt-form-search middle">
                        <input type="hidden" name="curPage" id="curPage">
                        <input type="text" class="search-input" name="contractTitle" value="${parms.contractTitle}" placeholder="请输入合同标题"/>
                        <i class="iconfont search-btn" onclick="submitForm()">&#xe72c;</i>
                    </div>
                </div>
            </form>
        </div>
        <table class="gt-table">
            <thead class="gt-table-thead">
            <tr>
                <th>编号</th>
                <th>合同标题</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="gt-table-tbody">
            <c:forEach items="${page.subList}" var="contract">
                <tr>
                    <td>${contract.id}</td>
                    <td>${contract.contract_title}</td>
                    <td><fmt:formatDate value="${contract.create_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <i class="iconfont" title="修改" onclick="location.href='/purchaseContract/contractForm.do?contractId=${contract.id}'">&#xe71c;</i>
                        <i class="iconfont" title="删除" onclick="removeContract('${contract.id}')">&#xe649;</i>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <jsp:include page="page.jsp"></jsp:include>
    </div>
</div>
<script src="/js/plugin/jquery-1.8.3.min.js"></script>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<script type="text/javascript">
    function submitForm() {
        $("#contractForm").submit();
    }

    function removeContract(contractId) {
        layer.confirm("确定要删除该条数据吗?", {
            shade:[0.1,'#fff'],
            btn: ['确定', '取消'],
            offset: '40%'
        }, function () {
            $.ajax({
                url: "/purchaseContract/deleteContract.do",
                data: {"contractId": contractId},
                type: "POST",
                dataType: "JSON",
                success: function (data) {
                    if (data.result == true || data.result == "true") {
                        window.alertMsg("删除成功!");
                        location.href = "/purchaseContract/contractIndex.do";
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
