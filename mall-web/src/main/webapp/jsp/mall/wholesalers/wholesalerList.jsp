<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>批发商</title>
    <meta id="meta" name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/wholesalers/pcReset.css?<%=System.currentTimeMillis()%>"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/wholesalers/pcWholesale.css?<%=System.currentTimeMillis()%>"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
</head>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="contentWarp">
    <div class="con-head">
        <a href="/mallWholesalers/index.do">批发管理</a>
        <a class="navColor" href="/mallWholesalers/wholesaleList.do">批发商管理</a>
        <a href="/mallWholesalers/toSetWholesale.do">批发设置</a>
    </div>

    <div class="title">
        <span class="i-con"></span>
        <p class="title-p">批发商列表</p>
    </div>
    <c:if test="${!empty page && !empty page.subList}">
        <input type="button" name="mallAdd" id="mallAdd" class="btns"
               value="同步成交数/金额" onclick="syncOrder();"/>
    </c:if>

    <!-- <div class="con-box">
        <select name="mallChange" class="change">
            <option value="">请选择</option>
            <option value="">全部商品</option>
        </select>
        <div class="srh">
            <div class="sbt" id="go"></div>
            <input type="text" placeholder="请输入识别码或手机号" id="search" class="srh" name="keyword">
        </div>
    </div> -->

    <table border="0" cellspacing="0" cellpadding="0" width="100%" class="saleTab">
        <tr>
            <th width="40" class="center">&nbsp;</th>
            <th width="90">批发商ID</th>
            <th width="92">昵称</th>
            <th width="105">累计成交笔数</th>
            <th width="138">累计成交金额(￥)</th>
            <th width="105">申请时间</th>
            <th width="105">通过时间</th>
            <th width="88">状态</th>
            <th width="70">操作</th>
            <th></th>
        </tr>
        <c:forEach var="wholesaler" items="${page.subList }">
            <tr>
                <td class="center">
                    <c:if test="${wholesaler.status == 0 }">
                        <input type="checkbox" name="check" value="${wholesaler.id }"/>
                    </c:if>
                </td>
                <td>${wholesaler.id }</td>
                <td>${wholesaler.nickname }</td>
                <td>${wholesaler.num }</td>
                <td><c:if test="${empty wholesaler.money }">0</c:if>
                    <c:if test="${!empty wholesaler.money }">${wholesaler.money}</c:if>(元)
                </td>
                <td>
                    <p>${wholesaler.create_time }</p>
                    <p><!-- 00:00:00 --></p>
                </td>
                <td>
                    <c:if test="${empty wholesaler.check_time }">-</c:if>
                    <c:if test="${!empty wholesaler.check_time && wholesaler.status == 1}">${wholesaler.check_time }</c:if>
                </td>
                <td>
                    <c:if test="${wholesaler.status == 0 }">未审核</c:if>
                    <c:if test="${wholesaler.status == -1 }">未通过</c:if>
                    <c:if test="${wholesaler.status == 1 }">
                        <c:set var="toggleCla" value="toggle--off"></c:set>
                        <c:if test="${wholesaler.is_use == 1}">
                            <c:set var="toggleCla" value="toggle--on red"></c:set>
                        </c:if>
                        <a class="toggle ${toggleCla }" onclick="openDisabled(${wholesaler.id},3,this);"><i></i></a>
                    </c:if>
                </td>
                <td class="operate">
                    <c:if test="${wholesaler.status == 0 }">
                        <a onclick="passed(1,${wholesaler.id});" class="operate1"></a>
                        <a onclick="passed(2,${wholesaler.id});" class="operate2"></a>
                    </c:if>
                </td>
                <td class="viewDetail">
                    <a onclick="pifaDetail('${wholesaler.name}','${wholesaler.company_name}','${wholesaler.telephone}','${wholesaler.remark}');">
                        查看详细</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${!empty page && !empty page.subList}">
            <tfoot>
            <td class="center">
                <input class="checkAll" type="checkbox" name="cbox" onclick="showMore(this)">
            </td>
            <td colspan="3">
                <a class="checkAll" href="javascript:;">全选</a>
                <a class="pass" onclick="passFailed(1);">通过</a>
                <a class="unpass" onclick="passFailed(2);">不通过</a>
            </td>
            <td colspan="6">
                <jsp:include page="/jsp/common/page/pageTwo.jsp"></jsp:include>
            </td>
            </tfoot>
        </c:if>
    </table>
</div>
<script type="text/javascript">
    function pifaDetail(name, company, tel, remark) {
        layer.open({
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['350px', '230px'], //宽高
            shade:[0.1,"#fff"],
            offset: '30%',
            content: '<div id="pifaDetail"><div><dl style="margin: 10px;"><dd><dt>姓名：' + name +
            '</dt></dd></dl><dl style="margin: 10px;"><dd><dt>公司名称：' + company + '</dt></dd></dl><dl style="margin: 10px;"><dd>' +
            '<dt>电话号码：' + tel + '</dt></dd></dl><dl style="margin: 10px;"><dd><dt>备注：' + remark +
            '</dt></dd></dl></div></div>'
        });
    }

    /*开关切换*/
    $('.toggle').click(function (e) {
        var toggle = this;
        e.preventDefault();
        $(toggle).toggleClass('toggle--on').toggleClass('toggle--off').addClass('toggle--moving').toggleClass("red");
        setTimeout(function () {
            $(toggle).removeClass('toggle--moving');
        }, 200)
    });
    /*全选*/
    var _array = $("input[name='check']");
    function showMore(obj) {
        if (obj.checked) {
            for (var i = 0; i < _array.length; i++) {
                _array[i].checked = true;
            }
        } else {
            for (var i = 0; i < _array.length; i++) {
                _array[i].checked = false;
            }
        }
    }

    function passFailed(type) {
        var ids = [];
        var _array = $("input[name='check']");
        for (var i = 0; i < _array.length; i++) {
            if (_array[i].checked) {
                ids += _array[i].value + ",";
            }
        }
        if (ids != "") {
            $.ajax({
                url: "/mallWholesalers/updateStatus.do",
                type: "post",
                data: {type: type, ids: ids},
                dataType: "json"
                , success: function (data) {
                    if (data.result == "1") {
                        promMsg("审核通过");
                    } else {
                        promMsg("审核未通过");
                    }
                }
            });
        } else {
            alertMsg("请选择审核通过或未通过的批发商");
        }
    }

    function passed(type, id) {
        if (id != "") {
            $.ajax({
                url: "/mallWholesalers/updateStatus.do",
                type: "post",
                data: {type: type, ids: id},
                dataType: "json",
                success: function (data) {
                    var content = "网络繁忙，请稍后再试";
                    if (data.result == "1") {
                        if (type == 1) {
                            content = "审核通过";
                        } else {
                            content = "审核未通过";
                        }
                    }
                    promMsg(content);
                }
            });
        } else {
            alertMsg("网络繁忙，请稍后再试");
        }
    }

    function openDisabled(id, type, obj) {
        var isUse = -1;
        if (type == 3) {
            if (!$(obj).hasClass("toggle--on")) {
                isUse = 1;
            }
        }
        $.ajax({
            url: "/mallWholesalers/updateStatus.do",
            type: "post",
            data: {type: type, ids: id, isUse: isUse},
            dataType: "json",
            success: function (data) {
                var content = "网络繁忙，请稍后再试。";
                if (data.result == "1") {
                    if (type == 3) {
                        if (isUse == 1) {
                            content = "已启用";
                        } else {
                            content = "已禁用";
                        }
                    } else if (type == 1) {
                        content = "审核通过";
                    } else {
                        content = "审核未通过";
                    }
                }
                /* else{
                 content = "";
                 } */
                promMsg(content);
            }
        });
    }
    //同步订单成交数
    function syncOrder() {
        var layerLoad = layer.load(1, {
            shade: [0.1, '#fff'],
            offset: "30%"
        });
        $.ajax({
            url: "/mallOrder/syncOrderPifa.do",
            type: "post",
            dataType: "json",
            success: function (data) {
                layer.close(layerLoad);
                if (data.flag) {
                    alertMsg("同步成功");
                    location.href = window.location.href;
                } else {
                    alertMsg("同步失败，请稍后重试");
                }
            }, error: function (data) {
                layer.close(layerLoad);
                alertMsg("同步失败，请稍后重试");
            }
        });
    }
    function promMsg(msgContent) {
        layer.alert(msgContent, {
            offset: "30%",
            shade:[0.1,"#fff"],
            end: function () {
                location.reload();//刷新本页面
            }
        });
    }

</script>
</body>
</html>