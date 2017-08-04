<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
    <meta name="apple-mobile-web-app-capable" content="yes" />
    <meta name="apple-mobile-web-app-status-bar-style" content="black" />
    <meta name="format-detection" content="telphone=no, email=no" />
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="HandheldFriendly" content="true">
    <meta name="MobileOptimized" content="320">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <meta name="full-screen" content="yes">
    <meta name="x5-fullscreen" content="true">
    <meta name="browsermode" content="application">
    <meta name="x5-page-mode" content="app">
    <meta name="msapplication-tap-highlight" content="no">
    <link rel="stylesheet" href="/css/mall/wholesalers/public.css?<%=System.currentTimeMillis()%>">
    <link rel="stylesheet" href="/css/mall/wholesalers/wsStyle.css?<%=System.currentTimeMillis()%>">
    <title>批发商申请</title>
</head>
<body>
    <!--提交申请 遮罩层-->
    <section class="cd-popup" id="cd-apply">
        <div class="cd-main">
            <div class="cd-content">
                <i class="cd-close" onclick="closeLayer(this)"></i>
                <p>您已经提交申请，请耐心等候！</p>
            </div>
            <div class="cd-btn">
                <a href="javascript:void(0)" class="btn-okay" onclick="okayBtn();">好的</a>
            </div>
        </div>
    </section>
    <div class="wWrapper">
        <div class="ws-top">
            <p class="ws-tip"><font>基本信息的有效性决定您能否成为批发商，以及最终可获得的折扣</font></p>
        </div>
        <div class="ws-box">
            <!-- <form action="/phoneWholesaler/79B4DE7C/addWholesaler.do" method="post"> -->
                <div class="ws-box-1">
                    <div class="iBox clearfix">
                        <label for="wsUName" class="ws-label">姓名</label>
                        <input type="text" placeholder="请输入本人真实姓名" class="ws-input" id="wsUName" name="name">
                    </div>
                    <div class="iBox clearfix">
                        <label for="wsCName" class="ws-label">公司名称</label>
                        <input type="text" placeholder="请输入公司名称" class="ws-input" id="wsCName" name="companyName">
                    </div>
                    <div class="iBox clearfix">
                        <label for="wsTel" class="ws-label">手机号</label>
                        <input type="text" placeholder="请输入手机号码" class="ws-input" id="wsTel" name="telephone" maxlength="11">
                    </div>
                    <div class="iBox clearfix">
                        <label for="wsCode" class="ws-label">验证码</label>
                        <input type="text" placeholder="请输入收到的验证码" class="ws-input-s" id="wsCode" name="code" maxlength="6">
                        <a onclick="sendMsg(0,42297,'',this);" class="ws-a" id="obtainCode">获取验证码</a>
                    </div>
                </div>
                <div class="ws-box-1">
                    <div class="taBox clearfix">
                        <label for="wsRemark" class="ws-label-s">备注</label>
                        <textarea name="remark" class="ws-input no-border" id="wsRemark" cols="30" rows="4" placeholder="请输入"></textarea>
                    </div>
                </div>
                <div class="ws-btn">
                    <input type="button" class="btn-sub" value="申请" id="btnSub">
                </div>
            <!-- </form> -->
        </div>
        <c:if test="${!empty  pfApplayRemark}">
        <ol class="ws-notice">
            <li>${pfApplayRemark }</li>
        </ol>
        </c:if>
    </div>
    <input type="hidden" class="memberId" value="${memberId }"/>
    <input type="hidden" class="userid" value="${userid }"/>
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="/js/plugin/layer/layer.js"></script>
    <script type="text/javascript" src="/js/common/phone.js"></script>
    <script type="text/javascript" src="/js/mall/wholesalers/phone/applyWholesaler.js?<%=System.currentTimeMillis()%>"></script>
</body>
</html>