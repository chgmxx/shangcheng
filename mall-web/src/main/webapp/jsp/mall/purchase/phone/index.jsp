<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/phone/index.css">
    <link rel="stylesheet" type="text/css" href="/css/weui-master/dist/style/weui.css"/>
    <link rel="stylesheet" type="text/css" href="/css/mall/purchase/phone/swiper.min.css">
    <script src="/js/plugin/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="/js/mall/purchase/phone/index.js" type="text/javascript" charset="utf-8"></script>
    <script src="/js/mall/purchase/phone/swiper.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function () {
            var mySwiper = new Swiper('.swiper-container', {
                autoplay: 3000,
                onSlideChangeStart: function (swiper) {
                }
            });
        });
        //调用地图
        function txMap(latitude, longitude) {
            var address = "";
            //var url="https://apis.map.qq.com/tools/locpicker?search=1&type=1&key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU&referer=test";
            var url = "https://apis.map.qq.com/tools/poimarker?key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU&&referer=test&type=0";
            if (latitude != "-1" && longitude != "-1") {
                address = latitude + "," + longitude;
                url += "&marker=coord:" + address;
                window.location.href = url;
            }
        }
    </script>
    <title>报价单</title>
</head>
<body>
<div class="warp">
    <header class="quotes_header boderbottom">
        <div class="quotes_header_pic">
            <div class="swiper-container quotes-carousel">
                <div class="swiper-wrapper">
                    <c:forEach items="${carouselList}" var="img">
                        <div class="swiper-slide">
                            <a href="${img.carouselUrl}">
                                <img src="${img.carouselImg}" class="carousel_pic" height="100%"/></a>
                        </div>
                    </c:forEach>
                </div>
            </div>

        </div>
        <h1 class="quotes_header_h1">${order.orderTitle}</h1>
    </header>
    <div class="quotes_company boderbottom">
        <div class="company_list">
            <div class="company_list_left company_list_left1">公司名称：</div>
            <div class="company_list_right">${companyMode.companyName}</div>
        </div>
        <div class="company_list">
            <div class="company_list_left company_list_left2">公司地址：</div>
            <div class="company_list_right" onclick="txMap('${companyMode.latitude}','${companyMode.longitude}')">${companyMode.companyAddress}</div>
        </div>
        <div class="company_list">
            <div class="company_list_left company_list_left3">公司电话：</div>
            <div class="company_list_right"><a href="tel:${companyMode.companyTel}">${companyMode.companyTel}</a></div>
        </div>
        <div class="company_list">
            <div class="company_list_left company_list_left4">公司官网：</div>
            <div class="company_list_right"><a href="${companyMode.companyInternet}">${companyMode.companyInternet}</a></div>
        </div>
    </div>
    <div class="quotes_line boderbottom">
        <div class="flex">
            <div class="flex-1">报价单号：${order.orderNumber}</div>
            <div class="">
                开单日期：
                <fmt:formatDate value="${order.createDate}" pattern="yyyy-MM-dd"/>
            </div>
        </div>
    </div>
    <div class="quotes_txt">
        <p>${order.orderDescribe}</p>
    </div>

    <ul class="quotes_order_list">
        <c:forEach items="${orderdetails}" var="product">
            <li class="quotes_order_item boderbottom bodertop" onclick="location.href='/purchasePhone/79B4DE7C/findDetails.do?id=${product.id}'">
                <div>
                    <div class="flex">
                        <div class="flex-1">编号：${product.productId}</div>
                        <div class="flex-1 text-right">数量：${product.count}</div>
                    </div>
                </div>
                <div class="">
                    <div class="order_item_pic order_item_left">
                        <div>
                            <img src="${product.productImg}" alt="商品图片">
                        </div>
                    </div>
                    <div class="order_item_detail order_item_right">
                        <div class="detail1">${product.productName}</div>
                        <div class="detail2">人工费：<fmt:formatNumber type="number" value="${product.laborCost}" pattern="0.00" maxFractionDigits="2"/>元</div>
                        <div class="detail2">安装费：<fmt:formatNumber type="number" value="${product.installationFee}" pattern="0.00" maxFractionDigits="2"/>元</div>
                        <div class="detail2">运费：<fmt:formatNumber type="number" value="${product.freight}" pattern="0.00" maxFractionDigits="2"/>元</div>
                        <div class="flex">
                            <div class="flex-1">原价￥:<fmt:formatNumber type="number" value="${product.money}" pattern="0.00" maxFractionDigits="2"/></div>
                            <div class="flex-1 colorf0">优惠价￥:<fmt:formatNumber type="number" value="${product.discountMoney}" pattern="0.00" maxFractionDigits="2"/>
                            </div>
                        </div>
                    </div>
                </div>
            </li>
        </c:forEach>
    </ul>

    <div class="quotes_explain boderbottom bodertop">
        <div class="quotes_explain_left fl">说明：</div>
        <div class="quotes_explian_right">
            <p>${order.orderExplain}</p>
        </div>
    </div>

    <ul class="quotes_msg_list boderbottom bodertop">
        <div class="quotes_msg_tit">
            <a href="/purchasePhone/79B4DE7C/languagePage.do?orderId=${order.id}&busId=${order.busId}">写留言</a>
        </div>
        <jsp:useBean id="nowDate" class="java.util.Date"/>
        <c:forEach items="${languageList}" var="language">
            <li class="quotes_msg">
                <div class="weui-panel__bd">
                    <a href="javascript:void(0);" class="weui-media-box_appmsg quotes_msg_replay">
                        <div class="weui-media-box__hd">
                            <img class="weui-media-box__thumb" src="${language.headimgurl}" alt="头像">
                        </div>
                        <div class="weui-media-box__bd">
                            <h4 class="weui-media-box__title">${language.nickname}</h4>
                            <p class="weui-media-box__desc">${language.language_content}</p>
                        </div>
                    </a>
                </div>
                <fmt:parseDate var="someDate" value="${language.language_time}" pattern="yyyy-MM-dd HH:mm:ss"/>
                <c:set var="interval" value="${nowDate.time - someDate.time}"/>
                <div class="quotes_msg_time">
                    <c:if test="${interval/1000/60<60}">
                        <fmt:formatNumber value="${interval/1000/60}" pattern="#0"/>分钟前
                    </c:if>
                    <c:if test="${interval/1000/60>60 && interval/1000/60/60<24}">
                        <fmt:formatNumber value="${interval/1000/60/60}" pattern="#0"/>小时前
                    </c:if>
                    <c:if
                            test="${interval/1000/60/60>24 && interval/1000/60/60/24<365}">
                        <fmt:formatNumber value="${interval/1000/60/60/24}" pattern="#0"/>天前
                    </c:if>
                    <c:if test="${interval/1000/60/60/24>365}">
                        <fmt:formatNumber value="${interval/1000/60/60/24/365}" pattern="#0"/>年前
                    </c:if>
                </div>
                <c:if test="${!empty language.admin_content && language.admin_content!=''}">
                    <div class="weui-panel__bd">
                        <div class="weui-media-box_text huifu_cell">
                            <h4 class="weui-media-box__title">企业回复</h4>
                            <p class="weui-media-box__desc">${language.admin_content}</p>
                        </div>
                    </div>
                </c:if>
            </li>
        </c:forEach>
    </ul>

    <div class="quotes_attention boderbottom bodertop">
        <div class="attention_txt">关注我们</div>
        <div>
            <img src="${order.orderQrcode}" alt="关注我们" class="attention_code">
        </div>

        <div>
            <img src="/images/mall/purchase/icon3.png" alt="技术支持" class="attention_support">
        </div>
    </div>
    <c:if test="${order.orderType==0}">
    <!-- 全款 -->
    <div class="quotes_footer flex bodertop">
        <div class="quotes_footer_pay quankuan">
            合计:￥<fmt:formatNumber type="number" value="${order.allMoney}" pattern="0.00" maxFractionDigits="2"/>${order.haveTax==1?"":"(含税)"}</div>
        <c:if test="${order.orderStatus==0}">
            <div class="quotes_footer_ok">已关闭</div>
        </c:if>
        <c:if test="${order.orderStatus==1}">
            <div class="quotes_footer_ok">待发布</div>
        </c:if>
        <c:if test="${order.orderStatus==2}">
            <c:if
                    test="${!empty receivablesList && receivablesList.size()>0}">
                <div class="quotes_footer_ok">已付款</div>
            </c:if>
            <c:if test="${empty receivablesList || receivablesList.size()==0}">
                <div class="quotes_footer_ok" onclick="location.href='/purchasePhone/79B4DE7C/buy.do?orderId=${order.id}&haveContract=${order.haveContract}&busId=${order.busId}'">
                    ￥<fmt:formatNumber type="number" value="${order.allMoney}" pattern="0.00" maxFractionDigits="2"/><br/>确认付款
                </div>
            </c:if>
        </c:if>
        <c:if test="${order.orderStatus==3}">
            <div class="quotes_footer_ok">已付款</div>
        </c:if>
        <c:if test="${order.orderStatus==4}">
            <div class="quotes_footer_ok">已完成</div>
        </c:if>
    </div>
    </c:if>
    <c:if test="${order.orderType==1}">
    <!-- 分期 -->
    <div class="quotes_footer flex bodertop">
        <div class="quotes_footer_pay">
            <div class="flex pt">
                <div class="flex-1 colorf0">
                        ${empty stage?"本":stage}期应付:￥<fmt:formatNumber type="number" value="${empty nowMoney?0.00:nowMoney}" pattern="0.00" maxFractionDigits="2"/>
                </div>
                <div class="flex-1">
                    下期应付:￥<fmt:formatNumber type="number" value="${empty nextMoney?0.00:nextMoney}" pattern="0.00" maxFractionDigits="2"/>
                </div>
            </div>
            <div class="flex">
                <div class="flex-1">分期总额:￥${order.allMoney}${order.haveTax==1?"":"(含税)"}</div>
                <div class="flex-1">剩余尾款:￥<fmt:formatNumber type="number" value="${retainage}" pattern="0.00" maxFractionDigits="2"/></div>
            </div>
            <div class="link_icon">
                <img src="/images/mall/purchase/down_icon.png" onclick="location.href='/purchasePhone/79B4DE7C/termDetails.do?orderId=${order.id}'"/>
            </div>
        </div>
        <c:if test="${order.orderStatus==0}">
            <div class="quotes_footer_ok">已关闭</div>
        </c:if>
        <c:if test="${order.orderStatus==1}">
            <div class="quotes_footer_ok">待发布</div>
        </c:if>
        <c:if test="${order.orderStatus==2}">
            <c:if test="${nowMoney==0}">
                <div class="quotes_footer_ok">已付款</div>
            </c:if>
            <c:if test="${nowMoney>0}">
                <div class="quotes_footer_ok box_ver"
                     onclick="location.href='/purchasePhone/79B4DE7C/buy.do?orderId=${order.id}&haveContract=${order.haveContract}&busId=${order.busId}'">
                    <p>￥${empty nowMoney?"0.00":nowMoney}</p>
                    <p>确认付款</p>
                </div>
            </c:if>
        </c:if>
        <c:if test="${order.orderStatus==3}">
            <div class="quotes_footer_ok">已付款</div>
        </c:if>
        <c:if test="${order.orderStatus==4}">
            <div class="quotes_footer_ok">已完成</div>
        </c:if>

    </div>
    </c:if>
</body>
</html>
