/**
 * Created by boo on 2016/3/22.
 */

angular.module('header',[]).controller("headerController",["$scope",function($scope){
	
    $scope.setpage = function(param){
        if($scope.dataedit.css>1 && param!=1){
            $scope.dataedit.css = 0;
        }
    };

    //显示添加商品弹框
    $scope.addCommodity = function(){
        //$scope.shade.commodity_shade = true;
        //$scope.shade.shade = true;
    	
    	$scope.addcommoditypic();
    };

    $scope.removeshop = function(param){
        jsonService._delete($scope.picJson[$scope.pageEdit].imgID,param);
    }

}])
.directive('headerDraggable', ["$document",function ($document) {
    return {
        templateUrl: "js/header/headerDirective.html",
        link: function(scope, element, attr){
        	
        }
    }
}]);
