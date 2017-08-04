<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE>
<html lang="en">
<head>
    <meta name="renderer" content="webkit"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta charset="utf-8"/>
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--[if IE 8]>
    <meta http-equiv="X-UA-Compatible" content="IE=8">
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="/css/common/left.css"/>
<body>
<!-- <div class="side-con fl clearfix"> -->
<!-- 除了菜单5之外的父类菜单，都要弹窗到绑定页面 -->
<!-- 父类菜单5，子类菜单8，弹窗到绑定页面 -->
<div id="nav">
    <div class="dao-hang" id="Navigation">
        <ul id="dao-hang0">
            <%-- ${fn:length(map.amenu)} --%>
            <c:if test="${map.appid==1}">
                <c:forEach items="${map.amenu}" var="amenu" varStatus="vs">
                    <c:if test="${amenu.menus_id == url.parentclass_id}">

                        <li onclick="setTab(0,${vs.index})" class="hover"><a
                                style="color:#1aa1e7" class="ahover"> <img id="img1"
                                                                           src="${amenu.menus_describe}" class="amenushover"/>${amenu.menus_name}</a>
                        </li>

                    </c:if>
                    <c:if test="${amenu.menus_id ne url.parentclass_id}">

                        <a style="color:#fff"
                           href="javascript:newhref('${amenu.url}','${url.parentclass_id}','${url.logo}')">
                            <li onclick="setTab(0,${vs.index})" class="candan"><input
                                    type="hidden" name="menus_id" class="menus_id"
                                    value="${amenu.menus_id}"/> <input type="hidden"
                                                                       name="menus_logo" class="menus_logo" value="${amenu.logo}"/>
                                <input type="hidden" name="menus_describe"
                                       class="menus_describe" value="${amenu.menus_describe}"/> <img
                                        id="img1" src="${amenu.logo}"/>${amenu.menus_name}</li>
                        </a>
                    </c:if>

                </c:forEach>
            </c:if>
            <c:if test="${map.appid==0}">
                <c:forEach items="${map.amenu}" var="amenu" varStatus="vs">
                    <c:choose>
                        <c:when test="${amenu.menus_id  eq 5  || amenu.iswx eq 1}">
                            <c:if test="${amenu.menus_id == url.parentclass_id}">
                                <li onclick="setTab(0,${vs.index})" class="hover"><a
                                        style="color:#1aa1e7" class="ahover"> <img id="img1"
                                                                                   src="${amenu.menus_describe}" class="amenushover"/>${amenu.menus_name}</a>

                                </li>
                            </c:if>
                            <c:if test="${amenu.menus_id ne url.parentclass_id}">

                                <a style="color:#fff"
                                   href="javascript:newhref('${amenu.url}',${url.parentclass_id},'${amenu.logo}')">
                                    <li onclick="setTab(0,${vs.index})" class="candan"><input
                                            type="hidden" name="menus_id" class="menus_id"
                                            value="${amenu.menus_id}"/> <input type="hidden"
                                                                               name="menus_logo" class="menus_logo" value="${amenu.logo}"/>
                                        <input type="hidden" name="menus_describe"
                                               class="menus_describe" value="${amenu.menus_describe}"/> <img
                                                id="img1" src="${amenu.logo}"/>${amenu.menus_name}</li>
                                </a>

                            </c:if>
                        </c:when>
                        <c:otherwise>
                            <li onclick="a()" class=candan><input type="hidden"
                                                                  name="menus_id" class="menus_id" value="${amenu.menus_id}"/>
                                <input type="hidden" name="menus_logo" class="menus_logo"
                                       value="${amenu.logo}"/> <input type="hidden"
                                                                      name="menus_describe" class="menus_describe"
                                                                      value="${amenu.menus_describe}"/> <img id="img1"
                                                                                                             src="${amenu.logo}"/>${amenu.menus_name}</li>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </c:if>
        </ul>
    </div>
    <div id="mcont0" class="zidao-hang">
        <c:forEach items="${map.amenu}" var="twomenu" varStatus="vs">
        <c:if test="${vs.index==map.num}">
        <ul id="dao${vs.index}" style='display: block; min-height: 444px '>
            </c:if>
            <c:if test="${vs.index!=map.num}">
            <ul id="dao${vs.index}" style='display: none;min-height: 444px'>
                </c:if>
                <c:if test="${map.appid==1}">
                    <c:forEach items="${map.twomenu}" var="zimenu" varStatus="vs">
                        <li
                                class='<c:if test="${url.menus_id eq zimenu.menus_id}">cur-hover</c:if>'>
                            <a href="${zimenu.url}">${zimenu.menus_name}</a>
                        </li>

                    </c:forEach>
                </c:if>
                <c:if test="${map.appid==0}">
                    <c:forEach items="${map.twomenu}" var="zimenu" varStatus="vs">
                        <c:choose>

                            <c:when test="${zimenu.iswx  eq 0}">
                                <li
                                        class='<c:if test="${url.menus_id eq zimenu.menus_id}">cur-hover</c:if>  not-hover'>
                                    <span style="cursor: pointer;">${zimenu.menus_name}</span>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li
                                        class='<c:if test="${url.menus_id eq zimenu.menus_id}">cur-hover</c:if>'>
                                    <a href="${zimenu.url}">${zimenu.menus_name}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </c:if>
                <!-- <li>
                    <div class="gt-left-menuz-title-nomore" onclick="gtLeftMenu.noMoreLink(this,'/loginController/index.do')">唯美融 1</div>
                </li>
                <li>
                    <div class="gt-left-menuz-title" onclick="gtLeftMenu.click(this)">唯美融2 <span class="iconz"></span></div>
                    <div class="gt-left-menuz-box">
                        <div class="gt-left-menuz-item" onclick="gtLeftMenu.link(this,'/loginController/index.do')">唯美融1</div>
                        <div class="gt-left-menuz-item" onclick="gtLeftMenu.link(this,'/loginController/index.do')">唯美融2</div>
                        <div class="gt-left-menuz-item" onclick="gtLeftMenu.link(this,'/loginController/index.do')">唯美融3</div>
                    </div>
                </li>
                <li>
                    <div class="gt-left-menuz-title" onclick="gtLeftMenu.click(this)">唯美融3 <span  class="iconz"></span></div>
                    <div class="gt-left-menuz-box">
                        <div class="gt-left-menuz-item" onclick="gtLeftMenu.link(this,'/loginController/index.do')">唯美融1</div>
                        <div class="gt-left-menuz-item" onclick="gtLeftMenu.link(this,'/loginController/index.do')">唯美融2</div>
                        <div class="gt-left-menuz-item" onclick="gtLeftMenu.link(this,'/loginController/index.do')">唯美融3</div>
                    </div>
                </li> -->
            </ul>
            </c:forEach>
    </div>
</div>
<style>
    .gt-left-menuz-title, .gt-left-menuz-item, .gt-left-menuz-title-nomore {
        line-height: 55px;
        text-align: center;
        font-size: 14px;
        cursor: pointer;
    }

    .gt-left-menuz-title .iconz {
        display: inline-block;
        width: 15px;
        height: 15px;
        background-image: url(/images/menuLeftDownz.png);
        background-size: 100%;
        background-repeat: no-repeat;
        background-position: bottom;
        vertical-align: sub;
    }

    .gt-left-menuz-title:hover, .gt-left-menuz-item:hover, .gt-left-menuz-title-nomore:hover {
        background-color: #dbe8ed;
    }

    .gt-left-menuz-box {
        display: none;
    }

    .gt-left-menuz-title.cur-hover {
        background-color: #fb989f;
        color: #fff
    }

    .gt-left-menuz-item.cur-hover, .gt-left-menuz-title-nomore.cur-hover {
        background-color: #fb989f;
        color: #fff;
        font-weight: 700;
    }
</style>
<!-- </div> -->
<script>
    var GtLeftMenu = function () {
    };
    GtLeftMenu.prototype.click = function (t) {
        console.log($(t))
        $(t).siblings('.gt-left-menuz-box').slideDown();
        $(t).addClass('cur-hover');
        $(t).parent('li').siblings('li').find('.gt-left-menuz-title').removeClass('cur-hover');
        $(t).find('.iconz').css({"background-image": "url(/images/menuLeftUpz.png)"})
        $(t).parent('li').siblings('li').find('.gt-left-menuz-box').slideUp();
        $(t).parent('li').siblings('li').find('.iconz').css({"background-image": "url(/images/menuLeftDownz.png)"})
    }
    GtLeftMenu.prototype.link = function (t, url) {
        $(t).addClass('cur-hover').siblings('.gt-left-menuz-item').removeClass('cur-hover');
        $(t).parents('li').siblings('li').removeClass('cur-hover');
        $(t).parents('li').siblings('li').find('.gt-left-menuz-item').removeClass('cur-hover');
        $(t).parents('li').siblings('li').find('.gt-left-menuz-title-nomore').removeClass('cur-hover');
        $(t).parent('li').siblings('li').find('.iconz').css({"background-image": "url(/images/menuLeftDownz.png)"})
        window.location.href = url;
    }
    GtLeftMenu.prototype.noMoreLink = function (t, url) {
        $(t).addClass('cur-hover');
        $(t).parents('li').siblings('li').removeClass('cur-hover');
        $(t).parents('li').siblings('li').find('.gt-left-menuz-item').removeClass('cur-hover');
        $(t).parents('li').siblings('li').find('.gt-left-menuz-title').removeClass('cur-hover');
        $(t).parent('li').siblings('li').find('.iconz').css({"background-image": "url(/images/menuLeftDownz.png)"});
        $(t).parent('li').siblings('li').find('.gt-left-menuz-box').slideUp();
        window.location.href = url;
    }
    var gtLeftMenu = new GtLeftMenu();

    function heght_left() {
        var s = $(".candan").length + 1;
        var height = s * 74 + 128;
        return height;
    }
    function update_light(height) {
        $("#nav").height(height);
    }
    $(function () {
        var s = ${fn:length(map.amenu)};
        console.log("一级菜单的个数为：" + s);
        var height = s * 74;
        $("#mcont0 ul").height(height);
        console.log("二级菜单的高度为" + height);
        $("#mcont0 ul").bind("mouseover", function () {
            $(this).css("overflow-y", "auto");
        }).bind("mouseout", function () {
            $(this).css("overflow-y", "hidden");
        });
    });
</script>
</body>
</html>