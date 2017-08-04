/**
 * Created by boo on 2016/3/22.
 */

angular.module('swiper',[]).controller("swiperController",["$scope",function($scope){

    $scope.removeswiperpic = function(param){
        $scope.dataedit.pic.removeAt(param);
        $scope.picJson[$scope.pageEdit].imgID.removeAt(param);
    }
    $scope.removeswipername = function(param){
        $scope.picJson[$scope.pageEdit].imgID[param]="";
    }
    
    $scope.swiperuploadpic = function(param){
    	$scope.pic_index = param;
    }
    

}])
.directive('swiperDraggable', [function () {
    return {
        templateUrl: "js/swiper/swiperDirective.html",
    }
}]);
