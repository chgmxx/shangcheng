<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html lang="en">
<head>
    <title>预售管理-编辑预售</title>
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
    <link rel="stylesheet" type="text/css" href="/css/mall/group.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/manageList.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/jquery-ui.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
    <script type="text/javascript">
        if (top == self) {
            window.location.href = "/mPresale/start.do";
        }
    </script>
    <style type="text/css">
        .inpt4 {
            width: 100px;
            height: 25px;
            line-height: 25px;
            outline: none;
        }

        .groupDiv {
            font-size: 14px;
            display: flex;
            width: 801px;
        }

        .table {
            border: 1px solid #ccc;
        }

        .table td {
            border-width: 0px;
            font-size: 14px;
            padding: 0px;
            height: 36px;
            line-height: 36px;
        }

        .table thead {
            text-align: left;
            background-color: #e6e6e6;
        }

        .table tr {
            border-bottom: 1px solid #ccc;
        }

        .table td a {
            color: #000;
        }

        td:first-child {
            padding-left: 5px;
        }

        .table .inpt3 {
            width: 100px;
        }
    </style>
    <script type="text/javascript">
        var setDefaultObj = new Object();
    </script>
</head>
<body>
<div id="newGroup">
    <c:if test="${!empty shoplist }">
        <div class="con-head">
            <a class="" href="/mPresale/index.do">预售管理</a>
            <a class="" href="/mPresale/deposit.do">定金管理</a>
            <a class="navColor" href="/mPresale/presale_set.do">预售送礼设置</a>
        </div>
        <form id="groupForm">
            <div class="groupDiv">
                <span class="font14"><em>&nbsp;&nbsp;</em>订购送礼：</span>
                <table class="table" style="width:700px;margin:0px;">
                    <thead>
                    <tr>
                        <td style="width: 10%">送礼名次</td>
                        <td style="width: 10%">礼品类型</td>
                        <td style="width: 10%">礼品名称</td>
                        <td style="width: 10%">礼品数量</td>
                        <td style="width: 5%">操作</td>
                    </tr>
                    </thead>
                    <tbody id="J_Tbody">
                    <c:if test="${!empty giveList }">
                        <c:forEach var="give" items="${giveList }" varStatus="j">
                            <tr>
                                <td>
                                    前<input type="text" class="inpt4 vali rank" id="rank" notNull="1"
                                            value="${give.giveRanking }" placeholder="1-9999的正整数" maxlength="4" datatype="^[0-9]{1,4}?$">名
                                </td>
                                <td>
                                    <c:set var="types" value=""></c:set>
                                    <c:set var="typeName" value=""></c:set>
                                    <input type="hidden" class="tId" value="${give.id }"/>
                                    <select class="types inpt3" style="width: 80px;" onchange="typeChange(this);">
                                        <c:if test="${!empty dictList }">
                                            <c:forEach var="dict" items="${dictList }" varStatus="i">
                                                <c:if test="${dict.item_key == give.giveType || i.index == 0}">
                                                    <c:set var="types" value="${dict.item_key }"></c:set>
                                                    <c:set var="typeName" value="${dict.item_value }"></c:set>
                                                </c:if>
                                                <option value="${dict.item_key }"
                                                        <c:if test="${dict.item_key == types }">selected="selected"</c:if> >${dict.item_value }</option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </td>
                                <td>
                                    <c:set var="giveName" value=""></c:set>
                                    <c:if test="${!empty give.giveName }">
                                        <c:set var="giveName" value="${give.giveName }"></c:set>
                                    </c:if>
                                    <c:if test="${empty give.giveName && typeName != ''}">
                                        <c:set var="giveName" value="${typeName }"></c:set>
                                    </c:if>
                                    <input type="text" class="inpt3 vali time_inp name" id="name" notNull="1"
                                           value="${giveName }" placeholder="礼品名称" maxlength="100">
                                </td>
                                <td>
                                    <input type="text" class="inpt4 vali tNum" id="tNum" notNull="1"
                                           value="${give.giveNum }" placeholder="1-9999的数量" maxlength="4" datatype="^[0-9]{1,4}?$">
                                </td>
                                <td>
                                    <a href="javascript:void(0);" class="add" onclick="addTr(this);"
                                       <c:if test="${j.index > 0 }">style="display: none;"</c:if> >新增</a>
                                    <a href="javascript:void(0);" class="del" onclick="delTr(this);"
                                       <c:if test="${j.index == 0 }">style="display: none;"</c:if> >删除</a>
                                </td>
                            </tr>
                            <script type="text/javascript">
                                setDefaultObj["${give.id}"] = "${give.id}";
                            </script>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>

            </div>
            <div class="groupDiv">
                <span class="font14"></span>
                <span style="color: red;">*实体物品要在发货的时候一起寄给买家</span>
            </div>
            <div class="groupDiv">
                <a href="javascript:editPresale();" class="addBtn">确定</a>
            </div>
        </form>
    </c:if>
    <c:if test="${empty shoplist }">
        <h1 class="groupH1"><strong>您还没有店铺，请先去店铺管理创建店铺</strong></h1>
    </c:if>
</div>
<div class="trObj_div" style="display: none;">
    <table>
        <tbody>
        <tr>
            <td>
                前<input type="text" class="inpt4 vali rank" id="rank" notNull="1"
                        value="" placeholder="1-9999的正整数" maxlength="4" datatype="^[0-9]{1,4}?$">名
            </td>
            <td>
                <c:set var="typeNames" value=""></c:set>
                <c:set var="types" value=""></c:set>
                <input type="hidden" class="tId" value=""/>
                <select class="types inpt3" style="width: 80px;" onchange="typeChange(this);">
                    <c:if test="${!empty dictList }">
                        <c:forEach var="dict" items="${dictList }" varStatus="i">
                            <c:if test="${i.index == 0}">
                                <c:set var="types" value="${dict.item_key }"></c:set>
                                <c:set var="typeNames" value="${dict.item_value }"></c:set>
                            </c:if>
                            <option value="${dict.item_key }"
                                    <c:if test="${dict.item_key == types }">selected="selected"</c:if> >${dict.item_value }</option>
                        </c:forEach>
                    </c:if>
                </select>
            </td>
            <td>
                <input type="text" class="inpt3 vali time_inp name" id="name" notNull="1"
                       value="${typeNames }" placeholder="礼品名称" maxlength="100">
            </td>
            <td>
                <input type="text" class="inpt4 vali tNum" id="tNum" notNull="1"
                       value="" placeholder="1-9999的数量" maxlength="4" datatype="^[0-9]{1,4}?$">
            </td>
            <td>
                <a href="javascript:void(0);" class="add" onclick="addTr(this);" style="display: none;">新增</a>
                <a href="javascript:void(0);" class="del" onclick="delTr(this);">删除</a>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    var shopId = $(".shopId").attr("id");
    $(".shopId").find("option[value=" + shopId + "]").attr("selected", true);
</script>
<script type="text/javascript" src="/js/mall/presale/presale_set.js"></script>
</body>
</html>