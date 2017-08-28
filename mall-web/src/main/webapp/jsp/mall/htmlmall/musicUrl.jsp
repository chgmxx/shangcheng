<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <base href="<%=basePath%>">
    <title>音乐</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my page">
    <link rel="stylesheet" type="text/css" href="/css/scene/music.css">
    <script type="text/javascript" src="/js/plugin/jquery-1.8.3.min.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript" src="/js/plugin/jquery-form.js?<%= System.currentTimeMillis()%>"></script>
    <script src="/js/plugin/layer/layer.js?<%= System.currentTimeMillis()%>"></script>
    <script type="text/javascript">

        $(function () {
            var musicaddres = '${addres}';
            if (musicaddres == 0 || musicaddres == 1) {
                $(".icon-selected-1").css("display", "block");
            } else if (musicaddres == 2) {
                $(".icon-selected-2").css("display", "block");
            }
            else if (musicaddres == 3) {
                $(".icon-selected-3").css("display", "block");
            }
            else {
                $(".icon-selected-4").css("display", "block");
            }
            var player_style = '${player_style}';
            if (player_style == null || player_style == '') {
                $(".icon-music").css("background", "url(/images/music/music01.png)");
                $(".icon-music").css("background-size", "contain");
            } else {
                $(".icon-music").css("background", "url(" + player_style + ")");
                $(".icon-music").css("background-size", "contain");
            }
            $(".icon-music").css("background-repeat", "no-repeat");
            var musicurl = '${musicurl}';
            if (musicurl == null || musicurl == '' || musicurl == undefined) {
                $("#playclass").attr("class", "icon-music-none");
            }


            //删除按钮
            $(".icon-delete").click(function () {

                $(".audio-name").text("没有背景音乐");
                $("#music_id").val("");
                $("#bgMusic").attr("src", "");
                promptBox(data.message);
                $("#playclass").attr("class", "icon-music-none");
                $(".icon-music-none").text('试听');


            })
            $(".audio-position-list-0").click(function () {
                $(".icon-selected").css("display", "none");
                $(this).find(".icon-music").css("display", "none");
                $(this).find(".icon-selected").css("display", "block");
                $(".icon-music").css("display", "block");
            })
        })
        //音乐试听效果
        function play(obj) {
            var clas = $(obj).attr('class');
            var music = document.getElementById("bgMusic");
            if (clas == "icon-play") {
                $(obj).attr("class", "icon-pause");
                music.play();
                $(".icon-pause").text('暂停');
            } else if (clas == "icon-pause") {
                $(obj).attr("class", "icon-play");
                $(".icon-play").text('试听');
                music.pause();
            } else {

            }
        }
        /**
         * 图片切换
         */
        function playchange() {
            var phone = $("#play_style").val();
            $(".icon-music").css("background", "url(" + phone + ")");
            $(".icon-music").css("background-size", "contain");
            $(".icon-music").css("background-repeat", "no-repeat");
        }
        /**
         * 保存方法
         */
        function save() {
            var bgmusicurl = $("#bgMusic").attr("src");
            var musicurl = bgmusicurl.split("upload")[1];
            var musicname = $(".audio-name").text();
            var player_style = $("#play_style").val();
            var addres;
            $(".icon-selected").each(function () {
                if ($(this).css("display") == "block") {
                    addres = $(this).attr("option");
                }
            })
            //TODO parent.layer.getFrameIndex   parent.layerwindow
            var index = parent.layer.getFrameIndex(window.name);
            parent.layerwindow(musicurl, musicname, player_style, addres, index);

        }
        /**
         * 返回方法
         */
        function fh() {

            this.save();
        }
        /**
         *消息提示弹出框
         */
        function promptBox(txt) {
            layer.alert(txt, {
                offset: "10%",
                shade:[0.1,"#fff"],
                closeBtn: 0
            });
        }

        //弹出音乐素材库
        function materiallayer() {
            layer.open({
                type: 2,
                title: '音乐素材库',
                fix: false,
                shade:[0.2,"#fff"],
                shadeClose: false,
                closeBtn: 0,
                shift: 1,
                maxmin: false,
                area: ['550px', '520px'],//定义宽、高
                content: '/common/musicmatre.do',

            });
        }
        function fhmat(musicid, musicname, musicurl) {

            $("#bgMusic").attr("src", musicurl);
            $("#playclass").attr("class", "icon-pause");
            var music = document.getElementById("bgMusic");
            music.play();
            $(".icon-pause").text('暂停');
            $("#music_id").val(musicid);
            $(".audio-name").text(musicname);
            promptBox("替换成功");
        }

    </script>
</head>

<body>
<jsp:include page="/jsp/common/headerCommon.jsp"/>
<div class="audio-edit-area">
    <div>
        <span class="has-back-img" title="更换音乐"></span>
        <span class="audio-name">
		<c:choose>
            <c:when test="${musicurl eq 'undefined'  }">没有音乐背景</c:when>
            <c:otherwise>${musicname}${musicurl}</c:otherwise>
        </c:choose></span>
        <span class="audio-btns">
			<a href="javascipt:void(0)" class="icon-update" title="更换音乐" onclick="materiallayer()">替换</a>
            <a href="javascipt:void(0)" title="播放/暂停" id="playclass" class="icon-play" onclick="play(this)"
               style="<c:if test='${musicurl eq "undefined"}'>color: #c3c3c3</c:if>">试听</a>
			<a href="javascipt:void(0)" class="icon-delete" title="删除音乐"></a>
		</span>
        <audio id="bgMusic" src="${http}${musicurl}" loop></audio>
    </div>
    <div style="clear: both;"></div>
    <!-- <div style="text-align: center;color: red;display: inline;font-size:12px;">提示：音乐上传MP3格式的，大小不能超过3M
    </div> -->
</div>

<!-- <div>
  <p style="text-align: center;color: red;line-height: 42px;display: inline;font-size:12px "> 提示：音乐上传MP3格式的，大小不能超过3M</p>
</div> -->

<div class="audio-position-select">
    <p style=" font-size: 14px;">设置音乐播放器页面位置(默认右上角)</p>
    <ul>
        <li class="audio-position-list-0"><span class="icon-backImg"></span>
            <span class="icon-position">右上</span> <i class="icon-selected icon-selected-1" option="1"
            ></i> <i class="icon-music icon-music-1"></i>
        </li>
        <li class="audio-position-list-0"><span class="icon-backImg"></span>
            <span class="icon-position">右下</span> <i class="icon-selected icon-selected-2" option="2"
            ></i> <i class="icon-music icon-music-2"></i>
        </li>
        <li class="audio-position-list-0"><span class="icon-backImg"></span>
            <span class="icon-position">左上</span> <i class="icon-selected icon-selected-3" option="3"
            ></i> <i class="icon-music icon-music-3"></i>
        </li>
        <li class="audio-position-list-0"><span class="icon-backImg"></span>
            <span class="icon-position">左下</span> <i class="icon-selected icon-selected-4" option="4"
            ></i> <i class="icon-music icon-music-4"></i>
        </li>
    </ul>
</div>
<div style=" margin-top: 15px;">
    <span style=" font-size: 14px;  margin-right: 114px; margin-left: 18px;">播放器样式选择</span>

    <select name="play_style" onchange="playchange()" id="play_style" style="width: 200px;text-align: center;height: 30px;">
        <c:forEach items="${playlist}" var="play" varStatus="vs">
            <option value="${play.item_key}"
                    <c:if test="${play.item_key == map.player_style}">selected</c:if> >${play.item_value}</option>
        </c:forEach>
    </select>
</div>

<div style="width: 200px; height: 30px; margin: 0 auto; padding-top: 3px">
    <a href="javascript:;" onclick="save()"
       style=" background-color: #1aa1e7; border-radius: 3px;color: #fff; display:inline-block; font-size: 14px; height: 15px;line-height: 15px; text-align: center;width: 70px; cursor: pointer; padding:8px;margin-right: 20px">保存</a>
    <a href="javascript:;" onclick="fh()"
       style=" background-color: #1aa1e7; border-radius: 3px;color: #fff; display: inline-block; font-size: 14px; height: 15px;line-height: 15px; text-align: center;width: 70px; cursor: pointer; padding:8px">返回</a>
</div>
</body>
</html>
