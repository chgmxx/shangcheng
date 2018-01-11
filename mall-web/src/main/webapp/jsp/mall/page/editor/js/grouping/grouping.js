/**
 * Created by boo on 2016/3/22.
 */

angular.module('grouping',[]).controller("groupingController",["$scope",function($scope){
	
    $scope.setpage = function(param){
        if($scope.dataedit.css>1 && param!=1){
            $scope.dataedit.css = 0;
        }
    };

    //显示添加商品弹框
    $scope.addCommodity = function(){
    	
    	$scope.addcommoditypic();
    };

    $scope.removeshop = function(param){
        $scope.picJson[$scope.pageEdit].imgID.removeAt(param);
    }

}])
.directive('groupingDraggable', ["$timeout",function ($timeout) {
    return {
        templateUrl: "js/grouping/groupingDirective.html",
        link: function(scope,element,attr){
        	
        }
    }
}]);
