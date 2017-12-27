/**
 * Created by boo on 2016/3/22.
 */

angular.module('window',[]).controller("windowController",["$scope",function($scope){

	$scope.removeswiperpic = function(param){
        $scope.dataedit.list.splice(param,1)
        $scope.picJson[$scope.pageEdit].imgID.splice(param,1);
    }
    $scope.removeswipername = function(param){
        $scope.picJson[$scope.pageEdit].imgID[param]="";
    }
    
    $scope.swiperuploadpic = function(param){
    	$scope.$parent.$parent.pic_index = param;
    }
    
    $scope.addtextNav = function(){
    	$scope.dataedit.list.push({"title":""})
    	$scope.picJson[$scope.pageEdit].imgID.push({});
    }

}])
.directive('windowDraggable', [function () {
    return {
        templateUrl: "js/window/windowDirective.html",
    }
}]);
