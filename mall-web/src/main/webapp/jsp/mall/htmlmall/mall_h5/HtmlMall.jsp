<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>
<%
    /* String path=application.getRealPath(request.getRequestURI());   */
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + request.getServletPath()
            .substring( 0, request.getServletPath().lastIndexOf( "/" ) + 1 );
/* String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/"; */
%>

<!DOCTYPE html>
<html lang="en" ng-app="app">
<head>
    <base href="<%=basePath%>">
    <title>h5商城</title>
    <meta name="renderer" content="webkit"/>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

    <link rel="stylesheet" href="css/swiper.min.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="css/base.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" href="css/creation.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="css/animate.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="css/magic.min.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="css/farbtastic.css?<%= System.currentTimeMillis()%>"/>
    <link rel="stylesheet" href="js/buttongo/buttongo.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="js/music/music.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="js/form/form.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="js/map/map.css?<%= System.currentTimeMillis()%>">
    <link rel="stylesheet" href="css/transverse.css?<%= System.currentTimeMillis()%>">

    <script charset="utf-8" src="https://map.qq.com/api/js?v=2.exp&key=2VBBZ-A3C3O-E7XW7-S6RWA-Q676Z-O6FGU?<%= System.currentTimeMillis()%>"></script>
    <script src="js/jquery-1.8.0.min.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/swiper.min.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/angular.min.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/adminController.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/dataService.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/myDraggableDirective.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/myPlanDirective.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/colorToService.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/jsonService.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/farbtastic.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/scaleDirective.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/keyDirective.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/jcrop/cutpicDirective.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/buttongo/buttongoController.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/music/musicController.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/form/formController.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/map/mapController.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/Sortable.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/layer.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/transverse/transverseController.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/transverse/tsDraggable.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/transverse/swiperDirective.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/html.js?<%= System.currentTimeMillis()%>"></script>
    <script src="js/htmljs.js?<%= System.currentTimeMillis()%>"></script>
    <style type="text/css">
        .music {
            display: inline-block;
        }

        .music a {
            width: 100px;
            height: 30px;
            display: inline-block;
            margin-top: 14px;
            border-radius: 50px;
            background-color: #374047;
            margin-left: 82px;
        }

        .music a:hover {
            background-color: #1c1c1c;
        }

        .music.no .icon-x22-hasmusic {
            background-position: 0 -1021px;
        }

        .music.no .music-name {
            color: #aaa;
        }

        .music a i {
            width: 22px;
            height: 22px;
            display: inline-block;
            vertical-align: super;
            margin: 4px 0px 4px 5px;
            background-image: url(images/icon-s9fcea044bf.png);
            background-repeat: no-repeat;
        }

        .music a .music-name {
            width: 63px;
            height: 30px;
            display: inline-block;
            overflow: hidden;
            position: relative;
            color: #36b34a;
            white-space: normal;
        }

        .icon-x22-hasmusic {
            background-position: 0 -999px;
        }
    </style>
</head>

<body ng-controller="adminController as admin" ng-cloak>
<input type="hidden" id="codeurl" value="${obj.codeUrl }"><!-- 值为0时，意味着还未选择值 -->
<input type="hidden" id="id" value="${obj.id }"><!-- 值为0时，意味着还未选择值 -->
<input type="hidden" id="imageurlst" value="0"><!-- 值为0时，意味着还未选择值 -->
<input type="hidden" id="htmlmusicurl">
<input type="hidden" id="htmlmusicname">
<input type="hidden" id="addres">
<input type="hidden" id="player_style">
<header class="f-fix g-nav tc">
    <div class="left-wrap fl-l">
        <a href="#" title="点击返回首页" target="_blank" class="logo"></a>
        <a title="添加文本" class="u-toolbtn" ng-click="addNewText()">
            <i class="icon-x1" style="background-image:url(images/icon-s.png)"></i>
            <p>文本</p>
        </a>
        <a title="添加图片" class="u-toolbtn" ng-click="setprovenance('pic')">
            <i class="icon-x1" style="background-image:url(images/icon-c.png)"></i>
            <p>图片</p>
        </a>
        <a id="nav-btn-header" class="u-toolbtn pr">
            <div>
                <i class="icon-x1" style="background-image:url(images/icon-d.png)"></i>
                <p>按钮</p>
            </div>
            <ul class="u-toolBtn-list">
                <li class="u-toolBtn-address" ng-click="addbutton(1)">链接</li>
                <li class="u-toolBtn-phone" ng-click="addbutton(2)">拨打电话</li>
                <li class="u-toolBtn-QQ" ng-click="addbutton(4)">QQ</li>
                <li class="u-toolBtn-layer" ng-click="addbutton(5)">弹出层</li>
                <li class="u-toolBtn-link" ng-click="addbutton(3)">跳转页面</li>
            </ul>
        </a>
        <a title="添加表单" class="u-toolbtn" ng-click="addform()">
            <i class="icon-x1" style="background-image:url(images/icon-d.png)"></i>
            <p>表单</p>
        </a>
        <a title="添加音乐" class="u-toolbtn" ng-click="addmusic()">
            <i class="icon-x1" style="background-image:url(images/icon-d.png)"></i>
            <p>音频</p>
        </a>
        <a title="添加地图" class="u-toolbtn" ng-click="addmap()">
            <i class="icon-x1" style="background-image:url(images/icon-d.png)"></i>
            <p>地图</p>
        </a>
    </div>
    <!--<div class="music">-->
    <!--<a>-->
    <!--<i class="icon-x22-hasmusic"></i>-->
    <!--<div class="music-name">1111.mp3</div>-->
    <!--</a>-->
    <!--</div>-->
    <div class="music">
        <a onclick="music()">
            <i class="icon-x22-hasmusic"></i>
            <div class="music-name">没有背景</div>
        </a>
    </div>
    <div class="right-wrap fl-r" data-ng-controller="htmljsController">
        <a href="javascript:void(0)" class="u-toolBtn" ng-click="save()"><i class="icon-save"></i>保存</a>
        <a href="javascript:void(0)" class="u-toolBtn" ng-click="preview()"><i class="icon-preview"></i>预览</a>
        <!--         <a href="javascipt:void(0)" class="u-toolBtn" onclick="music()"><i class="icon-publish"></i>音乐</a>  -->

        <a href="javascirpt:void(0)" onclick="window.close()" class="u-toolBtn"><i class="icon-publish"></i>返回</a>
    </div>
</header>

<section class="content-wrap" key-draggable style="position: relative;z-index: 50">
    <section class="page-wrap fl-l">
        <div class="page">
            <div class="c-ct" ng-repeat="datapage in dataJson" ng-click="setNavSelectIndex($index)">
                <div class="close icon-x22" ng-click="removepage($index)"></div>
                <div class="copy" data-ng-click="copy_addpage()"></div>
                <div class="up icon-x22"></div>
                <div class="down icon-x22"></div>
                <div class="nav" ng-bind="$index+1"></div>
                <div class="show" style="background:{{dataModuleBg[$index].background}}">
                    <div class="com-preview">
                        <div class="nav-container" ng-repeat="data in datapage"
                             style="width:{{data.attrs.width*scale}}px;height:{{data.attrs.height*scale}}px;top:{{data.attrs.top*scale}}px;left:{{data.attrs.left*scale}}px;z-index:{{data.attrs.zIndex}};">
                            <div style="overflow:hidden;height:100%;border-radius:{{borderCircular(data.attrs.borderRadius)}};opacity:{{data.attrs.opacity/100}};transform:rotate({{data.attrs.rotate}}deg);-webkit-transform:rotate({{data.attrs.rotate}}deg);">
                                <div ng-if="havewhat(data.src)">
                                    <img ng-src="{{data.src}}"
                                         style="width:{{setcutpicw(data.data.imgMaxW,data.data.width,data.attrs.width) * scale}}px;margin-left:{{-setcutpicxy(data.data.marginLeft,data.data.width,data.attrs.width) * scale}}px;margin-top:{{-setcutpicxy(data.data.marginTop,data.data.width,data.attrs.width) * scale}}px;">
                                </div>
                                <div ng-if="havewhat(data.text)" style="height:100%;{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                                    <div ng-bind="data.text"
                                         style="white-space: pre-wrap;font-size:{{data.data.font_size*scale}}px;color:{{data.data.font_color}};line-height:{{data.data.lineHeight}};font-family:{{font_name[data.data.font_name][0]}};text-align: {{data.data.font_align}};font-weight:{{data.data.font_weight}};font-style:{{data.data.font_style}};text-decoration:{{data.data.text_decoration}}"></div>
                                </div>
                                <div class="btn-go" ng-if="havewhat(data.btnType)" style="height:100%;{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                                    <div ng-if="!data.viewType"
                                         style="width: 100%;font-size: {{data.dataText.font_size*scale}}px;color:{{data.dataText.font_color}};text-align: {{data.dataText.font_align}};line-height: 1">
                                        <div ng-bind="data.dataText.value"></div>
                                    </div>
                                    <div ng-if="data.viewType">
                                        <div style="background-image: url({{data.dataPic.src}});"></div>
                                    </div>
                                </div>
                                <div ng-if="havewhat(data.datamusic)" class="preview-container" playertype="{{data.music}}"
                                     style="height:100%;{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}};background-image: url({{data.datamusic.src}});background-size: contain;background-repeat: no-repeat;background-position: 50% 50%;">
                                    <div></div>
                                </div>
                                <div class="preview-container form" ng-if="havewhat(data.dataform)" style="{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                                    <div style="padding: 20% 0 20% 0">
                                        <div class="form-box" data-ng-repeat="input in data.dataform.input.data">
                                            <input type="text" data-ng-if="input[0] != 5" placeholder="{{input[1]}}"
                                                   style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                                            <textarea data-ng-if="input[0] == 5" placeholder="{{input[1]}}"
                                                      style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}"></textarea>
                                        </div>
                                        <div class="form-box submit">
                                            <input type="submit" value="{{data.dataform.submit.val}}"
                                                   style="border-radius:{{borderCircular(data.dataform.submit.borderRadius)}};border-width: {{data.dataform.submit.border}}px;{{showbgcolor(data.dataform.submit.bgcolor,data.dataform.submit.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.submit.borcolor,data.dataform.submit.boropacity,'border-color:')}}">
                                        </div>
                                    </div>
                                </div>
                                <div class="preview-container map" ng-if="havewhat(data.dataMap)">
                                    <div class="pin"></div>
                                    <div class="pulse">
                                        <div class="after"></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="add-page-btn" ng-click="addpage()">
            <span class="plus">+</span>
            <div class="text">增加一页</div>
        </div>

        <div class="transverse-page" ng-controller="transverseController">
            <div class="transverse-box" ng-repeat="dataArray in dataTransverse" transverse-draggable>
                <div class="transverse-box-m" ng-show="dataArray.dataTransverse_on">
                    <div class="swiper-container">
                        <div class="swiper-wrapper">
                            <div class="swiper-slide" ng-repeat="dataPage in dataArray.data" ng-click="setJson($parent.$index,$index)">
                                <div class="close icon-x22" data-ng-click="remove_transverse_page($index)"></div>
                                <div class="show" style="background:{{dataArray.bg[$index].background}}">
                                    <div class="com-preview">
                                        <div class="nav-container" ng-repeat="data in dataPage"
                                             style="width:{{data.attrs.width*scale}}px;height:{{data.attrs.height*scale}}px;top:{{data.attrs.top*scale}}px;left:{{data.attrs.left*scale}}px;z-index:{{data.attrs.zIndex}};">
                                            <div style="overflow:hidden;height:100%;border-radius:{{borderCircular(data.attrs.borderRadius)}};opacity:{{data.attrs.opacity/100}};transform:rotate({{data.attrs.rotate}}deg);-webkit-transform:rotate({{data.attrs.rotate}}deg);">
                                                <div ng-if="havewhat(data.src)">
                                                    <img ng-src="{{data.src}}"
                                                         style="width:{{setcutpicw(data.data.imgMaxW,data.data.width,data.attrs.width) * scale}}px;margin-left:{{-setcutpicxy(data.data.marginLeft,data.data.width,data.attrs.width) * scale}}px;margin-top:{{-setcutpicxy(data.data.marginTop,data.data.width,data.attrs.width) * scale}}px;">
                                                </div>
                                                <div ng-if="havewhat(data.text)" style="height:100%;{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                                                    <div ng-bind="data.text"
                                                         style="white-space: pre-wrap;font-size:{{data.data.font_size*scale}}px;color:{{data.data.font_color}};line-height:{{data.data.lineHeight}};font-family:{{font_name[data.data.font_name][0]}};text-align: {{data.data.font_align}};font-weight:{{data.data.font_weight}};font-style:{{data.data.font_style}};text-decoration:{{data.data.text_decoration}}"></div>
                                                </div>
                                                <div class="btn-go" ng-if="havewhat(data.btnType)"
                                                     style="height:100%;{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                                                    <div ng-if="!data.viewType"
                                                         style="width: 100%;font-size: {{data.dataText.font_size*scale}}px;color:{{data.dataText.font_color}};text-align: {{data.dataText.font_align}};line-height: 1">
                                                        <div ng-bind="data.dataText.value"></div>
                                                    </div>
                                                    <div ng-if="data.viewType">
                                                        <div style="background-image: url({{data.dataPic.src}});"></div>
                                                    </div>
                                                </div>
                                                <div ng-if="havewhat(data.datamusic)" class="preview-container" playertype="{{data.music}}"
                                                     style="height:100%;{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}};background-image: url({{data.datamusic.src}});background-size: contain;background-repeat: no-repeat;background-position: 50% 50%;">
                                                    <div></div>
                                                </div>
                                                <div class="preview-container form" ng-if="havewhat(data.dataform)"
                                                     style="{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                                                    <div style="padding: 20% 0 20% 0">
                                                        <div class="form-box" data-ng-repeat="input in data.dataform.input.data">
                                                            <input type="text" data-ng-if="input[0] != 5" placeholder="{{input[1]}}"
                                                                   style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                                                            <textarea data-ng-if="input[0] == 5" placeholder="{{input[1]}}"
                                                                      style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}"></textarea>
                                                        </div>
                                                        <div class="form-box submit">
                                                            <input type="submit" value="{{data.dataform.submit.val}}"
                                                                   style="border-radius:{{borderCircular(data.dataform.submit.borderRadius)}};border-width: {{data.dataform.submit.border}}px;{{showbgcolor(data.dataform.submit.bgcolor,data.dataform.submit.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.submit.borcolor,data.dataform.submit.boropacity,'border-color:')}}">
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="preview-container map" ng-if="havewhat(data.dataMap)">
                                                    <div class="pin"></div>
                                                    <div class="pulse">
                                                        <div class="after"></div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="tp-add none" data-ng-click="add_transverse_page($index)">+</div>
                </div>
            </div>
        </div>

    </section>

    <section class="main-wrap flip" ng-click="showBgEdit()">
        <div id="editorFrame" ng-click="showBgEdit()"
             style="{{scalestyle}};animation:{{animateturnThePpage[dataModuleBg[NAV_SELECT_index].bgdata.slide][0]}}" scale-draggable>
            <ul class="u-Layer-update-16">
                <li ng-class={'disable':moveLayerClass('back')} ng-click="moveLayer(0,$event)"><span></span></li>
                <li ng-class={'disable':moveLayerClass('back')} ng-click="moveLayer(1,$event)"><span></span></li>
                <li ng-class={'disable':moveLayerClass('next')} ng-click="moveLayer(2,$event)"><span></span></li>
                <li ng-class={'disable':moveLayerClass('next')} ng-click="moveLayer(3,$event)"><span></span></li>
                <li ng-class={'disable':showBg} ng-click="removeLayer($event)"><span></span></li>
            </ul>
            <div class="transverse-dom"
                 style="width:{{dataTransverse[NAV_SELECT_index].attr.width*scale}}px;height:{{dataTransverse[NAV_SELECT_index].attr.height*scale}}px;top:{{dataTransverse[NAV_SELECT_index].attr.top*scale}}px;left:{{dataTransverse[NAV_SELECT_index].attr.left*scale}}px;background: {{dataTransverse[NAV_SELECT_index].bg[dataTransverse_index].background}}"
                 ng-class={"ontrans":!horizontal_page} dataattrs="dataTransverse[NAV_SELECT_index].attr" ts-draggable
                 ng-if="dataTransverse[NAV_SELECT_index].dataTransverse_on"></div>

            <div class="editorFrame-main box-blur" style="background:{{dataModuleBg[NAV_SELECT_index].background}};" ng-if="dataTransverse[NAV_SELECT_index].dataTransverse_on"
                 ng-class={"ontrans":horizontal_page}>
                <div ng-repeat="data in dataJson[NAV_SELECT_index]" class="box-controller"
                     style="width:{{data.attrs.width*scale}}px;height:{{data.attrs.height*scale}}px;top:{{data.attrs.top*scale}}px;left:{{data.attrs.left*scale}}px;z-index:{{data.attrs.zIndex}};transform:rotate({{data.attrs.rotate}}deg);-webkit-transform:rotate({{data.attrs.rotate}}deg);"
                     index="{{$index}}" data-stratanimate="{{data.stratanimate}}" data-endanimate="{{data.endanimate}}">
                    <div class="autoWh"
                         style="border-radius:{{borderCircular(data.attrs.borderRadius)}};opacity:{{data.attrs.opacity/100}};{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                        <div class="preview-container" ng-if="havewhat(data.src)">
                            <div class="preview-container-box" style="font-size:0px;height:100%;">
                                <img ng-src="{{data.src}}"
                                     style="width:{{setcutpicw(data.data.imgMaxW,data.data.width,data.attrs.width) * scale}}px;margin-left:{{-setcutpicxy(data.data.marginLeft,data.data.width,data.attrs.width) * scale}}px;margin-top:{{-setcutpicxy(data.data.marginTop,data.data.width,data.attrs.width) * scale}}px;"/>
                            </div>
                        </div>
                        <div class="preview-container" ng-if="havewhat(data.data.font_align)">
                            <div class="textbox preview-container-box"
                                 style="font-size:{{data.data.font_size*scale}}px;color:{{data.data.font_color}};line-height:{{data.data.lineHeight}};font-family:{{font_name[data.data.font_name][0]}};text-align: {{data.data.font_align}};font-weight:{{data.data.font_weight}};font-style:{{data.data.font_style}};text-decoration:{{data.data.text_decoration}}">
                                {{hasText(data.text)}}
                            </div>
                        </div>
                        <div class="preview-container btn-go" ng-if="havewhat(data.btnType)">
                            <div class="f-fix" ng-if="!data.viewType"
                                 style="width: 100%;font-size: {{data.dataText.font_size*scale}}px;font-weight: {{data.dataText.font_weight}};font-style: {{data.dataText.font_style}};text-decoration: {{data.dataText.text_decoration}};color:{{data.dataText.font_color}};text-align: {{data.dataText.font_align}};line-height: 1">
                                <div ng-bind="data.dataText.value"></div>
                            </div>
                            <div class="btn-icon" ng-if="data.viewType">
                                <div style="background-image: url({{data.dataPic.src}});"></div>
                            </div>
                        </div>
                        <div class="preview-container btn-go" ng-if="havewhat(data.datamusic)" playertype="{{data.music}}">
                            <div class="btn-icon">
                                <div style="background-image: url({{data.datamusic.src}});"></div>
                            </div>
                        </div>
                        <div class="preview-container form" ng-if="havewhat(data.dataform)">
                            <div style="padding: 20% 0 20% 0">
                                <div class="form-box" data-ng-repeat="input in data.dataform.input.data">
                                    <input type="text" data-ng-if="input[0] != 5" placeholder="{{input[1]}}"
                                           style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                                    <textarea data-ng-if="input[0] == 5" placeholder="{{input[1]}}"
                                              style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}"></textarea>
                                </div>
                                <div class="form-box submit">
                                    <input type="submit" value="{{data.dataform.submit.val}}"
                                           style="border-radius:{{borderCircular(data.dataform.submit.borderRadius)}};border-width: {{data.dataform.submit.border}}px;{{showbgcolor(data.dataform.submit.bgcolor,data.dataform.submit.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.submit.borcolor,data.dataform.submit.boropacity,'border-color:')}}">
                                </div>
                            </div>
                        </div>
                        <div class="preview-container map" ng-if="havewhat(data.dataMap)">
                            <div class="pin"></div>
                            <div class="pulse">
                                <div class="after"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="dashboard" class="editorFrame-main" style="background:{{choosebg()}};">
                <div ng-if="dataModuleBg[NAV_SELECT_index].bgswiper" id="bgswiper" bgswiper-draggable>
                    <div style="background-image: url('{{src}}')" ng-repeat="src in dataModuleBg[NAV_SELECT_index].bgswiperpics track by $index"></div>
                </div>
                <div ng-repeat="data in NAV_SELECT" class="box-controller"
                     style="width:{{data.attrs.width*scale}}px;height:{{data.attrs.height*scale}}px;top:{{data.attrs.top*scale}}px;left:{{data.attrs.left*scale}}px;z-index:{{data.attrs.zIndex}};transform:rotate({{data.attrs.rotate}}deg);-webkit-transform:rotate({{data.attrs.rotate}}deg);"
                     my-draggable index="{{$index}}" data-stratanimate="{{data.stratanimate}}" data-endanimate="{{data.endanimate}}">
                    <div class="autoWh"
                         style="border-radius:{{borderCircular(data.attrs.borderRadius)}};opacity:{{data.attrs.opacity/100}};{{showbgcolor(data.bgcolor,data.bgopacity,'background-color:')}}">
                        <div class="preview-container" ng-if="havewhat(data.src)" ng-click="showImgEdit($event,$index)">
                            <div class="preview-container-box" style="font-size:0px;height:100%;">
                                <img ng-src="{{data.src}}"
                                     style="width:{{setcutpicw(data.data.imgMaxW,data.data.width,data.attrs.width) * scale}}px;margin-left:{{-setcutpicxy(data.data.marginLeft,data.data.width,data.attrs.width) * scale}}px;margin-top:{{-setcutpicxy(data.data.marginTop,data.data.width,data.attrs.width) * scale}}px;"/>
                            </div>
                        </div>
                        <div class="preview-container" ng-if="havewhat(data.data.font_align)" ng-click="showTextEdit($event,$index)">
                            <div class="textbox preview-container-box"
                                 style="font-size:{{data.data.font_size*scale}}px;color:{{data.data.font_color}};line-height:{{data.data.lineHeight}};font-family:{{font_name[data.data.font_name][0]}};text-align: {{data.data.font_align}};font-weight:{{data.data.font_weight}};font-style:{{data.data.font_style}};text-decoration:{{data.data.text_decoration}}">
                                {{hasText(data.text)}}
                            </div>
                        </div>
                        <div class="preview-container btn-go" ng-if="havewhat(data.btnType)" ng-click="showbtngoEdit($event,$index)">
                            <div class="f-fix" ng-if="!data.viewType"
                                 style="width: 100%;font-size: {{data.dataText.font_size*scale}}px;font-weight: {{data.dataText.font_weight}};font-style: {{data.dataText.font_style}};text-decoration: {{data.dataText.text_decoration}};color:{{data.dataText.font_color}};text-align: {{data.dataText.font_align}};line-height: 1">
                                <div ng-bind="data.dataText.value"></div>
                            </div>
                            <div class="btn-icon" ng-if="data.viewType">
                                <div style="background-image: url({{data.dataPic.src}});"></div>
                            </div>
                        </div>
                        <div class="preview-container btn-go" ng-if="havewhat(data.datamusic)" ng-click="showmusicEdit($event,$index)" playertype="{{data.music}}">
                            <div class="btn-icon">
                                <div style="background-image: url({{data.datamusic.src}});"></div>
                            </div>
                        </div>
                        <div class="preview-container form" ng-if="havewhat(data.dataform)" ng-click="showformEdit($event,$index)">
                            <div style="padding: 20% 0 20% 0">
                                <div class="form-box" data-ng-repeat="input in data.dataform.input.data">
                                    <input type="text" data-ng-if="input[0] != 5" placeholder="{{input[1]}}"
                                           style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}">
                                    <textarea data-ng-if="input[0] == 5" placeholder="{{input[1]}}"
                                              style="border-radius:{{borderCircular(data.dataform.input.borderRadius)}};border-width: {{data.dataform.input.border}}px;{{showbgcolor(data.dataform.input.bgcolor,data.dataform.input.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.input.borcolor,data.dataform.input.boropacity,'border-color:')}}"></textarea>
                                </div>
                                <div class="form-box submit">
                                    <input type="submit" value="{{data.dataform.submit.val}}"
                                           style="border-radius:{{borderCircular(data.dataform.submit.borderRadius)}};border-width: {{data.dataform.submit.border}}px;{{showbgcolor(data.dataform.submit.bgcolor,data.dataform.submit.bgopacity,'background-color:')}};{{showbgcolor(data.dataform.submit.borcolor,data.dataform.submit.boropacity,'border-color:')}}">
                                </div>
                            </div>
                        </div>
                        <div class="preview-container map" ng-if="havewhat(data.dataMap)" ng-click="showmapEdit($event,$index)">
                            <div class="pin"></div>
                            <div class="pulse">
                                <div class="after"></div>
                            </div>
                        </div>
                    </div>
                    <!--
                    <div class="tl-c"></div>
                    <div class="tr-c"></div>
                    <div class="bl-c"></div>
                    <div class="br-c"></div>
                    -->
                </div>
            </div>
        </div>
    </section>

    <section class="g-config fl-r" ng-click="init_extend()">
        <div class="config-wrap">
            <!-- 背景 -->
            <section class="b-config f-fix" ng-show="showBg">

                <div class="c-background-pop">
                    <div class="b-background-btn" ng-click="setprovenance('bg')">添加背景</div>
                    <div class="color-list f-fix m-t-21 m-l-18">
                        <ul>
                            <li class="nonecolor fl-l" ng-click="editBgColor('none')"></li>
                            <li class="m-l-7 fl-l" ng-click="editBgColor('#ffffff')"
                                style="background-color: rgb(255, 255, 255);"></li>
                            <li class="m-l-7 fl-l" ng-click="editBgColor('#999999')"
                                style="background-color: rgb(153, 153, 153);"></li>
                            <li class="m-l-7 fl-l" ng-click="editBgColor('#000000')"
                                style="background-color: rgb(0, 0, 0);"></li>
                            <li class="m-l-7 fl-l" ng-click="editBgColor('#7ed321')"
                                style="background-color: rgb(126, 211, 33);"></li>
                            <li class="m-l-7 fl-l" ng-click="editBgColor('#50e3c2')"
                                style="background-color: rgb(80, 227, 194);"></li>
                            <li class="m-l-7 fl-l" ng-click="editBgColor('#f8e71c')"
                                style="background-color: rgb(248, 231, 28);"></li>
                            <li class="colorpick m-l-7 fl-l" ng-click="showHideBgColorBoard($event)"></li>
                        </ul>
                    </div>
                </div>

                <section class="b-config-section">

                    <!--
                    <div class="b-config-icon">
                        <label>翻页图标</label>
                        <select class="slide-page-select">
                            <option>UP</option>
                            <option>Down</option>
                        </select>
                    </div>

                    <hr>
                    -->

                    <div class="b-config-panel">
                        <h2>翻页动画</h2>
                        <div class="f-fix">
                            <ul class="u-chooseList-large f-fix">
                                <li class="fl-l" ng-repeat="data in turnThePpage">
                                    <a class="a-img-warp {{getturnThePpage(data[0],$index)}}" data-ng-click="setturnThePpage($index)"></a>
                                    <p>{{data[1]}}</p>
                                </li>
                            </ul>
                        </div>
                    </div>

                    <div class="b-config-row m-t-30">
                        <div class="f-fix">
                            <label>应用到所有页面</label>
                            <div class="allfy fl-r icon-off" data-ng-click="setAllTurnThePpage($event)"></div>
                        </div>

                        <hr class=" m-t-20">

                        <div class="f-fix m-t-20">
                            <label>锁定翻页</label>
                            <div class="fl-r icon-off" data-ng-class={"icon-on":dataModuleBg[NAV_SELECT_index].bgdata.lockslide} data-ng-click="setlockslide()"></div>
                        </div>

                        <hr class="m-t-20">

                        <div class="f-fix m-t-20">
                            <label>横向翻页</label>
                            <div class="fl-r icon-off" data-ng-class={"icon-on":dataTransverse[NAV_SELECT_index].dataTransverse_on} data-ng-click="set_dataTransverse_on()"></div>
                        </div>

                        <div ng-if="!horizontal_page">
                            <hr class="m-t-20">

                            <div class="f-fix m-t-20">
                                <label>多图变换</label>
                                <div class="fl-r icon-off" data-ng-class={"icon-on":dataModuleBg[NAV_SELECT_index].bgswiper}
                                     data-ng-click="dataModuleBg[NAV_SELECT_index].bgswiper=!dataModuleBg[NAV_SELECT_index].bgswiper"></div>
                            </div>

                            <div class="swiper-btn  m-t-20" ng-show="dataModuleBg[NAV_SELECT_index].bgswiper">
                                <ul>
                                    <li style="background-image: url('{{pic}}')" ng-repeat="pic in dataModuleBg[NAV_SELECT_index].bgswiperpics track by $index">
                                        <i class="remove" data-ng-click="removebgswiperpic($index)"></i>
                                        <i class="left-move" data-ng-click="setsortingbgswiperpic($index,$index-1)"></i>
                                        <i class="right-move" data-ng-click="setsortingbgswiperpic($index,$index+1)"></i>
                                    </li>
                                    <li class="addswiper-icon" data-ng-click="setprovenance('pics')">+</li>
                                </ul>
                                <input type="hidden" class="swiper-bg-btn-tip" ng-click="addbgswiperpic($event)"/>
                            </div>


                        </div>

                        <!--<div class="f-fix m-t-20">-->
                        <!--<label>自动翻页</label>-->
                        <!--<div class="fl-r icon-off"></div>-->
                        <!--</div>-->
                    </div>

                </section>

            </section>
            <!-- end背景 -->

            <!-- 元素 -->
            <section class="c-config" ng-show="!showBg">
                <!--文本-->
                <div class="c-text bg-f8" data-ng-if="showText">
                    <h1 style="color:#444"><img src="images/singletext.png"/>文本</h1>
                    <div class="text">
                        <div class="text-config">
                            <textarea id="textBox" ng-bind="MAIN_SELECT.text" ng-keyup="countHeight(true)" placeholder="亲，请在这里输入文字哦！"></textarea>
                        </div>
                        <div class="c-conf-row f-fix">
                            <ul class="u-tab">
                                <li class="dropdown" ng-click="showDropText(0,$event)">
                                    <a class="icon-x16 icon-font-size"></a>
                                </li>
                                <li ng-click="showHideTextColorBoard($event)">
                                    <a class="icon-x16 icon-font-color"></a>
                                </li>
                                <li class="dropDown" ng-click="showDropText(1,$event)">
                                    <a class="icon-x16 icon-font-center"></a>
                                </li>
                                <li class="dropDown" ng-click="showDropText(2,$event)">
                                    <a class="icon-x16 icon-font-line"></a>
                                </li>
                            </ul>

                            <ul class="dropdown-list text-size" ng-show="textSize">
                                <li ng-class={'selected':init_font(data)} ng-repeat="data in font_size"
                                    ng-click="setFontSize(data)">{{data}}
                                </li>
                            </ul>

                            <ul class="dropdown-list text-align" ng-show="textAlign">
                                <li ng-class={"selected":init_font("left")} ng-click="setFontAlign('left')"><i class="icon-x16 icon-left"></i></li>
                                <li ng-class={"selected":init_font("center")} ng-click="setFontAlign('center')"><i class="icon-x16 icon-center"></i></li>
                                <li ng-class={"selected":init_font("right")} ng-click="setFontAlign('right')"><i class="icon-x16 icon-right"></i></li>
                            </ul>

                            <ul class="dropdown-list text-line" ng-show="textLine">
                                <li ng-class={'selected':init_font_line(data)} ng-repeat="data in font_line"
                                    ng-click="setFontLine(data)">{{data}}
                                </li>
                            </ul>


                        </div>
                        <div style="height:10px;"></div>
                        <div class="c-conf-row f-fix">
                            <ul class="f-tab" ng-click="showDropText(3,$event)">
                                <li style="line-height: 24px;font-family: {{font_name[MAIN_SELECT.data.font_name][0]}};">
                                    {{font_name[MAIN_SELECT.data.font_name][1]}}
                                </li>
                            </ul>
                            <ul class="i-tab fl-l" style="margin-left:3px;">
                                <li ng-click="set_f_bold()" ng-class={"press":init_bold}><i
                                        class="icon-x16 icon-bold"></i></li>
                                <li ng-click="set_f_italic()" ng-class={"press":init_italic}><i
                                        class="icon-x16 icon-italic"></i></li>
                                <li ng-click="set_f_decoration()" ng-class={"press":init_decoration}><i
                                        class="icon-x16 icon-decoration"></i></li>
                            </ul>
                            <ul class="dropdown-list text-font-name" ng-show="textName">
                                <li ng-class={'selected':init_font($index)} ng-repeat="data in font_name"
                                    ng-click="setFontName($index)">{{data[1]}}
                                </li>
                            </ul>
                        </div>

                    </div>
                </div>
                <!-- end文本 -->

                <!-- 图片 -->
                <div class="c-img bg-f8" data-ng-if="showPic">
                    <h1 style="color:#444"><img src="images/singleimage.png"/>图片</h1>
                    <div class="img">
                        <div class="jcrop-panel-header" ng-click="setprovenance('img')">更换图片</div>
                        <div class="img-config" datawidth="MAIN_SELECT.data.width" dataheight="MAIN_SELECT.data.height"
                             datatop="MAIN_SELECT.data.marginTop" dataleft="MAIN_SELECT.data.marginLeft"
                             datasrc="{{MAIN_SELECT.src}}" dataratio="dataratio" datamainw="MAIN_SELECT.attrs.width"
                             datamainh="MAIN_SELECT.attrs.height" dataimgmaxw="MAIN_SELECT.data.imgMaxW"
                             cutpic-directive>
                        </div>

                        <div class="u-tab f-fix m-t-20">
                            <ul>
                                <li data-ng-click="setratio(0)" data-ng-class={"curr":dataratioclass(0)}>自由</li>
                                <li data-ng-click="setratio(1)" data-ng-class={"curr":dataratioclass(1)}>正方形</li>
                                <li data-ng-click="setratio(2)" data-ng-class={"curr":dataratioclass(2)}>4:3</li>
                                <li data-ng-click="setratio(3)" data-ng-class={"curr":dataratioclass(3)}>16:9</li>
                                <li>铺满</li>
                            </ul>
                        </div>

                        <div class="f-fix m-t-20" style="margin: 0 20px;">
                            <label>识别二维码</label>
                            <div class="fl-r icon-off" data-ng-class={"icon-on":MAIN_SELECT.discern} data-ng-click="setdiscern()" style="margin-right: 90px;"></div>
                        </div>

                    </div>
                </div>
                <!-- end图片 -->

                <!-- 按钮  -->
                <div class="c-btn bg-f8" data-ng-if="showbtngo" data-ng-controller="bottongoController" bottongo-directive ng-cloak></div>
                <!-- end按钮 -->

                <!-- 音频  -->
                <div class="c-btn bg-f8" data-ng-if="showmusic" data-ng-controller="musicController" music-directive ng-cloak></div>
                <!-- end音频 -->

                <!-- 表单 -->
                <div class="c-form" data-ng-if="showform" data-ng-controller="formController" form-directive ng-cloak></div>
                <!-- end表单 -->

                <!-- 地图 -->
                <div class="c-form" data-ng-if="showMap" data-ng-controller="mapController" map-directive ng-cloak></div>
                <!-- end地图 -->

                <!-- 样式动画 -->
                <section class="c-config-class-animate">
                    <section class="nav f-fix">
                        <ul>
                            <li data-ng-class={"c-active":animateclass} style="border-left:0px;"
                                data-ng-click="setanimateclass(false)">样式
                            </li>
                            <li data-ng-class={"c-active":!animateclass} data-ng-click="setanimateclass(true)">
                                动画
                            </li>
                        </ul>
                    </section>

                    <!-- 动画 -->
                    <div class="c-config-animation bg-f8" data-ng-hide="animateclass">
                        <section class="f-fix">
                            <ul class="z-singleLine">
                                <li data-ng-click="setanimateway(false,$event)"><a data-ng-class={'z-index':animateway}
                                                                                   href="javascript:void(0);">入场动画</a>
                                </li>
                                <li data-ng-click="setanimateway(true,$event)"><a data-ng-class={'z-index':!animateway}
                                                                                  href="javascript:void(0);">出场动画</a>
                                </li>
                            </ul>
                        </section>
                        <!-- 入场动画 -->
                        <div class="c-conf-animeSection" data-ng-show="animateway">
                            <div data-ng-hide="hasanimate()">
                                <div class="f-fix">
                                    <a class="link-chooseAnime" data-ng-click="setanimateon($event)">
                                        <span class="u-image-wrap">
                                            <span class="u-image-large undefined"></span>
                                        </span>
                                    </a>
                                    <div class="fl-r u-a-handle">
                                        <span>没有入场动画</span>
                                        <a class="u-btn u-btn-large" data-ng-click="setanimateon($event)">添加动画</a>
                                    </div>
                                </div>
                            </div>
                            <div data-ng-show="hasanimate()">
                                <div class="f-fix">
                                    <a class="link-chooseAnime" data-ng-click="setanimateon($event)">
                                        <span class="u-image-wrap">
                                            <span class="u-image-large {{animatejsonon[animate_name[0]][1]}}"></span>
                                        </span>
                                    </a>
                                    <div class="fl-r u-a-handle">
                                        <label class="">{{animatejsonon[animate_name[0]][0]}}</label>
                                        <a class="icon-toggle" data-ng-click="setanimateon($event)"></a>
                                        <a class="icon-play"></a>
                                        <a class="icon-remove" title="删除动画" data-ng-click="removeanimate()"></a>
                                    </div>
                                </div>

                                <hr>

                                <div class="f-fix m-b-20">
                                    <ul class="u-chooseList-small">
                                        <li data-ng-repeat="data in animatejsonon[animate_name[0]][2]"
                                            data-ng-click="setanimatenamechild($index)">
                                            <a class="u-image-wrap">
                                                <span class="u-image-small anime-in" data-animate="{{data[1]}}"></span>
                                                <i class="icon-x22-tick-circle icon-x22"
                                                   data-ng-if="$index == animate_name[1]"></i>
                                            </a>
                                            <p>{{data[0]}}</p>
                                        </li>
                                    </ul>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>延迟时间</label>
                                        <div class="fl-r">
                                            <div class="u-slider">
                                                <i my-plan style="left:{{MAIN_SELECT.stratanimate[2]*5}}%" datamodel="MAIN_SELECT.stratanimate[2]" datarate="5"></i>
                                            </div>
                                            <input type="text" data-ng-model="MAIN_SELECT.stratanimate[2]"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>持续时间</label>
                                        <div class="fl-r">
                                            <div class="u-slider">
                                                <i my-plan style="left:{{MAIN_SELECT.stratanimate[3]*3.3333}}%" datamodel="MAIN_SELECT.stratanimate[3]" datarate="3.3333"></i>
                                            </div>
                                            <input type="text" data-ng-model="MAIN_SELECT.stratanimate[3]"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>执行次数</label>
                                        <div class="fl-r">
                                            <div class="u-slider">
                                                <i my-plan style="left:{{MAIN_SELECT.stratanimate[4]*10}}%" datamodel="MAIN_SELECT.stratanimate[4]" datarate="10"
                                                   dataint="true"></i>
                                            </div>
                                            <input type="text" data-ng-model="MAIN_SELECT.stratanimate[4]"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>无限循环</label>
                                        <i class="icon-checkbox" data-ng-class={true:"on",false:"off"}[getanimatefor()]
                                           data-ng-click="setanimatefor()"></i>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>动画说明</label>
                                        <div class="icon-help">
                                            <div class="help-pop">
                                                <p>1、【入场延迟时间】等待多少秒后播放动画的时间</p>
                                                <p>2、【出场延迟时间】入场动画播放完成后等待多少秒执行动画的时间</p>
                                                <p>3、【持续时间】动画播放的时间，值越大，动画越慢</p>
                                                <p>4、【执行次数】动画播放的次数</p>
                                                <br/>
                                                <p>入场动画时间 = 入场延迟时间 + 持续时间 * 执行次数</p>
                                                <p>出场动画时间 = 出场延迟时间 + 持续时间 * 执行次数</p>
                                                <p>动画总时间 = 出场动画时间 + 入场动画时间</p>
                                                <p>如果想动画A完成立即播放动画B，则动画B入场延迟时间等于动画A的总时间</p>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                            </div>
                        </div>
                        <!-- end入场动画 -->

                        <!-- 出场动画 -->
                        <div data-ng-show="!animateway">
                            <div data-ng-hide="hasendanimate()">
                                <div class="f-fix">
                                    <a class="link-chooseAnime" data-ng-click="setanimateout($event)">
                                        <span class="u-image-wrap">
                                            <span class="u-image-large undefined"></span>
                                        </span>
                                    </a>
                                    <div class="fl-r u-a-handle">
                                        <span>没有出场动画</span>
                                        <a class="u-btn u-btn-large" data-ng-click="setanimateout($event)">添加动画</a>
                                    </div>
                                </div>
                            </div>
                            <div data-ng-show="hasendanimate()">
                                <div class="f-fix">
                                    <a class="link-chooseAnime" data-ng-click="setanimateout($event)">
                                        <span class="u-image-wrap">
                                            <span class="u-image-large {{animatejsonout[end_animate_name[0]][1]}}"></span>
                                        </span>
                                    </a>
                                    <div class="fl-r u-a-handle">
                                        <label class="">{{animatejsonout[end_animate_name[0]][0]}}</label>
                                        <a class="icon-toggle" data-ng-click="setanimateout($event)"></a>
                                        <a class="icon-play"></a>
                                        <a class="icon-remove" title="删除动画" data-ng-click="removeendanimate()"></a>
                                    </div>
                                </div>

                                <hr>

                                <div class="f-fix m-b-20">
                                    <ul class="u-chooseList-small">
                                        <li data-ng-repeat="data in animatejsonout[end_animate_name[0]][2]"
                                            data-ng-click="setendanimatenamechild($index)">
                                            <a class="u-image-wrap">
                                                <span class="u-image-small anime-in" data-animate="{{data[1]}}"></span>
                                                <i class="icon-x22-tick-circle icon-x22"
                                                   data-ng-if="$index == end_animate_name[1]"></i>
                                            </a>
                                            <p>{{data[0]}}</p>
                                        </li>
                                    </ul>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>延迟时间</label>
                                        <div class="fl-r">
                                            <div class="u-slider">
                                                <i my-plan style="left:{{MAIN_SELECT.endanimate[2]*5}}%" datamodel="MAIN_SELECT.endanimate[2]" datarate="5"></i>
                                            </div>
                                            <input type="text" data-ng-model="MAIN_SELECT.endanimate[2]"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>持续时间</label>
                                        <div class="fl-r">
                                            <div class="u-slider">
                                                <i my-plan style="left:{{MAIN_SELECT.endanimate[3]*3.3333}}%" datamodel="MAIN_SELECT.endanimate[3]" datarate="3.3333"></i>
                                            </div>
                                            <input type="text" data-ng-model="MAIN_SELECT.endanimate[3]"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>执行次数</label>
                                        <div class="fl-r">
                                            <div class="u-slider">
                                                <i my-plan style="left:{{MAIN_SELECT.endanimate[4]*10}}%" datamodel="MAIN_SELECT.endanimate[4]" datarate="10" dataint="true"></i>
                                            </div>
                                            <input type="text" data-ng-model="MAIN_SELECT.endanimate[4]"/>
                                        </div>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>无限循环</label>
                                        <i class="icon-checkbox"
                                           data-ng-class={true:"on",false:"off"}[getendanimatefor()]
                                           data-ng-click="setendanimatefor()"></i>
                                    </div>
                                </div>

                                <div class="c-config-chil">
                                    <div class="c-config-row">
                                        <label>动画说明</label>
                                        <div class="icon-help">
                                            <div class="help-pop">
                                                <p>1、【入场延迟时间】等待多少秒后播放动画的时间</p>
                                                <p>2、【出场延迟时间】入场动画播放完成后等待多少秒执行动画的时间</p>
                                                <p>3、【持续时间】动画播放的时间，值越大，动画越慢</p>
                                                <p>4、【执行次数】动画播放的次数</p>
                                                <br/>
                                                <p>入场动画时间 = 入场延迟时间 + 持续时间 * 执行次数</p>
                                                <p>出场动画时间 = 出场延迟时间 + 持续时间 * 执行次数</p>
                                                <p>动画总时间 = 出场动画时间 + 入场动画时间</p>
                                                <p>如果想动画A完成立即播放动画B，则动画B入场延迟时间等于动画A的总时间</p>
                                            </div>
                                        </div>

                                    </div>
                                </div>

                            </div>
                        </div>
                        <!-- end出场动画 -->

                    </div>
                    <!-- end动画 -->

                    <!-- 样式 -->
                    <div class="c-config-class">
                        <div class="bg-f8 p-20" data-ng-show="animateclass">
                            <div class="c-config-chil">
                                <div class="c-config-row">
                                    <label>背景</label>
                                    <div class="fl-r" style="line-height: 0">
                                        <div class="u-colorpicker" data-ng-click="showHidemoduleBoard($event)">
                                            <a class="bg-color" style="{{showbgcolorex(MAIN_SELECT.bgcolor)}}"></a>
                                            <a class="small"><i class="icon-x20 icon-x20-color"></i></a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!--
                            <div class="c-config-chil">
                                <div class="c-config-row">
                                    <label>边框</label>
                                    <div class="fl-r"></div>
                                </div>
                            </div>
                            -->
                            <div class="c-config-chil">
                                <div class="c-config-row">
                                    <label>圆角</label>
                                    <div class="fl-r">
                                        <div class="u-slider">
                                            <i my-plan style="left:{{MAIN_SELECT.attrs.borderRadius}}%" datamodel="MAIN_SELECT.attrs.borderRadius" datarate="1" dataint="true"></i>
                                        </div>
                                        <input type="text" ng-model="MAIN_SELECT.attrs.borderRadius"/>
                                    </div>
                                </div>
                            </div>
                            <div class="c-config-chil">
                                <div class="c-config-row">
                                    <label>透明</label>
                                    <div class="fl-r">
                                        <div class="u-slider">
                                            <i my-plan style="left: {{MAIN_SELECT.attrs.opacity}}%;" datamodel="MAIN_SELECT.attrs.opacity" datarate="1" dataint="true"></i>
                                        </div>
                                        <input type="text" ng-model="MAIN_SELECT.attrs.opacity"/>
                                    </div>
                                </div>
                            </div>
                            <div class="c-config-chil">
                                <div class="c-config-row" style="margin-bottom:0px">
                                    <label>旋转</label>
                                    <div class="fl-r">
                                        <div class="u-slider">
                                            <i my-plan style="left:{{MAIN_SELECT.attrs.rotate*0.2777777777777778}}%;" datamodel="MAIN_SELECT.attrs.rotate"
                                               datarate="0.2777777777777778" dataint="true"></i>
                                        </div>
                                        <input type="text" ng-model="MAIN_SELECT.attrs.rotate"/>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="c-config-coord">
                            <div class="f-fix m-b-20">
                                <label class="line-h-29">位置</label>
                                <div class="fl-r">
                                    <label class="c-label">X轴</label>
                                    <input type="text" class="m-r-35" ng-model="MAIN_SELECT.attrs.left"/>
                                    <label class="c-label">Y轴</label>
                                    <input type="text" ng-model="MAIN_SELECT.attrs.top"/>
                                </div>
                            </div>
                            <div class="f-fix">
                                <label class="line-h-29">大小</label>
                                <div class="fl-r">
                                    <label class="c-label">宽</label>
                                    <input type="text" class="m-r-35" ng-model="MAIN_SELECT.attrs.width"/>
                                    <label class="c-label">高</label>
                                    <input type="text" ng-model="MAIN_SELECT.attrs.height"/>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- end样式 -->


                </section>
                <!-- end样式动画 -->

            </section>
            <!-- end元素 -->

            <section class="">

            </section>

        </div>
    </section>

</section>

<!-- 弹出层 -->
<!-- <section id="mask-layer" class="mask-layer" ng-show="masLayer" ng-click="mask('hide')">

    <section class="upload-pic" ng-click="$event.stopPropagation()">
        <header>
            <div style="height:25px;"></div>
            <div class="u-upload-btn">上传文件</div>
            <p class="u-upload-txt">图片不能超过1M</p>
        </header>
        <div class="upload-pic-main">
            <div class="upload-pic-nav fl-l">
                <ul>
                    <li class="on">我的图库</li>
                    <li>背景</li>
                    <li>边框</li>
                    <li>图标</li>
                    <li>功能</li>
                    <li>形状</li>
                    <li>其他</li>
                </ul>
            </div>
            <div class="upload-pic-img fl-l">
                <div class="imagePreviewContent">
                    <ul>
                        <li class="imgPreview" ng-repeat="data in dataPic"
                            ng-click="setselectDataPic($index,$event,element)">
                            <div class="dz-details">
                                <div class="previewImg" style="background-image:url({{data.src}})"></div>
                                <div class="delbtn"></div>
                            </div>
                            <div class="close"></div>
                        </li>
                    </ul>
                </div>
                <div class="previewFooterBg">
                    <span class="close-btn" ng-click="mask('hide')">取消</span>
                    <span class="confirm-btn" ng-click="addNewPic('img')">确定</span>
                </div>
            </div>
        </div>
    </section>

</section> -->
<!-- end弹出层 -->
<span class="confirm-btn" ng-click="addNewPic('img')"></span>
<!-- 取色器 背景 -->
<section class="color-picker" ng-show="bgBoard" style="top:{{colorBoardTop+30}}px">
    <div class="">
        <div id="picker"></div>
        <div style="height:20px;"></div>
        <div class="form-item">
            <div class="cp-color-warp">
                <div class="cpColor bg-color-show"></div>
                <input type="text" id="bg" class="bgcolor" ng-model="dataModuleBg[NAV_SELECT_index].background"/>
            </div>
        </div>
        <div ng-controller="farbtasticController"></div>
    </div>
</section>
<!-- end取色器 -->

<!-- 取色器 背景 -->
<section class="color-picker" ng-show="bghBoard" style="top:{{colorBoardTop+30}}px">
    <div class="">
        <div id="pickerh"></div>
        <div style="height:20px;"></div>
        <div class="form-item">
            <div class="cp-color-warp">
                <div class="cpColor bg-color-show"></div>
                <input type="text" id="bgh" class="bghcolor" ng-model="dataTransverse[NAV_SELECT_index].bg[dataTransverse_index].background"/>
            </div>
        </div>
    </div>
</section>
<!-- end取色器 -->

<!-- 取色器 文字颜色-->
<section class="color-picker" ng-show="colorBoard" style="top:{{colorBoardTop+30}}px">
    <div class="">
        <div id="picker_color"></div>
        <div style="height:20px;"></div>
        <div class="form-item">
            <div class="cp-color-warp">
                <div class="cpColor bg-color-show"></div>
                <input type="text" id="color" class="textcolor" ng-model="MAIN_SELECT.data.font_color"/>
            </div>
            <!--
            <div class="bg-color-input">
                <label>透明</label>
                <input type="text" class="opacity" ng-model="MAIN_SELECT.attrs.opacity" />
            </div>
            -->
        </div>
    </div>
</section>
<!-- end取色器 -->

<!-- 取色器 模块背景色-->
<section class="color-picker" ng-show="moduleBoard" style="top:{{colorBoardTop+30}}px">
    <div class="">
        <div id="picker_module"></div>
        <div style="height:20px;"></div>
        <div class="form-item">
            <div class="cp-color-warp" style="left:0">
                <div class="cpColor bg-color-show"></div>
                <input type="text" id="module" class="modulecolor" ng-model="MAIN_SELECT.bgcolor"/>
            </div>
            <div class="bg-color-input">
                <label>透明</label>
                <input type="text" class="opacity" ng-model="MAIN_SELECT.bgopacity"/>
            </div>
        </div>
    </div>
</section>
<!-- end取色器 -->

<!-- 选择动画 -->
<section class="u-dialog" data-ng-if="animateon">
    <header class="u-dialog-head">选择动画</header>
    <div class="f-fix u-dialog-body">
        <ul class="u-chooseList">
            <li data-ng-repeat="data in animatejsonon" data-ng-click="setanimatename($index)">
                <a href="javascript:void(0);" class="u-image-wrap">
                    <div class="u-image-large {{data[1]}}"></div>
                    <i class="icon-x22 icon-x22-tick-circle" data-ng-if="$index == animate_name[0]"></i>
                </a>
                <p>{{data[0]}}</p>
            </li>
        </ul>
    </div>
</section>
<!-- end选择动画 -->
<!-- 选择动画 -->
<section class="u-dialog" data-ng-if="animateout">
    <header class="u-dialog-head">选择动画</header>
    <div class="f-fix u-dialog-body">
        <ul class="u-chooseList">
            <li data-ng-repeat="data in animatejsonout" data-ng-click="setendanimatename($index)">
                <a href="javascript:void(0);" class="u-image-wrap">
                    <div class="u-image-large {{data[1]}}"></div>
                    <i class="icon-x22 icon-x22-tick-circle" data-ng-if="$index == end_animate_name[0]"></i>
                </a>
                <p>{{data[0]}}</p>
            </li>
        </ul>
    </div>
</section>
<!-- end选择动画 -->
<script>
    var dataJson = ${obj.dataJson};
    var dataBg = ${obj.dataBg};
    var dataTransverse = ${obj.dataTransverse};
    var dataPic = [];     //定义全局变量
    if (dataJson.length > dataTransverse.length) {
        dataJson.forEach(function (o, i) {
            if (i < dataTransverse.length)return;
            dataTransverse.push({
                "data": [],
                "bg": [{"background": "#fff", "slide": ""}],
                "attr": {
                    "width": 380,
                    "height": 500,
                    "top": 62,
                    "left": 0,
                },
                "dataTransverse_on": false
            })
        })
    }

</script>
<script>
    $(".u-chooseList-small").on("mouseenter", "a", function () {
        var span = $(this).find("span");
        span.css("animation", span.attr("data-animate") + " 1s");
    }).on("mouseleave", "a", function () {
        $(this).find("span").css("animation", "none");
    });

    $(".page").on("scroll", function () {
        var top = $(".page").scrollTop();
        $(".transverse-page").css("top", -top);
    }).on("mouseenter", ".c-ct", function () {
        var _this = $(this);
        $(".transverse-page .transverse-box").eq(_this.index()).addClass("left0");
    }).on("mouseleave", ".c-ct", function () {
        $(".left0").removeClass("left0");
    });

    $("#nav-btn-header").on("click", function () {
        var list = $(this).find(".u-toolBtn-list");
        if (list.is(":visible")) list.hide();
        else list.show();
    });
    //加载时加入想对应的数据
    $(function () {
        var musicurl = '${obj.musicurl}';
        var music_name = '${obj.musicname}';
        var player_style = '${obj.playerStyle}';
        var addres = ${obj.addres};
        $("#htmlmusicurl").val(musicurl);
        $("#player_style").val(player_style);
        $("#addres").val(addres);
        $("#htmlmusicname").val(music_name);
        //背景音乐表面的样式

        if (musicurl == '' || musicurl == null) {
            $(".music").attr("class", "music no")
        } else {
            $(".music-name").text(music_name);
        }
    })

</script>
</body>
</html>
