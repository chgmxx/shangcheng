/**
 * Created by boo on 2016/3/22.
 */

angular.module('notice',[]).controller("noticeController",["$scope",function($scope){
	
}])
.directive('noticeDraggable', [function () {
    return {
        templateUrl: "js/notice/noticeDirective.html",
    }
}]);
