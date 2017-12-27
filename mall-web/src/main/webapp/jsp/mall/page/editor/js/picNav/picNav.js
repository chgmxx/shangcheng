/**
 * Created by boo on 2016/3/22.
 */

angular.module('picNav',[]).controller("picNavController",["$scope",function($scope){

    $scope.removeswiperpic = function(param){
        $scope.dataedit.pic.removeAt(param);
        $scope.picJson[$scope.pageEdit].imgID.removeAt(param);
    }
    $scope.removeswipername = function(param){
        $scope.picJson[$scope.pageEdit].imgID[param]="";
    }
    
    $scope.swiperuploadpic = function(param){
    	$scope.$parent.$parent.pic_index = param;
    }
    

}])
.directive('picNavDraggable', [function () {
    return {
        templateUrl: "js/picNav/picNavDirective.html",
    }
}]);
