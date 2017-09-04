<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + request.getServletPath()
            .substring( 0, request.getServletPath().lastIndexOf( "/" ) + 1 );

%>
<!doctype html>
<html ng-app="app">
<head>
    <base href="<%=basePath%>">
    <meta charset="utf-8">
    <title>${msg.htmlname }</title>
    <meta id="meta" name="viewport"
          content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="format-detection" content="telephone=no"/>
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
    <meta http-equiv="Cache-Control"
          content="no-cache, no-store, must-revalidate"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <link rel="stylesheet" href="css/base.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" href="css/init.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" href="css/swiper.min.css?<%= System.currentTimeMillis()%>"/>
</head>
<style>
    .music-logo {
        width: 20px;
        height: 20px;
        background-size: contain;
        position: absolute;
        z-index: 9999;
        background-position: 50% 50%;
        background-size: contain;
    }
</style>
<body ng-controller="appController">
<div style="display: none">
    <img src="${http}${msg.bakurl}" id="bakurl" option='${msg.introduce }'/>
</div>
<section class="loading">
    <div class="load3">
        <div class="double-bounce1"></div>
        <div class="double-bounce2"></div>
    </div>
</section>

<script charset="utf-8" src="http://map.qq.com/api/js?v=2.exp&key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU"></script>
<link rel="stylesheet" href="css/all.css?<%= System.currentTimeMillis()%>"/>
<link rel="stylesheet" href="css/animate.css?<%= System.currentTimeMillis()%>"/>
<script src="js/jquery-1.8.0.min.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/swiper.min.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/angular.min.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/app.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/swipeDirective.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/animationDirective.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/colorToService.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/mapDirective.js?<%= System.currentTimeMillis()%>"></script>
<script src="js/bgswiperDirective.js?<%= System.currentTimeMillis()%>"></script>

<!-- Swiper -->

<div class="swiper-container">
    <div class="close-x"></div>
    <div class="swiper-wrapper">
        <div class="swiper-slide" style="background-image: url('')"></div>
    </div>
    <!-- Add Arrows -->
    <div class="swiper-button-next swiper-button-white"></div>
    <div class="swiper-button-prev swiper-button-white"></div>
</div>
<section id="pages" class="pages flip" bg-layout="left" style="width:380px;height:100%;background-color:#000;margin:0 auto;" my-draggable>

    <section class="page" style="background:{{dataModuleBg[$index].background}}" data-ng-repeat="page in datajson">
        <div class="bgswiper" ng-if="dataModuleBg[$index].bgswiper">
            <div style="background-image: url('{{pic}}')" data-ng-repeat="pic in dataModuleBg[$index].bgswiperpics"></div>
        </div>

        <div class="swiper-container-box" ng-if="dataTransverse[$index].dataTransverse_on" style="{{dataTransverse_style(dataTransverse[$index])}}">
            <div class="swiper-wrapper">
                <div class="swiper-slide" ng-repeat="i in dataTransverse[$index].data" style="background:{{dataTransverse[$parent.$index].bg[$index].background}}">
                    <!-- 轮播里面的子元素 -->
                    <div data-ng-repeat="data in i"
                         style="width:{{data.attrs.width}}px;height:{{data.attrs.height}}px;top:{{data.attrs.top}}px;left:{{data.attrs.left}}px;z-index:{{data.attrs.zIndex}};">

                        <div data-ng-if="haveSrc(data.src)" style="height:{{data.attrs.height}}px;-webkit-transform:rotate({{data.attrs.rotate}}deg);overflow: hidden;">
                            <div data-ng-hide="showDiscern(data.discern)"
                                 style="width: 100%;height: 100%;border-radius:{{unitchange(data.attrs.borderRadius)}};background:url({{data.src}}) top left no-repeat;background-size:{{setcutpicw(data.data.imgMaxW,data.data.width,data.attrs.width)}}px;background-position-x: {{-setcutpicxy(data.data.marginLeft,data.data.width,data.attrs.width)}}px;background-position-y:{{-setcutpicxy(data.data.marginTop,data.data.width,data.attrs.width)}}px;"></div>
                            <img data-ng-show="showDiscern(data.discern)" data-ng-src="{{data.src}}" style="width:100%;">
                        </div>
                        <div class="textbox" data-ng-if="haveSrc(data.text)">
                            <div ng-bind="data.text"
                                 style="height:100%;font-size:{{data.data.font_size}}px;color:{{data.data.font_color}};line-height:{{data.data.lineHeight}};font-family:{{font_name[data.data.font_name][0]}};text-align:{{data.data.font_align}};font-weight:{{data.data.font_weight}};font-style:{{data.data.font_style}};text-decoration:{{data.data.text_decoration}};-webkit-transform:rotate({{data.attrs.rotate}}deg);border-radius:{{unitchange(data.attrs.borderRadius)}};{{changeColor(data)}}"></div>
                        </div>
                        <div class="btnbox" data-ng-class={"insidepage":hasinsidepage(data.btnType)} data-page="{{data.page}}" data-ng-if="haveSrc(data.btnType)"
                             style="-webkit-transform:rotate({{data.attrs.rotate}}deg);border-radius:{{unitchange(data.attrs.borderRadius)}};{{changeColor(data)}}">
                            <a data-ng-if="!data.viewType"
                               style="font-size:{{data.dataText.font_size}}px;color:{{data.dataText.font_color}};text-align: {{data.dataText.font_align}};font-weight:{{data.dataText.font_weight}};font-style:{{data.dataText.font_style}};text-decoration:{{data.dataText.text_decoration}};"
                               href="{{hashref(data)}}" data-ng-click="layer(data)">
                                <span ng-bind="data.dataText.value"></span>
                            </a>
                            <a data-ng-if="data.viewType" href="{{hashref(data)}};" data-ng-click="layer(data)">
                                <img data-ng-src="{{data.dataPic.src}}">
                            </a>
                        </div>
                        <div class="musicbox whall" data-ng-if="haveSrc(data.datamusic)">
                            <div class="musicphoto whall" style="background-image: url({{data.datamusic.src}});{{hidemusic(data.music)}}" onclick="modelmusicplay(this)"></div>
                            <audio data-page="{{data.datamusic.page}}" data-ng-src="{{sce(data.datamusic.music)}}"></audio>
                        </div>
                        <div class="map" data-ng-if="haveSrc(data.dataMap)" onclick="showmapmessage(this)">
                            <div class="pin"></div>
                            <div class="pulse">
                                <div class="after"></div>
                            </div>
                            <div class="mapmessage hideMapMessage" style="display: none;">
                                <div class="map_title">
                                    <span data-ng-bind="data.dataMap.name"></span>
                                    <img class="map_close" src="images/close.png" onclick="hidemapmessage(this)">
                                </div>
                                <div>
                                    <p class="map_phone" data-ng-bind="data.dataMap.phone"></p>
                                    <p class="map_address" data-ng-bind="data.dataMap.address"></p>
                                </div>
                                <div style="height: 1px;background-color: #bab9b8"></div>
                                <div class="map_btn">
                                    <a class="map_call" href="tel:{{data.dataMap.phone}}"><img src="images/map_phone.png"
                                                                                               style="width: 17px;vertical-align: text-bottom;margin-right: 7px;">打电话</a>
                                    <a class="map_navigation"
                                       href="https://apis.map.qq.com/tools/routeplan/eword={{data.dataMap.address}}&epointx={{getlatlng(data.dataMap.latlng,1)}}&epointy={{getlatlng(data.dataMap.latlng,0)}}?referer=myapp&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"><img
                                            src="images/navigation.png" style="width: 17px;vertical-align: text-bottom;margin-right: 7px">导航</a>
                                </div>
                            </div>
                        </div>

                    </div>
                    <!-- 轮播里面的子元素end -->
                </div>
            </div>
        </div>

        <div data-ng-repeat="data in page"
             style="width:{{data.attrs.width}}px;height:{{data.attrs.height}}px;top:{{data.attrs.top}}px;left:{{data.attrs.left}}px;z-index:{{data.attrs.zIndex}};"
             data-ng-class={"animate":hasanimate(data.stratanimate,data.endanimate),"delay":hasdelay(data.stratanimate[2]),"enddelay":hasdelay(data.endanimate[2])}
             data-stratanimate="{{data.stratanimate}}" data-endanimate="{{data.endanimate}}" data-end-delay="{{data.endanimate[2]}}" data-delay="{{data.stratanimate[2]}}"
             animation-directive>
            <div data-ng-if="haveSrc(data.src)" style="height:{{data.attrs.height}}px;-webkit-transform:rotate({{data.attrs.rotate}}deg);overflow: hidden;">
                <div data-ng-hide="showDiscern(data.discern)"
                     style="width: 100%;height: 100%;border-radius:{{unitchange(data.attrs.borderRadius)}};background:url({{data.src}}) top left no-repeat;background-size:{{setcutpicw(data.data.imgMaxW,data.data.width,data.attrs.width)}}px;background-position-x: {{-setcutpicxy(data.data.marginLeft,data.data.width,data.attrs.width)}}px;background-position-y:{{-setcutpicxy(data.data.marginTop,data.data.width,data.attrs.width)}}px;"></div>
                <img data-ng-show="showDiscern(data.discern)" data-ng-src="{{data.src}}" style="width:100%;">
            </div>
            <div class="textbox" data-ng-if="haveSrc(data.text)">
                <div ng-bind="data.text"
                     style="height:100%;font-size:{{data.data.font_size}}px;color:{{data.data.font_color}};line-height:{{data.data.lineHeight}};font-family:{{font_name[data.data.font_name][0]}};text-align:{{data.data.font_align}};font-weight:{{data.data.font_weight}};font-style:{{data.data.font_style}};text-decoration:{{data.data.text_decoration}};-webkit-transform:rotate({{data.attrs.rotate}}deg);border-radius:{{unitchange(data.attrs.borderRadius)}};{{changeColor(data)}}"></div>
            </div>
            <div class="btnbox" data-ng-class={"insidepage":hasinsidepage(data.btnType)} data-page="{{data.page}}" data-ng-if="haveSrc(data.btnType)"
                 style="-webkit-transform:rotate({{data.attrs.rotate}}deg);border-radius:{{unitchange(data.attrs.borderRadius)}};{{changeColor(data)}}">
                <a data-ng-if="!data.viewType"
                   style="font-size:{{data.dataText.font_size}}px;color:{{data.dataText.font_color}};text-align: {{data.dataText.font_align}};font-weight:{{data.dataText.font_weight}};font-style:{{data.dataText.font_style}};text-decoration:{{data.dataText.text_decoration}};"
                   href="{{hashref(data)}}" data-ng-click="layer(data)">
                    <span ng-bind="data.dataText.value"></span>
                </a>
                <a data-ng-if="data.viewType" href="{{hashref(data)}};" data-ng-click="layer(data)">
                    <img data-ng-src="{{data.dataPic.src}}">
                </a>
            </div>
            <div class="musicbox whall" data-ng-if="haveSrc(data.datamusic)">
                <div class="musicphoto whall" style="background-image: url({{data.datamusic.src}});{{hidemusic(data.music)}}" onclick="modelmusicplay(this)"></div>
                <audio data-page="{{data.datamusic.page}}" data-ng-src="{{sce(data.datamusic.music)}}"></audio>
            </div>
            <div class="form" data-ng-if="haveSrc(data.dataform)" style="height: 100%;{{changeColor(data)}}">
                <div style="padding: 20% 0 20% 0">
                    <form action="" method="post">
                        <div class="form-box" data-ng-repeat="input in data.dataform.input.data">
                            <input type="text" maxlength="50" data-ng-if="input[0] == 0" placeholder="{{input[1]}}"
                                   style="border-radius:{{unitchange(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                            <input type="text" maxlength="50" data-ng-if="input[0] == 1" placeholder="{{input[1]}}"
                                   style="border-radius:{{unitchange(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                            <input type="tel" maxlength="50" data-ng-if="input[0] == 2" placeholder="{{input[1]}}"
                                   style="border-radius:{{unitchange(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                            <input type="email" maxlength="50" data-ng-if="input[0] == 3" placeholder="{{input[1]}}"
                                   style="border-radius:{{unitchange(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                            <input type="text" maxlength="50" data-ng-if="input[0] == 4" placeholder="{{input[1]}}"
                                   style="border-radius:{{unitchange(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                            <textarea maxlength="50" data-ng-if="input[0] == 5" placeholder="{{input[1]}}"
                                      style="border-radius:{{unitchange(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}"></textarea>
                            <input type="text" maxlength="50" data-ng-if="input[0] == 6" placeholder="{{input[1]}}"
                                   style="border-radius:{{unitchange(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                        </div>
                        <div class="form-box submit">
                            <input type="button" onclick="save(this)" value="{{data.dataform.submit.val}}"
                                   style="border-radius:{{unitchange(data.dataform.submit.borderRadius)}};border-width: {{data.dataform.submit.border}}px;{{showbgcolor(data.dataform.submit.bgcolor,data.dataform.submit.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.submit.borcolor,data.dataform.submit.boropacity,'border-color:')}}">
                        </div>
                    </form>
                </div>
            </div>
            <div class="map" data-ng-if="haveSrc(data.dataMap)" onclick="showmapmessage(this)">
                <div class="pin"></div>
                <div class="pulse">
                    <div class="after"></div>
                </div>
                <div class="mapmessage hideMapMessage" style="display: none;">
                    <div class="map_title">
                        <span data-ng-bind="data.dataMap.name"></span>
                        <img class="map_close" src="images/close.png" onclick="hidemapmessage(this)">
                    </div>
                    <div>
                        <p class="map_phone" data-ng-bind="data.dataMap.phone"></p>
                        <p class="map_address" data-ng-bind="data.dataMap.address"></p>
                    </div>
                    <div style="height: 1px;background-color: #bab9b8"></div>
                    <div class="map_btn">
                        <a class="map_call" href="tel:{{data.dataMap.phone}}"><img src="images/map_phone.png" style="width: 17px;vertical-align: text-bottom;margin-right: 7px;">打电话</a>
                        <a class="map_navigation"
                           href="https://apis.map.qq.com/tools/routeplan/eword={{data.dataMap.address}}&epointx={{getlatlng(data.dataMap.latlng,1)}}&epointy={{getlatlng(data.dataMap.latlng,0)}}?referer=myapp&key=OB4BZ-D4W3U-B7VVO-4PJWW-6TKDJ-WPB77"><img
                                src="images/navigation.png" style="width: 17px;vertical-align: text-bottom;margin-right: 7px">导航</a>
                    </div>
                </div>
            </div>


        </div>
        <div ng-if="$last" class="tip-off">举报</div>
    </section>
    <div class="iconUp"></div>

    <div class="tip-off-wrap" id="tip-off-wrap" style="display: none;">
        <div class="tip-off-content">
            <h5>举报的原因</h5>
            <ul class="tip-off-reason">
                <li class=""><label><input class="ui-radio" type="radio" name="tip-off" value="1">诈骗、反社会、谣言</label></li>
                <li class=""><label><input class="ui-radio" type="radio" name="tip-off" value="2">色情、赌博、毒品</label></li>
                <li class=""><label><input class="ui-radio" type="radio" name="tip-off" value="3">传销、邪教、非法集会</label></li>
                <li class=""><label><input class="ui-radio" type="radio" name="tip-off" value="4">侵权、抄袭</label></li>
                <li class=""><label><input class="ui-radio" type="radio" name="tip-off" value="5">恶意营销、侵犯隐私、诱导分享</label></li>
                <li class=""><label><input class="ui-radio" type="radio" name="tip-off" value="6">虚假广告</label></li>
                <li class=""><label><input class="ui-radio" type="radio" name="tip-off" value="7">其他原因</label></li>
            </ul>
            <div class="tip-off-div">
                <span class="tip-off-btn tip-off-con">确定</span><span class="tip-off-btn tip-off-can">取消</span>
            </div>
        </div>
    </div>

</section>
<div class="music-logo" style="background-image: url('${msg.playerStyle}');"></div>
<div class="iconUp"></div>
<audio id="bgMusic" src="${http}${msg.musicurl}" loop></audio>
<input type="hidden" id="htmlid" value="${msg.id }"><!--场景id  -->
<script>
    var cloud = {
        datajson: ${msg.datajson},
        dataModuleBg:${msg.databg},
        dataTransverse:${msg.datatransverse}
    };
    var shipei = false;
    var bgmusic = true;
    $(function () {
        //是否公众号
        var style = ${style};
        var desc = $("#backer_image_url").attr("option");
        var link = window.location.href;
        var imgUrl = $("#backer_image_url").attr("src");
        var title = '${msg.htmlname }';
        if (iswx == 0 && style == 0) {
            wx.config({
                debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: "${record.get('appid')}", // 必填，公众号的唯一标识
                timestamp: "${record.get('timestamp')}", // 必填，生成签名的时间戳
                nonceStr: "${record.get('nonce_str')}", // 必填，生成签名的随机串
                signature: "${record.get('signature')}",// 必填，签名，见附录1
                jsApiList: ["onMenuShareTimeline", "onMenuShareAppMessage"],// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
            });
            wx.ready(function () {
                //分享到朋友圈
                wx.onMenuShareTimeline({
                    title: title, // 分享标题
                    link: link, // 分享链接
                    imgUrl: imgUrl, // 分享图标
                    success: function () {
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                    }
                });
                //分享给朋友
                wx.onMenuShareAppMessage({
                    title: title, // 分享标题
                    desc: desc, // 分享描述
                    link: link, // 分享链接
                    imgUrl: imgUrl, // 分享图标
                    type: '', // 分享类型,music、video或link，不填默认为link
                    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                    success: function () {
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                    }
                });
            });
        }
    })

    $(function () {
        var addres = '${msg.addres}';
        $(".music-logo").css("background-repeat", "no-repeat")
        if (addres == '1') {
            $(".music-logo").css("right", "10px")
            $(".music-logo").css("top", "10px")
        } else if (addres == '2') {
            $(".music-logo").css("right", "10px")
            $(".music-logo").css("bottom", "10px")
        } else if (addres == '3') {
            $(".music-logo").css("left", "10px")
            $(".music-logo").css("top", "10px")
        } else {
            $(".music-logo").css("left", "10px")
            $(".music-logo").css("bottom", "10px")
        }
        $(".music-logo").click(function () {
            var music = document.getElementById("bgMusic");
            if (music.paused) {
                bgmusic = true;
                music.play();

                $(".music-logo").addClass("playing");

            } else {
                bgmusic = false;
                music.pause();

                $(".music-logo").removeClass("playing");
            }
        });

    });
    //定义全局变

    /**
     * 显示地图信息
     */
    function showmapmessage(param) {
        $(param).find(".mapmessage").show().removeClass("hideMapMessage");
    }
    /**
     * 隐藏地图信息
     */
    function hidemapmessage(param) {
        $(param).parents(".mapmessage").addClass("hideMapMessage");
        event.stopPropagation();
    }

    /**
     * 播放停止音乐
     */
    function modelmusicplay(param) {
        var audio = $(param).next();
        if (audio.attr("data-page") == 2) {
            if (!audio[0].paused) {
                audio[0].pause();
                musicplay(0);

            } else {
                musicplay(1);
                audio[0].play();

            }
        }
    }
    function musicplay(index) {
        var music = document.getElementById("bgMusic");
        if (index == '1' || index == 1) {
            music.pause();
            $(".music-logo").removeClass("playing");
        } else {
            music.play();
            document.addEventListener("WeixinJSBridgeReady", function () {
                WeixinJSBridge.invoke('getNetworkType', {}, function (e) {
                    music.play();
                });
            }, false);
            $(".music-logo").addClass("playing");
        }

    }

    var swiper = new Swiper('.swiper-container', {
        nextButton: '.swiper-button-next',
        prevButton: '.swiper-button-prev',
    });


    $(".close-x").on("click", function () {

        $(this).parent().animate({opacity: 0}, 800, function () {
            $(this).hide();
        });


    });
    function save(obj) {
        var htmlid = $("#htmlid").val();
        var ob = $(obj).parent().siblings().find("input");
        var data = {};
        data.htmlId = htmlid;
        ob.each(function (index, el) {
            if (index == 0) {
                data.category = $(this).attr("placeholder");
                data.categoryname = $(this).val();
            }
            if (index == 1) {
                data.genre = $(this).attr("placeholder");
                data.genrename = $(this).val();
            }
            if (index == 2) {
                data.family = $(this).attr("placeholder");
                data.familyname = $(this).val();
            }
            if (index == 3) {
                data.property = $(this).attr("placeholder");
                data.propertyname = $(this).val();
            }
            if (index == 4) {
                data.nature = $(this).attr("placeholder");
                data.naturename = $(this).val();
            }
            if (index == 5) {
                data.quality = $(this).attr("placeholder");
                data.qualityname = $(this).val();
            }
            if (index == 6) {
                data.attribute = $(this).attr("placeholder");
                data.attributename = $(this).val();
            }


        })
        $(obj).attr("disabled", "disabled");
        alert("提交成功");
        $.ajax({
            type: "post",
            url: "/mallhtml/79B4DE7C/htmlfrom.do",
            data: data,
            async: false,
            dataType: "json",
            success: function (data) {
            }
        })
    }
</script>
</body>
</html>
