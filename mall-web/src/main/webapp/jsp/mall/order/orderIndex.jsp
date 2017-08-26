<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>订单管理</title>
    <%
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":"
                + request.getServerPort() + path + "/";
    %>

    <base href="<%=basePath%>">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="">
    <link rel="stylesheet" type="text/css" href="/css/common.css"/>
    <link rel="stylesheet" type="text/css" href="/css/common/comm.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/order.css"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/util.js"></script>
    <script type="text/javascript" src="/js/public.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/plugin/laydate/laydate.js"></script>
    <script type="text/javascript" src="/js/mall/mall_public.js"></script>
</head>
<body>
<div class="con_div">
    <c:if test="${empty isNoAdminFlag }">
        <!-- <div class="fansTitle">
        <span class="i-con fl"></span><span class="title-p">订单管理</span>
        </div> -->
        <div class="con-head">
            <a class="navColor" href="/mallOrder/toIndex.do">订单管理</a>
            <a class="" href="/comment/to_index.do">评价管理</a>
        </div>
        <div class="box-btm30">
            <div class="widget-list">
                <div class="js-list-filter-region clearfix">
                    <div class="widget-list-filter">
                        <form action="mallOrder/toIndex.do" method="post" id="orderForm">
                            <input type="hidden" name="status" id="status" value="${status}"/>
                            <input type="hidden" name="orderType" id="orderType" value="${orderType }"/>
                            <input type="hidden" name="orderFilter" id="orderFilter" value="${orderFilter }"/>
                            <input type="hidden" name="curPage" class="curPage" value="1"/>
                            <div class="order-filter">
                                <div class="fl">
                                    <select id="selectType">
                                        <option
                                                <c:if test="${orderType == 0}">selected</c:if> value="0">所有订单
                                        </option>
                                        <option
                                                <c:if test="${orderType == -1}">selected</c:if> value="1">退款订单
                                        </option>
                                        <!-- <option value="2">到店自提订单</option>
                                        <option value="3">货到付款订单</option> -->
                                        <!-- <option value="4">代付款订单</option> -->
                                    </select>
                                </div>
                                <div class="fl">
                                    <label>下单时间：</label>
                                    <input type="text" name="startTime" id="startTime" value="${startTime }" placeholder="请选择日期" class="laydate-icon inputquery"/>
                                    <span> - </span>
                                    <input type="text" name="endTime" id="endTime" value="${endTime }" placeholder="请选择日期" class="laydate-icon inputquery"/>
                                </div>
                                <div class="fl">
                                    <select id="filter" onchange="searchOrderFilter(this);">
                                        <option
                                                <c:if test="${orderFilter == 0}">selected</c:if> value="0">请选择
                                        </option>
                                        <option
                                                <c:if test="${orderFilter == 1}">selected</c:if> value="1">订单号
                                        </option>
                                            <%-- <option <c:if test="${orderFilter == 2}">selected</c:if> value="2">外部订单号</option> --%>
                                        <option
                                                <c:if test="${orderFilter == 3}">selected</c:if> value="3">收货人姓名
                                        </option>
                                        <option
                                                <c:if test="${orderFilter == 4}">selected</c:if> value="4">收货人手机
                                        </option>
                                        <option
                                                <c:if test="${orderFilter == 5}">selected</c:if> value="5">微信昵称
                                        </option>
                                    </select>
                                    <input type="text" name="orderNo" id="orderNo" style="width: 160px;" value="${orderNo }" onkeypress="pressSearch(event)"/>
                                    <i style="background:url(../../../images/search.jpg) no-repeat center center;margin-top:-27px;cursor: pointer;
    						height: 30px;margin-left:227px;position: absolute;width: 24px;z-index: 99;" id="srh"></i>
                                </div>
                                <div class="orderExp fl" onclick="orderExp();">导出订单</div>
                                <c:if test="${!empty videourl }">
                                    <div class="orderExp fl right-15">
                                        <a href='${videourl}' class="btn" target='_blank' style="color: white;">教学视频</a>
                                    </div>
                                </c:if>
                            </div>
                        </form>
                        <div class="ui-nav clearfix">
                            <ul class="pull-left">
                                <c:if test="${orderType == 0}">
                                    <li class="${status == ''?'active':'' || status == null?'active':''}"><a onclick="orderStatus(0);">全部</a></li>
                                    <li class="${status == 1?'active':''}"><a onclick="orderStatus(1);">待付款</a></li>
                                    <li class="${status == 2?'active':''}"><a onclick="orderStatus(2);">待发货</a></li>
                                    <li class="${status == 3?'active':''}"><a onclick="orderStatus(3);">已发货</a></li>
                                    <li class="${status == 4?'active':''}"><a onclick="orderStatus(4);">已完成</a></li>
                                    <li class="${status == 5?'active':''}"><a onclick="orderStatus(5);">已关闭</a></li>
                                    <li class="${status == 6?'active':''}"><a onclick="orderStatus(6);">退款中</a></li>
                                </c:if>
                                <c:if test="${orderType == -1}">
                                    <li class="${status == 7?'active':''}"><a onclick="orderStatus(7);">全部</a></li>
                                    <li class="${status == 8?'active':''}"><a onclick="orderStatus(8);">退款处理中</a></li>
                                    <li class="${status == 9?'active':''}"><a onclick="orderStatus(9);">退款结束</a></li>
                                </c:if>
                            </ul>
                        </div>
                    </div>
                </div>
                <div class="ui-box">
                    <table class="ui-table-order" style="padding: 0px;">
                        <thead class="" style="position:static; top: 0px; margin-top: 0px; left: 324.5px; z-index: 1; width: 849px;">
                        <tr class="widget-list-header">
                            <th class="" colspan="2" style="min-width: 224px; max-width: 224px;">商品</th>
                            <th class="price-cell" style="min-width: 90px; max-width: 90px;">单价</th>
                            <th class="price-cell" style="min-width: 45px; max-width: 45px;">数量</th>
                            <th class="aftermarket-cell" style="min-width: 85px; max-width: 85px;">售后</th>
                            <th class="customer-cell" style="min-width: 110px; max-width: 110px;">买家</th>
                            <th class="time-cell" style="min-width: 80px; max-width: 80px;">下单时间</th>
                            <th class="state-cell" style="min-width: 100px; max-width: 100px;">订单状态</th>
                            <th class="pay-price-cell" style="min-width: 120px; max-width: 120px;">实付金额</th>
                        </tr>
                        </thead>
                        <!-- <thead class="js-list-header-region tableFloatingHeader" style="display: none;">
                            <tr class="widget-list-header">
                                <th class="" colspan="2">商品</th>
                                <th class="price-cell">单价(元)</th>
                                <th class="price-cell">数量</th>
                                <th class="aftermarket-cell">售后</th>
                                <th class="customer-cell">买家</th>
                                <th class="time-cell">下单时间</th>
                                <th class="state-cell">订单状态</th>
                                <th class="pay-price-cell">实付金额(元)</th>
                            </tr>
                        </thead> -->
                        <c:forEach var="order" items="${page.subList }">
                            <tbody class="widget-list-item">
                            <tr class="separation-row">
                                <td colspan="8"></td>
                            </tr>
                            <tr class="header-row">
                                <td colspan="7">
                                    <input type="hidden" class="orderId" value="${order.id}"/>
                                    <input type="hidden" class="payWay" value="${order.orderPayWay}"/>
                                    <div>订单号: ${order.orderNo }
                                        <span style="padding-left: 90px;color: #d1d1d1;">
					        		<c:if test="${order.orderPayWay == 1 || order.orderPayWay == 10}">微信安全支付</c:if>
					        		<c:if test="${order.orderPayWay == 2}">货到付款</c:if>
					        		<c:if test="${order.orderPayWay == 3}">储值卡支付</c:if>
					        		<c:if test="${order.orderPayWay == 4}">积分支付</c:if>
					        		<c:if test="${order.orderPayWay == 5}">扫码支付</c:if>
					        		<c:if test="${order.orderPayWay == 6}">到店支付</c:if>
					        		<c:if test="${order.orderPayWay == 7}">找人代付</c:if>
					        		<c:if test="${order.orderPayWay == 8}">粉币支付</c:if>
					        		<c:if test="${order.orderPayWay == 9}">支付宝支付</c:if>
					        	</span>
                                    </div>
                                    <div class="clearfix"></div>
                                </td>
                                <td colspan="2" class="text-right">
                                    <div class="order-opts-container">
                                        <div class="js-opts">
                                            <a href="mallOrder/orderDetail.do?orderId=${order.id}" class="new-window" style="cursor: pointer;">查看详情</a> -
                                            <c:if test="${order.orderPayWay != 5 }">
                                                <a class="js-print" onclick="toPrint('${order.id}');" style="cursor: pointer;">打印订单</a> -
                                            </c:if>
                                            <a class="js-remark" onclick="orderRemark(1,this);" style="cursor: pointer;">备注</a>
                                            <!--  <a href="javascript:;" class="js-star">加星</a> -->
                                        </div>
                                        <!-- <div class="js-star-container hide">
                                            <div class="ui-star" data-reactid=".0">
                                                <span title="去星" class="active" data-reactid=".0.$0"></span>
                                                <span title="一星" class="" data-reactid=".0.$1"></span>
                                                <span title="二星" class="" data-reactid=".0.$2"></span>
                                                <span title="三星" class="" data-reactid=".0.$3"></span>
                                                <span title="四星" class="" data-reactid=".0.$4"></span>
                                                <span title="五星" class="" data-reactid=".0.$5"></span>
                                            </div>
                                        </div> -->
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${order.orderPayWay != 5 }">
                                <c:forEach var="orderDetail" items="${order.mallOrderDetail }" varStatus="s">
                                    <input type="hidden" class="returnStatus" value="${orderDetail.status }"/>
                                    <tr class="content-row">
                                        <td class="image-cell">
                                            <img src="${path }${orderDetail.productImageUrl }"/>
                                        </td>
                                        <td class="title-cell" width="164" style="overflow: hidden;">
                                            <p class="goods-title">
                                                <a href="javascript:void(0);" class="new-window" style="cursor: default;"
                                                   title="${orderDetail.detProName }">${orderDetail.detProName }</a>
                                            </p>
                                            <p>
                                                <span class="goods-sku">${orderDetail.productSpeciname }</span>
                                            </p>
                                        </td>
                                        <td class="price-cell">
                                            <c:if test="${order.orderPayWay != 4 && order.orderPayWay != 8}">
                                                <p>￥${orderDetail.detProPrice }</p>
                                            </c:if>
                                            <c:if test="${order.orderPayWay == 4 }">
                                                <p>${orderDetail.detProPrice }积分</p>
                                            </c:if>
                                            <c:if test="${order.orderPayWay == 8 }">
                                                <p>${orderDetail.detProPrice }粉币</p>
                                            </c:if>
                                        </td>
                                        <td class="price-cell">
                                            <p>${orderDetail.detProNum }</p>
                                        </td>
                                        <c:set var="isReturns" value="0"></c:set>
                                        <td class="aftermarket-cell" rowspan="1">
                                            <c:if test="${!empty orderDetail.status && orderDetail.orderReturn != null}">
                                                <c:if test="${orderDetail.status == 0 }">
                                                    <c:set var="isReturns" value="1"></c:set>
                                                    买家申请退款
                                                    <c:if test="${orderDetail.orderReturn.id != null }">
                                                        <c:if test="${orderDetail.orderReturn.retHandlingWay == 1 }">
                                                            <div><a href="javascript:void(0);" class="agreaReturn"
                                                                    rId="${orderDetail.orderReturn.id }"
                                                                    oNo="${order.orderPNo }" orderPayNo="${order.orderPayNo}"
                                                            >同意买家退款</a></div>
                                                        </c:if>
                                                        <c:if test="${orderDetail.orderReturn.retHandlingWay == 2 }">
                                                            <div><a href="javascript:void(0);" class="agreaReturnAddress"
                                                                    rId="${orderDetail.orderReturn.id }"
                                                                    oNo="${order.orderPNo }" orderPayNo="${order.orderPayNo}"
                                                            >同意退货，并发送退货地址</a></div>
                                                        </c:if>
                                                        <div><a href="javascript:void(0);" class="closeReturn"
                                                                rId="${orderDetail.orderReturn.id}"
                                                                oNo="${order.orderNo }" orderPayNo="${order.orderPayNo}"
                                                        >拒绝退款申请</a></div>
                                                    </c:if>
                                                </c:if>
                                                <c:if test="${orderDetail.status == 3 }">
                                                    <c:set var="isReturns" value="1"></c:set>
                                                    已退货等待商家确认收货
                                                    <div><a href="javascript:void(0);" class="realReturn"
                                                            rId="${orderDetail.orderReturn.id }"
                                                            oNo="${order.orderNo }" orderPayNo="${order.orderPayNo}"
                                                    >确认收货并退款</a></div>
                                                    <div><a href="javascript:void(0);" class="jujueReturn"
                                                            rId="${orderDetail.orderReturn.id }"
                                                            oNo="${order.orderNo }" orderPayNo="${order.orderPayNo}"
                                                    >拒绝确认收货</a></div>
                                                </c:if>
                                                <c:if test="${orderDetail.status == 1 || orderDetail.status == 5}">退款成功<c:set var="isReturns" value="1"></c:set></c:if>
                                                <c:if test="${orderDetail.status == -1 }">卖家不同意退款<c:set var="isReturns" value="1"></c:set></c:if>
                                                <c:if test="${orderDetail.status == -2 }">买家已关闭退款<c:set var="isReturns" value="1"></c:set></c:if>
                                                <c:if test="${orderDetail.status == 2 }">已同意退款退货，请退货给商家<c:set var="isReturns" value="1"></c:set></c:if>
                                                <c:if test="${orderDetail.status == 4 }">
                                                    商家未收到货，不同意退款申请
                                                    <div><a href="javascript:void(0);" class="updReturnAdress"
                                                            rId="${orderDetail.orderReturn.id }"
                                                            oNo="${order.orderNo }" orderPayNo="${order.orderPayNo}"
                                                    >同意退货，修改退货地址</a></div>
                                                    <c:set var="isReturns" value="1"></c:set>
                                                </c:if>
                                            </c:if>
                                        </td>
                                        <c:if test="${s.count==1}">
                                            <td class="customer-cell" rowspan="${fn:length(order.mallOrderDetail)}">
                                                <p>${order.memberName}</p>
                                                <p class="user-name">${order.receiveName}</p>
                                                    ${order.receivePhone}
                                            </td>
                                            <td class="time-cell" rowspan="${fn:length(order.mallOrderDetail)}">
                                                <div class="td-cont">
                                                    <fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                </div>
                                            </td>
                                            <td class="state-cell" rowspan="${fn:length(order.mallOrderDetail)}">
                                                <div class="td-cont">
                                                    <p class="js-order-state">
                                                        <c:if test="${order.orderStatus == 1 }">待付款
                                                            <a id="butCancel" onclick="orderRemark(2,this);" style="cursor: pointer;">取消订单</a>
                                                        </c:if>
                                                        <c:if test="${order.orderStatus == 2  && order.deliveryMethod == 1}">待发货
                                                            <a id="butShip" onclick="orderRemark(4,this,${order.groupBuyId==null?0:order.groupBuyId });"
                                                               style="cursor: pointer;">发货</a>
                                                        </c:if>
                                                        <c:if test="${order.orderStatus == 2  && order.deliveryMethod == 2}">待提货<br/>
                                                            <a id="butShip" onclick="orderRemark(5,this,${order.groupBuyId==null?0:order.groupBuyId });" style="cursor: pointer;">确认已提货</a>
                                                        </c:if>
                                                        <c:if test="${order.orderStatus == 3 }">已发货</c:if>
                                                        <c:if test="${order.orderStatus == 4 }">交易完成</c:if>
                                                        <c:if test="${order.orderStatus == 5 }">订单关闭</c:if>
                                                        <c:if test="${order.orderStatus == 6 }">退款中</c:if>
                                                    </p>
                                                </div>
                                            </td>
                                            <td class="pay-price-cell" rowspan="${fn:length(order.mallOrderDetail)}">
                                                <div class="td-cont text-center">
                                                    <div>
                                                        <c:if test="${order.orderPayWay != 4 && order.orderPayWay != 8}">
                                                            ￥${order.orderMoney}
                                                        </c:if>
                                                        <c:if test="${order.orderPayWay == 4 }">
                                                            ${order.orderMoney}积分
                                                        </c:if>
                                                        <c:if test="${order.orderPayWay == 8 }">
                                                            ${order.orderMoney}粉币
                                                        </c:if>
                                                        <br>
                                                        <c:if test="${order.orderFreightMoney != null && order.orderFreightMoney != ''&& order.orderFreightMoney > 0}">
                                                            <span class="c-gray">(含运费￥${order.orderFreightMoney})</span>
                                                        </c:if>
                                                        <br></div>
                                                    <c:if test="${order.orderStatus == 1 }">
                                                        <a style="color: #0077df; cursor: pointer;" onclick="orderRemark(3,this);">修改价格</a>
                                                    </c:if>
                                                    <c:set var="walletPay" value="false"></c:set>
                                                    <c:set var="msg" value="钱包支付"></c:set>
                                                        <%-- <c:if test="${order.orderStatus == 1 && order.orderPayWay == 1}">
                                                               <c:set var="walletPay" value="true"></c:set>
                                                        </c:if> --%>
                                                    <c:if test="${order.deliveryMethod == 2 && order.orderPayWay == 6 && (order.isWallet == 0)}">
                                                        <c:set var="walletPay" value="true"></c:set>
                                                    </c:if>
                                                    <c:if test="${walletPay && isReturns == 0 && order.orderStatus != 5}">
                                                        <a style="color: #0077df; cursor: pointer;" onclick="walletPay('${order.id}');">钱包支付</a>
                                                    </c:if>
                                                    <c:if test="${order.isWallet == -1 && isReturns == 0 && order.orderStatus != 5}">
                                                        <a style="color: #0077df; cursor: pointer;" href="/cashier/pay_list.do">钱包支付中，查询订单</a>
                                                    </c:if>
                                                        <%-- <c:if test="${order.isWallet==1 && isReturns == 0 }">已使用钱包支付</c:if> --%>
                                                </div>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <c:if test="${order.orderPayWay == 5 }"><!-- 扫码支付 -->
                            <tr class="content-row">
                                <td class="image-cell"></td>
                                <td class="title-cell">
                                    <p class="goods-title"> 扫码支付 </p>
                                </td>
                                <td class="price-cell">
                                    <p>￥${order.orderMoney }</p>
                                </td>
                                <td class="price-cell"><p>1</p></td>
                                <td class="aftermarket-cell" rowspan="1"></td>
                                <td class="customer-cell" rowspan="">
                                    <p>${order.memberName}</p>
                                    <p class="user-name">${order.receiveName}</p>
                                        ${order.receivePhone}
                                </td>
                                <td class="time-cell" rowspan="">
                                    <div class="td-cont">
                                        <fmt:formatDate value="${order.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </div>
                                </td>
                                <td class="state-cell" rowspan="">
                                    <div class="td-cont">
                                        <p class="js-order-state">
                                            <c:if test="${order.orderStatus == 1 }">待付款
                                            </c:if>
                                            <c:if test="${order.orderStatus == 2 }">已付款
                                            </c:if>
                                            <c:if test="${order.orderStatus == 5 }">订单已关闭
                                            </c:if>
                                        </p>
                                    </div>
                                </td>
                                <td class="pay-price-cell" rowspan="">
                                    <div class="td-cont text-center"> ￥${order.orderMoney} </div>
                                </td>
                            </tr>
                            </c:if>
                            <c:if test="${order.orderSellerRemark != ''&& order.orderSellerRemark != null}">
                                <tr class="remark-row">
                                    <td colspan="9">卖家备注： ${order.orderSellerRemark }</td>
                                </tr>
                            </c:if>
                            </tbody>
                        </c:forEach>
                    </table>
                    <div class="js-list-empty-region"></div>
                </div>
                <jsp:include page="pageTwo.jsp"></jsp:include>
            </div>
        </div>

    </c:if>
    <c:if test="${!empty isNoAdminFlag }">
        <h1 class="groupH1"><strong>您还不是管理员，不能管理商城</strong></h1>
    </c:if>
</div>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
</body>

<script type="text/javascript">
    $(function () {
        loadLaydate();

        $('#selectType').change(function () {
            var type = $("#selectType option:selected").val();

            var filterType = $("#filter option:selected").val();
            $('#orderFilter').val(filterType);
            if (type == 1) {
                $("#status").val(7);
                $('#orderType').val(-1);
            } else {
                $('#orderType').val(type);
            }
            $('#orderForm').submit();
        });

        $('#filter').change(function () {
            var orderType = $("#selectType option:selected").val();
            $('#orderType').val(orderType);
            var type = $("#filter option:selected").val();
            $('#orderFilter').val(type);
            /* $('#orderForm').submit(); */
        });

        $('#srh').click(function () {
            searchOrder();
        });

        /**
         * 拒绝退款
         */
        $(".closeReturn").click(function () {
            var rId = $(this).attr("rId");
            var oNo = $(this).attr("oNo");
            var orderPayNo = $(this).attr("orderPayNo");
            /*parentOpenIframe("拒绝退款","500px","350px","/mallOrder/returnPopUp.do?rId="+rId+"&type=-1"+"&oNo="+oNo+"&orderPayNo="+orderPayNo);*/
            SonScrollTop(0);
            setTimeout(function () {
                layer.open({
                    type: 2,
                    title: "拒绝退款",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['530px', '350px'], //宽高
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    content: "/mallOrder/returnPopUp.do?rId=" + rId + "&type=-1" + "&oNo=" + oNo + "&orderPayNo=" + orderPayNo
                });
            }, timeout);
        });
        /**
         * 同意买家退款
         */
        $(".agreaReturn").click(function () {
            var rId = $(this).attr("rId");
            var oNo = $(this).attr("oNo");
            var orderPayNo = $(this).attr("orderPayNo");
            /*parentOpenIframe("同意买家退款","500px","300px","/mallOrder/returnPopUp.do?rId="+rId+"&type=1"+"&oNo="+oNo+"&orderPayNo="+orderPayNo);*/

            SonScrollTop(0);
            setTimeout(function () {
                layer.open({
                    type: 2,
                    title: "同意买家退款",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['500px', '300px'], //宽高
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    content: "/mallOrder/returnPopUp.do?rId=" + rId + "&type=1" + "&oNo=" + oNo + "&orderPayNo=" + orderPayNo
                });
            }, timeout);
        });
        /**
         * 同意退款退货
         */
        $(".agreaReturnAddress").click(function () {
            var rId = $(this).attr("rId");
            var oNo = $(this).attr("oNo");
            var orderPayNo = $(this).attr("orderPayNo");
            /*parentOpenIframe("同意退款退货","500px","380px","/mallOrder/returnPopUp.do?rId="+rId+"&type=2"+"&oNo="+oNo+"&orderPayNo="+orderPayNo);*/

            SonScrollTop(0);
            setTimeout(function () {
                layer.open({
                    type: 2,
                    title: "同意退款退货",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['530px', '380px'], //宽高
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    content: "/mallOrder/returnPopUp.do?rId=" + rId + "&type=2" + "&oNo=" + oNo + "&orderPayNo=" + orderPayNo
                });
            }, timeout);
        });

        /**
         * 确认收货并退款
         */
        $(".realReturn").click(function () {
            var rId = $(this).attr("rId");
            var oNo = $(this).attr("oNo");
            var orderPayNo = $(this).attr("orderPayNo");
            /*parentOpenIframe("确认收货并退款","500px","400px","/mallOrder/returnPopUp.do?rId="+rId+"&type=3"+"&oNo="+oNo+"&orderPayNo="+orderPayNo);*/
            SonScrollTop(0);
            setTimeout(function () {
                layer.open({
                    type: 2,
                    title: "确认收货并退款",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['500px', '400px'], //宽高
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    content: "/mallOrder/returnPopUp.do?rId=" + rId + "&type=3" + "&oNo=" + oNo + "&orderPayNo=" + orderPayNo
                });
            }, timeout);
        });
        /**
         * 拒绝确认收货
         */
        $(".jujueReturn").click(function () {
            var rId = $(this).attr("rId");
            var oNo = $(this).attr("oNo");
            var orderPayNo = $(this).attr("orderPayNo");
            /*parentOpenIframe("拒绝确认收货","500px","350px","/mallOrder/returnPopUp.do?rId="+rId+"&type=4"+"&oNo="+oNo+"&orderPayNo="+orderPayNo);*/

            SonScrollTop(0);
            setTimeout(function () {
                layer.open({
                    type: 2,
                    title: "拒绝确认收货",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['500px', '350px'], //宽高
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    content: "/mallOrder/returnPopUp.do?rId=" + rId + "&type=4" + "&oNo=" + oNo + "&orderPayNo=" + orderPayNo
                });
            }, timeout);
        });
        /**
         * 同意退货，修改退货信息
         */
        $(".updReturnAdress").click(function () {
            var rId = $(this).attr("rId");
            var oNo = $(this).attr("oNo");
            var orderPayNo = $(this).attr("orderPayNo");
            /*parentOpenIframe("同意退货，修改退货信息","500px","350px","/mallOrder/returnPopUp.do?rId="+rId+"&type=5"+"&oNo="+oNo+"&orderPayNo="+orderPayNo);*/

            SonScrollTop(0);
            setTimeout(function () {
                layer.open({
                    type: 2,
                    title: "同意退货，修改退货信息",
                    skin: 'layui-layer-rim', //加上边框
                    area: ['500px', '350px'], //宽高
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    content: "/mallOrder/returnPopUp.do?rId=" + rId + "&type=5" + "&oNo=" + oNo + "&orderPayNo=" + orderPayNo
                });
            }, timeout);
        });
    });
    function searchOrderFilter(obj) {
        var type = $(obj).find("option:selected").val();
        if (type != 0) {
            $('#orderFilter').val(type);
            var orderNo = $("#orderNo").val();
            if (orderNo != null && orderNo != "" && typeof(orderNo) != "undefined") {
                $('#orderForm').submit();
            }
        }
    }
    function searchOrder() {
        var type = $("#filter option:selected").val();
        if (type != 0) {
            $('#orderForm').submit();
        } else {
            SonScrollTop(0);
            setTimeout(function () {
                layer.alert("请先选择搜索类型！", {
                    offset: scrollHeight + "px",
                    shade: [0.1, "#fff"],
                    closeBtn: 0
                }, function (index) {
                    layer.closeAll();
                });
            }, timeout);
        }

    }
    function pressSearch(e) {
        var e = e || window.event;
        if (e.keyCode == 13) {
            searchOrder();
        }
    }

    //添加修改备注
    function orderRemark(type, obj, groupBuyId) {
        var parentObj = $(obj).parents("tbody");
        var orderId = parentObj.find("input.orderId").val();
        var returnStatus = parentObj.find("input.returnStatus").val();
        var flag = true;
        if (type == 1) {	//备注
            var order_width = "462px";
            var order_heigth = "230px";
        } else if (type == 2) {	//取消订单
            var order_width = "262px";
            var order_heigth = "200px";
        } else if (type == 3) {	//修改价格
            var order_width = "652px";
            var order_heigth = "320px";
        } else if (type == 5) {	//确认已提货
            var order_width = "262px";
            var order_heigth = "150px";
        } else {	//发货
            var order_width = "562px";
            var order_heigth = "330px";
            if (returnStatus != null && returnStatus != "") {
                if (returnStatus != -2 && returnStatus != -3 && returnStatus != 1 && returnStatus != 5) {//正在退款的订单不能发货
                    /*alertMsg("订单中的部分商品，买家已提交了退款申请。你需要先跟买家协商，买家撤销退款申请后，才能进行发货操作。");*/
                    SonScrollTop(0);
                    setTimeout(function () {
                        layer.alert("订单中的部分商品，买家已提交了退款申请。你需要先跟买家协商，买家撤销退款申请后，才能进行发货操作。", {
                            offset: scrollHeight + "px",
                            shade: [0.1, "#fff"],
                            closeBtn: 0
                        }, function (index) {
                            layer.closeAll();
                        });
                    }, timeout);
                    flag = false;
                }
            }
        }
        if (flag) {
            //parentOpenIframe("",order_width,order_heigth,"mallOrder/orderPopUp.do?orderId="+orderId+"&type="+type+"&groupBuyId="+groupBuyId);
            SonScrollTop(0);
            setTimeout(function () {
                layer.open({
                    type: 2,
                    title: "",
                    shadeClose: true,
                    shade: [0.1, "#fff"],
                    offset: scrollHeight*1+10 + "px",
                    shadeClose: false,
                    area: [order_width, order_heigth],
                    content: "mallOrder/orderPopUp.do?orderId=" + orderId + "&type=" + type + "&groupBuyId=" + groupBuyId
                });
            }, timeout);
//		parent.openIframe("",order_width,order_heigth,"mallOrder/orderPopUp.do?orderId="+orderId+"&type="+type+"&groupBuyId="+groupBuyId);
        }
    }


    function orderStatus(num) {
        if (num == 0) {
            num = "";
        }
        $('#status').val(num);
        $('#orderForm').submit();
    }

    /**
     * 导出订单
     */
    function orderExp() {
        var status = $("input#status").val();
        var orderType = $("input#orderType").val();
        var orderFilter = $("input#orderFilter").val();
        var startTime = $("input#startTime").val();
        var endTime = $("input#endTime").val();
        var orderNo = $("#orderNo").val();
        //var orderFilter = $("#filter option:selected").val();
        window.location.href = "/mallOrder/exportMallOrder.do?status=" + status + "&orderType=" + orderType + "&startTime=" + startTime + "&endTime=" + endTime + "&orderFilter=" + orderFilter + "&orderNo=" + orderNo;
    }

    /**初始化两个日期控件*/
    function loadLaydate() {
        var datebox_1 = {
            elem: '#startTime',
            event: 'focus',
            festival: true,
            choose: function (datas) {
                $('#orderForm').submit();
            }
        };
        var datebox_2 = {
            elem: '#endTime',
            event: 'focus',
            festival: true,
            choose: function (datas) {
                $('#orderForm').submit();
            }
        };
        laydate(datebox_1);
        laydate(datebox_2);
    }

    var orderNos = "";

    /**
     * 钱包支付
     */
    function walletPay(orderId) {
        var layerLoad = layer.load(1, {
            shade: [0.3, '#fff'],
            offset: scrollHeight + "px",
        });

        $.ajax({
            type: "post",
            data: {id: orderId},
            url: "/phoneOrder/79B4DE7C/goPay.do",
            dataType: "json",
            success: function (data) {
                if (data.result) {

                    //重新生成订单号
                    $.ajax({
                        type: "post",
                        data: {id: orderId},
                        url: "/mallOrder/againGenerateOrderNo.do",
                        dataType: "json",
                        success: function (data) {
                            layer.closeAll();
                            if (data.result) {
                                //调用钱包接口
                                orderNos = data.no;
                                var out_trade_no = data.no;
                                var oMoney = data.money;
                                moneyPays(out_trade_no,oMoney);

                            } else {// 编辑失败
                                SonScrollTop(0);
                                setTimeout(function () {
                                    layer.alert(data.msg, {
                                        shade: [0.1, "#fff"],
                                        offset: scrollHeight + "px"
                                    });
                                }, timeout);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                            layer.closeAll();
                            SonScrollTop(0);
                            setTimeout(function () {
                                layer.alert("钱包支付失败，请稍后重试", {
                                    shade: [0.1, "#fff"],
                                    offset: scrollHeight + "px"
                                });
                            }, timeout);
                            return;
                        }
                    });

                } else {// 编辑失败
                    layer.closeAll();
                    SonScrollTop(0);
                    setTimeout(function () {
                        layer.alert(data.msg, {
                            shade: [0.1, "#fff"],
                            offset: scrollHeight + "px"
                        });
                    }, timeout);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                layer.closeAll();
                SonScrollTop(0);
                setTimeout(function () {
                    layer.alert("钱包支付失败，请稍后重试", {
                        shade: [0.1, "#fff"],
                        offset: scrollHeight + "px"
                    });
                }, timeout);
                return;
            }
        });
    }

    function moneyPays(out_trade_no,oMoney){
        var wxmpdomain = $(".wxmpdomain").val();
        var url = wxmpdomain + "/cashier/pay_page.do?model=3&out_trade_no=" + out_trade_no + "&total_fee=" + oMoney + "&businessUtilName=MallBusinessService&is_calculate=1";
//							 parent.openIframeNoScoll2("钱包支付",700,650,'4%',url,true);

        SonScrollTop(0);
        setTimeout(function () {
            layer.open({
                type: 2,
                title: "钱包支付",
                shadeClose: true,
                shade: [0.1, "#fff"],
                offset: scrollHeight + "px",
                shadeClose: false,
                area: ["700px", "650px"],
                content: url
            });
        }, timeout);
    }

    function refurbish() {
        layer.closeAll();
        //alert("刷新啦！")
        if (orderNos != null && orderNos != "") {
            layer.confirm('请前往钱包支付查询订单状态', {
                    btn: ['确定'],
                    shade: [0.1, '#fff'],
                    offset: "10%"
                },
                function (index, layero) {
                    layer.closeAll();
                    window.location.href = "/cashier/pay_list.do";
                });
        } else {
            location.reload();
        }
    }

    /**
     * 前往打印
     */
    function toPrint(orderId) {
        var printIndex = layer.open({
            type: 2,
            title: "订单打印",
            shadeClose: true,
            shade: [0.3, "#fff"],
            offset: "10%",
            shadeClose: false,
            area: ["730px", "360px"],
            content: "mallOrder/toPrintMallOrder.do?orderId=" + orderId
        });
    }

</script>
</html>