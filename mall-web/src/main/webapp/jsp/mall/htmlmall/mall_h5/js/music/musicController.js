/**
 * Created by Administrator on 2016/1/12.
 */
angular.module('music', []).controller("musicController",["$scope",function($scope){
    $scope.data = $scope.MAIN_SELECT;

    $scope.setmusic = function(){
        $scope.data.music = !$scope.data.music;
    };
    $scope.musicpage = function(param){
        if($scope.data.datamusic.page == param)return true;
        else return false;
    };
    $scope.setmusicpage = function(param){
        if($scope.data.datamusic.page == param)return;
        $scope.data.datamusic.page = param;
    };
    $scope.hasmusic = function(param){
        if(param)return true;
        else return;
    };
    $scope.addMusic = function(){
        layer.open({
            type: 2,
            title: '音乐素材库',
            fix: false,
            shade : 0.2,
            shadeClose: false,
            closeBtn : 0,
            shift : 1,
            maxmin: false,
            area: ['550px', '520px'],//定义宽、高
            // content: '/common/musicmatre.do?musicscene=1',
            content: crossDomainUrl+"/common/musicmatre.do?musicscene=1&etUrl="+window.location.href

        });
    }
}])
.directive('musicDirective', function () {
    return{
        templateUrl: "js/music/music.html"
    }
});