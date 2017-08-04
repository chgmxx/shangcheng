// JavaScript Document

angular.module('app', ['data','dragModule', 'dragPlan', 'colorTo', 'farbtastic', 'json', 'scalescreen', 'keydown', 'cutpic' , 'buttongo', 'music','scene','form','map'])
    .controller('adminController', ['$scope', '$timeout', 'colorToService', 'jsonService','Data', function ($scope, $timeout, colorToService, jsonService, Data) {
        //背景编辑显示状态
        $scope.showBg = true;
        //弹出层显示状态
        $scope.masLayer = false;
        //图片编辑显示状态
        $scope.showPic = false;
        //文本编辑显示状态
        $scope.showText = false;
        //按钮编辑显示状态
        $scope.showbtngo = false;
        //音频编辑显示状态
        $scope.showmusic = false;
        //表单编辑显示状态
        $scope.showform = false;
        //地图编辑显示状态
        $scope.showMap = false;
        //字体颜色取色器显示状态
        $scope.colorBoard = false;
        //背景取色器显示显示状态
        $scope.bgBoard = false;
        //模块背景取色器显示显示状态
        $scope.moduleBoard = false;
        //颜色板位置
        $scope.colorBoardTop = 0;
        //默认字体大小
        $scope.textSize = false;
        //默认字体间距
        $scope.textAlign = false;
        //默认行高
        $scope.textLine = false;
        //默认字体
        $scope.textName = false;
        //默认字体宽度
        $scope.textBold = false;
        //默认字体下横线
        $scope.textDecoration = false;
        //入场动画显示状态
        $scope.animateon = false;
        //出场动画显示状态
        $scope.animateout = false;
        //页面数据
        $scope.dataJson = Data.dataJson;
        //背景数据
        $scope.dataModuleBg = Data.dataModuleBg;
       /*  //数据库图片
        $scope.dataPic = Data.dataPic;*/
        //缩放比率
        $scope.scale = 1.0;
        //切图比率
        $scope.dataratio = 0;
        //适配
        $scope.scalestyle = "font-size:14px;margin-left:-250px;margin-top:-280px;width:380px;height:624px;";
        //字体大小
        $scope.font_size = Data.font_size;
        //字体行高
        $scope.font_line = Data.font_line;
        //储存字体
        $scope.font_name = Data.font_name;
        //储存选中图层样式
        var propertyAttrs = [];
        //复制内容
        $scope.copy = null;
        //入场动画
        $scope.animatejsonon = Data.animatejsonon;

        //出场动画
        $scope.animatejsonout = Data.animatejsonout;

        //翻页动画
        $scope.turnThePpage = Data.turnThePpage;
        $scope.animateturnThePpage = Data.animateturnThePpage;

        $scope.NAV_SELECT_index = 0;
        $scope.MAIN_SELECT_index = 0;
        $scope.NAV_SELECT = $scope.dataJson[0];
        $scope.MAIN_SELECT = null;
        //储存当前点击dom
        $scope.config_dom = null;
        //缓存上一次点击dom
        $scope.cacheDOM = null;
        //获取选择nav长度
        $scope.NAV_SELECT_length = $scope.NAV_SELECT.length;

        var selectDataPic = 0,  //储存选中的图片序号
            provenance = null; //储存点击出处

        for(var i=0; i<$scope.dataModuleBg.length; i++){
            if($scope.dataModuleBg[i] == "" || $scope.dataModuleBg[i] == null){
                $scope.dataModuleBg[i] = {};
            }
        }

        $scope.init = function () {
            $scope.showPic = false;
            $scope.showText = false;
            $scope.showbtngo = false;
            $scope.showmusic = false;
            $scope.showform = false;
            $scope.showMap = false;
            $scope.bgBoard = false;
            $scope.colorBoard = false;
            $scope.moduleBoard = false;
            $scope.textSize = false;
            $scope.textAlign = false;
            $scope.textLine = false;
            $scope.textName = false;
            $scope.animateon = false;
            $scope.animateout = false;
            $scope.animateway = true;
            $scope.animateclass = true;
            $scope.dataratio = 0;
        };

        $scope.init_extend = function () {
            $scope.bgBoard = false;
            $scope.colorBoard = false;
            $scope.moduleBoard = false;
            $scope.textSize = false;
            $scope.textAlign = false;
            $scope.textLine = false;
            $scope.textName = false;
            $scope.animateon = false;
            $scope.animateout = false;
        };

        $scope.updatedataJson = function(){
            $scope.dataJson = Data.dataJson;
        };

        /**
         *圆角判断
         */
        $scope.borderCircular = function (param) {
            if (param == 100) {
                return "100%"
            } else {
                return param + "px";
            }
        };

        /**
         * 文字判断
         */
        $scope.hasText = function (param) {
            if (param == "" || param == null) {
                return "请在右侧输入文字";
            } else {
                return param;
            }
        };

        /**
         * 切图对应显示范围
         */
            //$scope.cutratio = 258/data.data.width;
        $scope.setcutpicw = function (param1, param2, param3) {
            if (param2 == "" || param2 == "100%" || !param1 || param1 == "" || param1 == null) {
                return param3;
            } else {
                return param1 * param3 / param2;
            }
        };
        $scope.setcutpicxy = function (param1, param2, param3) {
            if (param2 == "" || param2 == "100%") {
                return param1;
            } else {
                return param1 * param3 / param2;
            }
        };

        /**
         * 锁定翻页
         */
        $scope.setlockslide = function(){
            if(!$scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata){
                $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata = {"slide":0,"lockslide":false};
            }
            $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata.lockslide = !$scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata.lockslide;
        };
        /**
         * 修改翻页动画
         */
        $scope.setturnThePpage = function(param){
            if(!$scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata){
                $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata = {"slide":0,"lockslide":false};
            }else{
                $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata.slide = param;
            }
            $(".allfy").removeClass("icon-on");
        };
        /**
         * 修改所有翻页动画
         */
        $scope.setAllTurnThePpage = function($event){
            try {
                var animate_page = $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata.slide || 0;
            }catch (err){
                var animate_page = 0;
            }
            $scope.dataModuleBg.forEach(function(o){
                if(o.bgdata){
                    o.bgdata.slide = animate_page;
                }else{
                    o.bgdata = {"slide":animate_page};
                }
            })
            $($event.currentTarget).addClass("icon-on");
        };

        $scope.getturnThePpage = function(){
            if(!$scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata){
                $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata = {"slide":0,"lockslide":false};
            }

            var page = $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata.slide;
            if(page == undefined || page == ""){
                $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata.slide = 0
            }
            if(arguments[1] == $scope.dataModuleBg[$scope.NAV_SELECT_index].bgdata.slide ){
                return arguments[0]+"2";
            }else{
                return arguments[0];
            }

        };

        /**
         * 开启微信二维码识别
         */
        $scope.setdiscern = function(){
            if($scope.MAIN_SELECT.discern){
                $scope.MAIN_SELECT.discern = false;
            }else{
                $scope.MAIN_SELECT.discern = true;
            }
        };

        /**
         * 修改切图比率
         */
        $scope.setratio = function (param) {
            var img = $(".jcrop-img");
            var width = img.width();
            var height = img.height();
            $scope.MAIN_SELECT.data.marginLeft = 0;
            $scope.MAIN_SELECT.data.marginTop = 0;
            $scope.MAIN_SELECT.data.width = $scope.MAIN_SELECT.attrs.width = width;
            if (param == 1) {
                if (height < width) {
                    $scope.MAIN_SELECT.data.width = $scope.MAIN_SELECT.attrs.width = $scope.MAIN_SELECT.data.height = $scope.MAIN_SELECT.attrs.height = height;
                } else {
                    $scope.MAIN_SELECT.data.height = $scope.MAIN_SELECT.attrs.height = width;
                }
            } else if (param == 2) {
                if (height < width * 3 / 4) {
                    $scope.MAIN_SELECT.data.width = $scope.MAIN_SELECT.attrs.width = height * 4 / 3;
                    $scope.MAIN_SELECT.data.height = $scope.MAIN_SELECT.attrs.height = height;
                } else {
                    $scope.MAIN_SELECT.data.height = $scope.MAIN_SELECT.attrs.height = width * 3 / 4;
                }
            } else if (param == 3) {
                if (height < width * 9 / 16) {
                    $scope.MAIN_SELECT.data.width = $scope.MAIN_SELECT.attrs.width = height * 16 / 9;
                    $scope.MAIN_SELECT.data.height = $scope.MAIN_SELECT.attrs.height = height;
                } else {
                    $scope.MAIN_SELECT.data.height = $scope.MAIN_SELECT.attrs.height = width * 9 / 16;
                }
            }

            $scope.dataratio = param;
        };
        $scope.dataratioclass = function (param) {
            if ($scope.dataratio == param)return true;
            else return false;
        };


        //入场动画配置
        var animate_style = function () {
            if (!$scope.MAIN_SELECT)return;
            $scope.animate_name = $scope.MAIN_SELECT.stratanimate;
            $scope.animate_starttime = $scope.MAIN_SELECT.stratanimate[2] || 0;
            $scope.animate_durationtime = $scope.MAIN_SELECT.stratanimate[3] || 1;
            $scope.animate_num = $scope.MAIN_SELECT.stratanimate[4] || 1;
        }

        //出场动画配置
        var endanimatestyle = function () {
            if (!$scope.MAIN_SELECT)return;
            $scope.end_animate_name = $scope.MAIN_SELECT.endanimate;
            $scope.end_animate_starttime = $scope.MAIN_SELECT.endanimate[2] || 0;
            $scope.end_animate_durationtime = $scope.MAIN_SELECT.endanimate[3] || 1;
            $scope.end_animate_num = $scope.MAIN_SELECT.endanimate[4] || 1;
        };

        //点击nav
        $scope.setNavSelectIndex = function (index) {
            this.init();
            $scope.NAV_SELECT_index = index;
            $scope.NAV_SELECT = $scope.dataJson[index];
            $scope.showBg = true;
            $scope.NAV_SELECT_length = $scope.NAV_SELECT.length;
            $(".c-ct").removeClass("c-ct-active").eq(index).addClass("c-ct-active");
            $(".allfy").removeClass("icon-on");
        };
        $scope.setMainSelectIndex = function (index) {
            this.init();
            $scope.MAIN_SELECT_index = index;
            $scope.MAIN_SELECT = $scope.NAV_SELECT[index];
            //$scope.setStyle($scope.config_dom);
            propertyAttrs = {
                css_width: $scope.MAIN_SELECT.attrs.width,
                css_height: $scope.MAIN_SELECT.attrs.height,
                css_top: $scope.MAIN_SELECT.attrs.top,
                css_left: $scope.MAIN_SELECT.attrs.left,
                css_opacity: $scope.MAIN_SELECT.attrs.opacity,
                css_zIndex: $scope.MAIN_SELECT.attrs.zIndex,
                css_rotate: $scope.MAIN_SELECT.attrs.rotate,
                css_borderRadius: $scope.MAIN_SELECT.attrs.borderRadius
            };
            $(".box-controller").removeClass("u-comChoose").eq($scope.MAIN_SELECT_index).addClass("u-comChoose");
        };

        //辨别板块
        $scope.havewhat = function (param) {
            if (!param)return;
            else return true;
        };

        //如果是背景
        $scope.showBgEdit = function () {
            this.init();
            $(".u-comChoose").removeClass("u-comChoose");
            $scope.showBg = true;
        };

        //如果是图片
        $scope.showImgEdit = function ($event, $index) {
            $scope.setMainSelectIndex($index);
            $scope.showBg = false;
            $scope.showPic = true;
            if ($event == "" || $event == null){
                $scope.config_dom = $("#editorFrame>div:last");
                 return false;
            };
            $event.stopPropagation();
        };
        //如果是文本
        $scope.showTextEdit = function ($event, $index) {
            $scope.setMainSelectIndex($index);
            $scope.showBg = false;
            $scope.showText = true;
            if ($scope.MAIN_SELECT.data.font_weight == "bold")$scope.init_bold = true;
            else $scope.init_bold = false;
            if ($scope.MAIN_SELECT.data.font_style == "italic")$scope.init_italic = true;
            else $scope.init_italic = false;
            if ($scope.MAIN_SELECT.data.text_decoration == "underline")$scope.init_decoration = true;
            else $scope.init_decoration = false;
            if ($event == "" || $event == null){
                $scope.config_dom = $("#editorFrame>div:last");
                return false;
            };
            $event.stopPropagation();
        };
          /*****************这里改了一点点********************/
        //如果是按钮
        $scope.showbtngoEdit = function($event,$index){
            $scope.setMainSelectIndex($index);
            $scope.showBg = false;
            $scope.showbtngo = true;
             $timeout(function(){
            	$("#zhuselect").append(option);
            })
            if ($event == "" || $event == null){
                $scope.config_dom = $("#editorFrame>div:last");
                return false;
            };
            $event.stopPropagation();
        };
         /*****************上面改了一点点********************/
        //如果是音频
        $scope.showmusicEdit = function($event,$index){
            $scope.setMainSelectIndex($index);
            $scope.showBg = false;
            $scope.showmusic = true;
            if ($event == "" || $event == null){
                $scope.config_dom = $("#editorFrame>div:last");
                return false;
            };
            $event.stopPropagation();
        };
        //如果是表单
        $scope.showformEdit = function($event,$index){
            $scope.setMainSelectIndex($index);
            $scope.showBg = false;
            $scope.showform = true;
            if ($event == "" || $event == null){
                $scope.config_dom = $("#editorFrame>div:last");
                return false;
            };
            $event.stopPropagation();
        };
        //如果是地图
        $scope.showmapEdit = function($event,$index){
            $scope.setMainSelectIndex($index);
            $scope.showBg = false;
            $scope.showMap = true;
            if ($event == "" || $event == null){
                $scope.config_dom = $("#editorFrame>div:last");
                return false;
            };
            $event.stopPropagation();
        };

        //弹出层显示隐藏
        $scope.mask = function (state) {
            if (state == 'show') {
                $scope.masLayer = true;
            } else {
                $scope.masLayer = false;
            }
        };

        /*******************************************************************************************************/
        //删除图层
        $scope.removeLayer = function ($event) {
            if (!this.showBg)jsonService._delete($scope.dataJson[$scope.NAV_SELECT_index], $scope.MAIN_SELECT_index);
            $timeout(function () {
                var obj = $(".box-controller");
                var len = obj.length;
                var domArray = [];
                var zIndexArray = [];
                var num = propertyAttrs.css_zIndex;
                $scope.NAV_SELECT_length = len;
                for (var i = 0; i < len; i++) {
                    domArray[i] = obj.eq(i);
                    zIndexArray[i] = parseInt(obj.eq(i).css("z-index"));
                }
                for (var i = 0; i < len; i++) {
                    if (zIndexArray[i] > num) {
                        $scope.NAV_SELECT[i].attrs.zIndex = zIndexArray[i] - 100;
                    }
                }
            })
        };
        //判断移动到顶层 移动到底层 显示隐藏  arguments
        $scope.moveLayerClass = function (arguments) {
            if ($scope.showBg)return true;
            var num = (propertyAttrs.css_zIndex - 10000) / 100;
            if (arguments == "next" && (this.showBg || num == 1))return true;
            if (arguments == "back" && (this.showBg || num >= this.NAV_SELECT_length))return true;
            else return false;
        };
        //移动图层
        $scope.moveLayer = function (arguments, $event) {
            if ($($event.currentTarget).hasClass("disable"))return;
            var obj = $(".box-controller");
            var len = obj.length;
            var domArray = [];
            var zIndexArray = [];
            var arguments = arguments;
            var num = propertyAttrs.css_zIndex;
            for (var i = 0; i < len; i++) {
                domArray[i] = obj.eq(i);
                zIndexArray[i] = parseInt(obj.eq(i).css("z-index"));
            }
            switch (arguments) {
                case 0:
                    for (var i = 0; i < len; i++) {
                        if (zIndexArray[i] > num) {
                            zIndexArray[i] -= 100;
                        }
                    }
                    zIndexArray[this.MAIN_SELECT_index] = propertyAttrs.css_zIndex = 10000 + 100 * len;

                    break;
                case 1:
                    for (var i = 0; i < len; i++) {
                        if (zIndexArray[i] == num + 100) {
                            zIndexArray[i] -= 100;
                            break;
                        }
                    }
                    zIndexArray[this.MAIN_SELECT_index] = propertyAttrs.css_zIndex = num + 100;

                    break;
                case 2:
                    for (var i = 0; i < len; i++) {
                        if (zIndexArray[i] < num) {
                            zIndexArray[i] += 100;
                        }
                    }
                    zIndexArray[this.MAIN_SELECT_index] = propertyAttrs.css_zIndex = 10000 + 100;
                    break;

                case 3:
                    for (var i = 0; i < len; i++) {
                        if (zIndexArray[i] == num - 100) {
                            zIndexArray[i] += 100;
                            break;
                        }
                    }
                    zIndexArray[this.MAIN_SELECT_index] = propertyAttrs.css_zIndex = num - 100;
                    break;
            }
            for (var i = 0; i < len; i++) {
                obj.eq(i).css("z-index", zIndexArray[i]);
                $scope.NAV_SELECT[i].attrs.zIndex = zIndexArray[i];
            }

            $event.stopPropagation();
        };
        /************************************************************************************************/
        /**
         * 添加地图
         */
        $scope.addmap = function(){
            var len = $scope.NAV_SELECT_length = $scope.dataJson[this.NAV_SELECT_index].length + 1;
            var num = len * 100 + 10000;
            $scope.dataJson[this.NAV_SELECT_index][len - 1] = {
                "style": "",
                "attrs": {
                    "width": 39,
                    "height": 56,
                    "top": 480,
                    "left": 168,
                    "borderRadius": 0,
                    "opacity": 100,
                    "zIndex": num,
                    "rotate": 0,
                    "cursor": "move"
                },
                "dataMap":{
                    "name":"谷通科技",
                    "phone":"400-889-4522",
                    "address":"广东省深圳市南山区兰光科技园",
                    "latlng":"22.55412,113.94172"
                },
                "scale": 1.0,
                "stratanimate": [],
                "endanimate": [],
                "animate": ""
            };
            $timeout(function () {
                $scope.showmapEdit("", len - 1);
            });
        };

        /**
         * 添加表单
         *[0,"姓名"],[1,"性别"],[2,"电话"],[3,"邮箱"],[4,"地址"],[5,"留言"],[6,"自定义"]
         */
        $scope.addform = function(){
            var len = $scope.NAV_SELECT_length = $scope.dataJson[this.NAV_SELECT_index].length + 1;
            var num = len * 100 + 10000;
            $scope.dataJson[this.NAV_SELECT_index][len - 1] = {
                "style": "",
                "attrs": {
                    "width": 380,
                    "height": 624,
                    "top": 0,
                    "left": 0,
                    "borderRadius": 0,
                    "opacity": 100,
                    "zIndex": num,
                    "rotate": 0,
                    "cursor": "move"
                },
                "bgcolor":"#000000",
                "bgopacity":50,
                "dataform":{
                    "submit":{
                        "bgcolor":"#1277e8",
                        "bgopacity":100,
                        "border":0,
                        "borcolor":"#cccccc",
                        "boropacity":100,
                        "borderRadius":10,
                        "val":"提交"
                    },
                    "input":{
                        "bgcolor":"#ffffff",
                        "bgopacity":100,
                        "border":0,
                        "borcolor":"#cccccc",
                        "boropacity":100,
                        "borderRadius":10,
                        "data":[[0,"姓名"],[1,"性别"],[2,"电话"],[3,"邮箱"]]
                    }
                },
                "scale": 1.0,
                "stratanimate": [],
                "endanimate": [],
                "animate": ""
            };
            $timeout(function () {
                $scope.showformEdit("", len - 1);
            });
        };

        /**
         * 添加按钮
         * 添加音乐
         */
        $scope.addmusic = function(){
            var len = $scope.NAV_SELECT_length = $scope.dataJson[this.NAV_SELECT_index].length + 1;
            var num = len * 100 + 10000;
            $scope.dataJson[this.NAV_SELECT_index][len - 1] = {
                "style": "",
                "attrs": {
                    "width": 90,
                    "height": 90,
                    "top": 119,
                    "left": 146,
                    "borderRadius": 0,
                    "opacity": 100,
                    "zIndex": num,
                    "rotate": 0,
                    "cursor": "move"
                },
                "bgcolor":"",
                "bgopacity":100,
                "music":false,
                "datamusic":{
                    "music":"",
                    "name":"",
                    "src":"/js/SceneEdit/images/audio-bg.png",
                    "page":1
                },
                "scale": 1.0,
                "stratanimate": [],
                "endanimate": [],
                "animate": ""
            };
            $timeout(function () {
                $scope.showmusicEdit("", len - 1);
            });
        };

        /**
         * 添加按钮
         * 内页跳转
         */
        $scope.addbutton = function(param){
            var len = $scope.NAV_SELECT_length = $scope.dataJson[this.NAV_SELECT_index].length + 1;
            var num = len * 100 + 10000;
            var val = "",src = "",color = "";
            switch (param)
            {
                case 1:
                    val = "点击打开";
                    src = "/js/SceneEdit/images/address.png";
                    color = "#2e2e2e";
                    break;
                case 2:
                    val = "拨打电话";
                    src = "/js/SceneEdit/images/phone.png";
                    color = "#16ba30";
                    break;
                case 3:
                    val = "点击跳转";
                    src = "/js/SceneEdit/images/link.png";
                    color = "#4a90e2";
                    break;
                case 4:
                    val = "联系QQ";
                    src = "/js/SceneEdit/images/QQ.png";
                    color = "#4a90e2";
                    break;
            }
            $scope.dataJson[this.NAV_SELECT_index][len - 1] = {
                "style": "",
                "attrs": {
                    "width": 120,
                    "height": 40,
                    "top": 543,
                    "left": 129,
                    "borderRadius": 99,
                    "opacity": 100,
                    "zIndex": num,
                    "rotate": 0,
                    "cursor": "move"
                },
                "dataText": {
                    "value":val,
                    "height": 40,
                    "bgcolor":color,
                    "bgopacity":90,
                    "font_size": 18,
                    "font_color": "#ffffff",
                    "font_align": "center",
                    "font_weight": "normal",
                    "font_style": "normal",
                    "text_decoration": "inherit"
                },
                "btnType": param,
                "viewType":false,
                "page":1,
                "url":"",
                "phone":"",
                "QQ":"",
                "bgcolor":color,
                "bgopacity":90,
                "dataPic":{
                    "height": 75,
                    "src":src,
                    "bgcolor":"",
                    "bgopacity":100,
                },
                "scale": 1.0,
                "stratanimate": [],
                "endanimate": [],
                "animate": ""
            };
            $scope.showbtngo = false;
            $timeout(function () {
                $scope.showbtngoEdit("", len - 1);
            });
        };
        //添加文本
        $scope.addNewText = function () {
            var len = $scope.NAV_SELECT_length = $scope.dataJson[this.NAV_SELECT_index].length + 1;
            var num = len * 100 + 10000;
            $scope.dataJson[this.NAV_SELECT_index][len - 1] = {
                "style": "",
                "attrs": {
                    "width": 252,
                    "height": 26,
                    "top": 37,
                    "left": 66,
                    "borderRadius": 0,
                    "opacity": 100,
                    "zIndex": num,
                    "rotate": 0,
                    "cursor": "move"
                },
                "bgcolor":"",
                "bgopacity":100,
                "text": "",
                "data": {
                    "font_size": 18,
                    "font_color": "#555555",
                    "font_align": "normal",
                    "lineHeight": "normal",
                    "font_name": "0",
                    "font_weight": "normal",
                    "font_style": "normal",
                    "text_decoration": "inherit"
                },
                "scale": 1.0,
                "stratanimate": [],
                "endanimate": [],
                "animate": ""
            };
            $timeout(function () {
                $scope.showTextEdit("", len - 1);
            });
        };

        //添加图片
        $scope.addNewPic = function () {
            var src = $("#imageurlst").val();
        	if(src==0){
    			alert("还未选择图片");
    			return;
    		}else{
    			src = src.trim();//去除空格
    		}
            if (provenance == 'pic') {
                var len = $scope.NAV_SELECT_length = $scope.dataJson[this.NAV_SELECT_index].length + 1;
                var num = len * 100 + 10000;
                var img = new Image();
                img.src = src;
                var imgW = img.width;
                if (imgW > 300) {
                    img.width = 300;
                    img.height = img.height * 300 / imgW;
                }
                $scope.dataJson[this.NAV_SELECT_index][len - 1] = {
                    style: "",
                    "attrs": {
                        "width": img.width,
                        "height": img.height,
                        "top": 5,
                        "left": -41,
                        "borderRadius": 0,
                        "opacity": 100,
                        "zIndex": num,
                        "rotate": 0,
                        "cursor": "move"
                    },
                    src: src,
                    "data": {
                        "width": "100%",
                        "height": "",
                        "marginLeft": 0,
                        "marginTop": 0,
                        "imgMaxW": 0,
                    },
                    "bgcolor":"",
                    "bgopacity":100,
                    "scale": 1.0,
                    "discern":false,
                    "stratanimate": [],
                    "endanimate": [],
                    "animate": ""
                };
                $timeout(function () {
                    $scope.showImgEdit("", len - 1);
                });
            } else if (provenance == 'bg') {
                $scope.dataModuleBg[this.NAV_SELECT_index] = {background: "url(" + src + ")"};
            } else if (provenance == 'img') {
                var img = new Image();
                img.src = src;
                var imgW = img.width;
                if (imgW > 300) {
                    img.width = 300;
                    img.height = img.height * 300 / imgW;
                }
                $scope.MAIN_SELECT.src = src;
                $scope.MAIN_SELECT.attrs.width = img.width;
                $scope.MAIN_SELECT.attrs.height = img.height;
                $scope.MAIN_SELECT.data = {"width": "100%","height": "","marginLeft": 0,"marginTop": 0,"imgMaxW": 0};
                $scope.showBg = true;
            }else if (provenance == 'btngo') {
                $scope.MAIN_SELECT.dataPic.src = src;
            }else if (provenance == 'music') {
                $scope.MAIN_SELECT.datamusic.src = src;
            }
            $scope.mask('hide');
        };

        //样式动画切换
        $scope.animateclass = true;
        $scope.setanimateclass = function (param) {
            if (param) {
                /* destroy_watch(); */
                $scope.animateclass = false;
                animate_style();
            } else {
                /*watchfc();*/
                $scope.animateclass = true;
            }
        };

        //判断有没有入场动画
        $scope.hasanimate = function () {
            if ($scope.animateclass)return;
            if (!$scope.MAIN_SELECT.stratanimate)return false;
            if ($scope.MAIN_SELECT.stratanimate.length > 1)return true;
            else return false;
        };

        //入场出场动画
        $scope.animateway = true;
        $scope.setanimateway = function (param, $event) {
            if (param) {
                endanimatestyle();
                $scope.animateway = false;
            } else {
                animate_style();
                $scope.animateway = true;
            }
            this.init_extend();
            $event.stopPropagation();
        };

        //显示选择动画
        $scope.setanimateon = function ($event) {
            $scope.animateon = !$scope.animateon;
            $event.stopPropagation();
        };

        //修改入场动画名称
        $scope.setanimatename = function (index) {
            if ($scope.MAIN_SELECT.stratanimate.length < 6) {
                $scope.MAIN_SELECT.stratanimate = [0, 0, 0, 1, 1, false];
                animate_style();
            }
            $scope.MAIN_SELECT.stratanimate[0] = index;
            $scope.MAIN_SELECT.stratanimate[1] = 0;
            this.init_extend();
            writeanimate();
        };

        //修改子类动画名称
        $scope.setanimatenamechild = function (index) {
            $scope.MAIN_SELECT.stratanimate[1] = index;
            writeanimate();
        };

        //判断无限循环
        $scope.getanimatefor = function () {
            if ($scope.animateclass)return;
            if (!$scope.MAIN_SELECT.stratanimate)return false;
            else return $scope.MAIN_SELECT.stratanimate[5] || false;
        };
        //修改无限循环
        $scope.setanimatefor = function () {
            $scope.MAIN_SELECT.stratanimate[5] = !$scope.MAIN_SELECT.stratanimate[5];
        };
        //删除动画
        $scope.removeanimate = function () {
            $scope.MAIN_SELECT.stratanimate = [];
            $scope.MAIN_SELECT.animate = "";
        };

        /**
         * 写入动画
         * stratanimate:[0,1,2,2,1,false]
         * ["淡入","anime-fadeIn",[["淡入","fadeIn"],["从上淡入","fadeInDown"],["从下淡入","fadeInUp"],["从左淡入","fadeInLeft"],["从右淡入","fadeInRight"]]]
         */
        var writeanimate = function () {
        	 var val = $scope.MAIN_SELECT.stratanimate;
             var name = $scope.animatejsonon[val[0]][2][val[1]][1];
             var str = name + " 1s";
             var dom = $scope.config_dom[0];
             dom.style.animation = "none";
             setTimeout(function(){
                 dom.style.animation = str;
                 dom.style.webkitAnimation = str;
             },100);
        };

        //出场动画
        $scope.setanimateout = function ($event) {
            $scope.animateout = !$scope.animateout;
            $event.stopPropagation();
        };
        //判断有没有出场动画
        $scope.hasendanimate = function () {
            if ($scope.animateclass)return;
            if (!$scope.MAIN_SELECT.endanimate)return false;
            if ($scope.MAIN_SELECT.endanimate.length > 1)return true;
            else return false;
        };

        //修改出场动画名称
        $scope.setendanimatename = function (index) {
            if ($scope.MAIN_SELECT.endanimate.length < 6) {
                $scope.MAIN_SELECT.endanimate = [0, 0, 0, 1, 1, false];
                endanimatestyle();
            }
            $scope.MAIN_SELECT.endanimate[0] = index;
            $scope.MAIN_SELECT.endanimate[1] = 0;
            this.init_extend();
            writeendanimate();
        };
        $scope.setendanimatenamechild = function (index) {
            $scope.MAIN_SELECT.endanimate[1] = index;
            writeendanimate();
        };
        //无限循环
        $scope.getendanimatefor = function () {
            if ($scope.animateclass)return;
            if (!$scope.MAIN_SELECT.endanimate)return false;
            else return $scope.MAIN_SELECT.endanimate[5] || false;
        };
        //修改无限循环
        $scope.setendanimatefor = function () {
            $scope.MAIN_SELECT.endanimate[5] = !$scope.MAIN_SELECT.endanimate[5];
        };
        //删除动画
        $scope.removeendanimate = function () {
            $scope.MAIN_SELECT.endanimate = [];
            $scope.MAIN_SELECT.animate02 = "";
            $scope.MAIN_SELECT.animate03 = "";
        };

        /**
         * 写入弹出动画
         * stratanimate:[0,1,2,2,1,false]
         * ["淡入","anime-fadeIn",[["淡入","fadeIn"],["从上淡入","fadeInDown"],["从下淡入","fadeInUp"],["从左淡入","fadeInLeft"],["从右淡入","fadeInRight"]]]
         */
        var writeendanimate = function () {
        	 var val = $scope.MAIN_SELECT.endanimate;
             var name = $scope.animatejsonout[val[0]][2][val[1]][1];
             var str = name + " 1s";
             var dom = $scope.config_dom.find(".autoWh")[0];
             dom.style.animation = "none";
             setTimeout(function(){
                 dom.style.animation = str;
                 dom.style.webkitAnimation = str;
             },100);
        };


        /************************************************************************************************/
        //编辑框计算高度
        $scope.countHeight = function (param) {
            if(param){
                $scope.MAIN_SELECT.text = $("#textBox").val();
            }

            $timeout(function(){
                $scope.MAIN_SELECT.attrs.height = $(".box-controller").eq($scope.MAIN_SELECT_index).find(".textbox").height() / $scope.scale;
            })
        };

        //取色板背景
        $scope.showHideBgColorBoard = function ($event) {
            if ($scope.bgBoard) {
                $scope.bgBoard = false;
                return;
            }
            $('#picker').farbtastic('#bg');
            setBoardposition($event);
            $scope.bgBoard = true;
            $timeout(function () {
                $("#bg").keyup();
            });
            $event.stopPropagation();
        };
        //取色板文字
        $scope.showHideTextColorBoard = function ($event) {
            if ($scope.colorBoard) {
                $scope.colorBoard = false;
                return;
            }
            setBoardposition($event);
            $scope.colorBoard = true;
            $timeout(function () {
                $("#color").keyup();
            });
            $event.stopPropagation();
        };
        //取色板模块背景
        $scope.showHidemoduleBoard = function ($event) {
            if ($scope.moduleBoard) {
                $scope.moduleBoard = false;
                return;
            }
            $('#picker_module').farbtastic('#module');
            setBoardposition($event);
            $scope.moduleBoard = true;
            $timeout(function () {
                $("#module").keyup();
            });
            $event.stopPropagation();
        };
        //设置取色板位置
        function setBoardposition($event){
            var Y = $event.pageY;
            var winh = $(window).height();
            var navh = $("header.g-nav").height();
            var boardh = 270;
            if( navh+Y+boardh > winh){
                if(Y-boardh-navh<0){
                    $scope.colorBoardTop = 118;
                }else{
                    $scope.colorBoardTop = Y-boardh-navh;
                }
            }else{
                $scope.colorBoardTop = Y;
            }
        }


        //选择颜色
        $scope.editBgColor = function (arguments) {
            $scope.dataModuleBg[$scope.NAV_SELECT_index].background = arguments;
        };
        $scope.showbgcolor = function(param1,param2,param3){
            if(param1 == "" || param1 == "none" || param1 == null || param2 == null)return "";
            else return param3 + "rgba(" + colorToService(param1) + "," + param2/100 + ");"
        };
        $scope.showbgcolorex = function(param){
            if(param == "" || param == "none" || param == null)return "background-image:url(images/bg-color-none.png)";
            else return "background-color:" + param;
        }

        //文本编辑器
        $scope.showDropText = function (arguments, $event) {
            this.init_extend();
            switch (arguments) {
                case 0:
                    $scope.textSize = true;
                    break;
                case 1:
                    $scope.textAlign = true;
                    break;
                case 2:
                    $scope.textLine = true;
                    break;
                case 3:
                    $scope.textName = true;
                    break;
            }
            $event.stopPropagation();
        };

        //改变字体粗细
        $scope.init_bold = false;
        $scope.set_f_bold = function () {
            $scope.init_bold = !$scope.init_bold;
            if ($scope.init_bold)$scope.MAIN_SELECT.data.font_weight = "bold";
            else $scope.MAIN_SELECT.data.font_weight = "normal";
        };

        //改变字体倾斜
        $scope.init_italic = false;
        $scope.set_f_italic = function () {
            $scope.init_italic = !$scope.init_italic;
            if ($scope.init_italic)$scope.MAIN_SELECT.data.font_style = "italic";
            else $scope.MAIN_SELECT.data.font_style = "normal";
        };

        //改变字体下横线
        $scope.init_decoration = false;
        $scope.set_f_decoration = function () {
            $scope.init_decoration = !$scope.init_decoration;
            if ($scope.init_decoration)$scope.MAIN_SELECT.data.text_decoration = "underline";
            else $scope.MAIN_SELECT.data.text_decoration = "inherit";
        };


        //默认值
        $scope.init_font = function (arguments) {
            if ($scope.showBg)return;
            if (arguments == $scope.MAIN_SELECT.data.font_size)return true;
            if (arguments == $scope.MAIN_SELECT.data.font_align)return true;
            if (arguments == $scope.MAIN_SELECT.data.font_name)return true;
            return false;
        };
        $scope.init_font_line = function(param){
            if ($scope.showBg)return;
            if (param == $scope.MAIN_SELECT.data.lineHeight)return true;
        };

        //设置样式
        var funChange = function (val, newValue) {
            //$($scope.config_dom).css(val,newValue+suffix);
            //val = newValue;
        };


        //选择字体大小
        $scope.setFontSize = function (arguments) {
            $scope.MAIN_SELECT.data.font_size = arguments;
            $scope.countHeight();
        };

        //选择字体左右居中
        $scope.setFontAlign = function (arguments) {
            $scope.MAIN_SELECT.data.font_align = arguments;
        };

        //选择字体行内间距
        $scope.setFontLine = function (arguments) {
            $scope.MAIN_SELECT.data.lineHeight = arguments;
            $scope.countHeight();
        };

        //选择字体
        $scope.setFontName = function (arguments) {
            $scope.MAIN_SELECT.data.font_name = arguments;
        };

        /*********************************************************************************/
        //copy一页
        $scope.copy_addpage = function(){
            jsonService._add($scope.dataJson, $scope.NAV_SELECT_index + 1);
            jsonService._add($scope.dataModuleBg, $scope.NAV_SELECT_index + 1,true);
            $scope.dataJson[$scope.NAV_SELECT_index + 1] = angular.fromJson(angular.toJson($scope.dataJson[$scope.NAV_SELECT_index]));
            $scope.dataModuleBg[$scope.NAV_SELECT_index + 1] = $scope.dataModuleBg[$scope.NAV_SELECT_index];
            $timeout(function(){
                $scope.setNavSelectIndex($scope.NAV_SELECT_index + 1);
            })
        };

        //新增一页
        $scope.addpage = function () {
            jsonService._add($scope.dataJson, $scope.NAV_SELECT_index + 1);
            jsonService._add($scope.dataModuleBg, $scope.NAV_SELECT_index + 1,true);
            $timeout(function(){
                $scope.setNavSelectIndex($scope.NAV_SELECT_index + 1);
            })
        };
        //删除一页
        $scope.removepage = function ($index) {
            if ($scope.dataJson.length <= 1)return;
            jsonService._delete($scope.dataJson, $index);
            jsonService._delete($scope.dataModuleBg, $index);
            $timeout(function(){
                if($index<$scope.NAV_SELECT_index){
                    $scope.NAV_SELECT_index --;
                }else if($index == $scope.NAV_SELECT_index){
                    if($index<$scope.dataJson.length)$scope.setNavSelectIndex($index);
                    else $scope.setNavSelectIndex($index-1);
                }
            })

        };
        //改变点击序号
        $scope.setselectDataPic = function (arguments, $event) {
            selectDataPic = arguments;
            $("#mask-layer .active").removeClass("active");
            $($event.currentTarget).addClass("active");
        };
        //改变出处
        $scope.setprovenance = function (arguments) {
            provenance = arguments;
            $scope.mask('show');
        };
        $scope.addPageHref = function(){
            if(!$scope.dataModuleBg[$scope.NAV_SELECT_index].href)$scope.dataModuleBg[$scope.NAV_SELECT_index].href = {"state":false,"val":""}
            $scope.dataModuleBg[$scope.NAV_SELECT_index].href.state = !$scope.dataModuleBg[$scope.NAV_SELECT_index].href.state;
        }

        //$http.jsonp("http://api.k780.com:88/?app=life.time&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json&jsoncallback=JSON_CALLBACK",{params:{name:'world'}}).success(function(data){console.log(data)});

        //保存数据
       /** $scope.save = function () {
            debugger;
        }*/

    }]);
