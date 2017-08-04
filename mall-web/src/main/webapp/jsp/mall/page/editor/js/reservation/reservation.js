/**
 * Created by boo on 2016/3/22.
 */

angular.module('reservation',[]).controller("reservationController",["$scope",function($scope){
    $scope.removeshop = function(param){
        $scope.picJson[$scope.pageEdit].imgID.removeAt(param);
    }
}])
.directive('reservationDraggable', ["$timeout",function ($timeout) {
    return {
        templateUrl: "js/reservation/reservationDirective.html",
        link: function(scope,element,attr){
        	$timeout(function(){
        		var start_index = 0;
        		new Sortable(element.find("ul")[0],{
        		    group: "name",
        		    store: null, // @see Store
        		    draggable: ".sort",   // 指定那些选项需要排序
        		    opacity: 0.6 ,
        		    onStart: function (/**Event*/evt) { // 拖拽
        		    	start_index = $(evt.item).index()-1;
        		    },
        		    onEnd: function (/**Event*/evt) { // 拖拽
        		        var itemEl = evt.item;
        		        scope.$apply(function(){
        		        	picJson[scope.pageEdit].imgID.changeAt(start_index,$(evt.item).index()-1);
        		        })
        		    }
        		});
        	})
        	
        }
    }
}]);
