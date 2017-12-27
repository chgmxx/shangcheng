/**
 * Created by boo on 2016/3/22.
 */

angular.module('coupon',[]).controller("couponController",["$scope",function($scope){
	
}])
.directive('couponDraggable', [function () {
    return {
        templateUrl: "js/coupon/couponDirective.html",
    }
}]);
