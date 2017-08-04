/**
 * Created by Administrator on 2016/1/8.
 */
angular.module('buttongo', ['data']).controller("bottongoController",["$scope",'Data',function($scope,Data){
    $scope.data = $scope.MAIN_SELECT;
    $scope.font_size = Data.font_size;
    $scope.pages = [];
    $scope.textSize = false;
    $scope.textAlign = false;
    $scope.textqt = false;
    $scope.btncolorBoard = false;
    $scope.btnBoardTop = 0;
    $scope.title = ["","链接","拨打电话","跳转页面","QQ"];
    $scope.explain = ["","跳转至","电话号码","跳转至","联系QQ"];

    Data.dataJson.forEach(function(o,i){
        $scope.pages[i] = (i+1);
    });
    $scope.init_btn = function(){
        $scope.textSize = false;
        $scope.textAlign = false;
        $scope.btncolorBoard = false;
        $scope.textqt = false;
        $scope.init_extend();
    };
    $scope.btnType = function(param){
        var num = $scope.data.btnType;
        if(param == num)return true;
        else return;
    };
    $scope.setviewType = function(param){
        $scope.data.viewType = param;
        if(param){
            $scope.data.dataText.height = $scope.data.attrs.height;
            $scope.data.dataText.bgcolor = $scope.data.bgcolor;
            $scope.data.dataText.bgopacity = $scope.data.bgopacity;

            $scope.data.attrs.height = $scope.data.dataPic.height;
            $scope.data.bgcolor = $scope.data.dataPic.bgcolor;
            $scope.data.bgopacity = $scope.data.dataPic.bgopacity;
        }else{
            $scope.data.dataPic.height = $scope.data.attrs.height;
            $scope.data.dataPic.bgcolor = $scope.data.bgcolor;
            $scope.data.dataPic.bgopacity = $scope.data.bgopacity;

            $scope.data.attrs.height = $scope.data.dataText.height;
            $scope.data.bgcolor = $scope.data.dataText.bgcolor;
            $scope.data.bgopacity = $scope.data.dataText.bgopacity;
        }
    };
    //默认值
    $scope.init_font = function (arguments) {
        if ($scope.showBg)return;
        if (arguments == $scope.data.dataText.font_size)return true;
        if (arguments == $scope.data.dataText.font_align)return true;
        return false;
    };
    //显示颜色板
    $scope.setbtncolorBoard = function($event){
        this.init_btn();
        $scope.btncolorBoard = !$scope.btncolorBoard;
        $scope.btnBoardTop = $event.pageY;
        $('#picker').farbtastic('.btncolor');
        $event.stopPropagation();
    };
    //选择字体大小
    $scope.setFontSize = function (arguments) {
        $scope.data.dataText.font_size = arguments;
    };

    $scope.settextSize = function($event){
        this.init_btn();
        $scope.textSize = !$scope.textSize;
        $event.stopPropagation();
    };
    $scope.settextAlign = function($event){
        this.init_btn();
        $scope.textAlign = !$scope.textAlign;
        $event.stopPropagation();
    };

    //选择字体左右居中
    $scope.setFontAlign = function (arguments) {
        $scope.data.dataText.font_align = arguments;
    };

    $scope.settextqt = function($event){
        this.init_btn();
        $scope.textqt = !$scope.textqt;
        if ($scope.data.dataText.font_weight == "bold")$scope.init_bold = true;
        else $scope.init_bold = false;
        if ($scope.data.dataText.font_style == "italic")$scope.init_italic = true;
        else $scope.init_italic = false;
        if ($scope.data.dataText.text_decoration == "underline")$scope.init_decoration = true;
        else $scope.init_decoration = false;
        $event.stopPropagation();
    };

    //改变字体粗细
    $scope.set_f_bold = function () {
        $scope.init_bold = !$scope.init_bold;
        if ($scope.init_bold)$scope.data.dataText.font_weight = "bold";
        else $scope.data.dataText.font_weight = "normal";
    };

    //改变字体倾斜
    $scope.set_f_italic = function () {
        $scope.init_italic = !$scope.init_italic;
        if ($scope.init_italic)$scope.data.dataText.font_style = "italic";
        else $scope.data.dataText.font_style = "normal";
    };

    //改变字体下横线
    $scope.set_f_decoration = function () {
        $scope.init_decoration = !$scope.init_decoration;
        if ($scope.init_decoration)$scope.data.dataText.text_decoration = "underline";
        else $scope.data.dataText.text_decoration = "inherit";
    };


}])
.directive('bottongoDirective', function () {
    return{
        templateUrl: "js/SceneEdit/js/buttongo/buttongo.html"
    }
});