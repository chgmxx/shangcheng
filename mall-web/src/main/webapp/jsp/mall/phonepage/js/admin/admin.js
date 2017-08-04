/**
 * Created by boo on 2016/3/22.
 */

angular.module('admin',[]).controller('adminController',["$scope",function($scope){

    $scope.dataJson = dataJson;                 //样式数据
    $scope.picJson  = picJson;                  //图片编号数据
    $scope.message = function(param){
    	messageCli(param);
    }
    /**
     * 对比数据
     */
    $scope.complete=function(param){
    	if(parseInt(param/3) == param/3)return true;
    }
    
    $scope.pfLaye = function(param){
    	if(!param)return;
    	$(".pfj-main").html(param);
    	$(".pfj").show();
    }

}])
.directive('admindraggable', ["$timeout",function ($timeout) {
    return {
    	scope: true,
        restrict:"E",
        templateUrl: "js/admin/admin.html"
    }
}]);

