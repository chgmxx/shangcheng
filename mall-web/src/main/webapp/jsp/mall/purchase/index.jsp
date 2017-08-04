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
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/public/public.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/index.css"/>
    <link rel="stylesheet" type="text/css" href="/js/plugin/laydate/skins/lan/laydate.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <script src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script src="/js/plugin/laydate/laydate.js" type="text/javascript" charset="utf-8"></script>
    <title>报价管理</title>
</head>
<body>
<div class="warp">
    <div class="gt-three-level-menu">
        <a href="/purchaseOrder/orderIndex.do" class="gt-t-menu-box linked"
           hidefocus="true"> <span class="gt-t-menu-title">报价单管理</span>
        </a> <a href="/purchaseContract/contractIndex.do" class="gt-t-menu-box"
                hidefocus="true"> <span class="gt-t-menu-title">合同管理</span>
    </a> <a href="/purchaseCompany/companyIndex.do" class="gt-t-menu-box"
            hidefocus="true"> <span class="gt-t-menu-title">公司模版管理</span>
    </a>
    </div>

    <div class="table-operate">
        <form action="/purchaseOrder/orderIndex.do" method="post" id="orderForm" style="padding-right: 45px">
            <div class="mar-tb5 text-right">
                <label for="" class="font14">筛选：</label> <select
                    class="gt-form-select operate-input" name="status" onchange="submitForm()">
                <option value="10" ${parms.status==10?"selected='selected'":""}  >全部</option>
                <option value="1" ${parms.status==1?"selected='selected'":""}>待发布</option>
                <option value="2" ${parms.status==2?"selected='selected'":""}>待付款</option>
                <option value="3" ${parms.status==3?"selected='selected'":""}>已付款</option>
                <option value="4" ${parms.status==4?"selected='selected'":""}>已完成</option>
                <option value="0" ${parms.status==0?"selected='selected'":""}>已关闭</option>
            </select>
                <div class="gt-form-search middle operate-input text-left">
                    <input type="text" class="search-input operate-input"
                           name="search" placeholder="请输入报价单号 " value="${parms.search}"/>
                    <i class="iconfont search-btn" onclick="submitForm()">&#xe72c;</i> <input type="hidden"
                                                                                              name="curPage" id="curPage">
                </div>
            </div>
            <div class="flex gt-table-search-box">
                <div>
                    <a href="/purchaseOrder/orderAdd.do" class="gt-btn blue middle">新增</a>
                </div>
                <div class="flex-1 operate-date">
                    <label for="" class="font14">创建时间：</label>
                    <input type="text" name="startTime" value="${parms.startTime}" class="start laydate-icon operate-input" id="start" placeholder=""/>
                    <input type="text" name="endTime" value="${parms.endTime}" class="end laydate-icon operate-input" id="end" value="" placeholder=""/>
                </div>
            </div>
        </form>
        <table class="gt-table">
            <thead class="gt-table-thead">
            <tr>
                <th>报价单号</th>
                <th>报价单标题</th>
                <th>付款类型</th>
                <th>运费(￥)</th>
                <th>报价单金额(￥)</th>
                <th>报价单状态</th>
                <th>创建时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody class="gt-table-tbody">
            <c:forEach var="order" items="${page.subList }">
                <tr>
                    <td>${order.order_number}</td>
                    <td>${order.order_title}</td>
                    <td>${order.order_type==0?"全款":"分期"}</td>
                    <td><fmt:formatNumber type="number" value="${order.freight}" pattern="0.00" maxFractionDigits="2"/></td>
                    <td><fmt:formatNumber type="number" value="${order.all_money}" pattern="0.00" maxFractionDigits="2"/></td>
                    <td>
                        <c:if test="${order.order_status==0}">已关闭</c:if>
                        <c:if test="${order.order_status==1}">待发布</c:if>
                        <c:if test="${order.order_status==2}">待付款</c:if>
                        <c:if test="${order.order_status==3}">已付款</c:if>
                        <c:if test="${order.order_status==4}">已完成</c:if>
                    </td>
                    <td><fmt:formatDate value="${order.create_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <c:if test="${order.order_status==1}">
                            <i class="iconfont" title="修改" onclick="location.href='/purchaseOrder/orderAdd.do?orderId=${order.id}'">&#xe71c;</i>
                        </c:if>
                        <i class="iconfont" title="查看详情" onclick="location.href='/purchaseOrder/orderIndexDetail.do?orderId=${order.id}'">&#xe600;</i>
                        <c:if test="${!empty order.member_id}">
                            <i class="iconfont" title="收款信息" onclick="location.href='/purchaseOrder/receivablesDetails.do?orderId=${order.id}&memberId=${order.member_id}'">&#xe60d;
                            </i>
                        </c:if>
                        <br/>
                        <i class="iconfont" title="留言管理" onclick="location.href='/purchaseOrder/languageList.do?orderId=${order.id}'">&#xe60c;</i>
                        <i class="iconfont" title="二维码" onclick="location.href='/purchaseOrder/downQrcode.do?orderId=${order.id}'">&#xe659;</i>
                        <i class="iconfont" title="查看统计" onclick="location.href='/purchaseStatistics/statisticsIndex.do?orderId=${order.id}'">&#xe7d2;</i></td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <jsp:include page="page.jsp"></jsp:include>
    </div>
</div>
<script type="text/javascript">
    var start = {
        elem: '.start',
        format: 'YYYY/MM/DD hh:mm:ss',
        min: '1970-01-01 00:00:00', //设定最小日期为当前日期
        max: '2099-06-16 23:59:59', //最大日期
        istime: true,
        istoday: false,
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '.end',
        format: 'YYYY/MM/DD hh:mm:ss',
        min: '1970-01-01 00:00:00',
        max: '2099-06-16 23:59:59',
        istime: true,
        istoday: false,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };

    $(function () {
        //日期初始化
        laydate(start);
        laydate(end);

        $(document).keyup(function (event) {
            if (event.keyCode == 13) {
                $("#orderForm").submit();
            }
        });
    });

    function submitForm() {
        $("#orderForm").submit();
    }
</script>
</body>
</html>
